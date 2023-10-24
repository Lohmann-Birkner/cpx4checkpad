//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.19 um 11:55:14 AM CEST 
//


package de.lb.cpx.gdv.messages;

import java.math.BigDecimal;
import java.math.BigInteger;
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
 *         &lt;element name="IdentifikationPartner">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Adresskennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp"/>
 *                   &lt;element name="Partnerreferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Totalschaden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinVermutlichTyp" minOccurs="0"/>
 *         &lt;element name="WaehrungsschluesselSchaden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
 *         &lt;element name="Schadenhoehe" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="9999999999.99"/>
 *                         &lt;totalDigits value="12"/>
 *                         &lt;fractionDigits value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Schadenkalkulation" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SchadenkalkulationTyp" minOccurs="0"/>
 *         &lt;element name="Mietwagen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}MietwagenstatusTyp" minOccurs="0"/>
 *         &lt;element name="WaehrungsschluesselMw" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
 *         &lt;element name="Mietwagenkosten" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="9999999999.99"/>
 *                         &lt;totalDigits value="12"/>
 *                         &lt;fractionDigits value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Reparaturdauer" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;totalDigits value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Besichtigung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Besichtigungsort" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
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
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Besichtigungstag" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Besichtigungszeit" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="15"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Anstossbereich" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="vornMitte" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="vornRechts" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="rechtsMitte" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="hintenRechts" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="hintenMitte" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="hintenLinks" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="linksMitte" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="vornLinks" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="DachBoden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}DachBodenTyp" minOccurs="0"/>
 *                   &lt;element name="rundum" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="BeschaedigteScheibe" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BeschaedigteScheibeTyp" minOccurs="0"/>
 *         &lt;element name="AnzReparaturenFrontscheibe" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="0"/>
 *               &lt;totalDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Feuerschaden" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4500">
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
    "identifikationPartner",
    "totalschaden",
    "waehrungsschluesselSchaden",
    "schadenhoehe",
    "schadenkalkulation",
    "mietwagen",
    "waehrungsschluesselMw",
    "mietwagenkosten",
    "reparaturdauer",
    "besichtigung",
    "besichtigungsort",
    "besichtigungstag",
    "besichtigungszeit",
    "anstossbereich",
    "beschaedigteScheibe",
    "anzReparaturenFrontscheibe",
    "feuerschaden"
})
@XmlRootElement(name = "BeschaedigungenFahrzeug")
public class BeschaedigungenFahrzeug {

