//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.19 um 11:55:14 AM CEST 
//


package de.lb.cpx.gdv.messages;

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
 *         &lt;element name="Absender">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Abs-DLNR" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}DienstleisternummerTyp"/>
 *                   &lt;element name="Abs-DLPNR">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="26"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Abs-OrdNr-DLP" minOccurs="0">
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
 *         &lt;element name="Empfaenger">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Empf-DLNR" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}DienstleisternummerTyp"/>
 *                   &lt;element name="Empf-DLPNR">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="26"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Empf-OrdNr-DLP" minOccurs="0">
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
 *         &lt;element name="Dienstleistungserbringer" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}DienstleisternummerTyp" minOccurs="0"/>
 *         &lt;element name="LKZ-VN" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LaenderkennzeichenTyp" minOccurs="0"/>
 *         &lt;element name="Kfz-Kennzeichen" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="12"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="VSNR" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="VU-Referenz" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Erstellungsdatum">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Erstellungsuhrzeit">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *               &lt;minLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ReferenzierungAbsender">
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
 *         &lt;element name="ReleaseNummer">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="0"/>
 *               &lt;totalDigits value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Geschaeftsvorfall" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="VersandquittungErwuenscht">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinMussTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4001">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="4"/>
 *             &lt;minLength value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Versionsnummer" fixed="004">
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
    "absender",
    "empfaenger",
    "dienstleistungserbringer",
    "lkzvn",
    "kfzKennzeichen",
    "vsnr",
    "vuReferenz",
    "erstellungsdatum",
    "erstellungsuhrzeit",
    "referenzierungAbsender",
    "releaseNummer",
    "geschaeftsvorfall",
    "versandquittungErwuenscht"
})
@XmlRootElement(name = "Vorsatz")
public class Vorsatz {

