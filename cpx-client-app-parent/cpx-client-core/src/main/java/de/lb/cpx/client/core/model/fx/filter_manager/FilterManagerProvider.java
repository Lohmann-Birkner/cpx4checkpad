/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Bohm - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.filter_manager;

import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.shared.filter.enums.RuleListAttributes;
import de.lb.cpx.shared.filter.enums.WorkflowListAttributes;
import de.lb.cpx.shared.filter.enums.WorkingListAttributes;

/**
 *
 * @author Bohm
 */
public class FilterManagerProvider {

    private static FilterManagerProvider instance;

    public static synchronized FilterManagerProvider instance() {
        if (instance == null) {
            instance = new FilterManagerProvider();
        }
        return instance;
    }

    private final FilterManager workingListFilterManager;
    private final FilterManager workflowListFilterManager;
    private final FilterManager ruleListFilterManager;

    private FilterManagerProvider() {
        workingListFilterManager = new FilterManager(SearchListTypeEn.WORKING, WorkingListAttributes.instance());
        workflowListFilterManager = new FilterManager(SearchListTypeEn.WORKFLOW, WorkflowListAttributes.instance());
        ruleListFilterManager = new FilterManager(SearchListTypeEn.RULE, RuleListAttributes.instance());
    }

    /**
     * @return the workingList_FilterManager
     */
    public FilterManager getWorkingListFilterManager() {
        return workingListFilterManager;
    }

    /**
     * @return the workflowList_FilterManager
     */
    public FilterManager getWorkflowListFilterManager() {
        return workflowListFilterManager;
    }

    /**
     * @return the ruleListFilterManager
     */
    public FilterManager getRuleListFilterManager() {
        return ruleListFilterManager;
    }

}
