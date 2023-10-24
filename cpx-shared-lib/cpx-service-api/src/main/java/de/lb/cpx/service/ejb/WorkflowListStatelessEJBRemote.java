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
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.shared.dto.DocumentSearchCaseItemDto;
import de.lb.cpx.shared.dto.DocumentSearchPatientItemDto;
import de.lb.cpx.shared.dto.DocumentSearchProcessItemDto;
import de.lb.cpx.shared.dto.WorkflowListItemDTO;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.wm.model.TWmProcess;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import javax.ejb.Remote;

/**
 * Remote Interface of WorkflowList Services
 *
 * @author Wilde
 */
@Remote
public interface WorkflowListStatelessEJBRemote {

    /**
     * get Count of all available Entries in View VwInputCaseInfo
     *
     * @return ocunt of all available Entries in the View
     */
    Integer getMaxCount();

    /**
     * get Count of canceled processes
     *
     * @return ocunt of all available Entries in the View
     */
    Integer getCanceledCount();

    /**
     * find List of WorkflowListItemDTO Objects from View VwInputCaseInfo, List
     * is filtered, by given arguments Filtering of the List of Entries in View
     * VwInputCaseInfo is based of Values in FilterOptions DTO only contains
     * Data if Column is shown in Client
     *
     * @param isLocal Local oder external case details
     * @param grouperModel Grouper Model
     * @param pPage Page
     * @param pFetchSize Amount of results per Page
     * @param options Map which defines Filter behaviour
     * @return List of WorkingListItemDTO Objects according to given FilterMap
     */
    Future<SearchResult<WorkflowListItemDTO>> find(final boolean isLocal, final GDRGModel grouperModel, final int pPage, final int pFetchSize, java.util.Map<ColumnOption, List<FilterOption>> options);

    /**
     * find all TWmProcess database ids
     *
     * @return list of available twmprocess db ids
     */
    List<Long> findAllCaseIds();

    /**
     * find a case for the specific id
     *
     * @param pCaseId case id of the case
     * @return tcase entity or null if no case with the id exists
     */
    TCase findCase(final Long pCaseId);

    /**
     * find a patient for the specifc id
     *
     * @param pPatientId patient id
     * @return tpatient entitiy or null if no patient with the id exists
     */
    TPatient findPatient(final Long pPatientId);

    /**
     * stores the process in the database may throw already exists exception if
     * id is set in the entity and an entity with the id already exists in the
     * db
     *
     * @param pProcess process to store
     * @return newly stored process with the id
     */
    TWmProcess storeProcess(final TWmProcess pProcess);

    /**
     * find a process for the specific id
     *
     * @param processId process id
     * @param pEager eager mode, loads case,documents,requests,reminder
     * @param pProcessSubClazz process subclass
     * @return twmprocess entity or null if no process with the id exists
     */
    TWmProcess findProcess(long processId, boolean pEager, Class<? extends TWmProcess> pProcessSubClazz);

    /**
     * find a list of all process entities stored in the database no arrays of
     * the entity are initializied may result in lazy loading exceptions
     *
     * @return list of twmprocess
     */
    List<TWmProcess> findAllProcess();

    /**
     * gets the items for the workflow list test implementation TPatient and
     * TCase is set, access lists may result in lazyloading exception
     *
     * @return list of all process entities with TPatient and TCase Data
     */
    List<TWmProcess> findWorkflowItems();

    void createTestProcess();

    CdbUsers getCdbUser(Long cellData);

    /**
     * find working list searchResult without the async stuff
     *
     * @param pIsLocal consider local(cpx version) versions if true, false than
     * extern versions are used(his version)
     * @param pIsShowAllReminders ShowAllReminders in WorkflowList
     * @param pGrouperModel gdrg model, for grouping results
     * @param pPage max count of pages that are shown
     * @param pFetchSize size of items to fetch
     * @param pOptions filter options
     * @return search result object
     */
    SearchResult<WorkflowListItemDTO> findWorkflowList(final boolean pIsLocal, final boolean pIsShowAllReminders, final GDRGModel pGrouperModel, final int pPage, final int pFetchSize, final java.util.Map<ColumnOption, List<FilterOption>> pOptions);

    List<DocumentSearchCaseItemDto> findCasesForDocumentImport(final String pCaseNumber, final int pMaxEntries /*, final String pWorkflowNumber, final String pPatientNumber */);

    List<DocumentSearchCaseItemDto> findCasesForDocumentImport(final String pHospitalIdent, final String pCaseNumber, final int pMaxEntries /*, final String pWorkflowNumber, final String pPatientNumber */);

    List<DocumentSearchCaseItemDto> findCasesForDocumentImport(final long pCaseId, final int pMaxEntries /*, final String pWorkflowNumber, final String pPatientNumber */);

    List<DocumentSearchProcessItemDto> findProcessesForDocumentImport(final String pWorkflowNumber, final int pMaxEntries /*, final String pWorkflowNumber, final String pPatientNumber */);

    List<DocumentSearchPatientItemDto> findPatientsForDocumentImportByNumber(final String pPatientNumber, final int pMaxEntries /*, final String pWorkflowNumber, final String pPatientNumber */);

    List<DocumentSearchPatientItemDto> findPatientsForDocumentImportByName(final String pPatientName, final int pMaxEntries /*, final String pWorkflowNumber, final String pPatientNumber */);

    List<DocumentSearchPatientItemDto> findPatientsForDocumentImportByDateOfBirth(final Date pDateOfBirth, final int pMaxEntries /*, final String pWorkflowNumber, final String pPatientNumber */);

    List<DocumentSearchPatientItemDto> findPatientsForDocumentImportByInsuranceNumber(final String pPatientInsuracneNumber, final int pMaxEntries);

}
