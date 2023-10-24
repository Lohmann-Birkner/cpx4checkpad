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
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.ActionEventSubject;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.CaseEventSubject;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.DocumentEventSubject;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.EventSubject;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.ReminderEventSubject;
import de.lb.cpx.client.app.wm.fx.process.listview.entries.subject.RequestEventSubject;
import de.lb.cpx.client.app.wm.util.auditquota.MdkAuditQuotaResult;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.model.catalog.AbstractCpxCatalog;
import de.lb.cpx.client.core.model.catalog.CpxBaserate;
import de.lb.cpx.client.core.model.catalog.CpxBaserateCatalog;
import de.lb.cpx.client.core.model.catalog.CpxDrgCatalog;
import de.lb.cpx.client.core.model.catalog.CpxHospital;
import de.lb.cpx.client.core.model.catalog.CpxHospitalCatalog;
import de.lb.cpx.client.core.model.catalog.CpxIcd;
import de.lb.cpx.client.core.model.catalog.CpxIcdCatalog;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompany;
import de.lb.cpx.client.core.model.catalog.CpxInsuranceCompanyCatalog;
import de.lb.cpx.client.core.model.catalog.CpxPeppCatalog;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.client.core.model.fx.tab.ShowTabAction;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TInsurance;
import de.lb.cpx.model.TP301KainInka;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.TPatientDetails;
import de.lb.cpx.model.enums.CategoryEn;
import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.patient.status.HealthStatusApp;
import de.lb.cpx.patient.status.organ.HumanPartGraphic;
import de.lb.cpx.server.commonDB.model.CDeadline;
import de.lb.cpx.server.commonDB.model.CMdkAuditquota;
import de.lb.cpx.server.commonDB.model.CMdkAuditreason;
import de.lb.cpx.server.commonDB.model.CWmListActionSubject;
import de.lb.cpx.server.commonDB.model.CWmListDraftType;
import de.lb.cpx.server.commonDB.model.CWmListMdkState;
import de.lb.cpx.server.commonDB.model.CWmListProcessResult;
import de.lb.cpx.server.commonDB.model.CWmListProcessTopic;
import de.lb.cpx.server.commonDB.model.CWmListReminderSubject;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.service.ejb.AuthServiceEJBRemote;
import de.lb.cpx.service.ejb.CaseServiceBeanRemote;
import de.lb.cpx.service.ejb.LockService;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.service.ejb.RiskServiceBeanRemote;
import de.lb.cpx.service.ejb.WorkflowListStatelessEJBRemote;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.dto.MdkAuditComplaintsDTO;
import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.shared.dto.acg.AcgPatientData;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmAction;
import de.lb.cpx.wm.model.TWmDocument;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.TWmFinalisationRisk;
import de.lb.cpx.wm.model.TWmMdkAuditReasons;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmProcessCase;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.TWmProcessHospitalFinalisation;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRequestMdk;
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.TWmRiskDetails;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import de.lb.cpx.wm.model.enums.WmRequestTypeEn;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.layout.StackPane;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.validation.constraints.NotNull;
import org.controlsfx.control.Notifications;

/**
 * ServiceFacade to get access to all Services from the Server for the Process
 * finishing
 *
 * @author wilde
 */
public final class ProcessServiceFacade {

    private static final Logger LOG = Logger.getLogger(ProcessServiceFacade.class.getName());
    private final ObjectProperty<ShowTabAction> loadAndShowProperty = new SimpleObjectProperty<>();
    private final CpxInsuranceCompanyCatalog insCatalog;
    private final CpxHospitalCatalog hosCatalog;
    private EjbProxy<AuthServiceEJBRemote> authServiceEjb;
    private final EjbProxy<LockService> lockServiceEjb;
    private ObservableList<TWmEvent> obsEventList;
    private ObservableList<TWmDocument> obsDocumentList;
    private ObservableList<TWmReminder> obsReminderList;
    private ObservableList<TWmProcessCase> obsProcessCaseList;
    private ObservableList<TWmRequest> obsRequestList;
    private ObservableList<TWmAction> obsActionList;
    private ObservableList<TP301KainInka> obsInkaMsgList;
    private final EjbProxy<WorkflowListStatelessEJBRemote> wmServiceBean;
    private TWmProcessHospital currentProcess;
    private final EjbProxy<CaseServiceBeanRemote> caseServiceBean;
    private final EjbProxy<ProcessServiceBeanRemote> processServiceBean;
    private final CpxIcdCatalog icdCatalog;
    private final CpxDrgCatalog drgCatalog;
    private final CpxPeppCatalog peppCatalog;
//    private ProcessMasterDetailPane pMasterDetailPane;
    private final EjbProxy<RiskServiceBeanRemote> riskServiceBean;
    
    /**
     * constructor for testing, load process with id 1 as current Process
     *
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException illegal argument
     */
    public ProcessServiceFacade() {
        this(null);
    }

    public ProcessServiceFacade(Long processId) {
        this(processId, false);
    }

    /**
     * construct new Facade with process with given id as currentProcess
     *
     * @param processId id of process to load
     * @param pEager if params should be eagerly loaded
     */
    public ProcessServiceFacade(Long processId, boolean pEager) {
//        this.testMode = processId == null;
        insCatalog = CpxInsuranceCompanyCatalog.instance();
        hosCatalog = CpxHospitalCatalog.instance();
        icdCatalog = CpxIcdCatalog.instance();
        drgCatalog = CpxDrgCatalog.instance();
        peppCatalog = CpxPeppCatalog.instance();
        authServiceEjb = Session.instance().getEjbConnector().connectAuthServiceBean();
        lockServiceEjb = Session.instance().getEjbConnector().connectLockServiceBean();
        wmServiceBean = Session.instance().getEjbConnector().connectWorkflowListBean();
        caseServiceBean = Session.instance().getEjbConnector().connectCaseServiceBean();
        processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
        riskServiceBean = Session.instance().getEjbConnector().connectRiskServiceBean();
        long start = System.currentTimeMillis();
        if (processId != null) {
            loadProcess(processId, pEager);
        }
        LOG.log(Level.INFO, "loading process with id {0} and number {1} in {2} ms", new Object[]{getCurrentProcessId(), getCurrentProcessNumber(), (System.currentTimeMillis() - start)});

    }
//    public void dispose(){
//        pMasterDetailPane = null;
//    }
    /**
     * load process for a specific id and set it as current
     *
     * @param processId process to load
     * @param pEager load in eager mode
     */
    public void loadProcess(long processId, boolean pEager) {
        currentProcess = getProcessById(processId, pEager);
    }
    
    public ProcessServiceBeanRemote getProcessServiceBean(){
        return processServiceBean.get();
    }
    
    /**
     * gets the full user Name(Firstname Lastname) from the CDBUsers Table in
     * the CommonDb
     *
     * @param userId unique user id
     * @return user name or null
     */
    public String getUserFullName(long userId) {
        return MenuCache.instance().getUserFullNameForId(userId);
        //return authServiceEjb.get().getUserName(userId);
    }

    /**
     * gets the user LoginName from the CDBUsers Table in the CommonDb
     *
     * @param userId unique user id
     * @return user login name or null
     */
    public String getUserLogin(long userId) {
        return MenuCache.instance().getUserNameForId(userId);
        //return authServiceEjb.get().getUserLoginName(userId);
    }

    public CdbUsers getUser(long userId) {
        return authServiceEjb.get().getCopy(userId);
    }

    /**
     * load and show specific Tab with corresponding db id of the object to show
     *
     * @param typeToShow type of the tab to show
     * @param id db id of obejct to show example usage to show a case tab
     * loadAndShow(TwoLineTab.TabType.CASE,12345), if listener is set to
     * loadAndShowProperty it can handle the new load request to fetch from the
     * database a case entity with id 12345 and handle the ui change
     */
    public void loadAndShow(TwoLineTab.TabType typeToShow, long id) {
        LOG.log(Level.INFO, "try to show {0} with id {1}", new Object[]{typeToShow.name(), id});
        loadAndShowProperty.setValue(new ShowTabAction(typeToShow, id));
    }

    /**
     * get Reference of the loadAndShowProperty to handle ui changes
     *
     * @return object property
     */
    public ObjectProperty<ShowTabAction> getLoadAndShowProperty() {
        return loadAndShowProperty;
    }

//    /*
//    Process
//     */
    /**
     * Gives you an visualization of this patient's health status
     *
     * @param pTargetPane stack pane where the graphics should be put on
     * @param pAcgPatientData some health information from the server about the
     * patient and organe
     * @return human body parts
     * @throws IOException Error loading images (svgs)
     */
    public Map<Integer, HumanPartGraphic> getPatientHealthVisualization(final StackPane pTargetPane, final AcgPatientData pAcgPatientData) throws IOException {
        HealthStatusApp healthApp = new HealthStatusApp();
        Map<Integer, HumanPartGraphic> humanParts = healthApp.getHealthVisualization(pTargetPane, pAcgPatientData);
        return humanParts;
    }

    /**
     * get Patient entity from current process
     *
     * @return TPatient entity
     */
    public TPatient getPatient() {
        return currentProcess.getPatient();
    }

    /**
     * stores WmProcess entity
     *
     * @param process entity to store
     * @return stored process
     */
    public TWmProcess storeProcess(TWmProcess process) {
        currentProcess = (TWmProcessHospital) wmServiceBean.get().storeProcess(process);
        return currentProcess;
    }

    /**
     * get Process entity with given id
     *
     * @param processId process id to load
     * @param pEager if process should eager loaded in
     * cases,requests,reminder,documents
     * @return process entity for the given id
     */
    public TWmProcessHospital getProcessById(long processId, boolean pEager) {
//        if (testMode) {
//            return (TWmProcessHospital) processTestData.get(processId);
//        }
        return (TWmProcessHospital) wmServiceBean.get().findProcess(processId, pEager, TWmProcessHospital.class);
    }

    /**
     * stores the event to the Process and to the obsList
     *
     * @param event event entity to store
     * @return stored event Entity
     */
    public TWmEvent storeEvent(TWmEvent event) {
        event = storeEventInDatabase(event);
        if (event == null) {
            LOG.log(Level.SEVERE, "Event can not be stored!");
            //should return null? throw exception?
            return event;
        }
        if (Session.instance().isShowHistoryDeleted() || !event.isOrphaned()) {
            getEventsAsObsList().add(event);    //if obsEventList is null, create new one and then add an event, otherwise get existing one and add an event
            currentProcess.getEvents().add(event);
        }
        return event;
    }

