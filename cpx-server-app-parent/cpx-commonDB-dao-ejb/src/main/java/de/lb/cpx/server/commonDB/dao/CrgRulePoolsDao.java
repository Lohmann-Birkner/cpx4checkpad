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
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools_;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables_;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.CrgRules_;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Data access object for domain model class CrgRulePools.Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 * @param <E> that extends CrgRulePools
 */
@SuppressWarnings("unchecked")
public abstract class CrgRulePoolsDao<E extends CrgRulePools> extends AbstractCommonDao<E> {

    /**
     * Creates a new instance.
     *
     * @param entityClass that extends CrgRulePools
     */
    public CrgRulePoolsDao(Class<E> entityClass) {
        super(entityClass);
    }
    
    protected abstract String getRfCode4Id();
    protected abstract String getRfCode4Rule();
    protected abstract String getRfCode4RuleTable();
    protected abstract String getSuffix();
    
    /**
     * looks for rule pool with name and year
     *
     * @param name pools identifier
     * @param year pools year
     * @return CrgRulePools
     */
    public E getRulePool(String name, Integer year) {
        List<E> lResults = null;

        String qryStr = "from " + getEntityName() + " p where p.crgplIdentifier =:pName and p.crgplPoolYear =:pYear";
        TypedQuery<E> query = getEntityManager().createQuery(qryStr, getEntityClass());
        query.setParameter("pName", name);
        query.setParameter("pYear", year);
        lResults = query.getResultList();
        if (lResults == null || lResults.isEmpty()) {
            return null;
        }
        return lResults.get(0);
    }

    /**
     * Create map of rule pools id and rule pools name
     *
     * @param pYear year
     * @return map of rule pools
     */
    public Map<Long, String> createRulePoolsMap(int pYear) {
        List<CrgRulePools> rulePoolsList;
        Map<Long, String> rulePoolsMap = new HashMap<>();
//        try {
        String queryString = "SELECT a FROM " + getEntityName() + " a "
                + "WHERE a.crgplPoolYear =:year "
                + "AND a.crgplFrom <= current_date "
                + "AND a.crgplTo >= current_date "
                + "ORDER BY a.crgplIdentifier";

        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("year", pYear);
        rulePoolsList = query.getResultList();
        rulePoolsList.forEach((pool) -> {
            rulePoolsMap.put(pool.getId(), pool.getCrgplIdentifier());
        });
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create rule pools list", ex);
//        }
        return rulePoolsMap;
    }

    public CrgRulePools findPoolWithNameAndDates(CrgRulePools pool) {
        return findPoolWithNameAndDates(pool, null);
    }

    public E findPoolWithNameAndDates(CrgRulePools pool, Long pCurrentUserId) {
        List<E> lResults = null;

        String qryStr = "from " + getEntityName() + " p where p.crgplIdentifier =:pName "
                + "and p.crgplPoolYear =:pYear";
//                +" and p.crgplFrom =:pFrom"
//                + " and p.crgplTo =:pTo";
        if (pCurrentUserId != null) {
            qryStr += " and p.creationUser =:pCurrentCpxUserId ";
        }
        TypedQuery<E> query = getEntityManager().createQuery(qryStr, getEntityClass());
        query.setParameter("pName", pool.getCrgplIdentifier());
        query.setParameter("pYear", pool.getCrgplPoolYear());
        if (pCurrentUserId != null) {
            query.setParameter("pCurrentCpxUserId", pCurrentUserId);
        }
        /*        query.setParameter("pFrom", pool.getCrgplFrom());
        query.setParameter("pTo", pool.getCrgplTo());*/
        lResults = query.getResultList();
        if (lResults == null || lResults.isEmpty()) {
            return null;
        }
        return lResults.get(0);
    }

