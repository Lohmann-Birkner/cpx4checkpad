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
package de.lb.cpx.shared.json.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wilde
 */
public enum MessageReasonEn {
    
    VALIDATION_NO_VALUE(0, Lang.RULE_VALIDATION_NO_VALUE),
    VALIDATION_YEAR_NOT_VALID(1, Lang.RULE_VALIDATION_YEAR_NOT_VALID),
    VALIDATION_VALUE_NOT_IN_CATALOG(2, Lang.RULE_VALIDATION_VALUE_NOT_IN_CATALOG),
    VALIDATION_CHECK_CATALOG_TRANSFER_TABLE(3, Lang.RULE_VALIDATION_CHECK_CATALOG_TRANSFER_TABLE),
    VALIDATION_RULE_NO_CONTENT(4, Lang.RULE_VALIDATION_RULE_NO_CONTENT),
    VALIDATION_SUGG_NO_VALUE(5, Lang.RULE_VALIDATION_SUGG_NO_VALUE),
    VALIDATION_CODE_ERROR(6, Lang.RULE_VALIDATION_CODE_ERROR),
    VALIDATION_RULE_WITHOUT_TABLES(7, Lang.RULE_VALIDATION_RULE_WITHOUT_TABLES),
    VALIDATION_RULE_NOT_VALID(8, Lang.RULE_VALIDATION_RULE_NOT_VALID),
    VALIDATION_SUGG_MERGED_REASON(9, Lang.RULE_VALIDATION_SUGG_CHECK_BOTH);
    
    int ind;
    String mLangKey;
    private MessageReasonEn(int pInd, String pLangKey){
        ind = pInd;
        mLangKey = pLangKey;
    }
    private static final Map<String, MessageReasonEn> BY_INDEX = new HashMap<>();
    
    static {
        for (MessageReasonEn e: values()) {
            BY_INDEX.put(String.valueOf(e.ind), e);
        }
    }
    @Override
    public String toString(){
        return getTranslation().getValue();
    }
    
    public static final MessageReasonEn valueOfIndex(int pInd){
        return valueOfIndex(String.valueOf(pInd));
    }
    
    public static final MessageReasonEn valueOfIndex(String pInd){
        return BY_INDEX.get(pInd);
    }

    public String getLangKey() {
        return mLangKey;
    }


    public Translation getTranslation() {
       return getTranslation((Object[]) null);
    }


    public Translation getTranslation(Object... pParams) {
        return Lang.get(getLangKey(), pParams);
    }
    
    public String getIndexString(){
        return String.valueOf(ind);
    }
    


}
