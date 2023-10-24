/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.editor.AuditRequestEditor;
import de.lb.cpx.client.app.wm.fx.dialog.editor.BasicRequestEditor;
import de.lb.cpx.client.app.wm.fx.dialog.editor.BegeRequestEditor;
import de.lb.cpx.client.app.wm.fx.dialog.editor.InsuranceRequestEditor;
import de.lb.cpx.client.app.wm.fx.dialog.editor.MdkRequestEditor;
import de.lb.cpx.client.app.wm.fx.dialog.editor.OtherRequestEditor;
import de.lb.cpx.client.app.wm.fx.dialog.editor.ReviewRequestEditor;
import de.lb.cpx.client.app.wm.util.auditquota.MdkAuditQuotaResult;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.connector.EjbConnector;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.enums.DeadlineTypeEn;
import de.lb.cpx.server.commonDB.model.CDeadline;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRequestAudit;
import de.lb.cpx.wm.model.TWmRequestBege;
import de.lb.cpx.wm.model.TWmRequestInsurance;
import de.lb.cpx.wm.model.TWmRequestMdk;
import de.lb.cpx.wm.model.TWmRequestOther;
import de.lb.cpx.wm.model.TWmRequestReview;
import de.lb.cpx.wm.model.enums.WmRequestTypeEn;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.util.Callback;
import javax.validation.constraints.NotNull;
import org.controlsfx.validation.ValidationSupport;

/**
 * create and show request creation dialog and request update dialog, handles
 * all necessary functions to create a new or update existing request entity
 *
 * @author nandola
 */
//public class RequestLayout extends FormularDialog<TWmRequest> {
public class RequestLayout {

    private static final Logger LOG = Logger.getLogger(RequestLayout.class.getSimpleName());
    protected BooleanProperty addRequestProperty = new SimpleBooleanProperty(false);
    protected final ProcessServiceFacade facade;
//    private final LabeledTextArea taComment;
//    private LabeledTextField tfInsuranceName;
//    private LabeledTextField tfInsuranceCity;
//    private LabeledTextField tfInsuranceIdent;
//    private LabeledTextField tfInsuranceZipCode;
//    private LabeledLabel labelAddress;
//    private LabeledLabel labelTelephoneNumber;
//    private LabeledLabel labelFax;
//    private LabeledTextField tfContactPerson;
//    private LabeledTextField tfExtensionNo;
//    private LabeledTextField tfFaxNo;
//    private final CpxInsuranceCompanyCatalog insuranceCatalog;
//    private Label labelAddData;
//    private Label labelInsurance;
    private final EjbProxy<ProcessServiceBeanRemote> processServiceBean;
    private TWmProcess currentProcess;
//    private Date caseAdmissionDate = null;
    private Long caseId = null;
    private final TWmRequest request;
//    private TWmRequestBege begeReq;
    private WmRequestTypeEn requestType;
//    private String begeInsIdent;
    private Window ownerWindow;
//    private ValidationSupport validationSupport;
    private MdkRequestEditor mdkEditor;
    private AuditRequestEditor auditEditor;
    private InsuranceRequestEditor insEditor;
    private OtherRequestEditor otherEditor;
    private ReviewRequestEditor reviewEditor;
    private final TCase mainCase;
//    private final TCaseDetails externDetails;
    private BegeRequestEditor begeEditor;
    private boolean checkQuota = false;

