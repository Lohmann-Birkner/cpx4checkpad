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

import de.lb.cpx.client.core.model.fx.autocompletion.AutocompletionMultiColTextField;
import java.io.Serializable;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

/**
 * Labeled TextField class handles a textfield and an corresponting label
 *
 * @author wilde
 * @param <T> type
 */
public class LabeledAutocompletionMultiColTextField<T extends Serializable> extends LabeledTextControl<AutocompletionMultiColTextField<T>> {

    /**
     * default no arg constructor for scene builder
     */
    public LabeledAutocompletionMultiColTextField() {
        super("LabeledTextField", new AutocompletionMultiColTextField<>());
    }

    /**
     * creates a new textField with that label
     *
     * @param pLabel label text
     */
    public LabeledAutocompletionMultiColTextField(String pLabel) {
        this(pLabel, 0);
    }

    /**
     * creates a new textField with that label
     *
     * @param pLabel textArea
     * @param maxSize maximum size (maximum amount of characters)
     */
    public LabeledAutocompletionMultiColTextField(String pLabel, int maxSize) {
        super(pLabel, new AutocompletionMultiColTextField<T>(), maxSize); // by default don't show character counts
    }

    /**
     * creates new LabeledTextField with label and control
     *
     * @param pLabel label text
     * @param pAutocompletionMultiColTextField control
     */
    public LabeledAutocompletionMultiColTextField(String pLabel, AutocompletionMultiColTextField<T> pAutocompletionMultiColTextField) {
        super(pLabel, pAutocompletionMultiColTextField);
        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    pAutocompletionMultiColTextField.requestFocus();
                }
            }
        });
    }

    /**
     * creates new LabeledTextField with label and control
     *
     * @param pLabel label text
     * @param pAutocompletionMultiColTextField control
     * @param maxSize maximum size (maximum amount of characters)
     */
    public LabeledAutocompletionMultiColTextField(String pLabel, AutocompletionMultiColTextField<T> pAutocompletionMultiColTextField, int maxSize) {
        super(pLabel, pAutocompletionMultiColTextField, maxSize);
        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    pAutocompletionMultiColTextField.requestFocus();
                }
            }
        });
    }

    public void setPromptText(String pText) {
        getControl().setPromptText(pText);
    }

    @Override
    public CharsDisplayMode getDefaultCharsDisplayMode() {
        return CharsDisplayMode.NONE;
    }

    /**
     * set the title row for popup
     *
     * @param pTitles titles for popup
     */
    public void setTitles(final String[] pTitles) {
        getControl().getAutocompletion().setTitles(pTitles);
    }

    /**
     * set the title row for popup
     *
     * @param pTitles titles for popup
     */
    public void setTitles(final List<String> pTitles) {
        getControl().getAutocompletion().setTitles(pTitles);
    }

    /**
     * gives the title row for popup
     *
     * @return titles for popup
     */
    public String[] getTitles() {
        return getControl().getAutocompletion().getTitles();
    }

    public ObjectProperty<String[]> titlesProperty() {
        return getControl().getAutocompletion().titlesProperty();
    }

    /**
     * gives minimum of characters before search is executed
     *
     * @return search will only fire when this minimum of characters was given
     * by the user in text field
     */
    public int getMinFilterTextLength() {
        return getControl().getAutocompletion().getMinFilterTextLength();
    }

    /**
     * search will only fire when this minimum of characters was given by the
     * user in text field
     *
     * @param pMinFilterTextLength minimum amount of characters before execute
     * search
     */
    public void setMinFilterTextLength(final int pMinFilterTextLength) {
        getControl().getAutocompletion().setMinFilterTextLength(pMinFilterTextLength);
    }

    public IntegerProperty minFilterTextLengthProperty() {
        return getControl().getAutocompletion().minFilterTextLengthProperty();
    }

    public IntegerProperty maxEntriesProperty() {
        return getControl().getAutocompletion().maxEntriesProperty();
    }

    /**
     * gets maximum number of entries in popup
     *
     * @return maximum amount of entries to be shown
     */
    public int getMaxEntries() {
        return getControl().getAutocompletion().getMaxEntries();
    }

    /**
     * sets maximum number of entries in popup
     *
     * @param pMaxEntries maximum amount of entries to be shown
     */
    public void setMaxEntries(final int pMaxEntries) {
        getControl().getAutocompletion().setMaxEntries(pMaxEntries);
    }

    public ObjectProperty<List<T>> entriesProperty() {
        return getControl().getAutocompletion().entriesProperty();
    }

    /**
     * Get the existing set of autocomplete entries.
     *
     * @return The existing autocomplete entries.
     */
    public List<T> getEntries() {
        return getControl().getAutocompletion().getEntries();
    }

    /**
     * set list of entries for popup
     *
     * @param pEntries entry list
     */
    public void setEntries(final List<T> pEntries) {
        getControl().getAutocompletion().setEntries(pEntries);
    }

    /**
     * clears list of entries for popup
     */
    public void clearEntries() {
        getControl().getAutocompletion().clearEntries();
    }

    /**
     * get selected item from popup
     *
     * @return selected item
     */
    public T getSelectedItem() {
        return getControl().getAutocompletion().getSelectedItem();
    }

    public ReadOnlyObjectProperty<T> selectedItemProperty() {
        return getControl().getAutocompletion().selectedItemProperty();
    }

    /**
     * fires when an item is drawn and can be used to add controls to each popup
     * entry
     *
     * @param pItemHandler item handler
     */
    public void setItemHandler(final Callback<T, List<? extends Node>> pItemHandler) {
        getControl().getAutocompletion().setItemHandler(pItemHandler);
    }

    /**
     * fires when values have to be splitted for popup
     *
     * @param pSplitHandler split handler
     */
    public void setSplitHandler(final Callback<T, String[]> pSplitHandler) {
        getControl().getAutocompletion().setSplitHandler(pSplitHandler);
    }

    /**
     * fires when text input has changed
     *
     * @param pFilterTextChangeHandler change handler
     */
    public void setOnFilterTextChanged(final EventHandler<? super KeyEvent> pFilterTextChangeHandler) {
        getControl().getAutocompletion().setOnFilterTextChanged(pFilterTextChangeHandler);
    }

    /**
     * fires when an entry from popup is selected
     *
     * @param pSelectEntryHandler selection handler
     */
    public void setOnSelectEntry(final EventHandler<? super Event> pSelectEntryHandler) {
        getControl().getAutocompletion().setOnSelectEntry(pSelectEntryHandler);
    }

    /**
     * hide popup with entries
     */
    public void hidePopup() {
        getControl().getAutocompletion().hidePopup();
    }

    /**
     * is popup with entries visible to the user?
     *
     * @return popup entries open?
     */
    public boolean isPopupShowing() {
        return getControl().getAutocompletion().isPopupShowing();
    }

    /**
     * starts search immediately
     */
    public void startSearch() {
        getControl().getAutocompletion().startSearch();
    }

}
