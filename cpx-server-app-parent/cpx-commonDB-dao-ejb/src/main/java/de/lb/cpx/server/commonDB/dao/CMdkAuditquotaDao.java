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
 *    2018  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.dao;

import de.lb.cpx.server.commonDB.model.CMdkAuditquota;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author shahin
 */
@Stateless
@SuppressWarnings("unchecked")
public class CMdkAuditquotaDao extends AbstractCommonDao<CMdkAuditquota> {

    /**
     * Creates a new instance.
     */
    public CMdkAuditquotaDao() {
        super(CMdkAuditquota.class);
    }

//    public Map<String, CMdkAuditquota> getMdkQuoatas() {
//        final Map<String, CMdkAuditquota> result = new HashMap<>();
//        for(CMdkAuditquota quota: findAll()) {
//            result.put(quota.getMdkAqHosIdent(), quota);
//        }
//        return result;
//    }
    public CMdkAuditquota getMdkAuditQuota(final String pHospitalIdent, final int pYear, final int pQuarter) {
        if (pYear < 2020) {
            return null;
        }
        Query query = getEntityManager().createQuery("from " + CMdkAuditquota.class.getSimpleName() + " b where b.mdkAqHosIdent = :hosIdent and b.mdkAqYear = :year and b.mdkAqQuarter = :quarter");
        query.setParameter("hosIdent", pHospitalIdent);
        query.setParameter("year", pYear);
        query.setParameter("quarter", pQuarter);
        return getSingleResultOrNull(query);
    }
    
    public List<CMdkAuditquota> getAllMdkAuditQuotaEntries() {
        List<CMdkAuditquota> list = null;
        Query query = getEntityManager().createQuery("from " + CMdkAuditquota.class.getSimpleName() + " a order by a.mdkAqHosIdent");
        list = query.getResultList();
        return list;
    }

}
