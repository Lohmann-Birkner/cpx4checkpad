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
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

/**
 * Simple Menuitem for one menu item and multiple actions
 *
 * @author wilde
 */
public class ToolbarMultiMenuItem extends ToolbarSingleMenuItem {

    private PopOver popover;

    //list of buttons to show in menu
    private List<Button> items = new ArrayList<>();

    public ToolbarMultiMenuItem(Toolbar pBar) {
        super(pBar);
        setOnAction((ActionEvent event) -> {
            if (popover == null) {
                VBox content = new VBox();
                content.getChildren().addAll(getItems());
                content.setFillWidth(true);
                popover = new PopOver(content);
                popover.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
                popover.setDetachable(false);
            }
            if (!popover.isShowing()) {
                popover.show((Node) event.getSource());
            }
        });
    }

    /**
     * @param pItems set new list as items
     */
    public final void setItems(List<Button> pItems) {
        items = new ArrayList<>(pItems);
    }

    /**
     * @return current list of items in the menu
     */
    public final List<Button> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * @param pButton button to add in list
     */
    public final void add(Button pButton) {
        items.add(pButton);
    }

    /**
     * @param pButton button to remove
     * @return indicator if remove was successful
     */
    public final boolean remove(Button pButton) {
        return items.remove(pButton);
    }

    /**
     * hide popover if shown
     */
    public void hidePopover() {
        if (popover != null) {
            popover.hide();
        }
    }

    /**
     * clear focus
     */
    public void clearFocus() {
        focusNode(null);
    }
}
