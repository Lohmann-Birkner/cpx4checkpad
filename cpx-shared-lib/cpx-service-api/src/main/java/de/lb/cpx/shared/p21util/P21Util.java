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

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Allgemeine Hilfsklasse fuer den P21 Import/Export
 *
 * @author Wilde
 */
public class P21Util {

    public static final String IMPORT_CREATOR_DESCRIPTION = "Created by P21Import";

    public static final String FILENAME_P21FALL = "fall.csv";
    public static final String FILENAME_P21FAB = "fab.csv";
    public static final String FILENAME_P21OPS = "ops.csv";
    public static final String FILENAME_P21ENT = "entgelte.csv";
    public static final String FILENAME_P21ICD = "icd.csv";
    public static final String FILENAME_P21INFO = "info.csv";
    public static final String FILENAME_P21ABRECHNUNG = "abrechnung.csv";
    public static final String FILENAME_P21AUSBILDUNG = "ausbildung.csv";
    public static final String FILENAME_P21FUSIONEN = "fusionen.csv";
    public static final String FILENAME_P21KOSTEN = "kosten.csv";
    public static final String FILENAME_P21KOSTENMODUL = "kostenmodul.csv";
    public static final String FILENAME_P21KRANKENHAUS = "krankenhaus.csv";
    public static final String FILENAME_P21MODELLVORHABEN = "modellverfahren.csv";
    public static final String FILENAME_P21PIALEI = "pia-lei.csv";
    public static final String DELIMITER = ";";
    public static final String NEW_LINE_SEPARATOR = "\n";

    // erwartete Spaltenzahl fuer die verschiedenen Jahre, wird gebraucht fuer die Versionierung
    public static final int COL_COUNT_FAB_2004 = 5;
    public static final int COL_COUNT_OPS_2004 = 9;
    public static final int COL_COUNT_FAB_2005 = 6;
    public static final int COL_COUNT_OPS_2005 = 10;
    public static final int COL_COUNT_FAB_2010 = 7;
    public static final int COL_COUNT_OPS_2010 = 11;
    public static final int COL_COUNT_ICD_2011 = 12;
    public static final int COL_COUNT_INFO_2007 = 7;
    public static final int COL_COUNT_ENTG_2008 = 9;
    public static final int COL_COUNT_ENTG_2012 = 12;
    public static final int COL_COUNT_FALL_2013 = 30;
    public static final int COL_COUNT_FALL_2014 = 31;
    public static final int COL_COUNT_FALL_2016 = 33;//LG
    public static final int COL_COUNT_FALL_2019 = 34;//LG    
    public static final int COL_COUNT_FAB_2020 = 9;

    private static final String[] REQUIRED_CSV_FILES = new String[]{FILENAME_P21FALL,
        FILENAME_P21ICD, FILENAME_P21OPS, FILENAME_P21FAB};
    private static final String[] ALL_AVAILABLE_FILES
            = new String[]{FILENAME_P21ABRECHNUNG, FILENAME_P21AUSBILDUNG, FILENAME_P21ENT,
                FILENAME_P21FAB, FILENAME_P21FALL, FILENAME_P21FUSIONEN, FILENAME_P21ICD,
                FILENAME_P21INFO, FILENAME_P21KOSTEN, FILENAME_P21KOSTENMODUL, FILENAME_P21KRANKENHAUS,
                FILENAME_P21MODELLVORHABEN, FILENAME_P21OPS, FILENAME_P21PIALEI};

    private P21Util() {
        //utility class needs no public constructor
    }

    /**
     * liefert eine Liste von allen Filenamen zurueck, welche von der inek
     * spezifiziert sind
     *
     * @return CSV Filenames
     */
    public static final String[] getAllAvailableCSVFiles() {
        //return ALL_AVAILABLE_FILES;
        return Arrays.copyOf(ALL_AVAILABLE_FILES, ALL_AVAILABLE_FILES.length);
    }

    /**
     * liefert ein File aus dem uebergebenen Directory, ToDo: besser machen,
     * abfrage ist eher brute Force
     *
     * @param dir Directory
     * @param nameLowerCase Name
     * @return File
     */
    public static File getFileFromDir(final File dir, final String nameLowerCase) {
        List<File> bla = Arrays.asList(dir.listFiles()).stream()
                .filter((final File file) -> file.getName().toLowerCase().equals(nameLowerCase))
                .collect(Collectors.toList());
        if (bla.isEmpty()) {
            return null;
        }
        return bla.get(0);

        /*
        return Arrays.asList(dir.listFiles()).stream()
                .filter((final File file) -> file.getName().toLowerCase().equals(nameLowerCase))
                .collect(Collectors.toList()).get(0);
         */
    }

    /**
     * liefert eine Liste von Filenamen zurueck, welche vorhanden sein muessen
     * fuer den Import
     *
     * @return CSV Filenames
     */
    public static final String[] getRequiredCSVFiles() {
        //return REQUIRED_CSV_FILES;
        return Arrays.copyOf(REQUIRED_CSV_FILES, REQUIRED_CSV_FILES.length);
    }

}
