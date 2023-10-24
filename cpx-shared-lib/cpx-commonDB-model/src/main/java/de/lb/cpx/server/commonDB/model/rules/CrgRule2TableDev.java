/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.model.rules;

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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author gerschmann
 */
@Entity
@Table(name = "CRG_RULE_2_TABLE_DEV")
@SuppressWarnings("serial")
public class CrgRule2TableDev extends CrgRule2Table {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "CRG_RULE_2_TABLE_DEV_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    @Override
    public long getId() {
        return this.id;
    }

    /**
     *
     * @return crgRuleTables :Referenz auf die Tabelle CRG_RULE_TABLE_DEV
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CRGT_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    public CrgRuleTablesDev getCrgRuleTablesDev() {
        return (CrgRuleTablesDev) crgRuleTables;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CRGR_ID", nullable = false)
    public CrgRulesDev getCrgRulesDev() {
        return (CrgRulesDev) crgRules;
    }

    public void setCrgRuleTablesDev(CrgRuleTablesDev crgRuleTables) {
        super.setCrgRuleTables(crgRuleTables);
    }

    public void setCrgRulesDev(CrgRulesDev crgRules) {
        super.setCrgRules(crgRules);
    }

}
