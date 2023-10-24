/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.model.ruletable.content;

import java.util.function.Predicate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class StringContentView extends Control {
    public static final String REFRESH_VIEW = "refresh";
    public StringContentView(Boolean pEditable) {
        super();
        setEditable(pEditable);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new StringContentViewSkin(this);
    }
    private ObjectProperty<Predicate<Node>> predicateProperty;

    public ObjectProperty<Predicate<Node>> predicateProperty() {
        if (predicateProperty == null) {
            predicateProperty = new SimpleObjectProperty<>(new Predicate<Node>() {
                @Override
                public boolean test(Node t) {
                    return true;
                }
            });
        }
        return predicateProperty;
    }

    public void setPredicate(Predicate<Node> pPredicate) {
        predicateProperty().set(pPredicate);
    }

    public Predicate<Node> getPredicate() {
        return predicateProperty().get();
    }
    private StringProperty contentProperty;

    public StringProperty contentProperty() {
        if (contentProperty == null) {
            contentProperty = new SimpleStringProperty();
        }
        return contentProperty;
    }

    public String getContent() {
        return contentProperty().get();
    }

    public void setContent(String pContent) {
        contentProperty().set(pContent);
    }
    
    private StringProperty validationMessageProperty;

    public StringProperty validationMessageProperty() {
        if (validationMessageProperty == null) {
            validationMessageProperty = new SimpleStringProperty();
        }
        return validationMessageProperty;
    }

    public String getValidationMessage() {
        return validationMessageProperty().get();
    }

    public void setValidationMessage(String pValidationMessage) {
        validationMessageProperty().set(pValidationMessage);
    }
    
    private BooleanProperty editableProperty;

    public BooleanProperty editableProperty() {
        if (editableProperty == null) {
            editableProperty = new SimpleBooleanProperty(false);
        }
        return editableProperty;
    }

    public boolean isEditable() {
        return editableProperty().get();
    }

    public final void setEditable(boolean pEditable) {
        editableProperty().set(pEditable);
    }

    private ObjectProperty<ContentMode> contentModeProperty;

    public ObjectProperty<ContentMode> contentModeProperty() {
        if (contentModeProperty == null) {
            contentModeProperty = new SimpleObjectProperty<>(ContentMode.ITEM);
        }
        return contentModeProperty;
    }

    public ContentMode getContentMode() {
        return contentModeProperty().get();
    }

    public void setContentMode(ContentMode pMode) {
        contentModeProperty().set(pMode);
    }
    
    private ObjectProperty<Callback<String,String>> codeSuggestionCallbackProperty;
    public ObjectProperty<Callback<String,String>> codeSuggestionCallbackProperty(){
        if(codeSuggestionCallbackProperty == null){
            codeSuggestionCallbackProperty = new SimpleObjectProperty<>();
        }
        return codeSuggestionCallbackProperty;
    }
    public Callback<String,String> getCodeSuggestionCallback(){
        return codeSuggestionCallbackProperty().get();
    }
    public void setCodeSuggestionCallback(Callback<String,String> pCallback){
        codeSuggestionCallbackProperty().set(pCallback);
    }
    public void refresh() {
        if(getProperties().containsKey(REFRESH_VIEW)){
            getProperties().remove(REFRESH_VIEW);
        }
        getProperties().put(REFRESH_VIEW, null);
    }
    public enum ContentMode {
        TEXT, ITEM;
    }
}
