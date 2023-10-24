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
import java.time.Month;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author niemeier
 */
public class VersionHistory implements Serializable {

    //public final static String MAIN_VERSION = "1";
    //public final static String MINOR_VERSION = "06";
    //public final static String VERSION = MAIN_VERSION + "." + MINOR_VERSION;
    private static final Set<Version> VERSIONS = Collections.unmodifiableSet(buildVersionList());
    private static final Version RECENT_VERSION = findRecentVersion(false);
    private static final Version RECENT_PUBLISHED_VERSION = findRecentVersion(true);
    private static final long serialVersionUID = 1L;

    private VersionHistory() {

    }

    public static Version getRecentVersion() {
        return RECENT_VERSION;
    }

    public static Version getRecentPublishedVersion() {
        return RECENT_PUBLISHED_VERSION;
    }

    public static Set<Version> getVersions() {
        return Collections.unmodifiableSet(new TreeSet<>(VERSIONS));
    }

    private static Set<Version> buildVersionList() {
        final Set<Version> versions = new TreeSet<>();
        versions.add(new Version(1, 0, VersionStageEn.FINAL, null, LocalDate.of(2018, Month.MAY, 31), "Ergänzungen, Korrekturen RePrü L&B")); //1.0.0
        versions.add(new Version(1, 1, VersionStageEn.FINAL, null, LocalDate.of(2018, Month.JUNE, 22), "Korr./Erweiterungen Reprü")); //1.01.0
        versions.add(new Version(1, 2, VersionStageEn.FINAL, null, LocalDate.of(2018, Month.JULY, 12), "Nachfolger von 1.01.0 (Release-Branch Reprü)")); //1.02.0
        versions.add(new Version(1, 3, VersionStageEn.FINAL, null, LocalDate.of(2018, Month.AUGUST, 24), "zweiter Release Branch Reprü (Nachfolger von 1.02.0)")); //1.03.0
        versions.add(new Version(1, 4, VersionStageEn.FINAL, null, LocalDate.of(2018, Month.DECEMBER, 3), "dritter Release Branch Reprü (Jahresendrelease)")); //1.04.0
        versions.add(new Version(1, 5, VersionStageEn.FINAL, null, LocalDate.of(2019, Month.MARCH, 5), "Wechsel auf WF15 und Java 11")); //1.05.0
        versions.add(new Version(1, 6, VersionStageEn.FINAL, null, LocalDate.of(2019, Month.MAY, 15), "Release für Pilotprojekte")); //1.06.0
        versions.add(new Version(1, 7, VersionStageEn.FINAL, null, LocalDate.of(2019, Month.JUNE, 28), "Verbessertes Release für Pilotprojekte")); //1.07.0
        versions.add(new Version(1, 7, 1, VersionStageEn.FINAL, null, LocalDate.of(2019, Month.JULY, 22), "Verbessertes Release für Pilotprojekte")); //1.07.1
        versions.add(new Version(1, 8, VersionStageEn.FINAL, null, LocalDate.of(2019, Month.SEPTEMBER, 26), "Release zur Produktivnahme von Pilotprojekten, Wechsel auf WF17 und Java 13")); //1.08.0
        versions.add(new Version(1, 8, 1, VersionStageEn.FINAL, null, LocalDate.of(2019, Month.OCTOBER, 31), "Verbessertes Release zur Produktivnahme von Pilotprojekten")); //1.08.1
        //versions.add(new Version(1, 8, 2, VersionStageEn.ALPHA, null, null, "")); //1.08.2
        versions.add(new Version(1, 9, VersionStageEn.FINAL, "Partridge", LocalDate.of(2019, Month.NOVEMBER, 15), "Verbessertes Release zur Produktivnahme von Pilotprojekten")); //1.09.0
        versions.add(new Version(1, 9, 1, VersionStageEn.FINAL, "Buzzard", LocalDate.of(2020, Month.JANUARY, 15), "Jahresrelease 2020, Pflege-DRG gesetzliche Änderung und Nachbearbeitung Funktion, Bug-Fixing 1.09.0")); //1.09.1
        versions.add(new Version(1, 9, 2, VersionStageEn.ALPHA, "Smart Sparrow", null, "Bugfixes 1.9.1+Erweiterungen")); //1.09.2
        versions.add(new Version(1, 10, VersionStageEn.FINAL, "Smart Finch", LocalDate.of(2020, Month.JUNE, 30), "Kundenrelease zur Produktivnahme")); //1.10
        versions.add(new Version(1, 10,1, VersionStageEn.FINAL, "Mockingbird", LocalDate.of(2020, Month.AUGUST, 25), "Kundenrelease, diverse verbesserungen")); //1.10.1
        versions.add(new Version(1, 10,2, VersionStageEn.FINAL, "Robin", LocalDate.of(2020, Month.OCTOBER, 27), "Kundenrelease, diverse verbesserungen")); //1.10.2
        versions.add(new Version(2, 0,0, VersionStageEn.FINAL, "Spotted Woodpecker", LocalDate.of(2020, Month.DECEMBER, 14), "Jahresrelease 2021, gesetzliche Änderungen und Regelüberleitung mit Überleitungstabellen")); //2.00.0
        versions.add(new Version(2, 0,1, VersionStageEn.FINAL, "Yellow-Shafted Flicker", LocalDate.of(2021, Month.JANUARY, 15), "Bugfixes 2.0.0 + zusätzliche Schlüssel 30 -Werte für KAIN-NAchrichten")); //2.00.1
        versions.add(new Version(2, 0,2, VersionStageEn.FINAL, "European Green Woodpecker", LocalDate.of(2021, Month.JANUARY, 29), "Bugfixes 2.0.1 + Anpassung Berechnung MD-Prüfquote an aktuelle vorgaben")); //2.00.2
        versions.add(new Version(2, 1,0, VersionStageEn.FINAL, "Pileated Woodpecker", LocalDate.of(2021, Month.JUNE, 04), "Erweiterungen, Kundenwünsche, Bugfixes")); //2.1.0
        versions.add(new Version(2, 2,0, VersionStageEn.FINAL, "Red-Crowned Woodpecker", LocalDate.of(2021, Month.SEPTEMBER, 30), "Fallzusammenführung, Interne Kundenwünsche")); //2.2.0
        versions.add(new Version(3, 0,0, VersionStageEn.FINAL, "Chaffinch", LocalDate.of(2021, Month.DECEMBER, 20), "Jahresrelease 2021, gesetzliche Änderungen und Regelüberleitung mit Überleitungstabellen")); //3.0.0
        versions.add(new Version(3, 0,1, VersionStageEn.FINAL, "Goldfinch", LocalDate.of(2022, Month.JANUARY, 30), "Jahresrelease 2021, gesetzliche Änderungen und Regelüberleitung mit Überleitungstabellen, Nachzertifizierung des DRGGroupers 2022")); //3.0.1
        versions.add(new Version(3, 0,2, VersionStageEn.FINAL, "Hawfinch", LocalDate.of(2022, Month.MARCH, 30), "INKA und KAIN Erweiterungen, Anzeigen und Suchen nach Strafzehlungen")); //3.0.2
        versions.add(new Version(3, 0,3, VersionStageEn.FINAL, "Greenfinch", LocalDate.of(2022, Month.SEPTEMBER, 6), "DRG Initial immer anzeigen")); //3.0.2
        versions.add(new Version(4, 0,0, VersionStageEn.FINAL, "Uhu", LocalDate.of(2022, Month.DECEMBER, 15), "Jahresrelease 2023")); //4.0.0
        versions.add(new Version(4, 1,0, VersionStageEn.FINAL, "Tawny Owl", LocalDate.of(2023, Month.JUNE, 15), "KAIN Key, Eingangsdatum, GDV")); //4.0.0
        versions.add(new Version(4, 1,1, VersionStageEn.FINAL, "Great Grey Owl", LocalDate.of(2023, Month.JULY, 27), "KAIN Keys")); //4.0.0
        return versions;
    }

    private static Version findRecentVersion(final boolean pHasToBePublished) {
        Version recentVersion = null;
        for (Version version : VERSIONS) {
            if ((recentVersion == null || recentVersion.compareTo(version) < 0)
                    && (!pHasToBePublished || version.getDate() != null)) {
                recentVersion = version;
            }
        }
        return recentVersion;
    }

//    public static void main(String[] args) {
//        System.out.println("Published Version: " + VersionHistory.RECENT_PUBLISHED_VERSION);
//        System.out.println("Recent Version: " + VersionHistory.RECENT_VERSION);
//        System.out.println("All Versions:");
//        for (Version version : getVersions()) {
//            System.out.println(version);
//        }
//    }
}
