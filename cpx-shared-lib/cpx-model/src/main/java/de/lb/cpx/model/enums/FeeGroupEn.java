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

/**
 *
 * @author Wilde
 */
public enum FeeGroupEn implements CpxEnumInterface<Integer> {
    /**
     *
     */
    BPflV(1, Lang.FEE_GROUP_BPFLV, false),
    /**
     *
     */
    DRG(2, Lang.FEE_GROUP_DRG, true),
    /**
     *
     */
    NUB(3, Lang.FEE_GROUP_NUB, false),
    /**
     *
     */
    Anl3(4, Lang.FEE_GROUP_APPENDIX_3, true),
    /**
     *
     */
    BesEin(5, Lang.FEE_GROUP_SPECIAL_INSTITUTIONS, false),
    /**
     *
     */
    VorStat(6, Lang.FEE_GROUP_BEFORE_INPATIENT, false),
    /**
     *
     */
    DakIntEnt(7, Lang.FEE_GROUP_DAK_INTERNAL_CHARGES, false),
    /**
     *
     */
    IntVer(8, Lang.FEE_GROUP_IV_INTEGRATED_CARE, false),
    /**
     *
     */
    DMP(9, Lang.FEE_GROUP_DMP, false),
    /**
     *
     */
    GesEnt(10, Lang.FEE_GROUP_SEPARATE_CHARGES, false),
    /**
     *
     */
    ModVor(11, Lang.FEE_GROUP_MODEL_PROJECTS, false),
    /**
     *
     */
    BPflVoS(12, Lang.FEE_GROUP_BPFLV_WITHOUT_FINAL_INVOICE, false),
    /**
     *
     */
    DRGoS(13, Lang.FEE_GROUP_DRG_WITHOUT_FINAL_INVOICE, false),
    /**
     *
     */
    NUBoS(14, Lang.FEE_GROUP_NUB_WITHOUT_FINAL_INVOICE, false),
    /**
     *
     */
    Anl3oS(15, Lang.FEE_GROUP_APPENDIX_3_WITHOUT_FINAL_INVOICE, false),
    /**
     *
     */
    BesEinoS(16, Lang.FEE_GROUP_SPECIAL_FACILITIES_WITHOUT_FINAL_INVOICE, false),
    /**
     *
     */
    VoroS(17, Lang.FEE_GROUP_BEFORE_INPATIENT_WITHOUT_FINAL_INVOICE, false),
    /**
     *
     */
    DakIntEntoS(18, Lang.FEE_GROUP_DAK_INTERNAL_CHARGES_WITHOUT_FINAL_INVOICE, false),
    /**
     *
     */
    IVoS(19, Lang.FEE_GROUP_IV_INTEGRATED_CARE_WITHOUT_FINAL_INVOICE, false),
    /**
     *
     */
    DMPoS(20, Lang.FEE_GROUP_DMP_WITHOUT_FINAL_INVOICE, false),
    /**
     *
     */
    GesEntoS(21, Lang.FEE_GROUP_SEPARATE_CHARGES_WITHOUT_FINAL_INVOICE, false),
    /**
     *
     */
    ModoS(22, Lang.FEE_GROUP_MODEL_PROJECTS_WITHOUT_FINAL_INVOICE, false),
    /**
     *
     */
    BPflVoSoE(23, Lang.FEE_GROUP_BPFLV_WITHOUT_FINAL_INVOICE_AND_WITHOUT_DISCHARGE, false),
    /**
     *
     */
    DRGoSoE(24, Lang.FEE_GROUP_DRG_WITHOUT_FINAL_INVOICE_AND_WITHOUT_DISCHARGE, false),
    /**
     *
     */
    NUBoSoE(25, Lang.FEE_GROUP_NUB_WITHOUT_FINAL_INVOICE_AND_WITHOUT_DISCHARGE, false),
    /**
     *
     */
    Anl3oSoE(26, Lang.FEE_GROUP_APPENDIX_3_WITHOUT_FINAL_INVOICE_AND_WITHOUT_DISCHARGE, false),
    /**
     *
     */
    BesEoSoE(27, Lang.FEE_GROUP_SPECIAL_INSTITUTIONS_WITHOUT_FINAL_INVOICE_AND_WITHOUT_DISCHARGE, false),
    /**
     *
     */
    VoroSoE(28, Lang.FEE_GROUP_BEFORE_INPATIENT_WITHOUT_FINAL_INVOICE_AND_WITHOUT_DISCHARGE, false),
    /**
     *
     */
    DAKintEoSoE(29, Lang.FEE_GROUP_DAK_INTERNAL_CHARGES_WITHOUT_FINAL_INVOICE_AND_WITHOUT_DISCHARGE, false),
    /**
     *
     */
    IVoSoE(30, Lang.FEE_GROUP_IV_INTEGRATED_CARE_WITHOUT_FINAL_INVOICE_AND_WITHOUT_DISCHARGE, false),
    /**
     *
     */
    DMPoSoE(31, Lang.FEE_GROUP_DMP_WITHOUT_FINAL_INVOICE_AND_WITHOUT_DISCHARGE, false),
    /**
     *
     */
    GesEntoSoE(32, Lang.FEE_GROUP_SEPARATE_CHARGES_WITHOUT_FINAL_INVOICE_AND_WITHOUT_DISCHARGE, false),
    /**
     *
     */
    ModoSoE(33, Lang.FEE_GROUP_MODEL_PROJECTS_WITHOUT_FINAL_INVOICE_AND_WITHOUT_DISCHARGE, false),
    /**
     *
     */
    PEPPAnl1au2amS(34, Lang.FEE_GROUP_PEPP_ANNEX_1A_INPATIENT_AND_2A_PARTIAL_INPATIENT_WITH_FINAL_INVOICE, true),
    /**
     *
     */
    PEPP1bu2bmS(35, Lang.FEE_GROUP_HOSPITAL_INDIVIDUAL_PEPP_ANNEX_1B_INPATIENT_AND_2B_PARTIAL_INPATIENT_WITH_FINAL_INVOICE, true),
    /**
     *
     */
    ModmS(36, Lang.FEE_GROUP_MODEL_PROJECTS_WITH_FINAL_INVOICE, false),
    /**
     *
     */
    IVvtmS(37, Lang.FEE_GROUP_IV_FULL_INPATIENT_AND_DAY_PATIENT_WITH_FINAL_INVOICE, false),
    /**
     *
     */
    VormS(38, Lang.FEE_GROUP_BEFORE_INPATIENT_ALONE_WITH_FINAL_INVOICE, false),
    /**
     *
     */
    ToSmE(39, Lang.FEE_GROUP_PARTIAL_PAYMENT_WITHOUT_FINAL_INVOICE_WITH_DISMISSAL, false),
    /**
     *
     */
    TzoSoE(40, Lang.FEE_GROUP_PARTIAL_PAYMENT_WITHOUT_FINAL_INVOICE_WITHOUT_DISMISSAL, false);

