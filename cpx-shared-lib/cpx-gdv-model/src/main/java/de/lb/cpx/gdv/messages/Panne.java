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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="Notrufdatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Notrufuhrzeit" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *               &lt;minLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZeitstempelDatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZeitstempelUhrzeit" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *               &lt;minLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZeitstempelReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ZeitreferenzTyp" minOccurs="0"/>
 *         &lt;element name="Bearbeiter" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NrZustaendigenVUAssisteur" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VU-NummernTyp" minOccurs="0"/>
 *         &lt;element name="NrAuftragsAssisteursOderVu" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="5"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="WKZ" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WKZTyp" minOccurs="0"/>
 *         &lt;element name="Fahrzeugkategorie" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Zuordnungsmerkmal" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ZuordmerkmalTyp" minOccurs="0"/>
 *         &lt;element name="Schadenursache" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="erwuenschteMassnahmen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}MassnahmeTyp" minOccurs="0"/>
 *         &lt;element name="Umweltschaden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="VeranlassteMassnahmen" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="OrtPannenereignisses" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="5"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SaeulenstandortKmangabe" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="999.9"/>
 *                         &lt;totalDigits value="4"/>
 *                         &lt;fractionDigits value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Fahrtrichtung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}FahrtrichtungTyp" minOccurs="0"/>
 *         &lt;element name="FahrtrichtungText" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SaeulenstandortNrSaeule" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;totalDigits value="25"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Wgs84KoordinateOstwert" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;totalDigits value="10"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Wgs84KoordinateNordwert" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;totalDigits value="10"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="KoordErmittlung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KoordErmittlungsArtTyp" minOccurs="0"/>
 *         &lt;element name="StandortFahrzeugs1" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="StandortFahrzeugs2" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4510">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="4"/>
 *             &lt;minLength value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Versionsnummer" fixed="003">
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
    "notrufdatum",
    "notrufuhrzeit",
    "zeitstempelDatum",
    "zeitstempelUhrzeit",
    "zeitstempelReferenz",
    "bearbeiter",
    "nrZustaendigenVUAssisteur",
    "nrAuftragsAssisteursOderVu",
    "wkz",
    "fahrzeugkategorie",
    "zuordnungsmerkmal",
    "schadenursache",
    "erwuenschteMassnahmen",
    "umweltschaden",
    "veranlassteMassnahmen",
    "ortPannenereignisses",
    "saeulenstandortKmangabe",
    "fahrtrichtung",
    "fahrtrichtungText",
    "saeulenstandortNrSaeule",
    "wgs84KoordinateOstwert",
    "wgs84KoordinateNordwert",
    "koordErmittlung",
    "standortFahrzeugs1",
    "standortFahrzeugs2"
})
@XmlRootElement(name = "Panne")
public class Panne {

