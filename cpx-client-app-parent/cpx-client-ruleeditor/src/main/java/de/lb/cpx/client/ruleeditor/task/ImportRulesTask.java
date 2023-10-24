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
import de.lb.cpx.client.ruleeditor.model.error.RuleExchangeTopicDialog;
import de.lb.cpx.client.ruleeditor.task.dialog.ImportStrategyDialog;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.server.rule.services.RuleEditorProcessException;
import de.lb.cpx.server.rule.services.RuleExchangeError;
import de.lb.cpx.shared.lang.Lang;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Worker;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

/**
 *
 * @author wilde
 */
public abstract class ImportRulesTask extends TaskService<List<RuleExchangeError>> {
    
    private static final boolean SHOW_TOPIC_DIALOG = false;
    private final File file;
    private ImportDialog dialog;

    public ImportRulesTask(File pFile) {
        super();
        file = Objects.requireNonNull(pFile, "File can not be null");
        setDescription("Datei: " + file.getName() + " wird importiert");
    }

    @Override
    public void start() {
        start(true);
//        dialog = getDialog();
//        dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
//            @Override
//            public void accept(ButtonType t) {
//                if(ButtonType.YES.equals(t)){
//                    ImportRulesTask.super.start();
//                }
//            }
//        });
    }

    public void start(boolean pShowDialog) {
        if (!pShowDialog) {
            ImportRulesTask.super.start();
            return;
        }
        dialog = getDialog();
        dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType t) {
                if (ButtonType.YES.equals(t)) {
                    ImportRulesTask.super.start();
                }
            }
        });
    }

    @Override
    public List<RuleExchangeError> call() {
        try {
            String content = readFile(file, Charset.forName("UTF-16"));

            List<RuleExchangeError> errors = importRules(content,
                    dialog != null ? dialog.getImportCheckFlag() : RuleImportCheckFlags.NO_CHECK_4_COLLISIONS,
                    dialog != null ? dialog.getOverrideFlag() : RuleOverrideFlags.SAVE_NEW);
//            ArrayList<RuleExchangeError> err = new ArrayList<>(errors);
//            err.addAll(errors);
//            err.addAll(errors);
//            err.addAll(errors);
//            err.addAll(errors);
//            err.addAll(errors);
//            err.addAll(errors);
            return errors;
        } catch (IOException | RuleEditorProcessException ex) {
            Logger.getLogger(RuleListFXMLController.class.getName()).log(Level.SEVERE, null, ex);
//            MainApp.showErrorMessageDialog(ex, "Import der Datei:" + file.getName() + " ist fehlgeschlagen!");
            return new ArrayList<>();
        }
    }
    
    
    public abstract List<RuleExchangeError> importRules(String pContent, RuleImportCheckFlags pDoCheck, RuleOverrideFlags pDoOverride) throws RuleEditorProcessException;

    private String readFile(File pFile, Charset pEncoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(pFile.toPath());
        return new String(encoded);
//                    return new String(encoded, pEncoding);
    }

    @Override
    public void afterTask(Worker.State pState) {
        super.afterTask(pState);
        List<RuleExchangeError> errors = getValue();
        Boolean success = errors.isEmpty();
        if (success) {
            MainApp.showInfoMessageDialog(Lang.getFileImportSuccess(file.getName()));
        } else {
            if (SHOW_TOPIC_DIALOG) {
                RuleExchangeTopicDialog errorDialog = new RuleExchangeTopicDialog(Alert.AlertType.WARNING,
                        //"Import der Datei:" + file.getName() + " wurde mit Fehler(n) abgeschlossen!",
                        Lang.getFileImportErrors(file.getName()),
                        MainApp.getStage(),
                        Modality.APPLICATION_MODAL);
                errorDialog.getItems().addAll(errors);
                errorDialog.showAndWait();
            } else {
                MainApp.showWarningMessageDialog(Lang.getFileImportErrors(file.getName()));
            }
        }
    }

    private ImportDialog getDialog() {
        return new ImportDialog();
    }

    private class ImportDialog extends ImportStrategyDialog {

        public ImportDialog() {
            super("Wollen Sie die ausgewählte Datei (" + file.getName() + ") wirklich importieren?");
        }

        public ImportDialog(Alert.AlertType pType, String pHeaderText, String pText) {
            super(pType, pHeaderText, pText);
        }
    }
}
