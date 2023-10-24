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
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AngebotRechnungsrahmendaten"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AngebotRechnungseinzelpositionen" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ErgebnisdatenSach" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LeckageMessprotokoll" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PartnerdatenBlock" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Partnerdaten"/>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VertragsmerkmaleSach" minOccurs="0"/>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kommentar" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Anhang" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Nachrichtentyp" fixed="031">
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
    "angebotRechnungsrahmendaten",
    "angebotRechnungseinzelpositionen",
    "ergebnisdatenSach",
    "leckageMessprotokoll",
    "partnerdatenBlock",
    "anhang"
})
@XmlRootElement(name = "SachAngebotRechnung")
public class SachAngebotRechnung {

    @XmlElement(name = "Header", required = true)
    protected Header header;
    @XmlElement(name = "BeschreibungLogischeEinheit", required = true)
    protected BeschreibungLogischeEinheit beschreibungLogischeEinheit;
    @XmlElement(name = "AngebotRechnungsrahmendaten", required = true)
    protected AngebotRechnungsrahmendaten angebotRechnungsrahmendaten;
    @XmlElement(name = "AngebotRechnungseinzelpositionen", required = true)
    protected List<AngebotRechnungseinzelpositionen> angebotRechnungseinzelpositionen;
    @XmlElement(name = "ErgebnisdatenSach")
    protected ErgebnisdatenSach ergebnisdatenSach;
    @XmlElement(name = "LeckageMessprotokoll")
    protected List<LeckageMessprotokoll> leckageMessprotokoll;
    @XmlElement(name = "PartnerdatenBlock", required = true)
    protected List<SachAngebotRechnung.PartnerdatenBlock> partnerdatenBlock;
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
     * Ruft den Wert der angebotRechnungsrahmendaten-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AngebotRechnungsrahmendaten }
     *     
     */
    public AngebotRechnungsrahmendaten getAngebotRechnungsrahmendaten() {
        return angebotRechnungsrahmendaten;
    }

    /**
     * Legt den Wert der angebotRechnungsrahmendaten-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AngebotRechnungsrahmendaten }
     *     
     */
    public void setAngebotRechnungsrahmendaten(AngebotRechnungsrahmendaten value) {
        this.angebotRechnungsrahmendaten = value;
    }

    /**
     * Gets the value of the angebotRechnungseinzelpositionen property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the angebotRechnungseinzelpositionen property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAngebotRechnungseinzelpositionen().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AngebotRechnungseinzelpositionen }
     * 
     * 
     */
    public List<AngebotRechnungseinzelpositionen> getAngebotRechnungseinzelpositionen() {
        if (angebotRechnungseinzelpositionen == null) {
            angebotRechnungseinzelpositionen = new ArrayList<AngebotRechnungseinzelpositionen>();
        }
        return this.angebotRechnungseinzelpositionen;
    }

    /**
     * Ruft den Wert der ergebnisdatenSach-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ErgebnisdatenSach }
     *     
     */
    public ErgebnisdatenSach getErgebnisdatenSach() {
        return ergebnisdatenSach;
    }

    /**
     * Legt den Wert der ergebnisdatenSach-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ErgebnisdatenSach }
     *     
     */
    public void setErgebnisdatenSach(ErgebnisdatenSach value) {
        this.ergebnisdatenSach = value;
    }

    /**
     * Gets the value of the leckageMessprotokoll property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the leckageMessprotokoll property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLeckageMessprotokoll().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LeckageMessprotokoll }
     * 
     * 
     */
    public List<LeckageMessprotokoll> getLeckageMessprotokoll() {
        if (leckageMessprotokoll == null) {
            leckageMessprotokoll = new ArrayList<LeckageMessprotokoll>();
        }
        return this.leckageMessprotokoll;
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
     * {@link SachAngebotRechnung.PartnerdatenBlock }
     * 
     * 
     */
    public List<SachAngebotRechnung.PartnerdatenBlock> getPartnerdatenBlock() {
        if (partnerdatenBlock == null) {
            partnerdatenBlock = new ArrayList<SachAngebotRechnung.PartnerdatenBlock>();
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
            return "031";
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
     *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VertragsmerkmaleSach" minOccurs="0"/>
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
        "vertragsmerkmaleSach",
        "kommentar"
    })
    public static class PartnerdatenBlock {

        @XmlElement(name = "Partnerdaten", required = true)
        protected Partnerdaten partnerdaten;
        @XmlElement(name = "VertragsmerkmaleSach")
        protected VertragsmerkmaleSach vertragsmerkmaleSach;
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
         * Ruft den Wert der vertragsmerkmaleSach-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link VertragsmerkmaleSach }
         *     
         */
        public VertragsmerkmaleSach getVertragsmerkmaleSach() {
            return vertragsmerkmaleSach;
        }

        /**
         * Legt den Wert der vertragsmerkmaleSach-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link VertragsmerkmaleSach }
         *     
         */
        public void setVertragsmerkmaleSach(VertragsmerkmaleSach value) {
            this.vertragsmerkmaleSach = value;
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
