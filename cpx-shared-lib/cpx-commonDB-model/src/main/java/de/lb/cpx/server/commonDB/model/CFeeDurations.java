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
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * CFeeDurations initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">C_FEE_DURATIONS : Tabelle der
 * Verweildauerdaten. </p>
 *
 */
@Entity
@Table(name = "C_FEE_DURATIONS")
@SuppressWarnings("serial")
public class CFeeDurations extends AbstractCatalogEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private CFeeValues CFeeValues;
    private String feedPartiion;
    private Double feedAlos;
    private BigDecimal feedUgvd;
    private BigDecimal feedOgvd;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "C_FEE_DURATIONS_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return FEE_VALUES_ID: Referenz auf die tabelle C_FEE_VALUES .
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEE_VALUES_ID", nullable = false)
    public CFeeValues getCFeeValues() {
        return this.CFeeValues;
    }

    /**
     *
     * @param CFeeValues Column FEE_VALUES_ID: Referenz auf die tabelle
     * C_FEE_VALUES .
     */
    public void setCFeeValues(CFeeValues CFeeValues) {
        this.CFeeValues = CFeeValues;
    }

    /**
     *
     * @return FEED_PARTIION :Partition zu dem das Entgelt gehört.
     */
    @Column(name = "FEED_PARTIION", length = 1)
    public String getFeedPartiion() {
        return this.feedPartiion;
    }

    /**
     *
     * @param feedPartiion Column FEED_PARTIION :Partition zu dem das Entgelt
     * gehört.
     */
    public void setFeedPartiion(String feedPartiion) {
        this.feedPartiion = feedPartiion;
    }

    /**
     *
     * @return FEED_ALOS : Mittlere Verweildauer des Entgelts .
     */
    @Column(name = "FEED_ALOS", precision = 126, scale = 0)
    public Double getFeedAlos() {
        return this.feedAlos;
    }

    /**
     *
     * @param feedAlos Column FEED_ALOS: Mittlere Verweildauer des Entgelts .
     */
    public void setFeedAlos(Double feedAlos) {
        this.feedAlos = feedAlos;
    }

    /**
     *
     * @return FEED_UGVD: UGVD zu dem Entgelt .
     */
    @Column(name = "FEED_UGVD", precision = 38, scale = 0)
    public BigDecimal getFeedUgvd() {
        return this.feedUgvd;
    }

    /**
     *
     * @param feedUgvd Column FEED_UGVD: UGVD zu dem Entgelt .
     */
    public void setFeedUgvd(BigDecimal feedUgvd) {
        this.feedUgvd = feedUgvd;
    }

    /**
     *
     * @return FEED_OGVD : OGVD zu dem Entgelt.
     */
    @Column(name = "FEED_OGVD", precision = 38, scale = 0)
    public BigDecimal getFeedOgvd() {
        return this.feedOgvd;
    }

    /**
     *
     * @param feedOgvd Column FEED_OGVD OGVD zu dem Entgelt.
     */
    public void setFeedOgvd(BigDecimal feedOgvd) {
        this.feedOgvd = feedOgvd;
    }

}
