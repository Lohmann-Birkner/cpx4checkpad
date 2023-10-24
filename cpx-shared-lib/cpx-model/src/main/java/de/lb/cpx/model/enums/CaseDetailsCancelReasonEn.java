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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enumeration für die Stati des Falles
 *
 * @author gerschmann
 */
public enum CaseDetailsCancelReasonEn implements CpxEnumInterface<Integer> {
    MERGE(1, Lang.CASE_DETAILS_CANCEL_REASON_CASE_MERGE),
    KIS(2, Lang.CASE_DETAILS_CANCEL_REASON_KIS),
    MANUAL(3, Lang.CASE_DETAILS_CANCEL_REASON_USER);

    private static final Logger LOG = Logger.getLogger(CaseDetailsCancelReasonEn.class.getName());

    private final int id;
    private final String langKey;

    private CaseDetailsCancelReasonEn(final Integer id, final String langKey) {
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
        return CpxEnumInterface.toStaticString(getViewId(), getLangKey());
    }

    @Override
    public String getViewId() {
        //return String.valueOf(id);
        return name();
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
//            Logger.getLogger(CsStatusEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
    public boolean isMerge() {
        if (this.equals(MERGE)) {
            return true;
        }
        return false;
    }

    public boolean isKis() {
        if (this.equals(KIS)) {
            return true;
        }
        return false;
    }

    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    public static CaseDetailsCancelReasonEn findById(final Integer pId) {
        return CaseDetailsCancelReasonEnMap.getInstance().get(pId);
    }

    public static CaseDetailsCancelReasonEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find CaseDetailsCancelReasonEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class CaseDetailsCancelReasonEnMap extends AbstractCpxEnumMap<CaseDetailsCancelReasonEn, Integer> {

    private static final CaseDetailsCancelReasonEnMap INSTANCE;

    static {
        INSTANCE = new CaseDetailsCancelReasonEnMap();
    }

    protected CaseDetailsCancelReasonEnMap() {
        super(CaseDetailsCancelReasonEn.class);
    }

    public static CaseDetailsCancelReasonEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public CaseDetailsCancelReasonEn[] getValues() {
        return CaseDetailsCancelReasonEn.values();
    }

}
