/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package module;

import de.lb.cpx.shared.dto.job.config.CpxDatabaseBasedImportJob;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 * @param <T> type
 */
public abstract class AbstractImportDbModule<T extends CpxDatabaseBasedImportJob> extends AbstractImportModule<T> {

    private static final Logger LOG = Logger.getLogger(AbstractImportDbModule.class.getName());
    private static final long serialVersionUID = 1L;

    public AbstractImportDbModule(final String pName, final T pInputConfig) {
        this(pName, pInputConfig, ImportModuleI.getTempOutputDirectory());
    }

    public AbstractImportDbModule(final String pName, final T pInputConfig, final String pOutputDirectory) {
        super(pName, pInputConfig, pOutputDirectory);
    }

}
