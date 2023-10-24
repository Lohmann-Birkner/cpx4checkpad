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

public enum LocalisationEn implements CpxEnumInterface<Integer> {
    E(0, "leer"),//empty keine Angabe 
    R(1, Lang.LOCALISATION_RIGHT),
    L(2, Lang.LOCALISATION_LEFT),
    B(3, Lang.LOCALISATION_BILATERAL);

    private static final Logger LOG = Logger.getLogger(LocalisationEn.class.getName());

    private final String langKey;
    private final int id;

    private LocalisationEn(final int id, final String langKey) {
        this.langKey = langKey;
        this.id = id;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    /*
  @Override
  public String toString(final CpxLanguageInterface cpxLanguage) {
    return getViewId() + " - " + cpxLanguage.get(langKey);
  }
     */
    @Override
    public String toString() {
        return CpxEnumInterface.toStaticString(getViewId(), getLangKey());
    }

    @Override
    public String getViewId() {
        return super.name();
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
//            Logger.getLogger(LocalisationEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
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

    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    public boolean isEmpty() {
        return E.equals(this);
    }

    public static LocalisationEn findById(final Integer pId) {
        return LocalisationEnMap.getInstance().get(pId);
    }

    public static LocalisationEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find LocalisationEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

    public static LocalisationEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (LocalisationEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }
}

final class LocalisationEnMap extends AbstractCpxEnumMap<LocalisationEn, Integer> {

    private static final LocalisationEnMap INSTANCE;

    static {
        INSTANCE = new LocalisationEnMap();
    }

    protected LocalisationEnMap() {
        super(LocalisationEn.class);
    }

    public static LocalisationEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public LocalisationEn[] getValues() {
        return LocalisationEn.values();
    }

}
