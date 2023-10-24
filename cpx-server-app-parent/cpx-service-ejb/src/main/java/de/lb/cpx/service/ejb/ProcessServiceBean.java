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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoRuntimeException;
import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.exceptions.CpxSapException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TP301Inka;
import de.lb.cpx.model.TP301KainInka;
import de.lb.cpx.model.TP301KainInkaPvt;
import de.lb.cpx.model.TP301KainInkaPvv;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.TSapFiBill;
import de.lb.cpx.model.TSapFiBillposition;
import de.lb.cpx.model.TSapFiOpenItems;
import de.lb.cpx.model.enums.SapReferenceTypeEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.sap.container.FallContainer;
import de.lb.cpx.sap.dto.SapExportResult;
import de.lb.cpx.sap.dto.SapFiFactura;
import de.lb.cpx.sap.dto.SapFiOpenItem;
import de.lb.cpx.sap.dto.SapFiPosition;
import de.lb.cpx.sap.importer.ImportProcessSap;
import de.lb.cpx.sap.importer.Sap;
import de.lb.cpx.sap.importer.SapConnection;
import static de.lb.cpx.sap.importer.SapConnection.MAP_KEY_FI_CASE_STATUS;
import static de.lb.cpx.sap.importer.SapConnection.MAP_KEY_FI_OPEN_ITEM_STATUS;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.auth.CpxUser;
import de.lb.cpx.server.commonDB.dao.CCaseDao;
import de.lb.cpx.server.commonDB.dao.CMdkAuditquotaDao;
import de.lb.cpx.server.commonDB.dao.CWmListMdkStateDao;
import de.lb.cpx.server.commonDB.dao.CdbUsersDao;
import de.lb.cpx.server.commonDB.model.CCase;
import de.lb.cpx.server.commonDB.model.CMdkAuditquota;
import de.lb.cpx.server.commonDB.model.CWmListMdkState;
import de.lb.cpx.server.config.CpxServerConfig;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.server.dao.TCaseIcdDao;
import de.lb.cpx.server.dao.TCaseOpsDao;
import de.lb.cpx.server.dao.TGroupingResultsDao;
import de.lb.cpx.server.dao.TP301InkaDao;
import de.lb.cpx.server.dao.TP301KainInkaDao;
import de.lb.cpx.server.dao.TP301KainInkaPvtDao;
import de.lb.cpx.server.dao.TSapFiBillDao;
import de.lb.cpx.server.dao.TSapFiOpenItemsDao;
import de.lb.cpx.server.wm.dao.TWmActionDao;
import de.lb.cpx.server.wm.dao.TWmDocumentDao;
import de.lb.cpx.server.wm.dao.TWmEventDao;
import de.lb.cpx.server.wm.dao.TWmProcessCaseDao;
import de.lb.cpx.server.wm.dao.TWmProcessDao;
import de.lb.cpx.server.wm.dao.TWmProcessHospitalDao;
import de.lb.cpx.server.wm.dao.TWmProcessHospitalFinalisationDao;
import de.lb.cpx.server.wm.dao.TWmReminderDao;
import de.lb.cpx.server.wm.dao.TWmRequestDao;
import de.lb.cpx.server.wm.dao.TWmRiskDao;
import static de.lb.cpx.service.ejb.CpxAuthorizationChecker.*;
import de.lb.cpx.service.jms.producer.StatusBroadcastProducer;
import de.lb.cpx.service.properties.CpxAuthorizationException;
import de.lb.cpx.service.startup_ejb.LicenseReadBean;
import de.lb.cpx.shared.dto.MdkAuditComplaintsDTO;
import de.lb.cpx.shared.dto.broadcast.BroadcastOriginEn;
import de.lb.cpx.shared.dto.job.config.CpxJobConstraints;
import de.lb.cpx.shared.dto.job.config.other.SapJob;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
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
import de.lb.cpx.wm.model.TWmRequestMdk;
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.enums.WmEventTypeEn;
import distributor.impl.CpxDistributor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import module.impl.ImportConfig;
import org.apache.commons.io.IOUtils;
import org.hibernate.Hibernate;
import org.jboss.ejb3.annotation.TransactionTimeout;

/**
 * Bean to access process related services e.g. storing and loading process
 * related entities
 *
 * @author wilde
 */
@Stateless
public class ProcessServiceBean implements ProcessServiceBeanRemote {

    private static final Logger LOG = Logger.getLogger(ProcessServiceBean.class.getName());
    @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;
    @Inject
    private TWmDocumentDao documentDao;
    @Inject
    private TWmEventDao eventDao;
    @Inject
    private TWmProcessCaseDao processCaseDao;
    @Inject
    private TWmReminderDao reminderDao;
    @Inject
    private TWmRequestDao requestDao;
    @Inject
    private TWmActionDao actionDao;
    @Inject
    private TP301KainInkaDao kainInkaDao;
    @Inject
    private TP301InkaDao inkaDao;
//    @EJB
//    private TP301KainInkaPvvDao kainInkaPvvDao;
//    @EJB
//    private TP301KainInkaPvtDao kainInkaPvtDao;
    @Inject
    private TWmProcessDao processDao;
    @Inject
    private TWmRiskDao riskDao;
    @Inject
    private CMdkAuditquotaDao mdkAuditquotaDao;
    @Inject
    private TWmProcessHospitalDao processHospitalDao;
    @Inject
    private TCaseIcdDao icdDao;
    @Inject
    private TCaseOpsDao opsDao;
    @Inject
    private TGroupingResultsDao groupingResultsDao;
//    @EJB
//    private TWmProcessCaseDao TWmProcessCaseDao;
    @Inject
    private TCaseDao caseDao;
    @Inject
    private TWmProcessHospitalFinalisationDao processResultDao;
//    @EJB
//    private CWmListProcessTopicDao processTopicsDao;
    @Inject
    private CdbUsersDao cdbUsersDao;
    @Inject
    private CWmListMdkStateDao cwmListMdkStateDao;
    @Inject
    private TP301KainInkaPvtDao tp301KainInkaPvtDao;
    @Inject
    private TSapFiBillDao sapFiBillDao;
//    @EJB
//    private TSapFiBillpositionDao sapFiBillpositionDao;
    @Inject
    private TSapFiOpenItemsDao sapFiOpenItemsDao;
    @Inject
    private LicenseReadBean licenseBean;
    @Inject
    private CCaseDao commonCaseDao;
    @Inject
    private StatusBroadcastProducer<long[]> broadcast;
    @Inject
    private StatusBroadcastProducer<Long> broadcast2;
//    @EJB
//    private CDepartmentDao departmentDao;
//    @EJB
//    private TCaseDepartmentDao tCaseDepartmentDao;

    @Override
    public void deleteProcess(final long pProcessId) throws CpxIllegalArgumentException, CpxAuthorizationException {
        checkDeleteProcess();
        final TWmProcess proc = processDao.findById(pProcessId);
        if (proc == null) {
            final String message = "Process with id " + pProcessId + " does not exist. Maybe this process was already deleted by another user. Deletion aborted!";
            LOG.log(Level.INFO, message);
            //throw new CpxIllegalArgumentException(message);
            throw new CpxIllegalArgumentException(Lang.getProcessDoesNotExistWithReason(String.valueOf(pProcessId)));
        }
        final CpxUser user = ClientManager.getCurrentCpxUser();
        final String database = ClientManager.getActualDatabase();
        LOG.log(Level.INFO, "Will remove process: {0} (initiated by {1} on database {2})...", new Object[]{proc.getWorkflowSignature(), String.valueOf(user), database});
        for (TWmDocument document : proc.getDocuments()) {
            try {
                removeDocument(document);
            } catch (final RuntimeException | IOException ex) {
                throw new CpxIllegalArgumentException("Was not able to delete document " + String.valueOf(document) + " of process " + proc.getWorkflowSignature(), ex);
            }
        }
        if (proc.isProcessHospital()) {
            final TWmProcessHospital processHospital = (TWmProcessHospital) proc;
            //TODO:Maybe delete completion risk here?
//            final TWmProcessHospitalFinalisation finalisation = processHospital.getProcessHospitalFinalisation();
//            if (finalisation != null) {
//                for (TWmRisk risk : finalisation.getRisks()) {
//                    try {
//                        //req.setProcessHospital(null);
//                        riskDao.remove(risk);
//                    } catch (final RuntimeException ex) {
//                        throw new CpxIllegalArgumentException("Was not able to delete risk with id " + risk.id + " of process " + proc.getWorkflowSignature() + " and process hospital finalisation with id " + finalisation.id, ex);
//                    }
//                }
//            }
            for (TWmRequest req : processHospital.getRequests()) {
                for (TWmRisk risk : req.getRisks()) {
                    try {
                        //req.setProcessHospital(null);
                        riskDao.remove(risk);
                    } catch (final RuntimeException ex) {
                        throw new CpxIllegalArgumentException("Was not able to delete risk with id " + risk.id + " of process " + proc.getWorkflowSignature() + " and request with id " + req.id, ex);
                    }
                }
                try {
                    //req.setProcessHospital(null);
                    requestDao.remove(req);
                } catch (final RuntimeException ex) {
                    throw new CpxIllegalArgumentException("Was not able to delete request with id " + req.id + " of process " + proc.getWorkflowSignature(), ex);
                }
            }
        }
        try {
            //for(TWmRequest req: proc.getMainProcessCase())
            processDao.remove(proc);
        } catch (final RuntimeException ex) {
            throw new CpxIllegalArgumentException("Was not able to delete process " + proc.getWorkflowSignature(), ex);
        }
        broadcast2.send(BroadcastOriginEn.DELETE_PROCESS, "Der Vorgang " + proc.getWorkflowNumber() + " wurde gelöscht", null, pProcessId);
        LOG.log(Level.INFO, "Process {0} was successfully removed by user {1} on database {2}!", new Object[]{proc.getWorkflowSignature(), String.valueOf(user), database});
    }

    @Override
    public void cancelProcess(final long pProcessId) throws CpxIllegalArgumentException {
        checkCancelCase();
        final TWmProcess process = processDao.findById(pProcessId);
        if (process == null) {
            final String message = "Workflow with id " + pProcessId + " does not exist. Maybe this workflow was already deleted by another user. Deletion aborted!";
            LOG.log(Level.INFO, message);
            throw new CpxIllegalArgumentException(Lang.getProcessDoesNotExistWithReason(String.valueOf(pProcessId)));
        }
        final CpxUser user = ClientManager.getCurrentCpxUser();
        final String database = ClientManager.getActualDatabase();
        LOG.log(Level.INFO, "Will cancel workflow: {0} (initiated by {1} on database {2})...", new Object[]{pProcessId, String.valueOf(user), database});
        processDao.cancelProcess(process);

        LOG.log(Level.INFO, "workflow {0} was successfully canceled by user {1} on database {2}!", new Object[]{process.getWorkflowSignature(), String.valueOf(user), database});
    }

