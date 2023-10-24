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
 *         &lt;element name="Leistungsart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LeistungserbringerTyp" minOccurs="0"/>
 *         &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
 *         &lt;element name="Prozentsatz" minOccurs="0">
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
 *         &lt;element name="Abrechnungsart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LeistungsabrechnungTyp" minOccurs="0"/>
 *         &lt;element name="BetragGebuehrenNetto" minOccurs="0">
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
 *         &lt;element name="Nebenkosten1" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
 *                   &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
 *         &lt;element name="Nebenkosten2" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
 *                   &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
 *         &lt;element name="Nebenkosten3" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
 *                   &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
 *         &lt;element name="Nebenkosten4" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
 *                   &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
 *         &lt;element name="Nebenkosten5" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
 *                   &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
 *         &lt;element name="Nebenkosten6" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
 *                   &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
 *         &lt;element name="Nebenkosten7" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
 *                   &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
 *         &lt;element name="Nebenkosten8" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
 *                   &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
 *         &lt;element name="Nebenkosten9" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
 *                   &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
 *         &lt;element name="GesamtbetragLeistungsartBrutto" minOccurs="0">
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
 *       &lt;attribute name="Satzart" fixed="4302">
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
    "leistungsart",
    "waehrungsschluessel",
    "prozentsatz",
    "abrechnungsart",
    "betragGebuehrenNetto",
    "nebenkosten1",
    "nebenkosten2",
    "nebenkosten3",
    "nebenkosten4",
    "nebenkosten5",
    "nebenkosten6",
    "nebenkosten7",
    "nebenkosten8",
    "nebenkosten9",
    "gesamtbetragLeistungsartBrutto"
})
@XmlRootElement(name = "Leistungabrechnung")
public class Leistungabrechnung {

    @XmlElement(name = "Leistungsart")
    protected String leistungsart;
    @XmlElement(name = "Waehrungsschluessel")
    protected String waehrungsschluessel;
    @XmlElement(name = "Prozentsatz")
    protected Leistungabrechnung.Prozentsatz prozentsatz;
    @XmlElement(name = "Abrechnungsart")
    protected String abrechnungsart;
    @XmlElement(name = "BetragGebuehrenNetto")
    protected Leistungabrechnung.BetragGebuehrenNetto betragGebuehrenNetto;
    @XmlElement(name = "Nebenkosten1")
    protected Leistungabrechnung.Nebenkosten1 nebenkosten1;
    @XmlElement(name = "Nebenkosten2")
    protected Leistungabrechnung.Nebenkosten2 nebenkosten2;
    @XmlElement(name = "Nebenkosten3")
    protected Leistungabrechnung.Nebenkosten3 nebenkosten3;
    @XmlElement(name = "Nebenkosten4")
    protected Leistungabrechnung.Nebenkosten4 nebenkosten4;
    @XmlElement(name = "Nebenkosten5")
    protected Leistungabrechnung.Nebenkosten5 nebenkosten5;
    @XmlElement(name = "Nebenkosten6")
    protected Leistungabrechnung.Nebenkosten6 nebenkosten6;
    @XmlElement(name = "Nebenkosten7")
    protected Leistungabrechnung.Nebenkosten7 nebenkosten7;
    @XmlElement(name = "Nebenkosten8")
    protected Leistungabrechnung.Nebenkosten8 nebenkosten8;
    @XmlElement(name = "Nebenkosten9")
    protected Leistungabrechnung.Nebenkosten9 nebenkosten9;
    @XmlElement(name = "GesamtbetragLeistungsartBrutto")
    protected Leistungabrechnung.GesamtbetragLeistungsartBrutto gesamtbetragLeistungsartBrutto;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der leistungsart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLeistungsart() {
        return leistungsart;
    }

    /**
     * Legt den Wert der leistungsart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLeistungsart(String value) {
        this.leistungsart = value;
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
     * Ruft den Wert der prozentsatz-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Leistungabrechnung.Prozentsatz }
     *     
     */
    public Leistungabrechnung.Prozentsatz getProzentsatz() {
        return prozentsatz;
    }

