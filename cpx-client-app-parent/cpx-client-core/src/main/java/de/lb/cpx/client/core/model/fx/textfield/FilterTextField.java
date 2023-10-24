/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.textfield;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class FilterTextField extends TextField {

    private static final Logger LOG = Logger.getLogger(FilterTextField.class.getName());
    
    private StringProperty filterValueProperty;
    private Callback<String, String> validateCallback;

    public FilterTextField() {
        super();
        addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    validateValue(getText());
                }
            }
        });
        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) {
                    return;
                }
                if (oldValue == null) {
                    return;
                }
                if (newValue.isEmpty() && !oldValue.isEmpty()) {
                    validateValue(newValue);
                }
            }
        });
        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    LOG.info("textfield focus change from " + oldValue + " to " + newValue);
                if (!newValue) {
                    if (getText() == null) {
                        return;
                    }
                    if (getText().isEmpty()) {
                        return;
                    }
                    if (getText().equals(getFilterValue())) {
                        return;
                    }
                    validateValue();
                    //setFilterValue(getText());
                }
            }
        });
    }

    /**
     * Paste text from something like Excel/CSV and replace line breaks and
     * tabulators with comma to make it more easier to search for multiple
     * values.
     */
    @Override
    public void paste() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            final String text = transformCsvText(clipboard.getString());
            if (text != null) {
                setCreateNewUndoRecord(true);
                try {
                    replaceSelection(text);
                } finally {
                    setCreateNewUndoRecord(false);
                }
            }
        }
    }

    protected void setCreateNewUndoRecord(final boolean pValue) {
        final String fieldName = "createNewUndoRecord";
        try {
            Field booleanField = TextInputControl.class.getDeclaredField(fieldName);
            booleanField.setAccessible(true);
            booleanField.set(this, pValue);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, "Was not able to set boolean field '" + fieldName + "' through reflection API", ex);
        }
    }

    protected String transformCsvText(final String pText) {
        if (pText == null) {
            return pText;
        }
        final String[] formattingStrings = new String[]{"\t", "\r\n", "\r", "\n"}; //mind ordering here!
        String text = pText;
        for (String fs : formattingStrings) {
            text = text.replace(fs, ",");
        }
//            if (text.equalsIgnoreCase(pText)) {
//                //no line breaks or tabs found -> paste as it is
//                return pText;
//            }
        while (true) {
            String c = text;
            //remove blanks and double commas
            c = c.replace(" ,", ",");
            c = c.replace(", ", ",");
            c = c.replace(",,", ",");
            if (c.length() > 0) {
                //remove last standalone comma
                if (c.substring(c.length() - 1).equals(",")) {
                    c = c.substring(0, c.length() - 1);
                }
            }
            if (c.length() > 0) {
                //remove first standalone comma
                if (c.substring(0).equals(",")) {
                    c = c.substring(1);
                }
            }
            c = c.trim();
            if (c.equalsIgnoreCase(text)) {
                break;
            }
            text = c;
        }
        return text.trim();
    }

    public final void validateValue() {
        validateValue(getText());
    }

    protected final void validateValue(String pNewValue) {
        if (validateCallback != null) {
            String value = validateCallback.call(pNewValue);
            if (value == null || value.isEmpty()) {
                setText("");
                setFilterValue("");
            } else {
                setFilterValue(value);
            }
        } else {
            setFilterValue(getText());
        }

    }

    //get filterValue property to react to focus change and execute filter event
    public StringProperty filterValueProperty() {
        if (filterValueProperty == null) {
            filterValueProperty = new SimpleStringProperty(getText());
        }
        return filterValueProperty;
    }

    public String getFilterValue() {
        return filterValueProperty().get();
    }

    public void setFilterValue(String pValue) {
        filterValueProperty().set(pValue);
    }

    public void setValidateCallback(Callback<String, String> pCallback) {
        validateCallback = pCallback;
    }
}
