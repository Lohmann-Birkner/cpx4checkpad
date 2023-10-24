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
 * Abteilungstyp
 *
 * @author gerschmann
 */
public enum DepartmentTypeEn implements CpxEnumInterface<Integer> {

    HA(1, Lang.DEPARTMENT_TYPE_MAIN_DEPARTMENT), //HA=Hauptabteilung
    BA(2, Lang.DEPARTMENT_TYPE_SLIP_DEPARTMENT), //BA=Belegabteilung
    BE(3, Lang.DEPARTMENT_TYPE_SPECIAL_INSTITUTION); //BE=Besondere Einrichtung//BE=Besondere Einrichtung
    //BE=Besondere Einrichtung//BE=Besondere Einrichtung

    private static final Logger LOG = Logger.getLogger(DepartmentTypeEn.class.getName());

    private final String langKey;
    private final int id;

    private DepartmentTypeEn(final int id, final String langKey) {
        this.id = id;
        this.langKey = langKey;
    }

    public static AdmissionModeEn cast2AdmissionMode(DepartmentTypeEn type) {
        switch (type) {
            case HA:
                return AdmissionModeEn.HA;
            case BA:
                return AdmissionModeEn.Bo;
            case BE:
                return AdmissionModeEn.HaBha;
        }
        return AdmissionModeEn.HA;
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
//            Logger.getLogger(DepartmentTypeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static DepartmentTypeEn findById(final Integer pId) {
        return DepartmentTypeEnMap.getInstance().get(pId);
    }

    public static DepartmentTypeEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find DepartmentTypeEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class DepartmentTypeEnMap extends AbstractCpxEnumMap<DepartmentTypeEn, Integer> {

    private static final DepartmentTypeEnMap INSTANCE;

    static {
        INSTANCE = new DepartmentTypeEnMap();
    }

    protected DepartmentTypeEnMap() {
        super(DepartmentTypeEn.class);
    }

    public static DepartmentTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public DepartmentTypeEn[] getValues() {
        return DepartmentTypeEn.values();
    }

}
