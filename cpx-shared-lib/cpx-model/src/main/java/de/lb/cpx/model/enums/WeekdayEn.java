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

/**
 * Enumeration für die Stati des Falles
 *
 * @author gerschmann
 */
public enum WeekdayEn implements CpxEnumInterface<Integer> {

    SUNDAY(1, Lang.DAY_SUNDAY),
    MONDAY(2, Lang.DAY_MONDAY),
    TUESDAY(3, Lang.DAY_TUESDAY),
    WEDNESDAY(4, Lang.DAY_WEDNESDAY),
    THURSDAY(5, Lang.DAY_THURSDAY),
    FRIDAY(6, Lang.DAY_FRIDAY),
    SATURDAY(7, Lang.DAY_SATURDAY);

    private final int id;
    private final String langKey;

    private WeekdayEn(final Integer id, final String langKey) {
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

    public static WeekdayEn findById(final Integer pId) {
        return WeekdayEnMap.getInstance().get(pId);
    }

}

final class WeekdayEnMap extends AbstractCpxEnumMap<WeekdayEn, Integer> {

    private static final WeekdayEnMap INSTANCE;

    static {
        INSTANCE = new WeekdayEnMap();
    }

    protected WeekdayEnMap() {
        super(WeekdayEn.class);
    }

    public static WeekdayEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public WeekdayEn[] getValues() {
        return WeekdayEn.values();
    }

}
