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

import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTypes_;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

/**
 * Data access object for domain model class CrgRuleTypes.Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 * @param <E> that extends CrgRuleTypes
 */
public abstract class CrgRuleTypesDao<E extends CrgRuleTypes> extends AbstractCommonDao<E> {

    /**
     * Creates a new instance.
     *
     * @param entityClass that extends CrgRuletypes
     */
    public CrgRuleTypesDao(Class<E> entityClass) {
        super(entityClass);
    }

    /**
     * returns all entites of CrgRuleTypes as HashMap ident to object
     *
     * @return map of rule types
     */
    public Map<String, E> getRuleTypes() {
//        final TypedQuery<CrgRuleTypes> query = getEntityManager().createQuery("from " + getEntityName(), CrgRuleTypes.class);
//
//        List<CrgRuleTypes> types = query.getResultList();
        List<E> types = getRuleTypesAsList();
        Map<String, E> retMap = new HashMap<>();
        if (types != null && !types.isEmpty()) {
            for (E type : types) {
                retMap.put(type.getCrgtShortText(), type);
            }
        }
        return retMap;
    }

    public List<E> getRuleTypesAsList() {
        final TypedQuery<E> query = getEntityManager().createQuery("from " + getEntityName(), getEntityClass());

        List<E> types = query.getResultList();
        return types;
    }

    /**
     * looks for rule type to short text
     *
     * @param pShortText short text
     * @return rule type
     */
    public E findRuleTypeWithShortText(String pShortText) {

        String qryStr = "from " + getEntityName() + " p where p.crgtShortText =:pShortText ";
        TypedQuery<E> query = getEntityManager().createQuery(qryStr, getEntityClass());
        query.setParameter("pShortText", pShortText);
        List<E> lResults = query.getResultList();
        if (lResults == null || lResults.isEmpty()) {
            return null;
        }
        return lResults.get(0);

    }

    /**
     * returns map mapping id to type
     *
     * @return map
     */
    public Map<String, E> getId2RuleType() {
        final TypedQuery<E> query = getEntityManager().createQuery("from " + getEntityName(), getEntityClass());
        final List<E> types = query.getResultList();
        Map<String, E> retMap = new HashMap<>();
        if (types != null && !types.isEmpty()) {
            for (E type : types) {
                retMap.put(String.valueOf(type.getId()), type);
            }
        }
        return retMap;
    }

    public String getRuleTypeNameForRuleAndPool(long pRuleId, long pPoolId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<E> from = query.from(getEntityClass());
        query.select(from.get(CrgRuleTypes_.CRGT_SHORT_TEXT));

        Join<CrgRuleTypes, CrgRules> join = from.join(getType2Rule());
        query.where(cb.equal(join.get(getRfCode4Pool()), pPoolId), cb.equal(join.get(getRfCode4Rule()), pRuleId));

        TypedQuery<String> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getSingleResult();
    }

    protected abstract String getType2Rule();

    protected abstract String getRfCode4Pool();

    protected abstract String getRfCode4Rule();

    protected abstract String getTableName();

    public String getRuleTypeDisplayNameForRuleAndPool(long pRuleId, long pPoolId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<E> from = query.from(getEntityClass());
        query.select(from.get(CrgRuleTypes_.CRGT_DISPLAY_TEXT));

        Join<CrgRuleTypes, CrgRules> join = from.join(getType2Rule());
        query.where(cb.equal(join.get(getRfCode4Pool()), pPoolId), cb.equal(join.get(getRfCode4Rule()), pRuleId));

        TypedQuery<String> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getSingleResult();
    }

    /**
     * removes all rule types for list of ids
     *
     * @param pTypesIds list of ids
     */
    public void deleteByIds(List<Long> pTypesIds) {
        String sql = "delete from " + getEntityName() + "  WHERE id IN (:pTypesIds)";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("pTypesIds", pTypesIds);
        query.executeUpdate();
    }

    /**
     * returns all types for identfied user
     *
     * @param pCurrentCpxUserId user id
     * @return list of rule types
     */
    @SuppressWarnings("unchecked")
    public List<E> findRuleTypes4CreationUser(long pCurrentCpxUserId) {
        String qryStr = "from " + getEntityName() + " p where p.creationUser =:pCurrentCpxUserId ";
        Query query = getEntityManager().createQuery(qryStr);
        query.setParameter("pCurrentCpxUserId", pCurrentCpxUserId);
        return query.getResultList();
    }

}
