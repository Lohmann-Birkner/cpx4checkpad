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

import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.server.commonDB.model.CIcdCatalog;
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
public class CIcdCatalogDao extends AbstractCommonDao<CIcdCatalog> {

    /**
     * Creates a new instance.
     */
    public CIcdCatalogDao() {
        super(CIcdCatalog.class);
    }

    public boolean catalogExists(final String pChecksum) {
        return super.catalogExists("C_ICD_CATALOG", pChecksum);
    }

    public boolean catalogExists(final int pYear, final CountryEn pCountryEn) {
        return getEntryCounter(pYear, pCountryEn) > 0;
    }

    public int getEntryCounter(final int pYear, final CountryEn pCountryEn) {
        String query = "SELECT COUNT(*) FROM C_ICD_CATALOG "
                + " WHERE ICD_YEAR = :year "
                + "   AND COUNTRY_EN = :country ";
        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        Number count = (Number) nativeQuery.getSingleResult();

        return (count == null) ? 0 : count.intValue();
    }

    public int dropEntries(final int pYear, final CountryEn pCountryEn) {
        int deleteCount = 0;
        String query = "DELETE FROM C_ICD_CATALOG "
                + " WHERE ICD_YEAR = :year "
                + "   AND COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }

    public List<CIcdCatalog> getEntries(final int pYear, final String pCountryEn) {
        List<CIcdCatalog> list = null;
        Query query = getEntityManager().createQuery("from " + CIcdCatalog.class.getSimpleName() + " a where a.icdYear = :year and a.countryEn = :country order by a.icdDepth, a.icdCode");
        query.setParameter("year", pYear);
        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        list = query.getResultList();
        return list;
    }

    public String getIcdText(String icdcCode, String countryCode, int year) {
        String icdText = null;
        String query = "SELECT ICD_DESCRIPTION FROM C_ICD_CATALOG "
                + " WHERE ICD_CODE = :code "
                + " AND ICD_YEAR = :year "
                + " AND COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("code", icdcCode);
        nativeQuery.setParameter("year", year);
        nativeQuery.setParameter("country", countryCode);

//        icdText = (String) nativeQuery.getSingleResult();
        List<?> resultList = nativeQuery.getResultList();
        if (!resultList.isEmpty()) {
            if (isOracle()) {
                Clob clob = (Clob) nativeQuery.getSingleResult();
                try {
                    int length = (int) clob.length();
                    icdText = clob.getSubString(1, length);
                } catch (SQLException ex) {
                    Logger.getLogger(CIcdCatalogDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                icdText = nativeQuery.getSingleResult().toString();
            }
            return (icdText == null) ? "" : icdText;
        } //        icdText = nativeQuery.getSingleResult().toString();
        else {
            return "";
        }
    }

    public List<CIcdCatalog> getIcdDetails(List<TCaseIcd> icd_list, int year, String countryCode) {
        final List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder(10 * icd_list.size());
//        StringBuffer sb = new StringBuffer();
//        DECLARE @MyList ICD_DESCRIPTION (Value INT);
//        List<String> resultList = null;
        List<CIcdCatalog> resultList1 = null;
        if (!icd_list.isEmpty()) {
            icd_list.forEach(new Consumer<TCaseIcd>() {
                @Override
                public void accept(TCaseIcd t) {
                    list.add(t.getIcdcCode());
                    sb.append("'" + t.getIcdcCode() + "'" + ", ");
                }
            });

//            String query = "SELECT ICD_CODE, ICD_DESCRIPTION FROM C_ICD_CATALOG "
//                    + "WHERE ICD_YEAR = :year "
//                    + "AND COUNTRY_EN = :country "
//                    + "AND ICD_CODE " + "IN (" + sb.substring(0, sb.length() - 2) + ")";
//            Query nativeQuery = getEntityManager().createNativeQuery(query);
//            nativeQuery.setParameter("year", year);
//            nativeQuery.setParameter("country", countryCode);
//            String queryName = "select u from " + CDrafts.class.getSimpleName() + " u where id =:idNbr";
//            Query query = getEntityManager().createQuery(queryName);
//            query.setParameter("idNbr", pCDraftsId);
            final TypedQuery<CIcdCatalog> query = getEntityManager().createQuery("from CIcdCatalog WHERE ICD_YEAR = :year AND COUNTRY_EN = :country AND ICD_CODE IN (" + sb.substring(0, sb.length() - 2) + ")", CIcdCatalog.class);

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
