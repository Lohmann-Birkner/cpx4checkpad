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
public class SapFiBillLine extends AbstractLine {

    private static final long serialVersionUID = 1L;

    protected static final Set<Field> FIELDS = new TreeSet<>();
    public static final Field INVOICE = createField(FIELDS, 7, "INVOICE", Type.STRING);
    public static final Field FISCAL_YEAR = createField(FIELDS, 8, "FISCAL_YEAR", Type.INT);
    public static final Field INVOICE_DATE = createField(FIELDS, 9, "INVOICE_DATE", Type.DATE);
    public static final Field INVOICE_KIND = createField(FIELDS, 10, "INVOICE_KIND", Type.STRING);
    public static final Field INVOICE_TYPE = createField(FIELDS, 11, "INVOICE_TYPE", Type.STRING);
    public static final Field NET_VALUE = createField(FIELDS, 12, "NET_VALUE", Type.FLOAT);
    public static final Field RECEIVER_REF = createField(FIELDS, 13, "RECEIVER_REF", Type.STRING);
    public static final Field REFERENCE_CURRENCY = createField(FIELDS, 14, "REFERENCE_CURRENCY", Type.STRING);
    public static final Field REFERENCE_TYPE = createField(FIELDS, 15, "REFERENCE_TYPE", Type.STRING);
    public static final Field STATE = createField(FIELDS, 16, "STATE", Type.STRING);
    public static final Field STORNO_REF = createField(FIELDS, 17, "STORNO_REF", Type.STRING);
    //final public static Field RECHNUNGSSTATUS = createField(FIELDS, 10, "RECHNUNGSSTATUS", Type.STRING);
    private static final Logger LOG = Logger.getLogger(SapFiBillLine.class.getName());

    public static SapFiBillLine unserialize(final String pCsvData) {
        return (SapFiBillLine) AbstractLine.unserialize(new SapFiBillLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
