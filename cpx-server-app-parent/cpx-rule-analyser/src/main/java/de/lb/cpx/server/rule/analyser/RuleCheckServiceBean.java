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
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferRuleAnalyseResult;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.service.ejb.GrouperCommunication;
import de.lb.cpx.shared.rules.enums.CaseValidationGroupErrList;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class RuleCheckServiceBean implements RuleCheckServiceBeanRemote {

    @EJB
    private RuleCheckService ruleCheckService;

    @EJB
    private GrouperCommunication grouperCommunication;

    @Override
    public TransferRuleAnalyseResult checkRule4Case(Transfer4RuleAnalyse pTransfer) throws Exception {
        return ruleCheckService.checkRule4Case(pTransfer);
    }

    @Override
    public TransferRuleAnalyseResult analyseRule(String pRule, TCase pCase) {
//        TransferCase cse = TransferCaseHelper.transformTCaseToTransferCase(pCase, true, 0);
        TransferCase cse = new TransferCase();
        TCaseDetails currentDetails = pCase.getCurrentLocal();
        List<CaseValidationGroupErrList> errorList = grouperCommunication.fillGrouperRequest(pCase, currentDetails, cse, 0, null, null, new ArrayList<>());
        if (errorList.isEmpty()) {
            Transfer4RuleAnalyse objToAnalyse = new Transfer4RuleAnalyse(cse, pRule);
            try {
                return ruleCheckService.checkRule4Case(objToAnalyse);
            } catch (Exception ex) {
                //todo: Better Exception??
                Logger.getLogger(RuleCheckServiceBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