    /**
     * build request layout based on Request type.
     *
     * @param pFacade process service facade
     * @param request request
     * @param pCaseId caseId
     */
    public RequestLayout(ProcessServiceFacade pFacade, TWmRequest request, long pCaseId) {
//        super(MainApp.getStage(), Modality.APPLICATION_MODAL, Lang.getMdkCreateRequestWindow());
        LOG.log(Level.INFO, "Open request dialog for case id {0}...", pCaseId);
        facade = pFacade;
//        insuranceCatalog = CpxInsuranceCompanyCatalog.instance(); 
        Session session = Session.instance();
        EjbConnector connector = session.getEjbConnector();
        processServiceBean = connector.connectProcessServiceBean();
        caseId = pCaseId;
//        taComment = new LabeledTextArea(Lang.getComment(), LabeledTextArea.REQUEST_COMMENT_SIZE);
//        taComment.setPrefHeight(100);
        this.request = request;
        if (facade.getCurrentProcess() != null) {
            currentProcess = facade.getCurrentProcess();    // this can give the process If we create new request from the process details view or update a request.
            //processCreationDate = currentProcess.getCreationDate();
            mainCase = currentProcess.getMainCase();
//            externDetails = mainCase == null ? null : facade.getCurrentExtern(mainCase.id);  // current kis-version (Extern version)
        } else {
            LOG.log(Level.INFO, "current process is null...");
            //processCreationDate = new Date();
            mainCase = processServiceBean.get().getMainCase(caseId);  // this hcaseId is consider as the mainCaseId when we create a new process from the Case list view.
//            externDetails = mainCase == null ? null : mainCase.getCurrentExtern();  // current kis-version (Extern version)
        }
//        if (mainCase == null ||externDetails == null) {
//            LOG.log(Level.WARNING, "mainCase or external case details is null! case with id " + hCaseId + " does not seem to exist!");
//            MainApp.showErrorMessageDialog(Lang.getProcessMainCaseError(),getOwnerWindow());
////            throw new CpxIllegalArgumentException(Lang.getProcessMainCaseError()); 
//        }
//        if (externDetails == null) {
//            LOG.log(Level.WARNING, "external case details is null!");
//             MainApp.showErrorMessageDialog(Lang.getProcessMainCaseError());
//              MainApp.showErrorMessageDialog(Lang.getProcessMainCaseError());
////            throw new CpxIllegalArgumentException("Der Fall hat keine externen Falldetails!");
//        }
    }

    /**
     * get the addRequestProperty to inform a observer that a new request could
     * be obtained
     *
     * @return boolean property
     */
    public BooleanProperty getAddRequestProperty() {
        return addRequestProperty;
    }

    /**
     * creates the request object with the current set user input, null if the
     * UI is not shown yet
     *
     * @return request entity object
     */
    private TWmRequest getRequestObject() {
         TWmRequest retReq = null;
        if (request != null && request.getRequestTypeEnum() != null) {

            switch (request.getRequestTypeEnum()) {
                case bege:
                    if(begeEditor != null){
                        retReq = begeEditor.getUpdatedRequest();
                    }
//                    begeReq = (TWmRequestBege) request;
//                    begeReq.setInsuranceIdentifier(tfInsuranceIdent.getText());
//                    begeReq.setComment(taComment.getText());
//                    begeReq.setBegeEditor(tfContactPerson.getText());
//                    begeReq.setDirectPhone(tfExtensionNo.getText());
//                    begeReq.setDirectFax(tfFaxNo.getText());
//                    retReq = begeReq;
                    break;
                case mdk:
                    if (mdkEditor != null) {
                        retReq = mdkEditor.getUpdatedRequest();
                    }
                    break;
                case audit:
                    if (auditEditor != null) {
                        retReq = auditEditor.getUpdatedRequest();
                    }
                    break;
                case insurance:
                    if (insEditor != null) {
                        retReq = insEditor.getUpdatedRequest();
                    }
                    break;
                case other:
                    if (otherEditor != null) {
                        retReq = otherEditor.getUpdatedRequest();
                    }
                    break;
                case review:
                    if(reviewEditor != null){
                        retReq = reviewEditor.getUpdatedRequest();
                    }
                    break;
                default:
                    LOG.log(Level.INFO, "Can not update unknown requestType {0}", request.getRequestTypeEnum());
                    throw new IllegalArgumentException("This is an in valid request type: " + request.getRequestTypeEnum());
            }
        } else {

            switch (requestType) {
                case bege:
//                    TWmRequestBege begereq = new TWmRequestBege();
//                    begereq.setInsuranceIdentifier(tfInsuranceIdent.getText());
//                    begereq.setComment(taComment.getText());
//                    begereq.setBegeEditor(tfContactPerson.getText());
//                    begereq.setDirectPhone(tfExtensionNo.getText());
//                    begereq.setDirectFax(tfFaxNo.getText());
//                    begereq.setKisDetails(mainCase.getCurrentExtern());
//                    retReq = begereq;
                    if(begeEditor != null){
                        retReq = begeEditor.getUpdatedRequest();
                    }
                    break;
                case mdk:
                    if (mdkEditor != null) {
                        retReq = mdkEditor.getUpdatedRequest();
                        
                    }
                    break;
                case audit:
                    if (auditEditor != null) {
                        retReq = auditEditor.getUpdatedRequest();
                    }
                    break;
                case insurance:
                    if (insEditor != null) {
                        retReq = insEditor.getUpdatedRequest();
                    }
                    break;
                case other:
                    if (otherEditor != null) {
                        retReq = otherEditor.getUpdatedRequest();
                    }
                    break;
                case review:
                    if(reviewEditor != null){
                        retReq = reviewEditor.getUpdatedRequest();                      
                    }
                    break;
                default:
                    LOG.log(Level.INFO, "Can not save unknown requestType {0}", requestType.toString());
                    throw new IllegalArgumentException("This is an in valid request type: " + requestType);
            }
        }
        if(retReq != null && mainCase != null){
            retReq.setKisDetails(mainCase.getCurrentExtern());
        }
        return retReq;


    }

