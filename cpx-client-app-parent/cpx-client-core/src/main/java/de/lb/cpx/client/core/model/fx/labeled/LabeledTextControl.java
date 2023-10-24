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

import de.lb.cpx.client.core.util.TextLimiter;
import de.lb.cpx.shared.lang.Lang;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;

/**
 *
 * @author nandola
 * @param <T> type
 */
public abstract class LabeledTextControl<T extends TextInputControl> extends LabeledControl<T> {

    private final TextLimiter limiter;

    public enum CharsDisplayMode {
        ALWAYS, ONLY_WHEN_RESTRICTED, NONE
    }

//    private IntegerProperty maxLength = new SimpleIntegerProperty(this, "maxLength", -1);
//    private int maxSize;
    private CharsDisplayMode displayMode = getDefaultCharsDisplayMode() == null ? CharsDisplayMode.ALWAYS : getDefaultCharsDisplayMode();

    public abstract CharsDisplayMode getDefaultCharsDisplayMode();

//    /**
//     * no arg contructor for scenebuilder with default title label
//     */
//    public LabeledTextControl() {
//        this("LabeledTextControl", 255);    //default label and allowed no. of characters
////        setWrapText(true);
////        this.maxSize = 255;
////        setListener();
//    }
    public CharsDisplayMode getCharsDisplayMode() {
        return displayMode;
    }

    public void setCharsDisplayMode(final CharsDisplayMode pDisplayMode) {
        displayMode = pDisplayMode == null ? CharsDisplayMode.NONE : pDisplayMode;
    }

    /**
     * unlimited text area
     *
     * @param pLabel label
     * @param pInstance instance of control
     */
    public LabeledTextControl(String pLabel, final T pInstance) {
        this(pLabel, pInstance, 0);
    }

    /**
     * creates a new labeled Textarea with given Title
     *
     * @param pLabel textArea
     * @param pInstance instance of control
     * @param maxSize maximum size (maximum amount of characters)
     */
    public LabeledTextControl(String pLabel, final T pInstance, int maxSize) {
        super(pLabel, pInstance);
//        setWrapText(true);
        //maxSize <= 0 -> no limitation
        limiter = new TextLimiter(maxSize);
        setListener();
        updateCharsInfo();
    }

//    @Override
//    public T getControl() {
//        return super.getControl();
//    }
    /**
     * gets the current textproerty of the textarea
     *
     * @return textproerty
     */
    public StringProperty getTextProperty() {
        return getControl().textProperty();
    }

    /**
     * gets the current setted text
     *
     * @return text as string
     */
    public String getText() {
        return getControl().getText();
    }

    /**
     * gets the length of current text
     *
     * @return return text length
     */
    public int getLength() {
        return getControl().getLength();
    }

    /**
     * sets the current Text
     *
     * @param pText text to set
     */
    public void setText(String pText) {
        getControl().setText(pText);
    }

    /**
     * sets the caret position (Sets the current caret position clearing the
     * previous selection as well)
     *
     * @param len a position to set
     */
    public void setCaretPosition(int len) {
        getControl().positionCaret(len);
    }

    /**
     * Selects the text between the last caret position up to the current caret
     * position that you entered.
     *
     * @param pos the current caret position that you entered.
     */
    public void selectPositionCaret(int pos) {
        getControl().selectPositionCaret(pos);
    }

    /**
     * sets a new textformatter
     *
     * @param pTextFormatter formatter to set
     */
    public void setTextFormatter(TextFormatter<?> pTextFormatter) {
        getControl().setTextFormatter(pTextFormatter);
    }

//    /**
//     * gets the textarea object
//     *
//     * @return current TextArea
//     */
//    public TextArea getTextArea() {
//        return getControl();
//    }
//
//    /**
//     * set text wrap value
//     *
//     * @param pWrapText value if text should be wrapped
//     */
//    public final void setWrapText(boolean pWrapText) {
//        getControl().setWrapText(pWrapText);
//    }
//
//    /**
//     * indicator if text is wrapping
//     *
//     * @return value if text wrap or not
//     */
//    public boolean isWrapText() {
//        return getControl().isWrapText();
//    }    
    /**
     * set editable
     *
     * @param pEditable editable
     */
    public final void setEditable(boolean pEditable) {
        getControl().setEditable(pEditable);
    }

    /**
     * editable
     *
     * @return editable
     */
    public boolean isEditable() {
        return getControl().isEditable();
    }

//    /**
//     * The max length property.
//     *
//     * @return The max length property.      
//     */
//    public IntegerProperty maxLengthProperty() {
//        return maxLength;
//    }
//    /**
//     * Gets the max length of the text field.
//     *
//     * @return The max length.      
//     */
//    public int getMaxLength() {
//        return maxLength.get();
//    }
//    /**
//     * Sets the max length of the text field.      
//     *
//     * @param maxLength The max length.      
//     */
//    public void setMaxLength(int maxLength) {
//        this.maxLength.set(maxLength);
//    }
    public final void setMaxSize(int maxSize) {
//        this.maxSize = maxSize < 0 ? 0 : maxSize;
        limiter.setMaxSize(maxSize);
    }

    public int getMaxSize() {
        return limiter.getMaxSize();
    }

    private void setListener() {
//        final BooleanProperty limitReachedProperty = new SimpleBooleanProperty(false);
//
//        final UnaryOperator<Change> modifyChange = c -> {
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
//        if(getControl().getTextFormatter() == null){
//            getControl().setTextFormatter(new TextFormatter<>(modifyChange));
//        }else{
//            UnaryOperator<Change> after = getControl().getTextFormatter().getFilter();
//            UnaryOperator<Change> compose = modifyChange.andThen(after)::apply;
//            getControl().setTextFormatter(new TextFormatter<>(compose));
//        }
        limiter.setTextInputControl(getControl()).buildAndApply();//.registerListeners(getControl());
        this.getControl().lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updateCharsInfo();
            }
        });
    }

    protected final void updateCharsInfo() {
        //show user how many characters are left
        if (getText() == null || getText().length() == 0) {
            setInfo(null);
        } else {
            if (CharsDisplayMode.NONE.equals(displayMode)) {
                setInfo(null);
            } else if (getMaxSize() == 0 && CharsDisplayMode.ONLY_WHEN_RESTRICTED.equals(displayMode)) {
                setInfo(null);
            } else if (getMaxSize() > 0) {
                //int charsLeft = maxSize - getText().length();
                setInfo(getText().length() + "/" + getMaxSize() + " " + Lang.getCharacters());
            } else {
                setInfo(getText().length() + " " + Lang.getCharacters());
            }
        }
    }

//    private ReadOnlyIntegerProperty lengthProperty() {
//        return maxLength;
//    }
}
