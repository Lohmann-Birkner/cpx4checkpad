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
package de.lb.cpx.common.dao;

import de.lb.cpx.server.commonDB.dao.AbstractCommonDao;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.server.commons.test.dao.DBUtil;
import de.lb.cpx.server.commons.test.dao.TransactionManager;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.After;
import org.junit.Before;

/**
 *
 * @author gerschmann
 * @param <D> DAO Class
 * @param <E> Entity Class
 */
public abstract class AbstractCommonDaoTest<D extends AbstractCommonDao<E>, E extends AbstractEntity> {

    private TransactionManager transactionManager;

    private final D dao;

    protected AbstractCommonDaoTest(final D dao) {
        this.dao = dao;
    }

    protected abstract E createTransientInstance();

    protected D getDao() {
        return dao;
    }

    @Before
    public void setUp() throws IllegalAccessException {
        transactionManager = new TransactionManager("test");
        DBUtil.executeSQLScripts(transactionManager);

        FieldUtils.writeField(dao, "entityManager", transactionManager.getEntityManager(), true);

        transactionManager.beginTransaction();

    }

    @After
    public void tearDown() {
        if (transactionManager != null) {
            transactionManager.commitTransaction();
            transactionManager.close();
        }
    }

//  @Test
//  public void testFindById() {
//    E entity = createTransientInstance();
//    dao.persist(entity);
//    assertNotNull(dao.findById(1L));
//  }
    /*  @Test
  public void testPersist() {
    dao.persist(createTransientInstance());
    dao.persist(createTransientInstance());
  }

  @Test
  public void testRemove() {
    dao.persist(createTransientInstance());
    final E entity = dao.findById(1L);
    dao.remove(entity);
  }
     */
}
