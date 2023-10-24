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
// Generiert: 2018.10.17 um 01:19:00 PM CEST 
//
package de.lb.cpx.rule.criteria.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.text.StringEscapeUtils;

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
 *         &lt;element name="criterion_types">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ctriterion_type" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="ident" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="operation_group" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="operation_groups">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="operation_group" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="operation" maxOccurs="unbounded">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;attribute name="nested" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="interval_groups">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="interval_group" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="interval_limit" maxOccurs="unbounded">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="interval_limits">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="interval_limit" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="single" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="interval_rules">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="interval_relation" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="interval_group" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="interval_limit" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="sugg_action_groups">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="sugg_action_group" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="sugg_action" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="sugg_actions">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="sugg_action" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="ident" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
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
    "criterionTypes",
    "operationGroups",
    "intervalGroups",
    "intervalLimits",
    "intervalRules",
    "suggActionGroups",
    "suggActions",
    "feeGroups"
})
@XmlRootElement(name = "types_and_operations")
public class TypesAndOperations {

    @XmlElement(name = "criterion_types", required = true)
    protected TypesAndOperations.CriterionTypes criterionTypes;
    @XmlElement(name = "operation_groups", required = true)
    protected TypesAndOperations.OperationGroups operationGroups;
    @XmlElement(name = "interval_groups", required = true)
    protected TypesAndOperations.IntervalGroups intervalGroups;
    @XmlElement(name = "interval_limits", required = true)
    protected TypesAndOperations.IntervalLimits intervalLimits;
    @XmlElement(name = "interval_rules", required = true)
    protected TypesAndOperations.IntervalRules intervalRules;
    @XmlElement(name = "sugg_action_groups", required = true)
    protected TypesAndOperations.SuggActionGroups suggActionGroups;
    @XmlElement(name = "sugg_actions", required = true)
    protected TypesAndOperations.SuggActions suggActions;
    @XmlElement(name = "fee_groups", required = true)
    protected TypesAndOperations.FeeGroups feeGroups;

    /**
     * Ruft den Wert der criterionTypes-Eigenschaft ab.
     *
     * @return possible object is {@link TypesAndOperations.CriterionTypes }
     *
     */
    public TypesAndOperations.CriterionTypes getCriterionTypes() {
        return criterionTypes;
    }

    /**
     * Legt den Wert der criterionTypes-Eigenschaft fest.
     *
     * @param value allowed object is {@link TypesAndOperations.CriterionTypes }
     *
     */
    public void setCriterionTypes(TypesAndOperations.CriterionTypes value) {
        this.criterionTypes = value;
    }

    /**
     * Ruft den Wert der operationGroups-Eigenschaft ab.
     *
     * @return possible object is {@link TypesAndOperations.OperationGroups }
     *
     */
    public TypesAndOperations.OperationGroups getOperationGroups() {
        return operationGroups;
    }

    /**
     * Legt den Wert der operationGroups-Eigenschaft fest.
     *
     * @param value allowed object is
     *     {@link TypesAndOperations.OperationGroups }
     *
     */
    public void setOperationGroups(TypesAndOperations.OperationGroups value) {
        this.operationGroups = value;
    }

    /**
     * Ruft den Wert der intervalGroups-Eigenschaft ab.
     *
     * @return possible object is {@link TypesAndOperations.IntervalGroups }
     *
     */
    public TypesAndOperations.IntervalGroups getIntervalGroups() {
        return intervalGroups;
    }

    /**
     * Legt den Wert der intervalGroups-Eigenschaft fest.
     *
     * @param value allowed object is {@link TypesAndOperations.IntervalGroups }
     *
     */
    public void setIntervalGroups(TypesAndOperations.IntervalGroups value) {
        this.intervalGroups = value;
    }

    /**
     * Ruft den Wert der intervalLimits-Eigenschaft ab.
     *
     * @return possible object is {@link TypesAndOperations.IntervalLimits }
     *
     */
    public TypesAndOperations.IntervalLimits getIntervalLimits() {
        return intervalLimits;
    }

    /**
     * Legt den Wert der intervalLimits-Eigenschaft fest.
     *
     * @param value allowed object is {@link TypesAndOperations.IntervalLimits }
     *
     */
    public void setIntervalLimits(TypesAndOperations.IntervalLimits value) {
        this.intervalLimits = value;
    }

    /**
     * Ruft den Wert der intervalRules-Eigenschaft ab.
     *
     * @return possible object is {@link TypesAndOperations.IntervalRules }
     *
     */
    public TypesAndOperations.IntervalRules getIntervalRules() {
        return intervalRules;
    }