    @XmlElement(name = "Absender", required = true)
    protected Vorsatz.Absender absender;
    @XmlElement(name = "Empfaenger", required = true)
    protected Vorsatz.Empfaenger empfaenger;
    @XmlElement(name = "Dienstleistungserbringer")
    protected String dienstleistungserbringer;
    @XmlElement(name = "LKZ-VN")
    protected String lkzvn;
    @XmlElement(name = "Kfz-Kennzeichen")
    protected String kfzKennzeichen;
    @XmlElement(name = "VSNR")
    protected Vorsatz.VSNR vsnr;
    @XmlElement(name = "VU-Referenz")
    protected String vuReferenz;
    @XmlElement(name = "Erstellungsdatum", required = true)
    protected String erstellungsdatum;
    @XmlElement(name = "Erstellungsuhrzeit", required = true)
    protected String erstellungsuhrzeit;
    @XmlElement(name = "ReferenzierungAbsender", required = true)
    protected Vorsatz.ReferenzierungAbsender referenzierungAbsender;
    @XmlElement(name = "ReleaseNummer", required = true)
    protected BigInteger releaseNummer;
    @XmlElement(name = "Geschaeftsvorfall")
    protected String geschaeftsvorfall;
    @XmlElement(name = "VersandquittungErwuenscht", required = true)
    protected Vorsatz.VersandquittungErwuenscht versandquittungErwuenscht;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der absender-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Vorsatz.Absender }
     *     
     */
    public Vorsatz.Absender getAbsender() {
        return absender;
    }

    /**
     * Legt den Wert der absender-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Vorsatz.Absender }
     *     
     */
    public void setAbsender(Vorsatz.Absender value) {
        this.absender = value;
    }

    /**
     * Ruft den Wert der empfaenger-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Vorsatz.Empfaenger }
     *     
     */
    public Vorsatz.Empfaenger getEmpfaenger() {
        return empfaenger;
    }

    /**
     * Legt den Wert der empfaenger-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Vorsatz.Empfaenger }
     *     
     */
    public void setEmpfaenger(Vorsatz.Empfaenger value) {
        this.empfaenger = value;
    }

    /**
     * Ruft den Wert der dienstleistungserbringer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDienstleistungserbringer() {
        return dienstleistungserbringer;
    }

    /**
     * Legt den Wert der dienstleistungserbringer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDienstleistungserbringer(String value) {
        this.dienstleistungserbringer = value;
    }

    /**
     * Ruft den Wert der lkzvn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLKZVN() {
        return lkzvn;
    }

    /**
     * Legt den Wert der lkzvn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLKZVN(String value) {
        this.lkzvn = value;
    }

    /**
     * Ruft den Wert der kfzKennzeichen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKfzKennzeichen() {
        return kfzKennzeichen;
    }

    /**
     * Legt den Wert der kfzKennzeichen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKfzKennzeichen(String value) {
        this.kfzKennzeichen = value;
    }

    /**
     * Ruft den Wert der vsnr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Vorsatz.VSNR }
     *     
     */
    public Vorsatz.VSNR getVSNR() {
        return vsnr;
    }

    /**
     * Legt den Wert der vsnr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Vorsatz.VSNR }
     *     
     */
    public void setVSNR(Vorsatz.VSNR value) {
        this.vsnr = value;
    }

    /**
     * Ruft den Wert der vuReferenz-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVUReferenz() {
        return vuReferenz;
    }

    /**
     * Legt den Wert der vuReferenz-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVUReferenz(String value) {
        this.vuReferenz = value;
    }

    /**
     * Ruft den Wert der erstellungsdatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErstellungsdatum() {
        return erstellungsdatum;
    }

    /**
     * Legt den Wert der erstellungsdatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErstellungsdatum(String value) {
        this.erstellungsdatum = value;
    }

    /**
     * Ruft den Wert der erstellungsuhrzeit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErstellungsuhrzeit() {
        return erstellungsuhrzeit;
    }

    /**
     * Legt den Wert der erstellungsuhrzeit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErstellungsuhrzeit(String value) {
        this.erstellungsuhrzeit = value;
    }

    /**
     * Ruft den Wert der referenzierungAbsender-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Vorsatz.ReferenzierungAbsender }
     *     
     */
    public Vorsatz.ReferenzierungAbsender getReferenzierungAbsender() {
        return referenzierungAbsender;
    }

    /**
     * Legt den Wert der referenzierungAbsender-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Vorsatz.ReferenzierungAbsender }
     *     
     */
    public void setReferenzierungAbsender(Vorsatz.ReferenzierungAbsender value) {
        this.referenzierungAbsender = value;
    }

    /**
     * Ruft den Wert der releaseNummer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getReleaseNummer() {
        return releaseNummer;
    }

    /**
     * Legt den Wert der releaseNummer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setReleaseNummer(BigInteger value) {
        this.releaseNummer = value;
    }

    /**
     * Ruft den Wert der geschaeftsvorfall-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeschaeftsvorfall() {
        return geschaeftsvorfall;
    }

    /**
     * Legt den Wert der geschaeftsvorfall-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeschaeftsvorfall(String value) {
        this.geschaeftsvorfall = value;
    }

    /**
     * Ruft den Wert der versandquittungErwuenscht-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Vorsatz.VersandquittungErwuenscht }
     *     
     */
    public Vorsatz.VersandquittungErwuenscht getVersandquittungErwuenscht() {
        return versandquittungErwuenscht;
    }

    /**
     * Legt den Wert der versandquittungErwuenscht-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Vorsatz.VersandquittungErwuenscht }
     *     
     */
    public void setVersandquittungErwuenscht(Vorsatz.VersandquittungErwuenscht value) {
        this.versandquittungErwuenscht = value;
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
            return "4001";
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
            return "004";
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
     *         &lt;element name="Abs-DLNR" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}DienstleisternummerTyp"/>
     *         &lt;element name="Abs-DLPNR">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="26"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Abs-OrdNr-DLP" minOccurs="0">
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
        "absDLNR",
        "absDLPNR",
        "absOrdNrDLP"
    })
    public static class Absender {

        @XmlElement(name = "Abs-DLNR", required = true)
        protected String absDLNR;
        @XmlElement(name = "Abs-DLPNR", required = true)
        protected String absDLPNR;
        @XmlElement(name = "Abs-OrdNr-DLP")
        protected String absOrdNrDLP;

        /**
         * Ruft den Wert der absDLNR-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAbsDLNR() {
            return absDLNR;
        }

        /**
         * Legt den Wert der absDLNR-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAbsDLNR(String value) {
            this.absDLNR = value;
        }

        /**
         * Ruft den Wert der absDLPNR-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAbsDLPNR() {
            return absDLPNR;
        }

        /**
         * Legt den Wert der absDLPNR-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAbsDLPNR(String value) {
            this.absDLPNR = value;
        }

        /**
         * Ruft den Wert der absOrdNrDLP-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAbsOrdNrDLP() {
            return absOrdNrDLP;
        }

        /**
         * Legt den Wert der absOrdNrDLP-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAbsOrdNrDLP(String value) {
            this.absOrdNrDLP = value;
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
     *         &lt;element name="Empf-DLNR" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}DienstleisternummerTyp"/>
     *         &lt;element name="Empf-DLPNR">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="26"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Empf-OrdNr-DLP" minOccurs="0">
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
        "empfDLNR",
        "empfDLPNR",
        "empfOrdNrDLP"
    })
    public static class Empfaenger {

        @XmlElement(name = "Empf-DLNR", required = true)
        protected String empfDLNR;
        @XmlElement(name = "Empf-DLPNR", required = true)
        protected String empfDLPNR;
        @XmlElement(name = "Empf-OrdNr-DLP")
        protected String empfOrdNrDLP;

        /**
         * Ruft den Wert der empfDLNR-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEmpfDLNR() {
            return empfDLNR;
        }

        /**
         * Legt den Wert der empfDLNR-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEmpfDLNR(String value) {
            this.empfDLNR = value;
        }

        /**
         * Ruft den Wert der empfDLPNR-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEmpfDLPNR() {
            return empfDLPNR;
        }

        /**
         * Legt den Wert der empfDLPNR-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEmpfDLPNR(String value) {
            this.empfDLPNR = value;
        }

        /**
         * Ruft den Wert der empfOrdNrDLP-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEmpfOrdNrDLP() {
            return empfOrdNrDLP;
        }

        /**
         * Legt den Wert der empfOrdNrDLP-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEmpfOrdNrDLP(String value) {
            this.empfOrdNrDLP = value;
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
    public static class ReferenzierungAbsender {

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
    public static class VSNR
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
     *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinMussTyp">
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
    public static class VersandquittungErwuenscht {

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
