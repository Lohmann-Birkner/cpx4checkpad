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
package de.lb.cpx.ruleviewer.analyser.editors;

import de.lb.cpx.client.core.model.fx.datepicker.FormatedDatePicker;
import de.lb.cpx.shared.lang.Lang;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

/**
 *
 * @author wilde
 */
public class DateOnlyEditor implements PropertyEditor<Date> {

    private DatePicker picker;
    private final PropertySheet.Item item;
    private HBox editor;

    public DateOnlyEditor(PropertySheet.Item property) {
        item = property;
    }
    public final PropertySheet.Item getItem(){
        return item;
    }
    public final DatePicker getDatePicker(){
        return picker;
    }
    @Override
    public Node getEditor() {
        if (editor == null) {
            Date date = (Date) item.getValue();
            picker = new FormatedDatePicker();
            picker.setValue(Lang.toLocalDate(date));
            picker.getEditor().setStyle("-fx-font-size:14px;");
            picker.valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    if (newValue == null) {
                        picker.setValue(oldValue);
                        return;
                    }
                    item.setValue(getValue());
                    CaseChangedEvent saveEvent = new CaseChangedEvent(CaseChangedEvent.caseChangedEvent());
                    Event.fireEvent(picker, saveEvent);
                }
            });
            picker.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            picker.getEditor().selectAll();
                        }
                    });
                }
            });
            HBox.setHgrow(picker, Priority.ALWAYS);
            editor = new HBox(5, picker);
            editor.setAlignment(Pos.CENTER);
            editor.setFillHeight(true);
        }
        return editor;
    }

    @Override
    public Date getValue() {
        HBox box = getEditorContainer();
        DatePicker date = (DatePicker) box.getChildren().get(0);
        LocalDateTime dt = LocalDateTime.of(date.getValue(),LocalTime.MIN);
        return Date.from(dt.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public void setValue(Date value) {
        HBox box = getEditorContainer();
        DatePicker date = (DatePicker) box.getChildren().get(0);
        date.setValue(Lang.toLocalDate(value));
    }
    
    public final HBox getEditorContainer(){
        return (HBox) getEditor();
    }
}
