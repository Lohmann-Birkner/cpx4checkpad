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
 *         &lt;element name="lfdNrBeschaedigtenSache" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="0"/>
 *               &lt;totalDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ReferenzierungEigentuemer" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Adresskennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
 *                   &lt;element name="Partnerreferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
 *         &lt;element name="WertderSache" minOccurs="0">
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
 *         &lt;element name="Anschaffungsdatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Anschaffungspreis" minOccurs="0">
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
 *         &lt;element name="Preisangabe" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PreisangabeTyp" minOccurs="0"/>
 *         &lt;element name="Art" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SchadenobjektTyp" minOccurs="0"/>
 *         &lt;element name="Warengruppe">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Produktgruppe">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Herstellername" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ModellTypname" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="IDNummer" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Merkmal1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Merkmal2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Merkmal3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Merkmal4" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Merkmal5" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Merkmal6" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Merkmal7" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Merkmal8" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Merkmal9" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Merkmal10" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="InformationUeberBegutachtung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}InfoUeberBegutachtungTyp" minOccurs="0"/>
 *         &lt;element name="PruefungVorOrt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="VersandDerSache" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VersandDerSacheTyp" minOccurs="0"/>
 *         &lt;element name="Versandart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VersandartTyp" minOccurs="0"/>
 *         &lt;element name="VerbleibDerSache" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VerbleibDerSacheTyp" minOccurs="0"/>
 *         &lt;element name="Vorsteuerabzugsberechtigung" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
 *         &lt;element name="Wertverbesserungsdatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Wertverbesserungsart" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Beschreibung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="180"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZustandBeschaedigteSache" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>ZustandBeschaedigteSacheTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Umfang" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ErforderlicheMassnahme" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ErforderlicheMassnahmeTyp" minOccurs="0"/>
 *         &lt;element name="Sanierungsmassnahme1" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *         &lt;element name="Sanierungsmassnahme2" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *         &lt;element name="Sanierungsmassnahme3" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *         &lt;element name="Sanierungsmassnahme4" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *         &lt;element name="Sanierungsmassnahme5" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *         &lt;element name="Sanierungsmassnahme6" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartBehebungTyp" minOccurs="0"/>
 *         &lt;element name="ZurFreienVerwendung1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZurFreienVerwendung2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ArtBeschaedigteVerglasung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ArtBeschaedigteVerglasungTyp" minOccurs="0"/>
 *         &lt;element name="ArtVerglasung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="60"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MieteBeschaedigteSache" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="MieteLeiheVon" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MieteLeiheBis" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4230">
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
    "lfdNrBeschaedigtenSache",
    "referenzierungEigentuemer",
    "waehrungsschluessel",
    "wertderSache",
    "anschaffungsdatum",
    "anschaffungspreis",
    "preisangabe",
    "art",
    "warengruppe",
    "produktgruppe",
    "herstellername",
    "modellTypname",
    "idNummer",
    "merkmal1",
    "merkmal2",
    "merkmal3",
    "merkmal4",
    "merkmal5",
    "merkmal6",
    "merkmal7",
    "merkmal8",
    "merkmal9",
    "merkmal10",
    "informationUeberBegutachtung",
    "pruefungVorOrt",
    "versandDerSache",
    "versandart",
    "verbleibDerSache",
    "vorsteuerabzugsberechtigung",
    "schadenhoehe",
    "wertverbesserungsdatum",
    "wertverbesserungsart",
    "beschreibung",
    "zustandBeschaedigteSache",
    "umfang",
    "erforderlicheMassnahme",
    "sanierungsmassnahme1",
    "sanierungsmassnahme2",
    "sanierungsmassnahme3",
    "sanierungsmassnahme4",
    "sanierungsmassnahme5",
    "sanierungsmassnahme6",
    "zurFreienVerwendung1",
    "zurFreienVerwendung2",
    "artBeschaedigteVerglasung",
    "artVerglasung",
    "mieteBeschaedigteSache",
    "mieteLeiheVon",
    "mieteLeiheBis"
})
@XmlRootElement(name = "BeschaedigteSache")
public class BeschaedigteSache {

