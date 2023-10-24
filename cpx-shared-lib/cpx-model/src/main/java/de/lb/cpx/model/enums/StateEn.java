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
public enum StateEn implements CpxEnumInterface<String> {
    st01("01", 1, Lang.STATE_SCHLESWIG_HOLSTEIN),
    st02("02", 2, Lang.STATE_HAMBURG),
    st03("03", 3, Lang.STATE_LOWER_SAXONY),
    st04("04", 4, Lang.STATE_BREMEN),
    st05("05", 5, Lang.STATE_NORTH_RHINE_WESTPHALIA),
    st06("06", 6, Lang.STATE_HESSE),
    st07("07", 7, Lang.STATE_RHINELAND_PALATINATE),
    st08("08", 8, Lang.STATE_BADEN_WUERTTEMBERG),
    st09("09", 9, Lang.STATE_BAVARIA),
    st10("10", 10, Lang.STATE_SAARLAND),
    st11("11", 11, Lang.STATE_BERLIN),
    st12("12", 12, Lang.STATE_BRANDENBURG),
    st13("13", 13, Lang.STATE_MECKLENBURG_WESTERN_POMERANIA),
    st14("14", 14, Lang.STATE_SAXONY),
    st15("15", 15, Lang.STATE_SAXONY_ANHALT),
    st16("16", 16, Lang.STATE_THURINGIA),
    st00("00", 0, Lang.STATE_FOREIGN_COUNTRY);

    private final String id;
    private final int idInt;
    private final String langKey;

    private StateEn(String id, int idInt, String langKey) {
        this.id = id;
        this.idInt = idInt;
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
        return idInt;
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
//            Logger.getLogger(StateEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
//    public static StateEn getStaticEnum(final String pValue) throws CpxIllegalArgumentException {
//        String value = (pValue == null) ? "" : pValue.trim();
//        if (value.length() == 1) {
//            value = "0" + value;
//        }
//        return (StateEn) CpxEnumInterface.findEnum(StateEn.values(), value);
//    }
    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    public static StateEn findById(final String pId) {
        String id = (pId == null) ? null : pId.trim();
        if (id != null && id.length() == 1) {
            id = "0" + id;
        }
        return StateEnMap.getInstance().get(id);
    }

}

final class StateEnMap extends AbstractCpxEnumMap<StateEn, String> {

    private static final StateEnMap INSTANCE;

    static {
        INSTANCE = new StateEnMap();
    }

    protected StateEnMap() {
        super(StateEn.class);
    }

    public static StateEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public StateEn[] getValues() {
        return StateEn.values();
    }

}
