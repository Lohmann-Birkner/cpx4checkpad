/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.model.enums;

import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.shared.lang.Translation;

/**
 *
 * @author gerschmann
 */
public enum BooleanEn implements CpxEnumInterface<Boolean>{
        SELECTED(1, true, Lang.RULE_COLUMN_RULE_SELECTED),
        NOT_SELECTED(0, false, Lang.RULE_COLUMN_RULE_NOT_SELECTED);
    
        private final boolean value;
        private final String langKey;
        private final int id ;
        
        private BooleanEn(int pId, boolean pValue, String pLangKey){
            value = pValue;
            langKey = pLangKey;
            id = pId;
        }
        
        @Override
        public String getLangKey() {
            return langKey;
        }
        @Override
        public Boolean getId(){
            return value;
        }
        
        @Override
        public int getIdInt(){
            return id;
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
            return String.valueOf(value);
        }
        

        @Override
        public String toString(){
            return Lang.get(getLangKey()).value;
        }
    } 

