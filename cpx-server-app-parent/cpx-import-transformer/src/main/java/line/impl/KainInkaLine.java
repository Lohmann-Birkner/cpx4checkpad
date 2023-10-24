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
 *    2017  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package line.impl;

import java.util.Set;
import java.util.TreeSet;
import line.AbstractLine;
import line.Field;
import line.Field.Type;

/**
 *
 * @author Dirk Niemeier
 */
public class KainInkaLine extends AbstractLine {

    private static final long serialVersionUID = 1L;

    protected static final Set<Field> FIELDS = new TreeSet<>();

    //KAIN&INKA
    public static final Field CONTRACT_REFERENCE = createField(FIELDS, 7, "CONTRACT_REFERENCE", Type.STRING);
    public static final Field COST_UNIT_SAP = createField(FIELDS, 8, "COST_UNIT_SAP", Type.STRING);
    public static final Field CURRENT_TRANSACTION_NR = createField(FIELDS, 9, "CURRENT_TRANSACTION_NR", Type.STRING);
    public static final Field HOSPITAL_IDENTIFIER = createField(FIELDS, 10, "HOSPITAL_IDENTIFIER", Type.STRING);
    public static final Field HOSPITAL_NUMBER_PATIENT = createField(FIELDS, 11, "HOSPITAL_NUMBER_PATIENT", Type.STRING);
    public static final Field INSURANCE_CASE_NUMBER = createField(FIELDS, 12, "INSURANCE_CASE_NUMBER", Type.STRING);
    public static final Field INSURANCE_IDENTIFIER = createField(FIELDS, 13, "INSURANCE_IDENTIFIER", Type.STRING);
    public static final Field INSURANCE_REF_NUMBER = createField(FIELDS, 14, "INSURANCE_REF_NUMBER", Type.STRING);
    public static final Field MESSAGE_TYPE = createField(FIELDS, 15, "MESSAGE_TYPE", Type.STRING);
    public static final Field PROCESSING_REF = createField(FIELDS, 16, "PROCESSING_REF", Type.STRING);
    public static final Field CPX_EXTERNAL_MSG_NR = createField(FIELDS, 17, "CPX_EXTERNAL_MSG_NR", Type.STRING);
    //INKA
    public static final Field CURRENT_NR_SENDING = createField(FIELDS, 18, "CURRENT_NR_SENDING", Type.LONG);
    public static final Field IS_SENDED_FL = createField(FIELDS, 19, "IS_SENDED_FL", Type.BOOLEAN);
    public static final Field READY_4_SENDING_FL = createField(FIELDS, 20, "READY_4_SENDING_FL", Type.BOOLEAN);
    public static final Field SENDING_DATE = createField(FIELDS, 21, "SENDING_DATE", Type.DATETIME);
    //KAIN
    public static final Field RECEIVING_DATE = createField(FIELDS, 22, "RECEIVING_DATE", Type.DATETIME);

    public static KainInkaLine unserialize(final String pCsvData) {
        return (KainInkaLine) AbstractLine.unserialize(new KainInkaLine(), pCsvData);
    }

    @Override
    protected Set<Field> getFieldSet() {
        return FIELDS;
    }

}
