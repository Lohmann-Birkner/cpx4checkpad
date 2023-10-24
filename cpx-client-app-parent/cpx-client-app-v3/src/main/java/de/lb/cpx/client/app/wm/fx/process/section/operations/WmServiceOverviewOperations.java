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
import de.lb.cpx.client.app.wm.fx.dialog.AddServiceDialog;
import de.lb.cpx.client.core.model.fx.alert.AlertDialog;
import de.lb.cpx.client.core.model.fx.alert.ConfirmDialog;
import de.lb.cpx.client.core.model.fx.tab.TwoLineTab;
import de.lb.cpx.shared.lang.Lang;
import de.lb.cpx.wm.model.TWmProcessCase;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import org.controlsfx.glyphfont.FontAwesome;

/**
 *
 * @author niemeier
 */
public class WmServiceOverviewOperations extends WmOperations<TWmProcessCase> {

    public WmServiceOverviewOperations(ProcessServiceFacade pFacade) {
        super(pFacade);
    }
    
//    @Override
//    protected List<ItemOperation> getOtherOperations(final TWmProcessCase pItem) {
//        final List<ItemOperation> result = new ArrayList<>();
//        EventHandler<Event> openItem = openItem(pItem);
//        if (openItem != null) {
//            result.add(new ItemOperation(openItem, ResourceLoader.getGlyph(FontAwesome.Glyph.SEND), "Öffnen"));
//        }
//        return result;
//    }

    @Override
    public ItemEventHandler openItem(final TWmProcessCase pItem) {
        if (pItem == null) {
            return null;
        }
        //TWmProcessCase processCase = tvServices.getSelectionModel().getSelectedItem();
//        final TCase cs = processCase == null ? null : processCase.getHosCase();
//        if (cs == null) {
//            return null;
//        }
        return new ItemEventHandler(FontAwesome.Glyph.FOLDER_OPEN, Lang.getEventOperationOpen(), FontAwesome.Glyph.FILE, Lang.getEventOperationOpenItem(getItemName()), true) {
            @Override
            public void handle(ActionEvent evt) {
                if (facade.isCaseLocked(pItem.getId())) {
                    MainApp.showErrorMessageDialog(Lang.getItemLockedObj().getTooltip());
                    return;
                }
                facade.loadAndShow(TwoLineTab.TabType.CASE, pItem.getHosCase().getId());
            }
        };
    }

    @Override
    public ItemEventHandler createItem() {
        return new ItemEventHandler(FontAwesome.Glyph.FILE, Lang.getEventOperationCreate(), FontAwesome.Glyph.FILE, Lang.getEventOperationCreateItem(getItemName()), false) {
            @Override
            public void handle(ActionEvent evt) {
                AddServiceDialog dialog = new AddServiceDialog(facade);
                dialog.showAndWait();
//                reload();
            }
        };
    }

    @Override
    public ItemEventHandler removeItem(final TWmProcessCase pItem) {
//         final TWmProcessCase item = tvServices.getSelectionModel().getSelectedItem();
//        final TWmProcessCase pc = getProcessCaseForCase(pItem);
        if (pItem == null) {
            //LOG.log(Level.WARNING, "process case not found in list!");
            return null;
        }
//        final TCase cs = pItem.getHosCase();
        return new ItemEventHandler(FontAwesome.Glyph.TRASH, Lang.getEventOperationRemove(), FontAwesome.Glyph.TRASH, Lang.getEventOperationRemoveItem(getItemName()), false) {
            @Override
            public void handle(ActionEvent evt) {
                if (pItem.getMainCase()) {
                    AlertDialog alert = AlertDialog.createErrorDialog(Lang.getDeleteErrorCaseMainCase(), MainApp.getWindow());//new AlertDialog(Alert.AlertType.ERROR, "Is Maincase", ButtonType.OK);
                    alert.show();
                    return;
                }
                new ConfirmDialog(MainApp.getWindow(), Lang.getDeleteService()).showAndWait().ifPresent(new Consumer<ButtonType>() {
                    @Override
                    public void accept(ButtonType t) {
                        if (t.equals(ButtonType.YES)) {
                            facade.removeProcessCase(pItem);
                        }
//                        tvServices.reload();
                    }
                });
            }
        };
    }
    @Override
    public String getItemName() {
        return Lang.getEventNameCase();
    }

}
