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
package de.lb.cpx.ruleviewer.analyser.editors;

import java.util.Arrays;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

/**
 *
 * @author wilde
 * @param <T> type
 */
public class ChoiceEditor<T> implements PropertyEditor<T> {

    private final PropertySheet.Item item;
    private final List<T> items;
    private ComboBox<T> comboBox;

    public ChoiceEditor(PropertySheet.Item property) {
        item = property;
        items = Arrays.<T>asList((T[]) item.getType().getEnumConstants());
    }

    @Override
    public Node getEditor() {
        if (comboBox == null) {
            comboBox = new ComboBox<>(FXCollections.observableArrayList(items));
            comboBox.setPrefWidth(350);
            comboBox.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
                @Override
                public ListCell<T> call(ListView<T> p) {
                    ListCell<T> cell = new ListCell<>() {
                        @Override
                        public void updateItem(T item, boolean empty) {
                            super.updateItem(item, empty);

                            getListView().setMaxWidth(350);
                            if (!empty) {
                                setText(comboBox.getConverter().toString(item));
                            } else {
                                setText(null);
                            }
                        }
                    };
                    return cell;
                }
            });
            comboBox.getEditor().setStyle("-fx-font-size:14px;");
            setValue((T) item.getValue());
            comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
                @Override
                public void changed(ObservableValue<? extends Object> ov, Object t, Object t1) {
                    item.setValue(getValue());
                    CaseChangedEvent saveEvent = new CaseChangedEvent(CaseChangedEvent.caseChangedEvent());
                    Event.fireEvent(comboBox, saveEvent);
                }
            });
        }
        return comboBox;
    }

    @Override
    public T getValue() {
        return ((ComboBox<T>) getEditor()).getSelectionModel().getSelectedItem();
    }

    @Override
    public void setValue(T value) {
        if (value == null) {
            item.setValue(null);
            return;
        }
        ((ComboBox<T>) getEditor()).getSelectionModel().select(value);
    }

}
