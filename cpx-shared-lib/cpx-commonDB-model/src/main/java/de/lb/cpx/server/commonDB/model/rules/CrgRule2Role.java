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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.model.rules;

import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * CrgRule2Role initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">CRG_RULE_2_ROLE : Regel - Rollen
 * Zuweisungen </p>
 */
@MappedSuperclass
public class CrgRule2Role extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    //    private long id;
    private CdbUserRoles cdbUserRoles;
    protected CrgRules crgRules;
//    private long crgrId;

    public static CrgRule2Role getTypeInstance(PoolTypeEn type) {
        switch (type) {
            case DEV:
                return new CrgRule2RoleDev();
            default:
                return new CrgRule2RoleProd();

        }
    }

    @Transient
    public static CrgRule2Role getTypeInstance() {
        return getTypeInstance(PoolTypeEn.PROD);
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return cdbUserRoles:Referenz auf die Tabelle CDB_USER_ROLES
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CDBUR_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    public CdbUserRoles getCdbUserRoles() {
        return this.cdbUserRoles;
    }

    /**
     *
     * @param cdbUserRoles Column CDBUR_ID :Referenz auf die Tabelle
     * CDB_USER_ROLES
     */
    public void setCdbUserRoles(CdbUserRoles cdbUserRoles) {
        this.cdbUserRoles = cdbUserRoles;
    }

    /**
     *
     * @param crgRules Column CRGR_ID : Referenz auf die Tabelle CRG_RULES
     */
    public void setCrgRules(CrgRules crgRules) {
        this.crgRules = crgRules;
    }

    @Transient
    public CrgRules getCrgRules() {
        return crgRules;
    }

    @Transient
    public long getId() {
        return id;
    }

//
//    @Column(name = "CRGR_ID", nullable = true)
//    public long getCrgrId() {
//        return crgrId;
//    }
//
//    public void setCrgrId(long crgrId) {
//        this.crgrId = crgrId;
//    }
//        
}
