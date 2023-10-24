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
import javax.ejb.Stateless;

/**
 * Lock service implementation Should use some common implementation? Where are
 * rule locks stored? in commondb??
 *
 * @author wilde
 */
@Stateless
public class RuleLockBean implements RuleLockBeanRemote {

    @Override
    public void lock(long pPoolId, long pRuleId) throws LockException {
//        MockUpData.instance().lockRule(pPoolId, pRuleId);
    }

    @Override
    public void unlock(long pPoolId, long pRuleId) {
//        MockUpData.instance().unLockRule(pPoolId, pRuleId);
    }

    @Override
    public Boolean hasLock(long pPoolId, long pRuleId) {
        return false;//MockUpData.instance().hasLockRule(pPoolId, pRuleId);
    }

    @Override
    public void clearLocks() {
//        MockUpData.instance().clearLockMap();
    }

    @Override
    public void clearLocksInPool(long pPoolId) {
//        MockUpData.instance().clearLockRuleForPool(pPoolId);
    }

}
