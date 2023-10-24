/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * <p style="font-size:1em; color:green;"> Die Tabelle "T_ROLE_2_CHECK" ist
 * Mapping der Regelanschlägen zu Rollen </p>
 *
 * @author gerschmann
 */
@Entity
@Table(name = "T_ROLE_2_CHECK",
        indexes = {
            @Index(name = "IDX_ROLE2CHECK4T_CHECK_RES_ID", columnList = "T_CHECK_RESULT_ID", unique = false)})
@SuppressWarnings("serial")
public class TRole2Check extends AbstractEntity {
//    private long id;

    private static final long serialVersionUID = 1L;

    private TCheckResult checkResult;
    private long roleId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_ROLE_2_CHECK_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gibt Verweis auf die Tablle T_CHECK_RESULT zurück.
     *
     * @return checkResult
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "T_CHECK_RESULT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_ROLE2CHECK4T_CHECK_RES_ID"))
    public TCheckResult getCheckResult() {
        return checkResult;
    }

    /**
     *
     * @param chkResult Column T_CHECK_RESULT_ID: Verweis auf die Tablle
     * T_CHECK_RESULT.
     */
    public void setCheckResult(TCheckResult chkResult) {
        this.checkResult = chkResult;
    }

    /**
     * Gibt Verweis auf die Tablle cpx_common.CDB_USER_ROLES zurück.
     *
     * @return roleId
     */
    @Column(name = "USER_ROLE_ID", precision = 19, scale = 0, nullable = false)
    public long getRoleId() {
        return roleId;
    }

    /**
     *
     * @param roleId Column USER_ROLE_ID: Verweis auf die Tablle
     * cpx_common.CDB_USER_ROLES .
     */
    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

}
