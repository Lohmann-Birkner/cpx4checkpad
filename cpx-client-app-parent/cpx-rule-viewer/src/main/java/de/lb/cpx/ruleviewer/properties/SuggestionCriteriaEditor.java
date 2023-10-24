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
package de.lb.cpx.ruleviewer.properties;

import de.lb.cpx.rule.criteria.CriteriaManager;
import de.lb.cpx.rule.criteria.model.Criteria;
import java.util.List;
import org.controlsfx.control.PropertySheet;

/**
 * Suggestion Criteria editor, other list is required as in term criteria
 *
 * @author wilde
 */
public class SuggestionCriteriaEditor extends BasicCriteriaEditor {

    public SuggestionCriteriaEditor(PropertySheet.Item property) {
        super(property);
    }

    @Override
    public List<Criteria> getCriteriaList() {
        return CriteriaManager.instance().getAllSuggAvailableCriteria();
    }

}
