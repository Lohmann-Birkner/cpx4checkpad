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
package de.lb.cpx.rule.criteria.model;

import de.lb.cpx.rule.criteria.CriteriaHelper;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group;
import de.lb.cpx.rule.criteria.model.CriterionTree.Supergroup.Group.Criterion;
import de.lb.cpx.shared.lang.Lang;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * extends pojo class with utility methodes to handle enum handling this should
 * not be affected of changes to pojo class!
 *
 * @author wilde
 */
public class Criteria extends CriterionTree {

    private static final Logger LOG = Logger.getLogger(Criteria.class.getName());
    protected Map<String, Criterion> criterionMap;

    /**
     * @param pName cpName, displayed text of the criterion
     * @return indicator if criteria is stored in this tree
     */
    public boolean hasCriterion(String pName) {
        if (pName == null) {
            return false;
        }
        long start = System.currentTimeMillis();
        if (getSupergroup().getGroup().stream().anyMatch((group) -> (group.getCriterion().stream().anyMatch((criterion) -> (criterion.getCpname().equals(pName) || getDisplayName(criterion).equals(pName)))))) {
            LOG.info("has criterion in " + (System.currentTimeMillis() - start));
            return true;
        }
        return false;
    }

    /**
     * @param pName kriterion cp name
     * @return checks if criterion is in map by key
     */
    public boolean containsCriterion(String pName) {
        return criterionMap().containsKey(pName);
    }

    public static  String getDisplayName(Criterion pCriterion) {
        return CriteriaHelper.getDisplayName(pCriterion);
    }

    public Criterion getCriterion(String pName) {
        if (pName == null) {
            return null;
        }
        long start = System.currentTimeMillis();
        for (Group group : getSupergroup().getGroup()) {
            for (Criterion criterion : group.getCriterion()) {
                if (criterion.getCpname().equals(pName) || getDisplayName(criterion).equals(pName)) {
                    LOG.info("find criterion in " + (System.currentTimeMillis() - start));
                    return criterion;
                }
            }
        }
        return null;
    }

    public Group getParentGroup(String pCriterionName) {
        if (pCriterionName == null) {
            return null;
        }
        for (Group group : getSupergroup().getGroup()) {
            for (Criterion criterion : group.getCriterion()) {
                if (criterion.getCpname().equals(pCriterionName)) {
                    return group;
                }
            }
        }
        return null;
    }

    public Map<String, Criterion> criterionMap() {
        if (criterionMap == null) {
            criterionMap = new HashMap<>();
            for (Group group : getSupergroup().getGroup()) {
                for (Criterion criterion : group.getCriterion()) {
                    criterionMap.put(criterion.getCpname(), criterion);
                }
            }
        }
        return criterionMap;
    }

    public Criterion getCriterionFromMap(String pName) {
        return criterionMap().get(pName);
    }

    public boolean hasCriterionFromMap(String pName) {
        return criterionMap().containsKey(pName);
    }
//    private Map<String,OperationGroups.OperationGroup> operationsMap;
//    
//    private Map<String,OperationGroups.OperationGroup> getOperationsMap(){
//        if(operationsMap == null){
//            operationsMap = new HashMap<>();
//            initOperationsMap();
//        }
//        return operationsMap;
//    }
//
//    private void initOperationsMap() {
//        for(OperationGroups.OperationGroup group : getOperationGroups().getOperationGroup()){
//            operationsMap.put(group.getName(), group);
//        }
//    }
//    public final OperationGroups.OperationGroup getOperationGroup(String pGroupName){
//        return getOperationsMap().get(pGroupName);
//    }
//    public final List<OperationGroups.OperationGroup.Operation> getOperations(String pGroupName){
//        OperationGroups.OperationGroup group = getOperationGroup(pGroupName);
//        if(group != null){
//            return group.getOperation();
//        }
//        return new ArrayList<>();
//    }

}
