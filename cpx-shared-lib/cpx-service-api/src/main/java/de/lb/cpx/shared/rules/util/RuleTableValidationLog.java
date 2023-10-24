/*
 * Copyright (c) 2018 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2018  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.shared.rules.util;

/**
 *
 * @author gerschmann
 */
public class RuleTableValidationLog extends RuleValidationLog {

    private static final long serialVersionUID = 1L;

    private String tableName = "";
    private String oldContent = ""; // table content found in DB
    private String newContent = ""; // table content imported from XML

    public RuleTableValidationLog(String tableName) {
        this(tableName, "", "");
    }

    public RuleTableValidationLog(String tableName, String oldContent, String newContent) {
        super("");
        this.tableName = tableName;
        this.oldContent = oldContent;
        this.newContent = newContent;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOldContent() {
        return oldContent;
    }

    public void setOldContent(String oldContent) {
        this.oldContent = oldContent;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

}
