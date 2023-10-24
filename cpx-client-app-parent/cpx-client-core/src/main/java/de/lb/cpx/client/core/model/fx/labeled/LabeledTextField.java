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

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Labeled TextField class handles a textfield and an corresponting label
 *
 * @author wilde
 */
public class LabeledTextField extends LabeledTextControl<TextField> {

    /**
     * default no arg constructor for scene builder
     */
    public LabeledTextField() {
        super("LabeledTextField", new TextField());
    }

    /**
     * creates a new textField with that label
     *
     * @param pLabel label text
     */
    public LabeledTextField(String pLabel) {
        this(pLabel, 0);
    }

    /**
     * creates a new textField with that label
     *
     * @param pLabel textArea
     * @param maxSize maximum size (maximum amount of characters)
     */
    public LabeledTextField(String pLabel, int maxSize) {
        super(pLabel, new TextField(), maxSize); // by default don't show character counts
    }

    /**
     * creates new LabeledTextField with label and control
     *
     * @param pLabel label text
     * @param pTextField control
     */
    public LabeledTextField(String pLabel, TextField pTextField) {
        super(pLabel, pTextField);
        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    pTextField.requestFocus();
                }
            }
        });
    }

    /**
     * creates new LabeledTextField with label and control
     *
     * @param pLabel label text
     * @param pTextField control
     * @param maxSize maximum size (maximum amount of characters)
     */
    public LabeledTextField(String pLabel, TextField pTextField, int maxSize) {
        super(pLabel, pTextField, maxSize);
        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    pTextField.requestFocus();
                }
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LabeledTextFieldSkin(getTitle(), this);
    }

    public void setPromptText(String pText) {
        getControl().setPromptText(pText);
    }

    @Override
    public CharsDisplayMode getDefaultCharsDisplayMode() {
        return CharsDisplayMode.NONE;
    }

    public void clear() {
        getControl().clear();
    }
    private ObjectProperty<Button> additionalButtonProperty;// = new ReadOnlyObjectWrapper<>();

    public ObjectProperty<Button> additionalButtonProperty() {
        if (additionalButtonProperty == null) {
            additionalButtonProperty = new SimpleObjectProperty<>();
        }
        return additionalButtonProperty;
    }

    public void setAdditionalButton(Button pButton) {
        pButton.getStyleClass().add("search-button");
        pButton.setFocusTraversable(false);
        pButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (MouseButton.PRIMARY.equals(t.getButton()) && !isFocused()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            requestFocus();
                        }
                    });
                }
            }
        });
        additionalButtonProperty.set(pButton);
    }

    public Button getAdditionalButton() {
        return additionalButtonProperty().get();
    }

    private BooleanProperty showCaretProperty;

    public final BooleanProperty showCaretProperty() {
        if (showCaretProperty == null) {
            showCaretProperty = new SimpleBooleanProperty(true);
        }
        return showCaretProperty;
    }

    public final void setShowCaret(boolean pShow) {
        showCaretProperty.set(pShow);
    }

    public final boolean isShowCaret() {
        return showCaretProperty.get();
    }
}
