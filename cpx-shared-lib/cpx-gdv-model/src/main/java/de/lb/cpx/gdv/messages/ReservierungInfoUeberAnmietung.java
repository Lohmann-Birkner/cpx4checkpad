//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.19 um 11:55:14 AM CEST 
//


package de.lb.cpx.gdv.messages;

import java.math.BigInteger;
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
 *         &lt;element name="Reservierungsart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ReservierungsartTyp"/>
 *         &lt;element name="GruppeMietwagen" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GruppeBeschaedigtesFahrzeug" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Uebernahmetag" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Uebernahmeuhrzeit" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *               &lt;minLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ausgebendeStation" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
 *                   &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="OrtFahrzeuguebernahme" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
 *                   &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Rueckgabetag" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Rueckgabeuhrzeit" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *               &lt;minLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ruecknehmendeStation" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
 *                   &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="OrtFahrzeuguebergabe" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
 *                   &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Anmietintervall" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;totalDigits value="2"/>
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
 *       &lt;attribute name="Satzart" fixed="4350">
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
    "reservierungsart",
    "gruppeMietwagen",
    "gruppeBeschaedigtesFahrzeug",
    "uebernahmetag",
    "uebernahmeuhrzeit",
    "ausgebendeStation",
    "ortFahrzeuguebernahme",
    "rueckgabetag",
    "rueckgabeuhrzeit",
    "ruecknehmendeStation",
    "ortFahrzeuguebergabe",
    "anmietintervall"
})
@XmlRootElement(name = "ReservierungInfoUeberAnmietung")
public class ReservierungInfoUeberAnmietung {

    @XmlElement(name = "Reservierungsart", required = true)
    protected String reservierungsart;
    @XmlElement(name = "GruppeMietwagen")
    protected String gruppeMietwagen;
    @XmlElement(name = "GruppeBeschaedigtesFahrzeug")
    protected String gruppeBeschaedigtesFahrzeug;
    @XmlElement(name = "Uebernahmetag")
    protected String uebernahmetag;
    @XmlElement(name = "Uebernahmeuhrzeit")
    protected String uebernahmeuhrzeit;
    protected ReservierungInfoUeberAnmietung.AusgebendeStation ausgebendeStation;
    @XmlElement(name = "OrtFahrzeuguebernahme")
    protected ReservierungInfoUeberAnmietung.OrtFahrzeuguebernahme ortFahrzeuguebernahme;
    @XmlElement(name = "Rueckgabetag")
    protected String rueckgabetag;
    @XmlElement(name = "Rueckgabeuhrzeit")
    protected String rueckgabeuhrzeit;
    protected ReservierungInfoUeberAnmietung.RuecknehmendeStation ruecknehmendeStation;
    @XmlElement(name = "OrtFahrzeuguebergabe")
    protected ReservierungInfoUeberAnmietung.OrtFahrzeuguebergabe ortFahrzeuguebergabe;
    @XmlElement(name = "Anmietintervall")
    protected ReservierungInfoUeberAnmietung.Anmietintervall anmietintervall;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der reservierungsart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReservierungsart() {
        return reservierungsart;
    }

    /**
     * Legt den Wert der reservierungsart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReservierungsart(String value) {
        this.reservierungsart = value;
    }

    /**
     * Ruft den Wert der gruppeMietwagen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGruppeMietwagen() {
        return gruppeMietwagen;
    }

    /**
     * Legt den Wert der gruppeMietwagen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGruppeMietwagen(String value) {
        this.gruppeMietwagen = value;
    }

    /**
     * Ruft den Wert der gruppeBeschaedigtesFahrzeug-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGruppeBeschaedigtesFahrzeug() {
        return gruppeBeschaedigtesFahrzeug;
    }

    /**
     * Legt den Wert der gruppeBeschaedigtesFahrzeug-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGruppeBeschaedigtesFahrzeug(String value) {
        this.gruppeBeschaedigtesFahrzeug = value;
    }

    /**
     * Ruft den Wert der uebernahmetag-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUebernahmetag() {
        return uebernahmetag;
    }

    /**
     * Legt den Wert der uebernahmetag-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUebernahmetag(String value) {
        this.uebernahmetag = value;
    }

    /**
     * Ruft den Wert der uebernahmeuhrzeit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUebernahmeuhrzeit() {
        return uebernahmeuhrzeit;
    }

    /**
     * Legt den Wert der uebernahmeuhrzeit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUebernahmeuhrzeit(String value) {
        this.uebernahmeuhrzeit = value;
    }

    /**
     * Ruft den Wert der ausgebendeStation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ReservierungInfoUeberAnmietung.AusgebendeStation }
     *     
     */
    public ReservierungInfoUeberAnmietung.AusgebendeStation getAusgebendeStation() {
        return ausgebendeStation;
    }

