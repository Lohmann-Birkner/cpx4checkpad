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
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author gerschmann
 */
public enum GrouperMdcOrSkEn implements CpxEnumInterface<String> {

    pre("PRE", Lang.GROUPER_MDC_OR_SK_MDC_PRE), //Sonderfälle (Beatmungsfälle, Transplantationen u. ä.)
    mdc01("01", Lang.GROUPER_MDC_OR_SK_MDC_01), //, 'Krankheiten und Störungen des Nervensystems', 
    mdc02("02", Lang.GROUPER_MDC_OR_SK_MDC_02), //'Krankheiten und Störungen des Auges', 
    mdc03("03", Lang.GROUPER_MDC_OR_SK_MDC_03), //der Nase, des Mundes und des Halses', 
    mdc04("04", Lang.GROUPER_MDC_OR_SK_MDC_04), //'Krankheiten und Störungen der Atmungsorgane', 
    mdc05("05", Lang.GROUPER_MDC_OR_SK_MDC_05), //'Krankheiten und Störungen des Kreislaufsystems', 
    mdc06("06", Lang.GROUPER_MDC_OR_SK_MDC_06), //'Krankheiten und Störungen der Verdauungsorgane',  
    mdc07("07", Lang.GROUPER_MDC_OR_SK_MDC_07), //'Krankheiten und Störungen an hepatobiliärem System und Pankreas' \n" +
    mdc08("08", Lang.GROUPER_MDC_OR_SK_MDC_08), //\" 'Krankheiten und Störungen an Muskel-Skelett-System und Bindegewebe' \n" +
    mdc09("09", Lang.GROUPER_MDC_OR_SK_MDC_09), //\" 'Krankheiten und Störungen an Haut, Unterhaut und Mamma' \n" +
    mdc10("10", Lang.GROUPER_MDC_OR_SK_MDC_10), //\" 'Endokrine, Ernährungs- und Stoffwechselkrankheiten' \n" +
    mdc11("11", Lang.GROUPER_MDC_OR_SK_MDC_11), //\" 'Krankheiten und Störungen der Harnorgane' \n" +
    mdc12("12", Lang.GROUPER_MDC_OR_SK_MDC_12), //\" 'Krankheiten und Störungen der männlichen Geschlechtsorgane' \n" +
    mdc13("13", Lang.GROUPER_MDC_OR_SK_MDC_13), //\" 'Krankheiten und Störungen der weiblichen Geschlechtsorgane' \n" +
    mdc14("14", Lang.GROUPER_MDC_OR_SK_MDC_14), //\" 'Schwangerschaft, Geburt und Wochenbett' \n" +
    mdc15("15", Lang.GROUPER_MDC_OR_SK_MDC_15), //\" 'Neugeborene' \n" +
    mdc16("16", Lang.GROUPER_MDC_OR_SK_MDC_16), //\" 'Krankheiten des Blutes, der blutbildenden Organe und des Immunsystems' \n" +
    mdc17("17", Lang.GROUPER_MDC_OR_SK_MDC_17), //\" 'Hämatologische und solide Neubildungen' \n" +
    mdc18A("18A", Lang.GROUPER_MDC_OR_SK_MDC_18_A), //\" 'HIV' \n" +
    mdc18B("18B", Lang.GROUPER_MDC_OR_SK_MDC_18_B), //\" 'Infektiöse und parasitäre Krankheiten' \n" +
    mdc19("19", Lang.GROUPER_MDC_OR_SK_MDC_19), //\" 'Psychische Krankheiten und Störungen' \n" +
    mdc20("20", Lang.GROUPER_MDC_OR_SK_MDC_20), //\" 'Alkohol- und Drogengebrauch und alkohol- und drogeninduzierte psychische Störungen' \n" +
    mdc21A("21A", Lang.GROUPER_MDC_OR_SK_MDC_21_A), //\" 'Polytrauma' \n" +
    mdc21B("21B", Lang.GROUPER_MDC_OR_SK_MDC_21_B), //\" 'Verletzungen, Vergiftungen und toxische Wirkungen von Drogen und Medikamenten' \n" +
    mdc22("22", Lang.GROUPER_MDC_OR_SK_MDC_22), //\" 'Verbrennungen' \n" +
    mdc23("23", Lang.GROUPER_MDC_OR_SK_MDC_23), //\" 'Faktoren, die den Gesundheitszustand beeinflussen, und andere Inanspruchnahme des Gesundheitswesens'];
    mdc24("24", Lang.GROUPER_MDC_OR_SK_MDC_24), //\" 'Sonstige DRGs'];
    mdc25("25", Lang.GROUPER_MDC_OR_SK_MDC_25), //\" 'Sonstige DRGs'];
    TKJ("TKJ", Lang.GROUPER_MDC_OR_SK_INFANT_DAY_CARE), //'Kinder- und Jugendpsychiatrie, teilstationär ', 
    TPO("TPO", Lang.GROUPER_MDC_OR_SK_PSYCHOSOMATIC_DAY_CARE), //'Psychosomatik, teilstationär ', 
    TPY("TPY", Lang.GROUPER_MDC_OR_SK_PSYCHO_DAY_CARE), //'Psychiatrie, teilstationär', 
    PKJ("PKJ", Lang.GROUPER_MDC_OR_SK_INFANT_INPATIENT_CARE), //'Kinder- und Jugendpsychiatrie, vollstationär ', 
    PSO("PSO", Lang.GROUPER_MDC_OR_SK_PSYCHOSOMATIC_INPATIENT_CARE), //'Psychosomatik, vollstationär ', 
    PSY("PSY", Lang.GROUPER_MDC_OR_SK_PSYCHO_INPATIENT_CARE);//'Psychiatrie, vollstationär '];//'Psychiatrie, vollstationär '];

