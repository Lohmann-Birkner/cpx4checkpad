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
 *         &lt;element name="lfdNrBeschaedigtenSache" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GefahrLwFrost" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Schadenstelle" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SchadenstelleTyp" minOccurs="0"/>
 *                   &lt;element name="GebaeudeUnbenutzt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="DatumUnbenutztVon" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="8"/>
 *                         &lt;minLength value="8"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DatumUnbenutztBis" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="8"/>
 *                         &lt;minLength value="8"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="GebaeudeBeaufsichtigt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="GebaeudeRaeumeBeheizt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="GefahrSturmElementar" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="FensterTuerenVerschlossen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="RegenSchmutzEingedrungen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="Eindringungsoeffnung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}EindringungsoeffnungTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="GefahrGlas" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="NotverglasungErforderlich" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
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
 *       &lt;attribute name="Satzart" fixed="4283">
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
    "gefahrLwFrost",
    "gefahrSturmElementar",
    "gefahrGlas"
})
@XmlRootElement(name = "SachSchadendatenLwElementar")
public class SachSchadendatenLwElementar {

    protected String lfdNrBeschaedigtenSache;
    @XmlElement(name = "GefahrLwFrost")
    protected SachSchadendatenLwElementar.GefahrLwFrost gefahrLwFrost;
    @XmlElement(name = "GefahrSturmElementar")
    protected SachSchadendatenLwElementar.GefahrSturmElementar gefahrSturmElementar;
    @XmlElement(name = "GefahrGlas")
    protected SachSchadendatenLwElementar.GefahrGlas gefahrGlas;
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
     * Ruft den Wert der gefahrLwFrost-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenLwElementar.GefahrLwFrost }
     *     
     */
    public SachSchadendatenLwElementar.GefahrLwFrost getGefahrLwFrost() {
        return gefahrLwFrost;
    }

    /**
     * Legt den Wert der gefahrLwFrost-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenLwElementar.GefahrLwFrost }
     *     
     */
    public void setGefahrLwFrost(SachSchadendatenLwElementar.GefahrLwFrost value) {
        this.gefahrLwFrost = value;
    }

    /**
     * Ruft den Wert der gefahrSturmElementar-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenLwElementar.GefahrSturmElementar }
     *     
     */
    public SachSchadendatenLwElementar.GefahrSturmElementar getGefahrSturmElementar() {
        return gefahrSturmElementar;
    }

    /**
     * Legt den Wert der gefahrSturmElementar-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenLwElementar.GefahrSturmElementar }
     *     
     */
    public void setGefahrSturmElementar(SachSchadendatenLwElementar.GefahrSturmElementar value) {
        this.gefahrSturmElementar = value;
    }

    /**
     * Ruft den Wert der gefahrGlas-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenLwElementar.GefahrGlas }
     *     
     */
    public SachSchadendatenLwElementar.GefahrGlas getGefahrGlas() {
        return gefahrGlas;
    }

    /**
     * Legt den Wert der gefahrGlas-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenLwElementar.GefahrGlas }
     *     
     */
    public void setGefahrGlas(SachSchadendatenLwElementar.GefahrGlas value) {
        this.gefahrGlas = value;
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
            return "4283";
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
     *         &lt;element name="NotverglasungErforderlich" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
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
        "notverglasungErforderlich"
    })
    public static class GefahrGlas {

        @XmlElement(name = "NotverglasungErforderlich")
        protected SachSchadendatenLwElementar.GefahrGlas.NotverglasungErforderlich notverglasungErforderlich;

        /**
         * Ruft den Wert der notverglasungErforderlich-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SachSchadendatenLwElementar.GefahrGlas.NotverglasungErforderlich }
         *     
         */
        public SachSchadendatenLwElementar.GefahrGlas.NotverglasungErforderlich getNotverglasungErforderlich() {
            return notverglasungErforderlich;
        }

        /**
         * Legt den Wert der notverglasungErforderlich-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SachSchadendatenLwElementar.GefahrGlas.NotverglasungErforderlich }
         *     
         */
        public void setNotverglasungErforderlich(SachSchadendatenLwElementar.GefahrGlas.NotverglasungErforderlich value) {
            this.notverglasungErforderlich = value;
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
        public static class NotverglasungErforderlich {

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
     *         &lt;element name="Schadenstelle" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SchadenstelleTyp" minOccurs="0"/>
     *         &lt;element name="GebaeudeUnbenutzt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="DatumUnbenutztVon" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="8"/>
     *               &lt;minLength value="8"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="DatumUnbenutztBis" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="8"/>
     *               &lt;minLength value="8"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="GebaeudeBeaufsichtigt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="GebaeudeRaeumeBeheizt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
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
        "schadenstelle",
        "gebaeudeUnbenutzt",
        "datumUnbenutztVon",
        "datumUnbenutztBis",
        "gebaeudeBeaufsichtigt",
        "gebaeudeRaeumeBeheizt"
    })
    public static class GefahrLwFrost {

        @XmlElement(name = "Schadenstelle")
        protected String schadenstelle;
        @XmlElement(name = "GebaeudeUnbenutzt")
        protected String gebaeudeUnbenutzt;
        @XmlElement(name = "DatumUnbenutztVon")
        protected String datumUnbenutztVon;
        @XmlElement(name = "DatumUnbenutztBis")
        protected String datumUnbenutztBis;
        @XmlElement(name = "GebaeudeBeaufsichtigt")
        protected String gebaeudeBeaufsichtigt;
        @XmlElement(name = "GebaeudeRaeumeBeheizt")
        protected String gebaeudeRaeumeBeheizt;

        /**
         * Ruft den Wert der schadenstelle-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSchadenstelle() {
            return schadenstelle;
        }

        /**
         * Legt den Wert der schadenstelle-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSchadenstelle(String value) {
            this.schadenstelle = value;
        }

        /**
         * Ruft den Wert der gebaeudeUnbenutzt-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGebaeudeUnbenutzt() {
            return gebaeudeUnbenutzt;
        }

        /**
         * Legt den Wert der gebaeudeUnbenutzt-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGebaeudeUnbenutzt(String value) {
            this.gebaeudeUnbenutzt = value;
        }

        /**
         * Ruft den Wert der datumUnbenutztVon-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDatumUnbenutztVon() {
            return datumUnbenutztVon;
        }

        /**
         * Legt den Wert der datumUnbenutztVon-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDatumUnbenutztVon(String value) {
            this.datumUnbenutztVon = value;
        }

        /**
         * Ruft den Wert der datumUnbenutztBis-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDatumUnbenutztBis() {
            return datumUnbenutztBis;
        }

        /**
         * Legt den Wert der datumUnbenutztBis-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDatumUnbenutztBis(String value) {
            this.datumUnbenutztBis = value;
        }

        /**
         * Ruft den Wert der gebaeudeBeaufsichtigt-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGebaeudeBeaufsichtigt() {
            return gebaeudeBeaufsichtigt;
        }

        /**
         * Legt den Wert der gebaeudeBeaufsichtigt-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGebaeudeBeaufsichtigt(String value) {
            this.gebaeudeBeaufsichtigt = value;
        }

        /**
         * Ruft den Wert der gebaeudeRaeumeBeheizt-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGebaeudeRaeumeBeheizt() {
            return gebaeudeRaeumeBeheizt;
        }

        /**
         * Legt den Wert der gebaeudeRaeumeBeheizt-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGebaeudeRaeumeBeheizt(String value) {
            this.gebaeudeRaeumeBeheizt = value;
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
     *         &lt;element name="FensterTuerenVerschlossen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="RegenSchmutzEingedrungen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="Eindringungsoeffnung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}EindringungsoeffnungTyp" minOccurs="0"/>
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
        "fensterTuerenVerschlossen",
        "regenSchmutzEingedrungen",
        "eindringungsoeffnung"
    })
    public static class GefahrSturmElementar {

        @XmlElement(name = "FensterTuerenVerschlossen")
        protected String fensterTuerenVerschlossen;
        @XmlElement(name = "RegenSchmutzEingedrungen")
        protected String regenSchmutzEingedrungen;
        @XmlElement(name = "Eindringungsoeffnung")
        protected String eindringungsoeffnung;

        /**
         * Ruft den Wert der fensterTuerenVerschlossen-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFensterTuerenVerschlossen() {
            return fensterTuerenVerschlossen;
        }

        /**
         * Legt den Wert der fensterTuerenVerschlossen-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFensterTuerenVerschlossen(String value) {
            this.fensterTuerenVerschlossen = value;
        }

        /**
         * Ruft den Wert der regenSchmutzEingedrungen-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRegenSchmutzEingedrungen() {
            return regenSchmutzEingedrungen;
        }

        /**
         * Legt den Wert der regenSchmutzEingedrungen-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRegenSchmutzEingedrungen(String value) {
            this.regenSchmutzEingedrungen = value;
        }

        /**
         * Ruft den Wert der eindringungsoeffnung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEindringungsoeffnung() {
            return eindringungsoeffnung;
        }

        /**
         * Legt den Wert der eindringungsoeffnung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEindringungsoeffnung(String value) {
            this.eindringungsoeffnung = value;
        }

    }

}
