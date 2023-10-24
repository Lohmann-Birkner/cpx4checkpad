/*
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
 *
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.server.commonDB.model.CPeppCatalog;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.persistence.Query;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * Data access object for domain model class CPeppCatalog. Initially generated
 * at 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SecurityDomain("cpx")
@SuppressWarnings("unchecked")
public class CPeppCatalogDao extends AbstractCommonDao<CPeppCatalog> {

    /**
     * Creates a new instance.
     */
    public CPeppCatalogDao() {
        super(CPeppCatalog.class);
    }

    public boolean catalogExists(final String pChecksum) {
        return super.catalogExists("C_PEPP_CATALOG", pChecksum);
    }

    public boolean catalogExists(final int pYear, final CountryEn pCountryEn) {
        return getEntryCounter(pYear, pCountryEn) > 0;
    }

    public int getEntryCounter(final int pYear, final CountryEn pCountryEn) {
        String query = "SELECT COUNT(*) FROM C_PEPP_CATALOG "
                + " WHERE PEPP_YEAR = :year "
                + "   AND COUNTRY_EN = :country ";
        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        Number count = (Number) nativeQuery.getSingleResult();

        return (count == null) ? 0 : count.intValue();
    }

    @TransactionAttribute(REQUIRES_NEW)
    public int dropEntries(final int pYear, final CountryEn pCountryEn) {

        String query = "DELETE FROM C_PEPP_CATALOG "
                + " WHERE PEPP_YEAR = :year "
                + "   AND COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());

        int deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }

    public List<CPeppCatalog> getEntries(final int pYear, final String pCountryEn) {
        List<CPeppCatalog> list = null;
        Query query = getEntityManager().createQuery("from " + CPeppCatalog.class.getSimpleName()
                + " a where a.peppYear = :year and a.countryEn = :country order by a.peppPepp, a.peppHosIdent, a.peppValidFrom , a.peppRelationNumber");
        query.setParameter("year", pYear);
        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        list = query.getResultList();
        return list;
    }

    /**
     * Get all distinct years of PEPP catalog
     *
     * @param pCountryEn "de"
     * @return list of years of PEPP catalog
     */
    public List<Integer> getPeppYearList(CountryEn pCountryEn) {
        List<Integer> peppYearsList = new ArrayList<>();
//        try {
        String queryString = "select distinct(peppYear) from " + CPeppCatalog.class.getSimpleName() + " a where a.countryEn = :country order by a.peppYear desc";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("country", pCountryEn);
        peppYearsList = query.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create list with years of PEPP", ex);
//        }
        return peppYearsList;
    }

    /**
     * Get negotiated PEPP catalog
     *
     * @param pPeppYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @return PEPP catalog for selected year and IKZ
     */
    public List<CPeppCatalog> getPeppCatalogByYearAndIkz(int pPeppYear, String pHosIdent, CountryEn pCountryEn) {
        List<CPeppCatalog> peppCatalog = new ArrayList<>();
//        try {

        String queryStringIkz = "SELECT a FROM " + CPeppCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.peppYear = :year "
                + "AND a.peppHosIdent = :hosIdent "
                + "AND a.peppIsNegotiatedFl = 1 "
                + "ORDER BY a.peppPepp, a.peppRelationNumber, a.peppValidFrom ASC";

        Query queryIkz = getEntityManager().createQuery(queryStringIkz);
        queryIkz.setParameter("year", pPeppYear);
        queryIkz.setParameter("country", pCountryEn);
        queryIkz.setParameter("hosIdent", pHosIdent);

        peppCatalog = queryIkz.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create PEPP list for selected year", ex);
//
//        }
        return peppCatalog;
    }

    /**
     * Get all PEPP catalog
     *
     * @param pPeppYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @return PEPP catalog for selected year and IKZ
     */
    public List<CPeppCatalog> getAllPeppCatalogByYearAndIkz(int pPeppYear, String pHosIdent, CountryEn pCountryEn) {
        List<CPeppCatalog> peppCatalog = new ArrayList<>();
//        try {

        String queryStringIkz = "SELECT a FROM " + CPeppCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.peppYear = :year "
                + "AND a.peppHosIdent = :hosIdent "
                + "ORDER BY a.peppPepp, a.peppRelationNumber, a.peppValidFrom ASC";

        Query queryIkz = getEntityManager().createQuery(queryStringIkz);
        queryIkz.setParameter("year", pPeppYear);
        queryIkz.setParameter("country", pCountryEn);
        queryIkz.setParameter("hosIdent", pHosIdent);

        peppCatalog = queryIkz.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create PEPP list for selected year", ex);
//
//        }
        return peppCatalog;
    }

    /**
     * Add new negotiated PEPP item
     *
     * @param pCPeppCatalog negotiated PEPP item
     * @return new negotiated PEPP item
     */
    public CPeppCatalog addNegotiatedPepp(CPeppCatalog pCPeppCatalog) {
//        try {
        persist(pCPeppCatalog);
        flush();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't add new negotiated PEPP item", ex);
//        }
        return pCPeppCatalog;
    }

    /**
     * update negotiated PEPP item
     *
     * @param pCPeppCatalog negotiated PEPP item
     * @return id for updated negotiated PEPP item
     */
    public boolean updateNegotiatedPepp(CPeppCatalog pCPeppCatalog) {
        boolean checkUpdateState = false;
//        try {
        if (pCPeppCatalog.getId() <= 0L) {
            checkUpdateState = false;
        } else {
            merge(pCPeppCatalog);
            checkUpdateState = true;
        }
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't update new negotiated PEPP item", ex);
//        }
        return checkUpdateState;
    }

    /**
     * Get negotiated default PEPP list
     *
     * @param pCountryEn catalog language
     * @param pPeppYear catalog year
     * @return list of items
     */
    public List<CPeppCatalog> getDefaultNegotiatedPeppList(CountryEn pCountryEn, int pPeppYear) {
        List<CPeppCatalog> defaultPeppCatalog = new ArrayList<>();
//        try {
        String queryStringDefault = "SELECT a FROM " + CPeppCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.peppYear = :year "
                + "AND a.peppHosIdent IS NULL "
                + "AND a.peppIsNegotiatedFl = 1 "
                + "ORDER BY a.peppPepp, a.peppRelationNumber, a.peppValidFrom ASC";

        Query queryDefault = getEntityManager().createQuery(queryStringDefault);
        queryDefault.setParameter("year", pPeppYear);
        queryDefault.setParameter("country", pCountryEn);

        defaultPeppCatalog = queryDefault.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't create deafault PEPP item", ex);
//        }
        return defaultPeppCatalog;
    }

    /**
     * Get default PEPP list
     *
     * @param pCountryEn catalog language
     * @param pPeppYear catalog year
     * @return list of default items
     */
    public List<CPeppCatalog> getDefaultAllPeppList(CountryEn pCountryEn, int pPeppYear) {
        List<CPeppCatalog> defaultPeppCatalog = new ArrayList<>();
//        try {
        String queryStringDefault = "SELECT a FROM " + CPeppCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.peppYear = :year "
                + "AND a.peppHosIdent IS NULL "
                + "ORDER BY a.peppPepp, a.peppRelationNumber, a.peppValidFrom ASC";

        Query queryDefault = getEntityManager().createQuery(queryStringDefault);
        queryDefault.setParameter("year", pPeppYear);
        queryDefault.setParameter("country", pCountryEn);

        defaultPeppCatalog = queryDefault.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't create deafault PEPP item", ex);
//        }
        return defaultPeppCatalog;
    }

    /**
     * Get already entered hospital ident numbers (IKZ) in PEPP catalog
     *
     * @param pCountryEn "de"
     * @param pPeppYear selected year
     * @return map of hospital ident numbers (IKZ)
     */
    public List<String> getExistingHosIdentPeppList(CountryEn pCountryEn, int pPeppYear) {
        List<String> existingHosIdentList = new ArrayList<>();
//        try {
        String queryString = "SELECT DISTINCT(a.peppHosIdent) FROM " + CPeppCatalog.class.getSimpleName()
                + " a WHERE a.countryEn = :country "
                + "AND a.peppIsNegotiatedFl = 1 "
                + "AND a.peppYear = :year "
                + "AND a.peppHosIdent IS NOT NULL "
                + "AND (a.peppRelationCostWeight IS NOT NULL "
                + "OR a.peppRelationFrom IS NOT -1 "
                + "OR a.peppRelationTo IS NOT -1 "
                + "OR a.peppValidFrom IS NOT NULL "
                + "OR a.peppValidTo IS NOT NULL) "
                + "order by a.peppHosIdent asc";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("country", pCountryEn);
        query.setParameter("year", pPeppYear);
        existingHosIdentList = query.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create list with IKZs", ex);
//        }
        return existingHosIdentList;
    }

