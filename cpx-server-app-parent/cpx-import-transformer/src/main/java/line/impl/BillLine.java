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
public class BillLine extends AbstractLine {

    private static final long serialVersionUID = 1L;

    protected static final Set<Field> FIELDS = new TreeSet<>();
    public static final Field RECHNUNGSNR = createField(FIELDS, 7, "RECHNUNGSNR", Type.STRING);
    public static final Field RECHNUNGSDATUM = createField(FIELDS, 8, "RECHNUNGSDATUM", Type.DATETIME);
    public static final Field RECHNUNGSART = createField(FIELDS, 9, "RECHNUNGSART", Type.STRING);
    //final public static Field RECHNUNGSSTATUS = createField(FIELDS, 10, "RECHNUNGSSTATUS", Type.STRING);

    public static BillLine unserialize(final String pCsvData) {
        return (BillLine) AbstractLine.unserialize(new BillLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
