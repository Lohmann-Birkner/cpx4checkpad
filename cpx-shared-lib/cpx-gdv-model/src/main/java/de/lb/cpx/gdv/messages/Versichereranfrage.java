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
 *         &lt;element name="AnfrageDL" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}DienstleisternummerTyp" minOccurs="0"/>
 *         &lt;element name="AnfrageVU" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VU-NummernTyp" minOccurs="0"/>
 *         &lt;element name="Anfragegrund" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AnfragegrundTyp"/>
 *         &lt;element name="ArtDeckung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SparteTyp" minOccurs="0"/>
 *         &lt;element name="AmtlichesKennzeichen">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="12"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Stichtag">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Benutzerkennung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Passwort" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4530">
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
    "anfrageDL",
    "anfrageVU",
    "anfragegrund",
    "artDeckung",
    "amtlichesKennzeichen",
    "stichtag",
    "benutzerkennung",
    "passwort"
})
@XmlRootElement(name = "Versichereranfrage")
public class Versichereranfrage {

    @XmlElement(name = "AnfrageDL")
    protected String anfrageDL;
    @XmlElement(name = "AnfrageVU")
    protected String anfrageVU;
    @XmlElement(name = "Anfragegrund", required = true)
    protected String anfragegrund;
    @XmlElement(name = "ArtDeckung")
    protected String artDeckung;
    @XmlElement(name = "AmtlichesKennzeichen", required = true)
    protected String amtlichesKennzeichen;
    @XmlElement(name = "Stichtag", required = true)
    protected String stichtag;
    @XmlElement(name = "Benutzerkennung")
    protected String benutzerkennung;
    @XmlElement(name = "Passwort")
    protected Versichereranfrage.Passwort passwort;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der anfrageDL-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnfrageDL() {
        return anfrageDL;
    }

    /**
     * Legt den Wert der anfrageDL-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnfrageDL(String value) {
        this.anfrageDL = value;
    }

    /**
     * Ruft den Wert der anfrageVU-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnfrageVU() {
        return anfrageVU;
    }

    /**
     * Legt den Wert der anfrageVU-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnfrageVU(String value) {
        this.anfrageVU = value;
    }

    /**
     * Ruft den Wert der anfragegrund-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnfragegrund() {
        return anfragegrund;
    }

    /**
     * Legt den Wert der anfragegrund-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnfragegrund(String value) {
        this.anfragegrund = value;
    }

    /**
     * Ruft den Wert der artDeckung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArtDeckung() {
        return artDeckung;
    }

    /**
     * Legt den Wert der artDeckung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtDeckung(String value) {
        this.artDeckung = value;
    }

    /**
     * Ruft den Wert der amtlichesKennzeichen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmtlichesKennzeichen() {
        return amtlichesKennzeichen;
    }

    /**
     * Legt den Wert der amtlichesKennzeichen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmtlichesKennzeichen(String value) {
        this.amtlichesKennzeichen = value;
    }

    /**
     * Ruft den Wert der stichtag-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStichtag() {
        return stichtag;
    }

    /**
     * Legt den Wert der stichtag-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStichtag(String value) {
        this.stichtag = value;
    }

    /**
     * Ruft den Wert der benutzerkennung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBenutzerkennung() {
        return benutzerkennung;
    }

    /**
     * Legt den Wert der benutzerkennung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBenutzerkennung(String value) {
        this.benutzerkennung = value;
    }

    /**
     * Ruft den Wert der passwort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Versichereranfrage.Passwort }
     *     
     */
    public Versichereranfrage.Passwort getPasswort() {
        return passwort;
    }

    /**
     * Legt den Wert der passwort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Versichereranfrage.Passwort }
     *     
     */
    public void setPasswort(Versichereranfrage.Passwort value) {
        this.passwort = value;
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
            return "4530";
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
    public static class Passwort
        extends SatzendeTyp
    {


    }

}
