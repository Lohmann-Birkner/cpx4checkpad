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

import de.lb.cpx.server.commonDB.model.CdbUser2Role;
import de.lb.cpx.server.commonDB.model.CdbUser2Role_;
import de.lb.cpx.server.commons.enums.EntityGraphType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;

/**
 * Data access object for domain model class CdbUser2Role. Initially generated
 * at 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools, sklarow
 */
@Stateless
@SuppressWarnings("unchecked")
public class CdbUser2RoleDao extends AbstractCommonDao<CdbUser2Role> {

    /**
     * Creates a new instance.
     */
    public CdbUser2RoleDao() {
        super(CdbUser2Role.class);
    }

    /**
     * Get an assignment list with all users IDs and his assigned role ID´s
     *
     * @return a assignment list with user ID´s and his role ID´s
     */
    public List<CdbUser2Role> getAllAssignmentsUser2Role() {
        final TypedQuery<CdbUser2Role> query = getEntityManager().createQuery("SELECT ur FROM CdbUser2Role ur WHERE ur.u2rIsActual =:isvalid", CdbUser2Role.class);
        query.setParameter("isvalid", true);

        EntityGraph<CdbUser2Role> graph = getEntityManager().createEntityGraph(CdbUser2Role.class);
        graph.addAttributeNodes(CdbUser2Role_.cdbUserRoles, CdbUser2Role_.cdbUsers);
        query.setHint(EntityGraphType.getLoadGraphType(), graph);
        return query.getResultList();
    }

    /**
     * Get all entries for user with this id
     *
     * @param userId user ID
     * @return list with entries for user with user id
     */
    public List<CdbUser2Role> getUser2RoleByUserId(Long userId) {
        final TypedQuery<CdbUser2Role> query = getEntityManager().createQuery("SELECT ur FROM CdbUser2Role ur WHERE ur.cdbUsers.id =:isId", CdbUser2Role.class);
        query.setParameter("isId", userId);

        EntityGraph<CdbUser2Role> graph = getEntityManager().createEntityGraph(CdbUser2Role.class);
        graph.addAttributeNodes(CdbUser2Role_.cdbUserRoles, CdbUser2Role_.cdbUsers);
        query.setHint(EntityGraphType.getLoadGraphType(), graph);
        return query.getResultList();
    }

//    /**
//     * Add new roles for cpx user
//     *
//     * @param cdbUser2Role include assignment properties
//     * @return id for new assignment between user and role
//     */
//    public long addUser2Role(CdbUser2Role cdbUser2Role) {
//        getEntityManager().persist(cdbUser2Role);
//        getEntityManager().flush();
//        return cdbUser2Role.getId();
//    }
    /**
     * Remove role from user and delete this match from database
     *
     * @param cdbUser2Role match between role and user
     */
    public void deleteUserToRole(CdbUser2Role cdbUser2Role) {
//            CdbUser2Role cdbUser2RoleL = findById(cdbUser2Role.getId());
        //try to detect if entity is not present in the persistance unit
        //if not merge, could result in error if entity was not saved
        if (cdbUser2Role.getId() <= 0) {
            cdbUser2Role = merge(cdbUser2Role);
        }
        deleteById(cdbUser2Role.id);
        flush();
//            getEntityManager().remove(cdbUser2RoleL);
//             getEntityManager().flush();
    }

    /**
     * Get if role id is ised
     *
     * @param pRoleId role ID
     * @return boolean value if role id is ised
     */
    public boolean getUserNameByRoleId(long pRoleId) {

        boolean roleIsUsed = true;
//        try {
        String queryName = "SELECT a FROM CdbUser2Role a WHERE a.cdbUserRoles.id =:roleId";
        final TypedQuery<CdbUser2Role> query = getEntityManager().createQuery(queryName, CdbUser2Role.class);
        query.setParameter("roleId", pRoleId);

        EntityGraph<CdbUser2Role> graph = getEntityManager().createEntityGraph(CdbUser2Role.class);
        graph.addAttributeNodes(CdbUser2Role_.cdbUserRoles, CdbUser2Role_.cdbUsers);
        query.setHint(EntityGraphType.getLoadGraphType(), graph);
        List<CdbUser2Role> lResults = query.getResultList();
        roleIsUsed = !(lResults == null || lResults.isEmpty());

//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can´t check if cpx role is usedrole", ex);
//        }
        return roleIsUsed;
    }

    public List<Long> getUser2RoleIdsByUserId(long pUserId) {
//       try {
        String queryName = "SELECT a.cdbUserRoles.id FROM CdbUser2Role a WHERE a.cdbUsers.id =:userId";
        final TypedQuery<Long> query = getEntityManager().createQuery(queryName, Long.class);
        query.setParameter("userId", pUserId);

        return query.getResultList();

//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can´t check if cpx role is usedrole", ex);
//        }
//        return null;
    }

}
