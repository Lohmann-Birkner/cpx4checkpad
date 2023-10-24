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
 *         &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Vorsatz"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AuftragWerkstattvermittlung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Behebungsbeauftragung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fahrzeugbewertung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fehlermeldung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}individuelleLE"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Kalkulation-Rechnung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LeckageAbgabeMessprotokoll"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Leistungsabrechnung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Mandatserteilung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Mietwagenrechnung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Mietwagenreservierung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PanneAnderesVU"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PanneZurKenntnis"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Pruefergebnis"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Quittung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}RALeistungsabrechnung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Rechnung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}RechtsschutzSchadenmeldung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}RegressDtRentenversicherung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Reparaturfreigabe-Kostenuebernahme"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Restwertboerseanfrage"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Restwertboerseangebot"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Restwertboersewiederbeschaffungsangebot"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Rueckfrage"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}RueckmeldungAnRechtsanwalt"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachAngebotRechnung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachKalkulationRechnung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachSVBeauftragung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachSVErgebnis"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Sachverstaendigenbeauftragung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Sachverstaendigenbericht"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Sachverstaendigenrechnung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SachZwischenbericht"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Schadenmeldung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}StornierungAuftrag"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}UnfallPanneErstmeldung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}UnfallPanneFolgemeldung"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ZA-AnfrageAntwort"/>
 *           &lt;element ref="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ZA-Versichereranfrage"/>
 *         &lt;/choice>
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
    "vorsatz",
    "auftragWerkstattvermittlungOrBehebungsbeauftragungOrFahrzeugbewertung"
})
@XmlRootElement(name = "GDV")
public class GDV {

