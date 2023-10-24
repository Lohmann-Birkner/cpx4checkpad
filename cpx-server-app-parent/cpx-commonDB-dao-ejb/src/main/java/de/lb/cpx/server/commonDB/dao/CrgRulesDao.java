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

import de.lb.cpx.model.enums.RuleTypeEn;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2Role;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2Role_;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2Table;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.CrgRules_;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 * Data access object for domain model class CrgRules.Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 * @param <E> that extends CrgRules
 * @param <R> that extends CrgRule2Role
 */
@SuppressWarnings("unchecked")
public abstract class CrgRulesDao<E extends CrgRules, R extends CrgRule2Role> extends AbstractCommonDao<E> {

    private static final Logger LOG = Logger.getLogger(CrgRulesDao.class.getName());

    /**
     * Creates a new instance.
     *
     * @param entityClass that extends CrgRules
     */
    public CrgRulesDao(Class<E> entityClass) {
        super(entityClass);
    }

    public List<E> getAllRules() {
        final TypedQuery<E> query = getEntityManager().createQuery("from " + getEntityName(), getEntityClass());
        return query.getResultList();
    }

    /**
     * returns a list of rules to rids
     *
     * @param rules_rid_list list of roles
     * @return list of rules
     */
    public List<E> getRules4RidList(List<String> rules_rid_list) {
        List<E> list = null;
        if (rules_rid_list != null && !rules_rid_list.isEmpty()) {
            TypedQuery<E> query = getEntityManager().createQuery("from " + getEntityName()
                    + " r  WHERE r.crgrRid in :rid_list", getEntityClass());

            query.setParameter("rid_list", rules_rid_list);
            list = query.getResultList();
            return list;
        }
        return null;
    }

//    /**
//     * returns rule object to id
//     *
//     * @param pRId id
//     * @return crg rules
//     */
//    public CrgRules getRule2Id(long pRId) {
//        return findById(pRId);
//    }
    /**
     * return a list of rulst for pool with given id
     *
     * @param pPoolId poolid
     * @return list of detected rules
     */
    public List<E> getRules4PoolId(long pPoolId) {

//         CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
//         CriteriaQuery<CrgRules> query = criteriaBuilder.createQuery(this.getEntityClass());
//         Root<CrgRules> from = query.from(getEntityClass());
//         query.where(criteriaBuilder.equal(from.get(getRfCode4Pool()), pPoolId));
        return getRules4PoolId_excludeContent(pPoolId);
//        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
//
//        CriteriaQuery<CrgRules> query = criteriaBuilder.createQuery(this.getEntityClass());
//        Root<CrgRules> from = query.from(getEntityClass());
//        query.where(criteriaBuilder.equal(from.get(getRfCode4Pool()), pPoolId));
//
//        TypedQuery<CrgRules> criteriaQuery = getEntityManager().createQuery(query);
//        final List<CrgRules> list = criteriaQuery.getResultList();
//
//        return list;
    }

