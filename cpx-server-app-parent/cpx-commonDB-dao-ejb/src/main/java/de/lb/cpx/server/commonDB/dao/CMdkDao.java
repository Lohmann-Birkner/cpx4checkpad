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
import de.lb.cpx.server.commonDB.model.CMdk;
import de.lb.cpx.server.commonDB.model.CMdk_;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Data access object for domain model class CMdk. Initially generated at
 * 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools, sklarow
 */
@Stateless
@SuppressWarnings("unchecked")
public class CMdkDao extends AbstractInternalIdDao<CMdk> {

    /**
     * Creates a new instance.
     */
    public CMdkDao() {
        super(CMdk.class);
    }

    public boolean catalogExists(final String pChecksum) {
        return super.catalogExists("C_MDK", pChecksum);
    }

    public boolean catalogExists(final CountryEn pCountryEn) {
        return getEntryCounter(pCountryEn) > 0;
    }

    public int getEntryCounter(final CountryEn pCountryEn) {
        String query = "SELECT COUNT(*) FROM C_MDK "
                + " WHERE COUNTRY_EN = :country ";
        Query nativeQuery = getEntityManager().createNativeQuery(query);
        //nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        Number count = (Number) nativeQuery.getSingleResult();

        return (count == null) ? 0 : count.intValue();
    }

