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
package de.lb.cpx.client.core.model.fx.login;

import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.util.CpxFXMLLoader;
import de.lb.cpx.client.core.util.ResourceLoader;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author wilde
 */
public class LoginScene extends CpxScene {
//
//    @Override
//    public void setUp() {
//        
//    }

    /**
     * loads the Login scene from the fxml
     *
     * @throws IOException of fxml is not found
     */
    public LoginScene() throws IOException {
        super(CpxFXMLLoader.getLoader(LoginFXMLController.class));

        if (controller != null) {
            controller.afterShow();
        }
        setSceneTitle(getTitle());
    }

    protected String getTitle() {
        return "checkpoint x Login";
    }

    public Image getLogo() {
        return ResourceLoader.getImage("/img/cpxLogo4.png");
    }

    public ImageView getBackgroundView() {
        return new ImageView("/img/menu_background.jpg");
    }
}
