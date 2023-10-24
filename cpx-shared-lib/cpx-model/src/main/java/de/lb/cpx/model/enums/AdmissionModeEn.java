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
import java.util.logging.Level;
import java.util.logging.Logger;

public enum AdmissionModeEn implements CpxEnumInterface<Integer> {

    HA(1, Lang.ADMISSION_MODE_MAIN_DEPARTMENT), //HA
    HaBh(2, Lang.ADMISSION_MODE_MAIN_DEPARTMENT_MIDWIFE), //HA/B-Heb.
    Bo(3, Lang.ADMISSION_MODE_SURGEON), //B-Op.
    BoBa(4, Lang.ADMISSION_MODE_SURGEON_ANESTHETIST), //B-Op./B-Anäst.
    BoBh(5, Lang.ADMISSION_MODE_SURGEON_MIDWIFE), //B-Op./B-Heb.
    BoBaBh(6, Lang.ADMISSION_MODE_SURGEON_ANESTHETIST_MIDWIFE), //B-Op./B-Anäst./B-Heb.
    // TeStVe(7, Lang.ADMISSION_MODE_DAY_CARE), //Teilstationaere Versorgung - not used in cp and therefore ignored mabe added later?
    HaBha(8, Lang.ADMISSION_MODE_MAIN_DEPARTMENT_CHARGE_DOCTOR), //HA/B-Hon.Arzt
    NR(7, Lang.ADMISSION_MODE_NOT_RELEVANT);//Not relevant 
    //Not relevant

    private static final Logger LOG = Logger.getLogger(AdmissionModeEn.class.getName());

    private final String langKey;
    private final int id;

    private AdmissionModeEn(final int id, final String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getIdStr() {
        return String.valueOf(id);
    }

    @Override
    public int getIdInt() {
        return id;
    }

    /**
     * @param pCaseType cscasetype enum for selection of admission modes
     * @return array of admission modes for case type, if casetype unknown all
     * admission modes are returned
     */
    public static AdmissionModeEn[] values(CaseTypeEn pCaseType) {
        switch (pCaseType) {
            case DRG:
                return valuesDrg();
            case PEPP:
                return valuesPepp();
            default:
                return values();
        }
    }

    /**
     * @return admission mode array for drg cases
     */
    public static AdmissionModeEn[] valuesDrg() {
        AdmissionModeEn[] drgAdmission = {HA, HaBh, Bo, BoBa, BoBh, BoBaBh, HaBha};
        return drgAdmission;
    }

    /**
     * @return admission mode array for pepp cases
     */
    public static AdmissionModeEn[] valuesPepp() {
        AdmissionModeEn[] peppAdmission = {NR};
        return peppAdmission;
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
//            Logger.getLogger(AdmissionModeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static AdmissionModeEn findById(final Integer pId) {
        return AdmissionModeEnMap.getInstance().get(pId);
    }

    public static AdmissionModeEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find AdmissionModeEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

    public static AdmissionModeEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (AdmissionModeEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

}

final class AdmissionModeEnMap extends AbstractCpxEnumMap<AdmissionModeEn, Integer> {

    private static final AdmissionModeEnMap INSTANCE;

    static {
        INSTANCE = new AdmissionModeEnMap();
    }

    protected AdmissionModeEnMap() {
        super(AdmissionModeEn.class);
    }

    public static AdmissionModeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public AdmissionModeEn[] getValues() {
        return AdmissionModeEn.values();
    }

}
