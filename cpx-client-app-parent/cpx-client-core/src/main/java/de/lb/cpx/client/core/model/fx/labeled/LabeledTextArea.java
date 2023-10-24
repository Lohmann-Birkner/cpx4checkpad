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
 *    2018  nandola - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.labeled;

import javafx.scene.control.TextArea;

/**
 *
 * @author nandola
 */
public class LabeledTextArea extends LabeledTextControl<TextArea> {
    public static final int RISK_COMMENT_SIZE = 500;
    public static final int CASE_VERSION_SIZE = 750;
    public static final int REQUEST_COMMENT_SIZE = 255;

//    private IntegerProperty maxLength = new SimpleIntegerProperty(this, "maxLength", -1);
    /**
     * no arg contructor for scenebuilder with default title label
     */
    public LabeledTextArea() {
        this("LabeledTextArea", 255);    //default label and allowed no. of characters
    }

    /**
     * creates a new labeled Textarea with given Title
     *
     * @param pLabel textArea
     */
    public LabeledTextArea(String pLabel) {
        this(pLabel, 0);
    }

    /**
     * creates a new labeled Textarea with given Title
     *
     * @param pLabel textArea
     * @param maxSize maximum size (maximum amount of characters)
     */
    public LabeledTextArea(String pLabel, int maxSize) {
        super(pLabel, new TextArea(), maxSize);
        setWrapText(true);
    }

    /**
     * set text wrap value
     *
     * @param pWrapText value if text should be wrapped
     */
    public final void setWrapText(boolean pWrapText) {
        getControl().setWrapText(pWrapText);
    }

    /**
     * indicator if text is wrapping
     *
     * @return value if text wrap or not
     */
    public boolean isWrapText() {
        return getControl().isWrapText();
    }

    @Override
    public CharsDisplayMode getDefaultCharsDisplayMode() {
        return CharsDisplayMode.ALWAYS;
    }

//    private ReadOnlyIntegerProperty lengthProperty() {
//        return maxLength;
//    }
}