//    /**
//     * Delete PEPP item from database by id
//     *
//     * @param id PEPP item id
//     * @return boolean value for delete state
//     */
//    public boolean removePeppById(Long id) {
//        boolean isDeleted;
//        try {
//            deleteById(id);
//            isDeleted = true;
//        } catch (Exception ex) {
//            isDeleted = false;
//            LOG.log(Level.SEVERE, "Can't delete PEPP item", ex);
//        }
//        return isDeleted;
//    }
    /**
     * Get negotiated PEPPs by key, year and IKZ
     *
     * @param peppPepp selected PEPP
     * @param pValidFrom PEPP valid from
     * @param pValidTo PEPP valid to
     * @param pPeppYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @return PEPP catalog for selected year and IKZ
     */
    public List<CPeppCatalog> getPeppByKeyAndIkz(String peppPepp, Date pValidFrom, Date pValidTo, int pPeppYear, String pHosIdent, CountryEn pCountryEn) {
        List<CPeppCatalog> peppCatalog = new ArrayList<>();
//        try {

        String queryStringIkz = "SELECT a FROM " + CPeppCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.peppYear = :year "
                + "AND a.peppPepp = :peppKey "
                + "AND a.peppHosIdent = :hosIdent "
                + "AND a.peppValidFrom = :validFrom "
                + "AND a.peppValidTo = :validTo "
                + "AND a.peppIsNegotiatedFl = 1 "
                + "ORDER BY a.peppRelationNumber, a.peppValidFrom ASC";

        Query queryIkz = getEntityManager().createQuery(queryStringIkz);
        queryIkz.setParameter("year", pPeppYear);
        queryIkz.setParameter("peppKey", peppPepp);
        queryIkz.setParameter("country", pCountryEn);
        queryIkz.setParameter("hosIdent", pHosIdent);
        queryIkz.setParameter("validFrom", pValidFrom);
        queryIkz.setParameter("validTo", pValidTo);

        peppCatalog = queryIkz.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create PEPP list for selected year, ikz and key", ex);
//
//        }
        return peppCatalog;
    }

    /**
     * Get negotiated PEPPs by key and year
     *
     * @param peppPepp selected PEPP
     * @param pPeppYear selected yaer
     * @param pCountryEn "de"
     * @return PEPP catalog for selected year and IKZ
     */
    public List<CPeppCatalog> getPeppByKeyAndYearDef(String peppPepp, int pPeppYear, CountryEn pCountryEn) {
        List<CPeppCatalog> peppCatalog = new ArrayList<>();
//        try {

        String queryStringDef = "SELECT a FROM " + CPeppCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.peppYear = :year "
                + "AND a.peppPepp = :peppKey "
                + "AND a.peppHosIdent IS NULL "
                + "AND a.peppIsNegotiatedFl = 1 "
                + "ORDER BY a.peppRelationNumber, a.peppValidFrom ASC";

        Query queryDef = getEntityManager().createQuery(queryStringDef);
        queryDef.setParameter("year", pPeppYear);
        queryDef.setParameter("peppKey", peppPepp);
        queryDef.setParameter("country", pCountryEn);

        peppCatalog = queryDef.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create PEPP list for selected year and key", ex);
//
//        }
        return peppCatalog;
    }

    /**
     * Get PEPP keys and description by year
     *
     * @param pCountryEn language
     * @param pYear year
     * @return map of PEPP keys and description
     */
    public Map<String, String> getPeppKeyByYear(CountryEn pCountryEn, int pYear) {
        Map<String, String> tmpCatalogMap = new HashMap<>();
//        try {

        String queryStringDef = "SELECT DISTINCT (a) FROM " + CPeppCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.peppYear = :year "
                + "AND a.peppIsNegotiatedFl = 1 "
                + "ORDER BY a.peppPepp ASC";

        Query queryDef = getEntityManager().createQuery(queryStringDef);
        queryDef.setParameter("year", pYear);
        queryDef.setParameter("country", pCountryEn);
        List<CPeppCatalog> tmpCatalogListTest = queryDef.getResultList();
        if (!tmpCatalogListTest.isEmpty()) {
            for (CPeppCatalog drg : tmpCatalogListTest) {
                tmpCatalogMap.put(drg.getPeppPepp(), drg.getPeppDescription());
            }
        }
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create list of PEPP keys for selected year", ex);
//
//        }
        return tmpCatalogMap;
    }

