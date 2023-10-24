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

import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.server.commonDB.model.CBaserate;
import de.lb.cpx.service.information.BaserateTypeEn;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Data access object for domain model class CDoctor. Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SuppressWarnings("unchecked")
public class CBaserateDao extends AbstractCommonDao<CBaserate> {

    private static final Logger LOG = Logger.getLogger(CBaserateDao.class.getName());

    /**
     * Creates a new instance.
     */
    public CBaserateDao() {
        super(CBaserate.class);
    }

    public boolean catalogExists(final String pChecksum) {
        return super.catalogExists("C_BASERATE", pChecksum);
    }

    public boolean catalogExists(final CountryEn pCountryEn) {
        return getEntryCounter(pCountryEn) > 0;
    }

    public int getEntryCounter(final CountryEn pCountryEn) {
        String query = "SELECT COUNT(*) FROM C_BASERATE "
                + " WHERE COUNTRY_EN = :country ";
        Query nativeQuery = getEntityManager().createNativeQuery(query);
        //nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        Number count = (Number) nativeQuery.getSingleResult();

        return (count == null) ? 0 : count.intValue();
    }

    public int dropEntries(/* final int pYear, */final CountryEn pCountryEn) {
        String query = "DELETE FROM C_BASERATE "
                + " WHERE COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        //nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        int deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }

    /**
     * Delete all entries in the table
     *
     * @return deleted rows
     */
    public int dropAllEntries() {
        String query = "DELETE FROM C_BASERATE ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        int deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }

    public List<CBaserate> getEntries(/* final int pYear, */final String pCountryEn) {
        Query query = getEntityManager().createQuery("from " + CBaserate.class.getSimpleName() + " b where b.countryEn = :country order by b.baseHosIdent, b.baseValidFrom, b.baseValidTo");
        //query.setParameter("year", pYear);
        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        List<CBaserate> list = query.getResultList();
        return list;
    }

    public List<CBaserate> getBaseratesCatalog(final String pCountryEn) {
        Query query = getEntityManager().createQuery("from " + CBaserate.class.getSimpleName() + " b JOIN FETCH b.items WHERE b.countryEn = :country order by b.baseHosIdent, b.baseValidFrom, b.baseValidTo");
        //query.setParameter("year", pYear);
        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        List<CBaserate> list = query.getResultList();
        return list;
    }

    /**
     * Get negotiated default DRG list
     *
     * @param pCountryEn country En
     * @param pStartDate start date for baserates
     * @return default list of DRGs
     */
    public List<CBaserate> getFullBaseratesCatalog(String pCountryEn, Date pStartDate) {
//        try {
        String queryStringDefault = "SELECT a FROM " + CBaserate.class.getSimpleName() + " a "
                + "WHERE a.countryEn = :country "
                + "AND a.baseValidFrom >= :startDate "
                + "ORDER BY a.baseHosIdent, a.baseValidFrom ASC";

        Query query = getEntityManager().createQuery(queryStringDefault);
        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        query.setParameter("startDate", pStartDate);

        List<CBaserate> baseratesCatalog = query.getResultList();
        flush();
        clear();
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't create baserates catalog", ex);
//        }
        return baseratesCatalog;
    }

    /**
     * Get base rates by hospital identifier
     *
     * @param pBaseHoIdent hospital identifier
     * @return list of baserates
     */
    public List<CBaserate> getBaseratesByBaseHoIdent(String pBaseHoIdent) {
        Query query = getEntityManager().createQuery("from " + CBaserate.class.getSimpleName() + " b where b.baseHosIdent = :baseHosIdent or b.baseHosIdent='000000000' order by b.baseHosIdent, b.baseValidFrom, b.baseValidTo");
        query.setParameter("baseHosIdent", pBaseHoIdent);
        List<CBaserate> list = query.getResultList();
        return list;
    }

//    /**
//     * update the baserate data
//     *
//     * @param pCBaserate baserate
//     * @return state of update process
//     */
//    public boolean updateBaserate(CBaserate pCBaserate) {
//        boolean checkUpdateState;
//        if (pCBaserate.getId() <= 0L) {
//            checkUpdateState = false;
//        } else {
//            merge(pCBaserate);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//
//    /**
//     * Delete baserate from database
//     *
//     * @param id baserate id
//     * @return boolean value for delete state
//     */
//    public boolean removeBaserateById(Long id) {
//        boolean isDeleted = false;
//        try {
//            isDeleted = deleteById(id);
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Cant't delete baserate", ex);
//        }
//        return isDeleted;
//    }
    /**
     * // * Get baserate by id // * // * @param baserateId baserate id //
     *
     *
     * @return searched baserate //
     */
//    public CBaserate getBaserateById(Long baserateId) {
//        List<CBaserate> lResults;
//        String queryName = "select u from " + CBaserate.class.getSimpleName() + " u where id =:idNbr";
//        final TypedQuery<CBaserate> query = getEntityManager().createQuery(queryName, CBaserate.class);
//        query.setParameter("idNbr", baserateId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
//
//    /**
//     * Add new baserate for hospital
//     *
//     * @param pCBaserate baserate
//     * @return id for new baserate
//     */
//    public long addNewBaserate(CBaserate pCBaserate) {
//        long baserateId = 0L;
//        if (pCBaserate != null) {
//            try {
//                persist(pCBaserate);
//                flush();
//                baserateId = pCBaserate.getId();
//
//            } catch (Exception ex) {
//                LOG.log(Level.SEVERE, "Can't add new baserate", ex);
//            }
//        }
//        return baserateId;
//    }
//    /**
//     * Import new baserates entries for hospital in database
//     *
//     * @param pCBaserateList list of baserates
//     * @return number of new baserate
//     */
//    public int importBaserate(List<CBaserate> pCBaserateList) {
//        int baseratesNumber = 0;
//        for (CBaserate baserate : pCBaserateList) {
//            persist(baserate);
//            baseratesNumber++;
//        }
//        return baseratesNumber;
//    }
    /**
     * Find baserate fee value
     *
     *
     * @param csHospitalIdent hospital ident
     * @param csdAdmissionDate admission date
     * @return searched baserate fee value
     */
    public Double findDrgBaserateFeeValue(String csHospitalIdent, Date csdAdmissionDate /*, String countryCode */) {
        return findBaserateFeeValue("70000000", csHospitalIdent, csdAdmissionDate /*, countryCode */);
//        if (csdAdmissionDate == null) {
//            LOG.log(Level.SEVERE, "Admission date is null, cannot determine baserate value for hospital ident " + csHospitalIdent + "!");
//            return 0.0d;
//        }
//
////        final long startTime = System.currentTimeMillis();
//        List<CBaserate> list = null;
//        Query query = getEntityManager().createQuery("from " + CBaserate.class.getSimpleName()
//                + " b  WHERE b.countryEn = :country and b.baseFeeKey = :feeKey"
//                + " and b.baseHosIdent = :hosIdent "
//                + " and (b.baseValidFrom <= :validFrom and b.baseValidTo >= :validFrom "
//                + " or b.baseValidFrom<=:validTo and b.baseValidTo>=:validTo)");
//
//        query.setParameter("country", CountryEn.de);
//        query.setParameter("feeKey", "70000000");
//        query.setParameter("hosIdent", csHospitalIdent);
//        query.setParameter("validFrom", csdAdmissionDate);
//        query.setParameter("validTo", csdAdmissionDate);
//
//        list = query.getResultList();
//        if (list.isEmpty()) {
//            LOG.log(Level.FINE, "No baserate entry found in table for hospital ident " + csHospitalIdent + " and admission date " + csdAdmissionDate + "!");
//            return 0.0d;
//        }
//        CBaserate br = list.get(0);
//        Double feeValue = br.getBaseFeeValue() == null ? 0.0 : br.getBaseFeeValue();
////        LOG.log(Level.INFO, "findDrgBaserateFeeValue: " + (System.currentTimeMillis() - startTime) + " ms");
////        LOG.log(Level.INFO, "csHospitalIdent = " + csHospitalIdent);
////        LOG.log(Level.INFO, "csdAdmissionDate = " + csdAdmissionDate);
////        LOG.log(Level.INFO, "csdAdmissionDate = " + csdAdmissionDate);
//        return feeValue;
    }

    public Double findCareBaserateFeeValue(String csHospitalIdent, Date csdAdmissionDate /*, String countryCode */) {
        return findBaserateFeeValue("74000000", csHospitalIdent, csdAdmissionDate /*, countryCode */);
    }

    public Double findBaserateFeeValue(String pFeeKey, String csHospitalIdent, Date csdAdmissionDate /*, String countryCode */) {
        if (csdAdmissionDate == null) {
            LOG.log(Level.SEVERE, "Admission date is null, cannot determine baserate value for hospital ident {0}!", csHospitalIdent);
            return 0.0d;
        }

//        final long startTime = System.currentTimeMillis();
        Query query = getEntityManager().createQuery("from " + CBaserate.class.getSimpleName()
                + " b  WHERE b.countryEn = :country and b.baseFeeKey = :feeKey"
                + " and b.baseHosIdent = :hosIdent "
                + " and (b.baseValidFrom <= :validFrom and b.baseValidTo >= :validFrom "
                + " or b.baseValidFrom<=:validTo and b.baseValidTo>=:validTo)");

        query.setParameter("country", CountryEn.de);
        query.setParameter("feeKey", pFeeKey);
        query.setParameter("hosIdent", csHospitalIdent);
        query.setParameter("validFrom", csdAdmissionDate);
        query.setParameter("validTo", csdAdmissionDate);

        List<CBaserate> list = query.getResultList();
        if (list.isEmpty()) {
            LOG.log(Level.FINE, "No baserate entry found in table for hospital ident {0} and admission date {1}!", new Object[]{csHospitalIdent, csdAdmissionDate});
            return 0.0D;
        }
        CBaserate br = list.get(0);
        final Double val = br.getBaseFeeValue();
        Double feeValue = val == null ? 0.0D : val;
//        LOG.log(Level.INFO, "findDrgBaserateFeeValue: " + (System.currentTimeMillis() - startTime) + " ms");
//        LOG.log(Level.INFO, "csHospitalIdent = " + csHospitalIdent);
//        LOG.log(Level.INFO, "csdAdmissionDate = " + csdAdmissionDate);
//        LOG.log(Level.INFO, "csdAdmissionDate = " + csdAdmissionDate);
        return feeValue;
    }

    public List<CBaserate> findBaserateFeeValue(CaseTypeEn caseType, String csHospitalIdent, Date csdAdmissionDate, Date csdDischargeDate, String countryCode) {
        if (csdAdmissionDate == null) {
            LOG.log(Level.SEVERE, "Admission date is null, cannot determine baserate value for hospital ident {0}!", csHospitalIdent);
            //return 0.0d;
            return new ArrayList<>();
        }

        final long startTime = System.currentTimeMillis();
        Query query = getEntityManager().createQuery("from " + CBaserate.class.getSimpleName()
                + " b  WHERE b.countryEn = :country and b.baseFeeKey = :feeKey"
                + " and b.baseHosIdent = :hosIdent "
                + " and (b.baseValidFrom <= :validFrom and b.baseValidTo >= :validFrom "
                + " or b.baseValidFrom<=:validTo and b.baseValidTo>=:validTo)");

        query.setParameter("country", CountryEn.valueOf(countryCode));
        if (null == caseType) {
            query.setParameter("feeKey", "");
        } else {
            switch (caseType) {
                case DRG:
                    query.setParameter("feeKey", "70000000");
                    break;
                case PEPP:
                    query.setParameter("feeKey", "C1000000");
                    break;
                default:
                    query.setParameter("feeKey", "");
                    break;
            }
        }
        query.setParameter("hosIdent", csHospitalIdent);
        query.setParameter("validFrom", csdAdmissionDate);
        query.setParameter("validTo", csdDischargeDate);

        List<CBaserate> list = query.getResultList();
        if (list.isEmpty()) {
            LOG.log(Level.FINE, "No baserate entry found in table for hospital ident {0} and admission date {1}!", new Object[]{csHospitalIdent, csdAdmissionDate});
            //return 0.0d;
            return new ArrayList<>();
        }
        //CBaserate br = list.get(0);
        //Double feeValue = br.getBaseFeeValue() == null ? 0.0 : br.getBaseFeeValue();
        LOG.log(Level.INFO, "findDrgBaserateFeeValue: {0} ms", System.currentTimeMillis() - startTime);
        LOG.log(Level.INFO, "csHospitalIdent = {0}", csHospitalIdent);
        LOG.log(Level.INFO, "csdAdmissionDate = {0}", csdAdmissionDate);
        LOG.log(Level.INFO, "csdAdmissionDate = {0}", csdAdmissionDate);
        return list;
    }

    /**
     * finds the list of baserates for one hospital case
     *
     * @param csHospitalIdent hospital
     * @param pCountryEn country
     * @param csdAdmissionDate case admission date
     * @param csdDischargeDate case discharge date
     * @return the list
     */
    public List<CBaserate> findPeppBaserates4Case(String csHospitalIdent, String pCountryEn,
            Date csdAdmissionDate, Date csdDischargeDate) {
        final long startTime = System.currentTimeMillis();
        Query query = getEntityManager().createQuery("from " + CBaserate.class.getSimpleName()
                + " b  WHERE b.countryEn = :country and b.baseFeeKey = :peppFeeKey"
                + " and b.baseHosIdent = :hosIdent "
                + " and (b.baseValidFrom <= :validFrom and b.baseValidTo >= :validFrom "
                + " or b.baseValidFrom<=:validTo and b.baseValidTo>=:validTo)"
                + " order by b.baseValidFrom ");

        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        query.setParameter("peppFeeKey", "C1000000");
        query.setParameter("hosIdent", csHospitalIdent);
        query.setParameter("validFrom", csdAdmissionDate);
        query.setParameter("validTo", csdDischargeDate);

        List<CBaserate> list = query.getResultList();
        LOG.log(Level.INFO, "findPeppBaserates4Case: {0} ms", System.currentTimeMillis() - startTime);
        return list;
    }

    /**
     * finds default value for drg baserate it is not determined how it will be
     * handled, that's why returns a value
     *
     * @return 3000.0d
     */
    public Double getDefaultDrgBaserate() {
        return 3000.0d;
    }

    /**
     * finds default value for drg baserate it is not determined how it will be
     * handled, that's why returns a value
     *
     * @param admissionReason12 admission reason 12
     * @return default value
     */
    public Double getDefaultPeppBaserate(int admissionReason12) {
        //CPX-1438 DefaultPeppBaserate=0.0d
//        if (admissionReason12 == AdmissionReasonEn.ar03.getIdInt()) {
//            return 190.0d;
//        } else if (admissionReason12 == AdmissionReasonEn.ar04.getIdInt()) {
        return 0.0d;
//        }
//        return 250.0d;
    }

    public List<CBaserate> findBaserate4CaseType(String feeKey, String csHospitalIdent, String countryCode) {
        final long startTime = System.currentTimeMillis();
        Query query = getEntityManager().createQuery("from " + CBaserate.class.getSimpleName()
                + " b  WHERE b.countryEn = :country and b.baseFeeKey = :feeKey"
                + " and b.baseHosIdent = :hosIdent "
                + "ORDER BY  b.baseValidFrom ASC");

        query.setParameter("country", CountryEn.valueOf(countryCode));
        query.setParameter("feeKey", feeKey);
        query.setParameter("hosIdent", csHospitalIdent);

        List<CBaserate> list = query.getResultList();
        if (list.isEmpty()) {
            LOG.log(Level.FINE, "No baserate entry found in table for hospital ident {0}!", csHospitalIdent);
            //return 0.0d;
            return new ArrayList<>();
        }
        //CBaserate br = list.get(0);
        //Double feeValue = br.getBaseFeeValue() == null ? 0.0 : br.getBaseFeeValue();
        LOG.log(Level.INFO, "findDrgBaserateFeeValue: {0} ms", System.currentTimeMillis() - startTime);
        LOG.log(Level.INFO, "csHospitalIdent = {0}", csHospitalIdent);

        return list;
    }

    public double getDefaultCareBaserate() {
//        if((LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(admDate)).getYear()) <2020){
//            return 0;
//        }

        return 146.55D;
    }

    public double getDefaultDrgBaserate(BaserateTypeEn pType, Date pAdmDate) {
        LOG.log(Level.FINEST, "find default baserate for admission date {0}...", pAdmDate);
        if (pType.equals(BaserateTypeEn.DRG)) {
            return this.getDefaultDrgBaserate();
        }
        if (pType.equals(BaserateTypeEn.DRG_CARE)) {
            //return this.getDefaultCareBaserate(admDate);
            return getDefaultCareBaserate();
        }
        return 0;
    }

}
