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
package de.lb.cpx.ruleviewer.properties.risk;

import de.lb.cpx.rule.element.model.Risk;
import de.lb.cpx.rule.element.model.RiskArea;
import de.lb.cpx.rule.element.model.Rule;
import de.lb.cpx.ruleviewer.util.RiskDisplayHelper;
import java.util.Iterator;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javax.validation.constraints.NotNull;

/**
 *
 * @author wilde
 */
public class RiskEditorItem {
    public static final String UPDATE_RISK = "update.risk";
    public static final String CHECK_RISK = "check.risk";
    
    private final ObservableMap<String,Object> properties = FXCollections.observableHashMap();
    private final Rule rule;
    private Risk risk;
    
    public RiskEditorItem(@NotNull Rule pRule) {
        Objects.requireNonNull(pRule, "Rule can not be null");
        rule = pRule;
        risk = rule.getRisks();
        getProperties().addListener(new MapChangeListener<String, Object>() {
            @Override
            public void onChanged(MapChangeListener.Change<? extends String, ? extends Object> change) {
                if (change.wasAdded() && CHECK_RISK.equals(change.getKey())) {
                    rule.setRisks(getRisk().getRiskAreas().isEmpty() ? null : getRisk());
                    getProperties().put(UPDATE_RISK, null);
                    getProperties().remove(CHECK_RISK);
                }
            }
        });
    }

    public final ObservableMap<String, Object> getProperties() {
        return properties;
    }

    public Risk getRisk() {
        if(risk == null){
            risk = new Risk();
        }
        return risk;
    }

    public String getRiskComment(){
        if(!hasAreas()){
            return null;
        }
        return getRisk().getRiskAreas().get(0).getRiskComment();
    }
    
    public String getRiskWasteValue(){
        if(!hasAreas()){
            return null;
        }
        return getRisk().getRiskAreas().get(0).getRiskWastePercentValue();
    }
    public String getRiskAuditValue(){
        if(!hasAreas()){
            return null;
        }
        return getRisk().getRiskAreas().get(0).getRiskAuditPercentValue();
    }
    public String getRiskDefaultWasteValue(){
        if(!hasAreas()){
            return null;
        }
        return getRisk().getRiskAreas().get(0).getRiskDefaultWasteValue();
    }
    
    public String getRiskAreas(){
        if(!hasAreas()){
            return null;
        }
        return RiskDisplayHelper.getTranslatedRiskAreas(getRisk());
    }
    /**
     * methode not in use only to satisfy editor implementation
     * should merge with other implementation to set riskarea
     * in future only one risk could be present?
     * @param pAreas string of areas 
     * @deprecated do not use!
     */
    @Deprecated(forRemoval = true)
    public void setRiskAreas(String pAreas){
        throw new UnsupportedOperationException("not supported");
    }
    
    public void setRiskComment(String pComment){
        if(!hasAreas()){
            return;
        }
        for(RiskArea area : getRisk().getRiskAreas()){
            area.setRiskComment(pComment);
        }
    }
    
    public void setRiskWasteValue(String pValue){
        if(!hasAreas()){
            return;
        }
        for(RiskArea area : getRisk().getRiskAreas()){
            area.setRiskWastePercentValue(pValue);
        }
    }
    public void setRiskAuditValue(String pValue){
        if(!hasAreas()){
            return;
        }
        for(RiskArea area : getRisk().getRiskAreas()){
            area.setRiskAuditPercentValue(pValue);
        }
    }
    public void setRiskDefaultWasteValue(String pWaste){
        if(!hasAreas()){
            return;
        }
        for(RiskArea area : getRisk().getRiskAreas()){
            area.setRiskDefaultWasteValue(pWaste);
        }
    }
    public boolean hasAreas(){
         Risk riskObj = getRisk();
        if(riskObj == null){
            return false;
        }
        if(riskObj.getRiskAreas().isEmpty()){
            return false;
        }
        return true;
    }
    protected void checkEmptyRiskArea() {
        if(getRisk().getRiskAreas().isEmpty()){
            rule.setRisks(null);
        }else{
            if(rule.getRisks() == null){
                rule.setRisks(getRisk());
            }
        }
    }

    protected void removeRiskArea(String name) {
        if(name == null || name.isEmpty()){
            return;
        }
        Risk riskObj = getRisk();
        if(riskObj == null){
            return;
        }
        Iterator<RiskArea> it = riskObj.getRiskAreas().iterator();
        while (it.hasNext()) {
            RiskArea next = it.next();
            if(name.equals(next.getRiskAreaName())){
                it.remove();
                break;
            }
        }
    }

    protected void addRiskArea(String name) {
        if(!hasRiskArea(name)){
            RiskArea newArea = new RiskArea();
            newArea.setRiskAreaName(name);
            newArea.setRiskComment(getRiskComment());
            newArea.setRiskDefaultWasteValue(getRiskDefaultWasteValue());
            newArea.setRiskWastePercentValue(getRiskWasteValue());
            newArea.setRiskAuditPercentValue(getRiskAuditValue());
            getRisk().getRiskAreas().add(newArea);
        }
    }
    protected boolean hasRiskArea(String name){
        if(name == null || name.isEmpty()){
            return false;
        }
        Risk riskObj = getRisk();
        if(riskObj == null){
            return false;
        }
        for(RiskArea area : riskObj.getRiskAreas()){
            if(name.equals(area.getRiskAreaName())){
                return true;
            }
        }
        return false;
    }
    
}
