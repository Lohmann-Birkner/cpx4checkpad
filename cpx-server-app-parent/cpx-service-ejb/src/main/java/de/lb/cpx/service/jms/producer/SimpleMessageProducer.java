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
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 *
 * @author wilde
 */
@Stateless
public class SimpleMessageProducer {

    private static final Logger LOG = Logger.getLogger(SimpleMessageProducer.class.getSimpleName());

    @Inject
    private JMSContext jmsCtx;

    @Resource(lookup = "java:jboss/exported/jms/queue/CpxJmsResponseQueue")
//    @Resource(lookup = "java:jboss/exported/jms/queue/CpxJmsBatchResponseQueue")
    private Destination destination;

    /**
     * creats a new SimpleMessageProducer, sends a message in
     * java:jboss/exported/jms/queue/CpxJmsResponseQueue and add currentCliendId
     * in propertys to specify the destination
     */
    public SimpleMessageProducer() {
    }

    /**
     * sends simple message add currentClientId to the properties of the message
     *
     * @param msg textMessage
     * @param clientId currentClient Id, can be null, should be removed
     * @throws JMSException throws Exception if message can't be set in Queue
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendMessage(String msg, String clientId) throws JMSException {
        TextMessage txtMsg = jmsCtx.createTextMessage(msg);
//        ClientManager.getCurrentClientId()
//        txtMsg.setStringProperty("ClientId", ClientManager.getCurrentCpxClientId());
        LOG.info("send new Message " + msg + " to " + ClientManager.getCurrentCpxClientId());
        jmsCtx.createProducer().send(destination, txtMsg);
    }

    public void sendMessage(TextMessage msg) throws JMSException {
        LOG.info("send message " + msg.getText());
        jmsCtx.createProducer().send(destination, msg);
    }
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
//    public void sendObjectMessage(BatchGroupingDTO batchGroupingDTO) throws JMSException {
////        TextMessage message = jmsCtx.createTextMessage("Test msg");//batchGroupingDTO.toString());
//        ObjectMessage message = jmsCtx.createObjectMessage(batchGroupingDTO);
//        message.setStringProperty("ClientId", "1");//ClientManager.getCurrentCpxClientId());
////        message.setStringProperty("ClientId", ClientManager.getCurrentCpxClientId());
//        log.info("send new Message " + batchGroupingDTO.toString() + " to " + ClientManager.getCurrentCpxClientId());
//        JMSProducer producer = jmsCtx.createProducer();
//        producer.setTimeToLive(1000);
//        producer.send(destination, message);
//    }
}
