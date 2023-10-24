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
public class DiagnoseLine extends AbstractLine {

    private static final long serialVersionUID = 1L;

    protected static final Set<Field> FIELDS = new TreeSet<>();
    public static final Field CODE = createField(FIELDS, 7, "CODE", Type.STRING);
    //final public static Field ART = createField(FIELDS, 8, "ART", Type.STRING, 1);
    public static final Field DEP_NR = createField(FIELDS, 8, "DEP_NR", Type.STRING);
    public static final Field WARD_NR = createField(FIELDS, 9, "WARD_NR", Type.STRING);
    public static final Field ICD_TYPE = createField(FIELDS, 10, "ICD_TYPE", Type.INT, 1);
    public static final Field REF_ICD_NR = createField(FIELDS, 11, "REF_ICD_NR", Type.STRING);
    public static final Field REF_ICD_TYPE = createField(FIELDS, 12, "REF_ICD_TYPE", Type.INT, 1);
    public static final Field LOKALISATION = createField(FIELDS, 13, "LOKALISATION", Type.STRING, 1);
    public static final Field TO_GROUP = createField(FIELDS, 14, "TO_GROUP", Type.BOOLEAN);
    public static final Field HDB = createField(FIELDS, 15, "HDB", Type.BOOLEAN);
    public static final Field HDX = createField(FIELDS, 16, "HDX", Type.BOOLEAN);
    private static final Logger LOG = Logger.getLogger(DiagnoseLine.class.getName());

    public static DiagnoseLine unserialize(final String pCsvData) {
        return (DiagnoseLine) AbstractLine.unserialize(new DiagnoseLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
