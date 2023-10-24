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
package de.lb.cpx.server.rule.services;

import de.lb.cpx.shared.dto.LockException;
import javax.ejb.Remote;

/**
 *
 * @author wilde
 */
@Remote
public interface RuleLockBeanRemote {

    /**
     * lock rule
     *
     * @param pPoolId rulePoolId
     * @param pRuleId ruleId
     * @throws de.lb.cpx.shared.dto.LockException if database is already locked
     */
    void lock(long pPoolId, long pRuleId) throws LockException;

    /**
     * unlock rule
     *
     * @param pPoolId rulePoolId
     * @param pRuleId ruleId
     */
    void unlock(long pPoolId, long pRuleId);

    /**
     * check if rule is already locked
     *
     * @param pPoolId rulePoolId
     * @param pRuleId ruleId
     * @return indicator if rule is locked for pool
     */
    Boolean hasLock(long pPoolId, long pRuleId);

    /**
     * clear all locks on rules in all pools
     */
    void clearLocks();

    void clearLocksInPool(long pPoolId);

}
