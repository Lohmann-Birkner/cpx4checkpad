/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.server.wm.dao;

import de.lb.cpx.server.commons.enums.EntityGraphType;
import de.lb.cpx.server.dao.AbstractCpxDao;
import de.lb.cpx.wm.model.TWmFinalisationRisk;
import de.lb.cpx.wm.model.TWmFinalisationRisk_;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author wilde
 */
@Stateless
@SuppressWarnings("unchecked")
public class TWmFinalisationRiskDao extends AbstractCpxDao<TWmFinalisationRisk> {

    private static final Logger LOG = Logger.getLogger(TWmFinalisationRiskDao.class.getName());

    /**
     * Creates a new instance.
     */
    public TWmFinalisationRiskDao() {
        super(TWmFinalisationRisk.class);
    }

    public TWmFinalisationRisk findFinalisationRisk(long pProcessCompletionId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<TWmFinalisationRisk> query = criteriaBuilder.createQuery(getEntityClass());
        Root<TWmFinalisationRisk> from = query.from(getEntityClass());
//        query.where(criteriaBuilder.equal(from.get(TWmRisk_.caseDetails), pCaseId));
        query.where(criteriaBuilder.equal(from.get(TWmFinalisationRisk_.PROCESS_HOSPITAL_FINALISATION), pProcessCompletionId));
        
        TypedQuery<TWmFinalisationRisk> criteriaQuery = getEntityManager().createQuery(query);
        addRiskDetailGraph(criteriaQuery);
        return getSingleResultOrNull(criteriaQuery);
    }
    
    private void addRiskDetailGraph(TypedQuery<TWmFinalisationRisk> criteriaQuery) {
        EntityGraph<TWmFinalisationRisk> toFetch = getEntityManager().createEntityGraph(TWmFinalisationRisk.class);
        //toFetch.addAttributeNodes(TCase_.currentExtern,TCase_.currentLocal);
        //toFetch.addSubgraph(TCase_.patient).addAttributeNodes(TPatient_.patInsuranceActual);
        toFetch.addSubgraph(TWmFinalisationRisk_.FINALISATION_RISK_DETAILS);
        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
//        return criteriaQuery;
    }
}
