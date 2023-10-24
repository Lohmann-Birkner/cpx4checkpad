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
package de.lb.cpx.service.jms.consumer;

import de.lb.cpx.service.jms.producer.SimpleMessageProducer;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author wilde
 */
@MessageDriven(
        activationConfig = {
            @ActivationConfigProperty(
                    propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
            @ActivationConfigProperty(
                    propertyName = "destinationLookup", propertyValue = "jms/queue/CpxJmsRequestQueue")
        },
        mappedName = "jms/queue/CpxJmsQueue")
public class SimpleMessageConsumer implements MessageListener {

    private static final Logger LOG = Logger.getLogger(SimpleMessageConsumer.class.getSimpleName());

    @Inject
    private SimpleMessageProducer producer;

    @Override
    public void onMessage(Message message) {
        try {
//        message.acknowledge();
            LOG.info("onMessage: Message vom Typ " + message.getClass().toString() + " erhalten. Id " + message.getJMSMessageID());
            LOG.info("wieder gesendet? " + message.getJMSRedelivered());
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                LOG.info("TextMessage enthält diesen Text: " + textMessage.getText());
//        textMessage.setText("Server send its regards");
                producer.sendMessage("get message on " + new Date().toString() + " Message was " + textMessage.getText(), message.getStringProperty("ClientId"));
            } else {
                LOG.info("Sonstige Message. toString() = " + message.toString());
            }
        } catch (JMSException jex) {
            LOG.log(Level.SEVERE, "Fehler beim Verarbeiten der Message: " + jex.getMessage(), jex);
            throw new EJBException("Fehler beim Verarbeiten der Message: " + jex.getMessage(), jex);
        }
    }

}
