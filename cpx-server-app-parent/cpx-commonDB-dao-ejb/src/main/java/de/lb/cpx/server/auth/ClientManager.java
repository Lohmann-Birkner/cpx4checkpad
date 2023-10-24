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
 *    2016  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.auth;

import de.checkpoint.enums.AppTypeEn;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commons.dao.AbstractDao;
import static de.lb.cpx.server.commons.dao.AbstractDao.getConnectionUrl;
import static de.lb.cpx.server.commons.dao.AbstractDao.isOracle;
import static de.lb.cpx.server.commons.dao.AbstractDao.isSqlSrv;
import de.lb.cpx.service.information.ConnectionString;
import de.lb.cpx.service.information.CpxDatabase;
import de.lb.cpx.service.information.CpxPersistenceUnit;
import de.lb.cpx.service.information.DatabaseInfo;
import java.lang.reflect.Field;
import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * ClientManager is responsible for managing client and job sessions. You can
 * also retrieve a list of available CPX databases over multiple persistence
 * units.
 *
 * @author Dirk Niemeier
 */
@Stateless
@Dependent
//@SecurityDomain("cpx")
public class ClientManager {

    @Resource
    private SessionContext ctx;

    private static final Logger LOG = Logger.getLogger(ClientManager.class.getName());
    private static boolean cdiAvailable = true;

    protected static final AtomicInteger CLIENT_ID_GENERATOR = new AtomicInteger(0);
    protected static final Map<String, CpxUser> CLIENT_MAP = new ConcurrentHashMap<>();
    protected static final Map<Long, CdbUserRoles> ROLES_MAP = new ConcurrentHashMap<>();
    protected static final Map<String, CpxJob> JOB_MAP = new ConcurrentHashMap<>();

    public static synchronized boolean isCdiAvailable() {
        if (!cdiAvailable) {
            return cdiAvailable;
        }
        try {
            getStaticSessionContext();
        } catch (IllegalStateException ex) {
            //CDI (Weld) seems to be unavailable
            LOG.log(Level.INFO, "CDI (Weld) seems to be unavailable", ex);
            cdiAvailable = false;
        }
        return cdiAvailable;
    }

    /**
     * Returns a list of available CPX case databases
     *
     * @return List of case databases (dbsys1:CPX_DEV, dbsys2:CPX and so on...)
     */
    public static synchronized List<String> getAvailableConnectionStrings() {
        Map<String, CpxDatabase> databases = getAvailableDatabases(true);
        List<String> keys = new ArrayList<>();
        keys.addAll(databases.keySet());
        Collections.sort(keys);
        return keys;
    }

    /**
     * Returns not only a list of databases, but also some extra informations
     * for each database (e.g. count of cases)
     *
     * @return Map with case databases as key (dbsys1:CPX_DEV, dbsys2:CPX and so
     * on...) and an extensible CpxDatabase object
     */
    public static synchronized Map<String, CpxDatabase> getAvailableDatabases() {
        return getAvailableDatabases(false);
    }

