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
 *    2016  wilde, nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.editor.CatalogValidationResult;
import de.lb.cpx.client.app.wm.util.texttemplate.TextTemplateController;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCheckBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextArea;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.connector.EjbConnector;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.enums.TextTemplateTypeEn;
import de.lb.cpx.server.commonDB.model.CWmListReminderSubject;
import de.lb.cpx.service.ejb.ProcessServiceBeanRemote;
import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.wm.model.TWmReminder;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.skin.TextAreaSkin;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

/**
 * Dialog to add a Reminder ToDo: test correct finishedDate bahaviour
 * TODO:Refactor search for user
 *
 * @author wilde
 */
public class AddReminderDialog extends FormularDialog<TWmReminder> {

    private static final Logger LOG = Logger.getLogger(AddReminderDialog.class.getName());
    protected BooleanProperty addReminderProperty = new SimpleBooleanProperty(false);
    private ProcessServiceFacade serviceFacade;
    private LabeledComboBox<CWmListReminderSubject> cbReminderType;
    private LabeledTextField cbAssignedUser;
    private LabeledTextArea taComment;
    private LabeledCheckBox chkbReminderFinished;
    private LabeledCheckBox chkbHighPriority;
    private final LabeledDatePicker dpDueTo;
    private final LabeledDatePicker dpFinishedDate;
    private final TWmReminder mReminder;
//    private TWmRequest mRequest = null;
    private final List<CWmListReminderSubject> listofReminderSubjectObjects;
    private final EjbProxy<ProcessServiceBeanRemote> processServiceBean;
    private final CatalogValidationResult catalogValidationResult;

    public AddReminderDialog(ProcessServiceFacade pServiceFacade) {
        this(pServiceFacade, null);
    }