    @XmlElement(name = "Notrufdatum")
    protected String notrufdatum;
    @XmlElement(name = "Notrufuhrzeit")
    protected String notrufuhrzeit;
    @XmlElement(name = "ZeitstempelDatum")
    protected String zeitstempelDatum;
    @XmlElement(name = "ZeitstempelUhrzeit")
    protected String zeitstempelUhrzeit;
    @XmlElement(name = "ZeitstempelReferenz")
    @XmlSchemaType(name = "string")
    protected ZeitreferenzTyp zeitstempelReferenz;
    @XmlElement(name = "Bearbeiter")
    protected String bearbeiter;
    @XmlElement(name = "NrZustaendigenVUAssisteur")
    protected String nrZustaendigenVUAssisteur;
    @XmlElement(name = "NrAuftragsAssisteursOderVu")
    protected String nrAuftragsAssisteursOderVu;
    @XmlElement(name = "WKZ")
    protected String wkz;
    @XmlElement(name = "Fahrzeugkategorie")
    protected String fahrzeugkategorie;
    @XmlElement(name = "Zuordnungsmerkmal")
    protected String zuordnungsmerkmal;
    @XmlElement(name = "Schadenursache")
    protected String schadenursache;
    protected String erwuenschteMassnahmen;
    @XmlElement(name = "Umweltschaden")
    protected String umweltschaden;
    @XmlElement(name = "VeranlassteMassnahmen")
    protected Panne.VeranlassteMassnahmen veranlassteMassnahmen;
    @XmlElement(name = "OrtPannenereignisses")
    protected String ortPannenereignisses;
    @XmlElement(name = "SaeulenstandortKmangabe")
    protected Panne.SaeulenstandortKmangabe saeulenstandortKmangabe;
    @XmlElement(name = "Fahrtrichtung")
    protected String fahrtrichtung;
    @XmlElement(name = "FahrtrichtungText")
    protected String fahrtrichtungText;
    @XmlElement(name = "SaeulenstandortNrSaeule")
    protected Panne.SaeulenstandortNrSaeule saeulenstandortNrSaeule;
    @XmlElement(name = "Wgs84KoordinateOstwert")
    protected Panne.Wgs84KoordinateOstwert wgs84KoordinateOstwert;
    @XmlElement(name = "Wgs84KoordinateNordwert")
    protected Panne.Wgs84KoordinateNordwert wgs84KoordinateNordwert;
    @XmlElement(name = "KoordErmittlung")
    protected String koordErmittlung;
    @XmlElement(name = "StandortFahrzeugs1")
    protected Panne.StandortFahrzeugs1 standortFahrzeugs1;
    @XmlElement(name = "StandortFahrzeugs2")
    protected Panne.StandortFahrzeugs2 standortFahrzeugs2;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der notrufdatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotrufdatum() {
        return notrufdatum;
    }

    /**
     * Legt den Wert der notrufdatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotrufdatum(String value) {
        this.notrufdatum = value;
    }

    /**
     * Ruft den Wert der notrufuhrzeit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotrufuhrzeit() {
        return notrufuhrzeit;
    }

    /**
     * Legt den Wert der notrufuhrzeit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotrufuhrzeit(String value) {
        this.notrufuhrzeit = value;
    }

    /**
     * Ruft den Wert der zeitstempelDatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZeitstempelDatum() {
        return zeitstempelDatum;
    }

    /**
     * Legt den Wert der zeitstempelDatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZeitstempelDatum(String value) {
        this.zeitstempelDatum = value;
    }

    /**
     * Ruft den Wert der zeitstempelUhrzeit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZeitstempelUhrzeit() {
        return zeitstempelUhrzeit;
    }

    /**
     * Legt den Wert der zeitstempelUhrzeit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZeitstempelUhrzeit(String value) {
        this.zeitstempelUhrzeit = value;
    }

    /**
     * Ruft den Wert der zeitstempelReferenz-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ZeitreferenzTyp }
     *     
     */
    public ZeitreferenzTyp getZeitstempelReferenz() {
        return zeitstempelReferenz;
    }

    /**
     * Legt den Wert der zeitstempelReferenz-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ZeitreferenzTyp }
     *     
     */
    public void setZeitstempelReferenz(ZeitreferenzTyp value) {
        this.zeitstempelReferenz = value;
    }

