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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Diagnosetyp.
 *
 * @author Gloski
 *
 */
public enum IcdcTypeEn implements CpxEnumInterface<Integer> {

    Einweisung(1, Lang.ICD_TYPE_HOSPITALIZATION),
    Aufnahme(2, Lang.ICD_TYPE_ADMISSION),
    Verlegung(3, Lang.ICD_TYPE_TRANSFERRING),
    Entlassung(4, Lang.ICD_TYPE_DISCHARGE),
    Behandlung(5, Lang.ICD_TYPE_TREATMENT),
    DRG(6, Lang.ICD_TYPE_DRG),
    Postoperativ(7, Lang.ICD_TYPE_POSTOPERATIVE),
    Praeoperativ(8, Lang.ICD_TYPE_PREOPERATIVE),
    Risiko(9, Lang.ICD_TYPE_RISK),
    Pflege(10, Lang.ICD_TYPE_CARE),
    Arbeits(11, Lang.ICD_TYPE_WORK),
    EntlassungAmb(12, Lang.ICD_TYPE_DISCHARGE_AMBULANT),
    FachabteilungAuf(13, Lang.ICD_TYPE_DEPARTMENT_ADMISSION),
    FachabteilungEnt(14, Lang.ICD_TYPE_DEPARTMENT_DISCHARGE),
    FachabteilungBeh(15, Lang.ICD_TYPE_DEPARTMENT_TREATMENT),
    Abrechnungs(16, Lang.ICD_TYPE_BILLING),
    Vorstationaere(17, Lang.ICD_TYPE_BEFORE_INPATIENT),
    Nachstationaere(18, Lang.ICD_TYPE_AFTER_INPATIENT);

    private static final Logger LOG = Logger.getLogger(IcdcTypeEn.class.getName());

    private final int id;
    private final String langKey;

    private IcdcTypeEn(final int id, final String langKey) {
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
//            Logger.getLogger(IcdcTypeEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static IcdcTypeEn findById(final Integer pId) {
        return IcdcTypeEnMap.getInstance().get(pId);
    }

    public static IcdcTypeEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find IcdcTypeEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

    public static IcdcTypeEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        for (IcdcTypeEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(pName)) {
                return val;
            }
        }
        return null;
    }

    public static Collection<IcdcTypeEn> findByIds(final String pIds) {
        Set<IcdcTypeEn> result = new HashSet<>();
        if (pIds == null || pIds.trim().isEmpty()) {
            return null;
        }
        String[] val = pIds.split(",");
        for (String v : val) {
            IcdcTypeEn item = findById(v);
            if (item != null) {
                result.add(item);
            } else {
                LOG.log(Level.WARNING, "cannot detect icd type with this id: " + v);
            }
        }
        return result;
    }

}

final class IcdcTypeEnMap extends AbstractCpxEnumMap<IcdcTypeEn, Integer> {

    private static final IcdcTypeEnMap INSTANCE;

    static {
        INSTANCE = new IcdcTypeEnMap();
    }

    protected IcdcTypeEnMap() {
        super(IcdcTypeEn.class);
    }

    public static IcdcTypeEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public IcdcTypeEn[] getValues() {
        return IcdcTypeEn.values();
    }

}
