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
 *    2018  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.autocompletion;

import java.io.Serializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author niemeier
 * @param <T> type
 */
public class AutocompletionMultiColTextField<T extends Serializable> extends TextField implements AutocompletionMultiColInterface<T> {

    private final Autocompletion<T, ?> autocompletion;

    public AutocompletionMultiColTextField() {
        autocompletion = new Autocompletion<>(this);
    }

    @Override
    public void setListener() {
        //Hide always by focus-in (optional) and out
//        focusedProperty().addListener((observableValue, oldValue, newValue) -> {
//            entriesPopup.hide();
//        });
//
//        addEventHandler(EventType.ROOT, (event) -> {
//            //System.out.println("FOCUS!");
//            //System.out.println(event.getTarget());
//            event.get
//        });

        setOnKeyReleased((KeyEvent event) -> {
//            if (returnHandler != null && event.getCode() == KeyCode.RIGHT) {
//                returnHandler.handle(event);
//            }
            if (autocompletion.isPopupShowing() && (event.getCode() == KeyCode.BACK_SPACE)) {
                autocompletion.getEntriesPopup().hide();
                //return; //activate this to deactivate search on backspace key
            }

            if ((autocompletion.isPopupShowing() && (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP
                    || event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT))
                    || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.ESCAPE) {
                return;
            }

//            if (isEntriesPopupShowing() && event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP) {
//                entriesPopup.requestFocus();
//            }
//            if ((isEntriesPopupShowing() && (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP))
//                    || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.ESCAPE) {
//                return;
//            }
            autocompletion.startSearch(event);
        });
    }

    @Override
    public String getText2() {
        return StringUtils.trimToEmpty(getText());
    }

    @Override
    public Autocompletion<T, ?> getAutocompletion() {
        return autocompletion;
    }

    /**
     * Calculate the whole length of search text
     *
     * @param textArray search text as array
     * @param index length of the text
     * @return int
     */
    private int calculatePointerPosition(String[] textArray, int index) {
        int position = 0;
        for (int i = 0; i < index; i++) {
            position += textArray[i].length() + 1;
        }
        return position + textArray[index].length();
    }

    protected void select(String[] result, int position) {
        String[] fullText = getText2().trim().split(" ");
        fullText[position] = result[0].trim();
        String toPrint = String.join(" ", fullText);
        setText(toPrint);
        int p = calculatePointerPosition(fullText, position);
        if (p >= getText().length()) {
            p = getText().length();
        }
//                positionCaret(toPrint.indexOf(result)+result.length()-1);
        positionCaret(p);
    }

    @Override
    public void selectItem(String[] result, int position) {
        String[] fullText = getText().trim().split(" ");
        fullText[position] = result[0].trim();
        String toPrint = String.join(" ", fullText);
        setText(toPrint);
        int p = calculatePointerPosition(fullText, position);
        if (p >= getText().length()) {
            p = getText().length();
        }
//                positionCaret(toPrint.indexOf(result)+result.length()-1);
        positionCaret(p);
    }

}
