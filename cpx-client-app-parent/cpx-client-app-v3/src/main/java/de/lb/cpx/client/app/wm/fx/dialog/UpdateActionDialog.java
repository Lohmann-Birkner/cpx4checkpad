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
 *    2019  wilde - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.wm.model.TWmAction;

/**
 *
 * @author wilde
 */
public class UpdateActionDialog extends TWmActionDialog {

    public UpdateActionDialog(TWmAction pAction, ProcessServiceFacade pFacade) {
        super("Aktion ändern", pFacade);
        setActionEntity(pAction);
    }

    @Override
    public TWmAction onSave() {
        getFacade().updateAction(getActionEntity());
        return getActionEntity();
    }

}
