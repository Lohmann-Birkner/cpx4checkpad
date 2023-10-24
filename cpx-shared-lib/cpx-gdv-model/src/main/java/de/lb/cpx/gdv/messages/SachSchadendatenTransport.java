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
 *         &lt;element name="lfdNrBeschaedigtenSache" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NrVersAnmeldungGueter" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DatumVersAnmeldungGueter" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="VersichertesGut" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AnzahlKolli" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GewichtInKg" minOccurs="0">
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
 *         &lt;element name="Verpackung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Befoerderungsmittel" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AKZEigenesTransportfahrzeug" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>Text015Typ">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Abgangsort" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Bestimmungsort" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BeginndatumTransport" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BeginnzeitTransport" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *               &lt;minLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AblieferungsdatumTransport" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AblieferungszeitTransport" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *               &lt;minLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp"/>
 *         &lt;element name="GesamtwertVersichertesGutInWE" minOccurs="0">
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
 *         &lt;element name="PartnerreferenzEntdecktDurch" minOccurs="0">
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
 *         &lt;element name="ReferenzierungBesichtigungsortGueter" minOccurs="0">
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
 *         &lt;element name="ReparaturMoeglich" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="BegruendungWennReparaturNichtMoeglichIst" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="KostenvoranschlagBeigefuegt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="HoeheKostenvoranschlagInWE" minOccurs="0">
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
 *                   &lt;element name="Indikator">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>IndikatorTyp">
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
 *         &lt;element name="BefestigungSicherungTransportgurt" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ReferenzierungBefestigungDurchgefuehrt" minOccurs="0">
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
 *         &lt;element name="TransportgutBeiAblieferungBeschaedigt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="VerpackungBeiAblieferungBeschaedigt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="BeschreibungAeusserlicheBeschaedigung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SchadenVorBeiAblieferungFestgestellt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="SchadenVomFrachtfuehrerBestaetigt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="SchriftlicheBestaetigungVorhanden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinNachgereichtTyp" minOccurs="0"/>
 *         &lt;element name="ReferenzierungVerkehrstraeger" minOccurs="0">
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
 *         &lt;element name="RegressanspruecheGegenVerkehrstraeger" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="RegressanspruecheDatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RegressanspruecheVorgehen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}MuendlichSchriftlichTyp" minOccurs="0"/>
 *         &lt;element name="AnspruchschreibenAntwort" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinNachgereichtTyp" minOccurs="0"/>
 *         &lt;element name="VerantwortlichkeitDritter" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="HaftbarmachungDritterVorgehen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}MuendlichSchriftlichTyp" minOccurs="0"/>
 *         &lt;element name="HaftungschreibenAntwort" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinNachgereichtTyp" minOccurs="0"/>
 *         &lt;element name="HavariekommissarSachverstaendigerBeauftragt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Beauftragungsdatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="FruehererTransportschadenVorhanden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="BeschreibungWannDerFruehereTransportschadenEntstandenIst" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4285">
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
    "lfdNrBeschaedigtenSache",
    "nrVersAnmeldungGueter",
    "datumVersAnmeldungGueter",
    "versichertesGut",
    "anzahlKolli",
    "gewichtInKg",
    "verpackung",
    "befoerderungsmittel",
    "akzEigenesTransportfahrzeug",
    "abgangsort",
    "bestimmungsort",
    "beginndatumTransport",
    "beginnzeitTransport",
    "ablieferungsdatumTransport",
    "ablieferungszeitTransport",
    "waehrungsschluessel",
    "gesamtwertVersichertesGutInWE",
    "partnerreferenzEntdecktDurch",
    "referenzierungBesichtigungsortGueter",
    "reparaturMoeglich",
    "begruendungWennReparaturNichtMoeglichIst",
    "kostenvoranschlagBeigefuegt",
    "hoeheKostenvoranschlagInWE",
    "befestigungSicherungTransportgurt",
    "referenzierungBefestigungDurchgefuehrt",
    "transportgutBeiAblieferungBeschaedigt",
    "verpackungBeiAblieferungBeschaedigt",
    "beschreibungAeusserlicheBeschaedigung",
    "schadenVorBeiAblieferungFestgestellt",
    "schadenVomFrachtfuehrerBestaetigt",
    "schriftlicheBestaetigungVorhanden",
    "referenzierungVerkehrstraeger",
    "regressanspruecheGegenVerkehrstraeger",
    "regressanspruecheDatum",
    "regressanspruecheVorgehen",
    "anspruchschreibenAntwort",
    "verantwortlichkeitDritter",
    "haftbarmachungDritterVorgehen",
    "haftungschreibenAntwort",
    "havariekommissarSachverstaendigerBeauftragt",
    "beauftragungsdatum",
    "fruehererTransportschadenVorhanden",
    "beschreibungWannDerFruehereTransportschadenEntstandenIst"
})
@XmlRootElement(name = "SachSchadendatenTransport")
public class SachSchadendatenTransport {

