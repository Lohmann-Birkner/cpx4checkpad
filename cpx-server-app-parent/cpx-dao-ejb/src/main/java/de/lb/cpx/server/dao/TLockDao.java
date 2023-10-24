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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.dao;

import de.lb.cpx.model.TLock;
import de.lb.cpx.model.enums.LockingCause;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.auth.CpxUser;
import de.lb.cpx.shared.dto.LockDTO;
import de.lb.cpx.shared.dto.LockException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * TCaseDetailsDao, grands single access to TCaseDetails Entities for update and
 * saving
 *
 * @author wilde
 */
@Stateless
@TransactionAttribute(REQUIRES_NEW)
@SuppressWarnings("unchecked")
public class TLockDao extends AbstractCpxDao<TLock> {
//    
//    @EJB
//    TCaseDao caseDao;

    public static final int LOCKING_TIME = 180; // 180 Minuten
    private static final Logger LOG = Logger.getLogger(TLockDao.class.getName());

    /**
     * Creates a new instance.
     */
    public TLockDao() {
        super(TLock.class);
    }

    public int removeExpiredLocks() {
        String sql = "DELETE FROM T_LOCK WHERE EXPIRES < ?";
        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter(1, new Date());
        int removedLocks = query.executeUpdate();
        if (removedLocks > 0) {
            LOG.log(Level.INFO, removedLocks + " expired locks were removed");
        }
        return removedLocks;
    }

    public LockDTO[] getDatabaseLock() {
        final long caseId = -1;
        final long processId = -1;
        return getLock(getLockQuery(caseId, processId));
    }

    public LockDTO[] getCaseLock(final Long pCaseId) {
        return getLock(getLockQuery(pCaseId, null));
    }

    public LockDTO[] getProcessLock(final Long pProcessId) {
        return getLock(getLockQuery(null, pProcessId));
    }

    public LockDTO[] getUserLock(final Long pUserId) {
        return getLock(getUserLockQuery(pUserId));
    }

    private List<Object[]> getLockQuery(final Long pCaseId, final Long pProcessId) {
        if ((pCaseId == null || pCaseId.equals(0L))
                && (pProcessId == null || pProcessId.equals(0L))) {
            LOG.log(Level.FINE, "Cannot reads locks from database because it was neither case id nor process id specified");
            return null;
        }
        removeExpiredLocks();

        final boolean dbLock = isDatabaseLock(pCaseId, pProcessId);
        final boolean csLock = isCaseLock(pCaseId, pProcessId);
        final boolean pcLock = isProcessLock(pCaseId, pProcessId);

        String baseQuery = "SELECT SINCE, EXPIRES, USER_ID, USER_NAME, CAUSE, CLIENT_ID, CASE_ID, "
                + " T_CASE.CS_HOSPITAL_IDENT, T_CASE.CS_CASE_NUMBER, PROCESS_ID, T_WM_PROCESS.WORKFLOW_NUMBER "
                + " FROM T_LOCK "
                + " LEFT JOIN T_CASE ON T_LOCK.CASE_ID = T_CASE.ID "
                + " LEFT JOIN T_WM_PROCESS ON T_LOCK.PROCESS_ID = T_WM_PROCESS.ID ";
        StringBuilder sql = new StringBuilder(baseQuery);
        if (dbLock) {
            //no where condition, because every locked process or lock case raises an lock exception if try to acquire database lock
        }
        if (csLock) {
            sql.append(" WHERE (CASE_ID = -1 AND PROCESS_ID = -1) OR (CASE_ID = ? AND PROCESS_ID = -1) ");// here i would add check of the client id so that no exception will be thrown when it is my lock
        }
        if (pcLock) {
            sql.append(" WHERE (CASE_ID = -1 AND PROCESS_ID = -1) OR (CASE_ID = -1 AND PROCESS_ID = ?) ");
        }
        //sql.append(" ORDER BY SINCE ");
        // OR CASE_ID = -1 ORDER BY CASE_ID DESC
        int paramIdx = 1;
        Query query = getEntityManager().createNativeQuery(sql.toString());
        if (csLock) {
            query.setParameter(paramIdx++, pCaseId);
        }
        if (pcLock) {
            query.setParameter(paramIdx++, pProcessId);
        }
        List<Object[]> results = query.getResultList();
        return results;
    }

