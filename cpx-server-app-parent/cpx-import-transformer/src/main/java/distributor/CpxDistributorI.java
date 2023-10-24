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
package distributor;

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
public interface CpxDistributorI<T extends ImportModuleI<? extends CpxJobImportConfig>> {

    /**
     * progressor helper
     *
     * @return progressor
     */
    ProgressorI getProgressor();

    /**
     * Takes transformation files (intermedia files) and writes them to CPX
     * database
     *
     * @param pImportConfig import configuration
     * @param pCommonDbConnection jdbc connection to common database
     * @param pCaseDbConnection jdbc connection to case database
     * @param pLogQueries shall sql queries be logged?
     * @throws IllegalArgumentException error
     * @throws IllegalAccessException error
     * @throws SQLException error
     * @throws IOException error
     * @throws ParseException error
     */
    void distributeData(final ImportConfig<T> pImportConfig, final Connection pCommonDbConnection, final Connection pCaseDbConnection, final boolean pLogQueries) throws IllegalArgumentException, IllegalAccessException, SQLException, IOException, ParseException;

}