    private static final Logger LOG = Logger.getLogger(FeeGroupEn.class.getName());

    private final int id;
    private final String langKey;
    private final boolean grouped;

    private FeeGroupEn(final int id, final String langKey, final boolean grouped) {
        this.id = id;
        this.langKey = langKey;
        this.grouped = grouped;
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

    public boolean isGrouped() {
        return grouped;
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
//            Logger.getLogger(FeeGroupEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

//    public static FeeGroupEn findById(final Integer pId) {
//        int id = (pId == null) ? 0 : pId;
//        if (id <= 0) {
//            return null;
//        }
//        for (FeeGroupEn val : values()) {
//            if (val == null) {
//                continue;
//            }
//            if (id == val.getId()) {
//                return val;
//            }
//        }
//        return null;
//    }
    public static FeeGroupEn findById(final Integer pId) {
        return FeeGroupEnMap.getInstance().get(pId);
    }

    public static FeeGroupEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find FeeGroupEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class FeeGroupEnMap extends AbstractCpxEnumMap<FeeGroupEn, Integer> {

    private static final FeeGroupEnMap INSTANCE;

    static {
        INSTANCE = new FeeGroupEnMap();
    }

    protected FeeGroupEnMap() {
        super(FeeGroupEn.class);
    }

    public static FeeGroupEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public FeeGroupEn[] getValues() {
        return FeeGroupEn.values();
    }

}
