/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lb.cpx.client.app.wm.fx.dialog;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.model.enums.CpxErrorTypeEn;
import de.lb.cpx.wm.model.TWmAction;
import javafx.scene.control.ButtonType;

/**
 *
 * @author gerschmann
 */
public class ReadonlyActionDialog extends TWmActionDialog implements IReadOnlyDialog{
    
    
    public ReadonlyActionDialog(TWmAction pAction, ProcessServiceFacade pFacade){
        super("Aktion anzeigen", pFacade);
        setActionEntity(pAction);
        deactivateFields();
        
    }

    @Override
    public TWmAction onSave() {
       return getActionEntity();
    }

    private void deactivateFields() {
        
        taComment.getControl().disableProperty().setValue(Boolean.TRUE);
        cbActionType.getControl().disableProperty().setValue(Boolean.TRUE);
        this.getDialogSkin().getButton(ButtonType.OK).setVisible(false);
        this.getDialogSkin().getButton(ButtonType.CANCEL).setText("Schließen");

        
    }

    @Override
    protected void addControls(){
//        setMessageType(CpxErrorTypeEn.WARNING);
//        setMessageText(Lang.getActionMsgNoEditRight());

        addControls( cbActionType, taComment);
    }

    @Override
    public void addReadOnlyReason(String pReadOnlyReason) {
        getCatalogValidationResult().add(CpxErrorTypeEn.WARNING, pReadOnlyReason, (Void e)->true);
        setMessageText(getCatalogValidationResult().getValidationMessages());
        setMessageType(getCatalogValidationResult().getHighestErrorType());
    }

    
}
