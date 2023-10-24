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
import de.lb.cpx.server.commonDB.model.rules.CrgRuleTables;
import java.io.IOException;
import java.util.Objects;

/**
 *
 * @author wilde
 */
public class CrgRuleTablesItem {

    private final CrgRuleTables table;
    private final RuleTableStateManager stateManager;

    public CrgRuleTablesItem(CrgRuleTables pTable) {
        table = pTable;
        stateManager = new RuleTableStateManager();
        if (pTable.getCrgtContent() != null) {
            stateManager.init(pTable);
        }
    }

    public CrgRuleTables getTable() {
        return table;
    }

    public boolean isDirty() {
        if (table.getCrgtContent() == null) {
            //no content loaded, can not be dirty?
            return false;
        }
        return !stateManager.check();
    }

    public StateManager<CrgRuleTables> getStateManager() {
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
        final CrgRuleTablesItem other = (CrgRuleTablesItem) obj;
        if (!Objects.equals(this.table, other.table)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.table);
        return hash;
    }

    private class RuleTableStateManager extends StateManager<CrgRuleTables> {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean compare(CrgRuleTables pTable1, CrgRuleTables pTable2) throws IOException {
            if (pTable1 == null) {
                return false;
            }
            if (pTable2 == null) {
                return false;
            }
            if (!pTable1.getCrgtTableName().equals(pTable2.getCrgtTableName())) {
                return false;
            }
            if (pTable1.getCrgtContent() == null) {
                return false;
            }
            if (pTable2.getCrgtContent() == null) {
                return false;
            }
            if (!pTable1.getCrgtContent().equals(pTable2.getCrgtContent())) {
                return false;
            }
            return true;
        }
    }

}
