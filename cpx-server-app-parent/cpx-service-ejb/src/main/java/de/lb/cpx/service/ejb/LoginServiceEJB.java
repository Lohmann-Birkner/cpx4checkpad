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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.enums.AppTypeEn;
import de.lb.cpx.app.crypter.PasswordDecrypter;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.exceptions.CpxLoginException;
import de.lb.cpx.hash.generator.HashGenerator;
import de.lb.cpx.rule.util.XMLHandler;
import de.lb.cpx.server.auth.CaseEntityManagerFactoryCreator;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.auth.CpxUser;
import de.lb.cpx.server.commonDB.dao.CdbUserRolesDao;
import de.lb.cpx.server.commonDB.dao.CdbUsersDao;
import de.lb.cpx.server.commonDB.model.CdbUser2Role;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.service.properties.RoleProperties;
import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import de.lb.cpx.updatedb.UpdateDbBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.IOUtils;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author Dirk Niemeier
 */
@Stateful
@LocalBean
@SecurityDomain("cpx")
@PermitAll
public class LoginServiceEJB implements LoginServiceEJBRemote {

    private static final Logger LOG = Logger.getLogger(LoginServiceEJB.class.getName());

    @Inject
    private License lic;

    @Inject
    private CdbUsersDao cdbUsersDao;
    @Inject
    private CdbUserRolesDao cdbUserRolesDao;
//    @Inject
//    private CWmListReminderSubjectDao reminderSubjectDao;
    @Inject
    private UpdateDbBean updateDbBean;

