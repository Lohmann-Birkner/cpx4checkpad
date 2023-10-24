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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.wizard;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.addcasewizard.fx.control.AddCaseHospitalCaseDataFXMLController;
import de.lb.cpx.client.app.addcasewizard.fx.control.AddCasePatientDataFXMLController;
import de.lb.cpx.client.app.addcasewizard.fx.control.AddCaseResolveCaseFXMLController;
import de.lb.cpx.client.app.service.facade.AddCaseServiceFacade;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.dialog.DialogSkin;
import de.lb.cpx.client.core.model.fx.wizard.Wizard;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.client.core.util.UndecoratedWindowHelper;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

/**
 * implements the add case wizard extends custom wizard implementation
 *
 * @author wilde
 */
public class AddCaseWizard extends Wizard {

    private static final Logger LOG = Logger.getLogger(AddCaseWizard.class.getName());
    private static final Double PREF_DIALOG_HEIGHT = 800.0d;

    private DialogSkin<ButtonType> skin;
//    private final ValidationSupport validationSupport;
    private final boolean anonymous;
    private CpxScene firstStep;
    private final AddCaseServiceFacade addCaseFacade;
    private CpxScene secondStep;
    private CpxScene thirdStep;
    private final AddCaseResolveCaseFXMLController thirdCtrl;
    private final AddCaseHospitalCaseDataFXMLController secondCtrl;
    private final AddCasePatientDataFXMLController firstCtrl;

    public AddCaseWizard(Window pOwner, Modality pModatity, String pTitle, boolean pAnonymous) throws IOException {
        super(pOwner, pModatity, pTitle);
        this.anonymous = pAnonymous;
        skin = new DialogSkin<>(getDialog());
        addCaseFacade = new AddCaseServiceFacade();
        getDialog().getDialogPane().setContent(null);

        firstStep = CpxFXMLLoader.getScene(AddCasePatientDataFXMLController.class);
        firstStep.initOwner((Stage) this.getDialog().getDialogPane().getScene().getWindow());
        firstCtrl = (AddCasePatientDataFXMLController) firstStep.getController();
        ValidatedWizardPane<AddCasePatientDataFXMLController> patientPane = new ValidatedWizardPane<>(firstCtrl, firstCtrl.getValidationSupport());
        patientPane.getContent();
        UndecoratedWindowHelper.enableDragOn(patientPane.getSkin().getHeaderGrid(), skin.getStage());
        UndecoratedWindowHelper.enableDragOn(patientPane.getSkin().getButtonBar(), skin.getStage());
        patientPane.setOnEnteringPane(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                computePrefHeight(patientPane, skin);
                patientPane.setHeaderText(Lang.getAddCaseTitleExtended(Lang.getPatientDetails(), 1, 3));
                setOnCheckBusy(null);
//                firstCtrl.init(anonymous, addCaseFacade); 
// anonymous - flag is for use in the secondCtrl in order to anonymaze the birth date of the patient; 
// the patient data is to be  anonymazed by the user which creates the case
                firstCtrl.init(false, addCaseFacade);
            }
        });
        patientPane.setOnExitingPage(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    firstCtrl.cachePatient();
                } catch (CpxIllegalArgumentException ex) {
                    Logger.getLogger(AddCaseWizard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        secondStep = CpxFXMLLoader.getScene(AddCaseHospitalCaseDataFXMLController.class);
        secondStep.initOwner((Stage) this.getDialog().getDialogPane().getScene().getWindow());
        secondCtrl = (AddCaseHospitalCaseDataFXMLController) secondStep.getController();
        ValidatedWizardPane<AddCaseHospitalCaseDataFXMLController> hospitalPane = new ValidatedWizardPane<>(secondCtrl, secondCtrl.getValidationSupport());
        UndecoratedWindowHelper.enableDragOn(hospitalPane.getSkin().getHeaderGrid(), skin.getStage());
        UndecoratedWindowHelper.enableDragOn(hospitalPane.getSkin().getButtonBar(), skin.getStage());
        hospitalPane.setOnEnteringPane(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hospitalPane.setHeaderText(Lang.getAddCaseTitleExtended(Lang.getCaseDetails(), 2, 3));
                try {
                computePrefHeight(hospitalPane, skin);
                secondCtrl.init(anonymous, addCaseFacade);
                setOnCheckBusy(new Callback<Void, Boolean>(){
                    public Boolean call(Void v){
                        return secondCtrl.isBusy();
                    }
                });
                } catch (IOException ex) {
                    Logger.getLogger(AddCaseWizard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        hospitalPane.setOnExitingPage(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                try {
                secondCtrl.cacheCaseData();
//                setOnCheckBusy(null);
//                } catch (CpxIllegalArgumentException ex) {
//                    Logger.getLogger(AddCaseWizard.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        });
        
        thirdStep = CpxFXMLLoader.getScene(AddCaseResolveCaseFXMLController.class);
        thirdStep.initOwner((Stage) this.getDialog().getDialogPane().getScene().getWindow());
        thirdCtrl = (AddCaseResolveCaseFXMLController) thirdStep.getController();
        ValidatedWizardPane<AddCaseResolveCaseFXMLController> resolvePane = new ValidatedWizardPane<>(thirdCtrl, thirdCtrl.getValidationSupport());
        UndecoratedWindowHelper.enableDragOn(resolvePane.getSkin().getHeaderGrid(), skin.getStage());
        UndecoratedWindowHelper.enableDragOn(resolvePane.getSkin().getButtonBar(), skin.getStage());
        resolvePane.setOnEnteringPane(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resolvePane.setHeaderText(Lang.getAddCaseTitleExtended(Lang.getCaseFileFinalisation(), 3, 3));
                try {
                    computePrefHeight(resolvePane, skin);
                    thirdCtrl.init(anonymous, addCaseFacade);
                    setOnCheckBusy(null);
                    if(resolvePane.isVisited()){
                        //group only if it was already visited to update for possible changes
                        //better way is to detected if case changed by user and only than group again
                        //it is possible to group here unnecessarily, for example when admission mode changed
                        //by user so will the listener fire on update and this will trigger
                        //but ... its only for reprü so who cares??
                        thirdCtrl.performGroup();
                    }
                } catch (CpxIllegalArgumentException ex) {
                    Logger.getLogger(AddCaseWizard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // create and assign the flow
        setFlow(new LinearFlow(patientPane, hospitalPane, resolvePane));

        getDialog().setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                //CPX-977 esc-taste /Abbrechen Mode
                Scene scene = getDialog().getDialogPane().getScene();
                if (getDialog().getDialogPane().getScene() != null) {
                    AlertDialog.createConfirmationDialog(Lang.getAddCaseCancelConfirmation(), scene.getWindow()).showAndWait().ifPresent(new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType t) {
                            if (t.equals(ButtonType.CANCEL)) {
                                event.consume();
                            }
                        }
                    });
                }
                LOG.info("on close wizard");
            }
        });
        setOnFinish(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    //CPX-1107 If an error occurs, the dialog will addCase Dilaog open
                    Boolean result = thirdCtrl.storeCase();
                    if (result == null || !result) {
                        event.consume();
                    } else {
                        //CPX-977 esc-taste/Abbrechen Mode
                        getDialog().setOnCloseRequest(null);
                    }
                } catch (CloneNotSupportedException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    MainApp.showErrorMessageDialog(ex, Lang.getErrorOccured(), getDialog().getDialogPane().getScene().getWindow());
                    event.consume();
                }
            }
        });
        //calculate height remaining on window that dialog does not exceed bounds 
        getDialog().setOnShowing(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent t) {
                computePrefHeight(getDialog().getDialogPane(), skin);
            }
        });
    }

    public AddCaseWizard(Window pOwner, String pTitle, boolean pAnonymize) throws IOException {
        this(pOwner, Modality.APPLICATION_MODAL, pTitle, pAnonymize);
    }

    public TCase getCreatedCase() {
        return addCaseFacade.getCurrentCase();
    }

    private void computePrefHeight(Pane pPane, DialogSkin<ButtonType> skin) {
        if (skin.getCurrentScreen().getBounds().getHeight() < PREF_DIALOG_HEIGHT) {
            pPane.setPrefHeight(skin.getCurrentScreen().getBounds().getHeight() - 150);
        } else {
            pPane.setPrefHeight(PREF_DIALOG_HEIGHT);
        }
    }
