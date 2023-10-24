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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.server.commonDB.model.CdbSequence;
import javax.ejb.Stateless;

/**
 * Dao to access Sequence table
 *
 * @author wilde
 */
@Stateless
public class CdbSequenceDao extends AbstractCommonDao<CdbSequence> {

    /**
     * Creates a new instance.
     */
    public CdbSequenceDao() {
        super(CdbSequence.class);
    }

//    /**
//     * get the last workflow number if nothing is found a newly created sequence
//     * object is returned ToDo: needs to be tested, only the first sequence
//     * object found is considert valid, there is no fall back if somehow a
//     * second sequence object is found
//     *
//     * @return last workflow number
//     */
//    public CdbSequence getLastWorkflowNumber() {
//        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
//
//        CriteriaQuery<CdbSequence> query = criteriaBuilder.createQuery(CdbSequence.class);
//
//        Root<CdbSequence> from = query.from(CdbSequence.class);
//
//        TypedQuery<CdbSequence> criteriaQuery = getEntityManager().createQuery(query);
//        CdbSequence seq = getSingleResultOrNull(criteriaQuery);
//        if (seq == null) {
//            LOG.warning("Can not find sequence create new one");
//            seq = new CdbSequence();
//            seq.setWorkflowNumberSeq("0");
//            persist(seq);
//        }
//        return seq;
//    }
    /**
     * get the next workflow number
     *
     * @return new workflow number
     */
    public long getNextWorkflowNumber() {
        final String sequenceName = "CDB_SEQUENCE_SQ";
        return getNextSequenceNumber(sequenceName);
//        final String query;
//        if (isOracle()) {
//            query = "SELECT " + sequenceName + ".nextval PROCESS_NUMBER FROM DUAL";
//        } else {
//            query = "SELECT NEXT VALUE FOR " + sequenceName + " PROCESS_NUMBER";
//        }
//        ReturningWork<Long> work = new ReturningWork<Long>() {
//            @Override
//            public Long execute(Connection connection) throws SQLException {
//                //SQLXML xmlData = connection.createSQLXML();
//                //xmlData.setString(props);
//                Long processNumber = null;
//                try (PreparedStatement stmt = connection.prepareStatement(query)) {
//                    //stmt.setSQLXML(1, xmlData);
//                    //stmt.setLong(1, pUserId);
//                    try (ResultSet rs = stmt.executeQuery()) {
//                        while (rs.next()) {
//                            processNumber = rs.getLong("PROCESS_NUMBER");
//                            break;
//                        }
//                    }
//                    return processNumber;
//                }
//            }
//        };
//
//        Session session = getSession();
//        return session.doReturningWork(work);        
//        
//        
//        try (ResultSet rs = pCommonDbQry.executeQuery(query)) {
//            while (rs.next()) {
//                processNumber = rs.getLong("PROCESS_NUMBER");
//            }
//        }
//        return processNumber;
    }

    public long getNextSendingInkaNumber() {
        final String sequenceName = "CURRENT_NO_4_SENDING_INKA_SQ";
        return getNextSequenceNumber(sequenceName);
    }

}
