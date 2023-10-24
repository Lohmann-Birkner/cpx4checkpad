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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * CrgRuleTypes initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">CRG_RULE_TYPES : Tabelle der
 * Regeltypen, wird mit vorgegebenen Typen vorgefüllt, danach wird von Benutzer
 * weitergepflegt </p>
 */
@MappedSuperclass
public class CrgRuleTypes extends AbstractEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    private String crgtShortText;
    private String crgtDisplayText;
    private boolean crgtReadonly;
    private int crgtIdent;
    protected Set<?> crgRuleses = new HashSet<>(0);

    public void setId(long id) {
        this.id = id;
    }

    public static CrgRuleTypes getTypeInstance(PoolTypeEn type) {
        switch (type) {
            case DEV:
                return new CrgRuleTypesDev();
            default:
                return new CrgRuleTypesProd();

        }
    }

    @Transient
    public static CrgRuleTypes getTypeInstance() {
        return getTypeInstance(PoolTypeEn.PROD);
    }

    /**
     *
     * @return crgtShortText :Kurzname des regeltyps
     */
    @Column(name = "CRGT_SHORT_TEXT", nullable = false, length = 50)
    public String getCrgtShortText() {
        return this.crgtShortText;
    }

    /**
     *
     * @param crgtShortText Column CRGT_SHORT_TEXT : Kurzname des regeltyps
     */
    public void setCrgtShortText(String crgtShortText) {
        this.crgtShortText = crgtShortText;
    }

    /**
     *
     * @return crgtDisplayText: Bezeichnung des Regeltyps
     */
    @Column(name = "CRGT_DISPLAY_TEXT", nullable = false)
    public String getCrgtDisplayText() {
        return this.crgtDisplayText;
    }

    /**
     *
     * @param crgtDisplayText ColumnCRGT_DISPLAY_TEXT: Bezeichnung des Regeltyps
     */
    public void setCrgtDisplayText(String crgtDisplayText) {
        this.crgtDisplayText = crgtDisplayText;
    }

    /**
     *
     * @return crgtReadonly: Flag = 1 für die vordefinierte Regeltypen wie DKR
     * und s.w.
     */
    @Column(name = "CRGT_READONLY", nullable = false, precision = 1, scale = 0)
    public boolean isCrgtReadonly() {
        return this.crgtReadonly;
    }

    /**
     *
     * @return crgtIdent: Ident des Regeltyps unabhängig von ID, eindeutige
     * Identifikator des Regeltyps, der in xml Darstellung übernomment wird
     */
    @Column(name = "CRGT_IDENT", precision = 5, scale = 0)
    public int getCrgtIdent() {
        return crgtIdent;
    }

    public void setCrgtIdent(int crgtIdent) {
        this.crgtIdent = crgtIdent;
    }

    /**
     *
     * @param crgtReadonly Column CRGT_READONLY :Flag = 1 für die vordefinierte
     * Regeltypen wie DKR und s.w.
     */
    public void setCrgtReadonly(boolean crgtReadonly) {
        this.crgtReadonly = crgtReadonly;
    }

    @Transient
    public Set<CrgRules> getCrgRuleses() {
        return (Set<CrgRules>) crgRuleses;
    }

    public void setCrgRuleses(Set<?> crgRuleses) {
        this.crgRuleses = crgRuleses;
    }

    @Transient
    public long getId() {
        return id;
    }

}
