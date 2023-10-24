/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.app.wm.fx.process.section.WmReminderSectionForRequest;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.model.catalog.AbstractCpxNonAnnualCatalog;
import de.lb.cpx.client.core.model.fx.combobox.RequestStateCombobox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCheckComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.model.enums.DeadlineTypeEn;
import de.lb.cpx.server.commonDB.model.CDeadline;
import de.lb.cpx.server.commonDB.model.CMdk;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRequestMdk;
import de.lb.cpx.wm.model.TWmRequestReview;
import de.lb.cpx.wm.model.enums.WmRequestTypeEn;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import javax.naming.NamingException;


/**
 *
 * @author gerschmann
 */
public class ReviewRequestEditorSkin extends BasicRequestEditorSkin<ReviewRequestEditor, TWmRequestReview> {
	private InsuranceInfoTextField ltfInsName;
	private LabeledTextField ltfInsEmployee;
	private LabeledTextField ltfInsNumber;
	private MdInfoTextField tfMdkName;
	private LabeledTextField tfMdkWorker;
	private LabeledDatePicker dpStartInsAuditDate;
	private LabeledDatePicker dpMdkStartAuditDate; 
	private CheckBox ckExpertiseActivate;
	private LabeledDatePicker dpExpertiseDate;
	private LabeledDatePicker dpExpertiseInDate;
	private RequestStateCombobox cbStatus;
	private LabeledCheckComboBox <Map.Entry<Long, String>> cbAuditReasons;
	private CheckBox ckEvDeadlineSet; 
	private LabeledDatePicker dpEvDeadlineToDate;
	private ReminderLabeledCheckboxLink ckEvWvDeadlineToDate;
	private CheckBox ckEvExtendedToDate;
	private LabeledDatePicker dpEvExtendedToDate;
	private ReminderLabeledCheckboxLink ckEvWvExtendedToDate;
	private CheckBox ckEvStarted;
	private LabeledDatePicker dpEvStartedDate;
	private LabeledDatePicker dpDeadlineAnswerInsDate;
	private ReminderLabeledCheckboxLink ckAnswerInsReminder;
	private CheckBox ckAnsweredIns;
	private LabeledDatePicker dpAnsweredDate;
	private CheckBox ckSendOn;
	private LabeledDatePicker dpSendOnDeadlineDate;
	private ReminderLabeledCheckboxLink ckEvWvSendOn;
	private CheckBox ckSentOn;
	private LabeledDatePicker dpSentOnDate;
	private ReminderLabeledCheckboxLink ckEvWvWasSentOn;
	private CheckBox ckEvEndDeadline;
	private LabeledDatePicker dpEvEndDeadline;
	private ReminderLabeledCheckboxLink ckEvWvEndDeadline;
	private CheckBox ckEvEnded;
	private LabeledDatePicker dpEvEnded;
	private ReminderLabeledCheckboxLink ckEvWvCompleted;
	private LabeledTextArea taEvComment;
        private Label lbReviewAfterMd;
        private Label lbSendOnDocuments;
        private Label lbEndOfReview;
        private HBox hbReminders;
        private WmReminderSectionForRequest wmRemSection;
        private  BooleanProperty keepDeactivatedExpertiseDateProperty;
        private  BooleanProperty keepDeactivatedExpertiseInDateProperty;
        private ObjectProperty<TWmRequestMdk> initialMdkInternalProperty;
        
        private List<ReminderLabeledCheckboxLink> reminderChecks;
        
    private static final Logger LOG = Logger.getLogger(ReviewRequestEditorSkin.class.getName());
    
    private BooleanProperty keepDeactivatedExpertiseDateProperty(){
        if(keepDeactivatedExpertiseDateProperty == null){
            keepDeactivatedExpertiseDateProperty = new SimpleBooleanProperty(false);
        }
        return keepDeactivatedExpertiseDateProperty;
    }

    private BooleanProperty keepDeactivatedExpertiseInDateProperty(){
        if(keepDeactivatedExpertiseInDateProperty == null){
            keepDeactivatedExpertiseInDateProperty = new SimpleBooleanProperty(false);
        }
        return keepDeactivatedExpertiseInDateProperty;
    }
    
    
    private ObjectProperty<TWmRequestMdk> initialMdkInternalProperty(){
        if(initialMdkInternalProperty == null){
            initialMdkInternalProperty = new SimpleObjectProperty<>();
        }
        return initialMdkInternalProperty;
    }
    

