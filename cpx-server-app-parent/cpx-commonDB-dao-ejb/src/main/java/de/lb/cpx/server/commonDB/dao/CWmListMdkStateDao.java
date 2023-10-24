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
 *    2018  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.server.commonDB.model.CWmListMdkState;
import de.lb.cpx.server.commonDB.model.CWmListMdkState_;
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
 * @author shahin
 */
@Stateless
@SuppressWarnings("unchecked")
public class CWmListMdkStateDao extends AbstractInternalIdDao<CWmListMdkState> {

    /**
     * Creates a new instance.
     */
    public CWmListMdkStateDao() {
        super(CWmListMdkState.class);
    }

    /**
     * Get all undeleted entries from table C_WM_LIST_MDK_STATE
     *
     * @return entries list
     */
    public List<CWmListMdkState> getWmListMdkStateCatalog() {
        List<CWmListMdkState> list;
        Query query = getEntityManager().createQuery("from " + CWmListMdkState.class.getSimpleName() + " b where b.wmMsDeleted=0 order by b.wmMsSort, b.wmMsValidFrom, b.wmMsValidTo");
        list = query.getResultList();
        return list;
    }

    /**
     * Get all valid and undeleted entries from table C_WM_LIST_MDK_STATE
     *
     * @return entries list
     */
    public List<CWmListMdkState> getAllValidMdkStates() {
        List<CWmListMdkState> list = null;
        Query query = getEntityManager().createQuery("from " + CWmListMdkState.class.getSimpleName() + " b where b.wmMsDeleted=0 and b.wmMsValid=1 order by b.wmMsSort, b.wmMsValidFrom, b.wmMsValidTo");
        list = query.getResultList();
        return list;
    }

    /**
     * Get all valid and undeleted entries from table C_WM_LIST_MDK_STATE
     *
     * @param pDate date that specifies validity
     * @return entries list
     */
    public List<CWmListMdkState> getAllAvailableMdkStates(Date pDate) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CWmListMdkState> query = criteriaBuilder.createQuery(CWmListMdkState.class);
        Root<CWmListMdkState> from = query.from(CWmListMdkState.class);
        Predicate deleteCondition = criteriaBuilder.equal(from.get(CWmListMdkState_.wmMsDeleted), 0);
        Predicate validCondition = criteriaBuilder.equal(from.get(CWmListMdkState_.wmMsValid), 1);
        Predicate startCondition = criteriaBuilder.or(criteriaBuilder.isNull(from.get(CWmListMdkState_.wmMsValidFrom)), criteriaBuilder.lessThanOrEqualTo(from.get(CWmListMdkState_.wmMsValidFrom), pDate));
        Predicate endCondition = criteriaBuilder.or(criteriaBuilder.isNull(from.get(CWmListMdkState_.wmMsValidTo)), criteriaBuilder.greaterThanOrEqualTo(from.get(CWmListMdkState_.wmMsValidTo), pDate));
        Predicate finalCondition = criteriaBuilder.and(startCondition, endCondition);
        query.where(deleteCondition, validCondition, finalCondition);
        query.orderBy(criteriaBuilder.asc(from.get(CWmListMdkState_.wmMsSort)));
        TypedQuery<CWmListMdkState> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

//    /**
//     * Add new mdk state
//     *
//     * @param pCWmListMdkState mdk state
//     * @return id for new mdk state
//     */
//    public long addNewMdkState(CWmListMdkState pCWmListMdkState) {
//        return addNewItem(pCWmListMdkState);
////        persist(pCWmListMdkState);
////        flush();
////        long id = pCWmListMdkState.getId();
////        return id;
//    }
//    /**
//     * update the mdk state
//     *
//     * @param pCWmListMdkState mdk state to update
//     * @return state of update process
//     */
//    public boolean updateMdkState(CWmListMdkState pCWmListMdkState) {
//        boolean checkUpdateState = false;
//        if (pCWmListMdkState.getId() <= 0L) {
//            checkUpdateState = false;
//        } else {
//            merge(pCWmListMdkState);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//
//    /**
//     * update the mdk state list
//     *
//     * @param pCWmListMdkStateList mdk state list to update
//     * @return state of update process
//     */
//    public boolean updateMdkStateList(List<CWmListMdkState> pCWmListMdkStateList) {
//        boolean checkUpdateState = false;
//        if (!pCWmListMdkStateList.isEmpty()) {
//            mergeList(pCWmListMdkStateList);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//    /**
//     * Get mdk state item by id
//     *
//     * @param pCWmListMdkStateId mdk state id
//     * @return searched mdk state
//     */
//    public CWmListMdkState getMdkStateById(Long pCWmListMdkStateId) {
//        List<CWmListMdkState> lResults = null;
//        String queryName = "select u from " + CWmListMdkState.class.getSimpleName() + " u where id =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pCWmListMdkStateId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
    /**
     * Inactivate mdk state by id
     *
     * @param pCWmListMdkState mdk state to inactivate
     * @return boolean value for inactivate state
     */
    public boolean inactivateMdkState(CWmListMdkState pCWmListMdkState) {
        boolean status;
        if (pCWmListMdkState.getId() <= 0L) {
            status = false;
        } else {
            pCWmListMdkState.setWmMsDeleted(true);
            pCWmListMdkState.setWmMsSort(-1);
            merge(pCWmListMdkState);
            status = true;
        }
        return status;
    }

    @Override
    public String getInternalIdSequence() {
        return "C_WM_LIST_MS_INTERNAL_ID_SQ";
    }

    @Override
    public SingularAttribute<CWmListMdkState, Long> getInternalIdField() {
        return CWmListMdkState_.wmMsInternalId;
    }

//    /**
//     * Get sequence for internal id of mdk state
//     *
//     * @return sequence id
//     */
//    public Long getNextMdkStateInternalId() {
//        Long intrenalIdSequence = 0L;
//        try {
//            if (isOracle()) {
//                String queryName = "SELECT C_WM_LIST_MS_INTERNAL_ID_SQ.NEXTVAL FROM DUAL";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Long.valueOf(query.getResultList().get(0).toString());
//            } else if (isSqlSrv()) {
//                String queryName = "SELECT NEXT VALUE FOR C_WM_LIST_MS_INTERNAL_ID_SQ";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Long.valueOf(query.getResultList().get(0).toString());
//
//            }
//        } catch (NumberFormatException ex) {
//            LOG.log(Level.SEVERE, "Can't get sequence for internal id of mdk status", ex);
//        }
//        return intrenalIdSequence;
//    }
    public Map<Long, CWmListMdkState> getMdkStates() {
        return getMenuCacheItems();
    }

    public Map<Long, CWmListMdkState> getMenuCacheItems() {
        return MenuCacheEntity.toMap(findAll());
    }

}
