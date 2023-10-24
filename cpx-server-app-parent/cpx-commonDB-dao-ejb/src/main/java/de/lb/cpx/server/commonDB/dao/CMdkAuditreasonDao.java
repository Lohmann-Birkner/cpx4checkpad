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

import de.lb.cpx.server.commonDB.model.CMdkAuditreason;
import de.lb.cpx.server.commonDB.model.CMdkAuditreason_;
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
 * Data access object for domain model class CDoctor. Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools, sklarow, nandola
 */
@Stateless
@SuppressWarnings("unchecked")
public class CMdkAuditreasonDao extends AbstractInternalIdDao<CMdkAuditreason> {

    /**
     * Creates a new instance.
     */
    public CMdkAuditreasonDao() {
        super(CMdkAuditreason.class);
    }

    /**
     * Get all entries from table of MDK audit reasons
     *
     * @return entries list
     */
    public List<CMdkAuditreason> getEntries() {
        Query query = getEntityManager().createQuery("from " + CMdkAuditreason.class.getSimpleName() + " b where b.mdkArDeleted=0 order by b.mdkArSort, b.mdkArValidFrom, b.mdkArValidTo");
        List<CMdkAuditreason> list = query.getResultList();
        return list;
    }

//    /**
//     * Add new mdk audit reason
//     *
//     * @param pCMdkAuditreason mdk audit reason
//     * @return id for new mdk audit reason
//     */
//    public long addNewMdkAuditReason(CMdkAuditreason pCMdkAuditreason) {
//        return addNewItem(pCMdkAuditreason);
////        persist(pCMdkAuditreason);
////        flush();
////        long auditReasonId = pCMdkAuditreason.getId();
////        return auditReasonId;
//    }
//    /**
//     * update the mdk audit reason
//     *
//     * @param pCMdkAuditreason mdk audit reason to update
//     * @return state of update process
//     */
//    public boolean updateMdkAuditReason(CMdkAuditreason pCMdkAuditreason) {
//        boolean checkUpdateState = false;
//        if (pCMdkAuditreason.getId() <= 0L) {
//            checkUpdateState = false;
//        } else {
//            merge(pCMdkAuditreason);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//
//    /**
//     * update the audit reason list
//     *
//     * @param pCMdkAuditreasonList audit reason list to update
//     * @return state of update process
//     */
//    public boolean updateMdkAuditReasonList(List<CMdkAuditreason> pCMdkAuditreasonList) {
//        boolean checkUpdateState = false;
//        if (!pCMdkAuditreasonList.isEmpty()) {
//            mergeList(pCMdkAuditreasonList);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//    /**
//     * Get mdk audit reason item by id
//     *
//     * @param pAuditreasonId mdk audit reason id
//     * @return searched mdk audit reason
//     */
//    public CMdkAuditreason getMdkAuditreasonById(Long pAuditreasonId) {
//        if (pAuditreasonId == null) {
//            LOG.log(Level.SEVERE, "pAuditreasonId is null!");
//            return null;
//        }
//        if (pAuditreasonId == 0) {
//            LOG.log(Level.WARNING, "Audit reason id is equal to 0");
//        }
//        List<CMdkAuditreason> lResults = null;
//        String queryName = "select u from " + CMdkAuditreason.class.getSimpleName() + " u where id =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pAuditreasonId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
//    /**
//     * Get mdk audit reason item by AR id
//     *
//     * @param pAuditreasonNumber mdk audit reason AR id
//     * @return searched mdk audit reason
//     */
//    public CMdkAuditreason getMdkAuditreasonByNumber(Integer pAuditreasonNumber) {
//        if (pAuditreasonNumber == null) {
//            LOG.log(Level.SEVERE, "Audit reason number is null!");
//            return null;
//        }
//        if (pAuditreasonNumber == 0) {
//            LOG.log(Level.WARNING, "Audit reason number is equal to 0");
//        }
//        List<CMdkAuditreason> lResults = null;
//        String queryName = "select u from " + CMdkAuditreason.class.getSimpleName() + " u where mdkArNumber =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pAuditreasonNumber);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
    /**
     *
     * @return AllValidCMdkAuditReasons
     */
    public List<CMdkAuditreason> getAllValidCMdkAuditReasons() {
        Query query = getEntityManager().createQuery("from " + CMdkAuditreason.class.getSimpleName() + " b where b.mdkArDeleted=0 AND b.mdkArValid=1 order by b.id");
        List<CMdkAuditreason> list = query.getResultList();
        return list;
    }

    /**
     * @return All CMdkAuditReasons
     */
    public List<CMdkAuditreason> getAllCMdkAuditReasons() {
        final TypedQuery<CMdkAuditreason> query = getEntityManager().createQuery("from CMdkAuditreason", CMdkAuditreason.class);
        return query.getResultList();
    }

