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

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.menu.cache.MenuCacheOptionsEn;
import de.lb.cpx.client.core.model.catalog.CpxHospital;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledLabel;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.model.TCase;
import de.lb.cpx.server.commonDB.model.CWmListProcessTopic;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmProcessCase;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.enums.WmWorkflowStateEn;
import de.lb.cpx.wm.model.enums.WmWorkflowTypeEn;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

/**
 * Dialog to add a mainCase to the process.
 *
 * @author nandola
 */
public class AddAvailableCaseDialog extends FormularDialog<TWmProcess> {

    private BooleanProperty addServiceProperty = new SimpleBooleanProperty(false);
    private final ObjectProperty<TCase> caseProperty = new SimpleObjectProperty<>();
    private ProcessServiceFacade facade;
    private final LabeledLabel labelHospital;
    private final LabeledLabel labelAdmDate;
    private final LabeledLabel labelDisDate;
    private final LabeledLabel labelCaseFinished;
    private final LabeledTextField tfCaseNumber;

    /**
     * construct new Dialog to create a new empty process and select service /
     * WmProcessCase Object
     *
     * @param pFacade ServiceFacade to handle Db/Server Access
     */
    public AddAvailableCaseDialog(ProcessServiceFacade pFacade) {
        this();
        facade = pFacade;
        AutoCompletionBinding<String> autoCompletion = TextFields.bindAutoCompletion(tfCaseNumber.getControl(), new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>() {
            @Override
            public Collection<String> call(AutoCompletionBinding.ISuggestionRequest param) {
                return facade.getMatchingCaseNumbers(param.getUserText(), facade.getPatient().getId());
            }
        });
        autoCompletion.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> event) {
                caseProperty.set(facade.getCaseForNumber(event.getCompletion()));
                updateUiWithCase(caseProperty.get());
                updateUiWithHospital(facade.findHospitalByIdent(caseProperty.get().getCsHospitalIdent()));
            }
        });
        validationSupport.registerValidator(tfCaseNumber.getControl(), new Validator<String>() {
            @Override
            public ValidationResult apply(Control t, String u) {
                ValidationResult res = new ValidationResult();
                res.addErrorIf(t, Lang.getValidationErrorNoCaseNumber(), facade.getCaseForNumber(u) == null);
                return res;
            }
        });
        getDialogSkin().getButton(ButtonType.OK).disableProperty().bind(validationSupport.invalidProperty());
    }

    public AddAvailableCaseDialog() {
        super(MainApp.getStage(), Modality.APPLICATION_MODAL, Lang.getProcessCreationTitle());

        getDialogSkin().setMinHeight(200);
//        getDialogSkin().setMaxHeight(400);
        getDialogSkin().setMinWidth(250);
//        getDialogSkin().setMaxWidth(400);

        labelHospital = new LabeledLabel(Lang.getHospital());
        labelAdmDate = new LabeledLabel(Lang.getAdmissionDate());
        labelDisDate = new LabeledLabel(Lang.getDischargeDate());
        labelCaseFinished = new LabeledLabel(Lang.getCaseStatus());
        tfCaseNumber = new LabeledTextField(Lang.getCaseNumber());

        addControls(tfCaseNumber, labelCaseFinished, labelAdmDate, labelDisDate, labelHospital);

        getDialogSkin().getSaveButtonDisableProperty().bind(caseProperty.isNull());

    }

    private void updateUiWithCase(TCase hCase) {
        labelAdmDate.setText(Lang.toDate(hCase.getCurrentLocal().getCsdAdmissionDate()));
        labelDisDate.setText(Lang.toDate(hCase.getCurrentLocal().getCsdDischargeDate()));
        labelCaseFinished.setText(hCase.getCsStatusEn().getTranslation().getValue());

    }

    private void updateUiWithHospital(CpxHospital hospital) {
        labelHospital.setText(hospital.getHosName());
        labelHospital.setTooltip(new Tooltip(hospital.toString()));
    }

    public BooleanProperty getAddServiceProperty() {
        return addServiceProperty;
    }

    @Override
    public TWmProcess onSave() {
        if (caseProperty.get() != null) {
            TWmProcessHospital process = new TWmProcessHospital();

            //List<CWmListProcessTopic> availableProcessTopics = processServiceBean.get().getAllAvailableProcessTopics(new Date()); // gives valid, undeleted, sorted (based on wm_pt_sort column) and within a valid timeframe process topics
            List<CWmListProcessTopic> availableProcessTopics = MenuCache.getMenuCacheProcessTopics().values(new Date(), MenuCacheOptionsEn.IGNORE_INACTIVE);
//CPX-1193 ProcessTopics are sortes  by wmPtSort 
//            Collections.sort(availableProcessTopics);            
            process.setProcessTopic(availableProcessTopics.get(0).getWmPtInternalId());    // set the first (sorted) process topic to the process of type hospital

            process.setWorkflowState(WmWorkflowStateEn.offen);
            process.setWorkflowType(WmWorkflowTypeEn.statKH);
            process.setAssignedUser(Session.instance().getCpxUserId()); // to set assigned user during process creation.
            process.setProcessModificationUser(Session.instance().getCpxUserId());
            TWmProcessCase procCase = new TWmProcessCase();
            procCase.setHosCase(caseProperty.get());
//            facade.storeProcessCase(procCase);

            process.setPatient(procCase.getHosCase().getPatient());
            process.setMainCase(procCase.getHosCase());
            process = (TWmProcessHospital) facade.storeProcess(process);
//            System.out.println("Empty process is created and stored!!");
            return process;
        }
        return null;
    }
}
