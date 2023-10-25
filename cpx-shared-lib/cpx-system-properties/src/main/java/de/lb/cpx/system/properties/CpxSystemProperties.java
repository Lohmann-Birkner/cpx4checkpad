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
package de.lb.cpx.system.properties;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxSystemProperties implements CpxSystemPropertiesInterface {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(CpxSystemProperties.class.getName());
    public static final String DEFAULT_ENCODING = "Cp1252";
    private static String mCpxHome = "";

    public final boolean isServer;
    public final boolean isJUnitTest;
    public final boolean isDebugMode;
    public final boolean isAssertionsEnabled;
    public final String[] jvmArguments;
    public final String wildflyCoreVersion;
    public final boolean cpxEarFile;
    public final boolean cpxWebAppFile;
    public final String deploymentDir;
    public final String fileSeparator;
    public final String javaClassPath;
    public final String javaHome;
    public final String javaVendor;
    public final String javaVendorUrl;
    public final String javaVersion;
    public final String javaVmName;
    public final String jdkModulePath;
    public final String javaFxRuntimeVersion;
    public final String javaFxVersion;
    public final boolean isRequiredJavaVersion;
    public final String lineSeparator;
    public final String osArch;
    public final String osName;
    public final String osVersion;
    public final String pathSeparator;
    public final String userDir;
    public final String userHome;
    public final String appData;
    public final String appDataLocal;
    public final String appDataRoaming;
    public final String userName;
    public final String cpxHome;
    public final String programFiles;
    public final String programFilesX86;
    public final String oracleHome;
    public final String userLanguage; //user.language
    public final String userRegion;// user.region
    public final String hostName;
    public final String hostIp;
    public final String defaultEncoding;
    public final String clientLogFile;
    public final String serverLogDir;
    public final String serverLogFile;
    public final String serverLoggingProperties;
    //public final long totalPhysicalMemorySize;
    public final long maxHeapMemorySize;
    public final int availableProcessors;
    public final int wildflyManagementPort;
    public final int wildflyHttpPort;
    public final String prismOrder;
    public final String tmpDir;
    //public final String clientCatalogDir;
    //You can add more properties like available processors, free memory, resolution of monitors etc.

    protected CpxSystemProperties(
            final boolean isServer,
            final boolean isJUnitTest,
            final boolean isDebugMode,
            final boolean isAssertionsEnabled,
            final String[] jvmArguments,
            final String wildflyCoreVersion,
            final boolean cpxEarFile,
            final boolean cpxWebAppFile,
            final String deploymentDir,
            final String fileSeparator,
            final String javaClassPath,
            final String javaHome,
            final String javaVendor,
            final String javaVendorUrl,
            final String javaVersion,
            final String javaVmName,
            final String jdkModulePath,
            final String javaFxRuntimeVersion,
            final String javaFxVersion,
            final String lineSeparator,
            final String osArch,
            final String osName,
            final String osVersion,
            final String pathSeparator,
            final String userDir,
            final String userHome,
            final String userName,
            final String cpxHome,
            final String programFiles,
            final String programFilesX86,
            final String oracleHome,
            final String userLanguage,
            final String userRegion,
            final String hostName,
            final String hostIp,
            final String defaultEncoding,
            //final long totalPhysicalMemorySize,
            final long maxHeapMemorySize,
            final int availableProcessors,
            final String serverLogDir,
            final String serverLogFile,
            final String serverLoggingProperties,
            final int wildflyManagementPort,
            final int wildflyHttpPort,
            final String prismOrder,
            final String tmpDir
    //final String clientCatalogDir
    ) {
        this.isServer = isServer;
        this.isJUnitTest = isJUnitTest;
        this.isDebugMode = isDebugMode;
        this.isAssertionsEnabled = isAssertionsEnabled;
        this.jvmArguments = jvmArguments;
        this.wildflyCoreVersion = toStr(wildflyCoreVersion);
        this.cpxEarFile = cpxEarFile;
        this.cpxWebAppFile = cpxWebAppFile;
        this.fileSeparator = toStr(fileSeparator);
        this.javaClassPath = toStr(javaClassPath);
        this.javaHome = toStr(javaHome);
        this.javaVendor = toStr(javaVendor);
        this.javaVendorUrl = toStr(javaVendorUrl);
        this.javaVersion = toStr(javaVersion);
        this.javaVmName = toStr(javaVmName);
        int reqJavaVersion;
        try {
            reqJavaVersion = javaVersionCompare(this.javaVersion, REQUIRED_JAVA_VERSION);
        } catch (IllegalArgumentException ex) {
            LOG.log(Level.SEVERE, "Java version check failed!", ex);
            reqJavaVersion = 0;
        }
        this.isRequiredJavaVersion = reqJavaVersion >= 0;
        this.jdkModulePath = toStr(jdkModulePath);
        this.javaFxRuntimeVersion = toStr(javaFxRuntimeVersion);
        this.javaFxVersion = toStr(javaFxVersion);
        this.lineSeparator = lineSeparator; //Do not trim this!
        this.osArch = toStr(osArch);
        this.osName = toStr(osName);
        this.osVersion = toStr(osVersion);
        this.pathSeparator = toStr(pathSeparator);

        String lUserDir = toStr(userDir);
        if (!lUserDir.isEmpty() && !lUserDir.endsWith(this.fileSeparator)) {
            lUserDir += this.fileSeparator;
        }

        this.userDir = toStr(lUserDir);
        String lUserHome = toStr(userHome);
        if (!lUserHome.isEmpty() && !lUserHome.endsWith(this.fileSeparator)) {
            lUserHome += this.fileSeparator;
        }
        this.userHome = lUserHome;
        this.appData = userHome + this.fileSeparator + "AppData" + this.fileSeparator;
        this.appDataLocal = appData + "Local" + fileSeparator + "CPX" + fileSeparator;
        this.appDataRoaming = appData + "Roaming" + fileSeparator + "CPX" + fileSeparator;
        this.userName = toStr(userName);
        this.userLanguage = toStr(userLanguage);
        this.userRegion = toStr(userRegion);
        String lCpxHome = toStr(cpxHome);
        if (!lCpxHome.isEmpty() && !lCpxHome.endsWith(this.fileSeparator)) {
            lCpxHome += this.fileSeparator;
        }

        this.cpxHome = toStr(lCpxHome);
        this.programFiles = toStr(programFiles);
        this.programFilesX86 = toStr(programFilesX86);
        this.oracleHome = toStr(oracleHome);
        this.hostName = toStr(hostName);
        this.hostIp = toStr(hostIp);
        this.defaultEncoding = toStr(defaultEncoding);

        String lDeploymentDir = toStr(deploymentDir);
        if (!lDeploymentDir.isEmpty() && !lDeploymentDir.endsWith(this.fileSeparator)) {
            lDeploymentDir += this.fileSeparator;
        }

        this.deploymentDir = toStr(lDeploymentDir);

        String lLogFile = "";
        //To be exactly: This is only in use on the client
        final String lHostName = this.hostName;
        final String lUserName = this.userName;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss,SSS");
        final String dateTime = formatter.format(new Date());
        lLogFile = "CPX {$app} " + dateTime + " " + lUserName + " on " + lHostName + ".log";
        this.clientLogFile = toStr(lLogFile);
        //this.totalPhysicalMemorySize = totalPhysicalMemorySize;
        this.maxHeapMemorySize = maxHeapMemorySize;
        this.availableProcessors = availableProcessors;

        String lServerLogDir = toStr(serverLogDir);
        if (!lServerLogDir.isEmpty() && !lServerLogDir.endsWith(this.fileSeparator)) {
            lServerLogDir += this.fileSeparator;
        }

        this.serverLogDir = toStr(lServerLogDir);
        this.serverLogFile = toStr(serverLogFile);
        this.serverLoggingProperties = toStr(serverLoggingProperties);
        this.wildflyManagementPort = wildflyManagementPort;
        this.wildflyHttpPort = wildflyHttpPort;
        this.prismOrder = toStr(prismOrder);

        String lTmpDir = toStr(tmpDir);
        if (!lTmpDir.isEmpty() && !lTmpDir.endsWith(this.fileSeparator)) {
            lTmpDir += this.fileSeparator;
        }
        this.tmpDir = lTmpDir;
//        String lClientCatalogDir = toStr(clientCatalogDir);
//        if (!lClientCatalogDir.isEmpty() && !lClientCatalogDir.endsWith(this.fileSeparator)) {
//            lClientCatalogDir += this.fileSeparator;
//        }
//        this.clientCatalogDir = lClientCatalogDir;
    }

    public static String getCpxHomeFromJndi() throws Exception {
        Context ctx = new InitialContext();

        String cpxHomeTmp = (String) ctx.lookup("java:global/CPX_HOME");

        cpxHomeTmp = (cpxHomeTmp == null) ? "" : cpxHomeTmp.trim();
        return cpxHomeTmp;
    }

    public static synchronized void useUseDirAsCpxHome(final boolean pUseRunningDirectoryAsCpxHome) {
        if (!pUseRunningDirectoryAsCpxHome) {
            mCpxHome = "";
            return;
        }
        final String userDir = CpxSystemProperties.getInstance().getUserDir();
        LOG.log(Level.INFO, "Set CPX_HOME explicitly to user directory (the directory where your code is running): " + userDir);
        setCpxHome(userDir);
    }

    /**
     * Overrides the automated way to get CPX_HOME
     *
     * @param pCpxHome CPX_HOME to be used
     */
    public static void setCpxHome(final String pCpxHome) {
        final String cpxHome = (pCpxHome == null ? "" : pCpxHome.trim());
        LOG.log(Level.INFO, "Set CPX_HOME explicitly to '" + cpxHome + "'");
        if (cpxHome.isEmpty()) {
            mCpxHome = cpxHome;
            return;
        }
        File file = new File(cpxHome);
        if (!file.exists()) {
            throw new IllegalArgumentException("This path does not exist: " + file.getAbsolutePath());
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("This path is not a directory: " + file.getAbsolutePath());
        }
        if (!file.canRead()) {
            throw new IllegalArgumentException("No permissions to read from this directory: " + file.getAbsolutePath());
        }
        mCpxHome = file.getAbsolutePath();
    }

    private static String readWildflyCoreVersion() {
        String version = "";
        try {
            MBeanServer mbeanServer = java.lang.management.ManagementFactory.getPlatformMBeanServer();
            ObjectName objectName = new ObjectName("jboss.as:management-root=server");

            String releaseCodename = (String) mbeanServer.getAttribute(objectName, "releaseCodename");
            String releaseVersion = (String) mbeanServer.getAttribute(objectName, "releaseVersion");

            version = toStr(releaseVersion + " (" + releaseCodename + ")");
        } catch (AttributeNotFoundException | InstanceNotFoundException | MBeanException | MalformedObjectNameException | ReflectionException ex) {
            Logger.getLogger(CpxSystemProperties.class.getName()).log(Level.FINEST, null, ex);
        }
        return version;
    }

    private static String getArgumentValue(final String[] pArguments, final String pArgName) {
        if (pArguments == null || pArguments.length == 0) {
            return "";
        }
        final String argName = "-D" + toStr(pArgName) + "=";
        String argValue = "";
        for (String arg : pArguments) {
            if (arg.startsWith(argName)) {
                final String[] tmp = arg.split("=");
                argValue = toStr(tmp[1]);
                break;
            }
        }
        return argValue;
    }

    public static synchronized CpxSystemPropertiesInterface getInstance() {

        Properties props = System.getProperties();

        final String wildflyCoreVersion = readWildflyCoreVersion();
        final boolean isServerTmp = (wildflyCoreVersion != null && !wildflyCoreVersion.trim().isEmpty());
        final boolean isJUnitTest = isJUnitTest();
        final boolean isDebugMode = isDebugMode();

        String cpxHomeTmp = mCpxHome;
        if (cpxHomeTmp.isEmpty()) {
            if (isServerTmp) {
                //Most servers should be started with java -Djava.awt.headless=true
                cpxHomeTmp = toStr(System.getenv("CPX_HOME"));
                if (cpxHomeTmp.isEmpty()) {
                    try {
                        cpxHomeTmp = getCpxHomeFromJndi();
                    } catch (Exception ex) {
                        Logger.getLogger(CpxSystemProperties.class.getName()).log(Level.FINEST, "Please notice: CPX_HOME is not available on CPX Client!", ex);
                    }
                }
            } else if (isJUnitTest) {
                cpxHomeTmp = getCpxHomeForTesting();
            }
        }

        String hostNameTmp = "";
        String hostIpTmp = "";
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostNameTmp = toStr(addr.getHostName());
            hostIpTmp = toStr(addr.getHostAddress());
        } catch (UnknownHostException ex) {
            LOG.log(Level.FINEST, "Was not able to resolve host name and its IP address", ex);
        }

        String userDirTmp = props.getProperty("user.dir");
        final String deploymentDir = isServerTmp ? System.getProperty("jboss.server.base.dir") : "";

        boolean cpxEarFile = false;
        boolean cpxWebAppFile = false;
        if (isServerTmp) {
            ClassLoader s = CpxSystemProperties.class.getClassLoader();
            Field f;
            try {
                f = s.getClass().getDeclaredField("protectionDomains");
                f.setAccessible(true);
                @SuppressWarnings("unchecked")
                Map<CodeSource, ProtectionDomain> protectionDomains = (Map<CodeSource, ProtectionDomain>) f.get(s);
                Iterator<Map.Entry<CodeSource, ProtectionDomain>> it = protectionDomains.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<CodeSource, ProtectionDomain> entry = it.next();
                    CodeSource key = entry.getKey();
                    //ProtectionDomain value = entry.getValue();
                    LOG.log(Level.FINEST, key.getLocation().getFile());
                    if (key.getLocation().getFile().contains("cpx-web-app.war")) {
                        cpxEarFile = false;
                        cpxWebAppFile = true;
                        break;
                    }
                    if (key.getLocation().getFile().contains("cpx-ear.ear")) {
                        cpxEarFile = true;
                        cpxWebAppFile = false;
                        break;
                    }
                }
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(CpxSystemProperties.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //final com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean)java.lang.management.ManagementFactory.getOperatingSystemMXBean();
        //long totalPhysicalMemorySize = os.getTotalPhysicalMemorySize() / 1024 / 1024;
        long maxHeapMemorySize = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        int availableProcessors = Runtime.getRuntime().availableProcessors();

        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        String[] jvmArguments = new String[arguments.size()];
        arguments.toArray(jvmArguments);
        final boolean assertionsEnabled = arguments.contains("-ea");

        String serverLogDir = "";
        String serverLogFile = "";
        String serverLoggingProperties = "";
        String prismOrder = "";
//        String clientCatalogDir = "";
        if (isServerTmp) {
            String serverLogPath = toStr(getArgumentValue(jvmArguments, "org.jboss.boot.log.file"));
            if (!serverLogPath.isEmpty()) {
                final File serverLog = new File(serverLogPath);
                serverLogDir = toStr(serverLog.getParentFile().getAbsolutePath());
                serverLogFile = toStr(serverLog.getName());
            }
            String loggingPropertiesPath = toStr(getArgumentValue(jvmArguments, "logging.configuration"));
            if (!loggingPropertiesPath.isEmpty()) {
                final File serverLoggingPropertiesFile = new File(loggingPropertiesPath);
                serverLoggingProperties = serverLoggingPropertiesFile.getAbsolutePath();
            }
        } else {
            prismOrder = toStr(getArgumentValue(jvmArguments, "prism.order"));
        }
        /* else {
            clientCatalogDir = CpxCustomSystemProperties.getInstance().getCpxCatalogDir();
            if (!clientCatalogDir.isEmpty()) {
                clientCatalogDir = new File(clientCatalogDir).getAbsolutePath();
            }
        } */

        int wildFlyManagementPort = 0;
        int wildflyHttpPort = 0;
        if (isServerTmp) {
            String managementPort = toStr(getArgumentValue(jvmArguments, "jboss.management.http.port"));
            if (!managementPort.isEmpty()) {
                try {
                    wildFlyManagementPort = Integer.parseInt(managementPort);
                } catch (NumberFormatException ex) {
                    LOG.log(Level.WARNING, "This is not a valid Wildfly Management Port: " + managementPort, ex);
                }
            }
            String httpPort = toStr(getArgumentValue(jvmArguments, "jboss.http.port"));
            if (!httpPort.isEmpty()) {
                try {
                    wildflyHttpPort = Integer.parseInt(httpPort);
                } catch (NumberFormatException ex) {
                    LOG.log(Level.WARNING, "This is not a valid Wildfly HTTP Port: " + httpPort, ex);
                }
            }
        }
        if (wildFlyManagementPort == 0) {
            LOG.log(Level.FINE, "No Wildfly Management Port found, assume default");
            wildFlyManagementPort = 9995;
        }
        if (wildflyHttpPort == 0) {
            LOG.log(Level.FINE, "No Wildfly HTTP Port found, assume default");
            wildflyHttpPort = 8085;
        }

        final String tmpDir = System.getProperty("java.io.tmpdir");

        final CpxSystemPropertiesInterface lInstance = new CpxSystemProperties(
                isServerTmp,
                isJUnitTest,
                isDebugMode,
                assertionsEnabled,
                jvmArguments,
                wildflyCoreVersion,
                cpxEarFile,
                cpxWebAppFile,
                deploymentDir,
                props.getProperty("file.separator"), //FileSeparator,
                props.getProperty("java.class.path"), //JavaClassPath,
                props.getProperty("java.home"), //JavaHome,
                props.getProperty("java.vendor"), //JavaVendor,
                props.getProperty("java.vendor.url"), //JavaVendorUrl,
                props.getProperty("java.version"), //JavaVersion,
                props.getProperty("java.vm.name"), //JavaVmName,
                props.getProperty("jdk.module.path"), //JdkModulePath
                props.getProperty("javafx.runtime.version"), //JavaFxRuntimeVersion
                props.getProperty("javafx.version"), //JavaFxVersion
                props.getProperty("line.separator"), //LineSeparator,
                props.getProperty("os.arch"), //OsArch,
                props.getProperty("os.name"), //OsName,
                props.getProperty("os.version"), //OsVersion,
                props.getProperty("path.separator"), //PathSeparator,
                userDirTmp, //UserDir,
                props.getProperty("user.home"), //UserHome,
                props.getProperty("user.name"), //UserName
                cpxHomeTmp, //CPX Home
                System.getenv("ProgramFiles"), //Program Files
                //System.getenv("%programfiles% (x86)") //Program Files 32-bit folder on 64-bit PCs
                System.getenv("ProgramFiles(x86)"), //Program Files 32-bit folder on 64-bit PCs
                System.getenv("ORACLE_HOME"),
                props.getProperty("user.language"), //UserLanguage,
                props.getProperty("user.region"), //UserRegion
                hostNameTmp, //HostName (like LB323)
                hostIpTmp, //HostName (like LB323)
                props.getProperty("file.encoding"),
                //totalPhysicalMemorySize,
                maxHeapMemorySize,
                availableProcessors,
                serverLogDir,
                serverLogFile,
                serverLoggingProperties,
                wildFlyManagementPort,
                wildflyHttpPort,
                prismOrder,
                tmpDir
        //clientCatalogDir
        );
        return lInstance;
    }

    /**
     * https://stackoverflow.com/questions/1109019/determine-if-a-java-application-is-in-debug-mode-in-eclipse
     *
     * @return is code running in debug mode?
     */
    public static boolean isDebugMode() {
        String tmp = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
        //yeah, right, that's vendor specific!
        boolean isDebug = tmp.contains("-agentlib:jdwp") || tmp.contains("-Xrunjdwp");
        return isDebug;
    }

    /**
     * https://stackoverflow.com/questions/2341943/how-can-i-find-out-if-code-is-running-inside-a-junit-test-or-not
     *
     * @return is running in JUnit Test
     */
    public static boolean isJUnitTest() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        List<StackTraceElement> list = Arrays.asList(stackTrace);
        for (StackTraceElement element : list) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }

    private static String toStr(String pValue) {
        if (pValue == null) {
            return "";
        }
        return pValue.trim();
    }

    /**
     * Compares two Java version strings (major version incl. minor version)
     *
     * @param version1 Java version 1
     * @param version2 Java version 2
     * @return 0 if equal, lower 0 if version 1 is before version 2, greater 0
     * if version 1 is after version 2
     */
    public static int javaVersionCompare(final String version1, final String version2) {
        final String v1 = version1 == null ? "" : version1.trim();
        final String v2 = version2 == null ? "" : version2.trim();
        if (v1.isEmpty() || v2.isEmpty()) {
            throw new IllegalArgumentException("Cannot compare Java Version " + v1 + " with " + v2 + " because at least one of them is empty");
        }
//        if (!v1.contains("_") || !v2.contains("_")) {
//            //LOG.log(Level.INFO, "Cannot compare Java Version " + v1 + " with " + v2 + " because there seems to be no minor version in at least one of them");
//            throw new IllegalArgumentException("Cannot compare Java Version " + v1 + " with " + v2 + " because there seems to be no minor version in at least one of them");
//        }
        int pos1 = v1.indexOf('_');
        int pos2 = v2.indexOf('_');

        int c = javaMajorVersionCompare(
                v1.substring(0, pos1 > -1 ? pos1 : v1.length()),
                v2.substring(0, pos2 > -1 ? pos2 : v2.length())
        );
        if (c == 0) {
            final String minVersion1 = (pos1 > -1 ? v1.substring(pos1 + 1) : "").trim();
            final String minVersion2 = (pos2 > -1 ? v2.substring(pos2 + 1) : "").trim();
            if (!minVersion1.isEmpty() && minVersion2.isEmpty()) {
                //okay, but what happens if minor version 1 is empty and minor version 2 is not empty (or the other way)?
                c = javaMinorVersionCompare(minVersion1, minVersion2);
            }
        }
        return c;
    }

    /**
     * Compares two Java major version strings
     *
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     *
     * note: It does not work if "1.10" is supposed to be equal to "1.10.0".
     *
     * @param version1 a string of ordinal numbers separated by decimal points.
     * @param version2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less
     * than str2. The result is a positive integer if str1 is _numerically_
     * greater than str2. The result is zero if the strings are _numerically_
     * equal.
     */
    public static int javaMajorVersionCompare(final String version1, final String version2) {
        final String v1 = version1 == null ? "" : version1.trim();
        final String v2 = version2 == null ? "" : version2.trim();
        if (v1.isEmpty() || v2.isEmpty()) {
            throw new IllegalArgumentException("Cannot compare Java Major Version " + v1 + " with " + v2 + " because at least one of them is empty");
        }

        String[] vals1 = v1.split("\\.");
        String[] vals2 = v2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }

    /**
     * Compares two Java minor version strings
     *
     * @param version1 Java minor version 1
     * @param version2 Java minor version 2
     * @return 0 if equal, lower 0 if version 1 is before version 2, greater 0
     * if version 1 is after version 2
     */
    public static int javaMinorVersionCompare(final String version1, final String version2) {
        final String v1 = version1 == null ? "" : version1.trim();
        final String v2 = version2 == null ? "" : version2.trim();

        if (v1.isEmpty() || v2.isEmpty()) {
            throw new IllegalArgumentException("Cannot compare Java Minor Version " + v1 + " with " + v2 + " because at least one of them is empty");
        }
        int v1Int = 0;
        int v2Int = 0;
        try {
            v1Int = Integer.parseInt(v1);
            v2Int = Integer.parseInt(v2);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Cannot compare Java Minor Version " + v1 + " with " + v2 + " because at least one of them is non-numeric", ex);
        }
        return Integer.compare(v1Int, v2Int);
    }

    /**
     * Just for testing
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        LOG.log(Level.INFO, getInstance().toString());
    }

    /**
     * Trys to detect CPX HOME (WD_CPX_Server) without running wildfly for
     * testing and development purposes (JUnit anyone?)
     *
     * @return CPX HOME
     */
    public static String getCpxHomeForTesting() {
        final String tmp = System.getenv("CPX_HOME");
        final String cpxHome = tmp == null ? "" : tmp.trim();

        final String baseDir;
        if (cpxHome.isEmpty()) {
            LOG.log(Level.WARNING, "THERE'S NO CPX_HOME ON THIS SYSTEMS ENV! I TRY A BEST GUESS STRATEGY TO FIND SERVER DIRECTORY (WD_CPX_SERVER?)");
            Properties props = System.getProperties();
            String userDir = props.getProperty("user.dir");
            int pos = userDir.indexOf("cpx-server-app-parent");
            if (pos >= 0) {
                userDir = userDir.substring(0, pos);
            }
            baseDir = userDir + "WD_CPX_Server";
        } else {
            LOG.log(Level.INFO, "Using CPX_HOME from Systems Environment: " + cpxHome);
            baseDir = cpxHome;
        }

        return baseDir;
    }

    @Override
    public boolean isServer() {
        return isServer;
    }

    @Override
    public boolean isTest() {
        return isJUnitTest;
    }

    @Override
    public boolean isClient() {
        return !isServer();
    }

    @Override
    public boolean isDebug() {
        return isDebugMode;
    }

    @Override
    public String getFileSeparator() {
        return this.fileSeparator;
    }

    @Override
    public String getJavaClassPath() {
        return this.javaClassPath;
    }

    @Override
    public String getJavaHome() {
        return this.javaHome;
    }

    @Override
    public String getJavaVendor() {
        return this.javaVendor;
    }

    @Override
    public String getJavaVendorUrl() {
        return this.javaVendorUrl;
    }

    @Override
    public String getJavaVersion() {
        return this.javaVersion;
    }

    @Override
    public String getJavaVmName() {
        return this.javaVmName;
    }

    @Override
    public String getLineSeparator() {
        return this.lineSeparator;
    }

    @Override
    public String getOsArch() {
        return this.osArch;
    }

    @Override
    public String getOsName() {
        return this.osName;
    }

    @Override
    public String getOsVersion() {
        return this.osVersion;
    }

    @Override
    public String getPathSeparator() {
        return this.pathSeparator;
    }

    @Override
    public String getUserDir() {
        return this.userDir;
    }

    @Override
    public String getUserHome() {
        return this.userHome;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public String getCpxHome() {
        return this.cpxHome;
    }

    @Override
    public String getProgramFiles() {
        return this.programFiles;
    }

    @Override
    public String getProgramFilesX86() {
        return this.programFilesX86;
    }

    @Override
    public String getCpxProgramDir() {
        if (isServer()) {
            //Server (WD_CPX_SERVER)
            return getCpxHome();
        }
        //Client (WD_CPX_CLIENT)
        return getUserDir();
        /*
        String lProgramFiles = getProgramFiles();
        if (!lProgramFiles.endsWith(getFileSeparator())) {
            lProgramFiles += getFileSeparator();
        }
        return toStr(lProgramFiles + "CPX" + getFileSeparator());
         */
    }

    @Override
    public String getCpxServerCatalogDir() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return toStr(getCpxProgramDir() + "catalog" + getFileSeparator());
    }

    @Override
    public String getCpxServerFilterDir() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return toStr(getCpxProgramDir() + "filter" + getFileSeparator());
    }

    @Override
    public String getOracleHome() {
        return this.oracleHome;
    }

    @Override
    public String getCpxServerGrouperDir() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return toStr(getCpxProgramDir() + "grouper" + getFileSeparator());
    }

    @Override
    public String getCpxServerRulesDir() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return toStr(getCpxProgramDir() + "rules" + getFileSeparator());
    }

    @Override
    public String getWildflyCoreVersion() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return wildflyCoreVersion;
    }

    @Override
    public String getCpxServerLicenseDir() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return toStr(getCpxProgramDir() + "license" + getFileSeparator());
    }

