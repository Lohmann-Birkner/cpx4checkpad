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
package de.lb.cpx.client.core.connection.database;

import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.service.information.CpxCatalogOverview;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxDbManager {

    private static CpxDbManager instance = null;
    private static final Logger LOG = Logger.getLogger(CpxDbManager.class.getName());
    protected final Map<String, Connection> connectionList = new HashMap<>();
    public static final String CATALOG_DIR = CpxSystemProperties.getInstance().getCpxClientCatalogDir();

    public static String getCatalogDir() {
        return CATALOG_DIR;
    }

    public static synchronized CpxDbManager instance() {
        if (instance == null) {
            instance = new CpxDbManager();
        }
        return instance;
    }

    public static String buildName(final String pName) {
        String name = pName == null ? "" : pName.trim().toLowerCase();
        if (name.isEmpty()) {
            Logger.getLogger(CpxDbManager.class.getName()).log(Level.WARNING, "No connection name given");
            return "";
        }
        if (!isValidName(name)) {
            Logger.getLogger(CpxDbManager.class.getName()).log(Level.WARNING, "Given connection name ('" + name + "') has invalid syntax");
            return "";
        }
        return name;
    }

    public static String buildConnectionUrl(final String pName) {
        String name = buildName(pName);
        String url = "";
        if (!name.isEmpty()) {
            //String path = CpxSystemProperties.getInstance().getCpxClientCatalogDir();
            createDatabaseDir(CATALOG_DIR);
            url = "jdbc:sqlite:///" + CATALOG_DIR + name + ".db";
        }
        return url;
    }

    protected static synchronized boolean createDatabaseDir(final String pDirectory) {
        if (pDirectory == null || pDirectory.trim().isEmpty()) {
            return true;
        }
        final File pFile = new File(pDirectory);
        if (pFile.exists() && pFile.isDirectory()) {
            return true;
        }
        if (pFile.setExecutable(false)) {
            LOG.log(Level.FINEST, "Was successfully set to unexecutable: " + pFile.getName());
        }
        if (pFile.setReadable(true)) {
            LOG.log(Level.FINEST, "Was successfully set to readable: " + pFile.getName());
        }
        if (pFile.setWritable(true)) {
            LOG.log(Level.FINEST, "Was successfully set to writeable: " + pFile.getName());
        }
//        pFile.setExecutable(false);
//        pFile.setReadable(true);
//        pFile.setWritable(true);
        if (!pFile.mkdirs()) {
            //throw new FileSystemException("Was not able to create directory '" + pFile.getAbsolutePath() + "'");
            LOG.log(Level.SEVERE, "Was not able to create directory '" + pFile.getAbsolutePath() + "'");
        }
        return pFile.exists() && pFile.isDirectory();
    }

    /*
  * Look at http://stackoverflow.com/questions/6730009/validate-a-file-name-on-windows
     */
    public static boolean isValidName(String text) {
        Pattern pattern = Pattern.compile(
                "# Match a valid Windows filename (unspecified file system).          \n"
                + "^                                # Anchor to start of string.        \n"
                + "(?!                              # Assert filename is not: CON, PRN, \n"
                + "  (?:                            # AUX, NUL, COM1, COM2, COM3, COM4, \n"
                + "    CON|PRN|AUX|NUL|             # COM5, COM6, COM7, COM8, COM9,     \n"
                + "    COM[1-9]|LPT[1-9]            # LPT1, LPT2, LPT3, LPT4, LPT5,     \n"
                + "  )                              # LPT6, LPT7, LPT8, and LPT9...     \n"
                + "  (?:\\.[^.]*)?                  # followed by optional extension    \n"
                + "  $                              # and end of string                 \n"
                + ")                                # End negative lookahead assertion. \n"
                + "[^<>:\"/\\\\|?*\\x00-\\x1F]*     # Zero or more valid filename chars.\n"
                + "[^<>:\"/\\\\|?*\\x00-\\x1F\\ .]  # Last char is not a space or dot.  \n"
                + "$                                # Anchor to end of string.            ",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
        Matcher matcher = pattern.matcher(text);
        boolean isMatch = matcher.matches();
        return isMatch;
    }

    public static String toParam(final String pValue) {
        if (pValue == null) {
            return "";
        }
        String value = pValue.trim();
        if (value.isEmpty()) {
            return value;
        }
        value = value.replace("*", "%");
        value = value.replace("?", "_");
        return value;
    }

    public static String QStr(String value) {
        if (value == null) {
            return "null";
        }
        value = value.replace("'", "''");
        return "'" + value + "'";
    }

    public static String QStr(BigDecimal value) {
        if (value == null) {
            return "null";
        }
        return String.valueOf(value.doubleValue());
    }

    public static String QStr(Number value) {
        if (value == null) {
            return "null";
        }
        return String.valueOf(value.longValue());
    }

    public static String QStr(Double value) {
        if (value == null) {
            return "null";
        }
        return String.valueOf(value);
    }

    public static String QStr(final Boolean value) {
        if (value == null) {
            return "null";
        }
        if (value) {
            return "1";
        }
        return "0";
    }

    public static String QStr(final Date value) {
        if (value == null) {
            return "null";
        }
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
        return QStr(dateStr);
    }
    //final public static String LANGUAGE = "cpx_language";
    //final public static String CATALOG = "cpx_catalog";

    protected CpxDbManager() {
        //
    }

    public synchronized Connection getConnection(final String pName) {
        return getConnection(pName, true);
    }

    public synchronized Connection getConnection(final String pName, final boolean pAutoCreateConnection) {
        String name = buildName(pName);
        //return sqlite_conn;
        if (connectionList.get(name) != null) {
            return connectionList.get(name);
        }
        if (pAutoCreateConnection) {
            try {
                Connection conn = testConnection(name);
                connectionList.put(name, conn);
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Cannot create database connection '" + pName + "'", ex);
            }
        }

        Connection conn = connectionList.get(name);
        if (conn == null) {
            LOG.log(Level.WARNING, "No SQLite database found for '" + pName + "' (pAutoCreateConnection=" + pAutoCreateConnection + ")!");
        } else {
            try ( Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA case_sensitive_like=OFF");
                //Busy timeout set to 60000 milliseconds
                //because of this error: java.sql.SQLException: [SQLITE_BUSY] The database file is locked (database is locked)
                //more information: https://www.sqlite.org/c3ref/busy_handler.html
                stmt.execute("PRAGMA busy_timeout=60000");
                //stmt.execute("PRAGMA journal_mode=OFF");
                //stmt.execute("PRAGMA automatic_index=OFF");
                //stmt.execute("PRAGMA ignore_check_constraints=ON");
                //stmt.execute("PRAGMA page_size=8192");
                //stmt.execute("PRAGMA synchronous=OFF");
                //stmt.execute("PRAGMA temp_store=MEMORY");
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Cannot set SQLite pragma", ex);
            }
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }

        return conn;
    }

    public synchronized Connection testConnection(final String pName) throws SQLException {
        String url = buildConnectionUrl(pName);
        //Class.forName("org.sqlite.JDBC");
        Properties prop = new java.util.Properties();
        prop.put("Pooling", "True");
        prop.put("Max Pool Size", "100");
        return DriverManager.getConnection(url, prop);
    }

    public boolean isConnected(final String pName) {
        Connection conn = getConnection(pName, false);
        if (conn == null) {
            return false;
        }
        try {
            if (!conn.isClosed()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CpxDbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public synchronized boolean stop(final String pName) {
        if (!isConnected(pName)) {
            return false;
        }
        Connection conn = getConnection(pName, false);
        if (conn == null) {
            return true;
        }
        try {
            conn.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CpxDbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public synchronized int stopAll() {
        int closedConnections = 0;
        for (Map.Entry<String, Connection> entry : connectionList.entrySet()) {
            String name = entry.getKey();
            if (stop(name)) {
                closedConnections++;
            }
        }
        return closedConnections;
    }

    /* LANGUAGE */
    public Connection getLanguageDb() {
        return getConnection(DATABASE.cpx_language.name());
    }

    public Connection testLanguageDb() throws SQLException {
        return testConnection(DATABASE.cpx_language.name());
    }

    /* CATALOG */

 /*
  public Connection getCatalogDb(final Integer pYear) {
    String name = DATABASE.cpx_catalog.name();
    if (pYear != null) { //&& pYear > 0
      name += "_" + pYear;
    }
    return getConnection(name);
  }
     */
    public Connection getCatalogDb(final CatalogTypeEn pCatalogType, final Integer pYear, final boolean pAutoCreateConnection) {
        String catalog = (pCatalogType == null) ? "" : pCatalogType.name();
        return getCatalogDb(catalog, pYear, pAutoCreateConnection);
    }

    public Connection getCatalogDb(final String pCatalog, final Integer pYear, final boolean pAutoCreateConnection) {
        String catalog = (pCatalog == null) ? "" : pCatalog.trim().toLowerCase();
        int year = (pYear == null) ? 0 : pYear;
        String name = DATABASE.cpx_catalog.name();
        if (year > 0 || !catalog.isEmpty()) {
            if (year > 0) {
                name += "_" + catalog + "_" + year;
            } else {
                name += "_" + catalog;
            }
        }
        return getConnection(name, pAutoCreateConnection);
    }

    public Connection getCatalogDb(final CpxCatalogOverview pCatalogOverview, final boolean pAutoCreateConnection) {
        Integer year = null;
        String catalog = "";
        if (pCatalogOverview != null) {
            year = pCatalogOverview.getYear();
            catalog = pCatalogOverview.getCatalog();
        }
        return getCatalogDb(catalog, year, pAutoCreateConnection);
    }

    public Connection testCatalogDb() throws SQLException {
        return testConnection(DATABASE.cpx_catalog.name());
    }

    public enum DATABASE {
        cpx_language, cpx_catalog
    }

}
