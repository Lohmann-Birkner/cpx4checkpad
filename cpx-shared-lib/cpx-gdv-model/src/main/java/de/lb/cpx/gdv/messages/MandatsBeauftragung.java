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
 *         &lt;element name="ArtDesMandats" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ArtDesMandatsTyp" minOccurs="0"/>
 *         &lt;element name="Gericht" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}GerichtTyp" minOccurs="0"/>
 *         &lt;element name="AktenzeichenGericht" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ReferenzierungGericht" minOccurs="0">
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
 *         &lt;element name="Klageart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KlageartTyp" minOccurs="0"/>
 *         &lt;element name="ArtVerfahren" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ArtDesVerfahrensTyp" minOccurs="0"/>
 *         &lt;element name="Klagegrund" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KlagegrundTyp" minOccurs="0"/>
 *         &lt;element name="DatumKlagezustellung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="FristVerteidigung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="FristErwiderung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Berufung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BerufungTyp" minOccurs="0"/>
 *         &lt;element name="Revision" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BerufungTyp" minOccurs="0"/>
 *         &lt;element name="Mahnverfahrenetc" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
 *                   &lt;element name="TitulierteForderung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="Forderungsart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ForderungsartTyp" minOccurs="0"/>
 *                   &lt;element name="Hauptforderung" minOccurs="0">
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
 *                   &lt;element name="RestDerHauptsache" minOccurs="0">
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
 *                   &lt;element name="BisherigeZinsen" minOccurs="0">
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
 *                   &lt;element name="ArtDerFaelligkeit" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ArtDerFaelligkeitTyp" minOccurs="0"/>
 *                   &lt;element name="Faelligkeit" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="8"/>
 *                         &lt;minLength value="8"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Zinssatz" minOccurs="0">
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
 *                   &lt;element name="Verrechnungsart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VerrechnungsartTyp" minOccurs="0"/>
 *                   &lt;element name="GeleisteteZahlungen" minOccurs="0">
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
 *                   &lt;element name="VorgerichtlicheMahnkosten" minOccurs="0">
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
 *                   &lt;element name="MahnbescheidsVollstreckungsbescheidskosten" minOccurs="0">
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
 *                   &lt;element name="Zwangsvollstreckungskosten" minOccurs="0">
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
 *                   &lt;element name="KostenFuerKostenfestsetzungsbeschluss" minOccurs="0">
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
 *         &lt;element name="Ermittlungsaktenanforderung" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ArtAktenanforderung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AktenanforderungsartTyp" minOccurs="0"/>
 *                   &lt;element name="Wunschtermin" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="8"/>
 *                         &lt;minLength value="8"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Aktenanforderungsgrund" minOccurs="0">
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
 *         &lt;element name="Anmerkungen" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4840">
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
    "artDesMandats",
    "gericht",
    "aktenzeichenGericht",
    "referenzierungGericht",
    "klageart",
    "artVerfahren",
    "klagegrund",
    "datumKlagezustellung",
    "fristVerteidigung",
    "fristErwiderung",
    "berufung",
    "revision",
    "mahnverfahrenetc",
    "ermittlungsaktenanforderung",
    "anmerkungen"
})
@XmlRootElement(name = "MandatsBeauftragung")
public class MandatsBeauftragung {

