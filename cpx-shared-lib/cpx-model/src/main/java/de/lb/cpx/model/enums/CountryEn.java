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

/**
 *
 * @author gerschmann
 */
public enum CountryEn implements CpxEnumInterface<String> {

    de("de", Lang.COUNTRY_GERMANY),
    fr("fr", Lang.COUNTRY_FRANCE),
    at("at", Lang.COUNTRY_AUSTRIA),
    ch("ch", Lang.COUNTRY_SWITZERLAND),
    en("en", Lang.COUNTRY_ENGLAND);

    private final String id;
    private final String langKey;

    private CountryEn(String id, String langKey) {
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
        return CpxEnumInterface.toStaticString(getViewId(), getLangKey());
    }

    @Override
    public String getViewId() {
        return id;
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
//            Logger.getLogger(CountryEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
//    public static CountryEn getStaticEnum(final String pValue) {
//        String value = (pValue == null) ? "" : pValue.toLowerCase().trim();
//        //Germany
//        if (value.equalsIgnoreCase("D")) {
//            value = "de";
//        }
//        //France
//        if (value.equalsIgnoreCase("F")) {
//            value = "fr";
//        }
//        //Austria
//        if (value.equalsIgnoreCase("A")) {
//            value = "at";
//        }
//        //and so on!
//        try {
//            return (CountryEn) CpxEnumInterface.findEnum(CountryEn.values(), value);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(CountryEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static CountryEn findById(final String pId) {
        String id = (pId == null) ? null : pId.toLowerCase().trim();
        if (id == null) {
            return null;
        }
        if (id.equalsIgnoreCase("D")) {
            //Germany
            //id = "de";
            return de;
        }
        if (id.equalsIgnoreCase("F")) {
            //France
            //id = "fr";
            return fr;
        }
        if (id.equalsIgnoreCase("A")) {
            //Austria
            //id = "at";
            return at;
        }
//        if (id.equalsIgnoreCase("GB")) {
//            //Great Britain
//            //id = "en";
//            return en;
//        }
        //and so on!
        return CountryEnMap.getInstance().get(id);
    }

}

final class CountryEnMap extends AbstractCpxEnumMap<CountryEn, String> {

    private static final CountryEnMap INSTANCE;

    static {
        INSTANCE = new CountryEnMap();
    }

    protected CountryEnMap() {
        super(CountryEn.class);
    }

    public static CountryEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public CountryEn[] getValues() {
        return CountryEn.values();
    }

}
