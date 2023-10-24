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

import com.sap.conn.jco.JCoTable;

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
public class SapImExManager {

    /**
     *
     */
    public SapImExManager() {
    }

    /**
     *
     * @param aTable JCO table
     * @return SAP FI factura
     */
    public SapFiFactura readFacturaFromSAPTable(final JCoTable aTable) {
        SapFiFactura fact = new SapFiFactura();
        fact.setVbeln(aTable.getString("VBELN"));
        fact.setFktyp(aTable.getString("FKTYP"));
        fact.setVbtyp(aTable.getString("VBTYP"));
        fact.setWaerk(aTable.getString("WAERK"));
        fact.setFkdat(new java.sql.Date(aTable.getDate("FKDAT").getTime()));
        fact.setGjahr(aTable.getInt("GJAHR"));
        fact.setFkart(aTable.getString("FKART"));
        fact.setRfbsk(aTable.getString("RFBSK"));
        fact.setNetwr(aTable.getDouble("NETWR"));
        fact.setKunrg(aTable.getString("KUNRG"));
        fact.setFksto(aTable.getString("FKSTO"));
        return fact;
    }

    /**
     *
     * @param aTable JCO table
     * @return SAP FI position
     */
    public SapFiPosition readFIPositionFromSAPTable(JCoTable aTable) {
        SapFiPosition pos = new SapFiPosition();
        pos.setVbeln(aTable.getString("VBELN"));
        pos.setPosnr(aTable.getString("POSNR"));
        pos.setArktx(aTable.getString("ARKTX"));
        pos.setIshablst(aTable.getString("ISHABLST"));
        pos.setFkimg(aTable.getDouble("FKIMG"));
        pos.setNetwr(aTable.getDouble("NETWR"));
        pos.setIshgprs(aTable.getDouble("ISHGPRS"));
        return pos;
    }

    /**
     *
     * @param aTable JCO table
     * @return SAP open FI item
     */
    public SapFiOpenItem readFIOpenItemFromSAPTable(final JCoTable aTable) {
        SapFiOpenItem item = new SapFiOpenItem();
        item.setBukrs(aTable.getString("BUKRS"));
        item.setKunnr(aTable.getString("KUNNR"));
        item.setDzuonr(aTable.getString("ZUONR"));
        item.setGjahr(aTable.getInt("GJAHR"));
        item.setBelnr(aTable.getString("BELNR"));
        item.setBudat(aTable.getDate("BUDAT"));
        item.setBldat(aTable.getDate("BLDAT"));
        item.setCpudt(aTable.getDate("CPUDT"));
        item.setWaers(aTable.getString("WAERS"));
        item.setXblnr(aTable.getString("XBLNR"));
        item.setBlart(aTable.getString("BLART"));
        item.setBschl(aTable.getString("BSCHL"));
        item.setShkzg(aTable.getString("SHKZG"));
        item.setDmbtr(aTable.getDouble("DMBTR"));
        item.setSgtxt(aTable.getString("SGTXT"));
        return item;
    }

}
