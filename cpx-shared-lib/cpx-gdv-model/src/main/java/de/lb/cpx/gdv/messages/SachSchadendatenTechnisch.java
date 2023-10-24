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
 *         &lt;element name="ProvisoriumAbDatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ProvisoriumBisDatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DatumEndgueltigeReparatur" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GeschaetzteAusfallzeitInTagen" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess52Typ" minOccurs="0"/>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="DatumWiederinbetriebname" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BauTeilleistungserstellungVonDatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BauTeilleistungserstellungBisDatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Fertigstellungsdatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Abnahmedatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BauwerkTeilweiseBenutzungAm" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BauwerkVollstaendigeBenutzungAm" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BauwerkAbgenommenAm" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MontageversicherungVonDatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MontageversicherungBisDatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DatumInbetriebnahme" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BisherigeBetriebsstunden" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess72Typ" minOccurs="0"/>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="GarantieversicherungVonDatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GarantieversicherungBisDatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ProbezeitVonDatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ProbezeitBisDatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SacheBereitsEingebaut" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4284">
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
    "provisoriumAbDatum",
    "provisoriumBisDatum",
    "datumEndgueltigeReparatur",
    "geschaetzteAusfallzeitInTagen",
    "datumWiederinbetriebname",
    "bauTeilleistungserstellungVonDatum",
    "bauTeilleistungserstellungBisDatum",
    "fertigstellungsdatum",
    "abnahmedatum",
    "bauwerkTeilweiseBenutzungAm",
    "bauwerkVollstaendigeBenutzungAm",
    "bauwerkAbgenommenAm",
    "montageversicherungVonDatum",
    "montageversicherungBisDatum",
    "datumInbetriebnahme",
    "bisherigeBetriebsstunden",
    "garantieversicherungVonDatum",
    "garantieversicherungBisDatum",
    "probezeitVonDatum",
    "probezeitBisDatum",
    "sacheBereitsEingebaut"
})
@XmlRootElement(name = "SachSchadendatenTechnisch")
public class SachSchadendatenTechnisch {

    protected String lfdNrBeschaedigtenSache;
    @XmlElement(name = "ProvisoriumAbDatum")
    protected String provisoriumAbDatum;
    @XmlElement(name = "ProvisoriumBisDatum")
    protected String provisoriumBisDatum;
    @XmlElement(name = "DatumEndgueltigeReparatur")
    protected String datumEndgueltigeReparatur;
    @XmlElement(name = "GeschaetzteAusfallzeitInTagen")
    protected SachSchadendatenTechnisch.GeschaetzteAusfallzeitInTagen geschaetzteAusfallzeitInTagen;
    @XmlElement(name = "DatumWiederinbetriebname")
    protected String datumWiederinbetriebname;
    @XmlElement(name = "BauTeilleistungserstellungVonDatum")
    protected String bauTeilleistungserstellungVonDatum;
    @XmlElement(name = "BauTeilleistungserstellungBisDatum")
    protected String bauTeilleistungserstellungBisDatum;
    @XmlElement(name = "Fertigstellungsdatum")
    protected String fertigstellungsdatum;
    @XmlElement(name = "Abnahmedatum")
    protected String abnahmedatum;
    @XmlElement(name = "BauwerkTeilweiseBenutzungAm")
    protected String bauwerkTeilweiseBenutzungAm;
    @XmlElement(name = "BauwerkVollstaendigeBenutzungAm")
    protected String bauwerkVollstaendigeBenutzungAm;
    @XmlElement(name = "BauwerkAbgenommenAm")
    protected String bauwerkAbgenommenAm;
    @XmlElement(name = "MontageversicherungVonDatum")
    protected String montageversicherungVonDatum;
    @XmlElement(name = "MontageversicherungBisDatum")
    protected String montageversicherungBisDatum;
    @XmlElement(name = "DatumInbetriebnahme")
    protected String datumInbetriebnahme;
    @XmlElement(name = "BisherigeBetriebsstunden")
    protected SachSchadendatenTechnisch.BisherigeBetriebsstunden bisherigeBetriebsstunden;
    @XmlElement(name = "GarantieversicherungVonDatum")
    protected String garantieversicherungVonDatum;
    @XmlElement(name = "GarantieversicherungBisDatum")
    protected String garantieversicherungBisDatum;
    @XmlElement(name = "ProbezeitVonDatum")
    protected String probezeitVonDatum;
    @XmlElement(name = "ProbezeitBisDatum")
    protected String probezeitBisDatum;
    @XmlElement(name = "SacheBereitsEingebaut")
    protected SachSchadendatenTechnisch.SacheBereitsEingebaut sacheBereitsEingebaut;
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
     * Ruft den Wert der provisoriumAbDatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvisoriumAbDatum() {
        return provisoriumAbDatum;
    }

