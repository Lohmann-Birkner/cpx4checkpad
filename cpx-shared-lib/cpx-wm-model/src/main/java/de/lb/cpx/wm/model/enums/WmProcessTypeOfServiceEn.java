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
package de.lb.cpx.wm.model.enums;

import de.lb.cpx.model.enums.AbstractCpxEnumMap;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bohm
 */
public enum WmProcessTypeOfServiceEn implements CpxEnumInterface<Integer> {

    ambulant(0, Lang.AMBULANT),
    stationär(1, Lang.STATIONARY);

    private static final Logger LOG = Logger.getLogger(WmProcessTypeOfServiceEn.class.getName());

    private final int id;
    private final String langKey;

    private WmProcessTypeOfServiceEn(final Integer id, final String langKey) {
        this.id = id;
        this.langKey = langKey;
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

    @Override
    public String getLangKey() {
        return this.langKey;
    }

    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public String toString() {
        return CpxEnumInterface.toStaticString(getViewId(), getLangKey());
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    @Override
    public String getViewId() {
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
//            Logger.getLogger(WmProcessTypeOfServiceEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
//        }
//        return null;
//    }
    public static WmProcessTypeOfServiceEn findById(final Integer pId) {
        return WmProcessTypeOfServiceEnMap.getInstance().get(pId);
    }

    public static WmProcessTypeOfServiceEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find WmProcessTypeOfServiceEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class WmProcessTypeOfServiceEnMap extends AbstractCpxEnumMap<WmProcessTypeOfServiceEn, Integer> {

    private static final WmProcessTypeOfServiceEnMap INSTANCE;

    static {
        INSTANCE = new WmProcessTypeOfServiceEnMap();
    }

    protected WmProcessTypeOfServiceEnMap() {
        super(WmProcessTypeOfServiceEn.class);
    }

    public static WmProcessTypeOfServiceEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public WmProcessTypeOfServiceEn[] getValues() {
        return WmProcessTypeOfServiceEn.values();
    }

}
