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
package de.lb.cpx.server.rule.services;

import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author gerschmann
 */
@Remote
public interface RuleReadBeanRemote {

    /**
     *
     * @return list of rules for this session. If it is prod - than all rules
     * from PROD - Pools , by rule editor according to settings. Now it is DEV
     * and from Pools, that are created with the actual user
     */
    List<CrgRules> getRules4user();

    /**
     *
     * @return list of rule pools for this session. If it is prod - than all
     * rules from PROD - Pools , by rule editor according to settings. Now it is
     * DEV and from Pools, that are created with the actual user
     */
    List<CrgRulePools> getRulePools4user();

    /**
     *
     * @return list of rule types for this session. If it is prod - than all
     * rules from PROD - Pools , by rule editor according to settings. Now it is
     * DEV and from Pools, that are created with the actual user
     */
    List<CrgRuleTypes> getRuleTypes4user();

}
