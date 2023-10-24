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
package de.lb.cpx.client.core.model.fx.spinner;

import de.lb.cpx.client.core.model.fx.spinner.SpinnerValueFactory.LocalTimeSpinnerValueFactory;
import de.lb.cpx.shared.lang.Lang;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 *
 * @author wilde
 */
public class CpxTimeSpinner extends CpxSpinner<LocalTime> {

    private static final Logger LOG = Logger.getLogger(CpxTimeSpinner.class.getName());

    private final ObjectProperty<LocalTime> timeValue = new SimpleObjectProperty<>(LocalTime.parse("00:00"));

    public CpxTimeSpinner(String initialValue) {
        super(initialValue, new SpinnerValueFactory.LocalTimeSpinnerValueFactory(LocalTime.parse(initialValue)));
        getEditor().setPromptText(Lang.getTimeFormat());
        getEditor().caretPositionProperty().addListener(it -> updateChronoUnit());
        selectAllOnFocus(true);
        timeValue.addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                setText(Lang.toTime(newValue));
//                getValueFactory().setValue(newValue);
            }
        });
        setConverter(new StringConverter<LocalTime>() {
            @Override
            public String toString(LocalTime date) {
                if (date != null) {
                    try {
                        return DateTimeFormatter.ofPattern(Lang.getTimeFormat()).format(date);
                    } catch (DateTimeException ex) {
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
                return "";
            }

            @Override
            public LocalTime fromString(String string) {
                if (string != null && !string.isEmpty() ) {
                    try {
                        LOG.log(Level.INFO, "fromString: {0}", string);
//                        if(!string.matches(Lang.getTimeFormat())){
//                            string = "00:00";
//                        }
//                        
                        LocalTime time = LocalTime.parse(string, DateTimeFormatter.ofPattern(Lang.getTimeFormat()));
                        timeValue.set(time);
                        return time;
                    } catch (DateTimeParseException ex) {
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
                return null;
            }
        });
        TextField textField = getEditor();
        textField.addEventHandler(KeyEvent.KEY_TYPED, event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                LOG.log(Level.INFO, "textfield: {0}", textField.getText());
            }
            //RSH 20140904 : CPX-628
            if (("0123456789:").contains(event.getCharacter())) {
                textField.cut();
                textField.selectForward();
            } else {
                event.consume();

            }
            if (":".equals(event.getCharacter()) && (textField.getText().isEmpty() || textField.getText().charAt(textField.getCaretPosition() - 1) == ':')) {
                //If the users types slash again after it has been added, cancels it.
                event.consume();
            }
            if (textField.getText().length() == Lang.getTimeFormat().length()) {
                event.consume();
            }
//            textField.selectForward();
            if (!event.getCharacter().equals(":") && textField.getSelectedText().equals(":")) {
                textField.cut();
                textField.selectForward();
            }
            textField.cut();

            Platform.runLater(() -> {
                String textUntilHere = textField.getText(0, textField.getCaretPosition());
//                String[] pattern = Lang.getDateFormat().split("\\"+devider);
                if (textUntilHere.matches("\\d\\d")) {// || textUntilHere.matches("\\d\\d:\\d\\d")) {
                    String textAfterHere = "";
                    int start = textField.getCaretPosition() + 1;
                    int end = textField.getText().length();
                    if (start <= end && start >= 0) {
                        textAfterHere = textField.getText(textField.getCaretPosition() + 1, textField.getText().length());
                    }
                    int caretPosition = textField.getCaretPosition();
                    textField.setText(textUntilHere + ":" + textAfterHere);
                    textField.positionCaret(caretPosition + 1);
                }
            });
        });

    }

    public CpxTimeSpinner() {
        this("00:00");
    }

    private void updateChronoUnit() {
        int pos = getEditor().getCaretPosition();
        switch (pos) {
            case 0:
                break;
            case 1:
            case 2:
                ((LocalTimeSpinnerValueFactory) getValueFactory())
                        .setTemporalUnit(HOURS);
                break;
            default:
                ((LocalTimeSpinnerValueFactory) getValueFactory())
                        .setTemporalUnit(MINUTES);
        }
    }

    /**
     * get LocalTime Value of the current valid Time in the TextArea of the
     * Spinner Uses default ISO-Time Format such as 13:45:00 or 13:45
     *
     * @return LocalTime representation
     */
    public LocalTime getLocalTime() {
//        if(getEditor().getText() != null){
//            return LocalTime.parse(getEditor().getText(), DateTimeFormatter.ISO_LOCAL_TIME);
//        }
//        return null;
        return timeValue.get();
    }

    /**
     * @return time property
     */
    public ObjectProperty<LocalTime> getTimeProperty() {
        return timeValue;
    }

    /**
     * @param pTime set Local Time, if null nothing happen
     */
    public void setLocalTime(LocalTime pTime) {
        if (pTime == null) {
            return;
        }
        timeValue.set(pTime);
    }
//    /**
//     * get LocalTime Value of the current valid Time in the TextArea of the Spinner
//     * @return LocalTime representation
//     */
//    public Date getDateTime(){
//        if(getEditor().getText() != null){
//            return CpxLanguageInterface;
//        }
//        return null;
//    }

    @Override
    public final LocalTime parse(String pValue) {
        if (pValue == null || pValue.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(pValue.trim());
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Cannot parse this value as LocalTime: " + pValue, ex);
        }
    }

}
