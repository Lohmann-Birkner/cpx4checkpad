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

import de.lb.cpx.client.core.model.fx.dialog.FormularDialog;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.ruleeditor.MainApp;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.util.StringConverter;
import org.controlsfx.validation.Validator;

/**
 *
 * @author wilde
 */
public class CreatePoolDialog extends FormularDialog<CrgRulePools> {

    private final LabeledComboBox<PoolTypeEn> cbType;
    private final LabeledTextField tfTitle;
    private final LabeledDatePicker dpTo;
    private final LabeledDatePicker dpFrom;
    private final LabeledComboBox<Integer> cbYear;

    public CreatePoolDialog(PoolTypeEn pType) {
        this("Pool erstellen", pType);
    }

    public CreatePoolDialog(String pTitle, PoolTypeEn pType) {
        super(pTitle, MainApp.getWindow());

        tfTitle = new LabeledTextField("Pool-Titel");

        cbYear = new LabeledComboBox<>("Pool-Jahr");
        cbYear.getItems().addAll(getValidYears());
        cbYear.select(LocalDate.now().getYear());

        dpTo = new LabeledDatePicker("Bis");
        dpFrom = new LabeledDatePicker("Von");

        cbType = new LabeledComboBox<>("Pool-Art");
        cbType.getItems().addAll(PoolTypeEn.values());
        cbType.setConverter(new StringConverter<PoolTypeEn>() {
            @Override
            public String toString(PoolTypeEn t) {
                if (t == null) {
                    return null;
                }
                return t.getTitle();
            }

            @Override
            public PoolTypeEn fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        cbType.select(pType);
        if (pType != null) {
            cbType.setDisable(true);
        }
        addControls(tfTitle, cbType, dpFrom, dpTo, cbYear);

        getDialogSkin().getButton(ButtonType.OK).setText("Speichern");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getValidation().registerValidator(tfTitle.getControl(), Validator.createEmptyValidator("Bitte einen Titel angeben"));
                getValidation().registerValidator(dpTo.getControl(), Validator.createEmptyValidator("Bitte ein Bis-Datum angeben"));
                getValidation().registerValidator(dpFrom.getControl(), Validator.createEmptyValidator("Bitte ein Von-Datum angeben"));
                getValidation().registerValidator(cbType.getControl(), Validator.createEmptyValidator("Bitte eine Pool-Art angeben"));
            }
        });
    }

    @Override
    public CrgRulePools onSave() {
        CrgRulePools pool = CrgRulePools.getTypeInstance(cbType.getSelectedItem());
        pool.setCrgplIdentifier(tfTitle.getText());
        pool.setCrgplFrom(Date.valueOf(dpFrom.getControl().getValue()));
        pool.setCrgplTo(Date.valueOf(dpTo.getControl().getValue()));
        pool.setCrgplPoolYear(cbYear.getSelectedItem());
        pool.setCrgplIsActive(true);
        return pool;
    }

    public PoolTypeEn getType() {
        return cbType.getSelectedItem();
    }

    private List<Integer> getValidYears() {
        List<Integer> years = new ArrayList<>();
        int now = LocalDate.now().getYear();
//        for(int i = now-5;i<=now+5;i++){
//            years.add(i);
//        }
        for (int i = 2013; i <= now + 1; i++) {
            years.add(i);
        }
        return years;
    }

}
