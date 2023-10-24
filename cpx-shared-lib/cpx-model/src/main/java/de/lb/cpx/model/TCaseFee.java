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
package de.lb.cpx.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.lb.cpx.server.commons.dao.AbstractCaseEntity;
import java.util.Date;
import java.util.Objects;
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
import org.hibernate.annotations.Type;

/**
 * TCaseFee initially generated at 21.01.2016 17:07:59 by Hibernate Tools
 * 3.2.2.GA
 * <p style="font-size:1em; color:green;">Die Tabelle "T_CASE_FEE" ist Tabelle
 * der Entgelten.</p>
 */
@Entity
@Table(name = "T_CASE_FEE",
        indexes = {
            @Index(name = "IDX_CASE_FEE4TCASE_DETAIL_ID", columnList = "T_CASE_DETAILS_ID", unique = false),
            @Index(name = "IDX_CASE_FEE4T_CASE_BILL_ID", columnList = "T_CASE_BILL_ID", unique = false)})
@SuppressWarnings("serial")
public class TCaseFee extends AbstractCaseEntity {

    private static final long serialVersionUID = 1L;

//  private long id;
    private TCaseBill caseBill;
    private TCaseDetails caseDetails;
    private String feecFeekey;
    private double feecValue;
    private int feecCount;
    private Date feecFrom;
    private Date feecTo;
    private int feecUnbilledDays;
    private boolean feecIsBillFl;
    private String feecInsurance;

    public TCaseFee() {
        setIgnoreFields();
    }
    
    public TCaseFee(Long pCurrentUserId){
        super(pCurrentUserId);
        setIgnoreFields();
    }
    @Override
    protected final void setIgnoreFields() {
         ignoredFields = new String[]{"caseDetails", "caseBill"};
    }


    /**
     * Gibt Verweis auf die Tabelle T_CASE_BILL zurück.
     *
     * @return caseBill
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "T_CASE_BILL_ID", foreignKey = @ForeignKey(name = "FK_CASE_FEE4T_CASE_BILL_ID"))
    public TCaseBill getCaseBill() {
        return this.caseBill;
    }

    /**
     * Gibt Verweis auf die Tabelle T_CASE_DETAILS zurück.
     *
     * @return caseDetails
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "T_CASE_DETAILS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CASE_FEE4T_CASE_DETAILS_ID"))
    @JsonBackReference(value = "fee")
    public TCaseDetails getCaseDetails() {
        return this.caseDetails;
    }

    /**
     * Gibt Entgeltanzahl zurück.
     *
     * @return feecCount
     */
    @Column(name = "FEEC_COUNT", precision = 3, scale = 0, nullable = false)
    public int getFeecCount() {
        return this.feecCount;
    }

    /**
     * Gibt Entgeltschlüssel zurück.
     *
     * @return feecFeekey
     */
    @Column(name = "FEEC_FEEKEY", nullable = false, length = 8)
    public String getFeecFeekey() {
        return this.feecFeekey;
    }

    /**
     * @return insurance of the fee
     */
    @Column(name = "FEEC_INSURANCE", length = 12)
    public String getFeecInsurance() {
        return this.feecInsurance;
    }

    /**
     * Gibt Anfangsdatum der Abrechnung zurück.
     *
     * @return feecFrom
     */
    //@Temporal(TemporalType.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FEEC_FROM", length = 7)
    public Date getFeecFrom() {
        return this.feecFrom;
    }

    /**
     * Gibt Flag 0/1 zurück , Berechnet/aus der Rechnung..
     *
     * @return feecIsBillFl
     */
    @Column(name = "FEEC_IS_BILL_FL", precision = 1, scale = 0, nullable = false)
    @Type(type = "numeric_boolean")
    public boolean getFeecIsBillFl() {
        return this.feecIsBillFl;
    }

    /**
     * Gibt Enddatum der Abrechnung zurück.
     *
     * @return feecTo
     */
    //@Temporal(TemporalType.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FEEC_TO", length = 7)
    public Date getFeecTo() {
        return this.feecTo;
    }

