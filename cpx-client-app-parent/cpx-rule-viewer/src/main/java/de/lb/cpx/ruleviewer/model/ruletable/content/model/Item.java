/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.model.ruletable.content.model;

import java.util.Objects;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.controlsfx.validation.Severity;

/**
 *
 * @author wilde
 */
public class Item extends HBox {

        protected static final String BORDER_STYLE = "-fx-border-color:lightgrey";
        public static final String SAVE_DATA = "save.data";
        private EventHandler<ActionEvent> onDeleteRequested;
        private Callback<String, Void> multiInputCallback;
        private Callback<String, Tooltip> tooltipFactory = (String param) -> null;
        private BooleanProperty editableProperty;
        private StringProperty textProperty;
        private Callback<String, Control> editorFactory = new Callback<String, Control>() {
            @Override
            public Control call(String param) {
                return createEditTextField();
            }
        };
        private EventHandler<ActionEvent> onContentChanged;
        private Severity severity;
    private Callback<String, Void> singleInputCallback;

        public Item(String pText, Boolean pEditable) {
            super();
            setStyle(BORDER_STYLE);
            setAlignment(Pos.CENTER_LEFT);
            setText(Objects.requireNonNullElse(pText,"").trim());
            setPadding(new Insets(5));
            setSpacing(5);
            if (pEditable) {
                setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        getChildren().get(0).getProperties().put(SAVE_DATA, null);
                        setEditbale(!isEditable());
                    }
                });
            }
            editableProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    updateUi(pEditable);
                }
            });
            graphicProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> ov, Node t, Node t1) {
                    updateUi(isEditable());
                }
            });
            updateUi(pEditable);
        }

        @Override
        public String toString() {
            return getText();
        }

        private void updateUi(boolean pEditable) {
            getChildren().clear();
            Node text = getTextNode();
            getChildren().addAll(text);
            updateSeverity(severity);
            if(hasGraphic()){
                getChildren().add(0,getGraphic());
            }
        }
        public Severity getSeverity(){
            return severity;
        }
        public void setSeverity(Severity pSeverity){
            severity = pSeverity;
            updateSeverity(pSeverity);
        }
        public void updateSeverity(Severity pSeverity){
            Node node = getChildren().size()>=1?getChildren().get(0):null;
            if(node==null){
                return;
            }
            pSeverity = Objects.requireNonNullElse(pSeverity, Severity.OK);
            final StringBuilder builder = new StringBuilder("-fx-fill: ");
            switch(pSeverity){
                case ERROR:
                    builder.append("red");
                    break;
                default:
                    builder.append("black");
            }
            builder.append(";");
            node.setStyle(builder.toString());
        }
        private ObjectProperty<Node> graphicProperty;
        public final ObjectProperty<Node> graphicProperty(){
            if(graphicProperty == null){
                graphicProperty = new SimpleObjectProperty<>();
            }
            return graphicProperty;
        }
        public boolean hasGraphic(){
            return getGraphic()!=null;
        }
        public Node getGraphic(){
            return graphicProperty().get();
        }
        public void setGraphic(Node pGraphic){
            graphicProperty().set(pGraphic);
        }
        public EventHandler<ActionEvent> getOnDeleteRequested() {
            return onDeleteRequested;
        }

        public void setOnDeleteRequested(EventHandler<ActionEvent> onDelete) {
            onDeleteRequested = onDelete;
        }

        public Callback<String, Void> getMultiInputCallback() {
            return multiInputCallback;
        }

        public void setMultiInputCallback(Callback<String, Void> pCallback) {
            multiInputCallback = pCallback;
        }
        public final Callback<String,Void> defaultSingleInputCallback = (String p) -> {
            setText(p);
            return null;
        };
        public Callback<String, Void> getSingleInputCallback() {
            return Objects.requireNonNullElse(singleInputCallback,defaultSingleInputCallback);
        }

        public void setSingleInputCallback(Callback<String, Void> pCallback) {
            singleInputCallback = Objects.requireNonNullElse(pCallback,defaultSingleInputCallback);
        }
        
        private boolean isMultiInput(String pText) {
            return pText.contains(",") || pText.contains(" ") || pText.contains("\n");
        }

        public void saveText(String pText) {
            pText = pText.replace("''", " ").replace("'", "");
            if (isMultiInput(pText)) {
                if (getMultiInputCallback() != null) {
                    getMultiInputCallback().call(pText);
                    setText("");
                    if (getOnDeleteRequested() != null) {
                        getOnDeleteRequested().handle(new ActionEvent());
                    }
                }
            } else {
                getSingleInputCallback().call(pText);
            }
            if (onContentChanged != null) {
                onContentChanged.handle(new ActionEvent());
            }
        }

        private Node getTextNode() {
            if (isEditable()) {
                return createEditControl();
            } else {
                Text label = new Text();
                textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                        t1 = Objects.requireNonNullElse(t1, "");
                        t1 = t1.replace("%", "*");
                        label.setText(t1);
                    }
                });
                String t1 = getText();
                t1 = Objects.requireNonNullElse(t1, "");
                t1 = t1.replace("%", "*");
                label.setText(t1);
                Tooltip tip = getTooltipFactory().call(label.getText());
                if (tip != null) {
                    Tooltip.install(Item.this, tip);
                }
                return label;
            }
        }

        public void setTooltipFactory(Callback<String, Tooltip> pFactory) {
            tooltipFactory = pFactory;
        }

        public Callback<String, Tooltip> getTooltipFactory() {
            return tooltipFactory;
        }

        public final BooleanProperty editableProperty() {
            if (editableProperty == null) {
                editableProperty = new SimpleBooleanProperty(false);
            }
            return editableProperty;
        }

        public boolean isEditable() {
            return editableProperty().get();
        }

        public void setEditbale(Boolean pEditable) {
            editableProperty().set(pEditable);
        }

        public StringProperty textProperty() {
            if (textProperty == null) {
                textProperty = new SimpleStringProperty();
            }
            return textProperty;
        }

        public final void setText(String pText) {
            textProperty().set(pText);
        }

        public String getText() {
            String text = textProperty().get();
            return text;
        }

        public void setEditorFactory(Callback<String, Control> pFactory) {
            editorFactory = pFactory;
        }

        public Callback<String, Control> getEditorFactory() {
            return editorFactory;
        }

        private Node createEditControl() {
            //USE CREATEEDITOR FACTORY INSTEAD!
            Control editor = getEditorFactory().call(getText());
            if (editor != null) {
                return editor;
            }
            return createEditTextField();
        }

        public TextField createEditTextField() {
            return createEditTextField(Boolean.TRUE);
        }
        public void addEditorListeners(Node pNode,boolean pAutoHide){
            pNode.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        if (pAutoHide) {
                            setEditbale(false);
                        }
                    }
                }
            });

            pNode.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (KeyCode.ENTER.equals(event.getCode())) {
                        setEditbale(false);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                requestFocus();
                            }
                        });
                        event.consume();
                    }
                    if (KeyCode.ESCAPE.equals(event.getCode())) {
                        setEditbale(false);
                        event.consume();
                    }
                }
            });
        }
        public TextField createEditTextField(Boolean pAutoHide) {
            String text = getText();
            text = text.replace("%", "*");
            TextField field = new TextField(text) {
                @Override
                public void paste() {
                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                    if (clipboard.hasString()) {
                        String text = clipboard.getString();
                        if (text != null) {
                            //for csv replace break with comma
                            text = text.replace("\r", ",");
                            replaceSelection(text);
                        }
                    }
                }

            };
            field.setId("field-edit");
            addEditorListeners(field, pAutoHide);
            field.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        if (field.getText().isEmpty()) {
                            if (getOnDeleteRequested() != null) {
                                getOnDeleteRequested().handle(new ActionEvent());
                            }
                        } else {
                            String text = field.getText();
                            text = text.replace("*", "%");
                            saveText(text);
                        }
                    }
                }
            });
            field.getProperties().addListener(new MapChangeListener<Object, Object>() {
                @Override
                public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> change) {
                    if (change.wasAdded()) {
                        if (SAVE_DATA.equals(change.getKey())) {
                            String text = field.getText();
                            text = text.replace("*", "%");
                            saveText(text);
                            field.getProperties().remove(SAVE_DATA);
                        }
                    }
                }
            });

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    field.selectAll();
                    field.requestFocus();
                }
            });
            return field;
        }

        public void setOnContentChanged(EventHandler<ActionEvent> pEventHandler) {
            onContentChanged = pEventHandler;
        }

        public Boolean hasFocus(Parent pParent) {
            if (pParent.isFocused()) {
                return true;
            }
            for (Node child : pParent.getChildrenUnmodifiable()) {
                if (child.isFocused()) {
                    return true;
                }
                if (child instanceof Parent) {
                    return hasFocus((Parent) child);
                }
            }
            return false;
        }

        public Boolean hasFocus() {
            return hasFocus(Item.this);
        }
    }
