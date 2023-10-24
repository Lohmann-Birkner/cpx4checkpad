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
 *         &lt;element name="VergleichsgeraetNr" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="0"/>
 *               &lt;totalDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
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
 *         &lt;element name="Herstellername">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ModellTypname">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
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
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
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
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
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
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Merkmal10" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp"/>
 *         &lt;element name="ProzentsatzMWSt">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert">
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
 *         &lt;element name="Neupreis" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="KostenBrutto" minOccurs="0">
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
 *                   &lt;element name="KostenNetto" minOccurs="0">
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
 *         &lt;element name="Ausstattungsunterschiede" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Ausstattungsunterschiede2" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4304">
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
    "vergleichsgeraetNr",
    "warengruppe",
    "produktgruppe",
    "herstellername",
    "modellTypname",
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
    "waehrungsschluessel",
    "prozentsatzMWSt",
    "neupreis",
    "ausstattungsunterschiede",
    "ausstattungsunterschiede2"
})
@XmlRootElement(name = "KalkulationVergleichbareSache")
public class KalkulationVergleichbareSache {

    protected BigInteger lfdNrBeschaedigtenSache;
    @XmlElement(name = "VergleichsgeraetNr")
    protected BigInteger vergleichsgeraetNr;
    @XmlElement(name = "Warengruppe", required = true)
    protected String warengruppe;
    @XmlElement(name = "Produktgruppe", required = true)
    protected String produktgruppe;
    @XmlElement(name = "Herstellername", required = true)
    protected String herstellername;
    @XmlElement(name = "ModellTypname", required = true)
    protected String modellTypname;
    @XmlElement(name = "Merkmal1", required = true)
    protected String merkmal1;
    @XmlElement(name = "Merkmal2")
    protected KalkulationVergleichbareSache.Merkmal2 merkmal2;
    @XmlElement(name = "Merkmal3")
    protected String merkmal3;
    @XmlElement(name = "Merkmal4")
    protected String merkmal4;
    @XmlElement(name = "Merkmal5")
    protected String merkmal5;
    @XmlElement(name = "Merkmal6")
    protected String merkmal6;
    @XmlElement(name = "Merkmal7")
    protected String merkmal7;
    @XmlElement(name = "Merkmal8")
    protected String merkmal8;
    @XmlElement(name = "Merkmal9")
    protected KalkulationVergleichbareSache.Merkmal9 merkmal9;
    @XmlElement(name = "Merkmal10")
    protected String merkmal10;
    @XmlElement(name = "Waehrungsschluessel", required = true)
    protected String waehrungsschluessel;
    @XmlElement(name = "ProzentsatzMWSt", required = true)
    protected KalkulationVergleichbareSache.ProzentsatzMWSt prozentsatzMWSt;
    @XmlElement(name = "Neupreis")
    protected KalkulationVergleichbareSache.Neupreis neupreis;
    @XmlElement(name = "Ausstattungsunterschiede")
    protected KalkulationVergleichbareSache.Ausstattungsunterschiede ausstattungsunterschiede;
    @XmlElement(name = "Ausstattungsunterschiede2")
    protected KalkulationVergleichbareSache.Ausstattungsunterschiede2 ausstattungsunterschiede2;
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
     * Ruft den Wert der vergleichsgeraetNr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getVergleichsgeraetNr() {
        return vergleichsgeraetNr;
    }

    /**
     * Legt den Wert der vergleichsgeraetNr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setVergleichsgeraetNr(BigInteger value) {
        this.vergleichsgeraetNr = value;
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
     *     {@link KalkulationVergleichbareSache.Merkmal2 }
     *     
     */
    public KalkulationVergleichbareSache.Merkmal2 getMerkmal2() {
        return merkmal2;
    }

