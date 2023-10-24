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
public class CpxDisconnectedException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public final String paramValue;

    public CpxDisconnectedException() {
        super();
        paramValue = getParamValue("");
    }

    public CpxDisconnectedException(String message) {
        super(message);
        paramValue = getParamValue("");
    }

    public CpxDisconnectedException(String message, Throwable cause) {
        super(message, cause);
        paramValue = getParamValue("");
    }

    public CpxDisconnectedException(Throwable cause) {
        super(cause);
        paramValue = getParamValue("");
    }

    protected CpxDisconnectedException(String message, Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        paramValue = getParamValue("");
    }

    public CpxDisconnectedException(final Object pParamValue) {
        super();
        paramValue = getParamValue(pParamValue);
    }

    public CpxDisconnectedException(String message, final Object pParamValue) {
        super(message);
        paramValue = getParamValue(pParamValue);
    }

    public CpxDisconnectedException(String message, Throwable cause, final Object pParamValue) {
        super(message, cause);
        paramValue = getParamValue(pParamValue);
    }

    public CpxDisconnectedException(Throwable cause, final Object pParamValue) {
        super(cause);
        paramValue = getParamValue(pParamValue);
    }

    protected CpxDisconnectedException(String message, Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace, final Object pParamValue) {
        super(message, cause, enableSuppression, writableStackTrace);
        paramValue = getParamValue(pParamValue);
    }

    private static String getParamValue(final Object pParamValue) {
        if (pParamValue == null) {
            return "";
        }
        String val = String.valueOf(pParamValue);
        return (val == null) ? "" : val.trim();
    }

    public String getParamValue() {
        return paramValue;
    }

}
