/*
 * Copyright (c) 2019 Lohmann & Birkner.
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

import de.lb.cpx.shared.dto.job.config.CpxJobConfig;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author niemeier
 */
@Remote
public interface JobServiceBeanRemote {

    Map<Integer, CpxJobConfig> getJobConfigs();

    <T extends CpxJobConfig> Map<Integer, T> getJobConfigs(Class<T> pJobConfigClass);

//    Map<Integer, CpxJobConfig> getJobConfig(String pName);
    Map<Integer, CpxJobConfig> getJobConfig(long pId);

//    <T extends CpxJobConfig> T getJobConfig(Class<T> pJobConfigClass, String pName);
    <T extends CpxJobConfig> T getJobConfig(Class<T> pJobConfigClass, long pId);

    boolean removeJobConfig(CpxJobConfig pJobConfig);

//    <T extends CpxJobConfig> boolean removeJobConfig(final Class<T> pJobConfigClass, final String pName);
    boolean saveJobConfig(CpxJobConfig pJobConfig);

}
