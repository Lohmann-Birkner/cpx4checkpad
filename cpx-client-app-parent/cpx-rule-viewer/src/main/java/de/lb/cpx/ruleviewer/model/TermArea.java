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
package de.lb.cpx.ruleviewer.model;

import de.lb.cpx.rule.element.model.RulesElement;
import de.lb.cpx.rule.element.model.RulesValue;
import de.lb.cpx.ruleviewer.dragdrop.format.DndFormat;
import java.util.List;
import javafx.scene.input.DragEvent;

/**
 *
 * @author wilde
 */
public class TermArea extends DragEndArea {

    public TermArea() {
        super();
        getStyleClass().add("term-area");
        setPlaceholderText("Platzieren Sie hier einen Term!");
//        setDataFormat(TermEnum.getDataFormat());
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
                if (((List<Object>) content).get(0) instanceof RulesValue || ((List<Object>) content).get(0) instanceof RulesElement) {
                    return true;
                }
            } else {
                if (content instanceof RulesValue || content instanceof RulesElement) {
                    return true;
                }
            }
        }
        if (event.getDragboard().hasContent(DndFormat.REORDER_FORMAT)) {
            return event.getGestureSource() instanceof Term || event.getGestureSource() instanceof Element;
//            content = event.getDragboard().getContent(DndFormat.REORDER_FORMAT);
//            if(content instanceof ReOrderWrapper){
//                return ReOrderWrapper.ContentType.TERM.equals(((ReOrderWrapper)content).getType());
//            }
        }
        return false;
    }

}
