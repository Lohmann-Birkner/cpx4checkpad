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
 */
package de.lb.cpx.server.commonDB.model;

import de.checkpoint.drg.GrouperInterfaceBasic;
import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.commons.dao.AbstractDrgmCatalogEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * CSupplementaryFee initially generated at 03.02.2016 10:32:45 by Hibernate
 * Tools 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_SUPPLEMENTARY_FEE : Tabelle der
 * Zusatzentgelte </p>
 */
@Entity
@Table(name = "C_SUPPLEMENTARY_FEE")
@SuppressWarnings("serial")
@SecurityDomain("cpx")
public class CSupplementaryFee extends AbstractDrgmCatalogEntity implements CCatalogIF{

    private static final long serialVersionUID = 1L;

//     private long id;
    private CountryEn countryEn;
    private String supplHosIdent; // for negotiated fees only
    private SupplFeeTypeEn supplTypeEn;//Member of the Enumeration: ZE, ZP, ET
    private int supplYear; // catalog year
    private String supplKey;//    fee key
    private String supplOpsCode; // ops code as in the catalog file. It can have wildcards(*)
    private Double supplValue; // Monetary value to this key ZE/ZP
    private Double supplCwValue; // cost weight value to this key only ETs
    private Date supplValidFrom;
    private Date supplValidTo;
    private Boolean supplNegotiated;
    private String supplDefinition;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_SUPPLEMENTARY_FEE_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return opsCode: OPS - Kode, wie es in dem DRG - Katalog steht, es kann
     * wie ein vollständiger als auch unvollständiger Kode mit einem Wildcard
     * (*) sein
     */
    @Column(name = "SUPPL_OPS_CODE", nullable = true, length = 25)
    public String getSupplOpsCode() {
        return this.supplOpsCode;
    }

    /**
     *
     * @param opsCode Column SUPPL_OPS_CODE: OPS - Kode, wie es in dem DRG -
     * Katalog steht, es kann wie ein vollständiger als auch unvollständiger
     * Kode mit einem Wildcard (*) sein
     */
    public void setSupplOpsCode(String opsCode) {
        this.supplOpsCode = opsCode;
    }

    /**
     *
     * @return supKey : Entgeltschlüssel
     */
    @Column(name = "SUPPL_KEY", nullable = false, length = 10)
    public String getSupplKey() {
        return this.supplKey;
    }

    /**
     *
     * @param supKey Column SUPPL_KEY : Entgeltschlüssel
     */
    public void setSupplKey(String supKey) {
        this.supplKey = supKey;
    }

    /**
     *
     * @return supplDefinition: Beschreibung des Zusatzentgeltes
     */
    @Column(name = "SUPPL_DEFINITION", nullable = true, length = 1000)
    public String getSupplDefinition() {
        return this.supplDefinition;
    }

    public void setSupplDefinition(String def) {
        this.supplDefinition = def;
    }

    /**
     *
     * @return supValue: Geldwert für Zusatzentgelt(ZE/ZP)
     */
    @Column(name = "SUPPL_VALUE", precision = 10, scale = 2)
    public Double getSupplValue() {
        return this.supplValue;
    }

    /**
     *
     * @param supValue Column SUPPL_VALUE : Geldwert für Zusatzentgelt(ZE/ZP)
     */
    public void setSupplValue(Double supValue) {
        this.supplValue = supValue;
    }

    /**
     *
     * @return supplCwValue : CW - Wert für ETs(Tagesentgelt)
     */
    @Column(name = "SUPPL_CW_VALUE", precision = 10, scale = 4)
    public Double getSupplCwValue() {
        return this.supplCwValue;
    }

    public void setSupplCwValue(Double supValue) {
        this.supplCwValue = supValue;
    }

    /**
     *
     * @return supplValidFrom: Anfang der Gültigkeit.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SUPPL_VALID_FROM", length = 7)
    public Date getSupplValidFrom() {
        return supplValidFrom == null ? null : new Date(supplValidFrom.getTime());
    }

    /**
     *
     * @param supplValidFrom Column SUPPL_VALID_FROM :Anfang der Gültigkeit
     */
    public void setSupplValidFrom(Date supplValidFrom) {
        this.supplValidFrom = supplValidFrom == null ? null : new Date(supplValidFrom.getTime());
    }

    /**
     *
     * @return supplValidTo:Ende der Gültigkeit.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SUPPL_VALID_TO", length = 7)
    public Date getSupplValidTo() {
        return supplValidTo == null ? null : new Date(supplValidTo.getTime());
    }

    /**
     *
     * @param supplValidTo Column SUPPL_VALID_TO :Ende der Gültigkeit.
     */
    public void setSupplValidTo(Date supplValidTo) {
        this.supplValidTo = supplValidTo == null ? null : new Date(supplValidTo.getTime());
    }

    /**
     *
     * @return supNegotiated :Verhandelbar Ja/Nein 1/0 .
     */
    @Column(name = "SUPPL_NEGOTIATED", precision = 1, scale = 0)
    public Boolean getSupplNegotiated() {
        return this.supplNegotiated;
    }