    private final String id;
    private final String langKey;

    private GrouperMdcOrSkEn(final String id, final String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

    public static GrouperMdcOrSkEn getValue2Id(String group) {

        for (GrouperMdcOrSkEn en : values()) {
            if (en.getId().equals(group)) {
                return en;
            }
        }
        return null;

    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getIdStr() {
        return id;
    }

    @Override
    public int getIdInt() {
        throw new UnsupportedOperationException("id as integer is not supported for " + getClass().getSimpleName());
    }

//    public int getIntInd() {
//        return Integer.parseInt(id);
//    }

    /*
  @Override
  public String toString(final CpxLanguageInterface cpxLanguage) {
    return this.getViewId() + " - " + cpxLanguage.get(langKey);
  }
     */
    @Override
    public String toString() {
        return CpxEnumInterface.toStaticString(getId(), getLangKey());
    }

    @Override
    public String getViewId() {
        //return String.valueOf(id);
        return name();
    }

    @Override
    public boolean isViewRelevant() {
        return true;
    }

//    @Override
//    public CpxEnumInterface getEnum(String value) {
//        try {
//            return CpxEnumInterface.findEnum(values(), value);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(GrouperMdcOrSkEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
//    public static GrouperMdcOrSkEn getStaticEnum(final String pValue) throws CpxIllegalArgumentException {
//        return (GrouperMdcOrSkEn) CpxEnumInterface.findEnum(GrouperMdcOrSkEn.values(), pValue);
//    }
    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    public static GrouperMdcOrSkEn findById(final String pId) {
        String id = (pId == null) ? null : pId.trim();
        if (id == null) {
            return null;
        }
        id = id.toUpperCase();
        if (id.equalsIgnoreCase("\"\"")) {
            return null;
        }
        if (id.isEmpty()) {
            return null;
        }
        //for GrouperMdcOrSk
        if (id.equalsIgnoreCase("PRÄ")) {
            //id = "PRE";
            return pre;
        }
        if (id.startsWith("MDC")) {
            id = id.substring(3).trim();
        }
        return GrouperMdcOrSkEnMap.getInstance().get(id);
    }

    public static GrouperMdcOrSkEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (GrouperMdcOrSkEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

}

final class GrouperMdcOrSkEnMap extends AbstractCpxEnumMap<GrouperMdcOrSkEn, String> {

    private static final GrouperMdcOrSkEnMap INSTANCE;

    static {
        INSTANCE = new GrouperMdcOrSkEnMap();
    }

    protected GrouperMdcOrSkEnMap() {
        super(GrouperMdcOrSkEn.class);
    }

    public static GrouperMdcOrSkEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public GrouperMdcOrSkEn[] getValues() {
        return GrouperMdcOrSkEn.values();
    }

}
