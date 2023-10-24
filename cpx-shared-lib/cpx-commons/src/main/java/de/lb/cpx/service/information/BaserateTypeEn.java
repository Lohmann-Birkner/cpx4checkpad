/*
 * Copyright (c) 2019 Lohmann & Birkner.
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
 *    2019  gerschmann - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.service.information;

/**
 *
 * @author gerschmann
 */
public enum BaserateTypeEn {
    PEPP("C1000000"),
    DRG ("70000000"),
    DRG_CARE("74000000");
    
    private String feeKey;
    private BaserateTypeEn(String key){
        feeKey = key;
    }
    
    public String getFeeKey(){
        return feeKey;
    }
}
