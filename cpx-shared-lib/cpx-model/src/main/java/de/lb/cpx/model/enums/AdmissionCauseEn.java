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

public enum AdmissionCauseEn implements CpxEnumInterface<Integer> {
    /*
  E(1, "Einweisung durch Arzt"), 
  Z(2, "Einweisung durch Zahnarzt"), 
  N(3, "Notfall"), 
  R(4, "Aufnahme nach vorausgehender Behandlung in einer Rehaeinrichtung"), 
  V(5, "Verlegung"), 
  K(6, "Verlegung (Aufnahme) aus einem anderem Krankenhaus i.R.e. Kooperation"), 
  G(7, "Geburt"), 
  B(8, "Begleitperson"), 
  A(9, "Verlegung (mit Behandlungsdauer im verlegenden Krankenhaus bis zu 24 Stunden");
     */

    E(1, Lang.ADMISSION_CAUSE_INSTRUCTION_BY_DOCTOR),
    Z(2, Lang.ADMISSION_CAUSE_INSTRUCTION_BY_DENTIST),
    N(3, Lang.ADMISSION_CAUSE_EMERGENCY),
    R(4, Lang.ADMISSION_CAUSE_ADMISSION_AFTER_REHA),
    V(5, Lang.ADMISSION_CAUSE_TRANSFERRING),
    K(6, Lang.ADMISSION_CAUSE_TRANSFERRING_FROM_ANOTHER_HOSPITAL),
    G(7, Lang.ADMISSION_CAUSE_BIRTH),
    B(8, Lang.ADMISSION_CAUSE_COMPANION),
    A(9, Lang.ADMISSION_CAUSE_TRANSFERRING_WITHIN_24H);

    private static final Logger LOG = Logger.getLogger(AdmissionCauseEn.class.getName());

    private final int id;
    private final String langKey;

    private AdmissionCauseEn(final int id, final String langKey) {
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
        return super.name();
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
//            Logger.getLogger(AdmissionCauseEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

//    public static AdmissionCauseEn findById(final Integer pId) {
//        int id = (pId == null) ? 0 : pId;
//        if (id <= 0) {
//            return null;
//        }
//        for (AdmissionCauseEn val : values()) {
//            if (val == null) {
//                continue;
//            }
//            if (id == val.getId()) {
//                return val;
//            }
//        }
//        return null;
//    }
    public static AdmissionCauseEn findById(final Integer pId) {
        return AdmissionCauseEnMap.getInstance().get(pId);
    }

    public static AdmissionCauseEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find AdmissionCauseEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

    public static AdmissionCauseEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (AdmissionCauseEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

    public static Collection<AdmissionCauseEn> findByNames(final String pNames) {
        Set<AdmissionCauseEn> result = new HashSet<>();
        if (pNames == null || pNames.trim().isEmpty()) {
            return null;
        }
        String[] val = pNames.split(",");
        for (String v : val) {
            AdmissionCauseEn item = findByName(v);
            if (item != null) {
                result.add(item);
            } else {
                LOG.log(Level.WARNING, "cannot detect admission cause with this name: " + v);
            }
        }
        return result;
    }
    
    public static boolean isTransfer(AdmissionCauseEn pAdmCause){
        return pAdmCause.equals(V) || pAdmCause.equals(K);
    }

}

final class AdmissionCauseEnMap extends AbstractCpxEnumMap<AdmissionCauseEn, Integer> {

    private static final AdmissionCauseEnMap INSTANCE;

    static {
        INSTANCE = new AdmissionCauseEnMap();
    }

    protected AdmissionCauseEnMap() {
        super(AdmissionCauseEn.class);
    }

    public static AdmissionCauseEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public AdmissionCauseEn[] getValues() {
        return AdmissionCauseEn.values();
    }

}
