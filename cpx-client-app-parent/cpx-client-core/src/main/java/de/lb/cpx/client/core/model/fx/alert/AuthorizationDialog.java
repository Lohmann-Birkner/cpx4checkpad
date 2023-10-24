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
 *    2019  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.alert;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.fx.dialog.DialogSkin;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import de.lb.cpx.service.properties.CpxAuthorizationException;
import javafx.scene.control.Alert;
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
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author shahin
 */
public class AuthorizationDialog extends Alert {

    private final DialogSkin<ButtonType> skin;
    private final GridPane grid = new GridPane();
    private final Label lblContent;
    private final TextArea taDetailsContent;

    /**
     * creates a new none modal error Authorization dialog
     *
     * @param pMsg msg to be shown in the content area
     * @return new alert dialog
     */
    public static AuthorizationDialog createAuthorizationDialog(final String pMsg) {
        String details = "";
        final CpxAuthorizationException exception = null;
        return createAuthorizationDialog(pMsg, details, exception);
    }

    /**
     * creates a new none modal error Authorization dialog
     *
     * @param pMsg msg to be shown in the content area
     * @param pException error or CpxAuthorizationException
     * @return new alert dialog
     */
    public static AuthorizationDialog createAuthorizationDialog(final String pMsg, final CpxAuthorizationException pException) {
        String details = "";
        return createAuthorizationDialog(pMsg, details, pException);
    }

    /**
     * creates a new none modal error Authorization dialog
     *
     * @param pMsg msg to be shown in the content area
     * @param pDetails Strack Trace or other details
     * @param pException error or CpxAuthorizationException
     * @return new alert dialog
     */
    public static AuthorizationDialog createAuthorizationDialog(final String pMsg, final String pDetails,
            final CpxAuthorizationException pException) {
        return createAuthorizationDialog(pMsg, pDetails, BasicMainApp.getWindow(), pException);
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
     * @param pException error or CpxAuthorizationException
     * @return new alert dialog
     */
    public static AuthorizationDialog createAuthorizationDialog(final String pMsg, final String pDetails,
            final Window pOwner, final Modality pModality, final CpxAuthorizationException pException) {
        AuthorizationDialog dialog = new AuthorizationDialog(pMsg, pDetails, pException);
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
     * @param pException CpxAuthorizationException
     * @return new alert dialog
     */
    public static AuthorizationDialog createAuthorizationDialog(final String pMsg, final Window pOwner, final CpxAuthorizationException pException) {
        String details = "";
        return createAuthorizationDialog(pMsg, details, pOwner, Modality.APPLICATION_MODAL, pException);
    }

    /**
     * creates a new error Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pOwner owner window of the alert dialog
     * @return new alert dialog
     */
    public static AuthorizationDialog createAuthorizationDialog(final String pMsg, final Window pOwner) {
        String details = "";
        final CpxAuthorizationException exception = null;
        return createAuthorizationDialog(pMsg, details, pOwner, exception);
    }

    /**
     * creates a new error Dialog with the msg,specified owner window and
     * buttons Dialog is set to Window Modal
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stack Trace or other details
     * @param pOwner owner window of the alert dialog
     * @param pException error or CpxAuthorizationException
     * @return new alert dialog
     */
    public static AuthorizationDialog createAuthorizationDialog(String pMsg, String pDetails, Window pOwner, final CpxAuthorizationException pException) {
        return createAuthorizationDialog(pMsg, pDetails, pOwner, Modality.APPLICATION_MODAL, pException);
    }

    /**
     * creates a new AlertDialog instance
     *
     * @param pMsg text to be shown in the content area
     * @param pDetails Stacktrace or other details
     * @param pException CpxAuthorizationException
     */
    public AuthorizationDialog(final String pMsg, final String pDetails, final CpxAuthorizationException pException) {
        super(AlertType.ERROR, "");
        ClipboardEnabler.installClipboardToScene(this.getDialogPane().getScene());
        setHeaderText("Berechtigung Hinweis");
        Glyph g = getAuthorizationIcon();
        setGraphic(g);
        VBox box = new VBox();
        lblContent = new Label(pMsg);
        lblContent.setWrapText(true);
        lblContent.setStyle("-fx-padding: 0 0 10 0");
        if ((lblContent.getText() != null && !lblContent.getText().trim().isEmpty())) {
            box.getChildren().add(grid);
            grid.addRow(0, lblContent);
        }
        taDetailsContent = pDetails == null || pDetails.trim().isEmpty() ? null : new TextArea(pDetails);
        grid.getColumnConstraints().add(0, new ColumnConstraints());
        grid.getColumnConstraints().add(1, new ColumnConstraints());
        grid.getColumnConstraints().get(0).setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().get(1).setHgrow(Priority.NEVER);
        grid.getRowConstraints().add(0, new RowConstraints());
        grid.getRowConstraints().get(0).setVgrow(Priority.ALWAYS);

        if (taDetailsContent != null) {
            taDetailsContent.setEditable(false);
            ScrollPane scrollPane = new ScrollPane(taDetailsContent);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);
            scrollPane.setMinWidth(600d);
            scrollPane.setMinHeight(300d);
            box.getChildren().add(new Label("Details"));
            box.getChildren().add(scrollPane);
        }

        getDialogPane().setContent(box);
        skin = new DialogSkin<>(this);
        skin.setMinHeight(50);
//        skin.getButtonTypes().clear();
//        skin.getButtonTypes().addAll(ButtonType.OK);
        skin.setMinWidth(150);
    }

    public static Glyph getAuthorizationIcon() {
        Glyph icon = ResourceLoader.getGlyph(FontAwesome.Glyph.SHIELD);
        icon.setFontSize(50d);
        return icon;
    }

}