    @Override
    public void unCancelProcess(final long pProcessId) throws CpxIllegalArgumentException {
        checkCancelCase();
        final TWmProcess process = processDao.findById(pProcessId);
        if (process == null) {
            final String message = "Hospital case with id " + pProcessId + " does not exist. Maybe this case was already deleted by another user. Deletion aborted!";
            LOG.log(Level.INFO, message);
            throw new CpxIllegalArgumentException(Lang.getProcessDoesNotExistWithReason(String.valueOf(pProcessId)));
        }

        final CpxUser user = ClientManager.getCurrentCpxUser();
        final String database = ClientManager.getActualDatabase();
        LOG.log(Level.INFO, "Will cancel hospital case: {0} (initiated by {1} on database {2})...", new Object[]{process.getWorkflowSignature(), String.valueOf(user), database});
        processDao.unCancelProcess(process);
        LOG.log(Level.INFO, "Hospital case {0} was successfully canceled by user {1} on database {2}!", new Object[]{process.getWorkflowSignature(), String.valueOf(user), database});
    }

//    @Override
//    public TWmDocument findDocument(long documentId) {
//        TWmDocument doc = documentDao.findById(documentId);
//        doc.setContent(null);
//        return doc;
//    }
//    @Override
//    public byte[] getDocumentContent(long documentId) {
//        return documentDao.getContent(documentId);
//    }
//
//    @Override
//    public void removeDocument(TWmDocument item) {
//        item = documentDao.attach(item);
////        eventDao.removeDocumentEvent(item);
//        documentDao.remove(item);
//    }
//    @Override
//    public void removeDocumentFromFS(TWmDocument item, String serverRootFolder) {
//        item = documentDao.attach(item);
////        eventDao.removeDocumentEvent(item);
//        documentDao.remove(item);
//
//        // delete document from the server's file system (if any)
//        File itemFile;
//        if (serverRootFolder != null && !serverRootFolder.isEmpty() && item.getFilePath() != null) {
//            itemFile = new File(serverRootFolder + "\\" + item.getFilePath());
//        } else {
//            String cpxHome = CpxSystemProperties.getInstance().getCpxHome();
//            itemFile = new File(cpxHome + "documents\\" + item.getFilePath());
//        }
//        if (itemFile.exists()) {
//            itemFile.delete();
//        }
//    }
    @Override
    public TWmEvent storeEvent(TWmEvent event) {
        if (event == null) {
            LOG.log(Level.WARNING, "event is null!");
            return event;
        }
        //RoleProperties prop = ClientManager.getCurrentCpxUser().getRoleProperties(ClientManager.getCurrentCpxRoleId());
        if (event.getDocument() != null) {
            event.getDocument().setProcess(event.getProcess());
            TWmDocument doc = storeDocument(event.getDocument());
            event.setDocument(doc);
        }
        if (event.getAction() != null) {
            event.getAction().setProcess(event.getProcess());
            TWmAction act = storeAction(event.getAction());
            event.setAction(act);
        }
        if (event.getReminder() != null) {
            event.getReminder().setProcess(event.getProcess());
            TWmReminder reminder = storeReminder(event.getReminder());
            event.setReminder(reminder);
        }
        if (event.getRequest() != null) {
            if (event.getProcess() instanceof TWmProcessHospital) {
                event.getRequest().setProcessHospital((TWmProcessHospital) event.getProcess());
                TWmRequest request = storeRequest(event.getRequest());
                event.setRequest(request);

                if (cpxServerConfig.getSapSendMDStateToSAP()) {
                    if (request != null && request instanceof TWmRequestMdk) {
                        sendMDStateToSAP(request);
                    }
                }
            } else {
                LOG.log(Level.WARNING, "Could not store request, processType is not TWmProcessHospital! Instead {0}", event.getProcess().getClass());
                return null;
            }
        }
//        if(event.getHosCase()!= null){
//            TWmProcessCase processCase = storeCaseInProcessCase(event.getHosCase());
//            event.setHosCase(processCase.getHosCase());
//        }
        if (event.getKainInka() != null && event.getEventType().equals(WmEventTypeEn.inkaStored)) {
            if (event.getProcess() instanceof TWmProcessHospital) {
//                event.getKainInka().setProcessHospital((TWmProcessHospital) event.getProcess());
                event.getKainInka().setTCaseId(event.getProcess().getMainCase());
                TP301KainInka kainInka = storeKainInka(event.getKainInka());
                event.setKainInka(kainInka);

            } else {
                LOG.log(Level.WARNING, "Could not store KainInka message, processType is not TWmProcessHospital! Instead {0}", event.getProcess().getClass());
                return null;
            }
        }
        if (event.getKainInka() != null && event.getEventType().equals(WmEventTypeEn.inkaUpdated)) {
            if (event.getProcess() instanceof TWmProcessHospital) {
                event.getKainInka().setTCaseId(event.getProcess().getMainCase());
                TP301KainInka kainInka = updateKainInka(event.getKainInka());
                event.setKainInka(kainInka);

            } else {
                LOG.log(Level.WARNING, "Could not update KainInka message, processType is not TWmProcessHospital! Instead {0}", event.getProcess().getClass());
                return null;
            }
        }
        if (event.getKainInka() != null && event.getEventType().equals(WmEventTypeEn.inkaSent)) {
            if (event.getProcess() instanceof TWmProcessHospital) {
                event.getKainInka().setTCaseId(event.getProcess().getMainCase());
                TP301KainInka kainInka = sendInkaMessage(event.getKainInka());
                event.setKainInka(kainInka);
            } else {
                LOG.log(Level.WARNING, "Could not send Inka message, processType is not TWmProcessHospital! Instead {0}", event.getProcess().getClass());
                return null;
            }
        }
        if (event.getKainInka() != null && event.getEventType().equals(WmEventTypeEn.inkaCancelled)) {
            if (event.getProcess() instanceof TWmProcessHospital) {
                event.getKainInka().setTCaseId(event.getProcess().getMainCase());
                TP301KainInka cancelInkaMessage = cancelInkaMessage(event.getKainInka());
                event.setKainInka(cancelInkaMessage);
//                event.setKainInka(event.getKainInka());
            } else {
                LOG.log(Level.WARNING, "Could not cancel an Inka message, processType is not TWmProcessHospital! Instead {0}", event.getProcess().getClass());
                return null;
            }
        }

        eventDao.persist(event);
        return event;
    }

    @Override
    public TWmEvent updateEvent(TWmEvent event) {
        //check delete Event for Action/Reminder/Document 
        if (event.isActionEvent() && event.getAction() == null) {
            checkEditAction();
        }
        if (event.isReminderEvent() && event.getReminder() == null) {
            checkEditReminder();
            checkEditReminderOfOtherUser(Objects.requireNonNullElse(event.getCreationUser(), 0L));
        }
        if (event.isDocumentEvent() && event.getDocument() == null) {
            checkEditDocument();
            checkEditDocumentOfOtherUser(event.getCreationUser());
        }
        if (event.isRequestEvent() && event.getRequest() == null) {
            checkEditRequestOfOtherUser(event.getCreationUser());
        }
        return eventDao.merge(event);
    }

    @Override
    public boolean existsInProcess(String fileName, long processId) {
        return documentDao.docExists(fileName, processId);
    }

    @Override
    public void removeProcessCase(TWmProcessCase processCase) {
        processCase = processCaseDao.attach(processCase);
        processCaseDao.remove(processCase);
    }

    @Override
    public TWmProcessCase storeProcessCase(TWmProcessCase processCase, long pProcessId) {
        TWmProcess process = processDao.findById(pProcessId);
        processCase.setProcess(process);
        if (processCase.id > 0) {
            processCaseDao.merge(processCase);
        } else {
            processCaseDao.persist(processCase);
        }
        return processCase;
    }

    /*
    @Override
    public TWmRequest storeRequest(TWmRequest request) {
        requestDao.persist(request);
        return request;
    }
     */
    @Override
    public TWmRequest storeRequest(TWmRequest request) {
        if (request == null) {
            return request;
        }
        if (request.getId() != 0L) {
            checkEditRequestOfOtherUser(request.getCreationUser());
            request = requestDao.merge(request);
        } else {
            requestDao.persist(request);
        }

        return request;
    }

    /*
        @Override
    public void removeRequest(TWmRequest request) {
        checkEditRequestOfOtherUser(request.getCreationUser());
        request = requestDao.attach(request);
        requestDao.remove(request);
    }
     */
    @Override
    public void removeRequest(TWmRequest request) {
        checkEditRequestOfOtherUser(request.getCreationUser());
        request = requestDao.attach(request);
        request.setProcessHospital(null);
        eventDao.removeRequestEvent(request);
        requestDao.remove(request);
    }

//    @Override
//    public TP301KainInka storeKainInka(TP301KainInka kainInka) {
//        if (kainInka == null) {
//            return kainInka;
//        }
//        if (kainInka.getId() != 0L) {
//            //merge kainInka object
//            for (int i = 0; i < kainInka.getKainInkaPvvs().size(); i++) {
//                TP301KainInkaPvv pvv = kainInka.getKainInkaPvvs().get(i);
//                if (pvv.getId() <= 0) {
//                    pvv.setP301KainInkaId(kainInka);
//                }
//            }
//            kainInkaDao.merge(kainInka);
//        } else {
//            process.attach(kainInka.getTCaseId());
////       kainInka = kainInkaDao.attach(kainInka);
//// persist works only with transient objects.If persist concludes the object is detached (which it will because the ID is set), it will return the "detached object passed to persist" error.
//            kainInkaDao.persist(kainInka); // makes a transient instance persistent. it will not execute an INSERT statement if it is called outside of transaction boundaries.
//        }
//        return kainInka;
//    }
    @Override
    public TP301KainInka storeKainInka(TP301KainInka kainInka) {
        caseDao.attach(kainInka.getTCaseId());
//       kainInka = kainInkaDao.attach(kainInka);
// persist works only with transient objects.If persist concludes the object is detached (which it will because the ID is set), it will return the "detached object passed to persist" error.
        kainInkaDao.persist(kainInka); // makes a transient instance persistent. it will not execute an INSERT statement if it is called outside of transaction boundaries.
        return kainInka;
    }

    @Override
    public TP301KainInka updateKainInka(TP301KainInka kainInka) {
//        Session hibernateSession = HibernateUtil.getSessionFactory().getCurrentSession();
//        TP301KainInka updateKainInka = (TP301KainInka) hibernateSession.merge(kainInka);

        if (kainInka != null) {

//            TP301KainInka updateKainInka = (TP301KainInka) kainInkaDao.merge(kainInka);
            for (int i = 0; i < kainInka.getKainInkaPvvs().size(); i++) {
                TP301KainInkaPvv pvv = kainInka.getKainInkaPvvs().get(i);
                if (pvv.getId() <= 0) {
                    pvv.setP301KainInkaId(kainInka);
//                    kainInkaPvvDao.persist(pvv);
                }
//                if (!kainInkaPvvDao.exists(pvv)) {
//                     kainInkaPvvDao.persist(pvv);
//                }

//                TP301KainInkaPvv updatePvv = (TP301KainInkaPvv) kainInkaPvvDao.merge(pvv);
//                Session session = kainInkaPvvDao.getSession();
//                session.merge(pvv);
//                for (int j = 0; j < pvv.getKainInkaPvts().size(); j++) {
//                    TP301KainInkaPvt pvt = pvv.getKainInkaPvts().get(j);
//                    if (pvt.getId() <= 0) {
////                        pvt.setP301KainInkaPvvId(pvv);
//                        kainInkaPvtDao.persist(pvt);
//                    }
////                    TP301KainInkaPvt updatePvt = (TP301KainInkaPvt) kainInkaPvtDao.merge(pvt);
//                    kainInkaPvtDao.getSession().merge(pvt);
//
//                }
            }

            // merge kainInka object
            kainInka = kainInkaDao.merge(kainInka);

        }

        return kainInka;

// If the appropriate parent row already exists in the database, then it will just insert the child row and assign the appropriate foreign key to the child row.
//       kainInka = kainInkaDao.attach(kainInka);
//        kainInkaDao.detach(kainInka);
//        TP301KainInka findById = kainInkaDao.findById(kainInka.id);
//        kainInkaDao.detach(findById);
//        kainInkaDao.persist(kainInka);
//        kainInkaDao.getSession().flush();
//        return kainInka;
        //TP301KainInka mergedEntity = kainInkaDao.merge(kainInka);
        //kainInkaDao.attach(kainInka);
        //kainInkaDao.update(kainInka);
        //TP301KainInka mergedEntity = kainInkaDao.merge(kainInka);
        //kainInkaDao.update(kainInka);
        //kainInkaPvtDao.attach(kainInka.getKainInkaPvvs().iterator().next().getKainInkaPvts().iterator().next());
        //kainInkaDao.changeFlushMode(FlushMode.MANUAL);
        //kainInka = kainInkaDao.attach(kainInka);
//        kainInka = kainInkaDao.merge(kainInka);
//kainInkaDao.flush();
        //TP301KainInka findById = kainInkaDao.findById(kainInka.id);
        //findById.setKainInkaPvvs(kainInka.getKainInkaPvvs());
        //kainInka = kainInkaDao.saveOrUpdate(kainInka);
        //kainInkaDao.flush();
        //kainInkaDao.persist(kainInka);
//        TP301KainInka attachedEntity = kainInkaDao.attach(kainInka);
//        //kainInkaDao.attach(kainInka);
//        kainInkaDao.persist(attachedEntity);
        //kainInkaDao.saveOrUpdate(kainInka);
//        if (kainInkaDao.isDetached(kainInka)) {
//            kainInkaDao.attach(kainInka);
//        }
//        kainInkaDao.persist(kainInka);
//        return kainInka;
//        kainInkaDao.refresh(mergedEntity);
//          return kainInkaDao.saveOrUpdate(kainInka);
//        kainInkaDao.update(kainInka);
    }

    @Override
    public void removeKainInka(TP301KainInka kainInka) {
        kainInka = kainInkaDao.attach(kainInka);
        kainInkaDao.remove(kainInka);
    }

    @Override
    public TWmReminder storeReminder(TWmReminder reminder) {
        if (reminder == null) {
            return reminder;
        }
        if (reminder.getId() != 0L) {
            checkEditReminder();
            checkEditReminderOfOtherUser(Objects.requireNonNullElse(reminder.getCreationUser(), 0L));
//            if (reminderDao.findAll().contains(reminder)) {
            reminder = reminderDao.merge(reminder);
//            }
        } else {
            reminderDao.persist(reminder);
        }
        return reminder;
    }

    @Override
    public void removeReminder(TWmReminder reminder) {
        checkEditReminder();
        checkEditReminderOfOtherUser(Objects.requireNonNullElse(reminder.getCreationUser(), 0L));
        reminder = reminderDao.attach(reminder);
        reminder.setProcess(null);
        eventDao.removeReminderEvent(reminder);
        reminderDao.remove(reminder);
    }

    /*
    @Override
    public TWmAction storeAction(TWmAction action) {
//        TWmActionType actionType;
//        if (!actionTypeDao.exists(action.getActionType())) {
//            actionType = storeActionType(action.getActionType());
//        } else {
//            actionType = actionTypeDao.getActionTypeForName(action.getActionType());
//        }

//        List<CWmListActionSubject> allActionSubjectObjects = getAllActionSubjectObjects();
//        action.setActionType(action.getActionType());     // by default set first action type.
        actionDao.persist(action);
        return action;
    }
     */
    @Override
    public TWmAction storeAction(TWmAction action) {
        if (action == null) {
            return action;
        }
        if (action.getId() != 0L) {
            action = actionDao.merge(action);
        } else {
            actionDao.persist(action);
        }
        return action;
    }

    @Override
    public void removeAction(TWmAction action) {
        checkEditAction();
        action = actionDao.attach(action);
        action.setProcess(null);
        eventDao.removeActionEvent(action);
        actionDao.remove(action);
    }

//    private TWmProcessCase storeCaseInProcessCase(TCase hosCase) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    @Override
    public TWmProcess updateProcess(TWmProcess process) {
        return processDao.merge(process);
    }

    @Override
    public void updateProcessModification(final long pProcessId) {
        processDao.updateProcessModification(pProcessId);
    }

