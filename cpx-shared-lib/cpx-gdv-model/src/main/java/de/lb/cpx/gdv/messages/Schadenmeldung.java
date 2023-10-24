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
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AllgemeineSchadendaten" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kalkulation" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Schadenmanagement" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Schadenverzeichnis" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Gebaeudeschaden" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}HaftSpezifischeSchadendaten" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}UnfallSpezifischeSchadendaten" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}RechtsschutzMeldung" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachSchadendatenFeuer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachSchadendatenBlitz" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachSchadendatenDiebstahl" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachSchadendatenLwElementar" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachSchadendatenTechnisch" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachSchadendatenTransport" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PartnerdatenBlock" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Partnerdaten"/>
 *                   &lt;sequence minOccurs="0">
 *                     &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Daten"/>
 *                     &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Ausstattung" minOccurs="0"/>
 *                   &lt;/sequence>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Vertragsmerkmale" minOccurs="0"/>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BeschaedigungenFahrzeug" minOccurs="0"/>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BeschaedigteSache" minOccurs="0"/>
 *                   &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Personenschaden" minOccurs="0"/>
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
 *       &lt;attribute name="Nachrichtentyp" fixed="001">
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
    "allgemeineSchadendaten",
    "kalkulation",
    "schadenmanagement",
    "schadenverzeichnis",
    "gebaeudeschaden",
    "haftSpezifischeSchadendaten",
    "unfallSpezifischeSchadendaten",
    "rechtsschutzMeldung",
    "sachSchadendatenFeuer",
    "sachSchadendatenBlitz",
    "sachSchadendatenDiebstahl",
    "sachSchadendatenLwElementar",
    "sachSchadendatenTechnisch",
    "sachSchadendatenTransport",
    "partnerdatenBlock",
    "anhang"
})
@XmlRootElement(name = "Schadenmeldung")
public class Schadenmeldung {

