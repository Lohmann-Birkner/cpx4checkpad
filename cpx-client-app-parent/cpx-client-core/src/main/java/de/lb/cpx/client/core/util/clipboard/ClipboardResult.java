/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.util.clipboard;

import javafx.scene.Node;
import javafx.scene.image.Image;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author niemeier
 */
public class ClipboardResult {

    public final String value;
    public final Image image;
    public final Node node;
    public final boolean isHtml;

    public ClipboardResult(final Node pNode, final String pValue) {
        this(pNode, pValue, false);
    }

    public ClipboardResult(final Node pNode, final String pValue, final boolean pIsHtml) {
        value = pValue == null ? "" : (pValue.trim().isEmpty() ? "" : StringUtils.stripEnd(pValue, null));
        image = null;
        node = pNode;
        isHtml = pIsHtml;
    }

    public ClipboardResult(final Node pNode, final Image pImage) {
        value = "";
        image = pImage;
        node = pNode;
        isHtml = false;
    }

    public Image getImage() {
        return image;
    }

    public String getValue() {
        return value;
    }

    public Node getNode() {
        return node;
    }

    public boolean isSet() {
        return image != null || (value != null && !value.isEmpty());
    }

    public boolean isHtml() {
        return isHtml;
    }

//    public static String ltrim(String s) {
//        int i = 0;
//        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
//            i++;
//        }
//        return s.substring(i);
//    }
//    private static String rtrim(String s) {
//        int i = s.length() - 1;
//        while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
//            i--;
//        }
//        return s.substring(0, i + 1);
//    }
}
