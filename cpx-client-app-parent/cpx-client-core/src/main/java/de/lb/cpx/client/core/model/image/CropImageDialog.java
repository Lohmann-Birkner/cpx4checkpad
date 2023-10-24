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
 *    2017  adameck - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.image;

import de.lb.cpx.client.core.model.fx.dialog.DialogSkin;
import de.lb.cpx.client.core.model.fx.dialog.TitledDialog;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author adameck
 */
public class CropImageDialog extends TitledDialog {

    private CropImageScene scene;

    public CropImageDialog(Window pOwner, Modality pModality, String pTitle) {
        super(pTitle, pOwner, pModality);
        try {
            scene = new CropImageScene();
            getDialogPane().setContent(scene.getRoot());
            Stage s = (Stage) this.getDialogPane().getScene().getWindow();
//            s.setMinHeight(650);
            s.setMinWidth(750);
            s.setMaxWidth(750);
            setDialogSkin(new DialogSkin<>(this));
        } catch (IOException ex) {
            Logger.getLogger(CropImageDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CropImageScene getScene() {
        return scene;
    }

}
