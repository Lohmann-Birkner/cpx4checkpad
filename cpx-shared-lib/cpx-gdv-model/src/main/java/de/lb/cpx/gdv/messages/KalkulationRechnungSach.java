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
 *         &lt;element name="lfdNrBeschaedigtenSache" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="0"/>
 *               &lt;totalDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Abrechnungsart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}AbrechnungsartSachTyp"/>
 *         &lt;element name="Schadenart" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}SchadenstatusTyp" minOccurs="0"/>
 *         &lt;element name="Reparaturausfuehrung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ReparaturausfuehrungTyp" minOccurs="0"/>
 *         &lt;element name="Ergebnistyp" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ErgebnistypTyp"/>
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
 *         &lt;element name="Reparatur">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Brutto">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Netto">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Zahlungsempfaenger" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
 *                             &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Arbeitslohn" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Brutto" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Netto" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Zahlungsempfaenger" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
 *                             &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ErsatzteilMaterialkosten" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Brutto" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Netto" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Zahlungsempfaenger" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp" minOccurs="0"/>
 *                             &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="NeupreisAmSchadentag" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Brutto" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Netto" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="NeupreisAmAnschaffungstag" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Brutto" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Netto" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="NPS" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>NPSTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Wiederbeschaffungswert" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="KostenBrutto" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="KostenNetto" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Wiederbeschaffungsmoeglichkeit" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="WiederbeschaffungDurchDL" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="AlsNeuteilNochImHandel" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="ZeitwertNetto" minOccurs="0">
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
 *         &lt;element name="NebenkostenSonstiges" minOccurs="0">
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
 *         &lt;element name="Abzug" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Ersatzteile" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Vorschaeden" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Sonstiges" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Gesamt" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Wertminderung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinOffenTyp" minOccurs="0"/>
 *         &lt;element name="BetragWertminderung" minOccurs="0">
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
 *         &lt;element name="KostenpauschaleInWE" minOccurs="0">
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
 *         &lt;element name="Restwert" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Brutto" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Netto" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="9999999999.99"/>
 *                                   &lt;totalDigits value="12"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Erstellungsdatum" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;minLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Vorschaden" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="Anzahl-Vorbesitzer" minOccurs="0">
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
 *         &lt;element name="PlausibilitaetMitSchadenhergang" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}JaNeinTyp" minOccurs="0"/>
 *         &lt;element name="ZeitwertBruttoinWE" minOccurs="0">
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
 *                   &lt;element name="Indikator">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>IndikatorTyp">
 *                           &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Begruendung" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Begruendung2" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="BesondereAusstattungBeschaedigtenSache1" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="BesondereAusstattungBeschaedigtenSache2" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4303">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="4"/>
 *             &lt;minLength value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Versionsnummer" fixed="002">
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
    "abrechnungsart",
    "schadenart",
    "reparaturausfuehrung",
    "ergebnistyp",
    "waehrungsschluessel",
    "prozentsatzMWSt",
    "reparatur",
    "arbeitslohn",
    "ersatzteilMaterialkosten",
    "neupreisAmSchadentag",
    "neupreisAmAnschaffungstag",
    "nps",
    "wiederbeschaffungswert",
    "wiederbeschaffungsmoeglichkeit",
    "wiederbeschaffungDurchDL",
    "alsNeuteilNochImHandel",
    "zeitwertNetto",
    "nebenkostenSonstiges",
    "abzug",
    "wertminderung",
    "betragWertminderung",
    "kostenpauschaleInWE",
    "restwert",
    "erstellungsdatum",
    "vorschaden",
    "anzahlVorbesitzer",
    "plausibilitaetMitSchadenhergang",
    "zeitwertBruttoinWE",
    "begruendung",
    "begruendung2",
    "besondereAusstattungBeschaedigtenSache1",
    "besondereAusstattungBeschaedigtenSache2"
})
@XmlRootElement(name = "KalkulationRechnungSach")
public class KalkulationRechnungSach {

