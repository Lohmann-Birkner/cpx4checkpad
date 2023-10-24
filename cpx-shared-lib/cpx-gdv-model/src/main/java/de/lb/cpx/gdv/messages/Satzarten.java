//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.19 um 11:55:14 AM CEST 
//


package de.lb.cpx.gdv.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AllgemeineSchadendaten"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Anfrage-RWB"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AngebotRechnungseinzelpositionen"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AngebotRechnungsrahmendaten"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Angebot-RWB"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Anhang"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Beauftragung-SV"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Behebungsauftrag"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BeschaedigteSache"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BeschaedigungenFahrzeug"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BeschreibungLogischeEinheit"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Bewertung"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ErgebnisdatenSach"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ErgebnisPruefung"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Ermittlungen"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fehlerbeschreibung"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Gebaeudeschaden"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}HaftSpezifischeSchadendaten"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kalkulation"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KalkulationRechnungSach"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KalkulationVergleichbareSache"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Ausstattung"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kfz-Daten"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KfzGK"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kommentar"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LeckageMessprotokoll"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Leistungabrechnung"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LeistungsabrechnungRA"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}MandatsBeauftragung"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Nachsatz"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Panne"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Partnerdaten"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Personenschaden"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Quittungsdaten"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}RALeistungsabrechnungEinzelposition"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Rechnung-MW"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Rechnung-SV"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}RechtsschutzMeldung"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Regress"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Reparaturfreigabe"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ReservierungInfoUeberAnmietung"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Rueckmeldung"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachSchadendatenBlitz"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachSchadendatenDiebstahl"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachSchadendatenFeuer"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachSchadendatenLwElementar"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachSchadendatenTechnisch"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachSchadendatenTransport"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachverstaendigenAuftragSach"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SatzartZurFreienVerfuegung"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Schadenhergang"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Schadenmanagement"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Schadenverzeichnis"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Stornierung"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SVErgebnisSach"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Unfall"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}UnfallSpezifischeSchadendaten"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VermittlungsauftragWerkstatt"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Versichereranfrage"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Versichererermittlung"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Vertragsmerkmale"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VertragsmerkmaleSach"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Vorsatz"/>
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Wiederbeschaffung-RWB"/>
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
    "allgemeineSchadendaten",
    "anfrageRWB",
    "angebotRechnungseinzelpositionen",
    "angebotRechnungsrahmendaten",
    "angebotRWB",
    "anhang",
    "beauftragungSV",
    "behebungsauftrag",
    "beschaedigteSache",
    "beschaedigungenFahrzeug",
    "beschreibungLogischeEinheit",
    "bewertung",
    "ergebnisdatenSach",
    "ergebnisPruefung",
    "ermittlungen",
    "fehlerbeschreibung",
    "gebaeudeschaden",
    "haftSpezifischeSchadendaten",
    "kalkulation",
    "kalkulationRechnungSach",
    "kalkulationVergleichbareSache",
    "kfzAusstattung",
    "kfzDaten",
    "kfzGK",
    "kommentar",
    "leckageMessprotokoll",
    "leistungabrechnung",
    "leistungsabrechnungRA",
    "mandatsBeauftragung",
    "nachsatz",
    "panne",
    "partnerdaten",
    "personenschaden",
    "quittungsdaten",
    "raLeistungsabrechnungEinzelposition",
    "rechnungMW",
    "rechnungSV",
    "rechtsschutzMeldung",
    "regress",
    "reparaturfreigabe",
    "reservierungInfoUeberAnmietung",
    "rueckmeldung",
    "sachSchadendatenBlitz",
    "sachSchadendatenDiebstahl",
    "sachSchadendatenFeuer",
    "sachSchadendatenLwElementar",
    "sachSchadendatenTechnisch",
    "sachSchadendatenTransport",
    "sachverstaendigenAuftragSach",
    "satzartZurFreienVerfuegung",
    "schadenhergang",
    "schadenmanagement",
    "schadenverzeichnis",
    "stornierung",
    "svErgebnisSach",
    "unfall",
    "unfallSpezifischeSchadendaten",
    "vermittlungsauftragWerkstatt",
    "versichereranfrage",
    "versichererermittlung",
    "vertragsmerkmale",
    "vertragsmerkmaleSach",
    "vorsatz",
    "wiederbeschaffungRWB"
})
@XmlRootElement(name = "Satzarten")
public class Satzarten {

