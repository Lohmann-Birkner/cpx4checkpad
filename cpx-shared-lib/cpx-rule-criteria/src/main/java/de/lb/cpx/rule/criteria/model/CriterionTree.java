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
// Generiert: 2018.10.17 um 02:32:40 PM CEST 
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
 *         &lt;element name="supergroup">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="tooltip" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="group" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="tooltip" maxOccurs="unbounded">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="criterion" maxOccurs="unbounded">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="tooltip" maxOccurs="unbounded">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                               &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                     &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;attribute name="criterion_type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;attribute name="usage" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                           &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="has_interval" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    "supergroup"
})
@XmlRootElement(name = "criterion_tree")
public class CriterionTree {

    @XmlElement(required = true)
    protected CriterionTree.Supergroup supergroup;

    /**
     * Ruft den Wert der supergroup-Eigenschaft ab.
     *
     * @return possible object is {@link CriterionTree.Supergroup }
     *
     */
    public CriterionTree.Supergroup getSupergroup() {
        return supergroup;
    }

    /**
     * Legt den Wert der supergroup-Eigenschaft fest.
     *
     * @param value allowed object is {@link CriterionTree.Supergroup }
     *
     */
    public void setSupergroup(CriterionTree.Supergroup value) {
        this.supergroup = value;
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
     *         &lt;element name="tooltip" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="group" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="tooltip" maxOccurs="unbounded">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                           &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="criterion" maxOccurs="unbounded">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="tooltip" maxOccurs="unbounded">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                                     &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                           &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                           &lt;attribute name="criterion_type" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                           &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                           &lt;attribute name="usage" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="has_interval" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
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
        "tooltip",
        "group"
    })
    public static class Supergroup {

        @XmlElement(required = true)
        protected List<CriterionTree.Supergroup.Tooltip> tooltip;
        @XmlElement(required = true)
        protected List<CriterionTree.Supergroup.Group> group;
        @XmlAttribute(name = "cpname")
        protected String cpname;
        @XmlAttribute(name = "display_name")
        protected String displayName;
        @XmlAttribute(name = "name")
        protected String name;

        /**
         * Gets the value of the tooltip property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the tooltip property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTooltip().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CriterionTree.Supergroup.Tooltip }
         *
         *
         * @return
         */
        public List<CriterionTree.Supergroup.Tooltip> getTooltip() {
            if (tooltip == null) {
                tooltip = new ArrayList<>();
            }
            return this.tooltip;
        }

        /**
         * Gets the value of the group property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the group property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGroup().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CriterionTree.Supergroup.Group }
         *
         *
         * @return
         */
        public List<CriterionTree.Supergroup.Group> getGroup() {
            if (group == null) {
                group = new ArrayList<>();
            }
            return this.group;
        }

        /**
         * Ruft den Wert der cpname-Eigenschaft ab.
         *
         * @return possible object is {@link String }
         *
         */
        public String getCpname() {
            return cpname;
        }

        /**
         * Legt den Wert der cpname-Eigenschaft fest.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setCpname(String value) {
            this.cpname = value;
        }

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
         *         &lt;element name="tooltip" maxOccurs="unbounded">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                 &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="criterion" maxOccurs="unbounded">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="tooltip" maxOccurs="unbounded">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                           &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *                 &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                 &lt;attribute name="criterion_type" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                 &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                 &lt;attribute name="usage" type="{http://www.w3.org/2001/XMLSchema}string" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="has_interval" type="{http://www.w3.org/2001/XMLSchema}boolean" />
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
            "tooltip",
            "criterion"
        })
        public static class Group {

            @XmlElement(required = true)
            protected List<CriterionTree.Supergroup.Group.Tooltip> tooltip;
            @XmlElement(required = true)
            protected List<CriterionTree.Supergroup.Group.Criterion> criterion;
            @XmlAttribute(name = "cpname")
            protected String cpname;
            @XmlAttribute(name = "display_name")
            protected String displayName;
            @XmlAttribute(name = "has_interval")
            protected Boolean hasInterval;
            @XmlAttribute(name = "name")
            protected String name;

            /**
             * Gets the value of the tooltip property.
             *
             * <p>
             * This accessor method returns a reference to the live list, not a
             * snapshot. Therefore any modification you make to the returned
             * list will be present inside the JAXB object. This is why there is
             * not a <CODE>set</CODE> method for the tooltip property.
             *
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getTooltip().add(newItem);
             * </pre>
             *
             *
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link CriterionTree.Supergroup.Group.Tooltip }
             *
             *
             * @return
             */
            public List<CriterionTree.Supergroup.Group.Tooltip> getTooltip() {
                if (tooltip == null) {
                    tooltip = new ArrayList<>();
                }
                return this.tooltip;
            }

            /**
             * Gets the value of the criterion property.
             *
             * <p>
             * This accessor method returns a reference to the live list, not a
             * snapshot. Therefore any modification you make to the returned
             * list will be present inside the JAXB object. This is why there is
             * not a <CODE>set</CODE> method for the criterion property.
             *
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getCriterion().add(newItem);
             * </pre>
             *
             *
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link CriterionTree.Supergroup.Group.Criterion }
             *
             *
             * @return
             */
            public List<CriterionTree.Supergroup.Group.Criterion> getCriterion() {
                if (criterion == null) {
                    criterion = new ArrayList<>();
                }
                return this.criterion;
            }

            /**
             * Ruft den Wert der cpname-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getCpname() {
                return cpname;
            }

            /**
             * Legt den Wert der cpname-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setCpname(String value) {
                this.cpname = value;
            }

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
             * Ruft den Wert der hasInterval-Eigenschaft ab.
             *
             * @return possible object is {@link Boolean }
             *
             */
            public Boolean isHasInterval() {
                return hasInterval;
            }

            /**
             * Legt den Wert der hasInterval-Eigenschaft fest.
             *
             * @param value allowed object is {@link Boolean }
             *
             */
            public void setHasInterval(Boolean value) {
                this.hasInterval = value;
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
             *       &lt;sequence>
             *         &lt;element name="tooltip" maxOccurs="unbounded">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
             *                 &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *       &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
             *       &lt;attribute name="criterion_type" type="{http://www.w3.org/2001/XMLSchema}string" />
             *       &lt;attribute name="display_name" type="{http://www.w3.org/2001/XMLSchema}string" />
             *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
             *       &lt;attribute name="usage" type="{http://www.w3.org/2001/XMLSchema}string" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             *
             *
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "tooltip"
            })
            public static class Criterion {

                @XmlElement(required = true)
                protected List<CriterionTree.Supergroup.Group.Criterion.Tooltip> tooltip;
                @XmlAttribute(name = "cpname")
                protected String cpname;
                @XmlAttribute(name = "criterion_type")
                protected String criterionType;
                @XmlAttribute(name = "display_name")
                protected String displayName;
                @XmlAttribute(name = "name")
                protected String name;
                @XmlAttribute(name = "usage")
                protected String usage;
                @XmlAttribute(name = "double_format")
                protected String doubleFormat;

                @Override
                public String toString() {
                    return name;
                }

                public String getDoubleFormat() {
                    return doubleFormat;
                }

                public void setDoubleFormat(String doubleFormat) {
                    this.doubleFormat = doubleFormat;
                }

                /**
                 * Gets the value of the tooltip property.
                 *
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object. This is
                 * why there is not a <CODE>set</CODE> method for the tooltip
                 * property.
                 *
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getTooltip().add(newItem);
                 * </pre>
                 *
                 *
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link CriterionTree.Supergroup.Group.Criterion.Tooltip }
                 *
                 *
                 * @return
                 */
                public List<CriterionTree.Supergroup.Group.Criterion.Tooltip> getTooltip() {
                    if (tooltip == null) {
                        tooltip = new ArrayList<>();
                    }
                    return this.tooltip;
                }

                /**
                 * Ruft den Wert der cpname-Eigenschaft ab.
                 *
                 * @return possible object is {@link String }
                 *
                 */
                public String getCpname() {
                    return cpname;
                }

                /**
                 * Legt den Wert der cpname-Eigenschaft fest.
                 *
                 * @param value allowed object is {@link String }
                 *
                 */
                public void setCpname(String value) {
                    this.cpname = value;
                }

                /**
                 * Ruft den Wert der criterionType-Eigenschaft ab.
                 *
                 * @return possible object is {@link String }
                 *
                 */
                public String getCriterionType() {
                    return criterionType;
                }

                /**
                 * Legt den Wert der criterionType-Eigenschaft fest.
                 *
                 * @param value allowed object is {@link String }
                 *
                 */
                public void setCriterionType(String value) {
                    this.criterionType = value;
                }

                /**
                 * Ruft den Wert der displayName-Eigenschaft ab.
                 *
                 * @return possible object is {@link String }
                 *
                 */
                public String getDisplayName() {
                    return displayName == null?name:displayName;
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
                 * Ruft den Wert der usage-Eigenschaft ab.
                 *
                 * @return possible object is {@link String }
                 *
                 */
                public String getUsage() {
                    return usage;
                }

                /**
                 * Legt den Wert der usage-Eigenschaft fest.
                 *
                 * @param value allowed object is {@link String }
                 *
                 */
                public void setUsage(String value) {
                    this.usage = value;
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
                    final Criterion other = (Criterion) obj;
                    if (!Objects.equals(this.cpname, other.cpname)) {
                        return false;
                    }
                    if (!Objects.equals(this.displayName, other.displayName)) {
                        return false;
                    }
                    if (!Objects.equals(this.name, other.name)) {
                        return false;
                    }
                    return true;
                }

                @Override
                public int hashCode() {
                    int hash = 3;
                    hash = 29 * hash + Objects.hashCode(this.tooltip);
                    hash = 29 * hash + Objects.hashCode(this.cpname);
                    hash = 29 * hash + Objects.hashCode(this.displayName);
                    hash = 29 * hash + Objects.hashCode(this.name);
                    hash = 29 * hash + Objects.hashCode(this.usage);
                    return hash;
                }

                /**
                 * <p>
                 * Java-Klasse f�r anonymous complex type.
                 *
                 * <p>
                 * Das folgende Schemafragment gibt den erwarteten Content an,
                 * der in dieser Klasse enthalten ist.
                 *
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
                 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 *
                 *
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Tooltip {

                    @XmlAttribute(name = "cpname")
                    protected String cpname;
                    @XmlAttribute(name = "description")
                    protected String description;
                    @XmlAttribute(name = "value")
                    protected String value;

                    /**
                     * Ruft den Wert der cpname-Eigenschaft ab.
                     *
                     * @return possible object is {@link String }
                     *
                     */
                    public String getCpname() {
                        return cpname;
                    }

                    /**
                     * Legt den Wert der cpname-Eigenschaft fest.
                     *
                     * @param value allowed object is {@link String }
                     *
                     */
                    public void setCpname(String value) {
                        this.cpname = value;
                    }

                    /**
                     * Ruft den Wert der description-Eigenschaft ab.
                     *
                     * @return possible object is {@link String }
                     *
                     */
                    public String getDescription() {
                        return description;
                    }

                    /**
                     * Legt den Wert der description-Eigenschaft fest.
                     *
                     * @param value allowed object is {@link String }
                     *
                     */
                    public void setDescription(String value) {
                        this.description = value;
                    }

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }

                }

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
             *       &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
             *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             *
             *
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Tooltip {

                @XmlAttribute(name = "cpname")
                protected String cpname;
                @XmlAttribute(name = "description")
                protected String description;

                /**
                 * Ruft den Wert der cpname-Eigenschaft ab.
                 *
                 * @return possible object is {@link String }
                 *
                 */
                public String getCpname() {
                    return cpname;
                }

                /**
                 * Legt den Wert der cpname-Eigenschaft fest.
                 *
                 * @param value allowed object is {@link String }
                 *
                 */
                public void setCpname(String value) {
                    this.cpname = value;
                }

                /**
                 * Ruft den Wert der description-Eigenschaft ab.
                 *
                 * @return possible object is {@link String }
                 *
                 */
                public String getDescription() {
                    return description;
                }

                /**
                 * Legt den Wert der description-Eigenschaft fest.
                 *
                 * @param value allowed object is {@link String }
                 *
                 */
                public void setDescription(String value) {
                    this.description = value;
                }

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
         *       &lt;attribute name="cpname" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Tooltip {

            @XmlAttribute(name = "cpname")
            protected String cpname;
            @XmlAttribute(name = "description")
            protected String description;

            /**
             * Ruft den Wert der cpname-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getCpname() {
                return cpname;
            }

            /**
             * Legt den Wert der cpname-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setCpname(String value) {
                this.cpname = value;
            }

            /**
             * Ruft den Wert der description-Eigenschaft ab.
             *
             * @return possible object is {@link String }
             *
             */
            public String getDescription() {
                return description;
            }

            /**
             * Legt den Wert der description-Eigenschaft fest.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setDescription(String value) {
                this.description = value;
            }

        }

    }

}
