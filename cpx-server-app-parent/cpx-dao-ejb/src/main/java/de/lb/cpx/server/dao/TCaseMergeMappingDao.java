/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.dao;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseDetails_;
import de.lb.cpx.model.TCaseDrgCareGrades;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseIcd_;
import de.lb.cpx.model.TCaseMergeMapping;
import de.lb.cpx.model.TCaseMergeMapping_;
import de.lb.cpx.model.TCase_;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TGroupingResults_;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.server.commons.enums.EntityGraphType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.Query;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Hibernate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Subquery;
/**
 *
 * @author gerschmann
 */
@Stateless
@SuppressWarnings("unchecked")
public class TCaseMergeMappingDao extends AbstractCpxDao<TCaseMergeMapping> {

    private static final Logger LOG = Logger.getLogger(TCaseMergeMappingDao.class.getName());

    public TCaseMergeMappingDao() {
        super(TCaseMergeMapping.class);
    }

    public int deleteAll() {
        String sql = "delete from TCaseMergeMapping mergeMap";
        Query query = getEntityManager().createQuery(sql);
        return query.executeUpdate();
    }

    public int deleteAllNotMergedEntries() {
        String sql = "delete from TCaseMergeMapping as mergeMap where mergeMap.caseByHoscId is null";
        Query query = getEntityManager().createQuery(sql);
        return query.executeUpdate();
    }

    public int deleteAllNotMergedEntries(CaseTypeEn pGrpresType) {
        String sql = "delete from TCaseMergeMapping as mergeMap where mergeMap.caseByHoscId is null AND mergeMap.grpresType = :type";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("type", pGrpresType);
        return query.executeUpdate();
    }

//    /**
//     * @return list of all viable Enties ( HOSC_ID = NULL) WARNING: due to
//     * entitygraph methode may have poor performance
//     */
//    public List<TCaseMergeMapping> findAllViableEntries() {
//        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<TCaseMergeMapping> query = criteriaBuilder.createQuery(TCaseMergeMapping.class);
//        query.distinct(true);
//        Root<TCaseMergeMapping> from = query.from(TCaseMergeMapping.class);
//        query.where(criteriaBuilder.isNull(from.get(TCaseMergeMapping_.caseByHoscId)));
//        //maybe more lightweight methode with jpql? some fetchjoins?
//        TypedQuery<TCaseMergeMapping> criteriaQuery = addEntityGraph(getEntityManager().createQuery(query));
//        return criteriaQuery.getResultList();
//    }

    /**
     * @param pGrpresType type drg,pepp,etc
     * @param pGrouperModel
     * @param pPatientId
     * @return list of all viable Enties ( HOSC_ID = NULL) WARNING: due to
     * entitygraph methode may have poor performance
     */
    public List<TCaseMergeMapping> findViableEntriesForGrpresType(CaseTypeEn pGrpresType,  
            GDRGModel pGrouperModel, 
            Long pPatientId) {
        var criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseMergeMapping> query = criteriaBuilder.createQuery(TCaseMergeMapping.class);
        query.distinct(true);
        Root<TCaseMergeMapping> from = query.from(TCaseMergeMapping.class);
        Join<TCaseMergeMapping, TGroupingResults> join1 = from.join(TCaseMergeMapping_.grpresId);
        Join<TCaseMergeMapping, TCase> join2 = from.join(TCaseMergeMapping_.caseByMergeMemberCaseId);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.isNull(from.get(TCaseMergeMapping_.caseByHoscId)));
        predicates.add(criteriaBuilder.equal(from.get(TCaseMergeMapping_.grpresType), pGrpresType));
        if((GDRGModel.AUTOMATIC.equals(pGrouperModel))){
            predicates.add(criteriaBuilder.equal(join1.get(TGroupingResults_.grpresIsAutoFl), 1));
        }else{
            predicates.add( criteriaBuilder.equal(join1.get(TGroupingResults_.grpresIsAutoFl), 0));                
            predicates.add(criteriaBuilder.equal(join1.get(TGroupingResults_.modelIdEn), pGrouperModel));
            
        }
        predicates.add(criteriaBuilder.equal(join2.get(TCase_.csCancellationReasonEn), false));
        predicates.add(criteriaBuilder.notLike(join2.get(TCase_.csCaseNumber), "%_m"));

