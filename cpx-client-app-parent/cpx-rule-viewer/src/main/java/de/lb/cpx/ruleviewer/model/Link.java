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
package de.lb.cpx.ruleviewer.model;

import de.lb.cpx.rule.criteria.TypesAndOperationsManager;
import de.lb.cpx.rule.criteria.model.TypesAndOperations.OperationGroups.OperationGroup.Operation;
import de.lb.cpx.rule.element.model.RulesOperator;
import de.lb.cpx.ruleviewer.properties.LinkOperatorEditor;
import de.lb.cpx.ruleviewer.properties.SettingsDescriptor;
import de.lb.cpx.ruleviewer.skin.LinkSkin;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Skin;

/**
 * Class to represent link between terms in the ui handles conversion between
 * rule and internal object
 *
 * @author wilde
 */
public class Link extends SelectableControl {

//    public static final Operation DEFAULT_OPERATION = new Operation() {
//        @Override
//        public String getName() {
//            return "&&";
//        }
//
//    };
    private PropertyDescriptor[] propDescriptors;
    private static final String DEFAULT_STYLE_CLASS = "link";

    /**
     * creates new instance with sepecific operation
     *
     * @param pValue operation to link terms
     */
    public Link(Operation pValue) {
        rulesOperatorProperty().addListener(new ChangeListener<RulesOperator>() {
            @Override
            public void changed(ObservableValue<? extends RulesOperator> observable, RulesOperator oldValue, RulesOperator newValue) {
                updateOperatorValue(newValue);
            }
        });
        setOperator(pValue);
    }

    /**
     * no arg constructor with default operation
     */
    public Link() {
        this(null);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    @Override
    public String toString() {
        return getOperator().getName();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new LinkSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(Link.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin();
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
//        if (ViewMode.READ_ONLY.equals(getViewMode())) {
//            return new PropertyDescriptor[0];
//        }
        if (propDescriptors == null) {
            propDescriptors = new PropertyDescriptor[1];
            try {
                propDescriptors[0] = new SettingsDescriptor("operatorProperty", Link.class, "getOperator", "setOperator");
                propDescriptors[0].setPropertyEditorClass(LinkOperatorEditor.class);
                propDescriptors[0].setDisplayName("Operator:");
                propDescriptors[0].setPreferred(true);

            } catch (IntrospectionException ex) {
                LOG.log(Level.WARNING, ex.toString(), ex);
            }
        }
        return propDescriptors.clone();
    }
    private static final Logger LOG = Logger.getLogger(Link.class.getName());

    @Override
    public String getDisplayName() {
        return "Verknüpfung";
    }

    @Override
    public String getDisplayText() {
        return getDisplayName() + ": "
                + (getOperator() != null ? getOperator().toString() : "");

    }

    private void updateOperatorValue(RulesOperator pValue) {
        if (pValue == null) {
            return;
        }
        setOperator(TypesAndOperationsManager.instance().getTermOperator(pValue.getOpType()));
    }
    private ObjectProperty<RulesOperator> rulesOperatorProperty;

    /**
     * @return objectproperty for rule definition
     */
    public final ObjectProperty<RulesOperator> rulesOperatorProperty() {
        if (rulesOperatorProperty == null) {
            rulesOperatorProperty = new SimpleObjectProperty<>();
        }
        return rulesOperatorProperty;
    }

    /**
     * @return operator object for rule definition
     */
    public RulesOperator getRulesOperator() {
        return rulesOperatorProperty().get();
    }

    /**
     * @param pOperator for object from rule definition
     */
    public void setRulesOperator(RulesOperator pOperator) {
        rulesOperatorProperty().set(pOperator);
    }
    private ObjectProperty<Operation> operatorProperty;

    /**
     * @see Operation operation from xml
     * @return operator property
     */
    public ObjectProperty<Operation> operatorProperty() {
        if (operatorProperty == null) {
            operatorProperty = new SimpleObjectProperty<>(TypesAndOperationsManager.instance().getTermOperator("&&"));
            if (getRulesOperator() != null) {
                operatorProperty.set(TypesAndOperationsManager.instance().getTermOperator(getRulesOperator().getOpType()));
            }
            operatorProperty.addListener((ObservableValue<? extends Operation> observable, Operation oldValue, Operation newValue) -> {
                updateOperator();
                getProperties().put(UPDATE_CONTROL, null);
            });
        }
        return operatorProperty;
    }

    /**
     * @return currently set operator to link to terms
     */
    public Operation getOperator() {
        return operatorProperty().get();
    }

    /**
     * @see Operation operation from xml
     * @param pOperator operator to link to terms
     */
    public final void setOperator(Operation pOperator) {
        operatorProperty().set(pOperator);
    }

    private final StringProperty placeholderTextProperty = new SimpleStringProperty("Term platzierbar");

    /**
     * @return placeholder text Property
     */
    public StringProperty placeholderTextProperty() {
        return placeholderTextProperty;
    }

    /**
     * @return placeholder text
     */
    public String getPlaceholderText() {
        return placeholderTextProperty().get();
    }

    /**
     * @param placeholderText set placeholder text
     */
    public void setPlaceholderText(String placeholderText) {
        placeholderTextProperty().set(placeholderText);
    }

    private void updateOperator() {
        if (getRulesOperator() == null) {
            return;
        }
        getRulesOperator().setOpType(getOperator() != null ? getOperator().getName() : "");
    }
}
