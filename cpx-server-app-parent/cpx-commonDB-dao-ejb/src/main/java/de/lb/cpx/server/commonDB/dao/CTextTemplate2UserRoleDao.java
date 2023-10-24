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
 *    2019  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.server.commonDB.model.CTextTemplate2UserRole;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author nandola
 */
@Stateless
@SuppressWarnings("unchecked")
public class CTextTemplate2UserRoleDao extends AbstractCommonDao<CTextTemplate2UserRole> {

    public CTextTemplate2UserRoleDao() {
        super(CTextTemplate2UserRole.class);
    }

    /**
     * Get all entries from C_TEXT_TEMPLATE_2_USER_ROLE table (sorted based on
     * text template)
     *
     * @return list
     */
    public List<CTextTemplate2UserRole> getAllTextTemplate2UserRoleEntries() {
        List<CTextTemplate2UserRole> list = null;
        Query query = getEntityManager().createQuery("from " + CTextTemplate2UserRole.class.getSimpleName() + " a order by a.textTemplate");
        list = query.getResultList();
        return list;
    }

    /**
     * @param cdbUserRole user role
     * @return sorted list of matched entries.
     */
    public List<CTextTemplate2UserRole> getAllTextTemplate2UserRoleBasedOnRole(CdbUserRoles cdbUserRole) {
        List<CTextTemplate2UserRole> lResults = null;
        String queryName = "select u from " + CTextTemplate2UserRole.class.getSimpleName() + " u where cdbUserRole =:idNbr order by u.textTemplate";
        Query query = getEntityManager().createQuery(queryName);
        query.setParameter("idNbr", cdbUserRole);
        lResults = query.getResultList();
        if (lResults == null || lResults.isEmpty()) {
            return null;
        }
        return lResults;
    }

}
