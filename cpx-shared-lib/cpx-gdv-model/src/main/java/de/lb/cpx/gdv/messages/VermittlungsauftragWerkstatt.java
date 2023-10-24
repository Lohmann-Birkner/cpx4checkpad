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
 *         &lt;element name="Auftraege">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AuftragsartWerkstatt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartWerkstattTyp" minOccurs="0"/>
 *                   &lt;element name="Auftragsbeschreibung" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="120"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="NotreparaturPruefen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="ArtDesServices" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Text001Typ" minOccurs="0"/>
 *                   &lt;element name="ErsatzfahrzeugFuerReparaturzeit" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ErsatzfahrzeugTyp" minOccurs="0"/>
 *                   &lt;element name="AbholungFahrzeugGewuenscht" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="WunschterminAbholung" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="8"/>
 *                         &lt;minLength value="8"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="WunschuhrzeitAbholung" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                           &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="InformationenZumFahrzeug" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Totalschaden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
 *                   &lt;element name="Gesamtschadenhoehe" minOccurs="0">
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
 *                   &lt;element name="Spurensicherung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SpurensicherungTyp" minOccurs="0"/>
 *                   &lt;element name="BeschreibungSonstigeSpurensicherung" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="50"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Fahrzeugzustand" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}FahrzeugzustandTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Standort" minOccurs="0">
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
 *         &lt;element name="ReferenzierungStandort" minOccurs="0">
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
 *         &lt;element name="BeschreibungStandort" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="80"/>
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
 *         &lt;element name="Wunschzeit" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="15"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SachverstaendigerBegutachtetFahrzeug" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Stundenverrechnungssaetze" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Mechanik" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="999.99"/>
 *                                   &lt;totalDigits value="5"/>
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
 *                   &lt;element name="Karosserie" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="999.99"/>
 *                                   &lt;totalDigits value="5"/>
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
 *                   &lt;element name="Lack" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="999.99"/>
 *                                   &lt;totalDigits value="5"/>
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
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="LackInformationen" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="KennzLackiermethode" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LackiermethodeTyp" minOccurs="0"/>
 *                   &lt;element name="LackfaktorProzent" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="999.99"/>
 *                                   &lt;totalDigits value="5"/>
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
 *                   &lt;element name="LackfaktorWE" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="99999.9"/>
 *                                   &lt;totalDigits value="6"/>
 *                                   &lt;fractionDigits value="1"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator">
 *                               &lt;complexType>
 *                                 &lt;simpleContent>
 *                                   &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>IndikatorTyp">
 *                                     &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                   &lt;/extension>
 *                                 &lt;/simpleContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4822">
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
    "referenzierungAuftraggeber",
    "auftraege",
    "informationenZumFahrzeug",
    "standort",
    "referenzierungStandort",
    "beschreibungStandort",
    "wunschtermin",
    "wunschzeit",
    "sachverstaendigerBegutachtetFahrzeug",
    "stundenverrechnungssaetze",
    "lackInformationen"
})
@XmlRootElement(name = "VermittlungsauftragWerkstatt")
public class VermittlungsauftragWerkstatt {

