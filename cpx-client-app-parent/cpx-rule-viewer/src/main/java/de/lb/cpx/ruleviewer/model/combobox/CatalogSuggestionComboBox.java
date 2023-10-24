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
package de.lb.cpx.ruleviewer.model.combobox;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class CatalogSuggestionComboBox extends ComboBox<String>{

    private static final Logger LOG = Logger.getLogger(CatalogSuggestionComboBox.class.getName());
    
    public CatalogSuggestionComboBox() {
        this(null);
    }
    
    public CatalogSuggestionComboBox(String pSuggestions) {
        super(pSuggestions!=null?FXCollections.observableArrayList(pSuggestions.split(",")):FXCollections.observableArrayList());
        setEditable(true);
        getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                getValueChangeCallback().call(t1);
            }
        });
        getEditor().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if(!t1 && hasValueChanged()){
                    getSelectionModel().select(getEditor().getText());
                }
            }
        });
        getEditor().addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (KeyCode.ENTER.equals(t.getCode())) {
                    if (hasValueChanged()) {
                        getSelectionModel().select(getEditor().getText());
                    }
                }
            }
        });
    }
    private boolean hasValueChanged(){
        return !getEditor().getText().equals(getSelectionModel().getSelectedItem());
    }
    private final Callback<String,Void> DEFAULT_VALUE_CHANGE_CALLBACK = new Callback<>() {
        @Override
        public Void call(String p) {
            LOG.log(Level.INFO, "Value for CatalogCodeSuggestion changed! Value changed to: {0}", p);
            return null;
        }
    };
    private Callback<String,Void> valueChangeCallback = DEFAULT_VALUE_CHANGE_CALLBACK;
    public Callback<String, Void> getValueChangeCallback(){
        return valueChangeCallback;
    }
    public void setValueChangeCallback(Callback<String,Void> pCallback){
        valueChangeCallback = Objects.requireNonNullElse(pCallback, DEFAULT_VALUE_CHANGE_CALLBACK);
    }
//    public CatalogSuggestionComboBox(ObservableList<String> pSuggestions) {
//        super(pSuggestions);
//        setEditable(true);
//    }
    
}