    protected BigInteger lfdNrBeschaedigtenSache;
    @XmlElement(name = "Abrechnungsart", required = true)
    protected String abrechnungsart;
    @XmlElement(name = "Schadenart")
    protected String schadenart;
    @XmlElement(name = "Reparaturausfuehrung")
    protected String reparaturausfuehrung;
    @XmlElement(name = "Ergebnistyp", required = true)
    protected String ergebnistyp;
    @XmlElement(name = "Waehrungsschluessel", required = true)
    protected String waehrungsschluessel;
    @XmlElement(name = "ProzentsatzMWSt", required = true)
    protected KalkulationRechnungSach.ProzentsatzMWSt prozentsatzMWSt;
    @XmlElement(name = "Reparatur", required = true)
    protected KalkulationRechnungSach.Reparatur reparatur;
    @XmlElement(name = "Arbeitslohn")
    protected KalkulationRechnungSach.Arbeitslohn arbeitslohn;
    @XmlElement(name = "ErsatzteilMaterialkosten")
    protected KalkulationRechnungSach.ErsatzteilMaterialkosten ersatzteilMaterialkosten;
    @XmlElement(name = "NeupreisAmSchadentag")
    protected KalkulationRechnungSach.NeupreisAmSchadentag neupreisAmSchadentag;
    @XmlElement(name = "NeupreisAmAnschaffungstag")
    protected KalkulationRechnungSach.NeupreisAmAnschaffungstag neupreisAmAnschaffungstag;
    @XmlElement(name = "NPS")
    protected KalkulationRechnungSach.NPS nps;
    @XmlElement(name = "Wiederbeschaffungswert")
    protected KalkulationRechnungSach.Wiederbeschaffungswert wiederbeschaffungswert;
    @XmlElement(name = "Wiederbeschaffungsmoeglichkeit")
    protected String wiederbeschaffungsmoeglichkeit;
    @XmlElement(name = "WiederbeschaffungDurchDL")
    protected String wiederbeschaffungDurchDL;
    @XmlElement(name = "AlsNeuteilNochImHandel")
    protected String alsNeuteilNochImHandel;
    @XmlElement(name = "ZeitwertNetto")
    protected KalkulationRechnungSach.ZeitwertNetto zeitwertNetto;
    @XmlElement(name = "NebenkostenSonstiges")
    protected KalkulationRechnungSach.NebenkostenSonstiges nebenkostenSonstiges;
    @XmlElement(name = "Abzug")
    protected KalkulationRechnungSach.Abzug abzug;
    @XmlElement(name = "Wertminderung")
    protected String wertminderung;
    @XmlElement(name = "BetragWertminderung")
    protected KalkulationRechnungSach.BetragWertminderung betragWertminderung;
    @XmlElement(name = "KostenpauschaleInWE")
    protected KalkulationRechnungSach.KostenpauschaleInWE kostenpauschaleInWE;
    @XmlElement(name = "Restwert")
    protected KalkulationRechnungSach.Restwert restwert;
    @XmlElement(name = "Erstellungsdatum")
    protected String erstellungsdatum;
    @XmlElement(name = "Vorschaden")
    protected String vorschaden;
    @XmlElement(name = "Anzahl-Vorbesitzer")
    protected KalkulationRechnungSach.AnzahlVorbesitzer anzahlVorbesitzer;
    @XmlElement(name = "PlausibilitaetMitSchadenhergang")
    protected String plausibilitaetMitSchadenhergang;
    @XmlElement(name = "ZeitwertBruttoinWE")
    protected KalkulationRechnungSach.ZeitwertBruttoinWE zeitwertBruttoinWE;
    @XmlElement(name = "Begruendung")
    protected KalkulationRechnungSach.Begruendung begruendung;
    @XmlElement(name = "Begruendung2")
    protected KalkulationRechnungSach.Begruendung2 begruendung2;
    @XmlElement(name = "BesondereAusstattungBeschaedigtenSache1")
    protected KalkulationRechnungSach.BesondereAusstattungBeschaedigtenSache1 besondereAusstattungBeschaedigtenSache1;
    @XmlElement(name = "BesondereAusstattungBeschaedigtenSache2")
    protected KalkulationRechnungSach.BesondereAusstattungBeschaedigtenSache2 besondereAusstattungBeschaedigtenSache2;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der lfdNrBeschaedigtenSache-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLfdNrBeschaedigtenSache() {
        return lfdNrBeschaedigtenSache;
    }

    /**
     * Legt den Wert der lfdNrBeschaedigtenSache-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLfdNrBeschaedigtenSache(BigInteger value) {
        this.lfdNrBeschaedigtenSache = value;
    }

    /**
     * Ruft den Wert der abrechnungsart-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbrechnungsart() {
        return abrechnungsart;
    }

    /**
     * Legt den Wert der abrechnungsart-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbrechnungsart(String value) {
        this.abrechnungsart = value;
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
     * Ruft den Wert der reparaturausfuehrung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReparaturausfuehrung() {
        return reparaturausfuehrung;
    }

    /**
     * Legt den Wert der reparaturausfuehrung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReparaturausfuehrung(String value) {
        this.reparaturausfuehrung = value;
    }

    /**
     * Ruft den Wert der ergebnistyp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErgebnistyp() {
        return ergebnistyp;
    }

    /**
     * Legt den Wert der ergebnistyp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErgebnistyp(String value) {
        this.ergebnistyp = value;
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
     *     {@link KalkulationRechnungSach.ProzentsatzMWSt }
     *     
     */
    public KalkulationRechnungSach.ProzentsatzMWSt getProzentsatzMWSt() {
        return prozentsatzMWSt;
    }

    /**
     * Legt den Wert der prozentsatzMWSt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.ProzentsatzMWSt }
     *     
     */
    public void setProzentsatzMWSt(KalkulationRechnungSach.ProzentsatzMWSt value) {
        this.prozentsatzMWSt = value;
    }

    /**
     * Ruft den Wert der reparatur-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.Reparatur }
     *     
     */
    public KalkulationRechnungSach.Reparatur getReparatur() {
        return reparatur;
    }

