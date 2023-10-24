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
package de.lb.cpx.client.core.model.fx.menu;

import de.lb.cpx.client.core.util.ResourceLoader;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;

import org.controlsfx.glyphfont.FontAwesome;

/**
 * Menued Control with add and remove button
 *
 * @author wilde
 * @param <T> control im menu
 */
public abstract class AddMenuedControl<T extends Control> extends MenuedControl<T> {

    private final Button add = new Button();
    private final Button remove = new Button();

    /**
     * construct new instance
     *
     * @param pControl control to wrap menu around
     */
    public AddMenuedControl(T pControl) {
        super(pControl);
        add.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.PLUS));
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //dialog to react to error?
                add();
            }
        });
        remove.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.MINUS));
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //dialog to react to error?
                remove();
            }
        });
        addMenuNodes(add, remove);
    }

    /**
     * execute add operation
     *
     * @return if add operation was successfull
     */
    public abstract boolean add();

    /**
     * execute remove operation
     *
     * @return if add operation was successfull
     */
    public abstract boolean remove();

    /**
     * @return Button to handle add
     */
    public Button getAddButton() {
        return add;
    }

    /**
     * @return bbutton to handle remove
     */
    public Button getRemoveButton() {
        return remove;
    }

}
