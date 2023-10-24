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

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TP301KainInka;
import de.lb.cpx.model.TP301KainInkaPvt;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.commonDB.model.CCase;
import de.lb.cpx.server.commonDB.model.CMdkAuditquota;
import de.lb.cpx.shared.dto.MdkAuditComplaintsDTO;
import de.lb.cpx.wm.model.TWmAction;
import de.lb.cpx.wm.model.TWmDocument;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.TWmMdkAuditReasons;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmProcessCase;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.TWmProcessHospitalFinalisation;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.TWmRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author wilde
 */
@Remote
public interface ProcessServiceBeanRemote {

    /**
     * delete process including documents
     *
     * @param pProcessId process id to delete
     * @throws CpxIllegalArgumentException error
     */
    void deleteProcess(final long pProcessId) throws CpxIllegalArgumentException;

    /**
     *
     * @param pProcessId process id to cancel
     * @throws CpxIllegalArgumentException error
     */
    void cancelProcess(final long pProcessId) throws CpxIllegalArgumentException;

    /**
     *
     * @param pProcessId process id to cancel
     * @throws CpxIllegalArgumentException error
     */
    void unCancelProcess(final long pProcessId) throws CpxIllegalArgumentException;

    /**
     * find document Entity for its database id warning: conent is not loaded!
     * use getDocumentContent() to access the content
     *
     * @param documentId database id of the document
     * @return document database entity
     */
//    TWmDocument findDocument(long documentId);
    /**
     * get document content, methode needed to only load data when requested
     *
     * @param documentId id of the document
     * @return byte array of the content
     */
//    byte[] getDocumentContent(long documentId);
//
//    void removeDocument(TWmDocument item);
//
//    void removeDocumentFromFS(TWmDocument item, String serverRootFolder);
    /**
     * store event entity, calls persist, maybe crash if event already exists
     *
     * @param event event to store
     * @return stored document
     */
    TWmEvent storeEvent(TWmEvent event);

    /**
     * update given event in the database
     *
     * @param event event to be updated
     * @return updated event entity
     */
    TWmEvent updateEvent(TWmEvent event);

    /**
     * checks if file already exits in the Database
     *
     * @param fileName name of the file
     * @param processId unique db id of the process
     * @return if already stored or not
     */
    boolean existsInProcess(String fileName, long processId);

    /**
     * remove given process Case from the Database
     *
     * @param processCase to remove
     */
    void removeProcessCase(TWmProcessCase processCase);

    /**
     * store processCase entity in the database
     *
     * @param processCase processCase to store
     * @param pProcessId process id to store process case
     * @return newly created processcase
     */
    TWmProcessCase storeProcessCase(TWmProcessCase processCase, long pProcessId);

    /**
     * store new request in the database
     *
     * @param request request to store
     * @return newly created request
     */
    TWmRequest storeRequest(TWmRequest request);

    /**
     * remove request from the database
     *
     * @param request request to remove
     */
    void removeRequest(TWmRequest request);

    /**
     * store new kainInka in the database
     *
     * @param kainInka kainInka to store
     * @return newly created kainInka
     */
    TP301KainInka storeKainInka(TP301KainInka kainInka);

    /**
     * update existing KainInka object
     *
     * @param kainInka kain/inka
     * @return kainInka object
     */
    TP301KainInka updateKainInka(TP301KainInka kainInka);

    /**
     * remove kainInka from the database
     *
     * @param kainInka kainInka to remove
     */
    void removeKainInka(TP301KainInka kainInka);

    /**
     * store new reminder entity in the database
     *
     * @param reminder reminder to store
     * @return newly stored reminder
     */
    TWmReminder storeReminder(TWmReminder reminder);

    /**
     * remove reminder entity from the database
     *
     * @param reminder reminder to remove
     */
    void removeReminder(TWmReminder reminder);

    /**
     * store new Action entity in the database
     *
     * @param action entity to store
     * @return newly created entity
     */
    TWmAction storeAction(TWmAction action);

    /**
     * remove action entity from the database
     *
     * @param action action to remove
     */
    void removeAction(TWmAction action);