    @XmlElement(name = "Header", required = true)
    protected Header header;
    @XmlElement(name = "BeschreibungLogischeEinheit", required = true)
    protected BeschreibungLogischeEinheit beschreibungLogischeEinheit;
    @XmlElement(name = "AllgemeineSchadendaten")
    protected AllgemeineSchadendaten allgemeineSchadendaten;
    @XmlElement(name = "Kalkulation")
    protected Kalkulation kalkulation;
    @XmlElement(name = "Schadenmanagement")
    protected Schadenmanagement schadenmanagement;
    @XmlElement(name = "Schadenverzeichnis")
    protected List<Schadenverzeichnis> schadenverzeichnis;
    @XmlElement(name = "Gebaeudeschaden")
    protected List<Gebaeudeschaden> gebaeudeschaden;
    @XmlElement(name = "HaftSpezifischeSchadendaten")
    protected HaftSpezifischeSchadendaten haftSpezifischeSchadendaten;
    @XmlElement(name = "UnfallSpezifischeSchadendaten")
    protected UnfallSpezifischeSchadendaten unfallSpezifischeSchadendaten;
    @XmlElement(name = "RechtsschutzMeldung")
    protected RechtsschutzMeldung rechtsschutzMeldung;
    @XmlElement(name = "SachSchadendatenFeuer")
    protected List<SachSchadendatenFeuer> sachSchadendatenFeuer;
    @XmlElement(name = "SachSchadendatenBlitz")
    protected List<SachSchadendatenBlitz> sachSchadendatenBlitz;
    @XmlElement(name = "SachSchadendatenDiebstahl")
    protected List<SachSchadendatenDiebstahl> sachSchadendatenDiebstahl;
    @XmlElement(name = "SachSchadendatenLwElementar")
    protected List<SachSchadendatenLwElementar> sachSchadendatenLwElementar;
    @XmlElement(name = "SachSchadendatenTechnisch")
    protected List<SachSchadendatenTechnisch> sachSchadendatenTechnisch;
    @XmlElement(name = "SachSchadendatenTransport")
    protected List<SachSchadendatenTransport> sachSchadendatenTransport;
    @XmlElement(name = "PartnerdatenBlock", required = true)
    protected List<Schadenmeldung.PartnerdatenBlock> partnerdatenBlock;
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
     * Ruft den Wert der allgemeineSchadendaten-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten }
     *     
     */
    public AllgemeineSchadendaten getAllgemeineSchadendaten() {
        return allgemeineSchadendaten;
    }

    /**
     * Legt den Wert der allgemeineSchadendaten-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten }
     *     
     */
    public void setAllgemeineSchadendaten(AllgemeineSchadendaten value) {
        this.allgemeineSchadendaten = value;
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
     * Ruft den Wert der schadenmanagement-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Schadenmanagement }
     *     
     */
    public Schadenmanagement getSchadenmanagement() {
        return schadenmanagement;
    }

    /**
     * Legt den Wert der schadenmanagement-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Schadenmanagement }
     *     
     */
    public void setSchadenmanagement(Schadenmanagement value) {
        this.schadenmanagement = value;
    }

    /**
     * Gets the value of the schadenverzeichnis property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the schadenverzeichnis property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSchadenverzeichnis().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Schadenverzeichnis }
     * 
     * 
     */
    public List<Schadenverzeichnis> getSchadenverzeichnis() {
        if (schadenverzeichnis == null) {
            schadenverzeichnis = new ArrayList<Schadenverzeichnis>();
        }
        return this.schadenverzeichnis;
    }

    /**
     * Gets the value of the gebaeudeschaden property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gebaeudeschaden property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGebaeudeschaden().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Gebaeudeschaden }
     * 
     * 
     */
    public List<Gebaeudeschaden> getGebaeudeschaden() {
        if (gebaeudeschaden == null) {
            gebaeudeschaden = new ArrayList<Gebaeudeschaden>();
        }
        return this.gebaeudeschaden;
    }

    /**
     * Ruft den Wert der haftSpezifischeSchadendaten-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link HaftSpezifischeSchadendaten }
     *     
     */
    public HaftSpezifischeSchadendaten getHaftSpezifischeSchadendaten() {
        return haftSpezifischeSchadendaten;
    }

    /**
     * Legt den Wert der haftSpezifischeSchadendaten-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link HaftSpezifischeSchadendaten }
     *     
     */
    public void setHaftSpezifischeSchadendaten(HaftSpezifischeSchadendaten value) {
        this.haftSpezifischeSchadendaten = value;
    }

    /**
     * Ruft den Wert der unfallSpezifischeSchadendaten-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link UnfallSpezifischeSchadendaten }
     *     
     */
    public UnfallSpezifischeSchadendaten getUnfallSpezifischeSchadendaten() {
        return unfallSpezifischeSchadendaten;
    }

    /**
     * Legt den Wert der unfallSpezifischeSchadendaten-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link UnfallSpezifischeSchadendaten }
     *     
     */
    public void setUnfallSpezifischeSchadendaten(UnfallSpezifischeSchadendaten value) {
        this.unfallSpezifischeSchadendaten = value;
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
     * Gets the value of the sachSchadendatenFeuer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sachSchadendatenFeuer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSachSchadendatenFeuer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SachSchadendatenFeuer }
     * 
     * 
     */
    public List<SachSchadendatenFeuer> getSachSchadendatenFeuer() {
        if (sachSchadendatenFeuer == null) {
            sachSchadendatenFeuer = new ArrayList<SachSchadendatenFeuer>();
        }
        return this.sachSchadendatenFeuer;
    }

    /**
     * Gets the value of the sachSchadendatenBlitz property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sachSchadendatenBlitz property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSachSchadendatenBlitz().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SachSchadendatenBlitz }
     * 
     * 
     */
    public List<SachSchadendatenBlitz> getSachSchadendatenBlitz() {
        if (sachSchadendatenBlitz == null) {
            sachSchadendatenBlitz = new ArrayList<SachSchadendatenBlitz>();
        }
        return this.sachSchadendatenBlitz;
    }

    /**
     * Gets the value of the sachSchadendatenDiebstahl property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sachSchadendatenDiebstahl property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSachSchadendatenDiebstahl().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SachSchadendatenDiebstahl }
     * 
     * 
     */
    public List<SachSchadendatenDiebstahl> getSachSchadendatenDiebstahl() {
        if (sachSchadendatenDiebstahl == null) {
            sachSchadendatenDiebstahl = new ArrayList<SachSchadendatenDiebstahl>();
        }
        return this.sachSchadendatenDiebstahl;
    }

    /**
     * Gets the value of the sachSchadendatenLwElementar property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sachSchadendatenLwElementar property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSachSchadendatenLwElementar().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SachSchadendatenLwElementar }
     * 
     * 
     */
    public List<SachSchadendatenLwElementar> getSachSchadendatenLwElementar() {
        if (sachSchadendatenLwElementar == null) {
            sachSchadendatenLwElementar = new ArrayList<SachSchadendatenLwElementar>();
        }
        return this.sachSchadendatenLwElementar;
    }

    /**
     * Gets the value of the sachSchadendatenTechnisch property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sachSchadendatenTechnisch property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSachSchadendatenTechnisch().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SachSchadendatenTechnisch }
     * 
     * 
     */
    public List<SachSchadendatenTechnisch> getSachSchadendatenTechnisch() {
        if (sachSchadendatenTechnisch == null) {
            sachSchadendatenTechnisch = new ArrayList<SachSchadendatenTechnisch>();
        }
        return this.sachSchadendatenTechnisch;
    }

    /**
     * Gets the value of the sachSchadendatenTransport property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sachSchadendatenTransport property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSachSchadendatenTransport().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SachSchadendatenTransport }
     * 
     * 
     */
    public List<SachSchadendatenTransport> getSachSchadendatenTransport() {
        if (sachSchadendatenTransport == null) {
            sachSchadendatenTransport = new ArrayList<SachSchadendatenTransport>();
        }
        return this.sachSchadendatenTransport;
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
     * {@link Schadenmeldung.PartnerdatenBlock }
     * 
     * 
     */
    public List<Schadenmeldung.PartnerdatenBlock> getPartnerdatenBlock() {
        if (partnerdatenBlock == null) {
            partnerdatenBlock = new ArrayList<Schadenmeldung.PartnerdatenBlock>();
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
            return "001";
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
     *         &lt;sequence minOccurs="0">
     *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Daten"/>
     *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Ausstattung" minOccurs="0"/>
     *         &lt;/sequence>
     *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Vertragsmerkmale" minOccurs="0"/>
     *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BeschaedigungenFahrzeug" minOccurs="0"/>
     *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BeschaedigteSache" minOccurs="0"/>
     *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Personenschaden" minOccurs="0"/>
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
        "kfzDaten",
        "kfzAusstattung",
        "vertragsmerkmale",
        "beschaedigungenFahrzeug",
        "beschaedigteSache",
        "personenschaden",
        "ermittlungen",
        "schadenhergang",
        "kommentar"
    })
    public static class PartnerdatenBlock {

        @XmlElement(name = "Partnerdaten", required = true)
        protected Partnerdaten partnerdaten;
        @XmlElement(name = "Kfz-Daten")
        protected KfzDaten kfzDaten;
        @XmlElement(name = "Kfz-Ausstattung")
        protected KfzAusstattung kfzAusstattung;
        @XmlElement(name = "Vertragsmerkmale")
        protected Vertragsmerkmale vertragsmerkmale;
        @XmlElement(name = "BeschaedigungenFahrzeug")
        protected BeschaedigungenFahrzeug beschaedigungenFahrzeug;
        @XmlElement(name = "BeschaedigteSache")
        protected BeschaedigteSache beschaedigteSache;
        @XmlElement(name = "Personenschaden")
        protected Personenschaden personenschaden;
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
         * Ruft den Wert der beschaedigteSache-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BeschaedigteSache }
         *     
         */
        public BeschaedigteSache getBeschaedigteSache() {
            return beschaedigteSache;
        }

        /**
         * Legt den Wert der beschaedigteSache-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BeschaedigteSache }
         *     
         */
        public void setBeschaedigteSache(BeschaedigteSache value) {
            this.beschaedigteSache = value;
        }

        /**
         * Ruft den Wert der personenschaden-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Personenschaden }
         *     
         */
        public Personenschaden getPersonenschaden() {
            return personenschaden;
        }

        /**
         * Legt den Wert der personenschaden-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Personenschaden }
         *     
         */
        public void setPersonenschaden(Personenschaden value) {
            this.personenschaden = value;
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
