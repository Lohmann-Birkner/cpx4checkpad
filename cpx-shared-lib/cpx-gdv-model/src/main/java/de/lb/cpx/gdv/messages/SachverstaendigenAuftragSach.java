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
 *         &lt;element name="Auftraege" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Auftragsart1" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp"/>
 *                   &lt;element name="Auftragsart2" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart3" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart4" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart5" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart6" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart7" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart8" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart9" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart10" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart11" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart12" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart13" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart14" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart15" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart16" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart17" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart18" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart19" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart20" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
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
 *         &lt;element name="Schadensparte" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Sparte1Typ" minOccurs="0"/>
 *         &lt;element name="Schadengrund" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SchadengrundSachTyp" minOccurs="0"/>
 *         &lt;element name="Hauptschadenbereich" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}HauptschadenbereichSachTyp" minOccurs="0"/>
 *         &lt;element name="GruppeSach" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}GruppeSachTyp" minOccurs="0"/>
 *         &lt;element name="Versicherungstyp" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VersicherungstypTyp" minOccurs="0"/>
 *         &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
 *         &lt;element name="formelleDeckung" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinUnklarTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
 *         &lt;element name="AbspracheMitVN" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AbspracheMitVNTyp" minOccurs="0"/>
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
 *                   &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ReferenzierungBesichtigungsort" minOccurs="0">
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
 *       &lt;attribute name="Satzart" fixed="4812">
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
    "auftraege",
    "auftragsbeschreibung",
    "schadensparte",
    "schadengrund",
    "hauptschadenbereich",
    "gruppeSach",
    "versicherungstyp",
    "waehrungsschluessel",
    "formelleDeckung",
    "schadenreserve",
    "abspracheMitVN",
    "zahlungsempfaengerAngebot",
    "zahlungsempfaengerRechnung",
    "referenzierungBesichtigungsort",
    "beschreibungBesichtigungsort",
    "besichtigungstag",
    "besichtigungszeit",
    "wunschtermin",
    "reguliererTaetig"
})
@XmlRootElement(name = "SachverstaendigenAuftragSach")
public class SachverstaendigenAuftragSach {

