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
package de.lb.cpx.ruleviewer.model.control;

import de.lb.cpx.ruleviewer.model.ruletable.content.model.Item;
import de.lb.cpx.shared.json.RuleMessage;
import de.lb.cpx.shared.json.enums.MessageReasonEn;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class ListValueEditor extends EditorControl{

    private static final Logger LOG = Logger.getLogger(ListValueEditor.class.getName());
    protected static final String REFRESH_SEVERITY = "refreshSeverity";
    
    public ListValueEditor(){
        super();
        ruleMessageProperty().addListener(new ChangeListener<RuleMessage>() {
            @Override
            public void changed(ObservableValue<? extends RuleMessage> ov, RuleMessage t, RuleMessage t1) {
                validationMap.clear();
                if(t1 == null || !hasValidationMessage()){
                    refreshSeverity();
                    return;
                }
                String[] split = getCodesFromMessage(t1).split(",");
                for(String key : split){
                    key = key.trim();
                    if(key.isEmpty()){
                        continue;
                    }
                    validationMap.put(key.replace("*", "%"), null);
                }
                refreshSeverity();
            }
        });
               
    }
    public void refreshSeverity(){
        getProperties().put(REFRESH_SEVERITY, null);
    }
    private String getCodesFromMessage(RuleMessage pMessage){
        return Objects.requireNonNullElse(pMessage.getCodes(),"");
//        return getCodesFromDescription_TEST(pMessage.getDescription());
    }
    private String getCodesFromDescription_TEST(String pDescription){
        pDescription = Objects.requireNonNullElse(pDescription, "");
        pDescription = pDescription.replace("Die Werte", "");
        pDescription = pDescription.replace(String.valueOf(getRuleMessage().getDestYear()), "");
        pDescription = pDescription.replace("für das Jahr  müssen angepasst werden", "");
        return pDescription;
    }
    @Override
    protected Skin<?> createDefaultSkin() {
        return new ListValueEditorSkin(this);
    }
    private ObservableList<String> values;
    public ObservableList<String> getValues(){
        if(values == null){
            values = FXCollections.observableArrayList();
        }
        return values;
    }
    
    public void setValuesAsString(String pValues){
        pValues = Objects.requireNonNullElse(pValues, "");
        if(pValues.isEmpty()){
            getValues().clear();
            return;
        }
//        pValues = pValues.concat(pValues);
//        pValues = pValues.concat(pValues);
        getValues().setAll(Arrays.asList(pValues.split(",")).stream()
//                .map((t) -> {
//                    return t.trim();
//                })
                .collect(Collectors.toList()));
    }
    public String getValuesAsString(){
        return values.stream().collect(Collectors.joining(","));
    }
    
    private final Callback<String,Void> DEFAULT_UPDATE_CALLBACK = new Callback<String, Void>() {
        @Override
        public Void call(String p) {
            p = Objects.requireNonNullElse(p, "");
            if(p.equals(getValuesAsString())){
                return null;
            }
            LOG.log(Level.INFO, "Update Values to: {0}", p);
            setValuesAsString(p);
            return null;
        }
    };
    private Callback<String,Void> updateCallback = DEFAULT_UPDATE_CALLBACK;
    public void setUpdateCallback(Callback<String,Void> pCallback){
        updateCallback = Objects.requireNonNullElse(pCallback, DEFAULT_UPDATE_CALLBACK);
    }
    public Callback<String,Void> getUpdateCallback(){
        return updateCallback;
    }
    
     private final Callback<String,Item> DEFAULT_ITEM_FACTORY = new Callback<String, Item>() {
        @Override
        public Item call(String p) {
            LOG.log(Level.INFO, "create default item for: {0}", p);
            return new Item(p, Boolean.TRUE);
        }
    };
    private Callback<String,Item> itemFactory = DEFAULT_ITEM_FACTORY;
    public void setItemFactory(Callback<String,Item> pCallback){
        itemFactory = Objects.requireNonNullElse(pCallback, DEFAULT_ITEM_FACTORY);
    }
    public Callback<String,Item> getItemFactory(){
        return itemFactory;
    }
    
    private final Callback<String,String> DEFAULT_VALIDATION_CALLBACK = (String p) -> null;
    private Callback<String,String> validationCallback = DEFAULT_VALIDATION_CALLBACK;
    public void setValidationCallback(Callback<String,String> pCallback){
        validationCallback = Objects.requireNonNullElse(pCallback, DEFAULT_VALIDATION_CALLBACK);
    }
    public Callback<String,String> getValidationCallback(){
        return validationCallback;
    }
    private final Map<String,String> validationMap = new HashMap<>();
    
    public String getValidationResult(String pCode){
        pCode = pCode.replace("*", "%");
        String key = validationMapGetKey(pCode);
        if(key == null){
            return null;
        }
        if(validationMap.get(key)==null){
            validationMap.put(key, getValidationCallback().call(pCode));
        }
        return validationMap.get(key);
    }
    
    public boolean hasValidationMessage(){
        return getRuleMessage() != null && !getRuleMessage().getDescription().isEmpty();
    }
    public String validationMapGetKey(String pCode){
        if(pCode == null){
            return null;
        }
        Iterator<String> it = validationMap.keySet().iterator();
        while (it.hasNext()) {
            String next = it.next();
            String key = next;
            if(key.contains(":")){
                key = key.split(":")[0].trim();
            }
            if(key.equalsIgnoreCase(pCode)){
                return next;
            }
        }
        return null;
    }
    public boolean valdationMapContainsCode(String pCode){
        return validationMapGetKey(pCode)!=null;
    }
    public MessageReasonEn getMessageReasonForCode(String pCode){
        pCode = pCode.trim().replace("*", "%");
        String key = validationMapGetKey(pCode);
        if(key == null){
            return null;
        }
        if (key.contains(pCode)) {
            String[] split = key.split(":");
            if (split.length > 1) {
                return MessageReasonEn.valueOfIndex(split[1].trim());
            }
        }
        return null;
    }
    
}
