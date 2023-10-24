/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model;

import org.hibernate.boot.model.relational.AbstractAuxiliaryDatabaseObject;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.SQLServerDialect;

/**
 *
 * @author niemeier
 */
public abstract class CpxAuxiliaryDatabaseObject extends AbstractAuxiliaryDatabaseObject {

    private static final long serialVersionUID = 1L;

    public static boolean isOracle(final Dialect pDialect) {
        return pDialect instanceof Oracle10gDialect;
    }

    public static boolean isSqlsrv(final Dialect pDialect) {
        return pDialect instanceof SQLServerDialect;
    }

}
