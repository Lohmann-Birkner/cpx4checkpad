/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.model.TBatchResult;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.dao.TBatchResultDao;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author wilde
 */
@Stateless
public class BatchResultEJB implements BatchResultEJBRemote {

    private static final long serialVersionUID = 1L;

    @EJB
    private TBatchResultDao batchGroupingDao;

    @Override
    public TBatchResult findBatchResults(GDRGModel grouperModel, boolean isLocal) {
        return batchGroupingDao.findResult2ModelAndLocalFlag(grouperModel, isLocal);
    }

    @Override
    public TBatchResult findBatchResultForRole(GDRGModel grouperModel, Boolean isLocal, long roleId) {
        return batchGroupingDao.findResults2ModelLocalFlagAndRole(grouperModel, isLocal, roleId);
    }

    /**
     * maybe grouping setting "nur für aktuelle Rolle" should be passed here as
     * an argument to decide if current user role id or role id = 0 has to be
     * used!
     *
     * @param grouperModel grouper model
     * @param isLocal local/external
     * @return batch result
     */
    @Override
    public TBatchResult findBatchResultForRole(GDRGModel grouperModel, Boolean isLocal) {
        TBatchResult batchResult = findBatchResultForRole(grouperModel, isLocal, ClientManager.getCurrentCpxUser().getActualRoleId());
        if (batchResult == null) {
            //use roleId = 0 (0 means all roles)
            long allRoles = 0L;
            batchResult = findBatchResultForRole(grouperModel, isLocal, allRoles);
        }
        return batchResult;
    }

}
