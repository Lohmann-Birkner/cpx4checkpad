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

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the test package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: test
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RulesOperator }
     *
     * @return rules operator
     */
    public RulesOperator createRulesOperator() {
        return new RulesOperator();
    }

    /**
     * Create an instance of {@link Sugg }
     *
     * @return suggestion
     */
    public Sugg createSugg() {
        return new Sugg();
    }

    /**
     * Create an instance of {@link Rule }
     *
     * @return rule
     */
    public Rule createRule() {
        return new Rule();
    }

    /**
     * Create an instance of {@link RulesElement }
     *
     * @return rules element
     */
    public RulesElement createRulesElement() {
        return new RulesElement();
    }

    /**
     * Create an instance of {@link RulesValue }
     *
     * @return rules value
     */
    public RulesValue createRulesValue() {
        return new RulesValue();
    }

    /**
     * Create an instance of {@link Suggestions }
     *
     * @return suggestions
     */
    public Suggestions createSuggestions() {
        return new Suggestions();
    }

    /**
     * Create an instance of {@link CaseRules }
     *
     */
//    public CaseRules createCaseRules() {
//        return new CaseRules();
//    }
}
