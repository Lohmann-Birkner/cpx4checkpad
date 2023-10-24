//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.19 um 11:55:14 AM CEST 
//


package de.lb.cpx.gdv.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="VU-Nr" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VU-NummernTyp" minOccurs="0"/>
 *         &lt;element name="Versicherungsschein-Nr" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="17"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Schaden-Nr" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Aktenzeichen" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
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
    "vuNr",
    "versicherungsscheinNr",
    "schadenNr",
    "aktenzeichen"
})
@XmlRootElement(name = "Header")
public class Header {

    @XmlElement(name = "VU-Nr")
    protected String vuNr;
    @XmlElement(name = "Versicherungsschein-Nr")
    protected String versicherungsscheinNr;
    @XmlElement(name = "Schaden-Nr")
    protected String schadenNr;
    @XmlElement(name = "Aktenzeichen")
    protected String aktenzeichen;

    /**
     * Ruft den Wert der vuNr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVUNr() {
        return vuNr;
    }

    /**
     * Legt den Wert der vuNr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVUNr(String value) {
        this.vuNr = value;
    }

    /**
     * Ruft den Wert der versicherungsscheinNr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersicherungsscheinNr() {
        return versicherungsscheinNr;
    }

    /**
     * Legt den Wert der versicherungsscheinNr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersicherungsscheinNr(String value) {
        this.versicherungsscheinNr = value;
    }

    /**
     * Ruft den Wert der schadenNr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenNr() {
        return schadenNr;
    }

    /**
     * Legt den Wert der schadenNr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenNr(String value) {
        this.schadenNr = value;
    }

    /**
     * Ruft den Wert der aktenzeichen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAktenzeichen() {
        return aktenzeichen;
    }

    /**
     * Legt den Wert der aktenzeichen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAktenzeichen(String value) {
        this.aktenzeichen = value;
    }

}
