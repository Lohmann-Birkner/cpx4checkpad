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
 *         &lt;element name="Herstellername" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="15"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Modellname" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Fahrzeugtyp" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}FahrzeugtypGKTyp" minOccurs="0"/>
 *         &lt;element name="Kfz-Kennzeichen" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="12"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GrueneKarteNr" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GueltigAb" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GueltigBis" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Erstellungsdatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Erstellungsuhrzeit" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *               &lt;minLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AnschriftenIDMelders" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AnschriftenIDHalters" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="SOrtLKZ" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LaenderkennzeichenTyp" minOccurs="0"/>
 *         &lt;element name="SOrt-Ort" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Schadendatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Schadenmeldedatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Schaden-NrMelder" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LKZVerursacherFahrzeug" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>LaenderkennzeichenTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4212">
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
    "herstellername",
    "modellname",
    "fahrzeugtyp",
    "kfzKennzeichen",
    "grueneKarteNr",
    "gueltigAb",
    "gueltigBis",
    "erstellungsdatum",
    "erstellungsuhrzeit",
    "anschriftenIDMelders",
    "anschriftenIDHalters",
    "sOrtLKZ",
    "sOrtOrt",
    "schadendatum",
    "schadenmeldedatum",
    "schadenNrMelder",
    "lkzVerursacherFahrzeug"
})
@XmlRootElement(name = "KfzGK")
public class KfzGK {

    @XmlElement(name = "Herstellername")
    protected String herstellername;
    @XmlElement(name = "Modellname")
    protected String modellname;
    @XmlElement(name = "Fahrzeugtyp")
    protected String fahrzeugtyp;
    @XmlElement(name = "Kfz-Kennzeichen")
    protected String kfzKennzeichen;
    @XmlElement(name = "GrueneKarteNr")
    protected String grueneKarteNr;
    @XmlElement(name = "GueltigAb")
    protected String gueltigAb;
    @XmlElement(name = "GueltigBis")
    protected String gueltigBis;
    @XmlElement(name = "Erstellungsdatum")
    protected String erstellungsdatum;
    @XmlElement(name = "Erstellungsuhrzeit")
    protected String erstellungsuhrzeit;
    @XmlElement(name = "AnschriftenIDMelders")
    protected String anschriftenIDMelders;
    @XmlElement(name = "AnschriftenIDHalters")
    protected KfzGK.AnschriftenIDHalters anschriftenIDHalters;
    @XmlElement(name = "SOrtLKZ")
    protected String sOrtLKZ;
    @XmlElement(name = "SOrt-Ort")
    protected String sOrtOrt;
    @XmlElement(name = "Schadendatum")
    protected String schadendatum;
    @XmlElement(name = "Schadenmeldedatum")
    protected String schadenmeldedatum;
    @XmlElement(name = "Schaden-NrMelder")
    protected String schadenNrMelder;
    @XmlElement(name = "LKZVerursacherFahrzeug")
    protected KfzGK.LKZVerursacherFahrzeug lkzVerursacherFahrzeug;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

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
     * Ruft den Wert der modellname-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModellname() {
        return modellname;
    }

    /**
     * Legt den Wert der modellname-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModellname(String value) {
        this.modellname = value;
    }

    /**
     * Ruft den Wert der fahrzeugtyp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFahrzeugtyp() {
        return fahrzeugtyp;
    }

    /**
     * Legt den Wert der fahrzeugtyp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFahrzeugtyp(String value) {
        this.fahrzeugtyp = value;
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
     * Ruft den Wert der grueneKarteNr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrueneKarteNr() {
        return grueneKarteNr;
    }

    /**
     * Legt den Wert der grueneKarteNr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrueneKarteNr(String value) {
        this.grueneKarteNr = value;
    }

    /**
     * Ruft den Wert der gueltigAb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGueltigAb() {
        return gueltigAb;
    }

    /**
     * Legt den Wert der gueltigAb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGueltigAb(String value) {
        this.gueltigAb = value;
    }

    /**
     * Ruft den Wert der gueltigBis-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGueltigBis() {
        return gueltigBis;
    }

    /**
     * Legt den Wert der gueltigBis-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGueltigBis(String value) {
        this.gueltigBis = value;
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
     * Ruft den Wert der anschriftenIDMelders-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnschriftenIDMelders() {
        return anschriftenIDMelders;
    }

    /**
     * Legt den Wert der anschriftenIDMelders-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnschriftenIDMelders(String value) {
        this.anschriftenIDMelders = value;
    }

    /**
     * Ruft den Wert der anschriftenIDHalters-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzGK.AnschriftenIDHalters }
     *     
     */
    public KfzGK.AnschriftenIDHalters getAnschriftenIDHalters() {
        return anschriftenIDHalters;
    }

    /**
     * Legt den Wert der anschriftenIDHalters-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzGK.AnschriftenIDHalters }
     *     
     */
    public void setAnschriftenIDHalters(KfzGK.AnschriftenIDHalters value) {
        this.anschriftenIDHalters = value;
    }

    /**
     * Ruft den Wert der sOrtLKZ-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSOrtLKZ() {
        return sOrtLKZ;
    }

    /**
     * Legt den Wert der sOrtLKZ-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSOrtLKZ(String value) {
        this.sOrtLKZ = value;
    }

    /**
     * Ruft den Wert der sOrtOrt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSOrtOrt() {
        return sOrtOrt;
    }

    /**
     * Legt den Wert der sOrtOrt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSOrtOrt(String value) {
        this.sOrtOrt = value;
    }

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
     * Ruft den Wert der schadenmeldedatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenmeldedatum() {
        return schadenmeldedatum;
    }

    /**
     * Legt den Wert der schadenmeldedatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenmeldedatum(String value) {
        this.schadenmeldedatum = value;
    }

    /**
     * Ruft den Wert der schadenNrMelder-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenNrMelder() {
        return schadenNrMelder;
    }

    /**
     * Legt den Wert der schadenNrMelder-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenNrMelder(String value) {
        this.schadenNrMelder = value;
    }

    /**
     * Ruft den Wert der lkzVerursacherFahrzeug-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzGK.LKZVerursacherFahrzeug }
     *     
     */
    public KfzGK.LKZVerursacherFahrzeug getLKZVerursacherFahrzeug() {
        return lkzVerursacherFahrzeug;
    }

    /**
     * Legt den Wert der lkzVerursacherFahrzeug-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzGK.LKZVerursacherFahrzeug }
     *     
     */
    public void setLKZVerursacherFahrzeug(KfzGK.LKZVerursacherFahrzeug value) {
        this.lkzVerursacherFahrzeug = value;
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
            return "4212";
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
    public static class AnschriftenIDHalters
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
     *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>LaenderkennzeichenTyp">
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
    public static class LKZVerursacherFahrzeug {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "Satzende")
        protected String satzende;

        /**
         * KFZ-Länderkennzeichen, z.B.D = DeutschlandB = Belgien DK = Dänemark F = Frankreich 
         *  
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