    @XmlElement(name = "ArtDesMandats")
    protected String artDesMandats;
    @XmlElement(name = "Gericht")
    protected String gericht;
    @XmlElement(name = "AktenzeichenGericht")
    protected String aktenzeichenGericht;
    @XmlElement(name = "ReferenzierungGericht")
    protected MandatsBeauftragung.ReferenzierungGericht referenzierungGericht;
    @XmlElement(name = "Klageart")
    protected String klageart;
    @XmlElement(name = "ArtVerfahren")
    protected String artVerfahren;
    @XmlElement(name = "Klagegrund")
    protected String klagegrund;
    @XmlElement(name = "DatumKlagezustellung")
    protected String datumKlagezustellung;
    @XmlElement(name = "FristVerteidigung")
    protected String fristVerteidigung;
    @XmlElement(name = "FristErwiderung")
    protected String fristErwiderung;
    @XmlElement(name = "Berufung")
    protected String berufung;
    @XmlElement(name = "Revision")
    protected String revision;
    @XmlElement(name = "Mahnverfahrenetc")
    protected MandatsBeauftragung.Mahnverfahrenetc mahnverfahrenetc;
    @XmlElement(name = "Ermittlungsaktenanforderung")
    protected MandatsBeauftragung.Ermittlungsaktenanforderung ermittlungsaktenanforderung;
    @XmlElement(name = "Anmerkungen")
    protected MandatsBeauftragung.Anmerkungen anmerkungen;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der artDesMandats-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArtDesMandats() {
        return artDesMandats;
    }

    /**
     * Legt den Wert der artDesMandats-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtDesMandats(String value) {
        this.artDesMandats = value;
    }

    /**
     * Ruft den Wert der gericht-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGericht() {
        return gericht;
    }

    /**
     * Legt den Wert der gericht-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGericht(String value) {
        this.gericht = value;
    }

    /**
     * Ruft den Wert der aktenzeichenGericht-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAktenzeichenGericht() {
        return aktenzeichenGericht;
    }

    /**
     * Legt den Wert der aktenzeichenGericht-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAktenzeichenGericht(String value) {
        this.aktenzeichenGericht = value;
    }

    /**
     * Ruft den Wert der referenzierungGericht-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MandatsBeauftragung.ReferenzierungGericht }
     *     
     */
    public MandatsBeauftragung.ReferenzierungGericht getReferenzierungGericht() {
        return referenzierungGericht;
    }

    /**
     * Legt den Wert der referenzierungGericht-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MandatsBeauftragung.ReferenzierungGericht }
     *     
     */
    public void setReferenzierungGericht(MandatsBeauftragung.ReferenzierungGericht value) {
        this.referenzierungGericht = value;
    }

    /**
     * Ruft den Wert der klageart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKlageart() {
        return klageart;
    }

    /**
     * Legt den Wert der klageart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKlageart(String value) {
        this.klageart = value;
    }

    /**
     * Ruft den Wert der artVerfahren-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArtVerfahren() {
        return artVerfahren;
    }

    /**
     * Legt den Wert der artVerfahren-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtVerfahren(String value) {
        this.artVerfahren = value;
    }

    /**
     * Ruft den Wert der klagegrund-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKlagegrund() {
        return klagegrund;
    }

    /**
     * Legt den Wert der klagegrund-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKlagegrund(String value) {
        this.klagegrund = value;
    }

    /**
     * Ruft den Wert der datumKlagezustellung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatumKlagezustellung() {
        return datumKlagezustellung;
    }

    /**
     * Legt den Wert der datumKlagezustellung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatumKlagezustellung(String value) {
        this.datumKlagezustellung = value;
    }

    /**
     * Ruft den Wert der fristVerteidigung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFristVerteidigung() {
        return fristVerteidigung;
    }

    /**
     * Legt den Wert der fristVerteidigung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFristVerteidigung(String value) {
        this.fristVerteidigung = value;
    }

    /**
     * Ruft den Wert der fristErwiderung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFristErwiderung() {
        return fristErwiderung;
    }

    /**
     * Legt den Wert der fristErwiderung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFristErwiderung(String value) {
        this.fristErwiderung = value;
    }

    /**
     * Ruft den Wert der berufung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBerufung() {
        return berufung;
    }

    /**
     * Legt den Wert der berufung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBerufung(String value) {
        this.berufung = value;
    }

    /**
     * Ruft den Wert der revision-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRevision() {
        return revision;
    }

    /**
     * Legt den Wert der revision-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRevision(String value) {
        this.revision = value;
    }

    /**
     * Ruft den Wert der mahnverfahrenetc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MandatsBeauftragung.Mahnverfahrenetc }
     *     
     */
    public MandatsBeauftragung.Mahnverfahrenetc getMahnverfahrenetc() {
        return mahnverfahrenetc;
    }

