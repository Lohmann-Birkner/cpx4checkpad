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
package de.lb.cpx.client.core.model.fx.login;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.connector.EjbConnector;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.service.ejb.AuthServiceEJBRemote;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.naming.NamingException;

/**
 *
 * @author niemeier
 */
public class NewClientConfigDialog extends FormularDialog<NewClientConfig> {

    public static final int DEFAULT_PORT = 8085;
    public static final String DEFAULT_USER = "admin";
    public static final String DEFAULT_PASSWORD = "";

    private static final Logger LOG = Logger.getLogger(NewClientConfigDialog.class.getName());

    public NewClientConfigDialog() {
        super(BasicMainApp.getStage(), Modality.APPLICATION_MODAL, "Konfigurationsdaten eingegeben");
        Stage s = (Stage) this.getDialogPane().getScene().getWindow();
        s.setMaxHeight(100);
        s.setMaxWidth(475);

        GridPane grid = new GridPane();
        final TextField hostText = new TextField();
        final TextField portText = new TextField();
        portText.setPromptText(String.valueOf(DEFAULT_PORT));
        grid.add(new Label("Server (Host oder IP)"), 0, 0);
        grid.add(hostText, 0, 1);
        grid.add(new Label("Port (Standard " + DEFAULT_PORT + ")"), 1, 0);
        grid.add(portText, 1, 1);
        grid.setHgap(5d);
        grid.setVgap(5d);

        grid.setAlignment(Pos.CENTER);

        getDialogPane().setContent(grid);

        Button openProcessBtn = getDialogSkin().getButton(ButtonType.OK);
        openProcessBtn.setText("Prüfen & Speichern");

        //Don't fu**ing close the dialog when user clicks on 'OK'
        getDialogPane().lookupButton(ButtonType.OK).addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    // to prevent the dialog to close
                    event.consume();
                    int port = DEFAULT_PORT;
                    if (!portText.getText().trim().isEmpty()) {
                        try {
                            port = Integer.valueOf(portText.getText().trim());
                        } catch (NumberFormatException ex) {
                            //LOG.log(Level.WARNING, "This is not a valid port number: " + portText.getText() + ". Use default port " + DEFAULT_PORT + " instead", ex);
                            AlertDialog dlg = AlertDialog.createErrorDialog("Dies ist kein gültiger Port: " + portText.getText().trim());
                            BasicMainApp.centerWindow(dlg.getDialogPane().getScene().getWindow());
                            dlg.showAndWait();
                            portText.requestFocus();
                            return;
                        }
                    }

                    NewClientConfig newConfig = new NewClientConfig(hostText.getText(), port, DEFAULT_USER, DEFAULT_PASSWORD);

                    EjbConnector ejbConnector = new EjbConnector();
                    boolean connectionFound = false;
                    try {
                        ejbConnector.initContexts(newConfig.host, newConfig.port, newConfig.user, newConfig.password);
                        EjbProxy<AuthServiceEJBRemote> authBean = ejbConnector.connectAuthServiceBean();
                        authBean.getWithEx();
                        connectionFound = true;
                    } catch (NamingException ex) {
                        LOG.log(Level.FINER, "Cannot establish connection to CPX server with these information: host=" + newConfig.host + ", port=" + newConfig.port + ", user=" + newConfig.user + ", password=*****", ex);
                        AlertDialog dlg = AlertDialog.createErrorDialog("Es konnte keine Verbindung zum CPX Server " + newConfig.getSocket() + " hergestellt werden!\nBitte korrigieren Sie ihre Daten!", ex);
                        dlg.setHeaderText("Verbindung fehlgeschlagen");
                        BasicMainApp.centerWindow(dlg.getDialogPane().getScene().getWindow());
                        dlg.showAndWait();
                        hostText.requestFocus();
                    } finally {
                        ejbConnector.closeContexts();
                    }

                    if (!connectionFound) {
                        return;
                    }

                    //setResult(hostText.getText(), port, DEFAULT_USER, DEFAULT_PASSWORD);
                    getResultsProperty().set(newConfig);
                }
        );
    }

//    private void setResult(final String pHost, final int pPort, final String pUser, final String pPassword) {
//        NewClientConfig newConfig = new NewClientConfig(pHost, pPort, pUser, pPassword);
//        getResultsProperty().set(newConfig);
//    }
    @Override
    public NewClientConfig onSave() {
        return null;
    }

}