    protected BigInteger lfdNrBeschaedigtenSache;
    @XmlElement(name = "ReferenzierungEigentuemer")
    protected BeschaedigteSache.ReferenzierungEigentuemer referenzierungEigentuemer;
    @XmlElement(name = "Waehrungsschluessel")
    protected String waehrungsschluessel;
    @XmlElement(name = "WertderSache")
    protected BeschaedigteSache.WertderSache wertderSache;
    @XmlElement(name = "Anschaffungsdatum")
    protected String anschaffungsdatum;
    @XmlElement(name = "Anschaffungspreis")
    protected BeschaedigteSache.Anschaffungspreis anschaffungspreis;
    @XmlElement(name = "Preisangabe")
    protected String preisangabe;
    @XmlElement(name = "Art")
    protected String art;
    @XmlElement(name = "Warengruppe", required = true)
    protected String warengruppe;
    @XmlElement(name = "Produktgruppe", required = true)
    protected String produktgruppe;
    @XmlElement(name = "Herstellername")
    protected String herstellername;
    @XmlElement(name = "ModellTypname")
    protected String modellTypname;
    @XmlElement(name = "IDNummer")
    protected BeschaedigteSache.IDNummer idNummer;
    @XmlElement(name = "Merkmal1", required = true)
    protected String merkmal1;
    @XmlElement(name = "Merkmal2")
    protected String merkmal2;
    @XmlElement(name = "Merkmal3")
    protected String merkmal3;
    @XmlElement(name = "Merkmal4")
    protected String merkmal4;
    @XmlElement(name = "Merkmal5")
    protected String merkmal5;
    @XmlElement(name = "Merkmal6")
    protected BeschaedigteSache.Merkmal6 merkmal6;
    @XmlElement(name = "Merkmal7")
    protected String merkmal7;
    @XmlElement(name = "Merkmal8")
    protected String merkmal8;
    @XmlElement(name = "Merkmal9")
    protected String merkmal9;
    @XmlElement(name = "Merkmal10")
    protected String merkmal10;
    @XmlElement(name = "InformationUeberBegutachtung")
    protected String informationUeberBegutachtung;
    @XmlElement(name = "PruefungVorOrt")
    protected String pruefungVorOrt;
    @XmlElement(name = "VersandDerSache")
    protected String versandDerSache;
    @XmlElement(name = "Versandart")
    protected String versandart;
    @XmlElement(name = "VerbleibDerSache")
    protected String verbleibDerSache;
    @XmlElement(name = "Vorsteuerabzugsberechtigung")
    protected BeschaedigteSache.Vorsteuerabzugsberechtigung vorsteuerabzugsberechtigung;
    @XmlElement(name = "Schadenhoehe")
    protected BeschaedigteSache.Schadenhoehe schadenhoehe;
    @XmlElement(name = "Wertverbesserungsdatum")
    protected String wertverbesserungsdatum;
    @XmlElement(name = "Wertverbesserungsart")
    protected BeschaedigteSache.Wertverbesserungsart wertverbesserungsart;
    @XmlElement(name = "Beschreibung")
    protected String beschreibung;
    @XmlElement(name = "ZustandBeschaedigteSache")
    protected BeschaedigteSache.ZustandBeschaedigteSache zustandBeschaedigteSache;
    @XmlElement(name = "Umfang")
    protected BeschaedigteSache.Umfang umfang;
    @XmlElement(name = "ErforderlicheMassnahme")
    protected String erforderlicheMassnahme;
    @XmlElement(name = "Sanierungsmassnahme1")
    protected String sanierungsmassnahme1;
    @XmlElement(name = "Sanierungsmassnahme2")
    protected String sanierungsmassnahme2;
    @XmlElement(name = "Sanierungsmassnahme3")
    protected String sanierungsmassnahme3;
    @XmlElement(name = "Sanierungsmassnahme4")
    protected String sanierungsmassnahme4;
    @XmlElement(name = "Sanierungsmassnahme5")
    protected String sanierungsmassnahme5;
    @XmlElement(name = "Sanierungsmassnahme6")
    protected String sanierungsmassnahme6;
    @XmlElement(name = "ZurFreienVerwendung1")
    protected String zurFreienVerwendung1;
    @XmlElement(name = "ZurFreienVerwendung2")
    protected String zurFreienVerwendung2;
    @XmlElement(name = "ArtBeschaedigteVerglasung")
    protected String artBeschaedigteVerglasung;
    @XmlElement(name = "ArtVerglasung")
    protected String artVerglasung;
    @XmlElement(name = "MieteBeschaedigteSache")
    protected String mieteBeschaedigteSache;
    @XmlElement(name = "MieteLeiheVon")
    protected String mieteLeiheVon;
    @XmlElement(name = "MieteLeiheBis")
    protected BeschaedigteSache.MieteLeiheBis mieteLeiheBis;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der lfdNrBeschaedigtenSache-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLfdNrBeschaedigtenSache() {
        return lfdNrBeschaedigtenSache;
    }

