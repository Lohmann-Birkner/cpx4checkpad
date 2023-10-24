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
package de.lb.cpx.client.ruleeditor;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.checkpoint.enums.AppTypeEn;
import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.fx.login.LoginFXMLController;
import de.lb.cpx.client.core.model.fx.login.StartupLoaderFXMLController;
import de.lb.cpx.client.ruleeditor.connection.jms.StatusBroadcastHandler;
import de.lb.cpx.client.ruleeditor.login.ReLoginScene;
import de.lb.cpx.client.ruleeditor.menu.ReToolbarMenuScene;
import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.shared.json.RuleTableMessageBuilder;
import de.lb.cpx.shared.json.RuleTableMessageReader;
import de.lb.cpx.shared.json.enums.MessageReasonEn;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
        setType(AppTypeEn.RULE_EDITOR);
        setDefaultLocale();
        initApp();
        try { //test only
            String json = new RuleTableMessageBuilder().setCodes("test,test").setDescription("test").setSeverity("false").setType("test").setCodes("testing,testing").setReason(MessageReasonEn.VALIDATION_NO_VALUE)
                    .add().setCodes("test2,test2").setDescription("test2").setSeverity("true").setType("test2")
                    .build();
            new RuleTableMessageReader().read(json);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
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
//        StartupLoaderFXMLController.setAllowedCatalogType(CatalogTypeEn.OPS, CatalogTypeEn.ICD,
//                CatalogTypeEn.OPS_THESAURUS, CatalogTypeEn.ICD_THESAURUS,
//                CatalogTypeEn.DRG, CatalogTypeEn.PEPP,
//                CatalogTypeEn.DEPARTMENT);
        StartupLoaderFXMLController.setToolbarMenuSceneClass(ReToolbarMenuScene.class);
        LoginFXMLController.setStatusBroadcastHandlerClass(StatusBroadcastHandler.class);
//        StartupLoaderFXMLController.setCpxHandleManagerClass(BasicCpxHandleManager.class);
        launch(args);
    }

    public static ReToolbarMenuScene getToolbarMenuScene() {
        if (!(getStage().getScene() instanceof ReToolbarMenuScene)) {
            return null;
        }
        return (ReToolbarMenuScene) getStage().getScene();
    }

    @Override
    public synchronized void start(Stage pStage) throws Exception {
        INSTANCE = this;
        stage = pStage;
//        stage.initStyle(StageStyle.UTILITY);
        stage.setMaximized(true);
        stage.setScene(new Scene(new AnchorPane(), 300, 300, Color.WHITE));
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    stop();
                } catch (NamingException ex) {
                    LOG.get().log(Level.SEVERE, null, ex);
                }
            }
        });
        setStageTitleBinding(STAGE_TITLE_PROPERTY);
//        setStageIcon("/img/cpxLogo4_.png");
        try {
            ReLoginScene login = new ReLoginScene();
            setScene(login);
            stage.show();
        } catch (IOException ex) {
            LOG.get().log(Level.SEVERE, null, ex);
        }
        setStageIcon("/img/cpxLogo4_re.png");
    }

    @Override
    public void stop() throws NamingException {
        getStage().hide(); //waits for document import tasks in background
        ReToolbarMenuScene toolbar = getToolbarMenuScene();
        if (toolbar != null) {
            toolbar.cleanUp();
        }
        super.stop();
    }

}
