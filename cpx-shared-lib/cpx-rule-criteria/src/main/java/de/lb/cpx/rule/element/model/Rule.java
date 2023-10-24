/*
 * Copyright (c) 2018 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.10.18 um 03:03:05 PM CEST 
//
package de.lb.cpx.rule.element.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java-Klasse f�r anonymous complex type.
 *
 * <p>
 * Das folgende Schemafragment gibt den erwarteten Content an, der in dieser
 * Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}rules_element"/>
 *         &lt;element ref="{}suggestions"/>
 *       &lt;/sequence>
 *       &lt;attribute name="caption" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="entgelt" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="errror_type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="feegroup" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="from" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="hasinterval" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="interval_from" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="interval_to" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="massnumber" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="medtype" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="number" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="profit" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="rid" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="role" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="rules_notice" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rules_number" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rules_year" type="{http://www.w3.org/2001/XMLSchema}short" />
 *       &lt;attribute name="text" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="to" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="typ" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="unchange" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="used" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="visible" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "rulesElement",
    "suggestions",
    "risks"
})
@XmlRootElement(name = "rule")
public class Rule extends Object implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "rules_element", required = true)
    protected RulesElement rulesElement;
    @XmlElement(required = true)
    protected Suggestions suggestions;
    @XmlElement(name = "risk")
    protected Risk risks;
    @XmlAttribute(name = "caption")
    protected String caption;
    @XmlAttribute(name = "entgelt")
    protected String entgelt;
    @XmlAttribute(name = "errror_type")
    protected String errrorType;
    @XmlAttribute(name = "feegroup")
    protected String feegroup;
    @XmlAttribute(name = "from")
    protected String from = null;
    @XmlAttribute(name = "hasinterval")
    protected String hasinterval;
    @XmlAttribute(name = "interval_from")
    protected String intervalFrom;
    @XmlAttribute(name = "interval_to")
    protected String intervalTo;
    @XmlAttribute(name = "massnumber")
    protected String massnumber;
    @XmlAttribute(name = "medtype")
    protected String medtype;
    @XmlAttribute(name = "number")
    protected String number = null;
    @XmlAttribute(name = "profit")
    protected Float profit;
    @XmlAttribute(name = "rid")
    protected Integer rid;
    @XmlAttribute(name = "role")
    protected String role;
    @XmlAttribute(name = "rules_notice")
    protected String rulesNotice;
    @XmlAttribute(name = "rules_number")
    protected String rulesNumber;
    @XmlAttribute(name = "rules_year")
    protected Short rulesYear;
    @XmlAttribute(name = "text")
    protected String text;
    @XmlAttribute(name = "to")
    protected String to = null;
    @XmlAttribute(name = "typ")
    protected String typ;
    @XmlAttribute(name = "unchange")
    protected Integer unchange;
    @XmlAttribute(name = "used")
    protected String used;
    @XmlAttribute(name = "visible")
    protected String visible;

    public Rule() {
//        number = "";
//        rulesNotice = "";
//        errrorType = "";
    }

    /**
     * Ruft den Wert der rulesElement-Eigenschaft ab.
     *
     * @return possible object is {@link RulesElement }
     *
     */
    public RulesElement getRulesElement() {
        return rulesElement;
    }

    /**
     * Legt den Wert der rulesElement-Eigenschaft fest.
     *
     * @param value allowed object is {@link RulesElement }
     *
     */
    public void setRulesElement(RulesElement value) {
        this.rulesElement = value;
    }

    /**
     * Ruft den Wert der suggestions-Eigenschaft ab.
     *
     * @return possible object is {@link Suggestions }
     *
     */
    public Suggestions getSuggestions() {
        return suggestions;
    }

    /**
     * Legt den Wert der suggestions-Eigenschaft fest.
     *
     * @param value allowed object is {@link Suggestions }
     *
     */
    public void setSuggestions(Suggestions value) {
        this.suggestions = value;
    }
    
    /**
     * Ruft den Wert der risks-Eigenschaft ab.
     *
     * @return possible object is {@link Risk }
     *
     */
    public Risk getRisks() {
        return risks;
    }

    /**
     * Legt den Wert der risks-Eigenschaft fest.
     *
     * @param value allowed object is {@link Risk }
     *
     */
    public void setRisks(Risk value) {
        this.risks = value;
    }
    
    /**
     * Ruft den Wert der caption-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Legt den Wert der caption-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setCaption(String value) {
        this.caption = value;
    }

    /**
     * Ruft den Wert der entgelt-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getEntgelt() {
        return entgelt;
    }

    /**
     * Legt den Wert der entgelt-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setEntgelt(String value) {
        this.entgelt = value;
    }

    /**
     * Ruft den Wert der errrorType-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getErrrorType() {
        return errrorType;
    }

    /**
     * Legt den Wert der errrorType-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setErrrorType(String value) {
        this.errrorType = value;
    }

    /**
     * Ruft den Wert der feegroup-Eigenschaft ab.
     *
     * @return possible object is {@link Byte }
     *
     */
    public String getFeegroup() {
        return feegroup;
    }

    /**
     * Legt den Wert der feegroup-Eigenschaft fest.
     *
     * @param value allowed object is {@link Byte }
     *
     */
    public void setFeegroup(String value) {
        this.feegroup = value;
    }

    /**
     * Ruft den Wert der from-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getFrom() {
        return from;
    }

    /**
     * Legt den Wert der from-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setFrom(String value) {
        this.from = value;
    }

    /**
     * Ruft den Wert der hasinterval-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getHasinterval() {
        return hasinterval;
    }

    /**
     * Legt den Wert der hasinterval-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setHasinterval(String value) {
        this.hasinterval = value;
    }

    /**
     * Ruft den Wert der intervalFrom-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getIntervalFrom() {
        return intervalFrom;
    }

    /**
     * Legt den Wert der intervalFrom-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setIntervalFrom(String value) {
        this.intervalFrom = value;
    }

    /**
     * Ruft den Wert der intervalTo-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getIntervalTo() {
        return intervalTo;
    }

    /**
     * Legt den Wert der intervalTo-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setIntervalTo(String value) {
        this.intervalTo = value;
    }

    /**
     * Ruft den Wert der massnumber-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getMassnumber() {
        return massnumber;
    }

    /**
     * Legt den Wert der massnumber-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setMassnumber(String value) {
        this.massnumber = value;
    }

    /**
     * Ruft den Wert der medtype-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getMedtype() {
        return medtype;
    }

    /**
     * Legt den Wert der medtype-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setMedtype(String value) {
        this.medtype = value;
    }

    /**
     * Ruft den Wert der number-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getNumber() {
        return number;
    }

    /**
     * Legt den Wert der number-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setNumber(String value) {
        this.number = value;
    }

    /**
     * Ruft den Wert der profit-Eigenschaft ab.
     *
     * @return possible object is {@link Float }
     *
     */
    public Float getProfit() {
        return profit;
    }

    /**
     * Legt den Wert der profit-Eigenschaft fest.
     *
     * @param value allowed object is {@link Float }
     *
     */
    public void setProfit(Float value) {
        this.profit = value;
    }

    /**
     * Ruft den Wert der rid-Eigenschaft ab.
     *
     * @return possible object is {@link Integer }
     *
     */
    public Integer getRid() {
        return rid;
    }

    /**
     * Legt den Wert der rid-Eigenschaft fest.
     *
     * @param value allowed object is {@link Integer }
     *
     */
    public void setRid(Integer value) {
        this.rid = value;
    }

    /**
     * Ruft den Wert der role-Eigenschaft ab.
     *
     * @return possible object is {@link Byte }
     *
     */
    public String getRole() {
        return role;
    }

    /**
     * Legt den Wert der role-Eigenschaft fest.
     *
     * @param value allowed object is {@link Byte }
     *
     */
    public void setRole(String value) {
        this.role = value;
    }

    /**
     * Ruft den Wert der rulesNotice-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getRulesNotice() {
        return rulesNotice;
    }

    /**
     * Legt den Wert der rulesNotice-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setRulesNotice(String value) {
        this.rulesNotice = value;
    }

    /**
     * Ruft den Wert der rulesNumber-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getRulesNumber() {
        return rulesNumber;
    }

    /**
     * Legt den Wert der rulesNumber-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setRulesNumber(String value) {
        this.rulesNumber = value;
    }

    /**
     * Ruft den Wert der rulesYear-Eigenschaft ab.
     *
     * @return possible object is {@link Short }
     *
     */
    public Short getRulesYear() {
        return rulesYear;
    }

    /**
     * Legt den Wert der rulesYear-Eigenschaft fest.
     *
     * @param value allowed object is {@link Short }
     *
     */
    public void setRulesYear(Short value) {
        this.rulesYear = value;
    }

    /**
     * Ruft den Wert der text-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getText() {
        return text;
    }

    /**
     * Legt den Wert der text-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Ruft den Wert der to-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getTo() {
        return to;
    }

    /**
     * Legt den Wert der to-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setTo(String value) {
        this.to = value;
    }

    /**
     * Ruft den Wert der typ-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getTyp() {
        return typ;
    }

    /**
     * Legt den Wert der typ-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setTyp(String value) {
        this.typ = value;
    }

    /**
     * Ruft den Wert der unchange-Eigenschaft ab.
     *
     * @return possible object is {@link Byte }
     *
     */
    public Integer getUnchange() {
        return unchange;
    }

    /**
     * Legt den Wert der unchange-Eigenschaft fest.
     *
     * @param value allowed object is {@link Byte }
     *
     */
    public void setUnchange(Integer value) {
        this.unchange = value;
    }

    /**
     * Ruft den Wert der used-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getUsed() {
        return used;
    }

    /**
     * Legt den Wert der used-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setUsed(String value) {
        this.used = value;
    }

    /**
     * Ruft den Wert der visible-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getVisible() {
        return visible;
    }

    /**
     * Legt den Wert der visible-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setVisible(String value) {
        this.visible = value;
    }

//    @Override
//    public String toString() {
//        try {
//            return "Rule{" + "rulesElement=" + SerializeHelper.getBytes(rulesElement) + " \n" + rulesElement.toString()+
//                    ", suggestions=" + SerializeHelper.getBytes(suggestions) + " " +(suggestions!=null?suggestions.toString():"")+
//                    ", caption=" + SerializeHelper.getBytes(caption) +
//                    ", entgelt=" + SerializeHelper.getBytes(entgelt) +
//                    ", errrorType=" + SerializeHelper.getBytes(errrorType) +
//                    ", feegroup=" + SerializeHelper.getBytes(feegroup) +
//                    ", from=" + SerializeHelper.getBytes(from) +
//                    ", hasinterval=" + SerializeHelper.getBytes(hasinterval) +
//                    ", intervalFrom=" + SerializeHelper.getBytes(intervalFrom) + 
//                    ", intervalTo=" + SerializeHelper.getBytes(intervalTo) +
//                    ", massnumber="  + SerializeHelper.getBytes(massnumber) +
//                    ", medtype=" + SerializeHelper.getBytes(medtype) +
//                    ", number=" + SerializeHelper.getBytes(number) + 
//                    ", profit=" + SerializeHelper.getBytes(profit) +
//                    ", rid=" + SerializeHelper.getBytes(rid) +
//                    ", role=" + SerializeHelper.getBytes(role) +
//                    ", rulesNotice=" + SerializeHelper.getBytes(rulesNotice) + 
//                    ", rulesNumber=" + SerializeHelper.getBytes(rulesNumber) +
//                    ", rulesYear=" + SerializeHelper.getBytes(rulesYear) +
//                    ", text=" + SerializeHelper.getBytes(text) +
//                    ", to=" + SerializeHelper.getBytes(to) + 
//                    ", typ=" + SerializeHelper.getBytes(typ) +
//                    ", unchange=" + SerializeHelper.getBytes(unchange) +
//                    ", used=" + SerializeHelper.getBytes(used) +
//                    ", visible=" + SerializeHelper.getBytes(visible) + '}'+"\n";
//        } catch (IOException ex) {
//            Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return "";
//    }
    @Override
    public String toString() {
        return "Rule{" + "rulesElement=\n" + rulesElement.toString()
                + ", suggestions=\n" + (suggestions != null ? suggestions.toString() : "")
                + ", caption=" + caption
                + ", entgelt=" + entgelt
                + ", errrorType=" + errrorType
                + ", feegroup=" + feegroup
                + ", from=" + from
                + ", hasinterval=" + hasinterval
                + ", intervalFrom=" + intervalFrom
                + ", intervalTo=" + intervalTo
                + ", massnumber=" + massnumber
                + ", medtype=" + medtype
                + ", number=" + number
                + ", profit=" + profit
                + ", rid=" + rid
                + ", role=" + role
                + ", rulesNotice=" + rulesNotice
                + ", rulesNumber=" + rulesNumber
                + ", rulesYear=" + rulesYear
                + ", text=" + text
                + ", to=" + to
                + ", typ=" + typ
                + ", unchange=" + unchange
                + ", used=" + used
                + ", visible=" + visible + '}' 
                + ", risks=\n" + (risks != null ? risks.toString() : "")
                + "\n";

    }
}