    protected String lfdNrBeschaedigtenSache;
    @XmlElement(name = "NrVersAnmeldungGueter")
    protected String nrVersAnmeldungGueter;
    @XmlElement(name = "DatumVersAnmeldungGueter")
    protected String datumVersAnmeldungGueter;
    @XmlElement(name = "VersichertesGut")
    protected String versichertesGut;
    @XmlElement(name = "AnzahlKolli")
    protected String anzahlKolli;
    @XmlElement(name = "GewichtInKg")
    protected SachSchadendatenTransport.GewichtInKg gewichtInKg;
    @XmlElement(name = "Verpackung")
    protected String verpackung;
    @XmlElement(name = "Befoerderungsmittel")
    protected String befoerderungsmittel;
    @XmlElement(name = "AKZEigenesTransportfahrzeug")
    protected SachSchadendatenTransport.AKZEigenesTransportfahrzeug akzEigenesTransportfahrzeug;
    @XmlElement(name = "Abgangsort")
    protected String abgangsort;
    @XmlElement(name = "Bestimmungsort")
    protected String bestimmungsort;
    @XmlElement(name = "BeginndatumTransport")
    protected String beginndatumTransport;
    @XmlElement(name = "BeginnzeitTransport")
    protected String beginnzeitTransport;
    @XmlElement(name = "AblieferungsdatumTransport")
    protected String ablieferungsdatumTransport;
    @XmlElement(name = "AblieferungszeitTransport")
    protected String ablieferungszeitTransport;
    @XmlElement(name = "Waehrungsschluessel", required = true)
    protected String waehrungsschluessel;
    @XmlElement(name = "GesamtwertVersichertesGutInWE")
    protected SachSchadendatenTransport.GesamtwertVersichertesGutInWE gesamtwertVersichertesGutInWE;
    @XmlElement(name = "PartnerreferenzEntdecktDurch")
    protected SachSchadendatenTransport.PartnerreferenzEntdecktDurch partnerreferenzEntdecktDurch;
    @XmlElement(name = "ReferenzierungBesichtigungsortGueter")
    protected SachSchadendatenTransport.ReferenzierungBesichtigungsortGueter referenzierungBesichtigungsortGueter;
    @XmlElement(name = "ReparaturMoeglich")
    protected String reparaturMoeglich;
    @XmlElement(name = "BegruendungWennReparaturNichtMoeglichIst")
    protected String begruendungWennReparaturNichtMoeglichIst;
    @XmlElement(name = "KostenvoranschlagBeigefuegt")
    protected String kostenvoranschlagBeigefuegt;
    @XmlElement(name = "HoeheKostenvoranschlagInWE")
    protected SachSchadendatenTransport.HoeheKostenvoranschlagInWE hoeheKostenvoranschlagInWE;
    @XmlElement(name = "BefestigungSicherungTransportgurt")
    protected String befestigungSicherungTransportgurt;
    @XmlElement(name = "ReferenzierungBefestigungDurchgefuehrt")
    protected SachSchadendatenTransport.ReferenzierungBefestigungDurchgefuehrt referenzierungBefestigungDurchgefuehrt;
    @XmlElement(name = "TransportgutBeiAblieferungBeschaedigt")
    protected String transportgutBeiAblieferungBeschaedigt;
    @XmlElement(name = "VerpackungBeiAblieferungBeschaedigt")
    protected String verpackungBeiAblieferungBeschaedigt;
    @XmlElement(name = "BeschreibungAeusserlicheBeschaedigung")
    protected String beschreibungAeusserlicheBeschaedigung;
    @XmlElement(name = "SchadenVorBeiAblieferungFestgestellt")
    protected String schadenVorBeiAblieferungFestgestellt;
    @XmlElement(name = "SchadenVomFrachtfuehrerBestaetigt")
    protected String schadenVomFrachtfuehrerBestaetigt;
    @XmlElement(name = "SchriftlicheBestaetigungVorhanden")
    protected String schriftlicheBestaetigungVorhanden;
    @XmlElement(name = "ReferenzierungVerkehrstraeger")
    protected SachSchadendatenTransport.ReferenzierungVerkehrstraeger referenzierungVerkehrstraeger;
    @XmlElement(name = "RegressanspruecheGegenVerkehrstraeger")
    protected String regressanspruecheGegenVerkehrstraeger;
    @XmlElement(name = "RegressanspruecheDatum")
    protected String regressanspruecheDatum;
    @XmlElement(name = "RegressanspruecheVorgehen")
    protected String regressanspruecheVorgehen;
    @XmlElement(name = "AnspruchschreibenAntwort")
    protected String anspruchschreibenAntwort;
    @XmlElement(name = "VerantwortlichkeitDritter")
    protected String verantwortlichkeitDritter;
    @XmlElement(name = "HaftbarmachungDritterVorgehen")
    protected String haftbarmachungDritterVorgehen;
    @XmlElement(name = "HaftungschreibenAntwort")
    protected String haftungschreibenAntwort;
    @XmlElement(name = "HavariekommissarSachverstaendigerBeauftragt")
    protected String havariekommissarSachverstaendigerBeauftragt;
    @XmlElement(name = "Beauftragungsdatum")
    protected String beauftragungsdatum;
    @XmlElement(name = "FruehererTransportschadenVorhanden")
    protected String fruehererTransportschadenVorhanden;
    @XmlElement(name = "BeschreibungWannDerFruehereTransportschadenEntstandenIst")
    protected SachSchadendatenTransport.BeschreibungWannDerFruehereTransportschadenEntstandenIst beschreibungWannDerFruehereTransportschadenEntstandenIst;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der lfdNrBeschaedigtenSache-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLfdNrBeschaedigtenSache() {
        return lfdNrBeschaedigtenSache;
    }

