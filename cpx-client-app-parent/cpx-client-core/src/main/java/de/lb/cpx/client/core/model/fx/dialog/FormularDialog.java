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
package de.lb.cpx.client.core.model.fx.dialog;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * Creates a new formular dialog Formulardialog are dialogs to enter values,
 * that generates an object as result results are stored in the resultproperty
 * through validationSupport, validation of the user input in the formular is
 * possible
 *
 * @author wilde
 * @param <T> content of the dialog
 */
public abstract class FormularDialog<T> extends TitledDialog {

    //AWi-20170613-CPX-542 ResultProperty
    private final ObjectProperty<T> resultsProperty = new SimpleObjectProperty<>();
    protected ValidationSupport validationSupport;

    /**
     * creates new instance of formular dialog
     *
     * @param pOwner owner of the dialog
     * @param pModality mode to show window,application or none
     * @param pTitle title that should be shown
     */
    public FormularDialog(Window pOwner, Modality pModality, String pTitle) {
        super(pTitle, pOwner, pModality);
        validationSupport = new ValidationSupport();
        validationSupport.initInitialDecoration();
        setDialogSkin(new FormularDialogSkin(this));
        setOnSave(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                T result = onSave();
                setResults(result);
            }
        });

        Button btnOk = getDialogSkin().getButton(ButtonType.OK);
        //stop button from executing when validation erros occure,
        //is this possible?? if validation support is invalid, button is disabled therefore no action event 
        //will be executed??
        btnOk.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (!additionalCheck() || validationSupport.isInvalid()) {
                    t.consume();
                    if(validationSupport.isInvalid()){
                        showValidationErrors();
                    }
                }
            }
        });
        btnOk.disableProperty().bind(validationSupport.invalidProperty());
//        pOwner.centerOnScreen();
    }
    
    protected boolean additionalCheck(){
        return true;
    }

    public ValidationSupport getValidation() {
        return validationSupport;
    }

    /**
     * creates new instance of formular dialog
     *
     * @param pOwner owner of the dialog
     * @param pTitle title that should be shown
     */
    public FormularDialog(String pTitle, Window pOwner) {
        this(pOwner, Modality.APPLICATION_MODAL, pTitle);
    }

    /**
     * add controlls in the center area of the dialog
     *
     * @param pControls list of controls for the formular, verticaly ordered
     */
    public final void addControls(Control... pControls) {
        getDialogSkin().addControls(pControls);
    }

    public void addControlGroup(Node pGroupPane) {
        getDialogSkin().addControlGroup(pGroupPane);
    }

    public void addControlGroup(Node pGroupPane, boolean pMaxGrow) {
        getDialogSkin().addControlGroup(pGroupPane, pMaxGrow);
    }

    public void removeControlGroup(Node pGroupPane) {
        getDialogSkin().removeControlGroup(pGroupPane);
    }

    /**
     * clears the list of active controls in the ui
     */
    public void clearControls() {
        if (!getDialogSkin().getControlList().isEmpty()) {
            getDialogSkin().getControlList().clear();
        }
    }

    public void removeControls(Control... pControls) {
        if (!getDialogSkin().getControlList().isEmpty()) {
            getDialogSkin().getControlList().removeAll(pControls);
        }
    }

    @Override
    public final FormularDialogSkin getDialogSkin() {
        return (FormularDialogSkin) super.getDialogSkin();
    }

    protected void showValidationErrors() {
        String msg = "";
        for (ValidationMessage res : validationSupport.getValidationResult().getErrors()) {
            msg = msg.concat(res.getText() + "\n");
        }
        BasicMainApp.showErrorMessageDialog(msg);
    }

    /**
     * @return get the result Property
     */
    public ObjectProperty<T> getResultsProperty() {
        return resultsProperty;
    }

    /**
     * @return result set in the resultProperty
     */
    public T getResults() {
        return resultsProperty.get();
    }

    /**
     * @param pResults set the result in the property
     */
    public void setResults(T pResults) {
        resultsProperty.set(pResults);
    }

    /**
     * abstract save methode to define what should happen if save is called an a
     * button
     *
     * @return newly saved instance
     */
    public abstract T onSave();

    public void setOnSave(EventHandler<ActionEvent> eventHandler) {
//        Button saveBtn = getDialogSkin().getButton(ButtonType.OK);
//        saveBtn.setOnAction(eventHandler);
        Button btn = getDialogSkin().getButton(ButtonType.OK);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                eventHandler.handle(t);
            }
        });//ActionEvent.ACTION, event -> {
//            eventHandler.handle(event);
//        });
    }

    public Scene getScene() {
        DialogPane dp = getDialogPane();
        if (dp == null) {
            return null;
        }
        return dp.getScene();
    }

    public Window getWindow() {
        Scene scene = getScene();
        return scene == null ? null : scene.getWindow();
    }
    
    private StringProperty messageTextProperty;
    public StringProperty messageTextProperty(){
        if(messageTextProperty == null){
            messageTextProperty = new SimpleStringProperty();
        }
        return messageTextProperty;
    }
    public String getMessageText(){
        return messageTextProperty().get();
    }
    public void setMessageText(String pMessage){
        messageTextProperty().set(pMessage);
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
    public void setMessageType(CpxErrorTypeEn pType){
        messageTypeProperty().set(pType);
    }
    
    private BooleanProperty autoResizeForMessageNode;
    public BooleanProperty autoResizeForMessageNode(){
        if(autoResizeForMessageNode == null){
            autoResizeForMessageNode = new SimpleBooleanProperty(true);
        }
        return autoResizeForMessageNode;
    }
    public void setAutoResizeForMessageNode(boolean pBoolean){
        autoResizeForMessageNode().set(pBoolean);
    }
    public boolean isAutoResizeforMessageNode(){
        return autoResizeForMessageNode().get();
    }
}
