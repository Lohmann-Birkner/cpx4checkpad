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
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.server.commonDB.dao.CdbUsersDao;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.server.dao.TPatientDao;
import de.lb.cpx.server.wm.dao.TWmProcessDao;
import de.lb.cpx.service.jms.producer.StatusBroadcastProducer;
import de.lb.cpx.shared.dto.DocumentSearchCaseItemDto;
import de.lb.cpx.shared.dto.DocumentSearchPatientItemDto;
import de.lb.cpx.shared.dto.DocumentSearchProcessItemDto;
import de.lb.cpx.shared.dto.WorkflowListItemDTO;
import de.lb.cpx.shared.dto.broadcast.BroadcastOriginEn;
import de.lb.cpx.shared.filter.ColumnOption;
import de.lb.cpx.shared.filter.FilterOption;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import de.lb.cpx.wm.model.enums.WmWorkflowTypeEn;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author Wilde
 */
@Stateless
@SecurityDomain("cpx")
public class WorkflowListStatelessEJB implements WorkflowListStatelessEJBRemote {

    private static final Logger LOG = Logger.getLogger(WorkflowListStatelessEJB.class.getName());

    @Inject
    private WorkflowListSearchService searchService;

    @Inject
    private TCaseDao caseDao;
//    @EJB
//    private TCaseDepartmentDao depDao;

    @Inject
    private TPatientDao patientDao;

    @Inject
    private TWmProcessDao processDao;

    @Inject
    private CdbUsersDao cdbUsersDao;

    @Inject
    private StatusBroadcastProducer<Long> broadcast;

    @Override
    public Integer getMaxCount() {
        return processDao.getCount();
        //ClientManager.updateProcessCount(count);
    }

    @Override
    public Integer getCanceledCount() {
        //return searchService.getMaxCount(isLocal);
        int count = processDao.getCanceledCount();
        return count;
    }

    @Override
    @Asynchronous
    public Future<SearchResult<WorkflowListItemDTO>> find(final boolean pIsLocal, final GDRGModel pGrouperModel, final int pPage, final int pFetchSize, java.util.Map<ColumnOption, List<FilterOption>> pOptions) {
        Set<ColumnOption> colOptions = pOptions.keySet();
        LOG.log(Level.INFO, "try to get List for ColumnSize of {0}", colOptions.size());
        SearchResult<WorkflowListItemDTO> result;
        try {
            result = searchService.getAllWithCriteriaForFilter(pIsLocal, pGrouperModel, pPage, pFetchSize, pOptions);
        } catch (IOException | InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
            result = new SearchResult<>();
        }
        return new AsyncResult<>(result);
    }

    @Override
    public List<Long> findAllCaseIds() {
        return caseDao.getAllCasesIds();
    }

    @Override
    public TCase findCase(final Long pCaseId) {
        long caseId = (pCaseId == null) ? 0L : pCaseId;
        if (caseId == 0L) {
            return null;
        }
        TCase cs = caseDao.findById(caseId);
        cs.getPatient();
        cs.getCaseDetails().iterator();
        return cs;
    }

    @Override
    public TPatient findPatient(final Long pPatientId) {
        long patientId = (pPatientId == null) ? 0L : pPatientId;
        if (patientId == 0L) {
            return null;
        }
        return patientDao.findById(patientId);
    }

    @Override
    public TWmProcess storeProcess(final TWmProcess pProcess) {
        boolean isNew = pProcess.id == 0L;
        processDao.persist(pProcess);
        if (isNew) {
            broadcast.send(BroadcastOriginEn.ADD_PROCESS, "Der Vorgang " + pProcess.getWorkflowNumber() + " wurde hinzugefügt", null, pProcess.id);
        }
        return pProcess;
    }

    @Override
    public TWmProcess findProcess(long processId, boolean pEager, Class<? extends TWmProcess> pProcessSubClazz) {
        final long startTime = System.currentTimeMillis();
        LOG.log(Level.INFO, "load process with id {0} ({1})", new Object[]{processId, pEager ? "eager" : "lazy"});
        TWmProcess proc = processDao.findEagerById(processId, pEager, pProcessSubClazz);
        LOG.log(Level.FINER, "loaded process with id {0} ({1}) in {2} ms: {3}", new Object[]{processId, pEager ? "eager" : "lazy", System.currentTimeMillis() - startTime, String.valueOf(proc)});
        return proc;
    }

