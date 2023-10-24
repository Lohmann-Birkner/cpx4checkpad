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
public class LabLine extends AbstractLine {

    private static final long serialVersionUID = 1L;

    protected static final Set<Field> FIELDS = new TreeSet<>();
    public static final Field LAB_ANALYSIS = createField(FIELDS, 7, "LAB_ANALYSIS", Type.STRING);
    public static final Field LAB_BENCHMARK = createField(FIELDS, 8, "LAB_BENCHMARK", Type.STRING);
    public static final Field LAB_COMMENT = createField(FIELDS, 9, "LAB_COMMENT", Type.STRING);
    public static final Field LAB_DESCRIPTION = createField(FIELDS, 10, "LAB_DESCRIPTION", Type.STRING);
    public static final Field LAB_GROUP = createField(FIELDS, 11, "LAB_GROUP", Type.STRING);
    public static final Field LAB_ANALYSIS_DATE = createField(FIELDS, 12, "LAB_ANALYSIS_DATE", Type.DATE);
    public static final Field LAB_CATEGORY = createField(FIELDS, 13, "LAB_CATEGORY", Type.INT);
    public static final Field LAB_DATE = createField(FIELDS, 14, "LAB_DATE", Type.DATE);
    public static final Field LAB_KIS_EXTERN_KEY = createField(FIELDS, 15, "LAB_KIS_EXTERN_KEY", Type.STRING);
    public static final Field LAB_LOCKDEL = createField(FIELDS, 16, "LAB_LOCKDEL", Type.INT);
    public static final Field LAB_MAX_LIMIT = createField(FIELDS, 17, "LAB_MAX_LIMIT", Type.FLOAT);
    public static final Field LAB_MIN_LIMIT = createField(FIELDS, 18, "LAB_MIN_LIMIT", Type.FLOAT);
    public static final Field LAB_POSITION = createField(FIELDS, 19, "LAB_POSITION", Type.INT);
    public static final Field LAB_VALUE = createField(FIELDS, 20, "LAB_VALUE", Type.FLOAT);
    public static final Field LAB_VALUE_2 = createField(FIELDS, 21, "LAB_VALUE_2", Type.FLOAT);
    public static final Field LAB_METHOD = createField(FIELDS, 22, "LAB_METHOD", Type.STRING);
    public static final Field LAB_RANGE = createField(FIELDS, 23, "LAB_RANGE", Type.STRING);
    public static final Field LAB_TEXT = createField(FIELDS, 24, "LAB_TEXT", Type.STRING);
    public static final Field LAB_UNIT = createField(FIELDS, 25, "LAB_UNIT", Type.STRING);

    private static final Logger LOG = Logger.getLogger(LabLine.class.getName());

    public static LabLine unserialize(final String pCsvData) {
        return (LabLine) AbstractLine.unserialize(new LabLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
