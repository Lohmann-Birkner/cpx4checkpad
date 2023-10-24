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

import de.lb.cpx.server.commonDB.model.CWmListProcessTopic;
import de.lb.cpx.server.commonDB.model.CWmListProcessTopic_;
import de.lb.cpx.server.commons.dao.MenuCacheEntity;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

/**
 *
 * @author sklarow
 */
@Stateless
@SuppressWarnings("unchecked")
public class CWmListProcessTopicDao extends AbstractInternalIdDao<CWmListProcessTopic> {

    /**
     * Creates a new instance.
     */
    public CWmListProcessTopicDao() {
        super(CWmListProcessTopic.class);
    }

    /**
     * Get all entries from table C_WM_LIST_PROCESS_TOPIC
     *
     * @return entries list
     */
    public List<CWmListProcessTopic> getWmListProcessTopicCatalog() {
        List<CWmListProcessTopic> list;
        Query query = getEntityManager().createQuery("from " + CWmListProcessTopic.class.getSimpleName() + " b where b.wmPtDeleted=0 order by b.wmPtSort, b.wmPtValidFrom, b.wmPtValidTo");
        list = query.getResultList();
        return list;
    }

    /**
     * Get all valid and undeleted entries from table C_WM_LIST_DOCUMENT_TYPE
     *
     * @return entries list
     */
    public List<CWmListProcessTopic> getAllValidDocumentTypeObjects() {
        List<CWmListProcessTopic> list;
        Query query = getEntityManager().createQuery("from " + CWmListProcessTopic.class.getSimpleName() + " b where b.wmPtDeleted=0 and b.wmPtValid=1 order by b.wmDtSort, b.wmDtValidFrom, b.wmDtValidTo");
        list = query.getResultList();
        return list;
    }

    /**
     * Get all valid and undeleted entries from table C_WM_LIST_DOCUMENT_TYPE
     *
     * @param pDate date
     * @return entries list
     */
    public List<CWmListProcessTopic> getAllValidDocumentTypeObjects(Date pDate) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CWmListProcessTopic> query = criteriaBuilder.createQuery(CWmListProcessTopic.class);
        Root<CWmListProcessTopic> from = query.from(CWmListProcessTopic.class);
        Predicate deleteCondition = criteriaBuilder.equal(from.get(CWmListProcessTopic_.wmPtDeleted), 0);
        Predicate validCondition = criteriaBuilder.equal(from.get(CWmListProcessTopic_.wmPtValid), 1);
        Predicate startCondition = criteriaBuilder.or(criteriaBuilder.isNull(from.get(CWmListProcessTopic_.wmPtValidFrom)), criteriaBuilder.lessThanOrEqualTo(from.get(CWmListProcessTopic_.wmPtValidFrom), pDate));
        Predicate endCondition = criteriaBuilder.or(criteriaBuilder.isNull(from.get(CWmListProcessTopic_.wmPtValidTo)), criteriaBuilder.greaterThanOrEqualTo(from.get(CWmListProcessTopic_.wmPtValidTo), pDate));
        Predicate finalCondition = criteriaBuilder.and(startCondition, endCondition);
        query.where(deleteCondition, validCondition, finalCondition);
        query.orderBy(criteriaBuilder.asc(from.get(CWmListProcessTopic_.wmPtName)));
        TypedQuery<CWmListProcessTopic> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

//    /**
//     * Add new process topic
//     *
//     * @param pCWmListProcessTopic process topic
//     * @return id for new process topic
//     */
//    public long addNewProcessTopic(CWmListProcessTopic pCWmListProcessTopic) {
//        return addNewItem(pCWmListProcessTopic);
////        persist(pCWmListProcessTopic);
////        flush();
////        long id = pCWmListProcessTopic.getId();
////        return id;
//    }
//    /**
//     * update the process topic
//     *
//     * @param pCWmListProcessTopic process topic to update
//     * @return state of update process
//     */
//    public boolean updateProcessTopic(CWmListProcessTopic pCWmListProcessTopic) {
//        boolean checkUpdateState = false;
//        if (pCWmListProcessTopic.getId() <= 0L) {
//            checkUpdateState = false;
//        } else {
//            merge(pCWmListProcessTopic);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//
//    /**
//     * update the process topic list
//     *
//     * @param pCWmListProcessTopicList process topic list to update
//     * @return state of update process
//     */
//    public boolean updateProcessTopicList(List<CWmListProcessTopic> pCWmListProcessTopicList) {
//        boolean checkUpdateState = false;
//        if (!pCWmListProcessTopicList.isEmpty()) {
//            mergeList(pCWmListProcessTopicList);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//    /**
//     * Get process topic item by id
//     *
//     * @param pCWmListProcessTopicId process topic id
//     * @return searched process topic
//     */
//    public CWmListProcessTopic getProcessTopicById(Long pCWmListProcessTopicId) {
//        List<CWmListProcessTopic> lResults = null;
//        String queryName = "select u from " + CWmListProcessTopic.class.getSimpleName() + " u where id =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pCWmListProcessTopicId);
//        lResults = query.getResultList();
//        if (lResults.isEmpty()) {
//            return null;
//        }
//        return (lResults == null || lResults.isEmpty()) ? null : lResults.get(0);
//    }
    /**
     * Inactivate process topic by id
     *
     * @param pCWmListProcessTopic process topic to inactivate
     * @return boolean value for inactivate state
     */
    public boolean inactivateProcessTopic(CWmListProcessTopic pCWmListProcessTopic) {
        boolean status;
        if (pCWmListProcessTopic.getId() <= 0L) {
            status = false;
        } else {
            pCWmListProcessTopic.setWmPtDeleted(true);
            pCWmListProcessTopic.setWmPtSort(-1);
            merge(pCWmListProcessTopic);
            status = true;
        }
        return status;
    }