    //check audit quota
    private MdkAuditQuotaResult getAuditQuotaResult(final TWmRequest pRequest) {
        if (pRequest == null || !pRequest.isRequestMdk()) {
            return null;
        }
        if (mainCase == null) {
            LOG.log(Level.WARNING, "cannot check audit quota, because main case does not exist");
            return null;
        }
        return facade.getAuditQuotaResult(mainCase, (TWmRequestMdk)pRequest);
    }

    /**
     * This method will call with the OK button to update the request and/or
     * process.
     *
     * @return request object
     */
    public TWmRequest onSave() {
        BasicRequestEditor<?> editor = getEditor();
        if(getEditor()!=null){
            return onSave(editor.isEditable());
        }
        return onSave(true);
    }
    public BasicRequestEditor<?> getEditor(){
        if(requestType == null){
            LOG.severe("Can not get Editor! No RequestType set!");
            return null;
        }
        switch (requestType) {
            case audit:
                return auditEditor;
            case bege:
                return begeEditor;
            case insurance:
                return insEditor;
            case mdk:
                return mdkEditor;
            case other:
                return otherEditor;
            case review:
                return reviewEditor;
            default:

                LOG.warning("Can not get Editor! Unknown RequestType set!\nType is: " + requestType.getClass().getName());
                return null;
        }
    }
    private void checkAuditQuota(final TWmRequest pRequest) {
        if (pRequest == null || pRequest.id != 0L) {
            //only check MD audit quota for brandly new created MD requests
            return;
        }
        final MdkAuditQuotaResult result = getAuditQuotaResult(pRequest);
        if (result != null) {
            if (result.isQuotaExceeded()) 
            {
//                MainApp.showWarningMessageDialog(Lang.getAuditQuotaResultWarning(quotaValues));
                new QuotaInfoDialog(false, result).showAndWait();
                if (pRequest instanceof TWmRequestMdk) {
                    ((TWmRequestMdk) pRequest).setMdkRequestQuotaExceededFl(TWmRequestMdk.QUOTA_EXCEEDED);
                }
            } else {
                if(Session.instance().showAlwaysInfoForExaminateQuota()) {
//                    MainApp.showInfoMessageDialog(Lang.getAuditQuotaResultInformation(quotaValues));
                    new QuotaInfoDialog(true, result).showAndWait();
                }
                if (pRequest instanceof TWmRequestMdk) {
                    ((TWmRequestMdk) pRequest).setMdkRequestQuotaExceededFl(TWmRequestMdk.QUOTA_NOT_EXCEEDED);
                }
            }
            if (pRequest instanceof TWmRequestMdk) {
                ((TWmRequestMdk) pRequest).setMdkQuotaHospitalIdent(result.hospitalIdent);
                ((TWmRequestMdk) pRequest).setMdkQuotaInsuranceIdent(result.insuranceIdent);
                ((TWmRequestMdk) pRequest).setMdkQuotaQuarter(result.quarter);
                ((TWmRequestMdk) pRequest).setMdkQuotaYear(result.year);
                ((TWmRequestMdk) pRequest).setMdkQuotaInPatientCaseCount(result.caseCount);
                ((TWmRequestMdk) pRequest).setMdkQuotaActComplInpatCase(result.actualComplaints);
                ((TWmRequestMdk) pRequest).setMdkMaxComplaintsForQuota(result.givenComplaints);
                ((TWmRequestMdk) pRequest).setMdkGivenQuotaForQuarter(result.givenQuota);
            }
        }
    }

