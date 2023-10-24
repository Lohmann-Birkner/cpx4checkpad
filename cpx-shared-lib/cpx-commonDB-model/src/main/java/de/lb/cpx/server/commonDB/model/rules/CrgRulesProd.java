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
@Table(name = "CRG_RULES_PROD")
@SuppressWarnings("serial")
public class CrgRulesProd extends CrgRules {

    public CrgRulesProd() {
    }

    public CrgRulesProd(long id,
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
        super(id, /*type,*/ caption, category, identifier, note, number, rid, ruleType, suggText, from, to, feeGroup, message);
    }

    public CrgRulesProd(long id,
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

    public CrgRulesProd(long id,
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
    @SequenceGenerator(name = "default_gen", sequenceName = "CRG_RULES_PROD_SQ", allocationSize = 1)
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
    public CrgRuleTypesProd getCrgRuleTypesProd() {
        return (CrgRuleTypesProd) crgRuleTypes;
    }

    public void setCrgRuleTypesProd(CrgRuleTypesProd crgRuleTypes) {
//        this.crgRuleTypes = crgRuleTypes;
        super.setCrgRuleTypes(crgRuleTypes);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "crgRulesProd")
    public Set<CrgRule2TableProd> getCrgRule2TableProd() {
        return (Set<CrgRule2TableProd>) crgRule2Tables;
    }

    public void setCrgRule2TableProd(Set<CrgRule2TableProd> crgRule2Tables) {
//        this.crgRule2Tables = crgRule2Tables;
        super.setCrgRule2Tables(crgRule2Tables);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "crgRulesProd")
    public Set<CrgRule2RoleProd> getCrgRule2RoleProd() {
        return (Set<CrgRule2RoleProd>) crgRule2Roles;
    }

    public void setCrgRule2RoleProd(Set<CrgRule2RoleProd> crgRule2Roles) {
        super.setCrgRule2Roles(crgRule2Roles);
    }

    /**
     *
     * @return crgRulePools :Referenz auf die Tablle CRG_RULE_POOLS ,d.h auf den
     * Pool in den die Regel gehört.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CRGPL_ID", nullable = false)
    public CrgRulePoolsProd getCrgRulePoolsProd() {
        return (CrgRulePoolsProd) crgRulePools;
    }

    /**
     *
     * @param crgRulePools Column CRGPL_ID :Referenz auf die Tablle
     * CRG_RULE_POOLS ,d.h auf den Pool in den die Regel gehört.
     */
    public void setCrgRulePoolsProd(CrgRulePoolsProd crgRulePools) {
        super.setCrgRulePools(crgRulePools);
    }

}
