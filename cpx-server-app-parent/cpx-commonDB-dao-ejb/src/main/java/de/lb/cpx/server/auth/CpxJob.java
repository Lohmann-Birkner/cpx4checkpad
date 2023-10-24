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
package de.lb.cpx.server.auth;

import de.checkpoint.enums.AppTypeEn;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.util.Objects;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxJob extends CpxAbstractClient {

    private final CpxUser mCpxUser;

    public CpxJob(final String pClientId, final String pDatabase, final CpxUser pCpxUser) {
        mClientId = pClientId;
        mDatabase = (pDatabase == null) ? "" : pDatabase.trim();
        mCpxUser = pCpxUser;
    }

    public String getDatabase() {
        return mDatabase;
    }

    public CpxUser getCpxUser() {
        return mCpxUser;
    }

    public String getCpxClientId() {
        if (mCpxUser == null) {
            return "";
        }
        return mCpxUser.getClientId();
    }

    @Override
    public long getUserId() {
        if (mCpxUser == null) {
            return 0L;
        }
        return mCpxUser.getUserId();
    }

    @Override
    public String getUserName() {
        if (mCpxUser == null) {
            return "";
        }
        return mCpxUser.getUserName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        //if (!(obj instanceof CpxJob)) {
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        return this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.mClientId);
        return hash;
    }

    @Override
    public boolean isJob() {
        return true;
    }

    @Override
    public String getActualDatabase() {
        return getDatabase();
    }

    @Override
    public long getActualRoleId() {
        return 0L;
    }

    @Override
    public String getActualRoleName() {
        return "";
    }

    @Override
    public AppTypeEn getAppType() {
        return null;
    }

    @Override
    public CpxSystemPropertiesInterface getCpxSystemProperties() {
        return null;
    }

}
