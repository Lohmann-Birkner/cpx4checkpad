//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.19 um 11:55:14 AM CEST 
//


package de.lb.cpx.gdv.messages;

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
 *         &lt;element name="lfdNrBeschaedigtenSache" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RaeumeBehaeltnisseAufgebrochen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="BeschreibungDerRaeumeBehaeltnisse" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SachenAusserhalbVersicherungsort" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="DatumSeitAusserhalb" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DatumGeplanteRueckbringung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SchlossartDerTuer" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>TuerschlossartTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="BeschreibungSchlossartTuer" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AnzahlSchluessel" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="0"/>
 *               &lt;totalDigits value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NachschluesselSelbstGefertigt" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="BeschreibungAnfertigerNachschluessel" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="AufbewahrungsortAllerSchluessel" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DatumDesSchluesselverlustes" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="FremdeSeitVerlustInVersichertenRaeumen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="FremdeAlleingelassen" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="IndikatorNachschluesseldiebstahl" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AbgestellteSache" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AbgestellteSacheTyp" minOccurs="0"/>
 *         &lt;element name="AbstellortTatzeit" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AbstellortZurTatzeitTyp" minOccurs="0"/>
 *         &lt;element name="Abstelldatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Abstelluhrzeit" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *               &lt;minLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DatumGeplanteWiederbenutzung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="UhrzeitGeplanteWiederbenutzung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *               &lt;minLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DatumLetzteSichtung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="UhrzeitLetzteSichtung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *               &lt;minLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SacheVerschlossen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}VerschlussartTyp" minOccurs="0"/>
 *         &lt;element name="HaeuslicheGemeinschaftMitEigentuemer" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>JaNeinTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4282">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="4"/>
 *             &lt;minLength value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Versionsnummer" fixed="001">
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
    "lfdNrBeschaedigtenSache",
    "raeumeBehaeltnisseAufgebrochen",
    "beschreibungDerRaeumeBehaeltnisse",
    "sachenAusserhalbVersicherungsort",
    "datumSeitAusserhalb",
    "datumGeplanteRueckbringung",
    "schlossartDerTuer",
    "beschreibungSchlossartTuer",
    "anzahlSchluessel",
    "nachschluesselSelbstGefertigt",
    "beschreibungAnfertigerNachschluessel",
    "aufbewahrungsortAllerSchluessel",
    "datumDesSchluesselverlustes",
    "fremdeSeitVerlustInVersichertenRaeumen",
    "fremdeAlleingelassen",
    "indikatorNachschluesseldiebstahl",
    "abgestellteSache",
    "abstellortTatzeit",
    "abstelldatum",
    "abstelluhrzeit",
    "datumGeplanteWiederbenutzung",
    "uhrzeitGeplanteWiederbenutzung",
    "datumLetzteSichtung",
    "uhrzeitLetzteSichtung",
    "sacheVerschlossen",
    "haeuslicheGemeinschaftMitEigentuemer"
})
@XmlRootElement(name = "SachSchadendatenDiebstahl")
public class SachSchadendatenDiebstahl {

