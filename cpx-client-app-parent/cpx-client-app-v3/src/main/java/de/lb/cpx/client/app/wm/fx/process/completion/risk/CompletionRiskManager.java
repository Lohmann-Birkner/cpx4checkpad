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
package de.lb.cpx.client.app.wm.fx.process.completion.risk;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.wm.model.TWmFinalisationRisk;
import de.lb.cpx.wm.model.TWmFinalisationRiskDetail;
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author wilde
 */
public class CompletionRiskManager {

    private static final Logger LOG = Logger.getLogger(CompletionRiskManager.class.getName());

    private final ProcessServiceFacade facade;
//    private List<TWmRisk> allRisks;
//    private TWmRisk finalRiskCache;
    private TWmFinalisationRisk finalRiskCache;
    private ArrayList<TWmRisk> tempRisk;
    private Long prevRiskId;

    public CompletionRiskManager(ProcessServiceFacade pFacade) {
        facade = pFacade;
    }
    public void load(){
//        allRisks = facade.getAllRisks();
        //needed because final risk can be cached!
        tempRisk = new ArrayList<>();
//        tempRisk.add(getFinalRisk());
        tempRisk.add(getInitialCaseRisk());
        tempRisk.addAll(getMdRequestRisks());
        riskAreas().setAll(findNonExistingRiskArea(new ArrayList<>(), tempRisk,getFinalRisk()));
        FXCollections.sort(riskAreas());
    }
    //make list unmodiviabnble for outside access??
    private final ObservableList<RiskAreaEn> riskAreas = FXCollections.observableArrayList();
    public final ObservableList<RiskAreaEn> riskAreas(){
        return riskAreas;
    }
    
    public TWmRisk getInitialCaseRisk(){
        TWmRisk initial = facade.getActualBilingRisk();//getActualRiskForVersionRiskType(VersionRiskTypeEn.BEFORE_BILLING);//findInitialCaseRisk();
        if(initial == null){
            initial = new TWmRisk();
//            initial.setHospitalCase(facade.getCurrentMainCase());
            initial.setRiskPlaceOfReg(PlaceOfRegEn.BEFORE_BILLING);
            initial.setRiskValueTotal(BigDecimal.ZERO);
            initial.setRiskPercentTotal(0);
        }
        return initial;
    }
    
//    private TWmRisk findInitialCaseRisk(){
//        for(TWmRisk risk : allRisks){
//            if(PlaceOfRegEn.BEFORE_BILLING.equals(risk.getRiskPlaceOfReg())){
//                return risk;
//            }
//        }
//        return null;
//    }
    
    public TWmFinalisationRisk getFinalRisk(){
        TWmFinalisationRisk finalRisk = findFinalRisk();
        if(finalRisk == null){
            finalRisk = new TWmFinalisationRisk();
            //try to find twmrisk for process completion
            TWmRisk risk = facade.getCompletionRisk();
            if(risk != null){
                prevRiskId = risk.getId();
                setRiskAreaValuesForFinalRisk(risk);
                finalRisk.setRiskValueTotal(risk.getRiskValueTotal());
                getLastRequestRiskAreaValues();
                for(TWmRiskDetails det : risk.getRiskDetails()){
                    if(!det.getRiskUsedForFinalFl()){
                        LOG.log(Level.INFO, "riskDetail: {0} not marked for final!", det.getRiskArea().name());
                        continue;
                    }
                    TWmFinalisationRiskDetail finDet = new TWmFinalisationRiskDetail();
                    finDet.setFinalisRisk(finalRisk);
                    finalRisk.getFinalisationRiskDetails().add(finDet);
                    finDet.setRiskArea(det.getRiskArea());
                    finDet.setRiskCalcValue(det.getRiskCalcValue());
                    finDet.setRiskComment(det.getRiskComment());
                    finDet.setRiskValue(det.getRiskValue());
                }
            }else{
                finalRisk.setRiskValueTotal(BigDecimal.ZERO);
            }
        }
        return finalRisk;
    }
    
