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
import java.util.Date;

/**
 *
 * @author niemeier
 */
public class SapPatientSearchResult {

    private String client = "";
    private String patientId = "";
    private String chkDigitPat = "";
    private String extPatId = "";
    private String institution = "";
    private String instStext = "";
    private String cancelInd = "";
    private String lastNamePat = "";
    private String stdLnamePat = "";
    private String frstNamePat = "";
    private String stdFnamePat = "";
    private String prefix = "";
    private String affix = "";
    private String title = "";
    private String pseudo = "";
    private String fullNamePat = "";
    private String birthName = "";
    private String stdBnamePat = "";
    private Date dob = null;
    private String sex = "";
    private String sexExt = "";
    private String sexStext = "";
    private String ssn = "";
    private String docType = "";
    private String docTypeText = "";
    private String docNo = "";
    private String nonResident = "";
    private String expired = "";
    private String cOfDeath = "";
    private String cOfDeathTxt = "";
    private String quickAdm = "";
    private String emergAdm = "";
    private String country = "";
    private String countryIso = "";
    private String countryText = "";
    private String pcd = "";
    private String city = "";
    private String district = "";
    private String strNo = "";
    private String phoneNo = "";
    private String address = "";
    private String patRef = "";
    private String apptId = "";
    private String vip = "";
    private String inactive = "";

    private String kasse = "";

    public SapPatientSearchResult() {
    }

    /**
     * @return the client
     */
    public String getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(String client) {
        this.client = client;
    }

    /**
     * @return the patientId
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * @param patientId the patientId to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the chkDigitPat
     */
    public String getChkDigitPat() {
        return chkDigitPat;
    }

    /**
     * @param chkDigitPat the chkDigitPat to set
     */
    public void setChkDigitPat(String chkDigitPat) {
        this.chkDigitPat = chkDigitPat;
    }

    /**
     * @return the extPatId
     */
    public String getExtPatId() {
        return extPatId;
    }

    /**
     * @param extPatId the extPatId to set
     */
    public void setExtPatId(String extPatId) {
        this.extPatId = extPatId;
    }

    /**
     * @return the institution
     */
    public String getInstitution() {
        return institution;
    }

    /**
     * @param institution the institution to set
     */
    public void setInstitution(String institution) {
        this.institution = institution;
    }

    /**
     * @return the instStext
     */
    public String getInstStext() {
        return instStext;
    }

    /**
     * @param instStext the instStext to set
     */
    public void setInstStext(String instStext) {
        this.instStext = instStext;
    }

    /**
     * @return the cancelInd
     */
    public String getCancelInd() {
        return cancelInd;
    }

    /**
     * @param cancelInd the cancelInd to set
     */
    public void setCancelInd(String cancelInd) {
        this.cancelInd = cancelInd;
    }

    /**
     * @return the lastNamePat
     */
    public String getLastNamePat() {
        return lastNamePat;
    }

    /**
     * @param lastNamePat the lastNamePat to set
     */
    public void setLastNamePat(String lastNamePat) {
        this.lastNamePat = lastNamePat;
    }

    /**
     * @return the stdLnamePat
     */
    public String getStdLnamePat() {
        return stdLnamePat;
    }

    /**
     * @param stdLnamePat the stdLnamePat to set
     */
    public void setStdLnamePat(String stdLnamePat) {
        this.stdLnamePat = stdLnamePat;
    }

    /**
     * @return the frstNamePat
     */
    public String getFrstNamePat() {
        return frstNamePat;
    }

    /**
     * @param frstNamePat the frstNamePat to set
     */
    public void setFrstNamePat(String frstNamePat) {
        this.frstNamePat = frstNamePat;
    }

    /**
     * @return the stdFnamePat
     */
    public String getStdFnamePat() {
        return stdFnamePat;
    }

