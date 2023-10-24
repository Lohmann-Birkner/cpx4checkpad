/* 
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.cpx.license.crypter.LicenseWriter;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.server.auth.CaseEntityManagerFactoryCreator;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.auth.CpxUser;
import de.lb.cpx.server.catalog.service.ejb.CatalogUtil;
import de.lb.cpx.server.commonDB.dao.CdbUser2RoleDao;
import de.lb.cpx.server.commonDB.dao.CdbUserRolesDao;
import de.lb.cpx.server.commonDB.dao.CdbUsersDao;
import static de.lb.cpx.server.commonDB.dao.CdbUsersDao.toUserDto;
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
import de.lb.cpx.shared.version.VersionHistory;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.hibernate.Hibernate;

/**
 *
 * @author niemeier, sklarow
 */
@ApplicationScoped
//@SecurityDomain("cpx")
//@RolesAllowed({"guest"})
@Stateful(name = "AuthServiceEJB")
@PermitAll
@LocalBean
public class AuthServiceEJB implements AuthServiceEJBRemote {

    private static final Logger LOG = Logger.getLogger(AuthServiceEJB.class.getName());

    @Inject
    private CdbUserRolesDao cdbUserRolesDao;

    @Inject
    private CdbUser2RoleDao cdbUser2RoleDao;

    @Inject
    private CdbUsersDao cdbUsersDao;

    @Inject
    private LockServiceBean dBLockService;

    public static synchronized boolean createDatabaseStatic(final String pConnectionString, final Long pUserId, final boolean pOverwrite) throws CpxIllegalArgumentException {
        boolean b = false;
        try {
            b = CaseEntityManagerFactoryCreator.dropAndCreateDatabase(pConnectionString, pUserId, pOverwrite);
        } catch (CpxIllegalArgumentException ex) {
            LOG.log(Level.SEVERE, "drop and create of database failed", ex);
            //throw new IllegalArgumentException(ex.getMessage());
            throw ex;
        }
        return b;
    }

    public static synchronized boolean dropDatabaseStatic(final String pConnectionString) throws CpxIllegalArgumentException {
        boolean b = false;
        try {
            b = CaseEntityManagerFactoryCreator.dropDatabase(pConnectionString);
        } catch (CpxIllegalArgumentException ex) {
            LOG.log(Level.SEVERE, "drop of database failed", ex);
            //throw new IllegalArgumentException(ex.getMessage());
            throw ex;
        }
        return b;
    }

    public static Date getDatabaseDateStatic(final String pConnectionString) throws CpxIllegalArgumentException {
        try {
            return CaseEntityManagerFactoryCreator.getLastDate(pConnectionString);
        } catch (CpxIllegalArgumentException ex) {
            LOG.log(Level.SEVERE, "getting last date of database failed", ex);
            //throw new IllegalArgumentException(ex.getMessage());
            throw ex;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "getting last date of database failed", ex);
            //throw new IllegalArgumentException(ex.getMessage());
            throw new CpxIllegalArgumentException(ex.getMessage());
        }
    }

    @Override
    public List<CpxDatabase> getCpxDatabases() {
        Map<String, CpxDatabase> databases = ClientManager.getAvailableDatabases();
        List<CpxDatabase> databaseList = new ArrayList<>();
        databaseList.addAll(databases.values());

        return databaseList;
    }

    @Override
    public List<CpxSession> getCpxSessions() {
        Iterator<Map.Entry<String, CpxUser>> it = ClientManager.getCpxClients().entrySet().iterator();
        final List<CpxSession> result = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry<String, CpxUser> entry = it.next();
            final CpxUser client = entry.getValue();
            if (client == null) {
                continue;
            }
            result.add(new CpxSession(
                    client.getClientId(),
                    client.isJob(),
                    client.getUserId(),
                    client.getUserName(),
                    client.getActualDatabase(),
                    client.getActualRoleId(),
                    client.getActualRoleName(),
                    client.getLoginDate() == null ? null : client.getLoginDate().getTime(),
                    client.getLastActionAt() == null ? null : client.getLastActionAt().getTime(),
                    client.getAppType(),
                    client.getCpxSystemProperties()
            ));
        }
        return result;
    }

    @Override
    public List<String> getCpxPersistenceUnitNames() {
        return ClientManager.getAvailablePersistenceUnitNames();
    }

    @Override
    public List<CpxPersistenceUnit> getCpxPersistenceUnits() {
        return ClientManager.getAvailablePersistenceUnits();
    }

    @Override
    public int getNewClientId() {
        return ClientManager.getNewClientId();
    }

    @Override
    public List<CdbUsers> getAllCpxUsers() {
        List<CdbUsers> cdbUserList = cdbUsersDao.getAllUsers();
        for (CdbUsers user : cdbUserList) {
            Hibernate.initialize(user);
            Hibernate.initialize(user.getCdbUser2Roles());
            Iterator<CdbUser2Role> itr = user.getCdbUser2Roles().iterator();
            while (itr.hasNext()) {
                CdbUser2Role user2role = itr.next();
                Hibernate.initialize(user2role);
                Hibernate.initialize(user2role.getCdbUserRoles());
            }
        }
        return cdbUsersDao.getAllUsers();
    }

    @Override
    public List<CdbUserRoles> getAllUserRoles() {
        return cdbUserRolesDao.findAll();
    }

    @Override
    public List<Long> getAllValidUserRolesId() {
        return cdbUserRolesDao.getAllValidRoleIds();
    }

    @Override
    public List<CdbUser2Role> getAssignmentsUser2Role() {
        return cdbUser2RoleDao.getAllAssignmentsUser2Role();
    }

