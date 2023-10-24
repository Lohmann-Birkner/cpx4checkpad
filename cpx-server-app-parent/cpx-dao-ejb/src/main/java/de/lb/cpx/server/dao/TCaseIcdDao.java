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
import de.lb.cpx.grouper.model.dto.IcdOverviewDTO;
import de.lb.cpx.grouper.model.util.IcdDtoHelper;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDepartment_;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseIcd_;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.server.commons.enums.EntityGraphType;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import org.hibernate.Hibernate;

/**
 *
 * @author wilde
 */
@Stateless
@SuppressWarnings("unchecked")
public class TCaseIcdDao extends AbstractCpxDao<TCaseIcd> {

    private static final Logger LOG = Logger.getLogger(TCaseIcdDao.class.getName());

    /**
     * Creates a new instance.
     */
    public TCaseIcdDao() {
        super(TCaseIcd.class);
    }

    public List<TCaseIcd> findListOfIcd(Long caseDetailId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseIcd> query = criteriaBuilder.createQuery(TCaseIcd.class);
        Root<TCaseIcd> from = query.from(TCaseIcd.class);
        query.distinct(true);

//        from.fetch(TCaseIcd_.refIcds, JoinType.LEFT);
        Join<TCaseIcd, TCaseDepartment> depJoin = from.join(TCaseIcd_.caseDepartment);

        query.where(criteriaBuilder.equal(depJoin.get(TCaseDepartment_.caseDetails), caseDetailId));
        query.orderBy(criteriaBuilder.desc(from.get(TCaseIcd_.icdcIsHdxFl)),
                criteriaBuilder.asc(from.get(TCaseIcd_.icdcCode)),
                criteriaBuilder.asc(from.get(TCaseIcd_.creationDate)));

        TypedQuery<TCaseIcd> criteriaQuery = getEntityManager().createQuery(query);
        criteriaQuery = addEntityGraph(criteriaQuery);
        return criteriaQuery.getResultList();
    }

    public TCaseIcd findMainDiagnosisIcd(Long caseDetailId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseIcd> query = criteriaBuilder.createQuery(TCaseIcd.class);
//        Query nq = getEntityManager().createNativeQuery("");

        Root<TCaseIcd> from = query.from(TCaseIcd.class);
//        query.distinct(true);

//        from.fetch(TCaseIcd_.refIcds, JoinType.LEFT);
        Join<TCaseIcd, TCaseDepartment> depJoin = from.join(TCaseIcd_.caseDepartment);

        query.where(criteriaBuilder.equal(depJoin.get(TCaseDepartment_.caseDetails), caseDetailId), criteriaBuilder.equal(from.get(TCaseIcd_.icdcIsHdxFl), 1));
        TypedQuery<TCaseIcd> criteriaQuery = getEntityManager().createQuery(query);
        criteriaQuery = addEntityGraph(criteriaQuery);
        return getSingleResultOrNull(criteriaQuery);
    }

    private TypedQuery<TCaseIcd> addEntityGraph(TypedQuery<TCaseIcd> criteriaQuery) {

        EntityGraph<TCaseIcd> toFetch = getEntityManager().createEntityGraph(TCaseIcd.class);
        toFetch.addSubgraph(TCaseIcd_.refIcds);

        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
        return criteriaQuery;
    }

    public Number countForDetailsId(long id) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<TCaseIcd> from = cq.from(TCaseIcd.class);
        cq.select(qb.count(from));
        Join<TCaseIcd, TCaseDepartment> depJoin = from.join(TCaseIcd_.caseDepartment);

        cq.where(qb.equal(depJoin.get(TCaseDepartment_.caseDetails), id));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    /**
     * gets the count of all secondary diagnosis
     *
     * @param pDetailsId version details id
     * @return count of all secondary diagnosis
     */
    public Number countOfSecondaryDiagnosisForDetails(long pDetailsId) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<TCaseIcd> from = cq.from(TCaseIcd.class);
        cq.select(qb.count(from));
        Join<TCaseIcd, TCaseDepartment> depJoin = from.join(TCaseIcd_.caseDepartment);
        //from.get(TCaseIcd_.icdcIsHdxFl,false)
        cq.where(qb.equal(depJoin.get(TCaseDepartment_.caseDetails), pDetailsId), qb.equal(from.get(TCaseIcd_.icdcIsHdxFl), false));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    public List<TCaseIcd> getFirstItems(int count) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseIcd> query = criteriaBuilder.createQuery(TCaseIcd.class);
        Root<TCaseIcd> from = query.from(TCaseIcd.class);
        query.select(from);
        TypedQuery<TCaseIcd> criteriaQuery = getEntityManager().createQuery(query);

