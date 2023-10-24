/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.mail;

/**
 *
 * @author gerschmann
 */

import de.lb.cpx.app.crypter.PasswordDecrypter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {

    public static final String MAIL_PORT_PROPERTY_KEY = "mail.smtp.port";
    public static final String MAIL_HOST_PROPERTY_KEY = "mail.smtp.host";
    public static final String MAIL_DEBUG_PROPERTY_KEY = "mail.debug";
    
    private static final Logger LOG = Logger.getLogger(SendMail.class.getName());
    private final Properties props;
        
    public static void main(String[] args) {
        SendMail mail = new SendMail();
    // Add recipient
        String pMailTo = "anna.gerschmann@gmx.de,gerschmann@lohmann-birkner.de";
        String emailFrom ="gerschmann@lohmann-birkner.de";
        String emailPassword = "vERFgZtqpZcNdawXsuDp4J7Qln/YwOqn";
        String subject = "test";
        String content = "let's go";
        mail.sendMail(pMailTo, emailFrom, emailPassword, subject, content);

    }   
    
    public  SendMail()  throws RuntimeException{
        props = System.getProperties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");// ssl enable
        if(props.getProperty("mail.smtp.host") == null || props.getProperty("mail.smtp.port")== null ){
            throw new IllegalArgumentException("email parameter are not set");       
        }
//        props.setProperty("mail.smtp.host", "smtp.office365.com");
//        props.setProperty("mail.smtp.port", "587");
//        props.put("mail.debug", "true");
   
    }
    
    public void sendMail(final String pToMail, final String pMailFrom, final String pMailPassword, final String pSubject, 
            final String pMsg) throws RuntimeException{
        // Get the Session object
        String password = PasswordDecrypter.getInstance().decrypt(pMailPassword);
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(pMailFrom, password);
            }
        });
        

        try {
            final Message message = createMessage(pToMail, pMailFrom, pSubject, pMsg, false, session);
            // Send message
            Transport transport = session.getTransport("smtp");
            transport.connect(props.getProperty("mail.smtp.host"), pMailFrom, pMailPassword);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            LOG.log(Level.INFO, "Sent message successfully....");

        } catch (IOException | MessagingException e) {
            LOG.log(Level.SEVERE, "mail could not be sent", e);
            throw new RuntimeException(e);
        }

    }
    
    private static Message createMessage(final String pToMail, final String pMailFrom, final String pSubject, 
            final String pMsg, final boolean pHtml, Session session) throws AddressException, MessagingException, IOException {
        String msg = pMsg == null ? "" : pMsg;
        if (pHtml) {
            msg = msg.replace("\r\n", "<br>");
            msg = msg.replace("\n", "<br>");
            msg = msg.replace("\r", "<br>");
        }
 // Get the Session object

        final Message message = new MimeMessage(session);
        message.setFrom(pMailFrom == null || pMailFrom.trim().isEmpty() ? null : new InternetAddress(pMailFrom));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(pToMail));
        message.setSubject(pSubject);
        message.addHeader("X-Unsent", "1"); //Open mail in compose mode
//        message.addHeader("Disposition-Notification-To", pMailFrom);// request for confirmation
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
        // integration
        message.setContent(multipart);
        message.saveChanges();
        // store file don't need
//        final File mailFile = File.createTempFile("cpx-mail-", ".eml");
//        LOG.log(Level.INFO, "Write mail to " + mailFile.getAbsolutePath());
//        try ( FileOutputStream fos = new FileOutputStream(mailFile)) {
//            message.writeTo(fos);
//        }
        return message;
    }
}
