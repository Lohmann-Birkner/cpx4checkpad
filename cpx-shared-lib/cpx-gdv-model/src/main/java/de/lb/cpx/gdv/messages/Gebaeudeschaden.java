//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.19 um 11:55:14 AM CEST 
//


package de.lb.cpx.gdv.messages;

import java.math.BigDecimal;
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
 *         &lt;element name="ErstellerGebaeudemeldung">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp"/>
 *                   &lt;element name="PartnerReferenz" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerReferenzTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Objektreferenzen" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="LfdNrSchadenverzeichnis" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="20"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="LNrObjektebene1" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="20"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TypObjektebene1" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ObjekttypTyp" minOccurs="0"/>
 *                   &lt;element name="BezeichnungObjektebene1" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="LNrObjektebene2" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="20"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TypObjektebene2" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ObjekttypTyp" minOccurs="0"/>
 *                   &lt;element name="BezeichnungObjektebene2" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="LNrObjektebene3" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="20"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="TypObjektebene3" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ObjekttypTyp" minOccurs="0"/>
 *                   &lt;element name="BezeichnungObjektebene3" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                           &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Objekteigenschaften" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="WEG" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ArtDerWEGTyp" minOccurs="0"/>
 *                   &lt;element name="Eigentumsverhaeltnis" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}EigentumsverhaeltnisTyp" minOccurs="0"/>
 *                   &lt;element name="PartnerrolleEigentumsverhaeltnis" minOccurs="0">
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
 *                   &lt;element name="Etage" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="20"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="ArtDecke" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ArtDeckeTyp" minOccurs="0"/>
 *                   &lt;element name="ArtWand" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}DeckenartTyp" minOccurs="0"/>
 *                   &lt;element name="ArtDeckenverkleidung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}DeckenverkleidungsartTyp" minOccurs="0"/>
 *                   &lt;element name="ArtWandverkleidung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WandverkleidungsartTyp" minOccurs="0"/>
 *                   &lt;element name="ArtUnterbodens" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}UnterbodenartTyp" minOccurs="0"/>
 *                   &lt;element name="ArtBedachung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BedachungsartTyp" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Masse" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="BreiteInM" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="HoeheInM" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="LaengeInM" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="UmfangInM" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
 *         &lt;element name="BetroffeneRaumGebaeudeteile" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="BodenflaecheInQm" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="DeckenflaecheInQm" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="WandflaecheInQm" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="SonstigeFlaecheInQm" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
 *                             &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="UmfangInM" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
 *                             &lt;element name="Indikator">
 *                               &lt;complexType>
 *                                 &lt;simpleContent>
 *                                   &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>IndikatorTyp">
 *                                     &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                   &lt;/extension>
 *                                 &lt;/simpleContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
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
 *         &lt;element name="Gewerk" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="LfdNrGewerk" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="10"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="SchadenGewerk" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="30"/>
 *                         &lt;minLength value="1"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="LfdNrLeistung" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BezeichnungLeistung" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Menge" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="PreisJeEinheitNetto" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="GesamtpreisJeLeistungNetto" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="GesamtpreisJeLeistungBrutto" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
 *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ZeitwertinProzent" minOccurs="0">
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
 *         &lt;element name="ZeitwertinWE" minOccurs="0">
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
 *         &lt;element name="Waehrungsschluessel">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>WaehrungsschluesselTyp">
 *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Satzart" fixed="4261">
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
    "erstellerGebaeudemeldung",
    "objektreferenzen",
    "objekteigenschaften",
    "masse",
    "betroffeneRaumGebaeudeteile",
    "gewerk",
    "lfdNrLeistung",
    "bezeichnungLeistung",
    "menge",
    "preisJeEinheitNetto",
    "gesamtpreisJeLeistungNetto",
    "gesamtpreisJeLeistungBrutto",
    "zeitwertinProzent",
    "zeitwertinWE",
    "waehrungsschluessel"
})
@XmlRootElement(name = "Gebaeudeschaden")
public class Gebaeudeschaden {

