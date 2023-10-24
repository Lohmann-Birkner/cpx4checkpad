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
package de.lb.cpx.client.core.model.fx.label;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

/**
 * Make Behavior of FilterLabel, public Label shows tooltip if mouse is entered
 * to change displayed tooltip text
 *
 * @author wilde
 */
public class TooltipLabel extends Label {

    public TooltipLabel() {
        super();
        //CPX-1201
        setTooltip(fetchTooltipText() == null ? null : new Tooltip(fetchTooltipText()));
        //set tooltip on mouse enter
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (getTooltip() == null) {
                    String tooltip = fetchTooltipText();
                    setTooltip(tooltip != null ? new Tooltip(tooltip) : null);
                }
            }
        });
    }

    public TooltipLabel(String pText) {
        this();
        setText(pText);
    }

    /**
     * overrideable methode to fetch tooltip data as string called every time
     * the mouse entered the label area
     *
     * @return text to show in tooltip
     */
    public String fetchTooltipText() {
        return null;
    }
}
