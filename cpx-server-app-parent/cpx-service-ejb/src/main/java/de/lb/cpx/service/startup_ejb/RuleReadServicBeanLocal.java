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
package de.lb.cpx.service.startup_ejb;

import de.checkpoint.ruleGrouper.CRGRule;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gerschmann
 */
public interface RuleReadServicBeanLocal {

    CRGRule[] getRules();

    CRGRule getRule(int year, long ruleId);

    CRGRule[] getRule2ListId(List<Long> ruleIds);

    Map<String, CRGRule> getRulesForYear(int year);

    void reloadRules4Pool(String poolName, int year);

    CrgRules getRule2Id(long pRId);

    List<String> getRids4SearchOptions(Map<String, List<String>> pFilterOptionMap);

    Map<String, String> getRuleTypes2Ids();

    void resetRuleList();
}
