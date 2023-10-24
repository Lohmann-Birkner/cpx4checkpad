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
 *    2016  Somebody - initial API and implementation and/or initial documentation
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
import de.lb.cpx.model.enums.CommentTypeEn;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.shared.dto.ReadOnlyRequestDTO;
import de.lb.cpx.shared.dto.VersionDetailsDTO;
import java.util.List;
import javax.ejb.Remote;
import javax.validation.constraints.NotNull;

/**
 * Remote Interface Class, handles remote Service Calls for CaseDetails in
 * Client Implements Access to Patient, Case and GroupingResult Objects
 *
 * @author wilde
 */
@Remote
public interface SingleCaseEJBRemote {

//    /**
//     * Drops TCase Entity
//     *
//     * @param caseId unique ID of case
//     * @return was drop successful?
//     */
//    Boolean deleteCase(Long caseId);
    /**
     * Canceling ("Stornieren") a case extends deleteById() or remove() in a
     * way, that processes, documents and other stuff, where this case is
     * assigned to, also have to be deleted.
     *
     * @param pCaseId Hospital Case ID
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException cannot delete
     * case
     */
    void deleteCase(final long pCaseId) throws CpxIllegalArgumentException;

    /**
     *
     * @param pCaseId Hospital Case ID
     * @throws CpxIllegalArgumentException error
     */
    void cancelCase(final long pCaseId) throws CpxIllegalArgumentException;

    /**
     *
     * @param pCaseId Hospital Case ID
     * @throws CpxIllegalArgumentException error
     * @throws java.lang.CloneNotSupportedException error
     */
    void unCancelCase(final long pCaseId) throws CpxIllegalArgumentException, CloneNotSupportedException;
    
    CaseDetailsCancelReasonEn getCancelReason(final long pCaseId) throws CpxIllegalArgumentException, CloneNotSupportedException;

    /**
     * find TCase Entity for CaseNumber and hospitalIdent EagerLoaded in
     * CaseObject: TCase_.patient TCase_.currentExtern TCase_.currentLocal
     * TCase_.caseDetails.caseDetailsesForCsdParentId
     *
     * @param caseNumber unique CaseNumber in Database, field CsCaseNumber
     * @param hospitalIdent Hospital Identifier (IKZ)
     * @return single TCase-Entity Object
     */
    TCase findSingleCaseForCaseNumberAndIdent(String caseNumber, String hospitalIdent);

    List<Long> findCaseIdsForCaseNumber(String caseNumber);

    List<Long> findCaseIdsForCaseNumber(String csHospitalIdent, String caseNumber);

    /**
     * find TCase Entity for CaseNumber
     *
     * @param caseNumber unique CaseNumber in Database, field CsCaseNumber
     * @return single TCase-Entity Object
     */
    List<TCase> findCasesForCaseNumber(String caseNumber);

    List<TCase> findPotentialCasesForCaseNumber(String caseNumber);

    List<TPatient> findPatientsForPatientNumber(String pPatientNumber);

    List<TPatient> findPotentialPatientsForPatientNumber(String pPatientNumber);

    /**
     * find TCase Entity for CaseNumber and hospitalIdent EagerLoaded in
     * CaseObject: TCase_.patient TCase_.currentExtern TCase_.currentLocal
     * TCase_.caseDetails.caseDetailsesForCsdParentId
     *
     * @param caseId Db-Id of the Case
     * @return single TCase-Entity Object
     */
    TCase findSingleCaseForId(Long caseId);

    TCase findSingleCaseForIdForDocumentImport(Long caseId);

    /**
     * find TPatient Entity for Database Id
     *
     * @param id Database Id
     * @return TPatient Entity
     */
    TPatient findPatientForId(Long id);

    /**
     * find List of TCase Entites for a Patient-Id
     *
     * @param id Patient Database Id
     * @return TPatient Entity for given Id
     */
    List<TCase> findCaseListForPatient(long id);

    /**
     * finds List of all TCaseDepartments for TCaseDetail Id
     *
     * @param caseDetailId TCaseDetail Id
     * @return List of TCaseDetpartment Entities
     */
    List<TCaseDepartment> findTCaseDepartmentsForCaseDetailId(Long caseDetailId);

