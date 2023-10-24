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
import javax.ejb.LocalBean;

/**
 *
 * @author gerschmann
 */
@LocalBean
public interface RuleCheckService {

    TransferRuleAnalyseResult checkRule4Case(Transfer4RuleAnalyse pTransfer) throws Exception;

    void init();

}