    @Override
    public String getInternalIdSequence() {
        return "C_WM_LIST_PT_INTERNAL_ID_SQ";
    }

    @Override
    public SingularAttribute<CWmListProcessTopic, Long> getInternalIdField() {
        return CWmListProcessTopic_.wmPtInternalId;
    }

//    /**
//     * Get sequence for internal id of process topic
//     *
//     * @return sequence id
//     */
//    public Long getNextProcessTopicInternalId() {
//        Long intrenalIdSequence = 0L;
//        try {
//            if (isOracle()) {
//                String queryName = "SELECT C_WM_LIST_PT_INTERNAL_ID_SQ.NEXTVAL FROM DUAL";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Long.valueOf(query.getResultList().get(0).toString());
//            } else if (isSqlSrv()) {
//                String queryName = "SELECT NEXT VALUE FOR C_WM_LIST_PT_INTERNAL_ID_SQ";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Long.valueOf(query.getResultList().get(0).toString());
////                if (intrenalIdSequence != 0L) {
////                    String isertQueryName= "INSERT INTO [dbo].[C_WM_LIST_PT_INTERNAL_ID_SQ] ([next_val]) VALUES (:nextValue)";
////                   javax.persistence.Query qry = getEntityManager().createNativeQuery(isertQueryName).setParameter("nextValue", intrenalIdSequence+1);
////                   boolean ret = (qry.executeUpdate() == 1);
////                }
//            }
//        } catch (NumberFormatException ex) {
//            LOG.log(Level.SEVERE, "Can't get sequence for internal id of process topic", ex);
//        }
//        return intrenalIdSequence;
//    }
//    /**
//     * @return all process topics stored in the database. Warning: do not check
//     * flag isValid
//     */
//    public List<CWmListProcessTopic> getAllProcessTopics() {
//        final TypedQuery<CWmListProcessTopic> query = getEntityManager().createQuery("from CWmListProcessTopic", CWmListProcessTopic.class);
//        return query.getResultList();
//    }
    /**
     * Get all valid and undeleted entries from table C_WM_LIST_PROCESS_TOPIC
     *
     * @return entries list
     */
    public List<CWmListProcessTopic> getAllValidProcessTopics() {
        List<CWmListProcessTopic> list;
        Query query = getEntityManager().createQuery("from " + CWmListProcessTopic.class.getSimpleName() + " b where b.wmPtDeleted=0 and b.wmPtValid=1 order by b.wmPtSort, b.wmPtValidFrom, b.wmPtValidTo");
        list = query.getResultList();
        return list;
    }

    /**
     * get all available process topics for the date null values in
     * wmPtValidFrom and or wmPtValidTo are considered as infinite and are
     * therefore valid Warning: do not check if result is set as valid
     *
     * @param pDate date that specifies validity
     * @return list of all avaiable
     */
    public List<CWmListProcessTopic> getAllAvailableProcessTopics(Date pDate) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CWmListProcessTopic> query = criteriaBuilder.createQuery(CWmListProcessTopic.class);
        Root<CWmListProcessTopic> from = query.from(CWmListProcessTopic.class);

        Path<Date> lowerBorder = from.get(CWmListProcessTopic_.wmPtValidFrom);
        Predicate startCondition = criteriaBuilder.or(criteriaBuilder.isNull(lowerBorder), criteriaBuilder.lessThanOrEqualTo(lowerBorder, pDate));

        Path<Date> upperBorder = from.get(CWmListProcessTopic_.wmPtValidTo);
        Predicate endCondition = criteriaBuilder.or(criteriaBuilder.isNull(upperBorder), criteriaBuilder.greaterThanOrEqualTo(upperBorder, pDate));

        Predicate validCondition = criteriaBuilder.and(criteriaBuilder.equal(from.get(CWmListProcessTopic_.wmPtValid), true), criteriaBuilder.equal(from.get(CWmListProcessTopic_.wmPtDeleted), false));
        Predicate finalCondition = criteriaBuilder.and(startCondition, endCondition);

        query.where(finalCondition, validCondition);
        query.orderBy(criteriaBuilder.asc(from.get(CWmListProcessTopic_.wmPtSort)));

        //above code more or less results in this query in the database
//        String sql = "SELECT * FROM C_WM_LIST_PROCESS_TOPIC WHERE \n" +
//            "(WM_PT_VALID_FROM <= pDate OR (WM_PT_VALID_FROM IS NULL ) \n" +
//            "AND \n" +
//            "(WM_PT_VALID_TO >= pDate OR WM_PT_VALID_TO IS NULL)";
        TypedQuery<CWmListProcessTopic> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

//    public CWmListProcessTopic findByIdent(Long pTopic) {
//        List<CWmListProcessTopic> lResults = null;
//        String queryName = "select u from " + CWmListProcessTopic.class.getSimpleName() + " u where wmPtInternalId =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pTopic);
//        lResults = query.getResultList();
//        if (lResults.isEmpty()) {
//            return null;
//        }
//        return (lResults == null || lResults.isEmpty()) ? null : lResults.get(0);
//    }
    public Map<Long, CWmListProcessTopic> getProcessTopics() {
        return getMenuCacheItems();
    }

    public Map<Long, CWmListProcessTopic> getMenuCacheItems() {
        return MenuCacheEntity.toMap(findAll());
    }

}
