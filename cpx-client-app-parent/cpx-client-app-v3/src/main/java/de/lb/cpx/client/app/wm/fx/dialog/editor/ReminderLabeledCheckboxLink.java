/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCheckboxLink;
import de.lb.cpx.server.commonDB.model.CWmListReminderSubject;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmReminder;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javax.naming.NamingException;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 *
 * @author gerschmann
 */
public class ReminderLabeledCheckboxLink extends LabeledCheckboxLink{
    
    private ObjectProperty <ReminderComponents> reminder;
//    WmReminderTypeEn reminderType;
    private static final Logger LOG = Logger.getLogger(ReminderLabeledCheckboxLink.class.getName());
    
    /**
     * no-arg for scenebuilder, default title is: Label
     */
    public ReminderLabeledCheckboxLink() {
        this("Label");
    }

    /**
     * creates a new labeled Checkbox
     *
     * @param pLabel label to set above the control
     */
    public ReminderLabeledCheckboxLink(String pLabel) {
        super(pLabel);
    }

//    public WmReminderTypeEn getReminderType() {
//        return reminderType;
//    }
//
//    public void setReminderType(WmReminderTypeEn reminderType) {
//        this.reminderType = reminderType;
//    }

    public void setupListeners(){
        setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getReminderComponents().showWvPopover(getControl());
            }
        });
        
    }
    private ObjectProperty<ValidationSupport> validationSupportProperty;

    public ObjectProperty<ValidationSupport> validationSupportProperty() {
        if (validationSupportProperty == null) {
            validationSupportProperty = new SimpleObjectProperty<>();
        }
        return validationSupportProperty;
    }

    public ValidationSupport getValidationSupport() {
        return validationSupportProperty().get();
    }

    public void setValidationSupport(ValidationSupport pSupport) {
        validationSupportProperty().set(pSupport);
    }
    
    public void setupValidation(){
        getValidationSupport().registerValidator(getReminderComponents().getUserCtrl().getControl(), new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, Lang.getReminderValidationWarningNoReceiver(), isChecked() && (u == null || u.isEmpty()));

                try {
                    res.addErrorIf(t, Lang.getReminderValidationErrorNoValidReceiver(), isChecked() && MenuCache.instance().getUserId(u) == null);
                } catch (NamingException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
                return res;
            }
        });
        
    }
    
    public ReminderComponents getReminderComponents(){
        return reminder().get();
    }

    private ObjectProperty<ReminderComponents> reminder() {
        if(reminder == null){
            reminder = new SimpleObjectProperty<>();
            reminder.set(new ReminderComponents());
        }
        return reminder;
    }
    
    public void setReminderComponents(ReminderComponents pReminder){
        reminder().set(pReminder);
    }
    
    public void setReminderDueTo(Date date, CWmListReminderSubject pReminderType){
        getReminderComponents().setDueTo(date);
        if(getReminderComponents().getRemTypeInternalId() == 0L){
            getReminderComponents().setRemType(pReminderType);
        }
    }
    
    public TWmReminder setReminderData()throws NamingException{
        TWmReminder rem = new TWmReminder();
        getReminderComponents().setReminderData(rem);
        return rem;
    }
    
//    public enum WmReminderTypeEn {
//
//        MD_DOCUMENT_DELIVERED, MD_CONTINUATION_FEE, MD_SUBSEQUENT_PROCEEDING,
//        REVIEW_DEADLINE_TO_DATE, REVIEW_EXTENDED_TO_DATE, REVIEW_ANWER_INS_REMINDER,
//        REVIEW_DOCUMENT_SEND_ON, REVIEW_DOCUMENT_WAS_SENT_ON, REVIEW_END_OF_DEADLINE, REVIEW_COMPLETED;
//
//    }
}
