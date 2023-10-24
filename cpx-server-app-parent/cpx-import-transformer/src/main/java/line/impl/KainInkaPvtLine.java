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
public class KainInkaPvtLine extends AbstractLine {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(KainInkaPvtLine.class.getName());

    protected static final Set<Field> FIELDS = new TreeSet<>();

    public static final Field MAIN_DIAG_ICD = createField(FIELDS, 7, "MAIN_DIAG_ICD", Type.STRING);
    public static final Field MAIN_DIAG_LOC_EN = createField(FIELDS, 8, "MAIN_DIAG_LOC_EN", Type.STRING, 1);
    public static final Field MAIN_DIAG_SECONDARY_ICD = createField(FIELDS, 9, "MAIN_DIAG_SECONDARY_ICD", Type.STRING);
    public static final Field MAIN_DIAG_SECONDARY_LOC_EN = createField(FIELDS, 10, "MAIN_DIAG_SECONDARY_LOC_EN", Type.STRING);
    public static final Field OPS_CODE = createField(FIELDS, 11, "OPS_CODE", Type.STRING);
    public static final Field OPS_LOCALISATION_EN = createField(FIELDS, 12, "OPS_LOCALISATION_EN", Type.STRING, 1);
    public static final Field SECONDARY_DIAG_ICD = createField(FIELDS, 13, "SECONDARY_DIAG_ICD", Type.STRING);
    public static final Field SECONDARY_DIAG_LOC_EN = createField(FIELDS, 14, "SECONDARY_DIAG_LOC_EN", Type.STRING, 1);
    public static final Field SECONDARY_SECOND_DIAG_ICD = createField(FIELDS, 15, "SECONDARY_SECOND_DIAG_ICD", Type.STRING);
    public static final Field SECONDARY_SECOND_DIAG_LOC_EN = createField(FIELDS, 16, "SECONDARY_SECOND_DIAG_LOC_EN", Type.STRING, 1);
    public static final Field TEXT = createField(FIELDS, 17, "TEXT", Type.STRING, 256);
    public static final Field KAIN_INKA_PVV_NR = createField(FIELDS, 18, "KAIN_INKA_PVV_NR", Type.STRING);

    public static KainInkaPvtLine unserialize(final String pCsvData) {
        return (KainInkaPvtLine) AbstractLine.unserialize(new KainInkaPvtLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
