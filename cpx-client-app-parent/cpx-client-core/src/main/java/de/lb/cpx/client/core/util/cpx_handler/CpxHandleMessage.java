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
package de.lb.cpx.client.core.util.cpx_handler;

import de.FileUtils;
import static de.lb.cpx.client.core.util.cpx_handler.CpxHandleLink.PROTOCOL_CASE_LABEL;
import static de.lb.cpx.client.core.util.cpx_handler.CpxHandleLink.PROTOCOL_CASE_PREFIX;
import static de.lb.cpx.client.core.util.cpx_handler.CpxHandleLink.PROTOCOL_PREFIX;
import static de.lb.cpx.client.core.util.cpx_handler.CpxHandleLink.PROTOCOL_PROCESS_LABEL;
import static de.lb.cpx.client.core.util.cpx_handler.CpxHandleLink.PROTOCOL_PROCESS_PREFIX;
import de.lb.cpx.service.information.CpxDatabase;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author niemeier
 */
public class CpxHandleMessage {

    private static final Logger LOG = Logger.getLogger(CpxHandleMessage.class.getName());

    public final String message;
    public final String userName;
    public final String sessionId;
    public final boolean isProtocol;
    public final boolean isFile;
    public final CpxDatabase cpxDatabase;

    public CpxHandleMessage(final String pMessage) {
        LOG.log(Level.FINEST, "Read message '" + pMessage + "'");
        String msg = pMessage == null ? "" : pMessage.trim();
        String user = "";
        String sessId = "";
        final String userDelim = "!@:";
        int userPos = msg.indexOf(userDelim);
        if (userPos > -1) {
            user = msg.substring(0, userPos).trim();
            msg = msg.substring(userPos + userDelim.length()).trim();
            final String sessionIdDelim = "?@:";
            int sessionIdPos = msg.indexOf(sessionIdDelim);
            if (sessionIdPos > -1) {
                sessId = msg.substring(0, sessionIdPos).trim();
                msg = msg.substring(sessionIdPos + sessionIdDelim.length()).trim();
            }
        }
        userName = user;
        sessionId = sessId;
        if (msg.toLowerCase().startsWith("url:")) {
            msg = msg.substring(4).trim();
        }
        isProtocol = msg.toLowerCase().startsWith(PROTOCOL_PREFIX);
        isFile = !isProtocol && (FileUtils.isValidFilepath(msg));
        if (isProtocol) {
            if (msg.toLowerCase().startsWith(PROTOCOL_PREFIX + "//")) {
                msg = msg.substring(PROTOCOL_PREFIX.length() + 2).trim();
            } else {
                msg = msg.substring(PROTOCOL_PREFIX.length()).trim();
            }
            if (msg.startsWith(PROTOCOL_CASE_LABEL)) {
                msg = msg.replace(" ", "_").toLowerCase();
            } else if (msg.startsWith(PROTOCOL_PROCESS_LABEL)) {
                msg = msg.replace(" ", "_").toLowerCase();
            }
        }
        final String portDelim = "@Port=";
        int portPos = msg.indexOf("@Port=");
        if (portPos > -1) {
            int start = portPos + portDelim.length();
            int end = indexOf(msg, start);
            final String port = msg.substring(start, end).trim();
            msg = (msg.substring(0, portPos) + msg.substring(end)).trim();
        }
        final String db = extractDatabase(msg);
        //CpxDatabase actualDatabase = CpxDatabase.getCpxDatabaseFromString(Session.instance().getCpxDatabase());
        cpxDatabase = CpxDatabase.getCpxDatabaseFromString(db, "");
        if (!db.isEmpty()) {
            int pos = msg.lastIndexOf(db);
            msg = msg.substring(0, pos - 1).trim();
        }
//        if (!msg.toLowerCase().startsWith(PROTOCOL_CASE_PREFIX) 
//                && !msg.toLowerCase().startsWith(PROTOCOL_WORKFLOW_PREFIX) 
//                && StringUtils.countMatches(msg, "_") == 1) {
//            //Seems to be a string like IKZ_FALLNR, but case prefix is missing, so I'll add it here!
//            msg = PROTOCOL_CASE_PREFIX + msg;
//        }
        message = msg;
        LOG.log(Level.FINEST, "Message '" + pMessage + "' is recognized as '" + this.toString() + "'");
    }

