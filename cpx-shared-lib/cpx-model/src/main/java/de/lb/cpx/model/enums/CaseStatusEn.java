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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enumeration für die Stati des Falles
 *
 * @author gerschmann
 */
public enum CaseStatusEn implements CpxEnumInterface<Integer> {
//    DEFAULT(0, Lang.CASE_STATUS_STANDARD),
    PROCESSED(1, Lang.CASE_STATUS_IN_PROGRESS),
    NEW(2, Lang.CASE_STATUS_NEW),
    NEW_VERS(3, Lang.CASE_STATUS_NEW_VERSION),
    SUGG(4, Lang.CASE_STATUS_SUGGESTION),
    CLOSED(5, Lang.CASE_STATUS_CLOSED),
    SAP_CLOSED(1001, Lang.CASE_STATUS_SAP_CLOSED);

    private static final Logger LOG = Logger.getLogger(CaseStatusEn.class.getName());

    private final int id;
    private final String langKey;

    private CaseStatusEn(final Integer id, final String langKey) {
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
    /**
     * help methodes, determines when case status is resolved(closed), enum
     * supports 2 kind of closed returns true if status is 5
     *
     * @return if case is closed for checkpoint
     */
    public boolean isClosed() {
        if (this.equals(CLOSED)) {
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

    public static CaseStatusEn findById(final Integer pId) {
        return CsStatusEnMap.getInstance().get(pId);
    }

    public static CaseStatusEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find CaseStatusEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

    public static CaseStatusEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (CaseStatusEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

}

final class CsStatusEnMap extends AbstractCpxEnumMap<CaseStatusEn, Integer> {

    private static final CsStatusEnMap INSTANCE;

    static {
        INSTANCE = new CsStatusEnMap();
    }

    protected CsStatusEnMap() {
        super(CaseStatusEn.class);
    }

    public static CsStatusEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public CaseStatusEn[] getValues() {
        return CaseStatusEn.values();
    }

}