    protected String lfdNrBeschaedigtenSache;
    @XmlElement(name = "RaeumeBehaeltnisseAufgebrochen")
    protected String raeumeBehaeltnisseAufgebrochen;
    @XmlElement(name = "BeschreibungDerRaeumeBehaeltnisse")
    protected String beschreibungDerRaeumeBehaeltnisse;
    @XmlElement(name = "SachenAusserhalbVersicherungsort")
    protected String sachenAusserhalbVersicherungsort;
    @XmlElement(name = "DatumSeitAusserhalb")
    protected String datumSeitAusserhalb;
    @XmlElement(name = "DatumGeplanteRueckbringung")
    protected String datumGeplanteRueckbringung;
    @XmlElement(name = "SchlossartDerTuer")
    protected SachSchadendatenDiebstahl.SchlossartDerTuer schlossartDerTuer;
    @XmlElement(name = "BeschreibungSchlossartTuer")
    protected String beschreibungSchlossartTuer;
    @XmlElement(name = "AnzahlSchluessel")
    protected BigInteger anzahlSchluessel;
    @XmlElement(name = "NachschluesselSelbstGefertigt")
    protected SachSchadendatenDiebstahl.NachschluesselSelbstGefertigt nachschluesselSelbstGefertigt;
    @XmlElement(name = "BeschreibungAnfertigerNachschluessel")
    protected SachSchadendatenDiebstahl.BeschreibungAnfertigerNachschluessel beschreibungAnfertigerNachschluessel;
    @XmlElement(name = "AufbewahrungsortAllerSchluessel")
    protected String aufbewahrungsortAllerSchluessel;
    @XmlElement(name = "DatumDesSchluesselverlustes")
    protected String datumDesSchluesselverlustes;
    @XmlElement(name = "FremdeSeitVerlustInVersichertenRaeumen")
    protected String fremdeSeitVerlustInVersichertenRaeumen;
    @XmlElement(name = "FremdeAlleingelassen")
    protected SachSchadendatenDiebstahl.FremdeAlleingelassen fremdeAlleingelassen;
    @XmlElement(name = "IndikatorNachschluesseldiebstahl")
    protected String indikatorNachschluesseldiebstahl;
    @XmlElement(name = "AbgestellteSache")
    protected String abgestellteSache;
    @XmlElement(name = "AbstellortTatzeit")
    protected String abstellortTatzeit;
    @XmlElement(name = "Abstelldatum")
    protected String abstelldatum;
    @XmlElement(name = "Abstelluhrzeit")
    protected String abstelluhrzeit;
    @XmlElement(name = "DatumGeplanteWiederbenutzung")
    protected String datumGeplanteWiederbenutzung;
    @XmlElement(name = "UhrzeitGeplanteWiederbenutzung")
    protected String uhrzeitGeplanteWiederbenutzung;
    @XmlElement(name = "DatumLetzteSichtung")
    protected String datumLetzteSichtung;
    @XmlElement(name = "UhrzeitLetzteSichtung")
    protected String uhrzeitLetzteSichtung;
    @XmlElement(name = "SacheVerschlossen")
    protected String sacheVerschlossen;
    @XmlElement(name = "HaeuslicheGemeinschaftMitEigentuemer")
    protected SachSchadendatenDiebstahl.HaeuslicheGemeinschaftMitEigentuemer haeuslicheGemeinschaftMitEigentuemer;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der lfdNrBeschaedigtenSache-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLfdNrBeschaedigtenSache() {
        return lfdNrBeschaedigtenSache;
    }

    /**
     * Legt den Wert der lfdNrBeschaedigtenSache-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLfdNrBeschaedigtenSache(String value) {
        this.lfdNrBeschaedigtenSache = value;
    }

    /**
     * Ruft den Wert der raeumeBehaeltnisseAufgebrochen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRaeumeBehaeltnisseAufgebrochen() {
        return raeumeBehaeltnisseAufgebrochen;
    }

    /**
     * Legt den Wert der raeumeBehaeltnisseAufgebrochen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRaeumeBehaeltnisseAufgebrochen(String value) {
        this.raeumeBehaeltnisseAufgebrochen = value;
    }

    /**
     * Ruft den Wert der beschreibungDerRaeumeBehaeltnisse-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeschreibungDerRaeumeBehaeltnisse() {
        return beschreibungDerRaeumeBehaeltnisse;
    }

    /**
     * Legt den Wert der beschreibungDerRaeumeBehaeltnisse-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeschreibungDerRaeumeBehaeltnisse(String value) {
        this.beschreibungDerRaeumeBehaeltnisse = value;
    }

    /**
     * Ruft den Wert der sachenAusserhalbVersicherungsort-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSachenAusserhalbVersicherungsort() {
        return sachenAusserhalbVersicherungsort;
    }

    /**
     * Legt den Wert der sachenAusserhalbVersicherungsort-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSachenAusserhalbVersicherungsort(String value) {
        this.sachenAusserhalbVersicherungsort = value;
    }

    /**
     * Ruft den Wert der datumSeitAusserhalb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatumSeitAusserhalb() {
        return datumSeitAusserhalb;
    }

    /**
     * Legt den Wert der datumSeitAusserhalb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatumSeitAusserhalb(String value) {
        this.datumSeitAusserhalb = value;
    }

    /**
     * Ruft den Wert der datumGeplanteRueckbringung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatumGeplanteRueckbringung() {
        return datumGeplanteRueckbringung;
    }

    /**
     * Legt den Wert der datumGeplanteRueckbringung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatumGeplanteRueckbringung(String value) {
        this.datumGeplanteRueckbringung = value;
    }

    /**
     * Ruft den Wert der schlossartDerTuer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenDiebstahl.SchlossartDerTuer }
     *     
     */
    public SachSchadendatenDiebstahl.SchlossartDerTuer getSchlossartDerTuer() {
        return schlossartDerTuer;
    }

