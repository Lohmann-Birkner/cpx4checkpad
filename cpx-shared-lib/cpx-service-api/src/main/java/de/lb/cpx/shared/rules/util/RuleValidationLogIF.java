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

import de.lb.cpx.server.commons.enums.RuleOverrideFlags;
import java.io.Serializable;
import javax.ejb.Remote;

/**
 *
 * @author gerschmann
 */
@Remote
public abstract class RuleValidationLogIF implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ruleIdent = "";
    private RuleOverrideFlags decision;
    private RuleValidationStatusEn statusMessage;

    protected RuleValidationLogIF(String ident) {
        ruleIdent = ident;
    }

    public void setDecision(RuleOverrideFlags doWhat) {
        decision = doWhat;
    }

    public RuleOverrideFlags getDecision() {
        return decision;
    }

    public RuleValidationStatusEn getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(RuleValidationStatusEn statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getRuleIdentNr() {
        return ruleIdent;
    }

    public void setRuleIdentNr(String ruleIdentNr) {
        this.ruleIdent = ruleIdentNr;
    }
}
