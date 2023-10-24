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
package de.lb.cpx.ruleviewer.model.treeview;

import de.lb.cpx.ruleviewer.model.Element;
import de.lb.cpx.ruleviewer.model.SelectableControl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ContextMenu;

/**
 *
 * @author wilde
 */
public final class StructureItem {

    private SelectableControl control = null;
    private boolean nestable = true;

    public StructureItem(SelectableControl pControl) {
        if (pControl != null) {
            control = pControl;
            setDisplayText(pControl.getDisplayText());
//            setContextMenu(pControl.getContextMenu());
            if (control instanceof Element) {
                setNestable(true);
            } else {
                setNestable(false);
            }
        }
    }

    public StructureItem(String pDisplayText, ContextMenu pContextMenu) {
        setDisplayText(pDisplayText);
        setContextMenu(pContextMenu);
    }

    public SelectableControl getControl() {
        return control;
    }
//    public String getDisplayText(){
//        return control.getDisplayText();
//    }

    public boolean isNestable() {
        return nestable;
    }

    public void setNestable(boolean pNestable) {
        nestable = pNestable;
    }
    private StringProperty displayTextProperty;

    public StringProperty displayTextProperty() {
        if (displayTextProperty == null) {
            displayTextProperty = new SimpleStringProperty();
        }
        return displayTextProperty;
    }

    public String getDisplayText() {
        return displayTextProperty().get();
    }

    public void setDisplayText(String pDisplayText) {
        displayTextProperty().set(pDisplayText);
    }

    private ObjectProperty<ContextMenu> contextMenuProperty;

    public ObjectProperty<ContextMenu> contextMenuProperty() {
        if (contextMenuProperty == null) {
            contextMenuProperty = new SimpleObjectProperty<>(new ContextMenu());
        }
        return contextMenuProperty;
    }

    public ContextMenu getContextMenu() {
        return contextMenuProperty().get();
    }

    public void setContextMenu(ContextMenu pMenu) {
        contextMenuProperty().set(pMenu);
    }
}
