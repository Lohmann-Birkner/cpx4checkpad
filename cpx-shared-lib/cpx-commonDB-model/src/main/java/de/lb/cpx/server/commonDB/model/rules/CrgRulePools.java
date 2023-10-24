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
package de.lb.cpx.server.commonDB.model.rules;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * CrgRulePools initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">CRG_RULE_POOLS :Tabelle definiert
 * die Regelpools </p>
 */
@MappedSuperclass
public class CrgRulePools extends AbstractEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private String crgplIdentifier;
    private Date crgplFrom;
    private Date crgplTo;
    private boolean crgplIsActive;
    private int crgplPoolYear; // for the compatibility with RuleSuite
    protected Set<?> crgRuleses = new HashSet<>(0);
    protected Set<?> crgRuleTableses = new HashSet<>(0);

    public static CrgRulePools getTypeInstance(PoolTypeEn type) {
        switch (type) {
            case DEV:
                return new CrgRulePoolsDev();
            default:
                return new CrgRulePoolsProd();

        }
    }

    @Transient
    public static CrgRulePools getTypeInstance() {
        return getTypeInstance(PoolTypeEn.PROD);
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return crgplIdentifier :Poolidentifikator
     */
    @Column(name = "CRGPL_IDENTIFIER", nullable = false, length = 50)
    public String getCrgplIdentifier() {
        return this.crgplIdentifier;
    }

    /**
     *
     * @param crgplIdentifier Column CRGPL_IDENTIFIER: Poolidentifikator
     */
    public void setCrgplIdentifier(String crgplIdentifier) {
        this.crgplIdentifier = crgplIdentifier;
    }

    /**
     *
     * @return crgplFrom: Anfang der Poolgültigkeit
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CRGPL_FROM", nullable = false, length = 7)
    public Date getCrgplFrom() {
        return crgplFrom == null ? null : new Date(crgplFrom.getTime());
    }

    /**
     *
     * @param crgplFrom Column CRGPL_FROM : Anfang der Poolgültigkeit
     */
    public void setCrgplFrom(Date crgplFrom) {
        this.crgplFrom = crgplFrom == null ? null : new Date(crgplFrom.getTime());
    }

    /**
     *
     * @return crgplTo : Ende der Poolgültigkeit
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CRGPL_TO", nullable = false, length = 7)
    public Date getCrgplTo() {
        return crgplTo == null ? null : new Date(crgplTo.getTime());
    }

    /**
     *
     * @param crgplTo Column CRGPL_TO: Ende der Poolgültigkeit
     */
    public void setCrgplTo(Date crgplTo) {
        this.crgplTo = crgplTo == null ? null : new Date(crgplTo.getTime());
    }

    /**
     *
     * @return crgplIsActive: Flag, der bestimmt, ob dieser Pool für die
     * Anzeige/Benutzung freigegeben ist.
     */
    @Column(name = "CRGPL_IS_ACTIVE", nullable = false, precision = 1, scale = 0)
    public boolean isCrgplIsActive() {
        return crgplIsActive;
    }

    /**
     *
     * @param crgplIsActive Column CRGPL_IS_AKTIVE: Flag, der bestimmt, ob
     * dieser Pool für die Anzeige/Benutzung freigegeben ist
     */
    public void setCrgplIsActive(boolean crgplIsActive) {
        this.crgplIsActive = crgplIsActive;
    }

    @Transient
    public Set<CrgRules> getCrgRuleses() {
        return (Set<CrgRules>) crgRuleses;
    }

    public void setCrgRuleses(Set<?> crgRuleses) {
        this.crgRuleses = crgRuleses;
    }

    @Transient
    public Set<CrgRuleTables> getCrgRuleTableses() {
        return (Set<CrgRuleTables>) crgRuleTableses;
    }

    public void setCrgRuleTableses(Set<?> crgRuleTableses) {
        this.crgRuleTableses = crgRuleTableses;
    }

    @Column(name = "CRGPL_POOL_YEAR", nullable = false, precision = 5, scale = 0)
    public int getCrgplPoolYear() {
        return crgplPoolYear;
    }

    public void setCrgplPoolYear(int crgplPoolYear) {
        this.crgplPoolYear = crgplPoolYear;
    }

    @Override
    public String toString() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return super.toString() + "\n"
                + this.getCrgplIdentifier() + ":" + String.valueOf(getCrgplPoolYear()) + ":"
                + (this.getCrgplFrom() == null ? "null" : mDateFormat.format(getCrgplFrom()))
                + ":"
                + (this.getCrgplTo() == null ? "null" : mDateFormat.format(getCrgplTo()));
    }

    @Transient
    public long getId() {
        return id;
    }
}
