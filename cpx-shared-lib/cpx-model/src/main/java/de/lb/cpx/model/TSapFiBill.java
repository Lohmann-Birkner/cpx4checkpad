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
 *    2017  Shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.lb.cpx.model.enums.SapReferenceTypeEn;
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.DATE;
import org.hibernate.annotations.OptimisticLock;

/**
 * TSapFiBill initially generated at 19.12.2017 17:07:59 by Hibernate Tools
 * 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle TSapFiBill enthält
 * Rechnungsdaten welche über das SAP-Modul FI (Finanzwesen) abgerufen werden. .
 * </p>
 *
 * @author Shahin
 */
@Entity
@Table(name = "T_SAP_FI_BILL", indexes = {
    @Index(name = "IDX_SAP_FI_BILL4T_CASE", columnList = "T_CASE_ID ", unique = false)})
@SuppressWarnings("serial")
@Inheritance(strategy = InheritanceType.JOINED)   // if no child, no need
//@DiscriminatorColumn(name = "INVOICE", discriminatorType = DiscriminatorType.STRING)
public class TSapFiBill extends AbstractVersionEntity {

    private static final long serialVersionUID = 1L;
    private TCase tCaseId;//T_CASE_ID    NUMBER(20,0)   Not Null( mit CONSTRAINT )
    private String invoice;//INVOICE     VARCHAR2(20 BYTE)    Null
    private String invoiceType;//INVOICE_TYPE   VARCHAR2(5 BYTE)    Null
    private SapReferenceTypeEn referenceType;//REFERENCE_TYPE  VARCHAR2(10 BYTE) Null (Rechnung; Storno)
    private String referenceCurrency;//REFERENCE_CURRENCY  VARCHAR2(10 BYTE)  Null
    private Date invoiceDate;//INVOICE_DATE   DATE  Null
    private Integer fiscalYear;//FISCAL_YEAR  NUMBER(15,0) Null
    private String invoiceKind;//INVOICE_KIND  VARCHAR2(5 BYTE)  Null
    private String state;//T_STATE  VARCHAR2(5 BYTE)  Null
    private Double netValue;//NET_VALUE   FLOAT Null
    private String receiverRef;//RECEIVER_REF  VARCHAR2(20 BYTE)  Null
    private String stornoRef;//STORNO_REF   VARCHAR2(5 BYTE) Null
    private List<TSapFiBillposition> listOfBillPositions;
//    private Set<TSapFiBillposition> listOfBillPositions  = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_SAP_FI_BILL_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     * Gibt Verweis auf die ID der Tabelle T_CASE zurück.
     *
     * @return tCaseId
     */
    @ManyToOne(fetch = FetchType.LAZY /*, cascade = CascadeType.ALL */, optional = false) // make the child-to-parent-relation mandatory (use, optional = false)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "T_CASE_ID", nullable = false, /* insertable = false, updatable = false, */ foreignKey = @ForeignKey(name = "FK_FL_BILL4T_CASE_ID"))
    @JsonBackReference(value = "sapBill")
    public TCase getTCaseId() {
        return this.tCaseId;
    }

    /**
     *
     * @param tCaseId :Verweis auf die ID der Tabelle TCase.
     */
    public void setTCaseId(final TCase tCaseId) {
        this.tCaseId = tCaseId;
    }

    /**
     *
     * @return invoice
     */
    @Column(name = "INVOICE", length = 31, nullable = false)
    public String getInvoice() {
        return this.invoice;
    }

    /**
     * @param invoice: Faktura (Rechnungsnummer)
     */
    public void setInvoice(final String invoice) {
        this.invoice = invoice;
    }

    /**
     *
     * @return invoiceType
     */
    @Column(name = "INVOICE_TYPE", length = 5, nullable = true)
    public String getInvoiceType() {
        return this.invoiceType;
    }

    /**
     * @param invoiceType: Fakturatyp
     */
    public void setInvoiceType(final String invoiceType) {
        this.invoiceType = invoiceType;
    }

    /**
     *
     * @return referenceType
     */
    @Column(name = "REFERENCE_TYPE", length = 10, nullable = true)
    @Enumerated(EnumType.STRING)
    public SapReferenceTypeEn getReferenceType() {
        return this.referenceType;
    }

    /**
     * @param referenceType: Vertriebsbelegtyp
     */
    public void setReferenceType(final SapReferenceTypeEn referenceType) {
        this.referenceType = referenceType;
    }

