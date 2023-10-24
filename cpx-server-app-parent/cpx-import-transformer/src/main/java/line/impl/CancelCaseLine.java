/*
 * Copyright (c) 2021 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2021  gerschmann - initial API and implementation and/or initial documentation
 */
package line.impl;

import java.util.Set;
import java.util.TreeSet;
import line.AbstractLine;
import line.Field;



/**
 *
 * @author gerschmann
 */
public class CancelCaseLine extends AbstractLine{
    protected static final Set<Field> FIELDS = new TreeSet<>();
    public static final Field CANCEL_REASON = createField(FIELDS, 7, "CANCEL_REASON", Field.Type.STRING);


    public static CancelCaseLine unserialize(final String pCsvData) {
        return (CancelCaseLine) AbstractLine.unserialize(new CancelCaseLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

    
}