    @Override
    public TWmProcessHospital updateProcessHospital(TWmProcessHospital process) {
        return processHospitalDao.merge(process);
    }

    @Override
    public TWmAction updateAction(TWmAction action) {
        checkEditAction();
        return actionDao.merge(action);
    }

    @Override
    public TWmRequest updateRequest(TWmRequest request) {
        checkEditRequestOfOtherUser(request.getCreationUser());
        return requestDao.merge(request);
    }

    @Override
    public TWmReminder updateReminder(TWmReminder reminder) {
        checkEditReminder();
        checkEditReminderOfOtherUser(Objects.requireNonNullElse(reminder.getCreationUser(), 0L));
        return reminderDao.merge(reminder);
    }

    @Override
    public void closeRemindersByIds(List<Long> pReminderIds) {
        reminderDao.closeRemindersByIds(pReminderIds);
    }

    @Override
    public void createReminderForAllProcesses(List<Long> pProcessIds, TWmReminder pReminder) {
        final long startTime = System.currentTimeMillis();
        pProcessIds.forEach((processId) -> {
            try {
                TWmReminder reminder = (TWmReminder) pReminder.clone();
                TWmEvent event = new TWmEvent();
                event.setEventType(WmEventTypeEn.reminderCreated);
                TWmProcess process = processDao.findById(processId);
                event.setContent(reminder);
                event.setProcess(process);
                event.setCreationDate(new Date());
                event.setCreationUser(ClientManager.getCurrentCpxUserId());
//                event.setCreationUserId(ClientManager.getCurrentCpxUserId());
                process.getReminders().add(reminder);
                storeEvent(event);
            } catch (CloneNotSupportedException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

        });
        LOG.log(Level.INFO, "add " + pProcessIds.size() + " reminders for " + pProcessIds.size() + " processes in " + (System.currentTimeMillis() - startTime) + " ms");
        processDao.updateProcessesModification(pProcessIds);

    }

    @Override
    public TCaseIcd getMainDiagnosisForVersion(long pCaseDetailsId) {
        return icdDao.findMainDiagnosisIcd(pCaseDetailsId);
    }

    @Override
    public int getSecondaryDiagnosisCount(long pVersionId) {
        return icdDao.countForDetailsId(pVersionId).intValue();
    }

    @Override
    public int getProcessCount(long pVersionId) {
        return opsDao.countForDetailsId(pVersionId).intValue();
    }

    @Override
    public TGroupingResults getGroupingResult(long pVersionId, GDRGModel pGrouper) {
        return groupingResultsDao.findGroupingResult_nativ(pVersionId, pGrouper);
//        return groupingResultsDao.findTGroupingResult(pVersionId, pGrouper, pMdId);
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
    public double findSupplementaryFee(GDRGModel pGrouper, long pDetailsId, SupplFeeTypeEn pType) {
        Number result = groupingResultsDao.getSupplementaryValueForIdCalculateOnDatabase(pGrouper, pDetailsId, pType);
        if (result != null) {
            return result.doubleValue();
        }
        return 0.0;
    }

    @Override
    public TWmDocument updateDocument(TWmDocument pDocument) {
        checkEditDocument();
        checkEditDocumentOfOtherUser(pDocument.getCreationUser());
        TWmDocument updatedDocument = documentDao.merge(pDocument);
//        updatedDocument.setContent(null); // is this really necessary to set the doc content to null, why??
        return updatedDocument;
    }

//    @Override
//    public List<CMdkAuditreason> getAllCMdkAuditReasons() {
//        //return CMdkAuditreasonDao.getAllCMdkAuditReasons();
//        return mdkAuditreasonDao.findAll();
//    }
//
//    @Override
//    public List<String> getAllCMdkAuditReasonsNames() {
//        return mdkAuditreasonDao.getAllCMdkAuditReasonsNames();
//    }
    @Override
    public TWmProcess getCurrentProcessByMainCase(long hCaseId) {
        return processCaseDao.getCurrentProcessByMainCase(hCaseId);
    }

    @Override
    public TWmProcessCase getCurrentProcessCaseByMainCase(long hCaseId) {
        return processCaseDao.getCurrentProcessCaseByMainCase(hCaseId);
    }

    @Override
    public List<TWmProcess> getProcessesOfCase(long hCaseId) {
        return getProcessesOfCase(hCaseId, false);
    }

    @Override
    public List<TWmProcess> getProcessesOfCase(long hCaseId, boolean pIncludeCanceled) {
        return processCaseDao.getProcessesOfCase(hCaseId, pIncludeCanceled);
    }

//    @Override
//    public List<CDeadline> getAllDeadlines() {
//        return deadlineDao.getEntries();
//    }
//
//    @Override
//    public List<CDeadline> getDeadlines(final Date pDate) {
//        final List<CDeadline> list = deadlineDao.getEntries();
//        if (pDate != null) {
//            final Iterator<CDeadline> it = list.iterator();
//            while (it.hasNext()) {
//                final CDeadline dl = it.next();
//                if (!dl.isValid(pDate)) {
//                    it.remove();
//                }
//            }
//        }
//        return list;
//    }
    @Override
    public TCase getMainCase(long caseId) {
        TCase hCase = caseDao.findById(caseId);

        if (hCase == null) {
            return null;
        }

        hCase.getPatient().getId();

        hCase.getCaseDetails().iterator();

        hCase.getPatient().getInsurances().iterator();

        hCase.getPatient().getPatientDetailList().iterator();

        return hCase;
    }

//    @Override
//    public List<CMdkAuditreason> getAllAvailableCMdkAuditReasons(Date pDate) {
//        if (pDate == null) {
//            //return CMdkAuditreasonDao.getAllCMdkAuditReasons();
//            return mdkAuditreasonDao.findAll();
//        }
//        return mdkAuditreasonDao.getAllAvailableAuditReasons(pDate);
//    }
//    @Override
//    public CMdkAuditreason getMdkAuditreasonById(Long auditReasonId) {
//        //return CMdkAuditreasonDao.getMdkAuditreasonById(auditReasonId);
//        return mdkAuditreasonDao.findById(auditReasonId);
//    }
//
//    @Override
//    public CMdkAuditreason getMdkAuditreasonByNumber(Long pAuditreasonNumber) {
//        //return CMdkAuditreasonDao.getMdkAuditreasonByNumber(pAuditreasonNumber);
//        return mdkAuditreasonDao.findByInternalId(pAuditreasonNumber);
//    }
//
//    @Override
//    public List<CWmListProcessResult> getAllAvailableProcessResults(Date pDate) {
//        if (pDate == null) {
//            //return cWmListProcessResultDao.getAllProcessResults();
//            return cWmListProcessResultDao.findAll();
//        }
//        return cWmListProcessResultDao.getAllAvailableProcessResults(pDate);
//    }
//
//    @Override
//    public CWmListProcessResult getProcessResultById(Long processResultId) {
//        //return cWmListProcessResultDao.getProcessResultById(processResultId);
//        return cWmListProcessResultDao.findById(processResultId);
//    }
//
//    @Override
//    public CWmListProcessResult getProcessResultByInternalId(Long processResultInternalId) {
//        //return cWmListProcessResultDao.getProcessResultByInternalId(processResultInternalId);
//        return cWmListProcessResultDao.findByInternalId(processResultInternalId);
//    }
//
//    @Override
//    public List<CWmListProcessTopic> getAllAvailableProcessTopics(Date pDate) {
//        if (pDate == null) {
//            return cWmListProcessTopicDao.getAllValidProcessTopics();
//        }
//        return cWmListProcessTopicDao.getAllAvailableProcessTopics(pDate);
//    }
//
//    @Override
//    public Long getFirstAvailableProcessTopicInternalId() {
//        return getFirstAvailableProcessTopicInternalId(new Date());
//    }
//
//    @Override
//    public Long getFirstAvailableProcessTopicInternalId(final Date pDate) {
//        final List<CWmListProcessTopic> availableProcessTopics = getAllAvailableProcessTopics(pDate); // gives valid, undeleted, sorted (based on wm_pt_sort column) and within a valid timeframe process topics
//        if (availableProcessTopics == null || availableProcessTopics.isEmpty()) {
//            LOG.log(Level.WARNING, "No process topics found in CommonDB for given date " + String.valueOf(pDate) + "!");
//            return null;
//        }
//        final CWmListProcessTopic topic = availableProcessTopics.get(0);
//        return topic.getWmPtInternalId();
//    }
    @Override
    public TWmProcessHospitalFinalisation saveOrUpdateProcessResult(TWmProcessHospitalFinalisation pProcessResult) {
        checkDoFinalisation();
        return processResultDao.merge(pProcessResult);
//        if (processResultDao.isDetached(pProcessResult)) {
//            pProcessResult = processResultDao.attach(pProcessResult);
//        }
//        return processResultDao.saveOrUpdate(pProcessResult);
    }

//    @Override
//    public List<CWmListReminderSubject> getAllReminderSubjects(Date pDate) {
//        return reminderSubjectDao.getAllAvailableReminderSubjects(pDate);
//    }
//
//    @Override
//    public CWmListReminderSubject getReminderSubjectById(Long pCWmListReminderSubjectId) {
//        //return reminderSubjectDao.getReminderSubjectById(pCWmListReminderSubjectId);
//        return reminderSubjectDao.findById(pCWmListReminderSubjectId);
//    }
//
//    @Override
//    public CWmListReminderSubject getReminderSubjectByInternalId(Long pCWmListReminderSubjectInternalId) {
//        //return reminderSubjectDao.getReminderSubjectByInternalId(pCWmListReminderSubjectInternalId);
//        return reminderSubjectDao.findByInternalId(pCWmListReminderSubjectInternalId);
//    }
//
//    @Override
//    public List<CWmListDocumentType> getAllDocumentTypeObjects(Date pDate) {
//        return documentTypeDao.getAllAvailableDocumentTypes(pDate);
//    }
//
//    @Override
//    public CWmListDocumentType getDocumentTypeById(long pCWmListDocumentTypeId) {
//        //return documentTypeDao.getDocumentTypeById(pCWmListDocumentTypeId);
//        return documentTypeDao.findById(pCWmListDocumentTypeId);
//    }
//
//    @Override
//    public List<CWmListActionSubject> getAllActionSubjectObjects(Date pDate) {
//        return actionSubjectDao.getAllAvailableActionSubjects(pDate);
//    }
//
//    @Override
//    public CWmListActionSubject getActionSubjectById(Long pCWmListActionSubjectId) {
//        //return actionSubjectDao.getActionSubjectById(pCWmListActionSubjectId);
//        return actionSubjectDao.findById(pCWmListActionSubjectId);
//    }
//
//    @Override
//    public CWmListActionSubject getActionSubjectByInternalId(Long pCWmListActionSubjectInternalId) {
//        //return actionSubjectDao.getActionSubjectByInternalId(pCWmListActionSubjectInternalId);
//        return actionSubjectDao.findByInternalId(pCWmListActionSubjectInternalId);
//    }
//
//    @Override
//    public List<CWmListMdkState> getAllMdkStatesObjects(Date pDate) {
//        if (pDate == null) {
//            return mdkStateDao.getAllValidMdkStates();
//        }
//        return mdkStateDao.getAllAvailableMdkStates(pDate);
//    }
//
//    @Override
//    public CWmListMdkState getMdkStateById(Long pCWmListMdkStateId) {
//        //return mdkStateDao.getMdkStateById(pCWmListMdkStateId);
//        return mdkStateDao.findById(pCWmListMdkStateId);
//    }
    @Override
    public TWmDocument storeDocument(final TWmDocument pWmDocument) {
        if (pWmDocument == null) {
            throw new IllegalArgumentException("Passed document is null!");
        }
        if (pWmDocument.getId() != 0L) {
            return documentDao.merge(pWmDocument);
        } else {
            documentDao.persist(pWmDocument);
            return pWmDocument;
        }
    }

    @Override
    public TWmDocument storeDocument(final TWmDocument pWmDocument, final byte[] pContent, final String pFileExtension) throws IOException {
        if (pWmDocument == null) {
            throw new IllegalArgumentException("Passed document is null!");
        }
        if (pContent == null || pContent.length == 0) {
            //throw new IllegalArgumentException("Content that was passed to document with id " + pWmDocument.id + " is null or empty!");
            LOG.log(Level.WARNING, "Content that was passed to document with id " + pWmDocument.id + " is null or empty!");
        }
        if (pWmDocument.getName() == null || pWmDocument.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name of document with id " + pWmDocument.id + " is null or empty!");
        }
        if (pFileExtension == null || pFileExtension.trim().isEmpty()) {
            throw new IllegalArgumentException("File extension that was passed to document with id " + pWmDocument.id + " is null or empty!");
        }
//        if (!pWmDocument.isFileContentDocument()) {
//            throw new IllegalArgumentException("Document with id " + pWmDocument.id + " is not a file system document, so its cannot be stored as a file");
//        }
//        if (!cpxServerConfig.getDocumentFileSystemConfig()) {
//            throw new IllegalArgumentException("Documents cannot be stored in file system, because this feature is not activated in cpx_server_config.xml");
//        }
        final TWmProcess process = pWmDocument.getProcess();
        if (process == null) {
            throw new IllegalArgumentException("Document with id " + pWmDocument.id + " is not assigned to a process!");
        }
        //pWmDocument.setName(pFileName);
//        if (cpxServerConfig.getDocumentsStoreInFilesystem()) {
        if (cpxServerConfig.getDocumentsStorageType().isEmpty()) {
            throw new IllegalArgumentException("Please define/provide Documents Storage Type (either filesystem or database) in cpx_server_config.xml ");
        } else if (cpxServerConfig.getDocumentsStorageType().toLowerCase().trim().equals("filesystem")) {
            //Store content to file system
            LOG.log(Level.INFO, "Will store content of document " + String.valueOf(pWmDocument) + " in file system...");
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss.SSS");
            final Date now = new Date();
            final String strDate = sdf.format(now);

            final long workflowNumber = process.getWorkflowNumber();
            //final String fileExtension = FilenameUtils.getExtension(pWmDocument.getName());

            final String docName;
            if (workflowNumber != 0L) {
                docName = strDate + "_" + Long.toString(workflowNumber);
            } else {
                LOG.log(Level.SEVERE, "process " + String.valueOf(process) + " has no workflow number");
                docName = strDate;
            }

            final String databaseName = documentDao.getConnectionDatabase();

            //Awi-20180323-CPX894
            //change fetching from db, because with mssql getConnection().getSchema() will cause an exception
            //there for documents can not be stored properly in filesystem and may cause other errors
            //like not finding documents
            final CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
            final String pathToStore = cpxProps.getCpxPathToStore(databaseName, docName.trim() + "." + pFileExtension.trim());
            LOG.log(Level.INFO, "Document storage path of " + String.valueOf(pWmDocument) + " will be set to " + pathToStore);
            pWmDocument.setContent(null);
            pWmDocument.setFileSize(pContent!=null?pContent.length:0);
            pWmDocument.setFilePath(pathToStore);
            final File file = getDocumentFile(pWmDocument, pathToStore);
            //        if (serverRootFolder != null && !serverRootFolder.trim().isEmpty()) {
            //            doc = new File(serverRootFolder.trim() + cpxProps.getFileSeparator() + pathToStore);
            //        } else {
            //            doc = new File(cpxProps.getCpxServerDocumentsDir() + pathToStore);
            //        }

            if (!file.getParentFile().exists()) {
                final boolean createdDirs = file.getParentFile().mkdirs();
                if (!createdDirs) {
                    //LOG.log(Level.SEVERE, "Was not able to create directory: {0}", file.getParentFile().getAbsolutePath());
                    throw new IOException("Was not able to create directory: " + file.getParentFile().getAbsolutePath());
                }
            }
            if (file.exists()) {
                LOG.log(Level.WARNING, "This file already exists with a length of " + (file.length() / 1024) + " KB. Will overwrite it now: " + file.getAbsolutePath());
            }
            try (OutputStream os = new FileOutputStream(file)) {
                if (pContent != null) {
                    os.write(pContent);
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Document with id " + pWmDocument.id + " has no attached content in database", ex);
                throw ex;
            }
        } else if (cpxServerConfig.getDocumentsStorageType().toLowerCase().trim().equals("database")) {
            //Store content to database
            LOG.log(Level.INFO, "Will store content of document {0} in database...", String.valueOf(pWmDocument));
            pWmDocument.setContent(pContent);
            pWmDocument.setFilePath(null);
        } else {
            throw new IllegalStateException("set document properties properly in cpx_server_config.xml (neither database nor file system is set as document content storage)!");
        }
        final String actualSize = org.apache.commons.io.FileUtils.byteCountToDisplaySize(pWmDocument.getFileSize());
        LOG.log(Level.INFO, "File size of document is actually " + actualSize + ": " + String.valueOf(pWmDocument));
        final int maxKb = cpxServerConfig.getDocumentsSizeMax();
        if (maxKb > 0 && pWmDocument.getFileSize() / 1_024D > maxKb) {
            LOG.log(Level.WARNING, "Document is too large. Maximum file size of " + maxKb + " KB is exceeded (acual size is " + actualSize + "): " + String.valueOf(pWmDocument));
        }
        TWmDocument document = storeDocument(pWmDocument);

        LOG.log(Level.INFO, "Document {0} was successfully stored!", String.valueOf(document));
        return document;
    }

    @Override
    public void removeDocument(final long pWmDocumentId) throws IOException {
        checkEditDocument();

        final TWmDocument document = documentDao.findById(pWmDocumentId);
        if (document == null) {
            throw new IllegalArgumentException("No document found with id " + pWmDocumentId);
        }
        checkEditDocumentOfOtherUser(document.getCreationUser());
        removeDocument(document);
    }

    @Override
    public void removeDocument(final TWmDocument pDocument) throws IOException {

        if (pDocument == null) {
            throw new IllegalArgumentException("document cannot be null!");
        }
        long documentId = pDocument.id;

        final CpxUser user = ClientManager.getCurrentCpxUser();
        final String database = ClientManager.getActualDatabase();
        LOG.log(Level.INFO, "Will remove document " + String.valueOf(pDocument) + " (" + pDocument.getFilePath() + ", initiated by " + String.valueOf(user) + " on database " + database + ")...");

        pDocument.setProcess(null);
        eventDao.removeDocumentEvent(pDocument);
        documentDao.remove(pDocument);
        documentDao.flush();

        if (pDocument.isFileContentDocument()) {
            //Content is in file system
            LOG.log(Level.INFO, "Will remove content of document " + String.valueOf(pDocument) + " from file system (" + pDocument.getFilePath() + ", initiated by " + String.valueOf(user) + " on database " + database + ")...");
            // delete document from the server's file system (if any)
            final File file = getDocumentFile(pDocument);
            try {
                checkDocumentFile(file, documentId);
            } catch (FileNotFoundException ex) {
                LOG.log(Level.WARNING, "File cannot be deleted from file system, because it seems to be already vanished: " + file.getAbsolutePath());
                LOG.log(Level.FINER, "Maybe file was already deleted meantime, storing document formerly failed or someone deleted this file manually from file system: " + file.getAbsolutePath(), ex);
            }
            if (file.exists()) {
                try {
                    Files.delete(file.toPath());
                    LOG.log(Level.INFO, "deleted file: {0}", file.getAbsolutePath());
                } catch (IOException ex) {
                    LOG.log(Level.WARNING, "was not able to delete file for document with id: " + file.getAbsolutePath(), ex);
                    throw new IOException("Was not able to delete file for document with id " + pDocument);
                }
//                if (!file.delete()) {
//                    throw new IOException("Was not able to delete file for document with id " + pDocument);
//                }
            }
        } else {
            //Content is in database
            LOG.log(Level.INFO, "Will remove content of document " + String.valueOf(pDocument) + " from database...");
            //does nothing in this branch (content is directly attached to entity)
        }

        LOG.log(Level.INFO, "Document " + String.valueOf(pDocument) + " was successfully removed by user " + String.valueOf(user) + " on database " + database + "!");
    }

    @Override
    public byte[] getDocumentContent(final long pWmDocumentId) throws IOException {
        final TWmDocument document = documentDao.findById(pWmDocumentId);
        if (document == null) {
            throw new IllegalArgumentException("No document found with id " + pWmDocumentId);
        }

        final byte[] content;
        if (document.isFileContentDocument()) {
            //Get from file system
            LOG.log(Level.INFO, "Will retrieve content of document " + String.valueOf(document) + " from file system (" + document.getFilePath() + ")...");
            final File file = getDocumentFile(document);
            checkDocumentFile(file, pWmDocumentId);
            try (FileInputStream in = new FileInputStream(file)) {
                content = IOUtils.toByteArray(in);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Cannot read file: " + file.getAbsolutePath(), ex);
                throw ex;
            }

            if (content == null || content.length == 0) {
                LOG.log(Level.SEVERE, "Document with id " + pWmDocumentId + " has no attached content in file system under this path: " + file.getAbsolutePath());
            }
        } else {
            //Get from database
            LOG.log(Level.INFO, "Will retrieve content of document " + String.valueOf(document) + " from database...");
            if (document.getContent() == null || document.getContent().length == 0) {
                LOG.log(Level.SEVERE, "Document with id " + pWmDocumentId + " has no attached content in database");
            }
            content = document.getContent();
        }
        LOG.log(Level.INFO, "Content of document " + String.valueOf(document) + " was successfully retrieved!");
        return content;
    }

    private File getDocumentFile(final TWmDocument pWmDocument, final String pFilePath) {
        if (pWmDocument == null) {
            throw new IllegalArgumentException("pWmDocument is null, cannot determine file path!");
        }
        if (pWmDocument.getFilePath() == null || pWmDocument.getFilePath().trim().isEmpty()) {
            throw new IllegalArgumentException("This document has no file path, cannot determine file path for document with id " + pWmDocument.id);
        }
        if (pFilePath == null || pFilePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Passed file path is null or empty for document with id " + pWmDocument.id);
        }
        final File file;
        final CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
//        final String serverRootFolder = cpxServerConfig.getServerRootFolder();
        // Pna: CPX-1325
        final String serverRootFolder = cpxServerConfig.getDocumentsFileSystemPath();

        Path path = Paths.get(serverRootFolder);
        boolean exists = Files.exists(path);

        if (!exists) {
            throw new IllegalArgumentException("Provided document-storage-path \" " + serverRootFolder + " \" is INVALID. \n Please configure cpx_server_config.xml properly ");
        }
        final String filePath = pFilePath;
        // user-defined fileSystem Path
        if (serverRootFolder != null && !serverRootFolder.trim().isEmpty()) {
            file = new File(serverRootFolder + cpxProps.getFileSeparator() + filePath);
        } else { // default path ("documents" folder)
            file = new File(cpxProps.getCpxServerDocumentsDir() + filePath);
        }
        return file;
    }

    private File getDocumentFile(final TWmDocument pWmDocument) {
        final String filePath = pWmDocument.getFilePath();
        return getDocumentFile(pWmDocument, filePath);
    }

    private static void checkDocumentFile(final File pFile, final long pWmDocumentId) throws IOException {
        if (pFile == null) {
            throw new IllegalArgumentException("pFile is null");
        }
        if (!pFile.exists()) {
            throw new FileNotFoundException("Cannot find file for document id " + pWmDocumentId + ": " + pFile.getAbsolutePath());
        }
        if (!pFile.isFile()) {
            throw new IOException("This is directory but not a file for document id " + pWmDocumentId + ": " + pFile.getAbsolutePath());
        }
        if (!pFile.canRead()) {
            throw new IOException("Cannot read from this file for document id " + pWmDocumentId + ": " + pFile.getAbsolutePath());
        }
    }

//    @Override
//    public byte[] getDocumentContentFromFS(String serverRootFolder, String filePath) {
//        byte[] fileContent = null;
//        try {
//            File file;
//
//            if (serverRootFolder != null && !serverRootFolder.isEmpty()) {
//                file = new File(serverRootFolder + "\\" + filePath);
//            } else {
//                String cpxHome = CpxSystemProperties.getInstance().getCpxHome();
////                file = new File(cpxHome + filePath);
//                file = new File(cpxHome + "documents\\" + filePath);
//            }
//
//            try (FileInputStream in = new FileInputStream(file)) {
//                fileContent = IOUtils.toByteArray(in);
//            }
//        } catch (FileNotFoundException ex) {
//            LOG.log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, null, ex);
//        }
//        return fileContent;
//    }
//    @Override
//    public void sendDocumentContent(byte[] docContent) {
//        try {
//            Calendar cal = Calendar.getInstance();
//            int currentYear = cal.get(Calendar.YEAR);
//            int currentMonth = cal.get(Calendar.MONTH) + 1;
//
//            final CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
////            String pathToStore = cpxProps.getCpxHome() + "documents\\2017\\09\\" + document.getName();
//            String pathToStore = cpxProps.getCpxHome() + "documents\\" + currentYear + "\\" + currentMonth + "\\" + "name of the doc";
//            File doc = new File(pathToStore);
//            if (!doc.getParentFile().exists()) {
//                doc.getParentFile().mkdirs();
//            }
//            try (OutputStream os = new FileOutputStream(doc)) {
//                os.write(docContent);
//            }
//        } catch (FileNotFoundException ex) {
//            LOG.log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, null, ex);
//        }
//    }
//
//    /**
//     *
//     * @param cat template category
//     * @return map of internal template ids with their name
//     */
//    @Override
//    public Map<Long, String> getAllDraftTypes(CategoryEn cat) {
//        Map<Long, String> map = new LinkedHashMap<>();
//        List<CWmListDraftType> draftTypes = draftTypesDao.getAllValidDraftTypes(cat);
//        for (CWmListDraftType draftType : draftTypes) {
//            map.put(draftType.getWmDrtInternalId(), draftType.getWmDrtName());
//        }
//        return map;
//    }
//
//    @Override
//    public List<CWmListDraftType> getAllAvailableDraftTypes(CategoryEn cat) {
//        List<CWmListDraftType> draftTypes = draftTypesDao.getAllValidDraftTypes(cat);
////        for (CWmListDraftType draftType : draftTypes) {
////            map.put(draftType.getWmDrtInternalId(), draftType.getWmDrtName());
////        }
//        return draftTypes;
//    }
//
//    @Override
//    public List<CWmListDraftType> getAllAvailableDraftTypes() {
////        Map<Long, String> map = new LinkedHashMap<>();
//        List<CWmListDraftType> draftTypes = draftTypesDao.getAllValidDraftTypes();
////        for (CWmListDraftType draftType : draftTypes) {
////            map.put(draftType.getWmDrtInternalId(), draftType.getWmDrtName());
////        }
//        return draftTypes;
//    }
//    @Override
//    public Map<String, String> getDepartmentsMap() {
//        Map<String, String> map = new LinkedHashMap<>();
//        List<CDepartment> departments = departmentDao.findAll();
////        List<CDepartment> departments = departmentDao.getEntries("de");
//        for (CDepartment dept : departments) {
//            map.put(dept.getDepKey301(), dept.getDepDescription301());
//        }
//        return map;
//    }
    @Override
    public TWmEvent storeEvent(TWmEvent pEvent, long pProcessId) {
        TWmProcess proc = processDao.findById(pProcessId);
        pEvent.setProcess(proc);
//        pEvent.setProcess((TWmProcessHospital) process);
        return storeEvent(pEvent);
    }

    @Override
    public List<TWmEvent> getEvents(long pProcessId, final boolean pHistoryDeleted, final Integer pLimit) {
        return eventDao.findAllForProcess(pProcessId, pHistoryDeleted, pLimit);
    }

    @Override
    public List<TWmEvent> getEvents(long pProcessId) {
        return eventDao.findAllForProcess(pProcessId, true);
    }

    @Override
    public List<TWmReminder> getReminder(long pProcessId) {
        return reminderDao.findAllForProcess(pProcessId);
    }

    @Override
    public List<TWmReminder> getReminder(long pProcessId, final Boolean pFinishedFl) {
        return reminderDao.findAllForProcess(pProcessId, pFinishedFl);
    }

    @Override
    public List<TWmProcessCase> getProcessCases(long pProcessId) {
        final long startTime = System.currentTimeMillis();
        LOG.log(Level.INFO, "get hospital cases for process id " + pProcessId);
        List<TWmProcessCase> result = processCaseDao.findAllForProcess(pProcessId);
        LOG.log(Level.FINER, "got " + result.size() + " hospital cases for process id " + pProcessId + " in " + (System.currentTimeMillis() - startTime) + " ms");
        return result;
    }

    @Override
    public TWmProcess findProcessById(long pProcessId) {
        return processDao.findById(pProcessId);

    }

    @Override
    public List<TWmDocument> getDocuments(long pProcessId) {
        return documentDao.findAllForProcess(pProcessId);
    }

    @Override
    public List<TWmRequest> getRequests(long pProcessId) {
        return requestDao.findAllForProcess(pProcessId);
    }

    @Override
    public long getLatestRequestId(long pProcessId) {
        return requestDao.getLatestRequestId(pProcessId);
    }

    @Override
    public TWmRequest getLatestRequest(long pProcessId) {
        return requestDao.getLatestRequest(pProcessId);
    }

    @Override
    public List<TWmAction> getActions(long pProcessId) {
        return actionDao.findAllForProcess(pProcessId);
    }

    @Override
    public List<TP301KainInka> getInkaMsgs(long tCaseId) {
        return kainInkaDao.findAllForCase(tCaseId);
    }

    @Override
    public TP301KainInka getTP301KainInkaById(long kainInkaId) {
        return kainInkaDao.getTP301KainInkaById(kainInkaId);
    }

    @Override
    public TCase getMainCaseForProcess(long pProcessId) {
        return processCaseDao.getMainCaseForProcess(pProcessId);
    }

    @Override
    public String getUserById(long assignedUserId) {
        return cdbUsersDao.getUserbyId(assignedUserId);
    }

    @Override
    public Collection<String> getMatchingUsers(String userText, Date pDate) {
        return cdbUsersDao.getValidMatchForUser(userText, pDate);
    }

    @Override
    public void saveOrUpdateProcess(TWmProcess process) {
        LOG.log(Level.FINE, "save or update process " + String.valueOf(process));
        processDao.merge(process);
    }

    @Override
    public String getRequesrSateById(long requestStateId) {
        final Map<Long, CWmListMdkState> mdkListStates = cwmListMdkStateDao.getMdkStates();
        if(mdkListStates != null) {
            final CWmListMdkState mdkListState = mdkListStates.get(requestStateId);
            if (mdkListState != null) {
                return mdkListState.getWmMsName();
            }
        }
        return null;
    }

    
//    @Override
//    public CWmListProcessTopic getProcessTopic(Long pTopic) {
//        //return cWmListProcessTopicDao.findByIdent(pTopic);
//        return cWmListProcessTopicDao.findByInternalId(pTopic);
//    }
//
//    @Override
//    public CWmListProcessResult getProcessResult(Long pResult) {
//        //return cWmListProcessResultDao.getProcessResultByIdent(pResult);
//        return cWmListProcessResultDao.findByInternalId(pResult);
//    }
//
    @Override
    public List<TP301KainInkaPvt> getAllPvtsForPvv(long pvvId) {
        return tp301KainInkaPvtDao.findAllPvtsForPvv(pvvId);
    }
//
//    @Override
//    public Map<Long, CMdkAuditreason> getValidUndeletedAuditReasons(Date date) {
//        Map<Long, CMdkAuditreason> map = new LinkedHashMap<>();
//        // valid, undeleted and within a valid timeframe
//        List<CMdkAuditreason> mdkAuditReasons = mdkAuditreasonDao.getAllAvailableAuditReasons(date);
//        for (CMdkAuditreason mdkAuditReason : mdkAuditReasons) {
//            map.put(mdkAuditReason.getMdkArNumber(), mdkAuditReason);
//        }
//        return map;
//    }

    @Override
    public TP301KainInka getKainInka(long oKainInka) {
        //kainInkaDao.changeFlushMode(FlushMode.MANUAL);
        TP301KainInka kainInka = kainInkaDao.findById(oKainInka);
        for (Iterator<TP301KainInkaPvv> itPvv = kainInka.getKainInkaPvvs().iterator(); itPvv.hasNext();) {
            TP301KainInkaPvv pvv = itPvv.next();
            for (Iterator<TP301KainInkaPvt> itPvt = pvv.getKainInkaPvts().iterator(); itPvt.hasNext();) {
                TP301KainInkaPvt pvt = itPvt.next();
            }
        }
        return kainInka;
    }

    @Override
    public void updateDocumentName(TWmDocument doc, String docNameToUpdate) {
        documentDao.updateDocumentName(doc, docNameToUpdate);
    }

    @Override
    public TWmProcess findSingleProcessForWorkflowNumber(Long workflowNumber) {
        TWmProcess processForId = processDao.findProcessForWorkflowNumber(workflowNumber);
        return processForId;
    }

    @Override
    public TWmProcess findSingleProcessForId(Long workflowId) {
        TWmProcess pc = processDao.findById(workflowId);
        return pc;
    }

    @Override
    public Long findSingleProcessIdForWorkflowNumber(Long workflowNumber) {
        Long processId = processDao.findProcessIdForWorkflowNumber(workflowNumber);
        return processId;
    }

    @Override
    public List<TWmProcess> findWorkflowsByPatient(TPatient patient) {
        return processDao.findWorkflowsByPatient(patient);
    }

//    @Override
//    public String getInsuCompName(String cellData, String de) {
//        return cInsuranceCompanyDao.getInsuCompName(cellData, de);
//    }
    @Override
    public TWmProcess storeProcessForMdkReminders(TWmProcess process, TWmRequest pRequest) {
        if (process == null) {
            return null;
        }
        if (process.getWorkflowNumber() == 0L) {
            process.setWorkflowNumber(processDao.createWorkflowNumber());
        }
        boolean isNew = process.id == 0L;
        if (isNew) {
            processDao.persist(process);
        }

        if (process.isProcessHospital()) {
            for (TWmRequest request : new HashSet<>(((TWmProcessHospital) process).getRequests())) {
                if (request.id != 0L) {
                    continue;
                }
                requestDao.persist(request);
            }
            if (process.isProcessHospital()) {
                for (TWmReminder rem : new HashSet<>(process.getReminders())) {
                    if (rem.id != 0L) {
                        continue;
                    }
                    reminderDao.persist(rem);
                }
            }
        }
        if (process.isProcessHospital()) {
            for (TWmEvent event : new HashSet<>(process.getEvents())) {
                if (event.id != 0L) {

//                       event.setRequest(pRequest);
                    event = eventDao.merge(event);
                    continue;
                }
                event.setRequest(pRequest);
                eventDao.persist(event);
            }
        }

        for (TWmEvent event : new HashSet<>(process.getEvents())) {
            if (event.getContent() instanceof TWmReminder) {
                //event.setRequest(pRequest);
                event = eventDao.merge(event);
            }

        }
        process = processDao.merge(process);

        return process;
    }

    @Override
    public TWmProcess storeProcessForDocumentImport(TWmProcess pProcess) throws IOException {
        if (pProcess == null) {
            return null;
        }
        if (pProcess.getWorkflowNumber() == 0L) {
            pProcess.setWorkflowNumber(processDao.createWorkflowNumber());
        }
        boolean isNew = pProcess.id == 0L;
        if (isNew) {
            processDao.persist(pProcess);
        }
        if (pProcess.isProcessHospital()) {
            for (TWmRequest request : new HashSet<>(((TWmProcessHospital) pProcess).getRequests())) {
                if (request.id != 0L) {
                    continue;
                }
                requestDao.persist(request);
            }
        }
        for (TWmEvent event : new HashSet<>(pProcess.getEvents())) {
            if (!isNew && event.id != 0L) {
                continue;
            }
            if (isNew || event.id == 0L) {
                if (event.getHosCase() != null) {
                    caseDao.attach(event.getHosCase());
                    caseDao.persist(event.getHosCase());
                }
                if (event.getKainInka() != null) {
                    kainInkaDao.attach(event.getKainInka());
                    kainInkaDao.persist(event.getKainInka());
                }
                if (event.getReminder() != null) {
                    checkEditReminder();
                    checkEditReminderOfOtherUser(Objects.requireNonNullElse(event.getReminder().getCreationUser(), 0L));
                    event.setReminder(reminderDao.merge(event.getReminder()));
                }
                if (event.getRequest() != null) {
    //                checkEditRequestOfOtherUser(event.getRequest().getCreationUser());
                    event.setRequest(requestDao.merge(event.getRequest()));
                }
                if (event.getAction() != null) {
                    checkEditAction();
                    event.setAction(actionDao.merge(event.getAction()));
                }
            }
            if (event.getDocument() != null) {
                //documentDao.persist(event.getDocument());
                //TWmDocument newDoc = storeDocument(event.getDocument(), event.getDocument().getContent(), event.getDocument().getExtension());
                try {
                    storeDocument(event.getDocument(), event.getDocument().getContent(), event.getDocument().getName());
                } catch (IllegalArgumentException ex) {
                    LOG.log(Level.SEVERE, "Cannot save document " + event.getDocument().getName(), ex);
                }
                //documentDao.flush();
                //event.setDocument(newDoc);
            }
//            if (isNew) {
            event = eventDao.merge(event);
//            }
        }

        // update PROCESS_MODIFICATION_USER and LAST_PROCESS_MODIFICATION
        updateProcessModification(pProcess.id);

        if (!isNew) {
            //pProcess = processDao.merge(pProcess);
        } else {
            broadcast2.send(BroadcastOriginEn.ADD_PROCESS, "Der Vorgang " + pProcess.getWorkflowNumber() + " wurde hinzugefügt", null, pProcess.id);
        }
//        for(TWmReminder reminder: new HashSet<>(pProcess.getReminders())) {
//            reminderDao.persist(reminder);
//        }
//        for(TWmReminder reminder: new HashSet<>(pProcess.get())) {
//            reminderDao.persist(reminder);
//        }
//        for(TWmDocument doc: new HashSet<>(pProcess.getDocuments())) {
//            documentDao.persist(doc);
//        }
        return pProcess;
    }

    private ImportConfig<Sap> getSapImportConfig(final CpxJobConstraints pImportConstraint) {
        final SapJob sapConfig = getSapConfig();
        return getSapImportConfig(sapConfig, pImportConstraint);
    }

    private ImportConfig<Sap> getSapImportConfig() {
        return getSapImportConfig((CpxJobConstraints) null);
    }

//    private ImportConfig<Sap> getSapImportConfig(final SapJob pSapConfig) {
//        return getSapImportConfig(pSapConfig, null);
//    }
    private ImportConfig<Sap> getSapImportConfig(final SapJob pSapConfig, final CpxJobConstraints pImportConstraint) {
        //final String database = ClientManager.getActualDatabase();
        final String identifier = caseDao.getDbIdentifier();
        final Sap module = new Sap(pSapConfig, new FallContainer());
//        module.getInputConfig().setTargetDatabase(identifier); //identifier
//        module.getInputConfig().setImportMode(ImportMode.Version);
//        module.getInputConfig().setConstraints(pImportConstraint);
        return new ImportConfig<>(identifier, module, licenseBean.getLicense(), pImportConstraint);
    }

    @Override
//    public void sendInkaMessage(final TP301KainInka pKainInka) {
    public TP301KainInka sendInkaMessage(final TP301KainInka pKainInka) throws IllegalArgumentException {
        if (pKainInka == null) {
//            return;
            LOG.log(Level.FINEST, "Passed TP301KainInka object cannot be null null!");
            return null;
        }
        if (!(pKainInka instanceof TP301Inka)) {
            throw new IllegalArgumentException("Passed object is not an INKA instance");
        }
        TP301Inka inka = (TP301Inka) pKainInka;
        final boolean usePool = false;
        final TCase cs = inka.getTCaseId();
//        final TPatient pat = cs == null ? null : cs.getPatient();
//        final TPatientDetails patDet = pat == null ? null : pat.getPatDetailsActual();
//        final TInsurance patIns = pat == null ? null : pat.getPatInsuranceActual();
        //final SapJob sapConfig = getSapConfig();
        //final String database = ClientManager.getActualDatabase();
        //final ImportConfig<Sap> importConfig = new ImportConfig<>(database, new Sap(sapConfig, new FallContainer()), ImportMode.Version, licenseBean.getLicense());
        final ImportConfig<Sap> importConfig = getSapImportConfig();
        final SapJob sapConfig = importConfig.getModule().getInputConfig();
        LOG.log(Level.INFO, "Will send INKA message via SAP now: {0}", String.valueOf(sapConfig));
        try (final SapConnection sapConnection = new SapConnection(importConfig /*, pKainInka.getHospitalIdentifier() */, usePool)) {
            final String institution = sapConfig.getInstitution();
            final String fallNr = cs.getCsCaseNumber();
            //final String handle = (new SecureRandom()).nextInt() + ""; //replace this with incrementing number
//            final long handle = System.nanoTime();

//create new SendingInkaNumber and set it to the inka (setCurrentNrSending) as well
            final long handle = inkaDao.createNextSendingInkaNumberValue();
            inka.setCurrentNrSending(handle);
            final String kostr = pKainInka.getCostUnitSap();
            if(inkaDao.isDetached(inka)) {
                inka = inkaDao.merge(inka);
            }
            final SapExportResult sapExportResult = sapConnection.sendInkaMessage(institution, fallNr, handle, kostr, inka);
            if (sapExportResult == null) {
                LOG.log(Level.INFO, "Tried to send INKA message, but result is null (sent successfully)");
                //throw new IllegalArgumentException("Cannot send INKA message, result is empty!");
            } else {
                LOG.log(Level.INFO, "Tried to send INKA message: " + sapExportResult.getText() + ", " + sapExportResult.getWarnings() + ", " + sapExportResult.getState());
                throw new IllegalArgumentException("Cannot send INKA message! Text: " + sapExportResult.getText() + ", Warnings: " + sapExportResult.getWarnings() + ", State: " + sapExportResult.getState());
            }
        } catch (JCoException | JCoRuntimeException ex) {
            LOG.log(Level.SEVERE, "Cannot establish SAP connection or error occured during sending INKA message with config name: " + sapConfig.getName() + ": " + String.valueOf(sapConfig), ex);
            throw new IllegalArgumentException("Cannot establish connection or error occured during sending INKA message with SAP configuration '" + sapConfig.getName() + "':\n" + String.valueOf(sapConfig) + "\n-> " + ex.getMessage());
        }
        return pKainInka;
    }
    //just for testing without SAP
//    @Override
//    public TP301KainInka sendInkaMessage(final TP301KainInka pKainInka) throws IllegalArgumentException {
//        if (pKainInka == null) {
////            return;
//            LOG.log(Level.FINEST, "Passed TP301KainInka object cannot be null null!");
//            return null;
//        }
//        if (!(pKainInka instanceof TP301Inka)) {
//            throw new IllegalArgumentException("Passed object is not an INKA instance");
//        }
//        final TP301Inka inka = (TP301Inka) pKainInka;
//        final boolean usePool = false;
//        final TCase cs = inka.getTCaseId();
//
//        final String fallNr = cs.getCsCaseNumber();
//        //final String handle = (new SecureRandom()).nextInt() + ""; //replace this with incrementing number
////            final long handle = System.nanoTime();
//
//        //create new SendingInkaNumber and set it to the inka (setCurrentNrSending) as well
//        final long handle = inkaDao.createNextSendingInkaNumberValue();
//        inka.setCurrentNrSending(handle);
//        final String kostr = pKainInka.getCostUnitSap();
//
//        return pKainInka;
//    }

    @Override
    public TP301KainInka cancelInkaMessage(final TP301KainInka pKainInka) throws IllegalArgumentException {
        if (pKainInka == null) {
            LOG.log(Level.FINEST, "Passed TP301KainInka object cannot be null!");
            return null;
//            return;
        }
        if (!(pKainInka instanceof TP301Inka)) {
            throw new IllegalArgumentException("Passed object is not an INKA instance");
        }
        final TP301Inka inka = (TP301Inka) pKainInka;
        final boolean usePool = false;
        final TCase cs = inka.getTCaseId();
        //final SapJob sapConfig = getSapConfig();

        final ImportConfig<Sap> importConfig = getSapImportConfig();
        final SapJob sapConfig = importConfig.getModule().getInputConfig();
        LOG.log(Level.INFO, "Will cancel an INKA message via SAP now: {0}", String.valueOf(sapConfig));
        try (final SapConnection sapConnection = new SapConnection(importConfig, usePool)) {
            final String institution = sapConfig.getInstitution();
            final String fallNr = cs.getCsCaseNumber();
            final long handle = inka.getCurrentNrSending(); //get existing one
            //final String handle = (new SecureRandom()).nextInt() + ""; //replace this with incrementing number
            //final String handle = System.nanoTime() + "";
            //final String kostr = pKainInka.getCostUnitSap();

            final SapExportResult sapExportResult = sapConnection.cancelInkaMessage(institution, fallNr, handle);

            if (sapExportResult == null) {
                LOG.log(Level.INFO, "Tried to cancel INKA message, but result is null");
                //throw new IllegalArgumentException("Cannot send INKA message, result is empty!");
            } else {
                LOG.log(Level.INFO, "Tried to cancel INKA message: " + sapExportResult.getText() + ", " + sapExportResult.getWarnings() + ", " + sapExportResult.getState());
                throw new IllegalArgumentException("Cannot cancel INKA message! Text: " + sapExportResult.getText() + ", Warnings: " + sapExportResult.getWarnings() + ", State: " + sapExportResult.getState());
            }
        } catch (JCoException | JCoRuntimeException ex) {
            LOG.log(Level.SEVERE, "Cannot establish SAP connection or error occured during cancellation of INKA message with config name: " + sapConfig.getName() + ": " + String.valueOf(sapConfig), ex);
            throw new IllegalArgumentException("Cannot establish connection or error occured during cancellation of INKA message with SAP configuration '" + sapConfig.getName() + "':\n" + String.valueOf(sapConfig) + "\n-> " + ex.getMessage());
        }
        return pKainInka;
    }
    //just for testing without SAP
//    @Override
//    public TP301KainInka cancelInkaMessage(final TP301KainInka pKainInka) throws IllegalArgumentException {
//        if (pKainInka == null) {
//            LOG.log(Level.FINEST, "Passed TP301KainInka object cannot be null!");
//            return null;
//        }
//        if (!(pKainInka instanceof TP301Inka)) {
//            throw new IllegalArgumentException("Passed object is not an INKA instance");
//        }
//        final TP301Inka inka = (TP301Inka) pKainInka;
//        final boolean usePool = false;
//        final TCase cs = inka.getTCaseId();
//
//        final String fallNr = cs.getCsCaseNumber();
//        final long handle = inka.getCurrentNrSending(); //get existing one
//        return pKainInka;
//    }

    public void removeBills(final TCase currentCase) {
//        caseDao.attach(currentCase);
        final List<TSapFiBill> bills = sapFiBillDao.findAllForCase(currentCase.id);
//        sapFiBillDao.deleteItems(bills);
//        sapFiBillDao.flush();
        if (bills != null && !bills.isEmpty()) {
            for (TSapFiBill bill : bills) {
                sapFiBillDao.attach(bill);
//                bill.setTCaseId(currentCase);
                sapFiBillDao.remove(bill);
//                sapFiBillDao.flush();
            }
            bills.clear();
        }
        final List<TSapFiOpenItems> openItems = sapFiOpenItemsDao.findAllForCase(currentCase.id);
//        sapFiOpenItemsDao.deleteItems(openItems);
//        sapFiOpenItemsDao.flush();
        if (openItems != null && !openItems.isEmpty()) {
            for (TSapFiOpenItems openItem : openItems) {
//                openItem.setTCase(currentCase);
                sapFiOpenItemsDao.attach(openItem);
                sapFiOpenItemsDao.remove(openItem);
//                sapFiOpenItemsDao.flush();
            }
            openItems.clear();
        }

    }

    @Override
//    public void updateBills(final Long pCaseId) {
    public void updateBills(final TCase currentCase) {
        if (currentCase == null || currentCase.getId() == 0L) {
            LOG.log(Level.FINEST, "Passed hospital case & caseId cannot be null or 0!");
            return;
        }

//       final TCase cs = caseDao.findById(pCaseId);
//        if (cs == null) {
//            LOG.log(Level.WARNING, "Cannot update bills, because there's no hospital case with id " + pCaseId);
//            return;
//        }
        final ImportConfig<Sap> importConfig = getSapImportConfig();
        final SapJob sapConfig = importConfig.getModule().getInputConfig();
        LOG.log(Level.INFO, "Will update bills for hospital case id {0} via SAP now: {1}", new Object[]{currentCase.getId(), String.valueOf(sapConfig)});
        final boolean usePool = false;
        Map<String, SapExportResult> fiDataMap;
        try (final SapConnection sapConnection = new SapConnection(importConfig, usePool)) {
            fiDataMap = sapConnection.getFiData(sapConfig.getInstitution(), currentCase.getCsCaseNumber());
        } catch (JCoException | JCoRuntimeException ex) {
            LOG.log(Level.SEVERE, "Cannot establish SAP connection or error occured during updating hospital bill with config name: " + sapConfig.getName() + ": " + sapConfig, ex);
            throw new CpxSapException("Cannot establish connection or error occured during updating hospital bill with SAP configuration '" + sapConfig.getName() + "':\n" + sapConfig + "\n-> " + ex.getMessage());
        } catch (Throwable ex) {
            LOG.log(Level.SEVERE, "Something went definitely wrong with SAP configuration: " + sapConfig.getName() + ": " + sapConfig, ex);
            throw new CpxSapException("Something went definitely wrong with SAP configuration '" + sapConfig.getName() + "':\n" + sapConfig + "\n-> " + ex.getMessage());
        }

        final SapExportResult fiData = fiDataMap.get(MAP_KEY_FI_CASE_STATUS);
        final SapExportResult fiOpenItems = fiDataMap.get(MAP_KEY_FI_OPEN_ITEM_STATUS);

        removeBills(currentCase); // remove bills of a case

//        caseDao.getSession().clear();
        caseDao.attach(currentCase);

        Iterator<SapFiFactura> itFiBill = fiData.getFacturas().iterator();
        while (itFiBill.hasNext()) {
            SapFiFactura fac = itFiBill.next();
            TSapFiBill fiBill = new TSapFiBill(); //cs.getIkz(), cs.getFallNr(), patNr
            fiBill.setTCaseId(currentCase);
            fiBill.setInvoice(fac.getVbeln());
            fiBill.setFiscalYear(fac.getGjahr());
            fiBill.setInvoiceDate(fac.getFkdat());
            fiBill.setInvoiceKind(fac.getFkart());
            fiBill.setInvoiceType(fac.getFktyp());
            fiBill.setNetValue(fac.getNetwr());
            fiBill.setReceiverRef(fac.getKunrg());
            fiBill.setReferenceCurrency(fac.getWaerk());
            fiBill.setReferenceType(fac.getVbtyp() == null || fac.getVbtyp().isEmpty() ? null : SapReferenceTypeEn.valueOf(fac.getVbtyp()));
            fiBill.setState(fac.getRfbsk());
            fiBill.setStornoRef(fac.getFksto());

            Iterator<SapFiPosition> itBillposition = fac.getPositions().iterator();
            List<TSapFiBillposition> listOfBillPositions = new ArrayList<>();
            while (itBillposition.hasNext()) {
                SapFiPosition billposition = itBillposition.next();
                TSapFiBillposition fiBillposition = new TSapFiBillposition();
                fiBillposition.setTSapFiBill(fiBill);
                fiBillposition.setAmount(billposition.getFkimg());
                fiBillposition.setBaseValue(billposition.getIshgprs());
                fiBillposition.setInvoice(billposition.getVbeln());
                fiBillposition.setNetValue(billposition.getNetwr());
                fiBillposition.setPositionNumber(billposition.getPosnr());
                fiBillposition.setReferenceId(billposition.getIshablst());
                fiBillposition.setText(billposition.getArktx());

                // add all bill positions to the bill
                listOfBillPositions.add(fiBillposition);
            }

            fiBill.setSapBillpositions(listOfBillPositions);

            //PNa: cpx-1760
//            caseDao.getSession().clear();
//            caseDao.attach(currentCase);
            sapFiBillDao.persist(fiBill);
        }

        Iterator<SapFiOpenItem> itFiOpenItems = fiOpenItems.getOpenItems().iterator();
        while (itFiOpenItems.hasNext()) {
            SapFiOpenItem fiOpenItemTmp = itFiOpenItems.next();
            TSapFiOpenItems fiOpenItem = new TSapFiOpenItems();
            fiOpenItem.setTCase(currentCase);
            fiOpenItem.setCompanyCode(fiOpenItemTmp.getBukrs());
            fiOpenItem.setCurrencyKey(fiOpenItemTmp.getWaers());
            fiOpenItem.setCustomerNumber(fiOpenItemTmp.getKunnr());
            fiOpenItem.setDebitCreditKey(fiOpenItemTmp.getShkzg());
            fiOpenItem.setFiscalYear(fiOpenItemTmp.getGjahr());
            fiOpenItem.setKindOfReceipt(fiOpenItemTmp.getBlart()); //fiOpenItemTmp.blart?
            fiOpenItem.setNetValue(fiOpenItemTmp.getNetwr());
            fiOpenItem.setNumberReceipt(fiOpenItemTmp.getBelnr()); //fiOpenItemTmp.belnr?
            fiOpenItem.setOrderDateReceipt(fiOpenItemTmp.getCpudt()); //fiOpenItemTmp.cpudt?
            fiOpenItem.setPostingKey(fiOpenItemTmp.getBschl());
            fiOpenItem.setReceiptDateReceipt(fiOpenItemTmp.getBudat()); //fiOpenItemTmp.budat?
            fiOpenItem.setRecordingDateReceipt(fiOpenItemTmp.getBldat()); //fiOpenItemTmp.bldat?
            fiOpenItem.setRefNumber(fiOpenItemTmp.getXblnr());
            fiOpenItem.setRefNumberReceipt(fiOpenItemTmp.getDzuonr()); //fiOpenItemTmp.dzuonr?
            fiOpenItem.setText(fiOpenItemTmp.getSgtxt());

//            caseDao.getSession().clear();
//            caseDao.attach(currentCase);
            sapFiOpenItemsDao.persist(fiOpenItem);
        }

        caseDao.flush();
        caseDao.executeUpdate(CpxDistributor.getBillingDateSql(currentCase.getId()));
    }

    /*
    // just for testing without SAP
    @Override
    public void updateBills(final TCase currentCase) {
        if (currentCase == null || currentCase.getId() == 0L) {
            LOG.log(Level.FINEST, "Passed hospital case id cannot be null or 0!");
            return;
        }

        removeBills(currentCase); // remove bills of a case

//        caseDao.flush();
//        caseDao.getSession().getSessionFactory().getCurrentSession().merge(currentCase);
//        caseDao.getSession().clear(); // it clears prev deleted transactions.
        caseDao.attach(currentCase); // A different object with the same identifier value was already associated with the session : [de.lb.cpx.model.TCase#29746001]
        for (int i = 0; i < 1; i++) {
            TSapFiBill fiBill = new TSapFiBill();
            fiBill.setTCaseId(currentCase);
            fiBill.setFiscalYear(1);
            fiBill.setInvoice("Invoice-" + i);
            fiBill.setInvoiceDate(new Date());
            fiBill.setInvoiceKind("kind3");
            fiBill.setInvoiceType("Type3");
            fiBill.setNetValue(2.0);
            fiBill.setReceiverRef("receiver ref3");
            fiBill.setReferenceCurrency("Cur3.");
            fiBill.setReferenceType(SapReferenceTypeEn.Rechnung);
            fiBill.setState("stt3");
            fiBill.setStornoRef("rf");

            List<TSapFiBillposition> arrayList = new ArrayList<>();
            for (int j = 0; j < 1; j++) {
                TSapFiBillposition fiBillposition = new TSapFiBillposition();
                fiBillposition.setTSapFiBill(fiBill);
                fiBillposition.setAmount(10.0);
                fiBillposition.setBaseValue(20.0);
                fiBillposition.setInvoice("Invoice" + j);
                fiBillposition.setNetValue(30.0);
                fiBillposition.setPositionNumber("5678");
                fiBillposition.setReferenceId("refId" + j);
                fiBillposition.setText("Text" + j);

                arrayList.add(fiBillposition);
            }
            fiBill.setSapBillpositions(arrayList);

//        caseDao.getSession().clear();
//        caseDao.getSession().getSessionFactory().getCurrentSession().
//            caseDao.attach(currentCase);
            sapFiBillDao.persist(fiBill);
        }
//        });

        for (int i = 0; i < 1; i++) {
            TSapFiOpenItems fiOpenItem = new TSapFiOpenItems();
            fiOpenItem.setTCase(currentCase);
            fiOpenItem.setCompanyCode("code" + i);
            fiOpenItem.setCurrencyKey("Curky");
            fiOpenItem.setCustomerNumber("custno");
            fiOpenItem.setDebitCreditKey("12233");
            fiOpenItem.setFiscalYear(1);
            fiOpenItem.setKindOfReceipt("rcpt"); //fiOpenItemTmp.blart?
            fiOpenItem.setNetValue(50.0);
            fiOpenItem.setNumberReceipt("Noreceipt"); //fiOpenItemTmp.belnr?
            fiOpenItem.setOrderDateReceipt(new Date()); //fiOpenItemTmp.cpudt?
            fiOpenItem.setPostingKey("pstky");
            fiOpenItem.setReceiptDateReceipt(new Date()); //fiOpenItemTmp.budat?
            fiOpenItem.setRecordingDateReceipt(new Date()); //fiOpenItemTmp.bldat?
            fiOpenItem.setRefNumber("232");
            fiOpenItem.setRefNumberReceipt("453453rgfg"); //fiOpenItemTmp.dzuonr?
            fiOpenItem.setText("dfgfbnkfnfg");
            fiOpenItem.setValue(5.0);

//        caseDao.getSession().clear();
//        caseDao.attach(currentCase);
            sapFiOpenItemsDao.persist(fiOpenItem);
        }

        caseDao.flush();
        caseDao.executeUpdate(CpxDistributor.getBillingDateSql(currentCase.getId()));

    }
     */
    private static SapJob getSapConfig() {
        final CpxServerConfig serverConfig = new CpxServerConfig();
        final Map<Integer, SapJob> sapConfigs = serverConfig.getActiveJobConfigs(SapJob.class);
        final String pDatabase = ClientManager.getActualDatabase();
        SapJob sapConfig = null;
        if (sapConfigs.isEmpty()) {
            throw new IllegalStateException("No SAP configuration found in cpx_server_config.xml");
        }
        if (pDatabase == null || pDatabase.isEmpty()) {
            throw new IllegalStateException("No database defined for SAP-connection");
        }

        for (Iterator<SapJob> itSapJob = sapConfigs.values().iterator(); itSapJob.hasNext();) {
            SapJob sapJob = itSapJob.next();
            if(sapJob != null && sapJob.getTargetDatabase().equalsIgnoreCase(pDatabase)) {
                sapConfig = sapJob;
                break;
            }
        }

        if(sapConfig == null) {
            throw new IllegalStateException("No SAP configuration found in cpx_server_config.xml for database: "+ pDatabase);
        }

        return sapConfig;
    }

    @Override
    public long createNextSendingInkaNumberValue() {
        return inkaDao.createNextSendingInkaNumberValue();
    }

    @Override
    public void updateCase(final String pDatabase, final long pCaseId) {
        if (pCaseId == 0L) {
            return;
        }
        updateCase(pDatabase, new long[]{pCaseId});
    }

    @Override
    @TransactionTimeout(value = 60, unit = TimeUnit.MINUTES)
    public void updateCase(final String pDatabase, final long[] pCaseIds) {
        if (pCaseIds == null || pCaseIds.length == 0) {
            return;
        }
        if (pDatabase == null || pDatabase.trim().isEmpty()) {
            LOG.log(Level.WARNING, "No database passed!");
            return;
        }
        Map<String, Set<String>> caseKeys = caseDao.getCaseKeys(pCaseIds);
        if (caseKeys.isEmpty()) {
            LOG.log(Level.WARNING, "No cases found for passed ids");
            return;
        }
        final SapJob sapConfig = getSapConfig();
        LOG.log(Level.INFO, "Will update cases via SAP now: {0}", String.valueOf(sapConfig));
        for (Map.Entry<String, Set<String>> entry : caseKeys.entrySet()) {
//            final String hosIdent = entry.getKey();
            final Set<String> caseNumbers = entry.getValue();
            //final String caseNumbersConcat = StringUtils.join(caseNumbers, ",");
            //final ImportMode importMode = ImportMode.Version;

            //final Date changeDate = null;
            final ImportProcessSap sapImportProcess = new ImportProcessSap();
            final boolean usePool = false;
            //final ImportConfig<Sap> importConfig = getSapImportConfig();
            //final SapJob sapConfig = importConfig.getModule().getInputConfig();
            final CpxJobConstraints constraint = new CpxJobConstraints(caseNumbers);
            final ImportConfig<Sap> importConfig = getSapImportConfig(sapConfig, constraint);

            try {
                final Date changeDate = cpxServerConfig.getSapChangeDate(sapConfig.getTargetDatabase());
                sapImportProcess.doSapImport(importConfig, changeDate, usePool);

                //*** NOW LEAN BACK AND OBSERVE ***//
                //if (SapImportProcess.USE_JSON_DUMP) {
                //final ImportConfig<Sap> importConfig = new ImportConfig<>(pDatabase, new Sap(fallContainer), importMode, licenseBean.getLicense());
                //final CpxTransformerI transformer = sapImportProcess.createTransformer(importConfig);
                //importConfig.setTransformer(transformer);
                //final DbConfigTest caseDbConfig = getTestDbs().get(DIRK22_ORACLE); //CUSTOMIZE THIS!
                //final DbConfigTest commonDbConfig = getTestDbs().get(CPX_COMMON); //CUSTOMIZE THIS!
                ImportProcessSap process = new ImportProcessSap();
                process.start(importConfig, pDatabase);
//            final String[] args = new String[]{
//                sapConfig.getName(),
//                hosIdent,
//                pDatabase,
//                caseNumbersConcat
//            };
//                SapImportProcessTest.main(args);
            } catch (JCoException | JCoRuntimeException ex) {
                LOG.log(Level.SEVERE, "Cannot establish SAP connection or error occured during updating hospital case with config name: " + sapConfig.getName() + ": " + sapConfig, ex);
                throw new CpxSapException("Cannot establish connection or error occured during updating hospital case with SAP configuration '" + sapConfig.getName() + "':\n" + sapConfig + "\n-> " + ex.getMessage());
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "SQL Exception occured most likely because change date cannot be detected with config name: " + sapConfig.getName() + ": " + sapConfig, ex);
                throw new CpxSapException("SQL Exception occured most likely because change date cannot be detected with SAP configuration '" + sapConfig.getName() + "':\n" + sapConfig + "\n-> " + ex.getMessage());
            } catch (Throwable ex) {
                LOG.log(Level.SEVERE, "Something went definitely wrong with SAP configuration: " + sapConfig.getName() + ": " + sapConfig, ex);
                throw new CpxSapException("Something went definitely wrong with SAP configuration '" + sapConfig.getName() + "':\n" + sapConfig + "\n-> " + ex.getMessage());
            }
        }
//        LOG.log(Level.INFO, "Will send INKA message via SAP now: {0}", String.valueOf(sapConfig));
//        final boolean usePool = false;
//        try (final SapConnection sapConnection = new SapConnection(sapConfig, pKainInka.getHospitalIdentifier(), usePool)) {
//            final String institution = sapConfig.getInstitution();
//            final String fallNr = cs.getCsCaseNumber();
//            //final String handle = (new SecureRandom()).nextInt() + ""; //replace this with incrementing number
////            final long handle = System.nanoTime();
//
//            //create new SendingInkaNumber and set it to the inka (setCurrentNrSending) as well
//            final long handle = inkaDao.createNextSendingInkaNumberValue();
//            inka.setCurrentNrSending(handle);
//            final String kostr = pKainInka.getCostUnitSap();
//            sapConnection.
//            final SapExportResult sapExportResult = sapConnection.sendInkaMessage(institution, fallNr, handle, kostr, inka);
//            if (sapExportResult == null) {
//                LOG.log(Level.INFO, "Tried to send INKA message, but result is null (sent successfully)");
//                //throw new IllegalArgumentException("Cannot send INKA message, result is empty!");
//            } else {
//                LOG.log(Level.INFO, "Tried to send INKA message: " + sapExportResult.getM_text() + ", " + sapExportResult.getM_warnings() + ", " + sapExportResult.getState());
//                throw new IllegalArgumentException("Cannot send INKA message! Text: " + sapExportResult.getM_text() + ", Warnings: " + sapExportResult.getM_warnings() + ", State: " + sapExportResult.getState());
//            }
//        } catch (JCoException | JCoRuntimeException ex) {
//            LOG.log(Level.SEVERE, "Cannot establish SAP connection or error occured during sending INKA message with config name: " + sapConfig.getName() + ": " + String.valueOf(sapConfig), ex);
//            throw new IllegalArgumentException("Cannot establish connection or error occured during sending INKA message with SAP configuration '" + sapConfig.getName() + "':\n" + String.valueOf(sapConfig) + "\n-> " + ex.getMessage());
//        }
    }

    private CCase getSerializedCase(final long pCaseId) {
        TCase cs = caseDao.getCaseForSerialization(pCaseId);
        if (cs == null) {
            return null;
        }
        return getSerializedCase(cs);
//        String serializedData;
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            serializedData = objectMapper.writeValueAsString(cs);
//            //objectMapper.writeValue(new File("target/car.json"), cs);
//        } catch (JsonProcessingException ex) {
//            LOG.log(Level.SEVERE, "Cannot serialize case: " + cs, ex);
//            return null;
//        }
//        Long userId = ClientManager.getCurrentCpxUserId();
//        String database = ClientManager.getActualDatabase();
//        Date now = new Date();
//
//        final CCase commonCs = new CCase();
//        commonCs.setCreationDate(now);
//        commonCs.setCreationUser(userId);
//        commonCs.setCaseId(pCaseId);
//        commonCs.setCsHospitalIdent(cs.getCsHospitalIdent());
//        commonCs.setCsCaseNumber(cs.getCsCaseNumber());
//        commonCs.setName(cs.getCsCaseNumber());
//        commonCs.setDb(database);
//        final String charset = CpxSystemProperties.DEFAULT_ENCODING;
//        try {
//            commonCs.setContent(serializedData.getBytes(charset));
//        } catch (UnsupportedEncodingException ex) {
//            LOG.log(Level.SEVERE, "Cannot apply charset to getBytes: " + charset, ex);
//            return null;
//        }
//        return commonCs;
    }

    private CCase getSerializedCase(final TCase cs) {
//        TCase cs = caseDao.getCaseForSerialization(pCaseId);
//        if (cs == null) {
//            return null;
//        }
        String serializedData;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            serializedData = objectMapper.writeValueAsString(cs);
            //objectMapper.writeValue(new File("target/car.json"), cs);
        } catch (JsonProcessingException ex) {
            LOG.log(Level.SEVERE, "Cannot serialize case: " + cs, ex);
            return null;
        }
        Long userId = ClientManager.getCurrentCpxUserId();
        String database = getDatabaseName();
        Date now = new Date();

        final CCase commonCs = new CCase();
        commonCs.setCreationDate(now);
        commonCs.setCreationUser(userId);
        commonCs.setCaseId(cs.getId());
        commonCs.setCsHospitalIdent(cs.getCsHospitalIdent());
        commonCs.setCsCaseNumber(cs.getCsCaseNumber());
        commonCs.setName(cs.getCsCaseNumber());
        commonCs.setDb(database);
        final String charset = CpxSystemProperties.DEFAULT_ENCODING;
        try {
            commonCs.setContent(serializedData.getBytes(charset));
        } catch (UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, "Cannot apply charset to getBytes: " + charset, ex);
            return null;
        }
        return commonCs;
    }

