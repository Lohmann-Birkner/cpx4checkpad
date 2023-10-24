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
package de.lb.cpx.client.core.model.fx.alert;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.InitLogger;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.HostServices;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 *
 * @author niemeier
 */
public class SendMail {

    private static final Logger LOG = Logger.getLogger(SendMail.class.getName());

    public File openExceptionDraft(final String pMailTo, final String pSubject, final String pMsg, final String pDetails, final CpxSystemPropertiesInterface pCpxPropsClient, final CpxSystemPropertiesInterface pCpxPropsServer, final String pCpxServerLog, final IssueInfo pIssueInfo) throws IOException, MessagingException {
        //if (Desktop.isDesktopSupported()) {
        //Desktop desktop = Desktop.getDesktop();
        final String senderMail = "";
        final String subject = pSubject;
        final String body = pMsg + (pIssueInfo == null ? "" : "\r\n\r\n" + pIssueInfo.toString() + "\r\n\r\n" + pDetails);
        final List<File> files = new ArrayList<>();
        for (Handler h : LOG.getHandlers()) {
            //ensure that we get the most recent state of the log file
            h.flush();
        }
        //files.add(new File(MainApp.LOGFILE_PATH.get()));
        files.add(InitLogger.instance().getLogFile());

        if (pCpxPropsClient != null) {
            final File cpxClientPropFile = File.createTempFile("cpx-client-props-", ".log");
            LOG.log(Level.INFO, "Write CPX Server properties to " + cpxClientPropFile.getAbsolutePath());
            try ( PrintWriter pw = new PrintWriter(cpxClientPropFile)) {
                pw.write(pCpxPropsClient.toString());
            }
            files.add(cpxClientPropFile);
        }

        if (pCpxServerLog != null && !pCpxServerLog.trim().isEmpty()) {
            final File cpxServerLogFile = File.createTempFile("cpx-server-log-", ".log");
            LOG.log(Level.INFO, "Write CPX Server log to " + cpxServerLogFile.getAbsolutePath());
            try ( PrintWriter pw = new PrintWriter(cpxServerLogFile)) {
                pw.write(pCpxServerLog);
            }
            files.add(cpxServerLogFile);
        }

        if (pCpxPropsServer != null) {
            final File cpxServerPropFile = File.createTempFile("cpx-server-props-", ".log");
            LOG.log(Level.INFO, "Write CPX Server properties to " + cpxServerPropFile.getAbsolutePath());
            try ( PrintWriter pw = new PrintWriter(cpxServerPropFile)) {
                pw.write(pCpxPropsServer.toString());
            }
            files.add(cpxServerPropFile);
        }

        //files.add(takeScreenshot(this.getDialogPane()));
//            if (BasicMainApp.getStage().isShowing()) {
//                // && createConfirmationDialog("Do you want to add a screenshot to this mail?")
//                files.add(takeScreenshot(BasicMainApp.getStage().getScene()));
//            }
        final boolean html = false;
        final File mailFile = createMessage(pMailTo, senderMail, subject, body, html, files);
        //desktop.open(mailFile);
        openMailProgram(mailFile);
        return mailFile;
        //}
    }

    public File openDraft(final String pMailTo, final String pSubject, final String pMsg, final boolean pHtml) throws MessagingException, AddressException, IOException {
        return openDraft(pMailTo, pSubject, pMsg, pHtml, new ArrayList<>());
    }

    public File openDraft(final String pMailTo, final String pSubject, final String pMsg, final boolean pHtml, final File pFile) throws MessagingException, AddressException, IOException {
        List<File> files = new ArrayList<>();
        if (pFile != null) {
            files.add(pFile);
        }
        return openDraft(pMailTo, pSubject, pMsg, pHtml, files);
    }

    public File openDraft(final String pMailTo, final String pSubject, final String pMsg, final boolean pHtml, final List<File> pFiles) throws MessagingException, AddressException, IOException {
        //final List<File> files = new ArrayList<>();
        final String senderMail = "";
        final File mailFile = createMessage(pMailTo, senderMail, pSubject, pMsg, pHtml, pFiles);
        openMailProgram(mailFile);
        return mailFile;
    }

    protected static File createMessage(final String pToMail, final String pMailFrom, final String pSubject, final String pMsg, final boolean pHtml, final List<File> pAttachments) throws AddressException, MessagingException, IOException {
        String msg = pMsg == null ? "" : pMsg;
        if (pHtml) {
            msg = msg.replace("\r\n", "<br>");
            msg = msg.replace("\n", "<br>");
            msg = msg.replace("\r", "<br>");
        }
        final Properties props = System.getProperties();
        props.setProperty("file.encoding", "UTF-8");
        final Message message = new MimeMessage(javax.mail.Session.getInstance(props));
        message.setFrom(pMailFrom == null || pMailFrom.trim().isEmpty() ? null : new InternetAddress(pMailFrom));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(pToMail));
        message.setSubject(pSubject);
        message.addHeader("X-Unsent", "1"); //Open mail in compose mode
        message.addHeader("Disposition-Notification-To", pMailFrom);
        final String encoding;
        if (pHtml) {
            encoding = "text/html";
        } else {
            encoding = "text/plain";
        }
        // create the message part 
        final MimeBodyPart content = new MimeBodyPart();
        // fill message
        //content.setText(body, "UTF-8");
        content.setText(msg, "UTF-8");
        content.setHeader("Content-Type", encoding + "; charset=UTF-8");

        //content.setHeader("Content-Type","text/plain; charset=\"utf-8\""); 
        //content.setContent( emailBody, "text/plain; charset=utf-8" ); 
        final Multipart multipart = new MimeMultipart();
        content.setHeader("Content-Transfer-Encoding", "quoted-printable");
        multipart.addBodyPart(content);
        // add attachments
        if (pAttachments != null) {
            for (File file : pAttachments) {
                if (file == null) {
                    continue;
                }
                MimeBodyPart attachment = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                attachment.setDataHandler(new DataHandler(source));
                //attachment.setFileName(file.getName());
                attachment.setFileName(MimeUtility.encodeText(file.getName(), "UTF-8", null));
                multipart.addBodyPart(attachment);
            }
        }
        // integration
        message.setContent(multipart);
        message.saveChanges();
        // store file
        final File mailFile = File.createTempFile("cpx-mail-", ".eml");
        LOG.log(Level.INFO, "Write mail to " + mailFile.getAbsolutePath());
        try ( FileOutputStream fos = new FileOutputStream(mailFile)) {
            message.writeTo(fos);
        }
        return mailFile;
    }

    protected void openMailProgram(final File pMailFile) {
        final HostServices hostServices = BasicMainApp.instance().getHostServices();
        hostServices.showDocument(pMailFile.getAbsolutePath());
    }

}
