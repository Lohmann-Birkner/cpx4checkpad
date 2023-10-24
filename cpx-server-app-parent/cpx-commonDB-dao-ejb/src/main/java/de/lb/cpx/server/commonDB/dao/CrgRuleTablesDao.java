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

import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables_;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Data access object for domain model class CrgRuleTables.Initially generated
 * at 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 * @param <E> extends CrgRuleTables
 */
@SuppressWarnings("unchecked")
public abstract class CrgRuleTablesDao<E extends CrgRuleTables> extends AbstractCommonDao<E> {

    /**
     * Creates a new instance.
     *
     * @param entityClass that extends CrgRuleTables
     */
    public CrgRuleTablesDao(Class<E> entityClass) {
        super(entityClass);
    }

    /**
     * finds rule table for table name and poolid
     *
     * @param pPoolId id of the pool
     * @param pName table name
     * @return table object
     */
    public E findRuleTable4Name(long pPoolId, String pName) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        pName = pName.replaceAll("'", "");
        CriteriaQuery<E> query = criteriaBuilder.createQuery(this.getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.where(criteriaBuilder.equal(from.get(getRfCode4Pool()), pPoolId),
                criteriaBuilder.equal(from.get(getTableNameField()), pName));

        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        List<E> rTables = criteriaQuery.getResultList();
        if (rTables != null && !rTables.isEmpty()) {
            return rTables.get(0);
        }
        return null;
    }