    /**
     * find List of TCaseIcd Entites for a caseDetails Id
     *
     * @param caseDetailId TcaseDetail Id
     * @param pModel selected grouper model
     * @return List of TCaseIcd Entities
     */
    List<TCaseIcd> findIcdsForCaseDetailId(Long caseDetailId, GDRGModel pModel);

    /**
     * find TCaseIcd Entity, which is MainDiagnosis for
     *
     * @param caseDetailId Case Details ID
     * @return Case ICD
     */
    TCaseIcd findMainDiagnosisIcdForCaseDetailId(Long caseDetailId);

    /**
     * find List of TCaseOps Entites for a caseDetails Id
     *
     * @param caseDetailId TcaseDetail Id
     * @return List of TCaseIcd Entities
     */
    List<TCaseOps> findOpsForCaseDetailsId(Long caseDetailId);

    /**
     * find TCaseDepartment for Database Id
     *
     * @param departmentId TCaseDepartment Id
     * @return TCaseDepartment Entitiy
     */
    TCaseDepartment findDepartmentForId(Long departmentId);

    /**
     * find List of TGroupringResults, with TCaseDetails Id and GrouperModell
     * Identifierer
     *
     * @param caseDetailsId TCaseDetails Id
     * @param modelIdent Groupermodel Identifierer
     * @return List of TGroupringResults Entities
     */
    List<TGroupingResults> findGroupingResults(Long caseDetailsId, GDRGModel modelIdent);

    TCase findSingleCaseForDatabaseId(long id);

    void saveCase(TCase currentCase, List<TCaseIcd> icdList, List<TCaseOps> opsList);

    TGroupingResults findNewestTGroupingResult(Long caseDetailsId, GDRGModel modelIdent, Long icdIdMainDiagnosis);

    TCaseIcd saveTCaseIcd(@NotNull TCaseIcd icd);

    TCaseOps saveTCaseOps(@NotNull TCaseOps ops);

//    List<TGroupingResults> getGroupringResultsForList(long id, GDRGModel grouperModel, List<TCaseIcd> icdLocal);
    Long saveCaseEntity(TCase currCase);

    Long saveCaseDetailsEntity(TCaseDetails details);

    Long savePatientDetailsEntity(TPatientDetails patDetails);

    Long savePatientEntity(TPatient patient);

    VersionDetailsDTO getVersionDetails(long id);

    /**
     * create new Version from existing Version with a specific comment
     *
     * @param caseId Database Id of the Case where new Version Should belong to
     * @param versionToCloneFrom version to clone
     * @return newly created details databse id or null
     */
    Long createNewVersion(Long caseId, Long versionToCloneFrom);

    /**
     * deletes Version from Database, check if Version
     *
     * @param versionId Database id of the Version to be deleted
     * @return if delete was successful
     */
    Boolean deleteVersion(Long versionId);

    /**
     * create new Version from existing Version with a specific comment
     *
     * @param hCase Database Id of the Case where new Version Should belong to
     * @param versionToCloneFrom version to clone
     * @param comment user comment to the new Version
     * @return if creation was successful
     */
    Boolean createNewVersion(TCase hCase, TCaseDetails versionToCloneFrom, String comment);

    /**
     * deletes Version from Database, check if Version
     *
     * @param version Database id of the Version to be deleted
     * @return previous version
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException invalid argument
     */
    TCaseDetails deleteVersion(TCaseDetails version) throws CpxIllegalArgumentException;

    /**
     * Get SuccessorDetails as String, null if no Successors are found
     *
     * @param version version to Check Successors
     * @return Successordetails or null if no Successors are found
     */
    String getSuccessorDetails(TCaseDetails version);

    /**
     * get List of Rules from Case-Evaluation from the Grouper
     *
     * @param idGroupingResults id of Grouping Results;
     * @return List of rules from the Grouper, for the current Role of the
     * current User
     */
    List<TCheckResult> findDetectedRules(long idGroupingResults);

    /**
     * find the Grouping result for a specific version uses the currently set
     * MainDiagnosis identified by the hbx flag for computation
     *
     * @param id version id in the data (T_CASE_DETAILS)
     * @param pModel selected grouper model by the user
     * @return Tgrouping result object or null if nothing was found
     */
    TGroupingResults findGroupingResultForVersion(long id, GDRGModel pModel);

