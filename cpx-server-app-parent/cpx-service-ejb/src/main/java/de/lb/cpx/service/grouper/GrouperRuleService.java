/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.grouper;

import de.checkpoint.drg.DrgEntryZusatz;
import de.checkpoint.drg.GDRGModel;
import de.checkpoint.drg.GrouperInterfaceBasic;
import de.checkpoint.drg.RulerInputObjectNull;
import de.checkpoint.drg.RulesRefOut;
import de.checkpoint.drg.ZusatzIF;
import de.checkpoint.javaGrouper.JavaGrouperInterface;
import de.checkpoint.ruleGrouper.CRGInputOutput;
import de.checkpoint.ruleGrouper.CRGRule;
import de.checkpoint.ruleGrouper.cpx.CPXRuleGrouper;
import de.checkpoint.utils.UtlDRGListStringManager;
import de.checkpoint.utils.UtlDateTimeConverter;
import de.checkpoint.utils.UtlPEPPListStringManager;
import de.lb.cpx.grouper.model.dto.CommunicationErrorObject;
import de.lb.cpx.grouper.model.dto.GrouperResponseObject;
import de.lb.cpx.grouper.model.enums.ErrorCause;
import de.lb.cpx.grouper.model.transfer.BatchGroupParameter;
import de.lb.cpx.grouper.model.transfer.GrouperPerformStatistic;
import de.lb.cpx.grouper.model.transfer.InOutCreator;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferPatient;
import de.lb.cpx.grouper.model.transfer.TransferRule;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wilde
 */
public class GrouperRuleService {

    private static final Logger LOG = Logger.getLogger(GrouperRuleService.class.getName());

    private CPXRuleGrouper mRuleCRG;
    private Map<String, String> mRuleTypes2Ids = new HashMap<>();