    public TWmEvent storeEventInDatabase(TWmEvent pEvent) {
        if (currentProcess == null) {
            LOG.log(Level.WARNING, "currentProcess is null!");
        } else {
            long start = System.currentTimeMillis();
            pEvent = processServiceBean.get().storeEvent(pEvent, currentProcess.getId());
//            loadProcess(currentProcess.getId(), true); // load new object state from server to keep up after whatever changed due to event update!
            LOG.log(Level.INFO, "store event in database in {0} ms", (System.currentTimeMillis() - start));
            return pEvent;
        }
        return null;
    }

    /**
     * get the set of events for a specific process
     *
     * @param process process from which the events are to fetch
     * @return list of event entities
     */
    public List<TWmEvent> getEvents(TWmProcess process) {
        return getEvents(process, null);
    }

    /**
     * get the set of events for a specific process
     *
     * @param process process from which the events are to fetch
     * @param pLimit limits number of events
     * @return list of event entities
     */
    public List<TWmEvent> getEvents(TWmProcess process, final Integer pLimit) {
        final long startTime = System.currentTimeMillis();
        final boolean historyDeleted = Session.instance().isShowHistoryDeleted();
        List<TWmEvent> events = processServiceBean.get().getEvents(process.getId(), historyDeleted, pLimit);
        LOG.log(Level.FINER, "getEvents where limitation of events is {0} (result size is {1}) took {2} ms", new Object[]{pLimit, events.size(), (System.currentTimeMillis() - startTime)});
        return events;
//        return process.getEvents();
    }

    /**
     * get the event set of the current process
     *
     * @return set of event entities
     */
    public List<TWmEvent> getEventsForCurrentProcess() {
        return getEventsForCurrentProcess(null);
    }

    /**
     * get the event set of the current process
     *
     * @param pLimit limits number of events
     * @return set of event entities
     */
    public List<TWmEvent> getEventsForCurrentProcess(final Integer pLimit) {
        return getEvents(currentProcess, pLimit);
    }

    /**
     * get the Observable Eventlist
     *
     * @return List of Events
     */
    public synchronized ObservableList<TWmEvent> getEventsAsObsList() {
        return getEventsAsObsList(null);
    }

    /**
     * get the Observable Eventlist
     *
     * @param pLimit limits number of events
     * @return List of Events
     */
    public synchronized ObservableList<TWmEvent> getEventsAsObsList(final Integer pLimit) {
        if (obsEventList == null) {
            //removed orphaned entries on server (if whished so)!
            final long startTime = System.currentTimeMillis();
            List<TWmEvent> events = getEventsForCurrentProcess(pLimit);
            currentProcess.setEvents(new HashSet<>(events));
            obsEventList = FXCollections.observableArrayList(events);
            LOG.log(Level.FINE, "----->create new event list where limitation of events is {0} in {1} ms<-----", new Object[]{pLimit, (System.currentTimeMillis() - startTime)});
        }
        return obsEventList;
    }

    public TWmEvent createAndStoreEvent(WmEventTypeEn type, AbstractEntity content, TWmProcessHospital process, String desc) {
        final long startTime = System.currentTimeMillis();
        TWmEvent event = createNewEvent(type, content, process, desc);
        event = storeEvent(event);
        LOG.log(Level.FINER, "createAndStoreEvent took {0} ms", (System.currentTimeMillis() - startTime));
        return event;
    }

    public TWmEvent createNewEvent(WmEventTypeEn pType, AbstractEntity pContent, TWmProcessHospital pProcess, String pDesc) {
        TWmEvent event = new TWmEvent();
        event.setEventType(pType);
        event.setContent(pContent);
        event.setSubject(pDesc);
//        event = storeEvent(event);
        if (pProcess != null) {
            // when we create any event, we can say the process is modified.
            pProcess.setProcessModificationUser(Session.instance().getCpxUserId());   //set with the user, who modifies the process.
            //this.updateProcess(process);    //update process with this new modification user
            //to update MODIFICATION_DATE, MODIFICATION_USER,LAST_PROCESS_MODIFICATION and PROCESS_MODIFICATION_USER
            this.updateProcessModification(pProcess.id);

            event.setProcess(pProcess);
        }
        return event;
    }

    public TWmEvent createAndStoreEvent(WmEventTypeEn type, AbstractEntity content, String desc) {
        return createAndStoreEvent(type, content, currentProcess, desc);
    }

    private void updateEvent(TWmEvent event) {
        processServiceBean.get().updateEvent(event);
    }

    /*
    Documents 
     */
    /**
     * store document
     *
     * @param pWmDocument document to be added
     * @param pContent document content
     * @param pFileName file name
     * @throws IOException file system error (if document has to be stored in
     * file system and not in database)
     * @return if adding was successful
     */
    public boolean storeDocument(final TWmDocument pWmDocument, final byte[] pContent, final String pFileName) throws IOException {
        LOG.log(Level.INFO, "Store document {0}...", pWmDocument);
        if(currentProcess != null){ // set current process but only if it is not null to prevent some other issues from occuring when facade was strangely disposed
            pWmDocument.setProcess(currentProcess);
        }
        TWmDocument document = pWmDocument;
//        if (!testMode) {
        document = processServiceBean.get().storeDocument(document, pContent, pFileName);
//        }
//        document.setProcess(currentProcess);
//        currentProcess.getDocuments().add(document);
        TWmEvent event = createAndStoreEvent(WmEventTypeEn.documentAdded, document, "");
        final boolean result;
        if (event != null && event.getContent() instanceof TWmDocument) {
            pWmDocument.setId(event.getContent().id);
            result = getObsDocuments().add(event.getDocument());
            currentProcess.getDocuments().add(document);
        } else {
            result = false;
        }
        LOG.log(Level.INFO, "Document {0} was successfully stored!", pWmDocument);
        return result;
    }
    /**
     * get observable Document List
     *
     * @return observable list of the documents
     */
    public synchronized ObservableList<TWmDocument> getObsDocuments() {
        if (obsDocumentList == null) {
            final long startTime = System.currentTimeMillis();
            List<TWmDocument> documents = processServiceBean.get().getDocuments(currentProcess.getId());
            currentProcess.setDocuments(new HashSet<>(documents));
            if (!documents.isEmpty()) {
//                Collections.sort(documents, (TWmDocument document1, TWmDocument document2) -> document2.getCreationDate().compareTo(document1.getCreationDate()));
                //rework sorting, sort by creation date, if no creation date is set (date is null) than order to last!
                Collections.sort(documents, getCreationDateComparator());//Comparator.comparing(TWmDocument::getCreationDate,Comparator.nullsLast(Comparator.naturalOrder())));
            }
            obsDocumentList = FXCollections.observableArrayList(documents);
            LOG.log(Level.FINER, "getObsDocuments with result size of {0} took {1} ms", new Object[]{(documents.size()), (System.currentTimeMillis() - startTime)});

        }

        return obsDocumentList;
    }

    private Comparator<AbstractEntity> getCreationDateComparator() {
        return Comparator.comparing(AbstractEntity::getCreationDate, Comparator.nullsLast(Comparator.naturalOrder()));
    }

    public void removeDocument(final TWmDocument pWmDocument) throws IOException {
        LOG.log(Level.INFO, "Remove document {0}...", pWmDocument);
        processServiceBean.get().removeDocument(pWmDocument.id);
        currentProcess.getDocuments().remove(pWmDocument);
        getObsDocuments().remove(pWmDocument);
        currentProcess.getDocuments().remove(pWmDocument);
        updateEventItemsForDelete(pWmDocument);
        final String desc = new DocumentEventSubject(WmEventTypeEn.documentRemoved, pWmDocument).getText();
        createAndStoreEvent(WmEventTypeEn.documentRemoved, null, desc);
        LOG.log(Level.INFO, "Document {0} was successfully removed!", pWmDocument);
    }

    public void removeAction(final TWmAction pWmAction) {
        LOG.log(Level.INFO, "Remove action {0}...", pWmAction);
        //currentProcess.getActions().remove(pWmAction);
        getObsActions().remove(pWmAction);
        currentProcess.getActions().remove(pWmAction);
        updateEventItemsForDelete(pWmAction);
        final String desc = new ActionEventSubject(WmEventTypeEn.actionRemoved, pWmAction).getText();
        createAndStoreEvent(WmEventTypeEn.actionRemoved, null, desc);
        LOG.log(Level.INFO, "Action {0} was successfully removed!", pWmAction);
    }

    public byte[] getDocumentContent(final long pWmDocumentId) throws IOException {
        LOG.log(Level.FINE, "Retrieve content of document id {0}...", pWmDocumentId);
        byte[] content = processServiceBean.get().getDocumentContent(pWmDocumentId);
        LOG.log(Level.FINE, "Content of document id {0} was successfully retrieved!", pWmDocumentId);
        return content;
    }

    /**
     * checks of a file with the given name is already stored in the current
     * Process
     *
     * @param file file to check
     * @return if the file is already stored
     */
    public boolean fileExists(File file) {
        return processServiceBean.get().existsInProcess(file.getName(), currentProcess.getId());
    }


    /*
    ProcessCases & Cases
     */
    /**
     * find Case Entity for db id Searches in the databse
     *
     * @param hCaseId db id for the hospitalcase to find
     * @return hospitalCase entity
     */
    public TCase findHospitalCase(long hCaseId) {
        return wmServiceBean.get().findCase(hCaseId);
    }

    /**
     * fetch case with current id from the Dataset different than
     * findHosptailCase, becasue methode search in current process Object
     *
     * @param id unique id for the case in process cases of the process
     * @return Tcase Entity or null
     */
    public TCase getCaseWithId(long id) {
        for (TWmProcessCase service : currentProcess.getProcessCases()) {
            if (service.getHosCase().getId() == id) {
                return service.getHosCase();
            }
        }
        return null;
    }

    /**
     * unlock case with id
     *
     * @param pCaseId case to unlock
     * @return boolean if case is unlocked
     */
    public boolean unlockCase(long pCaseId) {
        return lockServiceEjb.get().unlockCase(pCaseId, true);
    }

    /**
     * unlock process with id
     *
     * @param pProcessId case to unlock
     * @return boolean if process is unlocked
     */
    public boolean unlockProcess(long pProcessId) {
        return lockServiceEjb.get().unlockProcess(pProcessId, true);
    }

    /**
     * lock case with id
     *
     * @param pCaseId case to lock
     * @throws LockException lock failed
     */
    public void lockCase(long pCaseId) throws LockException {
        lockServiceEjb.get().lockCase(pCaseId);
    }

    /**
     * lock process with id
     *
     * @param pProcessId case to lock
     * @throws LockException lock failed
     */
    public void lockProcess(long pProcessId) throws LockException {
        lockServiceEjb.get().lockProcess(pProcessId);
    }

