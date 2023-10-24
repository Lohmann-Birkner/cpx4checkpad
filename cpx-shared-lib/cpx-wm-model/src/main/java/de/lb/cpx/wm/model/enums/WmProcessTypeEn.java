/*
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2016  Husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.wm.model.enums;

import de.lb.cpx.model.enums.AbstractCpxEnumMap;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author Husser
 */
public enum WmProcessTypeEn implements CpxEnumInterface<String> {
    hospital(Values.HOSPITAL, Lang.PROCESS_TYPE_HOSPITAL),
    insurance(Values.INSURANCE, Lang.PROCESS_TYPE_INSURANCE);

    public static class Values {

        private Values() {
            //utility class needs no public constructor
        }

        public static final String HOSPITAL = "HOSPITAL";
        public static final String INSURANCE = "INSURANCE";
    }

    private final String id;
    private final String langKey;

    private WmProcessTypeEn(String id, String langKey) {
        this.id = id;
        this.langKey = langKey;
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

    @Override
    public String getLangKey() {
        return langKey;
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
//            Logger.getLogger(WmProcessTypeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static WmProcessTypeEn findById(final String pId) {
        String id = (pId == null) ? null : pId.toUpperCase().trim();
        if (id == null) {
            return null;
        }
        return WmProcessTypeEnMap.getInstance().get(id);
    }

    public static WmProcessTypeEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (WmProcessTypeEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

}

final class WmProcessTypeEnMap extends AbstractCpxEnumMap<WmProcessTypeEn, String> {

    private static final WmProcessTypeEnMap INSTANCE;

    static {
        INSTANCE = new WmProcessTypeEnMap();
    }

    protected WmProcessTypeEnMap() {
        super(WmProcessTypeEn.class);
    }

    public static WmProcessTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public WmProcessTypeEn[] getValues() {
        return WmProcessTypeEn.values();
    }

}
