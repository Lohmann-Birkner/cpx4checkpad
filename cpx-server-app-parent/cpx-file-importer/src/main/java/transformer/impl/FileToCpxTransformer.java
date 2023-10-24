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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package transformer.impl;

import de.lb.cpx.shared.dto.job.config.CpxFileBasedImportJob;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import module.AbstractImportFileModule;
import module.impl.ImportConfig;
import transformer.AbstractCpxTransformer;

/**
 *
 * @author niemeier
 * @param <T> config type
 */
public abstract class FileToCpxTransformer<T extends AbstractImportFileModule<? extends CpxFileBasedImportJob>> extends AbstractCpxTransformer<T> {

    private final File mInputDirectory;

    public FileToCpxTransformer(final ImportConfig<T> pImportConfig) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException, SQLException {
        super(pImportConfig);
        mInputDirectory = checkInputDirectory(pImportConfig.getModule().getInputDirectory());
    }

    public File getInputDirectory() {
        return mInputDirectory;
    }

    public static File checkInputDirectory(final String pDirectory) {
        File dir = checkDirectory(pDirectory);
        if (!dir.canRead()) {
            throw new IllegalArgumentException("No read permission: " + dir.getAbsolutePath());
        }
        /*
    if (dir.listFiles().length <= 0) {
      throw new CpxIllegalArgumentException("Directory has no input files: " + dir.getAbsolutePath());
    }
         */
        return dir;
    }

}
