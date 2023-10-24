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
 *         &lt;element name="Risiko" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}RisikoRechtsschutzTyp" minOccurs="0"/>
 *         &lt;element name="Schadendatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Schadenort" minOccurs="0">
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
 *         &lt;element name="EigenschaftDesVN" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}EigenschaftDesVNTyp" minOccurs="0"/>
 *         &lt;element name="AnzahlWohnungen" minOccurs="0">
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
 *         &lt;element name="BereitsVorGericht" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="VorgangMitErstberatungAbgeschlossen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Personenschaden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Rechtslageaenderung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="VerhaltensbedingteKuendigung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Vorsatzvorwurf" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Vorwurf" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VorwurfTyp" minOccurs="0"/>
 *         &lt;element name="VorwurfText" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
 *         &lt;element name="Streitwert" minOccurs="0">
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
 *         &lt;element name="Verfahrensstadium" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VerfahrensstadiumTyp" minOccurs="0"/>
 *         &lt;element name="Gegner" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="VeranlagungszeitraumVon" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="VeranlagungszeitraumBis" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BetroffeneSteuer" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Zuordnung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ZuordnungTyp" minOccurs="0"/>
 *         &lt;element name="Bruttomonatsgehalt" minOccurs="0">
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
 *         &lt;element name="Klagefaehig" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4250">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="4"/>
 *             &lt;minLength value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Versionsnummer" fixed="002">
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
    "risiko",
    "schadendatum",
    "schadenort",
    "eigenschaftDesVN",
    "anzahlWohnungen",
    "bereitsVorGericht",
    "vorgangMitErstberatungAbgeschlossen",
    "personenschaden",
    "rechtslageaenderung",
    "verhaltensbedingteKuendigung",
    "vorsatzvorwurf",
    "vorwurf",
    "vorwurfText",
    "waehrungsschluessel",
    "streitwert",
    "verfahrensstadium",
    "gegner",
    "veranlagungszeitraumVon",
    "veranlagungszeitraumBis",
    "betroffeneSteuer",
    "zuordnung",
    "bruttomonatsgehalt",
    "klagefaehig"
})
@XmlRootElement(name = "RechtsschutzMeldung")
public class RechtsschutzMeldung {

    @XmlElement(name = "Risiko")
    protected String risiko;
    @XmlElement(name = "Schadendatum")
    protected String schadendatum;
    @XmlElement(name = "Schadenort")
    protected RechtsschutzMeldung.Schadenort schadenort;
    @XmlElement(name = "EigenschaftDesVN")
    protected String eigenschaftDesVN;
    @XmlElement(name = "AnzahlWohnungen")
    protected RechtsschutzMeldung.AnzahlWohnungen anzahlWohnungen;
    @XmlElement(name = "BereitsVorGericht")
    protected String bereitsVorGericht;
    @XmlElement(name = "VorgangMitErstberatungAbgeschlossen")
    protected String vorgangMitErstberatungAbgeschlossen;
    @XmlElement(name = "Personenschaden")
    protected String personenschaden;
    @XmlElement(name = "Rechtslageaenderung")
    protected String rechtslageaenderung;
    @XmlElement(name = "VerhaltensbedingteKuendigung")
    protected String verhaltensbedingteKuendigung;
    @XmlElement(name = "Vorsatzvorwurf")
    protected String vorsatzvorwurf;
    @XmlElement(name = "Vorwurf")
    protected String vorwurf;
    @XmlElement(name = "VorwurfText")
    protected String vorwurfText;
    @XmlElement(name = "Waehrungsschluessel")
    protected String waehrungsschluessel;
    @XmlElement(name = "Streitwert")
    protected RechtsschutzMeldung.Streitwert streitwert;
    @XmlElement(name = "Verfahrensstadium")
    protected String verfahrensstadium;
    @XmlElement(name = "Gegner")
    protected RechtsschutzMeldung.Gegner gegner;
    @XmlElement(name = "VeranlagungszeitraumVon")
    protected String veranlagungszeitraumVon;
    @XmlElement(name = "VeranlagungszeitraumBis")
    protected String veranlagungszeitraumBis;
    @XmlElement(name = "BetroffeneSteuer")
    protected String betroffeneSteuer;
    @XmlElement(name = "Zuordnung")
    protected String zuordnung;
    @XmlElement(name = "Bruttomonatsgehalt")
    protected RechtsschutzMeldung.Bruttomonatsgehalt bruttomonatsgehalt;
    @XmlElement(name = "Klagefaehig")
    protected RechtsschutzMeldung.Klagefaehig klagefaehig;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der risiko-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRisiko() {
        return risiko;
    }

