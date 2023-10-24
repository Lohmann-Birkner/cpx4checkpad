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
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author niemeier
 */
public enum AcgAgeBandEn implements CpxEnumInterface<String> {
    AB0004("00-04", 0, 4, Lang.ACG_AGE_BAND_00_04),
    AB0511("05-11", 5, 11, Lang.ACG_AGE_BAND_05_11),
    AB1217("12-17", 12, 17, Lang.ACG_AGE_BAND_12_17),
    AB1834("18-34", 18, 34, Lang.ACG_AGE_BAND_18_34),
    AB3544("35-44", 35, 44, Lang.ACG_AGE_BAND_35_44),
    AB4554("45-54", 45, 54, Lang.ACG_AGE_BAND_45_54),
    AB5564("55-64", 55, 64, Lang.ACG_AGE_BAND_55_64),
    AB6569("65-69", 65, 69, Lang.ACG_AGE_BAND_65_69),
    AB7074("70-74", 70, 74, Lang.ACG_AGE_BAND_70_74),
    AB7579("75-79", 75, 79, Lang.ACG_AGE_BAND_75_79),
    AB8084("80-84", 80, 84, Lang.ACG_AGE_BAND_80_84),
    AB85P("85+", 85, null, Lang.ACG_AGE_BAND_85_P);

    public final String id;
    public final String langKey;
    public final int lowerBound;
    public final Integer upperBound;

    private AcgAgeBandEn(final String pId, final int pLowerBound, final Integer pUpperBound, final String langKey) {
        this.id = pId;
        this.lowerBound = pLowerBound;
        this.upperBound = pUpperBound;
        this.langKey = langKey;
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
        return String.valueOf(id);
    }

    @Override
    public int getIdInt() {
        throw new UnsupportedOperationException("id as integer is not supported for " + getClass().getSimpleName());
    }

    /*
  @Override
  public String toString(final CpxLanguageInterface cpxLanguage) {
    return this.getViewId() + " - " + cpxLanguage.get(langKey);
  }
     */
    @Override
    public String toString() {
        return CpxEnumInterface.toStaticString(getViewId(), getLangKey());
    }

    @Override
    public String getViewId() {
        return String.valueOf(id);
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
//            Logger.getLogger(AdmissionReason2En.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

//    public static AdmissionReason2En findById(final String pId) {
//        String id = (pId == null) ? "" : pId.trim().toLowerCase();
//        if (id.isEmpty()) {
//            return null;
//        }
//        for (AdmissionReason2En val : values()) {
//            if (val == null) {
//                continue;
//            }
//            if (id.equalsIgnoreCase(val.getId())) {
//                return val;
//            }
//        }
//        return null;
//    }
    public static AcgAgeBandEn findById(final String pId) {
        return AcgAgeBandEnMap.getInstance().get(pId);
    }

    public static AcgAgeBandEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (AcgAgeBandEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

}

final class AcgAgeBandEnMap extends AbstractCpxEnumMap<AcgAgeBandEn, String> {

    private static final AcgAgeBandEnMap INSTANCE;

    static {
        INSTANCE = new AcgAgeBandEnMap();
    }

    protected AcgAgeBandEnMap() {
        super(AcgAgeBandEn.class);
    }

    public static AcgAgeBandEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public AcgAgeBandEn[] getValues() {
        return AcgAgeBandEn.values();
    }

}
