/* 
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.sap.container;

import de.lb.cpx.sap.importer.SapConnection;
import static de.lb.cpx.sap.importer.utils.SapStrUtils.*;
import de.lb.cpx.sap.results.SapMovementSearchResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Container für eine Bewegung (für den DB-Import aufbereitet
 * <p>
 * Überschrift: Checkpoint DRG</p>
 *
 * <p>
 * Beschreibung: Fallmanagement DRG</p>
 *
 * <p>
 * Copyright: </p>
 *
 * <p>
 * Organisation: </p>
 *
 * @author unbekannt
 * @version 2.0
 */
public class BewegungContainer {

    private static final Logger LOG = Logger.getLogger(BewegungContainer.class.getName());
    //public HashMap<String, AbteilungMapContainer> p301AbteilungenMap = null;

    private Date start = null;
    private Date ende = null;
    private long dauer = 0;
    private String aufnehmende = "0";
    private String behandelnde = "0";
    private String entlassende = "0";
    private boolean stationaer = false;
    private String abteilung = "";
    private String station = "";
    private String p301 = "";
    private String typ = "HA";
    private String typSchluessel = "1";
    private int bewegungnr = 0;
    private String seqNo = "00000";
    //public int id = 0;
    private int beatmungsdauer = 0;
    private List<DiagnosenContainer> diagList = new ArrayList<>();
    private List<ProzedurenContainer> procList = new ArrayList<>();
    private String specKey = "";
    private String wardKey = "";

    private String specDescription = "";
    private String wardDescription = "";

    /**
     * Bereitet die Bewegungsdaten für den DB-Import auf
     *
     * @param pSapConnection SAP Connection
     * @param pBewegung SapMovementSearchResult
     * @param pMandant String Mandant
     * @param pInstitution String Einrichtung
     * @param pErbringungsart Erbringungsart
     * @param pIsIntensiv Intensiv?
     */
    public BewegungContainer(
            final SapConnection pSapConnection,
            final SapMovementSearchResult pBewegung,
            final String pMandant,
            final String pInstitution,
            final int pErbringungsart,
            final boolean pIsIntensiv) {
        start = combineDate(pBewegung.getBwidt(), pBewegung.getBwizt());
        ende = combineDate(pBewegung.getBwedt(), pBewegung.getBwezp());
        abteilung = pBewegung.getOrgfa();
        station = pBewegung.getOrgpf();
        LOG.log(Level.FINEST, "Bewegung: {0}, {1}", new Object[]{abteilung, station});

        if (start != null && ende != null) {
            dauer = ende.getTime() - start.getTime();
            if (pIsIntensiv) {
                beatmungsdauer = (int) Math.ceil(dauer / 1000d / 60d / 60d);
                LOG.log(Level.FINEST, "Intensivdauer: {0}", beatmungsdauer);
            }
        }
        if (station == null) {
            station = "";
        }
        stationaer = pBewegung.getBewty().equals("1") || pBewegung.getBewty().equals("2") || pBewegung.getBewty().equals("3");
        specKey = pBewegung.getOrgfa();
        wardKey = pBewegung.getOrgpf();
        //if (getSSTVersion() > 1) {
        AbteilungMapContainer a = null, fa = null, sa = null;
        if (pSapConnection.sapProperties.getmP301AbteilungenMap() != null) {
            String key = pMandant + "_" + pInstitution + "_" + abteilung;
            a = pSapConnection.sapProperties.getmP301AbteilungenMap().get(key);
            key = "F_" + key;
            fa = pSapConnection.sapProperties.getmP301AbteilungenMap().get(key);
            key = "S_" + pMandant + "_" + pInstitution + "_" + station;
            sa = pSapConnection.sapProperties.getmP301AbteilungenMap().get(key);
        } else {
            LOG.severe("Abteilungshash nicht geladen!");
        }
        p301 = pBewegung.getOrgkbfa();
        LOG.log(Level.FINE, "Erbringungsart FA: {0}", pErbringungsart);

        if (pErbringungsart > 0) {
            if (pErbringungsart <= 2) {
                typ = "HA";
            } else {
                typ = "BA";
            }
            typSchluessel = String.valueOf(pErbringungsart);
        } else if (a != null) {
            typ = a.getTyp();
            typSchluessel = a.getTypSchluessel();
        } else {
            typ = "HA";
            typSchluessel = "1";
        }
        if (fa != null) {
            specDescription = fa.getP301();
        } else {
            specDescription = specKey;
        }
        if (sa != null) {
            wardDescription = sa.getP301();
        } else {
            wardDescription = wardKey;
        }
        seqNo = pBewegung.getLfdnr();
        try {
            bewegungnr = Integer.parseInt(pBewegung.getLfdnr()); // schon mal als int speichern
        } catch (NumberFormatException e) {
            LOG.log(Level.WARNING, "Bewegungsnr. ungültig: " + pBewegung.getLfdnr(), e);
        }
    }

