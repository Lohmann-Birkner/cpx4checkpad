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
 *    2018  Your Organisation - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.model;

import de.lb.cpx.rule.criteria.CriteriaManager;
import de.lb.cpx.rule.criteria.TypesAndOperationsManager;
import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion;
import de.lb.cpx.rule.criteria.model.TypesAndOperations.OperationGroups.OperationGroup.Operation;
import de.lb.cpx.rule.criteria.model.TypesAndOperations.SuggActions.SuggAction;
import de.lb.cpx.rule.element.model.Sugg;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.ruleviewer.layout.RuleView;
import static de.lb.cpx.ruleviewer.model.SelectableControl.UPDATE_CONTROL;
import de.lb.cpx.ruleviewer.properties.SettingsDescriptor;
import de.lb.cpx.ruleviewer.properties.SuggestionActionTypeEditor;
import de.lb.cpx.ruleviewer.properties.SuggestionCondValueEditor;
import de.lb.cpx.ruleviewer.properties.SuggestionConditionOperatorEditor;
import de.lb.cpx.ruleviewer.properties.SuggestionCritValueEditor;
import de.lb.cpx.ruleviewer.properties.SuggestionCriteriaEditor;
import de.lb.cpx.ruleviewer.properties.SuggestionCriteriaOperatorEditor;
import de.lb.cpx.ruleviewer.skin.SuggestionSkin;
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
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 * Suggestion (Vorschlag) Control for edit and diesplay a suggestion
 *
 * @author wilde
 */
public class Suggestion extends SelectableControl {

    private static final Logger LOG = Logger.getLogger(Suggestion.class.getName());
    private static final String DEFAULT_STYLE_CLASS = "suggestion";
    private static final String RULE_TABLE_SYMBOL = "@";
    private PropertyDescriptor[] propDescriptors;
    private ObjectProperty<Sugg> suggestionProperty;
    private ObjectProperty<SuggAction> actionIdProperty;
    private ObjectProperty<Criterion> criterionProperty;// = new SimpleStringProperty("----");
    private ObjectProperty<Operation> criterionOperatorProperty;// = new SimpleObjectProperty<>(OperatorEn.EQUALS);
    private ObjectProperty<Operation> conditionOperatorProperty;// = new SimpleObjectProperty<>(OperatorEn.EQUALS);
    private StringProperty criterionValueProperty;
    private StringProperty conditionValueProperty;
    private BooleanProperty conditionActiveProperty;
    private Callback<Void, Boolean> duplicateCallback;
    private SimpleObjectProperty<RuleMessage> messageProperty;

