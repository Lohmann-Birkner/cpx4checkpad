/*
 * Copyright (c) 2017 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.tooltip;

/**
 * Implementation of the Case Version Tooltip to unify behaviour
 *
 * @author wilde
 */
public class LineObject {

    private final String description;
    private final String value;
    private final String opterator;
    
    public LineObject(String pOperator, String pDescription, String pValue) {
        opterator = pOperator;
        description = pDescription;
        value = pValue;
    }
    public LineObject(String pDescription, String pValue) {
        this(null,pDescription,pValue);
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public String getOpterator() {
        return opterator;
    }
    
}
