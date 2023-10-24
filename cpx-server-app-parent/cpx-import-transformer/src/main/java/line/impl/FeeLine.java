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
public class FeeLine extends AbstractLine {

    private static final long serialVersionUID = 1L;

    protected static final Set<Field> FIELDS = new TreeSet<>();
    public static final Field ENTGELTSCHLUESSEL = createField(FIELDS, 7, "ENTGELTSCHLUESSEL", Type.STRING);
    public static final Field ANZAHL = createField(FIELDS, 8, "ANZAHL", Type.INT);
    public static final Field BETRAG = createField(FIELDS, 9, "BETRAG", Type.MONEY);
    public static final Field SUMME = createField(FIELDS, 10, "SUMME", Type.MONEY);
    public static final Field VON = createField(FIELDS, 11, "VON", Type.DATETIME);
    public static final Field BIS = createField(FIELDS, 12, "BIS", Type.DATETIME);
    public static final Field TOB = createField(FIELDS, 13, "TOB", Type.INT);
    public static final Field RECHNUNG_NR = createField(FIELDS, 14, "RECHNUNG_NR", Type.STRING);
    public static final Field KASSE = createField(FIELDS, 15, "KASSE", Type.STRING);
    public static final Field IS_RECHNUNG = createField(FIELDS, 16, "IS_RECHNUNG", Type.BOOLEAN);
    private static final Logger LOG = Logger.getLogger(FeeLine.class.getName());

    public static FeeLine unserialize(final String pCsvData) {
        return (FeeLine) AbstractLine.unserialize(new FeeLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
