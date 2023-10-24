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

public enum DischargeReasonEn implements CpxEnumInterface<String> {

    dr01("01", 1, Lang.DISCHARGE_REASON_COMPLETED_TREATMENT_REGULARLY),
    dr02("02", 2, Lang.DISCHARGE_REASON_TREATMENT_TERMINATE_REGULARLY_PROVIDED_INPATIENT_TREATMENT),
    dr03("03", 3, Lang.DISCHARGE_REASON_COMPLETED_TREATMENT_FOR_OTHER_REASONS),
    dr04("04", 4, Lang.DISCHARGE_REASON_COMPLETED_TREATMENT_AGAINST_MEDICAL_ADVICE),
    dr05("05", 5, Lang.DISCHARGE_REASON_CHANGES_IN_RESPONSIBILITIES_OF_PAYERS),
    dr06("06", 6, Lang.DISCHARGE_REASON_TRANSFER_TO_ANOTHER_HOSPITAL),
    dr07("07", 7, Lang.DISCHARGE_REASON_DEATH),
    dr08("08", 8, Lang.DISCHARGE_REASON_LAYING_TO_ANOTHER_HOSPITAL_COOPERATION),
    dr09("09", 9, Lang.DISCHARGE_REASON_DISMISSAL_IN_A_REHABILITATION_FACILITY),
    dr10("10", 10, Lang.DISCHARGE_REASON_DISMISSAL_IN_A_NURSING_FACILITY),
    dr11("11", 11, Lang.DISCHARGE_REASON_DISMISSAL_IN_A_HOSPICE),
    dr12("12", 12, Lang.DISCHARGE_REASON_INTERNAL_RELOCATION),
    dr13("13", 13, Lang.DISCHARGE_REASON_EXTERNAL_INSTALLATION_FOR_PSYCHIATRIC_TREATMENT),
    dr14("14", 14, Lang.DISCHARGE_REASON_COMPLETED_TREATMENT_FOR_OTHER_REASONS_INPATIENT_TREATMENT),
    dr15("15", 15, Lang.DISCHARGE_REASON_COMPLETED_TREATMENT_AGAINST_MEDICAL_ADVICE_INPATIENT_TREATMENT),
    dr16("16", 16, Lang.DISCHARGE_REASON_EXTERNAL_TRANSFER_TO_ANOTHER_HOSPITAL_AND_SUBSEQUENT_REDEPLOYMENT_OR_INTERNAL_INSTALLATION_WITH_ALTERNATING_BETWEEN_THE_SCOPES),
    dr17("17", 17, Lang.DISCHARGE_REASON_INTERNAL_INSTALLATION_WITH_ALTERNATING_BETWEEN_THE_SCOPES),
    dr18("18", 18, Lang.DISCHARGE_REASON_RELOCATION),
    dr19("19", 19, Lang.DISCHARGE_REASON_DISMISSAL_BEFORE_RESUMING_WITH_RECLASSIFICATION),
    dr20("20", 20, Lang.DISCHARGE_REASON_DISMISSAL_BEFORE_RESUMING_WITH_RECLASSIFICATION_FOR_COMPLICATION),
    dr21("21", 21, Lang.DISCHARGE_REASON_DISMISSAL_AND_SUBSEQUENT_RECOVERY),
    dr22("22", 22, Lang.DISCHARGE_REASON_CASE_STATEMENTS_WHEN_SWITCHING_BETWEEN_INPATIENT_AND_DAYCARE_TREATMENT),
    dr23("23", 23, Lang.DISCHARGE_REASON_START_EXTERNAL_STAY_WITH_ABSENCE_THROUGH_MIDNIGHT),
    dr24("24", 24, Lang.DISCHARGE_REASON_TERMINATION_EXTERNAL_STAY_WITH_ABSENCE_THROUGH_MIDNIGHT),
    dr25("25", 25, Lang.DISCHARGE_REASON_DISMISSAL_AT_THE_END_OF_RECORDING_IN_THE_PREVIOUS_YEAR),
    dr26("26", 26, Lang.DISCHARGE_REASON_DISMISSAL_FOR_START_STATION_EQUIVALENT),
    dr27("27", 27, Lang.DISCHARGE_REASON_DISMISSAL_END_STATION_EQUIVALENT),
    dr28("28", 28, Lang.DISCHARGE_REASON_DISMISSAL_TRANSFER_WITH_BREATHING_1),
    dr29("29", 29, Lang.DISCHARGE_REASON_DISMISSAL_TRANSFER_WITH_BREATHING_2),
    dr30("30", 30, Lang.DISCHARGE_REASON_COMPLETED_WITH_TRANSFER_IN_TEMPORARY_CARE);//Behandlung regulär beendet, Überleitung in die Übergangspflege

