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
package de.lb.cpx.client.core.model.fx.labeled;

import de.lb.cpx.client.core.model.fx.toggle.ToggleGroupControl;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Skin;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.util.Callback;

/**
 * LabeledToogleGroup
 *
 * @author wilde
 * @param <T> content of the toggle group
 * @param <Z> Object to store in toggle
 */
public class LabeledToggleGroup<T extends ToggleButton, Z> extends LabeledControl<ToggleGroupControl<T>> {

    public LabeledToggleGroup() {
        super("Label", new ToggleGroupControl<T>());
    }

    public LabeledToggleGroup(String pText) {
        this();
        setTitle(pText);
        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            @SuppressWarnings("unchecked")
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                ((T) getControl().getToggleGroup().getSelectedToggle()).requestFocus();
            }
        });
        groupAlignmentProperty().addListener(new ChangeListener<Pos>() {
            @Override
            public void changed(ObservableValue<? extends Pos> ov, Pos t, Pos t1) {
                getControl().setAlignment(t1);
            }
        });
        getControl().setAlignment(getGroupAlignment());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LabeledControlSkin<>(this);
    }

    public void setValues(Z[] values) {
        List<T> toggles = new ArrayList<>();
        for (Z obj : values) {
            T toggle = getToogleFactory().call(obj.toString());
            toggle.setUserData(obj);
            toggles.add(toggle);
        }
        getControl().addToggles(toggles);
    }

    @SuppressWarnings("unchecked")
    private Callback<String, T> toggleFactory = (String param) -> {
        return (T) new ToggleButton(param);
    };

    public void setToggleFactory(Callback<String, T> pFactory) {
        toggleFactory = pFactory;
    }

    public Callback<String, T> getToogleFactory() {
        return toggleFactory;
    }

    public void select(Z pSelect) {
        for (Toggle t : getControl().getToggleGroup().getToggles()) {
            if (t.getUserData().equals(pSelect)) {
                getControl().getToggleGroup().selectToggle(t);
            }
        }
    }

    public void selectFirst() {
        Toggle first = getControl().getToggleGroup().getToggles().get(0);
        if (first == null) {
            return;
        }
        getControl().getToggleGroup().selectToggle(first);
    }

    @SuppressWarnings("unchecked")
    public Z getSelected() {

        return (Z) getControl().getToggleGroup().getSelectedToggle() != null
                ? (Z) getControl().getToggleGroup().getSelectedToggle().getUserData() : null;
    }

    private ObjectProperty<Pos> groupAlignmentProperty;

    public final ObjectProperty<Pos> groupAlignmentProperty() {
        if (groupAlignmentProperty == null) {
            groupAlignmentProperty = new SimpleObjectProperty<>(Pos.BOTTOM_LEFT);
        }
        return groupAlignmentProperty;
    }

    public void setGroupAlignment(Pos pAlignment) {
        groupAlignmentProperty().set(pAlignment);
    }

    public final Pos getGroupAlignment() {
        return groupAlignmentProperty().get();
    }
}
