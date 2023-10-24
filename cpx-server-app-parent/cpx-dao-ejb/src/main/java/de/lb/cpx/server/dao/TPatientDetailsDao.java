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
package de.lb.cpx.server.dao;

import de.lb.cpx.model.TPatientDetails;
import de.lb.cpx.model.TPatientDetails_;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * TCasePatientDao, grands single access to TPatientDetails Entities for update
 * and saving
 *
 * @author wilde
 */
@Stateless
public class TPatientDetailsDao extends AbstractCpxDao<TPatientDetails> {

    /**
     * Creates a new instance.
     */
    public TPatientDetailsDao() {
        super(TPatientDetails.class);
    }

    public TPatientDetails findActualDetails(long id) {
//        try {
//            final TypedQuery<TPatientDetails> query = getEntityManager()
//                    .createQuery("from TPatientDetails e where e.patient =:patNumber and e.patdIsActualFl = 1", TPatientDetails.class);
//            query.setParameter("patNumber", id);
//            return getSingleResultOrNull(query);
//        } catch (JDBCConnectionException ex) {
//            LOG.log(Level.WARNING, "can not load Patient, JDBCConnection Error", ex);
//        }
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TPatientDetails> query = criteriaBuilder.createQuery(TPatientDetails.class);

        Root<TPatientDetails> from = query.from(TPatientDetails.class);
//        from.fetch(TPatient_.insurances,JoinType.LEFT);
//        from.fetch(TPatient_.patientDetailList);
        Predicate where1 = criteriaBuilder.equal(from.get(TPatientDetails_.patient), id);
        Predicate where2 = criteriaBuilder.equal(from.get(TPatientDetails_.patdIsActualFl), 1);
        query.where(where1, where2);

        TypedQuery<TPatientDetails> criteriaQuery = getEntityManager().createQuery(query);

//        EntityGraph<TPatient> toFetch = getEntityManager().createEntityGraph(TPatient.class);
//        toFetch.addSubgraph(TPatient_.insurances);
//        toFetch.addSubgraph(TPatient_.patientDetailList);
//        //toFetch.addAttributeNodes(TPatient_.patDetailsActual);
//        toFetch.addSubgraph(TPatient_.cases);
//        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
        return getSingleResultOrNull(criteriaQuery);
    }

}
