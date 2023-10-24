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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * String Typ zur Satzendemarkierung
 * 			
 * 
 * <p>Java-Klasse für SatzendeTyp complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="SatzendeTyp">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SatzendeTyp", propOrder = {
    "value"
})
@XmlSeeAlso({
    de.lb.cpx.gdv.messages.AllgemeineSchadendaten.Schadenmeldedatum.class,
    de.lb.cpx.gdv.messages.Kalkulation.FreieVerwendung.BeschreibungFreieVerwendung.class,
    de.lb.cpx.gdv.messages.Schadenmanagement.Vereinbarungen.class,
    de.lb.cpx.gdv.messages.Schadenverzeichnis.BezeichnungBeschaedigteSache.class,
    de.lb.cpx.gdv.messages.Gebaeudeschaden.Objektreferenzen.BezeichnungObjektebene3 .class,
    de.lb.cpx.gdv.messages.Gebaeudeschaden.BezeichnungLeistung.class,
    de.lb.cpx.gdv.messages.RechtsschutzMeldung.Gegner.class,
    de.lb.cpx.gdv.messages.SachSchadendatenBlitz.BeschreibungBlitzeinschlagstelle.class,
    de.lb.cpx.gdv.messages.SachSchadendatenDiebstahl.BeschreibungAnfertigerNachschluessel.class,
    de.lb.cpx.gdv.messages.SachSchadendatenTransport.BeschreibungWannDerFruehereTransportschadenEntstandenIst.class,
    de.lb.cpx.gdv.messages.Versichererermittlung.Agent.class,
    de.lb.cpx.gdv.messages.LeckageMessprotokoll.UrsacheFuerMehrfachortung.class,
    de.lb.cpx.gdv.messages.Schadenhergang.Schilderung2 .class,
    de.lb.cpx.gdv.messages.Vorsatz.VSNR.class,
    de.lb.cpx.gdv.messages.VermittlungsauftragWerkstatt.Auftraege.WunschuhrzeitAbholung.class,
    de.lb.cpx.gdv.messages.Bewertung.DatumLetzteZulassung.class,
    de.lb.cpx.gdv.messages.Fehlerbeschreibung.Beschreibung.class,
    de.lb.cpx.gdv.messages.SatzartZurFreienVerfuegung.BeliebigerInhalt.class,
    de.lb.cpx.gdv.messages.MandatsBeauftragung.Ermittlungsaktenanforderung.Aktenanforderungsgrund.class,
    de.lb.cpx.gdv.messages.MandatsBeauftragung.Anmerkungen.class,
    de.lb.cpx.gdv.messages.RechnungMW.Sonderausstattung.Freitext.class,
    de.lb.cpx.gdv.messages.Panne.VeranlassteMassnahmen.class,
    de.lb.cpx.gdv.messages.Panne.StandortFahrzeugs1 .class,
    de.lb.cpx.gdv.messages.Panne.StandortFahrzeugs2 .class,
    de.lb.cpx.gdv.messages.Unfall.KurzangabenZumUnfall.class,
    de.lb.cpx.gdv.messages.Unfall.StandortFahrzeugs1 .class,
    de.lb.cpx.gdv.messages.Unfall.StandortFahrzeugs2 .class,
    de.lb.cpx.gdv.messages.ErgebnisPruefung.PruefergebnisKommentar.class,
    de.lb.cpx.gdv.messages.KalkulationRechnungSach.Begruendung.class,
    de.lb.cpx.gdv.messages.KalkulationRechnungSach.Begruendung2 .class,
    de.lb.cpx.gdv.messages.KalkulationRechnungSach.BesondereAusstattungBeschaedigtenSache1 .class,
    de.lb.cpx.gdv.messages.KalkulationRechnungSach.BesondereAusstattungBeschaedigtenSache2 .class,
    de.lb.cpx.gdv.messages.KalkulationVergleichbareSache.Merkmal2 .class,
    de.lb.cpx.gdv.messages.KalkulationVergleichbareSache.Merkmal9 .class,
    de.lb.cpx.gdv.messages.KalkulationVergleichbareSache.Ausstattungsunterschiede.class,
    de.lb.cpx.gdv.messages.KalkulationVergleichbareSache.Ausstattungsunterschiede2 .class,
    de.lb.cpx.gdv.messages.Quittungsdaten.Sendeuhrzeit.class,
    de.lb.cpx.gdv.messages.LeistungsabrechnungRA.Rechnungsnummer.class,
    de.lb.cpx.gdv.messages.Regress.Korrespondenztyp.class,
    de.lb.cpx.gdv.messages.WiederbeschaffungRWB.URLzumFahrzeug.class,
    de.lb.cpx.gdv.messages.AngebotRechnungsrahmendaten.Zahlungsziel.class,
    de.lb.cpx.gdv.messages.BeauftragungSV.Wunschtermin.class,
    de.lb.cpx.gdv.messages.Stornierung.BeschreibungGrund.class,
    de.lb.cpx.gdv.messages.Stornierung.Auftragsreferenz.class,
    de.lb.cpx.gdv.messages.Versichereranfrage.Passwort.class,
    de.lb.cpx.gdv.messages.KfzAusstattung.Beschreibung1 .class,
    de.lb.cpx.gdv.messages.KfzAusstattung.Beschreibung2 .class,
    de.lb.cpx.gdv.messages.KfzAusstattung.Beschreibung3 .class,
    de.lb.cpx.gdv.messages.BeschaedigteSache.IDNummer.class,
    de.lb.cpx.gdv.messages.BeschaedigteSache.Merkmal6 .class,
    de.lb.cpx.gdv.messages.BeschaedigteSache.Wertverbesserungsart.class,
    de.lb.cpx.gdv.messages.BeschaedigteSache.Umfang.class,
    de.lb.cpx.gdv.messages.BeschaedigteSache.MieteLeiheBis.class,
    de.lb.cpx.gdv.messages.Partnerdaten.Abteilung.class,
    de.lb.cpx.gdv.messages.Partnerdaten.Hausnummer.class,
    de.lb.cpx.gdv.messages.Partnerdaten.Beruf.class,
    de.lb.cpx.gdv.messages.Personenschaden.BehandelnderArzt.class,
    de.lb.cpx.gdv.messages.Kommentar.Kommentar1 .class,
    de.lb.cpx.gdv.messages.Kommentar.Kommentar2 .class,
    de.lb.cpx.gdv.messages.KfzGK.AnschriftenIDHalters.class,
    de.lb.cpx.gdv.messages.VertragsmerkmaleSach.Versicherungsort.Strasse.class
})
public class SatzendeTyp {

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