    /**
     * Legt den Wert der lfdNrBeschaedigtenSache-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLfdNrBeschaedigtenSache(String value) {
        this.lfdNrBeschaedigtenSache = value;
    }

    /**
     * Ruft den Wert der nrVersAnmeldungGueter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNrVersAnmeldungGueter() {
        return nrVersAnmeldungGueter;
    }

    /**
     * Legt den Wert der nrVersAnmeldungGueter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNrVersAnmeldungGueter(String value) {
        this.nrVersAnmeldungGueter = value;
    }

    /**
     * Ruft den Wert der datumVersAnmeldungGueter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatumVersAnmeldungGueter() {
        return datumVersAnmeldungGueter;
    }

    /**
     * Legt den Wert der datumVersAnmeldungGueter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatumVersAnmeldungGueter(String value) {
        this.datumVersAnmeldungGueter = value;
    }

    /**
     * Ruft den Wert der versichertesGut-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersichertesGut() {
        return versichertesGut;
    }

    /**
     * Legt den Wert der versichertesGut-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersichertesGut(String value) {
        this.versichertesGut = value;
    }

    /**
     * Ruft den Wert der anzahlKolli-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnzahlKolli() {
        return anzahlKolli;
    }

    /**
     * Legt den Wert der anzahlKolli-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnzahlKolli(String value) {
        this.anzahlKolli = value;
    }

    /**
     * Ruft den Wert der gewichtInKg-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenTransport.GewichtInKg }
     *     
     */
    public SachSchadendatenTransport.GewichtInKg getGewichtInKg() {
        return gewichtInKg;
    }

    /**
     * Legt den Wert der gewichtInKg-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenTransport.GewichtInKg }
     *     
     */
    public void setGewichtInKg(SachSchadendatenTransport.GewichtInKg value) {
        this.gewichtInKg = value;
    }

