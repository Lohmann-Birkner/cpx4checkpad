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
import com.sap.conn.jco.JCoTable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niemeier
 */
public class SapPatientDetailResult {

    private static final Logger LOG = Logger.getLogger(SapPatientDetailResult.class.getName());
    

    private String addrNo = "";
    private String country = "";
    private String countryIso = "";
    private String countryText = "";
    private String region = "";
    private String regionText = "";
    private String pcd = "";
    private String city = "";
    private String stdCity = "";
    private String district = "";
    private String stdDistrict = "";
    private String strNo = "";
    private String stdStreet = "";
    private String strSup = "";
    private String stdStrSup = "";
    private String geogrArea = "";
    private String geogrAreaText = "";
    private String poboxPcd = "";
    private String pobCountry = "";
    private String pobCountryIso = "";
    private String pobCountryTxt = "";
    private String pobboxCity = "";
    private String stdPoboxCity = "";
    private String companyPcd = "";
    private String phoneNo = "";
    private String extension = "";
    private String otherPhones = "";
    private String faxNo = "";
    private String faxExtension = "";
    private String telexNo = "";
    private String building = "";
    private String floor = "";
    private String unit = "";
    private String addrString = "";
    private String eMail = "";

    private String stasp = "";
    private String resid = "";
    private String forei = "";
    private String bekat = "";
    private String caseState = "";
    private String kisState = "0";
    private int tob = 0;
    private int mdTOB = 0;

    /**
     *
     */
    public SapPatientDetailResult() {
    }

    /**
     * @return the addrNo
     */
    public String getAddrNo() {
        return addrNo;
    }