//    @Override
//    public String getCpxServerScriptsDir() {
//        if (getCpxHome().isEmpty()) {
//            return "";
//        }
//        return toStr(getCpxProgramDir() + "scripts" + getFileSeparator());
//    }
    @Override
    public String getCpxServerResourceBundlesDir() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return toStr(getCpxProgramDir() + "resourceBundle" + getFileSeparator());
    }

    @Override
    public String getCpxServerLocaleDir() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return toStr(getCpxProgramDir() + "locale" + getFileSeparator());
    }

    @Override
    public String getCpxServerReportsDir() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return toStr(getCpxProgramDir() + "reports" + getFileSeparator());
    }

    @Override
    public String getCpxServerCatalogAcgFile() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return toStr(getCpxServerCatalogDir() + "steuerung_acg.mdb");
    }

    @Override
    public String getCpxServerAcgOutputFile() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return toStr(getCpxServerCatalogDir() + "Beispieldatensatz_Visualisierung.csv");
    }

    @Override
    public String getUserLanguage() {
        return this.userLanguage;
    }

    @Override
    public String getUserRegion() {
        return this.userRegion;
    }

    @Override
    public String getCpxServerConfigFile() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
//        CpxCustomSystemPropertiesInterface cpxCustomProps = CpxCustomSystemProperties.getInstance();
//        String configFile = cpxCustomProps.getCpxConfigFile();
//        if (configFile.isEmpty()) {
//            configFile = "cpx_server_config.xml";
//        }
        final String configFile = "cpx_server_config.xml";
        return toStr(getCpxHome() + configFile);
    }

    @Override
    public String getCpxClientConfigFile() {
        if (!isClient()) {
            return "";
        }
        CpxCustomSystemPropertiesInterface cpxCustomProps = CpxCustomSystemProperties.getInstance();
        String configFile = cpxCustomProps.getCpxConfigFile();
        if (configFile.isEmpty()) {
            configFile = "cpx_client_config.xml";
        } else {
            File file = new File(configFile);
            if (file.isAbsolute()) {
                configFile = file.getAbsolutePath();
                if (file.exists() && file.isDirectory()) {
                    if (!configFile.isEmpty() && !configFile.endsWith(this.fileSeparator)) {
                        configFile += this.fileSeparator;
                    }
                    configFile += "cpx_client_config.xml";
                }
                return configFile;
            }
        }
        return toStr(getAppDataRoaming() + configFile);
    }

    @Override
    public String getCpxClientUserConfigFile() {
        if (!isClient()) {
            return "";
        }
        String configFile = "cpx_client_user_config.xml";
        return toStr(getAppDataRoaming() + configFile);
    }

    @Override
    public String getCpxClientCatalogDir() {
        if (!isClient()) {
            return "";
        }
        return getCpxClientCatalogOrDictionaryDirPath("catalog");
//        CpxCustomSystemPropertiesInterface cpxCustomProps = CpxCustomSystemProperties.getInstance();
//        String dir = cpxCustomProps.getCpxCatalogDir();
////        if (!clientCatalogDir.isEmpty()) {
////            return clientCatalogDir;
////        }
//        if (dir.isEmpty()) {
//            dir = "catalog";
//        } else {
//            File file = new File(dir);
//            if (file.isAbsolute()) {
//                dir = file.getAbsolutePath();
//                if (file.exists() && file.isDirectory()) {
//                    if (!dir.isEmpty() && !dir.endsWith(this.fileSeparator)) {
//                        dir += this.fileSeparator;
//                    }
//                    dir += "catalog" + getFileSeparator();
//                }
//                if (!dir.isEmpty() && !dir.endsWith(this.fileSeparator)) {
//                    dir += this.fileSeparator;
//                }
//                return dir;
//            }
//        }
//        return toStr(getAppDataLocal() + dir + getFileSeparator());
    }
    
    private String getCpxClientCatalogOrDictionaryDirPath(String pWhat){
        CpxCustomSystemPropertiesInterface cpxCustomProps = CpxCustomSystemProperties.getInstance();
        String dir = cpxCustomProps.getCpxCatalogDir();
//        if (!clientCatalogDir.isEmpty()) {
//            return clientCatalogDir;
//        }
        if (dir.isEmpty()) {
            dir = pWhat;
        } else {
            File file = new File(dir);
            if (file.isAbsolute()) {
                dir = file.getAbsolutePath();
                if (file.exists() && file.isDirectory() || !file.exists()) {
                    if (!dir.isEmpty() && !dir.endsWith(this.fileSeparator)) {
                        dir += this.fileSeparator;
                    }
                    dir += pWhat + getFileSeparator();
                }
                if (!dir.isEmpty() && !dir.endsWith(this.fileSeparator)) {
                    dir += this.fileSeparator;
                }
                return dir;
            }
        }
        return toStr(getAppDataLocal() + dir + getFileSeparator());
        
    }

