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
import de.lb.cpx.model.TCase2RuleSelection;
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
import de.lb.cpx.server.commonDB.dao.CIcdCatalogDao;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.server.dao.TCase2RuleSelectionDao;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.server.dao.TCaseDepartmentDao;
import de.lb.cpx.server.dao.TCaseDetailsDao;
import de.lb.cpx.server.dao.TCaseIcdDao;
import de.lb.cpx.server.dao.TCaseOpsDao;
import de.lb.cpx.server.dao.TCaseWardDao;
import de.lb.cpx.server.dao.TGroupingResultsDao;
import de.lb.cpx.server.dao.TLabDao;
import de.lb.cpx.server.dao.TPatientDao;
import de.lb.cpx.server.dao.TPatientDetailsDao;
import de.lb.cpx.server.dao.TSapFiBillDao;
import de.lb.cpx.server.dao.TSapFiBillpositionDao;
import de.lb.cpx.server.dao.TSapFiOpenItemsDao;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.hibernate.Hibernate;

/**
 * Implementation of all Services regarding a case ToDo: Refactor older services
 * to unify services
 *
 * @author wilde
 */
@Stateless
public class CaseServiceBean implements CaseServiceBeanRemote {

    @EJB
    private TCaseDao caseDao;
    @EJB
    private TGroupingResultsDao groupingResultsDao;
    @EJB
    private TCaseDetailsDao detailsDao;
    @EJB
    private TCaseIcdDao icdDao;
    @EJB
    private TCaseOpsDao opsDao;
    @EJB
    private TCaseWardDao wardDao;
    @EJB
    private CIcdCatalogDao icdCatalogDao;
    @EJB
    private TCaseDepartmentDao departmentDao;
    @EJB
    private TPatientDetailsDao patDetailsDao;
    @EJB
    private TPatientDao patientDao;
    @EJB
    private TSapFiBillDao sapFIBillDao;
    @EJB
    private TSapFiBillpositionDao sapFIBillPositionDao;
    @EJB
    private TSapFiOpenItemsDao sapFIOpenItemsDao;
    @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;
    @EJB
    private TLabDao labDao;

    @EJB
    private TCase2RuleSelectionDao case2RuleSelectionDao;
    
    @Override
    public List<TCase> findMatches(String partialCaseNumber) {
        return caseDao.findMatchesLazy(partialCaseNumber);
    }

    @Override
    public Collection<String> findMatchingCaseNumbers(String partialCaseNumbers, long patientID) {
        return caseDao.findMatchingCaseNumbers(partialCaseNumbers, patientID);
    }

    @Override
    public TCase getCaseForNumber(String caseNumber) {
        return caseDao.findCaseByNumber(caseNumber);
    }

//    @Override
//    public double findSupplementaryFee(long pGroupingResultId, boolean pCalcOnDb) {
//        if (pCalcOnDb) {
//            Number res = groupingResultsDao.getSupplementaryValueForIdCalculateOnDatabase(pGroupingResultId);
//            return res != null ? res.doubleValue() : 0.0d;
//        } else {
//            return groupingResultsDao.getSupplementaryValueForId(pGroupingResultId);
//        }
//    }
    @Override
    public TCaseDetails findCaseDetails(long pDbId) {
        TCaseDetails details = detailsDao.findById(pDbId);
        Hibernate.initialize(details.getHospitalCase());
        return details;
    }

    @Override
    public List<TCaseIcd> getIcdsOfDept(long id) {
        return icdDao.getIcdsOfDept(id);
    }

    @Override
    public List<TCaseOps> getOpsOfDept(long id) {
        return opsDao.getOpsOfDept(id);
    }

    @Override
    public List<TCaseIcd> getIcdsOfWard(long id) {
        return icdDao.getIcdsOfWard(id);
    }

    @Override
    public List<TCaseOps> getOpsOfWard(long id) {
        return opsDao.getOpsOfWard(id);
    }

    @Override
    public List<TCaseWard> getWardsOfDept(long id) {
        return wardDao.getWardsOfDept(id);
    }

    @Override
    public String getIcdText(String icdcCode, String countryCode, int year) {
        return icdCatalogDao.getIcdText(icdcCode, countryCode, year);
    }

    @Override
    public Set<TCaseDepartment> findDepartments(Long pDetailsId) {
        return new HashSet<>(departmentDao.findListByCaseDetailsId(pDetailsId,true));
    }
    
    @Override
    public TCaseDetails findCurrentLocal(long pCaseId) {
        return findCurrentVersion(pCaseId, true);
    }

    @Override
    public TCaseDetails findCurrentExtern(long pCaseId) {
        return findCurrentVersion(pCaseId, false);
    }

    @Override
    public TCaseDetails findCurrentVersion(long pCaseId, boolean pIsLocal) {
        return detailsDao.findCurrentDetails(pCaseId, pIsLocal);
    }

