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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.wm.dao;

import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.server.dao.AbstractCpxDao;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmProcessCase;
import de.lb.cpx.wm.model.TWmProcessCase_;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;

/**
 * Dao to access database access to the T_WM_PROCESS_CASE table
 *
 * @author wilde
 */
@Stateless
@SuppressWarnings("unchecked")
public class TWmProcessCaseDao extends AbstractCpxDao<TWmProcessCase> {

    private static final Logger LOG = Logger.getLogger(TWmProcessCaseDao.class.getName());

    public TWmProcessCaseDao() {
        super(TWmProcessCase.class);
    }

//    public TWmProcessCase getCurrentProcessByMainCase(TCase mainCase) {
//        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<TWmProcessCase> query = criteriaBuilder.createQuery(TWmProcessCase.class);
//        Root<TWmProcess> from = query.from(TWmProcess.class);
//        query.select(from.get(TWmProcessCase_.process));
//        query.where(criteriaBuilder.like(criteriaBuilder.lower(from.get(TWmProcessCase_.mainCase)), mainCase.getCsCaseNumber()));
//        List<TWmProcessCase> results = addEntityGraph(getEntityManager().createQuery(query)).getResultList();
//        return results.get(0);
//    }  
//    private Object addEntityGraph(TypedQuery<TWmProcessCase> createQuery) {
//        EntityGraph<TWmProcessCase> toFetch = getEntityManager().createEntityGraph(TWmProcessCase.class);
//        toFetch.addSubgraph(TWmProcessCase_.mainCase);
//        toFetch.addSubgraph(TWmProcessCase_.id);
//        toFetch.addSubgraph(TWmProcessCase_.process);
//        createQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
//        return createQuery;
//    }
//    public TWmProcessCase getCurrentProcessByMainCase(TCase mainCase) {
//        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<TWmProcessCase> query = criteriaBuilder.createQuery(TWmProcessCase.class);
//        Root<TWmProcess> from = query.from(TWmProcess.class);
//        query.select(from.get(TWmProcessCase_.process));
//        query.where(criteriaBuilder.like(criteriaBuilder.lower(from.get(TWmProcessCase_.mainCase)), mainCase.getCsCaseNumber()));
//        List<TWmProcessCase> results = addEntityGraph(getEntityManager().createQuery(query)).getResultList();
//        return results.get(0);
    public TWmProcess getCurrentProcessByMainCase(long hCaseId) {
        TypedQuery<TWmProcessCase> query = getEntityManager().createQuery(String.format("select pc.process from TWmProcessCase pc where pc.hosCase = %s", hCaseId), TWmProcessCase.class);
        TWmProcessCase result = getSingleResultOrNull(query);
        TWmProcess process = result == null ? null : result.getProcess();
        return process;
    }

    public TWmProcessCase getCurrentProcessCaseByMainCase(long hCaseId) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TWmProcessCase> cq = qb.createQuery(TWmProcessCase.class);
        Root<TWmProcessCase> from = cq.from(TWmProcessCase.class);
        cq.select((Selection<? extends TWmProcessCase>) TWmProcessCase_.process);
        cq.where(qb.equal(from.get(TWmProcessCase_.hosCase), hCaseId));
        return getSingleResultOrNull(getEntityManager().createQuery(cq));
    }

