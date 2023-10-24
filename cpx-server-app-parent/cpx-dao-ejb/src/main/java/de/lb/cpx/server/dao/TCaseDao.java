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
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.grouper.model.util.GrouperConstant;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseBill;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseFee;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.model.TCase_;
import de.lb.cpx.model.TLab;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.TPatient_;
import de.lb.cpx.model.enums.CaseDetailsCancelReasonEn;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.server.commonDB.dao.CInsuranceCompanyDao;
import de.lb.cpx.server.commons.enums.EntityGraphType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

/**
 * Data access object for domain model class TCase. Initially generated at
 * 21.01.2016 17:14:39 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SuppressWarnings("unchecked")
public class TCaseDao extends AbstractCpxDao<TCase> {
    
    @Inject
    CInsuranceCompanyDao insuranceCompanyDao;
    //@Inject TCaseDetailsDao caseDetailsDao;
    //@Inject TCaseDepartmentDao caseDepartmentDao;
    //@Inject TCaseIcdDao caseIcdDao;

    //final private static Boolean NOTHING = true;
    private static final Logger LOG = Logger.getLogger(TCaseDao.class.getName());
//    public static final int GROUPER_MIN_YEAR = 2013;

    /**
     * Creates a new instance.
     *
     */
    public TCaseDao() {
        super(TCase.class);
    }

    /**
     * find TCase Entity with hosptailIdent and CaseNumber
     *
     * @param csHospitalIdent unique hospital identifier
     * @param csCaseNumber case number
     * @return tcase entity
     */
    public TCase findCaseForCasenumber(final String csHospitalIdent, final String csCaseNumber) {
        return findCaseForCasenumber(csHospitalIdent, csCaseNumber, true);
    }

    /**
     * find TCase Entity with hosptailIdent and CaseNumber
     *
     * @param csHospitalIdent unique hospital identifier
     * @param csCaseNumber case number
     * @param pLazyLoading lazy?
     * @return tcase entity
     */
    public TCase findCaseForCasenumber(final String csHospitalIdent, final String csCaseNumber, final boolean pLazyLoading) {
        final TypedQuery<TCase> query = getEntityManager().createQuery(
                "from TCase e where e.csCaseNumber = :csCaseNumber and e.csHospitalIdent = :csHospitalIdent",
                TCase.class);
        query.setParameter("csCaseNumber", csCaseNumber);
        query.setParameter("csHospitalIdent", csHospitalIdent);
        TCase cs = getSingleResultOrNull(query);

        if (cs != null) {
            if (!pLazyLoading) {
                cs.getCaseDetails();
            }
        }
        return cs;
    }

    /**
     * load all TCase Entities from Database
     *
     * @return list of all cases
     */
    public List<TCase> getAllCases() {
        final TypedQuery<TCase> query = getEntityManager().createQuery("from TCase", TCase.class);
        return query.getResultList();
    }

    /**
     * returns list of case ids - used for tests
     *
     * @return list of case ids
     */
    public List<Long> getAllCasesIds() {
        //final TypedQuery<Long> query = getEntityManager().createQuery("select id from TCase c", Long.class);
        //return query.getResultList();
        return getAllIds();
    }

    /**
     * load TCase Entities for List of Database-Ids
     *
     * @param caseIds List of Case Database Ids
     * @param pEager load eagerly
     * @return List of TCase Entities
     */
    public List<TCase> getCasesForIds(final List<Long> caseIds, final boolean pEager) {
        final TypedQuery<TCase> query
                = getEntityManager().createQuery("from TCase e where e.id in :caseIds", TCase.class);
        query.setParameter("caseIds", caseIds);
        List<TCase> result = query.getResultList();
        if (pEager) {
            for (TCase cs : result) {
                List<TCaseDetails> csds = new ArrayList<>();
                csds.add(cs.getCurrentExtern());
                csds.add(cs.getCurrentLocal());
                Hibernate.initialize(cs.getCurrentExtern());
                Hibernate.initialize(cs.getCurrentLocal());
                Hibernate.initialize(cs.getPatient());
                Hibernate.initialize(cs.getPatient().getCurrentDetail());
                for (TCaseDetails csd : csds) {
                    Hibernate.initialize(csd.getCaseDepartments());
                    for (TCaseDepartment dep : csd.getCaseDepartments()) {
                        Hibernate.initialize(dep.getCaseIcds());
                        Hibernate.initialize(dep.getCaseOpses());
                    }
                }
            }
        }
        return result;
    }

    /**
     * load TCase Entities for List of Database-Ids
     *
     * @param caseIds List of Case Database Ids
     * @return List of TCase Entities
     */
    public List<TCase> getCasesForIds(final List<Long> caseIds) {
        return getCasesForIds(caseIds, false);
    }

    /**
     * find SingleCase for CaseNumber and HospitalIdent, Combination of these
     * two Values should be unique
     *
     * @param csCaseNumber CaseNumber
     * @param hospitalIdent unique Identification of the Hospital
     * @return TCase Entity with Eager loaded Values
     * (patient,currentExtern,currentLocal,caseDetails,caseDetails.caseDetailsesForCsdParentId
     */
    public TCase findCaseForCaseNumberAndIdent(String csCaseNumber, String hospitalIdent) {
        LOG.log(Level.FINE, "Try to find case with hospital ident " + hospitalIdent + " and case number " + csCaseNumber + "...");
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TCase> query = criteriaBuilder.createQuery(TCase.class);

        Root<TCase> from = query.from(TCase.class);
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
        query.where(criteriaBuilder.like(from.get(TCase_.csCaseNumber), csCaseNumber), criteriaBuilder.equal(from.get(TCase_.csHospitalIdent), hospitalIdent));

        TypedQuery<TCase> criteriaQuery = getEntityManager().createQuery(query);
        criteriaQuery = addEntityGraph(criteriaQuery);

        TCase cse = getSingleResultOrNull(criteriaQuery);
        if (cse == null) {
            LOG.log(Level.WARNING, "No case found with hospital ident " + hospitalIdent + " and case number " + csCaseNumber + "!");
        } else {
            LOG.log(Level.FINER, "Found with hospital ident " + hospitalIdent + " and case number " + csCaseNumber + ": " + String.valueOf(cse));
        }
        Hibernate.initialize(cse);
//      cse.getCaseDetails().iterator();
        return cse;
    }

    public List<TCase> findPotentialCasesForCaseNumber(final String pCsCaseNumber) {
        final String caseNumber = pCsCaseNumber == null ? "" : pCsCaseNumber.trim();
        final List<TCase> result = new ArrayList<>();
        if (caseNumber.isEmpty()) {
            return result;
        }
        List<String> generatedCaseNumber = new ArrayList<>();
        //generatedCaseNumber.add(caseNumber);
        generatedCaseNumber.add("_" + caseNumber);
        generatedCaseNumber.add(caseNumber + "_");
        generatedCaseNumber.add("_" + caseNumber + "_");
        for (int i = 0; i < caseNumber.length(); i++) {
            String newCaseNumber = caseNumber.substring(0, i) + "_" + caseNumber.substring(i + 1);
            generatedCaseNumber.add(newCaseNumber);
        }

        LOG.log(Level.FINE, "Try to find case with case number " + caseNumber + "...");
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TCase> query = criteriaBuilder.createQuery(TCase.class);

        Root<TCase> from = query.from(TCase.class);
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
        //List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicate = new ArrayList<>();
        for (final String patternNumber : generatedCaseNumber) {
            Predicate expr = criteriaBuilder.like(from.get(TCase_.csCaseNumber), patternNumber);
            if (!predicate.isEmpty()) {
                Predicate previousExpr = predicate.get(0);
                expr = criteriaBuilder.or(previousExpr, expr);
                predicate.set(0, expr);
            } else {
                predicate.add(expr);
            }
        }
        //Predicate[] tmpPredicates = new Predicate[predicates.size()];
        //predicates.toArray(tmpPredicates);
        query.where(predicate.get(0));

        query.orderBy(criteriaBuilder.asc(from.get(TCase_.csCaseNumber)));

        TypedQuery<TCase> criteriaQuery = getEntityManager().createQuery(query);
        //criteriaQuery = addEntityGraph(criteriaQuery);

        result.addAll(criteriaQuery.getResultList());
        for (TCase cs : result) {
            Hibernate.initialize(cs.getCaseDetails());
        }
        LOG.log(Level.FINER, "Found " + result.size() + " results with case number " + caseNumber);
        //Hibernate.initialize(result);
//      cse.getCaseDetails().iterator();
        return result;
    }

    /**
     * find SingleCaseIds for CaseNumber
     *
     * @param csCaseNumber CaseNumber
     * @return TCase Entity with Eager loaded Values
     * (patient,currentExtern,currentLocal,caseDetails,caseDetails.caseDetailsesForCsdParentId
     */
    public List<Long> findCaseIdsForCaseNumber(String csCaseNumber) {
        final TypedQuery<Long> query = getEntityManager().createQuery(
                "Select e.id from TCase e where e.csCaseNumber LIKE :csCaseNumber",
                Long.class);
        query.setParameter("csCaseNumber", csCaseNumber);
        return query.getResultList();
    }

    /**
     * find SingleCaseIds for CaseNumber and HospitalIdent, Combination of these
     * two Values should be unique
     *
     * @param csHospitalIdent unique hospital identifier
     * @param csCaseNumber CaseNumber
     * @return TCase Entity with Eager loaded Values
     * (patient,currentExtern,currentLocal,caseDetails,caseDetails.caseDetailsesForCsdParentId
     */
    public List<Long> findCaseIdsForCaseNumber(String csHospitalIdent, String csCaseNumber) {
        final TypedQuery<Long> query = getEntityManager().createQuery(
                "Select e.id from TCase e where e.csHospitalIdent LIKE :csHospitalIdent AND e.csCaseNumber LIKE :csCaseNumber",
                Long.class);
        query.setParameter("csHospitalIdent", csHospitalIdent);
        query.setParameter("csCaseNumber", csCaseNumber);
        return query.getResultList();
    }

    /**
     * find SingleCase for CaseNumber
     *
     * @param csCaseNumber CaseNumber
     * @return TCase Entity with Eager loaded Values
     * (patient,currentExtern,currentLocal,caseDetails,caseDetails.caseDetailsesForCsdParentId
     */
    public List<TCase> findCasesForCaseNumber(String csCaseNumber) {
        LOG.log(Level.FINE, "Try to find case with case number " + csCaseNumber + "...");
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TCase> query = criteriaBuilder.createQuery(TCase.class);

        Root<TCase> from = query.from(TCase.class);
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
        query.where(criteriaBuilder.like(from.get(TCase_.csCaseNumber), csCaseNumber));

        TypedQuery<TCase> criteriaQuery = getEntityManager().createQuery(query);
        //criteriaQuery = addEntityGraph(criteriaQuery);

        List<TCase> result = criteriaQuery.getResultList();
        for (TCase cs : result) {
            Hibernate.initialize(cs.getCaseDetails());
        }
        LOG.log(Level.FINER, "Found " + result.size() + " results with case number " + csCaseNumber);
        //Hibernate.initialize(result);
//      cse.getCaseDetails().iterator();
        return result;
    }

    public List<TPatient> findPotentialPatientsForPatientNumber(final String pPatientNumber) {
        final String patientNumber = pPatientNumber == null ? "" : pPatientNumber.trim();
        final List<TPatient> result = new ArrayList<>();
        if (patientNumber.isEmpty()) {
            return result;
        }
        List<String> generatedPatientNumber = new ArrayList<>();
        //generatedCaseNumber.add(caseNumber);
        generatedPatientNumber.add("_" + patientNumber);
        generatedPatientNumber.add(patientNumber + "_");
        generatedPatientNumber.add("_" + patientNumber + "_");
        for (int i = 0; i < patientNumber.length(); i++) {
            String newCaseNumber = patientNumber.substring(0, i) + "_" + patientNumber.substring(i + 1);
            generatedPatientNumber.add(newCaseNumber);
        }

        LOG.log(Level.FINE, "Try to find patient with patient number " + patientNumber + "...");
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TPatient> query = criteriaBuilder.createQuery(TPatient.class);

        Root<TPatient> from = query.from(TPatient.class);
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
        //List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicate = new ArrayList<>();
        for (final String patternNumber : generatedPatientNumber) {
            Predicate expr = criteriaBuilder.like(from.get(TPatient_.patNumber), patternNumber);
            if (!predicate.isEmpty()) {
                Predicate previousExpr = predicate.get(0);
                expr = criteriaBuilder.or(previousExpr, expr);
                predicate.set(0, expr);
            } else {
                predicate.add(expr);
            }
        }
        //Predicate[] tmpPredicates = new Predicate[predicates.size()];
        //predicates.toArray(tmpPredicates);
        query.where(predicate.get(0));

        query.orderBy(criteriaBuilder.asc(from.get(TPatient_.patNumber)));

        TypedQuery<TPatient> criteriaQuery = getEntityManager().createQuery(query);
        //criteriaQuery = addEntityGraph(criteriaQuery);

        result.addAll(criteriaQuery.getResultList());
        LOG.log(Level.FINER, "Found " + result.size() + " results with patient number " + patientNumber);
        //Hibernate.initialize(result);
//      cse.getCaseDetails().iterator();
        return result;
    }

    public List<TPatient> findPatientsForPatientNumber(String pPatientNumber) {
        LOG.log(Level.FINE, "Try to find patient with patient number " + pPatientNumber + "...");
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TPatient> query = criteriaBuilder.createQuery(TPatient.class);

        Root<TPatient> from = query.from(TPatient.class);
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
        query.where(criteriaBuilder.like(from.get(TPatient_.patNumber), pPatientNumber));

        TypedQuery<TPatient> criteriaQuery = getEntityManager().createQuery(query);
        //criteriaQuery = addEntityGraph(criteriaQuery);

        List<TPatient> result = criteriaQuery.getResultList();
        LOG.log(Level.FINER, "Found " + result.size() + " results with patient number " + pPatientNumber);
        //Hibernate.initialize(result);
//      cse.getCaseDetails().iterator();
        return result;
    }

    public TCase getCaseForGrouping2(long pCaseId, boolean pExtern) {
        List<Long> caseId = new ArrayList<>(1);
        caseId.add(pCaseId);
        List<TCase> list = getCaseForGrouping2(caseId, pExtern);
        if (list.isEmpty()) {
            LOG.log(Level.WARNING, "No case found with id " + pCaseId);
            return null;
        }
        if (list.size() > 1) {
            LOG.log(Level.WARNING, "There are multiple cases with id " + pCaseId);
        }
        return list.get(0);
    }

    public List<TCase> getCaseForGrouping2(final List<Long> pCaseIds, boolean pExtern) {
        if (pCaseIds == null) {
            LOG.log(Level.WARNING, "pCaseIds is null!");
            return new ArrayList<>();
        }
        if (pCaseIds.isEmpty()) {
            LOG.log(Level.WARNING, "pCaseIds has no entries!");
            return new ArrayList<>();
        }

        /*
        Query deleteQuery = getEntityManager().createQuery("delete from TGroupingResults grpres where grpres.caseDetails.hospitalCase.id in (:case_ids) and (" + (pExtern?"":" grpres.caseDetails.csdIsLocalFl = 1 and ") + " grpres.caseDetails.csdIsActualFl = 1) ");
        deleteQuery.setParameter("case_ids", pCaseIds);
        int c = deleteQuery.executeUpdate();
        LOG.log(Level.INFO, "Deleted " + c + " entries from T_GROUPING_RESULTS");
         */
//        Query deleteQuery = getEntityManager().createNativeQuery("delete from T_GROUPING_RESULTS g where EXISTS (SELECT ID FROM T_CASE_DETAILS CD WHERE CD.ID = g.T_CASE_DETAILS_ID AND CD.T_CASE_ID IN (:case_ids) and (" + (pExtern?"":" CD.LOCAL_FL = 1 and ") + " CD.ACTUAL_FL = 1)) ");
//        deleteQuery.setParameter("case_ids", pCaseIds);
//        int c = deleteQuery.executeUpdate();
//        LOG.log(Level.INFO, "Deleted " + c + " entries from T_GROUPING_RESULTS");
        StringBuilder stringBuilder = new StringBuilder("select distinct c from TCase c ");
        stringBuilder.append("join fetch c.caseDetails cd ");
        stringBuilder.append("join fetch c.patient pat ");
        stringBuilder.append("left join fetch c.caseLabor lab ");
        stringBuilder.append("left join fetch cd.groupingResultses grpres ");
        stringBuilder.append("left join fetch cd.caseDepartments dep ");
        stringBuilder.append("left join fetch dep.caseOpses ops ");
        stringBuilder.append("left join fetch dep.caseWards ward ");
        stringBuilder.append("left join fetch dep.caseIcds icd ");
        stringBuilder.append("where c.id in (:case_ids) ");
        stringBuilder.append(" and (" + (pExtern ? "" : " cd.csdIsLocalFl = 1 and ") + " cd.csdIsActualFl = 1) ");

        TypedQuery<TCase> query = getEntityManager().createQuery(stringBuilder.toString(), TCase.class);

        query.setParameter("case_ids", pCaseIds);

        List<TCase> cases = query.getResultList();
//        for(TCase cs: cases) {
//            //LOG.log(Level.INFO, "is " + cs + " detachted? " + isDetached(cs));
//            detach(cs);
//        }
        return cases;
    }

    public TCase getCaseForGrouping(long pCaseId, boolean pExtern) {

//        List<TCase> result = (List<TCase>) getSession()
//                .createCriteria(TCase.class)
//                .setFetchMode("patient", FetchMode.JOIN)
//                .setFetchMode("caseDetails", FetchMode.JOIN)
//                .setFetchMode("caseDetails.caseDepartments", FetchMode.JOIN)
//                .setFetchMode("caseDetails.caseDepartments.caseOpses", FetchMode.JOIN)
//                .setFetchMode("caseDetails.caseDepartments.caseIcds", FetchMode.JOIN)
//                .add(Restrictions.eq("id", pCaseId))
//                .list();
//        return result.isEmpty() ? null : result.get(0);
        TCase cs = findById(pCaseId);
        if (cs != null) {
            Hibernate.initialize(cs.getPatient());
            Hibernate.initialize(cs.getCaseDetails());
            Set<TCaseDetails> details = new HashSet<>();
            //local
            TCaseDetails detailLoc = cs.getCurrentLocal();
            if (detailLoc != null) {
                details.add(detailLoc);
            }
            if (pExtern) {
                //local
                TCaseDetails detailExt = cs.getCurrentExtern();
                if (detailExt != null) {
                    details.add(detailExt);
                }
            }
            Iterator<TCaseDetails> itDetails = details.iterator();
            while (itDetails.hasNext()) {
                TCaseDetails detail = itDetails.next();
                Hibernate.initialize(detail.getGroupingResultses());
                Hibernate.initialize(detail.getCaseDepartments());
                Iterator<TCaseDepartment> it2 = detail.getCaseDepartments().iterator();
                while (it2.hasNext()) {
                    TCaseDepartment dep = it2.next();
                    Hibernate.initialize(dep.getCaseIcds());
                    Hibernate.initialize(dep.getCaseOpses());
                    Hibernate.initialize(dep.getCaseWards());
                    //dep.getCaseIcds();
                    //dep.getCaseOpses();
                }

//                Query query = caseDao.getSession().createQuery("delete from " + TGroupingResults.class.getSimpleName() + " where caseDetails.id = :csd_id");
//                query.setLong("csd_id", detail.id);
//                query.executeUpdate();
                /*
                Hibernate.initialize(detail.getGroupingResultses());       
                Iterator<TGroupingResults> itGrpres = new HashSet<>(detail.getGroupingResultses()).iterator();
                while(itGrpres.hasNext()) {
                    TGroupingResults grpres = itGrpres.next();
                    detail.getGroupingResultses().remove(grpres);
                }
                 */
                //Yeah, remove the grouping results!
                //detail.setGroupingResultses(null);
                //detail.setGroupingResultses(new HashSet<>());
                //Session session = getSession();
                //org.hibernate.Query query = session.createQuery("delete from " + TGroupingResults.class.getSimpleName() + " where caseDetails.id = :csd_id");
                //query.setLong("pid", pid);
            }
        }

        //flush();
        return cs;
    }

    public static <T> T deproxy(T obj) {
        if (obj == null) {
            return obj;
        }
        if (obj instanceof HibernateProxy) {
            // Unwrap Proxy;
            //      -- loading, if necessary.
            HibernateProxy proxy = (HibernateProxy) obj;
            LazyInitializer li = proxy.getHibernateLazyInitializer();
            return (T) li.getImplementation();
        }
        return obj;
    }

    /**
     * find SingleCase for CaseNumber and HospitalIdent, Combination of these
     * two Values should be unique.
     *
     * NEVER EVER DO INITIALIZE T_GROUPING_RESULTS HERE, OR YOU WILL GET THIS
     * ERROR: merge of TCaseDetails with id xxxxxx failed:
     * javax.persistence.EntityNotFoundException: Unable to find
     * de.lb.cpx.model.TGroupingResults with id xxxxx
     *
     * @param csCaseId Db-Id of the Case
     * @return TCase Entity with Eager loaded Values
     * (patient,currentExtern,currentLocal,caseDetails,caseDetails.caseDetailsesForCsdParentId
     */
    public TCase findCaseForCaseNumberEager(Long csCaseId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TCase> query = criteriaBuilder.createQuery(TCase.class);

        Root<TCase> from = query.from(TCase.class);
        query.where(criteriaBuilder.equal(from.get(TCase_.id), csCaseId));

        TypedQuery<TCase> criteriaQuery = getEntityManager().createQuery(query);
        //entityGraph, should maybe deleted, perfornance dropoff in linked lists
        //awi:20170316
        criteriaQuery = addEntityGraphWithDepartments(criteriaQuery);
        TCase hosCase = getSingleResultOrNull(criteriaQuery); //criteriaQuery.getSingleResult();
        if (hosCase != null) {

            for (TCaseDetails det : hosCase.getCaseDetails()) {
                if (det.getCaseDetailsByCsdParentId() != null) {
                    Hibernate.initialize(det.getCaseDetailsByCsdParentId());
                }
            }
            TPatient patient = hosCase.getPatient();
            //AWI-20171220: not needed due to insurance data in case details?
            if (patient != null) {
                Hibernate.initialize(patient.getPatientDetailList());
                Hibernate.initialize(patient.getInsurances());
            }
        }
        return hosCase;
    }

    /**
     * find SingleCase for CaseNumber and HospitalIdent, Combination of these
     * two Values should be unique
     *
     * @param csCaseId Db-Id of the Case
     * @return TCase Entity with Eager loaded Values
     * (patient,currentExtern,currentLocal,caseDetails,caseDetails.caseDetailsesForCsdParentId
     */
    public TCase findCaseForCaseNumberEagerForDocumentImport(Long csCaseId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TCase> query = criteriaBuilder.createQuery(TCase.class);

        Root<TCase> from = query.from(TCase.class);
        query.where(criteriaBuilder.equal(from.get(TCase_.id), csCaseId));

        TypedQuery<TCase> criteriaQuery = getEntityManager().createQuery(query);
        //entityGraph, should maybe deleted, perfornance dropoff in linked lists
        //awi:20170316
        criteriaQuery = addEntityGraphWithDepartments(criteriaQuery);
        TCase hosCase = getSingleResultOrNull(criteriaQuery); //criteriaQuery.getSingleResult();
        if (hosCase != null) {

            for (TCaseDetails det : hosCase.getCaseDetails()) {
                Hibernate.initialize(det.getGroupingResultses());
                if (det.getCaseDetailsByCsdParentId() != null) {
                    Hibernate.initialize(det.getCaseDetailsByCsdParentId());
                }
                for (TCaseDepartment dep : det.getCaseDepartments()) {
                    for (TCaseIcd icd : dep.getCaseIcds()) {
                        //Hibernate.initialize(icd.getCaseDepartment().getCaseDetails());                        
                        TCaseDepartment dep2 = icd.getCaseDepartment();
                        Hibernate.initialize(dep2.getCaseDetails());
                        Hibernate.initialize(icd.getCaseIcdGroupeds());
                        Hibernate.initialize(icd.getGroupingResultses());
                    }
                }
                //Hibernate.initialize(det.getMainDiagnosis());
            }
            TPatient patient = hosCase.getPatient();
            //AWI-20171220: not needed due to insurance data in case details?
            if (patient != null) {
                Hibernate.initialize(patient.getPatientDetailList());
                Hibernate.initialize(patient.getInsurances());
            }
        }
        return hosCase;
    }

    /**
     * findList of TCase Entities for a PatientId
     *
     * @param patientId Database-Id of the Patient
     * @return List of TCase Entities
     */
    public List<TCase> findListOfTCaseForPatient(Long patientId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCase> query = criteriaBuilder.createQuery(TCase.class);
        Root<TCase> from = query.from(TCase.class);
        query.where(criteriaBuilder.equal(from.get(TCase_.patient), patientId));
        TypedQuery<TCase> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

//    public boolean isDetached(TCase tCase) {
//        return !getEntityManager().contains(tCase);
//    }
//    
//    public void detach(TCase tCase) {
//        getEntityManager().detach(tCase);
//    }
//    public void evict(TCase pCase){
//        ((Session)getEntityManager().getDelegate()).evict(pCase);
//    }
//    public void changeFlushMode(FlushMode pMode){
//        ((Session)getEntityManager().getDelegate()).setFlushMode(pMode);
//    }
//    public FlushMode getFlushMode(){
//        return ((Session)getEntityManager().getDelegate()).getFlushMode();
//    }
    public List<TCase> findByAdmissionAndDischargeDate(Date admissionDateFrom, Date admissionDateUntil, Date dischargeDateFrom, Date dischargeDateUntil, boolean grouped, boolean extern) {

        return findByAdmissionAndDischargeDate(admissionDateFrom, admissionDateUntil, dischargeDateFrom, dischargeDateUntil, grouped, extern, GDRGModel.AUTOMATIC);
    }

    public List<TCase> findByAdmissionAndDischargeDate(Date admissionDateFrom, Date admissionDateUntil, Date dischargeDateFrom, Date dischargeDateUntil, boolean grouped, boolean extern, String grouperModel) {
        return findByAdmissionAndDischargeDate(admissionDateFrom, admissionDateUntil, dischargeDateFrom, dischargeDateUntil, grouped, extern, GDRGModel.getModel2Name(grouperModel));
    }

    public List<TCase> findByAdmissionAndDischargeDate(Date admissionDateFrom, Date admissionDateUntil, Date dischargeDateFrom, Date dischargeDateUntil, boolean grouped, boolean extern, GDRGModel grouperModel) {

//        StringBuilder stringBuilder = new StringBuilder("select distinct c from TCase c join fetch c.caseDetails cd left join fetch c.patient left join fetch cd.caseDepartments cdep "
//                + "left join fetch cd.groupingResultses left join fetch cdep.caseWards left join fetch cdep.caseOpses left join fetch cdep.caseIcds ");
        StringBuilder stringBuilder = new StringBuilder("select distinct cd from TCaseDetails cd ");
        /*
        if(!extern){
// lokale fälle
            stringBuilder.append("join c.currentLocal cd ");
        }else{ 
// externe Fälle
             stringBuilder.append("join c.currentExtern cd ");
        }
         */
        stringBuilder.append("join cd.hospitalCase c ");
        stringBuilder.append("where");
        boolean whereClause = false;
        if (!extern) {
            // lokale fälle
            stringBuilder.append(" (cd.csdIsLocalFl = 1 and cd.csdIsActualFl = 1) ");
        } else {
            // externe Fälle
            stringBuilder.append(" (cd.csdIsLocalFl = 0 and cd.csdIsActualFl = 1) ");
        }
        if (admissionDateFrom != null) {
            stringBuilder.append(" and");
            stringBuilder.append(" cd.csdAdmissionDate >= :admissionDateFrom");
            whereClause = true;
        }
        if (admissionDateUntil != null) {
            stringBuilder.append(" and");
            stringBuilder.append(" cd.csdAdmissionDate <= :admissionDateUntil");
            whereClause = true;
        }
        if (dischargeDateFrom != null) {
            stringBuilder.append(" and");
            stringBuilder.append(" cd.csdDischargeDate >= :dischargeDateFrom");
            whereClause = true;
        }
        if (dischargeDateUntil != null) {
            stringBuilder.append(" and");
            stringBuilder.append(" cd.csdDischargeDate <= :dischargeDateUntil");
            whereClause = true;
        }

        if (!grouped) {

            stringBuilder.append(" and ");
            stringBuilder.append(" not exists  (select 1 from TGroupingResults gr where  cd = gr.caseDetails and ");
            if (grouperModel == null || grouperModel.equals(GDRGModel.AUTOMATIC)) {
// look for T_GROUPING_RESULTS.GRPRES_IS_AUTO_FL = 1
                stringBuilder.append("gr.grpresIsAutoFl = 1)");
            } else {
// else for T_GROUPING_RESULTS.GRPRES_IS_AUTO_FL = 0 and T_GROUPING_RESULTS.MOSEL_ID_EN = grouperModel
                stringBuilder.append("gr.grpresIsAutoFl = 0 and gr.modelIdEn = '" + grouperModel.name() + "')");

            }

            whereClause = true;
        }

        if (!whereClause) {
            stringBuilder.append(" and ");
            stringBuilder.append(" 0 = 0");
        }

        TypedQuery<TCaseDetails> query = getEntityManager().createQuery(stringBuilder.toString(), TCaseDetails.class);

        if (admissionDateFrom != null) {
            query.setParameter("admissionDateFrom", admissionDateFrom);
        }
        if (admissionDateUntil != null) {
            query.setParameter("admissionDateUntil", admissionDateUntil);
        }
        if (dischargeDateFrom != null) {
            query.setParameter("dischargeDateFrom", dischargeDateFrom);
        }
        if (dischargeDateUntil != null) {
            query.setParameter("dischargeDateUntil", dischargeDateUntil);
        }

        List<TCaseDetails> hosdList = query.getResultList();
        List<TCase> caseList = new ArrayList<>();
        for (TCaseDetails hosd : hosdList) {
            caseList.add(hosd.getHospitalCase());
        }
        return caseList;
    }

    /**
     * Gets a list of case ids that are relevant for batchgrouping
     *
     * @param pAdmissionDateFrom admission date from
     * @param pAdmissionDateUntil admission date to
     * @param pDischargeDateFrom discharge date from
     * @param pDischargeDateUntil discharge date to
     * @param pGrouped grouped
     * @param pExtern external/local
     * @param pGrouperModel grouper model
     * @return list of case ids
     */
    public List<Long> findIdsByAdmissionAndDischargeDate(
            final Date pAdmissionDateFrom, final Date pAdmissionDateUntil,
            final Date pDischargeDateFrom, final Date pDischargeDateUntil,
            final boolean pGrouped, final boolean pExtern,
            final String pGrouperModel) {
        return findIdsByAdmissionAndDischargeDate(pAdmissionDateFrom, pAdmissionDateUntil, pDischargeDateFrom, pDischargeDateUntil, pGrouped, pExtern, GDRGModel.getModel2Name(pGrouperModel));
    }

//    /**
//     * Gets a list of case ids that are relevant for batchgrouping
//     *
//     * @param pAdmissionDateFrom admission date from
//     * @param pAdmissionDateUntil admission date to
//     * @param pDischargeDateFrom discharge date from
//     * @param pDischargeDateUntil discharge date to
//     * @param pGrouped grouped
//     * @param pExtern external/local
//     * @param pFrom from
//     * @param pLimit limit
//     * @param pGrouperModel grouper model
//     * @return list of case ids
//     */
//    public List<TCase> findIdsByAdmissionAndDischargeDate2(final Date pAdmissionDateFrom, final Date pAdmissionDateUntil,
//            final Date pDischargeDateFrom, final Date pDischargeDateUntil,
//            final boolean pGrouped, final boolean pExtern,
//            final GDRGModel pGrouperModel,
//            final int pFrom, final int pLimit) {
//
//        StringBuilder stringBuilder = new StringBuilder("select distinct c from TCase c ");
//        stringBuilder.append("join fetch c.caseDetails cd ");
//        stringBuilder.append("left join fetch cd.groupingResultses grpres ");
//        stringBuilder.append("join fetch cd.caseDepartments dep ");
//        stringBuilder.append("join fetch dep.caseOpses ops ");
//        stringBuilder.append("left join fetch dep.caseWards ward ");
//        stringBuilder.append("join fetch dep.caseIcds icd ");
//        stringBuilder.append("where");
//        //2018-03-22 DNi - Ticket CPX-881: Ignore cases with admission date 
//        //before 2014! It doesn't make sense to consider them in batchgrouping 
//        //(it's just a waste of time to load and analyze them and they 
//        //manipulate the time statistics)
//        stringBuilder.append(" year(cd.csdAdmissionDate) >= 2014");
//        stringBuilder.append(" and");
//        stringBuilder.append(" (" + (pExtern ? "" : " cd.csdIsLocalFl = 1 and ") + " cd.csdIsActualFl = 1) ");
//        if (pAdmissionDateFrom != null) {
//            stringBuilder.append(" and");
//            stringBuilder.append(" cd.csdAdmissionDate >= :admissionDateFrom");
//        }
//        if (pAdmissionDateUntil != null) {
//            stringBuilder.append(" and");
//            stringBuilder.append(" cd.csdAdmissionDate <= :admissionDateUntil");
//        }
//        if (pDischargeDateFrom != null) {
//            stringBuilder.append(" and");
//            stringBuilder.append(" cd.csdDischargeDate >= :dischargeDateFrom");
//        }
//        if (pDischargeDateUntil != null) {
//            stringBuilder.append(" and");
//            stringBuilder.append(" cd.csdDischargeDate <= :dischargeDateUntil");
//        }
//
//        if (!pGrouped) {
//
//            stringBuilder.append(" and ");
//            stringBuilder.append(" not exists  (select 1 from TGroupingResults gr where  cd = gr.caseDetails and ");
//            if (pGrouperModel == null || pGrouperModel.equals(GDRGModel.AUTOMATIC)) {
//                // look for T_GROUPING_RESULTS.GRPRES_IS_AUTO_FL = 1
//                stringBuilder.append("gr.grpresIsAutoFl = 1)");
//            } else {
//                // else for T_GROUPING_RESULTS.GRPRES_IS_AUTO_FL = 0 and T_GROUPING_RESULTS.MOSEL_ID_EN = grouperModel
//                stringBuilder.append("gr.grpresIsAutoFl = 0 and gr.modelIdEn = '" + pGrouperModel.name() + "')");
//
//            }
//        }
//        //boah, evidently added due to error of no adm or dis dates are set to make a where clause
////        if (!whereClause) {
////            stringBuilder.append(" and ");
////            stringBuilder.append(" 0 = 0");
////        }
//
//        TypedQuery<TCase> query = getEntityManager().createQuery(stringBuilder.toString(), TCase.class);
//
//        if (pAdmissionDateFrom != null) {
//            query.setParameter("admissionDateFrom", pAdmissionDateFrom);
//        }
//        if (pAdmissionDateUntil != null) {
//            query.setParameter("admissionDateUntil", pAdmissionDateUntil);
//        }
//        if (pDischargeDateFrom != null) {
//            query.setParameter("dischargeDateFrom", pDischargeDateFrom);
//        }
//        if (pDischargeDateUntil != null) {
//            query.setParameter("dischargeDateUntil", pDischargeDateUntil);
//        }
//
//        query.setFirstResult(pFrom);
//        query.setMaxResults(pLimit);
//        //query.unwrap(org.hibernate.Query.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//        //LOG.log(Level.INFO, query.unwrap(org.hibernate.Query.class).getDatabaseQuery().getSQLString());
//
//        List<TCase> caseList = query.getResultList();
//
//        return caseList;
//    }
//
//    /**
//     * Gets a list of case ids that are relevant for batchgrouping
//     *
//     * @param pAdmissionDateFrom admission date from
//     * @param pAdmissionDateUntil admission date to
//     * @param pDischargeDateFrom discharge date from
//     * @param pDischargeDateUntil discharge date to
//     * @param pGrouped grouped
//     * @param pExtern external/local
//     * @param pGrouperModel grouper model
//     * @param pFrom From
//     * @param pLimit Limit
//     * @return list of case ids
//     */
//    public List<TCase> findIdsByAdmissionAndDischargeDate2(
//            final Date pAdmissionDateFrom, final Date pAdmissionDateUntil,
//            final Date pDischargeDateFrom, final Date pDischargeDateUntil,
//            final boolean pGrouped, final boolean pExtern,
//            final String pGrouperModel,
//            final int pFrom, final int pLimit) {
//        return findIdsByAdmissionAndDischargeDate2(pAdmissionDateFrom, pAdmissionDateUntil, pDischargeDateFrom, pDischargeDateUntil, pGrouped, pExtern, GDRGModel.getModel2Name(pGrouperModel), pFrom, pLimit);
//    }
    /**
     * Gets a list of case ids that are relevant for batchgrouping
     *
     * @param pAdmissionDateFrom admission date from
     * @param pAdmissionDateUntil admission date to
     * @param pDischargeDateFrom discharge date from
     * @param pDischargeDateUntil discharge date to
     * @param pGrouped grouped
     * @param pExtern external/local
     * @param pGrouperModel grouper model
     * @return list of case ids
     */
    public List<Long> findIdsByAdmissionAndDischargeDate(final Date pAdmissionDateFrom, final Date pAdmissionDateUntil,
            final Date pDischargeDateFrom, final Date pDischargeDateUntil,
            final boolean pGrouped, final boolean pExtern,
            final GDRGModel pGrouperModel) {

        StringBuilder stringBuilder = new StringBuilder("select distinct c.id from TCaseDetails cd ");
        stringBuilder.append("join cd.hospitalCase c ");
        stringBuilder.append("where");
        //2018-03-22 DNi - Ticket CPX-881: Ignore cases with admission date 
        //before 2014! It doesn't make sense to consider them in batchgrouping 
        //(it's just a waste of time to load and analyze them and they 
        //manipulate the time statistics)
        stringBuilder.append(" year(cd.csdAdmissionDate) >= ").append(GrouperConstant.GROUPER_MIN_YEAR);
        //boolean whereClause = true;
        if (!pExtern) {
            // lokale fälle
            stringBuilder.append(" and");
            stringBuilder.append(" (cd.csdIsLocalFl = 1 and cd.csdIsActualFl = 1) ");
        } else {
            // externe Fälle
            stringBuilder.append(" and");
            stringBuilder.append(" (cd.csdIsLocalFl = 0 and cd.csdIsActualFl = 1) ");
        }
        if (pAdmissionDateFrom != null) {
            stringBuilder.append(" and");
            stringBuilder.append(" cd.csdAdmissionDate >= :admissionDateFrom");
            //whereClause = true;
        }
        if (pAdmissionDateUntil != null) {
            stringBuilder.append(" and");
            stringBuilder.append(" cd.csdAdmissionDate <= :admissionDateUntil");
            //whereClause = true;
        }
        if (pDischargeDateFrom != null) {
            stringBuilder.append(" and");
            stringBuilder.append(" cd.csdDischargeDate >= :dischargeDateFrom");
            //whereClause = true;
        }
        if (pDischargeDateUntil != null) {
            stringBuilder.append(" and");
            stringBuilder.append(" cd.csdDischargeDate <= :dischargeDateUntil");
            //whereClause = true;
        }

        if (!pGrouped) {

            stringBuilder.append(" and ");
            stringBuilder.append(" not exists  (select 1 from TGroupingResults gr where  cd = gr.caseDetails and ");
            if (pGrouperModel == null || pGrouperModel.equals(GDRGModel.AUTOMATIC)) {
                // look for T_GROUPING_RESULTS.GRPRES_IS_AUTO_FL = 1
                stringBuilder.append("gr.grpresIsAutoFl = 1)");
            } else {
                // else for T_GROUPING_RESULTS.GRPRES_IS_AUTO_FL = 0 and T_GROUPING_RESULTS.MOSEL_ID_EN = grouperModel
                stringBuilder.append("gr.grpresIsAutoFl = 0 and gr.modelIdEn = '" + pGrouperModel.name() + "')");

            }

            //whereClause = true;
        }
        //boah, evidently added due to error of no adm or dis dates are set to make a where clause
        //if (!whereClause) {
        stringBuilder.append(" and ");
        stringBuilder.append(" 0 = 0");

        TypedQuery<Long> query = getEntityManager().createQuery(stringBuilder.toString(), Long.class);

        if (pAdmissionDateFrom != null) {
            query.setParameter("admissionDateFrom", pAdmissionDateFrom);
        }
        if (pAdmissionDateUntil != null) {
            query.setParameter("admissionDateUntil", pAdmissionDateUntil);
        }
        if (pDischargeDateFrom != null) {
            query.setParameter("dischargeDateFrom", pDischargeDateFrom);
        }
        if (pDischargeDateUntil != null) {
            query.setParameter("dischargeDateUntil", pDischargeDateUntil);
        }

        final List<Long> caseList = query.getResultList();

        LOG.log(Level.INFO, "Query to fetch case ids for batchgrouping: "
                + "\n     " + stringBuilder.toString()
                + "\n      "
                + "\n     Batchgrouping query was built on base of the following parameters: "
                + "\n     pGrouped: " + pGrouped + ", pExtern: " + pExtern + ", pGrouperModel: " + pGrouperModel
                + "\n     pAdmissionDateFrom: " + pAdmissionDateFrom + ", pAdmissionDateUntil: " + pAdmissionDateUntil
                + "\n     pDischargeDateFrom: " + pDischargeDateFrom + ", pDischargeDateUntil: " + pDischargeDateUntil
                + "\n     ===> Result: " + caseList.size() + " case ids found!"
        );
        //}

        return caseList;
    }

    private TypedQuery<TCase> addEntityGraph(TypedQuery<TCase> criteriaQuery) {
        EntityGraph<TCase> toFetch = getEntityManager().createEntityGraph(TCase.class);
        //toFetch.addAttributeNodes(TCase_.currentExtern,TCase_.currentLocal);
        //toFetch.addSubgraph(TCase_.patient).addAttributeNodes(TPatient_.patInsuranceActual);
        toFetch.addSubgraph(TCase_.patient);
        toFetch.addSubgraph(TCase_.caseDetails);
        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
        return criteriaQuery;
    }

    private TypedQuery<TCase> addEntityGraphWithDepartments(TypedQuery<TCase> criteriaQuery) {
        EntityGraph<TCase> toFetch = getEntityManager().createEntityGraph(TCase.class);
        //toFetch.addSubgraph(TCase_.currentExtern).addSubgraph(TCaseDetails_.caseDepartments);
        //toFetch.addSubgraph(TCase_.currentLocal).addSubgraph(TCaseDetails_.caseDepartments);
        toFetch.addSubgraph(TCase_.patient).addSubgraph(TPatient_.cases);
        //toFetch.addSubgraph(TCase_.patient).addSubgraph(TPatient_.patInsuranceActual);
//        toFetch.addSubgraph(TCase_.patient).addSubgraph(TPatient_.insurances);
        toFetch.addSubgraph(TCase_.caseDetails);
        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
        return criteriaQuery;
    }

//    /**
//     * clears a attached TCase Entity of given Version
//     * Warning: resets CurrentLocal and CurrentExtern to null, needs to be set to a new Value in an other process!
//     * @param hCase TCase to be cleared 
//     * @param version version to clear
//     */
//    public boolean removeVersionFromCase(TCase hCase, TCaseDetails version) {
//        /*
//        if(version.equals(hCase.getCurrentLocal())){
//            hCase.setCurrentLocal(null);
//        }
//        if(version.equals(hCase.getCurrentExtern())){
//            hCase.setCurrentExtern(version);
//        }
//        */
//        boolean ret;
//        if (ret = hCase.removeCaseDetails(version)) {
//          version =detailsDao.merge(version);
//          detailsDao.remove(version);
//          flush();
//        }
//        return ret;
//        /*
//        Iterator<TCaseDetails> it = hCase.getCaseDetails().iterator();
//        while(it.hasNext()) {
//          TCaseDetails csd = it.next();
//          if (csd.getId() == version.getId()) {
//            it.remove();
//            break;
//          }
//        }
//        */
//        /*
//        ArrayList<TCaseDetails> list = new ArrayList<>(hCase.getCaseDetails());
//        list.removeIf(p->p.getId()==version.getId());
//        hCase.setCaseDetails(new HashSet<>(list));
//        flush();
//        */
//    }
//    @Override
//    public boolean deleteById(final long id) {
//
//        try {
//            Query query = getEntityManager()
//                    .createNativeQuery("delete from T_CASE cascade where id = " + String.valueOf(id));
//            int affectedRows = query.executeUpdate();
//            if (affectedRows > 0) {
//                LOG.log(Level.FINE, "remove of case with id " + id + " successful");
//                return true;
//            } else {
//                LOG.log(Level.WARNING, "no case was deleted, because there seems to be exist no entry with id " + id);
//                return false;
//            }
//        } catch (final RuntimeException re) {
//            String message = "remove of case with id " + id + " failed";
//            LOG.log(Level.INFO, message, re);
//            throw new RuntimeException(message, re);
//        }
//    }
    /**
     *
     * @param pCase hospital case
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public synchronized void deleteCase(final TCase pCase) {
        if (pCase == null) {
            LOG.log(Level.WARNING, "pCase is null!");
            return;
        }
        if (pCase.id == 0L) {
            LOG.log(Level.WARNING, "id is equal to 0, maybe this case has not been persisted yet!");
            //Not persisted yet
            return;
        }
        //synchronized (NOTHING) {
        final String caseSignature = pCase.getCaseSignature();
        LOG.log(Level.INFO, "Procedure to remove hospital case {0} from database is starting...", caseSignature);
        EntityManager em = getEntityManager();
        Iterator<TCaseDetails> itDetails = pCase.getCaseDetails().iterator();
        while (itDetails.hasNext()) {
            TCaseDetails csd = itDetails.next();
            Iterator<TCaseDepartment> itDepartments = csd.getCaseDepartments().iterator();
            while (itDepartments.hasNext()) {
                TCaseDepartment dep = itDepartments.next();
                //2017-12-05 DNi: It would be better to use a prepared statement here!
                LOG.log(Level.FINE, "Will remove ICDs for case {0} now...", caseSignature);
                em.createNativeQuery(String.format("UPDATE T_CASE_ICD SET T_CASE_ICD_ID = NULL WHERE T_CASE_DEPARTMENT_ID = %s", dep.id)).executeUpdate();
            }
            //2017-12-05 DNi: It would be better to use a prepared statement here!
            LOG.log(Level.FINE, "Will release each others references on case details for case {0} now...", caseSignature);
            em.createNativeQuery(String.format("UPDATE T_CASE_DETAILS SET EXTERN_ID = NULL, PARENT_ID = NULL WHERE ID = %s", csd.id)).executeUpdate();
            LOG.log(Level.FINE, "Will release Risks for case {0} now...", caseSignature);
            em.createNativeQuery(String.format("UPDATE T_WM_RISK SET T_WM_REQUEST_ID = NULL, T_WM_PROCESS_HOSPITAL_FINAL_ID = NULL WHERE T_CASE_DETAILS_ID = %s", csd.id)).executeUpdate();
            LOG.log(Level.FINE, "Will release T_WM_REQUEST for case {0} now...", caseSignature);
            em.createNativeQuery(String.format("UPDATE T_WM_REQUEST SET T_CASE_DETAILS_ID = NULL WHERE T_CASE_DETAILS_ID = %s", csd.id)).executeUpdate();
            LOG.log(Level.FINE, "Will release T_WM_PROCESS_T_CASE for case {0} now...", caseSignature);
            em.createNativeQuery(String.format("UPDATE T_WM_PROCESS_T_CASE SET T_CASE_DETAILS_ID = NULL WHERE T_CASE_DETAILS_ID = %s", csd.id)).executeUpdate();
        }
        //flush();
        itDetails = pCase.getCaseDetails().iterator();
        while (itDetails.hasNext()) {
            TCaseDetails csd = itDetails.next();
            //2017-12-05 DNi: It would be better to use a prepared statement here!
            LOG.log(Level.FINE, "Will delete case risks for case {0} now...", caseSignature);
            em.createNativeQuery(String.format("DELETE FROM T_WM_RISK WHERE T_CASE_DETAILS_ID = %s", csd.id)).executeUpdate();
            LOG.log(Level.FINE, "Will delete case details for case {0} now...", caseSignature);
            em.createNativeQuery(String.format("DELETE FROM T_CASE_DETAILS WHERE ID = %s", csd.id)).executeUpdate();
        }
        //2017-12-05 DNi: It would be better to use a prepared statement here!
        //LOG.log(Level.FINE, "Will delete case details for case " + caseSignature + " now...");
        //em.createNativeQuery(String.format("UPDATE T_CASE SET T_PATIENT_ID = NULL WHERE ID = %s", pCase.id)).executeUpdate();
        //flush();
        LOG.log(Level.FINE, "Will delete drug references for case {0} now...", caseSignature);
        em.createNativeQuery(String.format("DELETE FROM T_DRUG WHERE T_CASE_ID = %s", pCase.id)).executeUpdate();
        LOG.log(Level.FINE, "Will delete process references for case {0} now...", caseSignature);
        em.createNativeQuery(String.format("DELETE FROM T_WM_PROCESS_T_CASE WHERE T_CASE_ID = %s", pCase.id)).executeUpdate();
        LOG.log(Level.FINE, "Will delete event references for case {0} now...", caseSignature);
        em.createNativeQuery(String.format("UPDATE T_WM_EVENT SET T_CASE_ID = NULL WHERE T_CASE_ID = %s", pCase.id)).executeUpdate();
        LOG.log(Level.FINE, "Will delete microbiological data (MiBi) for case {0} now...", caseSignature);
        em.createNativeQuery(String.format("DELETE FROM T_MIBI WHERE T_CASE_ID = %s", pCase.id)).executeUpdate();
        // remove(pCase); //Resulted in an Oracle Deadlock till 2016-12-07 (should be fixed with CPX-300)
        //2017-12-05 DNi: It would be better to use a prepared statement here!
        LOG.log(Level.FINE, "Will delete case {0} now...", caseSignature);
        em.createNativeQuery(String.format("DELETE FROM T_CASE WHERE ID = %s", pCase.id)).executeUpdate();
        //}
        LOG.log(Level.INFO, "Hospital case {0} was removed from database successfully!", caseSignature);

//    //synchronized(TCaseDao.class) {
//      Iterator<TCaseDetails> itDetails = pCase.getCaseDetails().iterator();
//      //List<TCaseDetails> detailsList = new ArrayList<>();
//      while(itDetails.hasNext()) {
//        TCaseDetails csd = itDetails.next();
//        Iterator<TCaseDepartment> itDepartments = csd.getCaseDepartments().iterator();
//        while(itDepartments.hasNext()) {
//          TCaseDepartment dep = itDepartments.next();
//          Iterator<TCaseIcd> itIcd;
//          itIcd = dep.getCaseIcds().iterator();
//          while(itIcd.hasNext()) {
//            TCaseIcd icd = itIcd.next();
//            icd.setRefIcd(null);
//          }
//          flush();
//          //itIcd = dep.getCaseIcds().iterator();
//          //while(itIcd.hasNext()) {
//          //  TCaseIcd icd = itIcd.next();
//          //  caseIcdDao.remove(icd);
//          //}
//          //flush();
//          //caseDepartmentDao.remove(dep);
//          //flush();
//        }
//        csd.setCaseDetailsByCsdParentId(null);
//        csd.setCaseDetailsByCsdExternId(null);
//        csd.setHospitalCase(null);
//        //detailsList.add(csd);
//        //flush();
//        //caseDetailsDao.remove(csd);
//      }
//      flush();
//      remove(pCase); //Resulted in an Oracle Deadlock till 2016-12-07 (should be fixed with CPX-300)
////      for(TCaseDetails csd: detailsList) {
////        caseDetailsDao.remove(csd);
////      }
//      Query query = getEntityManager().createNativeQuery("DELETE FROM T_CASE_DETAILS WHERE HOSC_ID IS NULL");
//      query.executeUpdate();
//      flush();
//    //}
    }

    /**
     *
     * @param pCase hospital case
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException error
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public synchronized void cancelCase(final TCase pCase) throws CpxIllegalArgumentException {
        if (pCase == null) {
            LOG.log(Level.WARNING, "pCase is null!");
            return;
        }
        if (pCase.id == 0L) {
            LOG.log(Level.WARNING, "id is equal to 0, maybe this case has not been persisted yet!");
            return;
        }
        final String caseSignature = pCase.getCaseSignature();
        LOG.log(Level.INFO, "Procedure to cancel hospital case {0}", caseSignature);
        if (pCase.getCsCancellationReasonEn()) {
            throw new CpxIllegalArgumentException("Fall ist schon unstorniert: " + caseSignature);
        } else {
            Iterator<TCaseDetails> itDetails = pCase.getCaseDetails().iterator();
            while (itDetails.hasNext()) {
                TCaseDetails csd = itDetails.next();
                csd.setCsdCancelDate(new Date());
                csd.setCsdCancelReasonEn(CaseDetailsCancelReasonEn.MANUAL);
            }
            pCase.setCsCancellationReasonEn(true);
            flush();
        }

    }

    /**
     *
     * @param pCase hospital case
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException error
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public synchronized void unCancelCase(final TCase pCase) throws CpxIllegalArgumentException {
        if (pCase == null) {
            LOG.log(Level.WARNING, "pCase is null!");
            return;
        }
        if (pCase.id == 0L) {
            LOG.log(Level.WARNING, "id is equal to 0, maybe this case has not been persisted yet!");
            return;
        }
        final String caseSignature = pCase.getCaseSignature();
        LOG.log(Level.INFO, "Procedure to cancel hospital case {0}", caseSignature);
        if (!pCase.getCsCancellationReasonEn()) {
            throw new CpxIllegalArgumentException("Fall ist schon unstorniert: " + caseSignature);
        } else {
            Iterator<TCaseDetails> itDetails = pCase.getCaseDetails().iterator();
            boolean userCancel = false;
//            TCaseDetails newCsd = null;
            while (itDetails.hasNext()) {
                TCaseDetails csd = itDetails.next();
//                if (csd.getCsdIsActualFl() && csd.getCsdIsLocalFl()) {
                if (CaseDetailsCancelReasonEn.MANUAL.equals(csd.getCsdCancelReasonEn())) {
                    userCancel = true;
                    csd.setCsdCancelDate(null);
                    csd.setCsdCancelReasonEn(null);
//                        newCsd = csd.cloneWithoutIds();
//                        newCsd.setCsdVersion(csd.getCsdVersion() + 1);
//                        newCsd.setCsdCancelDate(null);
//                        newCsd.setCsdCancelReasonEn(null);
//                        csd.setCsdIsActualFl(false);
//                    }
                }
            }
            if (userCancel) {
                pCase.setCsCancellationReasonEn(false);
            } else {
                throw new RuntimeException("Fall kann nicht unstorniert ,da nicht manuell storniert ist");
            }
            flush();
        }

    }

    public List<TCase> findMatchesLazy(String partialCaseNumber) {
        long start = System.currentTimeMillis();
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TCase> query = criteriaBuilder.createQuery(TCase.class);

        Root<TCase> from = query.from(TCase.class);
        query.where(criteriaBuilder.like(criteriaBuilder.lower(from.get(TCase_.csCaseNumber)), "%" + partialCaseNumber.toLowerCase() + "%"));
        List<TCase> results = addEntityGraph(getEntityManager().createQuery(query)).getResultList();
        LOG.info("result list size " + results.size() + " time needed " + (System.currentTimeMillis() - start));
        return results;
    }

    public Collection<String> findMatchingCaseNumbers(String partialCaseNumber, long patientID) {
        long start = System.currentTimeMillis();
//        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
//        
//        CriteriaQuery<String> query = criteriaBuilder.createQuery(String.class);
//        
//        Root<TCase> from = query.from(TCase.class);
//        query.select(from.get(TCase_.csCaseNumber));
//        query.where(criteriaBuilder.like(criteriaBuilder.lower(from.get(TCase_.csCaseNumber)), "%"+partialCaseNumber.toLowerCase()+"%"));
//        List<String> results = getEntityManager().createQuery(query).getResultList();
        final TypedQuery<String> query = getEntityManager().createQuery(
                "Select e.csCaseNumber from TCase e where e.patient.id = :patientID and e.csCaseNumber LIKE :csCaseNumber",
                String.class);
        query.setParameter("patientID", patientID);
        query.setParameter("csCaseNumber", "%" + partialCaseNumber + "%");
        List<String> results = query.getResultList();
        LOG.info("result list size for string case number " + results.size() + " time needed " + (System.currentTimeMillis() - start));
        return results;
    }

    /**
     * find single case entity for case number Warning: case number alone might
     * not be unique! only first fetch result is returned! Use
     * findCaseForCasenumber(final String csHospitalIdent, final String
     * csCaseNumber) instead!
     *
     * @param caseNumber case number of the case
     * @return single case entity
     */
    public TCase findCaseByNumber(String caseNumber) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TCase> query = criteriaBuilder.createQuery(TCase.class);

        Root<TCase> from = query.from(TCase.class);
        query.where(criteriaBuilder.like(criteriaBuilder.lower(from.get(TCase_.csCaseNumber)), caseNumber.toLowerCase()));
        List<TCase> results = addEntityGraph(getEntityManager().createQuery(query)).getResultList();
        if (results.size() > 1) {
            LOG.warning("Methode: findCaseByNumber return resultset > 1, only 1 result is expected! Size of results: " + results.size());
        }
        return !results.isEmpty() ? results.get(0) : null;
    }

    public List<TCase> getAllCasesWithPatient() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCase> query = criteriaBuilder.createQuery(TCase.class);
        query.distinct(true);
        //Root<TCase> from = query.from(TCase.class);
        List<TCase> results = addEntityGraph(getEntityManager().createQuery(query)).getResultList();
        return results;
    }

    public List<TCase> getLockedCaseForUser(long cpxUserId) {
        String sqlQuery = "SELECT cse.* FROM T_CASE cse JOIN T_LOCK lck ON lck.CASE_ID = cse.ID WHERE lck.USER_ID = " + String.valueOf(cpxUserId);
        Query query = getEntityManager().createNativeQuery(sqlQuery, TCase.class);
        return query.getResultList();
    }

    public Integer getCount() {
        String query = "SELECT COUNT(*) CNT FROM T_CASE WHERE CANCEL_FL = 0";
        List<Number> list = getEntityManager().createNativeQuery(query).getResultList();
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
    
    public Integer getCount4Type( CaseTypeEn pType) {
         String query = "SELECT COUNT(*) CNT FROM T_CASE WHERE CANCEL_FL = 0 AND CS_CASE_TYPE_EN = '" + pType.name() + "'";
        List<Number> list = getEntityManager().createNativeQuery(query).getResultList();
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
    
    public Integer getCountCasesWithNoGrpRes4Type(Boolean pIsLocal, CaseTypeEn pType ){
        return getCountCasesWithNoGrpRes4Type(pIsLocal, pType, null);
    }
    
    public Integer getCountCasesWithNoGrpRes4Type(Boolean pIsLocal, CaseTypeEn pType, Long pPatientId ){
        String qry = "SELECT COUNT(*) FROM T_CASE_DETAILS TD " +
                "INNER JOIN T_CASE CS ON CS.ID = TD.T_CASE_ID " +
                "WHERE LOCAL_FL = " + (pIsLocal?"1":"0")+ " AND (NOT EXISTS " +
                "(SELECT 1 FROM T_CASE_ICD ICD " +
                "INNER JOIN T_CASE_DEPARTMENT DEP ON DEP.ID = ICD.T_CASE_DEPARTMENT_ID " +
                "WHERE DEP.T_CASE_DETAILS_ID = TD.ID AND ICD.MAIN_DIAG_CASE_FL = 1)  " +
                "OR DISCHARGE_DATE IS NULL) " +
                "AND CS.CANCEL_FL = 0 AND CS.CS_CASE_TYPE_EN = '" + pType.name() + "' AND TD.ACTUAL_FL = 1 " +
                (pPatientId== null?"":(" AND CS.T_PATIENT_ID = " + String.valueOf(pPatientId)))
                ;
        
        
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

    public Integer getCanceledCount() {
        String query = "SELECT COUNT(*) CNT FROM T_CASE WHERE CANCEL_FL = 1";
        List<Number> list = getEntityManager().createNativeQuery(query).getResultList();
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

    public List<TCase> findByIds(List<Object> items) {
        String hql = "select distinct cse FROM TCase cse WHERE cse.id IN (:ids)";
        Query query = getEntityManager().createQuery(hql, TCase.class);
        query.setParameter("ids", items);

//        query.setHint(EntityGraphType.getLoadGraphType(), getEntityManager().getEntityGraph("fetchBatchCaseGraph"));
        return query.getResultList();
    }

    public List<TCase> deleteByIds(List<Object> items) {
        String query = "DELETE FROM T_CASE WHERE ID IN (";
        String in = "";
//        for(Long id : items){
//            in = in.concat(String.valueOf(id) + " ");
//        }
        in = items.stream().map((aLong) -> aLong.toString()).collect(Collectors.joining(","));
//        String.join(",", );
        query = query.concat(in + ")");
//        LOG.info("query: " + query);
        return getEntityManager().createNativeQuery(query, TCase.class).getResultList();
    }

    /**
     * @return all drg related cases in the database Note: should only beused
     * for testing, could return a huge dataset
     */
    public List<TCase> findAllDrg() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCase> query = criteriaBuilder.createQuery(TCase.class);
        query.distinct(true);
        Root<TCase> from = query.from(TCase.class);
        query.where(criteriaBuilder.equal(from.get(TCase_.csCaseTypeEn), CaseTypeEn.DRG));
        List<TCase> results = getEntityManager().createQuery(query).getResultList();
        return results;
    }

    /**
     * @return all pepp related cases in the database Note: should only beused
     * for testing, could return a huge dataset
     */
    public List<TCase> findAllPepp() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCase> query = criteriaBuilder.createQuery(TCase.class);
        query.distinct(true);
        Root<TCase> from = query.from(TCase.class);
        query.where(criteriaBuilder.equal(from.get(TCase_.csCaseTypeEn), CaseTypeEn.PEPP));
        List<TCase> results = getEntityManager().createQuery(query).getResultList();
        return results;
    }

    /**
     *
     * @return all Insurance related cases in the database
     */
    public List<String> getAllInsurance() {
        final TypedQuery<String> query = getEntityManager().createQuery("select insuranceIdentifier from TCase c where insuranceIdentifier is not Null group by insuranceIdentifier", String.class);
        return query.getResultList();
    }

    public Map<String, String> getAllInsShortNames() {
        //final TypedQuery<String> query = getEntityManager().createQuery("select distinct insuranceIdentifier from TCase c where insuranceIdentifier is not Null group by insuranceIdentifier", String.class);
        //final List<String> idents = getAllInsurance();
        return insuranceCompanyDao.getAllInsuranceCompaniesShort();
//        Map<String, String> allInsShortNames = insuranceCompanyDao.getAllInsuranceCompaniesShort();
//        final Set<String> names = new TreeSet<>();
//        for(final String ident: getAllInsurance()) {
//            String insShort = allInsShortNames.get(ident);
//            if (insShort != null) {
//                names.add(insShort);
//            }
//        }
//        return new ArrayList<>(names);
    }

    public String getHospitalIdent(final Long pCaseId) {
        if (pCaseId == null || pCaseId.equals(0L)) {
            LOG.log(Level.FINEST, "Passed hospital case id cannot be null or 0!");
            return "";
        }
        final String query = String.format("SELECT CS_HOSPITAL_IDENT FROM T_CASE WHERE ID = %s ", pCaseId);
        final String hospitalIdent = (String) getEntityManager().createNativeQuery(query).getSingleResult();
        return hospitalIdent;
    }

    public String getCaseNumber(final Long pCaseId) {
        if (pCaseId == null || pCaseId.equals(0L)) {
            LOG.log(Level.FINEST, "Passed hospital case id cannot be null or 0!");
            return "";
        }
        final String query = String.format("SELECT CS_CASE_NUMBER FROM T_CASE WHERE ID = %s ", pCaseId);
        final String hospitalIdent = (String) getEntityManager().createNativeQuery(query).getSingleResult();
        return hospitalIdent;
    }

    public Map<String, Set<String>> getCaseKeys(final long[] pCaseIds) {
        final Map<String, Set<String>> keys = new HashMap<>();
        if (pCaseIds == null || pCaseIds.length == 0) {
            LOG.log(Level.FINEST, "Passed hospital case id cannot be null or 0!");
            return keys;
        }
        StringBuilder sb = new StringBuilder();
        for (long caseId : pCaseIds) {
            if (caseId == 0L) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(" OR ");
            }
            sb.append(String.format("ID = %s ", caseId));
        }
        final String query = String.format("SELECT CS_HOSPITAL_IDENT, CS_CASE_NUMBER FROM T_CASE WHERE %s ", sb.toString());
        List<Object[]> result = getEntityManager().createNativeQuery(query).getResultList();
        for (Object[] r : result) {
            final String hosIdent = (String) r[0];
            final String caseNumber = (String) r[1];
            Set<String> list = keys.get(hosIdent);
            if (list == null) {
                list = new HashSet<>();
            }
            list.add(caseNumber);
            keys.put(hosIdent, list);
        }
        return keys;
    }

    public TCase getCaseForSerialization(final long pCaseId) {
        changeFlushMode(FlushModeType.COMMIT); //formerly (< WF14/Hibernate 5.3): FlushMode.MANUAL
        TCase cs = findCaseForCaseNumberEager(pCaseId);
        if (cs == null) {
            return null;
        }
        Iterator<TCaseDetails> itDet = cs.getCaseDetails().iterator();
        while (itDet.hasNext()) {
            TCaseDetails csd = itDet.next();
            if (!csd.getCsdIsActualFl()) {
                itDet.remove();
                continue;
            }
            csd.setGroupingResultses(null);
            csd.setCaseDetailsByCsdParentId(null);
            csd.setCaseDetailsesForCsdExternId(null);
            csd.setCaseDetailsesForCsdParentId(null);
            Iterator<TCaseDepartment> itDep = csd.getCaseDepartments().iterator();
            while (itDep.hasNext()) {
                TCaseDepartment dep = itDep.next();
                Iterator<TCaseIcd> itIcd = dep.getCaseIcds().iterator();
                while (itIcd.hasNext()) {
                    TCaseIcd icd = itIcd.next();
                    icd.getRefIcd();
                    icd.setGroupingResultses(null);
                    icd.setCaseIcdGroupeds(null);
                    Iterator<TCaseIcd> itRefIcd = icd.getRefIcds().iterator();
                    while (itRefIcd.hasNext()) {
                        TCaseIcd refIcd = itRefIcd.next();
                        refIcd.setGroupingResultses(null);
                        refIcd.setCaseIcdGroupeds(null);
                    }
                }
                Iterator<TCaseOps> itOps = dep.getCaseOpses().iterator();
                while (itOps.hasNext()) {
                    TCaseOps ops = itOps.next();
                    ops.setCaseOpsGroupeds(null);
                }
                Iterator<TCaseWard> itWard = dep.getCaseWards().iterator();
                while (itWard.hasNext()) {
                    TCaseWard ward = itWard.next();
                    ward.getCaseIcds();
                    ward.getCaseOpses();
                }
            }
            Iterator<TCaseFee> itFee = csd.getCaseFees().iterator();
            while (itFee.hasNext()) {
                TCaseFee fee = itFee.next();
            }
            Iterator<TCaseBill> itBill = csd.getCaseBills().iterator();
            while (itBill.hasNext()) {
                TCaseBill bill = itBill.next();
            }
//                Iterator<TGroupingResults> itGrpRes = csd.getGroupingResultses().iterator();
//                while(itGrpRes.hasNext()) {
//                    TGroupingResults grpRes = itGrpRes.next();
//                }
        }
        cs.setCaseMergeMappingsForMergeMemberCaseId(null);
        cs.setCaseMergeMappingsForHoscId(null);
        cs.setCaseComments(null);
        Iterator<TLab> itLab = cs.getCaseLabor().iterator();
        while (itLab.hasNext()) {
            TLab lab = itLab.next();
        }
        detach(cs);
        clear();
        return cs;
    }

    public int getPatientCasesCount(CaseTypeEn pType, Long pPatientId) {
         String query = "SELECT COUNT(*) CNT FROM T_CASE WHERE CANCEL_FL = 0 AND CS_CASE_TYPE_EN = '" + pType.name() + "'"
                 + " AND T_PATIENT_ID = " + String.valueOf(pPatientId) ;
        List<Number> list = getEntityManager().createNativeQuery(query).getResultList();
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
    
    public List<Long> getCaseIds4Patient(Long pPatientId){
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<TCase> from = query.from(TCase.class);
        query.select(from.get(TCase_.id));
        query.where(criteriaBuilder.equal(from.get(TCase_.PATIENT), pPatientId));

        TypedQuery<Long> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();

    }

}
