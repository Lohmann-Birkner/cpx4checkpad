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
 *    2018  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.rule.criteria;

import com.sun.istack.NotNull;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion;
import de.lb.cpx.rule.criteria.model.TypesAndOperations;
import de.lb.cpx.rule.criteria.model.TypesAndOperations.OperationGroups;
import de.lb.cpx.rule.criteria.model.TypesAndOperations.SuggActions.SuggAction;
import de.lb.cpx.rule.util.XMLHandler;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;

/**
 * Stores terms and operations as singelton in ram
 *
 * @author wilde
 */
public class TypesAndOperationsManager {

    private static final String RESOURCE_FILE = "/xml/";
    private static final String TYPES_AND_OPERATIONS = "types_operations.xml";
    private static final String TYPES_AND_OPERATIONS_RESOURCE = RESOURCE_FILE + TYPES_AND_OPERATIONS;
    private static final String SUGG_ACTION_IDENT_CHANGE = "2";
    private static final String SUGG_ACTION_IDENT_DELETE = "0";
    private static final String SUGG_ACTION_IDENT_ADD = "1";
    private static final String SUGG_ACTION_IDENT_NONE = "-1";

    private TypesAndOperations typesAndOperations;
    private Map<String, OperationGroups.OperationGroup> operationsGroupMap;
    private Map<String, TypesAndOperations.CriterionTypes.CtriterionType> ctriterionTypeMap;
    private Map<String, SuggAction> suggestionActionMap;

    private static TypesAndOperationsManager instance;
    private List<TypesAndOperations.IntervalLimits.IntervalLimit> allLimits;

    private TypesAndOperationsManager() {
    }

    public static synchronized TypesAndOperationsManager instance() {
        if (instance == null) {
            instance = new TypesAndOperationsManager();
        }
        return instance;
    }

    public synchronized void destroy() {
        if (instance != null) {
            instance.clear();
            instance = null;
        }
    }

    private void clear() {
        typesAndOperations = null;
    }

