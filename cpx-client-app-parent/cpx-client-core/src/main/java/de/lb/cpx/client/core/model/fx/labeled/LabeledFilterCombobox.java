/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.labeled;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.StringConverter;

/**
 * Labeled FilterCombobox Control
 *
 * @author niemeier
 * @param <T> type of Object in the Combobox
 */
public class LabeledFilterCombobox<T> extends LabeledControl<FilterComboBox<T>> {

    private static final Logger LOG = Logger.getLogger(LabeledFilterCombobox.class.getName());
    private final ObjectProperty<T> value = new SimpleObjectProperty<>(this, "value");

    private void setListener() {
        //clear selection on ENTF key
//        setOnKeyReleased((event) -> {
//            if (!isDisabled() && event.getCode() == KeyCode.DELETE) {
//                getControl().getSelectionModel().clearSelection();
//            }
//        });
//        getControl().getEditor().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                if (KeyCode.UP.equals(event.getCode())
//                        || KeyCode.DOWN.equals(event.getCode())) {
//                    AutoCompletionBinding
//                    //getControl().getEditor().textProperty().
////                    if (!getControl().isShowing()) {
////                        getControl().show();
////                        event.consume();
////                    }
//                }
//            }
//        });
//        getControl().getEditor().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                if (KeyCode.ENTER.equals(event.getCode())) {
//                    if (!getControl().isShowing()) {
//                        getControl().show();
//                        selectFirst();
//                        event.consume();
//                    }
//                }
//                if (KeyCode.UP.equals(event.getCode())) {
//                    if (!getControl().isShowing()) {
//                        getControl().show();
//                        selectUp();
//                        event.consume();
//                    }
//                }
//                if (KeyCode.DOWN.equals(event.getCode())) {
//                    if (!getControl().isShowing()) {
//                        getControl().show();
//                        selectDown();
//                        event.consume();
//                    }
//                }
//            }
//
//            private void selectFirst() {
//                if (getControl().getSelectionModel().getSelectedItem() == null) {
//                    getControl().getSelectionModel().select(0);
//                    return;
//                }
////                getControl().getSelectionModel().select(getControl().getSelectionModel().getSelectedItem()!=null?getControl().getSelectionModel().getSelectedIndex():0);
//            }
//
//            private void selectDown() {
//                if (getControl().getSelectionModel().getSelectedItem() == null) {
//                    getControl().getSelectionModel().select(0);
//                    return;
//                }
//                int index = ((ListView) ((ComboBoxListViewSkin) getControl().getSkin()).getPopupContent()).getSelectionModel().getSelectedIndex();
//                if (index < getItems().size()) {
//                    getControl().getSelectionModel().select(index + 1);
//                    return;
//                }
//                getControl().getSelectionModel().select(index);
//            }
//
//            private void selectUp() {
//                if (getControl().getSelectionModel().getSelectedItem() == null) {
//                    getControl().getSelectionModel().select(0);
//                    return;
//                }
//                int index = ((ListView) ((ComboBoxListViewSkin) getControl().getSkin()).getPopupContent()).getSelectionModel().getSelectedIndex();
//                if (index > 0) {
//                    getControl().getSelectionModel().select(index - 1);
//                    return;
//                }
//                getControl().getSelectionModel().select(index);
//            }
//        });
    }

    /**
     * no arg for scene builder
     */
    public LabeledFilterCombobox() {
        this("Label");
        setListener();
    }

    /**
     * contruct a new Combobox with the label and an empty box
     *
     * @param pLabel label to set
     */
    public LabeledFilterCombobox(String pLabel) {
        super(pLabel, new FilterComboBox<>());
        setListener();
    }

    /**
     * Construct a new instance with given label and set of items
     *
     * @param pLabel label to set
     * @param items content of the combobox
     */
    @SuppressWarnings("unchecked")
    public LabeledFilterCombobox(String pLabel, T... items) {
        this(pLabel);
        setItems(items);
        setListener();
    }

    /**
     * sets items in the Combobox, overrides the current list, if items are set
     *
     * @param items items to set
     */
    @SuppressWarnings("unchecked")
    public final void setItems(T... items) {
        getControl().setFilterItems(FXCollections.observableArrayList(items));
    }

    /**
     * sets items in the Combobox, overrides the current list, if items are set
     *
     * @param items items to set
     */
    public void setItems(List<T> items) {
        getControl().setFilterItems(FXCollections.observableArrayList(items));
    }

    /**
     * @param handler handler for event to happen on onAction on control
     */
    public void setOnAction(EventHandler<ActionEvent> handler) {
        getControl().setOnAction(handler);
    }

    /**
     * Calls Combox.getItems and returns them
     *
     * @return All Items in the ComboBox
     */
    public ObservableList<T> getItems() {
        return getControl().getFilterItems();
    }

    /**
     * gets the currently selected Item from the comboBox
     *
     * @return get the selected item in the combobox
     */
    public T getSelectedItem() {
        return getControl().getSelectedItem();
    }

    /**
     * select the given item in the combobox if present
     *
     * @param select item to select
     */
    public void select(T select) {
        getControl().select(select);
    }

    /**
     * sets a string converter to the combobox, for rendering content properly
     *
     * @param stringConverter string converter
     */
    public void setConverter(StringConverter<T> stringConverter) {
        getControl().setFilterConverter(stringConverter);
    }

//    /**
//     * get the selectedItemProperty from the combobox
//     *
//     * @return item property as read only
//     */
//    public ReadOnlyObjectProperty<T> getSelectedItemProperty() {
//        return getControl().getSelectionModel().selectedItemProperty();
//    }
    public void setValue(T value) {
        valueProperty().set(value);
    }

    public ObjectProperty<T> valueProperty() {
        return value;
    }

//    /**
//     * set the cell factory to the combobox
//     *
//     * @param pCallBack cell factory call back
//     */
//    public void setCellFactory(Callback<ListView<T>, ListCell<T>> pCallBack) {
//        getControl().setCellFactory(pCallBack);
//    }
//    /**
//     * get the current cell factory callback
//     *
//     * @return current cell factory
//     */
//    public Callback<ListView<T>, ListCell<T>> getCellFactory() {
//        return getControl().getCellFactory();
//    }
//
//    /**
//     * @return get the currently set button cell of the controll
//     */
//    public ListCell<T> getButtonCell() {
//        return getControl().getButtonCell();
//    }
//    /**
//     * @param pButtonCell list cell for rendering selcted value
//     */
//    public void setButtonCell(ListCell<T> pButtonCell) {
//        getControl().setButtonCell(pButtonCell);
//    }
    /**
     * select item by its "id" field
     *
     * @param pId id of the field to select
     * @throws NoSuchFieldException thrown if T has no field with name id
     * @throws IllegalAccessException thrown if field id is private
     */
    public void selectId(long pId) throws NoSuchFieldException, IllegalAccessException {
        for (T item : getItems()) {
            Field field;
            try {
                field = item.getClass().getDeclaredField("id");
                field.setAccessible(true);
            } catch (NoSuchFieldException ex) {
                LOG.log(Level.SEVERE, "Field with this id does not exist: " + pId, ex);
                field = item.getClass().getSuperclass().getDeclaredField("id");
                field.setAccessible(true);
            }
            long id = (long) field.get(item);
            if (id == pId) {
                select(item);
            }
        }
    }

}
