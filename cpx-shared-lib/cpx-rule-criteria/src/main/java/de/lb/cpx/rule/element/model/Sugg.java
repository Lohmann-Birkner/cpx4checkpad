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
 *       &lt;attribute name="actionid" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="condition_op" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="condition_value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="crit" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="op" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
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
@XmlRootElement(name = "sugg")
public class Sugg implements Serializable {

    private static final long serialVersionUID = 1L;

//    @XmlValue
//    protected String value;
    @XmlAttribute(name = "actionid")
    protected String actionid;
    @XmlAttribute(name = "condition_op")
    protected String conditionOp;
    @XmlAttribute(name = "condition_value")
    protected String conditionValue;
    @XmlAttribute(name = "crit")
    protected String crit;
    @XmlAttribute(name = "op")
    protected String op;
    @XmlAttribute(name = "value")
    protected String valueAttribute;

//    /**
//     * Ruft den Wert der value-Eigenschaft ab.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getValue() {
//        return value;
//    }
//
//    /**
//     * Legt den Wert der value-Eigenschaft fest.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setValue(String value) {
//        this.value = value;
//    }
    /**
     * Ruft den Wert der actionid-Eigenschaft ab.
     *
     * @return possible object is {@link Byte }
     *
     */
    public String getActionid() {
        return actionid;
    }

    /**
     * Legt den Wert der actionid-Eigenschaft fest.
     *
     * @param value allowed object is {@link Byte }
     *
     */
    public void setActionid(String value) {
        this.actionid = value;
    }

    /**
     * Ruft den Wert der conditionOp-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getConditionOp() {
        return conditionOp;
    }

    /**
     * Legt den Wert der conditionOp-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setConditionOp(String value) {
        this.conditionOp = value;
    }

    /**
     * Ruft den Wert der conditionValue-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getConditionValue() {
        return conditionValue;
    }

    /**
     * Legt den Wert der conditionValue-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setConditionValue(String value) {
        this.conditionValue = value;
    }

    /**
     * Ruft den Wert der crit-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getCrit() {
        return crit;
    }

    /**
     * Legt den Wert der crit-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setCrit(String value) {
        this.crit = value;
    }

    /**
     * Ruft den Wert der op-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getOp() {
        return op;
    }

    /**
     * Legt den Wert der op-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setOp(String value) {
        this.op = value;
    }

    /**
     * Ruft den Wert der valueAttribute-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getValueAttribute() {
        return valueAttribute;
    }

    /**
     * Legt den Wert der valueAttribute-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setValueAttribute(String value) {
        this.valueAttribute = value;
    }

    @Override
    public String toString() {
        return "\nSugg{ actionid=" + actionid
                + ", conditionOp=" + conditionOp
                + ", conditionValue=" + conditionValue
                + ", crit=" + crit
                + ", op=" + op
                + ", valueAttribute=" + valueAttribute + '}';
    }

}
