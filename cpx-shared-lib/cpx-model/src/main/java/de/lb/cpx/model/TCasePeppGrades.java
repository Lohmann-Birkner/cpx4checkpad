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
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author gerschmann
 */
@Entity
@Table(name = "T_CASE_PEPP_GRADES", indexes = {
    @Index(name = "IDX_CASE4PEPP_ID", columnList = "T_CASE_PEPP_ID", unique = false)})
//@SuppressWarnings("serial")
public class TCasePeppGrades extends AbstractEntity implements Comparable<TCasePeppGrades> {

    private static final long serialVersionUID = 1L;
    /*    @Override
  public int hashCode() {
  int hash = 3;
  hash = 29 * hash + Objects.hashCode(this.id);
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
  final TCasePeppGrades other = (TCasePeppGrades) obj;
  if (!Objects.equals(this.id, other.id)) {
  return false;
  }
  return true;
  }*/

//    private Long id;
    private TCasePepp casePepp;
    private int peppcgrNumber;
    private int peppcgrDays;
    private double peppcgrCw;
    private double oneFee;
    private double peppcgrBaserate;
    private Date peppcgrFrom;
    private Date peppcgrTo;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_PEPP_GRADES_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return id;
    }

    /**
     * for one pepp result cann exist many TCaseGrades items Gibt Verweis auf
     * die Tablle T_CASE_PEPP zurück.
     *
     * @return casePepp
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "T_CASE_PEPP_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_PEPP_GRADES4T_CASE_PEPP_ID"))
    public TCasePepp getCasePepp() {
        return casePepp;
    }

    /**
     * Gibt Anzahl der Tage für eine Stufe/Klasse. zurück.
     *
     * @return peppcgrDays
     */
    @Column(name = "DAYS", precision = 5, scale = 0)
    public int getPeppcgrDays() {
        return peppcgrDays;
    }

    /**
     * Gibt Groupping Nummder zurück, der 1-5 für Vergütungsstufen ,
     * Klassennummer für Vergütungsklasse ist.
     *
     * @return peppcgrNumber
     */
    @Column(name = "GRADE", precision = 5, scale = 0, nullable = false)
    public int getPeppcgrNumber() {
        return peppcgrNumber;
    }

    /**
     * Gibt CW pro Stufe/Klasse zurück.(4 nachkomma Stellen).
     *
     * @return peppcgrCw
     */
    @Column(name = "CW", precision = 10, scale = 4)
    public double getPeppcgrCw() {
        return peppcgrCw;
    }

    /**
     * Gibt Berechnete Erlös zu diesem Eintrag.
     *
     * @return oneFee
     */
    @Column(name = "ONE_FEE", precision = 10, scale = 2, nullable = true)
    public double getOneFee() {
        return oneFee;
    }

    /**
     * Gibt Baserate für die Stufe zurück.
     *
     * @return pappcgrBaserate
     */
    @Column(name = "BASERATE", precision = 10, scale = 2)
    public double getPeppcgrBaserate() {
        return peppcgrBaserate;
    }

    /**
     * Gibt Anfang der Gültigekeit zurück.
     *
     * @return peppcgrFrom
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCOUNTED_FROM")
    public Date getPeppcgrFrom() {
        return peppcgrFrom == null ? null : new Date(peppcgrFrom.getTime());
    }

    /**
     * Gibt Ende der Gültigekeit zurück.
     *
     * @return peppcgrTo
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCOUNTED_TO")
    public Date getPeppcgrTo() {
        return peppcgrTo == null ? null : new Date(peppcgrTo.getTime());
    }

    /**
     *
     * @param peppcgrDays Column DAYS:Anzahl der Tage für eine Stufe/Klasse.
     */
    public void setPeppcgrDays(int peppcgrDays) {
        this.peppcgrDays = peppcgrDays;
    }

    /**
     *
     * @param peppcgrCw Column CW:CW pro Stufe/Klasse, 4 nachkomma Stellen.
     */
    public void setPeppcgrCw(double peppcgrCw) {
        this.peppcgrCw = peppcgrCw;
    }

    /**
     *
     * @param oneFee :Berechnete Erlös zu diesem Eintrag Eurowert mit 2
     * nachkomma Stellen
     */
    public void setOneFee(double oneFee) {
        this.oneFee = oneFee;
    }

    /**
     *
     * @param peppcgrBaserate Column PEPPCGR_BASERATE: Baserate für die Stufe.
     */
    public void setPeppcgrBaserate(double peppcgrBaserate) {
        this.peppcgrBaserate = peppcgrBaserate;
    }

    /**
     *
     * @param casePepp Column T_CASE_PEPP_ID: Verweis auf die Tablle
     * T_CASE_PEPP.
     */
    public void setCasePepp(TCasePepp casePepp) {
        this.casePepp = casePepp;
    }

    /**
     *
     * @param peppcgrNumber Column GRADE:Für Vergütungsstufen 1-5,für
     * Vergütungsklasse - Klassennummer.
     */
    public void setPeppcgrNumber(int peppcgrNumber) {
        this.peppcgrNumber = peppcgrNumber;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @param peppcgrFrom Column ACCOUNTED_FROM: Anfang der Gültigekeit
     */
    public void setPeppcgrFrom(Date peppcgrFrom) {
        this.peppcgrFrom = peppcgrFrom == null ? null : new Date(peppcgrFrom.getTime());
    }

    /**
     *
     * @param peppcgrTo Column ACCOUNTED_TO: Ende der Gültigekeit
     */
    public void setPeppcgrTo(Date peppcgrTo) {
        this.peppcgrTo = peppcgrTo == null ? null : new Date(peppcgrTo.getTime());
    }

    @Override
    public int compareTo(TCasePeppGrades grade2) {
        if (getPeppcgrNumber() < grade2.getPeppcgrNumber()) {
            return -1;
        }
        if (getPeppcgrNumber() > grade2.getPeppcgrNumber()) {
            return 1;
        }
        return (getPeppcgrFrom().compareTo(grade2.getPeppcgrFrom()));
    }

}