    /**
     * This method will call with the OK button to update the request and/or
     * process.
     *
     * @param pStore store to database
     * @return request object
     */
    public TWmRequest onSave(final boolean pStore) {
        TWmRequest reqObj = getRequestObject();

        //wenn Vorgang schon vorhanden 
        if (facade.getCurrentProcess() != null) {
             int processChecked = 3;// there are 3 date fields of process, that are filled from request. 
            int processReviewChecked = 5;// there are 3 date fields of process, that are filled from request. 
            TWmProcessHospital process = (TWmProcessHospital) facade.getCurrentProcess();
            //Request ist schon vorhanden
            if (reqObj != null && reqObj.getRequestTypeEnum() != null) {
                if (reqObj instanceof TWmRequestMdk) {
                    if (isCheckQuota()) {
                        checkAuditQuota(reqObj);
                    }
                }
                if (reqObj.getCreationUser() == null) {
                    reqObj.setCreationUser(Session.instance().getCpxUserId());
                }

                switch (reqObj.getRequestTypeEnum()) {
                    case mdk:
                        TWmRequestMdk mdkreq = (TWmRequestMdk) reqObj;
                        if (process.getMdkBillCorrectionDeadline() != null && mdkreq.getBillCorrectionDeadline() != null) {
                            process.setMdkBillCorrectionDeadline(process.getMdkBillCorrectionDeadline().after(mdkreq.getBillCorrectionDeadline()) ? mdkreq.getBillCorrectionDeadline() : process.getMdkBillCorrectionDeadline());
                        } else if (mdkreq.getBillCorrectionDeadline() != null) {
                            process.setMdkBillCorrectionDeadline(mdkreq.getBillCorrectionDeadline());
                        }else{
                            processChecked--;
                        }
                        if (process.getMdkDocDeliverDeadline() != null && mdkreq.getMdkDocumentDeliverDeadline() != null) {
                            process.setMdkDocDeliverDeadline(process.getMdkDocDeliverDeadline().after(mdkreq.getMdkDocumentDeliverDeadline()) ? mdkreq.getMdkDocumentDeliverDeadline() : process.getMdkDocDeliverDeadline());

                        } else if (mdkreq.getMdkDocumentDeliverDeadline() != null) {
                            process.setMdkDocDeliverDeadline(mdkreq.getMdkDocumentDeliverDeadline());
                        }else{
                            processChecked--;
                        }
                        if (process.getMdkAuditCompletionDeadline() != null && mdkreq.getMdkAuditCompletionDeadline() != null) {
                            process.setMdkAuditCompletionDeadline(process.getMdkAuditCompletionDeadline().after(mdkreq.getMdkAuditCompletionDeadline()) ? mdkreq.getMdkAuditCompletionDeadline() : process.getMdkAuditCompletionDeadline());
                        } else if (mdkreq.getMdkAuditCompletionDeadline() != null) {
                            process.setMdkAuditCompletionDeadline(mdkreq.getMdkAuditCompletionDeadline());
                        }else{
                            processChecked--;
                        }
//                        if (pStore) {
//                            facade.updateProcess(process);
//                        }
                        Session.instance().setRecentMdkInternalId(mdkreq.getMdkInternalId());

                        break;
                    case audit:
                        TWmRequestAudit audirreq = (TWmRequestAudit) reqObj;
                        if (process.getAuditDataRecordCorrectionDeadline() != null && audirreq.getDataRecordCorrectionDeadline() != null) {
                            process.setAuditDataRecordCorrectionDeadline(process.getAuditDataRecordCorrectionDeadline().after(audirreq.getDataRecordCorrectionDeadline()) ? audirreq.getDataRecordCorrectionDeadline() : process.getAuditDataRecordCorrectionDeadline());
                        } else if (audirreq.getDataRecordCorrectionDeadline() != null) {
                            process.setAuditDataRecordCorrectionDeadline(audirreq.getDataRecordCorrectionDeadline());
                        }else{
                            processChecked--;
                        }
                        if (process.getAuditPrelProcAnsDeadline() != null && audirreq.getAnswerDeadline() != null) {
                            process.setAuditPrelProcAnsDeadline(process.getAuditPrelProcAnsDeadline().after(audirreq.getAnswerDeadline()) ? audirreq.getAnswerDeadline() : process.getAuditPrelProcAnsDeadline());
                        } else if (audirreq.getAnswerDeadline() != null) {
                            process.setAuditPrelProcAnsDeadline(audirreq.getAnswerDeadline());
                        }else{
                            processChecked--;
                        }
                        if (process.getAuditPrelProcClosedDeadline() != null && audirreq.getPreliminaryProceedingsClosedDeadline() != null) {
                            process.setAuditPrelProcClosedDeadline(process.getAuditPrelProcClosedDeadline().after(audirreq.getPreliminaryProceedingsClosedDeadline()) ? audirreq.getPreliminaryProceedingsClosedDeadline() : process.getAuditPrelProcClosedDeadline());
                        } else if (audirreq.getPreliminaryProceedingsClosedDeadline() != null) {
                            process.setAuditPrelProcClosedDeadline(audirreq.getPreliminaryProceedingsClosedDeadline());
                        }else{
                           processChecked--;
                        }
//                        if (pStore) {
//                            facade.updateProcess(process);
//                        }
                        break;
                    case review: 
                        TWmRequestReview reviewObj = (TWmRequestReview)reqObj;
                        if (process.getReviewDeadline()!= null && reviewObj.getReviewDeadline() != null) {
                            process.setReviewDeadline(process.getReviewDeadline().after(reviewObj.getReviewDeadline()) ? reviewObj.getReviewDeadline() : process.getReviewDeadline());
                        } else if (reviewObj.getReviewDeadline() != null) {
                            process.setReviewDeadline(reviewObj.getReviewDeadline());
                        }else{
                            processReviewChecked--;
                        }
                        if (process.getReviewRenewalDeadline()!= null && reviewObj.getRenewalDeadline()!= null) {
                            process.setReviewRenewalDeadline(process.getReviewRenewalDeadline().after(reviewObj.getRenewalDeadline()) ? reviewObj.getRenewalDeadline() : process.getReviewRenewalDeadline());
                        } else if (reviewObj.getRenewalDeadline() != null) {
                            process.setReviewRenewalDeadline(reviewObj.getRenewalDeadline());
                        }else{
                            processReviewChecked--;
                        }
                        if (process.getReviewInsReplyDeadline()!= null && reviewObj.getInsReplyDeadline()!= null) {
                            process.setReviewInsReplyDeadline(process.getReviewInsReplyDeadline().after(reviewObj.getInsReplyDeadline()) ? reviewObj.getInsReplyDeadline() : process.getReviewInsReplyDeadline());
                        } else if (reviewObj.getInsReplyDeadline() != null) {
                            process.setReviewInsReplyDeadline(reviewObj.getInsReplyDeadline());
                        }else{
                            processReviewChecked--;
                        }
                        if (process.getReviewReplySendDocDeadline()!= null && reviewObj.getReplySendDocDeadline()!= null) {
                            process.setReviewReplySendDocDeadline(process.getReviewReplySendDocDeadline().after(reviewObj.getReplySendDocDeadline()) ? reviewObj.getReplySendDocDeadline() : process.getReviewReplySendDocDeadline());
                        } else if (reviewObj.getReplySendDocDeadline() != null) {
                            process.setReviewReplySendDocDeadline(reviewObj.getReplySendDocDeadline());
                        }else{
                            processReviewChecked--;
                        }
                        if (process.getReviewCompletionDeadline()!= null && reviewObj.getCompletionDeadline()!= null) {
                            process.setReviewCompletionDeadline(process.getReviewCompletionDeadline().after(reviewObj.getCompletionDeadline()) ? reviewObj.getCompletionDeadline() : process.getReviewCompletionDeadline());
                        } else if (reviewObj.getCompletionDeadline() != null) {
                            process.setReviewCompletionDeadline(reviewObj.getCompletionDeadline());
                        }else{
                            processReviewChecked--;
                        }
                        break;
                    default:
                        break;
                }
            }

//            if (pStore) {
//                if (request != null && reqObj != null) {
//                    reqObj = facade.updateRequest(reqObj);
//                } //Vorgang ist vorhande , Request ist new
//                else if (request == null && reqObj != null) {
//                    reqObj = facade.storeRequest(reqObj);
//                }
//
//                if (reqObj != null && reqObj.getRequestTypeEnum().equals(WmRequestTypeEn.mdk)) {
//                    List<TWmReminder> reminders = getMdkRequestReminders();
//                    if (reqObj.isRequestMdk() && getMdkRequestReminders() != null && getMdkRequestReminders().size() > 0) {
//                        for (int i = 0; i < reminders.size(); i++) {
//                            reminders.get(i).setProcess(facade.getCurrentProcess());
//                            facade.storeReminderForRequest(reminders.get(i), reqObj);
//                        }
//                        reqObj = facade.updateRequest(reqObj);
//                    }
////                    facade.updateProcess(currentProcess);
//                } else if (reqObj != null && reqObj.isRequestAudit()) {
//                    List<TWmReminder> reminders = getAuditRequestReminders();
//                    if (getAuditRequestReminders() != null && getAuditRequestReminders().size() > 0) {
//                        for (int i = 0; i < reminders.size(); i++) {
//                            reminders.get(i).setProcess(facade.getCurrentProcess());
//                            facade.storeReminderForRequest(reminders.get(i), reqObj);
//                        }
//                        reqObj = facade.updateRequest(reqObj);
//                    }
//                }
//            }
            if (pStore) {
                if (request == null && reqObj != null) {
                    //new request should created
                    reqObj.setProcessHospital((TWmProcessHospital) facade.getCurrentProcess());
                    TWmEvent createRequestEvent = facade.createNewRequestEvent(reqObj);
                    if (createRequestEvent != null) {
                        TWmEvent event = facade.storeEventInDatabase(createRequestEvent);
                        reqObj = event.getRequest();
                        List<TWmEvent> events = getCreateReminderEventList(reqObj);
                        events.add(event);
                        facade.getEventsAsObsList().addAll(events);
                    }
                    //create new risk
//                    facade.createOrUpdateRiskData(reqObj);
                } else {
                    //update stuff
                    if (reqObj == null) {
                        return reqObj;
                    }
                    List<TWmEvent> events = getCreateReminderEventList(reqObj);
                    TWmEvent updateRequestEvent = facade.createUpdateRequestEvent(reqObj);
                    if (updateRequestEvent != null) {
                        updateRequestEvent = facade.storeEventInDatabase(updateRequestEvent);
                        facade.updateEventItemsForRequest(updateRequestEvent.getRequest());
                        reqObj = updateRequestEvent.getRequest();
                        events.add(updateRequestEvent);
                    }
                    facade.getEventsAsObsList().addAll(events);
//                    facade.createOrUpdateRiskData(reqObj);
                }
                if( processChecked > 0 || processReviewChecked > 0){// some field is updated: when no field are udated than processChecked = 0
                    facade.updateProcess(process);
                }
            }
        } else {
            if (reqObj != null && reqObj.getCreationDate() == null) {
                checkAuditQuota(reqObj); //why is it necessary to call this method at 2 different places?!
            }
        }
        return reqObj;
    }

