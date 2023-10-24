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
public class ConnectionString implements Comparable<ConnectionString>, Serializable {

    private static final long serialVersionUID = 1L;
    public final String connectionString;
    public final String persistenceUnit;
    public final String database;
    //final public List<ConnectionString> childrenList = new ArrayList<>();
    //public EntityManagerFactory emf;
    private ConnectionString parent;

    public ConnectionString(final String pConnectionString) {
        String connectionStringTmp = (pConnectionString == null) ? "" : pConnectionString.trim();
        String persistenceUnitTmp = "";
        String databaseTmp = "";
        if (!connectionStringTmp.contains(":")) {
            //return new String[] { "", "", "", "" };
            connectionString = pConnectionString;
            persistenceUnit = persistenceUnitTmp;
            database = databaseTmp;
            return;
        }
        String[] tmp = connectionStringTmp.split(":");
        if (tmp.length > 2) {
            connectionString = pConnectionString;
            persistenceUnit = persistenceUnitTmp;
            database = databaseTmp;
            return;
        }
        if (tmp.length > 0) {
            persistenceUnitTmp = tmp[0];
            persistenceUnitTmp = (persistenceUnitTmp == null) ? "" : persistenceUnitTmp.trim();
        }
        if (tmp.length > 1) {
            databaseTmp = tmp[1];
            databaseTmp = (databaseTmp == null) ? "" : databaseTmp.trim();
        }
        connectionString = pConnectionString;
        persistenceUnit = persistenceUnitTmp;
        database = databaseTmp;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public String getPersistenceUnit() {
        return persistenceUnit;
    }

    public String getDatabase() {
        return database;
    }

    /*
  public ConnectionString setEntityManagerFactory(final EntityManagerFactory pEmf) {
    emf = pEmf;
    return this;
  }
  
  public EntityManagerFactory getEntityManagerFactory() {
    return emf;
  }
     */
 /*
  public List<ConnectionString> getChildren() {
    return childrenList;
  }
     */
    public ConnectionString setParent(final ConnectionString pParent) {
        this.parent = pParent;
        return this;
    }

    public ConnectionString getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return getConnectionString();
    }

    @Override
    public int compareTo(ConnectionString o) {
        if (o == null) {
            return -1;
        }
        return this.getConnectionString().compareTo(o.getConnectionString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        //if (!(obj instanceof ConnectionString)) {
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        return (compareTo((ConnectionString) obj) == 0);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.connectionString);
        return hash;
    }

    /**
     * Has persistence unit and database name?
     *
     * @return success
     */
    public boolean isValidCaseDb() {
        return !persistenceUnit.isEmpty() && !database.isEmpty();
    }

    /**
     * Has persistence unit and no database name?
     *
     * @return success
     */
    public boolean isValidMasterDb() {
        return !persistenceUnit.isEmpty() && database.isEmpty();
    }

}
