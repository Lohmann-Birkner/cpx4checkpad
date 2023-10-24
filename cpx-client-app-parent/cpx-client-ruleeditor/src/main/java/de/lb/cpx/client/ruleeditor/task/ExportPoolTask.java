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
import java.io.File;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author wilde
 */
public abstract class ExportPoolTask extends ExportContentTask {

    private final CrgRulePools pool;

    public ExportPoolTask(CrgRulePools pPool, File pDirectory) {
        super(pDirectory);
        pool = Objects.requireNonNull(pPool, "Pool can not be null");
        setDescription("Export von Pool: " + pool.getCrgplIdentifier() + " nach " + pDirectory.getAbsolutePath() + " wird durchgeführt");
    }

    public abstract String getPoolAsXml(CrgRulePools pPool);

    @Override
    public String getContent() {
        return getPoolAsXml(pool);
    }

    @Override
    public String createOutputFileName() {
        StringBuilder builder = new StringBuilder("Export");
        builder = builder.append("_").append(pool.getCrgplIdentifier()).append("_").append(String.valueOf(pool.getCrgplPoolYear())).append("_").append(new Date().getTime());
        builder.append(".xml");
        return builder.toString();
    }

    @Override
    public String createSuccessMessage(File pSuccess) {
        return "Export des Pools:" + pool.getCrgplIdentifier() + " nach " + pSuccess.getAbsolutePath() + " war erfolgreich!";
    }

    @Override
    public String createErrorMessage() {
        return "Export des Pools:" + pool.getCrgplIdentifier() + " ist fehlgeschlagen!";
    }

}
