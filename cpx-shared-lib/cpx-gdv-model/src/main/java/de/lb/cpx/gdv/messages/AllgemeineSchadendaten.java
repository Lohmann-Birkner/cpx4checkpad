//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.19 um 11:55:14 AM CEST 
//


package de.lb.cpx.gdv.messages;

import java.math.BigDecimal;
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
 *         &lt;element name="Schaden-NrVM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Schadenort" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="LKZ" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LaenderkennzeichenTyp" minOccurs="0"/>
 *                   &lt;element name="PLZ" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="6"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Ort" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="25"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Strasse" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="BeschreibungSchadenort" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="80"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Schadendatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Schadenuhrzeit" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *               &lt;minLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Schadenmeldedatum" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Parallelschaden" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Meldung-Polizei" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Schadenverursacher" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SchadenverursacherTyp" minOccurs="0"/>
 *         &lt;element name="AnzahlVerletzte" minOccurs="0">
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
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="AnzahlTote" minOccurs="0">
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
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="VN-verstorben" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Schadensparte" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Sparte1Typ" minOccurs="0"/>
 *         &lt;element name="Personenschaden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Schadenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SchadenartTyp" minOccurs="0"/>
 *         &lt;element name="Schadenabwicklung" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="SchadenersatzanspruecheErhoben" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SchadenersatzTyp" minOccurs="0"/>
 *                   &lt;element name="VNZahlungEinvestanden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="BegruendungZahlung" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="RAEingeschaltet" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="AnderweitigerVersicherungsschutz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AnderweitigerSchutzTyp" minOccurs="0"/>
 *         &lt;element name="VersSchutzText" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="70"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SchadenAnderweitigAngezeigt" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="EigentuemerWohnung" minOccurs="0">
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
 *         &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp"/>
 *         &lt;element name="ProzentsatzMWSt">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="999.99"/>
 *                         &lt;totalDigits value="5"/>
 *                         &lt;fractionDigits value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="geschaetzeRepkostenInWE" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess112Typ" minOccurs="0"/>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="PreisangabeReparaturkostenDurch" minOccurs="0">
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
 *         &lt;element name="Reinigungskosten" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess112Typ" minOccurs="0"/>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="PreisangabeDerReinigungskostenDurch" minOccurs="0">
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
 *         &lt;element name="Wiederbeschaffungswert" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess112Typ" minOccurs="0"/>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="PreisangabeWiederbeschaffungswertDurch" minOccurs="0">
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
 *         &lt;element name="SchadenmeldungDurch" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AdressKennzeichen1" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
 *                   &lt;element name="PartnerReferenz1" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="KontaktUeber" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>KontaktartTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4200">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="4"/>
 *             &lt;minLength value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Versionsnummer" fixed="004">
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
    "schadenNrVM",
    "schadenort",
    "schadendatum",
    "schadenuhrzeit",
    "schadenmeldedatum",
    "parallelschaden",
    "meldungPolizei",
    "schadenverursacher",
    "anzahlVerletzte",
    "anzahlTote",
    "vnVerstorben",
    "schadensparte",
    "personenschaden",
    "schadenart",
    "schadenabwicklung",
    "anderweitigerVersicherungsschutz",
    "versSchutzText",
    "schadenAnderweitigAngezeigt",
    "eigentuemerWohnung",
    "waehrungsschluessel",
    "prozentsatzMWSt",
    "geschaetzeRepkostenInWE",
    "preisangabeReparaturkostenDurch",
    "reinigungskosten",
    "preisangabeDerReinigungskostenDurch",
    "wiederbeschaffungswert",
    "preisangabeWiederbeschaffungswertDurch",
    "schadenmeldungDurch",
    "kontaktUeber"
})
@XmlRootElement(name = "AllgemeineSchadendaten")
public class AllgemeineSchadendaten {

