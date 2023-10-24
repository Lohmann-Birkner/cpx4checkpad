/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.dao;

import de.lb.cpx.model.TCase2RuleSelection;
import de.lb.cpx.model.TCase2RuleSelection_;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author gerschmann
 */
@Stateless
@SuppressWarnings("unchecked")
public class TCase2RuleSelectionDao extends AbstractCpxDao<TCase2RuleSelection>{
    
    
    public TCase2RuleSelectionDao() {
        super(TCase2RuleSelection.class);
    }
    
    public TCase2RuleSelection find4caseAndRuleIds(long pCaseId, String pRuleId){
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCase2RuleSelection> query = criteriaBuilder.createQuery(TCase2RuleSelection.class);
        Root<TCase2RuleSelection> from = query.from(TCase2RuleSelection.class);
        query.where(criteriaBuilder.equal(from.get(TCase2RuleSelection_.ruleid), pRuleId), criteriaBuilder.equal(from.get(TCase2RuleSelection_.hospitalCase), pCaseId));
        
        TypedQuery<TCase2RuleSelection> criteriaQuery = getEntityManager().createQuery(query);
        return getSingleResultOrNull(criteriaQuery);
    }
    
    public int delete4caseAndRuleIds(long pCaseId, String pRuleId){
        String query = "delete from TCase2RuleSelection sel where sel.hospitalCase.id=:pCaseId and ruleid=:pRuleId";
        Query deleteQuery = getEntityManager().createQuery(query);
        deleteQuery.setParameter("pCaseId", pCaseId);
        deleteQuery.setParameter("pRuleId", pRuleId);
       
        return deleteQuery.executeUpdate(); 

    }
    
}