    @XmlElement(name = "ErstellerGebaeudemeldung", required = true)
    protected Gebaeudeschaden.ErstellerGebaeudemeldung erstellerGebaeudemeldung;
    @XmlElement(name = "Objektreferenzen")
    protected Gebaeudeschaden.Objektreferenzen objektreferenzen;
    @XmlElement(name = "Objekteigenschaften")
    protected Gebaeudeschaden.Objekteigenschaften objekteigenschaften;
    @XmlElement(name = "Masse")
    protected Gebaeudeschaden.Masse masse;
    @XmlElement(name = "BetroffeneRaumGebaeudeteile")
    protected Gebaeudeschaden.BetroffeneRaumGebaeudeteile betroffeneRaumGebaeudeteile;
    @XmlElement(name = "Gewerk")
    protected Gebaeudeschaden.Gewerk gewerk;
    @XmlElement(name = "LfdNrLeistung")
    protected String lfdNrLeistung;
    @XmlElement(name = "BezeichnungLeistung")
    protected Gebaeudeschaden.BezeichnungLeistung bezeichnungLeistung;
    @XmlElement(name = "Menge")
    protected Gebaeudeschaden.Menge menge;
    @XmlElement(name = "PreisJeEinheitNetto")
    protected Gebaeudeschaden.PreisJeEinheitNetto preisJeEinheitNetto;
    @XmlElement(name = "GesamtpreisJeLeistungNetto")
    protected Gebaeudeschaden.GesamtpreisJeLeistungNetto gesamtpreisJeLeistungNetto;
    @XmlElement(name = "GesamtpreisJeLeistungBrutto")
    protected Gebaeudeschaden.GesamtpreisJeLeistungBrutto gesamtpreisJeLeistungBrutto;
    @XmlElement(name = "ZeitwertinProzent")
    protected Gebaeudeschaden.ZeitwertinProzent zeitwertinProzent;
    @XmlElement(name = "ZeitwertinWE")
    protected Gebaeudeschaden.ZeitwertinWE zeitwertinWE;
    @XmlElement(name = "Waehrungsschluessel", required = true)
    protected Gebaeudeschaden.Waehrungsschluessel waehrungsschluessel;
    @XmlAttribute(name = "Satzart")
    protected String satzart;
    @XmlAttribute(name = "Versionsnummer")
    protected String versionsnummer;

    /**
     * Ruft den Wert der erstellerGebaeudemeldung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Gebaeudeschaden.ErstellerGebaeudemeldung }
     *     
     */
    public Gebaeudeschaden.ErstellerGebaeudemeldung getErstellerGebaeudemeldung() {
        return erstellerGebaeudemeldung;
    }

    /**
     * Legt den Wert der erstellerGebaeudemeldung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Gebaeudeschaden.ErstellerGebaeudemeldung }
     *     
     */
    public void setErstellerGebaeudemeldung(Gebaeudeschaden.ErstellerGebaeudemeldung value) {
        this.erstellerGebaeudemeldung = value;
    }

    /**
     * Ruft den Wert der objektreferenzen-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Gebaeudeschaden.Objektreferenzen }
     *     
     */
    public Gebaeudeschaden.Objektreferenzen getObjektreferenzen() {
        return objektreferenzen;
    }

    /**
     * Legt den Wert der objektreferenzen-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Gebaeudeschaden.Objektreferenzen }
     *     
     */
    public void setObjektreferenzen(Gebaeudeschaden.Objektreferenzen value) {
        this.objektreferenzen = value;
    }

    /**
     * Ruft den Wert der objekteigenschaften-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Gebaeudeschaden.Objekteigenschaften }
     *     
     */
    public Gebaeudeschaden.Objekteigenschaften getObjekteigenschaften() {
        return objekteigenschaften;
    }

    /**
     * Legt den Wert der objekteigenschaften-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Gebaeudeschaden.Objekteigenschaften }
     *     
     */
    public void setObjekteigenschaften(Gebaeudeschaden.Objekteigenschaften value) {
        this.objekteigenschaften = value;
    }

    /**
     * Ruft den Wert der masse-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Gebaeudeschaden.Masse }
     *     
     */
    public Gebaeudeschaden.Masse getMasse() {
        return masse;
    }

    /**
     * Legt den Wert der masse-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Gebaeudeschaden.Masse }
     *     
     */
    public void setMasse(Gebaeudeschaden.Masse value) {
        this.masse = value;
    }

    /**
     * Ruft den Wert der betroffeneRaumGebaeudeteile-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Gebaeudeschaden.BetroffeneRaumGebaeudeteile }
     *     
     */
    public Gebaeudeschaden.BetroffeneRaumGebaeudeteile getBetroffeneRaumGebaeudeteile() {
        return betroffeneRaumGebaeudeteile;
    }

    /**
     * Legt den Wert der betroffeneRaumGebaeudeteile-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Gebaeudeschaden.BetroffeneRaumGebaeudeteile }
     *     
     */
    public void setBetroffeneRaumGebaeudeteile(Gebaeudeschaden.BetroffeneRaumGebaeudeteile value) {
        this.betroffeneRaumGebaeudeteile = value;
    }

