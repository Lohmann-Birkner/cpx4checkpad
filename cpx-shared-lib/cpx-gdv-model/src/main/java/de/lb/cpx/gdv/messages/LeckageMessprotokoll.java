//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.19 um 11:55:14 AM CEST 
//


package de.lb.cpx.gdv.messages;

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
 *         &lt;element name="AnzahlEinsaetze" minOccurs="0">
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
 *         &lt;element name="AnzahlLecks" minOccurs="0">
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
 *         &lt;element name="BeweissicherungErfolgt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Einsatzmethode" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Thermografie" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="Druckprobe" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="Endoskopie" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="Tracergas" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="Feuchtigkeitsmessung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="Elektroakustik" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="Sonstiges" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="BeschreibungEinsatzmethode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Folgeaktivitaeten" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Trocknung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="Regulierereinsatz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="freigelegteSchadenstelleSchliessen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="Sonstiges" minOccurs="0">
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
 *         &lt;element name="BeschreibungFolgeaktivitaet" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Leckfundstelle" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Raum" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SaniertesObjektTyp" minOccurs="0"/>
 *                   &lt;element name="Stockwerk" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}StockwerkTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="gefliesterBereichBetroffen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="NeuverlegungGuenstiger" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Rohrart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}RohrartTyp" minOccurs="0"/>
 *         &lt;element name="Rohrnutzung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}RohrnutzungTyp" minOccurs="0"/>
 *         &lt;element name="SchadenstelleFreigelegt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Leckursache" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>LeckursacheTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="UrsacheFuerMehrfachortung" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4502">
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
    "anzahlEinsaetze",
    "anzahlLecks",
    "beweissicherungErfolgt",
    "einsatzmethode",
    "beschreibungEinsatzmethode",
    "folgeaktivitaeten",
    "beschreibungFolgeaktivitaet",
    "leckfundstelle",
    "gefliesterBereichBetroffen",
    "neuverlegungGuenstiger",
    "rohrart",
    "rohrnutzung",
    "schadenstelleFreigelegt",
    "leckursache",
    "ursacheFuerMehrfachortung"
})
@XmlRootElement(name = "LeckageMessprotokoll")
public class LeckageMessprotokoll {

