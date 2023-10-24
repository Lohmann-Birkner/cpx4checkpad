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

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.login.LoginFXMLController;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.enums.SearchListTypeEn;
import de.lb.cpx.service.ejb.LoginServiceEJBRemote;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;

/**
 * Login Task to setup settings
 *
 * @author wilde
 */
public class LoginTask extends CpxTask<Void> {

    private static final Logger LOG = Logger.getLogger(LoginTask.class.getName());

    private final String pw;
    private final String user;
    private final String database;
    private final boolean changeRole;
    /**
     * creates new instance
     *
     * @param pUser user name
     * @param pPw password
     * @param pDatabase database to connect to
     */
    public LoginTask(String pUser, String pPw, String pDatabase, boolean pChangeRole) {
        super();
        database = pDatabase;
        user = pUser;
        pw = pPw;
        changeRole = pChangeRole;
    }

    @Override
    protected Void call() throws Exception {
        LOG.log(Level.INFO, "Perform login for user '" + user + "' on database '" + database + "'...");
        boolean loginResult = Session.instance().getEjbConnector().doCpxLogin(user, pw, database, MainApp.getType(), changeRole);
        if (!loginResult) {
            throw new LoginException("Login Failed!");
        }
        LOG.log(Level.INFO, "Login for user '" + user + "' on database '" + database + "' was successfully performed!");
        CpxClientConfig.instance().reset();
        EjbProxy<LoginServiceEJBRemote> loginServiceBean = Session.instance().getEjbConnector().connectLoginServiceBean();

        Session.instance().setCpxActualRoleId(loginServiceBean.get().getActualRoleId());
        Session.instance().setRoleProperties(loginServiceBean.get().getRoleProperties(Session.instance().getCpxActualRoleId()));
        Session.instance().setCpxUserName(loginServiceBean.get().getUserName());
        Session.instance().setCpxUserId(loginServiceBean.get().getUserId());
        Session.instance().setCpxDatabase(database);
        Session.instance().setSelectedSearchList(SearchListTypeEn.WORKING, MenuCache.getMenuCacheSearchLists().getSelectedSearchListId(SearchListTypeEn.WORKING));
        Session.instance().setSelectedSearchList(SearchListTypeEn.WORKFLOW, MenuCache.getMenuCacheSearchLists().getSelectedSearchListId(SearchListTypeEn.WORKFLOW));
        //Session.instance().getCaseCount(true);
        //Session.instance().getProcessCount(true);
        Session.instance().setDatabaseInfo(null);
        Session.instance().setCdbUser(loginServiceBean.get().getCdbUser());

        Session.instance().setCpxLocale(CpxClientConfig.instance().getUserLanguage());
        LoginFXMLController.setLanguage(Session.instance().getCpxLocale());
        CpxClientConfig.instance().setUserName(Session.instance().getCpxUserName());
        if (MainApp.getNeedsDatabase()) {
            CpxClientConfig.instance().setLastSessionDatabase(database);
        }

        //LOG.log(Level.INFO, "User-ID of '" + user + "' is " + Session.instance().getCpxUserId() + " and the actual role id is " + Session.instance().getCpxActualRoleId());
        LOG.log(Level.INFO, Session.instance().toString());

        return null;
    }

}