    /**
     * Legt den Wert der reparatur-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.Reparatur }
     *     
     */
    public void setReparatur(KalkulationRechnungSach.Reparatur value) {
        this.reparatur = value;
    }

    /**
     * Ruft den Wert der arbeitslohn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.Arbeitslohn }
     *     
     */
    public KalkulationRechnungSach.Arbeitslohn getArbeitslohn() {
        return arbeitslohn;
    }

    /**
     * Legt den Wert der arbeitslohn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.Arbeitslohn }
     *     
     */
    public void setArbeitslohn(KalkulationRechnungSach.Arbeitslohn value) {
        this.arbeitslohn = value;
    }

    /**
     * Ruft den Wert der ersatzteilMaterialkosten-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.ErsatzteilMaterialkosten }
     *     
     */
    public KalkulationRechnungSach.ErsatzteilMaterialkosten getErsatzteilMaterialkosten() {
        return ersatzteilMaterialkosten;
    }

    /**
     * Legt den Wert der ersatzteilMaterialkosten-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.ErsatzteilMaterialkosten }
     *     
     */
    public void setErsatzteilMaterialkosten(KalkulationRechnungSach.ErsatzteilMaterialkosten value) {
        this.ersatzteilMaterialkosten = value;
    }

    /**
     * Ruft den Wert der neupreisAmSchadentag-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.NeupreisAmSchadentag }
     *     
     */
    public KalkulationRechnungSach.NeupreisAmSchadentag getNeupreisAmSchadentag() {
        return neupreisAmSchadentag;
    }

    /**
     * Legt den Wert der neupreisAmSchadentag-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.NeupreisAmSchadentag }
     *     
     */
    public void setNeupreisAmSchadentag(KalkulationRechnungSach.NeupreisAmSchadentag value) {
        this.neupreisAmSchadentag = value;
    }

    /**
     * Ruft den Wert der neupreisAmAnschaffungstag-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.NeupreisAmAnschaffungstag }
     *     
     */
    public KalkulationRechnungSach.NeupreisAmAnschaffungstag getNeupreisAmAnschaffungstag() {
        return neupreisAmAnschaffungstag;
    }

    /**
     * Legt den Wert der neupreisAmAnschaffungstag-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.NeupreisAmAnschaffungstag }
     *     
     */
    public void setNeupreisAmAnschaffungstag(KalkulationRechnungSach.NeupreisAmAnschaffungstag value) {
        this.neupreisAmAnschaffungstag = value;
    }

    /**
     * Ruft den Wert der nps-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.NPS }
     *     
     */
    public KalkulationRechnungSach.NPS getNPS() {
        return nps;
    }

    /**
     * Legt den Wert der nps-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.NPS }
     *     
     */
    public void setNPS(KalkulationRechnungSach.NPS value) {
        this.nps = value;
    }

    /**
     * Ruft den Wert der wiederbeschaffungswert-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.Wiederbeschaffungswert }
     *     
     */
    public KalkulationRechnungSach.Wiederbeschaffungswert getWiederbeschaffungswert() {
        return wiederbeschaffungswert;
    }

    /**
     * Legt den Wert der wiederbeschaffungswert-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.Wiederbeschaffungswert }
     *     
     */
    public void setWiederbeschaffungswert(KalkulationRechnungSach.Wiederbeschaffungswert value) {
        this.wiederbeschaffungswert = value;
    }

    /**
     * Ruft den Wert der wiederbeschaffungsmoeglichkeit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWiederbeschaffungsmoeglichkeit() {
        return wiederbeschaffungsmoeglichkeit;
    }

    /**
     * Legt den Wert der wiederbeschaffungsmoeglichkeit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWiederbeschaffungsmoeglichkeit(String value) {
        this.wiederbeschaffungsmoeglichkeit = value;
    }

    /**
     * Ruft den Wert der wiederbeschaffungDurchDL-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWiederbeschaffungDurchDL() {
        return wiederbeschaffungDurchDL;
    }

    /**
     * Legt den Wert der wiederbeschaffungDurchDL-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWiederbeschaffungDurchDL(String value) {
        this.wiederbeschaffungDurchDL = value;
    }

    /**
     * Ruft den Wert der alsNeuteilNochImHandel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlsNeuteilNochImHandel() {
        return alsNeuteilNochImHandel;
    }

    /**
     * Legt den Wert der alsNeuteilNochImHandel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlsNeuteilNochImHandel(String value) {
        this.alsNeuteilNochImHandel = value;
    }

    /**
     * Ruft den Wert der zeitwertNetto-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.ZeitwertNetto }
     *     
     */
    public KalkulationRechnungSach.ZeitwertNetto getZeitwertNetto() {
        return zeitwertNetto;
    }

    /**
     * Legt den Wert der zeitwertNetto-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.ZeitwertNetto }
     *     
     */
    public void setZeitwertNetto(KalkulationRechnungSach.ZeitwertNetto value) {
        this.zeitwertNetto = value;
    }