//    @Override
//    public String getCpxClientLogDir() {
//        if (!isClient()) {
//            return "";
//        }
//        String dir = "log";
//        return toStr(getAppDataLocal() + dir + getFileSeparator());
//    }
//
//    @Override
//    public String getCpxClientLogFile() {
//        if (!isClient()) {
//            return "";
//        }
//        return this.clientLogFile;
//    }
//
//    @Override
//    public String getCpxClientLoggingPropertiesFile() {
//        if (!isClient()) {
//            return "";
//        }
//        String loggingProperties = "logging.xml";
//        return toStr(getUserDir() + loggingProperties);
//    }
    @Override
    public String getCpxServerLogDir() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return this.serverLogDir;
    }

    @Override
    public String getCpxServerLogFile() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return this.serverLogFile;
    }

    @Override
    public String getCpxServerLoggingPropertiesFile() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return this.serverLoggingProperties;
    }

    @Override
    public int getWildflyManagementPort() {
        return this.wildflyManagementPort;
    }

    @Override
    public String getWildflyManagementUrl() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return "http://" + this.hostIp + ":" + wildflyManagementPort + "/";
    }

    @Override
    public int getWildflyHttpPort() {
        return this.wildflyHttpPort;
    }

    @Override
    public String getWebAppUrl() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return "http://" + this.hostIp + ":" + wildflyHttpPort + "/cpx-web-app/";
    }

    @Override
    public String getCpxClientFontDir() {
        if (!isClient()) {
            return "";
        }
        String dir = "font";
        return toStr(getUserDir() + dir + getFileSeparator());
    }

    @Override
    public String getCpxClientFontFile() {
        if (!isClient()) {
            return "";
        }
        String fontFile = "font.ttf";
        return toStr(getCpxClientFontDir() + fontFile);
    }

    @Override
    public String getCpxClientLibaryDir() {
        if (!isClient()) {
            return "";
        }
        String dir = "dll";
        return toStr(getUserDir() + dir + getFileSeparator());
    }

    @Override
    public String getCpxClientDictionariesDir() {
        if (!isClient()) {
            return "";
        }
//        String dir = "dictionaries";
//        return toStr(getUserDir() + dir + getFileSeparator());
        return getCpxClientCatalogOrDictionaryDirPath("dictionaries");
    }

    @Override
    public String getCpxClientDictionariesIcdDir() {
        final String dictionaryDir = getCpxClientDictionariesDir();
        if (dictionaryDir.isEmpty()) {
            return "";
        }
        String dir = "icd";
        return toStr(dictionaryDir + dir + getFileSeparator());
    }

    @Override
    public String getCpxClientDictionariesOpsDir() {
        final String dictionaryDir = getCpxClientDictionariesDir();
        if (dictionaryDir.isEmpty()) {
            return "";
        }
        String dir = "ops";
        return toStr(dictionaryDir + dir + getFileSeparator());
    }

    @Override
    public String getCpxServerDbUpdateDir() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        String dir = "db_update";
        return toStr(getCpxProgramDir() + dir + getFileSeparator());
    }

    @Override
    public String getCpxServerLibraryDir() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        String dir = "libaries";
        return toStr(getCpxProgramDir() + dir + getFileSeparator());
    }

    @Override
    public String getCpxServerDbUpdateCaseDbFile() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        String file = "updateDB.txt";
        return getCpxServerDbUpdateDir() + file;
    }

    @Override
    public String getCpxServerDbUpdateCaseDbViewsFile() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        String file = "viewsDB.txt";
        return getCpxServerDbUpdateDir() + file;
    }

    @Override
    public String getCpxServerDbUpdateCommonDbFile() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        String file = "updateCommonDB.txt";
        return getCpxServerDbUpdateDir() + file;
    }