    public String getDatabaseName() {
        String database = ClientManager.getActualDatabase();
        if (database == null || database.isEmpty()) {
            return "n/a";
        }
        return database;
    }

    @Override
    public int copyTCaseToCommon(long[] pCaseIds) {
//        //int result = 0;
//        final HashSet<Long> result = new HashSet<>();
//        if (pCaseIds == null || pCaseIds.length == 0) {
//            return result.size();
//        }
//        for (long caseId : pCaseIds) {
//            CCase commonCs = getSerializedCase(caseId);
//            if (commonCs == null) {
//                continue;
//            }
//            commonCaseDao.persist(commonCs);
//            result.add(commonCs.id);
//            //result++;
////            commonCaseDao.flush();
////            loadTCaseFromCommon(commonCs.id);
//        }
//        if (result.size() > 0) {
//            final long[] tmp = new long[result.size()];
//            int i = 0;
//            Iterator<Long> it = result.iterator();
//            while (it.hasNext()) {
//                tmp[i] = it.next();
//                i++;
//            }
//            broadcast.send(BroadcastOriginEn.CASE_TO_COMMON, "Es wurden " + result.size() + " neue Fälle für den Regeleditor bereitgestellt", tmp);
//        }
//        return result.size();
        return copyTCaseToCommon(pCaseIds, null);
    }

