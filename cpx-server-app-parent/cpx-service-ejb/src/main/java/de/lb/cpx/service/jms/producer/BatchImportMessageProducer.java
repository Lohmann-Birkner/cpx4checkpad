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
 *    2016  Husser - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.jms.producer;

import de.lb.cpx.shared.dto.MessageDTO;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;

/**
 *
 * @author Husser
 */
@Stateless
public class BatchImportMessageProducer {

    private static final Logger LOG = Logger.getLogger(BatchImportMessageProducer.class.getName());

    @Inject
    private JMSContext jmsContext;

    @Resource(lookup = "java:jboss/exported/jms/queue/CpxBatchImportStatusQueue")
    private Destination destination;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendObjectMessage(List<Future<Boolean>> result, long executionId, MessageDTO messageDTO, String type) throws JMSException {
        if (isDone(result)) {
            ObjectMessage message = jmsContext.createObjectMessage(messageDTO);
            message.setStringProperty("importMessageType", type);
            message.setStringProperty("ExecutionId", String.valueOf(executionId));
            JMSProducer producer = jmsContext.createProducer();
            producer.setTimeToLive(60000);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT).send(destination, message);
        }
    }

    private boolean isDone(List<Future<Boolean>> result) {
        for (Future<Boolean> processState : result) {
            boolean done = processState.isDone();
            if (!done) {
                try {
                    Thread.sleep(100);
                    isDone(result);
                    break;
                } catch (InterruptedException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
                }
            }
        }
        return true;
    }

}
