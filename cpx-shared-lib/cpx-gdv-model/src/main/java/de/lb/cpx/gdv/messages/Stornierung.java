//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.19 um 11:55:14 AM CEST 
//


package de.lb.cpx.gdv.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="ReferenzierungPartner">
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
 *         &lt;element name="StornierungDurch" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}StorniererTyp"/>
 *         &lt;element name="Stornobeschreibung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="StornoGrund" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}StornogrundTyp"/>
 *         &lt;element name="BeschreibungGrund" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Auftragsreferenz" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4870">
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
    "referenzierungPartner",
    "stornierungDurch",
    "stornobeschreibung",
    "stornoGrund",
    "beschreibungGrund",
    "auftragsreferenz"
})
@XmlRootElement(name = "Stornierung")
public class Stornierung {

    @XmlElement(name = "ReferenzierungPartner", required = true)
    protected Stornierung.ReferenzierungPartner referenzierungPartner;
    @XmlElement(name = "StornierungDurch", required = true)
    protected String stornierungDurch;
    @XmlElement(name = "Stornobeschreibung")
    protected String stornobeschreibung;
    @XmlElement(name = "StornoGrund", required = true)
    protected String stornoGrund;
    @XmlElement(name = "BeschreibungGrund")
    protected Stornierung.BeschreibungGrund beschreibungGrund;
    @XmlElement(name = "Auftragsreferenz")
    protected Stornierung.Auftragsreferenz auftragsreferenz;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der referenzierungPartner-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Stornierung.ReferenzierungPartner }
     *     
     */
    public Stornierung.ReferenzierungPartner getReferenzierungPartner() {
        return referenzierungPartner;
    }

    /**
     * Legt den Wert der referenzierungPartner-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Stornierung.ReferenzierungPartner }
     *     
     */
    public void setReferenzierungPartner(Stornierung.ReferenzierungPartner value) {
        this.referenzierungPartner = value;
    }

    /**
     * Ruft den Wert der stornierungDurch-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStornierungDurch() {
        return stornierungDurch;
    }

    /**
     * Legt den Wert der stornierungDurch-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStornierungDurch(String value) {
        this.stornierungDurch = value;
    }

    /**
     * Ruft den Wert der stornobeschreibung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStornobeschreibung() {
        return stornobeschreibung;
    }

    /**
     * Legt den Wert der stornobeschreibung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStornobeschreibung(String value) {
        this.stornobeschreibung = value;
    }

    /**
     * Ruft den Wert der stornoGrund-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStornoGrund() {
        return stornoGrund;
    }

    /**
     * Legt den Wert der stornoGrund-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStornoGrund(String value) {
        this.stornoGrund = value;
    }

    /**
     * Ruft den Wert der beschreibungGrund-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Stornierung.BeschreibungGrund }
     *     
     */
    public Stornierung.BeschreibungGrund getBeschreibungGrund() {
        return beschreibungGrund;
    }

    /**
     * Legt den Wert der beschreibungGrund-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Stornierung.BeschreibungGrund }
     *     
     */
    public void setBeschreibungGrund(Stornierung.BeschreibungGrund value) {
        this.beschreibungGrund = value;
    }

    /**
     * Ruft den Wert der auftragsreferenz-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Stornierung.Auftragsreferenz }
     *     
     */
    public Stornierung.Auftragsreferenz getAuftragsreferenz() {
        return auftragsreferenz;
    }

    /**
     * Legt den Wert der auftragsreferenz-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Stornierung.Auftragsreferenz }
     *     
     */
    public void setAuftragsreferenz(Stornierung.Auftragsreferenz value) {
        this.auftragsreferenz = value;
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
            return "4870";
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
    public static class Auftragsreferenz
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
    public static class BeschreibungGrund
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
    public static class ReferenzierungPartner {

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

}
