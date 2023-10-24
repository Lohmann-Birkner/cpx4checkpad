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

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseDetails_;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.auth.CpxUser;
import de.lb.cpx.server.commons.enums.EntityGraphType;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.hibernate.Hibernate;

/**
 * TCaseDetailsDao, grands single access to TCaseDetails Entities for update and
 * saving
 *
 * @author wilde
 */
@Stateless
public class TCaseDetailsDao extends AbstractCpxDao<TCaseDetails> {

    private static final Logger LOG = Logger.getLogger(TCaseDetailsDao.class.getName());
//    
//    @EJB
//    TCaseDao caseDao;

    /**
     * Creates a new instance.
     */
    public TCaseDetailsDao() {
        super(TCaseDetails.class);
    }

    /**
     * get Comment 'MetaData' with current User, creation Date (ISO Format) and
     * Details of the ParentVersion
     *
     * @param details TCaseDetails Entity to create MetaData for
     * @return String of the 'Metadata' sperated by ; e.g. mustermann;01.01.2000
     * 14:12:22;KIS-Version 1
     */
    public String getCaseDetailsMetaData(TCaseDetails details) {

        boolean isLocal;
        int versionCount;
//        if(details.getCaseDetailsByCsdParentId() != null){
//            isLocal = details.getCaseDetailsByCsdParentId().getCsdIsLocalFl();
//            versionCount = details.getCaseDetailsByCsdParentId().getCsdVersion();
//        }else{
//            LOG.warning("no parent detected");
        isLocal = details.getCsdIsLocalFl();
        versionCount = details.getCsdVersion();
//        }
        String parent = "";
        if (isLocal) {
            parent = parent.concat("CP");
        } else {
            parent = parent.concat("KIS");
        }
        parent = parent.concat("-Version " + versionCount);

        String user = "";
        try {
            CpxUser cpxUser = ClientManager.getCurrentCpxUser();
            user = cpxUser!=null?cpxUser.getUserName():"no user found";
        } catch (IllegalStateException stateExc) {
            LOG.log(Level.WARNING, "Can't detect user that want to create a new Version, reason " + stateExc.getMessage(), stateExc);
        }
        String date = Lang.toDateTime(new Date());
        return user + " ; " + date + " ; " + parent + " //// ";
    }