    /**
     * Legt den Wert der prozentsatz-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Leistungabrechnung.Prozentsatz }
     *     
     */
    public void setProzentsatz(Leistungabrechnung.Prozentsatz value) {
        this.prozentsatz = value;
    }

    /**
     * Ruft den Wert der abrechnungsart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbrechnungsart() {
        return abrechnungsart;
    }

    /**
     * Legt den Wert der abrechnungsart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbrechnungsart(String value) {
        this.abrechnungsart = value;
    }

    /**
     * Ruft den Wert der betragGebuehrenNetto-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Leistungabrechnung.BetragGebuehrenNetto }
     *     
     */
    public Leistungabrechnung.BetragGebuehrenNetto getBetragGebuehrenNetto() {
        return betragGebuehrenNetto;
    }

    /**
     * Legt den Wert der betragGebuehrenNetto-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Leistungabrechnung.BetragGebuehrenNetto }
     *     
     */
    public void setBetragGebuehrenNetto(Leistungabrechnung.BetragGebuehrenNetto value) {
        this.betragGebuehrenNetto = value;
    }

    /**
     * Ruft den Wert der nebenkosten1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Leistungabrechnung.Nebenkosten1 }
     *     
     */
    public Leistungabrechnung.Nebenkosten1 getNebenkosten1() {
        return nebenkosten1;
    }

    /**
     * Legt den Wert der nebenkosten1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Leistungabrechnung.Nebenkosten1 }
     *     
     */
    public void setNebenkosten1(Leistungabrechnung.Nebenkosten1 value) {
        this.nebenkosten1 = value;
    }

    /**
     * Ruft den Wert der nebenkosten2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Leistungabrechnung.Nebenkosten2 }
     *     
     */
    public Leistungabrechnung.Nebenkosten2 getNebenkosten2() {
        return nebenkosten2;
    }

    /**
     * Legt den Wert der nebenkosten2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Leistungabrechnung.Nebenkosten2 }
     *     
     */
    public void setNebenkosten2(Leistungabrechnung.Nebenkosten2 value) {
        this.nebenkosten2 = value;
    }

    /**
     * Ruft den Wert der nebenkosten3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Leistungabrechnung.Nebenkosten3 }
     *     
     */
    public Leistungabrechnung.Nebenkosten3 getNebenkosten3() {
        return nebenkosten3;
    }

    /**
     * Legt den Wert der nebenkosten3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Leistungabrechnung.Nebenkosten3 }
     *     
     */
    public void setNebenkosten3(Leistungabrechnung.Nebenkosten3 value) {
        this.nebenkosten3 = value;
    }

    /**
     * Ruft den Wert der nebenkosten4-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Leistungabrechnung.Nebenkosten4 }
     *     
     */
    public Leistungabrechnung.Nebenkosten4 getNebenkosten4() {
        return nebenkosten4;
    }

    /**
     * Legt den Wert der nebenkosten4-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Leistungabrechnung.Nebenkosten4 }
     *     
     */
    public void setNebenkosten4(Leistungabrechnung.Nebenkosten4 value) {
        this.nebenkosten4 = value;
    }

    /**
     * Ruft den Wert der nebenkosten5-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Leistungabrechnung.Nebenkosten5 }
     *     
     */
    public Leistungabrechnung.Nebenkosten5 getNebenkosten5() {
        return nebenkosten5;
    }

    /**
     * Legt den Wert der nebenkosten5-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Leistungabrechnung.Nebenkosten5 }
     *     
     */
    public void setNebenkosten5(Leistungabrechnung.Nebenkosten5 value) {
        this.nebenkosten5 = value;
    }

    /**
     * Ruft den Wert der nebenkosten6-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Leistungabrechnung.Nebenkosten6 }
     *     
     */
    public Leistungabrechnung.Nebenkosten6 getNebenkosten6() {
        return nebenkosten6;
    }

