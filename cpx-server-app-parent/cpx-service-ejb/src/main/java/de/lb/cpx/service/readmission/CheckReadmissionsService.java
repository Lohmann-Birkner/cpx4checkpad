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
package de.lb.cpx.service.readmission;

import de.checkpoint.server.rmServer.caseManager.RmcWiederaufnahmeIF;
import de.checkpoint.server.rmServer.payManager.RmpPeppWiederaufnahme;
import de.checkpoint.server.rmServer.payManager.RmpWiederaufnahme;
import de.checkpoint.server.rmServer.payManager.RmpWiederaufnahmeIF;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerschmann
 */
public class CheckReadmissionsService {

    private static final Logger LOG = Logger.getLogger(CheckReadmissionsService.class.getName());
    private RmpWiederaufnahme drgReadmission = null;
    private RmpPeppWiederaufnahme peppReadmission = null;

    /**
     * Anwendung der Regel zu Wiederaufnahmeregelung an die Liste der Faelle
     * eines Patienten
     *
     * @param waList Liste der Faelle eines Patienten in dem RmcWiederaufnahmeIF
     * Format
     * @param cntCase anzahl der Faelle
     * @param isPepp Flag, ob es DRG oder Pepp -Liste ist
     * @return liefert die selbe Liste mit den gefüllten Regelfelden
     */
    public List<RmcWiederaufnahmeIF> performDoRegeln(ArrayList<RmcWiederaufnahmeIF> waList, int cntCase, boolean isPepp) {
        try {
            getActive(isPepp).doRegeln(waList, cntCase);
        } catch (RemoteException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return waList;
    }

    /**
     * auswahl der Bearbeitungsklasse fuer die Regelanwendung
     *
     * @param isPepp Flag, ob es DRG oder Pepp ist
     * @return liefert den entsprechenden checker
     * @throws RemoteException
     */
    private RmpWiederaufnahmeIF getActive(boolean isPepp) throws RemoteException {
        if (isPepp) {
            peppReadmission = peppReadmission == null ? new RmpPeppWiederaufnahme(1, 1, 1, null) : peppReadmission;
            return peppReadmission;
        } else {
            drgReadmission = drgReadmission == null ? new RmpWiederaufnahme(1, 1, null) : drgReadmission;
            return drgReadmission;
        }
    }

}
