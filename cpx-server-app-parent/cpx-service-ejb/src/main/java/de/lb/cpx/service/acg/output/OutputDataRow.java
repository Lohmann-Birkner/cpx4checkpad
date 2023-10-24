/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.acg.output;

import de.lb.cpx.file.reader.CpxFileReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class OutputDataRow {

    private static final Logger LOG = Logger.getLogger(OutputDataRow.class.getName());

    public final Map<String, OutputDataCell> results;
    public final int lineNumber;

    public OutputDataRow(final Map<String, OutputDataCell> pResults, final int pLineNumber) {
        results = pResults == null ? new LinkedHashMap<>() : new LinkedHashMap<>(pResults);
        lineNumber = pLineNumber;
    }

    public OutputDataCell getResult(final String pColumnName) {
        //final String columnName = pColumnName == null ? "": pColumnName.trim().toLowerCase().replace(" ", "_");
        final String columnName = CpxFileReader.normalizeKey(pColumnName);
        OutputDataCell result = results.get(columnName);
        if (result == null) {
            LOG.log(Level.WARNING, "A column with the name '" + columnName + "' does not seem to exist in this CSV file");
        }
        return result;
    }

    public String getResultValue(final String pColumnName) {
        final OutputDataCell result = getResult(pColumnName);
        if (result == null) {
            return "";
        }
        return result.value;
    }

    public Integer getResultIndex(final String pColumnName) {
        final OutputDataCell result = getResult(pColumnName);
        if (result == null) {
            return null;
        }
        return result.index;
    }

    @Override
    public String toString() {
        return "OutputDataRow{" + "results=" + results + ", lineNumber=" + lineNumber + '}';
    }

}
