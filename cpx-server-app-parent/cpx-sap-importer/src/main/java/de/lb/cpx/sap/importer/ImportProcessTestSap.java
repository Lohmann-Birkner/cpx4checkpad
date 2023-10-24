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
package de.lb.cpx.sap.importer;

import com.sap.conn.jco.JCoException;
import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.sap.container.FallContainer;
import de.lb.cpx.server.config.CpxServerConfig;
import de.lb.cpx.service.information.ConnectionString;
import de.lb.cpx.shared.dto.job.config.CpxJobConstraints;
import de.lb.cpx.shared.dto.job.config.other.SapJob;
import static de.lb.cpx.str.utils.StrUtils.getArg;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import module.impl.ImportConfig;

/**
 *
 * @author niemeier
 */
public class ImportProcessTestSap {

    private static final Logger LOG = Logger.getLogger(ImportProcessTestSap.class.getName());


    private enum testModeEn {doImport, doExport, doAsConfig};

    /**
     * Run SAP Test Import
     *
     * @param args Some arguments (e.g. connection parameters)
     * @throws JCoException Exception
     * @throws Throwable Throwable
     */
    public static void main(String[] args) throws JCoException, Throwable {
        CpxSystemProperties.useUseDirAsCpxHome(true);
        LOG.log(Level.INFO, "SAP-Import runs in this directory: {0}", CpxSystemProperties.getInstance().getCpxHome());

        final String sapConfigName = getArg(args, 0); // (args != null && args.length >= 1) ? args[0].trim() : "";
//        final String institution = (args != null && args.length >= 2) ? args[1].trim() : "";
//        final String hosIdent = (args != null && args.length >= 2) ? args[1].trim() : "";
        final String caseDb = getArg(args, 1); //(args != null && args.length >= 2) ? args[1].trim() : "";
//        final String caseNumbers = getArg(args, 2); //(args != null && args.length >= 3) ? args[2].trim() : "";

        if (sapConfigName.isEmpty()) {
            throw new IllegalArgumentException("No SAP Config name passed (arg 1)!");
        }
//        if (institution.isEmpty()) {
//            throw new IllegalArgumentException("No institution passed (arg 2)!");
//        }
//        if (hosIdent.isEmpty()) {
//            throw new IllegalArgumentException("No hospital identifier (IKZ) passed (arg 2)!");
//        }
        if (caseDb.isEmpty()) {
            throw new IllegalArgumentException("No case database (e.g. dbsys1:CPX) passed (arg 1)!");
        }
        ConnectionString connStr = new ConnectionString(caseDb);
        if (!connStr.isValidCaseDb()) {
            throw new IllegalArgumentException("No valid case database passed (e.g. dbsys1:CPX) passed (arg 1): " + caseDb);
        }
        LOG.log(Level.INFO, "Will import to database: {0}", caseDb);
        // test mode
        
        String testModeStr = getArg(args, 2);
        testModeEn testMode = testModeEn.doAsConfig;
        if(!testModeStr.isEmpty()){
            if(testModeStr.equalsIgnoreCase(testModeEn.doImport.name())){
                testMode =testModeEn.doImport;
            }else if(testModeStr.equalsIgnoreCase(testModeEn.doExport.name())){
                testMode = testModeEn.doExport;
            }
        }
        LOG.log(Level.INFO, "test mode: {0}", testMode.name());

        String jsonPath = getArg(args, 3);

        LOG.log(Level.INFO, "jsonPath: {0}", jsonPath);
        if((jsonPath == null || jsonPath.isEmpty()) && (testMode.equals(testModeEn.doExport) || testMode.equals(testModeEn.doImport)) ){
            throw new IllegalArgumentException("No valid json path passed (arg 3): " + jsonPath + " for testMode: " + testMode.name());
        }

        String caseNumberPath = getArg(args, 4);
        LOG.log(Level.INFO, "caseNumberPath: {0}", caseNumberPath);
        if((caseNumberPath == null || caseNumberPath.isEmpty()) && (testMode.equals(testModeEn.doExport) || testMode.equals(testModeEn.doImport)) ){
            LOG.log(Level.INFO, "there are no case numbers for import/export, we use constraints from server config ");
        }else{
            
        }

        String doWards = getArg(args, 5);
        
        final CpxServerConfig serverConfig = new CpxServerConfig();
        final SapJob sapConfig = serverConfig.getSapConfig(sapConfigName);
        final String identifier = serverConfig.getDbIdentifier(caseDb);

        if (sapConfig == null) {
            throw new IllegalArgumentException("No SAP Config found in cpx_server_config.xml with the name '" + sapConfigName + "'");
        }
        CpxJobConstraints constraints = sapConfig.getConstraints();

        if(testMode ==testModeEn.doExport && caseNumberPath != null && !caseNumberPath.isEmpty()){
            // get case numbers from file on caseNumberPath
            String caseNumbers = getCaseNumbersFromPath(caseNumberPath);
            if(caseNumbers.length() > 0){
                constraints = new CpxJobConstraints(caseNumbers);
            }
        }
        
        SapJob sapConfig1 = new SapJob(
                sapConfig.getName(),
                sapConfig.getTargetDatabase(),
                sapConfig.getImportMode(),
                sapConfig.getServer(),
                sapConfig.getPort(),
                sapConfig.getUser(),
                sapConfig.getPassword(),
                caseDb,
                sapConfig.getSysNr(),
                sapConfig.getMandant(),
                sapConfig.getInstitution(),
                sapConfig.isRebuildIndexes(),
                sapConfig.getGrouperModel(),
                constraints,
                sapConfig.getTimePeriodValue(),
                sapConfig.getTimePeriodUnit(),
                sapConfig.getBeginDate(),
                sapConfig.getEndDate(),
                sapConfig.isActive(),
                doWards.isEmpty() || doWards.equalsIgnoreCase("true"),
                testMode ==testModeEn.doImport,
                testMode ==testModeEn.doExport,
                testMode ==testModeEn.doExport,
                jsonPath,
                "local"
);
        final Date changeDate = serverConfig.getSapChangeDate(caseDb);
        
        final License license = serverConfig.readLicense();

        final ImportProcessSap sapImportProcess = new ImportProcessSap();
        final FallContainer fallContainer = new FallContainer();
//        if(testMode ==testModeEn.doImport){
//            sapConfig.setUseJsonDump(true);
//            sapConfig.setWriteJsonDump(false);
////            sapConfig.setmJsonPath(jsonPath);
//        }else if(testMode ==testModeEn.doExport){
//            sapConfig.setWriteJsonDump(true);
//            sapConfig.setUseJsonDump(false);
//            sapConfig.setDoAnonymize(true);
//            sapConfig.setImportWard(true);
////            sapConfig.setmJsonPath(jsonPath);
//        }
        
//        final CpxJobConstraints importConstraint = new CpxJobConstraints(
//                admissionDateFrom, admissionDateTo,
//                dischargeDateFrom, dischargeDateTo,
//                admissionCauses, admissionReasons,
//                icdTypes, caseNumbers);
        final Sap module = new Sap(sapConfig1, fallContainer);

//        module.getInputConfig().setImportMode(importMode);
//        module.getInputConfig().setConstraints(importConstraint);
//        module.getInputConfig().setTargetDatabase(caseDb); //identifier
        final ImportConfig<Sap> importConfig = new ImportConfig<>(identifier, module, license);
        FallContainer container = sapImportProcess.doSapImport(importConfig, /* hosIdent, */ changeDate, false);
        //*** NOW LEAN BACK AND OBSERVE ***//
        if (testMode ==testModeEn.doImport) {
            final Sap module1 = new Sap(sapConfig1, container);
            final ImportConfig<Sap> importConfig1 = new ImportConfig<>(identifier, module1, license);
        
            ImportProcessSap process = new ImportProcessSap();
            process.start(importConfig1, caseDb);
       }
    }

      private static String getCaseNumbersFromPath(String caseNumberPath) {
        File file = new File(caseNumberPath);
        if(file != null && file.exists() && file.isFile() && file.canRead()){
         StringBuilder contentBuilder = new StringBuilder();
 
            try (Stream<String> stream = Files.lines( Paths.get(caseNumberPath), StandardCharsets.UTF_8)) 
            {
                stream.forEach(s -> contentBuilder.append(s).append(","));
            }
            catch (IOException e) 
            {
                throw new IllegalArgumentException("Cannot read list of case numbers on path '" + caseNumberPath + "'");
            }

            return contentBuilder.toString();           
        }else{
            throw new IllegalArgumentException("Cannot read list of case numbers on path '" + caseNumberPath + "'");
        }
        
    }
}
