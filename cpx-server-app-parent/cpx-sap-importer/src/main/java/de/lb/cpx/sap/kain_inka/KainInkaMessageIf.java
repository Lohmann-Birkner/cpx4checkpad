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
package de.lb.cpx.sap.kain_inka;

import java.util.Date;
import java.util.List;

/**
 *
 * @author gerschmann
 */
public interface KainInkaMessageIf {

    /**
     *
     * @param str str
     */
    void setProcessIdent(String str); //Verarbeitungskennzeichen

    /**
     *
     * @param str str
     */
    void setTransactionNumber(String str); //Laufende Nummer des Geschäftsvorfalls

    /**
     *
     * @param str str
     */
    void setSenderIdent(String str); //IK des Absenders

    /**
     *
     * @param str str
     */
    void setReceptorIdent(String str); //IK des Empfängers

    /**
     *
     * @param str str
     */
    void setInsuranceNumber(String str); //Krankenversicherten-Nr.

    /**
     *
     * @param it it
     */
    void setInsuranceType(int it); //Versichertenart

    /**
     *
     * @param sc sc
     */
    void setSpecialCirle(int sc); //comment	Besonderer Personenkreis

    /**
     *
     * @param da da
     */
    void setDmpAttendance(int da); //comment	DMP-Teilnahme

    /**
     *
     * @param str str
     */
    void setValidTo(String str); //Gültigkeit der Versichertenkarte

    /**
     *
     * @param str str
     */
    void setCaseNumberHospital(String str); //KH-internes Kennzeichen des Versicherten

    /**
     *
     * @param str str
     */
    void setCaseNumberINsurance(String str); //Fall-Nummer der Krankenkasse

    /**
     *
     * @param str str
     */
    void setRefNumberInsurance(String str); //Aktenzeichen der Krankenkasse

    /**
     *
     * @param dt dt
     */
    void setStartOfInsurance(Date dt); //comment	Tag des Beginns des Versicherungsschutzes entfällt?

    /**
     *
     * @param str str
     */
    void setContractIdent(String str); //Vertragskennzeichen

    /**
     *
     * @param str str
     */
    void setPatientName(String str); //Name des Versicherten

    /**
     *
     * @param str str
     */
    void setPatientFirstName(String str); //Vorname des Versicherten

    /**
     *
     * @param str str
     */
    void setGender(String str); //Geschlecht

    /**
     *
     * @param dt dt
     */
    void setBirthDate(Date dt); //Geburtsdatum des Versicherten

    /**
     *
     * @param str str
     */
    void setAddress(String str); //Strasse und Hausnummer

    /**
     *
     * @param str str
     */
    void setZipCode(String str); //Postleitzahl

    /**
     *
     * @param str str
     */
    void setCity(String str); //Wohnort

    /**
     *
     * @param str str
     */
    void setTitle(String str); //Titel des Versicherten

    /**
     *
     * @param str str
     */
    void setCountryIdent(String str); //Internationales Länderkennzeichen

    /**
     *
     * @param str str
     */
    void setNameAffix(String str); //Namenzusatz

    /**
     *
     * @param str str
     */
    void setNamePrefix(String str); //Vorsatzwort

    /**
     *
     * @param str str
     */
    void setAddressAddOn(String str); //Anschriftenzusatz

    /**
     *
     * @return process ident
     */
    String getProcessIdent(); //Verarbeitungskennzeichen

    /**
     *
     * @return transaction number
     */
    String getTransactionNumber(); //Laufende Nummer des Geschäftsvorfalls

    /**
     *
     * @return sender identifier
     */
    String getSenderIdent(); //IK des Absenders

    /**
     *
     * @return receptor identifier
     */
    String getReceptorIdent(); //IK des Empfängers

    /**
     *
     * @return insurance number
     */
    String getInsuranceNumber(); //Krankenversicherten-Nr.

    /**
     *
     * @return insurance type
     */
    int getInsuranceType(); //Versichertenart

    /**
     *
     * @return special circle
     */
    int getSpecialCirle(); //comment	Besonderer Personenkreis

    /**
     *
     * @return dmp attendance
     */
    int getDmpAttendance(); //comment	DMP-Teilnahme

    /**
     *
     * @return valid to
     */
    String getValidTo(); //Gültigkeit der Versichertenkarte

    /**
     *
     * @return case number hospital
     */
    String getCaseNumberHospital(); //KH-internes Kennzeichen des Versicherten

    /**
     *
     * @return case number insurance
     */
    String getCaseNumberInsurance(); //Fall-Nummer der Krankenkasse

    /**
     *
     * @return reference number insurance
     */
    String getRefNumberInsurance(); //Aktenzeichen der Krankenkasse

    /**
     *
     * @return start of insurance
     */
    Date getStartOfInsurance(); //comment	Tag des Beginns des Versicherungsschutzes entfällt?

    /**
     *
     * @return contract identifier
     */
    String getContractIdent(); //Vertragskennzeichen

    /**
     *
     * @return patient name
     */
    String getPatientName(); //Name des Versicherten

    /**
     *
     * @return patient first name
     */
    String getPatientFirstName(); //Vorname des Versicherten

    /**
     *
     * @return gender
     */
    String getGender(); //Geschlecht

    /**
     *
     * @return birth date
     */
    Date getBirthDate(); //Geburtsdatum des Versicherten

    /**
     *
     * @return address
     */
    String getAddress(); //Strasse und Hausnummer

    /**
     *
     * @return zip code
     */
    String getZipCode(); //Postleitzahl

    /**
     *
     * @return city
     */
    String getCity(); //Wohnort

    /**
     *
     * @return title
     */
    String getTitle(); //Titel des Versicherten

    /**
     *
     * @return country identifier
     */
    String getCountryIdent(); //Internationales Länderkennzeichen

    /**
     *
     * @return name affix
     */
    String getNameAffix(); //Namenzusatz

    /**
     *
     * @return name prefix
     */
    String getNamePrefix(); //Vorsatzwort

    /**
     *
     * @return adress add on
     */
    String getAddressAddOn(); //Anschriftenzusatz

    /**
     *
     * @return list of kain/inka elements
     */
    List<KainInkaElement> getPVVs();
}
