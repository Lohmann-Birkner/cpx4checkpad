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
package de.lb.cpx.shared.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author niemeier
 */
public class LockException extends Exception {

    private static final long serialVersionUID = 1L;

    private final LockDTO[] lockDtos;
    private final Long caseId;
    private final Long processId;

    private LockException(final Long pCaseId, final Long pProcessId, final LockDTO[] pLockDtos, final String pMessage) {
        super(pMessage);
        caseId = pCaseId;
        processId = pProcessId;
        lockDtos = (pLockDtos == null) ? new LockDTO[0] : pLockDtos;
    }

    private LockException() {
        super();
        lockDtos = new LockDTO[0];
        caseId = null;
        processId = null;
    }

//    private LockException(String message) {
//        super(message);
//        lockDtos = new LockDTO[0];
//        caseId = null;
//        processId = null;
//    }
//
//    private LockException(String message, Throwable cause) {
//        super(message, cause);
//        lockDtos = new LockDTO[0];
//        caseId = null;
//        processId = null;
//    }
//
//    private LockException(Throwable cause) {
//        super(cause);
//        lockDtos = new LockDTO[0];
//        caseId = null;
//        processId = null;
//    }
//    private LockException(final Long pCaseId, final String pMessage) {
//        this(pCaseId, null, pMessage);
//    }
//
//    private LockException(final Long pCaseId, final LockDTO[] pLockDtos) {
//        this(pCaseId, pLockDtos, createLockMessage(pLockDtos));
//    }
    public static LockException createDbLockException(final LockDTO[] pLockDtos) {
        return new LockException(null, null, pLockDtos, createLockMessage(pLockDtos));
    }

    public static LockException createCaseLockException(final Long pCaseId, final LockDTO[] pLockDtos) {
        return new LockException(pCaseId, null, pLockDtos, createLockMessage(pLockDtos));
    }

    public static LockException createProcessLockException(final Long pProcessId, final LockDTO[] pLockDtos) {
        return new LockException(null, pProcessId, pLockDtos, createLockMessage(pLockDtos));
    }

    public static LockException createDbLockException(final String pMessage) {
        return new LockException(null, null, null, pMessage);
    }

    public static LockException createCaseLockException(final Long pCaseId, final String pMessage) {
        return new LockException(pCaseId, null, null, pMessage);
    }

    public static LockException createProcessLockException(final Long pProcessId, final String pMessage) {
        return new LockException(null, pProcessId, null, pMessage);
    }

    private static String createLockMessage(final LockDTO[] pLockDtos) {
        final StringBuilder sb = new StringBuilder("Lock detected");
        if (pLockDtos != null && pLockDtos.length > 0) {
            sb.append(":");
            int i = 0;
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (LockDTO dto : pLockDtos) {
                i++;
                if (dto == null) {
                    continue;
                }
                sb.append("\r\n * ");
                if (i >= 10) {
                    //okay, that's enough, don't print all cases!
                    int left = pLockDtos.length - i;
                    if (left > 0) {
                        sb.append("[" + left + " more locks]");
                    }
                    break;
                }
                String since = (dto.getSince() == null ? "unknown" : df.format(dto.getSince()));
                if (dto.isCaseLock()) {
                    //Case Lock
                    sb.append("Hospital case " + dto.getCaseKey() + " (ID " + dto.caseId + ") since " + since);
                } else if (dto.isDbLock()) {
                    //DB Lock
                    sb.append("Database " + dto.database + " since " + since + " because of " + dto.cause);
                }
                sb.append(" by " + dto.userName + " (ID " + dto.userId + ", Client-ID " + dto.clientId + ")");
            }
        }
        return sb.toString();
    }

    public LockDTO[] getLockDtos() {
        final LockDTO[] tmp = new LockDTO[lockDtos.length];
        System.arraycopy(lockDtos, 0, tmp, 0, lockDtos.length);
        return tmp;
    }

    public LockDTO[] getMatchingLockDtos(final LockDTO pMatch) {
        if (pMatch == null) {
            throw new IllegalArgumentException("to filter list of lock dtos you have to specify a matching lock dto");
        }
        //final String clientId = pClientId == null ? "" : pClientId.trim();
        List<LockDTO> list = new ArrayList<>();
        for (LockDTO dto : lockDtos) {
            if (dto != null && dto.isMatching(pMatch)) {
                list.add(dto);
            }
        }
        LockDTO[] tmp = new LockDTO[list.size()];
        list.toArray(tmp);
        return tmp;
    }

    public LockDTO[] getLockDtosByClient(final String pClientId) {
        final String clientId = (pClientId == null ? "" : pClientId.trim());
        if (clientId.isEmpty()) {
            throw new IllegalArgumentException("Client-ID cannot be null or empty!");
        }
        return getMatchingLockDtos(LockDTO.createWithClientId(pClientId));
    }

    public LockDTO[] getLockDtosByOtherClients(final String pBeyondClientId) {
        final String clientId = (pBeyondClientId == null ? "" : pBeyondClientId.trim());
        if (clientId.isEmpty()) {
            throw new IllegalArgumentException("Client-ID cannot be null or empty!");
        }
        LockDTO[] lockDtosClient = getMatchingLockDtos(LockDTO.createWithClientId(clientId));
        LockDTO[] lockDtosAllClients = getLockDtos();
        List<LockDTO> tmp = new ArrayList<>();
        for (LockDTO lockDtoAllClient : lockDtosAllClients) {
            if (lockDtoAllClient == null) {
                continue;
            }
            boolean found = false;
            for (LockDTO lockDtoClient : lockDtosClient) {
                if (lockDtoClient != null && lockDtoClient == lockDtoAllClient) {
                    found = true;
                    break;
                }
            }
            if (found) {
                continue;
            }
            tmp.add(lockDtoAllClient);
        }
        LockDTO[] lockDtosOtherClients = new LockDTO[tmp.size()];
        tmp.toArray(lockDtosOtherClients);
        return lockDtosOtherClients;
    }

    public Long getCaseId() {
        return caseId;
    }

    public Long getProcessId() {
        return processId;
    }

}
