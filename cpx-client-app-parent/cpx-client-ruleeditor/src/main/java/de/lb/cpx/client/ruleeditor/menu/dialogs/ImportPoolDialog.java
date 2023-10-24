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

import de.lb.cpx.client.core.model.fx.file_chooser.FileChooserFactory;
import de.lb.cpx.client.ruleeditor.menu.labeled.LabeledFileChooser;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import java.io.File;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.stage.FileChooser;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

/**
 *
 * @author wilde
 */
public class ImportPoolDialog extends CreatePoolDialog {

    private final LabeledFileChooser importChooser;

    public ImportPoolDialog(PoolTypeEn pType) {
        super("Pool importieren", pType);

        importChooser = new LabeledFileChooser();
        importChooser.getControl().setChoosenPathType(FileChooserFactory.RULE_IMPORT);
        importChooser.getControl().addExtensionFilters(
                //                        new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("XML", "*.xml")
        //                        new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        importChooser.getControl().getExtensionFilters();
        addControls(importChooser);
        getDialogSkin().getControlContainer();
        getDialogSkin().getButton(ButtonType.OK).setText("Importieren");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getValidation().registerValidator(importChooser.getControl(), new Validator<Object>() {
                    @Override
                    public ValidationResult apply(Control t, Object u) {
                        ValidationResult result = new ValidationResult();
                        File file = importChooser.getControl().getFile();
                        result.addErrorIf(t, "Bitte eine Datei für den Import angeben", file == null);
                        if (file != null) {
                            result.addErrorIf(t, "Das ausgewählte Element ist keine Datei", importChooser.getControl().getFile().isDirectory());
                        }
                        return result;
                    }
                });//Validator.createEmptyValidator("Bitte eine Datei für den Import angeben"));
            }
        });
    }

    public File getFile() {
        return importChooser.getControl().getFile();
    }

}
