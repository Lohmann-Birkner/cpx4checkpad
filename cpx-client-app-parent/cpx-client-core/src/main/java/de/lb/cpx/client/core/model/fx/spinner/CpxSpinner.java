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

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.StringConverter;

/**
 * abstract basic class for Spinners in Cpx
 *
 * @author wilde
 * @param <T> type of object to spin
 */
public abstract class CpxSpinner<T> extends Spinner<T> {
//    protected StringProperty currentValue = new SimpleStringProperty();

    private ChangeListener<String> textListener;
    private final ObjectProperty<T> currentValue = new SimpleObjectProperty<>();
    private boolean selectAllOnFocus = false;

    public ObjectProperty<T> getCurrentValue() {
        return currentValue;
    }

    public CpxSpinner(String initialValue, SpinnerValueFactory<T> factory) {
        super();
//        this.currentValue.setValue(initialValue);
        setValueFactory(factory);
        setText(initialValue);
        currentValue.set(parse2(initialValue));
        getEditor().setAlignment(Pos.CENTER_RIGHT);
        setEditable(true);
        setUpEventListener();
//        textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                if(newValue.isEmpty()){
//                    setText(String.valueOf(currentValue.get()));
//                }
//            }
//        });
    }

    public final void setText(String txt) {
        getEditor().setText(txt);
    }

    private T parse2(String pValue) {
        return parse(pValue);
    }

    public abstract T parse(String pValue);
//    public StringProperty getCurrentTextProperty(){
//        return currentValue;
//    }

    public String getText() {
        return getEditor().getText();
    }

    public StringProperty textProperty() {
        return getEditor().textProperty();
    }

    public final void setConverter(StringConverter<T> pConverter) {
        getValueFactory().setConverter(pConverter);
    }

    public final StringConverter<T> getConverter() {
        return getValueFactory().getConverter();
    }

    private void setUpEventListener() {

//        .addTextListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                setText(newValue);
//            }
//        });
//        getEditor().textProperty().addTextListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                try{
//                    parse(newValue);
//                }catch(IllegalArgumentException ex){
//                    setText(oldValue);
//                }
//            }
//        });
        getEditor().addEventHandler(KeyEvent.KEY_TYPED, event -> {
            if (event.getCharacter().equals("+")) {
                increment();
                event.consume();
            }
            if (event.getCharacter().equals("-")) {
                decrement();
                event.consume();
            }
        });
        getEditor().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP:
                    event.consume();
                    getValueFactory().increment(1);
                    break;
                case DOWN:
                    event.consume();
                    getValueFactory().decrement(1);
                    break;
                case PAGE_UP:
                    event.consume();
                    getValueFactory().increment(10);
                    break;
                case PAGE_DOWN:
                    event.consume();
                    getValueFactory().decrement(10);
                    break;
//                case BEGIN:
//                    break;
//                case END:
//                    break;
                default:
                    break;
            }
        });
        getEditor().setOnScroll((ScrollEvent evt) -> {
            evt.consume();
            int step = evt.isControlDown() ? 10 : 1;
            if (evt.getDeltaY() > 0) {
                getValueFactory().increment(step);
            } else if (evt.getDeltaY() < 0) {
                getValueFactory().decrement(step);
            }
        });
        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue && selectAllOnFocus) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            getEditor().selectAll();
                        }
                    });
                } else {
                    commitEditorText(CpxSpinner.this);
                }
            }
        });
    }

    public void addTextListener(ChangeListener<String> pListener) {
        if (textListener != null) {
            getEditor().textProperty().removeListener(textListener);
        }
        textListener = pListener;
        getEditor().textProperty().addListener(textListener);
    }

    protected final void selectAllOnFocus(boolean pSelectAll) {
        selectAllOnFocus = pSelectAll;
    }

    protected boolean getSelectAllOnFocus() {
        return selectAllOnFocus;
    }

    private <T> void commitEditorText(Spinner<T> spinner) {
        if (!spinner.isEditable()) {
            return;
        }
        String text = spinner.getEditor().getText();
        SpinnerValueFactory<T> valueFactory = spinner.getValueFactory();
        if (valueFactory != null) {
            StringConverter<T> converter = valueFactory.getConverter();
            if (converter != null) {
//            if(!text.isEmpty()){
                T value = converter.fromString(text);
                valueFactory.setValue(value);
//            }else{
//                T value = (T) currentValue.get();
//                valueFactory.setValue(value);
//            }
            }
        }
    }

}
