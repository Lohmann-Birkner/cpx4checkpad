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
package de.lb.cpx.service.jms.producer;

import de.lb.cpx.server.auth.ClientManager;
//import de.lb.cpx.shared.dto.BatchGroupingDTO;
import de.lb.cpx.shared.dto.MessageDTO;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;

/**
 * Stateless Bean to send BatchGrouperInformations to the Client, Client is
 * identified by ClientId accessable via ClientManager.getCurrentCpxClientId()
 * ObjectMessage is send with content of BachGroupingDTO - Object Uses Queue:
 * java:jboss/exported/jms/queue/CpxJmsBatchResponseQueue
 *
 * @author wilde
 */
@Stateless
public class BatchGrouperMessageProducer {

    @Inject
    private JMSContext jmsCtx;

    @Resource(lookup = "java:jboss/exported/jms/queue/CpxJmsBatchResponseQueue")
    private Destination destination;

    /**
     * send ObjectMessage to the Client
     *
     * @param pExecutionId execution id
     * @param batchGroupingDTO DTO Object to Store a simple Message and a
     * percentage Value that represents the current Amount of done work
     * @param pTimeToLive time of live of the send message, till its deleted by
     * system if it is not conusmed
     * @throws JMSException Exception if the Communication malfunction for
     * example if queue is unavailable
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendObjectMessage(final long pExecutionId, MessageDTO batchGroupingDTO, long pTimeToLive) throws JMSException {
        ObjectMessage message = jmsCtx.createObjectMessage(batchGroupingDTO);
        message.setStringProperty("ClientId", ClientManager.getCurrentCpxClientId());
        message.setStringProperty("ExecutionId", String.valueOf(pExecutionId));
        JMSProducer producer = jmsCtx.createProducer();
        producer.setTimeToLive(pTimeToLive);
//        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        producer.send(destination, message);
    }

    /**
     * send ObjectMessage to the Client
     *
     * @param pExecutionId execution id
     * @param batchGroupingDTO DTO Object to Store a simple Message and a
     * percentage Value that represents the current Amount of done work
     * @throws JMSException Exception if the Communication malfunction for
     * example if queue is unavailable
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendObjectMessage(final long pExecutionId, MessageDTO batchGroupingDTO) throws JMSException {
        sendObjectMessage(pExecutionId, batchGroupingDTO, Message.DEFAULT_TIME_TO_LIVE);
    }

//    /**
//     * send ObjectMessage to the Client
//     *
//     * @param pExecutionId execution id
//     * @param comment Message to be send
//     * @throws JMSException Exception if the Communication malfunction for
//     * example if queue is unavailable
//     */
//    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
//    public void sendObjectMessage(final long pExecutionId, String comment) throws JMSException {
//        ObjectMessage message = jmsCtx.createObjectMessage(comment);
//        message.setStringProperty("ClientId", ClientManager.getCurrentCpxClientId());
//        message.setStringProperty("ExecutionId", String.valueOf(pExecutionId));
//        JMSProducer producer = jmsCtx.createProducer();
//        producer.send(destination, message);
//    }
}
