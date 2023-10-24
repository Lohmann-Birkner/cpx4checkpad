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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.grouper;

import de.lb.cpx.service.helper.FailureCallback;
import de.lb.cpx.service.helper.Callback;
import de.lb.cpx.grouper.model.dto.GrouperResponseObject;
import de.lb.cpx.grouper.model.transfer.BatchGroupParameter;
import de.lb.cpx.grouper.model.transfer.BatchGroupResult;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferPatient;
import de.lb.cpx.grouper.model.transfer.TransferRule;
import de.lb.cpx.model.TBatchCheckResult;
import de.lb.cpx.model.TBatchResult;
import de.lb.cpx.model.TBatchResult2Role;
import de.lb.cpx.service.ejb.GrouperService;
import de.lb.cpx.service.helper.ResponseLoader;
import de.lb.cpx.service.startup_ejb.RuleReadServicBeanLocal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

/**
 *
 * @author niemeier
 */
@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
//@AccessTimeout(value=60000)
public class GrouperResponseLoader extends ResponseLoader{

    private int counter = 0;

    private static final Logger LOG = Logger.getLogger(GrouperResponseLoader.class.getName());
//    private AtomicBoolean stopSignal;
//    private AtomicInteger responseLoaderStopped;
//    private Callback stoppedCb;
//    private boolean stopped = false;