//    @Override
//    public String getCpxServerTempDir() {
//        if (getCpxHome().isEmpty()) {
//            return "";
//        }
//        String dir = "temp";
//        return toStr(getCpxProgramDir() + dir + getFileSeparator());
//    }
    @Override
    public String getCpxServerDocumentsDir() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        String dir = "documents";
        return toStr(getCpxProgramDir() + dir + getFileSeparator());
    }

    @Override
    public String getCpxServerDocumentsDir(final String pDatabaseName) {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        return toStr(getCpxServerDocumentsDir() + getCpxPathToStore(pDatabaseName));
    }

    @Override
    public String getCpxPathToStore(final String pDatabaseName, final String pFileName) {
        final String databaseName = toStr(pDatabaseName);
        if (databaseName.isEmpty()) {
            throw new IllegalArgumentException("No database name passed!");
        }
        final String fileName = toStr(pFileName);
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        String currYear = String.valueOf(currentYear);
        String currMonth = (currentMonth < 10 ? "0" : "") + String.valueOf(currentMonth);
        String dir = getHostName() + "_" + databaseName + getFileSeparator() + currYear + getFileSeparator() + currMonth + getFileSeparator() + fileName;
        return toStr(dir);
    }

    @Override
    public String getCpxPathToStore(final String pDatabaseName) {
        final String fileName = "";
        return getCpxPathToStore(pDatabaseName, fileName);
    }

    /*
  @Override
  public String getCpxStylesheetFile() {
    String configFile = "cpx_theme.css";
    return toStr(getUserDir() + configFile);
  }
     */
 /*
  @Override
  public String getRedisPath() {
    return toStr(getUserDir() + "redis" + getFileSeparator());
  }
     */
