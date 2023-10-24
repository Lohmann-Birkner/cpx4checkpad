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
 *    2019  shahin - initial API and implementation and/or initial draftation
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.model.enums.CategoryEn;
import de.lb.cpx.server.commonDB.model.CWmListDraftType;
import de.lb.cpx.server.commonDB.model.CWmListDraftType_;
import de.lb.cpx.server.commons.dao.MenuCacheEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
public class CWmListDraftTypeDao extends AbstractInternalIdDao<CWmListDraftType> {

    /**
     * Creates a new instance.
     */
    public CWmListDraftTypeDao() {
        super(CWmListDraftType.class);
    }

    /**
     * Get all valid and undeleted entries from table C_WM_LIST_DRAFT_TYPE
     *
     * @return entries list
     */
    public List<CWmListDraftType> getWmListDraftType() {
        List<CWmListDraftType> list = null;
        Query query = getEntityManager().createQuery("from " + CWmListDraftType.class.getSimpleName() + " b where b.wmDrtDeleted=0 order by b.wmDrtSort, b.wmDrtValidFrom, b.wmDrtValidTo");
        list = query.getResultList();
        return list;
    }

    /**
     * Get all valid and undeleted entries from table C_WM_LIST_DRAFT_TYPE
     *
     * @return entries list
     */
    public List<CWmListDraftType> getAllValidDraftTypes() {
        List<CWmListDraftType> list;
        Query query = getEntityManager().createQuery("from " + CWmListDraftType.class.getSimpleName() + " b where b.wmDrtDeleted=0 and b.wmDrtValid=1  order by b.wmDrtSort, b.wmDrtValidFrom, b.wmDrtValidTo");
        list = query.getResultList();
        return list;
    }

