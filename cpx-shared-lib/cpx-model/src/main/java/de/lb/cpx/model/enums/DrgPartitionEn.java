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

import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public enum DrgPartitionEn implements CpxEnumInterface<Integer> {

    M(1, Lang.DRG_PARTITION_MEDICAL), //medical  
    O(2, Lang.DRG_PARTITION_SURGICAL), //surgical 
    A(3, Lang.DRG_PARTITION_OTHER);//other 
    //other

    private static final Logger LOG = Logger.getLogger(DrgPartitionEn.class.getName());

    private final String langKey;
    private final int id;

    private DrgPartitionEn(int id, final String langKey) {
        this.langKey = langKey;
        this.id = id;
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
//            Logger.getLogger(DrgPartitionEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static DrgPartitionEn getValue2name(String value) throws CpxIllegalArgumentException {
        return (DrgPartitionEn) CpxEnumInterface.findEnum(values(), value);
    }

    public static DrgPartitionEn findById(final Integer pId) {
        return DrgPartitionEnMap.getInstance().get(pId);
    }

    public static DrgPartitionEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find DrgPartitionEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class DrgPartitionEnMap extends AbstractCpxEnumMap<DrgPartitionEn, Integer> {

    private static final DrgPartitionEnMap INSTANCE;

    static {
        INSTANCE = new DrgPartitionEnMap();
    }

    protected DrgPartitionEnMap() {
        super(DrgPartitionEn.class);
    }

    public static DrgPartitionEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public DrgPartitionEn[] getValues() {
        return DrgPartitionEn.values();
    }

}