    /**
     * returns all pools for identfied user
     *
     * @param pCurrentCpxUserId user id
     * @return list of rule pools
     */
    public List<E> findAll4CreationUser(Long pCurrentCpxUserId) {

        String qryStr = "from " + getEntityName() + " p where p.creationUser =:pCurrentCpxUserId ";

        Query query = getEntityManager().createQuery(qryStr);
        query.setParameter("pCurrentCpxUserId", pCurrentCpxUserId);

        return query.getResultList();

    }

    public List<E> findAllYearsDesc(Long pCurrentUserId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.select(from);
        if (pCurrentUserId != null) {
            query.where(cb.equal(from.get(CrgRulePools_.CREATION_USER), pCurrentUserId));
        }
        query.orderBy(cb.desc(from.get(CrgRulePools_.CRGPL_POOL_YEAR)));

        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        final List<E> list = criteriaQuery.getResultList();
        return list;
    }

    public boolean containsPoolErrorMessages(long pPoolId) {
        /*
        SELECT count(*) FROM CRG_RULE_POOLS_DEV pool
        JOIN CRG_RULES_DEV rule_dev ON pool.id = rule_dev.CRGPL_ID
        JOIN CRG_RULE_TABLES_DEV table_dev ON pool.id = table_dev.CRGPL_ID
        where pool.id = 121 AND (rule_dev.CRGR_MESSAGE IS NOT NULL OR table_dev.CRGT_MESSAGE IS NOT NULL);
        */
//        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
//        Root<E> from = cq.from(getEntityClass());
//        
//        cq.select(qb.count(from));
//        
//        Join<CrgRulePools, CrgRules> joinRule = from.join(getRfCode4Rule());
//        Join<CrgRulePools, CrgRuleTables> joinTable = from.join(getRfCode4RuleTable());
//        
//        Predicate orPredicate = qb.or(qb.isNotNull(joinRule.get(CrgRules_.CRGR_MESSAGE)),qb.isNotNull(joinTable.get(CrgRuleTables_.CRGT_MESSAGE)));
//        Predicate andPredicate = qb.and(qb.equal(from.get(getRfCode4Id()), pPoolId),orPredicate);
//        cq.where(andPredicate);
//        
//        return getEntityManager().createQuery(cq).getSingleResult()> 0;
        
        //not good should be one query not two!
        return containsPoolRuleMessages(pPoolId) || containsPoolRuleTableMessages(pPoolId);
    }

    public boolean containsPoolRuleMessages(long pPoolId) {
         /*
        SELECT count(*) FROM CRG_RULE_POOLS_DEV pool
        JOIN CRG_RULES_DEV rule_dev ON pool.id = rule_dev.CRGPL_ID
        where pool.id = 121 AND rule_dev.CRGR_MESSAGE IS NOT NULL;
        */
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<E> from = cq.from(getEntityClass());
        
        cq.select(qb.count(from));
        
        Join<CrgRulePools, CrgRules> joinRule = from.join(getRfCode4Rule());
        
        Predicate andPredicate = qb.and(qb.equal(from.get(getRfCode4Id()), pPoolId),qb.isNotNull(joinRule.get(CrgRules_.CRGR_MESSAGE)));
        cq.where(andPredicate);
        
        return getEntityManager().createQuery(cq).getSingleResult()> 0;
    }

    public boolean containsPoolRuleTableMessages(long pPoolId) {
         /*
        SELECT count(*) FROM CRG_RULE_POOLS_DEV pool
        JOIN CRG_RULE_TABLES_DEV table_dev ON pool.id = table_dev.CRGPL_ID
        where pool.id = 121 AND table_dev.CRGT_MESSAGE IS NOT NULL;
        */
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<E> from = cq.from(getEntityClass());
        
        cq.select(qb.count(from));
        
        Join<CrgRulePools, CrgRuleTables> joinTable = from.join(getRfCode4RuleTable());
        Predicate andPredicate = qb.and(qb.equal(from.get(getRfCode4Id()), pPoolId),qb.isNotNull(joinTable.get(CrgRuleTables_.CRGT_MESSAGE)));
        cq.where(andPredicate);
        
        return getEntityManager().createQuery(cq).getSingleResult()> 0;
    }
}
