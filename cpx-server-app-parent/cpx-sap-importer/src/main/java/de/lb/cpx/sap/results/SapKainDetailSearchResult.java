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

import com.sap.conn.jco.JCoParameterList;
//import static de.lb.cpx.sap.importer.ImportProcessSap.ANONYMOUSIZE_DATA;
import static de.lb.cpx.sap.importer.utils.SapStrUtils.*;
import de.lb.cpx.sap.kain_inka.KainInkaElement;
import de.lb.cpx.sap.kain_inka.KainInkaMessage;
import de.lb.cpx.sap.kain_inka.KainInkaMessageIf;
import de.lb.cpx.sap.kain_inka.KainInkaProcessor;
import de.lb.cpx.sap.kain_inka.PvvChildSegment;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author urbach
 */
public class SapKainDetailSearchResult {

    private String worksign = "";
    private String cntevent = "";
    private String sender = "";
    private String receiver = "";
    private String patinsno = "";
    private String patinsst1 = "";
    private String specpgroup = "";
    private String dmppartici = "";
    private String insenddat = ""; //date
    private String cosbeahins = "";
    private String coscaseno = "";
    private String coscaseref = "";
    private Date insbegdat = null; //date
    private String contractcd = "";
    private String inssurname = "";
    private String insfstname = "";
    private String insgender = "";
    private Date insbirthdt = null; //date
    private String insuredstr = "";
    private String insuredpcd = "";
    private String insuredcit = "";
    private String insuredtit = "";
    private String insuredlnd = "";
    private String insnamszus = "";
    private String insvorsatz = "";
    private String insanschzu = "";

    private String ikz = "";
    //public String patientnr = "";

    private String falnr = "";
    private String einri = "";
    private String lf301 = "";
    private String kostr = "";
    private Date receivingDate = null; //date
    
    private boolean doAnonymize = false;

    private List<SapKainPvvSearchResult> alPVVs = new ArrayList<>(0);

    /**
     *
     */
    public SapKainDetailSearchResult() {
    }

    public SapKainDetailSearchResult(boolean pDoAnonymize) {
        this.doAnonymize = pDoAnonymize;
    }

    /**
     * @return the worksign
     */
    public String getWorksign() {
        return worksign;
    }

    /**
     * @param worksign the worksign to set
     */
    public void setWorksign(String worksign) {
        this.worksign = worksign;
    }

    /**
     * @return the cntevent
     */
    public String getCntevent() {
        return cntevent;
    }

    /**
     * @param cntevent the cntevent to set
     */
    public void setCntevent(String cntevent) {
        this.cntevent = cntevent;
    }

    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return the receiver
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * @param receiver the receiver to set
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * @return the patinsno
     */
    public String getPatinsno() {
        return patinsno;
    }

    /**
     * @param patinsno the patinsno to set
     */
    public void setPatinsno(String patinsno) {
        this.patinsno = patinsno;
    }

    /**
     * @return the patinsst1
     */
    public String getPatinsst1() {
        return patinsst1;
    }

    /**
     * @param patinsst1 the patinsst1 to set
     */
    public void setPatinsst1(String patinsst1) {
        this.patinsst1 = patinsst1;
    }

    /**
     * @return the specpgroup
     */
    public String getSpecpgroup() {
        return specpgroup;
    }

    /**
     * @param specpgroup the specpgroup to set
     */
    public void setSpecpgroup(String specpgroup) {
        this.specpgroup = specpgroup;
    }

    /**
     * @return the dmppartici
     */
    public String getDmppartici() {
        return dmppartici;
    }

    /**
     * @param dmppartici the dmppartici to set
     */
    public void setDmppartici(String dmppartici) {
        this.dmppartici = dmppartici;
    }

    /**
     * @return the insenddat
     */
    public String getInsenddat() {
        return insenddat;
    }

    /**
     * @param insenddat the insenddat to set
     */
    public void setInsenddat(String insenddat) {
        this.insenddat = insenddat;
    }

    /**
     * @return the cosbeahins
     */
    public String getCosbeahins() {
        return cosbeahins;
    }

