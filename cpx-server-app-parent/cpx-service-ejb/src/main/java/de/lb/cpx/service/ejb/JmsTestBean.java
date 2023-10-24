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
package de.lb.cpx.service.ejb;

import de.lb.cpx.service.jms.producer.SimpleMessageProducer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.JMSException;

/**
 *
 * @author wilde
 */
@Stateless
public class JmsTestBean implements JmsTestBeanRemote {

    private static final Logger LOG = Logger.getLogger(JmsTestBean.class.getName());

    @EJB
    private SimpleMessageProducer producer;

    @Override
    public void testBean(String clientId) {
        try {
            producer.sendMessage("automatic test", clientId);
        } catch (JMSException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

}
