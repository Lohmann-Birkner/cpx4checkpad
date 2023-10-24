/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.spinner;

import de.lb.cpx.client.core.model.fx.spinner.SpinnerValueFactory.DoubleSpinnerValueFactory;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 * Spinner impl for double values
 *
 * @author wilde
 */
public class DoubleSpinner extends CpxSpinner<Double> {

    private static final Logger LOG = Logger.getLogger(DoubleSpinner.class.getName());

    private final int precision;

    /**
     * creates new instance
     *
     * @param pInitialValue initial value to be displayed
     * @param pMin min value
     * @param pMax max value
     * @param pPrecision precision of the double (digits after comma)
     * @throws CpxIllegalArgumentException when parsing failed
     */
    public DoubleSpinner(double pInitialValue, double pMin, double pMax, int pPrecision) throws CpxIllegalArgumentException {
        super(parse(pInitialValue, pPrecision), new SpinnerValueFactory.DoubleSpinnerValueFactory(pMin, pMax, pInitialValue));
        this.precision = pPrecision;
//        ((DoubleSpinnerValueFactory) getValueFactory())
//                        .setAmountToStepBy(1.0);
        getEditor().caretPositionProperty().addListener(it -> updateUnit());
        setConverter(converter);
//        setConverter(new StringConverter<Double>() {
//            @Override
//            public String toString(Double pDouble) {
//                if (pDouble != null) {
//                    return String.valueOf(pDouble);
//                }
//                return "";
//            }
//
//            @Override
//            public Double fromString(String string) {
//                if (string != null && !string.isEmpty()) {
//                    return Double.parseDouble(String.format("%."+precision+"f", string));
//                }
//                return null;
//            }
//        });
        TextField textField = getEditor();
        textField.addEventHandler(KeyEvent.KEY_TYPED, event -> {
            //RSH 20140904 : CPX-628
            if (("0123456789" + DIVIDER).contains(event.getCharacter())) {
                textField.cut();
                textField.selectForward();
            } else {
                event.consume();

            }
            if (DIVIDER.equals(event.getCharacter()) && (textField.getText().isEmpty() || textField.getText().charAt(textField.getCaretPosition() - 1) == DIVIDER.charAt(0))) {
                //If the users types slash again after it has been added, cancels it.
                event.consume();
            }
            if (checkPrecision()) {
                event.consume();
            }
            textField.selectForward();
//            if (!event.getCharacter().equals(DIVIDER) && textField.getSelectedText().equals(DIVIDER)) {
//                textField.cut();
//                textField.selectForward();
//            }
//            textField.cut();

//            Platform.runLater(() -> {
//                String textUntilHere = textField.getText(0, textField.getCaretPosition());
////                String[] pattern = Lang.getDateFormat().split("\\"+devider);
//                if (textUntilHere.matches("\\d\\d")) {// || textUntilHere.matches("\\d\\d:\\d\\d")) {
//                    String textAfterHere = "";
//                    int start = textField.getCaretPosition() + 1;
//                    int end = textField.getText().length();
//                    if (start <= end && start >= 0) {
//                        textAfterHere = textField.getText(textField.getCaretPosition() + 1, textField.getText().length());
//                    }
//                    int caretPosition = textField.getCaretPosition();
//                    textField.setText(textUntilHere + DIVIDER + textAfterHere);
//                    textField.positionCaret(caretPosition + 1);
//                }
//            });
        });
    }
    private final StringConverter<Double> converter = new StringConverter<Double>() {
        private final DecimalFormat df = new DecimalFormat("##.####");

        @Override
        public String toString(Double value) {
            // If the specified value is null, return a zero-length String
            if (value == null) {
                return "";
            }
            df.setMaximumFractionDigits(precision);
            df.setMinimumFractionDigits(precision);
            return df.format(value);
        }

        @Override
        public Double fromString(String value) {
            try {
                df.setMaximumFractionDigits(precision);
                df.setMinimumFractionDigits(precision);
                // If the specified value is null or zero-length, return null
                if (value == null) {
                    return null;
                }
                value = value.trim();

                if (value.length() < 1) {
                    return null;
                }

                // Perform the requested parsing
                return df.parse(value).doubleValue();
            } catch (ParseException ex) {
                //throw new RuntimeException(ex);
                LOG.log(Level.SEVERE, "This is not a valid double: " + value, ex);
                return null;
            }
        }
    };

    @Override
    public Double parse(String pValue) {
        if (pValue == null || pValue.trim().isEmpty()) {
            return null;
        }
        return Double.parseDouble(pValue.trim().replace(",", "."));
    }