    @Override
    public TPatientDetails findActualPatientDetails(long id) {
        TPatientDetails patDet = patDetailsDao.findActualDetails(id);
        if (patDet != null) {
            return patDet;
        }
        return new TPatientDetails();
    }

    @Override
    public TInsurance findActualPatientInsurance(long id) {
        TInsurance ins = patientDao.findActualInsurance(id);
        return ins;
    }

    @Override
    public List<TSapFiBill> getAllCaseBills(long caseId) {
        return sapFIBillDao.findAllForCase(caseId);
    }

    @Override
    public List<TSapFiBillposition> getAllBillPositionsForBill(long billId) {
        return sapFIBillPositionDao.findAllBillPositionsForBill(billId);
    }

    @Override
    public List<TSapFiOpenItems> getAllCaseOpenItems(long caseId) {
        return sapFIOpenItemsDao.findAllForCase(caseId);
    }

    @Override
    public boolean getSapBillDisplayTab() {
        return cpxServerConfig.getSapBillDisplayTab();
    }

    @Override
    public boolean isRuleEditorClient() {
        return cpxServerConfig.isRuleEditorClient();
    }

//    @Override
//    public String getPdfReportType() {
//        return cpxServerConfig.getPdfReportType();
//    }
    @Override
    public boolean isPdfReportAllowedToUse() {
        return cpxServerConfig.isPdfReportAllowedToUse();
    }

    @Override
    public String getPdfReportXslFilePath() {
        return cpxServerConfig.getPdfReportXslFilePath();
    }

    @Override
    public String getPdfReportImageFilePath() {
        return cpxServerConfig.getPdfReportImageFilePath();
    }

    @Override
    public List<TLab> getAllLabs(long caseId) {
        return labDao.findAllForCase(caseId);
    }

    @Override
    public boolean getLaboratoryDataDisplayTab() {
        return cpxServerConfig.getLaboratoryDataDisplayTab();
    }

    @Override
    public TCaseDetails findVersion4ActiveRisk(Long currentCaseId, boolean pIsLocal, VersionRiskTypeEn pVersRiskType, boolean pAct) {
        return detailsDao.findVersion4ActiveRisk(currentCaseId, pIsLocal, pVersRiskType, pAct);
    }

    @Override
    public List<TCaseDetails> findVersionsWithActiveRisk(Long currentCaseId, boolean pIsLocal, boolean pActRisk) {
        return detailsDao.findVersionsWithActiveRisk(currentCaseId, pIsLocal,  pActRisk);
    }

    @Override
    public TCaseDetails findBilingVersionForCase(long pCaseId) {
        return initializeHospitalCase(detailsDao.findBilingVersionForCase(pCaseId,true));
    }

    @Override
    public TCaseDetails findAssessmentVersionForCase(long pCaseId) {
        return initializeHospitalCase(detailsDao.findAsssessmentVersionForCase(pCaseId,true));
    }
    
    private TCaseDetails initializeHospitalCase(TCaseDetails pDetails){
        if(pDetails == null){
            return null;
        }
        Hibernate.initialize(pDetails.getHospitalCase());
        return pDetails;
    }

    @Override
    public List<TCaseDetails> getBillingVersions(long pCaseId) {
        return detailsDao.findVersionsForRiskType(pCaseId, true, VersionRiskTypeEn.BEFORE_BILLING);
    }

    @Override
    public List<TCaseDetails> getAssessmentVersions(long pCaseId) {
        return detailsDao.findVersionsForRiskType(pCaseId, true, VersionRiskTypeEn.CASE_FINALISATION);
    }

    @Override
    public List< TCaseDetails> findVersionsWithActiveRisk4Req(Long currentCaseId, VersionRiskTypeEn pVersionRiskType, boolean pIsLocal) {
        return detailsDao.findVersionsWithActiveRisk4Req( currentCaseId,  pVersionRiskType,  pIsLocal); 
    }

    @Override
    public void saveOrUpdateCaseVersion(TCaseDetails pCaseVersion) {
        detailsDao.saveOrUpdate(pCaseVersion);
    }

    @Override
    public java.util.Set<TCaseDepartment> findDepartmentsLazy(Long pDetailsId) {
        return new HashSet<>(departmentDao.findListByCaseDetailsId(pDetailsId, false));
    }

    @Override
    public int getPatientCasesCount(CaseTypeEn pType, Long pPatientId) {
        return caseDao.getPatientCasesCount(pType, pPatientId);
    }

    @Override
    public void setOrDeleteRuleSelectFlag(String pRuleId, TCase pCase, boolean doSet){
        TCase2RuleSelection item =case2RuleSelectionDao.find4caseAndRuleIds(pCase.getId(), pRuleId);
        if(doSet){
            if(item != null){
                return;
            }
            item = new TCase2RuleSelection();
            item.setHospitalCase(pCase);
            item.setRuleid(pRuleId);
            case2RuleSelectionDao.persist(item);
        }else{
            if(item == null){
                return;
            }
            case2RuleSelectionDao.deleteById(item.getId());
        }
    }
}
