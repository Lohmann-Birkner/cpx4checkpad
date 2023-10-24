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

import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.ruleviewer.analyser.editors.CurrencyEditor;
import de.lb.cpx.ruleviewer.analyser.editors.DateEditor;
import de.lb.cpx.ruleviewer.analyser.editors.IntegerEditor;
import de.lb.cpx.ruleviewer.analyser.editors.StringEditor;

/**
 *
 * @author wilde
 */
public class FeeDataAttributes extends AnalyserAttributes {

    public static final String KEY_FEE_KEY = "feecFeekey";
    public static final String KEY_FEE_VALUE = "feecValue";
    public static final String KEY_FEE_COUNT = "feecCount";
    public static final String KEY_FEE_FROM = "feecFrom";
    public static final String KEY_FEE_TO = "feecTo";
    public static final String KEY_FEE_UNBILLED_DAYS = "feecUnbilledDays";
    private static FeeDataAttributes INSTANCE;

    private FeeDataAttributes() {
        addSingleAttribute(KEY_FEE_KEY, TCaseFee.class)
                .setDisplayName("Entgelt")
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_FEE_VALUE, TCaseFee.class)
                .setDisplayName("Entgelteinzelbetrag")
                .setEditorClass(CurrencyEditor.class);
        addSingleAttribute(KEY_FEE_COUNT, TCaseFee.class)
                .setDisplayName("Entgeltanzahl je Entgelt")
                .setEditorClass(IntegerEditor.class);
        addSingleAttribute(KEY_FEE_FROM, TCaseFee.class)
                .setDisplayName("Von")
                .setEditorClass(DateEditor.class);
        addSingleAttribute(KEY_FEE_TO, TCaseFee.class)
                .setDisplayName("Bis")
                .setEditorClass(DateEditor.class);
        addSingleAttribute(KEY_FEE_UNBILLED_DAYS, TCaseFee.class)
                .setDisplayName("T.o.B.")
                .setEditorClass(IntegerEditor.class);
    }

    public static FeeDataAttributes instance() {
        if (INSTANCE == null) {
            INSTANCE = new FeeDataAttributes();
        }
        return INSTANCE;
    }

}
