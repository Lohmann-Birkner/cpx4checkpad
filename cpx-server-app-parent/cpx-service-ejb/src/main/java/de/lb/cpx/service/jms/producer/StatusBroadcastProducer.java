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
import de.lb.cpx.shared.dto.broadcast.BroadcastOriginEn;
import de.lb.cpx.shared.dto.broadcast.StatusBroadcastDTO;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.batch.runtime.BatchStatus;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Topic;

/**
 * Object Uses Queue: java:jboss/exported/jms/queue/CpxJmsStatusBroadcast
 *
 * @author wilde
 * @param <T> result type
 */
@Stateless
public class StatusBroadcastProducer<T extends Serializable> {

    private static final Logger LOG = Logger.getLogger(StatusBroadcastProducer.class.getName());

    @Inject
    private JMSContext jmsCtx;

    @Resource(lookup = "java:jboss/exported/jms/topic/CpxJmsStatusBroadcast")
    private Topic topic;

    /**
     * send ObjectMessage to the Client
     *
     * @param pStatusBroadcastDTO dto
     * @param pTimeToLive time of live of the send message, till its deleted by
     * system if it is not conusmed
     * @throws JMSException Exception if the Communication malfunction for
     * example if queue is unavailable
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void send(final StatusBroadcastDTO<T> pStatusBroadcastDTO, long pTimeToLive) throws JMSException {
        if (pStatusBroadcastDTO == null) {
            return;
        }
        Long executionId = pStatusBroadcastDTO.getJobNumber();
        ObjectMessage message = jmsCtx.createObjectMessage(pStatusBroadcastDTO);
        message.setStringProperty("ClientId", ClientManager.getCurrentCpxClientId());
        message.setStringProperty("ExecutionId", String.valueOf(executionId));
        message.setStringProperty("Database", pStatusBroadcastDTO.getDatabase());
        message.setStringProperty("PersistenceUnit", pStatusBroadcastDTO.getPersistenceUnit());
        message.setStringProperty("Origin", pStatusBroadcastDTO.getOrigin() == null ? null : pStatusBroadcastDTO.getOrigin().name());
        message.setStringProperty("UserId", pStatusBroadcastDTO.getUserId() == null ? null : String.valueOf(pStatusBroadcastDTO.getUserId()));
        JMSProducer producer = jmsCtx.createProducer();
//        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        producer.setTimeToLive(pTimeToLive); //TTL in milliseconds
        producer.send(topic, message);
    }

    /**
     * send ObjectMessage to the Client
     *
     * @param pStatusBroadcastDTO dto
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void send(final StatusBroadcastDTO<T> pStatusBroadcastDTO) {
        try {
            send(pStatusBroadcastDTO, Message.DEFAULT_TIME_TO_LIVE); //DEFAULT_TIME_TO_LIVE = the message never expires. Is this clever?!?
        } catch (JMSException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void send(final BroadcastOriginEn pOrigin, final String pComment,
            final Long pJobNumber, final BatchStatus pBatchStatus,
            final Exception pException) {
        send(pOrigin, pComment, pJobNumber, pBatchStatus, pException, null);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void send(final BroadcastOriginEn pOrigin, final String pComment,
            final Long pJobNumber, final BatchStatus pBatchStatus,
            final Exception pException, final T pResult) {
        String clientId = ClientManager.getCurrentClientId();
        Long userId = ClientManager.getCurrentCpxUserId();
        String userName = ClientManager.getCurrentCpxUserName();
        String database = ClientManager.getActualDatabase();
        //String jobId = ClientManager.getCurrentCpxJobId();
        StatusBroadcastDTO<T> dto = new StatusBroadcastDTO<>(pOrigin, clientId, userId, userName, database, pComment, pJobNumber, pBatchStatus, pException, pResult);
        send(dto);
    }

    public void send(final BroadcastOriginEn pOrigin, final String pComment) {
        send(pOrigin, pComment, null, null, null, null);
    }

    public void send(final BroadcastOriginEn pOrigin, final String pComment, final Long pJobNumber) {
        send(pOrigin, pComment, pJobNumber, null, null, null);
    }

    public void send(final BroadcastOriginEn pOrigin, final String pComment, final Long pJobNumber, final Exception pException) {
        send(pOrigin, pComment, pJobNumber, null, pException, null);
    }

    public void send(final BroadcastOriginEn pOrigin, final String pComment, final Long pJobNumber, final BatchStatus pBatchStatus) {
        send(pOrigin, pComment, pJobNumber, pBatchStatus, null, null);
    }

    public void send(final BroadcastOriginEn pOrigin, final String pComment, final T pResult) {
        send(pOrigin, pComment, null, null, null, pResult);
    }

    public void send(final BroadcastOriginEn pOrigin, final String pComment, final Long pJobNumber, final T pResult) {
        send(pOrigin, pComment, pJobNumber, null, null, pResult);
    }

    public void send(final BroadcastOriginEn pOrigin, final String pComment, final Long pJobNumber, final Exception pException, final T pResult) {
        send(pOrigin, pComment, pJobNumber, null, pException, pResult);
    }

    public void send(final BroadcastOriginEn pOrigin, final String pComment, final Long pJobNumber, final BatchStatus pBatchStatus, final T pResult) {
        send(pOrigin, pComment, pJobNumber, pBatchStatus, null, pResult);
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
