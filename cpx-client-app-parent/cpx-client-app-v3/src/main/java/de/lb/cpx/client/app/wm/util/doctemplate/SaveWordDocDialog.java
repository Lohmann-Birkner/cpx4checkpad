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
package de.lb.cpx.client.app.wm.util.doctemplate;

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.dialog.AddDocByTemplateDialog;
import de.lb.cpx.client.app.wm.util.DocumentManager;
import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmDocument;
import de.lb.cpx.wm.model.TWmProcess;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Window;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author nandola
 */
public class SaveWordDocDialog {

    private static final Logger LOG = Logger.getLogger(SaveWordDocDialog.class.getName());

    private final TemplateGenerationResult tmplGenerationResult;

    public SaveWordDocDialog(final TemplateGenerationResult pTmplGenerationResult) {
        this.tmplGenerationResult = pTmplGenerationResult;
    }
    private ObjectProperty<ButtonType> resultProperty;
    public ObjectProperty<ButtonType> resultProperty(){
        if(resultProperty == null){
            resultProperty = new SimpleObjectProperty<>();
        }
        return resultProperty;
    }
    public void setResult(ButtonType pType){
        resultProperty().set(pType);
    }
    public ButtonType getResult(){
        return resultProperty().get();
    }
    // this method will call when you close your document.
    public void showDialog(final AddDocByTemplateDialog pAddDocByTemplateDialog) {
        final TWmDocument wmDoc = tmplGenerationResult.getWmDocument();
        final File file = tmplGenerationResult.getInOutFile();
        final ProcessServiceFacade facade = tmplGenerationResult.getFacade();
        final TWmProcess process = tmplGenerationResult.getProcess();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //EjbConnector connector = Session.instance().getEjbConnector();
                //EjbProxy<ProcessServiceBeanRemote> processServiceBean = connector.connectProcessServiceBean();
                //EjbProxy<ConfigurationServiceEJBRemote> connectConfigurationServiceBean = connector.connectConfigurationServiceBean();
//                final Scene ownerScene = pAddDocByTemplateDialog.getDialogPane().getScene();
//                final ConfirmDialog cfDialog = new ConfirmDialog(ownerScene == null ? MainApp.getWindow() : ownerScene.getWindow(), Lang.getTemplateConfirmationText(file.getName(), String.valueOf(process.getWorkflowNumber())));
                final Window ownerScene = BasicMainApp.getWindow();//pAddDocByTemplateDialog.getOwner();
                final ConfirmDialog cfDialog = new ConfirmDialog(ownerScene == null ? MainApp.getWindow() : ownerScene, Lang.getTemplateConfirmationText(file.getName(), String.valueOf(process.getWorkflowNumber())));
                cfDialog.initModality(Modality.WINDOW_MODAL);
                MainApp.bringToFront();
                cfDialog.setHeaderText(Lang.getTemplateConfirmationDialog());
                cfDialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
                    @Override
                    public void accept(ButtonType t) {
                        if (t.equals(ButtonType.YES)) {
                            try {
                                final byte[] content = Files.readAllBytes(file.toPath());
                                final String extension = FilenameUtils.getExtension(file.getName());
                                facade.storeDocument(wmDoc, content, extension);
                            } catch (IOException ex) {
                                LOG.log(Level.SEVERE, "Was not able to store word document", ex);
                                MainApp.showErrorMessageDialog(ex, "Das Word-Dokument konnte nicht gespeichert werden!");
                                return;
                            }
                        }
                        DocumentManager.deleteFile(file);
                        setResult(t);
                        //deleteFile(file);
                    }
                });
            }
        });
    }

//    private void deleteFile(final File pFile) {
//        if (pFile == null) {
//            return;
//        }
//        final TemplateController templateController = new TemplateController();
//        try {
//            templateController.deleteFile(pFile);
//        } catch (IOException ex) {
//            String msg = "Was not able to delete file: " + pFile.getAbsolutePath();
//            LOG.log(Level.SEVERE, msg, ex);
////            if (pFile.exists()) {
////                //switch to fallback solution (anti-pattern because it's undeterministic!)
////                pFile.deleteOnExit();
////            }
//            MainApp.showErrorMessageDialog(ex, msg);
//        }
//    }
}