    @Inject
    private GrouperService grouperService;
    @EJB(beanName = "RuleReadServiceBean")
    private RuleReadServicBeanLocal ruleReadServiceBean;

//    @Lock(LockType.READ)
    public void start(
            final BatchGroupParameter pParameter,
            //            final GrouperCallback grouperCb,
            final BlockingQueue<TransferPatient> pSourceQueue,
            final BlockingQueue<GrouperResponseObject> pTargetQueue,
            final AtomicInteger caseCount,
            final AtomicInteger caseDetailsCount,
            final AtomicBoolean stopSignal,
            final Callback stoppedCb,
            final AtomicInteger responseLoaderStopped,
            final Callback responseLoaderFinishedCb,
            final AtomicBoolean requestLoaderFinished,
            final FailureCallback failureCb,
            final AtomicReference<TBatchResult> batchResult,
            final AtomicLong timer) {
        this.stopSignal = stopSignal;
        this.responseLoaderStopped = responseLoaderStopped;
        this.stoppedCb = stoppedCb;

//        this.grouperService = ClientManager.lookup(GrouperService.class);
//        this.ruleReadServiceBean = ClientManager.lookup(RuleReadServicBeanLocal.class);
        TransferPatient request;
        try {
            if (pParameter.getRuleIds() != null) {
                //                long timestamp2 = System.currentTimeMillis();
                grouperService.setRuleList(ruleReadServiceBean.getRule2ListId(pParameter.getRuleIds()));
                LOG.log(Level.INFO, "rules: {0}", grouperService.getRulesCount());
            } else {
                grouperService.resetRuleList();
                LOG.log(Level.INFO, "rules: {0}", grouperService.getRulesCount());
            }
            TBatchResult br =  batchResult.get();
            if(br != null){
                br.setRulesCount(grouperService.getRulesCount()); 
            }
            while (true) {
                if (checkStopped()) {
                    return;
                }
                while ((request = pSourceQueue.poll()) != null) {
                    if (stopSignal.get()) {
                        break;
                    }
                    final long timeStart = System.nanoTime();
                    //                LOG.log(Level.INFO, "Pick request from queue");
                    try {

                        if (pParameter.isDoHistoryCases()) {
                            List<GrouperResponseObject> responseList = processRuleGrouperRequest4Patient(request, pParameter);
                            if (responseList != null) {
                                for (GrouperResponseObject respObject : responseList) {
                                    addOneResponse(respObject,
                                            pParameter,
                                            pTargetQueue,
                                            timer,
                                            batchResult,
                                            timeStart);
                                }
                            }
                        } else {
                            List<TransferCase> patCases = request.getHistoryCases();
                            for (TransferCase trCase : patCases) {
                                final GrouperResponseObject response;
                                if (pParameter.isDoRules() || pParameter.isDoRulesSimulate()) {

                                    response = processRuleGrouperRequest(trCase);

                                    //response = grouperService.processRuleGrouperRequest(request); //don't to this without proper grouping request!
                                    //LOG.log(Level.FINE, "grouperService.processRuleGrouperRequest (with rules or simulation): " + (System.currentTimeMillis() - start) + " ms");
                                } else {
                                    // group only
                                    //                            response = grouperService.processGrouperRequest(request);
                                    response = processGrouperRequest(trCase);
                                    //LOG.log(Level.FINE, "grouperService.processRuleGrouperRequest (without rules and simulation): " + (System.currentTimeMillis() - start) + " ms");
                                }
                                addOneResponse(response,
                                        pParameter,
                                        pTargetQueue,
                                        timer,
                                        batchResult,
                                        timeStart);
//                                if (response != null) {
//                                                    //                        LOG.log(Level.INFO, "Put response on queue");
//                                    if (response.getResult().getIsLocal()) {
//                                        BatchGroupResult result = response.getBatchResult();
//                                        if (result != null) {
//                                            addResult4OneCase(batchResult.get(), result, response.getDetectedRules(), pParameter.getRoleIds());
//                                        }
//                                    }
//                                    while (pTargetQueue.remainingCapacity() <= 0) {
//                                        Thread.sleep(500L);
//                                        if (checkStopped()) {
//                                            return;
//                                        }
//                                    }
//                                    timer.addAndGet(System.nanoTime() - timeStart);
//                                    pTargetQueue.put(response);
//                            //                            LOG.log(Level.INFO, "put in response in queue queue size = " + pTargetQueue.remainingCapacity());
//                                    counter++;
//                            //                            LOG.log(Level.INFO, "case counter =" + counter);
//                            //                            if (counter % 1000 == 0) {
//                            //                                LOG.log(Level.INFO, "Created response for case details " + counter);
//                            //                                //                                sendStatusJobMessage(executionId, phase.get(), caseNo /* number of files written */, caseCount /* of total number of files */, batchstatus, "Schreibe Fall " + String.format(java.util.Locale.GERMAN, "%,d", caseNo) + "/" + String.format(java.util.Locale.GERMAN, "%,d", caseDetailsCount) + " in die CSV-Dateien...");
//                            //                            }
//                                }
                            }
                        }

                    } catch (InterruptedException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                        failureCb.execute("error occured in GrouperResponseLoader for case " + request.getPatientId(), ex);
                        stopSignal.set(true);
                        responseLoaderStopped.incrementAndGet();
                        stopped = true;
                        stoppedCb.execute();
                        Thread.currentThread().interrupt();
                        return;
                    }catch(Exception ex){
                        LOG.log(Level.SEVERE, "error occured in GrouperResponseLoader for case " + request.getPatientId() + " we go on with other cases", ex);
                        failureCb.execute("error occured in GrouperResponseLoader for case " + request.getPatientId(), ex);
                        
                    }
                }
                if (requestLoaderFinished.get() && pSourceQueue.isEmpty()) {
                    break;
                } else {
//                    LOG.log(Level.INFO, "GrouperResponseLoader is waiting...");
                    Thread.sleep(500L);
                }
            }
            responseLoaderFinishedCb.execute();
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
            failureCb.execute("error occured in GrouperResponseLoader", ex);
            stopSignal.set(true);
            responseLoaderStopped.incrementAndGet();
            stopped = true;
            stoppedCb.execute();
            Thread.currentThread().interrupt();
        } 
        finally {
            if (!stopped) {
                responseLoaderStopped.incrementAndGet();
                stopped = true;
            }
        }
    }
//
//    private boolean checkStopped() {
//        if (stopSignal.get()) {
//            responseLoaderStopped.incrementAndGet();
//            stopped = true;
//            stoppedCb.execute();
//            return true;
//        }
//        return false;
//    }

    private void addOneResponse(GrouperResponseObject response,
            BatchGroupParameter pParameter,
            BlockingQueue<GrouperResponseObject> pTargetQueue,
            AtomicLong timer,
            AtomicReference<TBatchResult> batchResult,
            long timeStart) throws InterruptedException {

        if (response != null) {
            //                        LOG.log(Level.INFO, "Put response on queue");
//            if (response.getResult().getIsLocal()) {
            BatchGroupResult result = response.getBatchResult();
            if (result != null) {
                addResult4OneCase(batchResult.get(), result, response.getDetectedRules(), pParameter.getRoleIds());
            }
//            }
            while (pTargetQueue.remainingCapacity() <= 0) {
                Thread.sleep(500L);
                if (checkStopped()) {
                    return;
                }
            }
            timer.addAndGet(System.nanoTime() - timeStart);
            pTargetQueue.put(response);
            //                            LOG.log(Level.INFO, "put in response in queue queue size = " + pTargetQueue.remainingCapacity());
            counter++;
            //                            LOG.log(Level.INFO, "case counter =" + counter);
            //                            if (counter % 1000 == 0) {
            //                                LOG.log(Level.INFO, "Created response for case details " + counter);
            //                                //                                sendStatusJobMessage(executionId, phase.get(), caseNo /* number of files written */, caseCount /* of total number of files */, batchstatus, "Schreibe Fall " + String.format(java.util.Locale.GERMAN, "%,d", caseNo) + "/" + String.format(java.util.Locale.GERMAN, "%,d", caseDetailsCount) + " in die CSV-Dateien...");
            //                            }
        }

    }

