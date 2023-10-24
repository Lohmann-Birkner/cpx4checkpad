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
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="Auftragsart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragsartTyp"/>
 *         &lt;element name="Gueltigkeit" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Uhrzeitreferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ZeitreferenzTyp" minOccurs="0"/>
 *                   &lt;element name="AbDatum" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="8"/>
 *                         &lt;minLength value="8"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="AbUhrzeit" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="6"/>
 *                         &lt;minLength value="6"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="BisDatum" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="8"/>
 *                         &lt;minLength value="8"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="BisUhrzeit" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="6"/>
 *                         &lt;minLength value="6"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Rhythmus-Uebermittlung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AngebottaktTyp"/>
 *         &lt;element name="Standort">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="LKZ" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LaenderkennzeichenTyp"/>
 *                   &lt;element name="PLZ">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="6"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="MwStAusweisbar" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Reparaturkosten" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
 *                   &lt;element name="KostenNetto" minOccurs="0">
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
 *       &lt;attribute name="Satzart" fixed="4400">
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
    "auftragsart",
    "gueltigkeit",
    "rhythmusUebermittlung",
    "standort",
    "mwStAusweisbar",
    "reparaturkosten"
})
@XmlRootElement(name = "Anfrage-RWB")
public class AnfrageRWB {

    @XmlElement(name = "Auftragsart", required = true)
    protected String auftragsart;
    @XmlElement(name = "Gueltigkeit")
    protected AnfrageRWB.Gueltigkeit gueltigkeit;
    @XmlElement(name = "Rhythmus-Uebermittlung", required = true)
    protected String rhythmusUebermittlung;
    @XmlElement(name = "Standort", required = true)
    protected AnfrageRWB.Standort standort;
    @XmlElement(name = "MwStAusweisbar")
    protected String mwStAusweisbar;
    @XmlElement(name = "Reparaturkosten")
    protected AnfrageRWB.Reparaturkosten reparaturkosten;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der auftragsart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuftragsart() {
        return auftragsart;
    }

    /**
     * Legt den Wert der auftragsart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuftragsart(String value) {
        this.auftragsart = value;
    }

    /**
     * Ruft den Wert der gueltigkeit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AnfrageRWB.Gueltigkeit }
     *     
     */
    public AnfrageRWB.Gueltigkeit getGueltigkeit() {
        return gueltigkeit;
    }

    /**
     * Legt den Wert der gueltigkeit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AnfrageRWB.Gueltigkeit }
     *     
     */
    public void setGueltigkeit(AnfrageRWB.Gueltigkeit value) {
        this.gueltigkeit = value;
    }

    /**
     * Ruft den Wert der rhythmusUebermittlung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRhythmusUebermittlung() {
        return rhythmusUebermittlung;
    }

    /**
     * Legt den Wert der rhythmusUebermittlung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRhythmusUebermittlung(String value) {
        this.rhythmusUebermittlung = value;
    }

    /**
     * Ruft den Wert der standort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AnfrageRWB.Standort }
     *     
     */
    public AnfrageRWB.Standort getStandort() {
        return standort;
    }

    /**
     * Legt den Wert der standort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AnfrageRWB.Standort }
     *     
     */
    public void setStandort(AnfrageRWB.Standort value) {
        this.standort = value;
    }

    /**
     * Ruft den Wert der mwStAusweisbar-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMwStAusweisbar() {
        return mwStAusweisbar;
    }

    /**
     * Legt den Wert der mwStAusweisbar-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMwStAusweisbar(String value) {
        this.mwStAusweisbar = value;
    }

    /**
     * Ruft den Wert der reparaturkosten-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AnfrageRWB.Reparaturkosten }
     *     
     */
    public AnfrageRWB.Reparaturkosten getReparaturkosten() {
        return reparaturkosten;
    }