    public AddReminderDialog(ProcessServiceFacade pServiceFacade, final TWmReminder pReminder) {
        this(pServiceFacade, pReminder, MainApp.getStage());
    }

//    public AddReminderDialog(ProcessServiceFacade pServiceFacade, final TWmReminder pReminder, TWmRequest pRequest, Window pOwner) {
//        this(pServiceFacade, pReminder, pOwner);
//        mRequest = pRequest;
//    }
    public AddReminderDialog(ProcessServiceFacade pServiceFacade, final TWmReminder pReminder, Window pOwner) {
        super(pOwner, Modality.APPLICATION_MODAL, pReminder == null ? Lang.getAdd() : Lang.getReminderUpdateHeader());
        serviceFacade = pServiceFacade;

        if (pReminder == null) {
            mReminder = new TWmReminder();
            mReminder.setCreationUser(Session.instance().getCpxUserId());
            mReminder.setCreationDate(new Date());
            // mReminder.setFinishingDate(new Date());
        } else {
            mReminder = pReminder;
        }

        Session session = Session.instance();
        EjbConnector connector = session.getEjbConnector();
        processServiceBean = connector.connectProcessServiceBean();
        //listofReminderSubjectObjects = processServiceBean.get().getAllReminderSubjects(new Date());
        listofReminderSubjectObjects = MenuCache.getMenuCacheReminderSubjects().values(new Date(), MenuCacheOptionsEn.IGNORE_INACTIVE);
        cbReminderType = new LabeledComboBox<>(Lang.getReminderType());
        // eminderSubjects sort by wmRsSort from server
//        Collections.sort(listofReminderSubjectObjects);
        cbReminderType.setItems(listofReminderSubjectObjects);

        cbReminderType.setConverter(new StringConverter<CWmListReminderSubject>() {
            @Override
            public String toString(CWmListReminderSubject object) {
                LOG.log(Level.FINEST, "reminder type: {0}", object == null ? "" : object.getWmRsName());
                return object == null ? "" : object.getWmRsName();
            }

            @Override
            public CWmListReminderSubject fromString(String string) {
                return null;
            }
        });

        //CWmListReminderSubject CWmListReminderSubject = processServiceBean.get().getReminderSubjectByInternalId(mReminder.getSubject());
        CWmListReminderSubject reminderSubject = MenuCache.getMenuCacheReminderSubjects().get(mReminder.getSubject());
        cbReminderType.select(reminderSubject);
        //CPX- 646 RSH 16.10.2017 /Search function for input WV receiver/
        cbAssignedUser = new LabeledTextField();
        if (mReminder.getAssignedUserId() != 0L) {
            String userName = MenuCache.instance().getUserNameForId(mReminder.getAssignedUserId());
            if (userName.isEmpty()) {
                //fallback if client MenuCache is not up to date (this is maybe not necessary and can be removed in the future)
                userName = processServiceBean.get().getUserById(mReminder.getAssignedUserId());
            }
            cbAssignedUser.setText(userName);
        }
        cbAssignedUser.setTitle(Lang.getReminderReceiver());
        AutoCompletionBinding<UserDTO> user = TextFields.bindAutoCompletion(cbAssignedUser.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<UserDTO>>() {
            @Override
            public Collection<UserDTO> call(AutoCompletionBinding.ISuggestionRequest param) {
                //return processServiceBean.get().getMatchingUsers(param.getUserText(), dpDueTo.getDate());
                return MenuCache.getMenuCacheUsers().getValidMatchForUser(param.getUserText(), dpDueTo.getDate());
            }
        });
        user.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<UserDTO>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<UserDTO> event) {
//                cbAssignedUser.setText(cbAssignedUser.getText());
//                        cbAssignedUser.setText(String.valueOf(mReminder.getAssignedUserId()));
//mReminder.setAssignedUserId(cbAssignedUser.getText());
            }

        });

        dpDueTo = new LabeledDatePicker(Lang.getDurationTo());
        dpFinishedDate = new LabeledDatePicker(Lang.getReminderFinishedDate());
        if (pReminder == null) {
            dpDueTo.setDate(new Date());
//            dpFinishedDate.setDate(new Date());
            dpFinishedDate.setDisable(true);
        } else {
            dpDueTo.setDate(mReminder.getDueDate());
            dpFinishedDate.setDate(mReminder.getFinishedDate());
        }

        taComment = new LabeledTextArea(Lang.getComment(), 255);
        taComment.setText(mReminder.getComment());

        //add custom MenuItem to the ContextMenu
        if (serviceFacade.getCurrentProcess() != null) {
            TextTemplateController textTemplateController = new TextTemplateController(TextTemplateTypeEn.ReminderContext, serviceFacade, getScene().getWindow(), serviceFacade.getCurrentProcess().getMainCase());
            TextAreaSkin customContextSkin = textTemplateController.customContextSkin(taComment.getControl());
//        TextAreaSkin customContextSkin = new TextTemplateController(TextTemplateTypeEn.ReminderContext, serviceFacade, getWindow()).customContextSkin(taComment.getControl());
            taComment.getControl().setSkin(customContextSkin);
        }
        chkbReminderFinished = new LabeledCheckBox(Lang.getReminderFinished());

        chkbReminderFinished.setSelected(mReminder.isFinished());
        if (chkbReminderFinished.isSelected()) {
            dpDueTo.setDisable(true);
            cbAssignedUser.setDisable(true);
            dpFinishedDate.setDisable(false);
        } else {
            dpDueTo.setDisable(false);
            cbAssignedUser.setDisable(false);
            dpFinishedDate.setDisable(true);
        }
        chkbHighPriority = new LabeledCheckBox(Lang.getReminderHighPriority());

        chkbHighPriority.setSelected(mReminder.isHighPrio());
        //comment in
        addControls(cbReminderType, cbAssignedUser, dpDueTo, taComment, chkbReminderFinished, dpFinishedDate, chkbHighPriority);

        Platform.runLater(() -> {
            validationSupport.initInitialDecoration();
            validationSupport.registerValidator(taComment.getControl(), new Validator<String>() {
                @Override
                public ValidationResult apply(Control t, String u) {
                    ValidationResult res = new ValidationResult();
                    res.addErrorIf(t, Lang.getWiedervorlageCommentInfo(), u != null && u.length() > taComment.getMaxSize());
                    return res;
                }
            });
            validationSupport.registerValidator(cbAssignedUser.getControl(), new Validator<String>() {
                @Override
                public ValidationResult apply(Control t, String u) {
                    ValidationResult res = new ValidationResult();
                    res.addErrorIf(t, Lang.getReminderValidationWarningNoReceiver(), u == null || u.isEmpty());
                    res.addErrorIf(t, Lang.getReminderValidationErrorNoValidReceiver(), getUserID(u) == null);
//                        res.addErrorIf(t, Lang.getAuthorizationDilaogMessage(Session.instance().getCpxActualRoleId()), !Session.instance().getRoleProperties().isEditReminderAllowed()&&pReminder!=null);
                    return res;
                }
            });
        });

        chkbReminderFinished.getControl().selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                if (chkbReminderFinished.isSelected()) {
                    dpDueTo.setDisable(true);
                    cbAssignedUser.setDisable(true);
                    dpFinishedDate.setDisable(false);
                    dpFinishedDate.setDate(new Date());
                } else {
                    dpDueTo.setDisable(false);
                    cbAssignedUser.setDisable(false);
                    dpFinishedDate.setDisable(true);
                }
            }
        });
        catalogValidationResult = new CatalogValidationResult();
        catalogValidationResult.add(CpxErrorTypeEn.WARNING, Lang.getValidationMasterdataNoReminderTypesFound(), (t) -> {
            return MenuCache.getMenuCacheReminderSubjects().values(MenuCacheOptionsEn.IGNORE_INACTIVE).isEmpty();
        });
        catalogValidationResult.add(CpxErrorTypeEn.WARNING, Lang.getValidationMasterdataNoUsersFound(), (Void t) ->{
            return MenuCache.getMenuCacheUsers().values(dpDueTo.getDate(), MenuCacheOptionsEn.IGNORE_INACTIVE).isEmpty();
        });
        setMessageText(catalogValidationResult.getValidationMessages());
        setMessageType(catalogValidationResult.getHighestErrorType());
    }

    public CatalogValidationResult getCatalogValidationResult() {
        return catalogValidationResult;
    }

    /*
     * get Reminder Object from the current inserted Userdata
     * of no Reminder Object was set, a new one is created otherwise the reminderobject is updated with the current values
     * @return TWmReminder entity filled with user data
     */
    public TWmReminder getReminderObject() {
        //TWmReminder reminder = new TWmReminder();
        TWmReminder reminder = mReminder;
        if (taComment.getText() != null && taComment.getText().length() > 256) {
            AlertDialog dialog = AlertDialog.createInformationDialog(Lang.getWiedervorlageCommentInfo(), getDialogPane().getScene().getWindow());
            dialog.setHeaderText(Lang.getWiedervorlageCommentDialog());
            dialog.setResizable(true);
            dialog.getDialogPane().setPrefSize(550, 150);
//            dialog.setContentText(Lang.getWiedervorlageCommentInfo());
            dialog.showAndWait();
            reminder.setComment(taComment.getText().substring(0, 255));
        } else {
            reminder.setComment(taComment.getText());
        }

        if (dpDueTo.getDate() != null) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(dpDueTo.getDate());
//        cal.set(Calendar.ti, dueDate.getTime());
            cal.set(Calendar.HOUR, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));
            cal.set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND));
            cal.set(Calendar.MILLISECOND, Calendar.getInstance().get(Calendar.MILLISECOND));

            reminder.setDueDate(cal.getTime());
        }
        if (dpFinishedDate.getDate() != null && chkbReminderFinished.isChecked()) {
            reminder.setFinishedDate(dpFinishedDate.getDate());
        }