    private List<TWmEvent> getCreateReminderEventList(TWmRequest pRequest) {
        List<TWmEvent> events = new ArrayList<>();
        if (pRequest == null) {
            return events;
        }
        if (hasAttachedReminders(pRequest)) {
            for (TWmReminder reminder : getAttachedReminders(pRequest)) {
                reminder.setProcess(facade.getCurrentProcess());
                //create new event, store in list, add all events at the end
                facade.addOrUpdateReminder(reminder);
                TWmEvent reminderEvent = facade.createReminderEvent(reminder, pRequest);
                reminderEvent = facade.storeEventInDatabase(reminderEvent);
                reminder.setId(reminderEvent.getContent().id);
                events.add(reminderEvent);
//                        facade.updateEventItemsForReminder(reminder);

//                        facade.storeReminderForRequest(reminder, reqObj);
            }
        }
        return events;
    }

    private boolean hasAttachedReminders(@NotNull TWmRequest pRequest) {
        pRequest = Objects.requireNonNull(pRequest, "Request can not be null");
        if (pRequest.isRequestMdk()) {
            return getMdkRequestReminders() != null && !getMdkRequestReminders().isEmpty();
        }
        if (pRequest.isRequestAudit()) {
            return getAuditRequestReminders() != null && !getAuditRequestReminders().isEmpty();
        }
        if (pRequest.isRequestReview()) {
            return getReviewRequestReminders() != null && !getReviewRequestReminders().isEmpty();
        }
        return false;
    }

