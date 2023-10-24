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
package de.lb.cpx.client.app.cm.fx.details;

import de.lb.cpx.client.core.model.fx.button.DeleteButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 *
 * @author wilde
 */
public class SettedRiskCell extends CmRiskDocumentationListCell{

    private final DeleteButton btnDelete;
    
    public SettedRiskCell(CheckMode pType) {
        super(pType);
        btnDelete = new DeleteButton();
        setMenuItems(btnDelete);
    }
    
    public void setOnDelete(EventHandler<ActionEvent> pHandler){
        btnDelete.setOnAction(pHandler);
    }
}
