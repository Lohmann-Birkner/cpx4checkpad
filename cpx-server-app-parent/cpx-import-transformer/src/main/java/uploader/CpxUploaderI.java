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
package uploader;

import de.lb.cpx.shared.dto.job.config.CpxJobImportConfig;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import module.ImportModuleI;
import module.impl.ImportConfig;
import progressor.ProgressorI;

/**
 *
 * @author Dirk Niemeier
 * @param <T> config type
 */
public interface CpxUploaderI<T extends ImportModuleI<? extends CpxJobImportConfig>> {

    /**
     * progressor helper
     *
     * @return progressor
     */
    ProgressorI getProgressor();

    void uploadImex(final ImportConfig<T> pImportConfig, final Connection pConnection) throws InstantiationException, IllegalAccessException, IOException, SQLException, NoSuchFieldException, ParseException;

}
