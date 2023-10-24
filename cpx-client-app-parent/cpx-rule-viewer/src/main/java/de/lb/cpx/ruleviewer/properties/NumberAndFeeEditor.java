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

import de.lb.cpx.client.core.model.fx.checked_combobox.CpxCheckComboBox;
import de.lb.cpx.client.core.util.TextLimiter;
import de.lb.cpx.rule.criteria.TypesAndOperationsManager;
import de.lb.cpx.rule.criteria.model.TypesAndOperations.FeeGroups.FeeGroup.FeeType;
import de.lb.cpx.rule.element.model.Rule;
import de.lb.cpx.ruleviewer.event.RuleChangedEvent;
import de.lb.cpx.shared.lang.Lang;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanProperty;
import org.controlsfx.property.editor.PropertyEditor;

/**
 * Combinted editor to edit feegroup and number in rule
 *
 * @author wilde
 */
public class NumberAndFeeEditor implements PropertyEditor<String> {

    private TextField tfNumber;
    private Rule rule;
    private final PropertySheet.Item item;
    private CpxCheckComboBox<FeeType> chkFeeType;

    protected static final String FEE_GROUP = "hos_normal_fee";

    public NumberAndFeeEditor(PropertySheet.Item property) {
        item = property;
    }

    @Override
    public Node getEditor() {
        if (tfNumber == null) {
            tfNumber = new TextField();
            tfNumber.setMaxWidth(Double.MAX_VALUE);
            tfNumber.setMinWidth(70);
            tfNumber.setPrefWidth(150);
            new TextLimiter(50).setTextInputControl(tfNumber).buildAndApply();
//            tfNumber.setStyle("-fx-font-size:14px;");
            tfNumber.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                    getRule().setNumber(tfNumber.getText());
                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                    Event.fireEvent(tfNumber, saveEvent);
                }
            });
            tfNumber.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    if (!t1) {
                        getRule().setNumber(tfNumber.getText());
                        RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                        Event.fireEvent(tfNumber, saveEvent);
                    }
                }
            });
        }
        if (chkFeeType == null) {
            chkFeeType = new CpxCheckComboBox<>();
            chkFeeType.setMaxWidth(Double.MAX_VALUE);
            chkFeeType.setMinWidth(70);
            chkFeeType.setPrefWidth(150);
            chkFeeType.getItems().addAll(TypesAndOperationsManager.instance().getFeeTypes(FEE_GROUP));
            chkFeeType.setConverter(new StringConverter<FeeType>() {
                @Override
                public String toString(FeeType t) {
                    return t != null ? t.getName() : "";
                }

                @Override
                public FeeType fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            chkFeeType.getCheckModel().getCheckedItems().addListener(new ListChangeListener<FeeType>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends FeeType> change) {
                    String types = chkFeeType.getCheckModel().getCheckedItems().stream().map((item) -> {
                        return String.valueOf(item.getIdent());
                    }).collect(Collectors.joining(","));
                    getRule().setFeegroup(types);
                    RuleChangedEvent saveEvent = new RuleChangedEvent(RuleChangedEvent.ruleChangedEvent(), this);
                    Event.fireEvent(chkFeeType, saveEvent);
                }
            });
        }
        Label lbl = new Label("Entgeltgruppe:");
        lbl.setTooltip(new Tooltip(lbl.getText()));
        lbl.setPadding(new Insets(0, 10, 0, 10));
        lbl.setPrefWidth(150);
//        lbl.setMaxWidth(La);
        lbl.setMinWidth(50);
        lbl.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(lbl, Priority.ALWAYS);
        HBox box = new HBox(tfNumber, lbl, chkFeeType);
        HBox.setHgrow(tfNumber, Priority.ALWAYS);
        HBox.setHgrow(chkFeeType, Priority.ALWAYS);
//        box.setMinWidth(Label.USE_PREF_SIZE);
//        box.setMaxWidth(Double.MAX_VALUE);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setFillHeight(true);
        box.disabledProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                LOG.info("change disable Property");
                tfNumber.setDisable(t1);
                chkFeeType.setDisable(t1);
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

    @Override
    public void setValue(String value) {
        getEditor();
        tfNumber.setText(getRule().getNumber());
        for (String ident : getRule().getFeegroup().split(",")) {
            FeeType type = TypesAndOperationsManager.instance().getFeeType(FEE_GROUP, ident);
            if (type != null) {
                chkFeeType.getCheckModel().check(type);
            }
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
