/*
 * Copyright (c) 2020 Lohmann & Birkner.
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
 *    2020  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.server.commonDB.model.enums;

import de.lb.cpx.model.enums.CpxEnumInterface;
import de.lb.cpx.service.information.CatalogTypeEn;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author gerschmann
 */
public enum RuleTableCategoryEn implements CpxEnumInterface<String> {
    
    ICD(Lang.CASE_RESOLVE_ICD),
    OPS(Lang.CASE_RESOLVE_OPS), 
    OTHER(Lang.RISK_AREA_OTHER), 
    NOT_SET(Lang.REMINDER_DUEDATE_IS_BLANK);
    
    private final String langKey;
    
    private RuleTableCategoryEn(String pDefinition){
        langKey = pDefinition;
    }
    
    public static CatalogTypeEn getType2CatalogType(RuleTableCategoryEn pType)
    {
        if(pType == null){
            return null;
        }
        switch(pType){
            case ICD: return CatalogTypeEn.ICD_TRANSFER;
            case OPS: return CatalogTypeEn.OPS_TRANSFER;
            default: return null;
        }
    }
    
    @Override
    public String getLangKey() {
        return langKey;
    }

    public static boolean isCriterium4Check(String pCritName){
        return (pCritName.endsWith("iagnose") || pCritName.endsWith("rozedur"))
                   && !pCritName.contains("Anzahl")
                   && !pCritName.contains("Lokalisation")
                   && !pCritName.contains("Type");
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isViewRelevant() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getIdStr() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getIdInt() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static RuleTableCategoryEn getTableCategory2crit(String critName){
        if(critName == null || critName.isEmpty() || !isCriterium4Check(critName)){
            return NOT_SET;
        }
        if(critName.indexOf("iagnose")>0){
            return RuleTableCategoryEn.ICD;
        }else  if(critName.indexOf("rozedur")> 0){
            return RuleTableCategoryEn.OPS;
        }else{
           return RuleTableCategoryEn.OTHER;
        }

    }
}
