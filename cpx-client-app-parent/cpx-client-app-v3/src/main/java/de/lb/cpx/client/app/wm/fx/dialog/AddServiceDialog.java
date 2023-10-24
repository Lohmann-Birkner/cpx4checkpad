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

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.editor.CatalogValidationResult;
import de.lb.cpx.client.core.model.catalog.CpxHospital;
import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialogSkin;
import de.lb.cpx.client.core.model.fx.labeled.LabeledLabel;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.model.TCase;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmProcessCase;
import java.util.Collection;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

/**
 * Dialog to add a service in the process overview
 *
 * @author wilde
 */
public class AddServiceDialog extends FormularDialog<TWmProcessCase> {

//    private CpxScene scene;
//    private AddServiceDialogFXMLController controller;
    private BooleanProperty addServiceProperty = new SimpleBooleanProperty(false);
    private final ObjectProperty<TCase> caseProperty = new SimpleObjectProperty<>();
//    private Stage stage;
    private ProcessServiceFacade facade;
    private final LabeledLabel labelHospital;
    private final LabeledLabel labelAdmDate;
    private final LabeledLabel labelDisDate;
    private final LabeledLabel labelCaseFinished;
    private final LabeledTextField tfCaseNumber;
//    private CatalogValidationResult catalogValidationResult;
    private SimpleObjectProperty <CatalogValidationResult>catalogValidationResult;

    /**
     * construct new Dialog to create a new service / WmProcessCase Object
     *
     * @param pFacade ServiceFacade to handle Db/Server Access
     */
    public AddServiceDialog(ProcessServiceFacade pFacade) {
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
                res.addErrorIf(t, Lang.getValidationErrorCaseAlreadyExists(u), caseNumberAlreadyExists(u));
                res.addErrorIf(t, Lang.getValidationErrorNoCaseNumber(), facade.getCaseForNumber(u) == null);
                return res;
            }

            private boolean caseNumberAlreadyExists(String pCaseNumber) {
                TWmProcess currentProcess = facade.getCurrentProcess();
                boolean found = false;
                for (TWmProcessCase procCase : currentProcess.getProcessCases()) {
                    if (procCase.getHosCase() != null){
                        if( procCase.getHosCase().getCsCaseNumber().equals(pCaseNumber)) {                       
                            found = true;
                            break;
                        }
                    }
                }
                if(found){
//                    resetCaseFields();
                     setMessage( Lang.getValidationErrorCaseAlreadyExists(pCaseNumber), CpxErrorTypeEn.ERROR);
                    return true;
                }
                found = facade.isCaseNumber4PatientValid(pCaseNumber);

                if(found){
                    resetCaseFields();
                    setMessage( Lang.getValidationCaseNumberForPatientInvalid(), CpxErrorTypeEn.ERROR);
                }
//                if(!found){
//                    setMessage("" //Lang.getValidationSelectCaseFromList()
//                            , CpxErrorTypeEn.INFO);
//                     
//                }
                return found;
            }
        });
        getDialogSkin().getButton(ButtonType.OK).disableProperty().bind(validationSupport.invalidProperty());

        setMessage(Lang.getValidationSelectCaseFromList(), CpxErrorTypeEn.INFO);
                     

        ((VBox)getDialogSkin().getControlContainer()).setPrefWidth(380);
        ((VBox)getDialogSkin().getControlContainer()).setPrefHeight(280); 
    }
    
    private void setMessage(String pMessage, CpxErrorTypeEn pErrorType){
        
        boolean hasHospitals = facade.hasCatalogHospitals();
//        if(pMessage.length() != 0 || !hasHospitals){
//            getDialogSkin().showMessageNode();
//        }
        pMessage = hasHospitals?pMessage:("\n" + pMessage);
        getCatalogValidationResult().reset();
        getCatalogValidationResult().add(CpxErrorTypeEn.WARNING, Lang.getValidationCatalogdataNoHospitalsFound(), (t) -> {
            return !hasHospitals;
        });
        setMessageText(getCatalogValidationResult().getValidationMessages());
        setMessageType(getCatalogValidationResult().getHighestErrorType());
//        if(pMessage.length() == 0){
//            return;
//        }
        getCatalogValidationResult().add(pErrorType, pMessage, (t) -> {
            return true;
        });
        setMessageText(getCatalogValidationResult().getValidationMessages());
        setMessageType(getCatalogValidationResult().getHighestErrorType());
        
        
    }

    private AddServiceDialog() {
        super(MainApp.getStage(), Modality.APPLICATION_MODAL, Lang.getWmServiceCreationTitle());
//        getDialogSkin().setMinHeight(200);
//        getDialogSkin().setMinWidth(250);

        labelHospital = new LabeledLabel(Lang.getHospital());
        labelAdmDate = new LabeledLabel(Lang.getAdmissionDate());
        labelDisDate = new LabeledLabel(Lang.getDischargeDate());
        labelCaseFinished = new LabeledLabel(Lang.getCaseStatus());
        tfCaseNumber = new LabeledTextField(Lang.getCaseNumber());
        
        addControls(tfCaseNumber, labelCaseFinished, labelAdmDate, labelDisDate, labelHospital);

        getDialogSkin().getSaveButtonDisableProperty().bind(caseProperty.isNull());

    }

    public CatalogValidationResult getCatalogValidationResult() {
        return catalogValidationResultProperty().get();
    }
    
    private SimpleObjectProperty<CatalogValidationResult> catalogValidationResultProperty(){
        if(catalogValidationResult == null){
            catalogValidationResult = new SimpleObjectProperty<CatalogValidationResult>();
            catalogValidationResult.set(new CatalogValidationResult());
        }
        return catalogValidationResult;
    }


    private void updateUiWithCase(TCase hCase) {
        labelAdmDate.setText(Lang.toDate(hCase.getCurrentLocal().getCsdAdmissionDate()));
        labelDisDate.setText(Lang.toDate(hCase.getCurrentLocal().getCsdDischargeDate()));
        labelCaseFinished.setText(hCase.getCsStatusEn().getTranslation().getValue());

    }
    
    private void resetCaseFields(){
        labelAdmDate.setText("");
        labelDisDate.setText("");
        labelCaseFinished.setText("");
        
    }
    private void updateUiWithHospital(CpxHospital hospital) {
        labelHospital.setText(hospital.getHosName());
        labelHospital.setTooltip(new Tooltip(hospital.toString()));
        if(hospital.getId() == 0L){
             setMessage(Lang.getValidationCatalogdataNoHospitalExists(), CpxErrorTypeEn.WARNING);

        }
    }

    public BooleanProperty getAddServiceProperty() {
        return addServiceProperty;
    }

    @Override
    public TWmProcessCase onSave() {
        if (caseProperty.get() != null) {
            TWmProcessCase procCase = new TWmProcessCase();
            procCase.setHosCase(caseProperty.get());
            return facade.storeProcessCase(procCase);
        }
        return null;
    }
}
