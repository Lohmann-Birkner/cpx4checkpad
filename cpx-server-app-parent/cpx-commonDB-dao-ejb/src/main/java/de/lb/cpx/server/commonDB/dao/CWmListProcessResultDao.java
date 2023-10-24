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

import de.lb.cpx.server.commonDB.model.CWmListProcessResult;
import de.lb.cpx.server.commonDB.model.CWmListProcessResult_;
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
public class CWmListProcessResultDao extends AbstractInternalIdDao<CWmListProcessResult> {

    /**
     * Creates a new instance.
     */
    public CWmListProcessResultDao() {
        super(CWmListProcessResult.class);
    }

    /**
     * Get all entries from table C_WM_LIST_PROCESS_RESULT
     *
     * @return entries list
     */
    public List<CWmListProcessResult> getWmListProcessResultCatalog() {
        List<CWmListProcessResult> list = null;

        Query query = getEntityManager().createQuery("from " + CWmListProcessResult.class.getSimpleName() + "  b where b.wmPrDeleted=0 order by b.wmPrSort, b.wmPrValidFrom, b.wmPrValidTo");
        list = query.getResultList();
        return list;
    }

//    /**
//     * Add new process result
//     *
//     * @param pCWmListProcessResult process result
//     * @return id for new process result
//     */
//    public long addNewProcessResult(CWmListProcessResult pCWmListProcessResult) {
//        return addNewItem(pCWmListProcessResult);
////        persist(pCWmListProcessResult);
////        flush();
////        long id = pCWmListProcessResult.getId();
////        return id;
//    }
//    /**
//     * update the process result
//     *
//     * @param pCWmListProcessResult process result to update
//     * @return state of update process
//     */
//    public boolean updateProcessResult(CWmListProcessResult pCWmListProcessResult) {
//        boolean checkUpdateState = false;
//        if (pCWmListProcessResult.getId() <= 0L) {
//            checkUpdateState = false;
//        } else {
//            merge(pCWmListProcessResult);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//
//    /**
//     * update the process result list
//     *
//     * @param pCWmListProcessResultList process result list to update
//     * @return state of update process
//     */
//    public boolean updateProcessResultList(List<CWmListProcessResult> pCWmListProcessResultList) {
//        boolean checkUpdateState = false;
//        if (!pCWmListProcessResultList.isEmpty()) {
//            mergeList(pCWmListProcessResultList);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//    /**
//     * Get process result item by id
//     *
//     * @param pCWmListProcessResultId process result id
//     * @return searched process result
//     */
//    public CWmListProcessResult getProcessResultById(Long pCWmListProcessResultId) {
//        List<CWmListProcessResult> lResults = null;
//        String queryName = "select u from " + CWmListProcessResult.class.getSimpleName() + " u where id =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pCWmListProcessResultId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
//    /**
//     * Get process result item by id
//     *
//     * @param pCWmListProcessResultInternalId process result id
//     * @return searched process result
//     */
//    public CWmListProcessResult getProcessResultByInternalId(Long pCWmListProcessResultInternalId) {
//        List<CWmListProcessResult> lResults = null;
//        String queryName = "select u from " + CWmListProcessResult.class.getSimpleName() + " u where wmPrInternalId =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pCWmListProcessResultInternalId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
    /**
     * Inactivate process result by id
     *
     * @param pCWmListProcessResult process result to inactivate
     * @return boolean value for inactivate state
     */
    public boolean inactivateProcessResult(CWmListProcessResult pCWmListProcessResult) {
        boolean status;
        if (pCWmListProcessResult.getId() <= 0L) {
            status = false;
        } else {
            pCWmListProcessResult.setWmPrDeleted(true);
            pCWmListProcessResult.setWmPrSort(-1);
            merge(pCWmListProcessResult);
            status = true;
        }
        return status;
    }

    @Override
    public String getInternalIdSequence() {
        return "C_WM_LIST_PR_INTERNAL_ID_SQ";
    }