    @Override
    public int copyTCaseToCommon(TCase[] pCases, String pCategory) {
        //int result = 0;
        pCategory = Objects.requireNonNullElse(pCategory, Lang.getNoCategory());
        final HashSet<Long> result = new HashSet<>();
        if (pCases == null || pCases.length == 0) {
            return result.size();
        }
        for (TCase hospCase : pCases) {
            CCase commonCs = getSerializedCase(hospCase);
            if (commonCs == null) {
                continue;
            }
            commonCs.setCategory(pCategory);
            commonCaseDao.persist(commonCs);
            result.add(commonCs.id);
            //result++;
//            commonCaseDao.flush();
//            loadTCaseFromCommon(commonCs.id);
        }
        if (!result.isEmpty()) {
            final long[] tmp = new long[result.size()];
            int i = 0;
            Iterator<Long> it = result.iterator();
            while (it.hasNext()) {
                tmp[i] = it.next();
                i++;
            }
            broadcast.send(BroadcastOriginEn.CASE_TO_COMMON, "Es wurden " + result.size() + " neue Fälle für den Regeleditor bereitgestellt", tmp);
        }
        return result.size();
    }

    @Override
    public int copyTCaseToCommon(long[] pCaseIds, String pCategory) {
        //int result = 0;
        pCategory = Objects.requireNonNullElse(pCategory, Lang.getNoCategory());
        final HashSet<Long> result = new HashSet<>();
        if (pCaseIds == null || pCaseIds.length == 0) {
            return result.size();
        }
        for (long caseId : pCaseIds) {
            CCase commonCs = getSerializedCase(caseId);
            if (commonCs == null) {
                continue;
            }
            commonCs.setCategory(pCategory);
            commonCaseDao.persist(commonCs);
            result.add(commonCs.id);
            //result++;
//            commonCaseDao.flush();
//            loadTCaseFromCommon(commonCs.id);
        }
        if (!result.isEmpty()) {
            final long[] tmp = new long[result.size()];
            int i = 0;
            Iterator<Long> it = result.iterator();
            while (it.hasNext()) {
                tmp[i] = it.next();
                i++;
            }
            broadcast.send(BroadcastOriginEn.CASE_TO_COMMON, "Es wurden " + result.size() + " neue Fälle für den Regeleditor bereitgestellt", tmp);
        }
        return result.size();
    }

