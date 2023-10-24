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

import de.lb.cpx.client.core.model.fx.spinner.CpxTimeSpinner;
import de.lb.cpx.shared.lang.Lang;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.controlsfx.control.PropertySheet;

/**
 * Possible Duplcation of other class de.lb.cpx.ruleviewer.properties.DateEditor
 * Behavior should be unified
 *
 * @author wilde
 */
public class DateEditor extends DateOnlyEditor {

    private CpxTimeSpinner spinner;
    private LocalTime time;

    public DateEditor(PropertySheet.Item property) {
        super(property);
    }

    @Override
    public Node getEditor() {
        HBox container = (HBox) super.getEditor();
        Date date = (Date) getItem().getValue();
       if(container.getChildren().size() > 1){
           return container;
       }


        spinner = new CpxTimeSpinner();
        spinner.getEditor().setStyle("-fx-font-size:14px;");
        HBox.setHgrow(spinner, Priority.ALWAYS);
        container.getChildren().add( spinner);

        spinner.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    if (spinner.getValue() != null && !spinner.getValue().equals(time)) {
                        time = spinner.getValue();
                        getItem().setValue(getValue());
//                        CaseChangedEvent saveEvent = new CaseChangedEvent(CaseChangedEvent.caseChangedEvent());
//                        Event.fireEvent(getDatePicker(), saveEvent);
                    }else{if(time == null){
                        spinner.setLocalTime(LocalTime.parse("00:00", DateTimeFormatter.ISO_LOCAL_TIME));
                    }
                        spinner.setLocalTime(time);
                    }
                     CaseChangedEvent saveEvent = new CaseChangedEvent(CaseChangedEvent.caseChangedEvent());
                     Event.fireEvent(getDatePicker(), saveEvent);
                }
            }
        });
        spinner.disabledProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    spinner.setLocalTime(LocalTime.parse("00:00", DateTimeFormatter.ISO_LOCAL_TIME));
                    time = spinner.getLocalTime();
                }
            }
        });
        

        
        time = Lang.toLocalTime(date);
        spinner.setLocalTime(time);

        return container;
    }

    @Override
    public Date getValue() {
        HBox box = (HBox) getEditor();
        DatePicker date = (DatePicker) box.getChildren().get(0);
        if (date.getValue() == null) {
            return null;
        }
        
        LocalTime tm = LocalTime.parse("00:00", DateTimeFormatter.ISO_LOCAL_TIME);
        if(box.getChildren().size() > 1){
   
            CpxTimeSpinner timespinner = (CpxTimeSpinner) box.getChildren().get(1);
            tm = timespinner.getLocalTime();
        }
        LocalDateTime dt = LocalDateTime.of(date.getValue(), tm);
        return Date.from(dt.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public void setValue(Date value) {
        HBox box = (HBox) getEditor();
        DatePicker date = (DatePicker) box.getChildren().get(0);
        if (value == null) {
            getItem().setValue(null);
            return;
        }
        date.setValue(Lang.toLocalDate(value));
        if(box.getChildren().size() <= 1){
            return;
        }
        CpxTimeSpinner spinner = (CpxTimeSpinner) box.getChildren().get(1);
        spinner.setLocalTime(Lang.toLocalTime(value));
    }

}
