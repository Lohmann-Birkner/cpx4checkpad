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
package de.lb.cpx.server.imp.dao;
// Generated 18.12.2015 10:49:02 by Hibernate Tools 3.2.2.GA

import de.lb.cpx.server.imp.model.P21CountLogEntry;
import de.lb.cpx.server.imp.model.P21ImportLogEntry;
import de.lb.cpx.server.imp.model.P21ImportLogEntryBase;
import de.lb.cpx.server.imp.model.P21ImportLogLevel;
import de.lb.cpx.server.imp.model.P21MeasureLogEntry;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.TypedQuery;

/**
 * Data access object for domain model class TImportOps.
 *
 *
 * @author Hibernate Tools
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class P21ImportLogEntryDao extends AbstractImportDao<P21ImportLogEntryBase> {

    /**
     * Creates a new instance.
     */
    public P21ImportLogEntryDao() {
        super(P21ImportLogEntryBase.class);
    }

    public void addLogEntry(P21ImportLogLevel level, String importId, String text) {
        P21ImportLogEntry logEntry = new P21ImportLogEntry(level, importId, text);
        persist(logEntry);
    }

    public void addMeasureLogEntry(P21ImportLogLevel level, String importId, String text,
            long duration) {
        P21MeasureLogEntry logEntry = new P21MeasureLogEntry(level, importId, text, duration);
        persist(logEntry);
    }

    public void addCountLogEntry(P21ImportLogLevel level, String importId, String text, long duration,
            long count) {
        P21CountLogEntry logEntry = new P21CountLogEntry(level, importId, text, duration, count);
        persist(logEntry);
    }

    public List<P21ImportLogEntryBase> findLogEntries(String importId) {
        final TypedQuery<P21ImportLogEntryBase> query = getEntityManager().createQuery(
                "from P21ImportLogEntryBase e where e.importId = :importId", P21ImportLogEntryBase.class);
        query.setParameter("importId", importId);
        return query.getResultList();
    }

}
