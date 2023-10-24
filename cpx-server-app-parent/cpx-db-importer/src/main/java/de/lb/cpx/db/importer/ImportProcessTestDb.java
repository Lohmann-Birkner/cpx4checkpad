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
package de.lb.cpx.db.importer;

import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.server.config.CpxServerConfig;
import de.lb.cpx.shared.dto.job.config.CpxDatabaseBasedImportJob;
import de.lb.cpx.shared.dto.job.config.database.OrbisJob;
import static de.lb.cpx.str.utils.StrUtils.getArg;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.AbstractImportDbModule;
import module.Kissmed;
import module.Medico;
import module.Nexus;
import module.Orbis;
import module.impl.ImportConfig;

/**
 *
 * @author niemeier
 */
public class ImportProcessTestDb {

    public static final Logger LOG = Logger.getLogger(ImportProcessTestDb.class.getName());

    /**
     * Run DB Test Import
     *
     * @param args Some arguments (e.g. connection parameters)
     * @throws Throwable Throwable
     */
    public static void main(String args[]) throws Throwable {
        CpxSystemProperties.useUseDirAsCpxHome(true);
        LOG.log(Level.INFO, "DB-Import runs in this directory: " + CpxSystemProperties.getInstance().getCpxHome());

        final String dbConfigName = getArg(args, 0); //(args != null && args.length >= 1) ? args[0].trim() : "";
        final String moduleName = getArg(args, 1); //(args != null && args.length >= 2) ? args[1].trim() : "";
//        final String institution = (args != null && args.length >= 2) ? args[1].trim() : "";
//        final String hosIdent = (args != null && args.length >= 2) ? args[1].trim() : "";
//        final String caseDb = getArg(args, 2); //(args != null && args.length >= 3) ? args[2].trim() : "";
//        final String caseNumbers = (args != null && args.length >= 4) ? args[3].trim() : "";

        if (dbConfigName.isEmpty()) {
            throw new IllegalArgumentException("No DB Config name passed (arg 1)!");
        }
        if (moduleName.isEmpty()) {
            throw new IllegalArgumentException("No module name passed (arg 2)!");
        }
//        if (caseDb.isEmpty()) {
//            throw new IllegalArgumentException("No case database (e.g. dbsys1:CPX) passed (arg 3)!");
//        }
//        ConnectionString connStr = new ConnectionString(caseDb);
//        if (!connStr.isValidCaseDb()) {
//            throw new IllegalArgumentException("No valid case database passed (e.g. dbsys1:CPX) passed (arg 3): " + caseDb);
//        }
//        LOG.info("Will import to database: " + caseDb);

        final CpxServerConfig serverConfig = new CpxServerConfig();
               
        final License license = serverConfig.readLicense();


//        OrbisJob eif = new OrbisJob();
//        eif.setName(dbConfigName);
//        eif.setDriver(DbDriverEn.ORACLE);
//        serverConfig.saveExternalInterfaceConfig(eif);
//        eif = serverConfig.getOrbisConfig(dbConfigName);
        final AbstractImportDbModule<? extends CpxDatabaseBasedImportJob> module;
        
        switch (moduleName.toLowerCase()) {
            case "orbis":
                module = new Orbis(getOrbisConfig(dbConfigName));
                break;
            case "medico":
                module = new Medico(serverConfig.getMedicoConfig(dbConfigName));
                break;
            case "kissmed":
                module = new Kissmed(serverConfig.getKissmedConfig(dbConfigName));
                break;
            case "nexus":
                module = new Nexus(serverConfig.getNexusConfig(dbConfigName));
                break;
            default:
                throw new IllegalArgumentException("module '" + moduleName + "' is unknown (arg 2)!");
        }

        final String caseDb = module.getInputConfig().getTargetDatabase();
        final String identifier = serverConfig.getDbIdentifier(caseDb);
        LOG.info("Will import to database: " + caseDb + " (" + identifier + ")");

//        if (module == null) {
//            throw new IllegalArgumentException("No module found in cpx_server_config.xml with the name '" + moduleName + "' and config name '" + dbConfigName + "'");
//        }
//        final ImportMode importMode = ImportMode.Version; //CUSTOMIZE THIS! (use 'version',  'overwrite' or 'truncate')
        //try (final Connection connection = module.getInputConfig().createConnection()) {
 
//        module.getInputConfig().setImportMode(importMode);
        final ImportConfig<AbstractImportDbModule<? extends CpxDatabaseBasedImportJob>> importConfig = new ImportConfig<>(identifier, module, license);

        LOG.log(Level.INFO, "Will import " + module.getName());
        LOG.log(Level.INFO, "Will use this import mode: " + module.getInputConfig().getImportMode().modeName);

        //*** NOW LEAN BACK AND OBSERVE ***//
        ImportProcessDb<AbstractImportDbModule<? extends CpxDatabaseBasedImportJob>> process = new ImportProcessDb<>();
        process.start(importConfig, caseDb);
        //}
//                Orbis module = new Orbis((OrbisJob) databaseImportConfig);
//                ImportProcessDb<Orbis> process = new ImportProcessDb<>();
//                ImportConfig<Orbis> importConfig = new ImportConfig<>(pIdentifier, module, pLicense);
//                process.start(importConfig, pCommonDbConnection, pCaseDbConnection);


    }
    
    private  static OrbisJob getOrbisConfig(final String pName) {
        final CpxServerConfig serverConfig = new CpxServerConfig();

        return serverConfig.getOrbisConfig(pName);
    }

}