//    @Override
//    public RoleProperties getRoleProperties() {
//        RoleProperties roleProperties = new RoleProperties();
//        return roleProperties;
//    }
    @Override
    public void unlockDatabase() {
        dBLockService.unlockDatabase();
    }

    @Override
    public long createNewCpxUser(CdbUsers cdbUsers) {
        return cdbUsersDao.createNewCpxUser(cdbUsers);
    }

    @Override
    public long addUser2Role(CdbUser2Role cdbUser2Role) {
        //return cdbUser2RoleDao.addUser2Role(cdbUser2Role);
        return cdbUser2RoleDao.addNewItem(cdbUser2Role);
    }

    @Override
    public CdbUserRoles findRoleById(long roleId) {
        return cdbUserRolesDao.findById(roleId);
    }

    @Override
    public CdbUsers findUserById(long userId) {
        return cdbUsersDao.findById(userId);
    }

    @Override
    public Byte[] getUserImage(long userId) {
        return cdbUsersDao.getUserImage(userId);
    }

    @Override
    public boolean setUserImage(long userId, Byte[] image) {
        return cdbUsersDao.setUserImage(userId, image);
    }

    @Override
    public void setCpxUserInactiveById(long id) {
        cdbUsersDao.setCpxUserInactiveById(id);
    }

    @Override
    public void setCpxUserValidationById(long id, boolean isValid) {
        cdbUsersDao.setCpxUserValidationById(id, isValid);
    }

    @Override
    public void setCpxUserRoleValidationById(long id, boolean isValid) {
        cdbUserRolesDao.setCpxUserRoleValidationById(id, isValid);
    }

    @Override
    public boolean updateCpxUser(CdbUsers cdbUsers) {
        //return cdbUsersDao.updateCpxUser(cdbUsers);
        return cdbUsersDao.updateItem(cdbUsers);
    }

    @Override
    public CdbUsers getCdbUsersByIdWithGraph(long userId) {
        CdbUsers cdbUsers = cdbUsersDao.getCdbUsersByIdWithGraph(userId);
        Hibernate.initialize(cdbUsers);
        Hibernate.initialize(cdbUsers.getCdbUser2Roles());
        Iterator<CdbUser2Role> itr = cdbUsers.getCdbUser2Roles().iterator();
        while (itr.hasNext()) {
            CdbUser2Role user2role = itr.next();
            Hibernate.initialize(user2role);
            Hibernate.initialize(user2role.getCdbUserRoles());
        }
        return cdbUsers;
    }

    @Override
    public void deleteUserToRoleById(CdbUser2Role cdbUser2Role) {
        cdbUser2RoleDao.deleteUserToRole(cdbUser2Role);
    }

    /**
     * Remove cpx user role database
     *
     * @param id cpx user role id
     * @return boolean value for delete state
     */
    @Override
    public boolean deleteCpxUserRoleById(Long id) {
        //return cdbUserRolesDao.removeCpxUserRoleById(id);
        return cdbUserRolesDao.deleteById(id);
    }

    /**
     * Get if role id is ised
     *
     * @param pRoleId role ID
     * @return boolean value if role id is ised
     */
    @Override
    public boolean getUserNameByRoleId(long pRoleId) {
        return cdbUser2RoleDao.getUserNameByRoleId(pRoleId);
    }

    @Override
    public boolean dropDatabase(final String pConnectionString) throws CpxIllegalArgumentException {
        return dropDatabaseStatic(pConnectionString);
    }

    @Override
    public boolean createDatabase(final String pConnectionString, final Long pUserId) throws CpxIllegalArgumentException {
        return createDatabaseStatic(pConnectionString, pUserId, false);
    }

    @Override
    public boolean createDatabase(final String pConnectionString, final Long pUserId, final boolean pOverwrite) throws CpxIllegalArgumentException {
        return createDatabaseStatic(pConnectionString, pUserId, pOverwrite);
    }

    @Override
    public Date getLastDate(final String pConnectionString) throws CpxIllegalArgumentException {
        return getDatabaseDateStatic(pConnectionString);
    }

    /**
     * Create new role for cpx user.
     *
     * @param pCdbUserRoles role data
     * @param pRoleProperties role properties
     * @return id for new role
     */
    @Override
    public long createNewCpxUserRole(CdbUserRoles pCdbUserRoles, RoleProperties pRoleProperties) {
        return this.cdbUserRolesDao.createNewCpxUserRole(pCdbUserRoles, pRoleProperties);
    }

    /**
     * Update user role properties
     *
     * @param pUserRolesId role id
     * @param pRoleProperties cpx rights in xml
     * @return return "true" if update was succssesful
     */
    @Override
    public boolean setUserRoleProperties(Long pUserRolesId, RoleProperties pRoleProperties) {
        return this.cdbUserRolesDao.setUserRoleProperties(pUserRolesId, pRoleProperties);
    }

    /**
     * Update user role properties
     *
     * @param pUserRolesId role id
     * @return cpx rights in xml
     */
    @Override
    public RoleProperties getUserRoleProperties(Long pUserRolesId) {
        return this.cdbUserRolesDao.getUserRoleProperties(pUserRolesId);
    }

    /**
     * update cpx user role data.
     *
     * @param cdbUserRoles modifieded cpx user
     * @return is state of marge.
     */
    @Override
    public boolean updateCpxUserRole(CdbUserRoles cdbUserRoles) {
        //return cdbUserRolesDao.updateCpxUserRole(cdbUserRoles);
        return cdbUserRolesDao.updateItem(cdbUserRoles);
    }

    @Override
    public String getUserFullName(long userId) {
        CdbUsers user = findUserById(userId);
        if (user != null) {
            //return (user.getUFirstName() != null ? (user.getUFirstName() + " ") : "") + (user.getULastName() != null ? user.getULastName() : "");
            return user.getUFullName();
        }
        return null;
    }

    @Override
    public String getUserLoginName(long userId) {
        CdbUsers user = findUserById(userId);
        if (user != null) {
            return user.getUName();
        }
        return null;
    }

    /**
     * get actuall grouper models
     *
     * @param addDestGrouper add a det grouper (true/false)
     * @return list with grouper models
     */
    @Override
    public List<GDRGModel> getActualGrouperModelsAsList(boolean addDestGrouper) {
        return CatalogUtil.getActualGrouperModelsAsList(addDestGrouper);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userList = new ArrayList<>();

        cdbUsersDao.findAll().stream().forEach((user) -> {
            //String name = (user.getUFirstName() != null ? (user.getUFirstName() + " ") : "") + (user.getULastName() != null ? user.getULastName() : "");
            //String fullName = user.getUFullName();
            //userList.add(new UserDTO(user.getId(), fullName, user.getUName(), user.getUValidFrom(), user.getUValidTo(), user.isUIsValid(), user.isUIsDeleted()));
            UserDTO dto = toUserDto(user);
            if (dto != null) {
                userList.add(dto);
            }
        });

        return userList;
    }

    @Override
    public List<UserDTO> getAllActiveUsers() {
        List<UserDTO> userList = new ArrayList<>();

        cdbUsersDao.findAllActiveUsers().stream().forEach((user) -> {
            //String name = (user.getUFirstName() != null ? (user.getUFirstName() + " ") : "") + (user.getULastName() != null ? user.getULastName() : "");
            //String fullName = user.getUFullName();
            //userList.add(new UserDTO(user.getId(), fullName, user.getUName(), user.getUValidFrom(), user.getUValidTo(), user.isUIsValid(), user.isUIsDeleted()));
            UserDTO dto = toUserDto(user);
            if (dto != null) {
                userList.add(dto);
            }
        });

        return userList;
    }

    @Override
    public List<CdbUser2Role> getUser2RoleByUserId(Long userId) {
        return cdbUser2RoleDao.getUser2RoleByUserId(userId);
    }

    @Override
    public List<Long> getRoleIds(Long userId) {
        List<CdbUser2Role> tmp = getUser2RoleByUserId(userId);
        final List<Long> result = new ArrayList<>();
        tmp.forEach((role) -> {
            result.add(role.id);
        });
        return result;
    }

