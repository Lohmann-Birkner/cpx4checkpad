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

import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.grouper.model.transfer.GrouperPerformStatistic;
import javax.ejb.Remote;

/**
 *
 * @author gerschmann
 */
@Remote
public interface SingleCaseGroupingStatisticRemote {
//    GrouperPerformStatistic groupCaseLocal(Long hospitalCaseId, GrouperPerformStatistic statistic);

    GrouperPerformStatistic performGroupAnalysis(GrouperPerformStatistic statistic, int modelId) throws CpxIllegalArgumentException;

    void initDbUser(String database);
}
