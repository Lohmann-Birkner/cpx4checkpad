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
public class ProcedureLine extends AbstractLine {

    private static final long serialVersionUID = 1L;

    protected static final Set<Field> FIELDS = new TreeSet<>();
    public static final Field CODE = createField(FIELDS, 7, "CODE", Type.STRING);
    public static final Field DATUM = createField(FIELDS, 8, "DATUM", Type.DATETIME);
    public static final Field DEP_NR = createField(FIELDS, 9, "DEP_NR", Type.STRING);
    public static final Field WARD_NR = createField(FIELDS, 10, "WARD_NR", Type.STRING);
    public static final Field LOKALISATION = createField(FIELDS, 11, "LOKALISATION", Type.STRING, 1);
    public static final Field TO_GROUP = createField(FIELDS, 12, "TO_GROUP", Type.BOOLEAN);
    public static final Field BELEG_OP = createField(FIELDS, 13, "BELEG_OP", Type.INT);
    public static final Field BELEG_ANA = createField(FIELDS, 14, "BELEG_ANA", Type.INT);
    public static final Field BELEG_HEB = createField(FIELDS, 15, "BELEG_HEB", Type.INT);
    private static final Logger LOG = Logger.getLogger(ProcedureLine.class.getName());

    public static ProcedureLine unserialize(final String pCsvData) {
        return (ProcedureLine) AbstractLine.unserialize(new ProcedureLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
