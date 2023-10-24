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
import de.lb.cpx.shared.lang.Lang;
import java.io.IOException;

/**
 * setUp the startuploader scene
 *
 * @author wilde
 */
public class StartupLoaderScene extends CpxScene {

//
//    @Override
//    public void setUp() {
//        
//    }
    /**
     * creates a new startup loader scene from the fxml
     *
     * @throws IOException if fxml is not found
     */
    public StartupLoaderScene() throws IOException {
        super(CpxFXMLLoader.getLoader(StartupLoaderFXMLController.class));
        setSceneTitle(Lang.getCatalogDownloadWindow_title());
    }
}
