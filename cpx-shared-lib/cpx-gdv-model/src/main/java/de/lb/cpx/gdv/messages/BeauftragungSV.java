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
 *                   &lt;element name="Auftragsart1" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp"/>
 *                   &lt;element name="Auftragsart2" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart3" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart4" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart5" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart6" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart7" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart8" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart9" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart10" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart11" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart12" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart13" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart14" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsart15" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
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
 *         &lt;element name="ArtSchadenObjekt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ObjektArtTyp" minOccurs="0"/>
 *         &lt;element name="Versicherungsbedingungen" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Totalschaden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinVermutlichTyp" minOccurs="0"/>
 *         &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
 *         &lt;element name="GesamtSchadenhoehe" minOccurs="0">
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
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Ansprechpartner" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AnsprechpartnerFuerDieBesichtigungaTyp" minOccurs="0"/>
 *         &lt;element name="Telefonnummer" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Regulierungsauftrag" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Kalkulationsparameter" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="LOHNFAKTOR1" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="LOHNFAKTOR2" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="LOHNFAKTOR3" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="LOHNFAKTOR4" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="LOHNFAKTOR5" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="LOHNFAKTOR6" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Lackierlohn" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Lackiermethode" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LackiermethodeTyp" minOccurs="0"/>
 *                   &lt;element name="Lackiermaterial" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>LackiermaterialsystemTyp">
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
 *       &lt;attribute name="Satzart" fixed="4810">
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
    "referenzierungAuftraggeber",
    "auftraege",
    "auftragsbeschreibung",
    "artSchadenObjekt",
    "versicherungsbedingungen",
    "totalschaden",
    "waehrungsschluessel",
    "gesamtSchadenhoehe",
    "besichtigungsort",
    "referenzierungBesichtigungsort",
    "beschreibungBesichtigungsort",
    "besichtigungstag",
    "besichtigungszeit",
    "wunschtermin",
    "ansprechpartner",
    "telefonnummer",
    "regulierungsauftrag",
    "kalkulationsparameter"
})
@XmlRootElement(name = "Beauftragung-SV")
public class BeauftragungSV {