    @XmlElement(name = "Vorsatz", required = true)
    protected Vorsatz vorsatz;
    @XmlElements({
        @XmlElement(name = "AuftragWerkstattvermittlung", type = AuftragWerkstattvermittlung.class),
        @XmlElement(name = "Behebungsbeauftragung", type = Behebungsbeauftragung.class),
        @XmlElement(name = "Fahrzeugbewertung", type = Fahrzeugbewertung.class),
        @XmlElement(name = "Fehlermeldung", type = Fehlermeldung.class),
        @XmlElement(name = "individuelleLE", type = IndividuelleLE.class),
        @XmlElement(name = "Kalkulation-Rechnung", type = KalkulationRechnung.class),
        @XmlElement(name = "LeckageAbgabeMessprotokoll", type = LeckageAbgabeMessprotokoll.class),
        @XmlElement(name = "Leistungsabrechnung", type = Leistungsabrechnung.class),
        @XmlElement(name = "Mandatserteilung", type = Mandatserteilung.class),
        @XmlElement(name = "Mietwagenrechnung", type = Mietwagenrechnung.class),
        @XmlElement(name = "Mietwagenreservierung", type = Mietwagenreservierung.class),
        @XmlElement(name = "PanneAnderesVU", type = PanneAnderesVU.class),
        @XmlElement(name = "PanneZurKenntnis", type = PanneZurKenntnis.class),
        @XmlElement(name = "Pruefergebnis", type = Pruefergebnis.class),
        @XmlElement(name = "Quittung", type = Quittung.class),
        @XmlElement(name = "RALeistungsabrechnung", type = RALeistungsabrechnung.class),
        @XmlElement(name = "Rechnung", type = Rechnung.class),
        @XmlElement(name = "RechtsschutzSchadenmeldung", type = RechtsschutzSchadenmeldung.class),
        @XmlElement(name = "RegressDtRentenversicherung", type = RegressDtRentenversicherung.class),
        @XmlElement(name = "Reparaturfreigabe-Kostenuebernahme", type = ReparaturfreigabeKostenuebernahme.class),
        @XmlElement(name = "Restwertboerseanfrage", type = Restwertboerseanfrage.class),
        @XmlElement(name = "Restwertboerseangebot", type = Restwertboerseangebot.class),
        @XmlElement(name = "Restwertboersewiederbeschaffungsangebot", type = Restwertboersewiederbeschaffungsangebot.class),
        @XmlElement(name = "Rueckfrage", type = Rueckfrage.class),
        @XmlElement(name = "RueckmeldungAnRechtsanwalt", type = RueckmeldungAnRechtsanwalt.class),
        @XmlElement(name = "SachAngebotRechnung", type = SachAngebotRechnung.class),
        @XmlElement(name = "SachKalkulationRechnung", type = SachKalkulationRechnung.class),
        @XmlElement(name = "SachSVBeauftragung", type = SachSVBeauftragung.class),
        @XmlElement(name = "SachSVErgebnis", type = SachSVErgebnis.class),
        @XmlElement(name = "Sachverstaendigenbeauftragung", type = Sachverstaendigenbeauftragung.class),
        @XmlElement(name = "Sachverstaendigenbericht", type = Sachverstaendigenbericht.class),
        @XmlElement(name = "Sachverstaendigenrechnung", type = Sachverstaendigenrechnung.class),
        @XmlElement(name = "SachZwischenbericht", type = SachZwischenbericht.class),
        @XmlElement(name = "Schadenmeldung", type = Schadenmeldung.class),
        @XmlElement(name = "StornierungAuftrag", type = StornierungAuftrag.class),
        @XmlElement(name = "UnfallPanneErstmeldung", type = UnfallPanneErstmeldung.class),
        @XmlElement(name = "UnfallPanneFolgemeldung", type = UnfallPanneFolgemeldung.class),
        @XmlElement(name = "ZA-AnfrageAntwort", type = ZAAnfrageAntwort.class),
        @XmlElement(name = "ZA-Versichereranfrage", type = ZAVersichereranfrage.class)
    })
    protected List<Object> auftragWerkstattvermittlungOrBehebungsbeauftragungOrFahrzeugbewertung;

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
     * Gets the value of the auftragWerkstattvermittlungOrBehebungsbeauftragungOrFahrzeugbewertung property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the auftragWerkstattvermittlungOrBehebungsbeauftragungOrFahrzeugbewertung property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuftragWerkstattvermittlungOrBehebungsbeauftragungOrFahrzeugbewertung().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AuftragWerkstattvermittlung }
     * {@link Behebungsbeauftragung }
     * {@link Fahrzeugbewertung }
     * {@link Fehlermeldung }
     * {@link IndividuelleLE }
     * {@link KalkulationRechnung }
     * {@link LeckageAbgabeMessprotokoll }
     * {@link Leistungsabrechnung }
     * {@link Mandatserteilung }
     * {@link Mietwagenrechnung }
     * {@link Mietwagenreservierung }
     * {@link PanneAnderesVU }
     * {@link PanneZurKenntnis }
     * {@link Pruefergebnis }
     * {@link Quittung }
     * {@link RALeistungsabrechnung }
     * {@link Rechnung }
     * {@link RechtsschutzSchadenmeldung }
     * {@link RegressDtRentenversicherung }
     * {@link ReparaturfreigabeKostenuebernahme }
     * {@link Restwertboerseanfrage }
     * {@link Restwertboerseangebot }
     * {@link Restwertboersewiederbeschaffungsangebot }
     * {@link Rueckfrage }
     * {@link RueckmeldungAnRechtsanwalt }
     * {@link SachAngebotRechnung }
     * {@link SachKalkulationRechnung }
     * {@link SachSVBeauftragung }
     * {@link SachSVErgebnis }
     * {@link Sachverstaendigenbeauftragung }
     * {@link Sachverstaendigenbericht }
     * {@link Sachverstaendigenrechnung }
     * {@link SachZwischenbericht }
     * {@link Schadenmeldung }
     * {@link StornierungAuftrag }
     * {@link UnfallPanneErstmeldung }
     * {@link UnfallPanneFolgemeldung }
     * {@link ZAAnfrageAntwort }
     * {@link ZAVersichereranfrage }
     * 
     * 
     */
    public List<Object> getAuftragWerkstattvermittlungOrBehebungsbeauftragungOrFahrzeugbewertung() {
        if (auftragWerkstattvermittlungOrBehebungsbeauftragungOrFahrzeugbewertung == null) {
            auftragWerkstattvermittlungOrBehebungsbeauftragungOrFahrzeugbewertung = new ArrayList<Object>();
        }
        return this.auftragWerkstattvermittlungOrBehebungsbeauftragungOrFahrzeugbewertung;
    }

}
