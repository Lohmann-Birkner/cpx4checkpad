/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.information;

import de.lb.cpx.server.commons.enums.DbDriverEn;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class DatabaseInfo implements Serializable {

    private static final Logger LOG = Logger.getLogger(DatabaseInfo.class.getName());

    private static final long serialVersionUID = 1L;

//    public final boolean isOracle;
//    public final boolean isSqlsrv;
    public final DbDriverEn driver;
    public final String version;
    public final String connectionVendor;
    public final String connectionUrl;
    public final ConnectionString connectionString;
    public final String hostName;
    public final String port;

    public DatabaseInfo(
            final String pConnectionString,
            final String pConnectionUrl,
            final String pConnectionVendor,
            //final boolean isOracle, final boolean isSqlsrv,
            //final DbDriverEn pDriverEn,
            final String version) {
        this.connectionString = new ConnectionString(pConnectionString);
        this.connectionUrl = pConnectionUrl == null ? "" : pConnectionUrl.trim();
        this.driver = DbDriverEn.findByConnectionUrl(pConnectionUrl);
        String v = pConnectionVendor == null ? "" : pConnectionVendor.trim();
        if (v.isEmpty() && driver != null) {
            v = driver.getVendor();
        }
        this.connectionVendor = v;
//        this.isOracle = isOracle;
//        this.isSqlsrv = isSqlsrv;
        this.version = version == null ? "" : version.trim();
        this.hostName = extractHostName(connectionUrl);
        Integer portInt = extractPort(connectionUrl);
        String p = portInt == null ? null : portInt.toString();
        if (p == null && driver != null) {
            p = driver.getDefaultPort();
        }
        this.port = p;
    }

    public String getVersionShort() {
        if (isSqlsrv()) {
            int p = version.indexOf('(');
            if (p > -1) {
                return version.substring(0, p).trim();
            }
        }
        if (isOracle()) {
            int p = version.indexOf("Release");
            if (p > -1) {
                return version.substring(0, p).trim();
            }
        }
        return "";
    }

    public boolean isOracle() {
        return driver == null ? false : driver.isOracle();
    }

    public boolean isSqlsrv() {
        return driver == null ? false : driver.isSqlsrv();
    }

    public DbDriverEn getDriver() {
        return driver;
    }

    public String getDriverName() {
        return driver == null ? "" : driver.getName();
    }

    public String getDriverVendor() {
        return driver == null ? "" : driver.getVendor();
    }

    public String getDriverJdbc() {
        return driver == null ? "" : driver.getJdbcDriver();
    }

    public String getDriverDefaultPort() {
        return driver == null ? "" : driver.getDefaultPort();
    }

    public String getDriverNullFunction() {
        return driver == null ? "" : driver.getNullFunction();
    }

    public String toDate(final String pDate) {
        return driver == null ? "" : driver.toDate(pDate);
    }

    public String getVersion() {
        return version;
    }

    public String getConnectionVendor() {
        return connectionVendor;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public ConnectionString getConnectionString() {
        return connectionString;
    }

    public String getDatabase() {
        return connectionString.getDatabase();
    }

    public String getPeristenceUnit() {
        return connectionString.getPersistenceUnit();
    }

    public String getHostName() {
        return hostName;
    }

    public String getPort() {
        return port;
    }

    public static String extractHostName(final String pConnectionUrl) {
        if (pConnectionUrl.isEmpty()) {
            return "";
        }
        String hostname = "";
//        if (pConnectionUrl != null && !pConnectionUrl.trim().isEmpty()) {
        String tmp = pConnectionUrl.trim();
        tmp = tmp.replace("jdbc:oracle:thin:@", "");
        tmp = tmp.replace("jdbc:sqlserver://", "");
        int pos = tmp.indexOf(':');
        if (pos > -1) {
            tmp = tmp.substring(0, tmp.indexOf(':'));
        }
        hostname = tmp.trim();
//        }
        return hostname;
    }

    public static Integer extractPort(final String pConnectionUrl) {
        if (pConnectionUrl.isEmpty()) {
            return null;
        }
        final String hostName = extractHostName(pConnectionUrl);
        int pos = pConnectionUrl.indexOf(hostName);
        if (pos > -1) {
            StringBuilder sb = new StringBuilder();
            for (int i = pos + hostName.length() + 1; i < pConnectionUrl.length(); i++) {
                char ch = pConnectionUrl.charAt(i);
                if (ch == ':' || ch == ' ') {
                    continue;
                }
                if (ch >= '0' && ch <= '9') {
                    sb.append(ch);
                } else {
                    break;
                }
                if (sb.length() >= 5) {
                    break;
                }
            }
            if (sb.length() == 0) {
                return null;
            } else {
                final String val = sb.toString();
                try {
                    return Integer.parseInt(val);
                } catch (NumberFormatException ex) {
                    LOG.log(Level.SEVERE, "Cannot parse string as port number (int): " + val, ex);
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    public String getIdentifier() {
        if (driver == null) {
            return "";
        }
        return driver.getIdentifier(hostName, port, getDatabase());
    }

//    public static void main(String args[]) {
//        final DatabaseInfo dbInfo = new DatabaseInfo("dbsys1:DIRK", "jdbc:sqlserver://lbbatch-01:1434", "Oracle", "Test");
//        System.out.println(dbInfo.getPort());
//    }
}
