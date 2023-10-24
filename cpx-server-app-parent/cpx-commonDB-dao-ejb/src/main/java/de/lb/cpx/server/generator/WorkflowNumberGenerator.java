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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.generator;

import de.lb.cpx.server.commonDB.dao.CdbSequenceDao;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Custom generator for the workflow number Decides what building strategy for
 * the number is to be used
 *
 * @author wilde
 */
@Stateless
public class WorkflowNumberGenerator {

    @EJB
    private CdbSequenceDao seqDao;

//    /**
//     * generate next value of the number value is updated in the database TODO:
//     * needs to be tested, awi 20170106
//     *
//     * @return newly generated value
//     */
//    public Long generateNextValue() {
//        CdbSequence sequence = seqDao.getLastWorkflowNumber();
//        try {
//            Integer nextVal = Integer.parseInt(sequence.getWorkflowNumberSeq()) + 1;
//            sequence.setWorkflowNumberSeq(String.valueOf(nextVal));
//            seqDao.merge(sequence);
//            return nextVal.longValue();
//        } catch (NumberFormatException ex) {
//            LOG.severe("Can not compute WorkflowNumber, reason " + ex.getMessage());
//        }
//
//        return null;
//    }
    /**
     * generate next value of the number value is updated in the database TODO:
     * needs to be tested, awi 20170106
     *
     * @return newly generated value
     */
    public long generateNextValue() {
        return seqDao.getNextWorkflowNumber();
    }

}
