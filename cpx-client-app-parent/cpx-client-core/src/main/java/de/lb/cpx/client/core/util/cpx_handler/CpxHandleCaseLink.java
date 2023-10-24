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
public class CpxHandleCaseLink extends CpxHandleLink {

    public CpxHandleCaseLink(final String pHospitalIdent, final String pCaseNumber, final String pDatabase) {
        final String hospitalIdent = StringUtils.trimToEmpty(pHospitalIdent);
        final String caseNumber = StringUtils.trimToEmpty(pCaseNumber);
        final String database = StringUtils.trimToEmpty(pDatabase);
        String linkUrl;
        final String linkTitle;
        if (caseNumber.isEmpty()) {
            linkUrl = "";
            linkTitle = "";
        } else if (hospitalIdent.isEmpty()) {
            linkUrl = PROTOCOL_PREFIX + PROTOCOL_CASE_PREFIX + caseNumber;
            linkTitle = PROTOCOL_CASE_LABEL + caseNumber;
        } else {
            linkUrl = PROTOCOL_PREFIX + PROTOCOL_CASE_PREFIX + hospitalIdent + "_" + caseNumber;
            linkTitle = PROTOCOL_CASE_LABEL + hospitalIdent + "_" + caseNumber;
        }
        if (!linkUrl.isEmpty() && !database.isEmpty()) {
            linkUrl += "@" + database;
        }
        url = linkUrl;
        title = linkTitle;
    }

}