    /**
     * Check if case is locked
     *
     * @param pCaseId case to check
     * @return boolean if case is locked
     */
    public boolean isCaseLocked(long pCaseId) {
        return lockServiceEjb.get().isCaseLocked(pCaseId);
    }

    /**
     * Check if process is locked
     *
     * @param pProcessId process to check
     * @return boolean if case is locked
     */
    public boolean isLocked(long pProcessId) {
        return lockServiceEjb.get().isProcessLocked(pProcessId);
    }

    /**
     * Check if case is locked (throws LockException)
     *
     * @param pCaseId case to check
     * @throws LockException throws if case is locked
     */
    public void checkCaseLock(long pCaseId) throws LockException {
        lockServiceEjb.get().checkCaseLock(pCaseId);
    }

    /**
     * Check if case is locked (throws LockException)
     *
     * @param pProcessId process to check
     * @throws LockException throws if case is locked
     */
    public void checkProcessLock(long pProcessId) throws LockException {
        lockServiceEjb.get().checkProcessLock(pProcessId);
    }

    /**
     * unlocks the current process if its set
     *
     * @return if current process is unlocked or not
     */
    public boolean unlockCurrentProcess() {
        if (currentProcess != null) {
            long processId = currentProcess.getId();
            try {
                return lockServiceEjb.get().unlockProcess(processId);
            } catch (EJBException ex) {
                LOG.log(Level.FINEST, MessageFormat.format("Was not able to unlock process id {0}. Maybe CPX server was restarted!", processId), ex);
            }
        }
        return false;
    }

    /**
     * get list of cases of the current Process
     *
     * @return list of tcase entities
     */
    public List<TCase> getServiceOverviewList() {
        List<TCase> listOfCases = new ArrayList<>();
        if (obsProcessCaseList != null) {
            for (TWmProcessCase pCase : obsProcessCaseList) {
                listOfCases.add(pCase.getHosCase());
            }
        }
        return listOfCases;
    }

    /**
     * get observable list of Cases for the Process
     *
     * @return Process list
     */
    public synchronized ObservableList<TWmProcessCase> getObsProcessCases() {
        if (obsProcessCaseList == null) {
//            obsProcessCaseList = FXCollections.observableArrayList(currentProcess.getProcessCases());
            List<TWmProcessCase> procCases = processServiceBean.get().getProcessCases(currentProcess.getId());
            currentProcess.setProcessCases(new HashSet<>(procCases));
            obsProcessCaseList = FXCollections.observableArrayList(procCases);
        }
        return obsProcessCaseList;
    }

    /**
     * get all matching results for the partial case number
     *
     * @param partialCaseNumber partial case number
     * @return all cases in the db with that partial case number
     */
    public Collection<TCase> getCasesForNumber(String partialCaseNumber) {
        return caseServiceBean.get().findMatches(partialCaseNumber);
    }

    /**
     * get all available case numbers for partial case number
     *
     * @param partialCaseNumbers partial case number
     * @return list of case numbers as string
     */
    public Collection<String> getMatchingCaseNumbers(String partialCaseNumbers, long patientID) {
        return caseServiceBean.get().findMatchingCaseNumbers(partialCaseNumbers, patientID);
    }

    /**
     * gets a single case for a specific case number
     *
     * @param caseNumber number of that case
     * @return TCase entity for that casenumber
     */
    public TCase getCaseForNumber(String caseNumber) {
        return caseServiceBean.get().getCaseForNumber(caseNumber);
    }

    /**
     * stores process Case Object / Service and creates a case added event
     *
     * @param pCase TWmProcess Entity to store
     * @return newly stored Entity
     */
    public TWmProcessCase storeProcessCase(TWmProcessCase pCase) {
//        pCase.setProcess(currentProcess);
        if (currentProcess.getMainProcessCase() == null) {
            currentProcess.setMainProcessCase(pCase);
            pCase.setMainCase(true);
            currentProcess = (TWmProcessHospital) processServiceBean.get().updateProcess(currentProcess);
        } else {
            //AWi-20180105-CPX-1133:
            //add else to avoid unique constraint exception due to double persisting pCase-Entity
            pCase = processServiceBean.get().storeProcessCase(pCase, currentProcess.getId());
        }
        currentProcess.getProcessCases().add(pCase);
        obsProcessCaseList.add(pCase);
        
        createAndStoreEvent(WmEventTypeEn.caseAdded, pCase.getHosCase(), "");
        return pCase;
    }

    /**
     * delete processCase from Database, update ui and edit creation event can
     * not delete mainCase
     *
     * @param pWmProcessCase item to delete
     */
    public void removeProcessCase(TWmProcessCase pWmProcessCase) {
        if (pWmProcessCase == null) {
            LOG.log(Level.WARNING, "process case is null, cannot remove case from process!");
            return;
        }
        if (pWmProcessCase.getMainCase()) {
            LOG.log(Level.WARNING, "main case cannot be removed from process!");
            return;
        }
        currentProcess.getProcessCases().remove(pWmProcessCase);
        obsProcessCaseList.remove(pWmProcessCase);

        updateEventItemsForDelete(pWmProcessCase.getHosCase());
        final String desc = new CaseEventSubject(WmEventTypeEn.caseRemoved, pWmProcessCase.getHosCase()).getText();
        createAndStoreEvent(WmEventTypeEn.caseRemoved, null, desc);
        processServiceBean.get().removeProcessCase(pWmProcessCase);

    }

    /*
    Reminder
     */
    /**
     * store reminder in dataset and create new Event
     *
     * @param reminder reminder object to store
     */
    public void storeReminder(TWmReminder reminder) {
        if (obsReminderList == null) {
            obsReminderList = FXCollections.observableArrayList(reminder);
        }
        TWmEvent event;

        if (reminder.getId() == 0L) {
            //2017-04-18 DNi: New reminder
            obsReminderList.add(reminder);
            currentProcess.getReminders().add(reminder);
            event = createAndStoreEvent(WmEventTypeEn.reminderCreated, reminder, "");
        } else {
            //2017-04-18 DNi: Update existing reminder
            obsReminderList.set(obsReminderList.indexOf(reminder), reminder); //fires change listener and refreshes the reminder list on gui (or us refresh() on tableview!)
            event = createAndStoreEvent(WmEventTypeEn.reminderChanged, reminder, "");
            updateReminderListInProcess(reminder, currentProcess);
        }
//        long createEvent = System.currentTimeMillis();
        reminder.setProcess(currentProcess);

//        LOG.info("create reminder event in " + (System.currentTimeMillis() - createEvent) + " ms");
        if (event != null && event.getContent() instanceof TWmReminder) {
            reminder.setId(event.getContent().id);
            // add newly created reminder to the set of reminders (of the current process)
            currentProcess.getReminders().add(event.getReminder());
            updateEventItemsForReminder(event.getReminder());
        }
    }

    /**
     * get Observable reminder list
     *
     * @return list of reminders
     */
    public synchronized ObservableList<TWmReminder> getObsReminder() {
        return getObsReminder(null);
    }

    /**
     * get Observable reminder list
     *
     * @param pFinishedFl only (un-)finished reminders
     * @return list of reminders
     */
    public synchronized ObservableList<TWmReminder> getObsReminder(final Boolean pFinishedFl) {
        if (obsReminderList == null) {
//            obsReminderList = FXCollections.observableArrayList(currentProcess.getReminders());
            List<TWmReminder> reminder = processServiceBean.get().getReminder(currentProcess.getId(), pFinishedFl);
            if (!reminder.isEmpty()) {
                //rework sorting, sort by creation date, if no creation date is set (date is null) than order to last!
                Collections.sort(reminder, getCreationDateComparator());//Comparator.comparing(TWmReminder::getCreationDate,Comparator.nullsLast(Comparator.naturalOrder())));
            }
            currentProcess.setReminders(new HashSet<>(reminder));
            obsReminderList = FXCollections.observableArrayList(reminder);
        }
        return obsReminderList;
    }

    public void removeReminder(TWmReminder pWmReminder) {
//        currentProcess.getReminders().remove(item);
        LOG.log(Level.INFO, "Remove reminder {0}...", String.valueOf(pWmReminder));
        if (pWmReminder == null) {
            LOG.log(Level.WARNING, "reminder to remove is null!");
            return;
        }
//        if (!testMode) {
        if (currentProcess.getReminders().contains(pWmReminder)) {
            processServiceBean.get().removeReminder(pWmReminder);
        }
//        }
        currentProcess.getReminders().remove(pWmReminder);
        obsReminderList.remove(pWmReminder);
        updateEventItemsForDelete(pWmReminder);
        final String desc = new ReminderEventSubject(WmEventTypeEn.reminderRemoved, pWmReminder).getText();
        createAndStoreEvent(WmEventTypeEn.reminderRemoved, null, desc);
    }

//    /*
//    History Section / TimeLine
//     */

    /*
    Actions
     */
    /**
     * creates new emtpy Action type
     *
     * @return new Event with stored action
     */
    public TWmEvent createEmptyActionEvent() {
        TWmAction action = new TWmAction();
        //should implemented - but need handling
        List<CWmListActionSubject> listofActionSubjectObjects = MenuCache.getMenuCacheActionSubjects().values(new Date());
        Collections.sort(listofActionSubjectObjects);
        action.setActionType(listofActionSubjectObjects.get(0).getWmAsInternalId());

        return storeAction(action);
    }

    public TWmEvent storeAction(TWmAction action) {
        return createAndStoreEvent(WmEventTypeEn.actionAdded, action, "");
    }
    public TWmEvent updateAction(TWmAction action) {
        TWmEvent event = createAndStoreEvent(WmEventTypeEn.actionChanged, action, "");
        if (event != null) {
            action = event.getAction();
            updateEventItemsForAction(action);
            return event;
        }
        return null;
    }

