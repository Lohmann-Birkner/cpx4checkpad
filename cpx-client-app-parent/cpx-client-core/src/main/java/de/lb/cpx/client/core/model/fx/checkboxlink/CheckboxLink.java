/*
 * Copyright (c) 2016 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2018  shahin - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.checkboxlink;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 *
 * @author Shahin
 */
public class CheckboxLink extends Control {

    private static final Logger LOG = Logger.getLogger(CheckboxLink.class.getName());
    private final EventHandler<ActionEvent> defaultHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            LOG.log(Level.INFO, "HyperLink: {0} was clicked!", getText());
        }
    };

    private final StringProperty textProperty = new SimpleStringProperty();
    private final BooleanProperty visibleProperty = new SimpleBooleanProperty();
    private final BooleanProperty selectedProperty = new SimpleBooleanProperty();
    private ObjectProperty<EventHandler<ActionEvent>> onActionProperty;

    @Override
    protected Skin<?> createDefaultSkin() {
        return new CheckboxLinkSkin(this);
    }

    public StringProperty textProperty() {
        return textProperty;
    }

    public void setText(String pText) {
        textProperty().set(pText);
    }

    public String getText() {
        return textProperty().get();
    }

    public final void setSelectedValue(boolean value) {
        selectedValueProperty().set(value);
    }

    public final boolean isSelected() {
        return selectedValueProperty().get();
    }

    public final BooleanProperty selectedValueProperty() {

        return selectedProperty;
    }

    public BooleanProperty visiblePropertyLink() {
        return visibleProperty;
    }

    public void setVisibleLink(boolean visible) {
        visiblePropertyLink().set(visible);
    }

    public boolean getVisibleLink() {
        return visibleProperty().get();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        if (onActionProperty == null) {
            onActionProperty = new SimpleObjectProperty<>(defaultHandler);
        }
        return onActionProperty;
    }

    public void setOnAction(EventHandler<ActionEvent> pEvent) {
        onActionProperty().set(pEvent);
    }

    public EventHandler<ActionEvent> getOnAction() {
        return onActionProperty().get();
    }

}
