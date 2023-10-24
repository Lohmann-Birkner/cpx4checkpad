/*
 * Copyright (c) 2022 Lohmann & Birkner.
 * NOTICE:  All information contained herein is, and remains
 * the property of Lohmann & Birkner and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Lohmann & Birkner
 * and its suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Lohmann & Birkner.
 * http://www.lohmann-birkner.de/de/index.php
 *
 * Contributors:
 *    2022  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author gerschmann
 */
public enum BillTypeEn  implements CpxEnumInterface<String> {
    partialBill("1", Lang.BILL_TYPE_ENUM_PARTIAL),
    finalBill("2", Lang.BILL_TYPE_ENUM_FINAL),
    supplBill("3", Lang.BILL_TYPE_ENUM_SUPPL),
    creditBill("4", Lang.BILL_TYPE_ENUM_CREDIT_STORNO),
    reminderBill("5", Lang.BILL_TYPE_ENUM_REMINDER),
    firstWarningBill("6", Lang.BILL_TYPE_ENUM_1_WARNING),
    secondWarningBill("7", Lang.BILL_TYPE_ENUM_2_WARNING),
    forecastBill("8", Lang.BILL_TYPE_ENUM_FORECAST);
    
    private final String id;
    private final String langKey;
    
    private BillTypeEn(String id, String langKey){
        this.id = id;
        this.langKey = langKey;
    }

    @Override
    public String getLangKey() {
        return langKey;
    }

    @Override
    public Translation getTranslation() {
        return getTranslation((Object[]) null);
    }

    @Override
    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }

    @Override
    public String getViewId() {
        return String.valueOf(id);
    }

    @Override
    public boolean isViewRelevant() {
        return true;
    }
    @Override
    public String getIdStr() {
        return id;
    }

    @Override
    public int getIdInt() {
        return 0;
    }

    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String toString(){
        return id + " - " + getTranslation();
    }
    
    /**
     *
     * @param pString
     * @return
     */
    public static BillTypeEn valueTo(String pString){
        if(pString == null || pString.isEmpty()){
            return finalBill;
        }
        String parts[] = pString.split(" - ");
        for(BillTypeEn type: values()){
            if(type.getId().equals(parts[0])){
                return type;
            }
        }
        return finalBill;
    }
    
}