    @XmlElement(name = "ReferenzierungAuftraggeber", required = true)
    protected SachverstaendigenAuftragSach.ReferenzierungAuftraggeber referenzierungAuftraggeber;
    @XmlElement(name = "Auftraege")
    protected SachverstaendigenAuftragSach.Auftraege auftraege;
    @XmlElement(name = "Auftragsbeschreibung")
    protected String auftragsbeschreibung;
    @XmlElement(name = "Schadensparte")
    protected String schadensparte;
    @XmlElement(name = "Schadengrund")
    protected String schadengrund;
    @XmlElement(name = "Hauptschadenbereich")
    protected String hauptschadenbereich;
    @XmlElement(name = "GruppeSach")
    protected String gruppeSach;
    @XmlElement(name = "Versicherungstyp")
    protected String versicherungstyp;
    @XmlElement(name = "Waehrungsschluessel")
    protected String waehrungsschluessel;
    protected SachverstaendigenAuftragSach.FormelleDeckung formelleDeckung;
    @XmlElement(name = "Schadenreserve")
    protected SachverstaendigenAuftragSach.Schadenreserve schadenreserve;
    @XmlElement(name = "AbspracheMitVN")
    protected String abspracheMitVN;
    @XmlElement(name = "ZahlungsempfaengerAngebot")
    protected SachverstaendigenAuftragSach.ZahlungsempfaengerAngebot zahlungsempfaengerAngebot;
    @XmlElement(name = "ZahlungsempfaengerRechnung")
    protected SachverstaendigenAuftragSach.ZahlungsempfaengerRechnung zahlungsempfaengerRechnung;
    @XmlElement(name = "ReferenzierungBesichtigungsort")
    protected SachverstaendigenAuftragSach.ReferenzierungBesichtigungsort referenzierungBesichtigungsort;
    @XmlElement(name = "BeschreibungBesichtigungsort")
    protected String beschreibungBesichtigungsort;
    @XmlElement(name = "Besichtigungstag")
    protected String besichtigungstag;
    @XmlElement(name = "Besichtigungszeit")
    protected String besichtigungszeit;
    @XmlElement(name = "Wunschtermin")
    protected String wunschtermin;
    @XmlElement(name = "ReguliererTaetig")
    protected SachverstaendigenAuftragSach.ReguliererTaetig reguliererTaetig;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der referenzierungAuftraggeber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachverstaendigenAuftragSach.ReferenzierungAuftraggeber }
     *     
     */
    public SachverstaendigenAuftragSach.ReferenzierungAuftraggeber getReferenzierungAuftraggeber() {
        return referenzierungAuftraggeber;
    }

    /**
     * Legt den Wert der referenzierungAuftraggeber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachverstaendigenAuftragSach.ReferenzierungAuftraggeber }
     *     
     */
    public void setReferenzierungAuftraggeber(SachverstaendigenAuftragSach.ReferenzierungAuftraggeber value) {
        this.referenzierungAuftraggeber = value;
    }

    /**
     * Ruft den Wert der auftraege-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachverstaendigenAuftragSach.Auftraege }
     *     
     */
    public SachverstaendigenAuftragSach.Auftraege getAuftraege() {
        return auftraege;
    }

    /**
     * Legt den Wert der auftraege-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachverstaendigenAuftragSach.Auftraege }
     *     
     */
    public void setAuftraege(SachverstaendigenAuftragSach.Auftraege value) {
        this.auftraege = value;
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
     * Ruft den Wert der schadensparte-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadensparte() {
        return schadensparte;
    }

    /**
     * Legt den Wert der schadensparte-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadensparte(String value) {
        this.schadensparte = value;
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
     * Ruft den Wert der hauptschadenbereich-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHauptschadenbereich() {
        return hauptschadenbereich;
    }

    /**
     * Legt den Wert der hauptschadenbereich-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHauptschadenbereich(String value) {
        this.hauptschadenbereich = value;
    }

    /**
     * Ruft den Wert der gruppeSach-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGruppeSach() {
        return gruppeSach;
    }

    /**
     * Legt den Wert der gruppeSach-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGruppeSach(String value) {
        this.gruppeSach = value;
    }

    /**
     * Ruft den Wert der versicherungstyp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersicherungstyp() {
        return versicherungstyp;
    }

    /**
     * Legt den Wert der versicherungstyp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersicherungstyp(String value) {
        this.versicherungstyp = value;
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
     * Ruft den Wert der formelleDeckung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachverstaendigenAuftragSach.FormelleDeckung }
     *     
     */
    public SachverstaendigenAuftragSach.FormelleDeckung getFormelleDeckung() {
        return formelleDeckung;
    }

    /**
     * Legt den Wert der formelleDeckung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachverstaendigenAuftragSach.FormelleDeckung }
     *     
     */
    public void setFormelleDeckung(SachverstaendigenAuftragSach.FormelleDeckung value) {
        this.formelleDeckung = value;
    }

    /**
     * Ruft den Wert der schadenreserve-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachverstaendigenAuftragSach.Schadenreserve }
     *     
     */
    public SachverstaendigenAuftragSach.Schadenreserve getSchadenreserve() {
        return schadenreserve;
    }

    /**
     * Legt den Wert der schadenreserve-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachverstaendigenAuftragSach.Schadenreserve }
     *     
     */
    public void setSchadenreserve(SachverstaendigenAuftragSach.Schadenreserve value) {
        this.schadenreserve = value;
    }

    /**
     * Ruft den Wert der abspracheMitVN-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbspracheMitVN() {
        return abspracheMitVN;
    }

    /**
     * Legt den Wert der abspracheMitVN-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbspracheMitVN(String value) {
        this.abspracheMitVN = value;
    }

    /**
     * Ruft den Wert der zahlungsempfaengerAngebot-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachverstaendigenAuftragSach.ZahlungsempfaengerAngebot }
     *     
     */
    public SachverstaendigenAuftragSach.ZahlungsempfaengerAngebot getZahlungsempfaengerAngebot() {
        return zahlungsempfaengerAngebot;
    }

    /**
     * Legt den Wert der zahlungsempfaengerAngebot-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachverstaendigenAuftragSach.ZahlungsempfaengerAngebot }
     *     
     */
    public void setZahlungsempfaengerAngebot(SachverstaendigenAuftragSach.ZahlungsempfaengerAngebot value) {
        this.zahlungsempfaengerAngebot = value;
    }

    /**
     * Ruft den Wert der zahlungsempfaengerRechnung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachverstaendigenAuftragSach.ZahlungsempfaengerRechnung }
     *     
     */
    public SachverstaendigenAuftragSach.ZahlungsempfaengerRechnung getZahlungsempfaengerRechnung() {
        return zahlungsempfaengerRechnung;
    }

    /**
     * Legt den Wert der zahlungsempfaengerRechnung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachverstaendigenAuftragSach.ZahlungsempfaengerRechnung }
     *     
     */
    public void setZahlungsempfaengerRechnung(SachverstaendigenAuftragSach.ZahlungsempfaengerRechnung value) {
        this.zahlungsempfaengerRechnung = value;
    }

    /**
     * Ruft den Wert der referenzierungBesichtigungsort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachverstaendigenAuftragSach.ReferenzierungBesichtigungsort }
     *     
     */
    public SachverstaendigenAuftragSach.ReferenzierungBesichtigungsort getReferenzierungBesichtigungsort() {
        return referenzierungBesichtigungsort;
    }

    /**
     * Legt den Wert der referenzierungBesichtigungsort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachverstaendigenAuftragSach.ReferenzierungBesichtigungsort }
     *     
     */
    public void setReferenzierungBesichtigungsort(SachverstaendigenAuftragSach.ReferenzierungBesichtigungsort value) {
        this.referenzierungBesichtigungsort = value;
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
     * Ruft den Wert der reguliererTaetig-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachverstaendigenAuftragSach.ReguliererTaetig }
     *     
     */
    public SachverstaendigenAuftragSach.ReguliererTaetig getReguliererTaetig() {
        return reguliererTaetig;
    }

    /**
     * Legt den Wert der reguliererTaetig-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachverstaendigenAuftragSach.ReguliererTaetig }
     *     
     */
    public void setReguliererTaetig(SachverstaendigenAuftragSach.ReguliererTaetig value) {
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
            return "4812";
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
     *         &lt;element name="Auftragsart1" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp"/>
     *         &lt;element name="Auftragsart2" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart3" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart4" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart5" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart6" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart7" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart8" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart9" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart10" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart11" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart12" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart13" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart14" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart15" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart16" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart17" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart18" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart19" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart20" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeSachTyp" minOccurs="0"/>
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
    public static class Auftraege {

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
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinUnklarTyp">
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
    public static class FormelleDeckung {

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
    public static class ReferenzierungBesichtigungsort {

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
    public static class ZahlungsempfaengerRechnung {

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
