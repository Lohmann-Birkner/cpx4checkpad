/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.wm.model.TWmAction;

/**
 *
 * @author wilde
 */
public class CreateActionDialog extends TWmActionDialog {

    public CreateActionDialog(ProcessServiceFacade pFacade) {
        super("Aktion erstellen", pFacade);
    }

    @Override
    public TWmAction onSave() {
        getActionEntity().setActionType(getActionType().getWmAsInternalId());
        getActionEntity().setComment(getComment().toCharArray());
        getFacade().storeAction(getActionEntity());
        return getActionEntity();
    }

}
