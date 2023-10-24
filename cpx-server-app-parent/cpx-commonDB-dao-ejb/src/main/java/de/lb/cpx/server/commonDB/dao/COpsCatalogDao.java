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

import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.server.commonDB.model.COpsCatalog;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * Data access object for domain model class CHospital. Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SuppressWarnings("unchecked")
public class COpsCatalogDao extends AbstractCommonDao<COpsCatalog> {

    /**
     * Creates a new instance.
     */
    public COpsCatalogDao() {
        super(COpsCatalog.class);
    }

    public boolean catalogExists(final String pChecksum) {
        return super.catalogExists("C_OPS_CATALOG", pChecksum);
    }

    public boolean catalogExists(final int pYear, final CountryEn pCountryEn) {
        return getEntryCounter(pYear, pCountryEn) > 0;
    }

    public int getEntryCounter(final int pYear, final CountryEn pCountryEn) {
        String query = "SELECT COUNT(*) FROM C_OPS_CATALOG "
                + " WHERE OPS_YEAR = :year "
                + "   AND COUNTRY_EN = :country ";
        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        Number count = (Number) nativeQuery.getSingleResult();

        return (count == null) ? 0 : count.intValue();
    }

    public int dropEntries(final int pYear, final CountryEn pCountryEn) {
        int deleteCount = 0;
        String query = "DELETE FROM C_OPS_CATALOG "
                + " WHERE OPS_YEAR = :year "
                + "   AND COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }

    public List<COpsCatalog> getEntries(final int pYear, final String pCountryEn) {
        List<COpsCatalog> list = null;
        Query query = getEntityManager().createQuery("from " + COpsCatalog.class.getSimpleName() + " a where a.opsYear = :year and a.countryEn = :country order by a.opsDepth, a.opsCode");
        query.setParameter("year", pYear);
        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        list = query.getResultList();
        return list;
    }

    public String getOpsText(String opscCode, String countryCode, int year) {
        String opsText = null;
        String query = "SELECT OPS_DESCRIPTION FROM C_OPS_CATALOG "
                + " WHERE OPS_CODE = :code "
                + " AND OPS_YEAR = :year "
                + " AND COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("code", opscCode);
        nativeQuery.setParameter("year", year);
        nativeQuery.setParameter("country", countryCode);

        List<Clob> resultList = nativeQuery.getResultList();
        if (!resultList.isEmpty()) {
            if (isOracle()) {
                Clob clob = (Clob) nativeQuery.getSingleResult();
                try {
                    int length = (int) clob.length();
                    opsText = clob.getSubString(1, length);
                } catch (SQLException ex) {
                    Logger.getLogger(CIcdCatalogDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                opsText = nativeQuery.getSingleResult().toString();
            }
            return (opsText == null) ? "" : opsText;
        } else {
            return "";
        }
    }

    public List<COpsCatalog> getOpsDetails(List<TCaseOps> ops_list, int year, String countryCode) {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder(10 * ops_list.size());
        List<COpsCatalog> resultList1 = null;
        if (!ops_list.isEmpty()) {
            ops_list.forEach(new Consumer<TCaseOps>() {
                @Override
                public void accept(TCaseOps t) {
                    list.add(t.getOpscCode());
                    sb.append("'" + t.getOpscCode() + "'" + ", ");
                }
            });
            final TypedQuery<COpsCatalog> query = getEntityManager().createQuery("from COpsCatalog WHERE OPS_YEAR = :year AND COUNTRY_EN = :country AND OPS_CODE IN (" + sb.substring(0, sb.length() - 2) + ")", COpsCatalog.class);

            query.setParameter("year", year);
            query.setParameter("country", countryCode);

            resultList1 = query.getResultList();
        }
        return resultList1;
//        if (resultList1 != null && !resultList1.isEmpty()) {
//            return resultList1;
//        } else {
//            return resultList1;
//        }
    }
}
