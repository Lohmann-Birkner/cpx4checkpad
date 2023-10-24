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
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcess;
import javafx.event.ActionEvent;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 */
public class WmProcessUserChangedOperations extends WmProcessOperations {

    public WmProcessUserChangedOperations(ProcessServiceFacade pFacade) {
        super(pFacade);
    }

    @Override
    public ItemEventHandler openItem(TWmProcess pItem) {
        if (pItem == null) {
            return null;
        }
        return new ItemEventHandler(FontAwesome.Glyph.USERS, Lang.getEventOperationOpen(), FontAwesome.Glyph.USERS, Lang.getEventOperationChangeAssignedUser(), true) {
            @Override
            public void handle(ActionEvent evt) {
                final WmMainFrameFXMLController ctrl = getMainFrameController();
                if (ctrl == null) {
                    return;
                }
                ctrl.getCbAssignedUserValue().show();
            }
        };
    }

}
