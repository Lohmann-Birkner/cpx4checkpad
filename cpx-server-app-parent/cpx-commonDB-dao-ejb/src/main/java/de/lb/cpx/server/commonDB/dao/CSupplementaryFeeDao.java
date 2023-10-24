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
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.commonDB.model.CCatalogIF;
import de.lb.cpx.server.commonDB.model.CSupplementaryFee;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * Data access object for domain model class CSupplementaryFee. Initially
 * generated at 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SecurityDomain("cpx")
@SuppressWarnings("unchecked")
public class CSupplementaryFeeDao extends AbstractCommonDao<CSupplementaryFee> {

    private static final Logger LOG = Logger.getLogger(CSupplementaryFeeDao.class.getName());

    /**
     * Creates a new instance.
     */
    public CSupplementaryFeeDao() {
        super(CSupplementaryFee.class);
    }

    public boolean catalogExists(final String pChecksum) {
        return super.catalogExists("C_SUPPLEMENTARY_FEE", pChecksum);
    }

    public boolean catalogExists(final int pYear, final CountryEn pCountryEn, final String pSupplementType) {
        return getEntryCounter(pYear, pCountryEn, pSupplementType) > 0;
    }

    public int getEntryCounter(final int pYear, final CountryEn pCountryEn, final String pSupplementType) {
        String query = "SELECT COUNT(*) FROM C_SUPPLEMENTARY_FEE "
                + " WHERE C_SUPPLEMENTARY_FEE.SUPPL_TYPE_EN = :supp_type "
                + "   AND C_SUPPLEMENTARY_FEE.SUPPL_YEAR = :year "
                + "   AND C_SUPPLEMENTARY_FEE.COUNTRY_EN = :country ";
        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("supp_type", pSupplementType);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        Number count = (Number) nativeQuery.getSingleResult();

        return (count == null) ? 0 : count.intValue();
    }

