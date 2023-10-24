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
import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.client.core.model.fx.file_chooser.FileChooserControl;
import de.lb.cpx.client.core.model.fx.file_chooser.FileChooserFactory;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.ruleeditor.menu.labeled.LabeledFileChooser;
import de.lb.cpx.ruleviewer.util.PoolTypeHelper;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import java.io.File;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.util.StringConverter;
import org.controlsfx.validation.Validator;

/**
 *
 * @author wilde
 */
public class ExportPoolDialog extends FormularDialog<ExportPoolDialog.ExportTarget> {

    private final LabeledComboBox<CrgRulePools> comboBox;
    private final LabeledFileChooser chooser;

    public ExportPoolDialog() {
        super("Pool exportieren", MainApp.getWindow());

        comboBox = new LabeledComboBox<>("Verfügbare Pools");
        comboBox.setConverter(new StringConverter<CrgRulePools>() {
            @Override
            public String toString(CrgRulePools t) {
                if (t == null) {
                    return null;
                }
                return t.getCrgplIdentifier() + " " + (PoolTypeEn.DEV.equals(PoolTypeHelper.getPoolType(t)) ? "(Arbeitspool)" : "(Produktivpool)");
            }

            @Override
            public CrgRulePools fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        chooser = new LabeledFileChooser();
        chooser.setTitle("Ordner");
        chooser.getControl().setMode(FileChooserControl.Mode.DIR);
        chooser.getControl().setChoosenPathType(FileChooserFactory.RULE_EXPORT);

        addControls(comboBox, chooser);

        getDialogSkin().getButton(ButtonType.OK).setText("Exportieren");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getValidation().registerValidator(comboBox.getControl(), Validator.createEmptyValidator("Bitte einen Pool angeben"));
                getValidation().registerValidator(chooser.getControl(), Validator.createEmptyValidator("Bitte ein Export-Verzeichnis angeben"));
            }
        });

    }

    public void setPools(List<CrgRulePools> pPools) {
        comboBox.getItems().addAll(pPools);
    }

    @Override
    public ExportTarget onSave() {
        return new ExportTarget(chooser.getControl().getFile(), comboBox.getSelectedItem());
    }

    public CrgRulePools getPool() {
        return onSave().getPool();
    }

    public File getDir() {
        return onSave().getFile();
    }

    public class ExportTarget {

        private final File file;
        private final CrgRulePools pool;

        public ExportTarget(File pTarget, CrgRulePools pContent) {
            file = pTarget;
            pool = pContent;
        }

        public File getFile() {
            return file;
        }

        public CrgRulePools getPool() {
            return pool;
        }

    }
}
