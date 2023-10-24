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
package de.lb.cpx.server.imp.dao;

import de.lb.cpx.server.commonDB.dao.AbstractCommonDao;
import de.lb.cpx.server.commons.dao.AbstractDao;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractImportDao<E extends AbstractEntity> extends AbstractDao<E> {

    @PersistenceContext(unitName = "cpx-import")
    private EntityManager entityManager;

    protected AbstractImportDao(final Class<E> entityClass) {
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

}
