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
 *    2016  Husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model;

import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * <p style="font-size:1em; color:green;"> Tabelle der Action. </p>
 *
 * @author Husser
 */
@Entity
@Table(name = "T_WM_ACTION",
        indexes = {
            @Index(name = "IDX_WM_ACTION4PROCESS_ID", columnList = "T_WM_PROCESS_ID", unique = false)})
public class TWmAction extends AbstractVersionEntity {

    private static final long serialVersionUID = 1L;

//    private TWmActionType actionType;
    private long actionType;

    private char[] comment;

    private TWmProcess process;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_WM_ACTION_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     * Gibt Verweis auf T_WM_ACTION_TYPE zurück.
     *
     * @return actionType
     */
//    @OneToOne
//    @OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
//    @OneToOne(cascade=CascadeType.ALL)
    @Column(name = "ACTION_SUBJECT_ID", nullable = false)
    public long getActionType() {
        return actionType;
    }

    /**
     * Gibt Text der in diesem Eintrag steht zurück.
     *
     * @return comment
     */
    @Lob
    @Column(name = "ACTION_COMMENT", length = 6000)
    @Basic(fetch = FetchType.LAZY)
    public char[] getComment() {
        return comment == null ? null : comment.clone();
    }

    /**
     * Gibt Verweis auf T_WM_PROCESS zurück.
     *
     * @return process
     */
    @ManyToOne
    @JoinColumn(name = "T_WM_PROCESS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_WM_ACTION4PROCESS_ID"))
    public TWmProcess getProcess() {
        return process;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @param actionType Column ACTION_TYPE_ID : Verweis auf T_WM_ACTION_TYPE .
     */
//    public void setActionType(TWmActionType actionType) {
//        this.actionType = actionType;
//    }
    public void setActionType(long actionType) {
        this.actionType = actionType;
    }

    /**
     *
     * @param comment Column ACTION_COMMENT: Text der in diesem Eintrag steht.
     */
    public void setComment(char[] comment) {
        this.comment = comment == null ? null : comment.clone();
    }

    /**
     *
     * @param process Column T_WM_PROCESS_ID :Verweis auf T_WM_PROCESS.
     */
    public void setProcess(TWmProcess process) {
        this.process = process;
    }

    @Override
    public boolean versionEquals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof TWmAction)) {
            return false;
        }
        final TWmAction other = (TWmAction) object;
        if (!Objects.equals(this.actionType, other.actionType)) {
            return false;
        }
        if (!Objects.equals(this.comment, other.comment)) {
            return false;
        }
        if (!Objects.equals(this.process, other.process)) {
            return false;
        }
        return true;
    }

}
