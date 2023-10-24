/* 
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.wm.dao;

import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseDetails_;
import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.server.commons.enums.EntityGraphType;
import de.lb.cpx.server.dao.AbstractCpxDao;
import de.lb.cpx.shared.dto.MdkAuditComplaintsDTO;
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.TWmRisk_;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Data access object for domain model class TPatient. Initially generated at
 * 21.01.2016 17:14:39 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SuppressWarnings("unchecked")
public class TWmRiskDao extends AbstractCpxDao<TWmRisk> {

    private static final Logger LOG = Logger.getLogger(TWmRiskDao.class.getName());

    /**
     * Creates a new instance.
     */
    public TWmRiskDao() {
        super(TWmRisk.class);
    }

    @SuppressWarnings("unchecked")
    public List<MdkAuditComplaintsDTO> getMdkAuditComplaints(final String pHospitalIdent, final String pInsuranceIdent, final Integer pBillYear, final Integer pBillQuarter, final Integer pAuditYear, final Integer pAuditQuarter) {
        List<String> wheres = new ArrayList<>();
        if (pHospitalIdent != null) {
            wheres.add("T_CASE.CS_HOSPITAL_IDENT = '" + pHospitalIdent + "'");
        }
        if (pInsuranceIdent != null) {
            wheres.add("T_CASE.INSURANCE_IDENTIFIER = '" + pInsuranceIdent + "'");
        }
        if (pBillYear != null) {
            wheres.add(getYearSql("T_CASE.CS_BILLING_DATE") + " = " + pBillYear);
        }
        if (pBillQuarter != null) {
            wheres.add(getQuarterSql("T_CASE.CS_BILLING_DATE") + " = " + pBillQuarter);
        }
        if (wheres.isEmpty()) {
            wheres.add("1=1");
        }

        final String conditions = String.join(" AND ", wheres);

        final String sql = String.format("SELECT TMP.*, \n"
                + "COUNT(*) CASE_COUNT, \n"
                + "(SELECT COUNT(*) "
                + "FROM T_WM_PROCESS_T_CASE PC "
                + "INNER JOIN T_CASE CS2 ON CS2.ID = PC.T_CASE_ID "
                + "INNER JOIN T_WM_REQUEST REQ ON REQ.T_WM_PROCESS_HOSPITAL_ID = PC.T_WM_PROCESS_ID AND REQ.REQUEST_TYPE = 2 "
                + "INNER JOIN T_WM_REQUEST_MDK MDK ON MDK.ID = REQ.ID "
                + "INNER JOIN T_CASE_DETAILS CSD ON CSD.T_CASE_ID = PC.T_CASE_ID AND CSD.ACTUAL_FL = 1 AND CSD.LOCAL_FL = 0 "
                + "WHERE PC.MAIN_CASE_FL = 1 AND " + getQuarterSql("MDK.MDK_START_AUDIT") + " = " + pAuditQuarter + " AND " + getYearSql("MDK.MDK_START_AUDIT") + " = " + pAuditYear + " "
                + "AND CS2.CS_HOSPITAL_IDENT = TMP.CS_HOSPITAL_IDENT AND CS2.INSURANCE_IDENTIFIER = TMP.INSURANCE_IDENTIFIER "
                + "and CSD.ADMISSION_REASON_12_EN IN ('01', '02')) COMPLAINT_COUNT \n"
                + "FROM (\n"
                + "SELECT \n"
                + "CS_HOSPITAL_IDENT, \n"
                + "INSURANCE_IDENTIFIER, \n"
                + pAuditYear + " YEAR, \n"
                + pAuditQuarter + " QUARTER \n"
                + "FROM T_CASE \n"
                + "INNER JOIN T_CASE_DETAILS CSD ON CSD.T_CASE_ID = T_CASE.ID AND CSD.ACTUAL_FL = 1 AND CSD.LOCAL_FL = 0 \n"
                + "WHERE CSD.ADMISSION_REASON_12_EN IN ('01', '02') \n"
                + "  AND %s\n"
                + ") TMP GROUP BY CS_HOSPITAL_IDENT, QUARTER, YEAR, INSURANCE_IDENTIFIER", conditions);
        final Query query = getSession().createNativeQuery(sql);
        final List<MdkAuditComplaintsDTO> result = new ArrayList<>();
        for (Object[] obj : (List<Object[]>) query.getResultList()) {
            result.add(new MdkAuditComplaintsDTO(
                    (String) obj[0],
                    (String) obj[1],
                    toInt((Number) obj[2]),
                    toInt((Number) obj[3]),
                    toInt((Number) obj[4]),
                    toInt((Number) obj[5])
            ));
        }
        return result;
    }

    private static Integer toInt(Number pValue) {
        if (pValue == null) {
            return null;
        }

        return pValue.intValue();
    }

    private String getQuarterSql(final String pField) {
        if (isOracle()) {
            return "FLOOR((EXTRACT(MONTH FROM " + pField + ") - 1) / 3) + 1";
        } else {
            return "DATEPART(qq, " + pField + ")";
        }
    }

    private String getYearSql(final String pField) {
        if (isOracle()) {
            return "EXTRACT(YEAR FROM " + pField + ")";
        } else {
            return "DATEPART(yyyy, " + pField + ")";
        }
    }

    public List<TWmRisk> findRisks4CaseVersion(long id) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmRisk> query = criteriaBuilder.createQuery(getEntityClass());
        Root<TWmRisk> from = query.from(getEntityClass());
        query.where(criteriaBuilder.equal(from.get(TWmRisk_.caseDetails), id));
        TypedQuery<TWmRisk> criteriaQuery = getEntityManager().createQuery(query);
        final List<TWmRisk> risks = criteriaQuery.getResultList();
        if (risks == null) {
            return new ArrayList<>();
        }
        return risks;
    }

    public TWmRisk findRisk4CaseAndPlaceOrReg(long pCaseId, PlaceOfRegEn pPlaceOfReg) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmRisk> query = criteriaBuilder.createQuery(getEntityClass());
        Root<TWmRisk> from = query.from(getEntityClass());
        query.where(criteriaBuilder.equal(from.get(TWmRisk_.caseDetails), pCaseId), criteriaBuilder.equal(from.get(TWmRisk_.RISK_PLACE_OF_REG), pPlaceOfReg));
        TypedQuery<TWmRisk> criteriaQuery = getEntityManager().createQuery(query);
        //final List<TWmRisk> risks = criteriaQuery.getResultList();
        final TWmRisk risk = getSingleResultOrNull(criteriaQuery);//criteriaQuery.getResultList();
        return risk;
    }

    public TWmRisk getSingleRiskForAreaOfReg(long pCaseId, PlaceOfRegEn pPlaceOfRegEn) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TWmRisk> query = criteriaBuilder.createQuery(getEntityClass());
        Root<TWmRisk> from = query.from(getEntityClass());
        query.where(criteriaBuilder.equal(from.get(TWmRisk_.caseDetails), pCaseId), criteriaBuilder.equal(from.get(TWmRisk_.RISK_PLACE_OF_REG), pPlaceOfRegEn));
        TypedQuery<TWmRisk> criteriaQuery = getEntityManager().createQuery(query);
        addRiskDetailGraph(criteriaQuery);
        return getSingleResultOrNull(criteriaQuery);
    }

    private void addRiskDetailGraph(TypedQuery<TWmRisk> criteriaQuery) {
        EntityGraph<TWmRisk> toFetch = getEntityManager().createEntityGraph(TWmRisk.class);
        //toFetch.addAttributeNodes(TCase_.currentExtern,TCase_.currentLocal);
        //toFetch.addSubgraph(TCase_.patient).addAttributeNodes(TPatient_.patInsuranceActual);
        toFetch.addSubgraph(TWmRisk_.RISK_DETAILS);
        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
//        return criteriaQuery;
    }

    public TWmRisk getRequestRisk(long pCaseDetailsId, long pRequestId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TWmRisk> query = criteriaBuilder.createQuery(getEntityClass());
        Root<TWmRisk> from = query.from(getEntityClass());
        query.where(criteriaBuilder.equal(from.get(TWmRisk_.caseDetails), pCaseDetailsId),
                criteriaBuilder.equal(from.get(TWmRisk_.REQUEST), pRequestId),
                criteriaBuilder.equal(from.get(TWmRisk_.RISK_PLACE_OF_REG), PlaceOfRegEn.REQUEST));
        TypedQuery<TWmRisk> criteriaQuery = getEntityManager().createQuery(query);
        addRiskDetailGraph(criteriaQuery);
        return getSingleResultOrNull(criteriaQuery);
    }

    public List<TWmRisk> findAllRisks(long pCaseId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmRisk> query = criteriaBuilder.createQuery(getEntityClass());
        Root<TWmRisk> from = query.from(getEntityClass());
        query.where(criteriaBuilder.equal(from.get(TWmRisk_.caseDetails), pCaseId));
        TypedQuery<TWmRisk> criteriaQuery = getEntityManager().createQuery(query);
        addRiskDetailGraph(criteriaQuery);
        final List<TWmRisk> risks = criteriaQuery.getResultList();
        if (risks == null) {
            return new ArrayList<>();
        }
        return risks;
    }

    public TWmRisk findActualRisk(long pCaseId,PlaceOfRegEn placeOfRegEn, VersionRiskTypeEn pType) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmRisk> query = criteriaBuilder.createQuery(getEntityClass());
        Root<TWmRisk> from = query.from(getEntityClass());
        Join<TWmRisk, TCaseDetails> detailsJoin = from.join(TWmRisk_.CASE_DETAILS);
