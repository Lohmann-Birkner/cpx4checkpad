/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.information;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxPersistenceUnit implements Serializable, Comparable<CpxPersistenceUnit> {

    private static final long serialVersionUID = 1L;

    public final String persistenceUnit;
    public final DatabaseInfo databaseInfo;

    public CpxPersistenceUnit(final String pPersistenceUnit, final DatabaseInfo pDatabaseInfo) {
        this.persistenceUnit = (pPersistenceUnit == null) ? "" : pPersistenceUnit.trim();
        this.databaseInfo = pDatabaseInfo;
    }

    public DatabaseInfo getDatabaseInfo() {
        return databaseInfo;
    }

    public String getPersistenceUnit() {
        return this.persistenceUnit;
    }

    @Override
    public String toString() {
        return persistenceUnit;
    }

    @Override
    public int compareTo(CpxPersistenceUnit o) {
        return this.getPersistenceUnit().compareToIgnoreCase(o.getPersistenceUnit());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        return compareTo((CpxPersistenceUnit) obj) == 0;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.persistenceUnit);
        return hash;
    }

}