    public GrouperResponseObject processRuleGrouperRequest(TransferCase pTransferCase) {
        Object result = grouperService.processRuleGrouperRequest(pTransferCase);
        if (result instanceof GrouperResponseObject) {
            return (GrouperResponseObject) result;
        }
        return null;
    }

    private List<GrouperResponseObject> processRuleGrouperRequest4Patient(TransferPatient request, BatchGroupParameter pParameter) {
        return grouperService.processRuleGrouperRequest4Patient(request, pParameter);

    }

    public GrouperResponseObject processGrouperRequest(TransferCase pTransferCase) {
        Object result = grouperService.processGrouperRequest(pTransferCase);
        if (result instanceof GrouperResponseObject) {
            return (GrouperResponseObject) result;
        }
        return null;
    }

    /**
     * adds result of grouping of one case to the batch result
     *
     * @param batchCaseResult Object of TBatchResult which gathers the Results
     * of Batchgrouping
     * @param batchGroupResult Results of grouping of one case
     * @param rules list of rules for this case
     * @param roleIds role ids for which will be gegrouped
     */
//    public void addResult4OneCase(TBatchResult batchCaseResult, BatchGroupResult batchGroupResult, List<TransferRule> rules) {
//        addResult4OneCase(batchCaseResult, batchGroupResult, rules, null);
//    }
//    
//    
    public static void addResult4OneCase(TBatchResult batchCaseResult,
            BatchGroupResult batchGroupResult,
            List<TransferRule> rules, List<Long> roleIds) {
        if (batchGroupResult.isHtp()) {
            batchCaseResult.addBatchresHtpCount();
        }
        if (batchGroupResult.isLtp()) {
            batchCaseResult.addBatchresLtpCount();
        }

        if (batchGroupResult.isTransfer()) {
            batchCaseResult.addBatchresTransfCount();
        }

        batchCaseResult.addBatchresPcclSum(batchGroupResult.getPccl());
        batchCaseResult.addBatchresCwEffectivSum(batchGroupResult.getCwEff());
        batchCaseResult.addBatchresCwCatalogSum(batchGroupResult.getCwCatalog());
        if (batchGroupResult.isDead()) {
            batchCaseResult.addBatchresDeadCount();
        }
        if (batchGroupResult.isDayCare()) {
            batchCaseResult.addBatchresDayCareCount();
        }
        batchCaseResult.addBatchresCareDaysSum(batchGroupResult.getCareDays());
        batchCaseResult.addBatchresAuxdCount(batchGroupResult.getAuxIcdCount());
        batchCaseResult.addBatchresLosIntensivSum(batchGroupResult.getLosIntensiv());
        if (batchGroupResult.isIntensiv()) {
            batchCaseResult.addBatchresCaseIntensivCount();
        }
        if (batchGroupResult.isGrouped()) {
            batchCaseResult.addBatchresGroupedCount();
        }
        if (batchGroupResult.isErrDrg()) {
            batchCaseResult.addBatchresErrDrgCount();
        }
        batchCaseResult.addBatchresCaseCount();
        batchCaseResult.addBatchresAux9Count(batchGroupResult.getAux9count());
        batchCaseResult.addBatchresNalosSum(batchGroupResult.getNalos());

        Map<Long, TBatchResult2Role> role2RuleResult = batchCaseResult.getResult2Role();

        TBatchResult2Role allRoles = role2RuleResult.get(0L);
        if (allRoles == null) {
            allRoles = new TBatchResult2Role();
            allRoles.setRoleId(0L);
            allRoles.setBatchResult(batchCaseResult);
            batchCaseResult.getBatchres2role().add(allRoles);
            role2RuleResult.put(0L, allRoles);
        }
        if (roleIds != null) {
            for (Long id : roleIds) {
                TBatchResult2Role r2r = role2RuleResult.get(id);
                if (r2r == null) {
                    r2r = new TBatchResult2Role();
                    r2r.setRoleId(id);
                    r2r.setBatchResult(batchCaseResult);
                    batchCaseResult.getBatchres2role().add(r2r);
                    role2RuleResult.put(id, r2r);
                }

            }
        }
        Map<Long, TBatchResult2Role> minMax2rule = new HashMap<>();
        if (rules != null) {
            for (TransferRule rule : rules) {
                // allRoles - add rule results
                long[] roles = rule.getRoles();
                if (roles != null) {
                    // find 
                    for (long role : roles) {

                        if (rule.getUsedRoles() == null || rule.getUsedRoles().contains(role)) {
//                            TBatchResult2Role res = role2RuleResult.computeIfAbsent(role, (t) -> {
//                                TBatchResult2Role result = new TBatchResult2Role();
//                                result.setRoleId(role);
//                                result.setBatchResult(batchCaseResult);
//                                batchCaseResult.getBatchres2role().add(result);
//                                return result;
//                            });
                            TBatchResult2Role res = role2RuleResult.get(role);
                            if (res == null) {
                                res = new TBatchResult2Role();
                                res.setRoleId(role);
                                role2RuleResult.put(role, res);
                                res.setBatchResult(batchCaseResult);
                                batchCaseResult.getBatchres2role().add(res);
                            }
//                            TBatchResult2Role tmpRes = minMax2rule.computeIfAbsent(role, (t) -> {
//                                TBatchResult2Role tmpResult = new TBatchResult2Role();
//                                return tmpResult;
//                            });
                            TBatchResult2Role tmpRes = minMax2rule.get(role);
                            if (tmpRes == null) {
                                tmpRes = new TBatchResult2Role();
                                minMax2rule.put(role, tmpRes);
                            }

                            // add rule results for role
                            addRuleData(res, rule);
                            //checkMinMax
                            checkMinMax(tmpRes, rule);
                        }
                    }
                }

                // add rule results for allRoles
                addRuleData(allRoles, rule);
            }
// add min/max for role2result   

            Set<Long> usedRoles = minMax2rule.keySet();
            usedRoles.forEach((role) -> {
                TBatchResult2Role res = role2RuleResult.get(role);
                TBatchResult2Role tmpRes = minMax2rule.get(role);
                if (res != null && tmpRes != null) {
                    res.setB2rMaxDcwPosSum(res.getB2rMaxDcwPosSum() + tmpRes.getB2rMaxDcwPosSum());
                    res.setB2rMinDcwNegSum(res.getB2rMinDcwNegSum() + tmpRes.getB2rMinDcwNegSum());
                    res.setB2rMaxDfeePosSum(res.getB2rMaxDfeePosSum() + tmpRes.getB2rMaxDfeePosSum());
                    res.setB2rMinDfeeNegSum(res.getB2rMinDfeeNegSum() + tmpRes.getB2rMinDfeeNegSum());
                }
            });
        }
    }

