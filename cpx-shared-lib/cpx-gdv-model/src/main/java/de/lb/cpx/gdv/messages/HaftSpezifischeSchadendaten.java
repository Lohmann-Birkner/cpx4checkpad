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
 *         &lt;element name="VNVPUndASTVerwandt" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="ArtFamilienangehoerigerVerwandter" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ArtFamilienangehoerigerVerwandterTyp" minOccurs="0"/>
 *         &lt;element name="BeschreibungSonstigesFamilienverhaeltnis" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="VNMitASTInHaeuslicherGemeinschaft" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="InErstausbildung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="VNUndASTArbeitsverhaeltnis" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="ArtArbeitsverhaeltnis" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ArtDesArbeitsverhaeltnisTyp" minOccurs="0"/>
 *         &lt;element name="BeschreibungSonstigesVertragsverhaeltnis" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AngabenZuEigentumsverhaeltnissen" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="BeschaedigteSacheVonVNVPGeliehen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="ErlaeuterungBesitzverhaeltnisse" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>ErlaeuterungDerBesitzverhaeltnisseZurBeschaedigtenSacheTyp">
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
 *       &lt;attribute name="Satzart" fixed="4271">
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
    "vnvpUndASTVerwandt",
    "artFamilienangehoerigerVerwandter",
    "beschreibungSonstigesFamilienverhaeltnis",
    "vnMitASTInHaeuslicherGemeinschaft",
    "inErstausbildung",
    "vnUndASTArbeitsverhaeltnis",
    "artArbeitsverhaeltnis",
    "beschreibungSonstigesVertragsverhaeltnis",
    "angabenZuEigentumsverhaeltnissen"
})
@XmlRootElement(name = "HaftSpezifischeSchadendaten")
public class HaftSpezifischeSchadendaten {

    @XmlElement(name = "VNVPUndASTVerwandt")
    protected String vnvpUndASTVerwandt;
    @XmlElement(name = "ArtFamilienangehoerigerVerwandter")
    protected String artFamilienangehoerigerVerwandter;
    @XmlElement(name = "BeschreibungSonstigesFamilienverhaeltnis")
    protected String beschreibungSonstigesFamilienverhaeltnis;
    @XmlElement(name = "VNMitASTInHaeuslicherGemeinschaft")
    protected String vnMitASTInHaeuslicherGemeinschaft;
    @XmlElement(name = "InErstausbildung")
    protected String inErstausbildung;
    @XmlElement(name = "VNUndASTArbeitsverhaeltnis")
    protected String vnUndASTArbeitsverhaeltnis;
    @XmlElement(name = "ArtArbeitsverhaeltnis")
    protected String artArbeitsverhaeltnis;
    @XmlElement(name = "BeschreibungSonstigesVertragsverhaeltnis")
    protected String beschreibungSonstigesVertragsverhaeltnis;
    @XmlElement(name = "AngabenZuEigentumsverhaeltnissen")
    protected HaftSpezifischeSchadendaten.AngabenZuEigentumsverhaeltnissen angabenZuEigentumsverhaeltnissen;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der vnvpUndASTVerwandt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNVPUndASTVerwandt() {
        return vnvpUndASTVerwandt;
    }

