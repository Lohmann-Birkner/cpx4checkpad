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
package de.lb.cpx.client.core.connection.jms;

import de.lb.cpx.client.core.config.CpxClientConfig;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Base implementation of the JMS-Message Handler, implements basic queues for
 * communication TODO:Refactor to service-api?
 *
 * @author wilde
 */
public abstract class BasicMessageHandler {

    private static final Logger LOG = Logger.getLogger(BasicMessageHandler.class.getSimpleName());
    private Destination responseQueue;
    private Connection connection;
    private Session jmsSession;
    private MessageConsumer consumer;
    private String clientIdent;
    private boolean isClosed = false;

    /**
     * creates new instance to listen to cpxJmsRequestQueue
     */
    public BasicMessageHandler() {
        try {
            init("java:/jms/queue/CpxJmsResponseQueue", "java:/jms/queue/CpxJmsRequestQueue");
        } catch (NamingException | JMSException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * creates new instance to listen to cpxJmsRequestQueue
     *
     * @param clientId to identify the client
     */
    public BasicMessageHandler(String clientId) {
        clientIdent = clientId;
        try {
            init("java:/jms/queue/CpxJmsResponseQueue", "java:/jms/queue/CpxJmsRequestQueue");
        } catch (NamingException | JMSException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * creates new instance and enable basic request and response queues
     *
     * @param responseQueue server response
     * @param requestQueue request to server
     */
    public BasicMessageHandler(String responseQueue, String requestQueue) {
        try {
            init(responseQueue, requestQueue);
        } catch (NamingException | JMSException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * creates new instance and enable basic request and response queues
     *
     * @param responseQueue server response
     * @param requestQueue request to server
     * @param clientId to identify the client
     */
    public BasicMessageHandler(String responseQueue, String requestQueue, String clientId) {
        this.clientIdent = clientId;
        try {
            init(responseQueue, requestQueue);
        } catch (NamingException | JMSException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    // creates InitialContext, needed for Jndi context and security features
    protected InitialContext getInitialContext() throws NamingException {
        Properties props = new Properties();
        //JBoss (Wildfly 10) specific JndiContext Factory - this can change from Wildfly Version to Wildfly Version!
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
//        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        //Disable SecurityChecks
        props.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        //build URL of the Queues that should be addressed
        props.put(Context.PROVIDER_URL, "http-remoting://" + CpxClientConfig.instance().getServerHost() + ":" + CpxClientConfig.instance().getServerPort());

        props.put(Context.SECURITY_PRINCIPAL, String.valueOf(de.lb.cpx.client.core.config.Session.instance().getEjbConnector().getClientId()));
        props.put(Context.SECURITY_CREDENTIALS, "");

        InitialContext ic = new InitialContext(props);
        return ic;
    }

    private void init(String responseChannel, String requestChannel) throws NamingException, JMSException {
        String currentId;
        if (clientIdent == null) {
            currentId = de.lb.cpx.client.core.config.Session.instance().getEjbConnector().getClientId();
        } else {
            currentId = clientIdent;
        }
        LOG.log(Level.FINE, "init jmsHandler with id {0} listen to {1} send to {2}", new Object[]{currentId, responseChannel, requestChannel});
        InitialContext ctx = getInitialContext();
        ConnectionFactory cf = (ConnectionFactory) ctx.lookup("jms/RemoteConnectionFactory");
//        // Create a JMS connection
//        if (!requestChannel.isEmpty()) {
//            requestQueue = (Destination) ctx.lookup(requestChannel);
//        }
        if (!responseChannel.isEmpty()) {
            responseQueue = (Destination) ctx.lookup(responseChannel);
        }
        connection = cf.createConnection();

        connection.start();

//        jmsSession = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        jmsSession = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

        if (responseQueue != null) {
            //creates Listener, add Filter for the current ClientId, only messages were this Statement return true are forwarded to the MessageListener
            //to be checked if there are performance issues with a large number of clients and different Messages in the System
            LOG.log(Level.FINE, "init jms system with client id {0}", currentId);
//            currentId="1";
            consumer = jmsSession.createConsumer(responseQueue, "ClientId = '" + currentId + "'");
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        if (message instanceof TextMessage) {
                            try {
                                int id = 0;//message.getIntProperty("ClientId");
                                LOG.log(Level.INFO, "get Message from Server: {0} SendId {1} currentId {2}", new Object[]{((TextMessage) message).getText(), id, de.lb.cpx.client.core.config.Session.instance().getEjbConnector().getClientId()});
                            } catch (JMSException ex) {
                                LOG.log(Level.SEVERE, null, ex);
                            }
                        } else {
                            LOG.log(Level.WARNING, "Message has unexpected Type {0}", message.getClass().getName());
                        }
                        message.acknowledge();
                    } catch (JMSException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    }
                }
            });
        }

        connection.setExceptionListener(new ExceptionListener() {
            @Override
            public void onException(JMSException exception) {
                LOG.log(Level.SEVERE, "Exception happend in JMS Communication {0}", exception.getMessage());
            }
        });
    }

    /**
     * sets the exception listener for internal jms errors
     *
     * @param pListener listener to set
     * @throws JMSException throws sone jms excetions to ... for some reason, do
     * not ask me why
     */
    public void setExceptionListener(ExceptionListener pListener) throws JMSException {
        if (connection != null) {
            connection.setExceptionListener(pListener);
        }
    }

    /**
     * set on message listener to react to messages to the client
     *
     * @param listener listener to be called
     * @throws JMSException if message could not be processed
     */
    public void setOnMessageListener(MessageListener listener) throws JMSException {
        if (consumer != null) {
            consumer.setMessageListener(listener);
        } else {
            LOG.severe("Can't set Listener, no consumer set!");
        }
        LOG.log(Level.FINEST, "Consumer registered with selector: {0}", consumer == null ? "null" : consumer.getMessageSelector());
    }

    /**
     * Close the JMS Connection
     */
    public void close() {
        try {
            LOG.log(Level.FINE, "close JMSConnection...");
            if (connection == null) {
                LOG.log(Level.INFO, "JMS connection is null!");
            } else {
                connection.close();
            }
            isClosed = true;
        } catch (JMSException ex) {
            LOG.log(Level.SEVERE, "Was not able to close JMS connection", ex);
        }
    }

    /**
     * WARNING:UNSAFE, don't indicate if session or connection are closed .. no
     * methode provided to check the status of the objects
     *
     * @return is closed boolean , is true when close was called
     */
    public boolean isClosed() {
        return isClosed;
    }
}
