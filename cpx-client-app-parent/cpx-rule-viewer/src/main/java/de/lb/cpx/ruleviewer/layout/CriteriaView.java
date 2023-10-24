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
package de.lb.cpx.ruleviewer.layout;

import de.lb.cpx.rule.criteria.model.Criteria;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.text.Font;
import javafx.util.Callback;

/**
 * Criteria View
 *
 * @author wilde
 */
public class CriteriaView extends Control {

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new CriteriaViewSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(CriteriaView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin(); //To change body of generated methods, choose Tools | Templates.
    }

    private ObjectProperty<Font> fontProperty;

    public ObjectProperty<Font> fontProperty() {
        if (fontProperty == null) {
            fontProperty = new SimpleObjectProperty<>(Font.getDefault());
        }
        return fontProperty;
    }

    public void setFont(Font pFont) {
        fontProperty().set(pFont);
    }

    public Font getFont() {
        return fontProperty().get();
    }
    private Callback<Criterion, Void> onSelectedCallback;

    public Callback<Criterion, Void> getOnSelectedCallback() {
        return onSelectedCallback;
    }

    public void setOnSelectedCallback(Callback<Criterion, Void> pOnSelected) {
        onSelectedCallback = pOnSelected;
    }
    private ObjectProperty<Criterion> selectedItemProperty;

    protected ObjectProperty<Criterion> selectedItemProperty() {
        if (selectedItemProperty == null) {
            selectedItemProperty = new SimpleObjectProperty<>();
        }
        return selectedItemProperty;
    }

    public void selectCriterion(Criterion pCriterion) {
        selectedItemProperty().set(pCriterion);
    }

//    public Criterion getSelectedItem() {
//        return selectedItemProperty().get();
//    }
    private final ObservableList<Criteria> items = FXCollections.observableArrayList();

    public ObservableList<Criteria> getItems() {
        return items;
    }
}
