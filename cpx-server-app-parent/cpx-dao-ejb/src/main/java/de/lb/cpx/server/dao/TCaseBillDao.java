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
package de.lb.cpx.server.dao;

import de.lb.cpx.model.TCaseBill;
import de.lb.cpx.model.TCaseBill_;
import de.lb.cpx.model.TCaseFee;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Hibernate;

/**
 *
 * @author gerschmann
 */
public class TCaseBillDao extends AbstractCpxDao<TCaseBill> {

    /**
     * Creates a new instance.
     */
    public TCaseBillDao() {
        super(TCaseBill.class);
    }

    /**
     * @param pDetailsId case details id
     * @return list of all case fees for the case details
     */
    public List<TCaseBill> findTCaseFees4Details(long pDetailsId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TCaseBill> query = criteriaBuilder.createQuery(TCaseBill.class);
        Root<TCaseBill> from = query.from(TCaseBill.class);
        query.where(criteriaBuilder.equal(from.get(TCaseBill_.caseDetails), pDetailsId));
        List<TCaseBill> result = getEntityManager().createQuery(query).getResultList();
        if(result == null || result.isEmpty()){
            return new ArrayList<>();
        }
        for(TCaseBill bill: result){
            Set<TCaseFee> fees = bill.getCaseFees();
            if(fees == null || fees.isEmpty()){
                continue;
            }
            Hibernate.initialize(fees);
            for(TCaseFee fee: fees){
                Hibernate.initialize(fee);
            }
        }
        return result;
    }
}