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
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.server.commonDB.model.CCase;
import de.lb.cpx.server.commonDB.model.CCase_;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

/**
 * Data access object for domain model class CDoctor. Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SuppressWarnings("unchecked")
public class CCaseDao extends AbstractCommonDao<CCase> {

    private static final Logger LOG = Logger.getLogger(CCaseDao.class.getName());

    /**
     * Creates a new instance.
     */
    public CCaseDao() {
        super(CCase.class);
    }

    public CCase findByCaseId(final long pCaseId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<CCase> query = criteriaBuilder.createQuery(CCase.class);

        Root<CCase> from = query.from(CCase.class);
        query.where(criteriaBuilder.equal(from.get(CCase_.caseId), pCaseId));

        TypedQuery<CCase> criteriaQuery = getEntityManager().createQuery(query);
        //entityGraph, should maybe deleted, perfornance dropoff in linked lists
        //awi:20170316
        return getSingleResultOrNull(criteriaQuery);
    }

//    public List<CCase> findAllCases() {
//        long startTime = System.currentTimeMillis();
//        LOG.log(Level.INFO, "Find all common cases");
//        @SuppressWarnings("deprecation")
//        Criteria criteriaQuery = getSession().createCriteria(CCase.class)
//                //.add(Restrictions.eq(CCase_.process.getName() + ".id", pProcessId))
//                .addOrder(Order.desc(CCase_.creationDate.getName()))
//                .setProjection(Projections.projectionList()
//                        .add(Projections.property(CCase_.id.getName()), CCase_.id.getName())
//                        .add(Projections.property(CCase_.caseId.getName()), CCase_.caseId.getName())
//                        .add(Projections.property(CCase_.csHospitalIdent.getName()), CCase_.csHospitalIdent.getName())
//                        .add(Projections.property(CCase_.csCaseNumber.getName()), CCase_.csCaseNumber.getName())
//                        .add(Projections.property(CCase_.db.getName()), CCase_.db.getName())
//                        .add(Projections.property(CCase_.creationDate.getName()), CCase_.creationDate.getName())
//                        .add(Projections.property(CCase_.creationUser.getName()), CCase_.creationUser.getName())
//                        .add(Projections.property(CCase_.modificationDate.getName()), CCase_.modificationDate.getName())
//                        .add(Projections.property(CCase_.modificationUser.getName()), CCase_.modificationUser.getName())
//                        //.add(Projections.property(CCase_.modificationUserId.getName()), CCase_.modificationUserId.getName())
//                        .add(Projections.property(CCase_.version.getName()), CCase_.version.getName()))
//                //.add(Projections.property(TWmDocument_.content.getName())) //never ever do this or you will die in agony!
//                .setResultTransformer(Transformers.aliasToBean(CCase.class));
//
//        List<CCase> result = criteriaQuery.list();
//        LOG.log(Level.INFO, result.size() + " common cases found in " + (System.currentTimeMillis() - startTime) + " ms");
//
//        return result;
////        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
////        CriteriaQuery<TWmDocument> query = criteriaBuilder.createQuery(TWmDocument.class);
////        query.Root<TWmDocument> from = query.from(TWmDocument.class);
////        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
////        query.where(criteriaBuilder.equal(from.get(TWmDocument_.process), pProcessId));
////
////        TypedQuery<TWmDocument> criteriaQuery = getEntityManager().createQuery(query);
////        return criteriaQuery.getResultList();
//    }
    public List<CCase> findAllCases() {
        long startTime = System.currentTimeMillis();
        LOG.log(Level.INFO, "Find all common cases");
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CCase> query = cb.createQuery(CCase.class);
        Root<CCase> from = query.from(getEntityClass());
        query.select(getSelect(from));

        TypedQuery<CCase> criteriaQuery = getEntityManager().createQuery(query);
        final List<CCase> result = criteriaQuery.getResultList();
        LOG.log(Level.INFO, result.size() + " common cases found in " + (System.currentTimeMillis() - startTime) + " ms");

        return result;
    }

    public List<CCase> findAllCasesForUser(long pUserId) {
        long startTime = System.currentTimeMillis();
        LOG.log(Level.INFO, "Find all common cases");
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CCase> query = cb.createQuery(CCase.class);
        Root<CCase> from = query.from(getEntityClass());
        //necessary dirk should now fixed lazy loading annotation
        query.select(getSelect(from));
        query.where(cb.equal(from.get(CCase_.CREATION_USER), pUserId));
        TypedQuery<CCase> criteriaQuery = getEntityManager().createQuery(query);
        final List<CCase> result = criteriaQuery.getResultList();
        LOG.log(Level.INFO, result.size() + " common cases found in " + (System.currentTimeMillis() - startTime) + " ms");

        return result;
    }

    private Selection<CCase> getSelect(Root<CCase> pFrom) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        return cb.construct(CCase.class, pFrom.get("id"),
                pFrom.get(CCase_.CASE_ID),
                pFrom.get(CCase_.CS_HOSPITAL_IDENT),
                pFrom.get(CCase_.CS_CASE_NUMBER),
                pFrom.get(CCase_.DB),
                pFrom.get(CCase_.creationUser),
                pFrom.get(CCase_.creationDate),
                pFrom.get(CCase_.NAME),
                pFrom.get(CCase_.CATEGORY));
    }

    public byte[] getCaseContent(long id) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<byte[]> cq = qb.createQuery(byte[].class);
        Root<CCase> from = cq.from(getEntityClass());
        cq.select(from.get(CCase_.CONTENT));
        cq.where(qb.equal(from.get("id"), id));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    public Boolean deleteCasesById(List<Long> pCases) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaDelete<CCase> delete = qb.createCriteriaDelete(CCase.class);
        Root<CCase> from = delete.from(CCase.class);
//        delete.where(qb.equal(from.get("id"), pCases));
        delete.where(from.get("id").in(pCases));
        int result = getEntityManager().createQuery(delete).executeUpdate();
        if (result != pCases.size()) {
            return false;
        }
        return true;
    }

    public Collection<String> getMatchesAnalyserCaseCategory(String pText) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<String> query = criteriaBuilder.createQuery(String.class);

        Root<CCase> from = query.from(CCase.class);
        query.distinct(true);
        query.select(from.get(CCase_.CATEGORY));

        query.where(criteriaBuilder.like(criteriaBuilder.lower(from.get(CCase_.CATEGORY)), pText.toLowerCase() + "%"));
        List<String> results = getEntityManager().createQuery(query).getResultList();
        return results;
    }

}
