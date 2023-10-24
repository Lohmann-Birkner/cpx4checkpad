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
 *    2019  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.db.importer;

import de.lb.cpx.shared.dto.job.config.CpxDatabaseBasedImportJob;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.AbstractImportDbModule;
import module.Kissmed;
import module.Medico;
import module.Nexus;
import module.Orbis;
import module.impl.ImportConfig;
import process.impl.ImportProcess;
import transformer.CpxTransformerI;
import transformer.impl.KissmedToCpxTransformer;
import transformer.impl.MedicoToCpxTransformer;
import transformer.impl.NexusToCpxTransformer;
import transformer.impl.OrbisToCpxTransformer;

/**
 * Import process (overall class for all database imports like Orbis, Medico or
 * KISSMED)
 *
 * @author niemeier
 * @param <T> config type
 */
public class ImportProcessDb<T extends AbstractImportDbModule<? extends CpxDatabaseBasedImportJob>> extends ImportProcess<T> {

    private static final Logger LOG = Logger.getLogger(ImportProcessDb.class.getName());

    @Override
    @SuppressWarnings("unchecked")
    public CpxTransformerI<T> getTransformer(final ImportConfig<T> pImportConfig) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException, SQLException {
        final CpxTransformerI<T> transformer;
        if (pImportConfig.isModuleClass(Orbis.class)) {
            transformer = (CpxTransformerI<T>) new OrbisToCpxTransformer((ImportConfig<Orbis>) pImportConfig);
        } else if (pImportConfig.isModuleClass(Medico.class)) {
            transformer = (CpxTransformerI<T>) new MedicoToCpxTransformer((ImportConfig<Medico>) pImportConfig);
        } else if (pImportConfig.isModuleClass(Kissmed.class)) {
            transformer = (CpxTransformerI<T>) new KissmedToCpxTransformer((ImportConfig<Kissmed>) pImportConfig);
        } else if (pImportConfig.isModuleClass(Nexus.class)) {
            transformer = (CpxTransformerI<T>) new NexusToCpxTransformer((ImportConfig<Nexus>) pImportConfig);
        } else {
            LOG.log(Level.WARNING, "No transformer implemented for module " + pImportConfig.getModuleName() + "!");
            transformer = null;
        }
        //pImportConfig.setTransformer(transformer);
        return transformer;
    }

}
