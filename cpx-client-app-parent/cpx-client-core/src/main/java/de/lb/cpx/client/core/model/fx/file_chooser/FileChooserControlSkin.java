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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.file_chooser;

import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.fx.file_chooser.FileChooserControl.Mode;
import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 *
 * @author wilde
 */
public class FileChooserControlSkin extends SkinBase<FileChooserControl> {

    private static final Logger LOG = Logger.getLogger(FileChooserControlSkin.class.getName());

    private HBox root;
    private TextField label;
    private Button btn;

    public FileChooserControlSkin(FileChooserControl pSkinnable) {
        super(pSkinnable);
        getChildren().add(initLayout());
    }

    private Parent initLayout() {
        label = new TextField();
        getSkinnable().fileProperty().addListener(new ChangeListener<File>() {
            @Override
            public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
                if (newValue == null) {
                    label.setText("");
                    return;
                }
                LOG.info("change file to " + newValue.getName());
                label.setText(Objects.requireNonNullElse(newValue, new File("")).getAbsolutePath());
            }
        });
        getSkinnable().setFile(getFile());
        label.setMaxWidth(Double.MAX_VALUE);
        label.setEditable(false);
        btn = new Button();
//        btn.getStyleClass().add("cpx-icon-button");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch (getSkinnable().getMode()) {
                    case DIR:
                        showDirChooser();
                        break;
                    case FILE:
                        showFileChooser();
                        break;
                    default:
                        LOG.warning("Can not display Chooser, unknown Mode: " + getSkinnable().getMode().name());
                }
            }

            private void showFileChooser() {
                FileChooser chooser = getFileChooser();
                chooser.setTitle(getSkinnable().getChooserTitle());
                chooser.getExtensionFilters().addAll(getSkinnable().getExtensionFilters());
                File file = chooser.showOpenDialog(getSkinnable().getScene().getWindow());
                if (file == null) {
                    //clear selection??
                } else {
//                    CpxClientConfig.instance().setUserRecentFileChooserPath(file.getParentFile(),getSkinnable().getChoosenPathType());
                    saveFile(file);
                    getSkinnable().setFile(file);
                }
            }

            private void showDirChooser() {
                DirectoryChooser chooser = getDirChooser();
                chooser.setTitle(getSkinnable().getChooserTitle());
                File file = chooser.showDialog(getSkinnable().getScene().getWindow());
                if (file == null) {
                    //clear selection??
                } else {
                    saveFile(file);
                    //CpxClientConfig.instance().setUserRecentFileChooserPath(file.getParentFile(),getSkinnable().getChoosenPathType());
                    getSkinnable().setFile(file);
                }
            }

        });
        btn.setText("...");
//        btn.textProperty().bind(Bindings.when(label.textProperty().isEmpty()).then("Durchsuchen").otherwise("Ändern"));
//        btn.setPrefWidth(50);
        HBox.setHgrow(label, Priority.ALWAYS);
        root = new HBox(label, btn);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setFillHeight(true);
        return root;
    }

    private File getFile() {
        String path = CpxClientConfig.instance().getUserRecentFileChooserPath(getSkinnable().getChoosenPathType());
        File file = new File(path);
        if (file.exists()) {
            if (Mode.FILE.equals(getSkinnable().getMode())) {
                if (!file.isFile()) {
                    return null;
                }
                return file;
            }
            return file;
        }
        return null;
    }

    private void saveFile(File pFile) {
        if (getSkinnable().getChoosenPathType() == null || getSkinnable().getChoosenPathType().isEmpty()) {
            CpxClientConfig.instance().setUserRecentFileChooserPath(pFile);
        } else {
            CpxClientConfig.instance().setUserRecentFileChooserPath(pFile, getSkinnable().getChoosenPathType());
        }
    }

    private DirectoryChooser getDirChooser() {
        if (getSkinnable().getChoosenPathType() == null || getSkinnable().getChoosenPathType().isEmpty()) {
            return FileChooserFactory.instance().createDirectoryChooser();
        }
        return FileChooserFactory.instance().createDirectoryChooser(getSkinnable().getChoosenPathType());
    }

    private FileChooser getFileChooser() {
        if (getSkinnable().getChoosenPathType() == null || getSkinnable().getChoosenPathType().isEmpty()) {
            return FileChooserFactory.instance().createFileChooser();
        }
        return FileChooserFactory.instance().createFileChooser(getSkinnable().getChoosenPathType());
    }
}
