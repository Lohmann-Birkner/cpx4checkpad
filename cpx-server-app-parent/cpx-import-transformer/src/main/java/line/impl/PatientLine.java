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
public class PatientLine extends AbstractLine {

    private static final long serialVersionUID = 1L;

    protected static final Set<Field> FIELDS = new TreeSet<>();
    public static final Field NACHNAME = createField(FIELDS, 7, "NACHNAME", Type.STRING, 50);
    public static final Field VORNAME = createField(FIELDS, 8, "VORNAME", Type.STRING, 50);
    public static final Field GESCHLECHT = createField(FIELDS, 9, "GESCHLECHT", Type.STRING, 1);
    public static final Field GEBURTSDATUM = createField(FIELDS, 10, "GEBURTSDATUM", Type.DATE);
    public static final Field TITEL = createField(FIELDS, 11, "TITEL", Type.STRING, 50);
    public static final Field ADRESSE = createField(FIELDS, 12, "ADRESSE", Type.STRING, 400);
    public static final Field PLZ = createField(FIELDS, 13, "PLZ", Type.STRING, 5);
    public static final Field ORT = createField(FIELDS, 14, "ORT", Type.STRING, 250);
    public static final Field BUNDESLAND = createField(FIELDS, 15, "BUNDESLAND", Type.STRING, 25);
    public static final Field LAND = createField(FIELDS, 16, "LAND", Type.STRING, 25);
    public static final Field TELEFON = createField(FIELDS, 17, "TELEFON", Type.STRING, 20);
    public static final Field MOBIL = createField(FIELDS, 18, "MOBIL", Type.STRING, 20);
    public static final Field VERSICHERTENNR = createField(FIELDS, 19, "VERSICHERTENNR", Type.STRING, 50);
    public static final Field KASSE = createField(FIELDS, 20, "KASSE", Type.STRING, 50);

    public static PatientLine unserialize(final String pCsvData) {
        return (PatientLine) AbstractLine.unserialize(new PatientLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
