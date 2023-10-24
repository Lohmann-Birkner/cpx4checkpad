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
package de.lb.cpx.client.core.menu.model;

import de.lb.cpx.client.core.menu.fx.ToolbarMenuFXMLController.Toolbar;
import de.lb.cpx.client.core.util.ResourceLoader;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import org.controlsfx.glyphfont.FontAwesome;

/**
 * Simple ToolbarMenuItem for one entry and one action
 *
 * @author wilde
 */
public class ToolbarSingleMenuItem extends ToolbarMenuItem {

    private static final Logger LOG = Logger.getLogger(ToolbarSingleMenuItem.class.getName());

    private Button extendedMenuBtn;
    private Button reducedMenuBtn;
    //on action, handles action which should happen of user selected menu item
    private ObjectProperty<EventHandler<ActionEvent>> onActionProperty;

    /**
     * creates new instance
     *
     * @param pBar bar
     */
    public ToolbarSingleMenuItem(Toolbar pBar) {
        super(pBar);
        glyphProperty().addListener(new ChangeListener<FontAwesome.Glyph>() {
            @Override
            public void changed(ObservableValue<? extends FontAwesome.Glyph> observable, FontAwesome.Glyph oldValue, FontAwesome.Glyph newValue) {
                extendedMenuBtn.setGraphic(ResourceLoader.getGlyph(newValue));
                reducedMenuBtn.setGraphic(ResourceLoader.getGlyph(newValue));
            }
        });
    }

    @Override
    public Node getExtendedNode() {
        if (getExtendedRoot() == null) {
            extendedMenuBtn = new Button();
            extendedMenuBtn.getStyleClass().add("menu-tab-button");
            extendedMenuBtn.tooltipProperty().bind(tooltipProperty());
//            extendedMenuBtn.graphicProperty().bind(graphicBinding);
//            extendedMenuBtn.graphicProperty().bindBidirectional(glyphProperty());
            extendedMenuBtn.textProperty().bind(extendedTitleProperty());
            extendedMenuBtn.onActionProperty().bind(onActionProperty());
            extendedMenuBtn.setMaxWidth(Double.MAX_VALUE);
            setExtendedRoot(extendedMenuBtn);
        }
        return getExtendedRoot();
    }

    @Override
    public Node getReducedNode() {
        if (getReducedRoot() == null) {
            reducedMenuBtn = new Button();
            reducedMenuBtn.getStyleClass().add("menu-tab-button");
            reducedMenuBtn.tooltipProperty().bind(tooltipProperty());
//            reducedMenuBtn.graphicProperty().bind(graphicBinding);
            reducedMenuBtn.onActionProperty().bind(onActionProperty());
            reducedMenuBtn.setMaxWidth(Double.MAX_VALUE);
            setReducedRoot(reducedMenuBtn);
        }
        return getReducedRoot();
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        if (onActionProperty == null) {
            onActionProperty = new SimpleObjectProperty<>(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    LOG.warning("No event definied on click");
                }
            });
        }
        return onActionProperty;
    }

    /**
     * @param pHandler set handler to be executed if menu item is clicked
     */
    public final void setOnAction(EventHandler<ActionEvent> pHandler) {
        onActionProperty().set(pHandler);
    }

    /**
     * @return eventhandler, whisch is executed if menu item is selected
     */
    public final EventHandler<ActionEvent> getOnActionEvent() {
        return onActionProperty().get();
    }

}