    /**
     * Ruft den Wert der verpackung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerpackung() {
        return verpackung;
    }

    /**
     * Legt den Wert der verpackung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerpackung(String value) {
        this.verpackung = value;
    }

    /**
     * Ruft den Wert der befoerderungsmittel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBefoerderungsmittel() {
        return befoerderungsmittel;
    }

    /**
     * Legt den Wert der befoerderungsmittel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBefoerderungsmittel(String value) {
        this.befoerderungsmittel = value;
    }

    /**
     * Ruft den Wert der akzEigenesTransportfahrzeug-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenTransport.AKZEigenesTransportfahrzeug }
     *     
     */
    public SachSchadendatenTransport.AKZEigenesTransportfahrzeug getAKZEigenesTransportfahrzeug() {
        return akzEigenesTransportfahrzeug;
    }

    /**
     * Legt den Wert der akzEigenesTransportfahrzeug-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenTransport.AKZEigenesTransportfahrzeug }
     *     
     */
    public void setAKZEigenesTransportfahrzeug(SachSchadendatenTransport.AKZEigenesTransportfahrzeug value) {
        this.akzEigenesTransportfahrzeug = value;
    }

    /**
     * Ruft den Wert der abgangsort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbgangsort() {
        return abgangsort;
    }

    /**
     * Legt den Wert der abgangsort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbgangsort(String value) {
        this.abgangsort = value;
    }

    /**
     * Ruft den Wert der bestimmungsort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBestimmungsort() {
        return bestimmungsort;
    }

    /**
     * Legt den Wert der bestimmungsort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBestimmungsort(String value) {
        this.bestimmungsort = value;
    }

    /**
     * Ruft den Wert der beginndatumTransport-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeginndatumTransport() {
        return beginndatumTransport;
    }

    /**
     * Legt den Wert der beginndatumTransport-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeginndatumTransport(String value) {
        this.beginndatumTransport = value;
    }

    /**
     * Ruft den Wert der beginnzeitTransport-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeginnzeitTransport() {
        return beginnzeitTransport;
    }

    /**
     * Legt den Wert der beginnzeitTransport-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeginnzeitTransport(String value) {
        this.beginnzeitTransport = value;
    }

    /**
     * Ruft den Wert der ablieferungsdatumTransport-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAblieferungsdatumTransport() {
        return ablieferungsdatumTransport;
    }

    /**
     * Legt den Wert der ablieferungsdatumTransport-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAblieferungsdatumTransport(String value) {
        this.ablieferungsdatumTransport = value;
    }

    /**
     * Ruft den Wert der ablieferungszeitTransport-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAblieferungszeitTransport() {
        return ablieferungszeitTransport;
    }

    /**
     * Legt den Wert der ablieferungszeitTransport-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAblieferungszeitTransport(String value) {
        this.ablieferungszeitTransport = value;
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
     * Ruft den Wert der gesamtwertVersichertesGutInWE-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenTransport.GesamtwertVersichertesGutInWE }
     *     
     */
    public SachSchadendatenTransport.GesamtwertVersichertesGutInWE getGesamtwertVersichertesGutInWE() {
        return gesamtwertVersichertesGutInWE;
    }

    /**
     * Legt den Wert der gesamtwertVersichertesGutInWE-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenTransport.GesamtwertVersichertesGutInWE }
     *     
     */
    public void setGesamtwertVersichertesGutInWE(SachSchadendatenTransport.GesamtwertVersichertesGutInWE value) {
        this.gesamtwertVersichertesGutInWE = value;
    }

    /**
     * Ruft den Wert der partnerreferenzEntdecktDurch-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenTransport.PartnerreferenzEntdecktDurch }
     *     
     */
    public SachSchadendatenTransport.PartnerreferenzEntdecktDurch getPartnerreferenzEntdecktDurch() {
        return partnerreferenzEntdecktDurch;
    }

    /**
     * Legt den Wert der partnerreferenzEntdecktDurch-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenTransport.PartnerreferenzEntdecktDurch }
     *     
     */
    public void setPartnerreferenzEntdecktDurch(SachSchadendatenTransport.PartnerreferenzEntdecktDurch value) {
        this.partnerreferenzEntdecktDurch = value;
    }

    /**
     * Ruft den Wert der referenzierungBesichtigungsortGueter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenTransport.ReferenzierungBesichtigungsortGueter }
     *     
     */
    public SachSchadendatenTransport.ReferenzierungBesichtigungsortGueter getReferenzierungBesichtigungsortGueter() {
        return referenzierungBesichtigungsortGueter;
    }

