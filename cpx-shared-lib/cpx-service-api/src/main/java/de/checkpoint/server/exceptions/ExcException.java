/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.checkpoint.server.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * hides the ExcException class of the checkpoint
 *
 * @author gerschmann
 */
public class ExcException extends Exception {

    private static final long serialVersionUID = 1L;

    public static void createException(Throwable ex) {
        Logger.getAnonymousLogger().log(Level.WARNING, "", ex);
    }

    public static void createException(Throwable ex, String error) {
        Logger.getAnonymousLogger().log(Level.WARNING, error, ex);
    }
}