    @Override
    public boolean login(final String pClientId, final String pUserName, final String pHashedPassword, final String pDatabase, final CpxSystemPropertiesInterface pCpxClientSystemProperties
            , final AppTypeEn pAppTypeEn, boolean changeRole) throws LoginException {
//        if (lic == null || !lic.isValid()) {
        if (lic == null) {
            LOG.log(Level.SEVERE, "License is null " + "To use CPX, please buy a valid CPX License!");
            throw new CpxLoginException(Lang.LOGIN_FAILED_NO_LICENSE, Lang.getLoginFailedNoLicense()); //"Keine CPX-Lizenz gefunden"
        } else if (!lic.isValid()) {
            LOG.log(Level.SEVERE, "Login failed for user '" + pUserName + "': License is invalid since " + lic.getValidDate());
            throw new CpxLoginException(Lang.LOGIN_FAILED_NO_VALID_LICENSE, Lang.getLoginFailedNoValidLicense(lic.getValidDate())); //"Keine CPX-Lizenz gefunden"
        } else {
            LOG.log(Level.FINE, "Will check credentials for user '" + pUserName + "' now. Valid license was found: " + String.valueOf(lic));
        }

        String database = (pDatabase == null) ? "" : pDatabase.trim();
        String clientId = (pClientId == null) ? "" : pClientId.trim();

        final boolean needsDatabase = pAppTypeEn != null && pAppTypeEn.getNeedsDatabase();
        if (needsDatabase) {
            if (database.isEmpty()) {
                throw new CpxLoginException(Lang.LOGIN_FAILED_NO_DATABASE_SELECTED, Lang.getLoginFailedNoDatabaseSelected()); //"Es wurde keine Datenbank angegeben"
            }
            if (!ClientManager.isValidDatabase(database)) {
                LOG.log(Level.SEVERE, "Login failed for user '" + pUserName + "': Database '" + pDatabase + "' does not seem to exist");
                throw new CpxLoginException(Lang.LOGIN_FAILED_DATABASE_DOES_NOT_EXIST, Lang.getLoginFailedDatabaseDoesNotExist(database)); //"Datenbank '" + database + "' existiert nicht"
            }
            //2017-10-23 DNi: Be verbose if database is corrupted or is blown with the wind for any reason
            try {
                updateCaseDb(database);
                CaseEntityManagerFactoryCreator.fetchEntityManagerFactory(database);
            } catch (IllegalArgumentException ex) {
                String errorMsg = (ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                LOG.log(Level.SEVERE, "Login failed for user '" + pUserName + "': Was not able to connect to database '" + pDatabase + "'. Database is probably corrupted: " + errorMsg, ex);
                throw new CpxLoginException(Lang.LOGIN_FAILED_DATABASE_IS_NOT_AVAILABLE, Lang.getLoginFailedDatabaseIsNotAvailable(database, errorMsg)); //"Datenbank '" + database + "' existiert nicht"
            }
        }

        boolean success = login(pClientId, pUserName, pHashedPassword, pCpxClientSystemProperties, pAppTypeEn);
        CpxUser lCpxUser = ClientManager.getCpxClient(clientId);
        //2019-04-12 RSH: CPX-1535 permission on database
        RoleProperties prop = getActualRoleProperties();
        
        if (needsDatabase) {
            if (prop == null || !prop.isDatabaseAllowed(pDatabase)) {
                //RoleProperties newProp = null;
                CdbUserRoles newRole = null;
                //automatically change actual role
//                if (lCpxUser != null) {
                for (CdbUserRoles role : lCpxUser.getRoles()) {
                    if (role == null || !role.isValid()) {
                        continue;
                    }
                    RoleProperties p = cdbUserRolesDao.getUserRoleProperties(role.id);
                    if (p != null && p.isDatabaseAllowed(pDatabase)) {
                        //RoleProperties newProp = p;
                        newRole = role;
                        if(changeRole){
                            lCpxUser.setActualRole(role.id);
                        }
                        break;
                    }
                }
//                }
                
                if (newRole == null) {
                   success = false;
                    throw new CpxLoginException(Lang.LOGIN_FAILED_DATABASE_NO_RIGHT, Lang.getLoginFailedDatabaseNoRight((prop == null ? "(no role selected)" : prop.getName()), database));
                }else{
                    if(!changeRole){
                        success = false;
                        throw new CpxLoginException(newRole.getCdburName(), 
                                Lang.getLoginFailedDatabaseNoRight( prop.getName(), database) +"\n"+
                                Lang.getLoginFailedDatabaseNoRightOtherRole(newRole.getCdburName()) + "\n\n" +
                                Lang.getLoginFailedDatabaseNoRightOtherRole1()
                        );
                    }
                    
                }
            }
            if (success && lCpxUser != null) {
                lCpxUser.setActualDatabase(database);
            }
        }
        LOG.log(Level.INFO, "Login " + (success ? "successful" : "failed") + " for user '" + pUserName + "' (User-ID: " + (lCpxUser == null ? "null" : lCpxUser.getUserId()) + ", Client-ID " + pClientId + ") on database '" + database + "'");
        return success;
    }

    /*
     Only use this method for JUnit Test 
     if you change updateCaseDb(final String pDatabase) you need to change this method aswell
     */
    @Override
    public Exception updateCaseDb(String pDatabase, boolean pIsTest) {
        if (pIsTest) {
            String database = (pDatabase == null) ? "" : pDatabase.trim();
            if (database.isEmpty()) {
                return new IllegalArgumentException("Database is empty");
            }
            Connection connection = null;
            try {
                connection = CaseEntityManagerFactoryCreator.getJdbcConnection(pDatabase);
                connection.setAutoCommit(false);
                CpxSystemPropertiesInterface props = CpxSystemProperties.getInstance();
                String updateCaseDbFilename = props.getCpxServerDbUpdateCaseDbFile();
                String updateCaseDbViewsFile = props.getCpxServerDbUpdateCaseDbViewsFile();
                updateDbBean.startDbUpdate(connection, updateCaseDbFilename, updateCaseDbViewsFile);
                connection.commit();
            } catch (IllegalArgumentException | IOException | SQLException | ParseException ex) {
                if (connection != null) {
                    try {
                        connection.rollback();
                    } catch (SQLException ex1) {
                        Logger.getLogger(LoginServiceEJB.class.getName()).log(Level.SEVERE, null, ex1);
                        return ex1;
                    }
                }
                Logger.getLogger(LoginServiceEJB.class.getName()).log(Level.SEVERE, null, ex);
                return ex;
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LoginServiceEJB.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }
            }
        } else {
            return new IllegalArgumentException("pIsTest is false");
        }
        return null;
    }

