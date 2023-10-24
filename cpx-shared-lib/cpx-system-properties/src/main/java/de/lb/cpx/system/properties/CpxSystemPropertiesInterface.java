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

import java.io.Serializable;

/**
 *
 * @author Dirk Niemeier
 */
public interface CpxSystemPropertiesInterface extends Serializable {

    static final String REQUIRED_JAVA_VERSION = "1.8.0_102";

    /**
     * Gives you the answer if the code is currently running on a (Wildfly)
     * Server
     *
     * @return running on server?
     */
    boolean isServer();

    /**
     * Gives you the answer if the code is currently running on a Client
     *
     * @return running on client?
     */
    boolean isClient();

    /**
     * Gives you the answer if the code is currently running in a JUnit Test
     *
     * @return running test?
     */
    boolean isTest();

    /**
     * Gives you the answer if the code is currently running in Debug mode
     *
     * @return running debug mode?
     */
    boolean isDebug();

    /**
     * file.separator Character that separates components of a file path. This
     * is "/" on UNIX and "\" on Windows.
     *
     * @return file separator
     */
    String getFileSeparator();

    /**
     * java.class.path Path used to find directories and JAR archives containing
     * class files. Elements of the class path are separated by a
     * platform-specific character specified in the path.separator property.
     *
     * @return java class path
     */
    String getJavaClassPath();

    /**
     * java.home Installation directory for Java Runtime Environment (JRE) (e.g.
     * E:\Labor\Java\jdk1.8.0_152\jre)
     *
     * @return java home
     */
    String getJavaHome();

    /**
     * java.vendor JRE vendor name (e.g. Oracle Corporation)
     *
     * @return java vendor
     */
    String getJavaVendor();

    /**
     * java.vendor.url JRE vendor URL (e.g. http://java.oracle.com/)
     *
     * @return java vendor url
     */
    String getJavaVendorUrl();

    /**
     * java.version JRE version number (e.g. 1.8.0_152)
     *
     * @return java version
     */
    String getJavaVersion();

    /**
     * java.vm.name JRE vm name (e.g. OpenJDK 64-Bit Server VM)
     *
     * @return java vm name
     */
    String getJavaVmName();

    /**
     * line.separator Sequence used by operating system to separate lines in
     * (e.g. \r\n) text files
     *
     * @return line separator
     */
    String getLineSeparator();

    /**
     * os.arch Operating system architecture (e.g. amd64)
     *
     * @return os architecture
     */
    String getOsArch();

    /**
     * os.name Operating system name (e.g. Windows 10)
     *
     * @return os name
     */
    String getOsName();

    /**
     * os.version Operating system version (e.g. 10.0)
     *
     * @return os version
     */
    String getOsVersion();

    /**
     * path.separator Path separator character used in java.class.path (e.g. ;)
     *
     * @return path separator
     */
    String getPathSeparator();

    /**
     * user.dir User working directory (e.g. ..\WD_CPX_Client\)
     *
     * @return user dir
     */
    String getUserDir();

    /**
     * user.home User home directory (e.g. C:\Users\niemeier)
     *
     * @return user home
     */
    String getUserHome();

    /**
     * user.name User account name (e.g. niemeier)
     *
     * @return user name
     */
    String getUserName();

    /**
     * CPX_HOME CPX Home on server (e.g. ..\WD_CPX_Server\)
     *
     * @return cpx home
     */
    String getCpxHome();

    /**
     * ProgramFiles Program Files (e.g. C:\Program Files)
     *
     * @return program files
     */
    String getProgramFiles();

    /**
     * %programfiles% (x86) //Program Files 32-bit folder on 64-bit PCs (e.g.
     * C:\Program Files (x86))
     *
     * @return program files (32-bit folder on 64-bit PCs)
     */
    String getProgramFilesX86();

    /**
     * Program dir. Is identical to working directory in client and identical to
     * CPX HOME on server
     *
     * @return program dir
     */
    String getCpxProgramDir();

    /**
     * directory of catalogs on server (e.g. ..\WD_CPX_Server\catalog\)
     *
     * @return catalog dir
     */
    String getCpxServerCatalogDir();