    /**
     * @param cosbeahins the cosbeahins to set
     */
    public void setCosbeahins(String cosbeahins) {
        this.cosbeahins = cosbeahins;
    }

    /**
     * @return the coscaseno
     */
    public String getCoscaseno() {
        return coscaseno;
    }

    /**
     * @param coscaseno the coscaseno to set
     */
    public void setCoscaseno(String coscaseno) {
        this.coscaseno = coscaseno;
    }

    /**
     * @return the coscaseref
     */
    public String getCoscaseref() {
        return coscaseref;
    }

    /**
     * @param coscaseref the coscaseref to set
     */
    public void setCoscaseref(String coscaseref) {
        this.coscaseref = coscaseref;
    }

    /**
     * @return the insbegdat
     */
    public Date getInsbegdat() {
        return insbegdat == null ? null : new Date(insbegdat.getTime());
    }

    /**
     * @param insbegdat the insbegdat to set
     */
    public void setInsbegdat(Date insbegdat) {
        this.insbegdat = insbegdat == null ? null : new Date(insbegdat.getTime());
    }

    /**
     * @return the contractcd
     */
    public String getContractcd() {
        return contractcd;
    }

    /**
     * @param contractcd the contractcd to set
     */
    public void setContractcd(String contractcd) {
        this.contractcd = contractcd;
    }

    /**
     * @return the inssurname
     */
    public String getInssurname() {
        return inssurname;
    }

    /**
     * @param inssurname the inssurname to set
     */
    public void setInssurname(String inssurname) {
        this.inssurname = inssurname;
    }

    /**
     * @return the insfstname
     */
    public String getInsfstname() {
        return insfstname;
    }

    /**
     * @param insfstname the insfstname to set
     */
    public void setInsfstname(String insfstname) {
        this.insfstname = insfstname;
    }

    /**
     * @return the insgender
     */
    public String getInsgender() {
        return insgender;
    }

    /**
     * @param insgender the insgender to set
     */
    public void setInsgender(String insgender) {
        this.insgender = insgender;
    }

    /**
     * @return the insbirthdt
     */
    public Date getInsbirthdt() {
        return insbirthdt == null ? null : new Date(insbirthdt.getTime());
    }

    /**
     * @param insbirthdt the insbirthdt to set
     */
    public void setInsbirthdt(Date insbirthdt) {
        this.insbirthdt = insbirthdt == null ? null : new Date(insbirthdt.getTime());
    }

    /**
     * @return the insuredstr
     */
    public String getInsuredstr() {
        return insuredstr;
    }

    /**
     * @param insuredstr the insuredstr to set
     */
    public void setInsuredstr(String insuredstr) {
        this.insuredstr = insuredstr;
    }

    /**
     * @return the insuredpcd
     */
    public String getInsuredpcd() {
        return insuredpcd;
    }

    /**
     * @param insuredpcd the insuredpcd to set
     */
    public void setInsuredpcd(String insuredpcd) {
        this.insuredpcd = insuredpcd;
    }

    /**
     * @return the insuredcit
     */
    public String getInsuredcit() {
        return insuredcit;
    }

    /**
     * @param insuredcit the insuredcit to set
     */
    public void setInsuredcit(String insuredcit) {
        this.insuredcit = insuredcit;
    }

    /**
     * @return the insuredtit
     */
    public String getInsuredtit() {
        return insuredtit;
    }

    /**
     * @param insuredtit the insuredtit to set
     */
    public void setInsuredtit(String insuredtit) {
        this.insuredtit = insuredtit;
    }

    /**
     * @return the insuredlnd
     */
    public String getInsuredlnd() {
        return insuredlnd;
    }

    /**
     * @param insuredlnd the insuredlnd to set
     */
    public void setInsuredlnd(String insuredlnd) {
        this.insuredlnd = insuredlnd;
    }

    /**
     * @return the insnamszus
     */
    public String getInsnamszus() {
        return insnamszus;
    }

    /**
     * @param insnamszus the insnamszus to set
     */
    public void setInsnamszus(String insnamszus) {
        this.insnamszus = insnamszus;
    }

