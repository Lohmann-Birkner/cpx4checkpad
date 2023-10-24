/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.dialog;

import de.lb.cpx.client.core.util.clipboard.ClipboardEnabler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Window;

/**
 * Basic class for a titled Dialog, implements a simple way to create a
 * formulardialog Given class represents the object that should be created with
 * the formular OnSave methode should return a newly created Object of that type
 * Extends base Dialog class in javafx
 *
 * @author wilde
 */
public class TitledDialog extends Dialog<ButtonType> {

    protected final ObjectProperty<DialogSkin<ButtonType>> dialogSkinProperty = new SimpleObjectProperty<>();

    /**
     * construct a new TitledDialog with the default DialogSkin Window Motality
     * is set to NONE
     *
     * @param pTitle title to be set in the Dialog
     */
    public TitledDialog(String pTitle) {
        this(pTitle, null, Modality.NONE, null, true);
    }

    /**
     * construct a new TitledDialog with the default DialogSkin Window Motality
     * is set to NONE
     *
     * @param pTitle title to be set in the Dialog
     * @param pResizeable resizebale window
     */
    public TitledDialog(String pTitle, boolean pResizeable) {
        this(pTitle, null, Modality.NONE, null, pResizeable);
    }

    /**
     * construct a new TitledDialog with the default DialogSkin
     *
     * @param pOwner owner window, if set Dialog starts as WINDOW_MODAL
     * @param pTitle title to be set in the Dialog
     * @param pResizeable resize window
     */
    public TitledDialog(String pTitle, Window pOwner, boolean pResizeable) {
        this(pTitle, pOwner, Modality.WINDOW_MODAL, null, pResizeable);
    }

    /**
     * construct a new TitledDialog with the default DialogSkin
     *
     * @param pOwner owner window, if set Dialog starts as WINDOW_MODAL
     * @param pTitle title to be set in the Dialog
     */
    public TitledDialog(String pTitle, Window pOwner) {
        this(pTitle, pOwner, Modality.WINDOW_MODAL, null, true);
    }

    /**
     * construct a new TitledDialog with the default DialogSkin
     *
     * @param pOwner owner window
     * @param pModality modality to set in the window
     * @param pTitle title to be set in the Dialog
     * @param pResizeable resizeable window
     */
    public TitledDialog(String pTitle, Window pOwner, Modality pModality, boolean pResizeable) {
        this(pTitle, pOwner, pModality, null, pResizeable);
    }

    /**
     * construct a new TitledDialog with the default DialogSkin
     *
     * @param pOwner owner window
     * @param pModality modality to set in the window
     * @param pTitle title to be set in the Dialog
     */
    public TitledDialog(String pTitle, Window pOwner, Modality pModality) {
        this(pTitle, pOwner, pModality, null, true);
    }

    /**
     * construct a new TitledDialog with the default DialogSkin
     *
     * @param pOwner owner window
     * @param pModality modality to set in the window
     * @param pTitle title to be set in the Dialog
     * @param pSkin skin class of the Dialog, if null default skin will be set
     * @param pResizeable resizeable window
     */
    public TitledDialog(String pTitle, Window pOwner, Modality pModality, DialogSkin<ButtonType> pSkin, boolean pResizeable) {
        super();
        dialogSkinProperty.addListener(new ChangeListener<DialogSkin<ButtonType>>() {
            @Override
            public void changed(ObservableValue<? extends DialogSkin<ButtonType>> observable, DialogSkin<ButtonType> oldValue, DialogSkin<ButtonType> newValue) {
                newValue.setTitle(pTitle);
                newValue.addButtonTypes(ButtonType.OK, ButtonType.CANCEL);
            }
        });
        if (pOwner != null) {
            initOwner(pOwner);
        }
        initModality(pModality);
        setDialogSkin(pSkin != null ? pSkin : new DialogSkin<>(this, pResizeable));
        ClipboardEnabler.installClipboardToScene(this.getDialogPane().getScene());
//        this(pOwner,pTitle);
//        initModality(pModality);
    }

    /**
     * gets the skin (layout) of the dialog, that is extended from of a
     * SkinBase!
     *
     * @return layout class for the dialog
     */
    public DialogSkin<ButtonType> getDialogSkin() {
        return dialogSkinProperty.get();
    }

    /**
     * sets a new skin apply changes to it
     *
     * @param pSkin skin to set
     */
    public final void setDialogSkin(DialogSkin<ButtonType> pSkin) {
        dialogSkinProperty.set(pSkin);
    }

    /**
     * sets the modality in the dialog May not force the dialog to redraw it
     * self if its already showing
     *
     * @param pModality modality mode to set
     */
    public void setModality(Modality pModality) {
        initModality(pModality);
    }

    /**
     * @param pContent set Content in dialogpane
     */
    public void setContent(Node pContent) {
        getDialogPane().setContent(pContent);
    }

}
