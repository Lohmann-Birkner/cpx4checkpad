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
package de.lb.cpx.client.core.util.cpx_handler;

/**
 *
 * @author niemeier
 */
public abstract class CpxHandleLink {

    public static final String PROTOCOL_PREFIX = "cpx:";
    public static final String PROTOCOL_CASE_PREFIX = "fall_";
    public static final String PROTOCOL_PROCESS_PREFIX = "vorgang_";

    public static final String PROTOCOL_CASE_LABEL = "Fall ";
    public static final String PROTOCOL_PROCESS_LABEL = "Vorgang ";

    protected String url;
    protected String title;

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        if (url == null || url.isEmpty()) {
            return "";
        }
        return "<a href=\"" + url + "\">" + title + "</a>";
    }

    @Override
    public String toString() {
        return getLink();
    }

}
