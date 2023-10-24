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
package de.lb.cpx.reader.util;

import de.lb.cpx.reader.DocumentReader;
import java.io.File;

/**
 *
 * @author niemeier
 */
public class Document {

    public final File file;
    public final String text;
    private String caseNumber;
    private String patientNumber;

    public Document(final File pFile, final String pText) {
        if (pFile == null) {
            throw new IllegalArgumentException("source file cannot be null!");
        }
        this.file = pFile;
        this.text = pText == null ? "" : pText.trim();
    }

    public boolean isEmpty() {
        return text.isEmpty();
    }

    public int length() {
        return text.length();
    }

    public String getText() {
        return text;
    }

    public File getFile() {
        return file;
    }

    public String getCaseNumber() {
        if (caseNumber == null) {
            caseNumber = DocumentReader.getCaseNumber(text);
        }
        return caseNumber;
    }

    public String getPatientNumber() {
        if (patientNumber == null) {
            patientNumber = DocumentReader.getPatientNumber(text);
        }
        return patientNumber;
    }

    public String getMdkAddress() {
        return DocumentReader.getMdkAddress(text);
    }

    public String getIkz() {
        throw new UnsupportedOperationException("don't use this, dumbass!");
    }

    public String getAuditReasons() {
        throw new UnsupportedOperationException("don't use this, dumbass!");
    }

}
