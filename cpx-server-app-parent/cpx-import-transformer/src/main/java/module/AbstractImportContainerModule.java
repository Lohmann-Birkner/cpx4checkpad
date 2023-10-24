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

import de.lb.cpx.shared.dto.job.config.CpxExternalSystemBasedJobImportConfig;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 * @param <T> config type
 * @param <C> container type
 */
public abstract class AbstractImportContainerModule<T extends CpxExternalSystemBasedJobImportConfig, C> extends AbstractImportModule<T> {

    private static final Logger LOG = Logger.getLogger(AbstractImportContainerModule.class.getName());
    private final C mContainer;
    private static final long serialVersionUID = 1L;

    public AbstractImportContainerModule(final String pName, final T pInputContainer, final C pContainer) {
        this(pName, pInputContainer, pContainer, ImportModuleI.getTempOutputDirectory());
    }

    public AbstractImportContainerModule(final String pName, final T pInputConfig, final C pContainer, final String pOutputDirectory) {
        super(pName, pInputConfig, pOutputDirectory);

        mContainer = pContainer;

        if (mContainer == null) {
            throw new IllegalArgumentException("No container instance given!");
        }
    }

    public C getContainer() {
        return mContainer;
    }

}
