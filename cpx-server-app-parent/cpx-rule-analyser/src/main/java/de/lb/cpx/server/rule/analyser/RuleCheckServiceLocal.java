/*
 * Copyright (c) 2018 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2018  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.rule.analyser;

import de.checkpoint.drg.GrouperInterfaceBasic;
import de.checkpoint.ruleGrouper.CRGInputOutput;
import de.checkpoint.ruleGrouper.CRGRule;
import de.checkpoint.ruleGrouper.CRGRuleElement;
import de.checkpoint.ruleGrouper.CRGRuleGrouperManager;
import de.checkpoint.ruleGrouper.CRGRuleGrouperStatics;
import de.checkpoint.ruleGrouper.CheckpointRuleGrouper;
import de.checkpoint.ruleGrouper.cpx.CPXRuleGrouper;
import de.lb.cpx.grouper.model.transfer.InOutCreator;
import de.lb.cpx.grouper.model.transfer.Transfer4RuleAnalyse;
import de.lb.cpx.grouper.model.transfer.TransferBaseRate;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferRuleAnalyseResult;
import de.lb.cpx.grouper.model.transfer.TransferRuleResult;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.service.ejb.BaserateStore;
import de.lb.cpx.shared.rules.enums.CaseValidationGroupErrList;
import de.lb.cpx.shared.rules.util.CpxCRGRule;
import de.lb.cpx.shared.rules.util.RulesConverter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.w3c.dom.Element;

/**
 * Runtime check of rule created in the rule editor
 *
 * @author gerschmann
 */
@Stateless
//@Local
public class RuleCheckServiceLocal implements RuleCheckService {

    private static final Logger LOG = Logger.getLogger(RuleCheckServiceLocal.class.getName());

    private CheckpointRuleGrouper ruleGrouper;

    private RuleCheckManager ruleMgr;

//
    @Inject
    private BaserateStore baseRateStore;

    @PostConstruct
    @Override
    public void init() {
        try {
            ruleGrouper = new CPXRuleGrouper("", null, true, false);
            ruleGrouper.setGrouperModel(GrouperInterfaceBasic.AUTOMATIC);
            ruleGrouper.setIsDAK(0);

            ruleMgr = new RuleCheckManager();
            

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error on the initialising of RuleCheckService", ex);
        }
    }