    @Override
    public SingularAttribute<CWmListProcessResult, Long> getInternalIdField() {
        return CWmListProcessResult_.wmPrInternalId;
    }

//    /**
//     * Get sequence for internal id of process result
//     *
//     * @return sequence id
//     */
//    public Long getNextProcessResultInternalId() {
//        Long intrenalIdSequence = 0L;
//        try {
//            if (isOracle()) {
//                String queryName = "SELECT C_WM_LIST_PR_INTERNAL_ID_SQ.NEXTVAL FROM DUAL";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Long.valueOf(query.getResultList().get(0).toString());
//            } else if (isSqlSrv()) {
//                String queryName = "SELECT NEXT VALUE FOR C_WM_LIST_PR_INTERNAL_ID_SQ";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Long.valueOf(query.getResultList().get(0).toString());
////                if (intrenalIdSequence != 0L) {
////                    String isertQueryName= "INSERT INTO [dbo].[C_WM_LIST_PR_INTERNAL_ID_SQ] ([next_val]) VALUES (:nextValue)";
////                   javax.persistence.Query qry = getEntityManager().createNativeQuery(isertQueryName).setParameter("nextValue", intrenalIdSequence+1);
////                   boolean ret = (qry.executeUpdate() == 1);
////                }
//            }
//        } catch (NumberFormatException ex) {
//            LOG.log(Level.SEVERE, "Can't get sequence for internal id of process result", ex);
//        }
//        return intrenalIdSequence;
//    }
//    /**
//     * @return all process results stored in the database! Warning: select also
//     * inValid results!
//     */
//    public List<CWmListProcessResult> getAllProcessResults() {
//        final TypedQuery<CWmListProcessResult> query = getEntityManager().createQuery("from CWmListProcessResult", CWmListProcessResult.class);
//        return query.getResultList();
//    }
    //CPX-1028 RSH 08082018
    /**
     * @return all Valid process results stored in the database
     */
    public List<CWmListProcessResult> getAllValidProcessResults() {

        List<CWmListProcessResult> list = null;
        Query query = getEntityManager().createQuery("from " + CWmListProcessResult.class.getSimpleName() + " b where b.wmPrDeleted=0 and b.wmPrValid=1 order by b.wmPrSort, b.wmPrValidFrom, b.wmPrValidTo");
        list = query.getResultList();
        return list;
    }

    /**
     * get all available process results for the date null values in
     * wmPrValidFrom and or wmPrValidTo are considered as infinite and are
     * therefore valid Warning: do not check if result is set as valid
     *
     * @param pDate date that specifies validity
     * @return list of all avaiable
     */
    public List<CWmListProcessResult> getAllAvailableProcessResults(Date pDate) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CWmListProcessResult> query = criteriaBuilder.createQuery(CWmListProcessResult.class);
        Root<CWmListProcessResult> from = query.from(CWmListProcessResult.class);

        Path<Date> lowerBorder = from.get(CWmListProcessResult_.wmPrValidFrom);
        Predicate startCondition = criteriaBuilder.or(criteriaBuilder.isNull(lowerBorder), criteriaBuilder.lessThanOrEqualTo(lowerBorder, pDate));

        Path<Date> upperBorder = from.get(CWmListProcessResult_.wmPrValidTo);
        Predicate endCondition = criteriaBuilder.or(criteriaBuilder.isNull(upperBorder), criteriaBuilder.greaterThanOrEqualTo(upperBorder, pDate));

        Predicate dateCondition = criteriaBuilder.and(startCondition, endCondition);
        Predicate validCondition = criteriaBuilder.and(criteriaBuilder.equal(from.get(CWmListProcessResult_.wmPrValid), true), criteriaBuilder.equal(from.get(CWmListProcessResult_.wmPrDeleted), false));
        query.where(dateCondition, validCondition);

        query.orderBy(criteriaBuilder.asc(from.get(CWmListProcessResult_.wmPrSort)));
        //above code more or less results in this query in the database
//        String sql = "SELECT * FROM C_WM_LIST_PROCESS_RESULT WHERE \n" +
//            "(WM_PR_VALID_FROM <= pDate OR (WM_PR_VALID_FROM IS NULL ) \n" +
//            "AND \n" +
//            "(WM_PR_VALID_TO >= pDate OR WM_PR_VALID_TO IS NULL)";

        TypedQuery<CWmListProcessResult> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

//    /**
//     * Get process result item by ident
//     *
//     * @param pCWmListProcessResultIdent process result ident
//     * @return searched process result
//     */
//    public CWmListProcessResult getProcessResultByIdent(Long pCWmListProcessResultIdent) {
//        List<CWmListProcessResult> lResults = null;
//        String queryName = "select u from " + CWmListProcessResult.class.getSimpleName() + " u where wmPrInternalId =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pCWmListProcessResultIdent);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
    public Map<Long, CWmListProcessResult> getProcessResults() {
        return getMenuCacheItems();
    }

    public Map<Long, CWmListProcessResult> getMenuCacheItems() {
        return MenuCacheEntity.toMap(findAll());
    }

}
