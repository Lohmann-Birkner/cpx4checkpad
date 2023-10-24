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

import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.task.TaskService;
import de.lb.cpx.ruleviewer.util.PoolTypeHelper;
import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import de.lb.cpx.server.commonDB.model.rules.PoolTypeEn;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javafx.concurrent.Worker;

/**
 * TODO: unify this export task with xml task
 * @author wilde
 * @param <T> contentType to export
 */
public abstract class ExportRulesBasicTask<T> extends TaskService<File> implements BasicExportTask<T>{

    private final List<CrgRules> rules;
    private final CrgRulePools pool;
    private final File directory;

    public ExportRulesBasicTask(List<CrgRules> pRules, CrgRulePools pPool, File pDirectory) {
        super();
        rules = Objects.requireNonNull(pRules, "Rules can not be null");
        pool = Objects.requireNonNull(pPool, "Pool can not be null");
        directory = pDirectory;
//        setDescription("Export Regel(n): "+ getSelectedRuleNumbers() +" aus Pool: " + pool.getCrgplIdentifier() + " nach " + pDirectory.getAbsolutePath() + " wird durchgeführt");
        setDescription("Export der ausgewähler Regel(n) (" + rules.size() + ") aus Pool: " + pool.getCrgplIdentifier() + " nach " + pDirectory.getAbsolutePath() + " wird durchgeführt");
    }
    public File getDirectory(){
        return directory;
    }
    public List<CrgRules> getRules(){
        return rules;
    }
    public CrgRulePools getPool(){
        return pool;
    }
    public PoolTypeEn getPoolType(){
        return PoolTypeHelper.getPoolType(pool);
    }
    @Override
    public String createErrorMessage() {
//        return "Export der Regel(n):" + getSelectedRuleNumbers() + " ist fehlgeschlagen!";
        return "Export der ausgewählten Regel(n) (" + rules.size() + ") ist fehlgeschlagen!";
    }

    @Override
    public String createSuccessMessage(File pFile) {
        return "Export der ausgewählten Regel(n) (" + rules.size() + ") aus Pool: " + pool.getCrgplIdentifier() + " nach " + pFile.getAbsolutePath() + " war erfolgreich!";
    }

    @Override
    public String createOutputFileName() {
        StringBuilder builder = new StringBuilder("Export");
        builder = builder.append("_").append(pool.getCrgplIdentifier()).append("_").append(String.valueOf(pool.getCrgplPoolYear())).append("_").append(new Date().getTime());
        return builder.toString();
    }
    
    @Override
    public void afterTask(Worker.State pState) {
        super.afterTask(pState);
        if (Worker.State.SUCCEEDED.equals(pState)) {
            File success = getValue();
            if (success != null) {
                MainApp.showInfoMessageDialog(createSuccessMessage(success));
            }
        }
    }
   
    
}
