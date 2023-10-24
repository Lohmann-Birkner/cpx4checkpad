/*
 * Copyright (c) 2018 Lohmann & Birkner.
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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.editor;

import com.google.common.collect.Lists;
import de.lb.cpx.rule.element.CaseRuleManager;
import de.lb.cpx.rule.element.model.Rule;
import de.lb.cpx.rule.element.model.RulesElement;
import de.lb.cpx.rule.element.model.RulesValue;
import de.lb.cpx.rule.element.model.Sugg;
import de.lb.cpx.rule.element.model.Suggestions;
import de.lb.cpx.ruleviewer.analyser.RuleAnalyser;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.ruleviewer.editor.state.RuleEditorStateManager;
import de.lb.cpx.ruleviewer.enums.ViewMode;
import de.lb.cpx.ruleviewer.model.SelectableControl;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import javax.validation.constraints.NotNull;

/**
 * Rule editor Control handles ui Input and other layout stuff to edit rule,
 * load rule, and store rule?
 *
 * @author wilde
 */
public class RuleEditor extends Control {

    private static final Logger LOG = Logger.getLogger(RuleEditor.class.getName());

    private static final String DEFAULT_STLYE = "rule-editor-view";
    public static final String REFRESH_RULE_MESSAGE = "refresh-rule-message";
    protected static final String UPDATE_RULE = "update-rule";
    protected static final String REFRESH = "refresh";
    private final RuleEditorStateManager stateManager;

