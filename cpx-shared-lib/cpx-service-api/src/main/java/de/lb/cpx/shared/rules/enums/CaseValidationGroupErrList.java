/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.rules.enums;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.text.MessageFormat;
import java.util.List;

/**
 * Errors detected by validating and grouping, that are to tranfer to the
 * analyser client
 *
 * @author gerschmann
 */
public enum CaseValidationGroupErrList implements CpxEnumInterface<Integer> {
    NO_HOSPITAL_CASE(0, true, Lang.NO_HOSPITAL_CASE, true),
    NOT_DEFINED_CASE_TYPE(1, true, Lang.NOT_DEFINED_CASE_TYPE, true),
    NO_CASE_DETAILS_FOUND(2, true, Lang.NO_CASE_DETAILS_FOUND, true),
    NO_ADMISSION_DATE_FOUND(3, true, Lang.NO_ADMISSION_DATE_FOUND, true),
    NO_DISCHARGE_DATE_FOUND(4, true, Lang.NO_DISCHARGE_DATE_FOUND, true),
    NO_DISCHARGE_REASON_FOUND(5, true, Lang.NO_DISCHARGE_REASON_FOUND),
    NO_ADMISSION_MODE_FOUND(6, true, Lang.NO_ADMISSION_MODE_FOUND),
    ADMISSION_DATE_FOR_2013(7, true,Lang.ADMISSION_DATE_FOR_2013, true),
    NO_DEPARTMENT_ADMISSION_DATE(8, true, Lang.NO_DEPARMENT_ADMMISION_DATE, true),
    NO_DEPARTMENT_DISCHARGE_DATE(9, true, Lang.NO_DEPARTMENT_DISCHARGE_DATE, true),
    NO_DEPARTMENT_CODED(10, true, Lang.NO_DEPARTMENT_CODED),
    NO_ADMISSION_CAUSE_FOUND(11, true, Lang.NO_ADMISSION_CAUSE_FOUND),
    NO_DISCHARGE_REASON3_FOUND(12, true, Lang.NO_DISCHARGE_REASON_3_FOUND),
    NO_ADMISSION_REASON_FOUND(13, true, Lang.NO_ADMISSION_REASON_FOUND),
    NO_ADMISSION_REASON34_FOUND(14, true, Lang.NO_ADMISSION_REASON_34_FOUND),
    GROUP_GST01(15, false, Lang.GROUPER_STATUS_INVALID_PRINCIPAL_ICD),//Ungültige oder fehlende Hauptdiagnose"),
    GROUP_GST02(16, false, Lang.GROUPER_STATUS_INACCEPTABLE_PRINCIPAL_ICD), //Diagnosekode als Hauptdiagnose nicht zulässig"),
    GROUP_GST03(17, false, Lang.GROUPER_STATUS_INVALID_CASE), //Datensatz entspricht keinem der Kriterien für irgendeine DRG"),
    GROUP_GST04(18, false, Lang.GROUPER_STATUS_INVALID_AGE), //Ungültiges Alter"),
    GROUP_GST05(19, false, Lang.GROUPER_STATUS_INVALID_GENDER), //Ungültiges Geschlecht"),
    GROUP_GST06(20, false, Lang.GROUPER_STATUS_INVALID_ADM_DIS_REASON_MODE), //Ungültiger Entlas-sungsgrund, Aufnah-meanlass oder Auf-nahmegrund"),
    GROUP_GST07(21, false, Lang.GROUPER_STATUS_INVALID_WEIGHT), //Ungültiges Aufnahmegewicht"),
    GROUP_GST08(22, false, Lang.GROUPER_STATUS_INVALID_LOS), //Ungültige Verweildauer");//Ungültige Verweildauer");
    GROUP_pdx1(23, false, Lang.GROUP_RESULT_PDX_INVALID_PDX_CODE),//Ungültiger ICD-10-GM-Kode als Hauptdiagnose
    GROUP_pdx2(24, false, Lang.GROUP_RESULT_PDX_VWXY_PDX_CODE), //V-,W-,X-,Y-Kode als Hauptdiagnose
    GROUP_pdx3(25, false, Lang.GROUP_RESULT_PDX_IMPROPER_PDX_CODE),
    DISCHARGE_DATE_BEFORE_ADMISSION_DATE(26, true, Lang.DISCHARGE_DATE_BEFORE_ADMISSION_DATE),
    DRG_CASE_DAY_CARE(27, true, Lang.DRG_CASE_DAY_CARE),
    CASE_DAY_CARE(28, true, Lang.CASE_DAY_CARE);

    private final int id;
    private final String langKey;
    private final boolean isValidate;
    private final boolean isSevere;

    private CaseValidationGroupErrList(final Integer id, boolean isValidate, final String langKey) {
        this(id, isValidate, langKey, false);

    }

    private CaseValidationGroupErrList(final Integer id, boolean isValidate, final String langKey, boolean isSevere) {
        this.langKey = langKey;
        this.id = id;
        this.isValidate = isValidate;
        this.isSevere = isSevere;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
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

    @Override
    public String getIdStr() {
        return String.valueOf(id);
    }

    @Override
    public int getIdInt() {
        return id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    /**
     * error was detected by validating of input
     *
     * @return true if it was input, false if it is from grouping
     */
    public boolean getIsValidate() {
        return isValidate;
    }

    /**
     * grouping is not allowed if isSevere is true
     *
     * @return is is severe
     */
    public boolean isSevere() {
        return isSevere;
    }

    public static CaseValidationGroupErrList find2EnumName(String name) {
        CaseValidationGroupErrList[] values = CaseValidationGroupErrList.values();
        for (CaseValidationGroupErrList value : values) {
            if (!value.getIsValidate() && value.name().contains(name)) {
                return value;
            }
        }
        return null;
    }

    public static boolean hasSevere(List<CaseValidationGroupErrList> errorList) {
        if (errorList == null) {
            return false;
        }

        if (errorList.stream().anyMatch((value) -> (value.isSevere()))) {
            return true;
        }
        return false;
    }
}
