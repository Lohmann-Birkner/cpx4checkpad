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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.Date;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * TCaseSupplFee initially generated at 21.01.2016 17:07:59 by Hibernate Tools
 * 3.2.2.GA
 * <p style="font-size:1em; color:green;">Die Tabelle "T_CASE_SUPPL_FEE" enthält
 * die Berechnung der Zusatzentgelten </p>
 */
@Entity
@Table(name = "T_CASE_SUPPL_FEE",
        indexes = {
            @Index(name = "IDX_SUPPL_FEE_CASE_OPS_GROUPED", columnList = "T_CASE_OPS_GROUPED_ID")})
@SuppressWarnings("serial")
public class TCaseSupplFee extends AbstractEntity {

    private static final long serialVersionUID = 1L;
    /*   @Override
  public int hashCode() {
  int hash = 5;
  hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
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
  final TCaseSupplFee other = (TCaseSupplFee) obj;
  if (this.id != other.id) {
  return false;
  }
  return true;
  }
     */

//  private long id;
    private TCaseOpsGrouped caseOpsGrouped;
    private String cSuplFeeCode;
    private SupplFeeTypeEn cSuplTypeEn;
    private Integer csuplCount;
    private Date csuplFrom;
    private Date csuplTo;
    private double csuplValue = 0;
    private double csuplCwValue;

    /**
     * Gibt Verweis auf die Tabelle T_CASE_OPS_GROUPED zurück, zu dem der
     * Zusatz- oder Tagesentgelt ermittelt wurde .
     *
     * @return caseOpsGrouped
     */
    //@OneToOne(fetch = FetchType.LAZY)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_OPS_GROUPED_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_SUPPL_FEE_CASE_OPS_GROUPED"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    public TCaseOpsGrouped getCaseOpsGrouped() {
        return this.caseOpsGrouped;
    }

    /**
     * Gibt Anzahl des Zusatzentgeltes zurück.
     *
     * @return csuplCount
     */
    @Column(name = "COUNT", precision = 5, scale = 0, nullable = false)
    public Integer getCsuplCount() {
        return this.csuplCount;
    }

    /**
     *
     * @return csuplFrom: Abgerechnet ab.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCOUNTED_FROM", length = 7)
    public Date getCsuplFrom() {
        return csuplFrom == null ? null : new Date(csuplFrom.getTime());
    }

    /**
     *
     * @return cSuplFeeCode: code des Zusatzentgeltes oder Tagesentreltes .
     */
    @Column(name = "SUPPL_FEE_CODE", nullable = false, scale = 0)
    public String getCsuplfeeCode() {
        return this.cSuplFeeCode;
    }

    /**
     * Gibt Art des Zusatzentgeltes 1-3 zurück. z.b. ZE, ZP, ET .
     *
     * @return cSuplTypeEn
     */
    @Column(name = "SUPPL_FEE_TYPE_EN", nullable = false)
    @Enumerated(EnumType.STRING)
    public SupplFeeTypeEn getCsuplTypeEn() {
        return this.cSuplTypeEn;
    }

    /**
     *
     * @return csuplTo: Abgerechnet bis .
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCOUNTED_TO", length = 7)
    public Date getCsuplTo() {
        return csuplTo == null ? null : new Date(csuplTo.getTime());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_SUPPL_FEE_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     * Gibt Eurowert zu Zusatzentgelt zurück.
     *
     * @return csuplValue
     */
    @Column(name = "VALUE", precision = 10, scale = 2, nullable = false)
    public double getCsuplValue() {
        return csuplValue;
    }

    /**
     * Gibt Ermittelte CW zurück, d.h. Wert für Tagesentgelte
     *
     * @return csuplCwValue
     */
    @Column(name = "CW_VALUE", precision = 10, scale = 4)
    public double getCsuplCwValue() {
        return csuplCwValue;
    }

    /**
     *
     * @param caseOpsGrouped Column T_CASE_OPS_GROUPED_ID: Verweis auf die
     * Tabelle T_CASE_OPS_GROUPED, zu dem der Zusatz- oder Tagesentgelt
     * ermittelt wurde .
     */
    public void setCaseOpsGrouped(final TCaseOpsGrouped caseOpsGrouped) {
        this.caseOpsGrouped = caseOpsGrouped;
    }

    /**
     *
     * @param csuplCount Column COUNT: Anzahl des Zusatzentgeltes .
     */
    public void setCsuplCount(final Integer csuplCount) {
        this.csuplCount = csuplCount;
    }

    /**
     *
     * @param csuplFrom Column CSUPL_FROM: Abgerechnet ab.
     */
    public void setCsuplFrom(final Date csuplFrom) {
        this.csuplFrom = csuplFrom == null ? null : new Date(csuplFrom.getTime());
    }

    public void setCsuplfeeCode(final String csuplSuplfeeCode) {
        this.cSuplFeeCode = csuplSuplfeeCode;
    }

    /**
     *
     * @param csuplSuplTypeEn Column CSUPL_TYPE_EN: Art des Zusatzentgeltes
     * 1-3(ZE, ZP, ET) .
     */
    public void setCsuplTypeEn(final SupplFeeTypeEn csuplSuplTypeEn) {
        this.cSuplTypeEn = csuplSuplTypeEn;
    }

    /**
     *
     * @param csuplTo Column CSUPL_TO: Abgerechnet bis .
     */
    public void setCsuplTo(final Date csuplTo) {
        this.csuplTo = csuplTo == null ? null : new Date(csuplTo.getTime());
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     *
     * @param csuplValue Column CSUPL_VALUE: Eurowert zu Zusatzentgelt .
     */
    public void setCsuplValue(double csuplValue) {
        this.csuplValue = csuplValue;
    }

    /**
     *
     * @param csuplCwValue Column CSUPL_CW_VALUE: Ermittelte CW - Wert für
     * Tagesentgelte .
     */
    public void setCsuplCwValue(double csuplCwValue) {
        this.csuplCwValue = csuplCwValue;
    }

}