//        public List<TWmProcessCase> getProcessesOfMainCase(long hCaseId) {
//        List<TWmProcessCase> processes = null;
////        List<TWmProcess> processes = null;
//        TypedQuery<TWmProcessCase> query = getEntityManager().createQuery("select pc.process from TWmProcessCase pc where pc.hosCase = :hCaseId", TWmProcessCase.class);
//        query.setParameter("hCaseId", hCaseId);
//        return query.getResultList();
//    }
    public List<TWmProcess> getProcessesOfCase(long hCaseId, final boolean pIncludeCanceled) {
        //List<TWmProcess> processes = null;
        String qry = String.format("select pc.process from TWmProcessCase pc where pc.hosCase.id = %s", hCaseId);
        if (!pIncludeCanceled) {
            qry += " and pc.process.processCancellation = 0";
        }
        TypedQuery<TWmProcess> query = getEntityManager().createQuery(qry, TWmProcess.class);
//        query.setParameter("hCaseId", hCaseId);
        return query.getResultList();
//        query.getResultList().forEach(new Consumer<TWmProcess>() {
//            @Override
//            public void accept(TWmProcess t) {
////                processes.add(t.getProcess());
//                processes.add(t);
//            }
//        });
//        return processes;
    }
    public TWmProcess getLatestProcessForCase(long pHCaseId){
        List<TWmProcess> listOfProcesses = getProcessesOfCase(pHCaseId, false);
        if(listOfProcesses == null || listOfProcesses.isEmpty()){
            return null;
        }
        try{
            return  (TWmProcess) CollectionUtils.get(listOfProcesses, (listOfProcesses.size() - 1));
        }catch(IndexOutOfBoundsException | IllegalArgumentException ex){
            LOG.log(Level.WARNING, "can not find latest Process, reason: {0}", ex.getMessage());
            return null;
        }
//        return listOfProcesses.stream().reduce((t, u) -> {
//            return u;
//        }).get();
    }
    public List<TWmProcessCase> findAllForProcess(long pProcessId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmProcessCase> query = criteriaBuilder.createQuery(TWmProcessCase.class);

        Root<TWmProcessCase> from = query.from(TWmProcessCase.class);
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
        query.where(criteriaBuilder.equal(from.get(TWmProcessCase_.process), pProcessId));

        TypedQuery<TWmProcessCase> criteriaQuery = getEntityManager().createQuery(query);
        List<TWmProcessCase> result = criteriaQuery.getResultList();
        for (TWmProcessCase procCase : result) {
            Hibernate.initialize(procCase.getHosCase().getCaseDetails());
        }
        return result;
    }

    public TCase getMainCaseForProcess(long pProcessId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TCase> query = criteriaBuilder.createQuery(TCase.class);
        Root<TWmProcessCase> from = query.from(TWmProcessCase.class);
        query.select(from.get(TWmProcessCase_.hosCase));
        //add two Where-Conditions like csCaseNamber and equals hospitalIdent 
        query.where(criteriaBuilder.equal(from.get(TWmProcessCase_.process), pProcessId),
                criteriaBuilder.equal(from.get(TWmProcessCase_.mainCase), Boolean.TRUE));

//        TypedQuery<TCase> criteriaQuery = getEntityManager().createQuery(query);
//        List<TWmProcessCase> result = criteriaQuery.getResultList();
//        for(TWmProcessCase procCase : result){
//            Hibernate.initialize(procCase.getHosCase().getCaseDetails());
//        }
        TypedQuery<TCase> q = getEntityManager().createQuery(query);
        final List<TCase> res = q.getResultList();
        final TCase cs;
        if (res.isEmpty()) {
            LOG.log(Level.SEVERE, "No MainCase found for Process id: " + pProcessId);
            cs = null;
        } else {
            cs = res.get(0);
        }
        return cs;
    }

    public TWmProcess getProcessForKisDetails(long pCaseDetailsId) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TWmProcess> cq = qb.createQuery(TWmProcess.class);
        Root<TWmProcessCase> from = cq.from(TWmProcessCase.class);
        cq.select(from.get(TWmProcessCase_.PROCESS));
        cq.where(qb.equal(from.get(TWmProcessCase_.KIS_DETAILS), pCaseDetailsId));
        return getSingleResultOrNull(getEntityManager().createQuery(cq));
    }

    public TCaseDetails findCurrentKisDetailsVersionForProcess(long pProcessId) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseDetails> cq = qb.createQuery(TCaseDetails.class);
        Root<TWmProcessCase> from = cq.from(TWmProcessCase.class);
        cq.select(from.get(TWmProcessCase_.KIS_DETAILS));
        cq.where(qb.equal(from.get(TWmProcessCase_.PROCESS), pProcessId));
        return getSingleResultOrNull(getEntityManager().createQuery(cq));
    }

}
