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
package de.lb.cpx.client.ruleeditor.menu.popover;

import de.lb.cpx.client.core.model.fx.button.SaveButton;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import de.lb.cpx.client.core.model.fx.labeled.LabeledDatePicker;
import de.lb.cpx.client.core.model.fx.labeled.LabeledTextField;
import de.lb.cpx.client.core.model.fx.popover.AutoFitPopOver;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;

/**
 *
 * @author wilde
 */
public class EditPoolPopOver extends AutoFitPopOver {

    public EditPoolPopOver(CrgRulePools pPool, PoolTypeEn pType) {

        LabeledTextField tfTitle = new LabeledTextField("Pool-Titel");
        tfTitle.getStyleClass().add("labeled-control-label2");
        tfTitle.setText(pPool.getCrgplIdentifier());
//        LabeledToggleGroup<RadioButton, String> group = new LabeledToggleGroup<>("Aktiv");
//        group.setToggleFactory(RadioButton::new);
//        group.setValues(new String[]{"Ja", "Nein"});
//        group.select(pPool.isCrgplIsActive() ? "Ja" : "Nein");
        
        LabeledComboBox<Integer> cbYear = new LabeledComboBox<>("Pool-Jahr");
        cbYear.getItems().addAll(getValidYears());
        cbYear.select(pPool.getCrgplPoolYear());

        LabeledDatePicker dpTo = new LabeledDatePicker("Bis");
        dpTo.setDate(pPool.getCrgplTo());
        LabeledDatePicker dpFrom = new LabeledDatePicker("Von");
        dpFrom.setDate(pPool.getCrgplFrom());

        LabeledComboBox<PoolTypeEn> cbType = new LabeledComboBox<>("Pool-Art");
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
        GridPane pane = new GridPane();
        pane.setVgap(5);
        pane.setHgap(5);
        pane.setPadding(new Insets(5));
        pane.addRow(0, tfTitle/*, group*/);
        pane.addRow(1, dpFrom, dpTo);
        pane.addRow(2, cbYear, cbType);

        SaveButton btnEdit = new SaveButton();
        btnEdit.setTooltip(new Tooltip("Pool speichern"));
        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                pPool.setCrgplIdentifier(tfTitle.getText());
                pPool.setCrgplFrom(dpFrom.getDate());
                pPool.setCrgplTo(dpTo.getDate());
                pPool.setCrgplPoolYear(cbYear.getSelectedItem());
//                pPool.setCrgplIsActive(group.getSelected().equals("Ja"));
                if (getUpdateCallback() != null) {
                    getUpdateCallback().call(pPool);
                }
            }
        });
        HBox menu = new HBox(btnEdit);
        menu.setAlignment(Pos.CENTER_RIGHT);
        menu.setSpacing(5);
        VBox content = new VBox(menu, new Separator(Orientation.HORIZONTAL), pane);
        content.setSpacing(5);

        setContentNode(content);
        content.getStylesheets().add(getClass().getResource("/styles/ruleeditor.css").toExternalForm());
        content.getStyleClass().add("edit-popover");
        setDefaultArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        setDetachable(false);
        setAutoFix(true);
    }

    private Callback<CrgRulePools, Boolean> updateCallback;

    public Callback<CrgRulePools, Boolean> getUpdateCallback() {
        return updateCallback;
    }

    public void setUpdateCallback(Callback<CrgRulePools, Boolean> pCallback) {
        updateCallback = pCallback;
    }

    private List<Integer> getValidYears() {
        List<Integer> years = new ArrayList<>();
        int now = LocalDate.now().getYear();
        for (int i = now - 5; i <= now + 5; i++) {
            years.add(i);
        }
        return years;
    }
}
