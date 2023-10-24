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
package de.lb.cpx.server.rule.analyser;

import de.lb.cpx.grouper.model.transfer.Transfer4RuleAnalyse;
import de.lb.cpx.grouper.model.transfer.TransferRuleAnalyseResult;
import de.lb.cpx.model.TCase;
import javax.ejb.Remote;

/**
 *
 * @author gerschmann
 */
@Remote
public interface RuleCheckServiceBeanRemote {

    /**
     * checks case with rule configuration and returns result for each rule term
     * which has a mark attribute
     *
     * @param pTransfer transfer
     * @return transfer rule result
     * @throws Exception error
     */
    TransferRuleAnalyseResult checkRule4Case(Transfer4RuleAnalyse pTransfer) throws Exception;

    TransferRuleAnalyseResult analyseRule(String pRule, TCase pCase);
}