    /**
     * Clone and sets new CsdVersion and comment as meta data to the new Version
     * Object
     *
     * @param detailsToClone parent Version to clone from
     * @return new Version with incremented csdVersion and new Comment, contains
     * meta data, and is Flagged as LocalVersion
     */
    public TCaseDetails createNewVersionFrom(TCaseDetails detailsToClone) {
        try {
            TCaseDetails clone = detailsToClone.cloneWithoutIds(ClientManager.getCurrentCpxUserId());
            //clone.setCsdIsLocalFl(true);
            clone.setCsdVersion(getMaxVersionCountLocalVersion(detailsToClone.getHospitalCase()).intValue() + 1);
            clone.setComment(getCaseDetailsMetaData(detailsToClone));
//            clone.setCaseDetailsByCsdParentId(detailsToClone);
//            clone.setHospitalCase(detailsToClone.getHospitalCase());
            if (!detailsToClone.getCsdIsLocalFl()) {
                clone.setCaseDetailsByCsdExternId(detailsToClone);
            }
            return clone;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(TCaseDetailsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Get the current Max Value of CsdVersion Column for TCaseDetails Database
     * ID
     *
     * @param hCase TCase Entity for the CaseDetails
     * @return max Value of CsdVersion Column
     */
    public Number getMaxVersionCount(TCase hCase) {

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Number> query = criteriaBuilder.createQuery(Number.class);

        Root<TCaseDetails> from = query.from(TCaseDetails.class);

        query.select(criteriaBuilder.max(from.get(TCaseDetails_.csdVersion)));

        query.where(criteriaBuilder.equal(from.get(TCaseDetails_.hospitalCase), hCase));

        TypedQuery<Number> typedQuery = getEntityManager().createQuery(query);

        return typedQuery.getSingleResult();
    }

    /**
     * Get the current Max Value of CsdVersion Column for TCaseDetails Database
     * ID
     *
     * @param hCase TCase Entity for the CaseDetails
     * @return max Value of CsdVersion Column
     */
    public Number getMaxVersionCountLocalVersion(TCase hCase) {

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Number> query = criteriaBuilder.createQuery(Number.class);

        Root<TCaseDetails> from = query.from(TCaseDetails.class);

        query.select(criteriaBuilder.max(from.get(TCaseDetails_.csdVersion)));

        query.where(criteriaBuilder.equal(from.get(TCaseDetails_.hospitalCase), hCase), criteriaBuilder.equal(from.get(TCaseDetails_.csdIsLocalFl), Boolean.TRUE));

        TypedQuery<Number> typedQuery = getEntityManager().createQuery(query);

        return typedQuery.getSingleResult();
    }

    /**
     * get List of Successors of the actual Version
     *
     * @param detailsToCheck version to detect successors
     * @return Lsit of Successor Entities
     */
    public List<TCaseDetails> getSuccessors(TCaseDetails detailsToCheck) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseDetails> query = criteriaBuilder.createQuery(TCaseDetails.class);

        Root<TCaseDetails> from = query.from(TCaseDetails.class);

        query.where(criteriaBuilder.equal(from.get(TCaseDetails_.hospitalCase), detailsToCheck.getHospitalCase()),
                criteriaBuilder.equal(from.get(TCaseDetails_.csdIsLocalFl), detailsToCheck.getCsdIsLocalFl()),
                criteriaBuilder.equal(from.get(TCaseDetails_.caseDetailsByCsdParentId), detailsToCheck)
        );

        TypedQuery<TCaseDetails> typedQuery = getEntityManager().createQuery(query);

        return typedQuery.getResultList();
    }

    /**
     * Get TCaseDetails with the current Max Value of CsdVersion Column for
     * TCaseDetails Database ID
     *
     * @param hCase TCase Entity for the CaseDetails
     * @return CaseDetails with max Value of CsdVersion Column
     */
    public TCaseDetails getDetailsWithMaxVersion(TCase hCase) {

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseDetails> query = criteriaBuilder.createQuery(TCaseDetails.class);

        Root<TCaseDetails> from = query.from(TCaseDetails.class);
        Subquery<Integer> sq = query.subquery(Integer.class);
        Root<TCaseDetails> sf = sq.from(TCaseDetails.class);

        sq.where(criteriaBuilder.equal(sf.get(TCaseDetails_.hospitalCase), hCase));

        Subquery<Integer> select = sq.select(criteriaBuilder.max(sf.get(TCaseDetails_.csdVersion)));
//        query.select(criteriaBuilder.max(from.get(TCaseDetails_.csdVersion)));

        query.where(criteriaBuilder.equal(from.get(TCaseDetails_.hospitalCase), hCase),
                criteriaBuilder.equal(from.get(TCaseDetails_.csdIsLocalFl), true),
                criteriaBuilder.equal(from.get(TCaseDetails_.csdVersion), select.getSelection()));

        TypedQuery<TCaseDetails> typedQuery = getEntityManager().createQuery(query);
        //return typedQuery.getSingleResult();
        return getSingleResultOrNull(typedQuery);
    }

//    public void deleteVersion(TCaseDetails version, TCase hCase) throws Exception {
//        if(!version.getCsdIsLocalFl()){
//            throw new Exception("Can not delete HIS-Version!");
//        }
//
//        if(hCase.getCaseDetails().stream().filter(p->p.getCsdIsLocalFl()).count()<1){
//            throw new Exception("Can not delete. At least 1 CP-Version must remain");    
//        }
//        TCaseDetails parentVersion = version.getCaseDetailsByCsdParentId();
//        version.setHospitalCase(null);
////        List<TCaseDetails> all = findAll();
////        LOG.warning("size of all before delete " + all.size());
////        removeFromCase(hCase,version);
////        caseDao.flush();
//        remove(version);
//        
//        flush();
//
////        List<TCaseDetails> allAfter = findAll();
////        LOG.warning("size of all after delete " + allAfter.size());
//        
////        if(all.size() == allAfter.size()){
////            throw new Exception("nothing was deleted");
////        }
//        if(parentVersion == null || !parentVersion.getCsdIsLocalFl()){
//            TCaseDetails newActualDetails = getDetailsWithMaxVersion(hCase);
//            hCase.setCurrentLocal(newActualDetails);
//        }else{
//            hCase.setCurrentLocal(parentVersion);
//        }
//    }
//    public void deleteVersion(TCaseDetails version) throws Exception {
//        deleteVersion(version, version.getHospitalCase());
//    }
    public TCaseDetails deleteVersion(TCaseDetails version) {
        long start = System.currentTimeMillis();
        if (version == null) {
            //throw new CpxIllegalArgumentException("Passed TCaseDetails object is null!");
            return null;
        }
        if (!version.getCsdIsLocalFl()) {
            throw new IllegalArgumentException("It is not allowed to delete HIS-Version! You can only delete CP-Versions!");
        }

        version = merge(version);
        EntityManager em = getEntityManager();
        //em.detach(version);
        TCase cs = version.getHospitalCase();

        if (cs.getCaseDetails().stream().filter(p -> p.getCsdIsLocalFl()).count() < 1) {
            throw new IllegalArgumentException("Can not delete. At least 1 CP-Version must remain");
        }
        TCaseDetails parentVersion = version.getCaseDetailsByCsdParentId();
        LOG.info("time to get parent and do setup for delete " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        flush();
        for (TCaseDepartment dep : version.getCaseDepartments()) {
            if (dep == null) {
                continue;
            }
            //2017-12-05 DNi: It would be better to use a prepared statement here!
            Query query = em.createNativeQuery(String.format("UPDATE T_CASE_ICD SET T_CASE_ICD_ID = NULL WHERE T_CASE_ICD_ID IS NOT NULL AND T_CASE_ICD.T_CASE_DEPARTMENT_ID = %s", dep.id));
            int updatedRows = query.executeUpdate();
            LOG.log(Level.INFO, "Removed self-references in T_CASE_ID for T_CASE_DEPARTMENT_ID = " + dep.id + ": " + updatedRows);
        }
        LOG.info("time to flush and clear departments " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        //Query query = em.createNativeQuery("DELETE FROM T_CASE_DETAILS WHERE ID = " + version.id);
        //int updatedRows = query.executeUpdate();
        //System.out.println("Deleted T_CASE_DETAILS with ID = " + version.id + ": " + (updatedRows >= 1?"true":"false"));
        /*
    Iterator<TCaseDetails> it = cs.getCaseDetails().iterator();
    while(it.hasNext()) {
      TCaseDetails csd = it.next();
      if (csd != null && csd.getId() == version.getId()) {
        it.remove();
      }
    }
         */
        //Query query = em.createNativeQuery("DELETE FROM T_CASE_DETAILS WHERE ID = " + version.id);
        //int updatedRows = query.executeUpdate();
        //System.out.println("Deleted T_CASE_DETAILS with ID = " + version.id + ": " + (updatedRows >= 1?"true":"false"));
        version.setHospitalCase(null);
        if(version.getCsdIsActualFl()){
        version.setCsdIsActualFl(false);
        version.getCaseBills().clear();
        version.getCaseDepartments().clear();
        version.getCaseFees().clear();
        version.getGroupingResultses().clear();
        cs.getCaseDetails().remove(version);
        }
        remove(version);
        flush();
//      deleteById(version.getId());
        LOG.info("time to remove versions " + (System.currentTimeMillis() - start));
        //em.detach(version);
        //flush();
//    Iterator<TCaseDetails> it = cs.getCaseDetails().iterator();
//    while(it.hasNext()) {
//      TCaseDetails csd = it.next();
//      if (csd != null && csd.getId() == version.getId()) {
//        it.remove();
//      }
//    }
//    Query query = em.createNativeQuery("DELETE FROM T_CASE_DETAILS WHERE ID = " + version.id);
//    int updatedRows = query.executeUpdate();
//    System.out.println("Deleted T_CASE_DETAILS with ID = " + version.id + ": " + (updatedRows >= 1?"true":"false"));
        //flush();
        //em.refresh(cs);
        long delete = System.currentTimeMillis();
        if (parentVersion == null || !parentVersion.getCsdIsLocalFl()) {
            TCaseDetails newActualDetails = getDetailsWithMaxVersion(cs);
            cs.setCurrentLocal(newActualDetails);
        } else {
            cs.setCurrentLocal(parentVersion);
        }
        LOG.info("done settings values after delete in " + (System.currentTimeMillis() - delete));
        delete = System.currentTimeMillis();
        flush();
//    em.refresh(cs);
        LOG.info("done refresh and flush after delete in " + (System.currentTimeMillis() - delete));
        return cs.getCurrentLocal();
    }

    /**
     * get the current case version depends if local or extern is required
     *
     * @param pHospitalCase case to fetch details for
     * @param pIsLocal indicator if extern or local version is required
     * @return current version
     */
    public TCaseDetails findCurrentDetails(TCase pHospitalCase, Boolean pIsLocal) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseDetails> query = criteriaBuilder.createQuery(TCaseDetails.class);

        Root<TCaseDetails> from = query.from(TCaseDetails.class);
        query.distinct(true);
        query.where(criteriaBuilder.equal(from.get(TCaseDetails_.hospitalCase), pHospitalCase),
                criteriaBuilder.equal(from.get(TCaseDetails_.csdIsLocalFl), pIsLocal),
                criteriaBuilder.equal(from.get(TCaseDetails_.csdIsActualFl), Boolean.TRUE)
        );

        TypedQuery<TCaseDetails> typedQuery = getEntityManager().createQuery(query);
        typedQuery.setHint(EntityGraphType.getLoadGraphType(), getEntityManager().getEntityGraph("fetchBatchDetailGraph"));
        return getSingleResultOrNull(typedQuery);
    }

    /**
     * get the current case version depends if local or extern is required
     *
     * @param pHospitalCaseId case to fetch details for
     * @param pIsLocal indicator if extern or local version is required
     * @return current version
     */
    public TCaseDetails findCurrentDetails(long pHospitalCaseId, Boolean pIsLocal) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseDetails> query = criteriaBuilder.createQuery(TCaseDetails.class);

        Root<TCaseDetails> from = query.from(TCaseDetails.class);
        query.distinct(true);
        query.where(criteriaBuilder.equal(from.get(TCaseDetails_.hospitalCase), pHospitalCaseId),
                criteriaBuilder.equal(from.get(TCaseDetails_.csdIsLocalFl), pIsLocal),
                criteriaBuilder.equal(from.get(TCaseDetails_.csdIsActualFl), Boolean.TRUE)
        );

        TypedQuery<TCaseDetails> typedQuery = getEntityManager().createQuery(query);
//        typedQuery.setHint(EntityGraphType.getLoadGraphType(), getEntityManager().getEntityGraph("fetchBatchDetailGraph"));
        TCaseDetails csd = getSingleResultOrNull(typedQuery);
        if (csd == null) {
            LOG.log(Level.FINEST, "no case details found for hospital case id {0} and local={1}", new Object[]{pHospitalCaseId, pIsLocal});
        } else {
            if (pIsLocal) {
                //to show discharging/admissioning case department in WmServiceOverviewSection
                Hibernate.initialize(csd.getCaseDepartments());
            }
        }
        return csd;
    }

    /**
     * @param pHospitalCase hospital case, to find version
     * @return details marked as current Extern
     */
    public TCaseDetails findCurrentExtern(TCase pHospitalCase) {
        return findCurrentDetails(pHospitalCase, Boolean.FALSE);
    }

    /**
     * @param pHospitalCase hospital case, to find version
     * @return details marked as current local
     */
    public TCaseDetails findCurrentLocal(TCase pHospitalCase) {
        return findCurrentDetails(pHospitalCase, Boolean.TRUE);
    }

    public TCaseDetails findVersion4ActiveRisk(Long currentCaseId, boolean pIsLocal, VersionRiskTypeEn pVersRiskType, boolean pActRisk) {
        final String sql = "SELECT csd.* FROM T_CASE_DETAILS csd INNER JOIN T_WM_RISK wr on wr.T_CASE_DETAILS_ID = csd.ID "
                + "WHERE csd.T_CASE_ID = " + currentCaseId 
                + " AND csd.LOCAL_FL = " + (pIsLocal?1:0)
                + " AND csd.CSD_VERS_RISK_TYPE_EN = '" + pVersRiskType.name()
                + (pActRisk?"' AND wr.RISK_ACTUAL_4_REG = 1":"'");
        final Query query = getSession().createNativeQuery(sql, TCaseDetails.class);
        
        List<TCaseDetails> result = query.getResultList();
        if(result != null && !result.isEmpty()){
            return result.get(0);
        }
        return null;
    }
    
    public List<TCaseDetails>findVersionsWithActiveRisk(Long currentCaseId, boolean pIsLocal, boolean pActRisk) {
        final String sql = "SELECT csd.* FROM T_CASE_DETAILS csd INNER JOIN T_WM_RISK wr on wr.T_CASE_DETAILS_ID = csd.ID "
                + "WHERE csd.T_CASE_ID = " + currentCaseId 
                + " AND csd.LOCAL_FL = " + (pIsLocal?1:0)
                + (pActRisk?" AND wr.RISK_ACTUAL_4_REG = 1":"");
        final Query query = getSession().createNativeQuery(sql, TCaseDetails.class);
        
        return query.getResultList();
    }

    public List<TCaseDetails> findCaseDetails(long pHospitalCaseId, boolean pLocal, boolean pIncludeStorno) {
         CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseDetails> query = criteriaBuilder.createQuery(TCaseDetails.class);

        Root<TCaseDetails> from = query.from(TCaseDetails.class);
        query.distinct(true);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(from.get(TCaseDetails_.hospitalCase), pHospitalCaseId));
        predicates.add(criteriaBuilder.equal(from.get(TCaseDetails_.csdIsLocalFl), pLocal));
        if(!pIncludeStorno){
            predicates.add(criteriaBuilder.isNull(from.get(TCaseDetails_.csdCancelReasonEn)));
        }
        query.where(predicates.toArray(new Predicate[predicates.size()]));

        TypedQuery<TCaseDetails> typedQuery = getEntityManager().createQuery(query);
//        typedQuery.setHint(EntityGraphType.getLoadGraphType(), getEntityManager().getEntityGraph("fetchBatchDetailGraph"));
        return typedQuery.getResultList();
    }

    public TCaseDetails findBilingVersionForCase(long currentCaseId, boolean pIsLocal) {
        return findVersion4ActiveRisk(currentCaseId, pIsLocal, VersionRiskTypeEn.BEFORE_BILLING, true);
    }

    public TCaseDetails findAsssessmentVersionForCase(long currentCaseId, boolean pIsLocal) {
        return findMaxVersionForVersionRiskType(currentCaseId,pIsLocal,VersionRiskTypeEn.CASE_FINALISATION);//findVersion4ActiveRisk(currentCaseId, pIsLocal, VersionRiskTypeEn.CASE_FINALISATION, true);
    }

    public TCaseDetails findMaxVersionForVersionRiskType(long hCase, boolean pIsLocal, VersionRiskTypeEn pType) {
        final String sql = "SELECT det.VERSION_NUMBER,det.* FROM T_CASE_DETAILS det "
                + "JOIN T_CASE hcase ON hcase.id = det.T_CASE_ID "
                + "WHERE det.T_CASE_ID = " + hCase + " AND det.CSD_VERS_RISK_TYPE_EN = '" + pType.name() + "' AND det.VERSION_NUMBER = ("
                + "SELECT MAX(det.VERSION_NUMBER) FROM T_CASE_DETAILS det "
                + "JOIN T_CASE hcase ON hcase.id = det.T_CASE_ID "
                + "WHERE det.T_CASE_ID = " + hCase + " AND det.CSD_VERS_RISK_TYPE_EN = '" + pType.name() + "' AND det.LOCAL_FL = " + (pIsLocal ? 1 : 0)
                + ")";
        final Query query = getSession().createNativeQuery(sql, TCaseDetails.class);

        return getSingleResultOrNull(query);
    }

    public List<TCaseDetails> findVersionsForRiskType(long pCaseId, boolean pIsLocal, VersionRiskTypeEn pType) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseDetails> query = criteriaBuilder.createQuery(TCaseDetails.class);

        Root<TCaseDetails> from = query.from(TCaseDetails.class);

        query.where(criteriaBuilder.equal(from.get(TCaseDetails_.hospitalCase), pCaseId),
                criteriaBuilder.equal(from.get(TCaseDetails_.csdIsLocalFl), pIsLocal),
                criteriaBuilder.equal(from.get(TCaseDetails_.CSD_VERS_RISK_TYPE_EN), pType)
        );
        query.orderBy(criteriaBuilder.desc(from.get(TCaseDetails_.CSD_VERSION)));
        TypedQuery<TCaseDetails> typedQuery = getEntityManager().createQuery(query);
        typedQuery = addCaseEntityGraph(typedQuery);
        return typedQuery.getResultList();
    }
    private TypedQuery<TCaseDetails> addCaseEntityGraph(TypedQuery<TCaseDetails> criteriaQuery) {
        EntityGraph<TCaseDetails> toFetch = getEntityManager().createEntityGraph(TCaseDetails.class);
        //toFetch.addAttributeNodes(TCase_.currentExtern,TCase_.currentLocal);
        //toFetch.addSubgraph(TCase_.patient).addAttributeNodes(TPatient_.patInsuranceActual);
        toFetch.addSubgraph(TCaseDetails_.HOSPITAL_CASE);
        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
        return criteriaQuery;
    }

