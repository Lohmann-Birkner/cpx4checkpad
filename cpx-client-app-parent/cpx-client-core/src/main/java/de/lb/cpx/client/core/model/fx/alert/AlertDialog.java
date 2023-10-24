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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.alert;

import com.jacob.com.ComFailException;
import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.section.SectionHeader;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.exceptions.CpxDisconnectedException;
import de.lb.cpx.exceptions.CpxSapException;
import de.lb.cpx.reader.exception.OfficeNotFoundException;
import de.lb.cpx.service.ejb.AuthServiceEJBRemote;
import de.lb.cpx.shared.dto.LockException;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;
import javax.ejb.EJBException;
import javax.mail.MessagingException;
import javax.persistence.PersistenceException;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 * Implementation of the AlertDialog class Provides all types of
 * alert/notification Dialogs needed
 *
 * @author wilde
 */
public class AlertDialog extends AlertBaseDialog {

    private static final Logger LOG = Logger.getLogger(AlertDialog.class.getName());

    //AWi-20171018
    //should maybe move to special alert skin, to keep ui elements and logic sperated
    //but on the otherhand the wohle concept of dialogskin needs to be refactored
//    private final GridPane grid = new GridPane();
//    private final Label labelContent;
//    private final TextArea detailsContent;

    /**
     * creates a new AlertDialog instance
     *
     * @param pAlertType type of the Dialog ,Error,Information,Warning
     * @param pHeaderText header text
     * @param pMsg text to be shown in the content area
     * @param pButtonTypes types of buttons to be set
     * @param pException error or throwable
     */
    protected AlertDialog(AlertType pAlertType, String pHeaderText, String pMsg, final Throwable pException, ButtonType... pButtonTypes) {
        this(pAlertType, pHeaderText, pMsg, "", pException, pButtonTypes);
    }
    