    private int indexOf(final String pHeap, final int pFromIndex) {
        return indexOf(pHeap, pFromIndex, "?@:", ":", "@", ";", "=");
    }

    private int indexOf(final String pHeap, final int pFromIndex, String... pNeedles) {
        int minIndex = pHeap.length();
        for (String s : pNeedles) {
            int idx = pHeap.indexOf(s, pFromIndex);
            if (idx > -1 && idx < minIndex) {
                minIndex = idx;
            }
        }
        return minIndex;
    }

    protected static String extractDatabase(final String pMessage) {
//        if (pMessage == null || pMessage.trim().isEmpty()) {
//            return "";
//        }
        int pos = pMessage.lastIndexOf('@');
        if (pos == -1) {
            return "";
        } else {
            return pMessage.substring(pos + 1).trim();
        }
    }

    public String getUserName() {
        return userName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public boolean isProtocol() {
        return isProtocol;
    }

    public boolean isFile() {
        return isFile;
    }

    public boolean isUnknown() {
        return !isProtocol && !isFile;
    }

    public CpxDatabase getCpxDatabase() {
        return cpxDatabase;
    }

    public String getDatabase() {
        return cpxDatabase == null ? "" : cpxDatabase.getName();
    }

    public String getConnectionString() {
        return cpxDatabase == null ? "" : cpxDatabase.getConnectionString();
    }

    public String getPersistenceUnit() {
        return cpxDatabase == null ? "" : cpxDatabase.getPersistenceUnit();
    }

    public boolean isCpxfFile() {
        return isFile && FilenameUtils.getExtension(message).equalsIgnoreCase("cpxf");
    }

    public boolean isCaseDataProtocol() {
        return isProtocol && message.toLowerCase().startsWith(PROTOCOL_CASE_PREFIX);
    }

    public boolean isWorkflowDataProtocol() {
        return isProtocol && message.toLowerCase().startsWith(PROTOCOL_PROCESS_PREFIX);
    }

    public String[] getCaseDataFromProtocol() {
        String[] result = new String[2];
        if (isCaseDataProtocol()) {
            final String m = message.substring(PROTOCOL_CASE_PREFIX.length()).trim();
            final String s[] = m.split("_");
            if (s.length == 1) {
                //just case number was transmitted
                final String hospital = "";
                final String caseNumber = s[0].trim();
                result[0] = hospital;
                result[1] = caseNumber;
            } else {
                final String hospital = s[0].trim();
                final String caseNumber = s.length > 1 ? s[1].trim() : "";
                result[0] = hospital;
                result[1] = caseNumber;
            }
        }
        return result;
    }

    public String[] getWorkflowDataFromProtocol() {
        String[] result = new String[1];
        if (isWorkflowDataProtocol()) {
            final String m = message.substring(PROTOCOL_PROCESS_PREFIX.length()).trim();
            final String s[] = m.split("_");
            final String hospital = s[0].trim();
            //final String caseNumber = s.length > 1 ? s[1].trim() : "";
            result[0] = hospital;
        }
        return result;
    }

    public File getFile() {
        if (isFile) {
            return new File(message);
        }
        return null;
    }

    public String getFileContent() throws IOException {
        File file = getFile();
        if (file == null) {
            return null;
        }
        if (!file.exists()) {
            throw new IllegalStateException("Cannot read content, file does not exist: " + file.getAbsolutePath());
        }
        if (!file.isFile()) {
            throw new IllegalStateException("Cannot read content, path is not a file but a directory: " + file.getAbsolutePath());
        }
        if (!file.canRead()) {
            throw new IllegalStateException("Cannot read content, no read permission for file: " + file.getAbsolutePath());
        }
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded, CpxSystemProperties.DEFAULT_ENCODING);
    }

    @Override
    public String toString() {
        return message + " [" + (isProtocol() ? "PROTOCOL" : isFile() ? "FILE" : "UNKNOWN") + "] on database " + (cpxDatabase == null ? "unknown" : cpxDatabase.getConnectionString());
    }

}
