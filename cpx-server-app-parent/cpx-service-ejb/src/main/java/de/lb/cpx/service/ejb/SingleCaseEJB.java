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
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.grouper.model.dto.IcdOverviewDTO;
import de.lb.cpx.grouper.model.dto.OpsOverviewDTO;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseBill;
import de.lb.cpx.model.TCaseComment;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCheckResult;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TMibi;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.TPatientDetails;
import de.lb.cpx.model.enums.CaseDetailsCancelReasonEn;

import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.model.enums.CommentTypeEn;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.auth.CpxUser;
import de.lb.cpx.server.dao.TCaseBillDao;
import de.lb.cpx.server.dao.TCaseCommentDao;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.server.dao.TCaseDepartmentDao;
import de.lb.cpx.server.dao.TCaseDetailsDao;
import de.lb.cpx.server.dao.TCaseFeeDao;
import de.lb.cpx.server.dao.TCaseIcdDao;
import de.lb.cpx.server.dao.TCaseOpsDao;
import de.lb.cpx.server.dao.TCheckResultDao;
import de.lb.cpx.server.dao.TGroupingResultsDao;
import de.lb.cpx.server.dao.TMibiDao;
import de.lb.cpx.server.dao.TPatientDao;
import de.lb.cpx.server.dao.TPatientDetailsDao;
import de.lb.cpx.server.generator.SequenceNumberGenerator;
import de.lb.cpx.server.wm.dao.TWmProcessCaseDao;
import de.lb.cpx.server.wm.dao.TWmRequestDao;
import static de.lb.cpx.service.ejb.CpxAuthorizationChecker.*;
import de.lb.cpx.service.jms.producer.StatusBroadcastProducer;
import de.lb.cpx.shared.dto.ReadOnlyRequestDTO;
import de.lb.cpx.shared.dto.VersionDetailsDTO;
import de.lb.cpx.shared.dto.broadcast.BroadcastOriginEn;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRequestMdk;
import de.lb.cpx.wm.model.enums.WmRequestTypeEn;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import org.hibernate.Hibernate;

/**
 * Implement Interface for Service Access to show a Single Case in the Client
 *
 * @author wilde
 */
@Stateless
public class SingleCaseEJB implements SingleCaseEJBRemote {

    private static final Logger LOG = Logger.getLogger(SingleCaseEJB.class.getName());

    @Inject
    private TCaseDao caseDao;
    @Inject
    private TPatientDao patientDao;
    @Inject
    private TCaseDepartmentDao departmentDao;
    @Inject
    private TCaseDetailsDao caseDetailsDao;
    @Inject
    private TPatientDetailsDao patientDetailsDao;
    @Inject
    private TCaseIcdDao icdDao;
    @Inject
    private TCaseOpsDao opsDao;
    @Inject
    private TGroupingResultsDao groupingResultsDao;
    @Inject
    private TCheckResultDao checkResultDao;
    @Inject
    private SequenceNumberGenerator gen;
    @Inject
    private TCaseFeeDao feeDao;
   @Inject
    private TCaseBillDao billDao;
    @Inject
    private TCaseCommentDao commentDao;
    @Inject
    private TMibiDao mibiDao;
    @Inject
    private StatusBroadcastProducer<long[]> broadcast;
    @Inject
    private TWmProcessCaseDao processCaseDao;
    @Inject
    private TWmRequestDao requestDao;
//    @Inject
//    private TWmRiskDao riskDao;
//    @EJB
//    private TWmProcessCaseDao processCaseDao;
//    @EJB
//    private TWmProcessDao processDao;
//    @EJB
//    private TWmEventDao eventDao;
//    @EJB
//    private TWmRequestDao requestDao;
//    @EJB
//    private CdbUserRolesDao userRolesDao;

//    @Override
//    public Boolean deleteCase(Long caseId) {
//        return caseDao.deleteById(caseId);
//    }
    @Override
    public void deleteCase(final long pCaseId) throws CpxIllegalArgumentException {
        checkDeleteCase();
        final TCase cs = caseDao.findById(pCaseId);
        if (cs == null) {
            final String message = "Hospital case with id " + pCaseId + " does not exist. Maybe this case was already deleted by another user. Deletion aborted!";
            LOG.log(Level.WARNING, message);
            //throw new CpxIllegalArgumentException(message);
            throw new CpxIllegalArgumentException(Lang.getCaseDoesNotExistWithReason(String.valueOf(pCaseId)));
        }

        final CpxUser user = ClientManager.getCurrentCpxUser();
        final String database = ClientManager.getActualDatabase();
        LOG.log(Level.INFO, "Will remove hospital case: " + cs.getCaseSignature() + " (initiated by " + String.valueOf(user) + " on database " + database + ")...");

        try {
            caseDao.deleteCase(cs);
        } catch (final RuntimeException ex) {
            throw new CpxIllegalArgumentException("Was not able to delete case " + cs.getCaseSignature(), ex);
        }
        broadcast.send(BroadcastOriginEn.DELETE_CASE, "Der Fall " + cs.getCaseKey() + " wurde gelöscht", new long[]{pCaseId});

        LOG.log(Level.INFO, "Hospital case " + cs.getCaseSignature() + " was successfully removed by user " + String.valueOf(user) + " on database " + database + "!");
    }

