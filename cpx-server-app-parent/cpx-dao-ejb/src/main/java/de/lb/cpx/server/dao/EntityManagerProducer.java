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
 *    2016  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.dao;

import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.server.auth.CaseEntityManagerFactoryCreator;
import de.lb.cpx.server.auth.CaseEntityManagerFactoryCreator.EntityManagerFactoryResult;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commons.dao.AbstractDao;
import de.lb.cpx.server.commons.dao.CommonEntityManager;
import de.lb.cpx.server.commons.dao.CommonEntityManagerDs;
import de.lb.cpx.server.config.CpxServerConfig;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import de.lb.cpx.updatedb.UpdateDbBean;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 *
 * @author Dirk Niemeier
 */
@ApplicationScoped
public class EntityManagerProducer {

    @Inject
    private UpdateDbBean updateDbBean;

    //final private String commonDb = "dbsys_common:cpx_common";
    private synchronized EntityManagerFactory fetchEntityManagerFactory() throws CpxIllegalArgumentException {
        final String database = ClientManager.getActualDatabase();
        return fetchEntityManagerFactory(database);
    }

    private synchronized void updateCommonDb(final String pDatabase) {
        if (!CpxServerConfig.COMMONDB.equalsIgnoreCase(pDatabase)) {
            return;
        }
        if (CaseEntityManagerFactoryCreator.isNew(CpxServerConfig.COMMONDB)) {
            Connection connection = null;
            try {
                connection = CaseEntityManagerFactoryCreator.getJdbcConnection(CpxServerConfig.COMMONDB);
                connection.setAutoCommit(false);
                CpxSystemPropertiesInterface props = CpxSystemProperties.getInstance();
                String updateCommonDbFilename = props.getCpxServerDbUpdateCommonDbFile();
                String updateCommonDbViewsFile = ""; //props.getCpxServerDbUpdateCommonDbViewsDir();
                updateDbBean.startDbUpdate(connection, updateCommonDbFilename, updateCommonDbViewsFile);
                connection.commit();
            } catch (IllegalArgumentException | IOException | SQLException | ParseException ex) {
                if (connection != null) {
                    try {
                        connection.rollback();
                    } catch (SQLException ex1) {
                        Logger.getLogger(EntityManagerProducer.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
                Logger.getLogger(EntityManagerProducer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(EntityManagerProducer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private synchronized EntityManagerFactory fetchEntityManagerFactory(final String pDatabase) throws CpxIllegalArgumentException {
        //System.out.println("fetchEntityManagerFactory ActualDatabase: " + pDatabase);
        updateCommonDb(pDatabase);
        EntityManagerFactoryResult emfResult = CaseEntityManagerFactoryCreator.fetchEntityManagerFactory(pDatabase);
        EntityManagerFactory emf = emfResult.emf;
        return emf;
    }

    public void setConfiguration(final EntityManager pEm) {
        if (pEm == null) {
            return;
        }
        if (AbstractDao.isOracle(pEm)) {
            //2017-05-02 DNi: This is to do case-insensitive LIKE searches :-)
            pEm.createNativeQuery("ALTER SESSION SET NLS_COMP=LINGUISTIC").executeUpdate();
            pEm.createNativeQuery("ALTER SESSION SET NLS_SORT=BINARY_CI").executeUpdate();
        }
    }

    @Produces
    @CpxEntityManager
    @RequestScoped
    EntityManager createEntityManager() throws CpxIllegalArgumentException {
        //EntityManager em = fetchEntityManagerFactory().createEntityManager(SynchronizationType.SYNCHRONIZED);
        EntityManager em = CaseEntityManagerFactoryCreator.getEntityManager(fetchEntityManagerFactory());
        if (em == null) {
            throw new CpxIllegalArgumentException("No entity manager CaseDB found (maybe server was restarted)!");
        }
//        try {
        em.joinTransaction();
//        } catch (TransactionRequiredException ex) {
//            LOG.log(Level.WARNING, "was not able to join JTA transaction");
//            LOG.log(Level.FINEST, null, ex);
//            Session session = AbstractDao.getSession(em);
//            Transaction tx = session.beginTransaction();
//        }
        setConfiguration(em);
        return em;
    }

    @Produces
    @CommonEntityManager
    @RequestScoped
    EntityManager createCommonEntityManager() throws CpxIllegalArgumentException {
        //EntityManager em = fetchEntityManagerFactory().createEntityManager(SynchronizationType.SYNCHRONIZED);
        EntityManager em = CaseEntityManagerFactoryCreator.getEntityManager(fetchEntityManagerFactory(CpxServerConfig.COMMONDB));
        if (em == null) {
            throw new CpxIllegalArgumentException("No entity manager for CommonDB found (maybe server was restarted)!");
        }
//        try {
        em.joinTransaction();
//        } catch (TransactionRequiredException ex) {
//            LOG.log(Level.WARNING, "was not able to join JTA transaction");
//            LOG.log(Level.FINEST, null, ex);
//            Session session = AbstractDao.getSession(em);
//            Transaction tx = session.beginTransaction();
//        }
        setConfiguration(em);
        return em;
    }

    @Produces
    @CommonEntityManagerDs
    @RequestScoped
    DataSource createCommonDataSource() throws CpxIllegalArgumentException {
        //EntityManager em = fetchEntityManagerFactory().createEntityManager(SynchronizationType.SYNCHRONIZED);
        EntityManagerFactory emf = fetchEntityManagerFactory(CpxServerConfig.COMMONDB);
        if (emf == null) {
            return null;
        }
        DataSource ds = (DataSource) emf.getProperties().get("javax.persistence.jtaDataSource"); //probably this way is wrong or has to be adjuuusted
        return ds;
    }

    void closeEntityManager(@Disposes @CpxEntityManager EntityManager entityManager) {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }

    void closeDataSource(@Disposes @CommonEntityManager EntityManager entityManager) {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }

}
