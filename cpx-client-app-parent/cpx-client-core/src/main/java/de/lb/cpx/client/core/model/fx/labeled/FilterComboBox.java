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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * Labeled Combobox Control look at
 * https://stackoverflow.com/questions/19010619/javafx-filtered-combobox
 *
 * @author niemeier
 * @param <T> type of Object in the Combobox
 */
public class FilterComboBox<T> extends ComboBox<String> {

    private ObservableList<T> filterItems;
    private final ObjectProperty<StringConverter<T>> filterConverter = new SimpleObjectProperty<>();
    private ObservableList<String> possibleSuggestions = FXCollections.observableArrayList();
    private AutoCompletionBinding<String> binding;

    public AutoCompletionBinding<String> getBinding() {
        return binding;
    }

    public FilterComboBox() {
        this(null);
    }

    public FilterComboBox(ObservableList<T> items) {
        this(items, null);
    }

    public FilterComboBox(ObservableList<T> items, StringConverter<T> stringConverter) {
        //super(toStringList(items));
        filterConverter.addListener(new ChangeListener<StringConverter<T>>() {
            @Override
            public void changed(ObservableValue<? extends StringConverter<T>> ov, StringConverter<T> oldValue, StringConverter<T> newValue) {
                if (oldValue != newValue) {
                    setFilterItems(filterItems);
                }
            }
        });
        setFilterConverter(stringConverter);
        setFilterItems(items);
    }

    private ObservableList<String> toStringList(ObservableList<T> items) {
        final ObservableList<String> suggestions = FXCollections.observableArrayList();
        if (items == null) {
            return suggestions;
        }
        int size = items.size();
        for (int i = 0; i < size; i++) {
            final T item = items.get(i);
            final String value = getValue(item);
            suggestions.add(value);
        }
        return suggestions;
    }

    public ObservableList<T> getFilterItems() {
        return filterItems;
    }

    public final void setFilterItems(ObservableList<T> items) {
        setEditable(true);
//            if (this.getEditor() instanceof FakeFocusTextField) {
//                ((FakeFocusTextField) this.getEditor()).setFakeFocus(true);
//            }
        filterItems = items;

        //FilteredList<T> filterList = new FilteredList<>(items);
//        getEditor().textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
//                final TextField editor = getEditor();
//                final T selected = getSelectionModel().getSelectedItem();
//
//                // This needs run on the GUI thread to avoid the error described
//                // here: https://bugs.openjdk.java.net/browse/JDK-8081700.
//                Platform.runLater(() -> {
//                    // If the no item in the list is selected or the selected item
//                    // isn't equal to the current input, we refilter the list.
//                    LOG.log(Level.INFO, "selected == null: " + (selected == null));
//                    LOG.log(Level.INFO, "selected.toString(): " + (selected == null ? "n.A." : selected.toString()));
//                    LOG.log(Level.INFO, "selected.toString(): " + (selected == null ? "n.A." : selected.toString()));
//                    LOG.log(Level.INFO, "editor.getText(): " + editor.getText());
//                    LOG.log(Level.INFO, "!(selected.toString() != null && selected.toString().equals(editor.getText())): " + (selected == null ? "n.A." : !(selected.toString() != null && selected.toString().equals(editor.getText()))));
//                    LOG.log(Level.INFO, "");
//                    if (selected == null || !(selected.toString() != null && selected.toString().equals(editor.getText()))) {
//                        filterList.setPredicate(new Predicate<T>() {
//                            @Override
//                            public boolean test(T item) {
//                                // We return true for any items that starts with the
//                                // same letters as the input. We use toUpperCase to
//                                // avoid case sensitivity.
//                                if (item == null || (item.toString() != null && item.toString().toUpperCase().startsWith(newValue.toUpperCase()))) {
//                                    return true;
//                                } else {
//                                    return false;
//                                }
//                            }
//                        });
//                        show();
//                    }
//                });
//            }
//        });
        if (binding != null) {
            binding.dispose();
        }
        possibleSuggestions.clear();
        possibleSuggestions = toStringList(items);
        setItems(possibleSuggestions);

        binding = TextFields.bindAutoCompletion(getEditor(), possibleSuggestions);
        binding.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> t) {
                hide();
            }
        });
//        binding.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<String>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<String> t) {
//                String selectedItem = getEditor().getText();
//                for (T item : items) {
//                    if (item != null && item.toString() != null && item.toString().equalsIgnoreCase(selectedItem)) {
////                        FilterComboBox.this.getSelectionModel().select(item);
////                        //binding.setUserInput(selectedItem.toString());
////                        t.consume();
//                        FilterComboBox.this.getSelectionModel().select(item);
//                        t.consume();
//                        break;
//                    }
//                }
//            }
//        });
//        binding.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<T>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<T> event) {
//                FilterComboBox.this.setT
//                setText(manager.getRealEntry(FilterComboBox.this.getText()));
//            }
//        });
//        binding.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<T>>() {
//            @Override
//            public void handle(AutoCompletionBinding.AutoCompletionEvent<T> event) {
//                getEditor().setText(event.getCompletion() == null ? null : event.getCompletion().toString());
//                hide();
//                event.consume();
//            }
//        });
    }

    public String getValue(final T item) {
        if (item == null) {
            return null;
        }
        if (filterConverter.get() == null) {
            return item.toString();
        } else {
            return filterConverter.get().toString(item);
        }
    }

    public T getSelectedItem() {
        String selectedItem = getEditor().getText();
        if (selectedItem == null) {
            return null;
        }
        if (filterConverter.get() != null) {
            T res = filterConverter.get().fromString(selectedItem);
            if (res != null) {
                return res;
            }
        }
        for (T item : filterItems) {
            String value = getValue(item);
            if (value != null && value.equalsIgnoreCase(selectedItem)) {
//                        FilterComboBox.this.getSelectionModel().select(item);
//                        //binding.setUserInput(selectedItem.toString());
//                        t.consume();
                return item;
            }
        }
        return null;
    }

    public void select(T item) {
        final String value = getValue(item);
        for (int i = 0; i < getItems().size(); i++) {
            String it = getItems().get(i);
            if (value != null && value.equalsIgnoreCase(it)) {
                FilterComboBox.this.getSelectionModel().select(it);
                return;
            }
        }
    }

    public final void setFilterConverter(StringConverter<T> stringConverter) {
        filterConverter.set(stringConverter);
        //setFilterItems(filterItems);
    }

}
