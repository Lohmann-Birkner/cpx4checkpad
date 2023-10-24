/* 
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core;

import de.checkpoint.enums.AppTypeEn;
import de.lb.cpx.client.core.connection.jms.StatusBroadcastHandler;
import de.lb.cpx.client.core.menu.fx.ToolbarMenuScene;
import de.lb.cpx.client.core.model.fx.login.LoginFXMLController;
import de.lb.cpx.client.core.model.fx.login.StartupLoaderFXMLController;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.util.logging.Level;

/**
 *
 * @author niemeier
 */
public class MainApp extends BasicMainApp {

//    private static final Logger LOG;
    public MainApp() {
        super(/* true, */"Build Version not implemented in CPX Core");
    }

    //2018-02-23 DNi - Ticket #CPX-841: Load logging.properties
    static {
        setType(AppTypeEn.CORE);
        setDefaultLocale();
        initApp();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CpxSystemPropertiesInterface cpxProps = CpxSystemProperties.getInstance();
        if (cpxProps != null) {
            LOG.get().log(Level.INFO, cpxProps.toString()
                    + "\n=========================================================");
        }
        StartupLoaderFXMLController.setIgnoreAllCatalogTypes();
        StartupLoaderFXMLController.setToolbarMenuSceneClass(ToolbarMenuScene.class);
        LoginFXMLController.setStatusBroadcastHandlerClass(StatusBroadcastHandler.class);
        launch(args);
    }

}
