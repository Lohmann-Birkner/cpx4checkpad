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
import java.io.Serializable;
import java.util.Properties;

/**
 *
 * @author Dirk Niemeier
 */
public class CpxCustomSystemProperties implements CpxCustomSystemPropertiesInterface, Serializable {

    private static final long serialVersionUID = 1L;
    //private static final Logger LOG = Logger.getLogger(CpxCustomSystemProperties.class.getName());

    public final String cpxUsername;
    public final String cpxPassword;
    public final String cpxDatabase;
    public final String cpxConfigFile;
    public final boolean cpxSkipUpdate;
    private final String cpxCatalogDir;

    protected CpxCustomSystemProperties(
            final String cpxUsername,
            final String cpxPassword,
            final String cpxDatabase,
            final String cpxConfigFile,
            final String cpxSkipUpdate,
            final String cpxCatalogDir
    ) {
        /*
    String configFile = toStr(cpxConfigFile);
    if (!configFile.isEmpty() && !configFile.toLowerCase().endsWith(".xml")) {
      configFile += ".xml";
    }
         */

        this.cpxUsername = toStr(cpxUsername);
        this.cpxPassword = toStr(cpxPassword);
        this.cpxDatabase = toStr(cpxDatabase);
        this.cpxConfigFile = toStr(cpxConfigFile);
        this.cpxSkipUpdate = toBool(cpxSkipUpdate);

        this.cpxCatalogDir = toStr(cpxCatalogDir) ;//+ (toStr(cpxCatalogDir).isEmpty()?"":File.separator + cpxUsername + File.separator );
        
    }

    public static synchronized CpxCustomSystemPropertiesInterface getInstance() {
        Properties props = System.getProperties();
        String catalogDir = toStr(props.getProperty("cpx_catalog_dir"));
        if(!catalogDir.isEmpty()){
            catalogDir += File.separator + toStr(props.getProperty("user.name")) + File.separator;
        }
        return new CpxCustomSystemProperties(
                props.getProperty("cpx_username"), //CPX Username
                props.getProperty("cpx_password"), //CPX Password
                props.getProperty("cpx_database"), //CPX Database
                props.getProperty("cpx_config_file"), //CPX Database
                props.getProperty("cpx_skip_update"), //CPX Skip Update
                catalogDir//props.getProperty("cpx_catalog_dir") //CPX Catalog Directory
//                props.getProperty("user.dir")
        );
    }

    private static String toStr(String pValue) {
        if (pValue == null) {
            return "";
        }
        return pValue.trim();
    }

    private static boolean toBool(final String pValue) {
        String value = toStr(pValue);
        if (value.isEmpty()) {
            return false;
        }
        value = value.toLowerCase();
        if (value.equalsIgnoreCase("1")) {
            return true;
        }
        if (value.equalsIgnoreCase("t")) {
            return true;
        }
        if (value.equalsIgnoreCase("true")) {
            return true;
        }
        if (value.equalsIgnoreCase("on")) {
            return true;
        }
        if (value.equalsIgnoreCase("enabled")) {
            return true;
        }
        if (value.equalsIgnoreCase("enable")) {
            return true;
        }
        return false;
    }

    @Override
    public String getCpxUsername() {
        return this.cpxUsername;
    }

    @Override
    public String getCpxPassword() {
        return this.cpxPassword;
    }

    @Override
    public String getCpxDatabase() {
        return this.cpxDatabase;
    }

    @Override
    public String getCpxConfigFile() {
        return this.cpxConfigFile;
    }

    @Override
    public boolean getCpxSkipUpdate() {
        return this.cpxSkipUpdate;
    }

    @Override
    public String getCpxCatalogDir() {
        return this.cpxCatalogDir;
    }

}