    /**
     * Legt den Wert der nebenkosten6-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Leistungabrechnung.Nebenkosten6 }
     *     
     */
    public void setNebenkosten6(Leistungabrechnung.Nebenkosten6 value) {
        this.nebenkosten6 = value;
    }

    /**
     * Ruft den Wert der nebenkosten7-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Leistungabrechnung.Nebenkosten7 }
     *     
     */
    public Leistungabrechnung.Nebenkosten7 getNebenkosten7() {
        return nebenkosten7;
    }

    /**
     * Legt den Wert der nebenkosten7-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Leistungabrechnung.Nebenkosten7 }
     *     
     */
    public void setNebenkosten7(Leistungabrechnung.Nebenkosten7 value) {
        this.nebenkosten7 = value;
    }

    /**
     * Ruft den Wert der nebenkosten8-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Leistungabrechnung.Nebenkosten8 }
     *     
     */
    public Leistungabrechnung.Nebenkosten8 getNebenkosten8() {
        return nebenkosten8;
    }

    /**
     * Legt den Wert der nebenkosten8-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Leistungabrechnung.Nebenkosten8 }
     *     
     */
    public void setNebenkosten8(Leistungabrechnung.Nebenkosten8 value) {
        this.nebenkosten8 = value;
    }

    /**
     * Ruft den Wert der nebenkosten9-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Leistungabrechnung.Nebenkosten9 }
     *     
     */
    public Leistungabrechnung.Nebenkosten9 getNebenkosten9() {
        return nebenkosten9;
    }

    /**
     * Legt den Wert der nebenkosten9-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Leistungabrechnung.Nebenkosten9 }
     *     
     */
    public void setNebenkosten9(Leistungabrechnung.Nebenkosten9 value) {
        this.nebenkosten9 = value;
    }

    /**
     * Ruft den Wert der gesamtbetragLeistungsartBrutto-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Leistungabrechnung.GesamtbetragLeistungsartBrutto }
     *     
     */
    public Leistungabrechnung.GesamtbetragLeistungsartBrutto getGesamtbetragLeistungsartBrutto() {
        return gesamtbetragLeistungsartBrutto;
    }

