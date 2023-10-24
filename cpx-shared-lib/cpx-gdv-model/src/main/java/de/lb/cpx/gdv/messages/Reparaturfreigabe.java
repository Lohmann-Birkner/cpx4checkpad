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
 *         &lt;element name="Schadendatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Schadenuhrzeit" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *               &lt;minLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Finanzierungsart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Finanzierungsart1Typ" minOccurs="0"/>
 *         &lt;element name="Fahrzeugversicherung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}FahrzeugversicherungTyp" minOccurs="0"/>
 *         &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
 *         &lt;element name="Selbstbeteiligung-Teilkasko" minOccurs="0">
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
 *         &lt;element name="Selbstbeteiligung-Vollkasko" minOccurs="0">
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
 *         &lt;element name="Schutzbrief" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Freigabe" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Betrag" minOccurs="0">
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
 *         &lt;element name="Freigabegrundlage" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}FreigabegrundlageTyp" minOccurs="0"/>
 *         &lt;element name="Freigabe-Mietwagenkosten" minOccurs="0">
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
 *         &lt;element name="GruppeMietwagen" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Mietdauer" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="0"/>
 *               &lt;totalDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Haftungsquote" minOccurs="0">
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
 *         &lt;element name="Bearbeiter" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ReparaturDauer" minOccurs="0">
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
 *         &lt;element name="SchadensparteKfz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Sparte1Typ" minOccurs="0"/>
 *         &lt;element name="Uebernahme-Mietwagen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Uebernahme-Reparatur" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Uebernahmegrundlage" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}FreigabegrundlageTyp" minOccurs="0"/>
 *         &lt;element name="Vorsteuerabzug" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Uebersendung-erforderlich" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="UebersendungSiA-erforderlich" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="VerzichtBesicht" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="WillFotos" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="WillSV" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4800">
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
    "schadendatum",
    "schadenuhrzeit",
    "finanzierungsart",
    "fahrzeugversicherung",
    "waehrungsschluessel",
    "selbstbeteiligungTeilkasko",
    "selbstbeteiligungVollkasko",
    "schutzbrief",
    "freigabe",
    "betrag",
    "freigabegrundlage",
    "freigabeMietwagenkosten",
    "gruppeMietwagen",
    "mietdauer",
    "haftungsquote",
    "bearbeiter",
    "reparaturDauer",
    "schadensparteKfz",
    "uebernahmeMietwagen",
    "uebernahmeReparatur",
    "uebernahmegrundlage",
    "vorsteuerabzug",
    "uebersendungErforderlich",
    "uebersendungSiAErforderlich",
    "verzichtBesicht",
    "willFotos",
    "willSV"
})
@XmlRootElement(name = "Reparaturfreigabe")
public class Reparaturfreigabe {

    @XmlElement(name = "Schadendatum")
    protected String schadendatum;
    @XmlElement(name = "Schadenuhrzeit")
    protected String schadenuhrzeit;
    @XmlElement(name = "Finanzierungsart")
    protected String finanzierungsart;
    @XmlElement(name = "Fahrzeugversicherung")
    protected String fahrzeugversicherung;
    @XmlElement(name = "Waehrungsschluessel")
    protected String waehrungsschluessel;
    @XmlElement(name = "Selbstbeteiligung-Teilkasko")
    protected Reparaturfreigabe.SelbstbeteiligungTeilkasko selbstbeteiligungTeilkasko;
    @XmlElement(name = "Selbstbeteiligung-Vollkasko")
    protected Reparaturfreigabe.SelbstbeteiligungVollkasko selbstbeteiligungVollkasko;
    @XmlElement(name = "Schutzbrief")
    protected String schutzbrief;
    @XmlElement(name = "Freigabe")
    protected String freigabe;
    @XmlElement(name = "Betrag")
    protected Reparaturfreigabe.Betrag betrag;
    @XmlElement(name = "Freigabegrundlage")
    protected String freigabegrundlage;
    @XmlElement(name = "Freigabe-Mietwagenkosten")
    protected Reparaturfreigabe.FreigabeMietwagenkosten freigabeMietwagenkosten;
    @XmlElement(name = "GruppeMietwagen")
    protected String gruppeMietwagen;
    @XmlElement(name = "Mietdauer")
    protected BigInteger mietdauer;
    @XmlElement(name = "Haftungsquote")
    protected Reparaturfreigabe.Haftungsquote haftungsquote;
    @XmlElement(name = "Bearbeiter")
    protected String bearbeiter;
    @XmlElement(name = "ReparaturDauer")
    protected Reparaturfreigabe.ReparaturDauer reparaturDauer;
    @XmlElement(name = "SchadensparteKfz")
    protected String schadensparteKfz;
    @XmlElement(name = "Uebernahme-Mietwagen")
    protected String uebernahmeMietwagen;
    @XmlElement(name = "Uebernahme-Reparatur")
    protected String uebernahmeReparatur;
    @XmlElement(name = "Uebernahmegrundlage")
    protected String uebernahmegrundlage;
    @XmlElement(name = "Vorsteuerabzug")
    protected String vorsteuerabzug;
    @XmlElement(name = "Uebersendung-erforderlich")
    protected String uebersendungErforderlich;
    @XmlElement(name = "UebersendungSiA-erforderlich")
    protected String uebersendungSiAErforderlich;
    @XmlElement(name = "VerzichtBesicht")
    protected String verzichtBesicht;
    @XmlElement(name = "WillFotos")
    protected String willFotos;
    @XmlElement(name = "WillSV")
    protected Reparaturfreigabe.WillSV willSV;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

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
     * Ruft den Wert der schadenuhrzeit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenuhrzeit() {
        return schadenuhrzeit;
    }

