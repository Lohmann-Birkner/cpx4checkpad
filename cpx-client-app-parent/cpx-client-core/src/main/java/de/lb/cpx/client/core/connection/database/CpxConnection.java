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
package de.lb.cpx.client.core.connection.database;

import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Dirk Niemeier
 */
//public class CpxConnection extends org.sqlite.SQLiteConnection {
public class CpxConnection extends org.sqlite.jdbc4.JDBC4Connection {

    public CpxConnection(String url, String fileName, Properties prop) throws SQLException {
        super(url, fileName, prop);
    }

}