    @Override
    public TransferRuleAnalyseResult checkRule4Case(Transfer4RuleAnalyse pTransfer) throws Exception {
        init();
        TransferRuleAnalyseResult retResult = new TransferRuleAnalyseResult();
        if (pTransfer.getTransferCase() == null) {
            return retResult;
        }
        retResult.setGroupErrorValidationList(pTransfer.getCaseValidationGroupErrList());
        List<TransferBaseRate> list = baseRateStore == null?new ArrayList<>():baseRateStore.getBaserate(pTransfer.getTransferCase());

        pTransfer.getTransferCase().setBaseRate(list);

//        CRGInputOutput inout = InOutCreator.createInOut(ruleGrouper.getInout(), pTransfer.getTransferCase());
        ruleMgr.fillRuleTables(pTransfer.getRuleTables());
//        checkBaserates(inout);
        CpxCRGRule cpxRule = getCpxRule(pTransfer.getRuleXmlString(), pTransfer.getRuleTablesId2NameMapping());
        if (cpxRule == null) {
            return retResult;
        }
        boolean hasSevere = CaseValidationGroupErrList.hasSevere(pTransfer.getCaseValidationGroupErrList());
        boolean checkHistory = !hasSevere && pTransfer.hasHistoryCases() && cpxRule.isM_hasKHCrit();
        CRGInputOutput inout = null;
        List<CRGInputOutput> hisInOutList = new ArrayList<>();
        if (!hasSevere) {

// we have to check history        
            if(checkHistory){
                for(TransferCase hisCase: pTransfer.getHistoryCases()){
                    hisCase.setBaseRate( baseRateStore == null?new ArrayList<>():baseRateStore.getBaserate(hisCase));
                    InOutCreator.createInOut(ruleGrouper.getInout(), hisCase);
                    ruleGrouper.performGroup();
                    CRGInputOutput ioHis = new CRGInputOutput();
                    ioHis.copyCase(ruleGrouper.getInout());     
                    hisInOutList.add(ioHis);
                }

            }
//mainCase
            inout = InOutCreator.createInOut(ruleGrouper.getInout(), pTransfer.getTransferCase());
            ruleGrouper.performGroup();
            InOutCreator.createGroupAnalyseResult(retResult, inout);
        }
        if(inout != null){
            
            if(checkHistory){
                for(int i = 0, len = hisInOutList.size(); i < len ; i++){
                    CRGInputOutput tmpInout = hisInOutList.get(i);

                    inout.setHistoryValues(tmpInout, i);

                }
                inout.sortHistoryCases();
                
            }
            ruleGrouper.setM_setCheckDateFromAdmDate(!cpxRule.isM_hasACGCrit());
            TransferRuleResult ruleRes = performRuleAnalyse(inout, cpxRule);
            if (ruleRes != null) {
                LOG.log(Level.INFO, ruleRes.toString());
            }
            if (!hasSevere && cpxRule.m_hasSuggestions) {
                double suggCW = ruleGrouper.performSuggestion(cpxRule, ruleGrouper.getInout());
                String drgSimul = ruleGrouper.getSuggInout().getDrg();
                if (ruleRes == null) {
                    LOG.log(Level.WARNING, "ruleRes is null!");
                } else {
                    CaseTypeEn caseType = CaseTypeEn.getValue2Id(inout.getIsDrgCase());
                    if (ruleGrouper.getSuggInout().getAdmissionReason12() == 4
                            || caseType.equals(CaseTypeEn.DRG)
                            && ruleGrouper.getSuggInout().getAdmissionReason12() == 3 && drgSimul != null && drgSimul.startsWith("-")) {
                        drgSimul = null;
                        suggCW = 0;
                    }
                    retResult.setSuggResults(suggCW, drgSimul);
                    LOG.log(Level.INFO, "suggCW = {0} deltaCW = {1}drgSimul = {2}", new Object[]{String.valueOf(suggCW), String.valueOf(retResult.getDeltaCw()), drgSimul == null ? "null" : drgSimul});
                }
            }

            retResult.setRuleResult(ruleRes);
        }
        return retResult;
    }

