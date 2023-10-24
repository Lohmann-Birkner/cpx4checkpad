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
package de.lb.cpx.ruleviewer.analyser.model;

import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserBeanProperty;
import de.lb.cpx.ruleviewer.util.AnalyserFormater;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TextFormatter;
import javafx.util.Callback;
import org.controlsfx.control.PropertySheet.Item;
import org.controlsfx.property.editor.DefaultPropertyEditorFactory;
import org.controlsfx.property.editor.PropertyEditor;

/**
 *
 * @author wilde
 */
public class RuleAnalyserItem extends Control {

    private PropertyEditor<?> editor;

    public RuleAnalyserItem() {
        super();
        getStyleClass().add("rule-analyser-item");
        beanProperty().addListener(new ChangeListener<AnalyserBeanProperty>() {
            @Override
            public void changed(ObservableValue<? extends AnalyserBeanProperty> ov, AnalyserBeanProperty t, AnalyserBeanProperty t1) {
                if (t1 != null) {
                    setText(AnalyserFormater.formatText(t1));
                }
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new RuleAnalyserItemSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(RuleAnalyserItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    private ObjectProperty<TextFormatter<?>> editTextFormatterProperty;

    public ObjectProperty<TextFormatter<?>> editTextFormatterProperty() {
        if (editTextFormatterProperty == null) {
            editTextFormatterProperty = new SimpleObjectProperty<>();
        }
        return editTextFormatterProperty;
    }

    public TextFormatter<?> getEditTextFormatter() {
        return editTextFormatterProperty().get();
    }

    public void setEditTextFormatter(TextFormatter<?> pFormatter) {
        editTextFormatterProperty().set(pFormatter);
    }
    private StringProperty textProperty;

    public StringProperty textProperty() {
        if (textProperty == null) {
            textProperty = new SimpleStringProperty("");
        }
        return textProperty;
    }

    public String getText() {
        return textProperty().get();
    }

    public final void setText(String pText) {
        textProperty().set(pText);
    }
    private StringProperty titleProperty;

    public StringProperty titleProperty() {
        if (titleProperty == null) {
            titleProperty = new SimpleStringProperty("");
        }
        return titleProperty;
    }

    public String getTitle() {
        return titleProperty().get();
    }

    public void setTitle(String pTitle) {
        titleProperty().set(pTitle);
    }
    private final BooleanProperty expandProperty = new SimpleBooleanProperty(false);

    public BooleanProperty expandProperty() {
        return expandProperty;
    }

    public void setExpand(boolean pExpand) {
        expandProperty().set(pExpand);
    }

    public boolean isExpand() {
        return expandProperty().get();
    }
    private ObjectProperty<Callback<Void, Boolean>> onDeleteCallbackProperty;

    public ObjectProperty<Callback<Void, Boolean>> onDeleteCallbackProperty() {
        if (onDeleteCallbackProperty == null) {
            onDeleteCallbackProperty = new SimpleObjectProperty<>();
        }
        return onDeleteCallbackProperty;
    }

    public Callback<Void, Boolean> getOnDeleteCallback() {
        return onDeleteCallbackProperty().get();
    }

    public void setOnDeleteCallback(Callback<Void, Boolean> pCallback) {
        onDeleteCallbackProperty().set(pCallback);
    }

    private ObjectProperty<Callback<Void, Boolean>> onEditCallbackProperty;

    public ObjectProperty<Callback<Void, Boolean>> onEditCallbackProperty() {
        if (onEditCallbackProperty == null) {
            onEditCallbackProperty = new SimpleObjectProperty<>();
        }
        return onEditCallbackProperty;
    }

    public Callback<Void, Boolean> getOnEditCallback() {
        return onEditCallbackProperty().get();
    }

    public void setOnEditCallback(Callback<Void, Boolean> pCallback) {
        onEditCallbackProperty().set(pCallback);
    }

    private ObjectProperty<AnalyserBeanProperty> beanProperty;

    public final ObjectProperty<AnalyserBeanProperty> beanProperty() {
        if (beanProperty == null) {
            beanProperty = new SimpleObjectProperty<>();
        }
        return beanProperty;
    }

    public final AnalyserBeanProperty getBeanProperty() {
        return beanProperty().get();
    }

    public void setBeanProperty(AnalyserBeanProperty pType) {
        beanProperty().set(pType);
    }
    private DoubleProperty editorWidthProperty;

    public DoubleProperty editorWidthProperty() {
        if (editorWidthProperty == null) {
            editorWidthProperty = new SimpleDoubleProperty(200);
        }
        return editorWidthProperty;
    }

    public void setEditorWidth(double pWidth) {
        editorWidthProperty().set(pWidth);
    }

    public Double getEditorWidth() {
        return editorWidthProperty().get();
    }
    private Callback<Item, PropertyEditor<?>> editorFactory = new DefaultPropertyEditorFactory();

    public Callback<Item, PropertyEditor<?>> getEditorFactory() {
        return editorFactory;
    }

    public void setEditorFactorty(Callback<Item, PropertyEditor<?>> pFactory) {
        editorFactory = pFactory;
    }

    public Node getEditor() {
//        if(editor == null){
//            editor = getEditorFactory().call(getBeanProperty());
//        }
//        return editor.getEditor();
        return getPropertyEditor().getEditor();
    }

    public PropertyEditor<?> getPropertyEditor() {
        if (editor == null) {
            editor = getEditorFactory().call(getBeanProperty());
        }
        return editor;
    }
    private final ObservableList<RuleAnalyserItem> items = FXCollections.observableArrayList();

    public ObservableList<RuleAnalyserItem> getItems() {
        return items;
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    public String getItemsText() {
        if (!hasItems()) {
            return AnalyserFormater.formatText(getBeanProperty());
        }
        return getItems().stream().map((t) -> {
            return t.getTitle() + " (" + t.getText() + ")"; //To change body of generated lambdas, choose Tools | Templates.
        }).collect(Collectors.joining(", "));
    }
    private final ReadOnlyObjectWrapper<RuleAnalyserItem> parentItemProperty = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<RuleAnalyserItem> parentItemProeprty() {
        return parentItemProperty.getReadOnlyProperty();
    }

    public void setParentItem(RuleAnalyserItem pParent) {
        parentItemProperty.set(pParent);
    }

    public RuleAnalyserItem getParentItem() {
        return parentItemProeprty().get();
    }

    public void clearValue() {
        getPropertyEditor().setValue(null);
    }
}
