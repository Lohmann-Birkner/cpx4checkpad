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

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.model.TCase;
import de.lb.cpx.service.ejb.WorkflowListStatelessEJBRemote;
import de.lb.cpx.wm.model.TWmProcess;

/**
 * ServiceFacade to access all services regarding creation of a new Request
 * Todo: should be merged with Process servicefacade! Create Request is a task
 * that must also performed by it
 *
 * @author wilde
 */
public class CreateRequestServiceFacade {

    private final EjbProxy<WorkflowListStatelessEJBRemote> wmServiceBean;

    /**
     * creates new Servicefacade instance to access service methodes for
     * requests
     */
    public CreateRequestServiceFacade() {
        wmServiceBean = Session.instance().getEjbConnector().connectWorkflowListBean();
    }

    /**
     * find Case Entity for db id
     *
     * @param hCaseId db id for the hospitalcase to find
     * @return hospitalCase entity
     */
    public TCase findHospitalCase(long hCaseId) {
        return wmServiceBean.get().findCase(hCaseId);
    }

    /**
     * stores WmProcess entity in db
     *
     * @param process entity to store
     */
    public void storeProcess(TWmProcess process) {
        wmServiceBean.get().storeProcess(process);
    }
}
