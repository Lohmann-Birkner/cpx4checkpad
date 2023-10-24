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
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.shared.filter.ColumnOption;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author wilde
 */
public abstract class ExportRulesCsvExcelTask extends ExportRulesBasicTask<File>{

    private final List<ColumnOption> columnOptions;

    public ExportRulesCsvExcelTask(List<ColumnOption> pColumnOptions,List<CrgRules> pRules, CrgRulePools pPool, File pDirectory) {
        super(pRules, pPool, pDirectory);
        columnOptions = Objects.requireNonNullElse(pColumnOptions,new ArrayList<>());
    }

    @Override
    public File call() {
        return getContent();
    }

    @Override
    public File getContent() {
        RuleListExporter exporter = getRuleListExporter();
        initializeExporter(exporter);
        return exporter.export();
    }
    
    private void initializeExporter(RuleListExporter pExporter){
        pExporter.setItems(getRules());
        pExporter.setOptions(columnOptions);
        pExporter.setPool(getPool());
        pExporter.setPoolType(getPoolType());
        pExporter.setDefaultFileName(createOutputFileName());
    }
    
    public abstract RuleListExporter getRuleListExporter();
    
}
