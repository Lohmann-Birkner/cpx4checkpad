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

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.server.commonDB.model.CdbUser2Role;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.service.information.CpxDatabase;
import de.lb.cpx.service.information.CpxPersistenceUnit;
import de.lb.cpx.service.information.DatabaseInfo;
import de.lb.cpx.service.properties.RoleProperties;
import de.lb.cpx.shared.dto.CpxSession;
import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.shared.version.Version;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ejb.Remote;

/**
 *
 * @author niemeier, sklarow
 */
@Remote
@PermitAll
//@SecurityDomain("cpx")
//@RolesAllowed({"guest"})
public interface AuthServiceEJBRemote {

    int getNewClientId();

    /**
     * List of all CheckpointX Case Databases (does not depend from user!)
     *
     * @return List of CPX Databases
     */
    List<CpxDatabase> getCpxDatabases();

    /**
     * get list of current cpx sessions
     *
     * @return cpx client sessions
     */
    List<CpxSession> getCpxSessions();

    /**
     * List of all CheckpointX Persistence Unit Names (does not depend from
     * user!)
     *
     * @return List of CPX Persistence Unit Names
     */
    List<String> getCpxPersistenceUnitNames();

    /**
     * List of all CheckpointX Persistence Units (does not depend from user!)
     *
     * @return List of CPX Persistence Unit
     */
    List<CpxPersistenceUnit> getCpxPersistenceUnits();

    /**
     * List of all CPX users (valid and not valid).
     *
     * @return List of all CPX users
     */
    List<CdbUsers> getAllCpxUsers();

    /**
     * List of all valid CPX user role IDs
     *
     * @return List of all valid CPX user role IDs
     */
    List<Long> getAllValidUserRolesId();

    /**
     * List of all CheckpointX roles.
     *
     * @return List of roles for CPX users
     */
    List<CdbUserRoles> getAllUserRoles();

    /**
     * List of all assignments between user and his roles.
     *
     * @return List of assignments for CPX users und his roles
     */
    List<CdbUser2Role> getAssignmentsUser2Role();

//    /**
//     * Map of rights for all roles in CheckpointX.
//     * @return Map with role ID and Map(role ID, right)
//     */
//    RoleProperties getAllRoleRightsProperties();
    /**
     * CpxReights class include the structure of user role propertoies (xml)
     *
     * @return Map with role ID and Map(role ID, right)
     */
//    RoleProperties getRoleProperties();
    /**
     * Unlock the database
     */
    void unlockDatabase();

    /**
     * Add new user in cpx database
     *
     * @param cdbUsers include the properties for new cpx user
     * @return is ID of new cpx user
     */
    long createNewCpxUser(CdbUsers cdbUsers);

    /**
     * Add new roles for cpx user
     *
     * @param cdbUser2Role include assignment properties
     * @return id for new assignment between user and role
     */
    long addUser2Role(CdbUser2Role cdbUser2Role);

    /**
     * Find role by ID
     *
     * @param roleId ID of a role
     * @return User Role
     */
    CdbUserRoles findRoleById(long roleId);

    /**
     * Find cpx user by ID
     *
     * @param userId ID of an user
     * @return User
     */
    CdbUsers findUserById(long userId);

    /**
     * get cpx user image by ID
     *
     * @param userId ID of an user
     * @return image data
     */
    Byte[] getUserImage(long userId);

    /**
     * set cpx user image by ID
     *
     * @param userId ID of an user
     * @param image image datas
     * @return successful?
     */
    boolean setUserImage(long userId, Byte[] image);

    /**
     * Set cpx user inactive (isDeleted = true) by user id.
     *
     * @param id id of cpx user
     */
    void setCpxUserInactiveById(long id);

    /**
     * Set cpx user validation by user id.
     *
     * @param id id of cpx user
     * @param isValid is value of validation
     */
    void setCpxUserValidationById(long id, boolean isValid);

    /**
     * Set cpx user role validation by role id.
     *
     * @param id id of cpx user role
     * @param isValid is value of validation
     */
    void setCpxUserRoleValidationById(long id, boolean isValid);

    /**
     * Set modifieded cpx user data.
     *
     * @param cdbUser modifieded cpx user
     * @return is state of marge.
     */
    boolean updateCpxUser(CdbUsers cdbUser);

    /**
     * Get CdbUsers with graph "CdbUser2Role"
     *
     * @param userId User ID
     * @return CdbUsers with id "userId"
     */
    CdbUsers getCdbUsersByIdWithGraph(long userId);

    /**
     * Remove role from user and delete this match from database
     *
     * @param cdbUser2Role match between role and user
     */
    void deleteUserToRoleById(CdbUser2Role cdbUser2Role);

    /**
     * Remove cpx user role database
     *
     * @param id cpx user role id
     * @return boolean value for delete state
     */
    boolean deleteCpxUserRoleById(Long id);

    /**
     * Get if role id is ised
     *
     * @param pRoleId role ID
     * @return boolean value if role id is ised
     */
    boolean getUserNameByRoleId(long pRoleId);

    /**
     * Drops/deletes an existing database
     *
     * @param pConnectionString Connection string
     * @return successful?
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException error
     */
    boolean dropDatabase(final String pConnectionString) throws CpxIllegalArgumentException;

