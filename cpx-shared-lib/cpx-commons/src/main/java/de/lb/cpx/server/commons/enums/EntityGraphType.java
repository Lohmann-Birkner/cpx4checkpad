/* 
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commons.enums;

/**
 * Enum-Class for Graphtype for the EntityGraphs used for Data-Fetching
 *
 * @author wilde
 */
public enum EntityGraphType {
    LOADGRAPH("javax.persistence.loadgraph"),
    FETCHGRAPH("javax.persistence.fetchgraph");

    private final String typeClass;

    private EntityGraphType(String type) {
        this.typeClass = type;
    }

    /**
     * returns Class of LoadGraph-Type implementation
     * javax.persistence.loadgraph, loads all Values specified in entityGraph
     * and all Eager-Attributes in the specified Entities
     *
     * @return String javax.persistence.loadgraph
     */
    public static String getLoadGraphType() {
        return LOADGRAPH.typeClass;
    }

    /**
     * returns Class of FetchGraph-Type implementation
     * javax.persistence.fetchgraph, loads all Values specified in entityGraph
     * WITHOUT Eager-Attributes in the specified Entities
     *
     * @return String javax.persistence.fetchgraph
     */
    public static String getFetchGraphType() {
        return FETCHGRAPH.typeClass;
    }

    /**
     * returns Classtype of Enum
     *
     * @return String classType
     */
    public String getTypeClass() {
        return typeClass;
    }

}