    @XmlElement(name = "Schaden-NrVM")
    protected String schadenNrVM;
    @XmlElement(name = "Schadenort")
    protected AllgemeineSchadendaten.Schadenort schadenort;
    @XmlElement(name = "Schadendatum")
    protected String schadendatum;
    @XmlElement(name = "Schadenuhrzeit")
    protected String schadenuhrzeit;
    @XmlElement(name = "Schadenmeldedatum")
    protected AllgemeineSchadendaten.Schadenmeldedatum schadenmeldedatum;
    @XmlElement(name = "Parallelschaden")
    protected String parallelschaden;
    @XmlElement(name = "Meldung-Polizei")
    protected String meldungPolizei;
    @XmlElement(name = "Schadenverursacher")
    protected String schadenverursacher;
    @XmlElement(name = "AnzahlVerletzte")
    protected AllgemeineSchadendaten.AnzahlVerletzte anzahlVerletzte;
    @XmlElement(name = "AnzahlTote")
    protected AllgemeineSchadendaten.AnzahlTote anzahlTote;
    @XmlElement(name = "VN-verstorben")
    protected String vnVerstorben;
    @XmlElement(name = "Schadensparte")
    protected String schadensparte;
    @XmlElement(name = "Personenschaden")
    protected String personenschaden;
    @XmlElement(name = "Schadenart")
    protected String schadenart;
    @XmlElement(name = "Schadenabwicklung")
    protected AllgemeineSchadendaten.Schadenabwicklung schadenabwicklung;
    @XmlElement(name = "AnderweitigerVersicherungsschutz")
    protected String anderweitigerVersicherungsschutz;
    @XmlElement(name = "VersSchutzText")
    protected String versSchutzText;
    @XmlElement(name = "SchadenAnderweitigAngezeigt")
    protected AllgemeineSchadendaten.SchadenAnderweitigAngezeigt schadenAnderweitigAngezeigt;
    @XmlElement(name = "EigentuemerWohnung")
    protected AllgemeineSchadendaten.EigentuemerWohnung eigentuemerWohnung;
    @XmlElement(name = "Waehrungsschluessel", required = true)
    protected String waehrungsschluessel;
    @XmlElement(name = "ProzentsatzMWSt", required = true)
    protected AllgemeineSchadendaten.ProzentsatzMWSt prozentsatzMWSt;
    protected AllgemeineSchadendaten.GeschaetzeRepkostenInWE geschaetzeRepkostenInWE;
    @XmlElement(name = "PreisangabeReparaturkostenDurch")
    protected AllgemeineSchadendaten.PreisangabeReparaturkostenDurch preisangabeReparaturkostenDurch;
    @XmlElement(name = "Reinigungskosten")
    protected AllgemeineSchadendaten.Reinigungskosten reinigungskosten;
    @XmlElement(name = "PreisangabeDerReinigungskostenDurch")
    protected AllgemeineSchadendaten.PreisangabeDerReinigungskostenDurch preisangabeDerReinigungskostenDurch;
    @XmlElement(name = "Wiederbeschaffungswert")
    protected AllgemeineSchadendaten.Wiederbeschaffungswert wiederbeschaffungswert;
    @XmlElement(name = "PreisangabeWiederbeschaffungswertDurch")
    protected AllgemeineSchadendaten.PreisangabeWiederbeschaffungswertDurch preisangabeWiederbeschaffungswertDurch;
    @XmlElement(name = "SchadenmeldungDurch")
    protected AllgemeineSchadendaten.SchadenmeldungDurch schadenmeldungDurch;
    @XmlElement(name = "KontaktUeber")
    protected AllgemeineSchadendaten.KontaktUeber kontaktUeber;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der schadenNrVM-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenNrVM() {
        return schadenNrVM;
    }

    /**
     * Legt den Wert der schadenNrVM-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenNrVM(String value) {
        this.schadenNrVM = value;
    }

    /**
     * Ruft den Wert der schadenort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.Schadenort }
     *     
     */
    public AllgemeineSchadendaten.Schadenort getSchadenort() {
        return schadenort;
    }

