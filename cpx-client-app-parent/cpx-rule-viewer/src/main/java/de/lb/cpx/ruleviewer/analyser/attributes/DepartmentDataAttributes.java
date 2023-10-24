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

import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.ruleviewer.analyser.editors.BooleanEditor;
import de.lb.cpx.ruleviewer.analyser.editors.DateEditor;
import de.lb.cpx.ruleviewer.analyser.editors.StringEditor;
import de.lb.cpx.shared.lang.Lang;

/**
 *
 * @author wilde
 */
public class DepartmentDataAttributes extends AnalyserAttributes {

    private static DepartmentDataAttributes INSTANCE;
    public static final String KEY_DEP_NAME = "depKey301";
    public static final String KEY_DEP_ADM_FL = "depcIsAdmissionFl";
    public static final String KEY_DEP_DIS_FL = "depcIsDischargeFl";
    public static final String KEY_DEP_THR_FL = "depcIsTreatingFl";
    public static final String KEY_DEP_INT_FL = "depcIsBedIntensivFl";
    public static final String KEY_DEP_ADM_DATE = "depcAdmDate";
    public static final String KEY_DEP_DIS_DATE = "depcDisDate";
    public static final String KEY_DEP_ADMOD = "depcAdmodEn";
    public static final String KEY_DEP_HMV = "depcHmv";

    private DepartmentDataAttributes() {
        addSingleAttribute(KEY_DEP_NAME, TCaseDepartment.class)
                .setDisplayName(Lang.getRulesCaseSpec())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_DEP_ADM_FL, TCaseDepartment.class)
                .setDisplayName("Aufn. FA")
                .setEditorClass(BooleanEditor.class);
        addSingleAttribute(KEY_DEP_DIS_FL, TCaseDepartment.class)
                .setDisplayName("Entl. FA")
                .setEditorClass(BooleanEditor.class);
        addSingleAttribute(KEY_DEP_THR_FL, TCaseDepartment.class)
                .setDisplayName("B. FA")
                .setEditorClass(BooleanEditor.class);
//        addSingleAttribute(KEY_DEP_INT_FL, TCaseDepartment.class)
//                .setDisplayName("Int. FA")
//                .setEditorClass(BooleanEditor.class);
        addSingleAttribute(KEY_DEP_ADM_DATE, TCaseDepartment.class)
                .setDisplayName(Lang.getAdmissionDateObj().getAbbreviation())
                .setEditorClass(DateEditor.class);
        addSingleAttribute(KEY_DEP_DIS_DATE, TCaseDepartment.class)
                .setDisplayName(Lang.getDischargeDateObj().getAbbreviation())
                .setEditorClass(DateEditor.class);
//        addSingleAttribute(KEY_DEP_ADMOD, TCaseDepartment.class)
//                .setDisplayName("Aufn. Grund")
//                .setEditorClass(ChoiceEditor.class);
//        addSingleAttribute(KEY_DEP_HMV, TCaseDepartment.class)
//                .setDisplayName(Lang.getArtificialVentilationObj().getAbbreviation())
//                .setEditorClass(IntegerEditor.class);

    }

    public static DepartmentDataAttributes instance() {
        if (INSTANCE == null) {
            INSTANCE = new DepartmentDataAttributes();
        }
        return INSTANCE;
    }

    public enum DisplayMode {
        DEPARTMENT_ONLY, ICD, OPS, ALL;
    }
}
