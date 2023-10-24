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
package de.lb.cpx.server.commons.dao;

import de.lb.cpx.server.commons.enums.DbDriverEn;
import de.lb.cpx.service.information.DatabaseInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.jboss.weld.proxy.WeldClientProxy;

public abstract class AbstractDao<E extends AbstractEntity> {

    private static final Logger LOG = Logger.getLogger(AbstractDao.class.getName());

    private final Class<E> entityClass;

    protected AbstractDao(final Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    public String getEntityName() {
        return entityClass.getSimpleName();
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    private String getEntityId(final E instance) {
        if (instance == null) {
            return "null";
        }
        return String.valueOf(instance.id);
    }

    public static Session getSession(final EntityManager em) {
        if (em == null) {
            LOG.log(Level.WARNING, "em is null");
            return null;
        }
        if (em.getDelegate() instanceof WeldClientProxy) {
            return (org.hibernate.Session) (((WeldClientProxy) em.getDelegate()).getMetadata().getContextualInstance());
        } else {
            return (org.hibernate.Session) em.getDelegate();
        }

        //Session session = (Session) em.getDelegate();
        //return session;
    }

    public static Connection getConnection(final EntityManager em) {
        if (em == null) {
            LOG.log(Level.WARNING, "em is null");
            return null;
        }
        ReturningWork<Connection> work = new ReturningWork<Connection>() {
            @Override
            public Connection execute(Connection connection) throws SQLException {
                return connection;
            }
        };

        Session session = getSession(em);
        if (session == null) {
            LOG.log(Level.SEVERE, "session is null!");
            return null;
        }
        return session.doReturningWork(work);
    }

    public static String getConnectionUrl(final Connection pConnection) throws SQLException {
        if (pConnection == null) {
            LOG.log(Level.WARNING, "pConnection is null");
            return "";
        }
        final String str = pConnection.getMetaData().getURL();
        return str;
    }

    public static String getConnectionUrl(final EntityManager em) {
        if (em == null) {
            LOG.log(Level.WARNING, "em is null");
            return "";
        }
        StringBuilder conn_str = new StringBuilder();

        Work work = new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                //final String str = connection.getMetaData().getURL();
                final String str = getConnectionUrl(connection);
                conn_str.append(str);
            }
        };
        //Map<String, Object> lMap = getEntityManager().getEntityManagerFactory().getProperties();

        Session session = getSession(em);
        if (session == null) {
            LOG.log(Level.SEVERE, "session is null!");
            return null;
        }
        session.doWork(work);

        return conn_str.toString();
    }

    public static boolean isOracle(final EntityManager em) {
        String connUrl = getConnectionUrl(em);
        connUrl = connUrl == null ? "" : connUrl.toLowerCase();
        //return connUrl.contains(":oracle:");
        return isOracle(connUrl);
    }

    public static boolean isSqlSrv(final EntityManager em) {
        String connUrl = getConnectionUrl(em);
        connUrl = connUrl == null ? "" : connUrl.toLowerCase();
        //return connUrl.contains(":sqlserver:") || connUrl.contains(":sqlsrv:");
        return isSqlSrv(connUrl);
    }

    public static boolean isOracle(final String pConnectionUrl) {
        return DbDriverEn.isOracle(pConnectionUrl);
//        String connUrl = (pConnectionUrl == null) ? "" : pConnectionUrl.toLowerCase().trim();
//        return connUrl.contains(":oracle:");
    }

    public static boolean isSqlSrv(final String pConnectionUrl) {
        return DbDriverEn.isSqlSrv(pConnectionUrl);
//        String connUrl = (pConnectionUrl == null) ? "" : pConnectionUrl.toLowerCase().trim();
//        return connUrl.contains(":sqlserver:") || connUrl.contains(":sqlsrv:");
    }

    public static DbDriverEn getDbDriver(final String pConnectionUrl) {
        return DbDriverEn.findByConnectionUrl(pConnectionUrl);
//        if (isSqlSrv(pConnectionUrl)) {
//            return DbDriver.SQLSRV;
//        }
//        if (isOracle(pConnectionUrl)) {
//            return DbDriver.ORACLE;
//        }
//        return null;
    }

    public void clear() {
        LOG.log(Level.FINE, "perform clear");
        EntityManager em = getEntityManager();
        if (em != null) {
            em.clear();
        }
    }

