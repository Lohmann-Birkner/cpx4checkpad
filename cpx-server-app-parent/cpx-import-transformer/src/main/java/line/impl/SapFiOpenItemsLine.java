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
public class SapFiOpenItemsLine extends AbstractLine {

    private static final long serialVersionUID = 1L;

    protected static final Set<Field> FIELDS = new TreeSet<>();
    public static final Field COMPANY_CODE = createField(FIELDS, 7, "COMPANY_CODE", Type.STRING);
    public static final Field CURRENCY_KEY = createField(FIELDS, 8, "CURRENCY_KEY", Type.STRING);
    public static final Field CUSTOMER_NUMBER = createField(FIELDS, 9, "CUSTOMER_NUMBER", Type.STRING);
    public static final Field DEBIT_CREDIT_KEY = createField(FIELDS, 10, "DEBIT_CREDIT_KEY", Type.STRING);
    public static final Field FISCAL_YEAR = createField(FIELDS, 11, "FISCAL_YEAR", Type.INT);
    public static final Field KIND_OF_RECEIPT = createField(FIELDS, 12, "KIND_OF_RECEIPT", Type.STRING);
    public static final Field NET_VALUE = createField(FIELDS, 13, "NET_VALUE", Type.FLOAT);
    public static final Field NUMBER_RECEIPT = createField(FIELDS, 14, "NUMBER_RECEIPT", Type.STRING);
    public static final Field ORDERDATE_RECEIPT = createField(FIELDS, 15, "ORDERDATE_RECEIPT", Type.DATE);
    public static final Field POSTING_KEY = createField(FIELDS, 16, "POSTING_KEY", Type.STRING);
    public static final Field RECEIPTDATE_RECEIPT = createField(FIELDS, 17, "RECEIPTDATE_RECEIPT", Type.DATE);
    public static final Field RECORDINGDATE_RECEIPT = createField(FIELDS, 18, "RECORDINGDATE_RECEIPT", Type.DATE);
    public static final Field REF_NUMBER = createField(FIELDS, 19, "REF_NUMBER", Type.STRING);
    public static final Field REFNUMBER_RECEIPT = createField(FIELDS, 20, "REFNUMBER_RECEIPT", Type.STRING);
    public static final Field TEXT = createField(FIELDS, 21, "TEXT", Type.STRING);
    public static final Field VALUE = createField(FIELDS, 22, "VALUE", Type.FLOAT);
    //final public static Field RECHNUNGSSTATUS = createField(FIELDS, 10, "RECHNUNGSSTATUS", Type.STRING);
    private static final Logger LOG = Logger.getLogger(SapFiOpenItemsLine.class.getName());

    public static SapFiOpenItemsLine unserialize(final String pCsvData) {
        return (SapFiOpenItemsLine) AbstractLine.unserialize(new SapFiOpenItemsLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
