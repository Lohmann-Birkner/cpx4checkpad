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
import de.lb.cpx.grouper.model.dto.OpsOverviewDTO;
import de.lb.cpx.grouper.model.util.OpsDtoHelper;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDepartment_;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseOps_;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
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
public class TCaseOpsDao extends AbstractCpxDao<TCaseOps> {

    private static final Logger LOG = Logger.getLogger(TCaseOpsDao.class.getName());

    /**
     * Creates a new instance.
     */
    public TCaseOpsDao() {
        super(TCaseOps.class);
    }

    public List<TCaseOps> findListOfOps(Long caseDetailId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseOps> query = criteriaBuilder.createQuery(TCaseOps.class);
        Root<TCaseOps> from = query.from(TCaseOps.class);
        Join<TCaseOps, TCaseDepartment> depJoin = from.join(TCaseOps_.caseDepartment);
        query.where(criteriaBuilder.equal(depJoin.get(TCaseDepartment_.caseDetails), caseDetailId));
        query.orderBy(criteriaBuilder.asc(from.get(TCaseOps_.id)));
        TypedQuery<TCaseOps> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

    public Number countForDetailsId(long id) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<TCaseOps> from = cq.from(TCaseOps.class);
        cq.select(qb.count(from));
        Join<TCaseOps, TCaseDepartment> depJoin = from.join(TCaseOps_.caseDepartment);

        cq.where(qb.equal(depJoin.get(TCaseDepartment_.caseDetails), id));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    public List<TCaseOps> getFirstItems(int count) {

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseOps> query = criteriaBuilder.createQuery(TCaseOps.class);
        Root<TCaseOps> from = query.from(TCaseOps.class);
        query.select(from);
        TypedQuery<TCaseOps> criteriaQuery = getEntityManager().createQuery(query);
        criteriaQuery.setMaxResults(count);
//        criteriaQuery = addEntityGraph(criteriaQuery);
        return criteriaQuery.getResultList();
    }

    public List<OpsOverviewDTO> getAllOpsCodesForVersions(Long pVersion1, Long pVersion2, Long pVersion3) {
        String sqlQuery
                = String.format("SELECT \n"
                        + "  MAX_OPS_IDS, \n"
                        + "  OPSC_CODE, \n"
                        + "  MAX_HOSD_ID, \n"
                        + "  HOSD_IDS \n"
                        + " FROM ( \n"
                        + "  SELECT \n"
                        + "    MAX(MAX_OPS_ID) MAX_OPS_IDS, \n"
                        + "	OPSC_CODE, \n"
                        + "	MAX(T_CASE_DETAILS_ID) MAX_HOSD_ID, \n"
                        + "	LISTAGG(T_CASE_DETAILS_ID, ',') WITHIN GROUP (ORDER BY T_CASE_DETAILS_ID) AS HOSD_IDS -- LISTAGG is oracle specific syntax!\n"
                        + "  FROM (\n"
                        + "    SELECT \n"
                        + "	  MAX(T_CASE_OPS.ID) MAX_OPS_ID, \n"
                        + "	  OPSC_CODE, \n"
                        + "	  T_CASE_DETAILS_ID\n"
                        + "    FROM T_CASE_OPS\n"
                        + "    INNER JOIN T_CASE_DEPARTMENT ON T_CASE_DEPARTMENT.ID = T_CASE_OPS.T_CASE_DEPARTMENT_ID\n"
                        + "    WHERE T_CASE_DEPARTMENT.T_CASE_DETAILS_ID IN ("
                        + "%s"
                        + " %s"
                        + " %s"
                        + //                + "?, ?"
                        ") -- <- Replace this!\n"
                        + "    GROUP BY OPSC_CODE, T_CASE_DETAILS_ID\n"
                        + "  ) TMP \n"
                        + "  GROUP BY OPSC_CODE"
                        + "\n"
                        + ") TMP2 \n"
                        + "ORDER BY OPSC_CODE", String.valueOf(pVersion1), (pVersion2 != null ? " ," + String.valueOf(pVersion2) : ""), (pVersion2 != null ? " ," + String.valueOf(pVersion2) : ""));

        Query query = getEntityManager().createNativeQuery(sqlQuery);
//        query.setParameter(1, pVersion1);
//        query.setParameter(2, pVersion2);
        List<Object[]> results = query.getResultList();
        Iterator<Object[]> it = results.iterator();
        List<OpsOverviewDTO> opsCode = new ArrayList<>();
        while (it.hasNext()) {
            Object[] obj = it.next();
            OpsOverviewDTO dto = new OpsOverviewDTO((String) obj[1], (String) obj[3]);
            opsCode.add(dto);
//            icdCodes.add((String)obj);
        }
        return opsCode;
    }

    public List<OpsOverviewDTO> getAllOpsCodesForVersions(List<Long> pVersions) {
        return computeOverviewList(pVersions);
//        switch(pVersions.size()){
//            case 1:
//              return getAllOpsCodesForVersions(pVersions.get(0),null,null); 
//            case 2:
//              return getAllOpsCodesForVersions(pVersions.get(0), pVersions.get(1),null);
//            case 3:
//              return getAllOpsCodesForVersions(pVersions.get(0), pVersions.get(1),pVersions.get(2));
//            default:
//               return null;
//        }
    }

    List<OpsOverviewDTO> computeOverviewList(List<Long> pVersions) {
        HashMap<Long, List<TCaseOps>> versionOpsMap = new HashMap<>();
//        List<OpsOverviewDTO> overview = new ArrayList<>(); 
        long start = System.currentTimeMillis();
        for (Long version : pVersions) {
            versionOpsMap.put(version, findListOfOps_nativ(version));
        }
//        int index = 0;
//        do{
//
//            List<OpsOverviewDTO> tmp = new ArrayList<>();
//            versionLoop:for(long version : versionOpsMap.keySet()){
//                if(versionOpsMap.get(version).size()<=index){
//                    continue;
//                }
//                TCaseOps tmpOps = versionOpsMap.get(version).get(index);
//                Hibernate.initialize(tmpOps.getCaseDepartment());
//                for(OpsOverviewDTO dto : tmp){
//                    if(dto.getOpsCode().equals(tmpOps.getOpscCode())){
//                        dto.addOpsForVersion(String.valueOf(version), tmpOps);
//                        dto.getOccurance().add(version);
//                        continue versionLoop;
//                    }
//                }
////                versionOpsMap.get(version).add(index,new TCaseOps());
//                OpsOverviewDTO newDto = new OpsOverviewDTO(tmpOps.getOpscCode(), "");
//                newDto.addOpsForVersion(String.valueOf(version), tmpOps);
//                newDto.getOccurance().add(version);
//                tmp.add(newDto);
//            }
//            overview.addAll(tmp);
//            index++;
//            
//        }while (!checkSize(index,versionOpsMap.values()));
//        
//        overview.sort(Comparator.comparing(OpsOverviewDTO::getOpsCode));
        List<OpsOverviewDTO> overview = new OpsDtoHelper().computeDtoList2(versionOpsMap);
        LOG.log(Level.FINE, "get list of " + overview.size() + " ops entries in " + (System.currentTimeMillis() - start) + " ms");
        return overview;
    }

    public List<TCaseOps> findListOfOps_nativ(Long versionId) {
        //2017-12-05 DNi: It would be better to use a prepared statement here!
        String sql = String.format("Select ops.* FROM T_CASE_OPS ops"
                + " JOIN T_CASE_DEPARTMENT dep ON dep.id = ops.T_CASE_DEPARTMENT_ID"
                //                + " JOIN T_CASE_OPS_GROUPED opsgr ON opsgr.T_CASE_OPS_ID = ops.id"                                                      //ops.OPSC_DATUM
                + " WHERE dep.T_CASE_DETAILS_ID = %s"
                //FIX CPX-532, handle null values 
                + " ORDER BY CASE WHEN ops.CREATION_DATE IS NULL THEN 0 ELSE 1 END, "
                + "ops.CREATION_DATE ASC,ops.OPSC_CODE,ops.OPSC_DATUM ASC", versionId);
//                        " ORDER BY CASE WHEN icd.CREATION_DATE IS NULL THEN 0 ELSE 1 END , \n" +
//                "icd.CREATION_DATE ASC,icd.ICDC_CODE,icd.MAIN_DIAG_CASE_FL DESC,icdgr.ICD_RES_CCL DESC,icd.id";
        Query query = getEntityManager().createNativeQuery(sql, TCaseOps.class);
        List<TCaseOps> list = query.getResultList();
        long start = System.currentTimeMillis();
        Iterator<TCaseOps> it = list.iterator();
        while (it.hasNext()) {
            TCaseOps next = it.next();
            long start1 = System.currentTimeMillis();
            Hibernate.initialize(next.getCaseDepartment());
            if (next.getCaseWard() != null) {
                Hibernate.initialize(next.getCaseWard());
            }
            LOG.log(Level.FINE, "fetch ward/Dep for Ops in " + (System.currentTimeMillis() - start1) + " ms for version " + versionId);
        }
        LOG.log(Level.FINE, "fetch additional ops data in " + (System.currentTimeMillis() - start) + " ms for version " + versionId);
        return list;
    }

    public List<TCaseOps> findListOfOps_nativ2(Long versionId, GDRGModel pModel) {
        String sql;
        if (!pModel.equals(GDRGModel.AUTOMATIC)) {
            sql = "Select ops.* FROM T_CASE_OPS ops \n"
                    + "JOIN T_CASE_DEPARTMENT dep ON dep.id = ops.T_CASE_DEPARTMENT_ID \n"
                    + "LEFT JOIN (\n"
                    + "Select distinct gr.ID,gr.T_CASE_OPS_ID FROM T_CASE_OPS_GROUPED gr \n"
                    + "JOIN T_GROUPING_RESULTS res ON res.ID = gr.T_GROUPING_RESULTS_ID\n"
                    + "JOIN T_CASE_OPS opses ON opses.ID = res.T_CASE_ICD_ID\n"
                    + "WHERE res.MODEL_ID_EN = :grouper "
                    +//pModel.name()+"'\n" +
                    ") opsgr ON opsgr.T_CASE_OPS_ID = ops.id\n"
                    + //                "LEFT OUTER JOIN T_CASE_ICD icdref ON icdref.id = icd.ICDC_REF_ID \n"+
                    "WHERE dep.T_CASE_DETAILS_ID = :version "
                    + //versionId +
                    //FIX CPX-532, handle null values in date 
                    " ORDER BY CASE WHEN ops.CREATION_DATE IS NULL THEN 0 ELSE 1 END, "
                    + "ops.CREATION_DATE ASC,ops.OPSC_CODE,opsgr.ID";

//                sql = "Select ops.* FROM T_CASE_OPS ops"
//                + " JOIN T_CASE_DEPARTMENT dep ON dep.id = ops.T_CASE_DEPARTMENT_ID"
////                + " JOIN T_CASE_OPS_GROUPED opsgr ON opsgr.T_CASE_OPS_ID = ops.id"                                                      //ops.OPSC_DATUM
//                + " WHERE dep.T_CASE_DETAILS_ID = "+ versionId 
//                //FIX CPX-532, handle null values 
//                +" ORDER BY CASE WHEN ops.CREATION_DATE IS NULL THEN 0 ELSE 1 END, "
//                + "ops.CREATION_DATE ASC,ops.OPSC_CODE,ops.OPSC_DATUM ASC";
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
                    "WHERE dep.T_CASE_DETAILS_ID = :version "
                    + //versionId +
                    " ORDER BY CASE WHEN icd.CREATION_DATE IS NULL THEN 0 ELSE 1 END , \n"
                    + " icd.CREATION_DATE ASC,icd.ICDC_CODE,icd.MAIN_DIAG_CASE_FL DESC,icdgr.ICD_RES_CCL DESC,icd.id";
//                " ORDER BY icd.CREATION_DATE NULLS FIRST, icd.CREATION_DATE ASC,icd.ICDC_CODE,icd.MAIN_DIAG_CASE_FL DESC,icdgr.ICD_RES_CCL DESC,icd.id";            
        }
        List<TCaseOps> list = null;
        Query query = getEntityManager().createNativeQuery(sql, TCaseOps.class);
        //        
        //        EntityGraph<TCaseIcd> toFetch = getEntityManager().createEntityGraph(TCaseIcd.class);
        //        toFetch.addSubgraph(TCaseIcd_.refIcds);
        //        query.setHint("javax.persistence.loadgraph", toFetch);
        //        
        query.setParameter("version", "" + versionId);

        if (!pModel.equals(GDRGModel.AUTOMATIC)) {
            query.setParameter("grouper", pModel.name());
        }
        list = query.getResultList();

        return list;
    }
//    //Helper-Methode compute if an item has still items that are beyond the current index
//    private boolean checkSize(int index, Collection<List<TCaseOps>> values) {
//        List<Boolean> hasMore = new ArrayList<>();
//        for(List<TCaseOps> val : values){
//            hasMore.add(val.size()<=index);
//
//        }
//        return hasMore.stream().allMatch((Boolean t) -> t.equals(Boolean.TRUE));
//    }

    public List<TCaseOps> getOpsOfDept(long deptId) {
        //List<TCaseOps> opss = null;
        TypedQuery<TCaseOps> query = getEntityManager().createQuery(String.format("from TCaseOps tc where tc.caseDepartment = %s", deptId), TCaseOps.class);
        return query.getResultList();
    }

    public List<TCaseOps> getOpsOfWard(long wardId) {
        //List<TCaseOps> opss = null;
        TypedQuery<TCaseOps> query = getEntityManager().createQuery(String.format("from TCaseOps tc where tc.caseWard = %s", wardId), TCaseOps.class);
        return query.getResultList();
    }
}