    /**
     * directory of filters (working list, workflow list, rule list) (e.g.
     * ..\WD_CPX_Server\filter\)
     *
     * @return filter dir
     */
    String getCpxServerFilterDir();

    /**
     * directory of grouper xmls (e.g. ..\WD_CPX_Server\grouper\)
     *
     * @return Grouper directory
     */
    String getCpxServerGrouperDir();

    /**
     * directory of rule pools
     *
     * @return Rules directory
     */
    String getCpxServerRulesDir();

    /**
     * core version number of Wildfly (e.g. 2.0.10.Final (Kenny))
     *
     * @return Wildfly core version
     */
    String getWildflyCoreVersion();

    /**
     * is running under CPX Ear file?
     *
     * @return cpx-ear.ear?
     */
    boolean getCpxEarFile();

    /**
     * is running under CPX Web App file?
     *
     * @return cpx-web-app.war?
     */
    boolean getCpxWebAppFile();

    /**
     * directory of license files on server (e.g. ..\WD_CPX_Server\license\)
     *
     * @return license directory
     */
    String getCpxServerLicenseDir();

//    /**
//     * directory of scripts now for tests (e.g. ..\WD_CPX_Server\scripts\)
//     *
//     * @return scripts directory
//     */
//    String getCpxServerScriptsDir();
    /**
     * Directory for locale on server (e.g. ..\WD_CPX_Server\locale\)
     *
     * @return locale directory
     */
    String getCpxServerLocaleDir();

    /**
     * Directory for reports on server (e.g. ..\WD_CPX_Server\reports\);
     *
     * @return reports directory
     */
    String getCpxServerReportsDir();

    /**
     * File for ACG mapping data on server (e.g.
     * ..\WD_CPX_Server\catalog\steuerung_acg.mdb);
     *
     * @return acg mapping file (MS Access DB)
     */
    String getCpxServerCatalogAcgFile();

    /**
     * File for ACG output data on server (e.g.
     * ..\WD_CPX_Server\catalog\acg_output.csv);
     *
     * @return acg output data (samples for specific patients)
     */
    String getCpxServerAcgOutputFile();

    /**
     * Directory for resource bundles on server (e.g.
     * ..\WD_CPX_Server\resourceBundle\)
     *
     * @return Resource Bundles Directory
     */
    String getCpxServerResourceBundlesDir();

    /**
     * default Language from user.language, 2 symbols (e.g. de)
     *
     * @return User language
     */
    String getUserLanguage();

    /**
     * user.region - 2 symbols
     *
     * @return User region
     */
    String getUserRegion();

    /**
     * ORACLE_HOME Oracle Home (e.g. E:\Labor\app\product\12.1.0\dbhome_2)
     *
     * @return oracle home
     */
    String getOracleHome();

    /**
     * Directory of server config file (e.g.
     * ..\WD_CPX_Server\cpx_server_config.xml)
     *
     * @return config file
     */
    String getCpxServerConfigFile();

    /**
     * Config file on client (e.g. ..\WD_CPX_Client\cpx_client_config.xml)
     *
     * @return config file
     */
    String getCpxClientConfigFile();
//    String getCpxClientHelpDir();

    /**
     * PDF.js directory on client (e.g. ..\WD_CPX_Client\pdfjs)
     *
     * @return PDF.js folder
     */
    String getCpxClientPdfJsDir();

    /**
     * PDF.js viewer file on client (e.g.
     * ..\WD_CPX_Client\pdfjs\web\viewer.html)
     *
     * @return PDF.js viewer
     */
    String getCpxClientPdfJsViewerFile();

    /**
     * User config file on client (e.g.
     * ..\WD_CPX_Client\cpx_client_user_config.xml)
     *
     * @return user config file
     */
    String getCpxClientUserConfigFile();

