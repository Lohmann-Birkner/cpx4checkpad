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
package de.lb.cpx.server.dao;

import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseDepartment_;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseIcd_;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TCaseWard;
import de.lb.cpx.server.commons.enums.EntityGraphType;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Hibernate;

/**
 *
 * @author wilde
 */
@Stateless
public class TCaseDepartmentDao extends AbstractCpxDao<TCaseDepartment> {

    //private Logger log = Logger.getLogger(getClass().getName());
    /**
     * Creates a new instance.
     */
    public TCaseDepartmentDao() {
        super(TCaseDepartment.class);
    }

    public List<TCaseDepartment> findByCaseDetails(final TCaseDetails details) {
        final TypedQuery<TCaseDepartment> query = getEntityManager()
                .createQuery("from TCaseDepartment e where e.caseDetails =:detailsId", TCaseDepartment.class);
        query.setParameter("detailsId", details);
        return query.getResultList();
    }

    public List<TCaseDepartment> findListByCaseDetailsId(Long detailsId, boolean pFetchEager) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseDepartment> query = criteriaBuilder.createQuery(TCaseDepartment.class);
        query.distinct(true);
        Root<TCaseDepartment> from = query.from(TCaseDepartment.class);
//        from.fetch(TCaseDepartment_.caseOpses,JoinType.LEFT);
//        from.fetch(TCaseDepartment_.caseIcds,JoinType.LEFT).fetch(TCaseIcd_.refIcds,JoinType.LEFT);
        query.where(criteriaBuilder.equal(from.get(TCaseDepartment_.caseDetails), detailsId));

        TypedQuery<TCaseDepartment> criteriaQuery = getEntityManager().createQuery(query);
        if(pFetchEager){
            criteriaQuery = addEntityGraph(criteriaQuery);
        }
        List<TCaseDepartment> list = criteriaQuery.getResultList();
        for(TCaseDepartment department: list){
            if(department.getCaseWards() != null){
                Hibernate.initialize(department.getCaseWards());
                Set<TCaseWard> wards = department.getCaseWards();
                wards.forEach((ward) -> {
                    Hibernate.initialize(ward);
                    Set<TCaseIcd> wardIcds = ward.getCaseIcds();
                    if(wardIcds != null){
                        wardIcds.forEach((icd) -> {
                            Hibernate.initialize(icd);
                        });
                    }
                    Set<TCaseOps> wardOps = ward.getCaseOpses();
                    if(wardOps != null){
                        wardOps.forEach((ops) -> {
                            Hibernate.initialize(ops);
                        });
                    }
                });
            }
        }
        return list;
    }

    public TCaseDepartment findEagerById(Long departmentId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseDepartment> query = criteriaBuilder.createQuery(TCaseDepartment.class);

        Root<TCaseDepartment> from = query.from(TCaseDepartment.class);
//        from.fetch(TCaseDepartment_.caseOpses,JoinType.LEFT);
//        from.fetch(TCaseDepartment_.caseIcds,JoinType.LEFT).fetch(TCaseIcd_.refIcds,JoinType.LEFT);
        query.where(criteriaBuilder.equal(from.get(TCaseDepartment_.id), departmentId));

        TypedQuery<TCaseDepartment> criteriaQuery = getEntityManager().createQuery(query);
        criteriaQuery = addEntityGraph(criteriaQuery);
        //return criteriaQuery.getSingleResult();
        return getSingleResultOrNull(criteriaQuery);
    }

    private TypedQuery<TCaseDepartment> addEntityGraph(TypedQuery<TCaseDepartment> criteriaQuery) {
        EntityGraph<TCaseDepartment> toFetch = getEntityManager().createEntityGraph(TCaseDepartment.class);

        toFetch.addSubgraph(TCaseDepartment_.caseOpses);
        toFetch.addSubgraph(TCaseDepartment_.caseIcds).addSubgraph(TCaseIcd_.refIcds.getName());

        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), toFetch);
        return criteriaQuery;
    }
}
