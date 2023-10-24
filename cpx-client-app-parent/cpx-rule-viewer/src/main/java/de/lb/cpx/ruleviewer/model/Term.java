/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.model;

import de.lb.cpx.client.core.util.code.CodeExtraction;
import de.lb.cpx.rule.criteria.CriteriaManager;
import de.lb.cpx.rule.criteria.TypesAndOperationsManager;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion;
import de.lb.cpx.rule.criteria.model.TypesAndOperations;
import de.lb.cpx.rule.criteria.model.TypesAndOperations.OperationGroups.OperationGroup.Operation;
import de.lb.cpx.rule.element.model.RulesValue;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.ruleviewer.event.RuleChangingEvent;
import de.lb.cpx.ruleviewer.layout.RuleView;
import de.lb.cpx.ruleviewer.properties.BasicIntervallEditor;
import de.lb.cpx.ruleviewer.properties.EndIntervalEditor;
import de.lb.cpx.ruleviewer.properties.PropertyOperatorEditor;
import de.lb.cpx.ruleviewer.properties.SettingsDescriptor;
import de.lb.cpx.ruleviewer.properties.StartIntervalEditor;
import de.lb.cpx.ruleviewer.properties.TermCriteriaEditor;
import de.lb.cpx.ruleviewer.properties.TermValueEditor;
import de.lb.cpx.ruleviewer.skin.TermSkin;
import de.lb.cpx.shared.json.RuleMessage;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class Term extends SelectableControl {

    private static final String DEFAULT_STYLE_CLASS = "term";
    private static final Logger LOG = Logger.getLogger(Term.class.getName());
    private static final String RULE_TABLE_SYMBOL = "@";
    private PropertyDescriptor[] propDescriptors;
    private Callback<Void, Boolean> wrapCallback;
    private Callback<Void, Boolean> unWrapCallback;
    private ObjectProperty<RulesValue> rulesValueProperty;
    private ObjectProperty<Criterion> firstConditionProperty;// = new SimpleStringProperty("----");
    private StringProperty secondConditionProperty;// = new SimpleStringProperty("----");
    private ObjectProperty<Operation> operatorProperty;// = new SimpleObjectProperty<>(OperatorEn.EQUALS);
    private ObjectProperty<String> intervalFromProperty;// = new SimpleObjectProperty<>(OperatorEn.EQUALS);
    private ObjectProperty<String> intervalToProperty;// = new SimpleObjectProperty<>(OperatorEn.EQUALS);
    private BooleanProperty invertedProperty;// = new SimpleBooleanProperty(false);
    private ObjectProperty<RuleMessage> messageProperty;
    private final StringProperty placeholderTextProperty = new SimpleStringProperty("Link platzierbar");

    public Term() {
        super();
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        rulesValueProperty().addListener((ObservableValue<? extends RulesValue> observable, RulesValue oldValue, RulesValue newValue) -> {
            updateRuleValues(newValue);
        });

    }

    private void updateRuleValues(RulesValue newValue) {
        if (newValue == null) {
            return;
        }
        LOG.finest("update rules value!");
        long start = System.currentTimeMillis();
        setInverted(Boolean.valueOf(newValue.getNot()));
        LOG.finest("set inverted in " + (System.currentTimeMillis() - start));
        setFirstCondition(CriteriaManager.instance().getFirstCriterionFromMap(newValue.getKriterium()));
        LOG.finest("set first con in " + (System.currentTimeMillis() - start));
        TypesAndOperations.OperationGroups.OperationGroup.Operation op = TypesAndOperationsManager.instance().getOperator(getFirstCondition(), newValue.getOperator());
        LOG.finest("fetch op in " + (System.currentTimeMillis() - start));
        setOperator(op);
        LOG.finest("set op in " + (System.currentTimeMillis() - start));
        setSecondCondition(newValue.getWert());
        LOG.finest("set second con in " + (System.currentTimeMillis() - start));
        LOG.finer("update RuleValue in " + (System.currentTimeMillis() - start) + " ms");
    }

    public void setWrapCallback(Callback<Void, Boolean> pCallback) {
        wrapCallback = pCallback;
    }

    public Callback<Void, Boolean> getWrapCallback() {
        return wrapCallback;
    }

    public void setUnWrapCallback(Callback<Void, Boolean> pCallback) {
        unWrapCallback = pCallback;
    }

    public Callback<Void, Boolean> getUnWrapCallback() {
        return unWrapCallback;
    }

    @Override
    public ContextMenu populateContextMenu() {
        if (isReadOnly()) {
            return new ContextMenu();
        }
        ContextMenu menu = super.populateContextMenu();
        MenuItem itemWrap = new MenuItem("Klammern");
        itemWrap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (wrapCallback != null) {
                    wrapCallback.call(null);
                }
            }
        });
        final MenuItem itemUnWrap = new MenuItem("Entklammern");
        itemUnWrap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (unWrapCallback != null) {
                    unWrapCallback.call(null);
                }
            }
        });
        menu.getItems().add(0, itemUnWrap);
        menu.getItems().add(0, itemWrap);
        menu.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (getParentElement().getParentElement() == null) {
                    itemUnWrap.setDisable(true);
                } else {
                    itemUnWrap.setDisable(false);
                }
            }
        });
        return menu;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new TermSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(Term.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin();
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
//        if (ViewMode.READ_ONLY.equals(getViewMode())) {
//            return new PropertyDescriptor[0];
//        }
        if (propDescriptors == null) {
            propDescriptors = new PropertyDescriptor[6];
            try {
                propDescriptors[0] = new PropertyDescriptor("invertedProperty", Term.class, "isInverted", "setInverted");
                propDescriptors[0].setDisplayName("Verneint:");
                propDescriptors[0].setPreferred(true);

                propDescriptors[1] = new PropertyDescriptor("firstConditionProperty", Term.class, "getFirstCondition", "setFirstCondition");
                propDescriptors[1].setDisplayName("Kriterium:");
                propDescriptors[1].setPreferred(true);
                propDescriptors[1].setPropertyEditorClass(TermCriteriaEditor.class);

                propDescriptors[2] = new SettingsDescriptor("operatorProperty", Term.class, "getOperator", "setOperator");
                propDescriptors[2].setPropertyEditorClass(PropertyOperatorEditor.class);
                propDescriptors[2].setDisplayName("Operator:");
                propDescriptors[2].setPreferred(true);

                propDescriptors[3] = new SettingsDescriptor("secondConditionProperty", Term.class, "getSecondCondition", "setSecondCondition");
                propDescriptors[3].setPropertyEditorClass(TermValueEditor.class);
                propDescriptors[3].setDisplayName("Wert:");
                propDescriptors[3].setPreferred(true);

                propDescriptors[4] = new SettingsDescriptor("intervalFromProperty", Term.class, "getIntervalFrom", "setIntervalFrom");
                propDescriptors[4].setPropertyEditorClass(StartIntervalEditor.class);
                propDescriptors[4].setDisplayName("Intervall-Start:");
                propDescriptors[4].setPreferred(true);

                propDescriptors[5] = new SettingsDescriptor("intervalToProperty", Term.class, "getIntervalTo", "setIntervalTo");
                propDescriptors[5].setPropertyEditorClass(EndIntervalEditor.class);
                propDescriptors[5].setDisplayName("Intervall-Ziel:");
                propDescriptors[5].setPreferred(true);

            } catch (IntrospectionException ex) {
                LOG.log(Level.WARNING, ex.toString(), ex);
            }
        }
        return propDescriptors.clone();
    }

    @Override
    public String getDisplayName() {
        return "Term";
    }

    @Override
    public String getDisplayText() {
        return getDisplayName() + ": " + toString();
//                + (isInverted() ? "not" : "")
//                + " (" + (getFirstCondition() != null ? getCriteriaTranslation(getFirstCondition().getCpname()) : "----")
//                + (" " + (getOperator() != null ? getOperator().toString() : " ")
//                + " " + (getSecondCondition() != null ? getSecondCondition() : " ")) + ")";
    }

    @Override
    public String toString() {
        return (isInverted() ? "not" : "") + " "
                //                + "("
                + (isInverted() ? "(" : "")
                + (getFirstCondition() != null ? getCriteriaTranslation(getFirstCondition().getCpname()) : "----")
                + (" " + (getOperator() != null ? getOperator().toString() : " ")
                + " " + (getSecondCondition() != null ? (isDouble() ? getSecondContitionText().replace(".", ",") : getSecondContitionText().replace("%", "*")) : " "))
                + (isInverted() ? ")" : "") //                + ")"
                ;
//        return getDisplayName();
    }

    @Override
    public void refresh() {
        super.refresh();
        getProperties().put(SelectableControl.REFRESH_VALUE, null);
    }

    public final ObjectProperty<RulesValue> rulesValueProperty() {
        if (rulesValueProperty == null) {
            rulesValueProperty = new SimpleObjectProperty<>(new RulesValue());
        }
        return rulesValueProperty;
    }

    public void setRulesValue(RulesValue pValue) {
        rulesValueProperty().set(pValue);
    }

    public RulesValue getRulesValue() {
        return rulesValueProperty().get();
    }

    public Criterion getFirstCondition() {
        return firstConditionProperty().get();
    }

    public void setFirstCondition(Criterion firstCondition) {
        firstConditionProperty().set(firstCondition);
    }

    public ObjectProperty<Criterion> firstConditionProperty() {
        if (firstConditionProperty == null) {
            long start = System.currentTimeMillis();
            firstConditionProperty = new SimpleObjectProperty<>();
            if (getRulesValue() != null) {
                Criterion defaultData = CriteriaManager.instance().getFirstCriterionFromMap(getRulesValue().getKriterium());
                LOG.finest("load default data in property " + (System.currentTimeMillis() - start));
                firstConditionProperty.set(defaultData);
            }
            firstConditionProperty.addListener(new ChangeListener<Criterion>() {
                @Override
                public void changed(ObservableValue<? extends Criterion> observable, Criterion oldValue, Criterion newValue) {
                    updateRule();
                }
            });
            LOG.finer("create first condition property " + (System.currentTimeMillis() - start));
        }
        return firstConditionProperty;
    }

    public StringProperty secondConditionProperty() {
        if (secondConditionProperty == null) {
            secondConditionProperty = new SimpleStringProperty();
            if (getRulesValue() != null) {
                secondConditionProperty.set(getRulesValue().getWert());
            }
            secondConditionProperty.addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                    getProperties().put(UPDATE_CONTROL, null);
                    updateRule();
                }
            });
        }
        return secondConditionProperty;
    }

    public String getSecondCondition() {
        return secondConditionProperty().get();
    }

    public void setSecondCondition(String secondCondition) {
        secondConditionProperty().set(secondCondition);
    }

    public ObjectProperty<Operation> operatorProperty() {
        if (operatorProperty == null) {
            operatorProperty = new SimpleObjectProperty<>();
            if (getRulesValue() != null) {
                operatorProperty.set(TypesAndOperationsManager.instance().getOperator(getFirstCondition(), getRulesValue().getOperator()));
            }
            operatorProperty.addListener(new ChangeListener<Operation>() {
                @Override
                public void changed(ObservableValue<? extends Operation> observable, Operation oldValue, Operation newValue) {
//                    getProperties().put(UPDATE_CONTROL, null);
                    updateRule();
                }
            });
        }
        return operatorProperty;
    }

    public Operation getOperator() {
        return operatorProperty().get();
    }

    public void setOperator(Operation operator) {
        operatorProperty().set(operator);
    }

    public ObjectProperty<String> intervalFromProperty() {
        if (intervalFromProperty == null) {
            intervalFromProperty = new SimpleObjectProperty<>("");
            if (getRulesValue() != null) {
                intervalFromProperty.set(getRulesValue().getIntervalFrom() != null ? getRulesValue().getIntervalFrom() : "");
            }
            intervalFromProperty.addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                    getProperties().put(UPDATE_CONTROL, null);
                    updateRule();
                }
            });
        }
        return intervalFromProperty;
    }

    public String getIntervalFrom() {
        return intervalFromProperty().get();
    }

    public void setIntervalFrom(String pFrom) {
        intervalFromProperty().set(pFrom != null ? pFrom : "");
    }

    public ObjectProperty<String> intervalToProperty() {
        if (intervalToProperty == null) {
            intervalToProperty = new SimpleObjectProperty<>("");
            if (getRulesValue() != null) {
                intervalToProperty.set(getRulesValue().getIntervalTo() != null ? getRulesValue().getIntervalTo() : "");
            }
            intervalToProperty.addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                    getProperties().put(UPDATE_CONTROL, null);
                    updateRule();
                }
            });
        }
        return intervalToProperty;
    }

    public String getIntervalTo() {
        return intervalToProperty().get();
    }

    public void setIntervalTo(String pTo) {
        intervalToProperty().set(pTo != null ? pTo : "");
    }

    public BooleanProperty invertedProperty() {
        if (invertedProperty == null) {
            invertedProperty = new SimpleBooleanProperty();
            if (getRulesValue() != null) {
                invertedProperty.set(Boolean.parseBoolean(getRulesValue().getNot()));
            }
            invertedProperty.addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    getProperties().put(UPDATE_CONTROL, null);
                    updateRule();
                }
            });
        }
        return invertedProperty;
    }

    public boolean isInverted() {
        return invertedProperty().get();
    }

    public void setInverted(boolean inverted) {
        invertedProperty().set(inverted);
    }

    public StringProperty placeholderTextProperty() {
        return placeholderTextProperty;
    }

    public String getPlaceholderText() {
        return placeholderTextProperty().get();
    }

    public void setPlaceholderText(String placeholderText) {
        placeholderTextProperty().set(placeholderText);
    }

    /**
     * force update to rule values
     */
    public void updateRule() {
        RuleChangingEvent event = new RuleChangingEvent(RuleChangingEvent.ANY, "Term geändert");
        Event.fireEvent(this, event);
        getRulesValue().setOperator(getOperator() != null ? getOperator().getName() : "");
        getRulesValue().setKriterium(getFirstCondition() != null ? getFirstCondition().getCpname() : "");
        getRulesValue().setWert(getSecondCondition() != null ? getSecondCondition() : "");
        getRulesValue().setNot(String.valueOf(isInverted()));
        getRulesValue().setIntervalFrom(getIntervalFrom() != null ? getIntervalFrom() : "");
        getRulesValue().setIntervalTo(getIntervalTo() != null ? getIntervalTo() : "");
        getRulesValue().setHasinterval(String.valueOf(hasInterval()));
//        RuleChangedEvent event2 = new RuleChangedEvent();
//        Event.fireEvent(this, event2);
        getProperties().put(UPDATE_CONTROL, null);
    }

    public String getCriteriaTranslation(String critName) {
        if (critName == null) {
            return "";
        }
        if (critName.isEmpty()) {
            return "";
        }
        return critName;//Lang.get(critName).getValue();
    }

    public Boolean hasInterval() {
        if(getIntervalFrom().equals(BasicIntervallEditor.INTERVAL_NOTHING) && getIntervalTo().equals(BasicIntervallEditor.INTERVAL_NOTHING)){
            getRulesValue().setIntervalFrom( "");
            getRulesValue().setIntervalTo( "");

            return false;
        }
        if (!getIntervalFrom().isEmpty() || !getIntervalTo().isEmpty()) {
            return true;
        }
        return false;
    }

    private String getSecondContitionText() {
        if (isRuleTable()) {
            String tableName = RuleMetaDataCache.instance().getTableNameInPool(RuleView.getFacade().getPool(), getSecondCondition());
            return tableName != null ? tableName : "";
        }
        return getSecondCondition();
    }

    public boolean isRuleTable() {
        if (getOperator() != null) {
            if (getOperator().getName().contains(RULE_TABLE_SYMBOL)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkOps(String pCode) {
        return !CodeExtraction.getOpsCode(pCode).isEmpty();
    }

    public boolean checkIcd(String pCode) {
        return !CodeExtraction.getIcdCode(pCode).isEmpty();
    }

    public boolean isOps() {
        if (getSecondCondition() != null) {
            return !CodeExtraction.getOpsCode(getSecondCondition().replace("'", "")).isEmpty();
        }
        return false;
    }

    public boolean isIcd() {
        if (getSecondCondition() != null) {
            return !CodeExtraction.getIcdCode(getSecondCondition().replace("'", "")).isEmpty();
        }
        return false;
    }

    public boolean isUnknown() {
        if (isIcd()) {
            return false;
        }
        if (isOps()) {
            return false;
        }
        if (isRuleTable()) {
            return false;
        }
        return true;
    }

    public boolean isDouble() {
        String value = Objects.requireNonNullElse(getSecondCondition(),"");
        return value.matches("-?\\d+(\\.\\d+)?");
    }
    
    public ObjectProperty<RuleMessage> messageProperty(){
        if(messageProperty == null){
            messageProperty = new SimpleObjectProperty<>();
        }
        return messageProperty;
    }
    
    public void setMessage(RuleMessage pMessage){
        messageProperty().set(pMessage);
    }
    
    public RuleMessage getMessage(){
        return messageProperty().get();
    }
    
    public boolean hasMessage(){
        return getMessage() != null;
    }

}
