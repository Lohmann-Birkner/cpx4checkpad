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
package de.lb.cpx.client.core.menu.fx.dialog;

import com.sun.management.OperatingSystemMXBean;
import de.lb.cpx.client.core.InitLogger;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController;
import static de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController.headerLabel;
import static de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController.infoLabel;
import static de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController.keyLabel;
import de.lb.cpx.client.core.model.fx.alert.IssueInfo;
import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import de.lb.cpx.client.core.model.fx.alert.SendMail;
import de.lb.cpx.client.core.model.fx.dialog.TitledDialog;
import de.lb.cpx.client.core.util.ResourceLoader;
import de.lb.cpx.connector.EjbProxy;
import de.lb.cpx.reader.DocumentReader;
import de.lb.cpx.service.ejb.AuthServiceEJBRemote;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.version.Version;
import de.lb.cpx.shared.version.VersionHistory;
import de.lb.cpx.system.properties.CpxSystemProperties;
import de.lb.cpx.system.properties.CpxSystemPropertiesInterface;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javax.mail.MessagingException;
import org.controlsfx.control.Notifications;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 */
public class SystemInfoDialog extends TitledDialog {

    private static final Logger LOG = Logger.getLogger(SystemInfoDialog.class.getName());
    private static SystemInfoDialog dialogInstance = null;
    private final CpxSystemPropertiesInterface cpxProps;

    public static synchronized void showDialog() {
        if (dialogInstance == null) {
            dialogInstance = new SystemInfoDialog();
        } else {
            dialogInstance.getDialogSkin().centerWindow();
        }
        dialogInstance.show();
    }

    /**
     * construct new instance
     *
     */
    private SystemInfoDialog() {
        super("System-Informationen", MainApp.getWindow(), false);
        //setModality(Modality.NONE);
        getDialogPane().setMinWidth(630D);
        GridPane gpInfo = new GridPane();
        EjbProxy<AuthServiceEJBRemote> authServiceEjb = Session.instance().getEjbConnector().connectAuthServiceBean();
        final Version serverVersion = authServiceEjb.get().getVersion();
        final Version clientVersion = VersionHistory.getRecentVersion();
        final boolean versionMatch = serverVersion.equals(clientVersion);
        cpxProps = CpxSystemProperties.getInstance();

        int rowIndex = 0;

        gpInfo.add(headerLabel("Version & System"), 0, rowIndex);

        gpInfo.add(keyLabel("CPX-Version"), 0, ++rowIndex);
        gpInfo.add(infoLabel(clientVersion.getVersion() + (clientVersion.getCodeName().isEmpty() ? "" : " (" + clientVersion.getCodeName() + ")") + " vom " + clientVersion.getDateGerman() + ", " + MainApp.instance().getVersion() /* + (versionMatch ? "" : " (Server Version: " + serverVersion.getVersion() + ")") */, null), 1, rowIndex);

        if (!versionMatch) {
            final Label serverVersionLabel = infoLabel("Version auf dem Server ist " + serverVersion.getVersion(), "Bitte stellen Sie sicher, dass Server und Client in der gleichen CPX-Version betrieben werden!");
            serverVersionLabel.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.WARNING));

            gpInfo.add(keyLabel(null), 0, ++rowIndex);
            gpInfo.add(serverVersionLabel, 1, rowIndex);
        }

