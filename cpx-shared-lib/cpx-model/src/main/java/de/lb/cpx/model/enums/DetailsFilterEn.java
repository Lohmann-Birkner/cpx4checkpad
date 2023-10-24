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
public enum DetailsFilterEn implements CpxEnumInterface<String> {

     ACTUAL_LOCAL(Lang.getMenuBatchGroupingDetailsFilterActualLocal()),
     ACTUAL_BOTH(Lang.getMenuBatchGroupingDetailsFilterActualBoth()),
     ALL(Lang.getMenuBatchGroupingDetailsFilterAll()) ;
     
     private final String langKey; 

     private DetailsFilterEn(String pLangKey){
         langKey = pLangKey;
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
       return name();
    }

    @Override
    public boolean isViewRelevant() {
        return true;
    }

    @Override
    public String getIdStr() {
        return name();
    }

    @Override
    public int getIdInt() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getId() {
        return name();
    }
    
}
