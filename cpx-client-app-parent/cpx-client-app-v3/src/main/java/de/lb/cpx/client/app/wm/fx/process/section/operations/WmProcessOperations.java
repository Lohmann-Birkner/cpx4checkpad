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
 *    2019  niemeier - initial API and implementation and/or initial documentation
 */
package de.lb.cpx.client.app.wm.fx.process.section.operations;

import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.app.wm.fx.WmMainFrameFXMLController;
import de.lb.cpx.client.app.wm.fx.WmMainFrameScene;
import de.lb.cpx.client.core.MainApp;
import de.lb.cpx.client.core.model.CpxScene;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import javafx.event.ActionEvent;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 */
public abstract class WmProcessOperations extends WmOperations<TWmProcess> {

    public WmProcessOperations(ProcessServiceFacade pFacade) {
        super(pFacade);
    }

    @Override
    public String getItemName() {
        return Lang.getEventNameProcess();
    }

    @Override
    public ItemEventHandler openItem(TWmProcess pItem) {
        WmMainFrameFXMLController ctrl = getMainFrameController();
        if (ctrl == null) {
            return null;
        }
        return new ItemEventHandler(FontAwesome.Glyph.FOLDER_OPEN, Lang.getEventOperationOpen(), FontAwesome.Glyph.FOLDER_OPEN, Lang.getEventOperationOpenItem(getItemName()), true) {
            @Override
            public void handle(ActionEvent evt) {
                ctrl.getCbAssignedUserValue().show();
            }
        };
    }

    public final WmMainFrameFXMLController getMainFrameController() {
        CpxScene sc = MainApp.getToolbarMenuScene().getDisplayedScene();
        if (sc instanceof WmMainFrameScene) {
            return ((WmMainFrameScene) sc).getController();
        }
        return null;
    }

}
