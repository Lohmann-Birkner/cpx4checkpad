/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahmeIF;
import de.lb.cpx.model.enums.CaseTypeEn;
import java.util.ArrayList;
import javax.ejb.Local;

/**
 *
 * @author gerschmann
 */
@Local
public interface ReadmissionService {

    /**
     * Anwenden der Wiederaufnahmeregelung zu der Liste der Fälle
     *
     * @param candidates Liste der Fälle eines Patienten in RmcWiederaufnahmeIF
     * form
     * @param isPepp Flag DRG/PEPP
     * @return liefert dieselbe Liste mit den gefüllten Felden 1-10 Regeln
     *
     */
    ArrayList<RmcWiederaufnahmeIF> checkReadmissions(ArrayList<RmcWiederaufnahmeIF> candidates, boolean isPepp);

}