    public static final String parse(double pValue, int pPrecision) {
        return String.format("%." + pPrecision + "f", pValue);
    }
//    @Override
//    public void increment() {
//        int pos = getEditor().getCaretPosition();
//        super.increment(); //To change body of generated methods, choose Tools | Templates.
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                getEditor().positionCaret(pos);
//            }
//        });
//    }

    @Override
    public void increment(int steps) {
        int pos = getEditor().getCaretPosition();
//        LOG.info("increment on pos " + pos + " - " + getEditor().getText());
        updateUnit();
        super.increment(steps); //To change body of generated methods, choose Tools | Templates.
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
//                LOG.info("after increment on pos " + pos + " - " + getEditor().getText());
                getEditor().positionCaret(pos);
            }
        });
    }

//    @Override
//    public void decrement() {
//        int pos = getEditor().getCaretPosition();
//        super.decrement(); //To change body of generated methods, choose Tools | Templates.
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                getEditor().positionCaret(pos);
//            }
//        });
//    }
    @Override
    public void decrement(int steps) {
        int pos = getEditor().getCaretPosition();
        super.decrement(steps); //To change body of generated methods, choose Tools | Templates.
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getEditor().positionCaret(pos);
            }
        });
    }

    private void updateUnit() {
        //check the position of the divider and set correct spinning amount
        //ugly but it works =D
//        int pos = getEditor().getCaretPosition();
        int pos2 = getDividierPosition();
//        LOG.info(getEditor().getText()+" - change pos " + (pos-pos2) + " caret " + pos + " after comma " + pos2);
        //4 should be enough lol ^^
//        switch (pos-pos2) {
//            case 0:
//                ((DoubleSpinnerValueFactory) getValueFactory())
//                        .setAmountToStepBy(1.0);
//                break;
//            case 1:
//                ((DoubleSpinnerValueFactory) getValueFactory())
//                        .setAmountToStepBy(0.1);
//                break;
//            case 2:
//                ((DoubleSpinnerValueFactory) getValueFactory())
//                        .setAmountToStepBy(0.01);
//                break;
//            case 3:
//                ((DoubleSpinnerValueFactory) getValueFactory())
//                        .setAmountToStepBy(0.001);
//                break;
//            case 4:
//                ((DoubleSpinnerValueFactory) getValueFactory())
//                    .setAmountToStepBy(0.0001);
//                break;
//            case 5:
//                ((DoubleSpinnerValueFactory) getValueFactory())
//                        .setAmountToStepBy(0.00001);
//                break;
//            default:
//                ((DoubleSpinnerValueFactory) getValueFactory())
//                        .setAmountToStepBy(1.0);
//        }
        switch (pos2) {
            case 0:
//                ((DoubleSpinnerValueFactory) getValueFactory())
//                        .setAmountToStepBy(0.1);
                break;
            case 1:
                ((DoubleSpinnerValueFactory) getValueFactory())
                        .setAmountToStepBy(0.1);
                break;
            case 2:
                ((DoubleSpinnerValueFactory) getValueFactory())
                        .setAmountToStepBy(0.01);
                break;
            case 3:
                ((DoubleSpinnerValueFactory) getValueFactory())
                        .setAmountToStepBy(0.001);
                break;
            case 4:
                ((DoubleSpinnerValueFactory) getValueFactory())
                        .setAmountToStepBy(0.0001);
                break;
            case 5:
                ((DoubleSpinnerValueFactory) getValueFactory())
                        .setAmountToStepBy(0.00001);
                break;
            default:
                ((DoubleSpinnerValueFactory) getValueFactory())
                        .setAmountToStepBy(1.0);
        }
    }
    private static final String DIVIDER = ",";

    private int getDividierPosition() {
        if (!getEditor().getText().contains(DIVIDER)) {
            return -1;
        }
        String[] split = getEditor().getText().split(DIVIDER);
        if (split.length != 2) {
            return -1;
        }
        int textlenght = getEditor().getCaretPosition();
        textlenght = textlenght - split[0].length();
        return textlenght - 1;
    }

    private boolean checkPrecision() {
        TextField textField = getEditor();
        String[] split = textField.getText().split(DIVIDER);
        if (split.length < 2) {
            return false;
        }
//        LOG.info("lenght after comma " + textField.getText().split(DIVIDER)[1].length() + " precision " + precision);
        if (textField.getText().split(DIVIDER)[1].length() == precision) {
            return true;
        }
        return false;
    }
}
