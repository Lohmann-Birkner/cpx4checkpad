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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.alert;

import de.lb.cpx.client.core.model.fx.dialog.DialogSkin;
import javafx.concurrent.Worker;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.controlsfx.dialog.ProgressDialog;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author nandola
 */
public class ProgressWaitingDialog extends ProgressDialog {

    private final DialogSkin<Void> skin;

    public ProgressWaitingDialog(Worker<?> worker) {
        super(worker);
        skin = new DialogSkin<>(this);
        skin.removeButton(ButtonType.CANCEL);
        skin.setMinHeight(50);
        skin.setMinWidth(150);

        //2018-05-28 DNi - Ticket #CPX-924: Removing ControlsFX classes to use our custom alert icons
        getDialogPane().getStyleClass().remove("progress-dialog");
        getDialogPane().getStyleClass().remove("font-selector-dialog");
        getDialogPane().getStyleClass().remove("login-dialog");
        getDialogPane().getStyleClass().remove("command-links-dialog");
        getDialogPane().getStyleClass().remove("exception-dialog");

        //2018-05-28 DNi: Now insert our custom notice icon
        Glyph g = AlertDialog.getAlertIcon(Alert.AlertType.INFORMATION);
        setGraphic(g);
    }

    /**
     * @return Skin of the dialog
     */
    public DialogSkin<Void> getSkin() {
        return skin;
    }

}
