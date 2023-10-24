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

public enum GenderEn implements CpxEnumInterface<Integer> {

    M(1, Lang.GENDER_SALUTATION_MALE, Lang.GENDER_MALE),
    W(2, Lang.GENDER_SALUTATION_FEMALE, Lang.GENDER_FEMALE),
    I(3, Lang.GENDER_SALUTATION_UNDEFINED, Lang.GENDER_UNDEFINED),
    D(4, Lang.GENDER_SALUTATION_DIVERSE, Lang.GENDER_DIVERSE),
    U(9, Lang.GENDER_SALUTATION_UNKNOWN, Lang.GENDER_UNKNOWN);

    private static final Logger LOG = Logger.getLogger(GenderEn.class.getName());

    private final String langKey;
    private final int id;
    private final String salutationLangKey;

    private GenderEn(int id, final String salutationLangKey, final String langKey) {
        this.langKey = langKey;
        this.salutationLangKey = salutationLangKey;
        this.id = id;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    public String getSalutationLangKey() {
        return salutationLangKey;
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
        return getTranslation().getValue();
//        return CpxEnumInterface.toStaticString(getViewId(), getLangKey());
    }

    @Override
    public String getViewId() {
        //return String.valueOf(id);
        return name();
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
//            Logger.getLogger(GenderEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
    public Translation getSalutationTranslation() {
        return Lang.get(getSalutationLangKey());
    }

    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    public static GenderEn findById(final Integer pId) {
        return GenderEnMap.getInstance().get(pId);
    }

    public static GenderEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find GenderEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

    public static GenderEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        if (name.equalsIgnoreCase("F")) {
            //F = female
            return GenderEn.W;
        }
        for (GenderEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

}

final class GenderEnMap extends AbstractCpxEnumMap<GenderEn, Integer> {

    private static final GenderEnMap INSTANCE;

    static {
        INSTANCE = new GenderEnMap();
    }

    protected GenderEnMap() {
        super(GenderEn.class);
    }

    public static GenderEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public GenderEn[] getValues() {
        return GenderEn.values();
    }

}
