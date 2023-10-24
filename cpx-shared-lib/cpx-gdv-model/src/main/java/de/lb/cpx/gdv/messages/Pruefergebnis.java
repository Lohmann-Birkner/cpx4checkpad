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
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ErgebnisPruefung"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kalkulation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Bewertung" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KalkulationRechnungSach" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KalkulationVergleichbareSache" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Leistungabrechnung" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Rechnung-SV" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Rechnung-MW" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PartnerdatenBlock" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Partnerdaten"/>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BeschaedigungenFahrzeug" minOccurs="0"/>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kommentar" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Anhang" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Nachrichtentyp" fixed="039">
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
    "ergebnisPruefung",
    "kalkulation",
    "bewertung",
    "kalkulationRechnungSach",
    "kalkulationVergleichbareSache",
    "leistungabrechnung",
    "rechnungSV",
    "rechnungMW",
    "partnerdatenBlock",
    "anhang"
})
@XmlRootElement(name = "Pruefergebnis")
public class Pruefergebnis {

    @XmlElement(name = "Header", required = true)
    protected Header header;
    @XmlElement(name = "BeschreibungLogischeEinheit", required = true)
    protected BeschreibungLogischeEinheit beschreibungLogischeEinheit;
    @XmlElement(name = "ErgebnisPruefung", required = true)
    protected ErgebnisPruefung ergebnisPruefung;
    @XmlElement(name = "Kalkulation")
    protected List<Kalkulation> kalkulation;
    @XmlElement(name = "Bewertung")
    protected List<Bewertung> bewertung;
    @XmlElement(name = "KalkulationRechnungSach")
    protected List<KalkulationRechnungSach> kalkulationRechnungSach;
    @XmlElement(name = "KalkulationVergleichbareSache")
    protected List<KalkulationVergleichbareSache> kalkulationVergleichbareSache;
    @XmlElement(name = "Leistungabrechnung")
    protected List<Leistungabrechnung> leistungabrechnung;
    @XmlElement(name = "Rechnung-SV")
    protected List<RechnungSV> rechnungSV;
    @XmlElement(name = "Rechnung-MW")
    protected List<RechnungMW> rechnungMW;
    @XmlElement(name = "PartnerdatenBlock", required = true)
    protected List<Pruefergebnis.PartnerdatenBlock> partnerdatenBlock;
    @XmlElement(name = "Anhang", required = true)
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
     * Ruft den Wert der ergebnisPruefung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ErgebnisPruefung }
     *     
     */
    public ErgebnisPruefung getErgebnisPruefung() {
        return ergebnisPruefung;
    }

    /**
     * Legt den Wert der ergebnisPruefung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ErgebnisPruefung }
     *     
     */
    public void setErgebnisPruefung(ErgebnisPruefung value) {
        this.ergebnisPruefung = value;
    }

    /**
     * Gets the value of the kalkulation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the kalkulation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKalkulation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Kalkulation }
     * 
     * 
     */
    public List<Kalkulation> getKalkulation() {
        if (kalkulation == null) {
            kalkulation = new ArrayList<Kalkulation>();
        }
        return this.kalkulation;
    }

    /**
     * Gets the value of the bewertung property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bewertung property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBewertung().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Bewertung }
     * 
     * 
     */
    public List<Bewertung> getBewertung() {
        if (bewertung == null) {
            bewertung = new ArrayList<Bewertung>();
        }
        return this.bewertung;
    }

    /**
     * Gets the value of the kalkulationRechnungSach property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the kalkulationRechnungSach property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKalkulationRechnungSach().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KalkulationRechnungSach }
     * 
     * 
     */
    public List<KalkulationRechnungSach> getKalkulationRechnungSach() {
        if (kalkulationRechnungSach == null) {
            kalkulationRechnungSach = new ArrayList<KalkulationRechnungSach>();
        }
        return this.kalkulationRechnungSach;
    }

    /**
     * Gets the value of the kalkulationVergleichbareSache property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the kalkulationVergleichbareSache property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKalkulationVergleichbareSache().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KalkulationVergleichbareSache }
     * 
     * 
     */
    public List<KalkulationVergleichbareSache> getKalkulationVergleichbareSache() {
        if (kalkulationVergleichbareSache == null) {
            kalkulationVergleichbareSache = new ArrayList<KalkulationVergleichbareSache>();
        }
        return this.kalkulationVergleichbareSache;
    }

    /**
     * Gets the value of the leistungabrechnung property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the leistungabrechnung property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLeistungabrechnung().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Leistungabrechnung }
     * 
     * 
     */
    public List<Leistungabrechnung> getLeistungabrechnung() {
        if (leistungabrechnung == null) {
            leistungabrechnung = new ArrayList<Leistungabrechnung>();
        }
        return this.leistungabrechnung;
    }

    /**
     * Gets the value of the rechnungSV property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rechnungSV property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRechnungSV().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RechnungSV }
     * 
     * 
     */
    public List<RechnungSV> getRechnungSV() {
        if (rechnungSV == null) {
            rechnungSV = new ArrayList<RechnungSV>();
        }
        return this.rechnungSV;
    }

    /**
     * Gets the value of the rechnungMW property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rechnungMW property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRechnungMW().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RechnungMW }
     * 
     * 
     */
    public List<RechnungMW> getRechnungMW() {
        if (rechnungMW == null) {
            rechnungMW = new ArrayList<RechnungMW>();
        }
        return this.rechnungMW;
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
     * {@link Pruefergebnis.PartnerdatenBlock }
     * 
     * 
     */
    public List<Pruefergebnis.PartnerdatenBlock> getPartnerdatenBlock() {
        if (partnerdatenBlock == null) {
            partnerdatenBlock = new ArrayList<Pruefergebnis.PartnerdatenBlock>();
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
            return "039";
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
     *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BeschaedigungenFahrzeug" minOccurs="0"/>
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
        "beschaedigungenFahrzeug",
        "kommentar"
    })
    public static class PartnerdatenBlock {

        @XmlElement(name = "Partnerdaten", required = true)
        protected Partnerdaten partnerdaten;
        @XmlElement(name = "BeschaedigungenFahrzeug")
        protected BeschaedigungenFahrzeug beschaedigungenFahrzeug;
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
