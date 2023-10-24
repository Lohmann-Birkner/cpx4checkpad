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
import javax.xml.bind.annotation.XmlElements;
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
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}RechtsschutzMeldung"/>
 *         &lt;element name="PartnerdatenBlock" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Partnerdaten"/>
 *                   &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                     &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Daten"/>
 *                     &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Ausstattung" minOccurs="0"/>
 *                   &lt;/sequence>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BeschaedigungenFahrzeug" minOccurs="0"/>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Ermittlungen" minOccurs="0"/>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Schadenhergang" minOccurs="0"/>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kommentar" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Anhang" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Nachrichtentyp" fixed="026">
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
    "rechtsschutzMeldung",
    "partnerdatenBlock",
    "anhang"
})
@XmlRootElement(name = "RechtsschutzSchadenmeldung")
public class RechtsschutzSchadenmeldung {

    @XmlElement(name = "Header", required = true)
    protected Header header;
    @XmlElement(name = "BeschreibungLogischeEinheit", required = true)
    protected BeschreibungLogischeEinheit beschreibungLogischeEinheit;
    @XmlElement(name = "RechtsschutzMeldung", required = true)
    protected RechtsschutzMeldung rechtsschutzMeldung;
    @XmlElement(name = "PartnerdatenBlock", required = true)
    protected List<RechtsschutzSchadenmeldung.PartnerdatenBlock> partnerdatenBlock;
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
     * Ruft den Wert der rechtsschutzMeldung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RechtsschutzMeldung }
     *     
     */
    public RechtsschutzMeldung getRechtsschutzMeldung() {
        return rechtsschutzMeldung;
    }

    /**
     * Legt den Wert der rechtsschutzMeldung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RechtsschutzMeldung }
     *     
     */
    public void setRechtsschutzMeldung(RechtsschutzMeldung value) {
        this.rechtsschutzMeldung = value;
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
     * {@link RechtsschutzSchadenmeldung.PartnerdatenBlock }
     * 
     * 
     */
    public List<RechtsschutzSchadenmeldung.PartnerdatenBlock> getPartnerdatenBlock() {
        if (partnerdatenBlock == null) {
            partnerdatenBlock = new ArrayList<RechtsschutzSchadenmeldung.PartnerdatenBlock>();
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
            return "026";
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
     *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Daten"/>
     *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Ausstattung" minOccurs="0"/>
     *         &lt;/sequence>
     *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BeschaedigungenFahrzeug" minOccurs="0"/>
     *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Ermittlungen" minOccurs="0"/>
     *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Schadenhergang" minOccurs="0"/>
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
        "kfzDatenAndKfzAusstattung",
        "beschaedigungenFahrzeug",
        "ermittlungen",
        "schadenhergang",
        "kommentar"
    })
    public static class PartnerdatenBlock {

        @XmlElement(name = "Partnerdaten", required = true)
        protected Partnerdaten partnerdaten;
        @XmlElements({
            @XmlElement(name = "Kfz-Daten", type = KfzDaten.class),
            @XmlElement(name = "Kfz-Ausstattung", type = KfzAusstattung.class)
        })
        protected List<Object> kfzDatenAndKfzAusstattung;
        @XmlElement(name = "BeschaedigungenFahrzeug")
        protected BeschaedigungenFahrzeug beschaedigungenFahrzeug;
        @XmlElement(name = "Ermittlungen")
        protected Ermittlungen ermittlungen;
        @XmlElement(name = "Schadenhergang")
        protected Schadenhergang schadenhergang;
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
         * Gets the value of the kfzDatenAndKfzAusstattung property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the kfzDatenAndKfzAusstattung property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getKfzDatenAndKfzAusstattung().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link KfzDaten }
         * {@link KfzAusstattung }
         * 
         * 
         */
        public List<Object> getKfzDatenAndKfzAusstattung() {
            if (kfzDatenAndKfzAusstattung == null) {
                kfzDatenAndKfzAusstattung = new ArrayList<Object>();
            }
            return this.kfzDatenAndKfzAusstattung;
        }

        /**
         * Ruft den Wert der beschaedigungenFahrzeug-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BeschaedigungenFahrzeug }
         *     
         */
        public BeschaedigungenFahrzeug getBeschaedigungenFahrzeug() {
            return beschaedigungenFahrzeug;
        }

        /**
         * Legt den Wert der beschaedigungenFahrzeug-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BeschaedigungenFahrzeug }
         *     
         */
        public void setBeschaedigungenFahrzeug(BeschaedigungenFahrzeug value) {
            this.beschaedigungenFahrzeug = value;
        }

        /**
         * Ruft den Wert der ermittlungen-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Ermittlungen }
         *     
         */
        public Ermittlungen getErmittlungen() {
            return ermittlungen;
        }

        /**
         * Legt den Wert der ermittlungen-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Ermittlungen }
         *     
         */
        public void setErmittlungen(Ermittlungen value) {
            this.ermittlungen = value;
        }

        /**
         * Ruft den Wert der schadenhergang-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Schadenhergang }
         *     
         */
        public Schadenhergang getSchadenhergang() {
            return schadenhergang;
        }

        /**
         * Legt den Wert der schadenhergang-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Schadenhergang }
         *     
         */
        public void setSchadenhergang(Schadenhergang value) {
            this.schadenhergang = value;
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