    /**
     * Legt den Wert der schadenuhrzeit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenuhrzeit(String value) {
        this.schadenuhrzeit = value;
    }

    /**
     * Ruft den Wert der finanzierungsart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinanzierungsart() {
        return finanzierungsart;
    }

    /**
     * Legt den Wert der finanzierungsart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinanzierungsart(String value) {
        this.finanzierungsart = value;
    }

    /**
     * Ruft den Wert der fahrzeugversicherung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFahrzeugversicherung() {
        return fahrzeugversicherung;
    }

    /**
     * Legt den Wert der fahrzeugversicherung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFahrzeugversicherung(String value) {
        this.fahrzeugversicherung = value;
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
     * Ruft den Wert der selbstbeteiligungTeilkasko-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Reparaturfreigabe.SelbstbeteiligungTeilkasko }
     *     
     */
    public Reparaturfreigabe.SelbstbeteiligungTeilkasko getSelbstbeteiligungTeilkasko() {
        return selbstbeteiligungTeilkasko;
    }

    /**
     * Legt den Wert der selbstbeteiligungTeilkasko-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Reparaturfreigabe.SelbstbeteiligungTeilkasko }
     *     
     */
    public void setSelbstbeteiligungTeilkasko(Reparaturfreigabe.SelbstbeteiligungTeilkasko value) {
        this.selbstbeteiligungTeilkasko = value;
    }

    /**
     * Ruft den Wert der selbstbeteiligungVollkasko-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Reparaturfreigabe.SelbstbeteiligungVollkasko }
     *     
     */
    public Reparaturfreigabe.SelbstbeteiligungVollkasko getSelbstbeteiligungVollkasko() {
        return selbstbeteiligungVollkasko;
    }

    /**
     * Legt den Wert der selbstbeteiligungVollkasko-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Reparaturfreigabe.SelbstbeteiligungVollkasko }
     *     
     */
    public void setSelbstbeteiligungVollkasko(Reparaturfreigabe.SelbstbeteiligungVollkasko value) {
        this.selbstbeteiligungVollkasko = value;
    }

    /**
     * Ruft den Wert der schutzbrief-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchutzbrief() {
        return schutzbrief;
    }

    /**
     * Legt den Wert der schutzbrief-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchutzbrief(String value) {
        this.schutzbrief = value;
    }

    /**
     * Ruft den Wert der freigabe-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFreigabe() {
        return freigabe;
    }

    /**
     * Legt den Wert der freigabe-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFreigabe(String value) {
        this.freigabe = value;
    }

    /**
     * Ruft den Wert der betrag-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Reparaturfreigabe.Betrag }
     *     
     */
    public Reparaturfreigabe.Betrag getBetrag() {
        return betrag;
    }

    /**
     * Legt den Wert der betrag-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Reparaturfreigabe.Betrag }
     *     
     */
    public void setBetrag(Reparaturfreigabe.Betrag value) {
        this.betrag = value;
    }

    /**
     * Ruft den Wert der freigabegrundlage-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFreigabegrundlage() {
        return freigabegrundlage;
    }

    /**
     * Legt den Wert der freigabegrundlage-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFreigabegrundlage(String value) {
        this.freigabegrundlage = value;
    }

    /**
     * Ruft den Wert der freigabeMietwagenkosten-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Reparaturfreigabe.FreigabeMietwagenkosten }
     *     
     */
    public Reparaturfreigabe.FreigabeMietwagenkosten getFreigabeMietwagenkosten() {
        return freigabeMietwagenkosten;
    }

    /**
     * Legt den Wert der freigabeMietwagenkosten-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Reparaturfreigabe.FreigabeMietwagenkosten }
     *     
     */
    public void setFreigabeMietwagenkosten(Reparaturfreigabe.FreigabeMietwagenkosten value) {
        this.freigabeMietwagenkosten = value;
    }

