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
 *         &lt;element name="Adresse" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Anredeschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AnredeschluesselTyp" minOccurs="0"/>
 *                   &lt;element name="Name1" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Name2" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Name3" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Titel" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="20"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="LKZ" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LaenderkennzeichenTyp" minOccurs="0"/>
 *                   &lt;element name="PLZ" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="6"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Ort" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="25"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Strasse" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Postfach" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="8"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="IdentifikationPartner">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Adresskennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp"/>
 *                   &lt;element name="Partnerreferenz" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>PartnerReferenzTyp">
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
 *         &lt;element name="Personendaten" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Geburtsdatum" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="8"/>
 *                         &lt;minLength value="8"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Staatsangehoerigkeit" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LaenderkennzeichenTyp" minOccurs="0"/>
 *                   &lt;element name="Geschlecht" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}GeschlechtTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Zahlungsdaten" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Konto-Nr" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="12"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="BLZ" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="8"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Konto-Inhaber" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Kommunikation" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Typ" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KommunikationstypTyp" minOccurs="0"/>
 *                   &lt;element name="Nummer" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="20"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="KOMM-TYP2" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KommunikationstypTyp" minOccurs="0"/>
 *                   &lt;element name="KOMM-NR2" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="20"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="KOMM-TYP3" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KommunikationstypTyp" minOccurs="0"/>
 *                   &lt;element name="KOMM-NR3" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="20"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="KOMM-TYP4" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KommunikationstypTyp" minOccurs="0"/>
 *                   &lt;element name="KOMM-NR4" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="20"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Zahlungsart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ZahlungsartTyp" minOccurs="0"/>
 *         &lt;element name="Familienstand" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}FamilienstandTyp" minOccurs="0"/>
 *         &lt;element name="Abteilung" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Partnerbemerkung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="IBAN" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="35"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BIC" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="11"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Email" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="60"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Str" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Hausnummer" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Email2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="60"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Vorsteuerabzug" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Handelsregister" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="35"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="HRB" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="UmsatzsteuerId" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="11"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Steuernummer" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SteuerIDTyp" minOccurs="0"/>
 *         &lt;element name="KinderVorhanden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Beruf" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4100">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="4"/>
 *             &lt;minLength value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Versionsnummer" fixed="004">
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
    "adresse",
    "identifikationPartner",
    "personendaten",
    "zahlungsdaten",
    "kommunikation",
    "zahlungsart",
    "familienstand",
    "abteilung",
    "partnerbemerkung",
    "iban",
    "bic",
    "email",
    "str",
    "hausnummer",
    "email2",
    "vorsteuerabzug",
    "handelsregister",
    "hrb",
    "umsatzsteuerId",
    "steuernummer",
    "kinderVorhanden",
    "beruf"
})
@XmlRootElement(name = "Partnerdaten")
public class Partnerdaten {