    private List<TWmReminder> getAttachedReminders(@NotNull TWmRequest pRequest) {
        pRequest = Objects.requireNonNull(pRequest, "Request can not be null");
        if (pRequest.isRequestMdk()) {
            return getMdkRequestReminders();
        }
        if (pRequest.isRequestAudit()) {
            return getAuditRequestReminders();
        }
        if (pRequest.isRequestReview()) {
            return getReviewRequestReminders();
        }
        return new ArrayList<>();
    }

    public Node getMDKLayout(WmRequestTypeEn pRequestType) {
        mdkEditor = new MdkRequestEditor(facade, mainCase);
        if (getValidationSupport() != null) {
            mdkEditor.setValidationSupport(getValidationSupport());
            getValidationSupport().initInitialDecoration();
        }

        if (request != null) {
            if (request instanceof TWmRequestMdk) {
                final TWmRequestMdk mdkRequest = (TWmRequestMdk) request;
                mdkEditor.setRequest(mdkRequest);
            }
        }
        mdkEditor.initCatalogValidation();
        checkCatalogValidation(mdkEditor);
        mdkEditor.getCatalogValidationResult().setInvalidationCallback(new Callback<Void, Void>() {
            @Override
            public Void call(Void p) {
                checkCatalogValidation(mdkEditor);
                return null;
            }
        });
        requestType = pRequestType;
        return mdkEditor;
    }
    
