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

import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Dialog Skin for vertical Formular dialogs manges as content a Vbox for
 * controlls to add
 *
 * @author wilde
 */
public class FormularDialogSkin extends DialogSkin<ButtonType> {

    public static final Integer CONTROL_SPACING = 10;

    private final VBox controlContainer;
    private final VBox controlRoot;
    private MessageNode messageNode;

    public FormularDialogSkin(FormularDialog<?> pDialog) {
        super(pDialog);
        controlContainer = new VBox();
        controlContainer.setFillWidth(true);
//        controlContainer.setPadding(new Insets(5.0));
        controlContainer.setSpacing(CONTROL_SPACING);
        
        AnchorPane pane = new AnchorPane(controlContainer);
        AnchorPane.setTopAnchor(controlContainer, 0d);
        AnchorPane.setRightAnchor(controlContainer, 5d);
        AnchorPane.setLeftAnchor(controlContainer, 5d);
        AnchorPane.setBottomAnchor(controlContainer, 0d);
        
        ScrollPane scrollPane = new ScrollPane(pane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        controlRoot = new VBox(scrollPane);
//        controlRoot.setPadding(new Insets(5.0));
        controlRoot.setSpacing(CONTROL_SPACING);
        controlRoot.setFillWidth(true);
        
        dialog.getDialogPane().setContent(controlRoot);
        VBox.setMargin(getMessageNode(), new Insets(5,5,0,5));
        pDialog.messageTextProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if(t1 == null || t1.isEmpty()){
                    getMessageNode().setMessageText("");
                    hideMessageNode();
                    return;
                }
                getMessageNode().setMessageText(t1);
                showMessageNode();
            }
        });
        
        pDialog.messageTypeProperty().addListener(new ChangeListener<CpxErrorTypeEn>() {
            @Override
            public void changed(ObservableValue<? extends CpxErrorTypeEn> ov, CpxErrorTypeEn t, CpxErrorTypeEn t1) {
                if(t1 == null){
                    hideMessageNode();
                    return;
                }
                getMessageNode().setMessageType(t1);
                showMessageNode();
            }
        });
    }
    
    private MessageNode getMessageNode(){
        if(messageNode == null){
            messageNode = new MessageNode();
        }
        return messageNode;
    }
    
    public Node getControlContainer() {
        return controlContainer;
    }
    public boolean containsMessageNode(){
        return controlRoot.getChildren().contains(getMessageNode());
    }
    public void showMessageNode() {
        if (!containsMessageNode()) {
            controlRoot.getChildren().add(0, getMessageNode());
            resizeToScene();
        }
    }
    public void hideMessageNode(){
        if(containsMessageNode()){
            controlRoot.getChildren().remove(getMessageNode());
            resizeToScene();
        }
    }
    private void resizeToScene(){
        if(!(dialog instanceof FormularDialog)){
            return;
        }
        if(((FormularDialog)dialog).isAutoResizeforMessageNode()){
            getStage().sizeToScene();
        }
    }
    /**
     * adds controls to the layout, controls are added in the center area in an
     * VBox
     *
     * @param pControls list of controls to add
     */
    public void addControls(Control... pControls) {
//        for(Control ctrl: pControls){
//            VBox.setMargin(ctrl, new Insets(5));
//        }
        controlContainer.getChildren().addAll(pControls);
    }

    /**
     * add a pane with controls to the content box
     *
     * @param pGroupPane pane to add
     */
    public void addControlGroup(Node pGroupPane) {
        addControlGroup(pGroupPane, false);
    }

    /**
     * add a pane with controls to the content box
     *
     * @param pGroupPane pane to add
     * @param pMaxHeight occupy max space (vGrow Always)
     */
    public void addControlGroup(Node pGroupPane, boolean pMaxHeight) {
        if (pMaxHeight) {
            VBox.setVgrow(pGroupPane, Priority.ALWAYS);
            VBox.setMargin(pGroupPane, new Insets(5));
        }
        controlContainer.getChildren().add(pGroupPane);
    }

    /**
     * removes the pane
     *
     * @param pGrPane pane to remove
     */
    public void removeControlGroup(Node pGrPane) {
        controlContainer.getChildren().remove(pGrPane);
    }

    /**
     * sets controls to the layout, controls are set in the center area in an
     * VBox set will CLEAR the list of items, all previous added or setted items
     * will be lost
     *
     * @param pControls list of controls to set
     */
    public void setControls(Control... pControls) {
        for (Control ctrl : pControls) {
            VBox.setMargin(ctrl, new Insets(5));
        }
        controlContainer.getChildren().setAll(pControls);
    }

    /**
     * clears the list of control items in the dialog
     */
    public void clearControls() {
        controlContainer.getChildren().clear();
    }

    /**
     * removes the controls from the list
     *
     * @param pControls list of controls to remove
     */
    public void removeControls(Control... pControls) {
        controlContainer.getChildren().removeAll(pControls);
    }

    /**
     * get the list of controls
     *
     * @return list of controls, that are set
     */
    public ObservableList<Node> getControlList() {
        return controlContainer.getChildren();
    }

    public void addControls(Pane setUpLayoutForRequestType) {
//        VBox.setMargin(setUpLayoutForRequestType, new Insets(5));
        controlContainer.getChildren().setAll(setUpLayoutForRequestType);
    }
    
