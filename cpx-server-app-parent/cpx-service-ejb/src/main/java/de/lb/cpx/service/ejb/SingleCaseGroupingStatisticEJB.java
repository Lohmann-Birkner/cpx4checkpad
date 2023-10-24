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
 *    2016  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.ejb;

import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.grouper.model.dto.GrouperResponseObject;
import de.lb.cpx.grouper.model.transfer.GrouperPerformStatistic;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.dao.TCaseDao;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class SingleCaseGroupingStatisticEJB implements SingleCaseGroupingStatisticRemote {

    private static final Logger LOG = Logger.getLogger(SingleCaseGroupingStatisticEJB.class.getName());

    @EJB
    private TCaseDao hospitalCaseDao;
    @EJB
    private GrouperService grouperService;
    @EJB
    private GrouperCommunication grouperCommunication;

    private GrouperPerformStatistic groupCaseLocal(Long hospitalCaseId, GrouperPerformStatistic statistic, int modelId) throws CpxIllegalArgumentException {

        long timeStart = System.currentTimeMillis();
        TCase hospitalCase = hospitalCaseDao.findById(hospitalCaseId);
        if (hospitalCase == null) {
            LOG.severe("Can't find HospitalCase for Id " + hospitalCaseId + "! Abort Groupring");
            return statistic;
        }
        statistic.addTime4getCaseFromDB(System.currentTimeMillis() - timeStart);
        LOG.info("SingleCaseGroupingStatisticEJB: " + hospitalCaseId + " hospitalCaseDao.findById" + ((System.currentTimeMillis() - timeStart)));
        timeStart = System.currentTimeMillis();
        TransferCase requestObject = new TransferCase();
        grouperCommunication.fillGrouperRequest(hospitalCase, requestObject, true, modelId);
        requestObject.addRoleId(1L);
        statistic.addTime4fillInput(System.currentTimeMillis() - timeStart);
        LOG.info("SingleCaseGroupingStatisticEJB: GrouperCommunication.fillGrouperRequest" + ((System.currentTimeMillis() - timeStart)));
        timeStart = System.currentTimeMillis();
        Object responceObject = grouperService.processRuleGrouperRequest(requestObject, statistic);
        statistic.addTime4group(System.currentTimeMillis() - timeStart);
        LOG.info("SingleCaseGroupingStatisticEJB: grouperService.processRuleGrouperRequest" + ((System.currentTimeMillis() - timeStart)));

        if (responceObject instanceof GrouperResponseObject) {
            timeStart = System.currentTimeMillis();
            List<TGroupingResults> results = grouperCommunication.fillGrouperResults(hospitalCase, (GrouperResponseObject) responceObject).get("toRemove");
            printResults(results, "old results to be deleted");

//            groupingResultsDao.deleteOldResults(results);
            statistic.addTime4fillOutput(System.currentTimeMillis() - timeStart);
            LOG.info("SingleCaseGroupingStatisticEJB: GrouperCommunication.fillGrouperResults" + ((System.currentTimeMillis() - timeStart)));
        }
        timeStart = System.currentTimeMillis();
        try {
            Set<TGroupingResults> results = hospitalCase.getCurrentLocal().getGroupingResultses();
            printResults(results, "new results to be persistet");
            hospitalCaseDao.merge(hospitalCase);
            results = hospitalCase.getCurrentLocal().getGroupingResultses();
            printResults(results, "new results after merge");
        } catch (RuntimeException ex) {
//            ex.printStackTrace();
            LOG.log(Level.INFO, "SingleCaseGroupingStatisticEJB: merge failed with ex " + ex.getMessage(), ex);
            LOG.info("SingleCaseGroupingStatisticEJB: print Grouping Results for failed Case with Id " + hospitalCase.getId()
                    + " size of GroupingResults computed for Case " + hospitalCase.getCurrentLocal().getGroupingResultses().size());
            for (TGroupingResults gr : hospitalCase.getCurrentLocal().getGroupingResultses()) {
                LOG.info("SingleCaseGroupingStatisticEJB: groupingResults Id " + gr.getId());
            }
        }
        statistic.addTime4Merge(System.currentTimeMillis() - timeStart);
        LOG.info("SingleCaseGroupingStatisticEJB: " + ((System.currentTimeMillis() - timeStart)));
        return statistic;
    }

    private void printResults(Collection<TGroupingResults> results, String comment) {
        LOG.info(comment);
        Iterator<TGroupingResults> itr = results.iterator();
        while (itr.hasNext()) {
            TGroupingResults gr = itr.next();
            LOG.info("id = " + gr.getId()
                    + " icdId = " + gr.getCaseIcd().getId() + " " + gr.getCaseIcd().getIcdcCode()
                    + " caseDatailsId = "
                    + gr.getCaseDetails().getId()
                    + " drg/pepp = " + gr.getGrpresCode()
                    + " type = " + gr.getGrpresType()
            );
        }

    }

    @Override
    public GrouperPerformStatistic performGroupAnalysis(GrouperPerformStatistic statistic, int modelId) throws CpxIllegalArgumentException {
        List<Long> caseIds = hospitalCaseDao.getAllCasesIds();

        if (!caseIds.isEmpty()) {

            statistic.setCaseCount(caseIds.size());
            for (Long id : caseIds) {
                long timestamp = System.currentTimeMillis();
                statistic = groupCaseLocal(id, statistic, modelId);
                statistic.addTime4allWithId(System.currentTimeMillis() - timestamp);
                statistic.increaseGroupCount();
            }

        }
        return statistic;
    }

    @Override
    public void initDbUser(String database) {
        ClientManager.createJobSession(database);
    }

}