    /**
     * creates new editor
     */
    public RuleEditor() {
        super();
        getStyleClass().add(0, DEFAULT_STLYE);
        stateManager = new RuleEditorStateManager();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new RuleEditorSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(RuleEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin();
    }
    //rule property
    private ObjectProperty<CrgRules> ruleProperty;

    /**
     * @return rule proeprty, stores rule to edit
     */
    public ObjectProperty<CrgRules> ruleProperty() {
        if (ruleProperty == null) {
            ruleProperty = new SimpleObjectProperty<>();
        }
        return ruleProperty;
    }

    /**
     * @return get edited rule
     */
    public CrgRules getRule() {
        return ruleProperty().get();
    }

    /**
     * @param pRule set rule to edit
     */
    public void setRule(CrgRules pRule) {
        ruleProperty().set(pRule);
    }

    /**
     * @return get string representation of the xml rule, string will be encoded
     * in utf-16
     * @throws UnsupportedEncodingException if encoding to rule failed
     */
//    public String getRuleXml() throws UnsupportedEncodingException {
//        //update rule values from editor
//        updateRule();
//        //creates string from stored byte array in object
//        return CaseRuleManager.byteToString(getRule().getCrgrDefinition(), "UTF-16");
//    }
    public String getRuleXml() throws UnsupportedEncodingException {
        byte[] bytes = CaseRuleManager.transformObject(getStateManager().getCurrentStateItem(), "UTF-16");
        return CaseRuleManager.byteToStringUTF16(bytes);
    }

    public Map<String, String> getMapOfRuleTables() {
        Map<String, String> tables = new HashMap<>();
        Rule rule = stateManager.getCurrentStateItem();
        addRuleTableFromElement(tables, rule.getRulesElement());
        addRuleTableFromSuggestion(tables, rule.getSuggestions());
        return tables;
    }

    private void addRuleTableFromElement(Map<String, String> pMap, RulesElement pElement) {
        for (Object item : pElement.getRulesValueOrRulesOperatorOrRulesElement()) {
            if (item instanceof RulesElement) {
                addRuleTableFromElement(pMap, (RulesElement) item);
            }
            if (item instanceof RulesValue) {
                RulesValue val = (RulesValue) item;
                addTableFromRulesValue(pMap, val);
            }
        }
    }

    private void addRuleTableFromSuggestion(Map<String, String> pMap, Suggestions pSuggestion) {
        for (Sugg item : pSuggestion.getSugg()) {
            addTableFromSuggestion(pMap, item);
        }
    }

    private void addTableFromSuggestion(Map<String, String> pMap, Sugg pValue) {
        addTableParameter(pMap, pValue.getOp(), pValue.getValueAttribute());
        addTableParameter(pMap, pValue.getConditionOp(), pValue.getConditionValue());
    }

    private void addTableFromRulesValue(Map<String, String> pMap, RulesValue pValue) {
        addTableParameter(pMap, pValue.getOperator(), pValue.getWert());
    }

    private void addTableParameter(Map<String, String> pMap, String pOperator, String pValue) {
        if (pOperator == null || !pOperator.contains("@")) {
            return;
        }
        if (pMap.containsKey(pValue)) {
            return;
        }
        String content = RuleMetaDataCache.instance().getTableContentForIdInPool(getPool(), pValue);
        pMap.put(pValue, content != null ? content : "");
    }

    public void updateRule() {
        getStateManager().save();
        getProperties().put(UPDATE_RULE, null);
//        try {
//            getRule();
//            CaseRuleManager.byteToString(getRule().getCrgrDefinition(), "UTF-16");
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(RuleEditor.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    //selected control
    private final ReadOnlyObjectWrapper<SelectableControl> selectedControlProperty = new ReadOnlyObjectWrapper<>();

    /**
     * @return property to react to selection change in editor
     */
    public final ReadOnlyObjectProperty<SelectableControl> selectedControlProperty() {
        return selectedControlProperty.getReadOnlyProperty();
    }

    /**
     * @return get currently selected control
     */
    public final SelectableControl getSelectedControl() {
        return selectedControlProperty.get();
    }

    /**
     * @param pControl select control, null will deselect
     */
    public final void selectControl(SelectableControl pControl) {
        selectedControlProperty.set(pControl);
    }

    private ObjectProperty<CrgRulePools> poolProperty;

    public ObjectProperty<CrgRulePools> poolProperty() {
        if (poolProperty == null) {
            poolProperty = new SimpleObjectProperty<>();
        }
        return poolProperty;
    }

    /**
     * @return get edited rule
     */
    public CrgRulePools getPool() {
        return poolProperty().get();
    }

    /**
     * @param pPool set rule to edit
     */
    public void setPool(CrgRulePools pPool) {
        poolProperty().set(pPool);
    }

    private final BooleanProperty fitToHeightProperty = new SimpleBooleanProperty(true);

    public BooleanProperty fitToHeightProperty() {
        return fitToHeightProperty;
    }

    public boolean isFitToHeight() {
        return fitToHeightProperty().get();
    }

    public void setFitToHeight(Boolean pFitToHeight) {
        fitToHeightProperty().set(pFitToHeight);
    }

    public RuleEditorStateManager getStateManager() {
        return stateManager;
    }

    private ObjectProperty<ViewMode> viewModeProperty;

    public ObjectProperty<ViewMode> viewModeProperty() {
        if (viewModeProperty == null) {
            viewModeProperty = new SimpleObjectProperty<>(ViewMode.READ_WRITE);
        }
        return viewModeProperty;
    }

    public ViewMode getViewMode() {
        return viewModeProperty().get();
    }

    public void setViewMode(@NotNull ViewMode pMode) {
        Objects.requireNonNull(pMode, "ViewMode must not be null");
        viewModeProperty().set(pMode);
    }

    public void refresh() {
        getProperties().put(REFRESH, null);
        LOG.info("refresh");
    }

    public boolean isEditable() {
        return ViewMode.READ_WRITE.equals(getViewMode());
    }

    private ObjectProperty<RuleAnalyser> ruleAnalyserProperty;

    public ObjectProperty<RuleAnalyser> ruleAnalyserProperty() {
        if (ruleAnalyserProperty == null) {
            ruleAnalyserProperty = new SimpleObjectProperty<>(new RuleAnalyser());
        }
        return ruleAnalyserProperty;
    }

    public RuleAnalyser getRuleAnalyser() {
        return ruleAnalyserProperty().get();
    }
    private final BooleanProperty showRuleAnalyserProperty = new SimpleBooleanProperty(false);

    protected BooleanProperty showRuleAnalyserProperty() {
        return showRuleAnalyserProperty;
    }

    protected void setShowRuleAnalyser(boolean pShow) {
        showRuleAnalyserProperty().set(pShow);
    }

    public boolean isShowRuleAnalyser() {
        return showRuleAnalyserProperty().get();
    }

    public void updateRole(long pRole) {
        String roles = getStateManager().getCurrentStateItem().getRole();
        ArrayList<String> newRoles = Lists.newArrayList(roles.split(","));
        if (newRoles.contains(String.valueOf(pRole))) {
            newRoles.remove(String.valueOf(pRole));
        } else {
            newRoles.add(String.valueOf(pRole));
        }
        getStateManager().getCurrentStateItem().setRole(newRoles.stream().collect(Collectors.joining(",")));
    }
    
    private ObjectProperty<Callback<CrgRules,byte[]>> validationCallbackProperty;
    public ObjectProperty<Callback<CrgRules,byte[]>> validationCalllbackProperty(){
        if(validationCallbackProperty == null){
            validationCallbackProperty = new SimpleObjectProperty<>();
        }
        return validationCallbackProperty;
    }
    public Callback<CrgRules, byte[]> getValidationCalllback(){
        return validationCalllbackProperty().get();
    }
    public void setValidationCalllback(Callback<CrgRules, byte[]> pCallback){
        validationCalllbackProperty().set(pCallback);
    }
    
    public void refreshRuleMessage(boolean pRevalidate){
        if(getValidationCalllback() == null){
            return;
        }
        long start = System.currentTimeMillis();
        getProperties().put(UPDATE_RULE, null);
        if(pRevalidate){
            getRule().setCrgrMessage(getValidationCalllback().call(getRule()));
            LOG.log(Level.INFO, "get new ValidationResult in {0} ms", System.currentTimeMillis()-start);
            start = System.currentTimeMillis();
        }
        getProperties().put(REFRESH_RULE_MESSAGE, null);
        LOG.log(Level.INFO, "refresh ui for ValidationResult in {0} ms", System.currentTimeMillis()-start);
    }

    public void discardRuleChange() {
        stateManager.revertToOriginalState();
        updateRule();
    }
}