    /**
     * @return the insvorsatz
     */
    public String getInsvorsatz() {
        return insvorsatz;
    }

    /**
     * @param insvorsatz the insvorsatz to set
     */
    public void setInsvorsatz(String insvorsatz) {
        this.insvorsatz = insvorsatz;
    }

    /**
     * @return the insanschzu
     */
    public String getInsanschzu() {
        return insanschzu;
    }

    /**
     * @param insanschzu the insanschzu to set
     */
    public void setInsanschzu(String insanschzu) {
        this.insanschzu = insanschzu;
    }

    /**
     * @return the ikz
     */
    public String getIkz() {
        return ikz;
    }

    /**
     * @param ikz the ikz to set
     */
    public void setIkz(String ikz) {
        this.ikz = ikz;
    }

    /**
     * @return the falnr
     */
    public String getFalnr() {
        return falnr;
    }

    /**
     * @param falnr the falnr to set
     */
    public void setFalnr(String falnr) {
        this.falnr = falnr;
    }

    /**
     * @return the einri
     */
    public String getEinri() {
        return einri;
    }

    /**
     * @param einri the einri to set
     */
    public void setEinri(String einri) {
        this.einri = einri;
    }

    /**
     * @return the lf301
     */
    public String getLf301() {
        return lf301;
    }

    /**
     * @param lf301 the lf301 to set
     */
    public void setLf301(String lf301) {
        this.lf301 = lf301;
    }

    /**
     * @return the kostr
     */
    public String getKostr() {
        return kostr;
    }

    /**
     * @param kostr the kostr to set
     */
    public void setKostr(String kostr) {
        this.kostr = kostr;
    }

    /**
     * @return the receivingDate
     */
    public Date getReceivingDate() {
        return receivingDate;
    }

    /**
     * @param recieveDate the kostr to set
     */
    public void setReceivingDate(Date recieveDate) {
        this.receivingDate = recieveDate;
    }

    /**
     * @return the alPVVs
     */
    public List<SapKainPvvSearchResult> getAlPVVs() {
        return new ArrayList<>(alPVVs);
    }
//
//    /**
//     * @param alPVVs the alPVVs to set
//     */
//    public void setAlPVVs(List<SapKainPvvSearchResult> alPVVs) {
//        this.alPVVs = alPVVs;
//    }

    /**
     *
     * @param aParamList JCO parameter list
     */
    public void readFromParameterList(final JCoParameterList aParamList) {
        setWorksign(aParamList.getString("FKT_WORKSIGN"));
        setCntevent(aParamList.getString("FKT_CNTEVENT"));
        setSender(aParamList.getString("FKT_SENDER"));
        setReceiver(aParamList.getString("FKT_RECEIVER"));
        setPatinsno(doAnonymize ? ("ANO_" + getHash(aParamList.getString("INV_PATINSNO"), 16)) : aParamList.getString("INV_PATINSNO"));
        setPatinsst1(aParamList.getString("INV_PATINSST1"));
        setSpecpgroup(aParamList.getString("INV_SPECPGROUP"));
        setDmppartici(aParamList.getString("INV_DMPPARTICI"));
        setInsenddat(aParamList.getString("INV_INSENDDAT")); //date
        setCosbeahins(aParamList.getString("INV_COSBEAHINS"));
        setCoscaseno(aParamList.getString("INV_COSCASENO"));
        setCoscaseref(aParamList.getString("INV_COSCASEREF"));
        setInsbegdat(aParamList.getDate("INV_INSBEGDAT")); //date
        setContractcd(aParamList.getString("INV_CONTRACTCD"));
        setInssurname(doAnonymize ? "Anonym" : aParamList.getString("NAD_INSSURNAME"));
        setInsfstname(doAnonymize ? "Anonym" : aParamList.getString("NAD_INSFSTNAME"));
        setInsgender(aParamList.getString("NAD_INSGENDER"));
        setInsbirthdt(aParamList.getDate("NAD_INSBIRTHDT")); //date
        setInsuredstr(doAnonymize ? "Anonym" : aParamList.getString("NAD_INSUREDSTR"));
        setInsuredpcd(doAnonymize ? "" : aParamList.getString("NAD_INSUREDPCD"));
        setInsuredcit(doAnonymize ? "Anonym" : aParamList.getString("NAD_INSUREDCIT"));
        setInsuredtit(aParamList.getString("NAD_INSUREDTIT"));
        setInsuredlnd(aParamList.getString("NAD_INSUREDLND"));
        setInsnamszus(aParamList.getString("NAD_INSNAMSZUS"));
        setInsvorsatz(aParamList.getString("NAD_INSVORSATZ"));
        setInsanschzu(aParamList.getString("NAD_INSANSCHZU"));
    }

