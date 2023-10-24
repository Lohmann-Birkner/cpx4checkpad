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
import de.lb.cpx.model.TInsurance;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.TPatientDetails;
import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserAttribute;
import de.lb.cpx.ruleviewer.analyser.attributes.PatientDataAttributes;

/**
 *
 * @author wilde
 */
public class PatientDataTab extends AsyncAnalyserAttributesTab<PatientDataAttributes> {

    public PatientDataTab() {
        super("Patienten-Daten", PatientDataAttributes.instance());
        setBeanFactory((AnalyserAttribute en) -> {
            if (getCase() == null) {
                return null;
            }
            if (en.getBeanClass() == TCaseDetails.class) {
                return getCase().getCurrentLocal();
            }
            if (en.getBeanClass() == TPatient.class) {
                return getCase().getPatient();
            }
            if (en.getBeanClass() == TPatientDetails.class) {
                return getCase().getPatient().getCurrentDetail();
            }
            if (en.getBeanClass() == TInsurance.class) {
 //               return getCase().getPatient().getCurrentInsurance();
                TInsurance tmpIns = getCase().getPatient().getCurrentInsurance();
                if(tmpIns == null){
                    tmpIns = new TInsurance();
                    tmpIns.setInsIsActualFl(true);
                    getCase().getPatient().setCurrentInsurance(tmpIns);
                }
                return tmpIns;
            }
            return getCase();
        });
    }

}
