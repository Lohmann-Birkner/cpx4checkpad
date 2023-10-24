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
import de.lb.cpx.service.ejb.P21ExportEJB;
import de.lb.cpx.shared.dto.MessageDTO;
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
 * Stateless Bean to send BatchGrouperInformations to the Client, Client is
 * identified by ClientId accessable via ClientManager.getCurrentCpxClientId()
 * ObjectMessage is send with content of BachGroupingDTO - Object Uses Queue:
 * java:jboss/exported/jms/queue/CpxJmsBatchResponseQueue
 *
 * @author wilde
 */
@Stateless
public class P21MessageProducer {

    private static final Logger LOG = Logger.getLogger(P21MessageProducer.class.getName());

    @Inject
    private JMSContext jmsCtx;

    @Resource(lookup = "java:jboss/exported/jms/queue/CpxJmsBatchResponseQueue")
    private Destination destination;

    /**
     * send ObjectMessage to the Client
     *
     * @param pExecutionId t
     * @param messageDTO t
     * @param type t
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendObjectMessage(final long pExecutionId, MessageDTO messageDTO, String type) {
        try {
            LOG.log(Level.FINEST, "P21MessageProducer:sendObjectMessage: ExecutionID: {0}", pExecutionId);
            ObjectMessage message = jmsCtx.createObjectMessage(messageDTO);
            message.setStringProperty("ClientId", ClientManager.getCurrentCpxClientId());
            message.setStringProperty("importMessageType", type);
            message.setStringProperty("ExecutionId", String.valueOf(pExecutionId));
            JMSProducer producer = jmsCtx.createProducer();
            producer.setTimeToLive(60000);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT).send(destination, message);
        } catch (JMSException ex) {
            Logger.getLogger(P21ExportEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
