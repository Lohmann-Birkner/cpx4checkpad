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
package de.lb.cpx.client.app.job.fx.casemerging.model;

import de.lb.cpx.client.app.service.facade.CaseMergingFacade;
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.fx.comparablepane.ComparableContent;
import de.lb.cpx.exceptions.CpxIllegalArgumentException;
import de.lb.cpx.model.TCase;
import de.lb.cpx.model.TCaseDetails;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.TCaseOps;
import de.lb.cpx.model.TGroupingResults;
import de.lb.cpx.model.enums.CaseTypeEn;
import de.lb.cpx.model.enums.SupplFeeTypeEn;
import de.lb.cpx.shared.lang.Lang;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Compareable content for case merging based on TCase-Entity
 *
 * @author wilde
 */
public class CaseMergeContent extends ComparableContent<TCase> {

    private static final Logger LOG = Logger.getLogger(CaseMergeContent.class.getName());

    private boolean isMergedCase = false;
    private final CaseMergingFacade facade;

    public CaseMergeContent(TCase pContent, TGroupingResults pGrpResult, CaseMergingFacade pFacade) {
        super(pContent);
        facade = pFacade;
        isMergedCase = pContent.getId() == 0;
        this.setCaseType(pContent.getCsCaseTypeEn());
        groupingResultProperty().addListener(new ChangeListener<TGroupingResults>() {
            @Override
            public void changed(ObservableValue<? extends TGroupingResults> observable, TGroupingResults oldValue, TGroupingResults newValue) {
                setSupplFeeValues(newValue.getGrpresType());
                setDetectedRules(facade.findSortedDetectedRules(contentProperty().get().getCurrentLocal().getCsdAdmissionDate(), newValue));
                getCaseDetails().setCsdLeave((int) newValue.getCalculatedLeave());
                getCaseDetails().setCsdLos(newValue.getCalculatedLengthOfStay());
                newValue.setCaseDetails(getCaseDetails());
            }

            private void setSupplFeeValues(CaseTypeEn grpresType) {
                switch (grpresType) {
                    case DRG:
                        if (isMergedCase()) {
                            setSupplFeeValue(facade.getSupplementaryFeeValue(getGroupingResult(), SupplFeeTypeEn.ZE), SupplFeeTypeEn.ZE);
                        } else {
                            setSupplFeeValue(facade.getSupplementaryFeeValue(getCaseDetails(), SupplFeeTypeEn.ZE), SupplFeeTypeEn.ZE);
                        }
                        break;
                    case PEPP:
                        if (isMergedCase) {
                            setSupplFeeValue(facade.getSupplementaryFeeValue(getGroupingResult(), SupplFeeTypeEn.ZP), SupplFeeTypeEn.ZP);
                            setSupplFeeValue(facade.getSupplementaryFeeValue(getGroupingResult(), SupplFeeTypeEn.ET), SupplFeeTypeEn.ET);
                        } else {
                            setSupplFeeValue(facade.getSupplementaryFeeValue(getCaseDetails(), SupplFeeTypeEn.ZP), SupplFeeTypeEn.ZP);
                            setSupplFeeValue(facade.getSupplementaryFeeValue(getCaseDetails(), SupplFeeTypeEn.ET), SupplFeeTypeEn.ET);
                        }
                        break;
                    default:
                        LOG.warning("unknown type: " + grpresType.name());
                }
            }
        });
        initalizeGroupingResult();
        setCaseBaserate(facade.getCaseBaseRateFeeValue(pContent));
        setCareBaserate(facade.getCareBaseRateFeeValue(pContent));
        contentProperty().addListener(new ChangeListener<TCase>() {
            @Override
            public void changed(ObservableValue<? extends TCase> observable, TCase oldValue, TCase newValue) {
                setCaseBaserate(facade.getCaseBaseRateFeeValue(pContent));
                setCareBaserate(facade.getCareBaseRateFeeValue(pContent));
            }
        });

//        TGroupingResults dbResult = pGrpResult!=null ? pGrpResult : facade.findGrouperResult(getCaseDetails().getId(),CpxClientConfig.instance().getSelectedGrouper());
//        //set leave and los in grouping result for ui update
//        if(dbResult!= null && !isMergedCase){
//            dbResult.setCalculatedLeave(getCaseDetails().getCsdLeave());
//            dbResult.setCalculatedLengthOfStay(getCaseDetails().getCsdLos());
//        }
//        setGroupingResult(dbResult!=null?dbResult:performGroup());
        //facade.findGrouperResult(getCaseDetails().getId(),CpxClientConfig.instance().getSelectedGrouper());
        //set leave and los in grouping result for ui update
        if (!isMergedCase) {
            TGroupingResults dbResult = facade.findGrouperResult(pGrpResult.getId());
            dbResult.setCalculatedLeave(getCaseDetails().getCsdLeave());
            dbResult.setCalculatedLengthOfStay(getCaseDetails().getCsdLos());
            setGroupingResult(dbResult);
            return;
        }
        setGroupingResult(performGroup());
    }

