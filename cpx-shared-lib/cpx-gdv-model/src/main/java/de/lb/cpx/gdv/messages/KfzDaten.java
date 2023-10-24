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
 *         &lt;element name="IdentifikationPartner">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Adresskennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp"/>
 *                   &lt;element name="Partnerreferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Finanzierungsart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}FinanzierungsartTyp" minOccurs="0"/>
 *         &lt;element name="Wagniskennziffer" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WKZTyp" minOccurs="0"/>
 *         &lt;element name="Leistung" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="9999.9"/>
 *                         &lt;totalDigits value="5"/>
 *                         &lt;fractionDigits value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="GesGewicht" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="9999.9"/>
 *                         &lt;totalDigits value="5"/>
 *                         &lt;fractionDigits value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="AnzSitze" minOccurs="0">
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
 *         &lt;element name="Herstellername" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="15"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Modellname" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="HSN" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TSN" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Hubraum" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;totalDigits value="6"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Fahrzeug-Id-Nr" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="17"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Kfz-Kennzeichen" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="12"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Saisonkennzeichen" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Wechselkennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="LandZulassung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LaenderkennzeichenTyp" minOccurs="0"/>
 *         &lt;element name="Getriebe" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}GetriebeartTyp" minOccurs="0"/>
 *         &lt;element name="Motorart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}MotorartTyp" minOccurs="0"/>
 *         &lt;element name="Farbe" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Erstzulassung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Km-Stand" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;totalDigits value="7"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Km-Ermittlung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}KMErmittlungsartTyp" minOccurs="0"/>
 *         &lt;element name="Laufleistung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}LaufleistungsartTyp" minOccurs="0"/>
 *         &lt;element name="Datum-HU" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Datum-ASU" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Profil-VL" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="99.9"/>
 *                         &lt;totalDigits value="3"/>
 *                         &lt;fractionDigits value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Profil-VR" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="99.9"/>
 *                         &lt;totalDigits value="3"/>
 *                         &lt;fractionDigits value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Profil-HL" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="99.9"/>
 *                         &lt;totalDigits value="3"/>
 *                         &lt;fractionDigits value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Profil-HR" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="99.9"/>
 *                         &lt;totalDigits value="3"/>
 *                         &lt;fractionDigits value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Profil-Res" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="99.9"/>
 *                         &lt;totalDigits value="3"/>
 *                         &lt;fractionDigits value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ArtReserverad" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>ArtDesReserveradesTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Zustimmung-FH" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Waehrungsschluessel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WaehrungsschluesselTyp" minOccurs="0"/>
 *         &lt;element name="ProzentsatzMWSt" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
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
 *         &lt;element name="Wiederbeschaffungswert" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="9999999999.99"/>
 *                         &lt;totalDigits value="12"/>
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
 *         &lt;element name="Wiederbeschaffungswert-netto" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="9999999999.99"/>
 *                         &lt;totalDigits value="12"/>
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
 *         &lt;element name="Vorschaeden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="ZuordnungNutzungsausfall" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Fuehrerschein" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Vorhanden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *                   &lt;element name="Klasse" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="4"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Ausstellungsdatum" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="8"/>
 *                         &lt;minLength value="8"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Entzugsdatum" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="8"/>
 *                         &lt;minLength value="8"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="EDV-Bewertungsschluesselnr" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="EDV-BewertungsschluesselnrArt" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>EDV-SchluesselTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4210">
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
    "identifikationPartner",
    "finanzierungsart",
    "wagniskennziffer",
    "leistung",
    "gesGewicht",
    "anzSitze",
    "herstellername",
    "modellname",
    "hsn",
    "tsn",
    "hubraum",
    "fahrzeugIdNr",
    "kfzKennzeichen",
    "saisonkennzeichen",
    "wechselkennzeichen",
    "landZulassung",
    "getriebe",
    "motorart",
    "farbe",
    "erstzulassung",
    "kmStand",
    "kmErmittlung",
    "laufleistung",
    "datumHU",
    "datumASU",
    "profilVL",
    "profilVR",
    "profilHL",
    "profilHR",
    "profilRes",
    "artReserverad",
    "zustimmungFH",
    "waehrungsschluessel",
    "prozentsatzMWSt",
    "wiederbeschaffungswert",
    "wiederbeschaffungswertNetto",
    "vorschaeden",
    "zuordnungNutzungsausfall",
    "fuehrerschein",
    "edvBewertungsschluesselnr",
    "edvBewertungsschluesselnrArt"
})
@XmlRootElement(name = "Kfz-Daten")
public class KfzDaten {