//    private class MessageNode extends VBox{
//
//        private final Glyph icon;
//
//        public MessageNode() {
//            super(5);
//            setPadding(new Insets(10));
//            setAlignment(Pos.CENTER);
//            Label msg = new Label();
//            icon = ResourceLoader.getGlyph(FontAwesome.Glyph.EXCLAMATION_TRIANGLE);
//            msg.textProperty().bind(messageTextProperty());
//            updateStyleForType(CpxErrorTypeEn.INFO);
//            messageTypeProperty().addListener(new ChangeListener<CpxErrorTypeEn>() {
//                @Override
//                public void changed(ObservableValue<? extends CpxErrorTypeEn> ov, CpxErrorTypeEn t, CpxErrorTypeEn t1) {
//                    updateStyleForType(t1);
//                }
//            });
//            HBox container = new HBox(5,icon,msg);
//            container.setAlignment(Pos.CENTER);
//            container.setMinHeight(HBox.USE_PREF_SIZE);
//            getChildren().add(container);
//        }
//        private void updateStyleForType(CpxErrorTypeEn t1) {
//            t1 = Objects.requireNonNullElse(t1, CpxErrorTypeEn.INFO);
//            setStyle(new StringBuilder("-fx-background-color:").append(t1.getBackgroundColor()).append(";")
//                    .append("-fx-background-radius:10;").toString());
//            icon.setStyle(new StringBuilder("-fx-text-fill:").append(t1.getIconColor()).append(";").toString());
//        }
//        private StringProperty messageTextProperty;
//        public final StringProperty messageTextProperty(){
//            if(messageTextProperty == null){
//                messageTextProperty = new SimpleStringProperty();
//            }
//            return messageTextProperty;
//        }
//        public String getMessageText(){
//            return messageTextProperty().get();
//        }
//        public void setMessageText(String pMessage){
//            messageTextProperty().set(pMessage);
//        }
//
//        private ObjectProperty<CpxErrorTypeEn> messageTypeProperty;
//        public final ObjectProperty<CpxErrorTypeEn> messageTypeProperty(){
//            if(messageTypeProperty == null){
//                messageTypeProperty = new SimpleObjectProperty<>(CpxErrorTypeEn.INFO);
//            }
//            return messageTypeProperty;
//        }
//        public CpxErrorTypeEn getMessageType(){
//            return messageTypeProperty().get();
//        }
//        public void setMessageType(CpxErrorTypeEn pType){
//            messageTypeProperty().set(Objects.requireNonNullElse(pType, CpxErrorTypeEn.INFO));
//        }
//    }
}
