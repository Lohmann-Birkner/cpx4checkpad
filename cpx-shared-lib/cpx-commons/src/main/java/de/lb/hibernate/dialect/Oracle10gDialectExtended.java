/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.hibernate.dialect;

import java.sql.Types;
import org.hibernate.dialect.Oracle10gDialect;

/**
 *
 * @author Dirk Niemeier
 */
public class Oracle10gDialectExtended extends Oracle10gDialect {

    public Oracle10gDialectExtended() {
        super();
        setColumnType();
    }

    private void setColumnType() {
        registerColumnType(Types.DOUBLE, "float");
    }
}