    @XmlElement(name = "IdentifikationPartner", required = true)
    protected KfzDaten.IdentifikationPartner identifikationPartner;
    @XmlElement(name = "Finanzierungsart")
    protected String finanzierungsart;
    @XmlElement(name = "Wagniskennziffer")
    protected String wagniskennziffer;
    @XmlElement(name = "Leistung")
    protected KfzDaten.Leistung leistung;
    @XmlElement(name = "GesGewicht")
    protected KfzDaten.GesGewicht gesGewicht;
    @XmlElement(name = "AnzSitze")
    protected KfzDaten.AnzSitze anzSitze;
    @XmlElement(name = "Herstellername")
    protected String herstellername;
    @XmlElement(name = "Modellname")
    protected String modellname;
    @XmlElement(name = "HSN")
    protected String hsn;
    @XmlElement(name = "TSN")
    protected String tsn;
    @XmlElement(name = "Hubraum")
    protected KfzDaten.Hubraum hubraum;
    @XmlElement(name = "Fahrzeug-Id-Nr")
    protected String fahrzeugIdNr;
    @XmlElement(name = "Kfz-Kennzeichen")
    protected String kfzKennzeichen;
    @XmlElement(name = "Saisonkennzeichen")
    protected String saisonkennzeichen;
    @XmlElement(name = "Wechselkennzeichen")
    protected String wechselkennzeichen;
    @XmlElement(name = "LandZulassung")
    protected String landZulassung;
    @XmlElement(name = "Getriebe")
    protected String getriebe;
    @XmlElement(name = "Motorart")
    protected String motorart;
    @XmlElement(name = "Farbe")
    protected String farbe;
    @XmlElement(name = "Erstzulassung")
    protected String erstzulassung;
    @XmlElement(name = "Km-Stand")
    protected KfzDaten.KmStand kmStand;
    @XmlElement(name = "Km-Ermittlung")
    protected String kmErmittlung;
    @XmlElement(name = "Laufleistung")
    protected String laufleistung;
    @XmlElement(name = "Datum-HU")
    protected String datumHU;
    @XmlElement(name = "Datum-ASU")
    protected String datumASU;
    @XmlElement(name = "Profil-VL")
    protected KfzDaten.ProfilVL profilVL;
    @XmlElement(name = "Profil-VR")
    protected KfzDaten.ProfilVR profilVR;
    @XmlElement(name = "Profil-HL")
    protected KfzDaten.ProfilHL profilHL;
    @XmlElement(name = "Profil-HR")
    protected KfzDaten.ProfilHR profilHR;
    @XmlElement(name = "Profil-Res")
    protected KfzDaten.ProfilRes profilRes;
    @XmlElement(name = "ArtReserverad")
    protected KfzDaten.ArtReserverad artReserverad;
    @XmlElement(name = "Zustimmung-FH")
    protected String zustimmungFH;
    @XmlElement(name = "Waehrungsschluessel")
    protected String waehrungsschluessel;
    @XmlElement(name = "ProzentsatzMWSt")
    protected KfzDaten.ProzentsatzMWSt prozentsatzMWSt;
    @XmlElement(name = "Wiederbeschaffungswert")
    protected KfzDaten.Wiederbeschaffungswert wiederbeschaffungswert;
    @XmlElement(name = "Wiederbeschaffungswert-netto")
    protected KfzDaten.WiederbeschaffungswertNetto wiederbeschaffungswertNetto;
    @XmlElement(name = "Vorschaeden")
    protected String vorschaeden;
    @XmlElement(name = "ZuordnungNutzungsausfall")
    protected String zuordnungNutzungsausfall;
    @XmlElement(name = "Fuehrerschein")
    protected KfzDaten.Fuehrerschein fuehrerschein;
    @XmlElement(name = "EDV-Bewertungsschluesselnr")
    protected String edvBewertungsschluesselnr;
    @XmlElement(name = "EDV-BewertungsschluesselnrArt")
    protected KfzDaten.EDVBewertungsschluesselnrArt edvBewertungsschluesselnrArt;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der identifikationPartner-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.IdentifikationPartner }
     *     
     */
    public KfzDaten.IdentifikationPartner getIdentifikationPartner() {
        return identifikationPartner;
    }

    /**
     * Legt den Wert der identifikationPartner-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.IdentifikationPartner }
     *     
     */
    public void setIdentifikationPartner(KfzDaten.IdentifikationPartner value) {
        this.identifikationPartner = value;
    }

    /**
     * Ruft den Wert der finanzierungsart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinanzierungsart() {
        return finanzierungsart;
    }

    /**
     * Legt den Wert der finanzierungsart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinanzierungsart(String value) {
        this.finanzierungsart = value;
    }

    /**
     * Ruft den Wert der wagniskennziffer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWagniskennziffer() {
        return wagniskennziffer;
    }

    /**
     * Legt den Wert der wagniskennziffer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWagniskennziffer(String value) {
        this.wagniskennziffer = value;
    }

    /**
     * Ruft den Wert der leistung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.Leistung }
     *     
     */
    public KfzDaten.Leistung getLeistung() {
        return leistung;
    }

    /**
     * Legt den Wert der leistung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.Leistung }
     *     
     */
    public void setLeistung(KfzDaten.Leistung value) {
        this.leistung = value;
    }

    /**
     * Ruft den Wert der gesGewicht-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.GesGewicht }
     *     
     */
    public KfzDaten.GesGewicht getGesGewicht() {
        return gesGewicht;
    }

    /**
     * Legt den Wert der gesGewicht-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.GesGewicht }
     *     
     */
    public void setGesGewicht(KfzDaten.GesGewicht value) {
        this.gesGewicht = value;
    }

    /**
     * Ruft den Wert der anzSitze-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.AnzSitze }
     *     
     */
    public KfzDaten.AnzSitze getAnzSitze() {
        return anzSitze;
    }

    /**
     * Legt den Wert der anzSitze-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.AnzSitze }
     *     
     */
    public void setAnzSitze(KfzDaten.AnzSitze value) {
        this.anzSitze = value;
    }

    /**
     * Ruft den Wert der herstellername-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHerstellername() {
        return herstellername;
    }

    /**
     * Legt den Wert der herstellername-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHerstellername(String value) {
        this.herstellername = value;
    }

    /**
     * Ruft den Wert der modellname-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModellname() {
        return modellname;
    }

    /**
     * Legt den Wert der modellname-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModellname(String value) {
        this.modellname = value;
    }

    /**
     * Ruft den Wert der hsn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHSN() {
        return hsn;
    }

    /**
     * Legt den Wert der hsn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHSN(String value) {
        this.hsn = value;
    }

    /**
     * Ruft den Wert der tsn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTSN() {
        return tsn;
    }

    /**
     * Legt den Wert der tsn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTSN(String value) {
        this.tsn = value;
    }

    /**
     * Ruft den Wert der hubraum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.Hubraum }
     *     
     */
    public KfzDaten.Hubraum getHubraum() {
        return hubraum;
    }

    /**
     * Legt den Wert der hubraum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.Hubraum }
     *     
     */
    public void setHubraum(KfzDaten.Hubraum value) {
        this.hubraum = value;
    }

    /**
     * Ruft den Wert der fahrzeugIdNr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFahrzeugIdNr() {
        return fahrzeugIdNr;
    }

    /**
     * Legt den Wert der fahrzeugIdNr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFahrzeugIdNr(String value) {
        this.fahrzeugIdNr = value;
    }

    /**
     * Ruft den Wert der kfzKennzeichen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKfzKennzeichen() {
        return kfzKennzeichen;
    }

    /**
     * Legt den Wert der kfzKennzeichen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKfzKennzeichen(String value) {
        this.kfzKennzeichen = value;
    }

    /**
     * Ruft den Wert der saisonkennzeichen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaisonkennzeichen() {
        return saisonkennzeichen;
    }

    /**
     * Legt den Wert der saisonkennzeichen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaisonkennzeichen(String value) {
        this.saisonkennzeichen = value;
    }

    /**
     * Ruft den Wert der wechselkennzeichen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWechselkennzeichen() {
        return wechselkennzeichen;
    }

    /**
     * Legt den Wert der wechselkennzeichen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWechselkennzeichen(String value) {
        this.wechselkennzeichen = value;
    }

    /**
     * Ruft den Wert der landZulassung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLandZulassung() {
        return landZulassung;
    }

    /**
     * Legt den Wert der landZulassung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLandZulassung(String value) {
        this.landZulassung = value;
    }

    /**
     * Ruft den Wert der getriebe-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetriebe() {
        return getriebe;
    }

    /**
     * Legt den Wert der getriebe-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetriebe(String value) {
        this.getriebe = value;
    }

    /**
     * Ruft den Wert der motorart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotorart() {
        return motorart;
    }

    /**
     * Legt den Wert der motorart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotorart(String value) {
        this.motorart = value;
    }

    /**
     * Ruft den Wert der farbe-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFarbe() {
        return farbe;
    }

    /**
     * Legt den Wert der farbe-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFarbe(String value) {
        this.farbe = value;
    }

    /**
     * Ruft den Wert der erstzulassung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErstzulassung() {
        return erstzulassung;
    }

    /**
     * Legt den Wert der erstzulassung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErstzulassung(String value) {
        this.erstzulassung = value;
    }

    /**
     * Ruft den Wert der kmStand-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.KmStand }
     *     
     */
    public KfzDaten.KmStand getKmStand() {
        return kmStand;
    }

    /**
     * Legt den Wert der kmStand-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.KmStand }
     *     
     */
    public void setKmStand(KfzDaten.KmStand value) {
        this.kmStand = value;
    }

    /**
     * Ruft den Wert der kmErmittlung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKmErmittlung() {
        return kmErmittlung;
    }

    /**
     * Legt den Wert der kmErmittlung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKmErmittlung(String value) {
        this.kmErmittlung = value;
    }

    /**
     * Ruft den Wert der laufleistung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLaufleistung() {
        return laufleistung;
    }

    /**
     * Legt den Wert der laufleistung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLaufleistung(String value) {
        this.laufleistung = value;
    }

    /**
     * Ruft den Wert der datumHU-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatumHU() {
        return datumHU;
    }

    /**
     * Legt den Wert der datumHU-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatumHU(String value) {
        this.datumHU = value;
    }

    /**
     * Ruft den Wert der datumASU-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatumASU() {
        return datumASU;
    }

    /**
     * Legt den Wert der datumASU-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatumASU(String value) {
        this.datumASU = value;
    }

    /**
     * Ruft den Wert der profilVL-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.ProfilVL }
     *     
     */
    public KfzDaten.ProfilVL getProfilVL() {
        return profilVL;
    }

    /**
     * Legt den Wert der profilVL-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.ProfilVL }
     *     
     */
    public void setProfilVL(KfzDaten.ProfilVL value) {
        this.profilVL = value;
    }

    /**
     * Ruft den Wert der profilVR-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.ProfilVR }
     *     
     */
    public KfzDaten.ProfilVR getProfilVR() {
        return profilVR;
    }

    /**
     * Legt den Wert der profilVR-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.ProfilVR }
     *     
     */
    public void setProfilVR(KfzDaten.ProfilVR value) {
        this.profilVR = value;
    }

    /**
     * Ruft den Wert der profilHL-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.ProfilHL }
     *     
     */
    public KfzDaten.ProfilHL getProfilHL() {
        return profilHL;
    }

    /**
     * Legt den Wert der profilHL-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.ProfilHL }
     *     
     */
    public void setProfilHL(KfzDaten.ProfilHL value) {
        this.profilHL = value;
    }

    /**
     * Ruft den Wert der profilHR-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.ProfilHR }
     *     
     */
    public KfzDaten.ProfilHR getProfilHR() {
        return profilHR;
    }

    /**
     * Legt den Wert der profilHR-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.ProfilHR }
     *     
     */
    public void setProfilHR(KfzDaten.ProfilHR value) {
        this.profilHR = value;
    }

    /**
     * Ruft den Wert der profilRes-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.ProfilRes }
     *     
     */
    public KfzDaten.ProfilRes getProfilRes() {
        return profilRes;
    }

    /**
     * Legt den Wert der profilRes-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.ProfilRes }
     *     
     */
    public void setProfilRes(KfzDaten.ProfilRes value) {
        this.profilRes = value;
    }

    /**
     * Ruft den Wert der artReserverad-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.ArtReserverad }
     *     
     */
    public KfzDaten.ArtReserverad getArtReserverad() {
        return artReserverad;
    }

    /**
     * Legt den Wert der artReserverad-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.ArtReserverad }
     *     
     */
    public void setArtReserverad(KfzDaten.ArtReserverad value) {
        this.artReserverad = value;
    }

    /**
     * Ruft den Wert der zustimmungFH-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZustimmungFH() {
        return zustimmungFH;
    }

    /**
     * Legt den Wert der zustimmungFH-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZustimmungFH(String value) {
        this.zustimmungFH = value;
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
     *     {@link KfzDaten.ProzentsatzMWSt }
     *     
     */
    public KfzDaten.ProzentsatzMWSt getProzentsatzMWSt() {
        return prozentsatzMWSt;
    }

    /**
     * Legt den Wert der prozentsatzMWSt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.ProzentsatzMWSt }
     *     
     */
    public void setProzentsatzMWSt(KfzDaten.ProzentsatzMWSt value) {
        this.prozentsatzMWSt = value;
    }

    /**
     * Ruft den Wert der wiederbeschaffungswert-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.Wiederbeschaffungswert }
     *     
     */
    public KfzDaten.Wiederbeschaffungswert getWiederbeschaffungswert() {
        return wiederbeschaffungswert;
    }

    /**
     * Legt den Wert der wiederbeschaffungswert-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.Wiederbeschaffungswert }
     *     
     */
    public void setWiederbeschaffungswert(KfzDaten.Wiederbeschaffungswert value) {
        this.wiederbeschaffungswert = value;
    }

    /**
     * Ruft den Wert der wiederbeschaffungswertNetto-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.WiederbeschaffungswertNetto }
     *     
     */
    public KfzDaten.WiederbeschaffungswertNetto getWiederbeschaffungswertNetto() {
        return wiederbeschaffungswertNetto;
    }

    /**
     * Legt den Wert der wiederbeschaffungswertNetto-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.WiederbeschaffungswertNetto }
     *     
     */
    public void setWiederbeschaffungswertNetto(KfzDaten.WiederbeschaffungswertNetto value) {
        this.wiederbeschaffungswertNetto = value;
    }

    /**
     * Ruft den Wert der vorschaeden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVorschaeden() {
        return vorschaeden;
    }

    /**
     * Legt den Wert der vorschaeden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVorschaeden(String value) {
        this.vorschaeden = value;
    }

    /**
     * Ruft den Wert der zuordnungNutzungsausfall-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZuordnungNutzungsausfall() {
        return zuordnungNutzungsausfall;
    }

    /**
     * Legt den Wert der zuordnungNutzungsausfall-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZuordnungNutzungsausfall(String value) {
        this.zuordnungNutzungsausfall = value;
    }

    /**
     * Ruft den Wert der fuehrerschein-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.Fuehrerschein }
     *     
     */
    public KfzDaten.Fuehrerschein getFuehrerschein() {
        return fuehrerschein;
    }

    /**
     * Legt den Wert der fuehrerschein-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.Fuehrerschein }
     *     
     */
    public void setFuehrerschein(KfzDaten.Fuehrerschein value) {
        this.fuehrerschein = value;
    }

    /**
     * Ruft den Wert der edvBewertungsschluesselnr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEDVBewertungsschluesselnr() {
        return edvBewertungsschluesselnr;
    }

    /**
     * Legt den Wert der edvBewertungsschluesselnr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEDVBewertungsschluesselnr(String value) {
        this.edvBewertungsschluesselnr = value;
    }

    /**
     * Ruft den Wert der edvBewertungsschluesselnrArt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KfzDaten.EDVBewertungsschluesselnrArt }
     *     
     */
    public KfzDaten.EDVBewertungsschluesselnrArt getEDVBewertungsschluesselnrArt() {
        return edvBewertungsschluesselnrArt;
    }

    /**
     * Legt den Wert der edvBewertungsschluesselnrArt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KfzDaten.EDVBewertungsschluesselnrArt }
     *     
     */
    public void setEDVBewertungsschluesselnrArt(KfzDaten.EDVBewertungsschluesselnrArt value) {
        this.edvBewertungsschluesselnrArt = value;
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
            return "4210";
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
    public static class AnzSitze {

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
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>ArtDesReserveradesTyp">
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
    public static class ArtReserverad {

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
     *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>EDV-SchluesselTyp">
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
    public static class EDVBewertungsschluesselnrArt {

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
     *         &lt;element name="Vorhanden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
     *         &lt;element name="Klasse" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="4"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Ausstellungsdatum" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="8"/>
     *               &lt;minLength value="8"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Entzugsdatum" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="8"/>
     *               &lt;minLength value="8"/>
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
        "vorhanden",
        "klasse",
        "ausstellungsdatum",
        "entzugsdatum"
    })
    public static class Fuehrerschein {

        @XmlElement(name = "Vorhanden")
        protected String vorhanden;
        @XmlElement(name = "Klasse")
        protected String klasse;
        @XmlElement(name = "Ausstellungsdatum")
        protected String ausstellungsdatum;
        @XmlElement(name = "Entzugsdatum")
        protected String entzugsdatum;

        /**
         * Ruft den Wert der vorhanden-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVorhanden() {
            return vorhanden;
        }

        /**
         * Legt den Wert der vorhanden-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVorhanden(String value) {
            this.vorhanden = value;
        }

        /**
         * Ruft den Wert der klasse-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKlasse() {
            return klasse;
        }

        /**
         * Legt den Wert der klasse-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKlasse(String value) {
            this.klasse = value;
        }

        /**
         * Ruft den Wert der ausstellungsdatum-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAusstellungsdatum() {
            return ausstellungsdatum;
        }

        /**
         * Legt den Wert der ausstellungsdatum-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAusstellungsdatum(String value) {
            this.ausstellungsdatum = value;
        }

        /**
         * Ruft den Wert der entzugsdatum-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEntzugsdatum() {
            return entzugsdatum;
        }

        /**
         * Legt den Wert der entzugsdatum-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEntzugsdatum(String value) {
            this.entzugsdatum = value;
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
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="9999.9"/>
     *               &lt;totalDigits value="5"/>
     *               &lt;fractionDigits value="1"/>
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
    public static class GesGewicht {

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
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Wert" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               &lt;minInclusive value="0"/>
     *               &lt;totalDigits value="6"/>
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
    public static class Hubraum {

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
     *         &lt;element name="Adresskennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp"/>
     *         &lt;element name="Partnerreferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
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
        "adresskennzeichen",
        "partnerreferenz"
    })
    public static class IdentifikationPartner {

        @XmlElement(name = "Adresskennzeichen", required = true)
        protected String adresskennzeichen;
        @XmlElement(name = "Partnerreferenz")
        protected String partnerreferenz;

        /**
         * Ruft den Wert der adresskennzeichen-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAdresskennzeichen() {
            return adresskennzeichen;
        }

        /**
         * Legt den Wert der adresskennzeichen-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAdresskennzeichen(String value) {
            this.adresskennzeichen = value;
        }

        /**
         * Ruft den Wert der partnerreferenz-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPartnerreferenz() {
            return partnerreferenz;
        }

        /**
         * Legt den Wert der partnerreferenz-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPartnerreferenz(String value) {
            this.partnerreferenz = value;
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
     *               &lt;totalDigits value="7"/>
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
    public static class KmStand {

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
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="9999.9"/>
     *               &lt;totalDigits value="5"/>
     *               &lt;fractionDigits value="1"/>
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
    public static class Leistung {

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
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Wert" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="99.9"/>
     *               &lt;totalDigits value="3"/>
     *               &lt;fractionDigits value="1"/>
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
    public static class ProfilHL {

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
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Wert" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="99.9"/>
     *               &lt;totalDigits value="3"/>
     *               &lt;fractionDigits value="1"/>
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
    public static class ProfilHR {

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
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Wert" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="99.9"/>
     *               &lt;totalDigits value="3"/>
     *               &lt;fractionDigits value="1"/>
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
    public static class ProfilRes {

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
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Wert" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="99.9"/>
     *               &lt;totalDigits value="3"/>
     *               &lt;fractionDigits value="1"/>
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
    public static class ProfilVL {

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
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Wert" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="99.9"/>
     *               &lt;totalDigits value="3"/>
     *               &lt;fractionDigits value="1"/>
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
    public static class ProfilVR {

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
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Wert" minOccurs="0">
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
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Wert" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="9999999999.99"/>
     *               &lt;totalDigits value="12"/>
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
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="9999999999.99"/>
     *               &lt;totalDigits value="12"/>
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
    public static class WiederbeschaffungswertNetto {

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