    public int dropEntries(final int pYear, final CountryEn pCountryEn, final String pSupplementType) {
        int deleteCount = 0;
        String query = "DELETE FROM C_SUPPLEMENTARY_FEE "
                + " WHERE C_SUPPLEMENTARY_FEE.SUPPL_TYPE_EN = :supp_type "
                + "   AND C_SUPPLEMENTARY_FEE.SUPPL_YEAR = :year "
                + "   AND C_SUPPLEMENTARY_FEE.COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("supp_type", pSupplementType);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }

    public List<CSupplementaryFee> getEntries(final int pYear, final String pCountryEn, final String pSupplementType) {
        List<CSupplementaryFee> list = null;
        Query query = getEntityManager().createQuery("from " + CSupplementaryFee.class.getSimpleName() + " a where a.supplTypeEn = :supp_type and a.supplYear = :year and a.countryEn = :country order by a.supplKey");
        query.setParameter("supp_type", SupplFeeTypeEn.valueOf(pSupplementType));
        query.setParameter("year", pYear);
        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        list = query.getResultList();
        return list;
    }

    /**
     * Get all distinct years of supplementary fee catalog
     *
     * @param pCountryEn "de"
     * @return list of years of supplementary fee
     */
    public List<Integer> getFeeYearList(CountryEn pCountryEn) {
        List<Integer> feeYearsList = new ArrayList<>();
//        try {
        String queryString = "SELECT DISTINCT(supplYear) from " + CSupplementaryFee.class.getSimpleName()
                + " a WHERE a.countryEn = :country ORDER BY supplYear desc";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("country", pCountryEn);
        feeYearsList = query.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create list with years of supplementary fee", ex);
//        }
        return feeYearsList;
    }

    /**
     * Get negotiated supplementary fee catalog
     *
     * @param pFeeYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @param pSupplTypeEn catalog type
     * @return fee catalog for selected year, type and IKZ
     */
    public List<CSupplementaryFee> getFeeCatalogByYearIkzAndType(int pFeeYear, String pHosIdent, CountryEn pCountryEn, SupplFeeTypeEn pSupplTypeEn) {
        List<CSupplementaryFee> supplementaryFeeCatalogIkz = new ArrayList<>();
//        try {

        String queryStringIkz = "SELECT a FROM " + CSupplementaryFee.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.supplYear = :year "
                + "AND a.supplTypeEn = :type "
                + "AND a.supplHosIdent = :hosIdent "
                + "AND a.supplNegotiated = 1 "
                + "ORDER BY a.supplKey, a.supplValidFrom ASC";

        Query queryIkz = getEntityManager().createQuery(queryStringIkz);
        queryIkz.setParameter("year", pFeeYear);
        queryIkz.setParameter("type", pSupplTypeEn);
        queryIkz.setParameter("country", pCountryEn);
        queryIkz.setParameter("hosIdent", pHosIdent);

        supplementaryFeeCatalogIkz = queryIkz.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create supplementary fee catalog for selected year", ex);
//
//        }
        return supplementaryFeeCatalogIkz;
    }

    /**
     * Get all supplementary fee catalog
     *
     * @param pFeeYear selected yaer
     * @param pHosIdent selected IKZ
     * @param pCountryEn "de"
     * @param pSupplTypeEn catalog type
     * @return fee catalog for selected year, type and IKZ
     */
    public List<CSupplementaryFee> getAllFeeCatalogByYearIkzAndType(int pFeeYear, String pHosIdent, CountryEn pCountryEn, SupplFeeTypeEn pSupplTypeEn) {
        List<CSupplementaryFee> supplementaryFeeCatalogIkz = new ArrayList<>();
//        try {

        String queryStringIkz = "SELECT a FROM " + CSupplementaryFee.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.supplYear = :year "
                + "AND a.supplTypeEn = :type "
                + "AND a.supplHosIdent = :hosIdent "
                + "ORDER BY a.supplKey, a.supplValidFrom ASC";

        Query queryIkz = getEntityManager().createQuery(queryStringIkz);
        queryIkz.setParameter("year", pFeeYear);
        queryIkz.setParameter("type", pSupplTypeEn);
        queryIkz.setParameter("country", pCountryEn);
        queryIkz.setParameter("hosIdent", pHosIdent);

        supplementaryFeeCatalogIkz = queryIkz.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create supplementary fee catalog for selected year", ex);
//
//        }
        return supplementaryFeeCatalogIkz;
    }

    /**
     * Add new supplementary fee item
     *
     * @param pCSupplementaryFee negotiated fee item
     * @return new negotiated fee item
     */
    public CSupplementaryFee addNegotiatedSuplFee(CSupplementaryFee pCSupplementaryFee) {
        CSupplementaryFee feeItem = pCSupplementaryFee;
//        try {
        persist(feeItem);
        flush();
//            feeItem = pCSupplementaryFee;
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't add new negotiated supplementary fee  item", ex);
//        }
        return feeItem;
    }

//    /**
//     * update negotiated supplementary fee item
//     *
//     * @param pCSupplementaryFee negotiated fee item
//     * @return id for updated negotiated fee item
//     */
//    public boolean updateNegotiatedFee(CSupplementaryFee pCSupplementaryFee) {
//        boolean checkUpdateState = false;
//        try {
//            if (pCSupplementaryFee.getId() <= 0L) {
//                checkUpdateState = false;
//            } else {
//                merge(pCSupplementaryFee);
//                checkUpdateState = true;
//            }
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't update new negotiated supplementary fee item", ex);
//        }
//        return checkUpdateState;
//    }
    /**
     * Get negotiated default supplementary fee list
     *
     * @param pCountryEn catalog language
     * @param pSupplYear catalog year
     * @param pSupplTypeEn catalog type
     * @return fee catalog for selected year, type and IKZ
     */
    public List<CSupplementaryFee> getDefaultNegotiatedSuplFeeList(CountryEn pCountryEn, int pSupplYear, SupplFeeTypeEn pSupplTypeEn) {
        List<CSupplementaryFee> defaultFeeCatalog = new ArrayList<>();
//        try {
        String queryStringDefault = "SELECT a FROM " + CSupplementaryFee.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.supplYear = :year "
                + "AND a.supplTypeEn = :type "
                + "AND a.supplHosIdent IS NULL "
                + "AND a.supplNegotiated = 1 "
                + "ORDER BY a.supplKey, a.supplValidFrom ASC";

        Query queryDefault = getEntityManager().createQuery(queryStringDefault);
        queryDefault.setParameter("year", pSupplYear);
        queryDefault.setParameter("type", pSupplTypeEn);
        queryDefault.setParameter("country", pCountryEn);

        defaultFeeCatalog = queryDefault.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't create deafault supplementary fee catalog", ex);
//        }
        return defaultFeeCatalog;
    }
    
     public List<CCatalogIF> getDefaultNegoSuplFeeList(CountryEn pCountryEn, int pSupplYear, SupplFeeTypeEn pSupplTypeEn) {
        List<CCatalogIF> defaultFeeCatalog = new ArrayList<>();
//        try {
        String queryStringDefault = "SELECT a FROM " + CSupplementaryFee.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.supplYear = :year "
                + "AND a.supplTypeEn = :type "
                + "AND a.supplHosIdent IS NULL "
                + "AND a.supplNegotiated = 1 "
                + "ORDER BY a.supplKey, a.supplValidFrom ASC";

        Query queryDefault = getEntityManager().createQuery(queryStringDefault);
        queryDefault.setParameter("year", pSupplYear);
        queryDefault.setParameter("type", pSupplTypeEn);
        queryDefault.setParameter("country", pCountryEn);

        defaultFeeCatalog = queryDefault.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't create deafault supplementary fee catalog", ex);
//        }
        return defaultFeeCatalog;
    }
    /**
     * Get all default supplementary fee list
     *
     * @param pCountryEn catalog language
     * @param pSupplYear catalog year
     * @param pSupplTypeEn catalog type
     * @return fee catalog for selected year, type and IKZ
     */
    public List<CSupplementaryFee> getDefaultAllSuplFeeList(CountryEn pCountryEn, int pSupplYear, SupplFeeTypeEn pSupplTypeEn) {
        List<CSupplementaryFee> defaultFeeCatalog = new ArrayList<>();
//        try {
        String queryStringDefault = "SELECT a FROM " + CSupplementaryFee.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.supplYear = :year "
                + "AND a.supplTypeEn = :type "
                + "AND a.supplHosIdent IS NULL "
                + "ORDER BY a.supplKey, a.supplValidFrom ASC";

        Query queryDefault = getEntityManager().createQuery(queryStringDefault);
        queryDefault.setParameter("year", pSupplYear);
        queryDefault.setParameter("type", pSupplTypeEn);
        queryDefault.setParameter("country", pCountryEn);

        defaultFeeCatalog = queryDefault.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't create deafault supplementary fee catalog", ex);
//        }
        return defaultFeeCatalog;
    }

    /**
     * Get already entered hospital ident numbers (IKZ) in supplementary fee
     * catalog
     *
     * @param pCountryEn catalog language
     * @param pSupplYear selected year
     * @param pSupplTypeEn catalog type
     * @return map of hospital ident numbers (IKZ)
     */
    public List<String> getExistingHosIdentFeeList(CountryEn pCountryEn, int pSupplYear, SupplFeeTypeEn pSupplTypeEn) {
        List<String> existingHosIdentList = new ArrayList<>();
//        try {
        String queryString = "SELECT DISTINCT(a.supplHosIdent) FROM " + CSupplementaryFee.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.supplNegotiated = 1 "
                + "AND a.supplYear = :year "
                + "AND a.supplTypeEn = :type "
                + "AND a.supplHosIdent IS NOT NULL "
                + "AND (a.supplValidTo IS NOT NULL "
                + "OR a.supplValidFrom IS NOT NULL "
                + "OR a.supplCwValue IS NOT NULL) "
                + "ORDER BY a.supplHosIdent ASC";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("country", pCountryEn);
        query.setParameter("year", pSupplYear);
        query.setParameter("type", pSupplTypeEn);
        existingHosIdentList = query.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create list with IKZs", ex);
//        }
        return existingHosIdentList;
    }

    /**
     * Get already entered hospital ident numbers (IKZ) in supplementary fee for
     * PEPP catalog
     *
     * @param pCountryEn catalog language
     * @param pSupplYear selected year
     * @param pSupplTypeEnDf catalog type day fee
     * @param pSupplTypeEnSp catalog type suplementary fee for PEPP
     * @return map of hospital ident numbers (IKZ)
     */
    public List<String> getExistingHosIdentSupplFeePeppList(CountryEn pCountryEn, int pSupplYear, SupplFeeTypeEn pSupplTypeEnDf, SupplFeeTypeEn pSupplTypeEnSp) {
        List<String> existingHosIdentList = new ArrayList<>();
//        try {
        String queryString = "SELECT DISTINCT(a.supplHosIdent) FROM " + CSupplementaryFee.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.supplYear = :year "
                + "AND a.supplNegotiated = 1 "
                + "AND (a.supplTypeEn = :type1 "
                + "OR a.supplTypeEn = :type2) "
                + "AND a.supplHosIdent IS NOT NULL "
                + "AND (a.supplValidTo IS NOT NULL "
                + "OR a.supplValidFrom IS NOT NULL "
                + "OR a.supplCwValue IS NOT NULL) "
                + "ORDER BY a.supplHosIdent ASC";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("country", pCountryEn);
        query.setParameter("year", pSupplYear);
        query.setParameter("type1", pSupplTypeEnDf);
        query.setParameter("type2", pSupplTypeEnSp);
        existingHosIdentList = query.getResultList();
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create list with IKZs", ex);
//        }
        return existingHosIdentList;
    }

    /**
     * Get fee keys and description by year
     *
     * @param pCountryEn language
     * @param pYear year
     * @param pSupplFeeType fee type
     * @return map of fee keys and description
     */
    public Map<String, String> getFeeKeyByYear(CountryEn pCountryEn, int pYear, String pSupplFeeType) {
        Map<String, String> tmpCatalogMap = new HashMap<>();
//        try {

        String queryStringDef = "SELECT DISTINCT (a) FROM " + CSupplementaryFee.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.supplYear = :year "
                + "AND a.supplTypeEn = :type "
                + "AND a.supplNegotiated = 1 "
                + "ORDER BY a.supplKey ASC";

        Query queryDef = getEntityManager().createQuery(queryStringDef);
        queryDef.setParameter("year", pYear);
        queryDef.setParameter("type", SupplFeeTypeEn.valueOf(pSupplFeeType));
        queryDef.setParameter("country", pCountryEn);

        List<CSupplementaryFee> tmpCatalogListTest = queryDef.getResultList();
        if (!tmpCatalogListTest.isEmpty()) {
            for (CSupplementaryFee drg : tmpCatalogListTest) {
                tmpCatalogMap.put(drg.getSupplKey(), drg.getSupplDefinition());
            }
        }
//        } catch (Exception ex) {
//            LOG.log(Level.INFO, "Cant't create list of fees keys for selected year", ex);
//
//        }
        return tmpCatalogMap;
    }

//    /**
//     * Delete supl fee by ID
//     *
//     * @param id item id
//     * @return boolean value for delete state
//     */
//    public boolean removeSupplFeeById(Long id) {
//        boolean isDeleted = false;
//        try {
//            isDeleted = deleteById(id);
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Cant't delete fee item", ex);
//        }
//        return isDeleted;
//    }
//    /**
//     * Import new suppl. fee catalog items
//     *
//     * @param pCSupplementaryFee list of items
//     * @return number of added suppl fee catalog items
//     */
//    public int importSupplFeeCatalog(List<CSupplementaryFee> pCSupplementaryFee) {
//        int itemNumber = 0;
//        try {
//            for (CSupplementaryFee item : pCSupplementaryFee) {
//                persist(item);
//                itemNumber++;
//            }
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Cant't add custom fee item in the table", ex);
//        }
//        return itemNumber;
//    }
    /**
     * Drop suppl. fee custom entries by year
     *
     * @param pYear selected year
     * @param pCountryEn de
     * @param pSupplFeeType suppl fee type
     * @return number of deleted items
     */
    public int dropSupplFeeEntriesByYear(int pYear, CountryEn pCountryEn, SupplFeeTypeEn pSupplFeeType) {
        int deleteCount = 0;
//        try {
        String query = "DELETE FROM " + CSupplementaryFee.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.supplYear = :year "
                + "AND a.supplNegotiated = 1 "
                + "AND a.supplTypeEn = :feeType "
                + "AND (a.supplHosIdent IS NOT NULL "
                + "AND a.supplValue IS NOT NULL "
                + "AND a.supplValidFrom IS NOT NULL "
                + "AND a.supplValidTo IS NOT NULL) ";

        Query nativeQuery = getEntityManager().createQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn);
        nativeQuery.setParameter("feeType", pSupplFeeType);
        deleteCount = nativeQuery.executeUpdate();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Cant't delete custom fee item from table", ex);
//        }
        return deleteCount;
    }

//    /**
//     * Get supplementary fee by ID
//     *
//     * @param id id of supplementary fee
//     * @return supplementary fee item
//     */
//    public CSupplementaryFee getSupplFeeById(Long id) {
//        String queryName = "SELECT a FROM " + CSupplementaryFee.class.getSimpleName() + " a WHERE a.id =:id";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("id", id);
//        List<CSupplementaryFee> lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
    public int dropSupplFeeEntriesByYearAndHosIdent(int pYear, CountryEn pCountryEn, SupplFeeTypeEn pSupplFeeType, String pHosIdent) {
        int deleteCount = 0;
//        try {
        String query = "DELETE FROM " + CSupplementaryFee.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.supplYear = :year "
                + "AND a.supplNegotiated = 1 "
                + "AND a.supplTypeEn = :feeType "
                + "AND a.supplHosIdent =:hosIdent ";

        Query nativeQuery = getEntityManager().createQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn);
        nativeQuery.setParameter("feeType", pSupplFeeType);
        nativeQuery.setParameter("hosIdent", pHosIdent);
        deleteCount = nativeQuery.executeUpdate();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Cant't delete custom fee item from table", ex);
//        }
        return deleteCount;
    }

    public int dropSupplFeeEntriesByYearAndHosIdentList(int pYear, SupplFeeTypeEn pType, CountryEn pCountryEn, List<String> pIkzList) {
        if(pIkzList == null || pIkzList.isEmpty()){
            return 0;
        }
        int deleteCount = 0;
//        try {
        String query = "DELETE FROM " + CSupplementaryFee.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.supplYear = :year "
                + "AND a.supplNegotiated = 1 "
                + "AND a.supplTypeEn = :feeType "
                + "AND a.supplHosIdent in (:hosIdent) ";

        Query nativeQuery = getEntityManager().createQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn);
        nativeQuery.setParameter("feeType", pType);
        nativeQuery.setParameter("hosIdent", String.join(",", pIkzList));
        deleteCount = nativeQuery.executeUpdate();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Cant't delete custom fee item from table", ex);
