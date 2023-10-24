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

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.shared.lang.Lang;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.scene.control.TextFormatter;

/**
 *
 * @author wilde
 */
public class CurrencyTextField extends DoubleTextField {

    private static final Logger LOG = Logger.getLogger(CurrencyTextField.class.getName());
//    private static String currencySign;
    public static final String NULL_STRING = "0" + Lang.getNumberFormatDecimal() + "00€";
    private static final String CURRENCY_SIGN = Lang.getCurrencySymbol();
    public CurrencyTextField() {
        super();
//        currencySign = getCurrencySign();
        getConverter().setItemName(CURRENCY_SIGN);
        getConverter().setNullString(NULL_STRING); 
        Locale locale = new Locale(Session.instance().getCpxLocale());
        if (Locale.GERMAN.equals(locale)) {
            setPattern(String.format("^-?([0-9]+(\\,[0-9]{1,2})?)[" + CURRENCY_SIGN + "]{1}?$", Lang.getNumberFormatDecimal()));
        } else if (Locale.ENGLISH.equals(locale)) {
            setPattern(String.format("^-?([0-9]+(\\.[0-9]{1,2})?)[" + CURRENCY_SIGN + "]{1}?$", Lang.getNumberFormatDecimal()));
        } else {
            setPattern(String.format("-?(\\d*(%s-?\\d*)*)", Lang.getNumberFormatDecimal()));
        }
//        setPattern(String.format("-?(\\d*(%s-?\\d*)*)", Lang.getNumberFormatDecimal()));
        setText(NULL_STRING);

    }

//    private static String getCurrencySign() {
//        if (currencySign == null) {
//            currencySign = CURRENCY_SIGN;
////            try {
////
////                Locale locale = new Locale(Session.instance().getCpxLocale());
////                currencySign = Currency.getInstance(locale).getSymbol();
////            } catch (Exception ex) {
////                LOG.log(Level.SEVERE, "currency sumol not found, we use €:", ex);
////                currencySign = "€";
////            }
//        }
//        return currencySign;
//    }

//    @Override
//    protected NumberConverter setDoubleConverter() {
//        if (converter == null) {
//            return new NumberConverter();
//        }
//        return converter;
//    }
//
    @Override
    public void setPattern(String pPattern) {
        Pattern pattern = Pattern.compile(String.format(pPattern));
        TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
//                String newText = change.getControlNewText().isEmpty() ? NULL_STRING : change.getControlNewText();
//                if (newText.endsWith(Lang.getNumberFormatDecimal())) {
//                    newText += "00";
//                }
//                if (!newText.endsWith(getCurrencySign())) {
//                    newText += getCurrencySign();
//                }
                String newText = change.getControlNewText().isEmpty() ? NULL_STRING : checkTextDigits(change.getControlNewText());
//                LOG.log(Level.INFO, "change.getControlNewText()= " + change.getControlNewText() + " newText=" + newText + " change.getControlText()= " + change.getControlText());
                if (pattern.matcher(newText).matches()) {
//                    double val = (Double)getConverter().fromString(change.getControlNewText().isEmpty() ? NULL_STRING : change.getControlNewText());
//                    LOG.log(Level.INFO, "val = " + String.valueOf(val));
                    return change;
                }
                return null;
            }
        });
        setTextFormatter(formatter);
    }

    @Override
    protected void validateOnFocusLost() {
        if (getText() == null || getText().isEmpty()) {
            setDisplayValue(NULL_STRING);
            return;
        }

        String text = checkTextDigits(getText());
        setDisplayValue(text);
        setText(text);

    }

    @Override
    public String checkTextDigits(String text) {
        if (text.endsWith(CURRENCY_SIGN)) {
            text = text.substring(0, text.length() - 1);
        }
        int decPos = text.indexOf(Lang.getNumberFormatDecimal());
        if (decPos < 0) {
            text += Lang.getNumberFormatDecimal() + "00";
            decPos = text.indexOf(Lang.getNumberFormatDecimal());
        }
        if(decPos == 0) {
            text = "0" + text;
            decPos = 1;
        }
        if (decPos + 3 == text.length()) {
            ;
        } else 
        if (text.endsWith(Lang.getNumberFormatDecimal())) {
            text += "00";
        } else if(decPos + 2 == text.length()){
            text += "0";
        }
        if(decPos + 3 < text.length()){
            text = text.substring(0, decPos + 3);
        }
        return text + CURRENCY_SIGN;
    }

}