        if(pPatientId != null){
            predicates.add( criteriaBuilder.equal(join2.get(TCase_.PATIENT), pPatientId));
        }
        query.where(predicates.toArray(new Predicate[predicates.size()]));
//        if( pPatientId == null){
//            query.where(criteriaBuilder.isNull(from.get(TCaseMergeMapping_.caseByHoscId)), 
//                   criteriaBuilder.equal(from.get(TCaseMergeMapping_.grpresType), pGrpresType),
//                   (GDRGModel.AUTOMATIC.equals(pGrouperModel)?
//                   criteriaBuilder.equal(join1.get(TGroupingResults_.GRPRES_IS_AUTO_FL), 1):                
//                   criteriaBuilder.equal(join1.get(TGroupingResults_.modelIdEn), pGrouperModel)),
//                   criteriaBuilder.equal(join2.get(TCase_.csCancellationReasonEn), false)
//                  );
//        }else{
//            query.where(criteriaBuilder.isNull(from.get(TCaseMergeMapping_.caseByHoscId)), 
//                   criteriaBuilder.equal(from.get(TCaseMergeMapping_.grpresType), pGrpresType),
//                   (GDRGModel.AUTOMATIC.equals(pGrouperModel)?
//                   criteriaBuilder.equal(join1.get(TGroupingResults_.GRPRES_IS_AUTO_FL), 1):                
//                   criteriaBuilder.equal(join1.get(TGroupingResults_.modelIdEn), pGrouperModel)),
//                   criteriaBuilder.equal(join2.get(TCase_.csCancellationReasonEn), false),
//                   criteriaBuilder.equal(join2.get(TCase_.PATIENT), pPatientId)
//                  );
//            
//        }
//            
//          
//        //maybe more lightweight methode with jpql? some fetchjoins?
        TypedQuery<TCaseMergeMapping> criteriaQuery = addEntityGraph(getEntityManager().createQuery(query));
        List<TCaseMergeMapping> results = criteriaQuery.getResultList();
        if(results == null){
            return new ArrayList<TCaseMergeMapping>();
        }
        //if pepp initialize grades to compute revenue and set tooltip in client

        for (TCaseMergeMapping map : results) {
            try{
                if (pGrpresType.equals(CaseTypeEn.PEPP)) {
                        Hibernate.initialize(map.getGrpresId().getCasePepp().getPeppcGrades());
                    }

            }catch(Exception ex){
                LOG.log(Level.SEVERE, "mapID:" + map.getId() + "grpResID: " + map.getGrpresId(), ex);
            }
        }
        if(CaseTypeEn.DRG.equals(pGrpresType)){
            for (TCaseMergeMapping map : results) {
                Set<TCaseDrgCareGrades> grades = map.getGrpresId().getCaseDrg().getDrgCareGrades();
                if(grades != null){
                    Hibernate.initialize(map.getGrpresId().getCaseDrg().getDrgCareGrades());
                }
            }
        }
        
