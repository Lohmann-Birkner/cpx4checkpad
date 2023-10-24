/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.rule.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * gives the full information about import or copy process to client
 *
 * @author gerschmann
 */
public class RuleExchangeResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<RuleExchangeError> errors = new ArrayList<>();
    private List<Long> successRuleIds = new ArrayList<>();
    private List<Long> successRuleTableIds = new ArrayList<>();
    private List<Long> successRuleTypeIds = new ArrayList<>();

    public RuleExchangeResult() {

    }

    public List<RuleExchangeError> getErrors() {
        return errors;
    }

    public void setErrors(List<RuleExchangeError> errors) {
        this.errors = errors;
    }

    public List<Long> getSuccessRuleIds() {
        return successRuleIds;
    }

    public void setSuccessRuleIds(List<Long> successRuleIds) {
        this.successRuleIds = successRuleIds;
    }

    public List<Long> getSuccessRuleTableIds() {
        return successRuleTableIds;
    }

    public void setSuccessRuleTableIds(List<Long> successRuleTableIds) {
        this.successRuleTableIds = successRuleTableIds;
    }

    public List<Long> getSuccessRuleTypeIds() {
        return successRuleTypeIds;
    }

    public void setSuccessRuleTypeIds(List<Long> successTuleTypeIds) {
        this.successRuleTypeIds = successTuleTypeIds;
    }

}