    /**
     * update process entity in database
     *
     * @param process process to update
     * @return newly updated process
     */
    TWmProcess updateProcess(TWmProcess process);

    /**
     * update process modification user + modification date
     *
     * @param pProcessId process id to update
     */
    void updateProcessModification(long pProcessId);

    /**
     * update process hospital entity in database
     *
     * @param process process to update
     * @return newly updated process
     */
    TWmProcessHospital updateProcessHospital(TWmProcessHospital process);

    /**
     * updates an Action Entity
     *
     * @param action action to update
     * @return updated enity
     */
    TWmAction updateAction(TWmAction action);

    /**
     * update TWmRequestEntity in the Database
     *
     * @param request request to update
     * @return updated db entity
     */
    TWmRequest updateRequest(TWmRequest request);

    TWmReminder updateReminder(TWmReminder reminder);

    /**
     *
     * @param reminderIds list of reminderIds
     */
    void closeRemindersByIds(List<Long> reminderIds);

    /**
     *
     * @param processIds list of processIds
     * @param reminder Reminder Entity
     */
    void createReminderForAllProcesses(List<Long> processIds, TWmReminder reminder);

    /**
     * get the maindiagnosis for the version
     *
     * @param pCaseDetailsId version id to get the maindiagnosis
     * @return Case Icd with he main diagnosis
     */
    TCaseIcd getMainDiagnosisForVersion(long pCaseDetailsId);

    /**
     * get the count of all secondary diagnosis over all Departments
     *
     * @param pVersionId version id
     * @return count of all secondary diagnosis for the case version
     */
    int getSecondaryDiagnosisCount(long pVersionId);

    /**
     * get the cound of all procedures over all Departments
     *
     * @param pVersionId version id
     * @return all procedures for the case version
     */
    int getProcessCount(long pVersionId);

    /**
     * get the drg result for the case version
     *
     * @param pVersionId version id
     * @param pGrouper current grouper
     * @return drg result
     */
    TGroupingResults getGroupingResult(long pVersionId, GDRGModel pGrouper);

//    /**
//     * find the supplementary fee value for the given version
//     *
//     * @param pDEtailsId version id
//     * @param pCalcOnDb indicates if calculation is done on the database level
//     * or not
//     * @return the supplementary fee value
//     */
//    double findSupplementaryFee(long pGroupingResultId, boolean pCalcOnDb);
    double findSupplementaryFee(GDRGModel pGrouper, long pDetailsId, SupplFeeTypeEn pType);

    /**
     * update an document in the databse and return the newly updated value
     *
     * @param pDocument document to update
     * @return updated entity
     */
    TWmDocument updateDocument(TWmDocument pDocument);

//    List<CMdkAuditreason> getAllCMdkAuditReasons();
//    List<String> getAllCMdkAuditReasonsNames();
//    TWmProcess getCurrentProcessByMainCase(TCase mainCase);
    TWmProcess getCurrentProcessByMainCase(long hCaseId);

    TWmProcessCase getCurrentProcessCaseByMainCase(long hCaseId);

    List<TWmProcess> getProcessesOfCase(long hCaseId);

