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

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author gerschmann
 */
@Entity
@Table(name = "CRG_RULE_POOLS_PROD")
@SuppressWarnings("serial")
public class CrgRulePoolsProd extends CrgRulePools {

    private static final long serialVersionUID = 1L;

    public CrgRulePoolsProd() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "default_gen")
    @SequenceGenerator(name = "default_gen", sequenceName = "CRG_RULE_POOLS_PROD_SQ", allocationSize = 1)
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    @Override
    public long getId() {
        return this.id;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "crgRulePoolsProd")
    public Set<CrgRulesProd> getCrgRulesesProd() {
        return (Set<CrgRulesProd>) crgRuleses;
    }

    public void setCrgRulesesProd(Set<CrgRulesProd> crgRuleses) {
        super.setCrgRuleses(crgRuleses);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "crgRulePoolsProd")
    public Set<CrgRuleTablesProd> getCrgRuleTablesProd() {
        return (Set<CrgRuleTablesProd>) crgRuleTableses;
    }

    public void setCrgRuleTablesProd(Set<CrgRuleTablesProd> crgRuleTableses) {
        super.setCrgRuleTableses(crgRuleTableses);
    }

}