    /**
     * Goes thruogh the rule tree and returns true/false result for each term
     * which has "mark" - attribute
     *
     * @param root
     * @param parent
     * @throws Exception
     */
    private void analyse(CRGRuleElement root, TransferRuleResult parent, CRGInputOutput inout) throws Exception {
        if (root == null) {
            return;
        }
        CRGRuleElement[] children = root.m_childElements;
        if (children == null) {
            if (root.m_type == CRGRuleGrouperManager.TYPE_VALUE) {
                addAnalyseResultValue(root, parent, inout);
            }
            return;
        }
        for (CRGRuleElement child : children) {
            if (child.m_type == CRGRuleGrouperManager.TYPE_VALUE && child.m_operantType <= 0) {
                parent.addChild(new TransferRuleResult(child.m_mark, child.toString(), parent.isResult()));
                continue;
            }
            CRGRuleElement newCheck = new CRGRuleElement(null);
            String term = child.toString();
            if (child.m_type == CRGRuleGrouperManager.TYPE_OPERATOR && children.length > 1) {
                parent.addChild(new TransferRuleResult(child.m_mark, child.toString(), parent.isResult()));
                continue;

            } else if (child.m_type == CRGRuleGrouperManager.TYPE_VALUE) {
// wenn es math operation- übernehmen resultat von parent     
                if (CRGRuleGrouperManager.isDashOperation(child.m_operantType)
                        || CRGRuleGrouperManager.isDotOperation(child.m_operantType)) {
                    TransferRuleResult oneRes = new TransferRuleResult(child.m_mark, child.toString(), parent.isResult());
                    parent.addChild(oneRes);
//                        analyse(child, oneRes, inout);
                    continue;
                }
//                boolean res = ruleGrouper.checkTermRef4Rule(child, inout);
//                parent.addChild(new TransferRuleResult(child.m_mark, child.toString(), res));
                addAnalyseResultValue(child, parent, inout);
                continue;
            } else {
                newCheck = child;
                if (newCheck.m_childElements != null && newCheck.m_childElements.length == 3) {
// check, whether it is a compute term
                    if (CRGRuleGrouperManager.isDashOperation(newCheck.m_childElements[1].m_operantType)
                            || CRGRuleGrouperManager.isDotOperation(newCheck.m_childElements[1].m_operantType)) {
                        TransferRuleResult oneRes = new TransferRuleResult(child.m_mark, child.toString(), parent.isResult());
                        parent.addChild(oneRes);
                        analyse(newCheck, oneRes, inout);
                        continue;

                    } else {
                        boolean res = ruleGrouper.checkTermRef4Rule(newCheck, inout);
                        TransferRuleResult newCheckRes = new TransferRuleResult(newCheck.m_mark, newCheck.toString(), res, res ? ruleGrouper.getCodeReferencesForRule(0) : null);
                        parent.addChild(newCheckRes);
                        if (CRGRuleGrouperManager.isOperatorCompare(newCheck.m_childElements[1].m_operantType)) {
// all three children have the same result as the newCheck      
                            TransferRuleResult newCheckChild0 = new TransferRuleResult(newCheck.m_mark, newCheck.m_childElements[0].toString(), res, res ? ruleGrouper.getCodeReferencesForRule(0) : null);
                            newCheckRes.addChild(newCheckChild0);
                            TransferRuleResult newCheckChild1 = new TransferRuleResult(newCheck.m_mark, newCheck.m_childElements[1].toString(), res, res ? ruleGrouper.getCodeReferencesForRule(0) : null);
                            newCheckRes.addChild(newCheckChild1);
                            TransferRuleResult newCheckChild2 = new TransferRuleResult(newCheck.m_mark, newCheck.m_childElements[2].toString(), res, res ? ruleGrouper.getCodeReferencesForRule(0) : null);
                            newCheckRes.addChild(newCheckChild2);

                            continue;
                        } else if (newCheck.m_childElements[1].m_operantType == CRGRuleGrouperStatics.OP_AND
                                || newCheck.m_childElements[1].m_operantType == CRGRuleGrouperStatics.OP_OR) {
// the operation has the same result as newCheck, both member elements are to be checked separately
                            analyse(newCheck.m_childElements[0], newCheckRes, inout);
                            newCheckRes.addChild(new TransferRuleResult(newCheck.m_mark, newCheck.m_childElements[1].toString(), res, res ? ruleGrouper.getCodeReferencesForRule(0) : null));
                            analyse(newCheck.m_childElements[2], newCheckRes, inout);
                            continue;
                        }
                    }
                }
            }
            boolean res = ruleGrouper.checkTermRef4Rule(newCheck, inout);

            TransferRuleResult oneRes = new TransferRuleResult(newCheck.m_mark, term, res, res ? ruleGrouper.getCodeReferencesForRule(0) : null);
            //AWI-20190605: Remove mark check, due to i did not care at this point
            if (newCheck.m_mark != null //                   && !child.m_mark.isEmpty()
                    ) {
                parent.addChild(oneRes);
            } else {
                oneRes = parent;
            }
            analyse(newCheck, oneRes, inout);
        }

    }

    private void addAnalyseResultValue(CRGRuleElement child, TransferRuleResult parent, CRGInputOutput inout) throws Exception {
// wenn es math operation- übernehmen resultat von parent     
//        if(CRGRuleGrouperManager.isDashOperation(child.m_operantType)
//               || CRGRuleGrouperManager.isDotOperation(child.m_operantType)){
//               TransferRuleResult oneRes = new TransferRuleResult(child.m_mark, child.toString(), parent.isResult());
//                parent.addChild(oneRes);
//                analyse(child, oneRes, inout);
//
//        }else{
        CRGRuleElement temp = new CRGRuleElement(null);
        temp.m_childElements = new CRGRuleElement[1];
        temp.m_childElements[0] = child;
        temp.m_childCount = 1;
//            if((CRGRuleGrouperManager.isDashOperation(child.m_operantType)
//                       || CRGRuleGrouperManager.isDotOperation(child.m_operantType) && child.m_criterionIndex > 0){
        boolean res = ruleGrouper.checkTermRef4Rule(temp, inout);
        parent.addChild(new TransferRuleResult(child.m_mark, child.toString(), res, res ? ruleGrouper.getCodeReferencesForRule(0) : null));
//        }

    }