    /**
     * @param addrNo the addrNo to set
     */
    public void setAddrNo(String addrNo) {
        this.addrNo = addrNo;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the countryIso
     */
    public String getCountryIso() {
        return countryIso;
    }

    /**
     * @param countryIso the countryIso to set
     */
    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

    /**
     * @return the countryText
     */
    public String getCountryText() {
        return countryText;
    }

    /**
     * @param countryText the countryText to set
     */
    public void setCountryText(String countryText) {
        this.countryText = countryText;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the regionText
     */
    public String getRegionText() {
        return regionText;
    }

    /**
     * @param regionText the regionText to set
     */
    public void setRegionText(String regionText) {
        this.regionText = regionText;
    }

    /**
     * @return the pcd
     */
    public String getPcd() {
        return pcd;
    }

    /**
     * @param pcd the pcd to set
     */
    public void setPcd(String pcd) {
        this.pcd = pcd;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the stdCity
     */
    public String getStdCity() {
        return stdCity;
    }

    /**
     * @param stdCity the stdCity to set
     */
    public void setStdCity(String stdCity) {
        this.stdCity = stdCity;
    }

    /**
     * @return the district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * @param district the district to set
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * @return the stdDistrict
     */
    public String getStdDistrict() {
        return stdDistrict;
    }

    /**
     * @param stdDistrict the stdDistrict to set
     */
    public void setStdDistrict(String stdDistrict) {
        this.stdDistrict = stdDistrict;
    }

    /**
     * @return the strNo
     */
    public String getStrNo() {
        return strNo;
    }

    /**
     * @param strNo the strNo to set
     */
    public void setStrNo(String strNo) {
        this.strNo = strNo;
    }

    /**
     * @return the stdStreet
     */
    public String getStdStreet() {
        return stdStreet;
    }

    /**
     * @param stdStreet the stdStreet to set
     */
    public void setStdStreet(String stdStreet) {
        this.stdStreet = stdStreet;
    }

    /**
     * @return the strSup
     */
    public String getStrSup() {
        return strSup;
    }

    /**
     * @param strSup the strSup to set
     */
    public void setStrSup(String strSup) {
        this.strSup = strSup;
    }

    /**
     * @return the stdStrSup
     */
    public String getStdStrSup() {
        return stdStrSup;
    }

    /**
     * @param stdStrSup the stdStrSup to set
     */
    public void setStdStrSup(String stdStrSup) {
        this.stdStrSup = stdStrSup;
    }

    /**
     * @return the geogrArea
     */
    public String getGeogrArea() {
        return geogrArea;
    }

    /**
     * @param geogrArea the geogrArea to set
     */
    public void setGeogrArea(String geogrArea) {
        this.geogrArea = geogrArea;
    }

    /**
     * @return the geogrAreaText
     */
    public String getGeogrAreaText() {
        return geogrAreaText;
    }

    /**
     * @param geogrAreaText the geogrAreaText to set
     */
    public void setGeogrAreaText(String geogrAreaText) {
        this.geogrAreaText = geogrAreaText;
    }

    /**
     * @return the poboxPcd
     */
    public String getPoboxPcd() {
        return poboxPcd;
    }

    /**
     * @param poboxPcd the poboxPcd to set
     */
    public void setPoboxPcd(String poboxPcd) {
        this.poboxPcd = poboxPcd;
    }

    /**
     * @return the pobCountry
     */
    public String getPobCountry() {
        return pobCountry;
    }

    /**
     * @param pobCountry the pobCountry to set
     */
    public void setPobCountry(String pobCountry) {
        this.pobCountry = pobCountry;
    }

    /**
     * @return the pobCountryIso
     */
    public String getPobCountryIso() {
        return pobCountryIso;
    }

    /**
     * @param pobCountryIso the pobCountryIso to set
     */
    public void setPobCountryIso(String pobCountryIso) {
        this.pobCountryIso = pobCountryIso;
    }

    /**
     * @return the pobCountryTxt
     */
    public String getPobCountryTxt() {
        return pobCountryTxt;
    }

    /**
     * @param pobCountryTxt the pobCountryTxt to set
     */
    public void setPobCountryTxt(String pobCountryTxt) {
        this.pobCountryTxt = pobCountryTxt;
    }

    /**
     * @return the pobboxCity
     */
    public String getPobboxCity() {
        return pobboxCity;
    }

    /**
     * @param pobboxCity the pobboxCity to set
     */
    public void setPobboxCity(String pobboxCity) {
        this.pobboxCity = pobboxCity;
    }

    /**
     * @return the stdPoboxCity
     */
    public String getStdPoboxCity() {
        return stdPoboxCity;
    }

    /**
     * @param stdPoboxCity the stdPoboxCity to set
     */
    public void setStdPoboxCity(String stdPoboxCity) {
        this.stdPoboxCity = stdPoboxCity;
    }

    /**
     * @return the companyPcd
     */
    public String getCompanyPcd() {
        return companyPcd;
    }

    /**
     * @param companyPcd the companyPcd to set
     */
    public void setCompanyPcd(String companyPcd) {
        this.companyPcd = companyPcd;
    }

    /**
     * @return the phoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * @param phoneNo the phoneNo to set
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * @return the otherPhones
     */
    public String getOtherPhones() {
        return otherPhones;
    }

    /**
     * @param otherPhones the otherPhones to set
     */
    public void setOtherPhones(String otherPhones) {
        this.otherPhones = otherPhones;
    }

    /**
     * @return the faxNo
     */
    public String getFaxNo() {
        return faxNo;
    }

    /**
     * @param faxNo the faxNo to set
     */
    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }

    /**
     * @return the faxExtension
     */
    public String getFaxExtension() {
        return faxExtension;
    }

    /**
     * @param faxExtension the faxExtension to set
     */
    public void setFaxExtension(String faxExtension) {
        this.faxExtension = faxExtension;
    }

    /**
     * @return the telexNo
     */
    public String getTelexNo() {
        return telexNo;
    }

    /**
     * @param telexNo the telexNo to set
     */
    public void setTelexNo(String telexNo) {
        this.telexNo = telexNo;
    }

    /**
     * @return the building
     */
    public String getBuilding() {
        return building;
    }

    /**
     * @param building the building to set
     */
    public void setBuilding(String building) {
        this.building = building;
    }

    /**
     * @return the floor
     */
    public String getFloor() {
        return floor;
    }

    /**
     * @param floor the floor to set
     */
    public void setFloor(String floor) {
        this.floor = floor;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return the addrString
     */
    public String getAddrString() {
        return addrString;
    }

    /**
     * @param addrString the addrString to set
     */
    public void setAddrString(String addrString) {
        this.addrString = addrString;
    }

    /**
     * @return the eMail
     */
    public String geteMail() {
        return eMail;
    }

    /**
     * @param eMail the eMail to set
     */
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    /**
     * @return the stasp
     */
    public String getStasp() {
        return stasp;
    }

    /**
     * @param stasp the stasp to set
     */
    public void setStasp(String stasp) {
        this.stasp = stasp;
    }

    /**
     * @return the resid
     */
    public String getResid() {
        return resid;
    }

    /**
     * @param resid the resid to set
     */
    public void setResid(String resid) {
        this.resid = resid;
    }

    /**
     * @return the forei
     */
    public String getForei() {
        return forei;
    }

    /**
     * @param forei the forei to set
     */
    public void setForei(String forei) {
        this.forei = forei;
    }

    /**
     * @return the bekat
     */
    public String getBekat() {
        return bekat;
    }

    /**
     * @param bekat the bekat to set
     */
    public void setBekat(String bekat) {
        this.bekat = bekat;
    }

    /**
     * @return the caseState
     */
    public String getCaseState() {
        return caseState;
    }

    /**
     * @param caseState the caseState to set
     */
    public void setCaseState(String caseState) {
        this.caseState = caseState;
    }

    /**
     * @return the kisState
     */
    public String getKisState() {
        return kisState;
    }

    /**
     * @param kisState the kisState to set
     */
    public void setKisState(String kisState) {
        this.kisState = kisState;
    }

    /**
     * @return the tob
     */
    public int getTob() {
        return tob;
    }

    /**
     * @param tob the tob to set
     */
    public void setTob(int tob) {
        this.tob = tob;
    }

    /**
     * @return the mdTOB
     */
    public int getMDTob() {
        return mdTOB;
    }

    /**
     * @param mdtob the mdtob to set
     */
    public void setMDTob(int mdtob) {
    //LUr 07.01.2021 Tage ohne Berechnung vom MD müssen mit negativem Vorzeichen übernommen werden.
        this.mdTOB = -mdtob;
    }

    /**
     *
     * @param aTable JCO table
     */
    public void readFromTable(final JCoTable aTable) {
        setAddrNo(aTable.getString(0));
        setCountry(aTable.getString(1));
        setCountryIso(aTable.getString(2));
        setCountryText(aTable.getString(3));
        setRegion(aTable.getString(4));
        setRegionText(aTable.getString(5));
        setPcd(aTable.getString(6));
        setCity(aTable.getString(7));
        setStdCity(aTable.getString(8));
        setDistrict(aTable.getString(9));
        setStdDistrict(aTable.getString(10));
        setStrNo(aTable.getString(11));
        setStdStreet(aTable.getString(12));
        setStrSup(aTable.getString(13));
        setStdStrSup(aTable.getString(14));
        setGeogrArea(aTable.getString(15));
        setGeogrAreaText(aTable.getString(16));
        setPoboxPcd(aTable.getString(17));
        setPobCountry(aTable.getString(18));
        setPobCountryIso(aTable.getString(19));
        setPobCountryTxt(aTable.getString(20));
        setPobboxCity(aTable.getString(21));
        setStdPoboxCity(aTable.getString(22));
        setCompanyPcd(aTable.getString(23));
        setPhoneNo(aTable.getString(24));
        setExtension(aTable.getString(25));
        setOtherPhones(aTable.getString(26));
        setFaxNo(aTable.getString(27));
        setFaxExtension(aTable.getString(28));
        setTelexNo(aTable.getString(29));
        setBuilding(aTable.getString(30));
        setFloor(aTable.getString(31));
        setUnit(aTable.getString(32));
        setAddrString(aTable.getString(33));
        seteMail(aTable.getString(34));
    }

    /**
     *
     * @param struct JCO structure
     */
    public void readFromStructure(final JCoStructure struct) {
        setResid(struct.getString("RESID"));
        setForei(struct.getString("FOREI"));
        setStasp(struct.getString("STASP"));
        setBekat(struct.getString("BEKAT"));
        setTob(struct.getInt("TOB"));
        String test = "";
        try{
            test = struct.getString("MDTOB");
            setMDTob(Integer.parseInt(test));
        }catch(Exception ex){
            LOG.log(Level.SEVERE, "cannot get MDTOB field from " + test, ex);
        }
    }

    @Override
    public String toString() {
        return "Adressnummer  > " + getAddrNo() + "\n"
                + "Land  > " + getCountry() + "\n"
                + "ISO-Code des Landes  > " + getCountryIso() + "\n"
                + "Bezeichnung des Landes  > " + getCountryText() + "\n"
                + "Region (Bundesland)  > " + getRegion() + "\n"
                + "Bezeichnung  > " + getRegionText() + "\n"
                + "Postleitzahl des Ortes  > " + getPcd() + "\n"
                + "Ort  > " + getCity() + "\n"
                + "Standardisierter Ortsname  > " + getStdCity() + "\n"
                + "Ortsteil  > " + getDistrict() + "\n"
                + "Standardisierter Ortsteil  > " + getStdDistrict() + "\n"
                + "Strasse  > " + getStrNo() + "\n"
                + "Standardisierte Strasse  > " + getStdStreet() + "\n"
                + "Zusatz der Strasse  > " + getStrSup() + "\n"
                + "Standardisierter Strassenzusatz  > " + getStdStrSup() + "\n"
                + "Einzugsgebiet  > " + getGeogrArea() + "\n"
                + "Bezeichnung des Einzugsgebietes  > " + getGeogrAreaText() + "\n"
                + "Postleitzahl des Postfaches  > " + getPoboxPcd() + "\n"
                + "Land zum Postfach  > " + getPobCountry() + "\n"
                + "ISO-Codes des Landes (Postfach)  > " + getPobCountryIso() + "\n"
                + "Bezeichnung des Landes (Postfach)  > " + getPobCountryTxt() + "\n"
                + "Ort des Postfaches  > " + getPobboxCity() + "\n"
                + "Standardisierter Orstname des Postfaches  > " + getStdPoboxCity() + "\n"
                + "Postleitzahl der Firma  > " + getCompanyPcd() + "\n"
                + "Telefon  > " + getPhoneNo() + "\n"
                + "Telefon Durchwahl  > " + getExtension() + "\n"
                + "Kennzeichen, dass es weitere Telefonnummern gibt  > " + getOtherPhones() + "\n"
                + "Telefax  > " + getFaxNo() + "\n"
                + "Telefax-Durchwahl  > " + getFaxExtension() + "\n"
                + "Telex-Nummer  > " + getTelexNo() + "\n"
                + "Gebäude  > " + getBuilding() + "\n"
                + "Stockwerk im Gebäude  > " + getFloor() + "\n"
                + "Wohnungsnummer  > " + getUnit() + "\n"
                + "Adresse  > " + getAddrString() + "\n"
                + "Internet-Mail  > " + geteMail() + "\n"
                + "staatbürgerkennzeichen > " + getStasp() + "\n"
                + "kein Staatsbürger > " + getResid() + "\n"
                + "Ausländerkennzeichen > " + getForei() + "\n";
    }

}
