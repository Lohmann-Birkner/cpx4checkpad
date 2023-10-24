/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core;

import de.lb.cpx.client.core.handler.CpxUncaughtExceptionHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 *
 * @author niemeier
 */
public class InitLogger {

    private static InitLogger instance = null;
    private final ObjectProperty<File> logFile = new SimpleObjectProperty<>();
    public static final String CPX_PACKAGE = "de.lb.cpx.client";

    public static synchronized InitLogger instance() {
        if (instance == null) {
            instance = new InitLogger();
        }
        return instance;
    }

    private InitLogger() {
        initialize();
    }

//    public boolean isCoreVersion() {
//        return isCoreVersion;
//    }
    /**
     * don't use logger in this method, overwise in Javas Logger class the
     * property manager is initialized by Java Util Logger (JUL) before we are
     * able to setup LOG4J2 with
     * System.getProperty("java.util.logging.manager").
     */
    private void initialize() {
//        final CpxLoggingPropertiesInterface cpxProps = CpxLoggingProperties.getInstance();
//        final String logfileName = cpxProps.getCpxClientLogFile().replace("{$app}", getType().getTitle());
//        final String logfileName = getLogfileName().replace("{$app}", getType().getTitle());
        final String logfileDir = getLogfileDir();
        final String loggingPropertiesFilename = getLoggingPropertiesFilename();
//        if (pCpxProps == null) {
//            throw new IllegalArgumentException("cpx properties cannot be null!");
//        }
//        if (pLogfileName == null || pLogfileName.trim().isEmpty()) {
//            throw new IllegalArgumentException("cpx properties cannot be null or empty!");
//        }
        File logfileDirTmp = new File(logfileDir);
        if (!logfileDirTmp.exists()) {
            logfileDirTmp.mkdirs();
        }
//        final String loggingPropertiesFilename = cpxProps.getCpxClientLoggingPropertiesFile();
        File loggingPropertiesFile = new File(loggingPropertiesFilename);
        boolean error = false;
        if (!loggingPropertiesFile.exists()) {
            error = true;
            BasicMainApp.showErrorMessageDialog("Logger configuration does not exist: " + loggingPropertiesFile.getAbsolutePath());
        }
        if (!error && !loggingPropertiesFile.isFile()) {
            error = true;
            BasicMainApp.showErrorMessageDialog("Logger configuration is not a file: " + loggingPropertiesFile.getAbsolutePath());
        }
        if (!error && !loggingPropertiesFile.canRead()) {
            error = true;
            BasicMainApp.showErrorMessageDialog("Logger configuration is unreadable: " + loggingPropertiesFile.getAbsolutePath());
        }
        if (!error) {
//            final String logFileName = logfileDir + logfileName;
            System.setProperty("logFileDir", logfileDir);
            System.setProperty("appType", BasicMainApp.getType().getTitle());
            System.setProperty("userName", getUserName());
            System.setProperty("hostName", getHostName());

            System.out.println("Initialize Logger with the folling parameters:\n"
                    + "logFileDir: " + System.getProperty("logFileDir") + ", "
                    + "appType: " + System.getProperty("appType") + ", "
                    + "userName: " + System.getProperty("userName") + ", "
                    + "hostName: " + System.getProperty("hostName"));

            //don't use logger here but System.out.println or System.err.println! This is fully intended!
//            File dir = new File(logfileDir);
            if (!logfileDirTmp.exists()) {
                System.out.println("log file directory does not exist yet!");
                try {
                    Files.createDirectory(logfileDirTmp.toPath());
                    System.out.println("missing log file directory was successfully created!");
                } catch (IOException ex) {
                    System.err.println("was not able to create log file directory: " + ex.getMessage());
                    BasicMainApp.showErrorMessageDialog(ex, "Was not able to create log file directory: " + logfileDirTmp.getAbsolutePath());
                }
            } else {
                if (!logfileDirTmp.canWrite() || !Files.isWritable(logfileDirTmp.toPath())) {
                    System.err.println("no permissions to write to log file directory!");
                    BasicMainApp.showErrorMessageDialog("no permissions to write to log file directory: " + logfileDirTmp.getAbsolutePath());
                }
            }
//            System.setProperty("logFileName", logFileName);
//            LogManager.getLogManager().reset();
//            System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
            //LogManager.getLogManager().reset();
//            Field f;
//            try {
//                f = Logger.class.getDeclaredField("manager");
//                f.setAccessible(true);
//                f.set(null, null);
//            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
//                Logger.getLogger(BasicMainApp.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            
//            pCpxProps.getCpxClientLogFile();
//            String lLogFile = "";
//            System.setProperty("logFileName", logFileName);
//            System.setProperty("app", "bla");
//            System.setProperty("user", pCpxProps.getUserName());
//            System.setProperty("host", pCpxProps.getHostName());
            try ( FileInputStream fis = new FileInputStream(loggingPropertiesFile)) {
                ConfigurationSource source = new ConfigurationSource(fis);
                Configurator.initialize(null, source);
                //RollingFileAppender fileAppender = (RollingFileAppender) context.getConfiguration().getAppender("rollingFile");
                //LogManager.getLogManager().readConfiguration(new FileInputStream(loggingPropertiesFile));
                final CpxUncaughtExceptionHandler exHandler = new CpxUncaughtExceptionHandler();
                Thread.setDefaultUncaughtExceptionHandler(exHandler);
                for (Thread th : BasicMainApp.getThreads()) {
                    th.setUncaughtExceptionHandler(exHandler);
                }
            } catch (IOException | SecurityException ex) {
                Logger.getLogger(BasicMainApp.class.getName()).log(Level.SEVERE, "Was not able to read logging properties", ex);
                BasicMainApp.showErrorMessageDialog(ex, "Was not able to read logger configuration: " + loggingPropertiesFile.getAbsolutePath());
            }

            final RollingFileAppender appender = getRollingFileAppender();
            if (appender != null) {
                logFile.set(new File(appender.getFileName()));
            }
//            final File logfile = getLogFile();
//            LOGFILE_NAME.set(logfile.getName());
//            LOGFILE_PATH.set(logfileDir + LOGFILE_NAME.get());
        }
        final Logger log = Logger.getLogger(InitLogger.class.getName());
        if (logFile.get() != null) {
            log.log(Level.INFO, "New client log file was created here: " + logFile.get());
        }
        //log.set(Logger.getLogger(InitLogger.class.getName()));
        log.log(Level.INFO, "Logging with LOG4J can be configured in this file: " + loggingPropertiesFile);
        //now you can use logger functionality!
        //BasicMainApp.setLogger(LOG);
    }

