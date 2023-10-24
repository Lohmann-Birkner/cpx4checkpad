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
package de.lb.cpx.client.ruleeditor.menu.filterlists.tabs;

import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.ruleeditor.menu.filterlists.model.CrgRuleTablesItem;
import de.lb.cpx.ruleviewer.cache.RuleMetaDataCache;
import de.lb.cpx.ruleviewer.enums.ViewMode;
import de.lb.cpx.ruleviewer.model.state.StateManager;
import de.lb.cpx.ruleviewer.util.PoolTypeHelper;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.io.IOException;
import java.util.List;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import de.lb.cpx.ruleviewer.layout.RuleValidatable;

/**
 * Control for the Details of an CrgRuleTables Entity
 *
 * @author wilde
 */
public class RuleTablesDetail extends Control implements RuleValidatable{

    private static final Logger LOG = Logger.getLogger(RuleTablesDetail.class.getName());

    private final StateManager<CrgRuleTables> stateManager;
    private Callback<String,Boolean> onSaveComment;
    
    public RuleTablesDetail(CrgRulePools pPool, CrgRuleTablesItem pItem, List<CrgRules> pRules) {
        super();
        setRuleTables(pItem.getTable());
        setPool(pPool);
        getRules().addAll(pRules);
        stateManager = pItem.getStateManager();
        onSaveComment = new Callback<String, Boolean>() {
            @Override
            public Boolean call(String p) {
                try{
                getRuleTables().setCrgtComment(p);
                return true;
                }catch(Exception ex){
                    LOG.log(Level.SEVERE, "Cannot save rule table content", ex);
                    return false;
                }
            }
        }; 
        commentProperty.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                getRuleTables().setCrgtComment(t1);

            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new RuleTablesDetailSkin(this);
        } catch (IOException ex) {
            Logger.getLogger(RuleTablesDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin(); //To change body of generated methods, choose Tools | Templates.
    }

    private ObjectProperty<CrgRuleTables> ruleTablesProperty;

    public ObjectProperty<CrgRuleTables> ruleTablesProperty() {
        if (ruleTablesProperty == null) {
            ruleTablesProperty = new SimpleObjectProperty<>();
        }
        return ruleTablesProperty;
    }

    public CrgRuleTables getRuleTables() {
        return ruleTablesProperty().get();
    }

    public final void setRuleTables(CrgRuleTables pTable) {
        ruleTablesProperty().set(pTable);
        commentProperty().set(pTable.getCrgtComment());
    }

    private ObjectProperty<CrgRulePools> poolProperty;

    public ObjectProperty<CrgRulePools> poolProperty() {
        if (poolProperty == null) {
            poolProperty = new SimpleObjectProperty<>();
        }
        return poolProperty;
    }

    public final void setPool(CrgRulePools pPool) {
        poolProperty().set(pPool);
    }

    public CrgRulePools getPool() {
        return poolProperty().get();
    }

    private ObservableList<CrgRules> rules = FXCollections.observableArrayList();

    public final ObservableList<CrgRules> getRules() {
        return rules;
    }

    public void setRules(ObservableList<CrgRules> pRules) {
        rules = pRules;
    }

    public byte[] fetchRuleContent(CrgRules rule) {
        byte[] ruleContent = Session.instance().getEjbConnector().connectRuleEditorBean().get().findRuleDefinition(
                rule.getId(),
                getPool().getId(),
                PoolTypeHelper.getPoolType(getPool())
        );
        return ruleContent;
    }

    private ObjectProperty<ViewMode> viewModeProperty;

    public ObjectProperty<ViewMode> viewModeProperty() {
        if (viewModeProperty == null) {
            viewModeProperty = new SimpleObjectProperty<>(ViewMode.READ_ONLY);
        }
        return viewModeProperty;
    }

    public void setViewMode(ViewMode pMode) {
        viewModeProperty().set(pMode);
        editableProperty().set(pMode.equals(ViewMode.READ_WRITE));
    }

    public ViewMode getViewMode() {
        return viewModeProperty().get();
    }

    public void saveRuleTable() {
        if (stateManager != null) {
            stateManager.save();
        }
        Session.instance().getEjbConnector().connectRuleEditorBean().get().updateRuleTable(getPool().getId(), PoolTypeHelper.getPoolType(getPool()), getRuleTables());
        getRuleTables().setCrgtMessage(Session.instance().getEjbConnector().connectRuleEditorBean().get().getRuleTableMessage(getRuleTables().getId(), getPool().getId(), PoolTypeHelper.getPoolType(getPool())));
        RuleMetaDataCache.instance().updateTableInPoolToTableMap(getPool(), getRuleTables());
    }

    private StringProperty commentProperty;
    
    public StringProperty commentProperty(){
        if(commentProperty == null){
            commentProperty = new SimpleStringProperty();
        }
        return commentProperty;
    }
    public void setComment(String pComment){
        commentProperty().set(pComment);
    }
    public String getComment(){
        return commentProperty().get();
    }

    private BooleanProperty editableProperty;
    
     public BooleanProperty editableProperty() {
         if(editableProperty == null){
             editableProperty = new SimpleBooleanProperty();
         }
        return editableProperty;
    }

    public void isEditable(Boolean isEditable) {
        editableProperty.set(isEditable);
    }
    
    public boolean isEditable(){
        return editableProperty.get();
    }

     public Callback<String,Boolean> getOnSaveComment(){
        return onSaveComment;
    }
    
    private ObjectProperty<Callback<String,String>> codeSuggestionCallbackProperty;
    private ObjectProperty<Callback<CrgRuleTables,byte[]>> validationCallbackProperty;
    @Override
    public ObjectProperty<Callback<String, String>> codeSuggestionCallbackProperty() {
        if(codeSuggestionCallbackProperty == null){
            codeSuggestionCallbackProperty  =  new SimpleObjectProperty<>();
        }
        return codeSuggestionCallbackProperty;
    }

    @Override
    public Callback<String, String> getCodeSuggestionCallback() {
        return codeSuggestionCallbackProperty().get();
    }

    @Override
    public void setCodeSuggestionCallback(Callback<String, String> pCallback) {
        codeSuggestionCallbackProperty().set(pCallback);
    }

    @Override
    public ObjectProperty<Callback<CrgRuleTables, byte[]>> validationCalllbackProperty() {
        if(validationCallbackProperty == null){
            validationCallbackProperty = new SimpleObjectProperty<>();
        }
        return validationCallbackProperty;
    }

    @Override
    public Callback<CrgRuleTables, byte[]> getValidationCalllback() {
        return validationCalllbackProperty().get();
    }

    @Override
    public void setValidationCalllback(Callback<CrgRuleTables, byte[]> pCallback) {
        validationCalllbackProperty().set(pCallback);
    }
}
