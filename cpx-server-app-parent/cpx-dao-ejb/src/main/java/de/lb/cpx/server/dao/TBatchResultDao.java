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

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.TBatchResult;
import de.lb.cpx.model.TBatchResult2Role;
import de.lb.cpx.model.TBatchResult2Role_;
import de.lb.cpx.model.TBatchResult_;
import de.lb.cpx.server.commons.enums.EntityGraphType;
import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import org.hibernate.Hibernate;

/**
 *
 * @author Husser
 */
@Stateless
public class TBatchResultDao extends AbstractCpxDao<TBatchResult> {

    public TBatchResultDao() {
        super(TBatchResult.class);
    }

    /**
     * finds Entry in the T_BATCH_RESULT table to the grouping model and local
     * flag
     *
     * @param model grouping model
     * @param isLocal flag to distinguish the grouping results for local or
     * extern cases
     * @return returns entry to this parameter or null
     */
    public TBatchResult findResult2ModelAndLocalFlag(final GDRGModel model, boolean isLocal) {
        final TypedQuery<TBatchResult> query;
        query = getEntityManager().createQuery(
                " select b from TBatchResult b where b.modelIdEn = :modelIdEn "
                + "and b.batchresIsLocalFl = :batchresIsLocalFl",
                TBatchResult.class);
        query.setParameter("modelIdEn", model);
        query.setParameter("batchresIsLocalFl", isLocal);

        return getSingleResultOrNull(query);
    }

    public void setNewResult(final TBatchResult newResult) {
        TBatchResult oldResult = findResult2ModelAndLocalFlag(newResult.getModelIdEn(), newResult.isBatchresIsLocalFl());
        if (oldResult != null) {
            getEntityManager().remove(getEntityManager().merge(oldResult));
        }
        persist(newResult);

    }

    public TBatchResult loadEagerly(final long pId) {
        TBatchResult result = findById(pId);
        if (result != null) {
            Hibernate.initialize(result.getBatchres2role());
        }
        return result;
    }

    public TBatchResult findResults2ModelLocalFlagAndRole(GDRGModel grouperModel, Boolean local, long roleId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TBatchResult> query = criteriaBuilder.createQuery(TBatchResult.class);
        Root<TBatchResult> from = query.from(TBatchResult.class);

        SetJoin<TBatchResult, TBatchResult2Role> join = from.join(TBatchResult_.batchres2role, JoinType.LEFT);
        query.where(criteriaBuilder.equal(from.get(TBatchResult_.modelIdEn), grouperModel),
                //                    criteriaBuilder.equal(from.get(TBatchResult_.batchresIsLocalFl),local),
                criteriaBuilder.equal(join.get(TBatchResult2Role_.roleId), roleId));

        TypedQuery<TBatchResult> criteriaQuery = getEntityManager().createQuery(query);

        EntityGraph<TBatchResult> fetchAll = getEntityManager().createEntityGraph(TBatchResult.class);
        fetchAll.addSubgraph(TBatchResult_.batchres2role).addSubgraph(TBatchResult2Role_.batchCheckResult.getName());
        criteriaQuery.setHint(EntityGraphType.getLoadGraphType(), fetchAll);

        return getSingleResultOrNull(criteriaQuery);
    }

}
