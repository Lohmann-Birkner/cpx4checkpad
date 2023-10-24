/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.ruleeditor.util;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.json.RuleMessage;
import de.lb.cpx.shared.json.RuleTableMessage;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.rules.util.RuleMessageHelper;
import java.util.function.Consumer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author wilde
 */
public class JsonMessageHelper {

    public static void checkAndShowTransferCatalogError(CrgRules pRule, Consumer<ButtonType> pConsumer) {
        RuleMessage msg = RuleMessageHelper.getFirstTransferCatalogError(pRule);
        if(msg == null){
            pConsumer.accept(ButtonType.YES);
            return;
        }
        AlertDialog diag = AlertDialog.createYesNoConfirmDialog(Alert.AlertType.WARNING, Lang.getRuleMessageCatalogTransferDialogWarning(String.valueOf(msg.getSrcYear()),String.valueOf(msg.getDestYear())), null, MainApp.getWindow());
        diag.showAndWait().ifPresent(pConsumer);
    }
    
    public static void checkAndShowTransferCatalogError(CrgRuleTables pTable, Consumer<ButtonType> pConsumer) {
        RuleTableMessage msg = RuleMessageHelper.getFirstTransferCatalogError(pTable);
        if(msg == null){
            pConsumer.accept(ButtonType.YES);
            return;
        }
        AlertDialog diag = AlertDialog.createYesNoConfirmDialog(Alert.AlertType.WARNING, Lang.getRuleTableMessageCatalogTransferDialogWarning(String.valueOf(msg.getSrcYear()),String.valueOf(msg.getDestYear())), null, MainApp.getWindow());
        diag.showAndWait().ifPresent(pConsumer);
    }
}
