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
 *    2020  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.wm.dao;

import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.server.dao.AbstractCpxDao;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Data access object for domain model class TPatient. Initially generated at
 * 21.01.2016 17:14:39 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 */
@Stateless
@SuppressWarnings("unchecked")
public class TWmRiskDetailsDao extends AbstractCpxDao<TWmRiskDetails> {

    /**
     * Creates a new instance.
     */
    public TWmRiskDetailsDao() {
        super(TWmRiskDetails.class);
    }

    public void remove4Ids(List<Long> pIds) {
        String sql = "delete from  " + getEntityName() + "  WHERE ID IN (:pIds)";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("pIds", pIds);
        query.executeUpdate();
    }

    public List<TWmRiskDetails> findRisks4ActualVersionType(long caseId, VersionRiskTypeEn pType) {
        final String sql = "select det.* from t_wm_risk_details det "
                + "inner join t_wm_risk rr on rr.id = det.T_WM_RISK_ID "
                + "inner join t_case_details td on td.id = rr.T_CASE_DETAILS_ID " +
                "where td.CSD_VERS_RISK_TYPE_EN = '" + pType.name() + "' and rr.RISK_ACTUAL_4_REG = 1"
                + " and td.LOCAL_FL = 1 and td.T_CASE_ID = " + String.valueOf(caseId);
        final Query query = getSession().createNativeQuery(sql, TWmRiskDetails.class);
        
        return query.getResultList();    
    }


}