//        gpInfo.add(keyLabel(null), 0, ++rowIndex);
//        gpInfo.add(infoLabel(clientVersion.getDescription(), null), 1, rowIndex);
        gpInfo.add(keyLabel("Java-Version (JRE)"), 0, ++rowIndex);
        gpInfo.add(infoLabel(cpxProps.getJavaVersion()
                + (cpxProps.isRequiredJavaVersion() ? "" : (" " + Lang.getSystemInfoRecommendedJavaVersionText(CpxSystemPropertiesInterface.REQUIRED_JAVA_VERSION)))//" (recommended is " + CpxSystemPropertiesInterface.REQUIRED_JAVA_VERSION + " or higher!)")
                + ", " + cpxProps.getJavaVmName(), null), 1, rowIndex);

        final Label javaHomeLabel = infoLabel(
                cpxProps.getJavaHome(), Lang.getDirOpenInExplorerText());
        javaHomeLabel.setOnMouseClicked((eventMous) -> {
            if (eventMous.getButton() == MouseButton.PRIMARY) {
                ToolbarMenuFXMLController.openInExplorer(cpxProps.getJavaHome() + ".");
            }
        });

        gpInfo.add(keyLabel("Java-Installationspfad"), 0, ++rowIndex);
        gpInfo.add(javaHomeLabel, 1, rowIndex);

        gpInfo.add(keyLabel("JavaFX-Version"), 0, ++rowIndex);
        gpInfo.add(infoLabel(cpxProps.getJavaFxVersion() + " (" + cpxProps.getJavaFxRuntimeVersion() + ")", null), 1, rowIndex);

        final Label jdkModulePathLabel = infoLabel(
                cpxProps.getJdkModulePath(), Lang.getDirOpenInExplorerText());
        jdkModulePathLabel.setOnMouseClicked((eventMous) -> {
            if (eventMous.getButton() == MouseButton.PRIMARY) {
                ToolbarMenuFXMLController.openInExplorer(cpxProps.getJdkModulePath() + ".");
            }
        });

        gpInfo.add(keyLabel("JavaFX-Modulpfad"), 0, ++rowIndex);
        gpInfo.add(jdkModulePathLabel, 1, rowIndex);

        gpInfo.add(keyLabel("Betriebssystem"), 0, ++rowIndex);
        gpInfo.add(infoLabel(cpxProps.getOsName(), null), 1, rowIndex);

        gpInfo.add(keyLabel("Microsoft Office"), 0, ++rowIndex);
        gpInfo.add(infoLabel((DocumentReader.OFFICE_FOUND ? DocumentReader.getOfficeInformation() : "nicht gefunden"), null), 1, rowIndex);

        final HBox memoryBox = getMemoryBox();
        final Label usedMemoryAbs = new Label("");
        final Label usedMemoryRel = new Label("");
        final Label freeMemoryAbs = new Label("");
        final Label freeMemoryRel = new Label("");
        final Label maxMemoryAbs = new Label("");
        final ProgressBar memoryProgressBar = new ProgressBar();
        memoryProgressBar.prefWidthProperty().bind(memoryBox.widthProperty());

        memoryProgressBar.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double progress = newValue == null ? 0 : newValue.doubleValue();
                Node bar = memoryProgressBar.lookup(".bar");
                if (progress >= 0.9) {
                    bar.setStyle("-fx-background-color: #E60000;"); //#E60000 = Electric Red
                } else if (progress >= 0.75) {
                    bar.setStyle("-fx-background-color: #FF7800;"); //#FF7800 = Safety Orange
                } else {
                    bar.setStyle(null); //#4CBB17 = Kelly Green
                }
            }
        });

        Button gc = getGcButton();
        memoryBox.getChildren().addAll(usedMemoryAbs, usedMemoryRel, freeMemoryAbs, freeMemoryRel, maxMemoryAbs, gc);

        gpInfo.add(keyLabel("Speicher"), 0, ++rowIndex);
        gpInfo.add(memoryBox, 1, rowIndex);
        gpInfo.add(memoryProgressBar, 1, ++rowIndex);

        final OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        final HBox cpuBox = getMemoryBox();
        final Label cpuRel = new Label("");
        cpuBox.getChildren().addAll(cpuRel);
        final ProgressBar cpuProgressBar = new ProgressBar();
        cpuProgressBar.prefWidthProperty().bind(cpuBox.widthProperty());

        cpuProgressBar.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double progress = newValue == null ? 0 : newValue.doubleValue();
                Node bar = cpuProgressBar.lookup(".bar");
                if (progress >= 0.9) {
                    bar.setStyle("-fx-background-color: #E60000;"); //#E60000 = Electric Red
                } else if (progress >= 0.75) {
                    bar.setStyle("-fx-background-color: #FF7800;"); //#FF7800 = Safety Orange
                } else {
                    bar.setStyle(null); //#4CBB17 = Kelly Green
                }
            }
        });

        gpInfo.add(keyLabel("CPU"), 0, ++rowIndex);
        gpInfo.add(cpuBox, 1, rowIndex);
        gpInfo.add(cpuProgressBar, 1, ++rowIndex);

        gpInfo.add(keyLabel("Windows-Anwender"), 0, ++rowIndex);
        gpInfo.add(infoLabel(cpxProps.getUserName(), null), 1, rowIndex);

        gpInfo.add(keyLabel("Netzwerkadresse"), 0, ++rowIndex);
        gpInfo.add(infoLabel(cpxProps.getHostName() + " (IP: " + cpxProps.getHostIp() + ")", null), 1, rowIndex);

        final RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        gpInfo.add(keyLabel("Anwendung gestartet seit"), 0, ++rowIndex);
        gpInfo.add(infoLabel(Lang.toDateTime(new Date(rb.getStartTime())), null), 1, rowIndex);

        final Label userDirLabel = infoLabel(cpxProps.getUserDir(), Lang.getDirOpenInExplorerText());
        userDirLabel.setOnMouseClicked((eventMous) -> {
            if (eventMous.getButton() == MouseButton.PRIMARY) {
                ToolbarMenuFXMLController.openInExplorer(cpxProps.getUserDir() + ".");
            }
        });

        final Label appDataLocalLabel = infoLabel(
                cpxProps.getCpxClientCatalogDir(), Lang.getDirOpenInExplorerText());
        appDataLocalLabel.setOnMouseClicked((eventMous) -> {
            if (eventMous.getButton() == MouseButton.PRIMARY) {
                ToolbarMenuFXMLController.openInExplorer(cpxProps.getCpxClientCatalogDir() + ".");
            }
        });

        final Label configurationFileLabel = infoLabel(cpxProps.getCpxClientConfigFile(), Lang.getFileOpenInExplorerText());
        configurationFileLabel.setOnMouseClicked((eventMous) -> {
            if (eventMous.getButton() == MouseButton.PRIMARY) {
                ToolbarMenuFXMLController.openInExplorer(cpxProps.getCpxClientConfigFile());
            }
        });

        gpInfo.add(headerLabel("Dateien & Verzeichnisse"), 0, ++rowIndex);

        gpInfo.add(keyLabel("Installationsverzeichnis"), 0, ++rowIndex);
        gpInfo.add(userDirLabel, 1, rowIndex);

        gpInfo.add(keyLabel("Konfigurationsdatei"), 0, ++rowIndex);
        gpInfo.add(configurationFileLabel, 1, rowIndex);

        gpInfo.add(keyLabel("Katalogdaten"), 0, ++rowIndex);
        gpInfo.add(appDataLocalLabel, 1, rowIndex);

        gpInfo.add(keyLabel("Protokolldatei"), 0, ++rowIndex);
        gpInfo.add(getLogFileBox(), 1, rowIndex);

        gpInfo.setVgap(10);

        //MainApp.showInfoMessageDialog("System-Informationen", gpInfo);
        //AlertDialog dialog = AlertDialog.createInformationDialog("", MainApp.getWindow(), ButtonType.OK);
