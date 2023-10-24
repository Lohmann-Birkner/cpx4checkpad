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

import de.lb.cpx.client.core.util.VersionStringConverter;
import de.lb.cpx.model.TCaseDetails;

/**
 *
 * @author wilde
 */
public class RiskDocumentation4Version extends RiskDocumentationItem{

    private final boolean isActual;
    private final TCaseDetails details;

    public RiskDocumentation4Version(TCaseDetails pCaseDetails,boolean pIsActual) {
        super();
        details = pCaseDetails;
        isActual = pIsActual;
    }
    
    @Override
    public String getTitle() {
        return VersionStringConverter.convert(details, VersionStringConverter.DisplayMode.SIMPLE);
    }
    
    public String getDescription(){
        return new StringBuilder("Wird verwendet ").append(isActual ? "Ja" : "Nein")/*.append(" ").append(caseDetails.getCsdVersRiskTypeEn().getTranslation().getValue())*/.toString();
//        return Lang.getDocumentationMenuIsActive() + (isActual ? "Ja" : "Nein") + " " + caseDetails.getCsdVersRiskTypeEn().getTranslation().getValue();
    }

    public TCaseDetails getDetails() {
        return details;
    }

    public boolean isActual() {
        return isActual;
    }
    
}
