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

import de.lb.cpx.model.TInsurance;
import de.lb.cpx.model.TInsurance_;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.TPatient_;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.server.commons.enums.EntityGraphType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.exception.JDBCConnectionException;

/**
 * Data access object for domain model class TPatient. Initially generated at
 * 21.01.2016 17:14:39 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SuppressWarnings("unchecked")
public class TPatientDao extends AbstractCpxDao<TPatient> {

    private static final Logger LOG = Logger.getLogger(TPatientDao.class.getName());

    /**
     * Creates a new instance.
     */
    public TPatientDao() {
        super(TPatient.class);
    }

    public TPatient findByPatNumber(final String patNumber) {
        try {
            final TypedQuery<TPatient> query = getEntityManager()
                    .createQuery("from TPatient e where e.patNumber =:patNumber", TPatient.class);
            query.setParameter("patNumber", patNumber);
            return getSingleResultOrNull(query);
        } catch (JDBCConnectionException ex) {
            LOG.log(Level.WARNING, "can not load patient with number '" + patNumber + "', JDBCConnection Error", ex);
        }
        return null;
    }

    public TPatient findPatientByIdEager(Long id) {

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TPatient> query = criteriaBuilder.createQuery(TPatient.class);

        Root<TPatient> from = query.from(TPatient.class);
//        from.fetch(TPatient_.insurances,JoinType.LEFT);
//        from.fetch(TPatient_.patientDetailList);
        query.where(criteriaBuilder.equal(from.get(TPatient_.id), id));

        TypedQuery<TPatient> criteriaQuery = getEntityManager().createQuery(query);

        EntityGraph<TPatient> toFetch = getEntityManager().createEntityGraph(TPatient.class);

        toFetch.addSubgraph(TPatient_.insurances);
        //toFetch.addSubgraph(TPatient_.patInsuranceActual);
        toFetch.addSubgraph(TPatient_.patientDetailList);
        //toFetch.addAttributeNodes(TPatient_.patDetailsActual);

        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);

        //return criteriaQuery.getSingleResult();
        return getSingleResultOrNull(criteriaQuery);
    }

    public List<String> getMatchForPatientNumber(String number) {

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<String> query = criteriaBuilder.createQuery(String.class);

        Root<TPatient> from = query.from(TPatient.class);

        query.select(from.get(TPatient_.patNumber));

        query.where(criteriaBuilder.like(criteriaBuilder.lower(from.get(TPatient_.patNumber)), number.toLowerCase() + "%"));
        List<String> results = getEntityManager().createQuery(query).getResultList();
        LOG.log(Level.FINE, "result list size for patient number '" + number + "': " + results.size());
        return results;
    }

    public TPatient getPatientByPatientNumber(String patientNumber) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TPatient> query = criteriaBuilder.createQuery(TPatient.class);

        Root<TPatient> from = query.from(TPatient.class);
//        from.fetch(TPatient_.insurances,JoinType.LEFT);
//        from.fetch(TPatient_.patientDetailList);
        Predicate where = criteriaBuilder.equal(from.get(TPatient_.patNumber), patientNumber);

        query.where(where);

        TypedQuery<TPatient> criteriaQuery = getEntityManager().createQuery(query);

        EntityGraph<TPatient> toFetch = getEntityManager().createEntityGraph(TPatient.class);

        toFetch.addSubgraph(TPatient_.insurances);
        toFetch.addSubgraph(TPatient_.patientDetailList);
        //toFetch.addAttributeNodes(TPatient_.patDetailsActual);
        toFetch.addSubgraph(TPatient_.cases);
        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);

        return getSingleResultOrNull(criteriaQuery);
    }

    /**
     * returns list of case ids - used for tests
     *
     * @return list of case ids
     */
    public List<Long> getAllPatientIds() {
        final TypedQuery<Long> query = getEntityManager().createQuery("select id from TPatient c", Long.class);
        return query.getResultList();

    }

    /**
     * returns list of case ids - used for tests
     *
     * @return list of case ids
     */
    public List<Integer> findAllPatientIds() {
        String sql = "SELECT \n"
                + "  T_PATIENT.ID\n"
                + //"  --, COUNT(*) CASE_COUNT \n" +
                "FROM T_PATIENT\n";

//        TypedQuery<Long> query = getEntityManager().createQuery(sql, Long.class);
        Query query = getEntityManager().createNativeQuery(sql);
        return query.getResultList();
    }

    public List<Integer> findPatientIdsForMerging(CaseTypeEn pCsCaseType) {
        String sql = "SELECT \n"
                + "  T_PATIENT.ID\n"
                + //"  --, COUNT(*) CASE_COUNT \n" +
                "FROM T_PATIENT\n"
                + "INNER JOIN T_CASE ON T_CASE.T_PATIENT_ID = T_PATIENT.ID\n"
                + "WHERE T_CASE.CS_CASE_TYPE_EN = :ct \n"
                + "GROUP BY T_PATIENT.ID\n"
                + "HAVING COUNT(*) > 1";
//        TypedQuery<Long> query = getEntityManager().createQuery(sql, Long.class);
        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter("ct", pCsCaseType.name());
        return query.getResultList();

    }

    public TInsurance findActualInsurance(long id) {
        LOG.log(Level.INFO, "find actual insurance for patient with id " + id);
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TInsurance> query = criteriaBuilder.createQuery(TInsurance.class);

        Root<TInsurance> from = query.from(TInsurance.class);
//        from.fetch(TPatient_.insurances,JoinType.LEFT);
//        from.fetch(TPatient_.patientDetailList);
        query.where(criteriaBuilder.equal(from.get(TInsurance_.patient), id), criteriaBuilder.equal(from.get(TInsurance_.insIsActualFl), 1));

        TypedQuery<TInsurance> criteriaQuery = getEntityManager().createQuery(query);

        final List<TInsurance> result = criteriaQuery.getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);

        //return criteriaQuery.getSingleResult(); //Can be empty and throws a NoResultException: No entity found for query
    }

    public Integer getCount() {
        String query = "SELECT COUNT(*) CNT FROM T_PATIENT ";
        List<Number> list = getEntityManager().createNativeQuery(query).getResultList();
        int count = 0;
        if (list != null) {
            for (Number cnt : list) {
                if (cnt == null) {
                    continue;
                }
                count = cnt.intValue();
            }
        }
        return count;
    }

}
