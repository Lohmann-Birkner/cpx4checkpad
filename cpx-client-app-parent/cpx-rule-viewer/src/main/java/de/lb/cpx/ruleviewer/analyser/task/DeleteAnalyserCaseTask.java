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
package de.lb.cpx.ruleviewer.analyser.task;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.task.TaskService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.concurrent.Worker;
import javafx.scene.control.ButtonType;

/**
 *
 * @author wilde
 */
public abstract class DeleteAnalyserCaseTask extends TaskService<Boolean> {

    private final List<Long> cases;
    private DeleteDialog dialog;

    public DeleteAnalyserCaseTask(List<Long> pCases) {
        cases = Objects.requireNonNullElse(pCases, new ArrayList<>());
    }

    @Override
    public void start() {
        dialog = new DeleteDialog();
        dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType t) {
                if (ButtonType.YES.equals(t)) {
                    DeleteAnalyserCaseTask.super.start();
                }
            }
        });
    }

    @Override
    public void afterTask(State pState) {
        super.afterTask(pState);
        if (Worker.State.SUCCEEDED.equals(pState)) {
            Boolean result = getValue();
            if (result) {
                MainApp.showInfoMessageDialog("Löschen der ausgewählten Einträge war erfolgreich!");
            } else {
                MainApp.showErrorMessageDialog("Die ausgewählten Einträge konnten nicht gelöscht werden!");
            }
        }
    }

    private class DeleteDialog extends ConfirmDialog {

        public DeleteDialog() {
            super();
            initOwner(MainApp.getWindow());
            getContentLabel().setText(getDialogText(cases));
        }

        private String getDialogText(List<Long> cases) {
            if (cases.size() == 1) {
                return "Wollen Sie wirklich diesen Eintrag löschen?";
            }
            return "Wollen Sie wirklich diese Einträge (" + cases.size() + ") löschen?";
        }
    }
}
