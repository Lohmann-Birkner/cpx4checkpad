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

import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.server.commonDB.model.CHospital;
import java.util.ArrayList;
import java.util.List;
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
public class CHospitalDao extends AbstractCommonDao<CHospital> {

    private static final Logger LOG = Logger.getLogger(CHospitalDao.class.getName());

    /**
     * Creates a new instance.
     */
    public CHospitalDao() {
        super(CHospital.class);
    }

    public boolean catalogExists(final String pChecksum) {
        return super.catalogExists("C_HOSPITAL", pChecksum);
    }

    public boolean catalogExists(final CountryEn pCountryEn) {
        return getEntryCounter(pCountryEn) > 0;
    }

    public boolean catalogExists(final String pChecksum, final CountryEn pCountryEn) {
        return super.catalogExists("C_HOSPITAL", pChecksum, pCountryEn);
    }

    public int getEntryCounter(final CountryEn pCountryEn) {
        String query = "SELECT COUNT(*) FROM C_HOSPITAL "
                + " WHERE COUNTRY_EN = :country ";
        Query nativeQuery = getEntityManager().createNativeQuery(query);
        //nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        Number count = (Number) nativeQuery.getSingleResult();

        return (count == null) ? 0 : count.intValue();
    }

    public int dropEntries(/* final int pYear, */final CountryEn pCountryEn) {
        int deleteCount = 0;
        String query = "DELETE FROM C_HOSPITAL "
                + " WHERE COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        //nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }

    public List<CHospital> getEntries(/* final int pYear, */final String pCountryEn) {
        List<CHospital> list = null;
        Query query = getEntityManager().createQuery("from " + CHospital.class.getSimpleName() + " a where a.countryEn = :country order by a.hosIdent");
        //query.setParameter("year", pYear);
        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        list = query.getResultList();
        return list;
    }

    public String getHospitalName(String csHospitalIdent, String countryCode) {
        String query = "SELECT HOS_NAME FROM C_HOSPITAL"
                + " WHERE HOS_IDENT = :ident "
                + " AND COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("ident", csHospitalIdent);
        nativeQuery.setParameter("country", countryCode);

        List<String> resultList = nativeQuery.getResultList();
        String hospitalName = null;
        if (!resultList.isEmpty()) {
            if (resultList.size() > 1) {
                LOG.log(Level.SEVERE, "found " + resultList.size() + " results, but expected zero or one result. This is ambiguous and can cause several side effects like constraint exceptions!");
            }
            hospitalName = resultList.get(0);
        }

        return (hospitalName == null) ? "" : hospitalName;
    }

    /**
     * Get all hospital ident numbers (IKZ)
     *
     * @param pCountryEn "de"
     * @return map of hospital ident numbers (IKZ)
     */
    public List<String> getHosIdentList(CountryEn pCountryEn) {
        List<String> hosIdentList = new ArrayList<>();
//        try {
        String queryString = "select u.hosIdent from " + CHospital.class.getSimpleName() + " u where u.countryEn = :country order by u.hosIdent asc";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("country", pCountryEn);
        hosIdentList = query.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create list with IKZs", ex);
//        }
        return hosIdentList;
    }

    public CHospital getHospital2Ident(String pHosIdent, String pCountryEn){
        List<CHospital> list = null;
        Query query = getEntityManager().createQuery("from " + CHospital.class.getSimpleName() + " a where a.countryEn = :country and a.hosIdent = :hosIdent");
        //query.setParameter("year", pYear);
        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        query.setParameter("hosIdent", pHosIdent);
        list = query.getResultList();
        if(list == null || list.isEmpty()){
            return null;
        }
        return list.get(0);

    }
}
