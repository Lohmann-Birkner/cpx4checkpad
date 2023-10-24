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

import de.lb.cpx.server.commonDB.model.CMdkNegotiableAuditquota;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author hasse
 */
@Stateless
@SuppressWarnings("unchecked")
public class CMdkNegotiableAuditquotaDao extends AbstractCommonDao<CMdkNegotiableAuditquota> {

    /**
     * Creates a new instance.
     */
    public CMdkNegotiableAuditquotaDao() {
        super(CMdkNegotiableAuditquota.class);
    }

    public CMdkNegotiableAuditquota getMdkNegotiableAuditQuota(final String pInsuranceIdent) {

        Query query = getEntityManager().createQuery("from " + CMdkNegotiableAuditquota.class.getSimpleName() + " b where b.mdkNaqInscIdent = :inscIdent");
        query.setParameter("inscIdent", pInsuranceIdent);
        return getSingleResultOrNull(query);
    }
    
    public List<CMdkNegotiableAuditquota> getAllMdkNegotiableAuditQuotaEntries() {
        List<CMdkNegotiableAuditquota> list = null;
        Query query = getEntityManager().createQuery("from " + CMdkNegotiableAuditquota.class.getSimpleName() + " a order by a.mdkNaqInscIdent");
        list = query.getResultList();
        return list;
    }

}
