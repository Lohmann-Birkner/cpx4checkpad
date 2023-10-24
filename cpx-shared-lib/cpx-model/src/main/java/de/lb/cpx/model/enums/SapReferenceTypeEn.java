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
 *    2017  Shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shahin
 */
public enum SapReferenceTypeEn implements CpxEnumInterface<Integer> {
    //Rechnung; Storno

    Rechnung(1, Lang.SAP_REFERENCE_TYPE_BILL),
    Storno(2, Lang.SAP_REFERENCE_TYPE_CANCELLATION),;

    private static final Logger LOG = Logger.getLogger(SapReferenceTypeEn.class.getName());

    private final String langKey;
    private final int id;

    private SapReferenceTypeEn(final int id, final String langKey) {
        this.langKey = langKey;
        this.id = id;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    @Override
    public String toString() {
        return CpxEnumInterface.toStaticString(getViewId(), getLangKey());
    }

    @Override
    public String getViewId() {
//        return super.name();
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
//            Logger.getLogger(LocalisationEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static SapReferenceTypeEn findById(final Integer pId) {
        return SapReferenceTypeEnMap.getInstance().get(pId);
    }

    public static SapReferenceTypeEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find SapReferenceTypeEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class SapReferenceTypeEnMap extends AbstractCpxEnumMap<SapReferenceTypeEn, Integer> {

    private static final SapReferenceTypeEnMap INSTANCE;

    static {
        INSTANCE = new SapReferenceTypeEnMap();
    }

    protected SapReferenceTypeEnMap() {
        super(SapReferenceTypeEn.class);
    }

    public static SapReferenceTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public SapReferenceTypeEn[] getValues() {
        return SapReferenceTypeEn.values();
    }

}