    @XmlElement(name = "ReferenzierungAuftraggeber", required = true)
    protected BeauftragungSV.ReferenzierungAuftraggeber referenzierungAuftraggeber;
    @XmlElement(name = "Auftraege")
    protected BeauftragungSV.Auftraege auftraege;
    @XmlElement(name = "Auftragsbeschreibung")
    protected String auftragsbeschreibung;
    @XmlElement(name = "ArtSchadenObjekt")
    protected String artSchadenObjekt;
    @XmlElement(name = "Versicherungsbedingungen")
    protected String versicherungsbedingungen;
    @XmlElement(name = "Totalschaden")
    protected String totalschaden;
    @XmlElement(name = "Waehrungsschluessel")
    protected String waehrungsschluessel;
    @XmlElement(name = "GesamtSchadenhoehe")
    protected BeauftragungSV.GesamtSchadenhoehe gesamtSchadenhoehe;
    @XmlElement(name = "Besichtigungsort")
    protected BeauftragungSV.Besichtigungsort besichtigungsort;
    @XmlElement(name = "ReferenzierungBesichtigungsort")
    protected BeauftragungSV.ReferenzierungBesichtigungsort referenzierungBesichtigungsort;
    @XmlElement(name = "BeschreibungBesichtigungsort")
    protected String beschreibungBesichtigungsort;
    @XmlElement(name = "Besichtigungstag")
    protected String besichtigungstag;
    @XmlElement(name = "Besichtigungszeit")
    protected String besichtigungszeit;
    @XmlElement(name = "Wunschtermin")
    protected BeauftragungSV.Wunschtermin wunschtermin;
    @XmlElement(name = "Ansprechpartner")
    protected String ansprechpartner;
    @XmlElement(name = "Telefonnummer")
    protected String telefonnummer;
    @XmlElement(name = "Regulierungsauftrag")
    protected BeauftragungSV.Regulierungsauftrag regulierungsauftrag;
    @XmlElement(name = "Kalkulationsparameter")
    protected BeauftragungSV.Kalkulationsparameter kalkulationsparameter;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der referenzierungAuftraggeber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeauftragungSV.ReferenzierungAuftraggeber }
     *     
     */
    public BeauftragungSV.ReferenzierungAuftraggeber getReferenzierungAuftraggeber() {
        return referenzierungAuftraggeber;
    }

    /**
     * Legt den Wert der referenzierungAuftraggeber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeauftragungSV.ReferenzierungAuftraggeber }
     *     
     */
    public void setReferenzierungAuftraggeber(BeauftragungSV.ReferenzierungAuftraggeber value) {
        this.referenzierungAuftraggeber = value;
    }

    /**
     * Ruft den Wert der auftraege-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeauftragungSV.Auftraege }
     *     
     */
    public BeauftragungSV.Auftraege getAuftraege() {
        return auftraege;
    }

    /**
     * Legt den Wert der auftraege-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeauftragungSV.Auftraege }
     *     
     */
    public void setAuftraege(BeauftragungSV.Auftraege value) {
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
     * Ruft den Wert der artSchadenObjekt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArtSchadenObjekt() {
        return artSchadenObjekt;
    }

    /**
     * Legt den Wert der artSchadenObjekt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtSchadenObjekt(String value) {
        this.artSchadenObjekt = value;
    }

    /**
     * Ruft den Wert der versicherungsbedingungen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersicherungsbedingungen() {
        return versicherungsbedingungen;
    }

    /**
     * Legt den Wert der versicherungsbedingungen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersicherungsbedingungen(String value) {
        this.versicherungsbedingungen = value;
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
     * Ruft den Wert der gesamtSchadenhoehe-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeauftragungSV.GesamtSchadenhoehe }
     *     
     */
    public BeauftragungSV.GesamtSchadenhoehe getGesamtSchadenhoehe() {
        return gesamtSchadenhoehe;
    }

    /**
     * Legt den Wert der gesamtSchadenhoehe-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeauftragungSV.GesamtSchadenhoehe }
     *     
     */
    public void setGesamtSchadenhoehe(BeauftragungSV.GesamtSchadenhoehe value) {
        this.gesamtSchadenhoehe = value;
    }

    /**
     * Ruft den Wert der besichtigungsort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeauftragungSV.Besichtigungsort }
     *     
     */
    public BeauftragungSV.Besichtigungsort getBesichtigungsort() {
        return besichtigungsort;
    }

    /**
     * Legt den Wert der besichtigungsort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeauftragungSV.Besichtigungsort }
     *     
     */
    public void setBesichtigungsort(BeauftragungSV.Besichtigungsort value) {
        this.besichtigungsort = value;
    }

    /**
     * Ruft den Wert der referenzierungBesichtigungsort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeauftragungSV.ReferenzierungBesichtigungsort }
     *     
     */
    public BeauftragungSV.ReferenzierungBesichtigungsort getReferenzierungBesichtigungsort() {
        return referenzierungBesichtigungsort;
    }

    /**
     * Legt den Wert der referenzierungBesichtigungsort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeauftragungSV.ReferenzierungBesichtigungsort }
     *     
     */
    public void setReferenzierungBesichtigungsort(BeauftragungSV.ReferenzierungBesichtigungsort value) {
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
     *     {@link BeauftragungSV.Wunschtermin }
     *     
     */
    public BeauftragungSV.Wunschtermin getWunschtermin() {
        return wunschtermin;
    }

    /**
     * Legt den Wert der wunschtermin-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeauftragungSV.Wunschtermin }
     *     
     */
    public void setWunschtermin(BeauftragungSV.Wunschtermin value) {
        this.wunschtermin = value;
    }

    /**
     * Ruft den Wert der ansprechpartner-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnsprechpartner() {
        return ansprechpartner;
    }

    /**
     * Legt den Wert der ansprechpartner-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnsprechpartner(String value) {
        this.ansprechpartner = value;
    }

    /**
     * Ruft den Wert der telefonnummer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefonnummer() {
        return telefonnummer;
    }

    /**
     * Legt den Wert der telefonnummer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefonnummer(String value) {
        this.telefonnummer = value;
    }

    /**
     * Ruft den Wert der regulierungsauftrag-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeauftragungSV.Regulierungsauftrag }
     *     
     */
    public BeauftragungSV.Regulierungsauftrag getRegulierungsauftrag() {
        return regulierungsauftrag;
    }

    /**
     * Legt den Wert der regulierungsauftrag-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeauftragungSV.Regulierungsauftrag }
     *     
     */
    public void setRegulierungsauftrag(BeauftragungSV.Regulierungsauftrag value) {
        this.regulierungsauftrag = value;
    }

    /**
     * Ruft den Wert der kalkulationsparameter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeauftragungSV.Kalkulationsparameter }
     *     
     */
    public BeauftragungSV.Kalkulationsparameter getKalkulationsparameter() {
        return kalkulationsparameter;
    }

    /**
     * Legt den Wert der kalkulationsparameter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeauftragungSV.Kalkulationsparameter }
     *     
     */
    public void setKalkulationsparameter(BeauftragungSV.Kalkulationsparameter value) {
        this.kalkulationsparameter = value;
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
            return "4810";
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
     *         &lt;element name="Auftragsart1" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp"/>
     *         &lt;element name="Auftragsart2" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart3" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart4" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart5" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart6" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart7" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart8" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart9" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart10" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart11" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart12" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart13" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart14" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsart15" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartSachverstaendigeKraftfahrtTyp" minOccurs="0"/>
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
        "auftragsart15"
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
    public static class GesamtSchadenhoehe {

        @XmlElement(name = "Wert")
        protected BigDecimal wert;
        @XmlElement(name = "Indikator", required = true)
        protected BeauftragungSV.GesamtSchadenhoehe.Indikator indikator;

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
         *     {@link BeauftragungSV.GesamtSchadenhoehe.Indikator }
         *     
         */
        public BeauftragungSV.GesamtSchadenhoehe.Indikator getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BeauftragungSV.GesamtSchadenhoehe.Indikator }
         *     
         */
        public void setIndikator(BeauftragungSV.GesamtSchadenhoehe.Indikator value) {
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
     *         &lt;element name="LOHNFAKTOR1" minOccurs="0">
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
     *         &lt;element name="LOHNFAKTOR2" minOccurs="0">
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
     *         &lt;element name="LOHNFAKTOR3" minOccurs="0">
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
     *         &lt;element name="LOHNFAKTOR4" minOccurs="0">
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
     *         &lt;element name="LOHNFAKTOR5" minOccurs="0">
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
     *         &lt;element name="LOHNFAKTOR6" minOccurs="0">
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
     *         &lt;element name="Lackierlohn" minOccurs="0">
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
     *         &lt;element name="Lackiermethode" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LackiermethodeTyp" minOccurs="0"/>
     *         &lt;element name="Lackiermaterial" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>LackiermaterialsystemTyp">
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
        "lohnfaktor1",
        "lohnfaktor2",
        "lohnfaktor3",
        "lohnfaktor4",
        "lohnfaktor5",
        "lohnfaktor6",
        "lackierlohn",
        "lackiermethode",
        "lackiermaterial"
    })
    public static class Kalkulationsparameter {

        @XmlElement(name = "LOHNFAKTOR1")
        protected BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR1 lohnfaktor1;
        @XmlElement(name = "LOHNFAKTOR2")
        protected BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR2 lohnfaktor2;
        @XmlElement(name = "LOHNFAKTOR3")
        protected BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR3 lohnfaktor3;
        @XmlElement(name = "LOHNFAKTOR4")
        protected BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR4 lohnfaktor4;
        @XmlElement(name = "LOHNFAKTOR5")
        protected BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR5 lohnfaktor5;
        @XmlElement(name = "LOHNFAKTOR6")
        protected BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR6 lohnfaktor6;
        @XmlElement(name = "Lackierlohn")
        protected BeauftragungSV.Kalkulationsparameter.Lackierlohn lackierlohn;
        @XmlElement(name = "Lackiermethode")
        protected String lackiermethode;
        @XmlElement(name = "Lackiermaterial")
        protected BeauftragungSV.Kalkulationsparameter.Lackiermaterial lackiermaterial;

        /**
         * Ruft den Wert der lohnfaktor1-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR1 }
         *     
         */
        public BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR1 getLOHNFAKTOR1() {
            return lohnfaktor1;
        }

        /**
         * Legt den Wert der lohnfaktor1-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR1 }
         *     
         */
        public void setLOHNFAKTOR1(BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR1 value) {
            this.lohnfaktor1 = value;
        }

        /**
         * Ruft den Wert der lohnfaktor2-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR2 }
         *     
         */
        public BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR2 getLOHNFAKTOR2() {
            return lohnfaktor2;
        }

        /**
         * Legt den Wert der lohnfaktor2-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR2 }
         *     
         */
        public void setLOHNFAKTOR2(BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR2 value) {
            this.lohnfaktor2 = value;
        }

        /**
         * Ruft den Wert der lohnfaktor3-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR3 }
         *     
         */
        public BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR3 getLOHNFAKTOR3() {
            return lohnfaktor3;
        }

        /**
         * Legt den Wert der lohnfaktor3-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR3 }
         *     
         */
        public void setLOHNFAKTOR3(BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR3 value) {
            this.lohnfaktor3 = value;
        }

        /**
         * Ruft den Wert der lohnfaktor4-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR4 }
         *     
         */
        public BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR4 getLOHNFAKTOR4() {
            return lohnfaktor4;
        }

        /**
         * Legt den Wert der lohnfaktor4-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR4 }
         *     
         */
        public void setLOHNFAKTOR4(BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR4 value) {
            this.lohnfaktor4 = value;
        }

        /**
         * Ruft den Wert der lohnfaktor5-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR5 }
         *     
         */
        public BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR5 getLOHNFAKTOR5() {
            return lohnfaktor5;
        }

        /**
         * Legt den Wert der lohnfaktor5-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR5 }
         *     
         */
        public void setLOHNFAKTOR5(BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR5 value) {
            this.lohnfaktor5 = value;
        }

        /**
         * Ruft den Wert der lohnfaktor6-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR6 }
         *     
         */
        public BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR6 getLOHNFAKTOR6() {
            return lohnfaktor6;
        }

        /**
         * Legt den Wert der lohnfaktor6-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR6 }
         *     
         */
        public void setLOHNFAKTOR6(BeauftragungSV.Kalkulationsparameter.LOHNFAKTOR6 value) {
            this.lohnfaktor6 = value;
        }

        /**
         * Ruft den Wert der lackierlohn-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BeauftragungSV.Kalkulationsparameter.Lackierlohn }
         *     
         */
        public BeauftragungSV.Kalkulationsparameter.Lackierlohn getLackierlohn() {
            return lackierlohn;
        }

        /**
         * Legt den Wert der lackierlohn-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BeauftragungSV.Kalkulationsparameter.Lackierlohn }
         *     
         */
        public void setLackierlohn(BeauftragungSV.Kalkulationsparameter.Lackierlohn value) {
            this.lackierlohn = value;
        }

        /**
         * Ruft den Wert der lackiermethode-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLackiermethode() {
            return lackiermethode;
        }

        /**
         * Legt den Wert der lackiermethode-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLackiermethode(String value) {
            this.lackiermethode = value;
        }

        /**
         * Ruft den Wert der lackiermaterial-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BeauftragungSV.Kalkulationsparameter.Lackiermaterial }
         *     
         */
        public BeauftragungSV.Kalkulationsparameter.Lackiermaterial getLackiermaterial() {
            return lackiermaterial;
        }

        /**
         * Legt den Wert der lackiermaterial-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BeauftragungSV.Kalkulationsparameter.Lackiermaterial }
         *     
         */
        public void setLackiermaterial(BeauftragungSV.Kalkulationsparameter.Lackiermaterial value) {
            this.lackiermaterial = value;
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
        public static class LOHNFAKTOR1 {

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
        public static class LOHNFAKTOR2 {

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
        public static class LOHNFAKTOR3 {

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
        public static class LOHNFAKTOR4 {

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
        public static class LOHNFAKTOR5 {

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
        public static class LOHNFAKTOR6 {

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
        public static class Lackierlohn {

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
         *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>LackiermaterialsystemTyp">
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
        public static class Lackiermaterial {

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
    public static class Regulierungsauftrag {

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
    public static class Wunschtermin
        extends SatzendeTyp
    {


    }

}
