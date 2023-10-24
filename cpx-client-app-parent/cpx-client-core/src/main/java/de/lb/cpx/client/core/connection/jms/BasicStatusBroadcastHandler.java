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

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.shared.dto.broadcast.StatusBroadcastDTO;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.util.Callback;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import org.controlsfx.control.Notifications;

/**
 *
 * @author wilde
 * @param <T> type
 */
public abstract class BasicStatusBroadcastHandler<T extends Serializable> extends BasicTopicHandler {

    private static final Logger LOG = Logger.getLogger(BasicStatusBroadcastHandler.class.getName());
    private Callback<String, Void> textListener;
    private Callback<StatusBroadcastDTO<T>, Void> dtoListener;

    public BasicStatusBroadcastHandler() {
        //java:jboss/exported/jms/queue/CpxJmsBatchResponseQueue
        super("java:/jms/topic/CpxJmsStatusBroadcast", "");
        MessageListener listener = getMessageListener();
        if (listener != null) {
            try {
                setOnMessageListener(listener);
            } catch (JMSException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    public final Callback<String, Void> getTextListener() {
        return textListener;
    }

    public final Callback<StatusBroadcastDTO<T>, Void> getDtoListener() {
        return dtoListener;
    }

    protected void showNotification(StatusBroadcastDTO<T> dto, String text) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        if (dto != null && Session.instance().getClientId().equalsIgnoreCase(dto.getClientId())) {
            LOG.log(Level.FINEST, "passed message was initiated by current client, so I guess it is not necessary to show notification about this!");
            return;
        }
        Notifications not = NotificationsFactory.instance().createInformationNotification();
        not.text(text);
        not.show();
    }

    protected Callback<String, Void> createTextListener() {
        return new Callback<String, Void>() {
            @Override
            public Void call(String text) {
                showNotification(null, text);
                return null;
            }
        };
    }

    protected Callback<StatusBroadcastDTO<T>, Void> createDtoListener() {
        return new Callback<StatusBroadcastDTO<T>, Void>() {
            @Override
            public Void call(StatusBroadcastDTO<T> dto) {
                if (dto == null) {
                    return null;
                }
                showNotification(dto, dto.getComment());
                return null;
            }
        };
    }

//    public BasicStatusBroadcastHandler(String responseQueue) {
//        super(responseQueue, "");
//    }
    private MessageListener getMessageListener() {
        textListener = createTextListener();
        dtoListener = createDtoListener();
        return new MessageListener() {
            @Override
            public void onMessage(Message message) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (message instanceof TextMessage) {
                                TextMessage msg = (TextMessage) message;
                                if (textListener != null) {
                                    textListener.call(msg.getText());
                                }
                                //showNotification(msg.getText());
                                //LOG.log(Level.INFO, msg.getText());
                            }
                            if (message instanceof ObjectMessage) {
//                                    LOG.info("get Message´for client " + message.getIntProperty("ClientId"));
                                ObjectMessage msg = (ObjectMessage) message;

                                if (msg.getObject() instanceof StatusBroadcastDTO) {
                                    @SuppressWarnings("unchecked")
                                    StatusBroadcastDTO<T> dto = (StatusBroadcastDTO<T>) msg.getObject();
                                    if (dtoListener != null) {
                                        dtoListener.call(dto);
                                    }
                                    //showNotification(dto);
                                    //LOG.log(Level.INFO, dto.getComment());
                                } else if (msg.getObject() instanceof String) {
                                    String comment = (String) msg.getObject();
                                    if (textListener != null) {
                                        textListener.call(comment);
                                    }
                                    //showNotification(comment);
                                    //LOG.log(Level.INFO, comment);
                                }
                            }
                            if (message != null && !isClosed()) {
                                message.acknowledge();
                            }
                        } catch (JMSException | IllegalStateException ex) {
                            LOG.log(Level.WARNING, null, ex);
                        }
                    }

                    public void showNotification(String text) {
                        Notifications not = NotificationsFactory.instance().createInformationNotification();
                        not.text(text);
                        not.show();
                    }

                    public void showNotification(StatusBroadcastDTO<T> dto) {
                        if (dto == null) {
                            return;
                        }
                        showNotification(dto.getComment());
                    }
                });
            }
        };
    }

}
