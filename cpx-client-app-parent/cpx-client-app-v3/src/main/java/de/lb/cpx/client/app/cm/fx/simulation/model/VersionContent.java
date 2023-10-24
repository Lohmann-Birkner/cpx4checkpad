/*
 * Copyright (c) 2017 Lohmann & Birkner.
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
 *    2017  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.simulation.model;

import de.lb.cpx.client.app.service.facade.CaseServiceFacade;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.catalog.CpxBaserate;
import de.lb.cpx.client.core.model.fx.comparablepane.ComparableContent;
import de.lb.cpx.client.core.model.task.CpxTask;
import de.lb.cpx.client.core.model.task.GroupCaseDetailsTask;
import de.lb.cpx.client.core.util.VersionStringConverter;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.shared.dto.rules.CpxSimpleRuleDTO;
import de.lb.cpx.shared.lang.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Messy Object used to manage a version (case details) and its needed content
 * for the ui in case management / simulation Expect that to change, should not
 * implement some ui elements, should modify it to a screen with controller
 * Stores the combobox needed for selecting a case details that should be shown
 * in the ui and the combobox is needed to determine the size of the sections in
 * the icd/ops, case details screens
 *
 * @author wilde
 */
public class VersionContent extends ComparableContent<TCaseDetails> {

    private static final Logger LOG = Logger.getLogger(VersionContent.class.getName());

    private final CaseServiceFacade facade;
    private final VersionManager versionManager;
    private VersionStringConverter nameConverter = new VersionStringConverter();
    private boolean grouperRunning = false;

