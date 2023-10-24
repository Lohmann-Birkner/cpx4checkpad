/* 
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.p21_export;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.menu.fx.filterlists.cases.WorkingListScene;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.dialog.TitledDialog;
import de.lb.cpx.service.ejb.WorkingListStatelessEJBRemote;
import de.lb.cpx.service.properties.CpxAuthorizationException;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.p21util.P21ExportSettings;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Window;

/**
 *
 * @author niemeier
 */
public abstract class P21ExportDialog extends TitledDialog {

    private static final Logger LOG = Logger.getLogger(P21ExportDialog.class.getName());

    private P21ExportScene scene;

//    public ReadOnlyObjectProperty<Boolean> finishProperty() {
//        return finishProperty.getReadOnlyProperty();
//    }
    public P21ExportDialog(Window pOwner) {
        super("P21-Export");
        initOwner(pOwner);
        initModality(Modality.WINDOW_MODAL);
//        super();
//        initOwner(pOwner);
//        initModality(pModality);
//        setHeaderText(pTitle);
        try {
//            setDialogSkin(new DialogSkin(this));
            scene = new P21ExportScene();
            scene.getController().init();
//            setDialogSkin(new DialogSkin(this));
            getDialogPane().setContent(scene.getRoot());
//            getDialogPane().heightProperty().addListener(new ChangeListener<Number>() {
//                @Override
//                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                    getDialogPane().getScene().getWindow().sizeToScene();
//                }
//            });
//            getDialogPane().prefHeightProperty().bind(getDialogPane().getScene().heightProperty());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        dialogSkinProperty.get().addButtonTypes(ButtonType.OK);

        P21ExportFXMLController ctrl = scene.getController();

        Node okButton = getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(ctrl.invalidProperty());
        okButton.addEventFilter(
                ActionEvent.ACTION,
                evt -> {
                    evt.consume();
                    start();
                });

//        finishProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
//                if (newValue != null && newValue) {
//                    //true -> export was successful!
//                    Platform.runLater(() -> {
//                        close();
//                    });
//                }
//            }
//        });
//        showAndWait().ifPresent(new Consumer<ButtonType>() {
//            @Override
//            public void accept(ButtonType t) {
//                if (t.equals(ButtonType.OK)) {
//
//                }
//            }
//        });
        //getDialogSkin().addButtonTypes(ButtonType.OK);
    }

    private Window getWindow() {
        return (this.getDialogPane() == null || this.getDialogPane().getScene() == null || this.getDialogPane().getScene().getWindow() == null)
                ? MainApp.getWindow() : this.getDialogPane().getScene().getWindow();
    }

//    private void reset() {
//        finishProperty.set(null);
//        stopProperty.set(false);
//    }
    public abstract long[] getSelectedIds();

    public void start() {
        if (!Session.instance().isExportDataAllowed()) {
            MainApp.showAuthorizationMessage(new CpxAuthorizationException(Session.instance().getRoleProperties(), Lang.getAuthorizationDialogMessage(Session.instance().getRoleProperties() == null ? "---" : Session.instance().getRoleProperties().getName(), "Daten Export") + "\n" + Lang.getAuthorizationDialogMessageContact()));
        } else {
            P21ExportFXMLController ctrl = scene.getController();
            final P21ExportSettings settings = ctrl.getP21ExportSettings();
            CpxClientConfig.instance().setUserP21ExportSettings(settings);

            final BooleanProperty dlgResult = new SimpleBooleanProperty(true);
            final File targetFolderFile = settings.getTargetFolderFile();
            final String targetFolder = targetFolderFile.getAbsolutePath();
            if (!targetFolderFile.exists()) {
                ConfirmDialog dlg = new ConfirmDialog(getWindow(), "Das angegebene Zielverzeichnis existiert nicht:\n" + targetFolder + "\n\nSoll es jetzt angelegt werden?");
                dlg.showAndWait().ifPresent(new Consumer<ButtonType>() {
                    @Override
                    public void accept(ButtonType t) {
                        if (t.equals(ButtonType.YES)) {
                            if (!targetFolderFile.exists() && !targetFolderFile.mkdirs()) {
                                MainApp.showErrorMessageDialog("Das Verzeichnis konnte nicht angelegt werden\n: " + targetFolder);
                                dlgResult.set(false);
                            }
                        } else {
                            dlgResult.set(false);
                        }
                    }
                });
            }
            if (!dlgResult.get()) {
                return;
            }
            if (!targetFolderFile.canWrite() || !targetFolderFile.canRead() || !Files.isWritable(targetFolderFile.toPath())) {
                MainApp.showErrorMessageDialog("Keine Schreib-/Leserechte auf das Verzeichnis: " + targetFolder);
                return;
            }

            WorkingListScene workingListScene = MainApp.getToolbarMenuScene().getWorkingList();
            if (workingListScene == null) {
                LOG.log(Level.WARNING, "Working list is not initialized, cannot start P21 export");
//                finishProperty.set(false);
                return;
            }
            WorkingListStatelessEJBRemote workingListServiceBean = Session.instance().getEjbConnector().connectWorkingListBean().get();
            GDRGModel grouperModel = CpxClientConfig.instance().getSelectedGrouper();
            boolean isLocal = Session.instance().isCaseLocal();
            //final long token = System.currentTimeMillis();
            LOG.log(Level.INFO, "start P21 export...");

            try {
                final int maxPhases = workingListServiceBean.getMaxPhases() + 4;
                final long[] selectedIds = getSelectedIds();
                final long executionId = workingListServiceBean.prepareP21Export(isLocal, grouperModel, settings, selectedIds, new HashMap<>(workingListScene.getFilterManager().getFilterOptionMap()));

                final String restServiceSubPath = "p21_export";
                final boolean unzipDownloadFile = !settings.isZip();
                final File targetFile = null; //does not make sense here, because multiple files are returned from server
                try (final ExportDownloadDialog exportDownloadDlg = new ExportDownloadDialog(executionId, maxPhases, settings.getTargetFolderFile(), targetFile, unzipDownloadFile, restServiceSubPath, scene.getWindow()) {
                    @Override
                    public boolean stopExport(long pExecutionId) {
                        return workingListServiceBean.stopExport(executionId);
                    }

                    @Override
                    public Window getWindow() {
                        return P21ExportDialog.this.getWindow();
                    }
                }) {

                    exportDownloadDlg.finishProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                            if (newValue != null && newValue) {
                                //true -> export was successful!
                                Platform.runLater(() -> {
                                    close();
                                });
                            }
                        }
                    });

                    exportDownloadDlg.start();
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
                MainApp.showErrorMessageDialog(ex, "problem occured when I tried to start P21 export on server", getWindow());
            }
        }
    }

}
