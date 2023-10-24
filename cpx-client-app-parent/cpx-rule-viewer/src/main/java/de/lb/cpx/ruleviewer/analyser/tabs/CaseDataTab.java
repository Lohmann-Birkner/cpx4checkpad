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
package de.lb.cpx.ruleviewer.analyser.tabs;

import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserAttribute;
import de.lb.cpx.ruleviewer.analyser.attributes.CaseDataAttributes;

/**
 *
 * @author wilde
 */
public class CaseDataTab extends AsyncAnalyserAttributesTab<CaseDataAttributes> {

    public CaseDataTab() {
        super("Fall-Daten", CaseDataAttributes.instance());
        setBeanFactory((AnalyserAttribute en) -> {
            if (getCase() == null) {
                return null;
            }
            if (en.getBeanClass() == TCaseDetails.class) {
                return getCase().getCurrentLocal();
            }
            return getCase();
        });
    }

}