    @Override
    public List<TWmProcess> findAllProcess() {
        return processDao.findAll();
    }

    @Override
    public List<TWmProcess> findWorkflowItems() {
        return processDao.findWorkflowItems();
    }

    @Override
    public void createTestProcess() {
        TWmProcess process = new TWmProcessHospital();
        process.setComment("test");
        process.setWorkflowState(WmWorkflowStateEn.offen);
        process.setWorkflowType(WmWorkflowTypeEn.statKH);
        process.setPatient(patientDao.findById(1));
        processDao.persist(process);
    }

    @Override
    public CdbUsers getCdbUser(Long cellData) {
        return cdbUsersDao.findUserById(cellData);
    }

    @Override
    public SearchResult<WorkflowListItemDTO> findWorkflowList(boolean pIsLocal, boolean pIsShowAllReminders, GDRGModel pGrouperModel, int pPage, int pFetchSize, Map<ColumnOption, List<FilterOption>> pOptions) {
        Set<ColumnOption> colOptions = pOptions.keySet();
        LOG.log(Level.INFO, "try to get List for ColumnSize of {0}", colOptions.size());
        try {
            return searchService.getAllWithCriteriaForFilter(pIsLocal, pIsShowAllReminders, pGrouperModel, pPage, pFetchSize, pOptions);
        } catch (IOException | InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
            return new SearchResult<>();
        }
    }

    @Override
    public List<DocumentSearchCaseItemDto> findCasesForDocumentImport(final String pHospitalIdent, final String pCaseNumber, final int pMaxEntries /*, final String pWorkflowNumber, final String pPatientNumber */) {
        return processDao.findCasesForDocumentImport(0L, pHospitalIdent, pCaseNumber, pMaxEntries /*, pWorkflowNumber, pPatientNumber */);
    }

    @Override
    public List<DocumentSearchCaseItemDto> findCasesForDocumentImport(final long pCaseId, final int pMaxEntries /*, final String pWorkflowNumber, final String pPatientNumber */) {
        return processDao.findCasesForDocumentImport(pCaseId, null, null, pMaxEntries /*, pWorkflowNumber, pPatientNumber */);
    }

    @Override
    public List<DocumentSearchCaseItemDto> findCasesForDocumentImport(final String pCaseNumber, final int pMaxEntries /*, final String pWorkflowNumber, final String pPatientNumber */) {
        return processDao.findCasesForDocumentImport(pCaseNumber, pMaxEntries /*, pWorkflowNumber, pPatientNumber */);
    }

    @Override
    public List<DocumentSearchProcessItemDto> findProcessesForDocumentImport(final String pWorkflowNumber, final int pMaxEntries /*, final String pWorkflowNumber, final String pPatientNumber */) {
        return processDao.findProcessesForDocumentImport(pWorkflowNumber, pMaxEntries /*, pWorkflowNumber, pPatientNumber */);
    }

    @Override
    public List<DocumentSearchPatientItemDto> findPatientsForDocumentImportByNumber(final String pPatientNumber, final int pMaxEntries /*, final String pWorkflowNumber, final String pPatientNumber */) {
        return processDao.findPatientsForDocumentImport(pPatientNumber, null, null, pMaxEntries /*, pWorkflowNumber, pPatientNumber */);
    }

    @Override
    public List<DocumentSearchPatientItemDto> findPatientsForDocumentImportByName(final String pPatientName, final int pMaxEntries /*, final String pWorkflowNumber, final String pPatientNumber */) {
        return processDao.findPatientsForDocumentImport(null, pPatientName, null, pMaxEntries /*, pWorkflowNumber, pPatientNumber */);
    }

    @Override
    public List<DocumentSearchPatientItemDto> findPatientsForDocumentImportByInsuranceNumber(final String pPatientInsuracneNumber, final int pMaxEntries) {
        return processDao.findPatientsForDocumentImportByInsuranceNumber(pPatientInsuracneNumber, pMaxEntries);
    }

    @Override
    public List<DocumentSearchPatientItemDto> findPatientsForDocumentImportByDateOfBirth(Date pDateOfBirth, int pMaxEntries) {
        return processDao.findPatientsForDocumentImport(null, null, pDateOfBirth, pMaxEntries);
    }

}
