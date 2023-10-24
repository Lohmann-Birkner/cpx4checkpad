/*
 * Copyright (c) 2022 Lohmann & Birkner.
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
 *    2022  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.model.enums.CountryEn;
import de.lb.cpx.server.commonDB.model.COpsAop;
import de.lb.cpx.server.commonDB.model.COpsCatalog;
import de.lb.cpx.server.commons.dao.AbstractCatalogEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author gerschmann
 */
@Stateless
@SuppressWarnings("unchecked")
public class COpsAopDao extends  AbstractCommonDao<COpsAop> {

    public COpsAopDao(){
        super(COpsAop.class);
    }

    public int dropEntries(final int pYear, final CountryEn pCountryEn) {
        int deleteCount = 0;
        String query = "DELETE FROM C_OPS_AOP "
                + " WHERE OPS_YEAR = :year "
                + "   AND COUNTRY_EN = :country ";

        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        deleteCount = nativeQuery.executeUpdate();

        return deleteCount;
    }
 
    public boolean catalogExists(final int pYear, final CountryEn pCountryEn) {
        return getEntryCounter(pYear, pCountryEn) > 0;
    }

    public int getEntryCounter(final int pYear, final CountryEn pCountryEn) {
        String query = "SELECT COUNT(*) FROM C_OPS_AOP "
                + " WHERE OPS_YEAR = :year "
                + "   AND COUNTRY_EN = :country ";
        Query nativeQuery = getEntityManager().createNativeQuery(query);
        nativeQuery.setParameter("year", pYear);
        nativeQuery.setParameter("country", pCountryEn.name());
        Number count = (Number) nativeQuery.getSingleResult();

        return (count == null) ? 0 : count.intValue();
    }

    public List<? extends AbstractCatalogEntity> getEntries(int pYear, String pCountryEn) {
        List<COpsAop> list = null;
        Query query = getEntityManager().createQuery("from " + COpsAop.class.getSimpleName() + " a where a.opsYear = :year and a.countryEn = :country order by  a.opsCode");
        query.setParameter("year", pYear);
        query.setParameter("country", CountryEn.valueOf(pCountryEn));
        list = query.getResultList();
        return list;
    }

}
