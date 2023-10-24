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

import de.checkpoint.ruleGrouper.CRGRule;
import de.lb.cpx.grouper.model.dto.GrouperResponseObject;
import de.lb.cpx.grouper.model.transfer.BatchGroupParameter;
import de.lb.cpx.grouper.model.transfer.GrouperPerformStatistic;
import de.lb.cpx.grouper.model.transfer.TransferBaseRate;
import de.lb.cpx.grouper.model.transfer.TransferCase;
import de.lb.cpx.grouper.model.transfer.TransferPatient;
import de.lb.cpx.server.auth.ClientManager;
import de.lb.cpx.server.commonDB.dao.CdbUserRolesDao;
import de.lb.cpx.service.grouper.GrouperRuleService;
import de.lb.cpx.service.startup_ejb.RuleReadServicBeanLocal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import org.jboss.ejb3.annotation.TransactionTimeout;

@Stateful
@Local
@TransactionManagement(TransactionManagementType.BEAN)
@TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
@TransactionTimeout(value = 360, unit = TimeUnit.MINUTES)
public class GrouperServiceBean implements GrouperService {

    private static final Logger LOG = Logger.getLogger(GrouperServiceBean.class.getName());

    @EJB(beanName = "RuleReadServiceBean")
    private RuleReadServicBeanLocal ruleStartUpbean;

    @Inject
    private CdbUserRolesDao userRoles;

//    @EJB
//    private CBaserateDao baseRate;
//    private GrouperRuleService groupRuleService = null;
    private final Map<String, GrouperRuleService> grouperService2UserId = new HashMap<>();

    @Inject
    private BaserateStore baseRateStore;

    @PostConstruct
    @Override
    public void init() {
//        if (groupRuleService == null) {
//            LOG.info("GrouperServiceBean: init: groupRuleService is null");
//            if (ruleStartUpbean == null) {
//                groupRuleService = new GrouperRuleService("", new CRGRule[0]);
//            } else {
//                groupRuleService = new GrouperRuleService("", ruleStartUpbean.getRules());
//            }
//        }
        getGroupRuleService();
        LOG.info("GrouperServiceBean: init");
    }

    private GrouperRuleService getGroupRuleService() {
        GrouperRuleService groupRuleService = grouperService2UserId.get(ClientManager.getCurrentCpxClientId());

        if (groupRuleService == null) {
            LOG.log(Level.INFO, "GrouperServiceBean: groupRuleService for {0} is null", ClientManager.getCurrentCpxClientId());
            if (ruleStartUpbean == null) {
                groupRuleService = new GrouperRuleService("", new CRGRule[0]);
            } else {
                groupRuleService = new GrouperRuleService("", ruleStartUpbean.getRules());
                groupRuleService.setRuleTypes2Ids(ruleStartUpbean.getRuleTypes2Ids());
            }
            grouperService2UserId.put(ClientManager.getCurrentCpxClientId(), groupRuleService);
        }

        return groupRuleService;
    }

    /**
     * Performs grouping for one Case
     *
     * @param requestObject CaseObject
     * @return Response Object For Grouping
     */
    @Override
    public Object processGrouperRequest(TransferCase requestObject) {
        checkBaserates(requestObject);
        return getGroupRuleService().performGroup(requestObject);
    }

    /**
     * ermittelt die Liste der Baserates
     *
     * @param requestObject
     */
    private void checkBaserates(TransferCase requestObject) {
        final long startTime = System.currentTimeMillis();
        List<TransferBaseRate> list = baseRateStore.getBaserate(requestObject);

        requestObject.setBaseRate(list);
        LOG.log(Level.FINEST, "checkBaserates: {0} ms", System.currentTimeMillis() - startTime);

    }

    @Override
    public Object processRuleGrouperRequest(TransferCase requestObject) {
        checkBaserates(requestObject);
        return processRuleGrouperRequest(requestObject, null);
    }

