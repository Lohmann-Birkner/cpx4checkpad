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
package de.lb.cpx.client.core.model.fx.toggle;

import de.lb.cpx.client.core.model.fx.toggle.skin.ToggleGroupControlSkin;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * ToggleGroup-Control class to handles these Toggles
 *
 * @author wilde
 * @param <T> toggle group content
 */
public class ToggleGroupControl<T extends ToggleButton> extends Control {

    public static final Double DEFAULT_SPACING = 15.0d;
    private final BooleanProperty itemFocusedProperty = new SimpleBooleanProperty();
    private final ObservableList<T> toggleList = FXCollections.observableArrayList();
    private ObjectProperty<ToggleGroup> toggleGroupProperty;
    private ObjectProperty<Orientation> orientationProperty;
    private DoubleProperty spacingProperty;
    private SimpleObjectProperty<Pos> alignmentProperty;

    /**
     * construct new instance no-arg for scene builder
     */
    public ToggleGroupControl() {
        super();
        setFocusTraversable(true);
        toggleList.addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends T> c) {
                if (c.next()) {
                    if (c.wasAdded()) {
                        for (T added : c.getAddedSubList()) {
                            added.setFocusTraversable(false);
                            added.setToggleGroup(getToggleGroup());
                            added.setOnMouseClicked(new EventHandler<Event>() {
                                @Override
                                public void handle(Event event) {
                                    if (!isFocused()) {
                                        requestFocus();
                                    }
                                }
                            });

                        }
                    }
                    if (c.wasRemoved()) {
                        for (T removed : c.getRemoved()) {
                            removed.setToggleGroup(null);
                        }
                    }
                }

            }
        });
        addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (!isFocused()) {
                    return;
                }
                switch (getOrientation()) {
                    case HORIZONTAL:
                        handleHorizontal(event.getCode());
                        break;
                    case VERTICAL:
                        handleVertical(event.getCode());
                }
            }

        });
    }

    private void moveSelectionRight() {
        int index = getToggleList().indexOf(getToggleGroup().getSelectedToggle());
        if (getToggleList().size() - 1 > index) {
            getToggleGroup().selectToggle(getToggleGroup().getToggles().get(index + 1));
        }
    }

    private void moveSelectionLeft() {
        int index = getToggleList().indexOf(getToggleGroup().getSelectedToggle());
        if (index > 0) {
            getToggleGroup().selectToggle(getToggleGroup().getToggles().get(index - 1));
        }
    }

    private void handleHorizontal(KeyCode code) {
        if (KeyCode.RIGHT.equals(code)) {
            moveSelectionRight();
        }
        if (KeyCode.LEFT.equals(code)) {
            moveSelectionLeft();
        }
    }

    private void handleVertical(KeyCode code) {
        if (KeyCode.UP.equals(code)) {
            moveSelectionRight();
        }
        if (KeyCode.DOWN.equals(code)) {
            moveSelectionLeft();
        }
    }

    /**
     * @return property as indicator if an item of the group holds focus
     */
    public BooleanProperty itemFocusedProperty() {
        return itemFocusedProperty;
    }

    /**
     * @return indicator if an item of the group holdes focus
     */
    public Boolean isItemFocused() {
        return itemFocusedProperty().get();
    }

    /**
     * @return observalbe list for toggles in group
     */
    public ObservableList<T> getToggleList() {
        return toggleList;
    }

    /**
     * @param pToggle toggle to add
     */
    public void addToggle(T pToggle) {
        toggleList.add(pToggle);
    }

    /**
     *
     * @param pToggles toggles to add
     */
    @SuppressWarnings("unchecked")
    public void addToggles(T... pToggles) {
        toggleList.addAll(pToggles);
    }

    /**
     * @param pToggles list of toggles to add
     */
    public void addToggles(List<T> pToggles) {
        toggleList.addAll(pToggles);
    }

    /**
     * @return toggle group property to handle all toggles
     */
    public ObjectProperty<ToggleGroup> toggleGroupProperty() {
        if (toggleGroupProperty == null) {
            toggleGroupProperty = new SimpleObjectProperty<>(new ToggleGroup());
        }
        return toggleGroupProperty;
    }

    /**
     * @return get the current toggle group for toggles
     */
    public final ToggleGroup getToggleGroup() {
        return toggleGroupProperty().get();
    }

    /**
     * @param pGroup new toogle group for toggles
     */
    public void setToggleGroup(ToggleGroup pGroup) {
        toggleGroupProperty().set(pGroup);
    }

    /**
     * @return property to react to orientation changes to react to display
     * changes
     */
    public ObjectProperty<Orientation> orientationProperty() {
        if (orientationProperty == null) {
            orientationProperty = new SimpleObjectProperty<>(Orientation.HORIZONTAL);
        }
        return orientationProperty;
    }

    /**
     * @return the current orientation, default horizontal
     */
    public Orientation getOrientation() {
        return orientationProperty().get();
    }

    /**
     * @param pOrientation set new orientation trigger ui update, default value
     * horizontal
     */
    public void setOrientation(Orientation pOrientation) {
        orientationProperty().set(pOrientation);
    }

    /**
     * @return spacing as property between the toggles
     */
    public DoubleProperty spacingProperty() {
        if (spacingProperty == null) {
            spacingProperty = new SimpleDoubleProperty(DEFAULT_SPACING);
        }
        return spacingProperty;
    }

    /**
     * @return currently set spacing between toogles, default 15
     */
    public Double getSpacing() {
        return spacingProperty().get();
    }

    /**
     * @param pSpacing new spacing between toggles
     */
    public void setSpacing(double pSpacing) {
        spacingProperty().set(pSpacing);
    }

    /**
     * @return alignment of the toggles in group as property
     */
    public ObjectProperty<Pos> alignmentProperty() {
        if (alignmentProperty == null) {
            alignmentProperty = new SimpleObjectProperty<>(Pos.BOTTOM_LEFT);
        }
        return alignmentProperty;
    }

    /**
     * @return get current toggle alignment
     */
    public Pos getAlignment() {
        return alignmentProperty().get();
    }

    /**
     * @param pAlignment set new alignment for toggles
     */
    public void setAlignment(Pos pAlignment) {
        alignmentProperty().set(pAlignment);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ToggleGroupControlSkin(this);
    }

}