    public List<E> getRules4PoolId_excludeContent(long pPoolId) {
//        TypedQuery<CrgRules> qry = getEntityManager().createQuery("SELECT NEW de.lb.cpx.server.commonDB.model.rules."+getEntityName()+
//                        "(s.id, s.crgrIdentifier)"+
//                        " FROM "+getEntityName()+" s", getEntityClass());
//        List<CrgRules> myList = qry.getResultList();
//        return myList;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.select(cb.construct(getEntityClass(),
                from.get("id"),
                from.get(getRfCode4Type()),
                from.get(CrgRules_.CRGR_CAPTION),
                from.get(CrgRules_.CRGR_CATEGORY),
                from.get(CrgRules_.CRGR_IDENTIFIER),
                from.get(CrgRules_.CRGR_NOTE),
                from.get(CrgRules_.CRGR_NUMBER),
                from.get(CrgRules_.CRGR_RID),
                from.get(CrgRules_.CRGR_RULE_ERROR_TYPE),
                from.get(CrgRules_.CRGR_SUGG_TEXT),
                from.get(CrgRules_.CRGR_VALID_FROM),
                from.get(CrgRules_.CRGR_VALID_TO),
                from.get(CrgRules_.CRGR_FEE_GROUP),
                from.get(CrgRules_.CRGR_MESSAGE)));

        query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId));

        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        final List<E> list = criteriaQuery.getResultList();
        return list;
    }

    public List<E> getRules4PoolAndTableId(long pPoolId, long pTableId) {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.select(cb.construct(getEntityClass(),
                from.get("id"),
                //from.get(getRfCode4Type()),
                from.get(CrgRules_.CRGR_CAPTION),
                from.get(CrgRules_.CRGR_CATEGORY),
                from.get(CrgRules_.CRGR_IDENTIFIER),
                from.get(CrgRules_.CRGR_NOTE),
                from.get(CrgRules_.CRGR_NUMBER),
                from.get(CrgRules_.CRGR_RID),
                from.get(CrgRules_.CRGR_RULE_ERROR_TYPE),
                from.get(CrgRules_.CRGR_SUGG_TEXT),
                from.get(CrgRules_.CRGR_VALID_FROM),
                from.get(CrgRules_.CRGR_VALID_TO),
                from.get(CrgRules_.CRGR_FEE_GROUP),
                from.get(CrgRules_.CRGR_MESSAGE)));

        Join<CrgRules, CrgRule2Table> join = from.join(getRule2Table());
        query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId), cb.equal(join.get(getRfCode4Table()), pTableId));

        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();

    }

    public List<E> getAllRules_excludeContent() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.select(cb.construct(getEntityClass(),
                from.get("id"),
                from.get(getRfCode4Type()),
                from.get(getRfCode4Pool()),
                from.get(CrgRules_.CRGR_CAPTION),
                from.get(CrgRules_.CRGR_CATEGORY),
                from.get(CrgRules_.CRGR_IDENTIFIER),
                from.get(CrgRules_.CRGR_NOTE),
                from.get(CrgRules_.CRGR_NUMBER),
                from.get(CrgRules_.CRGR_RID),
                from.get(CrgRules_.CRGR_RULE_ERROR_TYPE),
                from.get(CrgRules_.CRGR_SUGG_TEXT),
                //                from.get(CrgRules_..CRGPL_POOL_YEAR),
                //                from.get(CrgRuleTypes_.CRGT_DISPLAY_TEXT),
                from.get(CrgRules_.CRGR_VALID_FROM),
                from.get(CrgRules_.CRGR_VALID_TO),
                from.get(CrgRules_.CRGR_FEE_GROUP),
                from.get(CrgRules_.CRGR_MESSAGE)));
        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        final List<E> list = criteriaQuery.getResultList();
        return list;
    }

    public Number countRules4PoolId(long id) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<E> from = cq.from(getEntityClass());
        cq.select(qb.count(from));
        cq.where(qb.equal(from.get(getRfCode4Pool()), id));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    protected abstract String getRfCode4Type();

    protected abstract String getTableName();

    protected abstract String getRfCode4Pool();

    protected abstract String getRule2Table();

    protected abstract String getRfCode4Table();

    protected abstract String getRule2Role();

    protected String getRfCode4Role() {
        return CrgRule2Role_.CDB_USER_ROLES;
    }

    protected abstract Class<R> getRule2RoleClass();

    protected abstract String getRfRule2Role();

    public byte[] getRuleDefinition(long pRuleId, long pPoolId) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<byte[]> cq = qb.createQuery(byte[].class);
        Root<E> from = cq.from(getEntityClass());
        cq.select(from.get(CrgRules_.CRGR_DEFINITION));
        cq.where(qb.equal(from.get(getRfCode4Pool()), pPoolId), qb.equal(from.get("id"), pRuleId));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    public byte[] getRuleMessage(long pRuleId, long pPoolId) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<byte[]> cq = qb.createQuery(byte[].class);
        Root<E> from = cq.from(getEntityClass());
        cq.select(from.get(CrgRules_.CRGR_MESSAGE));
        cq.where(qb.equal(from.get(getRfCode4Pool()), pPoolId), qb.equal(from.get("id"), pRuleId));
        return getEntityManager().createQuery(cq).getSingleResult();
    }
    
    public Long getSizeOfRuleTableRelation(long pPoolId, CrgRuleTables pTable) {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<E> from = query.from(getEntityClass());
        query.select(cb.count(from));
        Join<CrgRules, CrgRule2Table> join = from.join(getRule2Table());
        query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId), cb.equal(join.get(getRfCode4Table()), pTable.getId()));
        return getEntityManager().createQuery(query).getSingleResult();
    }
    
    public Long getSizeOfRuleTableRelation(long pPoolId, List<CrgRuleTables> pTables) {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<E> from = query.from(getEntityClass());
        query.select(cb.count(from));
        Join<CrgRules, CrgRule2Table> join = from.join(getRule2Table());
        List<Long> ids = pTables.stream().map((t) -> {
            return t.getId();
        }).collect(Collectors.toList());
//        CriteriaBuilder.In<Long> in = cb.in(join.get(getRfCode4Table()));
        Predicate in = join.get(getRfCode4Table()).in(ids);
//        in.in(ids);
        query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId), in);
        return getEntityManager().createQuery(query).getSingleResult();
    }

    public List<E> getRulesDefinitions4PoolAndRuleIds(long pPoolId, List<Long> ruleIds) {
        return getRulesDefinitions4PoolAndRuleIds(pPoolId, ruleIds, null);
    }

    public synchronized List<E> getRulesDefinitions4PoolAndRuleIds(long pPoolId, List<Long> ruleIds, Long userId) {
        LOG.log(Level.INFO, "poolId: {0}, userId:{1}", new Object[]{String.valueOf(pPoolId), userId == null ? "null" : String.valueOf(userId)});
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());
        if (userId != null) {
            if (ruleIds == null) {
                query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId), cb.equal(from.get(CrgRules_.CREATION_USER), userId));
            } else {
                Expression<Long> exp = from.get("id");
                Predicate pred = exp.in(ruleIds);
                query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId), pred, cb.equal(from.get(CrgRules_.CREATION_USER), userId));
            }
        } else {
            if (ruleIds == null) {
                query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId));
            } else {
                Expression<Long> exp = from.get("id");
                Predicate pred = exp.in(ruleIds);
                query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId), pred);
            }

        }

        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);

        final List<E> list = criteriaQuery.getResultList();
        return list;

    }

    public long getRulesCount4PoolAndUser(long pPoolId, Long userId) {
        LOG.log(Level.INFO, "poolId: {0}, userId:{1}", new Object[]{String.valueOf(pPoolId), userId == null ? "null" : String.valueOf(userId)});
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<E> from = query.from(getEntityClass());
        if (userId != null) {
            query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId), cb.equal(from.get(CrgRules_.CREATION_USER), userId));
        } else {
            query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId));
        }
        query.select(cb.count(from));

        return getEntityManager().createQuery(query).getSingleResult();
    }

    public E getRuleById(long pRuleId, long pPoolId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.select(from);

        query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId), cb.equal(from.get("id"), pRuleId));

        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        final E rule = getSingleResultOrNull(criteriaQuery);//criteriaQuery.getResultList();
        return rule;
    }

    public E getRuleByIdWithoutDefinition(long pRuleId, long pPoolId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.select(cb.construct(getEntityClass(),
                from.get("id"),
                //        from.get(getRfCode4Type()),
                from.get(CrgRules_.CRGR_CAPTION),
                from.get(CrgRules_.CRGR_CATEGORY),
                from.get(CrgRules_.CRGR_IDENTIFIER),
                from.get(CrgRules_.CRGR_NOTE),
                from.get(CrgRules_.CRGR_NUMBER),
                from.get(CrgRules_.CRGR_RID),
                from.get(CrgRules_.CRGR_RULE_ERROR_TYPE),
                from.get(CrgRules_.CRGR_SUGG_TEXT),
                from.get(CrgRules_.CRGR_VALID_FROM),
                from.get(CrgRules_.CRGR_VALID_TO),
                from.get(CrgRules_.CRGR_FEE_GROUP),
                from.get(CrgRules_.CRGR_MESSAGE)));

        query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId), cb.equal(from.get("id"), pRuleId));

        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        final E rule = getSingleResultOrNull(criteriaQuery);//criteriaQuery.getResultList();
        return rule;
    }

    public List<E> getAllRulesForPoolAndRole(long pPoolId, long pRoleId, boolean pIncludeRole) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        query.distinct(true);
        Root<E> from = query.from(getEntityClass());
        query.select(cb.construct(getEntityClass(),
                from.get("id"),
                from.get(getRfCode4Type()),
                from.get(CrgRules_.CRGR_CAPTION),
                from.get(CrgRules_.CRGR_CATEGORY),
                from.get(CrgRules_.CRGR_IDENTIFIER),
                from.get(CrgRules_.CRGR_NOTE),
                from.get(CrgRules_.CRGR_NUMBER),
                from.get(CrgRules_.CRGR_RID),
                from.get(CrgRules_.CRGR_RULE_ERROR_TYPE),
                from.get(CrgRules_.CRGR_SUGG_TEXT),
                from.get(CrgRules_.CRGR_VALID_FROM),
                from.get(CrgRules_.CRGR_VALID_TO),
                from.get(CrgRules_.CRGR_FEE_GROUP),
                from.get(CrgRules_.CRGR_MESSAGE)));

        Subquery<R> subQuery = query.subquery(getRule2RoleClass());
        Root<R> subFrom = subQuery.from(getRule2RoleClass());
        subQuery.select(subFrom);

        subQuery.where(cb.equal(from.get("id"), subFrom.get(getRfRule2Role())), cb.equal(subFrom.get(getRfCode4Role()), pRoleId));

        query.where((pIncludeRole ? cb.exists(subQuery) : cb.not(cb.exists(subQuery))), cb.equal(from.get(getRfCode4Pool()), pPoolId));
