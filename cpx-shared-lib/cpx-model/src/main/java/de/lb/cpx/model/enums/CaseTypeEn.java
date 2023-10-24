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
 * Diagnose-Referenz-Typ.
 *
 * @author Gloski
 *
 */
public enum CaseTypeEn implements CpxEnumInterface<Integer> {
    OTHER(0, false, Lang.CASE_TYPE_OTHER),
    DRG(1, true, Lang.CASE_TYPE_DRG),
    PEPP(2, true, Lang.CASE_TYPE_PEPP),
    //  PSY(2, false, Lang.CASE_TYPE_PEPP),
    PIA(3, false, Lang.CASE_TYPE_PIA),
    AmbuOP(4, false, Lang.CASE_TYPE_AMBU_OP),
    vorstatAbbrecher(5, false, Lang.CASE_TYPE_PRE_ADMISSION_TERMINATORS);

    private static final Logger LOG = Logger.getLogger(CaseTypeEn.class.getName());

    private final int id;
    private final boolean viewRelevant;
    private final String langKey;

    private CaseTypeEn(final Integer id, final boolean viewRelevant, final String langKey) {
        this.id = id;
        this.viewRelevant = viewRelevant;
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
        //return String.valueOf(id);
        return name();
    }

    @Override
    public boolean isViewRelevant() {
        return viewRelevant;
    }

//    @Override
//    public CpxEnumInterface getEnum(String value) {
//        try {
//            return CpxEnumInterface.findEnum(values(), value);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(CsCaseTypeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
    public static CaseTypeEn getValue2Id(int id) {
        CaseTypeEn[] values = CaseTypeEn.values();
        for (CaseTypeEn val : values) {
            if (val.getId() == id) {
                return val;
            }
        }
        return DRG; // in Checkpoint DRG is a default value for the case type.
    }

    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    public static CaseTypeEn findById(final Integer pId) {
        return CsCaseTypeEnMap.getInstance().get(pId);
    }

    public static CaseTypeEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find CaseTypeEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

    public static CaseTypeEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (CaseTypeEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

}

final class CsCaseTypeEnMap extends AbstractCpxEnumMap<CaseTypeEn, Integer> {

    private static final CsCaseTypeEnMap INSTANCE;

    static {
        INSTANCE = new CsCaseTypeEnMap();
    }

    protected CsCaseTypeEnMap() {
        super(CaseTypeEn.class);
    }

    public static CsCaseTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public CaseTypeEn[] getValues() {
        return CaseTypeEn.values();
    }

}
