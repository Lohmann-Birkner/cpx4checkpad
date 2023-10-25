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

/**
 *
 * @author Dirk Niemeier
 */
public interface CpxCustomSystemPropertiesInterface {

    /**
     * cpx_username CPX Username
     *
     * @return user name
     */
    String getCpxUsername();

    /**
     * cpx_password CPX Password
     *
     * @return password
     */
    String getCpxPassword();

    /**
     * cpx_database CPX Database
     *
     * @return database
     */
    String getCpxDatabase();

    /**
     * cpx_config_file CPX Config File (to switch between different
     * cpx_server_config.xml/cpx_client_config.xml files)
     *
     * @return config file
     */
    String getCpxConfigFile();

    /**
     * cpx_skip_update CPX Skip Update (skips update of catalog and core data
     * after login)
     *
     * @return skip update?
     */
    boolean getCpxSkipUpdate();

    /**
     * cpx_catalog_dir CPX Catalog Directory (if you don't want to store SQLite
     * catalogs in userdir\databases)
     *
     * @return catalog directory
     */
    String getCpxCatalogDir();

    String getCpxCriteriaFilePath();
}
