/*
 * Copyright (c) 2019 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.analyser.attributes;

import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TInsurance;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.TPatientDetails;
import de.lb.cpx.ruleviewer.analyser.editors.ChoiceEditor;
import de.lb.cpx.ruleviewer.analyser.editors.DateEditor;
import de.lb.cpx.ruleviewer.analyser.editors.IntegerEditor;
import de.lb.cpx.ruleviewer.analyser.editors.StringEditor;
import de.lb.cpx.shared.lang.Lang;

/**
 *
 * @author wilde
 */
public class PatientDataAttributes extends AnalyserAttributes {

    public static final String KEY_AGE_YEARS = "csdAgeYears";
    public static final String KEY_AGE_DAYS = "csdAgeDays";
    public static final String KEY_DATE_OF_BIRTH = "patDateOfBirth";
    public static final String KEY_GENDER = "csdGenderEn";
    public static final String KEY_WEIGHT = "csdAdmissionWeight";
    public static final String KEY_ZIP_CODE = "patdZipcode";
    public static final String KEY_INSURANCE_STATUS = "insStatusEn";
    public static final String KEY_CITY = "patdCity";
    private static PatientDataAttributes INSTANCE;

    private PatientDataAttributes() {
        addSingleAttribute(KEY_AGE_YEARS, TCaseDetails.class)
                .setDisplayName(Lang.getAgeInYearsObj().getAbbreviation())
                .setEditorClass(IntegerEditor.class);
        addSingleAttribute(KEY_AGE_DAYS, TCaseDetails.class)
                .setDisplayName(Lang.getAgeInDaysObj().getAbbreviation())
                .setEditorClass(IntegerEditor.class);
        addSingleAttribute(KEY_DATE_OF_BIRTH, TPatient.class)
                .setDisplayName(Lang.getDateOfBirthObj().getAbbreviation())
                .setEditorClass(DateEditor.class);
        addSingleAttribute(KEY_GENDER, TCaseDetails.class)
                .setDisplayName(Lang.getGenderObj().getAbbreviation())
                .setEditorClass(ChoiceEditor.class);
        addSingleAttribute(KEY_WEIGHT, TCaseDetails.class)
                .setDisplayName(Lang.getWeight())
                .setEditorClass(IntegerEditor.class);
        addSingleAttribute(KEY_ZIP_CODE, TPatientDetails.class)
                .setDisplayName(Lang.getAddressZipCodeObj().getAbbreviation())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_INSURANCE_STATUS, TInsurance.class)
                .setDisplayName(Lang.getInsuranceStatus())
                .setEditorClass(ChoiceEditor.class);
        addSingleAttribute(KEY_CITY, TPatientDetails.class)
                .setDisplayName(Lang.getAddressCity())
                .setEditorClass(StringEditor.class);

    }

    public static PatientDataAttributes instance() {
        if (INSTANCE == null) {
            INSTANCE = new PatientDataAttributes();
        }
        return INSTANCE;
    }

}
