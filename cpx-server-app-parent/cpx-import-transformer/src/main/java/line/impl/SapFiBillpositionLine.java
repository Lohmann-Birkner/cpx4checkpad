/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package line.impl;

import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import line.AbstractLine;
import line.Field;
import line.Field.Type;

/**
 *
 * @author Dirk Niemeier
 */
public class SapFiBillpositionLine extends AbstractLine {

    private static final long serialVersionUID = 1L;

    protected static final Set<Field> FIELDS = new TreeSet<>();
    public static final Field SAP_FI_BILL_NR = createField(FIELDS, 7, "SAP_FI_BILL_NR", Type.STRING);
    public static final Field AMOUNT = createField(FIELDS, 8, "AMOUNT", Type.FLOAT);
    public static final Field BASE_VALUE = createField(FIELDS, 9, "BASE_VALUE", Type.FLOAT);
    public static final Field INVOICE = createField(FIELDS, 10, "INVOICE", Type.STRING);
    public static final Field NET_VALUE = createField(FIELDS, 11, "NET_VALUE", Type.FLOAT);
    public static final Field POSITION_NUMBER = createField(FIELDS, 12, "POSITION_NUMBER", Type.STRING);
    public static final Field REFERENCE_ID = createField(FIELDS, 13, "REFERENCE_ID", Type.STRING);
    public static final Field TEXT = createField(FIELDS, 14, "TEXT", Type.STRING);
    //final public static Field RECHNUNGSSTATUS = createField(FIELDS, 10, "RECHNUNGSSTATUS", Type.STRING);
    private static final Logger LOG = Logger.getLogger(SapFiBillpositionLine.class.getName());

    public static SapFiBillpositionLine unserialize(final String pCsvData) {
        return (SapFiBillpositionLine) AbstractLine.unserialize(new SapFiBillpositionLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