    /**
     * Ruft den Wert der bearbeiter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBearbeiter() {
        return bearbeiter;
    }

    /**
     * Legt den Wert der bearbeiter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBearbeiter(String value) {
        this.bearbeiter = value;
    }

    /**
     * Ruft den Wert der nrZustaendigenVUAssisteur-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNrZustaendigenVUAssisteur() {
        return nrZustaendigenVUAssisteur;
    }

    /**
     * Legt den Wert der nrZustaendigenVUAssisteur-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNrZustaendigenVUAssisteur(String value) {
        this.nrZustaendigenVUAssisteur = value;
    }

    /**
     * Ruft den Wert der nrAuftragsAssisteursOderVu-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNrAuftragsAssisteursOderVu() {
        return nrAuftragsAssisteursOderVu;
    }

    /**
     * Legt den Wert der nrAuftragsAssisteursOderVu-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNrAuftragsAssisteursOderVu(String value) {
        this.nrAuftragsAssisteursOderVu = value;
    }

    /**
     * Ruft den Wert der wkz-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWKZ() {
        return wkz;
    }

    /**
     * Legt den Wert der wkz-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWKZ(String value) {
        this.wkz = value;
    }

    /**
     * Ruft den Wert der fahrzeugkategorie-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFahrzeugkategorie() {
        return fahrzeugkategorie;
    }

    /**
     * Legt den Wert der fahrzeugkategorie-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFahrzeugkategorie(String value) {
        this.fahrzeugkategorie = value;
    }

    /**
     * Ruft den Wert der zuordnungsmerkmal-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZuordnungsmerkmal() {
        return zuordnungsmerkmal;
    }

    /**
     * Legt den Wert der zuordnungsmerkmal-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZuordnungsmerkmal(String value) {
        this.zuordnungsmerkmal = value;
    }

    /**
     * Ruft den Wert der schadenursache-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenursache() {
        return schadenursache;
    }

    /**
     * Legt den Wert der schadenursache-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenursache(String value) {
        this.schadenursache = value;
    }

    /**
     * Ruft den Wert der erwuenschteMassnahmen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErwuenschteMassnahmen() {
        return erwuenschteMassnahmen;
    }

    /**
     * Legt den Wert der erwuenschteMassnahmen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErwuenschteMassnahmen(String value) {
        this.erwuenschteMassnahmen = value;
    }

    /**
     * Ruft den Wert der umweltschaden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUmweltschaden() {
        return umweltschaden;
    }

    /**
     * Legt den Wert der umweltschaden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUmweltschaden(String value) {
        this.umweltschaden = value;
    }

    /**
     * Ruft den Wert der veranlassteMassnahmen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Panne.VeranlassteMassnahmen }
     *     
     */
    public Panne.VeranlassteMassnahmen getVeranlassteMassnahmen() {
        return veranlassteMassnahmen;
    }

    /**
     * Legt den Wert der veranlassteMassnahmen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Panne.VeranlassteMassnahmen }
     *     
     */
    public void setVeranlassteMassnahmen(Panne.VeranlassteMassnahmen value) {
        this.veranlassteMassnahmen = value;
    }

    /**
     * Ruft den Wert der ortPannenereignisses-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrtPannenereignisses() {
        return ortPannenereignisses;
    }

    /**
     * Legt den Wert der ortPannenereignisses-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrtPannenereignisses(String value) {
        this.ortPannenereignisses = value;
    }

    /**
     * Ruft den Wert der saeulenstandortKmangabe-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Panne.SaeulenstandortKmangabe }
     *     
     */
    public Panne.SaeulenstandortKmangabe getSaeulenstandortKmangabe() {
        return saeulenstandortKmangabe;
    }

    /**
     * Legt den Wert der saeulenstandortKmangabe-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Panne.SaeulenstandortKmangabe }
     *     
     */
    public void setSaeulenstandortKmangabe(Panne.SaeulenstandortKmangabe value) {
        this.saeulenstandortKmangabe = value;
    }

    /**
     * Ruft den Wert der fahrtrichtung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFahrtrichtung() {
        return fahrtrichtung;
    }

    /**
     * Legt den Wert der fahrtrichtung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFahrtrichtung(String value) {
        this.fahrtrichtung = value;
    }

    /**
     * Ruft den Wert der fahrtrichtungText-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFahrtrichtungText() {
        return fahrtrichtungText;
    }

    /**
     * Legt den Wert der fahrtrichtungText-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFahrtrichtungText(String value) {
        this.fahrtrichtungText = value;
    }

    /**
     * Ruft den Wert der saeulenstandortNrSaeule-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Panne.SaeulenstandortNrSaeule }
     *     
     */
    public Panne.SaeulenstandortNrSaeule getSaeulenstandortNrSaeule() {
        return saeulenstandortNrSaeule;
    }

