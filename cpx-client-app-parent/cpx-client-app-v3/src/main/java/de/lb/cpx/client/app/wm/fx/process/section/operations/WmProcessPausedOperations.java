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
package de.lb.cpx.client.app.wm.fx.process.section.operations;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.WmMainFrameFXMLController;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.service.properties.RoleProperties;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author wilde
 */
public class WmProcessPausedOperations extends WmProcessOperations{
    public WmProcessPausedOperations(ProcessServiceFacade pFacade) {
        super(pFacade);
    }

    
    
//    @Override
//    public ItemEventHandler openItem(TWmProcess pItem) {
//        if (pItem == null) {
//            return null;
//        }
//        return new ItemEventHandler(FontAwesome.Glyph.PLAY, Lang.getEventOperationOpen(), FontAwesome.Glyph.PLAY, Lang.getEventOperationOpenItem(getItemName()), true) {
//            @Override
//            public void handle(ActionEvent evt) {
//                final WmMainFrameFXMLController ctrl = getMainFrameController();
//                if (ctrl == null) {
//                    return;
//                }
//                ctrl.getCbTypeValue().show();
//            }
//        };
//    }

    @Override
    public List<ItemEventHandler> getDefaultOperations(TWmProcess pItem) {
        List<ItemEventHandler> operations = new ArrayList<>();
        if(pItem == null){
            return operations;
        }
        if(getFacade().isProcessClosed()){
            return operations;
        }
        if(!getFacade().isProcessPaused()){
            return operations;
        }
        RoleProperties property = Session.instance().getRoleProperties();
        boolean canOpenFinalisation = property.modules.processManagement.rights.canDoFinalisation();
        if(!canOpenFinalisation){
            return operations;
        }
        operations.add(new ItemEventHandler(FontAwesome.Glyph.PLAY, Lang.getEventOperationOpen(), FontAwesome.Glyph.PLAY, Lang.getEventOperationOpenItem(getItemName()), true) {
            @Override
            public void handle(ActionEvent evt) {
                final WmMainFrameFXMLController ctrl = getMainFrameController();
                if (ctrl == null) {
                    return;
                }
                ConfirmDialog dialog = new ConfirmDialog(MainApp.getWindow(), "Wollen Sie den Vorgang wirklich fortsetzen?");
                dialog.show();
                dialog.resultProperty().addListener(new ChangeListener<ButtonType>() {
                    @Override
                    public void changed(ObservableValue<? extends ButtonType> ov, ButtonType t, ButtonType t1) {
                        if(ButtonType.YES.equals(t1)){
                            //continue event here
                            if(!getFacade().continueProcess()){
                                MainApp.showErrorMessageDialog("Der Vorgang konnte nicht fortgesetzt werden!");
                            }else{
                                getFacade().getProperties().put(WmMainFrameFXMLController.REFRESH_SUMMARY, null);
                            }
                        }
                    }
                });
            }
        });
        return operations;
    }
}
