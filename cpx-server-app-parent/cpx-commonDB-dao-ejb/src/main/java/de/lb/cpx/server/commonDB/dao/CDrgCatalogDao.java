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
import de.lb.cpx.server.commonDB.model.CCatalogIF;
import de.lb.cpx.server.commonDB.model.CDrgCatalog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * Data access object for domain model class CDrgCatalog. Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SecurityDomain("cpx")
@SuppressWarnings("unchecked")
public class CDrgCatalogDao extends AbstractCommonDao<CDrgCatalog> { 

    private static final Logger LOG = Logger.getLogger(CDrgCatalogDao.class.getName());

    /**
     * Creates a new instance.
     */
    public CDrgCatalogDao() {
        super(CDrgCatalog.class);
    }

    public boolean catalogExists(final String pChecksum) {
        return super.catalogExists("C_DRG_CATALOG", pChecksum);
    }

    public boolean catalogExists(final int pYear, final CountryEn pCountryEn) {
        return getEntryCounter(pYear, pCountryEn) > 0;
    }

    public int getEntryCounter(final int pYear, final CountryEn pCountryEn) {
        String query = "SELECT COUNT(*) FROM C_DRG_CATALOG "
                + " WHERE DRG_YEAR = :year "
                + "   AND COUNTRY_EN = :country ";
        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        Number count = (Number) nativeQuery.getSingleResult();

        return (count == null) ? 0 : count.intValue();
    }

