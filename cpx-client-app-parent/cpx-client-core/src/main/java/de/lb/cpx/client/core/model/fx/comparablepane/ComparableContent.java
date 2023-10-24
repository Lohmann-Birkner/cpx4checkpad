/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.core.model.fx.comparablepane;

import de.lb.cpx.client.core.model.fx.adapter.WeakPropertyAdapter;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.server.commons.dao.AbstractEntity;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Base class to compare items in the simulation sections
 *
 * @author wilde
 * @param <E> type of content
 */
public abstract class ComparableContent<E extends AbstractEntity> extends Object{
//    private final DoubleProperty widthProperty = new SimpleDoubleProperty(230);

    private final ObjectProperty<E> contentProperty = new SimpleObjectProperty<>();
    private ObjectProperty<TGroupingResults> groupingResultsProperty;// = new SimpleObjectProperty<>();
    private final BooleanProperty editableProperty = new SimpleBooleanProperty(true);
    private List<CpxSimpleRuleDTO> detectedRules = new ArrayList<>();
    private final Map<SupplFeeTypeEn, Double> supplFees = new HashMap<>();
    private  ObjectProperty<CaseTypeEn> caseTypeProperty = new SimpleObjectProperty<>();
//    private Double feeValue = 0.0;
    private Double caseBaserate = 0.0;
    private Double careBaserate = 0.0;

    private final WeakPropertyAdapter listenerAdapter;

    public ComparableContent(E pContent) {
        this.listenerAdapter = new WeakPropertyAdapter();
        contentProperty.set(pContent);
        setEditable(isEditable2());
    }

    public void destroy() {
    }

    public WeakPropertyAdapter getListenerAdapter() {
        return listenerAdapter;
    }
//    /**
//     * @return get the width of the content box 
//     */
//    public Double getWidth(){
//        return widthProperty.get();
//    }
//    /**
//     * @param pWidth set new width
//     */
//    public void setWidth(double pWidth){
//        widthProperty.set(pWidth);
//    }
//    /**
//     * @return with of the content  
//     */
//    public DoubleProperty widthProperty(){
//        return widthProperty;
//    }

    /**
     * @return grouping results after grouping
     */
    public abstract TGroupingResults performGroup();

    public abstract TGroupingResults performGroup(List<Long> ruleIds);

    /**
     * @param pIcd save this icd in database
     */
    public abstract void saveIcdEntity(TCaseIcd pIcd);

    public abstract void deleteIcdEntity(TCaseIcd pIcd);

    /**
     * @param pOps save this ops in database
     */
    public abstract void saveOpsEntity(TCaseOps pOps);

    public abstract void deleteOpsEntity(TCaseOps pOps);

    private Boolean isEditable2() {
        return isEditable();
    }

    /**
     * @return should content be editable
     */
    public abstract Boolean isEditable();

    /**
     *
     * @return should content be editable
     */
    public abstract Boolean isCanceled();

    /**
     * @return valid catalog year for this content
     */
    public abstract int getCatalogYear();

    /**
     * @return version name shown in the ui
     */
    public abstract String getVersionName();

    public abstract TCaseDetails getCaseDetails();

    public abstract void reload();

    public abstract void saveCaseDetailsEntity(TCaseDetails pDetails);

    public abstract TGroupingResults getLastGroupingResultFromDb();

    /**
     * @param pIsEditable set editable value
     */
    public final void setEditable(Boolean pIsEditable) {
        editableProperty.set(pIsEditable);
    }

    /**
     * @return editable property
     */
    public BooleanProperty editableProperty() {
        return editableProperty;
    }

    /**
     * @return id of the given entity
     */
    public long getContentId() {
        return getContent().id;
    }

    /**
     * @return give content entity
     */
    public E getContent() {
        return contentProperty.get();
    }

    /**
     * @param pContent set new content WARNING: UI is not automatically updated
     */
    public void setContent(E pContent) {
        contentProperty.set(pContent);
    }

    /**
     * @return content property to enable some ui updates
     */
    public final ObjectProperty<E> contentProperty() {
        return contentProperty;
    }

    /**
     * @return get the current grouping result
     */
    public final TGroupingResults getGroupingResult() {
//        if(groupingResultsProperty == null){
//            groupingResultsProperty = new SimpleObjectProperty<>();
//            groupingResultProperty().set(performGroup());
//            TGroupingResults lastResult = getLastGroupingResultFromDb();
//            if(lastResult == null){
//                TGroupingResults newResult = performGroup();
//                setGroupingResult(newResult);
//            }else{
//                setGroupingResult(lastResult);
//            }
//        }
        return groupingResultProperty().get();
    }

    /**
     * @param pGroupingResults set grouping results
     */
    public void setGroupingResult(TGroupingResults pGroupingResults) {
        groupingResultProperty().set(pGroupingResults);
    }
    

    /**
     * @return get the grouping result property
     */
    public final ObjectProperty<TGroupingResults> groupingResultProperty() {
        if (groupingResultsProperty == null) {
            groupingResultsProperty = new SimpleObjectProperty<>();
        }
        return groupingResultsProperty;
    }
    
