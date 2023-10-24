/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commons.enums;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public enum DbDriverEn implements Serializable {
    ORACLE("Oracle", "Oracle", "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@{host}:{port}:{database_sid}", "1522", "NVL"),
    SQLSRV("Microsoft", "SQL Server", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://{host}:{port};DATABASE={database_sid};sendStringParametersAsUnicode=false", "1433", "ISNULL"),
    INGRES("Actian", "Ingres", "com.ingres.jdbc.IngresDriver", "jdbc:ingres://{host}:{port}/{database_sid}", "II7", "IFNULL");
//    UNKNOWN("Unknown", "Unknown", "");

    private static final Logger LOG = Logger.getLogger(DbDriverEn.class.getName());

    private final String vendor;
    private final String name;
    private final String jdbcDriver;
    private final String url;
    private final String defaultPort;
    private final String nullFunction;

    DbDriverEn(final String pVendor, final String pName, final String pJdbcDriver, final String pUrl, final String pDefaultPort, final String pNullFunction) {
        vendor = pVendor;
        name = pName;
        jdbcDriver = pJdbcDriver;
        url = pUrl;
        defaultPort = pDefaultPort;
        nullFunction = pNullFunction;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public String getVendor() {
        return vendor;
    }

    public String getName() {
        return name;
    }

    public String getNullFunction() {
        return nullFunction;
    }

    public static DbDriverEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        for (DbDriverEn val : values()) {
            if (val != null && id.equalsIgnoreCase(val.name())) {
                return val;
            }
        }
        LOG.log(Level.WARNING, "Cannot find DbDriver, because this id is unknown: " + id);
        return null;
    }

    public static DbDriverEn findByConnectionUrl(final String pConnectionUrl) {
        final String connUrl = (pConnectionUrl == null) ? "" : pConnectionUrl.toLowerCase().trim();
        if (connUrl.isEmpty()) {
            LOG.log(Level.WARNING, "Cannot detect database driver if connection url is null or empty");
            return null;
        }
        if (isSqlSrv(connUrl)) {
            return DbDriverEn.SQLSRV;
        }
        if (isOracle(connUrl)) {
            return DbDriverEn.ORACLE;
        }
        if (isIngres(connUrl)) {
            return DbDriverEn.INGRES;
        }
        LOG.log(Level.WARNING, "Cannot detect database driver from connection url: " + pConnectionUrl);
        return null;
    }

    public static boolean isIngres(final String pConnectionUrl) {
        String connUrl = (pConnectionUrl == null) ? "" : pConnectionUrl.toLowerCase().trim();
        return connUrl.contains(":ingres:");
    }

    public static boolean isOracle(final String pConnectionUrl) {
        String connUrl = (pConnectionUrl == null) ? "" : pConnectionUrl.toLowerCase().trim();
        return connUrl.contains(":oracle:");
    }

    public static boolean isSqlSrv(final String pConnectionUrl) {
        String connUrl = (pConnectionUrl == null) ? "" : pConnectionUrl.toLowerCase().trim();
        return connUrl.contains(":sqlserver:") || connUrl.contains(":sqlsrv:");
    }

    public boolean isIngres() {
        return INGRES.equals(this);
    }

    public boolean isOracle() {
        return ORACLE.equals(this);
    }

    public boolean isSqlsrv() {
        return SQLSRV.equals(this);
    }

    public String getDefaultPort() {
        return defaultPort;
    }

    public String getUrl(final String pHost, final String pDatabaseOrSid, final String pUser, final String pPassword) {
        return getUrl(pHost, defaultPort, pDatabaseOrSid, pUser, pPassword);
    }

    public String getUrl(final String pHost, final String pPort, final String pDatabaseOrSid, final String pUser, final String pPassword) {
        String fullUrl = url;
        fullUrl = fullUrl.replace("{host}", pHost == null ? "" : pHost.trim());
        fullUrl = fullUrl.replace("{port}", String.valueOf(getPort(pPort)));
        fullUrl = fullUrl.replace("{database_sid}", pDatabaseOrSid == null ? "" : pDatabaseOrSid.trim());
        fullUrl = fullUrl.replace("{user}", pUser == null ? "" : pUser.trim());
        fullUrl = fullUrl.replace("{password}", pPassword == null ? "" : pPassword.trim());
        return fullUrl;
    }

    public Connection getConnection(final String pHost, final String pDatabaseOrSid, final String pUser, final String pPassword) throws SQLException {
        return getConnection(pHost, defaultPort, pDatabaseOrSid, pUser, pPassword);
    }

    public Connection getConnection(final String pHost, final String pPort, final String pDatabaseOrSid, final String pUser, final String pPassword) throws SQLException {
        final String fullUrl = getUrl(pHost, pPort, pDatabaseOrSid, pUser, pPassword);
        return DriverManager.getConnection(fullUrl, pUser, pPassword);
    }

    private String getPort(final String pPort) {
        if (pPort == null || pPort.isEmpty() || pPort.equals("0")) {
            return defaultPort;
        }
        return pPort;
    }

//    private String getDatabase(final String pDatabaseOrSid, final String pUser) {
//        if (isSqlsrv()) {
//            return pDatabaseOrSid;
//        } else {
//            //Oracle
//            return pUser;
//        }
//    }
    public String getIdentifier(final String pHost, final String pPort, final String pDatabase /*, final String pDatabaseOrSid, final String pUser */) {
        String ident = "{host}:{port}/{database}";
        ident = ident.replace("{host}", pHost == null ? "" : pHost.trim());
        ident = ident.replace("{port}", String.valueOf(getPort(pPort)));
        //ident = ident.replace("{database}", getDatabase(pDatabaseOrSid, pUser));
        ident = ident.replace("{database}", pDatabase == null ? "" : pDatabase.trim());
        return ident;
    }

    public String toStaticDate(final Date pDate) {
        String date = dateToString(pDate);
        return toDate(date);
    }

    public String toDate(final String pDate) {
        if (pDate == null || pDate.trim().isEmpty()) {
            return "";
        }
        if (isSqlsrv()) {
            return "'" + pDate.trim().replace("-", "") + "'";
        }
        return "TO_DATE('" + pDate + "', 'YYYY-MM-DD')";
    }

    public static String dateToString(final Date pDate) {
        if (pDate == null) {
            return "";
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(pDate);
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf((cal.get(Calendar.MONTH) + 1));
        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }

        String dateStr = year + "-" + month + "-" + day;
        return dateStr;
    }

}
