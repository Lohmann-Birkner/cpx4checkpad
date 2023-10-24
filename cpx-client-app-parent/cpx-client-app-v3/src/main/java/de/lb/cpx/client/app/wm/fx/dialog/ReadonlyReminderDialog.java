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
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.wm.model.TWmReminder;
import javafx.scene.control.ButtonType;

/**
 * implementation of the an Readonly reminder dialog
 * @author wilde
 */
public class ReadonlyReminderDialog extends AddReminderDialog implements IReadOnlyDialog{
    
    public ReadonlyReminderDialog(ProcessServiceFacade pServiceFacade, TWmReminder pReminder) {
        super(pServiceFacade, pReminder);
        setTitle("Wiedervorlage anzeigen");
        disableController();
//        Button btnOk = getDialogSkin().getButton(ButtonType.OK);
        getDialogSkin().removeButton(ButtonType.OK);
    }

    @Override
    public void addReadOnlyReason(String pReadOnlyReason) {
        getCatalogValidationResult().add(CpxErrorTypeEn.WARNING, pReadOnlyReason, (Void e) -> true);
        setMessageText(getCatalogValidationResult().getValidationMessages());
        setMessageType(getCatalogValidationResult().getHighestErrorType());
    }
    
}