    /**
     * Ruft den Wert der gewerk-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Gebaeudeschaden.Gewerk }
     *     
     */
    public Gebaeudeschaden.Gewerk getGewerk() {
        return gewerk;
    }

    /**
     * Legt den Wert der gewerk-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Gebaeudeschaden.Gewerk }
     *     
     */
    public void setGewerk(Gebaeudeschaden.Gewerk value) {
        this.gewerk = value;
    }

    /**
     * Ruft den Wert der lfdNrLeistung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLfdNrLeistung() {
        return lfdNrLeistung;
    }

    /**
     * Legt den Wert der lfdNrLeistung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLfdNrLeistung(String value) {
        this.lfdNrLeistung = value;
    }

    /**
     * Ruft den Wert der bezeichnungLeistung-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Gebaeudeschaden.BezeichnungLeistung }
     *     
     */
    public Gebaeudeschaden.BezeichnungLeistung getBezeichnungLeistung() {
        return bezeichnungLeistung;
    }

    /**
     * Legt den Wert der bezeichnungLeistung-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Gebaeudeschaden.BezeichnungLeistung }
     *     
     */
    public void setBezeichnungLeistung(Gebaeudeschaden.BezeichnungLeistung value) {
        this.bezeichnungLeistung = value;
    }

    /**
     * Ruft den Wert der menge-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Gebaeudeschaden.Menge }
     *     
     */
    public Gebaeudeschaden.Menge getMenge() {
        return menge;
    }

    /**
     * Legt den Wert der menge-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Gebaeudeschaden.Menge }
     *     
     */
    public void setMenge(Gebaeudeschaden.Menge value) {
        this.menge = value;
    }

    /**
     * Ruft den Wert der preisJeEinheitNetto-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Gebaeudeschaden.PreisJeEinheitNetto }
     *     
     */
    public Gebaeudeschaden.PreisJeEinheitNetto getPreisJeEinheitNetto() {
        return preisJeEinheitNetto;
    }

    /**
     * Legt den Wert der preisJeEinheitNetto-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Gebaeudeschaden.PreisJeEinheitNetto }
     *     
     */
    public void setPreisJeEinheitNetto(Gebaeudeschaden.PreisJeEinheitNetto value) {
        this.preisJeEinheitNetto = value;
    }

    /**
     * Ruft den Wert der gesamtpreisJeLeistungNetto-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Gebaeudeschaden.GesamtpreisJeLeistungNetto }
     *     
     */
    public Gebaeudeschaden.GesamtpreisJeLeistungNetto getGesamtpreisJeLeistungNetto() {
        return gesamtpreisJeLeistungNetto;
    }

    /**
     * Legt den Wert der gesamtpreisJeLeistungNetto-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Gebaeudeschaden.GesamtpreisJeLeistungNetto }
     *     
     */
    public void setGesamtpreisJeLeistungNetto(Gebaeudeschaden.GesamtpreisJeLeistungNetto value) {
        this.gesamtpreisJeLeistungNetto = value;
    }

    /**
     * Ruft den Wert der gesamtpreisJeLeistungBrutto-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Gebaeudeschaden.GesamtpreisJeLeistungBrutto }
     *     
     */
    public Gebaeudeschaden.GesamtpreisJeLeistungBrutto getGesamtpreisJeLeistungBrutto() {
        return gesamtpreisJeLeistungBrutto;
    }

    /**
     * Legt den Wert der gesamtpreisJeLeistungBrutto-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Gebaeudeschaden.GesamtpreisJeLeistungBrutto }
     *     
     */
    public void setGesamtpreisJeLeistungBrutto(Gebaeudeschaden.GesamtpreisJeLeistungBrutto value) {
        this.gesamtpreisJeLeistungBrutto = value;
    }

    /**
     * Ruft den Wert der zeitwertinProzent-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Gebaeudeschaden.ZeitwertinProzent }
     *     
     */
    public Gebaeudeschaden.ZeitwertinProzent getZeitwertinProzent() {
        return zeitwertinProzent;
    }

    /**
     * Legt den Wert der zeitwertinProzent-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Gebaeudeschaden.ZeitwertinProzent }
     *     
     */
    public void setZeitwertinProzent(Gebaeudeschaden.ZeitwertinProzent value) {
        this.zeitwertinProzent = value;
    }

    /**
     * Ruft den Wert der zeitwertinWE-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Gebaeudeschaden.ZeitwertinWE }
     *     
     */
    public Gebaeudeschaden.ZeitwertinWE getZeitwertinWE() {
        return zeitwertinWE;
    }

    /**
     * Legt den Wert der zeitwertinWE-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Gebaeudeschaden.ZeitwertinWE }
     *     
     */
    public void setZeitwertinWE(Gebaeudeschaden.ZeitwertinWE value) {
        this.zeitwertinWE = value;
    }