    @Override
    public void cancelCase(final long pCaseId) throws CpxIllegalArgumentException {
        checkCancelCase();
        final TCase cs = caseDao.findById(pCaseId);
        if (cs == null) {
            final String message = "Hospital case with id " + pCaseId + " does not exist. Maybe this case was already deleted by another user. Deletion aborted!";
            LOG.log(Level.WARNING, message);
            throw new CpxIllegalArgumentException(Lang.getCaseDoesNotExistWithReason(String.valueOf(pCaseId)));
        }

        final CpxUser user = ClientManager.getCurrentCpxUser();
        final String database = ClientManager.getActualDatabase();
        LOG.log(Level.INFO, "Will cancel hospital case: " + cs.getCaseSignature() + " (initiated by " + String.valueOf(user) + " on database " + database + ")...");
        caseDao.cancelCase(cs);

        LOG.log(Level.INFO, "Hospital case " + cs.getCaseSignature() + " was successfully canceled by user " + String.valueOf(user) + " on database " + database + "!");
    }

    @Override
    public void unCancelCase(final long pCaseId) throws CpxIllegalArgumentException, CloneNotSupportedException {
        checkCancelCase();
        final TCase cs = caseDao.findById(pCaseId);
        if (cs == null) {
            final String message = "Hospital case with id " + pCaseId + " does not exist. Maybe this case was already deleted by another user. Deletion aborted!";
            LOG.log(Level.WARNING, message);
            throw new CpxIllegalArgumentException(Lang.getCaseDoesNotExistWithReason(String.valueOf(pCaseId)));
        }

        final CpxUser user = ClientManager.getCurrentCpxUser();
        final String database = ClientManager.getActualDatabase();
        LOG.log(Level.INFO, "Will cancel hospital case: " + cs.getCaseSignature() + " (initiated by " + String.valueOf(user) + " on database " + database + ")...");
        caseDao.unCancelCase(cs);

        LOG.log(Level.INFO, "Hospital case " + cs.getCaseSignature() + " was successfully canceled by user " + String.valueOf(user) + " on database " + database + "!");
    }

    @Override
    public TCase findSingleCaseForCaseNumberAndIdent(String caseNumber, String hospitalIdent) {
        TCase caseForId = caseDao.findCaseForCaseNumberAndIdent(caseNumber, hospitalIdent);
        return caseForId;
    }

    @Override
    public List<Long> findCaseIdsForCaseNumber(String caseNumber) {
        return caseDao.findCaseIdsForCaseNumber(caseNumber);
    }

    @Override
    public List<Long> findCaseIdsForCaseNumber(String csHospitalIdent, String caseNumber) {
        return caseDao.findCaseIdsForCaseNumber(csHospitalIdent, caseNumber);
    }

    @Override
    public List<TCase> findCasesForCaseNumber(String caseNumber) {
        List<TCase> caseList = caseDao.findCasesForCaseNumber(caseNumber);
        return caseList;
    }

    @Override
    public List<TCase> findPotentialCasesForCaseNumber(String caseNumber) {
        List<TCase> caseList = caseDao.findPotentialCasesForCaseNumber(caseNumber);
        return caseList;
    }

    @Override
    public List<TPatient> findPatientsForPatientNumber(String pPatientNumber) {
        List<TPatient> patientList = caseDao.findPatientsForPatientNumber(pPatientNumber);
        return patientList;
    }

    @Override
    public List<TPatient> findPotentialPatientsForPatientNumber(String pPatientNumber) {
        List<TPatient> patientList = caseDao.findPotentialPatientsForPatientNumber(pPatientNumber);
        return patientList;
    }

    @Override
    public TPatient findPatientForId(Long id) {
        TPatient patientForId = patientDao.findPatientByIdEager(id);
        return patientForId;
    }

