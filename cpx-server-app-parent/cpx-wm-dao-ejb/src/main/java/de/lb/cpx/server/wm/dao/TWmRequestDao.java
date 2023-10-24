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

import de.lb.cpx.server.dao.AbstractCpxDao;
import de.lb.cpx.wm.model.TWmMdkAuditReasons;
import de.lb.cpx.wm.model.TWmRequest;
import de.lb.cpx.wm.model.TWmRequest_;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Hibernate;

/**
 *
 * @author Husser
 */
@Stateless
public class TWmRequestDao extends AbstractCpxDao<TWmRequest> {

    private static final Logger LOG = Logger.getLogger(TWmRequestDao.class.getName());

    public TWmRequestDao() {
        super(TWmRequest.class);
    }

    public List<TWmRequest> findRequestsByInstitution(String ikNumber) {
        String sql = "select r from TWmRequest r where r.ikNumber = :ikNumber";
        TypedQuery<TWmRequest> query = getEntityManager().createQuery(sql, TWmRequest.class);
        query.setParameter("ikNumber", ikNumber);
        return query.getResultList();
    }

    public List<TWmRequest> findAllForProcess(long pProcessId) {
        LOG.log(Level.FINE, "Find all requests for process with id " + pProcessId);
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmRequest> query = criteriaBuilder.createQuery(TWmRequest.class);

        Root<TWmRequest> from = query.from(TWmRequest.class);
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
        query.where(criteriaBuilder.equal(from.get(TWmRequest_.processHospital), pProcessId));

        TypedQuery<TWmRequest> criteriaQuery = getEntityManager().createQuery(query);
        List<TWmRequest> result = criteriaQuery.getResultList();
        return result;
    }

    public long getLatestRequestId(final long pProcessId) {
        String sql = "SELECT ID FROM VIEW_LATEST_REQUEST WHERE T_WM_PROCESS_HOSPITAL_ID = :processId";
        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter("processId", pProcessId);
        final Number latestRequestId = getSingleResultOrNull(query);
        return latestRequestId == null ? 0L : latestRequestId.longValue();
    }

    public TWmRequest getLatestRequest(long pProcessId) {
        final long latestRequestId = getLatestRequestId(pProcessId);

        if (latestRequestId == 0L) {
            LOG.log(Level.FINER, "No latest Request found, for ProcessId: {0} !", pProcessId);
            return null;
        }

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmRequest> query = criteriaBuilder.createQuery(TWmRequest.class);

        Root<TWmRequest> from = query.from(TWmRequest.class);
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
        query.where(criteriaBuilder.equal(from.get(TWmRequest_.ID), latestRequestId));

        TypedQuery<TWmRequest> criteriaQuery = getEntityManager().createQuery(query);
        return getSingleResultOrNull(criteriaQuery);
    }
    public TWmRequest getLatestRequestForRequestType(Long pProcessId,Integer pType){
        if(pProcessId == null){
            return null;
        }
        if(pType == null){
            return null;
        }
        /*SELECT * FROM T_WM_REQUEST WHERE ID = (
        SELECT MAX(ID) FROM T_WM_REQUEST 
        WHERE T_WM_PROCESS_HOSPITAL_ID = 593 
        AND REQUEST_TYPE = 2);*/
        String sql = "SELECT * FROM T_WM_REQUEST WHERE ID = (\n" +
        "SELECT MAX(ID) FROM T_WM_REQUEST \n" +
        "WHERE T_WM_PROCESS_HOSPITAL_ID = :processId \n" +
        "AND REQUEST_TYPE = :typeId)";
        Query query = getEntityManager().createNativeQuery(sql,TWmRequest.class);
        query.setParameter("processId", pProcessId);
        query.setParameter("typeId", pType);
        return getSingleResultOrNull(query);
    }
    public List<TWmMdkAuditReasons> findAuditReasons4case(Long pCaseId) {
         final String sql = "select ars.* from t_wm_request req inner join t_wm_process_t_case wcs on wcs.T_WM_PROCESS_ID = req.id "
        + "inner join T_WM_AUDIT_REASONS ars on ars.T_WM_REQUEST_ID = req.id "
        + "where wcs.T_CASE_ID = " + String.valueOf(pCaseId);

        final Query query = getSession().createNativeQuery(sql, TWmMdkAuditReasons.class);
        
        return query.getResultList();    
    }

   public long getLatestRequestId4CaseId(final long pCaseId) {
        String sql = "select vlr.ID from VIEW_LATEST_REQUEST vlr inner join t_wm_process_t_case wcs on vlr.T_WM_PROCESS_HOSPITAL_ID = wcs.T_WM_PROCESS_ID "
            + "where wcs.T_CASE_ID = :pCaseId";
        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter("pCaseId", pCaseId);
        final Number latestRequestId = getSingleResultOrNull(query);
        return latestRequestId == null ? 0L : latestRequestId.longValue();
    }
    public TWmRequest getLatestRequest4Case(long pCaseId){
        final long latestRequestId = getLatestRequestId4CaseId(pCaseId);

        if (latestRequestId == 0L) {
            LOG.log(Level.FINER, "No latest Request found, for pCaseId: {0} !", pCaseId);
            return null;
        }

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmRequest> query = criteriaBuilder.createQuery(TWmRequest.class);

        Root<TWmRequest> from = query.from(TWmRequest.class);
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
        query.where(criteriaBuilder.equal(from.get(TWmRequest_.ID), latestRequestId));

        TypedQuery<TWmRequest> criteriaQuery = getEntityManager().createQuery(query);
        TWmRequest retRequest =  getSingleResultOrNull(criteriaQuery);
        if(retRequest != null){
            Set<TWmMdkAuditReasons> audit = retRequest.getAuditReasons();
                if(audit != null){
                    audit.forEach((au) -> {
                        Hibernate.initialize(au);
                });
            }
        }
        return retRequest;

    }
}