    @XmlElement(name = "ReferenzierungAuftraggeber", required = true)
    protected VermittlungsauftragWerkstatt.ReferenzierungAuftraggeber referenzierungAuftraggeber;
    @XmlElement(name = "Auftraege", required = true)
    protected VermittlungsauftragWerkstatt.Auftraege auftraege;
    @XmlElement(name = "InformationenZumFahrzeug")
    protected VermittlungsauftragWerkstatt.InformationenZumFahrzeug informationenZumFahrzeug;
    @XmlElement(name = "Standort")
    protected VermittlungsauftragWerkstatt.Standort standort;
    @XmlElement(name = "ReferenzierungStandort")
    protected VermittlungsauftragWerkstatt.ReferenzierungStandort referenzierungStandort;
    @XmlElement(name = "BeschreibungStandort")
    protected String beschreibungStandort;
    @XmlElement(name = "Wunschtermin")
    protected String wunschtermin;
    @XmlElement(name = "Wunschzeit")
    protected String wunschzeit;
    @XmlElement(name = "SachverstaendigerBegutachtetFahrzeug")
    protected String sachverstaendigerBegutachtetFahrzeug;
    @XmlElement(name = "Stundenverrechnungssaetze")
    protected VermittlungsauftragWerkstatt.Stundenverrechnungssaetze stundenverrechnungssaetze;
    @XmlElement(name = "LackInformationen")
    protected VermittlungsauftragWerkstatt.LackInformationen lackInformationen;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der referenzierungAuftraggeber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VermittlungsauftragWerkstatt.ReferenzierungAuftraggeber }
     *     
     */
    public VermittlungsauftragWerkstatt.ReferenzierungAuftraggeber getReferenzierungAuftraggeber() {
        return referenzierungAuftraggeber;
    }

    /**
     * Legt den Wert der referenzierungAuftraggeber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VermittlungsauftragWerkstatt.ReferenzierungAuftraggeber }
     *     
     */
    public void setReferenzierungAuftraggeber(VermittlungsauftragWerkstatt.ReferenzierungAuftraggeber value) {
        this.referenzierungAuftraggeber = value;
    }

    /**
     * Ruft den Wert der auftraege-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VermittlungsauftragWerkstatt.Auftraege }
     *     
     */
    public VermittlungsauftragWerkstatt.Auftraege getAuftraege() {
        return auftraege;
    }

    /**
     * Legt den Wert der auftraege-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VermittlungsauftragWerkstatt.Auftraege }
     *     
     */
    public void setAuftraege(VermittlungsauftragWerkstatt.Auftraege value) {
        this.auftraege = value;
    }

    /**
     * Ruft den Wert der informationenZumFahrzeug-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VermittlungsauftragWerkstatt.InformationenZumFahrzeug }
     *     
     */
    public VermittlungsauftragWerkstatt.InformationenZumFahrzeug getInformationenZumFahrzeug() {
        return informationenZumFahrzeug;
    }

    /**
     * Legt den Wert der informationenZumFahrzeug-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VermittlungsauftragWerkstatt.InformationenZumFahrzeug }
     *     
     */
    public void setInformationenZumFahrzeug(VermittlungsauftragWerkstatt.InformationenZumFahrzeug value) {
        this.informationenZumFahrzeug = value;
    }

    /**
     * Ruft den Wert der standort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VermittlungsauftragWerkstatt.Standort }
     *     
     */
    public VermittlungsauftragWerkstatt.Standort getStandort() {
        return standort;
    }

    /**
     * Legt den Wert der standort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VermittlungsauftragWerkstatt.Standort }
     *     
     */
    public void setStandort(VermittlungsauftragWerkstatt.Standort value) {
        this.standort = value;
    }

    /**
     * Ruft den Wert der referenzierungStandort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VermittlungsauftragWerkstatt.ReferenzierungStandort }
     *     
     */
    public VermittlungsauftragWerkstatt.ReferenzierungStandort getReferenzierungStandort() {
        return referenzierungStandort;
    }

    /**
     * Legt den Wert der referenzierungStandort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VermittlungsauftragWerkstatt.ReferenzierungStandort }
     *     
     */
    public void setReferenzierungStandort(VermittlungsauftragWerkstatt.ReferenzierungStandort value) {
        this.referenzierungStandort = value;
    }

    /**
     * Ruft den Wert der beschreibungStandort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeschreibungStandort() {
        return beschreibungStandort;
    }

    /**
     * Legt den Wert der beschreibungStandort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeschreibungStandort(String value) {
        this.beschreibungStandort = value;
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
     * Ruft den Wert der wunschzeit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWunschzeit() {
        return wunschzeit;
    }

    /**
     * Legt den Wert der wunschzeit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWunschzeit(String value) {
        this.wunschzeit = value;
    }

    /**
     * Ruft den Wert der sachverstaendigerBegutachtetFahrzeug-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSachverstaendigerBegutachtetFahrzeug() {
        return sachverstaendigerBegutachtetFahrzeug;
    }

    /**
     * Legt den Wert der sachverstaendigerBegutachtetFahrzeug-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSachverstaendigerBegutachtetFahrzeug(String value) {
        this.sachverstaendigerBegutachtetFahrzeug = value;
    }

    /**
     * Ruft den Wert der stundenverrechnungssaetze-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VermittlungsauftragWerkstatt.Stundenverrechnungssaetze }
     *     
     */
    public VermittlungsauftragWerkstatt.Stundenverrechnungssaetze getStundenverrechnungssaetze() {
        return stundenverrechnungssaetze;
    }

    /**
     * Legt den Wert der stundenverrechnungssaetze-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VermittlungsauftragWerkstatt.Stundenverrechnungssaetze }
     *     
     */
    public void setStundenverrechnungssaetze(VermittlungsauftragWerkstatt.Stundenverrechnungssaetze value) {
        this.stundenverrechnungssaetze = value;
    }

    /**
     * Ruft den Wert der lackInformationen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VermittlungsauftragWerkstatt.LackInformationen }
     *     
     */
    public VermittlungsauftragWerkstatt.LackInformationen getLackInformationen() {
        return lackInformationen;
    }

    /**
     * Legt den Wert der lackInformationen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VermittlungsauftragWerkstatt.LackInformationen }
     *     
     */
    public void setLackInformationen(VermittlungsauftragWerkstatt.LackInformationen value) {
        this.lackInformationen = value;
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
            return "4822";
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
     *         &lt;element name="AuftragsartWerkstatt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartWerkstattTyp" minOccurs="0"/>
     *         &lt;element name="Auftragsbeschreibung" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="120"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="NotreparaturPruefen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="ArtDesServices" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Text001Typ" minOccurs="0"/>
     *         &lt;element name="ErsatzfahrzeugFuerReparaturzeit" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ErsatzfahrzeugTyp" minOccurs="0"/>
     *         &lt;element name="AbholungFahrzeugGewuenscht" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="WunschterminAbholung" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="8"/>
     *               &lt;minLength value="8"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="WunschuhrzeitAbholung" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
     *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
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
        "auftragsartWerkstatt",
        "auftragsbeschreibung",
        "notreparaturPruefen",
        "artDesServices",
        "ersatzfahrzeugFuerReparaturzeit",
        "abholungFahrzeugGewuenscht",
        "wunschterminAbholung",
        "wunschuhrzeitAbholung"
    })
    public static class Auftraege {

        @XmlElement(name = "AuftragsartWerkstatt")
        protected String auftragsartWerkstatt;
        @XmlElement(name = "Auftragsbeschreibung")
        protected String auftragsbeschreibung;
        @XmlElement(name = "NotreparaturPruefen")
        protected String notreparaturPruefen;
        @XmlElement(name = "ArtDesServices")
        protected String artDesServices;
        @XmlElement(name = "ErsatzfahrzeugFuerReparaturzeit")
        protected String ersatzfahrzeugFuerReparaturzeit;
        @XmlElement(name = "AbholungFahrzeugGewuenscht")
        protected String abholungFahrzeugGewuenscht;
        @XmlElement(name = "WunschterminAbholung")
        protected String wunschterminAbholung;
        @XmlElement(name = "WunschuhrzeitAbholung")
        protected VermittlungsauftragWerkstatt.Auftraege.WunschuhrzeitAbholung wunschuhrzeitAbholung;

        /**
         * Ruft den Wert der auftragsartWerkstatt-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAuftragsartWerkstatt() {
            return auftragsartWerkstatt;
        }

        /**
         * Legt den Wert der auftragsartWerkstatt-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAuftragsartWerkstatt(String value) {
            this.auftragsartWerkstatt = value;
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
         * Ruft den Wert der notreparaturPruefen-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNotreparaturPruefen() {
            return notreparaturPruefen;
        }

        /**
         * Legt den Wert der notreparaturPruefen-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNotreparaturPruefen(String value) {
            this.notreparaturPruefen = value;
        }

        /**
         * Ruft den Wert der artDesServices-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getArtDesServices() {
            return artDesServices;
        }

        /**
         * Legt den Wert der artDesServices-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setArtDesServices(String value) {
            this.artDesServices = value;
        }

        /**
         * Ruft den Wert der ersatzfahrzeugFuerReparaturzeit-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getErsatzfahrzeugFuerReparaturzeit() {
            return ersatzfahrzeugFuerReparaturzeit;
        }

        /**
         * Legt den Wert der ersatzfahrzeugFuerReparaturzeit-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setErsatzfahrzeugFuerReparaturzeit(String value) {
            this.ersatzfahrzeugFuerReparaturzeit = value;
        }

        /**
         * Ruft den Wert der abholungFahrzeugGewuenscht-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAbholungFahrzeugGewuenscht() {
            return abholungFahrzeugGewuenscht;
        }

        /**
         * Legt den Wert der abholungFahrzeugGewuenscht-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAbholungFahrzeugGewuenscht(String value) {
            this.abholungFahrzeugGewuenscht = value;
        }

        /**
         * Ruft den Wert der wunschterminAbholung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getWunschterminAbholung() {
            return wunschterminAbholung;
        }

        /**
         * Legt den Wert der wunschterminAbholung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setWunschterminAbholung(String value) {
            this.wunschterminAbholung = value;
        }

        /**
         * Ruft den Wert der wunschuhrzeitAbholung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link VermittlungsauftragWerkstatt.Auftraege.WunschuhrzeitAbholung }
         *     
         */
        public VermittlungsauftragWerkstatt.Auftraege.WunschuhrzeitAbholung getWunschuhrzeitAbholung() {
            return wunschuhrzeitAbholung;
        }

        /**
         * Legt den Wert der wunschuhrzeitAbholung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link VermittlungsauftragWerkstatt.Auftraege.WunschuhrzeitAbholung }
         *     
         */
        public void setWunschuhrzeitAbholung(VermittlungsauftragWerkstatt.Auftraege.WunschuhrzeitAbholung value) {
            this.wunschuhrzeitAbholung = value;
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
        public static class WunschuhrzeitAbholung
            extends SatzendeTyp
        {


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
     *         &lt;element name="Totalschaden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
     *         &lt;element name="Gesamtschadenhoehe" minOccurs="0">
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
     *         &lt;element name="Spurensicherung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SpurensicherungTyp" minOccurs="0"/>
     *         &lt;element name="BeschreibungSonstigeSpurensicherung" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="50"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Fahrzeugzustand" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}FahrzeugzustandTyp" minOccurs="0"/>
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
        "totalschaden",
        "waehrungsschluessel",
        "gesamtschadenhoehe",
        "spurensicherung",
        "beschreibungSonstigeSpurensicherung",
        "fahrzeugzustand"
    })
    public static class InformationenZumFahrzeug {

        @XmlElement(name = "Totalschaden")
        protected String totalschaden;
        @XmlElement(name = "Waehrungsschluessel")
        protected String waehrungsschluessel;
        @XmlElement(name = "Gesamtschadenhoehe")
        protected VermittlungsauftragWerkstatt.InformationenZumFahrzeug.Gesamtschadenhoehe gesamtschadenhoehe;
        @XmlElement(name = "Spurensicherung")
        protected String spurensicherung;
        @XmlElement(name = "BeschreibungSonstigeSpurensicherung")
        protected String beschreibungSonstigeSpurensicherung;
        @XmlElement(name = "Fahrzeugzustand")
        protected String fahrzeugzustand;

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
         * Ruft den Wert der gesamtschadenhoehe-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link VermittlungsauftragWerkstatt.InformationenZumFahrzeug.Gesamtschadenhoehe }
         *     
         */
        public VermittlungsauftragWerkstatt.InformationenZumFahrzeug.Gesamtschadenhoehe getGesamtschadenhoehe() {
            return gesamtschadenhoehe;
        }

        /**
         * Legt den Wert der gesamtschadenhoehe-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link VermittlungsauftragWerkstatt.InformationenZumFahrzeug.Gesamtschadenhoehe }
         *     
         */
        public void setGesamtschadenhoehe(VermittlungsauftragWerkstatt.InformationenZumFahrzeug.Gesamtschadenhoehe value) {
            this.gesamtschadenhoehe = value;
        }

        /**
         * Ruft den Wert der spurensicherung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSpurensicherung() {
            return spurensicherung;
        }

        /**
         * Legt den Wert der spurensicherung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSpurensicherung(String value) {
            this.spurensicherung = value;
        }

        /**
         * Ruft den Wert der beschreibungSonstigeSpurensicherung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBeschreibungSonstigeSpurensicherung() {
            return beschreibungSonstigeSpurensicherung;
        }

        /**
         * Legt den Wert der beschreibungSonstigeSpurensicherung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBeschreibungSonstigeSpurensicherung(String value) {
            this.beschreibungSonstigeSpurensicherung = value;
        }

        /**
         * Ruft den Wert der fahrzeugzustand-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFahrzeugzustand() {
            return fahrzeugzustand;
        }

        /**
         * Legt den Wert der fahrzeugzustand-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFahrzeugzustand(String value) {
            this.fahrzeugzustand = value;
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
        public static class Gesamtschadenhoehe {

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
     *         &lt;element name="KennzLackiermethode" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LackiermethodeTyp" minOccurs="0"/>
     *         &lt;element name="LackfaktorProzent" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert" minOccurs="0">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         &lt;minInclusive value="0"/>
     *                         &lt;maxInclusive value="999.99"/>
     *                         &lt;totalDigits value="5"/>
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
     *         &lt;element name="LackfaktorWE" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert" minOccurs="0">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         &lt;minInclusive value="0"/>
     *                         &lt;maxInclusive value="99999.9"/>
     *                         &lt;totalDigits value="6"/>
     *                         &lt;fractionDigits value="1"/>
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
        "kennzLackiermethode",
        "lackfaktorProzent",
        "lackfaktorWE"
    })
    public static class LackInformationen {

        @XmlElement(name = "KennzLackiermethode")
        protected String kennzLackiermethode;
        @XmlElement(name = "LackfaktorProzent")
        protected VermittlungsauftragWerkstatt.LackInformationen.LackfaktorProzent lackfaktorProzent;
        @XmlElement(name = "LackfaktorWE")
        protected VermittlungsauftragWerkstatt.LackInformationen.LackfaktorWE lackfaktorWE;

        /**
         * Ruft den Wert der kennzLackiermethode-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKennzLackiermethode() {
            return kennzLackiermethode;
        }

        /**
         * Legt den Wert der kennzLackiermethode-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKennzLackiermethode(String value) {
            this.kennzLackiermethode = value;
        }

        /**
         * Ruft den Wert der lackfaktorProzent-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link VermittlungsauftragWerkstatt.LackInformationen.LackfaktorProzent }
         *     
         */
        public VermittlungsauftragWerkstatt.LackInformationen.LackfaktorProzent getLackfaktorProzent() {
            return lackfaktorProzent;
        }

        /**
         * Legt den Wert der lackfaktorProzent-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link VermittlungsauftragWerkstatt.LackInformationen.LackfaktorProzent }
         *     
         */
        public void setLackfaktorProzent(VermittlungsauftragWerkstatt.LackInformationen.LackfaktorProzent value) {
            this.lackfaktorProzent = value;
        }

        /**
         * Ruft den Wert der lackfaktorWE-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link VermittlungsauftragWerkstatt.LackInformationen.LackfaktorWE }
         *     
         */
        public VermittlungsauftragWerkstatt.LackInformationen.LackfaktorWE getLackfaktorWE() {
            return lackfaktorWE;
        }

        /**
         * Legt den Wert der lackfaktorWE-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link VermittlungsauftragWerkstatt.LackInformationen.LackfaktorWE }
         *     
         */
        public void setLackfaktorWE(VermittlungsauftragWerkstatt.LackInformationen.LackfaktorWE value) {
            this.lackfaktorWE = value;
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
         *               &lt;maxInclusive value="999.99"/>
         *               &lt;totalDigits value="5"/>
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
        public static class LackfaktorProzent {

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
         *               &lt;maxInclusive value="99999.9"/>
         *               &lt;totalDigits value="6"/>
         *               &lt;fractionDigits value="1"/>
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
        public static class LackfaktorWE {

            @XmlElement(name = "Wert")
            protected BigDecimal wert;
            @XmlElement(name = "Indikator", required = true)
            protected VermittlungsauftragWerkstatt.LackInformationen.LackfaktorWE.Indikator indikator;

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
             *     {@link VermittlungsauftragWerkstatt.LackInformationen.LackfaktorWE.Indikator }
             *     
             */
            public VermittlungsauftragWerkstatt.LackInformationen.LackfaktorWE.Indikator getIndikator() {
                return indikator;
            }

            /**
             * Legt den Wert der indikator-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link VermittlungsauftragWerkstatt.LackInformationen.LackfaktorWE.Indikator }
             *     
             */
            public void setIndikator(VermittlungsauftragWerkstatt.LackInformationen.LackfaktorWE.Indikator value) {
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
    public static class ReferenzierungStandort {

        @XmlElement(name = "AdressKennzeichen")
        protected String adressKennzeichen;
        @XmlElement(name = "PartnerReferenz")
        protected VermittlungsauftragWerkstatt.ReferenzierungStandort.PartnerReferenz partnerReferenz;

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
         *     {@link VermittlungsauftragWerkstatt.ReferenzierungStandort.PartnerReferenz }
         *     
         */
        public VermittlungsauftragWerkstatt.ReferenzierungStandort.PartnerReferenz getPartnerReferenz() {
            return partnerReferenz;
        }

        /**
         * Legt den Wert der partnerReferenz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link VermittlungsauftragWerkstatt.ReferenzierungStandort.PartnerReferenz }
         *     
         */
        public void setPartnerReferenz(VermittlungsauftragWerkstatt.ReferenzierungStandort.PartnerReferenz value) {
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
    public static class Standort {

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
     *         &lt;element name="Mechanik" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert" minOccurs="0">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         &lt;minInclusive value="0"/>
     *                         &lt;maxInclusive value="999.99"/>
     *                         &lt;totalDigits value="5"/>
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
     *         &lt;element name="Karosserie" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert" minOccurs="0">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         &lt;minInclusive value="0"/>
     *                         &lt;maxInclusive value="999.99"/>
     *                         &lt;totalDigits value="5"/>
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
     *         &lt;element name="Lack" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert" minOccurs="0">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         &lt;minInclusive value="0"/>
     *                         &lt;maxInclusive value="999.99"/>
     *                         &lt;totalDigits value="5"/>
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
        "mechanik",
        "karosserie",
        "lack"
    })
    public static class Stundenverrechnungssaetze {

        @XmlElement(name = "Mechanik")
        protected VermittlungsauftragWerkstatt.Stundenverrechnungssaetze.Mechanik mechanik;
        @XmlElement(name = "Karosserie")
        protected VermittlungsauftragWerkstatt.Stundenverrechnungssaetze.Karosserie karosserie;
        @XmlElement(name = "Lack")
        protected VermittlungsauftragWerkstatt.Stundenverrechnungssaetze.Lack lack;

        /**
         * Ruft den Wert der mechanik-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link VermittlungsauftragWerkstatt.Stundenverrechnungssaetze.Mechanik }
         *     
         */
        public VermittlungsauftragWerkstatt.Stundenverrechnungssaetze.Mechanik getMechanik() {
            return mechanik;
        }

        /**
         * Legt den Wert der mechanik-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link VermittlungsauftragWerkstatt.Stundenverrechnungssaetze.Mechanik }
         *     
         */
        public void setMechanik(VermittlungsauftragWerkstatt.Stundenverrechnungssaetze.Mechanik value) {
            this.mechanik = value;
        }

        /**
         * Ruft den Wert der karosserie-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link VermittlungsauftragWerkstatt.Stundenverrechnungssaetze.Karosserie }
         *     
         */
        public VermittlungsauftragWerkstatt.Stundenverrechnungssaetze.Karosserie getKarosserie() {
            return karosserie;
        }

        /**
         * Legt den Wert der karosserie-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link VermittlungsauftragWerkstatt.Stundenverrechnungssaetze.Karosserie }
         *     
         */
        public void setKarosserie(VermittlungsauftragWerkstatt.Stundenverrechnungssaetze.Karosserie value) {
            this.karosserie = value;
        }

        /**
         * Ruft den Wert der lack-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link VermittlungsauftragWerkstatt.Stundenverrechnungssaetze.Lack }
         *     
         */
        public VermittlungsauftragWerkstatt.Stundenverrechnungssaetze.Lack getLack() {
            return lack;
        }

        /**
         * Legt den Wert der lack-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link VermittlungsauftragWerkstatt.Stundenverrechnungssaetze.Lack }
         *     
         */
        public void setLack(VermittlungsauftragWerkstatt.Stundenverrechnungssaetze.Lack value) {
            this.lack = value;
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
         *               &lt;maxInclusive value="999.99"/>
         *               &lt;totalDigits value="5"/>
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
        public static class Karosserie {

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
         *               &lt;maxInclusive value="999.99"/>
         *               &lt;totalDigits value="5"/>
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
        public static class Lack {

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
         *               &lt;maxInclusive value="999.99"/>
         *               &lt;totalDigits value="5"/>
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
        public static class Mechanik {

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

}
