/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.server.commonDB.model.rules.CTransferCatalog;
import java.util.List;
import java.util.TreeSet;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author gerschmann
 * @param <E>
 */
@SuppressWarnings("unchecked")
public abstract class CTransferCatalogDao <E extends CTransferCatalog> extends AbstractCommonDao<E>{
 
    /**
     * Creates a new instance.
     *
     * @param entityClass that extends CrgRulePools
     */
    public CTransferCatalogDao(Class<E> entityClass) {
        super(entityClass);
    }
    
    public boolean catalogExists(final int pSrcYear, final int pDestYear){
        return getEntryCounter(pSrcYear, pDestYear) > 0;
    }

    public boolean catalogExists(final String pChecksum) {
        return super.catalogExists(getTableName(), pChecksum);
    }

    public int dropEntries(int pSrcYear, int pDestYear){
        String qry = "DELETE FROM " + getTableName() + " WHERE SRC_YEAR = :pSrcYear AND DEST_YEAR = :pDestYear" ;
        Query nativeQuery = getEntityManager().createNativeQuery(qry);
        nativeQuery.setParameter("pSrcYear", pSrcYear);
        nativeQuery.setParameter("pDestYear", pDestYear);
        int deleteCount = nativeQuery.executeUpdate();
        return deleteCount;
    }

    private int getEntryCounter(int pSrcYear, int pDestYear) {
        String qry = "SELECT COUNT(*) FROM " + getTableName()+ " WHERE SRC_YEAR = :pSrcYear AND DEST_YEAR = :pDestYear" ;
        Query nativeQuery = getEntityManager().createNativeQuery(qry);
        nativeQuery.setParameter("pSrcYear", pSrcYear);
        nativeQuery.setParameter("pDestYear", pDestYear);
        Number count = (Number) nativeQuery.getSingleResult();

        return (count == null) ? 0 : count.intValue();
        
    }
    
    protected abstract String getTableName();

    public TreeSet<String> getValidCodes4Year(int pDestYear) {
        String qry = "SELECT DEST_CODE FROM " + getTableName()+ " WHERE DEST_YEAR = :pDestYear" ;
        Query nativeQuery = getEntityManager().createNativeQuery(qry);
        nativeQuery.setParameter("pDestYear", pDestYear);
        List <String> retList = nativeQuery.getResultList();
        if(retList == null){
            return new TreeSet<String>();
        }else{
            return new TreeSet<>(retList);
        }
    }

    public TreeSet<E> getTransferCatalog4Years(int pSrcYear, int pDestYear) {

        TypedQuery<E> query = getEntityManager().createQuery("from " + getEntityName()
                + " r  WHERE r.srcYear =:pSrcYear and r.destYear =:pDestYear", getEntityClass());

        query.setParameter("pSrcYear", pSrcYear);
        query.setParameter("pDestYear", pDestYear);
        List<E> list = query.getResultList();
        if(list == null){
            return new TreeSet<>();
        }
        return new TreeSet<>(list);

    }

}
