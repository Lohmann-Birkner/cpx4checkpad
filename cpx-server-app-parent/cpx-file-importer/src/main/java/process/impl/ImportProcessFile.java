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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package process.impl;

import de.lb.cpx.shared.dto.job.config.CpxFileBasedImportJob;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.AbstractImportFileModule;
import module.Fdse;
import module.P21;
import module.Sample;
import module.impl.ImportConfig;
import transformer.CpxTransformerI;
import transformer.impl.FdseToCpxTransformer;
import transformer.impl.P21ToCpxTransformer;
import transformer.impl.SampleToCpxTransformer;

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
/**
 * Import process (overall class for all imports like SAP or P21)
 *
 * @author niemeier
 * @param <T> config type
 */
public class ImportProcessFile<T extends AbstractImportFileModule<? extends CpxFileBasedImportJob>> extends ImportProcess<T> {

    private static final Logger LOG = Logger.getLogger(ImportProcessFile.class.getName());

    //public final static boolean isOracle = false; //false = Microsoft SQL Server
//    static {
//        setTransformer(new CpxFileTransformerFactory());
//    }
//    /**
//     * Method to import from P21 directory to target database
//     *
//     * @param args Arguments
//     * @author Dirk Niemeier
//     * @throws Exception Exception
//     */
//    public static void main(String[] args) throws Exception {
//        CpxSystemProperties.useUseDirAsCpxHome(true);
//        LOG.log(Level.INFO, "P21-Import runs in this directory: " + CpxSystemProperties.getInstance().getCpxHome());
//        
//        final String inputDirectory = (args != null && args.length >= 1) ? args[0].trim() : "";
//        final String caseDb = (args != null && args.length >= 2) ? args[1].trim() : "";
//        final String importModeTmp = (args != null && args.length >= 3) ? args[2].trim() : "";
//        
//        if (inputDirectory.isEmpty()) {
//            throw new IllegalArgumentException("No input directory (where are you're P21 files are lieing) passed (arg 1)!");
//        }
//        
//        if (caseDb.isEmpty()) {
//            throw new IllegalArgumentException("No case database (e.g. dbsys1:CPX) passed (arg 2)!");
//        }
//        
//        final ImportMode importMode = ImportMode.getImportMode(importModeTmp, ImportMode.Version);
//        
//        LOG.log(Level.INFO, "Will import P21 files from: " + inputDirectory);
//        LOG.log(Level.INFO, "Will import to database: " + caseDb);
//        LOG.log(Level.INFO, "Will use this import mode: " + importMode.name);
//        
//        final ImportConfig<P21> importConfig = new ImportConfig<>(caseDb, new P21(inputDirectory), importMode);
//        ImportProcessFile<P21> process = new ImportProcessFile<>();
//        process.start(importConfig, caseDb);
//    }
    @Override
    @SuppressWarnings("unchecked")
    public CpxTransformerI<T> getTransformer(final ImportConfig<T> pImportConfig) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchMethodException, InvocationTargetException, NoSuchAlgorithmException, SQLException {
        final CpxTransformerI<T> transformer;
        if (pImportConfig.isModuleClass(P21.class)) {
            transformer = (CpxTransformerI<T>) new P21ToCpxTransformer((ImportConfig<P21>) pImportConfig);
        } else if (pImportConfig.isModuleClass(Fdse.class)) {
            transformer = (CpxTransformerI<T>) new FdseToCpxTransformer((ImportConfig<Fdse>) pImportConfig);
        } else if (pImportConfig.isModuleClass(Sample.class)) {
            transformer = (CpxTransformerI<T>) new SampleToCpxTransformer((ImportConfig<Sample>) pImportConfig);
        } else {
            LOG.log(Level.WARNING, "No transformer implemented for module {0}!", pImportConfig.getModuleName());
            transformer = null;
        }
        //pImportConfig.setTransformer(transformer);
        return transformer;
    }

//    @Override
//    public void start(final ImportConfig<?> pImportConfig, final Connection pConnCommonDb, final Connection pConnCaseDb) throws IllegalArgumentException, IllegalStateException, InvocationTargetException, NoSuchFieldException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchMethodException, InterruptedException {
//        final CpxTransformerI transformer;
//        if (pImportConfig.isModuleClass(P21.class)) {
//            transformer = new P21ToCpxTransformer((ImportConfig<P21>) pImportConfig);
//        } else if (pImportConfig.isModuleClass(Fdse.class)) {
//            transformer = new FdseToCpxTransformer((ImportConfig<Fdse>) pImportConfig);
//        } else if (pImportConfig.isModuleClass(Sample.class)) {
//            transformer = new SampleToCpxTransformer((ImportConfig<Sample>) pImportConfig);
//        } else {
//            LOG.log(Level.WARNING, "No transformer implemented for module " + pImportConfig.getModuleName() + "!");
//            transformer = null;
//        }
//        pImportConfig.setTransformer(transformer);
//        super.start(pImportConfig, pConnCommonDb, pConnCaseDb);
//    }
}
