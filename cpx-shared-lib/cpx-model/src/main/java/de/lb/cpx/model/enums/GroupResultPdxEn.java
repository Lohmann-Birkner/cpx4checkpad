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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
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
public enum GroupResultPdxEn implements CpxEnumInterface<Integer> {
    pdx0(0, "0", Lang.GROUP_RESULT_PDX_VALID_PDX_CODE), // Gültige Hauptdiagnose
    pdx1(1, "1", Lang.GROUP_RESULT_PDX_INVALID_PDX_CODE),//Ungültiger ICD-10-GM-Kode als Hauptdiagnose
    pdx2(2, "2", Lang.GROUP_RESULT_PDX_VWXY_PDX_CODE), //V-,W-,X-,Y-Kode als Hauptdiagnose
    pdx3(3, "3", Lang.GROUP_RESULT_PDX_IMPROPER_PDX_CODE); //Unzulässige Hauptdiagnose
    //Unzulässige Hauptdiagnose

    private static final Logger LOG = Logger.getLogger(GroupResultPdxEn.class.getName());

    private final int id;
    private final String idStr;
    private final String langKey;

    private GroupResultPdxEn(final int id, final String idStr, final String langKey) {
        this.id = id;
        this.langKey = langKey;
        this.idStr = idStr;
    }

    public static GroupResultPdxEn getValue2IntId(int gpdx) {
        for (GroupResultPdxEn en : values()) {
            if (en.getId() == gpdx) {
                return en;
            }
        }
        return null;
    }

    public static GroupResultPdxEn getValue2Id(String gpdx) {
        if (gpdx == null || gpdx.isEmpty()) {
            return pdx0;
        }
        for (GroupResultPdxEn en : values()) {
            if (en.getViewId().equals(gpdx)) {
                return en;
            }
        }
        return null;
    }

    /*
  @Override
  public String toString(CpxLanguageInterface cpxLanguage) {
    return this.getViewId() + " - " + cpxLanguage.get(langKey);
  }
     */
    @Override
    public String toString() {
        return CpxEnumInterface.toStaticString(getViewId(), getLangKey());
    }

    @Override
    public String getViewId() {
        return idStr;
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
//            Logger.getLogger(GroupResultPdxEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
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
        return idStr;
    }

    @Override
    public int getIdInt() {
        return id;
    }

    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    public static GroupResultPdxEn findById(final Integer pId) {
        return GroupResultPdxEnMap.getInstance().get(pId);
    }

    public static GroupResultPdxEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find GroupResultPdxEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class GroupResultPdxEnMap extends AbstractCpxEnumMap<GroupResultPdxEn, Integer> {

    private static final GroupResultPdxEnMap INSTANCE;

    static {
        INSTANCE = new GroupResultPdxEnMap();
    }

    protected GroupResultPdxEnMap() {
        super(GroupResultPdxEn.class);
    }

    public static GroupResultPdxEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public GroupResultPdxEn[] getValues() {
        return GroupResultPdxEn.values();
    }

}