    /**
     * Legt den Wert der schadenort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.Schadenort }
     *     
     */
    public void setSchadenort(AllgemeineSchadendaten.Schadenort value) {
        this.schadenort = value;
    }

    /**
     * Ruft den Wert der schadendatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadendatum() {
        return schadendatum;
    }

    /**
     * Legt den Wert der schadendatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadendatum(String value) {
        this.schadendatum = value;
    }

    /**
     * Ruft den Wert der schadenuhrzeit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenuhrzeit() {
        return schadenuhrzeit;
    }

    /**
     * Legt den Wert der schadenuhrzeit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenuhrzeit(String value) {
        this.schadenuhrzeit = value;
    }

    /**
     * Ruft den Wert der schadenmeldedatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.Schadenmeldedatum }
     *     
     */
    public AllgemeineSchadendaten.Schadenmeldedatum getSchadenmeldedatum() {
        return schadenmeldedatum;
    }

    /**
     * Legt den Wert der schadenmeldedatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.Schadenmeldedatum }
     *     
     */
    public void setSchadenmeldedatum(AllgemeineSchadendaten.Schadenmeldedatum value) {
        this.schadenmeldedatum = value;
    }

    /**
     * Ruft den Wert der parallelschaden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParallelschaden() {
        return parallelschaden;
    }

    /**
     * Legt den Wert der parallelschaden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParallelschaden(String value) {
        this.parallelschaden = value;
    }

    /**
     * Ruft den Wert der meldungPolizei-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMeldungPolizei() {
        return meldungPolizei;
    }

    /**
     * Legt den Wert der meldungPolizei-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMeldungPolizei(String value) {
        this.meldungPolizei = value;
    }

    /**
     * Ruft den Wert der schadenverursacher-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenverursacher() {
        return schadenverursacher;
    }

    /**
     * Legt den Wert der schadenverursacher-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenverursacher(String value) {
        this.schadenverursacher = value;
    }

    /**
     * Ruft den Wert der anzahlVerletzte-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.AnzahlVerletzte }
     *     
     */
    public AllgemeineSchadendaten.AnzahlVerletzte getAnzahlVerletzte() {
        return anzahlVerletzte;
    }

    /**
     * Legt den Wert der anzahlVerletzte-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.AnzahlVerletzte }
     *     
     */
    public void setAnzahlVerletzte(AllgemeineSchadendaten.AnzahlVerletzte value) {
        this.anzahlVerletzte = value;
    }

    /**
     * Ruft den Wert der anzahlTote-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.AnzahlTote }
     *     
     */
    public AllgemeineSchadendaten.AnzahlTote getAnzahlTote() {
        return anzahlTote;
    }

    /**
     * Legt den Wert der anzahlTote-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.AnzahlTote }
     *     
     */
    public void setAnzahlTote(AllgemeineSchadendaten.AnzahlTote value) {
        this.anzahlTote = value;
    }

    /**
     * Ruft den Wert der vnVerstorben-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNVerstorben() {
        return vnVerstorben;
    }

    /**
     * Legt den Wert der vnVerstorben-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNVerstorben(String value) {
        this.vnVerstorben = value;
    }

    /**
     * Ruft den Wert der schadensparte-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadensparte() {
        return schadensparte;
    }

    /**
     * Legt den Wert der schadensparte-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadensparte(String value) {
        this.schadensparte = value;
    }

    /**
     * Ruft den Wert der personenschaden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonenschaden() {
        return personenschaden;
    }

    /**
     * Legt den Wert der personenschaden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonenschaden(String value) {
        this.personenschaden = value;
    }

    /**
     * Ruft den Wert der schadenart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchadenart() {
        return schadenart;
    }

    /**
     * Legt den Wert der schadenart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchadenart(String value) {
        this.schadenart = value;
    }

    /**
     * Ruft den Wert der schadenabwicklung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.Schadenabwicklung }
     *     
     */
    public AllgemeineSchadendaten.Schadenabwicklung getSchadenabwicklung() {
        return schadenabwicklung;
    }

