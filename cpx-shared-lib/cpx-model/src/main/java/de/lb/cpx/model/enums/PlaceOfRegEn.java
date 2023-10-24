/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public enum PlaceOfRegEn implements CpxEnumInterface<Integer> {

    BEFORE_BILLING(1, Lang.PLACE_OF_REG_BEFORE_BILLING),
    REQUEST(2, Lang.PLACE_OF_REG_REQUEST),
    REQUEST_EXTENSION(3, Lang.PLACE_OF_REG_REQUEST_EXTENSION),
    REQUEST_FINALISATION(4, Lang.PLACE_OF_REG_REQUEST_FINALISATION);

    private static final Logger LOG = Logger.getLogger(PlaceOfRegEn.class.getName());

    private final int id;
    private final String langKey;

    private PlaceOfRegEn(int id, String langKey) {
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
        return id + "";
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
        return id + "";
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

    public static PlaceOfRegEn findById(final Integer pId) {
        return PlaceOfRegEnMap.getInstance().get(pId);
    }

    public static PlaceOfRegEn findById(final String pId) {
        if (pId == null || pId.trim().isEmpty()) {
            return null;
        }
        try {
            final int id = Integer.parseInt(pId.trim());
            return findById(id);
        } catch (NumberFormatException ex) {
            LOG.log(Level.WARNING, MessageFormat.format("this is not a valid integer for place of registration: {0}", pId));
            LOG.log(Level.FINEST, null, ex);
            return null;
        }
    }

    public static PlaceOfRegEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (PlaceOfRegEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

}

final class PlaceOfRegEnMap extends AbstractCpxEnumMap<PlaceOfRegEn, Integer> {

    private static final PlaceOfRegEnMap INSTANCE;

    static {
        INSTANCE = new PlaceOfRegEnMap();
    }

    protected PlaceOfRegEnMap() {
        super(PlaceOfRegEn.class);
    }

    public static PlaceOfRegEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public PlaceOfRegEn[] getValues() {
        return PlaceOfRegEn.values();
    }

}
