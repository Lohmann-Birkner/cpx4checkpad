/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public enum AgeEn implements CpxEnumInterface<Integer> {
    AGEINYEARS(1, Lang.getAgeInYears()),
    AGEINDAYS(2, Lang.getAgeInDays());

    private static final Logger LOG = Logger.getLogger(AgeEn.class.getName());

    private final String langKey;
    private final int id;

    private AgeEn(int id, String langKey) {
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

//        @Override
//        public String toString(final CpxLanguageInterface cpxLanguage) {
//          return this.getViewId() + " - " + cpxLanguage.get(langKey);
//        }
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
//            Logger.getLogger(AgeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static AgeEn findById(final Integer pId) {
        return AgeEnMap.getInstance().get(pId);
    }

    public static AgeEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find AgeEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class AgeEnMap extends AbstractCpxEnumMap<AgeEn, Integer> {

    private static final AgeEnMap INSTANCE;

    static {
        INSTANCE = new AgeEnMap();
    }

    protected AgeEnMap() {
        super(AgeEn.class);
    }

    public static AgeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public AgeEn[] getValues() {
        return AgeEn.values();
    }

}
