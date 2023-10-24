/*
 * Copyright (c) 2016 Lohmann & Birkner.
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
 *    2016  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.checkpoint.ruleGrouper.CRGRule;
import de.lb.cpx.grouper.model.dto.GrouperResponseObject;
import de.lb.cpx.grouper.model.transfer.BatchGroupParameter;
import de.lb.cpx.grouper.model.transfer.GrouperPerformStatistic;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferPatient;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author gerschmann
 */
@Local  // Bean class annotation that specifies local view interfaces of the Session bean.
public interface GrouperService {

    Object processGrouperRequest(TransferCase requestObject);

    Object processRuleGrouperRequest(TransferCase requestObject);
    /**
     * used for single grouping with history cases 
     * the mainCase is not null ant has to be checked with all rules. Other cases, that are in the list of history cases are grouped only
     * @param requestObject patient for this cases
     * @return one result
     */
    Object processRuleGrouperRequest(TransferPatient requestObject); // for grouping with history cases
    
    /**
     * used for batchgrouping with history cases.Each case has to be checked with its rules
     * @param request patient object with all ist cases, its mainCase is null
     * @param pbatchParam
     * @return the list of results
     * @returns the list of results
     */
    List<GrouperResponseObject>  processRuleGrouperRequest4Patient(TransferPatient request, BatchGroupParameter pbatchParam);

    Object processRuleGrouperRequest(TransferCase requestObject, GrouperPerformStatistic statistic);

    void setRuleList(CRGRule[] rules);

    void setRuleList(List<Long> ruleIds);

    void init();

    void resetRuleList();

    int getRulesCount();
    
    boolean hasHistoryRules4Year(int pYear);
}
