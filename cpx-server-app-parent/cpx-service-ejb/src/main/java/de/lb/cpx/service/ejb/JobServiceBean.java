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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.server.config.CpxServerConfigLocal;
import de.lb.cpx.service.startup_ejb.JobSchedulerBean;
import de.lb.cpx.shared.dto.job.config.CpxJobConfig;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * insert description here :|
 *
 * @author niemeier
 */
@Stateless
public class JobServiceBean implements JobServiceBeanRemote {

    @Inject
    private JobSchedulerBean jobScheduler;

//    private static final Logger LOG = Logger.getLogger(JobServiceBean.class.getName());
    @EJB(name = "CpxServerConfig")
    private CpxServerConfigLocal cpxServerConfig;

    @Override
    public Map<Integer, CpxJobConfig> getJobConfigs() {
        return cpxServerConfig.getJobConfigs();
    }

//    @Override
//    public Map<Integer, CpxJobConfig> getJobConfig(final String pName) {
//        return cpxServerConfig.getJobConfig(pName);
//    }
    @Override
    public Map<Integer, CpxJobConfig> getJobConfig(final long pId) {
        return cpxServerConfig.getJobConfig(pId);
    }

//    @Override
//    public <T extends CpxJobConfig> T getJobConfig(final Class<T> pJobConfigClass, final String pName) {
//        return cpxServerConfig.getJobConfig(pJobConfigClass, pName);
//    }
    @Override
    public <T extends CpxJobConfig> T getJobConfig(final Class<T> pJobConfigClass, final long pId) {
        return cpxServerConfig.getJobConfig(pJobConfigClass, pId);
    }

    @Override
    public <T extends CpxJobConfig> Map<Integer, T> getJobConfigs(final Class<T> pJobConfigClass) {
        return cpxServerConfig.getJobConfigs(pJobConfigClass);
    }

    @Override
    public boolean removeJobConfig(CpxJobConfig pJobConfig) {
        boolean result = cpxServerConfig.removeJobConfig(pJobConfig);
        result = result && jobScheduler.cancelTimer(pJobConfig);
        return result;
    }

//    @Override
//    public <T extends CpxJobConfig> boolean removeJobConfig(final Class<T> pJobConfigClass, final String pName) {
//        boolean result = cpxServerConfig.removeJobConfig(pJobConfigClass, pName);
//        result = result && jobScheduler.cancelTimer(pJobConfig);
//        return result;
//    }
    @Override
    public boolean saveJobConfig(CpxJobConfig pJobConfig) {
        boolean result = cpxServerConfig.saveJobConfig(pJobConfig);
        result = result && jobScheduler.initJob(pJobConfig);
        return result;
    }

}
