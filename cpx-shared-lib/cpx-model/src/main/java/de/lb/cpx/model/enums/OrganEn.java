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
 * Enumeration für die Keime in Labordaten
 *
 * @author niemeier
 */
public enum OrganEn implements CpxEnumInterface<Integer> {

    Breathing(1, Lang.ORGAN_BREATHING),
    Eyes(2, Lang.ORGAN_EYES),
    Endocrine(3, Lang.ORGAN_ENDOCRINE),
    Gastrointestinal(4, Lang.ORGAN_GASTROINTESTINAL),
    UrinaryOrgans(5, Lang.ORGAN_URINARY_ORGANS),
    Gynecology(6, Lang.ORGAN_GYNECOLOGY),
    Hematology(7, Lang.ORGAN_HEMATOLOGY),
    Skin(8, Lang.ORGAN_SKIN),
    Cardiovascula(9, Lang.ORGAN_CARDIOVASCULA),
    Ent(10, Lang.ORGAN_ENT),
    Musculoskeletal(11, Lang.ORGAN_MUSCULOSKELETAL),
    Kidney(12, Lang.ORGAN_KIDNEY),
    Psychosocial(13, Lang.ORGAN_PSYCHOSOCIAL),
    Teeth(14, Lang.ORGAN_TEETH),
    Neurology(15, Lang.ORGAN_NEUROLOGY),
    Body(16, Lang.ORGAN_BODY);

    private final int id;
    private final String langKey;

    private OrganEn(final Integer id, final String langKey) {
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
        return CpxEnumInterface.toStaticString(getTranslation().getAbbreviation(), getLangKey());
    }

    @Override
    public String getViewId() {
        //return String.valueOf(id);
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
//            Logger.getLogger(WeekdayEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static OrganEn findById(final Integer pId) {
        return OrganEnMap.getInstance().get(pId);
    }

    public static OrganEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (OrganEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

}

final class OrganEnMap extends AbstractCpxEnumMap<OrganEn, Integer> {

    private static final OrganEnMap INSTANCE;

    static {
        INSTANCE = new OrganEnMap();
    }

    protected OrganEnMap() {
        super(OrganEn.class);
    }

    public static OrganEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public OrganEn[] getValues() {
        return OrganEn.values();
    }

}