    public Node getReviewLayout(WmRequestTypeEn pRequestType) {
       reviewEditor = new ReviewRequestEditor(facade, mainCase);
        if (getValidationSupport() != null) {
            reviewEditor.setValidationSupport(getValidationSupport());
            getValidationSupport().initInitialDecoration();
        }

        if (request != null) {
            if (request instanceof TWmRequestReview) {
                final TWmRequestReview reviewRequest = (TWmRequestReview) request;
                reviewEditor.setRequest(reviewRequest);
            }
        }
        reviewEditor.initCatalogValidation();
        checkCatalogValidation(reviewEditor);
        reviewEditor.getCatalogValidationResult().setInvalidationCallback(new Callback<Void, Void>() {
            @Override
            public Void call(Void p) {
                checkCatalogValidation(reviewEditor);
                return null;
            }
        });
        requestType = pRequestType;
        return reviewEditor;
     }


    public Node getAuditRequestLayout(WmRequestTypeEn pRequestType) {
        auditEditor = new AuditRequestEditor(facade, mainCase);
        if (getValidationSupport() != null) {
            auditEditor.setValidationSupport(getValidationSupport());
            getValidationSupport().initInitialDecoration();
        }

        if (request != null) {
            if (request instanceof TWmRequestAudit) {
                auditEditor.setRequest((TWmRequestAudit) request);
            }
        }
        auditEditor.initCatalogValidation();
        checkCatalogValidation(auditEditor);
        auditEditor.getCatalogValidationResult().setInvalidationCallback(new Callback<Void, Void>() {
            @Override
            public Void call(Void p) {
                checkCatalogValidation(auditEditor);
                return null;
            }
        });
        requestType = pRequestType;
        return auditEditor;

    }

    public void setOwnerWindow(final Window pOwnerWindow) {
        ownerWindow = pOwnerWindow;
    }

    public Window getOwnerWindow() {
        return ownerWindow;
    }

    private ObjectProperty<ValidationSupport> validationSupportProperty;

    private ObjectProperty<ValidationSupport> validationSupportProperty() {
        if (validationSupportProperty == null) {
            validationSupportProperty = new SimpleObjectProperty<>();
        }
        return validationSupportProperty;
    }

    public void setValidationSupport(ValidationSupport validationSupport) {
        validationSupportProperty().set(validationSupport);
    }

    public ValidationSupport getValidationSupport() {
        return validationSupportProperty().get();
    }
    private void checkCatalogValidation(BasicRequestEditor<?> pEditor){
        if(pEditor == null){
            LOG.fine("Could not check CatalogValidation, Editor was null");
            return;
        }
        if(pEditor.getCatalogValidationResult() == null){
            LOG.fine("No Catalog Validation found for Editor: " + pEditor.getClass().getName());
            setMessageType(null);
            setMessageText("");
            return;
        }
        setMessageType(pEditor.getCatalogValidationResult().getHighestErrorType());
        setMessageText(pEditor.getCatalogValidationResult().getValidationMessages());
    }
    public Node getBegeRequestLayout(WmRequestTypeEn pRequestType) {
        begeEditor = new BegeRequestEditor(facade, mainCase);
        if (getValidationSupport() != null) {
            begeEditor.setValidationSupport(getValidationSupport());
            getValidationSupport().initInitialDecoration();
        }
        if (request != null) {
            if (request instanceof TWmRequestBege) {
                begeEditor.setRequest((TWmRequestBege) request);
            }
        }
        begeEditor.initCatalogValidation();
        checkCatalogValidation(begeEditor);
        begeEditor.getCatalogValidationResult().setInvalidationCallback(new Callback<Void, Void>() {
            @Override
            public Void call(Void p) {
                checkCatalogValidation(begeEditor);
                return null;
            }
        });
        requestType = pRequestType;
        return begeEditor;
    }
    
