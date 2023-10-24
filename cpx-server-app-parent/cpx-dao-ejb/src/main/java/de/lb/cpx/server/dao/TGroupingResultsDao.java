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

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCase2RuleSelection;
import de.lb.cpx.model.TCase2RuleSelection_;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDepartment_;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseDetails_;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseIcdGrouped;
import de.lb.cpx.model.TCaseIcdGrouped_;
import de.lb.cpx.model.TCaseIcd_;
import de.lb.cpx.model.TCaseOpsGrouped;
import de.lb.cpx.model.TCaseOpsGrouped_;
import de.lb.cpx.model.TCaseSupplFee;
import de.lb.cpx.model.TCaseSupplFee_;
import de.lb.cpx.model.TCase_;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TGroupingResults_;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.commons.enums.EntityGraphType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityGraph;
import javax.persistence.LockModeType;
import javax.persistence.LockTimeoutException;
import javax.persistence.OptimisticLockException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import org.hibernate.Hibernate;

/**
 *
 * @author wilde
 */
@Stateless
@SuppressWarnings("unchecked")
public class TGroupingResultsDao extends AbstractCpxDao<TGroupingResults> {

    private static final Logger LOG = Logger.getLogger(TGroupingResultsDao.class.getName());

    @EJB
    private TCaseDetailsDao detailsDao;

    /**
     * Creates a new instance.
     */
    public TGroupingResultsDao() {
        super(TGroupingResults.class);
    }

    /**
     * find TCaseResults for ModelIdent and caseDetailId load Values in
     * EntityGraph in loadGraph
     *
     * @see de.lb.cpx.server.commons.enums.EntityGraphType
     * @param caseDetailsId Db-Id of TCaseDetails related to GroupingResults
     * @param modelIdent ModelIdent of used Grouper
     * @return List of GroupingResults
     */
    public List<TGroupingResults> findTGroupingResults(Long caseDetailsId, GDRGModel modelIdent) {

        List<TGroupingResults> queryResult = buildGroupingResultQuery(caseDetailsId, modelIdent, null, null).getResultList();

        if (queryResult.isEmpty()) {
            return null;
        }
        LOG.log(Level.FINE, "queryresult: " + queryResult.size() + " entries for caseDetailsId " + caseDetailsId);
        return queryResult;
    }

    public TGroupingResults findTGroupingResult(Long caseDetailsId, GDRGModel modelIdent, Long icdIdMainDiagnosis) {

        return getSingleResultOrNull(buildGroupingResultQuery(caseDetailsId, modelIdent, icdIdMainDiagnosis, null));
    }

    public TGroupingResults findTGroupingResultDesc(long caseDetailsId, GDRGModel modelIdent, long icdIdMainDiagnosis) {

        List<TGroupingResults> queryResult = buildGroupingResultQuery(caseDetailsId, modelIdent, icdIdMainDiagnosis, Boolean.FALSE).getResultList();
        if (queryResult.isEmpty()) {
            return null;
        }
        LOG.log(Level.FINE, "queryresult: " + queryResult.size() + " entries for caseDetailsId " + caseDetailsId);
        return queryResult.get(0);
    }

    /**
     * Calculates ZE-Value, reads Values from db and Calculates Result after
     * that
     *
     * @param id database Id of GroupingResult
     * @return Double default Value is 0.0
     */
    public Double getSupplementaryValueForId(long id) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TGroupingResults> query = criteriaBuilder.createQuery(TGroupingResults.class);
        Root<TGroupingResults> from = query.from(TGroupingResults.class);
        query.distinct(true);

        query.where(criteriaBuilder.equal(from.get(TGroupingResults_.id), id));

        TypedQuery<TGroupingResults> criteriaQuery = getEntityManager().createQuery(query);

        //TGroupingResults queryResult = criteriaQuery.getSingleResult();
        TGroupingResults queryResult = getSingleResultOrNull(criteriaQuery);
        Double supValue = 0.0;

        if (queryResult == null) {
            LOG.log(Level.WARNING, "No grouping results found for id " + id);
        } else {
            for (TCaseOpsGrouped ops : queryResult.getCaseOpsGroupeds()) {
                if (ops.getCaseSupplFees() != null) {
                    double val = ops.getCaseSupplFees().getCsuplValue() * ops.getCaseSupplFees().getCsuplCount();
                    supValue += supValue + val;
                }
            }
        }
        return supValue;
    }

