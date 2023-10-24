/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.ruleeditor.login;

import de.lb.cpx.client.core.model.fx.login.LoginFXMLController;
import de.lb.cpx.client.core.model.fx.login.LoginScene;
import de.lb.cpx.client.ruleeditor.connection.jms.StatusBroadcastHandler;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

/**
 * Login scene for the rule editor, sets specific title and background for the
 * login avoids to mess with other implementation
 *
 * @author wilde
 */
public class ReLoginScene extends LoginScene {

    public ReLoginScene() throws IOException {
        super();
    }

    @Override
    protected String getTitle() {
        return "Regelsuite Login";
    }

    @Override
    public ImageView getBackgroundView() {
        return new ImageView("/img/menu_background_re.png");
    }

    @Override
    public Image getLogo() {
        return new WritableImage(1, 1);
    }

    public static void registerBroadcastResultListener(ChangeListener<long[]> pListener) {
        if (LoginFXMLController.isBroadcastHandlerHandlerInitialized()) {
            if (LoginFXMLController.getBroadcastHandler() instanceof StatusBroadcastHandler) {
                ((StatusBroadcastHandler) LoginFXMLController.getBroadcastHandler()).resultProperty().addListener(pListener);
            }
        }
    }

    public static void deRegisterBroadcastResultListener(ChangeListener<long[]> pListener) {
        if (LoginFXMLController.isBroadcastHandlerHandlerInitialized()) {
            if (LoginFXMLController.getBroadcastHandler() instanceof StatusBroadcastHandler) {
                ((StatusBroadcastHandler) LoginFXMLController.getBroadcastHandler()).resultProperty().removeListener(pListener);
            }
        }
    }

}
