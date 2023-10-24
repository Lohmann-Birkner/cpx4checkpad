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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wilde
 */
public class AnalyserAttributes {

    protected final Map<String, AnalyserAttribute> attributeMap = new LinkedHashMap<>();

    protected final void add(String pKey, AnalyserAttribute pAttribute) {
        attributeMap.put(pKey, pAttribute);
    }

    protected final AnalyserSingleAttribute addSingleAttribute(String pKey, Class<?> beanClass) {
        return addSingleAttribute(pKey, beanClass, AnalyserAttributesHelper.getReadMethodeForKey(pKey), AnalyserAttributesHelper.getWriteMethodeForKey(pKey));
    }

    protected final AnalyserSingleAttribute addSingleAttribute(String pKey, Class<?> beanClass, String pReadMethode, String pWriteMethode) {
        AnalyserSingleAttribute att = new AnalyserSingleAttribute(pKey, beanClass, pReadMethode, pWriteMethode);
        add(pKey, att);
        return att;
    }

    protected final AnalyserGroupAttribute addGroupAttribute(String pKey, Class<?> beanClass) {
        AnalyserGroupAttribute att = new AnalyserGroupAttribute(beanClass);
        att.setKey(pKey);
        add(pKey, att);
        return att;
    }

    public final List<AnalyserAttribute> getAttributes() {
        return Lists.newArrayList(attributeMap.values());
    }

    public final List<AnalyserAttribute> getSortedAttributes() {
        List<AnalyserAttribute> attributes = getAttributes();
        attributes.sort(Comparator.comparing(AnalyserAttribute::getDisplayName));
        return attributes;
    }

    public final AnalyserAttribute getAttribute(String pKey) {
        return attributeMap.get(pKey);
    }

    public final AnalyserAttribute getAttributeByDisplayName(String pDisplayName) {
        for (AnalyserAttribute value : attributeMap.values()) {
            if (value.getDisplayName().equals(pDisplayName)) {
                return value;
            }
        }
        return null;
    }
}
