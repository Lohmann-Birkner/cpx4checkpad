/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.server.commons.dao.AbstractEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import javax.persistence.metamodel.SingularAttribute;

@SuppressWarnings("unchecked")
public abstract class AbstractInternalIdDao<E extends AbstractEntity> extends AbstractCommonDao<E> {

    private static final Logger LOG = Logger.getLogger(AbstractInternalIdDao.class.getName());

    public AbstractInternalIdDao(Class<E> entityClass) {
        super(entityClass);
    }

    /**
     * Get sequence for internal id
     *
     * @return sequence id
     */
    public long getNextInternalId() {
        final String sequenceName = getInternalIdSequence();
        return getNextSequenceNumber(sequenceName);
    }

    /**
     * Get items by internal id
     *
     * @param pInternalId reminder subject internal id
     * @return searched reminder subject
     */
    public List<E> findAllByInternalId(final Long pInternalId) {
        final SingularAttribute<E, Long> field = getInternalIdField();
        if (field == null) {
            LOG.log(Level.WARNING, "field is null!");
            return new ArrayList<>();
        }
        if (pInternalId == null) {
            LOG.log(Level.WARNING, "internal id is null!");
            return new ArrayList<>();
        }
//        List<E> list = null;
        final String entityName = getEntityName();
        LOG.log(Level.FINE, "getting all entries of " + entityName + " with internal id " + pInternalId + " on field '" + field.getName() + "'");
        try {
            @SuppressWarnings("unchecked")
            final String queryName = String.format("select u from %s u where %s =:idNbr", entityName, field.getName());
            Query query = getEntityManager().createQuery(queryName);
            query.setParameter("idNbr", pInternalId);
            List<E> list = query.getResultList();
            LOG.log(Level.FINE, "get of all " + entityName + " with internal id " + pInternalId + " successful: " + list.size() + " elements found");
            return list;
        } catch (final RuntimeException re) {
            LOG.log(Level.SEVERE, "get all " + entityName + " with internal id " + pInternalId + " failed", re);
            throw re;
        }
    }

    public E findByInternalId(final Long pInternalId) {
        final List<E> list = findAllByInternalId(pInternalId);
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            LOG.log(Level.WARNING, "Multiple entries found with internal id " + pInternalId + "!");
        }
        return list.iterator().next();
    }

    public abstract SingularAttribute<E, Long> getInternalIdField();

    public abstract String getInternalIdSequence();

//    /**
//     * Get all entries from table
//     *
//     * @return entries list
//     */
//    public abstract List<E> getEntries();
}
