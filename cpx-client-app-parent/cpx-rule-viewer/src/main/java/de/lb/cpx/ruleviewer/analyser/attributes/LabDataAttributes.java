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

import de.lb.cpx.model.TLab;
import de.lb.cpx.ruleviewer.analyser.editors.DateOnlyEditor;
import de.lb.cpx.ruleviewer.analyser.editors.DoubleEditor;
import de.lb.cpx.ruleviewer.analyser.editors.StringEditor;
import de.lb.cpx.shared.lang.Lang;

/**
 *
 * @author wilde
 */
public class LabDataAttributes extends AnalyserAttributes {

    public static final String KEY_LAB_DESC = "labDescription";
    public static final String KEY_LAB_TEXT = "labText";
    public static final String KEY_LAB_UNIT = "labUnit";
    public static final String KEY_LAB_VALUE = "labValue";
    public static final String KEY_LAB_DATE = "labAnalysisDate";
    private static LabDataAttributes INSTANCE;

    private LabDataAttributes() {
        addSingleAttribute(KEY_LAB_DESC, TLab.class)
                .setDisplayName(Lang.getRulesLaborbeschreibung())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_LAB_TEXT, TLab.class)
                .setDisplayName(Lang.getRulesTxtCritLaborTextDis())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_LAB_UNIT, TLab.class)
                .setDisplayName(Lang.getRulesTxtCritLaborUnitDis())
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_LAB_VALUE, TLab.class)
                .setDisplayName(Lang.getRulesTxtLaboratoryValue())
                .setEditorClass(DoubleEditor.class);
        addSingleAttribute(KEY_LAB_DATE, TLab.class)
                .setDisplayName(Lang.getRulesTxtCritLaborDateDis())
                .setEditorClass(DateOnlyEditor.class);
    }

    public static LabDataAttributes instance() {
        if (INSTANCE == null) {
            INSTANCE = new LabDataAttributes();
        }
        return INSTANCE;
    }

}