    @XmlElement(name = "AnzahlEinsaetze")
    protected LeckageMessprotokoll.AnzahlEinsaetze anzahlEinsaetze;
    @XmlElement(name = "AnzahlLecks")
    protected LeckageMessprotokoll.AnzahlLecks anzahlLecks;
    @XmlElement(name = "BeweissicherungErfolgt")
    protected String beweissicherungErfolgt;
    @XmlElement(name = "Einsatzmethode")
    protected LeckageMessprotokoll.Einsatzmethode einsatzmethode;
    @XmlElement(name = "BeschreibungEinsatzmethode")
    protected String beschreibungEinsatzmethode;
    @XmlElement(name = "Folgeaktivitaeten")
    protected LeckageMessprotokoll.Folgeaktivitaeten folgeaktivitaeten;
    @XmlElement(name = "BeschreibungFolgeaktivitaet")
    protected String beschreibungFolgeaktivitaet;
    @XmlElement(name = "Leckfundstelle")
    protected LeckageMessprotokoll.Leckfundstelle leckfundstelle;
    protected String gefliesterBereichBetroffen;
    @XmlElement(name = "NeuverlegungGuenstiger")
    protected String neuverlegungGuenstiger;
    @XmlElement(name = "Rohrart")
    protected String rohrart;
    @XmlElement(name = "Rohrnutzung")
    protected String rohrnutzung;
    @XmlElement(name = "SchadenstelleFreigelegt")
    protected String schadenstelleFreigelegt;
    @XmlElement(name = "Leckursache")
    protected LeckageMessprotokoll.Leckursache leckursache;
    @XmlElement(name = "UrsacheFuerMehrfachortung")
    protected LeckageMessprotokoll.UrsacheFuerMehrfachortung ursacheFuerMehrfachortung;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der anzahlEinsaetze-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LeckageMessprotokoll.AnzahlEinsaetze }
     *     
     */
    public LeckageMessprotokoll.AnzahlEinsaetze getAnzahlEinsaetze() {
        return anzahlEinsaetze;
    }

    /**
     * Legt den Wert der anzahlEinsaetze-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LeckageMessprotokoll.AnzahlEinsaetze }
     *     
     */
    public void setAnzahlEinsaetze(LeckageMessprotokoll.AnzahlEinsaetze value) {
        this.anzahlEinsaetze = value;
    }

    /**
     * Ruft den Wert der anzahlLecks-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LeckageMessprotokoll.AnzahlLecks }
     *     
     */
    public LeckageMessprotokoll.AnzahlLecks getAnzahlLecks() {
        return anzahlLecks;
    }

    /**
     * Legt den Wert der anzahlLecks-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LeckageMessprotokoll.AnzahlLecks }
     *     
     */
    public void setAnzahlLecks(LeckageMessprotokoll.AnzahlLecks value) {
        this.anzahlLecks = value;
    }

    /**
     * Ruft den Wert der beweissicherungErfolgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeweissicherungErfolgt() {
        return beweissicherungErfolgt;
    }

    /**
     * Legt den Wert der beweissicherungErfolgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeweissicherungErfolgt(String value) {
        this.beweissicherungErfolgt = value;
    }

    /**
     * Ruft den Wert der einsatzmethode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LeckageMessprotokoll.Einsatzmethode }
     *     
     */
    public LeckageMessprotokoll.Einsatzmethode getEinsatzmethode() {
        return einsatzmethode;
    }

    /**
     * Legt den Wert der einsatzmethode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LeckageMessprotokoll.Einsatzmethode }
     *     
     */
    public void setEinsatzmethode(LeckageMessprotokoll.Einsatzmethode value) {
        this.einsatzmethode = value;
    }

    /**
     * Ruft den Wert der beschreibungEinsatzmethode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeschreibungEinsatzmethode() {
        return beschreibungEinsatzmethode;
    }

    /**
     * Legt den Wert der beschreibungEinsatzmethode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeschreibungEinsatzmethode(String value) {
        this.beschreibungEinsatzmethode = value;
    }

    /**
     * Ruft den Wert der folgeaktivitaeten-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LeckageMessprotokoll.Folgeaktivitaeten }
     *     
     */
    public LeckageMessprotokoll.Folgeaktivitaeten getFolgeaktivitaeten() {
        return folgeaktivitaeten;
    }

    /**
     * Legt den Wert der folgeaktivitaeten-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LeckageMessprotokoll.Folgeaktivitaeten }
     *     
     */
    public void setFolgeaktivitaeten(LeckageMessprotokoll.Folgeaktivitaeten value) {
        this.folgeaktivitaeten = value;
    }

    /**
     * Ruft den Wert der beschreibungFolgeaktivitaet-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeschreibungFolgeaktivitaet() {
        return beschreibungFolgeaktivitaet;
    }

    /**
     * Legt den Wert der beschreibungFolgeaktivitaet-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeschreibungFolgeaktivitaet(String value) {
        this.beschreibungFolgeaktivitaet = value;
    }

    /**
     * Ruft den Wert der leckfundstelle-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LeckageMessprotokoll.Leckfundstelle }
     *     
     */
    public LeckageMessprotokoll.Leckfundstelle getLeckfundstelle() {
        return leckfundstelle;
    }

    /**
     * Legt den Wert der leckfundstelle-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LeckageMessprotokoll.Leckfundstelle }
     *     
     */
    public void setLeckfundstelle(LeckageMessprotokoll.Leckfundstelle value) {
        this.leckfundstelle = value;
    }

    /**
     * Ruft den Wert der gefliesterBereichBetroffen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGefliesterBereichBetroffen() {
        return gefliesterBereichBetroffen;
    }

    /**
     * Legt den Wert der gefliesterBereichBetroffen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGefliesterBereichBetroffen(String value) {
        this.gefliesterBereichBetroffen = value;
    }

    /**
     * Ruft den Wert der neuverlegungGuenstiger-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNeuverlegungGuenstiger() {
        return neuverlegungGuenstiger;
    }

    /**
     * Legt den Wert der neuverlegungGuenstiger-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNeuverlegungGuenstiger(String value) {
        this.neuverlegungGuenstiger = value;
    }

    /**
     * Ruft den Wert der rohrart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRohrart() {
        return rohrart;
    }

    /**
     * Legt den Wert der rohrart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRohrart(String value) {
        this.rohrart = value;
    }

    /**
     * Ruft den Wert der rohrnutzung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRohrnutzung() {
        return rohrnutzung;
    }

    /**
     * Legt den Wert der rohrnutzung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRohrnutzung(String value) {
        this.rohrnutzung = value;
    }

    /**
     * Ruft den Wert der schadenstelleFreigelegt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenstelleFreigelegt() {
        return schadenstelleFreigelegt;
    }

    /**
     * Legt den Wert der schadenstelleFreigelegt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenstelleFreigelegt(String value) {
        this.schadenstelleFreigelegt = value;
    }

    /**
     * Ruft den Wert der leckursache-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LeckageMessprotokoll.Leckursache }
     *     
     */
    public LeckageMessprotokoll.Leckursache getLeckursache() {
        return leckursache;
    }

    /**
     * Legt den Wert der leckursache-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LeckageMessprotokoll.Leckursache }
     *     
     */
    public void setLeckursache(LeckageMessprotokoll.Leckursache value) {
        this.leckursache = value;
    }

    /**
     * Ruft den Wert der ursacheFuerMehrfachortung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LeckageMessprotokoll.UrsacheFuerMehrfachortung }
     *     
     */
    public LeckageMessprotokoll.UrsacheFuerMehrfachortung getUrsacheFuerMehrfachortung() {
        return ursacheFuerMehrfachortung;
    }

    /**
     * Legt den Wert der ursacheFuerMehrfachortung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LeckageMessprotokoll.UrsacheFuerMehrfachortung }
     *     
     */
    public void setUrsacheFuerMehrfachortung(LeckageMessprotokoll.UrsacheFuerMehrfachortung value) {
        this.ursacheFuerMehrfachortung = value;
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
            return "4502";
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
    public static class AnzahlEinsaetze {

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
    public static class AnzahlLecks {

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
     *         &lt;element name="Thermografie" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="Druckprobe" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="Endoskopie" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="Tracergas" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="Feuchtigkeitsmessung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="Elektroakustik" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="Sonstiges" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
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
        "thermografie",
        "druckprobe",
        "endoskopie",
        "tracergas",
        "feuchtigkeitsmessung",
        "elektroakustik",
        "sonstiges"
    })
    public static class Einsatzmethode {

        @XmlElement(name = "Thermografie")
        protected String thermografie;
        @XmlElement(name = "Druckprobe")
        protected String druckprobe;
        @XmlElement(name = "Endoskopie")
        protected String endoskopie;
        @XmlElement(name = "Tracergas")
        protected String tracergas;
        @XmlElement(name = "Feuchtigkeitsmessung")
        protected String feuchtigkeitsmessung;
        @XmlElement(name = "Elektroakustik")
        protected String elektroakustik;
        @XmlElement(name = "Sonstiges")
        protected String sonstiges;

        /**
         * Ruft den Wert der thermografie-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getThermografie() {
            return thermografie;
        }

        /**
         * Legt den Wert der thermografie-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setThermografie(String value) {
            this.thermografie = value;
        }

        /**
         * Ruft den Wert der druckprobe-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDruckprobe() {
            return druckprobe;
        }

        /**
         * Legt den Wert der druckprobe-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDruckprobe(String value) {
            this.druckprobe = value;
        }

        /**
         * Ruft den Wert der endoskopie-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEndoskopie() {
            return endoskopie;
        }

        /**
         * Legt den Wert der endoskopie-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEndoskopie(String value) {
            this.endoskopie = value;
        }

        /**
         * Ruft den Wert der tracergas-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTracergas() {
            return tracergas;
        }

        /**
         * Legt den Wert der tracergas-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTracergas(String value) {
            this.tracergas = value;
        }

        /**
         * Ruft den Wert der feuchtigkeitsmessung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFeuchtigkeitsmessung() {
            return feuchtigkeitsmessung;
        }

        /**
         * Legt den Wert der feuchtigkeitsmessung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFeuchtigkeitsmessung(String value) {
            this.feuchtigkeitsmessung = value;
        }

        /**
         * Ruft den Wert der elektroakustik-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getElektroakustik() {
            return elektroakustik;
        }

        /**
         * Legt den Wert der elektroakustik-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setElektroakustik(String value) {
            this.elektroakustik = value;
        }

        /**
         * Ruft den Wert der sonstiges-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSonstiges() {
            return sonstiges;
        }

        /**
         * Legt den Wert der sonstiges-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSonstiges(String value) {
            this.sonstiges = value;
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
     *         &lt;element name="Trocknung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="Regulierereinsatz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="freigelegteSchadenstelleSchliessen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="Sonstiges" minOccurs="0">
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
        "trocknung",
        "regulierereinsatz",
        "freigelegteSchadenstelleSchliessen",
        "sonstiges"
    })
    public static class Folgeaktivitaeten {

        @XmlElement(name = "Trocknung")
        protected String trocknung;
        @XmlElement(name = "Regulierereinsatz")
        protected String regulierereinsatz;
        protected String freigelegteSchadenstelleSchliessen;
        @XmlElement(name = "Sonstiges")
        protected LeckageMessprotokoll.Folgeaktivitaeten.Sonstiges sonstiges;

        /**
         * Ruft den Wert der trocknung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTrocknung() {
            return trocknung;
        }

        /**
         * Legt den Wert der trocknung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTrocknung(String value) {
            this.trocknung = value;
        }

        /**
         * Ruft den Wert der regulierereinsatz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRegulierereinsatz() {
            return regulierereinsatz;
        }

        /**
         * Legt den Wert der regulierereinsatz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRegulierereinsatz(String value) {
            this.regulierereinsatz = value;
        }

        /**
         * Ruft den Wert der freigelegteSchadenstelleSchliessen-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFreigelegteSchadenstelleSchliessen() {
            return freigelegteSchadenstelleSchliessen;
        }

        /**
         * Legt den Wert der freigelegteSchadenstelleSchliessen-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFreigelegteSchadenstelleSchliessen(String value) {
            this.freigelegteSchadenstelleSchliessen = value;
        }

        /**
         * Ruft den Wert der sonstiges-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link LeckageMessprotokoll.Folgeaktivitaeten.Sonstiges }
         *     
         */
        public LeckageMessprotokoll.Folgeaktivitaeten.Sonstiges getSonstiges() {
            return sonstiges;
        }

        /**
         * Legt den Wert der sonstiges-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link LeckageMessprotokoll.Folgeaktivitaeten.Sonstiges }
         *     
         */
        public void setSonstiges(LeckageMessprotokoll.Folgeaktivitaeten.Sonstiges value) {
            this.sonstiges = value;
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
        public static class Sonstiges {

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
     *         &lt;element name="Raum" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SaniertesObjektTyp" minOccurs="0"/>
     *         &lt;element name="Stockwerk" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}StockwerkTyp" minOccurs="0"/>
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
        "raum",
        "stockwerk"
    })
    public static class Leckfundstelle {

        @XmlElement(name = "Raum")
        protected String raum;
        @XmlElement(name = "Stockwerk")
        protected String stockwerk;

        /**
         * Ruft den Wert der raum-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRaum() {
            return raum;
        }

        /**
         * Legt den Wert der raum-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRaum(String value) {
            this.raum = value;
        }

        /**
         * Ruft den Wert der stockwerk-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStockwerk() {
            return stockwerk;
        }

        /**
         * Legt den Wert der stockwerk-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStockwerk(String value) {
            this.stockwerk = value;
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
     *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>LeckursacheTyp">
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
    public static class Leckursache {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "Satzende")
        protected String satzende;

        /**
         * Ursache für Durchnässungsschaden
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
    public static class UrsacheFuerMehrfachortung
        extends SatzendeTyp
    {


    }

}
