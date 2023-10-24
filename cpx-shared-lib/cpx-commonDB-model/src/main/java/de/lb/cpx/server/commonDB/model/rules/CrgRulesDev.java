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

import de.lb.cpx.model.enums.RuleTypeEn;
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
@Table(name = "CRG_RULES_DEV")
@SuppressWarnings("serial")
public class CrgRulesDev extends CrgRules {

    public CrgRulesDev() {
    }

    public CrgRulesDev(long id,
            //            CrgRuleTypes type,
            String caption,
            String category,
            String identifier,
            String note,
            String number,
            String rid,
            RuleTypeEn ruleType,
            String suggText,
            java.util.Date from,
            java.util.Date to,
            String feeGroup,
            byte[] message) {
        super(id,/*type,*/ caption, category, identifier, note, number, rid, ruleType, suggText, from, to, feeGroup, message);
    }

    public CrgRulesDev(long id,
            CrgRuleTypes type,
            CrgRulePools pool,
            String caption,
            String category,
            String identifier,
            String note,
            String number,
            String rid,
            RuleTypeEn ruleType,
            String suggText,
            java.util.Date from,
            java.util.Date to,
            String feeGroup,
            byte[] message) {
        super(id, type, pool, caption, category, identifier, note, number, rid, ruleType, suggText, from, to, feeGroup, message);
    }

    public CrgRulesDev(long id,
            CrgRuleTypes type,
            String caption,
            String category,
            String identifier,
            String note,
            String number,
            String rid,
            RuleTypeEn ruleType,
            String suggText,
            java.util.Date from,
            java.util.Date to,
            String feeGroup,
            byte[] message) {
        super(id, type, caption, category, identifier, note, number, rid, ruleType, suggText, from, to, feeGroup, message);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "CRG_RULES_DEV_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    @Override
    public long getId() {
        return this.id;
    }

    /**
     *
     * @return crgRuleTypes:Referenz auf die tabelle CRG_RULE_TYPES.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CRGT_ID", nullable = true)
    public CrgRuleTypesDev getCrgRuleTypesDev() {
        return (CrgRuleTypesDev) crgRuleTypes;
    }

    public void setCrgRuleTypesDev(CrgRuleTypesDev crgRuleTypes) {
//        this.crgRuleTypes = crgRuleTypes;
        super.setCrgRuleTypes(crgRuleTypes);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "crgRulesDev")
    public Set<CrgRule2TableDev> getCrgRule2TableDev() {
        return (Set<CrgRule2TableDev>) crgRule2Tables;
    }

    public void setCrgRule2TableDev(Set<CrgRule2TableDev> crgRule2Tables) {
//        this.crgRule2Tables = crgRule2Tables;
        super.setCrgRule2Tables(crgRule2Tables);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "crgRulesDev")
    public Set<CrgRule2RoleDev> getCrgRule2RoleDev() {
        return (Set<CrgRule2RoleDev>) crgRule2Roles;
    }

    public void setCrgRule2RoleDev(Set<CrgRule2RoleDev> crgRule2Roles) {
        super.setCrgRule2Roles(crgRule2Roles);
    }

    /**
     *
     * @return crgRulePools :Referenz auf die Tablle CRG_RULE_POOLS ,d.h auf den
     * Pool in den die Regel gehört.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CRGPL_ID", nullable = false)
    public CrgRulePoolsDev getCrgRulePoolsDev() {
        return (CrgRulePoolsDev) crgRulePools;
    }

    /**
     *
     * @param crgRulePools Column CRGPL_ID :Referenz auf die Tablle
     * CRG_RULE_POOLS ,d.h auf den Pool in den die Regel gehört.
     */
    public void setCrgRulePoolsDev(CrgRulePoolsDev crgRulePools) {
        super.setCrgRulePools(crgRulePools);
    }

}
