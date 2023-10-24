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

import de.lb.cpx.server.commonDB.model.enums.RuleTableCategoryEn;
import java.util.Date;
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

/**
 *
 * @author gerschmann
 */
@Entity
@Table(name = "CRG_RULE_TABLES_DEV")
@SuppressWarnings("serial")
public class CrgRuleTablesDev extends CrgRuleTables {

    public CrgRuleTablesDev(long id, String tableName, Long creationUserId, Date creation_date, byte[] message, RuleTableCategoryEn crgtCategory) {
        super(id, tableName, creationUserId, creation_date, message, crgtCategory);
    }
    
    public CrgRuleTablesDev(long id, String tableName, String tableContent, Long creationUserId, Date creation_date, byte[] message, RuleTableCategoryEn crgtCategory) {
        super(id, tableName, tableContent, creationUserId, creation_date, message, crgtCategory);
    }

    public CrgRuleTablesDev(long id, String tableName, byte[] message, RuleTableCategoryEn crgtCategory) {
        super(id, tableName, message, crgtCategory);
    }

    public CrgRuleTablesDev() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "CRG_RULE_TABLES_DEV_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    @Override
    public long getId() {
        return this.id;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "crgRuleTablesDev")
    public Set<CrgRule2TableDev> getCrgRule2TablesDev() {
        return (Set<CrgRule2TableDev>) crgRule2Tables;
    }

    public void setCrgRule2TablesDev(Set<CrgRule2TableDev> crgRule2Tables) {
        super.setCrgRule2Tables(crgRule2Tables);
    }

    /**
     *
     * @return crgRulePools:Referenz auf die Tablle CRG_RULE_POOLS(Regelpool)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CRGPL_ID", nullable = false)
    public CrgRulePoolsDev getCrgRulePoolsDev() {
        return (CrgRulePoolsDev) crgRulePools;
    }

    public void setCrgRulePoolsDev(CrgRulePoolsDev crgRulePools) {
//        this.crgRuleTypes = crgRuleTypes;
        super.setCrgRulePools(crgRulePools);
    }
}