    /**
     * Legt den Wert der saeulenstandortNrSaeule-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Panne.SaeulenstandortNrSaeule }
     *     
     */
    public void setSaeulenstandortNrSaeule(Panne.SaeulenstandortNrSaeule value) {
        this.saeulenstandortNrSaeule = value;
    }

    /**
     * Ruft den Wert der wgs84KoordinateOstwert-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Panne.Wgs84KoordinateOstwert }
     *     
     */
    public Panne.Wgs84KoordinateOstwert getWgs84KoordinateOstwert() {
        return wgs84KoordinateOstwert;
    }

    /**
     * Legt den Wert der wgs84KoordinateOstwert-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Panne.Wgs84KoordinateOstwert }
     *     
     */
    public void setWgs84KoordinateOstwert(Panne.Wgs84KoordinateOstwert value) {
        this.wgs84KoordinateOstwert = value;
    }

    /**
     * Ruft den Wert der wgs84KoordinateNordwert-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Panne.Wgs84KoordinateNordwert }
     *     
     */
    public Panne.Wgs84KoordinateNordwert getWgs84KoordinateNordwert() {
        return wgs84KoordinateNordwert;
    }

    /**
     * Legt den Wert der wgs84KoordinateNordwert-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Panne.Wgs84KoordinateNordwert }
     *     
     */
    public void setWgs84KoordinateNordwert(Panne.Wgs84KoordinateNordwert value) {
        this.wgs84KoordinateNordwert = value;
    }

    /**
     * Ruft den Wert der koordErmittlung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKoordErmittlung() {
        return koordErmittlung;
    }

    /**
     * Legt den Wert der koordErmittlung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKoordErmittlung(String value) {
        this.koordErmittlung = value;
    }

    /**
     * Ruft den Wert der standortFahrzeugs1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Panne.StandortFahrzeugs1 }
     *     
     */
    public Panne.StandortFahrzeugs1 getStandortFahrzeugs1() {
        return standortFahrzeugs1;
    }

    /**
     * Legt den Wert der standortFahrzeugs1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Panne.StandortFahrzeugs1 }
     *     
     */
    public void setStandortFahrzeugs1(Panne.StandortFahrzeugs1 value) {
        this.standortFahrzeugs1 = value;
    }

    /**
     * Ruft den Wert der standortFahrzeugs2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Panne.StandortFahrzeugs2 }
     *     
     */
    public Panne.StandortFahrzeugs2 getStandortFahrzeugs2() {
        return standortFahrzeugs2;
    }

    /**
     * Legt den Wert der standortFahrzeugs2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Panne.StandortFahrzeugs2 }
     *     
     */
    public void setStandortFahrzeugs2(Panne.StandortFahrzeugs2 value) {
        this.standortFahrzeugs2 = value;
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
            return "4510";
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
            return "003";
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
     *         &lt;element name="Wert" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="999.9"/>
     *               &lt;totalDigits value="4"/>
     *               &lt;fractionDigits value="1"/>
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
    public static class SaeulenstandortKmangabe {

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
     *               &lt;totalDigits value="25"/>
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
    public static class SaeulenstandortNrSaeule {

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
    public static class StandortFahrzeugs1
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
    public static class StandortFahrzeugs2
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
    public static class VeranlassteMassnahmen
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
     *         &lt;element name="Wert" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               &lt;minInclusive value="0"/>
     *               &lt;totalDigits value="10"/>
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
    public static class Wgs84KoordinateNordwert {

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
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               &lt;minInclusive value="0"/>
     *               &lt;totalDigits value="10"/>
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
    public static class Wgs84KoordinateOstwert {

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

}
