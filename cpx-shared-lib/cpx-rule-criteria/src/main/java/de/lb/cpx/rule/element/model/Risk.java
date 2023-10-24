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
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wilde
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "risk")
public class Risk implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @XmlElement(name = "risk_area", type = RiskArea.class)
    protected final List<RiskArea> riskArea;// = new ArrayList<>();

    public Risk() {
        riskArea = new ArrayList<>();
    }

    public List<RiskArea> getRiskAreas() {
        return riskArea;
    }
    
    @Override
    public String toString() {
        return "risks{" + "risks=" + getListValue(riskArea) + '}';
    }

    public String getListValue(List<RiskArea> elements) {
        String values = "";
        for (Object obj : elements) {
            values += obj.toString();
        }
        return values;
    }
}
