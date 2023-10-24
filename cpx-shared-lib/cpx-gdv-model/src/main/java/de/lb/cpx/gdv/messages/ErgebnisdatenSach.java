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
 *         &lt;element name="anderesVUBetroffen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="anderesVUVersschutz" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="183"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RechteDritter" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Regressmoeglichkeit" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="SchadenstelleFreigegeben" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="AbtretungVorhanden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="WohnungBewohnbar" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="TrocknungErfolgreichDurchgefuehrt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="ArtBericht" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BerichtsartTyp" minOccurs="0"/>
 *         &lt;element name="Bearbeitungsstatus" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>BearbeitungsstatusTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4501">
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
    "anderesVUBetroffen",
    "anderesVUVersschutz",
    "rechteDritter",
    "regressmoeglichkeit",
    "schadenstelleFreigegeben",
    "abtretungVorhanden",
    "wohnungBewohnbar",
    "trocknungErfolgreichDurchgefuehrt",
    "artBericht",
    "bearbeitungsstatus"
})
@XmlRootElement(name = "ErgebnisdatenSach")
public class ErgebnisdatenSach {

    protected String anderesVUBetroffen;
    protected String anderesVUVersschutz;
    @XmlElement(name = "RechteDritter")
    protected ErgebnisdatenSach.RechteDritter rechteDritter;
    @XmlElement(name = "Regressmoeglichkeit")
    protected String regressmoeglichkeit;
    @XmlElement(name = "SchadenstelleFreigegeben")
    protected String schadenstelleFreigegeben;
    @XmlElement(name = "AbtretungVorhanden")
    protected String abtretungVorhanden;
    @XmlElement(name = "WohnungBewohnbar")
    protected String wohnungBewohnbar;
    @XmlElement(name = "TrocknungErfolgreichDurchgefuehrt")
    protected String trocknungErfolgreichDurchgefuehrt;
    @XmlElement(name = "ArtBericht")
    protected String artBericht;
    @XmlElement(name = "Bearbeitungsstatus")
    protected ErgebnisdatenSach.Bearbeitungsstatus bearbeitungsstatus;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der anderesVUBetroffen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnderesVUBetroffen() {
        return anderesVUBetroffen;
    }

    /**
     * Legt den Wert der anderesVUBetroffen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnderesVUBetroffen(String value) {
        this.anderesVUBetroffen = value;
    }

    /**
     * Ruft den Wert der anderesVUVersschutz-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnderesVUVersschutz() {
        return anderesVUVersschutz;
    }

    /**
     * Legt den Wert der anderesVUVersschutz-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnderesVUVersschutz(String value) {
        this.anderesVUVersschutz = value;
    }

    /**
     * Ruft den Wert der rechteDritter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ErgebnisdatenSach.RechteDritter }
     *     
     */
    public ErgebnisdatenSach.RechteDritter getRechteDritter() {
        return rechteDritter;
    }

    /**
     * Legt den Wert der rechteDritter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ErgebnisdatenSach.RechteDritter }
     *     
     */
    public void setRechteDritter(ErgebnisdatenSach.RechteDritter value) {
        this.rechteDritter = value;
    }

    /**
     * Ruft den Wert der regressmoeglichkeit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegressmoeglichkeit() {
        return regressmoeglichkeit;
    }

    /**
     * Legt den Wert der regressmoeglichkeit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegressmoeglichkeit(String value) {
        this.regressmoeglichkeit = value;
    }

    /**
     * Ruft den Wert der schadenstelleFreigegeben-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenstelleFreigegeben() {
        return schadenstelleFreigegeben;
    }

    /**
     * Legt den Wert der schadenstelleFreigegeben-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenstelleFreigegeben(String value) {
        this.schadenstelleFreigegeben = value;
    }

    /**
     * Ruft den Wert der abtretungVorhanden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbtretungVorhanden() {
        return abtretungVorhanden;
    }

    /**
     * Legt den Wert der abtretungVorhanden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbtretungVorhanden(String value) {
        this.abtretungVorhanden = value;
    }

    /**
     * Ruft den Wert der wohnungBewohnbar-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWohnungBewohnbar() {
        return wohnungBewohnbar;
    }

    /**
     * Legt den Wert der wohnungBewohnbar-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWohnungBewohnbar(String value) {
        this.wohnungBewohnbar = value;
    }

    /**
     * Ruft den Wert der trocknungErfolgreichDurchgefuehrt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrocknungErfolgreichDurchgefuehrt() {
        return trocknungErfolgreichDurchgefuehrt;
    }

    /**
     * Legt den Wert der trocknungErfolgreichDurchgefuehrt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrocknungErfolgreichDurchgefuehrt(String value) {
        this.trocknungErfolgreichDurchgefuehrt = value;
    }

    /**
     * Ruft den Wert der artBericht-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArtBericht() {
        return artBericht;
    }

    /**
     * Legt den Wert der artBericht-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtBericht(String value) {
        this.artBericht = value;
    }

    /**
     * Ruft den Wert der bearbeitungsstatus-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ErgebnisdatenSach.Bearbeitungsstatus }
     *     
     */
    public ErgebnisdatenSach.Bearbeitungsstatus getBearbeitungsstatus() {
        return bearbeitungsstatus;
    }

    /**
     * Legt den Wert der bearbeitungsstatus-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ErgebnisdatenSach.Bearbeitungsstatus }
     *     
     */
    public void setBearbeitungsstatus(ErgebnisdatenSach.Bearbeitungsstatus value) {
        this.bearbeitungsstatus = value;
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
            return "4501";
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
     *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>BearbeitungsstatusTyp">
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
    public static class Bearbeitungsstatus {

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
    public static class RechteDritter {

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
