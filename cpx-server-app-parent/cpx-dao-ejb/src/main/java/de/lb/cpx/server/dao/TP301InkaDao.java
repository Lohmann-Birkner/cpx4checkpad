/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2017  Shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.dao;

import de.lb.cpx.model.TP301Inka;
import de.lb.cpx.server.generator.SequenceNumberGenerator;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Shahin
 */
@Stateless
public class TP301InkaDao extends AbstractCpxDao<TP301Inka> {

    @EJB
    private SequenceNumberGenerator numberGenerator;

    public TP301InkaDao() {
        super(TP301Inka.class);
    }

    public Long createNextSendingInkaNumberValue() {
        return numberGenerator.generateNextSendingInkaNumberValue();
    }

//    @Override
//    public void persist(TP301Inka inka) {
//        if (inka.getCurrentNrSending() == 0L) {
//            inka.setCurrentNrSending(createNextSendingInkaNumberValue());
//        }
//        super.persist(inka);
//    }
}
