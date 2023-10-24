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

import java.util.Date;

public class SapFiOpenItem {

    private String bukrs; //Company Code 
    private String kunnr; //Customer No
    private String dzuonr; //Assignment Number
    private int gjahr; //Fiscal Year
    private String belnr; //Accounting Document Number
    private Date budat; //Posting Date in the Document
    private Date bldat; //Document Date in Document
    private Date cpudt; //Day On Which Accounting Document Was Entered
    private String waers; //Currency Key
    private String xblnr; //Reference Document Number
    private String blart; //Document Type
    private String bschl; //Posting Key
    private String shkzg; //Debit/Credit Indicator
    private double dmbtr; //Amount in Local Currency
    private double netwr; //Net Value
    private String sgtxt; //Item Text

    /**
     *
     */
    public SapFiOpenItem() {
        //super(TABLE_NAME, true);
    }

    /**
     * @return the bukrs
     */
    public String getBukrs() {
        return bukrs;
    }

    /**
     * @param bukrs the bukrs to set
     */
    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }

    /**
     * @return the kunnr
     */
    public String getKunnr() {
        return kunnr;
    }

    /**
     * @param kunnr the kunnr to set
     */
    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    /**
     * @return the dzuonr
     */
    public String getDzuonr() {
        return dzuonr;
    }

    /**
     * @param dzuonr the dzuonr to set
     */
    public void setDzuonr(String dzuonr) {
        this.dzuonr = dzuonr;
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
     * @return the belnr
     */
    public String getBelnr() {
        return belnr;
    }

    /**
     * @param belnr the belnr to set
     */
    public void setBelnr(String belnr) {
        this.belnr = belnr;
    }

    /**
     * @return the budat
     */
    public Date getBudat() {
        return budat == null ? null : new Date(budat.getTime());
    }

    /**
     * @param budat the budat to set
     */
    public void setBudat(Date budat) {
        this.budat = budat == null ? null : new Date(budat.getTime());
    }

    /**
     * @return the bldat
     */
    public Date getBldat() {
        return bldat == null ? null : new Date(bldat.getTime());
    }

    /**
     * @param bldat the bldat to set
     */
    public void setBldat(Date bldat) {
        this.bldat = bldat == null ? null : new Date(bldat.getTime());
    }

    /**
     * @return the cpudt
     */
    public Date getCpudt() {
        return cpudt == null ? null : new Date(cpudt.getTime());
    }

    /**
     * @param cpudt the cpudt to set
     */
    public void setCpudt(Date cpudt) {
        this.cpudt = cpudt == null ? null : new Date(cpudt.getTime());
    }

    /**
     * @return the waers
     */
    public String getWaers() {
        return waers;
    }

    /**
     * @param waers the waers to set
     */
    public void setWaers(String waers) {
        this.waers = waers;
    }

    /**
     * @return the xblnr
     */
    public String getXblnr() {
        return xblnr;
    }

    /**
     * @param xblnr the xblnr to set
     */
    public void setXblnr(String xblnr) {
        this.xblnr = xblnr;
    }

    /**
     * @return the blart
     */
    public String getBlart() {
        return blart;
    }

    /**
     * @param blart the blart to set
     */
    public void setBlart(String blart) {
        this.blart = blart;
    }

    /**
     * @return the bschl
     */
    public String getBschl() {
        return bschl;
    }

    /**
     * @param bschl the bschl to set
     */
    public void setBschl(String bschl) {
        this.bschl = bschl;
    }

    /**
     * @return the shkzg
     */
    public String getShkzg() {
        return shkzg;
    }

    /**
     * @param shkzg the shkzg to set
     */
    public void setShkzg(String shkzg) {
        this.shkzg = shkzg;
    }

    /**
     * @return the dmbtr
     */
    public double getDmbtr() {
        return dmbtr;
    }

    /**
     * @param dmbtr the dmbtr to set
     */
    public void setDmbtr(double dmbtr) {
        this.dmbtr = dmbtr;
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
     * @return the sgtxt
     */
    public String getSgtxt() {
        return sgtxt;
    }

    /**
     * @param sgtxt the sgtxt to set
     */
    public void setSgtxt(String sgtxt) {
        this.sgtxt = sgtxt;
    }

    @Override
    public String toString() {
        return "BUKRS >" + getBukrs() + "<\n"
                + "KUNNR >" + getKunnr() + "<\n"
                + "DZUONR >" + getDzuonr() + "<\n"
                + "GJAHR >" + getGjahr() + "<\n"
                + "BELNR >" + getBelnr() + "<\n"
                + "BUDAT >" + getBudat() + "<\n"
                + "BLDAT >" + getBldat() + "<\n"
                + "CPUDT >" + getCpudt() + "<\n"
                + "WAERS >" + getWaers() + "<\n"
                + "XBLNR >" + getXblnr() + "<\n"
                + "BLART >" + getBlart() + "<\n"
                + "BSCHL >" + getBschl() + "<\n"
                + "SHKZG >" + getShkzg() + "<\n"
                + "DMBTR >" + getDmbtr() + "<\n"
                + "SGTXT >" + getSgtxt() + "<\n";
    }
}
