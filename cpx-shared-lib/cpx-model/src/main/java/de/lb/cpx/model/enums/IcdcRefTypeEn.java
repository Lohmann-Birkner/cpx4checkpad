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
public enum IcdcRefTypeEn implements CpxEnumInterface<Integer> {

    Kreuz(1, Lang.ICD_REF_TYPE_CROSS),
    Stern(2, Lang.ICD_REF_TYPE_STAR),
    Zusatz(3, Lang.ICD_REF_TYPE_ADDITION),
    ZusatzZu(4, Lang.ICD_REF_TYPE_ADDITION_TO);

    private static final Logger LOG = Logger.getLogger(IcdcRefTypeEn.class.getName());

    private final int id;
    private final String langKey;

    private IcdcRefTypeEn(final int id, final String langKey) {
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
//            Logger.getLogger(IcdcRefTypeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

//    public static IcdcRefTypeEn findById(final Integer pId) {
//        int id = (pId == null) ? 0 : pId;
//        if (id <= 0) {
//            return null;
//        }
//        for (IcdcRefTypeEn val : values()) {
//            if (val == null) {
//                continue;
//            }
//            if (id == val.getId()) {
//                return val;
//            }
//        }
//        return null;
//    }
    public static IcdcRefTypeEn findById(final Integer pId) {
        if (pId != null && pId.equals(0)) {
            return null;
        }
        return IcdcRefTypeEnMap.getInstance().get(pId);
    }

    public static IcdcRefTypeEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find IcdcRefTypeEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

    public static IcdcRefTypeEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        for (IcdcRefTypeEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(pName)) {
                return val;
            }
        }
        return null;
    }

}

final class IcdcRefTypeEnMap extends AbstractCpxEnumMap<IcdcRefTypeEn, Integer> {

    private static final IcdcRefTypeEnMap INSTANCE;

    static {
        INSTANCE = new IcdcRefTypeEnMap();
    }

    protected IcdcRefTypeEnMap() {
        super(IcdcRefTypeEn.class);
    }

    public static IcdcRefTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public IcdcRefTypeEn[] getValues() {
        return IcdcRefTypeEn.values();
    }

}
