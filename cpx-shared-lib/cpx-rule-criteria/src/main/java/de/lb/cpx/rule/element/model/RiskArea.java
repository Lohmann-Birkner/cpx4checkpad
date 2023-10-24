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
package de.lb.cpx.rule.element.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wilde
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "risk_area")
public class RiskArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "risk_area_name")
    protected String riskAreaName;
    @XmlAttribute(name = "risk_waste_percent_value")
    protected String riskWastePercentValue;
    @XmlAttribute(name = "risk_audit_percent_value")
    protected String riskAuditPercentValue;
    @XmlAttribute(name = "risk_default_waste_value")
    protected String riskDefaultWasteValue;
    @XmlAttribute(name = "risk_comment")
    protected String riskComment;

    public String getRiskAreaName() {
        return riskAreaName;
    }

    public void setRiskAreaName(String riskAreaName) {
        this.riskAreaName = riskAreaName;
    }

    public String getRiskWastePercentValue() {
        return riskWastePercentValue;
    }

    public void setRiskWastePercentValue(String riskWastePercentValue) {
        this.riskWastePercentValue = riskWastePercentValue;
    }

    public String getRiskDefaultWasteValue() {
        return riskDefaultWasteValue;
    }

    public void setRiskDefaultWasteValue(String riskDefaultWasteValue) {
        this.riskDefaultWasteValue = riskDefaultWasteValue;
    }

    public String getRiskComment() {
        return riskComment;
    }

    public void setRiskComment(String riskComment) {
        this.riskComment = riskComment;
    }

    public String getRiskAuditPercentValue() {
        return riskAuditPercentValue;
    }

    public void setRiskAuditPercentValue(String riskAuditPercentValue) {
        this.riskAuditPercentValue = riskAuditPercentValue;
    }
    
    @Override
    public String toString() {
        return "\nRiskArea{ riskAreaName=" + riskAreaName
                + ", riskAuditPercentValue=" + riskAuditPercentValue
                + ", riskWastePercentValue=" + riskWastePercentValue
                + ", riskDefaultWasteValue=" + riskDefaultWasteValue
                + ", riskComment=" + riskComment
                + '}';
    }
    
}