    /**
     * Legt den Wert der intervalRules-Eigenschaft fest.
     *
     * @param value allowed object is {@link TypesAndOperations.IntervalRules }
     *
     */
    public void setIntervalRules(TypesAndOperations.IntervalRules value) {
        this.intervalRules = value;
    }

    /**
     * Ruft den Wert der suggActionGroups-Eigenschaft ab.
     *
     * @return possible object is {@link TypesAndOperations.SuggActionGroups }
     *
     */
    public TypesAndOperations.SuggActionGroups getSuggActionGroups() {
        return suggActionGroups;
    }

    /**
     * Legt den Wert der suggActionGroups-Eigenschaft fest.
     *
     * @param value allowed object is
     *     {@link TypesAndOperations.SuggActionGroups }
     *
     */
    public void setSuggActionGroups(TypesAndOperations.SuggActionGroups value) {
        this.suggActionGroups = value;
    }

    /**
     * Ruft den Wert der suggActions-Eigenschaft ab.
     *
     * @return possible object is {@link TypesAndOperations.SuggActions }
     *
     */
    public TypesAndOperations.SuggActions getSuggActions() {
        return suggActions;
    }

    /**
     * Legt den Wert der suggActions-Eigenschaft fest.
     *
     * @param value allowed object is {@link TypesAndOperations.SuggActions }
     *
     */
    public void setSuggActions(TypesAndOperations.SuggActions value) {
        this.suggActions = value;
    }

    /**
     * Ruft den Wert der feeGroups-Eigenschaft ab.
     *
     * @return possible object is {@link TypesAndOperations.FeeGroups }
     *
     */
    public TypesAndOperations.FeeGroups getFeeGroups() {
        return feeGroups;
    }

