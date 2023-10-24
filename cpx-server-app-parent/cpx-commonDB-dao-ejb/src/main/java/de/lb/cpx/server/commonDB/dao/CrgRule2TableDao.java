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

import de.lb.cpx.server.commonDB.model.rules.CrgRule2Table;
import java.util.List;
import javax.persistence.Query;

/**
 * Data access object for domain model class CrgRule2Table.Initially generated
 * at 02.02.2016 17:27:35 by Hibernate Tools 3.2.2.GA.
 *
 * @author Hibernate Tools
 * @param <E> implementation of the CrgRule2Table base entity
 */
public abstract class CrgRule2TableDao<E extends CrgRule2Table> extends AbstractCommonDao<E> {

    /**
     * Creates a new instance.
     *
     * @param entityClass class of the entity
     */
    public CrgRule2TableDao(Class<E> entityClass) {
        super(entityClass);
    }

    /**
     * remove all references to Tables for PRuleId
     *
     * @param pRuleId rule id
     */
    public void remove4ruleId(long pRuleId) {
//        String qry = "DELETE FROM " + getTableName() + " r where r.CRGR_ID =:pRuleId";
        //Awi: remove alias for mssql
        String qry = "DELETE FROM " + getTableName() + " where CRGR_ID =:pRuleId";
        Query nativeQuery = getEntityManager().createNativeQuery(qry);
        nativeQuery.setParameter("pRuleId", pRuleId);
        nativeQuery.executeUpdate();
    }

    protected abstract String getTableName();

    public void remove4ruleIds(List<Long> pRuleIds) {
        String sql = "delete from " + getEntityName() + "  WHERE CRGR_ID IN (:pRuleIds)";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("pRuleIds", pRuleIds);
        query.executeUpdate();
    }

    protected abstract String getRfCode4Rule();

}
