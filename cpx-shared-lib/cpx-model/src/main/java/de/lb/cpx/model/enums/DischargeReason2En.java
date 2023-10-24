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

public enum DischargeReason2En implements CpxEnumInterface<String> {

    dr201("1", 1, Lang.DISCHARGE_REASON_2_ABLE_TO_WORK),
    dr202("2", 2, Lang.DISCHARGE_REASON_2_UNABLE_TO_WORK),
    dr203("3", 3, Lang.DISCHARGE_REASON_2_ABLE_TO_WORK_NO_NEED_RESPIRATION_WEANING),
    dr204("4", 4, Lang.DISCHARGE_REASON_2_ABLE_TO_WORK_NEED_RESPIRATION_WEANING),
    dr205("5", 5, Lang.DISCHARGE_REASON_2_ABLE_TO_WORK_VENTILATED_NO_PRESCRIPTION),
    dr206("6", 6, Lang.DISCHARGE_REASON_2_ABLE_TO_WORK_VENTILATED_NO_SPECIFIED),
    dr209("9", 9, Lang.DISCHARGE_REASON_2_NOT_SPECIFIED);

    private final String langKey;
    private final String id;
    private final int idInt;

    private DischargeReason2En(final String id, final int idInt, final String langKey) {
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
//            Logger.getLogger(DischargeReason2En.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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
     * checks the current DischargeReason2 against the given DischargeReason and
     * returns if the combination is valid or not
     *
     * @param pDisReason DischargeReason (DischargeReason12) to validate the
     * combination
     * @return if the combination is invalid
     */
    public boolean isInvalidForDischargeReason(DischargeReasonEn pDisReason) {
        return (this.equals(dr201) && pDisReason.equals(DischargeReasonEn.dr06))
                || (this.equals(dr201) && pDisReason.equals(DischargeReasonEn.dr07)
                || (this.equals(dr202) && pDisReason.equals(DischargeReasonEn.dr07))
                || (this.equals(dr201) && pDisReason.equals(DischargeReasonEn.dr08))
                || (this.equals(dr201) && pDisReason.equals(DischargeReasonEn.dr09))
                || (this.equals(dr201) && pDisReason.equals(DischargeReasonEn.dr10))
                || (this.equals(dr201) && pDisReason.equals(DischargeReasonEn.dr11))
                || (this.equals(dr201) && pDisReason.equals(DischargeReasonEn.dr12))
                || (this.equals(dr201) && pDisReason.equals(DischargeReasonEn.dr17))
                || (this.equals(dr201) && pDisReason.equals(DischargeReasonEn.dr18))
                || (this.equals(dr201) && pDisReason.equals(DischargeReasonEn.dr21)));
    }

//    public static DischargeReason2En findById(final Integer pId) {
////        String id = (pId == null) ? "" : pId.trim().toLowerCase();
////        if (id.isEmpty()) {
////            return null;
////        }
//        if (pId == null) {
//            return null;
//        }
//        for (DischargeReason2En val : values()) {
//            if (val == null) {
//                continue;
//            }
//            if (pId.equals(val.getId())) {
//                return val;
//            }
////            if (id.equalsIgnoreCase(val.getId())) {
////                return val;
////            }
//        }
//        return null;
//    }
    public static DischargeReason2En findById(final String pId) {
        return DischargeReason2EnMap.getInstance().get(pId);
    }

    public static DischargeReason2En findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (DischargeReason2En val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

}

final class DischargeReason2EnMap extends AbstractCpxEnumMap<DischargeReason2En, String> {

    private static final DischargeReason2EnMap INSTANCE;

    static {
        INSTANCE = new DischargeReason2EnMap();
    }

    protected DischargeReason2EnMap() {
        super(DischargeReason2En.class);
    }

    public static DischargeReason2EnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public DischargeReason2En[] getValues() {
        return DischargeReason2En.values();
    }

}
