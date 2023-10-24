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

import de.lb.cpx.exceptions.CpxDisconnectedException;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.server.commonDB.dao.CdbUsersDao;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.server.commons.dao.AbstractDao;
import static de.lb.cpx.server.commons.dao.AbstractDao.*;
import de.lb.cpx.server.commons.enums.DbDriverEn;
import de.lb.cpx.server.config.CpxServerConfig;
import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.service.information.ConnectionString;
import de.lb.cpx.service.information.DatabaseInfo;
import de.lb.cpx.shared.version.VersionHistory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;

/**
 *
 * @author Dirk Niemeier
 */
public final class CaseEntityManagerFactoryCreator {

    private static final ConcurrentHashMap<ConnectionString, EntityManagerFactoryEntry> FACTORYMAP = new ConcurrentHashMap<>();
    private static final Map<ConnectionString, Boolean> RECONNECTMAP = new ConcurrentHashMap<>();
    private static final Logger LOG = Logger.getLogger(CaseEntityManagerFactoryCreator.class.getName());

    private CaseEntityManagerFactoryCreator() {
        //Nothing here
    }

    public static synchronized EntityManager getEntityManager(final EntityManagerFactory emf) {
        if (emf == null) {
            return null;
        }
        return emf.createEntityManager(SynchronizationType.SYNCHRONIZED);
    }

    public static String getDbIdentifier(final String pConnectionString) {
        final DatabaseInfo dbInfo = getDatabaseInfo(pConnectionString);
        return dbInfo.getIdentifier();
//        CpxServerConfigLocal cpxServerConfig = ClientManager.lookup(CpxServerConfigLocal.class);
//        return cpxServerConfig.getDbIdentifier(pConnectionString);
    }

    public static DatabaseInfo getDatabaseInfo(final String pConnectionString) {
        final EntityManagerFactory emf = fetchEntityManagerFactory(pConnectionString).emf;
        final EntityManager em = getEntityManager(emf);
        if (em == null) {
            LOG.log(Level.SEVERE, "Entity Manager is null!");
            return new DatabaseInfo("", "", "", "");
        }
        //final String database = AbstractDao.getConnectionDatabase(em);
        final String url = AbstractDao.getConnectionUrl(em);
        //final String vendor = AbstractDao.getConnectionDatabaseVendor(em);
        //final String version = AbstractDao.getDatabaseVersion(em);
        //return new DatabaseInfo(database, url, vendor, version);
        return new DatabaseInfo(pConnectionString, url, "", "");
    }

//    /**
//     * Will return Exception if connection is lost
//     *
//     * @param pEmf emf
//     * @return error
//     */
//    public static synchronized Exception testEntityManagerFactory(final EntityManagerFactory pEmf) {
//        Exception ex = null;
//        if (pEmf == null) {
//            return ex;
//        }
//        EntityManager em = pEmf.createEntityManager();
//        String query;
//        //Try to submit a nonsense dummy query to proof if connection is still alive
//        if (isOracle(em)) {
//            //Oracle
//            query = "SELECT 1 FROM DUAL";
//        } else {
//            //Microsoft SQL Server
//            query = "SELECT 1";
//        }
//        em.createNativeQuery(query).getSingleResult();
//        em.close();
//        return ex;
//    }
//  private static synchronized boolean closeEntityManagerFactory(final EntityManagerFactory pEmf) {
//    if (pEmf == null) {
//      return true;
//    }
//    if (!pEmf.isOpen()) {
//      return true;
//    }
//    try {
//      pEmf.close();
//      return true;
//    } catch (Exception ex) {
//      //
//    }
//    return false;
//  }
    protected static void checkNewDatabaseString(final ConnectionString pConnectionString) throws CpxIllegalArgumentException {
        final String persistenceUnit = pConnectionString.getPersistenceUnit();
        final String database = pConnectionString.getDatabase();

        final String exampleDb = "dbsys1:NEUE_DB";

        if (pConnectionString.getConnectionString().isEmpty()) {
            //throw new IllegalArgumentException("Connection string is empty (expecting dbsys1:CPX_DEV or something similar)");
            LOG.log(Level.SEVERE, "Connection string is empty (expecting dbsys1:CPX_DEV or something similar)");
            throw new CpxIllegalArgumentException("Es wurde kein Name angegeben (Beispielwert: " + exampleDb + ")");
        }

        if (persistenceUnit.isEmpty()) {
            //throw new IllegalArgumentException("Persistence unit was not given in connection string '" + connString.getConnectionString() + "'");
            LOG.log(Level.SEVERE, "Persistence unit was not given in connection string '" + pConnectionString.getConnectionString() + "'");
            throw new CpxIllegalArgumentException("Es wurde kein Datenbanksystem (Beispielwert: " + exampleDb + ") angegeben: '" + pConnectionString.getConnectionString() + "'");
        }

        if (database.isEmpty()) {
            //Not database specified. It is not possible to recreate ALL databases just by specifing the persistence unit
            //return false;
            //throw new IllegalArgumentException("Database was not specified in connection string '" + connString.getConnectionString() + "'. It is not possible to recreate ALL databases just by specifing the persistence unit");
            LOG.log(Level.SEVERE, "Database was not specified in connection string '" + pConnectionString.getConnectionString() + "'. It is not possible to recreate ALL databases just by specifing the persistence unit");
            throw new CpxIllegalArgumentException("Es wurde kein Datenbankname angegeben (Beispielwert: " + exampleDb + "): '" + pConnectionString.getConnectionString() + "'");
        }

        final int maxDbNameLength = 30;
        if (database.length() > maxDbNameLength) {
            //Not database specified. It is not possible to recreate ALL databases just by specifing the persistence unit
            //return false;
            //throw new IllegalArgumentException("Database was not specified in connection string '" + connString.getConnectionString() + "'. It is not possible to recreate ALL databases just by specifing the persistence unit");
            LOG.log(Level.SEVERE, "Database name specified in connection string '" + pConnectionString.getConnectionString() + "' is too long (max. " + maxDbNameLength + " chars)");
            throw new CpxIllegalArgumentException("Der Datenbankname ist zu lang (maximal zulässig: " + maxDbNameLength + " Zeichen): '" + pConnectionString.getConnectionString() + "'");
        }

        final String pattern = "^([a-zA-Z]|[0-9]|_)*$";
        final Pattern p = Pattern.compile(pattern);
        final Matcher m = p.matcher(database);
        if (!m.matches()) {
            LOG.log(Level.SEVERE, "Database name specified in connection string '" + pConnectionString.getConnectionString() + "' contains illegal characters (pattern: " + pattern + ")");
            throw new CpxIllegalArgumentException("Der Datenbankname enthält ungültige Zeichen (erwartetes Muster: " + pattern + " Zeichen): '" + pConnectionString.getConnectionString() + "'");
        }

        if (!ClientManager.getAvailablePersistenceUnitNames().contains(persistenceUnit)) {
            //throw new IllegalArgumentException("Persistence unit is not valid in connection string '" + connString.getConnectionString() + "'");
            LOG.log(Level.SEVERE, "Persistence unit is not valid in connection string '" + pConnectionString.getConnectionString() + "'");
            throw new CpxIllegalArgumentException("Das angegebene Datenbanksystem " + pConnectionString.persistenceUnit + " existiert nicht: '" + pConnectionString.getConnectionString() + "'");
        }
    }