    TGroupingResults findGroupingResult(long id);
//    List<IcdOverviewDTO> findAllIcdCodesForCase(long id);

    /**
     * get all icds for a version, erased dublicates with occurance of one
     *
     * @param pVersionIds version to fetch icds
     * @param pModel selected grouper model
     * @return list of icds
     */
    List<IcdOverviewDTO> findAllIcdCodesForVersions(List<Long> pVersionIds, GDRGModel pModel);

    /**
     * get all ops for a version, erased dublicates with occurance of one
     *
     * @param pVersionIds version to fetch ops
     * @return list of ops
     */
    List<OpsOverviewDTO> findAllOpsCodesForVersions(List<Long> pVersionIds);

    /**
     * remove the icd from the database
     *
     * @param pIcd icd to remove
     */
    void removeIcd(TCaseIcd pIcd);

    /**
     * remove the ops from the database
     *
     * @param pOps ops to remove
     */
    void removeOps(TCaseOps pOps);

    Long testSequence();

    TGroupingResults findGroupingResultsLazy(Long pDetailsId, GDRGModel pGrouperModel);

    /**
     * @param pDetailsId tcasedetails id for the casefees
     * @return list of all casefees for that casedetails
     */
    List<TCaseFee> findCaseFeesForDetails(long pDetailsId);

    TCaseDetails findCaseDetails(long pDetailsId);

    List<TCaseOps> findAllOpsCodes(long pId);//, GDRGModel pModel);

    List<TCaseIcd> findAllIcdCodes(long pId, GDRGModel pModel);

    /**
     * @param pCaseId case id to fetch comments for
     * @return all comments for the case
     */
    List<TCaseComment> findAllComments(long pCaseId);

    /**
     * @param pCaseId case id to fetch comments for
     * @param pType comment type
     * @return list of all comments with that type
     */
    List<TCaseComment> findAllComments(long pCaseId, CommentTypeEn pType);

    /**
     * @param pCaseId case database id
     * @param pType type of the comment
     * @return current active commet for case with comment type
     */
    TCaseComment findActiveComment(long pCaseId, CommentTypeEn pType);

    /**
     * @param pComment comment to store
     * @return store given comment
     */
    TCaseComment storeComment(TCaseComment pComment);

    /**
     * @param pCaseId case id
     * @param pComment comment to store
     * @return store comment for case
     */
    TCaseComment storeComment(Long pCaseId, TCaseComment pComment);

    /**
     * @param pComment comment to remove
     * @return if remove was succcessful
     */
    boolean removeComment(TCaseComment pComment);

    /**
     * @param pCommentId comment to remove by id
     * @return if remove was succcessful
     */
    boolean removeComment(Long pCommentId);

    /**
     * @param pComment comment to update
     * @return updated comment
     */
    TCaseComment updateComment(TCaseComment pComment);

    Boolean removeIcdById(Long pIcdId);

    /**
     * load mibi data for specific case
     *
     * @param pCaseId hospital case id
     * @return list of mibi data
     */
    List<TMibi> findAllMibis(long pCaseId);

    public boolean caseExists(long pId);
    
    public List<TCaseDetails> findCaseDetails(long pCaseId,boolean pLocal,boolean pIncludeStorno);
    public List<TCaseDetails> findCaseDetails(long pCaseId,boolean pLocal);
    public List<TCaseDetails> findLocalCaseDetails(long pCaseId);
    public List<TCaseDetails> findExternCaseDetails(long pCaseId);

    ReadOnlyRequestDTO getLatestRequestForRiskType(long pHospitalId, VersionRiskTypeEn pRiskType);

    public Long getRiskTypePredecessorVersionId(Long pHospitalId, VersionRiskTypeEn pRiskType);

    public TCaseDetails getKisBaseVersion(long id);

    public boolean hasGroupingResult(GDRGModel pModel, Long[] pIds);

    public boolean allIcdsGrouped(Long id, GDRGModel pModel);

    public List<TCaseBill> findCaseBills4Details(long id);
}