    /**
     * Tries to connect all persistence units and returns only the persistence
     * units that are valid
     *
     * @return List of available persistence units (dbsys1, dbsys2...)
     */
    public static synchronized List<CpxPersistenceUnit> getAvailablePersistenceUnits() {
        List<String> persistenceUnits = ClientManager.getAvailablePersistenceUnitNames();
        List<CpxPersistenceUnit> persistenceUnitList = new ArrayList<>();
        //CpxServerConfig cpxServerConfig = new CpxServerConfig();
        for (final String persistenceUnit : persistenceUnits) {
            String connectionString = persistenceUnit + ":";
            CaseEntityManagerFactoryCreator.EntityManagerFactoryResult emfResult = CaseEntityManagerFactoryCreator.fetchEntityManagerFactory(connectionString);
            EntityManager em = emfResult.emf.createEntityManager();
            DatabaseInfo databaseInfo;
            try (Connection conn = AbstractDao.getConnection(emfResult.emf.createEntityManager())) {
                final String connectionDatabase = AbstractDao.getConnectionDatabase(conn, em);
                //final String connectionDatabase = persistenceUnit;
                final String connectionUrl = AbstractDao.getConnectionUrl(conn);
                final String connectionDatabaseVendor = AbstractDao.getConnectionDatabaseVendor(em);
                //final boolean isOracle = AbstractDao.isOracle(em);
                //final boolean isSqlsrv = AbstractDao.isSqlSrv(em);
                final String databaseVersion = AbstractDao.getDatabaseVersion(em);

                databaseInfo = new DatabaseInfo(
                        connectionDatabase,
                        connectionUrl,
                        connectionDatabaseVendor,
                        //isOracle, isSqlsrv,
                        databaseVersion
                );
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, null, ex);
                databaseInfo = new DatabaseInfo("", "", "", /* false, false, */ "");
            }
            CpxPersistenceUnit pu = new CpxPersistenceUnit(persistenceUnit, databaseInfo);
            persistenceUnitList.add(pu);
        }
        return persistenceUnitList;
    }

    /**
     * Tries to connect all persistence units and returns only the persistence
     * units that are valid
     *
     * @return List of available persistence units (dbsys1, dbsys2...)
     */
    public static synchronized List<String> getAvailablePersistenceUnitNames() {
        List<String> persistentUnits = new ArrayList<>();
        List<String> availablePersistenceUnit = new ArrayList<>();
        //For future extensions: Put your new persistence unit from persistence.xml in cpx-dao-ejb here!
        persistentUnits.add("dbsys1");
        persistentUnits.add("dbsys2");
        persistentUnits.add("dbsys3");
        persistentUnits.add("dbsys4");
        persistentUnits.add("dbsys5");
        persistentUnits.add("dbsys6");
        persistentUnits.add("dbsys7");
        persistentUnits.add("dbsys8");
        persistentUnits.add("dbsys9");
        persistentUnits.add("dbsys10");

        for (String persistenceUnit : persistentUnits) {
            String connectionString = persistenceUnit + ":";
            //EntityManager em = null;
            try {
                LOG.log(Level.INFO, "Try to connect persistence unit ''{0}'' now...", persistenceUnit);
                CaseEntityManagerFactoryCreator.EntityManagerFactoryResult emfResult = CaseEntityManagerFactoryCreator.fetchEntityManagerFactory(connectionString);
                //EntityManagerFactory emf = CaseEntityManagerFactoryCreator.fetchEntityManagerFactory(connectionString).emf;
                EntityManagerFactory emf = (emfResult == null) ? null : emfResult.emf;
                if (emf != null) {
                    availablePersistenceUnit.add(persistenceUnit);
                }
            } catch (IllegalArgumentException ex) {
                LOG.log(Level.SEVERE, "Was not able to open persistence unit or database '" + connectionString + "'", ex);
            }
        }
        return availablePersistenceUnit;
    }

    @SuppressWarnings("unchecked")
    protected static Map<String, CpxDatabase> getAvailableDatabases(final boolean pKeysOnly) {
        return getAvailableDatabases(pKeysOnly, null);
    }

    @SuppressWarnings("unchecked")
    protected static CpxDatabase getDatabase(final String pConnectionString) {
        final boolean keysOnly = false;
        final Map<String, CpxDatabase> result = getAvailableDatabases(keysOnly, pConnectionString);
        if (result.isEmpty()) {
            return null;
        }
        return result.entrySet().iterator().next().getValue();
    }

    @SuppressWarnings("unchecked")
    private static synchronized Map<String, CpxDatabase> getAvailableDatabases(final boolean pKeysOnly, final String pConnectionString) {
        final ConnectionString connString = new ConnectionString(pConnectionString);
        final List<String> persistentUnits;
        if (connString.getPersistenceUnit().isEmpty()) {
            persistentUnits = getAvailablePersistenceUnitNames();
        } else {
            persistentUnits = new ArrayList<>();
            persistentUnits.add(connString.getPersistenceUnit());
        }
        Map<String, CpxDatabase> databases = new HashMap<>();

        for (String persistenceUnit : persistentUnits) {
            String connectionString = persistenceUnit + ":";
            EntityManager em = null;
            try {
                LOG.log(Level.INFO, "Try to connect persistence unit ''{0}'' now...", persistenceUnit);
                EntityManagerFactory emf = CaseEntityManagerFactoryCreator.fetchEntityManagerFactory(connectionString).emf;
                em = CaseEntityManagerFactoryCreator.getEntityManager(emf);
                if (em == null) {
                    continue;
                }
                String query;

                List<String> databasesTmp;
                if (isOracle(em)) {
                    //Oracle
                    /*
          query = "SELECT DISTINCT owner name " + 
                  " FROM all_tables " + 
                  " WHERE table_name IN ('T_CASE','T_CASE_DETAILS') " + 
                  "   AND owner NOT LIKE '%_LOG' " + 
                  "   AND owner <> 'SYSTEM' " + 
                  " ORDER BY owner";
                     */
 /* 3.9.5 2016-08-24 DNi: Improved query, get sure that T_CASE AND(!) T_CASE_DETAILS exist and not only one of them */
                    query = "SELECT DISTINCT owner name "
                            + " FROM all_tables "
                            + " WHERE EXISTS (SELECT 1 FROM all_tables at WHERE at.table_name = 'T_CASE' AND at.owner = all_tables.owner) "
                            + "   AND EXISTS (SELECT 1 FROM all_tables at WHERE at.table_name = 'T_CASE_DETAILS' AND at.owner = all_tables.owner) "
                            + "   AND owner NOT LIKE '%_LOG' "
                            + "   AND owner <> 'SYSTEM' ";
                    if (!connString.getDatabase().isEmpty()) {
                        query += String.format("%n   AND owner = '%s'", connString.getDatabase());
                    }
                    query += " ORDER BY owner";
                    databasesTmp = em.createNativeQuery(query).getResultList();
                } else if (isSqlSrv(em)) {
                    //Microsoft SQL Server
                    query = "SELECT cast(name as varchar) name "
                            + " FROM master.dbo.sysdatabases "
                            + " WHERE name NOT IN ('master', 'tempdb', 'model', 'msdb', 'ReportServer', 'ReportServerTempDB') "
                            + "   AND HAS_DBACCESS(name) = 1 ";
                    //2017-07-13 CPX-585 DNi: Added this line to avoid the following error: Msg 916, Level 14, State 1, Line 1 The server principal "XXXX" is not able to access the database "DB_NAME" under the current security context.
                    //"   AND dbid > 4 " + //Alternative to exclude system databases
                    if (!connString.getDatabase().isEmpty()) {
                        query += String.format("%n   AND name = '%s'", connString.getDatabase());
                    }
                    query += " ORDER BY name";
                    databasesTmp = em.createNativeQuery(query).getResultList();
                    Iterator<String> it = databasesTmp.iterator();
                    while (it.hasNext()) {
                        //Remove all databases that don't have T_CASE and T_CASE_DETAILS table
                        String database = it.next();
                        query = "SELECT COUNT(*) FROM [" + database + "].sys.tables WHERE name = 'T_CASE' OR name = 'T_CASE_DETAILS' ";
                        List<Number> resultTmp = em.createNativeQuery(query).getResultList();
                        if (resultTmp == null || resultTmp.isEmpty() || resultTmp.get(0).intValue() < 2) {
                            it.remove();
                        }
                    }
                } else {
                    //Something else (neither Oracle nor Microsoft SQL Server)
                    LOG.log(Level.SEVERE, "Was not able to detect database provider (provider is neither Oracle nor Microsoft SQL Server)");
                    return databases;
                }

                //List<String> databases = getEntityManager().createNativeQuery(query).getResultList();
                Iterator<String> it = databasesTmp.iterator();
                while (it.hasNext()) {
                    String database = it.next();
                    if (database == null || database.trim().isEmpty()) {
                        continue;
                    }
                    Integer caseCount = null;
                    Integer processCount = null;
                    String url = "";
                    if (pKeysOnly) {
                        //
                    } else {
                        url = getConnectionUrl(em);
                        LOG.log(Level.FINEST, "Will get the number of hospital cases and processes in database ''{0}''...", database);
                        caseCount = getCaseCount(em, database);
                        processCount = getProcessCount(em, database);
                    }
                    CpxDatabase cpxDatabase = new CpxDatabase(persistenceUnit, database, caseCount, processCount, url);
                    String connStr = cpxDatabase.getConnectionString();
                    if (pKeysOnly) {
                        cpxDatabase = null;
                    }
                    databases.put(connStr, cpxDatabase);
                    //databases.put(persistenceUnit + ":" + database, caseCount);
                    //it.set(element);
                }
            } catch (IllegalArgumentException ex) {
                LOG.log(Level.SEVERE, "Was not able to open persistence unit or database '" + connectionString + "'", ex);
            } finally {
                if (em != null) {
                    em.close();
                }
            }
            //Collections.sort(databases);
        }
        return databases;
    }

    protected static Integer getCount(final EntityManager em, final String pDatabase, final String pTable) {
        return getCount(em, pDatabase, pTable, null);
    }

    protected static Integer getCount(final EntityManager em, final String pDatabase, final String pTable, final String pWhere) {
        if (em == null) {
            return null;
        }
        String database = (pDatabase == null) ? "" : pDatabase.trim();
        if (database.isEmpty()) {
            return null;
        }

        String q;
        if (isSqlSrv(em)) {
            //Oracle
            q = String.format("SELECT COUNT(*) FROM %s.dbo." + pTable, database);
        } else {
            //SQL Server
            q = String.format("SELECT COUNT(*) FROM %s." + pTable, database);
        }
        if (pWhere != null && !pWhere.trim().isEmpty()) {
            q += String.format(" WHERE %s", pWhere);
        }
        final String query = q;
        //final IntegerProperty countProperty = new SimpleIntegerProperty();
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future<Integer> future = executor.submit(() -> {
            Number count = (Number) em.createNativeQuery(query).getSingleResult();
            if (count == null) {
                return 0;
            } else {
                return count.intValue();
            }
        });
        try {
            return future.get(2, TimeUnit.SECONDS);
        } catch (TimeoutException | ExecutionException ex) {
            LOG.log(Level.WARNING, "Cannot read count from table {0} from database ''{1}''. Maybe database is busy (running import)? -> {2}", new Object[]{pTable, database, ex.getMessage()});
            LOG.log(Level.FINEST, "query failed on " + database + ": " + q, ex);
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, "Cannot read count of table " + pTable + " from database '" + database + "'. Thread was interruped!", ex);
            Thread.currentThread().interrupt();
        }
        executor.shutdownNow();
