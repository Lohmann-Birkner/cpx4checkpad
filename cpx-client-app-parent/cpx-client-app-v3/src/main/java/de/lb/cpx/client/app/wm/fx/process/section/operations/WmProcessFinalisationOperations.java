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

import de.lb.cpx.client.app.MainApp;
import de.lb.cpx.client.app.service.facade.ProcessServiceFacade;
import de.lb.cpx.client.core.config.Session;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab;
import de.lb.cpx.service.properties.RoleProperties;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcessHospitalFinalisation;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 */
public class WmProcessFinalisationOperations extends WmOperations<TWmProcessHospitalFinalisation> {

    private static final Logger LOG = Logger.getLogger(WmProcessFinalisationOperations.class.getName());

    public WmProcessFinalisationOperations(ProcessServiceFacade pFacade) {
        super(pFacade);
    }

    @Override
    public ItemEventHandler openItem(final TWmProcessHospitalFinalisation pItem) {
//        if (pItem == null) {
//            return null;
//        }
        RoleProperties property = Session.instance().getRoleProperties();
        boolean canOpenFinalisation = property.modules.processManagement.rights.canDoFinalisation();
        if (!canOpenFinalisation) {
            return null;
            //btnCompleteProcess.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), true);
        }
        return new ItemEventHandler(FontAwesome.Glyph.FOLDER_OPEN, Lang.getEventOperationOpen(), FontAwesome.Glyph.FLAG, Lang.getEventOperationOpenItem(getItemName()), true) {
            @Override
            public void handle(ActionEvent evt) {
//                btnCompleteProcess.setGraphic(ResourceLoader.getGlyph(FontAwesome.Glyph.FLAG));
//                btnCompleteProcess.setText(Lang.getProcessCompletion());
//                btnCompleteProcess.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
                if (facade.getCurrentProcess().getMainCase() == null) {
                    LOG.log(Level.WARNING, "no main case found for process {0}", facade.getCurrentProcess());
                    MainApp.showErrorMessageDialog(Lang.getProcessMainCaseError());
//                            MainApp.showInfoMessageDialog("Legen Sie in der Leistungsübersicht zunächst einen Basisfall fest!", getScene().getWindow());
                    return;
                }
//                        if (!canOpenFinalisation) {
//                            MainApp.showErrorMessageDialog(new SecurityException("Sie verfügen nicht über die notwendigen Rechte für diese Aktion!"));
//                            return;
//                        }
                facade.loadAndShow(TwoLineTab.TabType.PROCESSCOMPLETION, facade.getCurrentProcess().getMainCase().getId());
//                menu.hide();
            }
        };
    }

    @Override
    public String getItemName() {
        return Lang.getEventNameProcessFinalisation();
    }

}