    /**
     * Legt den Wert der referenzierungBesichtigungsortGueter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenTransport.ReferenzierungBesichtigungsortGueter }
     *     
     */
    public void setReferenzierungBesichtigungsortGueter(SachSchadendatenTransport.ReferenzierungBesichtigungsortGueter value) {
        this.referenzierungBesichtigungsortGueter = value;
    }

    /**
     * Ruft den Wert der reparaturMoeglich-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReparaturMoeglich() {
        return reparaturMoeglich;
    }

    /**
     * Legt den Wert der reparaturMoeglich-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReparaturMoeglich(String value) {
        this.reparaturMoeglich = value;
    }

    /**
     * Ruft den Wert der begruendungWennReparaturNichtMoeglichIst-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBegruendungWennReparaturNichtMoeglichIst() {
        return begruendungWennReparaturNichtMoeglichIst;
    }

    /**
     * Legt den Wert der begruendungWennReparaturNichtMoeglichIst-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBegruendungWennReparaturNichtMoeglichIst(String value) {
        this.begruendungWennReparaturNichtMoeglichIst = value;
    }

    /**
     * Ruft den Wert der kostenvoranschlagBeigefuegt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKostenvoranschlagBeigefuegt() {
        return kostenvoranschlagBeigefuegt;
    }

    /**
     * Legt den Wert der kostenvoranschlagBeigefuegt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKostenvoranschlagBeigefuegt(String value) {
        this.kostenvoranschlagBeigefuegt = value;
    }

    /**
     * Ruft den Wert der hoeheKostenvoranschlagInWE-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenTransport.HoeheKostenvoranschlagInWE }
     *     
     */
    public SachSchadendatenTransport.HoeheKostenvoranschlagInWE getHoeheKostenvoranschlagInWE() {
        return hoeheKostenvoranschlagInWE;
    }

    /**
     * Legt den Wert der hoeheKostenvoranschlagInWE-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenTransport.HoeheKostenvoranschlagInWE }
     *     
     */
    public void setHoeheKostenvoranschlagInWE(SachSchadendatenTransport.HoeheKostenvoranschlagInWE value) {
        this.hoeheKostenvoranschlagInWE = value;
    }

    /**
     * Ruft den Wert der befestigungSicherungTransportgurt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBefestigungSicherungTransportgurt() {
        return befestigungSicherungTransportgurt;
    }

    /**
     * Legt den Wert der befestigungSicherungTransportgurt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBefestigungSicherungTransportgurt(String value) {
        this.befestigungSicherungTransportgurt = value;
    }

    /**
     * Ruft den Wert der referenzierungBefestigungDurchgefuehrt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenTransport.ReferenzierungBefestigungDurchgefuehrt }
     *     
     */
    public SachSchadendatenTransport.ReferenzierungBefestigungDurchgefuehrt getReferenzierungBefestigungDurchgefuehrt() {
        return referenzierungBefestigungDurchgefuehrt;
    }

    /**
     * Legt den Wert der referenzierungBefestigungDurchgefuehrt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenTransport.ReferenzierungBefestigungDurchgefuehrt }
     *     
     */
    public void setReferenzierungBefestigungDurchgefuehrt(SachSchadendatenTransport.ReferenzierungBefestigungDurchgefuehrt value) {
        this.referenzierungBefestigungDurchgefuehrt = value;
    }

