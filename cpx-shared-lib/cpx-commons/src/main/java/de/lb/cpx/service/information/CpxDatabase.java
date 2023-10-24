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

import de.lb.cpx.server.commons.enums.DbDriverEn;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxDatabase implements Serializable, Comparable<CpxDatabase> {

    private static final long serialVersionUID = 1L;

    public final String persistenceUnit;
    public final String name;
    public final Integer caseCount;
    public final Integer processCount;
    public final String url;
    public boolean selected = false;
    public boolean unknown = false;

    public CpxDatabase(final String pPersistenceUnit, final String pName, final Integer pCaseCount, final Integer pProcessCount, final String pUrl) {
        this.persistenceUnit = (pPersistenceUnit == null) ? "" : pPersistenceUnit.trim();
        this.name = (pName == null) ? "" : pName.trim();
        this.caseCount = pCaseCount;
        this.processCount = pProcessCount;
        this.url = (pUrl == null) ? "" : pUrl.trim();
    }

    public String getPersistenceUnit() {
        return this.persistenceUnit;
    }

    public String getName() {
        return this.name;
    }

    public Integer getCaseCount() {
        return this.caseCount;
    }

    public Integer getProcessCount() {
        return this.processCount;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(final boolean pSelected) {
        this.selected = pSelected;
    }

    public boolean isUnknown() {
        return this.unknown;
    }

    public void setUnknown(final boolean pUnknown) {
        this.unknown = pUnknown;
    }

    public boolean isOracle() {
        return DbDriverEn.isOracle(url);
//        return AbstractDao.isOracle(getUrl());
    }

    public boolean isSqlSrv() {
        return DbDriverEn.isSqlSrv(url);
//        return AbstractDao.isSqlSrv(getUrl());
    }

    public DbDriverEn getDriver() {
        return DbDriverEn.findByConnectionUrl(url);
    }

    @Override
    public String toString() {
        if (name.isEmpty()) {
            return "";
        }
        //String str = getConnectionString();
        final DatabaseInfo dbInfo = getDatabaseInfo();
        String str = dbInfo.getIdentifier();
        String caseCountTmp = " (" + ((caseCount == null) ? "???" : caseCount) + " Fälle, " + ((processCount == null) ? "???" : processCount) + " Vorgänge)";
        str += caseCountTmp;
        return str;
    }

    public DatabaseInfo getDatabaseInfo() {
        return new DatabaseInfo(getConnectionString(), url, null, null);
    }

    public String getConnectionString() {
        return getCommonConnectionString() + getName();
    }

    public String getCommonConnectionString() {
        return getPersistenceUnit() + ":";
    }

    @Override
    public int compareTo(CpxDatabase o) {
        return this.getConnectionString().compareToIgnoreCase(o.getConnectionString());
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
        return compareTo((CpxDatabase) obj) == 0;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.persistenceUnit);
        hash = 43 * hash + Objects.hashCode(this.name);
        return hash;
    }

    public static CpxDatabase getCpxDatabaseFromString(final String pConnectionString) {
        return getCpxDatabaseFromString(pConnectionString, "");
    }

    public static CpxDatabase getCpxDatabaseFromString(final String pConnectionString, final String pDefaultPersistenceUnit) {
        final String connString = pConnectionString == null ? "" : pConnectionString.trim();
        if (connString.isEmpty()) {
            return null;
        }
        String persistenceUnit = pDefaultPersistenceUnit == null ? "" : pDefaultPersistenceUnit.trim().toLowerCase();
        String database = connString;
        //String connUrl = "";
        if (connString.contains(":")) {
            String[] tmp = connString.split(":");
            if (tmp.length >= 1) {
                persistenceUnit = tmp[0].trim();
            }
            if (tmp.length >= 1) {
                database = tmp[1].trim();
            }
        }
        return new CpxDatabase(persistenceUnit, database, null, null, connString);
    }

}