    /**
     * returns all tables from pool with poolId
     *
     * @param pPoolId pool id
     * @return list of tables
     */
    public synchronized List<E> getRuleTable4Pool(long pPoolId) {
       CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = criteriaBuilder.createQuery(this.getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.select(criteriaBuilder.construct(getEntityClass(),
                from.get("id"),
                from.get(getTableNameField()),
                from.get(CrgRuleTables_.CREATION_USER),
                from.get(CrgRuleTables_.CREATION_DATE),
                from.get(CrgRuleTables_.CRGT_MESSAGE),
                from.get(CrgRuleTables_.CRGT_CATEGORY)
        ));
        query.where(criteriaBuilder.equal(from.get(getRfCode4Pool()), pPoolId));

        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }
    
    public synchronized List<E> getRuleTables4PoolWithContent(long pPoolId){
       CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = criteriaBuilder.createQuery(this.getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.select(criteriaBuilder.construct(getEntityClass(),
                from.get("id"),
                from.get(getTableNameField()),
                from.get(CrgRuleTables_.CRGT_CONTENT),
                from.get(CrgRuleTables_.CREATION_USER),
                from.get(CrgRuleTables_.CREATION_DATE),
                from.get(CrgRuleTables_.CRGT_MESSAGE),
                from.get(CrgRuleTables_.CRGT_CATEGORY)
        ));
        query.where(criteriaBuilder.equal(from.get(getRfCode4Pool()), pPoolId));

        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
        
    }

    /**
     * returns all tables from pool with poolId
     *
     * @param pPoolId pool id
     * @param pText text to search Table
     * @return list of tables
     */
    public List<E> getRuleTable4PoolAndNameOrContent(long pPoolId, String pText) {
        //Select * FROM CRG_RULE_TABLES_DEV WHERE CRGPL_ID = 141 AND (CRGT_CONTENT LIKE '%ICB%' OR CRGT_TABLE_NAME LIKE '%ICB%');
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<E> query = criteriaBuilder.createQuery(this.getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.select(criteriaBuilder.construct(getEntityClass(),
                from.get("id"),
                from.get(getTableNameField()),
                from.get(CrgRuleTables_.CREATION_USER),
                from.get(CrgRuleTables_.CREATION_DATE),
                from.get(CrgRuleTables_.CRGT_MESSAGE),
                from.get(CrgRuleTables_.CRGT_CATEGORY)
        ));
        Predicate poolPredicate = criteriaBuilder.equal(from.get(getRfCode4Pool()), pPoolId);
        Predicate contentPredicate = criteriaBuilder.like(from.get(CrgRuleTables_.CRGT_CONTENT), "%" + pText + "%");
        Predicate namePredicate = criteriaBuilder.like(from.get(CrgRuleTables_.CRGT_TABLE_NAME), "%" + pText + "%");

        Predicate contentOrNamePredicate = criteriaBuilder.or(namePredicate, contentPredicate);
        query.where(criteriaBuilder.and(poolPredicate, contentOrNamePredicate));

        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

    /**
     * returns all table names from pool with pPoolId that have suffix (number)
     *
     * @param tabName table name
     * @param pPoolId pool id
     * @return list of names found
     */
    public List<String> getLikeTableNames(String tabName, long pPoolId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<String> query = criteriaBuilder.createQuery(String.class);
        Root<E> from = query.from(getEntityClass());
        query.select(from.get(getTableNameField()));
        query.where(criteriaBuilder.equal(from.get(getRfCode4Pool()), pPoolId),
                criteriaBuilder.like(from.get(getTableNameField()), tabName + "(%"));
        TypedQuery<String> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

    protected abstract String getTableName();

    protected abstract String getRfCode4Pool();

    protected abstract String getTableNameField();

    /**
     * returns a Map id to table for pool
     *
     * @param pPoolId pool id
     * @return a map
     */
    public Map<String, E> getId2Tables(long pPoolId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<E> query = criteriaBuilder.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.where(criteriaBuilder.equal(from.get(getRfCode4Pool()), pPoolId));
        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        final List<E> tables = criteriaQuery.getResultList();
        Map<String, E> retMap = new HashMap<>();
        if (tables != null) {
            for (E table : tables) {
                retMap.put(String.valueOf(table.getId()), table);
            }
        }
        return retMap;
    }

    /**
     * returns a Map table name to table for pool
     *
     * @param pPoolId pool id
     * @return a map
     */
    public Map<String, E> getName2Tables(long pPoolId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<E> query = criteriaBuilder.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.where(criteriaBuilder.equal(from.get(getRfCode4Pool()), pPoolId));
        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        final List<E> tables = criteriaQuery.getResultList();
        Map<String, E> retMap = new HashMap<>();
        if (tables != null) {
            for (E table : tables) {
                retMap.put(String.valueOf(table.getCrgtTableName()), table);
            }
        }
        return retMap;
    }

    public List<E> findTables4PoolAndIdList(long pPoolId, List<Long> tableIds) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());
        if (tableIds == null) {
            query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId));
        } else {
            Expression<Long> exp = from.get("id");
            Predicate pred = exp.in(tableIds);
            query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId), pred);
        }

        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        final List<E> list = criteriaQuery.getResultList();
        return list;
    }

    /**
     * returns all tables from pool with poolId
     *
     * @param tableIds list of table ids
     * @return list of tables
     */
    public List<E> getRuleTableNames2Ids(List<Long> tableIds) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<E> query = criteriaBuilder.createQuery(this.getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.select(criteriaBuilder.construct(getEntityClass(),
                from.get("id"),
                from.get(getTableNameField()),
                from.get(CrgRuleTables_.CRGT_MESSAGE),
                from.get(CrgRuleTables_.CRGT_CATEGORY)
        ));
        query.where(from.get("id").in(tableIds));

        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

    public Number countRuleTables4PoolId(long pPoolId) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<E> from = cq.from(getEntityClass());
        cq.select(qb.count(from));
        cq.where(qb.equal(from.get(getRfCode4Pool()), pPoolId));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    public byte[] getRuleTableMessage(long pRuleId, long pPoolId) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<byte[]> cq = qb.createQuery(byte[].class);
        Root<E> from = cq.from(getEntityClass());
        cq.select(from.get(CrgRuleTables_.CRGT_MESSAGE));
        cq.where(qb.equal(from.get(getRfCode4Pool()), pPoolId), qb.equal(from.get("id"), pRuleId));
        return getEntityManager().createQuery(cq).getSingleResult();
    }


}
