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
 *    2016  Husser - Listener which is invoked after each step of csv-file import. 
 */
package de.lb.cpx.server.batch;

import java.util.logging.Logger;
import javax.batch.api.listener.StepListener;
import javax.batch.runtime.Metric;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Husser
 */
@Dependent
@Named("CpxStepListener")
public class CpxStepListener implements StepListener {

    private static final Logger LOG = Logger.getLogger(CpxStepListener.class.getName());

    @Inject
    private StepContext stepContext;

    public CpxStepListener() {
    }

    @Override
    public void beforeStep() throws Exception {
    }

    @Override
    public void afterStep() throws Exception {

        Metric[] metrics = stepContext.getMetrics();
        for (Metric metric : metrics) {
            String metricName = metric.getType().name();
            LOG.info(metricName + ": " + metric.getValue());
        }

    }

}
