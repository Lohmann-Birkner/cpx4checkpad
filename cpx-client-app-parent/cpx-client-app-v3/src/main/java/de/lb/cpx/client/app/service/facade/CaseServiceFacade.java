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
package de.lb.cpx.client.app.service.facade;

import de.checkpoint.drg.GDRGModel;
import de.checkpoint.ruleGrouper.CRGRule;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxBaserate;
import de.lb.cpx.client.core.model.catalog.CpxBaserateCatalog;
import de.lb.cpx.client.core.model.catalog.CpxHospital;
import de.lb.cpx.client.core.model.catalog.CpxHospitalCatalog;
import de.lb.cpx.client.core.model.catalog.CpxIcdCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.grouper.model.dto.IcdOverviewDTO;
import de.lb.cpx.grouper.model.dto.OpsOverviewDTO;
import de.lb.cpx.grouper.model.util.IcdDtoHelper;
import de.lb.cpx.model.HospitalDevisionIF;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseBill;
import de.lb.cpx.model.TCaseComment;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.model.TCheckResult;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TInsurance;
import de.lb.cpx.model.TLab;
import de.lb.cpx.model.TMibi;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.TPatientDetails;
import de.lb.cpx.model.TSapFiBill;
import de.lb.cpx.model.TSapFiBillposition;
import de.lb.cpx.model.TSapFiOpenItems;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.CommentTypeEn;
import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.server.catalog.service.ejb.RuleServiceBeanRemote;
import de.lb.cpx.server.commonDB.model.CIcdCatalog;
import de.lb.cpx.server.commonDB.model.CMdkAuditreason;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import de.lb.cpx.service.ejb.AuthServiceEJBRemote;
import de.lb.cpx.service.ejb.CaseServiceBeanRemote;
import de.lb.cpx.service.ejb.LockService;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.service.ejb.RiskServiceBeanRemote;
import de.lb.cpx.service.ejb.SingleCaseEJBRemote;
import de.lb.cpx.service.ejb.SingleCaseGroupingEJBRemote;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.dto.ReadOnlyRequestDTO;
import de.lb.cpx.shared.dto.VersionDetailsDTO;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.wm.model.TWmMdkAuditReasons;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javax.ejb.EJBException;
import org.hibernate.LazyInitializationException;

/**
 * ServiceFacade to handle TCase Object and wrap Ejb-Access
 *
 * @author wilde
 */
public class CaseServiceFacade {

    private static final Logger LOG = Logger.getLogger(CaseServiceFacade.class.getName());

    private EjbProxy<SingleCaseEJBRemote> caseEJB;
    private EjbProxy<RuleServiceBeanRemote> ruleServiceBean;

    private EjbProxy<LockService> lockService;
    private EjbProxy<CaseServiceBeanRemote> caseServiceBean;
    private EjbProxy<ProcessServiceBeanRemote> processServiceBean;

    private TCase currentCase;
    private EjbProxy<SingleCaseGroupingEJBRemote> singleCaseGroupingBean;
    private CpxHospitalCatalog hospitalCatalog;
    private CpxInsuranceCompanyCatalog insuranceCatalog;
    private CpxBaserateCatalog baserateCatalog;
    private EjbProxy<AuthServiceEJBRemote> authEJB;

    private EjbProxy<RiskServiceBeanRemote> riskEJB;


    /**
     * Create and Initialize ServiceFacade, load TCase for caseId
     */
    public CaseServiceFacade() {
        init();
//        currentCase = loadCase(caseId);
    }

    private void init() {
        caseEJB = Session.instance().getEjbConnector().connectSingleCaseBean();
        caseServiceBean = Session.instance().getEjbConnector().connectCaseServiceBean();
        processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
        ruleServiceBean = Session.instance().getEjbConnector().connectToRuleServiceBean();
        lockService = Session.instance().getEjbConnector().connectLockServiceBean();
        singleCaseGroupingBean = Session.instance().getEjbConnector().connectSingleCaseGroupingBean();
        hospitalCatalog = CpxHospitalCatalog.instance();
        insuranceCatalog = CpxInsuranceCompanyCatalog.instance();
        baserateCatalog = CpxBaserateCatalog.instance();
        authEJB = Session.instance().getEjbConnector().connectAuthServiceBean();
        riskEJB = Session.instance().getEjbConnector().connectRiskServiceBean();
    }

    /**
     * get currentCase Entity
     *
     * @return current loaded case
     */
    public TCase getCurrentCase() {
        return currentCase;
    }

    /**
     * get admission date of current local case details (this relevant for
     * EasyCoder)
     *
     * @return admission date
     */
    public Date getCurrentLocalAdmissionDate() {
        return (currentCase == null || currentCase.getCurrentLocal() == null) ? null : currentCase.getCurrentLocal().getCsdAdmissionDate();
    }

    /**
     * for your convenience
     *
     * @return current case number
     */
    public String getCurrentCaseNumber() {
        return currentCase == null ? "" : currentCase.getCsCaseNumber();
    }

    /**
     * for your convenience
     *
     * @return current case hospital identifier
     */
    public String getCurrentCaseHospital() {
        return currentCase == null ? "" : currentCase.getCsHospitalIdent();
    }

    /**
     * for your convenience
     *
     * @return current case id
     */
    public Long getCurrentCaseId() {
        return currentCase == null ? 0L : currentCase.getId();
    }

    /**
     * lock case with id
     *
     * @param pCaseId case id
     * @throws LockException lock exception
     */
    public void lock(final Long pCaseId) throws LockException {
        lockService.get().lockCase(pCaseId);
    }

    /**
     * Check if case is locked
     *
     * @param id case to check
     * @return boolean if case is locked
     */
    public boolean isLocked(long id) {
        return lockService.get().isCaseLocked(id);
    }

    /**
     * Check if case is locked (throws LockException)
     *
     * @param id case to check
     * @throws LockException throws if case is locked
     */
    public void checkLock(long id) throws LockException {
        lockService.get().checkCaseLock(id);
    }

    public TCase loadCase(Long caseId) {
        currentCase = caseEJB.get().findSingleCaseForId(caseId);
        if (currentCase == null) {
            LOG.log(Level.WARNING, "No case found for id " + caseId);
        }
//        for(TCaseDetails details : currentCase.getCaseDetails()){
////            isDetailsDisplayedMap.put(details, Boolean.FALSE);
//        }
        return currentCase;
    }

//    public boolean deleteCase(Long caseId) {
//        return caseEJB.deleteCase(caseId);
//    }
    /**
     * reloadCase from Database
     *
     * @return reloaded TCase Entity
     */
    public TCase reloadCase() {
        currentCase = loadCase(currentCase.getId());
        return currentCase;
    }

    /**
     * save current CaseEntity in Database
     */
    public void saveCaseEntity() {
        currentCase.setVersion(caseEJB.get().saveCaseEntity(currentCase));
    }

    /**
     * save TCaseIcd Entity in Database
     *
     * @param icd Diagnosis to save
     */
    public void saveIcdEntity(TCaseIcd icd) {
        icd.setId(caseEJB.get().saveTCaseIcd(icd).getId());
    }

    /**
     * save TCaseOps Entity in Database
     *
     * @param ops Procedure to save
     */
    public void saveOpsEntity(TCaseOps ops) {
        ops.setId(caseEJB.get().saveTCaseOps(ops).getId());
    }

    /**
     * save TPatient Entity in Database
     *
     * @param patient patient to save
     */
    public void savePatientEntity(TPatient patient) {
        patient.setVersion(caseEJB.get().savePatientEntity(patient));
    }

    /**
     * save TPatientDetails Entity in Database
     *
     * @param patDetails patient to save
     */
    public void savePatientDetailsEntity(TPatientDetails patDetails) {
        patDetails.setVersion(caseEJB.get().savePatientDetailsEntity(patDetails));
    }

    /**
     * save TCaseDetails Entity in Database
     *
     * @param caseDetails version to save
     */
    public void saveCaseDetails(TCaseDetails caseDetails) {
        caseDetails.setVersion(caseEJB.get().saveCaseDetailsEntity(caseDetails));
    }

