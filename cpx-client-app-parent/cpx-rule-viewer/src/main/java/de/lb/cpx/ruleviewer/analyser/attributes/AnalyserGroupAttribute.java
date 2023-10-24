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

import com.google.common.collect.Lists;
import de.lb.cpx.ruleviewer.util.AnalyserAttributesHelper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wilde
 */
public class AnalyserGroupAttribute extends AnalyserAttribute {

    private final List<AnalyserSingleAttribute> attributes = new ArrayList<>();

    public AnalyserGroupAttribute(Class<?> beanClass) {
        super(beanClass);
    }

    public AnalyserGroupAttribute addAttribute(AnalyserSingleAttribute pAttribute) {
        attributes.add(pAttribute);
        return this;
    }

    public AnalyserGroupAttribute addAttribute(String pPropertyName, String pWriteMethode, String pReadMethode) {
        return addAttribute(pPropertyName, getBeanClass(), pWriteMethode, pReadMethode);
    }

    public AnalyserGroupAttribute addAttribute(String pPropertyName, Class<?> pBeanClass, String pWriteMethode, String pReadMethode) {
        return addAttribute(new AnalyserSingleAttribute(pPropertyName, pBeanClass, pWriteMethode, pReadMethode));
    }

    public AnalyserGroupAttribute addAttribute(String pPropertyName, Class<?> pBeanClass) {
        return addAttribute(pPropertyName, pBeanClass, AnalyserAttributesHelper.getReadMethodeForKey(pPropertyName), AnalyserAttributesHelper.getWriteMethodeForKey(pPropertyName));
    }

    public AnalyserGroupAttribute addAttribute(String pPropertyName) {
        return addAttribute(pPropertyName, getBeanClass());
    }

    public List<AnalyserSingleAttribute> getAttributes() {
        return Lists.newArrayList(attributes);
    }

    public AnalyserSingleAttribute getAttribute(String pKey) {
        for (AnalyserSingleAttribute attribute : getAttributes()) {
            if (pKey.equals(attribute.getKey())) {
                return attribute;
            }
        }
        return null;
    }
}
