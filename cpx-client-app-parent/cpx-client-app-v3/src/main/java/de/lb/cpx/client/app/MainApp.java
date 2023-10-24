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
package de.lb.cpx.client.app;

import de.checkpoint.enums.AppTypeEn;
import de.lb.cpx.client.app.connection.jms.StatusBroadcastHandler;
import de.lb.cpx.client.app.menu.fx.ToolbarMenuScene;
import de.lb.cpx.client.app.util.cpx_handler.CpxHandleManager;
import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.fx.login.LoginFXMLController;
import de.lb.cpx.client.core.model.fx.login.StartupLoaderFXMLController;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.util.logging.Level;
import static javafx.application.Application.launch;
import javax.naming.NamingException;

/**
 * BasicMainApp class for the Application, handles basic stuff regarding its
 * stage initialize content on start and shutdown on stop TODO: Maybe put stage
 * elsewhere in the client, maybe some static client class is better suited,
 * also for refactoring client properties handling
 *
 * @author wilde
 */
public class MainApp extends BasicMainApp {

    static {
        setType(AppTypeEn.CLIENT);
        setDefaultLocale();
        initApp();
    }

    public MainApp() {
        super(/* false, */readApiVersion());
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
        StartupLoaderFXMLController.setToolbarMenuSceneClass(ToolbarMenuScene.class);
        StartupLoaderFXMLController.setCpxHandleManagerClass(CpxHandleManager.class);
        LoginFXMLController.setStatusBroadcastHandlerClass(StatusBroadcastHandler.class);
        launch(args);
    }

    public static ToolbarMenuScene getToolbarMenuScene() {
        if (!(getStage().getScene() instanceof ToolbarMenuScene)) {
            return null;
        }
        return (ToolbarMenuScene) getStage().getScene();
    }

    @Override
    public void stop() throws NamingException {
        getStage().hide(); //waits for document import tasks in background
        ToolbarMenuScene toolbar = getToolbarMenuScene();
        if (toolbar != null) {
            toolbar.cleanUp();
        }
        super.stop();
    }

}
