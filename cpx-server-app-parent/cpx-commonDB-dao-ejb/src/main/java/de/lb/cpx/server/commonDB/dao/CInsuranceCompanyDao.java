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
import de.lb.cpx.server.commonDB.model.CInsuranceCompany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Data access object for domain model class CInsuranceCompany. Initially
 * generated at 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SuppressWarnings("unchecked")
public class CInsuranceCompanyDao extends AbstractCommonDao<CInsuranceCompany> {

    /**
     * Creates a new instance.
     */
    public CInsuranceCompanyDao() {
        super(CInsuranceCompany.class);
    }

    public boolean catalogExists(final String pChecksum) {
        return super.catalogExists("C_INSURANCE_COMPANY", pChecksum);
    }

    public boolean catalogExists(final CountryEn pCountryEn) {
        return getEntryCounter(pCountryEn) > 0;
    }

    public boolean catalogExists(final String pChecksum, final CountryEn pCountryEn) {
        return super.catalogExists("C_INSURANCE_COMPANY", pChecksum, pCountryEn);
    }
    
    public int getEntryCounter(final CountryEn pCountryEn) {
        String query = "SELECT COUNT(*) FROM C_INSURANCE_COMPANY "
                + " WHERE COUNTRY_EN = :country ";
        Query nativeQuery = getEntityManager().createNativeQuery(query);
        //nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        Number count = (Number) nativeQuery.getSingleResult();

        return (count == null) ? 0 : count.intValue();
    }

    public int dropEntries(/* final int pYear, */final CountryEn pCountryEn) {
        int deleteCount = 0;
        String query = "DELETE FROM C_INSURANCE_COMPANY "
                + " WHERE COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        //nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }

    public List<CInsuranceCompany> getEntries(/* final int pYear, */final String pCountryEn) {
        List<CInsuranceCompany> list = null;
        Query query = getEntityManager().createQuery("from " + CInsuranceCompany.class.getSimpleName() + " a where a.countryEn = :country order by a.inscIdent");
        //query.setParameter("year", pYear);
        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        list = query.getResultList();
        return list;
    }

    public String getInsuCompName(String insInsuranceCompany, String countryCode) {
        String insuCompName = null;

        String query = "SELECT INSC_NAME FROM C_INSURANCE_COMPANY"
                + " WHERE INSC_IDENT = :ident "
                + " AND COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("ident", insInsuranceCompany);
        nativeQuery.setParameter("country", countryCode);

        List<String> resultList = nativeQuery.getResultList();
        if (!resultList.isEmpty()) {
            insuCompName = (String) nativeQuery.getSingleResult(); //why not nativeQuery.get(0)?
            return (insuCompName == null) ? "" : insuCompName;
        } else {
            return "";
        }
    }

    public Map<String, String> getInsuCompIdent(Collection<String> pInsShortList, String countryCode) {
        if (pInsShortList == null || pInsShortList.isEmpty()) {
            return new HashMap<>();
        }
        final Set<String> insShortList = new TreeSet<>(pInsShortList);
        Iterator<String> it = insShortList.iterator();
        while (it.hasNext()) {
            String insShort = it.next();
            if (insShort == null || insShort.trim().isEmpty()) {
                it.remove();
            }
        }
        if (insShortList.isEmpty()) {
            return new HashMap<>();
        }

        String query = "SELECT DISTINCT INSC_IDENT, INSC_SHORT FROM C_INSURANCE_COMPANY"
                + " WHERE INSC_SHORT IN (:insShort) "
                + " AND COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("insShort", insShortList);
        nativeQuery.setParameter("country", countryCode);

        List<Object[]> tmp = nativeQuery.getResultList();
        Map<String, String> result = new HashMap<>();
        for (Object[] obj : tmp) {
            String ident = (String) obj[0];
            String shortName = (String) obj[1];
            result.put(ident, shortName);
        }
        return result;
    }

    public Map<String, String> getAllInsuranceCompanies() {
        Map<String, String> map = new LinkedHashMap<>();
//        List<CWmListReminderSubject> reminders = reminderSubjectDao.findAll();
        List<CInsuranceCompany> insuranceComps = findAll();
        //Collections.sort(reminders);
        for (CInsuranceCompany insComp : insuranceComps) {
            map.put(insComp.getInscIdent(), insComp.getInscName());
        }
        return map;
    }

//    public Map<String, InsShortEn> getAllInsuranceCompaniesShort() {
//        Map<String, InsShortEn> map = new LinkedHashMap<>();
////        List<CWmListReminderSubject> reminders = reminderSubjectDao.findAll();
//        List<CInsuranceCompany> insuranceComps = findAll();
//        //Collections.sort(reminders);
//        for (CInsuranceCompany insComp : insuranceComps) {
//            if (insComp.getInscShort() == null) {
//                continue;
//            }
//            map.put(insComp.getInscIdent(), insComp.getInscShort());
//        }
//        return map;
//    }

    public Map<String, String> getAllInsuranceCompaniesShort() {
        Map<String, String> map = new LinkedHashMap<>();
//        List<CWmListReminderSubject> reminders = reminderSubjectDao.findAll();
        List<CInsuranceCompany> insuranceComps = findAll();
        //Collections.sort(reminders);
        for (CInsuranceCompany insComp : insuranceComps) {
            if (insComp.getInscShort() == null) {
                continue;
            }
            map.put(insComp.getInscIdent(), insComp.getInscShort());
        }
        return map;
    }
    
    /**
     * Get all Insurance ident numbers (IK)
     *
     * @param pCountryEn "de"
     * @return map of Insurance ident numbers (IK)
     */
    public List<String> getInscIdentList(CountryEn pCountryEn) {
        List<String> inscIdentList = new ArrayList<>();

        String queryString = "select u.inscIdent from " + CInsuranceCompany.class.getSimpleName() + " u where u.countryEn = :country order by u.inscIdent asc";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("country", pCountryEn);
        inscIdentList = query.getResultList();

        return inscIdentList;
    }
}
