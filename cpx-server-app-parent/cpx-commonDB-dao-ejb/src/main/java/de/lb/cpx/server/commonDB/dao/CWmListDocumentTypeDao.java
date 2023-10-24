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

import de.lb.cpx.server.commonDB.model.CWmListDocumentType;
import de.lb.cpx.server.commonDB.model.CWmListDocumentType_;
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
public class CWmListDocumentTypeDao extends AbstractInternalIdDao<CWmListDocumentType> {

    /**
     * Creates a new instance.
     */
    public CWmListDocumentTypeDao() {
        super(CWmListDocumentType.class);
    }

    /**
     * Get all valid and undeleted entries from table C_WM_LIST_DOCUMENT_TYPE
     *
     * @return entries list
     */
    public List<CWmListDocumentType> getWmListDocumentTypeCatalog() {
        List<CWmListDocumentType> list = null;
        Query query = getEntityManager().createQuery("from " + CWmListDocumentType.class.getSimpleName() + " b where b.wmDtDeleted=0 order by b.wmDtSort, b.wmDtValidFrom, b.wmDtValidTo");
        list = query.getResultList();
        return list;
    }

    /**
     * Get all valid and undeleted entries from table C_WM_LIST_DOCUMENT_TYPE
     *
     * @return entries list
     */
    public List<CWmListDocumentType> getAllValidDocumentTypes() {
        List<CWmListDocumentType> list;
        Query query = getEntityManager().createQuery("from " + CWmListDocumentType.class.getSimpleName() + " b where b.wmDtDeleted=0 and b.wmDtValid=1 order by b.wmDtSort, b.wmDtValidFrom, b.wmDtValidTo");
        list = query.getResultList();
        return list;
    }

    /**
     * Get all valid and undeleted entries from table C_WM_LIST_DOCUMENT_TYPE
     *
     * @param pDate date that specifies validity
     * @return entries list
     */
    public List<CWmListDocumentType> getAllAvailableDocumentTypes(Date pDate) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CWmListDocumentType> query = criteriaBuilder.createQuery(CWmListDocumentType.class);
        Root<CWmListDocumentType> from = query.from(CWmListDocumentType.class);
        Predicate deleteCondition = criteriaBuilder.equal(from.get(CWmListDocumentType_.wmDtDeleted), 0);
        Predicate validCondition = criteriaBuilder.equal(from.get(CWmListDocumentType_.wmDtValid), 1);
        Predicate startCondition = criteriaBuilder.or(criteriaBuilder.isNull(from.get(CWmListDocumentType_.wmDtValidFrom)), criteriaBuilder.lessThanOrEqualTo(from.get(CWmListDocumentType_.wmDtValidFrom), pDate));
        Predicate endCondition = criteriaBuilder.or(criteriaBuilder.isNull(from.get(CWmListDocumentType_.wmDtValidTo)), criteriaBuilder.greaterThanOrEqualTo(from.get(CWmListDocumentType_.wmDtValidTo), pDate));
        Predicate finalCondition = criteriaBuilder.and(startCondition, endCondition);
        query.where(deleteCondition, validCondition, finalCondition);
        query.orderBy(criteriaBuilder.asc(from.get(CWmListDocumentType_.wmDtSort)));
        TypedQuery<CWmListDocumentType> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

//    /**
//     * Add new document type
//     *
//     * @param pCWmListDocumentType document type
//     * @return id for new document type
//     */
//    public long addNewDocumentType(CWmListDocumentType pCWmListDocumentType) {
//        return addNewItem(pCWmListDocumentType);
////        persist(pCWmListDocumentType);
////        flush();
////        long id = pCWmListDocumentType.getId();
////        return id;
//    }
//    /**
//     * update the document type
//     *
//     * @param pCWmListDocumentType document type to update
//     * @return state of update process
//     */
//    public boolean updateDocumentType(CWmListDocumentType pCWmListDocumentType) {
//        boolean checkUpdateState;
//        if (pCWmListDocumentType.getId() <= 0L) {
//            checkUpdateState = false;
//        } else {
//            merge(pCWmListDocumentType);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//
//    /**
//     * update the document type list
//     *
//     * @param pCWmListDocumentTypeList document type list to update
//     * @return state of update process
//     */
//    public boolean updateDocumentTypeList(List<CWmListDocumentType> pCWmListDocumentTypeList) {
//        boolean checkUpdateState = false;
//        if (!pCWmListDocumentTypeList.isEmpty()) {
//            mergeList(pCWmListDocumentTypeList);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//    /**
//     * Get document type item by id
//     *
//     * @param pCWmListDocumentTypeId document type id
//     * @return searched document type
//     */
//    public CWmListDocumentType getDocumentTypeById(Long pCWmListDocumentTypeId) {
//        List<CWmListDocumentType> lResults;
//        String queryName = "select u from " + CWmListDocumentType.class.getSimpleName() + " u where id =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pCWmListDocumentTypeId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
    /**
     * Inactivate document type by id
     *
     * @param pCWmListDocumentType document type to inactivate
     * @return boolean value for inactivate state
     */
    public boolean inactivateDocumentType(CWmListDocumentType pCWmListDocumentType) {
        boolean status;
        if (pCWmListDocumentType.getId() <= 0L) {
            status = false;
        } else {
            pCWmListDocumentType.setWmDtDeleted(true);
            pCWmListDocumentType.setWmDtSort(-1);
            merge(pCWmListDocumentType);
            status = true;
        }
        return status;
    }

    @Override
    public String getInternalIdSequence() {
        return "C_WM_LIST_DT_INTERNAL_ID_SQ";
    }

    @Override
    public SingularAttribute<CWmListDocumentType, Long> getInternalIdField() {
        return CWmListDocumentType_.wmDtInternalId;
    }

//    /**
//     * Get sequence for internal id of document type
//     *
//     * @return sequence id
//     */
//    public Long getNextDocumentTypeInternalId() {
//        Long intrenalIdSequence = 0L;
//        try {
//            if (isOracle()) {
//                String queryName = "SELECT C_WM_LIST_DT_INTERNAL_ID_SQ.NEXTVAL FROM DUAL";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Long.valueOf(query.getResultList().get(0).toString());
//            } else if (isSqlSrv()) {
//                String queryName = "SELECT NEXT VALUE FOR C_WM_LIST_DT_INTERNAL_ID_SQ";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Long.valueOf(query.getResultList().get(0).toString());
////                if (intrenalIdSequence != 0L) {
////                    String isertQueryName= "INSERT INTO [dbo].[C_WM_LIST_DT_INTERNAL_ID_SQ] ([next_val]) VALUES (:nextValue)";
////                   javax.persistence.Query qry = getEntityManager().createNativeQuery(isertQueryName).setParameter("nextValue", intrenalIdSequence+1);
////                   boolean ret = (qry.executeUpdate() == 1);
////                }
//            }
//        } catch (NumberFormatException ex) {
//            LOG.log(Level.SEVERE, "Can't get sequence for internal id of document type", ex);
//        }
//        return intrenalIdSequence;
//    }
    public Map<Long, CWmListDocumentType> getDocumentTypes() {
        return getMenuCacheItems();
    }

    public Map<Long, CWmListDocumentType> getMenuCacheItems() {
        return MenuCacheEntity.toMap(findAll());
    }

}