//    /**
//     * Import new PEPP catalog items
//     *
//     * @param pCPeppCatalog list of items
//     * @return number of added PEPP catalog items
//     */
//    public int importPeppCatalog(List<CPeppCatalog> pCPeppCatalog) {
//        int itemNumber = 0;
//        for (CPeppCatalog item : pCPeppCatalog) {
//            persist(item);
//            itemNumber++;
//        }
//        return itemNumber;
//    }
    /**
     * Drop PEPPs custom entries by year
     *
     * @param pYear selected year
     * @param pCountryEn de
     * @return number of deleted items
     */
    public int dropPeppEntriesByYear(int pYear, CountryEn pCountryEn) {
        int deleteCount = 0;

        String query = "DELETE FROM " + CPeppCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.peppYear = :year "
                + "AND a.peppIsNegotiatedFl = 1 "
                + "AND NOT( a.peppHosIdent IS NULL "
                + "AND a.peppValidFrom IS NULL "
                + "AND a.peppValidTo IS NULL "
                + "AND a.peppRelationFrom = -1 "
                + "AND a.peppRelationTo = -1 "
                + "AND a.peppRelationCostWeight = 0 )";

        Query nativeQuery = getEntityManager().createQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn);
        deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }

//    /**
//     * Get PEPP by ID
//     *
//     * @param id id of PEPP
//     * @return PEPP item
//     */
//    public CPeppCatalog getPeppById(Long id) {
//        String queryName = "SELECT a FROM " + CPeppCatalog.class.getSimpleName() + " a WHERE a.id =:id";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("id", id);
//        List<CPeppCatalog> lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
    public int dropPeppEntriesByYearAndHosIdent(int pYear, CountryEn pCountryEn, String pHosIdent) {
        int deleteCount = 0;

        String query = "DELETE FROM " + CPeppCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.peppYear = :year "
                + "AND a.peppIsNegotiatedFl = 1 "
                + "AND a.peppHosIdent = :hosIdent";

        Query nativeQuery = getEntityManager().createQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn);
        nativeQuery.setParameter("hosIdent", pHosIdent);
        deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }
    
    public String getByCode(String grpresCode, String countryCode, int year) {
        String peppDescription;

        String query = "SELECT PEPP_DESCRIPTION FROM C_PEPP_CATALOG"
                + " WHERE PEPP_PEPP = :code "
                + " AND COUNTRY_EN = :country "
                + " AND PEPP_YEAR = :year ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("code", grpresCode);
        nativeQuery.setParameter("country", countryCode);
        nativeQuery.setParameter("year", year);

        // if multiple results, take the first one, like (getByCode method of the CpxDrgCatalog)
        if (!nativeQuery.getResultList().isEmpty()) {
            peppDescription = (String) nativeQuery.getResultList().get(0);
            if (peppDescription != null && !peppDescription.isEmpty()) {
                return peppDescription;
            }
        }
        return "";

        /*       List resultList = nativeQuery.getResultList();
        if (!resultList.isEmpty()) {
            drgDescription = (String) nativeQuery.getSingleResult();
            return (drgDescription == null) ? "" : drgDescription;
        } else {
            return "";
        }
         */
    }

    public int dropPeppEntriesByYearAndHosIdent(int pYear, CountryEn pCountryEn, List<String> pIkzList) {
        int deleteCount = 0;

        String query = "DELETE FROM " + CPeppCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.peppYear = :year "
                + "AND a.peppIsNegotiatedFl = 1 "
                + "AND a.peppHosIdent in( :hosIdent)";

        Query nativeQuery = getEntityManager().createQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn);
        nativeQuery.setParameter("hosIdent", String.join(", ", pIkzList));
        deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }
    
}