    private final String langKey;
    private final String id;
    private final int idInt;

    private DischargeReasonEn(final String id, final int idInt, final String langKey) {
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
//            Logger.getLogger(DischargeReasonEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    /**
     * checks the current Discharge reason against the given DischargeReason2
     * and returns if the combination is valid or not
     *
     * @param pDisReason2 DischargeReason2 (DischargeReason34) to validate the
     * combination
     * @return if the combination is invalid
     */
    public boolean isInvalidForDischargeReason2(DischargeReason2En pDisReason2) {
        return (this.equals(dr06) && DischargeReason2En.dr201.equals(pDisReason2))
                || (this.equals(dr07) && DischargeReason2En.dr201.equals(pDisReason2))
                || (this.equals(dr07) && DischargeReason2En.dr202.equals(pDisReason2))
                || (this.equals(dr08) && DischargeReason2En.dr201.equals(pDisReason2))
                || (this.equals(dr09) && DischargeReason2En.dr201.equals(pDisReason2))
                || (this.equals(dr10) && DischargeReason2En.dr201.equals(pDisReason2))
                || (this.equals(dr11) && DischargeReason2En.dr201.equals(pDisReason2))
                || (this.equals(dr12) && DischargeReason2En.dr201.equals(pDisReason2))
                || (this.equals(dr17) && DischargeReason2En.dr201.equals(pDisReason2))
                || (this.equals(dr18) && DischargeReason2En.dr201.equals(pDisReason2))
                || (this.equals(dr21) && DischargeReason2En.dr201.equals(pDisReason2));
    }

    public static DischargeReasonEn findById(final String pId) {
        String id = (pId == null) ? "" : pId.trim().toLowerCase();
        if (id.isEmpty()) {
            return null;
        }
        if (id.length() == 1) {
            id = "0" + id;
        }
        for (DischargeReasonEn val : values()) {
            if (val == null) {
                continue;
            }
            if (id.equalsIgnoreCase(val.getId())) {
                return val;
            }
        }
        return null;
    }

    public static DischargeReasonEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (DischargeReasonEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

    public static boolean isTransfer(DischargeReasonEn pDisReasonEn){
//        sep == 6 || sep == 8 || sep == 13|| sep == 16 || sep == 17 || sep == 18

        return pDisReasonEn.equals(dr06) 
                || pDisReasonEn.equals(dr08) 
                || pDisReasonEn.equals(dr13) 
                || pDisReasonEn.equals(dr16) 
                || pDisReasonEn.equals(dr17) 
                || pDisReasonEn.equals(dr18) ;
    }
//    public static DischargeReasonEn findById(final String pId) {
//        return DischargeReasonEnMap.getInstance().get(pId);
//    }
}

final class DischargeReasonEnMap extends AbstractCpxEnumMap<DischargeReasonEn, String> {

    private static final DischargeReasonEnMap INSTANCE;

    static {
        INSTANCE = new DischargeReasonEnMap();
    }

    protected DischargeReasonEnMap() {
        super(DischargeReasonEn.class);
    }

    public static DischargeReasonEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public DischargeReasonEn[] getValues() {
        return DischargeReasonEn.values();
    }

}
