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
package de.lb.cpx.client.core.model.fx.contextmenu;

import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import javafx.scene.control.ContextMenu;

/**
 * Context Menu to use to change ref types of an icd TODO: Refactor class to
 * work outside of tableview as owner
 *
 * @author wilde
 * @param <T> control type
 */
public class CtrlContextMenu<T> extends ContextMenu {

    private final T owner;

    public CtrlContextMenu(T pOwner) {
        super();
        ClipboardEnabler.installClipboardToScene(this.getScene());
        owner = pOwner;
    }

    public CtrlContextMenu() {
        this(null);
    }

    public T getOwner() {
        return owner;
    }

}
