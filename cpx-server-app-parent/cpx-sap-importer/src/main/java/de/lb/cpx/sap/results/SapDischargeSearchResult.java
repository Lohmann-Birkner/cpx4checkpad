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

import com.sap.conn.jco.JCoStructure;

/**
 *
 * @author niemeier
 */
public class SapDischargeSearchResult extends SapChargeSearchResult {

    /**
     *
     */
    public SapDischargeSearchResult() {
    }

    /**
     *
     * @param aTable JCO table
     */
    @Override
    public void readFromStructure(final JCoStructure aTable) {
        setClient(aTable.getString(0));
        setInstitution(aTable.getString(1));
        setInstStext(aTable.getString(2));
        setPatCaseId(aTable.getString(3));
        setChkDigitCase(aTable.getString(4));
        setCaseType(aTable.getString(5));
        setCaseTypeExt(aTable.getString(6));
        setCaseTypeStext(aTable.getString(7));
        setPatientId(aTable.getString(8));
        setBillingStatus(aTable.getString(9));
        setBillStext(aTable.getString(10));
        setRestricted(aTable.getString(11));
        setGeogrArea(aTable.getString(12));
        setGeogrAreaText(aTable.getString(13));
        setCaseComment(aTable.getString(14));
        setCaseStatus(aTable.getString(15));
        setCaseStatStext(aTable.getString(16));
        setEmergAdm(aTable.getString(17));
        setQuickAdm(aTable.getString(18));
        setStartDate(aTable.getString(19));
        setEndDate(aTable.getString(20));
        setBillBlock(aTable.getString(21));
        setBillBlockStext(aTable.getString(22));
        setPrevTreatDays(aTable.getString(23));
        setStatStcBlock(aTable.getString(24));
        setPpaRelev(aTable.getString(25));
        setRecOrder(aTable.getString(26));
        setPreviousDays(aTable.getString(27));
        setObjectNo(aTable.getString(28));
        setNonResident(aTable.getString(29));
        setChildren(aTable.getString(30));
        setForeignCase(aTable.getString(31));
        setCaseCategory(aTable.getString(32));
        setCaseCategoryStext(aTable.getString(33));
        setEmployeeType(aTable.getString(34));
        setEmployeeTypeStext(aTable.getString(35));
        setCantonTariff(aTable.getString(36));
        setCantonTariffStext(aTable.getString(37));
        setCantonConvtn(aTable.getString(38));
        setCantonConvtnStext(aTable.getString(39));
        setSrvGenTo(aTable.getString(40));
        setHealedDate(aTable.getString(41));
        setApplStatus(aTable.getString(42));
        setApplStatusStext(aTable.getString(43));
        setSpecialty(aTable.getString(44));
        setSpecialtyStext(aTable.getString(45));
        setCaseEndType(aTable.getString(46));
        setCaseEndTypeStext(aTable.getString(47));
        setDocType(aTable.getString(48));
        setDocTypeText(aTable.getString(49));
        setDocNo(aTable.getString(50));
        setChoiceCl(aTable.getString(51));
        setChoiceClText(aTable.getString(52));
        setPatWeight(aTable.getString(53));
        setWeightUnit(aTable.getString(54));
        setWeightUnitIso(aTable.getString(55));
        setPatHeight(aTable.getString(56));
        setHeightUnit(aTable.getString(57));
        setHeightUnitIso(aTable.getString(58));
        setMovemntSeqno(aTable.getString(59));
        setMovemntCtgry(aTable.getString(60));
        setMovemntCtgryText(aTable.getString(61));
        setMovemntType(aTable.getString(62));
        setMovemntTypeText(aTable.getString(63));
        setMovemntDate(aTable.getString(64));
        setMovemntTime(aTable.getString(65));
        setMovemntReas1(aTable.getString(66));
        setMovemntReas1Text(aTable.getString(67));
        setMovemntReas2(aTable.getString(68));
        setMovemntReas2Text(aTable.getString(69));
        setStatusInd(aTable.getString(70));
        setStatusIndText(aTable.getString(71));
        setRefPstTrtType(aTable.getString(72));
        setRefPstTrtTypeText(aTable.getString(73));
        setRefHospital(aTable.getString(74));
        setDischrgDisp(aTable.getString(75));
        setDischrgDispText(aTable.getString(76));
        setWorkIncapacity(aTable.getString(77));
        setCreationDate(aTable.getString(78));
        setCreationUser(aTable.getString(79));
        setUpdateDate(aTable.getString(80));
        setUpdateUser(aTable.getString(81));
        setCancelInd(aTable.getString(82));
        setCancelDate(aTable.getString(83));
        setCancelUser(aTable.getString(84));
        setCancelReason(aTable.getString(85));
        setCancelReasonText(aTable.getString(86));
        setCreationTime(aTable.getString(87));
        setRespiration(aTable.getString(88));
    }

