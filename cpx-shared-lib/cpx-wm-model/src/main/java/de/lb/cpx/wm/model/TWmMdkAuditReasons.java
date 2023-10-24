/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model;

import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 *
 * @author Husser
 */
@Entity
@Table(name = "T_WM_AUDIT_REASONS",
        indexes = {
            @Index(name = "IDX_T_WM_AUDIT_REASONS4REQ_ID", columnList = "T_WM_REQUEST_ID", unique = false),
            @Index(name = "IDX_T_WM_AUDIT_REASONS4PROC_ID", columnList = "T_WM_PROCESS_FINAL_ID", unique = false)})
//@Entity
//@DiscriminatorValue("2") //enum value in WmRequestType
//@Table(name = "T_WM_REQUEST")
public class TWmMdkAuditReasons extends AbstractVersionEntity {

    private static final long serialVersionUID = 1L;

    private TWmRequest request;
    private TWmProcessHospitalFinalisation process;
    private long auditReasonNumber;
    private boolean extended;

//    public abstract void setRequests(Set<TWmRequest> requests);
//    public abstract Set<TWmRequest> getRequests();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_WM_AUDIT_REASONS_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean versionEquals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof TWmRequest)) {
            return false;
        }
        final TWmMdkAuditReasons other = (TWmMdkAuditReasons) object;
        if (!Objects.equals(this.auditReasonNumber, other.auditReasonNumber)) {
            return false;
        }
        if (!Objects.equals(this.request, other.request)) {
            return false;
        }
        if (!Objects.equals(this.process, other.process)) {
            return false;
        }
        /*
        if (!Objects.equals(this.requestType, other.requestType)) {
            return false;
        }
         */
        return true;
    }

    /**
     * @return the request
     */
    @ManyToOne
    @JoinColumn(name = "T_WM_REQUEST_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_AUDIT_REASON4REQUEST_ID"))
    public TWmRequest getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(TWmRequest request) {
        this.request = request;
    }

    /**
     * @return the process
     */
    @ManyToOne
    @JoinColumn(name = "T_WM_PROCESS_FINAL_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_AUDIT_REASON4PROCESS_ID"))
    public TWmProcessHospitalFinalisation getProcess() {
        return process;
    }

    /**
     * @param process the process to set
     */
    public void setProcess(TWmProcessHospitalFinalisation process) {
        this.process = process;
    }

    /**
     * @return the auditReasonId
     */
    @Column(name = "AUDIT_REASON_NUMBER", nullable = false)
    public long getAuditReasonNumber() {
        return auditReasonNumber;
    }

    /**
     * @param auditReasonNumber the auditReasonId to set
     */
    public void setAuditReasonNumber(long auditReasonNumber) {
        this.auditReasonNumber = auditReasonNumber;
    }

    /**
     * Gibt MainProcessCase true=1,False=0 zurück.
     *
     * @return mainCase
     */
    @Column(name = "EXTENDED", nullable = false)
    public boolean getExtended() {
        return extended;
    }

    /**
     * @param extended Column EXTENDED: EXTENDED true=1,False=0.
     */
    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.request);
        hash = 97 * hash + (int) (this.auditReasonNumber ^ (this.auditReasonNumber >>> 32));
        hash = 97 * hash + (this.extended ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TWmMdkAuditReasons other = (TWmMdkAuditReasons) obj;
        if (this.auditReasonNumber != other.auditReasonNumber) {
            return false;
        }
        if (this.extended != other.extended) {
            return false;
        }
        if (!Objects.equals(this.request, other.request)) {
            return false;
        }
        return true;
    }

}