    @Override
    public String toString() {
        String result
                = "Verarbeitungskennzeichen  >" + getWorksign() + "<\r\n"
                + "Laufende Nummer des Geschäftsvorfalls  >" + getCntevent() + "<\r\n"
                + "IK des Absenders  >" + getSender() + "<\r\n"
                + "IK des Empfängers  >" + getReceiver() + "<\r\n"
                + "Krankenversicherten-Nr.  >" + getPatinsno() + "<\r\n"
                + "Versichertenart  >" + getPatinsst1() + "<\r\n"
                + "Besonderer Personenkreis  >" + getSpecpgroup() + "<\r\n"
                + "DMP-Teilnahme  >" + getDmppartici() + "<\r\n"
                + "Gültigkeit der Versichertenkarte  >" + getInsenddat() + "<\r\n"
                + "KH-internes Kennzeichen des Versicherten  >" + getCosbeahins() + "<\r\n"
                + "Fall-Nummer der Krankenkasse  >" + getCoscaseno() + "<\r\n"
                + "Aktenzeichen der Krankenkasse  >" + getCoscaseref() + "<\r\n"
                + "Tag des Beginns des Versicherungsschutzes  >" + getInsbegdat() + "<\r\n"
                + "Vertragskennzeichen  >" + getContractcd() + "<\r\n"
                + "Name des Versicherten  >" + getInssurname() + "<\r\n"
                + "Vorname des Versicherten  >" + getInsfstname() + "<\r\n"
                + "Geschlecht  >" + getInsgender() + "<\r\n"
                + "Geburtsdatum des Versicherten  >" + getInsbirthdt() + "<\r\n"
                + "Straße und Haus-Nr.  >" + getInsuredstr() + "<\r\n"
                + "Postleitzahl  >" + getInsuredpcd() + "<\r\n"
                + "Wohnort  >" + getInsuredcit() + "<\r\n"
                + "Titel des Versicherten  >" + getInsuredtit() + "<\r\n"
                + "Internationales Länderkennzeichen  >" + getInsuredlnd() + "<\r\n"
                + "Namenszusatz  >" + getInsnamszus() + "<\r\n"
                + "Vorsatzwort  >" + getInsvorsatz() + "<\r\n"
                + "Anschriftenzusatz  >" + getInsanschzu() + "<\r\n";
        for (SapKainPvvSearchResult pvv : alPVVs) {
            result += pvv.toString();
        }
        return result;
    }

    /**
     *
     * @param list list of pvv search results
     */
    public void addPVVElement(List<SapKainPvvSearchResult> list) {
        if (list == null) {
            return;
        }
        alPVVs = new ArrayList<>(list);
    }

    /**
     *
     * @param fallNr case number
     * @param institution institution
     * @param msgNumber message number
     * @param kostrNumber cost unit number
     */
    public void setCaseIndicator(String fallNr, String institution, String msgNumber, String kostrNumber) {
        setFalnr(fallNr);
        setEinri(institution);
        setLf301(msgNumber);
        setKostr(kostrNumber);
    }

    /**
     *
     * @return comma separated bill numbers
     */
    public String getAllBillNumbers() {
        String allBillNumbersFromPVV = "";

        for (int i = 0, n = alPVVs.size(); i < n; i++) {
            SapKainPvvSearchResult pvvElement = alPVVs.get(i);
            if (pvvElement != null) {
                String billNumber = pvvElement.getBillNumber();
                if (billNumber != null) {
                    if (!allBillNumbersFromPVV.contains(billNumber)) {
                        if (i > 0) {
                            allBillNumbersFromPVV = allBillNumbersFromPVV + ",";
                        }
                        allBillNumbersFromPVV = allBillNumbersFromPVV + billNumber;
                    }
                }
            }
        }
        return allBillNumbersFromPVV;
    }

