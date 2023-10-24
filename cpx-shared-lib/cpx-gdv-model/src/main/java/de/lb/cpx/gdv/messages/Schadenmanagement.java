//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.19 um 11:55:14 AM CEST 
//


package de.lb.cpx.gdv.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Reparaturkostenuebernahme" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="SicherungsabtretungVorhanden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Repabsicht" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Repauftrag" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="RegulierungBereitsErfolgt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="ReparaturBereitsErfolgt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="VermittlerReguliert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="RegulierungAn" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
 *                   &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="SchadenmanagementGewuenscht" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="FiktiveAbrechnungGewuenscht" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="VereinbarungenGetroffen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Vereinbarungen" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Besichtigung" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="SchadenBesichtigt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="BesichtigungDurch" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
 *                             &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="BesichtigungsInfo" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BesichtigungsInfoTyp" minOccurs="0"/>
 *                   &lt;element name="BesichtigungBei" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
 *                             &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="TerminwunschBesichtigung" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="8"/>
 *                         &lt;minLength value="8"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="BeschreibungBesichtigung" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="80"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Beeintraechtigungen" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="WohnenBeeintraechtigt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="erheblichBeeintraechtigt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="DauerBeeintraechtigung" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="80"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="SicherheitBeeintraechtigt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="ArbeitsablaufBeeintraechtigt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="BUDauertAn" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="EindringenDurchBeschaedigungMoeglich" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
 *                           &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4201">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="4"/>
 *             &lt;minLength value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Versionsnummer" fixed="001">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="3"/>
 *             &lt;minLength value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "reparaturkostenuebernahme",
    "sicherungsabtretungVorhanden",
    "repabsicht",
    "repauftrag",
    "regulierungBereitsErfolgt",
    "reparaturBereitsErfolgt",
    "vermittlerReguliert",
    "regulierungAn",
    "schadenmanagementGewuenscht",
    "fiktiveAbrechnungGewuenscht",
    "vereinbarungenGetroffen",
    "vereinbarungen",
    "besichtigung",
    "beeintraechtigungen"
})
@XmlRootElement(name = "Schadenmanagement")
public class Schadenmanagement {

    @XmlElement(name = "Reparaturkostenuebernahme")
    protected String reparaturkostenuebernahme;
    @XmlElement(name = "SicherungsabtretungVorhanden")
    protected String sicherungsabtretungVorhanden;
    @XmlElement(name = "Repabsicht")
    protected String repabsicht;
    @XmlElement(name = "Repauftrag")
    protected String repauftrag;
    @XmlElement(name = "RegulierungBereitsErfolgt")
    protected String regulierungBereitsErfolgt;
    @XmlElement(name = "ReparaturBereitsErfolgt")
    protected String reparaturBereitsErfolgt;
    @XmlElement(name = "VermittlerReguliert")
    protected String vermittlerReguliert;
    @XmlElement(name = "RegulierungAn")
    protected Schadenmanagement.RegulierungAn regulierungAn;
    @XmlElement(name = "SchadenmanagementGewuenscht")
    protected String schadenmanagementGewuenscht;
    @XmlElement(name = "FiktiveAbrechnungGewuenscht")
    protected String fiktiveAbrechnungGewuenscht;
    @XmlElement(name = "VereinbarungenGetroffen")
    protected String vereinbarungenGetroffen;
    @XmlElement(name = "Vereinbarungen")
    protected Schadenmanagement.Vereinbarungen vereinbarungen;
    @XmlElement(name = "Besichtigung")
    protected Schadenmanagement.Besichtigung besichtigung;
    @XmlElement(name = "Beeintraechtigungen")
    protected Schadenmanagement.Beeintraechtigungen beeintraechtigungen;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der reparaturkostenuebernahme-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReparaturkostenuebernahme() {
        return reparaturkostenuebernahme;
    }

