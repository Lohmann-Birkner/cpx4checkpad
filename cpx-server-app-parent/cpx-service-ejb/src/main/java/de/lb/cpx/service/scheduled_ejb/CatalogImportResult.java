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
package de.lb.cpx.service.scheduled_ejb;

import de.lb.cpx.service.information.CatalogTypeEn;
import java.io.File;

/**
 *
 * @author niemeier
 */
public class CatalogImportResult {

    public final boolean imported;
    public final File importFile;
    public final CatalogTypeEn catalogType;
    public final int year;
    public final String checksum;
    public final long size;
    public final int numberOfEntries;
    public final String message;

//    public CatalogImportResult() {
//        this(null);
//    }
//    public CatalogImportResult(final File pImportFile) {
//        this(null, pImportFile);
//    }
//    
//    public CatalogImportResult(final CatalogTypeEn pCatalogType, final File pImportFile) {
//        this(pCatalogType, false, pImportFile, 0, "", "");
//    }
    public CatalogImportResult(
            final boolean pImported,
            final File pImportFile,
            final CatalogTypeEn pCatalogType,
            final int pYear,
            final String pChecksum,
            final long pSize,
            final int pNumberOfEntries,
            final String pMessage) {
        imported = pImported;
        importFile = pImportFile;
        catalogType = pCatalogType;
        year = pYear;
        checksum = pChecksum == null ? "" : pChecksum.trim();
        size = pSize;
        numberOfEntries = pNumberOfEntries;
        message = pMessage == null ? "" : pMessage.trim();
    }

    public CatalogTypeEn getCatalogType() {
        return catalogType;
    }

    public boolean isImported() {
        return imported;
    }

    public File getImportFile() {
        return importFile;
    }

    public int getYear() {
        return year;
    }

    public String getChecksum() {
        return checksum;
    }

    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    public long getSize() {
        return size;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return (importFile == null ? "no file" : "File: " + importFile.getAbsolutePath())
                + ", Imported: " + (imported ? "YES" : "NO")
                + (catalogType != null ? ", Type: " + catalogType : "")
                + (year != 0 ? ", Year: " + year : "")
                + (!checksum.isEmpty() ? ", Checksum: " + checksum : "")
                + (size != 0L ? ", Size: " + size : "")
                + (numberOfEntries != 0 ? ", Number of Entries: " + numberOfEntries : "")
                + (!message.isEmpty() ? ", Message: " + message : "");
    }

}
