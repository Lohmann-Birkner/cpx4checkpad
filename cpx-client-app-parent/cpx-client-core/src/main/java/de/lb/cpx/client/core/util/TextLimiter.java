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
package de.lb.cpx.client.core.util;

import de.lb.cpx.client.core.model.fx.alert.NotificationsFactory;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.function.UnaryOperator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author wilde
 */
public class TextLimiter {

    private int maxSize = 255;
    private TextInputControl control;
    
    public TextLimiter(){
        
    }
    public TextLimiter(int pMaxSize){
        this();
        maxSize = pMaxSize;
    }
    public TextFormatter<String> build(){
        final BooleanProperty limitReachedProperty = new SimpleBooleanProperty(false);

        final UnaryOperator<TextFormatter.Change> modifyChange = c -> {
            if (c.isContentChange()) {
                int newSize = c.getControlNewText().length();
                if (maxSize > 0 && newSize > maxSize) {
                    boolean limitReachedOld = limitReachedProperty.get();
                    limitReachedProperty.set(true);
                    final int tooMuchSize = newSize - maxSize;
                    final String newText = c.getText().substring(0, c.getText().length() - tooMuchSize);
                    c.setText(newText);
                    int rangeStart = c.getRangeStart() + newText.length();
                    c.selectRange(rangeStart, rangeStart);

                    if (!limitReachedOld) {
                        Notifications not = NotificationsFactory.instance().createInformationNotification();
                        not.text(MessageFormat.format("Die Grenze von {0} Zeichen ist erreicht!", maxSize));
                        not.hideAfter(Duration.seconds(3D));
                        not.hideCloseButton();
                        not.show();
                    }
                } else {
                    limitReachedProperty.set(false);
                }
            }
            return c;
        };
        return new TextFormatter<>(modifyChange);
    }
    public void buildAndApply(){
        Objects.requireNonNull(getTextInputControl(), "TextInputControl must be set!");
        TextFormatter<String> formatter = build();
        Objects.requireNonNull(formatter, "TextInputControl must be set!");
        if(getTextInputControl().getTextFormatter() == null){
            getTextInputControl().setTextFormatter(formatter);
        }else{
            UnaryOperator<TextFormatter.Change> after = getTextInputControl().getTextFormatter().getFilter();
            UnaryOperator<TextFormatter.Change> compose = formatter.getFilter().andThen(after)::apply;
            getTextInputControl().setTextFormatter(new TextFormatter<>(compose));
        }
    }
//    public TextLimiter registerListeners(TextInputControl pControl) {
//        final BooleanProperty limitReachedProperty = new SimpleBooleanProperty(false);
//
//        final UnaryOperator<TextFormatter.Change> modifyChange = c -> {
//            if (c.isContentChange()) {
//                int newSize = c.getControlNewText().length();
//                if (maxSize > 0 && newSize > maxSize) {
//                    boolean limitReachedOld = limitReachedProperty.get();
//                    limitReachedProperty.set(true);
//                    final int tooMuchSize = newSize - maxSize;
//                    final String newText = c.getText().substring(0, c.getText().length() - tooMuchSize);
//                    c.setText(newText);
//                    int rangeStart = c.getRangeStart() + newText.length();
//                    c.selectRange(rangeStart, rangeStart);
//
//                    if (!limitReachedOld) {
//                        Notifications not = NotificationsFactory.instance().createInformationNotification();
//                        not.text(MessageFormat.format("Die Grenze von {0} Zeichen ist erreicht!", maxSize));
//                        not.hideAfter(Duration.seconds(3D));
//                        not.hideCloseButton();
//                        not.show();
//                    }
//                } else {
//                    limitReachedProperty.set(false);
//                }
//            }
//            return c;
//        };
//        if(pControl.getTextFormatter() == null){
//            pControl.setTextFormatter(new TextFormatter<>(modifyChange));
//        }else{
//            UnaryOperator<TextFormatter.Change> after = pControl.getTextFormatter().getFilter();
//            UnaryOperator<TextFormatter.Change> compose = modifyChange.andThen(after)::apply;
//            pControl.setTextFormatter(new TextFormatter<>(compose));
//        }
//        return this;
//    }

    public int getMaxSize() {
        return maxSize;
    }

    public TextLimiter setMaxSize(int maxSize) {
        this.maxSize = maxSize < 0 ? 0 : maxSize;
        return this;
    }
    
    public TextInputControl getTextInputControl(){
        return control;
    }
    public TextLimiter setTextInputControl(TextInputControl pControl){
        control = pControl;
        return this;
    }
    
}
