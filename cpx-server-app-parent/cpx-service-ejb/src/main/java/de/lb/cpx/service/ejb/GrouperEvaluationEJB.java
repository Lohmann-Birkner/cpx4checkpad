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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.grouper.model.enums.GrouperEvalResultEn;
import de.lb.cpx.grouper.model.enums.GrouperRefFieldsEn;
import static de.lb.cpx.grouper.model.enums.GrouperRefFieldsEn.DIAGNOSIS_POSITION;
import de.lb.cpx.grouper.model.transfer.EvaluationCaseResult;
import de.lb.cpx.grouper.model.transfer.EvaluationCaseTransfer;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseIcdGrouped;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseOpsGrouped;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.AdmissionReasonEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.dao.TCaseDao;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author gerschmann
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class GrouperEvaluationEJB implements GrouperEvaluationRemote {

    private static final Logger LOG = Logger.getLogger(GrouperEvaluationEJB.class.getName());

    @EJB
    private TCaseDao caseDao;

    @Override
    public void initDbUser(String database) {
        ClientManager.createJobSession(database);
    }

    @Override
    public EvaluationCaseResult evaluateCase(String ikz, String caseNr, EvaluationCaseTransfer refCase, GDRGModel model, EvaluationCaseResult result) {

        result.reset();
        long timeStamp = System.currentTimeMillis();
        long timeStamp1 = System.currentTimeMillis();
        TCase cs = caseDao.findCaseForCaseNumberAndIdent(caseNr, ikz);
        result.setTime4FindCase(ikz + "_" + caseNr, System.currentTimeMillis() - timeStamp1);
        timeStamp1 = System.currentTimeMillis();
        if (cs == null) {
            result.setResult(GrouperEvalResultEn.NotFound);
            result.setInfo("for key " + ikz + "_" + caseNr + " no case found");
            Logger.getLogger(GrouperEvaluationEJB.class.getName()).log(Level.INFO, result.getInfo());
        } else {
            try {
                if (evaluateCase(cs, refCase, model, result)) {
                    result.setResult(GrouperEvalResultEn.OK);
                } else {
                    result.setResult(GrouperEvalResultEn.Error);
                }
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(GrouperEvaluationEJB.class.getName()).log(Level.SEVERE, "Check the encoding charset of your input files!", ex);
            }
        }
        result.setTime4EvalCaseCase(ikz + "_" + caseNr, System.currentTimeMillis() - timeStamp1);
        result.setTimeCase(ikz + "_" + caseNr, System.currentTimeMillis() - timeStamp);

        return result;
    }

    /**
     * compares the case fields with the reference fields. Brakes by the first
     * error
     *
     * @param hospitalCase case from DB
     * @param local flag to check local or extern case
     * @param refCase reference from INEK file
     * @param model grouper model
     * @return returns result of comparison
     */
    private boolean evaluateCase(TCase hospitalCase, EvaluationCaseTransfer refCase, GDRGModel model, EvaluationCaseResult result) throws UnsupportedEncodingException {
        TCaseDetails details = refCase.isIsLocal() ? hospitalCase.getCurrentLocal() : hospitalCase.getCurrentExtern();
// we don't check cases with ADMMODE_12 = '03'  
        if (details.getCsdAdmReason12En().equals(AdmissionReasonEn.ar03)) {
            result.setInfo("first we ignore cases with admission reason 03");
            return true;
        }
        //Set<TCaseDepartment> departments = details.getCaseDepartments();

        TPatient pat = hospitalCase.getPatient();
        TGroupingResults res = getGroupingResult2Model(details, model);
        if (res == null) {
            result.setInfo("Evaluation false for case " + "id =" + hospitalCase.getId()
                    + ", caseNr = " + hospitalCase.getCsCaseNumber() + " no grouping results found");
            Logger.getLogger(GrouperEvaluationEJB.class.getName()).log(Level.INFO,
                    result.getInfo());
            return false;

        }
        boolean retValue = true;
        boolean isDrgChecked = false;

        boolean continue_loop = false;
        for (GrouperRefFieldsEn en : GrouperRefFieldsEn.values()) {
            Object testObj = null;
            switch (en) {
                case KH_POSITION:
                    testObj = hospitalCase.getCsHospitalIdent();

                    break;
                case CASE_NUMBER_POSITION:
                    testObj = hospitalCase.getCsCaseNumber();

                    break;
                case ADM_DATE_POSITION:
                    testObj = details.getCsdAdmissionDate();

                    break;
                case DIS_DATE_POSITION:
                    testObj = details.getCsdDischargeDate();

                    break;
                case ADM_REASON_POSITION:
                    testObj = details.getCsdAdmCauseEn().getViewId();

                    break;
                case ADM_MODE12_POSITION:
                    testObj = details.getCsdAdmReason12En().getId();

                    break;
                case DIS_MODE12_POSITION:
                    testObj = details.getCsdDisReason12En().getId();

                    break;
                case BIRTH_DATE_POSITION:
                    testObj = pat.getPatDateOfBirth();

                    break;
                case ADM_WEIGHT_POSITION:
                    testObj = details.getCsdAdmissionWeight();

                    break;
                case AGE_IN_YEARS_POSITION:
                    testObj = details.getCsdAgeYears();

                    break;
                case AGE_IN_DAYS_POSITION:
                    testObj = details.getCsdAgeDays();

                    break;
                case SEX_POSITION:
                    testObj = details.getCsdGenderEn().getViewId();

                    break;
                case BREATHING_HMV_POSITION:
                    testObj = details.getCsdHmv();

                    break;
                case DIAGNOSIS_POSITION:
                    ArrayList<String> icds = getCaseIcdsAsStringArray(res);
                    boolean dres = refCase.checkMultiField(DIAGNOSIS_POSITION, icds);
                    if (!dres) {
                        result.setInfo("Evaluation false for case " + "id =" + hospitalCase.getId()
                                + ", caseNr = " + hospitalCase.getCsCaseNumber()
                                + " field = " + en.name() + " reference = " + refCase.getField(en)
                                + " value = " + EvaluationCaseTransfer.getStringFromArray(icds));
                        Logger.getLogger(GrouperEvaluationEJB.class.getName()).log(Level.INFO, result.getInfo());

                    }
                    retValue = retValue && dres;
                    //continue;
                    continue_loop = true;
                    break;
                // break;
                case PROCEDURES_POSITION:
                    ArrayList<String> opss = this.getCaseOpssAsStringArray(res);
                    boolean dresOps = refCase.checkMultiField(GrouperRefFieldsEn.PROCEDURES_POSITION, opss);
                    if (!dresOps) {
                        result.setInfo("Evaluation false for case " + "id =" + hospitalCase.getId()
                                + ", caseNr = " + hospitalCase.getCsCaseNumber()
                                + " field = " + en.name() + " reference = " + refCase.getField(en)
                                + " value = " + EvaluationCaseTransfer.getStringFromArray(opss));
                        Logger.getLogger(GrouperEvaluationEJB.class.getName()).log(Level.INFO, result.getInfo());

                    }
                    retValue = retValue && dresOps;
                    //continue;
                    continue_loop = true;
                    break;

                // break;
                case DEPARTMENTS_POSITION:
                    if (hospitalCase.getCsCaseTypeEn().equals(CaseTypeEn.DRG)) {
                        //continue;
                        continue_loop = true;
                        break;
                    } else {
                        // for pepp cases later
                        //continue;
                        LOG.log(Level.FINEST, "has to be implemented for PEPP cases!");
                        continue_loop = true;
                        break;
                    }
                // break;
                case LENGTH_OF_STAY_LOS_POSITION:
                    testObj = details.getCsdLos();

                    break;
                case LEAVE_DAYS_POSITION:
                    /*                        testObj = details.getCsdLeave();

                        break;*/
                    //continue;
                    continue_loop = true;
                    break;
                case DRG_PEPP_POSITION:
                    testObj = res.getGrpresCode();

                    break;
                case MDC_POSITION:
                    testObj = res.getGrpresGroup();

                    break;
                case PCCL_POSITION:
                    testObj = res.getGrpresPccl();

                    break;
                case GST_POSITION:
                    testObj = res.getGrpresGst();

                    break;
                case GPDX_POSITION:
                    testObj = res.getGrpresGpdx();

                    break;
                case ET_POSITION: // for PEPP only
                    //continue;
                    continue_loop = true;
                    break;

            }

            if (continue_loop) {
                continue;
            }

            retValue = retValue && refCase.checkField(en, testObj);
            if (en.equals(GrouperRefFieldsEn.DRG_PEPP_POSITION)) {
                isDrgChecked = true;
            }
            if (!retValue) {
                result.setInfo("Evaluation false for case " + "id =" + hospitalCase.getId()
                        + ", caseNr = " + hospitalCase.getCsCaseNumber()
                        + " field = " + en.name() + " reference = " + refCase.getField(en)
                        + " value = " + String.valueOf(testObj));
                Logger.getLogger(GrouperEvaluationEJB.class.getName()).log(Level.INFO, result.getInfo());
                if (isDrgChecked) {
                    return retValue;
                }
            }
        }
        if (retValue) {
            result.setInfo("id =" + hospitalCase.getId()
                    + ", caseNr = " + hospitalCase.getCsCaseNumber());
        }
        return retValue;
    }

    /**
     * gets grouping result to Model and principal icd
     *
     * @param caseDetails case details
     * @param model grouper model
     * @return grouping result
     */
    private TGroupingResults getGroupingResult2Model(TCaseDetails caseDetails, GDRGModel model) {
        Set<TGroupingResults> groupingResults = caseDetails.getGroupingResultses();
        Iterator<TGroupingResults> itr = groupingResults.iterator();
        while (itr.hasNext()) {
            TGroupingResults oneResult = itr.next();
            if (!oneResult.getGrpresIsAutoFl() && oneResult.getModelIdEn().equals(model)) {
                TCaseIcd icd = oneResult.getCaseIcd();
                if (icd.getIcdcIsHdxFl()) {
                    return oneResult;
                }
            }
        }
        return null;
    }

    /**
     * translate case icds with grouping results into the Arrayof strings with
     * the Format of reference
     *
     * @param result grouping result to this case and model
     * @return array of strings
     */
    private ArrayList<String> getCaseIcdsAsStringArray(TGroupingResults result) {
        Set<TCaseIcdGrouped> icds = result.getCaseIcdGroupeds();
        ArrayList<String> grRes = new ArrayList<>();
        for (TCaseIcdGrouped icdgr : icds) {
            String code = icdgr.getCaseIcd().getIcdcCode();
            code = code + "^" + (icdgr.getIcdResU4gFl() ? "1" : "0") + icdgr.getIcdResValidEn() + icdgr.getIcdResCcl();
            grRes.add(code);
        }

        return grRes;
    }

    /**
     * translate case opsss with grouping results into the Arrayof strings with
     * the Format of reference
     *
     * @param result grouping result to this case and model
     * @return array of strings
     */
    private ArrayList<String> getCaseOpssAsStringArray(TGroupingResults result) {
        Set<TCaseOpsGrouped> ops = result.getCaseOpsGroupeds();
        ArrayList<String> grRes = new ArrayList<>();
        for (TCaseOpsGrouped opsgr : ops) {
            TCaseOps op = opsgr.getCaseOps();
            String code = opsgr.getCaseOps().getOpscCode();
            code = code
                    + "&" + (op.getOpscLocEn() == null ? "" : op.getOpscLocEn().getViewId())
                    + "&" + (op.getOpscDatum() == null ? "" : EvaluationCaseTransfer.date2String(op.getOpscDatum()))
                    + "^" + (opsgr.getOpsResU4gFl() ? "1" : "0") + opsgr.getOpsResValidEn() + (opsgr.getOpsResTypeEn() == null ? "0" : opsgr.getOpsResTypeEn());
            grRes.add(code);
        }
        return grRes;

    }

}
