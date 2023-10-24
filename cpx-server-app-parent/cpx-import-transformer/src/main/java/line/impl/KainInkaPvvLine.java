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
public class KainInkaPvvLine extends AbstractLine {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(KainInkaPvvLine.class.getName());

    protected static final Set<Field> FIELDS = new TreeSet<>();

    public static final Field BILL_DATE = createField(FIELDS, 7, "BILL_DATE", Type.DATETIME);
    public static final Field BILL_NR = createField(FIELDS, 8, "BILL_NR", Type.STRING);
    public static final Field INFORMATION_KEY_30 = createField(FIELDS, 9, "INFORMATION_KEY_30", Type.STRING);
    public static final Field KAIN_INKA_NR = createField(FIELDS, 10, "KAIN_INKA_NR", Type.STRING);

    public static KainInkaPvvLine unserialize(final String pCsvData) {
        return (KainInkaPvvLine) AbstractLine.unserialize(new KainInkaPvvLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
