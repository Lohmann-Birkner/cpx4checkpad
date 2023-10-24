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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enumeration für die Keime in Labordaten
 *
 * @author niemeier
 */
public enum GermGrowthEn implements CpxEnumInterface<Integer> {

    None(1, Lang.GERM_GROWTH_NONE),
    Isolated(2, Lang.GERM_GROWTH_ISOLATED),
    Scattered(3, Lang.GERM_GROWTH_SCATTERED),
    Moderately(4, Lang.GERM_GROWTH_MODERATELY),
    Plenty(5, Lang.GERM_GROWTH_PLENTY),
    AfterEnrichment(6, Lang.GERM_GROWTH_AFTER_ENRICHMENT),
    NoInformation(0, Lang.GERM_GROWTH_NO_INFORMATION);

    private static final Logger LOG = Logger.getLogger(GermGrowthEn.class.getName());

    private final int id;
    private final String langKey;

    private GermGrowthEn(final Integer id, final String langKey) {
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

    public static GermGrowthEn findById(final Integer pId) {
        return GermGrowthEnMap.getInstance().get(pId);
    }

    public static GermGrowthEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (GermGrowthEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

    public static GermGrowthEn detectGermGrowth(final String pHaeufigkeit) {
        final String val = pHaeufigkeit == null ? "" : pHaeufigkeit.trim();
        if (val.isEmpty()) {
            return null;
        }
        final GermGrowthEn growth;
        switch (val) {
            case "nicht nachgewiesen":
            case "nicht nachgewiesen.":
            case "n i c h t  nachgewiesen.":
                growth = GermGrowthEn.None;
                break;
            case "ganz vereinzelt":
                growth = GermGrowthEn.Isolated;
                break;
            case "vereinzelt":
                growth = GermGrowthEn.Scattered;
                break;
            case "mäßig viel":
                growth = GermGrowthEn.Moderately;
                break;
            case "reichlich":
                growth = GermGrowthEn.Plenty;
                break;
            case "nach Anreicherung":
                growth = GermGrowthEn.AfterEnrichment;
                break;
            default:
                LOG.log(Level.WARNING, "cannot detect germ growth: {0}", val);
                growth = null;
                break;
        }
        return growth;
    }

}

final class GermGrowthEnMap extends AbstractCpxEnumMap<GermGrowthEn, Integer> {

    private static final GermGrowthEnMap INSTANCE;

    static {
        INSTANCE = new GermGrowthEnMap();
    }

    protected GermGrowthEnMap() {
        super(GermGrowthEn.class);
    }

    public static GermGrowthEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public GermGrowthEn[] getValues() {
        return GermGrowthEn.values();
    }

}