    /**
     * Legt den Wert der reparaturkostenuebernahme-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReparaturkostenuebernahme(String value) {
        this.reparaturkostenuebernahme = value;
    }

    /**
     * Ruft den Wert der sicherungsabtretungVorhanden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSicherungsabtretungVorhanden() {
        return sicherungsabtretungVorhanden;
    }

    /**
     * Legt den Wert der sicherungsabtretungVorhanden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSicherungsabtretungVorhanden(String value) {
        this.sicherungsabtretungVorhanden = value;
    }

    /**
     * Ruft den Wert der repabsicht-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepabsicht() {
        return repabsicht;
    }

    /**
     * Legt den Wert der repabsicht-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepabsicht(String value) {
        this.repabsicht = value;
    }

    /**
     * Ruft den Wert der repauftrag-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepauftrag() {
        return repauftrag;
    }

    /**
     * Legt den Wert der repauftrag-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepauftrag(String value) {
        this.repauftrag = value;
    }

    /**
     * Ruft den Wert der regulierungBereitsErfolgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegulierungBereitsErfolgt() {
        return regulierungBereitsErfolgt;
    }

    /**
     * Legt den Wert der regulierungBereitsErfolgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegulierungBereitsErfolgt(String value) {
        this.regulierungBereitsErfolgt = value;
    }

    /**
     * Ruft den Wert der reparaturBereitsErfolgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReparaturBereitsErfolgt() {
        return reparaturBereitsErfolgt;
    }

    /**
     * Legt den Wert der reparaturBereitsErfolgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReparaturBereitsErfolgt(String value) {
        this.reparaturBereitsErfolgt = value;
    }

    /**
     * Ruft den Wert der vermittlerReguliert-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVermittlerReguliert() {
        return vermittlerReguliert;
    }

    /**
     * Legt den Wert der vermittlerReguliert-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVermittlerReguliert(String value) {
        this.vermittlerReguliert = value;
    }

    /**
     * Ruft den Wert der regulierungAn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Schadenmanagement.RegulierungAn }
     *     
     */
    public Schadenmanagement.RegulierungAn getRegulierungAn() {
        return regulierungAn;
    }

    /**
     * Legt den Wert der regulierungAn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Schadenmanagement.RegulierungAn }
     *     
     */
    public void setRegulierungAn(Schadenmanagement.RegulierungAn value) {
        this.regulierungAn = value;
    }

    /**
     * Ruft den Wert der schadenmanagementGewuenscht-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenmanagementGewuenscht() {
        return schadenmanagementGewuenscht;
    }

    /**
     * Legt den Wert der schadenmanagementGewuenscht-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenmanagementGewuenscht(String value) {
        this.schadenmanagementGewuenscht = value;
    }

    /**
     * Ruft den Wert der fiktiveAbrechnungGewuenscht-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFiktiveAbrechnungGewuenscht() {
        return fiktiveAbrechnungGewuenscht;
    }

    /**
     * Legt den Wert der fiktiveAbrechnungGewuenscht-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFiktiveAbrechnungGewuenscht(String value) {
        this.fiktiveAbrechnungGewuenscht = value;
    }

    /**
     * Ruft den Wert der vereinbarungenGetroffen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVereinbarungenGetroffen() {
        return vereinbarungenGetroffen;
    }

    /**
     * Legt den Wert der vereinbarungenGetroffen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVereinbarungenGetroffen(String value) {
        this.vereinbarungenGetroffen = value;
    }

    /**
     * Ruft den Wert der vereinbarungen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Schadenmanagement.Vereinbarungen }
     *     
     */
    public Schadenmanagement.Vereinbarungen getVereinbarungen() {
        return vereinbarungen;
    }

    /**
     * Legt den Wert der vereinbarungen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Schadenmanagement.Vereinbarungen }
     *     
     */
    public void setVereinbarungen(Schadenmanagement.Vereinbarungen value) {
        this.vereinbarungen = value;
    }

    /**
     * Ruft den Wert der besichtigung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Schadenmanagement.Besichtigung }
     *     
     */
    public Schadenmanagement.Besichtigung getBesichtigung() {
        return besichtigung;
    }

    /**
     * Legt den Wert der besichtigung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Schadenmanagement.Besichtigung }
     *     
     */
    public void setBesichtigung(Schadenmanagement.Besichtigung value) {
        this.besichtigung = value;
    }

