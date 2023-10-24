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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * T_SAP_FI_BILL initially generated at 19.12.2017 17:07:59 by Hibernate Tools
 * 3.2.2.GA
 * <p style="font-size:1em; color:green;"> Die Tabelle T_SAP_FI_BILL enthält
 * Rechnungsdaten welche über das SAP-Modul FI (Finanzwesen) abgerufen werden. .
 * </p>
 *
 * @author Shahin
 */
@Entity
@Table(name = "T_SAP_FI_BILLPOSITION", indexes = {
    @Index(name = "IDX_BILLPOSITION4SAP_FI_BILL ", columnList = "T_SAP_FI_BILL_ID ", unique = false)})

public class TSapFiBillposition extends AbstractVersionEntity {

    private static final long serialVersionUID = 1L;
    private TSapFiBill tSapFiBill;//T_SAP_FI_BILL_ID  NUMBER(20,0)
    private String invoice;//INVOICE  VARCHAR2(20 BYTE)
    private String positionNumber;//POSITION_NUMBER  VARCHAR2(50 BYTE)
    private String text;//TEXT  VARCHAR2(50 BYTE)
    private String referenceId;//REFERENCE_ID  VARCHAR2(15 BYTE)
    private Double amount;//AMOUNT  FLOAT
    private Double netValue;//NET_VALUE  FLOAT
    private Double baseValue;//BASE_VALUE  FLOAT

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "T_SAP_FI_BILLPOSITION_SQ")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    /**
     *
     *
     * @return tSapFiBillId
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JoinColumn(name = "T_SAP_FI_BILL_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_Bill_OPS4T_SAP_FI_BILL_ID"))
    @JoinColumn(name = "T_SAP_FI_BILL_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_BILL_OPS4T_SAP_FL_BILL_ID"))
    @JsonBackReference(value = "sapBillPosition")
    public TSapFiBill getTSapFiBill() {
        return this.tSapFiBill;
    }

    /**
     *
     * @param tSapFiBill : Verweis auf die ID der Tabelle T_SAP_FI_BILL
     */
    public void setTSapFiBill(final TSapFiBill tSapFiBill) {
        this.tSapFiBill = tSapFiBill;
    }

    /**
     *
     * @return invoice
     */
    @Column(name = "INVOICE", length = 20, nullable = true)
    public String getInvoice() {
        return this.invoice;
    }

    /**
     * @param invoice : Faktura (Rechnungsnummer)
     */
    public void setInvoice(final String invoice) {
        this.invoice = invoice;
    }

    /**
     *
     * @return positionNumber
     */
    @Column(name = "POSITION_NUMBER", length = 50, nullable = true)
    public String getPositionNumber() {
        return this.positionNumber;
    }

    /**
     * @param positionNumber: Fakturaposition (Rechnungsposition)
     */
    public void setPositionNumber(final String positionNumber) {
        this.positionNumber = positionNumber;
    }

    /**
     *
     * @return text
     */
    @Column(name = "TEXT", length = 50, nullable = true)
    public String getText() {
        return this.text;
    }

    /**
     * @param text: Kurztext der Kundenauftragsposition
     */
    public void setText(final String text) {
        this.text = text;
    }

    /**
     *
     * @return referenceId
     */
    @Column(name = "REFERENCE_ID", length = 15, nullable = true)
    public String getReferenceId() {
        return this.referenceId;
    }

    /**
     * @param referenceId: LeistungsID innerhalb eines Tarifwerks
     */
    public void setReferenceId(final String referenceId) {
        this.referenceId = referenceId;
    }

    /**
     *
     * @return amount
     */
    @Column(name = "AMOUNT", nullable = true)
    public Double getAmount() {
        return this.amount;
    }

    /**
     * @param amount: Tatsächlich fakturierte Menge
     */
    public void setAmount(final Double amount) {
        this.amount = amount;
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
     * @param netValue: Nettowert der Fakturaposition in Belegwährung
     */
    public void setNetValue(final Double netValue) {
        this.netValue = netValue;
    }

    /**
     *
     * @return baseValue
     */
    @Column(name = "BASE_VALUE", nullable = true)
    public Double getBaseValue() {
        return this.baseValue;
    }

    /**
     * @param baseValue: Grundpreis der Leistung
     */
    public void setBaseValue(final Double baseValue) {
        this.baseValue = baseValue;
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
        final TSapFiBillposition other = (TSapFiBillposition) obj;
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        if (!Objects.equals(this.baseValue, other.baseValue)) {
            return false;
        }
        if (!Objects.equals(this.invoice, other.invoice)) {
            return false;
        }
        if (!Objects.equals(this.netValue, other.netValue)) {
            return false;
        }
        if (!Objects.equals(this.positionNumber, other.positionNumber)) {
            return false;
        }
        if (!Objects.equals(this.referenceId, other.referenceId)) {
            return false;
        }
//        if (!Objects.equals(this.tSapFiBill, other.tSapFiBill)) {
//            return false;
//        }
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }

        return true;
    }

}