//        Join<CrgRules, CrgRule2Role> join = from.join(getRule2Role());
//        query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId),(pIncludeRole?cb.equal(join.get(getRfCode4Role()), pRoleId):cb.notEqual(join.get(getRfCode4Role()), pRoleId)));

        query.orderBy(cb.asc(from.get("id")));
        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();

    }

    public Long countRulesForRole(long pPoolId, long pRoleId) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<E> from = cq.from(getEntityClass());
        cq.select(qb.count(from));
        Join<CrgRules, CrgRule2Role> join = from.join(getRule2Role());
        cq.where(qb.equal(from.get(getRfCode4Pool()), pPoolId), qb.equal(join.get(getRfCode4Role()), pRoleId));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    public void deleteByIds(List<Long> pRuleIds) {
        String sql = "delete from " + getEntityName() + "  WHERE id IN (:pRuleIds)";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("pRuleIds", pRuleIds);
        query.executeUpdate();
    }

    public Long getSizeOfRuleTypeRelation(long pTypeId) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<E> from = cq.from(getEntityClass());
        cq.select(qb.count(from));
        cq.where(qb.equal(from.get(getRfCode4Type()), pTypeId));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    public List<E> findRules4CreationUser(Long pCurrentCpxUserId) {
        String qryStr = "from " + getEntityName() + " p where p.creationUser =:pCurrentCpxUserId ";

        Query query = getEntityManager().createQuery(qryStr);
        query.setParameter("pCurrentCpxUserId", pCurrentCpxUserId);

        return query.getResultList();
    }

    public List<E> getAllRules_excludeContentAndUser(Long pCurrentCpxUserId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.select(cb.construct(getEntityClass(),
                from.get("id"),
                from.get(getRfCode4Type()),
                from.get(getRfCode4Pool()),
                from.get(CrgRules_.CRGR_CAPTION),
                from.get(CrgRules_.CRGR_CATEGORY),
                from.get(CrgRules_.CRGR_IDENTIFIER),
                from.get(CrgRules_.CRGR_NOTE),
                from.get(CrgRules_.CRGR_NUMBER),
                from.get(CrgRules_.CRGR_RID),
                from.get(CrgRules_.CRGR_RULE_ERROR_TYPE),
                from.get(CrgRules_.CRGR_SUGG_TEXT),
                //                from.get(CrgRules_..CRGPL_POOL_YEAR),
                //                from.get(CrgRuleTypes_.CRGT_DISPLAY_TEXT),
                from.get(CrgRules_.CRGR_VALID_FROM),
                from.get(CrgRules_.CRGR_VALID_TO),
                from.get(CrgRules_.CRGR_FEE_GROUP),
                from.get(CrgRules_.CRGR_MESSAGE)));
        if (pCurrentCpxUserId != null) {
            query.where(cb.equal(from.get(CrgRules_.CREATION_USER), pCurrentCpxUserId));
        }
        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        final List<E> list = criteriaQuery.getResultList();
        return list;
    }

    public List<E> getAllRules4User(Long pCurrentCpxUserId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.where(cb.equal(from.get(CrgRules_.CREATION_USER), pCurrentCpxUserId));
        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        final List<E> list = criteriaQuery.getResultList();
        return list;
    }

    public List<E> getAllRules4PoolId(Long pPoolId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId));
        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        final List<E> list = criteriaQuery.getResultList();
        return list;
    }

    public List<E> findRulesByIds(List<Long> pRuleIds, long pPoolId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());