//        setHeaderText("System-Informationen");
        getDialogPane().setContent(gpInfo);
        getDialogSkin().setButtonTypes(ButtonType.OK);

        //2018-06-29 DNi: Show and continually refresh some memory information
        final ObjectProperty<Thread> th = new SimpleObjectProperty<>();
        showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    LOG.log(Level.INFO, "close system info dialog");
                    Thread t = th.get();
                    if (t != null) {
                        t.interrupt();
                    }
                    dialogInstance = null;
                } else {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                if (th.get().isInterrupted()) {
                                    break;
                                }
                                final Runtime runtime = Runtime.getRuntime();
                                final long maxMemory = runtime.maxMemory();
                                final long totalMemory = runtime.totalMemory(); //allocated memory
                                final long freeMemory = runtime.freeMemory() + (maxMemory - totalMemory);
                                final long usedMemory = maxMemory - freeMemory;

//                                    LOG.log(Level.INFO, "free: " + (freeMemory / 1024 / 1024));
//                                    LOG.log(Level.INFO, "used: " + (usedMemory / 1024 / 1024));
//                                    LOG.log(Level.INFO, "total: " + (totalMemory / 1024 / 1024));
//                                    LOG.log(Level.INFO, "max: " + (maxMemory / 1024 / 1024));
                                final String freeMemoryAbsStr = Lang.toDecimal((double) freeMemory / 1024 / 1024, 1);
                                final String usedMemoryAbsStr = Lang.toDecimal((double) usedMemory / 1024 / 1024, 1);
                                final String maxMemoryAbsStr = Lang.toDecimal((double) maxMemory / 1024 / 1024, 1);

                                final String freeMemoryRelStr = Lang.toDecimal(((double) freeMemory * 100) / maxMemory, 1);
                                final String usedMemoryRelStr = Lang.toDecimal(((double) usedMemory * 100) / maxMemory, 1);
                                //final String totalMemoryRelStr = Lang.toDecimal(((double) totalMemory * 100) / totalMemory, 1);

                                final double usedCpu = bean.getProcessCpuLoad();
                                final String usedCpuRelStr = Lang.toDecimal(usedCpu * 100, 0);

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        usedMemoryAbs.setText("benutzt: " + usedMemoryAbsStr + " MB");
                                        usedMemoryRel.setText("(" + usedMemoryRelStr + "%),");
                                        freeMemoryAbs.setText("frei: " + freeMemoryAbsStr + " MB");
                                        freeMemoryRel.setText("(" + freeMemoryRelStr + "%),");
                                        maxMemoryAbs.setText("gesamt: " + maxMemoryAbsStr + " MB");
                                        memoryProgressBar.setProgress((double) usedMemory / maxMemory);

                                        cpuRel.setText(usedCpuRelStr + " %");
                                        cpuProgressBar.setProgress(usedCpu);
                                        //totalMemoryRel.setText("(" + totalMemoryRelStr + "%)");
                                        //usedMemoryAbs.setText(usedMemory / 1024 / 1024);
                                    }
                                });
                                try {
                                    Thread.sleep(1000L);
                                } catch (InterruptedException ex) {
                                    LOG.log(Level.FINEST, "Memory refresh was interrupted", ex);
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                        }
                    });
                    t.setName("CPX Memory/CPU Refresher");
                    t.start();
                    th.set(t);
                }
            }
        });
    }

    private HBox getMemoryBox() {
        HBox memoryBox = new HBox();
        memoryBox.setSpacing(5d);
        memoryBox.setAlignment(Pos.CENTER_LEFT);
        return memoryBox;
    }

    private Button getGcButton() {
        final Button gc = new Button("GC");
        gc.setStyle("-fx-font-size: 10");
        gc.setTooltip(new Tooltip("Garbage Collection ausführen"));
        gc.setOnAction((event) -> {
            LOG.log(Level.INFO, "execute garbage collection");
            System.gc();
            System.runFinalization();
            Notifications notif = NotificationsFactory.instance().createInformationNotification();
            notif.text("Garbage Collection wurde ausgeführt");
            notif.show();
        });
        return gc;
    }

    private HBox getLogFileBox() {
        HBox logFileBox = new HBox();
        logFileBox.setSpacing(5d);
        logFileBox.setAlignment(Pos.CENTER_LEFT);

        final File logFile = InitLogger.instance().getLogFile();
        final Label logFileLabel = infoLabel(logFile == null ? "---" : logFile.getName(), Lang.getFileOpenInExplorerText());
        logFileLabel.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                ToolbarMenuFXMLController.openInExplorer(logFile);
            }
        });

