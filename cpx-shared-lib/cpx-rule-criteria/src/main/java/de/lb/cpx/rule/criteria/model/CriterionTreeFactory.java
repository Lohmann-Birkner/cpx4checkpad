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
// Generiert: 2018.10.17 um 01:18:12 PM CEST 
//
package de.lb.cpx.rule.criteria.model;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the test package.
 * <p>
 * An CriterionTreeFactory allows you to programatically construct new instances
 * of the Java representation for XML content. The Java representation of XML
 * content can consist of schema derived interfaces and classes representing the
 * binding of schema type definitions, element declarations and model groups.
 * Factory methods for each of these are provided in this class.
 *
 */
@XmlRegistry
public class CriterionTreeFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: test
     *
     */
    public CriterionTreeFactory() {
    }

    /**
     * Create an instance of {@link CriterionTree }
     *
     * @return
     */
    public CriterionTree createCriterionTree() {
        return new CriterionTree();
    }

    /**
     * Create an instance of {@link CriterionTree.Supergroup }
     *
     * @return
     */
    public CriterionTree.Supergroup createCriterionTreeSupergroup() {
        return new CriterionTree.Supergroup();
    }

    /**
     * Create an instance of {@link CriterionTree.Supergroup.Group }
     *
     * @return
     */
    public CriterionTree.Supergroup.Group createCriterionTreeSupergroupGroup() {
        return new CriterionTree.Supergroup.Group();
    }

    /**
     * Create an instance of {@link CriterionTree.Supergroup.Group.Criterion }
     *
     * @return
     */
    public CriterionTree.Supergroup.Group.Criterion createCriterionTreeSupergroupGroupCriterion() {
        return new CriterionTree.Supergroup.Group.Criterion();
    }

    /**
     * Create an instance of {@link CriterionTree.Supergroup.Tooltip }
     *
     * @return
     */
    public CriterionTree.Supergroup.Tooltip createCriterionTreeSupergroupTooltip() {
        return new CriterionTree.Supergroup.Tooltip();
    }

    /**
     * Create an instance of {@link CriterionTree.Supergroup.Group.Tooltip }
     *
     * @return
     */
    public CriterionTree.Supergroup.Group.Tooltip createCriterionTreeSupergroupGroupTooltip() {
        return new CriterionTree.Supergroup.Group.Tooltip();
    }

    /**
     * Create an instance of {@link CriterionTree.Supergroup.Group.Criterion.Tooltip
     * }
     *
     * @return
     */
    public CriterionTree.Supergroup.Group.Criterion.Tooltip createCriterionTreeSupergroupGroupCriterionTooltip() {
        return new CriterionTree.Supergroup.Group.Criterion.Tooltip();
    }

}
