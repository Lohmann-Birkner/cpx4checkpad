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
package process;

import de.lb.cpx.shared.dto.job.config.CpxJobImportConfig;
import java.sql.Connection;
import java.sql.SQLException;
import module.ImportModuleI;
import module.impl.ImportConfig;
import progressor.ProgressorI;

/**
 *
 * @author niemeier
 * @param <T> config type
 */
public interface ImportProcessI<T extends ImportModuleI<? extends CpxJobImportConfig>> {

    /**
     * Starts import process
     *
     * @param importConfig use this import configuration (SAP, P21... and its
     * parameters)
     */
    void startImportProcess(final ImportConfig<T> importConfig);

    /**
     * Sets the common db connection
     *
     * @param pConnection jdbc connection
     * @throws IllegalArgumentException error
     * @throws SQLException error
     */
    void setCommonDbConnection(final Connection pConnection) throws IllegalArgumentException, SQLException;

    /**
     * Sets the case db connection
     *
     * @param pConnection jdbc connection
     * @throws IllegalArgumentException error
     * @throws SQLException error
     */
    void setCaseDbConnection(final Connection pConnection) throws IllegalArgumentException, SQLException;

    /**
     * Gives the case db connection
     *
     * @return jdbc connection
     */
    Connection getCaseDbConnection();

    /**
     * Gives the case database
     *
     * @return case database
     */
    String getCaseDbDatabase();

    /**
     * Is this common db an Microsoft SQL Server db?
     *
     * @return is Microsoft SQL Server?
     */
    boolean isCaseDbOracle();

    /**
     * Is this case db an Microsoft SQL Server db?
     *
     * @return is Microsoft SQL Server?
     */
    boolean isCaseDbSqlSrv();

    /**
     * Gives the common db connection
     *
     * @return jdbc connection
     */
    Connection getCommonDbConnection();

    /**
     * Gives the common database
     *
     * @return common database
     */
    String getCommonDbDatabase();

    /**
     * Is this common db an Oracle db?
     *
     * @return is Oracle?
     */
    boolean isCommonDbOracle();

    /**
     * Is this common db an Microsoft SQL Server db?
     *
     * @return is Microsoft SQL Server?
     */
    boolean isCommonDbSqlSrv();

    /**
     * progressor helper
     *
     * @return progressor
     */
    ProgressorI getProgressor();

}