    /**
     * Legt den Wert der lfdNrBeschaedigtenSache-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLfdNrBeschaedigtenSache(BigInteger value) {
        this.lfdNrBeschaedigtenSache = value;
    }

    /**
     * Ruft den Wert der referenzierungEigentuemer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigteSache.ReferenzierungEigentuemer }
     *     
     */
    public BeschaedigteSache.ReferenzierungEigentuemer getReferenzierungEigentuemer() {
        return referenzierungEigentuemer;
    }

    /**
     * Legt den Wert der referenzierungEigentuemer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigteSache.ReferenzierungEigentuemer }
     *     
     */
    public void setReferenzierungEigentuemer(BeschaedigteSache.ReferenzierungEigentuemer value) {
        this.referenzierungEigentuemer = value;
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
     * Ruft den Wert der wertderSache-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigteSache.WertderSache }
     *     
     */
    public BeschaedigteSache.WertderSache getWertderSache() {
        return wertderSache;
    }

    /**
     * Legt den Wert der wertderSache-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigteSache.WertderSache }
     *     
     */
    public void setWertderSache(BeschaedigteSache.WertderSache value) {
        this.wertderSache = value;
    }

    /**
     * Ruft den Wert der anschaffungsdatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnschaffungsdatum() {
        return anschaffungsdatum;
    }

    /**
     * Legt den Wert der anschaffungsdatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnschaffungsdatum(String value) {
        this.anschaffungsdatum = value;
    }

    /**
     * Ruft den Wert der anschaffungspreis-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigteSache.Anschaffungspreis }
     *     
     */
    public BeschaedigteSache.Anschaffungspreis getAnschaffungspreis() {
        return anschaffungspreis;
    }

    /**
     * Legt den Wert der anschaffungspreis-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigteSache.Anschaffungspreis }
     *     
     */
    public void setAnschaffungspreis(BeschaedigteSache.Anschaffungspreis value) {
        this.anschaffungspreis = value;
    }

    /**
     * Ruft den Wert der preisangabe-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreisangabe() {
        return preisangabe;
    }

    /**
     * Legt den Wert der preisangabe-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreisangabe(String value) {
        this.preisangabe = value;
    }

    /**
     * Ruft den Wert der art-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArt() {
        return art;
    }

    /**
     * Legt den Wert der art-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArt(String value) {
        this.art = value;
    }

    /**
     * Ruft den Wert der warengruppe-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarengruppe() {
        return warengruppe;
    }

    /**
     * Legt den Wert der warengruppe-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarengruppe(String value) {
        this.warengruppe = value;
    }

    /**
     * Ruft den Wert der produktgruppe-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProduktgruppe() {
        return produktgruppe;
    }

    /**
     * Legt den Wert der produktgruppe-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProduktgruppe(String value) {
        this.produktgruppe = value;
    }

    /**
     * Ruft den Wert der herstellername-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHerstellername() {
        return herstellername;
    }

    /**
     * Legt den Wert der herstellername-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHerstellername(String value) {
        this.herstellername = value;
    }

    /**
     * Ruft den Wert der modellTypname-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModellTypname() {
        return modellTypname;
    }

    /**
     * Legt den Wert der modellTypname-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModellTypname(String value) {
        this.modellTypname = value;
    }

    /**
     * Ruft den Wert der idNummer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigteSache.IDNummer }
     *     
     */
    public BeschaedigteSache.IDNummer getIDNummer() {
        return idNummer;
    }

