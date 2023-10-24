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
 *    2018  Shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import de.lb.cpx.model.enums.CommentTypeEn;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

/**
 *
 * @author Shahin
 */
@Entity
@Table(name = "T_CASE_COMMENT", indexes = {
    @Index(name = "IDX_CASE_COMMENT4T_CASE_ID", columnList = "T_CASE_ID", unique = false)})

public class TCaseComment extends AbstractVersionEntity {

    private static final long serialVersionUID = 1L;

//    private CommentType typeEn = CommentType.caseReview;//TYPE_EN VARCHAR2(20 CHAR) 
    private CommentTypeEn typeEn;
    private Long commentNumber = 0L;//COMMENT_NUMBER NUMBER(19,0)
    private char[] text; //Text CLOB
    private TCase tCaseId;//T_CASE_ID
    private boolean isActive = false;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_COMMENT_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return typeEn
     */
//    @Column(name = "TYPE_EN", nullable = false, length = 20)
    @Column(name = "TYPE_EN", nullable = false)
    @Enumerated(EnumType.ORDINAL)   // enumerated type property or field as an integer
//    @MapKeyEnumerated(EnumType.ORDINAL)
    public CommentTypeEn getTypeEn() {
        return typeEn;
    }

    /**
     *
     * @param typeEn: Typ des Kommentares - Enum (CASE_REVIEW,...)
     */
    public void setTypeEn(CommentTypeEn typeEn) {
        this.typeEn = typeEn;
    }

    /**
     *
     * @return number
     */
    @Column(name = "COMMENT_NUMBER", nullable = false)
    public long getNumber() {
        return commentNumber;
    }

    /**
     *
     * @param commentNumber : laufende Kommentar-Nummer eines Falles, mehrere
     * pro Fall möglich
     */
    public void setnumber(Long commentNumber) {
        this.commentNumber = commentNumber;
    }

    /**
     * Gibt Dokumenten-Inhalt zurück.
     *
     * @return text
     */
    @Lob
    @Column(name = "TEXT", nullable = true, length = 10000)
    @Basic(fetch = FetchType.LAZY)
    public char[] getText() {
        return text == null ? null : text.clone();
    }

    /**
     *
     * @param text Column TEXT: Text des Kommentares größere Kommentare -
     * Textgrößen bis ca. 10000 Chars
     */
    public void setText(char[] text) {
        this.text = text == null ? null : text.clone();
    }

    /**
     * Gibt Verweis auf die ID der Tabelle T_CASE zurück
     *
     * @return TCaseId
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "T_CASE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CASE_COMMENT4T_CASE_ID"))
    public TCase getTCaseId() {
        return this.tCaseId;
    }

    /**
     *
     * @param tCaseId Column T_CASE_ID:Verweis auf die ID der Tabelle T_CASE.
     */
    public void setTCaseId(final TCase tCaseId) {
        this.tCaseId = tCaseId;
    }

    @Column(name = "IS_ACTIVE_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean pIsActive) {
        isActive = pIsActive;
    }

    @Override
    public boolean versionEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TCaseComment)) {
            return false;
        }
        return true;
    }
}
