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
package de.lb.cpx.server.dao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.hibernate.jdbc.Work;

/**
 *
 * @author niemeier
 */
@Singleton
@LocalBean
@TransactionManagement(TransactionManagementType.BEAN)
public class ConnectionProducer {
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    private Connection conn;

    @Inject
    private TCaseDao caseDao;

    @Produces
    @CpxConnection
    public synchronized Connection getConnection() {
        if (conn == null) {
            caseDao.getSession().doWork(new Work() {
                @Override
                public void execute(final Connection connection) throws SQLException {
                    conn = connection;
                }
            });
        }
        return conn;
    }

}
