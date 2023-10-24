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

import de.lb.cpx.server.commonDB.model.CWmListReminderSubject;
import de.lb.cpx.server.commonDB.model.CWmListReminderSubject_;
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
public class CWmListReminderSubjectDao extends AbstractInternalIdDao<CWmListReminderSubject> {

    /**
     * Creates a new instance.
     */
    public CWmListReminderSubjectDao() {
        super(CWmListReminderSubject.class);
    }

    /**
     * Get all valid and undeleted entries from table C_WM_LIST_REMINDER_SUBJECT
     *
     * @return entries list
     */
    public List<CWmListReminderSubject> getWmListReminderSubjectCatalog() {
        List<CWmListReminderSubject> list;
        Query query = getEntityManager().createQuery("from " + CWmListReminderSubject.class.getSimpleName() + " b where b.wmRsDeleted=0 order by b.wmRsSort, b.wmRsValidFrom, b.wmRsValidTo");
        list = query.getResultList();
        return list;
    }

    /**
     * Get all valid and undeleted entries from table C_WM_LIST_REMINDER_SUBJECT
     *
     * @return entries list
     */
    public List<CWmListReminderSubject> getAllValidReminderSubjects() {
        List<CWmListReminderSubject> list;
        Query query = getEntityManager().createQuery("from " + CWmListReminderSubject.class.getSimpleName() + " b where b.wmRsDeleted=0 and b.wmRsValid=1 order by b.wmRsSort, b.wmRsValidFrom, b.wmRsValidTo");
        list = query.getResultList();
        return list;
    }

    /**
     * Get all valid and undeleted entries from table C_WM_LIST_REMINDER_SUBJECT
     *
     * @param pDate date that specifies validity
     * @return entries list
     */
    public List<CWmListReminderSubject> getAllAvailableReminderSubjects(Date pDate) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CWmListReminderSubject> query = criteriaBuilder.createQuery(CWmListReminderSubject.class);
        Root<CWmListReminderSubject> from = query.from(CWmListReminderSubject.class);
        Predicate deleteCondition = criteriaBuilder.equal(from.get(CWmListReminderSubject_.wmRsDeleted), 0);
        Predicate validCondition = criteriaBuilder.equal(from.get(CWmListReminderSubject_.wmRsValid), 1);
        Predicate startCondition = criteriaBuilder.or(criteriaBuilder.isNull(from.get(CWmListReminderSubject_.wmRsValidFrom)), criteriaBuilder.lessThanOrEqualTo(from.get(CWmListReminderSubject_.wmRsValidFrom), pDate));
        Predicate endCondition = criteriaBuilder.or(criteriaBuilder.isNull(from.get(CWmListReminderSubject_.wmRsValidTo)), criteriaBuilder.greaterThanOrEqualTo(from.get(CWmListReminderSubject_.wmRsValidTo), pDate));
        Predicate finalCondition = criteriaBuilder.and(startCondition, endCondition);
        query.where(deleteCondition, validCondition, finalCondition);
        query.orderBy(criteriaBuilder.asc(from.get(CWmListReminderSubject_.wmRsSort)));
        TypedQuery<CWmListReminderSubject> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }
//
//    /**
//     * Add new reminder subject
//     *
//     * @param pCWmListReminderSubject reminder subject
//     * @return id for new reminder subject
//     */
//    public long addNewReminderSubject(CWmListReminderSubject pCWmListReminderSubject) {
//        return addNewItem(pCWmListReminderSubject);
////        persist(pCWmListReminderSubject);
////        flush();
////        long id = pCWmListReminderSubject.getId();
////        return id;
//    }