    public int dropEntries(final int pYear, final CountryEn pCountryEn) {
        int deleteCount = 0;
        String query = "DELETE FROM C_DRG_CATALOG "
                + " WHERE DRG_YEAR = :year "
                + "   AND COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }

    public List<CDrgCatalog> getEntries(final int pYear, final String pCountryEn) {
        List<CDrgCatalog> list = null;
        Query query = getEntityManager().createQuery("from " + CDrgCatalog.class.getSimpleName() + " a where a.drgYear = :year and a.countryEn = :country order by a.drgDrg");
        query.setParameter("year", pYear);
        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        list = query.getResultList();
        return list;
    }

    public String getByCode(String grpresCode, String countryCode, int year) {
        String drgDescription;
//        String hosIdent = null;
        int DrgMdMedianCaseCount = 0;

        // query did not return a unique result in case of there is any "abgesenkte DRGs (I68D und I68E)"
/*        String query = "SELECT DRG_DESCRIPTION FROM C_DRG_CATALOG"
                + " WHERE DRG_DRG = :code "
                + " AND COUNTRY_EN = :country "
                + " AND DRG_YEAR = :year ";
         */
        // cpx-1489 (to handle abgesenkte DRGs as well)
        // not sure whether this is 100% correct ??
        String query = "SELECT DRG_DESCRIPTION FROM C_DRG_CATALOG"
                + " WHERE DRG_DRG = :code "
                + " AND COUNTRY_EN = :country "
                + " AND DRG_YEAR = :year "
                //                + " AND DRG_HOS_IDENT = :hosIdent"
                + " AND DRG_MD_MEDIAN_CASE_COUNT = :DrgMdMedianCaseCount";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("code", grpresCode);
        nativeQuery.setParameter("country", countryCode);
        nativeQuery.setParameter("year", year);
//        nativeQuery.setParameter("hosIdent", hosIdent);
        nativeQuery.setParameter("DrgMdMedianCaseCount", DrgMdMedianCaseCount);

        // if multiple results, take the first one, like (getByCode method of the CpxDrgCatalog)
        if (!nativeQuery.getResultList().isEmpty()) {
            drgDescription = (String) nativeQuery.getResultList().get(0);
            if (drgDescription != null && !drgDescription.isEmpty()) {
                return drgDescription;
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

    /**
     * Get all distinct years of DRG catalog
     *
     * @param pCountryEn "de"
     * @return list of years of DRG catalog
     */
    public List<Integer> getDrgYearList(CountryEn pCountryEn) {
        List<Integer> drgYearsList = new ArrayList<>();
//        try {
        String queryString = "select distinct(drgYear) from " + CDrgCatalog.class.getSimpleName() + " a where a.countryEn = :country order by drgYear desc";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("country", pCountryEn);
        drgYearsList = query.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create list with years of DRG", ex);
//        }
        return drgYearsList;
    }

    /**
     * Get negotiated DRG catalog
     *
     * @param pDrgYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @return DRG catalog for selected year and IKZ
     */
    public List<CDrgCatalog> getDrgCatalogByYearAndIkz(int pDrgYear, String pHosIdent, CountryEn pCountryEn) {
        List<CDrgCatalog> mainDepartmentDrgCatalogIkz = new ArrayList<>();
//        try {

        String queryStringIkz = "SELECT a FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgHosIdent = :hosIdent "
                + "AND ( a.drgIsNegotiatedFl = 1 OR a.drgDrg like 'I68%') "// berücksichtigen abgesenkte DRGs
                + "ORDER BY a.drgDrg, a.drgValidFrom ASC";

        Query queryIkz = getEntityManager().createQuery(queryStringIkz);
        queryIkz.setParameter("year", pDrgYear);
        queryIkz.setParameter("country", pCountryEn);
        queryIkz.setParameter("hosIdent", pHosIdent);

        mainDepartmentDrgCatalogIkz = queryIkz.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create DRG list for selected year", ex);
//
//        }
        return mainDepartmentDrgCatalogIkz;
    }

    /**
     * Get DRG catalog
     *
     * @param pDrgYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @return DRG catalog for selected year and IKZ
     */
    public List<CDrgCatalog> getAllDrgCatalogByYearAndIkz(int pDrgYear, String pHosIdent, CountryEn pCountryEn) {
        List<CDrgCatalog> mainDepartmentDrgCatalogIkz = new ArrayList<>();
//        try {

        String queryStringIkz = "SELECT a FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgHosIdent = :hosIdent "
                + "ORDER BY a.drgDrg, a.drgValidFrom ASC";

        Query queryIkz = getEntityManager().createQuery(queryStringIkz);
        queryIkz.setParameter("year", pDrgYear);
        queryIkz.setParameter("country", pCountryEn);
        queryIkz.setParameter("hosIdent", pHosIdent);

        mainDepartmentDrgCatalogIkz = queryIkz.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create DRG list for selected year", ex);
//
//        }
        return mainDepartmentDrgCatalogIkz;
    }

//    /**
//     * Add new negotiated DRG item
//     *
//     * @param pCDrgCatalog negotiated DRG item
//     * @return new negotiated DRG item
//     */
//    public CDrgCatalog addNegotiatedDrg(CDrgCatalog pCDrgCatalog) {
//        CDrgCatalog drgItem = pCDrgCatalog;
//        try {
//            persist(drgItem);
//            flush();
//            drgItem = pCDrgCatalog;
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't add new negotiated DRG item", ex);
//        }
//        return drgItem;
//    }
//    /**
//     * update negotiated DRG item
//     *
//     * @param pCDrgCatalog negotiated DRG item
//     * @return id for updated negotiated DRG item
//     */
//    public boolean updateNegotiatedDrg(CDrgCatalog pCDrgCatalog) {
//        boolean checkUpdateState = false;
//        try {
//            if (pCDrgCatalog.getId() <= 0L) {
//                checkUpdateState = false;
//            } else {
//                merge(pCDrgCatalog);
//                checkUpdateState = true;
//            }
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't update new negotiated DRG item", ex);
//        }
//        return checkUpdateState;
//    }
    /**
     * Get negotiated default DRG list
     *
     * @param pCountryEn country En
     * @param pDrgYear selected DRG year
     * @return default list of DRGs
     */
    public List<CCatalogIF> getDefaultNegotiatedDrgList(CountryEn pCountryEn, int pDrgYear) {
        List<CCatalogIF> defaultDrgCatalog = new ArrayList<>();
//        try {
        String queryStringDefault = "SELECT a FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgHosIdent IS NULL "
                + "AND a.drgIsNegotiatedFl = 1 "
                + "ORDER BY a.drgDrg ASC";

        Query queryDefault = getEntityManager().createQuery(queryStringDefault);
        queryDefault.setParameter("year", pDrgYear);
        queryDefault.setParameter("country", pCountryEn);

        defaultDrgCatalog = queryDefault.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't create deafault DRG item", ex);
//        }
        return defaultDrgCatalog;
    }

    public List<CDrgCatalog> getDefaultNegoDrgList(CountryEn pCountryEn, int pDrgYear) {
        List<CDrgCatalog> defaultDrgCatalog = new ArrayList<>();
//        try {
        String queryStringDefault = "SELECT a FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgHosIdent IS NULL "
                + "AND a.drgIsNegotiatedFl = 1 "
                + "ORDER BY a.drgDrg ASC";

        Query queryDefault = getEntityManager().createQuery(queryStringDefault);
        queryDefault.setParameter("year", pDrgYear);
        queryDefault.setParameter("country", pCountryEn);

        defaultDrgCatalog = queryDefault.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't create deafault DRG item", ex);
//        }
        return defaultDrgCatalog;
    }

    /**
     * Get all default DRG list
     *
     * @param pCountryEn country En
     * @param pDrgYear selected DRG year
     * @return default list of all DRGs
     */
    public List<CDrgCatalog> getDefaultAllDrgList(CountryEn pCountryEn, int pDrgYear) {
        List<CDrgCatalog> defaultDrgCatalog = new ArrayList<>();
//        try {
        String queryStringDefault = "SELECT a FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgHosIdent IS NULL "
                + "ORDER BY a.drgDrg ASC";

        Query queryDefault = getEntityManager().createQuery(queryStringDefault);
        queryDefault.setParameter("year", pDrgYear);
        queryDefault.setParameter("country", pCountryEn);

        defaultDrgCatalog = queryDefault.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't create deafault DRG item", ex);
//        }
        return defaultDrgCatalog;
    }

    /**
     * Get already entered hospital ident numbers (IKZ) in DRG catalog
     *
     * @param pCountryEn "de"
     * @param pSupplYear selected year
     * @return map of hospital ident numbers (IKZ)
     */
    public List<String> getExistingHosIdentDrgList(CountryEn pCountryEn, int pSupplYear) {
        List<String> existingHosIdentList = new ArrayList<>();
//        try {
        String queryString = "SELECT DISTINCT(a.drgHosIdent) FROM " + CDrgCatalog.class.getSimpleName()
                + " a WHERE a.countryEn = :country "
                + "AND a.drgIsNegotiatedFl = 1 "
                + "AND a.drgYear = :year "
                + "AND a.drgHosIdent IS NOT NULL "
                + "AND (a.drgValidFrom IS NOT NULL "
                + "OR a.drgValidTo IS NOT NULL "
                + "OR a.drgMd1DeductionDay IS NOT NULL "
                + "OR a.drgMd1SurchargeDay IS NOT NULL "
                + "OR a.drgMdAlos IS NOT NULL "
                + "OR a.drgMdCw IS NOT NULL "
                + "OR a.drgMdMCw IS NOT NULL "
                + "OR a.drgMdCwDeduction IS NOT NULL "
                + "OR a.drgMdCwSurcharge IS NOT NULL "
                + "OR a.drgMdCwTransfDeduct IS NOT NULL "
                + "OR a.drgMdIsTransferFl IS NOT NULL "
                + "OR a.drgMdIsReadmFl IS NOT NULL "
                + "OR a.drgEo1DeductionDay IS NOT NULL "
                + "OR a.drgEo1SurchargeDay IS NOT NULL "
                + "OR a.drgEoAlos IS NOT NULL "
                + "OR a.drgEoCw IS NOT NULL "
                + "OR a.drgEoCw IS NOT NULL "
                + "OR a.drgEoaCw IS NOT NULL "
                + "OR a.drgEoaCw IS NOT NULL "
                + "OR a.drgEomCw IS NOT NULL "
                + "OR a.drgEoamCw IS NOT NULL "
                + "OR a.drgEoCwDeduction IS NOT NULL "
                + "OR a.drgEoCwSurcharge IS NOT NULL "
                + "OR a.drgEoCwTransfDeduct IS NOT NULL "
                + "OR a.drgEoIsTransferFl IS NOT NULL "
                + "OR a.drgNegoDayFee IS NOT NULL) "
                + "order by a.drgHosIdent asc";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("country", pCountryEn);
        query.setParameter("year", pSupplYear);
        existingHosIdentList = query.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create list with IKZs", ex);
//        }
        return existingHosIdentList;
    }

    /**
     * Get DRG keys and description by year
     *
     * @param pCountryEn language
     * @param pYear year
     * @return map of DRG keys and description
     */
    public Map<String, String> getDrgKeyByYear(CountryEn pCountryEn, int pYear) {
        Map<String, String> tmpCatalogMap = new HashMap<>();
//        try {

        String queryStringDef = "SELECT DISTINCT (a) FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgIsNegotiatedFl = 1 "
                + "ORDER BY a.drgDrg ASC";

        Query queryDef = getEntityManager().createQuery(queryStringDef);
        queryDef.setParameter("year", pYear);
        queryDef.setParameter("country", pCountryEn);
        List<CDrgCatalog> tmpCatalogListTest = queryDef.getResultList();
        if (!tmpCatalogListTest.isEmpty()) {
            for (CDrgCatalog drg : tmpCatalogListTest) {
                tmpCatalogMap.put(drg.getDrgDrg(), drg.getDrgDescription());
            }
        }
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create list of DRG keys for selected year", ex);
//        }
        return tmpCatalogMap;
    }

//    /**
//     * Delete DRG by ID
//     *
//     * @param id item id
//     * @return boolean value for delete state
//     */
//    public boolean removeDrgById(Long id) {
//        boolean isDeleted = false;
//        try {
//            isDeleted = deleteById(id);
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Cant't delete DRG item", ex);
//        }
//        return isDeleted;
//    }
//    /**
//     * Import new DRG catalog items
//     *
//     * @param pCDrgCatalog list of items
//     * @return number of added DRG catalog items
//     */
//    public int importDrgCatalog(List<CDrgCatalog> pCDrgCatalog) {
//        int itemNumber = 0;
//        for (CDrgCatalog item : pCDrgCatalog) {
//            persist(item);
//            itemNumber++;
//        }
//        return itemNumber;
//    }
    /**
     * Drop DRGs custom entries by year
     *
     * @param pYear selected year
     * @param pCountryEn de
     * @return number of deleted items
     */
    public int dropDrgEntriesByYear(int pYear, CountryEn pCountryEn) {
        int deleteCount = 0;

        String queryString = "DELETE FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgIsNegotiatedFl = 1 "
                + "AND a.drgHosIdent IS NOT NULL "
                + "AND a.drgNegoDayFee =0 "
                + "AND NOT( a.drgValidFrom IS NULL "
                + "AND a.drgValidTo IS NULL "
                + "AND a.drgMd1DeductionDay = 0 "
                + "AND a.drgMd1SurchargeDay = 0 "
                + "AND a.drgMdAlos = 0 "
                + "AND a.drgMdCw = 0 "
                + "AND a.drgMdMCw = 0 "
                + "AND a.drgMdCwDeduction = 0 "
                + "AND a.drgMdCwSurcharge = 0 "
                + "AND a.drgMdCwTransfDeduct = 0 "
                + "AND a.drgEo1DeductionDay = 0 "
                + "AND a.drgEo1SurchargeDay = 0 "
                + "AND a.drgEoAlos = 0 "
                + "AND a.drgEoCw = 0 "
                + "AND a.drgEoaCw = 0 "
                + "AND a.drgEomCw = 0 "
                + "AND a.drgEoamCw = 0 "
                + "AND a.drgEoCwDeduction = 0 "
                + "AND a.drgEoCwSurcharge = 0 "
                + "AND a.drgEoCwTransfDeduct = 0) ";

        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("year", pYear);
        query.setParameter("country", pCountryEn);
        deleteCount = query.executeUpdate();

        return deleteCount;
    }

    /**
     * Drop DRGs custom entries by year
     *
     * @param pYear selected year
     * @param pCountryEn de
     * @return number of deleted items
     */
    public int dropDrgEntriesByYearAndHosIdent(int pYear, CountryEn pCountryEn, String pHosIdent) {
        int deleteCount = 0;

        String queryString = "DELETE FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgIsNegotiatedFl = 1 "
                + "AND a.drgHosIdent = :hosIdent "
                + "AND a.drgNegoDayFee =0 "
                + "AND NOT( a.drgValidFrom IS NULL "
                + "AND a.drgValidTo IS NULL "
                + "AND a.drgMd1DeductionDay = 0 "
                + "AND a.drgMd1SurchargeDay = 0 "
                + "AND a.drgMdAlos = 0 "
                + "AND a.drgMdCw = 0 "
                + "AND a.drgMdMCw = 0 "
                + "AND a.drgMdCwDeduction = 0 "
                + "AND a.drgMdCwSurcharge = 0 "
                + "AND a.drgMdCwTransfDeduct = 0 "
                + "AND a.drgEo1DeductionDay = 0 "
                + "AND a.drgEo1SurchargeDay = 0 "
                + "AND a.drgEoAlos = 0 "
                + "AND a.drgEoCw = 0 "
                + "AND a.drgEoaCw = 0 "
                + "AND a.drgEomCw = 0 "
                + "AND a.drgEoamCw = 0 "
                + "AND a.drgEoCwDeduction = 0 "
                + "AND a.drgEoCwSurcharge = 0 "
                + "AND a.drgEoCwTransfDeduct = 0) ";

        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("year", pYear);
        query.setParameter("country", pCountryEn);
        query.setParameter("hosIdent", pHosIdent);
        deleteCount = query.executeUpdate();

        return deleteCount;
    }
    
    public int dropDrgNegoEntriesByYearAndHospIdentList(int pYear, CountryEn pCountryEn, List<String> pIkzList, List<String> pDrgList) {
        if(pIkzList == null || pIkzList.isEmpty()){
            return 0;
        }
        int deleteCount = 0;
         String queryString = "DELETE FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgIsNegotiatedFl = 1 "
                + "AND a.drgHosIdent in ( :hosIdent) "
                + ((pDrgList == null || pDrgList.isEmpty())?"": "AND a.drgDrg in ( :drgList) ");

        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("year", pYear);
        query.setParameter("country", pCountryEn);
        query.setParameter("hosIdent", String.join(",", pIkzList));
        if(pDrgList != null && !pDrgList.isEmpty()){
            query.setParameter("drgList", String.join(",", pDrgList));
        }
        deleteCount = query.executeUpdate();

        return deleteCount;
    }
    
    public int dropDrgEntriesByYearAndHospIdentList(int pYear, CountryEn pCountryEn, List<String> pIkzList) {
        if(pIkzList == null || pIkzList.isEmpty()){
            return 0;
        }
        int deleteCount = 0;
         String queryString = "DELETE FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgIsNegotiatedFl = 1 "
                + "AND a.drgHosIdent in ( :hosIdent) "
                + "AND a.drgNegoDayFee =0 "
                + "AND NOT( a.drgValidFrom IS NULL "
                + "AND a.drgValidTo IS NULL "
                + "AND a.drgMd1DeductionDay = 0 "
                + "AND a.drgMd1SurchargeDay = 0 "
                + "AND a.drgMdAlos = 0 "
                + "AND a.drgMdCw = 0 "
                + "AND a.drgMdMCw = 0 "
                + "AND a.drgMdCwDeduction = 0 "
                + "AND a.drgMdCwSurcharge = 0 "
                + "AND a.drgMdCwTransfDeduct = 0 "
                + "AND a.drgEo1DeductionDay = 0 "
                + "AND a.drgEo1SurchargeDay = 0 "
                + "AND a.drgEoAlos = 0 "
                + "AND a.drgEoCw = 0 "
                + "AND a.drgEoaCw = 0 "
                + "AND a.drgEomCw = 0 "
                + "AND a.drgEoamCw = 0 "
                + "AND a.drgEoCwDeduction = 0 "
                + "AND a.drgEoCwSurcharge = 0 "
                + "AND a.drgEoCwTransfDeduct = 0) ";

        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("year", pYear);
        query.setParameter("country", pCountryEn);
        query.setParameter("hosIdent", String.join(",", pIkzList));
        deleteCount = query.executeUpdate();

        return deleteCount;
       
    }

    /**
     * Drop DRGs daily fee custom entries by year
     *
     * @param pYear selected year
     * @param pCountryEn de
     * @return number of deleted items
     */
    public int dropDrgDailyFeeByYear(int pYear, CountryEn pCountryEn) {

        String queryString = "DELETE FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgIsNegotiatedFl = 1 "
                + "AND a.drgHosIdent IS NOT NULL "
                + "AND a.drgNegoDayFee !=0 ";

        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("year", pYear);
        query.setParameter("country", pCountryEn);
        int deleteCount = query.executeUpdate();

        return deleteCount;
    }
    
    public int dropDrgDailyFeeByYearAndHospIdentList(int pYear, CountryEn pCountryEn, List<String> pIkzList){
        if(pIkzList == null || pIkzList.isEmpty()){
            return 0;
        }
        String queryString = "DELETE FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgIsNegotiatedFl = 1 "
                + "AND a.drgHosIdent in (:hosIdent ) " 
                + "AND a.drgNegoDayFee !=0 ";

        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("year", pYear);
        query.setParameter("country", pCountryEn);
        query.setParameter("hosIdent", String.join(", ", pIkzList));
        int deleteCount = query.executeUpdate();

        return deleteCount;
       
    }

    /**
     * Drop DRGs daily fee custom entries by year
     *
     * @param pYear selected year
     * @param pCountryEn de
     * @return number of deleted items
     */
    public int dropDrgDailyFeeByYearAndHosIdent(int pYear, CountryEn pCountryEn, String pHosIdent) {

        String queryString = "DELETE FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgIsNegotiatedFl = 1 "
                + "AND a.drgHosIdent =:hosIdent "
                + "AND a.drgNegoDayFee !=0 ";

        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("year", pYear);
        query.setParameter("country", pCountryEn);
        query.setParameter("hosIdent", pHosIdent);
        int deleteCount = query.executeUpdate();

        return deleteCount;
    }

//    /**
//     * Get DRG by ID
//     *
//     * @param id id of DRG
//     * @return DRG item
//     */
//    public CDrgCatalog getDrgById(Long id) {
//        String queryName = "SELECT a FROM " + CDrgCatalog.class.getSimpleName() + " a WHERE a.id =:id";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("id", id);
//        List<CDrgCatalog> lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
    /**
     * Get only DRG daily fee
     *
     * @param pCountryEn de
     * @param pYear selected year
     * @return list of DRG daily fee
     */
    public List<CCatalogIF> getDailyFeeList(CountryEn pCountryEn, int pYear) {
        List<CCatalogIF> drgDailyFeeList = new ArrayList<>();
//        try {
        String queryString = "SELECT a FROM " + CDrgCatalog.class.getSimpleName()
                + " a WHERE a.countryEn = :country "
                + "AND a.drgIsNegotiatedFl = 1 "
                + "AND a.drgYear = :year "
                + "AND a.drgHosIdent IS NOT NULL "
                + "AND a.drgNegoDayFee IS NOT NULL "
                + "AND a.drgNegoDayFee !=0 "
                + "ORDER BY a.drgHosIdent, a.drgDrg, a.drgValidFrom ASC";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("country", pCountryEn);
        query.setParameter("year", pYear);
        drgDailyFeeList = query.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create list with DRG daily fee", ex);
//        }
        return drgDailyFeeList;
    }
    
    public List<CCatalogIF> getNegotiatedDrgsWithIKsList(CountryEn pCountryEn, int pYear) {
        List<CCatalogIF> drgNegotiatedList = new ArrayList<>();
         String queryString = "SELECT a FROM "  + CDrgCatalog.class.getSimpleName()
                + " a WHERE a.countryEn = :country "
                + "AND a.drgIsNegotiatedFl = 1 "
                + "AND a.drgYear = :year "
                + "AND a.drgHosIdent IS NOT NULL "

                + "ORDER BY a.drgHosIdent, a.drgDrg, a.drgValidFrom ASC";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("country", pCountryEn);
        query.setParameter("year", pYear);
        drgNegotiatedList = query.getResultList();


        return drgNegotiatedList;
   }
    /**
     * Get only DRG main and external department
     *
     * @param pCountryEn de
     * @param pYear selected year
     * @return list of DRG main and external department
     */
    public List<CDrgCatalog> getMainAndExternalDepartmentList(CountryEn pCountryEn, int pYear) {
        List<CDrgCatalog> drgDailyFeeList = new ArrayList<>();
//        try {
        String queryString = "SELECT a FROM " + CDrgCatalog.class.getSimpleName()
                + " a WHERE a.countryEn = :country "
                + "AND a.drgIsNegotiatedFl = 1 "
                + "AND a.drgYear = :year "
                + "AND a.drgHosIdent IS NOT NULL "
                + "AND (a.drgNegoDayFee IS NULL "
                + "OR a.drgNegoDayFee = 0) "
                + "AND (a.drgValidFrom IS NOT NULL "
                + "OR a.drgValidTo IS NOT NULL "
                + "OR a.drgMd1DeductionDay IS NOT NULL "
                + "OR a.drgMd1SurchargeDay IS NOT NULL "
                + "OR a.drgMdAlos IS NOT NULL "
                + "OR a.drgMdCw IS NOT NULL "
                + "OR a.drgMdMCw IS NOT NULL "
                + "OR a.drgMdCwDeduction IS NOT NULL "
                + "OR a.drgMdCwSurcharge IS NOT NULL "
                + "OR a.drgMdCwTransfDeduct IS NOT NULL "
                + "OR a.drgMdIsTransferFl IS NOT NULL "
                + "OR a.drgMdIsReadmFl IS NOT NULL "
                + "OR a.drgEo1DeductionDay IS NOT NULL "
                + "OR a.drgEo1SurchargeDay IS NOT NULL "
                + "OR a.drgEoAlos IS NOT NULL "
                + "OR a.drgEoCw IS NOT NULL "
                + "OR a.drgEoCw IS NOT NULL "
                + "OR a.drgEoaCw IS NOT NULL "
                + "OR a.drgEoaCw IS NOT NULL "
                + "OR a.drgEomCw IS NOT NULL "
                + "OR a.drgEoamCw IS NOT NULL "
                + "OR a.drgEoCwDeduction IS NOT NULL "
                + "OR a.drgEoCwSurcharge IS NOT NULL "
                + "OR a.drgEoCwTransfDeduct IS NOT NULL "
                + "OR a.drgEoIsTransferFl IS NOT NULL) "
                + "ORDER BY a.drgHosIdent, a.drgDrg, a.drgValidFrom ASC";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("country", pCountryEn);
        query.setParameter("year", pYear);
        drgDailyFeeList = query.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create list with DRG main and external department", ex);
//        }
        return drgDailyFeeList;
    }

    public List<CCatalogIF> getDefaultI68DrgList(CountryEn countryEn, int pSelectedYear) {
        List<CCatalogIF> defaultI68Catalog = new ArrayList<>();
//        try {
        String queryStringDefault = "SELECT a FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgHosIdent IS NULL "
                + "AND a.drgIsNegotiatedFl = 0 "
                + "AND a.drgDrg like 'I68%' "
                + "AND a.drgMdMedianCaseCount > 0 "
                + "ORDER BY a.drgDrg ASC";

        Query queryDefault = getEntityManager().createQuery(queryStringDefault);
        queryDefault.setParameter("year", pSelectedYear);
        queryDefault.setParameter("country", countryEn);

        defaultI68Catalog = queryDefault.getResultList();

        return defaultI68Catalog;
    }

    public List<CCatalogIF> getI68DrgList(CountryEn countryEn, int pSelectedYear) {
        List<CCatalogIF> i68Catalog = new ArrayList<>();
//        try {
        String queryStringDefault = "SELECT a FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgHosIdent IS NOT NULL "
                + "AND a.drgIsNegotiatedFl = 0 "
                + "AND a.drgDrg like 'I68%' "
                + "AND a.drgMdMedianCaseCount > 0 "
                +"ORDER BY a.drgDrg, a.drgValidFrom ASC";

        Query queryDefault = getEntityManager().createQuery(queryStringDefault);
        queryDefault.setParameter("year", pSelectedYear);
        queryDefault.setParameter("country", countryEn);

        i68Catalog = queryDefault.getResultList();

        return i68Catalog;
    }

    public int dropDrgI68EntriesByYearAndHospIdentList(int pYear, CountryEn pCountryEn, List<String> pIkzList, List<String> pDrgList) {
       if(pIkzList == null || pIkzList.isEmpty()){
            return 0;
        }
        int deleteCount = 0;
         String queryString = "DELETE FROM " + CDrgCatalog.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.drgYear = :year "
                + "AND a.drgIsNegotiatedFl = 0 "
                + "AND a.drgHosIdent in ( :hosIdent) "
                + ((pDrgList == null || pDrgList.isEmpty())?"": "AND a.drgDrg in ( :drgList) ");

        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("year", pYear);
        query.setParameter("country", pCountryEn);
        query.setParameter("hosIdent", String.join(",", pIkzList));
        if(pDrgList != null && !pDrgList.isEmpty()){
            query.setParameter("drgList", String.join(",", pDrgList));
        }
        deleteCount = query.executeUpdate();

        return deleteCount;
    }

    public int dropDrgI68EntriesByYearAndHospIdentList(int pYear, CountryEn pCountryEn, Map<String, List<String>> importIkzList) {
        if(importIkzList == null || importIkzList.isEmpty()){
            return 0;
        }
        int deleteCount = 0;
         String queryString = "DELETE FROM C_DRG_CATALOG " 
                + " WHERE COUNTRY_EN = '" + pCountryEn.name()
                + "' AND DRG_YEAR = " + pYear
                + " AND DRG_IS_NEGOTIATED_FL = 0 AND "
                + getIk2DrgSqlString(importIkzList, "DRG_HOS_IDENT", "DRG_DRG"); 
          
                Query query = getEntityManager().createNativeQuery(queryString);


            deleteCount = query.executeUpdate();

        return deleteCount;
    }

    public int dropDrgNegoEntriesByYearAndHospIdentList(int pYear, CountryEn pCountryEn, Map<String, List<String>> importIkzList) {
        if(importIkzList == null || importIkzList.isEmpty()){
            return 0;
        }
        int deleteCount = 0;
         String queryString = "DELETE FROM C_DRG_CATALOG " 
                + "WHERE COUNTRY_EN = '" + pCountryEn.name()
                + "' AND DRG_YEAR = " + pYear
                + " AND DRG_IS_NEGOTIATED_FL = 1 "
                + " AND " + getIk2DrgSqlString(importIkzList, "DRG_HOS_IDENT", "DRG_DRG"); 
         
         LOG.log(Level.INFO, queryString );
                Query query = getEntityManager().createNativeQuery(queryString);


            deleteCount = query.executeUpdate();

        return deleteCount;
    }
    

}
