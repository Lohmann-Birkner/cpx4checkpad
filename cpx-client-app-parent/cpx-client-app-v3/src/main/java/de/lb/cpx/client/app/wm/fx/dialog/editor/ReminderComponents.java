/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.checked_combobox.CpxComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCheckBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.server.commonDB.model.CWmListReminderSubject;
import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmReminder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.naming.NamingException;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 *
 * @author Shahin
 */
public class ReminderComponents {

    private LabeledComboBox<Map.Entry<Long, String>> cbRemType;
    private LabeledTextField cbAssUser;
    private LabeledDatePicker dpDue;
    private LabeledTextArea taComment;
    private LabeledCheckBox chKbHPrio;
    //private EjbProxy<ProcessServiceBeanRemote> processServiceBean;
//    private ValidationSupport m_validationSupport;

    public ReminderComponents() {
//        m_validationSupport = new ValidationSupport();

        Set<Map.Entry<Long, String>> reminderSubject = MenuCache.instance().getReminderSubjectEntries();
        //processServiceBean = Session.instance().getEjbConnector().connectProcessServiceBean();
        cbRemType = new LabeledComboBox<>(Lang.getReminderType());
        cbRemType.getItems().addAll(new ArrayList<>(reminderSubject));
        cbRemType.setConverter(new StringConverter<Map.Entry<Long, String>>() {
            @Override
            public String toString(Map.Entry<Long, String> object) {
                return object == null ? "" : object.getValue();
            }

            @Override
            public Map.Entry<Long, String> fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        dpDue = new LabeledDatePicker(Lang.getDurationTo());
        cbAssUser = new LabeledTextField(Lang.getReminderReceiver());
        taComment = new LabeledTextArea(Lang.getComment(), 255);
        chKbHPrio = new LabeledCheckBox(Lang.getReminderHighPriority());
        AutoCompletionBinding<UserDTO> userWvDocDeliDead = TextFields.bindAutoCompletion(cbAssUser.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<UserDTO>>() {
            @Override
            public Collection<UserDTO> call(AutoCompletionBinding.ISuggestionRequest param) {
                //return processServiceBean.get().getMatchingUsers(param.getUserText(), dpDue.getDate() != null ? dpDue.getDate() : new Date());
                return MenuCache.getMenuCacheUsers().getValidMatchForUser(param.getUserText(), dpDue.getDate() != null ? dpDue.getDate() : new Date());
            }
        });
        userWvDocDeliDead.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<UserDTO>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<UserDTO> event) {
                String str = cbAssUser.getText();
                setAssUser(str);

            }
        });

    }

    public LabeledTextField getUserCtrl() {
        return cbAssUser;

    }

    public CpxComboBox<Map.Entry<Long, String>> getComboBox() {
        return cbRemType.getControl();
    }

    /**
     *
     * @return UserId
     * @throws NamingException exception
     */
    public Long getAssUser() throws NamingException {
        final Long userId = MenuCache.instance().getUserId(cbAssUser.getText());
        return !cbAssUser.getText().isEmpty() || userId != null ? userId : Session.instance().getCpxUserId();
    }

    /**
     *
     * @return RemTypeId
     */
    public long getRemTypeInternalId() {
        final Long internalId = cbRemType.getSelectedItem() == null ? null : cbRemType.getSelectedItem().getKey();
        return internalId != null ? internalId : 0L;
    }

    /**
     *
     * @return CWmListReminderSubject
     */
    public CWmListReminderSubject getRemType() {
        long internalId = getRemTypeInternalId();
        return internalId == 0L ? null : MenuCache.instance().getReminderForInternalId(internalId);
    }

    /**
     *
     * @return Reminder DueToDate
     */
    public Date getDueTo() {
        Calendar cal = Calendar.getInstance();
//         cal.getTime();
//        cal.setTime(dpDue.getDate() != null ? dpDue.getDate() :cal.getTime());
//        cal.set(Calendar.HOUR, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
//        cal.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));
//        cal.set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND));
//        cal.set(Calendar.MILLISECOND, Calendar.getInstance().get(Calendar.MILLISECOND));
        return dpDue.getDate() != null ? dpDue.getDate() : cal.getTime();

    }

    /**
     *
     * @return Reminder Comment
     */
    public String getComment() {
        return taComment.getText();
    }

    /**
     *
     * @return Reminder Prio
     */
    public Boolean getPrio() {
        return chKbHPrio.isChecked();

    }

    /**
     *
     * @param user user
     */
    public void setAssUser(String user) {
        cbAssUser.setText(user);
    }

    /**
     *
     * @param pRemInternalId Reminder Type to set
     * @return reminder type successfully set?
     */
    public boolean setRemType(long pRemInternalId) {
        for (Map.Entry<Long, String> entry : new ArrayList<>(getComboBox().getItems())) {
            if (entry != null && entry.getKey() != null && entry.getKey().equals(pRemInternalId)) {
                cbRemType.getControl().getSelectionModel().select(entry);
                return true;
            }
        }
        return false;
//        cbRemType.getControl().getSelectionModel().select(pRemInternalId);
    }

    /**
     *
     * @param pRemType Reminder Type to set
     * @return reminder type successfully set?
     */
    public boolean setRemType(CWmListReminderSubject pRemType) {
        if (pRemType == null) {
            return false;
        }
        return setRemType(pRemType.getWmRsInternalId());
    }

    /**
     *
     * @param dueTo Reminder DueToDate to set
     */
    public void setDueTo(Date dueTo) {
//        Calendar cal = Calendar.getInstance();
//        if (dueTo != null) {
//            cal.setTime(dueTo);
//            cal.set(cal.HOUR, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
//            cal.set(cal.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));
//            cal.set(cal.SECOND, Calendar.getInstance().get(Calendar.SECOND));
//            cal.set(cal.MILLISECOND, Calendar.getInstance().get(Calendar.MILLISECOND));
//
//            dpDue.setDate(cal.getTime());
        dpDue.setDate(dueTo);
//        }

    }

    /**
     *
     * @param Text Reminder Comment to set
     */
    public void setComment(String Text) {
        taComment.setText(Text);
    }

    /**
     *
     * @param prio Reminder Prio to set
     */
    public void setPrio(boolean prio) {
        chKbHPrio.setSelected(prio);

    }

    /**
     *
     * @param chlink Show Reminder Components as Popover
     */
    void showWvPopover(Node chlink) {

        VBox vBox = new VBox();
        taComment.setMaxHeight(100);
        taComment.setMaxWidth(250);
        HBox hbox = new HBox();
        hbox.setSpacing(12.0);
        vBox.setSpacing(12);
//        HBox.setHgrow(hbox, Priority.ALWAYS);
        hbox.getChildren().addAll(dpDue, chKbHPrio);
        vBox.getChildren().addAll(cbAssUser, cbRemType, hbox, taComment);
        vBox.setPadding(new Insets(12.0, 12.0, 12.0, 12.0));
        PopOver popover = new PopOver();
        popover.setHideOnEscape(true);
        popover.setAutoHide(true);
        popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popover.setDetachable(false);

        popover.setContentNode(vBox);
        popover.show(chlink);
    }
    
    public void setReminderData(TWmReminder pReminder) throws NamingException {
            pReminder.setCreationDate(new Date());
            pReminder.setCreationUser(Session.instance().getCpxUserId());
            pReminder.setAssignedUserId(getAssUser());
            pReminder.setSubject(getRemTypeInternalId());

            pReminder.setDueDate(getDueTo());
            pReminder.setComment(getComment());
            pReminder.setHighPrio(getPrio());
        
    }

}
