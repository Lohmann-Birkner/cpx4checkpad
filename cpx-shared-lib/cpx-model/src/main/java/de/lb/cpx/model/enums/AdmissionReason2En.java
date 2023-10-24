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

public enum AdmissionReason2En implements CpxEnumInterface<String> {

    ar201("01", 1, Lang.ADMISSION_REASON_2_REGULAR_CASE),
    ar202("02", 2, Lang.ADMISSION_REASON_2_ACCIDENT_TYPE_WORK_COMMUTING_AND_WORK_RELATED_DISEASE),
    ar203("03", 3, Lang.ADMISSION_REASON_2_ACCIDENT_TYPE_TRAFFIC_SPORT_OTHER),
    ar204("04", 4, Lang.ADMISSION_REASON_2_INDICATION_OF_EXTERNAL_VIOLENCE),
    ar206("06", 6, Lang.ADMISSION_REASON_2_WAR_INVALIDS_AND_DISABLED_PEOPLE),
    ar207("07", 7, Lang.ADMISSION_REASON_2_EMERGENCY_CASE),
    ar221("21", 21, Lang.ADMISSION_REASON_2_COST_UNIT_REGULAR_CASE),
    ar222("22", 22, Lang.ADMISSION_REASON_2_COST_UNIT_CHANGE_ACCIDENT_TYPE_WORK_COMMUTING_AND_WORK_RELATED_DISEASE),
    ar223("23", 23, Lang.ADMISSION_REASON_2_COST_UNIT_CHANGE_ACCIDENT_TYPE_TRAFFIC_SPORT_OTHER),
    ar224("24", 24, Lang.ADMISSION_REASON_2_COST_UNIT_CHANGE_INDICATION_OF_EXTERNAL_VIOLENCE),
    ar226("26", 26, Lang.ADMISSION_REASON_2_COST_UNIT_CHANGE_WAR_INVALIDS_AND_DISABLED_PEOPLE),
    ar227("27", 27, Lang.ADMISSION_REASON_2_COST_UNIT_CHANGE_EMERGENCY_CASE),
    ar241("41", 41, Lang.ADMISSION_REASON_2_INTEGRATED_HEALTH_CARE_REGULAR_CASE),
    ar242("42", 42, Lang.ADMISSION_REASON_2_INTEGRATED_HEALTH_CARE_ACCIDENT_TYPE_WORK_COMMUTING_AND_WORK_RELATED_DISEASE),
    ar243("43", 43, Lang.ADMISSION_REASON_2_INTEGRATED_HEALTH_CARE_ACCIDENT_TYPE_TRAFFIC_SPORT_OTHER),
    ar244("44", 44, Lang.ADMISSION_REASON_2_INTEGRATED_HEALTH_CARE_INDICATION_OF_EXTERNAL_VIOLENCE),
    ar246("46", 46, Lang.ADMISSION_REASON_2_INTEGRATED_HEALTH_CARE_WAR_INVALIDS_AND_DISABLED_PEOPLE),
    ar247("47", 47, Lang.ADMISSION_REASON_2_INTEGRATED_HEALTH_CARE_EMERGENCY_CASE);

    private final String langKey;
    private final String id;
    private final int idInt;

    private AdmissionReason2En(final String id, final int idInt, final String langKey) {
        this.id = id;
        this.idInt = idInt;
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
        return id;
    }

    @Override
    public int getIdInt() {
        return idInt;
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
    public static AdmissionReason2En findById(final String pId) {
        return AdmissionReason2EnMap.getInstance().get(pId);
    }

    public static AdmissionReason2En findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (AdmissionReason2En val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

}

final class AdmissionReason2EnMap extends AbstractCpxEnumMap<AdmissionReason2En, String> {

    private static final AdmissionReason2EnMap INSTANCE;

    static {
        INSTANCE = new AdmissionReason2EnMap();
    }

    protected AdmissionReason2EnMap() {
        super(AdmissionReason2En.class);
    }

    public static AdmissionReason2EnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public AdmissionReason2En[] getValues() {
        return AdmissionReason2En.values();
    }

}
