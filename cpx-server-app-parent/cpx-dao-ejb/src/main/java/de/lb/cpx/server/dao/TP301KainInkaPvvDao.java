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

import de.lb.cpx.model.TP301KainInkaPvv;
import javax.ejb.Stateless;

/**
 *
 * @author Shahin
 */
@Stateless
public class TP301KainInkaPvvDao extends AbstractCpxDao<TP301KainInkaPvv> {

    public TP301KainInkaPvvDao() {
        super(TP301KainInkaPvv.class);
    }

}
