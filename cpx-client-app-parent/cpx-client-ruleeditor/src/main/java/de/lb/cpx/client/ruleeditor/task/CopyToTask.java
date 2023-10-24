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
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.task.TaskService;
import de.lb.cpx.client.ruleeditor.task.dialog.ImportStrategyDialog;
import de.lb.cpx.client.ruleeditor.util.RuleListHelper;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePoolsProd;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.server.rule.services.RuleExchangeResult;
import de.lb.cpx.shared.lang.Lang;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.concurrent.Worker;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Task to copy List of Rules from a origin pool to an target pool
 *
 * @author wilde
 */
public abstract class CopyToTask extends TaskService<RuleExchangeResult> {

    private final CrgRulePools origin;
    private final CrgRulePools target;
    private final List<CrgRules> rules;
    private final String numbers;
    private CopyDialog dialog;

    public CopyToTask(CrgRulePools pOrigin, CrgRulePools pTarget, List<CrgRules> pRules) {
        super();
        origin = Objects.requireNonNull(pOrigin, "origin pool can not be null");
        target = Objects.requireNonNull(pTarget, "target pool can not be null");
        rules = Objects.requireNonNull(pRules, "target pool can not be null");
        numbers = String.valueOf(pRules.size());//RuleListHelper.getSelectedRuleNumbers(pRules);
    }

    @Override
    public void start() {
        dialog = getDialog();
        dialog.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType t) {
                if (ButtonType.YES.equals(t)) {
                    CopyToTask.super.start();
                }
            }
        });
    }

    @Override
    public RuleExchangeResult call() {
        RuleOverrideFlags overrideFlag = dialog.getOverrideFlag();
        RuleImportCheckFlags importCheckFlag = dialog.getImportCheckFlag();
        Session.instance().setRuleOverrideFlag(overrideFlag);
        Session.instance().setRuleImportCheckFlag(importCheckFlag);
        return CopyToTask.this.call(origin, target, RuleListHelper.getSelectedRuleIds(rules), importCheckFlag, overrideFlag);
    }

    @Override
    public void afterTask(Worker.State pState) {
        super.afterTask(pState);
        if (Worker.State.SUCCEEDED.equals(pState)) {
            RuleExchangeResult success = getValue();
            if (success != null) {
                MainApp.showInfoMessageDialog("Kopieren/Verschieben der ausgewählten Regel(n) (" + numbers + ") aus Pool: " + origin.getCrgplIdentifier() + " nach: " + target.getCrgplIdentifier() + " war erfolgreich!");
            } else {
                MainApp.showErrorMessageDialog("Kopieren/Verschieben der Regel(n): " + numbers + " aus Pool: " + origin.getCrgplIdentifier() + " nach: " + target.getCrgplIdentifier() + " ist felgeschlagen!");
            }
        }
    }

    public abstract RuleExchangeResult call(CrgRulePools pOrigin, CrgRulePools pTarget, List<Long> pRules, RuleImportCheckFlags pCheckFlag, RuleOverrideFlags pOverrideFlag);

    private CopyDialog getDialog() {
        if (origin instanceof CrgRulePoolsProd) {
            return new CopyFromProdDialog();
        }
        if (target instanceof CrgRulePoolsProd) {
            return new CopyToProdDialog();
        }
        return new CopyDialog();
    }

    private class CopyToProdDialog extends CopyDialog {

        public CopyToProdDialog() {
            super(Alert.AlertType.WARNING, Lang.getWarning(), "Wollen Sie die ausgewählten Regel(n) (" + numbers + ") wirklich in den Produktivpool: " + target.getCrgplIdentifier() + " kopieren?\n"
                    + "Damit werden die Regeln in die Produktion importiert und für das Groupen verwendet!");
        }
    }

    private class CopyFromProdDialog extends CopyDialog {

        public CopyFromProdDialog() {
            super(AlertType.WARNING, Lang.getWarning(), "Wollen Sie die ausgewählten Regel(n) (" + numbers + ") wirklich aus dem Produktivpool: " + origin.getCrgplIdentifier() + " in den Pool: " + target.getCrgplIdentifier() + " verschieben?\n"
                    + "Damit werden die Regeln aus der Produktion entfernt und stehen nicht mehr für Grouping-Ergebnisse zur Verfügung!");
        }
    }

    private class CopyDialog extends ImportStrategyDialog {

        public CopyDialog() {
            super("Wollen Sie die ausgewählten Regel(n) (" + numbers + ") wirklich in den Pool: " + target.getCrgplIdentifier() + " kopieren?");
        }

        public CopyDialog(AlertType pType, String pHeaderText, String pText) {
            super(pType, pHeaderText, pText);
        }
    }
}