    /**
     * Legt den Wert der mahnverfahrenetc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MandatsBeauftragung.Mahnverfahrenetc }
     *     
     */
    public void setMahnverfahrenetc(MandatsBeauftragung.Mahnverfahrenetc value) {
        this.mahnverfahrenetc = value;
    }

    /**
     * Ruft den Wert der ermittlungsaktenanforderung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MandatsBeauftragung.Ermittlungsaktenanforderung }
     *     
     */
    public MandatsBeauftragung.Ermittlungsaktenanforderung getErmittlungsaktenanforderung() {
        return ermittlungsaktenanforderung;
    }

    /**
     * Legt den Wert der ermittlungsaktenanforderung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MandatsBeauftragung.Ermittlungsaktenanforderung }
     *     
     */
    public void setErmittlungsaktenanforderung(MandatsBeauftragung.Ermittlungsaktenanforderung value) {
        this.ermittlungsaktenanforderung = value;
    }

    /**
     * Ruft den Wert der anmerkungen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MandatsBeauftragung.Anmerkungen }
     *     
     */
    public MandatsBeauftragung.Anmerkungen getAnmerkungen() {
        return anmerkungen;
    }

    /**
     * Legt den Wert der anmerkungen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MandatsBeauftragung.Anmerkungen }
     *     
     */
    public void setAnmerkungen(MandatsBeauftragung.Anmerkungen value) {
        this.anmerkungen = value;
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
            return "4840";
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
    public static class Anmerkungen
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
     *         &lt;element name="ArtAktenanforderung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AktenanforderungsartTyp" minOccurs="0"/>
     *         &lt;element name="Wunschtermin" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="8"/>
     *               &lt;minLength value="8"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Aktenanforderungsgrund" minOccurs="0">
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
        "artAktenanforderung",
        "wunschtermin",
        "aktenanforderungsgrund"
    })
    public static class Ermittlungsaktenanforderung {

        @XmlElement(name = "ArtAktenanforderung")
        protected String artAktenanforderung;
        @XmlElement(name = "Wunschtermin")
        protected String wunschtermin;
        @XmlElement(name = "Aktenanforderungsgrund")
        protected MandatsBeauftragung.Ermittlungsaktenanforderung.Aktenanforderungsgrund aktenanforderungsgrund;

        /**
         * Ruft den Wert der artAktenanforderung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getArtAktenanforderung() {
            return artAktenanforderung;
        }

        /**
         * Legt den Wert der artAktenanforderung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setArtAktenanforderung(String value) {
            this.artAktenanforderung = value;
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
         * Ruft den Wert der aktenanforderungsgrund-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link MandatsBeauftragung.Ermittlungsaktenanforderung.Aktenanforderungsgrund }
         *     
         */
        public MandatsBeauftragung.Ermittlungsaktenanforderung.Aktenanforderungsgrund getAktenanforderungsgrund() {
            return aktenanforderungsgrund;
        }

        /**
         * Legt den Wert der aktenanforderungsgrund-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link MandatsBeauftragung.Ermittlungsaktenanforderung.Aktenanforderungsgrund }
         *     
         */
        public void setAktenanforderungsgrund(MandatsBeauftragung.Ermittlungsaktenanforderung.Aktenanforderungsgrund value) {
            this.aktenanforderungsgrund = value;
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
        public static class Aktenanforderungsgrund
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
     *         &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
     *         &lt;element name="TitulierteForderung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="Forderungsart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ForderungsartTyp" minOccurs="0"/>
     *         &lt;element name="Hauptforderung" minOccurs="0">
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
     *         &lt;element name="RestDerHauptsache" minOccurs="0">
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
     *         &lt;element name="BisherigeZinsen" minOccurs="0">
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
     *         &lt;element name="ArtDerFaelligkeit" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ArtDerFaelligkeitTyp" minOccurs="0"/>
     *         &lt;element name="Faelligkeit" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="8"/>
     *               &lt;minLength value="8"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Zinssatz" minOccurs="0">
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
     *         &lt;element name="Verrechnungsart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VerrechnungsartTyp" minOccurs="0"/>
     *         &lt;element name="GeleisteteZahlungen" minOccurs="0">
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
     *         &lt;element name="VorgerichtlicheMahnkosten" minOccurs="0">
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
     *         &lt;element name="MahnbescheidsVollstreckungsbescheidskosten" minOccurs="0">
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
     *         &lt;element name="Zwangsvollstreckungskosten" minOccurs="0">
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
     *         &lt;element name="KostenFuerKostenfestsetzungsbeschluss" minOccurs="0">
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
        "waehrungsschluessel",
        "titulierteForderung",
        "forderungsart",
        "hauptforderung",
        "restDerHauptsache",
        "bisherigeZinsen",
        "artDerFaelligkeit",
        "faelligkeit",
        "zinssatz",
        "verrechnungsart",
        "geleisteteZahlungen",
        "vorgerichtlicheMahnkosten",
        "mahnbescheidsVollstreckungsbescheidskosten",
        "zwangsvollstreckungskosten",
        "kostenFuerKostenfestsetzungsbeschluss"
    })
    public static class Mahnverfahrenetc {

        @XmlElement(name = "Waehrungsschluessel")
        protected String waehrungsschluessel;
        @XmlElement(name = "TitulierteForderung")
        protected String titulierteForderung;
        @XmlElement(name = "Forderungsart")
        protected String forderungsart;
        @XmlElement(name = "Hauptforderung")
        protected MandatsBeauftragung.Mahnverfahrenetc.Hauptforderung hauptforderung;
        @XmlElement(name = "RestDerHauptsache")
        protected MandatsBeauftragung.Mahnverfahrenetc.RestDerHauptsache restDerHauptsache;
        @XmlElement(name = "BisherigeZinsen")
        protected MandatsBeauftragung.Mahnverfahrenetc.BisherigeZinsen bisherigeZinsen;
        @XmlElement(name = "ArtDerFaelligkeit")
        protected String artDerFaelligkeit;
        @XmlElement(name = "Faelligkeit")
        protected String faelligkeit;
        @XmlElement(name = "Zinssatz")
        protected MandatsBeauftragung.Mahnverfahrenetc.Zinssatz zinssatz;
        @XmlElement(name = "Verrechnungsart")
        protected String verrechnungsart;
        @XmlElement(name = "GeleisteteZahlungen")
        protected MandatsBeauftragung.Mahnverfahrenetc.GeleisteteZahlungen geleisteteZahlungen;
        @XmlElement(name = "VorgerichtlicheMahnkosten")
        protected MandatsBeauftragung.Mahnverfahrenetc.VorgerichtlicheMahnkosten vorgerichtlicheMahnkosten;
        @XmlElement(name = "MahnbescheidsVollstreckungsbescheidskosten")
        protected MandatsBeauftragung.Mahnverfahrenetc.MahnbescheidsVollstreckungsbescheidskosten mahnbescheidsVollstreckungsbescheidskosten;
        @XmlElement(name = "Zwangsvollstreckungskosten")
        protected MandatsBeauftragung.Mahnverfahrenetc.Zwangsvollstreckungskosten zwangsvollstreckungskosten;
        @XmlElement(name = "KostenFuerKostenfestsetzungsbeschluss")
        protected MandatsBeauftragung.Mahnverfahrenetc.KostenFuerKostenfestsetzungsbeschluss kostenFuerKostenfestsetzungsbeschluss;

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
         * Ruft den Wert der titulierteForderung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTitulierteForderung() {
            return titulierteForderung;
        }

        /**
         * Legt den Wert der titulierteForderung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTitulierteForderung(String value) {
            this.titulierteForderung = value;
        }

        /**
         * Ruft den Wert der forderungsart-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getForderungsart() {
            return forderungsart;
        }

        /**
         * Legt den Wert der forderungsart-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setForderungsart(String value) {
            this.forderungsart = value;
        }

        /**
         * Ruft den Wert der hauptforderung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.Hauptforderung }
         *     
         */
        public MandatsBeauftragung.Mahnverfahrenetc.Hauptforderung getHauptforderung() {
            return hauptforderung;
        }

        /**
         * Legt den Wert der hauptforderung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.Hauptforderung }
         *     
         */
        public void setHauptforderung(MandatsBeauftragung.Mahnverfahrenetc.Hauptforderung value) {
            this.hauptforderung = value;
        }

        /**
         * Ruft den Wert der restDerHauptsache-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.RestDerHauptsache }
         *     
         */
        public MandatsBeauftragung.Mahnverfahrenetc.RestDerHauptsache getRestDerHauptsache() {
            return restDerHauptsache;
        }

        /**
         * Legt den Wert der restDerHauptsache-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.RestDerHauptsache }
         *     
         */
        public void setRestDerHauptsache(MandatsBeauftragung.Mahnverfahrenetc.RestDerHauptsache value) {
            this.restDerHauptsache = value;
        }

        /**
         * Ruft den Wert der bisherigeZinsen-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.BisherigeZinsen }
         *     
         */
        public MandatsBeauftragung.Mahnverfahrenetc.BisherigeZinsen getBisherigeZinsen() {
            return bisherigeZinsen;
        }

        /**
         * Legt den Wert der bisherigeZinsen-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.BisherigeZinsen }
         *     
         */
        public void setBisherigeZinsen(MandatsBeauftragung.Mahnverfahrenetc.BisherigeZinsen value) {
            this.bisherigeZinsen = value;
        }

        /**
         * Ruft den Wert der artDerFaelligkeit-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getArtDerFaelligkeit() {
            return artDerFaelligkeit;
        }

        /**
         * Legt den Wert der artDerFaelligkeit-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setArtDerFaelligkeit(String value) {
            this.artDerFaelligkeit = value;
        }

        /**
         * Ruft den Wert der faelligkeit-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFaelligkeit() {
            return faelligkeit;
        }

        /**
         * Legt den Wert der faelligkeit-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFaelligkeit(String value) {
            this.faelligkeit = value;
        }

        /**
         * Ruft den Wert der zinssatz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.Zinssatz }
         *     
         */
        public MandatsBeauftragung.Mahnverfahrenetc.Zinssatz getZinssatz() {
            return zinssatz;
        }

        /**
         * Legt den Wert der zinssatz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.Zinssatz }
         *     
         */
        public void setZinssatz(MandatsBeauftragung.Mahnverfahrenetc.Zinssatz value) {
            this.zinssatz = value;
        }

        /**
         * Ruft den Wert der verrechnungsart-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVerrechnungsart() {
            return verrechnungsart;
        }

        /**
         * Legt den Wert der verrechnungsart-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVerrechnungsart(String value) {
            this.verrechnungsart = value;
        }

        /**
         * Ruft den Wert der geleisteteZahlungen-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.GeleisteteZahlungen }
         *     
         */
        public MandatsBeauftragung.Mahnverfahrenetc.GeleisteteZahlungen getGeleisteteZahlungen() {
            return geleisteteZahlungen;
        }

        /**
         * Legt den Wert der geleisteteZahlungen-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.GeleisteteZahlungen }
         *     
         */
        public void setGeleisteteZahlungen(MandatsBeauftragung.Mahnverfahrenetc.GeleisteteZahlungen value) {
            this.geleisteteZahlungen = value;
        }

        /**
         * Ruft den Wert der vorgerichtlicheMahnkosten-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.VorgerichtlicheMahnkosten }
         *     
         */
        public MandatsBeauftragung.Mahnverfahrenetc.VorgerichtlicheMahnkosten getVorgerichtlicheMahnkosten() {
            return vorgerichtlicheMahnkosten;
        }

        /**
         * Legt den Wert der vorgerichtlicheMahnkosten-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.VorgerichtlicheMahnkosten }
         *     
         */
        public void setVorgerichtlicheMahnkosten(MandatsBeauftragung.Mahnverfahrenetc.VorgerichtlicheMahnkosten value) {
            this.vorgerichtlicheMahnkosten = value;
        }

        /**
         * Ruft den Wert der mahnbescheidsVollstreckungsbescheidskosten-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.MahnbescheidsVollstreckungsbescheidskosten }
         *     
         */
        public MandatsBeauftragung.Mahnverfahrenetc.MahnbescheidsVollstreckungsbescheidskosten getMahnbescheidsVollstreckungsbescheidskosten() {
            return mahnbescheidsVollstreckungsbescheidskosten;
        }

        /**
         * Legt den Wert der mahnbescheidsVollstreckungsbescheidskosten-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.MahnbescheidsVollstreckungsbescheidskosten }
         *     
         */
        public void setMahnbescheidsVollstreckungsbescheidskosten(MandatsBeauftragung.Mahnverfahrenetc.MahnbescheidsVollstreckungsbescheidskosten value) {
            this.mahnbescheidsVollstreckungsbescheidskosten = value;
        }

        /**
         * Ruft den Wert der zwangsvollstreckungskosten-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.Zwangsvollstreckungskosten }
         *     
         */
        public MandatsBeauftragung.Mahnverfahrenetc.Zwangsvollstreckungskosten getZwangsvollstreckungskosten() {
            return zwangsvollstreckungskosten;
        }

        /**
         * Legt den Wert der zwangsvollstreckungskosten-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.Zwangsvollstreckungskosten }
         *     
         */
        public void setZwangsvollstreckungskosten(MandatsBeauftragung.Mahnverfahrenetc.Zwangsvollstreckungskosten value) {
            this.zwangsvollstreckungskosten = value;
        }

        /**
         * Ruft den Wert der kostenFuerKostenfestsetzungsbeschluss-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.KostenFuerKostenfestsetzungsbeschluss }
         *     
         */
        public MandatsBeauftragung.Mahnverfahrenetc.KostenFuerKostenfestsetzungsbeschluss getKostenFuerKostenfestsetzungsbeschluss() {
            return kostenFuerKostenfestsetzungsbeschluss;
        }

        /**
         * Legt den Wert der kostenFuerKostenfestsetzungsbeschluss-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link MandatsBeauftragung.Mahnverfahrenetc.KostenFuerKostenfestsetzungsbeschluss }
         *     
         */
        public void setKostenFuerKostenfestsetzungsbeschluss(MandatsBeauftragung.Mahnverfahrenetc.KostenFuerKostenfestsetzungsbeschluss value) {
            this.kostenFuerKostenfestsetzungsbeschluss = value;
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
        public static class BisherigeZinsen {

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
        public static class GeleisteteZahlungen {

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
        public static class Hauptforderung {

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
        public static class KostenFuerKostenfestsetzungsbeschluss {

            @XmlElement(name = "Wert")
            protected BigDecimal wert;
            @XmlElement(name = "Indikator", required = true)
            protected MandatsBeauftragung.Mahnverfahrenetc.KostenFuerKostenfestsetzungsbeschluss.Indikator indikator;

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
             *     {@link MandatsBeauftragung.Mahnverfahrenetc.KostenFuerKostenfestsetzungsbeschluss.Indikator }
             *     
             */
            public MandatsBeauftragung.Mahnverfahrenetc.KostenFuerKostenfestsetzungsbeschluss.Indikator getIndikator() {
                return indikator;
            }

            /**
             * Legt den Wert der indikator-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link MandatsBeauftragung.Mahnverfahrenetc.KostenFuerKostenfestsetzungsbeschluss.Indikator }
             *     
             */
            public void setIndikator(MandatsBeauftragung.Mahnverfahrenetc.KostenFuerKostenfestsetzungsbeschluss.Indikator value) {
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
        public static class MahnbescheidsVollstreckungsbescheidskosten {

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
        public static class RestDerHauptsache {

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
        public static class VorgerichtlicheMahnkosten {

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
        public static class Zinssatz {

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
        public static class Zwangsvollstreckungskosten {

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
    public static class ReferenzierungGericht {

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