    /**
     * Legt den Wert der ausgebendeStation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ReservierungInfoUeberAnmietung.AusgebendeStation }
     *     
     */
    public void setAusgebendeStation(ReservierungInfoUeberAnmietung.AusgebendeStation value) {
        this.ausgebendeStation = value;
    }

    /**
     * Ruft den Wert der ortFahrzeuguebernahme-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ReservierungInfoUeberAnmietung.OrtFahrzeuguebernahme }
     *     
     */
    public ReservierungInfoUeberAnmietung.OrtFahrzeuguebernahme getOrtFahrzeuguebernahme() {
        return ortFahrzeuguebernahme;
    }

    /**
     * Legt den Wert der ortFahrzeuguebernahme-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ReservierungInfoUeberAnmietung.OrtFahrzeuguebernahme }
     *     
     */
    public void setOrtFahrzeuguebernahme(ReservierungInfoUeberAnmietung.OrtFahrzeuguebernahme value) {
        this.ortFahrzeuguebernahme = value;
    }

    /**
     * Ruft den Wert der rueckgabetag-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRueckgabetag() {
        return rueckgabetag;
    }

    /**
     * Legt den Wert der rueckgabetag-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRueckgabetag(String value) {
        this.rueckgabetag = value;
    }

    /**
     * Ruft den Wert der rueckgabeuhrzeit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRueckgabeuhrzeit() {
        return rueckgabeuhrzeit;
    }

    /**
     * Legt den Wert der rueckgabeuhrzeit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRueckgabeuhrzeit(String value) {
        this.rueckgabeuhrzeit = value;
    }

    /**
     * Ruft den Wert der ruecknehmendeStation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ReservierungInfoUeberAnmietung.RuecknehmendeStation }
     *     
     */
    public ReservierungInfoUeberAnmietung.RuecknehmendeStation getRuecknehmendeStation() {
        return ruecknehmendeStation;
    }

    /**
     * Legt den Wert der ruecknehmendeStation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ReservierungInfoUeberAnmietung.RuecknehmendeStation }
     *     
     */
    public void setRuecknehmendeStation(ReservierungInfoUeberAnmietung.RuecknehmendeStation value) {
        this.ruecknehmendeStation = value;
    }

    /**
     * Ruft den Wert der ortFahrzeuguebergabe-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ReservierungInfoUeberAnmietung.OrtFahrzeuguebergabe }
     *     
     */
    public ReservierungInfoUeberAnmietung.OrtFahrzeuguebergabe getOrtFahrzeuguebergabe() {
        return ortFahrzeuguebergabe;
    }

    /**
     * Legt den Wert der ortFahrzeuguebergabe-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ReservierungInfoUeberAnmietung.OrtFahrzeuguebergabe }
     *     
     */
    public void setOrtFahrzeuguebergabe(ReservierungInfoUeberAnmietung.OrtFahrzeuguebergabe value) {
        this.ortFahrzeuguebergabe = value;
    }

    /**
     * Ruft den Wert der anmietintervall-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ReservierungInfoUeberAnmietung.Anmietintervall }
     *     
     */
    public ReservierungInfoUeberAnmietung.Anmietintervall getAnmietintervall() {
        return anmietintervall;
    }

