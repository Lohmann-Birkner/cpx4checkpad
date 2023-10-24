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

/**
 *
 * @author wilde
 */
public class AnalyserAttribute {

    private final Class<?> beanClass;
    private String displayName;
    private Class<?> editorClass;
    private String key;

    public AnalyserAttribute(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public AnalyserAttribute setDisplayName(String pDisplayName) {
        displayName = pDisplayName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Class<?> getEditorClass() {
        return editorClass;
    }

    public AnalyserAttribute setEditorClass(Class<?> pEditorClass) {
        editorClass = pEditorClass;
        return this;
    }

    public final String getKey() {
        return key;
    }

    public final void setKey(String key) {
        this.key = key;
    }

}
