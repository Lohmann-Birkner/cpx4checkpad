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
public class DrugLine extends AbstractLine {

    private static final long serialVersionUID = 1L;

    protected static final Set<Field> FIELDS = new TreeSet<>();
    public static final Field PICNR = createField(FIELDS, 7, "PICNR", Type.STRING);
    public static final Field FAKTOR = createField(FIELDS, 8, "FAKTOR", Type.FLOAT);
    public static final Field BRUTTO = createField(FIELDS, 9, "BRUTTO", Type.MONEY);
    public static final Field PZN = createField(FIELDS, 10, "PZN", Type.STRING);
    public static final Field ATC = createField(FIELDS, 11, "ATC", Type.STRING);
    public static final Field GENERIKA = createField(FIELDS, 12, "GENERIKA", Type.INT);
    public static final Field DF = createField(FIELDS, 13, "DF", Type.STRING);
    public static final Field PACKUNGSGR = createField(FIELDS, 14, "PACKUNGSGR", Type.STRING);
    public static final Field NORMGR = createField(FIELDS, 15, "NORMGR", Type.INT);
    public static final Field ARZTNR = createField(FIELDS, 16, "ARZTNR", Type.INT);
    public static final Field BRUTTO_GESAMT = createField(FIELDS, 17, "BRUTTO_GESAMT", Type.MONEY);
    public static final Field APOIK = createField(FIELDS, 18, "APOIK", Type.STRING);
    public static final Field BMG = createField(FIELDS, 19, "BMG", Type.INT);
    public static final Field UNFK = createField(FIELDS, 20, "UNFK", Type.INT);
    public static final Field VERORD = createField(FIELDS, 21, "VERORD", Type.DATE);
    public static final Field ABG = createField(FIELDS, 22, "ABG", Type.DATE);
    //final public static Field RECHNUNGSSTATUS = createField(FIELDS, 10, "RECHNUNGSSTATUS", Type.STRING);

    public static DrugLine unserialize(final String pCsvData) {
        return (DrugLine) AbstractLine.unserialize(new DrugLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
