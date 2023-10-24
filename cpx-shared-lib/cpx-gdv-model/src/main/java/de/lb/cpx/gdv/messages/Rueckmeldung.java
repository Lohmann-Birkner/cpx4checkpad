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
 *         &lt;element name="RSKASV" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="DeckungszusageEintrittspflicht" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="Selbstbehalt" minOccurs="0">
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
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="RS" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Deckungssumme" minOccurs="0">
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
 *                   &lt;element name="Ausschluesse" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AusschluesseTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="KHAH" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="HaftungSachschaden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}HaftungSachschadenTyp" minOccurs="0"/>
 *                   &lt;element name="QuoteSachschaden" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="9999.99"/>
 *                         &lt;totalDigits value="6"/>
 *                         &lt;fractionDigits value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="HaftungPersonenschaden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}HaftungSachschadenTyp" minOccurs="0"/>
 *                   &lt;element name="QuotePersonenschaden" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeDecTyp">
 *                           &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4860">
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
    "waehrungsschluessel",
    "rskasv",
    "rs",
    "khah"
})
@XmlRootElement(name = "Rueckmeldung")
public class Rueckmeldung {

    @XmlElement(name = "Waehrungsschluessel")
    protected String waehrungsschluessel;
    @XmlElement(name = "RSKASV")
    protected Rueckmeldung.RSKASV rskasv;
    @XmlElement(name = "RS")
    protected Rueckmeldung.RS rs;
    @XmlElement(name = "KHAH")
    protected Rueckmeldung.KHAH khah;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

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
     * Ruft den Wert der rskasv-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Rueckmeldung.RSKASV }
     *     
     */
    public Rueckmeldung.RSKASV getRSKASV() {
        return rskasv;
    }

    /**
     * Legt den Wert der rskasv-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Rueckmeldung.RSKASV }
     *     
     */
    public void setRSKASV(Rueckmeldung.RSKASV value) {
        this.rskasv = value;
    }

    /**
     * Ruft den Wert der rs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Rueckmeldung.RS }
     *     
     */
    public Rueckmeldung.RS getRS() {
        return rs;
    }

    /**
     * Legt den Wert der rs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Rueckmeldung.RS }
     *     
     */
    public void setRS(Rueckmeldung.RS value) {
        this.rs = value;
    }

    /**
     * Ruft den Wert der khah-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Rueckmeldung.KHAH }
     *     
     */
    public Rueckmeldung.KHAH getKHAH() {
        return khah;
    }

    /**
     * Legt den Wert der khah-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Rueckmeldung.KHAH }
     *     
     */
    public void setKHAH(Rueckmeldung.KHAH value) {
        this.khah = value;
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
            return "4860";
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
     *         &lt;element name="HaftungSachschaden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}HaftungSachschadenTyp" minOccurs="0"/>
     *         &lt;element name="QuoteSachschaden" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="9999.99"/>
     *               &lt;totalDigits value="6"/>
     *               &lt;fractionDigits value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="HaftungPersonenschaden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}HaftungSachschadenTyp" minOccurs="0"/>
     *         &lt;element name="QuotePersonenschaden" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeDecTyp">
     *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
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
        "haftungSachschaden",
        "quoteSachschaden",
        "haftungPersonenschaden",
        "quotePersonenschaden"
    })
    public static class KHAH {

        @XmlElement(name = "HaftungSachschaden")
        protected String haftungSachschaden;
        @XmlElement(name = "QuoteSachschaden")
        protected BigDecimal quoteSachschaden;
        @XmlElement(name = "HaftungPersonenschaden")
        protected String haftungPersonenschaden;
        @XmlElement(name = "QuotePersonenschaden")
        protected Rueckmeldung.KHAH.QuotePersonenschaden quotePersonenschaden;

        /**
         * Ruft den Wert der haftungSachschaden-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHaftungSachschaden() {
            return haftungSachschaden;
        }

        /**
         * Legt den Wert der haftungSachschaden-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHaftungSachschaden(String value) {
            this.haftungSachschaden = value;
        }

        /**
         * Ruft den Wert der quoteSachschaden-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getQuoteSachschaden() {
            return quoteSachschaden;
        }

        /**
         * Legt den Wert der quoteSachschaden-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setQuoteSachschaden(BigDecimal value) {
            this.quoteSachschaden = value;
        }

        /**
         * Ruft den Wert der haftungPersonenschaden-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHaftungPersonenschaden() {
            return haftungPersonenschaden;
        }

        /**
         * Legt den Wert der haftungPersonenschaden-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHaftungPersonenschaden(String value) {
            this.haftungPersonenschaden = value;
        }

        /**
         * Ruft den Wert der quotePersonenschaden-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Rueckmeldung.KHAH.QuotePersonenschaden }
         *     
         */
        public Rueckmeldung.KHAH.QuotePersonenschaden getQuotePersonenschaden() {
            return quotePersonenschaden;
        }

        /**
         * Legt den Wert der quotePersonenschaden-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Rueckmeldung.KHAH.QuotePersonenschaden }
         *     
         */
        public void setQuotePersonenschaden(Rueckmeldung.KHAH.QuotePersonenschaden value) {
            this.quotePersonenschaden = value;
        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeDecTyp">
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
        public static class QuotePersonenschaden
            extends SatzendeDecTyp
        {


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
     *         &lt;element name="Deckungssumme" minOccurs="0">
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
     *         &lt;element name="Ausschluesse" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AusschluesseTyp" minOccurs="0"/>
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
        "deckungssumme",
        "ausschluesse"
    })
    public static class RS {

        @XmlElement(name = "Deckungssumme")
        protected Rueckmeldung.RS.Deckungssumme deckungssumme;
        @XmlElement(name = "Ausschluesse")
        protected String ausschluesse;

        /**
         * Ruft den Wert der deckungssumme-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Rueckmeldung.RS.Deckungssumme }
         *     
         */
        public Rueckmeldung.RS.Deckungssumme getDeckungssumme() {
            return deckungssumme;
        }

        /**
         * Legt den Wert der deckungssumme-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Rueckmeldung.RS.Deckungssumme }
         *     
         */
        public void setDeckungssumme(Rueckmeldung.RS.Deckungssumme value) {
            this.deckungssumme = value;
        }

        /**
         * Ruft den Wert der ausschluesse-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAusschluesse() {
            return ausschluesse;
        }

        /**
         * Legt den Wert der ausschluesse-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAusschluesse(String value) {
            this.ausschluesse = value;
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
        public static class Deckungssumme {

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
     *         &lt;element name="DeckungszusageEintrittspflicht" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="Selbstbehalt" minOccurs="0">
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
        "deckungszusageEintrittspflicht",
        "selbstbehalt"
    })
    public static class RSKASV {

        @XmlElement(name = "DeckungszusageEintrittspflicht")
        protected String deckungszusageEintrittspflicht;
        @XmlElement(name = "Selbstbehalt")
        protected Rueckmeldung.RSKASV.Selbstbehalt selbstbehalt;

        /**
         * Ruft den Wert der deckungszusageEintrittspflicht-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDeckungszusageEintrittspflicht() {
            return deckungszusageEintrittspflicht;
        }

        /**
         * Legt den Wert der deckungszusageEintrittspflicht-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDeckungszusageEintrittspflicht(String value) {
            this.deckungszusageEintrittspflicht = value;
        }

        /**
         * Ruft den Wert der selbstbehalt-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Rueckmeldung.RSKASV.Selbstbehalt }
         *     
         */
        public Rueckmeldung.RSKASV.Selbstbehalt getSelbstbehalt() {
            return selbstbehalt;
        }

        /**
         * Legt den Wert der selbstbehalt-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Rueckmeldung.RSKASV.Selbstbehalt }
         *     
         */
        public void setSelbstbehalt(Rueckmeldung.RSKASV.Selbstbehalt value) {
            this.selbstbehalt = value;
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
        public static class Selbstbehalt {

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

}