    /**
     * Performs grouping and rule check for one case rules for not active roles
     * in system will not be used
     *
     * @param requestObject CaseObject
     * @param statistic for gathering of statistics or null
     * @return Response Object For Grouping
     */
    @Override
    public Object processRuleGrouperRequest(TransferCase requestObject, GrouperPerformStatistic statistic) {
        long timeStart = System.currentTimeMillis();
        if (requestObject.getRoleIdsArray() == null) {
            if (requestObject.getRoleIdsArray() == null) {
                List<Long> roleIds;
                if (userRoles == null) {
                    Logger.getLogger(GrouperServiceBean.class.getName()).log(Level.WARNING, "User roles DAO is not injected, so I cannot load the role ids!");
                    roleIds = new ArrayList<>(0);
                } else {
                    roleIds = new ArrayList<>(userRoles.getAllValidRoleIds());
                }
                requestObject.setRoleIds(roleIds);
            }
        }
        if (statistic != null) {
            statistic.addTime4getAllValidRoleIds(System.currentTimeMillis() - timeStart);
        }
        checkBaserates(requestObject);
        return getGroupRuleService().performGroupAndCheck(requestObject, statistic);
    }

    /**
     * sets the defined Rules pile
     *
     * @param rules Array of rules
     */
    @Override
    public void setRuleList(CRGRule[] rules) {
        getGroupRuleService().setRuleList(rules);
    }

    @Override
    public void resetRuleList() {
        // we need the newest rule list.
        ruleStartUpbean.resetRuleList();
        getGroupRuleService().setRuleList(ruleStartUpbean.getRules());
        getGroupRuleService().setRuleTypes2Ids(ruleStartUpbean.getRuleTypes2Ids());
    }

    @Override
    public int getRulesCount() {
        return getGroupRuleService().getRuleCount();
    }

    @Override
    public void setRuleList(List<Long> ruleIds) {
        getGroupRuleService().setRuleList(ruleStartUpbean.getRule2ListId(ruleIds));
        getGroupRuleService().setRuleTypes2Ids(ruleStartUpbean.getRuleTypes2Ids());
    }

    @Override
    public boolean hasHistoryRules4Year(int pYear) {
        return getGroupRuleService().hasHistoryRules4Year(pYear);
    }

    @Override
    public Object processRuleGrouperRequest(TransferPatient requestObject) {
        if (requestObject == null) {
            return null;
        }

        if (requestObject.getHistoryCases().isEmpty() && requestObject.getMainCase() != null) {
// we don't have history cases for this patient, the main case is the only one
            return this.processRuleGrouperRequest(requestObject.getMainCase());
        }
        if (requestObject.getMainCase() != null) {
            checkBaserates(requestObject.getMainCase());
            return getGroupRuleService().performGoupAndCheckWithHistory(requestObject);
        }
        return null;

    }

    @Override
    public List<GrouperResponseObject> processRuleGrouperRequest4Patient(TransferPatient request, BatchGroupParameter pBatchParam) {
        if (request == null) {
            return null;
        }
        List<GrouperResponseObject> retList = new ArrayList<>();
        // main case set
        if (request.getMainCase() != null) {
            Object retObj = processRuleGrouperRequest(request);
            if (retObj instanceof GrouperResponseObject) {
                retList.add((GrouperResponseObject) retObj);
                return retList;
            }
        }
        if (request.getHistoryCases().isEmpty()) {
            return null;
        }
        // list of cases got, but we do not need to check with history cases
        if (!pBatchParam.isDoHistoryCases()) {

            request.getHistoryCases().forEach((trCase) -> {
                Object retObj = processRuleGrouperRequest(trCase);
                if (retObj instanceof GrouperResponseObject) {
                    retList.add((GrouperResponseObject) retObj);
                }
            });
            return retList;
        }
        // each case is to be checked with history cases
//        request.getHistoryCases().forEach((trCase) -> {
////            checkBaserates(trCase);
//            TransferPatient trPatient= new TransferPatient(request.getGrouperModelId());
//            trPatient.setMainCase(trCase);
//            for(TransferCase otherCase: request.getHistoryCases()){
//                if(!otherCase.equals(trCase)){
//                    trPatient.addHistoryCase(otherCase);
//                }
//            }
//            Object retObj = processRuleGrouperRequest(trPatient);
//            if(retObj != null&& retObj instanceof GrouperResponseObject ){
//                retList.add((GrouperResponseObject)retObj);
//            }
//        });
//        return retList;        
        request.getHistoryCases().forEach(this::checkBaserates);
//        if (request.getPatientId() == 195001) {
//            int kk = 0;
//        }
        return getGroupRuleService().performGroupAndCheckPatientCases(request.getHistoryCases(), pBatchParam);
    }
}
