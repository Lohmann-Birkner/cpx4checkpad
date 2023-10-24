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
import de.lb.cpx.wm.model.TWmFinalisationRisk;
import de.lb.cpx.wm.model.TWmFinalisationRiskDetail;
import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Logger;
import javafx.util.Callback;

/**
 *
 * @author wilde
 */
public class CompletionFinalRisk extends ProcessCompletionRisk<TWmFinalisationRisk,TWmFinalisationRiskDetail>{

    private static final Logger LOG = Logger.getLogger(CompletionFinalRisk.class.getName());
    private Map<RiskAreaEn, BigDecimal> riskAreaValueMap;
    private final Callback<RiskAreaEn,BigDecimal> riskDefaultValueCallback;
    public CompletionFinalRisk(TWmFinalisationRisk pRisk) {
        super(pRisk);
        this.riskDefaultValueCallback = new Callback<RiskAreaEn,BigDecimal>(){
            @Override
            public BigDecimal call(RiskAreaEn param) {
                if(riskAreaValueMap == null){
                    return BigDecimal.ZERO;
                }
                BigDecimal val = riskAreaValueMap.get(param);
                return val!=null?val:BigDecimal.ZERO;
            }
        };
        setCellFactory(new Callback<TWmFinalisationRisk, RiskListCell<TWmFinalisationRiskDetail>>() {
            @Override
            public RiskListCell<TWmFinalisationRiskDetail> call(TWmFinalisationRisk param) {
                CompletionRiskCell cell = new CompletionRiskCell(param);
                cell.setRiskDefaultValueCallback(riskDefaultValueCallback);
                return cell;
            }
        });
    }

    @Override
    public PlaceOfRegEn getPlaceOfRegEn() {
        return PlaceOfRegEn.REQUEST_FINALISATION;
    }

    @Override
    public boolean addRiskDetail(RiskAreaEn pRiskArea) {
        if(!super.addRiskDetail(pRiskArea)){
            return false;
        }
        TWmFinalisationRiskDetail newDetail = new TWmFinalisationRiskDetail();
        newDetail.setRiskArea(pRiskArea);
//        newDetail.setRiskPercent(0);
        newDetail.setRiskValue(riskDefaultValueCallback.call(pRiskArea));
        if (addRiskDetail(newDetail)) {
            getAddRiskArea().call(pRiskArea);
        }
        return true;
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
        for (TWmFinalisationRiskDetail detail : getRisk().getFinalisationRiskDetails()) {
            if (pRiskArea.equals(detail.getRiskArea())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean addRiskDetail(TWmFinalisationRiskDetail pRiskDetail) {
        if (pRiskDetail == null) {
            LOG.warning("can not add riskdetail, detail is null");
            return false;
        }
        if (getRisk() == null) {
            LOG.warning("can not add riskdetail, risk is null");
            return false;
        }
        pRiskDetail.setFinalisRisk(getRisk());
        return getRisk().getFinalisationRiskDetails().add(pRiskDetail);
    }

    @Override
    public Boolean removeRiskDetail(TWmFinalisationRiskDetail pRiskDetail) {
        if (pRiskDetail == null) {
            LOG.warning("can not delete riskdetail, it is null");
            return false;
        }
        if (getRisk() == null) {
            LOG.warning("can not delete riskdetails, risk is null");
            return false;
        }
        if (!getRisk().getFinalisationRiskDetails().contains(pRiskDetail)) {
            LOG.warning("can not delete riskdetail, risk did not contain such detail");
            return false;
        }
        if (!getCheckDetailDeletable().call(pRiskDetail)) {
            LOG.warning("can not delete riskdetail, is marked as not deletable!");
            return false;
        }
        if (getRisk().getFinalisationRiskDetails().remove(pRiskDetail)) {
            getDeleteRiskArea().call(pRiskDetail.getRiskArea());
        }
        return true;
    }
    private static final Callback<TWmFinalisationRiskDetail, Boolean> DEFAULT_CHECK_DETAIL_DELETABLE = new Callback<TWmFinalisationRiskDetail, Boolean>() {
        @Override
        public Boolean call(TWmFinalisationRiskDetail param) {
            return true;
        }
    };
    private Callback<TWmFinalisationRiskDetail, Boolean> checkDetailDeletable;

    public void setCheckDetailDeletable(Callback<TWmFinalisationRiskDetail, Boolean> pCallback) {
        checkDetailDeletable = pCallback;
    }

    public Callback<TWmFinalisationRiskDetail, Boolean> getCheckDetailDeletable() {
        return checkDetailDeletable == null ? DEFAULT_CHECK_DETAIL_DELETABLE : checkDetailDeletable;
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
//        if(getRisk() == null){
//            return;
//        }
//        getRisk().setRiskPercentTotal(intValue);        
    }

//    @Override
//    public PlaceOfRegEn getRiskPlaceOfReg() {
//        return PlaceOfRegEn.REQUEST_FINALISATION;
//    }

    @Override
    public Integer getRiskPercentTotal() {
        return null;
//        return getRisk()!=null?getRisk().getRiskValueTotal():null;
    }

    @Override
    public BigDecimal getRiskValueTotal() {
        return getRisk()!=null?getRisk().getRiskValueTotal():null;
    }

    @Override
    public boolean hasComment(TWmFinalisationRisk risk) {
        return !(risk.getRiskComment() == null || risk.getRiskComment().isEmpty());
    }

    @Override
    public String getRiskComment(TWmFinalisationRisk risk) {
        return getRisk()!=null?getRisk().getRiskComment():null;
    }

    public void setRiskAreaValueMap(Map<RiskAreaEn, BigDecimal> lastRequestRiskAreaValues) {
        riskAreaValueMap = lastRequestRiskAreaValues;
    }
    
}