    /**
     * Legt den Wert der reparaturkosten-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AnfrageRWB.Reparaturkosten }
     *     
     */
    public void setReparaturkosten(AnfrageRWB.Reparaturkosten value) {
        this.reparaturkosten = value;
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
            return "4400";
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
     *         &lt;element name="Uhrzeitreferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ZeitreferenzTyp" minOccurs="0"/>
     *         &lt;element name="AbDatum" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="8"/>
     *               &lt;minLength value="8"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="AbUhrzeit" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="6"/>
     *               &lt;minLength value="6"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="BisDatum" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="8"/>
     *               &lt;minLength value="8"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="BisUhrzeit" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="6"/>
     *               &lt;minLength value="6"/>
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
        "uhrzeitreferenz",
        "abDatum",
        "abUhrzeit",
        "bisDatum",
        "bisUhrzeit"
    })
    public static class Gueltigkeit {

        @XmlElement(name = "Uhrzeitreferenz")
        @XmlSchemaType(name = "string")
        protected ZeitreferenzTyp uhrzeitreferenz;
        @XmlElement(name = "AbDatum")
        protected String abDatum;
        @XmlElement(name = "AbUhrzeit")
        protected String abUhrzeit;
        @XmlElement(name = "BisDatum")
        protected String bisDatum;
        @XmlElement(name = "BisUhrzeit")
        protected String bisUhrzeit;

        /**
         * Ruft den Wert der uhrzeitreferenz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link ZeitreferenzTyp }
         *     
         */
        public ZeitreferenzTyp getUhrzeitreferenz() {
            return uhrzeitreferenz;
        }

        /**
         * Legt den Wert der uhrzeitreferenz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link ZeitreferenzTyp }
         *     
         */
        public void setUhrzeitreferenz(ZeitreferenzTyp value) {
            this.uhrzeitreferenz = value;
        }

        /**
         * Ruft den Wert der abDatum-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAbDatum() {
            return abDatum;
        }

        /**
         * Legt den Wert der abDatum-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAbDatum(String value) {
            this.abDatum = value;
        }

        /**
         * Ruft den Wert der abUhrzeit-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAbUhrzeit() {
            return abUhrzeit;
        }

        /**
         * Legt den Wert der abUhrzeit-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAbUhrzeit(String value) {
            this.abUhrzeit = value;
        }

        /**
         * Ruft den Wert der bisDatum-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBisDatum() {
            return bisDatum;
        }

        /**
         * Legt den Wert der bisDatum-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBisDatum(String value) {
            this.bisDatum = value;
        }

        /**
         * Ruft den Wert der bisUhrzeit-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBisUhrzeit() {
            return bisUhrzeit;
        }

        /**
         * Legt den Wert der bisUhrzeit-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBisUhrzeit(String value) {
            this.bisUhrzeit = value;
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
     *         &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
     *         &lt;element name="KostenNetto" minOccurs="0">
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
        "waehrungsschluessel",
        "kostenNetto",
        "indikator"
    })
    public static class Reparaturkosten {

        @XmlElement(name = "Waehrungsschluessel")
        protected String waehrungsschluessel;
        @XmlElement(name = "KostenNetto")
        protected BigDecimal kostenNetto;
        @XmlElement(name = "Indikator", required = true)
        protected AnfrageRWB.Reparaturkosten.Indikator indikator;

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
         * Ruft den Wert der kostenNetto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getKostenNetto() {
            return kostenNetto;
        }

        /**
         * Legt den Wert der kostenNetto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setKostenNetto(BigDecimal value) {
            this.kostenNetto = value;
        }

        /**
         * Ruft den Wert der indikator-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link AnfrageRWB.Reparaturkosten.Indikator }
         *     
         */
        public AnfrageRWB.Reparaturkosten.Indikator getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link AnfrageRWB.Reparaturkosten.Indikator }
         *     
         */
        public void setIndikator(AnfrageRWB.Reparaturkosten.Indikator value) {
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
     *         &lt;element name="LKZ" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LaenderkennzeichenTyp"/>
     *         &lt;element name="PLZ">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="6"/>
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
        "lkz",
        "plz"
    })
    public static class Standort {

        @XmlElement(name = "LKZ", required = true)
        protected String lkz;
        @XmlElement(name = "PLZ", required = true)
        protected String plz;

        /**
         * Ruft den Wert der lkz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLKZ() {
            return lkz;
        }

        /**
         * Legt den Wert der lkz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLKZ(String value) {
            this.lkz = value;
        }

        /**
         * Ruft den Wert der plz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPLZ() {
            return plz;
        }

        /**
         * Legt den Wert der plz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPLZ(String value) {
            this.plz = value;
        }

    }

}
