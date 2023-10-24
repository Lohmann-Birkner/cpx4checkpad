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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.service.facade;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TBatchResult;
import de.lb.cpx.service.ejb.BatchResultEJBRemote;

/**
 *
 * @author wilde
 */
public class BatchResultServiceFacade {

    private final EjbProxy<BatchResultEJBRemote> batchResultEJB;

    public BatchResultServiceFacade() {
        batchResultEJB = Session.instance().getEjbConnector().connectBatchResultBean();
    }

    public TBatchResult findBatchResult(GDRGModel grouperModel, boolean isLocal) {
        return batchResultEJB.get().findBatchResultForRole(grouperModel, isLocal);
    }
}
