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
public class WardLine extends AbstractLine {

    private static final long serialVersionUID = 1L;

    protected static final Set<Field> FIELDS = new TreeSet<>();
    public static final Field CODE = createField(FIELDS, 7, "CODE", Type.STRING);
    public static final Field DEP_NR = createField(FIELDS, 8, "DEP_NR", Type.STRING);
    public static final Field VERLEGUNGSDATUM = createField(FIELDS, 9, "VERLEGUNGSDATUM", Type.DATETIME);
    public static final Field ENTLASSUNGSDATUM = createField(FIELDS, 10, "ENTLASSUNGSDATUM", Type.DATETIME);
    //final public static Field ERBRINGUNGSART = createField(FIELDS, 12, "ERBRINGUNGSART", Type.STRING);
    //final public static Field PRIMAERDIAGNOSE = createField(FIELDS, 11, "PRIMAERDIAGNOSE", Type.STRING);
    //final public static Field PRIMAERDIAGNOSE_LOKALISATION = createField(FIELDS, 12, "PRIMAERDIAGNOSE_LOKALISATION", Type.STRING);
    //final public static Field SEKUNDAERDIAGNOSE = createField(FIELDS, 13, "SEKUNDAERDIAGNOSE", Type.STRING);
    //final public static Field SEKUNDAERDIAGNOSE_LOKALISATION = createField(FIELDS, 14, "SEKUNDAERDIAGNOSE_LOKALISATION", Type.STRING);
    private static final Logger LOG = Logger.getLogger(WardLine.class.getName());

    public static WardLine unserialize(final String pCsvData) {
        return (WardLine) AbstractLine.unserialize(new WardLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
