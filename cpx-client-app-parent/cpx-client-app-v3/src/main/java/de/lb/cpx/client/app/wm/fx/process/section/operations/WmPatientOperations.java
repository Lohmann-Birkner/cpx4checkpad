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
import de.lb.cpx.client.core.config.CpxClientConfig;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab;
import de.lb.cpx.model.TPatient;
import de.lb.cpx.shared.lang.Lang;
import javafx.event.ActionEvent;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 */
public class WmPatientOperations extends WmOperations<TPatient> {

    public WmPatientOperations(ProcessServiceFacade pFacade) {
        super(pFacade);
    }

    @Override
    public ItemEventHandler openItem(TPatient pItem) {
        if (pItem == null) {
            return null;
        }
        if(!CpxClientConfig.instance().getCommonHealthStatusVisualization()){
            return null;
        }
        if(getFacade().checkAcgConnection()){  
            return new ItemEventHandler(FontAwesome.Glyph.EXTERNAL_LINK, Lang.getContextMenuOpenpatient(), FontAwesome.Glyph.EXTERNAL_LINK, Lang.getEventOperationOpenItem(getItemName()), true) {
                @Override
                public void handle(ActionEvent evt) {
                    facade.loadAndShow(TwoLineTab.TabType.PATIENT, pItem.getId());
                }
            };
        }else{
            return null;
        }
    }

    @Override
    public String getItemName() {
        return Lang.getEventNamePatient();
    }

}
