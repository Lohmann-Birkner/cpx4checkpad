/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.model.enums.VersionRiskTypeEn;
import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.shared.dto.ReadOnlyRequestDTO;
import de.lb.cpx.shared.dto.rules.CpxSimpleRisk;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 *
 * @author gerschmann
 */
public class CmRiskDocumentationView extends Control {

    private static final Logger LOG = Logger.getLogger(CmRiskDocumentationView.class.getName());

    private final ObservableList<TWmRiskDetails> processedRisks = FXCollections.observableArrayList();

    private final ObservableList<TWmRiskDetails> suggestedRisks = FXCollections.observableArrayList();
    
    private final ObservableList<RiskAreaEn> editableRiskAreas = FXCollections.observableArrayList();

    private final ObservableList<CpxSimpleRuleDTO> rules = FXCollections.observableArrayList();
    
    private final Map<TWmRiskDetails, CpxSimpleRuleDTO> risk2rule = new HashMap<>();
    
    private final Map<RiskAreaEn, CpxSimpleRuleDTO> area2rule = new HashMap<>();
    
    public CmRiskDocumentationView() {
        //no-args constructor is needed for SceneBuilder

    }

    @Override
    protected Skin<?> createDefaultSkin() {
        try {
            return new CmRiskDocumentationViewSkin(this);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return super.createDefaultSkin();
    }

    public void setProcessedItems(List<TWmRiskDetails> risks4Case) {
        if(risks4Case != null){
            Collections.sort(risks4Case);
        }
//        risks4Case = mergeProcessedWithSuggestedItems(risks4Case);
        processedRisks.setAll(risks4Case);

    }
    
    
    public void mergeProcessedWithSuggestedItems() {

        if(processedRisks == null || processedRisks.isEmpty() || getSuggestedItems() == null || getSuggestedItems().isEmpty()){
            return ;
        }
        for(TWmRiskDetails risk: processedRisks){
            TWmRiskDetails suggRisk = getRiskItem4Risk(suggestedRisks, risk.getRiskArea());
            if(suggRisk != null){
                if(risk.getRiskCalcPercent() == null || risk.getRiskCalcPercent().intValue() == 0
                        && (risk.getRiskCalcValue()== null || risk.getRiskCalcValue().doubleValue() == 0)
                        && suggRisk.getRiskCalcPercent() != null && suggRisk.getRiskCalcPercent() != 0 
                        && suggRisk.getRiskCalcValue() != null &&suggRisk.getRiskCalcValue().doubleValue() != 0){
                    risk.setRiskCalcPercent(suggRisk.getRiskCalcPercent());
                    risk.setRiskCalcValue(suggRisk.getRiskCalcValue());
                    risk.setRiskAuditPercentSugg(suggRisk.getRiskAuditPercentSugg());
                    risk.setRiskWastePercentSugg(suggRisk.getRiskWastePercentSugg());
                    risk.setRiskPercent(suggRisk.getRiskPercent());
                    risk.setRiskValue(suggRisk.getRiskValue());
                    risk.setRiskAuditPercent(suggRisk.getRiskAuditPercent());
                    risk.setRiskWastePercent(suggRisk.getRiskWastePercent());
                    risk.setRiskBaseFee(suggRisk.getRiskBaseFee());
                    risk.setRiskNotCalculatedFee(suggRisk.getRiskNotCalculatedFee());
//                    if(risk.getRiskBaseFee() == null || risk.getRiskBaseFee() == BigDecimal.ZERO || risk.getRiskBaseFee().doubleValue() == 0.0){
//                        risk.setRiskBaseFee(suggRisk.getRiskBaseFee());
//                    }
                    risk.setRiskComment(suggRisk.getRiskComment());

                }
            }
        }

    }
    private final ObservableList<Node> menuItems = FXCollections.observableArrayList();
    public ObservableList<Node> getMenuItems(){
        return menuItems;
    }
    
    public ObservableList<TWmRiskDetails> getProcessedItems() {
        return processedRisks;
    }

    public void setSuggestedItems(List<TWmRiskDetails> risksFromRules) {
        if(risksFromRules != null){
            Collections.sort(risksFromRules);
        }
        checkResetProbability2VersType(risksFromRules);
        suggestedRisks.setAll(risksFromRules);
        setPrimaryRiskValues4Risk();

    }

    public ObservableList<TWmRiskDetails> getSuggestedItems() {
        return suggestedRisks;
    }

    public TWmRiskDetails getRiskItem4ProcessedRisk(RiskAreaEn value) {
        return getRiskItem4Risk(processedRisks, value);
    }
    
    
    public TWmRiskDetails getRiskItem4SuggestedRisk(RiskAreaEn value) {
        return getRiskItem4Risk(suggestedRisks, value);
    }
    
    public String getPredecessorDescription(VersionRiskTypeEn pVersionType){
        pVersionType = Objects.requireNonNullElse(pVersionType, VersionRiskTypeEn.NOT_SET);
        switch(pVersionType){
            case AUDIT_MD:
            case AUDIT_CASE_DIALOG:
                return "Abrechnungs-Version";
            case CASE_FINALISATION:
                return "Anfrage-Version";
//                return "Aus Abrechnungsversion";
            default:
                return null;
        }
    }
    
    private TWmRiskDetails getRiskItem4Risk(List<TWmRiskDetails> pRiskList, RiskAreaEn pValue) {
        for (TWmRiskDetails risk : pRiskList) {
            if (risk.getRiskArea().equals(pValue)) {
                return risk;
            }
        }
        return null;
    }

    private Callback<TWmRiskDetails, Boolean> onUpdateRisk;

    public void setOnUpdateRisk(Callback<TWmRiskDetails, Boolean> pOnUpdate) {
        onUpdateRisk = pOnUpdate;
    }

    public Callback<TWmRiskDetails, Boolean> getOnUpdateRisk() {
        return onUpdateRisk;
    }

    private Callback<Double, Boolean> onUpdateFullWaste;

    void setOnUpdateFullWaste(Callback<Double, Boolean> onUpdate) {
        onUpdateFullWaste = onUpdate;
    }

    public Callback<Double, Boolean> getOnUpdateFullWaste() {
        return onUpdateFullWaste;
    }
    private Callback<Integer, Boolean> onUpdateFullRisk;

    public Callback<Integer, Boolean> getOnUpdateFullRisk() {
        return onUpdateFullRisk;
    }

    void setOnUpdateFullRisk(Callback<Integer, Boolean> onUpdate) {
        onUpdateFullRisk = onUpdate;
    }

    private Callback<String, Boolean> onUpdateFullRiskComment;

    public Callback<String, Boolean> getOnUpdateFullRiskComment() {
        return onUpdateFullRiskComment;
    }

    void setOnUpdateFullRiskComment(Callback<String, Boolean> onUpdate) {
        onUpdateFullRiskComment = onUpdate;
    }
    
    private Callback<Boolean, Boolean> onUpdateActualRisk;

    public Callback<Boolean, Boolean> getOnUpdateActualRisk() {
        return onUpdateActualRisk;
    }

    void setOnUpdateActualRisk(Callback<Boolean, Boolean> onUpdate) {
        onUpdateActualRisk = onUpdate;
    }
    
    private ObjectProperty<TWmRisk> riskProperty;

    public ObjectProperty<TWmRisk> riskProperty() {
        if (riskProperty == null) {
            riskProperty = new SimpleObjectProperty<>();
        }
        return riskProperty;
    }

    public void setRisk(TWmRisk pRisk) {
        riskProperty().set(pRisk);

    }

    public TWmRisk getRisk() {
        return riskProperty().get();
    }

    /**
     * checked whether this risk is in the processed list
     * @param t
     * @return 
     */
    public boolean isProcessed(TWmRiskDetails t) {
       if(processedRisks == null || processedRisks.isEmpty()){
           return false;
       }
       for(TWmRiskDetails risk: processedRisks){
           if(t.getRiskArea().equals(risk.getRiskArea())){
               return true;
           }
       }
       return false;
    }

    public void createEditableRiskAreas() {

        for(RiskAreaEn riskArea: RiskAreaEn.values()){
            if(
//                    getRiskItem4ProcessedRisk(riskArea) != null   || 
                    getRiskItem4SuggestedRisk(riskArea) == null){
                editableRiskAreas.add(riskArea);
            } 
        }


    }
    
    public ObservableList<RiskAreaEn> getRiskAreas(){
        return editableRiskAreas;
    }
    
//    public void addRiskArea(RiskAreaEn riskArea){
//        editableRiskAreas.add(riskArea);
//    }
//    
//    public void removeRiskArea(RiskAreaEn riskArea){
//        editableRiskAreas.remove(riskArea);
//    }
//    
    public TWmRiskDetails cloneRiskValue(TWmRiskDetails pRisk){
        TWmRiskDetails clone = new TWmRiskDetails();
        clone.setRiskArea(pRisk.getRiskArea());
        clone.setRiskValue(pRisk.getRiskValue());
        clone.setRiskCalcValue(pRisk.getRiskCalcValue());
        clone.setRiskCalcPercent(pRisk.getRiskCalcPercent());
        clone.setRiskPercent(pRisk.getRiskPercent());
        clone.setRiskComment(pRisk.getRiskComment());
        clone.setRiskAuditPercent(pRisk.getRiskAuditPercent());
        clone.setRiskAuditPercentSugg(pRisk.getRiskAuditPercentSugg());
        clone.setRiskWastePercent(pRisk.getRiskWastePercent());
        clone.setRiskWastePercentSugg(pRisk.getRiskWastePercentSugg());
        clone.setRiskSourceSugg(pRisk.getRiskSourceSugg());
        clone.setRiskBaseFee(pRisk.getRiskBaseFee());
        clone.setRiskNotCalculatedFee(pRisk.getRiskNotCalculatedFee());
        clone.setRiskUsedForAuditFl(pRisk.getRiskUsedForAuditFl());
        clone.setRiskUsedForFinalFl(pRisk.getRiskUsedForFinalFl());
        return clone;
    }
    
    private ObjectProperty<TWmRiskDetails> maxProcessedProperty;
    
    
    public ObjectProperty<TWmRiskDetails> maxProcessedProperty(){
        if(maxProcessedProperty == null){
            maxProcessedProperty = new SimpleObjectProperty<TWmRiskDetails>();
        }
        return maxProcessedProperty;
    }
    
    public void setMaxProcessed(TWmRiskDetails pMax){
        maxProcessedProperty().set(pMax);
        getRisk().setRiskValueTotal((pMax == null?(new BigDecimal(0)):pMax.getRiskValue()));
        getRisk().setRiskPercentTotal(pMax == null?0:pMax.getRiskPercent());
//        ((CmRiskDocumentationViewSkin)getSkin()).updateRiskValues(getRisk());
    }
    
    public TWmRiskDetails getMaxProcessed(){
        return maxProcessedProperty().get();
    }

    public void checkMaxValue(List<? extends TWmRiskDetails> pCheckList, boolean added) {
        
        if(pCheckList == null || pCheckList.isEmpty()){
            return;
        }
        // It is single selection, it is only one value
        TWmRiskDetails check = pCheckList.get(0);
        checkMaxValue( check,  added);
        
    }
    public void checkMaxValue(TWmRiskDetails check, boolean added){
        
       if(added){
           // we got the list of added values
           if(getMaxProcessed() == null){
               setMaxProcessed(check);
           }else{
               if(getMaxProcessed().getRiskValue().compareTo(check.getRiskValue()) < 0){
                   setMaxProcessed(check);
               }
           }
       }else{
           // we got the list of removed values. 
           if(getMaxProcessed() == null){
               return;
           }
           if(getMaxProcessed().getRiskArea().equals(check.getRiskArea())){
               // find new max value
               findNewMaxProcessedValue();
//               return;
           }
           
           //AWi, causes strange nullPointer and has no visible effect, i wil remove it!
//           if(getMaxProcessed().getRiskCalcValue().compareTo(check.getRiskValue()) > 0){
//               return;
//           }
           
       }
    }

    private void findNewMaxProcessedValue() {

        setMaxProcessed(findMax(processedRisks));
    }
    
    private TWmRiskDetails findMax(List<TWmRiskDetails> pRiskList){
        if(pRiskList.isEmpty()){
            return null;
        }
        TWmRiskDetails val = pRiskList.get(0);
        for(TWmRiskDetails risk: pRiskList){
            if(val.getRiskValue().compareTo(risk.getRiskValue()) < 0){
                val = risk;
            }
        }
        return val;
    }

    private void setPrimaryRiskValues4Risk() {
       if(!getSuggestedItems().isEmpty()){
           // first suggestion for risk values will be get from suggested risks from rules
           TWmRiskDetails val = findMax(suggestedRisks);
           if(val == null){
               LOG.finer("can not set PrimarRiskValues, no max Risk found!");
               return;
           }
           getRisk().setRiskCalcPercentTotal(val.getRiskPercent());
           getRisk().setRiskCalcValueTotal(val.getRiskValue());
           getRisk().setRiskPercentTotal(getRisk().getRiskPercentTotal() == 0?val.getRiskPercent():getRisk().getRiskPercentTotal());
           getRisk().setRiskValueTotal((getRisk().getRiskValueTotal() == null || getRisk().getRiskValueTotal().doubleValue() == 0)?val.getRiskValue(): getRisk().getRiskValueTotal());

           getRisk().setRiskAuditPercent(getRisk().getRiskAuditPercent() == 0?val.getRiskAuditPercent():getRisk().getRiskAuditPercent());
           getRisk().setRiskAuditPercentSugg(getRisk().getRiskAuditPercentSugg() == 0?val.getRiskAuditPercentSugg():getRisk().getRiskAuditPercentSugg());
           getRisk().setRiskWastePercent(getRisk().getRiskWastePercent() == 0?val.getRiskWastePercent():getRisk().getRiskWastePercent());
           getRisk().setRiskWastePercentSugg(getRisk().getRiskWastePercentSugg() == 0?val.getRiskWastePercentSugg():getRisk().getRiskWastePercentSugg());
           getRisk().setRiskBaseFee((getRisk().getRiskBaseFee() == BigDecimal.ZERO||getRisk().getRiskBaseFee().doubleValue() <= 0.00)?
                   (val.getRiskBaseFee() == null?BigDecimal.ZERO:val.getRiskBaseFee())
                   :getRisk().getRiskBaseFee());
           
           getRisk().setRiskNotCalculatedFee((getRisk().getRiskNotCalculatedFee() == BigDecimal.ZERO
                   ||getRisk().getRiskNotCalculatedFee().doubleValue() <= 0.00)?
                   (val.getRiskNotCalculatedFee() == null?BigDecimal.ZERO:val.getRiskNotCalculatedFee())
                   :getRisk().getRiskNotCalculatedFee());
           setRule4Risk(risk2rule.get(val));
//           if(val != null){
//              ((CmRiskDocumentationViewSkin)getSkin()).addTooltips4RiskFields(rule2val); 
//           }
//           ((CmRiskDocumentationViewSkin)getSkin()).updateRiskValues(getRisk());
      }
    }

    public void setRules(List<CpxSimpleRuleDTO> rules4Risks) {
        rules.setAll(rules4Risks);
        setSuggestedItems(getRiskDetailsFromRules());
        
    }
    
    public void updateRiskValues(){
        if(((CmRiskDocumentationViewSkin)getSkin()) != null){
            ((CmRiskDocumentationViewSkin)getSkin()).updateRiskValues(getRisk());
        }
    }
    public ObservableList<CpxSimpleRuleDTO> getRules() {
        return rules;
    }

    public List <TWmRiskDetails> getRiskDetailsFromRules(){
            boolean isBilling = getRisk().getRiskPlaceOfReg().equals(PlaceOfRegEn.BEFORE_BILLING);
            VersionRiskTypeEn type = getVersionType();
             Map<RiskAreaEn, TWmRiskDetails> riskMap = new HashMap<>();
//             for(TWmRiskDetails wmRiskDet: riskDetails){
//                 riskMap.put(wmRiskDet.getRiskArea(), wmRiskDet);
//             }
// temporary calculation of risks here, will be moved to server side later
             for(CpxSimpleRuleDTO rule: rules){
                 if(rule.getRisks() == null || rule.getChkFeeSimulDiff() > 0 || rule.getChkFeeSimulDiff() == 0 && !rule.getRisks().doUseDefault() ){
                     continue;
                 }
                 CpxSimpleRisk ruleRisks = rule.getRisks();
                 if(ruleRisks == null){
                     continue;
                 }
                TWmRiskDetails wmRiskDet1 = riskMap.get(ruleRisks.getRiskAreasAsEn());
//                if(wmRiskDet == null){
                TWmRiskDetails   wmRiskDet = new TWmRiskDetails();
                    wmRiskDet.setRiskArea(ruleRisks.getRiskAreasAsEn());
                    wmRiskDet.setRiskValue(BigDecimal.valueOf(0.0));
                    wmRiskDet.setRiskPercent(0);
                    wmRiskDet.setRiskAuditPercent(0);
                    wmRiskDet.setRiskAuditPercentSugg(0);
                    wmRiskDet.setRiskWastePercent(0);
                    wmRiskDet.setRiskWastePercentSugg(0);

//                }
                wmRiskDet.setRiskComment(rule.getRuleNotice());
                double koeff = type.equals(VersionRiskTypeEn.CASE_FINALISATION)?1.0:
                        ((type.equals(VersionRiskTypeEn.AUDIT_MD) || type.equals(VersionRiskTypeEn.AUDIT_CASE_DIALOG))?ruleRisks.getWasteKoeff()
                        :ruleRisks.getForBillingKoeff());
                wmRiskDet.setRiskAuditPercent(isBilling?ruleRisks.getRiskAuditPercentValueAsNumber():100);
                wmRiskDet.setRiskAuditPercentSugg(wmRiskDet.getRiskAuditPercent());
                wmRiskDet.setRiskWastePercent(type.equals(VersionRiskTypeEn.CASE_FINALISATION)?100:
                        ruleRisks.getRiskWastePercentValueAsNumber());
                wmRiskDet.setRiskWastePercentSugg(wmRiskDet.getRiskWastePercent());
                wmRiskDet.setRiskSourceSugg(rule.getRuleId());
                if(rule.getChkFeeSimulDiff() < 0 
                        && wmRiskDet.getRiskValue().doubleValue() < Math.abs(rule.getChkFeeSimulDiff() * koeff )){

                    wmRiskDet.setRiskValue(BigDecimal.valueOf(Math.abs(Lang.round(rule.getChkFeeSimulDiff() * koeff, 2))));
                    wmRiskDet.setRiskPercent((int)(koeff * 100));
                    wmRiskDet.setRiskCalcValue(wmRiskDet.getRiskValue());
                    wmRiskDet.setRiskCalcPercent(wmRiskDet.getRiskPercent());
                    wmRiskDet.setRiskBaseFee(BigDecimal.valueOf( Math.abs(rule.getChkFeeSimulDiff())));


                }else if(rule.getChkFeeSimulDiff() == 0 && wmRiskDet.getRiskValue().doubleValue() < ruleRisks.getRiskDefaultWasteValueAsNumber() * koeff ){
                    wmRiskDet.setRiskValue(BigDecimal.valueOf(Math.abs(Lang.round(ruleRisks.getRiskDefaultWasteValueAsNumber() * koeff, 2))));
                    wmRiskDet.setRiskPercent((int)(koeff * 100));
                    wmRiskDet.setRiskCalcValue(wmRiskDet.getRiskValue());
                    wmRiskDet.setRiskCalcPercent(wmRiskDet.getRiskPercent());
                    wmRiskDet.setRiskBaseFee(BigDecimal.valueOf( Math.abs(ruleRisks.getRiskDefaultWasteValueAsNumber() )));
                }
                wmRiskDet.setRiskNotCalculatedFee(wmRiskDet.getRiskBaseFee());
                if(wmRiskDet1 == null && (wmRiskDet.getRiskValue() != null || wmRiskDet.getRiskPercent() != 0)){
                    riskMap.put(ruleRisks.getRiskAreasAsEn(), wmRiskDet);
                }else if(wmRiskDet1 != null){
                    // compare two risks and select one with highest delta
                    if(wmRiskDet1.getRiskValue().compareTo(wmRiskDet.getRiskValue()) > 0 ){
                        // new risk ignore
                    }else {
                        // replace in riskmap
                        riskMap.put(ruleRisks.getRiskAreasAsEn(), wmRiskDet);
                    }
                }
                this.risk2rule.put(wmRiskDet, rule);
                area2rule.put(wmRiskDet.getRiskArea(), rule);
             }
             
             if(!riskMap.isEmpty()){

                return new ArrayList<>(riskMap.values());
             }

        
        return new ArrayList<>();
    }

    public CpxSimpleRuleDTO getRule2area(RiskAreaEn pArea) {
        return area2rule.get(pArea);
    }
    
    private ObjectProperty<CpxSimpleRuleDTO> rule4riskProperty;
    
    public ObjectProperty<CpxSimpleRuleDTO> rule4riskProperty(){
        if(rule4riskProperty == null){
            rule4riskProperty = new SimpleObjectProperty<CpxSimpleRuleDTO>();
        }
        return rule4riskProperty;
    }
    
    public void setRule4Risk(CpxSimpleRuleDTO rule){
        rule4riskProperty().set(rule);
    }
    
    public CpxSimpleRuleDTO getRule4Risk(){
        return rule4riskProperty().get();
    }
    
    private StringProperty titleDescriptionProperty;
    public StringProperty titleDescriptionProperty(){
        if(titleDescriptionProperty == null){
            titleDescriptionProperty = new SimpleStringProperty();
        }
        return titleDescriptionProperty;
    }
    public String getTitleDescription(){
        return titleDescriptionProperty().get();
    }
    public void setTitleDescription(String pDescription){
        titleDescriptionProperty().set(pDescription);
    }

    private ObjectProperty<VersionRiskTypeEn> versionTypePropperty;
    
    public ObjectProperty<VersionRiskTypeEn> versionTypePropperty(){
        if(versionTypePropperty == null){
            versionTypePropperty = new SimpleObjectProperty<>();
        }
        return versionTypePropperty;
    } 
    
    public void setVersionType(VersionRiskTypeEn pType){
        versionTypePropperty().set(pType);
    }
    
    public VersionRiskTypeEn getVersionType(){
        return versionTypePropperty().get();
    }

    public double calculateWaste4Risk() {
        getRisk().setRiskPercentTotal(getRisk().getRiskAuditPercent() * getRisk().getRiskWastePercent()/100);
        getRisk().setRiskValueTotal(BigDecimal.valueOf(Lang.round(getRisk().getRiskNotCalculatedFee().doubleValue() /100 * getRisk().getRiskPercentTotal(), 2)));
        this.getOnUpdateFullRisk().call( getRisk().getRiskPercentTotal());
        return  getRisk().getRiskValueTotal().doubleValue();
    }

//    public double calculateWaste4RiskDetails(TWmRiskDetails pDetails) {
//        pDetails.setRiskPercent(pDetails.getRiskAuditPercent() * pDetails.getRiskWastePercent()/100);
//        pDetails.setRiskValue(BigDecimal.valueOf(Lang.round(pDetails.getRiskBaseFee().doubleValue() /100 * pDetails.getRiskPercent(), 2)));
//        this.getOnUpdateRisk().call( pDetails);
//        return  pDetails.getRiskValue().doubleValue();
//    }

    private void checkResetProbability2VersType(List<TWmRiskDetails> risksFromRules) {
        if(getVersionType().equals(VersionRiskTypeEn.NOT_SET) || getVersionType().equals(VersionRiskTypeEn.BEFORE_BILLING)){
            return;
            
        }
        for(TWmRiskDetails risk: risksFromRules){
            risk.setRiskAuditPercent(100);
            risk.setRiskAuditPercentSugg(100);

            if(getVersionType().equals(VersionRiskTypeEn.CASE_FINALISATION)){
                risk.setRiskWastePercent(100);
                risk.setRiskWastePercentSugg(100);
            }
            if(risk.getRiskNotCalculatedFee() != null && risk.getRiskNotCalculatedFee().doubleValue() > 0){
                risk.setRiskBaseFee(risk.getRiskNotCalculatedFee());
            }
            if((getVersionType().equals(VersionRiskTypeEn.AUDIT_MD) || getVersionType().equals(VersionRiskTypeEn.AUDIT_CASE_DIALOG))){
                if(risk.getRiskWastePercentSugg() == null || !risk.getRiskWastePercentSugg().equals(risk.getRiskWastePercent())){
                    // user has changed percent value
                    risk.setRiskWastePercentSugg(risk.getRiskWastePercent());
                }
            }
            risk.setRiskCalcPercent(risk.getRiskAuditPercent() * risk.getRiskWastePercent()/100);
            risk.setRiskPercent(risk.getRiskCalcPercent());
            if(risk.getRiskBaseFee().doubleValue() == 0.0 && risk.getRiskValue()!= null && risk.getRiskValue().doubleValue() >0 ){
                // this risk does not come  from rule, riskValue was changed from user 
                //suggestions from RiskWastePercent and RiskValue are to save into Calc values
                risk.setRiskCalcValue(risk.getRiskValue());
//                    risk.setRiskWastePercentSugg(risk.getRiskWastePercent());

            }else{
//                if(!risk.getRiskValue().equals(risk.getRiskCalcValue())){
//                    risk.setRiskCalcValue(risk.getRiskValue());
//                }else{
                    risk.setRiskValue(BigDecimal.valueOf(Lang.round(risk.getRiskBaseFee().doubleValue() /100 * risk.getRiskPercent(), 2)));
                    risk.setRiskCalcValue(risk.getRiskValue());
//                }
            }
        }
        
    }

    SimpleDoubleProperty deltaFeeProperty;
    
    private SimpleDoubleProperty deltaFeeProperty(){
        if(deltaFeeProperty == null){
            deltaFeeProperty = new SimpleDoubleProperty();
        }
        return deltaFeeProperty;
    }
    
    public BigDecimal getDeltaFee() {
        Double val = deltaFeeProperty().get();
        return BigDecimal.valueOf(val);
    }
    
    public void setDeltaFee(double deltaFee){
        deltaFeeProperty().set(Math.abs(deltaFee));
    }

    public void resetActualCheck() {
        ((CmRiskDocumentationViewSkin)getSkin()).resetActualCheck();
    }

    private static final Callback<VersionRiskTypeEn,ReadOnlyRequestDTO> DEFAULT_REQUEST_CALLBACK = (VersionRiskTypeEn param) -> null;
    
    private Callback<VersionRiskTypeEn,ReadOnlyRequestDTO> requestCallback = DEFAULT_REQUEST_CALLBACK;
    public Callback<VersionRiskTypeEn,ReadOnlyRequestDTO> getRequestCallback(){
        return requestCallback;
    }
    public void setRequestCallback(Callback<VersionRiskTypeEn,ReadOnlyRequestDTO> pCallback){
        requestCallback = Objects.requireNonNullElse(pCallback, DEFAULT_REQUEST_CALLBACK);
    }
    
//    private static final Callback<TCaseDetails,ReadOnlyRequestDTO> DEFAULT_REQUEST_CALLBACK = (VersionRiskTypeEn param) -> null;
//    
//    private Callback<VersionRiskTypeEn,ReadOnlyRequestDTO> requestCallback = DEFAULT_REQUEST_CALLBACK;
//    public Callback<VersionRiskTypeEn,ReadOnlyRequestDTO> getRequestCallback(){
//        return requestCallback;
//    }
//    public void setRequestCallback(Callback<VersionRiskTypeEn,ReadOnlyRequestDTO> pCallback){
//        requestCallback = Objects.requireNonNullElse(pCallback, DEFAULT_REQUEST_CALLBACK);
//    }

    private static final Callback<VersionRiskTypeEn,Boolean> DEFAULT_VERSION_COMPARE_CALLBACK = (VersionRiskTypeEn param) -> false;
    
    private Callback<VersionRiskTypeEn,Boolean> versionCompareCallback = DEFAULT_VERSION_COMPARE_CALLBACK;
    public Callback<VersionRiskTypeEn,Boolean> getVersionCompareCallback(){
        return versionCompareCallback;
    }
    public void setVersionCompareCallback(Callback<VersionRiskTypeEn,Boolean> pCallback){
        versionCompareCallback = Objects.requireNonNullElse(pCallback, DEFAULT_VERSION_COMPARE_CALLBACK);
    }
}
