/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto;

import de.checkpoint.enums.AppTypeEn;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author niemeier
 */
public class CpxSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String clientId;
    private final Long userId;
    private final String userName;
    private final String actualDatabase;
    private final Long actualRoleId;
    private final String actualRoleName;
    private final Date loginDate;
    private final Date lastActionAt;
    private final AppTypeEn appType;
    private final CpxSystemPropertiesInterface cpxSystemProperties;
    private final boolean isJob;

    public CpxSession(final String pClientId,
            final boolean pIsJob,
            final Long pUserId,
            final String pUserName,
            final String pActualDatabase,
            final Long pActualRoleId,
            final String pActualRoleName,
            final Date pLoginDate,
            final Date pLastActionAt,
            final AppTypeEn pAppType,
            final CpxSystemPropertiesInterface pCpxSystemProperties) {
        this.clientId = pClientId == null ? "" : pClientId.trim();
        this.isJob = pIsJob;
        this.userId = (pUserId == null || pUserId.equals(0L)) ? null : pUserId;
        this.userName = pUserName == null ? "" : pUserName.trim();
        this.actualDatabase = pActualDatabase == null ? "" : pActualDatabase.trim();
        this.actualRoleId = (pActualRoleId == null || pActualRoleId.equals(0L)) ? null : pActualRoleId;
        this.actualRoleName = pActualRoleName == null ? "" : pActualRoleName.trim();
        this.loginDate = pLoginDate == null ? null : new Date(pLoginDate.getTime());
        this.lastActionAt = pLastActionAt == null ? null : new Date(pLastActionAt.getTime());
        this.appType = pAppType;
        this.cpxSystemProperties = pCpxSystemProperties;
    }

    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the actualDatabase
     */
    public String getActualDatabase() {
        return actualDatabase;
    }

    /**
     * @return the actualRoleId
     */
    public Long getActualRoleId() {
        return actualRoleId;
    }

    /**
     * @return the actualRoleName
     */
    public String getActualRoleName() {
        return actualRoleName;
    }

    /**
     * @return the loginDate
     */
    public Date getLoginDate() {
        return loginDate == null ? null : new Date(loginDate.getTime());
    }

    /**
     * @return the lastActionAt
     */
    public Date getLastActionAt() {
        return lastActionAt == null ? null : new Date(lastActionAt.getTime());
    }

    /**
     * @return the appType
     */
    public AppTypeEn getAppType() {
        return appType;
    }

    /**
     * @return the cpxSystemProperties
     */
    public CpxSystemPropertiesInterface getCpxSystemProperties() {
        return cpxSystemProperties;
    }

    /**
     * @return the isJob
     */
    public boolean isJob() {
        return isJob;
    }

}