    protected List<Object[]> getUserLockQuery(final Long pUserId) {
        if (pUserId == null || pUserId.equals(0L)) {
            return null;
        }
        removeExpiredLocks();
        String sql = String.format("SELECT SINCE, EXPIRES, USER_ID, USER_NAME, CAUSE, CLIENT_ID, CASE_ID, "
                + " T_CASE.CS_HOSPITAL_IDENT, T_CASE.CS_CASE_NUMBER, PROCESS_ID, T_WM_PROCESS.WORKFLOW_NUMBER "
                + " FROM T_LOCK "
                + " LEFT JOIN T_CASE ON T_LOCK.CASE_ID = T_CASE.ID "
                + " LEFT JOIN T_WM_PROCESS ON T_LOCK.PROCESS_ID = T_WM_PROCESS.ID "
                + "%s", (pUserId > -1 ? " WHERE USER_ID = ? " : "")); // OR CASE_ID = -1 ORDER BY CASE_ID DESC
        Query query = getEntityManager().createNativeQuery(sql);
        if (pUserId > -1) {
            query.setParameter(1, pUserId);
        }
        List<Object[]> results = query.getResultList();
        return results;
    }

    protected LockDTO[] getLock(final List<Object[]> pResults) {
        if (pResults == null || pResults.isEmpty()) {
            return new LockDTO[0];
        }
        String actualDatabase = getActualDatabase();
        Iterator<Object[]> it = pResults.iterator();
        LockDTO[] dbLockTimeArray = new LockDTO[pResults.size()];
        int i = 0;
        while (it.hasNext()) {
            //Remove all databases that don't have T_CASE and T_CASE_DETAILS table
            Object[] obj = it.next();

            Date since = (Date) obj[0];
            Date expires = (Date) obj[1];
            Number userId = (Number) obj[2];
            String userName = (String) obj[3];
            String cause = (String) obj[4];
            String clientId = (String) obj[5];
            Number csId = (Number) obj[6];
            String hosIdent = (String) obj[7];
            String caseNumber = (String) obj[8];
            Number processId = (Number) obj[9];
            Number workflowNumber = (Number) obj[10];

            LockDTO lockDto = new LockDTO(actualDatabase, /* caseId, */ since, expires,
                    userId == null ? null : userId.longValue(), userName, cause, clientId,
                    csId == null ? null : csId.longValue(), hosIdent, caseNumber,
                    processId == null ? null : processId.longValue(),
                    workflowNumber == null ? null : workflowNumber.longValue());
            dbLockTimeArray[i] = lockDto;
            i++;
        }
        return dbLockTimeArray;
    }

    public boolean isDatabaseLocked() {
        LockDTO[] dbLockTimeArray = getDatabaseLock();
        return (dbLockTimeArray.length > 0);
    }

    public boolean isCaseLocked(final Long pCaseId) {
        LockDTO[] dbLockTimeArray = getCaseLock(pCaseId);
        return (dbLockTimeArray.length > 0);
    }

    public boolean isProcessLocked(final Long pProcessId) {
        LockDTO[] dbLockTimeArray = getProcessLock(pProcessId);
        return (dbLockTimeArray.length > 0);
    }

    public boolean unlockDatabase() {
        return unlockDatabase(true);
    }

    public boolean unlockProcess(final Long pProcessId) {
        return unlockProcess(pProcessId, false);
    }

    public boolean unlockCase(final Long pCaseId) {
        return unlockCase(pCaseId, false);
    }

    public int unlockProcess(final long[] pProcessIds) {
        return unlockProcess(pProcessIds, false);
    }

    public int unlockCase(final long[] pCaseIds) {
        return unlockCase(pCaseIds, false);
    }