    /**
     * Legt den Wert der schadenabwicklung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.Schadenabwicklung }
     *     
     */
    public void setSchadenabwicklung(AllgemeineSchadendaten.Schadenabwicklung value) {
        this.schadenabwicklung = value;
    }

    /**
     * Ruft den Wert der anderweitigerVersicherungsschutz-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnderweitigerVersicherungsschutz() {
        return anderweitigerVersicherungsschutz;
    }

    /**
     * Legt den Wert der anderweitigerVersicherungsschutz-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnderweitigerVersicherungsschutz(String value) {
        this.anderweitigerVersicherungsschutz = value;
    }

    /**
     * Ruft den Wert der versSchutzText-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersSchutzText() {
        return versSchutzText;
    }

    /**
     * Legt den Wert der versSchutzText-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersSchutzText(String value) {
        this.versSchutzText = value;
    }

    /**
     * Ruft den Wert der schadenAnderweitigAngezeigt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.SchadenAnderweitigAngezeigt }
     *     
     */
    public AllgemeineSchadendaten.SchadenAnderweitigAngezeigt getSchadenAnderweitigAngezeigt() {
        return schadenAnderweitigAngezeigt;
    }

    /**
     * Legt den Wert der schadenAnderweitigAngezeigt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.SchadenAnderweitigAngezeigt }
     *     
     */
    public void setSchadenAnderweitigAngezeigt(AllgemeineSchadendaten.SchadenAnderweitigAngezeigt value) {
        this.schadenAnderweitigAngezeigt = value;
    }

    /**
     * Ruft den Wert der eigentuemerWohnung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.EigentuemerWohnung }
     *     
     */
    public AllgemeineSchadendaten.EigentuemerWohnung getEigentuemerWohnung() {
        return eigentuemerWohnung;
    }

    /**
     * Legt den Wert der eigentuemerWohnung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.EigentuemerWohnung }
     *     
     */
    public void setEigentuemerWohnung(AllgemeineSchadendaten.EigentuemerWohnung value) {
        this.eigentuemerWohnung = value;
    }

    /**
     * Ruft den Wert der waehrungsschluessel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWaehrungsschluessel() {
        return waehrungsschluessel;
    }

    /**
     * Legt den Wert der waehrungsschluessel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWaehrungsschluessel(String value) {
        this.waehrungsschluessel = value;
    }

    /**
     * Ruft den Wert der prozentsatzMWSt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.ProzentsatzMWSt }
     *     
     */
    public AllgemeineSchadendaten.ProzentsatzMWSt getProzentsatzMWSt() {
        return prozentsatzMWSt;
    }

    /**
     * Legt den Wert der prozentsatzMWSt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.ProzentsatzMWSt }
     *     
     */
    public void setProzentsatzMWSt(AllgemeineSchadendaten.ProzentsatzMWSt value) {
        this.prozentsatzMWSt = value;
    }

    /**
     * Ruft den Wert der geschaetzeRepkostenInWE-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.GeschaetzeRepkostenInWE }
     *     
     */
    public AllgemeineSchadendaten.GeschaetzeRepkostenInWE getGeschaetzeRepkostenInWE() {
        return geschaetzeRepkostenInWE;
    }

    /**
     * Legt den Wert der geschaetzeRepkostenInWE-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.GeschaetzeRepkostenInWE }
     *     
     */
    public void setGeschaetzeRepkostenInWE(AllgemeineSchadendaten.GeschaetzeRepkostenInWE value) {
        this.geschaetzeRepkostenInWE = value;
    }

    /**
     * Ruft den Wert der preisangabeReparaturkostenDurch-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.PreisangabeReparaturkostenDurch }
     *     
     */
    public AllgemeineSchadendaten.PreisangabeReparaturkostenDurch getPreisangabeReparaturkostenDurch() {
        return preisangabeReparaturkostenDurch;
    }

