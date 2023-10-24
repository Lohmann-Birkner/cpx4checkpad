/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.dto.acg;

import java.io.Serializable;

/**
 *
 * @author niemeier
 */
public enum AcgIndexType implements Serializable {
    NP("Nicht vorhanden", false, false, false), //NP = Not present
    ICD("Diagnosen", true, false, false), //ICD = Diagnosis
    Rx("Medikamente", false, true, false), //Rx-Präparate sind verschreibungspflichtige bzw. rezeptpflichtige Medikamente
    BTH("Diagnosen & Medikamente", true, true, false), //BTH = Both
    TRT("Durchgehende Behandlung", true, true, true); //TRT = Treatment

    public final String description;
    public final boolean diagnosis;
    public final boolean medicins;
    public final boolean treatment;

    private AcgIndexType(final String pDescription, final boolean pDiagnosis, final boolean pMedicins, final boolean pTreatment) {
        this.description = pDescription == null ? "" : pDescription.trim();
        this.diagnosis = pDiagnosis;
        this.medicins = pMedicins;
        this.treatment = pTreatment;
    }

    @Override
    public String toString() {
        return "AcgIndexType{" + "description=" + description + ", diagnosis=" + diagnosis + ", medicins=" + medicins + ", treatment=" + treatment + '}';
    }

}