    public ReviewRequestEditorSkin(ReviewRequestEditor pSkinnable) throws IOException{
        super(pSkinnable, "/fxml/ReviewRequestEditorFXML.fxml");
    }

    @Override
    protected void setUpNodes() {

	ltfInsName = (InsuranceInfoTextField)lookUpInRoot("#ltfInsName");
	ltfInsEmployee = (LabeledTextField)lookUpInRoot("#ltfInsEmployee");
	ltfInsNumber = (LabeledTextField)lookUpInRoot("#ltfInsNumber");
        ltfInsName.setRelatedInsNumberField(ltfInsNumber);
        ltfInsName.setInsuranceCatalog(getSkinnable().getInsuranceCatalog());
	tfMdkName = (MdInfoTextField)lookUpInRoot("#tfMdkName");
        tfMdkName.setMdkCatalog(getSkinnable().getMdkCatalog());
        tfMdkName.mdkProperty().addListener(new ChangeListener<CMdk>() {
            public void changed(ObservableValue<? extends CMdk> observable, CMdk oldValue, CMdk newValue){
               checkMdFields( tfMdkName.getMdkInternalId());
            }           
        });
	tfMdkWorker = (LabeledTextField)lookUpInRoot("#tfMdkWorker");
	dpStartInsAuditDate = (LabeledDatePicker)lookUpInRoot("#dpStartInsAuditDate");
	dpMdkStartAuditDate = (LabeledDatePicker)lookUpInRoot("#dpMdkStartAuditDate");
        ckExpertiseActivate = (CheckBox)lookUpInRoot("#ckExpertiseActivate");
	dpExpertiseDate  = (LabeledDatePicker)lookUpInRoot("#dpExpertiseDate");
	dpExpertiseInDate = (LabeledDatePicker)lookUpInRoot("#dpExpertiseInDate");;
        cbStatus = (RequestStateCombobox)lookUpInRoot("#cbStatus");
        cbStatus.setItems(MenuCache.getMenuCacheRequestStates().values(new Date(), MenuCacheOptionsEn.IGNORE_INACTIVE));
        Set<Map.Entry<Long, String>> list = MenuCache.instance().getAuditReasonsEntries(getSkinnable().getCase()!=null?getSkinnable().getCase().getCsCaseTypeEn():null); 
        cbAuditReasons = (LabeledCheckComboBox)lookUpInRoot("#cbAuditReasons");
        cbAuditReasons.getItems().addAll(new ArrayList<>(list));
        cbAuditReasons.getControl().setConverter(new StringConverter<Map.Entry<Long, String>>() {
            @Override
            public String toString(Map.Entry<Long, String> t) {
                return t == null ? "" : t.getValue();
            }

            @Override
            public Map.Entry<Long, String> fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        ckEvDeadlineSet = (CheckBox)lookUpInRoot("#ckEvDeadlineSet");
	dpEvDeadlineToDate = (LabeledDatePicker)lookUpInRoot("#dpEvDeadlineToDate");
        ckEvWvDeadlineToDate = (ReminderLabeledCheckboxLink)lookUpInRoot("#ckEvWvDeadlineToDate");
        ckEvExtendedToDate = (CheckBox)lookUpInRoot("#ckEvExtendedToDate");
	dpEvExtendedToDate = (LabeledDatePicker)lookUpInRoot("#dpEvExtendedToDate");
        ckEvWvExtendedToDate = (ReminderLabeledCheckboxLink)lookUpInRoot("#ckEvWvExtendedToDate");
        ckEvStarted = (CheckBox)lookUpInRoot("#ckEvStarted");
	dpEvStartedDate = (LabeledDatePicker)lookUpInRoot("#dpEvStartedDate");
	dpDeadlineAnswerInsDate = (LabeledDatePicker)lookUpInRoot("#dpDeadlineAnswerInsDate");
        ckAnswerInsReminder = (ReminderLabeledCheckboxLink)lookUpInRoot("#ckAnswerInsReminder");
        ckAnsweredIns = (CheckBox)lookUpInRoot("#ckAnsweredIns");
        dpAnsweredDate = (LabeledDatePicker)lookUpInRoot("#dpAnsweredDate");
        ckSendOn = (CheckBox)lookUpInRoot("#ckSendOn");
	dpSendOnDeadlineDate = (LabeledDatePicker)lookUpInRoot("#dpSendOnDeadlineDate");
        ckEvWvSendOn = (ReminderLabeledCheckboxLink)lookUpInRoot("#ckEvWvSendOn");
        ckSentOn = (CheckBox)lookUpInRoot("#ckSentOn");
	dpSentOnDate = (LabeledDatePicker)lookUpInRoot("#dpSentOnDate");
        ckEvWvWasSentOn = (ReminderLabeledCheckboxLink)lookUpInRoot("#ckEvWvWasSentOn");
        ckEvEndDeadline = (CheckBox)lookUpInRoot("#ckEvEndDeadline");
	dpEvEndDeadline = (LabeledDatePicker)lookUpInRoot("#dpEvEndDeadline");
        ckEvWvEndDeadline = (ReminderLabeledCheckboxLink)lookUpInRoot("#ckEvWvEndDeadline");
        ckEvEnded = (CheckBox)lookUpInRoot("#ckEvEnded");
	dpEvEnded = (LabeledDatePicker)lookUpInRoot("#dpEvEnded");
	ckEvWvCompleted = (ReminderLabeledCheckboxLink)lookUpInRoot("#ckEvWvCompleted");
	taEvComment = (LabeledTextArea)lookUpInRoot("#taEvComment");
        addTextTemplateController(taEvComment);
        lbReviewAfterMd = (Label)lookUpInRoot("#lbReviewAfterMd");
        lbSendOnDocuments = (Label)lookUpInRoot("#lbSendOnDocuments");
        lbEndOfReview = (Label)lookUpInRoot("#lbEndOfReview");
        reminderChecks = new ArrayList<>(){
            {
                add(ckEvWvDeadlineToDate);
                add(ckAnswerInsReminder);
                add(ckEvWvExtendedToDate);
                add(ckEvWvSendOn);
                add(ckEvWvWasSentOn);
                add(ckEvWvEndDeadline);
                add(ckEvWvCompleted);
            }
        };
        hbReminders = (HBox)lookUpInRoot("#hbReminders");
        if (getSkinnable().getRequest().getId() != 0) {
            //Editing an existing request
            if(getSkinnable().getProcessServiceFacade() != null){
                if (!getSkinnable().getProcessServiceFacade().getRemindersForRequest(getSkinnable().getRequest().getId()).isEmpty()) {
                    wmRemSection = new WmReminderSectionForRequest(getSkinnable().getProcessServiceFacade(), getSkinnable().getRequest());
                    hbReminders.getChildren().addAll(wmRemSection.getRoot());
                }
            }
        }
    }

    @Override
    protected void setUpLanguage() {
        ltfInsName.setTitle(Lang.getReviewInsuranceName());
        ltfInsEmployee.setTitle(Lang.getReviewInsuranceWorker());
        ltfInsNumber.setTitle(Lang.getReviewInsuranceNumber());
        ltfInsName.setRelatedInsNumberField(ltfInsNumber);
        tfMdkName.setTitle(Lang.getReviewMdName());
        tfMdkWorker.setTitle(Lang.getReviewMdWorker());
 	dpStartInsAuditDate.setTitle(Lang.getReviewStartAuditInsurance());
	dpMdkStartAuditDate.setTitle(Lang.getReviewStartAuditMd());
	dpExpertiseDate.setTitle(Lang.getReviewExpertiseDate()); 
	dpExpertiseInDate.setTitle(Lang.getReviewExpertiseInDate());
        cbStatus.setTitle(Lang.getReviewRequestStatus());
        cbAuditReasons.setTitle(Lang.getReviewAuditReasons());
	dpEvDeadlineToDate.setTitle(Lang.getReviewDeadlineDate());
        ckEvWvDeadlineToDate.setTitle(Lang.getReviewReminder());
	dpEvExtendedToDate.setTitle(Lang.getReviewDeadlineExtendedDate());
        ckEvWvExtendedToDate.setTitle(Lang.getReviewReminder());
	dpEvStartedDate.setTitle(Lang.getReviewStartDate());
	dpDeadlineAnswerInsDate.setTitle(Lang.getReviewDeadlineAnswerInsuranceDate());
        ckAnswerInsReminder.setTitle(Lang.getReviewReminder());
        dpAnsweredDate.setTitle(Lang.getReviewAnsweredDate());
	dpSendOnDeadlineDate.setTitle(Lang.getReviewDeadlineSendOnDate());
        ckEvWvSendOn .setTitle(Lang.getReviewReminder());
	dpSentOnDate.setTitle(Lang.getReviewSentDate());
        ckEvWvWasSentOn.setTitle(Lang.getReviewReminder());
	dpEvEndDeadline.setTitle(Lang.getReviewCompletionDeadlineDate());
        ckEvWvEndDeadline.setTitle(Lang.getReviewReminder());
	dpEvEnded.setTitle(Lang.getReviewCompletedDate());
	ckEvWvCompleted.setTitle(Lang.getReviewReminder());
	taEvComment.setTitle(Lang.getReviewComment());
        lbReviewAfterMd.setText(Lang.getReviewAfterMd());
        lbSendOnDocuments.setText(Lang.getReviewSendOnDocuments());
        lbEndOfReview.setText(Lang.getReviewCompletion());
        disableFunctionControls(true);
    }

    @Override
    protected void setUpValidation() {
        ltfInsName.setValidationSupport(getSkinnable().getValidationSupport());
        ltfInsName.setupValidation();
        tfMdkName.setValidationSupport(getSkinnable().getValidationSupport());
        tfMdkName.setupValidation();
        for(ReminderLabeledCheckboxLink remCheck:reminderChecks){
            remCheck.setValidationSupport(getSkinnable().getValidationSupport());
            remCheck.setupValidation();
        }


}

    @Override
    protected void setUpAutoCompletion() {
        ltfInsName.setUpAutoCompletion();
        tfMdkName.setUpAutoCompletion();

//        if (getSkinnable().getRequest() == null || getSkinnable().getRequest().getId() == 0L) {
            //Editing an existing request
            if(getSkinnable().getProcessServiceFacade() != null){
                // try to put md  data
                TWmRequestMdk mdk = getSkinnable().getProcessServiceFacade().getLastObsRequest(WmRequestTypeEn.mdk);
//                TWmRequestInsurance insurance = getSkinnable().getProcessServiceFacade().getLastObsRequest(WmRequestTypeEn.insurance);
                initialMdkInternalProperty().set(mdk);
                filldefaultMdkFields(mdk);
//                if(mdk != null){
//                    tfMdkName.setMdInternalId(mdk.getMdkInternalId());
//                    if(mdk.getStartAudit()!= null){
//                        dpMdkStartAuditDate.setDate(mdk.getStartAudit());
//                        dpMdkStartAuditDate.setDisable(true);
//                    }
//                    if(mdk.getReportDate() != null){
//                        dpExpertiseDate.setDate(mdk.getReportDate());
//                        dpExpertiseDate.setDisable(true);
//                        keepDeactivatedExpertiseDateProperty().set(true);
//
//                        
//                    }
//                    if(mdk.getMdkReportReceiveDate() != null){
//                        dpExpertiseInDate.setDate(mdk.getMdkReportReceiveDate());
//                        dpExpertiseInDate.setDisable(true);
//                        keepDeactivatedExpertiseInDateProperty().set(true);
//                    }
//                    ckExpertiseActivate.setDisable(mdk.getReportDate() != null && mdk.getMdkReportReceiveDate() != null);
//                    
//                }
//                if(insurance != null){
//                    ltfInsName.getRelatedInsNumberField().setText(insurance.getInsuranceIdentifier());
//                }
//            }
        }

    }

    @Override
    protected void updateRequestValues(TWmRequestReview pRequest) {
        ltfInsEmployee.setText(pRequest.getInsEditor());
        ltfInsName.getRelatedInsNumberField().setText(pRequest.getInsIdentifier());
        tfMdkWorker.setText(pRequest.getMdEditor());
        tfMdkName.setMdInternalId(pRequest.getMdInternalId());

        checkMdFields(pRequest.getMdInternalId());
        
        dpStartInsAuditDate.setDate(pRequest.getStartAudit());
        dpMdkStartAuditDate.setDate(pRequest.getMdStartAudit());
        if(dpExpertiseDate.getDate() == null){
            dpExpertiseDate.setDate(pRequest.getReportDate());
        }
        if(dpExpertiseInDate.getDate() == null){
            dpExpertiseInDate.setDate(pRequest.getReportReceiveDate());
        }
        if(!ckExpertiseActivate.isSelected() && !ckExpertiseActivate.isDisabled()){
            ckExpertiseActivate.setSelected(pRequest.isReportFl());
            dpExpertiseInDate.setDisable(!ckExpertiseActivate.isSelected());
            dpExpertiseDate.setDisable(!ckExpertiseActivate.isSelected());
        }
        dpExpertiseInDate.setDisable(!ckExpertiseActivate.isSelected());
        ckEvDeadlineSet.setDisable(pRequest.getReportReceiveDate() == null);
        ckEvExtendedToDate.setDisable(pRequest.getReportReceiveDate() == null);
        ckEvStarted.setDisable(pRequest.getReportReceiveDate() == null);
        ckAnsweredIns.setDisable(pRequest.getReportReceiveDate() == null);
        
        dpEvDeadlineToDate.setDate(pRequest.getReviewDeadline());
        ckEvDeadlineSet.setSelected(pRequest.isReviewDeadlineFl());
        dpEvExtendedToDate.setDate(pRequest.getRenewalDeadline());
        dpEvStartedDate.setDate(pRequest.getReviewStart());
        ckEvStarted.setSelected(pRequest.isReviewStartFl());
        dpDeadlineAnswerInsDate.setDate(pRequest.getInsReplyDeadline());
        dpAnsweredDate.setDate(pRequest.getInsReplyDate());
ckSendOn.setDisable(pRequest.getInsReplyDate() == null);
ckSentOn.setDisable(pRequest.getInsReplyDate() == null);
ckEvEndDeadline.setDisable(pRequest.getInsReplyDate() == null);
ckEvEnded.setDisable(pRequest.getInsReplyDate() == null);
dpEvEnded.setDisable(pRequest.getInsReplyDate() == null);
        
        ckAnsweredIns.setSelected(pRequest.isInsReplyDateFl());
        dpSendOnDeadlineDate.setDate(pRequest.getReplySendDocDeadline());
        ckSendOn.setSelected(pRequest.isReplySendDocDeadlineFl());
        dpSentOnDate.setDate(pRequest.getReplySendDocDate());
        ckSentOn.setSelected(pRequest.isReplySendDocFl());
        dpEvEndDeadline.setDate(pRequest.getCompletionDeadline());
        ckEvEndDeadline.setSelected(pRequest.isCompletionDeadlineFl());
        dpEvEnded.setDate(pRequest.getCompletedDate());
        ckEvEnded.setSelected(pRequest.isCompletedFl());
        taEvComment.setText(pRequest.getComment());
        cbStatus.selectByInternalId(pRequest.getRequestState());
        updateAuditReasonsData(cbAuditReasons, pRequest);

        
    }

    @Override
    protected void disableControls(boolean armed) {
        ltfInsName.disableControls(armed);
        tfMdkName.disableControls(armed);
    }

    @Override
    protected void updateRequestData() {
        getSkinnable().getRequest().setInsEditor(ltfInsEmployee.getText());
        getSkinnable().getRequest().setInsIdentifier(ltfInsName.getRelatedInsNumberField().getText());
        getSkinnable().getRequest().setMdEditor(this.tfMdkWorker.getText());
        getSkinnable().getRequest().setMdInternalId(tfMdkName.getMdkInternalId());
        getSkinnable().getRequest().setReportFl(this.ckExpertiseActivate.isSelected());
        getSkinnable().getRequest().setStartAudit(this.dpStartInsAuditDate.getDate());
        getSkinnable().getRequest().setMdStartAudit(this.dpMdkStartAuditDate.getDate());

        getSkinnable().getRequest().setRenewalDeadline(this.dpEvExtendedToDate.getDate());
        getSkinnable().getRequest().setRenewalDeadlineFl(this.ckExpertiseActivate.isSelected());
        dpExpertiseDate.setDisable(!ckExpertiseActivate.isSelected());
        dpEvExtendedToDate.setDisable(!ckExpertiseActivate.isSelected());
//        if(ckExpertiseActivate.isSelected()){
        getSkinnable().getRequest().setReportDate(this.dpExpertiseDate.getDate());         
        getSkinnable().getRequest().setReportReceiveDate(this.dpExpertiseInDate.getDate());
            
//        }
        getSkinnable().getRequest().setReviewDeadlineFl(this.ckEvDeadlineSet.isSelected());        
        
        getSkinnable().getRequest().setReviewDeadline(this.dpEvDeadlineToDate.getDate());
        getSkinnable().getRequest().setReviewStart(this.dpEvStartedDate.getDate());
        getSkinnable().getRequest().setReviewStartFl(this.ckEvStarted.isSelected());
        getSkinnable().getRequest().setInsReplyDeadline(this.dpDeadlineAnswerInsDate.getDate());
        getSkinnable().getRequest().setInsReplyDate(this.dpAnsweredDate.getDate());
        getSkinnable().getRequest().setInsReplyDateFl(this.ckAnsweredIns.isSelected());
        getSkinnable().getRequest().setReplySendDocDeadline(this.dpSendOnDeadlineDate.getDate());
        getSkinnable().getRequest().setReplySendDocDeadlineFl(this.ckSendOn.isSelected());
        getSkinnable().getRequest().setReplySendDocDate(this.dpSentOnDate.getDate());
        getSkinnable().getRequest().setReplySendDocFl(this.ckSentOn.isSelected());
        getSkinnable().getRequest().setCompletionDeadline(this.dpEvEndDeadline.getDate());
        getSkinnable().getRequest().setCompletionDeadlineFl(this.ckEvEndDeadline.isSelected());
        getSkinnable().getRequest().setCompletedDate(this.dpEvEnded.getDate());
        getSkinnable().getRequest().setCompletedFl(this.ckEvEnded.isSelected());
        getSkinnable().getRequest().setComment(this.taEvComment.getText());
        updateAuditReasons(cbAuditReasons,getSkinnable().getRequest());
        Long internalId = cbStatus.getSelectedInternalId();
        getSkinnable().getRequest().setRequestState(internalId == null ? 0L : internalId);
        try {
            getSkinnable().setReminders(getReviewRequestReminders());
        } catch (NamingException ex) {
            Logger.getLogger(MdkRequestEditorSkin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }                

    private void disableFunctionControls(boolean b) {

        dpExpertiseDate.setDisable(b);
        dpExpertiseInDate.setDisable(b);
        ckEvDeadlineSet.setDisable(b);
        ckEvExtendedToDate.setDisable(b);
        ckEvStarted.setDisable(b);
        ckAnsweredIns.setDisable(b);
        ckSendOn.setDisable(b);
        ckSentOn.setDisable(b);
        ckEvEndDeadline.setDisable(b);
        ckEvEnded.setDisable(b);
        
        dpEvDeadlineToDate.setDisable(b);
        ckEvWvDeadlineToDate.setDisable(b);
        dpEvExtendedToDate.setDisable(b);
        ckEvWvExtendedToDate.setDisable(b);
        dpEvStartedDate.setDisable(b);
        dpEvExtendedToDate.setDisable(b);
        dpDeadlineAnswerInsDate.setDisable(b);
        ckAnswerInsReminder.setDisable(b);
        dpAnsweredDate.setDisable(b);
        dpEvExtendedToDate.setDisable(b);
        ckEvWvExtendedToDate.setDisable(b);
        dpSendOnDeadlineDate.setDisable(b);
        ckEvWvSendOn.setDisable(b);
        dpSentOnDate.setDisable(b);
        ckEvWvWasSentOn.setDisable(b);
        dpEvEndDeadline.setDisable(b);
        ckEvWvEndDeadline.setDisable(b);
        dpEvEnded.setDisable(b);
        ckEvWvCompleted.setDisable(b);
     }

    protected void checkMdFields(long mdInternalId) {
        if(initialMdkInternalProperty().get() != null){
            if( initialMdkInternalProperty().get().getMdkInternalId() != mdInternalId ){
//reset md fields     
                 filldefaultMdkFields(null);
            }else{
// fill fields     
                TWmRequestMdk mdk = initialMdkInternalProperty().get();
                filldefaultMdkFields(mdk);
            }
        }
    }
    
    private void filldefaultMdkFields(TWmRequestMdk mdk ){
        if(mdk != null){
                tfMdkName.setMdInternalId(mdk.getMdkInternalId());
                 if(mdk.getStartAudit()!= null){
                     dpMdkStartAuditDate.setDate(mdk.getStartAudit());
                     dpMdkStartAuditDate.setDisable(true);
                 }
                 if(mdk.getReportDate() != null){
                     dpExpertiseDate.setDate(mdk.getReportDate());
                     dpExpertiseDate.setDisable(true);
                     keepDeactivatedExpertiseDateProperty().set(true);


                 }
                 if(mdk.getMdkReportReceiveDate() != null){
                     dpExpertiseInDate.setDate(mdk.getMdkReportReceiveDate());
                     dpExpertiseInDate.setDisable(true);
                     keepDeactivatedExpertiseInDateProperty().set(true);
                 }
                 ckExpertiseActivate.setDisable(mdk.getReportDate() != null && mdk.getMdkReportReceiveDate() != null);
                 setDeadline2ExpertiseInDate();
            
        }else{

                dpMdkStartAuditDate.setDate(null);
                dpMdkStartAuditDate.setDisable(false);

                dpExpertiseDate.setDate(null);
                dpExpertiseDate.setDisable(true);
                keepDeactivatedExpertiseDateProperty().set(false);

                dpExpertiseInDate.setDate(null);
                dpExpertiseInDate.setDisable(true);
                keepDeactivatedExpertiseInDateProperty().set(false);
                
                ckExpertiseActivate.setSelected(false);
                ckExpertiseActivate.setDisable(false);
            
        }
    }
    
    private class ClearAutoCompleteFieldsListener implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (newValue != null && newValue.isEmpty()) {
                ltfInsName.getControl().clear();
                ltfInsNumber.getControl().clear();

            }
        }
    }
    
    @Override
    public void setUpdateListeners() {
        super.setUpdateListeners();
        tfMdkName.setUpdateListeners();
        ltfInsName.setUpdateListeners(
                (getSkinnable().getRequest() == null||getSkinnable().getRequest().getId() == 0L)?
                null:getSkinnable().getRequest().getInsIdentifier()
        );
         for(ReminderLabeledCheckboxLink remCheck:reminderChecks){
             remCheck.setupListeners();
         }
//   Gutachten      
         ckExpertiseActivate.selectedProperty().addListener(new ChangeListener<Boolean>(){
             @Override
             public void changed(ObservableValue<? extends Boolean> ov,    Boolean old_val, Boolean new_val) {
                 if(!keepDeactivatedExpertiseDateProperty().get()) {
                    dpExpertiseDate.setDisable(!new_val);
                 }
                 if(!keepDeactivatedExpertiseInDateProperty().get()){
                   dpExpertiseInDate.setDisable(!new_val);
                 }
            }
         });
         
         ckEvDeadlineSet.selectedProperty().addListener(new ChangeListener<Boolean>(){
             @Override
             public void changed(ObservableValue<? extends Boolean> ov,    Boolean old_val, Boolean new_val) {
//                  dpEvDeadlineToDate.setDisable(!new_val);always disabled
                  ckEvWvDeadlineToDate.setDisable(!new_val);
            }
         });

         ckEvExtendedToDate.selectedProperty().addListener(new ChangeListener<Boolean>(){
             @Override
             public void changed(ObservableValue<? extends Boolean> ov,    Boolean old_val, Boolean new_val) {
                  dpEvExtendedToDate.setDisable(!new_val);
                  ckEvWvExtendedToDate.setDisable(!new_val);
            }
         });
 
         ckEvStarted.selectedProperty().addListener(new ChangeListener<Boolean>(){
             @Override
             public void changed(ObservableValue<? extends Boolean> ov,    Boolean old_val, Boolean new_val) {
                  dpEvStartedDate.setDisable(!new_val);
//                  dpDeadlineAnswerInsDate.setDisable(!new_val);always disabled
                  ckAnswerInsReminder.setDisable(!new_val);
            }
         });

         ckAnsweredIns.selectedProperty().addListener(new ChangeListener<Boolean>(){
             @Override
             public void changed(ObservableValue<? extends Boolean> ov,    Boolean old_val, Boolean new_val) {
                  dpAnsweredDate.setDisable(!new_val);

            }
         });

         ckEvExtendedToDate.selectedProperty().addListener(new ChangeListener<Boolean>(){
             @Override
             public void changed(ObservableValue<? extends Boolean> ov,    Boolean old_val, Boolean new_val) {
                  dpEvExtendedToDate.setDisable(!new_val);
                  ckEvWvExtendedToDate.setDisable(!new_val);
            }
         });
 
         ckSendOn.selectedProperty().addListener(new ChangeListener<Boolean>(){
             @Override
             public void changed(ObservableValue<? extends Boolean> ov,    Boolean old_val, Boolean new_val) {
//                  dpSendOnDeadlineDate.setDisable(!new_val);always disabled
                  ckEvWvSendOn.setDisable(!new_val);
            }
         });
 
         ckSentOn.selectedProperty().addListener(new ChangeListener<Boolean>(){
             @Override
             public void changed(ObservableValue<? extends Boolean> ov,    Boolean old_val, Boolean new_val) {
                  dpSentOnDate.setDisable(!new_val);
                  ckEvWvWasSentOn.setDisable(!new_val);
            }
         });
 
         ckEvEndDeadline.selectedProperty().addListener(new ChangeListener<Boolean>(){
             @Override
             public void changed(ObservableValue<? extends Boolean> ov,    Boolean old_val, Boolean new_val) {
//                  dpEvEndDeadline.setDisable(!new_val);always disabled
                  ckEvWvEndDeadline.setDisable(!new_val);
            }
         });
 
         ckEvEnded.selectedProperty().addListener(new ChangeListener<Boolean>(){
             @Override
             public void changed(ObservableValue<? extends Boolean> ov,    Boolean old_val, Boolean new_val) {
                  dpEvEnded.setDisable(!new_val);
                  ckEvWvCompleted.setDisable(!new_val);
            }
         });
         
         //eingang gutachten
         dpExpertiseInDate.getControl().valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                setDeadline2ExpertiseInDate();
                
            }
         });
         dpEvStartedDate.getControl().valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                
                final Date date = dpEvStartedDate.getDate();
                if(date == null){
                    return;
                }
                CDeadline dl = MenuCache.getMenuCacheDeadlines().getDeadline(DeadlineTypeEn.DL12);
                if(dl != null){
                    dpDeadlineAnswerInsDate.setDate(dl.calculateDeadline(date));
                }else{
                    dpDeadlineAnswerInsDate.setDate(date);
                    dpDeadlineAnswerInsDate.setDisable(false);
                }
                if(dpEvExtendedToDate.getDate() == null){
                    dl = MenuCache.getMenuCacheDeadlines().getDeadline(DeadlineTypeEn.DL14);
                    if(dl != null){
                        dpEvEndDeadline.setDate(dl.calculateDeadline(date));
                    }
                }else{
                    dpEvEndDeadline.setDate(dl.calculateDeadline(dpEvExtendedToDate.getDate()));
                }

            }
         });
         
         dpEvExtendedToDate.getControl().valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                Date date = dpEvExtendedToDate.getDate();
                if(date == null){
                    return;
                }
                CDeadline dl = MenuCache.getMenuCacheDeadlines().getDeadline(DeadlineTypeEn.DL14);
                if(dl != null){
                    dpEvEndDeadline.setDate(dl.calculateDeadline(date));
                }else{
                    dpEvEndDeadline.setDate(date);

                }

            }
         });
         dpAnsweredDate.getControl().valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                Date date = dpAnsweredDate.getDate();
                if(date == null){
                    return;
                }
                CDeadline dl = MenuCache.getMenuCacheDeadlines().getDeadline(DeadlineTypeEn.DL13);
                if(dl != null){
                    dpSendOnDeadlineDate.setDate(dl.calculateDeadline(date));
                }else{
                    dpSendOnDeadlineDate.setDate(date);
                    dpSendOnDeadlineDate.setDisable(false);
                }
               ckSendOn.setDisable(false);
               ckSentOn.setDisable(false);
               ckEvEndDeadline.setDisable(false);
               ckEvEnded.setDisable(false);
               dpEvEnded.setDisable(false);
            
            }
         });
 
    }
    
    private void  setDeadline2ExpertiseInDate(){
        final Date date = dpExpertiseInDate.getDate();
        if(date == null){
            return;
        }
        CDeadline dl = MenuCache.getMenuCacheDeadlines().getDeadline(DeadlineTypeEn.DL11);
        if(dl != null){
            dpEvDeadlineToDate.setDate(dl.calculateDeadline(date));
        }else{
            dpEvDeadlineToDate.setDate(date);
            dpEvDeadlineToDate.setDisable(false);
        }
       ckEvDeadlineSet.setDisable(false);
       ckEvExtendedToDate.setDisable(false);
       ckEvStarted.setDisable(false);
       ckAnsweredIns.setDisable(false);


    }


    public List<TWmReminder> getReviewRequestReminders() throws NamingException {
        TWmRequest reqObject = getSkinnable().getRequest();
        final ArrayList<TWmReminder> remSet = new ArrayList<>();
        if (reqObject == null) {
            LOG.log(Level.SEVERE, "request object is null!");
            return remSet;
        }
        if(reqObject.isRequestReview()){
            for(ReminderLabeledCheckboxLink remCheck:reminderChecks){
                if(remCheck.isChecked()){
                    remSet.add(remCheck.setReminderData());
                }

            }
            return remSet;
        }else{
            return null;
        }
    }
}
