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
import de.lb.cpx.grouper.model.transfer.BatchGroupParameter;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.server.dao.TCaseDao;
import de.lb.cpx.service.startup_ejb.RuleReadServicBeanLocal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author gerschmann
 */
@Stateless
@Local
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class BatchGroupingBean implements BatchGrouping {

    private static final Logger LOG = Logger.getLogger(BatchGroupingBean.class.getName());

    @EJB(beanName = "GrouperServiceBean")
    private GrouperService grouperService;

    @EJB(beanName = "RuleReadServiceBean")
    private RuleReadServicBeanLocal ruleStartUpbean;

    @EJB
    private TCaseDao caseDao;

    @EJB
    private GrouperCommunication grouperCommunication;

//    @Inject
//    private PrepStorer prepStorer;
    @Override
    public Object processGrouperRequest(TCase hospitalCase, BatchGroupParameter batchGroupParameter) throws CpxIllegalArgumentException {

        TransferCase requestObject = new TransferCase();
        requestObject.setBatchGrouperParameter(batchGroupParameter);

        TCase attachedCase = caseDao.findById(hospitalCase.getId());

        grouperCommunication.fillGrouperRequest(attachedCase, requestObject, batchGroupParameter.doInclHis(), batchGroupParameter.getModelId());

        Object responseObject = grouperService.processGrouperRequest(requestObject);
        if (responseObject instanceof GrouperResponseObject) {
            grouperCommunication.fillGrouperResults(attachedCase, (GrouperResponseObject) responseObject);
        }

        return responseObject;

    }

//    @Override
//    public Object[] processRuleGrouperRequest(TCase hospitalCase, BatchGroupParameter batchGroupParameter) {
//
//        Object[] result = new Object[2];
////        long timestamp = System.currentTimeMillis();
////        long timestamp1 = System.currentTimeMillis();
//        TransferCase requestObject = new TransferCase();
//        requestObject.setBatchGrouperParameter(batchGroupParameter);
////        Logger.getLogger(BatchGroupingBean.class.getName()).log(Level.INFO, "setBatchGrouperParameter: casenumber =  " + hospitalCase.getCsCaseNumber()
////                + " time for case grouping = " + String.valueOf(System.currentTimeMillis() - timestamp));
////        timestamp = System.currentTimeMillis();
//        TCase attachedCase = caseDao.findById(hospitalCase.getId());
////        Logger.getLogger(BatchGroupingBean.class.getName()).log(Level.INFO, "caseDao.findById: casenumber =  " + hospitalCase.getCsCaseNumber()
////                + " time for case grouping = " + String.valueOf(System.currentTimeMillis() - timestamp));
////        timestamp = System.currentTimeMillis();
//        grouperCommunication.fillGrouperRequest(attachedCase, requestObject, batchGroupParameter.doInclHis(), batchGroupParameter.getModelId());
////        Logger.getLogger(BatchGroupingBean.class.getName()).log(Level.INFO, "GrouperCommunication.fillGrouperRequest: casenumber =  " + hospitalCase.getCsCaseNumber()
////                + " time for case grouping = " + String.valueOf(System.currentTimeMillis() - timestamp));
//        Object responseObject;
//
//        if (batchGroupParameter.isDoRules() || batchGroupParameter.isDoRulesSimulate()) {
////            long timestamp2 = System.currentTimeMillis();
//            if (batchGroupParameter.getRuleIds() != null && ruleStartUpbean != null) {
//                grouperService.setRuleList(ruleStartUpbean.getRule2ListId(batchGroupParameter.getRuleIds()));
//            }
////            Logger.getLogger(BatchGroupingBean.class.getName()).log(Level.INFO, "grouperService.setRuleList: casenumber =  " + hospitalCase.getCsCaseNumber()
////                    + " time for case grouping = " + String.valueOf(System.currentTimeMillis() - timestamp));
////            timestamp = System.currentTimeMillis();
//            responseObject = grouperService.processRuleGrouperRequest(requestObject);
////            Logger.getLogger(BatchGroupingBean.class.getName()).log(Level.INFO, "grouperService.processRuleGrouperRequest: casenumber =  " + hospitalCase.getCsCaseNumber()
////                    + " time for case grouping = " + String.valueOf(System.currentTimeMillis() - timestamp));
//        } else {
//            // group only
////            timestamp = System.currentTimeMillis();
//            responseObject = grouperService.processGrouperRequest(requestObject);
////            Logger.getLogger(BatchGroupingBean.class.getName()).log(Level.INFO, "grouperService.processGrouperRequest: casenumber =  " + hospitalCase.getCsCaseNumber()
////                    + " time for case grouping = " + String.valueOf(System.currentTimeMillis() - timestamp));
//        }
//        result[0] = responseObject;
//        result[1] = new ArrayList<TGroupingResults>();
////        long timestamp3 = System.currentTimeMillis();
//        if (responseObject instanceof GrouperResponseObject) {
//            ArrayList<TGroupingResults> groupingResultsToDelete = grouperCommunication.fillGrouperResults(attachedCase, (GrouperResponseObject) responseObject).get("toRemove");
//            result[1] = groupingResultsToDelete;
//        }
////        Logger.getLogger(BatchGroupingBean.class.getName()).log(Level.INFO, "GrouperCommunication.fillGrouperResults: casenumber =  " + hospitalCase.getCsCaseNumber()
////                + " time for case grouping = " + String.valueOf(System.currentTimeMillis() - timestamp3));
////        Logger.getLogger(BatchGroupingBean.class.getName()).log(Level.INFO, "gesamt: casenumber =  " + hospitalCase.getCsCaseNumber()
////                + " time for case grouping = " + String.valueOf(System.currentTimeMillis() - timestamp1));
//        return result;
//    }
    @Override
    public Object[] processRuleGrouperRequest(TCase hospitalCase, BatchGroupParameter batchGroupParameter) throws CpxIllegalArgumentException {
        if (hospitalCase == null) {
            throw new IllegalArgumentException("hospitalCase is null!");
        }
        Object[] result = new Object[2];
        TransferCase requestObject = new TransferCase();
        requestObject.setBatchGrouperParameter(batchGroupParameter);
        TCase attachedCase = caseDao.findById(hospitalCase.getId());
        final boolean groupingRequestPerformed = grouperCommunication.fillGrouperRequest(attachedCase, requestObject, batchGroupParameter.doInclHis(), batchGroupParameter.getModelId());

        //2018-03-01 DNi - Ticket #CPX-848: Stop further processing if no grouping request was performed! (strange errors can occur if we would follow the path here)
        if (!groupingRequestPerformed) {
            LOG.log(Level.WARNING, "No grouping performed for this case: " + hospitalCase.getCaseKey() + " (id " + String.valueOf(hospitalCase.getId()) + ")");
            return result;
        }

        Object responseObject;
        if (batchGroupParameter.isDoRules() || batchGroupParameter.isDoRulesSimulate()) {
            if (batchGroupParameter.getRuleIds() != null && ruleStartUpbean != null) {
                grouperService.setRuleList(ruleStartUpbean.getRule2ListId(batchGroupParameter.getRuleIds()));
            }
            responseObject = grouperService.processRuleGrouperRequest(requestObject); //don't to this without proper grouping request!
        } else {
            responseObject = grouperService.processGrouperRequest(requestObject);
        }
        result[0] = responseObject;
        result[1] = new ArrayList<>();
        if (responseObject instanceof GrouperResponseObject) {
            List<TGroupingResults> groupingResultsToDelete = grouperCommunication.fillGrouperResults(attachedCase, (GrouperResponseObject) responseObject).get("toRemove");
            result[1] = groupingResultsToDelete;
        }
        return result;
    }

//    @Override
//    public Object[] processRuleGrouperRequest2(TCase pHospitalCase, BatchGroupParameter pParameter, boolean pIsLocal) throws CpxIllegalArgumentException {
////       long totalStart = System.currentTimeMillis();
////        long start = System.currentTimeMillis();
//        Object[] result = new Object[1];
//        Object responseObject;
//        TransferCase requestObject = new TransferCase();
//        requestObject.setBatchGrouperParameter(pParameter);
////        long start = System.currentTimeMillis();
//        final boolean groupingRequestPerformed = grouperCommunication.fillGrouperRequest2(pHospitalCase, requestObject, pIsLocal, pParameter.getModelId());
////        Logger.getLogger(BatchGroupingBean.class.getName()).log(Level.INFO, "fill grouperRequest " + String.valueOf(System.currentTimeMillis() - start));
////        start = System.currentTimeMillis();
//
//        //2018-03-01 DNi - Ticket #CPX-848: Stop further processing if no grouping request was performed! (strange errors can occur if we would follow the path here)
//        if (!groupingRequestPerformed) {
//            LOG.log(Level.WARNING, "No grouping performed for this case: " + (pHospitalCase == null ? "NULL" : pHospitalCase.getCaseKey()) + " (id " + (pHospitalCase == null ? "NULL" : String.valueOf(pHospitalCase.getId())) + ")");
//            return result;
//        }
//
//        if (pParameter.isDoRules() || pParameter.isDoRulesSimulate()) {
//            if (pParameter.getRuleIds() != null && ruleStartUpbean != null) {
////                long timestamp2 = System.currentTimeMillis();
//                grouperService.setRuleList(ruleStartUpbean.getRule2ListId(pParameter.getRuleIds()));
////                Logger.getLogger(BatchGroupingBean.class.getName()).log(Level.INFO, "getRules " + String.valueOf(System.currentTimeMillis() - timestamp2));
//            }
//            responseObject = grouperService.processRuleGrouperRequest(requestObject); //don't to this without proper grouping request!
//        } else {
//            // group only
//            responseObject = grouperService.processGrouperRequest(requestObject);
//        }
////        Logger.getLogger(BatchGroupingBean.class.getName()).log(Level.INFO, "group in " + String.valueOf(System.currentTimeMillis() - start));
////        start = System.currentTimeMillis();
//        result[0] = responseObject;
////        result[1] = new ArrayList<TGroupingResults>();
//        if (responseObject instanceof GrouperResponseObject) {
//            Map<String, List<TGroupingResults>> results = grouperCommunication.fillGrouperResults3(pHospitalCase,
//                    pIsLocal ? pHospitalCase.getLocalCopyOfCurrentLocal() : pHospitalCase.getLocalCopyOfCurrentExtern(),
//                    (GrouperResponseObject) responseObject);
////            result[1] = results.get("toRemove");
////            result[2] = results.get("newResults");
//            Iterator<TGroupingResults> it = results.get("toRemove").iterator();
//            while (it.hasNext()) {
//                TGroupingResults grpRes = it.next();
//                try {
//                    prepStorer.deleteGroupingResult(grpRes);
//                } catch (SQLException ex) {
//                    Logger.getLogger(BatchGroupingBean.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//
//            Iterator<TGroupingResults> it2 = results.get("newResults").iterator();
//            while (it2.hasNext()) {
//                TGroupingResults grpRes = it2.next();
//                try {
//                    prepStorer.insertGroupingResult(grpRes);
//                } catch (SQLException ex) {
//                    Logger.getLogger(BatchGroupingBean.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
////        Logger.getLogger(BatchGroupingBean.class.getName()).log(Level.INFO, "fill result in " + String.valueOf(System.currentTimeMillis() - start));
////        Logger.getLogger(BatchGroupingBean.class.getName()).log(Level.INFO, "total grouping " + String.valueOf(System.currentTimeMillis() - totalStart) + " for case: " + pHospitalCase.getCsCaseNumber());
//        return result;
//    }
}