    /*
    catalog data
     */
    /**
     * loads from the catalog the insurance company with the specific ident
     *
     * @param insCompanyIdent unique ident of the insurance
     * @return InsuranceCompany catalog object
     */
    public CpxInsuranceCompany findInsuranceCompanyByIdent(String insCompanyIdent) {
        return insCatalog.getByCode(insCompanyIdent, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    /**
     * find the name from a specific InsuranceCompany
     *
     * @param insCompanyIdent unique indent
     * @return string name of the insurance company, empty if no insurance
     * company is found
     */
    public String findInsuranceCompanyName(String insCompanyIdent) {
        return insCatalog.getByCode(insCompanyIdent, AbstractCpxCatalog.DEFAULT_COUNTRY).getInscName();
    }

    /**
     * tries to return the tooltip text for a specific insurance company
     *
     * @param insCompanyIdent unique ident of a insurance
     * @return string text for the tooltip of a insurance
     */
    public String findInsuranceToolTipText(String insCompanyIdent) {
        return findInsuranceCompanyByIdent(insCompanyIdent).toString();
    }

    /**
     * find Hospital for the ident
     *
     * @param hospitalIdent unique ident of the hospital
     * @return hospital catalog object
     */
    public CpxHospital findHospitalByIdent(String hospitalIdent) {
        return hosCatalog.getByCode(hospitalIdent, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    
    public boolean hasCatalogHospitals(){
        return hosCatalog.hasHospitals(AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    
    /**
     * stores request and update lists, set process to current catched process
     * creates new event
     *
     * @param request request to store
     * @return newly created request entity, or null if save failed
     */
    public TWmRequest storeRequest(TWmRequest request) {
        if (currentProcess != null) {
            request.setProcessHospital(currentProcess);
        }

        TWmEvent event = createAndStoreEvent(WmEventTypeEn.requestCreated, request, "");
        if (event != null) {
            currentProcess.getRequests().add(event.getRequest());
            getObsRequests().add(event.getRequest());
            return event.getRequest();
        }
        return null;
    }

    /**
     * To store Inka message in DB
     *
     * @param kainInka KainInka object to store
     * @return stored value
     */
    public TP301KainInka storeKainInka(TP301KainInka kainInka) {
        if (currentProcess != null) {
//            kainInka.setProcessHospital(currentProcess);
            kainInka.setTCaseId(currentProcess.getMainCase());
        }
        TWmEvent event = createAndStoreEvent(WmEventTypeEn.inkaStored, kainInka, "");
        if (event != null) {
            getObsInkaMsgs().add(event.getKainInka());
            return event.getKainInka();
        }
        return null;
//        return processServiceBean.get().storeKainInka(kainInka);
    }

    /**
     * To update Inka message in DB
     *
     * @param kainInka KainInka object to update
     * @param isCreateNewEvent create new event?
     * @return updated kainInka object
     */
    public TP301KainInka updateKainInka(TP301KainInka kainInka, boolean isCreateNewEvent) {
        if (currentProcess != null) {
//            kainInka.setProcessHospital(currentProcess);
            kainInka.setTCaseId(currentProcess.getMainCase());
        }
        if (isCreateNewEvent) {
            TWmEvent event = createAndStoreEvent(WmEventTypeEn.inkaUpdated, kainInka, "");
            if (event != null) {
                getObsInkaMsgs().set(getObsInkaMsgs().indexOf(kainInka), kainInka);
                return event.getKainInka();
            }
        } else {
            TP301KainInka updateKainInka = processServiceBean.get().updateKainInka(kainInka);
            if (updateKainInka != null) {
                updateEventItemsForKainInka(updateKainInka);
            }
            return updateKainInka;
        }
        return null;
    }

    /**
     * To store Inka message in DB
     *
     * @param kainInka KainInka object to store
     * @return stored value
     */
    public TP301KainInka sendInkaMessage(TP301KainInka kainInka) {
        if (currentProcess != null) {
            kainInka.setTCaseId(currentProcess.getMainCase());
        }
        TWmEvent event = createAndStoreEvent(WmEventTypeEn.inkaSent, kainInka, "");
        if (event != null) {
            return event.getKainInka();
        }
        return null;
    }

//    public void cancelInkaMessage(TP301KainInka kainInka) throws IllegalArgumentException {
    public TP301KainInka cancelInkaMessage(TP301KainInka kainInka) {
        if (currentProcess != null) {
            kainInka.setTCaseId(currentProcess.getMainCase());
        }
        TWmEvent event = createAndStoreEvent(WmEventTypeEn.inkaCancelled, kainInka, "");
        if (event != null) {
            return event.getKainInka();
        }
        return null;
    }

    /**
     * gets the current observable list of requests
     *
     * @return list of twmrequest entites
     */
    public synchronized ObservableList<TWmRequest> getObsRequests() {
        if (currentProcess == null) {
            return FXCollections.emptyObservableList();
        }
        if (obsRequestList == null) {
            List<TWmRequest> requests = loadRequests(currentProcess.getId());
            currentProcess.setRequests(new HashSet<>(requests));
            obsRequestList = FXCollections.observableArrayList(requests);
        }
        return obsRequestList;
    }

    public synchronized List<TWmRequest> loadRequests(long pProcessId) {
        return processServiceBean.get().getRequests(pProcessId);
    }

    public TWmRequest loadLatestRequest() {
        long start = System.currentTimeMillis();
        TWmRequest request = processServiceBean.get().getLatestRequest(currentProcess.getId());
        LOG.log(Level.INFO, "load latest request for process {0} from server in {1}", new Object[]{currentProcess.id, (System.currentTimeMillis() - start)});
        return request;
    }

    public synchronized TWmRequest getLastObsRequest() {
        return getLastObsRequest(null);
    }

    @SuppressWarnings("unchecked")
    public <T extends TWmRequest> T getLastObsRequest(final WmRequestTypeEn pRequestType) {
        ObservableList<TWmRequest> obsRequests = getObsRequests();
        if (obsRequests == null || obsRequests.isEmpty()) {
            LOG.log(Level.INFO, "There is no any Request (of any type) for this Process ...");
            return null;
        }

        TWmRequest latestRequest = null;
        Iterator<TWmRequest> it = obsRequests.iterator();
        while (it.hasNext()) {
            TWmRequest t = it.next();
            if (t == null) {
                continue;
            }
            if (pRequestType != null && !pRequestType.equals(t.getRequestTypeEnum())) {
                continue;
            }
            if (latestRequest == null
                    || (t.getCreationDate() != null
                    && latestRequest.getCreationDate() != null
                    && t.getCreationDate().after(latestRequest.getCreationDate()))) {
                latestRequest = t;
            }
        }

        return (T) latestRequest;
    }

    public synchronized ObservableList<TWmAction> getObsActions() {
        if (currentProcess == null) {
            return FXCollections.emptyObservableList();
        }
        if (obsActionList == null) {
            List<TWmAction> actions = processServiceBean.get().getActions(currentProcess.getId());
            currentProcess.setActions(new HashSet<>(actions));
            obsActionList = FXCollections.observableArrayList(actions);
        }
        return obsActionList;
    }

    public TWmAction getLastObsAction() {
        ObservableList<TWmAction> obsActions = getObsActions();
        if (obsActions == null || obsActions.isEmpty()) {
            LOG.log(Level.INFO, "There is no any Actions for this Process ...");
            return null;
        }

        TWmAction latestAction = null;
        Iterator<TWmAction> it = obsActions.iterator();
        while (it.hasNext()) {
            TWmAction t = it.next();
            if (t == null) {
                continue;
            }
            if (latestAction == null
                    || (t.getCreationDate() != null
                    && latestAction.getCreationDate() != null
                    && t.getCreationDate().after(latestAction.getCreationDate()))) {
                latestAction = t;
            }
        }

        return latestAction;
    }

    public ObservableList<TP301KainInka> getObsInkaMsgs() {
        if (currentProcess == null) {
            return FXCollections.emptyObservableList();
        }
        if (obsInkaMsgList == null) {
            List<TP301KainInka> inkaMsgs = processServiceBean.get().getInkaMsgs(currentProcess.getMainCase().getId());
            obsInkaMsgList = FXCollections.observableArrayList(inkaMsgs);
        }
        return obsInkaMsgList;
    }

    public void updateEventItemsForRequest(TWmRequest item) {
        if (item == null) {
            return;
        }
        for (TWmEvent event : getEventsAsObsList()) {
            if (event.getRequest() == null) {
                continue;
            }
            if (event.getRequest().id == item.id) {
                event.setRequest(item);
            }
        }
        updateRequestListInProcess(item, currentProcess);
    }
    public void updateRequestListInProcess(TWmRequest pRequest, TWmProcessHospital pProcess){
        Objects.requireNonNull(pRequest);
        Objects.requireNonNull(pProcess);
        Iterator<TWmRequest> it = pProcess.getRequests().iterator();
        while (it.hasNext()) {
            AbstractEntity next = it.next();
            if(next.id == pRequest.id){
                it.remove();
                break;
            }
        }
        pProcess.getRequests().add(pRequest); // set not sorted - i guess it matters not where the new entity is added
    }
    public void updateActionListInProcess(TWmAction pAction, TWmProcess pProcess){
        Objects.requireNonNull(pAction);
        Objects.requireNonNull(pProcess);
        Iterator<TWmAction> it = pProcess.getActions().iterator();
        while (it.hasNext()) {
            AbstractEntity next = it.next();
            if(next.id == pAction.id){
                it.remove();
                break;
            }
        }
        pProcess.getActions().add(pAction); // set not sorted - i guess it matters not where the new entity is added
    }
    private void updateEventItemsForAction(TWmAction item) {
        if (item == null) {
            return;
        }
        for (TWmEvent event : getEventsAsObsList()) {
            if (event.getAction() == null) {
                continue;
            }
            if (event.getAction().id == item.id) {
                event.setAction(item);
            }
        }
        updateActionListInProcess(item, currentProcess);
    }
    
    public void updateReminderListInProcess(TWmReminder pReminder, TWmProcess pProcess){
        Objects.requireNonNull(pReminder);
        Objects.requireNonNull(pProcess);
        Iterator<TWmReminder> it = pProcess.getReminders().iterator();
        while (it.hasNext()) {
            AbstractEntity next = it.next();
            if(next.id == pReminder.id){
                it.remove();
                break;
            }
        }
        pProcess.getReminders().add(pReminder); // set not sorted - i guess it matters not where the new entity is added
    }
    
    public void updateEventItemsForReminder(TWmReminder item) {
        if (item == null) {
            return;
        }
        for (TWmEvent event : getEventsAsObsList()) {
            if (event.getReminder() == null) {
                continue;
            }
            if (event.getReminder().id == item.id) {
                event.setReminder(item);
            }
        }
        updateReminderListInProcess(item, currentProcess);
    }
    
    public void updateDocumentListInProcess(TWmDocument pDocument, TWmProcess pProcess){
        Objects.requireNonNull(pDocument);
        Objects.requireNonNull(pProcess);
        Iterator<TWmDocument> it = pProcess.getDocuments().iterator();
        while (it.hasNext()) {
            AbstractEntity next = it.next();
            if(next.id == pDocument.id){
                it.remove();
                break;
            }
        }
        pProcess.getDocuments().add(pDocument); // set not sorted - i guess it matters not where the new entity is added
    }
    
    private void updateEventItemsForDocument(TWmDocument item) {
        if (item == null) {
            return;
        }
        for (TWmEvent event : getEventsAsObsList()) {
            if (event.getDocument() == null) {
                continue;
            }
            if (event.getDocument().id == item.id) {
                event.setDocument(item);
            }
        }
        updateDocumentListInProcess(item, currentProcess);
    }
    
//    public void updateKainInkaListInProcess(TP301KainInka pKainInka, TWmProcessHospital pProcess){
//        Objects.requireNonNull(pKainInka);
//        Objects.requireNonNull(pProcess);
//        Iterator<TWmDocument> it = pProcess.getMainCase().get.iterator();
//        while (it.hasNext()) {
//            AbstractEntity next = it.next();
//            if(next.id == pKainInka.id){
//                it.remove();
//                break;
//            }
//        }
//        pProcess.getDocuments().add(pKainInka); // set not sorted - i guess it matters not where the new entity is added
//    }
    
    private void updateEventItemsForKainInka(TP301KainInka item) {
        if (item == null) {
            return;
        }
        for (TWmEvent event : getEventsAsObsList()) {
            if (event.getKainInka() == null) {
                continue;
            }
            if (event.getKainInka().id == item.id) {
                event.setKainInka(item);
            }
        }
    }

    /**
     * updates the event list for delete, stores current header for the history
     * in the event subject and sets content (Action,Reminder,Request,Document
     * or Case) reference to null saves new values in the Database.
     *
     * @param item item to update
     */
    private void updateEventItemsForDelete(AbstractEntity item) {
        if (item == null) {
            LOG.log(Level.WARNING, "cannot update item for delete, cuz its null!");
            return;
        }
        long start = System.currentTimeMillis();
        //int cnt = 0;
        List<TWmEvent> deletedEvents = new ArrayList<>();
        for (TWmEvent event : getEventsAsObsList()) {
            if (event.isOrphaned()) {
                //event has no content!
                continue;
            }
            final EventSubject<? extends AbstractEntity> subjectGenerator;
            if (event.isDocumentInEvent(item)) {
                subjectGenerator = new DocumentEventSubject(event.getEventType(), (TWmDocument) item);
            } else if (event.isHosCaseInEvent(item)) {
                subjectGenerator = new CaseEventSubject(event.getEventType(), (TCase) item);
            } else if (event.isReminderInEvent(item)) {
                subjectGenerator = new ReminderEventSubject(event.getEventType(), (TWmReminder) item);
            } else if (event.isActionInEvent(item)) {
                subjectGenerator = new ActionEventSubject(event.getEventType(), (TWmAction) item);
            } else if (event.isRequestInEvent(item)) {
                subjectGenerator = new RequestEventSubject(event.getEventType(), (TWmRequest) item);
            } else if (event.isKainInkaInEvent(item)) {
                LOG.log(Level.WARNING, "KAIN/INKA objects cannot be deleted!");
                subjectGenerator = null;
            } else {
                LOG.log(Level.FINEST, "Unknown event type (remove is not fully implemented for this type): {0}", event.getEventType().name());
                subjectGenerator = null;
            }

            if (subjectGenerator != null) {
                final String subject = subjectGenerator.getText();
                event.setSubject(subject);
                String desc = subjectGenerator.getRemovedDescription();
                event.setDescription(event.getDescription() == null ? desc : event.getDescription().concat("\n " + desc));
                event.removeContent();
                updateEvent(event);
                deletedEvents.add(event);
            }
        }
        int cnt = deletedEvents.size();
        if (!Session.instance().isShowHistoryDeleted()) {
            obsEventList.removeAll(deletedEvents);
        }
        LOG.log(Level.INFO, "update total of {0} items due to delete of {1}", new Object[]{cnt, item.getClass().getSimpleName()});
        LOG.log(Level.INFO, "Time total to update items {0} ms", (System.currentTimeMillis() - start));
    }

    public void removeOrphanedEvents() {
        if (obsEventList == null) {
            return;
        }
        if (!Session.instance().isShowHistoryDeleted()) {
            ArrayList<TWmEvent> orphan = new ArrayList<>();
            for (TWmEvent event : new ArrayList<>(obsEventList)) {
                if (event.isOrphaned()) {
                    orphan.add(event);
                }
            }
            obsEventList.removeAll(orphan);
        }
    }

    public List<UserDTO> getAllUsers() {
        //2017-04-26 DNi: I'd be probably nice to cache this list to only load it once per session
        return authServiceEjb.get().getAllActiveUsers();
    }

    /**
     * @param pUName string part of username
     * @return get users matching given part-string of the username
     */
    public Collection<String> getMatchingUsers(String pUName) {
        return authServiceEjb.get().findMatchingUsers(pUName);
    }

    /**
     * @param pUName user name , that is stored in db
     * @return user id
     */
    public Long getUserID(String pUName) {
        try {
            //return authServiceEjb.get().getIdbyUName(pUName);
            return MenuCache.instance().getUserId(pUName);
        } catch (NamingException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public UserDTO getActiveUser(final Long pId) {
        if (pId == null || pId.equals(0L)) {
            return null;
        }
        List<UserDTO> list = getAllUsers();
        Iterator<UserDTO> it = list.iterator();
        while (it.hasNext()) {
            UserDTO userDto = it.next();
            if (userDto == null) {
                continue;
            }
            if (pId.equals(userDto.getId())) {
                return userDto;
            }
        }
        return null;
    }

    public String getUserLabel(final String pUserId) {
        String userId = (pUserId == null) ? "" : pUserId.trim();
        Long id = null;
        if (!userId.isEmpty()) {
            try {
                id = Long.valueOf(userId);
            } catch (NumberFormatException ex) {
                //
            }
        }
        return getUserLabel(id);
    }

    public String getUserLabel(final Long pUserId) {
        if (pUserId == null || pUserId.equals(0L)) {
            return "";
        }
        String fullName = getUserFullName(pUserId);
        String name = getUserLogin(pUserId);
        if ((name == null || name.trim().isEmpty()) && (fullName == null || fullName.trim().isEmpty())) {
            //User for passed User-ID is missing. Print User-ID to get a clue
            return "User-ID " + pUserId;
        }
        return fullName + " (" + name + ")";
    }

    public TWmRequest updateRequest(TWmRequest request) {
        TWmEvent event = createAndStoreEvent(WmEventTypeEn.requestUpdated, request, "");
        if (event != null) {
            request = event.getRequest();
            updateEventItemsForRequest(request);
            return request;
        }
        return null;
    }

    public void removeRequest(final TWmRequest pWmRequest) {
        LOG.log(Level.INFO, "Remove request {0}...", pWmRequest);
        if (pWmRequest == null) {
            LOG.log(Level.WARNING, "request to remove is null!");
            return;
        }
        if (getObsRequests().contains(pWmRequest)) {
            processServiceBean.get().removeRequest(pWmRequest);
        }
        getObsRequests().remove(pWmRequest);

        updateEventItemsForDelete(pWmRequest);

        final String desc = new RequestEventSubject(WmEventTypeEn.requestRemoved, pWmRequest).getText();
        createAndStoreEvent(WmEventTypeEn.requestRemoved, null, desc);

        LOG.log(Level.INFO, "Request {0} was successfully removed!", pWmRequest);
    }

    public TCaseIcd getMainDiagnosisForVersion(long pCaseDetailsId) {
        return processServiceBean.get().getMainDiagnosisForVersion(pCaseDetailsId);
    }

    /**
     * gets the icd catalog data from the catalog by icd code and year by
     * default it is searched in the german catalog
     *
     * @param icdcCode code of the icd
     * @param year year of validity
     * @return catalog icd object
     */
    public CpxIcd findIcdCatalogData(String icdcCode, int year) {
        return icdCatalog.getByCode(icdcCode, AbstractCpxCatalog.DEFAULT_COUNTRY, year);
    }

    /**
     * get the count of the secondary diagnosis over all departments 0 by
     * default if testmode is active
     *
     * @param pVersionId version id
     * @return cound of all secondary diagnosis
     */
    public int getCountSecondaryDiagnosis(long pVersionId) {
        return processServiceBean.get().getSecondaryDiagnosisCount(pVersionId);
    }

    /**
     * get the Count of Procedures for the version over all departemtens 0 by
     * default if testmode is active
     *
     * @param pVersionId version id
     * @return count of procedures for that version
     */
    public int getCountProcedures(long pVersionId) {
        return processServiceBean.get().getProcessCount(pVersionId);
    }

    /**
     * get the last DRG Result for the version if any is present if testmode a
     * default Grouping result is returned (Z01Z)
     *
     * @param pVersionId case version to get drg result
     * @param pGrouper current selected grouper
     * @return drg result
     */
    public TGroupingResults getDrgResult(long pVersionId, GDRGModel pGrouper) {
        return processServiceBean.get().getGroupingResult(pVersionId, pGrouper);
    }

    /**
     * gets the description of the DRG from the drg catalog
     *
     * @param pDrgCode drg code
     * @param pYear year of validity
     * @return description as set in the catalog for that year
     */
    public String getDrgDescription(String pDrgCode, int pYear) {
        return drgCatalog.getDrgDescription(pDrgCode, AbstractCpxCatalog.DEFAULT_COUNTRY, pYear);
    }

    public String getDrgDescriptionText(String pDrgCode, int pYear) {
        return drgCatalog.getDrgDecriptionText(pDrgCode, AbstractCpxCatalog.DEFAULT_COUNTRY, pYear);
    }

    /**
     * gets the description of the PEPP from the PEPP catalog
     *
     * @param pPeppCode PEPP code
     * @param pYear year of validity
     * @return description as set in the catalog for that year
     */
    public String getPeppDescription(String pPeppCode, int pYear) {
        return peppCatalog.getPeppDescription(pPeppCode, AbstractCpxCatalog.DEFAULT_COUNTRY, pYear);
    }

    public String getPeppDescriptionText(String pPeppCode, int pYear) {
        return peppCatalog.getPeppDecriptionText(pPeppCode, AbstractCpxCatalog.DEFAULT_COUNTRY, pYear);
    }

    /**
     * gets the supplementary fee value for the version
     *
     * @param pDetailsId version id
     * @param pType supplementary fee type
     * @return supplementary fee value
     */
    public double findSupplementaryFee(long pDetailsId, SupplFeeTypeEn pType) {
        GDRGModel grouper = CpxClientConfig.instance().getSelectedGrouper();
        return processServiceBean.get().findSupplementaryFee(grouper, pDetailsId, pType);
    }

    /**
     * update document entity in the database
     *
     * @param pDocument document to update
     * @return updated document
     */
    public TWmDocument updateDocument(TWmDocument pDocument) {
        if (obsDocumentList != null) {
            int index = getObsDocuments().indexOf(pDocument);
            if (index > 0) {
                getObsDocuments().remove(index);
                currentProcess.getDocuments().remove(pDocument);
                getObsDocuments().add(index, pDocument);
                currentProcess.getDocuments().add(pDocument);
            }
        }
        TWmDocument document = processServiceBean.get().updateDocument(pDocument);
        if (document != null) {
            updateEventItemsForDocument(document);
            return document;
        }
        return document;
    }

    // to get the current process finalisation.
    public TWmProcessHospitalFinalisation getCurrentProcessFinalisation() {
        TWmProcess p = currentProcess;
        if (p == null || !p.isProcessHospital()) {
            return null;
        }
        TWmProcessHospital ph = (TWmProcessHospital) p;
        return ph.getProcessHospitalFinalisation();
    }

    // to get the main case of the current process.
    public TCase getCurrentMainCase() {
        TWmProcess p = currentProcess;
        if (p == null) {
            return null;
        }
        return p.getMainCase();
    }

    // to get the current process.
    public TWmProcess getCurrentProcess() {
        return currentProcess;
    }

    /**
     * the current process (workflow) number (method is just for your
     * convenience)
     *
     * @return workflow number of current process (returns 0 if missing)
     */
    public long getCurrentProcessNumber() {
        return currentProcess == null ? 0L : currentProcess.getWorkflowNumber();
    }

    /**
     * the current process id (method is just for your convenience)
     *
     * @return workflow number of current process (returns 0 if missing)
     */
    public long getCurrentProcessId() {
        return currentProcess == null ? 0L : currentProcess.id;
    }

    public TWmRequestMdk storeMdkRequest(TWmRequestMdk request) {
        if (currentProcess != null) {
            currentProcess.getRequests().add(request);
            request.setProcessHospital(currentProcess);
            getObsRequests().add(request);
        }

        TWmEvent event = createAndStoreEvent(WmEventTypeEn.requestCreated, request, "");
        if (event != null) {
            return (TWmRequestMdk) event.getRequest();
        }
        return null;
    }

    /**
     * get the baserate for the version
     *
     * @param pDetails version to get baserate for
     * @return baserate fee value
     */
    public Double getDrgBaseRateFeeValue(TCaseDetails pDetails) {
        return CpxBaserateCatalog.instance().findDrgBaserateFeeValue(pDetails.getHospitalCase().getCsHospitalIdent(), pDetails.getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    
    public CpxBaserate getDrgBaseRate(TCaseDetails pDetails) {
        return CpxBaserateCatalog.instance().findDrgBaserate(pDetails.getHospitalCase().getCsHospitalIdent(), pDetails.getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
    }
    
    

    public Double getCareBaseRateFeeValue(TCaseDetails pDetails) {
        return CpxBaserateCatalog.instance().findCareBaserateFeeValue(pDetails.getHospitalCase().getCsHospitalIdent(), pDetails.getCsdAdmissionDate(), AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    /**
     * get the baserate for Pepp Case by HosIdent
     *
     * @param pHostIdent HosIdent to get baserate for
     * @return baserate list
     */
    public List<CpxBaserate> findPeppBaserate4HosIdent(String pHostIdent) {
        return CpxBaserateCatalog.instance().findPeppBaserate4HosIdent(pHostIdent, AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    /**
     * @return all avaiable audit reasons for the creation date of the current
     * process
     */
    public List<CMdkAuditreason> getAvailableAuditReasons() {
        //return processServiceBean.get().getAllAvailableCMdkAuditReasons(currentProcess.getCreationDate());
        List<CMdkAuditreason> reasons = MenuCache.getMenuCacheAuditReasons().values(currentProcess.getCreationDate(), MenuCacheOptionsEn.IGNORE_INACTIVE);
        if(getCurrentMainCase() == null){
            return reasons;
        }
        return reasons.stream().filter((t) -> {
            return t.getMdkArCaseType().equals(getCurrentMainCase().getCsCaseTypeEn());
        }).collect(Collectors.toList());
    }

    // get valid, undeleted Audit reasons based on process creation date
    public Map<Long, CMdkAuditreason> getValidUndeletedAuditReasonsMap() {
        if (currentProcess != null && currentProcess.getCreationDate() != null) {
            //return processServiceBean.get().getValidUndeletedAuditReasons(currentProcess.getCreationDate());
            return MenuCache.getMenuCacheAuditReasons().map(currentProcess.getCreationDate(), MenuCacheOptionsEn.IGNORE_DELETED);
        } else {
            //return processServiceBean.get().getValidUndeletedAuditReasons(new Date());
            return MenuCache.getMenuCacheAuditReasons().map(new Date(), MenuCacheOptionsEn.IGNORE_DELETED);
        }
    }

    public Set<Map.Entry<Long, CMdkAuditreason>> getValidUndeletedAuditReasonsEntries() {
        return getValidUndeletedAuditReasonsMap().entrySet();
    }
    /**
     * @return all available process results for the creation date of current
     * process
     */
    public List<CWmListProcessResult> getAvailableProcessResults() {
        return MenuCache.getMenuCacheProcessResults().values(currentProcess.getCreationDate(), MenuCacheOptionsEn.IGNORE_INACTIVE);
        //return processServiceBean.get().getAllAvailableProcessResults(currentProcess.getCreationDate());
    }

    /**
     * @return all available mdk state for today process
     */
    public List<CWmListMdkState> getAvailableMdkStates() {
        return MenuCache.getMenuCacheRequestStates().values(new Date(), MenuCacheOptionsEn.IGNORE_INACTIVE);
        //return processServiceBean.get().getAllMdkStatesObjects(new Date());
    }

    /**
     * @return all available process topics for the creation date of current
     * process
     */
    public List<CWmListProcessTopic> getAvailableProcessTopics() {
        //return processServiceBean.get().getAllAvailableProcessTopics(currentProcess != null ? currentProcess.getCreationDate() : null);
        return MenuCache.getMenuCacheProcessTopics().values(currentProcess != null ? currentProcess.getCreationDate() : null, MenuCacheOptionsEn.IGNORE_INACTIVE);
    }

    /**
     * @param pProcessResult process result to save or update
     * @return updated process result
     */
    public TWmProcessHospitalFinalisation saveOrUpdateProcessResult(TWmProcessHospitalFinalisation pProcessResult) {
        ((TWmProcessHospital) getCurrentProcess()).setProcessHospitalFinalisation(processServiceBean.get().saveOrUpdateProcessResult(pProcessResult));
        return ((TWmProcessHospital) getCurrentProcess()).getProcessHospitalFinalisation();
    }

    public void updateProcess(TWmProcess process) {
        final long startTime = System.currentTimeMillis();
        processServiceBean.get().updateProcess(process);
        LOG.log(Level.FINER, "updateProcess took {0} ms", (System.currentTimeMillis() - startTime));
    }

    public void updateProcessModification(final long pProcessId) {
        final long startTime = System.currentTimeMillis();
        processServiceBean.get().updateProcessModification(pProcessId);
        LOG.log(Level.FINER, "updateProcessModification took {0} ms", (System.currentTimeMillis() - startTime));
    }

    public void updateProcessHospital(TWmProcessHospital process) {
        processServiceBean.get().updateProcessHospital(process);
    }

    /**
     * loads new instance of base case from server needed to avoid sync issue in
     * process finalisation
     *
     * @return actual tcase instance
     */
    public TCase loadBaseCase() {
        long start = System.currentTimeMillis();
        TCase baseCase = findHospitalCase(getCurrentProcess().getMainCase().getId());
        LOG.log(Level.INFO, "load new base case in {0}", (System.currentTimeMillis() - start));
        for (TWmProcessCase processCase : getCurrentProcess().getProcessCases()) {
            if (processCase.getHosCase().getId() == baseCase.getId()) {
                processCase.setHosCase(baseCase);
            }
        }
        return baseCase;
    }

    /**
     * @param pInitialVersion case details database id
     * @return get the case details entity with the given id
     */
    public TCaseDetails getCaseDetails(long pInitialVersion) {
        return caseServiceBean.get().findCaseDetails(pInitialVersion);
    }

//    public void setProcessmasterDetailPane(ProcessMasterDetailPane processMasterDetailPane) {
//        pMasterDetailPane = processMasterDetailPane;
//    }
//
//    public ProcessMasterDetailPane getProcessMasterDetailPane() {
//        return pMasterDetailPane;
//    }

    public TCase getMainCase(TWmProcess currentProcess) {
        return processServiceBean.get().getMainCaseForProcess(currentProcess.getId());
    }

    /**
     * @param pCaseId case databse id
     * @return get current local version of the case
     */
    public TCaseDetails getCurrentLocal(long pCaseId) {
        return caseServiceBean.get().findCurrentLocal(pCaseId);
    }

    /**
     * @param pCaseId case databse id
     * @return get current local version of the case
     */
    public TCaseDetails getCurrentExtern(long pCaseId) {
        return caseServiceBean.get().findCurrentExtern(pCaseId);
    }

    public List<CWmListDraftType> getAllAvailabledrafttTypes(CategoryEn pCategory) {
        return MenuCache.getMenuCacheDraftTypes().getDraftTypes(pCategory);
    }

    public TPatientDetails findActualPatientDetails(long id) {
        long startTime = System.currentTimeMillis();
        TPatientDetails patd = caseServiceBean.get().findActualPatientDetails(id);
        LOG.log(Level.FINER, "getting actual case details for patient id {0} took {1} ms", new Object[]{id, (System.currentTimeMillis() - startTime)});
        return patd;
    }

    public TInsurance findActualPatientInsurance(long id) {
        long startTime = System.currentTimeMillis();
        TInsurance ins = caseServiceBean.get().findActualPatientInsurance(id);
        LOG.log(Level.FINER, "getting actual insurance for patient id {0} took {1} ms", new Object[]{id, (System.currentTimeMillis() - startTime)});
        return ins;
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
    public ObservableMap<Object, Object> getProperties() {
        if (properties == null) {
            properties = FXCollections.observableMap(new HashMap<>());
        }
        return properties;
    }

    public void saveOrUpdateProcess(TWmProcess process) {
        processServiceBean.get().saveOrUpdateProcess(process);
    }

    /**
     * @param pTopic topic ident
     * @return topic for ident (from common db)
     */
    public CWmListProcessTopic getProcessTopic(Long pTopic) {
        //return processServiceBean.get().getProcessTopic(pTopic);
        return MenuCache.getMenuCacheProcessTopics().get(pTopic);
    }

    public List<TWmProcess> findWorkflowsByPatient(TPatient patient) {
        return processServiceBean.get().findWorkflowsByPatient(patient);
    }

    public ObservableList<TWmEvent> unSetEventList() {
        ObservableList<TWmEvent> oldList = obsEventList;
        obsEventList = null;
        return oldList;
    }

    public void setCurrentProcess(TWmProcess process) {
        if (process != null && !process.isProcessHospital()) {
            throw new IllegalArgumentException("process " + process + " is not a hospital process. Insurance process is not supported yet!");
        }
        currentProcess = (TWmProcessHospital) process;
    }

    public List<CWmListReminderSubject> getAllAvailableReminderSubject(final Date pDate) {
        //return processServiceBean.get().getAllReminderSubjects(pDate);
        return MenuCache.getMenuCacheReminderSubjects().values(pDate);
    }

    public List<TWmReminder> getRemindersForRequest(long requestId) {
        List<TWmReminder> reminders = new ArrayList<>();
        if (obsEventList != null) {
            obsEventList.stream().forEach((item) -> {
                if (item.getReminder() != null && item.getRequest() != null) {
                    if (item.getRequest().getId() == requestId && !reminders.contains(item.getReminder())) {
                        reminders.add(item.getReminder());
                    }
                }
            });
        }
        return reminders;
    }

    public TWmEvent storeReminderForRequest(TWmReminder reminder, TWmRequest req) {
        if (obsReminderList == null) {
            obsReminderList = FXCollections.observableArrayList(reminder);
        }

        TWmEvent event = createAndStoreReminderEvent(reminder, req);
//        if (reminder.getId() == 0L) {
//            obsReminderList.add(reminder);
//        } else {
//            obsReminderList.set(obsReminderList.indexOf(reminder), reminder);
//        }
        addOrUpdateReminder(reminder);
        reminder.setProcess(currentProcess);
        if (event != null) {
            event.setRequest(req);
            if (event.getContent() instanceof TWmReminder) {
                reminder.setId(event.getContent().id);
            }
            updateEventItemsForReminder(event.getReminder());
        }
        return event;
    }

    public void addOrUpdateReminder(TWmReminder pReminder) {
        if (pReminder.getId() == 0L) {
            getObsReminder().add(pReminder);
            currentProcess.getReminders().add(pReminder);
        } else {
            getObsReminder().set(getObsReminder().indexOf(pReminder), pReminder);
            updateReminderListInProcess(pReminder, currentProcess);
        }
    }

    public TWmEvent createAndStoreReminderEvent(TWmReminder pReminder, TWmRequest pRequest) {
        WmEventTypeEn eventType = pReminder.getId() == 0L ? WmEventTypeEn.reminderCreated : WmEventTypeEn.reminderChanged;
        TWmEvent event = createAndStoreEvent(eventType, pReminder, "");
        if (event == null) {
            LOG.log(Level.SEVERE, "Reminder eventType: {0} could not be created!", eventType.name());
            return null;
        }
        event.setRequest(pRequest);
        updateEvent(event);
        return event;
    }

    public TWmEvent createReminderEvent(TWmReminder pReminder, TWmRequest pRequest) {
        WmEventTypeEn eventType = pReminder.getId() == 0L ? WmEventTypeEn.reminderCreated : WmEventTypeEn.reminderChanged;
        TWmEvent event = createNewEvent(eventType, pReminder, currentProcess, "");
        event.setRequest(pRequest);
        return event;
    }

    public TWmEvent createUpdateRequestEvent(TWmRequest pRequest) {
        TWmEvent event = createNewEvent(WmEventTypeEn.requestUpdated, pRequest, currentProcess, "");
        event.setRequest(pRequest);
        return event;
    }

    public TWmEvent createNewRequestEvent(TWmRequest pRequest) {
        TWmEvent event = createNewEvent(WmEventTypeEn.requestCreated, pRequest, currentProcess, "");
        event.setRequest(pRequest);
        return event;
    }

    public void checkBillFor6WeeksDeadline(TCase pCase) {
        Date pBillingDate = pCase.getCsBillingDate();
        CDeadline sixWDeadline = MenuCache.getMenuCacheDeadlines().get6Wd();
        if (sixWDeadline != null) {
            if (pBillingDate == null) {
                Notifications notif = NotificationsFactory.instance().createWarningNotification();
                notif.text(Lang.getSapBillingdateEmpty());
                showNotification(notif);
            } else {
                if (!isInDeadline(pBillingDate, sixWDeadline)) {
//                long days = (new Date().getTime() - pBillingDate.getTime()) / (1000 * 60 * 60 * 24);
//                long weeks = days / 7;
//                long frist = sixWDeadline.getDlTimeQuantity();
//                if (sixWDeadline.getDlTimeUnit().equals(ChronoUnit.WEEKS) && weeks > frist) {
                    Notifications notif = NotificationsFactory.instance().createWarningNotification();
                    notif.text(Lang.getSap6weeksdeadline());
                    showNotification(notif);
                }
            }
        }
    }
    /**
     * @param pNotification notifications Object to show - do only simple show! Show is wrapped in Platform.runlater
     */
    public void showNotification(Notifications pNotification){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pNotification.show();
            }
        });
    }
    public boolean isInDeadline(Date pDate, CDeadline pDeadline) {
        pDate = Objects.requireNonNull(pDate, "Date Object is null");
        pDeadline = Objects.requireNonNull(pDeadline, "Deadline is null");

        long days = (new Date().getTime() - pDate.getTime()) / (1000 * 60 * 60 * 24);
        long weeks = days / 7;
        long frist = pDeadline.getDlTimeQuantity();
        return !(pDeadline.getDlTimeUnit().equals(ChronoUnit.WEEKS) && weeks > frist);
    }

    public void checkKain6WeeksDeadline(TWmProcess pProcess) {
        Objects.requireNonNull(pProcess, "Can not detect if there is a 6 Week deadline, Process is null!");
        if(pProcess.getMainCase() == null){
            MainApp.showWarningMessageDialog(Lang.getWorkflowWarningNoCase(String.valueOf(pProcess.getWorkflowNumber())));
            
        }else{
//        Objects.requireNonNull(pProcess.getMainCase(), "Can not detect if there is a 6 Week deadline, Process has no MainCase set!");
            boolean hasPossiblesDeadline = processServiceBean.get().hasPossibleKain6WeekDeadline(pProcess.getMainCase().getId());
            if (hasPossiblesDeadline) {
                checkBillFor6WeeksDeadline(pProcess.getMainCase());
            }
        }
    }

    public void checkKain6WeeksDeadline() {
        ProcessServiceFacade.this.checkKain6WeeksDeadline(getCurrentProcess());
    }

    public MdkAuditQuotaResult getAuditQuotaResult(@NotNull final TCase pCase, @NotNull final TWmRequestMdk pMDRequest) {
        Objects.requireNonNull(pCase, "cannot check audit quota, because case is null");
        Objects.requireNonNull(pMDRequest, "cannot check audit quota, because MDRequest is null");
        final String hospitalIdent = pCase.getCsHospitalIdent();
        final String insuranceIdent = pCase.getInsuranceIdentifier();
        final Integer billYear = pMDRequest.getCsBillingYearForMdAudit();
        final Integer billQuarter = pMDRequest.getCsBillingQuarterForMdAudit();
        final Integer mdRequestYear = pMDRequest.getAuditYear();
        final Integer mdRequestQuarter = pMDRequest.getAuditQuarter();
        final boolean isAdmissionReasonInPatient = pCase.isCurrentCaseExternInPatient();
        if (insuranceIdent == null) {
            LOG.log(Level.WARNING, "cannot check audit quota, because insurance identifier for case {0} does not exist", pCase);
            return null;
        }
        if (billYear == null || billQuarter == null) {
            LOG.log(Level.WARNING, "cannot check audit quota, because billing date for case {0} does not exist", pCase);
            return null;
        }
        if (mdRequestYear == null || mdRequestQuarter == null) {
            LOG.log(Level.WARNING, "cannot check audit quota, because audit start date for request {0} does not exist", pMDRequest);
            return null;
        }
        if (!isAdmissionReasonInPatient) {
            LOG.log(Level.WARNING, "don''t check audit quota, because case is not in-patient", pCase);
            return null;
        }

        if (!pCase.isCaseBillRelevantForQuote()) {
            LOG.log(Level.WARNING, "don''t check audit quota, because billing date is ealier than 2020", pCase);
            return null;
        }
        LOG.log(Level.INFO, "will check audit complaints with these params: hospital identifier = {0}, insurance identifier = {1}, year = {2}, quarter = {3}", new Object[]{hospitalIdent, insuranceIdent, mdRequestYear, mdRequestQuarter});
        final List<MdkAuditComplaintsDTO> auditComplaintsList = processServiceBean.get().getMdkAuditComplaints(hospitalIdent, insuranceIdent, billYear, billQuarter, mdRequestYear, mdRequestQuarter);
        if (auditComplaintsList.isEmpty()) {
            LOG.log(Level.WARNING, "no audit complaints result for case {0}", new Object[]{pCase});
            return null;
        }
        if (auditComplaintsList.size() > 1) {
            LOG.log(Level.WARNING, "audit complaints result for case {0} is ambigious, {1} entries found (will pick first one)", new Object[]{pCase, auditComplaintsList.size()});
        }
        final MdkAuditComplaintsDTO auditComplaints = auditComplaintsList.iterator().next();

        final CMdkAuditquota givenAuditQuota = processServiceBean.get().getMdkAuditQuota(hospitalIdent, mdRequestYear, mdRequestQuarter);
        if (givenAuditQuota == null) {
            LOG.log(Level.WARNING, "no given audit quota found for hospital ident {0}, year = {1} and quarter = {2}", new Object[]{hospitalIdent, mdRequestYear, mdRequestQuarter});
            return null;
        }

        final long givenAuditCaseCount = givenAuditQuota.getGivenComplaints(auditComplaints.getCaseCount());

        return new MdkAuditQuotaResult(
                auditComplaints.getHospitalIdent(),
                auditComplaints.getInsuranceIdent(),
                auditComplaints.getYear(),
                auditComplaints.getQuarter(),
                auditComplaints.getCaseCount(),
                auditComplaints.getComplaintCount(),
                givenAuditQuota.getMdkAqQuota(),
                givenAuditCaseCount
        );
    }

    public List<TWmRisk> getAllRisks() {

        final TCase mainCase = getCurrentMainCase();
        if (mainCase == null) {
            LOG.severe("can get all risks, main case is null!");
            return new ArrayList<>();
        }
        return riskServiceBean.get().findAllRisks(mainCase.getId());
    }

    public TWmRisk getCompletionRisk() {
        final TCase mainCase = getCurrentMainCase();
        if (mainCase == null) {
            LOG.severe("can not get completion risk, main case is null!");
            return null;
        }
        return riskServiceBean.get().getCompletionRisk(mainCase.getId());
    }

    public TWmRisk getRequestRisk(TWmRequest pRequest) {
        if (pRequest == null) {
            return null;
        }
        final TCase mainCase = getCurrentMainCase();
        if (mainCase == null) {
            //use main case here? could there be another case??
            return null;
        }
        return riskServiceBean.get().getRequestRisk(mainCase.getId(), pRequest.getId());
    }

    public TWmRisk createOrGetRequestRisk(TWmRequest pRequest) {
        TWmRisk dbRisk = getRequestRisk(pRequest);
        return dbRisk == null ? createNewRisk(pRequest) : dbRisk;
    }

    public TWmRisk saveOrUpdateRisk(TWmRisk pRisk) {
        return riskServiceBean.get().saveOrUpdateRisk(pRisk);
    }
    public TWmFinalisationRisk saveOrUpdateFinalisationRisk(TWmFinalisationRisk pRisk) {
        return riskServiceBean.get().saveOrUpdateFinalisationRisk(pRisk);
    }
    public void createOrUpdateRiskData(TWmRequest pRequest) {
        if (pRequest == null) {
            return;
        }
        TWmRisk risk = getRequestRisk(pRequest);
        risk = risk == null ? createNewRisk(pRequest) : risk;
        updateRiskDetails(risk, pRequest);
        saveOrUpdateRisk(risk);
    }

    public TWmRisk createNewRequestRisk(TWmRequest pRequest) {
        if (pRequest == null) {
            return null;
        }
        if (getCurrentMainCase() == null) {
            return null;
        }
        TWmRisk risk = new TWmRisk();
        risk.setRiskPercentTotal(0);
        risk.setRiskPlaceOfReg(PlaceOfRegEn.REQUEST);
        risk.setRiskValueTotal(BigDecimal.ZERO);
        risk.setRequest(pRequest);
        updateRiskDetails(risk, pRequest);
        return risk;
    }

    private TWmRisk createNewRisk(TWmRequest pRequest) {
        if (pRequest == null) {
            return null;
        }
        if (getCurrentMainCase() == null) {
            return null;
        }
        TWmRisk risk = new TWmRisk();
        risk.setRiskPercentTotal(0);
        risk.setRiskPlaceOfReg(PlaceOfRegEn.REQUEST);
        risk.setRiskValueTotal(BigDecimal.ZERO);
        risk.setRequest(pRequest);
        return risk;
    }

    private void updateRiskDetails(TWmRisk pRisk, TWmRequest pRequest) {
        for (TWmMdkAuditReasons auditReason : pRequest.getAuditReasons()) {
            CMdkAuditreason commonReason = MenuCache.getMenuCacheAuditReasons().get(auditReason.getAuditReasonNumber());
            Boolean hasRisk = hasRiskArea(pRisk, commonReason.getMdkArRiskArea());
            if (hasRisk == null) {
                //ignore if somehow null value
                continue;
            }
            if (!hasRisk) {
                TWmRiskDetails detail = new TWmRiskDetails();
                detail.setRisk(pRisk);
                detail.setRiskArea(commonReason.getMdkArRiskArea());
                detail.setRiskPercent(0);
                detail.setRiskValue(BigDecimal.ZERO);
                pRisk.getRiskDetails().add(detail);
            }
        }
    }

    public Boolean hasRiskArea(TWmRisk pRisk, RiskAreaEn pArea) {
        if (pRisk == null) {
            return null;
        }
        if (pArea == null) {
            return null;
        }
        for (TWmRiskDetails detail : pRisk.getRiskDetails()) {
            if (pArea.equals(detail.getRiskArea())) {
                return true;
            }
        }
        return false;
    }

    public TWmRisk getActualBilingRisk(){
        return getActualRiskForVersionRiskType(VersionRiskTypeEn.BEFORE_BILLING, PlaceOfRegEn.BEFORE_BILLING);
    }
    public TWmRisk getActualRiskForVersionRiskType(VersionRiskTypeEn pType,PlaceOfRegEn pReg) {
        TCase hCase = getCurrentMainCase();
        if(hCase == null){
            LOG.warning("Can not get risk! MainCase for Process is not set!");
            return null;
        }
        return riskServiceBean.get().findActualRisk(hCase.getId(),pReg,pType);
    }
    public List<TWmRisk> getActualRequestRisks() {
        TCase hCase = getCurrentMainCase();
        if(hCase == null){
            LOG.warning("Can not get risk! MainCase for Process is not set!");
            return new ArrayList<>();
        }
        return riskServiceBean.get().findActualRequestRisks(hCase.getId(),PlaceOfRegEn.REQUEST);
    }

    public TWmFinalisationRisk getCompletionRiskForProcessFinalisation() {
        TWmProcessHospitalFinalisation finalistion = getCurrentProcessFinalisation();
        if(finalistion == null){
            return null;
        }
        return riskServiceBean.get().findFinalisationRisk(finalistion.getId());
    }
    public TCaseDetails getCurrentBillingVersion(){
        TCase mainCase = getCurrentMainCase();
        if(mainCase == null){
            return null;
        }
        return caseServiceBean.get().findBilingVersionForCase(mainCase.id);
    }
    public TCaseDetails getCurrentAssessmentVersion(){
        TCase mainCase = getCurrentMainCase();
        if(mainCase == null){
            return null;
        }
        return caseServiceBean.get().findAssessmentVersionForCase(mainCase.id);
    }
    
    public List<TCaseDetails> getAssessmentVersions(){
        return getAssessmentVersions(getCurrentProcess().getMainCase());
    }
    public List<TCaseDetails> getBillingVersions(){
        return getBillingVersions(getCurrentProcess().getMainCase());
    }
    public List<TCaseDetails> getAssessmentVersions(TCase pCase) {
        return caseServiceBean.get().getAssessmentVersions(pCase.id);
    }
    public List<TCaseDetails> getBillingVersions(TCase pCase) {
        return caseServiceBean.get().getBillingVersions(pCase.id);
    }

    public List<CpxBaserate> findDrgCareBaserates4HosIdent(String pHostIdent) {
        return CpxBaserateCatalog.instance().findDrgCareBaserates4HosIdent(pHostIdent,  AbstractCpxCatalog.DEFAULT_COUNTRY);
    }

    public boolean isProcessPaused() {
        if(getCurrentProcess() == null){
            return false;
        }
        return WmWorkflowStateEn.paused.equals(getCurrentProcess().getWorkflowState());
    }
    
    public boolean setWorkFlowState(WmWorkflowStateEn pState){
        pState = Objects.requireNonNullElse(pState, WmWorkflowStateEn.offen);
        if(getCurrentProcess()==null){
            return false;
        }
        WmEventTypeEn eventType = getEventTypeForWorkFlowState(pState);
        if(eventType == null){
            LOG.warning("No proper EventType for WorkflowState: " + pState.getTranslation().getValue() + " found!");
            return false;
        }
        getCurrentProcess().setWorkflowState(pState);
        createAndStoreEvent(eventType, null, "");
        updateProcess(getCurrentProcess());
        return true;
//        getCurrentProcess().setWorkflowState(pState);
//        saveOrUpdateProcess(getCurrentProcess());
    }
    public WmEventTypeEn getEventTypeForWorkFlowState(WmWorkflowStateEn pState){
        if(pState == null){
            return null;
        }
        switch(pState){
            case offen:
                return WmEventTypeEn.processContinued;
            case paused:
                return WmEventTypeEn.processPaused;
            default:
                //TODO: handle closed here???
                return null;
        }
    }
    public boolean continueProcess() {
        if(getCurrentProcess() == null){
            return false;
        }
        if(!WmWorkflowStateEn.paused.equals(getCurrentProcess().getWorkflowState())){
            LOG.log(Level.INFO, "Workflow State for ProcessId: {0} is not paused! No need to contiune!", getCurrentProcess().getId());
            return false;
        }
        return setWorkFlowState(WmWorkflowStateEn.offen);
    }

    public boolean pauseProcess() {
        if(getCurrentProcess() == null){
            return false;
        }
        if(WmWorkflowStateEn.paused.equals(getCurrentProcess().getWorkflowState())){
            LOG.log(Level.INFO, "Workflow State for ProcessId: {0} is already paused! No need to pause again!", getCurrentProcess().getId());
            return false;
        }
        return setWorkFlowState(WmWorkflowStateEn.paused);
    }
    
    public TCaseDetails getCurrentKisDetailsVersion(){
        TWmProcess process = getCurrentProcess();
        if(process == null){
            return null;
        }
        return processServiceBean.get().findCurrentKisDetailsVersionForProcess(process.id);
    }

    public void saveOrUpdateCaseVersion(TCaseDetails finalVersion) {
        caseServiceBean.get().saveOrUpdateCaseVersion(finalVersion);
    }

    public boolean isProcessClosed() {
        if(getCurrentProcess() == null){
            return true;
        }
        return WmWorkflowStateEn.geschlossen.equals(getCurrentProcess().getWorkflowState());
    }
    private <T> void clearObsList(ObservableList<T> pList){
        if(pList == null){
            return;
        }
        pList.clear();
        pList = null;
    }
    public void dispose() {
        clearObsList(obsActionList);
        clearObsList(obsDocumentList);
        clearObsList(obsEventList);
        clearObsList(obsInkaMsgList);
        clearObsList(obsProcessCaseList);
        clearObsList(obsReminderList);
        clearObsList(obsRequestList);
    }

    public int getOpenRemindersCount() {
        return processServiceBean.get().getOpenReminderCount(this.getCurrentProcess().getId());
    }



    public boolean checkAcgConnection() {
        try{
            // We want to check the current URL
            HttpURLConnection.setFollowRedirects(false);
           
            URL u = new URL("http://" + CpxClientConfig.instance().getServerHost() + ":8085/acg-web-app/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) u.openConnection();

            // We don't need to get data
            httpURLConnection.setRequestMethod("HEAD");
            int responseCode = httpURLConnection.getResponseCode();

           // We check 404 only
           return responseCode != HttpURLConnection.HTTP_NOT_FOUND;    
        } catch(Exception ex){
            LOG.log(Level.SEVERE, "cound not connect with acg", ex);
            return false;
        
        }
    }

    public boolean isCaseNumber4PatientValid(String pCaseNumber) {
       if(pCaseNumber == null || pCaseNumber.trim().isEmpty()){
           return false;
       }
       Set <TCase> cases = getPatient().getCases();
       for(TCase cs: cases){
            if( cs.getCsCaseNumber().startsWith(pCaseNumber)
                    && cs.getCsCaseNumber().length() > pCaseNumber.length()) { 
                return false;
            }
           if(cs.getCsCaseNumber().equals(pCaseNumber)){
               return false;
           }
       }
       return true;
    }

    public void sendGdvResponse(TWmProcess currentProcess, List<TWmDocument> sendItems) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<TWmDocument> checkSentDocuments(List<TWmDocument> sendItems) {
        return null;
    }

    
    
}