    /**
     * unlocks multiple processes
     *
     * @param pProcessIds list of process ids
     * @param pForce force to unlock even if the user has not open it
     * @return unlocked processes
     */
    public int unlockProcess(final long[] pProcessIds, final boolean pForce) {
        if (pProcessIds == null || pProcessIds.length == 0) {
            return 0;
        }
        int numberOfOnlockedProcesses = 0;
        int size = 1000; //oracle is limited to 1000 entries in IN-Clause, so I have to create chunks
        int lower = 0;
        while (true) {
            int upper = lower + size < pProcessIds.length ? lower + size : pProcessIds.length;
            long[] subIds = Arrays.copyOfRange(pProcessIds, lower, upper);

            String clientId = pForce ? "" : ClientManager.getCurrentClientId();

            String sql = String.format("DELETE FROM T_LOCK WHERE PROCESS_ID IN (%s) AND CASE_ID = -1",
                    StringUtils.join(ArrayUtils.toObject(subIds), ","));
            if (!clientId.isEmpty()) {
                sql += " AND CLIENT_ID = ?";
            }
            Query query = getEntityManager().createNativeQuery(sql);
            //query.setParameter(1, StringUtils.join(ArrayUtils.toObject(subIds), ","));
            if (!clientId.isEmpty()) {
                query.setParameter(1, clientId);
            }
            numberOfOnlockedProcesses += query.executeUpdate();

            if (upper == pProcessIds.length) {
                break;
            }

            lower = upper;
        }

        return numberOfOnlockedProcesses;
    }

    /**
     * unlocks multiple cases
     *
     * @param pCaseIds list of case ids
     * @param pForce force to unlock even if the user has not open it
     * @return unlocked cases
     */
    public int unlockCase(final long[] pCaseIds, final boolean pForce) {
        if (pCaseIds == null || pCaseIds.length == 0) {
            return 0;
        }
        int numberOfOnlockedCases = 0;
        int size = 1000; //oracle is limited to 1000 entries in IN-Clause, so I have to create chunks
        int lower = 0;
        while (true) {
            int upper = lower + size < pCaseIds.length ? lower + size : pCaseIds.length;
            long[] subIds = Arrays.copyOfRange(pCaseIds, lower, upper);

            String clientId = pForce ? "" : ClientManager.getCurrentClientId();

            String sql = String.format("DELETE FROM T_LOCK WHERE CASE_ID IN (%s) AND PROCESS_ID = -1",
                    StringUtils.join(ArrayUtils.toObject(subIds), ","));
            if (!clientId.isEmpty()) {
                sql += " AND CLIENT_ID = ?";
            }
            Query query = getEntityManager().createNativeQuery(sql);
            //query.setParameter(1, StringUtils.join(ArrayUtils.toObject(subIds), ","));
            if (!clientId.isEmpty()) {
                query.setParameter(1, clientId);
            }
            numberOfOnlockedCases += query.executeUpdate();

            if (upper == pCaseIds.length) {
                break;
            }

            lower = upper;
        }

        return numberOfOnlockedCases;
    }

    /**
     * A forced unlock removes all locks on this case. It does not regard if the
     * client who unlocks the case is the same client that acquired the lock!
     * That means: An unforced unlock only unlocks cases that were acquired by
     * this client!
     *
     * @param pCaseId case id to unlock
     * @param pForce force to unlock even if the user has not open it
     * @return indicator if unlock succeded
     */
    public boolean unlockCase(final long pCaseId, final boolean pForce) {
        return unlockCase(new long[]{pCaseId}, pForce) > 0;
    }

    /**
     * A forced unlock removes all locks on this process. It does not regard if
     * the client who unlocks the case is the same client that acquired the
     * lock! That means: An unforced unlock only unlocks cases that were
     * acquired by this client!
     *
     * @param pProcessId case id to unlock
     * @param pForce force to unlock even if the user has not open it
     * @return indicator if unlock succeded
     */
    public boolean unlockProcess(final long pProcessId, final boolean pForce) {
        return unlockProcess(new long[]{pProcessId}, pForce) > 0;
    }

    /**
     * A forced unlock removes lock on this database. It does not regard if the
     * client who unlocks the case is the same client that acquired the lock!
     * That means: An unforced unlock only unlocks cases that were acquired by
     * this client!
     *
     * @param pForce force to unlock even if the user has not open it
     * @return indicator if unlock succeded
     */
    public boolean unlockDatabase(final boolean pForce) {
        String clientId = pForce ? "" : ClientManager.getCurrentClientId();

        String sql = "DELETE FROM T_LOCK WHERE CASE_ID = -1 AND PROCESS_ID = -1";
        if (!clientId.isEmpty()) {
            sql += " AND CLIENT_ID = ?";
        }
        Query query = getEntityManager().createNativeQuery(sql);
        if (!clientId.isEmpty()) {
            query.setParameter(1, clientId);
        }
        return (query.executeUpdate() > 0);
    }

