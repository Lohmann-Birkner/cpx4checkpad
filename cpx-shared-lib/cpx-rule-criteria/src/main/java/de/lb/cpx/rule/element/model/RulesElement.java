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
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{}rules_value"/>
 *         &lt;element ref="{}rules_operator"/>
 *         &lt;element ref="{}rules_element"/>
 *       &lt;/choice>
 *       &lt;attribute name="nested" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "rulesValueOrRulesOperatorOrRulesElement"
})
@XmlRootElement(name = "rules_element")
public class RulesElement implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElements({
        @XmlElement(name = "rules_value", type = RulesValue.class),
        @XmlElement(name = "rules_operator", type = RulesOperator.class),
        @XmlElement(name = "rules_element", type = RulesElement.class)
    })
    protected List<Object> rulesValueOrRulesOperatorOrRulesElement = new ArrayList<>();
    @XmlAttribute(name = "nested")
    protected String nested;
    @XmlAttribute(name = "not")
    protected String not;

    /**
     * Gets the value of the rulesValueOrRulesOperatorOrRulesElement property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the rulesValueOrRulesOperatorOrRulesElement
     * property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRulesValueOrRulesOperatorOrRulesElement().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list null null null
     * null null null null null null null null null     {@link RulesValue }
     * {@link RulesOperator }
     * {@link RulesElement }
     *
     * @return list of rules
     */
    public List<Object> getRulesValueOrRulesOperatorOrRulesElement() {
        return this.rulesValueOrRulesOperatorOrRulesElement;
    }

    /**
     * Ruft den Wert der nested-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getNested() {
        return nested;
    }

    /**
     * Legt den Wert der nested-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setNested(String value) {
        this.nested = value;
    }

    /**
     * Ruft den Wert der not-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getNot() {
        return not;
    }

    /**
     * Legt den Wert der not-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setNot(String value) {
        this.not = value != null ? value : "false";
    }

//    @Override
//    public String toString() {
//        try {
//            return "\nRulesElement ("+SerializeHelper.getBytes(this)+"){" + "rulesValueOrRulesOperatorOrRulesElement=" + SerializeHelper.getBytes(rulesValueOrRulesOperatorOrRulesElement) +
//                    ", values "+ getListValue(rulesValueOrRulesOperatorOrRulesElement)+
//                    ", nested=" + SerializeHelper.getBytes(nested) +
//                    ", not=" + SerializeHelper.getBytes(not) + '}';
//        } catch (IOException ex) {
//            Logger.getLogger(RulesElement.class.getName()).log(Level.SEVERE, null, ex);
//            return "";
//        }
//    }
    @Override
    public String toString() {
        return "\nRulesElement\n{" + "rulesValueOrRulesOperatorOrRulesElement="
                + ", values " + getListValue(rulesValueOrRulesOperatorOrRulesElement) + "\n"
                + ", nested=" + nested
                + ", not=" + not + '}';

    }

    public String getListValue(List<Object> elements) {
        String values = "";
        for (Object obj : elements) {
            values += obj.toString();
        }
        return values;
    }
}