    /**
     * Ruft den Wert der nebenkostenSonstiges-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.NebenkostenSonstiges }
     *     
     */
    public KalkulationRechnungSach.NebenkostenSonstiges getNebenkostenSonstiges() {
        return nebenkostenSonstiges;
    }

    /**
     * Legt den Wert der nebenkostenSonstiges-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.NebenkostenSonstiges }
     *     
     */
    public void setNebenkostenSonstiges(KalkulationRechnungSach.NebenkostenSonstiges value) {
        this.nebenkostenSonstiges = value;
    }

    /**
     * Ruft den Wert der abzug-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.Abzug }
     *     
     */
    public KalkulationRechnungSach.Abzug getAbzug() {
        return abzug;
    }

    /**
     * Legt den Wert der abzug-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.Abzug }
     *     
     */
    public void setAbzug(KalkulationRechnungSach.Abzug value) {
        this.abzug = value;
    }

    /**
     * Ruft den Wert der wertminderung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWertminderung() {
        return wertminderung;
    }

    /**
     * Legt den Wert der wertminderung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWertminderung(String value) {
        this.wertminderung = value;
    }

    /**
     * Ruft den Wert der betragWertminderung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.BetragWertminderung }
     *     
     */
    public KalkulationRechnungSach.BetragWertminderung getBetragWertminderung() {
        return betragWertminderung;
    }

    /**
     * Legt den Wert der betragWertminderung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.BetragWertminderung }
     *     
     */
    public void setBetragWertminderung(KalkulationRechnungSach.BetragWertminderung value) {
        this.betragWertminderung = value;
    }

    /**
     * Ruft den Wert der kostenpauschaleInWE-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.KostenpauschaleInWE }
     *     
     */
    public KalkulationRechnungSach.KostenpauschaleInWE getKostenpauschaleInWE() {
        return kostenpauschaleInWE;
    }

    /**
     * Legt den Wert der kostenpauschaleInWE-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.KostenpauschaleInWE }
     *     
     */
    public void setKostenpauschaleInWE(KalkulationRechnungSach.KostenpauschaleInWE value) {
        this.kostenpauschaleInWE = value;
    }

    /**
     * Ruft den Wert der restwert-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.Restwert }
     *     
     */
    public KalkulationRechnungSach.Restwert getRestwert() {
        return restwert;
    }

    /**
     * Legt den Wert der restwert-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.Restwert }
     *     
     */
    public void setRestwert(KalkulationRechnungSach.Restwert value) {
        this.restwert = value;
    }

    /**
     * Ruft den Wert der erstellungsdatum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErstellungsdatum() {
        return erstellungsdatum;
    }

    /**
     * Legt den Wert der erstellungsdatum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErstellungsdatum(String value) {
        this.erstellungsdatum = value;
    }

    /**
     * Ruft den Wert der vorschaden-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVorschaden() {
        return vorschaden;
    }

    /**
     * Legt den Wert der vorschaden-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVorschaden(String value) {
        this.vorschaden = value;
    }

    /**
     * Ruft den Wert der anzahlVorbesitzer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.AnzahlVorbesitzer }
     *     
     */
    public KalkulationRechnungSach.AnzahlVorbesitzer getAnzahlVorbesitzer() {
        return anzahlVorbesitzer;
    }

    /**
     * Legt den Wert der anzahlVorbesitzer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.AnzahlVorbesitzer }
     *     
     */
    public void setAnzahlVorbesitzer(KalkulationRechnungSach.AnzahlVorbesitzer value) {
        this.anzahlVorbesitzer = value;
    }

    /**
     * Ruft den Wert der plausibilitaetMitSchadenhergang-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlausibilitaetMitSchadenhergang() {
        return plausibilitaetMitSchadenhergang;
    }

    /**
     * Legt den Wert der plausibilitaetMitSchadenhergang-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlausibilitaetMitSchadenhergang(String value) {
        this.plausibilitaetMitSchadenhergang = value;
    }

    /**
     * Ruft den Wert der zeitwertBruttoinWE-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.ZeitwertBruttoinWE }
     *     
     */
    public KalkulationRechnungSach.ZeitwertBruttoinWE getZeitwertBruttoinWE() {
        return zeitwertBruttoinWE;
    }

    /**
     * Legt den Wert der zeitwertBruttoinWE-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.ZeitwertBruttoinWE }
     *     
     */
    public void setZeitwertBruttoinWE(KalkulationRechnungSach.ZeitwertBruttoinWE value) {
        this.zeitwertBruttoinWE = value;
    }

    /**
     * Ruft den Wert der begruendung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.Begruendung }
     *     
     */
    public KalkulationRechnungSach.Begruendung getBegruendung() {
        return begruendung;
    }

    /**
     * Legt den Wert der begruendung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.Begruendung }
     *     
     */
    public void setBegruendung(KalkulationRechnungSach.Begruendung value) {
        this.begruendung = value;
    }

