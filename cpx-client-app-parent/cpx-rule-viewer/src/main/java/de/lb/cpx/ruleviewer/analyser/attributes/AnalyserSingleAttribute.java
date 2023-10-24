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
package de.lb.cpx.ruleviewer.analyser.attributes;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wilde
 */
public class AnalyserSingleAttribute extends AnalyserAttribute {

    private final String readMethodName;
    private final String writeMethodName;
    private final String propertyName;
    private PropertyDescriptor propertyDescriptor;

    public AnalyserSingleAttribute(String pPropertyName, Class<?> pBeanClass, String pReadMethodName, String pWriteMethodName) {
        super(pBeanClass);
        this.propertyName = pPropertyName;
        this.readMethodName = pReadMethodName;
        this.writeMethodName = pWriteMethodName;
        setKey(propertyName);
    }

    public String getReadMethodName() {
        return readMethodName;
    }

    public String getWriteMethodName() {
        return writeMethodName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public PropertyDescriptor getPropertyDescriptor() {
        if (propertyDescriptor == null) {
            try {
                propertyDescriptor = new PropertyDescriptor(propertyName, getBeanClass(), readMethodName, writeMethodName);
                if (getEditorClass() != null) {
                    propertyDescriptor.setPropertyEditorClass(getEditorClass());
                }
            } catch (IntrospectionException ex) {
                Logger.getLogger(AnalyserSingleAttribute.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return propertyDescriptor;
    }
}
