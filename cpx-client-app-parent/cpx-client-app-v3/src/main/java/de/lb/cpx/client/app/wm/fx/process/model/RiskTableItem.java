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
package de.lb.cpx.client.app.wm.fx.process.model;

import de.lb.cpx.server.commonDB.model.enums.RiskAreaEn;
import de.lb.cpx.wm.model.TWmRisk;
import de.lb.cpx.wm.model.TWmRiskDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author wilde
 */
public class RiskTableItem {
    
    private final Type type;
    private TWmRisk risk;
    private TWmRiskDetails riskDetails;

    public RiskTableItem(TWmRiskDetails pRiskDetails) {
        this.type = Type.RISK_DETAIL;
        this.riskDetails = pRiskDetails;
    }
    
    public RiskTableItem(TWmRisk pRisk) {
        this.type = Type.RISK;
        this.risk = pRisk;
    }

    public TWmRisk getRisk() {
        return risk;
    }

    public TWmRiskDetails getRiskDetails() {
        return riskDetails;
    }

    public Type getType() {
        return type;
    }
    public RiskAreaEn getRiskArea(){
        if(riskDetails == null){
            return null;
        }
        return riskDetails.getRiskArea();
    }
    public List<RiskTableItem> getRiskDetailItems(){
        if(risk == null){
            return new ArrayList<>();
        }
        return risk.getRiskDetails().stream().map((t) -> {
            return new RiskTableItem(t);
        }).collect(Collectors.toList());
    }
    public String getRiskComment(){
       if (RiskTableItem.Type.RISK.equals(getType())) {
            return getRisk().getRiskComment();
        } else {
            return getRiskDetails().getRiskComment();
        } 
    }
    public void setRiskComment(String pComment){
        if (RiskTableItem.Type.RISK.equals(getType())) {
            getRisk().setRiskComment(pComment);
        } else {
            getRiskDetails().setRiskComment(pComment);
        }
    }
    public enum Type{
        RISK,RISK_DETAIL;
    }
}