    /**
     * Legt den Wert der preisangabeReparaturkostenDurch-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.PreisangabeReparaturkostenDurch }
     *     
     */
    public void setPreisangabeReparaturkostenDurch(AllgemeineSchadendaten.PreisangabeReparaturkostenDurch value) {
        this.preisangabeReparaturkostenDurch = value;
    }

    /**
     * Ruft den Wert der reinigungskosten-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.Reinigungskosten }
     *     
     */
    public AllgemeineSchadendaten.Reinigungskosten getReinigungskosten() {
        return reinigungskosten;
    }

    /**
     * Legt den Wert der reinigungskosten-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.Reinigungskosten }
     *     
     */
    public void setReinigungskosten(AllgemeineSchadendaten.Reinigungskosten value) {
        this.reinigungskosten = value;
    }

    /**
     * Ruft den Wert der preisangabeDerReinigungskostenDurch-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.PreisangabeDerReinigungskostenDurch }
     *     
     */
    public AllgemeineSchadendaten.PreisangabeDerReinigungskostenDurch getPreisangabeDerReinigungskostenDurch() {
        return preisangabeDerReinigungskostenDurch;
    }

    /**
     * Legt den Wert der preisangabeDerReinigungskostenDurch-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.PreisangabeDerReinigungskostenDurch }
     *     
     */
    public void setPreisangabeDerReinigungskostenDurch(AllgemeineSchadendaten.PreisangabeDerReinigungskostenDurch value) {
        this.preisangabeDerReinigungskostenDurch = value;
    }

    /**
     * Ruft den Wert der wiederbeschaffungswert-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.Wiederbeschaffungswert }
     *     
     */
    public AllgemeineSchadendaten.Wiederbeschaffungswert getWiederbeschaffungswert() {
        return wiederbeschaffungswert;
    }

    /**
     * Legt den Wert der wiederbeschaffungswert-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.Wiederbeschaffungswert }
     *     
     */
    public void setWiederbeschaffungswert(AllgemeineSchadendaten.Wiederbeschaffungswert value) {
        this.wiederbeschaffungswert = value;
    }

    /**
     * Ruft den Wert der preisangabeWiederbeschaffungswertDurch-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.PreisangabeWiederbeschaffungswertDurch }
     *     
     */
    public AllgemeineSchadendaten.PreisangabeWiederbeschaffungswertDurch getPreisangabeWiederbeschaffungswertDurch() {
        return preisangabeWiederbeschaffungswertDurch;
    }

    /**
     * Legt den Wert der preisangabeWiederbeschaffungswertDurch-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.PreisangabeWiederbeschaffungswertDurch }
     *     
     */
    public void setPreisangabeWiederbeschaffungswertDurch(AllgemeineSchadendaten.PreisangabeWiederbeschaffungswertDurch value) {
        this.preisangabeWiederbeschaffungswertDurch = value;
    }

    /**
     * Ruft den Wert der schadenmeldungDurch-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.SchadenmeldungDurch }
     *     
     */
    public AllgemeineSchadendaten.SchadenmeldungDurch getSchadenmeldungDurch() {
        return schadenmeldungDurch;
    }

    /**
     * Legt den Wert der schadenmeldungDurch-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.SchadenmeldungDurch }
     *     
     */
    public void setSchadenmeldungDurch(AllgemeineSchadendaten.SchadenmeldungDurch value) {
        this.schadenmeldungDurch = value;
    }

    /**
     * Ruft den Wert der kontaktUeber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AllgemeineSchadendaten.KontaktUeber }
     *     
     */
    public AllgemeineSchadendaten.KontaktUeber getKontaktUeber() {
        return kontaktUeber;
    }

