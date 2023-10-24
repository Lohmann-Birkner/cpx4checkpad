/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.handler;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.fx.alert.IssueInfo;
import de.lb.cpx.service.properties.CpxAuthorizationException;
import de.lb.cpx.shared.dto.LockException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 * This class is called when an unhandled exception occurs
 *
 * @author niemeier
 */
public class CpxUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger LOG = Logger.getLogger(CpxUncaughtExceptionHandler.class.getSimpleName());

    /**
     * Collect some (hopefully) useful information to analyze the problem
     *
     * @param thread affected thread
     * @param ex error
     */
    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        final String firstStackTraceLine = ex.getStackTrace() != null && ex.getStackTrace().length > 0 ? String.valueOf(ex.getStackTrace()[0]) : "";
        if (firstStackTraceLine.contains("javafx.stage.Window$TKBoundsConfigurator.apply(Window.java:")) {
            LOG.log(Level.FINEST, "Suppressed exception (caused by ControlsFX Notifications?! - yeah, that happens sometimes)", ex);
            return;
        }
        if (ex.getMessage() != null) {
            if (ex.getMessage().equals("Can not show popup. The node must be attached to a scene/window.")) {
                LOG.log(Level.FINEST, "Suppressed exception (caused by ControlsFX Autocompletion?! - yeah, that happens sometimes)", ex);
                return;
            }
        }
        if (ex.getCause() instanceof LockException) {
            //do not handle lock exceptions as unhandled exceptions!
            BasicMainApp.showLockMessage((LockException) ex.getCause());
            return;
        }
        if (ex.getCause() instanceof CpxAuthorizationException) {
            BasicMainApp.showAuthorizationMessage((CpxAuthorizationException) ex.getCause());
            return;
        }

        final IssueInfo issueInfo = new IssueInfo();

        final String msg = String.format("Uncaught Exception occured in thread %s under the following conditions:\n=> %s: %s ",
                thread.getName(),
                issueInfo.toString(),
                ex.getMessage() == null ? "exception message is null" : ex.getMessage());
        final LogRecord lr = new LogRecord(Level.SEVERE, msg);
        lr.setThrown(ex);
        LOG.log(lr);

        //2018-04-19 DNi: Maybe it makes sense to offer the ability to send an error report via mail here...
        Platform.runLater(() -> {
            BasicMainApp.showErrorMessageDialog(ex, "Es ist ein unerwarteter Fehler aufgetreten");
        });
    }

}