//            logFileLabel.setContentDisplay(ContentDisplay.RIGHT);
        //final org.controlsfx.glyphfont.Glyph logFileGlyph = ResourceLoader.getGlyph(FontAwesome.Glyph.EYE);
//            logFileLabel.setGraphic(logFileGlyph);
        final Button viewLogFileButton = new Button("öffnen");
        //viewLogFileButton.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.FILE));
        //viewLogFileButton.setGraphicTextGap(10D);
        viewLogFileButton.setOnAction((event) -> {
            ToolbarMenuFXMLController.editFile(logFile);
        });

        final Button sendLogFileButton = new Button("L&B zusenden");
        //viewLogFileButton.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.FILE));
        //viewLogFileButton.setGraphicTextGap(10D);
        sendLogFileButton.setOnAction((event) -> {
            if (logFile == null) {
                MainApp.showErrorMessageDialog("Protokolldatei nicht gefunden!", getDialogSkin().getStage());
            } else {
                final String subject = "Protokolldatei";
                final String msg = "";
                final String details = "";
                final String receiverMail = "cpx_team@lohmann-birkner.de"; //later: support@lohmann-birkner.de
                CpxSystemPropertiesInterface cpxPropsServer = null;
                final String serverLog = null;
                final IssueInfo issueInfo = new IssueInfo();
                final SendMail sendMail = new SendMail();
                try {
                    sendMail.openExceptionDraft(receiverMail, subject, msg, details, cpxProps, cpxPropsServer, serverLog, issueInfo);
                } catch (IOException | MessagingException ex) {
                    LOG.log(Level.SEVERE, "Was not able to open mail draft", ex);
                }
            }
        });

        logFileBox.getChildren().addAll(logFileLabel, viewLogFileButton, sendLogFileButton);
        return logFileBox;
    }

}
