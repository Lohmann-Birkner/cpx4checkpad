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
//package de.lb.cpx.sap.results;
//
//import com.sap.conn.jco.JCoTable;
//import java.util.Date;
//import java.util.logging.Logger;
//
///**
// *
// * @author niemeier
// */
//public class SapNleiSearchResult {
//
//    public static final Logger LOG = Logger.getLogger(SapNleiSearchResult.class.getName());
//
//    public String EINRI; //  Einrichtung
//    public String FALNR; // Fallnummer
//    public String LNRLS; // Laufende Nummer der Leistung
//    public String HAUST; // Identifikation eines Leistungskataloges
//    public String LEIST; // Leistung innerhalb eines Leistungskataloges
//    public String ANFOE; // Fachliche Org.einheit, die die Leistung anfordert
//    public String ANPOE; // Pflegerische Org.einheit, die die Leistung anfordert
//    public String ERBOE; // ID der Organisationeinheit, die die Leistung erbringt
//    public String ABRKZ; // Kennzeichen, daß Leistung abgerechnet werden kann
//    public String HCOKZ; // Kennzeichen Controllingrelevante Leistung
//    public long IMENG; // Leistungsmenge
//    public String ZEITR; // Kennzeichen zeitraumbezogene Leistung
//    public String BFORM; // Bewertungsformel fu?r zeitraumabh. Leistungen
//    public Date IBGDT; // Beginndatum der Leistungserbringung (IST)
//    public Date IENDT; //Endedatum der Leistungserbringung (IST)
//    public Date IBZT; // Beginnuhrzeit der Leistungserbringung (IST)
//    public Date IEZT; // Endeuhrzeit der Leistungserbringung (IST)
//    public String ENTGA; // Entgeltart einer Leistung
//    public String ENTKY; // Entgeltschlu?ssel fu?r §301/DALE-UV
//    public String ENZKY; // Zusatzschlu?ssel zur Entgeltart fu?r Fallp.,Sonderentg.
//    public String ENTG2; // Entgeltart nach §301
//    public String EAUFN; //Kostenträger (Auftrag)
//    public String REFKY; // Referenzschlu?ssel Leistung aus externem System
//    public String BFORMTXT; //K_u_rz_t_e_xt_ _z_u _F_e_st_w_e_rt_e_n___________________________________________       
//
//    /**
//     *
//     */
//    public SapNleiSearchResult() {
//        super();
//    }
//
//    /**
//     *
//     * @param aTable
//     */
//    public void readFromTable(final JCoTable aTable) {
//        String leistungsmenge = aTable.getString(10).trim();
//
//        EINRI = aTable.getString(0); //  Einrichtung
//        FALNR = aTable.getString(1); // Fallnummer
//        LNRLS = aTable.getString(2); // Laufende Nummer der Leistung
//        HAUST = aTable.getString(3); // Identifikation eines Leistungskataloges
//        LEIST = aTable.getString(4); // Leistung innerhalb eines Leistungskataloges
//        ANFOE = aTable.getString(5); // Fachliche Org.einheit, die die Leistung anfordert
//        ANPOE = aTable.getString(6); // Pflegerische Org.einheit, die die Leistung anfordert
//        ERBOE = aTable.getString(7); // ID der Organisationeinheit, die die Leistung erbringt
//        ABRKZ = aTable.getString(8); // Kennzeichen, daß Leistung abgerechnet werden kann
//        HCOKZ = aTable.getString(9); // Kennzeichen Controllingrelevante Leistung
//        IMENG = leistungsmenge.isEmpty() ? 0 : Integer.parseInt(leistungsmenge); // Leistungsmenge
//        ZEITR = aTable.getString(11); // Kennzeichen zeitraumbezogene Leistung
//        BFORM = aTable.getString(12); // Bewertungsformel fu?r zeitraumabh. Leistungen
//        IBGDT = aTable.getDate(13); // Beginndatum der Leistungserbringung (IST)
//        IENDT = aTable.getDate(14); //Endedatum der Leistungserbringung (IST)
//        IBZT = aTable.getDate(15); // Beginnuhrzeit der Leistungserbringung (IST)
//        IEZT = aTable.getDate(16); // Endeuhrzeit der Leistungserbringung (IST)
//        ENTGA = aTable.getString(17); // Entgeltart einer Leistung
//        ENTKY = aTable.getString(18); // Entgeltschlu?ssel fu?r §301/DALE-UV
//        ENZKY = aTable.getString(19); // Zusatzschlu?ssel zur Entgeltart fu?r Fallp.,Sonderentg.
//        ENTG2 = aTable.getString(20); // Entgeltart nach §301
//        EAUFN = aTable.getString(21); //Kostenträger (Auftrag)
//        REFKY = aTable.getString(22); // Referenzschlu?ssel Leistung aus externem System
//        BFORMTXT = aTable.getString(23); //K_u_rz_t_e_xt_ _z_u _F_e_st_w_e_rt_e_n                
//    }
//}
