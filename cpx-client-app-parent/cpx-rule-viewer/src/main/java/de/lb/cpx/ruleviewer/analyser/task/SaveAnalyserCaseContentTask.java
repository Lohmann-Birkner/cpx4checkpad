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
import de.lb.cpx.client.core.model.task.TaskService;
import javafx.concurrent.Worker;

/**
 *
 * @author wilde
 */
public abstract class SaveAnalyserCaseContentTask extends TaskService<Boolean> {

    @Override
    public void afterTask(Worker.State pState) {
        super.afterTask(pState); //To change body of generated methods, choose Tools | Templates.
        if (Worker.State.SUCCEEDED.equals(pState)) {
            if (getValue()) {
                MainApp.showInfoMessageDialog("Falldaten wurden erfolgreich gespeichert!");
            } else {
                MainApp.showErrorMessageDialog("Falldaten konnten nicht gespeichert werden!");
            }
        }
    }
}
