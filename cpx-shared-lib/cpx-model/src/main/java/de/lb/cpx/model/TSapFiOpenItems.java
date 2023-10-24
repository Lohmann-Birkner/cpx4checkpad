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
import de.lb.cpx.server.commons.dao.AbstractVersionEntity;
import java.util.Date;
import java.util.Objects;
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
import static javax.persistence.TemporalType.DATE;

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
@Table(name = "T_SAP_FI_OPEN_ITEMS", indexes = {
    @Index(name = "IDX_SAP_FI_OPEN_ITEMS4T_CASE ", columnList = "T_CASE_ID", unique = false)})
@SuppressWarnings("serial")

public class TSapFiOpenItems extends AbstractVersionEntity {

    private static final long serialVersionUID = 1L;
    private TCase tCase;//T_CASE_ID    NUMBER(20,0)   Not Null( mit CONSTRAINT )
    private String companyCode;//COMPANY_CODE     VARCHAR2(5 BYTE)    Null
    private String customerNumber;//CUSTOMER_NUMBER   VARCHAR2(15 BYTE)    Null
    private String refNumber;//REF_NUMBER  VARCHAR2(20 BYTE) Null (Rechnung; Storno)
    private Integer fiscalYear;//FISCAL_YEAR  NUMBER(15,0) Null
    private String numberReceipt;//NUMBER_RECEIPT  VARCHAR2(15 BYTE)  Null
    private Date orderDateReceipt;//ORDERDATE_RECEIPT   DATE  Null
    private Date receiptDateReceipt;//RECEIPTDATE_RECEIPT   DATE  Null
    private Date recordingDateReceipt;//RECORDINGDATE_RECEIPT   DATE  Null
    private String currencyKey;//CURRENCY_KEY  VARCHAR2(5 BYTE)  Null
    private String refNumberReceipt;//REFNUMBER__RECEIPT  VARCHAR2(20 BYTE)  Null
    private String kindOfReceipt;//KIND_OF_RECEIPT  VARCHAR2(5 BYTE)  Null
    private String postingKey;//POSTING_KEY  VARCHAR2(5 BYTE)  Null
    private String debitCreditKey;//POSTING_KEY  VARCHAR2(15 BYTE)  Null
    private Double value;//VALUE   Float Null
    private Double netValue;//NET_VALUE   Float Null
    private String text;//TEXT  VARCHAR2(50 BYTE)

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_SAP_FI_OPEN_ITEMS_SQ")
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
    @ManyToOne(fetch = FetchType.LAZY /*, cascade = CascadeType.ALL*/, optional = false)
    @JoinColumn(name = "T_CASE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_OPEN_ITEMS4T_CASE_ID"))
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference(value = "sapFiOpenItems")
    public TCase getTCase() {
        return this.tCase;
    }

    /**
     *
     * @param tCase :Verweis auf die ID der Tabelle TCase.
     */
    public void setTCase(final TCase tCase) {
        this.tCase = tCase;
    }

    /**
     *
     * @return companyCode
     */
    @Column(name = "COMPANY_CODE", length = 5, nullable = true)
    public String getCompanyCode() {
        return this.companyCode;
    }

    /**
     * @param companyCode: Buchungskreis
     */
    public void setCompanyCode(final String companyCode) {
        this.companyCode = companyCode;
    }

    /**
     *
     * @return customerNumber
     */
    @Column(name = "CUSTOMER_NUMBER", length = 15, nullable = true)
    public String getCustomerNumber() {
        return this.customerNumber;
    }

    /**
     * @param customerNumber: Kundennummer
     */
    public void setCustomerNumber(final String customerNumber) {
        this.customerNumber = customerNumber;
    }

    /**
     *
     * @return refNumber
     */
    @Column(name = "REF_NUMBER", length = 20, nullable = true)
    public String getRefNumber() {
        return this.refNumber;
    }

    /**
     * @param refNumber: Zuordnungsnummer
     */
    public void setRefNumber(final String refNumber) {
        this.refNumber = refNumber;
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
     * @param fiscalYear:Geschäftsjahr
     */
    public void setFiscalYear(final Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    /**
     *
     * @return numberReceipt
     */
    @Column(name = "NUMBER_RECEIPT", length = 15, nullable = true)
    public String getNumberReceipt() {
        return this.numberReceipt;
    }

    /**
     * @param numberReceipt:
     */
    public void setNumberReceipt(final String numberReceipt) {
        this.numberReceipt = numberReceipt;
    }

    /**
     *
     * @return orderDateReceipt
     */
    @Column(name = "ORDERDATE_RECEIPT", nullable = true)
    @Temporal(DATE)
    public Date getOrderDateReceipt() {
        return orderDateReceipt == null ? null : new Date(orderDateReceipt.getTime());
    }

    /**
     * @param orderDateReceipt: Buchungsdatum im Beleg
     */
    public void setOrderDateReceipt(final Date orderDateReceipt) {
        this.orderDateReceipt = orderDateReceipt == null ? null : new Date(orderDateReceipt.getTime());
    }

    /**
     *
     * @return receiptDateReceipt
     */
    @Column(name = "RECEIPTDATE_RECEIPT", nullable = true)
    @Temporal(DATE)
    public Date getReceiptDateReceipt() {
        return receiptDateReceipt == null ? null : new Date(receiptDateReceipt.getTime());
    }

    /**
     * @param receiptDateReceipt: Belegdatum im Beleg
     */
    public void setReceiptDateReceipt(final Date receiptDateReceipt) {
        this.receiptDateReceipt = receiptDateReceipt == null ? null : new Date(receiptDateReceipt.getTime());
    }

    /**
     *
     * @return recordingDateReceipt
     */
    @Column(name = "RECORDINGDATE_RECEIPT", nullable = true)
    @Temporal(DATE)
    public Date getRecordingDateReceipt() {
        return recordingDateReceipt == null ? null : new Date(recordingDateReceipt.getTime());
    }

    /**
     * @param recordingDateReceipt : Tag der Erfassung des Buchhaltungsbeleges
     */
    public void setRecordingDateReceipt(final Date recordingDateReceipt) {
        this.recordingDateReceipt = recordingDateReceipt == null ? null : new Date(recordingDateReceipt.getTime());
    }

    /**
     *
     * @return currencyKey
     */
    @Column(name = "CURRENCY_KEY", length = 5, nullable = true)
    public String getCurrencyKey() {
        return this.currencyKey;
    }

    /**
     * @param currencyKey: Währungsschlüssel
     */
    public void setCurrencyKey(final String currencyKey) {
        this.currencyKey = currencyKey;
    }

    /**
     *
     * @return refNumberReceipt
     */
    @Column(name = "REFNUMBER__RECEIPT", length = 20, nullable = true)
    public String getRefNumberReceipt() {
        return this.refNumberReceipt;
    }

    /**
     * @param refNumberReceipt : Referenz-Belegnummer
     */
    public void setRefNumberReceipt(final String refNumberReceipt) {
        this.refNumberReceipt = refNumberReceipt;
    }

    /**
     *
     * @return kindOfReceipt
     */
    @Column(name = "KIND_OF_RECEIPT", length = 5, nullable = true)
    public String getKindOfReceipt() {
        return this.kindOfReceipt;
    }

    /**
     * @param kindOfReceipt: Belegart
     */
    public void setKindOfReceipt(final String kindOfReceipt) {
        this.kindOfReceipt = kindOfReceipt;
    }

    /**
     *
     * @return postinkKey
     */
    @Column(name = "POSTING_KEY", length = 5, nullable = true)
    public String getPostingKey() {
        return this.postingKey;
    }

    /**
     * @param postingKey : Buchungsschlüssel
     */
    public void setPostingKey(final String postingKey) {
        this.postingKey = postingKey;
    }

    /**
     *
     * @return debitCreditKey
     */
    @Column(name = "DEBIT_CREDIT_KEY", length = 15, nullable = true)
    public String getDebitCreditKey() {
        return this.debitCreditKey;
    }

    /**
     * @param debitCreditKey: Soll-/Haben-Kennzeichen
     */
    public void setDebitCreditKey(final String debitCreditKey) {
        this.debitCreditKey = debitCreditKey;
    }

    /**
     *
     * @return value
     */
    @Column(name = "VALUE", nullable = true)
    public Double getValue() {
        return this.value;
    }

    /**
     * @param value : Betrag in Hauswährung
     */
    public void setValue(final Double value) {
        this.value = value;
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
     * @param netValue : Nettowert der Fakturaposition in Belegwährung
     */
    public void setNetValue(final Double netValue) {
        this.netValue = netValue;
    }

    /**
     *
     * @return text
     */
    @Column(name = "TEXT", length = 100, nullable = true)
    public String getText() {
        return this.text;
    }

    /**
     * @param text:Text zur Rechnungsposition
     */
    public void setText(final String text) {
        this.text = text;
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
        final TSapFiOpenItems other = (TSapFiOpenItems) obj;
        if (!Objects.equals(this.tCase, other.tCase)) {
            return false;
        }
        if (!Objects.equals(this.companyCode, other.companyCode)) {
            return false;
        }
        if (!Objects.equals(this.customerNumber, other.customerNumber)) {
            return false;
        }
        if (!Objects.equals(this.refNumber, other.refNumber)) {
            return false;
        }
        if (!Objects.equals(this.fiscalYear, other.fiscalYear)) {
            return false;
        }
        if (!Objects.equals(this.numberReceipt, other.numberReceipt)) {
            return false;
        }
        if (!Objects.equals(this.orderDateReceipt, other.orderDateReceipt)) {
            return false;
        }
        if (!Objects.equals(this.receiptDateReceipt, other.receiptDateReceipt)) {
            return false;
        }
        if (!Objects.equals(this.recordingDateReceipt, other.recordingDateReceipt)) {
            return false;
        }
        if (!Objects.equals(this.currencyKey, other.currencyKey)) {
            return false;
        }
        if (!Objects.equals(this.refNumberReceipt, other.refNumberReceipt)) {
            return false;
        }
        if (!Objects.equals(this.kindOfReceipt, other.kindOfReceipt)) {
            return false;
        }
        if (!Objects.equals(this.debitCreditKey, other.debitCreditKey)) {
            return false;
        }

        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (!Objects.equals(this.netValue, other.netValue)) {
            return false;
        }
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        return true;
    }
}