    /**
     * Ruft den Wert der transportgutBeiAblieferungBeschaedigt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransportgutBeiAblieferungBeschaedigt() {
        return transportgutBeiAblieferungBeschaedigt;
    }

    /**
     * Legt den Wert der transportgutBeiAblieferungBeschaedigt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransportgutBeiAblieferungBeschaedigt(String value) {
        this.transportgutBeiAblieferungBeschaedigt = value;
    }

    /**
     * Ruft den Wert der verpackungBeiAblieferungBeschaedigt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerpackungBeiAblieferungBeschaedigt() {
        return verpackungBeiAblieferungBeschaedigt;
    }

    /**
     * Legt den Wert der verpackungBeiAblieferungBeschaedigt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerpackungBeiAblieferungBeschaedigt(String value) {
        this.verpackungBeiAblieferungBeschaedigt = value;
    }

    /**
     * Ruft den Wert der beschreibungAeusserlicheBeschaedigung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeschreibungAeusserlicheBeschaedigung() {
        return beschreibungAeusserlicheBeschaedigung;
    }

    /**
     * Legt den Wert der beschreibungAeusserlicheBeschaedigung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeschreibungAeusserlicheBeschaedigung(String value) {
        this.beschreibungAeusserlicheBeschaedigung = value;
    }

    /**
     * Ruft den Wert der schadenVorBeiAblieferungFestgestellt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenVorBeiAblieferungFestgestellt() {
        return schadenVorBeiAblieferungFestgestellt;
    }

    /**
     * Legt den Wert der schadenVorBeiAblieferungFestgestellt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenVorBeiAblieferungFestgestellt(String value) {
        this.schadenVorBeiAblieferungFestgestellt = value;
    }

    /**
     * Ruft den Wert der schadenVomFrachtfuehrerBestaetigt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenVomFrachtfuehrerBestaetigt() {
        return schadenVomFrachtfuehrerBestaetigt;
    }

    /**
     * Legt den Wert der schadenVomFrachtfuehrerBestaetigt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenVomFrachtfuehrerBestaetigt(String value) {
        this.schadenVomFrachtfuehrerBestaetigt = value;
    }

    /**
     * Ruft den Wert der schriftlicheBestaetigungVorhanden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchriftlicheBestaetigungVorhanden() {
        return schriftlicheBestaetigungVorhanden;
    }

    /**
     * Legt den Wert der schriftlicheBestaetigungVorhanden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchriftlicheBestaetigungVorhanden(String value) {
        this.schriftlicheBestaetigungVorhanden = value;
    }

    /**
     * Ruft den Wert der referenzierungVerkehrstraeger-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenTransport.ReferenzierungVerkehrstraeger }
     *     
     */
    public SachSchadendatenTransport.ReferenzierungVerkehrstraeger getReferenzierungVerkehrstraeger() {
        return referenzierungVerkehrstraeger;
    }

    /**
     * Legt den Wert der referenzierungVerkehrstraeger-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenTransport.ReferenzierungVerkehrstraeger }
     *     
     */
    public void setReferenzierungVerkehrstraeger(SachSchadendatenTransport.ReferenzierungVerkehrstraeger value) {
        this.referenzierungVerkehrstraeger = value;
    }

    /**
     * Ruft den Wert der regressanspruecheGegenVerkehrstraeger-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegressanspruecheGegenVerkehrstraeger() {
        return regressanspruecheGegenVerkehrstraeger;
    }

    /**
     * Legt den Wert der regressanspruecheGegenVerkehrstraeger-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegressanspruecheGegenVerkehrstraeger(String value) {
        this.regressanspruecheGegenVerkehrstraeger = value;
    }

    /**
     * Ruft den Wert der regressanspruecheDatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegressanspruecheDatum() {
        return regressanspruecheDatum;
    }

    /**
     * Legt den Wert der regressanspruecheDatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegressanspruecheDatum(String value) {
        this.regressanspruecheDatum = value;
    }

    /**
     * Ruft den Wert der regressanspruecheVorgehen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegressanspruecheVorgehen() {
        return regressanspruecheVorgehen;
    }

    /**
     * Legt den Wert der regressanspruecheVorgehen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegressanspruecheVorgehen(String value) {
        this.regressanspruecheVorgehen = value;
    }

    /**
     * Ruft den Wert der anspruchschreibenAntwort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnspruchschreibenAntwort() {
        return anspruchschreibenAntwort;
    }

    /**
     * Legt den Wert der anspruchschreibenAntwort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnspruchschreibenAntwort(String value) {
        this.anspruchschreibenAntwort = value;
    }

    /**
     * Ruft den Wert der verantwortlichkeitDritter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerantwortlichkeitDritter() {
        return verantwortlichkeitDritter;
    }

    /**
     * Legt den Wert der verantwortlichkeitDritter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerantwortlichkeitDritter(String value) {
        this.verantwortlichkeitDritter = value;
    }

    /**
     * Ruft den Wert der haftbarmachungDritterVorgehen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHaftbarmachungDritterVorgehen() {
        return haftbarmachungDritterVorgehen;
    }

    /**
     * Legt den Wert der haftbarmachungDritterVorgehen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHaftbarmachungDritterVorgehen(String value) {
        this.haftbarmachungDritterVorgehen = value;
    }

    /**
     * Ruft den Wert der haftungschreibenAntwort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHaftungschreibenAntwort() {
        return haftungschreibenAntwort;
    }

    /**
     * Legt den Wert der haftungschreibenAntwort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHaftungschreibenAntwort(String value) {
        this.haftungschreibenAntwort = value;
    }

    /**
     * Ruft den Wert der havariekommissarSachverstaendigerBeauftragt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHavariekommissarSachverstaendigerBeauftragt() {
        return havariekommissarSachverstaendigerBeauftragt;
    }

    /**
     * Legt den Wert der havariekommissarSachverstaendigerBeauftragt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHavariekommissarSachverstaendigerBeauftragt(String value) {
        this.havariekommissarSachverstaendigerBeauftragt = value;
    }

    /**
     * Ruft den Wert der beauftragungsdatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeauftragungsdatum() {
        return beauftragungsdatum;
    }

    /**
     * Legt den Wert der beauftragungsdatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeauftragungsdatum(String value) {
        this.beauftragungsdatum = value;
    }

    /**
     * Ruft den Wert der fruehererTransportschadenVorhanden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFruehererTransportschadenVorhanden() {
        return fruehererTransportschadenVorhanden;
    }

    /**
     * Legt den Wert der fruehererTransportschadenVorhanden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFruehererTransportschadenVorhanden(String value) {
        this.fruehererTransportschadenVorhanden = value;
    }

    /**
     * Ruft den Wert der beschreibungWannDerFruehereTransportschadenEntstandenIst-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenTransport.BeschreibungWannDerFruehereTransportschadenEntstandenIst }
     *     
     */
    public SachSchadendatenTransport.BeschreibungWannDerFruehereTransportschadenEntstandenIst getBeschreibungWannDerFruehereTransportschadenEntstandenIst() {
        return beschreibungWannDerFruehereTransportschadenEntstandenIst;
    }

