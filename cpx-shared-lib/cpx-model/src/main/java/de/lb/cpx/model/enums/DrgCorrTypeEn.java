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
 * typ der DRG - Korrektur
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
public enum DrgCorrTypeEn implements CpxEnumInterface<Integer> {

    no(0, Lang.DRG_CORR_TYPE_NO),
    Surcharge(1, Lang.DRG_CORR_TYPE_SURCHARGE),
    DeductionTransfer(2, Lang.DRG_CORR_TYPE_DEDUCTION_TRANSFER),
    Deduction(3, Lang.DRG_CORR_TYPE_DEDUCTION),
    DeductionTransferAdm(4, Lang.DRG_CORR_TYPE_DEDUCTION_TRANSFER_ADMISSION),
    DeductionTransferDis(5, Lang.DRG_CORR_TYPE_DEDUCTION_TRANSFER_DISCHARGE);

    private static final Logger LOG = Logger.getLogger(DrgCorrTypeEn.class.getName());

    private final String langKey;
    private final int id;

    private DrgCorrTypeEn(int id, String name) {
        this.id = id;
        this.langKey = name;
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

    public static DrgCorrTypeEn get2Id(int typeid) {
        switch (typeid) {
            case 0:
                return no;
            case 1:
                return Surcharge;
            case 2:
                return DeductionTransfer;
            case 3:
                return Deduction;
            case 4:
                return DeductionTransferAdm;
            case 5:
                return DeductionTransferDis;
            default:
                LOG.log(Level.WARNING, "Unknown type id: " + typeid);
        }
        return no;
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
//            Logger.getLogger(DrgCorrTypeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static DrgCorrTypeEn findById(final Integer pId) {
        return DrgCorrTypeEnMap.getInstance().get(pId);
    }

    public static DrgCorrTypeEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find DrgCorrTypeEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class DrgCorrTypeEnMap extends AbstractCpxEnumMap<DrgCorrTypeEn, Integer> {

    private static final DrgCorrTypeEnMap INSTANCE;

    static {
        INSTANCE = new DrgCorrTypeEnMap();
    }

    protected DrgCorrTypeEnMap() {
        super(DrgCorrTypeEn.class);
    }

    public static DrgCorrTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public DrgCorrTypeEn[] getValues() {
        return DrgCorrTypeEn.values();
    }

}