    /**
     * Legt den Wert der idNummer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigteSache.IDNummer }
     *     
     */
    public void setIDNummer(BeschaedigteSache.IDNummer value) {
        this.idNummer = value;
    }

    /**
     * Ruft den Wert der merkmal1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerkmal1() {
        return merkmal1;
    }

    /**
     * Legt den Wert der merkmal1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerkmal1(String value) {
        this.merkmal1 = value;
    }

    /**
     * Ruft den Wert der merkmal2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerkmal2() {
        return merkmal2;
    }

    /**
     * Legt den Wert der merkmal2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerkmal2(String value) {
        this.merkmal2 = value;
    }

    /**
     * Ruft den Wert der merkmal3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerkmal3() {
        return merkmal3;
    }

    /**
     * Legt den Wert der merkmal3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerkmal3(String value) {
        this.merkmal3 = value;
    }

    /**
     * Ruft den Wert der merkmal4-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerkmal4() {
        return merkmal4;
    }

    /**
     * Legt den Wert der merkmal4-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerkmal4(String value) {
        this.merkmal4 = value;
    }

    /**
     * Ruft den Wert der merkmal5-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerkmal5() {
        return merkmal5;
    }

    /**
     * Legt den Wert der merkmal5-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerkmal5(String value) {
        this.merkmal5 = value;
    }

    /**
     * Ruft den Wert der merkmal6-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigteSache.Merkmal6 }
     *     
     */
    public BeschaedigteSache.Merkmal6 getMerkmal6() {
        return merkmal6;
    }

    /**
     * Legt den Wert der merkmal6-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigteSache.Merkmal6 }
     *     
     */
    public void setMerkmal6(BeschaedigteSache.Merkmal6 value) {
        this.merkmal6 = value;
    }

    /**
     * Ruft den Wert der merkmal7-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerkmal7() {
        return merkmal7;
    }

    /**
     * Legt den Wert der merkmal7-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerkmal7(String value) {
        this.merkmal7 = value;
    }

    /**
     * Ruft den Wert der merkmal8-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerkmal8() {
        return merkmal8;
    }

    /**
     * Legt den Wert der merkmal8-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerkmal8(String value) {
        this.merkmal8 = value;
    }

    /**
     * Ruft den Wert der merkmal9-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerkmal9() {
        return merkmal9;
    }

    /**
     * Legt den Wert der merkmal9-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerkmal9(String value) {
        this.merkmal9 = value;
    }

    /**
     * Ruft den Wert der merkmal10-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerkmal10() {
        return merkmal10;
    }

    /**
     * Legt den Wert der merkmal10-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerkmal10(String value) {
        this.merkmal10 = value;
    }

    /**
     * Ruft den Wert der informationUeberBegutachtung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInformationUeberBegutachtung() {
        return informationUeberBegutachtung;
    }

    /**
     * Legt den Wert der informationUeberBegutachtung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInformationUeberBegutachtung(String value) {
        this.informationUeberBegutachtung = value;
    }

    /**
     * Ruft den Wert der pruefungVorOrt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPruefungVorOrt() {
        return pruefungVorOrt;
    }

    /**
     * Legt den Wert der pruefungVorOrt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPruefungVorOrt(String value) {
        this.pruefungVorOrt = value;
    }

    /**
     * Ruft den Wert der versandDerSache-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersandDerSache() {
        return versandDerSache;
    }

    /**
     * Legt den Wert der versandDerSache-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersandDerSache(String value) {
        this.versandDerSache = value;
    }

    /**
     * Ruft den Wert der versandart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersandart() {
        return versandart;
    }

    /**
     * Legt den Wert der versandart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersandart(String value) {
        this.versandart = value;
    }

    /**
     * Ruft den Wert der verbleibDerSache-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerbleibDerSache() {
        return verbleibDerSache;
    }

    /**
     * Legt den Wert der verbleibDerSache-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerbleibDerSache(String value) {
        this.verbleibDerSache = value;
    }

    /**
     * Ruft den Wert der vorsteuerabzugsberechtigung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigteSache.Vorsteuerabzugsberechtigung }
     *     
     */
    public BeschaedigteSache.Vorsteuerabzugsberechtigung getVorsteuerabzugsberechtigung() {
        return vorsteuerabzugsberechtigung;
    }