    /**
     * initialisies the CPXRuleGrouper with path to rules and catalogs on the
     * file system
     *
     * @param catalogPath path to catalog
     * @param rules rules used for rules check
     *
     */
    public GrouperRuleService(String catalogPath, CRGRule[] rules) {
        try {
            initGrouper(catalogPath, rules);

        } catch (Exception ex) {
            Logger.getLogger(GrouperRuleService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initGrouper(String catalogPath, CRGRule[] rules) throws Exception {

        mRuleCRG = new CPXRuleGrouper(catalogPath, rules, true, false);
        mRuleCRG.setGrouperModel(GrouperInterfaceBasic.AUTOMATIC);
        mRuleCRG.setIsDAK(1); //for admission mode = 8 to calculate 80%
        mRuleCRG.setM_setCheckDateFromAdmDate(true);// for history cases, where checkdate is from admission/discharge date, for acg is to set false

    }

    private Object createErrorObject(ErrorCause cause, String errorText) {
        return new CommunicationErrorObject(cause, errorText);
    }

    /**
     * a hospital case from requestObject will be only grouped, rules are not
     * applied
     *
     * @param requestObject Dto Case Object
     * @return grouperresponse Object
     */
    public Object performGroup(TransferCase requestObject) {
        try {

            InOutCreator.createInOut(mRuleCRG.getInout(), requestObject);

// initializing was performed with parameter, we use rules from path               
            mRuleCRG.setGrouperModel(requestObject.getGrouperModelId());

            return groupAndCalculateAdditionalFees(requestObject);
        } catch (Exception ex) {
            Logger.getLogger(GrouperRuleService.class.getName()).log(Level.SEVERE, null, ex);
            return createErrorObject(ErrorCause.EXCEPTION, ex.toString());
        }
    }

    /**
     * a hospital case from requestObject will be gegrouped and the rules will
     * be applied
     *
     * @param requestObject Dto Case Object
     * @return grouper response Object
     */
    public Object performGroupAndCheck(TransferCase requestObject) {
        return performGroupAndCheck(requestObject, null);
    }

    /**
     * a hospital case from requestObject will be gegrouped and the rules will
     * be applied
     *
     * @param requestObject Dto Case Object
     * @param statistic for gathering of statistics or null
     * @return grouper response Object
     */
    public Object performGroupAndCheck(TransferCase requestObject, GrouperPerformStatistic statistic) {
        try {
            long timeStart = System.currentTimeMillis();
            //long timeTotal = timeStart;
            InOutCreator.createInOut(mRuleCRG.getInout(), requestObject);
            if (statistic != null) {
                statistic.addTime4FillInOutCreator(System.currentTimeMillis() - timeStart);
                //timeStart = System.currentTimeMillis();
            }
            mRuleCRG.setGrouperModel(requestObject.getGrouperModelId());

            return performGroupAndCheckOnly(requestObject, statistic);

        } catch (Exception ex) {
            Logger.getLogger(GrouperRuleService.class.getName()).log(Level.SEVERE, null, ex);
            return createErrorObject(ErrorCause.EXCEPTION, ex.toString());
        }
    }

    private Object performGroupAndCheckOnly(TransferCase requestObject, GrouperPerformStatistic statistic) {
        long timeStart = System.currentTimeMillis();
        long timeTotal = timeStart;
        try {
            GrouperResponseObject response = groupAndCalculateAdditionalFees(requestObject);
            // the simulated values are to save in responce object now, because they will be overritten with values from simulating of rules
            if (statistic != null) {
                statistic.addTime4groupAndCalculateAdditionalFees(System.currentTimeMillis() - timeStart);
                timeStart = System.currentTimeMillis();
            }
            if (!response.isVwdValid()) {
                return response;
            }
            CRGRule[] rules = mRuleCRG.performCheckRef(mRuleCRG.getInout(), requestObject.getDateOfAdmission(), requestObject.getRoleIdsArray());
            if (statistic != null) {
                statistic.addTime4performCheckRef(System.currentTimeMillis() - timeStart);
                timeStart = System.currentTimeMillis();
            }
            //TODO: references for each rule m_ruleCRG.setCodeReferences(chk, j);

            simulateRules(rules, mRuleCRG.getInout(), response, requestObject);
            if (statistic != null) {
                statistic.addTime4simulateRules(System.currentTimeMillis() - timeStart);
                statistic.addTime4performGroupAndCheck(System.currentTimeMillis() - timeTotal);
            }
            return response;

        } catch (Exception ex) {
            Logger.getLogger(GrouperRuleService.class.getName()).log(Level.SEVERE, null, ex);
            return createErrorObject(ErrorCause.EXCEPTION, ex.toString());
        }

    }

    /**
     * Applies the detected rules to the case and saves the results of it in the
     * responce object
     *
     * @param rules Array of GrouperRules
     * @param responce Grouped Case
     * @param orgCW some CostWeight Value
     */
    private void simulateRules(CRGRule[] rules, CRGInputOutput inout, GrouperResponseObject responce, TransferCase requestObject) {
        try {

            if (rules != null) {
                int i = 0;

                for (CRGRule rule : rules) {
                    RulesRefOut ref = null;
                    TransferRule createdRule;
                    if (requestObject.doSimulateRules()) {
                        ref = mRuleCRG.getCodeReferences(i);
                    }
                    double supplFees = 0;
                    if (requestObject.doSimulate() || requestObject.doSimulateRules()) {

                        if (rule.m_hasSuggestions) {
                            mRuleCRG.performSuggestion(rule, inout);
                            // check only when case was checked for supplementary fees 
                            if(requestObject.doSupplementaryFees()) {
                                if(requestObject.doSupplementaryFees()){
                                    supplFees = calculateSupplementaryFees(mRuleCRG.getSuggInout(), null);
                                }
                            }
                            createdRule = InOutCreator.createNewDetectedRule(rule, inout, mRuleCRG.getSuggInout(), requestObject.getRoleIds(), mRuleTypes2Ids, responce.getSupplFeeSum(), supplFees);
                            
                        } else {
                            createdRule = InOutCreator.createNewDetectedRule(rule, inout, null, requestObject.getRoleIds(), mRuleTypes2Ids, responce.getSupplFeeSum(), responce.getSupplFeeSum()); 
                        }

                    } else {
                        createdRule = InOutCreator.createNewDetectedRule(rule, null, null, requestObject.getRoleIds(), mRuleTypes2Ids, responce.getSupplFeeSum(), responce.getSupplFeeSum());

                    }
                    createdRule.setRuleReferences(ref);

                    createdRule.setRuleRisks(rule.getRuleRisks(), !rule.m_hasSuggestions);
                    responce.addRule(createdRule);
                    i++;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(GrouperRuleService.class.getName()).log(Level.WARNING, "Error on rules simulating", ex);
        }

    }

    /**
     * performs grouping and calculates supplementary fee for each ops
     *
     * @throws Exception Exception thrown when something happend in Grouping
     * Process
     */
    private GrouperResponseObject groupAndCalculateAdditionalFees(TransferCase requestObject) throws Exception {
        boolean vwdValid = performGroupOnly(requestObject);
        GrouperResponseObject responce = new GrouperResponseObject(InOutCreator.createResult4Case(mRuleCRG.getInout(), requestObject), InOutCreator.createBatchResult4Case(mRuleCRG.getInout(), requestObject.getAux9count()));
        if (vwdValid && !InOutCreator.isGroupResultNotValid(mRuleCRG.getInout()) && requestObject.doSupplementaryFees()) {
            calculateSupplementaryFees(mRuleCRG.getInout(), responce);
        }
        responce.setVwdValid(vwdValid);
//    Logger.getLogger(GrouperRuleService.class.getName()).log(Level.INFO, m_ruleCRG.getInout().dumpRecord().toString());    
        return responce;
    }

    /**
     * performs the grouping action for TransferCase after ist values were set
     * in mRuleCRG.inout object
     *
     * @param requestObject TransferCase
     * @return returns the result of checking of the LOS validation
     * @throws Exception
     */
    private boolean performGroupOnly(TransferCase requestObject) throws Exception {
        return performGroupOnly(requestObject, requestObject.doSimulate());
    }

    /**
     * performs the grouping action for TransferCase after ist values were set
     * in mRuleCRG.inout object
     *
     * @param requestObject TransferCase
     * @return returns the result of checking of the LOS validation
     * @throws Exception
     */
    private boolean performGroupOnly(TransferCase requestObject, boolean doSimulate) throws Exception {
        boolean vwdValid = InOutCreator.checkVwdValid(mRuleCRG.getInout());
        if (vwdValid) {
            if (doSimulate) {
                mRuleCRG.performGroupSimulate();
            } else {
                //LOG.log(Level.FINE, "InOut object dump record:\n" + String.valueOf(m_ruleCRG.getInout().dumpRecord()));    
                mRuleCRG.performGroup();
            }
        } else {
            InOutCreator.fillDefaults(mRuleCRG.getInout());
            mRuleCRG.getInout().setLengthOfStay(0);
        }

        return vwdValid;
    }

    /**
     * calculates supplementary fee for each ops and gets evt. calculated ETs
     */
    private double calculateSupplementaryFees(CRGInputOutput inout, GrouperResponseObject responce) throws Exception {
        double supplFeeSum = 0;
        List<String> procs = Arrays.asList(inout.getProcedures());
        List<String> diags = Arrays.asList(inout.getDDX());
        int[] procIds = inout.getOPSIds();
        String[] etCodes = inout.getETCodes();
        int[] etCounts = inout.getETCounts();
        Date[] etStarts = inout.getETStarts();
        Date[] etEnds = inout.getETEnds();
        double[] values = inout.getETValues();
        double[] cws = inout.getETCWs();
        int i = 0;
        int drgVersion = inout.getDRGVersion(); //  modelID of the used Grouper
        boolean doEts = (inout.getDRGVersion() > GrouperInterfaceBasic.GDRG2014
                || inout.getDRGVersion() > GrouperInterfaceBasic.GDRG_DEST2014)
                && inout.getIsDrgCase() == CaseTypeEn.PEPP.getId() && etCodes != null && etCodes.length > 0;

        int catId = GDRGModel.getModel(drgVersion).getCatalogVersion(); // catalogId of the used Grouper
        if (catId == 0 && !procs.isEmpty()) {
            LOG.log(Level.WARNING, "catId is 0 because drgVersion is 0! {0} grouper ist not supported...", drgVersion);
            return 0;
        }
        for (String code : procs) {
            if(procIds.length > i){
                supplFeeSum += calculateSupplFee2Ops(responce, procIds[i], inout.getAdmissionYear(), code,
                        inout.getIkz(), inout.getAgeY(), procs,
                        diags, inout.getIsDrgCase() - 1, catId, inout.getDrg(), inout.getAdmissionDate(), inout.getLengthOfStay());

                if (doEts && etCodes != null && etCodes.length > i &&(etCodes[i] != null && !etCodes[i].isEmpty())) {
                    if(responce != null && etCounts.length > i && cws.length > i && values.length > i&& etStarts.length > i && etEnds.length > i){
                        
                        responce.addAdditionalFee2Ops(SupplFeeTypeEn.ET.getId(), procIds[i], etCodes[i], 
                                etCounts[i], cws[i], values[i], etStarts[i], etEnds[i]);
                    }
                    supplFeeSum += values.length > i?values[i]:0;
                }
            }else{
                break;
            }
            i++;
        }
        return supplFeeSum;
    }

    private double calculateSupplFee2Ops(GrouperResponseObject responce, int procId, int admYear,
            String proc, String ikz, int ageY, List<String> procs, List<String> diags, int grouperType, int catId, String drg, Date admDate, int vwd) {
        UtlDateTimeConverter conv = UtlDateTimeConverter.converter();
        ZusatzIF entry = null;
        int id = 0;
        switch (grouperType) {
            case JavaGrouperInterface.DRG_POS: {
                id = SupplFeeTypeEn.ZE.getId();
                entry = UtlDRGListStringManager.drgLists().findZeEntry(proc, catId, ikz, procs, diags,
                        ageY, admDate, drg, vwd);
                if (entry != null) {
                    entry = UtlDRGListStringManager.drgLists().checkZEResult((DrgEntryZusatz) entry, procs, drg, ageY);
                }
                break;
            }
            case JavaGrouperInterface.PEPP_POS: {
                id = SupplFeeTypeEn.ZP.getId();
                entry = UtlPEPPListStringManager.peppManager().findZeEntry(proc, catId, ikz, ageY, admDate);

            }
            break;
            default:
                LOG.log(Level.WARNING, "Unknown grouper type: {0}", grouperType);
        }
        if (entry != null) {
            //Date dt = entry.getValidFrom() == null ? conv.getStartDateForYear(conv.getYear(admDate)) : entry.getValidFrom();
            if(responce != null){
                responce.addAdditionalFee2Ops(id, procId, entry.getName(), 1, 0, entry.getValue(), null, null);
            }
            return entry.getValue();
//                     Logger.getLogger(GrouperRuleService.class.getName()).log(Level.INFO, "proc = " + proc + " entry = " + entry.getName() + " value = " + String.valueOf(entry.getValue()));
        }
        return 0;
    }

    /**
     * sets the defined Rules pile
     *
     * @param rules Array of rules
     */
    public void setRuleList(CRGRule[] rules) {
        if (mRuleCRG != null && rules != null) {
            mRuleCRG.setRuleList(rules);
        }
    }

    /**
     * returns the number of rules in rule kernel
     *
     * @return -1, when rule kernel ist not instantiated
     */
    public int getRuleCount() {
        if (mRuleCRG != null) {
            return mRuleCRG.getRuleCount();
        }
        return -1;
    }

    public void setRuleTypes2Ids(Map<String, String> ruleTypes2Ids) {
        mRuleTypes2Ids = ruleTypes2Ids;
    }

    public boolean hasHistoryRules4Year(int pYear) {
        return mRuleCRG.hasHistoryRules4Year(pYear);
    }

    public Object performGoupAndCheckWithHistory(TransferPatient requestObject) {

        try {
            long start = System.currentTimeMillis();
            mRuleCRG.setGrouperModel(requestObject.getGrouperModelId());
            if (!requestObject.getHistoryCases().isEmpty()) {
                if (requestObject.getMainCase() != null) {
                    // group history cases first                
                    for (TransferCase hisCase : requestObject.getHistoryCases()) {
//                        hisCase.resetAllFlags(); 
                        InOutCreator.createInOut(mRuleCRG.getInout(), hisCase);
                        performGroupOnly(hisCase, false);
                        CRGInputOutput ioHis = new CRGInputOutput();
                        ioHis.copyCase(mRuleCRG.getInout());
                        requestObject.addGroupingResult4HistoryCase(ioHis);
                    }
// now group main case
                    InOutCreator.createInOut(mRuleCRG.getInout(), requestObject);
                    Object responce = performGroupAndCheckOnly(requestObject.getMainCase(), null);
                    requestObject.clearHistory();
                    LOG.log(Level.FINEST, "performGoupAndCheckWithHistory: {0} history cases {1} ms", new Object[]{requestObject.getHistoryCases().size(), String.valueOf(System.currentTimeMillis() - start)});
                    return responce;
                } else {
                    LOG.log(Level.WARNING, "performGoupAndCheckWithHistory, no main case set for Patient, patientID =  {0} has {1} history cases, will not be checked", new Object[]{requestObject.getPatientId(), requestObject.getHistoryCases().size()});
                }
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return createErrorObject(ErrorCause.EXCEPTION, ex.toString());

        }
        return null;
    }

    public List<GrouperResponseObject> performGroupAndCheckPatientCases(List<TransferCase> patientCases, BatchGroupParameter pbatchParam) {
        if (patientCases == null || patientCases.isEmpty()) {
            return null;
        }
        mRuleCRG.setGrouperModel(pbatchParam.getModelId());
        List<GrouperResponseObject> retList = new ArrayList<>();
        try {
            mRuleCRG.newCaseAll();
            for (int i = 0, len = patientCases.size(); i < len; i++) {
                mRuleCRG.setFormerCount(i + 1);
                CRGInputOutput inoutCRG = mRuleCRG.setFormerInout(new RulerInputObjectNull(), i);
                InOutCreator.createInOut(inoutCRG, patientCases.get(i));
            }
            mRuleCRG.sortFormerCasesWithAdmissionDate();

            mRuleCRG.groupFormerCases(true, pbatchParam.isDoSimulate());
            int formerCount = mRuleCRG.getFormerCount();
            int[] sortInd = mRuleCRG.getFormerSort();
// all cases are grouped and sorted with admission date, now we have to check rules for all of them
// here we have to add medicines and so on later
            for (int i = 0; i < formerCount; i++) {
                int sInd = sortInd[i];
                mRuleCRG.getFormerInout(sInd).setFallcount(mRuleCRG.getFormerCount());
                int j = 0;
                int tillFormer = formerCount;
                int count = 0;
                boolean checkHistoryLimits = formerCount >= getMaxForAfterCaseCount();
                if (checkHistoryLimits) {
                    if (i > getMaxForAfterCaseCount() / 2) {
                        j = i - getMaxForAfterCaseCount() / 2;
                    }
                    if (i + getMaxForAfterCaseCount() / 2 < tillFormer) {
                        tillFormer = i + getMaxForAfterCaseCount() / 2;
                    }
                }
                for (; j < tillFormer; j++) {
                    if (j != i) {
                        mRuleCRG.getFormerInout(sInd).setHistoryValues(mRuleCRG.getFormerInout(sortInd[j]), count);
                        count++;
                    }
                }

                mRuleCRG.getFormerInout(sInd).sortClinicCases();
                GrouperResponseObject responce = new GrouperResponseObject(InOutCreator.createResult4Case(mRuleCRG.getFormerInout(sInd), patientCases.get(sInd)), InOutCreator.createBatchResult4Case(mRuleCRG.getFormerInout(sInd), patientCases.get(sInd).getAux9count()));
                retList.add(responce);
                if (pbatchParam.isDoSupplementaryFees()) {
                    this.calculateSupplementaryFees(mRuleCRG.getFormerInout(sInd), responce);
                }
                CRGRule[] rules = mRuleCRG.performCheckRef(mRuleCRG.getFormerInout(sInd), patientCases.get(sInd).getDateOfAdmission(), patientCases.get(sInd).getRoleIdsArray());

                simulateRules(rules, mRuleCRG.getFormerInout(sInd), responce, patientCases.get(sInd));
            }

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);

        }
        return retList;
    }

    protected int getMaxForAfterCaseCount() {
        return CPXRuleGrouper.MAX_CASE_COUNT;
    }
}
