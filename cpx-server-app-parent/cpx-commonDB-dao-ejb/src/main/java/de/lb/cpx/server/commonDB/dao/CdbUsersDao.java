/**
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.server.commonDB.model.CdbUser2Role_;
import de.lb.cpx.server.commonDB.model.CdbUsers;
import de.lb.cpx.server.commonDB.model.CdbUsers_;
import de.lb.cpx.server.commons.enums.EntityGraphType;
import de.lb.cpx.service.properties.UserProperties;
import de.lb.cpx.shared.dto.UserDTO;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;

/**
 * Data access object for domain model class CdbUsers. Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
//@SecurityDomain("other")
//@PermitAll
public class CdbUsersDao extends AbstractCommonDao<CdbUsers> {

    private static final Logger LOG = Logger.getLogger(CdbUsersDao.class.getName());
    private static final long BATCHJOB_USER_STARTS_WITH_ID = 1000000;

    /**
     * Creates a new instance.
     */
    public CdbUsersDao() {
        super(CdbUsers.class);
    }

//    /**
//     * Method which fills availableDatasources list with DataSource references.
//     * References are retrieved via JMX. From
//     * https://fascynacja.wordpress.com/2013/08/08/jboss-7-retrieve-list-of-configured-datasources-at-runtime/
//     *
//     * @param availableDatasources - list to which method will add datasoruces
//     * @param server - reference to mbean server
//     * @param dataSourceKeyName - part of jmx name which will be used to
//     * retrieve the datasource mbeans
//     */
//    private void addDatasourcesForKeyName(List<DataSource> availableDatasources, MBeanServer server, String dataSourceKeyName) throws MalformedObjectNameException, NamingException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException {
//
//        // Get the starting point of the namespace 
//        Context ctx = new InitialContext();
//
//        // create jmx filter name eg. (3)
//        // "jboss.as:subsystem=datasources,xa-data-source=*" 
//        final ObjectName filterName = new ObjectName("jboss.as:subsystem=datasources," + dataSourceKeyName + "=*");
//
//        // get the results matching given filter. (4) 
//        final Set<ObjectInstance> mBeans = server.queryMBeans(filterName, null);
//
//        // iterate over mbeans and retrieve information about jndi-name of datasource 
//        for (final ObjectInstance mBean : mBeans) {
//
//            // get name for mbean describing current datasource mbean   
//            ObjectName mbeanName = mBean.getObjectName();
//
//            // one of the attributes of mbean is 
//            // "jndiName" , we will read that attribute now:(5) 
//            String bindName = (String) server.getAttribute(mbeanName, "jndiName");
//
//            // having the jndi-name, we can look up the datasource instance 
//            DataSource ds = (DataSource) ctx.lookup(bindName);
//            availableDatasources.add(ds);
//        }
//    }
    public boolean setActualDatabase(final Long pUserId, final String pDatabase) {
        final String database = (pDatabase == null) ? "" : pDatabase.trim();

        ReturningWork<Boolean> work = new ReturningWork<Boolean>() {
            @Override
            public Boolean execute(Connection connection) throws SQLException {
                //SQLXML xmlData = connection.createSQLXML();
                //xmlData.setString(props);
                String query = "UPDATE CDB_USERS SET U_DATABASE = ? WHERE CDB_USERS.ID = ? ";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    //stmt.setSQLXML(1, xmlData);
                    stmt.setString(1, database);
                    stmt.setLong(2, pUserId);
                    stmt.executeUpdate();
                    return true;
                }
            }
        };

        Session session = getSession();
        return session.doReturningWork(work);
    }

    public String getActualDatabase(final Long pUserId) {
        ReturningWork<String> work = new ReturningWork<String>() {
            @Override
            public String execute(Connection connection) throws SQLException {
                //SQLXML xmlData = connection.createSQLXML();
                //xmlData.setString(props);
                String database = "";
                String query = "SELECT U_DATABASE FROM CDB_USERS WHERE CDB_USERS.ID = ? ";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    //stmt.setSQLXML(1, xmlData);
                    stmt.setLong(1, pUserId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            database = rs.getString("U_DATABASE");
                            break;
                        }
                    }
                    return database;
                }
            }
        };

        Session session = getSession();
        return session.doReturningWork(work);
    }

    public boolean setActualRole(final Long pUserId, final Long pRoleId) {
        final Long roleId = (pRoleId != null && pRoleId.equals(0L)) ? null : pRoleId;

        ReturningWork<Boolean> work = new ReturningWork<Boolean>() {
            @Override
            public Boolean execute(Connection connection) throws SQLException {
                //SQLXML xmlData = connection.createSQLXML();
                //xmlData.setString(props);
                String query = "UPDATE CDB_USERS SET U_ROLE_ID = ? WHERE CDB_USERS.ID = ? ";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    //stmt.setSQLXML(1, xmlData);
                    stmt.setLong(1, roleId);
                    stmt.setLong(2, pUserId);
                    stmt.executeUpdate();
                    return true;
                }
            }
        };

        Session session = getSession();
        return session.doReturningWork(work);
    }

    public Long getActualRole(final Long pUserId) {
        ReturningWork<Long> work = new ReturningWork<Long>() {
            @Override
            public Long execute(Connection connection) throws SQLException {
                //SQLXML xmlData = connection.createSQLXML();
                //xmlData.setString(props);
                Long roleId = null;
                String query = "SELECT U_ROLE_ID FROM CDB_USERS WHERE CDB_USERS.ID = ? ";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    //stmt.setSQLXML(1, xmlData);
                    stmt.setLong(1, pUserId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            roleId = rs.getLong("U_ROLE_ID");
                            break;
                        }
                    }
                    return roleId;
                }
            }
        };

        Session session = getSession();
        return session.doReturningWork(work);
    }

    public UserProperties getUserProperties(final Long pUserId) {
        if (pUserId == null || pUserId.equals(0L)) {
            return null;
        }

        String query;
        if (isOracle()) {
            query = "SELECT ID, U_NAME, NVL2(U_PROPERTIES, (U_PROPERTIES).getClobVal(), NULL) U_PROPERTIES FROM CDB_USERS WHERE CDB_USERS.ID = ? ";
        } else {
            //Microsoft SQL Server
            query = "SELECT ID, U_NAME, CAST(U_PROPERTIES AS VARCHAR(MAX)) U_PROPERTIES FROM CDB_USERS WHERE CDB_USERS.ID = ? ";
        }

        Query qry = getEntityManager().createNativeQuery(query);
        qry.setParameter(1, pUserId);
        final Object o = qry.getSingleResult();
        if (o == null) {
            return null;
        }
        final Object[] obj = (Object[]) o;
//        if (obj.length < 3 || obj[2] == null) {
//            return null;
//        }
        String text = "";
        final Long id = ((Number) obj[0]).longValue();
        final String name = ((String) obj[1]);
        if (obj[2] != null) {
            if (obj[2] instanceof String) {
                text = (String) obj[2];
            } else {
                try {
                    InputStream in = ((java.sql.Clob) obj[2]).getAsciiStream();
                    try (Reader read = new InputStreamReader(in, CpxSystemProperties.DEFAULT_ENCODING)) {
                        StringWriter write = new StringWriter();
                        int c;
                        while ((c = read.read()) != -1) {
                            write.write(c);
                        }
                        write.flush();
                        text = write.toString();
                    }
                } catch (SQLException | IOException ex) {
                    LOG.log(Level.SEVERE, "Cannot read user properties '" + name + "' with id " + id, ex);
                }
            }
        }
        UserProperties props;
        if (text == null || text.trim().isEmpty()) {
            props = new UserProperties();
        } else {
            props = UserProperties.deserialize(text);
        }
        if (props != null) {
            props.setId(id);
            props.setName(name);
        }
        return props;
    }

    public boolean setUserProperties(final Long pUserId, final UserProperties pUserProperties) {
        final String props = (pUserProperties == null) ? "" : pUserProperties.serialize();
        final String connUrl = getConnectionUrl();

        ReturningWork<Boolean> work = new ReturningWork<Boolean>() {
            @Override
            public Boolean execute(Connection connection) throws SQLException {
                //SQLXML xmlData = connection.createSQLXML();
                //xmlData.setString(props);
                String query = "";
                if (isOracle(connUrl)) {
                    query = "UPDATE CDB_USERS SET U_PROPERTIES = XMLTYPE(?) WHERE CDB_USERS.ID = ? ";
                } else {
                    //Microsoft SQL Server
                    query = "UPDATE CDB_USERS SET U_PROPERTIES = ? WHERE CDB_USERS.ID = ? ";
                }
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    //stmt.setSQLXML(1, xmlData);
                    stmt.setClob(1, new StringReader(props));
                    stmt.setLong(2, pUserId);
                    stmt.executeUpdate();
                    return true;
                }
            }
        };

        Session session = getSession();
        return session.doReturningWork(work);
    }

    public CdbUsers getCdbUsers(String pName /*, String pPassword */) {
        EntityGraph<CdbUsers> toFetch = getEntityManager().createEntityGraph(CdbUsers.class);
        toFetch.addSubgraph(CdbUsers_.cdbUser2Roles)
                .addAttributeNodes(CdbUser2Role_.cdbUserRoles.getName());
        List<CdbUsers> lResults = getEntityManager().createNamedQuery("findUserByName", CdbUsers.class)
                .setParameter("name", pName)
                //.setParameter("password", pPassword)
                .setHint(EntityGraphType.getLoadGraphType(), toFetch)
                .getResultList();

        if (lResults.isEmpty()) {
            return null;
        }
        for (CdbUsers cdbUser : lResults) {
            if (cdbUser.isUIsDeleted()) {
                continue;
            }
            return cdbUser;
        }
        return null;
    }

    /**
     * Get list with all users, which are not deleted (U_IS_DELETED = 0)
     *
     * @return a list with user data, which are valid and not valid, but are not
     * deleted
     */
    public List<CdbUsers> getAllUsers() {
        String sql = MessageFormat.format("SELECT DISTINCT ur FROM CdbUsers ur WHERE ur.UIsDeleted = :isdeleted and ur.id < {0}", BATCHJOB_USER_STARTS_WITH_ID + "");
        final TypedQuery<CdbUsers> query = getEntityManager().createQuery(sql, CdbUsers.class);
        query.setParameter("isdeleted", false);

        EntityGraph<CdbUsers> fetchAll = getEntityManager().createEntityGraph(CdbUsers.class);
        fetchAll.addSubgraph(CdbUsers_.cdbUser2Roles).addAttributeNodes(CdbUser2Role_.cdbUserRoles.getName());
//        fetchAll.addSubgraph(CdbUsers_.cdbUser2Roles);
        query.setHint(EntityGraphType.getLoadGraphType(), fetchAll);
        return query.getResultList();
    }

    /**
     * Insert new cpx user in database
     *
     * @param cdbUsers include the properties of new cpx user
     * @return ID of CdbUsers
     */
    public long createNewCpxUser(CdbUsers cdbUsers) {
        persist(cdbUsers);
        flush();
        long id = cdbUsers.getId();
        return id;

    }

    /**
     * find cpx user by user id
     *
     * @param userId is id of user in database
     * @return CdbUsers class with parameter "userId"
     */
    public CdbUsers findUserById(long userId) {
        String queryName = "select u from CdbUsers u where id =:idNbr";
        final TypedQuery<CdbUsers> query = getEntityManager().createQuery(queryName, CdbUsers.class);
        query.setParameter("idNbr", userId);
        List<CdbUsers> lResults = query.getResultList();
        if (lResults.isEmpty()) {
            return null;
        }
        return lResults.get(0);
    }

    /**
     * Set cpx user inactive (IsDeleted = true)
     *
     * @param user cpx user
     * @return state of delete process
     */
    public int setCpxUserInactive(CdbUsers user) {
        int checkDeleteState = 0;
        if (user.getId() <= 0L) {
            checkDeleteState = 1;
        } else {
            user.setUIsDeleted(true);
            merge(user);
            checkDeleteState = 100;
        }
        return checkDeleteState;
    }

    /**
     * Set cpx user inactive by id (IsDeleted = true)
     *
     * @param id id of cpx user
     * @return state of delete process
     */
    public int setCpxUserInactiveById(Long id) {
        CdbUsers user = findById(id);
        int checkDeleteState = setCpxUserInactive(user);
        return checkDeleteState;
    }

    /**
     * Set validation of cpx user (IsValid = true/false)
     *
     * @param user cpx user
     * @param isValid boolean value of validation
     */
    public void setCpxUserValidation(CdbUsers user, boolean isValid) {
        if (user.getId() <= 0L) {
            return;
        }
        user.setUIsValid(isValid);
        merge(user);
    }

    /**
     * Set validation of cpx user by id (IsValid = true/false)
     *
     * @param id id of cpx user
     * @param isValid boolean value of validation
     */
    public void setCpxUserValidationById(Long id, boolean isValid) {
        CdbUsers user = findById(id);
        setCpxUserValidation(user, isValid);
    }

