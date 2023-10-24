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
package de.lb.cpx.sap.results;

import com.sap.conn.jco.JCoTable;
import de.lb.cpx.sap.kain_inka.KainInkaElement;
import de.lb.cpx.sap.kain_inka.PvtChildSegment;
import de.lb.cpx.sap.kain_inka.PvtResultIf;
import de.lb.cpx.sap.kain_inka.PvvChildSegment;
import de.lb.cpx.sap.kain_inka.PvvResultIf;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author urbach
 */
public class SapKainPvvSearchResult implements PvvResultIf {

    private String pvvrowid = "";
    private String pvvinfo = "";
    private String pvvrecno = "";
    private Date pvvrecdat = null;

    private final List<PvtResultIf> alPVTs = new ArrayList<>(0);
    //public ArrayList<SAPKAINPVTSearchResult> alPVTs = new ArrayList<>(0);

    /**
     *
     */
    public SapKainPvvSearchResult() {
    }

    /**
     * @return the pvvrowid
     */
    public String getPvvrowid() {
        return pvvrowid;
    }

    /**
     * @param pvvrowid the pvvrowid to set
     */
    public void setPvvrowid(String pvvrowid) {
        this.pvvrowid = pvvrowid;
    }

    /**
     * @return the pvvinfo
     */
    public String getPvvinfo() {
        return pvvinfo;
    }

    /**
     * @param pvvinfo the pvvinfo to set
     */
    public void setPvvinfo(String pvvinfo) {
        this.pvvinfo = pvvinfo;
    }

    /**
     * @return the pvvrecno
     */
    public String getPvvrecno() {
        return pvvrecno;
    }

    /**
     * @param pvvrecno the pvvrecno to set
     */
    public void setPvvrecno(String pvvrecno) {
        this.pvvrecno = pvvrecno;
    }

    /**
     * @return the pvvrecdat
     */
    public Date getPvvrecdat() {
        return pvvrecdat == null ? null : new Date(pvvrecdat.getTime());
    }

    /**
     * @param pvvrecdat the pvvrecdat to set
     */
    public void setPvvrecdat(Date pvvrecdat) {
        this.pvvrecdat = pvvrecdat == null ? null : new Date(pvvrecdat.getTime());
    }

    /**
     * @return the alPVTs
     */
    public List<PvtResultIf> getAlPVTs() {
        return new ArrayList<>(alPVTs);
    }
//
//    /**
//     * @param alPVTs the alPVTs to set
//     */
//    public void setAlPVTs(List<PvtResultIf> alPVTs) {
//        this.alPVTs = alPVTs;
//    }

    /**
     *
     * @param aTable JCO table
     */
    public void readFromTable(final JCoTable aTable) {
        setPvvrowid(aTable.getString("PVVROWID"));
        setPvvinfo(aTable.getString("PVVINFO"));
        setPvvrecno(aTable.getString("PVVRECNO"));
        setPvvrecdat(aTable.getDate("PVVRECDAT"));
    }

    @Override
    public String toString() {
        String result
                = "PVV-ID  >" + getPvvrowid() + "<\r\n"
                + "Information  >" + getPvvinfo() + "<\r\n"
                + "Rechnungsnummer (RECH)  >" + getPvvrecno() + "<\r\n"
                + "Rechnungsdatum (RECH)  >" + getPvvrecdat() + "<\r\n";
        for (PvtResultIf pvt : alPVTs) {
            result += pvt.toString();
        }
        return result;
    }

    /**
     *
     * @param newElement pvt search result
     */
    public void addPVTElement(final SapKainPvtSearchResult newElement) {
        if (newElement == null) {
            return;
        }
        alPVTs.add(newElement);
    }

    /**
     *
     * @param info information
     */
    @Override
    public void setInformation(final String info) {
        setPvvinfo(info);
    }

    /**
     *
     * @param nr bill number
     */
    @Override
    public void setBillNumber(String nr) {
        setPvvrecno(nr);
    }

    /**
     *
     * @param date bill date
     */
    @Override
    public void setBillDate(Date date) {
        setPvvrecdat(date == null ? null : new Date(date.getTime()));
    }

    /**
     *
     * @param pvt pvt
     */
    @Override
    public void addPVT(PvtResultIf pvt) {
        if (pvt == null) {
            return;
        }
        alPVTs.add(pvt);
    }

    /**
     *
     * @return information
     */
    @Override
    public String getInformation() {
        return getPvvinfo();
    }

    /**
     *
     * @return bill number
     */
    @Override
    public String getBillNumber() {
        return getPvvrecno();
    }

    /**
     *
     * @return bill date
     */
    @Override
    public Date getBillDate() {
        return getPvvrecdat() == null ? null : new Date(getPvvrecdat().getTime());
    }

    /**
     *
     * @return list of pvt results
     */
    @Override
    public List<PvtResultIf> getPVTResultList() {
        return new ArrayList<>(alPVTs);
    }

    protected void setData(final PvvChildSegment pvv) {
        setInformation(pvv.getInformation());
        setBillNumber(pvv.getBillNumber());
        setBillDate(pvv.getBillDate());
        List<KainInkaElement> pvts = pvv.getPVTs();
        //setAlPVTs((List<PvtResultIf>) new ArrayList<>(0));
        if (pvts != null) {
            for (KainInkaElement pvt : pvts) {
                if (pvt instanceof PvtChildSegment) {
                    SapKainPvtSearchResult spvt = new SapKainPvtSearchResult();
                    alPVTs.add(spvt);
                    spvt.setData((PvtResultIf) pvt);
                }
            }

        }
    }

}