    public CaseMergeContent(TCase pContent, CaseMergingFacade pFacade) {
        this(pContent, null, pFacade);
    }

    @Override
    public void saveIcdEntity(TCaseIcd pIcd) {
//        facade.saveIcdEntity(pIcd);
    }

    @Override
    public void saveOpsEntity(TCaseOps pOps) {
//        facade.saveOpsEntity(pOps);
    }

    @Override
    public Boolean isEditable() {
        return getContentId() == 0;
    }

    @Override
    public Boolean isCanceled() {
        return getContent().getCsCancellationReasonEn();

    }

    @Override
    public int getCatalogYear() {
        return Lang.toYear(getContent().getCurrentLocal().getCsdAdmissionDate());
    }

    @Override
    public String getVersionName() {
        return !isMergedCase ? getContent().getCsCaseNumber() : Lang.getCaseMergingCaseObj().getAbbreviation();
    }

    @Override
    public TGroupingResults performGroup() {
        if (isMergedCase) {
            LOG.finer("perform group for " + getVersionName());
            TGroupingResults result;
            try {
                result = facade.getGrouperResult(getContent());
//                result.setCalculatedLeave(getCaseDetails().getCsdLeave());
//                result.setCalculatedLengthOfStay(getCaseDetails().getCsdLos());
                //???
                //do not understand increment of the id
//                result.setId((getGroupingResult()!=null?getGroupingResult().getId():0)+1);
                setGroupingResults(result.getCaseDetails());
                setGroupingResult(result);
            } catch (CpxIllegalArgumentException ex) {
                LOG.log(Level.SEVERE, null, ex);
                return null;
            }
        }
        return groupingResultProperty().get();//getGroupingResult();
    }

    public boolean isMergedCase() {
        return isMergedCase;
    }

//    private TCaseIcd getMd(TCase pCase, boolean pLocal) {
//        TCaseDetails version;
//        if (pLocal) {
//            version = pCase.getCurrentLocal();
//        } else {
//            version = pCase.getCurrentExtern();
//        }
//        for (TCaseDepartment dep : version.getCaseDepartments()) {
//            for (TCaseIcd icd : dep.getCaseIcds()) {
//                if (icd.getIcdcIsHdxFl()) {
//                    return icd;
//                }
//            }
//        }
//        return null;
//    }
    @Override
    public void deleteIcdEntity(TCaseIcd pIcd) {
        facade.deleteIcd(getCaseDetails(), pIcd);
    }

    @Override
    public void deleteOpsEntity(TCaseOps pOps) {
        facade.deleteOps(getCaseDetails(), pOps);
    }

    @Override
    public TCaseDetails getCaseDetails() {
        return getContent().getCurrentLocal();
    }

    @Override
    public void reload() {
        try {
            facade.reloadContent(getContent());
        } catch (CpxIllegalArgumentException ex) {
            LOG.log(Level.WARNING, "Can not reload data for current content! ContentId {0}", getContent().getId());
            LOG.log(Level.WARNING, null, ex);
        }
    }

    @Override
    public void saveCaseDetailsEntity(TCaseDetails pDetails) {
        facade.saveCaseDetailsEntity(pDetails);
    }

    @Override
    public TGroupingResults getLastGroupingResultFromDb() {
        if(groupingResultProperty().get() == null){
            performGroup();
        }
        return groupingResultProperty().get();
    }

    @Override
    public TGroupingResults performGroup(List<Long> ruleIds) {
        return performGroup();
    }
    
//    public CaseTypeEn getCaseType(){
//        return contentProperty().get() == null || contentProperty().get().getCsCaseTypeEn() == null?CaseTypeEn.OTHER:contentProperty().get().getCsCaseTypeEn();
//    }

    private void setGroupingResults(TCaseDetails caseDetails) {
        if(isMergedCase){
            // we got only one local case, which we replace with the same one, but with grouping results for all icds
            Set<TCaseDetails> details = getContent().getCaseDetails();
            for(TCaseDetails detail: details){
                if(detail.getCsdIsLocalFl()){
                    details.remove(detail);
                    break;
                }
            }
            details.add(caseDetails);
            getContent().setCaseDetails(details);
        }
    }

       
    protected boolean allIcdsGrouped() {
        return facade.allIcdsGrouped(getCaseDetails(), CpxClientConfig.instance().getSelectedGrouper());
    }

}
