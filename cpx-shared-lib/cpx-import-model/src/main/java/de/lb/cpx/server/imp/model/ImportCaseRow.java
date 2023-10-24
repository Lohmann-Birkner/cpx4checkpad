/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.imp.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author husser
 */
public class ImportCaseRow implements Serializable {

    private static final long serialVersionUID = 1L;

    private ImportCaseKey caseKey;
    private String[] csvRow;

    public ImportCaseRow(ImportCaseKey caseKey, String[] csvRow) {
        this.caseKey = caseKey;
        this.csvRow = csvRow == null ? null : Arrays.copyOf(csvRow, csvRow.length);
    }

    public ImportCaseKey getCaseKey() {
        return caseKey;
    }

    public void setCaseKey(ImportCaseKey caseKey) {
        this.caseKey = caseKey;
    }

    public String[] getCsvRow() {
        return csvRow == null ? null : Arrays.copyOf(csvRow, csvRow.length);
    }

    public void setCsvRow(String[] csvRow) {
        this.csvRow = csvRow == null ? null : Arrays.copyOf(csvRow, csvRow.length);
    }

}