//    @Override
//    public String getCpxClientTeamviewerPath() {
//        if (!isClient()) {
//            return "";
//        }
//        return toStr(getUserDir() + "TeamViewerQS_de.exe");
//    }
    @Override
    public String toString() {
        String o = "CPX System Properties:"
                + "\nServer/Client: " + (isServer ? "Server" : "Client")
                + "\nRunning Test: " + (isJUnitTest ? "yes" : "no")
                + "\nDebug Mode: " + (isDebugMode ? "yes" : "no")
                + "\nAssertions enable: " + (isAssertionsEnabled ? "yes" : "no")
                + "\nJVM Arguments: " + (jvmArguments.length == 0 ? "none" : String.join(" ", jvmArguments))
                + "\nJAVA_CLASS_PATH: " + javaClassPath
                + "\nJAVA_HOME: " + javaHome
                + "\nJAVA_VENDOR: " + javaVendor
                + "\nJAVA_VENDOR_URL: " + javaVendorUrl
                + "\nJAVA_VERSION: " + (javaVersion + (isRequiredJavaVersion ? "" : " (does not fulfill minimum required Java Version " + REQUIRED_JAVA_VERSION + "!)")
                + "\nJAVA_VM_NAME: " + javaVmName
                + "\nORACLE_HOME: " + oracleHome
                + "\nOS_ARCH: " + osArch
                + "\nOS_NAME: " + osName
                + "\nOS_VERSION: " + osVersion
                + "\nFILE_SEPARATOR: " + fileSeparator
                + "\nLINE_SEPARATOR: " + lineSeparator.replace("\r", "\\r").replace("\n", "\\n")
                + "\nPATH_SEPARATOR: " + pathSeparator
                + "\nPROGRAM_FILES: " + programFiles
                + "\nPROGRAM_FILES_X86: " + programFilesX86
                + "\nUSER_DIR: " + userDir
                + "\nUSER_HOME: " + userHome
                + "\nAPP_DATA: " + appData
                + "\nTMP_DIR: " + tmpDir
                //+ "\nAPP_DATA\\Local: " + appDataLocal
                //+ "\nAPP_DATA\\Roaming: " + appDataRoaming
                + "\nUSER_LANGUAGE: " + userLanguage
                + "\nUSER_NAME: " + userName
                + "\nUSER_REGION: " + userRegion
                + "\nHOST_NAME: " + hostName
                + "\nHOST_IP: " + hostIp
                + "\nDEFAULT_ENCODING: " + (defaultEncoding + (DEFAULT_ENCODING.equalsIgnoreCase(defaultEncoding) ? "" : " (this differs from encoding " + DEFAULT_ENCODING + " which is used in development enviroment!)"))
                //                + "\nTotal physical memory: " + totalPhysicalMemorySize + " MB"
                + "\nMaximum heap memory: " + maxHeapMemorySize + " MB"
                + "\nAvailable processors: " + availableProcessors);

        if (isServer) {
            //Server specific settings
            o += "\n=> Server specific settings:"
                    + "\nWildfly Core Version: " + getWildflyCoreVersion()
                    + "\nWildfly Management URL: " + getWildflyManagementUrl()
                    + "\nWebApp URL: " + getWebAppUrl()
                    + "\nCPX Ear File: " + (getCpxEarFile() ? "yes" : "no")
                    + "\nCPX WebApp File: " + (getCpxWebAppFile() ? "yes" : "no")
                    + "\nDeployment directory: " + getDeploymentDir()
                    + "\nProgramm directory (CPX_HOME): " + getCpxProgramDir()
                    //+ "\nScripts directory: " + getCpxServerScriptsDir()
                    + "\nReports directory: " + getCpxServerReportsDir()
                    + "\nResource bundles directory: " + getCpxServerResourceBundlesDir()
                    + "\nRules directory: " + getCpxServerRulesDir()
                    + "\nConfig file: " + getCpxServerConfigFile()
                    + "\nUpdate directory: " + getCpxServerDbUpdateDir()
                    + "\nCommon DB update file: " + getCpxServerDbUpdateCommonDbFile()
                    + "\nCase DB update file: " + getCpxServerDbUpdateCaseDbFile()
                    + "\nCatalog directory: " + getCpxServerCatalogDir()
                    + "\nHelp file: " + getCpxServerHelpFile()
                    + "\nFilter directory: " + getCpxServerFilterDir()
                    + "\nGrouper directory: " + getCpxServerGrouperDir()
                    + "\nLibrary directory: " + getCpxServerLibraryDir()
                    + "\nLocale directory: " + getCpxServerLocaleDir()
                    //+ "\nTemp directory: " + getCpxServerTempDir()
                    + "\nLicense directory: " + getCpxServerLicenseDir()
                    + "\nACG mapping file (Access DB): " + getCpxServerCatalogAcgFile()
                    + "\nACG output file (CSV): " + getCpxServerAcgOutputFile()
                    + "\nDocuments directory: " + getCpxServerDocumentsDir()
                    + "\nDocuments storage directory: " + getCpxServerDocumentsDir("<DATABASE>")
                    + "\nLogging properties file: " + getCpxServerLoggingPropertiesFile()
                    + "\nLog directory: " + getCpxServerLogDir()
                    + "\nLog file: " + getCpxServerLogFile();
        } else {
            //Client specific settings
            o += "\n=> Client specific settings:"
                    + "\nJavaFX Runtime Version: " + getJavaFxRuntimeVersion()
                    + "\nJavaFX Version: " + getJavaFxVersion()
                    + "\nJDK module path (JavaFX): " + getJdkModulePath()
                    + "\nSoftware acceleration activated: " + (isPrismOrderSw() ? "yes" : "no")
                    + "\nProgramm directory (USER_DIR): " + getCpxProgramDir()
                    + "\nConfig file: " + getCpxClientConfigFile()
                    + "\nUser config file: " + getCpxClientUserConfigFile()
                    + "\nCatalog directory: " + getCpxClientCatalogDir()
                    + "\nCriteria file: " + getCpxCriteriaPath()
                    + "\nFont directory: " + getCpxClientFontDir()
                    + "\nFont file: " + getCpxClientFontFile()
                    + "\nDictionary directory: " + getCpxClientDictionariesDir()
                    //                    + "\nLogging properties file: " + getCpxClientLoggingPropertiesFile()
                    //                    + "\nLog directory: " + getCpxClientLogDir()
                    //                    + "\nLog file: " + getCpxClientLogFile()
                    + "\nPDF.js directory: " + getCpxClientPdfJsDir()
                    + "\nPDF.js viewer file: " + getCpxClientPdfJsViewerFile()
                    + "\nLibrary directory: " + getCpxClientLibaryDir();
        }
        //+ "\nCPX_PROGRAM_DIR: " + getCpxProgramDir()
        //+ "\nCPX_CATALOG_DIR: " + getCpxCatalogDir()
        //+ "\nCPX_GROUPER_DIR: " + getCpxGrouperDir()
        //+ "\nCPX_RULES_DIR: " + getCpxRulesDir()
        //+ "\nCPX_RESOURCE_BUNDLES_DIR: " + getCpxResourceBundlesDir()
        //+ "\nCPX_LOCALE_DIR: " + getCpxLocaleDir()
        //+ "\nCPX_SERVER_CONFIG_FILE: " + getCpxServerConfigFile()
        //+ "\nCPX_CLIENT_CONFIG_FILE: " + getCpxClientConfigFile()
        //+ "\nCPX_CLIENT_USER_CONFIG_FILE: " + getCpxClientUserConfigFile()
        //+ "\nCPX_CLIENT_DATABASE_DIR: " + getCpxClientDatabaseDir();

        return o;
    }

    @Override
    public String getHostName() {
        return this.hostName;
    }

    @Override
    public String getHostIp() {
        return this.hostIp;
    }

    @Override
    public String getDefaultEncoding() {
        return this.defaultEncoding;
    }

    @Override
    public boolean getCpxEarFile() {
        return this.cpxEarFile;
    }

    @Override
    public boolean getCpxWebAppFile() {
        return this.cpxWebAppFile;
    }

    @Override
    public String getDeploymentDir() {
        return this.deploymentDir;
    }

