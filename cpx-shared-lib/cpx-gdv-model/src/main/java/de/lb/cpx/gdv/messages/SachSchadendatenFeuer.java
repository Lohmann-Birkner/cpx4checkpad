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
 *         &lt;element name="FeuererscheinungVorhanden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="SchadenDurchWaermeGlut" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Loeschart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LoeschartTyp" minOccurs="0"/>
 *         &lt;element name="SchilderungLoeschvorgang" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LaengeDerSchadenstelleInCm" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="BreiteDerSchadenstelleInCm" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
 *       &lt;attribute name="Satzart" fixed="4280">
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
    "feuererscheinungVorhanden",
    "schadenDurchWaermeGlut",
    "loeschart",
    "schilderungLoeschvorgang",
    "laengeDerSchadenstelleInCm",
    "breiteDerSchadenstelleInCm"
})
@XmlRootElement(name = "SachSchadendatenFeuer")
public class SachSchadendatenFeuer {

    protected String lfdNrBeschaedigtenSache;
    @XmlElement(name = "FeuererscheinungVorhanden")
    protected String feuererscheinungVorhanden;
    @XmlElement(name = "SchadenDurchWaermeGlut")
    protected String schadenDurchWaermeGlut;
    @XmlElement(name = "Loeschart")
    protected String loeschart;
    @XmlElement(name = "SchilderungLoeschvorgang")
    protected String schilderungLoeschvorgang;
    @XmlElement(name = "LaengeDerSchadenstelleInCm")
    protected SachSchadendatenFeuer.LaengeDerSchadenstelleInCm laengeDerSchadenstelleInCm;
    @XmlElement(name = "BreiteDerSchadenstelleInCm")
    protected SachSchadendatenFeuer.BreiteDerSchadenstelleInCm breiteDerSchadenstelleInCm;
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
     * Ruft den Wert der feuererscheinungVorhanden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFeuererscheinungVorhanden() {
        return feuererscheinungVorhanden;
    }

    /**
     * Legt den Wert der feuererscheinungVorhanden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFeuererscheinungVorhanden(String value) {
        this.feuererscheinungVorhanden = value;
    }

    /**
     * Ruft den Wert der schadenDurchWaermeGlut-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenDurchWaermeGlut() {
        return schadenDurchWaermeGlut;
    }

    /**
     * Legt den Wert der schadenDurchWaermeGlut-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenDurchWaermeGlut(String value) {
        this.schadenDurchWaermeGlut = value;
    }

    /**
     * Ruft den Wert der loeschart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoeschart() {
        return loeschart;
    }

    /**
     * Legt den Wert der loeschart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoeschart(String value) {
        this.loeschart = value;
    }

    /**
     * Ruft den Wert der schilderungLoeschvorgang-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchilderungLoeschvorgang() {
        return schilderungLoeschvorgang;
    }

    /**
     * Legt den Wert der schilderungLoeschvorgang-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchilderungLoeschvorgang(String value) {
        this.schilderungLoeschvorgang = value;
    }

    /**
     * Ruft den Wert der laengeDerSchadenstelleInCm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenFeuer.LaengeDerSchadenstelleInCm }
     *     
     */
    public SachSchadendatenFeuer.LaengeDerSchadenstelleInCm getLaengeDerSchadenstelleInCm() {
        return laengeDerSchadenstelleInCm;
    }

    /**
     * Legt den Wert der laengeDerSchadenstelleInCm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenFeuer.LaengeDerSchadenstelleInCm }
     *     
     */
    public void setLaengeDerSchadenstelleInCm(SachSchadendatenFeuer.LaengeDerSchadenstelleInCm value) {
        this.laengeDerSchadenstelleInCm = value;
    }

    /**
     * Ruft den Wert der breiteDerSchadenstelleInCm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenFeuer.BreiteDerSchadenstelleInCm }
     *     
     */
    public SachSchadendatenFeuer.BreiteDerSchadenstelleInCm getBreiteDerSchadenstelleInCm() {
        return breiteDerSchadenstelleInCm;
    }

    /**
     * Legt den Wert der breiteDerSchadenstelleInCm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenFeuer.BreiteDerSchadenstelleInCm }
     *     
     */
    public void setBreiteDerSchadenstelleInCm(SachSchadendatenFeuer.BreiteDerSchadenstelleInCm value) {
        this.breiteDerSchadenstelleInCm = value;
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
            return "4280";
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
     *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
    public static class BreiteDerSchadenstelleInCm {

        @XmlElement(name = "Wert")
        protected BigDecimal wert;
        @XmlElement(name = "Indikator", required = true)
        protected SachSchadendatenFeuer.BreiteDerSchadenstelleInCm.Indikator indikator;

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
         *     {@link SachSchadendatenFeuer.BreiteDerSchadenstelleInCm.Indikator }
         *     
         */
        public SachSchadendatenFeuer.BreiteDerSchadenstelleInCm.Indikator getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SachSchadendatenFeuer.BreiteDerSchadenstelleInCm.Indikator }
         *     
         */
        public void setIndikator(SachSchadendatenFeuer.BreiteDerSchadenstelleInCm.Indikator value) {
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
     *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
    public static class LaengeDerSchadenstelleInCm {

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