    @Override
    public List<TCase> findCaseListForPatient(long id) {
        List<TCase> caseList = caseDao.findListOfTCaseForPatient(id);
        if (caseList == null) {
            LOG.log(Level.WARNING, "CaseList is null!");
        }
        return caseList;
    }

    @Override
    public List<TCaseDepartment> findTCaseDepartmentsForCaseDetailId(Long caseDetailId) {
        return departmentDao.findListByCaseDetailsId(caseDetailId,true);
    }

    @Override
    public List<TCaseIcd> findIcdsForCaseDetailId(Long caseDetailId, GDRGModel pModel) {
        return icdDao.findListOfIcd_nativ(caseDetailId, pModel, true);
    }

    @Override
    public TCaseIcd findMainDiagnosisIcdForCaseDetailId(Long caseDetailId) {
        return icdDao.findMainDiagnosisIcd(caseDetailId);
    }

    @Override
    public List<TCaseOps> findOpsForCaseDetailsId(Long caseDetailsId) {
        return opsDao.findListOfOps(caseDetailsId);
    }

    @Override
    public TCaseDepartment findDepartmentForId(Long departmentId) {
        return departmentDao.findEagerById(departmentId);
    }

    @Override
    public List<TGroupingResults> findGroupingResults(Long caseDetailsId, GDRGModel modelIdent) {
        return groupingResultsDao.findTGroupingResults(caseDetailsId, modelIdent);
    }

    @Override
    public TCase findSingleCaseForDatabaseId(long id) {
        LOG.info("find case for id " + id);
        TCase foundCase = caseDao.findById(id);
        if (foundCase == null) {
            LOG.warning("No case found for id " + id);
        } else {
            LOG.log(Level.FINER, "id of found case " + foundCase.getId());
            //        caseForId.getCaseDetails();
        }
        return foundCase;
    }

    @Override
    public void saveCase(TCase currentCase, List<TCaseIcd> icdList, List<TCaseOps> opsList) {
        caseDao.merge(currentCase);
        icdDao.mergeList(icdList);
        opsDao.mergeList(opsList);
    }

