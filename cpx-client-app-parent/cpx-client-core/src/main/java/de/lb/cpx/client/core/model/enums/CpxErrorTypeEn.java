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
package de.lb.cpx.client.core.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author wilde
 */
public enum CpxErrorTypeEn {
    INFO(Lang.getInformationObj(),"skyblue","#edf7fc"),
    WARNING(Lang.getWarningObj(),"gold","lightyellow"),
    ERROR(Lang.getErrorObj(),"orangered","#ffe9e0"/*"#ffd0bf"*/);
    
    private final String backgroundColor;
    private final String iconColor;
    private final Translation translation;
    
    private CpxErrorTypeEn(Translation pTranslation,String pIconColor,String pBackgroundColor){
        translation = pTranslation;
        iconColor = pIconColor;
        backgroundColor = pBackgroundColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getIconColor() {
        return iconColor;
    }

    public Translation getTranslation() {
        return translation;
    }
    
}
