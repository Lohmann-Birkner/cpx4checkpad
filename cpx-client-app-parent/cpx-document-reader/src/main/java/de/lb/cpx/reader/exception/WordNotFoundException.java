/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.reader.exception;

/**
 *
 * @author niemeier
 */
public class WordNotFoundException extends ProgramNotFoundException {

    private static final long serialVersionUID = 1L;

    public WordNotFoundException() {
    }

    public WordNotFoundException(String message) {
        super(message);
    }

    public WordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WordNotFoundException(Throwable cause) {
        super(cause);
    }

    public WordNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}