    public synchronized void updateCaseDb(final String pDatabase) {
        String database = (pDatabase == null) ? "" : pDatabase.trim();
        if (database.isEmpty()) {
            return;
        }
        //if (CaseEntityManagerFactoryCreator.isNew(pDatabase)) {
        Connection connection = null;
        try {
            connection = CaseEntityManagerFactoryCreator.getJdbcConnection(pDatabase);
            connection.setAutoCommit(false);
            CpxSystemPropertiesInterface props = CpxSystemProperties.getInstance();
            String updateCaseDbFilename = props.getCpxServerDbUpdateCaseDbFile();
            String updateCaseDbViewsFile = props.getCpxServerDbUpdateCaseDbViewsFile();
            updateDbBean.startDbUpdate(connection, updateCaseDbFilename, updateCaseDbViewsFile);
            connection.commit();
        } catch (IllegalArgumentException | IOException | SQLException | ParseException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(LoginServiceEJB.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            Logger.getLogger(LoginServiceEJB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginServiceEJB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //}
    }

    protected CpxUser getCpxUser() {
        return ClientManager.getCurrentCpxUser();
    }

    @Override
    public RoleProperties getActualRoleProperties() {
        if (getCpxUser() == null) {
            return null;
        }
        RoleProperties roleProperties = getCpxUser().getActualRoleProperties();
        return roleProperties;
    }

    @Override
    public RoleProperties getRoleProperties(final Long pRoleId) {
        if (getCpxUser() == null) {
            return null;
        }
        RoleProperties roleProperties = getCpxUser().getRoleProperties(pRoleId);
        return roleProperties;
    }

    @Override
    public boolean isLoggedIn() {
        return getCpxUser() != null;
    }

    @Override
    public synchronized boolean doLogout() {
        if (!isLoggedIn()) {
            return false;
        }
        return ClientManager.removeCurrentCpxUser();
    }

    @Override
    public String getUserName() {
        if (!isLoggedIn()) {
            return "";
        }
        return getCpxUser().getUserName();
    }

    @Override
    public long getUserId() {
        if (!isLoggedIn()) {
            return 0L;
        }
        return getCpxUser().getUserId();
    }

    @Override
    public synchronized CdbUserRoles getActualRole() {
        if (!isLoggedIn()) {
            return null;
        }
        return getCpxUser().getActualRole();
    }

    @Override
    public String getActualRoleName() {
        if (!isLoggedIn()) {
            return "";
        }
        return getCpxUser().getActualRoleName();
    }

    @Override
    public long getActualRoleId() {
        if (!isLoggedIn()) {
            return 0L;
        }
        return getCpxUser().getActualRoleId();
    }

    /*
  @Override
  public Map<Long, String> getRoleNames() {
    Map<Long, String> lMap = new LinkedHashMap<>();
    if (isLoggedIn()) {
      for(CdbUserRoles lCdbUserRoles: getCpxUser().getRoles()) {
        if (lCdbUserRoles == null) {
          continue;
        }
        String lName = lCdbUserRoles.getCdburName();
        lName = (lName == null)?"":lName.trim();
        lMap.put(lCdbUserRoles.getId(), lName);
      }
    }
    return lMap;
  }
     */
    @Override
    public Map<Long, String> getRoleNames() {
        Map<Long, String> lMap = new LinkedHashMap<>();
        if (!isLoggedIn()) {
            return lMap;
        }
        lMap = getCpxUser().getRoleNames();
        return lMap;
    }

    @Override
    public List<CdbUserRoles> getRoles() {
        List<CdbUserRoles> list = new ArrayList<>();
        if (!isLoggedIn()) {
            return list;
        }
        list = getCpxUser().getRoles();
        return list;
    }

    @Override
    public synchronized String getActualDatabase() {
        if (!isLoggedIn()) {
            return "";
        }
        return getCpxUser().getActualDatabase();
    }

    @Override
    public synchronized boolean setActualDatabase(final String pDatabase) {
        if (!isLoggedIn()) {
            return false;
        }
        return getCpxUser().setActualDatabase(pDatabase);
    }

    @Override
    public synchronized boolean setActualRole(final String pName) {
        if (!isLoggedIn()) {
            return false;
        }
        return getCpxUser().setActualRole(pName);
    }

    @Override
    public synchronized boolean setActualRole(final Long pRoleId) {
        if (!isLoggedIn()) {
            return false;
        }
        return getCpxUser().setActualRole(pRoleId);
    }

    @Override
    public Calendar getLoginDate() {
        if (!isLoggedIn()) {
            return null;
        }
        return getCpxUser().getLoginDate();
    }

    private CdbUsers getUserByName(final String pUserName) {
        CdbUsers lCdbUser = cdbUsersDao.getCdbUsers(pUserName);
        return lCdbUser;
    }

    @Override
    public boolean login(final String pClientId, final String pUserName, String pHashedPassword, final CpxSystemPropertiesInterface pCpxClientSystemProperties, final AppTypeEn pAppTypeEn) throws LoginException {
        String username = (pUserName == null) ? "" : pUserName.trim();
        String hashedPassword = (pHashedPassword == null) ? "" : pHashedPassword.trim();
        //String database = (pDatabase == null)?"":pDatabase.trim();
        String clientId = (pClientId == null) ? "" : pClientId.trim();

        if (pAppTypeEn == null) {
            throw new CpxLoginException("Application type missing", "Application type missing"); //"Es wurde kein Benutzername angegeben"
        }
        if (username.isEmpty()) {
            throw new CpxLoginException(Lang.LOGIN_FAILED_USERNAME_MISSING, Lang.getLoginFailedUsernameMissing()); //"Es wurde kein Benutzername angegeben"
        }
        if (hashedPassword.isEmpty()) {
            throw new CpxLoginException(Lang.LOGIN_FAILED_PASSWORD_MISSING, Lang.getLoginFailedPasswordMissing()); //"Es wurde kein Passwort angegeben"
        }
        /*
    if (database.isEmpty()) {
      throw new CpxLoginException("Es wurde keine Datenbank angegeben");
    }
         */
        if (clientId.isEmpty()) {
            throw new CpxLoginException(Lang.LOGIN_FAILED_CLIENT_ID_MISSING, Lang.getLoginFailedClientIdMissing()); //"Es wurde keine Client ID angegeben"
        }
        /*
    if (!ClientManager.isValidDatabase(database)) {
      throw new CpxLoginException("Datenbank '" + database + "' existiert nicht");
    }
         */

        String idTmp = ClientManager.getCurrentClientId();
        String currentClientId = (idTmp == null) ? "" : idTmp.trim();

        if (!clientId.equalsIgnoreCase(currentClientId)) {
            throw new CpxLoginException(Lang.LOGIN_FAILED_CLIENT_ID_DOES_NOT_MATCH, Lang.getLoginFailedClientIdDoesNotMatch(clientId)); //"Übergebene Client ID " + clientId + " stimmt nicht mit der Client ID in der Session überein"
        }

        CdbUsers lCdbUser = getUserByName(username);
        if (lCdbUser == null) {
            throw new CpxLoginException(Lang.LOGIN_FAILED_USERNAME_INVALID, Lang.getLoginFailedUsernameInvalid()); //"Name ungültig"
        }

        if (PasswordDecrypter.getInstance().isEncrypted(lCdbUser.getUPassword())) {
            LOG.log(Level.WARNING, "This password for user '" + username + "' is encrypted with the old CP AppDecrypter mechanism!");
            LOG.log(Level.INFO, "I will upgrade password coming from Checkpoint of user '" + username + "' to the more secure version of CPX now...");
            final String plaintextPassword = PasswordDecrypter.getInstance().decrypt(lCdbUser.getUPassword());
            final String[] newSaltedHashPassword = HashGenerator.getInstance().createSaltedHashPassword(plaintextPassword);
            final String saltedHashPassword = newSaltedHashPassword[0];
            final String salt = newSaltedHashPassword[1];
            lCdbUser.setUPassword(saltedHashPassword);
            lCdbUser.setUPwSalt(salt);
            cdbUsersDao.persist(lCdbUser);
            lCdbUser = getUserByName(username);
            LOG.log(Level.INFO, "CP Password of user '" + username + "' was successfully upgraded to CPX password!");
        }

//        String saltedHash = HashGenerator.hashSalted(hashedPassword, lCdbUser.getUPwSalt());
//        if (!saltedHash.equals(lCdbUser.getUPassword())) {
//            throw new CpxLoginException(Lang.LOGIN_FAILED_PASSWORD_INVALID, Lang.getLoginFailedPasswordInvalid()); //"Passwort ungültig"
//        }
        if (!HashGenerator.getInstance().checkHashedPassword(hashedPassword, lCdbUser.getUPwSalt(), lCdbUser.getUPassword())) {
            throw new CpxLoginException(Lang.LOGIN_FAILED_PASSWORD_INVALID, Lang.getLoginFailedPasswordInvalid()); //"Passwort ungültig"
        }

        Date lNow = new Date();

        if (!lCdbUser.isUIsValid()) {
            throw new CpxLoginException(Lang.LOGIN_FAILED_ACCOUNT_INVALID, Lang.getLoginFailedAccountInvalid()); //"Benutzerkonto ist nicht mehr gültig"
        }

        if (lCdbUser.getUValidFrom() != null && lCdbUser.getUValidFrom().after(lNow)
                || lCdbUser.getUValidTo() != null && lCdbUser.getUValidTo().before(lNow)) {
            //Benutzer nicht gültig
            throw new CpxLoginException(Lang.LOGIN_FAILED_ACCOUNT_EXPIRED, Lang.getLoginFailedAccountExpired()); //"Benutzerkonto ist entweder abgelaufen oder noch nicht gültig"
        }

        //List<CdbUserRoles> lCdbUserRolesList = getActualUserRoles(lCdbUser);
        List<CdbUserRoles> lCdbUserRolesList = new ArrayList<>();

        for (CdbUser2Role lCdbUser2Roles : lCdbUser.getCdbUser2Roles()) {
            if (lCdbUser2Roles == null) {
                continue;
            }
            if (!lCdbUser2Roles.isValid()) {
                continue;
            }
//            if (lCdbUser2Roles.getU2rValidFrom() != null
//                    && lCdbUser2Roles.getU2rValidFrom().after(lNow)) {
//                //Rolle noch nicht gültig
//                continue;
//            }
//            if (lCdbUser2Roles.getU2rValidTo() != null
//                    && lCdbUser2Roles.getU2rValidTo().before(lNow)) {
//                //Rolle nicht mehr gültig
//                continue;
//            }
            CdbUserRoles lCdbUserRoles = lCdbUser2Roles.getCdbUserRoles();
            if (lCdbUserRoles != null) {
                //Manage roles centralized in ClientManager!
                ClientManager.addCpxRole(lCdbUserRoles.getId(), lCdbUserRoles);
                lCdbUserRoles = ClientManager.getCpxRole(lCdbUserRoles.getId());
                if (lCdbUserRoles != null) {
                    lCdbUserRolesList.add(lCdbUserRoles);
                }
            }
        }

        CpxUser lCpxUser = new CpxUser().init(lCdbUser, lCdbUserRolesList, clientId, pAppTypeEn);

        lCpxUser.setCpxSystemProperties(pCpxClientSystemProperties);

        RoleProperties prop = lCpxUser.getActualRoleProperties();

        final boolean ruleEditor = pAppTypeEn.isRuleEditor();
        final boolean webApp = pAppTypeEn.isWebApp();
        if (ruleEditor) {
            //check specific Rule Editor rights
            if (prop == null || !prop.isEditRuleAllowed()) {
                //RoleProperties newProp = null;
                CdbUserRoles newRole = null;
                //automatically change actual role
//                if (lCpxUser != null) {
                for (CdbUserRoles role : lCpxUser.getRoles()) {
                    if (role == null || !role.isValid()) {
                        continue;
                    }
                    RoleProperties p = cdbUserRolesDao.getUserRoleProperties(role.id);
                    if (p != null && p.isEditRuleAllowed()) {
                        //RoleProperties newProp = p;
                        newRole = role;
                        lCpxUser.setActualRole(role.id);
                        break;
                    }
                }
//                }
                if (newRole == null) {
                    //success = false;
                    throw new CpxLoginException("cannot edit rules", "Die Rolle " + (prop == null ? "(no role selected)" : prop.getName()) + " hat keine Berechtigung zum Ändern von Regeln");
                }
            }
        } else if (webApp) {
            //check specific WebApp rights
            if (prop == null || !prop.isConfigSystemAllowed()) {
                //RoleProperties newProp = null;
                CdbUserRoles newRole = null;
                //automatically change actual role
//                if (lCpxUser != null) {
                for (CdbUserRoles role : lCpxUser.getRoles()) {
                    if (role == null || !role.isValid()) {
                        continue;
                    }
                    RoleProperties p = cdbUserRolesDao.getUserRoleProperties(role.id);
                    if (p != null && p.isConfigSystemAllowed()) {
                        //RoleProperties newProp = p;
                        newRole = role;
                        lCpxUser.setActualRole(role.id);
                        break;
                    }
                }
//                }
                if (newRole == null) {
                    //success = false;
                    throw new CpxLoginException("cannot config system", "Die Rolle " + (prop == null ? "(no role selected)" : prop.getName()) + " hat keine Berechtigung zur Konfiguration des Systems");
                }
            }
        }

        ClientManager.addCpxClient(clientId, lCpxUser);

        return true;
    }

    @Override
    public Map<Long, String> getUserNames() {
        Map<Long, String> map = new LinkedHashMap<>();
        List<CdbUsers> users = cdbUsersDao.findAll();

        Collections.sort(users);
        for (CdbUsers user : users) {
            map.put(user.getId(), user.getUName());
        }
        return map;
    }

    @Override
    public CdbUsers getCdbUser() {
        return cdbUsersDao.getCopy(getUserId());
        /*
      CdbUsers cdbUser = cdbUsersDao.findById(getUserId());
      CdbUsers cdbUserTmp = new CdbUsers();
      if (cdbUser != null) {
        cdbUserTmp.setUFirstName(cdbUser.getUFirstName());
        cdbUserTmp.setULastName(cdbUser.getULastName());
        cdbUserTmp.setUName(cdbUser.getUName());
        cdbUserTmp.setUTitle(cdbUser.getUTitle());
        cdbUserTmp.setUValidFrom(cdbUser.getUValidFrom());
        cdbUserTmp.setUValidTo(cdbUser.getUValidTo());
        cdbUserTmp.setUDatabase(cdbUser.getUDatabase());
        cdbUserTmp.setURoleId(cdbUser.getURoleId());
      }
      return cdbUserTmp;
         */
    }

    @Override
    public boolean changeDatabase(String pClientId, String pDatabase) throws LoginException {
        String database = (pDatabase == null) ? "" : pDatabase.trim();
        if (database.isEmpty()) {
            throw new CpxLoginException(Lang.LOGIN_FAILED_NO_DATABASE_SELECTED, Lang.getLoginFailedNoDatabaseSelected()); //"Es wurde keine Datenbank angegeben"
        }
        if (!ClientManager.isValidDatabase(database)) {
            throw new CpxLoginException(Lang.LOGIN_FAILED_DATABASE_DOES_NOT_EXIST, Lang.getLoginFailedDatabaseDoesNotExist(database)); //"Datenbank '" + database + "' existiert nicht"
        }
        //2019-04-12 RSH: CPX-1535 permission on database
        RoleProperties prop = getActualRoleProperties();
        if (prop == null || !prop.isDatabaseAllowed(pDatabase)) {
            throw new CpxLoginException(Lang.LOGIN_FAILED_DATABASE_NO_RIGHT, Lang.getLoginFailedDatabaseNoRight(database));
        }
        //2017-10-23 DNi: Be verbose if database is corrupted or is blown with the wind for any reason
        try {
            updateCaseDb(database);
            CaseEntityManagerFactoryCreator.fetchEntityManagerFactory(database);
        } catch (IllegalArgumentException ex) {
            String errorMsg = (ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
            LOG.log(Level.SEVERE, "Was not able to connect to database '" + pDatabase + "'. Database is probably corrupted: " + errorMsg, ex);
            throw new CpxLoginException(Lang.LOGIN_FAILED_DATABASE_IS_NOT_AVAILABLE, Lang.getLoginFailedDatabaseIsNotAvailable(database, errorMsg)); //"Datenbank '" + database + "' existiert nicht"
        }
        CpxUser lCpxUser = ClientManager.getCpxClient(pClientId);
        if (lCpxUser == null) {
            throw new LoginException("No matching Client ID Found!");
        }
        lCpxUser.setActualDatabase(database);
        return true;
    }

    @Override
    public Map<Long, UserDTO> getUsers() {
        return cdbUsersDao.getUsers();
//        Map<Long, UserDTO> map = new LinkedHashMap<>();
//        List<CdbUsers> users = cdbUsersDao.getAllUsers();
//
//        Collections.sort(users);
//        for (CdbUsers user : users) {
//            //String name = user.getUFullName();
//            String fullName = user.getUFullName();
//            map.put(user.getId(), new UserDTO(user.getId(), fullName, user.getUName(), user.isUIsValid(), user.isUIsDeleted()));
//        }
//        return map;
    }

    @Override
    public int getNewClientId() {
        return ClientManager.getNewClientId();
    }

    @Override
    public boolean loginForExternalCall() {
        final String clientId = String.valueOf(ClientManager.getNewClientId());
        final String loginname = "batchJob";
        final String password = "masterkey";
        final String hashedPassword = HashGenerator.getInstance().hash(password);
        final AppTypeEn appType = AppTypeEn.CLIENT;
        ClientManager.manipulateUserPrincipal(clientId);
        try {
            login(clientId, loginname, hashedPassword, CpxSystemProperties.getInstance(), appType);
        } catch (LoginException ex) {
            LOG.log(Level.SEVERE, "Unable to create login for external application call", ex);
            return false;
        }
        return true;
    }

    @Override
    public byte[] getHelpDocumentContent() throws IOException {
        final File helpFile = new File(CpxSystemProperties.getInstance().getCpxServerHelpFile());
        if (!helpFile.exists() || !helpFile.isFile()) {
            throw new IOException("Help file not found: " + helpFile.getAbsolutePath());
        }
        if (!helpFile.canRead()) {
            throw new IOException("No permission to read from help file: " + helpFile.getAbsolutePath());
        }
        final byte[] content;
        try (FileInputStream in = new FileInputStream(helpFile)) {
            content = IOUtils.toByteArray(in);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot read file: " + helpFile.getAbsolutePath(), ex);
            throw ex;
        }

        if (content == null || content.length == 0) {
            LOG.log(Level.SEVERE, "Help file has no content: " + helpFile.getAbsolutePath());
        }

        return content;
    }
    
    @Override
    public String getActualRolePropertiesAsString() {
        if (getCpxUser() == null) {
            return null;
        }
        String rolePropertiesString = "";
        RoleProperties roleProperties = getCpxUser().getActualRoleProperties();
        if(roleProperties!=null){
            try{
                rolePropertiesString = XMLHandler.marshalXML(roleProperties, RoleProperties.class);
            }catch(JAXBException ex){
                LOG.log(Level.SEVERE, "Can't build String from RoleProperties");
            }
        }       
        
        return rolePropertiesString;
    }
    
    @Override
    public long getActualUserId() {
        if (getCpxUser() == null) {
            return 0;
        }else{
            return getCpxUser().getUserId();
        }    
    }
}