//    /**
//     * Calculates ZE-Value in a Query Ze-Value is calcualted Row based by COUNT
//     * multiplyed with VALUE, aaggregated with each other row
//     *
//     * @param casedetailsId Database-ID for the case details according to
//     * Grouped Ops Results with the desired Values
//     * @deprecated don't use this
//     * @return calculated Number, default Value if calculation failed is null
//     */
//    @Deprecated(since = "1.05")
//    public Number getSupplementaryValueForIdCalculateOnDatabase(long casedetailsId) {
//        //init query
//        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<Number> query = criteriaBuilder.createQuery(Number.class);
//
//        Root<TGroupingResults> from = query.from(TGroupingResults.class);
//        //select distinct to clear resultlist - maybe obsolete 
//        query.distinct(true);
//
//        //joins
//        Join<TGroupingResults, TCaseIcd> icd = from.join(TGroupingResults_.caseIcd);
//        SetJoin<TGroupingResults, TCaseOpsGrouped> opsGrouped = from.join(TGroupingResults_.caseOpsGroupeds);
//
//        //select Values based on GroupingResult with specific id
//        query.where(criteriaBuilder.equal(from.get(TGroupingResults_.caseDetails), casedetailsId), criteriaBuilder.equal(icd.get(TCaseIcd_.icdcIsHdxFl), Boolean.TRUE));
//
//        //join and get access to T_CASE_SUPPL_FEE - Table
//        Path<TCaseSupplFee> supFee = opsGrouped.get(TCaseOpsGrouped_.caseSupplFees);
//
//        //start calculation, first get values row based, typecast needed because of impl. of prod()- need same Object-Typ for Multiplicaction
////        Expression<Double> prod = criteriaBuilder.prod(supFee.get(TCaseSupplFee_.csuplValue), supFee.get(TCaseSupplFee_.csuplCount).as(Double.class));
//        //sum up rowValues to 1 Result
//        Expression<Double> sum = criteriaBuilder.sum(supFee.get(TCaseSupplFee_.csuplValue));
//
//        //create Query and select sum-Value
//        query.select(sum);
//        TypedQuery<Number> criteriaQuery = getEntityManager().createQuery(query);
//        //getValue, null if nothing is found
//        Number result = criteriaQuery.getSingleResult();
//        LOG.log(Level.FINE, "result for calc on db " + result + " for detailsId " + casedetailsId);
//        return result;
//    }
    /**
     * Calculates ZE-Value in a Query Ze-Value is calcualted Row based by COUNT
     * multiplyed with VALUE, aaggregated with each other row
     *
     * @param pGrouper grouper
     * @param casedetailsId Database-ID for the case details according to
     * Grouped Ops Results with the desired Values
     * @param pType supplementary type
     * @return calculated Number, default Value if calculation failed is null
     */
    public Number getSupplementaryValueForIdCalculateOnDatabase(GDRGModel pGrouper,long casedetailsId, SupplFeeTypeEn pType) {
        //init query
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Number> query = criteriaBuilder.createQuery(Number.class);

        Root<TGroupingResults> from = query.from(TGroupingResults.class);
        //select distinct to clear resultlist - maybe obsolete 
        query.distinct(true);

        //joins
        Join<TGroupingResults, TCaseIcd> icd = from.join(TGroupingResults_.caseIcd);
        SetJoin<TGroupingResults, TCaseOpsGrouped> opsGrouped = from.join(TGroupingResults_.caseOpsGroupeds);
        //join and get access to T_CASE_SUPPL_FEE - Table
        Path<TCaseSupplFee> supFee = opsGrouped.get(TCaseOpsGrouped_.caseSupplFees);
        //select Values based on GroupingResult with specific id
        query.where(criteriaBuilder.equal(from.get(TGroupingResults_.caseDetails), casedetailsId),
                criteriaBuilder.equal(icd.get(TCaseIcd_.icdcIsHdxFl), Boolean.TRUE),
                criteriaBuilder.equal(supFee.get(TCaseSupplFee_.csuplTypeEn), pType),
                getGrouperPredicate(criteriaBuilder,from,pGrouper));

        //start calculation, first get values row based, typecast needed because of impl. of prod()- need same Object-Typ for Multiplicaction
        //CPX-1426 RSH 12.02.2019 
//        Expression<Double> prod = criteriaBuilder.prod(supFee.get(TCaseSupplFee_.csuplValue), supFee.get(TCaseSupplFee_.csuplCount).as(Double.class));
        //sum up rowValues to 1 Result
        Expression<Double> sum = criteriaBuilder.sum(supFee.get(TCaseSupplFee_.csuplValue));
        //create Query and select sum-Value
        query.select(sum);
        TypedQuery<Number> criteriaQuery = getEntityManager().createQuery(query);
        //getValue, null if nothing is found
        Number result = criteriaQuery.getSingleResult();
        LOG.log(Level.FINE, "result for calc on db " + result + " for detailsId " + casedetailsId);
        return result;
    }
    public Predicate getGrouperPredicate(CriteriaBuilder pBuilder,Root<TGroupingResults> from,GDRGModel pModel){
        if(GDRGModel.AUTOMATIC.equals(pModel)){
            return pBuilder.equal(from.get(TGroupingResults_.grpresIsAutoFl), Boolean.TRUE);
        }
        return pBuilder.equal(from.get(TGroupingResults_.MODEL_ID_EN), pModel);
    }
    /**
     * deletes old results of grouping of one case when it was once more grouped
     *
     * @param results the list of the old results which are to be deleted from
     * database
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteOldResults(List<TGroupingResults> results) {
        if (results == null || results.isEmpty()) {
            return;
        }
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.lock.timeout", 5000L);
        for (TGroupingResults groupingResults : results) {
            deleteGroupingResult(groupingResults, 1000, properties);
        }
    }

    private void deleteGroupingResult(TGroupingResults groupingResult, int repeat, Map<String, Object> properties) {
        if (groupingResult != null) {
            try {
                TGroupingResults toDelete = getEntityManager().find(TGroupingResults.class, groupingResult.getId(), LockModeType.PESSIMISTIC_WRITE, properties);
                if (toDelete != null) {
                    try {
                        refresh(toDelete);
                        getEntityManager().remove(toDelete);
                    } catch (OptimisticLockException e) {
                        if (repeat >= 0) {
                            LOG.log(Level.WARNING, "TGroupingResult with id = " + groupingResult.getId() + " could not be deleted.", e);
                            try {
                                Thread.sleep(1000L);
                            } catch (InterruptedException ex) {
                                LOG.log(Level.SEVERE, null, ex);
                                Thread.currentThread().interrupt();
                            }
                            deleteGroupingResult(groupingResult, repeat--, properties);
                        }
                    }
                }
            } catch (LockTimeoutException e) {
                LOG.log(Level.WARNING, "TGroupingResult with id = " + groupingResult.getId() + " could not be deleted.", e);
                if (repeat >= 0) {
                    long timeoutValue = (Long) properties.get("javax.persistence.lock.timeout");
                    properties.put("javax.persistence.lock.timeout", timeoutValue * 2);
                    deleteGroupingResult(groupingResult, repeat--, properties);
                }
            }
        }
    }

    private TypedQuery<TGroupingResults> buildGroupingResultQuery(Long caseDetailsId, GDRGModel modelIdent, Long icdIdMainDiagnosis, Boolean orderAsc) {
        LOG.log(Level.FINE, "try to find GroupingResults for detailsId " + caseDetailsId + " modelIdent " + modelIdent + " icdMainDiag " + icdIdMainDiagnosis + " orderAsc : " + orderAsc);
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TGroupingResults> query = criteriaBuilder.createQuery(TGroupingResults.class);
        query.distinct(true);
        Root<TGroupingResults> from = query.from(TGroupingResults.class);

        //build PredicateList for all Cases
        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(criteriaBuilder.equal(from.get(TGroupingResults_.caseDetails), caseDetailsId));
        //add if MainDiagnosis is required in the where Clause
        if (icdIdMainDiagnosis != null) {
            predicateList.add(criteriaBuilder.equal(from.get(TGroupingResults_.caseIcd), icdIdMainDiagnosis));
        }
        //check GrouperModel if its not automatic, get all Grouping Results for CaseDetails and Model and where isAutoFlag is false
        //if true, get all for Casedetails where isAutoFlag is true
        if (!modelIdent.equals(GDRGModel.AUTOMATIC)) {
            LOG.log(Level.FINE, "model is not automatic! " + modelIdent);
            predicateList.add(criteriaBuilder.equal(from.get(TGroupingResults_.modelIdEn), modelIdent));
            predicateList.add(criteriaBuilder.equal(from.get(TGroupingResults_.grpresIsAutoFl), false));
        } else {
            LOG.log(Level.FINE, "model is automatic!");
            predicateList.add(criteriaBuilder.equal(from.get(TGroupingResults_.grpresIsAutoFl), true));
        }
        query.where(predicateList.toArray(new Predicate[predicateList.size()]));

        //orderClause
        if (orderAsc != null) {

            if (orderAsc) {
                query.orderBy(criteriaBuilder.asc(from.get(TGroupingResults_.creationDate)));
            } else {
                query.orderBy(criteriaBuilder.desc(from.get(TGroupingResults_.creationDate)));
            }
        }

        TypedQuery<TGroupingResults> criteriaQuery = getEntityManager().createQuery(query);
        //build EntityGraph                     
        EntityGraph<TGroupingResults> fetchAll = getEntityManager().createEntityGraph(TGroupingResults.class);
        fetchAll.addAttributeNodes(TGroupingResults_.caseIcd);
//        fetchAll.addSubgraph(TGroupingResults_.checkResults);
        fetchAll.addSubgraph(TGroupingResults_.caseDetails);
        fetchAll.addSubgraph(TGroupingResults_.caseIcdGroupeds).addAttributeNodes(TCaseIcdGrouped_.caseIcd.getName());
        fetchAll.addSubgraph(TGroupingResults_.caseOpsGroupeds).addAttributeNodes(TCaseOpsGrouped_.caseOps.getName());
        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), fetchAll);

        return criteriaQuery;

    }

//    @Override
//    public void deleteById(final long id) {
//
//        try {
//            Query query = getEntityManager()
//                    //CPX-532:fix for mssql: cascade keyword causes error 
//                    //              .createNativeQuery("delete from T_GROUPING_RESULTS cascade where id = " + String.valueOf(id));
//                    .createNativeQuery("delete from T_GROUPING_RESULTS where id = " + String.valueOf(id));
//            query.executeUpdate();
//            LOG.log(Level.INFO, "remove done ");
//        } catch (final RuntimeException re) {
//            LOG.log(Level.INFO, "remove failed", re);
//            throw re;
//        }
//    }
    /**
     * find the grouping result in the database for the md currently stored in
     * the database identified by the hbx flag
     *
     * @param pVersionId version id
     * @param pModel grouper model
     * @return grouper result or null if nothing was found
     */
    public TGroupingResults findTGroupingResult(long pVersionId, GDRGModel pModel) {
        //Select gr.* FROM T_GROUPING_RESULTS gr 
        //JOIN T_CASE_ICD icd ON icd.ID = gr.T_CASE_ICD_ID 
        //WHERE icd.MAIN_DIAG_CASE_FL = 1 AND gr.MODEL_ID_EN = 'GDRG2016' AND gr.T_CASE_DETAILS_ID = 19002;

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TGroupingResults> query = criteriaBuilder.createQuery(TGroupingResults.class);
        Root<TGroupingResults> from = query.from(TGroupingResults.class);

        Join<TGroupingResults, TCaseIcd> join = from.join(TGroupingResults_.caseIcd);

        query.where(criteriaBuilder.equal(from.get(TGroupingResults_.caseDetails), pVersionId),
                criteriaBuilder.equal(from.get(TGroupingResults_.modelIdEn), pModel),
                criteriaBuilder.equal(join.get(TCaseIcd_.icdcIsHdxFl), true));

        TypedQuery<TGroupingResults> criteriaQuery = getEntityManager().createQuery(query);
        EntityGraph<TGroupingResults> fetchAll = getEntityManager().createEntityGraph(TGroupingResults.class);
        fetchAll.addSubgraph(TGroupingResults_.caseIcdGroupeds).addAttributeNodes(TCaseIcdGrouped_.caseIcd.getName());
        fetchAll.addSubgraph(TGroupingResults_.caseOpsGroupeds).addAttributeNodes(TCaseOpsGrouped_.caseOps.getName());
        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), fetchAll);

        return getSingleResultOrNull(criteriaQuery);
    }

    public TGroupingResults findGroupingResult_nativ(long pVersionId, GDRGModel pModel) {
        long start = System.currentTimeMillis();
        TGroupingResults result = findGroupingResult_nativ_lazy(pVersionId, pModel);
        LOG.log(Level.FINE, "fetch result object in " + (System.currentTimeMillis() - start) + " ms");
        start = System.currentTimeMillis();
        if (result != null) {
            Hibernate.initialize(result.getCaseDetails());
            Iterator<TCaseIcdGrouped> icdIt = result.getCaseIcdGroupeds().iterator();
            while (icdIt.hasNext()) {
                TCaseIcdGrouped next = icdIt.next();
                Hibernate.initialize(next.getCaseIcd());
                TCaseIcd icd = next.getCaseIcd();
//                if(icd.getGroupingResultses() != null){
//                    Set<TGroupingResults> grRess = icd.getGroupingResultses();
//                    Hibernate.initialize(icd.getGroupingResultses());
//                    for(TGroupingResults grRes: grRess){
//                        Hibernate.initialize(grRes);
//                    }
                
//            }

//                next.getCaseIcd().getId();
//                next.getCaseIcd().getCaseIcdGroupeds().iterator();
            }
            Iterator<TCaseOpsGrouped> opsIt = result.getCaseOpsGroupeds().iterator();
            while (opsIt.hasNext()) {
                TCaseOpsGrouped next = opsIt.next();
                Hibernate.initialize(next.getCaseOps());
//                next.getCaseOps().getCaseOpsGroupeds().iterator();
            }
            if (result.getGrpresType().equals(CaseTypeEn.PEPP)) {
                Hibernate.initialize(result.getCasePepp().getPeppcGrades());
            }
            if (result.getGrpresType().equals(CaseTypeEn.DRG)) {
                Hibernate.initialize(result.getCaseDrg().getDrgCareGrades());
            }
        }

        LOG.log(Level.FINE, "fetch additional data for grpres id " + pVersionId + " in " + (System.currentTimeMillis() - start) + " ms");
        return result;
    }

    public TGroupingResults findGroupingResult_nativ(long pId) {
        long start = System.currentTimeMillis();
        TGroupingResults result = findGroupingResult(pId);
        LOG.log(Level.FINE, "fetch result object for grpres id " + pId + " in " + (System.currentTimeMillis() - start) + " ms");
        start = System.currentTimeMillis();
        if (result != null) {
//            Hibernate.initialize(result.getCaseIcdGroupeds());
            Iterator<TCaseIcdGrouped> icdIt = result.getCaseIcdGroupeds().iterator();
            while (icdIt.hasNext()) {
                TCaseIcdGrouped next = icdIt.next();
                Hibernate.initialize(next.getCaseIcd());
                Hibernate.initialize(next.getCaseIcd().getGroupingResultses());
//                next.getCaseIcd().getId();
//                next.getCaseIcd().getCaseIcdGroupeds().iterator();
            }
//            Hibernate.initialize(result.getCaseOpsGroupeds());
            Iterator<TCaseOpsGrouped> opsIt = result.getCaseOpsGroupeds().iterator();
            while (opsIt.hasNext()) {
                TCaseOpsGrouped next = opsIt.next();
                Hibernate.initialize(next.getCaseOps());
//                next.getCaseOps().getCaseOpsGroupeds().iterator();
            }
            if (result.getGrpresType().equals(CaseTypeEn.PEPP)) {
                Hibernate.initialize(result.getCasePepp().getPeppcGrades());
            }
           if (result.getGrpresType().equals(CaseTypeEn.DRG)) {
                Hibernate.initialize(result.getCaseDrg().getDrgCareGrades());
            }
        }

        LOG.log(Level.FINE, "fetch additional data for grpres id " + pId + " in " + (System.currentTimeMillis() - start) + " ms");
        return result;
    }

    public TGroupingResults findGroupingResult_nativ_lazy(long pDetailsId, GDRGModel pGrouperModel) {

        //2017-12-05 DNi: It would be better to use a prepared statement here!
//2019-07-31 CPX-1839 this statement does not fond the grouping results for the cases without main diagnosis
//        String sql = String.format("Select gr.* FROM T_GROUPING_RESULTS gr "
//                + "JOIN T_CASE_ICD icd ON icd.ID = gr.T_CASE_ICD_ID "
//                + "WHERE icd.MAIN_DIAG_CASE_FL = 1 AND gr.MODEL_ID_EN = '%s' AND gr.T_CASE_DETAILS_ID = %s ", pGrouperModel.name(), pDetailsId);
//        if (pGrouperModel.equals(GDRGModel.AUTOMATIC)) {
//            sql = String.format("Select gr.* FROM T_GROUPING_RESULTS gr "
//                    + "JOIN T_CASE_ICD icd ON icd.ID = gr.T_CASE_ICD_ID "
//                    + "WHERE icd.MAIN_DIAG_CASE_FL = 1 AND gr.GRPRES_IS_AUTO_FL = 1 AND gr.T_CASE_DETAILS_ID = %s ", pDetailsId);
//        }
//2019-07-31 CPX-1839 
        String sql = String.format("Select gr.* FROM T_GROUPING_RESULTS gr "
                + " WHERE gr.MODEL_ID_EN = '%s' AND gr.T_CASE_DETAILS_ID = %s "
                + " and (exists (select 1 from T_CASE_ICD icd WHERE icd.MAIN_DIAG_CASE_FL = 1 and icd.ID = gr.T_CASE_ICD_ID)"
                + " or gr.T_CASE_ICD_ID is null )", pGrouperModel.name(), pDetailsId);
        if (pGrouperModel.equals(GDRGModel.AUTOMATIC)) {
            sql = String.format("Select gr.* FROM T_GROUPING_RESULTS gr "
                    + "WHERE gr.GRPRES_IS_AUTO_FL = 1 AND gr.T_CASE_DETAILS_ID = %s "
                    + " and (exists (select 1 from T_CASE_ICD icd WHERE icd.MAIN_DIAG_CASE_FL = 1 and icd.ID = gr.T_CASE_ICD_ID)"
                    + " or gr.T_CASE_ICD_ID is null )", pDetailsId);
        }
        LOG.log(Level.FINE, "Query for Grouping Result: " + sql);
        Query query = getEntityManager().createNativeQuery(sql, TGroupingResults.class);
        if (query.getResultList().isEmpty()) {
            return null;
        }
        return getSingleResultOrNull(query);
    }

    public TGroupingResults findGroupingResult(long pId) {

        //2017-12-05 DNi: It would be better to use a prepared statement here!
//2019-07-31 CPX-1839 this statement does not fond the grouping results for the cases without main diagnosis
        String sql = String.format("Select gr.* FROM T_GROUPING_RESULTS gr "
                + "JOIN T_CASE_ICD icd ON icd.ID = gr.T_CASE_ICD_ID "
                + "WHERE icd.MAIN_DIAG_CASE_FL = 1 AND gr.ID = %s ", pId);
//        if (pGrouperModel.equals(GDRGModel.AUTOMATIC)) {
//            sql = String.format("Select gr.* FROM T_GROUPING_RESULTS gr "
//                    + "JOIN T_CASE_ICD icd ON icd.ID = gr.T_CASE_ICD_ID "
//                    + "WHERE icd.MAIN_DIAG_CASE_FL = 1 AND gr.GRPRES_IS_AUTO_FL = 1 AND gr.T_CASE_DETAILS_ID = %s ", pDetailsId);
//        }
        LOG.log(Level.FINE, "Query for Grouping Result: " + sql);
        Query query = getEntityManager().createNativeQuery(sql, TGroupingResults.class);
        if (query.getResultList().isEmpty()) {
            return null;
        }
        return getSingleResultOrNull(query);
    }
