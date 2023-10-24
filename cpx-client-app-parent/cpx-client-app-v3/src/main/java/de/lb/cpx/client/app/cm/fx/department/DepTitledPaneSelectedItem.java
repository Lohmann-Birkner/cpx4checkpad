/*
 * Copyright (c) 2021 Lohmann & Birkner.
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
 *    2021  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.cm.fx.department;

import de.lb.cpx.client.core.model.fx.titledpane.AccordionSelectedItem;
import de.lb.cpx.model.TCaseDepartment;
import de.lb.cpx.model.TCaseWard;

/**
 *
 * @author wilde
 */
public class DepTitledPaneSelectedItem extends AccordionSelectedItem<TCaseWard>{

    private final TCaseDepartment department;

    public DepTitledPaneSelectedItem(TCaseWard pItem,TCaseDepartment pDepartment) {
        super(pItem);
        department = pDepartment;
    }
    
    public TCaseDepartment getDepartment(){
        return department;
    }
    
    public boolean hasWard(){
        return getItem()!=null;
    }
    
    public boolean hasDepartment(){
        return getDepartment()!=null;
    }
}