    /**
     * Legt den Wert der vorsteuerabzugsberechtigung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigteSache.Vorsteuerabzugsberechtigung }
     *     
     */
    public void setVorsteuerabzugsberechtigung(BeschaedigteSache.Vorsteuerabzugsberechtigung value) {
        this.vorsteuerabzugsberechtigung = value;
    }

    /**
     * Ruft den Wert der schadenhoehe-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigteSache.Schadenhoehe }
     *     
     */
    public BeschaedigteSache.Schadenhoehe getSchadenhoehe() {
        return schadenhoehe;
    }

    /**
     * Legt den Wert der schadenhoehe-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigteSache.Schadenhoehe }
     *     
     */
    public void setSchadenhoehe(BeschaedigteSache.Schadenhoehe value) {
        this.schadenhoehe = value;
    }

    /**
     * Ruft den Wert der wertverbesserungsdatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWertverbesserungsdatum() {
        return wertverbesserungsdatum;
    }

    /**
     * Legt den Wert der wertverbesserungsdatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWertverbesserungsdatum(String value) {
        this.wertverbesserungsdatum = value;
    }

    /**
     * Ruft den Wert der wertverbesserungsart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigteSache.Wertverbesserungsart }
     *     
     */
    public BeschaedigteSache.Wertverbesserungsart getWertverbesserungsart() {
        return wertverbesserungsart;
    }

    /**
     * Legt den Wert der wertverbesserungsart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigteSache.Wertverbesserungsart }
     *     
     */
    public void setWertverbesserungsart(BeschaedigteSache.Wertverbesserungsart value) {
        this.wertverbesserungsart = value;
    }

    /**
     * Ruft den Wert der beschreibung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeschreibung() {
        return beschreibung;
    }

    /**
     * Legt den Wert der beschreibung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeschreibung(String value) {
        this.beschreibung = value;
    }

    /**
     * Ruft den Wert der zustandBeschaedigteSache-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigteSache.ZustandBeschaedigteSache }
     *     
     */
    public BeschaedigteSache.ZustandBeschaedigteSache getZustandBeschaedigteSache() {
        return zustandBeschaedigteSache;
    }

    /**
     * Legt den Wert der zustandBeschaedigteSache-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigteSache.ZustandBeschaedigteSache }
     *     
     */
    public void setZustandBeschaedigteSache(BeschaedigteSache.ZustandBeschaedigteSache value) {
        this.zustandBeschaedigteSache = value;
    }

    /**
     * Ruft den Wert der umfang-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigteSache.Umfang }
     *     
     */
    public BeschaedigteSache.Umfang getUmfang() {
        return umfang;
    }

    /**
     * Legt den Wert der umfang-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigteSache.Umfang }
     *     
     */
    public void setUmfang(BeschaedigteSache.Umfang value) {
        this.umfang = value;
    }

