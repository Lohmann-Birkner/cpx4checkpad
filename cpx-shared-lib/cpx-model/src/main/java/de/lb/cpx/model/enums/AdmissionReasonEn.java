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

public enum AdmissionReasonEn implements CpxEnumInterface<String> {

    ar01("01", 1, Lang.ADMISSION_REASON_HOSPITAL_CARE_INPATIENT, true),
    ar02("02", 2, Lang.ADMISSION_REASON_HOSPITAL_CARE_INPATIENT_WITH_EXPECTED_BEFORE_INPATIENT_TREATMENT, true),
    ar03("03", 3, Lang.ADMISSION_REASON_HOSPITAL_DAY_PATIENT, true),
    ar04("04", 4, Lang.ADMISSION_REASON_HOSPITAL_TREATMENT_WITHOUT_SUBSEQUENT_HOSPITALIZATION, true),
    ar05("05", 5, Lang.ADMISSION_REASON_MATERNITY_HOSPITAL, true),
    ar06("06", 6, Lang.ADMISSION_REASON_BIRTH, true),
    ar07("07", 7, Lang.ADMISSION_REASON_RECOVERY_DUE_COMPLICATION, true),
    ar08("08", 8, Lang.ADMISSION_REASON_HOSPITALISATION_TO_ORGAN_REMOVAL, true),
    ar09("09", 9, Lang.ADMISSION_REASON_FREE, false),
    ar10("10", 10, Lang.ADMISSION_REASON_STATION_EQUIVALENT, true),
    ar11("11", 11, Lang.ADMISSION_REASON_TRANSITIONAL_CARE, true);//Übergangspflege

    private static final Logger LOG = Logger.getLogger(AdmissionReasonEn.class.getName());

    private final String langKey;
    private final boolean viewRelevant;
    private final String id;
    private final int idInt;

    private AdmissionReasonEn(final String id, final int idInt, final String langKey, final boolean pViewRelevant) {
        this.id = id;
        this.idInt = idInt;
        this.langKey = langKey;
        this.viewRelevant = pViewRelevant;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getIdStr() {
        return id;
    }

    @Override
    public int getIdInt() {
        return idInt;
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
        return viewRelevant;
    }

//    @Override
//    public CpxEnumInterface getEnum(String value) {
//        try {
//            return CpxEnumInterface.findEnum(values(), value);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(AdmissionReasonEn.class.getName()).log(Level.SEVERE, "Cannot find enum for this value: " + value, ex);
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

//    public static AdmissionReasonEn findById(final String pId) {
//        String id = (pId == null) ? "" : pId.trim().toLowerCase();
//        if (id.isEmpty()) {
//            return null;
//        }
//        for (AdmissionReasonEn val : values()) {
//            if (val == null) {
//                continue;
//            }
//            if (id.equalsIgnoreCase(val.getId())) {
//                return val;
//            }
//        }
//        return null;
//    }
    public static AdmissionReasonEn findById(final String pId) {
        return AdmissionReasonEnMap.getInstance().get(pId);
    }

    public static AdmissionReasonEn findByName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            return null;
        }
        final String name = pName.trim();
        for (AdmissionReasonEn val : values()) {
            if (val != null && val.name().equalsIgnoreCase(name)) {
                return val;
            }
        }
        return null;
    }

    public static Collection<AdmissionReasonEn> findByIds(final String Ids) {
        Set<AdmissionReasonEn> result = new HashSet<>();
        if (Ids == null || Ids.trim().isEmpty()) {
            return result;
        }
        String[] val = Ids.split(",");
        for (String v : val) {
            AdmissionReasonEn item = findById(v);
            if (item != null) {
                result.add(item);
            } else {
                LOG.log(Level.WARNING, "cannot detect admission reason with this id: " + v);
            }
        }
        return result;
    }

}

final class AdmissionReasonEnMap extends AbstractCpxEnumMap<AdmissionReasonEn, String> {

    private static final AdmissionReasonEnMap INSTANCE;

    static {
        INSTANCE = new AdmissionReasonEnMap();
    }

    protected AdmissionReasonEnMap() {
        super(AdmissionReasonEn.class);
    }

    public static AdmissionReasonEnMap getInstance() {
        return INSTANCE;
    }

    @Override
    public AdmissionReasonEn[] getValues() {
        return AdmissionReasonEn.values();
    }

}
