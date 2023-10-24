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
import de.lb.cpx.server.commons.dao.MenuCacheEntity;
import de.lb.cpx.service.properties.RoleProperties;
import de.lb.cpx.system.properties.CpxSystemProperties;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;

/**
 * Data access object for domain model class CdbUserRoles. Initially generated
 * at 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools, sklarow
 */
@Stateless
public class CdbUserRolesDao extends AbstractCommonDao<CdbUserRoles> {

    private static final Logger LOG = Logger.getLogger(CdbUserRolesDao.class.getName());

    /**
     * Creates a new instance.
     */
    public CdbUserRolesDao() {
        super(CdbUserRoles.class);
    }

    /*
    public List<CdbUserRoles> getCdbUserRolesList(Long pCdbUserId) {
      String lQuery = "SELECT ur FROM " + CdbUserRoles.class.getSimpleName() + " ur, " + CdbUser2Role.class.getSimpleName() + " u2ur WHERE ur.id = u2ur.cdburId AND u2ur.cdbuId = :user_id ";

      return getEntityManager().createQuery(lQuery)
          .setParameter("user_id", pCdbUserId)
          .getResultList();
    }
     */
    public RoleProperties getUserRoleProperties(final Long pUserRolesId) {
        if (pUserRolesId == null || pUserRolesId.equals(0L)) {
            return null;
        }

        String query;
        if (isOracle()) {
            query = "SELECT ID, CDBUR_NAME, NVL2(CDBUR_PROPERTIES, (CDBUR_PROPERTIES).getClobVal(), NULL) CDBUR_PROPERTIES FROM CDB_USER_ROLES WHERE CDB_USER_ROLES.ID = ? ";
        } else {
            //Microsoft SQL Server
            query = "SELECT ID, CDBUR_NAME, CAST(CDBUR_PROPERTIES AS VARCHAR(MAX)) CDBUR_PROPERTIES FROM CDB_USER_ROLES WHERE CDB_USER_ROLES.ID = ? ";
        }

        Query qry = getEntityManager().createNativeQuery(query);
        qry.setParameter(1, pUserRolesId);
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
                    LOG.log(Level.SEVERE, "Cannot read role properties '" + name + "' with id " + id, ex);
                }
            }
        }
        RoleProperties props;
        if (text == null || text.trim().isEmpty()) {
            props = new RoleProperties();
        } else {
            props = RoleProperties.deserialize(text);
        }
        if (props != null) {
            props.setId(id);
            props.setName(name);
        }
        return props;
    }

    public boolean setUserRoleProperties(final Long pUserRolesId, final RoleProperties pRoleProperties) {
        final String props = (pRoleProperties == null) ? "" : pRoleProperties.serialize();
        final String connUrl = getConnectionUrl();

        ReturningWork<Boolean> work = new ReturningWork<Boolean>() {
            @Override
            public Boolean execute(Connection connection) throws SQLException {
                //SQLXML xmlData = connection.createSQLXML();
                //xmlData.setString(props);
                String query;
                if (isOracle(connUrl)) {
                    query = "UPDATE CDB_USER_ROLES SET CDBUR_PROPERTIES = XMLTYPE(?) WHERE CDB_USER_ROLES.ID = ? ";
                } else {
                    //Microsoft SQL Server
                    query = "UPDATE CDB_USER_ROLES SET CDBUR_PROPERTIES = ? WHERE CDB_USER_ROLES.ID = ? ";
                }
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    //stmt.setSQLXML(1, xmlData);
                    stmt.setClob(1, new StringReader(props));
                    stmt.setLong(2, pUserRolesId);
                    stmt.executeUpdate();
                    return true;
                }
            }
        };

        Session session = getSession();
        return session.doReturningWork(work);
    }

    /**
     * returns a list of roleids of the valid roles, which will be used by rules
     * check
     *
     * @return list
     */
    public List<Long> getAllValidRoleIds() {
        final TypedQuery<Long> query = getEntityManager().createQuery("select id from CdbUserRoles ur where cdburIsvalid =:isvalid", Long.class);
        query.setParameter("isvalid", true);
        return query.getResultList();
    }

    /**
     * SSK returns a list of roleids of the all roles
     *
     * @return list of role IDs
     *
     */
    public List<Long> getAllRoleIds() {
        final TypedQuery<Long> query = getEntityManager().createQuery("select id from CdbUserRoles ur", Long.class);
        query.setParameter("isvalid", true);
        return query.getResultList();
    }

    /**
     * find cpx role by role id
     *
     * @param roleId is id of role in database
     * @return CdbUserRoles class with parameter "roleId"
     */
    public CdbUserRoles findRoleById(long roleId) {
        String queryName = "select r from CdbUserRoles r where id =:idNbr";
        final TypedQuery<CdbUserRoles> query = getEntityManager().createQuery(queryName, CdbUserRoles.class);
        query.setParameter("idNbr", roleId);
        List<CdbUserRoles> lResults = query.getResultList();
        if (lResults.isEmpty()) {
            return null;
        }
        return lResults.get(0);
    }

    /**
     * Create new cpx user role
     *
     * @param pCdbUserRoles role data
     * @param pRoleProperties role properties
     * @return id for new role
     */
    public long createNewCpxUserRole(CdbUserRoles pCdbUserRoles, RoleProperties pRoleProperties) {

        persist(pCdbUserRoles);
        flush();
        long roleId = pCdbUserRoles.getId();
        setUserRoleProperties(roleId, pRoleProperties);
        return roleId;
    }

    /**
     * Set validation of cpx user role by id (IsValid = true/false)
     *
     * @param id id of cpx user role
     * @param isValid boolean value of validation
     */
    public void setCpxUserRoleValidationById(Long id, boolean isValid) {
        CdbUserRoles role = findById(id);
        if (role.getId() <= 0L) {
            return;
        }
        role.setCdburIsvalid(isValid);
        merge(role);
    }

//    /**
//     * Delete cpx user role from database
//     *
//     * @param id role id
//     * @return boolean value for delete state
//     */
//    public boolean removeCpxUserRoleById(Long id) {
//        deleteById(id);
//        boolean isDeleted = true;
//        return isDeleted;
//    }
//    /**
//     * update the cpx user role data
//     *
//     * @param cdbUserRoles cpx user role
//     * @return state of update process
//     */
//    public boolean updateCpxUserRole(CdbUserRoles cdbUserRoles) {
//        boolean checkChangeState = false;
//        if (cdbUserRoles.getId() <= 0L) {
//            checkChangeState = false;
//            return checkChangeState;
//        } else {
//            merge(cdbUserRoles);
//            checkChangeState = true;
//        }
//        return checkChangeState;
//    }
    /**
     * Create map of role id and role name
     *
     * @return map of roles
     */
    public Map<Long, String> createRoleMap() {
        Map<Long, String> roleMap = new HashMap<>();
//        try {

        String queryString = "SELECT a FROM " + CdbUserRoles.class.getSimpleName() + " a "
                + "WHERE a.cdburIsvalid = 1 "
                + "ORDER BY a.cdburName";

        Query query = getEntityManager().createQuery(queryString);
        @SuppressWarnings("unchecked")
        List<CdbUserRoles> roleList = query.getResultList();
        for (CdbUserRoles role : roleList) {
            roleMap.put(role.getId(), role.getCdburName());
        }
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create role list", ex);
//        }
        return roleMap;
    }

    public Map<Long, CdbUserRoles> getMenuCacheItems() {
        return MenuCacheEntity.toMap(findAll());
    }

}
