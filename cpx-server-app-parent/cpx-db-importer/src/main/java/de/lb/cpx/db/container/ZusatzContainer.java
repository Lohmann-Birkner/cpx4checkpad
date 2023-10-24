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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.db.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ZusatzContainer<beatmungszeitType extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int beatmungsdauer = 0;
    private int geburtsgewicht = 0;
    private boolean fallFreigabe = false;
    private boolean abrechnungsFreigabe = false;
    private boolean mcAnfrage = false;
    private String bemFallFreigabe = "";
    private String bemAbrechnungsFreigabe = "";
    private String bemMcAnfrage = "";
    private int bewegungsBeatmungsDauer = 0;
    private ArrayList<beatmungszeitType> beatmungszeiten = new ArrayList<>();

    /**
     * @return the beatmungsdauer
     */
    public int getBeatmungsdauer() {
        return beatmungsdauer;
    }

    /**
     * @return the geburtsgewicht
     */
    public int getGeburtsgewicht() {
        return geburtsgewicht;
    }

    /**
     * @return the fallFreigabe
     */
    public boolean isFallFreigabe() {
        return fallFreigabe;
    }

    /**
     * @return the abrechnungsFreigabe
     */
    public boolean isAbrechnungsFreigabe() {
        return abrechnungsFreigabe;
    }

    /**
     * @return the mcAnfrage
     */
    public boolean isMcAnfrage() {
        return mcAnfrage;
    }

    /**
     * @return the bemFallFreigabe
     */
    public String getBemFallFreigabe() {
        return bemFallFreigabe;
    }

    /**
     * @return the bemAbrechnungsFreigabe
     */
    public String getBemAbrechnungsFreigabe() {
        return bemAbrechnungsFreigabe;
    }

    /**
     * @return the bemMcAnfrage
     */
    public String getBemMcAnfrage() {
        return bemMcAnfrage;
    }

    /**
     * @return the bewegungsBeatmungsDauer
     */
    public int getBewegungsBeatmungsDauer() {
        return bewegungsBeatmungsDauer;
    }

    /**
     * @return the beatmungszeiten
     */
    public List<beatmungszeitType> getBeatmungszeiten() {
        return beatmungszeiten == null ? null : new ArrayList<>(beatmungszeiten);
    }

    /**
     * @param beatmungsdauer the beatmungsdauer to set
     */
    public void setBeatmungsdauer(int beatmungsdauer) {
        this.beatmungsdauer = beatmungsdauer;
    }

    /**
     * @param geburtsgewicht the geburtsgewicht to set
     */
    public void setGeburtsgewicht(int geburtsgewicht) {
        this.geburtsgewicht = geburtsgewicht;
    }

    /**
     * @param fallFreigabe the fallFreigabe to set
     */
    public void setFallFreigabe(boolean fallFreigabe) {
        this.fallFreigabe = fallFreigabe;
    }

    /**
     * @param abrechnungsFreigabe the abrechnungsFreigabe to set
     */
    public void setAbrechnungsFreigabe(boolean abrechnungsFreigabe) {
        this.abrechnungsFreigabe = abrechnungsFreigabe;
    }

    /**
     * @param mcAnfrage the mcAnfrage to set
     */
    public void setMcAnfrage(boolean mcAnfrage) {
        this.mcAnfrage = mcAnfrage;
    }

    /**
     * @param bemFallFreigabe the bemFallFreigabe to set
     */
    public void setBemFallFreigabe(String bemFallFreigabe) {
        this.bemFallFreigabe = bemFallFreigabe;
    }

    /**
     * @param bemAbrechnungsFreigabe the bemAbrechnungsFreigabe to set
     */
    public void setBemAbrechnungsFreigabe(String bemAbrechnungsFreigabe) {
        this.bemAbrechnungsFreigabe = bemAbrechnungsFreigabe;
    }

    /**
     * @param bemMcAnfrage the bemMcAnfrage to set
     */
    public void setBemMcAnfrage(String bemMcAnfrage) {
        this.bemMcAnfrage = bemMcAnfrage;
    }

    /**
     * @param bewegungsBeatmungsDauer the bewegungsBeatmungsDauer to set
     */
    public void setBewegungsBeatmungsDauer(int bewegungsBeatmungsDauer) {
        this.bewegungsBeatmungsDauer = bewegungsBeatmungsDauer;
    }

    /**
     * @param beatmungszeiten the beatmungszeiten to set
     */
    public void setBeatmungszeiten(List<beatmungszeitType> beatmungszeiten) {
        this.beatmungszeiten = beatmungszeiten == null ? null : new ArrayList<>(beatmungszeiten);
    }

    public void setBeatmungszeit(beatmungszeitType beatmungszeit) {
        this.beatmungszeiten.add(beatmungszeit);
    }
}
