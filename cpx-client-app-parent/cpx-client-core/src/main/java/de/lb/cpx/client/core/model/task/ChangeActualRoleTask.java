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
 *    2019  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.task;

import de.lb.cpx.client.core.config.Session;
import java.util.logging.Logger;

/**
 * Task to change userActualRole for user
 *
 * @author shahin
 */
public class ChangeActualRoleTask extends CpxTask<Void> {

    private static final Logger LOG = Logger.getLogger(ChangeActualRoleTask.class.getName());

    private final long userActualRole;

    /**
     * creates new instance
     *
     * @param pUserRole userActualRole
     */
    public ChangeActualRoleTask(long pUserRole) {
        super();
        userActualRole = pUserRole;
    }

    @Override
    protected Void call() throws Exception {
        Session.instance().setCpxActualRoleId(userActualRole);
//        Session.instance().setCpxActualRoleId(userActualRole);
        Session.instance().setRoleProperties(Session.instance().getEjbConnector().connectLoginServiceBean().get().getRoleProperties(userActualRole));
        Session.instance().getEjbConnector().connectLoginServiceBean().get().setActualRole(userActualRole);

        return null;
    }
}
