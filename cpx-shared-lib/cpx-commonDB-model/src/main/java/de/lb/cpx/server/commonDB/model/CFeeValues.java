/**
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

import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CFeeValues initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_FEE_VALUES : Tabelle der
 * Zahlbeträge eines Krankenhauses.</p>
 *
 */
@Entity
@Table(name = "C_FEE_VALUES")
@SuppressWarnings("serial")
public class CFeeValues extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private CHospital CHospital;
    private String feevFeekey;
    private boolean feevIspepp;
    private Double feevValue;
    private Date feevValidFrom;
    private Date feevValidTo;
    private String feevCurrency;
    private boolean feevIsnegotiated;
    private Set<CFeeDurations> CFeeDurationses = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_FEE_VALUES_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return CHospital :Referenz auf die Tabelle C_HOSPITAL .
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "C_HOSPITAL_ID")
    public CHospital getCHospital() {
        return this.CHospital;
    }

    /**
     *
     * @param CHospital Column C_HOSPITAL_ID :Referenz auf die Tabelle
     * C_HOSPITAL .
     */
    public void setCHospital(CHospital CHospital) {
        this.CHospital = CHospital;
    }

    /**
     *
     * @return feevFeekey :Kodierung des Entgeltes.
     */
    @Column(name = "FEEV_FEEKEY", nullable = false, length = 8)
    public String getFeevFeekey() {
        return this.feevFeekey;
    }

    /**
     *
     * @param feevFeekey Column FEEV_FEEKEY :Kodierung des Entgeltes.
     */
    public void setFeevFeekey(String feevFeekey) {
        this.feevFeekey = feevFeekey;
    }

    /**
     *
     * @return feevIspepp : PEPP Ja/Nein 1/0 .
     */
    @Column(name = "FEEV_ISPEPP", nullable = false, precision = 1, scale = 0)
    public boolean isFeevIspepp() {
        return this.feevIspepp;
    }

    /**
     *
     * @param feevIspepp Column FEEV_ISPEPP : PEPP Ja/Nein 1/0 .
     */
    public void setFeevIspepp(boolean feevIspepp) {
        this.feevIspepp = feevIspepp;
    }

    /**
     *
     * @return feevValue: Zahlbetrag für die benannt Kodierung(in FEEV_FEEKEY).
     */
    @Column(name = "FEEV_VALUE", precision = 126, scale = 0)
    public Double getFeevValue() {
        return this.feevValue;
    }

    /**
     *
     * @param feevValue Column FEEV_VALUE: Zahlbetrag für die benannt
     * Kodierung(in FEEV_FEEKEY).
     */
    public void setFeevValue(Double feevValue) {
        this.feevValue = feevValue;
    }

    /**
     *
     * @return feevValidFrom : Datum ,ab dem das genannte Entgelt gültig ist.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FEEV_VALID_FROM", length = 11)
    public Date getFeevValidFrom() {
        return feevValidFrom == null ? null : new Date(feevValidFrom.getTime());
    }

    /**
     *
     * @param feevValidFrom Column FEEV_VALID_FROM: Datum ,ab dem das genannte
     * Entgelt gültig ist.
     */
    public void setFeevValidFrom(Date feevValidFrom) {
        this.feevValidFrom = feevValidFrom == null ? null : new Date(feevValidFrom.getTime());
    }

    /**
     *
     * @return feevValidTo : Datum ,bis dem das genannte Entgelt gültig ist.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FEEV_VALID_TO", length = 11)
    public Date getFeevValidTo() {
        return feevValidTo == null ? null : new Date(feevValidTo.getTime());
    }

    /**
     *
     * @param feevValidTo Column FEEV_VALID_TO : Datum ,bis dem das genannte
     * Entgelt gültig ist.
     */
    public void setFeevValidTo(Date feevValidTo) {
        this.feevValidTo = feevValidTo == null ? null : new Date(feevValidTo.getTime());
    }

    /**
     *
     * @return feevCurrency : Währungsbezeichnung für den Zahlbetrag .
     */
    @Column(name = "FEEV_CURRENCY", length = 3)
    public String getFeevCurrency() {
        return this.feevCurrency;
    }

    /**
     *
     * @param feevCurrency Column FEEV_CURRENCY : Währungsbezeichnung für den
     * Zahlbetrag .
     */
    public void setFeevCurrency(String feevCurrency) {
        this.feevCurrency = feevCurrency;
    }

    /**
     *
     * @return feevIsnegotiated : Verhandelbar Ja/Nein 1/0 .
     */
    @Column(name = "FEEV_ISNEGOTIATED", nullable = false, precision = 1, scale = 0)
    public boolean isFeevIsnegotiated() {
        return this.feevIsnegotiated;
    }

    /**
     *
     * @param feevIsnegotiated Column FEEV_ISNEGOTIATED: Verhandelbar Ja/Nein
     * 1/0 .
     */
    public void setFeevIsnegotiated(boolean feevIsnegotiated) {
        this.feevIsnegotiated = feevIsnegotiated;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "CFeeValues")
    public Set<CFeeDurations> getCFeeDurationses() {
        return this.CFeeDurationses;
    }

    public void setCFeeDurationses(Set<CFeeDurations> CFeeDurationses) {
        this.CFeeDurationses = CFeeDurationses;
    }

}
