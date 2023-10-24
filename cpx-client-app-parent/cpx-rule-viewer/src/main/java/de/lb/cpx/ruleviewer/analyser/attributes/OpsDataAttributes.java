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

import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.ruleviewer.analyser.editors.ChoiceEditor;
import de.lb.cpx.ruleviewer.analyser.editors.DateEditor;
import de.lb.cpx.ruleviewer.analyser.editors.StringEditor;
import de.lb.cpx.shared.lang.Lang;

/**
 *
 * @author wilde
 */
public class OpsDataAttributes extends AnalyserAttributes {

    private static final String KEY_OPS_CODE = "opscCode";
    private static final String KEY_OPS_LOC = "opscLocEn";
    private static final String KEY_OPS_DATE = "opscDatum";
    private static OpsDataAttributes INSTANCE;

    private OpsDataAttributes() {
        addSingleAttribute(KEY_OPS_CODE, TCaseOps.class)
                .setDisplayName("Code")
                .setEditorClass(StringEditor.class);
        addSingleAttribute(KEY_OPS_LOC, TCaseOps.class)
                .setDisplayName(Lang.getCaseResolveLocalisationObj().getAbbreviation())
                .setEditorClass(ChoiceEditor.class);
        addSingleAttribute(KEY_OPS_DATE, TCaseOps.class)
                .setDisplayName("Ops-Datum")
                .setEditorClass(DateEditor.class);
    }

    public static OpsDataAttributes instance() {
        if (INSTANCE == null) {
            INSTANCE = new OpsDataAttributes();
        }
        return INSTANCE;
    }
}
