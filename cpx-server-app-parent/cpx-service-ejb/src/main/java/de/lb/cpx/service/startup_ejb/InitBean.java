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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.startup_ejb;

import de.lb.cpx.server.config.CpxServerConfig;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.version.VersionHistory;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.security.AccessControlException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.NamingException;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 *
 * @author Dirk Niemeier
 */
@Singleton
@Startup
@SecurityDomain("cpx")
public class InitBean {

    private static final Logger LOG = Logger.getLogger(InitBean.class.getName());
    private boolean printedCpxInformation = false;

    public InitBean() throws FileNotFoundException, NamingException {
        LOG.log(Level.INFO, "*** INIT BEAN STARTED ***");

        if (!printedCpxInformation) {
            CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
            if (cpxProps != null) {
                LOG.log(Level.INFO, cpxProps.toString()
                        + "\n=========================================================");
            }
            LOG.log(Level.INFO, "CPX Version: " + String.valueOf(VersionHistory.getRecentVersion()));
        }
        printedCpxInformation = true;

        checkPrerequisites();

        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        //String cpxProgramDir = cpxProps.getCpxProgramDir();

        createCpxDir(cpxProps.getCpxProgramDir(), "Program");
        createCpxDir(cpxProps.getCpxServerCatalogDir(), "Catalog");
        createCpxDir(cpxProps.getCpxServerFilterDir(), "Filter");
        createCpxDir(cpxProps.getCpxServerGrouperDir(), "Grouper");
        createCpxDir(cpxProps.getCpxServerRulesDir(), "Rules");
        createCpxDir(cpxProps.getCpxServerReportsDir(), "Reports");
        createCpxDir(cpxProps.getCpxServerLicenseDir(), "License");
        createCpxDir(cpxProps.getCpxServerResourceBundlesDir(), "Resource Bundles");
        createCpxDir(cpxProps.getCpxServerDbUpdateDir(), "Database Updates");
        //createCpxDir(cpxProps.getCpxServerTempDir(), "Temp");
        //createCpxDir(cpxProps.getCpxServerScriptsDir(), "Scripts");
    }

    private boolean createCpxDir(final String pDir, final String pType) {
        LOG.log(Level.INFO, "Create CPX " + pType + " Directory '" + pDir + "'...");
        //Logger.getLogger(AuthServiceEJB.class.getName()).log(Level.INFO, "CPX Program Directory is " + cpxProgramDir);
        if (pDir.isEmpty()) {
            throw new IllegalArgumentException("CPX " + pType + " Directory is empty!");
        }
        File cpxDir = new File(pDir);
        if (cpxDir.setExecutable(false)) {
            LOG.log(Level.FINEST, "Was successfully set to unexecutable: " + cpxDir.getName());
        }
        if (cpxDir.setReadable(true)) {
            LOG.log(Level.FINEST, "Was successfully set to readable: " + cpxDir.getName());
        }
        if (cpxDir.setWritable(true)) {
            LOG.log(Level.FINEST, "Was successfully set to writeable: " + cpxDir.getName());
        }
//        cpxDir.setExecutable(false);
//        cpxDir.setReadable(true);
//        cpxDir.setWritable(true);
        if (!isValidFile(cpxDir)) {
            throw new IllegalArgumentException("CPX " + pType + " Directory is not a valid directory (syntax error)!");
        }
        if (cpxDir.isFile()) {
            throw new IllegalArgumentException("CPX " + pType + " Directory is a file and not a directory!");
        }
        if (!cpxDir.exists()) {
            LOG.log(Level.INFO, "Will create CPX " + pType + " Directory '" + cpxDir.getAbsolutePath() + "' now...");
            if (!cpxDir.mkdirs()) {
                throw new AccessControlException("Cannot create CPX " + pType + " Directory!");
            }
        }

        if (!isReadable(cpxDir)) {
            throw new AccessControlException("No read permissions for CPX " + pType + " Directory!");
        }

        if (!isWriteable(cpxDir)) {
            throw new AccessControlException("No write permissions for CPX " + pType + " Directory!");
        }

        return true;
    }

    private boolean isValidFile(final File pFile) {
        if (pFile == null) {
            return true;
        }
        try {
            pFile.getCanonicalPath();
        } catch (IOException ex) {
            LOG.log(Level.INFO, "File is invalid.", ex);
            return false;
        }
        return true;
    }

    private boolean isWriteable(final File pFile) {
        if (pFile == null) {
            return true;
        }
        return pFile.canWrite() && Files.isWritable(pFile.toPath());
    }

    private boolean isReadable(final File pFile) {
        if (pFile == null) {
            return true;
        }
        return pFile.canRead() && Files.isReadable(pFile.toPath());
    }

    @PreDestroy
    public void destroy() {
        LOG.log(Level.INFO, "*** INIT BEAN FINISHED ***");
    }

    private void checkPrerequisites() throws FileNotFoundException {
        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();

        checkServerConfigFile(cpxProps.getCpxServerConfigFile());

        //we are alone here (exclusive access, yeah!). Now... let's force initialization of xml config
        LOG.log(Level.INFO, "Initialize CPX server configuration...");
        CpxServerConfig cpxServerConfig = new CpxServerConfig();
        cpxServerConfig.getRulesDatabaseConfig(); //just for initialization, could be any other method!
        Lang.setStaticLocale(cpxServerConfig.getLanguage());
        LOG.log(Level.INFO, "CPX server configuration initialized!");
    }

    private void checkServerConfigFile(final String serverConfigFileName) {
        LOG.log(Level.INFO, "Check CPX Server Config File '" + serverConfigFileName + "'...");
        if (serverConfigFileName.isEmpty()) {
            throw new IllegalArgumentException("CPX Server Config is empty!");
        }
        File serverConfigFile = new File(serverConfigFileName);
        if (!isValidFile(serverConfigFile)) {
            throw new IllegalArgumentException("CPX Server Config is not a valid file name (syntax error)!");
        }
        if (!serverConfigFile.exists()) {
            throw new IllegalArgumentException("CPX Server Config does not exist!");
        }
        if (serverConfigFile.isDirectory()) {
            throw new IllegalArgumentException("CPX Server Config is a directory and not a file!");
        }
        if (!serverConfigFile.canRead()) {
            throw new AccessControlException("No read permissions for CPX Server Config!");
        }
        if (!serverConfigFile.canWrite()) {
            throw new AccessControlException("No write permissions for CPX Server Config!");
        }
    }

}