    /**
     * Legt den Wert der merkmal2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationVergleichbareSache.Merkmal2 }
     *     
     */
    public void setMerkmal2(KalkulationVergleichbareSache.Merkmal2 value) {
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
     *     {@link String }
     *     
     */
    public String getMerkmal6() {
        return merkmal6;
    }

    /**
     * Legt den Wert der merkmal6-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerkmal6(String value) {
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
     *     {@link KalkulationVergleichbareSache.Merkmal9 }
     *     
     */
    public KalkulationVergleichbareSache.Merkmal9 getMerkmal9() {
        return merkmal9;
    }

    /**
     * Legt den Wert der merkmal9-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationVergleichbareSache.Merkmal9 }
     *     
     */
    public void setMerkmal9(KalkulationVergleichbareSache.Merkmal9 value) {
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
     * Ruft den Wert der prozentsatzMWSt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationVergleichbareSache.ProzentsatzMWSt }
     *     
     */
    public KalkulationVergleichbareSache.ProzentsatzMWSt getProzentsatzMWSt() {
        return prozentsatzMWSt;
    }

    /**
     * Legt den Wert der prozentsatzMWSt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationVergleichbareSache.ProzentsatzMWSt }
     *     
     */
    public void setProzentsatzMWSt(KalkulationVergleichbareSache.ProzentsatzMWSt value) {
        this.prozentsatzMWSt = value;
    }

    /**
     * Ruft den Wert der neupreis-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationVergleichbareSache.Neupreis }
     *     
     */
    public KalkulationVergleichbareSache.Neupreis getNeupreis() {
        return neupreis;
    }

    /**
     * Legt den Wert der neupreis-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationVergleichbareSache.Neupreis }
     *     
     */
    public void setNeupreis(KalkulationVergleichbareSache.Neupreis value) {
        this.neupreis = value;
    }

    /**
     * Ruft den Wert der ausstattungsunterschiede-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationVergleichbareSache.Ausstattungsunterschiede }
     *     
     */
    public KalkulationVergleichbareSache.Ausstattungsunterschiede getAusstattungsunterschiede() {
        return ausstattungsunterschiede;
    }

    /**
     * Legt den Wert der ausstattungsunterschiede-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationVergleichbareSache.Ausstattungsunterschiede }
     *     
     */
    public void setAusstattungsunterschiede(KalkulationVergleichbareSache.Ausstattungsunterschiede value) {
        this.ausstattungsunterschiede = value;
    }

    /**
     * Ruft den Wert der ausstattungsunterschiede2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationVergleichbareSache.Ausstattungsunterschiede2 }
     *     
     */
    public KalkulationVergleichbareSache.Ausstattungsunterschiede2 getAusstattungsunterschiede2() {
        return ausstattungsunterschiede2;
    }

    /**
     * Legt den Wert der ausstattungsunterschiede2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationVergleichbareSache.Ausstattungsunterschiede2 }
     *     
     */
    public void setAusstattungsunterschiede2(KalkulationVergleichbareSache.Ausstattungsunterschiede2 value) {
        this.ausstattungsunterschiede2 = value;
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
            return "4304";
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
    public static class Ausstattungsunterschiede
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
    public static class Ausstattungsunterschiede2
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
    public static class Merkmal2
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
    public static class Merkmal9
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
     *         &lt;element name="KostenBrutto" minOccurs="0">
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
     *         &lt;element name="KostenNetto" minOccurs="0">
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
        "kostenBrutto",
        "kostenNetto"
    })
    public static class Neupreis {

        @XmlElement(name = "KostenBrutto")
        protected KalkulationVergleichbareSache.Neupreis.KostenBrutto kostenBrutto;
        @XmlElement(name = "KostenNetto")
        protected KalkulationVergleichbareSache.Neupreis.KostenNetto kostenNetto;

        /**
         * Ruft den Wert der kostenBrutto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationVergleichbareSache.Neupreis.KostenBrutto }
         *     
         */
        public KalkulationVergleichbareSache.Neupreis.KostenBrutto getKostenBrutto() {
            return kostenBrutto;
        }

        /**
         * Legt den Wert der kostenBrutto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationVergleichbareSache.Neupreis.KostenBrutto }
         *     
         */
        public void setKostenBrutto(KalkulationVergleichbareSache.Neupreis.KostenBrutto value) {
            this.kostenBrutto = value;
        }

        /**
         * Ruft den Wert der kostenNetto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationVergleichbareSache.Neupreis.KostenNetto }
         *     
         */
        public KalkulationVergleichbareSache.Neupreis.KostenNetto getKostenNetto() {
            return kostenNetto;
        }

        /**
         * Legt den Wert der kostenNetto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationVergleichbareSache.Neupreis.KostenNetto }
         *     
         */
        public void setKostenNetto(KalkulationVergleichbareSache.Neupreis.KostenNetto value) {
            this.kostenNetto = value;
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
        public static class KostenBrutto {

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
        public static class KostenNetto {

            @XmlElement(name = "Wert")
            protected BigDecimal wert;
            @XmlElement(name = "Indikator", required = true)
            protected KalkulationVergleichbareSache.Neupreis.KostenNetto.Indikator indikator;

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
             *     {@link KalkulationVergleichbareSache.Neupreis.KostenNetto.Indikator }
             *     
             */
            public KalkulationVergleichbareSache.Neupreis.KostenNetto.Indikator getIndikator() {
                return indikator;
            }

            /**
             * Legt den Wert der indikator-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link KalkulationVergleichbareSache.Neupreis.KostenNetto.Indikator }
             *     
             */
            public void setIndikator(KalkulationVergleichbareSache.Neupreis.KostenNetto.Indikator value) {
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
     *         &lt;element name="Wert">
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
    public static class ProzentsatzMWSt {

        @XmlElement(name = "Wert", required = true)
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