    /**
     * Ruft den Wert der beeintraechtigungen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Schadenmanagement.Beeintraechtigungen }
     *     
     */
    public Schadenmanagement.Beeintraechtigungen getBeeintraechtigungen() {
        return beeintraechtigungen;
    }

    /**
     * Legt den Wert der beeintraechtigungen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Schadenmanagement.Beeintraechtigungen }
     *     
     */
    public void setBeeintraechtigungen(Schadenmanagement.Beeintraechtigungen value) {
        this.beeintraechtigungen = value;
    }

    /**
     * Ruft den Wert der satzart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSatzart() {
        if (satzart == null) {
            return "4201";
        } else {
            return satzart;
        }
    }

    /**
     * Legt den Wert der satzart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSatzart(String value) {
        this.satzart = value;
    }

    /**
     * Ruft den Wert der versionsnummer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionsnummer() {
        if (versionsnummer == null) {
            return "001";
        } else {
            return versionsnummer;
        }
    }

    /**
     * Legt den Wert der versionsnummer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionsnummer(String value) {
        this.versionsnummer = value;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="WohnenBeeintraechtigt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="erheblichBeeintraechtigt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="DauerBeeintraechtigung" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="80"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="SicherheitBeeintraechtigt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="ArbeitsablaufBeeintraechtigt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="BUDauertAn" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="EindringenDurchBeschaedigungMoeglich" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
     *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "wohnenBeeintraechtigt",
        "erheblichBeeintraechtigt",
        "dauerBeeintraechtigung",
        "sicherheitBeeintraechtigt",
        "arbeitsablaufBeeintraechtigt",
        "buDauertAn",
        "eindringenDurchBeschaedigungMoeglich"
    })
    public static class Beeintraechtigungen {

        @XmlElement(name = "WohnenBeeintraechtigt")
        protected String wohnenBeeintraechtigt;
        protected String erheblichBeeintraechtigt;
        @XmlElement(name = "DauerBeeintraechtigung")
        protected String dauerBeeintraechtigung;
        @XmlElement(name = "SicherheitBeeintraechtigt")
        protected String sicherheitBeeintraechtigt;
        @XmlElement(name = "ArbeitsablaufBeeintraechtigt")
        protected String arbeitsablaufBeeintraechtigt;
        @XmlElement(name = "BUDauertAn")
        protected String buDauertAn;
        @XmlElement(name = "EindringenDurchBeschaedigungMoeglich")
        protected Schadenmanagement.Beeintraechtigungen.EindringenDurchBeschaedigungMoeglich eindringenDurchBeschaedigungMoeglich;

        /**
         * Ruft den Wert der wohnenBeeintraechtigt-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getWohnenBeeintraechtigt() {
            return wohnenBeeintraechtigt;
        }

        /**
         * Legt den Wert der wohnenBeeintraechtigt-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setWohnenBeeintraechtigt(String value) {
            this.wohnenBeeintraechtigt = value;
        }

        /**
         * Ruft den Wert der erheblichBeeintraechtigt-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getErheblichBeeintraechtigt() {
            return erheblichBeeintraechtigt;
        }

        /**
         * Legt den Wert der erheblichBeeintraechtigt-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setErheblichBeeintraechtigt(String value) {
            this.erheblichBeeintraechtigt = value;
        }

        /**
         * Ruft den Wert der dauerBeeintraechtigung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDauerBeeintraechtigung() {
            return dauerBeeintraechtigung;
        }

        /**
         * Legt den Wert der dauerBeeintraechtigung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDauerBeeintraechtigung(String value) {
            this.dauerBeeintraechtigung = value;
        }

        /**
         * Ruft den Wert der sicherheitBeeintraechtigt-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSicherheitBeeintraechtigt() {
            return sicherheitBeeintraechtigt;
        }

        /**
         * Legt den Wert der sicherheitBeeintraechtigt-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSicherheitBeeintraechtigt(String value) {
            this.sicherheitBeeintraechtigt = value;
        }

        /**
         * Ruft den Wert der arbeitsablaufBeeintraechtigt-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getArbeitsablaufBeeintraechtigt() {
            return arbeitsablaufBeeintraechtigt;
        }

        /**
         * Legt den Wert der arbeitsablaufBeeintraechtigt-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setArbeitsablaufBeeintraechtigt(String value) {
            this.arbeitsablaufBeeintraechtigt = value;
        }

        /**
         * Ruft den Wert der buDauertAn-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBUDauertAn() {
            return buDauertAn;
        }

        /**
         * Legt den Wert der buDauertAn-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBUDauertAn(String value) {
            this.buDauertAn = value;
        }

        /**
         * Ruft den Wert der eindringenDurchBeschaedigungMoeglich-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Schadenmanagement.Beeintraechtigungen.EindringenDurchBeschaedigungMoeglich }
         *     
         */
        public Schadenmanagement.Beeintraechtigungen.EindringenDurchBeschaedigungMoeglich getEindringenDurchBeschaedigungMoeglich() {
            return eindringenDurchBeschaedigungMoeglich;
        }