    /**
     * Legt den Wert der risiko-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRisiko(String value) {
        this.risiko = value;
    }

    /**
     * Ruft den Wert der schadendatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadendatum() {
        return schadendatum;
    }

    /**
     * Legt den Wert der schadendatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadendatum(String value) {
        this.schadendatum = value;
    }

    /**
     * Ruft den Wert der schadenort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RechtsschutzMeldung.Schadenort }
     *     
     */
    public RechtsschutzMeldung.Schadenort getSchadenort() {
        return schadenort;
    }

    /**
     * Legt den Wert der schadenort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RechtsschutzMeldung.Schadenort }
     *     
     */
    public void setSchadenort(RechtsschutzMeldung.Schadenort value) {
        this.schadenort = value;
    }

    /**
     * Ruft den Wert der eigenschaftDesVN-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEigenschaftDesVN() {
        return eigenschaftDesVN;
    }

    /**
     * Legt den Wert der eigenschaftDesVN-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEigenschaftDesVN(String value) {
        this.eigenschaftDesVN = value;
    }

    /**
     * Ruft den Wert der anzahlWohnungen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RechtsschutzMeldung.AnzahlWohnungen }
     *     
     */
    public RechtsschutzMeldung.AnzahlWohnungen getAnzahlWohnungen() {
        return anzahlWohnungen;
    }

    /**
     * Legt den Wert der anzahlWohnungen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RechtsschutzMeldung.AnzahlWohnungen }
     *     
     */
    public void setAnzahlWohnungen(RechtsschutzMeldung.AnzahlWohnungen value) {
        this.anzahlWohnungen = value;
    }

    /**
     * Ruft den Wert der bereitsVorGericht-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBereitsVorGericht() {
        return bereitsVorGericht;
    }

    /**
     * Legt den Wert der bereitsVorGericht-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBereitsVorGericht(String value) {
        this.bereitsVorGericht = value;
    }

    /**
     * Ruft den Wert der vorgangMitErstberatungAbgeschlossen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVorgangMitErstberatungAbgeschlossen() {
        return vorgangMitErstberatungAbgeschlossen;
    }

    /**
     * Legt den Wert der vorgangMitErstberatungAbgeschlossen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVorgangMitErstberatungAbgeschlossen(String value) {
        this.vorgangMitErstberatungAbgeschlossen = value;
    }

    /**
     * Ruft den Wert der personenschaden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonenschaden() {
        return personenschaden;
    }

    /**
     * Legt den Wert der personenschaden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonenschaden(String value) {
        this.personenschaden = value;
    }

    /**
     * Ruft den Wert der rechtslageaenderung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRechtslageaenderung() {
        return rechtslageaenderung;
    }

    /**
     * Legt den Wert der rechtslageaenderung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRechtslageaenderung(String value) {
        this.rechtslageaenderung = value;
    }

    /**
     * Ruft den Wert der verhaltensbedingteKuendigung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerhaltensbedingteKuendigung() {
        return verhaltensbedingteKuendigung;
    }

    /**
     * Legt den Wert der verhaltensbedingteKuendigung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerhaltensbedingteKuendigung(String value) {
        this.verhaltensbedingteKuendigung = value;
    }

    /**
     * Ruft den Wert der vorsatzvorwurf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVorsatzvorwurf() {
        return vorsatzvorwurf;
    }

    /**
     * Legt den Wert der vorsatzvorwurf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVorsatzvorwurf(String value) {
        this.vorsatzvorwurf = value;
    }

    /**
     * Ruft den Wert der vorwurf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVorwurf() {
        return vorwurf;
    }

    /**
     * Legt den Wert der vorwurf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVorwurf(String value) {
        this.vorwurf = value;
    }

    /**
     * Ruft den Wert der vorwurfText-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVorwurfText() {
        return vorwurfText;
    }

    /**
     * Legt den Wert der vorwurfText-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVorwurfText(String value) {
        this.vorwurfText = value;
    }

    /**
     * Ruft den Wert der waehrungsschluessel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWaehrungsschluessel() {
        return waehrungsschluessel;
    }

    /**
     * Legt den Wert der waehrungsschluessel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWaehrungsschluessel(String value) {
        this.waehrungsschluessel = value;
    }

    /**
     * Ruft den Wert der streitwert-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RechtsschutzMeldung.Streitwert }
     *     
     */
    public RechtsschutzMeldung.Streitwert getStreitwert() {
        return streitwert;
    }

