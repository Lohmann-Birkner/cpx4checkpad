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
package de.lb.cpx.client.ruleeditor.task;

import de.lb.cpx.server.commonDB.model.rules.CrgRulePools;
import de.lb.cpx.server.commonDB.model.rules.CrgRules;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author wilde
 */
public abstract class ExportRulesTask extends ExportContentTask {

    private final List<CrgRules> rules;
    private final CrgRulePools pool;

    public ExportRulesTask(List<CrgRules> pRules, CrgRulePools pPool, File pDirectory) {
        super(pDirectory);
        rules = Objects.requireNonNull(pRules, "Rules can not be null");
        pool = Objects.requireNonNull(pPool, "Pool can not be null");

//        setDescription("Export Regel(n): "+ getSelectedRuleNumbers() +" aus Pool: " + pool.getCrgplIdentifier() + " nach " + pDirectory.getAbsolutePath() + " wird durchgeführt");
        setDescription("Export der ausgewähler Regel(n) (" + rules.size() + ") aus Pool: " + pool.getCrgplIdentifier() + " nach " + pDirectory.getAbsolutePath() + " wird durchgeführt");
    }

    public abstract String getRulesAsXml(List<CrgRules> pRules);

    @Override
    public String getContent() {
        return getRulesAsXml(rules);
    }

    @Override
    public String createErrorMessage() {
//        return "Export der Regel(n):" + getSelectedRuleNumbers() + " ist fehlgeschlagen!";
        return "Export der ausgewählten Regel(n) (" + rules.size() + ") ist fehlgeschlagen!";
    }

    @Override
    public String createSuccessMessage(File pFile) {
        return "Export der asugewählten Regel(n) (" + rules.size() + ") aus Pool: " + pool.getCrgplIdentifier() + " nach " + pFile.getAbsolutePath() + " war erfolgreich!";
    }

    @Override
    public String createOutputFileName() {
        StringBuilder builder = new StringBuilder("Export");
        builder = builder.append("_").append(pool.getCrgplIdentifier()).append("_").append(String.valueOf(pool.getCrgplPoolYear())).append("_").append(new Date().getTime());
        builder.append(".xml");
        return builder.toString();
    }
//    public final String getSelectedRuleNumbers(){
//        String numbers = rules.stream()
//                .map( n -> n.getCrgrNumber() )
//                .collect(Collectors.joining( "," ));
//        return numbers;
//    }
}
