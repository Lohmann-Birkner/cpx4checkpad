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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.lb.cpx.shared.rules.util.RuleValidationStatusEn.ErrPrioEn;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gerschmann
 */
public class RulesImportStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private RuleValidationStatusEn endImportStatus = RuleValidationStatusEn.SUCCESS;
    private Map<String, List<RuleValidationLog>> importStatus = new HashMap<>();
    private String importMessage = "";
    private String errorXml = null;

    public RulesImportStatus() {

    }

    public void addRuleImportResult(RuleValidationLog ruleElem, RuleValidationStatusEn status) {
        List<RuleValidationLog> lst = importStatus.get(status.toString());
        if (!ErrPrioEn.INFO.equals(status.getErrPrio()) && RuleValidationStatusEn.SUCCESS.equals(endImportStatus)) {
            setEndImportStatus(RuleValidationStatusEn.COLLISION);
        }
        if (lst == null) {
            lst = new ArrayList<>();
            importStatus.put(status.toString(), lst);
        }
        lst.add(ruleElem);
    }

    /**
     * Create a error message with empty message
     *
     * @param pStatus status
     * @param pMessage message
     */
    public void addErrorStatus(RuleValidationStatusEn pStatus, String pMessage) {
        List<RuleValidationLog> lst = importStatus.get(pStatus);
        setImportMessage(pMessage);
    }

    public String getJsonString() {

        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RulesImportStatus.class, new RulesImportStatusSerializer());
        gsonBuilder.setPrettyPrinting();
        final Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }

    public RulesImportStatus getRulesImportStatus() {
        RulesImportStatus rulesImportStatus = null;
//        try {
        rulesImportStatus = this;
//        } catch (Exception ex) {
//            LOG.log(Level.SEVERE, "Can't get RulesImportStatus", ex);
//        }
        return rulesImportStatus;
    }

    public RuleValidationStatusEn getEndImportStatus() {
        return endImportStatus;
    }

    public void setEndImportStatus(RuleValidationStatusEn endImportStatus) {
        this.endImportStatus = endImportStatus;
    }

    public Map<String, List<RuleValidationLog>> getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(Map<String, List<RuleValidationLog>> importStatus) {
        this.importStatus = importStatus;
    }

    public String getImportMessage() {
        return importMessage;
    }

    public void setImportMessage(String importMessage) {
        this.importMessage = importMessage;
    }

    public String getErrorXml() {
        return errorXml;
    }

    public void setErrorXml(String errorXml) {
        this.errorXml = errorXml;
    }

    

}
