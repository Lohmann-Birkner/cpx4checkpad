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

import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.stage.FileChooser;

/**
 *
 * @author wilde
 */
public class FileChooserControl extends Control {

    public FileChooserControl() {
        super();
    }

    public FileChooserControl(Mode pMode) {
        this();
        setMode(pMode);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FileChooserControlSkin(this);
    }
    private ObjectProperty<File> fileProperty;

    public ObjectProperty<File> fileProperty() {
        if (fileProperty == null) {
            fileProperty = new SimpleObjectProperty<>();
        }
        return fileProperty;
    }

    public File getFile() {
        return fileProperty().get();
    }

    public void setFile(File pFile) {
        fileProperty().set(pFile);
    }
    private StringProperty chooserTitleProperty;

    public StringProperty chooserTitleProperty() {
        if (chooserTitleProperty == null) {
            chooserTitleProperty = new SimpleStringProperty("Öffnen");
        }
        return chooserTitleProperty;
    }

    public void setChooserTitle(String pTitle) {
        chooserTitleProperty().set(pTitle);
    }

    public String getChooserTitle() {
        return chooserTitleProperty().get();
    }
    private final List<FileChooser.ExtensionFilter> extensionFilters = new ArrayList<>();

    public List<FileChooser.ExtensionFilter> getExtensionFilters() {
        return new ArrayList<>(extensionFilters);
    }

    public boolean addExtensionFilters(FileChooser.ExtensionFilter... pFilter) {
        return extensionFilters.addAll(Lists.newArrayList(pFilter));
    }
    private ObjectProperty<Mode> modeProperty;

    private StringProperty choosenPathTypeProperty;

    public StringProperty choosenPathTypeProperty() {
        if (choosenPathTypeProperty == null) {
            choosenPathTypeProperty = new SimpleStringProperty(null);
        }
        return choosenPathTypeProperty;
    }

    public void setChoosenPathType(String pPathType) {
        choosenPathTypeProperty().set(pPathType);
    }

    public String getChoosenPathType() {
        return choosenPathTypeProperty().get();
    }

    public ObjectProperty<Mode> modeProperty() {
        if (modeProperty == null) {
            modeProperty = new SimpleObjectProperty<>(Mode.FILE);
        }
        return modeProperty;
    }

    public void setMode(Mode pMode) {
        modeProperty().set(pMode);
    }

    public Mode getMode() {
        return modeProperty().get();
    }

    public enum Mode {
        FILE, DIR
    }
}