    public synchronized void lockCase(final Long pCaseId) throws LockException {
        final Long processId = null;
        createLock(pCaseId, processId, null);
    }

    public synchronized void lockCase(final Long pCaseId, final String pCause) throws LockException {
        final Long processId = null;
        createLock(pCaseId, processId, pCause);
    }

    public synchronized void lockProcess(final Long pProcessId) throws LockException {
        final Long caseId = null;
        createLock(caseId, pProcessId, null);
    }

    public synchronized void lockDatabase(final String pCause) throws LockException {
        final long caseId = -1;
        final long processId = -1;
        createLock(caseId, processId, pCause);
    }

    public void checkDatabaseLock() throws LockException {
        //List<Object[]> dbLockList = LOCKED_CASES.get(caseId);
        LockDTO[] dbLockTimeArray = getDatabaseLock();

        if (dbLockTimeArray.length > 0) {
            throw LockException.createDbLockException(dbLockTimeArray);
        }
    }

    public void checkCaseLock(final Long caseId) throws LockException {
        LockDTO[] dbLockTimeArray = getCaseLock(caseId);

        if (dbLockTimeArray.length > 0) {
            throw LockException.createCaseLockException(caseId, dbLockTimeArray);
        }
    }

    public void checkProcessLock(final Long processId) throws LockException {
        //List<Object[]> dbLockList = LOCKED_CASES.get(caseId);
        LockDTO[] dbLockTimeArray = getProcessLock(processId);

        if (dbLockTimeArray.length > 0) {
            throw LockException.createProcessLockException(processId, dbLockTimeArray);
        }
    }

    private static boolean isDatabaseLock(final Long pCaseId, final Long pProcessId) {
        return (pCaseId != null && pCaseId.equals(-1L)) && (pProcessId != null && pProcessId.equals(-1L));
    }

    private static boolean isProcessLock(final Long pCaseId, final Long pProcessId) {
        return !isDatabaseLock(pCaseId, pProcessId) && (pProcessId != null && pProcessId > 0L);
    }

    private static boolean isCaseLock(final Long pCaseId, final Long pProcessId) {
        return !isDatabaseLock(pCaseId, pProcessId) && (pCaseId != null && pCaseId > 0L);
    }

    private void checkLock(final Long pCaseId, final Long pProcessId) throws LockException {
        final boolean dbLock = isDatabaseLock(pCaseId, pProcessId);
        if (dbLock) {
            checkDatabaseLock();
        }
        final boolean csLock = isCaseLock(pCaseId, pProcessId);
        if (csLock) {
            checkCaseLock(pCaseId);
        }
        final boolean pcLock = isProcessLock(pCaseId, pProcessId);
        if (pcLock) {
            checkProcessLock(pProcessId);
        }
    }

