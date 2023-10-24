/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  niemeier - initial API and implementation and/or initial documentation
 */
package process.impl;

import de.lb.cpx.cpx.license.crypter.License;
import de.lb.cpx.shared.dto.job.config.ImportMode;
import de.lb.cpx.shared.dto.job.config.file.FdseJob;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.Fdse;
import module.impl.ImportConfig;

/**
 * For internal use (developer test imports)
 *
 * @author niemeier
 */
public abstract class ImportProcessTestFdse extends ImportProcessTest {

    private static final Logger LOG = Logger.getLogger(ImportProcessTestFdse.class.getName());

    /**
     * SAMPLE METHOD TO TEST FDSE IMPORT
     *
     * @author Dirk Niemeier
     * @param args arguments
     * @throws Exception Exception
     */
    public static void main(String[] args) throws Exception {
        //*** HEY FELLOW, PLEASE INSERT YOUR TEST CONFIGURATION HERE ***//
        LOG.log(Level.INFO, "You're starting import for developer's internal test use!");
        final DbConfigTest commonDbConfig = TEST_DBS.get(CPX_COMMON_ORACLE); //CUSTOMIZE THIS!
        final DbConfigTest caseDbConfig = TEST_DBS.get(DIRK_SQLSRV); //CUSTOMIZE THIS!
        //final String inputDirectory = "E:\\test"; //CUSTOMIZE THIS! (it's faster when you use a SSD instead of HDD here!)
        final String inputDirectory = "E:\\fdse_test\\input_data2"; //CUSTOMIZE THIS! (it's faster when you use a SSD instead of HDD here!)
        final String outputDirectory = "E:\\fdse_test\\cpx_transformation"; //CUSTOMIZE THIS! (but should not be neccessary, it's faster when you use a SSD instead of HDD here!)
        final ImportMode importMode = ImportMode.Version; //CUSTOMIZE THIS! (use 'version', 'overwrite' or 'truncate')
        //final Fdse module = new Fdse(new FdseJob(inputDirectory), outputDirectory); //CUSTOMIZE THIS! (use P21, Fdse or Sample here)
        final FdseJob p21Job = new FdseJob(inputDirectory, importMode, true, null);
        final Fdse module = new Fdse(p21Job, outputDirectory);
        //final Fdse module = new Fdse(inputDirectory, outputDirectory); //CUSTOMIZE THIS! (use P21, Fdse or Sample here)
        final License license = getFullFeaturedTestLicense();
//        module.getInputConfig().setTargetDatabase(caseDbConfig.getIdentifier()); //caseDbConfig.getIdentifier(), 
//        module.getInputConfig().setImportMode(importMode);
        final ImportConfig<Fdse> importConfig = new ImportConfig<>(caseDbConfig.getIdentifier(), module, license);

        LOG.log(Level.INFO, "Will import FDSE files from: {0}", inputDirectory);
        LOG.log(Level.INFO, "Will use this import mode: {0}", importMode.modeName);

        //*** NOW LEAN BACK AND OBSERVE ***//
        ImportProcessFile<Fdse> process = new ImportProcessFile<>();
        process.start(importConfig, commonDbConfig, caseDbConfig);
    }

}