//    /**
//     * update the cpx user data
//     *
//     * @param cdbUsers cpx user
//     * @return state of update process
//     */
//    public boolean updateCpxUser(CdbUsers cdbUsers) {
//        boolean checkChangeState;
//        if (cdbUsers.getId() <= 0L) {
//            checkChangeState = false;
//            return checkChangeState;
//        } else {
//            merge(cdbUsers);
//            checkChangeState = true;
//        }
//        return checkChangeState;
//    }
    /**
     * Get cpx user entity with entity graph for cdbUser2Roles
     *
     * @param userID id of cpx user
     * @return result for cpx user entity with entity graph for cdbUser2Roles
     */
    public CdbUsers getCdbUsersByIdWithGraph(long userID) {

        final TypedQuery<CdbUsers> query = getEntityManager().createQuery("SELECT ur FROM CdbUsers ur WHERE ur.id =:userId", CdbUsers.class);
        query.setParameter("userId", userID);

        EntityGraph<CdbUsers> graph = getEntityManager().createEntityGraph(CdbUsers.class);
        graph.addAttributeNodes(CdbUsers_.cdbUser2Roles);
        query.setHint(EntityGraphType.getLoadGraphType(), graph);
        return query.getSingleResult();
    }

    public List<CdbUsers> findAllActiveUsers() {

//        String sql = "SELECT users.U_IS_VALID,users.U_IS_DELETED,users.U_TITLE,users.U_NAME,users.U_LAST_NAME,users.U_FIRST_NAME"
//                + " FROM CDB_USERS users WHERE users.U_IS_VALID = 1 AND users.U_IS_DELETED = 0";
//        Query query = getEntityManager().createNativeQuery(sql, CdbUsers.class);
//        
//        return query.getResultList();
        final TypedQuery<CdbUsers> query = getEntityManager().createQuery("SELECT DISTINCT ur FROM CdbUsers ur WHERE ur.UIsDeleted =:isdeleted AND ur.UIsValid=:isvalid order by ur.ULastName, ur.UFirstName", CdbUsers.class);
        query.setParameter("isdeleted", false);
        query.setParameter("isvalid", true);

//        EntityGraph<CdbUsers> fetchAll = getEntityManager().createEntityGraph(CdbUsers.class);
//        fetchAll.addSubgraph(CdbUsers_.cdbUser2Roles).addAttributeNodes(CdbUser2Role_.cdbUserRoles.getName());
////        fetchAll.addSubgraph(CdbUsers_.cdbUser2Roles);
//        query.setHint(EntityGraphType.getLoadGraphType(), fetchAll);
        return query.getResultList();
    }

    public boolean setUserImage(long pUserId, Byte[] image) {
        //final Long userId = (pUserId != null && pUserId.equals(0L)) ? null: pUserId;

        ReturningWork<Boolean> work = new ReturningWork<Boolean>() {
            @Override
            public Boolean execute(Connection connection) throws SQLException {
                //SQLXML xmlData = connection.createSQLXML();
                //xmlData.setString(props);
                String query = "UPDATE CDB_USERS SET U_IMAGE = ? WHERE CDB_USERS.ID = ? ";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    //stmt.setSQLXML(1, xmlData);
                    if (image == null) {
                        stmt.setNull(1, Types.BLOB);
                    } else {
                        byte[] image2 = ArrayUtils.toPrimitive(image);
                        stmt.setBytes(1, image2);
                    }
                    stmt.setLong(2, pUserId);
                    stmt.executeUpdate();
                    return true;
                }
            }
        };

        Session session = getSession();
        return session.doReturningWork(work);
    }

    public Byte[] getUserImage(long pUserId) {
        //final Long userId = (pUserId != null && pUserId.equals(0L)) ? null: pUserId;

        ReturningWork<Byte[]> work = new ReturningWork<Byte[]>() {
            @Override
            public Byte[] execute(Connection connection) throws SQLException {
                //SQLXML xmlData = connection.createSQLXML();
                //xmlData.setString(props);
                String query = "SELECT U_IMAGE FROM CDB_USERS WHERE CDB_USERS.ID = ? AND U_IMAGE IS NOT NULL";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    //stmt.setSQLXML(1, xmlData);
                    stmt.setLong(1, pUserId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            byte[] image = rs.getBytes("U_IMAGE");
                            Byte[] image2 = ArrayUtils.toObject(image);
                            return image2;
                        }
                    }
                    return null;
                }
            }
        };

        Session session = getSession();
        return session.doReturningWork(work);
    }

    /**
     * Returns copy of cdbusers object
     *
     * @param pUserId User-ID
     * @return Copy of CdbUsers object
     */
    public CdbUsers getCopy(final Long pUserId) {
        if (pUserId == null) {
            return null;
        }
        CdbUsers cdbUser = findById(pUserId);
        CdbUsers cdbUserTmp = new CdbUsers();
        if (cdbUser != null) {
            cdbUserTmp.setUFirstName(cdbUser.getUFirstName());
            cdbUserTmp.setULastName(cdbUser.getULastName());
            cdbUserTmp.setUName(cdbUser.getUName());
            cdbUserTmp.setUTitle(cdbUser.getUTitle());
            cdbUserTmp.setUValidFrom(cdbUser.getUValidFrom());
            cdbUserTmp.setUValidTo(cdbUser.getUValidTo());
            cdbUserTmp.setUDatabase(cdbUser.getUDatabase());
            cdbUserTmp.setURoleId(cdbUser.getURoleId());
            cdbUserTmp.setUEmailAddresse(cdbUser.getUEmailAddresse());
            cdbUserTmp.setUPhoneNumber(cdbUser.getUPhoneNumber());
            cdbUserTmp.setUFaxNumber(cdbUser.getUFaxNumber());
        }
        return cdbUserTmp;
    }

    /**
     *
     * @param PUName Search teilstring
     * @return FullUserName als String U_NAME(FIRST_NAME,LAST_NAME)
     */
    public Collection<String> findMatchingUsers(String PUName) {

        ReturningWork<List<String>> work = new ReturningWork<List<String>>() {
            @Override
            public List<String> execute(Connection connection) throws SQLException {
                String query = "Select U_NAME,U_FIRST_NAME,U_LAST_NAME from CDB_USERS  where U_NAME LIKE ? and  U_IS_DELETED =0 and  U_IS_VALID =1";
                List<String> results = new ArrayList<>();
                try (PreparedStatement sql = connection.prepareStatement(query)) {
                    sql.setString(1, "%" + PUName + "%");
                    try (ResultSet rs = sql.executeQuery()) {
                        while (rs.next()) {
                            String uname = rs.getString("U_NAME");
                            String firstName = rs.getString("U_FIRST_NAME");
                            String lastName = rs.getString("U_LAST_NAME");
                            String fullname = uname + " ( " + firstName + " , " + lastName + " ) ";
                            if (fullname != null) {
                                results.add(fullname);
                            }
                        }
                    }
                    return results;
                }
            }

        };
        Session session = getSession();
        return session.doReturningWork(work);
    }

    /**
     *
     * @param PUName UName
     * @return list of UName as string
     */
    public List<String> getMatchForUser(String PUName) {

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<String> query = criteriaBuilder.createQuery(String.class);

        Root<CdbUsers> from = query.from(CdbUsers.class);

        query.select(from.get(CdbUsers_.UName));
        Predicate deleteCondition = criteriaBuilder.equal(from.get(CdbUsers_.UIsDeleted), 0);
        Predicate validCondition = criteriaBuilder.equal(from.get(CdbUsers_.UIsValid), 1);
        query.where(deleteCondition, validCondition, criteriaBuilder.like(from.get(CdbUsers_.UName), PUName + "%"));
        query.orderBy(criteriaBuilder.asc(from.get(CdbUsers_.UName)));

        List<String> results = getEntityManager().createQuery(query).getResultList();
        LOG.info("result list size " + results.size() + " for user name '" + PUName + "'");
        return results;
    }

    /**
     *
     * @deprecated try to use client adaption first (look at MenuCacheUsers)!
     * @param PUName UName
     * @param pDate date
     * @return list of valid UName from Date to Date as string
     */
    @Deprecated(since = "1.09.2")
    public List<String> getValidMatchForUser(String PUName, Date pDate) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<String> query = criteriaBuilder.createQuery(String.class);
        Root<CdbUsers> from = query.from(CdbUsers.class);
        query.select(from.get(CdbUsers_.UName));
        Predicate deleteCondition = criteriaBuilder.equal(from.get(CdbUsers_.UIsDeleted), 0);
        Predicate validCondition = criteriaBuilder.equal(from.get(CdbUsers_.UIsValid), 1);
        Predicate jobUserCondition = criteriaBuilder.lessThan(from.get(CdbUsers_.ID), BATCHJOB_USER_STARTS_WITH_ID);
        Predicate startCondition = criteriaBuilder.or(criteriaBuilder.isNull(from.get(CdbUsers_.UValidFrom)), criteriaBuilder.lessThanOrEqualTo(from.get(CdbUsers_.UValidFrom), pDate));
        Predicate endCondition = criteriaBuilder.or(criteriaBuilder.isNull(from.get(CdbUsers_.UValidTo)), criteriaBuilder.greaterThanOrEqualTo(from.get(CdbUsers_.UValidTo), pDate));
        Predicate finalCondition = criteriaBuilder.and(startCondition, endCondition);
