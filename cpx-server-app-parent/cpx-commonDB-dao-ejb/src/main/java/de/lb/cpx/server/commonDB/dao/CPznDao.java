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
import de.lb.cpx.server.commonDB.model.CPzn;
import java.util.List;
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
public class CPznDao extends AbstractCommonDao<CPzn> {

    /**
     * Creates a new instance.
     */
    public CPznDao() {
        super(CPzn.class);
    }

    public boolean catalogExists(final String pChecksum) {
        return super.catalogExists("C_PZN", pChecksum);
    }

    public boolean catalogExists(final CountryEn pCountryEn) {
        return getEntryCounter(pCountryEn) > 0;
    }

    public boolean catalogExists(final String pChecksum, final CountryEn pCountryEn) {
        return super.catalogExists("C_PZN", pChecksum, pCountryEn);
    }
    
    public int getEntryCounter(final CountryEn pCountryEn) {
        String query = "SELECT COUNT(*) FROM C_PZN "
                + " WHERE COUNTRY_EN = :country ";
        Query nativeQuery = getEntityManager().createNativeQuery(query);
        //nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        Number count = (Number) nativeQuery.getSingleResult();

        return (count == null) ? 0 : count.intValue();
    }

    public int dropEntries(/* final int pYear, */final CountryEn pCountryEn) {
        int deleteCount = 0;
        String query = "DELETE FROM C_PZN "
                + " WHERE COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        //nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }

    public List<CPzn> getEntries(/* final int pYear, */final String pCountryEn) {
        List<CPzn> list = null;
        Query query = getEntityManager().createQuery("from " + CPzn.class.getSimpleName() + " a where a.countryEn = :country order by a.pznIdent");
        //query.setParameter("year", pYear);
        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        list = query.getResultList();
        return list;
    }

}
