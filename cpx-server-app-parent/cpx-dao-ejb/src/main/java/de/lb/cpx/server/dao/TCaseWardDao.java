/*
 * Copyright (c) 2017 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2017  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.dao;

import de.lb.cpx.model.TCaseWard;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

/**
 *
 * @author nandola
 */
@Stateless
public class TCaseWardDao extends AbstractCpxDao<TCaseWard> {

    /**
     * Creates a new instance.
     */
    public TCaseWardDao() {
        super(TCaseWard.class);
    }

    public List<TCaseWard> getWardsOfDept(long deptId) {
        List<TCaseWard> wards;
        TypedQuery<TCaseWard> query = getEntityManager().createQuery(String.format("from TCaseWard tw where tw.caseDepartment = %s", deptId), TCaseWard.class);
        wards = query.getResultList();
        Iterator<TCaseWard> it = wards.iterator();
        while (it.hasNext()) {
            TCaseWard ward = it.next();
            if (ward != null) {
                ward.getCaseIcds();
                ward.getCaseOpses();
            }
        }
        return wards;
    }

}
