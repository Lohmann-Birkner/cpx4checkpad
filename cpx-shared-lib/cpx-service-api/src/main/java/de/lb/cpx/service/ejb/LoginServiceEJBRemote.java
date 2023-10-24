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
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.service.properties.RoleProperties;
import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.annotation.security.PermitAll;
import javax.ejb.Remote;
import javax.security.auth.login.LoginException;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author Dirk Niemeier
 */
@Remote
@SecurityDomain("cpx")
@PermitAll
public interface LoginServiceEJBRemote {

    /**
     * List of all CheckpointX Case Databases (does not depend from user!)
     *
     * @return List of CPX Databases
     */
    //public Map<String, Integer> getCpxDatabases();
    /**
     * Is the user logged in?
     *
     * @return User already logged in?
     */
    boolean isLoggedIn();

    /**
     * Returns the CPX Rights object of the actual role
     *
     * @return CPX Rights Object
     */
    RoleProperties getActualRoleProperties();

    /**
     * Returns the CPX Rights object of a specific role
     *
     * @param pRoleId Role-ID
     * @return CPX Rights Object
     */
    RoleProperties getRoleProperties(final Long pRoleId);
    
    int getNewClientId();

    boolean loginForExternalCall();

    /**
     * get help file (PDF) from server
     *
     * @return CPX help document
     * @throws IOException document is missing or corrupted
     */
    byte[] getHelpDocumentContent() throws IOException;

    /**
     * Logon Throws LoginException when the given dates are invalid or when the
     * user is not valid any more (i.e. user disabled, account expired)
     *
     * @param pClientId Client-ID
     * @param pUserName Username
     * @param pHashedPassword Hashed Password
     * @param pDatabase Database
     * @param cpxClientSystemProperties Client properties (Java version and so
     * on)
     * @return was login successful?
     * @throws javax.security.auth.login.LoginException Login failed
     */
    //public boolean doLogin(final String pUser, final String pPassword, final String pDatabase) throws LoginException;
    /**
     * Logon Throws LoginException when the given dates are invalid or when the
     * user is not valid any more (i.e. user disabled, account expired)
     *
     * @param pClientId Client-ID
     * @param pUserName Username
     * @param pHashedPassword Hashed Password
     * @param cpxClientSystemProperties Client properties (Java version and so
     * on)
     * @param pAppTypeEn type of cpx client
     * @return was login successful?
     * @throws javax.security.auth.login.LoginException Login failed
     */
    boolean login(final String pClientId, final String pUserName, final String pHashedPassword, final CpxSystemPropertiesInterface cpxClientSystemProperties, final AppTypeEn pAppTypeEn) throws LoginException;

    /**
     * Logon Throws LoginException when the given dates are invalid or when the
     * user is not valid any more (i.e.user disabled, account expired)
     *
     * @param pClientId Client-ID
     * @param pUserName Username
     * @param pHashedPassword Hashed Password
     * @param pDatabase Database
     * @param cpxClientSystemProperties Client properties (Java version and so
     * on)
     * @param pAppTypeEn type of cpx client
     * @return was login successful?
     * @throws javax.security.auth.login.LoginException Login failed
     */
    boolean login(final String pClientId, final String pUserName, final String pHashedPassword, final String pDatabase, final CpxSystemPropertiesInterface cpxClientSystemProperties, final AppTypeEn pAppTypeEn, boolean changeRole) throws LoginException;

    //@PermitAll
    //public boolean check(final String pDatabase, final CpxSystemPropertiesInterface cpxClientSystemProperties) throws LoginException;
    /**
     * Logout
     *
     * @return was logout successful?
     */
    boolean doLogout();

    /**
     * Name of registered user
     *
     * @return Name of user which is logged in
     */
    String getUserName();

    /**
     * User
     *
     * @return User
     */
    CdbUsers getCdbUser();

    /**
     * Identity of registered user
     *
     * @return Database ID of user which is logged in
     */
    long getUserId();

    /**
     * Name of actual selected role
     *
     * @return Name of actual role of the user which is logged in
     */
    String getActualRoleName();

    /**
     * Returns the ID of the actual role
     *
     * @return Actual Role-ID
     */
    long getActualRoleId();

    /**
     * Returns the CdbUserRoles object of the actual role
     *
     * @return CdbUserRoles
     */
    CdbUserRoles getActualRole();

    /**
     * Returns the connection string of the actual database (e.g.
     * dbsys1:CPX_DEV1)
     *
     * @return Actual database
     */
    String getActualDatabase();

    /**
     * Sets the connection string of the actual database (e.g. dbsys1:CPX_DEV1)
     *
     * @param pDatabase Database
     * @return Was stored successfully?
     */
    boolean setActualDatabase(String pDatabase);

    /**
     * List of roles for the registered user
     *
     * @return List of roles of the user which is logged in
     */
    Map<Long, String> getRoleNames();

    /**
     * Returns a list of CdbUserRoles objects
     *
     * @return List of CdbUserRoles
     */
    List<CdbUserRoles> getRoles();

    /**
     * Sets the actual role
     *
     * @param pName Role name
     * @return was changing role successful?
     */
    boolean setActualRole(final String pName);

    /**
     * Sets the actual role
     *
     * @param pRoleId Role ID
     * @return was changing role successful?
     */
    boolean setActualRole(final Long pRoleId);

    /**
     * Date of logon
     *
     * @return When did the user logged in?
     */
    Calendar getLoginDate();

    /**
     * Returns a map of CPX user (ID in CdbUsers =&gt; Name for Login)
     *
     * @return Map of Login names
     */
    Map<Long, String> getUserNames();

    /**
     * Object with rights of the registered user (depends on actual selected
     * role!)
     *
     * @param pDatabase the database to update
     * @param pIsTest indicates that this methods is called during Unit Test
     * @return Exception
     */
    Exception updateCaseDb(final String pDatabase, final boolean pIsTest);

    /**
     * change database for current user on the server
     *
     * @param pClientId client id
     * @param pDatabase database to change to
     * @return if change was successful
     * @throws LoginException exception if database could not be changed
     */
    boolean changeDatabase(String pClientId, String pDatabase) throws LoginException;

    /**
     * @return map of all uesers, used by menu cache to show users
     */
    Map<Long, UserDTO> getUsers();
    
    String getActualRolePropertiesAsString();
    
    long getActualUserId();

}