    /**
     * Legt den Wert der provisoriumAbDatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvisoriumAbDatum(String value) {
        this.provisoriumAbDatum = value;
    }

    /**
     * Ruft den Wert der provisoriumBisDatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvisoriumBisDatum() {
        return provisoriumBisDatum;
    }

    /**
     * Legt den Wert der provisoriumBisDatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvisoriumBisDatum(String value) {
        this.provisoriumBisDatum = value;
    }

    /**
     * Ruft den Wert der datumEndgueltigeReparatur-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatumEndgueltigeReparatur() {
        return datumEndgueltigeReparatur;
    }

    /**
     * Legt den Wert der datumEndgueltigeReparatur-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatumEndgueltigeReparatur(String value) {
        this.datumEndgueltigeReparatur = value;
    }

    /**
     * Ruft den Wert der geschaetzteAusfallzeitInTagen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenTechnisch.GeschaetzteAusfallzeitInTagen }
     *     
     */
    public SachSchadendatenTechnisch.GeschaetzteAusfallzeitInTagen getGeschaetzteAusfallzeitInTagen() {
        return geschaetzteAusfallzeitInTagen;
    }

    /**
     * Legt den Wert der geschaetzteAusfallzeitInTagen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenTechnisch.GeschaetzteAusfallzeitInTagen }
     *     
     */
    public void setGeschaetzteAusfallzeitInTagen(SachSchadendatenTechnisch.GeschaetzteAusfallzeitInTagen value) {
        this.geschaetzteAusfallzeitInTagen = value;
    }

    /**
     * Ruft den Wert der datumWiederinbetriebname-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatumWiederinbetriebname() {
        return datumWiederinbetriebname;
    }

    /**
     * Legt den Wert der datumWiederinbetriebname-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatumWiederinbetriebname(String value) {
        this.datumWiederinbetriebname = value;
    }

    /**
     * Ruft den Wert der bauTeilleistungserstellungVonDatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBauTeilleistungserstellungVonDatum() {
        return bauTeilleistungserstellungVonDatum;
    }

    /**
     * Legt den Wert der bauTeilleistungserstellungVonDatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBauTeilleistungserstellungVonDatum(String value) {
        this.bauTeilleistungserstellungVonDatum = value;
    }

    /**
     * Ruft den Wert der bauTeilleistungserstellungBisDatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBauTeilleistungserstellungBisDatum() {
        return bauTeilleistungserstellungBisDatum;
    }

    /**
     * Legt den Wert der bauTeilleistungserstellungBisDatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBauTeilleistungserstellungBisDatum(String value) {
        this.bauTeilleistungserstellungBisDatum = value;
    }

    /**
     * Ruft den Wert der fertigstellungsdatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFertigstellungsdatum() {
        return fertigstellungsdatum;
    }

    /**
     * Legt den Wert der fertigstellungsdatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFertigstellungsdatum(String value) {
        this.fertigstellungsdatum = value;
    }

    /**
     * Ruft den Wert der abnahmedatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbnahmedatum() {
        return abnahmedatum;
    }

    /**
     * Legt den Wert der abnahmedatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbnahmedatum(String value) {
        this.abnahmedatum = value;
    }

    /**
     * Ruft den Wert der bauwerkTeilweiseBenutzungAm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBauwerkTeilweiseBenutzungAm() {
        return bauwerkTeilweiseBenutzungAm;
    }

    /**
     * Legt den Wert der bauwerkTeilweiseBenutzungAm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBauwerkTeilweiseBenutzungAm(String value) {
        this.bauwerkTeilweiseBenutzungAm = value;
    }

    /**
     * Ruft den Wert der bauwerkVollstaendigeBenutzungAm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBauwerkVollstaendigeBenutzungAm() {
        return bauwerkVollstaendigeBenutzungAm;
    }

    /**
     * Legt den Wert der bauwerkVollstaendigeBenutzungAm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBauwerkVollstaendigeBenutzungAm(String value) {
        this.bauwerkVollstaendigeBenutzungAm = value;
    }

    /**
     * Ruft den Wert der bauwerkAbgenommenAm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBauwerkAbgenommenAm() {
        return bauwerkAbgenommenAm;
    }

    /**
     * Legt den Wert der bauwerkAbgenommenAm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBauwerkAbgenommenAm(String value) {
        this.bauwerkAbgenommenAm = value;
    }

    /**
     * Ruft den Wert der montageversicherungVonDatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMontageversicherungVonDatum() {
        return montageversicherungVonDatum;
    }

    /**
     * Legt den Wert der montageversicherungVonDatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMontageversicherungVonDatum(String value) {
        this.montageversicherungVonDatum = value;
    }

    /**
     * Ruft den Wert der montageversicherungBisDatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMontageversicherungBisDatum() {
        return montageversicherungBisDatum;
    }

    /**
     * Legt den Wert der montageversicherungBisDatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMontageversicherungBisDatum(String value) {
        this.montageversicherungBisDatum = value;
    }

    /**
     * Ruft den Wert der datumInbetriebnahme-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatumInbetriebnahme() {
        return datumInbetriebnahme;
    }

    /**
     * Legt den Wert der datumInbetriebnahme-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatumInbetriebnahme(String value) {
        this.datumInbetriebnahme = value;
    }

    /**
     * Ruft den Wert der bisherigeBetriebsstunden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenTechnisch.BisherigeBetriebsstunden }
     *     
     */
    public SachSchadendatenTechnisch.BisherigeBetriebsstunden getBisherigeBetriebsstunden() {
        return bisherigeBetriebsstunden;
    }