//        reminder.setSubject(cbReminderType.getSelectedItem());
        reminder.setSubject(cbReminderType.getSelectedItem() != null ? cbReminderType.getSelectedItem().getWmRsInternalId() : -1);

//        if (chkbReminderFinished.isChecked() && reminder.getFinishingDate() == null) {
//            reminder.setFinishingDate(new Date());
//        }
        reminder.setFinished(chkbReminderFinished.isChecked());
        reminder.setHighPrio(chkbHighPriority.isChecked());
        try {
            //CPX- 646 RSH 16.10.2017
//        if (cbAssignedUser.getSelectedItem() != null) {
//            reminder.setAssignedUserId(cbAssignedUser.getSelectedItem().getId());
//        }
            if (cbAssignedUser.getText().getBytes(CpxSystemProperties.DEFAULT_ENCODING).length > 0 && cbAssignedUser.getText() != null) {
                reminder.setAssignedUserId(getUserID(cbAssignedUser.getText()));

            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AddReminderDialog.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return reminder;
    }

    public BooleanProperty getAddReminderProperty() {
        return addReminderProperty;
    }

    @Override
    public TWmReminder onSave() {

        TWmReminder reminder = getReminderObject();
//        if (serviceFacade != null) {
//            if (mRequest == null) {
        if (serviceFacade != null && serviceFacade.getCurrentProcess() != null) {
            serviceFacade.storeReminder(reminder);
        }

//            } else {
//                serviceFacade.storeReminderForRequest(reminder, mRequest);
//            }
//        }
        return reminder;
    }

    /**
     * RISKY, may result in alot of server queries!
     *
     * @param pUName username
     * @return Id der username
     */
    private Long getUserID(String pUName) {
        if (pUName == null || pUName.trim().isEmpty()) {
            return 0L;
        } else {
            return serviceFacade.getUserID(pUName);
        }
    }

    protected final void disableController() {
        cbReminderType.setDisable(true);
        cbAssignedUser.setDisable(true);
        taComment.setDisable(true);
        chkbReminderFinished.setDisable(true);
        chkbHighPriority.setDisable(true);
        dpDueTo.setDisable(true);
        dpFinishedDate.setDisable(true);

    }

}
