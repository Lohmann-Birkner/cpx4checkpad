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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.p21_export;

import de.FileUtils;
import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.core.model.fx.file_chooser.FileChooserFactory;
import de.lb.cpx.model.enums.ExportTypeEn;
import de.lb.cpx.model.enums.SearchListTypeEn;
import java.io.File;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 *
 * @author niemeier
 */
public abstract class ListExportDialog {

    private static final Logger LOG = Logger.getLogger(ListExportDialog.class.getName());

//    private P21ExportScene scene;
    public void start(final SearchListTypeEn pListType, final ExportTypeEn pExportType) {
//        if (!Session.instance().isExportDataAllowed()) {
//            MainApp.showAuthorizationMessage(new CpxAuthorizationException(Session.instance().getRoleProperties(), Lang.getAuthorizationDialogMessage(Session.instance().getRoleProperties() == null ? "---" : Session.instance().getRoleProperties().getName(), "Daten Export") + "\n" + Lang.getAuthorizationDialogMessageContact()));
//        } else {
//            CpxClientConfig.instance().setUserP21ExportSettings(settings);

        final BooleanProperty dlgResult = new SimpleBooleanProperty(true);
        FileChooser fileChooser = FileChooserFactory.instance().createFileChooser();
        final String fileExtension = pExportType.getFileExtension();
        List<String> supportedFileTypes = Arrays.asList("*." + fileExtension);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileExtension, supportedFileTypes));
        //fileChooser.setTitle(title);
        fileChooser.setInitialFileName(pListType.getFileName() + "." + fileExtension);
        Window window = MainApp.getWindow();
        File file;
        while (true) {
            file = fileChooser.showSaveDialog(window);
            if (file != null && FileUtils.isFileLock(file)) {
                MainApp.showErrorMessageDialog(MessageFormat.format("Die Datei {0} ist bereits im Zugriff.\n\nSchließen Sie zunächst die offene Anwendung oder wählen Sie einen anderen Dateinamen aus", file.getAbsolutePath()));
            } else {
                break;
            }
        }
        if (file == null) {
            dlgResult.set(false);
            return;
        }
//        DirectoryChooser dirChooser = FileChooserFactory.instance().createDirectoryChooser();
//        dirChooser.setTitle("Verzeichnis auswählen");
//        File targetFolderDir = dirChooser.showDialog(MainApp.getWindow());
//        if (targetFolderDir == null) {
//            return;
//        }
//        CpxClientConfig.instance().setUserRecentFileChooserPath(targetFolderDir);
//            final File targetFolderFile = settings.getTargetFolderFile();
//        final String targetFolder = targetFolderDir.getAbsolutePath();
//        if (!targetFolderDir.exists()) {
//            ConfirmDialog dlg = new ConfirmDialog(MainApp.getWindow(), "Das angegebene Zielverzeichnis existiert nicht:\n" + targetFolder + "\n\nSoll es jetzt angelegt werden?");
//            dlg.showAndWait().ifPresent(new Consumer<ButtonType>() {
//                @Override
//                public void accept(ButtonType t) {
//                    if (t.equals(ButtonType.YES)) {
//                        if (!targetFolderDir.exists() && !targetFolderDir.mkdirs()) {
//                            MainApp.showErrorMessageDialog("Das Verzeichnis konnte nicht angelegt werden\n: " + targetFolder);
//                            dlgResult.set(false);
//                        }
//                    } else {
//                        dlgResult.set(false);
//                    }
//                }
//            });
//        }
//        if (!dlgResult.get()) {
//            return;
//        }
        if (file.exists() && (!file.canWrite() || !Files.isWritable(file.toPath()))) {
            MainApp.showErrorMessageDialog("Keine Schreib-/Leserechte um die Datei erstellen: " + file);
            return;
        }

        LOG.log(Level.INFO, "start list export...");
        //Node cancelButton = dlgProgress.getDialogPane().lookupButton(ButtonType.CANCEL);
        final int maxPhases = 8 + 4; //workingListServiceBean.getMaxPhases() + 4;
        final long executionId = prepareExport();

        final String restServiceSubPath = "list_export";
        final boolean unzipDownloadFile = true;
        try (final ExportDownloadDialog exportDownloadDlg = new ExportDownloadDialog(executionId, maxPhases, file.getParentFile(), file, unzipDownloadFile, restServiceSubPath, MainApp.getWindow()) {
            @Override
            public boolean stopExport(long pExecutionId) {
                return ListExportDialog.this.stopExport(pExecutionId);
            }

            @Override
            public Window getWindow() {
                return MainApp.getWindow();
            }
        }) {
            exportDownloadDlg.start();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public abstract long prepareExport();

    public abstract boolean stopExport(long pExecutionId);

}
