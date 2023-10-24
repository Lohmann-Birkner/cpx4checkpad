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
package line;

import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author Dirk Niemeier
 */
public class LineEntity {

    private static final Logger LOG = Logger.getLogger(LineEntity.class.getName());
    public final Class<? extends LineI> clazz;
    public final String clazzName;
    public final String imexTmpTableName;
    public final String imexTableName;
    public final String imexTmpFileName;
    public final Set<Field> fieldSet;

    public LineEntity(final Class<? extends LineI> pClass, final String pImexTmpTableName, final String pImexTableName, final String pImexTmpFileName, final Set<Field> pFieldSet) {
        String imexTmpTableNameTmp = (pImexTmpTableName == null) ? "" : pImexTmpTableName.trim();
        String imexTableNameTmp = (pImexTableName == null) ? "" : pImexTableName.trim();
        String imexTmpFileNameTmp = (pImexTmpFileName == null) ? "" : pImexTmpFileName.trim();

        if (pClass == null) {
            throw new IllegalArgumentException("No line class given");
        }
        if (imexTmpTableNameTmp.isEmpty()) {
            throw new IllegalArgumentException("No temporary imex table name given");
        }
        if (imexTableNameTmp.isEmpty()) {
            throw new IllegalArgumentException("No imex table name given");
        }
        if (imexTmpFileNameTmp.isEmpty()) {
            throw new IllegalArgumentException("No file name given");
        }
        if (pFieldSet == null) {
            throw new IllegalArgumentException("No fileset given");
        }

        clazz = pClass;
        clazzName = pClass.getSimpleName();
        imexTmpTableName = imexTmpTableNameTmp;
        imexTableName = imexTableNameTmp;
        imexTmpFileName = imexTmpFileNameTmp;
        fieldSet = Collections.unmodifiableSet(pFieldSet);
    }

    public String getImexTmpTableName() {
        return imexTmpTableName;
    }

    public String getImexTableName() {
        return imexTableName;
    }

    public String getImexTmpFileName() {
        return imexTmpFileName;
    }

    public Class<? extends LineI> getClazz() {
        return clazz;
    }

    public String getClazzName() {
        return clazzName;
    }

    public Set<Field> getFieldSet() {
        //return new TreeSet<>(fieldSet);
        return fieldSet;
    }

}
