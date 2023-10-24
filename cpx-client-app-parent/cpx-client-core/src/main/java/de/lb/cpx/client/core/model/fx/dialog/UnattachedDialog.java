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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.dialog;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.fx.dialog.event.MinimizeWindowEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Creates an Unattached Dialog Window Shows information unattached to the
 * application, dialog could be moved to background an stayes open, with no
 * callback to previous window or window state of the application!
 *
 * @author wilde
 */
public class UnattachedDialog extends TitledDialog {

    public UnattachedDialog(String pTitle) {
        super(pTitle, null, Modality.NONE, false);
        getDialogSkin().getButtonTypes().clear();
        getDialogSkin().getButtonTypes().add(ButtonType.CLOSE);
        initStyle(StageStyle.DECORATED);
        setTitle(pTitle);
        setResizable(true);
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.getIcons().addAll(BasicMainApp.getStage().getIcons());
        getDialogPane().getScene().getWindow().addEventHandler(MinimizeWindowEvent.WINDOW_MINIMIZE, new EventHandler<MinimizeWindowEvent>() {
            @Override
            public void handle(MinimizeWindowEvent event) {
                minimize();
                event.consume();
            }
        });
    }

    public void minimize() {
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.setIconified(true);
    }

}