    /**
     * Legt den Wert der gesamtbetragLeistungsartBrutto-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Leistungabrechnung.GesamtbetragLeistungsartBrutto }
     *     
     */
    public void setGesamtbetragLeistungsartBrutto(Leistungabrechnung.GesamtbetragLeistungsartBrutto value) {
        this.gesamtbetragLeistungsartBrutto = value;
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
            return "4302";
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
    public static class BetragGebuehrenNetto {

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
    public static class GesamtbetragLeistungsartBrutto {

        @XmlElement(name = "Wert")
        protected BigDecimal wert;
        @XmlElement(name = "Indikator", required = true)
        protected Leistungabrechnung.GesamtbetragLeistungsartBrutto.Indikator indikator;

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
         *     {@link Leistungabrechnung.GesamtbetragLeistungsartBrutto.Indikator }
         *     
         */
        public Leistungabrechnung.GesamtbetragLeistungsartBrutto.Indikator getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Leistungabrechnung.GesamtbetragLeistungsartBrutto.Indikator }
         *     
         */
        public void setIndikator(Leistungabrechnung.GesamtbetragLeistungsartBrutto.Indikator value) {
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
     *         &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
     *         &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
        "nebenkostenart",
        "betragNebenkostenNetto",
        "indikator"
    })
    public static class Nebenkosten1 {

        @XmlElement(name = "Nebenkostenart")
        protected String nebenkostenart;
        @XmlElement(name = "BetragNebenkostenNetto")
        protected BigDecimal betragNebenkostenNetto;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der nebenkostenart-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNebenkostenart() {
            return nebenkostenart;
        }

        /**
         * Legt den Wert der nebenkostenart-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNebenkostenart(String value) {
            this.nebenkostenart = value;
        }

        /**
         * Ruft den Wert der betragNebenkostenNetto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getBetragNebenkostenNetto() {
            return betragNebenkostenNetto;
        }

        /**
         * Legt den Wert der betragNebenkostenNetto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setBetragNebenkostenNetto(BigDecimal value) {
            this.betragNebenkostenNetto = value;
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
     *         &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
     *         &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
        "nebenkostenart",
        "betragNebenkostenNetto",
        "indikator"
    })
    public static class Nebenkosten2 {

        @XmlElement(name = "Nebenkostenart")
        protected String nebenkostenart;
        @XmlElement(name = "BetragNebenkostenNetto")
        protected BigDecimal betragNebenkostenNetto;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der nebenkostenart-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNebenkostenart() {
            return nebenkostenart;
        }

        /**
         * Legt den Wert der nebenkostenart-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNebenkostenart(String value) {
            this.nebenkostenart = value;
        }

        /**
         * Ruft den Wert der betragNebenkostenNetto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getBetragNebenkostenNetto() {
            return betragNebenkostenNetto;
        }

        /**
         * Legt den Wert der betragNebenkostenNetto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setBetragNebenkostenNetto(BigDecimal value) {
            this.betragNebenkostenNetto = value;
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
     *         &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
     *         &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
        "nebenkostenart",
        "betragNebenkostenNetto",
        "indikator"
    })
    public static class Nebenkosten3 {

        @XmlElement(name = "Nebenkostenart")
        protected String nebenkostenart;
        @XmlElement(name = "BetragNebenkostenNetto")
        protected BigDecimal betragNebenkostenNetto;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der nebenkostenart-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNebenkostenart() {
            return nebenkostenart;
        }

        /**
         * Legt den Wert der nebenkostenart-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNebenkostenart(String value) {
            this.nebenkostenart = value;
        }

        /**
         * Ruft den Wert der betragNebenkostenNetto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getBetragNebenkostenNetto() {
            return betragNebenkostenNetto;
        }

        /**
         * Legt den Wert der betragNebenkostenNetto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setBetragNebenkostenNetto(BigDecimal value) {
            this.betragNebenkostenNetto = value;
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
     *         &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
     *         &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
        "nebenkostenart",
        "betragNebenkostenNetto",
        "indikator"
    })
    public static class Nebenkosten4 {

        @XmlElement(name = "Nebenkostenart")
        protected String nebenkostenart;
        @XmlElement(name = "BetragNebenkostenNetto")
        protected BigDecimal betragNebenkostenNetto;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der nebenkostenart-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNebenkostenart() {
            return nebenkostenart;
        }

        /**
         * Legt den Wert der nebenkostenart-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNebenkostenart(String value) {
            this.nebenkostenart = value;
        }

        /**
         * Ruft den Wert der betragNebenkostenNetto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getBetragNebenkostenNetto() {
            return betragNebenkostenNetto;
        }

        /**
         * Legt den Wert der betragNebenkostenNetto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setBetragNebenkostenNetto(BigDecimal value) {
            this.betragNebenkostenNetto = value;
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
     *         &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
     *         &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
        "nebenkostenart",
        "betragNebenkostenNetto",
        "indikator"
    })
    public static class Nebenkosten5 {

        @XmlElement(name = "Nebenkostenart")
        protected String nebenkostenart;
        @XmlElement(name = "BetragNebenkostenNetto")
        protected BigDecimal betragNebenkostenNetto;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der nebenkostenart-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNebenkostenart() {
            return nebenkostenart;
        }

        /**
         * Legt den Wert der nebenkostenart-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNebenkostenart(String value) {
            this.nebenkostenart = value;
        }

        /**
         * Ruft den Wert der betragNebenkostenNetto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getBetragNebenkostenNetto() {
            return betragNebenkostenNetto;
        }

        /**
         * Legt den Wert der betragNebenkostenNetto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setBetragNebenkostenNetto(BigDecimal value) {
            this.betragNebenkostenNetto = value;
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
     *         &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
     *         &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
        "nebenkostenart",
        "betragNebenkostenNetto",
        "indikator"
    })
    public static class Nebenkosten6 {

        @XmlElement(name = "Nebenkostenart")
        protected String nebenkostenart;
        @XmlElement(name = "BetragNebenkostenNetto")
        protected BigDecimal betragNebenkostenNetto;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der nebenkostenart-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNebenkostenart() {
            return nebenkostenart;
        }

        /**
         * Legt den Wert der nebenkostenart-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNebenkostenart(String value) {
            this.nebenkostenart = value;
        }

        /**
         * Ruft den Wert der betragNebenkostenNetto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getBetragNebenkostenNetto() {
            return betragNebenkostenNetto;
        }

        /**
         * Legt den Wert der betragNebenkostenNetto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setBetragNebenkostenNetto(BigDecimal value) {
            this.betragNebenkostenNetto = value;
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
     *         &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
     *         &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
        "nebenkostenart",
        "betragNebenkostenNetto",
        "indikator"
    })
    public static class Nebenkosten7 {

        @XmlElement(name = "Nebenkostenart")
        protected String nebenkostenart;
        @XmlElement(name = "BetragNebenkostenNetto")
        protected BigDecimal betragNebenkostenNetto;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der nebenkostenart-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNebenkostenart() {
            return nebenkostenart;
        }

        /**
         * Legt den Wert der nebenkostenart-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNebenkostenart(String value) {
            this.nebenkostenart = value;
        }

        /**
         * Ruft den Wert der betragNebenkostenNetto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getBetragNebenkostenNetto() {
            return betragNebenkostenNetto;
        }

        /**
         * Legt den Wert der betragNebenkostenNetto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setBetragNebenkostenNetto(BigDecimal value) {
            this.betragNebenkostenNetto = value;
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
     *         &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
     *         &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
        "nebenkostenart",
        "betragNebenkostenNetto",
        "indikator"
    })
    public static class Nebenkosten8 {

        @XmlElement(name = "Nebenkostenart")
        protected String nebenkostenart;
        @XmlElement(name = "BetragNebenkostenNetto")
        protected BigDecimal betragNebenkostenNetto;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der nebenkostenart-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNebenkostenart() {
            return nebenkostenart;
        }

        /**
         * Legt den Wert der nebenkostenart-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNebenkostenart(String value) {
            this.nebenkostenart = value;
        }

        /**
         * Ruft den Wert der betragNebenkostenNetto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getBetragNebenkostenNetto() {
            return betragNebenkostenNetto;
        }

        /**
         * Legt den Wert der betragNebenkostenNetto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setBetragNebenkostenNetto(BigDecimal value) {
            this.betragNebenkostenNetto = value;
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
     *         &lt;element name="Nebenkostenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}NebenkostenTyp" minOccurs="0"/>
     *         &lt;element name="BetragNebenkostenNetto" minOccurs="0">
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
        "nebenkostenart",
        "betragNebenkostenNetto",
        "indikator"
    })
    public static class Nebenkosten9 {

        @XmlElement(name = "Nebenkostenart")
        protected String nebenkostenart;
        @XmlElement(name = "BetragNebenkostenNetto")
        protected BigDecimal betragNebenkostenNetto;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der nebenkostenart-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNebenkostenart() {
            return nebenkostenart;
        }

        /**
         * Legt den Wert der nebenkostenart-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNebenkostenart(String value) {
            this.nebenkostenart = value;
        }

        /**
         * Ruft den Wert der betragNebenkostenNetto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getBetragNebenkostenNetto() {
            return betragNebenkostenNetto;
        }

        /**
         * Legt den Wert der betragNebenkostenNetto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setBetragNebenkostenNetto(BigDecimal value) {
            this.betragNebenkostenNetto = value;
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
    public static class Prozentsatz {

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