    /**
     * applies rule to the given case configuration and returns ther result of
     * validation for each rule term
     *
     */
    private TransferRuleResult performRuleAnalyse(CRGInputOutput inout, CpxCRGRule cpxRule) {
        try {
            CRGRuleElement root = cpxRule.getRuleElement();

            boolean res = ruleGrouper.checkTermRef4Rule(root, inout);
            TransferRuleResult oneRes = new TransferRuleResult("0", root.toString(), res, res ? ruleGrouper.getCodeReferencesForRule(0) : null);
            analyse(root, oneRes, inout);
            return oneRes;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, " Error by rule analyse", ex);
            return null;
        }
    }

    /**
     * creates CPXCRGRule from xml - notation
     *
     * @param ruleAsXml
     * @return
     */
    private CpxCRGRule getCpxRule(String ruleAsXml, Map<String, String> pTableId2NameMapping) {
        try {
            if (ruleAsXml == null || ruleAsXml.isEmpty()) {
                return null;
            }

            byte[] check = ruleAsXml.getBytes("UTF-16");
            Element e = RulesConverter.getRuleDomElementFromDbString(check);
            return new CpxCRGRule(ruleMgr, e, null, null, CRGRule.RULE_ANALYSE_FULL, pTableId2NameMapping); // hier muss isServer = true gesetzt werden, sonst rule.getRuleElement() = null
        } catch (UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, "Error by creating of check configuration for xml rule", ex);
            return null;
        }
    }

//    private void checkBaserates(CRGInputOutput inout) throws Exception{
//        final long startTime = System.currentTimeMillis();
//        final Date admissionDate = inout.getAdmissionDate() == null ? null : new Date( inout.getAdmissionDate().getTime());
//        final Date dischargeDate = inout.getDischargeDate() == null ? null : new Date(inout.getDischargeDate().getTime());
//        if(inout.getIkz() == null){
//            return;
//        }
//        if (inout.getIsDrgCase() == CaseTypeEn.PEPP.getId()) {
//            if (admissionDate != null  && dischargeDate != null) {
//                List<CBaserate> baseRates = baserateDao.findPeppBaserates4Case(inout.getIkz(),
//                        CountryEn.de.name(),
//                        admissionDate, dischargeDate);
//
//                if (baseRates != null && !baseRates.isEmpty()) {
//                    inout.setBaseRate(baseRates.get(0).getBaseFeeValue());
//                    inout.setCheckBaserates(1);
//                    for (CBaserate br : baseRates) {
//                        if (br.getBaseValidFrom()== null) {
//                            LOG.log(Level.WARNING, "Baserate has ot valid from: " + br.getBaseValidFrom());
//                            continue;
//                        }
//                        if (br.getBaseValidTo()== null) {
//                            LOG.log(Level.WARNING, "Baserate has ot valid to: " + br.getBaseValidTo());
//                            continue;
//                        }
//                        Date from = br.getBaseValidFrom().before(admissionDate)?admissionDate:br.getBaseValidFrom();
//                        Date to = br.getBaseValidTo().after(dischargeDate)?dischargeDate:br.getBaseValidTo();
//                        inout.addOneBaserate(from, to, br.getBaseFeeValue());
//                    }
//
//                } else {
//                    // default baserate
//                    double br = baserateDao.getDefaultPeppBaserate(inout.getAdmissionReason12());
//                    inout.setBaseRate(br);
//                    inout.addOneBaserate(admissionDate, dischargeDate, br);
//                }
//            }
//        } else {
//            double br = baserateDao.findDrgBaserateFeeValue(inout.getIkz(),
//                    admissionDate, CountryEn.de.name());
//            if (Double.doubleToRawLongBits(br) == Double.doubleToRawLongBits(0.0d)) {
//                // default baserate
//                br = baserateDao.getDefaultDrgBaserate();
//            }
//            inout.setBaseRate(br);
//        }
//     LOG.log(Level.INFO, "checkBaserates: {0} ms", System.currentTimeMillis() - startTime);
//    }    
}