    protected synchronized void createLock(final Long pCaseId, final Long pProcessId, final String pCause) throws LockException {
        if ((pCaseId == null || pCaseId.equals(0L))
                && (pProcessId == null || pProcessId.equals(0L))) {
            LOG.log(Level.FINE, "Cannot create lock because it was neither case id nor process id specified");
            return;
        }
        String cause = (pCause == null) ? "" : pCause.trim();

        final boolean dbLock = isDatabaseLock(pCaseId, pProcessId);
        final boolean csLock = isCaseLock(pCaseId, pProcessId);
        final boolean pcLock = isProcessLock(pCaseId, pProcessId);
        checkLock(pCaseId, pProcessId);

        CpxUser cdbUser = ClientManager.getCurrentCpxUser();

        Long userId = ClientManager.getCurrentCpxUserId();
        String clientId = ClientManager.getCurrentClientId();
        String userName = (cdbUser == null) ? "" : cdbUser.getUserName();
        Date since = new Date();

        Date expires = null;
        if (csLock || pcLock) {
            Calendar expiresCal = Calendar.getInstance();
            expiresCal.setTime(since);
            expiresCal.add(Calendar.MINUTE, LOCKING_TIME);
            expires = expiresCal.getTime();
        }

        //boolean isSqlSrv = lockDao.isSqlSrv();
        String baseQuery = "INSERT INTO T_LOCK (ID, CASE_ID, SINCE, EXPIRES, USER_ID, USER_NAME, CAUSE, CLIENT_ID, PROCESS_ID) "
                + " SELECT " + (isSqlSrv() ? "ISNULL" : "NVL") + "((SELECT MAX(ID) FROM T_LOCK), 0) + 1, ?, ?, ?, ?, ?, ?, ?, ? "
                + (isOracle() ? " FROM DUAL " : "");

        StringBuilder sql = new StringBuilder(baseQuery);
        sql.append(" WHERE NOT EXISTS (SELECT 1 FROM T_LOCK WHERE CASE_ID = -1 AND PROCESS_ID = -1) ");
        if (csLock) {
            sql.append(" AND NOT EXISTS (SELECT 1 FROM T_LOCK WHERE CASE_ID = ? AND PROCESS_ID = -1) ");
        }
        if (pcLock) {
            sql.append(" AND NOT EXISTS (SELECT 1 FROM T_LOCK WHERE PROCESS_ID = ? AND CASE_ID = -1) ");
        }
        Query query = getEntityManager().createNativeQuery(sql.toString());
        query.setParameter(1, pCaseId == null ? -1L : pCaseId);
        query.setParameter(2, since, TemporalType.TIMESTAMP);
        query.setParameter(3, expires, TemporalType.TIMESTAMP);
        query.setParameter(4, userId == null ? -1L : userId);
        query.setParameter(5, userName);
        query.setParameter(6, cause);
        query.setParameter(7, clientId);
        query.setParameter(8, pProcessId == null ? -1L : pProcessId);
        if (csLock) {
            query.setParameter(9, pCaseId);
        }
        if (pcLock) {
            query.setParameter(9, pProcessId);
        }

        if (query.executeUpdate() <= 0) {
            //throw createLockException(caseId, "Lock was requested by another instance meantime");
            checkLock(pCaseId, pProcessId); //should fire LockException!
            //but if no LockException is fired (lock was released very quickly!), then use this helper exception... (other strategy could be to retry insert)
            if (dbLock) {
                throw LockException.createDbLockException("Lock on database was requested by another oser or client instance meantime");
            }
            if (csLock) {
                throw LockException.createCaseLockException(pCaseId, "Lock on case (or whole database) requested by another user or client instance meantime");
            }
            if (pcLock) {
                throw LockException.createProcessLockException(pProcessId, "Lock on process (or whole database) requested by another oser or client instance meantime");
            }
        }
    }

    public int removeAllCaseLocks() {
        String sql = "DELETE FROM T_LOCK WHERE CASE_ID <> -1";
        Query query = getEntityManager().createNativeQuery(sql);
        return query.executeUpdate();
    }

    public int removeAllProcessLocks() {
        String sql = "DELETE FROM T_LOCK WHERE PROCESS_ID <> -1";
        Query query = getEntityManager().createNativeQuery(sql);
        return query.executeUpdate();
    }

    public synchronized int removeAllLocks() {
        String sql = "TRUNCATE TABLE T_LOCK";
        Query query = getEntityManager().createNativeQuery(sql);
        return query.executeUpdate();
    }

    protected String getActualDatabase() {
        return ClientManager.getActualDatabase();
    }

//    /**
//     * @return all locks for cases, not db locks if there are any
//     */
//    public LockDTO[] getAllLocksForCases() {
//        final long caseId = -1L;
//        return getCaseLock(caseId);
////        String sql = "SELECT * FROM T_LOCK WHERE CASE_ID NOT LIKE -1 "; // OR CASE_ID = -1 ORDER BY CASE_ID DESC
////        Query query = getEntityManager().createNativeQuery(sql,TLock.class);  
////        return query.getResultList();
//    }
    /**
     * needed for logout? all resources have to be unlocked?
     *
     * @param pUserId user id of the current user
     * @return if anything was removed
     */
    public int removeAllForUser(Long pUserId) {
        if (ClientManager.getCurrentClientId() == null) {
            LOG.log(Level.FINE, "Cannot remove user locks for logout, because Client-ID of current user is unknown");
            return 0;
        }
        String sql = "DELETE FROM T_LOCK WHERE USER_ID = ? AND CLIENT_ID = ?";
        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter(1, pUserId);
        query.setParameter(2, ClientManager.getCurrentClientId());
        return query.executeUpdate();
    }


}