    /**
     * Gibt Einzellwert des Entgelts zurück.
     *
     * @return feecValue
     */
    @Column(name = "FEEC_VALUE", precision = 10, scale = 2, nullable = false)
    public double getFeecValue() {
        return this.feecValue;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_FEE_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     * Gibt Tage ohne Berechnung zurück.
     *
     * @return feecUnbilledDays
     */
    @Column(name = "FEEC_UNBILLED_DAYS", precision = 3, scale = 0)
    public int getFeecUnbilledDays() {
        return this.feecUnbilledDays;
    }

    /**
     *
     * @param caseBill Column T_CASE_BILL_ID: Verweis auf die Tabelle
     * T_CASE_BILL.
     */
    public void setCaseBill(final TCaseBill caseBill) {
        this.caseBill = caseBill;
    }

    /**
     *
     * @param caseDetails Column T_CASE_DETAILS_ID :Verweis auf die Tabelle
     * T_CASE_DETAILS.
     */
    public void setCaseDetails(final TCaseDetails caseDetails) {
        this.caseDetails = caseDetails;
    }

    /**
     *
     * @param feecCount Column FEEC_COUNT: Entgeltanzahl.
     */
    public void setFeecCount(final int feecCount) {
        this.feecCount = feecCount;
    }

    /**
     *
     * @param feecFeekey Column FEEC_FEEKEY: Entgeltschlüssel .
     */
    public void setFeecFeekey(final String feecFeekey) {
        this.feecFeekey = feecFeekey;
    }

    /**
     *
     * @param feecInsurance Column FEEC_INSURANCE , sets new insurance value to
     * the fee .
     */
    public void setFeecInsurance(final String feecInsurance) {
        this.feecInsurance = feecInsurance;
    }

    /**
     *
     * @param feecFrom Column FEEC_FROM : Abrechnung von.
     */
    public void setFeecFrom(final Date feecFrom) {
        this.feecFrom = feecFrom == null ? null : new Date(feecFrom.getTime());
    }

    /**
     *
     * @param feecIsBillFl Column FEEC_IS_BILL_FL: 0/1 Flag, Berechnet/aus der
     * Rechnung.
     */
    public void setFeecIsBillFl(final boolean feecIsBillFl) {
        this.feecIsBillFl = feecIsBillFl;
    }

    /**
     *
     * @param feecTo Column FEEC_TO: Abrechnung bis .
     */
    public void setFeecTo(final Date feecTo) {
        this.feecTo = feecTo == null ? null : new Date(feecTo.getTime());
    }

    /**
     *
     * @param feecValue Column FEEC_VALUE: Einzellwert des Entgelts .
     */
    public void setFeecValue(final double feecValue) {
        this.feecValue = feecValue;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setFeecUnbilledDays(int days) {
        this.feecUnbilledDays = days;
    }

    @Override
    public TCaseFee clone() throws CloneNotSupportedException {
        TCaseFee clone = new TCaseFee();
        clone.setFeecFeekey(feecFeekey);
        clone.setFeecValue(feecValue);
        clone.setFeecCount(feecCount);
        clone.setFeecFrom(feecFrom);
        clone.setFeecTo(feecTo);
        clone.setFeecIsBillFl(feecIsBillFl);
        clone.setFeecUnbilledDays(feecUnbilledDays);
        return clone;
    }

    @Override
    public boolean versionEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TCaseFee)) {
            return false;
        }

        final TCaseFee other = (TCaseFee) obj;
        if (Double.doubleToLongBits(this.feecValue) != Double.doubleToLongBits(other.getFeecValue())) {
            return false;
        }
        if (this.feecIsBillFl != other.getFeecIsBillFl()) {
            return false;
        }
        if (!Objects.equals(this.feecFeekey, other.getFeecFeekey())) {
            return false;
        }
        if (!Objects.equals(this.feecCount, other.getFeecCount())) {
            return false;
        }
        if (!Objects.equals(this.feecFrom, other.getFeecFrom())) {
            return false;
        }
        if (!Objects.equals(this.feecTo, other.getFeecTo())) {
            return false;
        }
        return true;
    }

}
