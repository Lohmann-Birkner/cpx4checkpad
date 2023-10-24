/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Bohm - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.dialog;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 *
 * @author Bohm
 */
public class CpxTextInputDialog extends Dialog<String> {

    protected ValidationSupport validationSupport;
    private final ObjectProperty<DialogSkin<String>> dialogSkinProperty = new SimpleObjectProperty<>();
    private final LabeledTextField textField;

    /**
     * construct a new CpxTextInputDialog with the default DialogSkin Window
     * Motality is set to APPLICATION_MODAL
     *
     * @param pTitle title to be set in the Dialog
     */
    public CpxTextInputDialog(String pTitle) {
        super();
        validationSupport = new ValidationSupport();
        validationSupport.initInitialDecoration();
        initModality(Modality.APPLICATION_MODAL);
//        dialogSkinProperty.set(new DialogSkin(this));

//        dialogSkin = new DialogSkin(this);
//        dialogSkin.setTitle(pTitle);
        dialogSkinProperty.addListener(new ChangeListener<DialogSkin<String>>() {
            @Override
            public void changed(ObservableValue<? extends DialogSkin<String>> observable, DialogSkin<String> oldValue, DialogSkin<String> newValue) {
                newValue.setTitle(pTitle);
                newValue.addButtonTypes(ButtonType.OK, ButtonType.CANCEL);
                Button saveBtn = newValue.getButton(ButtonType.OK);
                saveBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (validationSupport.isInvalid()) {
                            event.consume();
                            String msg = "";
                            for (ValidationMessage res : validationSupport.getValidationResult().getErrors()) {
                                msg = msg.concat(res.getText() + "\n");
                            }
                            BasicMainApp.showErrorMessageDialog(msg);
                            return;
                        }

                    }
                });
                saveBtn.disableProperty().bind(validationSupport.invalidProperty());
            }
        });
//        dialogSkin.addButtonTypes(ButtonType.OK,ButtonType.CANCEL);
//        Button saveBtn = dialogSkin.getButton(ButtonType.OK);
//        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                if(validationSupport.isInvalid()){
//                    event.consume();
//                    String msg = "";
//                    for(ValidationMessage res : validationSupport.getValidationResult().getErrors()){
//                        msg = msg.concat(res.getText()+"\n");
//                    }
//                    BasicMainApp.showErrorMessageDialog(msg);
//                    return;
//                 }
//                onSave();
//            }
//        });

//        setOnCloseRequest(new EventHandler<DialogEvent>() {
//            @Override
//            public void handle(DialogEvent event) {
//                event.getSource();
//                
//                if(validationSupport.isInvalid()){
//                    event.consume();
//                    String msg = "";
//                    for(ValidationMessage res : validationSupport.getValidationResult().getErrors()){
//                        msg = msg.concat(res.getText()+"\n");
//                    }
//                    BasicMainApp.showErrorMessageDialog(msg);
//                 }
//            }
//        });
        textField = new LabeledTextField();
        textField.setId("textField");
        HBox box = new HBox();
        box.getChildren().add(textField);
        this.getDialogPane().setContent(box);
        this.dialogSkinProperty.set(new DialogSkin<>(this));
        Stage s = (Stage) this.getDialogPane().getScene().getWindow();
        s.setMinHeight(170);
        s.setHeight(170);
        //s.setMinWidth(550);
        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? textField.getText() : null;
        });
        //focus textfield to enable entering title
        setOnShown(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        textField.requestFocus();
                    }
                });
            }
        });
    }

    /**
     * construct a new CpxTextInputDialog with the default DialogSkin
     *
     * @param pOwner owner window, if set Dialog starts as WINDOW_MODAL
     * @param pTitle title to be set in the Dialog
     */
    public CpxTextInputDialog(Window pOwner, String pTitle) {
        this(pTitle);
        if (pOwner != null) {
            initModality(Modality.WINDOW_MODAL);
            initOwner(pOwner);
        }

    }

    /**
     * construct a new CpxTextInputDialog with the default DialogSkin
     *
     * @param pOwner owner window
     * @param pModality modality to set in the window
     * @param pTitle title to be set in the Dialog
     */
    public CpxTextInputDialog(Window pOwner, Modality pModality, String pTitle) {
        this(pOwner, pTitle);
        initModality(pModality);
    }

    /**
     * construct a new CpxTextInputDialog with the default DialogSkin
     *
     * @param pTitle title to be set in the Dialog
     * @param pInputDescString the Text of the Label should say what input is
     * expected
     */
    public CpxTextInputDialog(String pTitle, String pInputDescString) {
        this(pTitle);
        textField.setTitle(pInputDescString);
        ClipboardEnabler.installClipboardToScene(this.getDialogPane().getScene());
    }

    /**
     * construct a new CpxTextInputDialog with the default DialogSkin
     *
     * @param pTitle title to be set in the Dialog
     * @param pInputDescString the Text of the Label should say what input is
     * @param pOwner owner window
     */
    public CpxTextInputDialog(String pTitle, String pInputDescString, Window pOwner) {
        this(pTitle);
        textField.setTitle(pInputDescString);
        initModality(Modality.WINDOW_MODAL);
        initOwner(pOwner);
    }

    public void setOnSave(EventHandler<ActionEvent> eventHandler) {
        Button saveBtn = getDialogSkin().getButton(ButtonType.OK);
        saveBtn.setOnAction(eventHandler);
    }

    /**
     * gets the skin (layout) of the dialog, that is extended from of a
     * SkinBase!
     *
     * @return layout class for the dialog
     */
    public DialogSkin<String> getDialogSkin() {
        return dialogSkinProperty.get();
    }

    /**
     * sets a new skin apply changes to it
     *
     * @param pSkin skin to set
     */
    public void setDialogSkin(DialogSkin<String> pSkin) {
        dialogSkinProperty.set(pSkin);
    }

    /**
     * sets the modality in the dialog May not force the dialog to redraw it
     * self if its already showing
     *
     * @param pModality modality mode to set
     */
    public void setModality(Modality pModality) {
        initModality(pModality);
    }

    /**
     *
     * @param pValidator validator, register to check user input
     */
    public void registerValidator(Validator<String> pValidator) {
        validationSupport.registerValidator(textField.getControl(), pValidator);
    }
}
