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
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author wilde
 */
public class SettingsDescriptor extends PropertyDescriptor {

//    public SettingsDescriptor (String pPropertyName, Class<?> pBeanClass) throws IntrospectionException {
//        super(pPropertyName, pBeanClass);
//    }
    public SettingsDescriptor(String pPropertyName, Class<?> pBeanClass, String pReadMethodName, String pWriteMethodName) throws IntrospectionException {
        super(pPropertyName, pBeanClass, pReadMethodName, pWriteMethodName);

    }
//
//    public SettingsDescriptor (String pPropertyName, Method pReadMethod, Method pWriteMethod) throws IntrospectionException {
//        super(pPropertyName, pReadMethod, pWriteMethod);
//    }

    private final BooleanProperty editable = new SimpleBooleanProperty(true);

    /**
     * @return the editable
     */
    public boolean isEditable() {
        return editable.get();
    }

    /**
     * @param editable the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public BooleanProperty editableProperty() {
        return editable;
    }

    private ObservableList<Operation> values = FXCollections.emptyObservableList();

    public ObservableList<Operation> getValues() {
        return values;
    }

    public void setValues(ObservableList<Operation> pValues) {
        values = pValues;
    }
}