//        Predicate finalCondition = criteriaBuilder.between(pDate,from.get(CdbUsers_.UValidFrom), from.get(CdbUsers_.UValidTo));
        query.where(deleteCondition, validCondition, jobUserCondition, finalCondition, criteriaBuilder.like(from.get(CdbUsers_.UName), "%" + PUName + "%"));
        query.orderBy(criteriaBuilder.asc(from.get(CdbUsers_.UName)));

        List<String> results = getEntityManager().createQuery(query).getResultList();
        LOG.log(Level.INFO, "result list size {0} for user name ''{1}''", new Object[]{results.size(), PUName});
        return results;
    }

    /**
     *
     * @deprecated try to use client adaption first (look at MenuCacheUsers)!
     * @param PFullName U_NAME(FIRST_NAME,LAST_NAME)
     * @return Id for U_NAME
     */
    @Deprecated(since = "1.09.2")
    public Long getIDbyUName(String PFullName) {
        ReturningWork<Long> work = new ReturningWork<Long>() {
            @Override
            public Long execute(Connection connection) throws SQLException {
                if (PFullName.isEmpty()) {
                    return 0L;
                } else {
                    String query = "Select ID from CDB_USERS  where U_NAME LIKE ? ";
                    try (PreparedStatement sql = connection.prepareStatement(query)) {
//                        String[] str = PFullName.split(" ");
//                    sql.setString(1, str[0].trim());
                        sql.setString(1, PFullName);
                        Long Id = null;
                        try (ResultSet rs = sql.executeQuery()) {
                            while (rs.next()) {
                                Id = rs.getLong("ID");
                                break;
                            }
                        }

                        return Id;
                    }
                }
            }

        };
        Session session = getSession();
        return session.doReturningWork(work);
    }

    /**
     *
     * @param userId Id der User
     * @deprecated try to use client adaption first (look at MenuCacheUsers)!
     * @return fullname (U_NAME)
     */
    @Deprecated(since = "1.09.2")
    public String getUserbyId(long userId) {
        ReturningWork<String> work = new ReturningWork<String>() {
            @Override
            public String execute(Connection connection) throws SQLException {
                String query = "Select U_NAME from CDB_USERS  where ID = ? and  U_IS_DELETED =0 and  U_IS_VALID =1 ";
                String results = "";
                try (PreparedStatement sql = connection.prepareStatement(query)) {
                    sql.setLong(1, userId);
                    try (ResultSet rs = sql.executeQuery()) {
                        while (rs.next()) {
                            String uname = rs.getString("U_NAME");
//                            String firstName = rs.getString("U_FIRST_NAME");
//                            String lastName = rs.getString("U_LAST_NAME");
//                            String fullname = uname + " ( " + firstName + " , " + lastName + " ) ";
                            if (uname != null) {
                                results = uname;
                            }
                        }
                    }
                    return results;
                }
            }

        };
        Session session = getSession();
        return session.doReturningWork(work);
    }

    public Map<Long, UserDTO> getUsers() {
        return getMenuCacheItems();
    }

    public static UserDTO toUserDto(final CdbUsers user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(user.getId(), user.getUFullName(), user.getUName(), user.getUValidFrom(), user.getUValidTo(), user.isUIsValid(), user.isUIsDeleted());
    }

    public Map<Long, UserDTO> getMenuCacheItems() {
        Map<Long, UserDTO> map = new LinkedHashMap<>();
        List<CdbUsers> users = getAllUsers();

        Collections.sort(users);
        for (CdbUsers user : users) {
            //String name = user.getUFullName();
            //String fullName = user.getUFullName();
            UserDTO dto = toUserDto(user);
            if (dto != null) {
                map.put(user.getId(), dto);//new UserDTO(user.getId(), fullName, user.getUName(), user.isUIsValid(), user.isUIsDeleted()));
            }
        }
        return map;
    }

}