        /**
         * Legt den Wert der eindringenDurchBeschaedigungMoeglich-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Schadenmanagement.Beeintraechtigungen.EindringenDurchBeschaedigungMoeglich }
         *     
         */
        public void setEindringenDurchBeschaedigungMoeglich(Schadenmanagement.Beeintraechtigungen.EindringenDurchBeschaedigungMoeglich value) {
            this.eindringenDurchBeschaedigungMoeglich = value;
        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
         *       &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "value"
        })
        public static class EindringenDurchBeschaedigungMoeglich {

            @XmlValue
            protected String value;
            @XmlAttribute(name = "Satzende")
            protected String satzende;

            /**
             * Ruft den Wert der value-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValue() {
                return value;
            }

            /**
             * Legt den Wert der value-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValue(String value) {
                this.value = value;
            }

            /**
             * Ruft den Wert der satzende-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSatzende() {
                return satzende;
            }

            /**
             * Legt den Wert der satzende-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSatzende(String value) {
                this.satzende = value;
            }

        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="SchadenBesichtigt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="BesichtigungDurch" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
     *                   &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="BesichtigungsInfo" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BesichtigungsInfoTyp" minOccurs="0"/>
     *         &lt;element name="BesichtigungBei" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
     *                   &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="TerminwunschBesichtigung" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="8"/>
     *               &lt;minLength value="8"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="BeschreibungBesichtigung" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="80"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "schadenBesichtigt",
        "besichtigungDurch",
        "besichtigungsInfo",
        "besichtigungBei",
        "terminwunschBesichtigung",
        "beschreibungBesichtigung"
    })
    public static class Besichtigung {

        @XmlElement(name = "SchadenBesichtigt")
        protected String schadenBesichtigt;
        @XmlElement(name = "BesichtigungDurch")
        protected Schadenmanagement.Besichtigung.BesichtigungDurch besichtigungDurch;
        @XmlElement(name = "BesichtigungsInfo")
        protected String besichtigungsInfo;
        @XmlElement(name = "BesichtigungBei")
        protected Schadenmanagement.Besichtigung.BesichtigungBei besichtigungBei;
        @XmlElement(name = "TerminwunschBesichtigung")
        protected String terminwunschBesichtigung;
        @XmlElement(name = "BeschreibungBesichtigung")
        protected String beschreibungBesichtigung;

        /**
         * Ruft den Wert der schadenBesichtigt-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSchadenBesichtigt() {
            return schadenBesichtigt;
        }

        /**
         * Legt den Wert der schadenBesichtigt-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSchadenBesichtigt(String value) {
            this.schadenBesichtigt = value;
        }

        /**
         * Ruft den Wert der besichtigungDurch-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Schadenmanagement.Besichtigung.BesichtigungDurch }
         *     
         */
        public Schadenmanagement.Besichtigung.BesichtigungDurch getBesichtigungDurch() {
            return besichtigungDurch;
        }

        /**
         * Legt den Wert der besichtigungDurch-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Schadenmanagement.Besichtigung.BesichtigungDurch }
         *     
         */
        public void setBesichtigungDurch(Schadenmanagement.Besichtigung.BesichtigungDurch value) {
            this.besichtigungDurch = value;
        }

        /**
         * Ruft den Wert der besichtigungsInfo-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBesichtigungsInfo() {
            return besichtigungsInfo;
        }

        /**
         * Legt den Wert der besichtigungsInfo-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBesichtigungsInfo(String value) {
            this.besichtigungsInfo = value;
        }

        /**
         * Ruft den Wert der besichtigungBei-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Schadenmanagement.Besichtigung.BesichtigungBei }
         *     
         */
        public Schadenmanagement.Besichtigung.BesichtigungBei getBesichtigungBei() {
            return besichtigungBei;
        }

        /**
         * Legt den Wert der besichtigungBei-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Schadenmanagement.Besichtigung.BesichtigungBei }
         *     
         */
        public void setBesichtigungBei(Schadenmanagement.Besichtigung.BesichtigungBei value) {
            this.besichtigungBei = value;
        }

        /**
         * Ruft den Wert der terminwunschBesichtigung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTerminwunschBesichtigung() {
            return terminwunschBesichtigung;
        }

        /**
         * Legt den Wert der terminwunschBesichtigung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTerminwunschBesichtigung(String value) {
            this.terminwunschBesichtigung = value;
        }

        /**
         * Ruft den Wert der beschreibungBesichtigung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBeschreibungBesichtigung() {
            return beschreibungBesichtigung;
        }

        /**
         * Legt den Wert der beschreibungBesichtigung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBeschreibungBesichtigung(String value) {
            this.beschreibungBesichtigung = value;
        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
         *         &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "adressKennzeichen",
            "partnerReferenz"
        })
        public static class BesichtigungBei {

            @XmlElement(name = "AdressKennzeichen")
            protected String adressKennzeichen;
            @XmlElement(name = "PartnerReferenz")
            protected String partnerReferenz;

            /**
             * Ruft den Wert der adressKennzeichen-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAdressKennzeichen() {
                return adressKennzeichen;
            }

            /**
             * Legt den Wert der adressKennzeichen-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAdressKennzeichen(String value) {
                this.adressKennzeichen = value;
            }

            /**
             * Ruft den Wert der partnerReferenz-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPartnerReferenz() {
                return partnerReferenz;
            }

            /**
             * Legt den Wert der partnerReferenz-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPartnerReferenz(String value) {
                this.partnerReferenz = value;
            }

        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
         *         &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "adressKennzeichen",
            "partnerReferenz"
        })
        public static class BesichtigungDurch {

            @XmlElement(name = "AdressKennzeichen")
            protected String adressKennzeichen;
            @XmlElement(name = "PartnerReferenz")
            protected String partnerReferenz;

            /**
             * Ruft den Wert der adressKennzeichen-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAdressKennzeichen() {
                return adressKennzeichen;
            }

            /**
             * Legt den Wert der adressKennzeichen-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAdressKennzeichen(String value) {
                this.adressKennzeichen = value;
            }

            /**
             * Ruft den Wert der partnerReferenz-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPartnerReferenz() {
                return partnerReferenz;
            }

            /**
             * Legt den Wert der partnerReferenz-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPartnerReferenz(String value) {
                this.partnerReferenz = value;
            }

        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
     *         &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "adressKennzeichen",
        "partnerReferenz"
    })
    public static class RegulierungAn {

        @XmlElement(name = "AdressKennzeichen")
        protected String adressKennzeichen;
        @XmlElement(name = "PartnerReferenz")
        protected String partnerReferenz;

        /**
         * Ruft den Wert der adressKennzeichen-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAdressKennzeichen() {
            return adressKennzeichen;
        }

        /**
         * Legt den Wert der adressKennzeichen-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAdressKennzeichen(String value) {
            this.adressKennzeichen = value;
        }

        /**
         * Ruft den Wert der partnerReferenz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPartnerReferenz() {
            return partnerReferenz;
        }

        /**
         * Legt den Wert der partnerReferenz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPartnerReferenz(String value) {
            this.partnerReferenz = value;
        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
     *       &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Vereinbarungen
        extends SatzendeTyp
    {


    }

}
