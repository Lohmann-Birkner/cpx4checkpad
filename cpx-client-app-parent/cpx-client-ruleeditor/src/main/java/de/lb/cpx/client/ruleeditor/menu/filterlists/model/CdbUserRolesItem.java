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
package de.lb.cpx.client.ruleeditor.menu.filterlists.model;

import de.lb.cpx.ruleviewer.model.state.StateManager;
import de.lb.cpx.server.commonDB.model.CdbUserRoles;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author wilde
 */
public class CdbUserRolesItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private final CdbUserRoles role;
    private final StateManager<ArrayList<CrgRules>> stateManager;

    public CdbUserRolesItem(CdbUserRoles pRoles) {
        role = pRoles;
        stateManager = new RulesStateManager();//new RolesStateManager();
//        if(pRoles.getCrgtContent() != null){
//        }
    }
    private ArrayList<CrgRules> activeItems;

    public ArrayList<CrgRules> getActiveItems() {
        if (activeItems == null) {
            activeItems = fetchActiveItems(role);
        }
        return activeItems;
    }
    private ArrayList<CrgRules> availableItems;

    public ArrayList<CrgRules> getAvaiableItems() {
        if (availableItems == null) {
            availableItems = fetchAvailableItems(role);
        }
        return availableItems;
    }

    public CdbUserRoles getRole() {
        return role;
    }

    public ArrayList<CrgRules> fetchAvailableItems(CdbUserRoles pRoles) {
        return new ArrayList<>();
    }

    public ArrayList<CrgRules> fetchActiveItems(CdbUserRoles pRoles) {
        return new ArrayList<>();
    }

    public boolean isDirty() {
        if (role == null) {
            //no content loaded, can not be dirty?
            return false;
        }
        if (!stateManager.isInitialized()) {
            return false;
        }
        return !stateManager.check();
    }

    public StateManager<ArrayList<CrgRules>> getStateManager() {
        return stateManager;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CdbUserRolesItem other = (CdbUserRolesItem) obj;
        if (!Objects.equals(role, other.getRole())) {
            return false;
        }
        if (!getActiveItems().equals(other.getActiveItems())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.role);
        return hash;
    }

    public List<CrgRules> filterAvailableItems(String pFilter) {
        return filterItems(getAvaiableItems(), pFilter);
    }

    public List<CrgRules> filterItems(List<CrgRules> pItems, String pFilter) {
        if (pFilter == null) {
            return pItems;
        }
        if (pFilter.isEmpty()) {
            return pItems;
        }
        return pItems.stream().filter(new Predicate<CrgRules>() {
            @Override
            public boolean test(CrgRules t) {
                return Objects.requireNonNullElse(t.getCrgrNumber(), "").toLowerCase().contains(pFilter.toLowerCase()) || Objects.requireNonNullElse(t.getCrgrCaption(), "").toLowerCase().contains(pFilter.toLowerCase());
            }
        }).collect(Collectors.toList());
    }

    public List<CrgRules> filterActiveItems(String pFilter) {
        return filterItems(getActiveItems(), pFilter);
    }

    public void setAllActive() {
        setItemsToActive(new ArrayList<>(getAvaiableItems()));
    }

    public void setAllAvailable() {
        setItemsToAvailable(new ArrayList<>(getActiveItems()));
    }

    public void setItemsToActive(List<CrgRules> pRules) {
        getAvaiableItems().removeAll(pRules);
        getActiveItems().addAll(pRules);
        getActiveItems().size();
    }

    public void setItemsToAvailable(List<CrgRules> pRules) {
        getActiveItems().removeAll(pRules);
        getAvaiableItems().addAll(pRules);
        getAvaiableItems().size();
    }

    public List<Long> getRuleChangeSet(ArrayList<CrgRules> oldItems, ArrayList<CrgRules> newItems) {
        List<Long> diff = new ArrayList<>();
        for (CrgRules rule : oldItems) {
            if (!newItems.contains(rule)) {
                if (!diff.contains(rule.getId())) {
                    diff.add(rule.getId());
                }
            }
        }
        for (CrgRules rule : newItems) {
            if (!oldItems.contains(rule)) {
                if (!diff.contains(rule.getId())) {
                    diff.add(rule.getId());
                }
            }
        }
        return diff;
    }

    private class RulesStateManager extends StateManager<ArrayList<CrgRules>> {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean compare(ArrayList<CrgRules> pItem1, ArrayList<CrgRules> pItem2) throws IOException {
            if (pItem1 == null) {
                return false;
            }
            if (pItem2 == null) {
                return false;
            }
//            if(!pItem1.equals(pItem2)){
//                return false;
//            }
            if (pItem1.size() != pItem2.size()) {
                return false;
            }
            return true;
        }

    }

//    private class RolesStateManager extends StateManager<CdbUserRolesItem> {
//
//        private static final long serialVersionUID = 1L;
//
//        @Override
//        public boolean compare(CdbUserRolesItem pRole1, CdbUserRolesItem pRole2) throws IOException {
//            if (pRole1 == null) {
//                return false;
//            }
//            if (pRole2 == null) {
//                return false;
//            }
//            if (!pRole2.getActiveItems().equals(pRole1.getActiveItems())) {
//                return false;
//            }
////            if (!pRole1.getCrgtTableName().equals(pTable2.getCrgtTableName())) {
////                return false;
////            }
////            if (pRole1.getCrgtContent() == null) {
////                return false;
////            }
////            if (pTable2.getCrgtContent() == null) {
////                return false;
////            }
////            if (!pRole1.getCrgtContent().equals(pTable2.getCrgtContent())) {
////                return false;
////            }
//            return true;
//        }
//    }
}