    public Suggestion() {
        super();
        LOG.finer("create new suggestion");
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        suggestionProperty().addListener(new ChangeListener<Sugg>() {
            @Override
            public void changed(ObservableValue<? extends Sugg> observable, Sugg oldValue, Sugg newValue) {
                updateSuggestion(newValue);
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new SuggestionSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(Suggestion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin();
    }

    private void updateSuggestion(Sugg pSuggestion) {
        if (pSuggestion == null) {
            return;
        }
        Criterion criterion = CriteriaManager.instance().getFirstCriterionFromMap(pSuggestion.getCrit());
        setCriterion(criterion);
        setActionId(TypesAndOperationsManager.instance().getSuggestionActionById(pSuggestion.getActionid()));
        setCriterionOperator(TypesAndOperationsManager.instance().getOperator(criterion, pSuggestion.getOp()));
        setCriterionValue(pSuggestion.getValueAttribute() != null ? pSuggestion.getValueAttribute() : "");
        setConditionOperator(TypesAndOperationsManager.instance().getOperator(criterion, pSuggestion.getConditionOp()));
        setConditionValue(pSuggestion.getConditionValue() != null ? pSuggestion.getConditionValue() : "");
        //active condition area if 
        setConditionActive(((getConditionOperator() != null /*&& !Link.DEFAULT_OPERATION.equals(getConditionOperator())*/) && !getConditionOperator().getName().isEmpty())
                || ((getConditionValue() != null) && !getConditionValue().equals("----") && !getConditionValue().isEmpty()));
    }

    public final ObjectProperty<Sugg> suggestionProperty() {
        if (suggestionProperty == null) {
            suggestionProperty = new SimpleObjectProperty<>();
        }
        return suggestionProperty;
    }

    public void setSuggestion(Sugg pValue) {
        suggestionProperty().set(pValue);
    }

    public Sugg getSuggestion() {
        return suggestionProperty().get();
    }

    public ObjectProperty<SuggAction> actionIdProperty() {
        if (actionIdProperty == null) {
            actionIdProperty = new SimpleObjectProperty<>();
            if (getSuggestion() != null) {
                actionIdProperty.set(TypesAndOperationsManager.instance().getSuggestionActionById(getSuggestion().getActionid()));
            }
            actionIdProperty.addListener(new ChangeListener<SuggAction>() {
                @Override
                public void changed(ObservableValue<? extends SuggAction> observable, SuggAction oldValue, SuggAction newValue) {
                    getSuggestion().setActionid(getActionId() != null ? String.valueOf(getActionId().getIdent()) : "");
                    getProperties().put(UPDATE_CONTROL, null);
                }
            });
        }
        return actionIdProperty;
    }

    public void setActionId(SuggAction pId) {
        actionIdProperty().set(pId);
    }

    public SuggAction getActionId() {
        return actionIdProperty().get();
    }

    public Criterion getCriterion() {
        return criterionProperty().get();
    }

    public void setCriterion(Criterion pCriterion) {
        criterionProperty().set(pCriterion);
    }

    public ObjectProperty<Criterion> criterionProperty() {
        if (criterionProperty == null) {
            criterionProperty = new SimpleObjectProperty<>(null);
            if (getSuggestion() != null) {
                criterionProperty.set(CriteriaManager.instance().getFirstCriterionFromMap(getSuggestion().getCrit()));//findFirstCriterion(getSuggestion().getCrit()));
            }
            criterionProperty.addListener(new ChangeListener<CriterionTree.Supergroup.Group.Criterion>() {
                @Override
                public void changed(ObservableValue<? extends CriterionTree.Supergroup.Group.Criterion> observable, CriterionTree.Supergroup.Group.Criterion oldValue, CriterionTree.Supergroup.Group.Criterion newValue) {
//                    getProperties().put(UPDATE_CONTROL, null);
                    getSuggestion().setCrit(getCriterion() != null ? getCriterion().getCpname() : "");
                    getProperties().put(UPDATE_CONTROL, null);
                }
            });
        }
        return criterionProperty;
    }

    public ObjectProperty<Operation> criterionOperatorProperty() {
        if (criterionOperatorProperty == null) {
            criterionOperatorProperty = new SimpleObjectProperty<>(TypesAndOperationsManager.instance().getTermOperator("&&"));
            if (getSuggestion() != null) {
                criterionOperatorProperty.set(TypesAndOperationsManager.instance().getOperator(getCriterion(), getSuggestion().getOp()));
            }
            criterionOperatorProperty.addListener(new ChangeListener<Operation>() {
                @Override
                public void changed(ObservableValue<? extends Operation> observable, Operation oldValue, Operation newValue) {
//                    getProperties().put(UPDATE_CONTROL, null);

                    getSuggestion().setOp(getCriterionOperator() != null ? getCriterionOperator().getName() : "");
                    getProperties().put(UPDATE_CONTROL, null);
                }
            });
        }
        return criterionOperatorProperty;
    }

    public Operation getCriterionOperator() {
        return criterionOperatorProperty().get();
    }

    public void setCriterionOperator(Operation operator) {
        criterionOperatorProperty().set(operator);
    }

    public ObjectProperty<Operation> conditionOperatorProperty() {
        if (conditionOperatorProperty == null) {
            conditionOperatorProperty = new SimpleObjectProperty<>(TypesAndOperationsManager.instance().getTermOperator("&&"));//Link.DEFAULT_OPERATION);
            if (getSuggestion() != null) {
                conditionOperatorProperty.set(TypesAndOperationsManager.instance().getOperator(getCriterion(), getSuggestion().getConditionOp()));
            }
            conditionOperatorProperty.addListener(new ChangeListener<Operation>() {
                @Override
                public void changed(ObservableValue<? extends Operation> observable, Operation oldValue, Operation newValue) {
//                    getProperties().put(UPDATE_CONTROL, null);
                    if(isEmptyOperator(oldValue) && isEmptyOperator(newValue)){
                        return; // if both empty do nothing
                    }
                    getSuggestion().setConditionOp(getConditionOperator() != null ? getConditionOperator().getName() : "");
                    getProperties().put(UPDATE_CONTROL, null);
                }
            });
        }
        return conditionOperatorProperty;
    }
    public boolean isEmptyOperator(Operation pOperation){
        if(pOperation == null){
            return true;
        }
        return pOperation.getName().isEmpty();
    }
    public Operation getConditionOperator() {
        return conditionOperatorProperty().get();
    }

    public void setConditionOperator(Operation operator) {
        conditionOperatorProperty().set(operator);
    }

    public StringProperty criterionValueProperty() {
        if (criterionValueProperty == null) {
            criterionValueProperty = new SimpleStringProperty("----");
            if (getSuggestion() != null) {
                criterionValueProperty.set(getSuggestion().getValueAttribute());
            }
            criterionValueProperty.addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    getSuggestion().setValueAttribute(getCriterionValue() != null ? getCriterionValue() : "");
                    getProperties().put(UPDATE_CONTROL, null);
                }
            });
        }
        return criterionValueProperty;
    }

    public String getCriterionValue() {
        return criterionValueProperty().get();
    }

    public void setCriterionValue(String pValue) {
        criterionValueProperty().set(pValue);
    }

    public StringProperty conditionValueProperty() {
        if (conditionValueProperty == null) {
            conditionValueProperty = new SimpleStringProperty("----");
            if (getSuggestion() != null) {
                conditionValueProperty.set(getSuggestion().getConditionValue());
            }
            conditionValueProperty.addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    getSuggestion().setConditionValue(getConditionValue() != null ? getConditionValue() : "");
                    getProperties().put(UPDATE_CONTROL, null);
                }
            });
        }
        return conditionValueProperty;
    }

    public String getConditionValue() {
        return conditionValueProperty().get();
    }

    public void setConditionValue(String pValue) {
        conditionValueProperty().set(pValue);
    }

    public BooleanProperty conditionActiveProperty() {
        if (conditionActiveProperty == null) {
            conditionActiveProperty = new SimpleBooleanProperty(false);
            conditionActiveProperty.addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    getProperties().put(UPDATE_CONTROL, null);
                }
            });
        }
        return conditionActiveProperty;
    }

    public void setConditionActive(boolean pActive) {
        conditionActiveProperty().set(pActive);
    }

    public boolean isConditionActive() {
        return conditionActiveProperty().get();
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
//        if (isReadOnly()) {
//            return new PropertyDescriptor[0];
//        }
        if (propDescriptors == null) {
            propDescriptors = new PropertyDescriptor[6];
            try {
                propDescriptors[0] = new PropertyDescriptor("actionIdProperty", Suggestion.class, "getActionId", "setActionId");
                propDescriptors[0].setDisplayName("Aktionsart:");
                propDescriptors[0].setPreferred(true);
                propDescriptors[0].setPropertyEditorClass(SuggestionActionTypeEditor.class);

                propDescriptors[1] = new PropertyDescriptor("criterionProperty", Suggestion.class, "getCriterion", "setCriterion");
                propDescriptors[1].setDisplayName("Kriterium:");
                propDescriptors[1].setPreferred(true);
                propDescriptors[1].setPropertyEditorClass(SuggestionCriteriaEditor.class);

                propDescriptors[2] = new SettingsDescriptor("criterionOperatorProperty", Suggestion.class, "getCriterionOperator", "setCriterionOperator");
                propDescriptors[2].setPropertyEditorClass(SuggestionCriteriaOperatorEditor.class);
                propDescriptors[2].setDisplayName("Kriterium-Operator:");
                propDescriptors[2].setPreferred(true);

                propDescriptors[3] = new SettingsDescriptor("criterionValueProperty", Suggestion.class, "getCriterionValue", "setCriterionValue");
                propDescriptors[3].setPropertyEditorClass(SuggestionCritValueEditor.class);
                propDescriptors[3].setDisplayName("Kriterium-Wert:");
                propDescriptors[3].setPreferred(true);

                propDescriptors[4] = new SettingsDescriptor("conditionOpertatorProperty", Suggestion.class, "getConditionOperator", "setConditionOperator");
                propDescriptors[4].setPropertyEditorClass(SuggestionConditionOperatorEditor.class);
                propDescriptors[4].setDisplayName("Bedingung-Operator:");
                propDescriptors[4].setPreferred(true);

                propDescriptors[5] = new SettingsDescriptor("conditionValueProperty", Suggestion.class, "getConditionValue", "setConditionValue");
                propDescriptors[5].setPropertyEditorClass(SuggestionCondValueEditor.class);
                propDescriptors[5].setDisplayName("Bedingung-Wert:");
                propDescriptors[5].setPreferred(true);

            } catch (IntrospectionException ex) {
                LOG.log(Level.WARNING, ex.toString(), ex);
            }
        }
        return propDescriptors.clone();
    }

    @Override
    public String getDisplayName() {
        return "Vorschlag";
    }
    /*
    private ObjectProperty<Sugg> suggestionProperty;
    private ObjectProperty<SuggAction> actionIdProperty;
    private ObjectProperty<Criterion> criterionProperty;// = new SimpleStringProperty("----");
    private ObjectProperty<Operation> criterionOperatorProperty;// = new SimpleObjectProperty<>(OperatorEn.EQUALS);
    private ObjectProperty<Operation> conditionOperatorProperty;// = new SimpleObjectProperty<>(OperatorEn.EQUALS);
    private StringProperty criterionValueProperty;
    private StringProperty conditionValueProperty;
    private BooleanProperty conditionActiveProperty;
    */
    @Override
    public String toString() {
        return getDisplayText();
//        return (getActionId() != null ? SuggestionSkin.getTranslation(getActionId().getDisplayName()) : " ---- ") + " "
//                + //(getActionId()!= null?Lang.get(getActionId().getDisplayName()).getValue():" ---- ") + " "+
//                (getCriterion() != null ? getCriterion().getCpname()/*Lang.get(getCriterion().getCpname()).getValue()*/ : " ---- ") + " "
//                + (getCriterionOperator() != null ? getCriterionOperator().getFormatedName() : " ---- ") + " "
//                + (isDouble(getCriterionValueText()) ? getCriterionValueText().replace(".", ",") : getCriterionValueText().replace("%", "*"))
//                + (!isConditionActive() ? "" : getConditionText());
    }
    
    @Override
    public ContextMenu populateContextMenu() {
        if (isReadOnly()) {
            return new ContextMenu();
        }
        ContextMenu menu = super.populateContextMenu();
        MenuItem itemDuplicate = new MenuItem("Duplizieren");
        itemDuplicate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (duplicateCallback != null) {
                    duplicateCallback.call(null);
                }
            }
        });
        menu.getItems().add(0, itemDuplicate);
