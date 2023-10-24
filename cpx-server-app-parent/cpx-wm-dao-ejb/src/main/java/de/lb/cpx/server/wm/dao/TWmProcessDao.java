/*
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2016  Husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.wm.dao;

import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase_;
import de.lb.cpx.model.TP301KainInka;
import de.lb.cpx.model.TP301KainInkaPvv;
import de.lb.cpx.model.TP301KainInkaPvv_;
import de.lb.cpx.model.TP301KainInka_;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.TPatient_;
import de.lb.cpx.model.enums.CaseStatusEn;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commons.enums.EntityGraphType;
import de.lb.cpx.server.dao.AbstractCpxDao;
import de.lb.cpx.server.generator.SequenceNumberGenerator;
import de.lb.cpx.shared.dto.DocumentSearchCaseItemDto;
import de.lb.cpx.shared.dto.DocumentSearchPatientItemDto;
import de.lb.cpx.shared.dto.DocumentSearchProcessItemDto;
import de.lb.cpx.wm.model.TWmAction;
import de.lb.cpx.wm.model.TWmDocument;
import de.lb.cpx.wm.model.TWmEvent;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmProcessCase;
import de.lb.cpx.wm.model.TWmProcessCase_;
import de.lb.cpx.wm.model.TWmProcessHospital;
import de.lb.cpx.wm.model.TWmProcess_;
import de.lb.cpx.wm.model.TWmReminder;
import de.lb.cpx.wm.model.enums.Tp301Key30En;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.FlushModeType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Subquery;
import org.hibernate.Hibernate;

/**
 *
 * @author Husser
 */
@Stateless
@SuppressWarnings("unchecked")
public class TWmProcessDao extends AbstractCpxDao<TWmProcess> {

    private static final Logger LOG = Logger.getLogger(TWmProcessDao.class.getName());

    @EJB
    private SequenceNumberGenerator numberGenerator;

    public TWmProcessDao() {
        super(TWmProcess.class);
    }

//    public List<TWmProcess> findWorkflowsByLastUser(long modificationUserId) {
//        String sql = "select p from TWmProcess p where p.modificationUserId = :modificationUserId";
//        TypedQuery<TWmProcess> query = getEntityManager().createQuery(sql, TWmProcess.class);
//        query.setParameter("modificationUserId", modificationUserId);
//        return query.getResultList();
//    }
    public List<TWmProcess> findWorkflowsByLastUser(long modificationUserId) {
        String sql = "select p from TWmProcess p where p.processModificationUser = :modificationUserId";
        TypedQuery<TWmProcess> query = getEntityManager().createQuery(sql, TWmProcess.class);
        query.setParameter("modificationUserId", modificationUserId);
        return query.getResultList();
    }

    public List<TWmProcess> findWorkflowsByPatient(TPatient patient) {
        String sql = "select p from TWmProcess p where p.patient = :patient";
        TypedQuery<TWmProcess> query = getEntityManager().createQuery(sql, TWmProcess.class);
        query.setParameter("patient", patient);
        return query.getResultList();
    }

    public <T extends TWmProcess> TWmProcess findEagerById(long processId, boolean pEager, Class<T> pProcessSubClazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<T> query = criteriaBuilder.createQuery(pProcessSubClazz);

        Root<T> from = query.from(pProcessSubClazz);
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
        query.where(criteriaBuilder.equal(from.get(TWmProcess_.id), processId));

        TypedQuery<T> criteriaQuery = getEntityManager().createQuery(query);

        if (pEager) {
            EntityGraph<TWmProcess> toFetch = getEntityManager().createEntityGraph(TWmProcess.class);
            Subgraph<Set<TWmProcessCase>> procCases = toFetch.addSubgraph(TWmProcess_.processCases);
            criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
        }
//        criteriaQuery = addEntityGraph(criteriaQuery);
//        final List<TWmProcess> res = criteriaQuery.getResultList();
//        TWmProcess proc;
//        if (res.isEmpty()) {
//          proc = null;
//        } else {
//          proc = res.get(0);            
//        }
        TWmProcess proc = getSingleResultOrNull(criteriaQuery);
        if (proc == null) {
            return null;
        }
        setEagerly(proc, pEager);
        return proc;
    }

    private <T extends TWmProcess> TypedQuery<T> addEntityGraph(TypedQuery<T> criteriaQuery) {
        EntityGraph<TWmProcess> toFetch = getEntityManager().createEntityGraph(TWmProcess.class);
//        toFetch.addAttributeNodes(TCase_.currentExtern,TCase_.currentLocal);
//        toFetch.addSubgraph(TCase_.patient).addAttributeNodes(TPatient_.patInsuranceActual);
        Subgraph<TPatient> pat = toFetch.addSubgraph(TWmProcess_.patient);
        pat.addAttributeNodes(TPatient_.patientDetailList);
        pat.addAttributeNodes(TPatient_.cases);
        pat.addAttributeNodes(TPatient_.insurances);
        Subgraph<Set<TWmDocument>> doc = toFetch.addSubgraph(TWmProcess_.documents);
        Subgraph<Set<TWmProcessCase>> proc = toFetch.addSubgraph(TWmProcess_.processCases);
        proc.addSubgraph(TWmProcessCase_.hosCase.getName()).addAttributeNodes(TCase_.caseDetails.getName());
        Subgraph<Set<TWmReminder>> rem = toFetch.addSubgraph(TWmProcess_.reminders);
        //Subgraph<Set<TWmRequest>> req = toFetch.addSubgraph(TWmProcess_.requests);
        Subgraph<Set<TWmEvent>> events = toFetch.addSubgraph(TWmProcess_.events);
        Subgraph<Set<TWmAction>> actions = toFetch.addSubgraph(TWmProcess_.actions);
        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
        return criteriaQuery;
    }

