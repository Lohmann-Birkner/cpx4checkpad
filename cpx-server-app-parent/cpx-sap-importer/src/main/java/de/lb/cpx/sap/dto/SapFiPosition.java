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

/**
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
public class SapFiPosition {

    private String vbeln; //Faktura
    private String posnr; //Fakturaposition
    private String arktx; //Kurztext der Kundenauftragsposition
    private String ishablst; //IS-H: Service Code Within a Service Catalog (Billing)
    private double fkimg; //Tatsächlich fakturierte Menge
    private double netwr; //Nettowert der Fakturaposition in Belegwährung 
    private double ishgprs; //Grundpreis der Leistung

    /**
     *
     */
    public SapFiPosition() {
        //super(TABLE_NAME, true);
    }

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
     * @return the posnr
     */
    public String getPosnr() {
        return posnr;
    }

    /**
     * @param posnr the posnr to set
     */
    public void setPosnr(String posnr) {
        this.posnr = posnr;
    }

    /**
     * @return the arktx
     */
    public String getArktx() {
        return arktx;
    }

    /**
     * @param arktx the arktx to set
     */
    public void setArktx(String arktx) {
        this.arktx = arktx;
    }

    /**
     * @return the ishablst
     */
    public String getIshablst() {
        return ishablst;
    }

    /**
     * @param ishablst the ishablst to set
     */
    public void setIshablst(String ishablst) {
        this.ishablst = ishablst;
    }

    /**
     * @return the fkimg
     */
    public double getFkimg() {
        return fkimg;
    }

    /**
     * @param fkimg the fkimg to set
     */
    public void setFkimg(double fkimg) {
        this.fkimg = fkimg;
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
     * @return the ishgprs
     */
    public double getIshgprs() {
        return ishgprs;
    }

    /**
     * @param ishgprs the ishgprs to set
     */
    public void setIshgprs(double ishgprs) {
        this.ishgprs = ishgprs;
    }

    @Override
    public String toString() {
        return "Faktura >" + getVbeln() + "<\n"
                + "POSNR >" + getPosnr() + "<\n"
                + "ARKTX >" + getArktx() + "<\n"
                + "ISHABLST >" + getIshablst() + "<\n"
                + "FKIMG >" + getFkimg() + "<\n"
                + "NETWR >" + getNetwr() + "<\n"
                + "ISHGPRS >" + getIshgprs() + "<\n";
    }
}