//    private void overwatchValidation(ValidationSupport pValidationSupport) {
//        invalidProperty().unbind();
//        invalidProperty().set(true);
//        invalidProperty().bind(pValidationSupport.invalidProperty());
////        pValidationSupport.invalidProperty().addListener(new ChangeListener<Boolean>() {
////            @Override
////            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
////                LOG.info("validion invalid from " + oldValue + " to " + newValue);    
////            }
////        });
////        invalidProperty().addListener(new ChangeListener<Boolean>() {
////            @Override
////            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
////                LOG.info("wizard invalid from " + oldValue + " to " + newValue);    
////            }
////        });
//
//    }
//    public class ValidatedWizardPane<T extends Controller> extends WizardPane {
//        
//        private EventHandler<ActionEvent> onEnteringPage;
//        private EventHandler<ActionEvent> onExitingPage;
//        private T controller;
//        public ValidationSupport validationSupport;
//        
//        public ValidatedWizardPane(T pController, ValidationSupport sup) {
//            this(pController.getScene().getRoot(), sup);
//            validationSupport = sup;
//        }
//        
//        public ValidatedWizardPane(Parent pParent, ValidationSupport sup) {
//            super(pParent);
//            validationSupport = sup;
//        }
//        
//        @Override
//        public void onEnteringPage(Wizard wizard) {
//            overwatchValidation(validationSupport);
//            if (onEnteringPage != null) {
//                onEnteringPage.handle(new ActionEvent());
//            }
//        }
//        
//        @Override
//        public void onExitingPage(Wizard wizard) {
//            if (onExitingPage != null) {
//                onExitingPage.handle(new ActionEvent());
//            }
//            
//        }
//        
//        public void setOnEnteringPane(EventHandler<ActionEvent> pEvent) {
//            onEnteringPage = pEvent;
//        }
//        
//        public void setOnExitingPage(EventHandler<ActionEvent> pEvent) {
//            onExitingPage = pEvent;
//        }
//        
//        public T getController() {
//            return controller;
//        }
//    }
}