    /**
     * getList of all Cases for the current Patient, if patient are set in
     * Client return these List of Cases - warning: only lazy loaded
     *
     * @return list of all cases for the patient
     */
    public List<TCase> getCaseListForPatient() {
        try {
            return new ArrayList<>(currentCase.getPatient().getCases());
        } catch (LazyInitializationException ex) {
            LOG.log(Level.WARNING, "Was not able to get cases for patient", ex);
            return caseEJB.get().findCaseListForPatient(currentCase.getPatient().getId());
        }
    }

    /**
     * load List of TDepartment Entities from the Database by given Id
     *
     * @param caseDetailsId databaseId of specific Version
     * @return List of TDepartment Entities
     */
    public List<TCaseDepartment> getDepartmentsForDetailsId(long caseDetailsId) {
        return caseEJB.get().findTCaseDepartmentsForCaseDetailId(caseDetailsId);
    }
    
    public List<HospitalDevisionIF> getHospitalDivisionsForDetailsId(long caseDetailsId) {
         List<HospitalDevisionIF>  retWards = new ArrayList<>();
         List<HospitalDevisionIF>  retDepartments = new ArrayList<>();
         List<TCaseDepartment> departments = getDepartmentsForDetailsId(caseDetailsId);
         if(departments != null){
             for(TCaseDepartment dep: departments){
                 Set<TCaseWard> wards = dep.getCaseWards();
                 if (wards != null && ! wards.isEmpty()) {
                     for(TCaseWard ward: wards) {
                         retWards.add(ward);
                     }
                     
                 }
                 retDepartments.add(dep);
             };
             
         }
          List<HospitalDevisionIF> retList;
         if(retWards.isEmpty()){
             retList = retDepartments;
         }else{
            retList = retWards;
         }
         retList = retList.stream().sorted(Comparator.comparing(HospitalDevisionIF::getStartDate)).collect(Collectors.toList()); 
         return retList;
    }

