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
import de.lb.cpx.shared.dto.job.config.file.SampleJob;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.Sample;
import module.impl.ImportConfig;
import static process.impl.ImportProcessTest.getFullFeaturedTestLicense;

/**
 * For internal use (developer test imports)
 *
 * @author niemeier
 */
public abstract class ImportProcessTestSample extends ImportProcessTest {

    private static final Logger LOG = Logger.getLogger(ImportProcessTestSample.class.getName());

    /**
     * SAMPLE METHOD TO TEST SAMPLE IMPORT
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
        final String inputDirectory = "E:\\sample_test\\input_data"; //CUSTOMIZE THIS! (it's faster when you use a SSD instead of HDD here!)
        final String outputDirectory = "E:\\sample_test\\cpx_transformation"; //CUSTOMIZE THIS! (but should not be neccessary, it's faster when you use a SSD instead of HDD here!)
        final ImportMode importMode = ImportMode.Version; //CUSTOMIZE THIS! (use 'version', 'overwrite' or 'truncate')
        final SampleJob sampleJob = new SampleJob(inputDirectory, importMode, true, null);
        final Sample module = new Sample(sampleJob, outputDirectory); //CUSTOMIZE THIS! (use P21, Fdse or Sample here)
        final License license = getFullFeaturedTestLicense();
//        module.getInputConfig().setTargetDatabase(caseDbConfig.getIdentifier()); //caseDbConfig.getIdentifier(), 
//        module.getInputConfig().setImportMode(importMode);
        final ImportConfig<Sample> importConfig = new ImportConfig<>(caseDbConfig.getIdentifier(), module, license);

        LOG.log(Level.INFO, "Will import Sample files from: " + inputDirectory);
        LOG.log(Level.INFO, "Will use this import mode: " + importMode.modeName);

        //*** NOW LEAN BACK AND OBSERVE ***//
        ImportProcessFile<Sample> process = new ImportProcessFile<>();
        process.start(importConfig, commonDbConfig, caseDbConfig);
    }

}
