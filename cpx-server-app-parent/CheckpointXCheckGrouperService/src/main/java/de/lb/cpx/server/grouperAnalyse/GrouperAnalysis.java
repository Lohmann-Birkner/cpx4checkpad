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
 *    2016  Somebody - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.grouperAnalyse;

import de.checkpoint.drg.GDRGModel;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.grouper.model.transfer.GrouperPerformStatistic;
import de.lb.cpx.server.grouperEvaluation.GrouperEvaluation;
import de.lb.cpx.service.ejb.SingleCaseGroupingStatisticRemote;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author gerschmann
 */
@Stateless
public class GrouperAnalysis implements GrouperAnalysisLocal {

    @EJB
    private SingleCaseGroupingStatisticRemote singleCaseGrouping;

    @Override
    public String performGroupAnalysis(String model, String database) {

        GrouperPerformStatistic statistic = new GrouperPerformStatistic();
        try {
            singleCaseGrouping.initDbUser(database);
        } catch (NoSuchElementException ex) {
            Logger.getLogger(GrouperEvaluation.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: could not login on database " + database;
        }

        int md = GDRGModel.getModel2Name(model).getGDRGVersion();
        try {
            statistic = singleCaseGrouping.performGroupAnalysis(statistic, md);
        } catch (CpxIllegalArgumentException ex) {
            Logger.getLogger(GrouperAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }

        return statistic.toString();
    }

}
