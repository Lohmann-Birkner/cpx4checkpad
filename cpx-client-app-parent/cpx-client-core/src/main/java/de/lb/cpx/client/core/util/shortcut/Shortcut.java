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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util.shortcut;

import de.lb.cpx.client.core.easycoder.EasyCoderDialogFXMLController;
import de.lb.cpx.client.core.model.Controller;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.client.core.model.fx.datepicker.FormatedDatePicker;
import de.lb.cpx.client.core.util.code.TextFlowWithCopyText;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author adameck
 */
public class Shortcut {

    private static final Logger LOG = Logger.getLogger(Shortcut.class.getName());

    private final KeyCombination combPrint = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);
    private final KeyCombination combSearch = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
    private final KeyCombination combClose = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);
    private final KeyCombination combNew = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
    private final KeyCombination combRefresh = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
    private final KeyCombination combSave = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    private final KeyCombination combBack = new KeyCodeCombination(KeyCode.LEFT, KeyCombination.ALT_DOWN);
    private final KeyCombination combForward = new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.ALT_DOWN);
    private final KeyCombination combCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
    private final KeyCombination combDelete = new KeyCodeCombination(KeyCode.DELETE, KeyCombination.SHIFT_DOWN);

    public void setShortcut(final Scene pScene) {
//        if (pController == null) {
//            LOG.log(Level.SEVERE, "controller cannot be null to install shortcuts");
//            return;
//        }
//        Scene scene = pController.getScene();
        if (pScene == null) {
            LOG.log(Level.SEVERE, "controller has no scene so I cannot install shortcuts");
            return;
        }
        pScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
//                Object source = event.getSource();
                //EventTarget target = event.getTarget();
                EventTarget target = event.getTarget();
                if (!(target instanceof Node)) {
                    return;
                }
                Node n = (Node) target;

                if (n instanceof TextInputControl) {
                    return;
                }
                if (n instanceof FormatedDatePicker) {
                    if (KeyCode.ENTER.equals(event.getCode())) {
                        final FormatedDatePicker formatedDp = (FormatedDatePicker) n;
                        FormatedDatePicker.applyEditorText(formatedDp);
                        //event.consume();
                    }
                    return;
                }
                if (n instanceof DatePicker) {
                    if (KeyCode.ENTER.equals(event.getCode())) {
                        final DatePicker dp = (DatePicker) n;
                        FormatedDatePicker.applyEditorText(dp);
                    }
                    return;
                }

                List<ShortcutHandler> testedHandlers = new ArrayList<>();
                while (true) {
                    ShortcutHandler tmp[] = new ShortcutHandler[2];
                    for (int i = 1; i <= 2; i++) {
                        final ShortcutHandler controller;
                        if (i == 1) {
                            controller = getShortcutHandlerFromRedirector(n);
                            tmp[0] = controller;
                        } else {
                            controller = getShortcutHandler(n);
                            tmp[1] = controller;
                            if (tmp[0] == null && tmp[1] == null) {
                                return;
                            }
                        }
                        if (controller == null) {
                            continue;
                        }
                        if (testedHandlers.contains(controller)) {
                            continue;
                        } else {
                            testedHandlers.add(controller);
                        }
                        if (combPrint.match(event)) {
                            LOG.log(Level.INFO, "Ctrl + P (Print) pressed");
                            if (controller.shortcutControlPPrint(event)) {
                                event.consume();
                                return;
                            }
                        } else if (combSearch.match(event)) {
                            LOG.log(Level.INFO, "Ctrl + F (Search) pressed");
                            if (controller.shortcutControlFFind(event)) {
                                event.consume();
                                return;
                            }
                        } else if (combClose.match(event)) {
                            LOG.log(Level.INFO, "Ctrl + W (Close) pressed");
                            if (controller.shortcutControlWClose(event)) {
                                event.consume();
                                return;
                            }
                        } else if (combNew.match(event)) {
                            LOG.log(Level.INFO, "Ctrl + N (New) pressed");
                            if (controller.shortcutControlNNew(event)) {
                                event.consume();
                                return;
                            }
                        } else if (combRefresh.match(event)) {
                            LOG.log(Level.INFO, "Ctrl + R (Refresh) pressed");
                            if (controller.shortcutControlRRefresh(event)) {
                                event.consume();
                                return;
                            }
                        } else if (combSave.match(event)) {
                            LOG.log(Level.INFO, "Ctrl + S (Save) pressed");
                            if (controller.shortcutControlSSave(event)) {
                                event.consume();
                                return;
                            }
                        } else if (combBack.match(event)) {
                            LOG.log(Level.INFO, "Alt + Left (Back) pressed");
                            if (controller.shortcutAltLeftBack(event)) {
                                event.consume();
                                return;
                            }
                        } else if (combForward.match(event)) {
                            LOG.log(Level.INFO, "Alt + Right (Forward) pressed");
                            if (controller.shortcutAltRightForward(event)) {
                                event.consume();
                                return;
                            }
                        } else if (combCopy.match(event)) {
                            LOG.log(Level.INFO, "Ctrl + C (Copy) pressed");
                            if (controller.shortcutControlCCopy(event)) {
                                event.consume();
                                return;
                            }
                        } else if (combDelete.match(event)) {
                            LOG.log(Level.INFO, "Shift + Delete (Copy) pressed");
                            if (controller.shortcutShiftDelRemove(event)) {
                                event.consume();
                                return;
                            }
                        }

                        switch (event.getCode()) {
                            case F1:
                                LOG.log(Level.INFO, "F1 (Help) pressed");
                                if (controller.shortcutF1Help(event)) {
                                    event.consume();
                                    return;
                                }
                                break;
                            case F2:
                                LOG.log(Level.INFO, "F2 (Create new or rename/edit) pressed");
                                if (controller.shortcutF2New(event)) {
                                    event.consume();
                                    return;
                                }
                                break;
                            case F3:
                                LOG.log(Level.INFO, "F3 (Find) pressed");
                                if (controller.shortcutF3Find(event)) {
                                    event.consume();
                                    return;
                                }
                                break;
                            case F4:
                                LOG.log(Level.INFO, "F4 (Close) pressed");
                                if (controller.shortcutF4Close(event)) {
                                    event.consume();
                                    return;
                                }
                                break;
                            case F5:
                                LOG.log(Level.INFO, "F5 (Refresh) pressed");
                                if (controller.shortcutF5Refresh(event)) {
                                    event.consume();
                                    return;
                                }
                                break;
                            case ENTER:
                                LOG.log(Level.INFO, "ENTER (Open or execute) pressed");
                                if (controller.shortcutEnterExecute(event)) {
                                    event.consume();
                                    return;
                                }
                                break;
                            case DELETE:
                                LOG.log(Level.INFO, "Delete (Remove) pressed");
                                if (controller.shortcutDelRemove(event)) {
                                    event.consume();
                                    return;
                                }
                                break;
                            default:
                                LOG.log(Level.FINEST, "Unknown key code: " + event.getCode());
                        }
                    }
                    n = n.getParent();
                }
            }
        });
    }