    /**
     * get TDepartment Entities for the current KIS-Version
     *
     * @return List of TDepartment Entities
     */
    public List<TCaseDepartment> getDepartmentListExtern() {
//        return getDepartmentsForDetailsId(currentCase.getCurrentExtern().getId());
        if (currentCase.getCurrentExtern() == null) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Case with ID " + currentCase.getId() + " has no external case details (CS_EXTERN_ACTUAL_ID seems to be NULL)!");
            return new ArrayList<>();
        }
        return currentCase.getCurrentExtern().getSortedDepartments();
    }

    /**
     * get TDepartment Entities for the current CPX-Version
     *
     * @return List of TDepartment Entities
     */
    public List<TCaseDepartment> getDepartmentListLocal() {
//        return getDepartmentsForDetailsId(currentCase.getCurrentLocal().getId());
        if (currentCase.getCurrentLocal() == null) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Case with ID " + currentCase.getId() + " has no local case details (CS_LOCAL_ACTUAL_ID seems to be NULL)!");
            return new ArrayList<>();
        }
        return currentCase.getCurrentLocal().getSortedDepartments();
    }

    /**
     * get Patient from currentCase, if Patient is not initialized it is loaded
     * from the Database and set in the Entity Can throw lazy loading exception
     * if load wasn't called in this facade before
     *
     * @return Patient of currentCase
     */
    public TPatient getPatient() {
        try {
            //2017-04-20 DNi - CPX-471: It is not guaranteed, that the patient has an actual details or an actual insurance -> potential NullPointerException!
            //currentCase.getPatient().getPatDetailsActual().getId();
            //currentCase.getPatient().getPatInsuranceActual().getId();
            TPatient patient = currentCase.getPatient();
            TPatientDetails actPatDetails = (patient == null) ? null : patient.getPatDetailsActual();
            TInsurance actPatInsurance = (patient == null) ? null : patient.getPatInsuranceActual();
            if (actPatDetails != null) {
                actPatDetails.getId();
            }
            if (actPatInsurance != null) {
                actPatInsurance.getId();
            }
            return patient;
        } catch (LazyInitializationException ex) {
            LOG.log(Level.WARNING, "Was not able to get patient details or patient insurance", ex);
            TPatient patient = caseEJB.get().findPatientForId(currentCase.getPatient().getId());
            currentCase.setPatient(patient);
            return patient;
        }
    }

    public TPatient loadPatient() {
        currentCase.setPatient(caseEJB.get().findPatientForId(currentCase.getPatient().getId()));
        return currentCase.getPatient();
    }

    /**
     * get List of TCaseOps Entities for a specific Version
     *
     * @param caseDetailsId database Id of TCaseDetails
     * @return List of all Ops for that TcaseDetails
     */
    public List<TCaseOps> getListOfOpsForCaseDetailsId(Long caseDetailsId) {
        return caseEJB.get().findOpsForCaseDetailsId(caseDetailsId);
    }

    /**
     * get List of all Procedures for the current KIS-Version
     *
     * @return List of TCaseOps Entities
     */
    public List<TCaseOps> getListOfOpsCaseExtern() {
        return getListOfOpsForCaseDetailsId(currentCase.getCurrentExtern().getId());
    }

    /**
     * get List of all Procedures for the current CPX-Version
     *
     * @return List of TCaseOps Entities
     */
    public List<TCaseOps> getListOfOpsCaseLocal() {
        if (currentCase.getCurrentLocal() == null) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Case with ID " + currentCase.getId() + " has no local case details (CS_LOCAL_ACTUAL_ID seems to be NULL)!");
            return new ArrayList<>();
        }
        return getListOfOpsForCaseDetailsId(currentCase.getCurrentLocal().getId());
    }

    /**
     * get List of TCaseICD Entities for a specific Version
     *
     * @param caseDetailsId database Id of TCaseDetails
     * @return List of all Icd for that TcaseDetails
     */
    public List<TCaseIcd> getListOfIcdForCaseDetailsId(Long caseDetailsId) {
        return caseEJB.get().findIcdsForCaseDetailId(caseDetailsId, CpxClientConfig.instance().getSelectedGrouper());
    }

    /**
     * get List of all Diagnosis for the current KIS-Version
     *
     * @return List of TCaseIcd Entities
     */
    public List<TCaseIcd> getListOfIcdsCaseExtern() {
        return getListOfIcdForCaseDetailsId(currentCase.getCurrentExtern().getId());
    }

    /**
     * get List of all Diagnosis for the current CPX-Version
     *
     * @return List of TCaseIcd Entities
     */
    public List<TCaseIcd> getListOfIcdsCaseLocal() {
        if (currentCase.getCurrentLocal() != null) {
            return getListOfIcdForCaseDetailsId(currentCase.getCurrentLocal().getId());
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * get Grouping Results for Version Id and Groupermodel
     *
     * @param caseDetailsId Id of the version
     * @param model groupermodel that was used
     * @return List of GroupingResults
     */
    public List<TGroupingResults> getGroupingResultsForCaseDetailsAndModel(Long caseDetailsId, GDRGModel model) {
        return caseEJB.get().findGroupingResults(caseDetailsId, model);
    }

    /**
     * get Grouping Results for current Kis Version
     *
     * @param model groupermodel
     * @return List of Grouping results for current Kis version
     */
    public List<TGroupingResults> getGroupingResultsForCaseExtern(GDRGModel model) {
        return getGroupingResultsForCaseDetailsAndModel(currentCase.getCurrentExtern().getId(), model);
    }

    /**
     * get Grouping Results for current CPX Version
     *
     * @param model groupermodel
     * @return List of Grouping results for current Cpx version
     */
    public List<TGroupingResults> getGroupingResultsForCaseLocal(GDRGModel model) {
        return getGroupingResultsForCaseDetailsAndModel(currentCase.getCurrentLocal().getId(), model);
    }

    /**
     * get List of Rule Objects for Admissiondate and detected RuleIds for that
     * case
     *
     * @param admDate Case admission Date
     * @param ruleIds List of RuleIds that are detected
     * @return Map of CpxSimeRuleDTO objects, key are the ruleId
     */
    public Map<String, CpxSimpleRuleDTO> getRuleMapForAdmDateAndRuleIds(Date admDate, List<String> ruleIds) {
        return ruleServiceBean.get().findRulesAdmissionDateAndIds(admDate, ruleIds);
    }
//    /**
//     * Get Map of Rules for List of detected RuleIds for the current Localcase
//     * @param ruleIds List of detected Rules
//     * @return  Map of CpxSimeRuleDTO objects, key are the ruleId 
//     */
//    public Map<String, CpxSimpleRuleDTO> getRuleMapForLocalAndRuleIds(List<String> ruleIds){
//        return ruleServiceBean.findRulesAdmissionDateAndIds(currentCase.getCurrentLocal().getCsdAdmissionDate(), ruleIds);
//    }

    /**
     * Get List of Rules for groupingResult Id, Year of Validity specified in
     * Admission Date of current Local
     *
     * @param resultId groupingResult id
     * @return List of CpxSimeRuleDTO objects, key are the ruleId
     */
    public List<CpxSimpleRuleDTO> findRulesForLocalAndResultIds(long resultId) {
        if(currentCase.getCurrentLocal() == null){
            LOG.severe("No current case detected, can not fetch rules!");
            return null;
        }
        return findSortedRulesForYearOfValidity(currentCase.getCurrentLocal().getCsdAdmissionDate(), resultId);
    }
//    /**
//     * Get Map of Rules for List of detected RuleIds for the current Externcase
//     * @param ruleIds List of detected Rules
//     * @return  Map of CpxSimeRuleDTO objects, key are the ruleId 
//     */
//    public Map<String, CpxSimpleRuleDTO> getRuleMapForExternAndRuleIds(List<String> ruleIds){
//        return ruleServiceBean.findRulesAdmissionDateAndIds(currentCase.getCurrentExtern().getCsdAdmissionDate(), ruleIds);
//    }

    /**
     * Get List of Rules for groupingResult Id, Year of Validity specified in
     * Admission Date of current Extern
     *
     * @param resultId groupingResult id
     * @return List of CpxSimeRuleDTO objects, key are the ruleId
     */
    public List<CpxSimpleRuleDTO> findRulesForExternAndResultIds(long resultId) {
        
        List<CpxSimpleRuleDTO> rulesList =  findSortedRulesForYearOfValidity(currentCase.getCurrentExtern().getCsdAdmissionDate(), resultId);
        return rulesList;
    }

    /**
     * get List Of Rules for Date, Year of date specify Year of Validity for
     * Rules to search
     *
     * @param date date within Year if Validity
     * @param resultId id of GroupingResult to get Rules for
     * @return List of Rules, empty if nothing is found
     */
    public List<CpxSimpleRuleDTO> findRulesForYearOfValidity(Date date, long resultId) {
        return ruleServiceBean.get().findRulesAdmissionDateAndGroupingId(date, resultId);
    }

    public List<CpxSimpleRuleDTO> findSortedRulesForYearOfValidity(Date date, long resultId) {
        List<CpxSimpleRuleDTO> dtos = ruleServiceBean.get().findRulesAdmissionDateAndGroupingId(date, resultId);
        if (dtos != null) {
            
            dtos.sort(Comparator.comparingInt(CpxSimpleRuleDTO::getRuleTypeSeverity).reversed()
                    .thenComparing(CpxSimpleRuleDTO::getErrorTyp)
                    .thenComparing(CpxSimpleRuleDTO::getRuleNumber, String.CASE_INSENSITIVE_ORDER)
            );
        }
        return dtos;
    }

    /**
     * Find TCaseIcd which is MainDiagnosis in specified CaseDetails
     *
     * @param caseDetailId database Id for Version
     * @return TCaseIcd Entity where icdcIsHdxFl is true
     */
    public TCaseIcd getMainDiagnosisForCd(Long caseDetailId) {
        return caseEJB.get().findMainDiagnosisIcdForCaseDetailId(caseDetailId);
    }

    /**
     * Find TCaseIcd which is MainDiagnosis in CPX-Version
     *
     * @return TCaseIcd Entity where icdcIsHdxFl is true
     */
    public TCaseIcd getMainDiagnosisForLocal() {
        return caseEJB.get().findMainDiagnosisIcdForCaseDetailId(currentCase.getCurrentLocal().getId());
    }

    /**
     * Find TCaseIcd which is MainDiagnosis in KIS-Version
     *
     * @return TCaseIcd Entity where icdcIsHdxFl is true
     */
    public TCaseIcd getMainDiagnosisForExtern() {
        return caseEJB.get().findMainDiagnosisIcdForCaseDetailId(currentCase.getCurrentExtern().getId());
    }

    /**
     * get TGroupingResult for MainDiagnosis of specific Version
     *
     * @param caseDetailsId id of Version
     * @param model Groupermodel
     * @param idOfMd id of MainDiagnosis
     * @return GroupingResult
     */
    public TGroupingResults getGroupingResultForMd(Long caseDetailsId, GDRGModel model, Long idOfMd) {
        return caseEJB.get().findNewestTGroupingResult(caseDetailsId, model, idOfMd);
    }

    /**
     * get grouping Result for MainDiagnosis in CPX-Version
     *
     * @param model groupermodel
     * @param idOfMd id of MainDiagnosis
     * @return grouping result for local cpx version
     */
    public TGroupingResults getGroupingResultForMdLocal(GDRGModel model, Long idOfMd) {
        return caseEJB.get().findNewestTGroupingResult(currentCase.getCurrentLocal().getId(), model, idOfMd);
    }

    /**
     * get grouping Result for MainDiagnosis in Kis-Version
     *
     * @param model groupermodel
     * @param idOfMd id of MainDiagnosis
     * @return grouping result for extern cpx version
     */
    public TGroupingResults getGroupingResultForMdExtern(GDRGModel model, Long idOfMd) {
        return caseEJB.get().findNewestTGroupingResult(currentCase.getCurrentExtern().getId(), model, idOfMd);
    }

    /**
     * get department By Id from the database
     *
     * @param departmentId id of department Object
     * @return Department Object
     */
    public TCaseDepartment getDepartmentForId(long departmentId) {
        return caseEJB.get().findDepartmentForId(departmentId);
    }

    /**
     * unlock case with id
     *
     * @param caseId case to lock
     * @return boolean if case is unlocked
     */
    public boolean unlock(final long caseId) {
        return lockService.get().unlockCase(caseId);
    }

    /**
     * unlocks the currentCase if its set
     *
     * @return if current case is unlocked or not
     */
    public boolean unlockCurrentCase() {
        if (currentCase != null) {
            long caseId = currentCase.getId();
            try {
                return lockService.get().unlockCase(caseId);
            } catch (EJBException ex) {
                LOG.log(Level.FINEST, "Was not able to unlock case id " + caseId + ". Maybe CPX server was restarted!", ex);
            }
        }
        return false;
    }

//    public boolean unlock(final long caseId, final boolean pForce) {
//        return lockService.get().unlockCase(caseId, pForce);
//    }
//    public LockDTO[] getLock(long caseId) {
//        return lockService.get().getCaseLock(caseId);
//    }
    /**
     * get Current local Version of the Case
     *
     * @return cpx version
     */
    public TCaseDetails getCurrentCaseLocal() {
        return currentCase.getCurrentLocal();
    }

    /**
     * @return all current local from current case
     */
    public Set<TCaseDetails> getCurrentCaseLocals() {
        return currentCase.getLocals();
    }

    /**
     * get current extern Version of the Case
     *
     * @return kis version
     */
    public TCaseDetails getCurrentCaseExtern() {
        return currentCase.getCurrentExtern();
    }

    /**
     * @return all current extern from current case
     */
    public Set<TCaseDetails> getCurrentCaseExterns() {
        return currentCase.getExterns();
    }

    public VersionDetailsDTO getVersionDetails(long id) {
        return caseEJB.get().getVersionDetails(id);
    }

    public TCaseDetails createNewVersion(TCaseDetails selected) throws CpxIllegalArgumentException {
        Long newDetailsId = caseEJB.get().createNewVersion(currentCase.getId(), selected.getId());
        TCaseDetails newDetails = newDetailsId!=null?caseEJB.get().findCaseDetails(newDetailsId):null; //CPX-2534 - load object that has proxys, because when saving the newly created version will 'reset' department,icd,ops values!
        if (newDetails == null) {
            return null;
        }
        if (selected.getCsdIsLocalFl()) {
            selected.setCsdIsActualFl(false);
        }
        getCurrentCase().setCsStatusEn(CaseStatusEn.NEW_VERS);
        getCurrentCase().setCurrentLocal(newDetails);
//        isDetailsDisplayedMap.put(newDetails, Boolean.FALSE);
//        setDisplayed(selected, false);
        return newDetails;
    }

    public TCaseDetails deleteVersion(TCaseDetails item) throws CpxIllegalArgumentException {
        TCaseDetails newCurrentLocal = caseEJB.get().deleteVersion(item);
        if (newCurrentLocal == null) {
            throw new CpxIllegalArgumentException("Saving failed");
        }
        getCurrentCase().setCurrentLocal(newCurrentLocal);
        caseEJB.get().saveCaseDetailsEntity(newCurrentLocal);
        getCurrentCase().getCaseDetails().remove(item);
//        isDetailsDisplayedMap.remove(item);
        return newCurrentLocal;
    }

    public String getSuccessorDetails(TCaseDetails item) {
        return caseEJB.get().getSuccessorDetails(item);
    }

    /**
     * get Detected Rules from grouping, Rule list is specific for the current
     * Role of the current user
     *
     * @param results list of Rules detected in Rule-Evaluation
     * @return list of Rules detected in Rule-Evaluation, if nothing was found
     * empty List
     */
    public List<TCheckResult> getDetectedRules(TGroupingResults results) {
        return caseEJB.get().findDetectedRules(results.getId());
    }

    /**
     * @param id unique db id of the tpatient entity
     * @return Patient data, execute a select query on the server
     */
    public TPatient findPatient(long id) {
        return caseEJB.get().findPatientForId(id);
    }

    /**
     * @param icdcCode diagnosis code
     * @param pYear year of validity
     * @return catalog text in the year and for the code
     */
    public String getIcdText(String icdcCode, int pYear) {
        CIcdCatalog icdCat = CpxIcdCatalog.instance().getByCode(icdcCode, "de", pYear);
        return icdCat.getIcdDescription();
    }

    /**
     * @param pAdmDate admission date of the case version to determine validity
     * @return base rate catalog object, if nothing is found dummy object is
     * returned
     */
    public CpxBaserate getBaseRate(Date pAdmDate) {
        CpxBaserate rate = baserateCatalog.findDrgBaserate(getCurrentCase().getCsHospitalIdent(), pAdmDate, AbstractCpxCatalog.DEFAULT_COUNTRY);
        if (rate.getId() == 0) {
            LOG.warning("No Baserate found!\n"
                    + "HospitalIdent " + getCurrentCase().getCsHospitalIdent() + "\n"
                    + "AdmissionDate " + (pAdmDate != null ? pAdmDate.toString() : "null"));
        }
        return rate;
    }

    /**
     * @param pAdmDate admission date of the case version
     * @return fee value of the base rate from the catalog. if nothing is found
     * 0.0 is returned
     */
    public Double getCaseBaseRateFeeValue(Date pAdmDate) {
        if (getCurrentCase().getCsCaseTypeEn() == CaseTypeEn.PEPP) {
            return baserateCatalog.findPeppBaserateFeeValue(getCurrentCase().getCsHospitalIdent(), pAdmDate, AbstractCpxCatalog.DEFAULT_COUNTRY);
        } else {
            return baserateCatalog.findDrgBaserateFeeValue(getCurrentCase().getCsHospitalIdent(), pAdmDate, AbstractCpxCatalog.DEFAULT_COUNTRY);
        }
    }
    public Double getCareBaseRateFeeValue(Date pAdmDate) {
        return baserateCatalog.findCareBaserateFeeValue(getCurrentCase().getCsHospitalIdent(), pAdmDate, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    /**
     * @param pVersion case version
     * @param pType supplementary fee type
     * @return calculated supplementary fee value from the server for a case
     * version
     */
    public Double getSupplementaryFeeValue(TCaseDetails pVersion, SupplFeeTypeEn pType) {
        GDRGModel grouper = CpxClientConfig.instance().getSelectedGrouper();
        return singleCaseGroupingBean.get().getSupplFeeValue(grouper, pVersion.getId(), pType);
    }

    /**
     * @param pVersion case version
     * @param pModel grouper model
     * @return grouping result entity, execute a select query on the server, can
     * be null if nothing is found
     */
    public TGroupingResults getGroupingResultForVersion(TCaseDetails pVersion, GDRGModel pModel) {
        return caseEJB.get().findGroupingResultForVersion(pVersion.getId(), pModel);
    }

    /**
     * get all Icd Codes for the current Case
     *
     * @param pVersionIds list of version ids, if null all icds for the current
     * extern and local
     * @return all icd codes for the current Case
     */
    public List<IcdOverviewDTO> getAllIcdCodes(List<Long> pVersionIds) {
        if (pVersionIds == null) {
            List<Long> ids = new ArrayList<>();
            ids.add(getCurrentCaseLocal().id);
            ids.add(getCurrentCaseExtern().id);
            return caseEJB.get().findAllIcdCodesForVersions(ids, CpxClientConfig.instance().getSelectedGrouper());
        }
        return caseEJB.get().findAllIcdCodesForVersions(pVersionIds, CpxClientConfig.instance().getSelectedGrouper());
    }

    public List<IcdOverviewDTO> refreshIcdCodes(HashMap<Long, List<TCaseIcd>> pMap) {
        return new IcdDtoHelper().computeDtoList(pMap);
    }

    public List<IcdOverviewDTO> getAllIcdCodes(TCaseDetails currentCaseLocal) {
        List<Long> ids = new ArrayList<>();
        ids.add(currentCaseLocal.id);
        return caseEJB.get().findAllIcdCodesForVersions(ids, CpxClientConfig.instance().getSelectedGrouper());
    }

    /**
     * mockup methode to get detected rules from the server mimic detected rules
     *
     * @param id id of the grouping result
     * @return list of detected Rules
     */
    public List<CpxSimpleRuleDTO> findRulesForGroupingResult_MOCKUP(long id) {
        List<CpxSimpleRuleDTO> rulesMockupList = new ArrayList<>();
        CRGRule currentRule = new CRGRule();
        currentRule.m_rid = "1";
        currentRule.m_number = "2016_1004310_20129999000000";
        currentRule.m_typeText = "error";
        currentRule.m_errorTypeText = "DKR";
        currentRule.m_text = "";
        currentRule.m_caption = "DKR 1107: Dehydratation bei Gastroenteritis";
        currentRule.m_notice = "DKR 1107 Dehydratation bei Gastroenteritis&amp;#013&amp;#013Bei stationärer Aufnahme zur Behandlung einer Gastroenteritis mit Dehydratation wird die Gastroenteritis als Hauptdiagnose und „Dehydratation” (E86 Volumenmangel) als Nebendiagnose&amp;#013angegeben.&amp;#013&amp;#013&amp;#013&amp;#013";
        currentRule.m_suggestion = "Gastroenteritis ist die korrekte Hauptdiagnose (+HD A09)";
        CpxSimpleRuleDTO dto1 = new CpxSimpleRuleDTO(currentRule);

        currentRule = new CRGRule();
        currentRule.m_rid = "2";
        currentRule.m_number =  "2016_1004311_20129999000000";
        currentRule.m_typeText = "error";
        currentRule.m_errorTypeText = "ICD";
        currentRule.m_text = "";
        currentRule.m_caption = "Peritonitis als Nebendiagnose mit anderer Peritonitis-Diagnose ";
        currentRule.m_notice = "K65 Peritonitis  &amp;#013  Exkl.:  &amp;#013Peritonitis: &amp;#013· aseptisch ( T81.6 ) &amp;#013· bei oder nach: &amp;#013· Abort, Extrauteringravidität oder Molenschwangerschaft ( O00-O07 , O08.0 ) &amp;#013· Appendizitis ( K35.- ) &amp;#013· Divertikulose des Darmes ( K57.- ) &amp;#013· beim Neugeborenen ( P78.0-P78.1 ) &amp;#013· benigne, paroxysmal ( E85.0 ) &amp;#013· durch chemische Substanzen ( T81.6 ) &amp;#013· durch Talkum oder sonstige Fremdsubstanzen ( T81.6 ) &amp;#013· periodisch, familiär ( E85.0 ) &amp;#013· puerperal ( O85 ) &amp;#013· weibliches Becken ( N73.3-N73.5 ) &amp;#013 &amp;#013";
        currentRule.m_suggestion = "Peritonitis: gleichzeitige Kodierung aus K65* und den dazugehörigen Exklusiva, bitte korrekte ND angeben";
        CpxSimpleRuleDTO dto2 = new CpxSimpleRuleDTO(currentRule);
        
        currentRule = new CRGRule();
        currentRule.m_rid = "3";
        currentRule.m_number = "2016_1004312_20129999000000";
        currentRule.m_typeText = "error";
        currentRule.m_errorTypeText = "ICD";
        currentRule.m_text = "";
        currentRule.m_caption = "Peritonitis als Hauptdiagnose mit anderer Peritonitis-Diagnose";
        currentRule.m_notice = "K65 Peritonitis  &amp;#013  Exkl.:  &amp;#013Peritonitis: &amp;#013· aseptisch ( T81.6 ) &amp;#013· bei oder nach: &amp;#013· Abort, Extrauteringravidität oder Molenschwangerschaft ( O00-O07 , O08.0 ) &amp;#013· Appendizitis ( K35.- ) &amp;#013· Divertikulose des Darmes ( K57.- ) &amp;#013· beim Neugeborenen ( P78.0-P78.1 ) &amp;#013· benigne, paroxysmal ( E85.0 ) &amp;#013· durch chemische Substanzen ( T81.6 ) &amp;#013· durch Talkum oder sonstige Fremdsubstanzen ( T81.6 ) &amp;#013· periodisch, familiär ( E85.0 ) &amp;#013· puerperal ( O85 ) &amp;#013· weibliches Becken ( N73.3-N73.5 ) &amp;#013 &amp;#013";
        currentRule.m_suggestion = "Peritonitis: HD aus K65* und ND aus den dazugehörigen Exklusiva, bitte prüfen";
        CpxSimpleRuleDTO dto3 = new CpxSimpleRuleDTO(currentRule);
        
       currentRule = new CRGRule();
        currentRule.m_rid = "4";
        currentRule.m_number = "2016_1004318_20129999000000";
        currentRule.m_typeText =  "warning";
        currentRule.m_errorTypeText = "DRG";
        currentRule.m_text = "";
        currentRule.m_caption = "Fehler DRG: Eingriff(e) ohne Bezug zur Hauptdiagnose";
        currentRule.m_notice = "";
        currentRule.m_suggestion = "Kodierung der Hauptdiagnose (DKR D002) prüfen";
        CpxSimpleRuleDTO dto4 = new CpxSimpleRuleDTO(currentRule);
        
       currentRule = new CRGRule();
        currentRule.m_rid = "5";
        currentRule.m_number = "2016_1004328_20129999000000";
        currentRule.m_typeText = "warning";
        currentRule.m_errorTypeText = "DKR";
        currentRule.m_text = "";
        currentRule.m_caption = "DKR 0901: Myokardinfarkt und Angina pectoris als ND";
        currentRule.m_notice = "DKR 0901 Angina pectoris (I20.–)&amp;#013&amp;#013... Wenn ein Patient mit instabiler Angina pectoris &amp;#013aufgenommen wird und diese sich während des &amp;#013Krankenhausaufenthaltes zu einem Myokardinfarkt &amp;#013entwickelt, ist nur der Kode für einen Myokardinfarkt &amp;#013anzugeben.&amp;#013Wenn der Patient jedoch eine Postinfarkt-Angina &amp;#013entwickelt, kann I20.0 Instabile Angina pectoris als &amp;#013zusätzlicher Kode angegeben werden...";
        currentRule.m_suggestion = "DKR 0901: Angina pectoris bitte nur zusätzlich im Falle einer Postinfarkt-Angina kodieren (-ND I20)";
        CpxSimpleRuleDTO dto5 = new CpxSimpleRuleDTO(currentRule);

       currentRule = new CRGRule();
        currentRule.m_rid = "6";
        currentRule.m_number = "2016_1004351_20129999000000";
        currentRule.m_typeText = "suggestion";
        currentRule.m_errorTypeText = "MV";
        currentRule.m_text = "";
        currentRule.m_caption = "Hypertonus Mehrfachauswahl ";
        currentRule.m_notice = "";
        currentRule.m_suggestion = "Hypertonie: bitte für eine Hypertonieform entscheiden";
        CpxSimpleRuleDTO dto6 = new CpxSimpleRuleDTO(currentRule);

        rulesMockupList.add(dto1);
        rulesMockupList.add(dto2);
        rulesMockupList.add(dto3);
        rulesMockupList.add(dto4);
        rulesMockupList.add(dto5);
        rulesMockupList.add(dto6);
        return rulesMockupList;
    }

    public int findRuleErrorCount_MOCKUP(long id) {
        int counter = 0;
        for (CpxSimpleRuleDTO dto : findRulesForGroupingResult_MOCKUP(id)) {
            //if (dto.getRuleTyp().equals("error")) {
            if (dto.isError()) {
                counter++;
            }
        }
        return counter;
    }

    public int findRuleSuggestionCount_MOCKUP(long id) {
        int counter = 0;
        for (CpxSimpleRuleDTO dto : findRulesForGroupingResult_MOCKUP(id)) {
            //if (dto.getRuleTyp().equals("suggestion")) {
            if (dto.isInformation()) {
                counter++;
            }
        }
        return counter;
    }

    public int findRuleWarningCount_MOCKUP(long id) {
        int counter = 0;
        for (CpxSimpleRuleDTO dto : findRulesForGroupingResult_MOCKUP(id)) {
            //if (dto.getRuleTyp().equals("warning")) {
            if (dto.isWarning()) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * @param pVersionIds list of versions to get the set of all ops
     * @return list of all ops for the specified versions
     */
    public List<OpsOverviewDTO> getAllOpsCodes(List<Long> pVersionIds) {
        if (pVersionIds == null) {
            List<Long> ids = new ArrayList<>();
            ids.add(getCurrentCaseLocal().id);
            ids.add(getCurrentCaseExtern().id);
            return caseEJB.get().findAllOpsCodesForVersions(ids);
        }
        return caseEJB.get().findAllOpsCodesForVersions(pVersionIds);
    }

//    /**
//     * gets the supplementary fee value for the version
//     *
//     * @param groupingResultId version id
//     * @return supplementary fee value
//     */
//    public double findSupplementaryFee(long groupingResultId) {
//        return caseServiceBean.get().findSupplementaryFee(groupingResultId, true);
//    }
    /**
     * @param csHospitalIdent unique hospital ident
     * @return hospital catalog data
     */
    public CpxHospital getHospitalData(String csHospitalIdent) {
        return hospitalCatalog.getByCode(csHospitalIdent, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    /**
     * @param insNumber unique number of the insurance
     * @return insurance catalog data
     */
    public CpxInsuranceCompany getInsuranceData(String insNumber) {
        return insuranceCatalog.getByCode(insNumber, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    /**
     * @param pIcd removes icd from the database
     */
    public void deleteIcd(TCaseIcd pIcd) {
        caseEJB.get().removeIcdById(pIcd.getId());
    }

    /**
     * @param pOps removes ops from the database
     */
    public void deleteOps(TCaseOps pOps) {
        caseEJB.get().removeOps(pOps);
    }

    /**
     * @param pDetails case version to get fees for
     * @return list of fees (Entgelte) stored in the database
     */
    public List<TCaseFee> getCaseFeesForDetails(TCaseDetails pDetails) {
        return caseEJB.get().findCaseFeesForDetails(pDetails.getId());
    }

    public TCaseDetails findCaseDetails(long pDetailsId) {
        return caseEJB.get().findCaseDetails(pDetailsId);
    }

    public List<TMibi> findCaseMibis(final long pCaseId) {
        return caseEJB.get().findAllMibis(pCaseId);
    }

    public List<TCaseComment> findCaseComments(long pCaseId, CommentTypeEn pCommentType) {
        //TODO:enable server connection
//        return caseEJB.findAllComments(pCaseId,pCommentType);
        switch (pCommentType) {
            case caseReview:
                return caseEJB.get().findAllComments(pCaseId, pCommentType);
//                return getCaseCommentTestData();
            default:
                return new ArrayList<>();
        }
    }

    public List<TCaseComment> findCaseComments(CommentTypeEn pCommentType) {
        return findCaseComments(getCurrentCase().getId(), pCommentType);
    }
    
    public List <? extends AbstractVersionEntity> findItems4Documnetation(CommentTypeEn pCommentType){
        switch (pCommentType) {
            case caseReview:
                return findCaseComments(pCommentType);
            case riskReview:
                return getLocalVersionsFromDb();//getCurrentCaseLocals().stream().collect(Collectors.toList());
            default:
                return this.getForBillingVersions();
        }
    }
    /**
    * returns all local versions of the current case with BEFORE_BILLING type
     * @return 
    */
    public List<TCaseDetails> getForBillingVersions(){
        Set<TCaseDetails> locals = getCurrentCaseLocals();
        List<TCaseDetails> retList = new ArrayList<>();
        for(TCaseDetails d : locals){
            if(d.getCsdVersRiskTypeEn() != null && d.getCsdVersRiskTypeEn().equals(VersionRiskTypeEn.BEFORE_BILLING)){
                retList.add(d);
            }
            
        }
        return retList;
    }
//    private List<TCaseComment> getCaseCommentTestData(){
//        
//        List<TCaseComment> comments = new ArrayList<>();
//        TCaseComment comment1 = new TCaseComment();
//        comment1.setnumber(1L);
//        comment1.setId(1L);
//        comment1.setText("test 1".toCharArray());
//        comment1.setCreationDate(new Date());
//        comment1.setCreationUser(13L);
//        comment1.setModificationDate(new Date());
//        comment1.setModificationUser(4L);
//        comments.add(comment1);
//        TCaseComment comment2 = new TCaseComment();
//        comment2.setnumber(2L);
//        comment2.setId(2L);
//        comment2.setText("test 2".toCharArray());
//        comment2.setCreationDate(new Date());
//        comment2.setCreationUser(13L);
//        comment2.setModificationDate(new Date());
//        comment2.setModificationUser(4L);
//        comment2.setActive(Boolean.TRUE);
//        comments.add(comment2);
//        
//        TCaseComment comment3 = new TCaseComment();
//        comment3.setnumber(3L);
//        comment3.setId(3L);
//        comment3.setText("test 3".toCharArray());
//        comment3.setCreationDate(new Date());
//        comment3.setCreationUser(13L);
//        comment3.setModificationDate(new Date());
//        comment3.setModificationUser(4L);
//        comments.add(comment3);
//        return comments;
////        return caseEJB.findAllComments(pCaseId, CommentType.caseReview);
//    }

    /**
     * @param pType comment type
     * @return active comment for the current case with given type, if none is
     * found new empty comment is returned
     */
    public TCaseComment findActiveComment(CommentTypeEn pType) {
        //TODO:enable server connection
//        List<TCaseComment> data = getCaseCommentTestData();
//        for(TCaseComment com : data){
//            if(com.isActive()){
//                return com;
//            }
//        }
//        return null;
        TCaseComment active = caseEJB.get().findActiveComment(currentCase.getId(), pType);
        if (active != null) {
            return active;
        }
        TCaseComment comment = new TCaseComment();
        comment.setTypeEn(pType);
        return comment;
    }

    public String findUserName(Long pUserId) {
        if (authEJB == null) {
            return "";
        }
        return authEJB.get().getUserLoginName(pUserId);
    }

    public TCaseComment storeComment(TCaseComment pComment) {
        return caseEJB.get().storeComment(currentCase.getId(), pComment);
    }

    public boolean removeComment(TCaseComment pComment) {
        return caseEJB.get().removeComment(pComment.getId());
    }

    //properties for test to recognize changes and update views?
    //try to remove this static stuff
    private ObservableMap<Object, Object> properties;

    /**
     * Returns an observable map of properties on this node for use primarily by
     * application developers.
     *
     * @return an observable map of properties on this node for use primarily by
     * application developers
     */
    public final ObservableMap<Object, Object> getProperties() {
        if (properties == null) {
            properties = FXCollections.observableMap(new HashMap<>());
        }
        return properties;
    }

    public void destroy() {
        properties.clear();
        properties = null;
//        currentCase = null;
    }

    public List<TSapFiBill> getAllCaseBills(long caseId) {
        return caseServiceBean.get().getAllCaseBills(caseId);
    }

    public List<TSapFiBillposition> getAllBillPositionsForBill(long billId) {
        return caseServiceBean.get().getAllBillPositionsForBill(billId);
    }

    public List<TSapFiOpenItems> getAllCaseOpenItems(long caseId) {
        return caseServiceBean.get().getAllCaseOpenItems(caseId);
    }

    public boolean getSapBillDisplayTab() {
        return caseServiceBean.get().getSapBillDisplayTab();
    }

    public boolean isRuleEditorClient() {
        return caseServiceBean.get().isRuleEditorClient();
    }

    public List<TLab> getAllLaboratoryData(long caseId) {
        return caseServiceBean.get().getAllLabs(caseId);
    }

    public boolean getLaboratoryDataDisplayTab() {
        return caseServiceBean.get().getLaboratoryDataDisplayTab();
    }

    public void updateBills(TCase currentCase) {
        processServiceBean.get().updateBills(currentCase);
    }
    
//    public List<TWmRiskDetails> getRisks4Billing(){
//        
//         TWmRisk billingRisk = getBillingRisk4Case();
//         return checkRiskDetailsFromRules(billingRisk);
////         if(billingRisk.getRiskDetails() == null){
////             return createRiskDetailsFromRules();
////         }
////         return new ArrayList<>(billingRisk.getRiskDetails());
//    }
//    


    public TWmRisk addRisks(List<? extends TWmRiskDetails> addedSubList, TWmRisk billingRisk) {
//        TWmRisk billingRisk = getBillingRisk4Case();
        Set<TWmRiskDetails> details = billingRisk.getRiskDetails();
        if(details == null){
            details = new HashSet<>();
            billingRisk.setRiskDetails(details);
        }
        for(TWmRiskDetails added: addedSubList){
            boolean isAdded = false;
            for(TWmRiskDetails detail : details){

                if(detail.getRiskArea().equals(added.getRiskArea())){
                   
                    detail.setRiskPercent(added.getRiskPercent());
                    detail.setRiskValue(added.getRiskValue());
                    detail.setModificationDate(new Date());
                    detail.setModificationUser(Session.instance().getCpxUserId());
                    isAdded = true;
                    break;
                }
            }
            if(!isAdded){
                added.setRisk(billingRisk);
                added.setCreationDate(new Date());
                added.setCreationUser(Session.instance().getCpxUserId());
                details.add(added);
            }
        }
        return saveRiskEntity(billingRisk);
    }
    
    public TWmRisk saveRiskEntity(TWmRisk pRisk){
       return riskEJB.get().saveRiskEntity(pRisk);
    }

    public void removeRisks(List<? extends TWmRiskDetails> pRemovedRisks,  TWmRisk billingRisk ) {

        Set<TWmRiskDetails> details = billingRisk.getRiskDetails();
        
        if(details == null || details.isEmpty() || pRemovedRisks == null || pRemovedRisks.isEmpty()){
            LOG.log(Level.INFO, " can't remove any risk");
            return;
        }
        List<Long> detailsIds = new ArrayList<>();
        for(TWmRiskDetails removed: pRemovedRisks){

            for(TWmRiskDetails detail : details){

                if(detail.getRiskArea().equals(removed.getRiskArea())){
                     details.remove(detail);
                     detail.setRisk(null);
                     detailsIds.add(detail.getId());
                    break;
                }
            }

        }
        riskEJB.get().removeRiskDetails(detailsIds);
    }

    public Boolean checkRiskDetails(TWmRiskDetails pRiskDetails,  TWmRisk billingRisk ) {
        if(pRiskDetails != null){
            List <TWmRiskDetails> list = new ArrayList<>();
            list.add(pRiskDetails);
            addRisks(list, billingRisk);
        }
        return true;
    }
    
    public List<CpxSimpleRuleDTO> getRules4Risks(TCaseDetails pVersion){
                GDRGModel grouperModel = CpxClientConfig.instance().getSelectedGrouper();
        try{
            TGroupingResults result = getGroupingResultForVersion(pVersion, grouperModel);
            if(result == null){
                //TODO: Group for version
                 return new ArrayList<>();
            }
           
            return findRulesForLocalAndResultIds(result.getId());

        }catch(Exception ex){
            LOG.log(Level.SEVERE, " could not find grouping results for actual version, risk cannot be found", ex);
        }
        return new ArrayList<>();
    }

    public TCaseDetails getCaseVersion(Long pVersionId, boolean pIsLocal) {
        if(pVersionId == null){
            LOG.log(Level.WARNING, "Can not load {0}-Version, id is invalid", pIsLocal?"local":"extern");
            return null;
        }
        Set<TCaseDetails> versions = pIsLocal?currentCase.getLocals():currentCase.getExterns();
        for(TCaseDetails version : versions){
            if(pVersionId.equals(version.getId())){
                return version;
            }
        }
        LOG.log(Level.WARNING, "Can not load {0}-Version, id: {1} is not found in caseNumber {2}", new Object[]{pIsLocal?"local":"extern", pVersionId, getCurrentCaseNumber()});
        return null;
    }

    public TWmRisk getRisk4CaseVersion(TCaseDetails version) {
        PlaceOfRegEn billingType = getRiskType2VersionType(version);
        TWmRisk billingRisk =  riskEJB.get().findRisk4CaseAndPlaceOrReg(version.getId(), billingType);
         if(billingRisk == null){
            billingRisk = new TWmRisk();
           billingRisk.setCaseDetails(version);
          
           billingRisk.setRiskPlaceOfReg(billingType);
           billingRisk.setCreationDate(new Date());
           billingRisk.setCreationUser(Session.instance().getCpxUserId());
           billingRisk.setRiskPercentTotal(0);
           billingRisk.setRiskValueTotal(BigDecimal.ZERO);
           billingRisk.setRiskActual4Req(false);
           billingRisk.setRiskAuditPercent(0);
           billingRisk.setRiskAuditPercentSugg(0);
           billingRisk.setRiskWastePercent(0);
           billingRisk.setRiskWastePercentSugg(0);
           billingRisk.setRiskBaseFee(getdFee4case(version, version.getCsdVersRiskTypeEn()));
           billingRisk.setRiskNotCalculatedFee(billingRisk.getRiskBaseFee()); 
           checkRiskDetailsFromAuditReasons(billingRisk);
           billingRisk = riskEJB.get().saveRiskEntity(billingRisk);
           
         }

         return billingRisk;
    }

    public TCaseDetails getLastActiveVersion() {
        List <TCaseDetails> ret = caseServiceBean.get().findVersionsWithActiveRisk(getCurrentCaseId(), true,  true);
        if(ret == null){
            return this.getCurrentCaseLocal();
        }
         Map<VersionRiskTypeEn,TCaseDetails>retMap = new EnumMap<>(VersionRiskTypeEn.class);
         for(TCaseDetails vers: ret){
            retMap.put(vers.getCsdVersRiskTypeEn(), vers);
        }

        TCaseDetails vers = retMap.get(VersionRiskTypeEn.CASE_FINALISATION);
        if(vers != null){
            return vers;
        }
        vers = retMap.get( VersionRiskTypeEn.AUDIT_MD);
        if(vers != null){
            return vers;
        }
        vers = retMap.get( VersionRiskTypeEn.AUDIT_CASE_DIALOG);
        if(vers != null){
            return vers;
        }
        vers = retMap.get( VersionRiskTypeEn.CASE_FINALISATION);
        if(vers != null){
            return vers;
        }
         return this.getCurrentCaseLocal();
    }
    


    public boolean check4ActualRisk(TCaseDetails version) {
        TWmRisk billingRisk =  riskEJB.get().findRisk4CaseAndPlaceOrReg(version.getId(), getRiskType2VersionType(version));
        if(billingRisk != null && billingRisk.getRiskActual4Req()){
            return true;
        }
        return false;
    }

    public List<TCaseDetails> getLocalVersionsFromDb() {
        //AWi: would be more efficient if case finalisation values should not be loaded and sent in the first case
        //but should be ok for now
        List<TCaseDetails> list = caseEJB.get().findLocalCaseDetails(getCurrentCaseId());
        return list;
    }

    @SuppressWarnings("fallthrough")
    private PlaceOfRegEn getRiskType2VersionType(TCaseDetails version) {
        if(version.getCsdVersRiskTypeEn() == null){
            return PlaceOfRegEn.BEFORE_BILLING;
        }
        switch(version.getCsdVersRiskTypeEn()){

            case AUDIT_CASE_DIALOG:
            case AUDIT_MD:
                return PlaceOfRegEn.REQUEST;
            case CASE_FINALISATION:
                return PlaceOfRegEn.REQUEST_FINALISATION;
            case NOT_SET:
            case BEFORE_BILLING:
            default:  return PlaceOfRegEn.BEFORE_BILLING;
        }
    }

    private void checkRiskDetailsFromAuditReasons(TWmRisk billingRisk) {
        TWmRequest request = processServiceBean.get().getLatestRequest4Case(currentCase.getId());
        if(request == null ){
            return;
        }
        billingRisk.setRequest(request);
        Set<TWmMdkAuditReasons> auditReasons = request.getAuditReasons();
        if(auditReasons == null){
            return;
        }
        List<RiskAreaEn> usedReasons = new ArrayList<>();
        for (TWmMdkAuditReasons auditReason : auditReasons) {
            CMdkAuditreason commonReason = MenuCache.getMenuCacheAuditReasons().get(auditReason.getAuditReasonNumber());
            if(usedReasons.contains(commonReason.getMdkArRiskArea())){
                continue;
            }
            usedReasons.add(commonReason.getMdkArRiskArea());
            TWmRiskDetails detail = new TWmRiskDetails();
            detail.setRisk(billingRisk);
            detail.setRiskArea(commonReason.getMdkArRiskArea());
            detail.setRiskPercent(0);
            detail.setRiskValue(BigDecimal.valueOf(0));
            detail.setRiskAuditPercent(100);
            detail.setRiskAuditPercentSugg(100);
            detail.setRiskWastePercent(0);
            detail.setRiskWastePercentSugg(0);
            detail.setRiskBaseFee(getdFee4case(billingRisk.getCaseDetails(), VersionRiskTypeEn.BEFORE_BILLING));
            detail.setRiskNotCalculatedFee(detail.getRiskBaseFee());
            detail.setRiskUsedForAuditFl(true);
            billingRisk.getRiskDetails().add(detail);
            
        }
        
    }

    private BigDecimal getdFee4case(TCaseDetails pCase4Risk, VersionRiskTypeEn pType) {
        if(pCase4Risk == null 
                || pCase4Risk.getCsdVersRiskTypeEn() == null
                || pCase4Risk.getCsdVersRiskTypeEn().equals(VersionRiskTypeEn.BEFORE_BILLING) 
                || pCase4Risk.getCsdVersRiskTypeEn().equals(VersionRiskTypeEn.NOT_SET)){
            return BigDecimal.ZERO;
        }
// find billig version
        List <TCaseDetails> ret = caseServiceBean.get().findVersionsWithActiveRisk(getCurrentCaseId(), true,  true);
        if(ret == null || ret.isEmpty()){
            return BigDecimal.ZERO;
        }
        for(TCaseDetails det: ret){
            if(det.getCsdVersRiskTypeEn() == null){
                continue;
            }
            if(det.getCsdVersRiskTypeEn().equals(pType)){
                // calculate full revenue for pCaseRisk and det
                // return delta
                return BigDecimal.valueOf(calculateFullRevenue4Version(det) - calculateFullRevenue4Version(pCase4Risk));
            }
        }
        return BigDecimal.ZERO;
    }
    
    private double calculateFullRevenue4Version(TCaseDetails pVersion){
        GDRGModel model = CpxClientConfig.instance().getSelectedGrouper();

        TGroupingResults result = processServiceBean.get().getGroupingResult(pVersion.getId(), model);
        if(result == null ){
            return 0;
        }
        
         if (getCurrentCase().getCsCaseTypeEn() == CaseTypeEn.PEPP) {
            Double suppl = getSupplementaryFeeValue(pVersion, SupplFeeTypeEn.ZP) ;
            Double suppl1 = getSupplementaryFeeValue(pVersion, SupplFeeTypeEn.ET);
            return result.getCasePepp().getRevenue() + (suppl == null?0:suppl) + (suppl1 == null?0:suppl1);
         }else{
            double bRate = baserateCatalog.findDrgBaserateFeeValue(getCurrentCase().getCsHospitalIdent(), pVersion.getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            double bCareRate = baserateCatalog.findCareBaserateFeeValue(getCurrentCase().getCsHospitalIdent(), pVersion.getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
            Double suppl = getSupplementaryFeeValue(pVersion, SupplFeeTypeEn.ZE) ;
             return result.getCaseDrg().getRevenue(bRate, bCareRate) + (suppl == null?0:suppl);
         }

    }

    public List<TWmRiskDetails> getRisksFromActualBillingVersion() {

        List<TWmRiskDetails> retDet =  riskEJB.get().findRisksFromActualBillingVersion(currentCase.getId());
        if(retDet == null || retDet.isEmpty()){
            // Message
            return new ArrayList<>();
        }
        return retDet;
    }

    public List<TWmRiskDetails> getRisksFromActualAuditVersion(TCaseDetails pFinalVersion) {
        List<TWmRiskDetails> retDet =  riskEJB.get().findRisksFromActualAuditVersion(currentCase.getId());
        if(retDet == null || retDet.isEmpty()){
            // Message
            return new ArrayList<>();
        }
        BigDecimal dFee = getdFee4case(pFinalVersion, VersionRiskTypeEn.BEFORE_BILLING);
        if(!dFee.equals(BigDecimal.ZERO) ){
            for(TWmRiskDetails det: retDet){
                if(det.getRiskNotCalculatedFee() == null || det.getRiskNotCalculatedFee().doubleValue() == 0){
                   det.setRiskBaseFee(dFee);
                   det.setRiskNotCalculatedFee(dFee);
                }
            }
        }
        return retDet;
    }

    public double getDeltaFee4Version(TCaseDetails pDetails) {
        if(pDetails == null || pDetails.getCsdVersRiskTypeEn() == null || 
                pDetails.getCsdVersRiskTypeEn().equals(VersionRiskTypeEn.NOT_SET) || pDetails.getCsdVersRiskTypeEn().equals(VersionRiskTypeEn.BEFORE_BILLING)){
            return 0;
        }
//        if(pDetails.getCsdVersRiskTypeEn().equals(VersionRiskTypeEn.AUDIT_MD) || pDetails.getCsdVersRiskTypeEn().equals(VersionRiskTypeEn.AUDIT_CASE_DIALOG)){
//            return getdFee4case(pDetails, VersionRiskTypeEn.BEFORE_BILLING).doubleValue();
//        }
//        return getdFee4case(pDetails, VersionRiskTypeEn.AUDIT_MD).doubleValue();
          return getdFee4case(pDetails, VersionRiskTypeEn.BEFORE_BILLING).doubleValue();
    }

    public List<TCaseDetails> check4ActualRisk4Case(VersionRiskTypeEn pVersionRiskType) {
        return caseServiceBean.get().findVersionsWithActiveRisk4Req(getCurrentCaseId(), pVersionRiskType, true);

    }

    /**
     * for all risks of the actual case with pVersRiskTypeEn from pRisk the flag RISK_ACTUAL_4_REG will be resetet.the pRisk will not be included
     * @param pRisk actual risk
     * @param pVersRiskTypeEn version type

     * @return  
     */
    public int resetActual4OtherRisks(TWmRisk pRisk, VersionRiskTypeEn pVersRiskTypeEn) {
        return riskEJB.get().resetActual4OtherRisks(getCurrentCaseId(), pRisk.getId(), pVersRiskTypeEn, true);
    }

    public ReadOnlyRequestDTO getLatestRequestForRiskType(VersionRiskTypeEn pType){
        return caseEJB.get().getLatestRequestForRiskType(getCurrentCaseId(), pType);
    }

    public Long getRiskTypePredecessorVersionId(VersionRiskTypeEn param) {
        param = Objects.requireNonNullElse(param, VersionRiskTypeEn.NOT_SET);
        if(VersionRiskTypeEn.NOT_SET.equals(param) || VersionRiskTypeEn.BEFORE_BILLING.equals(param)){
            //no predecessor definied for these yet
            return null;
        }
        return caseEJB.get().getRiskTypePredecessorVersionId(getCurrentCaseId(),param);
    }

    public TCaseDetails getKisBaseVersion(TCaseDetails pPredecessorVersion) {
        return caseEJB.get().getKisBaseVersion(pPredecessorVersion.getId());
    }



    public TWmRisk getActualRisk(PlaceOfRegEn placeOfRegEn, VersionRiskTypeEn versionRiskTypeEn) {
        return riskEJB.get().findActualRisk(currentCase.getId(), placeOfRegEn, versionRiskTypeEn);
    }

    public boolean isBasicForProcess(TCaseDetails item) {
        return processServiceBean.get().isDetailBasicForProcess(item.getId());
    }

    public boolean hasGroupingResult(Long[] pIds) {
        return caseEJB.get().hasGroupingResult(CpxClientConfig.instance().getSelectedGrouper(),pIds);
    }
    
    public boolean hasGroupingResult(GDRGModel pModel,Long[] pIds) {
        return caseEJB.get().hasGroupingResult(pModel,pIds);
    }

    public Set<TCaseDepartment> findDepartments(long pDetailsId) {
        return caseServiceBean.get().findDepartmentsLazy(pDetailsId);
    }
    
    public List<TCaseIcd> findIcdsForDepartment(TCaseDepartment pDepartment){
        if(pDepartment == null){
            return new ArrayList<>();
        }
        return caseServiceBean.get().getIcdsOfDept(pDepartment.getId());
    }
    public List<TCaseOps> findOpsesForDepartment(TCaseDepartment pDepartment){
        if(pDepartment == null){
            return new ArrayList<>();
        }
        return caseServiceBean.get().getOpsOfDept(pDepartment.getId());
    }
    public List<TCaseIcd> findIcdsForWard(TCaseWard pWard){
        if(pWard == null){
            return new ArrayList<>();
        }
        return caseServiceBean.get().getIcdsOfWard(pWard.getId());
    }
    public List<TCaseOps> findOpsesForWard(TCaseWard pWard){
        if(pWard == null){
            return new ArrayList<>();
        }
        return caseServiceBean.get().getOpsOfWard(pWard.getId());
    }

    public List<TCaseWard> findWardsForDepartment(TCaseDepartment pDepartment) {
        if(pDepartment == null){
            return new ArrayList<>();
        }
        return caseServiceBean.get().getWardsOfDept(pDepartment.getId());
    }

    public boolean hasManyDrgCases() {
        if(currentCase == null || currentCase.getPatient() == null){
            return false;
        }
        return caseServiceBean.get().getPatientCasesCount(CaseTypeEn.DRG, currentCase.getPatient().getId()) > 1;
    }

    public boolean hasManyPeppCases() {
        if(currentCase == null || currentCase.getPatient() == null){
            return false;
        }
        return caseServiceBean.get().getPatientCasesCount(CaseTypeEn.PEPP, currentCase.getPatient().getId()) > 1;
    }

    public boolean allIcdsGrouped(TCaseDetails pVersion, GDRGModel pModel) {
        return caseEJB.get().allIcdsGrouped(pVersion.getId(), pModel);
    
    }

    public void setOrUpdateRuleSelectFlag(CpxSimpleRuleDTO rule) {
        if(rule != null && rule.getRuleId() != null){
            caseServiceBean.get().setOrDeleteRuleSelectFlag(rule.getRuleId(), getCurrentCase(), rule.isSelectedRuleFlag());
        }
    }

    public boolean canSelectRules() {

        return Session.instance().getRoleProperties().canSetRelevanceFlag();
    }

    public List<TCaseBill> getCaseBills4Details(TCaseDetails pDetails) {
        return caseEJB.get().findCaseBills4Details(pDetails.getId());
    }
}