    @Override
    public List<CCase> getAllCCases() {
        return commonCaseDao.findAllCases();
    }

    @Override
    public Map<Date, List<CCase>> getCCaseDateDistribution() {
        final Map<Date, List<CCase>> result = new LinkedHashMap<>();
        List<CCase> list = commonCaseDao.findAllCases();
        Collections.sort(list, new Comparator<CCase>() {
            @Override
            public int compare(CCase o1, CCase o2) {
                if (o1.getCreationDate() == null) {
                    return 1;
                }
                if (o2.getCreationDate() == null) {
                    return -1;
                }
                return o1.getCreationDate().compareTo(o2.getCreationDate());
            }
        });
        for (CCase cs : list) {
            if (cs.getCreationDate() == null) {
                continue;
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(cs.getCreationDate());
            //int year = cal.get(Calendar.YEAR);
            //int month = cal.get(Calendar.MONTH) + 1;
            //int day = cal.get(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date dt = cal.getTime();
            List<CCase> val = result.get(dt);
            if (val == null) {
                val = new ArrayList<>();
                result.put(dt, val);
            }
            val.add(cs);
        }
        return result;
    }

    @Override
    public CCase loadCCaseFromCommon(final long id) {
        return commonCaseDao.findById(id);
    }

    @Override
    public TCase loadTCaseFromCommon(final long id) {
        //CCase commonCs = commonCaseDao.findByCaseId(pCaseId);
        CCase commonCs = commonCaseDao.findById(id);
        if (commonCs == null) {
            return null;
        }
        byte[] content = commonCs.getContent();
        if (content == null || content.length == 0) {
            return null;
        }
        TCase cs;
        try {
            cs = new ObjectMapper().readValue(content, TCase.class);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot unserialize case from common case with id " + commonCs.id + " (case id is " + commonCs.getCaseId() + ")", ex);
            return null;
        }
        return cs;
    }

    @Override
    public int getDocumentsSizeMax() {
        return cpxServerConfig.getDocumentsSizeMax();
    }

    @Override
    public boolean processExists(long pId) {
        return processDao.exists(pId);
    }

    @Override
    public boolean hasPossibleKain6WeekDeadline(long pCaseId) {
        return processDao.hasPossibleKain6WeekDeadline(pCaseId);
    }

    @Override
//<<<<<<< .mine
//*    public List<MdkAuditQuotaDTO> getAuditQuotas(final String pHospitalIdent, final String pInsuranceIdent, final Integer pYear, final Integer pQuarter, final BigDecimal pQuote) {
//        return riskDao.getAuditQuotas(pHospitalIdent, pInsuranceIdent, pYear, pQuarter, pQuote);
//        if (result.isEmpty()) {
//            LOG.log(Level.WARNING, "no audit quote for hospital ident {0}, insurance ident {1}, year = {2} and quartal = {3}", new Object[]{pHospitalIdent, pInsuranceIdent, pYear, pQuartal});
//            return null;
//        }
//        if (result.size() > 1) {
//            LOG.log(Level.WARNING, "audit quote for hospital ident {0}, insurance ident {1}, year = {2} and quartal = {3} is ambigious, {4} entries found", new Object[]{pHospitalIdent, pInsuranceIdent, pYear, pQuartal, result.size()});
//        }
//        return result.iterator().next();
//||||||| .r9913
//    public List<MdkAuditQuotaDTO> getAuditQuotas(final String pHospitalIdent, final String pInsuranceIdent, final Integer pYear, final Integer pQuarter) {
//        return riskDao.getAuditQuotas(pHospitalIdent, pInsuranceIdent, pYear, pQuarter);
//        if (result.isEmpty()) {
//            LOG.log(Level.WARNING, "no audit quote for hospital ident {0}, insurance ident {1}, year = {2} and quartal = {3}", new Object[]{pHospitalIdent, pInsuranceIdent, pYear, pQuartal});
//            return null;
//        }
//        if (result.size() > 1) {
//            LOG.log(Level.WARNING, "audit quote for hospital ident {0}, insurance ident {1}, year = {2} and quartal = {3} is ambigious, {4} entries found", new Object[]{pHospitalIdent, pInsuranceIdent, pYear, pQuartal, result.size()});
//        }
//        return result.iterator().next();
//=======
    public List<MdkAuditComplaintsDTO> getMdkAuditComplaints(final String pHospitalIdent, final String pInsuranceIdent, final Integer pBillYear, final Integer pBillQuartal, final Integer pAuditYear, final Integer pAuditQuartal) {
        return riskDao.getMdkAuditComplaints(pHospitalIdent, pInsuranceIdent, pBillYear, pBillQuartal, pAuditYear, pAuditQuartal);
//>>>>>>> .r9928
    }

    @Override
    public CMdkAuditquota getMdkAuditQuota(final String pHospitalIdent, final int pYear, final int pQuarter) {
        return mdkAuditquotaDao.getMdkAuditQuota(pHospitalIdent, pYear, pQuarter);
    }

    @Override
    public List<TWmMdkAuditReasons> findAuditReasons4case(Long pCaseId) {
        return requestDao.findAuditReasons4case( pCaseId);
    }
    
    @Override
    public TWmRequest getLatestRequest4Case(long pCaseId){
        return requestDao.getLatestRequest4Case( pCaseId);
    }

    @Override
    public boolean isDetailBasicForProcess(long pCaseDetailsId) {
        return processCaseDao.getProcessForKisDetails(pCaseDetailsId) != null;
    }

    @Override
    public TCaseDetails findCurrentKisDetailsVersionForProcess(long pProcessId) {
        TCaseDetails kisDetail = processCaseDao.findCurrentKisDetailsVersionForProcess(pProcessId);
        if(kisDetail != null){
            Hibernate.initialize(kisDetail.getHospitalCase());
        }
        return kisDetail;
    }
    
    @Override
        public int getOpenReminderCount(long pProcessId){
            return reminderDao.findCountForProcess(pProcessId, Boolean.FALSE); 
        }

    @Override
    public boolean isDocumentToArchivateafterImport() {
        return !cpxServerConfig.getDocumentsNotArchivate();
    }

    @Override
    public boolean sendMDStateToSAP(final TWmRequest pRequest) throws IllegalArgumentException {

        if (pRequest == null) {
            LOG.log(Level.FINEST, "Passed TWmRequest object cannot be null null!");
            return false;
        }
        if (!(pRequest instanceof TWmRequestMdk)) {
            throw new IllegalArgumentException("Passed object is not an MD reuqest");
        }
        TWmRequestMdk requestMDK = (TWmRequestMdk) pRequest;
        final boolean usePool = false;
        
        final TCase cs = pRequest.getProcessHospital().getMainCase();
        final ImportConfig<Sap> importConfig = getSapImportConfig();
        final SapJob sapConfig = importConfig.getModule().getInputConfig();
        LOG.log(Level.INFO, "Will send MD-Request-State to SAP now: {0}", String.valueOf(sapConfig));

        final String institution = sapConfig.getInstitution();
        final String fallNr = cs.getCsCaseNumber();
        final Long processNumber = pRequest.getProcessHospital().getWorkflowNumber();
        final String requestStateText = getRequesrSateById(requestMDK.getRequestState());
        final String user = getUserById(ClientManager.getCurrentCpxUserId());
        try (final SapConnection sapConnection = new SapConnection(importConfig /*, pKainInka.getHospitalIdentifier() */, usePool)) {
            final SapExportResult sapExportResult = sapConnection.setMDStateToSAP(institution, fallNr, processNumber, requestMDK.getRequestState(), 
                                                        requestStateText, user);
            if (sapExportResult == null) {
                LOG.log(Level.INFO, "Tried to send MD state to SAP, but result is null (sent successfully)");
                //throw new IllegalArgumentException("Cannot send INKA message, result is empty!");
            } else {
                if(sapExportResult.getState() == 0) {
                    LOG.log(Level.INFO, "Tried to send MD state to SAP, but result is null (sent successfully)");
                } else {
                    LOG.log(Level.INFO, "Tried to send MD state to SAP: " + sapExportResult.getText() + ", " + sapExportResult.getWarnings() + ", " + sapExportResult.getState());
                    throw new IllegalArgumentException("Cannot send MD state to SAP! Text: " + sapExportResult.getText() + ", Warnings: " + sapExportResult.getWarnings() + ", State: " + sapExportResult.getState());
                }
            }
        } catch (JCoException | JCoRuntimeException ex) {
            LOG.log(Level.SEVERE, "Cannot establish SAP connection or error occured during sending MD state to SAP with config name: " + sapConfig.getName() + ": " + String.valueOf(sapConfig), ex);
            throw new IllegalArgumentException("Cannot establish connection or error occured during sending MD state to SAP with SAP configuration '" + sapConfig.getName() + "':\n" + String.valueOf(sapConfig) + "\n-> " + ex.getMessage());
        }
        return true;
    }

    @Override
    public boolean isSendMDStateToSAPActive() {
        return cpxServerConfig.getSapSendMDStateToSAP();
    }

}