    /**
     * @param entlassende the entlassende to set
     */
    public void setEntlassende(String entlassende) {
        this.entlassende = entlassende;
    }

    /**
     * @param aufnehmende the aufnehmende to set
     */
    public void setAufnehmende(String aufnehmende) {
        this.aufnehmende = aufnehmende;
    }

    /**
     * @param behandelnde the behandelnde to set
     */
    public void setBehandelnde(String behandelnde) {
        this.behandelnde = behandelnde;
    }

    /**
     * @return the start
     */
    public Date getStart() {
        return start == null ? null : new Date(start.getTime());
    }

    /**
     * @return the ende
     */
    public Date getEnde() {
        return ende == null ? null : new Date(ende.getTime());
    }

    /**
     * @return the dauer
     */
    public long getDauer() {
        return dauer;
    }

    /**
     * @return the aufnehmende
     */
    public String getAufnehmende() {
        return aufnehmende;
    }

    /**
     * @return the behandelnde
     */
    public String getBehandelnde() {
        return behandelnde;
    }

    /**
     * @return the entlassende
     */
    public String getEntlassende() {
        return entlassende;
    }

    /**
     * @return the stationaer
     */
    public boolean isStationaer() {
        return stationaer;
    }

    /**
     * @return the abteilung
     */
    public String getAbteilung() {
        return abteilung;
    }

    /**
     * @return the station
     */
    public String getStation() {
        return station;
    }

    /**
     * @return the p301
     */
    public String getP301() {
        return p301;
    }

    /**
     * @return the typ
     */
    public String getTyp() {
        return typ;
    }

    /**
     * @return the typSchluessel
     */
    public String getTypSchluessel() {
        return typSchluessel;
    }

    /**
     * @return the bewegungnr
     */
    public int getBewegungnr() {
        return bewegungnr;
    }

    /**
     * @return the seqNo
     */
    public String getSeqNo() {
        return seqNo;
    }

    /**
     * @return the beatmungsdauer
     */
    public int getBeatmungsdauer() {
        return beatmungsdauer;
    }

    /**
     * @return the diagList
     */
    public List<DiagnosenContainer> getDiagList() {
        return new ArrayList<>(diagList);
    }

    /**
     * @return the procList
     */
    public List<ProzedurenContainer> getProcList() {
        return new ArrayList<>(procList);
    }

    /**
     * @return the spec_key
     */
    public String getSpecKey() {
        return specKey;
    }

    /**
     * @return the ward_key
     */
    public String getWardKey() {
        return wardKey;
    }

    /**
     * @return the spec_description
     */
    public String getSpecDescription() {
        return specDescription;
    }

    /**
     * @return the ward_description
     */
    public String getWardDescription() {
        return wardDescription;
    }

    /**
     * Fügt der Bewegung eine Diagnose hinzu
     *
     * @param pDiagnose DiagnosenContainer
     */
    public void addDiag(final DiagnosenContainer pDiagnose) {
        if (pDiagnose == null) {
            return;
        }
        //getDiagList().add(pDiagnose);
        diagList.add(pDiagnose);
    }

    /**
     * Fügt der Bewegung eine Prozedur hinzu
     *
     * @param pProcedure ProzedurenContainer
     */
    public void addProc(final ProzedurenContainer pProcedure) {
        if (pProcedure == null) {
            return;
        }
        //getProcList().add(pProcedure);
        procList.add(pProcedure);
    }

}
