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
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commons.dao.AbstractDao;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.server.commons.dao.CommonEntityManager;
import de.lb.cpx.server.config.CpxServerConfig;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@SuppressWarnings("unchecked")
public abstract class AbstractCommonDao<E extends AbstractEntity> extends AbstractDao<E> {

    //@PersistenceContext(unitName = "cpx-commonDB")
    //private EntityManager entityManager;
    @Inject
    @CommonEntityManager
    private EntityManager entityManager;

    protected AbstractCommonDao(final Class<E> entityClass) {
        super(entityClass);
    }

    public static void fillUser(final Object transientInstance) {
        if (transientInstance == null) {
            return;
        }
        if (!(transientInstance instanceof AbstractEntity)) {
            return;
        }
        AbstractEntity instance = (AbstractEntity) transientInstance;
        Long creationUser = instance.getCreationUser();
        //String modificationUser = instance.getModificationUser();
        //creationUser = (creationUser == null)?"":creationUser.trim();
        //modificationUser = (modificationUser == null)?"":modificationUser.trim();
        boolean isNew = (instance.id == 0L);
//
//        if (isNew && creationUser != null) {
//            //New entry, but creation user was obviously set by hand
//            return;
//        }

        Date now = new Date();

        if (isNew) {
            //INSERT NEW ENTITY
            if (instance.getCreationDate() == null) {
                instance.setCreationDate(now);
            }
        } else {
            //UPDATE EXISTING ENTITY
            instance.setModificationDate(now);
        }
        
        if(creationUser != null && isNew){
            return;
        }

        if (!ClientManager.isCdiAvailable()) {
            //Cannot process without CDI (Weld), so creation user and modification user remain empty
            return;
        }

        String clientId = ClientManager.getCurrentCpxClientId();
        Long userId; //ID in CDB_USERS
        if (clientId == null || clientId.trim().isEmpty()) {
            //entry is not created or updated by a real user
            userId = null; //system
        } else {
            userId = ClientManager.getCurrentCpxUserId();
        }

        if (userId != null) {
            if (isNew) {
                //INSERT NEW ENTITY
                if (instance.getCreationUser() == null) {
                    instance.setCreationUser(userId);
                }
                //      if (instance.getCreationDate() == null) {
                //        instance.setCreationDate(now);
                //      }
            } else {
                //UPDATE EXISTING ENTITY
                instance.setModificationUser(userId);
                //      instance.setModificationDate(now);
            }
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void persist(final E transientInstance) {
        AbstractCommonDao.fillUser(transientInstance);
        super.persist(transientInstance);
    }

    @Override
    public E merge(final E detachedInstance) {
        AbstractCommonDao.fillUser(detachedInstance);
        return super.merge(detachedInstance);
    }

    public boolean catalogExists(final String pTable, final String pChecksum) {
        String query = String.format("SELECT 1 FROM %s WHERE EXISTS (SELECT 1 FROM %s WHERE CHECKSUM = :checksum) ", pTable, pTable);
        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setMaxResults(1);
        //nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("checksum", pChecksum);
        List<Number> resultList = nativeQuery.getResultList();
        if (resultList == null || resultList.isEmpty()) {
            return false;
        }
        Number count = resultList.get(0);

        if (count == null) {
            return false;
        }
        return (count.intValue() > 0);
    }

    public boolean catalogExists(final String pTable, final String pChecksum, final CountryEn pCountryEn) {
        String query = String.format("SELECT 1 FROM %s WHERE EXISTS (SELECT 1 FROM %s WHERE CHECKSUM = :checksum and COUNTRY_EN = :country) ",
                pTable, pTable);
        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setMaxResults(1);
        //nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("checksum", pChecksum);
        nativeQuery.setParameter("country", pCountryEn.name());
        List<Number> resultList = nativeQuery.getResultList();
        if (resultList == null || resultList.isEmpty()) {
            return false;
        }
        Number count = resultList.get(0);

        if (count == null) {
            return false;
        }
        return (count.intValue() > 0);
    }

    @Override
    public String getConnectionString() {
        return CpxServerConfig.COMMONDB;
    }
    public static String getIk2DrgSqlString( Map<String, List<String>> importIkzList, String hosIdentColName, String codeColName){
        Set<String> iks = importIkzList.keySet();
        StringBuilder ret = new StringBuilder();
        int i = 0;
        for(String ik:iks){
            i++;
            if(i > 1){
                ret.append(" OR ");
            }
            List<String> drgs = importIkzList.get(ik);
            if(drgs != null && !drgs.isEmpty()){
                ret.append(" ").append(hosIdentColName).append(" = '").append(ik)
                        .append("' AND ").append(codeColName).append(" in ('").append(String.join("', '",drgs)).append("')");
                
            }
            
        }
        String retStr = ret.toString();
        if (i > 1){
            retStr = "(" + retStr + ")";
        }
        return retStr;
    }

}
