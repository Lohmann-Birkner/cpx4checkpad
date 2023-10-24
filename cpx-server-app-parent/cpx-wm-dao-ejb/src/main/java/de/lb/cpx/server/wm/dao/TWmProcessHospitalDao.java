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
 *    2016  Husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.wm.dao;

import de.lb.cpx.server.dao.AbstractCpxDao;
import de.lb.cpx.server.generator.SequenceNumberGenerator;
import de.lb.cpx.wm.model.TWmProcessHospital;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Husser
 */
@Stateless
public class TWmProcessHospitalDao extends AbstractCpxDao<TWmProcessHospital> {

    @EJB
    private SequenceNumberGenerator numberGenerator;

    public TWmProcessHospitalDao() {
        super(TWmProcessHospital.class);
    }

    /**
     * persist the twmprocess entity and sets a new workflow number generated by
     * the workflowNumberGenerator
     *
     * @param pProcess process to store
     */
    @Override
    public void persist(TWmProcessHospital pProcess) {
        pProcess.setWorkflowNumber(numberGenerator.generateNextWorkflowNumberValue());
        super.persist(pProcess);
    }
}