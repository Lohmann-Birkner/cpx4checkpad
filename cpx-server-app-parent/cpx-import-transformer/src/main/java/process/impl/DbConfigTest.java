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
 *    2017  niemeier - initial API and implementation and/or initial documentation
 */
package process.impl;

import de.lb.cpx.server.commons.enums.DbDriverEn;
import static de.lb.cpx.str.utils.StrUtils.toStr;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Class for database test configuration
 *
 * @author niemeier
 */
public class DbConfigTest {

    private static final Logger LOG = Logger.getLogger(DbConfigTest.class.getName());

    public final DbDriverEn mDriver;
    public final String mHost;
    public final String mPort;
    public final String mDatabaseOrSid;
    public final String mUser;
    public final String mPassword;

    /**
     * Creates a database test configuration for imports
     *
     * @param pDriver driver
     * @param pHost host
     * @param pDatabaseOrSid database or SID (Oracle)
     * @param pUser user
     * @param pPassword password
     * @throws IllegalArgumentException error if parameter is empty or invalid
     */
    public DbConfigTest(final DbDriverEn pDriver, final String pHost, final String pDatabaseOrSid, final String pUser, final String pPassword) throws IllegalArgumentException {
        this(pDriver, pHost, null, pDatabaseOrSid, pUser, pPassword);
    }

    /**
     * Creates a database test configuration for imports
     *
     * @param pDriver driver
     * @param pHost host
     * @param pPort port
     * @param pDatabaseOrSid database or SID (Oracle)
     * @param pUser user
     * @param pPassword password
     * @throws IllegalArgumentException error if parameter is empty or invalid
     */
    public DbConfigTest(final DbDriverEn pDriver, final String pHost, final String pPort, final String pDatabaseOrSid, final String pUser, final String pPassword) throws IllegalArgumentException {
        mDriver = pDriver;
        mHost = toStr(pHost);
        mPort = pPort;
        mDatabaseOrSid = toStr(pDatabaseOrSid);
        mUser = toStr(pUser);
        mPassword = toStr(pPassword);

        if (mDriver == null) {
            throw new IllegalArgumentException("No database driver passed!");
        }
        if (mHost.isEmpty()) {
            throw new IllegalArgumentException("No database host passed!");
        }
        if (mDatabaseOrSid.isEmpty()) {
            throw new IllegalArgumentException("No database name or SID passed!");
        }
        if (mUser.isEmpty()) {
            throw new IllegalArgumentException("No database user passed!");
        }
        if (mPassword.isEmpty()) {
            throw new IllegalArgumentException("No database password passed!");
        }
    }

    /**
     * Host
     *
     * @return host
     */
    public String getHost() {
        return mHost;
    }

    /**
     * Port
     *
     * @return port
     */
    public String getPort() {
        return mPort;
    }

    /**
     * Database or SID (Oracle)
     *
     * @return database or SID (Oracle)
     */
    public String getDatabaseOrSid() {
        return mDatabaseOrSid;
    }

    /**
     * User
     *
     * @return user
     */
    public String getUser() {
        return mUser;
    }

    /**
     * Password
     *
     * @return password
     */
    public String getPassword() {
        return mPassword;
    }

    /**
     * creates jdbc connection
     *
     * @return jdbc connection
     * @throws SQLException connection failed
     */
    public Connection createConnection() throws SQLException {
        return mDriver.getConnection(mHost, mPort, mDatabaseOrSid, mUser, mPassword);
    }

    @Override
    public String toString() {
        //return "DbConfigTest{" + "mHost=" + mHost + ", mPort=" + mPort + ", mDatabaseOrSid=" + mDatabaseOrSid + ", mUser=" + mUser + ", mPassword=*******}";
        return getIdentifier();
    }

    public String getIdentifier() {
        return mDriver.getIdentifier(mHost, mPort, (mDriver.isOracle() ? mUser : mDatabaseOrSid));
    }

}