    /**
     *
     * @return kain/inka message
     */
    public KainInkaMessage createKAINMessage() {
        KainInkaMessage msg = null;
//        try {
        msg = KainInkaProcessor.createOriginalMessage();
        // fill fields
        msg.setProcessIdent(getWorksign());
        msg.setTransactionNumber(getCntevent());
        msg.setSenderIdent(getSender());
        msg.setReceptorIdent(getReceiver());
        msg.setInsuranceNumber(getPatinsno());
        // integer
        //msg.setInsuranceType(patinsst1);
        //msg.setSpecialCirle(specpgroup);
        //msg.setDmpAttendance( dmppartici);
        msg.setValidTo(getInsenddat());
        msg.setCaseNumberHospital(getCosbeahins());
        msg.setCaseNumberINsurance(getCoscaseno());
        msg.setRefNumberInsurance(getCoscaseref());
        msg.setStartOfInsurance(getInsbegdat());
        msg.setContractIdent(getContractcd());
        msg.setPatientName(getInssurname());
        msg.setPatientFirstName(getInsfstname());
        msg.setGender(getInsgender());
        msg.setBirthDate(getInsbirthdt());
        msg.setAddress(getInsuredstr());
        msg.setZipCode(getInsuredpcd());
        msg.setCity(getInsuredcit());
        msg.setTitle(getInsuredtit());
        msg.setCountryIdent(getInsuredlnd());
        msg.setNameAffix(getInsnamszus());
        msg.setNamePrefix(getInsvorsatz());
        msg.setAddressAddOn(getInsanschzu());

        for (int i = 0, n = alPVVs.size(); i < n; i++) {
            SapKainPvvSearchResult pvv = alPVVs.get(i);
            msg.addPVV(pvv, i);
        }

//        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "This should not happen...", e);
//            //e.printStackTrace();
//        }
        return msg;
    }

    /**
     *
     * @param msg kain/inka message
     */
    public void fillFromKainMessage(KainInkaMessageIf msg) {
        setWorksign(msg.getProcessIdent());
        setCntevent(msg.getTransactionNumber());
        setSender(msg.getSenderIdent());
        setReceiver(msg.getReceptorIdent());
        setPatinsno(msg.getInsuranceNumber());
        // integer
        // = msg.getInsuranceType(patinsst1);
        // = msg.getSpecialCirle(specpgroup);
        // = msg.getDmpAttendance( dmppartici);
        setInsenddat(msg.getValidTo());
        setCosbeahins(msg.getCaseNumberHospital());
        setCoscaseno(msg.getCaseNumberInsurance());
        setCoscaseref(msg.getRefNumberInsurance());
        setInsbegdat(msg.getStartOfInsurance());
        setContractcd(msg.getContractIdent());
        setInssurname(msg.getPatientName());
        setInsfstname(msg.getPatientFirstName());
        setInsgender(msg.getGender());
        setInsbirthdt(msg.getBirthDate());
        setInsuredstr(msg.getAddress());
        setInsuredpcd(msg.getZipCode());
        setInsuredcit(msg.getCity());
        setInsuredtit(msg.getTitle());
        setInsuredlnd(msg.getCountryIdent());
        setInsnamszus(msg.getNameAffix());
        setInsvorsatz(msg.getNamePrefix());
        setInsanschzu(msg.getAddressAddOn());

        //setAlPVVs((List<SapKainPvvSearchResult>) new ArrayList<>());
        List<KainInkaElement> pvvs = msg.getPVVs();

        for (int i = 0, n = pvvs.size(); i < n; i++) {

            KainInkaElement ki = pvvs.get(i);
            if (ki instanceof PvvChildSegment) {
                SapKainPvvSearchResult spvv = new SapKainPvvSearchResult();
                alPVVs.add(spvv);
                spvv.setData((PvvChildSegment) ki);
            }

        }

    }

}
