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

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author niemeier
 */
public class CpxHandleProcessLink extends CpxHandleLink {

    public CpxHandleProcessLink(final Long pProcessNumber, final String pDatabase) {
        this(String.valueOf(pProcessNumber), pDatabase);
    }

    public CpxHandleProcessLink(final String pProcessNumber, final String pDatabase) {
        final String processNumber = StringUtils.trimToEmpty(pProcessNumber);
        final String database = StringUtils.trimToEmpty(pDatabase);
        String linkUrl;
        final String linkTitle;
        if (pProcessNumber.isEmpty()) {
            linkUrl = "";
            linkTitle = "";
        } else {
            linkUrl = PROTOCOL_PREFIX + PROTOCOL_PROCESS_PREFIX + processNumber;
            linkTitle = "Vorgang " + processNumber;
        }
        if (!linkUrl.isEmpty() && !database.isEmpty()) {
            linkUrl += "@" + database;
        }
        url = linkUrl;
        title = linkTitle;
    }

}
