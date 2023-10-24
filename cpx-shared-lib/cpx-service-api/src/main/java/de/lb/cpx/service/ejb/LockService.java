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
 *    2016  Husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.model.enums.LockingCause;
import de.lb.cpx.shared.dto.LockDTO;
import de.lb.cpx.shared.dto.LockException;
import javax.ejb.Remote;

/**
 *
 * @author Husser
 */
@Remote
public interface LockService {

    void lockCase(long pCaseId) throws LockException;

    /**
     * throws lock exception if case is locked (does not acquire lock!)
     *
     * @param pCaseId ID of hospital case
     * @throws LockException lock exception
     */
    void checkCaseLock(long pCaseId) throws LockException;

    boolean unlockCase(long pCaseId, final boolean pForce);

    boolean unlockCase(long pCaseId);

    int unlockCase(final long[] pCaseIds, final boolean pForce);

    int unlockCase(final long[] pCaseIds);

    boolean isCaseLocked(long pCaseId);

    LockDTO[] getCaseLock(long pCaseId);

    void lockProcess(long pProcessId) throws LockException;

    /**
     * throws lock exception if process is locked (does not acquire lock!)
     *
     * @param pProcessId ID of process
     * @throws LockException lock exception
     */
    void checkProcessLock(long pProcessId) throws LockException;

    boolean unlockProcess(long pProcessId, final boolean pForce);

    boolean unlockProcess(long pProcessId);

    int unlockProcess(final long[] pProcessIds, final boolean pForce);

    int unlockProcess(final long[] pProcessIds);

    boolean isProcessLocked(long pProcessId);

    LockDTO[] getProcessLock(long pProcessId);

    void lockDatabase(final LockingCause pCause) throws LockException;

    /**
     * throws lock exception if database is locked (does not acquire lock!)
     *
     * @throws LockException lock exception
     */
    void checkDatabaseLock() throws LockException;

    boolean unlockDatabase(final boolean pForce);

    boolean unlockDatabase();

    boolean isDatabaseLocked();

    LockDTO[] getDatabaseLock();

    int removeExpiredLocks();

    int removeAllForUser(Long pUserId);

    int removeAllLocks();

    int removeAllCaseLocks();

    int removeAllProcessLocks();

    /*
    int removeExpiredLocks();

    int removeAllLocks();
     */
    /**
     * find the list of currently locked TCaseEntities for a user
     *
     * @param cpxUserId current user id
     * @return list of locked cases
     */
//    List<TCase> findLockedCasesForCurrentUser(long cpxUserId);
    /**
     * find the list of currently locked TCaseEntities for the user currently
     * logged in the server
     *
     * @return list of locked cases
     */
//    List<TCase> findLockedCasesForCurrentUser();
    /**
     * find a list of all locked processes for a specific user
     *
     * @param pUser userid
     * @return list of locked cases
     */
//    List<TWmProcess> findLockedProcessesForUser(long pUser);
    /**
     * find a list of all locked processes for the currently locked in user
     *
     * @return list of locked cases
     */
//    List<TWmProcess> findLockedProcessesForCurrentUser();
//    LockDTO[] findLockedEntries();
//    Boolean isCaseDbLocked();
//
//    boolean removeAllForUser(Long pUserId);
}
