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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.properties;

import de.lb.cpx.rule.criteria.model.TypesAndOperations.OperationGroups.OperationGroup.Operation;
import de.lb.cpx.ruleviewer.model.RuleMessageIndicator;
import de.lb.cpx.shared.lang.Lang;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

/**
 * BasicEditor for changing an operator
 *
 * @author wilde
 */
public class BasicOperatorEditor implements PropertyEditor<Operation> {

    private ComboBox<Operation> editor;
    private final PropertySheet.Item item;
    private HBox container;
    private final RuleMessageIndicator message;

    public BasicOperatorEditor(final PropertySheet.Item item) {
        this.item = item;
        this.message = new RuleMessageIndicator();
        message.tooltipProperty().bind(messageTooltipProperty());
        message.setPadding(new Insets(0, 8, 0, 0));
        getEditor();
        showMessageProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                showRuleMessage(t1);
            }
        });
        showRuleMessage(isShowMessage());
    }

    @Override
    public Node getEditor() {
        if (editor == null) {
            editor = new ComboBox<>();
            editor.setMaxWidth(Double.MAX_VALUE);
            editor.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Operation>() {
                @Override
                public void changed(ObservableValue<? extends Operation> observable, Operation oldValue, Operation newValue) {
                    if (editor.isDisabled()) {
                        newValue = null;
                        editor.getSelectionModel().select(null);
                    }
                    setValue(newValue);
                    item.setValue(getValue());
                }
            });
            editor.setConverter(new StringConverter<Operation>() {
                @Override
                public String toString(Operation object) {
                    return object != null ? Lang.get(object.getDisplayName()).getValue() : "";
                }

                @Override
                public Operation fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
        }
        if(container == null){
            container = new HBox(editor);
            HBox.setHgrow(editor, Priority.ALWAYS);
        }
        return container;
    }
    
    private BooleanProperty showMessageProperty;
    public final BooleanProperty showMessageProperty(){
        if(showMessageProperty == null){
            showMessageProperty = new SimpleBooleanProperty();
        }
        return showMessageProperty;
    }
    public final boolean isShowMessage(){
        return showMessageProperty().get();
    }
    public void setShowMessage(boolean pMessage){
        showMessageProperty().set(pMessage);
    }
    
    private void showRuleMessage(Boolean pShow) {
        if(container == null){
            getEditor();
        }
        if(pShow){
            if(!container.getChildren().contains(message)){
                container.getChildren().add(0, message);
            }
        }else{
            if(container.getChildren().contains(message)){
                container.getChildren().remove(message);
            }
        }
    }
    
    private ObjectProperty<Tooltip> messageTooltipProperty;
    public final ObjectProperty<Tooltip> messageTooltipProperty(){
        if(messageTooltipProperty == null){
            messageTooltipProperty = new SimpleObjectProperty<>();
        }
        return messageTooltipProperty;
    }
    public Tooltip getMessageTooltip(){
        return messageTooltipProperty().get();
    }
    public void setMessageTooltip(Tooltip pTooltip){
        messageTooltipProperty().set(pTooltip);
    }
    @Override
    public Operation getValue() {
        return valueProperty().get();
    }

    @Override
    public final void setValue(Operation t) {
        if (editor == null) {
            getEditor();
        }
        editor.getSelectionModel().select(t);
    }

    public final void setValues(List<Operation> allowedOperators) {
        if (editor == null) {
            getEditor();
        }
        if (allowedOperators == null) {
            editor.setItems(FXCollections.observableArrayList());
            return;
        }
        editor.setItems(FXCollections.observableArrayList(allowedOperators));
    }

    private ReadOnlyObjectProperty<Operation> valueProperty() {
        if (editor == null) {
            getEditor();
        }
        return editor.getSelectionModel().selectedItemProperty();
    }

    public void selectFirst() {
        if (editor == null) {
            getEditor();
        }
        editor.getSelectionModel().selectFirst();
    }
    
    public void disableEditor(boolean pDisable){
        if(editor == null){
            getEditor();
        }
        if(editor.isDisable()==pDisable){
            return; //ignore same state
        }
        editor.setDisable(pDisable);
        if(pDisable){
            editor.getSelectionModel().select(null);
//        }else{
//            editor.getSelectionModel().select(getOperationForName(noOperationName));
        }
    }

//    private Operation getOperationForName(String pOperationName) {
//        if(pOperationName == null || pOperationName.isEmpty()){
//            return null;
//        }
//        if(editor == null){
//            getEditor();
//        }
//        return editor.getItems().stream().filter((t) -> {
//            return t.getDisplayName().equals(pOperationName);
//        }).findFirst().orElse(null);
//    }
    
    public PropertySheet.Item getItem() {
        return item;
    }
}
