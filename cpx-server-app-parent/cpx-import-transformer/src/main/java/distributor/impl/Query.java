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
package distributor.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class Query {

    public static final Logger LOG = Logger.getLogger(Query.class.getName());
    private final Statement mStatement;
    private final boolean mLogQueries;

    public Query(final Statement pStatement) {
        this(pStatement, true);
    }

    public Query(final Statement pStatement, final boolean pLogQueries) {
        mStatement = pStatement;
        mLogQueries = pLogQueries;
    }

    public boolean getLogQueries() {
        return mLogQueries;
    }

    public int executeUpdate(final String pQuery) throws SQLException {
        logQuery(pQuery);
        return mStatement.executeUpdate(pQuery);
    }

    public ResultSet executeQuery(final String pQuery) throws SQLException {
        logQuery(pQuery);
        return mStatement.executeQuery(pQuery);
    }

    public boolean execute(final String pQuery) throws SQLException {
        logQuery(pQuery);
        return mStatement.execute(pQuery);
    }

    private void logQuery(final String pQuery) {
        if (!mLogQueries) {
            return;
        }
        LOG.log(Level.INFO, pQuery);
    }

    public Statement getStmt() {
        return mStatement;
    }

    public boolean closeStmt() {
        if (mStatement == null) {
            return true;
        }
        try {
            mStatement.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
