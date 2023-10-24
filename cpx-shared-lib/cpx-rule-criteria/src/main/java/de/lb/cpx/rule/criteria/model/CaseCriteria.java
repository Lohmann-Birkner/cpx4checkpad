/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 * Contributors:
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.rule.criteria.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wilde
 */
public class CaseCriteria extends Criteria {

    @Override
    public Map<String, Supergroup.Group.Criterion> criterionMap() {
        if (criterionMap == null) {
            criterionMap = new HashMap<>();
            for (Supergroup.Group group : getSupergroup().getGroup()) {
//                if(group.getName().equals("rules.txt.group.labor.dis")){
//                    continue;
//                }
//                if(group.getName().equals("rules.GKMedicineNode.medicine")){
//                    continue;
//                }
                for (Supergroup.Group.Criterion criterion : group.getCriterion()) {
                    criterionMap.put(criterion.getCpname(), criterion);
                }
            }
        }
        return criterionMap;
    }

}
