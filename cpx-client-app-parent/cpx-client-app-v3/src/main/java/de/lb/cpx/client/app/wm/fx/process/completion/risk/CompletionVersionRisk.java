/*
 * Copyright (c) 2020 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner Health Care Consulting GmbH and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner Health Care Consulting GmbH
 * http://www.lohmann-birkner.de
 *
 * Contributors:
 *    2020  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.completion.risk;

import de.lb.cpx.model.enums.PlaceOfRegEn;
import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.math.BigDecimal;
import java.util.logging.Logger;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class CompletionVersionRisk extends ProcessCompletionRisk<TWmRisk,TWmRiskDetails>{

    private static final Logger LOG = Logger.getLogger(CompletionVersionRisk.class.getName());
    
    public CompletionVersionRisk(TWmRisk pRisk) {
        super(pRisk);
        setCellFactory(new Callback<TWmRisk, RiskListCell<TWmRiskDetails>>() {
            @Override
            public RiskListCell<TWmRiskDetails> call(TWmRisk param) {
                return new CompletionVersionCell(param);
            }
        });
    }
    @Override
    public boolean addRiskDetail(RiskAreaEn pRiskArea){
        if(super.addRiskDetail(pRiskArea)){
            return false;
        }
        TWmRiskDetails newDetail = new TWmRiskDetails();
        newDetail.setRiskArea(pRiskArea);
        newDetail.setRiskPercent(0);
        newDetail.setRiskValue(BigDecimal.ZERO);
        if (addRiskDetail(newDetail)) {
            getAddRiskArea().call(pRiskArea);
        }
        return true;
    }

    @Override
    public Boolean addRiskDetail(TWmRiskDetails pDetail) {
        if (pDetail == null) {
            LOG.warning("can not add riskdetail, detail is null");
            return false;
        }
        if (getRisk() == null) {
            LOG.warning("can not add riskdetail, risk is null");
            return false;
        }
        pDetail.setRisk(getRisk());
        return getRisk().getRiskDetails().add(pDetail);
    }

    @Override
    public boolean hasRiskArea(RiskAreaEn pRiskArea) {
        if (pRiskArea == null) {
            LOG.warning("can not check riskdetail, riskArea is null");
            return false;
        }
        if (getRisk() == null) {
            LOG.warning("can not check riskdetail, risk is null");
            return false;
        }
        for (TWmRiskDetails detail : getRisk().getRiskDetails()) {
            if (pRiskArea.equals(detail.getRiskArea())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean removeRiskDetail(TWmRiskDetails pRiskDetail) {
        if (pRiskDetail == null) {
            LOG.warning("can not delete riskdetail, it is null");
            return false;
        }
        if (getRisk() == null) {
            LOG.warning("can not delete riskdetails, risk is null");
            return false;
        }
        if (!getRisk().getRiskDetails().contains(pRiskDetail)) {
            LOG.warning("can not delete riskdetail, risk did not contain such detail");
            return false;
        }
        if (!getCheckDetailDeletable().call(pRiskDetail)) {
            LOG.warning("can not delete riskdetail, is marked as not deletable!");
            return false;
        }
        if (getRisk().getRiskDetails().remove(pRiskDetail)) {
            getDeleteRiskArea().call(pRiskDetail.getRiskArea());
        }
        return true;
//        boolean result = riskAreas().remove(pRiskDetail);
//        FXCollections.sort(riskAreas()/*, getComparator()*/);
//        return result;
    }

    @Override
    public PlaceOfRegEn getPlaceOfRegEn() {
        return getRisk()!=null?getRisk().getRiskPlaceOfReg():null;
    }
    
    private static final Callback<TWmRiskDetails, Boolean> DEFAULT_CHECK_DETAIL_DELETABLE = new Callback<TWmRiskDetails, Boolean>() {
        @Override
        public Boolean call(TWmRiskDetails param) {
            return true;
        }
    };
    private Callback<TWmRiskDetails, Boolean> checkDetailDeletable;

    public void setCheckDetailDeletable(Callback<TWmRiskDetails, Boolean> pCallback) {
        checkDetailDeletable = pCallback;
    }

    public Callback<TWmRiskDetails, Boolean> getCheckDetailDeletable() {
        return checkDetailDeletable == null ? DEFAULT_CHECK_DETAIL_DELETABLE : checkDetailDeletable;
    }
    
    @Override
    public boolean hasComment(TWmRisk pRisk) {
        return !(pRisk.getRiskComment() == null || pRisk.getRiskComment().isEmpty());
    }

    @Override
    public void setRiskValueTotal(BigDecimal valueOf) {
        if(getRisk() == null){
            return;
        }
        getRisk().setRiskValueTotal(valueOf);
    }

    @Override
    public void setRiskPercentTotal(int intValue) {
        if(getRisk() == null){
            return;
        }
        getRisk().setRiskPercentTotal(intValue);
    }

//    @Override
//    public PlaceOfRegEn getRiskPlaceOfReg() {
//        return getRisk()!=null?getRisk().getRiskPlaceOfReg():null;
//    }

    @Override
    public Integer getRiskPercentTotal() {
        return getRisk()!=null?getRisk().getRiskPercentTotal():null;
    }

    @Override
    public BigDecimal getRiskValueTotal() {
        return getRisk()!=null?getRisk().getRiskValueTotal():null;
    }

    @Override
    public String getRiskComment(TWmRisk risk) {
        return getRisk()!=null?getRisk().getRiskComment():null;
    }
}