    /**
     * Legt den Wert der kontaktUeber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AllgemeineSchadendaten.KontaktUeber }
     *     
     */
    public void setKontaktUeber(AllgemeineSchadendaten.KontaktUeber value) {
        this.kontaktUeber = value;
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
            return "4200";
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
            return "004";
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
     *         &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
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
    public static class AnzahlTote {

        @XmlElement(name = "Wert")
        protected BigInteger wert;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

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
         *     {@link String }
         *     
         */
        public String getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndikator(String value) {
            this.indikator = value;
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
     *         &lt;element name="Wert" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               &lt;minInclusive value="0"/>
     *               &lt;totalDigits value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
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
    public static class AnzahlVerletzte {

        @XmlElement(name = "Wert")
        protected BigInteger wert;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

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
         *     {@link String }
         *     
         */
        public String getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndikator(String value) {
            this.indikator = value;
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
    public static class EigentuemerWohnung {

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
     *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess112Typ" minOccurs="0"/>
     *         &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
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
    public static class GeschaetzeRepkostenInWE {

        @XmlElement(name = "Wert")
        protected BigDecimal wert;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der wert-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getWert() {
            return wert;
        }

        /**
         * Legt den Wert der wert-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setWert(BigDecimal value) {
            this.wert = value;
        }

        /**
         * Ruft den Wert der indikator-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndikator(String value) {
            this.indikator = value;
        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>KontaktartTyp">
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
    public static class KontaktUeber {

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
    public static class PreisangabeDerReinigungskostenDurch {

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
    public static class PreisangabeReparaturkostenDurch {

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
    public static class PreisangabeWiederbeschaffungswertDurch {

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
     *         &lt;element name="Wert">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="999.99"/>
     *               &lt;totalDigits value="5"/>
     *               &lt;fractionDigits value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
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
    public static class ProzentsatzMWSt {

        @XmlElement(name = "Wert", required = true)
        protected BigDecimal wert;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der wert-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getWert() {
            return wert;
        }

        /**
         * Legt den Wert der wert-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setWert(BigDecimal value) {
            this.wert = value;
        }

        /**
         * Ruft den Wert der indikator-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndikator(String value) {
            this.indikator = value;
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
     *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess112Typ" minOccurs="0"/>
     *         &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
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
    public static class Reinigungskosten {

        @XmlElement(name = "Wert")
        protected BigDecimal wert;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der wert-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getWert() {
            return wert;
        }

        /**
         * Legt den Wert der wert-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setWert(BigDecimal value) {
            this.wert = value;
        }

        /**
         * Ruft den Wert der indikator-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndikator(String value) {
            this.indikator = value;
        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
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
    public static class SchadenAnderweitigAngezeigt {

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
     *         &lt;element name="SchadenersatzanspruecheErhoben" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SchadenersatzTyp" minOccurs="0"/>
     *         &lt;element name="VNZahlungEinvestanden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="BegruendungZahlung" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="RAEingeschaltet" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
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
        "schadenersatzanspruecheErhoben",
        "vnZahlungEinvestanden",
        "begruendungZahlung",
        "raEingeschaltet"
    })
    public static class Schadenabwicklung {

        @XmlElement(name = "SchadenersatzanspruecheErhoben")
        protected String schadenersatzanspruecheErhoben;
        @XmlElement(name = "VNZahlungEinvestanden")
        protected String vnZahlungEinvestanden;
        @XmlElement(name = "BegruendungZahlung")
        protected String begruendungZahlung;
        @XmlElement(name = "RAEingeschaltet")
        protected String raEingeschaltet;

        /**
         * Ruft den Wert der schadenersatzanspruecheErhoben-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSchadenersatzanspruecheErhoben() {
            return schadenersatzanspruecheErhoben;
        }

        /**
         * Legt den Wert der schadenersatzanspruecheErhoben-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSchadenersatzanspruecheErhoben(String value) {
            this.schadenersatzanspruecheErhoben = value;
        }

        /**
         * Ruft den Wert der vnZahlungEinvestanden-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVNZahlungEinvestanden() {
            return vnZahlungEinvestanden;
        }

        /**
         * Legt den Wert der vnZahlungEinvestanden-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVNZahlungEinvestanden(String value) {
            this.vnZahlungEinvestanden = value;
        }

        /**
         * Ruft den Wert der begruendungZahlung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBegruendungZahlung() {
            return begruendungZahlung;
        }

        /**
         * Legt den Wert der begruendungZahlung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBegruendungZahlung(String value) {
            this.begruendungZahlung = value;
        }

        /**
         * Ruft den Wert der raEingeschaltet-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRAEingeschaltet() {
            return raEingeschaltet;
        }

        /**
         * Legt den Wert der raEingeschaltet-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRAEingeschaltet(String value) {
            this.raEingeschaltet = value;
        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
     *       &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Schadenmeldedatum
        extends SatzendeTyp
    {


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
     *         &lt;element name="AdressKennzeichen1" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
     *         &lt;element name="PartnerReferenz1" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
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
        "adressKennzeichen1",
        "partnerReferenz1"
    })
    public static class SchadenmeldungDurch {

        @XmlElement(name = "AdressKennzeichen1")
        protected String adressKennzeichen1;
        @XmlElement(name = "PartnerReferenz1")
        protected String partnerReferenz1;

        /**
         * Ruft den Wert der adressKennzeichen1-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAdressKennzeichen1() {
            return adressKennzeichen1;
        }

        /**
         * Legt den Wert der adressKennzeichen1-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAdressKennzeichen1(String value) {
            this.adressKennzeichen1 = value;
        }

        /**
         * Ruft den Wert der partnerReferenz1-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPartnerReferenz1() {
            return partnerReferenz1;
        }

        /**
         * Legt den Wert der partnerReferenz1-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPartnerReferenz1(String value) {
            this.partnerReferenz1 = value;
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
     *         &lt;element name="LKZ" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LaenderkennzeichenTyp" minOccurs="0"/>
     *         &lt;element name="PLZ" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="6"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Ort" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="25"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Strasse" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="BeschreibungSchadenort" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="80"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
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
        "lkz",
        "plz",
        "ort",
        "strasse",
        "beschreibungSchadenort"
    })
    public static class Schadenort {

        @XmlElement(name = "LKZ")
        protected String lkz;
        @XmlElement(name = "PLZ")
        protected String plz;
        @XmlElement(name = "Ort")
        protected String ort;
        @XmlElement(name = "Strasse")
        protected String strasse;
        @XmlElement(name = "BeschreibungSchadenort")
        protected String beschreibungSchadenort;

        /**
         * Ruft den Wert der lkz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLKZ() {
            return lkz;
        }

        /**
         * Legt den Wert der lkz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLKZ(String value) {
            this.lkz = value;
        }

        /**
         * Ruft den Wert der plz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPLZ() {
            return plz;
        }

        /**
         * Legt den Wert der plz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPLZ(String value) {
            this.plz = value;
        }

        /**
         * Ruft den Wert der ort-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOrt() {
            return ort;
        }

        /**
         * Legt den Wert der ort-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOrt(String value) {
            this.ort = value;
        }

        /**
         * Ruft den Wert der strasse-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStrasse() {
            return strasse;
        }

        /**
         * Legt den Wert der strasse-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStrasse(String value) {
            this.strasse = value;
        }

        /**
         * Ruft den Wert der beschreibungSchadenort-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBeschreibungSchadenort() {
            return beschreibungSchadenort;
        }

        /**
         * Legt den Wert der beschreibungSchadenort-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBeschreibungSchadenort(String value) {
            this.beschreibungSchadenort = value;
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
     *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess112Typ" minOccurs="0"/>
     *         &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
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
    public static class Wiederbeschaffungswert {

        @XmlElement(name = "Wert")
        protected BigDecimal wert;
        @XmlElement(name = "Indikator", required = true)
        protected String indikator;

        /**
         * Ruft den Wert der wert-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getWert() {
            return wert;
        }

        /**
         * Legt den Wert der wert-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setWert(BigDecimal value) {
            this.wert = value;
        }

        /**
         * Ruft den Wert der indikator-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndikator(String value) {
            this.indikator = value;
        }

    }

}
