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
import de.lb.cpx.sap.kain_inka.PvtResultIf;

/**
 *
 * @author urbach
 */
public class SapKainPvtSearchResult implements PvtResultIf {

    private String pvvrowid = "";
    private String pvtrowid = "";
    private String pvttext = "";
    private String hdPrim = "";
    private String hdSec = "";
    private String ndPrim = "";
    private String ndSec = "";
    private String pvtproc = "";

    /**
     *
     */
    public SapKainPvtSearchResult() {
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
     * @return the pvtrowid
     */
    public String getPvtrowid() {
        return pvtrowid;
    }

    /**
     * @param pvtrowid the pvtrowid to set
     */
    public void setPvtrowid(String pvtrowid) {
        this.pvtrowid = pvtrowid;
    }

    /**
     * @return the pvttext
     */
    public String getPvttext() {
        return pvttext;
    }

    /**
     * @param pvttext the pvttext to set
     */
    public void setPvttext(String pvttext) {
        this.pvttext = pvttext;
    }

    /**
     * @return the hdPrim
     */
    public String getHdPrim() {
        return hdPrim;
    }

    /**
     * @param hdPrim the hdPrim to set
     */
    public void setHdPrim(String hdPrim) {
        this.hdPrim = hdPrim;
    }

    /**
     * @return the hdSec
     */
    public String getHdSec() {
        return hdSec;
    }

    /**
     * @param hdSec the hdSec to set
     */
    public void setHdSec(String hdSec) {
        this.hdSec = hdSec;
    }

    /**
     * @return the ndPrim
     */
    public String getNdPrim() {
        return ndPrim;
    }

    /**
     * @param ndPrim the ndPrim to set
     */
    public void setNdPrim(String ndPrim) {
        this.ndPrim = ndPrim;
    }

    /**
     * @return the ndSec
     */
    public String getNdSec() {
        return ndSec;
    }

    /**
     * @param ndSec the ndSec to set
     */
    public void setNdSec(String ndSec) {
        this.ndSec = ndSec;
    }

    /**
     * @return the pvtproc
     */
    public String getPvtproc() {
        return pvtproc;
    }

    /**
     * @param pvtproc the pvtproc to set
     */
    public void setPvtproc(String pvtproc) {
        this.pvtproc = pvtproc;
    }

    /**
     *
     * @param aTable JCO table
     */
    public void readFromTable(final JCoTable aTable) {
        setPvvrowid(aTable.getString("PVVROWID"));
        setPvtrowid(aTable.getString("PVTROWID"));
        setPvttext(aTable.getString("PVTTEXT"));
        setHdPrim(aTable.getString("HD_PRIM"));
        setHdSec(aTable.getString("HD_SEC"));
        setNdPrim(aTable.getString("ND_PRIM"));
        setNdSec(aTable.getString("ND_SEC"));
        setPvtproc(aTable.getString("PVTPROC"));
    }

    /**
     *
     * @param aTable JCO table
     */
    public void readFromTableForExport(final JCoTable aTable) {
        setPvvrowid(aTable.getString("PVVROWID"));
        setPvtrowid(aTable.getString("PVTROWID"));
        setPvttext(aTable.getString("PVTTEXT"));
    }

    @Override
    public String toString() {
        return "PVV-ID  >" + getPvvrowid() + "<\r\n"
                + "PVT-ID  >" + getPvtrowid() + "<\r\n"
                + "PrüfvV-Text  >" + getPvttext() + "<\r\n"
                + "PrüfvV-Hauptdiagnose  >" + getHdPrim() + "<\r\n"
                + "HD-Sekundärdiagnose  >" + getHdSec() + "<\r\n"
                + "PrüfvV-Nebendiagnose  >" + getNdPrim() + "<\r\n"
                + "ND-Sekundärdiagnose  >" + getNdSec() + "<\r\n"
                + "PrüfvV-Prozedur  >" + getPvtproc() + "<\r\n";
    }

    /**
     *
     * @param text text
     */
    @Override
    public void setText(final String text) {
        setPvttext(text);
    }

    /**
     *
     * @param code code
     * @param loc localisation
     */
    @Override
    public void setPVTPrincipalDiag(final String code, final String loc) {
        setHdPrim(code);
    }

    /**
     *
     * @param code code
     * @param loc localisation
     */
    @Override
    public void setPVTPrincipalSecondaryDiag(final String code, final String loc) {
        setHdSec(code);
    }

    /**
     *
     * @param code code
     * @param loc localisation
     */
    @Override
    public void setPVTAuxDiag(final String code, final String loc) {
        setNdPrim(code);
    }

    /**
     *
     * @param code code
     * @param loc localisation
     */
    @Override
    public void setPVTSecondaryAuxDiag(final String code, final String loc) {
        setNdSec(code);
    }

    /**
     *
     * @param code code
     * @param loc localisation
     */
    @Override
    public void setPVTProcedure(final String code, final String loc) {
        setPvtproc(code);
    }

    /**
     *
     * @return text
     */
    @Override
    public String getText() {
        return getPvttext();
    }

    /**
     *
     * @return icd code
     */
    @Override
    public String getPVTPrincipalDiagCode() {
        return getHdPrim();
    }

    /**
     *
     * @return icd localisation
     */
    @Override
    public String getPVTPrincipalDiagLoc() {
        return "";
    }

    /**
     *
     * @return icd secondary code
     */
    @Override
    public String getPVTPrincipalDiagSecondaryCode() {
        return getHdSec();
    }

    /**
     *
     * @return icd secondary localisation
     */
    @Override
    public String getPVTPrincipalDiagSecondaryLoc() {
        return "";
    }

    /**
     *
     * @return auxiliary icd code
     */
    @Override
    public String getPVTAuxDiagCode() {
        return getNdPrim();
    }

    /**
     *
     * @return auxiliary icd localisation
     */
    @Override
    public String getPVTAuxDiagLoc() {
        return "";
    }

    /**
     *
     * @return auxiliary icd secondary code
     */
    @Override
    public String getPVTAuxDiagSecondaryCode() {
        return getNdSec();
    }

    /**
     *
     * @return auxiliary icd secondary localisation
     */
    @Override
    public String getPVTAuxDiagSecondaryLoc() {
        return "";
    }

    /**
     *
     * @return ops code
     */
    @Override
    public String getPVTProcedureCode() {
        return getPvtproc();
    }

    /**
     *
     * @return ops localisation
     */
    @Override
    public String getPVTProcedureLoc() {
        return "";
    }

    void setData(final PvtResultIf pvt) {
        this.setPVTAuxDiag(pvt.getPVTAuxDiagCode(), pvt.getPVTAuxDiagLoc());
        this.setText(pvt.getText());
        this.setPVTPrincipalDiag(pvt.getPVTPrincipalDiagCode(), pvt.getPVTPrincipalDiagLoc());
        this.setPVTPrincipalSecondaryDiag(pvt.getPVTPrincipalDiagSecondaryCode(), pvt.getPVTPrincipalDiagSecondaryLoc());
        this.setPVTSecondaryAuxDiag(pvt.getPVTAuxDiagSecondaryCode(), pvt.getPVTAuxDiagSecondaryLoc());
        this.setPVTProcedure(pvt.getPVTProcedureCode(), pvt.getPVTProcedureLoc());
    }

}