    /**
     * Legt den Wert der streitwert-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RechtsschutzMeldung.Streitwert }
     *     
     */
    public void setStreitwert(RechtsschutzMeldung.Streitwert value) {
        this.streitwert = value;
    }

    /**
     * Ruft den Wert der verfahrensstadium-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerfahrensstadium() {
        return verfahrensstadium;
    }

    /**
     * Legt den Wert der verfahrensstadium-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerfahrensstadium(String value) {
        this.verfahrensstadium = value;
    }

    /**
     * Ruft den Wert der gegner-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RechtsschutzMeldung.Gegner }
     *     
     */
    public RechtsschutzMeldung.Gegner getGegner() {
        return gegner;
    }

    /**
     * Legt den Wert der gegner-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RechtsschutzMeldung.Gegner }
     *     
     */
    public void setGegner(RechtsschutzMeldung.Gegner value) {
        this.gegner = value;
    }

    /**
     * Ruft den Wert der veranlagungszeitraumVon-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVeranlagungszeitraumVon() {
        return veranlagungszeitraumVon;
    }

    /**
     * Legt den Wert der veranlagungszeitraumVon-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVeranlagungszeitraumVon(String value) {
        this.veranlagungszeitraumVon = value;
    }

    /**
     * Ruft den Wert der veranlagungszeitraumBis-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVeranlagungszeitraumBis() {
        return veranlagungszeitraumBis;
    }

    /**
     * Legt den Wert der veranlagungszeitraumBis-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVeranlagungszeitraumBis(String value) {
        this.veranlagungszeitraumBis = value;
    }

    /**
     * Ruft den Wert der betroffeneSteuer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBetroffeneSteuer() {
        return betroffeneSteuer;
    }

    /**
     * Legt den Wert der betroffeneSteuer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBetroffeneSteuer(String value) {
        this.betroffeneSteuer = value;
    }

    /**
     * Ruft den Wert der zuordnung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZuordnung() {
        return zuordnung;
    }

    /**
     * Legt den Wert der zuordnung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZuordnung(String value) {
        this.zuordnung = value;
    }

    /**
     * Ruft den Wert der bruttomonatsgehalt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RechtsschutzMeldung.Bruttomonatsgehalt }
     *     
     */
    public RechtsschutzMeldung.Bruttomonatsgehalt getBruttomonatsgehalt() {
        return bruttomonatsgehalt;
    }

    /**
     * Legt den Wert der bruttomonatsgehalt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RechtsschutzMeldung.Bruttomonatsgehalt }
     *     
     */
    public void setBruttomonatsgehalt(RechtsschutzMeldung.Bruttomonatsgehalt value) {
        this.bruttomonatsgehalt = value;
    }

    /**
     * Ruft den Wert der klagefaehig-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RechtsschutzMeldung.Klagefaehig }
     *     
     */
    public RechtsschutzMeldung.Klagefaehig getKlagefaehig() {
        return klagefaehig;
    }

    /**
     * Legt den Wert der klagefaehig-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RechtsschutzMeldung.Klagefaehig }
     *     
     */
    public void setKlagefaehig(RechtsschutzMeldung.Klagefaehig value) {
        this.klagefaehig = value;
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
            return "4250";
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
            return "002";
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
    public static class AnzahlWohnungen {

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
    public static class Bruttomonatsgehalt {

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
    public static class Gegner
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
    public static class Klagefaehig {

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
    public static class Schadenort {

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
    public static class Streitwert {

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
