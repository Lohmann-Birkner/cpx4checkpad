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
import javax.xml.bind.annotation.XmlRootElement;

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
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="op_type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="hasinterval" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="interval_from" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="interval_to" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="kriterium" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="method" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="not" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="operator" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="parameter" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="wert" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "", propOrder = {
//    "value"
//})
@XmlRootElement(name = "rules_operator")
public class RulesOperator implements Serializable {

    private static final long serialVersionUID = 1L;

//    @XmlValue
//    protected String value;
    @XmlAttribute(name = "op_type")
    protected String opType;
//    @XmlAttribute(name = "hasinterval")
//    protected String hasinterval;
//    @XmlAttribute(name = "interval_from")
//    protected String intervalFrom;
//    @XmlAttribute(name = "interval_to")
//    protected String intervalTo;
//    @XmlAttribute(name = "kriterium")
//    protected String kriterium;
//    @XmlAttribute(name = "method")
//    protected String method;
//    @XmlAttribute(name = "not")
//    protected String not;
//    @XmlAttribute(name = "operator")
//    protected String operator;
//    @XmlAttribute(name = "parameter")
//    protected String parameter;
//    @XmlAttribute(name = "wert")
//    protected String wert;

    /**
     * Ruft den Wert der value-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
//    public String getValue() {
//        return value;
//    }
    /**
     * Legt den Wert der value-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
//    public void setValue(String value) {
//        this.value = value;
//    }
    /**
     * Ruft den Wert der opType-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getOpType() {
        return opType;
    }

    /**
     * Legt den Wert der opType-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setOpType(String value) {
        this.opType = value;
    }

//    /**
//     * Ruft den Wert der hasinterval-Eigenschaft ab.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getHasinterval() {
//        return hasinterval;
//    }
//
//    /**
//     * Legt den Wert der hasinterval-Eigenschaft fest.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setHasinterval(String value) {
//        this.hasinterval = value;
//    }
//
//    /**
//     * Ruft den Wert der intervalFrom-Eigenschaft ab.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getIntervalFrom() {
//        return intervalFrom;
//    }
//
//    /**
//     * Legt den Wert der intervalFrom-Eigenschaft fest.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setIntervalFrom(String value) {
//        this.intervalFrom = value;
//    }
//
//    /**
//     * Ruft den Wert der intervalTo-Eigenschaft ab.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getIntervalTo() {
//        return intervalTo;
//    }
//
//    /**
//     * Legt den Wert der intervalTo-Eigenschaft fest.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setIntervalTo(String value) {
//        this.intervalTo = value;
//    }
//
//    /**
//     * Ruft den Wert der kriterium-Eigenschaft ab.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getKriterium() {
//        return kriterium;
//    }
//
//    /**
//     * Legt den Wert der kriterium-Eigenschaft fest.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setKriterium(String value) {
//        this.kriterium = value;
//    }
//
//    /**
//     * Ruft den Wert der method-Eigenschaft ab.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getMethod() {
//        return method;
//    }
//
//    /**
//     * Legt den Wert der method-Eigenschaft fest.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setMethod(String value) {
//        this.method = value;
//    }
//
//    /**
//     * Ruft den Wert der not-Eigenschaft ab.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getNot() {
//        return not;
//    }
//
//    /**
//     * Legt den Wert der not-Eigenschaft fest.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setNot(String value) {
//        this.not = value;
//    }
//
//    /**
//     * Ruft den Wert der operator-Eigenschaft ab.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getOperator() {
//        return operator;
//    }
//
//    /**
//     * Legt den Wert der operator-Eigenschaft fest.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setOperator(String value) {
//        this.operator = value;
//    }
//
//    /**
//     * Ruft den Wert der parameter-Eigenschaft ab.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getParameter() {
//        return parameter;
//    }
//
//    /**
//     * Legt den Wert der parameter-Eigenschaft fest.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setParameter(String value) {
//        this.parameter = value;
//    }
//
//    /**
//     * Ruft den Wert der wert-Eigenschaft ab.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getWert() {
//        return wert;
//    }
//
//    /**
//     * Legt den Wert der wert-Eigenschaft fest.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setWert(String value) {
//        this.wert = value;
//    }
    @Override
    public String toString() {
        return "\nRulesOperator{" + "opType=" + opType + '}';
    }

}
