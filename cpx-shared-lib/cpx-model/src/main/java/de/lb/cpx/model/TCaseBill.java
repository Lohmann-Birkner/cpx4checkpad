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
import de.lb.cpx.model.enums.BillTypeEn;
import de.lb.cpx.model.util.ModelUtil;
import de.lb.cpx.server.commons.dao.AbstractCaseEntity;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * TCaseBill initially generated at 21.01.2016 17:07:59 by Hibernate Tools
 * 3.2.2.GA
 * <p style="font-size:1em; color:green;">"T_CASE_BILL" ist Tabelle der
 * Berechnung des Entgeltes  </p>
 */
@Entity
@Table(name = "T_CASE_BILL",
        indexes = {
            @Index(name = "IDX_CASE_BILL4TCASE_DETAIL_ID", columnList = "T_CASE_DETAILS_ID", unique = false)})
@SuppressWarnings("serial")
public class TCaseBill extends AbstractCaseEntity {

    private static final long serialVersionUID = 1L;

//    private long id;
    private TCaseDetails caseDetails;
    private String billcNumber;
    private String billcType;
    private Date billcFrom;
    private Date billcTo;
    private BillTypeEn billcTypeEn= BillTypeEn.finalBill; // TODO Welcher Enum?
    private Set<TCaseFee> caseFees = new HashSet<>(0);
    private String billComment;
    public TCaseBill() {
       setIgnoreFields();

    }
    
    public TCaseBill(Long pCurrentUserId){
        super(pCurrentUserId);
        setIgnoreFields();
    }
    @Override
    protected final void setIgnoreFields() {
         ignoredFields = new String[]{"caseDetails"};
    }

    /**
     * Gibt Anfangsdatum der Abrechnung zurück.
     *
     * @return billcFrom
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BILLC_FROM", length = 7)
    public Date getBillcFrom() {
        return this.billcFrom;
    }

    /**
     * Gibt Rechnungsnummer zurück.
     *
     * @return billcNumber
     */
    @Column(name = "BILLC_NUMBER", length = 20)
    public String getBillcNumber() {
        return this.billcNumber;
    }

    /**
     * Gibt enddatum der Abrechnung zurück.
     *
     * @return billcTo
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BILLC_TO", length = 7)
    public Date getBillcTo() {
        return this.billcTo;
    }

    /**
     * Gibt Rechnungstyp zurück.
     *
     * @return billcType
     */
    @Column(name = "BILLC_TYPE", length = 2)
    public String getBillcType() {
        return this.billcType;
    }

    /**
     * Gibt Enumeration für Rechnungstyp zurück.
     *
     * @return billcTypeEn
     */
    @Column(name = "BILLC_TYPE_EN", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    public BillTypeEn getBillcTypeEn() {
        return this.billcTypeEn;
    }

    @Column (name = "BILLC_COMMENT", length = 255, nullable = true)
    public String getBillComment() {
        return billComment;
    }

    public void setBillComment(String billComment) {
        this.billComment = billComment;
    }

    /**
     * Gibt Verweis auf die Tabelle T_CASE_DETAILS zurück.
     *
     * @return caseDetails
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_CASE_DETAILS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CASE_BILL4T_CASE_DETAILS_ID"))
    @JsonBackReference(value = "bill")
    public TCaseDetails getCaseDetails() {
        return this.caseDetails;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "caseBill")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<TCaseFee> getCaseFees() {
        return this.caseFees;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_CASE_BILL_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    /**
     *
     * @param billcFrom Column BILLC_FROM: Abrechnung ab .
     */
    public void setBillcFrom(final Date billcFrom) {
        this.billcFrom = billcFrom == null ? null : new Date(billcFrom.getTime());
    }

    /**
     *
     * @param billcNumber Column BILLC_NUMBER: Rechnungsnummer .
     */
    public void setBillcNumber(final String billcNumber) {
        this.billcNumber = billcNumber;
    }

    /**
     *
     * @param billcTo Column BILLC_TO: Abrechnung bis .
     */
    public void setBillcTo(final Date billcTo) {
        this.billcTo = billcTo == null ? null : new Date(billcTo.getTime());
    }

    /**
     *
     * @param billcType Column BILLC_TYPE: Rechnungstyp .
     */
    public void setBillcType(final String billcType) {
        this.billcType = billcType;
    }

    /**
     *
     * @param billcTypeEn Column BILLC_TYPE_EN: Enumeration für Rechnungstyp .
     */
    public void setBillcTypeEn(final BillTypeEn billcTypeEn) {
        this.billcTypeEn = billcTypeEn;
    }

    /**
     *
     * @param caseDetails Column T_CASE_DETAILS_ID: Verweis auf die Tabelle
     * T_CASE_DETAILS .
     */
    public void setCaseDetails(final TCaseDetails caseDetails) {
        this.caseDetails = caseDetails;
    }

    public void setCaseFees(final Set<TCaseFee> caseFees) {
        this.caseFees = caseFees;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public boolean versionEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TCaseBill)) {
            return false;
        }

        final TCaseBill other = (TCaseBill) obj;
        if (!Objects.equals(this.billcNumber, other.getBillcNumber())) {
            return false;
        }
        if (!Objects.equals(this.billcType, other.getBillcType())) {
            return false;
        }
        if (!Objects.equals(this.billcTypeEn, other.getBillcTypeEn())) {
            return false;
        }
        if (!Objects.equals(this.billcFrom, other.getBillcFrom())) {
            return false;
        }
        if (!Objects.equals(this.billcTo, other.getBillcTo())) {
            return false;
        }
        if (!ModelUtil.versionSetEquals(this.caseFees, other.getCaseFees())) {
            return false;
        }
        return true;
    }

    /**
     * Clones this object
     *
     * @return Clone of TCaseBill
     * @throws CloneNotSupportedException cloning exception
     */
    @Override
    public TCaseBill clone() throws CloneNotSupportedException {
        TCaseBill clone = new TCaseBill();
        clone.setBillcNumber(billcNumber);
        clone.setBillcType(billcType);
        clone.setBillcFrom(billcFrom);
        clone.setBillcTo(billcTo);
        clone.setBillcTypeEn(billcTypeEn); // TODO Welcher Enum?
// fees
        Set<TCaseFee> cloneFees = new HashSet<>(0);
        for (TCaseFee fee : caseFees) {
            TCaseFee cloneFee = fee.clone();
            cloneFee.setCaseDetails(null);
            cloneFee.setCaseBill(clone);
            cloneFees.add(fee);
        }
        clone.setCaseFees(cloneFees);

        return clone;
    }


}
