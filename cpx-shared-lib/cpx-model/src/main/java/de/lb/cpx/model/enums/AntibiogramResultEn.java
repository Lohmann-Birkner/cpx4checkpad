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
public enum AntibiogramResultEn implements CpxEnumInterface<String> {

    S("S", Lang.ANTIBIOGRAM_RESULT_SENSITIVE),
    R("R", Lang.ANTIBIOGRAM_RESULT_RESISTENT),
    I("I", Lang.ANTIBIOGRAM_RESULT_INTERMEDIATE);

    private final String id;
    private final String langKey;

    private AntibiogramResultEn(final String id, final String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getIdStr() {
        return id;
    }

    @Override
    public int getIdInt() {
        throw new UnsupportedOperationException("id as integer is not supported for " + getClass().getSimpleName());
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

    public static AntibiogramResultEn findById(final String pId) {
        return AntibiogramResultEnMap.getInstance().get(pId);
    }

    public static AntibiogramResultEn findById(final char pId) {
        return AntibiogramResultEnMap.getInstance().get(pId + "");
    }

}

final class AntibiogramResultEnMap extends AbstractCpxEnumMap<AntibiogramResultEn, String> {

    private static final AntibiogramResultEnMap INSTANCE;

    static {
        INSTANCE = new AntibiogramResultEnMap();
    }

    protected AntibiogramResultEnMap() {
        super(AntibiogramResultEn.class);
    }

    public static AntibiogramResultEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public AntibiogramResultEn[] getValues() {
        return AntibiogramResultEn.values();
    }

}