    private static void checkMinMax(TBatchResult2Role role2result, TransferRule rule) {
        if (role2result.getB2rMaxDcwPosSum() < rule.getDcw()) {
            role2result.setB2rMaxDcwPosSum(rule.getDcw());
        }
        if (role2result.getB2rMinDcwNegSum() > rule.getDcw()) {
            role2result.setB2rMinDcwNegSum(rule.getDcw());
        }
        if (role2result.getB2rMaxDfeePosSum() < rule.getDfee()) {
            role2result.setB2rMaxDfeePosSum(rule.getDfee());
        }
        if (role2result.getB2rMinDfeeNegSum() > rule.getDfee()) {
            role2result.setB2rMinDfeeNegSum(rule.getDfee());
        }
    }

    /**
     * distributes the rule results to the different roles
     */
    private static void addRuleData(TBatchResult2Role role2result, TransferRule rule) {

        Map<String, TBatchCheckResult> type2checkResult = role2result.getType2checkResult();

// TODO:  rule.getRuleErrType() is always null, to fill? from rule.getRuleDescrTypeID()  fill CRGRuleTypes csruletypes.xml?      
        TBatchCheckResult checkResult = type2checkResult.get(rule.getRuleErrType());
        if (checkResult == null) {
            checkResult = new TBatchCheckResult();
            checkResult.setBcheckresRuleType(rule.getRuleErrType());
            role2result.getBatchCheckResult().add(checkResult);
            checkResult.setBatchResult2Role(role2result);
            type2checkResult.put(rule.getRuleErrType(), checkResult);
        }
        switch (rule.getRuleType()) {
            case STATE_WARNING:
                checkResult.setBchechresWarnCount(checkResult.getBchechresWarnCount() + 1);
                break;
            case STATE_ERROR:
                checkResult.setBchechresErrCount(checkResult.getBchechresErrCount() + 1);
                break;
            case STATE_SUGG:
                checkResult.setBatchresAdviceCount(checkResult.getBatchresAdviceCount() + 1);
                break;
            default:
                LOG.log(Level.FINEST, "Unknown rule type: {0}", rule.getRuleType());
        }
    }

}