    /**
     * Ruft den Wert der begruendung2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.Begruendung2 }
     *     
     */
    public KalkulationRechnungSach.Begruendung2 getBegruendung2() {
        return begruendung2;
    }

    /**
     * Legt den Wert der begruendung2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.Begruendung2 }
     *     
     */
    public void setBegruendung2(KalkulationRechnungSach.Begruendung2 value) {
        this.begruendung2 = value;
    }

    /**
     * Ruft den Wert der besondereAusstattungBeschaedigtenSache1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.BesondereAusstattungBeschaedigtenSache1 }
     *     
     */
    public KalkulationRechnungSach.BesondereAusstattungBeschaedigtenSache1 getBesondereAusstattungBeschaedigtenSache1() {
        return besondereAusstattungBeschaedigtenSache1;
    }

    /**
     * Legt den Wert der besondereAusstattungBeschaedigtenSache1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.BesondereAusstattungBeschaedigtenSache1 }
     *     
     */
    public void setBesondereAusstattungBeschaedigtenSache1(KalkulationRechnungSach.BesondereAusstattungBeschaedigtenSache1 value) {
        this.besondereAusstattungBeschaedigtenSache1 = value;
    }

    /**
     * Ruft den Wert der besondereAusstattungBeschaedigtenSache2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KalkulationRechnungSach.BesondereAusstattungBeschaedigtenSache2 }
     *     
     */
    public KalkulationRechnungSach.BesondereAusstattungBeschaedigtenSache2 getBesondereAusstattungBeschaedigtenSache2() {
        return besondereAusstattungBeschaedigtenSache2;
    }