    public DatabaseInfo getDatabaseInfo() {
        return new DatabaseInfo(
                getConnectionString(),
                getConnectionUrl(),
                getConnectionDatabaseVendor(),
                //caseDao.isOracle(), caseDao.isSqlSrv(),
                getDatabaseVersion()
        );
    }

    public String getDbIdentifier() {
        return getDatabaseInfo().getIdentifier();
    }

    public Session getSession() {
        return getSession(getEntityManager());
    }

    public Connection getConnection() {
        return getConnection(getEntityManager());
    }

    public PreparedStatement prepareStatement(final String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    public PreparedStatement prepareStatement(final String sql, final int pFetchSize) throws SQLException {
        PreparedStatement pstmt = prepareStatement(sql);
        pstmt.setFetchSize(pFetchSize);
        return pstmt;
    }

    public Statement createStatement() throws SQLException {
        return getConnection().createStatement();
    }

    public Statement createStatement(final int pFetchSize) throws SQLException {
        Statement stmt = createStatement();
        stmt.setFetchSize(pFetchSize);
        return stmt;
    }

    public static String getConnectionDatabase(final Connection connection, final String pConnectionString) {
        if (connection == null) {
            LOG.log(Level.WARNING, "connection is null");
            return "";
        }
        String connectionDatabase = "";
        //String connectionString = getConnectionUrl();
        String connectionString = pConnectionString;
        try {
            if (isOracle(connectionString)) {
                //Oracle
                connectionDatabase = connection.getMetaData().getUserName();
            } else {
                //SqlSrv
                connectionString = (connectionString == null) ? "" : connectionString.trim();
                String token = "databaseName=";
                final int pos = connectionString.indexOf(token);
                String tmp;
                if (pos > -1) {
                    tmp = connectionString.substring(pos + token.length()).trim();
                } else {
                    tmp = connectionString;
                }
                if (tmp.contains(";")) {
                    tmp = tmp.substring(0, tmp.indexOf(';'));
                }
                connectionDatabase = tmp.trim();
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return connectionDatabase.trim();
    }

    public static String getConnectionDatabase(final EntityManager em) {
        if (em == null) {
            return "";
        }
        String connectionString = getConnectionUrl(em);
        try ( Connection connection = getConnection(em)) {
            return getConnectionDatabase(connection, connectionString);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return "";
        }
    }

    /**
     * returns something like dbsys1:CPX01
     *
     * @return Connection string
     */
    public abstract String getConnectionString();

    /**
     * returns something like CPX01
     *
     * @return database name
     */
    public String getConnectionDatabase() {
        return getConnectionDatabase(getConnection(), getEntityManager());
    }

    public static String getConnectionDatabase(final Connection connection, final EntityManager em) {
        final String connectionString = getConnectionUrl(em);
        return getConnectionDatabase(connection, connectionString);
    }

    public static String getConnectionDatabaseVendor(final EntityManager em) {
        if (isOracle(em)) {
            return "Oracle";
        }
        if (isSqlSrv(em)) {
            return "Microsoft SQL Server";
        }
        return "Unknown vendor";
    }

    public String getConnectionDatabaseVendor() {
        return getConnectionDatabaseVendor(getEntityManager());
//        if (isOracle()) {
//            return "Oracle";
//        }
//        if (isSqlSrv()) {
//            return "Microsoft SQL Server";
//        }
//        return "Unknown vendor";
    }

    public String getConnectionUrl() {
        return getConnectionUrl(getEntityManager());
    }

    public boolean isOracle() {
        return isOracle(getEntityManager());
    }

    public boolean isSqlSrv() {
        return isSqlSrv(getEntityManager());
    }

    public static String getDatabaseVersion(final EntityManager em) {
        final boolean isOracle = isOracle(em);
        final boolean isSqlSrv = isSqlSrv(em);
        final String version;
        if (isOracle || isSqlSrv) {
            final String query;
            if (isOracle) {
                query = "SELECT BANNER FROM v$version WHERE banner LIKE 'Oracle%'";
            } else {
                query = "SELECT @@VERSION";
            }
            Query qry = em.createNativeQuery(query);
            Object obj = qry.getSingleResult();
            if (obj == null) {
                version = "cannot retrieve database version";
            } else {
                version = (String) obj;
            }
        } else {
            version = "unsupported database version";
        }
        return version;
    }

    public String getDatabaseVersion() {
        return getDatabaseVersion(getEntityManager());
    }

    public List<E> findByIds(final Long[] pIds) {
        if (pIds == null || pIds.length == 0) {
            return new ArrayList<>();
        }
        final List<Long> ids = new ArrayList<>();
        for (Long id : pIds) {
//            if (id == null /* || id.equals(0L) */) {
//                continue;
//            }
            ids.add(id);
        }
        return findByIds(ids);
    }

    public List<E> findByIds(final long[] pIds) {
        if (pIds == null || pIds.length == 0) {
            return new ArrayList<>();
        }
        final List<Long> ids = new ArrayList<>();
        for (long id : pIds) {
//            if (id == 0L) {
//                continue;
//            }
            ids.add(id);
        }
        return findByIds(ids);
    }

    public List<E> findByIds(final Collection<Long> pIds) {
        final String entityName = getEntityName();
        LOG.log(Level.FINE, "getting {0} instances with ids {1}", new Object[]{entityName, pIds});
        if (pIds == null || pIds.isEmpty()) {
            LOG.log(Level.WARNING, "collection of ids either null or empty!");
            return new ArrayList<>();
        }
        final Set<Long> ids = new TreeSet<>(pIds); //use set to remove duplicated ids and use tree set to order values
        Iterator<Long> it = ids.iterator();
        while (it.hasNext()) {
            Long id = it.next();
            if (id == null || id.equals(0L)) {
                it.remove();
            }
        }
        if (ids.isEmpty()) {
            LOG.log(Level.WARNING, "don''t pass only null values or values that are equal to 0L");
            return new ArrayList<>();
        }
        final StringBuilder sb = new StringBuilder("from " + entityName);
        sb.append(" where id in (:ids)");
        Query query = getEntityManager().createQuery(sb.toString());
        query.setParameter("ids", ids);
        List<E> result = query.getResultList();
        LOG.log(Level.FINEST, "found {0} instances of {1} for {2} passed valid unique ids", new Object[]{result.size(), entityName, ids.size()});
        return result;
    }

    public E findById(final long id) {
        final String entityName = getEntityName();
        LOG.log(Level.FINE, "getting {0} instance with id {1}", new Object[]{entityName, id});
        if (id == 0L) {
            LOG.log(Level.WARNING, "id is equal to 0");
            return null;
        }
        try {
            @SuppressWarnings("unchecked")
            final E instance = getEntityManager().find(entityClass, id);
            LOG.log(Level.FINE, "get {0} with id {1} successful", new Object[]{entityName, id});
            return instance;
        } catch (final RuntimeException re) {
            LOG.log(Level.SEVERE, MessageFormat.format("get {0} with id {1} failed", entityName, id), re);
            throw re;
        }
    }

    public List<E> findAll() {
        final String entityName = getEntityName();
        LOG.log(Level.FINE, "getting all entries of " + entityName);
        try {
            @SuppressWarnings("unchecked")
            final List<E> list = getEntityManager().createQuery("from " + entityName).getResultList();
            LOG.log(Level.FINE, "get of all " + entityName + " successful: " + (list == null ? "null" : list.size()) + " elements found");
            return list;
        } catch (final RuntimeException re) {
            LOG.log(Level.SEVERE, "get all " + entityName + " failed", re);
            throw re;
        }
    }

    /**
     * returns list of IDs.
     *
     * @return IDs
     */
    public List<Long> getAllIds() {
        final String entityName = getEntityName();
        final String queryName = String.format("select id from %s c", entityName);
        final TypedQuery<Long> query = getEntityManager().createQuery(queryName, Long.class);
        return query.getResultList();
    }

    protected abstract EntityManager getEntityManager();

    protected E getSingleResultOrNull(final TypedQuery<E> pQuery) {
        if (pQuery == null) {
            LOG.log(Level.WARNING, "pQuery is null");
            return null;
        }
        final List<E> res = pQuery.getResultList();
        if (res.isEmpty()) {
            return null;
        }
        if (res.size() > 1) {
            LOG.log(Level.SEVERE, "getSingleResultOrNull found {0} results, but expected zero or one result. This is ambiguous and can cause several side effects like constraint exceptions!", res.size());
        }
        return res.get(0);
    }

    protected <T> T getSingleResultOrNull(final Query pQuery) {
        if (pQuery == null) {
            LOG.log(Level.WARNING, "pQuery is null");
            return null;
        }
        @SuppressWarnings("unchecked")
        final List<T> res = pQuery.getResultList();
        if (res.isEmpty()) {
            return null;
        }
        if (res.size() > 1) {
            LOG.log(Level.SEVERE, "getSingleResultOrNull found {0} results, but expected zero or one result. This is ambiguous and can cause several side effects like constraint exceptions!", res.size());
        }
        return res.get(0);
    }

    public E merge(final E pDetachedInstance) {
        final String entityName = getEntityName();
        final String entityId = getEntityId(pDetachedInstance);
        LOG.log(Level.FINE, "merging of " + entityName + " instance with id " + entityId);
        if (pDetachedInstance == null) {
            LOG.log(Level.WARNING, "pDetachedInstance is null");
            return null;
        }
        try {
            final E result = getEntityManager().merge(pDetachedInstance);
            LOG.log(Level.FINE, "merge of " + entityName + " with id " + entityId + " successful");
            return result;
        } catch (final RuntimeException re) {
            LOG.log(Level.SEVERE, "merge of " + entityName + " with id " + entityId + " failed", re);
            throw re;
        }
    }

    /**
     * Attempt to merge(update) List of detachedInstances of the Entity Calls
     * flush and Clear after 20 Entities processed this is the best way
     *
     * @param pListOfDetachedInstance ,List of detached Instances
     * @return List of attached Entities
     */
    public List<E> mergeList(final List<E> pListOfDetachedInstance) {
        LOG.log(Level.FINE, "merging list with " + (pListOfDetachedInstance == null ? "null" : pListOfDetachedInstance.size() + " elements"));
        List<E> mList = new ArrayList<>();
        if (pListOfDetachedInstance == null) {
            LOG.log(Level.WARNING, "pListOfDetachedInstance is null");
            return mList;
        }
        int i = 0;
        for (final E instance : pListOfDetachedInstance) {
            E mergedInstance = merge(instance);
            mList.add(mergedInstance);
            //Try to flush after 20 Entries
            if (++i % 20 == 0) {
                getEntityManager().flush();
                getEntityManager().clear();
            }
        }
        return mList;
    }

    public void persist(final E pTransientInstance) {
        final String entityName = getEntityName();
        final String entityId = getEntityId(pTransientInstance);
        LOG.log(Level.FINE, "persisting of " + entityName + " instance with id " + entityId);
        if (pTransientInstance == null) {
            LOG.log(Level.WARNING, "pTransientInstance is null");
            return;
        }
        try {
            getEntityManager().persist(pTransientInstance);
            LOG.log(Level.FINE, "persist of " + entityName + " with id " + entityId + " successful");
        } catch (final RuntimeException re) {
            LOG.log(Level.SEVERE, "persist of " + entityName + " with id " + entityId + " failed", re);
            throw re;
        }
    }

    public boolean deleteById(final long id) {
        final String entityName = getEntityName();
        LOG.log(Level.FINE, "removing " + entityName + " instance with id " + id);
        if (id == 0L) {
            LOG.log(Level.WARNING, "id is equal to 0");
            return true; //okay, that can be discussed, but it is not an error, right?
        }
        try {
            Query query = getEntityManager()
                    .createQuery("delete from " + entityName + " e where e.id = :id");
            query.setParameter("id", id);
            int affectedRows = query.executeUpdate();
            if (affectedRows > 0) {
                LOG.log(Level.FINE, "delete of " + entityName + " with id " + id + " successful");
                return true;
            } else {
                LOG.log(Level.WARNING, "no " + entityName + " was deleted, because there seems to be exist no entry with id " + id);
                return false;
            }
        } catch (final RuntimeException re) {
            String message = "remove of " + entityName + " failed for id " + id + " with this error: " + re.getMessage();
            LOG.log(Level.INFO, message, re);
            throw new RuntimeException(message, re);
        }
    }

    /**
     * SSk. This function dont't works sometimes. As alternative use
     * deleteById(...) Awi 20170526: if methode does not work check References
     * in the Entity. DeleteById(...) should not be used some Entities uses
     * Hibernate Lifecycle Methodes as preRemove with DeleteById these Methodes
     * are not called
     *
     * @param pPersistentInstance instance
     */
    public void remove(final E pPersistentInstance) {
        final String entityName = getEntityName();
        final String entityId = getEntityId(pPersistentInstance);
        LOG.log(Level.FINE, "removing " + entityName + " instance with id " + entityId);
        if (pPersistentInstance == null) {
            LOG.log(Level.WARNING, "pPersistentInstance is null");
            return;
        }
        try {
            getEntityManager().remove(pPersistentInstance);
            LOG.log(Level.FINE, "remove " + entityName + " with id " + entityId + " successful");
        } catch (final RuntimeException re) {
            LOG.log(Level.SEVERE, "remove " + entityName + " with id " + entityId + " failed", re);
            throw re;
        }
    }

    public void remove(final long id) {
        remove(findById(id));
        /*
    log.debug("removing instance");
    try {
      getEntityManager().remove(findById(id));
      log.debug("remove successful");
    } catch (final RuntimeException re) {
      log.error("remove failed", re);
      throw re;
    }
         */
    }

    public void flushAndClear() {
        LOG.log(Level.FINE, "perform flush and clear");
        flush();
        clear();
    }

    /**
     * flush all changes
     */
    public void flush() {
        LOG.log(Level.FINE, "perform flush");
        EntityManager em = getEntityManager();
        if (em != null) {
            em.flush();
        }
    }

    /**
     * refresh Entity with new Database Values, Entity must be attached to the
     * PersitanceContext!
     *
     * @param pRefreshInstance entity to refresh
     */
    public void refresh(final E pRefreshInstance) {
        final String entityName = getEntityName();
        final String entityId = getEntityId(pRefreshInstance);
        LOG.log(Level.FINE, "refreshing " + entityName + " instance with id " + entityId);
        if (pRefreshInstance == null) {
            LOG.log(Level.WARNING, "pRefreshInstance is null");
            return;
        }
        getEntityManager().refresh(pRefreshInstance);
    }

    /**
     * Save or Update Instance, checks with findById if there is already an
     * instance of the entity, if not persist otherwise merge if Ibect not an
     * abstractEntity, null is returned
     *
     * @param pInstance instance to save or update
     * @return saved or updated instance
     */
//  public void saveOrUpdate(final E instance){
//      getSession().saveOrUpdate(instance);
//  }
    public E saveOrUpdate(final E pInstance) {
        final String entityName = getEntityName();
        final String entityId = getEntityId(pInstance);
        LOG.log(Level.FINE, "saving or updating " + entityName + " instance with id " + entityId);
//        if (!(pInstance instanceof AbstractEntity)) {
//            LOG.log(Level.WARNING, "instance with id " + entityId + " is not an AbstractEntity but " + (pInstance == null ? "null" : pInstance.getClass().getSimpleName()));
//            return null;
//        }
//        if(exists(((AbstractEntity)instance).id)){
//            return merge(instance);
//        }
//        persist(instance);
        getSession().saveOrUpdate(pInstance);
        return pInstance;
    }

    /**
     * checks with the find methode if an entity with the given id(primary key)
     * is presend in the database
     *
     * @param id primary key for the entity to check
     * @return indicator if an entity with that id already exists
     */
    public boolean exists(final long id) {
        final String entityName = getEntityName();
        LOG.log(Level.FINE, "does " + entityName + " instance with id " + id + " exist?");
        if (id == 0L) {
            LOG.log(Level.WARNING, "id is equal to 0");
            return false;
        }
        final boolean doesExist = getEntityManager().find(entityClass, id) != null;
        LOG.log(Level.FINE, "does " + entityName + " instance with id " + id + " exist? -> " + (doesExist ? "Yope" : "Nope"));
        return doesExist;
    }

    /**
     * checks with the find methode if an entity with the given id(primary key)
     * is presend in the database
     *
     * @param pTable table name
     * @param id primary key for the entity to check
     * @return indicator if an entity with that id already exists
     */
    public boolean exists(final String pTable, final long id) {
        //final String entityName = getEntityName();
        LOG.log(Level.FINE, "does instance with id {0} exist in table {1}?", new Object[]{id, pTable});
        if (id == 0L) {
            LOG.log(Level.WARNING, "id is equal to 0");
            return false;
        }
        if (pTable == null || pTable.trim().isEmpty()) {
            LOG.log(Level.WARNING, "table name is null or empty");
            return false;
        }
        final String query = "SELECT 1 FROM " + pTable + " WHERE ID = " + id;
        Query qry = getEntityManager().createNativeQuery(query);
        return !qry.getResultList().isEmpty();
    }

    /**
     * checks if that instance with its id is already presend in the database,
     * returns null if instance is not an instance of AbstractEntity. Uses the
     * exists(long id) methode to perform the check
     *
     * @param pInstance instance to check
     * @return indicator if the instance already exists
     */
    public boolean exists(final E pInstance) {
//        final String entityId = getEntityId(pInstance);
//        if (!(pInstance instanceof AbstractEntity)) {
//            LOG.log(Level.WARNING, "instance with id " + entityId + " is not an AbstractEntity but " + (pInstance == null ? "null" : pInstance.getClass().getSimpleName()));
//            return false;
//        }
//        return exists(((AbstractEntity) pInstance).id);
        final String entityName = getEntityName();
        final String entityId = getEntityId(pInstance);
        LOG.log(Level.FINE, "does " + entityName + " instance with id " + entityId + " exist?");
        if (pInstance == null) {
            LOG.log(Level.WARNING, "pInstance is null");
            return false;
        }
        return exists(pInstance.id);
    }

    /**
     * Attempt to attach detached Entity to SessionContext. Read-Access only
     *
     * @param pInstance detached Entity
     * @return attached instance
     */
    public E attach(final E pInstance) {
        final String entityName = getEntityName();
        final String entityId = getEntityId(pInstance);
        LOG.log(Level.FINE, "attaching " + entityName + " instance with id " + entityId);
        if (pInstance == null) {
            LOG.log(Level.WARNING, "pInstance is null");
            return null;
        }
        if (getSession().contains(pInstance)) {
            LOG.log(Level.FINE, "instance " + entityName + " with id " + entityId + " is already attached");
            return pInstance;
        }
        getSession().buildLockRequest(LockOptions.NONE).lock(pInstance);

        return pInstance;
    }

    /**
     * Attempt to attach detached Entity to SessionContext. Read-Access only
     *
     * @param pInstance detached Entity
     * @param pOption lockOption for entity
     * @return attached instance
     */
    public E attach(final E pInstance, final LockOptions pOption) {
        final String entityName = getEntityName();
        final String entityId = getEntityId(pInstance);
        LOG.log(Level.FINE, "attaching " + entityName + " instance with id " + entityId + " and option " + (pOption == null ? "null" : pOption.getLockMode()));
        if (pInstance == null) {
            LOG.log(Level.WARNING, "pInstance is null");
            return null;
        }
        if (pOption == null) {
            LOG.log(Level.WARNING, "pOption is null");
            return null;
        }
        if (getSession().contains(pInstance)) {
            LOG.log(Level.FINE, "instance " + entityName + " with id " + entityId + " is already attached");
            return pInstance;
        }
        getSession().buildLockRequest(pOption).lock(pInstance);

        return pInstance;
    }

    /**
     * update instance on Hibernate Session
     *
     * @param pInstance detached Entity
     */
    public void update(final E pInstance) {
        final String entityName = getEntityName();
        final String entityId = getEntityId(pInstance);
        LOG.log(Level.FINE, "updating " + entityName + " instance with id " + entityId);
        if (pInstance == null) {
            LOG.log(Level.WARNING, "pInstance is null");
            return;
        }
        getSession().update(pInstance);
    }

    /**
     * Merge and Flush Detached Entity and return updated instance
     *
     * @param pInstance detached instance
     * @return updated instance, possibly still detached!
     */
    public E mergeAndFlush(final E pInstance) {
        final String entityName = getEntityName();
        final String entityId = getEntityId(pInstance);
        LOG.log(Level.FINE, "merging and flushing " + entityName + " instance with id " + entityId);
        if (pInstance == null) {
            LOG.log(Level.WARNING, "pInstance is null");
            return null;
        }
        E newInstance = merge(pInstance);
        flush();
        return newInstance;
    }

    public void deleteItems(final List<E> pItems) {
        LOG.log(Level.FINE, "deleting list with " + (pItems == null ? "null" : pItems.size() + " elements"));
        if (pItems == null) {
            LOG.log(Level.WARNING, "pItems is null");
            return;
        }
        int i = 0;
        for (E instance : pItems) {
            if (++i % 25 == 0) {
                getEntityManager().flush();
            }
            remove(instance);
        }
    }

    public boolean isDetached(final E pInstance) {
        final String entityName = getEntityName();
        final String entityId = getEntityId(pInstance);
        LOG.log(Level.FINE, "is " + entityName + " instance with id " + entityId + " detached?");
        if (pInstance == null) {
            LOG.log(Level.WARNING, "pInstance is null");
            return true;
        }
        final boolean isDetached = !getEntityManager().contains(pInstance);
        LOG.log(Level.FINE, "is " + entityName + " instance with id " + entityId + " detached? -> " + (isDetached ? "Yope" : "Nope"));
        return isDetached;
    }

    public void detach(final E pInstance) {
        final String entityName = getEntityName();
        final String entityId = getEntityId(pInstance);
        LOG.log(Level.FINE, "detach " + entityName + " instance with id " + entityId);
        if (pInstance == null) {
            LOG.log(Level.WARNING, "pInstance is null");
            return;
        }
        getEntityManager().detach(pInstance);
    }

    public void evict(final E pInstance) {
        final String entityName = getEntityName();
        final String entityId = getEntityId(pInstance);
        LOG.log(Level.FINE, "evicting " + entityName + " instance with id " + entityId);
        if (pInstance == null) {
            LOG.log(Level.WARNING, "pInstance is null");
            return;
        }
        getSession().evict(pInstance);
    }

    /**
     * @param pMode change the flushmode in the current session WARNING:
     * changing flushmode will effect behavior in the whole Session/Transaction,
     * it will change mode for ALL Daos
     */
    public void changeFlushMode(final FlushModeType pMode) {
        if (pMode == null) {
            LOG.log(Level.WARNING, "pMode is null");
            return;
        }
        LOG.log(Level.FINE, "Change flush mode to " + pMode.name());
        getSession().setFlushMode(pMode);
    }

    public FlushModeType getFlushMode() {
        return getSession().getFlushMode();
    }

    public int executeUpdate(final String pQuery) {
        return getEntityManager().createNativeQuery(pQuery).executeUpdate();
    }

    /**
     * get the next sequence number
     *
     * @param pSequenceName sequence name
     * @return new sequence number
     */
    public long getNextSequenceNumber(final String pSequenceName) {
        //final String sequenceName = "CDB_SEQUENCE_SQ";
        LOG.log(Level.FINEST, "Get next sequence number for '" + pSequenceName + "'...");
        final String query;
        if (isOracle()) {
            query = String.format("SELECT %s.nextval SEQUENCE_NUMBER FROM DUAL", pSequenceName);
        } else {
            query = String.format("SELECT NEXT VALUE FOR %s SEQUENCE_NUMBER", pSequenceName);
        }
        ReturningWork<Long> work = new ReturningWork<Long>() {
            @Override
            public Long execute(Connection connection) throws SQLException {
                //SQLXML xmlData = connection.createSQLXML();
                //xmlData.setString(props);
                Long sequenceNumber = null;
                try ( PreparedStatement stmt = connection.prepareStatement(query)) {
                    //stmt.setSQLXML(1, xmlData);
                    //stmt.setLong(1, pUserId);
                    try ( ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            sequenceNumber = rs.getLong("SEQUENCE_NUMBER");
                            break;
                        }
                    }
                    return sequenceNumber;
                }
            }
        };

        Session session = getSession();
        Long val = session.doReturningWork(work);
        LOG.log(Level.FINEST, "Next sequence number for '" + pSequenceName + "' is " + val);
        return val;
    }

    /**
     * Add new item
     *
     * @param pItem item
     * @return id for new item
     */
    public long addNewItem(E pItem) {
        if (pItem == null) {
            LOG.log(Level.WARNING, "item is null!");
            return 0L;
        }
        persist(pItem);
        flush();
        long id = pItem.id;
        return id;
    }

    /**
     * update item
     *
     * @param pItem item to update
     * @return state of update process
     */
    public boolean updateItem(final E pItem) {
        boolean checkUpdateState;
        if (pItem == null || pItem.id <= 0L) {
            checkUpdateState = false;
        } else {
            merge(pItem);
            checkUpdateState = true;
        }
        return checkUpdateState;
    }

    /**
     * update the item list
     *
     * @param pItems item list to update
     * @return state of update process
     */
    public boolean updateItemList(final List<E> pItems) {
        boolean checkUpdateState;
        if (pItems == null || pItems.isEmpty()) {
            checkUpdateState = false;
        } else {
            mergeList(pItems);
            checkUpdateState = true;
        }
        return checkUpdateState;
    }

    /**
     * Import new item list
     *
     * @param pItems list of items
     * @return number of added items
     */
    public int importItemList(List<E> pItems) {
        int itemNumber = 0;
        for (E item : pItems) {
            if (item == null) {
                continue;
            }
            persist(item);
            itemNumber++;
        }
        return itemNumber;
    }

}
