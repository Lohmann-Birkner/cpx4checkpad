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
package de.lb.cpx.ruleviewer.model;

import de.lb.cpx.rule.element.model.Sugg;
import de.lb.cpx.ruleviewer.dragdrop.format.DndFormat;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.input.DragEvent;

/**
 * Endarea for drops in Suggestions, not really needed but to have same look and
 * feel as rule
 *
 * @author wilde
 */
public class SuggestionArea extends DragEndArea {

    public SuggestionArea() {
        super();
        getStyleClass().add("suggestion-area");
        setPlaceholderText("Platzieren Sie hier einen Vorschlag!");
        setPadding(new Insets(5, 0, 0, 0));
    }

    @Override
    public boolean checkDragEvent(DragEvent event) {
        Object content = null;
        if (event.getDragboard().hasContent(DndFormat.ADD_FORMAT)) {
            content = event.getDragboard().getContent(DndFormat.ADD_FORMAT);
            if (content == null) {
                return false;
            }
            if (content instanceof List) {
                if (((List<Object>) content).get(0) instanceof Sugg) {
                    return true;
                }
            } else {
                if (content instanceof Sugg) {
                    return true;
                }
            }
        }
        if (event.getDragboard().hasContent(DndFormat.REORDER_FORMAT)) {
            return event.getGestureSource() instanceof Suggestion;
        }
        return false;
    }

}