//    public void deleteItems(List<TGroupingResults> items) {
//       int i = 0;
//        for(TGroupingResults hcase: items)
//        {
//            if(++i%25==0)
//            {
//                getEntityManager().flush();
//            }
//
//            CriteriaBuilder criteriaBuilder=getEntityManager().getCriteriaBuilder();
//            CriteriaDelete<TGroupingResults> criteriaDelete = criteriaBuilder.createCriteriaDelete(TGroupingResults.class);
//
//            Root<TGroupingResults> from = criteriaDelete.from(TGroupingResults.class);
//            criteriaDelete.where(criteriaBuilder.equal(from.get(TGroupingResults_.id), hcase.getId()));
//            getEntityManager().createQuery(criteriaDelete).executeUpdate();
//        }    
//    }

    public void persistsItems(List<TGroupingResults> items) {
        int i = 0;
        for (TGroupingResults result : items) {
            if (i % 25 == 0) {
                getEntityManager().flush();
//                getEntityManager().clear();
            }
            CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaUpdate<TGroupingResults> criteriaDelete = criteriaBuilder.createCriteriaUpdate(TGroupingResults.class);
            Root<TGroupingResults> from = criteriaDelete.from(TGroupingResults.class);
            criteriaDelete.set(from.get(TGroupingResults_.grpresCode), result.getGrpresCode());
            criteriaDelete.set(from.get("caseIcdGroupeds"), result.getCaseIcdGroupeds());
            criteriaDelete.set(from.get("caseOpsGroupeds"), result.getCaseOpsGroupeds());
            criteriaDelete.set(from.get("checkResults"), result.getCheckResults());
            criteriaDelete.set(from.get(TGroupingResults_.grpresGpdx), result.getGrpresGpdx());
            criteriaDelete.set(from.get(TGroupingResults_.grpresGroup), result.getGrpresGroup());
            criteriaDelete.set(from.get(TGroupingResults_.grpresPccl), result.getGrpresPccl());
            criteriaDelete.set(from.get(TGroupingResults_.modificationDate), new Date());
//            criteriaDelete.set(from.get(TGroupingResults_.modificationUser), );
            criteriaDelete.where(criteriaBuilder.equal(from.get(TGroupingResults_.caseDetails), result.getCaseDetails()), criteriaBuilder.equal(from.get(TGroupingResults_.modelIdEn), result.getModelIdEn()));
            getEntityManager().createQuery(criteriaDelete).executeUpdate();
//            long start = System.currentTimeMillis();
//            TCaseDetails details = getEntityManager().merge(result.getCaseDetails());
//            details.getGroupingResultses().add(result);
//            if(i%50==0){
//                LOG.info("merge " + (System.currentTimeMillis()-start));
//            }
//            start = System.currentTimeMillis();
//            result.setCaseDetails(details);
//            persist(result);
//            if(i%50==0){
//                LOG.info("persist " + (System.currentTimeMillis()-start));
//            }
//            i++;
////            getSession().saveOrUpdate(result);
            Logger.getLogger("persist item " + i);
        }
    }

    public int deleteByDetailsId(List<TCaseDetails> items, GDRGModel pModel) {
        String sql = "delete from TGroupingResults WHERE caseDetails IN (:ids) ";
//        String in = items.stream().map((aLong) -> aLong.toString()).collect(Collectors.joining(","));
//        sql = sql.concat(in + " ) ");
        Query query = null;
        if (pModel.equals(GDRGModel.AUTOMATIC)) {
            sql = sql.concat("AND grpresIsAutoFl = true");
            query = getEntityManager().createQuery(sql);
            query.setParameter("ids", items);
        } else {
            sql = sql.concat("AND modelIdEn = :model");
            query = getEntityManager().createQuery(sql);
            query.setParameter("ids", items);
            query.setParameter("model", pModel);
//            query.executeUpdate();  
        }
//        query.setParameter("ids", in);
        return query.executeUpdate();
    }

    /**
     * deletes object with specified ids from the database
     *
     * @param pIds list of ids (pk)
     * @return number of affected rows
     */
    public Integer deleteByIds(List<Long> pIds) {
        String sql = "delete from TGroupingResults res WHERE res.id IN (:ids)";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("ids", pIds);
        return query.executeUpdate();
    }

    /**
     * @param pModel gdrg modl
     * @param pIsLocal version type
     * @return count of grouping results for that groupermodel and choosen
     * version type
     */
    public Integer getCountForModel(GDRGModel pModel, Boolean pIsLocal) {

        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);

        Root<TGroupingResults> from = cq.from(TGroupingResults.class);

        //joins
        Join<TGroupingResults, TCaseIcd> join1 = from.join(TGroupingResults_.caseIcd);       

        Join<TCaseIcd, TCaseDepartment> join2 = join1.join(TCaseIcd_.caseDepartment);
        Join<TCaseDepartment, TCaseDetails> join3 = join2.join(TCaseDepartment_.caseDetails);
        
        cq.select(qb.count(from));
        cq.where((GDRGModel.AUTOMATIC.equals(pModel)?
                qb.equal(from.get(TGroupingResults_.GRPRES_IS_AUTO_FL), 1):
                
                qb.equal(from.get(TGroupingResults_.modelIdEn), pModel)),
                qb.equal(join1.get(TCaseIcd_.icdcIsHdxFl), Boolean.TRUE),
                qb.equal(join3.get(TCaseDetails_.csdIsLocalFl), pIsLocal),
                qb.equal(join3.get(TCaseDetails_.csdIsActualFl), Boolean.TRUE),
                qb.isNull(join3.get(TCaseDetails_.csdCancelReasonEn))
        );
        return getEntityManager().createQuery(cq).getSingleResult().intValue();
    }

   /**
     * @param pModel gdrg modl
     * @param pIsLocal version type
     * @return count of grouping results for that groupermodel and choosen
     * version type
     */
    public Integer getCountForModelAndType(GDRGModel pModel, Boolean pIsLocal, CaseTypeEn pType) {
        return getCountForModelAndType(pModel, pIsLocal, pType, null);
    }

    public Integer getCountForModelAndType(GDRGModel pModel, Boolean pIsLocal, CaseTypeEn pType, Long pPatientId) {

            String qry = "SELECT COUNT(*) FROM T_GROUPING_RESULTS GR " +
                "WHERE EXISTS " +
                "(SELECT 1 FROM T_CASE_ICD  ICD " +
                "WHERE ICD.ID = GR.T_CASE_ICD_ID AND ICD.MAIN_DIAG_CASE_FL = 1) " +
                "AND EXISTS(SELECT 1 FROM T_CASE_DETAILS TD " +
                 (pPatientId == null?"":" INNER JOIN T_CASE CS ON CS.ID = TD.T_CASE_ID ")   +
                " WHERE TD.ID = GR.T_CASE_DETAILS_ID AND TD.LOCAL_FL = " + 
                (pIsLocal?"1":"0") +
                " AND TD.ACTUAL_FL = 1" + 
                (pPatientId == null?"": (" AND CS.CANCEL_FL = 0 AND CS.T_PATIENT_ID = " + String.valueOf(pPatientId))) +
                ") " +
                " AND GR.GRPRES_TYPE_EN = '" + pType.name() + "' AND " + 
                (pModel.equals((GDRGModel.AUTOMATIC))?"GR.GRPRES_IS_AUTO_FL = 1":("GR.GRPRES_IS_AUTO_FL = 0 and GR.MODEL_ID_EN = '" + pModel.name() + "'"));
        
        
            List<Number> list = getEntityManager().createNativeQuery(qry).getResultList();
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

    public boolean hasGroupingResultOld(GDRGModel pModel, Long... pIds) {
        String sql = "select count(*) from T_GROUPING_RESULTS WHERE T_CASE_DETAILS_ID IN (:ids) ";
        String dbIds =  Arrays.asList(pIds).stream().filter((t) -> {
            return t!=null;
        }).map((t) -> {
            return String.valueOf(t);
        }).collect(Collectors.joining(","));
        if(dbIds.isEmpty()){
            return false;
        }
        Query query = null;
        if (pModel.equals(GDRGModel.AUTOMATIC)) {
            sql = sql.concat("AND GRPRES_IS_AUTO_FL = 1");
            sql = sql.replace(":ids", dbIds);
            query = getEntityManager().createNativeQuery(sql);
//            query.setParameter(1, dbIds);
        } else {
            sql = sql.concat("AND GRPRES_IS_AUTO_FL = 0 AND MODEL_ID_EN = ':model'"); //remove '-chars to try to set via setParameter
            //somehow setParameter will not work for groupermodel
            //result will be empty
            sql = sql.replace(":ids", dbIds);
            sql = sql.replace(":model", pModel.name());
            query = getEntityManager().createNativeQuery(sql);
//            query.setParameter("ids", dbIds);
//            query.setParameter("model", pModel.name());
        }
        Object result = query.getSingleResult();
        int cnt = -1;
        if(result instanceof BigDecimal){
            cnt = ((BigDecimal) query.getSingleResult()).intValue();
        }else if(result instanceof Integer){
            cnt = (Integer) result;
        }else{
            LOG.severe("can not parse result - unknown ReturnType! Expected BigDecimal or Integer, but was " + result.getClass());
        }
        // now calcul
        return pIds.length == cnt; // given ids have result when count match size of given ids 
    }

    public boolean hasGroupingResult(GDRGModel pModel, Long... pIds) {
        for(Long id: pIds){
            if(!hasAllGrouped(pModel, id)){
                return false;
            }
        }
        return true;
    }
    
    public boolean hasAllGrouped(GDRGModel pModel, Long pCaseDetailsId){
        String sql = "select count(*) from T_GROUPING_RESULTS WHERE T_CASE_DETAILS_ID =:id ";
            sql = sql.replace(":id", String.valueOf(pCaseDetailsId));
// find all grouping results for icds and model
        Query query = null;
        if (pModel.equals(GDRGModel.AUTOMATIC)) {
            sql = sql.concat("AND GRPRES_IS_AUTO_FL = 1");
            query = getEntityManager().createNativeQuery(sql);
//            query.setParameter(1, dbIds);
        } else {
            sql = sql.concat("AND GRPRES_IS_AUTO_FL = 0 AND MODEL_ID_EN = ':model'"); //remove '-chars to try to set via setParameter
            //somehow setParameter will not work for groupermodel

            sql = sql.replace(":model", pModel.name());
            query = getEntityManager().createNativeQuery(sql);
//            query.setParameter("ids", dbIds);
//            query.setParameter("model", pModel.name());
        }
        Object result = query.getSingleResult();
        int cnt = -1;
        if(result instanceof BigDecimal){
            cnt = ((BigDecimal) query.getSingleResult()).intValue();
        }else if(result instanceof Integer){
            cnt = (Integer) result;
        }else{
            LOG.severe("can not parse result - unknown ReturnType! Expected BigDecimal or Integer, but was " + result.getClass());
        }
 // find number of grouping icds      
        sql = "select count(*) from t_case_department dep " +
            "inner join t_case_icd icd on icd.T_CASE_DEPARTMENT_ID = dep.id " +
            "where icd.TO_GROUP_FL = 1 and dep.T_CASE_DETAILS_ID =:id ";
        sql = sql.replace(":id", String.valueOf(pCaseDetailsId));
        query = getEntityManager().createNativeQuery(sql);
        Object result1 = query.getSingleResult();
        int cnt1 = -1;
        if(result1 instanceof BigDecimal){
            cnt1 = ((BigDecimal) query.getSingleResult()).intValue();
        }else if(result instanceof Integer){
            cnt1 = (Integer) result1;
        }else{
            LOG.severe("can not parse result - unknown ReturnType! Expected BigDecimal or Integer, but was " + result1.getClass());
        }
        return cnt == cnt1;
    }

    public List<TCase2RuleSelection> getCheckCaseRules4GroupingResult(long groupingResultsId) {
        
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCase2RuleSelection> query = qb.createQuery(TCase2RuleSelection.class);

        Root<TCase2RuleSelection> from = query.from(TCase2RuleSelection.class);
        Join<TCase2RuleSelection, TCase> join = from.join(TCase2RuleSelection_.hospitalCase);
        Join<TCase, TCaseDetails> join1 = join.join(TCase_.caseDetails);
        Join<TCaseDetails, TGroupingResults>join2 = join1.join(TCaseDetails_.groupingResultses);
        query.where(//qb.equal(join1.get(TCaseDetails_.csdIsLocalFl), false),
                //qb.equal(join1.get(TCaseDetails_.csdVersion), 1),
                qb.equal(join2.get(TGroupingResults_.id), groupingResultsId));
        return getEntityManager().createQuery(query).getResultList();
    }

}
