/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util;

import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.control.behavior.TextInputControlBehavior;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.skin.TextAreaSkin;
import javafx.scene.control.skin.TextFieldSkin;

/**
 * Helper class to access node objects
 *
 * @author wilde
 */
public class ContextMenuUtil {

    private static final Logger LOG = Logger.getLogger(ContextMenuUtil.class.getName());

    private ContextMenuUtil() {
    }

    public static void install(final TextArea pTextArea,
            final List<MenuItem> pMenuItems) {
        if (pTextArea == null) {
            throw new IllegalArgumentException("node cannot be null!");
        }
        TextAreaSkin skin = (TextAreaSkin) pTextArea.getSkin();
        if (skin == null) {
            LOG.log(Level.WARNING, "skin is null, will create new TextAreaSkin!");
            skin = new TextAreaSkin(pTextArea);
            pTextArea.setSkin(skin);
        }
        install(skin, pMenuItems);
    }

    public static void install(final TextAreaSkin pTextAreaSkin,
            final List<MenuItem> pMenuItems) {
        if (pTextAreaSkin == null) {
            throw new IllegalArgumentException("node cannot be null!");
        }
        final TextAreaBehavior behavior = getTextAreaBehavior(pTextAreaSkin);
        install(behavior, pMenuItems);
    }

    public static void install(final TextFieldSkin pFieldSkin,
            final List<MenuItem> pMenuItems) {
        if (pFieldSkin == null) {
            throw new IllegalArgumentException("node cannot be null!");
        }
        final TextFieldBehavior behavior = getTextFieldBehavior(pFieldSkin);
        install(behavior, pMenuItems);
    }

    @SuppressWarnings("unchecked")
    private static ObservableList<MenuItem> getItemList(final ContextMenu pContextMenu) {
        try {
            Field field = ContextMenu.class.getDeclaredField("items");
            field.setAccessible(true);
            return (ObservableList<MenuItem>) field.get(pContextMenu);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static TextAreaBehavior getTextAreaBehavior(final TextAreaSkin pTextAreaSkin) {
        try {
            Field field = TextAreaSkin.class.getDeclaredField("behavior");
            field.setAccessible(true);
            return (TextAreaBehavior) field.get(pTextAreaSkin);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static TextFieldBehavior getTextFieldBehavior(final TextFieldSkin pTextFieldSkin) {
        try {
            Field field = TextFieldSkin.class.getDeclaredField("behavior");
            field.setAccessible(true);
            return (TextFieldBehavior) field.get(pTextFieldSkin);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void install(final TextInputControlBehavior<?> pBehavior,
            final List<MenuItem> pMenuItems) {

        if (pBehavior == null) {
            throw new IllegalArgumentException("node cannot be null!");
        }

        final ContextMenu contextMenu = getContextMenu(pBehavior);
        if (contextMenu != null) {
            contextMenu.setOnShowing((t) -> {
                final ObservableList<MenuItem> items = getItemList(contextMenu);
                items.addAll(pMenuItems);
            });
        } else {
            LOG.log(Level.WARNING, "context menu is null, cannot add menu items");
        }
    }

    private static ContextMenu getContextMenu(final TextInputControlBehavior<?> pBehavior) {
        try {
            Field field = TextInputControlBehavior.class.getDeclaredField("contextMenu");
            field.setAccessible(true);
            return (ContextMenu) field.get(pBehavior);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