//    private ShortcutHandler getShortcutHandler(final KeyEvent event) {
//        EventTarget target = event.getTarget();
//        if (target instanceof Node) {
//            Node n = (Node) target;
//            return getShortcutHandler(n);
//        }
//        return null;
//    }
    private ShortcutHandler getShortcutHandlerFromRedirector(final Node pNode) {
        Node n = pNode;
        while (n != null) {
            if (n instanceof ShortcutsRedirector) {
                return ((ShortcutsRedirector) n).getShortcuts();
            }
            if(n instanceof ScrollPane){
                Node nd = ((ScrollPane) n).getContent();
            
                if(nd instanceof TextFlowWithCopyText) {
                     Controller<CpxScene> sc = ((TextFlowWithCopyText) nd).getRedirector();
                     if(sc != null && sc  instanceof ShortcutHandler){
                          return ((ShortcutsRedirector)sc.getScene()).getShortcuts();
                     }
                }
            
            }
            if (n.getScene() instanceof ShortcutHandler) {
                return ((ShortcutsRedirector) n.getScene()).getShortcuts();
            }
            if (n.getScene() instanceof CpxScene) {
                CpxScene sc = (CpxScene) n.getScene();
                if (sc.getController() instanceof ShortcutsRedirector) {
                    return ((ShortcutsRedirector) sc.getController()).getShortcuts();
                }
            }

            n = n.getParent();
        }
        return null;
    }

    private ShortcutHandler getShortcutHandler(final Node pNode) {
        Node n = pNode;
        while (n != null) {
            if (n instanceof ShortcutHandler) {
                return (ShortcutHandler) n;
            }
            if (n.getScene() instanceof ShortcutHandler) {
                return (ShortcutHandler) n.getScene();
            }
            if (n.getScene() instanceof CpxScene) {
                CpxScene sc = (CpxScene) n.getScene();
                if (sc.getController() instanceof ShortcutHandler) {
                    return sc.getController();
                }
            }

            n = n.getParent();
        }
        return null;
    }

//    private void showOrHideMenu() {
////        if (Menu isOpen) {
////            close
////        } else {
////            open
////        }
//    }
}
