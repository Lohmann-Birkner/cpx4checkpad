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
package de.lb.cpx.sap.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author niemeier
 */
public class SapFiFactura {

    private final List<SapFiPosition> mResults = new ArrayList<>();

    private String vbeln;
    private String fktyp;
    private String vbtyp;
    private String waerk;
    private java.sql.Date fkdat;
    private int gjahr;
    private String fkart;
    private String rfbsk;
    private double netwr;
    private String kunrg;
    private String fksto;

    /**
     *
     */
    public SapFiFactura() {
        //super(TABLE_NAME, true);
    }

//    /**
//     * @return the m_results
//     */
//    public List<SapFiPosition> getM_results() {
//        return m_results;
//    }
//
//    /**
//     * @param m_results the m_results to set
//     */
//    public void setM_results(List<SapFiPosition> m_results) {
//        this.m_results = m_results;
//    }
    /**
     * @return the vbeln
     */
    public String getVbeln() {
        return vbeln;
    }

    /**
     * @param vbeln the vbeln to set
     */
    public void setVbeln(String vbeln) {
        this.vbeln = vbeln;
    }

    /**
     * @return the fktyp
     */
    public String getFktyp() {
        return fktyp;
    }

    /**
     * @param fktyp the fktyp to set
     */
    public void setFktyp(String fktyp) {
        this.fktyp = fktyp;
    }

    /**
     * @return the vbtyp
     */
    public String getVbtyp() {
        return vbtyp;
    }

    /**
     * @param vbtyp the vbtyp to set
     */
    public void setVbtyp(String vbtyp) {
        this.vbtyp = vbtyp;
    }

    /**
     * @return the waerk
     */
    public String getWaerk() {
        return waerk;
    }

    /**
     * @param waerk the waerk to set
     */
    public void setWaerk(String waerk) {
        this.waerk = waerk;
    }

    /**
     * @return the fkdat
     */
    public java.sql.Date getFkdat() {
        return fkdat == null ? null : new java.sql.Date(fkdat.getTime());
    }

    /**
     * @param fkdat the fkdat to set
     */
    public void setFkdat(java.sql.Date fkdat) {
        this.fkdat = fkdat == null ? null : new java.sql.Date(fkdat.getTime());
    }

    /**
     * @return the gjahr
     */
    public int getGjahr() {
        return gjahr;
    }

    /**
     * @param gjahr the gjahr to set
     */
    public void setGjahr(int gjahr) {
        this.gjahr = gjahr;
    }

    /**
     * @return the fkart
     */
    public String getFkart() {
        return fkart;
    }

    /**
     * @param fkart the fkart to set
     */
    public void setFkart(String fkart) {
        this.fkart = fkart;
    }

    /**
     * @return the rfbsk
     */
    public String getRfbsk() {
        return rfbsk;
    }

    /**
     * @param rfbsk the rfbsk to set
     */
    public void setRfbsk(String rfbsk) {
        this.rfbsk = rfbsk;
    }

    /**
     * @return the netwr
     */
    public double getNetwr() {
        return netwr;
    }

    /**
     * @param netwr the netwr to set
     */
    public void setNetwr(double netwr) {
        this.netwr = netwr;
    }

    /**
     * @return the kunrg
     */
    public String getKunrg() {
        return kunrg;
    }

    /**
     * @param kunrg the kunrg to set
     */
    public void setKunrg(String kunrg) {
        this.kunrg = kunrg;
    }

    /**
     * @return the fksto
     */
    public String getFksto() {
        return fksto;
    }

    /**
     * @param fksto the fksto to set
     */
    public void setFksto(String fksto) {
        this.fksto = fksto;
    }

    /**
     *
     * @param obj SAP FI position
     */
    public void addReferenceObject(SapFiPosition obj) {
//    try {
        if (obj == null) {
            return;
        }
        if (!mResults.contains(obj)) {
            mResults.add(obj);
        }
    }

    /**
     *
     * @return list of SAP FI positions
     */
    public List<SapFiPosition> getPositions() {
        return new ArrayList<>(mResults);
    }

    @Override
    public String toString() {
        return "Faktura >" + getVbeln() + "<\n"
                + "Fakturatyp (X-Aktive Rechnung) >" + getFktyp() + "<\n"
                + "Fakturabeleg (M-Rechnung, N-Storno) >" + getVbtyp() + "<\n"
                + "Währung (EUR) >" + getWaerk() + "<\n"
                + "Fakturadatum >" + getFkdat() + "<\n"
                + "Geschäftsjahr >" + getGjahr() + "<\n"
                + "Faktura-Art >" + getFkart() + "<\n"
                + "Buchungstatus (C-Buchungsbeleg erzeugt) >" + getRfbsk() + "<\n"
                + "Nettowert in Belegwährung >" + getNetwr() + "<\n"
                + "Regulierer >" + getKunrg() + "<\n"
                + "Faktura ist storniert >" + getFksto() + "<\n";
    }

    /**
     *
     * @param pos SAP FI position
     * @return SAP FI position
     */
    public SapFiPosition addPosition(final SapFiPosition pos) {
        this.addReferenceObject(pos);
        return pos;
    }

}
