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
package de.lb.cpx.client.app.cm.fx.documentation.risk.tree;

import de.lb.cpx.model.enums.VersionRiskTypeEn;

/**
 *
 * @author wilde
 */
public class RiskDocumentionCategory extends RiskDocumentationItem{

    private final VersionRiskTypeEn type;
    private final int count;

    public RiskDocumentionCategory(VersionRiskTypeEn pType, int pContentCount) {
        type = pType;
        count = pContentCount;
    }
    
    @Override
    public String getTitle() {
        return new StringBuilder(getTypeString()).append(" ").append(getCountString()).toString();
    }
    
    public VersionRiskTypeEn getType() {
        return type;
    }

    public int getCount() {
        return count;
    }
    
    private String getTypeString(){
        return type!=null?type.getTranslation().getValue():"Unbekannt";
    }
    
    private String getCountString(){
        if(count == 0){
            return "";
        }
        return new StringBuilder().append("(").append(count).append(")").toString();
    }
}
