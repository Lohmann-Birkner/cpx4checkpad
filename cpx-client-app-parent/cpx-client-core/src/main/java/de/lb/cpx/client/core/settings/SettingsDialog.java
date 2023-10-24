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
 *    2017  adameck - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.settings;

import de.lb.cpx.client.core.model.fx.dialog.TitledDialog;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Window;

/**
 *
 * @author adameck
 */
public class SettingsDialog extends TitledDialog {

    private SettingsDialogScene contentScene;

    public SettingsDialog(Window pOwner, Modality pModality, String pTitle, SettingsDialogScene pScene) {
        this(pTitle, pOwner, pModality);
        getDialogPane().setContent(pScene.getRoot());
    }

    public SettingsDialog(String pTitle, Window pOwner, Modality pModality) {
        super(pTitle, pOwner, pModality);
//        super("Einstellungen",pOwner, Modality.APPLICATION_MODAL);
        dialogSkinProperty.get().setButtonTypes(ButtonType.CLOSE);
    }

    public SettingsDialog(Window pOwner) {
        this("Einstellungen", pOwner, Modality.APPLICATION_MODAL);
        try {
//            setDialogSkin(new DialogSkin(this));
            contentScene = new SettingsDialogScene();
//            setDialogSkin(new DialogSkin(this));
            getDialogPane().setContent(contentScene.getRoot());

            dialogSkinProperty.get().addButtonTypes(ButtonType.OK);
        } catch (IOException ex) {
            Logger.getLogger(SettingsDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void selectCategory(String pCategory) {
        if(contentScene == null){
            return;
        }
        contentScene.selectCategory(pCategory);
    }
}