    /**
     * Legt den Wert der bisherigeBetriebsstunden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenTechnisch.BisherigeBetriebsstunden }
     *     
     */
    public void setBisherigeBetriebsstunden(SachSchadendatenTechnisch.BisherigeBetriebsstunden value) {
        this.bisherigeBetriebsstunden = value;
    }

    /**
     * Ruft den Wert der garantieversicherungVonDatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGarantieversicherungVonDatum() {
        return garantieversicherungVonDatum;
    }

    /**
     * Legt den Wert der garantieversicherungVonDatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGarantieversicherungVonDatum(String value) {
        this.garantieversicherungVonDatum = value;
    }

    /**
     * Ruft den Wert der garantieversicherungBisDatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGarantieversicherungBisDatum() {
        return garantieversicherungBisDatum;
    }

    /**
     * Legt den Wert der garantieversicherungBisDatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGarantieversicherungBisDatum(String value) {
        this.garantieversicherungBisDatum = value;
    }

    /**
     * Ruft den Wert der probezeitVonDatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbezeitVonDatum() {
        return probezeitVonDatum;
    }

    /**
     * Legt den Wert der probezeitVonDatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbezeitVonDatum(String value) {
        this.probezeitVonDatum = value;
    }

    /**
     * Ruft den Wert der probezeitBisDatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbezeitBisDatum() {
        return probezeitBisDatum;
    }

    /**
     * Legt den Wert der probezeitBisDatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbezeitBisDatum(String value) {
        this.probezeitBisDatum = value;
    }

    /**
     * Ruft den Wert der sacheBereitsEingebaut-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenTechnisch.SacheBereitsEingebaut }
     *     
     */
    public SachSchadendatenTechnisch.SacheBereitsEingebaut getSacheBereitsEingebaut() {
        return sacheBereitsEingebaut;
    }

    /**
     * Legt den Wert der sacheBereitsEingebaut-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenTechnisch.SacheBereitsEingebaut }
     *     
     */
    public void setSacheBereitsEingebaut(SachSchadendatenTechnisch.SacheBereitsEingebaut value) {
        this.sacheBereitsEingebaut = value;
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
            return "4284";
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
     *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess72Typ" minOccurs="0"/>
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
    public static class BisherigeBetriebsstunden {

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
     *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess52Typ" minOccurs="0"/>
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
    public static class GeschaetzteAusfallzeitInTagen {

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
    public static class SacheBereitsEingebaut {

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
