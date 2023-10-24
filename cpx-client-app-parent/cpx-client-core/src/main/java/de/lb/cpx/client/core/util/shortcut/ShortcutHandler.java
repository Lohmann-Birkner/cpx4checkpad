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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util.shortcut;

import javafx.scene.input.KeyEvent;

/**
 *
 * @author niemeier
 */
public interface ShortcutHandler {

    /**
     * F1 (Help)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutF1Help(KeyEvent pEvent);

    /**
     * F2 (New)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutF2New(KeyEvent pEvent);

    /**
     * F3 (Find)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutF3Find(KeyEvent pEvent);

    /**
     * F4 (Close)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutF4Close(KeyEvent pEvent);

    /**
     * F5 (Shortcut)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutF5Refresh(KeyEvent pEvent);

    /**
     * ENTER (Execute)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutEnterExecute(KeyEvent pEvent);

    /**
     * CTRL+P (Print)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutControlPPrint(KeyEvent pEvent);

    /**
     * CTRL+F (Find)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutControlFFind(KeyEvent pEvent);

    /**
     * CTRL+W (Close)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutControlWClose(KeyEvent pEvent);

    /**
     * CTRL+N (New)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutControlNNew(KeyEvent pEvent);

    /**
     * CTRL+R (Refresh)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutControlRRefresh(KeyEvent pEvent);

    /**
     * CTRL+S (Save)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutControlSSave(KeyEvent pEvent);

    /**
     * ALT+Left cursor (Back)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutAltLeftBack(KeyEvent pEvent);

    /**
     * ALT+Right cursor (Forward)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutAltRightForward(KeyEvent pEvent);

    /**
     * CTRL+C (Copy)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutControlCCopy(KeyEvent pEvent);

    /**
     * CTRL+V (Paste)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutControlVPaste(KeyEvent pEvent);

    /**
     * DEL (Remove)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutDelRemove(KeyEvent pEvent);

    /**
     * SHIFT+DEL (Remove)
     *
     * @param pEvent event
     * @return was consumed?
     */
    boolean shortcutShiftDelRemove(KeyEvent pEvent);

//    /**
//     * Unhandled single key
//     * @param pEvent event
//     * @return was consumed?
//     */
//    public boolean shortcutUnhandled(KeyEvent pEvent);
//    
//    /**
//     * Unhandled key combination
//     * @param pEvent event
//     * @return was consumed?
//     */
//    public boolean shortcutUnhandledCombo(KeyEvent pEvent);
}
