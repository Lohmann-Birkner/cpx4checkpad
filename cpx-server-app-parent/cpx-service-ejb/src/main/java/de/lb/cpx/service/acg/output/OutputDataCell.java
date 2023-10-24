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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.acg.output;

/**
 *
 * @author niemeier
 */
public class OutputDataCell {

    public final int index;
    public final String name;
    public final String value;

    public OutputDataCell(final int pIndex, final String pName, final String pValue) {
        index = pIndex;
        name = pName == null ? "" : pName.trim();
        value = pValue == null ? "" : pValue.trim();
    }

    @Override
    public String toString() {
        return "OutputDataCell{" + "index=" + index + ", name=" + name + ", value=" + value + '}';
    }

}
