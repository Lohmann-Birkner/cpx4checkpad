/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
//package de.lb.cpx.model.enums;
//
//import de.lb.cpx.exceptions.CpxIllegalArgumentException;
//import de.lb.cpx.shared.lang.Lang;
//import de.lb.cpx.shared.lang.Translation;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// *
// * @author niemeier
// */
//public enum InsShortEn implements CpxEnumInterface<String> {
//    AOK("AOK", Lang.INS_SHORT_AOK),
//    Barmer("Barmer", Lang.INS_SHORT_BARMER),
//    BG("BG", Lang.INS_SHORT_BG),
//    BKK("BKK", Lang.INS_SHORT_BKK),
//    DAK("DAK", Lang.INS_SHORT_DAK),
//    GEK("GEK", Lang.INS_SHORT_GEK),
//    HaMue("HaMü", Lang.INS_SHORT_HA_MUE),
//    HEK("HEK", Lang.INS_SHORT_HEK),
//    HZK("HZK", Lang.INS_SHORT_HZK),
//    IKK("IKK", Lang.INS_SHORT_IKK),
//    KEH("KEH", Lang.INS_SHORT_KEH),
//    KKH("KKH", Lang.INS_SHORT_KKH),
//    Knappschaft("Knappschaft", Lang.INS_SHORT_KNAPPSCHAFT),
//    SBK("SBK", Lang.INS_SHORT_SBK),
//    Sozialamt("Sozialamt", Lang.INS_SHORT_SOZIALAMT),
//    TKK("TKK", Lang.INS_SHORT_TKK);
//
//    private final String id;
//    private final String langKey;
//
//    private InsShortEn(String id, String langKey) {
//        this.id = id;
//        this.langKey = langKey;
//    }
//
//    @Override
//    public String getLangKey() {
//        return langKey;
//    }
//
//    @Override
//    public String getId() {
//        return id;
//    }
//
//    @Override
//    public String getIdStr() {
//        return id;
//    }
//
//    @Override
//    public int getIdInt() {
//        return Integer.parseInt(id);
//    }
//
//    /*
//  @Override
//  public String toString(final CpxLanguageInterface cpxLanguage) {
//    return this.getViewId() + " - " + cpxLanguage.get(langKey);
//  }
//     */
//    @Override
//    public String toString() {
//        return CpxEnumInterface.toStaticString(getViewId(), getLangKey());
//    }
//
//    @Override
//    public String getViewId() {
//        return String.valueOf(id);
//    }
//
//    @Override
//    public boolean isViewRelevant() {
//        return true;
//    }
//
////    @Override
////    public CpxEnumInterface getEnum(String value) {
////        try {
////            return CpxEnumInterface.findEnum(values(), value);
////        } catch (IllegalArgumentException ex) {
////            Logger.getLogger(IdentClassEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
////        }
////        return null;
////    }
//    public static InsShortEn getStaticEnum(final String pValue) throws CpxIllegalArgumentException {
//        String value = (pValue == null) ? "" : pValue.trim();
////        if (value.length() > 2) {
////            value = value.substring(0, 2);
////        }
////        //System.out.println("IDENT: " + value + ": " + pValue);
////        if (value.equalsIgnoreCase("98") || value.equalsIgnoreCase("99")) {
////            value = "97"; //97-99: Reserved for internal administrative-free use in the institutions
////        }
//        return (InsShortEn) CpxEnumInterface.findEnum(InsShortEn.values(), value);
//    }
//
//    @Override
//    public Translation getTranslation() {
//        return getTranslation((Object[]) null);
//    }
//
//    @Override
//    public Translation getTranslation(Object... pParams) {
//        return Lang.get(getLangKey(), pParams);
//    }
//
//    public static InsShortEn findById(final String pId) {
//        if (pId == null || pId.isEmpty()) {
//            return null;
//        }
//        return InsShortEnMap.getInstance().get(pId);
//    }
//
//    public static String detectInsShort(final String pInsuranceName) {
//        return InsShortDetector.detect(pInsuranceName);
//    }
//
//}
//
//final class InsShortDetector {
//
//    private static final Map<String, List<String>> SHORTINGSMAP = new LinkedHashMap<>();
//
//    private InsShortDetector() {
//        //
//    }
//
//    static {
//        SHORTINGSMAP.put("SBK", Arrays.asList("SBK", "Siemens")); //check Siemens Betriebskasse before other Betriebskrankenkassen!
//        SHORTINGSMAP.put("DAK", Arrays.asList("DAK", "VdAK", "Deutsche Angestellten- Krankenkasse"));
//        SHORTINGSMAP.put("IKK", Arrays.asList("IKK", "VIKK", "mitteldeutscheIKK", "Innungskrankenkasse", "Innungskrankenkassen"));
//        SHORTINGSMAP.put("AOK", Arrays.asList("AOK", "AOKfN", "AOKHessen"));
//        SHORTINGSMAP.put("Knappschaft", Arrays.asList("Knappschaft", "Knappschft", "Knappschafts", "Bundesknappschaft"));
//        SHORTINGSMAP.put("KEH", Arrays.asList("KEH"));
//        SHORTINGSMAP.put("HZK", Arrays.asList("HZK"));
//        SHORTINGSMAP.put("KKH", Arrays.asList("KKH", "Kaufmännische Krankenkasse", "Kaufm Krankenkasse"));
//        SHORTINGSMAP.put("HaMü", Arrays.asList("HaMü", "Hamburg Münchener", "Hamburg-Münchener"));
//        SHORTINGSMAP.put("Barmer", Arrays.asList("Barmer", "BARMER")); //Pay attention: The names had changed to Barmer GEK!
//        SHORTINGSMAP.put("GEK", Arrays.asList("GEK", "Gmünder ErsatzKasse", "Gmünder Ersatzkasse")); //Pay attention: The names had changed to Barmer GEK!
//        SHORTINGSMAP.put("TKK", Arrays.asList("TKK", "Techniker Krankenkasse", "Techniker-Krankenkasse"));
//        SHORTINGSMAP.put("HEK", Arrays.asList("HEK", "Hanseatische Ersatzkasse", "Hanseatische Krankenkasse", "Hanseatischen Krankenkasse", "HANSEATISCHE KRANKENKASSE"));
//        SHORTINGSMAP.put("Sozialamt", Arrays.asList("Sozialamt"));
//        SHORTINGSMAP.put("BKK", Arrays.asList("BKK", "Bkk", "CityBKK", "CITYBKK", "neue bkk", "ISBKK", "ktpBKK", "Betriebskrankenkasse", "Betriebskrankenkassen", "Betriebskranken- kassen", "Betiebskrankenkasse", "Betriebskrankenkenkasse", "Betriebskrankenkassse", "BetriebskrankenkasseKC", "Betriebs- krankenkassen")); //Last check!
//    }
//
//    public static String detect(final String pInsuranceName) {
//        final String insName = pInsuranceName == null ? "" : pInsuranceName.trim();
//        if (insName.isEmpty()) {
//            return null;
//        }
//
////        if (pInsurance == null) {
////            LOG.log(Level.WARNING, "pInsurance is null!");
////            return "";
////        }
//        //final String insIdentClass = pInsurance.getInscIdentClassEn() == null ? "" : pInsurance.getInscIdentClassEn().getId();
////        final String insName = pInsurance.getInscName() == null ? "" : pInsurance.getInscName().trim();
////        String insShort = pInsurance.getInscShort() == null ? "" : pInsurance.getInscShort().trim();
////        if (!insShort.isEmpty()) {
////            LOG.log(Level.FINE, "Insurance '" + insName + "' has already assigned short name '" + insShort + "'");
////            return insShort;
////        }
////        final String insIdentClass = pInsurance.getInscIdentClassEn() == null ? "" : pInsurance.getInscIdentClassEn().getIdStr();
////        final String insIdent = pInsurance.getInscIdent() == null ? "" : pInsurance.getInscIdent().trim();
//        //Column of insurance type is missing or empty, try to detect it by myself
////        if ("12".equalsIgnoreCase(insIdentClass) || insIdent.startsWith("12")) { //second condition is somewhat redundant...
////            insShort = "BG"; //Berufsgenossenschaft
////        }
////
////        if (insShort.isEmpty()) {
//        String insShort = "";
//        Iterator<Map.Entry<String, List<String>>> it = SHORTINGSMAP.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, List<String>> entry = it.next();
//            String insShortCandidate = entry.getKey();
//            List<String> needles = entry.getValue();
//            for (String needle : needles) {
//                if (needle == null || needle.trim().isEmpty()) {
//                    continue;
//                }
//                if (insName.equals(needle)) {
//                    insShort = insShortCandidate;
//                    break;
//                }
//                if (insName.startsWith(needle)) {
//                    insShort = insShortCandidate;
//                    break;
//                }
//                if (insName.endsWith(" " + needle)) {
//                    insShort = insShortCandidate;
//                    break;
//                }
//                if (insName.endsWith("/" + needle)) {
//                    insShort = insShortCandidate;
//                    break;
//                }
//                if (insName.endsWith("-" + needle)) {
//                    insShort = insShortCandidate;
//                    break;
//                }
//                if (insName.contains("-" + needle + "-")) {
//                    insShort = insShortCandidate;
//                    break;
//                }
//                if ((" " + insName + " ").contains(" " + needle + " ")) {
//                    insShort = insShortCandidate;
//                    break;
//                }
//                if ((" " + insName + " ").contains(" " + needle + "-")) {
//                    insShort = insShortCandidate;
//                    break;
//                }
//                if ((" " + insName + " ").contains("-" + needle + " ")) {
//                    insShort = insShortCandidate;
//                    break;
//                }
//                if ((" " + insName + " ").contains("/" + needle + " ")) {
//                    insShort = insShortCandidate;
//                    break;
//                }
//                if ((" " + insName + " ").contains(" " + needle + "/")) {
//                    insShort = insShortCandidate;
//                    break;
//                }
//            }
//            if (!insShort.isEmpty()) {
//                break;
//            }
//        }
////        }
//        return insShort;
//    }
//
//}
//
//final class InsShortEnMap extends AbstractCpxEnumMap<InsShortEn, String> {
//
//    private static final InsShortEnMap INSTANCE;
//
//    static {
//        INSTANCE = new InsShortEnMap();
//    }
//
//    protected InsShortEnMap() {
//        super(InsShortEn.class);
//    }
//
//    public static InsShortEnMap getInstance() {
//        return INSTANCE;
//    }
//
//    @Override
//    public InsShortEn[] getValues() {
//        return InsShortEn.values();
//    }
//
//}