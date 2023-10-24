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
package de.lb.cpx.shared.p21util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum P21Version implements Serializable {

    P21V2004(2004, "n.a.", "2003"),
    P21V2005(2005, "n.a.", "2004"),
    P21V2008(2008, "n.a.", "2007"),
    P21V2010(2010, "n.a.", "2009"),
    P21V2011(2011, "n.a.", "2010"),
    P21V2012(2012, "20120101", "2011"),
    P21V2013(2013, "20130101", "2012"),
    P21V2014(2014, "20140101", "2013"),
    P21V2015(2015, "20150101", "2014"),
    P21V2016(2016, "20160101", "2015"),
    P21V2017(2017, "20170101", "2016"),
    P21V2018(2018, "20180101", "2017"),
    P21V2019(2019, "20190101", "2018"),
    P21V2020(2020, "20200101", "2019");

    private static final Logger LOG = Logger.getLogger(P21Version.class.getName());
    public static final int MIN_SUPPORTED_EXPORT_YEAR = 2019;
    public static final int MIN_SUPPORTED_IMPORT_YEAR = 2013;

    private final int year;
    private final String versionIdentifier;
    private final String label;

    private P21Version(final int year, final String pVersionIdentifier, final String pLabel) {
        this.year = year;
        this.versionIdentifier = pVersionIdentifier;
        this.label = pLabel;
    }

    public int getYear() {
        return year;
    }

    public String getVersionIdentifier() {
        return versionIdentifier;
    }

    public String getLabel() {
        return label;
    }

    public static P21Version[] getExportVersions() {
        final List<P21Version> list = new ArrayList<>();
        for (P21Version version : values()) {
            if (version.isExportVersion()) {
                list.add(version);
            }
        }
        P21Version[] tmp = new P21Version[list.size()];
        list.toArray(tmp);
        return tmp;
    }

    public static P21Version[] getImportVersions() {
        final List<P21Version> list = new ArrayList<>();
        for (P21Version version : values()) {
            if (version.isImportVersion()) {
                list.add(version);
            }
        }
        P21Version[] tmp = new P21Version[list.size()];
        list.toArray(tmp);
        return tmp;
    }

    public boolean isImportVersion() {
        return this.year >= MIN_SUPPORTED_IMPORT_YEAR;
    }

    public boolean isExportVersion() {
        return this.year >= MIN_SUPPORTED_EXPORT_YEAR;
    }

    public static int getRecentYear() {
        int maxYear = 0;
        for (P21Version version : values()) {
            if (version.year > maxYear) {
                maxYear = version.year;
            }
        }
        return maxYear;
    }

    public static P21Version getRecent() {
//        Calendar cal = Calendar.getInstance();
        int recentYear = getRecentYear();
//        int year = cal.get(Calendar.YEAR);
        for (P21Version version : values()) {
            if (version.year == recentYear) {
                return version;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return label + " - " + versionIdentifier;
    }

    public static P21Version find(final String pName) {
        if (pName == null) {
            return null;
        }
        for (P21Version item : values()) {
            if (pName.trim().equalsIgnoreCase(item.name())) {
                return item;
            }
        }
        LOG.log(Level.WARNING, "This is not a valid P21 Version: " + pName);
        return null;
    }

}
