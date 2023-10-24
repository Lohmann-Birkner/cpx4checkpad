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

import de.lb.cpx.server.commonDB.model.CWmListActionSubject;
import de.lb.cpx.server.commonDB.model.CWmListActionSubject_;
import de.lb.cpx.server.commons.dao.MenuCacheEntity;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

/**
 *
 * @author sklarow
 */
@Stateless
@SuppressWarnings("unchecked")
public class CWmListActionSubjectDao extends AbstractInternalIdDao<CWmListActionSubject> {

    /**
     * Creates a new instance.
     */
    public CWmListActionSubjectDao() {
        super(CWmListActionSubject.class);
    }

    /**
     * Get all undeleted entries from table C_WM_LIST_ACTION_SUBJECT
     *
     * @return entries list
     */
    public List<CWmListActionSubject> getWmListActionSubjectCatalog() {
        List<CWmListActionSubject> list;
        Query query = getEntityManager().createQuery("from " + CWmListActionSubject.class.getSimpleName() + " b where b.wmAsDeleted=0 order by b.wmAsSort, b.wmAsValidFrom, b.wmAsValidTo");
        list = query.getResultList();
        return list;
    }

    /**
     * Get all valid and undeleted entries from table C_WM_LIST_ACTION_SUBJECT
     *
     * @return entries list
     */
    public List<CWmListActionSubject> getAllValidActionSubjects() {
        List<CWmListActionSubject> list;
        Query query = getEntityManager().createQuery("from " + CWmListActionSubject.class.getSimpleName() + " b where b.wmAsDeleted=0 and b.wmAsValid=1 order by b.wmAsSort, b.wmAsValidFrom, b.wmAsValidTo");
        list = query.getResultList();
        return list;
    }

    /**
     * Get all valid and undeleted entries from table C_WM_LIST_ACTION_SUBJECT
     *
     * @param pDate date that specifies validity
     * @return entries list
     */
    public List<CWmListActionSubject> getAllAvailableActionSubjects(Date pDate) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CWmListActionSubject> query = criteriaBuilder.createQuery(CWmListActionSubject.class);
        Root<CWmListActionSubject> from = query.from(CWmListActionSubject.class);
        Predicate deleteCondition = criteriaBuilder.equal(from.get(CWmListActionSubject_.wmAsDeleted), 0);
        Predicate validCondition = criteriaBuilder.equal(from.get(CWmListActionSubject_.wmAsValid), 1);
        Predicate startOne = criteriaBuilder.isNull(from.get(CWmListActionSubject_.wmAsValidFrom));
        Predicate startTwo = criteriaBuilder.lessThanOrEqualTo(from.get(CWmListActionSubject_.wmAsValidFrom), pDate);
        Predicate startCondition = criteriaBuilder.or(startOne, startTwo);
        Predicate endOne = criteriaBuilder.isNull(from.get(CWmListActionSubject_.wmAsValidTo));
        Predicate endTwo = criteriaBuilder.greaterThanOrEqualTo(from.get(CWmListActionSubject_.wmAsValidTo), pDate);
        Predicate endCondition = criteriaBuilder.or(endOne, endTwo);
        Predicate finalCondition = criteriaBuilder.and(startCondition, endCondition);
        query.where(deleteCondition, validCondition, finalCondition);
        query.orderBy(criteriaBuilder.asc(from.get(CWmListActionSubject_.wmAsSort)));
        TypedQuery<CWmListActionSubject> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

//    /**
//     * Add new action subject
//     *
//     * @param pCWmListActionSubject action subject
//     * @return id for new action subject
//     */
//    public long addNewActionSubject(CWmListActionSubject pCWmListActionSubject) {
//        return addNewItem(pCWmListActionSubject);
////        persist(pCWmListActionSubject);
////        flush();
////        long id = pCWmListActionSubject.getId();
////        return id;
//    }
    /**
     * update the action subject
     *
     * @param pCWmListActionSubject action subject to update
     * @return state of update process
     */
//    public boolean updateActionSubject(CWmListActionSubject pCWmListActionSubject) {
//        boolean checkUpdateState = false;
//        if (pCWmListActionSubject.getId() <= 0L) {
//            checkUpdateState = false;
//        } else {
//            merge(pCWmListActionSubject);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//
//    /**
//     * update the action subject list
//     *
//     * @param pCWmListActionSubjectList action subject list to update
//     * @return state of update process
//     */
//    public boolean updateActionSubjectList(List<CWmListActionSubject> pCWmListActionSubjectList) {
//        boolean checkUpdateState = false;
//        if (!pCWmListActionSubjectList.isEmpty()) {
//            mergeList(pCWmListActionSubjectList);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//    /**
//     * Get action subject item by id
//     *
//     * @param pCWmListActionSubjectId action subject id
//     * @return searched action subject
//     */
//    public CWmListActionSubject getActionSubjectById(Long pCWmListActionSubjectId) {
//        List<CWmListActionSubject> lResults = null;
//        String queryName = "select u from " + CWmListActionSubject.class.getSimpleName() + " u where id =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pCWmListActionSubjectId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
//    public CWmListActionSubject getActionSubjectByInternalId(Long pCWmListActionSubjectInternalId) {
//        List<CWmListActionSubject> lResults = null;
//        String queryName = "select u from " + CWmListActionSubject.class.getSimpleName() + " u where wmAsInternalId =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pCWmListActionSubjectInternalId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
    /**
     * Inactivate action subject by id
     *
     * @param pCWmListActionSubject action subject to inactivate
     * @return boolean value for inactivate state
     */
    public boolean inactivateActionSubject(CWmListActionSubject pCWmListActionSubject) {
        boolean status;
        if (pCWmListActionSubject.getId() <= 0L) {
            status = false;
        } else {
            pCWmListActionSubject.setWmAsDeleted(true);
            pCWmListActionSubject.setWmAsSort(-1);
            merge(pCWmListActionSubject);
            status = true;
        }
        return status;
    }