    /**
     *
     * @param supNegotiated Column SUPPL_NEGOTIATED : Verhandelbar Ja/Nein 1/0 .
     */
    public void setSupplNegotiated(Boolean supNegotiated) {
        this.supplNegotiated = supNegotiated;
    }

    /**
     *
     * @return supplHosIdent : Für verhandelbare Entgelte IK des Krankenhauses
     */
    @Column(name = "SUPPL_HOS_IDENT", nullable = true)
    public String getSupplHosIdent() {
        return this.supplHosIdent;
    }

    /**
     *
     * @param supplHosIdent Column SUPPL_HOS_IDENT: Für verhandelbare Entgelte
     * IK des Krankenhauses
     */
    public void setSupplHosIdent(final String supplHosIdent) {
        this.supplHosIdent = supplHosIdent;
    }

    @Column(name = "COUNTRY_EN", length = 25)
    @Enumerated(EnumType.STRING)
    public CountryEn getCountryEn() {
        return countryEn;
    }

    public void setCountryEn(CountryEn countryEn) {
        this.countryEn = countryEn;
    }

    /**
     *
     * @return supplFeeTypeEn: Enumeration für Art des Zusätzliche Entgelte 1-3
     * : ZE(Zusatzentgelt ) , ZP(Zusatzentgelt PEPP) , ET(Tagesentgelt).
     */
    @Column(name = "SUPPL_TYPE_EN", nullable = false)
    @Enumerated(EnumType.STRING)
    public SupplFeeTypeEn getSupplTypeEn() {
        return this.supplTypeEn;
    }

    /**
     *
     * @param supplFeeTypeEn Column SUPPL_TYPE_EN :Enumeration für Art des
     * Zusätzliche Entgelte 1-3 : ZE(Zusatzentgelt ) , ZP(Zusatzentgelt PEPP) ,
     * ET(Tagesentgelt).
     */
    public void setSupplTypeEn(final SupplFeeTypeEn supplFeeTypeEn) {
        this.supplTypeEn = supplFeeTypeEn;
    }

    /**
     *
     * @return supplYear :Katalogjahr
     */
    @Column(name = "SUPPL_YEAR", nullable = false, length = 4)
    public int getSupplYear() {
        return supplYear;
    }

    /**
     *
     * @param supplYear Column SUPPL_YEAR:Katalogjahr
     */
    public void setSupplYear(final int supplYear) {
        this.supplYear = supplYear;
    }

    /**
     * gibt die Darstellung des Objektes in dem Format drg drgm - Datei aus
     *
     * @return ein DRGM - String
     */
    @Transient
    @Override
    public String get2DrgmMapping() {
//        try {

        StringBuilder br = new StringBuilder();
        br.append(getId());
        br.append(";");
        br.append(this.getSupplHosIdent() == null ? "" : getSupplHosIdent());
        br.append(";");
        br.append(String.valueOf(GrouperInterfaceBasic.getModelIdByYear(this.getSupplYear())));
        br.append(";");
        br.append(this.getSupplKey());
        br.append(";");
        br.append(this.getSupplOpsCode());
        br.append(";");
        if (SupplFeeTypeEn.ET.equals(this.getSupplTypeEn())) {
            br.append(getSupplCwValue() == null ? 0 : getSupplCwValue().floatValue());
        } else {
            br.append(getSupplValue() == null ? 0 : getSupplValue().floatValue());
        }
        br.append(";");
        br.append(this.getSupplNegotiated() ? 1 : 0);
        br.append(";");
        if (SupplFeeTypeEn.ET.equals(getSupplTypeEn()) || SupplFeeTypeEn.ZP.equals(getSupplTypeEn())) {
            br.append("0;");
        }
        br.append(checkDate4MappingSuppl(getSupplValidFrom()));
        br.append(";");
        br.append(checkDate4MappingSuppl(getSupplValidTo()));
        br.append("\r\n");
        return br.toString();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Error on creating of the DRGM String for supplenetary fee " + getSupplTypeEn().toString() + ": " + ex.getMessage(), ex);
//
//        }
//        return null;

    }

    @Override
    protected boolean showDrgmDate() {
        return this.getSupplNegotiated() && getSupplHosIdent() != null && !getSupplHosIdent().isEmpty();
    }

    @Transient
    @Override
    public String getCode() {
        return getSupplKey();
    }

    @Transient
    @Override
    public Date getValidFrom() {
        return getSupplValidFrom();
    }

    @Transient
    @Override
    public Date getValidTo() {
        return getSupplValidTo();
    }

    @Transient
    @Override
    public String getIk() {
        return getSupplHosIdent();
    }
    @Transient
    @Override
    public int getCatalogYear(){
        return this.getSupplYear();
    }
   
    
    @Transient
    @Override
    public String getAdditionalCode(){
        return getSupplOpsCode() == null?"":getSupplOpsCode();
    }
    
   
}