    /**
     * Be careful with this function! Parents are liable for their children
     * here!!!
     *
     * @param pConnectionString something like dbsys1:CPX_DEV
     * @param pUserId user id that creates the database
     * @param pOverwrite Force truncation
     * @return was successful?
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException thrown if
     * database exsists or could not be overwritten
     */
    public static synchronized boolean dropAndCreateDatabase(final String pConnectionString, final Long pUserId, final boolean pOverwrite) throws CpxIllegalArgumentException {
        ConnectionString connString = new ConnectionString(pConnectionString);

        LOG.log(Level.WARNING, "Preparing drop and create of database '" + connString.getConnectionString() + "'");

        final String persistenceUnit = connString.getPersistenceUnit();
        final String database = connString.getDatabase();

        checkNewDatabaseString(connString);

        boolean isValidDatabase = ClientManager.isValidDatabase(connString.getConnectionString());

        if (!pOverwrite) {
            if (isValidDatabase) {
                LOG.log(Level.SEVERE, "Database '" + connString.getConnectionString() + "' already exists");
                throw new CpxIllegalArgumentException("Die Datenbank '" + connString.getConnectionString() + "' existiert bereits!");
            }
        } else {
            if (!isValidDatabase) {
                LOG.log(Level.SEVERE, "Cannot overwrite Database '" + connString.getConnectionString() + "', database does not exist yet");
                throw new CpxIllegalArgumentException("Die Datenbank '" + connString.getConnectionString() + "' kann nicht überschrieben werden, da sie (noch) nicht existiert!");
            }
        }

        if (!isValidDatabase) {
            //Create database first time
            createDatabase(connString.connectionString);
        }

        Map<String, Level> errorList = checkDatabaseProperties(pConnectionString);
        if (!errorList.isEmpty()) {
            throw new CpxIllegalArgumentException(errorList.entrySet().iterator().next().getKey());
//            for (Map.Entry<String, Level> error : errorList.entrySet()) {
//                throw new CpxIllegalArgumentException(error.getKey());
//                //LOGGER.log(Level.INFO, error);
//            }
            //return false;
        }

        CpxServerConfigLocal cpxServerConfig = ClientManager.lookup(CpxServerConfigLocal.class);
        Map<String, String> props = cpxServerConfig.getDatabaseProperties(persistenceUnit, database);

        RECONNECTMAP.put(connString, false);

        //DANGEROUS LINE!
        props.put("hibernate.hbm2ddl.auto", "create-drop");

        EntityManagerFactory emf;
        boolean success = false;
        try {
            LOG.log(Level.WARNING, "Fasten your seat bealts, all data on '" + connString.getConnectionString() + "' is going to /dev/null now!");
            //emf = Persistence.createEntityManagerFactory(persistenceUnit, props);

            props.put("hibernate.hbm2ddl.auto", "create");
            emf = Persistence.createEntityManagerFactory(persistenceUnit, props);

            EntityManager em = CaseEntityManagerFactoryCreator.getEntityManager(emf);
            if (em == null) {
                LOG.log(Level.SEVERE, "Entity Manager is null!");
                return success;
            }

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date today = formatter.parse(formatter.format(new Date()));
            Date updateDate = Date.from(VersionHistory.getRecentPublishedVersion().getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

            final CdbUsers user;
            if (pUserId == null) {
                user = null;
            } else {
                CdbUsersDao usersDao = ClientManager.lookup(CdbUsersDao.class);
                user = usersDao.findById(pUserId);
            }

            String query = "INSERT INTO IN4MED (ID, CREATION_DATE, CREATION_USER, LAST_UPDATE, CREATED_BY, IS_NEW_DB) VALUES (1, :creation_date, :creation_user, :update_date, :created_by, 1)";
            javax.persistence.Query qry = em.createNativeQuery(query);
            qry.setParameter("creation_date", today);
            qry.setParameter("creation_user", user == null ? -1L : user.id);
            qry.setParameter("created_by", user == null ? "(undefined)" : user.getUName());
            qry.setParameter("update_date", updateDate); //don't use 'today' here!
            int ret = qry.executeUpdate();
            LOG.log(Level.INFO, ret + " entry in IN4MED was created");

            success = true;
            LOG.log(Level.WARNING, "Database recreation of '" + connString.getConnectionString() + "' was executed!");
        } catch (ParseException ex1) {
            RECONNECTMAP.put(connString, true);
            LOG.log(Level.SEVERE, "Database recreation of '" + connString.getConnectionString() + "' failed, database is probably corrupted now!", ex1);
            throw new CpxIllegalArgumentException("Bei der Erzeugung der Datenbank '" + connString.getConnectionString() + "' ist etwas schief gegangen. Die neue Datenbank ist möglicherweise beschädigt!");
        }
        return success;
    }

    /**
     *
     * @param pConnectionString something like dbsys1:CPX_DEV
     * @return List of errors
     */
    public static Map<String, Level> checkDatabaseProperties(final String pConnectionString) {
        Map<String, Level> errorList = new LinkedHashMap<>();
        String connectionString = (pConnectionString == null) ? "" : pConnectionString.trim();
        if (connectionString.isEmpty()) {
            //LOGGER.log(Level.SEVERE, "No connection string specified");
            errorList.put("No connection string specified", Level.SEVERE);
            return errorList;
        }

        ConnectionString connString = new ConnectionString(connectionString);
        final String persistenceUnit = connString.getPersistenceUnit();

        //CpxServerConfigLocal cpxServerConfig = ClientManager.lookup(CpxServerConfigLocal.class);
        CpxServerConfig cpxServerConfig = new CpxServerConfig();
        final String url = cpxServerConfig.getDatabaseUrl(persistenceUnit);
        final String user = cpxServerConfig.getDatabaseUser(persistenceUnit);
        final String driver = cpxServerConfig.getDatabaseDriver(persistenceUnit);

        if (url == null || url.isEmpty()) {
            errorList.put("Database URL for persistence unit '" + persistenceUnit + "' was not defined in CPX server configuration file. I'll skip this persistence unit.", Level.FINEST);
        } else {
            if (driver == null || driver.isEmpty()) {
                errorList.put("Cannot detect Database Driver for persistence unit '" + persistenceUnit + "'. The URL '" + url + "' seems to be neither Oracle nor Microsoft SQL Server! I'll skip this persistence unit.", Level.SEVERE);
            }
        }

        if (user == null || user.isEmpty()) {
            errorList.put("Database Username for persistence unit '" + persistenceUnit + "' was not defined in CPX server configuration file. I'll skip this persistence unit.", Level.FINEST);
        }

        return errorList;
    }

    public static boolean isNew(final String pConnectionString) {
        String connectionString = (pConnectionString == null) ? "" : pConnectionString.trim();
        if (connectionString.isEmpty()) {
            LOG.log(Level.SEVERE, "Cannot check entity manager factory, user has no database selected!");
            return false;
        }

        //Format of Database Parameter: PersistenceUnit:Database - so we have to split it now!
        ConnectionString connString = new ConnectionString(connectionString);

        //final String persistenceUnit = connString.getPersistenceUnit(); //dbsys1, dbsys2...
        //final String database = connString.getDatabase(); //CPX_DEV, CPX_DEV1...
        if (FACTORYMAP.get(connString) != null) {
            return false;
        }
        return true;
    }

    public static Connection getJdbcConnection(final String pConnectionString) throws SQLException {
        CpxServerConfigLocal cpxServerConfig = ClientManager.lookup(CpxServerConfigLocal.class);
        return cpxServerConfig.getJdbcConnection(pConnectionString);
    }

//
//    public static Connection getJdbcConnection(final String pConnectionString) throws SQLException {
//        String connectionString = (pConnectionString == null) ? "" : pConnectionString.trim();
//        if (connectionString.isEmpty()) {
//            LOG.log(Level.SEVERE, "Cannot fetch jdbc connection, user has no database selected!");
//            return null;
//        }
//
//        //Format of Database Parameter: PersistenceUnit:Database - so we have to split it now!
//        ConnectionString connString = new ConnectionString(connectionString);
//
//        final String persistenceUnit = connString.getPersistenceUnit(); //dbsys1, dbsys2...
//        final String database = connString.getDatabase(); //CPX_DEV, CPX_DEV1...
//
//        CpxServerConfigLocal cpxServerConfig = ClientManager.lookup(CpxServerConfigLocal.class);
//
//        // Falls nötig, Map-Eintrag für URL erstellen
//        //factoryMap.computeIfAbsent(key, u -> {
//        Map<String, String> props = cpxServerConfig.getDatabaseProperties(persistenceUnit, database);
//        if (props == null) {
//            LOG.log(Level.SEVERE, "No connection properties found for connection string '" + pConnectionString + "'. Is this persistence unit really defined in your cpx_server_config.xml?");
//            return null;
//        }
//
//        String url = props.get("javax.persistence.jdbc.url");
//        String user = props.get("javax.persistence.jdbc.user");
//        String password = props.get("javax.persistence.jdbc.password");
//
//        LOG.log(Level.INFO, "Try to establish jdbc connection with this url: " + url);
//        if (url == null || url.trim().isEmpty()) {
//            LOG.log(Level.SEVERE, "No connection url found for connection string '" + pConnectionString + "'. Is this persistence unit really defined in your cpx_server_config.xml?");
//        }
//
//        Connection connection = DriverManager.getConnection(url, user, password);
//
//        return connection;
//    }
    /**
     *
     * @param pConnectionString something like dbsys1:CPX_DEV
     * @return EntityManagerFactory
     */
    public static EntityManagerFactoryResult fetchEntityManagerFactory(final String pConnectionString) {
        String connectionString = (pConnectionString == null) ? "" : pConnectionString.trim();
        if (connectionString.isEmpty()) {
            LOG.log(Level.SEVERE, "Cannot fetch entity manager factory, user has no database selected!");
            return new EntityManagerFactoryResult(null, false);
        }

        //Format of Database Parameter: PersistenceUnit:Database - so we have to split it now!
        ConnectionString connString = new ConnectionString(connectionString);

//        final String persistenceUnit = connString.getPersistenceUnit(); //dbsys1, dbsys2...
//        final String database = connString.getDatabase(); //CPX_DEV, CPX_DEV1...
        final EntityManagerFactoryResult emfResult = getConnection(connString, true);
        if (emfResult != null) {
            return emfResult;
        }

        // Aktueller URL kommt aus anderem Service, z. B. "jdbc:h2:mem:showcase_1;DB_CLOSE_DELAY=-1"
        //String url = "jdbc:oracle:thin:@LBBatch-01:1522:ORCL12"; 
        //String url = "jdbc:oracle:thin:" + database + "/" + password + "@LBBatch-01:1522:ORCL12"; 
        EntityManagerFactoryResult result = createConnection(connString);

        /*
        if (!database.isEmpty()) {
        Iterator<Map.Entry<String, ConnectionString>> it = factoryMap.entrySet().iterator();
        while(it.hasNext()) {
        Map.Entry<String, ConnectionString> entry = it.next();
        ConnectionString connStrTmp = entry.getValue();
        if (connStrTmp.getDatabase().isEmpty() && connStrTmp.getPersistenceUnit().equalsIgnoreCase(connString.getConnectionString())) {
        connStrTmp.getChildren().add(connString);
        }
        }
        }
         */
        return result;
    }

    private static EntityManagerFactoryResult getConnection(final ConnectionString pConnString, final boolean pCheckIfAlive) {
        if (FACTORYMAP.get(pConnString) != null) {
            final EntityManagerFactoryEntry emfEntry = FACTORYMAP.get(pConnString);

            boolean reconnect = RECONNECTMAP.computeIfAbsent(pConnString, (t) -> {
                return true;
            });

            if (!reconnect || !pCheckIfAlive) {
                return new EntityManagerFactoryResult(emfEntry.emf, false);
            }

            //check if connection is alive. 
            //If not: close and clean this entry from FACTORYMAP and continue as a new connection to establish, otherwise return existing connection
            final String query;
            if (emfEntry.driver.isOracle()) {
                query = "SELECT 1 FROM DUAL";
            } else {
                query = "SELECT 1";
            }
            boolean connectionIsAlive;
            final EntityManager em = emfEntry.emf.createEntityManager();
            final Query qry = em.createNativeQuery(query);
            try {
                Object resultToDiscard = qry.getSingleResult(); //just for whatever. Ignore the result. We only want to see if an exception occurs here
                connectionIsAlive = true;
            } catch (PersistenceException ex) {
                LOG.log(Level.FINEST, "connection is dead: " + pConnString, ex);
                connectionIsAlive = false;
            }
            if (connectionIsAlive) {
                return new EntityManagerFactoryResult(emfEntry.emf, false);
            } else {
                LOG.log(Level.WARNING, "connection is dead, will discard and reinitialize it: {0}", pConnString);
                try {
                    em.close();
                } catch (RuntimeException ex) {
                    LOG.log(Level.FINEST, MessageFormat.format("cannot close EntityManager for connection: {0}", pConnString), ex);
                }
                removeConnection(pConnString);
                //emfEntry.emf.close();
                //FACTORYMAP.remove(connString);
            }
        }
        return null;
    }

    private static EntityManagerFactoryResult createConnection(final ConnectionString pConnString) {
// Falls nötig, Map-Eintrag für URL erstellen
        //factoryMap.computeIfAbsent(key, u -> {
        Map<String, Level> errorList = checkDatabaseProperties(pConnString.connectionString);
        if (!errorList.isEmpty()) {
            for (Map.Entry<String, Level> error : errorList.entrySet()) {
                LOG.log(error.getValue(), error.getKey());
            }
            return new EntityManagerFactoryResult(null, false);
        }

        final String persistenceUnit = pConnString.getPersistenceUnit(); //dbsys1, dbsys2...
        final String database = pConnString.getDatabase(); //CPX_DEV, CPX_DEV1...
        CpxServerConfigLocal cpxServerConfig = ClientManager.lookup(CpxServerConfigLocal.class);
        Map<String, String> props = cpxServerConfig.getDatabaseProperties(persistenceUnit, database);
        DbDriverEn driver = DbDriverEn.findByConnectionUrl(props.get("javax.persistence.jdbc.url"));

        final EntityManagerFactory newEmf;
        try {
            newEmf = Persistence.createEntityManagerFactory(persistenceUnit, props);
        } catch (RuntimeException ex) {
            LOG.log(Level.SEVERE, "was not able to establish to database '" + pConnString + "'", ex);
            Throwable e = ex;
            StringBuilder sb = new StringBuilder();
            while (true) {
                if (e == null) {
                    break;
                }
                if (e.getMessage() != null) {
                    sb.append("\r\n -> ").append(e.getMessage());
                }
                e = e.getCause();
            }
            throw new CpxDisconnectedException("Es konnte keine Verbindung zur Datenbank '" + pConnString + "' aufgebaut werden: " + sb.toString());
        }
        EntityManagerFactoryEntry newEntry = new EntityManagerFactoryEntry(newEmf, driver);
        FACTORYMAP.put(pConnString, newEntry);

        //Set parent
        if (!database.isEmpty()) {
            Iterator<Map.Entry<ConnectionString, EntityManagerFactoryEntry>> it = FACTORYMAP.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<ConnectionString, EntityManagerFactoryEntry> entry = it.next();
                ConnectionString connStrTmp = entry.getKey();
                if (connStrTmp.getDatabase().isEmpty() && connStrTmp.getPersistenceUnit().equalsIgnoreCase(pConnString.getPersistenceUnit())) {
                    pConnString.setParent(connStrTmp);
                    break;
                }
            }
        }

        EntityManagerFactoryResult result = new EntityManagerFactoryResult(newEntry.emf, true);

        if (result.emf != null && driver.isOracle()) {
            //SET NLSSORT and NLSCOMP
            String[] queries = new String[]{
                "ALTER SESSION SET NLS_COMP=LINGUISTIC",
                "ALTER SESSION SET NLS_SORT=BINARY_CI"
            };
            try (Connection conn = getJdbcConnection(pConnString.connectionString)) {
                for (final String query : queries) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(query);
                    } catch (SQLException ex) {
                        LOG.log(Level.SEVERE, MessageFormat.format("Cannot run initialization query on Oracle DB {0}: {1}", new Object[]{pConnString.connectionString, query}), ex);
                    }
                }
                final String query = "SELECT * FROM nls_session_parameters WHERE parameter IN ('NLS_COMP', 'NLS_SORT')";
                LOG.log(Level.INFO, "Oracle Session parameters for sorting and comparison on {0}: ", pConnString.connectionString);
                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        LOG.log(Level.INFO, "{0} = {1}", new Object[]{rs.getString("PARAMETER"), rs.getString("VALUE")});
                    }
                }
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, MessageFormat.format("Cannot initialize JDBC connection for {0}", pConnString.connectionString), ex);
            }
        }

        return result;
    }

    public static Date getLastDate(final String pConnectionString) throws SQLException, CpxIllegalArgumentException {
        ConnectionString connString = new ConnectionString(pConnectionString);
        String database = connString.getDatabase();
        String persistenceUnit = connString.getPersistenceUnit();

        LOG.log(Level.WARNING, "I'll drop the database '" + connString.getConnectionString() + " now!...'");

        if (connString.getConnectionString().isEmpty()) {
            throw new CpxIllegalArgumentException("Connection string is empty (expecting dbsys1:CPX_DEV or something similar)");
        }

        if (persistenceUnit.isEmpty()) {
            throw new CpxIllegalArgumentException("Persistence unit was not given in connection string '" + connString.getConnectionString() + "'");
        }

        if (database.isEmpty()) {
            //Not database specified. It is not possible to recreate ALL databases just by specifing the persistence unit
            //return false;
            throw new CpxIllegalArgumentException("Database was not specified in connection string '" + connString.getConnectionString() + "'. It is not possible to recreate ALL databases just by specifing the persistence unit");
        }

        if (!ClientManager.getAvailablePersistenceUnitNames().contains(persistenceUnit)) {
            throw new CpxIllegalArgumentException("Persistence unit is not valid in connection string '" + connString.getConnectionString() + "'");
        }

        boolean isValidDatabase = ClientManager.isValidDatabase(connString.getConnectionString());
        if (!isValidDatabase) {
            LOG.log(Level.WARNING, "Database '" + connString.getConnectionString() + " does not exist, nothing to drop!'");
            return null;
        }

        try (Connection conn = getJdbcConnection(pConnectionString)) {

            if (conn == null) {
                LOG.log(Level.SEVERE, "connection is null!");
                return null;
            }
            String sql = "SELECT\n"
                    + "  CASE WHEN MAX_CREATION_DATE > MAX_MODIFICATION_DATE AND MAX_CREATION_DATE IS NOT NULL THEN MAX_CREATION_DATE ELSE MAX_MODIFICATION_DATE END LAST_DATE \n"
                    + "FROM (\n"
                    + "  SELECT \n"
                    + "    MAX(MAX_CREATION_DATE) MAX_CREATION_DATE, \n"
                    + "    MAX(MAX_MODIFICATION_DATE) MAX_MODIFICATION_DATE\n"
                    + "  FROM (\n"
                    // + "    SELECT 'T_BATCH_CHECK_RESULT' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_BATCH_CHECK_RESULT UNION ALL \n"
                    // + "    SELECT 'T_BATCH_RESULT' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_BATCH_RESULT UNION ALL \n"
                    // + "    SELECT 'T_BATCH_RESULT_2_ROLE' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_BATCH_RESULT_2_ROLE UNION ALL \n"
                    + "    SELECT 'T_CASE' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_CASE UNION ALL \n"
                    + "    SELECT 'T_CASE_BILL' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_CASE_BILL UNION ALL \n"
                    //  + "    SELECT 'T_CASE_COMMENT' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_CASE_COMMENT UNION ALL \n"
                    + "    SELECT 'T_CASE_DEPARTMENT' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_CASE_DEPARTMENT UNION ALL \n"
                    + "    SELECT 'T_CASE_DETAILS' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_CASE_DETAILS UNION ALL \n"
                    + "    SELECT 'T_CASE_FEE' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_CASE_FEE UNION ALL \n"
                    + "    SELECT 'T_CASE_ICD' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_CASE_ICD UNION ALL \n"
                    // + "    SELECT 'T_CASE_ICD_GROUPED' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_CASE_ICD_GROUPED UNION ALL \n"
                    // + "    SELECT 'T_CASE_MERGE_MAPPING' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_CASE_MERGE_MAPPING UNION ALL \n"
                    + "    SELECT 'T_CASE_OPS' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_CASE_OPS UNION ALL \n"
                    // + "    SELECT 'T_CASE_OPS_GROUPED' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_CASE_OPS_GROUPED UNION ALL \n"
                    // + "    SELECT 'T_CASE_PEPP_GRADES' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_CASE_PEPP_GRADES UNION ALL \n"
                    // + "    SELECT 'T_CASE_SUPPL_FEE' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_CASE_SUPPL_FEE UNION ALL \n"
                    // + "    SELECT 'T_CASE_WARD' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_CASE_WARD UNION ALL \n"
                    // + "    SELECT 'T_CHECK_RESULT' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_CHECK_RESULT UNION ALL \n"
                    // + "    SELECT 'T_GROUPING_RESULTS' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_GROUPING_RESULTS UNION ALL \n"
                    + "    SELECT 'T_INSURANCE' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_INSURANCE UNION ALL \n"
                    // + "    SELECT 'T_LOCK' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_LOCK UNION ALL \n"
                    + "    SELECT 'T_PATIENT' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_PATIENT UNION ALL \n"
                    + "    SELECT 'T_PATIENT_DETAILS' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_PATIENT_DETAILS UNION ALL \n"
                    // "    -- SELECT 'T_P301_INKA' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_P301_INKA UNION ALL \n" +
                    // "    -- SELECT 'T_P301_KAIN' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_P301_KAIN UNION ALL \n" +
                    // "    SELECT 'T_P301_KAIN_INKA' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_P301_KAIN_INKA UNION ALL \n" + 
                    // "    SELECT 'T_P301_KAIN_INKA_PVT' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_P301_KAIN_INKA_PVT UNION ALL \n"
                    // "    SELECT 'T_P301_KAIN_INKA_PVV' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_P301_KAIN_INKA_PVV UNION ALL \n"
                    // "    SELECT 'T_ROLE_2_CHECK' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_ROLE_2_CHECK UNION ALL \n"
                    // "    SELECT 'T_ROLE_2_RESULT' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_ROLE_2_RESULT UNION ALL \n"
                    // "    SELECT 'T_SAP_FI_BILL' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_SAP_FI_BILL UNION ALL \n"
                    // "    SELECT 'T_SAP_FI_BILLPOSITION' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_SAP_FI_BILLPOSITION UNION ALL \n"
                    // "    SELECT 'T_SAP_FI_OPEN_ITEMS' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_SAP_FI_OPEN_ITEMS UNION ALL \n"
                    + "    SELECT 'T_WM_ACTION' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_WM_ACTION UNION ALL \n"
                    + "    SELECT 'T_WM_DOCUMENT' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_WM_DOCUMENT UNION ALL \n"
                    + "    SELECT 'T_WM_EVENT' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_WM_EVENT UNION ALL \n"
                    // "    -- SELECT 'T_WM_KEYWORDS' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_WM_KEYWORDS UNION ALL \n" +
                    + "    SELECT 'T_WM_PROCESS' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_WM_PROCESS UNION ALL \n"
                    + "    SELECT 'T_WM_PROCESS' TBL, NULL MAX_CREATION_DATE, MAX(LAST_PROCESS_MODIFICATION) MAX_MODIFICATION_DATE FROM T_WM_PROCESS UNION ALL \n"
                    // + "    -- SELECT 'T_WM_PROCESS_HOSPITAL' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_WM_PROCESS_HOSPITAL UNION ALL \n" +
                    + "    SELECT 'T_WM_PROCESS_HOSPITAL_FINALIS' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_WM_PROCESS_HOSPITAL_FINALIS UNION ALL \n"
                    // "    -- SELECT 'T_WM_PROCESS_INSURANCE' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_WM_PROCESS_INSURANCE UNION ALL \n" +
                    + "    SELECT 'T_WM_PROCESS_T_CASE' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_WM_PROCESS_T_CASE UNION ALL \n"
                    + "    SELECT 'T_WM_REMINDER' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_WM_REMINDER UNION ALL \n"
                    + "    SELECT 'T_WM_REQUEST' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_WM_REQUEST\n"
                    + // "    -- SELECT 'T_WM_REQUEST_AUDIT' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_WM_REQUEST_AUDIT UNION ALL \n" +
                    // "    -- SELECT 'T_WM_REQUEST_BEGE' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_WM_REQUEST_BEGE UNION ALL \n" +
                    // "    -- SELECT 'T_WM_REQUEST_MDK' TBL, MAX(CREATION_DATE) MAX_CREATION_DATE, MAX(MODIFICATION_DATE) MAX_MODIFICATION_DATE FROM T_WM_REQUEST_MDK\n" +
                    "  ) TMP\n"
                    + ") TMP";
            //sql = sql.replace("[DB]", connString.database);
            Date lastDate = null;
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        lastDate = rs.getTimestamp("LAST_DATE");
                    }
                }
            }
            return lastDate;
        }
    }

    public static synchronized boolean dropDatabase(final String pConnectionString) throws CpxIllegalArgumentException {
        ConnectionString connString = new ConnectionString(pConnectionString);
        String database = connString.getDatabase();
        //String persistenceUnit = connString.getPersistenceUnit();

        LOG.log(Level.WARNING, "I'll drop the database '" + connString.getConnectionString() + " now!...'");

        checkNewDatabaseString(connString);
//        if (connString.getConnectionString().isEmpty()) {
//            throw new IllegalArgumentException("Connection string is empty (expecting dbsys1:CPX_DEV or something similar)");
//        }
//
//        if (persistenceUnit.isEmpty()) {
//            throw new IllegalArgumentException("Persistence unit was not given in connection string '" + connString.getConnectionString() + "'");
//        }
//
//        if (database.isEmpty()) {
//            //Not database specified. It is not possible to recreate ALL databases just by specifing the persistence unit
//            //return false;
//            throw new IllegalArgumentException("Database was not specified in connection string '" + connString.getConnectionString() + "'. It is not possible to recreate ALL databases just by specifing the persistence unit");
//        }
//
//        if (!ClientManager.getAvailablePersistenceUnitNames().contains(persistenceUnit)) {
//            throw new IllegalArgumentException("Persistence unit is not valid in connection string '" + connString.getConnectionString() + "'");
//        }

        boolean isValidDatabase = ClientManager.isValidDatabase(connString.getConnectionString());
        if (!isValidDatabase) {
            LOG.log(Level.WARNING, "Database '" + connString.getConnectionString() + " does not exist, nothing to drop!'");
            return false;
        }

        boolean success = false;

        EntityManagerFactory emf;
        EntityManager em;
        //Kill Entity Manager Factory before low level database dropping can start
        final EntityManagerFactoryResult emfResult = fetchEntityManagerFactory(connString.getPersistenceUnit() + ":");
        emf = emfResult.emf;
//        final EntityManagerFactoryEntry emfEntry = FACTORYMAP.get(connString);
        RECONNECTMAP.put(connString, false);
        /*
      try {
        EntityManagerFactory emfTmp = fetchEntityManagerFactory(connString.getConnectionString());
        if (emfTmp != null) {
          closeEntityManagerFactory(emfTmp);
          emfTmp = null;
        }
      } catch (Exception exTmp) {
        //
      }
         */
        em = getEntityManager(emf);
        if (em == null) {
            LOG.log(Level.SEVERE, "Entity Manager is null!");
            return success;
        }

        int maxTry = 3;
        if (isOracle(em)) {
            //DROP DATABASE FOR ORACLE HERE
            //Kill open user sessions on this database
            for (int i = 1; i <= maxTry; i++) {
                //Kill 'em all, up to three times!
                /*
            //em.createNativeQuery("ALTER USER " + database + " ACCOUNT LOCK").executeUpdate();
            Query query = em.createNativeQuery("SELECT 'ALTER SYSTEM DISCONNECT SESSION ''' || sid || ',' || serial# || ''' IMMEDIATE' QRY from v$session where username = '" + database + "' and status <> 'KILLED'");
            List<String> queries = query.getResultList();
            if (queries != null) {
              for(String qry: queries) {
                if (qry == null || qry.trim().isEmpty()) {
                  continue;
                }
                em.createNativeQuery(qry).executeUpdate();
              }
            }

            em.createNativeQuery("DROP USER " + database + " CASCADE").executeUpdate();
                 */

                //Drop user forcefully!
                //Look at here: http://dba.stackexchange.com/questions/12439/trouble-killing-sessions-then-immediately-dropping-users-in-oracle-11g-xe
                String script
                        = String.format("DECLARE \n"
                                + "  l_cnt integer; \n"
                                + "BEGIN \n"
                                + "  EXECUTE IMMEDIATE 'alter user %s account lock'; \n"
                                + "  FOR x IN (SELECT * \n"
                                + "              FROM v$session \n"
                                + "             WHERE username = '%s') \n"
                                + "  LOOP \n"
                                + "    EXECUTE IMMEDIATE 'alter system disconnect session ''' || x.sid || ',' || x.serial# || ''' IMMEDIATE'; \n"
                                + "  END LOOP; \n"
                                + " \n"
                                + "  -- Wait for as long as it takes for all the sessions to go away \n"
                                + "  LOOP \n"
                                + "    SELECT COUNT(*) \n"
                                + "      INTO l_cnt \n"
                                + "      FROM v$session \n"
                                + "     WHERE username = '%s'; \n"
                                + "    EXIT WHEN l_cnt = 0; \n"
                                + "    dbms_lock.sleep( 2 ); \n"
                                + "  END LOOP; \n"
                                + " \n"
                                + "  EXECUTE IMMEDIATE 'drop user %s cascade'; \n"
                                + "END;", database, database, database, database);

                int result = em.createNativeQuery("BEGIN " + script + " END;").executeUpdate();
               // would never return result > 0, because The executeUpdate() returns either the row count for INSERT, 
                // UPDATE or DELETE statements, or 0 for SQL statements that return nothing
////                if (result > 0) {
////                    break;
////                }
// check, whether DB exists
                if(!ClientManager.isValidDatabase(connString.getConnectionString())){
                    success = true;
                    break;
                }
            }
        } else if (isSqlSrv(em)) {
            //DROP DATABASE FOR MSSQL HERE
            for (int i = 1; i <= maxTry; i++) {
                //Kill 'em all, up to three times!
                em.createNativeQuery(String.format("ALTER DATABASE %s SET SINGLE_USER WITH ROLLBACK IMMEDIATE", database)).executeUpdate();

                //em.createNativeQuery(String.format("ALTER DATABASE %s SET OFFLINE", database)).executeUpdate();
                int result = em.createNativeQuery(String.format("DROP DATABASE %s", database)).executeUpdate();
                
                // would never return result > 0, because The executeUpdate() returns either the row count for INSERT, 
                // UPDATE or DELETE statements, or 0 for SQL statements that return nothing
////                if (result > 0) {
////                    break;
////                }
// check, whether DB exists
                if(!ClientManager.isValidDatabase(connString.getConnectionString())){
                    success = true;
                    break;
                }
            }
        } else {
            throw new CpxIllegalArgumentException("Illegal DBMS");
        }
        if (success) {
            //FACTORYMAP.remove(connString);
            removeConnection(connString);
        } else {
            RECONNECTMAP.put(connString, true);
        }
        return success;
    }

    private static void removeConnection(final ConnectionString pConnString) {
        final EntityManagerFactoryEntry emfEntry = FACTORYMAP.get(pConnString);
        if (emfEntry == null) {
            return;
        }
        try {
            emfEntry.emf.close();
        } catch (RuntimeException ex) {
            LOG.log(Level.FINEST, "cannot close EntityManagerFactory for connection: " + pConnString, ex);
        }

        FACTORYMAP.remove(pConnString);
        RECONNECTMAP.remove(pConnString);
    }

    /**
     * Create database
     *
     * @param pConnectionString e.g. dbsys1:CPX_NEW
     * @return successful?
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException thrown if
     * database could not be created due to illegal dbms (only oracle and mssql
     * are allowed, or Databasename is somehow invalid
     */
    public static synchronized boolean createDatabase(final String pConnectionString) throws CpxIllegalArgumentException {
        ConnectionString connString = new ConnectionString(pConnectionString);
        String database = connString.getDatabase();
        //String persistenceUnit = connString.getPersistenceUnit();

        LOG.log(Level.WARNING, "I'll create the brandly new database '" + connString.getConnectionString() + " now!...'");

        checkNewDatabaseString(connString);

        boolean isValidDatabase = ClientManager.isValidDatabase(connString.getConnectionString());
        if (isValidDatabase) {
            LOG.log(Level.WARNING, "Database '" + connString.getConnectionString() + " already exists!'");
            return false;
        }

        boolean success = false;

        EntityManagerFactory emf;
        emf = fetchEntityManagerFactory(connString.getPersistenceUnit() + ":").emf;
        EntityManager em = getEntityManager(emf);
        if (em == null) {
            LOG.log(Level.SEVERE, "Entity Manager is null!");
            return success;
        }

        if (isOracle(em)) {
            //DNi 2017-08-29: In Oracle 12c you have to add C## or c## as a 
            //user prefix, but you can avoid this with _ORACLE_SCRIPT="true"
            em.createNativeQuery("ALTER SESSION SET \"_ORACLE_SCRIPT\"=true").executeUpdate();
            //CREATE DATABASE FOR ORACLE HERE
            em.createNativeQuery(String.format("CREATE USER %s\n"
                    + "  IDENTIFIED BY oracle\n"
                    + "  DEFAULT Tablespace users\n"
                    + "  TEMPORARY TABLESPACE temp\n"
                    + "  QUOTA UNLIMITED ON users", database)).executeUpdate();

            em.createNativeQuery(String.format("GRANT \n"
                    + "  CONNECT, RESOURCE, CREATE SESSION, CREATE TABLE, CREATE VIEW, \n"
                    + "  CREATE PROCEDURE, CREATE SYNONYM, ALTER ANY TABLE, ALTER ANY PROCEDURE,\n"
                    + "  DROP ANY TABLE, DROP ANY VIEW, DROP ANY PROCEDURE, DROP ANY SYNONYM,\n"
                    + "  CREATE ANY DIRECTORY, DROP ANY DIRECTORY, ADVISOR, CREATE MATERIALIZED VIEW, \n"
                    + "  SELECT_CATALOG_ROLE, SELECT ANY DICTIONARY, ADMINISTER SQL TUNING SET \n"
                    + "TO %s", database)).executeUpdate();

            em.createNativeQuery(String.format("ALTER USER %s ACCOUNT UNLOCK", database)).executeUpdate();
            success = true;
        } else if (isSqlSrv(em)) {
            //CREATE DATABASE FOR MSSQL HERE
            em.createNativeQuery(String.format("CREATE DATABASE %s", database)).executeUpdate();
            em.createNativeQuery(String.format("ALTER DATABASE %s SET AUTO_CLOSE OFF", database)).executeUpdate();
            success = true;
        } else {
            throw new CpxIllegalArgumentException("Illegal DBMS");
        }
        em.close();
        return success;
    }

    public static class EntityManagerFactoryResult {

        public final EntityManagerFactory emf;
        public final boolean isNew;
        private boolean autoReconnect = true;

        public EntityManagerFactoryResult(final EntityManagerFactory pEmf, final boolean pIsNew) {
            emf = pEmf;
            isNew = pIsNew;
        }

        public boolean doAutoReconnect() {
            return autoReconnect;
        }

        public void setAutoReconnect(final boolean pAutoReconnect) {
            autoReconnect = pAutoReconnect;
        }

    }

    public static class EntityManagerFactoryEntry {

        public final EntityManagerFactory emf;
        public final DbDriverEn driver;

        public EntityManagerFactoryEntry(final EntityManagerFactory pEmf, final DbDriverEn pDriver) {
            emf = pEmf;
            driver = pDriver;
        }

    }

}
