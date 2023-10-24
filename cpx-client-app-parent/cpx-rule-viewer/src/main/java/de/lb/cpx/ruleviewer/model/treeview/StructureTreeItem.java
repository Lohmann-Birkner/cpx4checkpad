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

import de.lb.cpx.ruleviewer.model.SelectableControl;
import javafx.scene.control.TreeItem;

/**
 *
 * @author wilde
 */
public class StructureTreeItem extends TreeItem<StructureItem> {

    public StructureTreeItem(SelectableControl pControl) {
        super(new StructureItem(pControl));
    }

    public SelectableControl getControl() {
        if (getValue() == null) {
            return null;
        }
        return getValue().getControl();
    }
}
