/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.exceptions;

/**
 * cannot establish connection to database. Lost connection to database.
 *
 * @author niemeier
 */
public class CpxSapException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CpxSapException() {
    }

    public CpxSapException(String message) {
        super(message);
    }

    public CpxSapException(String message, Throwable cause) {
        super(message, cause);
    }

    public CpxSapException(Throwable cause) {
        super(cause);
    }

    public CpxSapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
