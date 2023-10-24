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
package de.lb.cpx.client.core.model.fx.labeled;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;

/**
 * Implements a Labeled Label controll
 *
 * @author wilde
 */
public final class LabeledLabel extends LabeledControl<Label> {

    /**
     * consturcts new Labeled Label with the given label text
     *
     * @param pLabel label text
     */
    public LabeledLabel(String pLabel) {
        super(pLabel, new Label());
        getControl().wrapTextProperty().bind(wrapTextProperty());
        getControl().prefWidthProperty().bind(prefWidthProperty());
        getControl().minHeightProperty().bind(contentPrefHeightProperty());
//        getControl().setWrapText(true);
    }

    /**
     * construct new default labeled label with label as title and text as text
     */
    public LabeledLabel() {
        super("label", new Label());
        setText("text");
        getControl().wrapTextProperty().bind(wrapTextProperty());
        getControl().prefWidthProperty().bind(prefWidthProperty());
        getControl().minHeightProperty().bind(contentPrefHeightProperty());
    }

    /**
     * sets new text
     *
     * @param pText text to show
     */
    public void setText(String pText) {
        getControl().setText(pText);
    }

    /**
     * @return text property of the text
     */
    public StringProperty getTextProperty() {
        return getControl().textProperty();
    }

    /**
     * @return get the current text
     */
    public String getText() {
        return getControl().getText();
    }

    private BooleanProperty wrapTextProperty;

    public BooleanProperty wrapTextProperty() {
        if (wrapTextProperty == null) {
            wrapTextProperty = new SimpleBooleanProperty(true);
        }
        return wrapTextProperty;
    }

    public void setWrapText(boolean pWrap) {
        wrapTextProperty().set(pWrap);
    }

    public boolean getWrapText() {
        return wrapTextProperty().get();
    }
}