//    @Override
//    public void manipulateUserPrincipal(String clientID){
//        try {
//            ClientManager.manipulateUserPrincipal(clientID);
//        } catch (NoSuchElementException | ReflectiveOperationException | InstanceAlreadyExistsException ex) {
//            Logger.getLogger(AuthServiceEJB.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    @Override
    public CdbUsers getCopy(Long pUserId) {
        return cdbUsersDao.getCopy(pUserId);
    }

    @Override
    public Collection<String> findMatchingUsers(String userText) {

        return cdbUsersDao.findMatchingUsers(userText);
    }

    @Override
    public Long getIdbyUName(String userText) {

        return cdbUsersDao.getIDbyUName(userText);
    }

    @Override
    public String findRoleNameById(long roleId) {
        CdbUserRoles role = findRoleById(roleId);
        return role != null ? role.getCdburName() : "-";
    }

    @Override
    public CpxSystemPropertiesInterface getCpxSystemProperties() {
        return CpxSystemProperties.getInstance();
    }

    @Override
    public Version getVersion() {
        return VersionHistory.getRecentVersion();
    }

    @Override
    public String getWebAppUrl() {
        return getCpxSystemProperties().getWebAppUrl();
    }

    @Override
    public String getManagementInterfaceUrl() {
        return getCpxSystemProperties().getWildflyManagementUrl();
    }

    @Override
    public void logClientMessage(final String pMessage, final Throwable pException, final CpxSystemPropertiesInterface pCpxPropsClient) {
//        String clientId = ClientManager.getCurrentCpxClientId();
//        Long cpxUserId = ClientManager.getCurrentCpxUserId();
//        String cpxUserName = ClientManager.getCurrentCpxUserName();
//        String database = ClientManager.getActualDatabase();
        if ((pMessage == null || pMessage.trim().isEmpty()) && pException == null) {
            LOG.log(Level.WARNING, "empty client message retrieved");
            return;
        }
        //LOG.log(Level.SEVERE, "Retrieved client message:\r\n" + pMessage + (pCpxPropsClient == null ? "": "\r\n\r\n" + pCpxPropsClient.toString() + (pException != null?"\r\n\r\n":"")), pException);
        LOG.log(Level.SEVERE, "Retrieved client message:\r\n" + pMessage + "\r\n", pException);
    }

    @Override
    public String getServerLogLines(final Date pDate) throws IOException {
        return readLogLines(pDate);
    }

    private static String readLogLines(final Date pDate) throws IOException {
        if (pDate == null) {
            throw new IllegalArgumentException("pDate is null!");
        }
        final Calendar dateFrom = Calendar.getInstance();
        final Calendar dateTo = Calendar.getInstance();

        dateFrom.setTime(pDate);
        dateTo.setTime(pDate);

        dateFrom.set(Calendar.MINUTE, dateFrom.get(Calendar.MINUTE) - 1);
        dateTo.set(Calendar.SECOND, dateFrom.get(Calendar.SECOND) + 1);

        //dateFrom.set(Calendar.SECOND, dateFrom.get(Calendar.SECOND) - 30);
        final int maxMessages = 1000;
        //final File logFile = new File("C:\\Labor\\Wildfly_10\\standalone\\log\\server.log");
        final CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        final File logFile = new File(cpxProps.getCpxServerLogDir() + cpxProps.getCpxServerLogFile());
        return readLogLines(logFile, dateFrom.getTime(), dateTo.getTime(), maxMessages);
    }

    private static String readLogLines(final File pLogFile, final Date pDateFrom, final Date pDateTo, final int pMaxMessages) throws IOException {
        if (pDateFrom == null) {
            throw new IllegalArgumentException("pDateFrom is null!");
        }
        if (pDateTo == null) {
            throw new IllegalArgumentException("pDateTo is null!");
        }
        if (pDateFrom.after(pDateTo)) {
            throw new IllegalArgumentException("pDateFrom has to be smaller than pDateTo!");
        }
        if (pMaxMessages <= 0) {
            throw new IllegalArgumentException("pMaxMessages has to be >= 1!");
        }
        if (pLogFile == null) {
            throw new IOException("pLogFile is null!");
        }
        if (!pLogFile.exists()) {
            throw new IOException("Log file does not exist: " + pLogFile.getAbsolutePath());
        }
        if (!pLogFile.isFile()) {
            throw new IOException("This is a directory and not a single log file: " + pLogFile.getAbsolutePath());
        }
        if (!pLogFile.canRead()) {
            throw new IOException("No permissions to read this log file: " + pLogFile.getAbsolutePath());
        }
        final int MAX_MESSAGES_LIMIT = 1000;
        if (pMaxMessages > MAX_MESSAGES_LIMIT) {
            throw new IllegalArgumentException("pMaxMessages has to be <= " + MAX_MESSAGES_LIMIT + "!");
        }
        final String datePattern = "yyyy-MM-dd HH:mm:ss";
        final DateFormat df = new SimpleDateFormat(datePattern);
        final String dateFrom = df.format(pDateFrom);
        final String dateTo = df.format(pDateTo);
        final Charset charset = Charset.forName("Cp1252");
        final int blockSize = 4096;
        LOG.log(Level.INFO, "Retrieve last lines from log file '" + pLogFile.getAbsolutePath() + "' with these parameters: dateFrom=" + dateFrom + ", dateTo=" + dateTo + ", maxMessages=" + pMaxMessages + ", charset=" + charset.displayName() + ", blocksize=" + blockSize);
        for (Handler h : LOG.getHandlers()) {
            h.flush(); //get sure that buffered output was written to log file
        }
        final List<String> lines = new ArrayList<>();
        try (final ReversedLinesFileReader object = new ReversedLinesFileReader(pLogFile, blockSize, charset)) {
            int counter = 0;
            while (counter < pMaxMessages) {
                final String line = object.readLine();
                if (line == null) {
                    break;
                }
                if (line.length() >= datePattern.length()) {
                    String d = line.substring(0, datePattern.length());
                    Date dt = null;
                    char c = d.charAt(0);
                    if (c >= '0' && c <= '9') { //Only parse if line potentially starts with a year
                        try {
                            dt = df.parse(d);
                        } catch (ParseException ex) {
                            LOG.log(Level.FINEST, "This is not a valid date: " + d, ex);
                        }
                    }
                    if (dt != null) {
                        if (dt.before(pDateFrom)) {
                            break;
                        }
                        if (dt.after(pDateTo)) {
                            break;
                        }
                        counter++;
                    }
                }
                lines.add(line);
            }
        }
        LOG.log(Level.INFO, "Found " + lines.size() + " lines in " + pLogFile.getAbsolutePath());
        final StringBuilder sb = new StringBuilder();
        for (int i = lines.size() - 1; i >= 0; i--) {
            if (sb.length() > 0) {
                sb.append("\r\n");
            }
            sb.append(lines.get(i));
        }
        return sb.toString();
    }

    @Override
    public License getLicense() {
        final String licenseFilename = CpxSystemProperties.getInstance().getCpxServerLicenseDir() + "\\" + LicenseWriter.DEFAULT_LICENSE_FILENAME;
        License license = null;
        try {
            license = License.loadFromLicenseFile(licenseFilename);
        } catch (IllegalArgumentException ex) {
            LOG.log(Level.FINER, "No CPX license available", ex);
        }
        return license;
    }

    @Override
    public DatabaseInfo getDatabaseInfoCommon() {
        return cdbUsersDao.getDatabaseInfo();
//        return new DatabaseInfo(
//                caseDao.getConnectionDatabase(),
//                caseDao.getConnectionUrl(),
//                caseDao.getConnectionDatabaseVendor(),
//                //caseDao.isOracle(), caseDao.isSqlSrv(),
//                caseDao.getDatabaseVersion()
//        );
    }

}
