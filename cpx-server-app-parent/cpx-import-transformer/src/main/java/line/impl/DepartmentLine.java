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
public class DepartmentLine extends AbstractLine {

    private static final long serialVersionUID = 1L;

    protected static final Set<Field> FIELDS = new TreeSet<>();
    public static final Field CODE = createField(FIELDS, 7, "CODE", Type.STRING);
    public static final Field CODE_INTERN = createField(FIELDS, 8, "CODE_INTERN", Type.STRING);
    public static final Field VERLEGUNGSDATUM = createField(FIELDS, 9, "VERLEGUNGSDATUM", Type.DATETIME);
    public static final Field ERBRINGUNGSART = createField(FIELDS, 10, "ERBRINGUNGSART", Type.STRING);
    public static final Field ERBRINGUNGSART_INT = createField(FIELDS, 11, "ERBRINGUNGSART_INT", Type.INT);
    public static final Field AUFNEHMENDE_IK = createField(FIELDS, 12, "AUFNEHMENDE_IK", Type.STRING);
    public static final Field ENTLASSUNGSDATUM = createField(FIELDS, 13, "ENTLASSUNGSDATUM", Type.DATETIME);
    public static final Field IS_HOSPITAL = createField(FIELDS, 14, "IS_HOSPITAL", Type.BOOLEAN);
    public static final Field IS_PSEUDO = createField(FIELDS, 15, "IS_PSEUDO", Type.BOOLEAN);
    public static final Field DAUER = createField(FIELDS, 16, "DAUER", Type.INT);
    public static final Field IS_BED_INTENSIV = createField(FIELDS, 17, "IS_BED_INTENSIV", Type.BOOLEAN);
    public static final Field LOCATION_NUMBER = createField(FIELDS, 18, "LOCATION_NUMBER", Type.INT);
    //final public static Field ERBRINGUNGSART = createField(FIELDS, 12, "ERBRINGUNGSART", Type.STRING);
    //final public static Field PRIMAERDIAGNOSE = createField(FIELDS, 11, "PRIMAERDIAGNOSE", Type.STRING);
    //final public static Field PRIMAERDIAGNOSE_LOKALISATION = createField(FIELDS, 12, "PRIMAERDIAGNOSE_LOKALISATION", Type.STRING);
    //final public static Field SEKUNDAERDIAGNOSE = createField(FIELDS, 13, "SEKUNDAERDIAGNOSE", Type.STRING);
    //final public static Field SEKUNDAERDIAGNOSE_LOKALISATION = createField(FIELDS, 14, "SEKUNDAERDIAGNOSE_LOKALISATION", Type.STRING);
//    private static final Logger LOG = Logger.getLogger(DepartmentLine.class.getName());

    public static DepartmentLine unserialize(final String pCsvData) {
        return (DepartmentLine) AbstractLine.unserialize(new DepartmentLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