    /**
     * @param stdFnamePat the stdFnamePat to set
     */
    public void setStdFnamePat(String stdFnamePat) {
        this.stdFnamePat = stdFnamePat;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the affix
     */
    public String getAffix() {
        return affix;
    }

    /**
     * @param affix the affix to set
     */
    public void setAffix(String affix) {
        this.affix = affix;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * @param pseudo the pseudo to set
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * @return the fullNamePat
     */
    public String getFullNamePat() {
        return fullNamePat;
    }

    /**
     * @param fullNamePat the fullNamePat to set
     */
    public void setFullNamePat(String fullNamePat) {
        this.fullNamePat = fullNamePat;
    }

    /**
     * @return the birthName
     */
    public String getBirthName() {
        return birthName;
    }

    /**
     * @param birthName the birthName to set
     */
    public void setBirthName(String birthName) {
        this.birthName = birthName;
    }

    /**
     * @return the stdBnamePat
     */
    public String getStdBnamePat() {
        return stdBnamePat;
    }

    /**
     * @param stdBnamePat the stdBnamePat to set
     */
    public void setStdBnamePat(String stdBnamePat) {
        this.stdBnamePat = stdBnamePat;
    }

    /**
     * @return the dob
     */
    public Date getDob() {
        return dob == null ? null : new Date(dob.getTime());
    }

    /**
     * @param dob the dob to set
     */
    public void setDob(Date dob) {
        this.dob = dob == null ? null : new Date(dob.getTime());
    }

    /**
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return the sexExt
     */
    public String getSexExt() {
        return sexExt;
    }

    /**
     * @param sexExt the sexExt to set
     */
    public void setSexExt(String sexExt) {
        this.sexExt = sexExt;
    }

    /**
     * @return the sexStext
     */
    public String getSexStext() {
        return sexStext;
    }

    /**
     * @param sexStext the sexStext to set
     */
    public void setSexStext(String sexStext) {
        this.sexStext = sexStext;
    }

    /**
     * @return the ssn
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * @param ssn the ssn to set
     */
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    /**
     * @return the docType
     */
    public String getDocType() {
        return docType;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

    /**
     * @return the docTypeText
     */
    public String getDocTypeText() {
        return docTypeText;
    }

    /**
     * @param docTypeText the docTypeText to set
     */
    public void setDocTypeText(String docTypeText) {
        this.docTypeText = docTypeText;
    }

    /**
     * @return the docNo
     */
    public String getDocNo() {
        return docNo;
    }

    /**
     * @param docNo the docNo to set
     */
    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    /**
     * @return the nonResident
     */
    public String getNonResident() {
        return nonResident;
    }

    /**
     * @param nonResident the nonResident to set
     */
    public void setNonResident(String nonResident) {
        this.nonResident = nonResident;
    }

    /**
     * @return the expired
     */
    public String getExpired() {
        return expired;
    }

    /**
     * @param expired the expired to set
     */
    public void setExpired(String expired) {
        this.expired = expired;
    }

    /**
     * @return the cOfDeath
     */
    public String getcOfDeath() {
        return cOfDeath;
    }

    /**
     * @param cOfDeath the cOfDeath to set
     */
    public void setcOfDeath(String cOfDeath) {
        this.cOfDeath = cOfDeath;
    }

    /**
     * @return the cOfDeathTxt
     */
    public String getcOfDeathTxt() {
        return cOfDeathTxt;
    }

    /**
     * @param cOfDeathTxt the cOfDeathTxt to set
     */
    public void setcOfDeathTxt(String cOfDeathTxt) {
        this.cOfDeathTxt = cOfDeathTxt;
    }

    /**
     * @return the quickAdm
     */
    public String getQuickAdm() {
        return quickAdm;
    }

    /**
     * @param quickAdm the quickAdm to set
     */
    public void setQuickAdm(String quickAdm) {
        this.quickAdm = quickAdm;
    }

    /**
     * @return the emergAdm
     */
    public String getEmergAdm() {
        return emergAdm;
    }

    /**
     * @param emergAdm the emergAdm to set
     */
    public void setEmergAdm(String emergAdm) {
        this.emergAdm = emergAdm;
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
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the patRef
     */
    public String getPatRef() {
        return patRef;
    }

    /**
     * @param patRef the patRef to set
     */
    public void setPatRef(String patRef) {
        this.patRef = patRef;
    }

    /**
     * @return the apptId
     */
    public String getApptId() {
        return apptId;
    }

    /**
     * @param apptId the apptId to set
     */
    public void setApptId(String apptId) {
        this.apptId = apptId;
    }

    /**
     * @return the vip
     */
    public String getVip() {
        return vip;
    }

    /**
     * @param vip the vip to set
     */
    public void setVip(String vip) {
        this.vip = vip;
    }

    /**
     * @return the inactive
     */
    public String getInactive() {
        return inactive;
    }

    /**
     * @param inactive the inactive to set
     */
    public void setInactive(String inactive) {
        this.inactive = inactive;
    }

    /**
     * @return the kasse
     */
    public String getKasse() {
        return kasse;
    }

    /**
     * @param kasse the kasse to set
     */
    public void setKasse(String kasse) {
        this.kasse = kasse;
    }

    public void readFromTable(final JCoTable aTable) {
        setClient(aTable.getString(0));
        setPatientId(aTable.getString(1));
        setChkDigitPat(aTable.getString(2));
        setExtPatId(aTable.getString(3));
        setInstitution(aTable.getString(4));
        setInstStext(aTable.getString(5));
        setCancelInd(aTable.getString(6));
        setLastNamePat(aTable.getString(7));
        setStdLnamePat(aTable.getString(8));
        setFrstNamePat(aTable.getString(9));
        setStdFnamePat(aTable.getString(10));
        setPrefix(aTable.getString(11));
        setAffix(aTable.getString(12));
        setTitle(aTable.getString(13));
        setPseudo(aTable.getString(14));
        setFullNamePat(aTable.getString(15));
        setBirthName(aTable.getString(16));
        setStdBnamePat(aTable.getString(17));
        setDob(aTable.getDate(18));
        setSex(aTable.getString(19));
        setSexExt(aTable.getString(20));
        setSexStext(aTable.getString(21));
        setSsn(aTable.getString(22));
        setDocType(aTable.getString(23));
        setDocTypeText(aTable.getString(24));
        setDocNo(aTable.getString(25));
        setNonResident(aTable.getString(26));
        setExpired(aTable.getString(27));
        setcOfDeath(aTable.getString(28));
        setcOfDeathTxt(aTable.getString(29));
        setQuickAdm(aTable.getString(30));
        setEmergAdm(aTable.getString(31));
        setCountry(aTable.getString(32));
        setCountryIso(aTable.getString(33));
        setCountryText(aTable.getString(34));
        setPcd(aTable.getString(35));
        setCity(aTable.getString(36));
        setDistrict(aTable.getString(37));
        setStrNo(aTable.getString(38));
        setPhoneNo(aTable.getString(39));
        setAddress(aTable.getString(40));
        setPatRef(aTable.getString(41));
        setApptId(aTable.getString(42));
        setVip(aTable.getString(43));
        setInactive(aTable.getString(44));
    }

    public void readFromStructure(final JCoStructure struct) {
        setClient(struct.getString(0));                      //MANDT
        setPatientId(struct.getString(1));                   //PATNR
        setChkDigitPat("");                                  //nv, nb (nicht vorhande, nicht benutzt)
        setExtPatId("");                                   //EXTNR, nb
        setInstitution(struct.getString(2));                 //EINRI
        setInstStext("");                                   //nv, nb
        setCancelInd("");                                   //nv, nb
        setLastNamePat(struct.getString(4));               //NNAME
        setStdLnamePat(struct.getString(5));               //NNAMS
        setFrstNamePat(struct.getString(6));               //VNAME
        setStdFnamePat(struct.getString(7));               //VNAMS
        setPrefix("");                                       //TITEL
        setAffix(struct.getString(10));                      //VORSW
        setTitle(struct.getString(8));                       //TITEL
        setPseudo(struct.getString(11));                     //NAME2
        setFullNamePat("");                                //nv, nb
        setBirthName(struct.getString(13));                 //GBNAM
        setStdBnamePat(struct.getString(14));              //GBNAS
        setDob(struct.getDate(12));                          //GBDAT
        setSex(struct.getString(3));                         //GSCHL
        setSexExt("");                                      //nv, nb
        setSexStext("");                                    //nv, nb
        setSsn("");                                          //RVNUM, nb
        setDocType("");                                     //PASSTY, nb
        setDocTypeText("");                                //nv, nb
        setDocNo("");                                       //PASSNR, nb
        setNonResident("");                                 //RESID, nb
        setExpired("");                                      //TODKZ, nb
        setcOfDeath("");                                  //TODUR, nb
        setcOfDeathTxt("");                                //nv, nb
        setQuickAdm("");                                    //KRZAN, nb
        setEmergAdm("");                                    //NOTAN, nb
        setCountry("");                                      //LAND, nb
        setCountryIso("");                                  //nv, nb
        setCountryText("");                                 //nv, nb
        setPcd("");                                          //PSTLZ, nb
        setCity("");                                         //ORT, nb
        setDistrict("");                                     //ORT2
        setStrNo("");                                       //STRAS
        setPhoneNo("");                                      //TELF1, nb
        setAddress("");                                      //nv, nb
        setPatRef("");                                      //RFPAT, nb
        setApptId("");                                      //nv, nb
        setVip("");                                          //VIPKZ, nb
        setInactive("");                                     //nv, nb
    }

    @Override
    public String toString() {
        return "Mandant >" + getClient() + "<\n"
                + "Patientennummer >" + getPatientId() + "<\n"
                + "Prüfziffer Patient >" + getChkDigitPat() + "<\n"
                + "Externe Patientenidentifikation >" + getExtPatId() + "<\n"
                + "Einrichtung >" + getInstitution() + "<\n"
                + "Kurzbezeichnung einer Einrichtung >" + getInstStext() + "<\n"
                + "Stornokennzeichen >" + getCancelInd() + "<\n"
                + "Nachname Patient >" + getLastNamePat() + "<\n"
                + "standardisierter Nachname >" + getStdLnamePat() + "<\n"
                + "Vorname Patient >" + getFrstNamePat() + "<\n"
                + "standardisierter Vorname >" + getStdFnamePat() + "<\n"
                + "Vorsatzwort >" + getPrefix() + "<\n"
                + "Namenszusatz >" + getAffix() + "<\n"
                + "Titel >" + getTitle() + "<\n"
                + "Pseudonym, Ordensname >" + getPseudo() + "<\n"
                + "Name des Patienten >" + getFullNamePat() + "<\n"
                + "Geburtsname >" + getBirthName() + "<\n"
                + "standardisierter Geburtsname >" + getStdBnamePat() + "<\n"
                + "Geburtsdatum >" + getDob() + "<\n"
                + "Geschlechtskennzeichen - intern >" + getSex() + "<\n"
                + "Geschlechtskennzeichen - anwenderspezifisch >" + getSexExt() + "<\n"
                + "Geschlechtsbezeichnung - anwenderspezifisch >" + getSexStext() + "<\n"
                + "Sozialversicherungsnummer >" + getSsn() + "<\n"
                + "Typ des Ausweisdokuments >" + getDocType() + "<\n"
                + "Text für Ausweisdokumenttyp >" + getDocTypeText() + "<\n"
                + "Ausweisdokumentnummer >" + getDocNo() + "<\n"
                + "Kennzeichen Patient kein Staatsbürger >" + getNonResident() + "<\n"
                + "Kennzeichen Patient verstorben >" + getExpired() + "<\n"
                + "Todesursache >" + getcOfDeath() + "<\n"
                + "Todesursachentext >" + getcOfDeathTxt() + "<\n"
                + "Kurzaufnahmekennzeichen >" + getQuickAdm() + "<\n"
                + "Notaufnahmekennzeichen >" + getEmergAdm() + "<\n"
                + "Land des Patientenwohnortes >" + getCountry() + "<\n"
                + "ISO-Code des Landes >" + getCountryIso() + "<\n"
                + "Bezeichnung des Landes >" + getCountryText() + "<\n"
                + "Postleitzahl des Orts >" + getPcd() + "<\n"
                + "Ort >" + getCity() + "<\n"
                + "Ortsteil >" + getDistrict() + "<\n"
                + "Straße >" + getStrNo() + "<\n"
                + "Telefon >" + getPhoneNo() + "<\n"
                + "Adresse des Patienten >" + getAddress() + "<\n"
                + "Verweis auf Patientennummer bei Zusammenführung >" + getPatRef() + "<\n"
                + "ID eines Termins >" + getApptId() + "<\n"
                + "VIP-Kennzeichen >" + getVip() + "<\n"
                + "Kennzeichen Inaktiv >" + getInactive();
    }

}