    /**
     * SQLite database directory on client (e.g. ..\WD_CPX_Client\databases\)
     *
     * @return database directory
     */
    String getCpxClientCatalogDir();

//    /**
//     * Output directory for log files (e.g. ..\WD_CPX_Client\log\)
//     *
//     * @return log directory
//     */
//    String getCpxClientLogDir();
//
//    /**
//     * Output directory for log files (e.g. "CPX 2018-04-30 12-56-22334 niemeier
//     * on LB323 (002).log")
//     *
//     * @return log file
//     */
//    String getCpxClientLogFile();
//
//    /**
//     * Java Util Logger (e.g. ..\WD_CPX_Client\log\logging.properties)
//     *
//     * @return logger properties
//     */
//    String getCpxClientLoggingPropertiesFile();
    /**
     * Output directory for log files (e.g. C:\Labor\Wildfly_10\standalone\log\)
     *
     * @return log directory
     */
    String getCpxServerLogDir();

    /**
     * Output directory for log files (e.g. server.log)
     *
     * @return log file
     */
    String getCpxServerLogFile();

    /**
     * Java Util Logger (e.g.
     * C:\Labor\Wildfly_10\standalone\configuration/logging.properties)
     *
     * @return logger properties
     */
    String getCpxServerLoggingPropertiesFile();

    /**
     * Port for Wildfly Management Interface (e.g. 9995)
     *
     * @return wildfly management port
     */
    int getWildflyManagementPort();

    /**
     * Port for Wildfly HTTP Interface (e.g. 8085)
     *
     * @return wildfly http port
     */
    int getWildflyHttpPort();

    /**
     * Browser URL for Wildfly Management Interface (e.g.
     * http://10.50.10.136:9995)
     *
     * @return url to wildfly management interface
     */
    String getWildflyManagementUrl();

    /**
     * Browser URL to CPX Web App (e.g. http://10.50.10.136:9995/cpx-web-app/)
     *
     * @return url to WebApp
     */
    String getWebAppUrl();

    /**
     * Font Awesome directory on client (e.g. ..\WD_CPX_CLIENT\font\)
     *
     * @return font directory
     */
    String getCpxClientFontDir();

    /**
     * Fone Awesome path on client (e.g. ..\WD_CPX_CLIENT\font\font.ttf)
     *
     * @return font file
     */
    String getCpxClientFontFile();

    /**
     * Library directory on client (e.g. ..\WD_CPX_Client\dll\)
     *
     * @return library directory
     */
    String getCpxClientLibaryDir();

    /**
     * Apache lucence directory for easy coder on client (e.g.
     * ..\WD_CPX_Client\dictionaries\)
     *
     * @return easy coder dictionary directory
     */
    String getCpxClientDictionariesDir();

    /**
     * Apache lucence directory for easy coder ICDs on client (e.g.
     * ..\WD_CPX_Client\dictionaries\)
     *
     * @return easy coder dictionary ICDs directory
     */
    String getCpxClientDictionariesIcdDir();

    /**
     * Apache lucence directory for easy coder OPSes on client (e.g.
     * ..\WD_CPX_Client\dictionaries\)
     *
     * @return easy coder dictionary OPSes directory
     */
    String getCpxClientDictionariesOpsDir();

    /**
     * Directory of update scripts on server (e.g. ..\WD_CPX_Server\db_update\);
     *
     * @return update directory
     */
    String getCpxServerDbUpdateDir();

    /**
     * Directory for library files on server
     *
     * @return library directory
     */
    String getCpxServerLibraryDir();

    /**
     * Path of update script for case databases (e.g.
     * ..\WD_CPX_Server\db_update\\updateCommonDB.txt)
     *
     * @return update files for case db
     */
    String getCpxServerDbUpdateCaseDbFile();

    /**
     * Path of view scripts for case databases (e.g.
     * ..\WD_CPX_Server\db_update\views\)
     *
     * @return view folder for case db
     */
    String getCpxServerDbUpdateCaseDbViewsFile();