    @XmlElement(name = "IdentifikationPartner", required = true)
    protected BeschaedigungenFahrzeug.IdentifikationPartner identifikationPartner;
    @XmlElement(name = "Totalschaden")
    protected String totalschaden;
    @XmlElement(name = "WaehrungsschluesselSchaden")
    protected String waehrungsschluesselSchaden;
    @XmlElement(name = "Schadenhoehe")
    protected BeschaedigungenFahrzeug.Schadenhoehe schadenhoehe;
    @XmlElement(name = "Schadenkalkulation")
    protected String schadenkalkulation;
    @XmlElement(name = "Mietwagen")
    protected String mietwagen;
    @XmlElement(name = "WaehrungsschluesselMw")
    protected String waehrungsschluesselMw;
    @XmlElement(name = "Mietwagenkosten")
    protected BeschaedigungenFahrzeug.Mietwagenkosten mietwagenkosten;
    @XmlElement(name = "Reparaturdauer")
    protected BeschaedigungenFahrzeug.Reparaturdauer reparaturdauer;
    @XmlElement(name = "Besichtigung")
    protected String besichtigung;
    @XmlElement(name = "Besichtigungsort")
    protected BeschaedigungenFahrzeug.Besichtigungsort besichtigungsort;
    @XmlElement(name = "Besichtigungstag")
    protected String besichtigungstag;
    @XmlElement(name = "Besichtigungszeit")
    protected String besichtigungszeit;
    @XmlElement(name = "Anstossbereich")
    protected BeschaedigungenFahrzeug.Anstossbereich anstossbereich;
    @XmlElement(name = "BeschaedigteScheibe")
    protected String beschaedigteScheibe;
    @XmlElement(name = "AnzReparaturenFrontscheibe")
    protected BigInteger anzReparaturenFrontscheibe;
    @XmlElement(name = "Feuerschaden")
    protected BeschaedigungenFahrzeug.Feuerschaden feuerschaden;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der identifikationPartner-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigungenFahrzeug.IdentifikationPartner }
     *     
     */
    public BeschaedigungenFahrzeug.IdentifikationPartner getIdentifikationPartner() {
        return identifikationPartner;
    }

    /**
     * Legt den Wert der identifikationPartner-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigungenFahrzeug.IdentifikationPartner }
     *     
     */
    public void setIdentifikationPartner(BeschaedigungenFahrzeug.IdentifikationPartner value) {
        this.identifikationPartner = value;
    }

    /**
     * Ruft den Wert der totalschaden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalschaden() {
        return totalschaden;
    }

    /**
     * Legt den Wert der totalschaden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalschaden(String value) {
        this.totalschaden = value;
    }

    /**
     * Ruft den Wert der waehrungsschluesselSchaden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWaehrungsschluesselSchaden() {
        return waehrungsschluesselSchaden;
    }

    /**
     * Legt den Wert der waehrungsschluesselSchaden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWaehrungsschluesselSchaden(String value) {
        this.waehrungsschluesselSchaden = value;
    }

    /**
     * Ruft den Wert der schadenhoehe-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigungenFahrzeug.Schadenhoehe }
     *     
     */
    public BeschaedigungenFahrzeug.Schadenhoehe getSchadenhoehe() {
        return schadenhoehe;
    }

    /**
     * Legt den Wert der schadenhoehe-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigungenFahrzeug.Schadenhoehe }
     *     
     */
    public void setSchadenhoehe(BeschaedigungenFahrzeug.Schadenhoehe value) {
        this.schadenhoehe = value;
    }

    /**
     * Ruft den Wert der schadenkalkulation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenkalkulation() {
        return schadenkalkulation;
    }

    /**
     * Legt den Wert der schadenkalkulation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenkalkulation(String value) {
        this.schadenkalkulation = value;
    }

    /**
     * Ruft den Wert der mietwagen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMietwagen() {
        return mietwagen;
    }

    /**
     * Legt den Wert der mietwagen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMietwagen(String value) {
        this.mietwagen = value;
    }

    /**
     * Ruft den Wert der waehrungsschluesselMw-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWaehrungsschluesselMw() {
        return waehrungsschluesselMw;
    }

    /**
     * Legt den Wert der waehrungsschluesselMw-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWaehrungsschluesselMw(String value) {
        this.waehrungsschluesselMw = value;
    }

    /**
     * Ruft den Wert der mietwagenkosten-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigungenFahrzeug.Mietwagenkosten }
     *     
     */
    public BeschaedigungenFahrzeug.Mietwagenkosten getMietwagenkosten() {
        return mietwagenkosten;
    }

    /**
     * Legt den Wert der mietwagenkosten-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigungenFahrzeug.Mietwagenkosten }
     *     
     */
    public void setMietwagenkosten(BeschaedigungenFahrzeug.Mietwagenkosten value) {
        this.mietwagenkosten = value;
    }

    /**
     * Ruft den Wert der reparaturdauer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigungenFahrzeug.Reparaturdauer }
     *     
     */
    public BeschaedigungenFahrzeug.Reparaturdauer getReparaturdauer() {
        return reparaturdauer;
    }

    /**
     * Legt den Wert der reparaturdauer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigungenFahrzeug.Reparaturdauer }
     *     
     */
    public void setReparaturdauer(BeschaedigungenFahrzeug.Reparaturdauer value) {
        this.reparaturdauer = value;
    }

    /**
     * Ruft den Wert der besichtigung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBesichtigung() {
        return besichtigung;
    }

    /**
     * Legt den Wert der besichtigung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBesichtigung(String value) {
        this.besichtigung = value;
    }

    /**
     * Ruft den Wert der besichtigungsort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigungenFahrzeug.Besichtigungsort }
     *     
     */
    public BeschaedigungenFahrzeug.Besichtigungsort getBesichtigungsort() {
        return besichtigungsort;
    }

    /**
     * Legt den Wert der besichtigungsort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigungenFahrzeug.Besichtigungsort }
     *     
     */
    public void setBesichtigungsort(BeschaedigungenFahrzeug.Besichtigungsort value) {
        this.besichtigungsort = value;
    }

    /**
     * Ruft den Wert der besichtigungstag-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBesichtigungstag() {
        return besichtigungstag;
    }

    /**
     * Legt den Wert der besichtigungstag-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBesichtigungstag(String value) {
        this.besichtigungstag = value;
    }

    /**
     * Ruft den Wert der besichtigungszeit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBesichtigungszeit() {
        return besichtigungszeit;
    }

    /**
     * Legt den Wert der besichtigungszeit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBesichtigungszeit(String value) {
        this.besichtigungszeit = value;
    }

    /**
     * Ruft den Wert der anstossbereich-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigungenFahrzeug.Anstossbereich }
     *     
     */
    public BeschaedigungenFahrzeug.Anstossbereich getAnstossbereich() {
        return anstossbereich;
    }

    /**
     * Legt den Wert der anstossbereich-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigungenFahrzeug.Anstossbereich }
     *     
     */
    public void setAnstossbereich(BeschaedigungenFahrzeug.Anstossbereich value) {
        this.anstossbereich = value;
    }

    /**
     * Ruft den Wert der beschaedigteScheibe-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeschaedigteScheibe() {
        return beschaedigteScheibe;
    }

    /**
     * Legt den Wert der beschaedigteScheibe-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeschaedigteScheibe(String value) {
        this.beschaedigteScheibe = value;
    }

    /**
     * Ruft den Wert der anzReparaturenFrontscheibe-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnzReparaturenFrontscheibe() {
        return anzReparaturenFrontscheibe;
    }

    /**
     * Legt den Wert der anzReparaturenFrontscheibe-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnzReparaturenFrontscheibe(BigInteger value) {
        this.anzReparaturenFrontscheibe = value;
    }

    /**
     * Ruft den Wert der feuerschaden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigungenFahrzeug.Feuerschaden }
     *     
     */
    public BeschaedigungenFahrzeug.Feuerschaden getFeuerschaden() {
        return feuerschaden;
    }

    /**
     * Legt den Wert der feuerschaden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigungenFahrzeug.Feuerschaden }
     *     
     */
    public void setFeuerschaden(BeschaedigungenFahrzeug.Feuerschaden value) {
        this.feuerschaden = value;
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
            return "4500";
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
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="vornMitte" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="vornRechts" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="rechtsMitte" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="hintenRechts" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="hintenMitte" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="hintenLinks" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="linksMitte" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="vornLinks" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="DachBoden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}DachBodenTyp" minOccurs="0"/>
     *         &lt;element name="rundum" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
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
        "vornMitte",
        "vornRechts",
        "rechtsMitte",
        "hintenRechts",
        "hintenMitte",
        "hintenLinks",
        "linksMitte",
        "vornLinks",
        "dachBoden",
        "rundum"
    })
    public static class Anstossbereich {

        protected String vornMitte;
        protected String vornRechts;
        protected String rechtsMitte;
        protected String hintenRechts;
        protected String hintenMitte;
        protected String hintenLinks;
        protected String linksMitte;
        protected String vornLinks;
        @XmlElement(name = "DachBoden")
        protected String dachBoden;
        protected String rundum;

        /**
         * Ruft den Wert der vornMitte-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVornMitte() {
            return vornMitte;
        }

        /**
         * Legt den Wert der vornMitte-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVornMitte(String value) {
            this.vornMitte = value;
        }

        /**
         * Ruft den Wert der vornRechts-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVornRechts() {
            return vornRechts;
        }

        /**
         * Legt den Wert der vornRechts-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVornRechts(String value) {
            this.vornRechts = value;
        }

        /**
         * Ruft den Wert der rechtsMitte-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRechtsMitte() {
            return rechtsMitte;
        }

        /**
         * Legt den Wert der rechtsMitte-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRechtsMitte(String value) {
            this.rechtsMitte = value;
        }

        /**
         * Ruft den Wert der hintenRechts-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHintenRechts() {
            return hintenRechts;
        }

        /**
         * Legt den Wert der hintenRechts-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHintenRechts(String value) {
            this.hintenRechts = value;
        }

        /**
         * Ruft den Wert der hintenMitte-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHintenMitte() {
            return hintenMitte;
        }

        /**
         * Legt den Wert der hintenMitte-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHintenMitte(String value) {
            this.hintenMitte = value;
        }

        /**
         * Ruft den Wert der hintenLinks-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHintenLinks() {
            return hintenLinks;
        }

        /**
         * Legt den Wert der hintenLinks-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHintenLinks(String value) {
            this.hintenLinks = value;
        }

        /**
         * Ruft den Wert der linksMitte-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLinksMitte() {
            return linksMitte;
        }

        /**
         * Legt den Wert der linksMitte-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLinksMitte(String value) {
            this.linksMitte = value;
        }

        /**
         * Ruft den Wert der vornLinks-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVornLinks() {
            return vornLinks;
        }

        /**
         * Legt den Wert der vornLinks-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVornLinks(String value) {
            this.vornLinks = value;
        }

        /**
         * Ruft den Wert der dachBoden-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDachBoden() {
            return dachBoden;
        }

        /**
         * Legt den Wert der dachBoden-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDachBoden(String value) {
            this.dachBoden = value;
        }

        /**
         * Ruft den Wert der rundum-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRundum() {
            return rundum;
        }

        /**
         * Legt den Wert der rundum-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRundum(String value) {
            this.rundum = value;
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
        "lkz",
        "plz",
        "ort",
        "strasse"
    })
    public static class Besichtigungsort {

        @XmlElement(name = "LKZ")
        protected String lkz;
        @XmlElement(name = "PLZ")
        protected String plz;
        @XmlElement(name = "Ort")
        protected String ort;
        @XmlElement(name = "Strasse")
        protected String strasse;

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
    public static class Feuerschaden {

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
     *         &lt;element name="Partnerreferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
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
        protected String partnerreferenz;

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
         *     {@link String }
         *     
         */
        public String getPartnerreferenz() {
            return partnerreferenz;
        }

        /**
         * Legt den Wert der partnerreferenz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPartnerreferenz(String value) {
            this.partnerreferenz = value;
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
     *         &lt;element name="Wert" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="9999999999.99"/>
     *               &lt;totalDigits value="12"/>
     *               &lt;fractionDigits value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
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
        "wert",
        "indikator"
    })
    public static class Mietwagenkosten {

        @XmlElement(name = "Wert")
        protected BigDecimal wert;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der wert-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getWert() {
            return wert;
        }

        /**
         * Legt den Wert der wert-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setWert(BigDecimal value) {
            this.wert = value;
        }

        /**
         * Ruft den Wert der indikator-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndikator(String value) {
            this.indikator = value;
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
     *         &lt;element name="Wert" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               &lt;minInclusive value="0"/>
     *               &lt;totalDigits value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
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
        "wert",
        "indikator"
    })
    public static class Reparaturdauer {

        @XmlElement(name = "Wert")
        protected BigInteger wert;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der wert-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getWert() {
            return wert;
        }

        /**
         * Legt den Wert der wert-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setWert(BigInteger value) {
            this.wert = value;
        }

        /**
         * Ruft den Wert der indikator-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndikator(String value) {
            this.indikator = value;
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
     *         &lt;element name="Wert" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="9999999999.99"/>
     *               &lt;totalDigits value="12"/>
     *               &lt;fractionDigits value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
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
        "wert",
        "indikator"
    })
    public static class Schadenhoehe {

        @XmlElement(name = "Wert")
        protected BigDecimal wert;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der wert-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getWert() {
            return wert;
        }

        /**
         * Legt den Wert der wert-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setWert(BigDecimal value) {
            this.wert = value;
        }

        /**
         * Ruft den Wert der indikator-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndikator(String value) {
            this.indikator = value;
        }

    }

}