    public List<TCaseDetails> findVersionsWithActiveRisk4Req(Long currentCaseId, VersionRiskTypeEn pVersionRiskType, boolean pIsLocal) {
        if(pVersionRiskType == null){
            return null;
        }
        final String sql = "SELECT det.* FROM T_CASE_DETAILS det "
                + "INNER JOIN T_WM_RISK risk ON risk.T_CASE_DETAILS_ID = det.ID "
                + "WHERE det.T_CASE_ID = " + currentCaseId 
                + " AND det.CSD_VERS_RISK_TYPE_EN = '" + pVersionRiskType.name() + "' "
                + " AND risk.RISK_ACTUAL_4_REG = 1 "
                + " AND det.LOCAL_FL = " +  (pIsLocal ? 1 : 0);
        final Query query = getSession().createNativeQuery(sql, TCaseDetails.class);

        return query.getResultList();
    }

    public TCaseDetails getActualBillingVersion(Long pHospitalId) {
        return findVersion4ActiveRisk(pHospitalId,true, VersionRiskTypeEn.BEFORE_BILLING, true);
    }
    
    public TCaseDetails getActualMdVersion(Long pHospitalId) {
        return findVersion4ActiveRisk(pHospitalId,true, VersionRiskTypeEn.AUDIT_MD, true);
    }
    
    public TCaseDetails getActualAuditVersion(Long pHospitalId) {
        return findVersion4ActiveRisk(pHospitalId,true, VersionRiskTypeEn.AUDIT_CASE_DIALOG, true);
    }

    public List<TCaseDetails> getActualDetails4PatientId(long pPatientId, boolean pIsLocal) {
       final String sql = "SELECT det.* FROM T_CASE_DETAILS det " +
            "INNER JOIN T_CASE cs on cs.ID = det.T_CASE_ID " +
            "INNER JOIN T_PATIENT pat on pat.ID = cs.T_PATIENT_ID" +
            " WHERE det.LOCAL_FL = " +  (pIsLocal ? 1 : 0)
               + " and det.ACTUAL_FL = 1 and pat.ID = " + pPatientId;
       
       final Query query = getSession().createNativeQuery(sql, TCaseDetails.class);

        return query.getResultList();
    }
}
