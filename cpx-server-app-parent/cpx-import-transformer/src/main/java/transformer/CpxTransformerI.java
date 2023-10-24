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
package transformer;

import de.lb.cpx.shared.dto.job.config.CpxJobConstraints;
import de.lb.cpx.shared.dto.job.config.CpxJobImportConfig;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import module.ImportModuleI;
import module.impl.ImportConfig;
import transformer.impl.TransformResult;

/**
 *
 * @author Dirk Niemeier
 * @param <T> config type
 */
public interface CpxTransformerI<T extends ImportModuleI<? extends CpxJobImportConfig>> extends AutoCloseable {

    /**
     * Start transformation to intermedia files
     *
     * @return result of transformation (number of hospital cases)
     * @throws InstantiationException error
     * @throws IllegalAccessException error
     * @throws IOException error
     * @throws NoSuchFieldException error
     * @throws InterruptedException error
     * @throws NoSuchAlgorithmException error
     * @throws java.lang.NoSuchMethodException error
     * @throws java.lang.reflect.InvocationTargetException error
     * @throws java.sql.SQLException sql error
     */
    TransformResult start() throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, InterruptedException, NoSuchAlgorithmException, NoSuchMethodException, InvocationTargetException, SQLException;

    ImportConfig<T> getImportConfig();

    CpxJobConstraints getImportConstraint();

    T getModule();

}