//    @Override
//    public long getTotalPhysicalMemorySize() {
//        return this.totalPhysicalMemorySize;
//    }
    @Override
    public long getMaxHeapMemorySize() {
        return this.maxHeapMemorySize;
    }

    @Override
    public int getAvailableProcessors() {
        return this.availableProcessors;
    }

    @Override
    public boolean isRequiredJavaVersion() {
        return this.isRequiredJavaVersion;
    }

    @Override
    public boolean isAssertionsEnabled() {
        return this.isAssertionsEnabled;
    }

    @Override
    public String[] getJvmArguments() {
        return this.jvmArguments;
    }

    @Override
    public String getPrismOrder() {
        return this.prismOrder;
    }

    @Override
    public boolean isPrismOrderSw() {
        return "j2d".equals(this.prismOrder) || "sw".equals(this.prismOrder);
    }

    @Override
    public String getAppData() {
        return appData;
    }

    @Override
    public String getAppDataLocal() {
        return appDataLocal;
    }

    @Override
    public String getAppDataRoaming() {
        return appDataRoaming;
    }
//    @Override
//    public String getCpxClientHelpDir() {
//        if (!isClient()) {
//            return "";
//        }
//        String dir = "help";
//        return toStr(getUserDir() + dir + getFileSeparator());
//    }

    @Override
    public String getCpxClientPdfJsDir() {
        if (!isClient()) {
            return "";
        }
        String dir = "pdfjs";
        return toStr(getUserDir() + dir + getFileSeparator());
    }

    @Override
    public String getCpxClientPdfJsViewerFile() {
        if (!isClient()) {
            return "";
        }
        //web\viewer.html
        String viewerFile = "web" + getFileSeparator() + "viewer.html";
        return toStr(getCpxClientPdfJsDir() + viewerFile);
    }

    @Override
    public String getCpxServerHelpDir() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        String dir = "help";
        return toStr(getCpxProgramDir() + dir + getFileSeparator());
    }

    @Override
    public String getCpxServerHelpFile() {
        if (getCpxHome().isEmpty()) {
            return "";
        }
        String file = "hilfefunktion_cpx.pdf";
        return getCpxServerHelpDir() + file;
    }

    @Override
    public String getTmpDir() {
        return this.tmpDir;
    }

    @Override
    public String getJdkModulePath() {
        if (!isClient()) {
            return "";
        }
        return jdkModulePath;
    }

    @Override
    public String getJavaFxRuntimeVersion() {
        if (!isClient()) {
            return "";
        }
        return javaFxRuntimeVersion;
    }

    @Override
    public String getJavaFxVersion() {
        if (!isClient()) {
            return "";
        }
        return javaFxVersion;
    }

    @Override
    public String getCpxCriteriaPath() {
        if (!isClient()) {
            return "";
        }
        return getCpxClientCriteriaPath();
    }

    private String getCpxClientCriteriaPath(){
        CpxCustomSystemPropertiesInterface cpxCustomProps = CpxCustomSystemProperties.getInstance();
        String criteriaPath = cpxCustomProps.getCpxCriteriaFilePath(); 
        if(criteriaPath.isEmpty()){
        String dir = "criteria";
        String file = "criteria.xml";
            return toStr(getUserDir() + dir + getFileSeparator() + file);
        }
        return criteriaPath;

    }      

}