    public void setCaseType(CaseTypeEn pCaseType){
        caseTypeProperty().set(pCaseType);
    }
    
    public final ObjectProperty<CaseTypeEn> caseTypeProperty(){
        if(caseTypeProperty == null){
            caseTypeProperty = new SimpleObjectProperty();
        }
        return caseTypeProperty;
    }
    
    public CaseTypeEn getCaseType(){
        return caseTypeProperty().get() == null?CaseTypeEn.OTHER:caseTypeProperty().get();
    }
    
    


    protected void initalizeGroupingResult() {
        TGroupingResults lastResult = getLastGroupingResultFromDb();
        if (lastResult == null || !allIcdsGrouped()){
            TGroupingResults newResult = performGroup();
            setGroupingResult(newResult);
        } else {
            setGroupingResult(lastResult);
        }
    }

    /**
     * @param pDetectedRules clear list and add list of rules to it
     */
    public void clearAndAllDetectedRules(List<CpxSimpleRuleDTO> pDetectedRules) {
        detectedRules.clear();
        detectedRules.addAll(pDetectedRules);
    }

    /**
     * @param pDetectedRules add all rules to existing rules set
     */
    public void addDetectedRules(List<CpxSimpleRuleDTO> pDetectedRules) {
        detectedRules.addAll(pDetectedRules);
    }

    /**
     * @param pDetectedrules replace list with previous one
     */
    public void setDetectedRules(List<CpxSimpleRuleDTO> pDetectedrules) {
        detectedRules = pDetectedrules == null ? null : new ArrayList<>(pDetectedrules);
    }

    /**
     * @return get the current list
     */
    public List<CpxSimpleRuleDTO> getDetectedRules() {
        return detectedRules == null ? null : new ArrayList<>(detectedRules);
    }

    /**
     * @return supplementary fees currently set
     */
    public Map<SupplFeeTypeEn, Double> getSupplFeeValues() {
        return supplFees;
    }

    /**
     * @param pSupplFeeValue set supplementary fee
     * @param pType type of the supplementary fee
     */
    public void setSupplFeeValue(Double pSupplFeeValue, SupplFeeTypeEn pType) {
        supplFees.put(pType, pSupplFeeValue);
//        feeValue = pSupFeeValue;
    }

    public Double getSupplFeeValue(SupplFeeTypeEn pType) {
        return supplFees.get(pType);
    }

    /**
     * @return get fee value from detected baserate
     */
    public Double getCaseBaserate() {
        return caseBaserate;
    }

    /**
     * @param pBaserate set baserate fee values
     */
    public void setCaseBaserate(Double pBaserate) {
        caseBaserate = pBaserate;
    }
    
    /**
     * @return get fee value from detected baserate
     */
    public Double getCareBaserate() {
        return careBaserate;
    }

    /**
     * @param pBaserate set baserate fee values
     */
    public void setCareBaserate(Double pBaserate) {
        careBaserate = pBaserate;
    }
    
    /**
     * @return number of rule errors detected
     */
    public int getRuleErrorCount() {
        int counter = 0;
        if (detectedRules == null) {
            return counter;
        }
        for (CpxSimpleRuleDTO dto : detectedRules) {
//            if (dto.getRuleTyp().equals(Lang.getError())) {
//                counter++;
//            }
            if (dto.isError()) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * @return number of suggestions(information) detected
     */
    public int getRuleSuggestionCount() {
        int counter = 0;
        if (detectedRules == null) {
            return counter;
        }
        for (CpxSimpleRuleDTO dto : detectedRules) {
//            if (dto.getRuleTyp().equals(Lang.getInformation())) {
//                counter++;
//            }
            if (dto.isInformation()) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * @return number of rule warnings detected
     */
    public int getRuleWarningCount() {
        int counter = 0;
        if (detectedRules == null) {
            return counter;
        }
        for (CpxSimpleRuleDTO dto : detectedRules) {
//            if (dto.getRuleTyp().equals(Lang.getWarning())) {
//                counter++;
//            }
            if (dto.isWarning()) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * @return one of the highest rated rules
     */
    public CpxSimpleRuleDTO getHighestRule() {
        List<CpxSimpleRuleDTO> sorted = getSortedRules();
        if (!sorted.isEmpty()) {
            return getSortedRules().get(0);
        }
        return null;
    }

    /**
     * @return sorted rules by severity
     */
    public List<CpxSimpleRuleDTO> getSortedRules() {
        List<CpxSimpleRuleDTO> list = getDetectedRules();
        if (list == null) {
            return new ArrayList<>();
        }
        list.sort(new Comparator<CpxSimpleRuleDTO>() {
            @Override
            public int compare(CpxSimpleRuleDTO o1, CpxSimpleRuleDTO o2) {
                return Integer.compare(o2.getRuleTyp() != null ? o2.getRuleTypeSeverity() : 0, o1.getRuleTyp() != null ? o1.getRuleTypeSeverity() : 0);
            }
        });
        return list;
    }

    /**
     *
     * @return
     */
    protected abstract boolean allIcdsGrouped();
}
