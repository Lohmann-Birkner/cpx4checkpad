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
 *    2016  Dirk Niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.scheduled_ejb;

import de.lb.cpx.server.auth.ClientManager;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timer;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;

/**
 * Tries to create all Entity Manager Factories, so that the first client don't
 * has to wait for this step
 *
 * @author Dirk Niemeier
 */
@Singleton
//@Startup
//@DependsOn("InitBean")
@SecurityDomain("cpx")
public class InitEntityManagerFactoryBean {

    private static final Logger LOG = Logger.getLogger(InitEntityManagerFactoryBean.class.getName());

    //@PostConstruct
    @Schedule(hour = "*", minute = "*", second = "*", persistent = false)
    @TransactionTimeout(value = 30, unit = TimeUnit.MINUTES)
    public void init(Timer timer) {
        if (timer != null) {
            try {
                timer.cancel();
            } catch (NoSuchObjectLocalException ex) {
                LOG.log(Level.FINEST, "Timer canceled with error", ex);
            }
        }
        LOG.log(Level.INFO, "Try to create Entity Manager Factories on startup...");
        List<String> persistenceUnits = ClientManager.getAvailablePersistenceUnitNames();
        LOG.log(Level.INFO, (persistenceUnits == null ? 0 : persistenceUnits.size()) + " valid persistence units found");
    }

}
