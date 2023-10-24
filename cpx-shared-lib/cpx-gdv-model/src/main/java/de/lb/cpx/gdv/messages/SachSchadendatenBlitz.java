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
 *         &lt;element name="BeschreibungBlitzeinschlagstelle" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="BeschreibungBlitzeinschlagspuren" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AnschlussleitungenBeschaedigterSachen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AnschlussleitungenTyp" minOccurs="0"/>
 *         &lt;element name="ZugehoerigkeitZaehlerEtc" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ZugehoerigkeitZaehlerTyp" minOccurs="0"/>
 *         &lt;element name="DatumLetzterBetrieb" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="UhrzeitLetzterBetrieb" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *               &lt;minLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="EntfernungBlitzeinschlagstelleInM" minOccurs="0">
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
 *       &lt;attribute name="Satzart" fixed="4281">
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
    "beschreibungBlitzeinschlagstelle",
    "beschreibungBlitzeinschlagspuren",
    "anschlussleitungenBeschaedigterSachen",
    "zugehoerigkeitZaehlerEtc",
    "datumLetzterBetrieb",
    "uhrzeitLetzterBetrieb",
    "entfernungBlitzeinschlagstelleInM"
})
@XmlRootElement(name = "SachSchadendatenBlitz")
public class SachSchadendatenBlitz {

    protected String lfdNrBeschaedigtenSache;
    @XmlElement(name = "BeschreibungBlitzeinschlagstelle")
    protected SachSchadendatenBlitz.BeschreibungBlitzeinschlagstelle beschreibungBlitzeinschlagstelle;
    @XmlElement(name = "BeschreibungBlitzeinschlagspuren")
    protected String beschreibungBlitzeinschlagspuren;
    @XmlElement(name = "AnschlussleitungenBeschaedigterSachen")
    protected String anschlussleitungenBeschaedigterSachen;
    @XmlElement(name = "ZugehoerigkeitZaehlerEtc")
    protected String zugehoerigkeitZaehlerEtc;
    @XmlElement(name = "DatumLetzterBetrieb")
    protected String datumLetzterBetrieb;
    @XmlElement(name = "UhrzeitLetzterBetrieb")
    protected String uhrzeitLetzterBetrieb;
    @XmlElement(name = "EntfernungBlitzeinschlagstelleInM")
    protected SachSchadendatenBlitz.EntfernungBlitzeinschlagstelleInM entfernungBlitzeinschlagstelleInM;
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
     * Ruft den Wert der beschreibungBlitzeinschlagstelle-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenBlitz.BeschreibungBlitzeinschlagstelle }
     *     
     */
    public SachSchadendatenBlitz.BeschreibungBlitzeinschlagstelle getBeschreibungBlitzeinschlagstelle() {
        return beschreibungBlitzeinschlagstelle;
    }

    /**
     * Legt den Wert der beschreibungBlitzeinschlagstelle-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenBlitz.BeschreibungBlitzeinschlagstelle }
     *     
     */
    public void setBeschreibungBlitzeinschlagstelle(SachSchadendatenBlitz.BeschreibungBlitzeinschlagstelle value) {
        this.beschreibungBlitzeinschlagstelle = value;
    }

    /**
     * Ruft den Wert der beschreibungBlitzeinschlagspuren-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeschreibungBlitzeinschlagspuren() {
        return beschreibungBlitzeinschlagspuren;
    }

    /**
     * Legt den Wert der beschreibungBlitzeinschlagspuren-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeschreibungBlitzeinschlagspuren(String value) {
        this.beschreibungBlitzeinschlagspuren = value;
    }

    /**
     * Ruft den Wert der anschlussleitungenBeschaedigterSachen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnschlussleitungenBeschaedigterSachen() {
        return anschlussleitungenBeschaedigterSachen;
    }

    /**
     * Legt den Wert der anschlussleitungenBeschaedigterSachen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnschlussleitungenBeschaedigterSachen(String value) {
        this.anschlussleitungenBeschaedigterSachen = value;
    }

    /**
     * Ruft den Wert der zugehoerigkeitZaehlerEtc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZugehoerigkeitZaehlerEtc() {
        return zugehoerigkeitZaehlerEtc;
    }

    /**
     * Legt den Wert der zugehoerigkeitZaehlerEtc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZugehoerigkeitZaehlerEtc(String value) {
        this.zugehoerigkeitZaehlerEtc = value;
    }

    /**
     * Ruft den Wert der datumLetzterBetrieb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatumLetzterBetrieb() {
        return datumLetzterBetrieb;
    }

    /**
     * Legt den Wert der datumLetzterBetrieb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatumLetzterBetrieb(String value) {
        this.datumLetzterBetrieb = value;
    }

    /**
     * Ruft den Wert der uhrzeitLetzterBetrieb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUhrzeitLetzterBetrieb() {
        return uhrzeitLetzterBetrieb;
    }

    /**
     * Legt den Wert der uhrzeitLetzterBetrieb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUhrzeitLetzterBetrieb(String value) {
        this.uhrzeitLetzterBetrieb = value;
    }

    /**
     * Ruft den Wert der entfernungBlitzeinschlagstelleInM-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenBlitz.EntfernungBlitzeinschlagstelleInM }
     *     
     */
    public SachSchadendatenBlitz.EntfernungBlitzeinschlagstelleInM getEntfernungBlitzeinschlagstelleInM() {
        return entfernungBlitzeinschlagstelleInM;
    }

    /**
     * Legt den Wert der entfernungBlitzeinschlagstelleInM-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenBlitz.EntfernungBlitzeinschlagstelleInM }
     *     
     */
    public void setEntfernungBlitzeinschlagstelleInM(SachSchadendatenBlitz.EntfernungBlitzeinschlagstelleInM value) {
        this.entfernungBlitzeinschlagstelleInM = value;
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
            return "4281";
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
    public static class BeschreibungBlitzeinschlagstelle
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
    public static class EntfernungBlitzeinschlagstelleInM {

        @XmlElement(name = "Wert")
        protected BigDecimal wert;
        @XmlElement(name = "Indikator", required = true)
        protected SachSchadendatenBlitz.EntfernungBlitzeinschlagstelleInM.Indikator indikator;

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
         *     {@link SachSchadendatenBlitz.EntfernungBlitzeinschlagstelleInM.Indikator }
         *     
         */
        public SachSchadendatenBlitz.EntfernungBlitzeinschlagstelleInM.Indikator getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SachSchadendatenBlitz.EntfernungBlitzeinschlagstelleInM.Indikator }
         *     
         */
        public void setIndikator(SachSchadendatenBlitz.EntfernungBlitzeinschlagstelleInM.Indikator value) {
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

}