    /**
     *
     * @param cat DraftType
     * @return entries list of DraftType
     */
    public List<CWmListDraftType> getAllValidDraftTypes(CategoryEn cat) {

        List<CWmListDraftType> list;
        String queryName = "select u from " + CWmListDraftType.class.getSimpleName() + " u where wmDrtCategory =:category and  wmDrtDeleted=0 and  wmDrtValid=1  order by  wmDrtSort,  wmDrtValidFrom,  wmDrtValidTo";
        Query query = getEntityManager().createQuery(queryName);

        query.setParameter("category", cat);
        list = query.getResultList();
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * Get all valid and undeleted entries from table C_WM_LIST_DRAFT_TYPE
     *
     * @param pDate date that specifies validity
     * @return entries list
     */
    public List<CWmListDraftType> getAllAvailableDraftTypes(Date pDate) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CWmListDraftType> query = criteriaBuilder.createQuery(CWmListDraftType.class);
        Root<CWmListDraftType> from = query.from(CWmListDraftType.class);
        Predicate deleteCondition = criteriaBuilder.equal(from.get(CWmListDraftType_.wmDrtDeleted), 0);
        Predicate validCondition = criteriaBuilder.equal(from.get(CWmListDraftType_.wmDrtValid), 1);
        Predicate startCondition = criteriaBuilder.or(criteriaBuilder.isNull(from.get(CWmListDraftType_.wmDrtValidFrom)), criteriaBuilder.lessThanOrEqualTo(from.get(CWmListDraftType_.wmDrtValidFrom), pDate));
        Predicate endCondition = criteriaBuilder.or(criteriaBuilder.isNull(from.get(CWmListDraftType_.wmDrtValidTo)), criteriaBuilder.greaterThanOrEqualTo(from.get(CWmListDraftType_.wmDrtValidTo), pDate));
        Predicate finalCondition = criteriaBuilder.and(startCondition, endCondition);
        query.where(deleteCondition, validCondition, finalCondition);
        query.orderBy(criteriaBuilder.asc(from.get(CWmListDraftType_.wmDrtSort)));
        TypedQuery<CWmListDraftType> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

//    /**
//     * Add new draft type
//     *
//     * @param pCWmListDraftType draft type
//     * @return id for new draft type
//     */
//    public long addNewDraftType(CWmListDraftType pCWmListDraftType) {
//        return addNewItem(pCWmListDraftType);
////        persist(pCWmListDraftType);
////        flush();
////        long id = pCWmListDraftType.getId();
////        return id;
//    }
//    /**
//     * update the draft type
//     *
//     * @param pCWmListDraftType draft type to update
//     * @return state of update process
//     */
//    public boolean updateDraftType(CWmListDraftType pCWmListDraftType) {
//        boolean checkUpdateState;
//        if (pCWmListDraftType.getId() <= 0L) {
//            checkUpdateState = false;
//        } else {
//            merge(pCWmListDraftType);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//
//    /**
//     * update the draft type list
//     *
//     * @param pCWmListDraftTypeList draft type list to update
//     * @return state of update process
//     */
//    public boolean updateDraftTypeList(List<CWmListDraftType> pCWmListDraftTypeList) {
//        boolean checkUpdateState = false;
//        if (!pCWmListDraftTypeList.isEmpty()) {
//            mergeList(pCWmListDraftTypeList);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//    /**
//     * Get draft type item by id
//     *
//     * @param pCWmListDraftTypeId draft type id
//     * @return searched draft type
//     */
//    public CWmListDraftType getDraftTypeById(Long pCWmListDraftTypeId) {
//        List<CWmListDraftType> lResults;
//        String queryName = "select u from " + CWmListDraftType.class.getSimpleName() + " u where id =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pCWmListDraftTypeId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
    /**
     * Inactivate draft type by id
     *
     * @param pCWmListDraftType draft type to inactivate
     * @return boolean value for inactivate state
     */
    public boolean inactivateDraftType(CWmListDraftType pCWmListDraftType) {
        boolean status;
        if (pCWmListDraftType.getId() <= 0L) {
            status = false;
        } else {
            pCWmListDraftType.setWmDrtDeleted(true);
            pCWmListDraftType.setWmDrtSort(-1);
            merge(pCWmListDraftType);
            status = true;
        }
        return status;
    }

    @Override
    public String getInternalIdSequence() {
        return "C_WM_LIST_DRT_INTERNAL_ID_SQ";
    }

    @Override
    public SingularAttribute<CWmListDraftType, Long> getInternalIdField() {
        return CWmListDraftType_.wmDrtInternalId;
    }

//    /**
//     * Get sequence for internal id of draft type
//     *
//     * @return sequence id
//     */
//    public Long getNextDraftTypeInternalId() {
//        Long intrenalIdSequence = 0L;
//        try {
//            if (isOracle()) {
//                String queryName = "SELECT C_WM_LIST_DRT_INTERNAL_ID_SQ.NEXTVAL FROM DUAL";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Long.valueOf(query.getResultList().get(0).toString());
//            } else if (isSqlSrv()) {
//                String queryName = "SELECT NEXT VALUE FOR C_WM_LIST_DRT_INTERNAL_ID_SQ";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Long.valueOf(query.getResultList().get(0).toString());
//            }
//        } catch (NumberFormatException ex) {
//            LOG.log(Level.SEVERE, "Can't get sequence for internal id of draft type", ex);
//        }
//        return intrenalIdSequence;
//    }
    /**
     * Get all valid and not deleted items from table C_WM_LIST_DRAFT_TYPE for
     * any category
     *
     * @param pCategoryEn country code
     * @return entries list
     */
    public Map<Long, String> getDraftTypesCategoryMap(CategoryEn pCategoryEn) {
        Map<Long, String> map = new HashMap<>();
        List<CWmListDraftType> list = null;
        String queryName = "select b from " + CWmListDraftType.class.getSimpleName() + " b "
                + "where b.wmDrtDeleted=0 "
                + "and b.wmDrtCategory= '" + pCategoryEn.getId() + "'\n"
                + "order by b.wmDrtName";
        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("category", pCategoryEn.getId());
        list = query.getResultList();
        for (CWmListDraftType item : list) {
            map.put(item.getWmDrtInternalId(), item.getWmDrtName());
        }
        return map;
    }

    public Map<Long, CWmListDraftType> getDraftTypes() {
        return getMenuCacheItems();
    }

    public Map<Long, CWmListDraftType> getMenuCacheItems() {
        return MenuCacheEntity.toMap(findAll());
    }

}
