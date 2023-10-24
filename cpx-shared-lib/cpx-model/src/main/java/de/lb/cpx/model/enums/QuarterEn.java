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
 * Enumeration für die Stati des Falles
 *
 * @author niemeier
 */
public enum QuarterEn implements CpxEnumInterface<Integer> {

    Q1(1, Lang.QUARTER_1),
    Q2(2, Lang.QUARTER_2),
    Q3(3, Lang.QUARTER_3),
    Q4(4, Lang.QUARTER_4);

    private static final Logger LOG = Logger.getLogger(QuarterEn.class.getName());

    private final int id;
    private final String langKey;

    private QuarterEn(final Integer id, final String langKey) {
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
        return String.valueOf(id);
        //return CpxEnumInterface.toStaticString(getTranslation().getAbbreviation(), getLangKey());
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

    public static QuarterEn findById(final Integer pId) {
        return QuarterEnMap.getInstance().get(pId);
    }

    public static QuarterEn findById(final String pId) {
        if (pId == null || pId.trim().isEmpty()) {
            return null;
        }
        try {
            final int id = Integer.parseInt(pId.trim());
            return findById(id);
        } catch (NumberFormatException ex) {
            LOG.log(Level.WARNING, MessageFormat.format("this is not a valid integer for quarter: {0}", pId));
            LOG.log(Level.FINEST, null, ex);
            return null;
        }
    }

}

final class QuarterEnMap extends AbstractCpxEnumMap<QuarterEn, Integer> {

    private static final QuarterEnMap INSTANCE;

    static {
        INSTANCE = new QuarterEnMap();
    }

    protected QuarterEnMap() {
        super(QuarterEn.class);
    }

    public static QuarterEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public QuarterEn[] getValues() {
        return QuarterEn.values();
    }

}