    /**
     * Legt den Wert der besondereAusstattungBeschaedigtenSache2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KalkulationRechnungSach.BesondereAusstattungBeschaedigtenSache2 }
     *     
     */
    public void setBesondereAusstattungBeschaedigtenSache2(KalkulationRechnungSach.BesondereAusstattungBeschaedigtenSache2 value) {
        this.besondereAusstattungBeschaedigtenSache2 = value;
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
            return "4303";
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
            return "002";
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
     *         &lt;element name="Ersatzteile" minOccurs="0">
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
     *         &lt;element name="Vorschaeden" minOccurs="0">
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
     *         &lt;element name="Sonstiges" minOccurs="0">
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
     *         &lt;element name="Gesamt" minOccurs="0">
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
        "ersatzteile",
        "vorschaeden",
        "sonstiges",
        "gesamt"
    })
    public static class Abzug {

        @XmlElement(name = "Ersatzteile")
        protected KalkulationRechnungSach.Abzug.Ersatzteile ersatzteile;
        @XmlElement(name = "Vorschaeden")
        protected KalkulationRechnungSach.Abzug.Vorschaeden vorschaeden;
        @XmlElement(name = "Sonstiges")
        protected KalkulationRechnungSach.Abzug.Sonstiges sonstiges;
        @XmlElement(name = "Gesamt")
        protected KalkulationRechnungSach.Abzug.Gesamt gesamt;

        /**
         * Ruft den Wert der ersatzteile-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.Abzug.Ersatzteile }
         *     
         */
        public KalkulationRechnungSach.Abzug.Ersatzteile getErsatzteile() {
            return ersatzteile;
        }

        /**
         * Legt den Wert der ersatzteile-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.Abzug.Ersatzteile }
         *     
         */
        public void setErsatzteile(KalkulationRechnungSach.Abzug.Ersatzteile value) {
            this.ersatzteile = value;
        }

        /**
         * Ruft den Wert der vorschaeden-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.Abzug.Vorschaeden }
         *     
         */
        public KalkulationRechnungSach.Abzug.Vorschaeden getVorschaeden() {
            return vorschaeden;
        }

        /**
         * Legt den Wert der vorschaeden-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.Abzug.Vorschaeden }
         *     
         */
        public void setVorschaeden(KalkulationRechnungSach.Abzug.Vorschaeden value) {
            this.vorschaeden = value;
        }

        /**
         * Ruft den Wert der sonstiges-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.Abzug.Sonstiges }
         *     
         */
        public KalkulationRechnungSach.Abzug.Sonstiges getSonstiges() {
            return sonstiges;
        }

        /**
         * Legt den Wert der sonstiges-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.Abzug.Sonstiges }
         *     
         */
        public void setSonstiges(KalkulationRechnungSach.Abzug.Sonstiges value) {
            this.sonstiges = value;
        }

        /**
         * Ruft den Wert der gesamt-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.Abzug.Gesamt }
         *     
         */
        public KalkulationRechnungSach.Abzug.Gesamt getGesamt() {
            return gesamt;
        }

        /**
         * Legt den Wert der gesamt-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.Abzug.Gesamt }
         *     
         */
        public void setGesamt(KalkulationRechnungSach.Abzug.Gesamt value) {
            this.gesamt = value;
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
        public static class Ersatzteile {

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
        public static class Gesamt {

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
        public static class Sonstiges {

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
        public static class Vorschaeden {

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
    public static class AnzahlVorbesitzer {

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
     *         &lt;element name="Brutto" minOccurs="0">
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
     *         &lt;element name="Netto" minOccurs="0">
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
     *         &lt;element name="Zahlungsempfaenger" minOccurs="0">
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
        "brutto",
        "netto",
        "zahlungsempfaenger"
    })
    public static class Arbeitslohn {

        @XmlElement(name = "Brutto")
        protected KalkulationRechnungSach.Arbeitslohn.Brutto brutto;
        @XmlElement(name = "Netto")
        protected KalkulationRechnungSach.Arbeitslohn.Netto netto;
        @XmlElement(name = "Zahlungsempfaenger")
        protected KalkulationRechnungSach.Arbeitslohn.Zahlungsempfaenger zahlungsempfaenger;

        /**
         * Ruft den Wert der brutto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.Arbeitslohn.Brutto }
         *     
         */
        public KalkulationRechnungSach.Arbeitslohn.Brutto getBrutto() {
            return brutto;
        }

        /**
         * Legt den Wert der brutto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.Arbeitslohn.Brutto }
         *     
         */
        public void setBrutto(KalkulationRechnungSach.Arbeitslohn.Brutto value) {
            this.brutto = value;
        }

        /**
         * Ruft den Wert der netto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.Arbeitslohn.Netto }
         *     
         */
        public KalkulationRechnungSach.Arbeitslohn.Netto getNetto() {
            return netto;
        }

        /**
         * Legt den Wert der netto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.Arbeitslohn.Netto }
         *     
         */
        public void setNetto(KalkulationRechnungSach.Arbeitslohn.Netto value) {
            this.netto = value;
        }

        /**
         * Ruft den Wert der zahlungsempfaenger-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.Arbeitslohn.Zahlungsempfaenger }
         *     
         */
        public KalkulationRechnungSach.Arbeitslohn.Zahlungsempfaenger getZahlungsempfaenger() {
            return zahlungsempfaenger;
        }

        /**
         * Legt den Wert der zahlungsempfaenger-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.Arbeitslohn.Zahlungsempfaenger }
         *     
         */
        public void setZahlungsempfaenger(KalkulationRechnungSach.Arbeitslohn.Zahlungsempfaenger value) {
            this.zahlungsempfaenger = value;
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
        public static class Brutto {

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
        public static class Netto {

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
        public static class Zahlungsempfaenger {

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
    public static class Begruendung
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
    public static class Begruendung2
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
    public static class BesondereAusstattungBeschaedigtenSache1
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
    public static class BesondereAusstattungBeschaedigtenSache2
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
    public static class BetragWertminderung {

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
     *         &lt;element name="Brutto" minOccurs="0">
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
     *         &lt;element name="Netto" minOccurs="0">
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
     *         &lt;element name="Zahlungsempfaenger" minOccurs="0">
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
        "brutto",
        "netto",
        "zahlungsempfaenger"
    })
    public static class ErsatzteilMaterialkosten {

        @XmlElement(name = "Brutto")
        protected KalkulationRechnungSach.ErsatzteilMaterialkosten.Brutto brutto;
        @XmlElement(name = "Netto")
        protected KalkulationRechnungSach.ErsatzteilMaterialkosten.Netto netto;
        @XmlElement(name = "Zahlungsempfaenger")
        protected KalkulationRechnungSach.ErsatzteilMaterialkosten.Zahlungsempfaenger zahlungsempfaenger;

        /**
         * Ruft den Wert der brutto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.ErsatzteilMaterialkosten.Brutto }
         *     
         */
        public KalkulationRechnungSach.ErsatzteilMaterialkosten.Brutto getBrutto() {
            return brutto;
        }

        /**
         * Legt den Wert der brutto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.ErsatzteilMaterialkosten.Brutto }
         *     
         */
        public void setBrutto(KalkulationRechnungSach.ErsatzteilMaterialkosten.Brutto value) {
            this.brutto = value;
        }

        /**
         * Ruft den Wert der netto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.ErsatzteilMaterialkosten.Netto }
         *     
         */
        public KalkulationRechnungSach.ErsatzteilMaterialkosten.Netto getNetto() {
            return netto;
        }

        /**
         * Legt den Wert der netto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.ErsatzteilMaterialkosten.Netto }
         *     
         */
        public void setNetto(KalkulationRechnungSach.ErsatzteilMaterialkosten.Netto value) {
            this.netto = value;
        }

        /**
         * Ruft den Wert der zahlungsempfaenger-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.ErsatzteilMaterialkosten.Zahlungsempfaenger }
         *     
         */
        public KalkulationRechnungSach.ErsatzteilMaterialkosten.Zahlungsempfaenger getZahlungsempfaenger() {
            return zahlungsempfaenger;
        }

        /**
         * Legt den Wert der zahlungsempfaenger-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.ErsatzteilMaterialkosten.Zahlungsempfaenger }
         *     
         */
        public void setZahlungsempfaenger(KalkulationRechnungSach.ErsatzteilMaterialkosten.Zahlungsempfaenger value) {
            this.zahlungsempfaenger = value;
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
        public static class Brutto {

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
        public static class Netto {

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
        public static class Zahlungsempfaenger {

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
    public static class KostenpauschaleInWE {

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
     *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>NPSTyp">
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
    public static class NPS {

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
    public static class NebenkostenSonstiges {

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
     *         &lt;element name="Brutto" minOccurs="0">
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
     *         &lt;element name="Netto" minOccurs="0">
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
        "brutto",
        "netto"
    })
    public static class NeupreisAmAnschaffungstag {

        @XmlElement(name = "Brutto")
        protected KalkulationRechnungSach.NeupreisAmAnschaffungstag.Brutto brutto;
        @XmlElement(name = "Netto")
        protected KalkulationRechnungSach.NeupreisAmAnschaffungstag.Netto netto;

        /**
         * Ruft den Wert der brutto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.NeupreisAmAnschaffungstag.Brutto }
         *     
         */
        public KalkulationRechnungSach.NeupreisAmAnschaffungstag.Brutto getBrutto() {
            return brutto;
        }

        /**
         * Legt den Wert der brutto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.NeupreisAmAnschaffungstag.Brutto }
         *     
         */
        public void setBrutto(KalkulationRechnungSach.NeupreisAmAnschaffungstag.Brutto value) {
            this.brutto = value;
        }

        /**
         * Ruft den Wert der netto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.NeupreisAmAnschaffungstag.Netto }
         *     
         */
        public KalkulationRechnungSach.NeupreisAmAnschaffungstag.Netto getNetto() {
            return netto;
        }

        /**
         * Legt den Wert der netto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.NeupreisAmAnschaffungstag.Netto }
         *     
         */
        public void setNetto(KalkulationRechnungSach.NeupreisAmAnschaffungstag.Netto value) {
            this.netto = value;
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
        public static class Brutto {

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
        public static class Netto {

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
     *         &lt;element name="Brutto" minOccurs="0">
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
     *         &lt;element name="Netto" minOccurs="0">
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
        "brutto",
        "netto"
    })
    public static class NeupreisAmSchadentag {

        @XmlElement(name = "Brutto")
        protected KalkulationRechnungSach.NeupreisAmSchadentag.Brutto brutto;
        @XmlElement(name = "Netto")
        protected KalkulationRechnungSach.NeupreisAmSchadentag.Netto netto;

        /**
         * Ruft den Wert der brutto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.NeupreisAmSchadentag.Brutto }
         *     
         */
        public KalkulationRechnungSach.NeupreisAmSchadentag.Brutto getBrutto() {
            return brutto;
        }

        /**
         * Legt den Wert der brutto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.NeupreisAmSchadentag.Brutto }
         *     
         */
        public void setBrutto(KalkulationRechnungSach.NeupreisAmSchadentag.Brutto value) {
            this.brutto = value;
        }

        /**
         * Ruft den Wert der netto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.NeupreisAmSchadentag.Netto }
         *     
         */
        public KalkulationRechnungSach.NeupreisAmSchadentag.Netto getNetto() {
            return netto;
        }

        /**
         * Legt den Wert der netto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.NeupreisAmSchadentag.Netto }
         *     
         */
        public void setNetto(KalkulationRechnungSach.NeupreisAmSchadentag.Netto value) {
            this.netto = value;
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
        public static class Brutto {

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
        public static class Netto {

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
     *         &lt;element name="Brutto">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert">
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
     *         &lt;element name="Netto">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert">
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
     *         &lt;element name="Zahlungsempfaenger" minOccurs="0">
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
        "brutto",
        "netto",
        "zahlungsempfaenger"
    })
    public static class Reparatur {

        @XmlElement(name = "Brutto", required = true)
        protected KalkulationRechnungSach.Reparatur.Brutto brutto;
        @XmlElement(name = "Netto", required = true)
        protected KalkulationRechnungSach.Reparatur.Netto netto;
        @XmlElement(name = "Zahlungsempfaenger")
        protected KalkulationRechnungSach.Reparatur.Zahlungsempfaenger zahlungsempfaenger;

        /**
         * Ruft den Wert der brutto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.Reparatur.Brutto }
         *     
         */
        public KalkulationRechnungSach.Reparatur.Brutto getBrutto() {
            return brutto;
        }

        /**
         * Legt den Wert der brutto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.Reparatur.Brutto }
         *     
         */
        public void setBrutto(KalkulationRechnungSach.Reparatur.Brutto value) {
            this.brutto = value;
        }

        /**
         * Ruft den Wert der netto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.Reparatur.Netto }
         *     
         */
        public KalkulationRechnungSach.Reparatur.Netto getNetto() {
            return netto;
        }

        /**
         * Legt den Wert der netto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.Reparatur.Netto }
         *     
         */
        public void setNetto(KalkulationRechnungSach.Reparatur.Netto value) {
            this.netto = value;
        }

        /**
         * Ruft den Wert der zahlungsempfaenger-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.Reparatur.Zahlungsempfaenger }
         *     
         */
        public KalkulationRechnungSach.Reparatur.Zahlungsempfaenger getZahlungsempfaenger() {
            return zahlungsempfaenger;
        }

        /**
         * Legt den Wert der zahlungsempfaenger-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.Reparatur.Zahlungsempfaenger }
         *     
         */
        public void setZahlungsempfaenger(KalkulationRechnungSach.Reparatur.Zahlungsempfaenger value) {
            this.zahlungsempfaenger = value;
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
        public static class Brutto {

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
         *         &lt;element name="Wert">
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
        public static class Netto {

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
        public static class Zahlungsempfaenger {

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
     *         &lt;element name="Brutto" minOccurs="0">
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
     *         &lt;element name="Netto" minOccurs="0">
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
        "brutto",
        "netto"
    })
    public static class Restwert {

        @XmlElement(name = "Brutto")
        protected KalkulationRechnungSach.Restwert.Brutto brutto;
        @XmlElement(name = "Netto")
        protected KalkulationRechnungSach.Restwert.Netto netto;

        /**
         * Ruft den Wert der brutto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.Restwert.Brutto }
         *     
         */
        public KalkulationRechnungSach.Restwert.Brutto getBrutto() {
            return brutto;
        }

        /**
         * Legt den Wert der brutto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.Restwert.Brutto }
         *     
         */
        public void setBrutto(KalkulationRechnungSach.Restwert.Brutto value) {
            this.brutto = value;
        }

        /**
         * Ruft den Wert der netto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.Restwert.Netto }
         *     
         */
        public KalkulationRechnungSach.Restwert.Netto getNetto() {
            return netto;
        }

        /**
         * Legt den Wert der netto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.Restwert.Netto }
         *     
         */
        public void setNetto(KalkulationRechnungSach.Restwert.Netto value) {
            this.netto = value;
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
        public static class Brutto {

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
        public static class Netto {

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
     *         &lt;element name="KostenBrutto" minOccurs="0">
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
     *         &lt;element name="KostenNetto" minOccurs="0">
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
        "kostenBrutto",
        "kostenNetto"
    })
    public static class Wiederbeschaffungswert {

        @XmlElement(name = "KostenBrutto")
        protected KalkulationRechnungSach.Wiederbeschaffungswert.KostenBrutto kostenBrutto;
        @XmlElement(name = "KostenNetto")
        protected KalkulationRechnungSach.Wiederbeschaffungswert.KostenNetto kostenNetto;

        /**
         * Ruft den Wert der kostenBrutto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.Wiederbeschaffungswert.KostenBrutto }
         *     
         */
        public KalkulationRechnungSach.Wiederbeschaffungswert.KostenBrutto getKostenBrutto() {
            return kostenBrutto;
        }

        /**
         * Legt den Wert der kostenBrutto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.Wiederbeschaffungswert.KostenBrutto }
         *     
         */
        public void setKostenBrutto(KalkulationRechnungSach.Wiederbeschaffungswert.KostenBrutto value) {
            this.kostenBrutto = value;
        }

        /**
         * Ruft den Wert der kostenNetto-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link KalkulationRechnungSach.Wiederbeschaffungswert.KostenNetto }
         *     
         */
        public KalkulationRechnungSach.Wiederbeschaffungswert.KostenNetto getKostenNetto() {
            return kostenNetto;
        }

        /**
         * Legt den Wert der kostenNetto-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.Wiederbeschaffungswert.KostenNetto }
         *     
         */
        public void setKostenNetto(KalkulationRechnungSach.Wiederbeschaffungswert.KostenNetto value) {
            this.kostenNetto = value;
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
        public static class KostenBrutto {

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
        public static class KostenNetto {

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
     *         &lt;element name="Indikator">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>IndikatorTyp">
     *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
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
        "wert",
        "indikator"
    })
    public static class ZeitwertBruttoinWE {

        @XmlElement(name = "Wert")
        protected BigDecimal wert;
        @XmlElement(name = "Indikator", required = true)
        protected KalkulationRechnungSach.ZeitwertBruttoinWE.Indikator indikator;

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
         *     {@link KalkulationRechnungSach.ZeitwertBruttoinWE.Indikator }
         *     
         */
        public KalkulationRechnungSach.ZeitwertBruttoinWE.Indikator getIndikator() {
            return indikator;
        }

        /**
         * Legt den Wert der indikator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link KalkulationRechnungSach.ZeitwertBruttoinWE.Indikator }
         *     
         */
        public void setIndikator(KalkulationRechnungSach.ZeitwertBruttoinWE.Indikator value) {
            this.indikator = value;
        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>IndikatorTyp">
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
        public static class Indikator {

            @XmlValue
            protected String value;
            @XmlAttribute(name = "Satzende")
            protected String satzende;

            /**
             * Gibt über das Vorzeichen der Zahl im vorangehenden Feld Auskunft bzw. zeigt an, dass das Feld beim Absender nicht belegt wurde.
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
    public static class ZeitwertNetto {

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
