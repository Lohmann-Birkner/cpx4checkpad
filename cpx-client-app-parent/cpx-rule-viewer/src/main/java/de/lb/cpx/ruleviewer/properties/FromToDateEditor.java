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
package de.lb.cpx.ruleviewer.properties;

import de.lb.cpx.client.core.model.fx.datepicker.FormatedDatePicker;
import de.lb.cpx.rule.element.model.Rule;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import de.lb.cpx.shared.lang.Lang;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanProperty;
import org.controlsfx.property.editor.PropertyEditor;

/**
 *
 * @author wilde
 */
public class FromToDateEditor implements PropertyEditor<String> {

    private FormatedDatePicker pickerFrom;
    private FormatedDatePicker pickerTo;
    private Rule rule;
    private final PropertySheet.Item item;

    public FromToDateEditor(PropertySheet.Item property) {
        item = property;
    }

    @Override
    public Node getEditor() {
        if (pickerFrom == null) {

            pickerFrom = new FormatedDatePicker();
            pickerFrom.setMaxWidth(Double.MAX_VALUE);
            pickerFrom.setMinWidth(70);
            pickerFrom.setPrefWidth(150);
            pickerFrom.getEditor().setStyle("-fx-font-size:14px;");
            pickerFrom.valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    if(newValue == null){
                        pickerFrom.setValue(oldValue);
                        return;
                    }
                    getRule().setFrom(getValue(pickerFrom.getValue()));
                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                    Event.fireEvent(pickerFrom, saveEvent);
                }
            });
            pickerFrom.getEditor().textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                    if (t1 == null || t1.isEmpty()) {
                        pickerFrom.setValue(null);
                    }
                }
            });
            pickerFrom.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    if (!t1) {
                        getRule().setFrom(getValue(pickerFrom.getValue()));
                        RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                        Event.fireEvent(pickerFrom, saveEvent);
                    }
                }
            });
        }
        if (pickerTo == null) {
            pickerTo = new FormatedDatePicker();
            pickerTo.setMaxWidth(Double.MAX_VALUE);
            pickerTo.setMinWidth(70);
            pickerTo.setPrefWidth(150);
            pickerTo.getEditor().setStyle("-fx-font-size:14px;");
            pickerTo.valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                    if(newValue == null){
                        pickerTo.setValue(oldValue);
                        return;
                    }
                    getRule().setTo(getValue(pickerTo.getValue()));
                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                    Event.fireEvent(pickerTo, saveEvent);
                }
            });
            pickerTo.getEditor().textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                    if (t1 == null || t1.isEmpty()) {
                        pickerTo.setValue(null);
                    }
                }
            });
            pickerTo.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    if (!t1) {
//                        item.setValue(getValue());
                        getRule().setTo(getValue(pickerTo.getValue()));
                        RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                        Event.fireEvent(pickerTo, saveEvent);
                    }
                }
            });
        }
        Label lbl = new Label("bis:");
        lbl.setTooltip(new Tooltip(lbl.getText()));
//        lbl.setMaxWidth(Double.MAX_VALUE);
//        lbl.setAlignment(Pos.CENTER_LEFT);
//        HBox.setHgrow(lbl, Priority.ALWAYS);
//        HBox box = new HBox(pickerFrom, lbl, pickerTo);
//        box.setAlignment(Pos.CENTER_LEFT);
//        box.setFillHeight(true);
        lbl.setPadding(new Insets(0, 10, 0, 10));
        lbl.setPrefWidth(150);
//        lbl.setMaxWidth(La);
        lbl.setMinWidth(50);
        lbl.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(lbl, Priority.ALWAYS);
        HBox box = new HBox(pickerFrom, lbl, pickerTo);
        HBox.setHgrow(pickerFrom, Priority.ALWAYS);
        HBox.setHgrow(pickerTo, Priority.ALWAYS);
//        box.setMinWidth(Label.USE_PREF_SIZE);
//        box.setMaxWidth(Double.MAX_VALUE);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setFillHeight(true);
        box.disabledProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                pickerFrom.setDisable(t1);
                pickerTo.setDisable(t1);
            }
        });
        return box;
    }

    @Override
    public String getValue() {
        if (((DatePicker) getEditor()).getValue() == null) {
            return null;
        }
        return Lang.toDate(((DatePicker) getEditor()).getValue());
    }

    public String getValue(LocalDate pDate) {
        return Lang.toDate(pDate);
    }

    @Override
    public void setValue(String value) {
        //needs to be called to avoid np exception!
        getEditor();
        try {
            pickerFrom.setValue(LocalDate.parse(getRule().getFrom(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        } catch (DateTimeParseException ex) {
            LOG.log(Level.FINEST, "can not parse from data " + rule.getFrom(), ex);
            LOG.log(Level.WARNING, "can not parse from data {0}", rule.getFrom());
        }
        try {
            pickerTo.setValue(LocalDate.parse(getRule().getTo(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        } catch (DateTimeParseException ex) {
            LOG.log(Level.FINEST, "can not parse to data " + rule.getTo(), ex);
            LOG.log(Level.WARNING, "can not parse to data {0}", rule.getTo());
        }
    }
    private static final Logger LOG = Logger.getLogger(FromToDateEditor.class.getName());

    public Rule getRule() {
        if (rule == null) {
            Object bean = ((BeanProperty) item).getBean();
            if (!(bean instanceof Rule)) {
                LOG.severe("bean of the editor is not a Suggestion!");
                return null;
            }
            rule = (Rule) bean;
        }
        return rule;
    }
}
