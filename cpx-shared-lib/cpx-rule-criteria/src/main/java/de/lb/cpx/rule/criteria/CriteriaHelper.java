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
package de.lb.cpx.rule.criteria;

import de.lb.cpx.rule.criteria.model.CriterionTree;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion.Tooltip;
import de.lb.cpx.shared.lang.Lang;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author wilde
 */
public class CriteriaHelper {

    public static final String DATATYPE_DATE_WITH_TIME_SHOW = "DATATYPE_DATE_WITH_TIME_SHOW";
    public static final String DATATYPE_INTEGER = "DATATYPE_INTEGER";
    public static final String DATATYPE_ARRAY_INTEGER = "DATATYPE_ARRAY_INTEGER";
    public static final String DATATYPE_DATE = "DATATYPE_DATE";
    public static final String DATATYPE_ARRAY_DOUBLE = "DATATYPE_ARRAY_DOUBLE";
    public static final String DATATYPE_ARRAY_STRING = "ATATYPE_ARRAY_STRING";
    public static final String DATATYPE_DAY_TIME = "DATATYPE_DAY_TIME";
    public static final String DATATYPE_ARRAY_DATE = "DATATYPE_ARRAY_DATE";
    public static final String DATATYPE_UNFORMATTED = "DATATYPE_UNFORMATTED";
    public static final String DATATYPE_STRING = "DATATYPE_STRING";
    public static final String DATATYPE_DOUBLE = "DATATYPE_DOUBLE";
    public static final String DATATYPE_ARRAY_DAY_TIME = "DATATYPE_ARRAY_DAY_TIME";

    public static final String DATA_NAME_PCCL = "PCCL";

    private CriteriaHelper() {
        //utility class needs no public constructor
    }

    /**
     * @param pCriterion criterion to check
     * @return indicator if criterion values are enum, there are no mixed
     * tooltips, should abort after first
     */
    public static boolean isEnum(CriterionTree.Supergroup.Group.Criterion pCriterion) {
        if (pCriterion == null) {
            return false;
        }
        for (CriterionTree.Supergroup.Group.Criterion.Tooltip tip : pCriterion.getTooltip()) {
            if (tip.getValue() != null) {
                return true;
            }
        }

//        if (!pCriterion.getTooltip().isEmpty()) {
//            return pCriterion.getTooltip().get(0).getValue() != null;
//        }
        return false;
    }

    public static String getTooltipText(CriterionTree.Supergroup.Group.Criterion criterion) {
        if (criterion == null) {
            return "----";
        }
        return criterion.getTooltip().stream().map(new Function<Tooltip, String>() {
            @Override
            public String apply(Tooltip t) {
                return getTooltipDescription(t);
            }
        }).collect(Collectors.joining("\n"));
    }

    public static String getTooltipValue(String pValue, CriterionTree.Supergroup.Group.Criterion criterion) {
        for (Tooltip tip : criterion.getTooltip()) {
            if (pValue.equals(tip.getValue())) {
                return getTooltipDescription(tip);
            }
        }
        return null;
    }

    public static Boolean isDate(CriterionTree.Supergroup.Group.Criterion pCriterion) {
        String pType = pCriterion.getCriterionType();
        return DATATYPE_ARRAY_DATE.equals(pType) || DATATYPE_ARRAY_DAY_TIME.equals(pType) || DATATYPE_DAY_TIME.equals(pType) || DATATYPE_DATE.equals(pType) || DATATYPE_DATE_WITH_TIME_SHOW.equals(pType);
    }

    public static Boolean isString(CriterionTree.Supergroup.Group.Criterion pCriterion) {
        String pType = pCriterion.getCriterionType();
        return DATATYPE_ARRAY_STRING.equals(pType) || DATATYPE_STRING.equals(pType);
    }

    public static Boolean isDouble(CriterionTree.Supergroup.Group.Criterion pCriterion) {
        String pType = pCriterion.getCriterionType();
        return DATATYPE_ARRAY_DOUBLE.equals(pType) || DATATYPE_DOUBLE.equals(pType);
    }

    public static Boolean isInteger(CriterionTree.Supergroup.Group.Criterion pCriterion) {
        String pType = pCriterion.getCriterionType();
        return DATATYPE_ARRAY_INTEGER.equals(pType) || DATATYPE_INTEGER.equals(pType);
    }

    public static Boolean isUnformatted(CriterionTree.Supergroup.Group.Criterion pCriterion) {
        String type = pCriterion.getCriterionType();
        return DATATYPE_UNFORMATTED.equals(type);
    }

    public static Boolean isPccl(CriterionTree.Supergroup.Group.Criterion pCriterion) {
        String name = pCriterion.getCpname();
        return DATA_NAME_PCCL.equals(name);
    }

    public static  String getDisplayName(CriterionTree.Supergroup.Group.Criterion pCriterion) {
        if(pCriterion == null){
            return "";
        }
        if(pCriterion.getDisplayName() == null ){
            if(pCriterion.getCpname() != null && !pCriterion.getCpname().isEmpty()){
                return pCriterion.getCpname();
            }
            
            return "";
        }
        if(pCriterion.getDisplayName().contains("rules") ){
            return Lang.get(pCriterion.getDisplayName().replace("rules", "Rules")).getValue();
        }
        return pCriterion.getDisplayName();
    }
    
    public static String getTooltipDescription(Tooltip tooltip){
        if(tooltip == null || tooltip.getDescription() == null || tooltip.getDescription().isEmpty()){
            return "";
        }
        if(tooltip.getDescription().contains("rules")){
            return Lang.get(tooltip.getDescription().replace("rules", "Rules")).getValue();
        }
        return tooltip.getDescription();
    }
}
