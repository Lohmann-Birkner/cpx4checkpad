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
package de.lb.cpx.ruleviewer.model.ruletable.content;

import de.lb.cpx.client.core.model.fx.checked_combobox.CpxComboBoxListViewSkin;
import de.lb.cpx.client.core.model.fx.labeled.LabeledComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 *
 * @author wilde
 */
public class RuleTableContentChooserSkin extends SkinBase<RuleTableContentChooser> {

    public RuleTableContentChooserSkin(RuleTableContentChooser pSkinnable) {
        super(pSkinnable);
        getChildren().add(initRoot());
    }

    private Parent initRoot() {
        LabeledComboBox<RuleTableContentEnum> cbType = new LabeledComboBox<>("Art");
        cbType.setItems(RuleTableContentEnum.getSortedList());
        cbType.getControl().getSelectionModel().select(RuleTableContentEnum.FREE_TEXT);

        CpxComboBoxListViewSkin<RuleTableContentEnum> sk = new CpxComboBoxListViewSkin<>(cbType.getControl());
        sk.setHideOnClick(true);
        sk.getListView().setFixedCellSize(27);
        cbType.getControl().setSkin(sk);

        cbType.setConverter(new StringConverter<RuleTableContentEnum>() {
            @Override
            public String toString(RuleTableContentEnum object) {
                if (object == null) {
                    return null;
                }
                return object.getTitle();
            }

            @Override
            public RuleTableContentEnum fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        VBox container = new VBox(5, cbType, getSkinnable().getEditorFactory().call(cbType.getSelectedItem())/*getEditorFieldForType(cbType.getControl().getSelectionModel().getSelectedItem())*/);
        container.setFillWidth(true);

        cbType.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RuleTableContentEnum>() {
            @Override
            public void changed(ObservableValue<? extends RuleTableContentEnum> observable, RuleTableContentEnum oldValue, RuleTableContentEnum newValue) {
                if (container.getChildren().size() == 2) {
                    container.getChildren().remove(1);
                    container.getChildren().add(getSkinnable().getEditorFactory().call(newValue));
                }
            }
        });
        container.setMinWidth(VBox.USE_PREF_SIZE);
        container.setPrefWidth(250);
        container.setMaxWidth(VBox.USE_PREF_SIZE);
        return container;
    }

//     private Node getEditorFieldForType(RuleTableContentEnum pType) {
//        String title = "Wert";
//        if (pType == null) {
//            return new LabeledTextField(title, createEditTextField());
//        }
//        switch (pType) {
//            case FREE_TEXT:
//                return new LabeledTextField(title, createEditTextField());
//            default:
//                if (pType.isEnum()) {
//                    return getCheckComboBox(title, pType);
//                }
//                return new LabeledTextField(title, createEditTextField());
//        }
//    }
//
//    private LabeledCheckComboBox<CpxEnumInterface> getCheckComboBox(String pTitle, RuleTableContentEnum pType) {
//        LabeledCheckComboBox<CpxEnumInterface> chkComboBox = new LabeledCheckComboBox<>(pTitle, pType.getValues());
//        chkComboBox.getListView().setFixedCellSize(27);
//        chkComboBox.setConverter(new StringConverter<CpxEnumInterface>() {
//            @Override
//            public String toString(CpxEnumInterface object) {
//                if (object == null) {
//                    return null;
//                }
//                return object.getTranslation().getValue();
//            }
//
//            @Override
//            public CpxEnumInterface fromString(String string) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        });
//        return chkComboBox;
//    }
//
//    private TextField createEditTextField() {
//        return new TextField();
//    }
}
