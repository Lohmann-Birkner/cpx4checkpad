/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.checkpoint.ruleGrouper.cpx;

import de.checkpoint.drg.RulerInputObjectNull;
import de.checkpoint.ruleGrouper.CRGRule;
import de.checkpoint.ruleGrouper.CheckpointRuleGrouper;

/**
 * overrides the Checkpoint class for grouping and rules applying for CPX
 *
 * @author gerschmann
 */
public class CPXRuleGrouper extends CheckpointRuleGrouper {

    public CPXRuleGrouper() {
        super();
    }

    /**
     * Overrides the Constructor of CheckpointRuleGrouper
     *
     * @param catalogPath path to the Directory of the drgm - files
     * @param rules rules, that were read by start of the service
     * @param usePepp flag to use PEPP
     * @param useESKA flag to use eska will be used later
     * @throws Exception something went wrong
     */
    public CPXRuleGrouper(String catalogPath, CRGRule[] rules,
            boolean usePepp, boolean useESKA) throws Exception {
        // initialises the grouper
        super(new RulerInputObjectNull(), catalogPath, usePepp);
        setRuleList(rules);
    }

    /**
     * need to use this Method to read the Rules from path instead of the
     * protected CheckpointRuleGrouper::initRuler because of usage of
     * CPXFileManager, which overrides the
     * CRGCheckpointGrouperFileManager::getRulesInputStream method in order not
     * to touch the old checkpoint classes
     *
     * @param rules all rules
     *
     */
    protected void initRuler(CRGRule[] rules) {

        this.resetRulesArrays();
        m_allRulesWithYears[ALL_RULES_INDEX] = rules;
        distributeRules();
    }

    /**
     * sets the defined Rules pile
     *
     * @param rules Array of rules
     */
    public void setRuleList(CRGRule[] rules) {
        initRuler(rules);
    }

    public int getRuleCount() {
        return m_allRulesWithYears[ALL_RULES_INDEX].length;
    }
}
