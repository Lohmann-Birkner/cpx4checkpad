/*
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.model.TInsurance;
import de.lb.cpx.model.TLab;
import de.lb.cpx.model.TPatientDetails;
import de.lb.cpx.model.TSapFiBill;
import de.lb.cpx.model.TSapFiBillposition;
import de.lb.cpx.model.TSapFiOpenItems;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.ejb.Remote;

/**
 * Service interface to all Case related services
 *
 * @author wilde
 */
@Remote
public interface CaseServiceBeanRemote {

    /**
     * find all matches in the case Database with a partial CaseNumber
     *
     * @param partialCaseNumber partial casenumber
     * @return all cases with a case number like partial caseNumber
     */
    List<TCase> findMatches(String partialCaseNumber);

    /**
     * find all available matchign case numbers for partial number
     *
     * @param partialCaseNumbers partial case number
     * @return list of case numbers as string
     */
    Collection<String> findMatchingCaseNumbers(String partialCaseNumbers, long patientID);

    /**
     * get a single Case for a CaseNumber
     *
     * @param caseNumber case number
     * @return single case entity
     */
    TCase getCaseForNumber(String caseNumber);

//    /**
//     * find the supplementary fee value for the given version
//     *
//     * @param pGroupingResultId groupingResult id
//     * @param pCalcOnDb indicates if calculation is done on the database level
//     * or not
//     * @return the supplementary fee value
//     */
//    double findSupplementaryFee(long pGroupingResultId, boolean pCalcOnDb);
    /**
     * @param pDbId database id of the case details
     * @return get the case details entity with given id
     */
    TCaseDetails findCaseDetails(long pDbId);

    /**
     *
     * @param id department id
     * @return list of all Icds
     */
    List<TCaseIcd> getIcdsOfDept(long id);

    List<TCaseOps> getOpsOfDept(long id);

    List<TCaseIcd> getIcdsOfWard(long id);

    List<TCaseOps> getOpsOfWard(long id);

    List<TCaseWard> getWardsOfDept(long id);

    String getIcdText(String icdcCode, String countryCode, int year);

    Set<TCaseDepartment> findDepartments(Long pDetailsId);

    /**
     * @param pCaseId case database id
     * @return current local version of the case
     */
    TCaseDetails findCurrentLocal(long pCaseId);

    TCaseDetails findCurrentExtern(long pCaseId);

    TCaseDetails findCurrentVersion(long pCaseId, boolean pIsLocal);

    TPatientDetails findActualPatientDetails(long id);

    TInsurance findActualPatientInsurance(long id);

    List<TSapFiBill> getAllCaseBills(long caseId);

    List<TSapFiBillposition> getAllBillPositionsForBill(long billId);

    List<TSapFiOpenItems> getAllCaseOpenItems(long caseId);

    boolean getSapBillDisplayTab();

    boolean isRuleEditorClient();

//    String getPdfReportType();
    boolean isPdfReportAllowedToUse();

    String getPdfReportXslFilePath();

    String getPdfReportImageFilePath();

    List<TLab> getAllLabs(long caseId);

    boolean getLaboratoryDataDisplayTab();
   /**
     * finds case version where billing risk was tagged  as actual 
     * @param currentCaseId id of the case
     * @param pIsLocal
     * @param pVersRiskType
     * @param pAct
     * @return found version or null
     **/
    TCaseDetails findVersion4ActiveRisk(Long currentCaseId, boolean pIsLocal, VersionRiskTypeEn pVersRiskType, boolean pAct);
    
    /**
     * finds all versions of the case with active flag
     * @param currentCaseId current case id
     * @param pIsLocal local flag
     * @param pActRisk active flag
     * @return list of vesions
     */
    public List<TCaseDetails>findVersionsWithActiveRisk(Long currentCaseId, boolean pIsLocal, boolean pActRisk) ;

    public TCaseDetails findBilingVersionForCase(long pCaseId);

    public TCaseDetails findAssessmentVersionForCase(long pCaseId);

    public List<TCaseDetails> getBillingVersions(long pCaseId);

    public List<TCaseDetails> getAssessmentVersions(long pCaseId);

    public List<TCaseDetails> findVersionsWithActiveRisk4Req(Long currentCaseId, VersionRiskTypeEn pVersionRiskType, boolean pIsLocal) ;

    public void saveOrUpdateCaseVersion(TCaseDetails pCaseVersion);

    public java.util.Set<TCaseDepartment> findDepartmentsLazy(Long pDetailsId);
    
    public int getPatientCasesCount(CaseTypeEn pType, Long pPatientId);

    public void setOrDeleteRuleSelectFlag(String pRuleId, TCase pCase, boolean doSet);
}
