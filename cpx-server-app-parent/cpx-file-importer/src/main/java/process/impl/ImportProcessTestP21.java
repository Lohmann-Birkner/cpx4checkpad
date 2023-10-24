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
import de.lb.cpx.shared.dto.job.config.file.P21Job;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.P21;
import module.impl.ImportConfig;
import static process.impl.ImportProcessTest.getFullFeaturedTestLicense;

/**
 * For internal use (developer test imports)
 *
 * @author niemeier
 */
public class ImportProcessTestP21 extends ImportProcessTest {

    private static final Logger LOG = Logger.getLogger(ImportProcessTestP21.class.getName());

    //public final static String P21_INPUT_DIRECTORY = "E:\\p21-import\\"; //Basic input directory where are your P21 dataset files
    public static final String P21_INPUT_DIRECTORY = "E:\\p21-import\\"; //Basic input directory where are your P21 dataset files
    public static final String INEK_SMALL_8 = P21_INPUT_DIRECTORY + "inek-small";
    public static final String INEK_MEDIUM_452 = P21_INPUT_DIRECTORY + "inek-medium";
    public static final String INEK_BIG_27185 = P21_INPUT_DIRECTORY + "inek-big";
    public static final String INEK_400000 = P21_INPUT_DIRECTORY + "inek-400000";
    public static final String INEK_HUGE_802704 = P21_INPUT_DIRECTORY + "inek-huge";
    public static final String INEK_TEST_802704 = P21_INPUT_DIRECTORY + "test";
    public static final String INEK_20170314_P21FORMAT2014_29488 = P21_INPUT_DIRECTORY + "p21_20170314_P21Format2014";
    public static final String INEK_IMPORT_200000 = P21_INPUT_DIRECTORY + "p21_import_200K";
    public static final String P21_ANONYM_LUTZ = P21_INPUT_DIRECTORY + "P21_anonym_lutz2";
    public static final String VONHRLUTZMITPEPP_V2013 = P21_INPUT_DIRECTORY + "vonHrLutzMitPEPP_V2013";
    public static final String P21_2019 = P21_INPUT_DIRECTORY + "P21_2019";
    public static final String P21_2019_1 = P21_INPUT_DIRECTORY + "p21_2019_1";
    public static final String P21_2019_1_VERS = P21_INPUT_DIRECTORY + "p21_2019_1_vers";
    public static final String P21_2019_1_VERS1 = P21_INPUT_DIRECTORY + "p21_2019_1_vers1";
    public static final String VERSION_2014 = P21_INPUT_DIRECTORY + "Version_2014";
    public static final String VERSION_2015 = P21_INPUT_DIRECTORY + "Version_2015";
    public static final String VERSION_2016 = P21_INPUT_DIRECTORY + "Version_2016";
    public static final String VERSION_2017 = P21_INPUT_DIRECTORY + "Version_2017";
    public static final String VERSION_2018 = P21_INPUT_DIRECTORY + "Version_2018";
    public static final String VERSION_2019 = P21_INPUT_DIRECTORY + "Version_2019";
    public static final String VERSION_2019_2 = P21_INPUT_DIRECTORY + "Version_2019_2";
    public static final String P21VERSION2014KONVERTIERTANONYMISIERT = P21_INPUT_DIRECTORY + "P21Version2014KonvertiertAnonymisiert";
    public static final String P21_5300FAELLE = P21_INPUT_DIRECTORY + "P21_5300Fälle";
    public static final String VONHRLUTZMITPEPP_FAELLEN = P21_INPUT_DIRECTORY + "vonHrLutzMitPEPP_Fällen";
    public static final String P21_HDX_MIT_ZUSATZ = P21_INPUT_DIRECTORY + "p21_hdx_mit_zusatz";
    public static final String P21_088 = P21_INPUT_DIRECTORY + "p21_088";
    public static final String PATIENT = P21_INPUT_DIRECTORY + "patient";

    public static final String P21_OUTPUT_DIRECTORY = P21_INPUT_DIRECTORY + "cpx_transformation"; //Basic output directory where are your P21 dataset files

    /**
     * SAMPLE METHOD TO TEST P21 IMPORT
     *
     * @author Dirk Niemeier
     * @param args arguments
     * @throws Exception Exception
     */
    public static void main(String[] args) throws Exception {
        //*** HEY FELLOW, PLEASE INSERT YOUR TEST CONFIGURATION HERE ***//
        LOG.log(Level.INFO, "You're starting import for developer's internal test use!");
        final DbConfigTest commonDbConfig = TEST_DBS.get(CPX_COMMON_ORACLE); //CUSTOMIZE THIS!
        final DbConfigTest caseDbConfig = TEST_DBS.get(SPACKODATENBANK_ORACLE); //CUSTOMIZE THIS!
        final String inputDirectory = P21_2019;
        //final String inputDirectory = "E:\\p21-import\\p21_cp_mig_angepasst_1"; //CUSTOMIZE THIS! (it's faster when you use a SSD instead of HDD here!)
        //final String inputDirectory = INEK_BIG_27185; //CUSTOMIZE THIS! (it's faster when you use a SSD instead of HDD here!)
        final String outputDirectory = P21_OUTPUT_DIRECTORY; //CUSTOMIZE THIS! (but should not be neccessary, it's faster when you use a SSD instead of HDD here!)
        final ImportMode importMode = ImportMode.Version; //CUSTOMIZE THIS! (use 'version', 'overwrite' or 'truncate')
        final P21Job p21Job = new P21Job(inputDirectory, importMode, true, null);
        final P21 module = new P21(p21Job, outputDirectory);
        //final P21 module = new P21(new P21Job(inputDirectory), outputDirectory); //CUSTOMIZE THIS! (use P21, Fdse or Sample here)
        //final P21 module = new P21(inputDirectory, outputDirectory); //CUSTOMIZE THIS! (use P21, Fdse or Sample here)
        final License license = getFullFeaturedTestLicense();
        //module.getInputConfig().setTargetDatabase(caseDbConfig.getIdentifier()); //caseDbConfig.getIdentifier(), 
        //module.getInputConfig().setImportMode(importMode);
        final ImportConfig<P21> importConfig = new ImportConfig<>(caseDbConfig.getIdentifier(), module, license
        //                 new ImportConstraint(
        //                        new Date(),
        //                        new Date(),
        //                        new Date(),
        //                        new Date(),
        //                        Arrays.asList(new AdmissionCauseEn[]{AdmissionCauseEn.A}),
        //                        Arrays.asList(new AdmissionReasonEn[]{AdmissionReasonEn.ar01, AdmissionReasonEn.ar05}),
        //                        Arrays.asList(new IcdcTypeEn[]{IcdcTypeEn.DRG}),
        //                        "1,2,3,5,6,6")
        );

        LOG.log(Level.INFO, "Will import P21 files from: " + inputDirectory);
        LOG.log(Level.INFO, "Will use this import mode: " + importMode.modeName);

        //*** NOW LEAN BACK AND OBSERVE ***//
        ImportProcessFile<P21> process = new ImportProcessFile<>();
        process.start(importConfig, commonDbConfig, caseDbConfig);
    }

}
