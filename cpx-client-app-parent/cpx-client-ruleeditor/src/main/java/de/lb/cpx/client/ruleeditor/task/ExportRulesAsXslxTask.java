/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.ruleeditor.task;

import de.lb.cpx.client.ruleeditor.export.RuleListExporter;
import de.lb.cpx.client.ruleeditor.export.RuleListXslxExporter;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.filter.ColumnOption;
import java.io.File;
import java.util.List;

/**
 *
 * @author wilde
 */
public class ExportRulesAsXslxTask extends ExportRulesCsvExcelTask{

    public ExportRulesAsXslxTask(List<ColumnOption> pColumnOptions, List<CrgRules> pRules, CrgRulePools pPool, File pDirectory) {
        super(pColumnOptions, pRules, pPool, pDirectory);
    }

    @Override
    public RuleListExporter getRuleListExporter() {
        return new RuleListXslxExporter();
    }
    
}
