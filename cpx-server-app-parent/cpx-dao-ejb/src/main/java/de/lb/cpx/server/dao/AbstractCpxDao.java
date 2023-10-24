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

import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.dao.AbstractCommonDao;
import de.lb.cpx.server.commons.dao.AbstractDao;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import javax.inject.Inject;
import javax.persistence.EntityManager;

public abstract class AbstractCpxDao<E extends AbstractEntity> extends AbstractDao<E> {

    @Inject
    @CpxEntityManager
    private EntityManager entityManager;

    protected AbstractCpxDao(final Class<E> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void persist(final E transientInstance) {
        AbstractCommonDao.fillUser(transientInstance);
        super.persist(transientInstance);
    }

    @Override
    public E merge(final E detachedInstance) {
        AbstractCommonDao.fillUser(detachedInstance);
        return super.merge(detachedInstance);
    }

    @Override
    public E saveOrUpdate(final E instance) {
        AbstractCommonDao.fillUser(instance);
        return super.saveOrUpdate(instance);
    }

    @Override
    public String getConnectionString() {
        return ClientManager.getActualDatabase();
    }

}
