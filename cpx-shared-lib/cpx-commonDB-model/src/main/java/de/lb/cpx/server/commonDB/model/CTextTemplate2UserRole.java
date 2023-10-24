/**
 * Copyright (c) 2019 Lohmann & Birkner.
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
 */
package de.lb.cpx.server.commonDB.model;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Mapping entity (table) between CTextTemplate and CdbUserRoles
 *
 * @author nandola
 */
@Entity
@Table(name = "C_TEXT_TEMPLATE_2_USER_ROLE")
@SuppressWarnings("serial")
public class CTextTemplate2UserRole extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private CTextTemplate cTextTemplate;
    private CdbUserRoles cdbUserRoles;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_TEXT_TEMPLATE_2_USER_ROLE_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return cdbUserRoles: Referenz auf die Tabelle CDB_USER_ROLES
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CDB_USER_ROLE_ID", nullable = false)
    public CdbUserRoles getCdbUserRole() {
        return this.cdbUserRoles;
    }

    /**
     *
     * @param cdbUserRoles Column CDB_USER_ROLE_ID :Referenz auf die Tabelle
     * CDB_USER_ROLES
     */
    public void setCdbUserRole(CdbUserRoles cdbUserRoles) {
        this.cdbUserRoles = cdbUserRoles;
    }

    /**
     *
     * @return cdbUsers: Referenz auf die Tabelle C_TEXT_TEMPLATE
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "C_TEXT_TEMPLATE_ID", nullable = false)
    public CTextTemplate getTextTemplate() {
        return this.cTextTemplate;
    }

    /**
     *
     * @param cTextTemplate Column C_TEXT_TEMPLATE_ID :Referenz auf die Tabelle
     * C_TEXT_TEMPLATE
     */
    public void setTextTemplate(CTextTemplate cTextTemplate) {
        this.cTextTemplate = cTextTemplate;
    }

}