    /**
     * creates a new AlertDialog instance
     *
     * @param pAlertType type of the Dialog ,Error,Information,Warning
     * @param pHeaderText header text
     * @param pMsg text to be shown in the content area
     * @param pDetails Stacktrace or other details
     * @param pButtonTypes types of buttons to be set
     */
    protected AlertDialog(AlertType pAlertType, String pHeaderText, String pMsg, String pDetails, ButtonType... pButtonTypes) {
        this(pAlertType, pHeaderText, pMsg, pDetails, null, pButtonTypes);
    }
        /**
     * creates a new AlertDialog instance
     *
     * @param pAlertType type of the Dialog ,Error,Information,Warning
     * @param pHeaderText header text
     * @param pMsg text to be shown in the content area
     * @param pDetails Stacktrace or other details
     * @param pButtonTypes types of buttons to be set
     * @param pException exception or throwable
     */
    protected AlertDialog(AlertType pAlertType, String pHeaderText, String pMsg, String pDetails, final Throwable pException, ButtonType... pButtonTypes) {
        super(pAlertType, pHeaderText, pMsg, pDetails, pException, pButtonTypes);
    }
    private final static List<Class<? extends Throwable>> EXCEPTIONS_TO_CONVERT = Arrays.asList(ComFailException.class,OfficeNotFoundException.class);
    protected static Throwable convertException(final Throwable pException){
        if(EXCEPTIONS_TO_CONVERT.contains(pException.getClass())){
            Exception newEx = new Exception(new StringBuilder(pException.getClass().getName()).append(":\n").append(pException.getMessage()).toString(),pException.getCause());
            newEx.setStackTrace(pException.getStackTrace());
            return newEx;
        }
        return pException;
    }
//    /**
//     * creates a new AlertDialog instance
//     *
//     * @param pAlertType type of the Dialog ,Error,Information,Warning
//     * @param pHeaderText header text
//     * @param pMsg text to be shown in the content area
//     * @param pDetails Stacktrace or other details
//     * @param pButtonTypes types of buttons to be set
//     * @param pException exception or throwable
//     */
//    protected AlertDialog(AlertType pAlertType, String pHeaderText, String pMsg, String pDetails, final Throwable pException, ButtonType... pButtonTypes) {
//        super(pAlertType, "");
//        ClipboardEnabler.installClipboardToScene(this.getDialogPane().getScene());
//        String msg = pMsg == null ? "" : pMsg.trim();
//        Throwable ex = pException;
//        if (pException instanceof EJBException && pException.getCause() != null) {
//            ex = ex.getCause();
//        }
////        if (pException instanceof EJBException && pException.getCause() instanceof PersistenceException) {
////            ex = ex.getCause();
////        } else if (pException instanceof EJBException && pException.getCause() instanceof CpxDisconnectedException) {
////            ex = ex.getCause();
////        }
//        final boolean isLockException = ex instanceof LockException;
//        final boolean isPersistenceException = ex instanceof PersistenceException;
//        final boolean isDisconnectedException = ex instanceof CpxDisconnectedException;
//        final boolean isSapDisconnectedException = ex instanceof CpxSapException;
//        final boolean isException = ex != null && !isLockException && !isPersistenceException && !isDisconnectedException && !isSapDisconnectedException;
//        final String stacktrace;
//        if (isException) {
//            stacktrace = getStacktrace(ex);
//            if (pDetails != null && !pDetails.trim().isEmpty()) {
//                pDetails += "\r\n\r\n";
//            } else {
//                pDetails = "";
//            }
//            pDetails += stacktrace;
//        } else {
//            stacktrace = "";
//        }
//        if (isPersistenceException) {
//            Throwable c = ex;
//            while (c != null) {
//                if (c.getMessage() != null
//                        && !msg.contains(c.getMessage())) {
//                    //&& (msg.isEmpty() || !c.getMessage().contains(msg))) {
//                    if (!msg.isEmpty()) {
//                        msg += "\n";
//                    }
//                    msg += c.getMessage();
//                }
//                c = c.getCause();
//            }
//        }
//        final boolean isServerException = !stacktrace.contains("de.lb.cpx.client");
//        if (isException) {
//            pHeaderText = "Ausnahmefehler"; // (╯°□°）╯︵ ┻━┻
//        } else {
//            if (msg.isEmpty() && ex != null) {
//                msg = ex.getMessage() == null ? "" : ex.getMessage();
//            }
//        }
//        setHeaderText(pHeaderText);
//        Glyph g = getAlertIcon(pAlertType);
//        setGraphic(g);
//
//        VBox box = new VBox();
//        //GridPane lockGrid = null;
//
//        labelContent = new Label(msg);
//        labelContent.setWrapText(true);
//        //labelContent.setStyle("-fx-padding: 0 0 10 0");
//        labelContent.setPadding(new Insets(0, 0, 10, 0));
//
//        final Button sendMailBtn = new Button(Lang.getInformSupport());
//        sendMailBtn.setVisible(false);
//
//        if (isException) {
//            box.getChildren().add(grid);
//            grid.addRow(0, labelContent, sendMailBtn);
//
//            grid.getColumnConstraints().add(0, new ColumnConstraints());
//            grid.getColumnConstraints().add(1, new ColumnConstraints());
//            grid.getColumnConstraints().get(0).setHgrow(Priority.ALWAYS);
//            grid.getColumnConstraints().get(1).setHgrow(Priority.NEVER);
//            grid.getRowConstraints().add(0, new RowConstraints());
//            grid.getRowConstraints().get(0).setVgrow(Priority.ALWAYS);
//
//            getDialogPane().setContent(box);
//        } else {
//            box.getChildren().add(labelContent);
//            getDialogPane().setContent(box);
//            //getDialogPane().setContent(labelContent);
//            //labelContent.setStyle("-fx-padding: 10px");
//        }
//
//        detailsContent = pDetails == null || pDetails.trim().isEmpty() ? null : new TextArea(pDetails);
//        IssueInfo issueInfo = null;
//        CpxSystemPropertiesInterface cpxPropsServerTmp = null;
//        String serverLogTmp = "";
//        final CpxSystemPropertiesInterface cpxProps;
//
//        if (isException) {
//            cpxProps = CpxSystemProperties.getInstance();
//            issueInfo = new IssueInfo();
//            try {
//                EjbProxy<AuthServiceEJBRemote> authService = Session.instance().getEjbConnector().connectAuthServiceBean();
//                cpxPropsServerTmp = authService.get() == null ? null : authService.get().getCpxSystemProperties();
//                serverLogTmp = authService.get() == null ? null : authService.get().getServerLogLines(new Date());
//                issueInfo = new IssueInfo();
//                if (!isServerException && authService.get() != null) {
//                    ex = convertException(ex);
////                    if(ex instanceof ComFailException){
////                        Exception newEx = new Exception(new StringBuilder(ex.getClass().getName()).append(":\n").append(ex.getMessage()).toString(),ex.getCause());
////                        newEx.setStackTrace(ex.getStackTrace());
////                        ex = newEx;
////                    }
//                    authService.get().logClientMessage("Message: " + pMsg + "\r\n\r\nContext information about this issue: " + issueInfo.toString(), ex, cpxProps);
//                }
//            } catch (IOException ex2) {
//                LOG.log(Level.INFO, "Cannot retrieve log and CPX System Properties from server", ex2);
//            }
//            final Label labelIssueInfo = new Label("Let me give you some context about this issue:\n" + issueInfo.toString());
//            labelIssueInfo.setStyle("-fx-padding: 0 0 10 0");
//            labelIssueInfo.setWrapText(true);
//            labelIssueInfo.setMaxWidth(700d);
//            box.getChildren().add(labelIssueInfo);
//        } else {
//            cpxProps = null;
//            issueInfo = null;
//        }
//        final CpxSystemPropertiesInterface cpxPropsServer = cpxPropsServerTmp;
//        final String serverLog = serverLogTmp;
//        if (detailsContent != null) {
//            detailsContent.setEditable(false);
//            //detailsContent.setPrefWidth(Region.USE_COMPUTED_SIZE);
//            //detailsContent.setPrefHeight(Region.USE_COMPUTED_SIZE);
//            ScrollPane scrollPane = new ScrollPane(detailsContent);
//            scrollPane.setFitToHeight(true);
//            scrollPane.setFitToWidth(true);
//            VBox.setVgrow(scrollPane, Priority.ALWAYS);
//            scrollPane.setMinWidth(600d);
////            scrollPane.setMinHeight(300d);
//            box.getChildren().add(new SectionHeader("Details"));
//            box.getChildren().add(scrollPane);
//        }
//        if (serverLog != null && !serverLog.trim().isEmpty()) {
//            final TextArea detailsServerContent = new TextArea((cpxPropsServer == null ? "" : cpxPropsServer.toString() + "\r\n\r\n") + "[...]\r\n" + serverLog);
//            detailsServerContent.setEditable(false);
//            //detailsContent.setPrefWidth(Region.USE_COMPUTED_SIZE);
//            //detailsContent.setPrefHeight(Region.USE_COMPUTED_SIZE);
//            ScrollPane scrollPane = new ScrollPane(detailsServerContent);
//            scrollPane.setFitToHeight(true);
//            scrollPane.setFitToWidth(true);
//            VBox.setVgrow(scrollPane, Priority.ALWAYS);
//            scrollPane.setMinWidth(600d);
////            scrollPane.setMinHeight(300d);
//            box.getChildren().add(new Label("Server Log (Ausschnitt)"));
//            box.getChildren().add(scrollPane);
//        }
//
//        //final Button sendMailBtn = new Button("Send via Mail");
//        if (isException) {
//            final String msg2 = msg;
//            final String details = pDetails;
//            final IssueInfo issInfo = issueInfo;
//            sendMailBtn.setVisible(true);
//            sendMailBtn.setAlignment(Pos.CENTER_RIGHT);
//            sendMailBtn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ENVELOPE));
//            sendMailBtn.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    try {
//                        final String subject = "Es ist ein Fehler in CPX aufgetreten";
//                        final String receiverMail = "cpx_team@lohmann-birkner.de"; //later: support@lohmann-birkner.de
//                        final SendMail sendMail = new SendMail();
//                        sendMail.openExceptionDraft(receiverMail, subject, msg2, details, cpxProps, cpxPropsServer, serverLog, issInfo);
//                    } catch (IOException | MessagingException ex) {
//                        LOG.log(Level.SEVERE, "Was not able to open mail draft", ex);
//                    }
//                }
//            });
//            //box.getChildren().add(sendMailBtn);
//        }
//
//        skin = new DialogSkin<>(this);
////        skin.setMinHeight(50);
////        skin.setMinWidth(150);
//        skin.setButtonTypes(pButtonTypes);
//
//    }