//        if (count == null) {
//            return 0;
//        }
        return null;
    }

    protected static Integer getCaseCount(final EntityManager em, final String pDatabase) {
        return getCount(em, pDatabase, "T_CASE", "CANCEL_FL = 0");
    }

//    protected static Integer getCanceledCaseCount(final EntityManager em, final String pDatabase) {
//        return getCount(em, pDatabase, "T_CASE", "CANCEL_FL = 1");
//    }
    protected static Integer getProcessCount(final EntityManager em, final String pDatabase) {
        return getCount(em, pDatabase, "T_WM_PROCESS", "CANCEL_FL = 0");
    }

//    protected static Integer getCanceledProcessCount(final EntityManager em, final String pDatabase) {
//        return getCount(em, pDatabase, "T_WM_PROCESS", "CANCEL_FL = 1");
//    }
    /**
     * Does the given database exist?
     *
     * @param pDatabase dbsys1:CPX_DEV, dbsys2:CPX and so on...
     * @return successful?
     */
    public static boolean isValidDatabase(final String pDatabase) {
        String database = (pDatabase == null) ? "" : pDatabase.trim();
        /*
    if (database.isEmpty()) {
      LOG.log(Level.SEVERE, "No database specified!");
      return false;
    }
         */
        List<String> databaseList = getAvailableConnectionStrings();
        Iterator<String> it = databaseList.iterator();
        while (it.hasNext()) {
            String connString = it.next();
            if (connString.equalsIgnoreCase(database)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the database for a following job (Batch Import or something
     * similar).
     *
     * @param pDatabase database
     * @return Job Session ID
     * @throws NoSuchElementException no such element
     */
    public static synchronized CpxJob createJobSession(final String pDatabase) {
        String database = (pDatabase == null) ? "" : pDatabase.trim();
        if (database.isEmpty()) {
            throw new IllegalArgumentException("No database specified!");
        }
        if (!isValidDatabase(database)) {
            throw new IllegalArgumentException("Database '" + database + "' does not exist!");
        }
        CpxUser cpxUser = getCurrentCpxUser();
        String jobClientId = "job" + getNewClientId();
        try {
            manipulateUserPrincipal(jobClientId);
        } catch (NoSuchElementException | IllegalStateException | IllegalArgumentException ex) {
            LOG.log(Level.SEVERE, "Was not able to set client id for a job!", ex);
            throw new IllegalStateException("Was not able to set client id for a job on database '" + pDatabase + "'!");
        }
        CpxJob cpxJobSession = new CpxJob(jobClientId, pDatabase, cpxUser);

        JOB_MAP.put(jobClientId, cpxJobSession);
        return cpxJobSession;
    }

    /**
     * Be careful with this!
     *
     * @param pId id
     */
    public static synchronized void manipulateUserPrincipal(final String pId) {
        Principal principal = getCallerPrincipal();
        if (principal == null) {
            throw new IllegalArgumentException("No caller principal found!");
        }
        String principalName = (principal.getName() == null) ? "" : principal.getName().toLowerCase().trim();
        if (principalName.startsWith("job")) {
            throw new IllegalArgumentException("Job session '" + principalName + "' within this request already exists!");
        }
        Field nameField;
        try {
            nameField = principal.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(principal, pId);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Was not able to change Caller Principal through Reflection API: " + ex.getMessage(), ex);
        }
    }

    /**
     * Returns a list of Job Sessions created by the given user (yeah, user is
     * really meant here, not client!)
     *
     * @param pCpxUserId Database-ID of CPX User
     * @return List of Jobs
     */
    public static Set<CpxJob> getCpxUserJobs(final Long pCpxUserId) {
        Set<CpxJob> cpxJobSet = new LinkedHashSet<>();
        if (pCpxUserId == null || pCpxUserId.equals(0L)) {
            return cpxJobSet;
        }
        Iterator<Map.Entry<String, CpxJob>> it = JOB_MAP.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, CpxJob> entry = it.next();
            if (entry == null || entry.getValue() == null) {
                continue;
            }
            CpxJob cpxJob = entry.getValue();
            if (pCpxUserId.equals(cpxJob.getUserId())) {
                cpxJobSet.add(cpxJob);
            }
        }
        return cpxJobSet;
    }

    /**
     * Returns a list of Job Sessions created by current user (yeah, user is
     * really meant here, not client!)
     *
     * @return set of cpx jobs
     */
    public static Set<CpxJob> getCurrentCpxUserJobs() {
        return getCpxUserJobs(getCurrentCpxUserId());
    }

    /**
     * Get actual database used by this session (primarily database of current
     * Job Session is returned, if it is empty the database of CPX User is
     * returned)
     *
     * @return database
     */
    public static String getActualDatabase() {
        String clientId = getCurrentClientId();
        if (clientId == null) {
            LOG.log(Level.WARNING, "Client ID is null!");
            return "";
        }

        if (clientId.startsWith("job")) {
            CpxJob cpxJob = getCurrentCpxJob();
            if (cpxJob == null) {
                return null;
            }
            return cpxJob.getDatabase();
        }

        CpxUser cpxUser = ClientManager.getCurrentCpxUser();
        String databaseTmp = "";
        if (cpxUser != null) {
            databaseTmp = cpxUser.getActualDatabase();
        }
        return databaseTmp;
    }

    @SuppressWarnings("unchecked")
    public static <T> T lookup(final Class<T> clazz) {
        BeanManager beanManager = null;
        try{
            beanManager = CDI.current().getBeanManager();
        }catch(IllegalStateException ex){
            LOG.severe("Can not SetUp CDI BeanManager!\nReason was:\n"+ex.getMessage());
        }
        if(beanManager == null){
            return null;
        }
        final Iterator<Bean<?>> iter = beanManager.getBeans(clazz).iterator();
        if (!iter.hasNext()) {
            throw new IllegalStateException("CDI BeanManager cannot find an instance of requested type " + clazz.getName());
        }
        final Bean<T> bean = (Bean<T>) iter.next();
        final CreationalContext<T> ctx = beanManager.createCreationalContext(bean);
        return (T) beanManager.getReference(bean, clazz, ctx);
    }

    /**
     * Returns Caller Principal
     *
     * @return principal
     */
    public static Principal getCallerPrincipal() {
        SessionContext ctx = getStaticSessionContext();
        if (ctx == null) {
            LOG.log(Level.SEVERE, "No session context found, cannot lookup current CPX Client!");
            return null;
        }
        Principal principal = ctx.getCallerPrincipal();
        return principal;
    }

    /**
     * Returns current Client ID (primarily ID of current Job Session is
     * returned, if it is empty the ID of CPX User is returned)
     *
     * @return client id
     */
    public static String getCurrentClientId() {
        Principal principal = getCallerPrincipal();
        if (principal == null) {
            LOG.log(Level.SEVERE, "No caller principal found, cannot lookup current CPX Client!");
            return null;
        }

        String username = principal.getName();
        if (username == null || username.trim().isEmpty()) {
            LOG.log(Level.SEVERE, "No JNDI user name found, cannot lookup current CPX Client!");
            return null;
        }
        return username;

    }

    /**
     * Returns current CPX Client ID (never returns Job Session ID!)
     *
     * @return current client id
     */
    public static String getCurrentCpxClientId() {
        String currentClientId = getCurrentClientId();
        if (currentClientId == null) {
            LOG.log(Level.WARNING, "Client ID is null!");
            return "";
        }

        if (currentClientId.startsWith("job")) {
            CpxJob cpxJob = getCurrentCpxJob();
            if (cpxJob == null) {
                return null;
            }
            if (cpxJob.getCpxUser() == null) {
                return null;
            }
            return cpxJob.getCpxUser().getClientId();
        }
        return currentClientId;
    }

    /**
     * Returns current CPX Job Session ID (never returns CPX Client ID!)
     *
     * @return job id
     */
    public static String getCurrentCpxJobId() {
        String currentClientId = getCurrentClientId();
        if (currentClientId == null) {
            LOG.log(Level.WARNING, "Client ID is null!");
            return "";
        }
        if (currentClientId.startsWith("job")) {
            return currentClientId;
        }
        return null;
    }

    /**
     * Returns database ID (CDB_USERS) of the current CPX User
     *
     * @return user id
     */
    public static Long getCurrentCpxUserId() {
        CpxUser cpxUser = getCurrentCpxUser();
        if (cpxUser == null) {
            return null;
        }
        return cpxUser.getUserId();
    }

    public static List<Long> getCurrentCpxRoleIds() {
        CpxUser cpxUser = getCurrentCpxUser();
        if (cpxUser == null) {
            return new ArrayList<>();
        }
        final List<CdbUserRoles> roles = cpxUser.getRoles();
        if (roles == null || roles.isEmpty()) {
            return new ArrayList<>();
        }
        final List<Long> roleIds = new ArrayList<>();
        for (CdbUserRoles role : roles) {
            if (role == null) {
                continue;
            }
            if (role.id == 0L) {
                continue;
            }
            roleIds.add(role.id);
        }
        return roleIds;
    }

    /**
     * Remove (logout) current CPX User from list of clients
     *
     * @return successful?
     */
    public static boolean removeCurrentCpxUser() {
        String clientId = getCurrentClientId();
        if (clientId == null) {
            return false;
        }
        return (removeCpxClient(clientId) != null);
    }

    /**
     * SessionContext (use this to get Caller Principal)
     *
     * @return session context
     */
    public static SessionContext getStaticSessionContext() {
        ClientManager clientManager = lookup(ClientManager.class);
        if(clientManager == null){
            return null;
        }
        SessionContext ctx = clientManager.getSessionContext();
        return ctx;
    }

    /**
     * Returns current CPX User
     *
     * @return cpx user
     */
    public static CpxUser getCurrentCpxUser() {
        String clientId = getCurrentCpxClientId();
        CpxUser cpxUser = ClientManager.getCpxClient(clientId);
        if (cpxUser == null) {
            //System.err.println("No CPX User found for Client ID " + clientId + "!");
            return null;
        }
        cpxUser.updateLastActionAt();

        return cpxUser;
    }

    public static Long getCurrentCpxRoleId() {
        CpxUser cpxUser = getCurrentCpxUser();
        if (cpxUser == null) {
            return null;
        }
        CdbUserRoles role = cpxUser.getActualRole();
        if (role == null) {
            return null;
        }
        return role.id;
    }

    /**
     * Returns current CPX Job
     *
     * @return cpx job
     */
    public static CpxJob getCurrentCpxJob() {
        String clientId = getCurrentCpxJobId();
        CpxJob cpxJob = ClientManager.getCpxJob(clientId);

        if (cpxJob == null) {
            LOG.log(Level.SEVERE, "No CPX Job found for Client ID {0}!", clientId);
            return null;
        }

        cpxJob.updateLastActionAt();

        return cpxJob;
    }

    /**
     * Returns new Client ID (is strictly incremented, every Client ID is
     * unique) Client ID is used for CPX Clients and CPX Jobs (shared number
     * range)
     *
     * @return new client id
     */
    public static int getNewClientId() {
        return CLIENT_ID_GENERATOR.incrementAndGet();
    }

    /**
     * Returns a list (as copy) of CPX Clients. (Can be used to clean up old
     * Clients that didn't logout correctly)
     *
     * @return list of cpx clients
     */
    public static Map<String, CpxUser> getCpxClients() {
        Map<String, CpxUser> newClientMap = new ConcurrentHashMap<>();
        newClientMap.putAll(CLIENT_MAP);
        return newClientMap;
    }

    /**
     * Returns a list (as copy) of CPX Jobs. (Can be used to clean up old Jobs
     * that didn't logout correctly)
     *
     * @return list of cpx jobs
     */
    public static Map<String, CpxJob> getCpxJobs() {
        Map<String, CpxJob> newJobMap = new ConcurrentHashMap<>();
        newJobMap.putAll(JOB_MAP);
        return newJobMap;
    }

    /**
     * Returns a list (as copy) of CPX Clients and Jobs. (Can be used to clean
     * up old Clients that didn't logout correctly)
     *
     * @return list of cpx clients and jobs
     */
    public static Map<String, CpxAbstractClient> getCpxAll() {
        Map<String, CpxAbstractClient> newAllMap = new ConcurrentHashMap<>();
        newAllMap.putAll(CLIENT_MAP);
        newAllMap.putAll(JOB_MAP);
        return newAllMap;
    }

    /**
     * Returns CPX Client with the given Client ID
     *
     * @param pCpxClientId cpx client id
     * @return cpx user
     */
    public static CpxUser getCpxClient(final String pCpxClientId) {
        if (pCpxClientId == null || pCpxClientId.trim().isEmpty()) {
            return null;
        }
        return CLIENT_MAP.get(pCpxClientId);
    }

    /**
     * Returns CPX Job with the given Job ID
     *
     * @param pJobId job id
     * @return cpx job
     */
    public static CpxJob getCpxJob(final String pJobId) {
        if (pJobId == null || pJobId.trim().isEmpty()) {
            return null;
        }
        return JOB_MAP.get(pJobId);
    }

    /**
     * Adds (logins) CPX Client to the list of clients.
     *
     * @param pCpxClientId cpx client id
     * @param pCpxUser cpx user
     * @return successful?
     */
    public static synchronized boolean addCpxClient(final String pCpxClientId, final CpxUser pCpxUser) {
        if (pCpxClientId == null || pCpxClientId.trim().isEmpty()) {
            return false;
        }
        if (pCpxUser == null) {
            return false;
        }
        if (CLIENT_MAP.containsKey(pCpxClientId)) {
            return false;
        }
        CLIENT_MAP.put(pCpxClientId, pCpxUser);
        return true;
    }

    /**
     * Removes (logout) CPX Client from the list of clients with a given Client
     * ID.
     *
     * @param pCpxClientId cpx client id
     * @return cpx user
     */
    public static synchronized CpxUser removeCpxClient(final String pCpxClientId) {
        if (pCpxClientId == null || pCpxClientId.trim().isEmpty()) {
            return null;
        }
        if (!CLIENT_MAP.containsKey(pCpxClientId)) {
            return null;
        }
        return CLIENT_MAP.remove(pCpxClientId);
    }

    public static CdbUserRoles getCpxRole(final Long pCpxRolesId) {
        if (pCpxRolesId == null || pCpxRolesId.equals(0L)) {
            return null;
        }
        return ROLES_MAP.get(pCpxRolesId);
    }

    public static Map<Long, CdbUserRoles> getCpxRoles() {
        Map<Long, CdbUserRoles> newRolesMap = new ConcurrentHashMap<>();
        newRolesMap.putAll(ROLES_MAP);
        return newRolesMap;
    }

    public static synchronized boolean addCpxRole(final Long pCpxRolesId, final CdbUserRoles pCpxRole) {
        if (pCpxRolesId == null || pCpxRolesId.equals(0L)) {
            return false;
        }
        if (pCpxRole == null) {
            return false;
        }
        if (ROLES_MAP.containsKey(pCpxRolesId)) {
            return false;
        }
        ROLES_MAP.put(pCpxRolesId, pCpxRole);
        return true;
    }

    public static synchronized CdbUserRoles removeCpxRole(final Long pCpxRolesId) {
        if (pCpxRolesId == null || pCpxRolesId.equals(0L)) {
            return null;
        }
        if (!ROLES_MAP.containsKey(pCpxRolesId)) {
            return null;
        }
        return ROLES_MAP.remove(pCpxRolesId);
    }

    public static String getCurrentCpxUserName() {
        CpxUser cpxUser = getCurrentCpxUser();
        if (cpxUser == null) {
            return null;
        }
        return cpxUser.getUserName();
    }

    public static AppTypeEn getAppType() {
        CpxUser cpxUser = getCurrentCpxUser();
        if (cpxUser == null) {
            return null;
        }
        return cpxUser.getAppType();
    }

    /**
     * SessionContext (use this to get Caller Principal)
     *
     * @return session context
     */
    public SessionContext getSessionContext() {
        return ctx;
    }

//    public static void updateCaseCount(final Integer pCount) {
//        final String connectionString = ClientManager.getActualDatabase();
//        if (connectionString.isEmpty()) {
//            LOG.log(Level.FINEST, "no actual database found");
//            return;
//        }
//        final CpxDatabase database = getDatabase(connectionString);
//        if (database == null) {
//            LOG.log(Level.FINEST, "no database found for connection string: {0}", connectionString);
//            return;
//        }
//        database.setCaseCount(pCount);
//    }
//
//    public static void updateProcessCount(final Integer pCount) {
//        final String connectionString = ClientManager.getActualDatabase();
//        if (connectionString.isEmpty()) {
//            LOG.log(Level.FINEST, "no actual database found");
//            return;
//        }
//        final CpxDatabase database = getDatabase(connectionString);
//        if (database == null) {
//            LOG.log(Level.FINEST, "no database found for connection string: {0}", connectionString);
//            return;
//        }
//        database.setProcessCount(pCount);
//    }
}
