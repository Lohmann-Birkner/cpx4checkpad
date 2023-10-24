/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.mail;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.core.menu.cache.MenuCache;
import de.lb.cpx.client.core.model.fx.alert.SendMail;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmAction;
import de.lb.cpx.wm.model.TWmProcess;
import de.lb.cpx.wm.model.TWmProcessCase;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;

/**
 *
 * @author niemeier
 */
public class MailCreator {

    private static final Logger LOG = Logger.getLogger(MailCreator.class.getName());

    /**
     *
     * @param pProcess process
     * @param pAction action
     */
    public void sendMail(final TWmProcess pProcess, final TWmAction pAction) {
        if (pProcess == null) {
            return;
        }
        if (pAction == null) {
            return;
        }
        SendMail sendMail = new SendMail();
        final String receiverMail = "";
        final StringBuilder Cases = new StringBuilder();
        for (TWmProcessCase cs : pProcess.getProcessCases()) {
            //getObsProcessCases().forEach((pCase) -> {
            String append = Cases.length() != 0 ? ", " : "";
            Cases.append(append).append(cs.getHosCase().getCsCaseNumber());
        }
        String actionSubjectName = "";
        if (pAction.getActionType() != 0L) {
            actionSubjectName = MenuCache.instance().getActionSubjectName(pAction.getActionType());
        }
        final String subject = "Vorgang " + pProcess.getWorkflowNumber() + " (" + actionSubjectName + ") " + Lang.getCaseNumber() + ": " + Cases.toString();
        String messg = "";
        if (pAction.getComment() != null) {
            messg = String.valueOf(pAction.getComment()).replace("\n", "<br>");
        }
        final boolean html = true;
        try {
            sendMail.openDraft(receiverMail, subject, messg, html);
        } catch (MessagingException | IOException ex) {
            LOG.log(Level.SEVERE, "Unable to create mail draft", ex);
            MainApp.showErrorMessageDialog(ex, "Es konnte kein E-Mail-Entwurf erstellt werden");
        }
    }

}
