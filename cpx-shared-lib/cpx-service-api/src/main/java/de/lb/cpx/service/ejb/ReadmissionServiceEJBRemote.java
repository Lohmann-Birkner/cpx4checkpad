/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.enums.CaseTypeEn;
import javax.ejb.Remote;

/**
 *
 * @author gerschmann
 */
@Remote
public interface ReadmissionServiceEJBRemote {
    
    /**
     * prüft die wiederaufnahmen für einen Patienten abhängig von dem
     * Falltyp(DRG/PEPP)
     *
     * @param patientId id des Patienten
     * @param type Type des Falles
     * @param isLocal true - werden lokale Fälle analysiert, false - externe
     * @param model Groupermodel für DRG zu berücksichtigen
     * @param isAuto Autogroupen
     * @return Anzahl der überprüften Fällen des Typs type
     */
    int checkReadmissions4Patient(Long patientId, CaseTypeEn type, boolean isLocal, GDRGModel model, boolean isAuto);

}