    /**
     * finds list of items for the workflow list loads tCase and tpatient
     * entites for the process warning: for tests only
     *
     * @return list of twmprocess entites for the workflow list
     */
    public List<TWmProcess> findWorkflowItems() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TWmProcess> query = criteriaBuilder.createQuery(TWmProcess.class);
        query.distinct(true);
        Root<TWmProcess> from = query.from(TWmProcess.class);
//        List<TWmProcess> results = addEntityGraph(getEntityManager().createQuery(query)).getResultList();
        TypedQuery<TWmProcess> criteriaQuery = getEntityManager().createQuery(query);
        EntityGraph<TWmProcess> toFetch = getEntityManager().createEntityGraph(TWmProcess.class);
        toFetch.addSubgraph(TWmProcess_.patient);
        toFetch.addSubgraph(TWmProcess_.processCases);
        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
        List<TWmProcess> results = criteriaQuery.getResultList();
        return results;
    }

    /**
     * persist the twmprocess entity and sets a new workflow number generated by
     * the workflowNumberGenerator
     *
     * @param pProcess process to store
     */
    @Override
    public void persist(TWmProcess pProcess) {
        if (pProcess.getWorkflowNumber() == 0L) {
            pProcess.setWorkflowNumber(createWorkflowNumber());
        }
        super.persist(pProcess);
    }

    public Long createWorkflowNumber() {
        return numberGenerator.generateNextWorkflowNumberValue();
    }

    private void setEagerly(TWmProcess proc, boolean pEager) {
        changeFlushMode(FlushModeType.COMMIT); //formerly (< WF14/Hibernate 5.3): FlushMode.MANUAL
        //workaround to prevent document context to be send to the client
        //ugly but hibernate/jpa do not support lazy loading on singleAttributeFields - are always eager loadeds
        if (pEager) {
            for (TWmDocument doc : proc.getDocuments()) {
                doc.setContent(null);
            }
            proc.getEvents().iterator();
            proc.getProcessCases().iterator();
            for (TWmProcessCase pc : proc.getProcessCases()) {
                pc.getHosCase().getCaseDetails().iterator();
            }
            proc.getReminders().iterator();
            if (proc instanceof TWmProcessHospital) {
                ((TWmProcessHospital) proc).getRequests().iterator();
                Hibernate.initialize(((TWmProcessHospital) proc).getProcessHospitalFinalisation());
            }
            proc.getActions().iterator();
        }
        proc.getPatient().getPatientDetailList().iterator();
        proc.getPatient().getInsurances().iterator();
        proc.getPatient().getCases().iterator();

    }

    public Integer getCount() {
        String query = "SELECT COUNT(*) CNT FROM T_WM_PROCESS WHERE CANCEL_FL = 0";
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

    public Integer getCanceledCount() {
        String query = "SELECT COUNT(*) CNT FROM T_WM_PROCESS WHERE CANCEL_FL = 1";
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

    /**
     * find SingleProcess for WorkflowNumber
     *
     * @param workflowNumber Workflow Number (Process Number)
     * @return TWmProcess Entity with Eager loaded Values
     */
    public TWmProcess findProcessForWorkflowNumber(Long workflowNumber) {
        LOG.log(Level.FINE, "Try to find process with workflow number {0}...", String.valueOf(workflowNumber));
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmProcess> query = criteriaBuilder.createQuery(TWmProcess.class);

        Root<TWmProcess> from = query.from(TWmProcess.class);
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
        //query.where(criteriaBuilder.like(from.get(TWmProcess_.workflowNumber), workflowNumber), criteriaBuilder.equal(from.get(TCase_.csHospitalIdent), hospitalIdent));
        query.where(criteriaBuilder.equal(from.get(TWmProcess_.workflowNumber), workflowNumber));

        TypedQuery<TWmProcess> criteriaQuery = getEntityManager().createQuery(query);
        //criteriaQuery = addEntityGraph(criteriaQuery);

        TWmProcess pc = getSingleResultOrNull(criteriaQuery);
        if (pc == null) {
            LOG.log(Level.WARNING, "No process found with workflow number {0}!", String.valueOf(workflowNumber));
        } else {
            LOG.log(Level.FINER, "Found with workflow number {0}: {1}", new Object[]{String.valueOf(workflowNumber), String.valueOf(pc)});
        }
        //Hibernate.initialize(pc);
//      cse.getCaseDetails().iterator();
        return pc;
    }

    /**
     * find SingleProcess id for WorkflowNumber
     *
     * @param workflowNumber Workflow Number (Process Number)
     * @return Long id of process
     */
    public Long findProcessIdForWorkflowNumber(final Long workflowNumber) {
        if (workflowNumber == null) {
            return null;
        }
        LOG.log(Level.FINE, "Try to find process id with workflow number {0}...", String.valueOf(workflowNumber));

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<TWmProcess> from = query.from(getEntityClass());
        query.where(cb.equal(from.get(TWmProcess_.workflowNumber), workflowNumber));
        query.select(from.get(TWmProcess_.id));

        TypedQuery<Long> criteriaQuery = getEntityManager().createQuery(query);
        List<Long> result = criteriaQuery.getResultList();

        if (result.isEmpty()) {
            LOG.log(Level.WARNING, "No process id found with workflow number {0}!", String.valueOf(workflowNumber));
            return null;
        } else {
            final Long processId = result.get(0);
            LOG.log(Level.FINER, "Found with workflow id {0} for workflow number {1}", new Object[]{processId, String.valueOf(workflowNumber)});
            return processId;
        }
        //Hibernate.initialize(pc);
//      cse.getCaseDetails().iterator();
    }

//    /**
//     * find SingleProcess id for WorkflowNumber
//     *
//     * @param workflowNumber Workflow Number (Process Number)
//     * @return Long id of process
//     */
//    public Long findProcessIdForWorkflowNumber(final Long workflowNumber) {
//        if (workflowNumber == null) {
//            return null;
//        }
//        LOG.log(Level.FINE, "Try to find process id with workflow number " + String.valueOf(workflowNumber) + "...");
//
//        Criteria criteriaQuery = getSession().createCriteria(TWmProcess.class)
//                .add(Restrictions.eq(TWmProcess_.workflowNumber.getName(), workflowNumber))
//                .setProjection(Projections.projectionList()
//                        .add(Projections.property(TWmProcess_.id.getName()), TWmProcess_.id.getName())
//                );
//        //.setResultTransformer(Transformers.aliasToBean(TWmDocument.class));
//
//        List<Long> result = criteriaQuery.list();
//
//        if (result.isEmpty()) {
//            LOG.log(Level.WARNING, "No process id found with workflow number " + String.valueOf(workflowNumber) + "!");
//            return null;
//        } else {
//            Long processId = result.get(0);
//            LOG.log(Level.FINER, "Found with workflow id " + processId + " for workflow number " + String.valueOf(workflowNumber));
//            return processId;
//        }
//        //Hibernate.initialize(pc);
////      cse.getCaseDetails().iterator();
//    }
    public void updateProcessModification(final long pProcessId) {
        //final String userName = ClientManager.getCurrentCpxUserName();
        final Long userId = ClientManager.getCurrentCpxUserId();
        EntityManager em = getEntityManager();
        final String date = isOracle() ? "SYSDATE" : "getdate()";
        em.createNativeQuery(String.format("UPDATE T_WM_PROCESS SET "
                + "MODIFICATION_DATE = %s, LAST_PROCESS_MODIFICATION = %s, "
                + "MODIFICATION_USER = %s, PROCESS_MODIFICATION_USER = %s "
                + "WHERE T_WM_PROCESS.ID = %s", date, date, userId, userId, pProcessId)).executeUpdate();
    }

    public void updateProcessesModification(final List<Long> pProcessIds) {
        final long startTime = System.currentTimeMillis();
        EntityManager em = getEntityManager();
        final Long userId = ClientManager.getCurrentCpxUserId();
        final String date = isOracle() ? "SYSDATE" : "getdate()";
        Query query = em.createNativeQuery(String.format("UPDATE T_WM_PROCESS SET "
                + "MODIFICATION_DATE = %s, LAST_PROCESS_MODIFICATION = %s, "
                + "MODIFICATION_USER = %s, PROCESS_MODIFICATION_USER = %s "
                + "WHERE T_WM_PROCESS.ID IN (:ids)", date, date, userId, userId));
        query.setParameter("ids", pProcessIds);
        int updatedRows = query.executeUpdate();
        LOG.log(Level.INFO, "update {0} processes from {1} processes in {2} ms", new Object[]{updatedRows, pProcessIds.size(), +(System.currentTimeMillis() - startTime)});
    }

    public List<DocumentSearchPatientItemDto> findPatientsForDocumentImport(final String pPatientNumber, final String pPatientName, final Date pDateOfBirth, final int pMaxEntries) {

        final String patientNumber = pPatientNumber == null ? "" : pPatientNumber.trim();
        final String patientName = pPatientName == null ? "" : pPatientName.trim();
        final int limit = pMaxEntries <= 0 ? 10 : pMaxEntries;

        StringBuilder patientNameSql = new StringBuilder();
        StringBuilder patientNameOrderSql = new StringBuilder();
        if (!patientName.isEmpty()) {
            String[] sa = patientName.split("[,\\s]");
            List<String> vals = new ArrayList<>();
            for (int i = 0; i < sa.length; i++) {
                String s = sa[i].trim();
                if (s.isEmpty()) {
                    continue;
                } else {
                    s = "'" + s.replace("*", "%") + "%' ";
                    vals.add(s);
                }
                //sa[i] = "'" + sa[i].replace("*", "%") + "%' ";
            }
            sa = new String[vals.size()];
            vals.toArray(sa);
            patientNameSql.append("PAT.PAT_SEC_NAME LIKE " + String.join("OR PAT.PAT_SEC_NAME LIKE ", sa));
            patientNameSql.append(" OR ");
            patientNameSql.append("PAT.PAT_FIRST_NAME LIKE " + String.join("OR PAT.PAT_FIRST_NAME LIKE ", sa));
            for (String s : sa) {
                if (patientNameOrderSql.length() != 0) {
                    patientNameOrderSql.append(" + ");
                }
                patientNameOrderSql.append("CASE WHEN PAT.PAT_SEC_NAME LIKE " + s + " THEN 2 ELSE 0 END + ");
                patientNameOrderSql.append("CASE WHEN PAT.PAT_FIRST_NAME LIKE " + s + " THEN 1 ELSE 0 END");
            }
        }
        String patNameSql = patientNameSql.toString().trim();

        String sql = String.format("SELECT TMP.*, PC.ID PROCESS_ID, PC.WORKFLOW_NUMBER, CS.ID PC_CASE_ID, PAT2.ID PAT2_ID, PAT2.PAT_NUMBER PAT2_NUMBER, PAT2.PAT_SEC_NAME PAT2_SEC_NAME, PAT2.PAT_FIRST_NAME PAT2_FIRST_NAME, PAT2.PAT_DATE_OF_BIRTH PAT2_DATE_OF_BIRTH, PC_CS.MAIN_CASE_FL PC_MAIN_CASE_FL, CS.CS_STATUS_EN PC_CS_STATE_EN, CS.CS_HOSPITAL_IDENT PC_CS_HOSPITAL_EN, CS.CS_CASE_NUMBER PC_CS_CASE_NUMBER, CSD.ADMISSION_DATE PC_ADMISSION_DATE, CSD.DISCHARGE_DATE PC_DISCHARGE_DATE FROM ( "
                + "SELECT "
                + " PAT.ID PAT_ID, "
                + " PAT.PAT_NUMBER, "
                + " PAT.PAT_SEC_NAME, "
                + " PAT.PAT_FIRST_NAME, "
                + " PAT.PAT_DATE_OF_BIRTH, "
                + " CS.ID CASE_ID, "
                + " CS.CS_STATUS_EN, "
                + " CS.CS_HOSPITAL_IDENT, "
                + " CS.CS_CASE_NUMBER, "
                + " CSD.ADMISSION_DATE, "
                + " CSD.DISCHARGE_DATE, "
                + " CS.INSURANCE_NUMBER_PATIENT"
                //                + (!patNameSql.isEmpty() ? ", " + 
                //                    (isOracle() ? "utl_match.jaro_winkler (PAT_SEC_NAME || PAT_FIRST_NAME, '" + patientName + "')" : " MATCH (PAT_SEC_NAME, PAT_FIRST_NAME) AGAINST ('" + patientName + "') ") + " SCORE" : "")
                + " FROM ( "
                + "   SELECT "
                + "   %s * "
                + "   FROM T_PATIENT PAT "
                + "   WHERE %s "
                + "     AND %s "
                + "     AND %s "
                + "   %s "
                + "   ORDER BY %s "
                + " ) PAT "
                + " LEFT JOIN T_CASE CS ON CS.T_PATIENT_ID = PAT.ID "
                + " LEFT JOIN T_CASE_DETAILS CSD ON CSD.T_CASE_ID = CS.ID AND (CSD.ACTUAL_FL IS NULL OR CSD.ACTUAL_FL = 1) AND (CSD.LOCAL_FL IS NULL OR CSD.LOCAL_FL = 1) "
                + " ) TMP"
                + " LEFT JOIN T_WM_PROCESS PC ON PC.T_PATIENT_ID = TMP.PAT_ID "
                + " LEFT JOIN T_WM_PROCESS_T_CASE PC_CS ON PC_CS.T_WM_PROCESS_ID = PC.ID "
                + " LEFT JOIN T_CASE CS ON CS.ID = PC_CS.T_CASE_ID "
                + " LEFT JOIN T_CASE_DETAILS CSD ON CSD.T_CASE_ID = CS.ID AND (CSD.ACTUAL_FL IS NULL OR CSD.ACTUAL_FL = 1) AND (CSD.LOCAL_FL IS NULL OR CSD.LOCAL_FL = 1) "
                + " LEFT JOIN T_PATIENT PAT2 ON PAT2.ID = PC.T_PATIENT_ID ",
                (isSqlSrv() ? " TOP " + limit : ""), //LIMIT FOR MSSQL
                (!patientNumber.isEmpty() ? "PAT.PAT_NUMBER LIKE '" + patientNumber.replace("*", "%") + "%' " : "1=1"), //WHERE 1
                (!patNameSql.isEmpty() ? " (" + patNameSql + ") " : "1=1"), //WHERE 2
                (pDateOfBirth != null
                        ? (isOracle() ? "PAT.PAT_DATE_OF_BIRTH= TO_DATE('" + pDateOfBirth + "','YYYY-MM-DD') "
                                : "PAT.PAT_DATE_OF_BIRTH= convert(datetime,'" + pDateOfBirth + "',121) ") : "1=1"),
                (isOracle() ? " AND ROWNUM <= " + limit : ""), //LIMIT FOR ORACLE
                (!patientNumber.isEmpty() ? "PAT.PAT_NUMBER" : !patientNameOrderSql.toString().isEmpty() ? patientNameOrderSql.toString() : "PAT.PAT_NUMBER" + " DESC") //ORDER BY
        );

        final Map<Long, DocumentSearchPatientItemDto> result = new HashMap<>();

        Query query = getEntityManager().createNativeQuery(sql);
        List<Object[]> list = query.getResultList();
//        List<DocumentSearchCaseItemDto> result = new ArrayList<>();
        for (Object[] item : list) {
            Long patientId = item[0] == null ? null : ((Number) item[0]).longValue();
            final DocumentSearchPatientItemDto dto;
            if (result.get(patientId) == null) {
                dto = new DocumentSearchPatientItemDto();
                dto.setPatientId(item[0] == null ? null : ((Number) item[0]).longValue());
                dto.setPatientNumber((String) item[1]);
                dto.setPatientSecName((String) item[2]);
                dto.setPatientFirstName((String) item[3]);
                dto.setPatientDateOfBirth((Date) item[4]);
                dto.setInsuranceNumberPatient((String) item[11]);
//                dto.caseId = item[7] == null ? null : ((Number) item[7]).longValue();
//                dto.hospitalIdent = (String) item[8];
//                dto.caseNumber = (String) item[9];
//                dto.admissionDate = (Date) item[10];
//                dto.dischargeDate = (Date) item[11];
                result.put(patientId, dto);
            } else {
                dto = result.get(patientId);
            }
            final Long caseId = item[5] == null ? null : ((Number) item[5]).longValue();
            if (caseId != null && dto.getCaseById(caseId) == null) {
                final boolean mainCaseFl = false;
                final CaseStatusEn caseStatus = CaseStatusEn.valueOf((String) item[6]);
                final String hospitalIdent = (String) item[7];
                final String caseNumber = (String) item[8];
                final Date admissionDate = (Date) item[9];
                final Date dischargeDate = (Date) item[10];

                final DocumentSearchCaseItemDto caseDto = new DocumentSearchCaseItemDto(
                        caseId,
                        caseStatus,
                        hospitalIdent,
                        caseNumber,
                        admissionDate,
                        dischargeDate,
                        mainCaseFl
                );
                caseDto.setPatientDateOfBirth(dto.getPatientDateOfBirth());
                caseDto.setPatientFirstName(dto.getPatientFirstName());
                caseDto.setPatientNumber(dto.getPatientNumber());
                caseDto.setPatientSecName(dto.getPatientSecName());
                caseDto.setPatientId(dto.getPatientId());
                dto.addCase(caseDto);
            }
            final Long processId = item[12] == null ? null : ((Number) item[12]).longValue();
            if (processId != null) {
                DocumentSearchProcessItemDto processDto = dto.getProcessById(processId);
                if (processDto == null) {
                    processDto = new DocumentSearchProcessItemDto();
                    processDto.setProcessId(item[12] == null ? null : ((Number) item[12]).longValue());
                    processDto.setWorkflowNumber(item[13] == null ? null : ((Number) item[13]).longValue());
                    processDto.setPatientId(item[15] == null ? null : ((Number) item[15]).longValue());
                    processDto.setPatientNumber((String) item[16]);
                    processDto.setPatientSecName((String) item[17]);
                    processDto.setPatientFirstName((String) item[18]);
                    processDto.setPatientDateOfBirth((Date) item[19]);
                    //                dto.caseId = item[7] == null ? null : ((Number) item[7]).longValue();
                    //                dto.hospitalIdent = (String) item[8];
                    //                dto.caseNumber = (String) item[9];
                    //                dto.admissionDate = (Date) item[10];
                    //                dto.dischargeDate = (Date) item[11];
                    //result.put(processId, dto);
                    dto.addProcess(processDto);
                }
                final Long pcCaseId = item[14] == null ? null : ((Number) item[14]).longValue();
                if (pcCaseId != null) {
                    final boolean mainCaseFl = toBool(item[20]);
                    final CaseStatusEn caseStatus = CaseStatusEn.valueOf((String) item[21]);
                    final String hospitalIdent = (String) item[22];
                    final String caseNumber = (String) item[23];
                    final Date admissionDate = (Date) item[24];
                    final Date dischargeDate = (Date) item[25];
                    final DocumentSearchCaseItemDto caseDto = new DocumentSearchCaseItemDto(
                            caseId,
                            caseStatus,
                            hospitalIdent,
                            caseNumber,
                            admissionDate,
                            dischargeDate,
                            mainCaseFl
                    );
                    caseDto.setPatientDateOfBirth(processDto.getPatientDateOfBirth());
                    caseDto.setPatientFirstName(processDto.getPatientFirstName());
                    caseDto.setPatientNumber(processDto.getPatientNumber());
                    caseDto.setPatientSecName(processDto.getPatientSecName());
                    caseDto.setPatientId(processDto.getPatientId());

                    processDto.addCase(caseDto);
                }
            }
//            if (processId != null) {
//                final boolean mainCaseFl = (Boolean) item[12];
//                final Long workflowNumber = item[13] == null ? null : ((Number) item[13]).longValue();
//                dto.addProcess(new DocumentSearchProcess(
//                        processId,
//                        workflowNumber,
//                        mainCaseFl
//                ));
//            }
            //dto.processes = item[9] == null ? null : ((Number) item[9]).intValue();
            //dto.processesAll = item[9] == null ? null : ((Number) item[9]).intValue();
            //dto.processesMain = item[9] == null ? null : ((Number) item[9]).intValue();
            //dto.processId = item[9] == null ? null : ((Number) item[9]).longValue();
            //dto.workflowNumber = item[10] == null ? null : ((Number) item[10]).longValue();
            //result.add(dto);
        }

        ArrayList<DocumentSearchPatientItemDto> newResult = new ArrayList<>(result.values());
        for (DocumentSearchPatientItemDto itemPat : newResult) {
            for (DocumentSearchProcessItemDto item : itemPat.getProcesses()) {
                DocumentSearchCaseItemDto mainCase = item.getMainCase();
                if (mainCase != null) {
                    item.setCaseId(mainCase.getCaseId());
                    item.setHospitalIdent(mainCase.getHospitalIdent());
                    item.setCaseNumber(mainCase.getCaseNumber());
                    item.setAdmissionDate(mainCase.getAdmissionDate());
                    item.setDischargeDate(mainCase.getDischargeDate());
                    item.setCaseStatusEn(mainCase.getCaseStatusEn());
                }
            }
            DocumentSearchCaseItemDto cs = null;
            if (itemPat.getCases().size() == 1) {
                cs = itemPat.getCases().iterator().next();
                itemPat.setCaseId(cs.getCaseId());
                itemPat.setHospitalIdent(cs.getHospitalIdent());
                itemPat.setCaseNumber(cs.getCaseNumber());
                itemPat.setAdmissionDate(cs.getAdmissionDate());
                itemPat.setDischargeDate(cs.getDischargeDate());
                itemPat.setCaseStatusEn(cs.getCaseStatusEn());
                //}
//                DocumentSearchProcessItemDto process = itemPat.getProcesses().iterator().next();
//                DocumentSearchCase mainCase = process.getMainCase();
//                if (mainCase != null) {
//                    if (cs.caseId.equals(mainCase.caseId)) {
//                        itemPat.processId = process.processId;
//                        itemPat.processNumber = process.processNumber;                        
//                    }
//                }
            }

            List<DocumentSearchProcessItemDto> listOfProcesses = new ArrayList<>();
            if (cs != null) {
                for (DocumentSearchProcessItemDto process : itemPat.getProcesses()) {
                    DocumentSearchCaseItemDto mainCase = process.getMainCase();
                    if (mainCase != null) {
                        if (cs.getCaseId().equals(mainCase.getCaseId())) {
                            listOfProcesses.add(process);
                        }
                    }
                }
            }

            DocumentSearchProcessItemDto proc = null;
            //if (listOfProcesses.size() == 1) {
            if (!listOfProcesses.isEmpty()) {
                proc = listOfProcesses.iterator().next();
            } else {
                if (!itemPat.getProcesses().isEmpty()) {
                    proc = itemPat.getProcesses().iterator().next();
                }
            }

            if (proc != null) {
                itemPat.setProcessId(proc.getProcessId());
                itemPat.setWorkflowNumber(proc.getWorkflowNumber());
            }

            for (DocumentSearchCaseItemDto item : itemPat.getCases()) {
                for (DocumentSearchProcessItemDto itemProc : itemPat.getProcesses()) {
                    DocumentSearchCaseItemDto res = itemProc.getCaseById(item.getCaseId());
                    if (res != null) {
                        item.addProcess(itemProc);
                    }
                }
            }
        }
        return newResult;
    }

    public List<DocumentSearchProcessItemDto> findProcessesForDocumentImport(final String pWorkflowNumber, final int pMaxEntries) {
        final String workflowNumber = pWorkflowNumber == null ? "" : pWorkflowNumber.trim();
        final int limit = pMaxEntries <= 0 ? 10 : pMaxEntries;

        String sql = String.format("SELECT TMP.*, CS.ID CASE_ID, PC_CS.MAIN_CASE_FL, CS.CS_STATUS_EN, CS.CS_HOSPITAL_IDENT, CS.CS_CASE_NUMBER, CSD.ADMISSION_DATE, CSD.DISCHARGE_DATE FROM ( "
                + "SELECT "
                + " PC.ID PROCESS_ID, "
                + " PC.WORKFLOW_NUMBER, "
                + " PAT.ID PAT_ID, "
                + " PAT.PAT_NUMBER, "
                + " PAT.PAT_SEC_NAME, "
                + " PAT.PAT_FIRST_NAME, "
                + " PAT.PAT_DATE_OF_BIRTH "
                //                + " CS.ID CS_ID, "
                //                + " CS.CS_HOSPITAL_IDENT, "
                //                + " CS.CS_CASE_NUMBER, "
                //                + " CSD.ADMISSION_DATE, "
                //                + " CSD.DISCHARGE_DATE "
                + " FROM ( "
                + "   SELECT "
                + "   %s * "
                + "   FROM T_WM_PROCESS PC"
                + "   WHERE CAST(PC.WORKFLOW_NUMBER AS VARCHAR(255)) LIKE '%s' "
                + "   %s "
                + "   ORDER BY PC.WORKFLOW_NUMBER "
                + " ) PC "
                + " INNER JOIN T_PATIENT PAT ON PAT.ID = PC.T_PATIENT_ID "
                + " LEFT JOIN T_WM_PROCESS_T_CASE PC_CS ON PC_CS.T_WM_PROCESS_ID = PC.ID AND (PC_CS.MAIN_CASE_FL = 1 OR PC_CS.MAIN_CASE_FL IS NULL) "
                //+ " LEFT JOIN T_CASE CS ON CS.ID = PC_CS.T_CASE_ID "
                //+ " LEFT JOIN T_CASE_DETAILS CSD ON CSD.T_CASE_ID = CS.ID AND (CSD.ACTUAL_FL IS NULL OR CSD.ACTUAL_FL = 1) AND (CSD.LOCAL_FL IS NULL OR CSD.LOCAL_FL = 1) "
                + " ) TMP "
                + " LEFT JOIN T_WM_PROCESS_T_CASE PC_CS ON PC_CS.T_WM_PROCESS_ID = PROCESS_ID "
                + " LEFT JOIN T_CASE CS ON CS.ID = PC_CS.T_CASE_ID "
                + " LEFT JOIN T_CASE_DETAILS CSD ON CSD.T_CASE_ID = CS.ID AND (CSD.ACTUAL_FL IS NULL OR CSD.ACTUAL_FL = 1) AND (CSD.LOCAL_FL IS NULL OR CSD.LOCAL_FL = 1) ",
                (isSqlSrv() ? " TOP " + limit : ""), //LIMIT FOR MSSQL
                workflowNumber.replace("*", "%") + "%", //WHERE
                (isOracle() ? " AND ROWNUM <= " + limit : "") //LIMIT FOR ORACLE
        );

        final Map<Long, DocumentSearchProcessItemDto> result = new HashMap<>();

        Query query = getEntityManager().createNativeQuery(sql);
        List<Object[]> list = query.getResultList();
//        List<DocumentSearchCaseItemDto> result = new ArrayList<>();
        for (Object[] item : list) {
            Long processId = item[0] == null ? null : ((Number) item[0]).longValue();
            final DocumentSearchProcessItemDto dto;
            if (result.get(processId) == null) {
                dto = new DocumentSearchProcessItemDto();
                dto.setProcessId(item[0] == null ? null : ((Number) item[0]).longValue());
                dto.setWorkflowNumber(item[1] == null ? null : ((Number) item[1]).longValue());
                //dto.caseStatusEn = CaseStatusEn.valueOf((String) item[1]);
                dto.setPatientId(item[2] == null ? null : ((Number) item[2]).longValue());
                dto.setPatientNumber((String) item[3]);
                dto.setPatientSecName((String) item[4]);
                dto.setPatientFirstName((String) item[5]);
                dto.setPatientDateOfBirth((Date) item[6]);
//                dto.caseId = item[7] == null ? null : ((Number) item[7]).longValue();
//                dto.hospitalIdent = (String) item[8];
//                dto.caseNumber = (String) item[9];
//                dto.admissionDate = (Date) item[10];
//                dto.dischargeDate = (Date) item[11];
                result.put(processId, dto);
            } else {
                dto = result.get(processId);
            }
            final Long caseId = item[7] == null ? null : ((Number) item[7]).longValue();
            if (caseId != null) {
                //final boolean mainCaseFl = (Boolean) item[8];
                final boolean mainCaseFl = toBool(item[8]);
                final CaseStatusEn caseStatus = CaseStatusEn.valueOf((String) item[9]);
                final String hospitalIdent = (String) item[10];
                final String caseNumber = (String) item[11];
                final Date admissionDate = (Date) item[12];
                final Date dischargeDate = (Date) item[13];
                final DocumentSearchCaseItemDto caseDto = new DocumentSearchCaseItemDto(
                        caseId,
                        caseStatus,
                        hospitalIdent,
                        caseNumber,
                        admissionDate,
                        dischargeDate,
                        mainCaseFl
                );
                caseDto.setPatientDateOfBirth(dto.getPatientDateOfBirth());
                caseDto.setPatientFirstName(dto.getPatientFirstName());
                caseDto.setPatientNumber(dto.getPatientNumber());
                caseDto.setPatientSecName(dto.getPatientSecName());
                caseDto.setPatientId(dto.getPatientId());
                dto.addCase(caseDto);
            }
            //dto.processes = item[9] == null ? null : ((Number) item[9]).intValue();
            //dto.processesAll = item[9] == null ? null : ((Number) item[9]).intValue();
            //dto.processesMain = item[9] == null ? null : ((Number) item[9]).intValue();
            //dto.processId = item[9] == null ? null : ((Number) item[9]).longValue();
            //dto.workflowNumber = item[10] == null ? null : ((Number) item[10]).longValue();
            //result.add(dto);
        }

        ArrayList<DocumentSearchProcessItemDto> newResult = new ArrayList<>(result.values());
        for (DocumentSearchProcessItemDto item : newResult) {
            DocumentSearchCaseItemDto mainCase = item.getMainCase();
            if (mainCase != null) {
                item.setCaseId(mainCase.getCaseId());
                item.setHospitalIdent(mainCase.getHospitalIdent());
                item.setCaseNumber(mainCase.getCaseNumber());
                item.setAdmissionDate(mainCase.getAdmissionDate());
                item.setDischargeDate(mainCase.getDischargeDate());
                item.setCaseStatusEn(mainCase.getCaseStatusEn());
            }
        }
        return newResult;
    }

    public List<DocumentSearchCaseItemDto> findCasesForDocumentImport(final String pCaseNumber, final int pMaxEntries) {
        return findCasesForDocumentImport(0L, null, pCaseNumber, pMaxEntries);
    }

    public List<DocumentSearchCaseItemDto> findCasesForDocumentImport(final long pCaseId, final String pHospitalIdent, final String pCaseNumber, final int pMaxEntries) {
        final String hospitalIdent = pHospitalIdent == null ? null : pHospitalIdent.trim();
        final String caseNumber = pCaseNumber == null ? "" : pCaseNumber.trim();
        //final String workflowNumber = pWorkflowNumber == null ? "" : pWorkflowNumber.trim();
        //final String patientNumber = pPatientNumber == null ? "" : pPatientNumber.trim();
        final int limit = pMaxEntries <= 0 ? 10 : pMaxEntries;

        String sql = String.format("SELECT TMP.*, PC.ID PROCESS_ID, PC_CS.MAIN_CASE_FL, PC.WORKFLOW_NUMBER FROM ( "
                + " SELECT "
                + " CS.ID CS_ID, "
                + " CS.CS_STATUS_EN, "
                + " CS.CS_HOSPITAL_IDENT, "
                + " CS.CS_CASE_NUMBER, "
                + " PAT.ID PAT_ID, "
                + " PAT.PAT_NUMBER, "
                + " PAT.PAT_SEC_NAME, "
                + " PAT.PAT_FIRST_NAME, "
                + " PAT.PAT_DATE_OF_BIRTH, "
                + " CSD.ADMISSION_DATE, "
                + " CSD.DISCHARGE_DATE "
                //+ " (SELECT COUNT(*) FROM T_WM_PROCESS_T_CASE PC_CS WHERE PC_CS.T_CASE_ID = CS.ID) PROCESSES_ALL,"
                //+ " (SELECT COUNT(*) FROM T_WM_PROCESS_T_CASE PC_CS WHERE PC_CS.T_CASE_ID = CS.ID AND PC_CS.MAIN_CASE_FL = 1) PROCESSES_MAIN"
                // + " (SELECT COUNT(*) FROM T_WM_PROCESS_T_CASE PC_CS WHERE PC_CS.T_CASE_ID = CS.ID AND PC_CS.MAIN_CASE_FL = 1) PROCESSES"
                + " FROM ( "
                + "   SELECT "
                + "   %s * "
                + "   FROM T_CASE CS"
                + "   WHERE CS.CS_CASE_NUMBER LIKE '%s' "
                + "   %s "
                + "   %s "
                + "   %s "
                + "   ORDER BY CS.CS_HOSPITAL_IDENT, CS.CS_CASE_NUMBER "
                + " ) CS "
                + " INNER JOIN T_PATIENT PAT ON PAT.ID = CS.T_PATIENT_ID "
                + " LEFT JOIN T_CASE_DETAILS CSD ON CSD.T_CASE_ID = CS.ID AND (CSD.ACTUAL_FL IS NULL OR CSD.ACTUAL_FL = 1) AND (CSD.LOCAL_FL IS NULL OR CSD.LOCAL_FL = 1)"
                + " ) TMP "
                + " LEFT JOIN T_WM_PROCESS_T_CASE PC_CS ON PC_CS.T_CASE_ID = CS_ID "
                + " LEFT JOIN T_WM_PROCESS PC ON PC.ID = PC_CS.T_WM_PROCESS_ID ",
                (isSqlSrv() ? " TOP " + limit : ""), //LIMIT FOR MSSQL
                caseNumber.replace("*", "%") + "%", //WHERE
                (hospitalIdent == null ? "" : " AND CS.CS_HOSPITAL_IDENT = '" + hospitalIdent + "' "),
                (pCaseId == 0L ? "" : " AND CS.ID = " + pCaseId + " "),
                (isOracle() ? " AND ROWNUM <= " + limit : "") //LIMIT FOR ORACLE
        );
        //+ " ORDER BY CS_CASE_NUMBER, WORKFLOW_NUMBER";

        final Map<Long, DocumentSearchCaseItemDto> result = new HashMap<>();

        Query query = getEntityManager().createNativeQuery(sql);
        List<Object[]> list = query.getResultList();
//        List<DocumentSearchCaseItemDto> result = new ArrayList<>();
        for (Object[] item : list) {
            Long caseId = item[0] == null ? null : ((Number) item[0]).longValue();
            final DocumentSearchCaseItemDto dto;
            if (result.get(caseId) == null) {
                dto = new DocumentSearchCaseItemDto();
                dto.setCaseId(item[0] == null ? null : ((Number) item[0]).longValue());
                dto.setCaseStatusEn(CaseStatusEn.valueOf((String) item[1]));
                dto.setHospitalIdent((String) item[2]);
                dto.setCaseNumber((String) item[3]);
                dto.setPatientId(item[4] == null ? null : ((Number) item[4]).longValue());
                dto.setPatientNumber((String) item[5]);
                dto.setPatientSecName((String) item[6]);
                dto.setPatientFirstName((String) item[7]);
                dto.setPatientDateOfBirth((Date) item[8]);
                dto.setAdmissionDate((Date) item[9]);
                dto.setDischargeDate((Date) item[10]);
                result.put(caseId, dto);
            } else {
                dto = result.get(caseId);
            }
            final Long processId = item[11] == null ? null : ((Number) item[11]).longValue();
            if (processId != null) {
                //final boolean mainCaseFl = (Boolean) item[12];
                final boolean mainCaseFl = toBool(item[12]);
                final Long workflowNumber = item[13] == null ? null : ((Number) item[13]).longValue();
                DocumentSearchProcessItemDto dtoProc = new DocumentSearchProcessItemDto(
                        processId,
                        workflowNumber,
                        mainCaseFl
                );
                dtoProc.setCaseId(dto.getCaseId());
                dtoProc.setHospitalIdent(dto.getHospitalIdent());
                dtoProc.setCaseNumber(dto.getCaseNumber());
                dtoProc.setPatientId(dto.getPatientId());
                dtoProc.setPatientNumber(dto.getPatientNumber());
                dtoProc.setPatientSecName(dto.getPatientSecName());
                dtoProc.setPatientFirstName(dto.getPatientFirstName());
                dtoProc.setPatientDateOfBirth(dto.getPatientDateOfBirth());
                dtoProc.setAdmissionDate(dto.getAdmissionDate());
                dtoProc.setDischargeDate(dto.getDischargeDate());
                dto.addProcess(dtoProc);
            }
            //dto.processes = item[9] == null ? null : ((Number) item[9]).intValue();
            //dto.processesAll = item[9] == null ? null : ((Number) item[9]).intValue();
            //dto.processesMain = item[9] == null ? null : ((Number) item[9]).intValue();
            //dto.processId = item[9] == null ? null : ((Number) item[9]).longValue();
            //dto.workflowNumber = item[10] == null ? null : ((Number) item[10]).longValue();
            //result.add(dto);
        }
        return new ArrayList<>(result.values());
    }

    public List<DocumentSearchPatientItemDto> findPatientsForDocumentImportByInsuranceNumber(final String pPatientInsuranceNumber, final int pMaxEntries) {
        final String patientInsuranceNumber = pPatientInsuranceNumber == null ? "" : pPatientInsuranceNumber.trim();

        final int limit = pMaxEntries <= 0 ? 10 : pMaxEntries;

        String sql = String.format("SELECT TMP.*, PC.ID PROCESS_ID, PC_CS.MAIN_CASE_FL, PC.WORKFLOW_NUMBER FROM ( "
                + " SELECT "
                + " CS.ID CS_ID, "
                + " CS.CS_STATUS_EN, "
                + " CS.CS_HOSPITAL_IDENT, "
                + " CS.CS_CASE_NUMBER, "
                + " PAT.ID PAT_ID, "
                + " PAT.PAT_NUMBER, "
                + " PAT.PAT_SEC_NAME, "
                + " PAT.PAT_FIRST_NAME, "
                + " PAT.PAT_DATE_OF_BIRTH, "
                + " CSD.ADMISSION_DATE, "
                + " CSD.DISCHARGE_DATE, "
                + " CS.INSURANCE_NUMBER_PATIENT "
                //                + " (SELECT COUNT(*) FROM T_WM_PROCESS_T_CASE PC_CS WHERE PC_CS.T_CASE_ID = CS.ID) PROCESSES_ALL,"
                //                + " (SELECT COUNT(*) FROM T_WM_PROCESS_T_CASE PC_CS WHERE PC_CS.T_CASE_ID = CS.ID AND PC_CS.MAIN_CASE_FL = 1) PROCESSES_MAIN"
                //                 + "(SELECT COUNT(*) FROM T_WM_PROCESS_T_CASE PC_CS WHERE PC_CS.T_CASE_ID = CS.ID AND PC_CS.MAIN_CASE_FL = 1) PROCESSES"
                + " FROM ( "
                + "   SELECT "
                + "   %s * "
                + "   FROM T_CASE CS"
                + "   WHERE CS.INSURANCE_NUMBER_PATIENT LIKE '%s' "
                + "   ORDER BY CS.CS_HOSPITAL_IDENT, CS.CS_CASE_NUMBER "
                + " ) CS "
                + " INNER JOIN T_PATIENT PAT ON PAT.ID = CS.T_PATIENT_ID "
                + " LEFT JOIN T_CASE_DETAILS CSD ON CSD.T_CASE_ID = CS.ID AND (CSD.ACTUAL_FL IS NULL OR CSD.ACTUAL_FL = 1) AND (CSD.LOCAL_FL IS NULL OR CSD.LOCAL_FL = 1)"
                + " ) TMP "
                + " LEFT JOIN T_WM_PROCESS_T_CASE PC_CS ON PC_CS.T_CASE_ID = CS_ID "
                + " LEFT JOIN T_WM_PROCESS PC ON PC.ID = PC_CS.T_WM_PROCESS_ID ",
                (isSqlSrv() ? " TOP " + limit : ""), //LIMIT FOR MSSQL
                patientInsuranceNumber.replace("*", "%") + "%", //WHERE

                (isOracle() ? " AND ROWNUM <= " + limit : "") //LIMIT FOR ORACLE
        );
        //+ " ORDER BY CS_CASE_NUMBER, WORKFLOW_NUMBER";

        final Map<Long, DocumentSearchPatientItemDto> result = new HashMap<>();

        Query query = getEntityManager().createNativeQuery(sql);
        List<Object[]> list = query.getResultList();
//        List<DocumentSearchCaseItemDto> result = new ArrayList<>();
        for (Object[] item : list) {
            Long caseId = item[0] == null ? null : ((Number) item[0]).longValue();
            final DocumentSearchPatientItemDto dto;
            if (result.get(caseId) == null) {
                dto = new DocumentSearchPatientItemDto();
                dto.setCaseId(item[0] == null ? null : ((Number) item[0]).longValue());
                dto.setCaseStatusEn(CaseStatusEn.valueOf((String) item[1]));
                dto.setHospitalIdent((String) item[2]);
                dto.setCaseNumber((String) item[3]);
                dto.setPatientId(item[4] == null ? null : ((Number) item[4]).longValue());
                dto.setPatientNumber((String) item[5]);
                dto.setPatientSecName((String) item[6]);
                dto.setPatientFirstName((String) item[7]);
                dto.setPatientDateOfBirth((Date) item[8]);
                dto.setAdmissionDate((Date) item[9]);
                dto.setDischargeDate((Date) item[10]);
                dto.setInsuranceNumberPatient((String) item[11]);
                result.put(caseId, dto);
            } else {
                dto = result.get(caseId);
            }
            final Long processId = item[12] == null ? null : ((Number) item[12]).longValue();
            if (processId != null) {
                //final boolean mainCaseFl = (Boolean) item[12];
                final boolean mainCaseFl = toBool(item[13]);
                final Long workflowNumber = item[14] == null ? null : ((Number) item[14]).longValue();
                DocumentSearchProcessItemDto dtoProc = new DocumentSearchProcessItemDto(
                        processId,
                        workflowNumber,
                        mainCaseFl
                );
                dtoProc.setCaseId(dto.getCaseId());
                dtoProc.setHospitalIdent(dto.getHospitalIdent());
                dtoProc.setCaseNumber(dto.getCaseNumber());
                dtoProc.setPatientId(dto.getPatientId());
                dtoProc.setPatientNumber(dto.getPatientNumber());
                dtoProc.setPatientSecName(dto.getPatientSecName());
                dtoProc.setPatientFirstName(dto.getPatientFirstName());
                dtoProc.setPatientDateOfBirth(dto.getPatientDateOfBirth());
                dtoProc.setAdmissionDate(dto.getAdmissionDate());
                dtoProc.setDischargeDate(dto.getDischargeDate());
                dto.addProcess(dtoProc);
            }

        }
        ArrayList<DocumentSearchPatientItemDto> newResult = new ArrayList<>(result.values());
        return new ArrayList<>(result.values());
    }

    protected Boolean toBool(final Object pValue) {
        if (pValue == null) {
            return (Boolean) null;
        }
        if (pValue instanceof Boolean) {
            //Microsoft SQL Server
            return (Boolean) pValue;
        }
        if (pValue instanceof Number) {
            //Oracle
            final Number number = (Number) pValue;
            if (number.byteValue() == 1) {
                return true;
            }
            if (number.byteValue() == 0) {
                return false;
            }
            LOG.log(Level.SEVERE, "This numeric value cannot be identified as true (1) or false (0): {0}. Return null instead!", number.longValue());
            return (Boolean) null;
        }
        LOG.log(Level.SEVERE, "Class of type {0} is unknown, so I cannot convert value ''{1}'' to boolean. Return null instead!", new Object[]{pValue.getClass().getSimpleName(), String.valueOf(pValue)});
        return (Boolean) null;
        /*
      if (isSqlSrv()) {
        return "'" + escapeSQL(pDate.replace("-", "")) + "'";
      }
      return "TO_DATE('" + escapeSQL(pDate) + "', 'YYYY-MM-DD')";
         */
    }

    /**
     *
     * @param pProcess hospital case
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException error
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public synchronized void cancelProcess(final TWmProcess pProcess) throws CpxIllegalArgumentException {
        if (pProcess == null) {
            LOG.log(Level.WARNING, "pProcess is null!");
            return;
        }
        if (pProcess.id == 0L) {
            LOG.log(Level.WARNING, "id is equal to 0, maybe this workflow has not been persisted yet!");
            return;
        }
        LOG.log(Level.INFO, "Procedure to cancel workflow case {0}", pProcess.getWorkflowNumber());
        if (pProcess.getProcessCancellation()) {
            throw new CpxIllegalArgumentException("Vorgang ist schon storniert: " + pProcess.getWorkflowNumber());
        } else {

            pProcess.setProcessCancellation(true);
            flush();
        }

    }

    /**
     *
     * @param pProcess hospital case
     * @throws de.lb.cpx.exceptions.CpxIllegalArgumentException error
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void unCancelProcess(TWmProcess pProcess) throws CpxIllegalArgumentException {

        if (pProcess == null) {
            LOG.log(Level.WARNING, "workflow is null!");
            return;
        }
        if (pProcess.getId() == 0L) {
            LOG.log(Level.WARNING, "id is equal to 0, maybe this case has not been persisted yet!");
            return;
        }
        final String processSignature = pProcess.getWorkflowSignature();
        LOG.log(Level.INFO, "Procedure to cancel workflow {0}", processSignature);
        if (!pProcess.getProcessCancellation()) {
            throw new CpxIllegalArgumentException("Vorgang ist schon unstorniert: " + processSignature);
        } else {
            pProcess.setProcessCancellation(false);
        }

        flush();
    }

    /**
     * @param pCaseId case database id to check if there are kain inka messages
     * that could indicate a 6 week deadline
     * @return indicator if case contains one or more deadline specific keys for
     * the 6 week deadline
     */
    public boolean hasPossibleKain6WeekDeadline(Long pCaseId) {
        /*
        SELECT Count(*) FROM T_P301_KAIN_INKA kain WHERE EXISTS(SELECT pvv.* FROM T_P301_KAIN_INKA_PVV pvv 
        WHERE pvv.INFORMATION_KEY_30 LIKE 'SF000' OR pvv.INFORMATION_KEY_30 LIKE 'PF000' AND kain.id = pvv.T_P301_KAIN_INKA_ID) AND kain.T_CASE_ID = '29668001';
         */
        final EntityManager em = getEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        final CriteriaQuery<Long> mainQuery = cb.createQuery(Long.class);
        final Root<TP301KainInka> mainFrom = mainQuery.from(TP301KainInka.class);
        //subquery
        Subquery<TP301KainInkaPvv> subQuery = mainQuery.subquery(TP301KainInkaPvv.class);
        final Root<TP301KainInkaPvv> subFrom = subQuery.from(TP301KainInkaPvv.class);

        //subquery where
        subQuery.select(subFrom);
        Predicate ors = cb.or(cb.like(subFrom.get(TP301KainInkaPvv_.INFORMATION_KEY30), Tp301Key30En.pf000.getViewId()),
                cb.like(subFrom.get(TP301KainInkaPvv_.INFORMATION_KEY30), Tp301Key30En.sf000.getViewId()),
                cb.like(subFrom.get(TP301KainInkaPvv_.INFORMATION_KEY30), Tp301Key30En.kp000.getViewId()),
                cb.like(subFrom.get(TP301KainInkaPvv_.INFORMATION_KEY30), Tp301Key30En.fv000.getViewId()),
                cb.like(subFrom.get(TP301KainInkaPvv_.INFORMATION_KEY30), Tp301Key30En.kl000.getViewId()));
        subQuery.where(cb.and(ors, cb.equal(subFrom.get(TP301KainInkaPvv_.p301KainInkaId), mainFrom.get(TP301KainInka_.ID))));

        mainQuery.select(cb.count(mainFrom));
        mainQuery.where(cb.equal(mainFrom.get(TP301KainInka_.T_CASE_ID), pCaseId),
                cb.exists(subQuery));

        final TypedQuery<Long> tq = em.createQuery(mainQuery);
        return tq.getSingleResult() > 0;
    }

}