    List<TWmProcess> getProcessesOfCase(long hCaseId, boolean pIncludeCanceled);

//    List<CDeadline> getAllDeadlines();
//    
//    List<CDeadline> getDeadlines(final Date pDate);
    TCase getMainCase(long caseId);

//    /**
//     * get the list of all availble audit reasons, which are valid for the date
//     *
//     * @param creationDate specifies validity
//     * @return list of audit reasons if date is null all reasons are returned
//     */
//    List<CMdkAuditreason> getAllAvailableCMdkAuditReasons(Date creationDate);
//
//    /**
//     * get Audit reason based on given auditReason ID
//     *
//     * @param auditReasonId provided auditReason ID
//     * @return Audit reason based on given auditReason ID
//     */
//    CMdkAuditreason getMdkAuditreasonById(Long auditReasonId);
//
//    /**
//     *
//     * @param pAuditreasonNumber mdk audit reason AR id
//     * @return searched mdk audit reason
//     */
//    CMdkAuditreason getMdkAuditreasonByNumber(Long pAuditreasonNumber);
//
//    /**
//     * get the list of all availble process results, which are valid for the
//     * date
//     *
//     * @param creationDate specifies validity
//     * @return list of process results if date is null all reasons are returned
//     */
//    List<CWmListProcessResult> getAllAvailableProcessResults(Date creationDate);
//
//    /**
//     * get process result based on given process Result ID
//     *
//     * @param processResultId provided processResultId
//     * @return get process result
//     */
//    CWmListProcessResult getProcessResultById(Long processResultId);
//
//    /**
//     * get process result based on given process Result ID
//     *
//     * @param processResultInternalId provided processResultId
//     * @return get process result
//     */
//    CWmListProcessResult getProcessResultByInternalId(Long processResultInternalId);
//
//    /**
//     * get the list of all availble process topics, which are valid for the date
//     *
//     * @param creationDate specifies validity
//     * @return list of process topics if date is null all reasons are returned
//     */
//    List<CWmListProcessTopic> getAllAvailableProcessTopics(Date creationDate);
//
//    /**
//     * gets the internal of of the first available process topic
//     *
//     * @return internal id of first available process topic
//     */
//    Long getFirstAvailableProcessTopicInternalId();
//
//    /**
//     * gets the internal of of the first available process topic
//     *
//     * @param pDate date to filter available process topics
//     * @return internal id of first available process topic
//     */
//    Long getFirstAvailableProcessTopicInternalId(Date pDate);
    /**
     * @param pProcessResult save or update process result
     * @return updated process result
     */
    TWmProcessHospitalFinalisation saveOrUpdateProcessResult(TWmProcessHospitalFinalisation pProcessResult);

//    List<CWmListReminderSubject> getAllReminderSubjects(Date pDate);
//
//    CWmListReminderSubject getReminderSubjectById(Long pCWmListReminderSubjectId);
//
//    CWmListReminderSubject getReminderSubjectByInternalId(Long pCWmListReminderSubjectInternalId);
//
//    List<CWmListDocumentType> getAllDocumentTypeObjects(Date pDate);
//
//    CWmListDocumentType getDocumentTypeById(long pCWmListDocumentTypeId);
//
//    List<CWmListActionSubject> getAllActionSubjectObjects(Date pDate);
//    /**
//     * Get action subject item by id
//     *
//     * @param pCWmListActionSubjectId action subject id
//     * @return searched action subject
//     */
//    CWmListActionSubject getActionSubjectById(Long pCWmListActionSubjectId);
//    /**
//     * Get action subject item by InternalId
//     *
//     * @param pCWmListActionSubjectInternalId action subject id
//     * @return searched action subject
//     */
//    CWmListActionSubject getActionSubjectByInternalId(Long pCWmListActionSubjectInternalId);
//    /**
//     * get the list of all availble mdk states, which are valid for the date
//     *
//     * @param pDate specifies validity
//     * @return list of mdk states if date is null all reasons are returned
//     */
//    List<CWmListMdkState> getAllMdkStatesObjects(Date pDate);
//    /**
//     * Get mdk state item by id
//     *
//     * @param pCWmListMdkStateId mdk state id
//     * @return searched mdk state
//     */
//    CWmListMdkState getMdkStateById(Long pCWmListMdkStateId);
//    Map<String, String> getDepartmentsMap();
    /**
     * store document entity, calls persist, maybe crash if document already
     * exists
     *
     * @param pWmDocument document to store
     * @return stored document
     */
    TWmDocument storeDocument(final TWmDocument pWmDocument);

    /**
     * To store document into the server's file system.
     *
     * @param pWmDocument document uploaded from the client
     * @param pContent content
     * @param pFileExtension file name extension
     * @throws IOException cannot store document
     * @return document
     */
    //public String sendDocument(TWmDocument document, TWmProcess currentProcess, String serverRootFolder, String fileExtension);
    TWmDocument storeDocument(final TWmDocument pWmDocument, final byte[] pContent, final String pFileExtension) throws IOException;