    @Override
    public TGroupingResults findNewestTGroupingResult(Long caseDetailsId, GDRGModel modelIdent, Long icdIdMainDiagnosis) {
//        return groupingResultsDao.findTGroupingResultDesc(caseDetailsId, modelIdent, icdIdMainDiagnosis);
        long start = System.currentTimeMillis();
        TGroupingResults result = groupingResultsDao.findTGroupingResultDesc(caseDetailsId, modelIdent, icdIdMainDiagnosis);
        LOG.info("create result in " + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public TCaseIcd saveTCaseIcd(@NotNull TCaseIcd icd) {
        icd = Objects.requireNonNull(icd, "Icd could not be null");
        long time = System.currentTimeMillis();
//        if (icd == null) {
//            throw new IllegalArgumentException("icd is null!");
//        }
//        try {
//            icd = icdDao.mergeAndFlush(icd);
//        } catch (RuntimeException ex) {
////            if (ex.getCause() instanceof EntityNotFoundException) {
////                LOG.log(Level.SEVERE, "icd with id " + icd.id + " does not seem to exist anymore!");
////            }
//            LOG.log(Level.FINE, "Runtime error while saving icd with id " + icd.id + " occured", ex);
//            //icd = icdDao.mergeAndFlush(icd);
//        }
        //AWi-20190823 - make saving of icd similar to saving ops
        //remove try catch to not suppress an error 
        icd = icdDao.merge(icd);
        icd = icdDao.saveOrUpdate(icd);
        LOG.fine("save icd in " + (System.currentTimeMillis() - time) + " ms");
        return icd;
    }

    @Override
    public TCaseOps saveTCaseOps(@NotNull TCaseOps ops) {
        ops = Objects.requireNonNull(ops, "Ops could not be null");
        long time = System.currentTimeMillis();
//        if (ops == null) {
//            throw new IllegalArgumentException("ops is null!");
//        }
//        try {
//            ops = opsDao.mergeAndFlush(ops);
//            ops = opsDao.saveOrUpdate(ops);
//        } catch (RuntimeException ex) {
//            if (ex.getCause() instanceof EntityNotFoundException) {
//                LOG.log(Level.SEVERE, "ops with id " + ops.id + " does not seem to exist anymore!");
//            }
//            LOG.log(Level.SEVERE, "Runtime error while saving ops with id " + ops.id + " occured", ex);
        //ops = opsDao.mergeAndFlush(ops);
//        }

        //AWi-20190823 - fix issue with not persisting ops for case simulation
        //remove try catch to not suppress an error 
        ops = opsDao.merge(ops);
        ops = opsDao.saveOrUpdate(ops);
        LOG.fine("save ops in " + (System.currentTimeMillis() - time) + " ms");
        return ops;
    }

//    @Override
//    public List<TGroupingResults> getGroupringResultsForList(long caseDetailsId, GDRGModel modelIdent, List<TCaseIcd> icdLocal) {
//        List<TGroupingResults> resultList = new ArrayList<>();
//        for(TCaseIcd icd : icdLocal){
//            resultList.add(groupingResultsDao.findTGroupingResultDesc(caseDetailsId, modelIdent, icd.getId()));
//        }
//        return resultList;
//    }
    @Override
    public TCase findSingleCaseForId(Long caseId) {
        long start = System.currentTimeMillis();
        TCase caseForId = caseDao.findCaseForCaseNumberEager(caseId);
        if (caseForId == null) {
            LOG.warning("No case found for id " + caseId + " in " + (System.currentTimeMillis() - start) + " ms");
        } else {
            LOG.info("id of found case " + caseForId.getId() + " in " + (System.currentTimeMillis() - start) + " ms");
            //        caseForId.getCaseDetails();
        }
        return caseForId;
    }

    @Override
    public TCase findSingleCaseForIdForDocumentImport(Long caseId) {
        long start = System.currentTimeMillis();
        TCase caseForId = caseDao.findCaseForCaseNumberEagerForDocumentImport(caseId);
        if (caseForId == null) {
            LOG.warning("No case found for id " + caseId + " in " + (System.currentTimeMillis() - start) + " ms");
        } else {
            LOG.info("id of found case " + caseForId.getId() + " in " + (System.currentTimeMillis() - start) + " ms");
            //        caseForId.getCaseDetails();
        }
        return caseForId;
    }

    @Override
    public Long saveCaseEntity(TCase currCase) {
//        for (TCaseDetails details : currCase.getCaseDetails()) {
//            LOG.info("hashcode " + details.hashCode());
//        }
        return caseDao.mergeAndFlush(currCase).getVersion();
    }

    @Override
    public Long saveCaseDetailsEntity(TCaseDetails details) {
        return caseDetailsDao.mergeAndFlush(details).getVersion();
    }

    @Override
    public Long savePatientDetailsEntity(TPatientDetails patDetails) {
        return patientDetailsDao.mergeAndFlush(patDetails).getVersion();
    }

    @Override
    public Long savePatientEntity(TPatient patient) {
        return patientDao.mergeAndFlush(patient).getVersion();
    }

    @Override
    public VersionDetailsDTO getVersionDetails(long id) {
        VersionDetailsDTO dto = new VersionDetailsDTO();
        TCaseDetails details = caseDetailsDao.findById(id);
        if (details == null) {
            LOG.warning("No details found for id " + id);
            return dto;
        }
        TCaseIcd principalIcd = details.getPrincipalCaseIcd();
//        TCaseIcd principalIcd = icdDao.findMainDiagnosisIcd(id);
        Number numberOfIcd = icdDao.countForDetailsId(id);
        Number numberOfOps = opsDao.countForDetailsId(id);
        TGroupingResults groupingResult = null;

        for (TGroupingResults results : details.getGroupingResultses()) {
            if (results.getCaseIcd().getIcdcIsHdxFl()) {
                groupingResult = results;
                break;
            }
        }
        dto.setAdmReason(details.getCsdAdmReason12En().getLangKey());
        if (principalIcd != null) {
            dto.setPrincipalDiagnosis(principalIcd.getIcdcCode());
        }
        dto.setComment(details.getComment());
        dto.setNumberOfIcd(numberOfIcd);
        dto.setNumberOfOps(numberOfOps);
        if (groupingResult != null) {
            dto.setDrgPeppCode(groupingResult.getGrpresCode());
        }
        return dto;
    }

    @Override
    public Long createNewVersion(Long caseId, Long versionToCloneFrom) {
        TCase hospitalCase = caseDao.findById(caseId);
        if (hospitalCase == null) {
            LOG.warning("No case found for id " + caseId);
        }
        TCaseDetails detailsToClone = caseDetailsDao.findById(versionToCloneFrom);
        if (detailsToClone == null) {
            LOG.warning("No details to clone found for id " + versionToCloneFrom);
        }
        try {
            TCaseDetails clone = cloneVersion(hospitalCase, detailsToClone);

            clone.setCsdVersRiskTypeEn(getVersionRiskType(hospitalCase));
            return clone.getId();
        } catch (CpxIllegalArgumentException ex) {
            LOG.log(Level.SEVERE, "Cloning object failed", ex);
        }
        return null;
    }

    private TCaseDetails cloneVersion(TCase hospitalCase, TCaseDetails toClone) throws CpxIllegalArgumentException {
        if (hospitalCase == null) {
            throw new IllegalArgumentException("hospitalCase is null!");
        }
        TCaseDetails cloned = caseDetailsDao.createNewVersionFrom(toClone);
        cloned.setCsdIsActualFl(false);
//        cloned.setComment(cloned.getComment().concat(comment));
        cloned.setHospitalCase(hospitalCase);
        cloned =  caseDetailsDao.saveOrUpdate(cloned);
        TCaseDetails curLoc = hospitalCase.getCurrentLocal();
        if (curLoc != null) {
            curLoc.setCsdIsActualFl(false);
            caseDetailsDao.flush();
        }

        hospitalCase.setCsStatusEn(CaseStatusEn.NEW_VERS);
        hospitalCase.setCurrentLocal(cloned);
        
        return cloned;
    }

    @Override
    public Boolean deleteVersion(Long versionId) {
        caseDetailsDao.deleteById(versionId);
        return true;
    }

    @Override
    public Boolean createNewVersion(TCase hCase, TCaseDetails versionToCloneFrom, String comment) {
        try {
            TCase hospitalCase = caseDao.merge(hCase);
            TCaseDetails detailsToClone = caseDetailsDao.merge(versionToCloneFrom);
            cloneVersion(hospitalCase, detailsToClone);
            return true;
        } catch (CpxIllegalArgumentException exc) {
            LOG.log(Level.SEVERE, "Can't create new Version, reason: " + exc.getMessage(), exc);
        }
        return false;
    }

    @Override
    public TCaseDetails deleteVersion(TCaseDetails version) throws CpxIllegalArgumentException {
        if (version == null) {
            throw new IllegalArgumentException("version is null!");
        }
        long startTime = System.currentTimeMillis();
        if(caseDetailsDao.isDetached(version)){
            version = caseDetailsDao.attach(version);
        }
//        long total = System.currentTimeMillis();
//        //version = caseDetailsDao.merge(version);
//        //Logger.getLogger(getClass().getSimpleName()).info("merge version: "+ version.getId() + ": " + (System.currentTimeMillis()-time)+" ms");
//        time = System.currentTimeMillis();
//        //caseDao.removeVersionFromCase(version.getHospitalCase(), version);
//        LOG.info("remove version from case: " + version.getId() + ": " + (System.currentTimeMillis() - time) + " ms");
//        time = System.currentTimeMillis();
        //for(TCaseDepartment dep: version.getCaseDepartments()) {
        //  em.createNativeQuery("UPDATE T_CASE_ICD SET ICDC_REF_ID = NULL where T_CASE_ICD.T_CASE_DEPARTMENT_ID = " + dep.id);
        //}
        //em.createNativeQuery("DELETE FROM T_CASE_DETAILS WHERE ID = " + version.id);
        //caseDetailsDao.deleteVersion(version,version.getHospitalCase());
        TCase cs = version.getHospitalCase();
        TCaseDetails parentVersion = caseDetailsDao.deleteVersion(version);
        LOG.log(Level.INFO, "Delete Version: {0} in {1} ms", new Object[]{version.getId(), System.currentTimeMillis() - startTime});

//        long delete = System.currentTimeMillis();
        //LOG.log(Level.INFO, "parentVersion.getCsdIsLocalFl(): " + parentVersion.getCsdIsLocalFl());
        if (parentVersion == null || !parentVersion.getCsdIsLocalFl()) {
            TCaseDetails newActualDetails = caseDetailsDao.getDetailsWithMaxVersion(cs);
            cs.setCurrentLocal(newActualDetails);
        } else {
            cs.setCurrentLocal(parentVersion);
        }
//        LOG.info("done settings values after delete in " + (System.currentTimeMillis() - delete));
//        caseDao.persist(cs);
//        flush();
//        delete = System.currentTimeMillis();
//    em.refresh(cs);
//        LOG.info("done refresh and flush after delete in " + (System.currentTimeMillis() - delete));
        TCaseDetails newLocalVersion = cs.getCurrentLocal();
        if(newLocalVersion.getCaseDetailsByCsdParentId()!=null && !Hibernate.isInitialized(newLocalVersion.getCaseDetailsByCsdParentId())){
            Hibernate.initialize(newLocalVersion.getCaseDetailsByCsdParentId()); // initialize stuff to avoid exception
        }
//        LOG.info("remove version from caseDetails table : " + version.getId() + ": " + (System.currentTimeMillis() - time) + " ms");
        return newLocalVersion;
//        return null;
    }

    @Override
    public String getSuccessorDetails(TCaseDetails version) {
        if (version == null) {
            throw new IllegalArgumentException("version is null!");
        }
        List<TCaseDetails> listOfsuccessors = caseDetailsDao.getSuccessors(version);
        if (listOfsuccessors == null || listOfsuccessors.isEmpty()) {
            return null;
        }
        String successorDetails = "";
        LOG.log(Level.INFO, "list of successors {0}", listOfsuccessors.size());
        for (TCaseDetails successor : listOfsuccessors) {
            if (successor.getCsdIsLocalFl()) {
                successorDetails = successorDetails.concat("CP-Version " + successor.getCsdVersion() + "\n");
            } else {
                successorDetails = successorDetails.concat("KIS-Version " + successor.getCsdVersion() + "\n");
            }
        }
        return successorDetails;
    }

    @Override
    public List<TCheckResult> findDetectedRules(long idGroupingResults) {
        return checkResultDao.findDetectedRules(idGroupingResults, ClientManager.getCurrentCpxUser().getActualRole().getId());
    }

    @Override
    public TGroupingResults findGroupingResultForVersion(long pVersionId, GDRGModel pModel) {
//        return groupingResultsDao.findTGroupingResult(pVersionId, pModel);
        return groupingResultsDao.findGroupingResult_nativ(pVersionId, pModel);
    }

    @Override
    public TGroupingResults findGroupingResult(long pVersionId) {
//        return groupingResultsDao.findTGroupingResult(pVersionId, pModel);
        return groupingResultsDao.findGroupingResult_nativ(pVersionId);
    }
//    @Override
//    public List<IcdOverviewDTO> findAllIcdCodesForCase(long pCaseId) {
//        return icdDao.getAllIcdCodesForCase(pCaseId);
//    }

    @Override
    public List<IcdOverviewDTO> findAllIcdCodesForVersions(List<Long> pVersionIds, GDRGModel pModel) {
        return icdDao.getAllIcdCodesForVersions(pVersionIds, pModel);
    }

    @Override
    public List<OpsOverviewDTO> findAllOpsCodesForVersions(List<Long> pVersionIds) {
        return opsDao.getAllOpsCodesForVersions(pVersionIds);
    }

    @Override
    public void removeIcd(TCaseIcd pIcd) {
        pIcd = icdDao.merge(pIcd);
        icdDao.remove(pIcd);
//        icdDao.deleteById(pIcd.getId());
    }

    @Override
    public void removeOps(TCaseOps pOps) {
        pOps = opsDao.merge(pOps);
        opsDao.remove(pOps);
    }

    @Override
    public Long testSequence() {
        return gen.generateNextWorkflowNumberValue();
    }

    @Override
    public TGroupingResults findGroupingResultsLazy(Long pDetailsId, GDRGModel pGrouperModel) {
        TGroupingResults res = groupingResultsDao.findGroupingResult_nativ_lazy(pDetailsId, pGrouperModel);
        if (res != null) {
            Hibernate.initialize(res.getCaseIcd());
        }
        return res;
    }

    @Override
    public List<TCaseFee> findCaseFeesForDetails(long pDetailsId) {
        return feeDao.findTCaseFeesForDetails(pDetailsId);
    }

    @Override
    public TCaseDetails findCaseDetails(long pDetailsId) {
        TCaseDetails details = caseDetailsDao.findById(pDetailsId);
        if (details != null && details.getCaseDetailsByCsdParentId() != null) {
            Hibernate.initialize(details.getCaseDetailsByCsdParentId());
        }
//        details.getSortedDepartments();
        return details;
    }

    @Override
    public List<TCaseOps> findAllOpsCodes(long pId) {//, GDRGModel pModel) {
        return opsDao.findListOfOps_nativ(pId);
    }

    @Override
    public List<TCaseIcd> findAllIcdCodes(long pId, GDRGModel pModel) {
        return icdDao.findListOfIcd_nativ(pId, pModel, false);
    }

    @Override
    public List<TCaseComment> findAllComments(long pCaseId) {
        return commentDao.findAllForCase(pCaseId);
    }

    @Override
    public List<TCaseComment> findAllComments(long pCaseId, CommentTypeEn pType) {
        return commentDao.findAllForCaseAndType(pCaseId, pType);
    }

    @Override
    public TCaseComment findActiveComment(long pCaseId, CommentTypeEn pType) {
        return commentDao.findActiveComment(pCaseId, pType);
    }

    @Override
    public TCaseComment storeComment(TCaseComment pComment) {
        return commentDao.saveOrUpdate(pComment);
    }

    @Override
    public TCaseComment storeComment(Long pCaseId, TCaseComment pComment) {
        TCase cse = caseDao.findById(pCaseId);
        pComment.setTCaseId(cse);
        return commentDao.saveOrUpdate(pComment);
    }

    @Override
    public boolean removeComment(TCaseComment pComment) {
        commentDao.remove(pComment);
        return true;
    }

    @Override
    public boolean removeComment(Long pCommentId) {
        commentDao.remove(pCommentId);
        return true;
    }

    @Override
    public TCaseComment updateComment(TCaseComment pComment) {
        return commentDao.merge(pComment);
    }

    @Override
    public Boolean removeIcdById(Long pIcdId) {
        icdDao.remove(pIcdId);
        return true;
    }

    @Override
    public List<TMibi> findAllMibis(final long pCaseId) {
        List<TMibi> list = mibiDao.findAllForCase(pCaseId);
        for (TMibi mibi : list) {
            Hibernate.initialize(mibi.getAntibiogram());
            Hibernate.initialize(mibi.getAppraisal());
        }
        return list;
    }

    @Override
    public boolean caseExists(long pId) {
        return caseDao.exists(pId);
    }

    private VersionRiskTypeEn getVersionRiskType(TCase hospitalCase) {
        Objects.requireNonNull(hospitalCase, "Case can not be null!");
        TWmProcess process = processCaseDao.getLatestProcessForCase(hospitalCase.getId());
        if(process == null){
            //check here if discharge date is set
            //extern version is used, due to not commiting result based on random local version the user messed up
            TCaseDetails currentExtern = hospitalCase.getCurrentExtern();
            if(currentExtern != null && currentExtern.getCsdDischargeDate() != null){
                return VersionRiskTypeEn.BEFORE_BILLING;
            }else{
                return VersionRiskTypeEn.NOT_SET;
            }
//            return VersionRiskTypeEn.BEFORE_BILLING;
        }
//        if(!requestDao.existOpenRequestForProcess(process)){
//            return VersionRiskTypeEn.CASE_FINALISATION;
//        }
        TWmRequest latestRequest = requestDao.getLatestRequest(process.getId());
        if(latestRequest == null){
            return VersionRiskTypeEn.BEFORE_BILLING;
        }
        if(latestRequest.isRequestMdk()){
            TWmRequestMdk mdRequest = (TWmRequestMdk) latestRequest;
            if(mdRequest.getReportDate() != null || mdRequest.getMdkReportReceiveDate() != null){
                return VersionRiskTypeEn.CASE_FINALISATION;
            }
            return VersionRiskTypeEn.AUDIT_MD;
        }else if(latestRequest.isRequestAudit()){
            return VersionRiskTypeEn.AUDIT_CASE_DIALOG;
        }
        return VersionRiskTypeEn.NOT_SET;
    }
    private ReadOnlyRequestDTO getLatestRequestForRiskType(TCase pHospitalCase,VersionRiskTypeEn pType){
        Objects.requireNonNull(pHospitalCase, "Case can not be null!");
        if((!VersionRiskTypeEn.AUDIT_MD.equals(pType)) && (!VersionRiskTypeEn.AUDIT_CASE_DIALOG.equals(pType))){
            return null;
        }
        TWmProcess process = processCaseDao.getLatestProcessForCase(pHospitalCase.getId());
        if(process == null){
            return null;
        }
        return new ReadOnlyRequestDTO(requestDao.getLatestRequestForRequestType(process.getId(),convertRequestType(pType)),Objects.requireNonNullElse(process.getCreationDate(),new Date()));
    }
    
    @Override
    public List<TCaseDetails> findCaseDetails(long pCaseId,boolean pLocal,boolean pIncludeStorno){
        return caseDetailsDao.findCaseDetails(pCaseId,pLocal,pIncludeStorno);
    }
    @Override
    public List<TCaseDetails> findCaseDetails(long pCaseId,boolean pLocal){
        return findCaseDetails(pCaseId,pLocal,false);
    }
    @Override
    public List<TCaseDetails> findLocalCaseDetails(long pCaseId){
        return findCaseDetails(pCaseId,true);
    }
    @Override
    public List<TCaseDetails> findExternCaseDetails(long pCaseId){
        return findCaseDetails(pCaseId,false);
    }

    private Integer convertRequestType(VersionRiskTypeEn pType) {
        pType = Objects.requireNonNullElse(pType, VersionRiskTypeEn.NOT_SET);
        switch(pType){
            case AUDIT_MD:
                return WmRequestTypeEn.mdk.getId();
            case AUDIT_CASE_DIALOG:
                return WmRequestTypeEn.audit.getId();
            default:
                return null;
        }
    }

    @Override
    public ReadOnlyRequestDTO getLatestRequestForRiskType(long pHospitalId, VersionRiskTypeEn pRiskType) {
        TCase hosCase = caseDao.findById(pHospitalId);
        return getLatestRequestForRiskType(hosCase, pRiskType);
    }

    @Override
    public Long getRiskTypePredecessorVersionId(Long pHospitalId, VersionRiskTypeEn pRiskType) {
        if(pHospitalId == null){
            return null;
        }
        pRiskType = Objects.requireNonNullElse(pRiskType, VersionRiskTypeEn.NOT_SET);
        if(VersionRiskTypeEn.NOT_SET.equals(pRiskType)|| VersionRiskTypeEn.BEFORE_BILLING.equals(pRiskType)){
            //nothign to find here there is no predecessor for these types
            return null;
        }
        if(VersionRiskTypeEn.AUDIT_MD.equals(pRiskType)|| VersionRiskTypeEn.AUDIT_CASE_DIALOG.equals(pRiskType)){
            TCaseDetails version = caseDetailsDao.getActualBillingVersion(pHospitalId);
            return version.getId();
        }
        //now it becomes tricky..
        //if no other mode is present so it must be the case completion 
        //for this we need to find the latest request and determine by its type what
        //is the predecessor for latest md request it is audit md 
        
        //needs better implemenation, maybe inefficent and slow on nig databases?
        TWmProcess process = processCaseDao.getLatestProcessForCase(pHospitalId);
        if(process == null){
            //no process so nothing to find here
            return null;
        }
        TWmRequest latestRequest = requestDao.getLatestRequest(process.getId());
        if(latestRequest == null){
            // no request so nothing to find here
            return null;
        }
        if(latestRequest.isRequestMdk()){
            TCaseDetails version = caseDetailsDao.getActualMdVersion(pHospitalId);
            return version!=null?version.getId():null;
        }
        if(latestRequest.isRequestAudit()){
            TCaseDetails version = caseDetailsDao.getActualAuditVersion(pHospitalId);
            return version!=null?version.getId():null;
        }
        return null;
    }

    @Override
    public TCaseDetails getKisBaseVersion(long pCaseDetails) {
        TCaseDetails detail = caseDetailsDao.findById(pCaseDetails);
        TCaseDetails kisVersion = detail.getCaseDetailsByCsdExternId();
        if(kisVersion == null || kisVersion.getCsdIsLocalFl()){
            LOG.warning("Could find Kis-Version for CaseDetailsId: " + pCaseDetails);
            return null;
        }
        return kisVersion;
    }
    
    @Override
    public boolean hasGroupingResult(GDRGModel pModel, Long[] pIds){
        return groupingResultsDao.hasGroupingResult(pModel,pIds);
    }
    
    @Override    
    public boolean allIcdsGrouped(Long caseDetailsId, GDRGModel pModel){
         return groupingResultsDao.hasAllGrouped(pModel, caseDetailsId);
    }

    @Override
    public CaseDetailsCancelReasonEn getCancelReason(long pCaseId) throws CpxIllegalArgumentException, CloneNotSupportedException {
         final TCase cs = caseDao.findById(pCaseId);
        CaseDetailsCancelReasonEn ret = null;
        if (cs == null) {
            final String message = "Hospital case with id " + pCaseId + " does not exist. Maybe this case was already deleted by another user. Deletion aborted!";
            LOG.log(Level.WARNING, message);
            throw new CpxIllegalArgumentException(Lang.getCaseDoesNotExistWithReason(String.valueOf(pCaseId)));
        }
        if (!cs.getCsCancellationReasonEn()) {
            return null;
        } else {
            Iterator<TCaseDetails> itDetails = cs.getCaseDetails().iterator();

            while (itDetails.hasNext()) {
                TCaseDetails csd = itDetails.next();
                    if (CaseDetailsCancelReasonEn.MANUAL.equals(csd.getCsdCancelReasonEn())) {
                        return CaseDetailsCancelReasonEn.MANUAL;
                    }
                    if(ret == null || csd.getCsdCancelReasonEn()!= null && ret.getId()< csd.getCsdCancelReasonEn().getId()){
                        ret = csd.getCsdCancelReasonEn();
                    }
                }
            }

        return ret;

    }
    public List<TCaseBill> findCaseBills4Details(long id){
        return billDao.findTCaseFees4Details(id);
    }
}