    @XmlElement(name = "AllgemeineSchadendaten", required = true)
    protected AllgemeineSchadendaten allgemeineSchadendaten;
    @XmlElement(name = "Anfrage-RWB", required = true)
    protected AnfrageRWB anfrageRWB;
    @XmlElement(name = "AngebotRechnungseinzelpositionen", required = true)
    protected AngebotRechnungseinzelpositionen angebotRechnungseinzelpositionen;
    @XmlElement(name = "AngebotRechnungsrahmendaten", required = true)
    protected AngebotRechnungsrahmendaten angebotRechnungsrahmendaten;
    @XmlElement(name = "Angebot-RWB", required = true)
    protected AngebotRWB angebotRWB;
    @XmlElement(name = "Anhang", required = true)
    protected Anhang anhang;
    @XmlElement(name = "Beauftragung-SV", required = true)
    protected BeauftragungSV beauftragungSV;
    @XmlElement(name = "Behebungsauftrag", required = true)
    protected Behebungsauftrag behebungsauftrag;
    @XmlElement(name = "BeschaedigteSache", required = true)
    protected BeschaedigteSache beschaedigteSache;
    @XmlElement(name = "BeschaedigungenFahrzeug", required = true)
    protected BeschaedigungenFahrzeug beschaedigungenFahrzeug;
    @XmlElement(name = "BeschreibungLogischeEinheit", required = true)
    protected BeschreibungLogischeEinheit beschreibungLogischeEinheit;
    @XmlElement(name = "Bewertung", required = true)
    protected Bewertung bewertung;
    @XmlElement(name = "ErgebnisdatenSach", required = true)
    protected ErgebnisdatenSach ergebnisdatenSach;
    @XmlElement(name = "ErgebnisPruefung", required = true)
    protected ErgebnisPruefung ergebnisPruefung;
    @XmlElement(name = "Ermittlungen", required = true)
    protected Ermittlungen ermittlungen;
    @XmlElement(name = "Fehlerbeschreibung", required = true)
    protected Fehlerbeschreibung fehlerbeschreibung;
    @XmlElement(name = "Gebaeudeschaden", required = true)
    protected Gebaeudeschaden gebaeudeschaden;
    @XmlElement(name = "HaftSpezifischeSchadendaten", required = true)
    protected HaftSpezifischeSchadendaten haftSpezifischeSchadendaten;
    @XmlElement(name = "Kalkulation", required = true)
    protected Kalkulation kalkulation;
    @XmlElement(name = "KalkulationRechnungSach", required = true)
    protected KalkulationRechnungSach kalkulationRechnungSach;
    @XmlElement(name = "KalkulationVergleichbareSache", required = true)
    protected KalkulationVergleichbareSache kalkulationVergleichbareSache;
    @XmlElement(name = "Kfz-Ausstattung", required = true)
    protected KfzAusstattung kfzAusstattung;
    @XmlElement(name = "Kfz-Daten", required = true)
    protected KfzDaten kfzDaten;
    @XmlElement(name = "KfzGK", required = true)
    protected KfzGK kfzGK;
    @XmlElement(name = "Kommentar", required = true)
    protected Kommentar kommentar;
    @XmlElement(name = "LeckageMessprotokoll", required = true)
    protected LeckageMessprotokoll leckageMessprotokoll;
    @XmlElement(name = "Leistungabrechnung", required = true)
    protected Leistungabrechnung leistungabrechnung;
    @XmlElement(name = "LeistungsabrechnungRA", required = true)
    protected LeistungsabrechnungRA leistungsabrechnungRA;
    @XmlElement(name = "MandatsBeauftragung", required = true)
    protected MandatsBeauftragung mandatsBeauftragung;
    @XmlElement(name = "Nachsatz", required = true)
    protected Nachsatz nachsatz;
    @XmlElement(name = "Panne", required = true)
    protected Panne panne;
    @XmlElement(name = "Partnerdaten", required = true)
    protected Partnerdaten partnerdaten;
    @XmlElement(name = "Personenschaden", required = true)
    protected Personenschaden personenschaden;
    @XmlElement(name = "Quittungsdaten", required = true)
    protected Quittungsdaten quittungsdaten;
    @XmlElement(name = "RALeistungsabrechnungEinzelposition", required = true)
    protected RALeistungsabrechnungEinzelposition raLeistungsabrechnungEinzelposition;
    @XmlElement(name = "Rechnung-MW", required = true)
    protected RechnungMW rechnungMW;
    @XmlElement(name = "Rechnung-SV", required = true)
    protected RechnungSV rechnungSV;
    @XmlElement(name = "RechtsschutzMeldung", required = true)
    protected RechtsschutzMeldung rechtsschutzMeldung;
    @XmlElement(name = "Regress", required = true)
    protected Regress regress;
    @XmlElement(name = "Reparaturfreigabe", required = true)
    protected Reparaturfreigabe reparaturfreigabe;
    @XmlElement(name = "ReservierungInfoUeberAnmietung", required = true)
    protected ReservierungInfoUeberAnmietung reservierungInfoUeberAnmietung;
    @XmlElement(name = "Rueckmeldung", required = true)
    protected Rueckmeldung rueckmeldung;
    @XmlElement(name = "SachSchadendatenBlitz", required = true)
    protected SachSchadendatenBlitz sachSchadendatenBlitz;
    @XmlElement(name = "SachSchadendatenDiebstahl", required = true)
    protected SachSchadendatenDiebstahl sachSchadendatenDiebstahl;
    @XmlElement(name = "SachSchadendatenFeuer", required = true)
    protected SachSchadendatenFeuer sachSchadendatenFeuer;
    @XmlElement(name = "SachSchadendatenLwElementar", required = true)
    protected SachSchadendatenLwElementar sachSchadendatenLwElementar;
    @XmlElement(name = "SachSchadendatenTechnisch", required = true)
    protected SachSchadendatenTechnisch sachSchadendatenTechnisch;
    @XmlElement(name = "SachSchadendatenTransport", required = true)
    protected SachSchadendatenTransport sachSchadendatenTransport;
    @XmlElement(name = "SachverstaendigenAuftragSach", required = true)
    protected SachverstaendigenAuftragSach sachverstaendigenAuftragSach;
    @XmlElement(name = "SatzartZurFreienVerfuegung", required = true)
    protected SatzartZurFreienVerfuegung satzartZurFreienVerfuegung;
    @XmlElement(name = "Schadenhergang", required = true)
    protected Schadenhergang schadenhergang;
    @XmlElement(name = "Schadenmanagement", required = true)
    protected Schadenmanagement schadenmanagement;
    @XmlElement(name = "Schadenverzeichnis", required = true)
    protected Schadenverzeichnis schadenverzeichnis;
    @XmlElement(name = "Stornierung", required = true)
    protected Stornierung stornierung;
    @XmlElement(name = "SVErgebnisSach", required = true)
    protected SVErgebnisSach svErgebnisSach;
    @XmlElement(name = "Unfall", required = true)
    protected Unfall unfall;
    @XmlElement(name = "UnfallSpezifischeSchadendaten", required = true)
    protected UnfallSpezifischeSchadendaten unfallSpezifischeSchadendaten;
    @XmlElement(name = "VermittlungsauftragWerkstatt", required = true)
    protected VermittlungsauftragWerkstatt vermittlungsauftragWerkstatt;
    @XmlElement(name = "Versichereranfrage", required = true)
    protected Versichereranfrage versichereranfrage;
    @XmlElement(name = "Versichererermittlung", required = true)
    protected Versichererermittlung versichererermittlung;
    @XmlElement(name = "Vertragsmerkmale", required = true)
    protected Vertragsmerkmale vertragsmerkmale;
    @XmlElement(name = "VertragsmerkmaleSach", required = true)
    protected VertragsmerkmaleSach vertragsmerkmaleSach;
    @XmlElement(name = "Vorsatz", required = true)
    protected Vorsatz vorsatz;
    @XmlElement(name = "Wiederbeschaffung-RWB", required = true)
    protected WiederbeschaffungRWB wiederbeschaffungRWB;

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
     * Ruft den Wert der anfrageRWB-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AnfrageRWB }
     *     
     */
    public AnfrageRWB getAnfrageRWB() {
        return anfrageRWB;
    }

    /**
     * Legt den Wert der anfrageRWB-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AnfrageRWB }
     *     
     */
    public void setAnfrageRWB(AnfrageRWB value) {
        this.anfrageRWB = value;
    }

    /**
     * Ruft den Wert der angebotRechnungseinzelpositionen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AngebotRechnungseinzelpositionen }
     *     
     */
    public AngebotRechnungseinzelpositionen getAngebotRechnungseinzelpositionen() {
        return angebotRechnungseinzelpositionen;
    }

    /**
     * Legt den Wert der angebotRechnungseinzelpositionen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AngebotRechnungseinzelpositionen }
     *     
     */
    public void setAngebotRechnungseinzelpositionen(AngebotRechnungseinzelpositionen value) {
        this.angebotRechnungseinzelpositionen = value;
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
     * Ruft den Wert der angebotRWB-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AngebotRWB }
     *     
     */
    public AngebotRWB getAngebotRWB() {
        return angebotRWB;
    }

    /**
     * Legt den Wert der angebotRWB-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AngebotRWB }
     *     
     */
    public void setAngebotRWB(AngebotRWB value) {
        this.angebotRWB = value;
    }

    /**
     * Ruft den Wert der anhang-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Anhang }
     *     
     */
    public Anhang getAnhang() {
        return anhang;
    }

    /**
     * Legt den Wert der anhang-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Anhang }
     *     
     */
    public void setAnhang(Anhang value) {
        this.anhang = value;
    }

    /**
     * Ruft den Wert der beauftragungSV-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BeauftragungSV }
     *     
     */
    public BeauftragungSV getBeauftragungSV() {
        return beauftragungSV;
    }

    /**
     * Legt den Wert der beauftragungSV-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BeauftragungSV }
     *     
     */
    public void setBeauftragungSV(BeauftragungSV value) {
        this.beauftragungSV = value;
    }

    /**
     * Ruft den Wert der behebungsauftrag-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Behebungsauftrag }
     *     
     */
    public Behebungsauftrag getBehebungsauftrag() {
        return behebungsauftrag;
    }

    /**
     * Legt den Wert der behebungsauftrag-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Behebungsauftrag }
     *     
     */
    public void setBehebungsauftrag(Behebungsauftrag value) {
        this.behebungsauftrag = value;
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
     * Ruft den Wert der bewertung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Bewertung }
     *     
     */
    public Bewertung getBewertung() {
        return bewertung;
    }

    /**
     * Legt den Wert der bewertung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Bewertung }
     *     
     */
    public void setBewertung(Bewertung value) {
        this.bewertung = value;
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
     * Ruft den Wert der fehlerbeschreibung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Fehlerbeschreibung }
     *     
     */
    public Fehlerbeschreibung getFehlerbeschreibung() {
        return fehlerbeschreibung;
    }

    /**
     * Legt den Wert der fehlerbeschreibung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Fehlerbeschreibung }
     *     
     */
    public void setFehlerbeschreibung(Fehlerbeschreibung value) {
        this.fehlerbeschreibung = value;
    }

    /**
     * Ruft den Wert der gebaeudeschaden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Gebaeudeschaden }
     *     
     */
    public Gebaeudeschaden getGebaeudeschaden() {
        return gebaeudeschaden;
    }

    /**
     * Legt den Wert der gebaeudeschaden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Gebaeudeschaden }
     *     
     */
    public void setGebaeudeschaden(Gebaeudeschaden value) {
        this.gebaeudeschaden = value;
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
     * Ruft den Wert der kalkulationRechnungSach-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach }
     *     
     */
    public KalkulationRechnungSach getKalkulationRechnungSach() {
        return kalkulationRechnungSach;
    }

    /**
     * Legt den Wert der kalkulationRechnungSach-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach }
     *     
     */
    public void setKalkulationRechnungSach(KalkulationRechnungSach value) {
        this.kalkulationRechnungSach = value;
    }

    /**
     * Ruft den Wert der kalkulationVergleichbareSache-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationVergleichbareSache }
     *     
     */
    public KalkulationVergleichbareSache getKalkulationVergleichbareSache() {
        return kalkulationVergleichbareSache;
    }

    /**
     * Legt den Wert der kalkulationVergleichbareSache-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationVergleichbareSache }
     *     
     */
    public void setKalkulationVergleichbareSache(KalkulationVergleichbareSache value) {
        this.kalkulationVergleichbareSache = value;
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
     * Ruft den Wert der kfzGK-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzGK }
     *     
     */
    public KfzGK getKfzGK() {
        return kfzGK;
    }

    /**
     * Legt den Wert der kfzGK-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzGK }
     *     
     */
    public void setKfzGK(KfzGK value) {
        this.kfzGK = value;
    }

    /**
     * Ruft den Wert der kommentar-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Kommentar }
     *     
     */
    public Kommentar getKommentar() {
        return kommentar;
    }

    /**
     * Legt den Wert der kommentar-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Kommentar }
     *     
     */
    public void setKommentar(Kommentar value) {
        this.kommentar = value;
    }

    /**
     * Ruft den Wert der leckageMessprotokoll-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LeckageMessprotokoll }
     *     
     */
    public LeckageMessprotokoll getLeckageMessprotokoll() {
        return leckageMessprotokoll;
    }

    /**
     * Legt den Wert der leckageMessprotokoll-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LeckageMessprotokoll }
     *     
     */
    public void setLeckageMessprotokoll(LeckageMessprotokoll value) {
        this.leckageMessprotokoll = value;
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
     * Ruft den Wert der leistungsabrechnungRA-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LeistungsabrechnungRA }
     *     
     */
    public LeistungsabrechnungRA getLeistungsabrechnungRA() {
        return leistungsabrechnungRA;
    }

    /**
     * Legt den Wert der leistungsabrechnungRA-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LeistungsabrechnungRA }
     *     
     */
    public void setLeistungsabrechnungRA(LeistungsabrechnungRA value) {
        this.leistungsabrechnungRA = value;
    }

    /**
     * Ruft den Wert der mandatsBeauftragung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MandatsBeauftragung }
     *     
     */
    public MandatsBeauftragung getMandatsBeauftragung() {
        return mandatsBeauftragung;
    }

    /**
     * Legt den Wert der mandatsBeauftragung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MandatsBeauftragung }
     *     
     */
    public void setMandatsBeauftragung(MandatsBeauftragung value) {
        this.mandatsBeauftragung = value;
    }

    /**
     * Ruft den Wert der nachsatz-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Nachsatz }
     *     
     */
    public Nachsatz getNachsatz() {
        return nachsatz;
    }

    /**
     * Legt den Wert der nachsatz-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Nachsatz }
     *     
     */
    public void setNachsatz(Nachsatz value) {
        this.nachsatz = value;
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
     * Ruft den Wert der quittungsdaten-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Quittungsdaten }
     *     
     */
    public Quittungsdaten getQuittungsdaten() {
        return quittungsdaten;
    }

    /**
     * Legt den Wert der quittungsdaten-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Quittungsdaten }
     *     
     */
    public void setQuittungsdaten(Quittungsdaten value) {
        this.quittungsdaten = value;
    }

    /**
     * Ruft den Wert der raLeistungsabrechnungEinzelposition-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RALeistungsabrechnungEinzelposition }
     *     
     */
    public RALeistungsabrechnungEinzelposition getRALeistungsabrechnungEinzelposition() {
        return raLeistungsabrechnungEinzelposition;
    }

    /**
     * Legt den Wert der raLeistungsabrechnungEinzelposition-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RALeistungsabrechnungEinzelposition }
     *     
     */
    public void setRALeistungsabrechnungEinzelposition(RALeistungsabrechnungEinzelposition value) {
        this.raLeistungsabrechnungEinzelposition = value;
    }

    /**
     * Ruft den Wert der rechnungMW-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RechnungMW }
     *     
     */
    public RechnungMW getRechnungMW() {
        return rechnungMW;
    }

    /**
     * Legt den Wert der rechnungMW-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RechnungMW }
     *     
     */
    public void setRechnungMW(RechnungMW value) {
        this.rechnungMW = value;
    }

    /**
     * Ruft den Wert der rechnungSV-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RechnungSV }
     *     
     */
    public RechnungSV getRechnungSV() {
        return rechnungSV;
    }

    /**
     * Legt den Wert der rechnungSV-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RechnungSV }
     *     
     */
    public void setRechnungSV(RechnungSV value) {
        this.rechnungSV = value;
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
     * Ruft den Wert der regress-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Regress }
     *     
     */
    public Regress getRegress() {
        return regress;
    }

    /**
     * Legt den Wert der regress-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Regress }
     *     
     */
    public void setRegress(Regress value) {
        this.regress = value;
    }

    /**
     * Ruft den Wert der reparaturfreigabe-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Reparaturfreigabe }
     *     
     */
    public Reparaturfreigabe getReparaturfreigabe() {
        return reparaturfreigabe;
    }

    /**
     * Legt den Wert der reparaturfreigabe-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Reparaturfreigabe }
     *     
     */
    public void setReparaturfreigabe(Reparaturfreigabe value) {
        this.reparaturfreigabe = value;
    }

    /**
     * Ruft den Wert der reservierungInfoUeberAnmietung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ReservierungInfoUeberAnmietung }
     *     
     */
    public ReservierungInfoUeberAnmietung getReservierungInfoUeberAnmietung() {
        return reservierungInfoUeberAnmietung;
    }

    /**
     * Legt den Wert der reservierungInfoUeberAnmietung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ReservierungInfoUeberAnmietung }
     *     
     */
    public void setReservierungInfoUeberAnmietung(ReservierungInfoUeberAnmietung value) {
        this.reservierungInfoUeberAnmietung = value;
    }

    /**
     * Ruft den Wert der rueckmeldung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Rueckmeldung }
     *     
     */
    public Rueckmeldung getRueckmeldung() {
        return rueckmeldung;
    }

    /**
     * Legt den Wert der rueckmeldung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Rueckmeldung }
     *     
     */
    public void setRueckmeldung(Rueckmeldung value) {
        this.rueckmeldung = value;
    }

    /**
     * Ruft den Wert der sachSchadendatenBlitz-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenBlitz }
     *     
     */
    public SachSchadendatenBlitz getSachSchadendatenBlitz() {
        return sachSchadendatenBlitz;
    }

    /**
     * Legt den Wert der sachSchadendatenBlitz-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenBlitz }
     *     
     */
    public void setSachSchadendatenBlitz(SachSchadendatenBlitz value) {
        this.sachSchadendatenBlitz = value;
    }

    /**
     * Ruft den Wert der sachSchadendatenDiebstahl-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenDiebstahl }
     *     
     */
    public SachSchadendatenDiebstahl getSachSchadendatenDiebstahl() {
        return sachSchadendatenDiebstahl;
    }

    /**
     * Legt den Wert der sachSchadendatenDiebstahl-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenDiebstahl }
     *     
     */
    public void setSachSchadendatenDiebstahl(SachSchadendatenDiebstahl value) {
        this.sachSchadendatenDiebstahl = value;
    }

    /**
     * Ruft den Wert der sachSchadendatenFeuer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenFeuer }
     *     
     */
    public SachSchadendatenFeuer getSachSchadendatenFeuer() {
        return sachSchadendatenFeuer;
    }

    /**
     * Legt den Wert der sachSchadendatenFeuer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenFeuer }
     *     
     */
    public void setSachSchadendatenFeuer(SachSchadendatenFeuer value) {
        this.sachSchadendatenFeuer = value;
    }

    /**
     * Ruft den Wert der sachSchadendatenLwElementar-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenLwElementar }
     *     
     */
    public SachSchadendatenLwElementar getSachSchadendatenLwElementar() {
        return sachSchadendatenLwElementar;
    }

    /**
     * Legt den Wert der sachSchadendatenLwElementar-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenLwElementar }
     *     
     */
    public void setSachSchadendatenLwElementar(SachSchadendatenLwElementar value) {
        this.sachSchadendatenLwElementar = value;
    }

    /**
     * Ruft den Wert der sachSchadendatenTechnisch-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenTechnisch }
     *     
     */
    public SachSchadendatenTechnisch getSachSchadendatenTechnisch() {
        return sachSchadendatenTechnisch;
    }

    /**
     * Legt den Wert der sachSchadendatenTechnisch-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenTechnisch }
     *     
     */
    public void setSachSchadendatenTechnisch(SachSchadendatenTechnisch value) {
        this.sachSchadendatenTechnisch = value;
    }

    /**
     * Ruft den Wert der sachSchadendatenTransport-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenTransport }
     *     
     */
    public SachSchadendatenTransport getSachSchadendatenTransport() {
        return sachSchadendatenTransport;
    }

    /**
     * Legt den Wert der sachSchadendatenTransport-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenTransport }
     *     
     */
    public void setSachSchadendatenTransport(SachSchadendatenTransport value) {
        this.sachSchadendatenTransport = value;
    }

    /**
     * Ruft den Wert der sachverstaendigenAuftragSach-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachverstaendigenAuftragSach }
     *     
     */
    public SachverstaendigenAuftragSach getSachverstaendigenAuftragSach() {
        return sachverstaendigenAuftragSach;
    }

    /**
     * Legt den Wert der sachverstaendigenAuftragSach-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachverstaendigenAuftragSach }
     *     
     */
    public void setSachverstaendigenAuftragSach(SachverstaendigenAuftragSach value) {
        this.sachverstaendigenAuftragSach = value;
    }

    /**
     * Ruft den Wert der satzartZurFreienVerfuegung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SatzartZurFreienVerfuegung }
     *     
     */
    public SatzartZurFreienVerfuegung getSatzartZurFreienVerfuegung() {
        return satzartZurFreienVerfuegung;
    }

    /**
     * Legt den Wert der satzartZurFreienVerfuegung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SatzartZurFreienVerfuegung }
     *     
     */
    public void setSatzartZurFreienVerfuegung(SatzartZurFreienVerfuegung value) {
        this.satzartZurFreienVerfuegung = value;
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
     * Ruft den Wert der schadenverzeichnis-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Schadenverzeichnis }
     *     
     */
    public Schadenverzeichnis getSchadenverzeichnis() {
        return schadenverzeichnis;
    }

    /**
     * Legt den Wert der schadenverzeichnis-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Schadenverzeichnis }
     *     
     */
    public void setSchadenverzeichnis(Schadenverzeichnis value) {
        this.schadenverzeichnis = value;
    }

    /**
     * Ruft den Wert der stornierung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Stornierung }
     *     
     */
    public Stornierung getStornierung() {
        return stornierung;
    }

    /**
     * Legt den Wert der stornierung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Stornierung }
     *     
     */
    public void setStornierung(Stornierung value) {
        this.stornierung = value;
    }

    /**
     * Ruft den Wert der svErgebnisSach-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SVErgebnisSach }
     *     
     */
    public SVErgebnisSach getSVErgebnisSach() {
        return svErgebnisSach;
    }

    /**
     * Legt den Wert der svErgebnisSach-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SVErgebnisSach }
     *     
     */
    public void setSVErgebnisSach(SVErgebnisSach value) {
        this.svErgebnisSach = value;
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
     * Ruft den Wert der vermittlungsauftragWerkstatt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VermittlungsauftragWerkstatt }
     *     
     */
    public VermittlungsauftragWerkstatt getVermittlungsauftragWerkstatt() {
        return vermittlungsauftragWerkstatt;
    }

    /**
     * Legt den Wert der vermittlungsauftragWerkstatt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VermittlungsauftragWerkstatt }
     *     
     */
    public void setVermittlungsauftragWerkstatt(VermittlungsauftragWerkstatt value) {
        this.vermittlungsauftragWerkstatt = value;
    }

    /**
     * Ruft den Wert der versichereranfrage-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Versichereranfrage }
     *     
     */
    public Versichereranfrage getVersichereranfrage() {
        return versichereranfrage;
    }

    /**
     * Legt den Wert der versichereranfrage-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Versichereranfrage }
     *     
     */
    public void setVersichereranfrage(Versichereranfrage value) {
        this.versichereranfrage = value;
    }

    /**
     * Ruft den Wert der versichererermittlung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Versichererermittlung }
     *     
     */
    public Versichererermittlung getVersichererermittlung() {
        return versichererermittlung;
    }

    /**
     * Legt den Wert der versichererermittlung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Versichererermittlung }
     *     
     */
    public void setVersichererermittlung(Versichererermittlung value) {
        this.versichererermittlung = value;
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
     * Ruft den Wert der vorsatz-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Vorsatz }
     *     
     */
    public Vorsatz getVorsatz() {
        return vorsatz;
    }

    /**
     * Legt den Wert der vorsatz-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Vorsatz }
     *     
     */
    public void setVorsatz(Vorsatz value) {
        this.vorsatz = value;
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

}