//    /**
//     * update the reminder subject
//     *
//     * @param pCWmListReminderSubject reminder subject to update
//     * @return state of update process
//     */
//    public boolean updateReminderSubject(CWmListReminderSubject pCWmListReminderSubject) {
//        boolean checkUpdateState = false;
//        if (pCWmListReminderSubject.getId() <= 0L) {
//            checkUpdateState = false;
//        } else {
//            merge(pCWmListReminderSubject);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//
//    /**
//     * update the reminder subject list
//     *
//     * @param pCWmListReminderSubjectList reminder subject list to update
//     * @return state of update process
//     */
//    public boolean updateReminderSubjectList(List<CWmListReminderSubject> pCWmListReminderSubjectList) {
//        boolean checkUpdateState = false;
//        if (!pCWmListReminderSubjectList.isEmpty()) {
//            mergeList(pCWmListReminderSubjectList);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//    /**
//     * Get reminder subject item by id
//     *
//     * @param pCWmListReminderSubjectId reminder subject id
//     * @return searched reminder subject
//     */
//    public CWmListReminderSubject getReminderSubjectById(Long pCWmListReminderSubjectId) {
//        List<CWmListReminderSubject> lResults = null;
//        String queryName = "select u from " + CWmListReminderSubject.class.getSimpleName() + " u where id =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pCWmListReminderSubjectId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
//    /**
//     * Get reminder subject item by internal id
//     *
//     * @param pCWmListReminderSubjectInternalId reminder subject internal id
//     * @return searched reminder subject
//     */
//    public CWmListReminderSubject getReminderSubjectByInternalId(Long pCWmListReminderSubjectInternalId) {
//        List<CWmListReminderSubject> lResults = null;
//        String queryName = "select u from " + CWmListReminderSubject.class.getSimpleName() + " u where wmRsInternalId =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pCWmListReminderSubjectInternalId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
    /**
     * Inactivate reminder subject by id
     *
     * @param pCWmListReminderSubject reminder subject to inactivate
     * @return boolean value for inactivate state
     */
    public boolean inactivateReminderSubject(CWmListReminderSubject pCWmListReminderSubject) {
        boolean status;
        if (pCWmListReminderSubject.getId() <= 0L) {
            status = false;
        } else {
            pCWmListReminderSubject.setWmRsDeleted(true);
            pCWmListReminderSubject.setWmRsSort(-1);
            merge(pCWmListReminderSubject);
            status = true;
        }
        return status;
    }

    @Override
    public String getInternalIdSequence() {
        return "C_WM_LIST_RS_INTERNAL_ID_SQ";
    }

    @Override
    public SingularAttribute<CWmListReminderSubject, Long> getInternalIdField() {
        return CWmListReminderSubject_.wmRsInternalId;
    }

//    /**
//     * Get sequence for internal id of reminder subject
//     *
//     * @return sequence id
//     */
//    public Long getNextReminderSubjectInternalId() {
//        Long intrenalIdSequence = 0L;
//        try {
//            if (isOracle()) {
//                String queryName = "SELECT C_WM_LIST_RS_INTERNAL_ID_SQ.NEXTVAL FROM DUAL";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Long.valueOf(query.getResultList().get(0).toString());
//            } else if (isSqlSrv()) {
//                String queryName = "SELECT NEXT VALUE FOR C_WM_LIST_RS_INTERNAL_ID_SQ";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Long.valueOf(query.getResultList().get(0).toString());
////                if (intrenalIdSequence != 0L) {
////                    String isertQueryName= "INSERT INTO [dbo].[C_WM_LIST_RS_INTERNAL_ID_SQ] ([next_val]) VALUES (:nextValue)";
////                   javax.persistence.Query qry = getEntityManager().createNativeQuery(isertQueryName).setParameter("nextValue", intrenalIdSequence+1);
////                   boolean ret = (qry.executeUpdate() == 1);
////                } 
//            }
//        } catch (NumberFormatException ex) {
//            LOG.log(Level.SEVERE, "Can't get sequence for internal id of reminder subject", ex);
//        }
//        return intrenalIdSequence;
//    }
    /**
     * Get all entries from table of deadlines
     *
     * @return entries list
     */
    public List<CWmListReminderSubject> getReminderTypeList() {
        List<CWmListReminderSubject> list;
        Query query = getEntityManager().createQuery("select a from " + CWmListReminderSubject.class.getSimpleName() + " a where a.wmRsDeleted=0 order by a.wmRsSort, a.wmRsValidFrom, a.wmRsValidTo");
        list = query.getResultList();
        return list;
    }

    public Map<Long, CWmListReminderSubject> getReminderSubjects() {
        return getMenuCacheItems();
    }

    public Map<Long, CWmListReminderSubject> getMenuCacheItems() {
        return MenuCacheEntity.toMap(findAll());
    }

}
