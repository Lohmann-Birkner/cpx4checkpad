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
package de.lb.cpx.ruleviewer.util;

import de.lb.cpx.rule.element.model.Risk;
import de.lb.cpx.rule.element.model.RiskArea;
import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author wilde
 */
public class RiskDisplayHelper {
    protected static final String RISK_TEXT = "Risiko: ";
    protected static final String NOT_AVAILABLE = "n.A.";
    protected static final String NOT_SELECTED_TEXT = "Nicht angegeben";
    protected static final String EMPTY_TEXT = "----";
    protected static final String COLLECTION_SEPERATOR = ", ";
    protected static final String PERCENT_VALUE = " %";
    protected static final String CURRENCY_VALUE = " €";
    protected static final String TITLE_VALUE_DIVIDER = " - ";
    
    private RiskDisplayHelper() {
    throw new IllegalStateException("Utility class");
  }
    
    public static final String getRiskDisplayText(Risk pRisk){
        StringBuilder sb = new StringBuilder(RISK_TEXT);
        if(pRisk == null || pRisk.getRiskAreas().isEmpty()){
            sb.append(NOT_SELECTED_TEXT);
            return sb.toString();
        }
        sb.append(getRiskAreaText(pRisk));
        sb.append(COLLECTION_SEPERATOR);
        //only first is relevant if there are somehow multiple! values in other risk areas should match besides of risk area name
        RiskArea firstRiskArea = pRisk.getRiskAreas().get(0);
        sb.append(getRisksAndWasteText(firstRiskArea));
        return sb.toString();
    }
    public static final String getRiskAreaText(Risk pRisk){
        return new StringBuilder().append(Lang.getRiskAreaObj().getAbbreviation()).append(TITLE_VALUE_DIVIDER).append(getTranslatedRiskAreas(pRisk)).toString();
    }
    public static final String getTranslatedRiskAreas(Risk pRisk){
        if(pRisk == null || pRisk.getRiskAreas().isEmpty()){
            return NOT_AVAILABLE;
        }
        return getTranslatedRiskAreas(pRisk.getRiskAreas().get(0));
//        return pRisk.getRiskAreas().stream()
//                .map((t) -> {
//                    String names = getTranslatedRiskAreas(t);
//                    if(names == null ||names.isEmpty()){
//                        return null;
//                    }
//                    return names;
//                }).collect(Collectors.joining(COLLECTION_SEPERATOR));
    }
    public static final String getTranslatedRiskAreas(RiskArea pRiskArea){
        if(pRiskArea == null){
            return NOT_AVAILABLE;
        }
        String areaNames = pRiskArea.getRiskAreaName();
        if(areaNames == null){
            return EMPTY_TEXT;
        }
        return Stream.of(areaNames.split(","))
                .map((name) -> {
                    name = name.trim();
                    name = RiskAreaEn.valueOf(name).getTranslation().getValue();
                    return name;
                }).collect(Collectors.joining(COLLECTION_SEPERATOR));
    }
    
    public static final String getRisksAndWasteText(RiskArea pRiskArea){
        return new StringBuilder()
                .append(getRisksText(pRiskArea))
                .append(COLLECTION_SEPERATOR)
                .append(getRiskDefaultWasteText(pRiskArea)).toString();
    }
    
    public static final String getRisksText(RiskArea pRiskArea){
        return new StringBuilder()
                .append(getRiskAuditValueText(pRiskArea))
                .append(COLLECTION_SEPERATOR)
                .append(getRiskWasteValueText(pRiskArea)).toString();
    }
    
    public static final String getRiskWasteValueText(RiskArea pRiskArea){
        if(pRiskArea == null){
            return EMPTY_TEXT;
        }
        return new StringBuilder().append(Lang.getRiskEditorRiskWasteValueObj().getAbbreviation()).append(TITLE_VALUE_DIVIDER).append(getTranslatedRiskWastePercent(pRiskArea)).toString();
    }
    public static final String getRiskAuditValueText(RiskArea pRiskArea){
        if(pRiskArea == null){
            return EMPTY_TEXT;
        }
        return new StringBuilder().append(Lang.getRiskEditorRiskAuditValueObj().getAbbreviation()).append(TITLE_VALUE_DIVIDER).append(getTranslatedRiskAuditPercent(pRiskArea)).toString();
    }
    public static final String getRiskDefaultWasteText(RiskArea pRiskArea){
        return new StringBuilder().append(Lang.getRiskEditorRiskWasteDefaultValueObj().getAbbreviation()).append(TITLE_VALUE_DIVIDER).append(getTranslatedRiskDefaultWaste(pRiskArea)).toString();
    }
    public static final String getTranslatedRiskDefaultWaste(Risk pRisk){
        if(pRisk == null || pRisk.getRiskAreas().isEmpty()){
            return NOT_AVAILABLE;
        }
        return getTranslatedRiskDefaultWaste(pRisk.getRiskAreas().get(0));
    }
    public static final String getTranslatedRiskDefaultWaste(RiskArea pRiskArea){
        if(pRiskArea == null){
            return EMPTY_TEXT;
        }
        String riskWaste = Objects.requireNonNullElse(pRiskArea.getRiskDefaultWasteValue(),"");
        return riskWaste.isEmpty()?NOT_AVAILABLE:new StringBuilder().append(getCurrencyString(riskWaste)).append(CURRENCY_VALUE).toString();
    }
    public static final String getTranslatedRiskWastePercent(Risk pRisk){
        if(pRisk == null || pRisk.getRiskAreas().isEmpty()){
            return NOT_AVAILABLE;
        }
        return getTranslatedRiskWastePercent(pRisk.getRiskAreas().get(0));
    }
    public static final String getTranslatedRiskWastePercent(RiskArea pRiskArea){
        if(pRiskArea == null){
            return NOT_AVAILABLE;
        }
        String riskValue = Objects.requireNonNullElse(pRiskArea.getRiskWastePercentValue(),"");
        return riskValue.isEmpty()?NOT_AVAILABLE:new StringBuilder().append(riskValue).append(PERCENT_VALUE).toString();
    }
    public static final String getTranslatedRiskAuditPercent(Risk pRisk){
        if(pRisk == null || pRisk.getRiskAreas().isEmpty()){
            return NOT_AVAILABLE;
        }
        return getTranslatedRiskAuditPercent(pRisk.getRiskAreas().get(0));
    }
    public static final String getTranslatedRiskAuditPercent(RiskArea pRiskArea){
        if(pRiskArea == null){
            return NOT_AVAILABLE;
        }
        String riskValue = Objects.requireNonNullElse(pRiskArea.getRiskAuditPercentValue(),"");
        return riskValue.isEmpty()?NOT_AVAILABLE:new StringBuilder().append(riskValue).append(PERCENT_VALUE).toString();
    }
    private static String getCurrencyString(String pDoubleString) {
        try {
            Double value = Double.valueOf(pDoubleString.replace(",", "."));
            return Lang.toDecimal(value, 2);
        } catch (NumberFormatException ex) {
            return pDoubleString;
        }
    }
}