    private TWmFinalisationRisk findFinalRisk(){
        if(finalRiskCache != null){
            return finalRiskCache;
        }
//        for(TWmRisk risk : allRisks){
//            if(PlaceOfRegEn.REQUEST_FINALISATION.equals(risk.getRiskPlaceOfReg())){
//                return risk;
//            }
//        }
        if(facade.getCurrentProcessFinalisation() == null){
            return null;
        }
//        return facade.getCurrentProcessFinalisation().getRisks();
        return facade.getCompletionRiskForProcessFinalisation();
    }
    public List<TWmRisk> getMdRequestRisks(){
        return findMdRequestRisks();
    }
    private List<TWmRisk> findMdRequestRisks(){
//        return allRisks.stream().filter((t) -> {
//            return PlaceOfRegEn.REQUEST.equals(t.getRiskPlaceOfReg())/* || PlaceOfRegEn.REQUEST_EXTENSION.equals(t.getRiskPlaceOfReg())*/;
//        }).sorted((o1, o2) -> {
//            return Long.compare(o1.getRequest()!=null?o1.getRequest().id:-1,o2.getRequest()!=null?o2.getRequest().id:-1);
//        }).collect(Collectors.toList());
        //only to display can cause database issues when risk is stored
        //remove lol!
        return facade.getActualRequestRisks().stream().map((t) -> {
            t.setRiskPlaceOfReg(PlaceOfRegEn.REQUEST);
            return t;
        }).collect(Collectors.toList());
    }
    
    private List<RiskAreaEn> findNonExistingRiskArea(List<RiskAreaEn> pAreas,List<TWmRisk> pRisks, TWmFinalisationRisk pFinalRisk){
        for(TWmRisk risk : pRisks){
            for(TWmRiskDetails detail : risk.getRiskDetails()){
                if(!pAreas.contains(detail.getRiskArea())){
                    pAreas.add(detail.getRiskArea());
                }
            }
        }
        for(TWmFinalisationRiskDetail finalisDetail : pFinalRisk.getFinalisationRiskDetails()){
            if(!pAreas.contains(finalisDetail.getRiskArea())){
                pAreas.add(finalisDetail.getRiskArea());
            }
        }
        return pAreas;
    }
    
    public boolean hasAnyoneRiskArea(RiskAreaEn pArea){
        for(TWmRisk risk : tempRisk){
            for(TWmRiskDetails detail : risk.getRiskDetails()){
                if(pArea.equals(detail.getRiskArea())){
                    return true;
                }
            }
        }
        return false;
    }
    public Boolean addRiskArea(RiskAreaEn param) {
//        if(!hasAnyoneRiskArea(param)){
//            return riskAreas().add(param);
//        }
        //add if not in array yet
        if(!riskAreas().contains(param)){
            boolean result = riskAreas().add(param);
            FXCollections.sort(riskAreas());
            return result;
        }
        return false;
    }

    public Boolean removeRiskArea(RiskAreaEn param) {
        //remove if non of stored risks has riskarea
        if(!hasAnyoneRiskArea(param)){
            boolean result = riskAreas().remove(param);
            FXCollections.sort(riskAreas());
            return result;
        }
        return false;
    }

    public void cacheFinalRiskState(TWmFinalisationRisk risk) {
        finalRiskCache = risk;
    }

    public TWmFinalisationRisk getFinalRiskCache() {
        return finalRiskCache;
    }
    private static final Map<RiskAreaEn,BigDecimal> lastRequestRiskAreaValues = new EnumMap<>(RiskAreaEn.class);
    public synchronized void setRiskAreaValuesForFinalRisk(TWmRisk risk) {
        if(risk == null){
            return;
        }
        if(!PlaceOfRegEn.REQUEST.equals(risk.getRiskPlaceOfReg())&&!PlaceOfRegEn.REQUEST_FINALISATION.equals(risk.getRiskPlaceOfReg())){
            return;
        }
        lastRequestRiskAreaValues.clear();
        for(TWmRiskDetails detail : risk.getRiskDetails()){
            lastRequestRiskAreaValues.put(detail.getRiskArea(), detail.getRiskValue());
        }
    }
    
    public Map<RiskAreaEn,BigDecimal> getLastRequestRiskAreaValues(){
        return lastRequestRiskAreaValues;
    } 

    public boolean hasFinalRiskChanged() {
        if(prevRiskId == null){
            return false;
        }
        TWmRisk risk = facade.getCompletionRisk();
        if(risk == null){
            return false;
        }
        //has changed if ids are not equals
        return !Objects.equals(risk.getId(), prevRiskId);
    }
}