//        menu.setOnShowing(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                if (getParentElement().getParentElement() == null) {
//                    itemUnWrap.setDisable(true);
//                } else {
//                    itemUnWrap.setDisable(false);
//                }
//            }
//        });
        return menu;
    }

    public void setDuplicateCallback(Callback<Void, Boolean> pCallback) {
        duplicateCallback = pCallback;
    }

    public Callback<Void, Boolean> getDuplicateCallback() {
        return duplicateCallback;
    }

    /**
     * @return indicator if conditional values are displayed in suggestion
     */
    public boolean isConditionDisplayed() {
        //simple check if operator is set, if not display nothing
        return (getConditionOperator() != null && !getConditionOperator().getName().isEmpty()) || (getConditionValue()!=null && !getConditionValue().isEmpty());
    }

    @Override
    public String getDisplayText() {
//        return "";
        return (getActionId() != null ? SuggestionSkin.getTranslation(getActionId().getDisplayName()) : " ---- ") + " "
                + //(getActionId()!= null?Lang.get(getActionId().getDisplayName()).getValue():" ---- ") + " "+
                (getCriterion() != null ? getCriterion().getCpname()/*Lang.get(getCriterion().getCpname()).getValue()*/ : " ---- ") + " "
                + (getCriterionOperator() != null ? getCriterionOperator().getFormatedName() : " ---- ") + " "
                + (isDouble(getCriterionValueText()) ? getCriterionValueText().replace(".", ",") : getCriterionValueText().replace("%", "*"))
                + (!isConditionDisplayed() ? "" : getConditionText());
//        return getActionId() !=null?(Lang.get(getActionId().getDisplayName())):" ---- "+
//                getCriterion()!=null?Lang.get(getCriterion().getDisplayName()):" ---- "+
//               (getCriterion()!=null?(Lang.get(getCriterion().getDisplayName())) + " ") +
//               getCriterionOperator()!=null?getCriterionOperator().getFormatedName():" ---- " + " ") +
//               getCriterionValue();
    }

    private String getConditionText() {
        return " (" + SuggestionSkin.CONDITION_TEXT + " "
                + (getCriterion() != null ? getCriterion().getCpname()/*Lang.get(getCriterion().getCpname()).getValue()*/ : " ---- ") + " "
                + (getConditionOperator() != null ? getConditionOperator().getFormatedName() : " ---- ") + " "
                + (Objects.requireNonNullElse(getConditionValueText(),"").replace("%", "*"))
                + ")";
    }

    private String getConditionValueText() {
        if (isRuleTable(getConditionOperator())) {
            return RuleMetaDataCache.instance().getTableNameInPool(RuleView.getFacade().getPool(), getConditionValue());
        }
        return getConditionValue();
    }

    private String getCriterionValueText() {
        if (isRuleTable(getCriterionOperator())) {
            String table = RuleMetaDataCache.instance().getTableNameInPool(RuleView.getFacade().getPool(), getCriterionValue());
            return table != null ? table : "";
        }
        return getCriterionValue() != null ? getCriterionValue() : "";
    }

    public boolean isRuleTable(Operation pOperator) {
        if (pOperator != null) {
            if (pOperator.getName().contains(RULE_TABLE_SYMBOL)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDouble(String pValue) {
        String value = Objects.requireNonNull(pValue, "Value for Double Check can ot be null");
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
    
//    public boolean checkOps(String pCode){
//        return !CodeExtraction.getOpsCode(pCode).isEmpty();
//    }
//    public boolean checkIcd(String pCode){
//        return !CodeExtraction.getIcdCode(pCode).isEmpty();
//    }
//    public boolean isOps(String pCriteriaValue){
//        if(pCriteriaValue!=null){
//            return !CodeExtraction.getOpsCode(pCriteriaValue.replace("'", "")).isEmpty(); 
//        }
//        return false;
//    }
//    public boolean isIcd(String pCriteriaValue){
//        if(pCriteriaValue!=null){
//            return !CodeExtraction.getIcdCode(pCriteriaValue.replace("'", "")).isEmpty(); 
//        }
//        return false;
//    }
//    public boolean isUnknown(String pCriteriaValue){
//        if(isIcd(pCriteriaValue)){
//            return false;
//        }
//        if(isOps(pCriteriaValue)){
//            return false;
//        }
//        if(isRuleTable()){
//            return false;
//        }
//        return true;
//    }
}
