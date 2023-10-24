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

import de.lb.cpx.server.commons.dao.AbstractDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public abstract class AbstractPrepStorer  implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(AbstractPrepStorer.class.getName());
    
    
    public static final int CHUNK_INSERT_SIZE = 1000;
    public static final boolean STORE_GRPRESULTS = true;

    protected Connection connection;
    public final boolean isSqlsrv;
    public final boolean isOracle;   
    
    
    protected final AtomicInteger counterInsert = new AtomicInteger(0);
    protected final AtomicBoolean start = new AtomicBoolean(true);

    protected int waitingInserts;
    protected long executionId;
    
    public AbstractPrepStorer(){
        isSqlsrv = false;
        isOracle = false;
       
    }
    
    public AbstractPrepStorer(final Connection pConn) throws SQLException {
         connection = pConn;
        String connStr = AbstractDao.getConnectionUrl(pConn);
        isOracle = AbstractDao.isOracle(connStr);
        isSqlsrv = AbstractDao.isSqlSrv(connStr);
        initialize();
        open();
       
    }

    protected synchronized void open() throws SQLException {
//      if (start.get()) {
//        return;
//      }
        //destroy();
        createPStatements();
        start.set(false);
    }
    
    public abstract  void createPStatements() throws SQLException;

    @Override
    public void close() {
        destroy();
    }
    
    protected abstract void destroy();
    
    protected abstract void initialize() throws SQLException;
    
    

    public PreparedStatement createPstmt(final String pQuery) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(pQuery);
        return pstmt;
    }

    public synchronized void closePstmt(final PreparedStatement pStmt) {
        if (pStmt != null) {
            if (STORE_GRPRESULTS) {
                try (pStmt) {
                    pStmt.executeBatch();
                } catch (SQLException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
            try {
                if (!pStmt.isClosed()) {
                    pStmt.close();
                }
                LOG.log(Level.INFO, " closed? {0}", pStmt.isClosed());
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }
    
       protected long getId4Storage( final Set<Long> pIdStorage, String pSeqName, int pSeqSize)throws SQLException {
       if (pIdStorage.isEmpty()) {
            synchronized (this) {
                if (pIdStorage.isEmpty()) {
                    final String query = getNextIdQuery(pSeqName, pSeqSize);
                    addToSequenceStore(query, pIdStorage);
                }
            }
        }
        Long id = null;
        if (!pIdStorage.isEmpty()) {
            Iterator<Long> it = pIdStorage.iterator();
            id = it.next();
            it.remove();
        }

        return id;
        
    }



    public void addToSequenceStore(final String query, final Set<Long> pSequenceStore) throws SQLException {
        Long minVal = -1L;
        Long maxVal = -1L;
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    long id = rs.getLong("VAL");
                    if (id < minVal || minVal == -1) {
                        minVal = id;
                    }
                    if (id > maxVal || maxVal == -1) {
                        maxVal = id;
                    }
                    pSequenceStore.add(id);
                }
            }
            for (long i = minVal + 1; i < maxVal; i++) {
                pSequenceStore.add(i);
            }
        }
    }


    public boolean executeInsert(final int pMinimumWaitingInserts) throws SQLException {
        if (waitingInserts >= pMinimumWaitingInserts) {
            executeInsert();
            return true;
        } else {
            return false;
        }
    }

    public abstract void executeInsert() throws SQLException;



    public int getWaitingInserts() {
        return waitingInserts;
    }

    protected abstract void rebuildIndexes();
    
    
    protected void rebuildIndexes( final String[][] indexes){
         try (Statement stmt = connection.createStatement()) {
            for (String[] tmp : indexes) {
                final String table = tmp[0];
                final String index = tmp[1];
                final String query;
                if (isOracle) {
                    query = "ALTER INDEX " + index + " REBUILD /* ONLINE */ PARALLEL 5 NOLOGGING";
                } else {
                    query = "ALTER INDEX " + index + " ON " + table + " REORGANIZE";
                }
                LOG.log(Level.INFO, "rebuild index " + table + "." + index);
                try {
                    stmt.execute(query);
                } catch (SQLException ex) {
                    LOG.log(Level.WARNING, "cannot rebuild index: " + query + " (index is missing?)");
                    LOG.log(Level.FINEST, "sql statement failed: " + query, ex);
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "cannot create statement to rebuild indexes", ex);
        }
       
    }
    
    protected String nextSqVal(final String pSequenceName) {
        if (isOracle) {
            return pSequenceName + ".nextval";
        } else {
            return "NEXT VALUE FOR " + pSequenceName;
        }
    }
    
    protected abstract void dropTempTables() throws SQLException ;

    
    protected void dropTempTables(String[] dropTables)throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            for (String table : dropTables) {
                LOG.log(Level.INFO, "drop temporary table " + table);
                final String query = "DROP TABLE " + table;
                try {
                    stmt.execute(query);
                } catch (SQLException ex) {
                    LOG.log(Level.SEVERE, "cannot drop table: " + query + " (table is missing?)", ex);
                    //LOG.log(Level.FINEST, "sql statement failed: " + query, ex);
                }
            }
        }
        
    }
    protected String currentDate() {
        return (isOracle ? "SYSDATE" : "GETDATE()");
    }

    public String getNextIdQuery(final String pSequenceName, final int pSize) {
        final String query;
        if (isOracle) {
            query = "select " + nextSqVal(pSequenceName) + " VAL "
                    + " from dual "
                    + " connect by level <= " + pSize;
        } else {
            query = "select " + nextSqVal(pSequenceName) + " VAL "
                    + " FROM (SELECT TOP " + pSize + " ID FROM T_CASE) TMP ";
        }
        return query;
    }

    public long getNextId(final String pSequenceName) throws SQLException {
        final String query = getNextIdQuery(pSequenceName, 1);
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    long id = rs.getLong("VAL");
                    return id;
                }
            }
        }
        return 0L;
    }
}
