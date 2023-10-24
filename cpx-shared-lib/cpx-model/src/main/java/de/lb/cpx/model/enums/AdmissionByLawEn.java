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

public enum AdmissionByLawEn implements CpxEnumInterface<Integer> {

    Freiwillig(1, Lang.ADMISSION_BY_LAW_VOLUNTARY),
    Unfreiwillig(2, Lang.ADMISSION_BY_LAW_UNVOLUNTARY);

    private static final Logger LOG = Logger.getLogger(AdmissionByLawEn.class.getName());

    private final Integer id;
    private final String langKey;

    private AdmissionByLawEn(final Integer id, final String langKey) {
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
    return this.getViewId() + " - " + cpxLanguage.get(String.valueOf(this.id));
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
//            Logger.getLogger(AdmissionByLawEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

    public static AdmissionByLawEn findById(final Integer pId) {
        return AdmissionByLawEnMap.getInstance().get(pId);
    }

    public static AdmissionByLawEn findById(final String pId) {
        final String id = pId == null ? "" : pId.trim();
        if (id.isEmpty()) {
            return null;
        }
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, "Cannot find AdmissionByLawEn, because this is not a valid integer: " + id + " (maybe invalid enum value is stored in database?)", ex);
            return null;
        }
    }

}

final class AdmissionByLawEnMap extends AbstractCpxEnumMap<AdmissionByLawEn, Integer> {

    private static final AdmissionByLawEnMap INSTANCE;

    protected AdmissionByLawEnMap() {
        super(AdmissionByLawEn.class);
    }

    static {
        INSTANCE = new AdmissionByLawEnMap();
    }

    public static AdmissionByLawEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public AdmissionByLawEn[] getValues() {
        return AdmissionByLawEn.values();
    }

}