    @Override
    public String toString() {
        return "Mandant >" + getClient() + "<\n" + "Einrichtung >" + getInstitution() + "<\n" + "xx >" + getInstStext() + "<\n" + "Kurzbezeichnung einer Einrichtung >" + getPatCaseId() + "<\n" + "Fallnummer >" + getChkDigitCase() + "<\n" + "Prüfziffer Fall >" + getCaseType() + "<\n" + "Fallart >" + getCaseTypeExt() + "<\n" + "Fallart - anwenderspezifisch >" + getCaseTypeStext() + "<\n" + "Patientennummer >" + getPatientId() + "<\n" + "Abrechnungskennzeichen eines Falles >" + getBillingStatus() + "<\n" + "Abrechnungskennzeichen eines Falles - Beschreibung >" + getBillStext() + "<\n" + "Kennzeichen für Sicherheitsverwahrung >" + getRestricted() + "<\n" + "Einzugsgebiet >" + getGeogrArea() + "<\n" + "Bezeichnung des Einzugsgebietes >" + getGeogrAreaText() + "<\n" + "Bemerkung zum Fall >" + getCaseComment() + "<\n" + "Fallstatus >" + getCaseStatus() + "<\n" + "Fallstatus - Beschreibung >" + getCaseStatStext() + "<\n" + "Notaufnahmekennzeichen >" + getEmergAdm() + "<\n" + "Kurzaufnahmekennzeichen >" + getQuickAdm() + "<\n" + "Beginndatum des Falles >" + getStartDate() + "<\n" + "Endedatum des Falles >" + getEndDate() + "<\n" + "Fakturasperre eines Falles >" + getBillBlock() + "<\n" + "Fakturasperre eines Falles - Beschreibung >" + getBillBlockStext() + "<\n" + "Anzahl Vorpflegetage des Patienten im Kalenderjahr >" + getPrevTreatDays() + "<\n" + "Kennzeichen für die Aufnahmeart Statistiksperre >" + getStatStcBlock() + "<\n" + "Kennzeichen, daß für die KV-Abrechnung relevant >" + getPpaRelev() + "<\n" + "CO: Empfänger (Auftrag) >" + getRecOrder() + "<\n" + "anzurechnende Vortage des Falles >" + getPreviousDays() + "<\n" + "Objektnummer >" + getObjectNo() + "<\n" + "Kennzeichen Patient kein Staatsbürger >" + getNonResident() + "<\n" + "Anzahl Kinder >" + getChildren() + "<\n" + "Auslandsfall >" + getForeignCase() + "<\n" + "Falltyp >" + getCaseCategory() + "<\n" + "Falltyp - Text >" + getCaseCategoryStext() + "<\n" + "Arbeitnehmertyp >" + getEmployeeType() + "<\n" + "Text zum Arbeitnehmertyp >" + getEmployeeTypeStext() + "<\n" + "CH: Kantonstarif >" + getCantonTariff() + "<\n" + "CH: Bezeichnung des Kantonstarifs >" + getCantonTariffStext() + "<\n" + "CH: Abkommen (Spital <-> Kanton) >" + getCantonConvtn() + "<\n" + "CH: Bezeichnung des Abkommens >" + getCantonConvtnStext() + "<\n" + "CH: Leistungsgenerierungsdatum bis >" + getSrvGenTo() + "<\n" + "Datum der Wundheilung >" + getHealedDate() + "<\n" + "Applikationsstatus >" + getApplStatus() + "<\n" + "Text des Applikationsstatus >" + getApplStatusStext() + "<\n" + "Fachrichtung für Organisationseinheit >" + getSpecialty() + "<\n" + "Bezeichnung der Fachrichtung >" + getSpecialtyStext() + "<\n" + "Art des Fallendes >" + getCaseEndType() + "<\n" + "Text zur Fallendeart >" + getCaseEndTypeStext() + "<\n" + "Typ des Ausweisdokuments >" + getDocType() + "<\n" + "Text für Ausweisdokumenttyp >" + getDocTypeText() + "<\n" + "Ausweisdokumentnummer (lang) >" + getDocNo() + "<\n" + "Wahl-Unterbringungskategorie >" + getChoiceCl() + "<\n" + "Text zur Wahl-Unterbringungskategorie >" + getChoiceClText() + "<\n" + "Gewicht des Patienten bei Aufnahme >" + getPatWeight() + "<\n" + "Maßeinheit Gewicht Patient >" + getWeightUnit() + "<\n" + "ISO-Maßeinheit Gewicht Patient >" + getWeightUnitIso() + "<\n" + "Größe des Patienten bei Aufnahme >" + getPatHeight() + "<\n" + "Maßeinheit Größe Patient >" + getHeightUnit() + "<\n" + "ISO-Maßeinheit Größe Patient >" + getHeightUnitIso() + "<\n" + "Laufende Nummer einer Bewegung >" + getMovemntSeqno() + "<\n" + "Bewegungstyp >" + getMovemntCtgry() + "<\n" + "Bewegungstypentext >" + getMovemntCtgryText() + "<\n" + "Bewegungsart >" + getMovemntType() + "<\n" + "Bewegungsartentext >" + getMovemntTypeText() + "<\n" + "Datum der Bewegung >" + getMovemntDate() + "<\n" + "Uhrzeit der Bewegung >" + getMovemntTime() + "<\n" + "Bewegungsgrund - 1. und 2. Stelle >" + getMovemntReas1() + "<\n" + "Bewegungsgrund - Text >" + getMovemntReas1Text() + "<\n" + "Bewegungsgrund - 3. und 4. Stelle >" + getMovemntReas2() + "<\n" + "Bewegungsgrund - Text >" + getMovemntReas2Text() + "<\n" + "Plankennzeichen Datum der Bewegung >" + getStatusInd() + "<\n" + "Plankennzeichen Datum der Bewegung - Beschreibung >" + getStatusIndText() + "<\n" + "Einweisungs-, Überweisungs-, Nachbehandlungsart >" + getRefPstTrtType() + "<\n" + "Text Einweisungs-, Überweisungs-, Nachbehandlungsart >" + getRefPstTrtTypeText() + "<\n" + "Identifikation eines externen Krankenhauses >" + getRefHospital() + "<\n" + "Entlassungszustand >" + getDischrgDisp() + "<\n" + "Entlassungszustandstext >" + getDischrgDispText() + "<\n" + "Arbeitsunfähigkeits-Bis-Datum >" + getWorkIncapacity() + "<\n" + "Datum, an dem der Satz hinzugefügt wurde >" + getCreationDate() + "<\n" + "Name des Sachbearbeiters, der den Satz hinzugefügt hat >" + getCreationUser() + "<\n" + "Änderungsdatum >" + getUpdateDate() + "<\n" + "Name des ändernden Sachbearbeiters >" + getUpdateUser() + "<\n" + "Stornokennzeichen >" + getCancelInd() + "<\n" + "Stornierungsdatum >" + getCancelDate() + "<\n" + "Name des stornierenden Sachbearbeiters >" + getCancelUser() + "<\n" + "Stornogrund der Bewegung >" + getCancelReason() + "<\n" + "Bezeichnung des Stornogrundes >" + getCancelReasonText() + "<\n" + "Uhrzeit, zu der der Satz hinzugefügt wurde >" + getCreationTime() + "<\n" + "Beatmungsdauer in Stunden >" + getRespiration() + "<";
    }

}