    /**
     * Legt den Wert der beschreibungWannDerFruehereTransportschadenEntstandenIst-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenTransport.BeschreibungWannDerFruehereTransportschadenEntstandenIst }
     *     
     */
    public void setBeschreibungWannDerFruehereTransportschadenEntstandenIst(SachSchadendatenTransport.BeschreibungWannDerFruehereTransportschadenEntstandenIst value) {
        this.beschreibungWannDerFruehereTransportschadenEntstandenIst = value;
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
            return "4285";
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
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>Text015Typ">
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
    public static class AKZEigenesTransportfahrzeug {

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
    public static class BeschreibungWannDerFruehereTransportschadenEntstandenIst
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
    public static class GesamtwertVersichertesGutInWE {

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
    public static class GewichtInKg {

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
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="9999999999.99"/>
     *               &lt;totalDigits value="12"/>
     *               &lt;fractionDigits value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Indikator">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>IndikatorTyp">
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
        "wert",
        "indikator"
    })
    public static class HoeheKostenvoranschlagInWE {

        @XmlElement(name = "Wert")
        protected BigDecimal wert;
        @XmlElement(name = "Indikator", required = true)
        protected SachSchadendatenTransport.HoeheKostenvoranschlagInWE.Indikator indikator;

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
         *     {@link SachSchadendatenTransport.HoeheKostenvoranschlagInWE.Indikator }
         *     
         */
        public SachSchadendatenTransport.HoeheKostenvoranschlagInWE.Indikator getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SachSchadendatenTransport.HoeheKostenvoranschlagInWE.Indikator }
         *     
         */
        public void setIndikator(SachSchadendatenTransport.HoeheKostenvoranschlagInWE.Indikator value) {
            this.indikator = value;
        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>IndikatorTyp">
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
        public static class Indikator {

            @XmlValue
            protected String value;
            @XmlAttribute(name = "Satzende")
            protected String satzende;

            /**
             * Gibt über das Vorzeichen der Zahl im vorangehenden Feld Auskunft bzw. zeigt an, dass das Feld beim Absender nicht belegt wurde.
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
    public static class PartnerreferenzEntdecktDurch {

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
    public static class ReferenzierungBefestigungDurchgefuehrt {

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
    public static class ReferenzierungBesichtigungsortGueter {

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
    public static class ReferenzierungVerkehrstraeger {

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