    /**
     * Ruft den Wert der erforderlicheMassnahme-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErforderlicheMassnahme() {
        return erforderlicheMassnahme;
    }

    /**
     * Legt den Wert der erforderlicheMassnahme-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErforderlicheMassnahme(String value) {
        this.erforderlicheMassnahme = value;
    }

    /**
     * Ruft den Wert der sanierungsmassnahme1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSanierungsmassnahme1() {
        return sanierungsmassnahme1;
    }

    /**
     * Legt den Wert der sanierungsmassnahme1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSanierungsmassnahme1(String value) {
        this.sanierungsmassnahme1 = value;
    }

    /**
     * Ruft den Wert der sanierungsmassnahme2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSanierungsmassnahme2() {
        return sanierungsmassnahme2;
    }

    /**
     * Legt den Wert der sanierungsmassnahme2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSanierungsmassnahme2(String value) {
        this.sanierungsmassnahme2 = value;
    }

    /**
     * Ruft den Wert der sanierungsmassnahme3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSanierungsmassnahme3() {
        return sanierungsmassnahme3;
    }

    /**
     * Legt den Wert der sanierungsmassnahme3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSanierungsmassnahme3(String value) {
        this.sanierungsmassnahme3 = value;
    }

    /**
     * Ruft den Wert der sanierungsmassnahme4-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSanierungsmassnahme4() {
        return sanierungsmassnahme4;
    }

    /**
     * Legt den Wert der sanierungsmassnahme4-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSanierungsmassnahme4(String value) {
        this.sanierungsmassnahme4 = value;
    }

    /**
     * Ruft den Wert der sanierungsmassnahme5-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSanierungsmassnahme5() {
        return sanierungsmassnahme5;
    }

    /**
     * Legt den Wert der sanierungsmassnahme5-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSanierungsmassnahme5(String value) {
        this.sanierungsmassnahme5 = value;
    }

    /**
     * Ruft den Wert der sanierungsmassnahme6-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSanierungsmassnahme6() {
        return sanierungsmassnahme6;
    }

    /**
     * Legt den Wert der sanierungsmassnahme6-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSanierungsmassnahme6(String value) {
        this.sanierungsmassnahme6 = value;
    }

    /**
     * Ruft den Wert der zurFreienVerwendung1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZurFreienVerwendung1() {
        return zurFreienVerwendung1;
    }

    /**
     * Legt den Wert der zurFreienVerwendung1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZurFreienVerwendung1(String value) {
        this.zurFreienVerwendung1 = value;
    }

    /**
     * Ruft den Wert der zurFreienVerwendung2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZurFreienVerwendung2() {
        return zurFreienVerwendung2;
    }

    /**
     * Legt den Wert der zurFreienVerwendung2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZurFreienVerwendung2(String value) {
        this.zurFreienVerwendung2 = value;
    }

    /**
     * Ruft den Wert der artBeschaedigteVerglasung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArtBeschaedigteVerglasung() {
        return artBeschaedigteVerglasung;
    }

    /**
     * Legt den Wert der artBeschaedigteVerglasung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtBeschaedigteVerglasung(String value) {
        this.artBeschaedigteVerglasung = value;
    }

    /**
     * Ruft den Wert der artVerglasung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArtVerglasung() {
        return artVerglasung;
    }

    /**
     * Legt den Wert der artVerglasung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtVerglasung(String value) {
        this.artVerglasung = value;
    }

    /**
     * Ruft den Wert der mieteBeschaedigteSache-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMieteBeschaedigteSache() {
        return mieteBeschaedigteSache;
    }

    /**
     * Legt den Wert der mieteBeschaedigteSache-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMieteBeschaedigteSache(String value) {
        this.mieteBeschaedigteSache = value;
    }

    /**
     * Ruft den Wert der mieteLeiheVon-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMieteLeiheVon() {
        return mieteLeiheVon;
    }

    /**
     * Legt den Wert der mieteLeiheVon-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMieteLeiheVon(String value) {
        this.mieteLeiheVon = value;
    }

    /**
     * Ruft den Wert der mieteLeiheBis-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschaedigteSache.MieteLeiheBis }
     *     
     */
    public BeschaedigteSache.MieteLeiheBis getMieteLeiheBis() {
        return mieteLeiheBis;
    }

    /**
     * Legt den Wert der mieteLeiheBis-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschaedigteSache.MieteLeiheBis }
     *     
     */
    public void setMieteLeiheBis(BeschaedigteSache.MieteLeiheBis value) {
        this.mieteLeiheBis = value;
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
            return "4230";
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
    public static class Anschaffungspreis {

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
    public static class IDNummer
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
    public static class Merkmal6
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
    public static class MieteLeiheBis
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
     *         &lt;element name="Adresskennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
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
    public static class ReferenzierungEigentuemer {

        @XmlElement(name = "Adresskennzeichen")
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
    public static class Umfang
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
    public static class Vorsteuerabzugsberechtigung {

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
    public static class WertderSache {

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
    public static class Wertverbesserungsart
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
     *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>ZustandBeschaedigteSacheTyp">
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
    public static class ZustandBeschaedigteSache {

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
