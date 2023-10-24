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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.file_chooser;

import de.lb.cpx.client.core.config.CpxClientConfig;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 * FileChooser is a static defined class, so I'm using a factory instead
 *
 * @author niemeier
 */
public class FileChooserFactory {

    public static final String RULE_IMPORT = "rule_import";
    public static final String RULE_EXPORT = "rule_export";

    private static final Logger LOG = Logger.getLogger(FileChooserFactory.class.getName());

    private static FileChooserFactory instance;

    private FileChooserFactory() {

    }

    public static synchronized FileChooserFactory instance() {
        if (instance == null) {
            instance = new FileChooserFactory();
        }
        return instance;
    }

    /**
     * Creates a file chooser and sets the recent used path as initial directory
     *
     * @return file chooser with preselected path
     */
    public FileChooser createFileChooser() {
        return createFileChooser(null);
    }

    /**
     * Creates a file chooser and sets the recent used path as initial directory
     *
     * @param pSection section
     * @return file chooser with preselected path
     */
    public FileChooser createFileChooser(final String pSection) {
        final FileChooser fileChooser = new FileChooser();
        String recentPath = CpxClientConfig.instance().getUserRecentFileChooserPath(pSection);
        File file = null;
        if (!recentPath.isEmpty()) {
            file = new File(recentPath);
            if (!file.exists()) {
                LOG.log(Level.WARNING, "Using parent folder as initial file chooser directory, because this path does not exist anymore: " + file.getAbsolutePath());
                file = file.getParentFile();
            }
            if (file != null && !file.exists()) {
                LOG.log(Level.FINE, "Cannot set recent file chooser path, because this folder does not exist: " + file.getAbsolutePath());
                file = null;
            }
            if (file != null && !file.isDirectory()) {
                LOG.log(Level.FINE, "Using parent folder as initial file chooser directory, because this path seems to be a file and not a folder: " + file.getAbsolutePath());
                file = file.getParentFile();
            }
        }
        if (file != null) {
            fileChooser.setInitialDirectory(file);
        }
        return fileChooser;
    }

    /**
     * Creates a file chooser and sets the recent used path as initial directory
     *
     * @return file chooser with preselected path
     */
    public DirectoryChooser createDirectoryChooser() {
        return createDirectoryChooser(null);
    }

    /**
     * Creates a file chooser and sets the recent used path as initial directory
     *
     * @param pSection section
     * @return file chooser with preselected path
     */
    public DirectoryChooser createDirectoryChooser(final String pSection) {
        final DirectoryChooser dirChooser = new DirectoryChooser();
        String recentPath = CpxClientConfig.instance().getUserRecentFileChooserPath(pSection);
        File file = null;
        if (!recentPath.isEmpty()) {
            file = new File(recentPath);
            if (!file.exists()) {
                LOG.log(Level.WARNING, "Using parent folder as initial file chooser directory, because this path does not exist anymore: " + file.getAbsolutePath());
                file = file.getParentFile();
            }
            if (file != null && !file.exists()) {
                LOG.log(Level.FINE, "Cannot set recent file chooser path, because this folder does not exist: " + file.getAbsolutePath());
                file = null;
            }
            if (file != null && !file.isDirectory()) {
                LOG.log(Level.FINE, "Using parent folder as initial file chooser directory, because this path seems to be a file and not a folder: " + file.getAbsolutePath());
                file = file.getParentFile();
            }
        }
        if (file != null) {
            dirChooser.setInitialDirectory(file);
        }
        return dirChooser;
    }

}
