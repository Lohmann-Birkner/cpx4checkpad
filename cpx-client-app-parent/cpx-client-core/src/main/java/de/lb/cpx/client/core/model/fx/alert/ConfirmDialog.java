/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.alert;

import de.lb.cpx.client.core.BasicMainApp;
import de.lb.cpx.shared.lang.Lang;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

/**
 * Simple alert Dialog to handly confirmation
 *
 * @author wilde
 */
public class ConfirmDialog extends AlertDialog {

    /**
     * creates new alert dialog with given text as message default buttons are
     * no and yes
     *
     * @param pOwner owner window
     * @param pMsg message text to be shown to the user
     */
    public ConfirmDialog(Window pOwner, String pMsg) {
        this(pOwner, pMsg, "");
    }

    /**
     * creates new alert dialog with given text as message default buttons are
     * no and yes
     *
     * @param pOwner owner window
     * @param pMsg message text to be shown to the user
     * @param pButtonTypes buttons to be shown
     */
    public ConfirmDialog(Window pOwner, String pMsg, ButtonType... pButtonTypes) {
        this(pOwner, pMsg, "", pButtonTypes);
    }

    /**
     * creates new alert dialog with given text as message default buttons are
     * no and yes
     *
     * @param pOwner owner window
     * @param pMsg message text to be shown to the user
     * @param pDetails Stack Trace or other details
     * @param pButtonTypes buttons to be shown
     */
    public ConfirmDialog(Window pOwner, String pMsg, String pDetails, ButtonType... pButtonTypes) {
        super(AlertType.CONFIRMATION, Lang.getConformation(), pMsg, pDetails, pButtonTypes);
        initOwner(pOwner);
        //AWi-20170615-Bug: add default height and width to avoid issue that some alerts are not shown properly in the ui
        setHeight(50);
        setWidth(100);
    }

    /**
     * creates new alert dialog with given text as message default buttons are
     * no and yes
     *
     * @param pOwner owner window
     * @param pMsg message text to be shown to the user
     * @param pDetails Stack Trace or other details
     */
    public ConfirmDialog(Window pOwner, String pMsg, String pDetails) {
        this(pOwner, pMsg, pDetails, ButtonType.NO, ButtonType.YES);
    }

    /**
     * creates new alert dialog with default text uses mainapp window as owner
     */
    public ConfirmDialog() {
        this(BasicMainApp.getWindow(), Lang.getConfirmationDefaultText());
    }
}
