/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.scheduled_ejb;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author niemeier
 */
public final class InsShortDetector {

    private static final Map<String, List<String>> SHORTINGSMAP = new LinkedHashMap<>();

    private InsShortDetector() {
        //
    }

    static {
        SHORTINGSMAP.put("SBK", Arrays.asList("SBK", "Siemens")); //check Siemens Betriebskasse before other Betriebskrankenkassen!
        SHORTINGSMAP.put("DAK", Arrays.asList("DAK", "VdAK", "Deutsche Angestellten- Krankenkasse"));
        SHORTINGSMAP.put("IKK", Arrays.asList("IKK", "VIKK", "mitteldeutscheIKK", "Innungskrankenkasse", "Innungskrankenkassen"));
        SHORTINGSMAP.put("AOK", Arrays.asList("AOK", "AOKfN", "AOKHessen"));
        SHORTINGSMAP.put("Knappschaft", Arrays.asList("Knappschaft", "Knappschft", "Knappschafts", "Bundesknappschaft"));
        SHORTINGSMAP.put("KEH", Arrays.asList("KEH"));
        SHORTINGSMAP.put("HZK", Arrays.asList("HZK"));
        SHORTINGSMAP.put("KKH", Arrays.asList("KKH", "Kaufmännische Krankenkasse", "Kaufm Krankenkasse"));
        SHORTINGSMAP.put("HaMü", Arrays.asList("HaMü", "Hamburg Münchener", "Hamburg-Münchener"));
        SHORTINGSMAP.put("Barmer", Arrays.asList("Barmer", "BARMER")); //Pay attention: The names had changed to Barmer GEK!
        SHORTINGSMAP.put("GEK", Arrays.asList("GEK", "Gmünder ErsatzKasse", "Gmünder Ersatzkasse")); //Pay attention: The names had changed to Barmer GEK!
        SHORTINGSMAP.put("TKK", Arrays.asList("TKK", "Techniker Krankenkasse", "Techniker-Krankenkasse"));
        SHORTINGSMAP.put("HEK", Arrays.asList("HEK", "Hanseatische Ersatzkasse", "Hanseatische Krankenkasse", "Hanseatischen Krankenkasse", "HANSEATISCHE KRANKENKASSE"));
        SHORTINGSMAP.put("Sozialamt", Arrays.asList("Sozialamt"));
        SHORTINGSMAP.put("BKK", Arrays.asList("BKK", "Bkk", "CityBKK", "CITYBKK", "neue bkk", "ISBKK", "ktpBKK", "Betriebskrankenkasse", "Betriebskrankenkassen", "Betriebskranken- kassen", "Betiebskrankenkasse", "Betriebskrankenkenkasse", "Betriebskrankenkassse", "BetriebskrankenkasseKC", "Betriebs- krankenkassen")); //Last check!
    }

    public static String detect(final String pInsuranceName) {
        final String insName = pInsuranceName == null ? "" : pInsuranceName.trim();
        if (insName.isEmpty()) {
            return null;
        }

//        if (pInsurance == null) {
//            LOG.log(Level.WARNING, "pInsurance is null!");
//            return "";
//        }
        //final String insIdentClass = pInsurance.getInscIdentClassEn() == null ? "" : pInsurance.getInscIdentClassEn().getId();
//        final String insName = pInsurance.getInscName() == null ? "" : pInsurance.getInscName().trim();
//        String insShort = pInsurance.getInscShort() == null ? "" : pInsurance.getInscShort().trim();
//        if (!insShort.isEmpty()) {
//            LOG.log(Level.FINE, "Insurance '" + insName + "' has already assigned short name '" + insShort + "'");
//            return insShort;
//        }
//        final String insIdentClass = pInsurance.getInscIdentClassEn() == null ? "" : pInsurance.getInscIdentClassEn().getIdStr();
//        final String insIdent = pInsurance.getInscIdent() == null ? "" : pInsurance.getInscIdent().trim();
        //Column of insurance type is missing or empty, try to detect it by myself
//        if ("12".equalsIgnoreCase(insIdentClass) || insIdent.startsWith("12")) { //second condition is somewhat redundant...
//            insShort = "BG"; //Berufsgenossenschaft
//        }
//
//        if (insShort.isEmpty()) {
        String insShort = "";
        Iterator<Map.Entry<String, List<String>>> it = SHORTINGSMAP.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            String insShortCandidate = entry.getKey();
            List<String> needles = entry.getValue();
            for (String needle : needles) {
                if (needle == null || needle.trim().isEmpty()) {
                    continue;
                }
                if (insName.equals(needle)) {
                    insShort = insShortCandidate;
                    break;
                }
                if (insName.startsWith(needle)) {
                    insShort = insShortCandidate;
                    break;
                }
                if (insName.endsWith(" " + needle)) {
                    insShort = insShortCandidate;
                    break;
                }
                if (insName.endsWith("/" + needle)) {
                    insShort = insShortCandidate;
                    break;
                }
                if (insName.endsWith("-" + needle)) {
                    insShort = insShortCandidate;
                    break;
                }
                if (insName.contains("-" + needle + "-")) {
                    insShort = insShortCandidate;
                    break;
                }
                if ((" " + insName + " ").contains(" " + needle + " ")) {
                    insShort = insShortCandidate;
                    break;
                }
                if ((" " + insName + " ").contains(" " + needle + "-")) {
                    insShort = insShortCandidate;
                    break;
                }
                if ((" " + insName + " ").contains("-" + needle + " ")) {
                    insShort = insShortCandidate;
                    break;
                }
                if ((" " + insName + " ").contains("/" + needle + " ")) {
                    insShort = insShortCandidate;
                    break;
                }
                if ((" " + insName + " ").contains(" " + needle + "/")) {
                    insShort = insShortCandidate;
                    break;
                }
            }
            if (!insShort.isEmpty()) {
                break;
            }
        }
//        }
        return insShort;
    }

}
