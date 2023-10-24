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
package de.lb.cpx.server.batch;

import de.lb.cpx.service.ejb.LockServiceBean;
import de.lb.cpx.service.jms.producer.SimpleMessageProducer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import javax.batch.api.listener.JobListener;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Husser
 */
@Dependent
@Named("CpxJobListener")
public class CpxJobListener implements JobListener {
    //@Inject
    //private PrepStorer prepStorer;

    private static final Logger LOG = Logger.getLogger(CpxJobListener.class.getName());

    @Inject
    protected JobContext jobContext;
    
    @Inject
    private SimpleMessageProducer producer;
    @Inject
    private LockServiceBean dBLockService;

    public CpxJobListener() {
    }

    @Override
    public void beforeJob() throws Exception {
        //prepStorer.destroy();
        //prepStorer.createPStatements();
        logTimestamp(true, BatchStatus.STARTING);
    }

    @Override
    public void afterJob() throws Exception {
        //prepStorer.destroy();
        //prepStorer.createPStatements();
        logTimestamp(false, jobContext.getBatchStatus());
    }

    protected void logTimestamp(boolean started, BatchStatus batchStatus) {

        DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        String text = (started ? "Start" : "End") + " job at " + format.format(new Date());
        StringBuilder stringBuffer = new StringBuilder(text);
        if (!started) {
            stringBuffer.append(", Status = ").append(batchStatus.name());
        }
        LOG.info(stringBuffer.toString());
    }

}
