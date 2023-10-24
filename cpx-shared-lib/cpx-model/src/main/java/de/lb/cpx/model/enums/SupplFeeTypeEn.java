/*
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 *   typ des Zusatzentgeltes
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public enum SupplFeeTypeEn implements CpxEnumInterface<Integer> {

    ZE(1, Lang.SUPPL_FEE_SF), //ZE=Zusatzentgelt (SF=Supplementary Fee)
    ZP(2, Lang.SUPPL_FEE_SP), //ZP=Zusatzentgelt PEPP (SP=Supplementary Fee Pepp)
    ET(3, Lang.SUPPL_FEE_DF); //ET=Tagesentgelt (DF=Daily Fee)//ET=Tagesentgelt (DF=Daily Fee)//ET=Tagesentgelt (DF=Daily Fee)//ET=Tagesentgelt (DF=Daily Fee)
    //ET=Tagesentgelt (DF=Daily Fee)//ET=Tagesentgelt (DF=Daily Fee)//ET=Tagesentgelt (DF=Daily Fee)//ET=Tagesentgelt (DF=Daily Fee)

    private static final Logger LOG = Logger.getLogger(SupplFeeTypeEn.class.getName());

    private final String langKey;
    private final int id;

    private SupplFeeTypeEn(int id, String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

    public static SupplFeeTypeEn get2Id(int typeid) {
        switch (typeid) {
            case 1:
                return ZE;
            case 2:
                return ZP;
            case 3:
                return ET;
            default:
                LOG.log(Level.WARNING, "Unknown type id: " + typeid);
        }
        return ZE;
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
//            Logger.getLogger(SupplFeeTypeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static SupplFeeTypeEn findById(final Integer pId) {
        return SupplFeeTypeEnMap.getInstance().get(pId);
    }

    public static SupplFeeTypeEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find SupplFeeTypeEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class SupplFeeTypeEnMap extends AbstractCpxEnumMap<SupplFeeTypeEn, Integer> {

    private static final SupplFeeTypeEnMap INSTANCE;

    static {
        INSTANCE = new SupplFeeTypeEnMap();
    }

    protected SupplFeeTypeEnMap() {
        super(SupplFeeTypeEn.class);
    }

    public static SupplFeeTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public SupplFeeTypeEn[] getValues() {
        return SupplFeeTypeEn.values();
    }

}
