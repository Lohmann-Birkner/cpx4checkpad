/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  Bohm - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.filter.enums;

import de.lb.cpx.model.enums.BooleanEn;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.RuleTypeEn;
import static de.lb.cpx.shared.filter.enums.WorkingListAttributes.csStatusEn;
import static de.lb.cpx.shared.filter.enums.WorkingListAttributes.csdAdmissionDate;
import static de.lb.cpx.shared.filter.enums.WorkingListAttributes.csdAdmissionDateFrom;
import static de.lb.cpx.shared.filter.enums.WorkingListAttributes.csdAdmissionDateTo;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Bohm
 */
public class RuleListAttributes extends WorkingListAttributes {

    private static RuleListAttributes instance = null;
    public static final String id = "id";
    public static final String checkResultId = "checkResultId"; //t_check_result.id
    public static final String chkCwSimulDiff = "chkCwSimulDiff";
    public static final String chkCwSimulDiffEqual = "chkCwSimulDiffEqual";
    public static final String chkCwSimulDiffFrom = "chkCwSimulDiffFrom";
    public static final String chkCwSimulDiffTo = "chkCwSimulDiffTo";
    public static final String ruleDescription = "ruleDescription";
    public static final String crgrId = "crgrId";
    public static final String ruleSuggestion = "ruleSuggestion";
    public static final String typeText = "typeText";
    public static final String xRuleNumber = "xRuleNumber";
    public static final String caseFees = "caseFees";
    public static final String caseIcds = "caseIcds";
    public static final String caseOpses = "caseOpses";
    public static final String departmentMd = "departmentMd";
    public static final String admDiagnosis = "admDiagnosis"; //aufnehmende Diagnose
    public static final String hosDiagnosis = "hosDiagnosis"; //einweisende Diagnose
    public static final String rules = "rules";
    public static final String crgrCategory = "crgrCategory";
    public static final String ruleSelected = "ruleSelected";

    protected static final List<String> RULE_COLUMNS = Arrays.asList(WorkingListAttributes.id, checkResultId, crgrId,
            chkCwSimulDiff, csDrg, csCaseNumber, cwEffective, csdLos, csdHmv, csdAgeYears, csStatusEn, csdAdmissionDate,
            ruleDescription, crgrCategory, xRuleNumber, ruleSuggestion, typeText, csCaseTypeEn, ruleSelected);

    protected static final List<String> RULE_FIELD_NAMES = Arrays.asList(Lang.getRuleColumnCategory(), //Lang.RULE_COLUMN_CATEGORY),
            Lang.getRuleColumnCaption(), //Lang.RULE_COLUMN_CAPTION, 
            Lang.getRuleColumnSuggestion(), //Lang.RULE_COLUMN_SUGGESTION, 
            Lang.getRuleColumnStatus(), //Lang.RULE_COLUMN_STATUS, 
            Lang.getRuleColumnNumber() //Lang.RULE_COLUMN_NUMBER
    );

    protected static final Map<String, String> RULE_FIELDS_NAMES_2_FIELD;

    static {
        RULE_FIELDS_NAMES_2_FIELD = new HashMap<>();
        RULE_FIELDS_NAMES_2_FIELD.put(Lang.getRuleColumnCategory(), //Lang.RULE_COLUMN_CATEGORY,
                "crgrCategory");
        RULE_FIELDS_NAMES_2_FIELD.put(Lang.getRuleColumnCaption(), //Lang.RULE_COLUMN_CAPTION, 
                "crgrCaption");
        RULE_FIELDS_NAMES_2_FIELD.put(Lang.getRuleColumnSuggestion(), //Lang.RULE_COLUMN_SUGGESTION, 
                "crgrSuggText");
        RULE_FIELDS_NAMES_2_FIELD.put(Lang.getRuleColumnStatus(), //Lang.RULE_COLUMN_STATUS, 
                "crgrRuleErrorType");
        RULE_FIELDS_NAMES_2_FIELD.put(Lang.getRuleColumnNumber(), //Lang.RULE_COLUMN_NUMBER 
                "crgrNumber");
    }

