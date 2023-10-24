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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.version;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 *
 * @author niemeier
 */
public class Version implements Serializable, Comparable<Version> {

    private static final long serialVersionUID = 1L;

    public final LocalDate date;
    public final int majorVersion;
    public final int minorVersion;
    public final int revisionNumber;
    public final String version;
    public final VersionStageEn stage;
    public final String codeName;
    public final String description;

    public Version(final int pMajorVersion, final int pMinorVersion, final int pRevisionNumber, final VersionStageEn pStage, final String pCodeName, final LocalDate pDate, final String pDescription) {
        majorVersion = pMajorVersion;
        minorVersion = pMinorVersion;
        revisionNumber = pRevisionNumber;
        stage = pStage == null ? VersionStageEn.FINAL : pStage;
        codeName = pCodeName == null ? "" : pCodeName.trim();
        date = pDate;
        description = pDescription == null ? "" : pDescription.trim();
        version = buildVersion(majorVersion, minorVersion, revisionNumber, stage);
    }

//    public Version(final int pMajorVersion, final int pMinorVersion, final LocalDate pDate, final String pDescription) {
//        this(pMajorVersion, pMinorVersion, VersionStageEn.FINAL, pDate, pDescription);
//    }
//
//    public Version(final int pMajorVersion, final int pMinorVersion, final LocalDate pDate) {
//        this(pMajorVersion, pMinorVersion, VersionStageEn.FINAL, pDate, null);
//    }
//
//    public Version(final int pMajorVersion, final int pMinorVersion, final VersionStageEn pStage, final LocalDate pDate) {
//        this(pMajorVersion, pMinorVersion, pStage, pDate, null);
//    }
//
    public Version(final int pMajorVersion, final int pMinorVersion, final VersionStageEn pStage, final String pCodeName, final LocalDate pDate, final String pDescription) {
        this(pMajorVersion, pMinorVersion, 0, pStage, pCodeName, pDate, pDescription);
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public int getRevisionNumber() {
        return revisionNumber;
    }

    public String getVersion() {
        return version;
    }

    public String getCodeName() {
        return codeName;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDateIso() {
        final String dt;
        if (date == null) {
            dt = "----/--/--";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            dt = date.format(formatter);
//            SimpleDateFormat dtf = new SimpleDateFormat("yyyy-mm-dd"); 
//            dt = dtf.format(tmp); 
        }
        return dt;
    }

    public String getDateGerman() {
        final String dt;
        if (date == null) {
            dt = "--.--.----";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            dt = date.format(formatter);
//            SimpleDateFormat dtf = new SimpleDateFormat("yyyy-mm-dd"); 
//            dt = dtf.format(tmp); 
        }
        return dt;
    }

    public String getDescription() {
        return description;
    }

    private static String buildVersion(final int pMajorVersion, final int pMinorVersion, final int pRevisionNumber, final VersionStageEn pStage) {
        final String minorVersionFormat = "%02d";
        final String revisionNumberFormat = "%01d";
        return pMajorVersion
                + "." + String.format(minorVersionFormat, pMinorVersion)
                //+ (pRevisionNumber == 0 ? "" : "." + String.format(revisionNumberFormat, pRevisionNumber))
                + "." + String.format(revisionNumberFormat, pRevisionNumber)
                + (pStage == null ? "" : pStage.suffix);
    }

    @Override
    public int compareTo(Version o) {
        int c = Integer.compare(this.majorVersion, o.majorVersion);
        if (c != 0) {
            return c;
        }
        c = Integer.compare(this.minorVersion, o.minorVersion);
        if (c != 0) {
            return c;
        }
        c = Integer.compare(this.revisionNumber, o.revisionNumber);
        if (c != 0) {
            return c;
        }
        return Integer.compare(this.stage.maturityLevel, o.stage.maturityLevel);
    }

    @Override
    public String toString() {
        final String dt = getDateIso();
        return version + " (" + (codeName.isEmpty() ? "" : codeName + ", ") + dt + ")" + (description.isEmpty() ? "" : " -> " + description);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.majorVersion;
        hash = 47 * hash + this.minorVersion;
        hash = 47 * hash + this.revisionNumber;
        hash = 47 * hash + Objects.hashCode(this.stage);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Version other = (Version) obj;
        return compareTo(other) == 0;
    }

}
