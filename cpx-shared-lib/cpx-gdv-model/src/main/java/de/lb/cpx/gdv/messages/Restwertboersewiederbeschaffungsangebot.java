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
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Wiederbeschaffung-RWB"/>
 *         &lt;element name="PartnerdatenBlock">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Partnerdaten"/>
 *                   &lt;sequence>
 *                     &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Daten"/>
 *                     &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Ausstattung" minOccurs="0"/>
 *                   &lt;/sequence>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kommentar" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kalkulation" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Leistungabrechnung" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Anhang" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Nachrichtentyp" fixed="011">
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
    "wiederbeschaffungRWB",
    "partnerdatenBlock",
    "kalkulation",
    "leistungabrechnung",
    "anhang"
})
@XmlRootElement(name = "Restwertboersewiederbeschaffungsangebot")
public class Restwertboersewiederbeschaffungsangebot {

    @XmlElement(name = "Header", required = true)
    protected Header header;
    @XmlElement(name = "BeschreibungLogischeEinheit", required = true)
    protected BeschreibungLogischeEinheit beschreibungLogischeEinheit;
    @XmlElement(name = "Wiederbeschaffung-RWB", required = true)
    protected WiederbeschaffungRWB wiederbeschaffungRWB;
    @XmlElement(name = "PartnerdatenBlock", required = true)
    protected Restwertboersewiederbeschaffungsangebot.PartnerdatenBlock partnerdatenBlock;
    @XmlElement(name = "Kalkulation")
    protected Kalkulation kalkulation;
    @XmlElement(name = "Leistungabrechnung")
    protected Leistungabrechnung leistungabrechnung;
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
     * Ruft den Wert der wiederbeschaffungRWB-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link WiederbeschaffungRWB }
     *     
     */
    public WiederbeschaffungRWB getWiederbeschaffungRWB() {
        return wiederbeschaffungRWB;
    }

    /**
     * Legt den Wert der wiederbeschaffungRWB-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link WiederbeschaffungRWB }
     *     
     */
    public void setWiederbeschaffungRWB(WiederbeschaffungRWB value) {
        this.wiederbeschaffungRWB = value;
    }

    /**
     * Ruft den Wert der partnerdatenBlock-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Restwertboersewiederbeschaffungsangebot.PartnerdatenBlock }
     *     
     */
    public Restwertboersewiederbeschaffungsangebot.PartnerdatenBlock getPartnerdatenBlock() {
        return partnerdatenBlock;
    }

    /**
     * Legt den Wert der partnerdatenBlock-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Restwertboersewiederbeschaffungsangebot.PartnerdatenBlock }
     *     
     */
    public void setPartnerdatenBlock(Restwertboersewiederbeschaffungsangebot.PartnerdatenBlock value) {
        this.partnerdatenBlock = value;
    }

    /**
     * Ruft den Wert der kalkulation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Kalkulation }
     *     
     */
    public Kalkulation getKalkulation() {
        return kalkulation;
    }

    /**
     * Legt den Wert der kalkulation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Kalkulation }
     *     
     */
    public void setKalkulation(Kalkulation value) {
        this.kalkulation = value;
    }

    /**
     * Ruft den Wert der leistungabrechnung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Leistungabrechnung }
     *     
     */
    public Leistungabrechnung getLeistungabrechnung() {
        return leistungabrechnung;
    }

    /**
     * Legt den Wert der leistungabrechnung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Leistungabrechnung }
     *     
     */
    public void setLeistungabrechnung(Leistungabrechnung value) {
        this.leistungabrechnung = value;
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
            return "011";
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
     *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Partnerdaten"/>
     *         &lt;sequence>
     *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Daten"/>
     *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Ausstattung" minOccurs="0"/>
     *         &lt;/sequence>
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
        "kommentar"
    })
    public static class PartnerdatenBlock {

        @XmlElement(name = "Partnerdaten", required = true)
        protected Partnerdaten partnerdaten;
        @XmlElement(name = "Kfz-Daten", required = true)
        protected KfzDaten kfzDaten;
        @XmlElement(name = "Kfz-Ausstattung")
        protected KfzAusstattung kfzAusstattung;
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