//        query.select(from);
        query.select(cb.construct(getEntityClass(),
                from.get("id"),
                from.get(getRfCode4Type()),
                from.get(getRfCode4Pool()),
                from.get(CrgRules_.CRGR_CAPTION),
                from.get(CrgRules_.CRGR_CATEGORY),
                from.get(CrgRules_.CRGR_IDENTIFIER),
                from.get(CrgRules_.CRGR_NOTE),
                from.get(CrgRules_.CRGR_NUMBER),
                from.get(CrgRules_.CRGR_RID),
                from.get(CrgRules_.CRGR_RULE_ERROR_TYPE),
                from.get(CrgRules_.CRGR_SUGG_TEXT),
                //                from.get(CrgRules_..CRGPL_POOL_YEAR),
                //                from.get(CrgRuleTypes_.CRGT_DISPLAY_TEXT),
                from.get(CrgRules_.CRGR_VALID_FROM),
                from.get(CrgRules_.CRGR_VALID_TO),
                from.get(CrgRules_.CRGR_FEE_GROUP),
                from.get(CrgRules_.CRGR_MESSAGE)));

        query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId), from.get("id").in(pRuleIds));

        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

    public void updateRuleDefinition(Long pRuleId, long pPoolId, byte[] ruleDefinition) {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaUpdate<E> update = cb.createCriteriaUpdate(getEntityClass());

        Root<E> from = update.from(getEntityClass());

        update.set(from.get(CrgRules_.CRGR_DEFINITION), ruleDefinition).where(cb.equal(from.get(getRfCode4Pool()), pPoolId), cb.equal(from.get("id"), pRuleId));

        getEntityManager().createQuery(update).executeUpdate();
    }

    public List<CrgRules> getAllRules4CreationUser(Long pCurrentCpxUserId) {
        String qryStr = "from " + getEntityName() + " p where p.creationUser =:pCurrentCpxUserId ";
        Query query = getEntityManager().createQuery(qryStr);
        query.setParameter("pCurrentCpxUserId", pCurrentCpxUserId);
        return query.getResultList();
    }

    public List<E> getRuleList4Grouper(List<Long> pRuleIds) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());
        query.where(from.get("id").in(pRuleIds));

        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

    public List<String> getRids4SearchOptions(Map<String, List<String>> pFilterOptionMap) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);

        Root<E> from = query.from(getEntityClass());
        query.select(from.get(CrgRules_.CRGR_RID));
        Set<String> fields = pFilterOptionMap.keySet();
        List<Predicate> andPredicate = new ArrayList<>();
        for (String field : fields) {
            List<String> orStrings = pFilterOptionMap.get(field);
            List<Predicate> pred = new ArrayList<>();
            for (String orStr : orStrings) {
                if (field.equals("crgrRuleErrorType")) {
                    String typeStr[] = orStr.split(",");
                    for (String tStr : typeStr) {
                        Predicate orPr = cb.equal(from.get(field), RuleTypeEn.valueOf(tStr));
                        pred.add(orPr);
                    }

                } else {
                    Predicate orPr = cb.like(from.get(field), "%" + orStr + "%");
                    pred.add(orPr);
                }
            }
            Predicate[] predArray = new Predicate[pred.size()];
            pred.toArray(predArray);
            andPredicate.add(cb.or(predArray));
        }
        Predicate[] predArray = new Predicate[andPredicate.size()];
        andPredicate.toArray(predArray);
        query.where(cb.and(predArray));
        return getEntityManager().createQuery(query).getResultList();

    }

    public List<E>  getFullRules4PoolAndTableId(long pPoolId, long pTableId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());
        Root<E> from = query.from(getEntityClass());

        Join<CrgRules, CrgRule2Table> join = from.join(getRule2Table());
        query.where(cb.equal(from.get(getRfCode4Pool()), pPoolId), cb.equal(join.get(getRfCode4Table()), pTableId));

        TypedQuery<E> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

}
