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

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.ruleviewer.analyser.editors.ChoiceEditor;
import de.lb.cpx.ruleviewer.analyser.editors.DateEditor;
import de.lb.cpx.ruleviewer.analyser.editors.IntegerEditor;
import de.lb.cpx.ruleviewer.analyser.editors.LongEditor;
import de.lb.cpx.ruleviewer.analyser.editors.StringEditor;
import de.lb.cpx.ruleviewer.util.AnalyserAttributesHelper;
import de.lb.cpx.shared.lang.Lang;

/**
 *
 * @author wilde
 */
public class CaseDataAttributes extends AnalyserAttributes {

    private static final Boolean SHOW_EXTENSION_FIELDS = true;

    public static final String KEY_CASETYPE = "csCaseTypeEn";
    public static final String KEY_HOS_IDENT = "csHospitalIdent";
    public static final String KEY_BILLING_DATE = "csBillingDate";
    public static final String KEY_ADMISSION_DATE = "csdAdmissionDate";
    public static final String KEY_DISCHARGE_DATE = "csdDischargeDate";
    public static final String KEY_ADMISSION_REASON_1 = "csdAdmReason12En";
    public static final String KEY_ADMISSION_REASON_2 = "csdAdmReason34En";
    public static final String KEY_ADMISSION_CAUSE = "csdAdmCauseEn";
    public static final String KEY_HMV = "csdHmv";
    public static final String KEY_DOCTOR = "csDoctorIdent";
    public static final String KEY_DISCHARGE_REASON_1 = "csdDisReason12En";
    public static final String KEY_DISCHARGE_REASON_2 = "csdDisReason3En";
    public static final String KEY_CASE_NUMBER = "csCaseNumber";
//    private static final String KEY_LOS = "csdLos";
    public static final String KEY_LOS_ALTERATION = "csdLosAlteration";
    public static final String KEY_INSURANCE = "insuranceIdentifier";
    public static final String KEY_FEE_GROUP = "csFeeGroupEn";
    public static final String KEY_NUMERIC_1 = "numeric1";
    public static final String KEY_NUMERIC_2 = "numeric2";
    public static final String KEY_NUMERIC_3 = "numeric3";
    public static final String KEY_NUMERIC_4 = "numeric4";
    public static final String KEY_NUMERIC_5 = "numeric5";
    public static final String KEY_NUMERIC_6 = "numeric6";
    public static final String KEY_NUMERIC_7 = "numeric7";
    public static final String KEY_NUMERIC_8 = "numeric8";
    public static final String KEY_NUMERIC_9 = "numeric9";
    public static final String KEY_NUMERIC_10 = "numeric10";
    public static final String KEY_STRING_1 = "string1";
    public static final String KEY_STRING_2 = "string2";
    public static final String KEY_STRING_3 = "string3";
    public static final String KEY_STRING_4 = "string4";
    public static final String KEY_STRING_5 = "string5";
    public static final String KEY_STRING_6 = "string6";
    public static final String KEY_STRING_7 = "string7";
    public static final String KEY_STRING_8 = "string8";
    public static final String KEY_STRING_9 = "string9";
    public static final String KEY_STRING_10 = "string10";

    private static CaseDataAttributes INSTANCE;

    private CaseDataAttributes() {
        addSingleAttribute(KEY_CASETYPE, TCase.class)
                .setDisplayName(Lang.getCaseType())
                .setEditorClass(ChoiceEditor.class);
        addSingleAttribute(KEY_HOS_IDENT, TCase.class)
                .setDisplayName(Lang.getHospitalIdentifier())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_BILLING_DATE, TCase.class)
                .setDisplayName(Lang.getBillDate())
                .setEditorClass(DateEditor.class);
        addSingleAttribute(KEY_ADMISSION_DATE, TCaseDetails.class)
                .setDisplayName(Lang.getAdmissionDate())
                .setEditorClass(DateEditor.class);
        addSingleAttribute(KEY_DISCHARGE_DATE, TCaseDetails.class)
                .setDisplayName(Lang.getDischargeDate())
                .setEditorClass(DateEditor.class);
        addSingleAttribute(KEY_ADMISSION_REASON_1, TCaseDetails.class)
                .setDisplayName(Lang.getAdmissionReason())
                .setEditorClass(ChoiceEditor.class);
        addSingleAttribute(KEY_ADMISSION_REASON_2, TCaseDetails.class)
                .setDisplayName(Lang.getAdmissionReason2())
                .setEditorClass(ChoiceEditor.class);
        addSingleAttribute(KEY_ADMISSION_CAUSE, TCaseDetails.class)
                .setDisplayName(Lang.getAdmissionCauseObj().getAbbreviation())
                .setEditorClass(ChoiceEditor.class);
        addSingleAttribute(KEY_HMV, TCaseDetails.class)
                .setDisplayName(Lang.getArtificialRespiration())
                .setEditorClass(IntegerEditor.class);
        addSingleAttribute(KEY_DOCTOR, TCase.class)
                .setDisplayName(Lang.getDoctorIdent())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_DISCHARGE_REASON_1, TCaseDetails.class)
                .setDisplayName(Lang.getDischargeReason())
                .setEditorClass(ChoiceEditor.class);
        addSingleAttribute(KEY_DISCHARGE_REASON_2, TCaseDetails.class)
                .setDisplayName(Lang.getDischargeReason2())
                .setEditorClass(ChoiceEditor.class);
        addSingleAttribute(KEY_CASE_NUMBER, TCase.class)
                .setDisplayName(Lang.getCaseNumber())
                .setEditorClass(StringEditor.class);
