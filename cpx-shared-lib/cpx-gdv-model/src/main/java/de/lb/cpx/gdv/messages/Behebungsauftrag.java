//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.19 um 11:55:14 AM CEST 
//


package de.lb.cpx.gdv.messages;

import java.math.BigDecimal;
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
 *         &lt;element name="ReferenzierungAuftraggeber">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp"/>
 *                   &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Auftragstypen" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Auftragsart1" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp"/>
 *                   &lt;element name="Auftragsart2" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart3" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart4" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart5" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart6" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart7" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart8" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart9" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart10" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart11" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart12" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart13" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart14" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart15" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart16" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart17" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart18" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart19" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart20" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Auftragsbeschreibung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Auftragstyp" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragstypTyp" minOccurs="0"/>
 *         &lt;element name="Rahmenvertrag" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="UebernahmeVonDL" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="BetroffenerBereich" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}versichertesRisikoTyp" minOccurs="0"/>
 *         &lt;element name="FuerLeckageortZusaetzlichBeweissicherung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="ZahlungsempfaengerAngebot" minOccurs="0">
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
 *         &lt;element name="ZahlungsempfaengerRechnung" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
 *                   &lt;element name="PartnerReferenz" minOccurs="0">
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
 *         &lt;element name="AdressKennzeichenBesichtigungsort" minOccurs="0">
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
 *         &lt;element name="BeschreibungBesichtigungsort" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="80"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
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
 *         &lt;element name="Wunschtermin" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Schadengrund" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SchadengrundSachTyp" minOccurs="0"/>
 *         &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
 *         &lt;element name="Schadenreserve" minOccurs="0">
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
 *         &lt;element name="ReguliererTaetig" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4801">
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
    "referenzierungAuftraggeber",
    "auftragstypen",
    "auftragsbeschreibung",
    "auftragstyp",
    "rahmenvertrag",
    "uebernahmeVonDL",
    "betroffenerBereich",
    "fuerLeckageortZusaetzlichBeweissicherung",
    "zahlungsempfaengerAngebot",
    "zahlungsempfaengerRechnung",
    "adressKennzeichenBesichtigungsort",
    "beschreibungBesichtigungsort",
    "besichtigungstag",
    "besichtigungszeit",
    "wunschtermin",
    "schadengrund",
    "waehrungsschluessel",
    "schadenreserve",
    "reguliererTaetig"
})
@XmlRootElement(name = "Behebungsauftrag")
public class Behebungsauftrag {

