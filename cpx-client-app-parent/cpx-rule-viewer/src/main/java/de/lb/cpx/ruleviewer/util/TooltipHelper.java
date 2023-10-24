/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.util;

import de.lb.cpx.rule.criteria.CriteriaHelper;
import de.lb.cpx.rule.criteria.model.CriterionTree;
import java.util.List;

/**
 * Class to get the tooltip representation of Criterias/Groups/Supergroups
 *
 * @author wilde
 */
public class TooltipHelper {

    public static String getCriteriaTooltipText(List<de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion.Tooltip> pTips) {
        String text = "";
        for (de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion.Tooltip tip : pTips) {
            if (isEnum(tip)) {
                text = concat(text, CriteriaHelper.getTooltipDescription(tip));
            } else {
                text = concat(text, tip.getCpname());
            }
        }
        return text;
    }

    public static String getGroupTooltipText(List<de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Tooltip> pTips) {
        String text = "";
        for (de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Tooltip tip : pTips) {
            text = concat(text, tip.getCpname());
        }
        return text;
    }

    public static String getSuperGroupTooltipText(List<de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Tooltip> pTips) {
        String text = "";
        for (de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Tooltip tip : pTips) {
            text = concat(text, tip.getCpname());
        }
        return text;
    }

    private static String concat(String pText, String pContact) {
        return pText.concat("-" + pContact + "\n");
    }

    private static boolean isEnum(CriterionTree.Supergroup.Group.Criterion.Tooltip tip) {
        return tip.getValue() != null;
    }
    

}
