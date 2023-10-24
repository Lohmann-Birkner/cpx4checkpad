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
package de.lb.cpx.client.ruleeditor.menu.dialogs;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.ruleeditor.menu.dialogs.buttontypes.RuleEditorButtonTypes;
import de.lb.cpx.shared.lang.Lang;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

/**
 *
 * @author wilde
 */
public class UnsavedContentDialog extends AlertDialog {

    public UnsavedContentDialog() {
        super(AlertType.WARNING, Lang.getWarning(), "Sie haben ungesicherte Änderungen!\nWie wollen Sie fortfahren?", "", RuleEditorButtonTypes.SAVE_ALL, RuleEditorButtonTypes.DISCARD, ButtonType.CANCEL);
        initOwner(MainApp.getWindow());
        initModality(Modality.APPLICATION_MODAL);
    }
}