    private static Map<String, Appender> getAllAppenders() {
        org.apache.logging.log4j.Logger logger = LogManager.getLogger();
        Map<String, Appender> appenderMap = ((org.apache.logging.log4j.core.Logger) logger).getAppenders();
        return appenderMap;
    }

    private static RollingFileAppender getRollingFileAppender() {
        return (RollingFileAppender) getAllAppenders().get("rollingFile");
    }

    public File getLogFile() {
        return logFile.get();
    }

//    private static File getLogFile() {
//        RollingFileAppender appender = getRollingFileAppender();
//        if (appender == null) {
//            return null;
//        }
//        return new File(appender.getFileName());
//    }
    private static String getHostName() {
        String hostName = "";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            //don't call Logger here when you try to catch UnknownHostException!!! Otherweise LOG4j2 cannot be initialized properly
            System.out.println(ex.getMessage());
        }
        return hostName;
    }

    private static String getUserName() {
        final Properties props = System.getProperties();
        final String userName = props.getProperty("user.name");
        return userName;
    }

//    private static String getLogfileName() {
//        final Properties props = System.getProperties();
////        final String fileSeparator = props.getProperty("file.separator");
//        final String userName = props.getProperty("user.name");
//////        final String userDir = props.getProperty("user.dir");
////        String userHome = props.getProperty("user.home");
////
////        if (!userHome.isEmpty() && !userHome.endsWith(fileSeparator)) {
////            userHome += fileSeparator;
////        }
////        final String userHome = getLogfileDir();
//
//        //this.appDataLocal = appData + "Local" + fileSeparator + "CPX" + fileSeparator;
//        String hostName = "";
//        try {
//            hostName = InetAddress.getLocalHost().getHostName();
//        } catch (UnknownHostException ex) {
//            //don't call Logger here when you try to catch UnknownHostException!!! Otherweise LOG4j2 cannot be initialized properly
//            System.out.println(ex.getMessage());
//        }
//
//        //To be exactly: This is only in use on the client
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss,SSS");
//        final String dateTime = formatter.format(new Date());
//        final String lLogFile = "CPX {$app} " + dateTime + " " + userName + " on " + hostName + ".log";
//        return lLogFile;
//    }
    private static String getLogfileDir() {
        final Properties props = System.getProperties();
        String userHome = props.getProperty("user.home");
        final String fileSeparator = props.getProperty("file.separator");
        if (!userHome.isEmpty() && !userHome.endsWith(fileSeparator)) {
            userHome += fileSeparator;
        }
        final String appData = userHome + "AppData" + fileSeparator;
        return appData + "Local" + fileSeparator + "CPX" + fileSeparator + "log" + fileSeparator;
    }

    private static String getLoggingPropertiesFilename() {
        final Properties props = System.getProperties();
        final String fileSeparator = props.getProperty("file.separator");
        String userDir = props.getProperty("user.dir");

        if (!userDir.isEmpty() && !userDir.endsWith(fileSeparator)) {
            userDir += fileSeparator;
        }
        return userDir + "logging.xml";
    }

//    private LoggerConfig getLoggerConfig(final String pPackage) {
//        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
//        Configuration config = ctx.getConfiguration();
//        LoggerConfig loggerConfig = config.getLoggerConfig(pPackage);
//        return loggerConfig;
//    }
    public org.apache.logging.log4j.Level getLogLevel() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(CPX_PACKAGE);
        return loggerConfig.getLevel();
        //return LogManager.getLogger(CPX_PACKAGE).getLevel();
    }

    public void setLogLevel(final org.apache.logging.log4j.Level pLevel) {
        final org.apache.logging.log4j.Level level = pLevel == null ? org.apache.logging.log4j.Level.INFO : pLevel;
        Logger.getLogger(InitLogger.class.getName()).log(Level.INFO, "set log level for package " + CPX_PACKAGE + " to " + level);
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(CPX_PACKAGE);
        loggerConfig.setLevel(level);
        ctx.updateLoggers();
    }

}
