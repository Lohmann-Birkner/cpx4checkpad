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
package de.lb.cpx.ruleviewer.model.search;

import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;

/**
 * Basic search via combobox for fast access and menu button for detail menu
 *
 * @author wilde
 * @param <T> Objects stored in control
 */
public class BasicSearchComboBox<T> extends Control {

    private ObservableList<T> items = null;

    public List<T> getItems() {
        if (items == null) {
            items = populateItems();
        }
        return new ArrayList<>(items);
    }

    protected ObservableList<T> populateItems() {
        if (items == null) {
            items = FXCollections.observableArrayList();
        }
        return items;
    }

    private ObjectProperty<T> selectedItemProperty;

    public ObjectProperty<T> selectedItemProperty() {
        if (selectedItemProperty == null) {
            selectedItemProperty = new SimpleObjectProperty<>();
        }
        return selectedItemProperty;
    }

    public T getSelectedItem() {
        return selectedItemProperty().get();
    }

    public void selectItem(T pItem) {
        selectedItemProperty().set(pItem);
    }

    private ObjectProperty<Criterion> criterionProeprty;

    public ObjectProperty<Criterion> criterionProperty() {
        if (criterionProeprty == null) {
            criterionProeprty = new SimpleObjectProperty<>();
        }
        return criterionProeprty;
    }

    public Criterion getCriterion() {
        return criterionProperty().get();
    }

    public void setCriterion(Criterion pCriterion) {
        criterionProperty().set(pCriterion);
    }
}