    /**
     * Ruft den Wert der waehrungsschluessel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Gebaeudeschaden.Waehrungsschluessel }
     *     
     */
    public Gebaeudeschaden.Waehrungsschluessel getWaehrungsschluessel() {
        return waehrungsschluessel;
    }

    /**
     * Legt den Wert der waehrungsschluessel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Gebaeudeschaden.Waehrungsschluessel }
     *     
     */
    public void setWaehrungsschluessel(Gebaeudeschaden.Waehrungsschluessel value) {
        this.waehrungsschluessel = value;
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
            return "4261";
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
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="BodenflaecheInQm" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
     *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="DeckenflaecheInQm" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
     *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="WandflaecheInQm" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
     *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="SonstigeFlaecheInQm" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
     *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="UmfangInM" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
        "bodenflaecheInQm",
        "deckenflaecheInQm",
        "wandflaecheInQm",
        "sonstigeFlaecheInQm",
        "umfangInM"
    })
    public static class BetroffeneRaumGebaeudeteile {

        @XmlElement(name = "BodenflaecheInQm")
        protected Gebaeudeschaden.BetroffeneRaumGebaeudeteile.BodenflaecheInQm bodenflaecheInQm;
        @XmlElement(name = "DeckenflaecheInQm")
        protected Gebaeudeschaden.BetroffeneRaumGebaeudeteile.DeckenflaecheInQm deckenflaecheInQm;
        @XmlElement(name = "WandflaecheInQm")
        protected Gebaeudeschaden.BetroffeneRaumGebaeudeteile.WandflaecheInQm wandflaecheInQm;
        @XmlElement(name = "SonstigeFlaecheInQm")
        protected Gebaeudeschaden.BetroffeneRaumGebaeudeteile.SonstigeFlaecheInQm sonstigeFlaecheInQm;
        @XmlElement(name = "UmfangInM")
        protected Gebaeudeschaden.BetroffeneRaumGebaeudeteile.UmfangInM umfangInM;

        /**
         * Ruft den Wert der bodenflaecheInQm-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Gebaeudeschaden.BetroffeneRaumGebaeudeteile.BodenflaecheInQm }
         *     
         */
        public Gebaeudeschaden.BetroffeneRaumGebaeudeteile.BodenflaecheInQm getBodenflaecheInQm() {
            return bodenflaecheInQm;
        }

        /**
         * Legt den Wert der bodenflaecheInQm-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Gebaeudeschaden.BetroffeneRaumGebaeudeteile.BodenflaecheInQm }
         *     
         */
        public void setBodenflaecheInQm(Gebaeudeschaden.BetroffeneRaumGebaeudeteile.BodenflaecheInQm value) {
            this.bodenflaecheInQm = value;
        }

        /**
         * Ruft den Wert der deckenflaecheInQm-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Gebaeudeschaden.BetroffeneRaumGebaeudeteile.DeckenflaecheInQm }
         *     
         */
        public Gebaeudeschaden.BetroffeneRaumGebaeudeteile.DeckenflaecheInQm getDeckenflaecheInQm() {
            return deckenflaecheInQm;
        }

        /**
         * Legt den Wert der deckenflaecheInQm-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Gebaeudeschaden.BetroffeneRaumGebaeudeteile.DeckenflaecheInQm }
         *     
         */
        public void setDeckenflaecheInQm(Gebaeudeschaden.BetroffeneRaumGebaeudeteile.DeckenflaecheInQm value) {
            this.deckenflaecheInQm = value;
        }

        /**
         * Ruft den Wert der wandflaecheInQm-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Gebaeudeschaden.BetroffeneRaumGebaeudeteile.WandflaecheInQm }
         *     
         */
        public Gebaeudeschaden.BetroffeneRaumGebaeudeteile.WandflaecheInQm getWandflaecheInQm() {
            return wandflaecheInQm;
        }

        /**
         * Legt den Wert der wandflaecheInQm-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Gebaeudeschaden.BetroffeneRaumGebaeudeteile.WandflaecheInQm }
         *     
         */
        public void setWandflaecheInQm(Gebaeudeschaden.BetroffeneRaumGebaeudeteile.WandflaecheInQm value) {
            this.wandflaecheInQm = value;
        }

        /**
         * Ruft den Wert der sonstigeFlaecheInQm-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Gebaeudeschaden.BetroffeneRaumGebaeudeteile.SonstigeFlaecheInQm }
         *     
         */
        public Gebaeudeschaden.BetroffeneRaumGebaeudeteile.SonstigeFlaecheInQm getSonstigeFlaecheInQm() {
            return sonstigeFlaecheInQm;
        }

        /**
         * Legt den Wert der sonstigeFlaecheInQm-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Gebaeudeschaden.BetroffeneRaumGebaeudeteile.SonstigeFlaecheInQm }
         *     
         */
        public void setSonstigeFlaecheInQm(Gebaeudeschaden.BetroffeneRaumGebaeudeteile.SonstigeFlaecheInQm value) {
            this.sonstigeFlaecheInQm = value;
        }

        /**
         * Ruft den Wert der umfangInM-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Gebaeudeschaden.BetroffeneRaumGebaeudeteile.UmfangInM }
         *     
         */
        public Gebaeudeschaden.BetroffeneRaumGebaeudeteile.UmfangInM getUmfangInM() {
            return umfangInM;
        }

        /**
         * Legt den Wert der umfangInM-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Gebaeudeschaden.BetroffeneRaumGebaeudeteile.UmfangInM }
         *     
         */
        public void setUmfangInM(Gebaeudeschaden.BetroffeneRaumGebaeudeteile.UmfangInM value) {
            this.umfangInM = value;
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
         *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
        public static class BodenflaecheInQm {

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
         *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
        public static class DeckenflaecheInQm {

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
         *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
        public static class SonstigeFlaecheInQm {

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
         *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
        public static class UmfangInM {

            @XmlElement(name = "Wert")
            protected BigDecimal wert;
            @XmlElement(name = "Indikator", required = true)
            protected Gebaeudeschaden.BetroffeneRaumGebaeudeteile.UmfangInM.Indikator indikator;

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
             *     {@link Gebaeudeschaden.BetroffeneRaumGebaeudeteile.UmfangInM.Indikator }
             *     
             */
            public Gebaeudeschaden.BetroffeneRaumGebaeudeteile.UmfangInM.Indikator getIndikator() {
                return indikator;
            }

            /**
             * Legt den Wert der indikator-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Gebaeudeschaden.BetroffeneRaumGebaeudeteile.UmfangInM.Indikator }
             *     
             */
            public void setIndikator(Gebaeudeschaden.BetroffeneRaumGebaeudeteile.UmfangInM.Indikator value) {
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
         *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
        public static class WandflaecheInQm {

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
    public static class BezeichnungLeistung
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
     *         &lt;element name="AdressKennzeichen" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}PartnerrolleTyp"/>
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
    public static class ErstellerGebaeudemeldung {

        @XmlElement(name = "AdressKennzeichen", required = true)
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
     *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
    public static class GesamtpreisJeLeistungBrutto {

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
     *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
    public static class GesamtpreisJeLeistungNetto {

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
     *         &lt;element name="LfdNrGewerk" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="10"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="SchadenGewerk" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
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
        "lfdNrGewerk",
        "schadenGewerk"
    })
    public static class Gewerk {

        @XmlElement(name = "LfdNrGewerk")
        protected String lfdNrGewerk;
        @XmlElement(name = "SchadenGewerk")
        protected String schadenGewerk;

        /**
         * Ruft den Wert der lfdNrGewerk-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLfdNrGewerk() {
            return lfdNrGewerk;
        }

        /**
         * Legt den Wert der lfdNrGewerk-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLfdNrGewerk(String value) {
            this.lfdNrGewerk = value;
        }

        /**
         * Ruft den Wert der schadenGewerk-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSchadenGewerk() {
            return schadenGewerk;
        }

        /**
         * Legt den Wert der schadenGewerk-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSchadenGewerk(String value) {
            this.schadenGewerk = value;
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
     *         &lt;element name="BreiteInM" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
     *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="HoeheInM" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
     *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="LaengeInM" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
     *                   &lt;element name="Indikator" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}IndikatorTyp"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="UmfangInM" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
        "breiteInM",
        "hoeheInM",
        "laengeInM",
        "umfangInM"
    })
    public static class Masse {

        @XmlElement(name = "BreiteInM")
        protected Gebaeudeschaden.Masse.BreiteInM breiteInM;
        @XmlElement(name = "HoeheInM")
        protected Gebaeudeschaden.Masse.HoeheInM hoeheInM;
        @XmlElement(name = "LaengeInM")
        protected Gebaeudeschaden.Masse.LaengeInM laengeInM;
        @XmlElement(name = "UmfangInM")
        protected Gebaeudeschaden.Masse.UmfangInM umfangInM;

        /**
         * Ruft den Wert der breiteInM-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Gebaeudeschaden.Masse.BreiteInM }
         *     
         */
        public Gebaeudeschaden.Masse.BreiteInM getBreiteInM() {
            return breiteInM;
        }

        /**
         * Legt den Wert der breiteInM-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Gebaeudeschaden.Masse.BreiteInM }
         *     
         */
        public void setBreiteInM(Gebaeudeschaden.Masse.BreiteInM value) {
            this.breiteInM = value;
        }

        /**
         * Ruft den Wert der hoeheInM-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Gebaeudeschaden.Masse.HoeheInM }
         *     
         */
        public Gebaeudeschaden.Masse.HoeheInM getHoeheInM() {
            return hoeheInM;
        }

        /**
         * Legt den Wert der hoeheInM-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Gebaeudeschaden.Masse.HoeheInM }
         *     
         */
        public void setHoeheInM(Gebaeudeschaden.Masse.HoeheInM value) {
            this.hoeheInM = value;
        }

        /**
         * Ruft den Wert der laengeInM-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Gebaeudeschaden.Masse.LaengeInM }
         *     
         */
        public Gebaeudeschaden.Masse.LaengeInM getLaengeInM() {
            return laengeInM;
        }

        /**
         * Legt den Wert der laengeInM-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Gebaeudeschaden.Masse.LaengeInM }
         *     
         */
        public void setLaengeInM(Gebaeudeschaden.Masse.LaengeInM value) {
            this.laengeInM = value;
        }

        /**
         * Ruft den Wert der umfangInM-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Gebaeudeschaden.Masse.UmfangInM }
         *     
         */
        public Gebaeudeschaden.Masse.UmfangInM getUmfangInM() {
            return umfangInM;
        }

        /**
         * Legt den Wert der umfangInM-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Gebaeudeschaden.Masse.UmfangInM }
         *     
         */
        public void setUmfangInM(Gebaeudeschaden.Masse.UmfangInM value) {
            this.umfangInM = value;
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
         *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
        public static class BreiteInM {

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
         *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
        public static class HoeheInM {

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
         *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
        public static class LaengeInM {

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
         *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
        public static class UmfangInM {

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
     *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
    public static class Menge {

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
     *         &lt;element name="WEG" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ArtDerWEGTyp" minOccurs="0"/>
     *         &lt;element name="Eigentumsverhaeltnis" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}EigentumsverhaeltnisTyp" minOccurs="0"/>
     *         &lt;element name="PartnerrolleEigentumsverhaeltnis" minOccurs="0">
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
     *         &lt;element name="Etage" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="20"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="ArtDecke" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ArtDeckeTyp" minOccurs="0"/>
     *         &lt;element name="ArtWand" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}DeckenartTyp" minOccurs="0"/>
     *         &lt;element name="ArtDeckenverkleidung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}DeckenverkleidungsartTyp" minOccurs="0"/>
     *         &lt;element name="ArtWandverkleidung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}WandverkleidungsartTyp" minOccurs="0"/>
     *         &lt;element name="ArtUnterbodens" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}UnterbodenartTyp" minOccurs="0"/>
     *         &lt;element name="ArtBedachung" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}BedachungsartTyp" minOccurs="0"/>
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
        "weg",
        "eigentumsverhaeltnis",
        "partnerrolleEigentumsverhaeltnis",
        "etage",
        "artDecke",
        "artWand",
        "artDeckenverkleidung",
        "artWandverkleidung",
        "artUnterbodens",
        "artBedachung"
    })
    public static class Objekteigenschaften {

        @XmlElement(name = "WEG")
        protected String weg;
        @XmlElement(name = "Eigentumsverhaeltnis")
        protected String eigentumsverhaeltnis;
        @XmlElement(name = "PartnerrolleEigentumsverhaeltnis")
        protected Gebaeudeschaden.Objekteigenschaften.PartnerrolleEigentumsverhaeltnis partnerrolleEigentumsverhaeltnis;
        @XmlElement(name = "Etage")
        protected String etage;
        @XmlElement(name = "ArtDecke")
        protected String artDecke;
        @XmlElement(name = "ArtWand")
        protected String artWand;
        @XmlElement(name = "ArtDeckenverkleidung")
        protected String artDeckenverkleidung;
        @XmlElement(name = "ArtWandverkleidung")
        protected String artWandverkleidung;
        @XmlElement(name = "ArtUnterbodens")
        protected String artUnterbodens;
        @XmlElement(name = "ArtBedachung")
        protected String artBedachung;

        /**
         * Ruft den Wert der weg-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getWEG() {
            return weg;
        }

        /**
         * Legt den Wert der weg-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setWEG(String value) {
            this.weg = value;
        }

        /**
         * Ruft den Wert der eigentumsverhaeltnis-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEigentumsverhaeltnis() {
            return eigentumsverhaeltnis;
        }

        /**
         * Legt den Wert der eigentumsverhaeltnis-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEigentumsverhaeltnis(String value) {
            this.eigentumsverhaeltnis = value;
        }

        /**
         * Ruft den Wert der partnerrolleEigentumsverhaeltnis-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Gebaeudeschaden.Objekteigenschaften.PartnerrolleEigentumsverhaeltnis }
         *     
         */
        public Gebaeudeschaden.Objekteigenschaften.PartnerrolleEigentumsverhaeltnis getPartnerrolleEigentumsverhaeltnis() {
            return partnerrolleEigentumsverhaeltnis;
        }

        /**
         * Legt den Wert der partnerrolleEigentumsverhaeltnis-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Gebaeudeschaden.Objekteigenschaften.PartnerrolleEigentumsverhaeltnis }
         *     
         */
        public void setPartnerrolleEigentumsverhaeltnis(Gebaeudeschaden.Objekteigenschaften.PartnerrolleEigentumsverhaeltnis value) {
            this.partnerrolleEigentumsverhaeltnis = value;
        }

        /**
         * Ruft den Wert der etage-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEtage() {
            return etage;
        }

        /**
         * Legt den Wert der etage-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEtage(String value) {
            this.etage = value;
        }

        /**
         * Ruft den Wert der artDecke-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getArtDecke() {
            return artDecke;
        }

        /**
         * Legt den Wert der artDecke-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setArtDecke(String value) {
            this.artDecke = value;
        }

        /**
         * Ruft den Wert der artWand-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getArtWand() {
            return artWand;
        }

        /**
         * Legt den Wert der artWand-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setArtWand(String value) {
            this.artWand = value;
        }

        /**
         * Ruft den Wert der artDeckenverkleidung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getArtDeckenverkleidung() {
            return artDeckenverkleidung;
        }

        /**
         * Legt den Wert der artDeckenverkleidung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setArtDeckenverkleidung(String value) {
            this.artDeckenverkleidung = value;
        }

        /**
         * Ruft den Wert der artWandverkleidung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getArtWandverkleidung() {
            return artWandverkleidung;
        }

        /**
         * Legt den Wert der artWandverkleidung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setArtWandverkleidung(String value) {
            this.artWandverkleidung = value;
        }

        /**
         * Ruft den Wert der artUnterbodens-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getArtUnterbodens() {
            return artUnterbodens;
        }

        /**
         * Legt den Wert der artUnterbodens-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setArtUnterbodens(String value) {
            this.artUnterbodens = value;
        }

        /**
         * Ruft den Wert der artBedachung-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getArtBedachung() {
            return artBedachung;
        }

        /**
         * Legt den Wert der artBedachung-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setArtBedachung(String value) {
            this.artBedachung = value;
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
        public static class PartnerrolleEigentumsverhaeltnis {

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
     *         &lt;element name="LfdNrSchadenverzeichnis" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="20"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="LNrObjektebene1" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="20"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TypObjektebene1" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ObjekttypTyp" minOccurs="0"/>
     *         &lt;element name="BezeichnungObjektebene1" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="LNrObjektebene2" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="20"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TypObjektebene2" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ObjekttypTyp" minOccurs="0"/>
     *         &lt;element name="BezeichnungObjektebene2" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="30"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="LNrObjektebene3" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="20"/>
     *               &lt;minLength value="1"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="TypObjektebene3" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}ObjekttypTyp" minOccurs="0"/>
     *         &lt;element name="BezeichnungObjektebene3" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;restriction base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>SatzendeTyp">
     *                 &lt;attribute name="Satzende" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
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
        "lfdNrSchadenverzeichnis",
        "lNrObjektebene1",
        "typObjektebene1",
        "bezeichnungObjektebene1",
        "lNrObjektebene2",
        "typObjektebene2",
        "bezeichnungObjektebene2",
        "lNrObjektebene3",
        "typObjektebene3",
        "bezeichnungObjektebene3"
    })
    public static class Objektreferenzen {

        @XmlElement(name = "LfdNrSchadenverzeichnis")
        protected String lfdNrSchadenverzeichnis;
        @XmlElement(name = "LNrObjektebene1")
        protected String lNrObjektebene1;
        @XmlElement(name = "TypObjektebene1")
        protected String typObjektebene1;
        @XmlElement(name = "BezeichnungObjektebene1")
        protected String bezeichnungObjektebene1;
        @XmlElement(name = "LNrObjektebene2")
        protected String lNrObjektebene2;
        @XmlElement(name = "TypObjektebene2")
        protected String typObjektebene2;
        @XmlElement(name = "BezeichnungObjektebene2")
        protected String bezeichnungObjektebene2;
        @XmlElement(name = "LNrObjektebene3")
        protected String lNrObjektebene3;
        @XmlElement(name = "TypObjektebene3")
        protected String typObjektebene3;
        @XmlElement(name = "BezeichnungObjektebene3")
        protected Gebaeudeschaden.Objektreferenzen.BezeichnungObjektebene3 bezeichnungObjektebene3;

        /**
         * Ruft den Wert der lfdNrSchadenverzeichnis-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLfdNrSchadenverzeichnis() {
            return lfdNrSchadenverzeichnis;
        }

        /**
         * Legt den Wert der lfdNrSchadenverzeichnis-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLfdNrSchadenverzeichnis(String value) {
            this.lfdNrSchadenverzeichnis = value;
        }

        /**
         * Ruft den Wert der lNrObjektebene1-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLNrObjektebene1() {
            return lNrObjektebene1;
        }

        /**
         * Legt den Wert der lNrObjektebene1-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLNrObjektebene1(String value) {
            this.lNrObjektebene1 = value;
        }

        /**
         * Ruft den Wert der typObjektebene1-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTypObjektebene1() {
            return typObjektebene1;
        }

        /**
         * Legt den Wert der typObjektebene1-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTypObjektebene1(String value) {
            this.typObjektebene1 = value;
        }

        /**
         * Ruft den Wert der bezeichnungObjektebene1-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBezeichnungObjektebene1() {
            return bezeichnungObjektebene1;
        }

        /**
         * Legt den Wert der bezeichnungObjektebene1-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBezeichnungObjektebene1(String value) {
            this.bezeichnungObjektebene1 = value;
        }

        /**
         * Ruft den Wert der lNrObjektebene2-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLNrObjektebene2() {
            return lNrObjektebene2;
        }

        /**
         * Legt den Wert der lNrObjektebene2-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLNrObjektebene2(String value) {
            this.lNrObjektebene2 = value;
        }

        /**
         * Ruft den Wert der typObjektebene2-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTypObjektebene2() {
            return typObjektebene2;
        }

        /**
         * Legt den Wert der typObjektebene2-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTypObjektebene2(String value) {
            this.typObjektebene2 = value;
        }

        /**
         * Ruft den Wert der bezeichnungObjektebene2-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBezeichnungObjektebene2() {
            return bezeichnungObjektebene2;
        }

        /**
         * Legt den Wert der bezeichnungObjektebene2-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBezeichnungObjektebene2(String value) {
            this.bezeichnungObjektebene2 = value;
        }

        /**
         * Ruft den Wert der lNrObjektebene3-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLNrObjektebene3() {
            return lNrObjektebene3;
        }

        /**
         * Legt den Wert der lNrObjektebene3-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLNrObjektebene3(String value) {
            this.lNrObjektebene3 = value;
        }

        /**
         * Ruft den Wert der typObjektebene3-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTypObjektebene3() {
            return typObjektebene3;
        }

        /**
         * Legt den Wert der typObjektebene3-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTypObjektebene3(String value) {
            this.typObjektebene3 = value;
        }

        /**
         * Ruft den Wert der bezeichnungObjektebene3-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Gebaeudeschaden.Objektreferenzen.BezeichnungObjektebene3 }
         *     
         */
        public Gebaeudeschaden.Objektreferenzen.BezeichnungObjektebene3 getBezeichnungObjektebene3() {
            return bezeichnungObjektebene3;
        }

        /**
         * Legt den Wert der bezeichnungObjektebene3-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Gebaeudeschaden.Objektreferenzen.BezeichnungObjektebene3 }
         *     
         */
        public void setBezeichnungObjektebene3(Gebaeudeschaden.Objektreferenzen.BezeichnungObjektebene3 value) {
            this.bezeichnungObjektebene3 = value;
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
        public static class BezeichnungObjektebene3
            extends SatzendeTyp
        {


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
     *         &lt;element name="Wert" type="{http://www.gdv-online.de/snetz/namespaces/KSN/release2013}Fliess102Typ" minOccurs="0"/>
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
    public static class PreisJeEinheitNetto {

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
     *     &lt;extension base="&lt;http://www.gdv-online.de/snetz/namespaces/KSN/release2013>WaehrungsschluesselTyp">
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
    public static class Waehrungsschluessel {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "Satzende")
        protected String satzende;

        /**
         * Währungsschlüssel gemäß ISO-Code siehe Anlage 3 (z.B. EUR...) 
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
    public static class ZeitwertinProzent {

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
    public static class ZeitwertinWE {

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