//        }
        return deleteCount;
    }

    public int dropSupplFeeEntriesByYearAndHospIdentList(int pYear, SupplFeeTypeEn pType, CountryEn pCountryEn, Map<String, List<String>> importCode2Ik) {
        if(importCode2Ik == null || importCode2Ik.isEmpty()){
            return 0;
        }
        int deleteCount = 0;
//        try {
        String queryString = "DELETE FROM C_SUPPLEMENTARY_FEE "
                + "WHERE  COUNTRY_EN = '" + pCountryEn.name()
                + "' AND SUPPL_YEAR = " + pYear
                + " AND SUPPL_NEGOTIATED = 1 "
                + " AND SUPPL_TYPE_EN =  '" + pType.name()
                + "' AND " + getIk2DrgSqlString(importCode2Ik, "SUPPL_HOS_IDENT", "SUPPL_KEY"); 

         LOG.log(Level.INFO, queryString );
                Query query = getEntityManager().createNativeQuery(queryString);


            deleteCount = query.executeUpdate();

        return deleteCount;
    }

    public List<CCatalogIF> getNegoSupplWithIkFeeList(int pYear, SupplFeeTypeEn pType, CountryEn pCountryEn) {
         List<CCatalogIF> tmpCatalogListTest = new ArrayList<>();

        String queryStringDef = "SELECT DISTINCT (a) FROM " + CSupplementaryFee.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.supplYear = :year "
                + "AND a.supplTypeEn = :type "
                + "AND a.supplNegotiated = 1 "
                + "AND a.supplHosIdent IS NOT NULL "
                + "ORDER BY a.supplHosIdent, a.supplKey, a.supplValidFrom ASC";

        Query queryDef = getEntityManager().createQuery(queryStringDef);
        queryDef.setParameter("year", pYear);
        queryDef.setParameter("type", pType);
        queryDef.setParameter("country", pCountryEn);

        tmpCatalogListTest = queryDef.getResultList();
        return tmpCatalogListTest;
    }
}
