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
public enum AcgIndexTypeEn implements CpxEnumInterface<Integer> {
    NP(0, false, false, false, Lang.ACG_INDEX_TYPE_NP), //NP = Not present
    ICD(1, true, false, false, Lang.ACG_INDEX_TYPE_ICD), //ICD = Diagnosis
    Rx(2, false, true, false, Lang.ACG_INDEX_TYPE_RX), //Rx-Präparate sind verschreibungspflichtige bzw. rezeptpflichtige Medikamente
    BTH(3, true, true, false, Lang.ACG_INDEX_TYPE_BTH), //BTH = Both
    TRT(4, true, true, true, Lang.ACG_INDEX_TYPE_TRT); //TRT = Treatment

    public final int id;
    public final boolean diagnosis;
    public final boolean drugs;
    public final boolean treatment;
    public final String langKey;

    private AcgIndexTypeEn(final int pId, final boolean pDiagnosis, final boolean pDrugs, final boolean pTreatment, final String langKey) {
        this.id = pId;
        this.diagnosis = pDiagnosis;
        this.drugs = pDrugs;
        this.treatment = pTreatment;
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
    public static AcgIndexTypeEn findById(final Integer pId) {
        return AcgIndexTypeEnMap.getInstance().get(pId);
    }

    public static AcgIndexTypeEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (AcgIndexTypeEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

}

final class AcgIndexTypeEnMap extends AbstractCpxEnumMap<AcgIndexTypeEn, Integer> {

    private static final AcgIndexTypeEnMap INSTANCE;

    static {
        INSTANCE = new AcgIndexTypeEnMap();
    }

    protected AcgIndexTypeEnMap() {
        super(AcgIndexTypeEn.class);
    }

    public static AcgIndexTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public AcgIndexTypeEn[] getValues() {
        return AcgIndexTypeEn.values();
    }

}
