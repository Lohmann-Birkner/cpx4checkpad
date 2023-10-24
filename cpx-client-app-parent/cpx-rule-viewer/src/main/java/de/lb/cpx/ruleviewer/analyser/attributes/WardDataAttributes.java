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

import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.ruleviewer.analyser.editors.StringEditor;
import de.lb.cpx.shared.lang.Lang;

/**
 * Ward Attribute to edit this in Departments when its set
 *
 * @author wilde
 */
public class WardDataAttributes extends AnalyserAttributes {

    public static final String KEY_WARD_CODE = "wardcIdent";
    private static WardDataAttributes INSTANCE;

    private WardDataAttributes() {
        addSingleAttribute(KEY_WARD_CODE, TCaseWard.class)
                .setDisplayName(Lang.getWard())
                .setEditorClass(StringEditor.class);
    }

    public static WardDataAttributes instance() {
        if (INSTANCE == null) {
            INSTANCE = new WardDataAttributes();
        }
        return INSTANCE;
    }
}
