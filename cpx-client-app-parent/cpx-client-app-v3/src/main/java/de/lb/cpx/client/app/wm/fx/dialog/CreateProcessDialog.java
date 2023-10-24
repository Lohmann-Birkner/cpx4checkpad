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
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRequestAudit;
import de.lb.cpx.wm.model.TWmRequestMdk;
import de.lb.cpx.wm.model.enums.WmRequestTypeEn;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import de.lb.cpx.wm.model.enums.WmWorkflowTypeEn;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 * Creates a new AddRequest dialog, creates Request AND Process in database
 *
 * @author wilde
 */
public class CreateProcessDialog extends AddRequestDialog {

    /**
     * constructor sets up Creates new Process Dialog
     *
     * @param title dialog title
     * @param hCaseId case id to add the process to
     * @param facade service facade to access server services
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException case does not
     * exist
     */
    public CreateProcessDialog(String title, long hCaseId, ProcessServiceFacade facade) throws CpxIllegalArgumentException {
        super(facade, hCaseId);
//        super(facade);
        setTitle(title);
        Stage s = (Stage) this.getDialogPane().getScene().getWindow();
        s.setMinHeight(650);
        s.setMinWidth(750);
        s.setMaxWidth(500);
        setOnSave(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addRequestProperty.setValue(Boolean.TRUE);
                addRequestProperty.setValue(Boolean.FALSE);

                TWmRequest request = onSave();  // get the created request (of any type) from the RequestDialog.

                if (request != null) {
                    //creates a new process of type Hospital (or type Tcase)
                    TWmProcessHospital process = new TWmProcessHospital();

                    TCase cs = facade.findHospitalCase(hCaseId);

//                    List<CWmListProcessTopic> availableProcessTopics = facade.getAvailableProcessTopics();    //gives all process topics without any filters
                    //TODO: USE MENUCACHE INSTEAD HERE?!
                    //List<CWmListProcessTopic> availableProcessTopics = processServiceBean.getAllAvailableProcessTopics(new Date()); // gives valid, undeleted, sorted (based on wm_pt_sort column) and within a valid timeframe process topics
//                    process.setSubject(availableProcessTopics.get(0).getWmPtName());    //it's a subject for all types of the processes!!
                    //process.setProcessTopic(availableProcessTopics.isEmpty() ? null : availableProcessTopics.get(0).getWmPtInternalId());    //set the first (sorted) process topic to the process of type hospital
                    //final Long topicId = processServiceBean.getFirstAvailableProcessTopicInternalId();
                    final Long topicId = MenuCache.getMenuCacheProcessTopics().getFirstKey();
                    process.setProcessTopic(topicId);    //set the first (sorted) process topic to the process of type hospital

                    process.setWorkflowState(WmWorkflowStateEn.offen);
                    process.setWorkflowType(WmWorkflowTypeEn.statKH);
                    process.setPatient(cs.getPatient());
                    process.setMainCase(cs);
                    process.setAssignedUser(Session.instance().getCpxUserId()); //Is always set during creation of the "Vorgang"
                    process.setProcessModificationUser(Session.instance().getCpxUserId());

//                    DeadlineList listofDeadlines = new DeadlineList(Session.instance().getEjbConnector().connectProcessServiceBean().get().getAllDeadlines());
                    //20191230-AWi: is this intended? only checking if bill is past 6 weeks?
                    facade.checkBillFor6WeeksDeadline(cs);
                    if (request.getRequestTypeEnum().equals(WmRequestTypeEn.mdk)) {
                        TWmRequestMdk mdkreq = (TWmRequestMdk) request;
                        process.setMdkBillCorrectionDeadline(mdkreq.getBillCorrectionDeadline());
                        process.setMdkDocDeliverDeadline(mdkreq.getMdkDocumentDeliverDeadline());
                        process.setMdkAuditCompletionDeadline(mdkreq.getMdkAuditCompletionDeadline());
                    } else if (request.getRequestTypeEnum().equals(WmRequestTypeEn.audit)) {
                        TWmRequestAudit auditReq = (TWmRequestAudit) request;
                        process.setAuditDataRecordCorrectionDeadline(auditReq.getDataRecordCorrectionDeadline());
                        process.setAuditPrelProcAnsDeadline(auditReq.getAnswerDeadline());
                        process.setAuditPrelProcClosedDeadline(auditReq.getPreliminaryProceedingsClosedDeadline());
                    }
                    process = (TWmProcessHospital) facade.storeProcess(process);    // store process
                    //RSh-20191031: CPX-2032 storing of request should be  before storing of Reminders
                    request = facade.storeRequest(request); // store request
                    //AWi-20190919: move storing of request further down,
                    //this will avoid the creation of two separate events in process!
                    //attention : but causes errors when saving Reminders(RSH: 20191031)
//                    process.getRequests().add(request); // add this request to particular process
//                    request.setProcessHospital(process);

                    if (request.getRequestTypeEnum().equals(WmRequestTypeEn.mdk)) {
                        List<TWmReminder> reminders = getMdkReminders();
//                        facade.storeMDKRequestReminders(request, reminders);
                        if (request.isRequestMdk() && getMdkReminders() != null) {
                            for (int i = 0; i < reminders.size(); i++) {
                                reminders.get(i).setProcess(facade.getCurrentProcess());
                                process.getReminders().add(reminders.get(i));
                                process.getEvents().add(
                                        facade.storeReminderForRequest(reminders.get(i), request));
                            }

                        }
//                        process = (TWmProcessHospital) processServiceBean.storeProcessForMdkReminders(process, request);
                    }
                    if (request.isRequestAudit()) {
                        List<TWmReminder> reminders = getAuditReminders();
//                        facade.storeMDKRequestReminders(request, reminders);
                        if (getAuditReminders() != null) {
                            for (int i = 0; i < reminders.size(); i++) {
                                reminders.get(i).setProcess(facade.getCurrentProcess());
                                process.getReminders().add(reminders.get(i));
                                process.getEvents().add(
                                        facade.storeReminderForRequest(reminders.get(i), request));
                            }

                        }
//                        process = (TWmProcessHospital) processServiceBean.storeProcessForMdkReminders(process, request);
                    }
                    //store request, update process with new request
                    //RSh-20191031: move storing of request this will avoid the creation of two separate events in process!
//                    facade.storeRequest(request);
                    facade.updateProcess(process);
//                    facade.createOrUpdateRiskData(request);
////////******************************
//                    //AWi-20170612-CPX-542:
//                    //set result to observe new value
                    setResults(request);
                } else if (validationSupport.isInvalid()) {
                    showValidationErrors();
                }
            }
        }
        );
    }

}
