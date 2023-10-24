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
package de.lb.cpx.server.commons.test.dao;

import java.sql.Statement;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;

public final class DBUtil {

    private DBUtil() {
    }

    public static void executeSQLScripts(final TransactionManager transactionContainer,
            final String... scripts) {

        final Session session = (Session) transactionContainer.getEntityManager().getDelegate();

        //final Dialect dialect = ((SessionFactoryImplementor) session.getSessionFactory()).getDialect();
        final Dialect dialect = ((SessionFactoryImplementor) session.getSessionFactory()).getJdbcServices().getDialect();
        if (dialect instanceof H2Dialect) {
            session.doWork(connection -> {
                try ( Statement executeScriptStmt = connection.createStatement()) {
                    for (final String script : scripts) {
                        executeScriptStmt.execute("RUNSCRIPT FROM 'classpath:" + script + "'");
                    }
                }
            });
        }
    }

}
