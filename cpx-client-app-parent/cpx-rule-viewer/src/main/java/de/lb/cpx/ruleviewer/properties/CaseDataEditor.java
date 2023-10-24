/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.properties;

import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

/**
 *
 * @author wilde
 */
public class CaseDataEditor implements PropertyEditor<Criterion> {

//    private final ComboBox<CaseDataEn> cbEditor;
    private final PropertySheet.Item item;
//    private final ObjectProperty<CaseDataEn> value = new SimpleObjectProperty<>();

    public CaseDataEditor(final PropertySheet.Item item) {
        this.item = item;
//        cbEditor = new ComboBox<>(FXCollections.observableArrayList(CaseDataEn.getCategories()));
//        setValue((CaseDataEn) item.getValue());
//        cbEditor.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CaseDataEn>() {
//            @Override
//            public void changed(ObservableValue<? extends CaseDataEn> observable, CaseDataEn oldValue, CaseDataEn newValue) {
//                setValue(newValue);
//                item.setValue(getValue());
//            }
//        });

//        if (item.getValue() != null) {
//            btnEditor.getSelectionModel().select((CaseDataEn) item.getValue());
//            value.set((Link) item.getValue());
//        } else {
//            btnEditor.getSelectionModel().select(CaseDataEn.EMPTY);
//        }
//        btnEditor.setAlignment(Pos.CENTER_LEFT);
//        btnEditor.setOnAction((ActionEvent event) -> {
//            displayPopupEditor();
//        });
    }

    @Override
    public Node getEditor() {
        return new TextField();//cbEditor;
    }
//    @Override
//    public CaseDataEn getValue() {
//        return value.get();
//    }
//    @Override
//    public void setValue(CaseDataEn t) {
//        value.set(t);
//        if (t != null) {
//            cbEditor.getSelectionModel().select(t);
//        }
//    }
//    public void setValues(CaseDataEn[] allowedOperators) {
//        cbEditor.setItems(FXCollections.observableArrayList(allowedOperators));
////        cbEditor.getItems().addAll(allowedOperators);
//    }

    @Override
    public Criterion getValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setValue(Criterion value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
