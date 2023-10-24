/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.ruleviewer.util;

import de.lb.cpx.client.core.util.IcdHelper;
import de.lb.cpx.model.TCaseIcd;
import de.lb.cpx.model.enums.IcdcRefTypeEn;
import de.lb.cpx.ruleviewer.analyser.attributes.AnalyserBeanProperty;
import de.lb.cpx.ruleviewer.analyser.attributes.FeeDataAttributes;
import de.lb.cpx.ruleviewer.analyser.attributes.IcdDataAttributes;
import de.lb.cpx.shared.lang.Lang;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

/**
 *
 * @author wilde
 */
public class AnalyserFormater {

    public final static String formatText(@NotNull AnalyserBeanProperty pProperty) {
        Objects.requireNonNull(pProperty, "Property can not be null");
        if (pProperty.getValue() == null) {
            return null;
        }
        if (pProperty.getType() == Date.class) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            return dateFormat.format((Date) pProperty.getValue());
        }
        if (pProperty.getType() == boolean.class) {
            return Boolean.TRUE.equals(pProperty.getValue()) ? "Ja" : "Nein";
        }
        if (pProperty.getType() == IcdcRefTypeEn.class) {
            pProperty.getBean();
            return getRefIcds((TCaseIcd) pProperty.getBean());
        }
        if (IcdDataAttributes.KEY_ICD_CODE.equals(pProperty.getAttribute().getKey())) {
            return getIcdCodeWithRef((TCaseIcd) pProperty.getBean());
        }
        if(FeeDataAttributes.KEY_FEE_VALUE.equals(pProperty.getAttribute().getKey())){
            return getCurrencyValue((Double)pProperty.getValue());
        }
        return String.valueOf(pProperty.getValue());
    }

    public static String getIcdCodeWithRef(TCaseIcd pIcd) {
        if (pIcd == null) {
            return "";
        }
        return pIcd.getIcdcCode().concat(getIcdRef(pIcd));
    }

    public static String getIcdRef(TCaseIcd pIcd) {
        if (pIcd == null) {
            return "";
        }
        if (pIcd.getIcdcReftypeEn() == null) {
            return "";
        }
        switch (pIcd.getIcdcReftypeEn()) {
            case Kreuz:
                return "+";
            case Stern:
                return "*";
            case ZusatzZu:
                return "!";
            default:
                return "";
        }
    }

    public static String getRefIcds(TCaseIcd pIcd) {
        if (!IcdHelper.isPrimRefType(pIcd.getIcdcReftypeEn())) {
            return getIcdCodeWithRef(pIcd.getRefIcd());
        }
        return pIcd.getRefIcds().stream().map(AnalyserFormater::getIcdCodeWithRef).collect(Collectors.joining(", "));
    }

    private static String getCurrencyValue(Double aDouble) {
        return Lang.toDecimal(aDouble, 2) + " "+ Lang.getCurrencySymbol();
    }

}
