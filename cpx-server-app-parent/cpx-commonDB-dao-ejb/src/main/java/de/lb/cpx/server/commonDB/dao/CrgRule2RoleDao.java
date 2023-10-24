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

import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.rules.CrgRule2Role;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

/**
 * Data access object for domain model class CrgRule2Role.Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 * @param <E> extends CrgRule2Role
 */
public abstract class CrgRule2RoleDao<E extends CrgRule2Role> extends AbstractCommonDao<E> {

    /**
     * Creates a new instance.
     *
     * @param entityClass Class that extends CrgRule2Role
     */
    public CrgRule2RoleDao(Class<E> entityClass) {
        super(entityClass);
    }

    public List<CdbUserRoles> findUserRoles2RuleId(long pRuleId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CdbUserRoles> query = criteriaBuilder.createQuery(CdbUserRoles.class);
        Root<E> from = query.from(getEntityClass());
        query.select(from.get(getRf4UserRoles()));

        query.where(criteriaBuilder.equal(from.get(getRfCode4Rule()), pRuleId));

        TypedQuery<CdbUserRoles> criteriaQuery = getEntityManager().createQuery(query);
        return criteriaQuery.getResultList();
    }

    protected abstract String getRfCode4Rule();

    protected abstract String getRf4UserRoles();

    protected abstract String getTableName();

    protected abstract String getRfCode4Pool();

    public void remove4RuleId(long pRuleId) {
//        String qry = "DELETE FROM " + getTableName() + " r where r.CRGR_ID =:pRuleId";
        //Awi remove alias due to mssql error
        String qry = "DELETE FROM " + getTableName() + " where CRGR_ID =:pRuleId";
        Query nativeQuery = getEntityManager().createNativeQuery(qry);
        nativeQuery.setParameter("pRuleId", pRuleId);
        nativeQuery.executeUpdate();
    }

    public void remove4RuleIdAndRole(long pRuleId, long pRoleId) {
//        String qry = "DELETE FROM " + getTableName() + " r where r.CRGR_ID =:pRuleId";
        //Awi remove alias due to mssql error
        String qry = "DELETE FROM " + getTableName() + " where CRGR_ID =:pRuleId AND CDBUR_ID =:pRoleId";
        Query nativeQuery = getEntityManager().createNativeQuery(qry);
        nativeQuery.setParameter("pRuleId", pRuleId);
        nativeQuery.setParameter("pRoleId", pRoleId);
        nativeQuery.executeUpdate();
    }

    public void remove4RuleIds(List<Long> pRuleIds) {
        String sql = "delete from " + getEntityName() + "  WHERE CRGR_ID IN (:pRuleIds)";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("pRuleIds", pRuleIds);
        query.executeUpdate();
    }

    public List<Long> findRuleIdsForRole(long pRoleId, long pPoolId) {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<E> from = cq.from(getEntityClass());
        Join<CrgRule2Role, CrgRules> join = from.join(getRfCode4Rule());
        cq.select(join.get("id"));
        cq.where(qb.equal(join.get(getRfCode4Pool()), pPoolId), qb.equal(from.get(getRf4UserRoles()), pRoleId));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<Long> findUserRolesIds2RuleId(long pRuleId) {
        String queryName = "SELECT a.cdbUserRoles.id FROM " + getEntityName() + " a WHERE a." + getRfCode4Rule() + ".id =:pRuleId";
        final TypedQuery<Long> query = getEntityManager().createQuery(queryName, Long.class);
        query.setParameter("pRuleId", pRuleId);

        return query.getResultList();

    }
//        List <Long> ret = new ArrayList<>();
//        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<CdbUserRoles> query = criteriaBuilder.createQuery(CdbUserRoles.class);
//        Root<E> from = query.from(getEntityClass());
//
//        query.select(from.get(CrgRule2Role_.CDB_USER_ROLES));
//
//        query.where(criteriaBuilder.equal(from.get(getRfCode4Rule()), pRuleId));
//
//         List<CdbUserRoles>  roles = getEntityManager().createQuery(query).getResultList();
//         if(roles != null && !roles.isEmpty()){
//             roles.forEach((role) -> {
//                 ret.add(role.getId());
//            });
//         }
//         return ret;
//    }
}
