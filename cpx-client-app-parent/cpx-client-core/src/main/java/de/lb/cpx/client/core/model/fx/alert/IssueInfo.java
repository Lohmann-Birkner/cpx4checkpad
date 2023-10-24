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
package de.lb.cpx.client.core.model.fx.alert;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.service.information.DatabaseInfo;
import de.lb.cpx.shared.version.VersionHistory;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Collects some (hopefully) useful information to analyze problems.
 *
 * @author niemeier
 */
public class IssueInfo {

    private static final Logger LOG = Logger.getLogger(IssueInfo.class.getName());

    public final Date time;
    public final Long userId;
    public final String clientId;
    public final String database;
    public final DatabaseInfo databaseInfo;
    public final DatabaseInfo databaseInfoCommon;
    public final Long roleId;
    public final String userName;
    public final String serverHost;
    public final Integer serverPort;
    public final String server;
    public final String hostIp;
    public final String hostName;
    public final String javaVersion;
    public final String os;
    public final String winUsername;
    public final Long totalMemory;
    public final Long maxHeapMemory;
    public final Integer availableProcessors;
    public final String version = VersionHistory.getRecentVersion().getVersion();
    public final String build = MainApp.instance().getVersion();

    /**
     * Creates an object with static user and system information
     */
    public IssueInfo() {
        Date lTime = null;
        Long lUserId = null;
        String lClientId = "";
        String lDatabase = "";
        DatabaseInfo lDatabaseInfo = null;
        DatabaseInfo lDatabaseInfoCommon = null;
        Long lRoleId = null;
        String lUserName = "";
        String lServerHost = "";
        Integer lServerPort = null;
        String lServer = "";
        String lHostIp = "";
        String lHostName = "";
        String lJavaVersion = "";
        String lOs = "";
        String lWinUsername = "";
        Long lTotalMemory = null;
        Long lMaxHeapMemory = null;
        Integer lAvailableProcessors = null;
        try {
            CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();

            //CPX Info
            lTime = Calendar.getInstance().getTime();
            lUserId = Session.instance().getCpxUserId();
            lClientId = Session.instance().getClientId();
            lDatabase = Session.instance().getCpxDatabase();
            lDatabaseInfo = Session.instance().getCpxDatabaseInfo();
            lDatabaseInfoCommon = Session.instance().getCpxDatabaseInfoCommon();
            lRoleId = Session.instance().getCpxActualRoleId();
            lUserName = Session.instance().getCpxUserName();

            //General Info
            lHostIp = cpxProps.getHostIp();
            lHostName = cpxProps.getHostName();
            lJavaVersion = cpxProps.getJavaVersion();
            lOs = cpxProps.getOsName();
            lWinUsername = cpxProps.getUserName();
            //lTotalMemory = cpxProps.getTotalPhysicalMemorySize();
            lMaxHeapMemory = cpxProps.getMaxHeapMemorySize();
            lAvailableProcessors = cpxProps.getAvailableProcessors();
            final com.sun.management.OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();
            lTotalMemory = osBean.getTotalPhysicalMemorySize() / 1024 / 1024;
            lServerHost = CpxClientConfig.instance().getServerHost();
            lServerPort = CpxClientConfig.instance().getServerPort();
            lServer = lServerHost + ":" + lServerPort;
        } catch (Exception ex) {
            LOG.log(Level.FINEST, "Was not able to collect data for uncaught exception", ex);
        }
        time = lTime;
        userId = lUserId;
        clientId = lClientId;
        database = lDatabase;
        databaseInfo = lDatabaseInfo;
        databaseInfoCommon = lDatabaseInfoCommon;
        roleId = lRoleId;
        userName = lUserName;
        serverHost = lServerHost;
        serverPort = lServerPort;
        server = lServer;
        hostIp = lHostIp;
        hostName = lHostName;
        javaVersion = lJavaVersion;
        os = lOs;
        winUsername = lWinUsername;
        totalMemory = lTotalMemory;
        maxHeapMemory = lMaxHeapMemory;
        availableProcessors = lAvailableProcessors;
    }

    /**
     * Get values as a map
     *
     * @return map of collected user/system attributes
     */
    public Map<String, String> getValues() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> map = new LinkedHashMap<>();
        map.put("Version", version);
        map.put("Build", build);
        map.put("Username", userName);
        map.put("User-ID", String.valueOf(userId));
        map.put("Client-ID", clientId);
        map.put("Database", database);
        if (databaseInfo != null) {
            map.put("Database Info", databaseInfo.getIdentifier() + " (" + databaseInfo.getDriverName() + ")");
        }
        if (databaseInfoCommon != null) {
            map.put("Database Info (Common)", databaseInfoCommon.getIdentifier() + " (" + databaseInfoCommon.getDriverName() + ")");
        }
        map.put("Role-ID", String.valueOf(roleId));
        map.put("CPX Server", server);
        map.put("Time", time == null ? "" : df.format(time));
        map.put("Host IP", hostIp);
        map.put("Host name", hostName);
        map.put("Windows user", winUsername);
        map.put("Java", javaVersion);
        map.put("OS", os);
        map.put("Total memory", String.valueOf(totalMemory) + " MB");
        map.put("Max. heap", String.valueOf(maxHeapMemory) + " MB");
        map.put("CPUs", String.valueOf(availableProcessors));
        return map;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : getValues().entrySet()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(entry.getKey() + ": " + entry.getValue());
        }
        return sb.toString();
    }

}