    @XmlElement(name = "Adresse")
    protected Partnerdaten.Adresse adresse;
    @XmlElement(name = "IdentifikationPartner", required = true)
    protected Partnerdaten.IdentifikationPartner identifikationPartner;
    @XmlElement(name = "Personendaten")
    protected Partnerdaten.Personendaten personendaten;
    @XmlElement(name = "Zahlungsdaten")
    protected Partnerdaten.Zahlungsdaten zahlungsdaten;
    @XmlElement(name = "Kommunikation")
    protected Partnerdaten.Kommunikation kommunikation;
    @XmlElement(name = "Zahlungsart")
    protected String zahlungsart;
    @XmlElement(name = "Familienstand")
    protected String familienstand;
    @XmlElement(name = "Abteilung")
    protected Partnerdaten.Abteilung abteilung;
    @XmlElement(name = "Partnerbemerkung")
    protected String partnerbemerkung;
    @XmlElement(name = "IBAN")
    protected String iban;
    @XmlElement(name = "BIC")
    protected String bic;
    @XmlElement(name = "Email")
    protected String email;
    @XmlElement(name = "Str")
    protected String str;
    @XmlElement(name = "Hausnummer")
    protected Partnerdaten.Hausnummer hausnummer;
    @XmlElement(name = "Email2")
    protected String email2;
    @XmlElement(name = "Vorsteuerabzug")
    protected String vorsteuerabzug;
    @XmlElement(name = "Handelsregister")
    protected String handelsregister;
    @XmlElement(name = "HRB")
    protected String hrb;
    @XmlElement(name = "UmsatzsteuerId")
    protected String umsatzsteuerId;
    @XmlElement(name = "Steuernummer")
    protected String steuernummer;
    @XmlElement(name = "KinderVorhanden")
    protected String kinderVorhanden;
    @XmlElement(name = "Beruf")
    protected Partnerdaten.Beruf beruf;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der adresse-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Partnerdaten.Adresse }
     *     
     */
    public Partnerdaten.Adresse getAdresse() {
        return adresse;
    }

    /**
     * Legt den Wert der adresse-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Partnerdaten.Adresse }
     *     
     */
    public void setAdresse(Partnerdaten.Adresse value) {
        this.adresse = value;
    }

    /**
     * Ruft den Wert der identifikationPartner-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Partnerdaten.IdentifikationPartner }
     *     
     */
    public Partnerdaten.IdentifikationPartner getIdentifikationPartner() {
        return identifikationPartner;
    }

    /**
     * Legt den Wert der identifikationPartner-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Partnerdaten.IdentifikationPartner }
     *     
     */
    public void setIdentifikationPartner(Partnerdaten.IdentifikationPartner value) {
        this.identifikationPartner = value;
    }

    /**
     * Ruft den Wert der personendaten-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Partnerdaten.Personendaten }
     *     
     */
    public Partnerdaten.Personendaten getPersonendaten() {
        return personendaten;
    }

    /**
     * Legt den Wert der personendaten-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Partnerdaten.Personendaten }
     *     
     */
    public void setPersonendaten(Partnerdaten.Personendaten value) {
        this.personendaten = value;
    }

    /**
     * Ruft den Wert der zahlungsdaten-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Partnerdaten.Zahlungsdaten }
     *     
     */
    public Partnerdaten.Zahlungsdaten getZahlungsdaten() {
        return zahlungsdaten;
    }

    /**
     * Legt den Wert der zahlungsdaten-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Partnerdaten.Zahlungsdaten }
     *     
     */
    public void setZahlungsdaten(Partnerdaten.Zahlungsdaten value) {
        this.zahlungsdaten = value;
    }

    /**
     * Ruft den Wert der kommunikation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Partnerdaten.Kommunikation }
     *     
     */
    public Partnerdaten.Kommunikation getKommunikation() {
        return kommunikation;
    }

    /**
     * Legt den Wert der kommunikation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Partnerdaten.Kommunikation }
     *     
     */
    public void setKommunikation(Partnerdaten.Kommunikation value) {
        this.kommunikation = value;
    }

    /**
     * Ruft den Wert der zahlungsart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZahlungsart() {
        return zahlungsart;
    }

    /**
     * Legt den Wert der zahlungsart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZahlungsart(String value) {
        this.zahlungsart = value;
    }

    /**
     * Ruft den Wert der familienstand-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFamilienstand() {
        return familienstand;
    }

    /**
     * Legt den Wert der familienstand-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFamilienstand(String value) {
        this.familienstand = value;
    }

    /**
     * Ruft den Wert der abteilung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Partnerdaten.Abteilung }
     *     
     */
    public Partnerdaten.Abteilung getAbteilung() {
        return abteilung;
    }

    /**
     * Legt den Wert der abteilung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Partnerdaten.Abteilung }
     *     
     */
    public void setAbteilung(Partnerdaten.Abteilung value) {
        this.abteilung = value;
    }

    /**
     * Ruft den Wert der partnerbemerkung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartnerbemerkung() {
        return partnerbemerkung;
    }

    /**
     * Legt den Wert der partnerbemerkung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartnerbemerkung(String value) {
        this.partnerbemerkung = value;
    }

    /**
     * Ruft den Wert der iban-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIBAN() {
        return iban;
    }

    /**
     * Legt den Wert der iban-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIBAN(String value) {
        this.iban = value;
    }

    /**
     * Ruft den Wert der bic-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBIC() {
        return bic;
    }

    /**
     * Legt den Wert der bic-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBIC(String value) {
        this.bic = value;
    }

    /**
     * Ruft den Wert der email-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Legt den Wert der email-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Ruft den Wert der str-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStr() {
        return str;
    }

    /**
     * Legt den Wert der str-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStr(String value) {
        this.str = value;
    }

    /**
     * Ruft den Wert der hausnummer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Partnerdaten.Hausnummer }
     *     
     */
    public Partnerdaten.Hausnummer getHausnummer() {
        return hausnummer;
    }

    /**
     * Legt den Wert der hausnummer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Partnerdaten.Hausnummer }
     *     
     */
    public void setHausnummer(Partnerdaten.Hausnummer value) {
        this.hausnummer = value;
    }

    /**
     * Ruft den Wert der email2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail2() {
        return email2;
    }

    /**
     * Legt den Wert der email2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail2(String value) {
        this.email2 = value;
    }

    /**
     * Ruft den Wert der vorsteuerabzug-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVorsteuerabzug() {
        return vorsteuerabzug;
    }

    /**
     * Legt den Wert der vorsteuerabzug-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVorsteuerabzug(String value) {
        this.vorsteuerabzug = value;
    }

    /**
     * Ruft den Wert der handelsregister-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHandelsregister() {
        return handelsregister;
    }

    /**
     * Legt den Wert der handelsregister-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHandelsregister(String value) {
        this.handelsregister = value;
    }

    /**
     * Ruft den Wert der hrb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHRB() {
        return hrb;
    }

    /**
     * Legt den Wert der hrb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHRB(String value) {
        this.hrb = value;
    }

    /**
     * Ruft den Wert der umsatzsteuerId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUmsatzsteuerId() {
        return umsatzsteuerId;
    }

    /**
     * Legt den Wert der umsatzsteuerId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUmsatzsteuerId(String value) {
        this.umsatzsteuerId = value;
    }

    /**
     * Ruft den Wert der steuernummer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSteuernummer() {
        return steuernummer;
    }

    /**
     * Legt den Wert der steuernummer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSteuernummer(String value) {
        this.steuernummer = value;
    }

    /**
     * Ruft den Wert der kinderVorhanden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKinderVorhanden() {
        return kinderVorhanden;
    }

    /**
     * Legt den Wert der kinderVorhanden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKinderVorhanden(String value) {
        this.kinderVorhanden = value;
    }

    /**
     * Ruft den Wert der beruf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Partnerdaten.Beruf }
     *     
     */
    public Partnerdaten.Beruf getBeruf() {
        return beruf;
    }

    /**
     * Legt den Wert der beruf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Partnerdaten.Beruf }
     *     
     */
    public void setBeruf(Partnerdaten.Beruf value) {
        this.beruf = value;
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
            return "4100";
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
            return "004";
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
    public static class Abteilung
        extends SatzendeTyp
    {


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
     *         &lt;element name="Anredeschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AnredeschluesselTyp" minOccurs="0"/>
     *         &lt;element name="Name1" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Name2" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Name3" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Titel" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="20"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="LKZ" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LaenderkennzeichenTyp" minOccurs="0"/>
     *         &lt;element name="PLZ" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="6"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Ort" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="25"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Strasse" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Postfach" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="8"/>
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
        "anredeschluessel",
        "name1",
        "name2",
        "name3",
        "titel",
        "lkz",
        "plz",
        "ort",
        "strasse",
        "postfach"
    })
    public static class Adresse {

        @XmlElement(name = "Anredeschluessel")
        protected String anredeschluessel;
        @XmlElement(name = "Name1")
        protected String name1;
        @XmlElement(name = "Name2")
        protected String name2;
        @XmlElement(name = "Name3")
        protected String name3;
        @XmlElement(name = "Titel")
        protected String titel;
        @XmlElement(name = "LKZ")
        protected String lkz;
        @XmlElement(name = "PLZ")
        protected String plz;
        @XmlElement(name = "Ort")
        protected String ort;
        @XmlElement(name = "Strasse")
        protected String strasse;
        @XmlElement(name = "Postfach")
        protected String postfach;

        /**
         * Ruft den Wert der anredeschluessel-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAnredeschluessel() {
            return anredeschluessel;
        }

        /**
         * Legt den Wert der anredeschluessel-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAnredeschluessel(String value) {
            this.anredeschluessel = value;
        }

        /**
         * Ruft den Wert der name1-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName1() {
            return name1;
        }

        /**
         * Legt den Wert der name1-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName1(String value) {
            this.name1 = value;
        }

        /**
         * Ruft den Wert der name2-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName2() {
            return name2;
        }

        /**
         * Legt den Wert der name2-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName2(String value) {
            this.name2 = value;
        }

        /**
         * Ruft den Wert der name3-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName3() {
            return name3;
        }

        /**
         * Legt den Wert der name3-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName3(String value) {
            this.name3 = value;
        }

        /**
         * Ruft den Wert der titel-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTitel() {
            return titel;
        }

        /**
         * Legt den Wert der titel-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTitel(String value) {
            this.titel = value;
        }

        /**
         * Ruft den Wert der lkz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLKZ() {
            return lkz;
        }

        /**
         * Legt den Wert der lkz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLKZ(String value) {
            this.lkz = value;
        }

        /**
         * Ruft den Wert der plz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPLZ() {
            return plz;
        }

        /**
         * Legt den Wert der plz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPLZ(String value) {
            this.plz = value;
        }

        /**
         * Ruft den Wert der ort-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOrt() {
            return ort;
        }

        /**
         * Legt den Wert der ort-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOrt(String value) {
            this.ort = value;
        }

        /**
         * Ruft den Wert der strasse-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStrasse() {
            return strasse;
        }

        /**
         * Legt den Wert der strasse-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStrasse(String value) {
            this.strasse = value;
        }

        /**
         * Ruft den Wert der postfach-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPostfach() {
            return postfach;
        }

        /**
         * Legt den Wert der postfach-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPostfach(String value) {
            this.postfach = value;
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
    public static class Beruf
        extends SatzendeTyp
    {


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
    public static class Hausnummer
        extends SatzendeTyp
    {


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
     *         &lt;element name="Adresskennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp"/>
     *         &lt;element name="Partnerreferenz" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>PartnerReferenzTyp">
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
        "adresskennzeichen",
        "partnerreferenz"
    })
    public static class IdentifikationPartner {

        @XmlElement(name = "Adresskennzeichen", required = true)
        protected String adresskennzeichen;
        @XmlElement(name = "Partnerreferenz")
        protected Partnerdaten.IdentifikationPartner.Partnerreferenz partnerreferenz;

        /**
         * Ruft den Wert der adresskennzeichen-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAdresskennzeichen() {
            return adresskennzeichen;
        }

        /**
         * Legt den Wert der adresskennzeichen-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAdresskennzeichen(String value) {
            this.adresskennzeichen = value;
        }

        /**
         * Ruft den Wert der partnerreferenz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Partnerdaten.IdentifikationPartner.Partnerreferenz }
         *     
         */
        public Partnerdaten.IdentifikationPartner.Partnerreferenz getPartnerreferenz() {
            return partnerreferenz;
        }

        /**
         * Legt den Wert der partnerreferenz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Partnerdaten.IdentifikationPartner.Partnerreferenz }
         *     
         */
        public void setPartnerreferenz(Partnerdaten.IdentifikationPartner.Partnerreferenz value) {
            this.partnerreferenz = value;
        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>PartnerReferenzTyp">
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
        public static class Partnerreferenz {

            @XmlValue
            protected String value;
            @XmlAttribute(name = "Satzende")
            protected String satzende;

            /**
             * In diesem Feld kann ein Merkmal (0 - 9, A - Z) eingetragen werden. Dadurch ist es möglich, mehrere Partnersätze mit gleichem Adress-Kennzeichen in der Nachricht zu übermitteln und aus anderen Sätzen eindeutig auf diesen Partnersatz hinzuweisen.Siehe dazu auch die Erläuterungen zum Partnersatz.
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
     *         &lt;element name="Typ" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KommunikationstypTyp" minOccurs="0"/>
     *         &lt;element name="Nummer" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="20"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="KOMM-TYP2" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KommunikationstypTyp" minOccurs="0"/>
     *         &lt;element name="KOMM-NR2" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="20"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="KOMM-TYP3" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KommunikationstypTyp" minOccurs="0"/>
     *         &lt;element name="KOMM-NR3" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="20"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="KOMM-TYP4" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KommunikationstypTyp" minOccurs="0"/>
     *         &lt;element name="KOMM-NR4" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="20"/>
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
        "typ",
        "nummer",
        "kommtyp2",
        "kommnr2",
        "kommtyp3",
        "kommnr3",
        "kommtyp4",
        "kommnr4"
    })
    public static class Kommunikation {

        @XmlElement(name = "Typ")
        protected String typ;
        @XmlElement(name = "Nummer")
        protected String nummer;
        @XmlElement(name = "KOMM-TYP2")
        protected String kommtyp2;
        @XmlElement(name = "KOMM-NR2")
        protected String kommnr2;
        @XmlElement(name = "KOMM-TYP3")
        protected String kommtyp3;
        @XmlElement(name = "KOMM-NR3")
        protected String kommnr3;
        @XmlElement(name = "KOMM-TYP4")
        protected String kommtyp4;
        @XmlElement(name = "KOMM-NR4")
        protected String kommnr4;

        /**
         * Ruft den Wert der typ-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTyp() {
            return typ;
        }

        /**
         * Legt den Wert der typ-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTyp(String value) {
            this.typ = value;
        }

        /**
         * Ruft den Wert der nummer-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNummer() {
            return nummer;
        }

        /**
         * Legt den Wert der nummer-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNummer(String value) {
            this.nummer = value;
        }

        /**
         * Ruft den Wert der kommtyp2-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKOMMTYP2() {
            return kommtyp2;
        }

        /**
         * Legt den Wert der kommtyp2-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKOMMTYP2(String value) {
            this.kommtyp2 = value;
        }

        /**
         * Ruft den Wert der kommnr2-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKOMMNR2() {
            return kommnr2;
        }

        /**
         * Legt den Wert der kommnr2-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKOMMNR2(String value) {
            this.kommnr2 = value;
        }

        /**
         * Ruft den Wert der kommtyp3-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKOMMTYP3() {
            return kommtyp3;
        }

        /**
         * Legt den Wert der kommtyp3-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKOMMTYP3(String value) {
            this.kommtyp3 = value;
        }

        /**
         * Ruft den Wert der kommnr3-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKOMMNR3() {
            return kommnr3;
        }

        /**
         * Legt den Wert der kommnr3-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKOMMNR3(String value) {
            this.kommnr3 = value;
        }

        /**
         * Ruft den Wert der kommtyp4-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKOMMTYP4() {
            return kommtyp4;
        }

        /**
         * Legt den Wert der kommtyp4-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKOMMTYP4(String value) {
            this.kommtyp4 = value;
        }

        /**
         * Ruft den Wert der kommnr4-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKOMMNR4() {
            return kommnr4;
        }

        /**
         * Legt den Wert der kommnr4-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKOMMNR4(String value) {
            this.kommnr4 = value;
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
     *         &lt;element name="Geburtsdatum" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="8"/>
     *               &lt;minLength value="8"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Staatsangehoerigkeit" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LaenderkennzeichenTyp" minOccurs="0"/>
     *         &lt;element name="Geschlecht" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}GeschlechtTyp" minOccurs="0"/>
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
        "geburtsdatum",
        "staatsangehoerigkeit",
        "geschlecht"
    })
    public static class Personendaten {

        @XmlElement(name = "Geburtsdatum")
        protected String geburtsdatum;
        @XmlElement(name = "Staatsangehoerigkeit")
        protected String staatsangehoerigkeit;
        @XmlElement(name = "Geschlecht")
        protected String geschlecht;

        /**
         * Ruft den Wert der geburtsdatum-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGeburtsdatum() {
            return geburtsdatum;
        }

        /**
         * Legt den Wert der geburtsdatum-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGeburtsdatum(String value) {
            this.geburtsdatum = value;
        }

        /**
         * Ruft den Wert der staatsangehoerigkeit-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStaatsangehoerigkeit() {
            return staatsangehoerigkeit;
        }

        /**
         * Legt den Wert der staatsangehoerigkeit-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStaatsangehoerigkeit(String value) {
            this.staatsangehoerigkeit = value;
        }

        /**
         * Ruft den Wert der geschlecht-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGeschlecht() {
            return geschlecht;
        }

        /**
         * Legt den Wert der geschlecht-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGeschlecht(String value) {
            this.geschlecht = value;
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
     *         &lt;element name="Konto-Nr" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="12"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="BLZ" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="8"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Konto-Inhaber" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
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
        "kontoNr",
        "blz",
        "kontoInhaber"
    })
    public static class Zahlungsdaten {

        @XmlElement(name = "Konto-Nr")
        protected String kontoNr;
        @XmlElement(name = "BLZ")
        protected String blz;
        @XmlElement(name = "Konto-Inhaber")
        protected String kontoInhaber;

        /**
         * Ruft den Wert der kontoNr-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKontoNr() {
            return kontoNr;
        }

        /**
         * Legt den Wert der kontoNr-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKontoNr(String value) {
            this.kontoNr = value;
        }

        /**
         * Ruft den Wert der blz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBLZ() {
            return blz;
        }

        /**
         * Legt den Wert der blz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBLZ(String value) {
            this.blz = value;
        }

        /**
         * Ruft den Wert der kontoInhaber-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKontoInhaber() {
            return kontoInhaber;
        }

        /**
         * Legt den Wert der kontoInhaber-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKontoInhaber(String value) {
            this.kontoInhaber = value;
        }

    }

}