    /**
     * Path of update script for common databases (e.g.
     * ..\WD_CPX_Server\db_update\\updateDB.txt)
     *
     * @return update files for common db
     */
    String getCpxServerDbUpdateCommonDbFile();

//    /**
//     * Temporary directory on server (e.g. ..\WD_CPX_Server\temp\)
//     *
//     * @return temporary directory
//     */
//    String getCpxServerTempDir();
    /**
     * Path to store documents on server (e.g. ..\WD_CPX_Server\documents\)
     *
     * @return documents directory
     */
    String getCpxServerDocumentsDir();

    /**
     * Path to store documents on server (e.g. ..\WD_CPX_Server\documents\)
     *
     * @param pDatabaseName database name
     * @return documents directory
     */
    String getCpxServerDocumentsDir(final String pDatabaseName);

    /**
     * Path to store documents on server (e.g. )
     *
     * @param pDatabaseName database name
     * @return path to store documents
     */
    String getCpxPathToStore(final String pDatabaseName);

    /**
     * Path to store documents on server (e.g. )
     *
     * @param pDatabaseName database name
     * @param pFileName file name
     * @return path to store documents
     */
    String getCpxPathToStore(final String pDatabaseName, final String pFileName);

    //public String getCpxStylesheetFile();
    //public String getRedisPath();
    //public String getCpxClientTeamviewerPath();
    /**
     * Network name of your computer (e.g. LB323)
     *
     * @return Computer's name
     */
    String getHostName();

    /**
     * Primary IP Address of your network adapters (e.g. 10.50.10.136)
     *
     * @return IP address
     */
    String getHostIp();

    /**
     * Default Encoding in the running JVM (normally it is Cp1252, that means
     * ANSI)
     *
     * @return default encoding (e.g. Cp1252)
     */
    String getDefaultEncoding();

    /**
     * Deployment directory of wildfly (e.g. C:\Labor\Wildfly_10\standalone\)
     *
     * @return Wildfly deployment directory
     */
    String getDeploymentDir();

//    /**
//     * Total physical memory in byte
//     * @return installed physical memory in megabyte
//     */
//    long getTotalPhysicalMemorySize();
    /**
     * Maximum heap memory size in byte
     *
     * @return max available heap memory in megabyte
     */
    long getMaxHeapMemorySize();

    /**
     * Available Processors (CPU Cores)
     *
     * @return available processors
     */
    int getAvailableProcessors();

    /**
     * Does this software run with the minimum Java Version
     * (REQUIRED_JAVA_VERSION)
     *
     * @return required java version available?
     */
    boolean isRequiredJavaVersion();

    /**
     * Are assertions evaluated?
     *
     * @return assertions enabled?
     */
    boolean isAssertionsEnabled();

    /**
     * List of arguments that were passed to JVM with the start of this
     * application
     *
     * @return JVM arguments
     */
    String[] getJvmArguments();

    /**
     * Returns prism.order argument
     *
     * @return prism.order value
     */
    String getPrismOrder();

    /**
     * Is software acceleration activated?
     *
     * @return prism.order=sw?
     */
    boolean isPrismOrderSw();

    /**
     * APPDATA
     *
     * @return the appData
     */
    String getAppData();

    /**
     * APPDATA\Local
     *
     * @return the appDataLocal
     */
    String getAppDataLocal();

    /**
     * APPDATA\Roaming
     *
     * @return the appDataRoaming
     */
    String getAppDataRoaming();

    /**
     * directory for temporary files
     *
     * @return temporary directory
     */
    String getTmpDir();

    /**
     * directory on server for help files (e.g. ..\WD_CPX_Server\help\)
     *
     * @return server directory for help
     */
    String getCpxServerHelpDir();

    /**
     * directory on server for help document (e.g.
     * ..\WD_CPX_Server\help\hilfefunktion_cpx.pdf)
     *
     * @return help file on server
     */
    String getCpxServerHelpFile();

    /**
     * directory where JavaFX libraries are installed
     *
     * @return JavaFX module path
     */
    String getJdkModulePath();

    /**
     * JavaFX runtime version
     *
     * @return JavaFX runtime version
     */
    String getJavaFxRuntimeVersion();

    /**
     * JavaFX version
     *
     * @return JavaFX version
     */
    String getJavaFxVersion();

}
