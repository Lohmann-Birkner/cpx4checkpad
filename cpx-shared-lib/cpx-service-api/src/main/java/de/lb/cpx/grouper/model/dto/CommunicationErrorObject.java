/* 
 * Copyright (c) 2015 Lohmann & Birkner.
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
 *    2015  Wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.grouper.model.dto;

import de.lb.cpx.grouper.model.enums.ErrorCause;

/**
 * Klasse zur Darstellung der eines Fehlers auf der Clientseite
 *
 * @author Wilde
 */
public class CommunicationErrorObject {

    private final ErrorCause m_errorCause;
    private final String m_errorText;

    public CommunicationErrorObject(ErrorCause cause, String errorText) {
        m_errorCause = cause;
        m_errorText = errorText;
    }

    public ErrorCause getErrorCause() {
        return m_errorCause;
    }

    public String getErrorText() {
        return m_errorText;
    }

}
