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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.model.enums.LockingCause;
import de.lb.cpx.server.dao.TLockDao;
import static de.lb.cpx.service.ejb.CpxAuthorizationChecker.*;
import de.lb.cpx.shared.dto.LockDTO;
import de.lb.cpx.shared.dto.LockException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.inject.Inject;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author Husser
 */
@Singleton
@SecurityDomain("cpx")
@LocalBean
public class LockServiceBean implements LockService {

    private static final Logger LOG = Logger.getLogger(LockServiceBean.class.getName());

    @Inject
    private TLockDao lockDao;
//    @EJB
//    private TCaseDao caseDao;
//    @EJB
//    private TWmProcessDao processDao;

    @Override
    public synchronized void lockCase(final long pCaseId) throws LockException {
        lockDao.lockCase(pCaseId);
    }

    @Override
    public void checkCaseLock(final long pCaseId) throws LockException {
        lockDao.checkCaseLock(pCaseId);
    }

    @Override
    public boolean unlockCase(final long pCaseId, final boolean pForce) {
        checkUnlockCase();
        return lockDao.unlockCase(pCaseId, pForce);
    }

    @Override
    public boolean unlockCase(final long pCaseId) {
        checkUnlockCase(pCaseId);
        return lockDao.unlockCase(pCaseId);
    }

    @Override
    public int unlockCase(final long[] pCaseIds, final boolean pForce) {
        checkUnlockCase();
        return lockDao.unlockCase(pCaseIds, pForce);
    }

    @Override
    public int unlockCase(final long[] pCaseIds) {
        checkUnlockCase();
        return lockDao.unlockCase(pCaseIds);
    }

    @Override
    public LockDTO[] getCaseLock(final long pCaseId) {
        return lockDao.getCaseLock(pCaseId);
    }

    @Override
    public boolean isCaseLocked(final long pCaseId) {
        return lockDao.isCaseLocked(pCaseId);
    }

    @Override
    public synchronized void lockProcess(final long pProcessId) throws LockException {
        lockDao.lockProcess(pProcessId);
    }

    @Override
    public void checkProcessLock(final long pProcessId) throws LockException {
        lockDao.checkProcessLock(pProcessId);
    }

    @Override
    public boolean unlockProcess(final long pProcessId, final boolean pForce) {
        checkUnlockProcess();
        return lockDao.unlockProcess(pProcessId, pForce);
    }

    @Override
    public boolean unlockProcess(final long pProcessId) {
        checkUnlockProcess(pProcessId);
        return lockDao.unlockProcess(pProcessId);
    }

    @Override
    public int unlockProcess(final long[] pProcessIds, final boolean pForce) {
        checkUnlockProcess();
        return lockDao.unlockProcess(pProcessIds, pForce);
    }

    @Override
    public int unlockProcess(final long[] pProcessIds) {
        checkUnlockProcess();
        return lockDao.unlockProcess(pProcessIds);
    }

    @Override
    public LockDTO[] getProcessLock(final long pProcessId) {
        return lockDao.getProcessLock(pProcessId);
    }

    @Override
    public boolean isProcessLocked(final long pProcessId) {
        return lockDao.isProcessLocked(pProcessId);
    }

    @Override
    public synchronized void lockDatabase(final LockingCause pCause) throws LockException {
        lockDao.lockDatabase((pCause == null) ? "" : pCause.name());
    }

    @Override
    public void checkDatabaseLock() throws LockException {
        lockDao.checkDatabaseLock();
    }

    @Override
    public boolean unlockDatabase(final boolean pForce) {
        return lockDao.unlockDatabase(pForce);
    }

    @Override
    public boolean unlockDatabase() {
        return lockDao.unlockDatabase();
    }

    @Override
    public LockDTO[] getDatabaseLock() {
        return lockDao.getDatabaseLock();
    }

    @Override
    public boolean isDatabaseLocked() {
        return lockDao.isDatabaseLocked();
    }

//    @Override
//    public Boolean isCaseDbLocked() {
//        return lockDao.isDbLocked();
//    }
//
    @Override
    public int removeAllForUser(Long pUserId) {
        if (pUserId == null) {
            return 0;
        }
        int c = lockDao.removeAllForUser(pUserId);
        LOG.log(Level.INFO, c + " locks were released for logout");
        return c;
    }

    @Override
    public int removeExpiredLocks() {
        int c = lockDao.removeExpiredLocks();
        LOG.log(Level.INFO, c + " expired locks were released");
        return c;
    }

    @Override
    public int removeAllLocks() {
        int c = lockDao.removeAllLocks();
        LOG.log(Level.INFO, c + " locks were removed");
        return c;
    }

    @Override
    public int removeAllCaseLocks() {
        int c = lockDao.removeAllCaseLocks();
        LOG.log(Level.INFO, c + " case locks were removed");
        return c;
    }

    @Override
    public int removeAllProcessLocks() {
        int c = lockDao.removeAllProcessLocks();
        LOG.log(Level.INFO, c + " process locks were removed");
        return c;
    }

}
