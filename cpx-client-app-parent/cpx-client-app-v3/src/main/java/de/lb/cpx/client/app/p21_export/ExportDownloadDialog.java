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
import de.lb.cpx.client.app.wm.util.DocumentManager;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.connection.jms.BatchTaskMessageHandler;
import de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.shared.dto.MessageDTO;
import de.lb.cpx.shared.lang.Lang;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;
import javax.batch.runtime.BatchStatus;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.controlsfx.control.Notifications;

/**
 *
 * @author niemeier
 */
public abstract class ExportDownloadDialog implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(ExportDownloadDialog.class.getName());
    private static final String KB_FORMAT = "%,.0f";

    private final BooleanProperty stopProperty = new SimpleBooleanProperty(false);
    private final ProgressBar mainProgInd;
    private final ProgressBar subProgInd;
    private final Label progLabel;
    private final ReadOnlyObjectWrapper<Boolean> finishProperty = new ReadOnlyObjectWrapper<>(null);
    private final AlertDialog dlgProgress;

    private final int maxPhases;
    private final long executionId;
    private final File targetDownloadFolder;
    private final File targetDownloadFile;
    private final String restServiceSubPath;
    private BatchTaskMessageHandler jmsHandler;
    private final boolean unzipDownloadFile;

    public ExportDownloadDialog(final long pExecutionId, final int pMaxPhases, final File pTargetDownloadFolder, final File pTargetDownloadFile, final boolean pUnzipDownloadFile, final String pRestServiceSubPath, final Window pOwner) {
        maxPhases = pMaxPhases;
        executionId = pExecutionId;
        targetDownloadFolder = pTargetDownloadFolder;
        targetDownloadFile = pTargetDownloadFile;
        restServiceSubPath = pRestServiceSubPath;
        unzipDownloadFile = pUnzipDownloadFile;

        final VBox box = new VBox();
        mainProgInd = new ProgressBar();
        subProgInd = new ProgressBar();
        progLabel = new Label();

        box.setPrefWidth(USE_COMPUTED_SIZE);
        mainProgInd.setMaxWidth(Integer.MAX_VALUE);
        subProgInd.setMaxWidth(Integer.MAX_VALUE);
        progLabel.setMaxWidth(Integer.MAX_VALUE);
        progLabel.setAlignment(Pos.CENTER);
        progLabel.setText("Export wird gestartet...");
        box.setFillWidth(true);
        box.setSpacing(5d);
        subProgInd.setVisible(false);
        box.getChildren().addAll(mainProgInd, subProgInd, progLabel);
        box.setMinWidth(450d);

        dlgProgress = AlertDialog.createInformationDialog("Einträge werden exportiert", MainApp.getWindow());
        dlgProgress.setHeaderText("Lade Daten...");
        dlgProgress.initOwner(pOwner);
        dlgProgress.initModality(Modality.WINDOW_MODAL);
        dlgProgress.getDialogPane().setContent(box);
        dlgProgress.getButtonTypes().remove(ButtonType.OK);
        dlgProgress.getButtonTypes().add(ButtonType.CANCEL);
    }

    public void start() {
        LOG.log(Level.INFO, "start monitoring export...");
        final IntegerProperty lastPhase = new SimpleIntegerProperty(0);

        jmsHandler = new BatchTaskMessageHandler();
        try {
            jmsHandler.setOnMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (message instanceof ObjectMessage) {
                                    final String execId = message.getStringProperty("ExecutionId");
                                    if (!String.valueOf(executionId).equals(execId)) {
                                        return;
                                    }
                                    ObjectMessage msg = (ObjectMessage) message;
                                    if (msg.getObject() instanceof MessageDTO) {
                                        MessageDTO dto = (MessageDTO) msg.getObject();
                                        if (lastPhase.get() < dto.getPhase()) {
                                            lastPhase.set(dto.getPhase());
                                        }
                                        if (!BatchStatus.STARTED.equals(dto.getBatchStatus())) {
//                                                            dlgProgress.close();
                                            jmsHandler.setOnMessageListener(null); //don't process any messages that come from server after this
                                            jmsHandler.close();
                                            if (BatchStatus.COMPLETED.equals(dto.getBatchStatus())) {
                                                //start download
                                                startClientThread(lastPhase.get());
                                            } else if (BatchStatus.FAILED.equals(dto.getBatchStatus())) {
                                                //it failed!
                                                Platform.runLater(() -> {
                                                    dlgProgress.close();
                                                });
                                                MainApp.showErrorMessageDialog(dto.getException(), dto.getBatchReasonForFailure(), MainApp.getWindow());
                                            } else if (BatchStatus.STOPPED.equals(dto.getBatchStatus())) {
                                                //it failed!
                                                Platform.runLater(() -> {
                                                    dlgProgress.close();
                                                });
                                            }
                                            return;
                                        }
//                                                        if (!jmsHandler.isClosed()) {
                                        progLabel.setText(Lang.getStep() + " " + dto.getPhase() + "/" + maxPhases + ": " + dto.getComment());
                                        mainProgInd.setProgress((double) dto.getPhase() / maxPhases);
                                        if (dto.getMaxSubphases() > 0d) {
                                            subProgInd.setProgress(Double.doubleToRawLongBits(dto.getMaxSubphases()) == Double.doubleToRawLongBits(0.0d) ? 0.0d : (double) dto.getSubphase() / dto.getMaxSubphases());
                                            subProgInd.setVisible(true);
                                        } else {
                                            subProgInd.setProgress(0.0d);
                                            subProgInd.setVisible(false);
                                        }
//                                                        }
                                    } else if (msg.getObject() instanceof String) {
                                        String comment = (String) msg.getObject();
                                        progLabel.setText(comment);
                                    }
                                }
                                if (message != null && !jmsHandler.isClosed()) {
                                    message.acknowledge();
                                }
                            } catch (JMSException | IllegalStateException ex) {
                                LOG.log(Level.WARNING, null, ex);
                            }
                        }
                    });
                }
            });
        } catch (JMSException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
//                        });
//                        th.start();

        final Button cancelButton = getCancelButton();
        cancelButton.addEventFilter(
                ActionEvent.ACTION,
                evt -> {
                    // to prevent the dialog to close
                    //event.consume();
                    evt.consume();
                    stopProperty.set(true);
                    if (stopExport(executionId)) {
                        dlgProgress.close();
                        return;
                    }
                    Platform.runLater(() -> {
                        progLabel.setText("Export wird abgebrochen...");
                        cancelButton.setDisable(true);
                    });
                }
        );
        dlgProgress.showAndWait();
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, null, ex);
//            MainApp.showErrorMessageDialog(ex, null, MainApp.getWindow());
//        }
        finishProperty.set(false);