    /**
     * @param pManager manager instance to access displayed status of the case
     * details and get access to server functions via service facade
     * @param pCaseDetails initial version to display
     */
    public VersionContent(VersionManager pManager, TCaseDetails pCaseDetails) {
        super(pCaseDetails);
        setCaseType(pCaseDetails.getHospitalCase() != null?pCaseDetails.getHospitalCase().getCsCaseTypeEn():CaseTypeEn.OTHER);
//        long start = System.currentTimeMillis();
        versionManager = pManager;
        facade = pManager.getServiceFacade();
        //if version changes grouper results must be updated, if none are found grouping will be performed
        getListenerAdapter().addChangeListener(contentProperty(), contentListener);
//        contentProperty().addListener(contentListener);
//        //if grouping result is updated rules and fee vaules will become updated
        getListenerAdapter().addChangeListener(groupingResultProperty(), grouperListener);
//        groupingResultProperty().addListener(grouperListener);
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    TimeUnit.SECONDS.sleep(10);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(VersionContent.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                initalizeGroupingResult();
//            }
//        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                initalizeGroupingResult();
//            }
//        }).start();

//        task.run();
//        reloadDetails();
//        setValuesForVersion(pCaseDetails);

        initalizeGroupingResult();
        if (pCaseDetails != null) {
            setCaseBaserate(facade.getCaseBaseRateFeeValue(pCaseDetails.getCsdAdmissionDate()));
            setCareBaserate(facade.getCareBaseRateFeeValue(pCaseDetails.getCsdAdmissionDate()));
        } else {
            LOG.log(Level.WARNING, "case details is null!");
        }
    }
    private ChangeListener<TCaseDetails> contentListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends TCaseDetails> observable, TCaseDetails oldValue, TCaseDetails newValue) {
            setNewValuesForVersion(newValue);
        }
    };
    private ChangeListener<TGroupingResults> grouperListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends TGroupingResults> observable, TGroupingResults oldValue, TGroupingResults newValue) {
//                long start = System.currentTimeMillis();
            if (newValue == null) {
//                    LOG.warning("No GroupingResults available, set ruleList to empty");
                setDetectedRules(new ArrayList<>());
                getSupplFeeValues().clear();
//                    setSupFeeValue(0.0d);
                return;
            }
            setSupplFeeValues(newValue.getGrpresType());
//                setSupFeeValue(facade.getSupplementaryFeeValue(getContent()));
            if (getContent().getCsdIsLocalFl()) {
                setDetectedRules(facade.findRulesForLocalAndResultIds(newValue.getId()));
//                    LOG.info("update grouping results in " + (System.currentTimeMillis()-start));
                return;
            }
            setDetectedRules(facade.findRulesForExternAndResultIds(newValue.getId()));
//                LOG.info("update grouping results in " + (System.currentTimeMillis()-start));
        }

        private void setSupplFeeValues(CaseTypeEn grpresType) {
            if (grpresType == null) {
                LOG.severe("grpresType is null!");
                return;
            }
            switch (grpresType) {
                case DRG:
                    setSupplFeeValue(facade.getSupplementaryFeeValue(getCaseDetails(), SupplFeeTypeEn.ZE), SupplFeeTypeEn.ZE);
                    break;
                case PEPP:
                    setSupplFeeValue(facade.getSupplementaryFeeValue(getCaseDetails(), SupplFeeTypeEn.ZP), SupplFeeTypeEn.ZP);
                    setSupplFeeValue(facade.getSupplementaryFeeValue(getCaseDetails(), SupplFeeTypeEn.ET), SupplFeeTypeEn.ET);
                    break;
                default:
                    LOG.warning("unknown type: " + grpresType.name());
            }
        }
    };

    @Override
    public void destroy() {
        super.destroy(); //To change body of generated methods, choose Tools | Templates.
        getListenerAdapter().dispose();
//        if(contentListener != null){
        contentListener = null;
//        }
////        //if grouping result is updated rules and fee vaules will become updated
//        if(grouperListener != null){
        grouperListener = null;
//        }
    }

    /**
     * get the name of the case detials currently displayed in the combobox
     *
     * @return name defined in the currently set converter of the combobox
     */
    @Override
    public String getVersionName() {
        return nameConverter.toString(getContent());
    }

    /**
     * Baserate fetched from the local database uses admission date of the
     * currentl set case details in the version property and hospital set in
     * parent case Only searches for BASE_FEE_KEY = 70000000 aka drg cases
     *
     * @return baserate catalog object
     */
    public CpxBaserate getBaserateCatalog() {
        return facade.getBaseRate(getContent().getCsdAdmissionDate());
//        return new CpxBaserate();
    }

    @Override
    public TGroupingResults performGroup() {
        return performGroup(null);
    }

    /**
     * perform group methode performes grouping WITHOUT simulation of the
     * specific drgs
     *
     * @param ruleIds list of rule ids
     * @return new grouping result with hbx flag from grouper TODO: refactor to
     * better reflect that result should be a single grouping result
     */
    @Override
    public TGroupingResults performGroup(List<Long> ruleIds) {
        grouperRunning = true;
        long start = System.currentTimeMillis();
//        TGroupingResults resultWithHbx = getLastGroupingResultFromDb();//facade.getGroupingResultForVersion(getContent(),CpxClientConfig.instance().getSelectedGrouper());
//        if (resultWithHbx == null) {
        TGroupingResults resultWithHbx = null;
        CpxTask<List<TGroupingResults>> cpxTask = new GroupCaseDetailsTask(getContent(), ruleIds);
        cpxTask.start();
        List<TGroupingResults> gResult = null;
        try {
            gResult = cpxTask.get();
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
            Thread.currentThread().interrupt();
        } catch (ExecutionException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        if (gResult != null && !gResult.isEmpty()) {
//            if (gResult.size() > 1) {
//                
//                LOG.warning("Grouper returned more than one Result! Should not happen. Only use the first");
//            }
//          

            for(TGroupingResults res: gResult){
                if(res.getCaseIcd() != null && res.getCaseIcd().getIcdcIsHdxFl()){
                    resultWithHbx = res;
                    break;
                }
            }
            
            
        }
        reloadDetails();
//        }
        LOG.log(Level.INFO, "performed group of case " + String.valueOf(facade.getCurrentCase()) + " in " + (System.currentTimeMillis() - start) + " ms, grouping result id is " + (resultWithHbx == null ? "null" : resultWithHbx.id));
        groupingResultProperty().set(resultWithHbx);
        grouperRunning = false;
        return resultWithHbx;
    }
//    /**
//     * perform group methode
//     * performes grouping WITHOUT simulation of the specific drgs 
//     * @return new grouping result with hbx flag from grouper
//     * TODO: refactor to better reflect that result should be a single grouping result
//     */
//    @Override
//    public TGroupingResults performGroup() {
//        long start = System.currentTimeMillis();
//        CpxTask<List<TGroupingResults>> cpxTask = new GroupCaseDetailsTask(getContent());
//        ((GroupCaseDetailsTask)cpxTask).start();
//        TGroupingResults resultWithHbx = null;
//        List<TGroupingResults> gResult = null;
//        try {
//            gResult = cpxTask.get();
//
//        } catch (InterruptedException | ExecutionException ex) {
//          Logger.getLogger(VersionContent.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if(gResult != null){
////            LOG.info("get result in " + (System.currentTimeMillis()-start) + " size is " + gResult.size());
//            for(TGroupingResults res : gResult){
//                
//                if(res.getCaseIcd() != null && res.getCaseIcd().getIcdcIsHdxFl()){
////                    long startSet = System.currentTimeMillis();
//                    groupingResultProperty().set(res);
////                    LOG.info("check 1 result in " + (System.currentTimeMillis()-startSet));
//                    resultWithHbx = res;
//                }
//                
//            }
////            LOG.info("fetch result in " + (System.currentTimeMillis()-start));
//        }else{
//            groupingResultProperty().set(facade.getGroupingResultForVersion(getContent(),CpxClientConfig.instance().getSelectedGrouper()));
//            resultWithHbx = groupingResultProperty().get();
//        }
//        LOG.info("perfom group in " + (System.currentTimeMillis()-start) + " ms");
//        return resultWithHbx;
//    }

    /**
     * service methdoe to save or update a icd entity
     *
     * @param pIcdEnity entity to save or update
     */
    @Override
    public void saveIcdEntity(TCaseIcd pIcdEnity) {
        facade.saveIcdEntity(pIcdEnity);
    }

    /**
     * service methode to save or update a ops entity
     *
     * @param pOpsEntity entity to save or update
     */
    @Override
    public void saveOpsEntity(TCaseOps pOpsEntity) {
        facade.saveOpsEntity(pOpsEntity);
    }

    /**
     * service methode to save or update a case details entity
     *
     * @param pVersionDetails entity to save or update
     */
    @Override
    public void saveCaseDetailsEntity(TCaseDetails pVersionDetails) {
        facade.saveCaseDetails(pVersionDetails);
    }

    /**
     * @return patient from currently loaded case, might throw lazy loading
     * exception
     */
    public TPatient getPatient() {
        return facade.getPatient();
    }

    /**
     * @return database id of the case details set in the version property
     */
    public String getVersionContentId() {
        return String.valueOf(getContent() == null ? "" : getContent().getId());
    }

    @Override
    public String toString() {
        return getVersionName();
    }

    public TCaseDetails reloadDetails() {
        TCaseDetails details = getContent();
        if (details != null) {
            TCaseDetails updated = facade.findCaseDetails(details.getId());
            details.setCsdLos(updated.getCsdLos());
            details.setCsdLeave(updated.getCsdLeave());
        }
        return details;
    }

    @Override
    public Boolean isEditable() {
        TCaseDetails details = getContent();
        if (details == null) {
            return false;
        } else {
            return details.getCsdIsLocalFl();
        }
    }

    @Override
    public Boolean isCanceled() {
        TCaseDetails details = getContent();
        if (details == null) {
            return false;
        } else {
            return details.getHospitalCase().getCsCancellationReasonEn() || (details.getCsdCancelReasonEn() != null && details.getCsdCancelDate() != null);
        }
    }

    @Override
    public int getCatalogYear() {
        //20180205-AWi:
        //After talk with GKr and AGe, switch back to get catalog year regardless of grouper by admission date of the case
//        return GrouperHelper.getYearOfValidity(getGroupingResult(),getContent());
        return Lang.toYear(getContent().getCsdAdmissionDate());
    }

    @Override
    public void deleteIcdEntity(TCaseIcd pIcd) {
        facade.deleteIcd(pIcd);
    }

    @Override
    public void deleteOpsEntity(TCaseOps pOps) {
        facade.deleteOps(pOps);
    }

    @Override
    public TCaseDetails getCaseDetails() {
        return getContent();
    }

    @Override
    public void reload() {
        reloadDetails();
    }

    //updates values after new case details / case version is selected
    private void setNewValuesForVersion(TCaseDetails newValue) {
        long startTotal = System.currentTimeMillis();
        if (contentProperty().get() == null || newValue == null) {
            return;
        }
//                long start = System.currentTimeMillis();
//        TGroupingResults results = serviceFacade.getGroupingResultForVersion(newValue,CpxClientConfig.instance().getSelectedGrouper());
////                LOG.info("get result from db in " + (System.currentTimeMillis()-start));
//        if(results == null){
//            results = performGroup();
//            LOG.warning("no groupingResult found, group anew for " + newValue.getId());
//        }
////                LOG.info("load grouperResults in " + (System.currentTimeMillis()-start));
//        groupingResultProperty().set(results);
//                LOG.info("done version update in " + (System.currentTimeMillis()-startTotal));
        TGroupingResults result = getLastGroupingResultFromDb();
        if (result == null) {
            result = performGroup();
        }
        groupingResultProperty().set(result);
        setCaseBaserate(facade.getCaseBaseRateFeeValue(newValue.getCsdAdmissionDate()));
        setCareBaserate(facade.getCareBaseRateFeeValue(newValue.getCsdAdmissionDate()));
        LOG.log(Level.FINER, "update to new version in " + (System.currentTimeMillis() - startTotal) + " ms");
    }

    @Override
    public TGroupingResults getLastGroupingResultFromDb() {
        if (getContent() == null) {
            LOG.warning("VersionContent, do not contain TCaseDetails! Abort Grouping!");
            return null;
        }
        return facade.getGroupingResultForVersion(getContent(), CpxClientConfig.instance().getSelectedGrouper());
    }

    public boolean isGrouperRunning() {
        return grouperRunning;
    }
    
    protected boolean allIcdsGrouped() {
        return facade.allIcdsGrouped(getContent(), CpxClientConfig.instance().getSelectedGrouper());
    }

    void setOrUpdateRuleSelectFlag(CpxSimpleRuleDTO pRule) {
       List<CpxSimpleRuleDTO> rules = getDetectedRules();
       if(rules != null){
           for(CpxSimpleRuleDTO rule: rules){
               if(rule.getRuleId().equals(pRule.getRuleId())){
                   rule.setSelectedRuleFl(pRule.isSelectedRuleFlag());
                   break;
               }
           }
       }
    }

}
