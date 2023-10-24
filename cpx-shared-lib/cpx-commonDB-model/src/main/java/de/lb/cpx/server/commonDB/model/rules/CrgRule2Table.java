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
package de.lb.cpx.server.commonDB.model.rules;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * CrgRule2Table initially generated at 03.02.2016 10:32:45 by Hibernate Tools
 * 3.2.2.GA
 * <br> <p style="font-size:1em; color:green;">CRG_RULE_2_TABLE: Mapping der
 * Regeln zu Regeltabellen </p>
 */
@MappedSuperclass
public class CrgRule2Table extends AbstractEntity {

    private static final long serialVersionUID = 1L;

//     private long id;
    protected CrgRuleTables crgRuleTables;
    protected CrgRules crgRules;

    public static CrgRule2Table getTypeInstance(PoolTypeEn type) {
        switch (type) {
            case DEV:
                return new CrgRule2TableDev();
            default:
                return new CrgRule2TableProd();

        }
    }

    @Transient
    public static CrgRule2Table getTypeInstance() {
        return getTypeInstance(PoolTypeEn.PROD);
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return crgRuleTables :Referenz auf die Tabelle CRG_RULE_TABLE
     */
    @Transient
    public CrgRuleTables getCrgRuleTables() {
        return this.crgRuleTables;
    }

    /**
     *
     * @param crgRuleTables Column CRGT_ID :Referenz auf die Tabelle
     * CRG_RULE_TABLE
     */
    public void setCrgRuleTables(CrgRuleTables crgRuleTables) {
        this.crgRuleTables = crgRuleTables;
    }

    /**
     *
     * @return crgRules :Referenz auf die Tabelle CRG_RULES
     */
    @Transient
    public CrgRules getCrgRules() {
        return this.crgRules;
    }

    /**
     *
     * @param crgRules Column CRGR_ID:Referenz auf die Tabelle CRG_RULES
     */
    public void setCrgRules(CrgRules crgRules) {
        this.crgRules = crgRules;
    }

    @Transient
    public long getId() {
        return id;
    }
}
