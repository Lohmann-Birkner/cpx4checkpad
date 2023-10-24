/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.model.enums;

import de.lb.cpx.model.enums.AbstractCpxEnumMap;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author niemeier
 */
public enum RiskAreaEn implements CpxEnumInterface<Integer> {

 /*   MD(1, Lang.RISK_AREA_MAIN_DIAGNOSES),
    SD(2, Lang.RISK_AREA_SECONDARY_DIAGNOSIS),
    PROCS(3, Lang.RISK_AREA_PROCEDURES),
    LOS(4, Lang.RISK_AREA_LENGTH_OF_STAY),
    PUUHD(5, Lang.RISK_AREA_PUUHD),
    OTHER(6, Lang.RISK_AREA_OTHER);
*/
    DRG_PEPP(1, Lang.RISK_AREA_DRG_PEPP),
    MD(2, Lang.RISK_AREA_MAIN_DIAGNOSES),
    SD(3, Lang.RISK_AREA_SECONDARY_DIAGNOSIS),
    PROCS(4, Lang.RISK_AREA_PROCEDURES),
    PRI_MISALO(5, Lang.RISK_AREA_PRIMARY_MISALLOC),
    SEC_MISALO(6, Lang.RISK_AREA_SECONDARY_MISALLOC),
    CASE_MERGE(7, Lang.RISK_AREA_CASE_MERGE),
    OT_FEE_SUR(8, Lang.RISK_AREA_OTHER_FEE_SURCHARGE),
    OTHER(9, Lang.RISK_AREA_OTHER);

    public static String getTranslation2Value(String mRiskAreas) {
        RiskAreaEn[] values = RiskAreaEn.values();
        for(RiskAreaEn val: values){
            if(val.name().equals(mRiskAreas)){
                return val.getTranslation().getValue();
            }
        }
        return mRiskAreas;
    }
    
    private final int id;
    private final String langKey;

    private RiskAreaEn(int id, String langKey) {
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
        return id + "";
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
        return id + "";
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
//            Logger.getLogger(CountryEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
//    public static CountryEn getStaticEnum(final String pValue) {
//        String value = (pValue == null) ? "" : pValue.toLowerCase().trim();
//        //Germany
//        if (value.equalsIgnoreCase("D")) {
//            value = "de";
//        }
//        //France
//        if (value.equalsIgnoreCase("F")) {
//            value = "fr";
//        }
//        //Austria
//        if (value.equalsIgnoreCase("A")) {
//            value = "at";
//        }
//        //and so on!
//        try {
//            return (CountryEn) CpxEnumInterface.findEnum(CountryEn.values(), value);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(CountryEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static RiskAreaEn findById(final Integer pId) {
        return RiskAreaEnMap.getInstance().get(pId);
    }
}

final class RiskAreaEnMap extends AbstractCpxEnumMap<RiskAreaEn, Integer> {

    private static final RiskAreaEnMap INSTANCE;

    static {
        INSTANCE = new RiskAreaEnMap();
    }

    protected RiskAreaEnMap() {
        super(RiskAreaEn.class);
    }

    public static RiskAreaEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public RiskAreaEn[] getValues() {
        return RiskAreaEn.values();
    }

}