    public Node getInsuranceRequestLayout(WmRequestTypeEn pRequestType) {
        insEditor = new InsuranceRequestEditor(facade, mainCase);
        if (getValidationSupport() != null) {
            insEditor.setValidationSupport(getValidationSupport());
            getValidationSupport().initInitialDecoration();
        }
        if (request != null) {
            if (request instanceof TWmRequestInsurance) {
                insEditor.setRequest((TWmRequestInsurance) request);
            }
        }
        insEditor.initCatalogValidation();
        checkCatalogValidation(insEditor);
        insEditor.getCatalogValidationResult().setInvalidationCallback(new Callback<Void, Void>() {
            @Override
            public Void call(Void p) {
                checkCatalogValidation(insEditor);
                return null;
            }
        });
        requestType = pRequestType;
        return insEditor;
    }

    public Node getOtherRequestLayout(WmRequestTypeEn pRequestType) {
        otherEditor = new OtherRequestEditor(facade, mainCase);
//        otherEditor.setProcessServiceFacade(facade);
        if (getValidationSupport() != null) {
            otherEditor.setValidationSupport(getValidationSupport());
            getValidationSupport().initInitialDecoration();
        }
        if (request != null) {
            if (request instanceof TWmRequestOther) {
                otherEditor.setRequest((TWmRequestOther) request);
            }
        }
        otherEditor.initCatalogValidation();
        checkCatalogValidation(otherEditor);
        otherEditor.getCatalogValidationResult().setInvalidationCallback(new Callback<Void, Void>() {
            @Override
            public Void call(Void p) {
                checkCatalogValidation(otherEditor);
                return null;
            }
        });
        requestType = pRequestType;
        return otherEditor;
    }

    public List<TWmReminder> getMdkRequestReminders() {
        return mdkEditor.getReminders();

    }

    public List<TWmReminder> getAuditRequestReminders() {
        return auditEditor.getReminders();
    }

    public List<TWmReminder> getReviewRequestReminders() {
        return reviewEditor.getReminders();
    }

    public static void resizeAndAlignWindow(final Window pWindow) {
        if (pWindow == null) {
            return;
        }
        pWindow.sizeToScene();
//        getDialogSkin().setMinWidth(getDialogPane().sceneProperty().get().getWindow().getWidth());
//        getDialogSkin().setMaxWidth(getDialogPane().sceneProperty().get().getWindow().getWidth());
//        getDialogSkin().setMinHeight(getDialogPane().sceneProperty().get().getWindow().getHeight());
//        getDialogSkin().setMaxHeight(getDialogPane().sceneProperty().get().getWindow().getHeight());
        // reset positions
        pWindow.setX(Double.NaN);
        pWindow.setY(Double.NaN);

        //set dialog window to the center of the screen.
        pWindow.centerOnScreen();
    }
    private ObjectProperty<CpxErrorTypeEn> messageTypeProperty;
    public ObjectProperty<CpxErrorTypeEn> messageTypeProperty(){
        if(messageTypeProperty == null){
            messageTypeProperty = new SimpleObjectProperty<>(CpxErrorTypeEn.INFO);
        }
        return messageTypeProperty;
    }
    
    public CpxErrorTypeEn getMessageType(){
        return messageTypeProperty().get();
    }
    
    public void setMessageType(CpxErrorTypeEn pMessageType){
        messageTypeProperty().set(Objects.requireNonNullElse(pMessageType, CpxErrorTypeEn.INFO));
    }
    
    private StringProperty messageTextProperty;
    public StringProperty messageTextProperty(){
        if(messageTextProperty == null){
            messageTextProperty = new SimpleStringProperty();
        }
        return messageTextProperty;
    }
    public void setMessageText(String pText){
        messageTextProperty().set(pText);
    }
    public String getMessageText(){
        return messageTextProperty().get();
    }

    public void setCheckQuota(boolean pQuotaCheck){
        checkQuota = pQuotaCheck;
    }
    public boolean isCheckQuota() {
        return checkQuota;
    }

    public boolean useReview() {
        if(mainCase != null){
            mainCase.getCurrentExtern().getCsdAdmissionDate();
            List<CDeadline> deadlines =  MenuCache.getMenuCacheDeadlines().getDeadlines(mainCase.getCurrentExtern().getCsdAdmissionDate(), DeadlineTypeEn.DL11);
            return deadlines != null && !deadlines.isEmpty();
        }
        return false;
    }


}
