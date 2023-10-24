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
package de.lb.cpx.grouper.model.enums;

/**
 *
 * @author gerschmann
 */
public enum GrouperRefFieldsEn {
    KH_POSITION(0), CASE_NUMBER_POSITION(1), ADM_DATE_POSITION(2), DIS_DATE_POSITION(3),
    ADM_REASON_POSITION(4), ADM_MODE12_POSITION(5), DIS_MODE12_POSITION(6),
    BIRTH_DATE_POSITION(7), ADM_WEIGHT_POSITION(8), AGE_IN_YEARS_POSITION(9), AGE_IN_DAYS_POSITION(10),
    SEX_POSITION(11), BREATHING_HMV_POSITION(12), DIAGNOSIS_POSITION(13), PROCEDURES_POSITION(14),
    DEPARTMENTS_POSITION(15), LENGTH_OF_STAY_LOS_POSITION(16), LEAVE_DAYS_POSITION(17),
    // results
    DRG_PEPP_POSITION(18), MDC_POSITION(19), PCCL_POSITION(20), GST_POSITION(21), GPDX_POSITION(22), ET_POSITION(23);

    private final int id;

    private GrouperRefFieldsEn(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
