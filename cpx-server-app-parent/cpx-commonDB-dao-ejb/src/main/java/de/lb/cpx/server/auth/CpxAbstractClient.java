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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Dirk Niemeier
 */
public abstract class CpxAbstractClient {

    protected String mClientId = "";
    protected Calendar mLoginDate = null;
    protected Calendar mLastActionAt = null;
    protected String mDatabase = "";

    public void updateLastActionAt() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        mLastActionAt = cal;
        //return getLastActionAt();
    }

    public Calendar getLastActionAt() {
        return mLastActionAt;
    }

    public Calendar getLoginDate() {
        return mLoginDate;
    }

    public String getClientId() {
        return mClientId;
    }

    public abstract boolean isJob();

    public abstract long getUserId();

    public abstract String getUserName();

    public abstract String getActualDatabase();

    public abstract long getActualRoleId();

    public abstract String getActualRoleName();

    public abstract AppTypeEn getAppType();

    public abstract CpxSystemPropertiesInterface getCpxSystemProperties();

}
