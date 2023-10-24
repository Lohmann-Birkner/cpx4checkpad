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
package de.lb.cpx.client.core.model.fx.login;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author niemeier
 */
public class NewClientConfig {

    public final String host;
    public final int port;
    public final String user;
    public final String password;

    public NewClientConfig(final String pHost, final int pPort, final String pUser, final String pPassword) {
        this.host = StringUtils.trimToEmpty(pHost);
        this.port = pPort;
        this.user = StringUtils.trimToEmpty(pUser);
        this.password = StringUtils.trimToEmpty(pPassword);
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "NewClientConfig{" + "host=" + host + ", port=" + port + ", user=" + user + ", password=" + password + '}';
    }

    public String getSocket() {
        return host + ":" + port;
    }

}