    /**
     * Legt den Wert der vnvpUndASTVerwandt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNVPUndASTVerwandt(String value) {
        this.vnvpUndASTVerwandt = value;
    }

    /**
     * Ruft den Wert der artFamilienangehoerigerVerwandter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArtFamilienangehoerigerVerwandter() {
        return artFamilienangehoerigerVerwandter;
    }

    /**
     * Legt den Wert der artFamilienangehoerigerVerwandter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtFamilienangehoerigerVerwandter(String value) {
        this.artFamilienangehoerigerVerwandter = value;
    }

    /**
     * Ruft den Wert der beschreibungSonstigesFamilienverhaeltnis-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeschreibungSonstigesFamilienverhaeltnis() {
        return beschreibungSonstigesFamilienverhaeltnis;
    }

    /**
     * Legt den Wert der beschreibungSonstigesFamilienverhaeltnis-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeschreibungSonstigesFamilienverhaeltnis(String value) {
        this.beschreibungSonstigesFamilienverhaeltnis = value;
    }

    /**
     * Ruft den Wert der vnMitASTInHaeuslicherGemeinschaft-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNMitASTInHaeuslicherGemeinschaft() {
        return vnMitASTInHaeuslicherGemeinschaft;
    }

    /**
     * Legt den Wert der vnMitASTInHaeuslicherGemeinschaft-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNMitASTInHaeuslicherGemeinschaft(String value) {
        this.vnMitASTInHaeuslicherGemeinschaft = value;
    }

    /**
     * Ruft den Wert der inErstausbildung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInErstausbildung() {
        return inErstausbildung;
    }

    /**
     * Legt den Wert der inErstausbildung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInErstausbildung(String value) {
        this.inErstausbildung = value;
    }

    /**
     * Ruft den Wert der vnUndASTArbeitsverhaeltnis-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNUndASTArbeitsverhaeltnis() {
        return vnUndASTArbeitsverhaeltnis;
    }

    /**
     * Legt den Wert der vnUndASTArbeitsverhaeltnis-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNUndASTArbeitsverhaeltnis(String value) {
        this.vnUndASTArbeitsverhaeltnis = value;
    }

    /**
     * Ruft den Wert der artArbeitsverhaeltnis-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArtArbeitsverhaeltnis() {
        return artArbeitsverhaeltnis;
    }

    /**
     * Legt den Wert der artArbeitsverhaeltnis-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtArbeitsverhaeltnis(String value) {
        this.artArbeitsverhaeltnis = value;
    }

    /**
     * Ruft den Wert der beschreibungSonstigesVertragsverhaeltnis-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeschreibungSonstigesVertragsverhaeltnis() {
        return beschreibungSonstigesVertragsverhaeltnis;
    }

    /**
     * Legt den Wert der beschreibungSonstigesVertragsverhaeltnis-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeschreibungSonstigesVertragsverhaeltnis(String value) {
        this.beschreibungSonstigesVertragsverhaeltnis = value;
    }

    /**
     * Ruft den Wert der angabenZuEigentumsverhaeltnissen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link HaftSpezifischeSchadendaten.AngabenZuEigentumsverhaeltnissen }
     *     
     */
    public HaftSpezifischeSchadendaten.AngabenZuEigentumsverhaeltnissen getAngabenZuEigentumsverhaeltnissen() {
        return angabenZuEigentumsverhaeltnissen;
    }

    /**
     * Legt den Wert der angabenZuEigentumsverhaeltnissen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link HaftSpezifischeSchadendaten.AngabenZuEigentumsverhaeltnissen }
     *     
     */
    public void setAngabenZuEigentumsverhaeltnissen(HaftSpezifischeSchadendaten.AngabenZuEigentumsverhaeltnissen value) {
        this.angabenZuEigentumsverhaeltnissen = value;
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
            return "4271";
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
     *         &lt;element name="BeschaedigteSacheVonVNVPGeliehen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="ErlaeuterungBesitzverhaeltnisse" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>ErlaeuterungDerBesitzverhaeltnisseZurBeschaedigtenSacheTyp">
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
        "beschaedigteSacheVonVNVPGeliehen",
        "erlaeuterungBesitzverhaeltnisse"
    })
    public static class AngabenZuEigentumsverhaeltnissen {

        @XmlElement(name = "BeschaedigteSacheVonVNVPGeliehen")
        protected String beschaedigteSacheVonVNVPGeliehen;
        @XmlElement(name = "ErlaeuterungBesitzverhaeltnisse")
        protected HaftSpezifischeSchadendaten.AngabenZuEigentumsverhaeltnissen.ErlaeuterungBesitzverhaeltnisse erlaeuterungBesitzverhaeltnisse;

        /**
         * Ruft den Wert der beschaedigteSacheVonVNVPGeliehen-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBeschaedigteSacheVonVNVPGeliehen() {
            return beschaedigteSacheVonVNVPGeliehen;
        }

        /**
         * Legt den Wert der beschaedigteSacheVonVNVPGeliehen-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBeschaedigteSacheVonVNVPGeliehen(String value) {
            this.beschaedigteSacheVonVNVPGeliehen = value;
        }

        /**
         * Ruft den Wert der erlaeuterungBesitzverhaeltnisse-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link HaftSpezifischeSchadendaten.AngabenZuEigentumsverhaeltnissen.ErlaeuterungBesitzverhaeltnisse }
         *     
         */
        public HaftSpezifischeSchadendaten.AngabenZuEigentumsverhaeltnissen.ErlaeuterungBesitzverhaeltnisse getErlaeuterungBesitzverhaeltnisse() {
            return erlaeuterungBesitzverhaeltnisse;
        }

        /**
         * Legt den Wert der erlaeuterungBesitzverhaeltnisse-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link HaftSpezifischeSchadendaten.AngabenZuEigentumsverhaeltnissen.ErlaeuterungBesitzverhaeltnisse }
         *     
         */
        public void setErlaeuterungBesitzverhaeltnisse(HaftSpezifischeSchadendaten.AngabenZuEigentumsverhaeltnissen.ErlaeuterungBesitzverhaeltnisse value) {
            this.erlaeuterungBesitzverhaeltnisse = value;
        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>ErlaeuterungDerBesitzverhaeltnisseZurBeschaedigtenSacheTyp">
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
        public static class ErlaeuterungBesitzverhaeltnisse {

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

}
