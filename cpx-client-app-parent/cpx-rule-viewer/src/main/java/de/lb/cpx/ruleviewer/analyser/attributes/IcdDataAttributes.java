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

import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.ruleviewer.analyser.editors.BooleanEditor;
import de.lb.cpx.ruleviewer.analyser.editors.ChoiceEditor;
import de.lb.cpx.ruleviewer.analyser.editors.IcdRefTypeEditor;
import de.lb.cpx.ruleviewer.analyser.editors.StringEditor;
import de.lb.cpx.shared.lang.Lang;

/**
 *
 * @author wilde
 */
public class IcdDataAttributes extends AnalyserAttributes {

    public static final String KEY_ICD_CODE = "icdcCode";
    public static final String KEY_ICD_LOC = "icdcLocEn";
    public static final String KEY_ICD_MAIN_DIAG = "icdcIsHdxFl";
    public static final String KEY_ICD_TYPE = "icdcTypeEn";
    public static final String KEY_ICD_REF_ICD = "icdcReftypeEn";
    private static IcdDataAttributes INSTANCE;

    private IcdDataAttributes() {
        addSingleAttribute(KEY_ICD_CODE, TCaseIcd.class)
                .setDisplayName("Code")
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_ICD_LOC, TCaseIcd.class)
                .setDisplayName(Lang.getCaseResolveLocalisationObj().getAbbreviation())
                .setEditorClass(ChoiceEditor.class);
        addSingleAttribute(KEY_ICD_MAIN_DIAG, TCaseIcd.class)
                .setDisplayName("HD")
                .setEditorClass(BooleanEditor.class);
        addSingleAttribute(KEY_ICD_TYPE, TCaseIcd.class)
                .setDisplayName("Typ")
                .setEditorClass(ChoiceEditor.class);
        addSingleAttribute(KEY_ICD_REF_ICD, TCaseIcd.class)
                .setDisplayName("Ref.ICD")
                .setEditorClass(IcdRefTypeEditor.class);
    }

    public static IcdDataAttributes instance() {
        if (INSTANCE == null) {
            INSTANCE = new IcdDataAttributes();
        }
        return INSTANCE;
    }
}
