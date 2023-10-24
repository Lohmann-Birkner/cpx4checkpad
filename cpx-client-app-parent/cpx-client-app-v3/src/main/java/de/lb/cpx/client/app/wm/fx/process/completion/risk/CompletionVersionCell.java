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

import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.math.BigDecimal;

/**
 *
 * @author wilde
 */
public class CompletionVersionCell extends RiskListCell<TWmRiskDetails>{

    private final TWmRisk risk;

    public CompletionVersionCell(TWmRisk pRisk){
        risk = pRisk;
    }

    @Override
    public Integer getRiskPercent(TWmRiskDetails pDetails) {
        return pDetails!=null?pDetails.getRiskPercent():null;
    }

    @Override
    public void setRiskPercent(TWmRiskDetails pDetails, int intValue) {
        if(pDetails==null){
            return;
        }
        pDetails.setRiskPercent(intValue);
    }

    @Override
    public BigDecimal getRiskValue(TWmRiskDetails pDetails) {
        return pDetails!=null?pDetails.getRiskValue():null;
    }

    @Override
    public void setRiskValue(TWmRiskDetails pDetails, BigDecimal valueOf) {
        if(pDetails==null){
            return;
        }
        pDetails.setRiskValue(valueOf);
    }

    @Override
    public String getRiskComment(TWmRiskDetails pDetails) {
        return pDetails!=null?pDetails.getRiskComment():null;
    }

    @Override
    public TWmRiskDetails getDetailForArea(RiskAreaEn pArea) {
        if (risk == null) {
            return null;
        }
        if (pArea == null) {
            return null;
        }
        for (TWmRiskDetails detail : risk.getRiskDetails()) {
            if (pArea.equals(detail.getRiskArea())) {
                return detail;
            }
        }
        return null;
    }

}