    /**
     *
     * @return referenceCurreny
     */
    @Column(name = "REFERENCE_CURRENCY", length = 10, nullable = true)
    public String getReferenceCurrency() {
        return this.referenceCurrency;
    }

    /**
     * @param referenceCurrency :Währung des Vertriebsbelegs
     */
    public void setReferenceCurrency(final String referenceCurrency) {
        this.referenceCurrency = referenceCurrency;
    }

    /**
     *
     * @return invoiceDate
     */
    @Column(name = "INVOICE_DATE", nullable = true)
    @Temporal(DATE)
    public Date getInvoiceDate() {
        return this.invoiceDate;
    }

    /**
     * @param invoiceDate:Fakturadatum für Fakturaindex und Druck
     */
    public void setInvoiceDate(final Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    /**
     *
     * @return fiscalYear
     */
    @Column(name = "FISCAL_YEAR", length = 15, nullable = true)
    public Integer getFiscalYear() {
        return this.fiscalYear;
    }

    /**
     * @param fiscalYear: Geschäftsjahr
     */
    public void setFiscalYear(final Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    /**
     *
     * @return invoiceKind
     */
    @Column(name = "INVOICE_KIND", length = 5, nullable = true)
    public String getInvoiceKind() {
        return this.invoiceKind;
    }

    /**
     * @param invoiceKind:Fakturaart ,z.b. NF01
     */
    public void setInvoiceKind(final String invoiceKind) {
        this.invoiceKind = invoiceKind;
    }

    /**
     *
     * @return state
     */
    @Column(name = "STATE", length = 5, nullable = true)
    public String getState() {
        return this.state;
    }

    /**
     * @param state: Status für die Überleitung an die Buchhaltung
     */
    public void setState(final String state) {
        this.state = state;
    }

    /**
     *
     * @return netValue
     */
    @Column(name = "NET_VALUE", nullable = true)
    public Double getNetValue() {
        return this.netValue;
    }

    /**
     * @param netValue: Nettowert in Belegwährung
     */
    public void setNetValue(final Double netValue) {
        this.netValue = netValue;
    }

    /**
     *
     * @return receiverRef
     */
    @Column(name = "RECEIVER_REF", length = 20, nullable = true)
    public String getReceiverRef() {
        return this.receiverRef;
    }

    /**
     * @param receiverRef: Regulierer (Rechnungsempfänger) (SAP-internes
     * Kassenkürzel)
     */
    public void setReceiverRef(final String receiverRef) {
        this.receiverRef = receiverRef;
    }

    /**
     *
     * @return stornoRef
     */
    @Column(name = "STORNO_REF", length = 5, nullable = true)
    public String getStornoRef() {
        return this.stornoRef;
    }

    /**
     * @param stornoRef:Stornokennzeichen der Rechnung
     */
    public void setStornoRef(final String stornoRef) {
        this.stornoRef = stornoRef;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "TSapFiBill")
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @OptimisticLock(excluded = false)
    public List<TSapFiBillposition> getSapBillpositions() {
        return this.listOfBillPositions;
    }

    public void setSapBillpositions(final List<TSapFiBillposition> listOfBillPositions) {
        this.listOfBillPositions = listOfBillPositions;
    }

    @Override
    public boolean versionEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TCase)) {
            return false;
        }
        final TSapFiBill other = (TSapFiBill) obj;
        if (!Objects.equals(this.tCaseId, other.tCaseId)) {
            return false;
        }
        if (!Objects.equals(this.invoice, other.invoice)) {
            return false;
        }
        if (!Objects.equals(this.invoiceType, other.invoiceType)) {
            return false;
        }
        if (!Objects.equals(this.referenceType, other.referenceType)) {
            return false;
        }
        if (!Objects.equals(this.referenceCurrency, other.referenceCurrency)) {
            return false;
        }
        if (!Objects.equals(this.invoiceDate, other.invoiceDate)) {
            return false;
        }
        if (!Objects.equals(this.fiscalYear, other.fiscalYear)) {
            return false;
        }
        if (!Objects.equals(this.invoiceKind, other.invoiceKind)) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.netValue, other.netValue)) {
            return false;
        }
        if (!Objects.equals(this.receiverRef, other.receiverRef)) {
            return false;
        }
        if (!Objects.equals(this.stornoRef, other.stornoRef)) {
            return false;
        }
        return true;
    }

}