    public int dropEntries(/* final int pYear, */final CountryEn pCountryEn) {
        int deleteCount = 0;
        String query = "DELETE FROM C_MDK "
                + " WHERE COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        //nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }

    public List<CMdk> getEntries(/* final int pYear, */final String pCountryEn) {
        List<CMdk> list;
        Query query = getEntityManager().createQuery("from " + CMdk.class.getSimpleName() + " a where a.countryEn = :country order by a.mdkName");
        //query.setParameter("year", pYear);
        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        list = query.getResultList();
        return list;
    }

    /**
     * Get all entries of MDK master data
     *
     * @param pCountryEn name of country
     * @return list of MDK master data
     */
    public List<CMdk> getMdkEntries(final String pCountryEn) {
        List<CMdk> list;
        Query query = getEntityManager().createQuery("from " + CMdk.class.getSimpleName()
                + " a where a.countryEn = :country order by a.mdkName, a.mdkCity, a.mdkStreet");
        //query.setParameter("year", pYear);
        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        list = query.getResultList();
        return list;
    }

//    /**
//     * Update the MDK master data
//     *
//     * @param pCMdk mdk master data item
//     * @return state of update process
//     */
//    public boolean updateMdkItem(CMdk pCMdk) {
//        boolean checkUpdateState;
//        if (pCMdk.getId() <= 0L) {
//            checkUpdateState = false;
//        } else {
//            merge(pCMdk);
//            checkUpdateState = true;
//        }
//        return checkUpdateState;
//    }
//
//    /**
//     * Delete MDK master data item by id
//     *
//     * @param id mdk item id
//     * @return boolean value for delete state
//     */
//    public boolean removeMdkItemById(Long id) {
//        deleteById(id);
//        boolean isDeleted = true;
//        return isDeleted;
//    }
//    /**
//     * Get MDK master data item by id
//     *
//     * @param pMdkItemId mdk item id
//     * @return searched mdk item
//     */
//    public CMdk getMdkItemById(Long pMdkItemId) {
//        List<CMdk> lResults;
//        String queryName = "select u from " + CMdk.class.getSimpleName() + " u where id =:idNbr";
//        Query query = getEntityManager().createQuery(queryName);
//        query.setParameter("idNbr", pMdkItemId);
//        lResults = query.getResultList();
//        if (lResults == null || lResults.isEmpty()) {
//            return null;
//        }
//        return lResults.get(0);
//    }
//    /**
//     * Add new MDK master data item
//     *
//     * @param pCMdk master data item
//     * @return id for new mdk item
//     */
//    public long addNewMdkItem(CMdk pCMdk) {
//        return addNewItem(pCMdk);
////        persist(pCMdk);
////        flush();
////        long id = pCMdk.getId();
////        return id;
//    }
//    /**
//     * Import new MDK master data entries
//     *
//     * @param pCMdk list of mdk master data
//     * @return sum of imported mdk items
//     */
//    public int importMdkMasterData(List<CMdk> pCMdk) {
//        int mdkItemNumber = 0;
//        for (CMdk mdkItem : pCMdk) {
//            persist(mdkItem);
//            mdkItemNumber++;
//        }
//        return mdkItemNumber;
//    }
    /**
     * Get last mdk ident of user definded mdk
     *
     * @return sum of imported mdk items
     */
    public Long getLastUserDefinedMDK() {
        long lastIdentMdkOfUser;
        List<Long> lResults;
        String queryName = "SELECT MAX(mdkInternalId) FROM " + CMdk.class.getSimpleName() + " u WHERE u.userDefinedEntry = 1";
        Query query = getEntityManager().createQuery(queryName);
        lResults = query.getResultList();
        if (!lResults.isEmpty() && lResults.get(0) != null) {
            lastIdentMdkOfUser = lResults.get(0);
        } else {
            lastIdentMdkOfUser = 0L;
        }
        return lastIdentMdkOfUser;
    }

    /**
     * Delete all centrally based MDKs in the table
     *
     * @return deleted rows
     */
    public int dropCentrallyBasedMdks() {
        int deleteCount;
        String query = "DELETE FROM C_MDK WHERE USER_DEFINED_ENTRY=0";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }

//    /**
//     * Import new MDKs entries for hospital in database
//     *
//     * @param pCMdkList list of MDKs
//     * @return number of new MDKs
//     */
//    public int importMdkList(List<CMdk> pCMdkList) {
//        int baseratesNumber;
//        mergeList(pCMdkList);
//        baseratesNumber = pCMdkList.size();
//        return baseratesNumber;
//    }
    @Override
    public String getInternalIdSequence() {
        return "C_MDK_INTERNAL_ID_SQ";
    }

    @Override
    public SingularAttribute<CMdk, Long> getInternalIdField() {
        return CMdk_.mdkInternalId;
    }

//    public Map<Long, CMdk> getMdks() {
//        return getMenuCacheItems();
//    }
//
//    @Override
//    public Map<Long, CMdk> getMenuCacheItems() {
//        return MenuCacheEntity.toMap(findAll());
//    }
//    /**
//     * Get sequence for internal id of mdk
//     *
//     * @return sequence id
//     */
//    public Integer getNextMdkInternalId() {
//        Integer intrenalIdSequence = 0;
//        try {
//            if (isOracle()) {
//                String queryName = "SELECT C_MDK_INTERNAL_ID_SQ.NEXTVAL FROM DUAL";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Integer.valueOf(query.getResultList().get(0).toString());
//            } else if (isSqlSrv()) {
//                String queryName = "SELECT NEXT VALUE FOR C_MDK_INTERNAL_ID_SQ";
//                Query query = getEntityManager().createNativeQuery(queryName);
//                intrenalIdSequence = Integer.valueOf(query.getResultList().get(0).toString());
////                if (intrenalIdSequence != 0) {
////                    String isertQueryName= "INSERT INTO dbo.C_MDK_INTERNAL_ID_SQ ([next_val]) VALUES (:nextValue)";
////                   javax.persistence.Query qry = getEntityManager().createNativeQuery(isertQueryName).setParameter("nextValue", intrenalIdSequence+1);
////                   boolean ret = (qry.executeUpdate() == 1);
////                }
//            }
//        } catch (NumberFormatException ex) {
//            LOG.log(Level.SEVERE, "Can't get sequence for internal id of mdk", ex);
//        }
//        return intrenalIdSequence;
//    }
    public Map<Long, CMdk> getAllMdks() {
        Map<Long, CMdk> map = new LinkedHashMap<>();
//        List<CWmListReminderSubject> reminders = reminderSubjectDao.findAll();
        List<CMdk> mdks = findAll();
        //Collections.sort(reminders);
        for (CMdk mdk : mdks) {
            map.put(mdk.getMdkInternalId(), mdk);
        }
        return map;
    }
}
