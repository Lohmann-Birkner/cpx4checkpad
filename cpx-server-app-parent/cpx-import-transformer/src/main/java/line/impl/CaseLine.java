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
import line.AbstractLine;
import line.Field;
import line.Field.Type;

/**
 *
 * @author Dirk Niemeier
 */
public class CaseLine extends AbstractLine {

    private static final long serialVersionUID = 1L;

    protected static final Set<Field> FIELDS = new TreeSet<>();
    public static final Field AUFNAHMEDATUM = createField(FIELDS, 7, "AUFNAHMEDATUM", Type.DATETIME);
    public static final Field ENTGELTBEREICH = createField(FIELDS, 8, "ENTGELTBEREICH", Type.STRING);
    public static final Field ENTLASSUNGSDATUM = createField(FIELDS, 9, "ENTLASSUNGSDATUM", Type.DATETIME);
    public static final Field ALTER_IN_JAHREN = createField(FIELDS, 10, "ALTER_IN_JAHREN", Type.INT);
    public static final Field ALTER_IN_TAGEN = createField(FIELDS, 11, "ALTER_IN_TAGEN", Type.INT);
    public static final Field GEWICHT = createField(FIELDS, 12, "GEWICHT", Type.INT);
    public static final Field ENTLASSUNGSGRUND12 = createField(FIELDS, 13, "ENTLASSUNGSGRUND12", Type.STRING, 2);
    public static final Field ENTLASSUNGSGRUND3 = createField(FIELDS, 14, "ENTLASSUNGSGRUND3", Type.STRING, 1);
    public static final Field AUFNAHMEGRUND1 = createField(FIELDS, 15, "AUFNAHMEGRUND1", Type.STRING, 2);
    public static final Field AUFNAHMEGRUND2 = createField(FIELDS, 16, "AUFNAHMEGRUND2", Type.STRING, 2);
    public static final Field AUFNAHMEANLASS = createField(FIELDS, 17, "AUFNAHMEANLASS", Type.STRING, 1);
    public static final Field BEATMUNGSSTUNDEN = createField(FIELDS, 18, "BEATMUNGSSTUNDEN", Type.INT);
    public static final Field FALLART = createField(FIELDS, 19, "FALLART", Type.STRING);
    public static final Field STORNIERT = createField(FIELDS, 20, "STORNIERT", Type.BOOLEAN);
    public static final Field VWD_INTENSIV = createField(FIELDS, 21, "VWD_INTENSIV", Type.INT);
    //final public static Field ERBRINGUNGSART = createField(FIELDS, 22, "ERBRINGUNGSART", Type.STRING);
    public static final Field GESETZL_PSYCHSTATUS = createField(FIELDS, 22, "GESETZL_PSYCHSTATUS", Type.INT);
    public static final Field URLAUBSTAGE = createField(FIELDS, 23, "URLAUBSTAGE", Type.INT);
    public static final Field VWD = createField(FIELDS, 24, "VWD", Type.INT);
    public static final Field VWD_SIMULIERT = createField(FIELDS, 25, "VWD_SIMULIERT", Type.INT);
    public static final Field VERSICHERTENNR = createField(FIELDS, 26, "VERSICHERTENNR", Type.STRING, 50);
    public static final Field KASSE = createField(FIELDS, 27, "KASSE", Type.STRING, 50);
    public static final Field FALLSTATUS = createField(FIELDS, 28, "FALLSTATUS", Type.STRING, 10);
    public static final Field GESCHLECHT = createField(FIELDS, 29, "GESCHLECHT", Type.STRING, 1);
    public static final Field STRING1 = createField(FIELDS, 30, "STRING1", Type.STRING, 100);
    public static final Field STRING2 = createField(FIELDS, 31, "STRING2", Type.STRING, 100);
    public static final Field STRING3 = createField(FIELDS, 32, "STRING3", Type.STRING, 100);
    public static final Field STRING4 = createField(FIELDS, 33, "STRING4", Type.STRING, 100);
    public static final Field STRING5 = createField(FIELDS, 34, "STRING5", Type.STRING, 100);
    public static final Field STRING6 = createField(FIELDS, 35, "STRING6", Type.STRING, 500); // CPX-2675: this field contains the list of numbers of cases that ware merged. In the error case there ware 14 cases, so the string length was > 100; we change it to 500
    public static final Field STRING7 = createField(FIELDS, 36, "STRING7", Type.STRING, 100);
    public static final Field STRING8 = createField(FIELDS, 37, "STRING8", Type.STRING, 100);
    public static final Field STRING9 = createField(FIELDS, 38, "STRING9", Type.STRING, 100);
    public static final Field STRING10 = createField(FIELDS, 39, "STRING10", Type.STRING, 100);
    public static final Field NUMERIC1 = createField(FIELDS, 40, "NUMERIC1", Type.INT);
    public static final Field NUMERIC2 = createField(FIELDS, 41, "NUMERIC2", Type.INT);
    public static final Field NUMERIC3 = createField(FIELDS, 42, "NUMERIC3", Type.INT);
    public static final Field NUMERIC4 = createField(FIELDS, 43, "NUMERIC4", Type.INT);
    public static final Field NUMERIC5 = createField(FIELDS, 44, "NUMERIC5", Type.INT);
    public static final Field NUMERIC6 = createField(FIELDS, 45, "NUMERIC6", Type.INT);
    public static final Field NUMERIC7 = createField(FIELDS, 46, "NUMERIC7", Type.INT);
    public static final Field NUMERIC8 = createField(FIELDS, 47, "NUMERIC8", Type.INT);
    public static final Field NUMERIC9 = createField(FIELDS, 48, "NUMERIC9", Type.INT);
    public static final Field NUMERIC10 = createField(FIELDS, 49, "NUMERIC10", Type.INT);
    public static final Field MD_TOB = createField(FIELDS, 50, "MD_TOB", Type.INT);
    public static final Field BILLING_DATE = createField(FIELDS, 51, "BILLING_DATE", Type.DATETIME);

    public static CaseLine unserialize(final String pCsvData) {
        return (CaseLine) AbstractLine.unserialize(new CaseLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