    @XmlElement(name = "ReferenzierungAuftraggeber", required = true)
    protected Behebungsauftrag.ReferenzierungAuftraggeber referenzierungAuftraggeber;
    @XmlElement(name = "Auftragstypen")
    protected Behebungsauftrag.Auftragstypen auftragstypen;
    @XmlElement(name = "Auftragsbeschreibung")
    protected String auftragsbeschreibung;
    @XmlElement(name = "Auftragstyp")
    protected String auftragstyp;
    @XmlElement(name = "Rahmenvertrag")
    protected String rahmenvertrag;
    @XmlElement(name = "UebernahmeVonDL")
    protected String uebernahmeVonDL;
    @XmlElement(name = "BetroffenerBereich")
    protected String betroffenerBereich;
    @XmlElement(name = "FuerLeckageortZusaetzlichBeweissicherung")
    protected String fuerLeckageortZusaetzlichBeweissicherung;
    @XmlElement(name = "ZahlungsempfaengerAngebot")
    protected Behebungsauftrag.ZahlungsempfaengerAngebot zahlungsempfaengerAngebot;
    @XmlElement(name = "ZahlungsempfaengerRechnung")
    protected Behebungsauftrag.ZahlungsempfaengerRechnung zahlungsempfaengerRechnung;
    @XmlElement(name = "AdressKennzeichenBesichtigungsort")
    protected Behebungsauftrag.AdressKennzeichenBesichtigungsort adressKennzeichenBesichtigungsort;
    @XmlElement(name = "BeschreibungBesichtigungsort")
    protected String beschreibungBesichtigungsort;
    @XmlElement(name = "Besichtigungstag")
    protected String besichtigungstag;
    @XmlElement(name = "Besichtigungszeit")
    protected String besichtigungszeit;
    @XmlElement(name = "Wunschtermin")
    protected String wunschtermin;
    @XmlElement(name = "Schadengrund")
    protected String schadengrund;
    @XmlElement(name = "Waehrungsschluessel")
    protected String waehrungsschluessel;
    @XmlElement(name = "Schadenreserve")
    protected Behebungsauftrag.Schadenreserve schadenreserve;
    @XmlElement(name = "ReguliererTaetig")
    protected Behebungsauftrag.ReguliererTaetig reguliererTaetig;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der referenzierungAuftraggeber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Behebungsauftrag.ReferenzierungAuftraggeber }
     *     
     */
    public Behebungsauftrag.ReferenzierungAuftraggeber getReferenzierungAuftraggeber() {
        return referenzierungAuftraggeber;
    }

    /**
     * Legt den Wert der referenzierungAuftraggeber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Behebungsauftrag.ReferenzierungAuftraggeber }
     *     
     */
    public void setReferenzierungAuftraggeber(Behebungsauftrag.ReferenzierungAuftraggeber value) {
        this.referenzierungAuftraggeber = value;
    }

    /**
     * Ruft den Wert der auftragstypen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Behebungsauftrag.Auftragstypen }
     *     
     */
    public Behebungsauftrag.Auftragstypen getAuftragstypen() {
        return auftragstypen;
    }

    /**
     * Legt den Wert der auftragstypen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Behebungsauftrag.Auftragstypen }
     *     
     */
    public void setAuftragstypen(Behebungsauftrag.Auftragstypen value) {
        this.auftragstypen = value;
    }

    /**
     * Ruft den Wert der auftragsbeschreibung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuftragsbeschreibung() {
        return auftragsbeschreibung;
    }

    /**
     * Legt den Wert der auftragsbeschreibung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuftragsbeschreibung(String value) {
        this.auftragsbeschreibung = value;
    }

    /**
     * Ruft den Wert der auftragstyp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuftragstyp() {
        return auftragstyp;
    }

    /**
     * Legt den Wert der auftragstyp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuftragstyp(String value) {
        this.auftragstyp = value;
    }

    /**
     * Ruft den Wert der rahmenvertrag-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRahmenvertrag() {
        return rahmenvertrag;
    }

    /**
     * Legt den Wert der rahmenvertrag-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRahmenvertrag(String value) {
        this.rahmenvertrag = value;
    }

    /**
     * Ruft den Wert der uebernahmeVonDL-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUebernahmeVonDL() {
        return uebernahmeVonDL;
    }

    /**
     * Legt den Wert der uebernahmeVonDL-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUebernahmeVonDL(String value) {
        this.uebernahmeVonDL = value;
    }

    /**
     * Ruft den Wert der betroffenerBereich-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBetroffenerBereich() {
        return betroffenerBereich;
    }

    /**
     * Legt den Wert der betroffenerBereich-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBetroffenerBereich(String value) {
        this.betroffenerBereich = value;
    }

    /**
     * Ruft den Wert der fuerLeckageortZusaetzlichBeweissicherung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFuerLeckageortZusaetzlichBeweissicherung() {
        return fuerLeckageortZusaetzlichBeweissicherung;
    }

    /**
     * Legt den Wert der fuerLeckageortZusaetzlichBeweissicherung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFuerLeckageortZusaetzlichBeweissicherung(String value) {
        this.fuerLeckageortZusaetzlichBeweissicherung = value;
    }

    /**
     * Ruft den Wert der zahlungsempfaengerAngebot-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Behebungsauftrag.ZahlungsempfaengerAngebot }
     *     
     */
    public Behebungsauftrag.ZahlungsempfaengerAngebot getZahlungsempfaengerAngebot() {
        return zahlungsempfaengerAngebot;
    }

    /**
     * Legt den Wert der zahlungsempfaengerAngebot-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Behebungsauftrag.ZahlungsempfaengerAngebot }
     *     
     */
    public void setZahlungsempfaengerAngebot(Behebungsauftrag.ZahlungsempfaengerAngebot value) {
        this.zahlungsempfaengerAngebot = value;
    }

    /**
     * Ruft den Wert der zahlungsempfaengerRechnung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Behebungsauftrag.ZahlungsempfaengerRechnung }
     *     
     */
    public Behebungsauftrag.ZahlungsempfaengerRechnung getZahlungsempfaengerRechnung() {
        return zahlungsempfaengerRechnung;
    }

    /**
     * Legt den Wert der zahlungsempfaengerRechnung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Behebungsauftrag.ZahlungsempfaengerRechnung }
     *     
     */
    public void setZahlungsempfaengerRechnung(Behebungsauftrag.ZahlungsempfaengerRechnung value) {
        this.zahlungsempfaengerRechnung = value;
    }

    /**
     * Ruft den Wert der adressKennzeichenBesichtigungsort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Behebungsauftrag.AdressKennzeichenBesichtigungsort }
     *     
     */
    public Behebungsauftrag.AdressKennzeichenBesichtigungsort getAdressKennzeichenBesichtigungsort() {
        return adressKennzeichenBesichtigungsort;
    }

    /**
     * Legt den Wert der adressKennzeichenBesichtigungsort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Behebungsauftrag.AdressKennzeichenBesichtigungsort }
     *     
     */
    public void setAdressKennzeichenBesichtigungsort(Behebungsauftrag.AdressKennzeichenBesichtigungsort value) {
        this.adressKennzeichenBesichtigungsort = value;
    }

    /**
     * Ruft den Wert der beschreibungBesichtigungsort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeschreibungBesichtigungsort() {
        return beschreibungBesichtigungsort;
    }

    /**
     * Legt den Wert der beschreibungBesichtigungsort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeschreibungBesichtigungsort(String value) {
        this.beschreibungBesichtigungsort = value;
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
     * Ruft den Wert der wunschtermin-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWunschtermin() {
        return wunschtermin;
    }

    /**
     * Legt den Wert der wunschtermin-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWunschtermin(String value) {
        this.wunschtermin = value;
    }

    /**
     * Ruft den Wert der schadengrund-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadengrund() {
        return schadengrund;
    }

    /**
     * Legt den Wert der schadengrund-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadengrund(String value) {
        this.schadengrund = value;
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
     * Ruft den Wert der schadenreserve-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Behebungsauftrag.Schadenreserve }
     *     
     */
    public Behebungsauftrag.Schadenreserve getSchadenreserve() {
        return schadenreserve;
    }

    /**
     * Legt den Wert der schadenreserve-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Behebungsauftrag.Schadenreserve }
     *     
     */
    public void setSchadenreserve(Behebungsauftrag.Schadenreserve value) {
        this.schadenreserve = value;
    }

    /**
     * Ruft den Wert der reguliererTaetig-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Behebungsauftrag.ReguliererTaetig }
     *     
     */
    public Behebungsauftrag.ReguliererTaetig getReguliererTaetig() {
        return reguliererTaetig;
    }

    /**
     * Legt den Wert der reguliererTaetig-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Behebungsauftrag.ReguliererTaetig }
     *     
     */
    public void setReguliererTaetig(Behebungsauftrag.ReguliererTaetig value) {
        this.reguliererTaetig = value;
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
            return "4801";
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
    public static class AdressKennzeichenBesichtigungsort {

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
     *         &lt;element name="Auftragsart1" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp"/>
     *         &lt;element name="Auftragsart2" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart3" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart4" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart5" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart6" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart7" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart8" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart9" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart10" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart11" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart12" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart13" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart14" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart15" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart16" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart17" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart18" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart19" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart20" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
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
        "auftragsart1",
        "auftragsart2",
        "auftragsart3",
        "auftragsart4",
        "auftragsart5",
        "auftragsart6",
        "auftragsart7",
        "auftragsart8",
        "auftragsart9",
        "auftragsart10",
        "auftragsart11",
        "auftragsart12",
        "auftragsart13",
        "auftragsart14",
        "auftragsart15",
        "auftragsart16",
        "auftragsart17",
        "auftragsart18",
        "auftragsart19",
        "auftragsart20"
    })
    public static class Auftragstypen {

        @XmlElement(name = "Auftragsart1", required = true)
        protected String auftragsart1;
        @XmlElement(name = "Auftragsart2")
        protected String auftragsart2;
        @XmlElement(name = "Auftragsart3")
        protected String auftragsart3;
        @XmlElement(name = "Auftragsart4")
        protected String auftragsart4;
        @XmlElement(name = "Auftragsart5")
        protected String auftragsart5;
        @XmlElement(name = "Auftragsart6")
        protected String auftragsart6;
        @XmlElement(name = "Auftragsart7")
        protected String auftragsart7;
        @XmlElement(name = "Auftragsart8")
        protected String auftragsart8;
        @XmlElement(name = "Auftragsart9")
        protected String auftragsart9;
        @XmlElement(name = "Auftragsart10")
        protected String auftragsart10;
        @XmlElement(name = "Auftragsart11")
        protected String auftragsart11;
        @XmlElement(name = "Auftragsart12")
        protected String auftragsart12;
        @XmlElement(name = "Auftragsart13")
        protected String auftragsart13;
        @XmlElement(name = "Auftragsart14")
        protected String auftragsart14;
        @XmlElement(name = "Auftragsart15")
        protected String auftragsart15;
        @XmlElement(name = "Auftragsart16")
        protected String auftragsart16;
        @XmlElement(name = "Auftragsart17")
        protected String auftragsart17;
        @XmlElement(name = "Auftragsart18")
        protected String auftragsart18;
        @XmlElement(name = "Auftragsart19")
        protected String auftragsart19;
        @XmlElement(name = "Auftragsart20")
        protected String auftragsart20;

        /**
         * Ruft den Wert der auftragsart1-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart1() {
            return auftragsart1;
        }

        /**
         * Legt den Wert der auftragsart1-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart1(String value) {
            this.auftragsart1 = value;
        }

        /**
         * Ruft den Wert der auftragsart2-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart2() {
            return auftragsart2;
        }

        /**
         * Legt den Wert der auftragsart2-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart2(String value) {
            this.auftragsart2 = value;
        }

        /**
         * Ruft den Wert der auftragsart3-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart3() {
            return auftragsart3;
        }

        /**
         * Legt den Wert der auftragsart3-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart3(String value) {
            this.auftragsart3 = value;
        }

        /**
         * Ruft den Wert der auftragsart4-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart4() {
            return auftragsart4;
        }

        /**
         * Legt den Wert der auftragsart4-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart4(String value) {
            this.auftragsart4 = value;
        }

        /**
         * Ruft den Wert der auftragsart5-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart5() {
            return auftragsart5;
        }

        /**
         * Legt den Wert der auftragsart5-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart5(String value) {
            this.auftragsart5 = value;
        }

        /**
         * Ruft den Wert der auftragsart6-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart6() {
            return auftragsart6;
        }

        /**
         * Legt den Wert der auftragsart6-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart6(String value) {
            this.auftragsart6 = value;
        }

        /**
         * Ruft den Wert der auftragsart7-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart7() {
            return auftragsart7;
        }

        /**
         * Legt den Wert der auftragsart7-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart7(String value) {
            this.auftragsart7 = value;
        }

        /**
         * Ruft den Wert der auftragsart8-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart8() {
            return auftragsart8;
        }

        /**
         * Legt den Wert der auftragsart8-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart8(String value) {
            this.auftragsart8 = value;
        }

        /**
         * Ruft den Wert der auftragsart9-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart9() {
            return auftragsart9;
        }

        /**
         * Legt den Wert der auftragsart9-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart9(String value) {
            this.auftragsart9 = value;
        }

        /**
         * Ruft den Wert der auftragsart10-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart10() {
            return auftragsart10;
        }

        /**
         * Legt den Wert der auftragsart10-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart10(String value) {
            this.auftragsart10 = value;
        }

        /**
         * Ruft den Wert der auftragsart11-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart11() {
            return auftragsart11;
        }

        /**
         * Legt den Wert der auftragsart11-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart11(String value) {
            this.auftragsart11 = value;
        }

        /**
         * Ruft den Wert der auftragsart12-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart12() {
            return auftragsart12;
        }

        /**
         * Legt den Wert der auftragsart12-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart12(String value) {
            this.auftragsart12 = value;
        }

        /**
         * Ruft den Wert der auftragsart13-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart13() {
            return auftragsart13;
        }

        /**
         * Legt den Wert der auftragsart13-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart13(String value) {
            this.auftragsart13 = value;
        }

        /**
         * Ruft den Wert der auftragsart14-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart14() {
            return auftragsart14;
        }

        /**
         * Legt den Wert der auftragsart14-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart14(String value) {
            this.auftragsart14 = value;
        }

        /**
         * Ruft den Wert der auftragsart15-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart15() {
            return auftragsart15;
        }

        /**
         * Legt den Wert der auftragsart15-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart15(String value) {
            this.auftragsart15 = value;
        }

        /**
         * Ruft den Wert der auftragsart16-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart16() {
            return auftragsart16;
        }

        /**
         * Legt den Wert der auftragsart16-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart16(String value) {
            this.auftragsart16 = value;
        }

        /**
         * Ruft den Wert der auftragsart17-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart17() {
            return auftragsart17;
        }

        /**
         * Legt den Wert der auftragsart17-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart17(String value) {
            this.auftragsart17 = value;
        }

        /**
         * Ruft den Wert der auftragsart18-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart18() {
            return auftragsart18;
        }

        /**
         * Legt den Wert der auftragsart18-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart18(String value) {
            this.auftragsart18 = value;
        }

        /**
         * Ruft den Wert der auftragsart19-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart19() {
            return auftragsart19;
        }

        /**
         * Legt den Wert der auftragsart19-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart19(String value) {
            this.auftragsart19 = value;
        }

        /**
         * Ruft den Wert der auftragsart20-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsart20() {
            return auftragsart20;
        }

        /**
         * Legt den Wert der auftragsart20-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsart20(String value) {
            this.auftragsart20 = value;
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
     *         &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp"/>
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
    public static class ReferenzierungAuftraggeber {

        @XmlElement(name = "AdressKennzeichen", required = true)
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
    public static class ReguliererTaetig {

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
    public static class Schadenreserve {

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
    public static class ZahlungsempfaengerAngebot {

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
     *         &lt;element name="PartnerReferenz" minOccurs="0">
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
        "adressKennzeichen",
        "partnerReferenz"
    })
    public static class ZahlungsempfaengerRechnung {

        @XmlElement(name = "AdressKennzeichen")
        protected String adressKennzeichen;
        @XmlElement(name = "PartnerReferenz")
        protected Behebungsauftrag.ZahlungsempfaengerRechnung.PartnerReferenz partnerReferenz;

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
         *     {@link Behebungsauftrag.ZahlungsempfaengerRechnung.PartnerReferenz }
         *     
         */
        public Behebungsauftrag.ZahlungsempfaengerRechnung.PartnerReferenz getPartnerReferenz() {
            return partnerReferenz;
        }

        /**
         * Legt den Wert der partnerReferenz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Behebungsauftrag.ZahlungsempfaengerRechnung.PartnerReferenz }
         *     
         */
        public void setPartnerReferenz(Behebungsauftrag.ZahlungsempfaengerRechnung.PartnerReferenz value) {
            this.partnerReferenz = value;
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
        public static class PartnerReferenz {

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

}