    /**
     * Legt den Wert der schlossartDerTuer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenDiebstahl.SchlossartDerTuer }
     *     
     */
    public void setSchlossartDerTuer(SachSchadendatenDiebstahl.SchlossartDerTuer value) {
        this.schlossartDerTuer = value;
    }

    /**
     * Ruft den Wert der beschreibungSchlossartTuer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeschreibungSchlossartTuer() {
        return beschreibungSchlossartTuer;
    }

    /**
     * Legt den Wert der beschreibungSchlossartTuer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeschreibungSchlossartTuer(String value) {
        this.beschreibungSchlossartTuer = value;
    }

    /**
     * Ruft den Wert der anzahlSchluessel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnzahlSchluessel() {
        return anzahlSchluessel;
    }

    /**
     * Legt den Wert der anzahlSchluessel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnzahlSchluessel(BigInteger value) {
        this.anzahlSchluessel = value;
    }

    /**
     * Ruft den Wert der nachschluesselSelbstGefertigt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenDiebstahl.NachschluesselSelbstGefertigt }
     *     
     */
    public SachSchadendatenDiebstahl.NachschluesselSelbstGefertigt getNachschluesselSelbstGefertigt() {
        return nachschluesselSelbstGefertigt;
    }

    /**
     * Legt den Wert der nachschluesselSelbstGefertigt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenDiebstahl.NachschluesselSelbstGefertigt }
     *     
     */
    public void setNachschluesselSelbstGefertigt(SachSchadendatenDiebstahl.NachschluesselSelbstGefertigt value) {
        this.nachschluesselSelbstGefertigt = value;
    }

    /**
     * Ruft den Wert der beschreibungAnfertigerNachschluessel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenDiebstahl.BeschreibungAnfertigerNachschluessel }
     *     
     */
    public SachSchadendatenDiebstahl.BeschreibungAnfertigerNachschluessel getBeschreibungAnfertigerNachschluessel() {
        return beschreibungAnfertigerNachschluessel;
    }

    /**
     * Legt den Wert der beschreibungAnfertigerNachschluessel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenDiebstahl.BeschreibungAnfertigerNachschluessel }
     *     
     */
    public void setBeschreibungAnfertigerNachschluessel(SachSchadendatenDiebstahl.BeschreibungAnfertigerNachschluessel value) {
        this.beschreibungAnfertigerNachschluessel = value;
    }

    /**
     * Ruft den Wert der aufbewahrungsortAllerSchluessel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAufbewahrungsortAllerSchluessel() {
        return aufbewahrungsortAllerSchluessel;
    }

    /**
     * Legt den Wert der aufbewahrungsortAllerSchluessel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAufbewahrungsortAllerSchluessel(String value) {
        this.aufbewahrungsortAllerSchluessel = value;
    }

    /**
     * Ruft den Wert der datumDesSchluesselverlustes-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatumDesSchluesselverlustes() {
        return datumDesSchluesselverlustes;
    }

    /**
     * Legt den Wert der datumDesSchluesselverlustes-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatumDesSchluesselverlustes(String value) {
        this.datumDesSchluesselverlustes = value;
    }

    /**
     * Ruft den Wert der fremdeSeitVerlustInVersichertenRaeumen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFremdeSeitVerlustInVersichertenRaeumen() {
        return fremdeSeitVerlustInVersichertenRaeumen;
    }

    /**
     * Legt den Wert der fremdeSeitVerlustInVersichertenRaeumen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFremdeSeitVerlustInVersichertenRaeumen(String value) {
        this.fremdeSeitVerlustInVersichertenRaeumen = value;
    }

    /**
     * Ruft den Wert der fremdeAlleingelassen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenDiebstahl.FremdeAlleingelassen }
     *     
     */
    public SachSchadendatenDiebstahl.FremdeAlleingelassen getFremdeAlleingelassen() {
        return fremdeAlleingelassen;
    }

    /**
     * Legt den Wert der fremdeAlleingelassen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenDiebstahl.FremdeAlleingelassen }
     *     
     */
    public void setFremdeAlleingelassen(SachSchadendatenDiebstahl.FremdeAlleingelassen value) {
        this.fremdeAlleingelassen = value;
    }