    public static List<String> getRuleFieldNames() {
        return new ArrayList<>(RULE_FIELD_NAMES);
    }

    public static Map<String, String> getRuleFieldsNames2Field() {
        return new HashMap<>(RULE_FIELDS_NAMES_2_FIELD);
    }

    protected RuleListAttributes() {
        initKeys();
    }

    public static List<String> getRuleColumns() {
        return new ArrayList<>(RULE_COLUMNS);
    }

    public static synchronized RuleListAttributes instance() {
        if (instance == null) {
            instance = new RuleListAttributes();
        }
        return instance;
    }

//    @Override
//    protected void initNoColumnKeys() {
//        add(rules, "T_CHECK_RESULT", "CRGR_ID", "Regel-ID")
//                .setFormat(new SearchListFormatString())
//                .setSize(200)
//                .setNoColumn(true);
//    }
    @Override
    protected final void initKeys() {
        add(id, "T_CASE", "ID", "CaseId") //Fall-ID
                .setVisible(false);
        initNoColumnKeys();

        add(checkResultId, "T_CHECK_RESULT", "ID", "CheckResultId").
                setVisible(false);

        add(chkCwSimulDiff, "T_CHECK_RESULT", "CHK_CW_SIMUL_DIFF", "Delta CW")
                .setFormat(new SearchListFormatDouble())//new SearchListFormatCommon().setDataType(String.class))
                .setSize(130)
                //                .addEqualChild(add(chkCwSimulDiffEqual, getByKey(chkCwSimulDiff), "Delta CW"))
                .addBetweenChildren(
                        add(chkCwSimulDiffFrom, getByKey(chkCwSimulDiff), "Delta CW von"),
                        add(chkCwSimulDiffTo, getByKey(chkCwSimulDiff), "Delta CW bis")
                );
        add(csDrg, "T_GROUPING_RESULTS", "GRPRES_CODE", Lang.DRG)
                .setFormat(new SearchListFormatString())
                .setSize(130);

        add(csCaseNumber, "T_CASE", "CS_CASE_NUMBER", Lang.CASE_NUMBER)
                .setFormat(new SearchListFormatString())
                .setSize(100);

        add(cwEffective, "T_GROUPING_RESULTS", "DRGC_CW_EFFECTIV", Lang.CASE_CW_EFFECTIVE)
                .setFormat(new SearchListFormatDouble())//new SearchListFormatCommon().setDataType(String.class))
                .setSize(130)
                //                .addEqualChild(add(cwEffectiveEqual, getByKey(cwEffective), Lang.CASE_CW_EFFECTIVE))
                .addBetweenChildren(
                        add(cwEffectiveFrom, getByKey(cwEffective), Lang.CASE_CW_EFFECTIVE_FROM),
                        add(cwEffectiveTo, getByKey(cwEffective), Lang.CASE_CW_EFFECTIVE_TO)
                );

        add(csdLos, "T_CASE_DETAILS", "LOS", Lang.LENGTH_OF_STAY) //Verweildauer
                .setFormat(new SearchListFormatInteger()
                        .setMaxLength(10))
                .setSize(60)
                .addEqualChild(add(csdLosEqual, getByKey(csdLos), Lang.LENGTH_OF_STAY))
                .addBetweenChildren(
                        add(csdLosFrom, getByKey(csdLos), Lang.LENGTH_OF_STAY_FROM),
                        add(csdLosTo, getByKey(csdLos), Lang.LENGTH_OF_STAY_TO)
                );

        add(csdHmv, "T_CASE_DETAILS", "HMV", Lang.ARTIFICIAL_VENTILATION) //Beatmungsstunden
                .setFormat(new SearchListFormatInteger()
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                .setSize(60);

        add(csdAgeYears, "T_CASE_DETAILS", "AGE_YEARS", Lang.AGE_IN_YEARS) //Alter in Jahren
                .setFormat(new SearchListFormatInteger()
                        .setMaxLength(10)
                        .setSignType(SearchListFormatInteger.SIGN_TYPE.POSITIVE))
                .setSize(40)
                .addEqualChild(add(csdAgeYearsEqual, getByKey(csdAgeYears), Lang.AGE_IN_YEARS))
                .addBetweenChildren(add(csdAgeYearsFrom, getByKey(csdAgeYears), Lang.AGE_IN_YEARS_FROM),
                        add(csdAgeYearsTo, getByKey(csdAgeYears), Lang.AGE_IN_YEARS_TO)
                );

        add(csStatusEn, "T_CASE", "CS_STATUS_EN", Lang.CASE_STATUS) //FS (Fallstatus)
                .setFormat(new SearchListFormatEnum(CaseStatusEn.class))
                .setSize(70);

        add(csdAdmissionDate, "T_CASE_DETAILS", "ADMISSION_DATE", Lang.ADMISSION_DATE) //Aufnahmedatum
                .setFormat(new SearchListFormatDateTime())
                .setSize(130)
                .addBetweenChildren(
                        add(csdAdmissionDateFrom, "T_CASE_DETAILS", "ADMISSION_DATE", Lang.ADMISSION_DATE_FROM)
                                //.setOperator(SearchListAttribute.OPERATOR.GREATER_THAN_OR_EQUAL_TO)
                                .setFormat(new SearchListFormatDateTime()),
                        add(csdAdmissionDateTo, "T_CASE_DETAILS", "ADMISSION_DATE", Lang.ADMISSION_DATE_TO)
                                //.setOperator(SearchListAttribute.OPERATOR.LESS_THAN_OR_EQUAL_TO)
                                //.setNoFilter(true)
                                .setFormat(new SearchListFormatDateTime())
                );
        add(ruleSelected, "VIEW_CASE_2_RULE_SELECTION", "SELECTED", Lang.RULE_COLUMN_RELEVACE).setSize(130).
                setFormat(new SearchListFormatEnum(BooleanEn.class));
        add(ruleDescription, "", "crgrCaption", Lang.getRuleColumnCaption())//Lang.RULE_COLUMN_CAPTION)
                .setFormat(new SearchListFormatString())
                .setSize(130)
                .setIsClientSide(true);

        add(crgrCategory, "", "crgrCategory", Lang.getRuleColumnCategory())//Lang.RULE_COLUMN_CATEGORY)
                .setFormat(new SearchListFormatString())
                .setSize(130)
                .setIsClientSide(true);

        add(ruleSuggestion, "", "crgrSuggText", Lang.getRuleColumnSuggestion())//Lang.RULE_COLUMN_SUGGESTION)
                .setFormat(new SearchListFormatString())
                .setSize(130)
                .setIsClientSide(true);

        add(crgrId, "T_CHECK_RESULT", "CRGR_ID", "Regel ID")
                .setFormat(new SearchListFormatString()
                        .setMaxLength(20))
                .setSize(40).setOperator(SearchListAttribute.OPERATOR.EQUAL)
                .setVisible(false);

        add(typeText, "", "crgrRuleErrorType", Lang.getRuleColumnStatus())//Lang.RULE_COLUMN_STATUS)
                .setFormat(new SearchListFormatEnum(RuleTypeEn.class))
                .setSize(130)
                .setIsClientSide(true);

        add(xRuleNumber, "", "crgrNumber", Lang.getRuleColumnNumber())//Lang.RULE_COLUMN_NUMBER)
                .setFormat(new SearchListFormatString())
                .setSize(130)
                .setIsClientSide(true);

        add(csCaseTypeEn, "T_CASE", "CS_CASE_TYPE_EN", Lang.CASE_TYPE) //Fallart (PEPP, DRG)
                .setFormat(new SearchListFormatEnum(CaseTypeEn.class))
                .setSize(60)
                .setVisible(false);

    }

}