    /**
     * Legt den Wert der anmietintervall-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ReservierungInfoUeberAnmietung.Anmietintervall }
     *     
     */
    public void setAnmietintervall(ReservierungInfoUeberAnmietung.Anmietintervall value) {
        this.anmietintervall = value;
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
            return "4350";
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
     *         &lt;element name="Wert" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               &lt;minInclusive value="0"/>
     *               &lt;totalDigits value="2"/>
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
    public static class Anmietintervall {

        @XmlElement(name = "Wert")
        protected BigInteger wert;
        @XmlElement(name = "Indikator", required = true)
        protected ReservierungInfoUeberAnmietung.Anmietintervall.Indikator indikator;

        /**
         * Ruft den Wert der wert-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getWert() {
            return wert;
        }

        /**
         * Legt den Wert der wert-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setWert(BigInteger value) {
            this.wert = value;
        }

        /**
         * Ruft den Wert der indikator-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link ReservierungInfoUeberAnmietung.Anmietintervall.Indikator }
         *     
         */
        public ReservierungInfoUeberAnmietung.Anmietintervall.Indikator getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link ReservierungInfoUeberAnmietung.Anmietintervall.Indikator }
         *     
         */
        public void setIndikator(ReservierungInfoUeberAnmietung.Anmietintervall.Indikator value) {
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
     *         &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
     *         &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
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
        "adressKennzeichen",
        "partnerReferenz"
    })
    public static class AusgebendeStation {

        @XmlElement(name = "AdressKennzeichen")
        protected String adressKennzeichen;
        @XmlElement(name = "PartnerReferenz")
        protected String partnerReferenz;

        /**
         * Ruft den Wert der adressKennzeichen-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAdressKennzeichen() {
            return adressKennzeichen;
        }

        /**
         * Legt den Wert der adressKennzeichen-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAdressKennzeichen(String value) {
            this.adressKennzeichen = value;
        }

        /**
         * Ruft den Wert der partnerReferenz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPartnerReferenz() {
            return partnerReferenz;
        }

        /**
         * Legt den Wert der partnerReferenz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPartnerReferenz(String value) {
            this.partnerReferenz = value;
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
     *         &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
     *         &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
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
        "adressKennzeichen",
        "partnerReferenz"
    })
    public static class OrtFahrzeuguebergabe {

        @XmlElement(name = "AdressKennzeichen")
        protected String adressKennzeichen;
        @XmlElement(name = "PartnerReferenz")
        protected String partnerReferenz;

        /**
         * Ruft den Wert der adressKennzeichen-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAdressKennzeichen() {
            return adressKennzeichen;
        }

        /**
         * Legt den Wert der adressKennzeichen-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAdressKennzeichen(String value) {
            this.adressKennzeichen = value;
        }

        /**
         * Ruft den Wert der partnerReferenz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPartnerReferenz() {
            return partnerReferenz;
        }

        /**
         * Legt den Wert der partnerReferenz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPartnerReferenz(String value) {
            this.partnerReferenz = value;
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
     *         &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
     *         &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
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
        "adressKennzeichen",
        "partnerReferenz"
    })
    public static class OrtFahrzeuguebernahme {

        @XmlElement(name = "AdressKennzeichen")
        protected String adressKennzeichen;
        @XmlElement(name = "PartnerReferenz")
        protected String partnerReferenz;

        /**
         * Ruft den Wert der adressKennzeichen-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAdressKennzeichen() {
            return adressKennzeichen;
        }

        /**
         * Legt den Wert der adressKennzeichen-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAdressKennzeichen(String value) {
            this.adressKennzeichen = value;
        }

        /**
         * Ruft den Wert der partnerReferenz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPartnerReferenz() {
            return partnerReferenz;
        }

        /**
         * Legt den Wert der partnerReferenz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPartnerReferenz(String value) {
            this.partnerReferenz = value;
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
     *         &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
     *         &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
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
        "adressKennzeichen",
        "partnerReferenz"
    })
    public static class RuecknehmendeStation {

        @XmlElement(name = "AdressKennzeichen")
        protected String adressKennzeichen;
        @XmlElement(name = "PartnerReferenz")
        protected String partnerReferenz;

        /**
         * Ruft den Wert der adressKennzeichen-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAdressKennzeichen() {
            return adressKennzeichen;
        }

        /**
         * Legt den Wert der adressKennzeichen-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAdressKennzeichen(String value) {
            this.adressKennzeichen = value;
        }

        /**
         * Ruft den Wert der partnerReferenz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPartnerReferenz() {
            return partnerReferenz;
        }

        /**
         * Legt den Wert der partnerReferenz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPartnerReferenz(String value) {
            this.partnerReferenz = value;
        }

    }

}