        criteriaQuery.setMaxResults(count);
        criteriaQuery = addEntityGraph(criteriaQuery);
        return criteriaQuery.getResultList();
    }

    public List<IcdOverviewDTO> getAllIcdCodesForVersions(List<Long> pVersions, GDRGModel pModel) {
        return computeOverviewList(pVersions, pModel);
    }

    public List<IcdOverviewDTO> computeOverviewList(List<Long> pVersions, GDRGModel pModel) {
        HashMap<Long, List<TCaseIcd>> versionIcdMap = new HashMap<>();
        long start = System.currentTimeMillis();
        for (Long version : pVersions) {
            versionIcdMap.put(version, findListOfIcd_nativ(version, pModel, true));
        }
        List<IcdOverviewDTO> overview = new IcdDtoHelper().computeDtoList(versionIcdMap);
        overview.sort(Comparator.comparing(IcdOverviewDTO::hasHbxFl).reversed().thenComparing(IcdOverviewDTO::getIcdCode));
        LOG.log(Level.FINE, "get list of " + overview.size() + " icd entries " + (System.currentTimeMillis() - start) + " ms");
        return overview;
    }

    public List<TCaseIcd> findListOfIcd_nativ(Long versionId, GDRGModel pModel, boolean pDbSort) {
//        String sql = "Select icd.* FROM T_CASE_ICD icd"
//                + " JOIN T_CASE_DEPARTMENT dep ON dep.id = icd.T_CASE_DEPARTMENT_ID"  
//                + " WHERE dep.HOSD_ID = "+ versionId                            //,icd.MAIN_DIAG_CASE_FL DESC
//                + " ORDER BY icd.CREATION_DATE NULLS FIRST, icd.CREATION_DATE ASC,icd.ICDC_CODE,icd.MAIN_DIAG_CASE_FL_FL DESC,icd.id ";//ORDER BMAIN_DIAG_CASE_FLHDX_FL DESC,icd.CREATION_DATE DESC,icd.ICDC_CODE";
        long start = System.currentTimeMillis();
        String sql = "";
        if (!pModel.equals(GDRGModel.AUTOMATIC)) {
            sql = "Select icd.* FROM T_CASE_ICD icd \n"
                    + "JOIN T_CASE_DEPARTMENT dep ON dep.id = icd.T_CASE_DEPARTMENT_ID \n"
                    + "LEFT JOIN (\n"
                    + "Select distinct gr.T_CASE_ICD_ID, gr.ICD_RES_CCL FROM T_CASE_ICD_GROUPED gr\n"
                    + "JOIN T_GROUPING_RESULTS res ON res.ID = gr.T_GROUPING_RESULTS_ID\n"
                    + "JOIN T_CASE_ICD icds ON icds.ID = res.T_CASE_ICD_ID\n"
                    + "WHERE icds.MAIN_DIAG_CASE_FL = 1 AND res.MODEL_ID_EN = :grouper "
                    + " and res.GRPRES_IS_AUTO_FL = 0 "
                    +//pModel.name()+"'\n" +
                    ") icdgr ON icdgr.T_CASE_ICD_ID = icd.id\n"
                    + //                "LEFT OUTER JOIN T_CASE_ICD icdref ON icdref.id = icd.ICDC_REF_ID \n"+
                    "WHERE dep.T_CASE_DETAILS_ID = :version ";
            //versionId +
            if (pDbSort) {
                //FIX CPX-532, handle null values in date 
                sql = sql + " ORDER BY CASE WHEN icd.CREATION_DATE IS NULL THEN 0 ELSE 1 END , \n"
                        + "icd.CREATION_DATE ASC,icd.ICDC_CODE,icd.MAIN_DIAG_CASE_FL DESC,icdgr.ICD_RES_CCL DESC,icd.id";
            }
//                " ORDER BY icd.CREATION_DATE NULLS FIRST, icd.CREATION_DATE ASC,icd.ICDC_CODE,icd.MAIN_DIAG_CASE_FL DESC,icdgr.ICD_RES_CCL DESC,icd.id";
        } else {
            sql = "Select icd.* FROM T_CASE_ICD icd \n"
                    + "JOIN T_CASE_DEPARTMENT dep ON dep.id = icd.T_CASE_DEPARTMENT_ID \n"
                    + "LEFT JOIN (\n"
                    + "Select distinct gr.T_CASE_ICD_ID, gr.ICD_RES_CCL FROM T_CASE_ICD_GROUPED gr\n"
                    + "JOIN T_GROUPING_RESULTS res ON res.ID = gr.T_GROUPING_RESULTS_ID\n"
                    + "JOIN T_CASE_ICD icds ON icds.ID = res.T_CASE_ICD_ID\n"
                    + "WHERE icds.MAIN_DIAG_CASE_FL = 1 AND res.GRPRES_IS_AUTO_FL = 1 \n"
                    + ") icdgr ON icdgr.T_CASE_ICD_ID = icd.id\n"
                    + //                "LEFT JOIN FETCH T_CASE_ICD refIcd ON icd.id=refIcd.ICDC_REF_ID "+
                    "WHERE dep.T_CASE_DETAILS_ID = :version ";
            if (pDbSort) {
                //FIX CPX-532, handle null values in date 
                sql = sql + " ORDER BY CASE WHEN icd.CREATION_DATE IS NULL THEN 0 ELSE 1 END , \n"
                        + "icd.CREATION_DATE ASC,icd.ICDC_CODE,icd.MAIN_DIAG_CASE_FL DESC,icdgr.ICD_RES_CCL DESC,icd.id";
            }
//                    + //versionId +
//                    " ORDER BY CASE WHEN icd.CREATION_DATE IS NULL THEN 0 ELSE 1 END , \n"
//                    + " icd.CREATION_DATE ASC,icd.ICDC_CODE,icd.MAIN_DIAG_CASE_FL DESC,icdgr.ICD_RES_CCL DESC,icd.id";
//                " ORDER BY icd.CREATION_DATE NULLS FIRST, icd.CREATION_DATE ASC,icd.ICDC_CODE,icd.MAIN_DIAG_CASE_FL DESC,icdgr.ICD_RES_CCL DESC,icd.id";            
        }
        List<TCaseIcd> list;
        Query query = getEntityManager().createNativeQuery(sql, TCaseIcd.class);

        query.setParameter("version", "" + versionId);

        if (!pModel.equals(GDRGModel.AUTOMATIC)) {
            query.setParameter("grouper", pModel.name());
        }
        list = query.getResultList();
        long timeIcd = System.currentTimeMillis() - start;
        LOG.log(Level.INFO, "fetch icds in " + timeIcd + " ms for version " + versionId);
        initialiseReferences(list);
//        start = System.currentTimeMillis();
        //better with some jpql query and fetch join?!
//        Iterator<TCaseIcd> it = list.iterator();
//        while (it.hasNext()) {
//            TCaseIcd next = it.next();
//            long start1 = System.currentTimeMillis();
//             Hibernate.initialize(next.getCaseDepartment());
//            if (next.getCaseWard() != null) {
//                Hibernate.initialize(next.getCaseWard());
//            }
//            LOG.log(Level.FINE, "fetch ward/Dep for icd in " + (System.currentTimeMillis() - start1) + " for version " + versionId);
//            Hibernate.initialize(next.getRefIcds());
//        }
//        LOG.log(Level.FINE, "fetch ref icds in " + (System.currentTimeMillis() - timeIcd) + " for version " + versionId);
        return list;
    }

    public List<TCaseIcd> findListOfIcd_nativ2(List<Long> versionIds) {
        String sql = "Select icd.* FROM T_CASE_ICD icd "
                + "JOIN T_CASE_DEPARTMENT dep ON dep.id = icd.T_CASE_DEPARTMENT_ID "
                + "JOIN T_CASE_ICD_GROUPED icdgr ON icdgr.T_CASE_ICD_ID = icd.id"
                + "WHERE dep.T_CASE_DETAILS_ID IN( " + String.join(",", versionIds.stream().map(Object::toString)
                        .collect(Collectors.joining(", "))) + ") "
                + "ORDER BY icd.MAIN_DIAG_CASE_FL DESC,icdgr.ICD_RES_CCL, icd.ICDC_CODE ASC";

        Query query = getEntityManager().createNativeQuery(sql, TCaseIcd.class);
        List<TCaseIcd> list = query.getResultList();
        initialiseReferences(list);
        return list;
    }

    public List<TCaseIcd> getIcdsOfDept(long deptId) {
        //List<TCaseIcd> icds = null;
        TypedQuery<TCaseIcd> query = getEntityManager().createQuery(String.format("from TCaseIcd tc where tc.caseDepartment = %s", deptId), TCaseIcd.class);
        List<TCaseIcd> list = query.getResultList();
        initialiseReferences(list);
        return(list);
    }

    public List<TCaseIcd> getIcdsOfWard(long wardId) {
        //List<TCaseIcd> icds = null;
        TypedQuery<TCaseIcd> query = getEntityManager().createQuery(String.format("from TCaseIcd tc where tc.caseWard = %s", wardId), TCaseIcd.class);
        List<TCaseIcd> list = query.getResultList();
        initialiseReferences(list);
        return list;
    }
    
    private void initialiseReferences(List<TCaseIcd> list){
        Iterator<TCaseIcd> it = list.iterator();
        while (it.hasNext()) {
            TCaseIcd next = it.next();

             Hibernate.initialize(next.getCaseDepartment());
            if (next.getCaseWard() != null) {
                Hibernate.initialize(next.getCaseWard());
                Set<TCaseIcd> depIcds = next.getCaseDepartment().getCaseIcds();
                if(depIcds != null){
                    for(TCaseIcd icd: depIcds){
                         Hibernate.initialize(icd);
                    }
                }
            }
            if(next.getGroupingResultses() != null){
                Set<TGroupingResults> grRess = next.getGroupingResultses();
                Hibernate.initialize(next.getGroupingResultses());
                for(TGroupingResults grRes: grRess){
                    Hibernate.initialize(grRes);
                }
                
            }

            Hibernate.initialize(next.getRefIcds());
        }
        
    }

}