    /**
     * Ruft den Wert der gruppeMietwagen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGruppeMietwagen() {
        return gruppeMietwagen;
    }

    /**
     * Legt den Wert der gruppeMietwagen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGruppeMietwagen(String value) {
        this.gruppeMietwagen = value;
    }

    /**
     * Ruft den Wert der mietdauer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMietdauer() {
        return mietdauer;
    }

    /**
     * Legt den Wert der mietdauer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMietdauer(BigInteger value) {
        this.mietdauer = value;
    }

    /**
     * Ruft den Wert der haftungsquote-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Reparaturfreigabe.Haftungsquote }
     *     
     */
    public Reparaturfreigabe.Haftungsquote getHaftungsquote() {
        return haftungsquote;
    }

    /**
     * Legt den Wert der haftungsquote-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Reparaturfreigabe.Haftungsquote }
     *     
     */
    public void setHaftungsquote(Reparaturfreigabe.Haftungsquote value) {
        this.haftungsquote = value;
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
     * Ruft den Wert der reparaturDauer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Reparaturfreigabe.ReparaturDauer }
     *     
     */
    public Reparaturfreigabe.ReparaturDauer getReparaturDauer() {
        return reparaturDauer;
    }

    /**
     * Legt den Wert der reparaturDauer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Reparaturfreigabe.ReparaturDauer }
     *     
     */
    public void setReparaturDauer(Reparaturfreigabe.ReparaturDauer value) {
        this.reparaturDauer = value;
    }

    /**
     * Ruft den Wert der schadensparteKfz-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadensparteKfz() {
        return schadensparteKfz;
    }

    /**
     * Legt den Wert der schadensparteKfz-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadensparteKfz(String value) {
        this.schadensparteKfz = value;
    }

    /**
     * Ruft den Wert der uebernahmeMietwagen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUebernahmeMietwagen() {
        return uebernahmeMietwagen;
    }

    /**
     * Legt den Wert der uebernahmeMietwagen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUebernahmeMietwagen(String value) {
        this.uebernahmeMietwagen = value;
    }

    /**
     * Ruft den Wert der uebernahmeReparatur-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUebernahmeReparatur() {
        return uebernahmeReparatur;
    }

    /**
     * Legt den Wert der uebernahmeReparatur-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUebernahmeReparatur(String value) {
        this.uebernahmeReparatur = value;
    }

    /**
     * Ruft den Wert der uebernahmegrundlage-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUebernahmegrundlage() {
        return uebernahmegrundlage;
    }

    /**
     * Legt den Wert der uebernahmegrundlage-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUebernahmegrundlage(String value) {
        this.uebernahmegrundlage = value;
    }

    /**
     * Ruft den Wert der vorsteuerabzug-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVorsteuerabzug() {
        return vorsteuerabzug;
    }

    /**
     * Legt den Wert der vorsteuerabzug-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVorsteuerabzug(String value) {
        this.vorsteuerabzug = value;
    }

    /**
     * Ruft den Wert der uebersendungErforderlich-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUebersendungErforderlich() {
        return uebersendungErforderlich;
    }

    /**
     * Legt den Wert der uebersendungErforderlich-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUebersendungErforderlich(String value) {
        this.uebersendungErforderlich = value;
    }

    /**
     * Ruft den Wert der uebersendungSiAErforderlich-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUebersendungSiAErforderlich() {
        return uebersendungSiAErforderlich;
    }

    /**
     * Legt den Wert der uebersendungSiAErforderlich-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUebersendungSiAErforderlich(String value) {
        this.uebersendungSiAErforderlich = value;
    }

    /**
     * Ruft den Wert der verzichtBesicht-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerzichtBesicht() {
        return verzichtBesicht;
    }

    /**
     * Legt den Wert der verzichtBesicht-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerzichtBesicht(String value) {
        this.verzichtBesicht = value;
    }

    /**
     * Ruft den Wert der willFotos-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWillFotos() {
        return willFotos;
    }

    /**
     * Legt den Wert der willFotos-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWillFotos(String value) {
        this.willFotos = value;
    }

    /**
     * Ruft den Wert der willSV-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Reparaturfreigabe.WillSV }
     *     
     */
    public Reparaturfreigabe.WillSV getWillSV() {
        return willSV;
    }

    /**
     * Legt den Wert der willSV-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Reparaturfreigabe.WillSV }
     *     
     */
    public void setWillSV(Reparaturfreigabe.WillSV value) {
        this.willSV = value;
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
            return "4800";
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
    public static class Betrag {

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
    public static class FreigabeMietwagenkosten {

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
    public static class Haftungsquote {

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
    public static class ReparaturDauer {

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
    public static class SelbstbeteiligungTeilkasko {

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
    public static class SelbstbeteiligungVollkasko {

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
    public static class WillSV {

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