    /**
     * delete document (also removes content from file system and database)
     *
     * @param pWmDocumentId id of the document to remove
     * @throws IOException cannot store document
     */
    void removeDocument(final long pWmDocumentId) throws IOException;

    /**
     * delete document (also removes content from file system and database)
     *
     * @param pWmDocument document to remove
     * @throws IOException cannot remove document
     */
    void removeDocument(final TWmDocument pWmDocument) throws IOException;

    /**
     * get document content
     *
     * @param pWmDocumentId id of the document
     * @throws IOException cannot retrieve document content
     * @return byte array of the content
     */
    byte[] getDocumentContent(final long pWmDocumentId) throws IOException;

//    /**
//     * To store document into the server's file system.
//     *
//     * @param docContent document content from the client
//     *
//     */
    //public void sendDocumentContent(byte[] docContent);
//    byte[] getDocumentContentFromFS(String serverRootFolder, String filePath);
//    /**
//     * Returns a map of Reminder Subjects
//     *
//     * @param cat category type
//     * @return all reminder subjects
//     */
//    Map<Long, String> getAllDraftTypes(CategoryEn cat);
//
//    List<CWmListDraftType> getAllAvailableDraftTypes(CategoryEn cat);
//
//    List<CWmListDraftType> getAllAvailableDraftTypes();
    TWmEvent storeEvent(TWmEvent pEvent, long pProcessId);

    /**
     * @param pProcessId database id of process
     * @return all events for a process
     */
    List<TWmEvent> getEvents(long pProcessId);

    /**
     * @param pProcessId database id of process
     * @param pHistoryDeleted show deleted entries?
     * @param pLimit limits number of events
     * @return all events for a process
     */
    List<TWmEvent> getEvents(long pProcessId, final boolean pHistoryDeleted, final Integer pLimit);

    /**
     * @param pProcessId database id of process
     * @return all reminders for a process
     */
    List<TWmReminder> getReminder(long pProcessId);

    /**
     * @param pProcessId database id of process
     * @param pFinishedFl only (un-)finished reminders
     * @return all reminders for a process
     */
    List<TWmReminder> getReminder(long pProcessId, final Boolean pFinishedFl);

    /**
     * @param pProcessId database id of process
     * @return all ProcessCases for a process
     */
    List<TWmProcessCase> getProcessCases(long pProcessId);

    TWmProcess findProcessById(long pProcessId);

    /**
     * @param pProcessId database id of process
     * @return all events for a process
     */
    List<TWmDocument> getDocuments(long pProcessId);

    /**
     * @param pProcessId database id of process
     * @return all requests for a process
     */
    List<TWmRequest> getRequests(long pProcessId);

    long getLatestRequestId(long pProcessId);

    TWmRequest getLatestRequest(long pProcessId);

    /**
     * @param pProcessId database id of process
     * @return all actions for a process
     */
    List<TWmAction> getActions(long pProcessId);

    /**
     * Get a set of INKA messages for a specified hospital case id
     *
     * @param tCaseId case id
     * @return set of INKA messages
     */
    List<TP301KainInka> getInkaMsgs(long tCaseId);

    /**
     * Get KainInka object based on ID
     *
     * @param kainInkaId given ID
     * @return kainInka object
     */
    TP301KainInka getTP301KainInkaById(long kainInkaId);

    /**
     * @param pProcessId process ID
     * @return get the main case for the process
     */
    TCase getMainCaseForProcess(long pProcessId);

    String getUserById(long assignedUserId);

    Collection<String> getMatchingUsers(String userText, Date pDate);

    /**
     * @param process save or update given process
     */
    void saveOrUpdateProcess(TWmProcess process);

//    /**
//     * get process topic for ident
//     *
//     * @param pTopic topic ident
//     * @return topic from common db
//     */
//    CWmListProcessTopic getProcessTopic(Long pTopic);
//
//    /**
//     * get process topic for ident
//     *
//     * @param pResult topic ident
//     * @return topic from common db
//     */
//    CWmListProcessResult getProcessResult(Long pResult);
    /**
     * @param pvvId pvv id of KAIN INKA
     * @return all pvts of that pvv
     */
    List<TP301KainInkaPvt> getAllPvtsForPvv(long pvvId);

//    Map<Long, CMdkAuditreason> getValidUndeletedAuditReasons(Date date);
    /**
     * Get complete KainInka object with pvv and pvts
     *
     * @param pKainInkaId id of KainInka object
     * @return KainInka object
     */
    TP301KainInka getKainInka(long pKainInkaId);

