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
package de.lb.cpx.client.app.wm.fx.dialog.editor;

import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 *
 * @author wilde
 */
public class CatalogValidationMessage implements Comparable<CatalogValidationMessage>{
    
    public static final Comparator<CatalogValidationMessage> COMPARATOR = (CatalogValidationMessage vm1, CatalogValidationMessage vm2) -> {
        if (vm1 == vm2) {
            return 0;
        }
        if (vm1 == null) {
            return 1;
        }
        if (vm2 == null) {
            return -1;
        }
        return vm1.compareTo(vm2);
    };
    
    private final CpxErrorTypeEn type;
    private final String message;
    private final Predicate<Void> predicate;
    
    public CatalogValidationMessage(CpxErrorTypeEn pType, String pMessage, Predicate<Void> pPredicate){
        this.type = pType;
        this.message = pMessage;
        this.predicate = pPredicate;
    }

    public CpxErrorTypeEn getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Predicate<Void> getPredicate() {
        return predicate;
    }
    
    @Override
    public int compareTo(CatalogValidationMessage msg) {
        return msg == null ? -1: getType().compareTo(msg.getType());
    }
}