    public TypesAndOperations getTypesAndOperations() {
        if (typesAndOperations == null) {
            try {
                typesAndOperations = (TypesAndOperations) XMLHandler.unmarshalXML(getClass().getResource(TYPES_AND_OPERATIONS_RESOURCE), TypesAndOperations.class);
            } catch (JAXBException ex) {
                Logger.getLogger(TypesAndOperationsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return typesAndOperations;
    }

    public Map<String, OperationGroups.OperationGroup> getOperationsGroupMap() {
        if (operationsGroupMap == null) {
            operationsGroupMap = new HashMap<>();
            initOperationsGroupMap(operationsGroupMap);
        }
        return operationsGroupMap;
    }

    public OperationGroups.OperationGroup getOperationGroupByName(String pGroupName) {
        return getOperationsGroupMap().get(pGroupName);
    }

    private void initOperationsGroupMap(Map<String, OperationGroups.OperationGroup> pMap) {
        for (OperationGroups.OperationGroup group : getTypesAndOperations().getOperationGroups().getOperationGroup()) {
//            group = clearGroup(group);
            pMap.put(group.getName(), group);
        }
    }

    public Map<String, TypesAndOperations.CriterionTypes.CtriterionType> getCtriterionTypeMap() {
        if (ctriterionTypeMap == null) {
            ctriterionTypeMap = new HashMap<>();
            initCtrierionTypesMap(ctriterionTypeMap);
        }
        return ctriterionTypeMap;
    }

    public TypesAndOperations.CriterionTypes.CtriterionType getCtriterionTypeByName(String pName) {
        return getCtriterionTypeMap().get(pName);
    }

    private void initCtrierionTypesMap(Map<String, TypesAndOperations.CriterionTypes.CtriterionType> pMap) {
        for (TypesAndOperations.CriterionTypes.CtriterionType type : getTypesAndOperations().getCriterionTypes().getCtriterionType()) {
            pMap.put(type.getName(), type);
        }
    }

    public OperationGroups.OperationGroup getOperationGroupByType(String pType) {
        TypesAndOperations.CriterionTypes.CtriterionType type = getCtriterionTypeByName(pType);
        if (type == null) {
            return null;
        }
        return getOperationGroupByName(type.getOperationGroup());
    }

    public OperationGroups.OperationGroup.Operation getOperator(Criterion pCriterion, String pOperator) {
        if (pCriterion == null) {
            return null;
        }
        //OperationGroups.OperationGroup group = getOperationGroupByType(pCriterion.getCriterionType());
        OperationGroups.OperationGroup group;
        String type;
        if (CriteriaHelper.isEnum(pCriterion)) {
            type = CriteriaHelper.isPccl(pCriterion) ? "opList_compare_only" : "opList_equal";
            group = TypesAndOperationsManager.instance().getOperationGroupByName(type);
        } else {
            type = pCriterion.getCriterionType();
            group = TypesAndOperationsManager.instance().getOperationGroupByType(type);
        }
//        OperationGroups.OperationGroup group = TypesAndOperationsManager.instance().getOperationGroupByType(type);
        if (group == null) {
            return null;
        }
        for (OperationGroups.OperationGroup.Operation op : group.getOperation()) {
            if (op.getName().equals(pOperator)) {
                return op;
            }
        }
        return null;
    }

    public OperationGroups.OperationGroup.Operation getTermOperator(String pOperator) {
        OperationGroups.OperationGroup group = getOperationGroupByName("opListNested");
        if (group == null) {
            return null;
        }
        for (OperationGroups.OperationGroup.Operation op : group.getOperation()) {
            if (op.getName().equals(pOperator)) {
                return op;
            }
        }
        return null;
    }

    public Map<String, SuggAction> getSuggestionActionMap() {
        if (suggestionActionMap == null) {
            suggestionActionMap = new HashMap<>();
            initSuggestionActionMap(suggestionActionMap);
        }
        return suggestionActionMap;
    }

    private void initSuggestionActionMap(Map<String, SuggAction> pSuggestionActionMap) {
        for (SuggAction action : getTypesAndOperations().getSuggActions().getSuggAction()) {
            pSuggestionActionMap.put(String.valueOf(action.getIdent()), action);
        }
    }

    /**
     * @param pId -1=nothing, 0=delete,1=add,2=change
     * @return Suggestion Action based on id
     */
    public SuggAction getSuggestionActionById(String pId) {
        return getSuggestionActionMap().get(pId);
    }

    /**
     * @return all suggestion actions sorted by ident
     */
    public List<SuggAction> getAllSuggActions() {
        List<SuggAction> list = new ArrayList<>(getTypesAndOperations().getSuggActions().getSuggAction());
        list.sort(new Comparator<SuggAction>() {
            @Override
            public int compare(SuggAction o1, SuggAction o2) {
                return o1.getIdent().compareTo(o2.getIdent());
            }
        });
        return list;
    }

    /**
     * @return all valid suggestions as list
     */
    public List<SuggAction> getSuggActions_ActList() {
        List<SuggAction> list = new ArrayList<>();
        list.add(getSuggestionActionById(SUGG_ACTION_IDENT_ADD));
        list.add(getSuggestionActionById(SUGG_ACTION_IDENT_CHANGE));
        list.add(getSuggestionActionById(SUGG_ACTION_IDENT_DELETE));
        return list;
    }

    /**
     * @return list with only add action
     */
    public List<SuggAction> getSuggActions_ActListAddOnly() {
        List<SuggAction> list = new ArrayList<>();
        list.add(getSuggestionActionById(SUGG_ACTION_IDENT_ADD));
        return list;
    }

    /**
     * @return list with only change action
     */
    public List<SuggAction> getSuggActions_ActListChangeOnly() {
        List<SuggAction> list = new ArrayList<>();
        list.add(getSuggestionActionById(SUGG_ACTION_IDENT_CHANGE));
        return list;
    }

    /**
     * @return list with action delete and add
     */
    public List<SuggAction> getSuggActions_ActListAddDelete() {
        List<SuggAction> list = new ArrayList<>();
        list.add(getSuggestionActionById(SUGG_ACTION_IDENT_ADD));
        list.add(getSuggestionActionById(SUGG_ACTION_IDENT_DELETE));
        return list;
    }

    private OperationGroups.OperationGroup clearGroup(OperationGroups.OperationGroup group) {
        Iterator<OperationGroups.OperationGroup.Operation> it = group.getOperation().iterator();
        while (it.hasNext()) {
            OperationGroups.OperationGroup.Operation next = it.next();
            if (next.getName().isEmpty()) {
                it.remove();
                break;
            }
        }
        return group;
    }

    public TypesAndOperations.IntervalGroups.IntervalGroup getIntervalGroup(String pName) {
        for (TypesAndOperations.IntervalGroups.IntervalGroup group : getTypesAndOperations().getIntervalGroups().getIntervalGroup()) {
            if (group.getName().equals(pName)) {
                return group;
            }
        }
        return null;
    }

    public List<TypesAndOperations.IntervalLimits.IntervalLimit> getAllIntervals() {
        if (allLimits == null) {
//            allLimits = getTypesAndOperations().getIntervalLimits().getIntervalLimit();
            allLimits = getIntervalsByName("m_disTimeStampIntervals");//new ArrayList<>();
//            for(TypesAndOperations.IntervalGroups.IntervalGroup.IntervalLimit limit : getIntervalGroup("m_disTimeStampIntervals").getIntervalLimit()){
//                allLimits.add(getInterval(limit.getName()));
//            }
        }
        return allLimits;
    }

    public TypesAndOperations.IntervalLimits.IntervalLimit getInterval(String value) {
        for (TypesAndOperations.IntervalLimits.IntervalLimit limit : getTypesAndOperations().getIntervalLimits().getIntervalLimit()) {
            if (limit.getName().equals(value)) {
                return limit;
            }
        }
        return null;
    }

    public List<TypesAndOperations.IntervalLimits.IntervalLimit> getIntervalsByName(String interval) {
        List<TypesAndOperations.IntervalLimits.IntervalLimit> limits = new ArrayList<>();
        for (TypesAndOperations.IntervalGroups.IntervalGroup.IntervalLimit limit : getIntervalGroup(interval).getIntervalLimit()) {
            limits.add(getInterval(limit.getName()));
        }
        return limits;
    }

    public List<TypesAndOperations.IntervalLimits.IntervalLimit> getIntervals(String interval) {
        if (interval == null || interval.isEmpty()) {
            return new ArrayList<>();
        }
        return getIntervalsByName(getIntervalGroupName(interval));
    }

    public String getIntervalGroupName(String interval) {
        for (TypesAndOperations.IntervalRules.IntervalRelation relation : getTypesAndOperations().getIntervalRules().getIntervalRelation()) {
            if (relation.getIntervalLimit().equals(interval)) {
                return relation.getIntervalGroup();
            }
        }
        return "";
    }

    public List<TypesAndOperations.FeeGroups.FeeGroup.FeeType> getFeeTypes(@NotNull String pFeeGroupName) {
        Objects.requireNonNull(pFeeGroupName, "FeeGroupCan not be null");
        for (TypesAndOperations.FeeGroups.FeeGroup feeGroup : getTypesAndOperations().getFeeGroups().getFeeGroup()) {
            if (pFeeGroupName.equals(feeGroup.getName())) {
                return feeGroup.getFeeTypes();
            }
        }
        return new ArrayList<>();
    }

    public TypesAndOperations.FeeGroups.FeeGroup.FeeType getFeeType(@NotNull String pFeeGroup, @NotNull String pIdent) {
        Objects.requireNonNull(pIdent, "Fee Group Ident can not be null");
        List<TypesAndOperations.FeeGroups.FeeGroup.FeeType> feeTypes = getFeeTypes(pFeeGroup);
        for (TypesAndOperations.FeeGroups.FeeGroup.FeeType feeType : feeTypes) {
            if (pIdent.equals(feeType.getIdent())) {
                return feeType;
            }
        }
        return null;
    }
}