    void updateDocumentName(TWmDocument doc, String docNameToUpdate);

    TWmProcess findSingleProcessForWorkflowNumber(Long workflowNumber);

    TWmProcess findSingleProcessForId(Long workflowId);

    Long findSingleProcessIdForWorkflowNumber(Long workflowNumber);

    List<TWmProcess> findWorkflowsByPatient(TPatient patient);

//    String getInsuCompName(String cellData, String de);
    TWmProcess storeProcessForDocumentImport(TWmProcess pProcess) throws IOException;

    TP301KainInka sendInkaMessage(TP301KainInka pKainInka);

    TP301KainInka cancelInkaMessage(final TP301KainInka pKainInka);

    TWmProcess storeProcessForMdkReminders(TWmProcess process, TWmRequest request);

    long createNextSendingInkaNumberValue();

    void updateCase(String pDatabase, long[] pCaseIds);

    void updateCase(String pDatabase, long pCaseId);

    //    void updateBills(final Long pCaseId);
    void updateBills(final TCase currentCase);

    int copyTCaseToCommon(long[] pCaseIds);

    int copyTCaseToCommon(long[] pCaseIds, String pCategory);

    int copyTCaseToCommon(TCase[] pCaseIds, String pCategory);

    List<CCase> getAllCCases();

    Map<Date, List<CCase>> getCCaseDateDistribution();

    CCase loadCCaseFromCommon(long id);

    TCase loadTCaseFromCommon(long pCaseId);

    int getDocumentsSizeMax();

    boolean processExists(long pId);

    /**
     * @param pCaseId case database id to check kain messaged
     * @return indicator if there are kain messages that indicates possible 6
     * week deadline
     */
    boolean hasPossibleKain6WeekDeadline(long pCaseId);

    /**
     * parameters are optional (pass null to ignore)
     *
     * @param pHospitalIdent hospital identifier
     * @param pInsuranceIdent insurance identifier
     * @param pYear evaluation year
     * @param pQuartal evaluation quarter (1-4)
     * @return list of complaints data for latter quota calculation
     */

//    List<MdkAuditQuotaDTO> getAuditQuotas(final String pHospitalIdent, final String pInsuranceIdent, final Integer pYear, final Integer pQuartal, final BigDecimal pQuote);
    List<MdkAuditComplaintsDTO> getMdkAuditComplaints(final String pHospitalIdent, final String pInsuranceIdent, final Integer pBillYear, final Integer pBillQuartal, final Integer pAuditYear, final Integer pAuditQuartal);

    CMdkAuditquota getMdkAuditQuota(final String pHospitalIdent, final int pYear, final int pQuartal);

    List<TWmMdkAuditReasons> findAuditReasons4case(Long pCaseId);
    
    TWmRequest getLatestRequest4Case(long pCaseId);
    /**
     * @param pCaseDetailsId case details database id to check database for
     * @return indicator if caseDetails is basic version for a process
     */
    public boolean isDetailBasicForProcess(long pCaseDetailsId);
    /**
     * @param pProcessId process database id
     * @return TCaseDetailsVersion set as base kis version in process
     */
    public TCaseDetails findCurrentKisDetailsVersionForProcess(long pProcessId);
    /**
     * returns count of open reminders for process with id
     * @param pProcessId process id
     * @return count
     */

    public int getOpenReminderCount(long pProcessId);

    public boolean isDocumentToArchivateafterImport();

    public String getRequesrSateById(long requestStateId);

    public boolean sendMDStateToSAP(final TWmRequest pRequest) throws IllegalArgumentException;

    public boolean isSendMDStateToSAPActive();
}
