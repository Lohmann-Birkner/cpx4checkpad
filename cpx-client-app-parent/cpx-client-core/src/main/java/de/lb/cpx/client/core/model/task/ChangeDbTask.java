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
package de.lb.cpx.client.core.model.task;

import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;

/**
 * Task to change database for user
 *
 * @author wilde
 */
public class ChangeDbTask extends CpxTask<Void> {

    private static final Logger LOG = Logger.getLogger(ChangeDbTask.class.getName());

    private final String database;

    /**
     * creates new instance
     *
     * @param pDatabase database to connect to
     */
    public ChangeDbTask(String pDatabase) {
        super();
        database = pDatabase;
    }

    @Override
    protected Void call() throws Exception {
        boolean changeResult = Session.instance().getEjbConnector().doChangeDatabase(database);
        if (!changeResult) {
            throw new LoginException("Change Failed!");
        }
        Session.instance().setCpxDatabase(database);
        CpxClientConfig.instance().setLastSessionDatabase(database);
        LOG.log(Level.INFO, "New Database: " + database + " for User-ID: " + Session.instance().getCpxUserId());

        return null;
    }
}
