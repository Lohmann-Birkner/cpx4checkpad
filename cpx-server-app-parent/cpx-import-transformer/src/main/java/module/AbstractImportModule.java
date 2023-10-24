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
package module;

import de.lb.cpx.shared.dto.job.config.CpxJobImportConfig;
import static de.lb.cpx.str.utils.StrUtils.toStr;

/**
 * Specifies the import modules in general
 *
 * @author Dirk Niemeier
 * @param <T> type
 */
public abstract class AbstractImportModule<T extends CpxJobImportConfig> implements ImportModuleI<T> {

    private static final long serialVersionUID = 1L;

    private final T mInputConfig;
    public String mOutputDirectory;
    public final String mName;

    public AbstractImportModule(final String pName, final T pInputConfig) {
        this(pName, pInputConfig, ImportModuleI.getTempOutputDirectory());
    }

    public AbstractImportModule(final String pName, final T pInputConfig, final String pOutputDirectory) {
        mOutputDirectory = toStr(pOutputDirectory);
        mInputConfig = pInputConfig;
        mName = toStr(pName);

        if (mOutputDirectory.isEmpty()) {
            throw new IllegalArgumentException("No output directory given!");
        }
        if (mName.isEmpty()) {
            throw new IllegalArgumentException("No name given!");
        }
        if (mInputConfig == null) {
            throw new IllegalArgumentException("No input connection given!");
        }
    }

    @Override
    public String getOutputDirectory() {
        return mOutputDirectory;
    }
    
    @Override
    public void setOutputDirectory(String pDir){
        mOutputDirectory = pDir;
    }

    @Override
    public String getName() {
        return mName;
    }

    /**
     * Database connection to KIS
     *
     * @return import connection to KIS
     */
    @Override
    public T getInputConfig() {
        return mInputConfig;
    }

}
