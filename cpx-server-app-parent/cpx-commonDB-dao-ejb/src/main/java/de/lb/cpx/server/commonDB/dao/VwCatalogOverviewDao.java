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

import de.lb.cpx.server.commonDB.model.VwCatalogOverview;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Data access object for domain model class CrgRules. Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SuppressWarnings("unchecked")
public class VwCatalogOverviewDao extends AbstractCommonDao<VwCatalogOverview> {

    /**
     * Creates a new instance.
     */
    public VwCatalogOverviewDao() {
        super(VwCatalogOverview.class);
    }

    public List<VwCatalogOverview> find(final String pCountryEn, final String pCatalog, final Integer pYear) {
        String sql = "from " + VwCatalogOverview.class.getSimpleName() + " p";
        final String country = pCountryEn == null ? "" : pCountryEn.trim();
        final String catalog = pCatalog == null ? "" : pCatalog.trim();
        if (!catalog.isEmpty() || !country.isEmpty() || pYear != null) {
            StringBuilder whereSql = new StringBuilder();
            if (!country.isEmpty()) {
                whereSql.append(String.format("countryEn = '%s' ", country));
            }
            if (!catalog.isEmpty()) {
                if (whereSql.length() > 0) {
                    whereSql.append(" AND ");
                }
                whereSql.append(String.format("catalog = '%s' ", catalog));
            }
            if (pYear != 0) {
                if (whereSql.length() > 0) {
                    whereSql.append(" AND ");
                }
                whereSql.append(String.format("year = %s ", String.valueOf(pYear)));
            }
            sql += " WHERE " + whereSql.toString();
        }
        Query query = getEntityManager().createQuery(sql);
        List<VwCatalogOverview> lResults = query.getResultList();
        if (lResults == null || lResults.isEmpty()) {
            return null;
        }
        return lResults;
    }

}