        return results;
    }

    public int getNextMergeIdent() {

        if (isOracle()) {
            //Oracle
            BigDecimal seq = (BigDecimal) getEntityManager().createNativeQuery("select CASE_MERGE_IDENT_SEQ.nextval from dual").getResultList().get(0);
            return seq.intValue();
        } else {
            //MS SQL
            BigInteger seq = (BigInteger) (Number) getEntityManager().createNativeQuery("select NEXT VALUE FOR CASE_MERGE_IDENT_SEQ").getResultList().get(0);
            return seq.intValue();
        }

    }

    private TypedQuery<TCaseMergeMapping> addEntityGraph(TypedQuery<TCaseMergeMapping> criteriaQuery) {
        EntityGraph<TCaseMergeMapping> toFetch = getEntityManager().createEntityGraph(TCaseMergeMapping.class);
        //toFetch.addAttributeNodes(TCase_.currentExtern,TCase_.currentLocal);
        //toFetch.addSubgraph(TCase_.patient).addAttributeNodes(TPatient_.patInsuranceActual);
        toFetch.addSubgraph(TCaseMergeMapping_.grpresId);
        Subgraph<TCase> caseGraph = toFetch.addSubgraph(TCaseMergeMapping_.caseByMergeMemberCaseId);
        caseGraph.addAttributeNodes(TCase_.caseDetails);
        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
        return criteriaQuery;
    }

    public List<TCaseMergeMapping> findByMergeId(Integer pMergeId) {
        LOG.info("try to find merge mappings for mergeID: " + pMergeId);
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseMergeMapping> query = criteriaBuilder.createQuery(TCaseMergeMapping.class);
        query.distinct(true);
        Root<TCaseMergeMapping> from = query.from(TCaseMergeMapping.class);
        query.where(criteriaBuilder.equal(from.get(TCaseMergeMapping_.mrgMergeIdent), pMergeId));
        //maybe more lightweight methode with jpql? some fetchjoins?
        TypedQuery<TCaseMergeMapping> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

    /**
     * @param pIdent merge ident
     * @return list of merge case member objects
     */
    public List<TCase> findCasesByIdent(Integer pIdent) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCase> query = criteriaBuilder.createQuery(TCase.class);
        Root<TCaseMergeMapping> from = query.from(TCaseMergeMapping.class);
//        query.distinct(true);
        query.select(from.get(TCaseMergeMapping_.caseByMergeMemberCaseId));
        query.where(criteriaBuilder.equal(from.get(TCaseMergeMapping_.mrgMergeIdent), pIdent));
        //maybe more lightweight methode with jpql? some fetchjoins?
        TypedQuery<TCase> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

    /**
     * find mergeMapping members for patient with patient id
     * @param pGrpresType type of cases to look for. is pGrpresType is null, will get all cases
     * @param pGrouperModel grouper model
     * @param pPatientId patient id
     * @param csIds ids of cases to exclude from list
     * @return the list of mappinds
     **/
    public List<TCaseMergeMapping> findNotMergingCasesForType(CaseTypeEn pGrpresType, 
            GDRGModel pGrouperModel, 
            Long pPatientId, 
            List<Long> csIds) {
        LOG.log(Level.INFO, "start findNotMergingCasesForType: ");
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseMergeMapping> query = criteriaBuilder.createQuery(TCaseMergeMapping.class);
        Root<TCase> from = query.from(TCase.class);
        Join<TCase, TCaseDetails> join = from.join(TCase_.caseDetails);
        Join<TCaseDetails, TGroupingResults> join1 = join.join(TCaseDetails_.groupingResultses);
        query.select(criteriaBuilder.construct(TCaseMergeMapping.class, from, join1, from.get(TCase_.csCaseTypeEn)));
        
        List<Predicate> predicates = new ArrayList<>();
        // from t_case
        predicates.add(criteriaBuilder.equal(from.get(TCase_.PATIENT), pPatientId));
        if(pGrpresType != null){
            predicates.add(criteriaBuilder.equal(from.get(TCase_.csCaseTypeEn), pGrpresType));
        }
        predicates.add(criteriaBuilder.equal(from.get(TCase_.csCancellationReasonEn), false));
        if(csIds!= null && !csIds.isEmpty()){
            predicates.add(criteriaBuilder.not(from.get(TCase_.ID).in(csIds) ));
        }
        // from t_case_details
        predicates.add(criteriaBuilder.equal(join.get(TCaseDetails_.csdIsLocalFl), true));
        predicates.add(criteriaBuilder.equal(join.get(TCaseDetails_.csdIsActualFl), true));
        // t_grouping_results
        if((GDRGModel.AUTOMATIC.equals(pGrouperModel))){
            predicates.add(criteriaBuilder.equal(join1.get(TGroupingResults_.grpresIsAutoFl), 1));
        }else{
            predicates.add( criteriaBuilder.equal(join1.get(TGroupingResults_.grpresIsAutoFl), 0));                
            predicates.add(criteriaBuilder.equal(join1.get(TGroupingResults_.modelIdEn), pGrouperModel));
            
        }
 
        // subquery on t_case_icd
        Subquery <TCaseIcd> subquery = query.subquery(TCaseIcd.class);
        Root<TCaseIcd> icdFrom = subquery.from(TCaseIcd.class);
        subquery.select(icdFrom);
        subquery.where(criteriaBuilder.equal(icdFrom.get(TCaseIcd_.icdcIsHdxFl), true));
        
        predicates.add(criteriaBuilder.or(criteriaBuilder.isNull(join1.get(TGroupingResults_.caseIcd)),
               (join1.get(TGroupingResults_.caseIcd).in(subquery))));
        query.where(predicates.toArray(new Predicate[predicates.size()]));
        
//        EntityGraph<TCaseMergeMapping> toFetch = getEntityManager().createEntityGraph(TCaseMergeMapping.class);
//        
//        Subgraph<TCase> caseGraph = toFetch.addSubgraph(TCaseMergeMapping_.caseByMergeMemberCaseId);
//        caseGraph.addAttributeNodes(TCase_.caseDetails);
        TypedQuery<TCaseMergeMapping>  criteriaQuery = getEntityManager().createQuery(query);
//        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
        List<TCaseMergeMapping> results = criteriaQuery.getResultList();
        if(results == null){
            return new ArrayList<TCaseMergeMapping>();
        }

        for (TCaseMergeMapping map : results) {
            Hibernate.initialize(map.getCaseByMergeMemberCaseId());
            Hibernate.initialize(map.getCaseByMergeMemberCaseId().getCurrentLocal());
            
            try{
                if (pGrpresType != null && pGrpresType.equals(CaseTypeEn.PEPP)) {
                        Hibernate.initialize(map.getGrpresId().getCasePepp().getPeppcGrades());
                    }

            }catch(Exception ex){
                LOG.log(Level.SEVERE, "mapID:" + map.getId() + "grpResID: " + map.getGrpresId(), ex);
            }
        }
        if(pGrpresType != null && CaseTypeEn.DRG.equals(pGrpresType)){
            for (TCaseMergeMapping map : results) {
                Set<TCaseDrgCareGrades> grades = map.getGrpresId().getCaseDrg().getDrgCareGrades();
                if(grades != null){
                    Hibernate.initialize(map.getGrpresId().getCaseDrg().getDrgCareGrades());
                }
            }
        }

        
        return results;
        
    }

    public List<String> getCaseNumbers4CanceledCase4Merge(long pCaseId) {
        // get ident for case with pCaseId
        int ident = getMergeIdent4MemberCaseId(pCaseId);
        if(ident == 0){
            return null;
        }
        //get merged case for ident
        List<TCase> cases = this.findCasesByIdent(ident) ;
        // get all cases for ident
        List<String> retList = new ArrayList<>();
        if(cases == null){
            return retList;
        }

        for(TCase cs: cases){
            retList.add(cs.getCsCaseNumber());
            
        }
        return retList;
    }
    
    private int getMergeIdent4MemberCaseId(long pCaseId){
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Integer> query = criteriaBuilder.createQuery(Integer.class);
        Root<TCaseMergeMapping> from = query.from(TCaseMergeMapping.class);
//        query.distinct(true);
        query.select(from.get(TCaseMergeMapping_.mrgMergeIdent));
        query.where(criteriaBuilder.equal(from.get(TCaseMergeMapping_.caseByMergeMemberCaseId), pCaseId));
        //maybe more lightweight methode with jpql? some fetchjoins?
        TypedQuery<Integer> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList().get(0);        
    }
    
        public List<Long> findMappedAndMergedCases4Patient( GDRGModel pGrouperModel, 
            Long pPatientId ) {
        LOG.log(Level.INFO, "start findMappedAndMergedCases4Patient: ");
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<TCaseMergeMapping> from = query.from(TCaseMergeMapping.class);
        Join<TCaseMergeMapping, TCase> join = from.join(TCaseMergeMapping_.caseByMergeMemberCaseId);
        Join<TCase, TCaseDetails> join2 = join.join(TCase_.caseDetails);
        
        Join<TCaseDetails, TGroupingResults> join1 = join2.join(TCaseDetails_.groupingResultses);
        query.select(join.get(TCase_.id));
       
        List<Predicate> predicates = new ArrayList<>();
        // from t_case
        predicates.add(criteriaBuilder.equal(from.get(TCase_.PATIENT), pPatientId));
        // from t_case_details
        predicates.add(criteriaBuilder.equal(join2.get(TCaseDetails_.csdIsLocalFl), true));
        predicates.add(criteriaBuilder.equal(join2.get(TCaseDetails_.csdIsActualFl), true));
        // t_grouping_results
        if((GDRGModel.AUTOMATIC.equals(pGrouperModel))){
            predicates.add(criteriaBuilder.equal(join1.get(TGroupingResults_.grpresIsAutoFl), 1));
        }else{
            predicates.add( criteriaBuilder.equal(join1.get(TGroupingResults_.grpresIsAutoFl), 0));                
            predicates.add(criteriaBuilder.equal(join1.get(TGroupingResults_.modelIdEn), pGrouperModel));
            
        }

        predicates.add(from.get(TCaseMergeMapping_.caseByHoscId).isNotNull());
        // subquery on t_case_icd
        Subquery <TCaseIcd> subquery = query.subquery(TCaseIcd.class);
        Root<TCaseIcd> icdFrom = subquery.from(TCaseIcd.class);
        subquery.select(icdFrom);
        subquery.where(criteriaBuilder.equal(icdFrom.get(TCaseIcd_.icdcIsHdxFl), true));
        
        predicates.add(criteriaBuilder.or(criteriaBuilder.isNull(join1.get(TGroupingResults_.caseIcd)),
               (join1.get(TGroupingResults_.caseIcd).in(subquery))));
        query.where(predicates.toArray(new Predicate[predicates.size()]));
        
//        EntityGraph<TCaseMergeMapping> toFetch = getEntityManager().createEntityGraph(TCaseMergeMapping.class);
//        
//        Subgraph<TCase> caseGraph = toFetch.addSubgraph(TCaseMergeMapping_.caseByMergeMemberCaseId);
//        caseGraph.addAttributeNodes(TCase_.caseDetails);
        TypedQuery<Long>  criteriaQuery = getEntityManager().createQuery(query);
//        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
        List<Long> results = criteriaQuery.getResultList();
        if(results == null){
            return new ArrayList<Long>();
        }
        
        return results;
        
    }

    public void deleteMappings4GrpresId(long grpResId) {
        String qry = "delete from T_CASE_MERGE_MAPPING  where mrg_Merge_Ident = "
                + "(select mergeMap1.mrg_Merge_Ident from T_CASE_MERGE_MAPPING mergeMap1 where mergeMap1.T_GROUPING_RESULTS_ID = :grpresId)";
        Query nativeQuery = getEntityManager().createNativeQuery(qry);
        nativeQuery.setParameter("grpresId", grpResId);
        nativeQuery.executeUpdate();
    }


}
