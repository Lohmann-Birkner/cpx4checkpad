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
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}sugg" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="suggtext" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "", propOrder = {
//    "sugg"
//})
@XmlRootElement(name = "suggestions")
public class Suggestions implements Serializable {

    private static final long serialVersionUID = 1L;

    protected final List<Sugg> sugg;// = new ArrayList<>();
    @XmlAttribute(name = "suggtext")
    protected String suggtext;

    public Suggestions() {
        sugg = new ArrayList<>();
    }

    /**
     * Gets the value of the sugg property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the sugg property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSugg().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Sugg }
     *
     * @return list of suggestions
     */
    public List<Sugg> getSugg() {
        return this.sugg;
    }

    /**
     * Ruft den Wert der suggtext-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     *
     */
    public String getSuggtext() {
        return suggtext;
    }

    /**
     * Legt den Wert der suggtext-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setSuggtext(String value) {
        this.suggtext = value;
    }

    @Override
    public String toString() {
        return "Suggestions{" + "sugg=" + getListValue(sugg) + ", suggtext=" + suggtext + '}';
    }

    public String getListValue(List<Sugg> elements) {
        String values = "";
        for (Object obj : elements) {
            values += obj.toString();
        }
        return values;
    }
}
