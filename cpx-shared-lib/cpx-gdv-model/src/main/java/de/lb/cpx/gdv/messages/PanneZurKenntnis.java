//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.19 um 11:55:14 AM CEST 
//


package de.lb.cpx.gdv.messages;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Header"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BeschreibungLogischeEinheit"/>
 *         &lt;choice>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Panne"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Unfall"/>
 *         &lt;/choice>
 *         &lt;element name="PartnerdatenBlock" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Partnerdaten" minOccurs="0"/>
 *                   &lt;sequence minOccurs="0">
 *                     &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Daten"/>
 *                     &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Ausstattung" minOccurs="0"/>
 *                   &lt;/sequence>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Vertragsmerkmale" minOccurs="0"/>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kommentar" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Anhang" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Nachrichtentyp" fixed="044">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;length value="3"/>
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
    "header",
    "beschreibungLogischeEinheit",
    "panne",
    "unfall",
    "partnerdatenBlock",
    "anhang"
})
@XmlRootElement(name = "PanneZurKenntnis")
public class PanneZurKenntnis {

    @XmlElement(name = "Header", required = true)
    protected Header header;
    @XmlElement(name = "BeschreibungLogischeEinheit", required = true)
    protected BeschreibungLogischeEinheit beschreibungLogischeEinheit;
    @XmlElement(name = "Panne")
    protected Panne panne;
    @XmlElement(name = "Unfall")
    protected Unfall unfall;
    @XmlElement(name = "PartnerdatenBlock")
    protected List<PanneZurKenntnis.PartnerdatenBlock> partnerdatenBlock;
    @XmlElement(name = "Anhang")
    protected List<Anhang> anhang;
    @XmlAttribute(name = "Nachrichtentyp")
    protected String nachrichtentyp;

    /**
     * Ruft den Wert der header-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Header }
     *     
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Legt den Wert der header-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Header }
     *     
     */
    public void setHeader(Header value) {
        this.header = value;
    }

    /**
     * Ruft den Wert der beschreibungLogischeEinheit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeschreibungLogischeEinheit }
     *     
     */
    public BeschreibungLogischeEinheit getBeschreibungLogischeEinheit() {
        return beschreibungLogischeEinheit;
    }

    /**
     * Legt den Wert der beschreibungLogischeEinheit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeschreibungLogischeEinheit }
     *     
     */
    public void setBeschreibungLogischeEinheit(BeschreibungLogischeEinheit value) {
        this.beschreibungLogischeEinheit = value;
    }

    /**
     * Ruft den Wert der panne-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Panne }
     *     
     */
    public Panne getPanne() {
        return panne;
    }

    /**
     * Legt den Wert der panne-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Panne }
     *     
     */
    public void setPanne(Panne value) {
        this.panne = value;
    }

    /**
     * Ruft den Wert der unfall-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Unfall }
     *     
     */
    public Unfall getUnfall() {
        return unfall;
    }

    /**
     * Legt den Wert der unfall-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Unfall }
     *     
     */
    public void setUnfall(Unfall value) {
        this.unfall = value;
    }

    /**
     * Gets the value of the partnerdatenBlock property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the partnerdatenBlock property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPartnerdatenBlock().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PanneZurKenntnis.PartnerdatenBlock }
     * 
     * 
     */
    public List<PanneZurKenntnis.PartnerdatenBlock> getPartnerdatenBlock() {
        if (partnerdatenBlock == null) {
            partnerdatenBlock = new ArrayList<PanneZurKenntnis.PartnerdatenBlock>();
        }
        return this.partnerdatenBlock;
    }

    /**
     * Gets the value of the anhang property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the anhang property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnhang().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Anhang }
     * 
     * 
     */
    public List<Anhang> getAnhang() {
        if (anhang == null) {
            anhang = new ArrayList<Anhang>();
        }
        return this.anhang;
    }

    /**
     * Ruft den Wert der nachrichtentyp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNachrichtentyp() {
        if (nachrichtentyp == null) {
            return "044";
        } else {
            return nachrichtentyp;
        }
    }

    /**
     * Legt den Wert der nachrichtentyp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNachrichtentyp(String value) {
        this.nachrichtentyp = value;
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
     *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Partnerdaten" minOccurs="0"/>
     *         &lt;sequence minOccurs="0">
     *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Daten"/>
     *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Ausstattung" minOccurs="0"/>
     *         &lt;/sequence>
     *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Vertragsmerkmale" minOccurs="0"/>
     *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kommentar" maxOccurs="unbounded" minOccurs="0"/>
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
        "partnerdaten",
        "kfzDaten",
        "kfzAusstattung",
        "vertragsmerkmale",
        "kommentar"
    })
    public static class PartnerdatenBlock {

        @XmlElement(name = "Partnerdaten")
        protected Partnerdaten partnerdaten;
        @XmlElement(name = "Kfz-Daten")
        protected KfzDaten kfzDaten;
        @XmlElement(name = "Kfz-Ausstattung")
        protected KfzAusstattung kfzAusstattung;
        @XmlElement(name = "Vertragsmerkmale")
        protected Vertragsmerkmale vertragsmerkmale;
        @XmlElement(name = "Kommentar")
        protected List<Kommentar> kommentar;

        /**
         * Ruft den Wert der partnerdaten-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Partnerdaten }
         *     
         */
        public Partnerdaten getPartnerdaten() {
            return partnerdaten;
        }

        /**
         * Legt den Wert der partnerdaten-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Partnerdaten }
         *     
         */
        public void setPartnerdaten(Partnerdaten value) {
            this.partnerdaten = value;
        }

        /**
         * Ruft den Wert der kfzDaten-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KfzDaten }
         *     
         */
        public KfzDaten getKfzDaten() {
            return kfzDaten;
        }

        /**
         * Legt den Wert der kfzDaten-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KfzDaten }
         *     
         */
        public void setKfzDaten(KfzDaten value) {
            this.kfzDaten = value;
        }

        /**
         * Ruft den Wert der kfzAusstattung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KfzAusstattung }
         *     
         */
        public KfzAusstattung getKfzAusstattung() {
            return kfzAusstattung;
        }

        /**
         * Legt den Wert der kfzAusstattung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KfzAusstattung }
         *     
         */
        public void setKfzAusstattung(KfzAusstattung value) {
            this.kfzAusstattung = value;
        }

        /**
         * Ruft den Wert der vertragsmerkmale-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Vertragsmerkmale }
         *     
         */
        public Vertragsmerkmale getVertragsmerkmale() {
            return vertragsmerkmale;
        }

        /**
         * Legt den Wert der vertragsmerkmale-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Vertragsmerkmale }
         *     
         */
        public void setVertragsmerkmale(Vertragsmerkmale value) {
            this.vertragsmerkmale = value;
        }

        /**
         * Gets the value of the kommentar property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the kommentar property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getKommentar().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Kommentar }
         * 
         * 
         */
        public List<Kommentar> getKommentar() {
            if (kommentar == null) {
                kommentar = new ArrayList<Kommentar>();
            }
            return this.kommentar;
        }

    }

}