    @Override
    public Node createContentNode(String pMsg, String pDetails, Throwable pException) {
        String msg = pMsg == null ? "" : pMsg.trim();
        Throwable ex = pException;
        if (pException instanceof EJBException && pException.getCause() != null) {
            ex = ex.getCause();
        }
        final boolean isLockException = ex instanceof LockException;
        final boolean isPersistenceException = ex instanceof PersistenceException;
        final boolean isDisconnectedException = ex instanceof CpxDisconnectedException;
        final boolean isSapDisconnectedException = ex instanceof CpxSapException;
        final boolean isException = ex != null && !isLockException && !isPersistenceException && !isDisconnectedException && !isSapDisconnectedException;
        final String stacktrace;
        if (isException) {
            stacktrace = getStacktrace(ex);
            if (pDetails != null && !pDetails.trim().isEmpty()) {
                pDetails += "\r\n\r\n";
            } else {
                pDetails = "";
            }
            pDetails += stacktrace;
        } else {
            stacktrace = "";
        }
        if (isPersistenceException) {
            Throwable c = ex;
            while (c != null) {
                if (c.getMessage() != null
                        && !msg.contains(c.getMessage())) {
                    //&& (msg.isEmpty() || !c.getMessage().contains(msg))) {
                    if (!msg.isEmpty()) {
                        msg += "\n";
                    }
                    msg += c.getMessage();
                }
                c = c.getCause();
            }
        }
        final boolean isServerException = !stacktrace.contains("de.lb.cpx.client");
        if (isException) {
            setHeaderText("Ausnahmefehler"); // (╯°□°）╯︵ ┻━┻
        } else {
            if (msg.isEmpty() && ex != null) {
                msg = ex.getMessage() == null ? "" : ex.getMessage();
            }
        }
        VBox box = new VBox();
        GridPane grid = new GridPane();
        //GridPane lockGrid = null;
        final Button sendMailBtn = new Button(Lang.getInformSupport());
        sendMailBtn.setVisible(false);
        getItemContainer().getChildren().clear();
        if (isException) {
            box.getChildren().add(grid);
            grid.addRow(0, getContentLabel(), sendMailBtn);

            grid.getColumnConstraints().add(0, new ColumnConstraints());
            grid.getColumnConstraints().add(1, new ColumnConstraints());
            grid.getColumnConstraints().get(0).setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().get(1).setHgrow(Priority.NEVER);
            grid.getRowConstraints().add(0, new RowConstraints());
            grid.getRowConstraints().get(0).setVgrow(Priority.ALWAYS);

//            getDialogPane().setContent(box);
        } else {
            box.getChildren().add(getContentLabel());
//            getDialogPane().setContent(box);
        }

        TextArea detailsContent = pDetails == null || pDetails.trim().isEmpty() ? null : new TextArea(pDetails);
        IssueInfo issueInfo = null;
        CpxSystemPropertiesInterface cpxPropsServerTmp = null;
        String serverLogTmp = "";
        final CpxSystemPropertiesInterface cpxProps;

        if (isException) {
            cpxProps = CpxSystemProperties.getInstance();
            issueInfo = new IssueInfo();
            try {
                EjbProxy<AuthServiceEJBRemote> authService = Session.instance().getEjbConnector().connectAuthServiceBean();
                cpxPropsServerTmp = authService.get() == null ? null : authService.get().getCpxSystemProperties();
                serverLogTmp = authService.get() == null ? null : authService.get().getServerLogLines(new Date());
                issueInfo = new IssueInfo();
                if (!isServerException && authService.get() != null) {
                    ex = convertException(ex);
//                    if(ex instanceof ComFailException){
//                        Exception newEx = new Exception(new StringBuilder(ex.getClass().getName()).append(":\n").append(ex.getMessage()).toString(),ex.getCause());
//                        newEx.setStackTrace(ex.getStackTrace());
//                        ex = newEx;
//                    }
                    authService.get().logClientMessage("Message: " + pMsg + "\r\n\r\nContext information about this issue: " + issueInfo.toString(), ex, cpxProps);
                }
            } catch (IOException ex2) {
                LOG.log(Level.INFO, "Cannot retrieve log and CPX System Properties from server", ex2);
            }
            final Label labelIssueInfo = new Label("Let me give you some context about this issue:\n" + issueInfo.toString());
            labelIssueInfo.setStyle("-fx-padding: 0 0 10 0");
            labelIssueInfo.setWrapText(true);
            labelIssueInfo.setMaxWidth(700d);
            box.getChildren().add(labelIssueInfo);
        } else {
            cpxProps = null;
            issueInfo = null;
        }
        final CpxSystemPropertiesInterface cpxPropsServer = cpxPropsServerTmp;
        final String serverLog = serverLogTmp;
        if (detailsContent != null) {
            detailsContent.setEditable(false);
            //detailsContent.setPrefWidth(Region.USE_COMPUTED_SIZE);
            //detailsContent.setPrefHeight(Region.USE_COMPUTED_SIZE);
            ScrollPane scrollPane = new ScrollPane(detailsContent);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);
            scrollPane.setMinWidth(600d);
//            scrollPane.setMinHeight(300d);
            box.getChildren().add(new SectionHeader("Details"));
            box.getChildren().add(scrollPane);
        }
        if (serverLog != null && !serverLog.trim().isEmpty()) {
            final TextArea detailsServerContent = new TextArea((cpxPropsServer == null ? "" : cpxPropsServer.toString() + "\r\n\r\n") + "[...]\r\n" + serverLog);
            detailsServerContent.setEditable(false);
            //detailsContent.setPrefWidth(Region.USE_COMPUTED_SIZE);
            //detailsContent.setPrefHeight(Region.USE_COMPUTED_SIZE);
            ScrollPane scrollPane = new ScrollPane(detailsServerContent);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);
            scrollPane.setMinWidth(600d);
//            scrollPane.setMinHeight(300d);
            box.getChildren().add(new Label("Server Log (Ausschnitt)"));
            box.getChildren().add(scrollPane);
        }

        //final Button sendMailBtn = new Button("Send via Mail");
        if (isException) {
            final String msg2 = msg;
            final String details = pDetails;
            final IssueInfo issInfo = issueInfo;
            sendMailBtn.setVisible(true);
            sendMailBtn.setAlignment(Pos.CENTER_RIGHT);
            sendMailBtn.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.ENVELOPE));
            sendMailBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        final String subject = "Es ist ein Fehler in CPX aufgetreten";
                        final String receiverMail = "cpx_team@lohmann-birkner.de"; //later: support@lohmann-birkner.de
                        final SendMail sendMail = new SendMail();
                        sendMail.openExceptionDraft(receiverMail, subject, msg2, details, cpxProps, cpxPropsServer, serverLog, issInfo);
                    } catch (IOException | MessagingException ex) {
                        LOG.log(Level.SEVERE, "Was not able to open mail draft", ex);
                    }
                }
            });
            //box.getChildren().add(sendMailBtn);
        }
        return box;
    }

    
    /**
     * creates a new none modal error dialog
     *
     * @param pMsg msg to be shown in the content area
     * @return new alert dialog
     */
    public static AlertDialog createErrorDialog(String pMsg) {
        String details = "";
        final Throwable exception = null;
        return createErrorDialog(pMsg, details, exception, ButtonType.OK);
    }

    /**
     * creates a new none modal error dialog
     *
     * @param pMsg msg to be shown in the content area
     * @param pException error or throwable
     * @return new alert dialog
     */
    public static AlertDialog createErrorDialog(String pMsg, final Throwable pException) {
        String details = "";
        return createErrorDialog(pMsg, details, pException, ButtonType.OK);
    }

    /**
     * creates a new none modal error dialog
     *
     * @param pMsg msg to be shown in the content area
     * @param pDetails Strack Trace or other details
     * @param pException error or throwable
     * @return new alert dialog
     */
    public static AlertDialog createErrorDialog(String pMsg, String pDetails, final Throwable pException) {
        return createErrorDialog(pMsg, pDetails, pException, ButtonType.OK);
    }

    /**
     * creates a new non modal error dialog, with the specified buttons
     *
     * @param pMsg text to be shown in the content area
     * @param pButtonTypes buttons to be shown
     * @return new alert dialog
     */
    public static AlertDialog createErrorDialog(String pMsg, ButtonType... pButtonTypes) {
        String details = "";
        final Throwable exception = null;
        return createErrorDialog(pMsg, details, exception, pButtonTypes);
    }

    /**
     * creates a new non modal error dialog, with the specified buttons
     *
     * @param pMsg text to be shown in the content area
     * @param pButtonTypes buttons to be shown
     * @param pException error or throwable
     * @return new alert dialog
     */
    public static AlertDialog createErrorDialog(String pMsg, final Throwable pException, ButtonType... pButtonTypes) {
        String details = "";
        return createErrorDialog(pMsg, details, pException, pButtonTypes);
    }

    /**
     * creates a new non modal error dialog, with the specified buttons
     *
     * @param pMsg text to be shown in the content area
     * @param pOwner window owner
     * @param pException error or throwable
     * @return new alert dialog
     */
    public static AlertDialog createErrorDialog(String pMsg, final Throwable pException, Window pOwner) {
        String details = "";
        AlertDialog dialog = createErrorDialog(pMsg, details, pException);
        dialog.initOwner(pOwner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        return dialog;
    }

    /**
     * creates a new non modal error dialog, with the specified buttons
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Strack Trace or other details
     * @param pButtonTypes buttons to be shown
     * @param pException exception or throwable?
     * @return new alert dialog
     */
    public static AlertDialog createErrorDialog(String pMsg, String pDetails, final Throwable pException, ButtonType... pButtonTypes) {
//        final boolean isLockException = pException != null && pException instanceof LockException;
//        if (isLockException) {
//            return LockDialog.createLockDialog(pMsg, pDetails, (LockException) pException);
//        } else {
        return new AlertDialog(AlertType.ERROR, Lang.getError(), pMsg, pDetails, pException, pButtonTypes);
//        }
    }

    /**
     * creates a new non modal warning dialog
     *
     * @param pMsg text to be shown in the content area
     * @return new alert dialog
     */
    public static AlertDialog createWarningDialog(String pMsg) {
        String details = "";
        return createWarningDialog(pMsg, details);
    }

    /**
     * creates a new non modal warning dialog
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @return new alert dialog
     */
    public static AlertDialog createWarningDialog(String pMsg, String pDetails) {
        return createWarningDialog(pMsg, pDetails, ButtonType.OK);
    }

//    /**
//    * creates a new non modal warning dialog, with the specified buttons 
//     * @param pMsg text to be shown in the content area
//     * @param pButtonTypes buttons to be shown
//     * @return new alert dialog
//     */
//    public static AlertDialog createWarningDialog(String pMsg,ButtonType... pButtonTypes){
//        return createWarningDialog(AlertType.WARNING,Lang.getWarning(),pMsg,pButtonTypes);
//    }
    /**
     * creates a new non modal information dialog, with the specified buttons
     *
     * @param pMsg text to be shown in the content area
     * @return new alert dialog
     */
    public static AlertDialog createInformationDialog(String pMsg) {
        String details = "";
        return createInformationDialog(pMsg, details, ButtonType.OK);
    }

    /**
     * creates a new non modal information dialog, with the specified buttons
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @return new alert dialog
     */
    public static AlertDialog createInformationDialog(String pMsg, String pDetails) {
        return createInformationDialog(pMsg, pDetails, ButtonType.OK);
    }

    /**
     * creates a new non modal information dialog, with the specified buttons
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Strack Trace or other details
     * @param pButtonTypes buttons to be shown
     * @return new alert dialog
     */
    public static AlertDialog createInformationDialog(String pMsg, String pDetails, ButtonType... pButtonTypes) {
        final Throwable exception = null;
        return new AlertDialog(AlertType.INFORMATION, Lang.getInformation(), pMsg, pDetails, exception, pButtonTypes);
    }

    /**
     * creates a new error Dialog with the msg,specified owner window,modiality
     * and buttons
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @param pOwner owner window of the alert dialog
     * @param pModality modality mode for the dialog(Apllication modal,Window
     * modal or none)
     * @param pButtonTypes buttons to be shown
     * @param pException error or throwable
     * @return new alert dialog
     */
    public static AlertDialog createErrorDialog(String pMsg, String pDetails, Window pOwner, Modality pModality, final Throwable pException, ButtonType... pButtonTypes) {
        AlertDialog dialog = createErrorDialog(pMsg, pDetails, pException, pButtonTypes);
        dialog.initOwner(pOwner);
        dialog.initModality(pModality);
//        dialog.setGraphic(new ImageView(dialog.getClass().getResource("/img/error32.png").toString()));
        return dialog;
    }

    /**
     * creates a new error Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pOwner owner window of the alert dialog
     * @param pButtonTypes buttons to be shown
     * @param pException error or throwable
     * @return new alert dialog
     */
    public static AlertDialog createErrorDialog(String pMsg, Window pOwner, final Throwable pException, ButtonType... pButtonTypes) {
        String details = "";
        return createErrorDialog(pMsg, details, pOwner, Modality.APPLICATION_MODAL, pException, pButtonTypes);
    }

    /**
     * creates a new error Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @param pOwner owner window of the alert dialog
     * @param pButtonTypes buttons to be shown
     * @param pException error or throwable
     * @return new alert dialog
     */
    public static AlertDialog createErrorDialog(String pMsg, String pDetails, Window pOwner, final Throwable pException, ButtonType... pButtonTypes) {
        return createErrorDialog(pMsg, pDetails, pOwner, Modality.APPLICATION_MODAL, pException, pButtonTypes);
    }

    /**
     * creates a new error Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pOwner owner window of the alert dialog
     * @return new alert dialog
     */
    public static AlertDialog createErrorDialog(String pMsg, Window pOwner) {
        String details = "";
        final Throwable exception = null;
        return createErrorDialog(pMsg, details, pOwner, exception);
    }

    /**
     * creates a new error Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pEx exception
     * @param pOwner owner window of the alert dialog
     * @return new alert dialog
     */
    public static AlertDialog createErrorDialog(Throwable pEx, Window pOwner) {
        String details = "";
        final String msg = null;
        return createErrorDialog(msg, details, pOwner, pEx);
    }

    /**
     * creates a new error Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @param pOwner owner window of the alert dialog
     * @param pException error or throwable
     * @return new alert dialog
     */
    public static AlertDialog createErrorDialog(String pMsg, String pDetails, Window pOwner, final Throwable pException) {
        return createErrorDialog(pMsg, pDetails, pOwner, Modality.APPLICATION_MODAL, pException, ButtonType.OK);
    }

    /**
     * creates a new error Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @param pOwner owner window of the alert dialog
     * @return new alert dialog
     */
    public static AlertDialog createErrorDialog(String pMsg, String pDetails, Window pOwner) {
        return createErrorDialog(pMsg, pDetails, pOwner, Modality.APPLICATION_MODAL, null, ButtonType.OK);
    }

    /**
     * creates a new warning Dialog with the msg,specified owner
     * window,modiality and buttons
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @param pOwner owner window of the alert dialog
     * @param pModality modality mode for the dialog(Apllication modal,Window
     * modal or none)
     * @param pButtonTypes buttons to be shown
     * @return new alert dialog
     */
    public static AlertDialog createWarningDialog(String pMsg, String pDetails, Window pOwner, Modality pModality, ButtonType... pButtonTypes) {
        final Throwable exception = null;
        AlertDialog dialog = new AlertDialog(AlertType.WARNING, Lang.getWarning(), pMsg, pDetails, exception, pButtonTypes);
        dialog.initOwner(pOwner);
        dialog.initModality(pModality);
        return dialog;
    }

    /**
     * creates a new warning Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pOwner owner window of the alert dialog
     * @param pButtonTypes buttons to be shown
     * @return new alert dialog
     */
    public static AlertDialog createWarningDialog(String pMsg, Window pOwner, ButtonType... pButtonTypes) {
        String details = "";
        return createWarningDialog(pMsg, details, pOwner, pButtonTypes);
    }

    /**
     * creates a new warning Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @param pOwner owner window of the alert dialog
     * @param pButtonTypes buttons to be shown
     * @return new alert dialog
     */
    public static AlertDialog createWarningDialog(String pMsg, String pDetails, Window pOwner, ButtonType... pButtonTypes) {
        return createWarningDialog(pMsg, pDetails, pOwner, Modality.WINDOW_MODAL, pButtonTypes);
    }

    /**
     * creates a new warning Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pButtonTypes buttons to be shown
     * @return new alert dialog
     */
    public static AlertDialog createWarningDialog(String pMsg, ButtonType... pButtonTypes) {
        String details = "";
        return createWarningDialog(pMsg, details, pButtonTypes);
    }

    /**
     * creates a new warning Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @param pButtonTypes buttons to be shown
     * @return new alert dialog
     */
    public static AlertDialog createWarningDialog(String pMsg, String pDetails, ButtonType... pButtonTypes) {
        return createWarningDialog(pMsg, pDetails, BasicMainApp.getWindow(), Modality.WINDOW_MODAL, pButtonTypes);
    }

    /**
     * creates a new warning Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pOwner owner window of the alert dialog
     * @return new alert dialog
     */
    public static AlertDialog createWarningDialog(String pMsg, Window pOwner) {
        String details = "";
        return createWarningDialog(pMsg, details, pOwner, ButtonType.OK);
    }

    /**
     * creates a new warning Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @param pOwner owner window of the alert dialog
     * @return new alert dialog
     */
    public static AlertDialog createWarningDialog(String pMsg, String pDetails, Window pOwner) {
        return createWarningDialog(pMsg, pDetails, pOwner, ButtonType.OK);
    }

    /**
     * creates a new information Dialog with the msg,specified owner
     * window,modiality and buttons
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @param pOwner owner window of the alert dialog
     * @param pModality modality mode for the dialog(Apllication modal,Window
     * modal or none)
     * @param pButtonTypes buttons to be shown
     * @return new alert dialog
     */
    public static AlertDialog createInformationDialog(String pMsg, String pDetails, Window pOwner, Modality pModality, ButtonType... pButtonTypes) {
        AlertDialog dialog = createInformationDialog(pMsg, pDetails, pButtonTypes);
        dialog.initOwner(pOwner);
        dialog.initModality(pModality);
        return dialog;
    }

    /**
     * creates a new information Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pOwner owner window of the alert dialog
     * @param pButtonTypes buttons to be shown
     * @return new alert dialog
     */
    public static AlertDialog createInformationDialog(String pMsg, Window pOwner, ButtonType... pButtonTypes) {
        String details = "";
        return createInformationDialog(pMsg, details, pOwner, pButtonTypes);
    }

    /**
     * creates a new information Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @param pOwner owner window of the alert dialog
     * @param pButtonTypes buttons to be shown
     * @return new alert dialog
     */
    public static AlertDialog createInformationDialog(String pMsg, String pDetails, Window pOwner, ButtonType... pButtonTypes) {
        return createInformationDialog(pMsg, pDetails, pOwner, Modality.WINDOW_MODAL, pButtonTypes);
    }

//    public void showOnTop(){
//        skin.showOnTop();
//    }
    /**
     * creates a new information Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pOwner owner window of the alert dialog
     * @return new alert dialog
     */
    public static AlertDialog createInformationDialog(String pMsg, Window pOwner) {
        String details = "";
        return createInformationDialog(pMsg, details, pOwner);
    }

    /**
     * creates a new information Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @param pOwner owner window of the alert dialog
     * @return new alert dialog
     */
    public static AlertDialog createInformationDialog(String pMsg, String pDetails, Window pOwner) {
        return createInformationDialog(pMsg, pDetails, pOwner, ButtonType.OK);
    }

    /**
     * creates new conformation Dialog with ok and cancel Button
     *
     * @param pMsg text to be shown in the content area
     * @return alert dialog
     */
    public static AlertDialog createConfirmationDialog(String pMsg) {
        String details = "";
        return createConfirmationDialog(pMsg, details, BasicMainApp.getWindow(), ButtonType.OK, ButtonType.CANCEL);
    }

    /**
     * creates new conformation Dialog with ok and cancel Button
     *
     * @param pMsg text to be shown in the content area
     * @param pOwner owner of the dialog
     * @return alert dialog
     */
    public static AlertDialog createConfirmationDialog(String pMsg, Window pOwner) {
        final Throwable exception = null;
        final String details = "";
        AlertDialog alert = new AlertDialog(AlertType.CONFIRMATION, Lang.getConformation(), pMsg, details, exception, ButtonType.OK, ButtonType.CANCEL);
        alert.initOwner(pOwner);
        alert.initModality(Modality.APPLICATION_MODAL);
        return alert;
    }

    /**
     * creates new conformation Dialog with ok and cancel Button
     *
     * @param pType alert Type
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @param pOwner owner of the dialog
     * @param pTypes button types
     * @return alert dialog
     */
    public static AlertDialog createConfirmationDialog(AlertType pType,String pMsg, String pDetails, Window pOwner, ButtonType... pTypes) {
        final Throwable exception = null;
        AlertDialog alert = new AlertDialog(AlertType.CONFIRMATION, getAlertText(pType), pMsg, pDetails, exception, pTypes);
        alert.initOwner(pOwner);
        alert.initModality(Modality.APPLICATION_MODAL);
        return alert;
    }
    
    public static AlertDialog createConfirmationDialog(String pMsg, String pDetails, Window pOwner, ButtonType... pTypes) {
        return createConfirmationDialog(AlertType.CONFIRMATION, pMsg, pDetails, pOwner, pTypes);
    }
    
    public static AlertDialog createConfirmationDialg(String pMsg, String pDetails, Window pOwener) {
        return createConfirmationDialog(pMsg, pDetails, pOwener, ButtonType.OK, ButtonType.CANCEL);
    }
    public static AlertDialog createYesNoConfirmDialog(AlertType pType,String pMsg,String pDetails, Window pOwner){
        return createConfirmationDialog(pType, pMsg, pDetails, pOwner, ButtonType.YES,ButtonType.NO);
    }
    public static String getAlertText(AlertType pAlertType){
         switch (pAlertType) {
            case CONFIRMATION:
                return Lang.getConformation();
            case INFORMATION:
                return Lang.getInformation();
            case WARNING:
                return Lang.getWarning();
            case ERROR:
                return Lang.getError();
            default:
                return "Not set";
        }
    }
    public static Glyph getAlertIcon(AlertType pAlertType) {
        final Glyph g;
        switch (pAlertType) {
            case CONFIRMATION:
                g = ResourceLoader.getGlyph(FontAwesome.Glyph.QUESTION_CIRCLE);
                if (g != null) {
                    g.setStyle("-fx-text-fill: #1569a6;");
                }
                break;
            case INFORMATION:
                g = ResourceLoader.getGlyph(FontAwesome.Glyph.INFO_CIRCLE);
                if (g != null) {
                    g.setStyle("-fx-text-fill: #1569a6;");
                }
                break;
            case WARNING:
                g = ResourceLoader.getGlyph(FontAwesome.Glyph.EXCLAMATION_TRIANGLE);
                if (g != null) {
                    g.setStyle("-fx-text-fill: #f6a117;");
                }
                break;
            case ERROR:
                g = ResourceLoader.getGlyph(FontAwesome.Glyph.EXCLAMATION_CIRCLE);
                if (g != null) {
                    g.setStyle("-fx-text-fill: #ca2424;");
                }
                break;
            default:
                //Unspecified alert type (NONE or null!)
                g = ResourceLoader.getGlyph(FontAwesome.Glyph.BELL);
                if (g != null) {
                    g.setStyle("-fx-text-fill: #1569a6;");
                }
                break;
        }
        if (g != null) {
            g.setFontSize(ALERT_ICON_SIZE);
        }
        return g;
    }

    public static Glyph getLockIcon() {
        Glyph g = ResourceLoader.getGlyph(FontAwesome.Glyph.LOCK);
        //g.setStyle("-fx-text-fill: #f6a117;");
        if (g != null) {
            g.setFontSize(ALERT_ICON_SIZE);
        }
        return g;
    }

    /**
     * Transforms an exception's stacktrace into a string representation
     *
     * @param ex error
     * @return stacktrace as a string
     */
    public static String getStacktrace(final Throwable ex) {
        if (ex == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string
        return sStackTrace;
    }
//    public File takeScreenshot(Scene pScene) throws IOException {
//        WritableImage snapshot = pScene.snapshot(null);
//        final File imgFile = File.createTempFile("cpx-error-screenshot-", ".png");
//        ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", imgFile);
//        return imgFile;
//    }
//    public File takeScreenshot(Node pNode) throws IOException {
//        WritableImage snapshot = pNode.snapshot(new SnapshotParameters(), null);
//        final File imgFile = File.createTempFile("cpx-error-screenshot-", ".png");
//        ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", imgFile);
//        return imgFile;
//    }

}