    /**
     * Legt den Wert der feeGroups-Eigenschaft fest.
     *
     * @param value allowed object is {@link TypesAndOperations.FeeGroups }
     *
     */
    public void setFeeGroups(TypesAndOperations.FeeGroups value) {
        this.feeGroups = value;
    }

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
     *         &lt;element name="ctriterion_type" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="ident" type="{http://www.w3.org/2001/XMLSchema}int" />
     *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="operation_group" type="{http://www.w3.org/2001/XMLSchema}string" />
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
        "ctriterionType"
    })
    public static class CriterionTypes {

        @XmlElement(name = "ctriterion_type", required = true)
        protected List<TypesAndOperations.CriterionTypes.CtriterionType> ctriterionType;

        /**
         * Gets the value of the ctriterionType property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the ctriterionType property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCtriterionType().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TypesAndOperations.CriterionTypes.CtriterionType }
         *
         *
         * @return
         */
        public List<TypesAndOperations.CriterionTypes.CtriterionType> getCtriterionType() {
            if (ctriterionType == null) {
                ctriterionType = new ArrayList<>();
            }
            return this.ctriterionType;
        }

        /**
         * <p>
         * Java-Klasse f�r anonymous complex type.
         *
         * <p>
         * Das folgende Schemafragment gibt den erwarteten Content an, der in
         * dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="ident" type="{http://www.w3.org/2001/XMLSchema}int" />
         *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="operation_group" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class CtriterionType {

            @XmlAttribute(name = "ident")
            protected Integer ident;
            @XmlAttribute(name = "name")
            protected String name;
            @XmlAttribute(name = "operation_group")
            protected String operationGroup;

            /**
             * Ruft den Wert der ident-Eigenschaft ab.
             *
             * @return possible object is {@link Integer }
             *
             */
            public Integer getIdent() {
                return ident;
            }

            /**
             * Legt den Wert der ident-Eigenschaft fest.
             *
             * @param value allowed object is {@link Integer }
             *
             */
            public void setIdent(Integer value) {
                this.ident = value;
            }

            /**
             * Ruft den Wert der name-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getName() {
                return name;
            }

            /**
             * Legt den Wert der name-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Ruft den Wert der operationGroup-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getOperationGroup() {
                return operationGroup;
            }

            /**
             * Legt den Wert der operationGroup-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setOperationGroup(String value) {
                this.operationGroup = value;
            }

        }

    }

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
     *         &lt;element name="interval_group" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="interval_limit" maxOccurs="unbounded">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
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
        "intervalGroup"
    })
    public static class IntervalGroups {

        @XmlElement(name = "interval_group", required = true)
        protected List<TypesAndOperations.IntervalGroups.IntervalGroup> intervalGroup;

        /**
         * Gets the value of the intervalGroup property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the intervalGroup property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIntervalGroup().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TypesAndOperations.IntervalGroups.IntervalGroup }
         *
         *
         * @return
         */
        public List<TypesAndOperations.IntervalGroups.IntervalGroup> getIntervalGroup() {
            if (intervalGroup == null) {
                intervalGroup = new ArrayList<>();
            }
            return this.intervalGroup;
        }

        /**
         * <p>
         * Java-Klasse f�r anonymous complex type.
         *
         * <p>
         * Das folgende Schemafragment gibt den erwarteten Content an, der in
         * dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="interval_limit" maxOccurs="unbounded">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "intervalLimit"
        })
        public static class IntervalGroup {

            @XmlElement(name = "interval_limit", required = true)
            protected List<TypesAndOperations.IntervalGroups.IntervalGroup.IntervalLimit> intervalLimit;
            @XmlAttribute(name = "name")
            protected String name;

            /**
             * Gets the value of the intervalLimit property.
             *
             * <p>
             * This accessor method returns a reference to the live list, not a
             * snapshot. Therefore any modification you make to the returned
             * list will be present inside the JAXB object. This is why there is
             * not a <CODE>set</CODE> method for the intervalLimit property.
             *
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getIntervalLimit().add(newItem);
             * </pre>
             *
             *
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link TypesAndOperations.IntervalGroups.IntervalGroup.IntervalLimit }
             *
             *
             * @return
             */
            public List<TypesAndOperations.IntervalGroups.IntervalGroup.IntervalLimit> getIntervalLimit() {
                if (intervalLimit == null) {
                    intervalLimit = new ArrayList<>();
                }
                return this.intervalLimit;
            }

            /**
             * Ruft den Wert der name-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getName() {
                return name;
            }

            /**
             * Legt den Wert der name-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * <p>
             * Java-Klasse f�r anonymous complex type.
             *
             * <p>
             * Das folgende Schemafragment gibt den erwarteten Content an, der
             * in dieser Klasse enthalten ist.
             *
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             *
             *
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class IntervalLimit {

                @XmlAttribute(name = "name")
                protected String name;

                /**
                 * Ruft den Wert der name-Eigenschaft ab.
                 *
                 * @return possible object is {@link String }
                 *
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Legt den Wert der name-Eigenschaft fest.
                 *
                 * @param value allowed object is {@link String }
                 *
                 */
                public void setName(String value) {
                    this.name = value;
                }

            }

        }

    }

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
     *         &lt;element name="interval_limit" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="single" type="{http://www.w3.org/2001/XMLSchema}boolean" />
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
        "intervalLimit"
    })
    public static class IntervalLimits {

        @XmlElement(name = "interval_limit", required = true)
        protected List<TypesAndOperations.IntervalLimits.IntervalLimit> intervalLimit;

        /**
         * Gets the value of the intervalLimit property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the intervalLimit property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIntervalLimit().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TypesAndOperations.IntervalLimits.IntervalLimit }
         *
         *
         * @return
         */
        public List<TypesAndOperations.IntervalLimits.IntervalLimit> getIntervalLimit() {
            if (intervalLimit == null) {
                intervalLimit = new ArrayList<>();
            }
            return this.intervalLimit;
        }

        /**
         * <p>
         * Java-Klasse f�r anonymous complex type.
         *
         * <p>
         * Das folgende Schemafragment gibt den erwarteten Content an, der in
         * dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="single" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class IntervalLimit {

            @XmlAttribute(name = "display_name")
            protected String displayName;
            @XmlAttribute(name = "name")
            protected String name;
            @XmlAttribute(name = "single")
            protected Boolean single;

            /**
             * Ruft den Wert der displayName-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getDisplayName() {
                return displayName;
            }

            /**
             * Legt den Wert der displayName-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setDisplayName(String value) {
                this.displayName = value;
            }

            /**
             * Ruft den Wert der name-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getName() {
                return name;
            }

            /**
             * Legt den Wert der name-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Ruft den Wert der single-Eigenschaft ab.
             *
             * @return possible object is {@link Boolean }
             *
             */
            public Boolean isSingle() {
                return single;
            }

            /**
             * Legt den Wert der single-Eigenschaft fest.
             *
             * @param value allowed object is {@link Boolean }
             *
             */
            public void setSingle(Boolean value) {
                this.single = value;
            }

        }

    }

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
     *         &lt;element name="interval_relation" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="interval_group" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="interval_limit" type="{http://www.w3.org/2001/XMLSchema}string" />
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
        "intervalRelation"
    })
    public static class IntervalRules {

        @XmlElement(name = "interval_relation", required = true)
        protected List<TypesAndOperations.IntervalRules.IntervalRelation> intervalRelation;

        /**
         * Gets the value of the intervalRelation property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the intervalRelation property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIntervalRelation().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TypesAndOperations.IntervalRules.IntervalRelation }
         *
         *
         * @return
         */
        public List<TypesAndOperations.IntervalRules.IntervalRelation> getIntervalRelation() {
            if (intervalRelation == null) {
                intervalRelation = new ArrayList<>();
            }
            return this.intervalRelation;
        }

        /**
         * <p>
         * Java-Klasse f�r anonymous complex type.
         *
         * <p>
         * Das folgende Schemafragment gibt den erwarteten Content an, der in
         * dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="interval_group" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="interval_limit" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class IntervalRelation {

            @XmlAttribute(name = "interval_group")
            protected String intervalGroup;
            @XmlAttribute(name = "interval_limit")
            protected String intervalLimit;

            /**
             * Ruft den Wert der intervalGroup-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getIntervalGroup() {
                return intervalGroup;
            }

            /**
             * Legt den Wert der intervalGroup-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setIntervalGroup(String value) {
                this.intervalGroup = value;
            }

            /**
             * Ruft den Wert der intervalLimit-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getIntervalLimit() {
                return intervalLimit;
            }

            /**
             * Legt den Wert der intervalLimit-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setIntervalLimit(String value) {
                this.intervalLimit = value;
            }

        }

    }

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
     *         &lt;element name="operation_group" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="operation" maxOccurs="unbounded">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                           &lt;attribute name="nested" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
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
        "operationGroup"
    })
    public static class OperationGroups {

        @XmlElement(name = "operation_group", required = true)
        protected List<TypesAndOperations.OperationGroups.OperationGroup> operationGroup;

        /**
         * Gets the value of the operationGroup property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the operationGroup property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getOperationGroup().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TypesAndOperations.OperationGroups.OperationGroup }
         *
         *
         * @return
         */
        public List<TypesAndOperations.OperationGroups.OperationGroup> getOperationGroup() {
            if (operationGroup == null) {
                operationGroup = new ArrayList<>();
            }
            return this.operationGroup;
        }

        /**
         * <p>
         * Java-Klasse f�r anonymous complex type.
         *
         * <p>
         * Das folgende Schemafragment gibt den erwarteten Content an, der in
         * dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="operation" maxOccurs="unbounded">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                 &lt;attribute name="nested" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "operation"
        })
        public static class OperationGroup {

            @XmlElement(required = true)
            protected List<TypesAndOperations.OperationGroups.OperationGroup.Operation> operation;
            @XmlAttribute(name = "name")
            protected String name;

            /**
             * Gets the value of the operation property.
             *
             * <p>
             * This accessor method returns a reference to the live list, not a
             * snapshot. Therefore any modification you make to the returned
             * list will be present inside the JAXB object. This is why there is
             * not a <CODE>set</CODE> method for the operation property.
             *
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getOperation().add(newItem);
             * </pre>
             *
             *
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link TypesAndOperations.OperationGroups.OperationGroup.Operation }
             *
             *
             * @return
             */
            public List<TypesAndOperations.OperationGroups.OperationGroup.Operation> getOperation() {
                if (operation == null) {
                    operation = new ArrayList<>();
                }
                return this.operation;
            }

            /**
             * Ruft den Wert der name-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getName() {
                return name;
            }

            /**
             * Legt den Wert der name-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * <p>
             * Java-Klasse f�r anonymous complex type.
             *
             * <p>
             * Das folgende Schemafragment gibt den erwarteten Content an, der
             * in dieser Klasse enthalten ist.
             *
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
             *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
             *       &lt;attribute name="nested" type="{http://www.w3.org/2001/XMLSchema}boolean" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             *
             *
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Operation {

                @XmlAttribute(name = "display_name")
                protected String displayName;
                @XmlAttribute(name = "name")
                protected String name;
                @XmlAttribute(name = "nested")
                protected Boolean nested;

                /**
                 * Ruft den Wert der displayName-Eigenschaft ab.
                 *
                 * @return possible object is {@link String }
                 *
                 */
                public String getDisplayName() {
                    return displayName;
                }

                /**
                 * Legt den Wert der displayName-Eigenschaft fest.
                 *
                 * @param value allowed object is {@link String }
                 *
                 */
                public void setDisplayName(String value) {
                    this.displayName = value;
                }

                /**
                 * Ruft den Wert der name-Eigenschaft ab.
                 *
                 * @return possible object is {@link String }
                 *
                 */
                public String getName() {
                    return name;
                }

                public String getFormatedName() {
                    return StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeXml(this.name));
                }

                /**
                 * Legt den Wert der name-Eigenschaft fest.
                 *
                 * @param value allowed object is {@link String }
                 *
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Ruft den Wert der nested-Eigenschaft ab.
                 *
                 * @return possible object is {@link Boolean }
                 *
                 */
                public Boolean isNested() {
                    return nested;
                }

                /**
                 * Legt den Wert der nested-Eigenschaft fest.
                 *
                 * @param value allowed object is {@link Boolean }
                 *
                 */
                public void setNested(Boolean value) {
                    this.nested = value;
                }

                @Override
                public String toString() {
                    return getFormatedName();
                }

                @Override
                public int hashCode() {
                    int hash = 5;
                    hash = 79 * hash + Objects.hashCode(this.displayName);
                    hash = 79 * hash + Objects.hashCode(this.name);
                    hash = 79 * hash + Objects.hashCode(this.nested);
                    return hash;
                }

                @Override
                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    if (obj == null) {
                        return false;
                    }
                    if (getClass() != obj.getClass()) {
                        return false;
                    }
                    final Operation other = (Operation) obj;
                    if (!Objects.equals(this.name, other.name)) {
                        return false;
                    }
                    return true;
                }

            }

        }

    }

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
     *         &lt;element name="sugg_action_group" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="sugg_action" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
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
        "suggActionGroup",
        "suggAction"
    })
    public static class SuggActionGroups {

        @XmlElement(name = "sugg_action_group", required = true)
        protected List<TypesAndOperations.SuggActionGroups.SuggActionGroup> suggActionGroup;
        @XmlElement(name = "sugg_action", required = true)
        protected List<TypesAndOperations.SuggActionGroups.SuggAction> suggAction;

        /**
         * Gets the value of the suggActionGroup property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the suggActionGroup property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSuggActionGroup().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TypesAndOperations.SuggActionGroups.SuggActionGroup }
         *
         *
         * @return
         */
        public List<TypesAndOperations.SuggActionGroups.SuggActionGroup> getSuggActionGroup() {
            if (suggActionGroup == null) {
                suggActionGroup = new ArrayList<>();
            }
            return this.suggActionGroup;
        }

        /**
         * Gets the value of the suggAction property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the suggAction property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSuggAction().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TypesAndOperations.SuggActionGroups.SuggAction }
         *
         *
         * @return
         */
        public List<TypesAndOperations.SuggActionGroups.SuggAction> getSuggAction() {
            if (suggAction == null) {
                suggAction = new ArrayList<>();
            }
            return this.suggAction;
        }

        /**
         * <p>
         * Java-Klasse f�r anonymous complex type.
         *
         * <p>
         * Das folgende Schemafragment gibt den erwarteten Content an, der in
         * dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class SuggAction {

            @XmlAttribute(name = "name")
            protected String name;

            /**
             * Ruft den Wert der name-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getName() {
                return name;
            }

            /**
             * Legt den Wert der name-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setName(String value) {
                this.name = value;
            }

        }

        /**
         * <p>
         * Java-Klasse f�r anonymous complex type.
         *
         * <p>
         * Das folgende Schemafragment gibt den erwarteten Content an, der in
         * dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class SuggActionGroup {

            @XmlAttribute(name = "name")
            protected String name;

            /**
             * Ruft den Wert der name-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getName() {
                return name;
            }

            /**
             * Legt den Wert der name-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setName(String value) {
                this.name = value;
            }

        }

    }

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
     *         &lt;element name="sugg_action" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="ident" type="{http://www.w3.org/2001/XMLSchema}int" />
     *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
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
        "suggAction"
    })
    public static class SuggActions {

        @XmlElement(name = "sugg_action", required = true)
        protected List<TypesAndOperations.SuggActions.SuggAction> suggAction;

        /**
         * Gets the value of the suggAction property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the suggAction property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSuggAction().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TypesAndOperations.SuggActions.SuggAction }
         *
         *
         * @return
         */
        public List<TypesAndOperations.SuggActions.SuggAction> getSuggAction() {
            if (suggAction == null) {
                suggAction = new ArrayList<>();
            }
            return this.suggAction;
        }

        /**
         * <p>
         * Java-Klasse f�r anonymous complex type.
         *
         * <p>
         * Das folgende Schemafragment gibt den erwarteten Content an, der in
         * dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="ident" type="{http://www.w3.org/2001/XMLSchema}int" />
         *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class SuggAction {

            @XmlAttribute(name = "display_name")
            protected String displayName;
            @XmlAttribute(name = "ident")
            protected Integer ident;
            @XmlAttribute(name = "name")
            protected String name;

            /**
             * Ruft den Wert der displayName-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getDisplayName() {
                return displayName;
            }

            /**
             * Legt den Wert der displayName-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setDisplayName(String value) {
                this.displayName = value;
            }

            /**
             * Ruft den Wert der ident-Eigenschaft ab.
             *
             * @return possible object is {@link Integer }
             *
             */
            public Integer getIdent() {
                return ident;
            }

            /**
             * Legt den Wert der ident-Eigenschaft fest.
             *
             * @param value allowed object is {@link Integer }
             *
             */
            public void setIdent(Integer value) {
                this.ident = value;
            }

            /**
             * Ruft den Wert der name-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getName() {
                return name;
            }

            /**
             * Legt den Wert der name-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setName(String value) {
                this.name = value;
            }

        }

    }

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
     *         &lt;element name="fee_group" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="fee" maxOccurs="unbounded">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
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
        "feeGroup"
    })
    public static class FeeGroups {

        @XmlElement(name = "fee_group", required = true)
        protected List<TypesAndOperations.FeeGroups.FeeGroup> feeGroup;

        /**
         * Gets the value of the feeGroup property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the feeGroup property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFeeGroup().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TypesAndOperations.IntervalGroups.FeeGroup }
         *
         *
         * @return
         */
        public List<TypesAndOperations.FeeGroups.FeeGroup> getFeeGroup() {
            if (feeGroup == null) {
                feeGroup = new ArrayList<>();
            }
            return this.feeGroup;
        }

        /**
         * <p>
         * Java-Klasse f�r anonymous complex type.
         *
         * <p>
         * Das folgende Schemafragment gibt den erwarteten Content an, der in
         * dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="interval_limit" maxOccurs="unbounded">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "feeTypes"
        })
        public static class FeeGroup {

            @XmlElement(name = "fee_type", required = true)
            protected List<TypesAndOperations.FeeGroups.FeeGroup.FeeType> feeTypes;
            @XmlAttribute(name = "name")
            protected String name;

            /**
             * Gets the value of the intervalLimit property.
             *
             * <p>
             * This accessor method returns a reference to the live list, not a
             * snapshot. Therefore any modification you make to the returned
             * list will be present inside the JAXB object. This is why there is
             * not a <CODE>set</CODE> method for the intervalLimit property.
             *
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getIntervalLimit().add(newItem);
             * </pre>
             *
             *
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link TypesAndOperations.IntervalGroups.IntervalGroup.IntervalLimit }
             *
             *
             * @return
             */
            public List<TypesAndOperations.FeeGroups.FeeGroup.FeeType> getFeeTypes() {
                if (feeTypes == null) {
                    feeTypes = new ArrayList<>();
                }
                return this.feeTypes;
            }

            /**
             * Ruft den Wert der name-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getName() {
                return name;
            }

            /**
             * Legt den Wert der name-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * <p>
             * Java-Klasse f�r anonymous complex type.
             *
             * <p>
             * Das folgende Schemafragment gibt den erwarteten Content an, der
             * in dieser Klasse enthalten ist.
             *
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             *
             *
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class FeeType {

                @XmlAttribute(name = "name")
                protected String name;
                @XmlAttribute(name = "ident")
                protected String ident;

                /**
                 * Ruft den Wert der name-Eigenschaft ab.
                 *
                 * @return possible object is {@link String }
                 *
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Legt den Wert der name-Eigenschaft fest.
                 *
                 * @param value allowed object is {@link String }
                 *
                 */
                public void setName(String value) {
                    this.name = value;
                }

                /**
                 * Ruft den Wert der ident-Eigenschaft ab.
                 *
                 * @return possible object is {@link String }
                 *
                 */
                public String getIdent() {
                    return ident;
                }

                /**
                 * Legt den Wert der ident-Eigenschaft fest.
                 *
                 * @param value allowed object is {@link String }
                 *
                 */
                public void setIdent(String value) {
                    this.ident = value;
                }

            }

        }

    }
}