//        }        
    }

    private void startClientThread(final int pLastPhase) {
        Thread th = new Thread(() -> {
            int downloadPhase = pLastPhase + 1;
            //ExportP21RestFullClient restfulClient = new ExportP21RestFullClient();
            Platform.runLater(() -> {
                mainProgInd.setProgress((double) downloadPhase / maxPhases);
                subProgInd.setProgress(0.0d);
                subProgInd.setVisible(false);
            });
            //MainApp.showInfoMessageDialog("P21 Export war erfolgreich!", getWindow());
            try ( SizeInputStream stream = new SizeInputStream()) {
                //ObjectProperty<SizeInputStream> property = new SimpleObjectProperty<>();
//                                                                property.addListener(new ChangeListener<SizeInputStream>() {
//                                                                    @Override
//                                                                    public void changed(ObservableValue<? extends SizeInputStream> ov, SizeInputStream oldStream, SizeInputStream newStream) {
                //LOG.log(Level.INFO, "TESSSSSSSSSSSST!!!");
                final ObjectProperty<File> targetFile = new SimpleObjectProperty<>();
                stream.bytesReadProperty().addListener(new ChangeListener<Number>() {

                    @Override
                    public void changed(ObservableValue<? extends Number> ov, Number oldBytesRead, Number newBytesRead) {
                        final int bytesRead = newBytesRead.intValue();
                        final int bytesTotal = stream.size();
                        //LOG.log(Level.INFO, "bytes read: " + bytesRead);
                        Platform.runLater(() -> {
                            subProgInd.setVisible(true);
                            subProgInd.setProgress((double) bytesRead / bytesTotal);
                            final double kiloBytesRead = bytesRead / 1024d;
                            final double kiloBytesTotal = bytesTotal / 1024d;
                            progLabel.setText(Lang.getStep() + " " + downloadPhase + "/" + maxPhases + ": Lade ZIP-Datei herunter: " + String.format(java.util.Locale.GERMAN, KB_FORMAT, kiloBytesRead) + "/" + String.format(java.util.Locale.GERMAN, KB_FORMAT, kiloBytesTotal) + " KB");
                            if (stopProperty.get() && !stream.isClosed()) {
                                LOG.log(Level.INFO, "Stopping download stream");
                                try {
                                    stream.close();
                                } catch (IOException ex) {
                                    LOG.log(Level.SEVERE, null, ex);
                                }
                                DocumentManager.deleteFile(targetFile.get(), true);
//                            final File file = targetFile.get();
//                            deleteFile(file);
//                            if (file != null && file.exists()) {
//                                if (!file.delete()) {
//                                    file.deleteOnExit();
//                                }
//                            }
                            }
                        });
                    }
                });
//                                                                    }
//                                                                });

                try {
                    //final File targetFolder = pSettings.getTargetFolderFile();
                    final File targetFolder = targetDownloadFolder;
                    //final File targetFile = download(executionId, targetUnzipFolder, stream);
//                final String path = "p21_export";
                    download(executionId, restServiceSubPath, targetFolder.getAbsolutePath(), stream, targetFile, stopProperty);
                    int unzipPhase = downloadPhase + 1;
                    Platform.runLater(() -> {
                        mainProgInd.setProgress((double) unzipPhase / maxPhases);
                        subProgInd.setProgress(0.0d);
                        subProgInd.setVisible(false);
                        progLabel.setText(Lang.getStep() + " " + unzipPhase + "/" + maxPhases + ": Bereite das Entpacken der Datei(en) vor");
                    });
                    ObjectProperty<MessageDTO> property = new SimpleObjectProperty<>();
                    property.addListener(new ChangeListener<MessageDTO>() {
                        @Override
                        public void changed(ObservableValue<? extends MessageDTO> ov, MessageDTO oldDto, MessageDTO newDto) {
                            Platform.runLater(() -> {
                                //mainProgInd.setProgress((double) newPhase / maxPhases);
                                subProgInd.setProgress((double) newDto.getSubphase() / newDto.getMaxSubphases());
                                subProgInd.setVisible(true);
                                progLabel.setText(Lang.getStep() + " " + unzipPhase + "/" + maxPhases + ": " + newDto.getComment());
                            });
                        }
                    });
                    if (stopProperty.get()) {
                        LOG.log(Level.INFO, "Stopping client processing of export");
                        DocumentManager.deleteFile(targetFile.get(), true);
                        return;
                    }
                    final File destDir;
                    final File selectedFileInDir;
                    final boolean isZipFile = targetFile.get().getName().toLowerCase().endsWith(".zip");
                    final int fileCount;
                    if (!isZipFile || !unzipDownloadFile) {
                        fileCount = 1;
                        if (isZipFile) {
                            LOG.log(Level.FINE, "Preserve ZIP file, will not expand it: " + targetFile.get());
                        }
                        destDir = targetFolder;
                        selectedFileInDir = targetFile.get();
                    } else {
                        LOG.log(Level.INFO, "Uncompress and delete ZIP file: " + targetFile.get() + "...");
                        List<File> newFiles = unzip(targetFile.get(), unzipPhase, property);
                        fileCount = newFiles.size();
                        File selectedFile = newFiles.isEmpty() ? null : newFiles.iterator().next();
                        destDir = selectedFile == null ? null : selectedFile.getParentFile();

                        if (selectedFile != null && targetDownloadFile != null) {
                            LOG.log(Level.INFO, "will rename file name now from {0} to {1}", new Object[]{selectedFile.getAbsolutePath(), targetDownloadFile.getAbsolutePath()});
                            if (FileUtils.deleteFile(targetDownloadFile)) {
                                LOG.log(Level.INFO, "deleted already existing file: {0}", targetDownloadFile.getAbsolutePath());
                            }
                            if (selectedFile.renameTo(targetDownloadFile)) {
                                selectedFile = targetDownloadFile;
                            }
                        }

                        selectedFileInDir = selectedFile;

                        if (stopProperty.get()) {
                            LOG.log(Level.INFO, "Stopping client processing of export");
                            //deleteFile(targetFile.get());
                            DocumentManager.deleteFile(targetFile.get(), true);
                            return;
                        }
                        int deletePhase = unzipPhase + 1;
                        Platform.runLater(() -> {
                            mainProgInd.setProgress((double) deletePhase / maxPhases);
                            subProgInd.setProgress(0.0d);
                            subProgInd.setVisible(false);
                            progLabel.setText(Lang.getStep() + " " + deletePhase + "/" + maxPhases + ": Lösche ZIP-Datei");
                        });
                        //deleteFile(targetFile.get());
                        DocumentManager.deleteFile(targetFile.get(), true);
//                    selectedFileInDir = destDir.listFiles()[0];
                    }

                    int finishingPhase = maxPhases;

                    Platform.runLater(() -> {
                        mainProgInd.setProgress((double) finishingPhase / maxPhases);
                        subProgInd.setVisible(false);
                        progLabel.setText(Lang.getStep() + " " + finishingPhase + "/" + maxPhases + ": Export erfolgreich beendet!");
                        ToolbarMenuFXMLController.openInExplorer(selectedFileInDir.getAbsolutePath());

                        //notifications.position(Pos.TOP_CENTER);
                        //notifications.darkStyle();
                        //notifications.owner(window);
                        Notifications notifications = NotificationsFactory.instance().createInformationNotification();
                        notifications.title("Export erfolgreich beendet")
                                .text("Ort: " + (fileCount == 1 ? selectedFileInDir.getAbsolutePath() : destDir.getAbsolutePath() + " (" + fileCount + " entpackte Dateien)"))
                                .onAction((event) -> {
                                    DocumentManager.showFile(selectedFileInDir);
                                    //ToolbarMenuFXMLController.openInExplorer(selectedFileInDir.getAbsolutePath());
                                });
                        notifications.show();
                    });
                    finishProperty.set(true);
                } catch (IOException ex) {
                    if (!stopProperty.get()) {
                        LOG.log(Level.SEVERE, null, ex);
                        Platform.runLater(() -> {
                            MainApp.showErrorMessageDialog(ex, null, getWindow());
                        });
                    }
                }
                Platform.runLater(() -> {
                    dlgProgress.close();
                });
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "error from closing stream", ex);
            }
        });
        th.start();
    }

    public static void download(final long executionId, final String pPath, final String targetFolder, final SizeInputStream pStream, final ObjectProperty<File> pTargetFile, final BooleanProperty pStopProperty) throws IOException {
        final String serverSocket = CpxClientConfig.instance().getServerSocket();
        final String baseUri = "http://" + serverSocket + "/cpx-p21-war/rest/p21import2";
        final Client client = ClientBuilder.newClient();
        final String fullPath = "/" + pPath + "/download/" + executionId;
        LOG.log(Level.INFO, "start download from {0}...", baseUri + fullPath);
        try {
            // change SERVER_URL, API_PATH and PATH as per REST API details
            //path = "p21_export";
            WebTarget webTarget = client.target(baseUri).path(fullPath);

            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_OCTET_STREAM);
            //invocation = invocationBuilder.buildGet();
            Response response = invocationBuilder.get();

            String contentDispositionHeader = response.getHeaderString("Content-Disposition");
            final String fileName = contentDispositionHeader
                    .substring(contentDispositionHeader.indexOf("filename=") + "filename=".length()).replace("\"", "");

            String contentLengthHeader = response.getHeaderString("Content-Length");
            final int size = Integer.parseInt(contentLengthHeader);

            InputStream responseStream = response.readEntity(InputStream.class);
            //SizeInputStream sizeInputStream = new SizeInputStream(responseStream, size);
            pStream.init(responseStream, size, pStopProperty);
            //pStream.set(sizeInputStream);

            final File path = new File(targetFolder, fileName);
            pTargetFile.set(path);
            File parent = path.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            Files.copy(pStream, path.toPath(), StandardCopyOption.REPLACE_EXISTING);
            // Set location here where you want to store downloaded file. 
            // It will replace the file if already exist in that location with same name.
            LOG.log(Level.INFO, "File was downloaded: {0}", path.getAbsolutePath());
        } finally {
            client.close();
        }
    }

    private static Map<String, Integer> getFilesInZip(final File pZipFile) throws IOException {
        final Map<String, Integer> files = new HashMap<>();
        final int kb = 10;
        byte[] buffer = new byte[kb * 1024];
        //int numberOfFiles = 0;
        try ( ZipInputStream zis = new ZipInputStream(new FileInputStream(pZipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
//                if (stopProperty.get()) {
//                    return new HashMap<>();
//                }
                int bytesTotal = 0;
                //numberOfFiles++;
                //try (FileOutputStream fos = new FileOutputStream(newFile)) {
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    //fos.write(buffer, 0, len);
                    bytesTotal += len;
                }
                //}
                files.put(zipEntry.getName(), bytesTotal);
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
        return files;
    }

    private List<File> unzip(final File pZipFile, final int pPhase, ObjectProperty<MessageDTO> pProgressProperty) throws IOException {
        //String fileZip = "src/main/resources/unzipTest/compressed.zip";
        //File destDir = new File("src/main/resources/unzipTest");
        File destDir = pZipFile.getParentFile();
        final int kb = 10;
        byte[] buffer = new byte[kb * 1024];
        int decompressedFiles = 0;
        //int numberOfFiles = getNumberOfFilesInZip(pZipFile);
        final Map<String, Integer> files = getFilesInZip(pZipFile);
        final int numberOfFiles = files.size();
        int bytesTotalAllFiles = 0;
        for (Map.Entry<String, Integer> entry : files.entrySet()) {
            bytesTotalAllFiles += entry.getValue();
        }
        int bytesWrittenAllFiles = 0;
        final List<File> newFiles = new ArrayList<>();
        long lastUpdate = 0L;

        pProgressProperty.set(new MessageDTO(pPhase, maxPhases, bytesWrittenAllFiles, bytesTotalAllFiles, BatchStatus.STARTED, null, executionId, "", numberOfFiles + " Dateien in der ZIP-Datei gefunden"));
        try ( ZipInputStream zis = new ZipInputStream(new FileInputStream(pZipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                if (stopProperty.get()) {
                    return new ArrayList<>();
                }
                decompressedFiles++;
                final String fileName = zipEntry.getName();
                final int bytesTotal = files.get(fileName);
                pProgressProperty.set(new MessageDTO(pPhase, maxPhases, bytesWrittenAllFiles, bytesTotalAllFiles, BatchStatus.STARTED, null, executionId, "", "Entpacke Datei " + decompressedFiles + "/" + numberOfFiles + " (" + fileName + ")"));
                File newFile = newFile(destDir, zipEntry);
                newFiles.add(newFile);
                try ( FileOutputStream fos = new FileOutputStream(newFile)) {
                    int bytesWritten = 0;
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                        bytesWritten += len;
                        bytesWrittenAllFiles += len;
                        final double kiloBytesWritten = bytesWritten / 1024d;
                        final double kiloBytesWrittenAllFiles = bytesWrittenAllFiles / 1024d;
                        final double kiloBytesTotal = bytesTotal / 1024d;
                        final double kiloBytesTotalAllFiles = bytesTotalAllFiles / 1024d;

                        final int updatesPerSecond = 10;
                        long now = System.currentTimeMillis() / (1000L / updatesPerSecond);
                        if (lastUpdate == 0L || lastUpdate != now) {
                            //don't update on each change. Limit to 10 updates per second
                            pProgressProperty.set(new MessageDTO(pPhase, maxPhases, (int) Math.round(kiloBytesWrittenAllFiles), (int) Math.round(kiloBytesTotalAllFiles), BatchStatus.STARTED, null, executionId, "", "Entpacke Datei " + decompressedFiles + "/" + numberOfFiles + " (" + fileName + "): " + String.format(java.util.Locale.GERMAN, KB_FORMAT, kiloBytesWritten) + "/" + String.format(java.util.Locale.GERMAN, KB_FORMAT, kiloBytesTotal) + " KB"));
                            lastUpdate = now;
                        }

                        if (stopProperty.get()) {
                            return new ArrayList<>();
                        }
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        } finally {
            if (stopProperty.get()) {
                LOG.log(Level.INFO, "Stopping decompression of files");
                for (File file : new ArrayList<>(newFiles)) {
                    //deleteFile(file);
                    DocumentManager.deleteFile(file, true);
                }
            }

        }
        return newFiles;
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public ReadOnlyObjectProperty<Boolean> finishProperty() {
        return finishProperty.getReadOnlyProperty();
    }

//    public void setMaxPhases(final int pMaxPhases) {
//        maxPhases = pMaxPhases;
//    }
//
//    public void setExecutionId(final int pExecutionId) {
//        executionId = pExecutionId;
//    }
    protected Button getCancelButton() {
        return (Button) dlgProgress.getDialogPane().lookupButton(ButtonType.CANCEL);
    }

    public abstract boolean stopExport(long pExecutionId);

    public abstract Window getWindow();

    @Override
    public void close() throws Exception {
        if (jmsHandler != null) {
            jmsHandler.close();
        }
    }

}