//        addSingleAttribute(KEY_LOS, TCaseDetails.class)
//                .setDisplayName(Lang.getLOS())
//                .setEditorClass(LongEditor.class);
        addSingleAttribute(KEY_LOS_ALTERATION, TCaseDetails.class)
                .setDisplayName(Lang.getLosAlterationObj().getAbbreviation())
                .setEditorClass(LongEditor.class);
        addSingleAttribute(KEY_INSURANCE, TCase.class)
                .setDisplayName(Lang.getInsurance())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_FEE_GROUP, TCase.class)
                .setDisplayName(Lang.getFeeGroup())
                .setEditorClass(ChoiceEditor.class);

//        addTestFields(9);
        addExtentionField(SHOW_EXTENSION_FIELDS);

    }

    public static final CaseDataAttributes instance() {
        if (INSTANCE == null) {
            INSTANCE = new CaseDataAttributes();
        }
        return INSTANCE;
    }

    private void addExtentionField(Boolean pShow) {
        if (!pShow) {
            return;
        }

        addSingleAttribute(KEY_NUMERIC_1, TCase.class)
                .setDisplayName(Lang.getRulesTxtCritCaseNumeric1Dis())
                .setEditorClass(IntegerEditor.class);
        addSingleAttribute(KEY_NUMERIC_2, TCase.class)
                .setDisplayName(Lang.getRulesTxtCritCaseNumeric2Dis())
                .setEditorClass(IntegerEditor.class);
        addSingleAttribute(KEY_NUMERIC_3, TCase.class)
                .setDisplayName(Lang.getRulesTxtCritCaseNumeric3Dis())
                .setEditorClass(IntegerEditor.class);
        addSingleAttribute(KEY_NUMERIC_4, TCase.class)
                .setDisplayName(Lang.getRulesTxtCritCaseNumeric4Dis())
                .setEditorClass(IntegerEditor.class);
        addSingleAttribute(KEY_NUMERIC_5, TCase.class)
                .setDisplayName(Lang.getRulesTxtCritCaseNumeric5Dis())
                .setEditorClass(IntegerEditor.class);
        addSingleAttribute(KEY_NUMERIC_6, TCase.class)
                .setDisplayName(Lang.getRulesTemp53FallNumeric6())
                .setEditorClass(IntegerEditor.class);
        addSingleAttribute(KEY_NUMERIC_7, TCase.class)
                .setDisplayName(Lang.getRulesTemp54FallNumeric7())
                .setEditorClass(IntegerEditor.class);
        addSingleAttribute(KEY_NUMERIC_8, TCase.class)
                .setDisplayName(Lang.getRulesTemp55FallNumeric8())
                .setEditorClass(IntegerEditor.class);
        addSingleAttribute(KEY_NUMERIC_9, TCase.class)
                .setDisplayName(Lang.getRulesTemp56FallNumeric9())
                .setEditorClass(IntegerEditor.class);
        addSingleAttribute(KEY_NUMERIC_10, TCase.class)
                .setDisplayName(Lang.getRulesTemp52FallNumeric10())
                .setEditorClass(IntegerEditor.class);

        addSingleAttribute(KEY_STRING_1, TCase.class)
                .setDisplayName(Lang.getRulesTxtCritCaseStr1Dis())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_STRING_2, TCase.class)
                .setDisplayName(Lang.getRulesTxtCritCaseStr2Dis())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_STRING_3, TCase.class)
                .setDisplayName(Lang.getRulesTxtCritCaseStr3Dis())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_STRING_4, TCase.class)
                .setDisplayName(Lang.getRulesTxtCritCaseStr4Dis())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_STRING_5, TCase.class)
                .setDisplayName(Lang.getRulesTxtCritCaseStr5Dis())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_STRING_6, TCase.class)
                .setDisplayName(Lang.getRulesTemp58FallString6())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_STRING_7, TCase.class)
                .setDisplayName(Lang.getRulesTemp59FallString7())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_STRING_8, TCase.class)
                .setDisplayName(Lang.getRulesTemp60FallString8())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_STRING_9, TCase.class)
                .setDisplayName(Lang.getRulesTemp61FallString9())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_STRING_10, TCase.class)
                .setDisplayName(Lang.getRulesTemp57FallString10())
                .setEditorClass(StringEditor.class);
    }

    private void addTestFields(int pCount) {
        for (int i = 0; i < pCount; i++) {
            addSingleAttribute(KEY_CASETYPE + i, TCase.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_CASETYPE), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_CASETYPE))
                    .setDisplayName(Lang.getCaseType())
                    .setEditorClass(ChoiceEditor.class);
            addSingleAttribute(KEY_HOS_IDENT + i, TCase.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_HOS_IDENT), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_HOS_IDENT))
                    .setDisplayName(Lang.getHospitalIdentifier())
                    .setEditorClass(StringEditor.class);
            addSingleAttribute(KEY_BILLING_DATE + i, TCase.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_BILLING_DATE), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_BILLING_DATE))
                    .setDisplayName(Lang.getBillDate())
                    .setEditorClass(DateEditor.class);
            addSingleAttribute(KEY_ADMISSION_DATE + i, TCaseDetails.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_ADMISSION_DATE), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_ADMISSION_DATE))
                    .setDisplayName(Lang.getAdmissionDate())
                    .setEditorClass(DateEditor.class);
            addSingleAttribute(KEY_DISCHARGE_DATE + i, TCaseDetails.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_DISCHARGE_DATE), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_DISCHARGE_DATE))
                    .setDisplayName(Lang.getDischargeDate())
                    .setEditorClass(DateEditor.class);
            addSingleAttribute(KEY_ADMISSION_REASON_1 + i, TCaseDetails.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_ADMISSION_REASON_1), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_ADMISSION_REASON_1))
                    .setDisplayName(Lang.getAdmissionReason())
                    .setEditorClass(ChoiceEditor.class);
            addSingleAttribute(KEY_ADMISSION_REASON_2 + i, TCaseDetails.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_ADMISSION_REASON_2), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_ADMISSION_REASON_2))
                    .setDisplayName(Lang.getAdmissionReason2())
                    .setEditorClass(ChoiceEditor.class);
            addSingleAttribute(KEY_ADMISSION_CAUSE + i, TCaseDetails.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_ADMISSION_CAUSE), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_ADMISSION_CAUSE))
                    .setDisplayName(Lang.getAdmissionCauseObj().getAbbreviation())
                    .setEditorClass(ChoiceEditor.class);
            addSingleAttribute(KEY_HMV + i, TCaseDetails.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_HMV), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_HMV))
                    .setDisplayName(Lang.getArtificialRespiration())
                    .setEditorClass(IntegerEditor.class);
            addSingleAttribute(KEY_DOCTOR + i, TCase.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_DOCTOR), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_DOCTOR))
                    .setDisplayName(Lang.getDoctorIdent())
                    .setEditorClass(StringEditor.class);
            addSingleAttribute(KEY_DISCHARGE_REASON_1 + i, TCaseDetails.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_DISCHARGE_REASON_1), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_DISCHARGE_REASON_1))
                    .setDisplayName(Lang.getDischargeReason())
                    .setEditorClass(ChoiceEditor.class);
            addSingleAttribute(KEY_DISCHARGE_REASON_2 + i, TCaseDetails.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_DISCHARGE_REASON_2), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_DISCHARGE_REASON_2))
                    .setDisplayName(Lang.getDischargeReason2())
                    .setEditorClass(ChoiceEditor.class);
            addSingleAttribute(KEY_CASE_NUMBER + i, TCase.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_CASE_NUMBER), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_CASE_NUMBER))
                    .setDisplayName(Lang.getCaseNumber())
                    .setEditorClass(StringEditor.class);
            //        addSingleAttribute(KEY_LOS, TCaseDetails.class)
            //                .setDisplayName(Lang.getLOS())
            //                .setEditorClass(LongEditor.class);
            addSingleAttribute(KEY_LOS_ALTERATION + i, TCaseDetails.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_LOS_ALTERATION), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_LOS_ALTERATION))
                    .setDisplayName(Lang.getLosAlterationObj().getAbbreviation())
                    .setEditorClass(LongEditor.class);
            addSingleAttribute(KEY_INSURANCE + i, TCase.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_INSURANCE), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_INSURANCE))
                    .setDisplayName(Lang.getInsurance())
                    .setEditorClass(StringEditor.class);
            addSingleAttribute(KEY_FEE_GROUP + i, TCase.class, AnalyserAttributesHelper.getReadMethodeForKey(KEY_FEE_GROUP), AnalyserAttributesHelper.getWriteMethodeForKey(KEY_FEE_GROUP))
                    .setDisplayName(Lang.getFeeGroup())
                    .setEditorClass(ChoiceEditor.class);
        }
    }
}
