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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.datepicker;

import de.lb.cpx.shared.lang.AbstractLang;
import de.lb.cpx.shared.lang.Lang;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.StringConverter;

/**
 * Formated Datepicker who formats themself with autocomplition to choosen
 * format in lang class
 *
 * @author wilde
 */
public class FormatedDatePicker extends DatePicker {

    private static final Logger LOG = Logger.getLogger(FormatedDatePicker.class.getName());
    private String lastValue;
    public FormatedDatePicker(LocalDate pDate) {
        super(pDate);
        TextField editor = getEditor();
//        editor.addEventFilter(KeyEvent.KEY_TYPED, (KeyEvent event) -> {
//            if (KeyCode.ENTER.equals(event.getCode())) {
//                event.consume();
//                applyEditorText();
//            }
//        });
        editor.addEventHandler(KeyEvent.KEY_TYPED, (KeyEvent event) -> {
            String devider = Lang.getDateFormatDevider();
            if (!("0123456789" + devider).contains(event.getCharacter())) {
                event.consume();
                return;
            }
//            if (KeyCode.ENTER.equals(event.getCode())) {
//                event.consume();
//                applyEditorText();
//            }
            if (devider.equals(event.getCharacter()) && (editor.getText().isEmpty() || editor.getText().charAt(editor.getCaretPosition() - 1) == devider.charAt(0))) {
                //If the users types slash again after it has been added, cancels it.
                event.consume();
            }
            String text = editor.getText();
            if (text != null && text.length() == Lang.getDateFormat().length()) {
                event.consume();
            }
            //CPX-2401: comment out to avoid weird behavior when editing existing date value via editor
//            editor.selectForward();
//            if (!event.getCharacter().equals(devider) && editor.getSelectedText().equals(devider)) {
//                editor.cut();
//                editor.selectForward();
//            }
//            Clipboard.getSystemClipboard().getString();
//            editor.cut();

            Platform.runLater(() -> {
                editor.getText();
                String textUntilHere = editor.getText(0, editor.getCaretPosition());
                String[] pattern = Lang.getDateFormat().split("\\" + devider);
                if (textUntilHere.matches(getPattern(pattern[0])) || textUntilHere.matches(getPattern(pattern[0]) + devider + getPattern(pattern[1]))) {
                    String textAfterHere = "";
                    int start = editor.getCaretPosition() + 1;
                    int end = editor.getText().length();
                    if (start <= end && start >= 0) {
                        textAfterHere = editor.getText(editor.getCaretPosition() + 1, editor.getText().length());
                    }
                    int caretPosition = editor.getCaretPosition();
                    editor.setText(textUntilHere + devider + textAfterHere);
                    editor.positionCaret(caretPosition + 1);
                }
            });
        });
        editor.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP:
                    event.consume();
                    addDays(1);
                    break;
                case DOWN:
                    event.consume();
                    addDays(-1);
                    break;
                case PAGE_UP:
                    event.consume();
                    addMonths(1);
                    break;
                case PAGE_DOWN:
                    event.consume();
                    addMonths(-1);
                    break;
//                case BEGIN:
//                    break;
//                case END:
//                    break;
                default:
                    break;
            }
        });

        editor.setOnScroll((ScrollEvent event) -> {
            event.consume();
            if (event.getDeltaY() > 0) {
                if (event.isControlDown()) {
                    addMonths(1);
                } else {
                    addDays(1);
                }
            } else if (event.getDeltaY() < 0) {
                if (event.isControlDown()) {
                    addMonths(-1);
                } else {
                    addDays(-1);
                }
            }
        });
        editor.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    applyEditorText(FormatedDatePicker.this);
                }
            }
        });
        setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate t) {
                return Lang.toDate(t);
            }

            @Override
            public LocalDate fromString(String string) {
                return Lang.toLocalDate(string);
            }
        });
    }

    public static void applyEditorText(final DatePicker datePicker) {
        TextField editor = datePicker.getEditor();
        final String text = editor.getText();
        LocalDate dt = null;
        try {
            dt = AbstractLang.toLocalDate(text);
        } catch (DateTimeParseException ex) {
            LOG.log(Level.SEVERE, "This is not a valid date: " + text);
            LOG.log(Level.FINEST, null, ex);
        }
        final LocalDate date = dt;
        LOG.log(Level.FINEST, "User typed text '" + text + "'. I interpret this as date '" + (date == null ? "null" : date) + "'");
        Platform.runLater(() -> {
            datePicker.setValue(date);
            if (date == null && (text != null && !text.trim().isEmpty())) {
                editor.setText(null);
            }
        });
    }

    public void addDays(final int pDays) {
        LocalDate dt = getValue();
        if (dt == null) {
            setValue(LocalDate.now());
            return;
        }
        setValue(dt.plusDays(pDays));
    }

    public void addWeeks(final int pWeeks) {
        LocalDate dt = getValue();
        if (dt == null) {
            setValue(LocalDate.now());
            return;
        }
        setValue(dt.plusWeeks(pWeeks));
    }

    public void addMonths(final int pMonths) {
        LocalDate dt = getValue();
        if (dt == null) {
            setValue(LocalDate.now());
            return;
        }
        setValue(dt.plusMonths(pMonths));
    }

    public void addYears(final int pYears) {
        LocalDate dt = getValue();
        if (dt == null) {
            setValue(LocalDate.now());
            return;
        }
        setValue(dt.plusYears(pYears));
    }

    public FormatedDatePicker() {
        this(null);
//        getStyleClass().add(0,"filter-date-picker");
    }

//    @Override
//    public void show() {
//        super.show(); //To change body of generated methods, choose Tools | Templates.
//    }
//
    @Override
    protected Skin<?> createDefaultSkin() {
        return new DatePickerSkin(this) {
            @Override
            public void show() {
                //foce stylesheeet into popover to avoid faulty layout after reload
                getStylesheets().add(0, getClass().getResource("/styles/cpx-default.css").toExternalForm());
                super.show();
            }

        };
    }
    
    /*
    *
    * PRIVATE METHODES
    *
     */
    private String getPattern(String pSubString) {
        switch (pSubString.length()) {
            case 2:
                return "\\d\\d";
            case 4:
                return "\\d\\d\\d\\d";
            default:
                return "-1";
        }
    }

}
