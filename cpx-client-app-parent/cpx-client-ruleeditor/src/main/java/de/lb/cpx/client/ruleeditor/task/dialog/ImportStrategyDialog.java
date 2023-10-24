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
package de.lb.cpx.client.ruleeditor.task.dialog;

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledCheckBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.server.commons.enums.RuleImportCheckFlags;
import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import de.lb.cpx.shared.lang.Lang;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.StringConverter;

/**
 *
 * @author wilde
 */
public class ImportStrategyDialog extends AlertDialog {

    private LabeledComboBox<RuleOverrideFlags> cbOverrideFlag;
    private LabeledCheckBox chkBoxImportCheckFlag;

    public ImportStrategyDialog(String pText) {
        this(Lang.getConformation(), pText);
    }

    public ImportStrategyDialog(String pTitle, String pText) {
        this(AlertType.INFORMATION, pTitle, pText);
    }
//        public ImportStrategyDialog(){
//            this(Alert.AlertType.INFORMATION,Lang.getConformation(),"Wollen Sie die ausgewählten Regel(n) ("+numbers+") wirklich in den Pool: " + target.getCrgplIdentifier() + " kopieren?");
//        }

    public ImportStrategyDialog(Alert.AlertType pType, String pHeaderText, String pText) {
        super(pType,
                pHeaderText,
                pText,
                "",
                ButtonType.YES, ButtonType.NO);
        setOnShowing(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent t) {
                addContent(createContent());
            }
        });
        getDialogPane().setMinHeight(300);
        initModality(Modality.APPLICATION_MODAL);
        initOwner(MainApp.getWindow());
//            addContent(createContent());
    }

    public final RuleOverrideFlags getOverrideFlag() {
        return cbOverrideFlag.getSelectedItem();
    }

    public final RuleImportCheckFlags getImportCheckFlag() {
        return chkBoxImportCheckFlag.isSelected() ? RuleImportCheckFlags.CHECK_4_COLLISIONS : RuleImportCheckFlags.NO_CHECK_4_COLLISIONS;
    }

    private Node createContent() {
        cbOverrideFlag = new LabeledComboBox<>("Konfliktstrategie", RuleOverrideFlags.requiredValues());
        cbOverrideFlag.select(Session.instance().getRuleOverrideFlag());
        cbOverrideFlag.setButtonCell(new ListCell<RuleOverrideFlags>() {
            @Override
            protected void updateItem(RuleOverrideFlags t, boolean bln) {
                super.updateItem(t, bln); //To change body of generated methods, choose Tools | Templates.
                if (t == null || bln) {
                    setText("");
                    cbOverrideFlag.getControl().setTooltip(null);
                    return;
                }
                setText(t.getDisplayText());
                cbOverrideFlag.getControl().setTooltip(new Tooltip(t.getDescription()));
            }

        });
        cbOverrideFlag.setConverter(new StringConverter<RuleOverrideFlags>() {
            @Override
            public String toString(RuleOverrideFlags t) {
                if (t == null) {
                    return null;
                }
                return t.getDisplayText() + "\n" + t.getDescription();
            }

            @Override
            public RuleOverrideFlags fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        chkBoxImportCheckFlag = new LabeledCheckBox("Duplikatprüfung");
        chkBoxImportCheckFlag.setSelected(RuleImportCheckFlags.CHECK_4_COLLISIONS.equals(Session.instance().getRuleImportCheckFlag()));
        chkBoxImportCheckFlag.getControl().setText("Duplikatprüfung Ja/Nein");
        VBox box = new VBox(5, cbOverrideFlag, chkBoxImportCheckFlag);
        return box;
    }

    private void addContent(Node pNode) {
        if (getDialogPane().getContent() instanceof VBox) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ((Pane) getDialogPane().getContent()).getChildren().add(pNode);
                }
            });
        }
    }

}