//        query.where(criteriaBuilder.equal(from.get(TWmRisk_.caseDetails), pCaseId));
        query.where(criteriaBuilder.equal(from.get(TWmRisk_.RISK_PLACE_OF_REG), placeOfRegEn),
                criteriaBuilder.equal(from.get(TWmRisk_.RISK_ACTUAL4_REQ), Boolean.TRUE),
                criteriaBuilder.equal(detailsJoin.get(TCaseDetails_.HOSPITAL_CASE), pCaseId),
                criteriaBuilder.equal(detailsJoin.get(TCaseDetails_.CSD_VERS_RISK_TYPE_EN), pType));
        
        TypedQuery<TWmRisk> criteriaQuery = getEntityManager().createQuery(query);
        addRiskDetailGraph(criteriaQuery);
        return getSingleResultOrNull(criteriaQuery);
    }

    public List<TWmRisk> findActualRequestRisks(long pCaseId, PlaceOfRegEn placeOfRegEn) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmRisk> query = criteriaBuilder.createQuery(getEntityClass());
        Root<TWmRisk> from = query.from(getEntityClass());
        Join<TWmRisk, TCaseDetails> detailsJoin = from.join(TWmRisk_.CASE_DETAILS);
//        query.where(criteriaBuilder.equal(from.get(TWmRisk_.caseDetails), pCaseId));
        Predicate typeOrPredicate= criteriaBuilder.or(criteriaBuilder.equal(detailsJoin.get(TCaseDetails_.CSD_VERS_RISK_TYPE_EN), VersionRiskTypeEn.AUDIT_MD),
                criteriaBuilder.equal(detailsJoin.get(TCaseDetails_.CSD_VERS_RISK_TYPE_EN), VersionRiskTypeEn.AUDIT_CASE_DIALOG));
        query.where(typeOrPredicate,
                criteriaBuilder.equal(from.get(TWmRisk_.RISK_PLACE_OF_REG), placeOfRegEn),
                criteriaBuilder.equal(from.get(TWmRisk_.RISK_ACTUAL4_REQ), Boolean.TRUE),
                criteriaBuilder.equal(detailsJoin.get(TCaseDetails_.HOSPITAL_CASE), pCaseId));
        
        TypedQuery<TWmRisk> criteriaQuery = getEntityManager().createQuery(query);
        addRiskDetailGraph(criteriaQuery);
        return criteriaQuery.getResultList();
    }

    public int resetActual4OtherRisks(long pCaseId, long pRiskId, VersionRiskTypeEn pVersionRiskType, boolean pLocal) {
       String qry =" update t_wm_risk  set RISK_ACTUAL_4_REG = 0 " +
                " where " + //RISK_PLACE_OF_REG = '" + pRisk.getRiskPlaceOfReg().name() + "'" +
                "  T_CASE_DETAILS_ID in (select id from t_case_details where t_case_id = " + pCaseId +
                " and local_fl = " + (pLocal?1:0) +
                " and csd_vers_risk_type_en = '" + pVersionRiskType.name() + "') and id <> " + pRiskId;
       final Query query = getSession().createNativeQuery(qry);
       return query.executeUpdate();
    }

    
}
