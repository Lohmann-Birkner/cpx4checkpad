/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.connector;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 *
 * @author niemeier
 * @param <T> bean class
 */
public class EjbProxy<T> {

    private static final Logger LOG = Logger.getLogger(EjbProxy.class.getName());

    private T instance;
    private final String jndiName;
    private final ObjectProperty<Context> contextProperty;

    public EjbProxy(final ObjectProperty<Context> pContextProperty, final String pJndiName) {
        jndiName = pJndiName;
        contextProperty = pContextProperty;
//        WeakAdapter weak = new WeakAdapter();
//        weak.addChangeListener(contextProperty, (observable, oldValue, newValue) -> {
//            instance = null;
//        });
//        contextProperty.addListener(new WeakChangeListener<Context>() {
//            @Override
//            public void changed(ObservableValue<? extends Context> observable, Context oldValue, Context newValue) {
//                instance = null;
//            }
//        });
//        try {
//            getInstance();
//        } catch (NamingException ex) {
//            LOG.log(Level.FINEST, "Was not able to initialize bean: " + jndiName, ex);
//        }
    }

    public T get() {
        try {
            return getInstance();
        } catch (NamingException ex) {
            LOG.log(Level.FINEST, "Was not able to connect bean: " + jndiName, ex);
            return null;
        }
    }

    public T getWithEx() throws NamingException {
        return getInstance();
    }

    private T getInstance() throws NamingException {
        T tmp = instance;
        if (tmp == null) {
            //this construct avoids synchronize blocks and improves performance, 
            //but needs double check (yes right, this is not unintended stupid code)!
            //look at Double-Check-Idiom by Doug Schmitt to get more information
            //alternative could be Holder-Class-Idiom
            synchronized (this) {
                tmp = instance;
                if (tmp == null) {
                    instance = create();
                }
            }
        }
        return instance;
    }

    /**
     * Connects to EJB with jndi-Name
     *
     * @return Remote Interfaceclass of target EJB
     * @throws NamingException naming error
     */
    @SuppressWarnings("unchecked")
    protected T create() throws NamingException {
        if (contextProperty == null || contextProperty.get() == null) {
            return null;
        }
        return (T) contextProperty.get().lookup(jndiName);
    }

}