    /**
     * Ruft den Wert der indikatorNachschluesseldiebstahl-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndikatorNachschluesseldiebstahl() {
        return indikatorNachschluesseldiebstahl;
    }

    /**
     * Legt den Wert der indikatorNachschluesseldiebstahl-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndikatorNachschluesseldiebstahl(String value) {
        this.indikatorNachschluesseldiebstahl = value;
    }

    /**
     * Ruft den Wert der abgestellteSache-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbgestellteSache() {
        return abgestellteSache;
    }

    /**
     * Legt den Wert der abgestellteSache-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbgestellteSache(String value) {
        this.abgestellteSache = value;
    }

    /**
     * Ruft den Wert der abstellortTatzeit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbstellortTatzeit() {
        return abstellortTatzeit;
    }

    /**
     * Legt den Wert der abstellortTatzeit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbstellortTatzeit(String value) {
        this.abstellortTatzeit = value;
    }

    /**
     * Ruft den Wert der abstelldatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbstelldatum() {
        return abstelldatum;
    }

    /**
     * Legt den Wert der abstelldatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbstelldatum(String value) {
        this.abstelldatum = value;
    }

    /**
     * Ruft den Wert der abstelluhrzeit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbstelluhrzeit() {
        return abstelluhrzeit;
    }

    /**
     * Legt den Wert der abstelluhrzeit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbstelluhrzeit(String value) {
        this.abstelluhrzeit = value;
    }

    /**
     * Ruft den Wert der datumGeplanteWiederbenutzung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatumGeplanteWiederbenutzung() {
        return datumGeplanteWiederbenutzung;
    }

    /**
     * Legt den Wert der datumGeplanteWiederbenutzung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatumGeplanteWiederbenutzung(String value) {
        this.datumGeplanteWiederbenutzung = value;
    }

    /**
     * Ruft den Wert der uhrzeitGeplanteWiederbenutzung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUhrzeitGeplanteWiederbenutzung() {
        return uhrzeitGeplanteWiederbenutzung;
    }

    /**
     * Legt den Wert der uhrzeitGeplanteWiederbenutzung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUhrzeitGeplanteWiederbenutzung(String value) {
        this.uhrzeitGeplanteWiederbenutzung = value;
    }

    /**
     * Ruft den Wert der datumLetzteSichtung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatumLetzteSichtung() {
        return datumLetzteSichtung;
    }

    /**
     * Legt den Wert der datumLetzteSichtung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatumLetzteSichtung(String value) {
        this.datumLetzteSichtung = value;
    }

    /**
     * Ruft den Wert der uhrzeitLetzteSichtung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUhrzeitLetzteSichtung() {
        return uhrzeitLetzteSichtung;
    }

    /**
     * Legt den Wert der uhrzeitLetzteSichtung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUhrzeitLetzteSichtung(String value) {
        this.uhrzeitLetzteSichtung = value;
    }

    /**
     * Ruft den Wert der sacheVerschlossen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSacheVerschlossen() {
        return sacheVerschlossen;
    }

    /**
     * Legt den Wert der sacheVerschlossen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSacheVerschlossen(String value) {
        this.sacheVerschlossen = value;
    }

    /**
     * Ruft den Wert der haeuslicheGemeinschaftMitEigentuemer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SachSchadendatenDiebstahl.HaeuslicheGemeinschaftMitEigentuemer }
     *     
     */
    public SachSchadendatenDiebstahl.HaeuslicheGemeinschaftMitEigentuemer getHaeuslicheGemeinschaftMitEigentuemer() {
        return haeuslicheGemeinschaftMitEigentuemer;
    }

    /**
     * Legt den Wert der haeuslicheGemeinschaftMitEigentuemer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SachSchadendatenDiebstahl.HaeuslicheGemeinschaftMitEigentuemer }
     *     
     */
    public void setHaeuslicheGemeinschaftMitEigentuemer(SachSchadendatenDiebstahl.HaeuslicheGemeinschaftMitEigentuemer value) {
        this.haeuslicheGemeinschaftMitEigentuemer = value;
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
            return "4282";
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
            return "001";
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
    public static class BeschreibungAnfertigerNachschluessel
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
    public static class FremdeAlleingelassen {

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
    public static class HaeuslicheGemeinschaftMitEigentuemer {

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
    public static class NachschluesselSelbstGefertigt {

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
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>TuerschlossartTyp">
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
    public static class SchlossartDerTuer {

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

}
