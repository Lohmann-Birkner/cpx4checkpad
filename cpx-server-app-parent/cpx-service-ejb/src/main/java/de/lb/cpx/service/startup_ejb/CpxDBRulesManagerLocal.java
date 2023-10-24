/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.startup_ejb;

import de.checkpoint.ruleGrouper.CRGRule;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author gerschmann
 */
@Local
public interface CpxDBRulesManagerLocal {

//    CRGRule[] getAllRules(List<CrgRules> cpxRules);
    CrgRules findRuleById(long pRId);

    CRGRule[] getAllRules(List<CrgRules> cpxRules, PoolTypeEn ruleType);
}