    /**
     * Creates a new database
     *
     * @param pDatabase e.g. dbsys1:CPX_NEW
     * @param pUserId created by this user
     * @return successful?
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException error
     */
    boolean createDatabase(final String pDatabase, final Long pUserId) throws CpxIllegalArgumentException;

    /**
     * Creates a new database
     *
     * @param pDatabase e.g. dbsys1:CPX_NEW
     * @param pUserId created by this user
     * @param pOverwrite Overwrite database if it already exists?
     * @return successful?
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException error
     */
    boolean createDatabase(final String pDatabase, final Long pUserId, final boolean pOverwrite) throws CpxIllegalArgumentException;

    /**
     * retrieve last creation or modification date of database
     *
     * @param pDatabase e.g. dbsys1:CPX_NEW
     * @return last date
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException error
     */
    Date getLastDate(final String pDatabase) throws CpxIllegalArgumentException;

    /**
     * Create new cpx user role
     *
     * @param pCdbUserRoles role data
     * @param pRoleProperties role properties
     * @return id for new role
     */
    long createNewCpxUserRole(CdbUserRoles pCdbUserRoles, RoleProperties pRoleProperties);

    /**
     * Update user role properties
     *
     * @param pUserRolesId role id
     * @param pRoleProperties cpx rights in xml
     * @return return "true" if update was succssesful
     */
    boolean setUserRoleProperties(Long pUserRolesId, RoleProperties pRoleProperties);

    /**
     * Update user role properties
     *
     * @param pUserRolesId role id
     * @return cpx rights in xml
     */
    RoleProperties getUserRoleProperties(Long pUserRolesId);

    /**
     * update cpx user role data.
     *
     * @param cdbUserRoles modifieded cpx user
     * @return is state of marge.
     */
    boolean updateCpxUserRole(CdbUserRoles cdbUserRoles);

    /**
     * get Name of the user
     *
     * @param userId unique user id
     * @return Name of the User
     */
    String getUserFullName(long userId);

    /**
     * get the Login Name of the User
     *
     * @param userId unique user id
     * @return Login name of the user
     */
    String getUserLoginName(long userId);

    /**
     * get actuall grouper models
     *
     * @param addDestGrouper add a det grouper (true/false)
     * @return list with grouper models
     */
    List<GDRGModel> getActualGrouperModelsAsList(boolean addDestGrouper);

    /**
     * get user to roles entries for user with this id
     *
     * @param userId user id
     * @return list for user to roles entries for user with this id
     */
    List<CdbUser2Role> getUser2RoleByUserId(Long userId);

    List<Long> getRoleIds(Long userId);

    /**
     * gets all users represented by an userDTO DTO contains its name and the
     * database id Name in the dto may differ, and is calculated on server side
     * - like for the DAK some number
     *
     * @return list of UserDTO objects
     */
    List<UserDTO> getAllUsers();

    /**
     * gets all users that are considered as active active Users are Entries in
     * the CDB_USERS table with isValid = 1 AND deleted = 0 represented by an
     * userDTO DTO contains its name and the database id Name in the dto may
     * differ, and is calculated on server side - like for the DAK some number
     *
     * @return list of UserDTO objects
     */
    List<UserDTO> getAllActiveUsers();

    /**
     * returns copy of CdbUsers object
     *
     * @param pUserId User-ID
     * @return copy of CdbUsers object
     */
    CdbUsers getCopy(final Long pUserId);

    /**
     *
     * @param userText User name
     * @return matching user names (full name)
     */
    Collection<String> findMatchingUsers(String userText);

    /**
     *
     * @param userText User name
     * @return User ID
     */
    Long getIdbyUName(String userText);

    /**
     * @param roleId role id
     * @return name of the role by id
     */
    String findRoleNameById(long roleId);

    /**
     * Returns information about the server configuration
     *
     * @return server-side CpxSystemProperties
     */
    CpxSystemPropertiesInterface getCpxSystemProperties();

    /**
     * Recent server version
     *
     * @return version
     */
    Version getVersion();

    /**
     * Gives the Browser URL to CPX WebApp
     *
     * @return url to WebApp
     */
    String getWebAppUrl();

    /**
     * Gives the Browser URL to Wildfly Management Interface
     *
     * @return url to Wildfly Management Interface
     */
    String getManagementInterfaceUrl();

    /**
     * Retrieves client message and put it into server log file
     *
     * @param pMessage client message
     * @param pException client exception or throwable
     * @param pCpxPropsClient client system properties
     */
    void logClientMessage(final String pMessage, final Throwable pException, final CpxSystemPropertiesInterface pCpxPropsClient);

    /**
     * Gets the server entries around the specified date
     *
     * @param pDate date filter for log file
     * @return part of log file
     * @throws IOException log file not found or something similar
     */
    String getServerLogLines(final Date pDate) throws IOException;

    /**
     * Get CPX license
     *
     * @return license
     */
    License getLicense();

    /**
     * information about common database
     *
     * @return database info common
     */
    DatabaseInfo getDatabaseInfoCommon();



}
