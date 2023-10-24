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
package de.lb.cpx.client.ruleeditor.task;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.task.TaskService;
import de.lb.cpx.client.ruleeditor.menu.filterlists.RuleListFXMLController;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Worker;

/**
 *
 * @author wilde
 */
public abstract class ExportContentTask extends TaskService<File> implements BasicExportTask<String>{

    private final File dir;

    public ExportContentTask(File pDirectory) {
        super();
        dir = Objects.requireNonNull(pDirectory, "Directory can not be null");
        setDescription("Exportiere...");
    }

    @Override
    public File call() {
        try {
            String poolAsString = getContent();
            File newFile = new File(dir.getAbsolutePath() + "/" + createOutputFileName());
            Files.write(newFile.toPath(), poolAsString.getBytes(), StandardOpenOption.CREATE);
            return newFile;
        } catch (IOException ex) {
            Logger.getLogger(RuleListFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            MainApp.showErrorMessageDialog(ex, createErrorMessage());
//                            } catch (RuleEditorProcessException ex) {
//                                Logger.getLogger(RuleListFXMLController.class.getName()).log(Level.SEVERE, null, ex);
//                                MainApp.showErrorMessageDialog(ex, "Import der Datei:" + file.getName() + " ist fehlgeschlagen!");
        }
        return null;
    }

    @Override
    public void afterTask(Worker.State pState) {
        super.afterTask(pState);
        if (Worker.State.SUCCEEDED.equals(pState)) {
            File success = getValue();
            if (success != null) {
                MainApp.showInfoMessageDialog(createSuccessMessage(success));
            }
        }
    }

}