    public List<String> getAllCMdkAuditReasonsNames() {
        final TypedQuery<String> query = getEntityManager().createQuery("select m.mdkArName from CMdkAuditreason m", String.class);
        return query.getResultList();
    }

    /**
     * get all available audit reason for the date, checks if date is
     * lessThanOrEquals mdkArValidFrom and date is greaterThanOrEquals
     * mdkArValidTo null value in (validFrom or validTo) are interpreted as
     * infinite, and therefore always valid Warning: do not check if auditreason
     * is set as valid
     *
     * @param pDate date to check validity
     * @return list of valid /available auditReasons
     */
    public List<CMdkAuditreason> getAllAvailableAuditReasons(Date pDate) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CMdkAuditreason> query = criteriaBuilder.createQuery(CMdkAuditreason.class);
        Root<CMdkAuditreason> from = query.from(CMdkAuditreason.class);

        Path<Date> lowerBorder = from.get(CMdkAuditreason_.mdkArValidFrom);
        Predicate startCondition = criteriaBuilder.or(criteriaBuilder.isNull(lowerBorder), criteriaBuilder.lessThanOrEqualTo(lowerBorder, pDate));

        Path<Date> upperBorder = from.get(CMdkAuditreason_.mdkArValidTo);
        Predicate endCondition = criteriaBuilder.or(criteriaBuilder.isNull(upperBorder), criteriaBuilder.greaterThanOrEqualTo(upperBorder, pDate));

        Predicate finalCondition = criteriaBuilder.and(startCondition, endCondition);
        Predicate validCondition = criteriaBuilder.and(criteriaBuilder.equal(from.get(CMdkAuditreason_.mdkArValid), true), criteriaBuilder.equal(from.get(CMdkAuditreason_.mdkArDeleted), false));
        query.where(finalCondition, validCondition);

        query.orderBy(criteriaBuilder.asc(from.get(CMdkAuditreason_.mdkArSort)));

        //above code more or less results in this query in the database
//        String sql = "SELECT * FROM C_MDK_AUDITREASON WHERE \n" +
//            "(MDK_AR_VALID_FROM <= pDate OR (MDK_AR_VALID_FROM IS NULL ) \n" +
//            "AND \n" +
//            "(MDK_AR_VALID_TO >= pDate OR MDK_AR_VALID_TO IS NULL)";
        TypedQuery<CMdkAuditreason> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

    /**
     * Inactivate audit reason by id
     *
     * @param pCMdkAuditreason audit reason to inactivate
     * @return boolean value for inactivate state
     */
    public boolean inactivateMdkAuditreason(CMdkAuditreason pCMdkAuditreason) {
        boolean status;
        if (pCMdkAuditreason.getId() <= 0L) {
            status = false;
        } else {
            pCMdkAuditreason.setMdkArDeleted(true);
            pCMdkAuditreason.setMdkArSort(-1);
            merge(pCMdkAuditreason);
            status = true;
        }
        return status;
    }

    @Override
    public String getInternalIdSequence() {
        return "C_MDK_AUDREASON_INTERNAL_ID_SQ";
    }

    @Override
    public SingularAttribute<CMdkAuditreason, Long> getInternalIdField() {
        return CMdkAuditreason_.mdkArNumber;
    }

//    /**
//     * Get sequence for internal id of audit reason
//     *
//     * @return sequence id
//     */
//    public Integer getNextMdkAuditreasonInternalId() {
//        Integer intrenalIdSequence = 0;
//        try {
//            if (isOracle()) {
//                String queryName = "SELECT C_MDK_AUDREASON_INTERNAL_ID_SQ.NEXTVAL FROM DUAL";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Integer.valueOf(query.getResultList().get(0).toString());
//            } else if (isSqlSrv()) {
//                String queryName = "SELECT NEXT VALUE FOR C_MDK_AUDREASON_INTERNAL_ID_SQ";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Integer.valueOf(query.getResultList().get(0).toString());
////                if (intrenalIdSequence != 0) {
////                    String isertQueryName= "INSERT INTO [dbo].[C_MDK_AUDREASON_INTERNAL_ID_SQ ] ([next_val]) VALUES (:nextValue)";
////                   javax.persistence.Query qry = getEntityManager().createNativeQuery(isertQueryName).setParameter("nextValue", intrenalIdSequence+1);
////                   boolean ret = (qry.executeUpdate() == 1);
////                }
//            }
//        } catch (NumberFormatException ex) {
//            LOG.log(Level.SEVERE, "Can't get sequence for internal id of audit reason", ex);
//        }
//        return intrenalIdSequence;
//    }
    public Map<Long, CMdkAuditreason> getMdkAuditReasons() {
        return getMenuCacheItems();
    }

    public Map<Long, CMdkAuditreason> getMenuCacheItems() {
        return MenuCacheEntity.toMap(findAll());
    }
    
}
