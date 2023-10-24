/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.helper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public abstract class RequestLoader {

    private static final Logger LOG = Logger.getLogger(RequestLoader.class.getName());
    
    protected AtomicBoolean stopSignal;
    protected AtomicBoolean requestLoaderStopped;
    protected Callback stoppedCb;
    
    public void executeStatement(final Connection pConn, final int pFetchSize, final String pQuery, DbCallback pCallback) throws SQLException {
        try ( Statement stmt = pConn.createStatement()) {
            stmt.setFetchSize(pFetchSize);
            try ( ResultSet rs = stmt.executeQuery(pQuery)) {
                pCallback.call(rs);
            }
        }
    }


    protected boolean checkStopped() {
        if (stopSignal.get()) {
            requestLoaderStopped.set(true);
            stoppedCb.execute();
            return true;
        }
        return false;
    }
    


    protected String nextSqVal(final boolean isOracle, final String pSequenceName) {
        if (isOracle) {
            return pSequenceName + ".nextval";
        } else {
            return "NEXT VALUE FOR " + pSequenceName;
        }
    }

    protected static boolean dropTable(final Statement pStmt, final String pTableName) {
        final String query = "DROP TABLE " + pTableName; //add PURGE keyword to free space in oracle
        try {
            pStmt.execute(query);
            return true;
        } catch (SQLException ex) {
            LOG.log(Level.FINEST, "Cannot drop table: " + query, ex);
            return false;
        }
    }

    protected static boolean dropSequence(final Statement pStmt, final String pSequenceName) {
        final String query = "DROP SEQUENCE " + pSequenceName;
        try {
            pStmt.execute(query);
            return true;
        } catch (SQLException ex) {
            LOG.log(Level.FINEST, "Cannot drop sequence: " + query, ex);
            return false;
        }
    }
}