    @Override
    public String getInternalIdSequence() {
        return "C_WM_LIST_AS_INTERNAL_ID_SQ";
    }

    @Override
    public SingularAttribute<CWmListActionSubject, Long> getInternalIdField() {
        return CWmListActionSubject_.wmAsInternalId;
    }

//    /**
//     * Get sequence for internal id of action subject
//     *
//     * @return sequence id
//     */
//    public Long getNextActionSubjectInternalId() {
//        Long intrenalIdSequence = 0L;
//        try {
//            if (isOracle()) {
//                String queryName = "SELECT C_WM_LIST_AS_INTERNAL_ID_SQ.NEXTVAL FROM DUAL";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Long.valueOf(query.getResultList().get(0).toString());
//            } else if (isSqlSrv()) {
//                String queryName = "SELECT NEXT VALUE FOR C_WM_LIST_AS_INTERNAL_ID_SQ";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Long.valueOf(query.getResultList().get(0).toString());
////                if (intrenalIdSequence != 0L) {
////                    String isertQueryName= "INSERT INTO [dbo].[C_WM_LIST_AS_INTERNAL_ID_SQ] ([next_val]) VALUES (:nextValue)";
////                   javax.persistence.Query qry = getEntityManager().createNativeQuery(isertQueryName).setParameter("nextValue", intrenalIdSequence+1);
////                   boolean ret = (qry.executeUpdate() == 1);
////                }
//
//            }
//        } catch (NumberFormatException ex) {
//            LOG.log(Level.SEVERE, "Can't get sequence for internal id of action subject", ex);
//        }
//        return intrenalIdSequence;
//    }
    public Map<Long, CWmListActionSubject> getActionSubjects() {
        return getMenuCacheItems();
    }

    public Map<Long, CWmListActionSubject> getMenuCacheItems() {
        return MenuCacheEntity.toMap(findAll());
    }

}
